package com.swokowski.shopifyappscraper.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swokowski.shopifyappscraper.ShopifyAppScraperApplication;
import com.swokowski.shopifyappscraper.category.Category;
import com.swokowski.shopifyappscraper.review.Review;
import com.swokowski.shopifyappscraper.shopifyapp.ShopifyApp;

public class Utils {
	
	private static final Logger log = LoggerFactory.getLogger(ShopifyAppScraperApplication.class);
	
    public static Document getDocument(String url, int sleepTime) throws InterruptedException {
        Connection conn = Jsoup.connect(url).timeout(60000);
        Document document = null;
        
        try {
            document = conn.get();
        } catch (HttpStatusException e) {
        	if (e.getStatusCode() == 429) {
        		log.info("429 Exception: " + url);
        		Thread.sleep(sleepTime);
        		return getDocument(url, sleepTime);
        	}
        } catch (SocketTimeoutException e) {
    		Thread.sleep(sleepTime);
    		return getDocument(url, sleepTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return document;
    }
    
    public static Date convertDate(String dateStr) {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
    	LocalDate date = LocalDate.parse(dateStr, formatter);
    	return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
    
    public static void printResults(List<Category> categories, List<ShopifyApp> shopifyApps, List<Review> reviews) throws IOException {
	    try {
	    	FileWriter writer = new FileWriter("output.txt"); 

	    	writer.write("------:" + System.lineSeparator());
	    	writer.write("New Categories:" + System.lineSeparator());
	    	writer.write("------:" + System.lineSeparator());
	    	for (Category category : categories) {
	    		writer.write(category.getName() + System.lineSeparator());
	    	}
	    	
	    	writer.write("------:" + System.lineSeparator());
	    	writer.write("New Apps:" + System.lineSeparator());
	    	writer.write("------:" + System.lineSeparator());
	    	for (ShopifyApp app : shopifyApps) {
	    		writer.write(app.getName() + System.lineSeparator());
	    	}
	    	
	    	writer.write("------:" + System.lineSeparator());
	    	writer.write("New Reviews:" + System.lineSeparator());
	    	writer.write("------:" + System.lineSeparator());
	    	for (Review review : reviews) {
	    		writer.write(review.getReviewer() + System.lineSeparator());
	    	}
	    	
	    	writer.close();
	    } finally {
	    	log.info("Program completed...");
	    }
    }
}
