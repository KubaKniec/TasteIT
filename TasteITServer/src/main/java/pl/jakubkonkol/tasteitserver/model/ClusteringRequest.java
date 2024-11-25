package pl.jakubkonkol.tasteitserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.jakubkonkol.tasteitserver.dto.PostDto;

import java.util.List;
@Data
@AllArgsConstructor
public class ClusteringRequest {
    private String correlationId;
    private List<PostDto> posts;
}
