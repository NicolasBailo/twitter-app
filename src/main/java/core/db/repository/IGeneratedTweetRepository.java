
package core.db.repository;

import core.db.model.GeneratedTweetDto;
import core.db.model.SearchedTweetDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "generatedTweets", path = "generatedTweets")
public interface IGeneratedTweetRepository extends MongoRepository<GeneratedTweetDto, String> { //TweetJsonDto es la clase que enlaza con el repositorio, String el tipo de dato correspondiente al id de la clase

	//List<TweetJsonDto> findByLastName(@Param("name") String name);

}
