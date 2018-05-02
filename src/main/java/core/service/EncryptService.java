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

    private GeneratedTweetDto copyAndEncryptTweet(Encryptor encryptor, Tweet tweet) {
        GeneratedTweetDto generatedTweetDto = new GeneratedTweetDto();

        generatedTweetDto.setIdReferenced(tweet.getIdStr());
        String textEncrypted = encryptor.aesEncryptor(tweet.getText(), aesKey);
        generatedTweetDto.setText(textEncrypted);
        generatedTweetDto.setPlainText(tweet.getText());
        generatedTweetDto.setFromUser(tweet.getFromUser());

        return generatedTweetDto;
    }

    public List<GeneratedTweetDto> encryptTweets(List<Tweet> tweets) {
        List<GeneratedTweetDto> result = new ArrayList<>();
        List<SearchedTweetDto> oldTweets = new ArrayList<>();

        Encryptor encryptor = new Encryptor();
        for (Tweet tweet : tweets) {
            SearchedTweetDto searchedTweetDto = new SearchedTweetDto();

            BeanUtils.copyProperties(tweet, searchedTweetDto);
            oldTweets.add(searchedTweetDto);

            if (tweet.getText() != null) {
                result.add(copyAndEncryptTweet(encryptor, tweet));
            }
        }

        searchedTweetRepository.save(oldTweets);
        generatedTweetRepository.save(result);

        return result;
    }


}

