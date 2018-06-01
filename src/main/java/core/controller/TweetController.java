package core.controller;

import core.tweetchoser.TwitterLookupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class TweetController {

    @Autowired
    TwitterLookupService twitter;

    @MessageMapping("/search")
    public void search(SimpMessageHeaderAccessor headerAccessor, @RequestParam("query") String query) {
        String sessionId = headerAccessor.getSessionId(); // Gets session ID
        twitter.search(query, sessionId);
    }

    @MessageExceptionHandler(Exception.class)
    public void handleError(Exception exception) {
        System.err.println("Error in TweetController");
    }

}
