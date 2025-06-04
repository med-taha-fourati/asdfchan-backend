package com.trbinc.asdfchanbackend.Models;

import lombok.*;
import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"boardId"})
@Entity
@Table(name = "boards")
public class Boards {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long boardId;
    private String boardName;
    private String boardDesc;
    private String boardIcon;
    private String boardBanner;

    // backend check to see if it doesnt have spaces
    private String boardSubName = GenerateDefaultName(boardName);

    public Boards(String board_name, String board_desc) {
        this.boardName = board_name;
        this.boardDesc = board_desc;

        this.boardSubName = GenerateDefaultName(board_name);
    }

    public Boards(String board_name, String board_desc, String board_icon) {
        this.boardName = board_name;
        this.boardDesc = board_desc;
        this.boardIcon = board_icon;

        this.boardSubName = GenerateDefaultName(board_name);
    }

    public Boards(String board_name, String board_desc, String board_icon, String board_banner) {
        this.boardName = board_name;
        this.boardDesc = board_desc;
        this.boardIcon = board_icon;
        this.boardBanner = board_banner;

        this.boardSubName = GenerateDefaultName(board_name);
    }

    public Boards(String board_name, String board_desc, String board_icon, String board_banner, String board_sub_name) {
        this.boardName = board_name;
        this.boardDesc = board_desc;
        this.boardIcon = board_icon;
        this.boardBanner = board_banner;

        this.boardSubName = board_sub_name;
    }

    private String GenerateDefaultName(@NotNull String board_sub_name) {
        if (board_sub_name == null) return "";
        return board_sub_name.toLowerCase().replaceAll(" ", "_");
    }
}
