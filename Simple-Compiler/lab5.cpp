#include "llvm/ADT/APFloat.h"
#include "llvm/ADT/APInt.h"
#include "llvm/ADT/STLExtras.h"
#include "llvm/IR/BasicBlock.h"
#include "llvm/IR/Constants.h"
#include "llvm/IR/DerivedTypes.h"
#include "llvm/IR/Function.h"
#include "llvm/IR/IRBuilder.h"
#include "llvm/IR/LLVMContext.h"
#include "llvm/IR/LegacyPassManager.h"
#include "llvm/IR/Module.h"
#include "llvm/IR/Type.h"
#include "llvm/IR/Verifier.h"
#include "llvm/Support/FileSystem.h"
#include "llvm/Support/Host.h"
#include "llvm/Support/TargetRegistry.h"
#include "llvm/Support/TargetSelect.h"
#include "llvm/Support/raw_ostream.h"
#include "llvm/Target/TargetMachine.h"
#include "llvm/Target/TargetOptions.h"
#include <algorithm>
#include <cassert>
#include <cctype>
#include <cstdint>
#include <cstdio>
#include <cstdlib>
#include <iostream>
#include <map>
#include <memory>
#include <string>
#include <vector>
using namespace llvm;
using namespace llvm::sys;

//===----------------------------------------------------------------------===//
// Lexer
//===----------------------------------------------------------------------===//

// The lexer returns tokens [0-255] if it is an unknown character, otherwise one
// of these for known things.
enum Token {
    tok_eof = -1,

    // commands
    tok_def = -2,
    tok_extern = -3,

    // primary
    tok_identifier = -4,    // [A-Z_a-z][0-9A-Z_a-z]*
    tok_number_int = -5,    // [0-9][0-9]*
    tok_number_double = -6, // <intconst>.<intconst>

    // function
    tok_return = -7,

    // control flow
    tok_if = -8,
    tok_else = -9,
    tok_while = -10,
    tok_for = -11,
};

enum Types { type_int = 1, type_double = 2 };

static std::map<std::string, Types> TypeOfString; // Map typeString to int
static std::map<Types, std::string> StringOfType; // Map typeString to int
static FILE *fip;
static std::string IdentifierStr; // Filled in if tok_identifier
static int NumValI;               // Filled in if tok_number_int
static double NumValD;            // Filled in if tok_number_double
static Types ValType;             // Filled in if tok_def
std::string ASTHeadIdent = "";

static void InitializeTypeValue() {
    TypeOfString["int"] = type_int;
    TypeOfString["double"] = type_double;
    StringOfType[type_int] = "int";
    StringOfType[type_double] = "double";
}

/// gettok - Return the next token from standard input.
static int gettok() {
    static int LastChar = ' ';
    std::map<std::string, int>::iterator iter;
    // Skip any whitespace.
    while (isspace(LastChar)) {
        LastChar = fgetc(fip);
    }
    if (isalpha(LastChar)) {
        //-------------------------------------------
        // Use finite automaton
        std::string identifierRes = "";
        identifierRes.push_back(LastChar);
        LastChar = fgetc(fip);
        while (isalpha(LastChar) || isdigit(LastChar)) {
            identifierRes.push_back(LastChar);
            LastChar = fgetc(fip);
        }
        if (identifierRes == "return") {
            return tok_return;
        } else if (identifierRes == "extern") {
            return tok_extern;
        } else if (identifierRes == "int" || identifierRes == "double") {
            ValType = TypeOfString[identifierRes];
            return tok_def;
        } else if (identifierRes == "if") {
            return tok_if;
        } else if (identifierRes == "else") {
            return tok_else;
        } else if (identifierRes == "while") {
            return tok_while;
        } else if (identifierRes == "for") {
            return tok_for;
        } else {
            // std::cout << "IdentifierStr" << IdentifierStr << std::endl;
            IdentifierStr = identifierRes;
            return tok_identifier;
        }
    }
    if (isdigit(LastChar)) {
        bool dotted = false;
        std::string res = "";
        //-----------------------------------
        // TODO: number_int // number_double
        //-----------------------------------
        while (isdigit(LastChar) || LastChar == '.') {
            if (LastChar == '.') {
                dotted = true;
            }
            res += LastChar;
            LastChar = fgetc(fip);
        }
        if (dotted) {
            NumValD = atof(res.c_str());
            return tok_number_double;
        } else {
            NumValI = atoi(res.c_str());
            return tok_number_int;
        }
    }
    // Check for end of file.
    if (LastChar == EOF)
        return tok_eof;
    // Otherwise, just return the character as its ascii value.
    int ThisChar = LastChar;
    LastChar = fgetc(fip);
    return ThisChar;
}

//===----------------------------------------------------------------------===//
// Abstract Syntax Tree (aka Parse Tree)
//===----------------------------------------------------------------------===//
// you don't have to modify this part. (of course it is ok to add something if
// needed)
namespace {

/// ExprAST - Base class for all expression nodes.
class ExprAST {
  public:
    virtual ~ExprAST() = default;
    virtual Value *codegen() = 0;
};

/// NumberExprAST - Expression class for numeric literals like "1.0".
class NumberDoubleExprAST : public ExprAST {
    double Val;

