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
public class SearchQueryInterceptorService {

    private final Logger logger = LoggerFactory.getLogger(SearchQueryInterceptorService.class);

    @Autowired
    private WebscrapeService webscrapeService;

    public String createQueryUrl(String userSearchQuery) {
        return  "https://ac.cnstrc.com/search/" + userSearchQuery
        + "?c=ciojs-client-2.54.0&key=key_XT7bjdbvjgECO5d8&i=004cdbd1-d626-4aa8-83e4-2bcd21b37d76&s=24&page=1&num_results_per_page=24&sort_by=relevance&sort_order=descending&fmt_options%5Bhidden_fields%5D=gp_lowest_price_cents_3&fmt_options%5Bhidden_fields%5D=gp_instant_ship_lowest_price_cents_3&fmt_options%5Bhidden_facets%5D=gp_lowest_price_cents_3&fmt_options%5Bhidden_facets%5D=gp_instant_ship_lowest_price_cents_3&variations_map=%7B%22group_by%22%3A%5B%7B%22name%22%3A%22product_condition%22%2C%22field%22%3A%22data.product_condition%22%7D%2C%7B%22name%22%3A%22box_condition%22%2C%22field%22%3A%22data.box_condition%22%7D%5D%2C%22values%22%3A%7B%22min_regional_price%22%3A%7B%22aggregation%22%3A%22min%22%2C%22field%22%3A%22data.gp_lowest_price_cents_3%22%7D%2C%22min_regional_instant_ship_price%22%3A%7B%22aggregation%22%3A%22min%22%2C%22field%22%3A%22data.gp_instant_ship_lowest_price_cents_3%22%7D%7D%2C%22dtype%22%3A%22object%22%7D&qs=%7B%22features%22%3A%7B%22display_variations%22%3Atrue%7D%2C%22feature_variants%22%3A%7B%22display_variations%22%3A%22matched%22%7D%7D&_dt=1728570542572";
    }

    public ObjectNode interceptRequest(String userSearchQuery) {
        String url = createQueryUrl(userSearchQuery);
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(request);

            return (ObjectNode) response;
        } catch (Exception e) {
            logger.error("Failed to intercept query request at: {} for {} - {}",
                    url, userSearchQuery, e.getMessage());
            return null;
        }
    }
}
