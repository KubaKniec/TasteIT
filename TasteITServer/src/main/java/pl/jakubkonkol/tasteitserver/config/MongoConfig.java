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
        ConnectionString connectionString = new ConnectionString("mongodb://tasteitroot:tasteitserver10@localhost:27017/tasteit_db?authSource=admin");
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