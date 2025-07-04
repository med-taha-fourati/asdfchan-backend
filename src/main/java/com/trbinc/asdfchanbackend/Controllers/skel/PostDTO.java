package com.trbinc.asdfchanbackend.Controllers.skel;

import com.trbinc.asdfchanbackend.Models.PostAttachement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private String postTitle;
    private String postContent;
    private List<PostAttachement> postAttachements;
}
