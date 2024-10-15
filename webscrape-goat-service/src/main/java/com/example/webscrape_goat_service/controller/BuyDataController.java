package com.example.webscrape_goat_service.controller;

import com.example.webscrape_goat_service.Service.BuyDataInterceptorService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/buy_data")
public class BuyDataController {

    private final BuyDataInterceptorService buyDataInterceptorService;

    public BuyDataController(BuyDataInterceptorService buyDataInterceptorService) {
        this.buyDataInterceptorService = buyDataInterceptorService;
    }

    @GetMapping("/{userSearchQuery}")
    public ObjectNode getBuyData(@RequestParam String userSearchQuery) {
        buyDataInterceptorService.createBuyDataTableUrl(userSearchQuery);

        return buyDataInterceptorService.interceptRequest(userSearchQuery);
    }
}
