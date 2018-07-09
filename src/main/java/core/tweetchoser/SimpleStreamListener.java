package core.tweetchoser;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.db.model.SearchedTweetDto;
import org.springframework.beans.BeanUtils;
import org.springframework.social.twitter.api.StreamDeleteEvent;
import org.springframework.social.twitter.api.StreamListener;
import org.springframework.social.twitter.api.StreamWarningEvent;
import org.springframework.social.twitter.api.Tweet;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SimpleStreamListener implements StreamListener {

    private final String exchangeName = "tweets";


    private String queryList;
    private int operation;
    private RabbitService rabbitService;


    public SimpleStreamListener(String queryList, int operation, RabbitService rabbitService) {
        this.queryList = queryList;
        this.operation = operation;
        this.rabbitService = rabbitService;
    }


    public void setQueryList(String queryList){
        this.queryList = queryList;
    }

    public void setOperation(int operation){
        this.operation = operation;
    }

    @Override
    public void onTweet(Tweet tweet) {
        try{
            String[] queryArray = queryList.split(",");
            Set<String> queries = new HashSet<String>(Arrays.asList(queryArray));

            for(String query : queries){
                query= query.trim();
                if(tweet.getText().contains(query)){

                    ObjectMapper mapper = new ObjectMapper();
                    SearchedTweetDto searchedTweetDto = new SearchedTweetDto();
                    BeanUtils.copyProperties(tweet, searchedTweetDto);
                    searchedTweetDto.setSearchedQuery(query);
                    searchedTweetDto.setOperation(operation);

                    String tweetString = mapper.writeValueAsString(searchedTweetDto);
                    rabbitService.publish(tweetString, exchangeName);
                }
            }

        }catch(Exception e){

            System.out.println("peto aquiiiiii");
            e.printStackTrace();
        }


    }

    @Override
    public void onDelete(StreamDeleteEvent deleteEvent) {
        System.out.println("cierro conexión");
    }

    @Override
    public void onLimit(int numberOfLimitedTweets) {

    }

    @Override
    public void onWarning(StreamWarningEvent warningEvent) {

    }
}