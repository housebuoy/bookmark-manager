package com.dev.bookmarkmanager.service;

import com.dev.bookmarkmanager.model.Tag;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ScraperService {

    private final TagService tagService;

    public ScraperService(TagService tagService) {
        this.tagService = tagService;
    }

    public Map<String, Object> scrapeUrl(String urlInput, String userId) {
        Map<String, Object> result = new HashMap<>();

        String url = urlInput;
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }

        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) ...") // Keep your user agent
                    .timeout(5000)
                    .get();

            // --- IMPROVED TITLE EXTRACTION ---

            // 1. Try "og:site_name" first (This is usually just "Next.js", "GitHub", etc.)
            String title = doc.select("meta[property=og:site_name]").attr("content");

            // 2. Try "application-name"
            if (title.isEmpty()) {
                title = doc.select("meta[name=application-name]").attr("content");
            }

            // 3. Fallback to <title> with cleanup
            if (title.isEmpty()) {
                title = doc.title();

                // Split by common separators to remove taglines
                // e.g., "Next.js by Vercel - The React Framework" -> splits at " by " or " - "
                String[] separators = {" - ", " | ", " : ", " — ", " by "};

                for (String sep : separators) {
                    if (title.contains(sep)) {
                        // Keep only the part BEFORE the separator
                        title = title.split(Pattern.quote(sep))[0];
                        break;
                    }
                }
            }

            result.put("title", title.trim());

            // --- END TITLE EXTRACTION ---

            // ... (Rest of your description and tag logic remains the same) ...

            String description = doc.select("meta[name=description]").attr("content");
            if (description.isEmpty()) {
                description = doc.select("meta[property=og:description]").attr("content");
            }
            result.put("description", description);

            List<String> rawTags = new ArrayList<>();
            String keywords = doc.select("meta[name=keywords]").attr("content");
            if (!keywords.isEmpty()) {
                rawTags.addAll(Arrays.asList(keywords.split(",")));
            }
            if (rawTags.isEmpty()) {
                String ogTags = doc.select("meta[property=article:tag]").attr("content");
                if (!ogTags.isEmpty()) rawTags.add(ogTags);
            }

            List<String> cleanedTags = rawTags.stream()
                    .map(String::trim)
                    .filter(s -> !s.isBlank() && s.length() > 2)
                    .distinct()
                    .limit(5)
                    .collect(Collectors.toList());

            List<Tag> userTags = tagService.getAllTags(userId);
            Map<String, String> existingTagMap = userTags.stream()
                    .collect(Collectors.toMap(
                            tag -> tag.getName().toLowerCase(),
                            Tag::getName,
                            (existing, replacement) -> existing
                    ));

            List<String> finalTags = cleanedTags.stream()
                    .map(tag -> existingTagMap.getOrDefault(tag.toLowerCase(), tag))
                    .collect(Collectors.toList());

            result.put("tags", finalTags);

        } catch (IOException | IllegalArgumentException e) {
            throw new RuntimeException("Failed to scrape URL: " + e.getMessage());
        }
        return result;
    }
}