  public:
    NumberDoubleExprAST(double Val) : Val(Val) {}

    Value *codegen() override;
};

class NumberIntExprAST : public ExprAST {
    int Val;

  public:
    NumberIntExprAST(int Val) : Val(Val) {}

    Value *codegen() override;
};

/// VariableExprAST - Expression class for referencing a variable, like "a".
class VariableExprAST : public ExprAST {
    std::string Name;

  public:
    VariableExprAST(const std::string &Name) : Name(Name) {}

    Value *codegen() override;
};

/// BinaryExprAST - Expression class for a binary operation expression.
class BinaryExprAST : public ExprAST {
    char Op;
    std::unique_ptr<ExprAST> LHS, RHS;

  public:
    BinaryExprAST(char Op, std::unique_ptr<ExprAST> LHS,
                  std::unique_ptr<ExprAST> RHS)
        : Op(Op), LHS(std::move(LHS)), RHS(std::move(RHS)) {}

    Value *codegen() override;
};

/// CallExprAST - Expression class for function calls.
class CallExprAST : public ExprAST {
    std::string Callee;
    std::vector<std::unique_ptr<ExprAST>> Args;

  public:
    CallExprAST(const std::string &Callee,
                std::vector<std::unique_ptr<ExprAST>> Args)
        : Callee(Callee), Args(std::move(Args)) {}

    Value *codegen() override;
};

/// StmtAST - Base class for all statement nodes.
class StmtAST {
  public:
    virtual ~StmtAST() = default;

    virtual Value *codegen() = 0;
    virtual bool blockend() { return false; }
};

/// DeclStmtAST - Statement class for declaration.
class DeclStmtAST : public StmtAST {
    std::string FnName;
    Types declaredType;
    std::vector<std::string> declatedElements;

  public:
    DeclStmtAST(std::string FnName, Types declaredType,
                std::vector<std::string> declatedElements)
        : FnName(FnName), declaredType(declaredType),
          declatedElements(declatedElements) {}

    Value *codegen() override;
};

/// AssignStmtAST - Statement class for assignment statement "=".
class AssignStmtAST : public StmtAST {
    std::string Ident;
    std::unique_ptr<ExprAST> RHS;

  public:
    AssignStmtAST(std::string Ident, std::unique_ptr<ExprAST> RHS)
        : Ident(Ident), RHS(std::move(RHS)) {}

    Value *codegen() override;
};

/// RetStmtAST - For returning statement.
class RetStmtAST : public StmtAST {
    std::unique_ptr<ExprAST> Ret;

  public:
    RetStmtAST(std::unique_ptr<ExprAST> Ret) : Ret(std::move(Ret)) {}

    Value *codegen() override;
    bool blockend() override { return true; }
};

/// IfElseAST - For if-else structure statement.
class IfElseAST : public StmtAST {
    std::unique_ptr<ExprAST> Condition;
    std::vector<std::unique_ptr<StmtAST>> ThenStmts;
    std::vector<std::unique_ptr<StmtAST>> FalseStmts;

  public:
    IfElseAST(std::unique_ptr<ExprAST> Condition,
              std::vector<std::unique_ptr<StmtAST>> ThenStmts,
              std::vector<std::unique_ptr<StmtAST>> FalseStmts)
        : Condition(std::move(Condition)), ThenStmts(std::move(ThenStmts)),
          FalseStmts(std::move(FalseStmts)) {}

    Value *codegen() override;
    bool is_end_of_then_blocks() {
        return ThenStmts.size() > 0 &&
               ThenStmts.at(ThenStmts.size() - 1)->blockend();
    }
    bool is_end_of_false_blocks() {
        return FalseStmts.size() > 0 &&
               FalseStmts.at(FalseStmts.size() - 1)->blockend();
    }
};

class WhileAST : public StmtAST {
    std::unique_ptr<ExprAST> Condition;
    std::vector<std::unique_ptr<StmtAST>> Stmts;

  public:
    WhileAST(std::unique_ptr<ExprAST> Condition,
             std::vector<std::unique_ptr<StmtAST>> Stmts)
        : Condition(std::move(Condition)), Stmts(std::move(Stmts)) {}

    Value *codegen() override;
};

class ForExprAST : public StmtAST {
    std::unique_ptr<StmtAST> InitStmt;
    std::unique_ptr<ExprAST> Condition;
    std::unique_ptr<StmtAST> Update;
    std::vector<std::unique_ptr<StmtAST>> Stmts;

  public:
    ForExprAST(std::unique_ptr<StmtAST> InitStmt,
               std::unique_ptr<ExprAST> Condition,
               std::unique_ptr<StmtAST> Update,
               std::vector<std::unique_ptr<StmtAST>> Stmts)
        : InitStmt(std::move(InitStmt)), Condition(std::move(Condition)),
          Update(std::move(Update)), Stmts(std::move(Stmts)) {}

