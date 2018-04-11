package core.db.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "searchedTweets")

public class SearchedTweetDto extends TweetJsonDto{


}
