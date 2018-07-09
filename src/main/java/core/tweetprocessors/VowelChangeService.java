package core.tweetprocessors;

import core.db.model.GeneratedTweetDto;
import core.db.model.SearchedTweetDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.stereotype.Service;

@Service
public class VowelChangeService {

    @Autowired
    private CounterService counterService;

    private final char vowelToChange = 'i';

    public VowelChangeService() {
        super();
    }

    public GeneratedTweetDto changeTweet(SearchedTweetDto tweet) {
        counterService.increment("counter.changedtweets.total");

        GeneratedTweetDto generatedTweetDto = new GeneratedTweetDto();

        generatedTweetDto.setIdReferenced(tweet.getIdStr());
        String textChanged = tweet.getText()
                .replace('a', vowelToChange)
                .replace('e', vowelToChange)
                .replace('i', vowelToChange)
                .replace('o', vowelToChange)
                .replace('u', vowelToChange);
        generatedTweetDto.setText(textChanged);
        generatedTweetDto.setPlainText(tweet.getText());
        generatedTweetDto.setFromUser(tweet.getFromUser());

        return generatedTweetDto;
    }

}