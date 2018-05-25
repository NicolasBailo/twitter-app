package core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.social.twitter.api.Stream;
import org.springframework.social.twitter.api.StreamListener;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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


    HashMap<String, String> sessionQueries= new HashMap<>();

    Stream s = null;
    List<StreamListener> list = new ArrayList<>();


    public void search(String query, String sessionId) {
        Twitter twitter = new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);

        sessionQueries.put(sessionId, query);

        String queries = "";
        for(String querySession : sessionQueries.values()){
            queries += queries.isEmpty()?querySession:(","+querySession);
        }
        if(list.size()>0){
            ((SimpleStreamListener)list.get(0)).setQueryList(queries);
        }else{
            list.add(new SimpleStreamListener(queries));
        }
        s = twitter.streamingOperations().filter(queries, list);
    }

    public void cancelSearch(StompHeaderAccessor headerAccessor) {
        Twitter twitter = new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);
        String sessionId = headerAccessor.getHeader("simpSessionId").toString();

        sessionQueries.remove(sessionId);

        String queries = "";
        for(String querySession : sessionQueries.values()){
            queries += queries.isEmpty()?querySession:(","+querySession);
        }

        ((SimpleStreamListener)list.get(0)).setQueryList(queries);
        s = twitter.streamingOperations().filter(queries, list);

    }
}