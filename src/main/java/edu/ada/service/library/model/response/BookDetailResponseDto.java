package edu.ada.service.library.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//DTO for books after comment has been added
public class BookDetailResponseDto {
    private Long id;
    private String name;
    private String author;
    private LocalDate publishDate;
    private CategoryResponseDto category;
    private boolean isAvailable;
    private String pickerEmail;
    private List<CommentResponseDto> comments;
}
