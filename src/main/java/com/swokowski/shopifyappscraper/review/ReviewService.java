package com.swokowski.shopifyappscraper.review;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swokowski.shopifyappscraper.ShopifyAppScraperApplication;
import com.swokowski.shopifyappscraper.shopifyapp.ShopifyApp;
import com.swokowski.shopifyappscraper.utils.Utils;

@Service
public class ReviewService {
	
	private static final Logger log = LoggerFactory.getLogger(ShopifyAppScraperApplication.class);
	
    @Autowired
    private ReviewRepository reviewRepo;
    
    public List<Review> getNewReviews(ShopifyApp app, int sleepTime) throws InterruptedException {
    	List<Review> appReviews = new ArrayList<>();
    	
		Document appDoc = Utils.getDocument(app.getAppUrl() + "/reviews", sleepTime);
		if (appDoc == null) return appReviews;
		
		String resultsStr = appDoc.getElementsByClass("grid__item gutter-bottom").first().text().trim();
		int totalPages;
		String totalResults;
		
		if (resultsStr.contains("of")) {
			String[] resultsStrArr = appDoc.getElementsByClass("grid__item gutter-bottom").first().text().trim().split("of");
			String resultsPerPage = resultsStrArr[0].split("-")[1].trim();
			totalResults = resultsStrArr[1].trim().split(" ")[0].trim();
			totalPages = (int) Math.ceil(Double.parseDouble(totalResults) / Double.parseDouble(resultsPerPage));
		} else {
			String[] resultsStrArr = appDoc.getElementsByClass("grid__item gutter-bottom").first().text().trim().split(" ");
			totalResults = resultsStrArr[0];
			totalPages = 1;
		}
		
		for (int i = 1; i <= totalPages; i++) {
			Document appDocPage = Utils.getDocument(app.getAppUrl() + "/reviews?page=" + i, sleepTime);
			
			if (appDocPage != null) {
				Elements reviewListings = appDocPage.getElementsByClass("review-listing");
				
				if (reviewListings != null) {
					for (Element reviewListing : reviewListings) {
						String reviewer = reviewListing.selectFirst("h3").text().trim();
						String rating = reviewListing.getElementsByClass("ui-star-rating").first().attr("data-rating");
						Element dateContainer =  reviewListing.getElementsByClass("review-metadata__item").last();
						Date date = Utils.convertDate(dateContainer.getElementsByClass("review-metadata__item-value").first().text().trim());
						String description = reviewListing.selectFirst("p").text().trim();
						
						Review review = new Review(reviewer, rating, date, description, app);
						if (reviewRepo.findByReviewerAndDateAndAppId(reviewer, date, app.getId()).size() == 0) {
							log.info("Saving new review: " + app.getName() + " " + reviewer + " " + date);
							appReviews.add(review);
						} else {
							return appReviews;
						}
					}
				}
			}
		}	
    	return appReviews;
    }
    
    public void saveNewReviews(List<Review> reviews) {
    	for (Review review : reviews) saveReview(review);
    }
    
    public void saveReview(Review review) {
    	reviewRepo.save(review);
    }
}
