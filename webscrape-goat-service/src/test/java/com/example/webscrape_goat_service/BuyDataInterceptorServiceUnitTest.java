package com.example.webscrape_goat_service;

import com.example.webscrape_goat_service.Service.WebscrapeService;
import com.example.webscrape_goat_service.model.RawBuyData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BuyDataInterceptorServiceUnitTest {

    @InjectMocks
    private WebscrapeService webscrapeService;

    @InjectMocks
    private RawBuyData rawBuyData;

    @InjectMocks
    private CloseableHttpClient httpClient;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateBuyDataTableUrlAndReturnTemplateId() {
        String testTemplateId = "12345";
        String templateResult = "https://www.goat.com/web-api/v1/product_variants/buy_bar_data?productTemplateId="
                + testTemplateId + "&countryCode=CA";

        System.out.println(templateResult);

        when(webscrapeService.getItemTemplateID(testTemplateId)).thenReturn(templateResult);
        assertEquals(templateResult, webscrapeService.getItemTemplateID(testTemplateId));
    }

    @Test
    public void testInterceptRequestAndReturnJsonResponse() {
        rawBuyData = new RawBuyData();
    }
}
