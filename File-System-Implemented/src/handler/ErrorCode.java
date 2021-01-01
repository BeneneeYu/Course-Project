package handler;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: CSE_lab1
 * @description: 异常处理机制
 * @author: Shen Zhengyu
 * @create: 2020-10-03 16:14
 **/
public class ErrorCode extends RuntimeException {
    public static final int IO_EXCEPTION = 1;
    public static final int INCONSISTENT_CHECKSUM_VALUE = 2;
    public static final int CREATE_EXISTED_FILE = 3;
    public static final int FILE_NOT_EXIST = 4;
    public static final int BLOCK_NOT_EXIST = 5;
    public static final int WRITE_OUT_OF_BOUND = 6;
    public static final int INCORRECT_ARGUMENT_FORMAT = 7;
    public static final int NUMBER_FORMAT_EXCEPTION = 8;
    public static final int READ_OUT_OF_BOUND = 9;
    public static final int COMMAND_NOT_FOUND = 10;
    public static final int POINTER_POSITION_ERROR = 11;
    public static final int FILE_META_LOSS = 12;
    public static final int ID_ACHIEVING_EXCEPTION = 13;
    public static final int SERIALIZED_ERROR = 14;
    public static final int BLOCK_INFORMATION_LOSS = 15;
    public static final int INCORRECT_LENGTH_VALUE_EXCEPTION = 16;
    public static final int INCORRECT_FM_ID = 17;
    public static final int INCORRECT_BM_ID = 18;
    public static final int UNKNOWN = 1000;
    private static final Map<Integer, String> ErrorCodeMap = new HashMap<>();
    static {
        ErrorCodeMap.put(IO_EXCEPTION, "IO exception.");
        ErrorCodeMap.put(INCONSISTENT_CHECKSUM_VALUE, "Inconsistent checksum value.");
        ErrorCodeMap.put(CREATE_EXISTED_FILE, "Creating existed file exception.");
        ErrorCodeMap.put(FILE_NOT_EXIST, "File doesn't exist exception.");
        ErrorCodeMap.put(BLOCK_NOT_EXIST, "Block doesn't exist exception.");
        ErrorCodeMap.put(WRITE_OUT_OF_BOUND, "Write out of bound exception.");
        ErrorCodeMap.put(INCORRECT_ARGUMENT_FORMAT, "Incorrect arguments format exception.");
        ErrorCodeMap.put(NUMBER_FORMAT_EXCEPTION, "Number format casting exception.");
        ErrorCodeMap.put(READ_OUT_OF_BOUND, "Read out of bound exception.");
        ErrorCodeMap.put(COMMAND_NOT_FOUND, "Command not found exception.");
        ErrorCodeMap.put(POINTER_POSITION_ERROR, "Pointer position error.");
        ErrorCodeMap.put(FILE_META_LOSS, "File meta loss exception.");
        ErrorCodeMap.put(ID_ACHIEVING_EXCEPTION, "Id achieving exception");
        ErrorCodeMap.put(SERIALIZED_ERROR, "Serializing error.");
        ErrorCodeMap.put(BLOCK_INFORMATION_LOSS, "Block data or meta loss exception.");
        ErrorCodeMap.put(INCORRECT_LENGTH_VALUE_EXCEPTION, "Incorrect length value exception.");
        ErrorCodeMap.put(INCORRECT_FM_ID, "Incorrect File Manager Id.");
        ErrorCodeMap.put(INCORRECT_BM_ID, "Incorrect Block Manager Id.");
        ErrorCodeMap.put(UNKNOWN, "unknown");
    }
    public static String getErrorText(int errorCode) {
        return ErrorCodeMap.getOrDefault(errorCode, "invalid");
    }
    private int errorCode;
    public ErrorCode(int errorCode) {
        super(String.format("error code '%d' \"%s\"", errorCode,
                getErrorText(errorCode)));
        this.errorCode = errorCode;
    }
    public int getErrorCode() {
        return errorCode;
    }
}