    Value *codegen() override;
};

/// PrototypeAST - This class represents the "prototype" for a function,
/// which captures its name, and its argument names (thus implicitly the number
/// of arguments the function takes).
class PrototypeAST {
    std::string Name;
    std::vector<std::string> Args;
    std::vector<Types> ArgTypes;
    Types FnType;

  public:
    PrototypeAST(const std::string &Name, std::vector<std::string> Args,
                 std::vector<Types> ArgTypes, Types FnType)
        : Name(Name), Args(std::move(Args)), ArgTypes(std::move(ArgTypes)),
          FnType(FnType) {}

    Function *codegen();
    const std::string &getName() const { return Name; }
    const Types getReturnType() { return FnType; }
    const std::vector<Types> &getArgTypes() { return ArgTypes; }
};

/// BodyAST - This class represents the body for a function.
class BodyAST {
    std::vector<std::unique_ptr<StmtAST>> Stmts;

  public:
    BodyAST(std::vector<std::unique_ptr<StmtAST>> Stmts)
        : Stmts(std::move(Stmts)) {}

    Value *codegen();
};

/// FunctionAST - This class represents a function definition itself.
class FunctionAST {
    std::unique_ptr<PrototypeAST> Proto;
    std::unique_ptr<BodyAST> Body;

  public:
    FunctionAST(std::unique_ptr<PrototypeAST> Proto,
                std::unique_ptr<BodyAST> Body)
        : Proto(std::move(Proto)), Body(std::move(Body)) {}

    Function *codegen();
};

} // end anonymous namespace

//===----------------------------------------------------------------------===//
// Parser
//===----------------------------------------------------------------------===//

/// CurTok/getNextToken - Provide a simple token buffer.  CurTok is the current
/// token the parser is looking at.  getNextToken reads another token from the
/// lexer and updates CurTok with its results.
static int CurTok;
static int getNextToken() { return CurTok = gettok(); }

/// BinopPrecedence - This holds the precedence for each binary operator that is
/// defined.
static std::map<char, int> BinopPrecedence;

/// LogError* - These are little helper functions for error handling.
/// you can add additional function to help you log error.
std::unique_ptr<ExprAST> LogError(const char *Str) {
    fprintf(stderr, "Error: %s\n", Str);
    return nullptr;
}

std::unique_ptr<PrototypeAST> LogErrorP(const char *Str) {
    LogError(Str);
    return nullptr;
}

std::unique_ptr<FunctionAST> LogErrorF(const char *Str) {
    LogError(Str);
    return nullptr;
}

std::unique_ptr<StmtAST> LogErrorS(const char *Str) {
    LogError(Str);
    return nullptr;
}

std::unique_ptr<BodyAST> LogErrorB(const char *Str) {
    LogError(Str);
    return nullptr;
}

/*TODO: Finish the Parse*() function to implement the Parser.
  We provide some implemented Parse* function for reference, like
  ParseNumberExpr(), ParseExtern(), which are marked with "example", and you can
  use these functions directly.
  >>>note: You can add some other Parse*() function to help you achieve this
  goal,
  >>>e.g. ParseParenExpr() which parenexpr ::= '(' expression ')'.
*/

static std::unique_ptr<ExprAST> ParseExpression(int precedence);
static std::unique_ptr<StmtAST> ParseStatement(std::string FnName);

/// numberexpr ::= number
/// example
static std::unique_ptr<ExprAST> ParseNumberExpr(int NumberType) {
    if (NumberType == type_double) {
        auto result = std::make_unique<NumberDoubleExprAST>(NumValD);
        getNextToken(); // consume the number
        return std::move(result);
    } else {
        auto result = std::make_unique<NumberIntExprAST>(NumValI);
        getNextToken(); // consume the number
        return std::move(result);
    }
}

static std::unique_ptr<ExprAST> ParseIdentifierExpr() {
    std::string IdName = IdentifierStr;
    getNextToken(); // consume identifier

    if (CurTok != '(')
        return std::make_unique<VariableExprAST>(IdName);
    getNextToken(); // consume '('

    std::vector<std::unique_ptr<ExprAST>> Args;
    if (CurTok != ')') {
        Args.push_back(ParseExpression(0));
        while (CurTok == ',') {
            getNextToken(); // consume ','
            Args.push_back(ParseExpression(0));
        }
    }

    if (CurTok != ')')
        return LogError("Expected ')' in callee");
    getNextToken(); // consume ')'

    return std::make_unique<CallExprAST>(IdName, std::move(Args));
}

