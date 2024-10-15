package com.example.webscrape_goat_service.repository;

import com.example.webscrape_goat_service.model.RawSearchQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RawSearchQueryRepository extends JpaRepository<RawSearchQuery, Long> {
    void deleteRawSearchQueryByQuery(String query);
}
