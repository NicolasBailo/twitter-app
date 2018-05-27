package core.service;

import core.tweetprocessors.EncryptService;
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

    @Autowired
    private RabbitService rabbitService;

    @Value("${twitter.consumerKey}")
    private String consumerKey;

    @Value("${twitter.consumerSecret}")
    private String consumerSecret;

    @Value("${twitter.accessToken}")
    private String accessToken;

    @Value("${twitter.accessTokenSecret}")
    private String accessTokenSecret;


    Stream s = null;
    List<StreamListener> list = new ArrayList<>();
    HashMap<String, String> sessionQueries = new HashMap<>();


    public void search(String query, String sessionId) {
        counterService.increment("counter.streams.current");
        counterService.increment("counter.streams.total");

        Twitter twitter = new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);
        sessionQueries.put(sessionId, query);

        String queries = getQueries();
        if (list.size() > 0) {
            ((SimpleStreamListener) list.get(0)).setQueryList(queries);
        } else {
            list.add(new SimpleStreamListener(queries,rabbitService));
        }

        closeStreamAndStopThread();
        s = twitter.streamingOperations().filter(queries, list);
    }

    public void cancelSearch(StompHeaderAccessor headerAccessor) {
        Twitter twitter = new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);

        String sessionId = headerAccessor.getHeader("simpSessionId").toString();
        if (sessionQueries.containsKey(sessionId)) {
            sessionQueries.remove(sessionId);
            counterService.decrement("counter.streams.current");
        }

        String queries = getQueries();
        if (list.size() > 0) {
            ((SimpleStreamListener) list.get(0)).setQueryList(queries);
        }

        closeStreamAndStopThread();

        if (!queries.isEmpty()) {
            s = twitter.streamingOperations().filter(queries, list);
        }
    }

    private String getQueries() {
        String queries = "";
        for (String querySession : sessionQueries.values()) {
            if (!queries.contains(querySession)) {
                queries += queries.isEmpty() ? querySession : ("," + querySession);
            }

        }
        return queries;
    }

    private void closeStreamAndStopThread() {
        if (s != null) {
            try {
                s.close();
                ((Thread) s).stop();
            } catch (IndexOutOfBoundsException | NullPointerException e) {
                System.out.println("Error controlado al cerrar un Thread de búsqueda");
            }
        }
    }

}