static std::unique_ptr<ExprAST> ParseExpression(int precedence) {
    std::unique_ptr<ExprAST> result;
    if (CurTok == '(') {
        getNextToken(); // consume '('
        result = ParseExpression(0);
        if (CurTok != ')')
            return LogError("Expected ')' in expression");
        getNextToken(); // consume ')'
    } else if (CurTok == tok_number_int || CurTok == tok_number_double) {
        result = ParseNumberExpr(-CurTok - 4);
    } else if (CurTok == tok_identifier) {
        result = ParseIdentifierExpr();
    } else {
        return LogError("Invalid expression input");
    }

    while (CurTok == '+' || CurTok == '-' || CurTok == '*' || CurTok == '<') {
        char op = CurTok;
        int rp = BinopPrecedence[op];
        int lp = rp - 1;
        if (lp < precedence)
            break;
        getNextToken();
        auto Right = ParseExpression(rp);
        result = std::make_unique<BinaryExprAST>(op, std::move(result),
                                                 std::move(Right));
    }

    return result;
}

static std::unique_ptr<StmtAST> ParseDeclaringStatement(std::string FnName) {
    Types declaredType = ValType;
    getNextToken(); // consume <type>

    if (CurTok != tok_identifier)
        return LogErrorS("Expected variable name in declaration");
    std::vector<std::string> DeclNames;
    DeclNames.push_back(IdentifierStr);
    getNextToken(); // consume <ident>

    while (CurTok == ',') {
        getNextToken(); // consume ','
        if (CurTok != tok_identifier)
            return LogErrorS("Expected valid variable name in declaration");
        DeclNames.push_back(IdentifierStr);
        getNextToken(); // consume <ident>
    }

    if (CurTok != ';') {
        return LogErrorS("Expected ';' after a statement");
    }
    getNextToken(); // consume ';'

    return std::make_unique<DeclStmtAST>(FnName, declaredType, DeclNames);
}

static std::unique_ptr<StmtAST> ParseAssigningStatement(bool EndSemi = true) {
    std::string ident = IdentifierStr;
    getNextToken(); // consume <ident>

    if (CurTok != '=') {
        return LogErrorS("Expected '=' in assignment statement");
    }
    getNextToken(); // consume '='

    auto E = ParseExpression(0);

    if (EndSemi) {
        if (CurTok != ';') {
            return LogErrorS("Expected ';' after a statement");
        }
        getNextToken(); // consume ';'
    }

    return std::make_unique<AssignStmtAST>(ident, std::move(E));
}

static std::unique_ptr<StmtAST> ParseReturningStatement() {
    getNextToken(); // consume "return"

    auto E = ParseExpression(0);

    if (CurTok != ';') {
        return LogErrorS("Expected ';' after a statement");
    }
    getNextToken(); // consume ';'

    return std::make_unique<RetStmtAST>(std::move(E));
}

// Parse nested if else
static std::unique_ptr<StmtAST> ParseIfAndElseStatement(std::string FnName) {
    std::vector<std::unique_ptr<StmtAST>> ThenStmts, FalseStmts;
    getNextToken(); // consume the "if"
    if (CurTok != '(') {
        return LogErrorS("Expect expression after keyword if");
    }
    getNextToken(); // consume '('
    // condition.
    auto Condition = ParseExpression(0);
    if (CurTok != ')') {
        return LogErrorS("')' missing after keyword if");
    }
    getNextToken(); // consume ')'
    if (CurTok == '{') {
        getNextToken(); // consume '{'
        while (CurTok != '}')
            ThenStmts.push_back(ParseStatement(FnName));
        getNextToken(); // consume '}'
    } else
        ThenStmts.push_back(ParseStatement(FnName));
    if (CurTok == tok_else) {
        getNextToken(); // consume "else"
        if (CurTok == '{') {
            getNextToken(); // consume '{'
            while (CurTok != '}')
                FalseStmts.push_back(ParseStatement(FnName));
            getNextToken(); // consume '}'
        } else
            FalseStmts.push_back(ParseStatement(FnName));
    }
    return std::make_unique<IfElseAST>(
        std::move(Condition), std::move(ThenStmts), std::move(FalseStmts));
}

static std::unique_ptr<StmtAST> ParseWhileStatement(std::string FnName) {
    std::vector<std::unique_ptr<StmtAST>> Body;
    getNextToken(); // consume "while"
    if (CurTok != '(') {
        return LogErrorS("Expect expression after while");
    }

    getNextToken(); // consume '('
    auto Condition = ParseExpression(0);

    if (CurTok != ')') {
        return LogErrorS("')' missing after while");
    }
    getNextToken(); // consume ')'

    if (CurTok == '{') {
        getNextToken(); // consume '{'
        while (CurTok != '}')
            Body.push_back(ParseStatement(FnName));
        getNextToken(); // consume '}'
    } else
        Body.push_back(ParseStatement(FnName));

    return std::make_unique<WhileAST>(std::move(Condition), std::move(Body));
}

