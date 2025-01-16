package pl.jakubkonkol.tasteitserver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class CommentDto {
    private String commentId;
    private String userId;
    @NotBlank(message = "Content cannot be blank")
    @Size(min = 1, max = 500, message = "Content must be between 1 and 500 characters")
    private String content;
    @PastOrPresent(message = "Date cannot be in the future")
    private Date date;
}
