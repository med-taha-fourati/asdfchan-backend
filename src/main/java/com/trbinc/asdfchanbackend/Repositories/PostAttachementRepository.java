package com.trbinc.asdfchanbackend.Repositories;

import com.trbinc.asdfchanbackend.Models.PostAttachement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostAttachementRepository extends JpaRepository<PostAttachement, Long> {
    // Additional query methods can be defined here if needed
}