static std::unique_ptr<StmtAST> ParseForStatement(std::string FnName) {
    std::vector<std::unique_ptr<StmtAST>> Body;
    getNextToken(); // consume "for"
    if (CurTok != '(') {
        return LogErrorS("Expect '(' after keyword for");
    }
    getNextToken(); // consume "("
    auto Initial = ParseStatement(FnName);
    auto Condition = ParseExpression(0);
    if (CurTok != ';') {
        return LogErrorS("Expect ';' after expression in for statement");
    }
    getNextToken(); // consume ";"
    auto Update = ParseAssigningStatement(false);
    if (CurTok != ')') {
        return LogErrorS("')' missing in for statement");
    }
    getNextToken(); // consume ")"

    if (CurTok == '{') {
        getNextToken(); // consume '{'
        while (CurTok != '}')
            Body.push_back(ParseStatement(FnName));
        getNextToken(); // consume '}'
    } else
        Body.push_back(ParseStatement(FnName));

    return std::make_unique<ForExprAST>(std::move(Initial),
                                        std::move(Condition), std::move(Update),
                                        std::move(Body));
}

static std::unique_ptr<StmtAST> ParseStatement(std::string FnName) {
    switch (CurTok) {
    case tok_def:
        return ParseDeclaringStatement(FnName);
        break;
    case tok_if:
        return ParseIfAndElseStatement(FnName);
        break;
    case tok_while:
        return ParseWhileStatement(FnName);
        break;
    case tok_for:
        return ParseForStatement(FnName);
        break;
    case tok_identifier:
        return ParseAssigningStatement();
        break;
    case tok_return:
        return ParseReturningStatement();
        break;
    default:
        return LogErrorS("Invalid statement input");
    }
}

static std::unique_ptr<PrototypeAST> ParsePrototype() {
    Types FnType = ValType;
    getNextToken(); // consume ValType
    if (CurTok != tok_identifier)
        return LogErrorP("Expected function name in prototype");

    std::string FnName = IdentifierStr;
    getNextToken(); // consume FnName

    if (CurTok != '(')
        return LogErrorP("Expected '(' in prototype");
    getNextToken(); // consume '('
    std::vector<Types> ArgTypes;
    std::vector<std::string> ArgNames;
    if (CurTok == tok_def) {
        ArgTypes.push_back(ValType);
        getNextToken(); // consume <type>
        if (CurTok != tok_identifier)
            return LogErrorP("Expected valid arg name in paramlist");
        ArgNames.push_back(IdentifierStr);
        getNextToken(); // consume <ident>
        while (CurTok == ',') {
            getNextToken(); // consume ','
            if (CurTok != tok_def)
                return LogErrorP("Expected arg type in paramlist");
            ArgTypes.push_back(ValType);
            getNextToken(); // consume <type>
            if (CurTok != tok_identifier)
                return LogErrorP("Expected valid arg name in paramlist");
            ArgNames.push_back(IdentifierStr);
            getNextToken(); // consume <ident>
        }
    }

    if (CurTok != ')')
        return LogErrorP("Expected ')' in prototype.");

    // Success.
    getNextToken(); // consume ')'

    return std::make_unique<PrototypeAST>(FnName, std::move(ArgNames),
                                          std::move(ArgTypes), FnType);
}

static std::unique_ptr<BodyAST> ParseBody(std::string FnName) {
    getNextToken(); // consume '{'
    std::vector<std::unique_ptr<StmtAST>> Stmts;
    while (CurTok != '}') {
        auto S = ParseStatement(FnName);
        Stmts.push_back(std::move(S));
    }
    getNextToken(); // consume '}'
    return std::make_unique<BodyAST>(std::move(Stmts));
}

static std::unique_ptr<FunctionAST> ParseDefinition() {
    auto Proto = ParsePrototype();
    auto Body = ParseBody(Proto->getName());
    return std::make_unique<FunctionAST>(std::move(Proto), std::move(Body));
}

static std::unique_ptr<PrototypeAST> ParseExtern() {
    int isdef = getNextToken(); // consume "extern"
    if (isdef != tok_def)
        return LogErrorP("Expected type declaration");
    auto Proto = ParsePrototype();
    if (CurTok != ';')
        return LogErrorP("Expected ';' in global declaration");
    getNextToken(); // consume ';'
    return Proto;
}

//===----------------------------------------------------------------------===//
// Code Generation
//===----------------------------------------------------------------------===//

static std::unique_ptr<LLVMContext> TheContext;
static std::unique_ptr<Module> TheModule;
static std::unique_ptr<IRBuilder<>> Builder;
static std::map<std::string, AllocaInst *> NamedValues;
static std::map<std::string, std::unique_ptr<PrototypeAST>> FunctionProtos;

Value *LogErrorV(const char *Str) {
    LogError(Str);
    return nullptr;
}

Function *LogErrorFn(const char *Str) {
    LogError(Str);
    return nullptr;
}

// getFunction(Name) can return a Function structure variable, F, to caller,
// which can be used to creat a callee statement in codegen.
Function *getFunction(std::string Name) {
    // First, see if the function has already been added to the current module.
    if (auto *F = TheModule->getFunction(Name))
        return F;

    // If not, check whether we can codegen the declaration from some existing
    // prototype.
    auto FI = FunctionProtos.find(Name);
    if (FI != FunctionProtos.end())
        return FI->second->codegen();

    // If no existing prototype exists, return null.
    return nullptr;
}

