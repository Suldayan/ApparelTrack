package com.example.webscrape_goat_service.repository;

import com.example.webscrape_goat_service.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    void deleteByName(String name);
}
