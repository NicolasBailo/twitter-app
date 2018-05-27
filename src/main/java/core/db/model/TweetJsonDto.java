package core.db.model;

import org.springframework.data.annotation.Id;
import java.io.Serializable;

public class TweetJsonDto implements Serializable {

    @Id
    String idStr;

    Integer favouriteCount;

    Boolean favorited;

    String fromUser;

    Long fromUserId;

    String inReplyToScreenName;

    Long inReplyToUserId;

    String languageCOde;

    String profileImageUrl;

    Boolean retweet;

    Long retweetCount;

    Boolean retweeted;

    String source;

    String text;

    Long toUserId;

    String unmodifiedText;

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public Integer getFavouriteCount() {
        return favouriteCount;
    }

    public void setFavouriteCount(Integer favouriteCount) {
        this.favouriteCount = favouriteCount;
    }

    public Boolean getFavorited() {
        return favorited;
    }

    public void setFavorited(Boolean favorited) {
        this.favorited = favorited;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getInReplyToScreenName() {
        return inReplyToScreenName;
    }

    public void setInReplyToScreenName(String inReplyToScreenName) {
        this.inReplyToScreenName = inReplyToScreenName;
    }

    public Long getInReplyToUserId() {
        return inReplyToUserId;
    }

    public void setInReplyToUserId(Long inReplyToUserId) {
        this.inReplyToUserId = inReplyToUserId;
    }

    public String getLanguageCOde() {
        return languageCOde;
    }

    public void setLanguageCOde(String languageCOde) {
        this.languageCOde = languageCOde;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public Boolean getRetweet() {
        return retweet;
    }

    public void setRetweet(Boolean retweet) {
        this.retweet = retweet;
    }

    public Long getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(Long retweetCount) {
        this.retweetCount = retweetCount;
    }

    public Boolean getRetweeted() {
        return retweeted;
    }

    public void setRetweeted(Boolean retweeted) {
        this.retweeted = retweeted;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public String getUnmodifiedText() {
        return unmodifiedText;
    }

    public void setUnmodifiedText(String unmodifiedText) {
        this.unmodifiedText = unmodifiedText;
    }
}