/// CreateEntryBlockAlloca - Create an alloca instruction in the entry block of
/// the function.  This is used for mutable variables etc.
static AllocaInst *CreateEntryBlockAlloca(Function *TheFunction,
                                          const std::string &VarName,
                                          Type *ty) {
    IRBuilder<> TmpB(&TheFunction->getEntryBlock(),
                     TheFunction->getEntryBlock().begin());
    return TmpB.CreateAlloca(ty, nullptr, VarName.c_str());
}

Value *NumberDoubleExprAST::codegen() {
    return ConstantFP::get(*TheContext, APFloat(Val));
}

Value *NumberIntExprAST::codegen() {
    return ConstantInt::get(*TheContext, APInt(32, Val));
}

Value *VariableExprAST::codegen() {
    // Look this variable up in the function.
    Value *V = NamedValues[Name];
    if (!V)
        return LogErrorV("Unknown variable!");
    // Load the value.
    return Builder->CreateLoad(V, Name.c_str());
}

Value *BinaryExprAST::codegen() {
    Value *L = LHS->codegen();
    if (!L)
        return nullptr;
    Value *R = RHS->codegen();
    if (!R)
        return nullptr;

    // Do value type conversion before binary operation.
    bool is_int_operation;
    if (L->getType()->isDoubleTy()) {
        if (R->getType()->isIntegerTy()) {
            R = Builder->CreateUIToFP(R, Type::getDoubleTy(*TheContext),
                                      "double");
        } else if (!R->getType()->isDoubleTy()) {
            return LogErrorV("Invalid operation value type");
            is_int_operation = false;
        }

    } else if (L->getType()->isIntegerTy()) {
        if (R->getType()->isDoubleTy()) {
            L = Builder->CreateUIToFP(L, Type::getDoubleTy(*TheContext), "int");
            is_int_operation = false;
        } else if (R->getType()->isIntegerTy()) {
            is_int_operation = true;

        } else {
            return LogErrorV("Invalid operation value type");
        }
    } else {
        return LogErrorV("Invalid operation value type");
    }

    switch (Op) {
    case '+':
        if (is_int_operation)
            return Builder->CreateAdd(L, R, "add");
        else
            return Builder->CreateFAdd(L, R, "add");
    case '-':
        if (is_int_operation)
            return Builder->CreateSub(L, R, "sub");
        else
            return Builder->CreateFSub(L, R, "sub");
    case '*':
        if (is_int_operation)
            return Builder->CreateMul(L, R, "mul");
        else
            return Builder->CreateFMul(L, R, "mul");
    case '<':
        if (is_int_operation)
            return Builder->CreateICmpULT(L, R, "lt");
        else
            return Builder->CreateFCmpULT(L, R, "lt");
    default:
        return LogErrorV("Unknown binary opreation");
    }
}

Value *CallExprAST::codegen() {
    std::vector<Value *> ArgValues;
    Function *CalleeF = TheModule->getFunction(Callee);
    if (!CalleeF)
        return LogErrorV("Unknown fault!");

    if (CalleeF->arg_size() != Args.size())
        return LogErrorV("Error arguments number!");

    int size = Args.size();
    for (int i = 0; i < size; i++) {
        ArgValues.push_back(Args[i]->codegen());
        if (!ArgValues.back())
            return nullptr;
    }

    return Builder->CreateCall(CalleeF, ArgValues, "calltmp");
}

Function *PrototypeAST::codegen() {
    // Make the function type:  double(double,double) etc.
    llvm::Type *result;
    switch (FnType) {
    case type_int:
        result = Type::getInt32Ty(*TheContext);
        break;
    case type_double:
        result = Type::getDoubleTy(*TheContext);
        break;
    default:
        return LogErrorFn("Invalid return value type!");
    }

    std::vector<Type *> parameters;
    int size = Args.size();
    for (int i = 0; i < size; i++) {
        switch (ArgTypes[i]) {
        case type_int:
            parameters.push_back(Type::getInt32Ty(*TheContext));
            break;
        case type_double:
            parameters.push_back(Type::getDoubleTy(*TheContext));
            break;
        default:
            return LogErrorFn("Invalid parameter value type!");
        }
    }

    Function *F =
        Function::Create(FunctionType::get(result, parameters, false),
                         Function::ExternalLinkage, Name, TheModule.get());

    // Set names for all arguments.
    unsigned Idx = 0;
    for (auto &Arg : F->args())
        Arg.setName(Args[Idx++]);

    return F;
}

Value *BodyAST::codegen() {
    for (auto &S : Stmts)
        S->codegen();
    return nullptr;
}

