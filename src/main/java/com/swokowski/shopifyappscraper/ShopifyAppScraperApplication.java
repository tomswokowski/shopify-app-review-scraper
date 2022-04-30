package com.swokowski.shopifyappscraper;

import com.swokowski.shopifyappscraper.category.*;
import com.swokowski.shopifyappscraper.review.*;
import com.swokowski.shopifyappscraper.shopifyapp.*;
import com.swokowski.shopifyappscraper.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShopifyAppScraperApplication implements CommandLineRunner {
	
	private static final Logger log = LoggerFactory.getLogger(ShopifyAppScraperApplication.class);
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private ShopifyAppService shopifyAppService;
    
    @Autowired
    private ReviewService reviewService;
	
	public static void main(String[] args) {
		SpringApplication.run(ShopifyAppScraperApplication.class, args);
	}
	
    @Override
    public void run(String... args) throws InterruptedException, IOException {

    	log.info("StartApplication...");       
    	int sleepTime = 30000;

    	List<Category> newCategories = categoryService.getNewCategories(sleepTime);
    	categoryService.saveNewCategories(newCategories);
    	List<Category> categories = categoryService.getAllCategories();
    	
    	
    	List<ShopifyApp> newShopifyApps = new ArrayList<>();
    	
    	for (Category cat : categories) {
    		List<ShopifyApp> apps = shopifyAppService.getNewAppsFromCategory(cat, sleepTime, newShopifyApps);
    		for (ShopifyApp app : apps) newShopifyApps.add(app);
    	}
    	
    	shopifyAppService.saveNewShopifyApps(newShopifyApps);
    	List<ShopifyApp> shopifyApps = shopifyAppService.getAllShopifyApps();
    	
    	List<Review> newReviews = new ArrayList<>();
    	for (ShopifyApp app : shopifyApps) {
    		List<Review> reviewsForApp = reviewService.getNewReviews(app, sleepTime);
    		for (Review review : reviewsForApp) newReviews.add(review);
    		log.info("Getting reviews for: " + app.getName());
    	}
    	
    	reviewService.saveNewReviews(newReviews);
    	
    	Utils.printResults(newCategories, newShopifyApps, newReviews); 
    	
    }
    
}
