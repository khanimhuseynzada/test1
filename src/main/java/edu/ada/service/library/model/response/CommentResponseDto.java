package edu.ada.service.library.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
//DTO of a comment
public class CommentResponseDto {
    private String id;
    private String author;
    private String text;
    private Long bookId;
    private List<CommentResponseDto> replies;
}