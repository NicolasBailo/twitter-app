package core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.db.model.SearchedTweetDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.StreamDeleteEvent;
import org.springframework.social.twitter.api.StreamListener;
import org.springframework.social.twitter.api.StreamWarningEvent;
import org.springframework.social.twitter.api.Tweet;

public class SimpleStreamListener implements StreamListener {
    //private SimpMessageSendingOperations sender;
    //private String query;

    private final String exchangeName = "tweets";

    @Autowired RabbitService rabbitService;

    @Autowired RabbitTemplate rabbitTemplate;

    private String queryList;




    public SimpleStreamListener(String queryList) {
        this.queryList = queryList;

        rabbitService = new RabbitService();
    }


    public void setQueryList(String queryList){
        this.queryList = queryList;
    }

    @Override
    public void onTweet(Tweet tweet) {
        try{
            String[] queries = queryList.split(",");

            for(String query : queries){
                query= query.trim();
                if(tweet.getText().contains(query)){

                    ObjectMapper mapper = new ObjectMapper();
                    SearchedTweetDto searchedTweetDto = new SearchedTweetDto();
                    BeanUtils.copyProperties(tweet, searchedTweetDto);
                    searchedTweetDto.setSearchedQuery(query);

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