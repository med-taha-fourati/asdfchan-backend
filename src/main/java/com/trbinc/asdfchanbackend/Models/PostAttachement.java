package com.trbinc.asdfchanbackend.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="post_attachement")
public class PostAttachement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name", nullable = false)
    private String fileName;
    @Column(name = "file_path", nullable = false)
    private String filePath;

    @ManyToOne
    @JoinColumn(name = "post_id", foreignKey = @ForeignKey(name = "post_attachement_post_fkey"))
    @JsonIgnore // Prevents infinite recursion during serialization
    private Post originalPost;

    public PostAttachement(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }
}
