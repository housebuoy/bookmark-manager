package com.dev.bookmarkmanager.controller;

import com.dev.bookmarkmanager.service.ScraperService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/scraper")
@CrossOrigin(origins = {"http://localhost:3000", "https://bookmark-manager-xyz.vercel.app"})
public class ScraperController {

    private final ScraperService scraperService;

    public ScraperController(ScraperService scraperService) {
        this.scraperService = scraperService;
    }

    @PostMapping("/preview")
    public ResponseEntity<Map<String, Object>> scrapeUrl(@RequestBody Map<String, String> payload, Principal principal) {
        String url = payload.get("url");
        if (url == null || url.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        // Pass userId to service to perform tag matching
        return ResponseEntity.ok(scraperService.scrapeUrl(url, principal.getName()));
    }
}