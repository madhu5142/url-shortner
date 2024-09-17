package com.example.mvp.service;

import com.example.mvp.exception.InvalidUrlException;
import com.example.mvp.exception.UrlNotFoundException;
import com.example.mvp.entity.UrlEntity;
import com.example.mvp.repository.UrlRepository;
import com.example.mvp.service.impl.UrlServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UrlShortenerServiceTest {

    @Mock
    private UrlRepository urlRepository;

    @InjectMocks
    private UrlServiceImpl urlService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shortenUrl_whenValidUrlIsProvided() {
        String longUrl = "https://www.example.com";
        String shortUrl = "12345678";
        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setLongUrl(longUrl);
        urlEntity.setShortUrl(shortUrl);

        when(urlRepository.findByLongUrl(longUrl)).thenReturn(Optional.empty());
        when(urlRepository.save(any(UrlEntity.class))).thenReturn(urlEntity);

        String result = urlService.shortenUrl(longUrl);
        //assertEquals("http://short.ly/" + , result);

    }

    @Test
    void shortenUrl_shouldThrowInvalidUrlException_whenInvalidUrlIsProvided() {
        String invalidUrl = "invalid-url";

        InvalidUrlException exception = assertThrows(InvalidUrlException.class, () -> {
            urlService.shortenUrl(invalidUrl);
        });

        assertEquals("Invalid URL format: " + invalidUrl, exception.getMessage());
        verify(urlRepository, never()).save(any());
    }

    @Test
    void getOriginalUrl_whenValidHashIsProvided() {
        String shortUrl = "12345678";
        String longUrl = "https://www.example.com";
        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setLongUrl(longUrl);
        urlEntity.setShortUrl(shortUrl);

        when(urlRepository.findByShortUrl(shortUrl)).thenReturn(Optional.of(urlEntity));

        String result = urlService.getOriginalUrl(shortUrl);
        assertEquals(longUrl, result);

        verify(urlRepository, times(1)).findByShortUrl(shortUrl);
    }

    @Test
    void getOriginalUrl_shouldThrowUrlNotFoundException_whenInvalidHashIsProvided() {
        String invalidHash = "invalidhash";

        when(urlRepository.findByShortUrl(invalidHash)).thenReturn(Optional.empty());

        UrlNotFoundException exception = assertThrows(UrlNotFoundException.class, () -> {
            urlService.getOriginalUrl(invalidHash);
        });

        assertEquals("Short url not found", exception.getMessage());
        verify(urlRepository, times(1)).findByShortUrl(invalidHash);
    }
}
