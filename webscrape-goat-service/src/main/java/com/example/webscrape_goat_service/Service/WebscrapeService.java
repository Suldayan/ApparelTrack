package com.example.webscrape_goat_service.Service;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import jakarta.annotation.PreDestroy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebscrapeService {

    public static final Logger logger = LoggerFactory.getLogger(WebscrapeService.class);
    private final Browser browser;
    private final String baseUrl;

    @Autowired
    public WebscrapeService(Browser browser, String baseUrl) {
        this.browser = browser;
        this.baseUrl = baseUrl;
        logger.info("WebscrapeService initialized with browser and base URL: {}", baseUrl);
    }

    public Page renderWebsite(String userSearchQuery) {
        try (Page page = browser.newPage()) {
            page.navigate(baseUrl + userSearchQuery);
            logger.info("Successfully navigated to page with query: {}", userSearchQuery);
            return page;
        } catch (Exception e) {
            logger.error("Failed to navigate to page with query: {} - {}",
                    userSearchQuery, e.getMessage());
            return null;
        }
    }

    public String getItemTemplateID(String userSearchQuery) {
        try (Page page = renderWebsite(userSearchQuery)) {
            page.navigate(baseUrl + userSearchQuery);

            // Wait for the content to load
            page.waitForSelector("[data-grid-cell-position]");

            String pageContent = page.content();
            Document document = Jsoup.parse(pageContent);

            // Select the first element with data-grid-cell-position
            var element = document.selectFirst("[data-grid-cell-position]");

            if (element == null) {
                logger.error("No element found with data-grid-cell-position for query: {}", userSearchQuery);
                return null;
            }

            // Get the data-grid-cell-name attribute from this element
            String templateId = element.attr("data-grid-cell-name");

            if (templateId.isEmpty()) {
                logger.error("data-grid-cell-name attribute is empty for query: {}", userSearchQuery);
                return null;
            }

            logger.info("Successfully grabbed {}'s template id: {}", userSearchQuery, templateId);
            return templateId;
        } catch (Exception e) {
            logger.error("Error occurred while getting template ID for {}: {}", userSearchQuery, e.getMessage());
            return null;
        }
    }

    @PreDestroy
    public void cleanup() {
        if (browser != null) {
            browser.close();
            logger.info("The Browser instance has been closed");
        }
    }
}