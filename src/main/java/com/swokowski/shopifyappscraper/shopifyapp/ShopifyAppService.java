package com.swokowski.shopifyappscraper.shopifyapp;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swokowski.shopifyappscraper.ShopifyAppScraperApplication;
import com.swokowski.shopifyappscraper.category.Category;
import com.swokowski.shopifyappscraper.utils.Utils;

@Service
public class ShopifyAppService {
	
	private static final Logger log = LoggerFactory.getLogger(ShopifyAppScraperApplication.class);
	
    @Autowired
    private ShopifyAppRepository shopifyAppRepo;
	
    public List<ShopifyApp> getNewAppsFromCategory(Category cat, int sleepTime, List<ShopifyApp> shopifyAppList) throws InterruptedException {
    	List<ShopifyApp> shopifyApps = new ArrayList<>();
    	
		Document catDoc = Utils.getDocument(cat.getUrl(), sleepTime);
		String[] resultsStrArr = catDoc.getElementsByClass("search-filters__header").first().child(0).text().split("of");
		Double resultsPerPage = Double.parseDouble(resultsStrArr[0].split("-")[1].trim());
		Double totalResults = Double.parseDouble(resultsStrArr[1].trim().split(" ")[0].trim());
		int totalPages = (int) Math.ceil(totalResults / resultsPerPage);
		
		
		for (int i = 1; i <= totalPages; i++) {
			Document catDocPage = Utils.getDocument(cat.getUrl() + "&page=" + i, sleepTime);
			Element searchResultsListingsEl = catDocPage.getElementById("SearchResultsListings");
			Elements apps = searchResultsListingsEl.getElementsByClass("grid-item--app-card-listing");

			if (apps != null) {
				for (Element app : apps) {
					String name = app.selectFirst("h2").text().trim();
					String developer = app.getElementsByClass("ui-app-card__developer-name").first().text().split(" ", 2)[1].trim();
					String description = app.getElementsByClass("ui-app-card__details").first().text().trim();
					String imgUrl = app.selectFirst("img").attributes().get("srcset").split(" ")[0];
					String appUrl = app.selectFirst("a").attributes().get("href").split("\\?")[0];

					ShopifyApp shopifyApp = new ShopifyApp(name, developer, description, imgUrl, appUrl, cat);
					
					boolean appAlreadyExists = shopifyAppList.stream().filter(shopApp-> appUrl.equals(shopApp.getAppUrl())).findFirst().orElse(null) != null;
					if (appAlreadyExists) continue;
					
					if (shopifyAppRepo.findByAppUrl(appUrl) == null) {
						shopifyApps.add(shopifyApp);
					} else {
						return shopifyApps;
					}
				}
			}
		}
		return shopifyApps;
    }
    
    
    public void saveNewShopifyApps(List<ShopifyApp> shopifyApps) {
    	for (ShopifyApp app : shopifyApps) saveShopifyApp(app);
    }
    
    public void saveShopifyApp(ShopifyApp shopifyApp) {
    	log.info("Saving new app: " + shopifyApp.getName());
    	shopifyAppRepo.save(shopifyApp);
    }
    
    public List<ShopifyApp> getAllShopifyApps() {
    	return (List<ShopifyApp>) shopifyAppRepo.findAll();
    }

}
