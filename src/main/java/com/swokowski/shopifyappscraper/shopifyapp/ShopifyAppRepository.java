package com.swokowski.shopifyappscraper.shopifyapp;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopifyAppRepository extends CrudRepository<ShopifyApp, Long> {
	ShopifyApp findByAppUrl(String url);
}
