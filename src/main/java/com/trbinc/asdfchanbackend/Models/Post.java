package com.trbinc.asdfchanbackend.Models;

import jakarta.persistence.*;
import lombok.*;

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
    @JoinColumn(foreignKey = @ForeignKey(name="post_post_board_fkey"))
    private Boards originBoard;
}
