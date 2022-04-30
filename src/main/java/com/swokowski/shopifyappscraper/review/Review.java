package com.swokowski.shopifyappscraper.review;

import com.swokowski.shopifyappscraper.shopifyapp.ShopifyApp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Review {
	
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
	private @NonNull String reviewer;
	private @NonNull String rating;
	private @NonNull Date date;
	
	@Column(columnDefinition="text", length=10485760)
	private @NonNull String description;
	
    @ManyToOne
    @JoinColumn(name="shopify_app_id", nullable=false)
    @OnDelete(action = OnDeleteAction.CASCADE)
	private ShopifyApp app;
	
	public Review(String reviewer, String rating, Date date, String description, ShopifyApp app) {
		this.reviewer = reviewer;
		this.rating = rating;
		this.date = date;
		this.description = description;
		this.app = app;
	}
}
