package com.example.webscrape_goat_service.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuyDataInterceptorService {

    private final Logger logger = LoggerFactory.getLogger(BuyDataInterceptorService.class);

    @Autowired
    private WebscrapeService webscrapeService;

    public String createBuyDataTableUrl(String userSearchQuery) {
        String templateId = webscrapeService.getItemTemplateID(userSearchQuery);

        return "https://www.goat.com/web-api/v1/product_variants/buy_bar_data?productTemplateId="
                + templateId +"&countryCode=CA";
    }

    public ObjectNode interceptRequest(String userSearchQuery) {
        String url = createBuyDataTableUrl(userSearchQuery);
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(request);

            return (ObjectNode) response;
        } catch (Exception e) {
            logger.error("Failed to intercept buy data request at: {} for: {} - {}",
                    url, userSearchQuery, e.getMessage());
            return null;
        }
    }

}
