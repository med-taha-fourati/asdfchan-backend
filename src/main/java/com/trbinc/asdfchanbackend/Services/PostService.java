package com.trbinc.asdfchanbackend.Services;

import com.trbinc.asdfchanbackend.Models.Post;
import com.trbinc.asdfchanbackend.Repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    public long GenerateNewPostNumber() {
        return postRepository.count() + 1;
    }
}
