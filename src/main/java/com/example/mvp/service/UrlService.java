package com.example.mvp.service;

public interface UrlService {
	
	public String shortenUrl(String longUrl);
	
	public String getOriginalUrl(String hash);

}
