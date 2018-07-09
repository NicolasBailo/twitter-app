package core.db.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "searchedTweets")

public class SearchedTweetDto extends TweetJsonDto{

    public final int operationEncrypt = 1;
    public final int operationChange = 2;

    private int operation;
    private String searchedQuery;

    public int getOperation() {
        return operation;
    }

    public String getSearchedQuery() {
        return searchedQuery;
    }

    public void setOperation(int operation) { this.operation = operation; }

    public void setSearchedQuery(String searchedQuery) {
        this.searchedQuery = searchedQuery;
    }
}
