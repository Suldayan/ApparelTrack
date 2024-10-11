package com.example.webscrape_goat_service.Service;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import jakarta.annotation.PreDestroy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class WebscrapeService {

    public final Logger logger = LoggerFactory.getLogger(WebscrapeService.class);
    private final Page page;

    public WebscrapeService() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch();
            page = browser.newPage();

            logger.info("Starting Playwright instance");
        }
    }

    public String renderWebsite(String userSearchQuery) {
        String base_url = "https://www.goat.com/en-ca/search?query=";
        try {
            page.navigate(base_url + userSearchQuery);
        } catch (Exception e) {
            logger.error("Failed to navigate to page with query: {} - {}",
                    userSearchQuery, e.getMessage());
            return null;
        }

        logger.info("Successfully navigated to page with query: {}", userSearchQuery);

        return page.content();
    }

    public String getItemTemplateID(String userSearchQuery) {
        Document document = Jsoup.parse(renderWebsite(userSearchQuery));
        try {
            document.selectFirst("[data-grid-cell-position]");
        } catch (NoSuchElementException ignored) {
            logger.error("Failed to get the {} template id", userSearchQuery);
            return null;
        }

        logger.info("Successfully grabbed {}'s template id: {}",
                userSearchQuery, document.attr("]data-grid-cell-name]"));

        return document.attr("[data-grid-cell-name]");
    }

    @PreDestroy
    public void cleanup() {
        if (page != null) {
            page.close();
            logger.info("The Playwright instance has been closed");
        }
    }
}
