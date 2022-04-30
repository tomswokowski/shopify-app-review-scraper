package com.swokowski.shopifyappscraper.category;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swokowski.shopifyappscraper.utils.Utils;

@Service
public class CategoryService {
	
    @Autowired
    private CategoryRepository categoryRepo;

    public List<Category> getNewCategories(int sleepTime) throws InterruptedException {
    	Document doc = Utils.getDocument("https://apps.shopify.com/browse", sleepTime);
		Element categoryNav = doc.getElementById("CategoriesFilter");
		Elements appCats = categoryNav.getElementsByClass("accordion-item");
		
		ArrayList<Category> categories = new ArrayList<Category>();
		
		for (Element cat : appCats) {
			Element catEl = cat.selectFirst("a");
			String catName = catEl.ownText().trim();
			String catUrl = catEl.attributes().get("href").split("\\?")[0];
			catUrl = "https://apps.shopify.com" + catUrl + "?sort_by=newest";
			
			Category category = new Category(catName, catUrl);
			if (categoryRepo.findByName(category.getName()) == null) {
				categories.add(category);
				saveCategory(category);
			}
		}
    	return categories;
    }
    
    public void saveNewCategories(List<Category> categories) {
    	for (Category category : categories) saveCategory(category);
    }
    
    public List<Category> getAllCategories() {
    	return (List<Category>) categoryRepo.findAll();
    }
    
    public void saveCategory(Category category) {
    	categoryRepo.save(category);
    }
}
