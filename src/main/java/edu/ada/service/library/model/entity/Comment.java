package edu.ada.service.library.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data //lombok - adds getter and setter to entity
@NoArgsConstructor //lombok - adds constructor
@AllArgsConstructor //lombok - adds constructor
@Builder
@Entity //marks this class as entity of application
public class Comment {
    @Id
    private String id; //this is a special ID for comments, it is not related to user or book IDs
    private String text; //comment text
    private String author; //author will be resembled with user email
    @OneToOne
    private Book book; //each comment belongs to one book
    @OneToMany
    private List<Comment> replies; //each comment can have multiple replies
}
