package com.swokowski.shopifyappscraper.review;


import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends CrudRepository<Review, UUID> {
	Integer countByAppId(UUID id);
	List<Review> findByReviewerAndDateAndAppId(String reviewer, Date date, UUID id);
}
