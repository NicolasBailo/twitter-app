
package core.db.repository;

import core.db.model.GeneratedTweetDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "generatedTweets", path = "generatedTweets")
public interface IGeneratedTweetRepository extends MongoRepository<GeneratedTweetDto, String> {

}
