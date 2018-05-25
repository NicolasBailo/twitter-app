package core.controller;

import core.service.EncryptService;
import core.service.TwitterLookupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class TweetController {

    @Autowired
    TwitterLookupService twitter;
    @Autowired
    EncryptService encryptService;


    @MessageMapping("/search")
    public void search(SimpMessageHeaderAccessor headerAccessor, @RequestParam("query") String query) {

        String sessionId = headerAccessor.getSessionId(); // Session ID
        twitter.search(query,sessionId);
    }

    @MessageExceptionHandler(Exception.class)
    public void handleError(Exception exception) {

        System.err.println("errrorrrrrrr");

    }

}
