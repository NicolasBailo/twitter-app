package core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.db.model.GeneratedTweetDto;
import core.db.model.SearchedTweetDto;
import core.db.repository.ISearchedTweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.CountDownLatch;

@Component
public class TweetSaver {

    @Autowired
    private ISearchedTweetRepository searchedTweetRepository;

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


        try {
            SearchedTweetDto searchedTweetDto = mapper.readValue(message, SearchedTweetDto.class);
            searchedTweetRepository.save(searchedTweetDto);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CountDownLatch getLatch() {
        return latch;
    }

}