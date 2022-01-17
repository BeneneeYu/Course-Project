本程序已通过所有手工测试用例

# 文件替换位置

请务必保证词源文件放在System.getProperty( "user.dir" )下

TextEditor/src的位置，程序可以适配读取linux/windows系统

# 整体实现

运用命令模式（详细解释见下），适配器模式等

设计命令基类和不同种类的命令类型，设计过滤器基类和不同的过滤器类型，设计检查器

将功能拆分，不同模块间通信，职责明确，如拼写检查时，先利用filter进行格式解析，再利用spellChecker检查词语拼写

# Undo/Redo的实现

因为undo指令时，后进先出，所以可以借助栈数据结构实现

在做每一步非特殊操作时，向undoStack push进当前的指令，所以在接收到undo命令后，可以从undoStack pop出第一条指令进行undo

而undo时，也需要将这条被pop的指令push进redoStack中，来为可能接收到的redo指令做好准备

# 测试代码

放在`TextEditor\src\client\Test.java`下

既然能通过手工测试，测试代码的完备性和正确性是显然的

```java
    commandList.add("-t \"Hotel \"");// Initiate the string, type in word 'Hotel'.
    commandList.add("s");// Print the string.
    commandList.add("A \"California\"");// Append the word 'California' to string.
    commandList.add("a \"Welcome to the \"");// Add the sentence 'Welcome to the' to the head.
    commandList.add("D 11");// Delete the last 10 characters, which is ' California'.
    commandList.add("d 8");// Delete the first 8 characters, which is 'Welcome '.
    commandList.add("l 10");// Show last ten commands.
    commandList.add("u");// Undo 'Delete the first 8 characters'.
    commandList.add("r");// Redo 'Delete the first 8 characters'.
    commandList.add("m 5 m10");// Define macro command m10.
    commandList.add("$m10");// Use macro command m10.
    commandList.add("a \"Welcome \"");
    commandList.add("A \" California\"");
    commandList.add("A \" Balifornia\"");
    commandList.add("spell");
    commandList.add("spell-a");
    commandList.add("spell-m");
    commandList.add("lang fra");
    commandList.add("spell-a");
    commandList.add("content xml");
    commandList.add("lang eng");
    commandList.add("a \"<A>\"");
    commandList.add("A \"</A>\"");
    commandList.add("spell-a");
    commandList.add("q");// Quit.
```

# 命令模式

运用命令模式

## 定义

宏命令：先进行宏命令定义时范围内包括的修改类命令中先被执行的命令

## 概念

命令模式：**将一个请求封装为一个对象，从而使你可用不同的请求对客户进行参数化，对请求排队或记录请求日志。以及支持可撤销的操作。**即封装请求命令，不直接调用执行者的方法，便于扩展。

![img](https://images2015.cnblogs.com/blog/527668/201601/527668-20160109144931450-1153732536.jpg)

**Command（抽象命令类）**：抽象出命令对象，可以根据不同的命令类型。写出不同的实现类

**ConcreteCommand（具体命令类）**：实现了抽象命令对象的具体实现

**Invoker（调用者/请求者）**：请求的发送者，它通过命令对象来执行请求。一个调用者并不需要在设计时确定其接收者，因此它只与抽象命令来之间

存在关联。在程序运行时，将调用命令对象的execute() ，间接调用接收者的相关操作。

**Receiver（接收者）**：接收者执行与请求相关的操作，真正执行命令的对象。具体实现对请求的业务处理。未抽象前，实际执行操作内容的对象。

**Client（客户端）**：在客户类中需要创建调用者对象，具体命令类对象，在创建具体命令对象时指定对应的接收者。发送者和接收者之间没有之间关系。

都通过命令对象来调用。

## 实现

首先定义一个命令的接收者，也就是到最后真正执行命令的对象

```java
//接收者：真正执行命令的对象
public class Receiver {
    public void action(){
        System.out.println("命令执行了.......");
    }
}
```

然后定义抽象命令和抽象命令的具体实现，具体命令类中需要持有真正执行命令的那个对象。

```java
//抽象命令类：抽象的命令，可以根据不同类型的命令写出不同的实现
public interface Command {
    //调用命令
    void execute();
}
//具体命令类
class ConcreteCommand implements Command{
    private Receiver receiver;//持有真正执行命令对象的引用
    public ConcreteCommand(Receiver receiver) {
        super();
        this.receiver = receiver;
    }
    @Override
    public void execute() {
        //调用接收者执行命令的方法
        receiver.action();
    }
}
```

接下来就可以定义命令的发起者了，发起者需要持有一个命令对象。以便来发起命令。

```java
//请求者/调用者：发起执行命令请求的对象
public class Invoker {
    private Command command;//持有命令对象的引用
    public Invoker(Command command) {
        super();
        this.command = command;
    }
    public void call(){
        //请求者调用命令对象执行命令的那个execute方法
        command.execute();
    }
}
```

客户端测试：客户端

```java
public static void main(String[] args) {
    //通过请求者（invoker）调用命令对象（command），命令对象中调用了命令具体执行者（Receiver）
    Command command = new ConcreteCommand(new Receiver());
    Invoker invoker = new Invoker(command);
    invoker.call();
}
```





