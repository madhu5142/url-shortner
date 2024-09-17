package com.example.mvp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class UrlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String longUrl;
    private String shortUrl;

}