package com.trbinc.asdfchanbackend.Controllers;

import com.trbinc.asdfchanbackend.Controllers.skel.PostDTO;
import com.trbinc.asdfchanbackend.Middleware.JWTUtil;
import com.trbinc.asdfchanbackend.Models.Boards;
import com.trbinc.asdfchanbackend.Models.Post;
import com.trbinc.asdfchanbackend.Models.User;
import com.trbinc.asdfchanbackend.Repositories.BoardsRepository;
import com.trbinc.asdfchanbackend.Repositories.PostRepository;
import com.trbinc.asdfchanbackend.Repositories.UserRepository;
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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTUtil jwtUtil;

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
    public ResponseEntity<?> CreatePostInBoard(@RequestBody PostDTO givenPost,
                                               @RequestParam Long boardId) {
        try {
            Post newpost = new Post(
                givenPost.getPostTitle(),
                givenPost.getPostContent(),
                givenPost.getPostAttachements()
            );
            String authorJwtToken = givenPost.getAuthorJwtToken();
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

            // there is prob a better way to do this part 2
            if (authorJwtToken != null && !authorJwtToken.isEmpty()) {

                if (jwtUtil.isTokenExpired(authorJwtToken)) {
                    return new ResponseEntity<>("Author JWT token is expired", HttpStatus.UNAUTHORIZED);
                }
                String author = jwtUtil.extractUsername(authorJwtToken);
                Optional<User> user = userRepository.findByUsername(author);
                if (user.isEmpty()) {
                    return new ResponseEntity<>("Author not found", HttpStatus.NOT_FOUND);
                }
                newpost.setPostAuthor(user.get());
            }

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
