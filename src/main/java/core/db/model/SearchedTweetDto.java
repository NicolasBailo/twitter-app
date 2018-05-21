package core.db.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "searchedTweets")

public class SearchedTweetDto extends TweetJsonDto{

    private String searchedQuery;

    public String getSearchedQuery() {
        return searchedQuery;
    }

    public void setSearchedQuery(String searchedQuery) {
        this.searchedQuery = searchedQuery;
    }
}
