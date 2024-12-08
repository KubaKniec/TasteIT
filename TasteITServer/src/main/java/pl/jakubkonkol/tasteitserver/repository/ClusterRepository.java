package pl.jakubkonkol.tasteitserver.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.jakubkonkol.tasteitserver.model.Cluster;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ClusterRepository extends MongoRepository<Cluster, String> {
    boolean existsByClusterId(String clusterId);
    Optional<Cluster> findByClusterId(String clusterId);
    List<Cluster> findByClusterIdIn(Collection<String> clusterIds);
}
