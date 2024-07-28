package pl.jakubkonkol.tasteitserver.config;


import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.connection.ConnectionPoolSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing
public class MongoConfig{
    @Bean
    public MongoClient mongoClient(){
        var MONGO_USERNAME = System.getenv("MONGO_USERNAME");
        var MONGO_PASSWORD = System.getenv("MONGO_PASSWORD");
        var MONGO_DB_NAME = System.getenv("MONGO_DB_NAME");
        var MONGO_URL = ("mongodb://"+MONGO_USERNAME+':'+MONGO_PASSWORD+"@localhost:27017/"+MONGO_DB_NAME+"?authSource=admin");
        ConnectionString connectionString = new ConnectionString(MONGO_URL);
        ConnectionPoolSettings connectionPoolSettings = ConnectionPoolSettings.builder()
                .maxSize(150)
                .minSize(10)
                .build();
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .applyToConnectionPoolSettings(builder -> builder.applySettings(connectionPoolSettings))
                .build();

        return MongoClients.create(mongoClientSettings);
    }

}