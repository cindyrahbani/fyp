package murex.dev.mxem.Scheduler.config;

import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

//@Configuration
public class MongoConfig  {

    //@Bean
    public MongoClient mongo() {
        return new MongoClient("localhost",27017);
    }

   // @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongo(), "requests");
    }
}