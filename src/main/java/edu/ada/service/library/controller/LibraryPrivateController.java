package edu.ada.service.library.controller;

import edu.ada.service.library.model.entity.User;
import edu.ada.service.library.model.request.CommentRequestDto;
import edu.ada.service.library.model.request.PickupRequestDto;
import edu.ada.service.library.model.response.CommentResponseDto;
import edu.ada.service.library.model.response.PickupResponseDto;
import edu.ada.service.library.service.LibraryService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/library/private")
public class LibraryPrivateController {

    private LibraryService libraryService;
    public LibraryPrivateController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @PostMapping("/pickup")
    public PickupResponseDto pickupBook(
            @Valid @RequestBody PickupRequestDto requestDto,
            @RequestAttribute(name = "user") User user //replaces token
    ) {
        return libraryService.pickupBook(requestDto, user);
    }

    @PostMapping("/dropoff")
    public PickupResponseDto dropOffBook(
            @Valid @RequestBody PickupRequestDto requestDto,
            @RequestAttribute(name = "user") User user
    ) {
        return libraryService.dropOffBook(requestDto, user);
    }

    @PostMapping("/books/{book_id}/comment") //post request should contain
                                                // exact book id that exists in DB
    public CommentResponseDto addComment(
            @Valid @RequestBody CommentRequestDto requestDto, //takes one comment instance
                                                            // of comment entity
            @PathVariable("book_id") Long bookId, //one book
            @RequestAttribute(name = "user") User user //and one user as its parameters
    ) {
        return libraryService.addComment(bookId, requestDto, user); //passes the related info to
                                    //to addComment method of libraryService
                                    //in order to implement the business logic
    }

    @PostMapping("/comments/{comment_id}/reply") //post request should contain
                                                    // exact book id that exists in DB
    public CommentResponseDto replyToComment(
            @PathVariable("comment_id") String commentId, //takes id of the comment that it replies
            @Valid @RequestBody CommentRequestDto requestDto, // reply text
            @RequestAttribute(name = "user") User user //one user
    ) {
        return libraryService.replyToComment(commentId, requestDto, user);//passes the related info to
        //to replyToComment method of libraryService
        //in order to implement the business logic
    }
}
