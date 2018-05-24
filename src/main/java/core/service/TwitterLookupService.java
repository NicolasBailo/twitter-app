package core.service;

import core.utils.StreamAux;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.social.twitter.api.*;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
public class TwitterLookupService {

    @Autowired
    EncryptService encryptService;

    @Value("${twitter.consumerKey}")
    private String consumerKey;

    @Value("${twitter.consumerSecret}")
    private String consumerSecret;

    @Value("${twitter.accessToken}")
    private String accessToken;

    @Value("${twitter.accessTokenSecret}")
    private String accessTokenSecret;


    //HashMap<String, Stream> streams = new HashMap<>();
    List<Stream> streams= new ArrayList<>();

    public void search(String query, String sessionId) {
        Twitter twitter = new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);
        List<StreamListener> list = new ArrayList<>();
        list.add(new SimpleStreamListener(query));

        streams.add(twitter.streamingOperations().filter(query, list));
    }

    public void cancelSearch(StompHeaderAccessor headerAccessor) {
        System.out.println("Cancelar busqueda!!!!");

        String sessionId = headerAccessor.getHeader("simpSessionId").toString();

        try {
            //((Thread)streams.get(sessionId)).stop();
            //streams.remove(sessionId);

            //((Thread)streams.get(0)).stop();

            ((Thread) streams.get(0)).stop();

            streams.remove(0);

        } catch (Exception e) {
            System.out.println("errorororororororo");
        }
    }
}