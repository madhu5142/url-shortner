package com.example.mvp.controller;

import com.example.mvp.exception.InvalidUrlException;
import com.example.mvp.exception.UrlNotFoundException;
import com.example.mvp.service.UrlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UrlController.class)
class UrlControllerTest {

    @MockBean
    private UrlService urlService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
    	
    }

    @Test
    void shortenUrl_shouldReturnShortenedUrl_whenValidUrlIsProvided() throws Exception {
        String longUrl = "https://www.example.com";
        String shortUrl = "http://localhost:8080/12345678";

        when(urlService.shortenUrl(longUrl)).thenReturn(shortUrl);

        mockMvc.perform(post("/shorten")
                .content(longUrl)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(shortUrl));

    }

    @Test
    void shortenUrl_shouldReturnBadRequest_whenInvalidUrlIsProvided() throws Exception {
        String invalidUrl = "invalid-url";

        when(urlService.shortenUrl(invalidUrl)).thenThrow(new InvalidUrlException(invalidUrl));

        mockMvc.perform(post("/shorten")
                .content(invalidUrl)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid URL format: " + invalidUrl));

    }

    @Test
    void getOriginalUrl_shouldReturnLongUrl_whenValidHashIsProvided() throws Exception {
        String shortUrlHash = "12345678";
        String longUrl = "https://localhost:8080/12345678";

        when(urlService.getOriginalUrl(shortUrlHash)).thenReturn(longUrl);

        mockMvc.perform(get("/{hash}", shortUrlHash))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void getOriginalUrl_shouldReturnNotFound_whenInvalidHashIsProvided() throws Exception {
        String invalidHash = "invalidhash";

        when(urlService.getOriginalUrl(invalidHash)).thenThrow(new UrlNotFoundException());

        mockMvc.perform(get("/{hash}", invalidHash))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Short url not found"));

    }
}
