package com.trbinc.asdfchanbackend.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long postId;

    private int postNo;
    private String postTitle;
    private String postContent;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false, foreignKey = @ForeignKey(name="post_post_board_fkey"))
    private Boards originBoard;

    @OneToMany(mappedBy = "originalPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostAttachement> postAttachements;

    @ManyToOne
    @JoinColumn(name = "author_id", foreignKey = @ForeignKey(name="post_post_author_fkey"))
    private User postAuthor;

    public Post(String postTitle, String postContent) {
        this.postTitle = postTitle;
        this.postContent = postContent;
    }

    public Post(String postTitle, String postContent, List<PostAttachement> postAttachements) {
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postAttachements = postAttachements;
    }

    public Post(String postTitle, String postContent, Boards originBoard, User postAuthor) {
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.originBoard = originBoard;
        this.postAuthor = postAuthor;
    }
}
