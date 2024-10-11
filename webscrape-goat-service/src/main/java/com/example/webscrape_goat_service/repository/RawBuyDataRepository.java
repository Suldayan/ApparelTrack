package com.example.webscrape_goat_service.repository;

import com.example.webscrape_goat_service.model.RawBuyData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RawBuyDataRepository extends JpaRepository<RawBuyData, Long> {
    void deleteBuyDataByTemplateId(String templateId);
}
