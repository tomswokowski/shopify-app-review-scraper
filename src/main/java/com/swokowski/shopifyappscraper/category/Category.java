package com.swokowski.shopifyappscraper.category;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import com.swokowski.shopifyappscraper.shopifyapp.ShopifyApp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Category {
	
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @OneToMany(mappedBy = "category")
    private List<ShopifyApp> shopifyApps;
    
	private @NonNull String name;
	private @NonNull String url;

	public Category(String name, String url) {
		this.name = name;
		this.url = url;
	}
}
