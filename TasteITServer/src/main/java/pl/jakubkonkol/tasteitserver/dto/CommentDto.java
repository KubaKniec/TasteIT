package pl.jakubkonkol.tasteitserver.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CommentDto {
    private String commentId;
    private String userId;
    private String content;
    private Date date;
}
