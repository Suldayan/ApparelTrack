package com.example.webscrape_goat_service.Service;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

    private final Logger logger = LoggerFactory.getLogger(ItemService.class);

    private final Page page;

    public ItemService() {
        Playwright playwright = Playwright.create();
        Browser browser = playwright.webkit().launch();
        this.page = browser.newPage();
    }

    private void navigateToSearch(String userSearchQuery) {
        try {
            String base_goat_url = "https://www.goat.com/en-ca";
            String requestParam = "/search?query=";
            page.navigate(base_goat_url + requestParam + userSearchQuery);
        } catch (Exception e) {
            logger.error("Error navigating to '{}': {}", userSearchQuery, e.getMessage());
            throw new RuntimeException("Failed to navigate to: " + userSearchQuery);
        }
    }

    public String getItemName(String userSearchQuery) {
        try {
            navigateToSearch(userSearchQuery);
            String selector = "[data-grid-cell-position='1'] [data-qa='grid_cell_product_name']";
            return page.locator(selector).textContent();
        } catch (Exception e) {
            logger.error("Error scraping item name for '{}': {}", userSearchQuery, e.getMessage());
            return "Failed to scrape item name for: " + userSearchQuery + ". Error: " + e.getMessage();
        }
    }

    public String getItemSizeAndPrice(String userSearchQuery, String size) {
        try {
            navigateToSearch(userSearchQuery);
            String sizeSelector = "[data-grid-cell-position='1'] [data-qa='buy_bar_size_" + size + "']";
            String priceSelector = "[data-grid-cell-position='1'] [data-qa='buy_bar_price_size_" + size + "']";

            return page.locator(sizeSelector).textContent() + " - " + page.locator(priceSelector).textContent();
        } catch (Exception e) {
            logger.error("Error scraping item size ({}) for '{}': {}", size, userSearchQuery, e.getMessage());
            return "Failed to scrape item size (" + size + ") for: " + userSearchQuery + ". Error: " + e.getMessage();
        }
    }

    @PreDestroy
    public void cleanup() {
        if (page != null) {
            page.close();
        }
        logger.info("Playwright instance closed");
    }
}

