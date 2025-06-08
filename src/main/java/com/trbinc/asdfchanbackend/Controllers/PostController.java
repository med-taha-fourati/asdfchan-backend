package com.trbinc.asdfchanbackend.Controllers;

import com.trbinc.asdfchanbackend.Models.Boards;
import com.trbinc.asdfchanbackend.Models.Post;
import com.trbinc.asdfchanbackend.Repositories.BoardsRepository;
import com.trbinc.asdfchanbackend.Repositories.PostRepository;
import com.trbinc.asdfchanbackend.Services.PostService;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
public class PostController {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private BoardsRepository boardsRepository;

    private final PostService postService;
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @CrossOrigin(origins="*")
    @GetMapping("/api/posts")
    public ResponseEntity<?> GetPostsByBoard(@NotNull @RequestParam Boards boards) {
        try {
            List<Post> res = postRepository.findByOriginBoard(boards);
            if (res.isEmpty()) return new ResponseEntity<>("No posts have been made for this board yet...", HttpStatus.NOT_FOUND);
            return new ResponseEntity<List<Post>>(res, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>("Internal server error while fetching posts", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @CrossOrigin(origins="*")
    @GetMapping("/api/posts/all")
    public ResponseEntity<?> GetPosts() {
        try {
            List<Post> res = postRepository.findAll();
            if (res.isEmpty()) return new ResponseEntity<>("No posts have been made for this board yet...", HttpStatus.NOT_FOUND);
            return new ResponseEntity<List<Post>>(res, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>("Internal server error while fetching posts", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins="*")
    @PostMapping("/api/posts/create")
    public ResponseEntity<?> CreatePostInBoard(@RequestBody Post newpost,
                                               @RequestParam Long boardId) {
        try {
            // if board fetching doesnt work we'll try fetching it ourselves
            // or we add it as a request param
            Optional<Boards> board = Optional.ofNullable(
                boardsRepository.findById(boardId)
                    .orElseThrow(() -> new IllegalArgumentException("Board with ID " + boardId + " does not exist."))
            );

            Boards boardObj = board.get();

            newpost.setPostNo((int)postService.GenerateNewPostNumber());
            newpost.setOriginBoard(boardObj);

            // there is prob a better way to do this
            newpost.getPostAttachements().stream()
                .forEach(attachment -> attachment.setOriginalPost(newpost));

            postRepository.save(newpost);
            return new ResponseEntity<>(newpost, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>("Internal server error while adding posts" + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins="*")
    @DeleteMapping("/api/posts/delete")
    public ResponseEntity<?> DeletePostInBoard(@RequestParam Long postId) {
        try {
            postRepository.deleteById(postId);
            return new ResponseEntity<>("Deleted post with success", HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>("Internal server error while adding posts" + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
