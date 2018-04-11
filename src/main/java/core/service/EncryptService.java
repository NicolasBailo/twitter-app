package core.service;

import core.db.model.GeneratedTweetDto;
import core.db.model.SearchedTweetDto;
import core.db.repository.IGeneratedTweetRepository;
import core.db.repository.ISearchedTweetRepository;
import core.utils.Encryptor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//import org.apache.commons.beanutils.BeanUtils;



@Service
public class EncryptService {

    @Autowired
    ISearchedTweetRepository searchedTweetRepository;
    @Autowired
    IGeneratedTweetRepository generatedTweetRepository;

    @Value("${encrypt.aes.key}")
    private String aesKey;


    public List<GeneratedTweetDto> encryptTweets(List<Tweet> tweets) {
        Encryptor encryptor = new Encryptor();


        List<GeneratedTweetDto> result = new ArrayList<>();
        List<SearchedTweetDto> oldTweets = new ArrayList<>();
        //List<ResponseAuxDto> lista = new ArrayList<>();

        for (Tweet tweet : tweets){

            SearchedTweetDto searchedTweetDto = new SearchedTweetDto();
            GeneratedTweetDto generatedTweetDto = new GeneratedTweetDto();

            BeanUtils.copyProperties(tweet, searchedTweetDto);
            oldTweets.add(searchedTweetDto);

            generatedTweetDto.setIdReferenced(tweet.getIdStr());

            if(tweet.getText()!=null) {
                String textEncrypted = encryptor.aesEncryptor(tweet.getText(),aesKey);
                generatedTweetDto.setText(textEncrypted);
                result.add(generatedTweetDto);
            //lista.add(new ResponseAuxDto(tweet.getText(),encryptor.aesEncryptor(tweet.getText(),aesKey)));
            }

        }

        searchedTweetRepository.save(oldTweets);
        generatedTweetRepository.save(result);

        return result;
        //return lista;
    }



}


