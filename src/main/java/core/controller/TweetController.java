package core.controller;

import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;
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

        // Reads param
        BasicDBObject argument = (BasicDBObject) JSON.parse(query);
        String q = (String)argument.get("query");
        int op = (int)argument.get("mode");
        twitter.search(q, op, sessionId);
    }

    @MessageExceptionHandler(Exception.class)
    public void handleError(Exception exception) {
        System.err.println("Error in TweetController");
    }

}
