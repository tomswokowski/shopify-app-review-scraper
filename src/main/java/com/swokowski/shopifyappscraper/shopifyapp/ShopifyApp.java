package com.swokowski.shopifyappscraper.shopifyapp;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.swokowski.shopifyappscraper.category.Category;
import com.swokowski.shopifyappscraper.review.Review;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class ShopifyApp {
	
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
	private @NonNull String name;
	private @NonNull String developer;
	
	@Column(columnDefinition="text", length=10485760)
	private @NonNull String description;
	
	private @NonNull String imgUrl;
	private @NonNull String appUrl;
	
    @ManyToOne
    @JoinColumn(name="category_id", nullable=false)
    @OnDelete(action = OnDeleteAction.CASCADE)
	private Category category;
    
    @OneToMany(mappedBy = "app")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Review> reviews;
	
	public ShopifyApp(String name, String developer, String description, String imgUrl, String appUrl, Category category) {
		this.name = name;
		this.developer = developer;
		this.description = description;
		this.imgUrl = imgUrl;
		this.appUrl = appUrl;
		this.category = category;
	}
}
