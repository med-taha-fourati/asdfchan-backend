package com.trbinc.asdfchanbackend.Controllers;

import com.trbinc.asdfchanbackend.Models.Boards;
import com.trbinc.asdfchanbackend.Repositories.BoardsRepository;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BoardsController {
    @Autowired
    private BoardsRepository boardsRepository;

    @CrossOrigin(origins = "*")
    @GetMapping("/api/boards")
    public ResponseEntity<?> GetBoards() {
        try {
            List<Boards> res = boardsRepository.findAll();
            if (res.isEmpty())
                return new ResponseEntity<>("No boards available. Contact an admin to resolve this issue", HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/api/admin/boards/create")
    public ResponseEntity<?> CreateBoard(@NotNull @RequestBody Boards board) {
        try {
            if (board.getBoardName().isEmpty()) return new ResponseEntity<>("Name should not be empty", HttpStatus.BAD_REQUEST);
            boardsRepository.save(board);
            return new ResponseEntity<>("Board created", HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>("Internal Server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    } // verify admin existence too

    @CrossOrigin(origins = "*")
    @DeleteMapping("/api/admin/boards/delete")
    public ResponseEntity<?> DeleteBoard(@NotNull @RequestBody Boards board) {
        try {
            if (board.getBoardName().isEmpty()) return new ResponseEntity<>("Name should not be empty", HttpStatus.BAD_REQUEST);
            boardsRepository.delete(board);
            return new ResponseEntity<>("Board created", HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>("Internal Server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    } // verify admin existence too
}
