package core.tweetprocessors;

import core.db.model.GeneratedTweetDto;
import core.db.model.SearchedTweetDto;
import core.db.repository.IGeneratedTweetRepository;
import core.db.repository.ISearchedTweetRepository;
import core.utils.Encryptor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EncryptService {

    @Autowired
    ISearchedTweetRepository searchedTweetRepository;
    @Autowired
    IGeneratedTweetRepository generatedTweetRepository;

    @Autowired
    private CounterService counterService;

    @Value("${encrypt.aes.key}")
    private String aesKey;

    public EncryptService() {
        super();
    }

    private GeneratedTweetDto copyAndEncryptTweet(Encryptor encryptor, SearchedTweetDto tweet) {
        counterService.increment("counter.encryptedtweets.total");

        GeneratedTweetDto generatedTweetDto = new GeneratedTweetDto();

        generatedTweetDto.setIdReferenced(tweet.getIdStr());
        String textEncrypted = encryptor.aesEncryptor(tweet.getText(), aesKey);
        generatedTweetDto.setText(textEncrypted);
        generatedTweetDto.setPlainText(tweet.getText());
        generatedTweetDto.setFromUser(tweet.getFromUser());

        return generatedTweetDto;
    }

    public GeneratedTweetDto encryptTweet(SearchedTweetDto tweet) {
        Encryptor encryptor = new Encryptor();

        if (tweet.getText() != null) {
            return copyAndEncryptTweet(encryptor, tweet);
        }
        return null;
    }

}