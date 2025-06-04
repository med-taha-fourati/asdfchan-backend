package com.trbinc.asdfchanbackend.Repositories;
import com.trbinc.asdfchanbackend.Models.Boards;
import org.springframework.data.jpa.repository.JpaRepository;
public interface BoardsRepository extends JpaRepository<Boards, Long> { }
