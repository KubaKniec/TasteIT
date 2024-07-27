package pl.jakubkonkol.tasteitserver.dto;

import lombok.Data;

import java.util.List;

@Data
public class PageDto<T> {
    private List<T> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
}
