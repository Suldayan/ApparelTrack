package com.example.webscrape_goat_service.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuyDataInterceptorService {

    private final Logger logger = LoggerFactory.getLogger(BuyDataInterceptorService.class);

    @Autowired
    private WebscrapeService webscrapeService;

    @Autowired
    private ObjectMapper objectMapper;

    public String createBuyDataTableUrl(String userSearchQuery) {
        String templateId = webscrapeService.getItemTemplateID(userSearchQuery);
        return "https://www.goat.com/web-api/v1/product_variants/buy_bar_data?productTemplateId="
                + templateId + "&countryCode=CA";
    }

    public ObjectNode interceptRequest(String userSearchQuery) {
        String url = createBuyDataTableUrl(userSearchQuery);
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(request);

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity);
                ObjectNode jsonResponse = (ObjectNode) objectMapper.readTree(result);
                logger.info("Successfully intercepted buy data for: {}", userSearchQuery);
                return jsonResponse;
            }
        } catch (Exception e) {
            logger.error("Failed to intercept buy data request at: {} for: {} - {}",
                    url, userSearchQuery, e.getMessage());
        }
        return null;
    }
}
