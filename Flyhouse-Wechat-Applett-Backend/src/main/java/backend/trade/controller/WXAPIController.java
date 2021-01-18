package backend.trade.controller;

/**
 * @program: trade
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-05-24 08:54
 **/
import backend.trade.service.WXOpenService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/wx")
public class WXAPIController {

    private final WXOpenService wxOpenService;

    public WXAPIController(WXOpenService wxOpenService) {
        this.wxOpenService = wxOpenService;
    }

    @GetMapping(path = "/wxacode", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] miniCode(@RequestParam String scene, @RequestParam(required = false) String page) {
        return wxOpenService.getWXACode(scene, page);
    }
}
