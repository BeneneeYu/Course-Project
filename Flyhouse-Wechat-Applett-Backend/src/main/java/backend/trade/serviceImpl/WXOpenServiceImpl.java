package backend.trade.serviceImpl;

import backend.trade.config.AppProperties;
import backend.trade.service.WXOpenService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @program: trade
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-05-18 13:58
 **/
@Service
public class WXOpenServiceImpl implements WXOpenService {

    private final AppProperties appProperties;
    Logger log = LoggerFactory.getLogger(WXOpenServiceImpl.class);

    public WXOpenServiceImpl(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    private String accessToken = null;
    private Long refreshTime = 0L;

    private void refreshAccessToken() {
        final String baseUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appProperties.getAppId() + "&secret=" + appProperties.getAppSecret();
        JsonNode root = get(baseUrl);
        int errcode = root.path("errcode").asInt();
        if (errcode == 0) {
            accessToken = root.path("access_token").asText();
            int expires_in = root.path("expires_in").asInt();
            log.info("Refresh token. expires_in: {}", expires_in);
            refreshTime = System.currentTimeMillis() + expires_in * 1000;
        } else {
            log.error("Refresh token. errcode: {}", errcode);
            throw new WXOpenFailException("微信开放接口调用失败");
        }
    }

    @Override
    public synchronized String getAccessToken() {
        if (accessToken == null || System.currentTimeMillis() > refreshTime) {
            refreshAccessToken();
        }
        return accessToken;
    }

    //小程序给出code，后台获取openid
    @Override
    public String getOpenId(String code) {
        final String baseUrl = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appProperties.getAppId() + "&secret=" + appProperties.getAppSecret() + "&js_code=" + code + "&grant_type=authorization_code";
        JsonNode root = get(baseUrl);
        if (root.path("errcode").asInt() == 0) {
            return root.path("openid").asText();
        } else {
            throw new WXOpenFailException("微信开放接口调用失败");
        }
    }

    @Override
    public byte[] getWXACode(String scene, String page) {
        try {
//            URI uri = new URI("https://api.weixin.qq.com/wxa/getwxacode?access_token=" + accessToken); // limited
            URI uri = new URI("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + getAccessToken());
            Map<String, String> data = new HashMap<>();
            if (scene.equals("")) {
                throw new Exception(); // TODO: error here
            }
            data.put("scene", scene);
            if (page != null && !page.equals("")) {
                data.put("page", page);
            }
            ResponseEntity<byte[]> responseEntity = restTemplate.postForEntity(uri, data, byte[].class);
            return Objects.requireNonNull(responseEntity.getBody());
        } catch (Exception e) {
            throw new WXOpenFailException("微信开放接口调用失败");
        }
    }

    private RestTemplate restTemplate = new RestTemplate();
    private ObjectMapper mapper = new ObjectMapper();

    private JsonNode get(String url) {
        try {
            URI uri = new URI(url);
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);
            return mapper.readTree(responseEntity.getBody());
        } catch (Exception ignore) {
        }
        throw new WXOpenFailException("微信开放接口调用失败");
    }
}
