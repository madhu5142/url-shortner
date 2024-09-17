package com.example.mvp.service.impl;

import com.example.mvp.entity.UrlEntity;
import com.example.mvp.exception.InvalidUrlException;
import com.example.mvp.exception.UrlNotFoundException;
import com.example.mvp.repository.UrlRepository;
import com.example.mvp.service.UrlService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

@Service
public class UrlServiceImpl implements UrlService {

    @Autowired
    private UrlRepository urlRepository;
    
    @Value("${url.path}")
    String urlPath;

    @Cacheable(value = "url", key = "longUrl", unless = "result == null")
    public String shortenUrl(String longUrl) {
        
    	validateUrl(longUrl);
    	
        Optional<UrlEntity> existingMapping = urlRepository.findByLongUrl(longUrl);
        if (existingMapping.isPresent()) {
            return urlPath + existingMapping.get().getShortUrl();
        }

        String hash = DigestUtils.sha256Hex(longUrl).substring(0, 8);

        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setLongUrl(longUrl);
        urlEntity.setShortUrl(hash);
        urlRepository.save(urlEntity);

        return urlPath + hash;
    }

    @Cacheable(value = "url", key = "hash", unless = "result == null")
    public String getOriginalUrl(String hash) {
        
        Optional<UrlEntity> entity = urlRepository.findByShortUrl(hash);
        return entity.map(UrlEntity::getLongUrl).orElseThrow(() -> new UrlNotFoundException());
    }
    
    private void validateUrl(String url) {
        try {
            new URL(url);
        } catch (MalformedURLException e) {
            throw new InvalidUrlException(url);
        }
    }
}