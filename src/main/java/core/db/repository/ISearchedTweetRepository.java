
package core.db.repository;

import core.db.model.GeneratedTweetDto;
import core.db.model.SearchedTweetDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import core.db.model.TweetJsonDto;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "searchedTweets", path = "searchedTweets")
public interface ISearchedTweetRepository extends MongoRepository<SearchedTweetDto, String> { //TweetJsonDto es la clase que enlaza con el repositorio, String el tipo de dato correspondiente al id de la clase

    List<SearchedTweetDto> findByTextContaining(@Param("text") String text);

}
