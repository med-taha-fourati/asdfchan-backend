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
            if (res.isEmpty()) return new ResponseEntity<>("No boards available. Contact an admin to resolve this issue", HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/api/admin/boards/create")
    public ResponseEntity<?> CreateBoard(@RequestBody Boards board) {
        try {
            if (board.getBoardName() == null || board.getBoardDesc() == null) {
                return new ResponseEntity<>("Board data is missing. Minimum needed boardName and boardDesc", HttpStatus.BAD_REQUEST);
            }
            if (board.getBoardName().isEmpty()) return new ResponseEntity<>("Name should not be empty", HttpStatus.BAD_REQUEST);
            board.setBoardSubName(board.GenerateDefaultName(board.getBoardName()));
            boardsRepository.save(board);
            return new ResponseEntity<>("Board created", HttpStatus.OK);
        } catch (Exception ex) {
            System.out.println("Error while creating board: " + ex.getMessage());
            return new ResponseEntity<>("Internal Server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    } // verify admin existence too

    @CrossOrigin(origins = "*")
    @DeleteMapping("/api/admin/boards/delete")
    public ResponseEntity<?> DeleteBoard(@RequestParam long boardId) {
        try {
            //if (boardId == null) return new ResponseEntity<>("Id should not be empty", HttpStatus.BAD_REQUEST);
            boardsRepository.deleteById(boardId);
            return new ResponseEntity<>("Board created", HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>("Internal Server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    } // verify admin existence too
}
