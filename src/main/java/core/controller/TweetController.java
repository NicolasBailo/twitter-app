package core.controller;

import core.db.model.GeneratedTweetDto;
import core.db.model.ResponseAuxDto;
import core.service.EncryptService;
import core.service.TwitterLookupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TweetController {

    @Autowired
    TwitterLookupService twitter;
    @Autowired
    EncryptService encryptService;

    @RequestMapping("/search")
    @ResponseBody
    public List<GeneratedTweetDto> search(@RequestParam("q") String q) {

        return encryptService.encryptTweets(twitter.search(q));
    }
}
