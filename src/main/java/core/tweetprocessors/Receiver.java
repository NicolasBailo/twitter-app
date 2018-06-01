package core.tweetprocessors;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.CountDownLatch;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.db.model.GeneratedTweetDto;
import core.db.model.SearchedTweetDto;
import core.tweetprocessors.EncryptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    EncryptService encryptService;

    private CountDownLatch latch = new CountDownLatch(1);

    public void receiveMessage(byte[] bytes) {
        String message;
        try{
            message = new String(bytes, "UTF-8");

        }catch(UnsupportedEncodingException e){
            message = new String(bytes);
        }




        latch.countDown();

        ObjectMapper mapper = new ObjectMapper();


        GeneratedTweetDto generatedTweetDto = null;
        try {
            SearchedTweetDto searchedTweetDto = mapper.readValue(message, SearchedTweetDto.class);
            generatedTweetDto = encryptService.encryptTweet(searchedTweetDto);
            System.out.println("Received from <" + searchedTweetDto.getSearchedQuery()+ ">");
            messagingTemplate.convertAndSend("/queue/search/" + searchedTweetDto.getSearchedQuery(), generatedTweetDto);
        } catch (IOException e) {
            e.printStackTrace();
        }


        //messagingTemplate.convertAndSend("/queue/search/" + name, tweet, map);
    }

    public CountDownLatch getLatch() {
        return latch;
    }

}