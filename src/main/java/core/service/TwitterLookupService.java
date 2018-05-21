package core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.social.twitter.api.*;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TwitterLookupService {
    /*@Value("${twitter.consumerKey}")
    private String consumerKey;

    @Value("${twitter.consumerSecret}")
    private String consumerSecret;

    @Value("${twitter.accessToken}")
    private String accessToken;

    @Value("${twitter.accessTokenSecret}")
    private String accessTokenSecret;

    public List<Tweet> search(String query) {
        Twitter twitter = new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);
        SearchResults results = twitter.searchOperations().search(query);
        return results.getTweets();
    }

    public SearchResults emptyAnswer() {
        return new SearchResults(Collections.emptyList(), new SearchMetadata(0,0 ));
    }*/

    @Autowired EncryptService encryptService;

    @Value("${twitter.consumerKey}")
    private String consumerKey;

    @Value("${twitter.consumerSecret}")
    private String consumerSecret;

    @Value("${twitter.accessToken}")
    private String accessToken;

    @Value("${twitter.accessTokenSecret}")
    private String accessTokenSecret;



    List<Stream> streams = new ArrayList<>();

    //@Autowired
    //private SimpMessageSendingOperations messagingTemplate;

    public void search(String query) {
        Twitter twitter = new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);

        List<StreamListener> list = new ArrayList<>();
        list.add(new SimpleStreamListener(/*messagingTemplate, */query));



        streams.add(twitter.streamingOperations().filter(query, list));



        SimpleStreamListener simpleStreamListener = new SimpleStreamListener(/*messagingTemplate, */query);


        //RabbitListener rabbitListener = new RabbitListener(messagingTemplate, query, encryptService);
        //rabbitListener.run();
    }

    public SearchResults emptyAnswer() {
        return new SearchResults(Collections.emptyList(), new SearchMetadata(0, 0));
    }
}