Value *DeclStmtAST::codegen() {
    Function *TheFunction = getFunction(FnName);

    llvm::Type *ty;
    if (declaredType == type_int)
        ty = Type::getInt32Ty(*TheContext);
    else if (declaredType == type_double)
        ty = Type::getDoubleTy(*TheContext);

    for (auto &Decl : declatedElements) {
        // Create an alloca for this variable.
        AllocaInst *Alloca = CreateEntryBlockAlloca(TheFunction, Decl, ty);
        // Add arguments to variable symbol table.
        NamedValues[Decl] = Alloca;
    }

    return nullptr;
}

Value *AssignStmtAST::codegen() {
    Value *R = RHS->codegen();
    if (!R)
        return nullptr;

    // Look up the name.
    AllocaInst *Variable = NamedValues[Ident];
    if (!Variable)
        return LogErrorV("Unknown variable name");

    // Start type casting.
    if (Variable->getAllocatedType()->isIntegerTy() &&
        R->getType()->isDoubleTy())
        R = Builder->CreateFPToUI(R, Type::getInt32Ty(*TheContext), "int");
    if (Variable->getAllocatedType()->isDoubleTy() &&
        R->getType()->isIntegerTy())
        R = Builder->CreateUIToFP(R, Type::getDoubleTy(*TheContext), "double");

    Builder->CreateStore(R, Variable);
    return nullptr;
}

Value *RetStmtAST::codegen() {
    Value *ret = Builder->CreateRet(Ret->codegen());
    return ret;
}

Value *IfElseAST::codegen() {
    Value *CondV = Condition->codegen();
    if (!CondV)
        return nullptr;

    Function *TheFunction = Builder->GetInsertBlock()->getParent();

    BasicBlock *ThenBB = BasicBlock::Create(*TheContext, "true", TheFunction);
    BasicBlock *FalseBB = BasicBlock::Create(*TheContext, "false", TheFunction);
    BasicBlock *MergeBB = BasicBlock::Create(*TheContext, "merge", TheFunction);

    Builder->CreateCondBr(CondV, ThenBB, FalseBB);

    Builder->SetInsertPoint(ThenBB);
    for (auto &Stmt : ThenStmts)
        Stmt->codegen();
    if (!is_end_of_then_blocks())
        Builder->CreateBr(MergeBB);

    Builder->SetInsertPoint(FalseBB);
    for (auto &Stmt : FalseStmts)
        Stmt->codegen();
    if (!is_end_of_false_blocks())
        Builder->CreateBr(MergeBB);

    Builder->SetInsertPoint(MergeBB);

    return nullptr;
}

Value *WhileAST::codegen() {
    Function *TheFunction = Builder->GetInsertBlock()->getParent();
    BasicBlock *continueBB =
        BasicBlock::Create(*TheContext, "continue", TheFunction);
    BasicBlock *checkBB = BasicBlock::Create(*TheContext, "check", TheFunction);
    BasicBlock *doBB = BasicBlock::Create(*TheContext, "do", TheFunction);

    Builder->CreateBr(checkBB);

    Builder->SetInsertPoint(checkBB);
    Value *CondV = Condition->codegen();
    if (!CondV) {
        return nullptr;
    }

    Builder->CreateCondBr(CondV, doBB, continueBB);

    Builder->SetInsertPoint(doBB);
    for (auto &Stmt : Stmts) {
        Stmt->codegen();
    }
    Builder->CreateBr(checkBB);

    Builder->SetInsertPoint(continueBB);
    return nullptr;
}

// Output for-loop as:
//   ...
//   start = startexpr
//   goto loop
// loop:
//   variable = phi [start, loopheader], [nextvariable, loopend]
//   ...
//   bodyexpr
//   ...
// loopend:
//   step = stepexpr
//   nextvariable = variable + step
//   endcond = endexpr
//   br endcond, loop, endloop
// outloop:
Value *ForExprAST::codegen() {
    Function *TheFunction = Builder->GetInsertBlock()->getParent();
    BasicBlock *continueBB =
        BasicBlock::Create(*TheContext, "continue", TheFunction);
    BasicBlock *checkBB = BasicBlock::Create(*TheContext, "check", TheFunction);
    BasicBlock *doBB = BasicBlock::Create(*TheContext, "do", TheFunction);

    InitStmt->codegen();

    Builder->CreateBr(checkBB);
    Builder->SetInsertPoint(checkBB);

    Value *CondV = Condition->codegen();
    if (!CondV) {
        return nullptr;
    }

    Builder->CreateCondBr(CondV, doBB, continueBB);
    Builder->SetInsertPoint(doBB);
    for (auto &Stmt : Stmts) {
        Stmt->codegen();
    }
    Update->codegen();
    Builder->CreateBr(checkBB);
    Builder->SetInsertPoint(continueBB);
    return nullptr;
}

