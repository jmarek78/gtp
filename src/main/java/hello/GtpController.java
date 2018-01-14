package hello;

import java.util.concurrent.atomic.AtomicLong;

import hello.gtp.GtpUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GtpController {

    @RequestMapping("/gtp")
    public Path gtp(@RequestParam(value="url", defaultValue="NONE") String url) {
        return new GtpUtil().findPhilosophy(url);
    }
}
