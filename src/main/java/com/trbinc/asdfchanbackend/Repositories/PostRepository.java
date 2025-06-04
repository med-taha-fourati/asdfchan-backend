package com.trbinc.asdfchanbackend.Repositories;

import com.trbinc.asdfchanbackend.Models.Boards;
import com.trbinc.asdfchanbackend.Models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByOriginBoard(Boards originBoard);
}
