package backend.trade.service;

public interface WXOpenService {
    String getAccessToken();

    String getOpenId(String code);

    byte[] getWXACode(String scene, String page);
}
