package hello;

import hello.gtp.GtpUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GtpController {

    @RequestMapping(value = "/gtp/find", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public ResponseEntity<Path> findPath(@RequestBody WikiPageVO wikiPage)
    {
        String url = wikiPage.getWikiPage();
        System.out.println("got call to find url: " + url);
        Path path = new GtpUtil().findPhilosophy(url);
        return new ResponseEntity<>(path, HttpStatus.OK);
    }

}
