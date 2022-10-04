package edu.ada.service.library.service.impl;

import edu.ada.service.library.controller.ErrorHandler;
import edu.ada.service.library.exception.InvalidPickupException;
import edu.ada.service.library.exception.NotExistsException;
import edu.ada.service.library.model.entity.Book;
import edu.ada.service.library.model.entity.Comment;
import edu.ada.service.library.model.entity.Pickup;
import edu.ada.service.library.model.entity.User;
import edu.ada.service.library.model.mapper.BookMapper;
import edu.ada.service.library.model.mapper.CategoryMapper;
import edu.ada.service.library.model.mapper.CommentMapper;
import edu.ada.service.library.model.mapper.PickupMapper;
import edu.ada.service.library.model.repository.BookRepository;
import edu.ada.service.library.model.repository.CategoryRepository;
import edu.ada.service.library.model.repository.CommentRepository;
import edu.ada.service.library.model.repository.PickupRepository;
import edu.ada.service.library.model.request.CommentRequestDto;
import edu.ada.service.library.model.request.PickupRequestDto;
import edu.ada.service.library.model.request.SearchBookParams;
import edu.ada.service.library.model.response.*;
import edu.ada.service.library.service.LibraryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LibraryServiceImpl implements LibraryService {
    private Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    private BookRepository bookRepository;
    private CategoryRepository categoryRepository;
    private PickupRepository pickupRepository;
    private CommentRepository commentRepository;

    public LibraryServiceImpl(
            BookRepository bookRepository,
            CategoryRepository categoryRepository,
            PickupRepository pickupRepository,
            CommentRepository commentRepository
    ) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.pickupRepository = pickupRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public List<CategoryResponseDto> listCategories() {
        logger.info("Service List categories: started");

        var categories = categoryRepository.findAll();

        logger.info("Service List categories: ended");
        return CategoryMapper.mapEntitiesToDtos(categories);
    }

    @Override
    public List<BookResponseDto> listBooks() {
        logger.info("Service List books: started");

        var books = bookRepository.findAll();

        logger.info("Service List books: ended");
        return BookMapper.mapEntitiesToDtos(books);
    }

    @Override
    public List<BookResponseDto> searchBooks(SearchBookParams params) {
        logger.info("*** Search books started ***");

        List<Book> books;

            books = bookRepository.findAllByNameContainingAndCategory_idAndAuthorContaining(
                    params.getName(),
                    params.getCategoryId(),
                    params.getAuthor()
            );

        logger.info("*** Search books finished ***");
        return BookMapper.mapEntitiesToDtos(books);
    }

    @Override
    public BookDetailResponseDto getBookById(Long bookId) {
        logger.info("getBookById: started");

        Book book = bookRepository
                .findById(bookId)
                .orElseThrow(() -> new NotExistsException("*** Book not Found ***"));

        logger.info("*** Book found ***");

        var comments = commentRepository.findByBookId(bookId);

        logger.info("getBookById: finished");
        return BookMapper.mapEntityToDetailedDto(book, CommentMapper.mapEntitiesToDtos(comments));
    }

    @Override
    public PickupResponseDto pickupBook(PickupRequestDto requestDto, User user) {
        logger.info("Search Pick up book: started");

        Book book = bookRepository
                .findById(requestDto.getBookId())
                .orElseThrow(() -> new NotExistsException("*** Not Found ***"));

        Pickup pickup = pickupRepository.findByBookAndDropOffFalse(book);

        if (pickup != null) {
            if (pickup.getUser().getId().equals(user.getId())) {
                throw new InvalidPickupException("*** Book is picked up ***");
            }
            throw new InvalidPickupException("*** Unavailable book ***");
        }

        var newPickup = Pickup
                .builder()
                .book(book)
                .user(user)
                .build();

        pickupRepository.save(newPickup);

        logger.info("*** Search for pickup finished ***");
        return PickupMapper.mapEntityToDto(newPickup);
    }

    @Override
    public PickupResponseDto dropOffBook(PickupRequestDto requestDto, User user) {
        logger.info("*** Search for dropoff started ***");

        Book book = bookRepository
                .findById(requestDto.getBookId())
                .orElseThrow(() -> new NotExistsException("*** Book does not exist in the database ***"));

        Pickup pickup = pickupRepository.findByBookAndUserAndDropOffFalse(book, user);

        if (pickup == null) {
            throw new InvalidPickupException("*** Book does not belong to this user ***");
        }

        pickup.setDropOff(true);
        pickupRepository.save(pickup);

        logger.info("*** Search for dropoff finished ***");
        return PickupMapper.mapEntityToDto(pickup);
    }

    @Override
    public CommentResponseDto addComment(Long bookId, CommentRequestDto requestDto, User user) {
        logger.info("*** Service addComment started ***");

        Book book = bookRepository
                .findById(bookId) //finds book by its ID
                .orElseThrow(() -> new NotExistsException("*** Book does not exist in the database ***"));
        //throws exception if book is not found

        logger.info("*** Book found ***");


        var newComment = Comment.builder()  //creates a comment based on passed parameters
                .author(user.getEmail()) //user email
                .book(book) //info about specified book
                .text(requestDto.getText()) //text of comment
                .build();
        commentRepository.save(newComment); //save is a method of mongo repository and saves
                                            //new comment as an entity

        logger.info("*** Comment created ***");
        logger.info("*** Service addComment finished ***");

        return CommentMapper.mapEntityToDto(newComment); //here entity is mapped to DTO in
                                                        // order to save to the DB
    }

    @Override
    public CommentResponseDto replyToComment(String commentId, CommentRequestDto requestDto, User user) {
        logger.info("*** Service replyToComment started ***");

        Comment comment = commentRepository
                .findById(commentId) //finds comment
                .orElseThrow(() -> new NotExistsException("*** Comment not found ***"));

        logger.info("*** Comment found ***");

        //generate a new instance of comment in order to save it as a reply
        var newComment = Comment.builder()
                .author(user.getEmail())
                .book(comment.getBook()) //here we get book information using initial comment
                .text(requestDto.getText())
                .build();

        if (comment.getReplies() == null) { //if there is no reply
            comment.setReplies(new ArrayList<>()); //create a new arraylist
        }
        comment.getReplies().add(newComment); //if arraylist already exists, then add new reply to there
        commentRepository.save(comment); //save changes to the comment

        //no need to save reply additionally

        logger.info("*** Service replyToComment started ***");

        return CommentMapper.mapEntityToDto(newComment); //map to DTO
    }
}

