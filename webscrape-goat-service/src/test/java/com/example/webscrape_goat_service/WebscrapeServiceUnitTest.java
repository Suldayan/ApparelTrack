package com.example.webscrape_goat_service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.webscrape_goat_service.Service.WebscrapeService;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WebscrapeServiceUnitTest {

    @Mock
    private Browser browser;

    @Mock
    private Page page;

    @InjectMocks
    private  WebscrapeService webscrapeService;

    private static final String BASE_URL = "https://example.com/";

    @BeforeEach
    void setUp() {
        webscrapeService = new WebscrapeService(browser, BASE_URL);
    }

    @Test
    void getItemTemplateID_WithValidQuery_ReturnsTemplateID() {
        // Arrange
        String userSearchQuery = "testQuery";
        String expectedTemplateId = "template123";
        String htmlContent = "<div data-grid-cell-position='1' data-grid-cell-name='template123'></div>";

        when(browser.newPage()).thenReturn(page);
        when(page.content()).thenReturn(htmlContent);

        // Act
        String result = webscrapeService.getItemTemplateID(userSearchQuery);

        // Assert
        assertEquals(expectedTemplateId, result);
        verify(page, times(2)).navigate(BASE_URL + userSearchQuery);
        verify(page).waitForSelector("[data-grid-cell-position]");
        verify(page, times(2)).close();
    }

    @Test
    void testRenderWebsite_Failure() {
        // Arrange
        String userSearchQuery = "testQuery";
        when(browser.newPage()).thenThrow(new RuntimeException("Network Issue"));

        // Act
        Page result = webscrapeService.renderWebsite(userSearchQuery);

        // Assert
        assertNull(result);
    }
}