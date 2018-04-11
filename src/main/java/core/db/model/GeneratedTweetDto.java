package core.db.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "generatedTweets")
public class GeneratedTweetDto extends TweetJsonDto{

    @JsonIgnore
    private String idReferenced;

    public String getIdReferenced() {
        return idReferenced;
    }

    public void setIdReferenced(String idReferenced) {
        this.idReferenced = idReferenced;
    }
}