// getFunction(Name) can return a Function structure variable, F, to caller,
// which can be used to creat a callee statement in codegen.
Function *FunctionAST::codegen() {
    // Transfer ownership of the prototype to the FunctionProtos map, but keep a
    // reference to it for use below.
    auto &P = *Proto;
    FunctionProtos[Proto->getName()] = std::move(Proto);
    Function *TheFunction = getFunction(P.getName());
    if (!TheFunction) {
        return nullptr;
    }

    // Create a new basic block to start insertion into.
    BasicBlock *BB = BasicBlock::Create(*TheContext, "entry", TheFunction);
    Builder->SetInsertPoint(BB);

    NamedValues.clear();
    for (auto &Arg : TheFunction->args()) {
        AllocaInst *Alloca = CreateEntryBlockAlloca(
            TheFunction, std::string(Arg.getName()), Arg.getType());
        Builder->CreateStore(&Arg, Alloca);
        NamedValues[std::string(Arg.getName())] = Alloca;
    }

    Body->codegen();
    //****************
    // Correctly create the RetVal use Builder.
    //****************
    verifyFunction(*TheFunction);
    return TheFunction;
}

//===----------------------------------------------------------------------===//
// Top-Level
//===----------------------------------------------------------------------===//
// don't modify this part

static void InitializeModuleAndPassManager() {
    // Open a new context and module.
    TheContext = std::make_unique<LLVMContext>();
    TheModule = std::make_unique<Module>("my cool jit", *TheContext);

    // Create a new builder for the module.
    Builder = std::make_unique<IRBuilder<>>(*TheContext);
}

static void HandleDefinition() {
    if (auto FnAST = ParseDefinition()) {
        fprintf(stderr, "Read function definition:");
        if (auto *FnIR = FnAST->codegen()) {
            fprintf(stderr, "\nStart generating code from definition\n");
            FnIR->print(errs());
            fprintf(stderr, "\n");
        }
    } else {
        // Skip token for error recovery.
        getNextToken();
    }
}

static void HandleExtern() {
    if (auto ProtoAST = ParseExtern()) {
        fprintf(stderr, "[INFO] ParseAST from extern\n");
        if (auto *FnIR = ProtoAST->codegen()) {
            fprintf(stderr, "[INFO] CodeGen from extern\n");
            FnIR->print(errs());
            fprintf(stderr, "\n");
            FunctionProtos[ProtoAST->getName()] = std::move(ProtoAST);
        }
    } else {
        // Skip token for error recovery.
        getNextToken();
    }
}

/// top ::= definition | external | expression | ';'
static void MainLoop() {
    while (true) {
        switch (CurTok) {
        case tok_eof:
            return;
        case tok_def:
            HandleDefinition();
            break;
        case tok_extern:
            HandleExtern();
            break;
        default:
            std::cerr << "invalid input" << std::endl;
            getNextToken();
            break;
        }
    }
}
//===----------------------------------------------------------------------===//
// Main driver code.
//===----------------------------------------------------------------------===//
// don't modify this part

int main(int argc, char *argv[]) {
    if (argc < 2) {
        errs() << "You need to specify the file to compile";
        return 1;
    }
    char *FileName = argv[1];
    fip = fopen(FileName, "r");
    if (fip == nullptr) {
        errs() << "The file '" << FileName << "' is not existed";
        return 1;
    }

    InitializeTypeValue();

    BinopPrecedence['<'] = 10;
    BinopPrecedence['+'] = 20;
    BinopPrecedence['-'] = 20;
    BinopPrecedence['*'] = 40; // highest.
    getNextToken();

    InitializeModuleAndPassManager();
    MainLoop();

    InitializeAllTargetInfos();
    InitializeAllTargets();
    InitializeAllTargetMCs();
    InitializeAllAsmParsers();
    InitializeAllAsmPrinters();

    auto TargetTriple = sys::getDefaultTargetTriple();
    TheModule->setTargetTriple(TargetTriple);

    std::string Error;
    auto Target = TargetRegistry::lookupTarget(TargetTriple, Error);

    // Print an error and exit if we couldn't find the requested target.
    // This generally occurs if we've forgotten to initialise the
    // TargetRegistry or we have a bogus target triple.
    if (!Target) {
        errs() << Error;
        return 1;
    }

    auto CPU = "generic";
    auto Features = "";

    TargetOptions opt;
    auto RM = Optional<Reloc::Model>();
    auto TheTargetMachine =
        Target->createTargetMachine(TargetTriple, CPU, Features, opt, RM);
    TheModule->setDataLayout(TheTargetMachine->createDataLayout());

    auto Filename = "output.o";
    std::error_code EC;
    raw_fd_ostream dest(Filename, EC, sys::fs::OF_None);

    if (EC) {
        errs() << "Could not open file: " << EC.message();
        return 1;
    }

    legacy::PassManager pass;
    auto FileType = llvm::LLVMTargetMachine::CGFT_ObjectFile;

    if (TheTargetMachine->addPassesToEmitFile(pass, dest, nullptr, FileType)) {
        errs() << "TheTargetMachine can't emit a file of this type";
        return 1;
    }

    pass.run(*TheModule);
    dest.flush();

    outs() << "Wrote " << Filename << "\n";

    return 0;
}
