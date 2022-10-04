package edu.ada.service.library.model.mapper;

import edu.ada.service.library.model.entity.Comment;
import edu.ada.service.library.model.response.CommentResponseDto;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

//maps comment entity to DTOs in order to transfer them to DB

public class CommentMapper {
    public static CommentResponseDto mapEntityToDto(Comment comment) {
        var replies = comment.getReplies();

        return CommentResponseDto
                .builder()
                .id(comment.getId())
                .bookId(comment.getBook().getId())
                .text(comment.getText())
                .author(comment.getAuthor())
                .replies(replies != null ? mapEntitiesToDtos(replies) : null)
                .build();
    }

    public static List<CommentResponseDto> mapEntitiesToDtos(Iterable<Comment> comments) {
        return StreamSupport.stream(comments.spliterator(), false)
                .map(CommentMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }
}
