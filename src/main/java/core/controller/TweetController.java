package core.controller;

import core.db.model.GeneratedTweetDto;
import core.db.model.ResponseAuxDto;
import core.service.EncryptService;
import core.service.TwitterLookupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.social.UncategorizedApiException;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TweetController {

    @Autowired
    TwitterLookupService twitter;
    @Autowired
    EncryptService encryptService;


    @MessageMapping("/search")
    public void search(@RequestParam("query") String query) {
        twitter.search(query);
    }

    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UncategorizedApiException.class)
    public SearchResults handleUncategorizedApiException() {
        return twitter.emptyAnswer();
    }


//    @RequestMapping("/x")
//    @ResponseBody
//    public List<GeneratedTweetDto> x(@RequestParam("q") String q) {
//        return null;
//        //return encryptService.encryptTweets(twitter.search(q));
//    }

}
