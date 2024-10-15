package com.example.webscrape_goat_service.controller;

import com.example.webscrape_goat_service.Service.SearchQueryInterceptorService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/search_query")
public class SearchQueryController {

    private final SearchQueryInterceptorService service;

    public SearchQueryController(SearchQueryInterceptorService service) {
        this.service = service;
    }

    @GetMapping("/{userSearchQuery}")
    public ObjectNode getSearchQueryRequest(@RequestParam String userSearchQuery) {
        service.createQueryUrl(userSearchQuery);

        return service.interceptRequest(userSearchQuery);
    }
}
