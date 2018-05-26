package core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.CounterService;
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

    @Autowired
    private CounterService counterService;

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
        counterService.increment("counter.streams.current");
        counterService.increment("counter.streams.total");

        Twitter twitter = new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);

        sessionQueries.put(sessionId, query);

        String queries = getQueries();

        if(list.size()>0){
            ((SimpleStreamListener)list.get(0)).setQueryList(queries);
        }else{
            list.add(new SimpleStreamListener(queries));
        }

        if (s != null) {
            try{
                s.close();
                ((Thread)s).stop();
            }catch(IndexOutOfBoundsException | NullPointerException e){
                System.out.println("peto y sigo");
            }

        }

        s = twitter.streamingOperations().filter(queries, list);
    }

    public void cancelSearch(StompHeaderAccessor headerAccessor) {
        counterService.decrement("counter.streams.current");

        Twitter twitter = new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);

        String sessionId = headerAccessor.getHeader("simpSessionId").toString();

        sessionQueries.remove(sessionId);

        String queries = getQueries();

        if(list.size()>0){
            ((SimpleStreamListener)list.get(0)).setQueryList(queries);
        }


        if (s != null) {
            try{
                s.close();
                ((Thread)s).stop();
            }catch(IndexOutOfBoundsException | NullPointerException e){
                System.out.println("peto y sigo");
            }

            //list.remove(0);


        }
        if(!queries.isEmpty()){
            s = twitter.streamingOperations().filter(queries, list);
        }


        //s.close();

        /*if (s != null) {
            s.close();
        }*/

        /*if(queries.isEmpty()){
            ((Thread)list.get(0)).stop();
            s = twitter.streamingOperations().filter(queries,new ArrayList<StreamListener>());

            list.remove(0);
            //s.close();
            ((Thread)s).stop();
        }else{
            s = twitter.streamingOperations().filter(queries,list);

        }*/

    }

    private String getQueries(){
        String queries = "";
        for(String querySession : sessionQueries.values()){
            if(!queries.contains(querySession)){
                queries += queries.isEmpty()?querySession:(","+querySession);
            }

        }
        return queries;
    }
}