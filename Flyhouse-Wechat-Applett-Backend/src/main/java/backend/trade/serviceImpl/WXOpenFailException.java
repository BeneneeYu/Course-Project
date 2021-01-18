package backend.trade.serviceImpl;

/**
 * @program: trade
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-05-18 14:26
 **/
public class WXOpenFailException extends RuntimeException {
    WXOpenFailException(String message) {
        super(message);
    }

    WXOpenFailException(String message, Throwable cause) {
        super(message, cause);
    }
}
