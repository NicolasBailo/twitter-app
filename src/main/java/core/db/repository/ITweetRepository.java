
package core.db.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import core.db.model.TweetJsonDto;

@RepositoryRestResource(collectionResourceRel = "tweets", path = "tweets")
public interface ITweetRepository extends MongoRepository<TweetJsonDto, Long> { //TweetJsonDto es la clase que enlaza con el repositorio, String el tipo de dato correspondiente al id de la clase

	//List<TweetJsonDto> findByLastName(@Param("name") String name);

}
