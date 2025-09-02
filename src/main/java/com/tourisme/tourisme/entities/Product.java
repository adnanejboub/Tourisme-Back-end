package com.tourisme.tourisme.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "produit")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "produit_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("PRODUIT")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produit")
    private Long id;
    
    @Column(name = "nom", nullable = false)
    private String title; // Maps to nom in Produit
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "prix")
    private double price; // Maps to prix in Produit
    
    @Column(name = "prix_unitaire")
    private double discountedPrice; // Maps to prixUnitaire in Produit
    
    @Column(name = "image")
    private String imageUrl; // Maps to image in Produit
    
    @Column(name = "origine")
    private String origine;
    
    @Column(name = "authentique")
    private Boolean authentique;
    
    @Column(name = "date_fabrication")
    @Temporal(TemporalType.DATE)
    private Date dateFabrication;
    
    @Column(name = "taille")
    private String taille;
    
    @Column(name = "stock_disponible")
    private Integer stockQuantity; // Maps to stockDisponible in Produit
    
    @Column(name = "poids")
    private Float poids;
    
    @Column(name = "date_expiration")
    @Temporal(TemporalType.DATE)
    private Date dateExpiration;
    
    @Column(name = "exclusif")
    private Boolean exclusif;
    
    @Column(name = "date_creation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate ;
    
    @Column(name = "is_disponible")
    private Boolean isActive; // Maps to isDisponible in Produit
    
    // Additional attributes for your tourism app
    @ElementCollection
    @CollectionTable(name = "product_colors", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "color")
    private List<String> colors;
    
    @ElementCollection
    @CollectionTable(name = "product_sizes", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "size")
    private List<String> sizes;
    
    private Boolean isFeatured = false;
    private double rating = 0.0;
    private Integer reviewCount = 0;
    private LocalDateTime updatedDate = LocalDateTime.now();

    // Relationships (keep existing ones, remove Fournisseur and Media)
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CartItem> cartItems;

    // Constructors
    public Product() {
        super();
        this.createdDate  = new Date();
        this.isActive = true;
    }

    @PreUpdate
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }

    // Getters and Setters - All the existing ones plus new ones for Produit fields
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public double getDiscountedPrice() { return discountedPrice; }
    public void setDiscountedPrice(double discountedPrice) { this.discountedPrice = discountedPrice; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public String getOrigine() { return origine; }
    public void setOrigine(String origine) { this.origine = origine; }
    
    public Boolean getAuthentique() { return authentique; }
    public void setAuthentique(Boolean authentique) { this.authentique = authentique; }
    
    public Date getDateFabrication() { return dateFabrication; }
    public void setDateFabrication(Date dateFabrication) { this.dateFabrication = dateFabrication; }
    
    public String getTaille() { return taille; }
    public void setTaille(String taille) { this.taille = taille; }
    
    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
    
    public Float getPoids() { return poids; }
    public void setPoids(Float poids) { this.poids = poids; }
    
    public Date getDateExpiration() { return dateExpiration; }
    public void setDateExpiration(Date dateExpiration) { this.dateExpiration = dateExpiration; }
    
    public Boolean getExclusif() { return exclusif; }
    public void setExclusif(Boolean exclusif) { this.exclusif = exclusif; }
    
    public Date getCreatedDate () { return createdDate ; }
    public void setCreatedDate (Date createdDate ) { this.createdDate  = createdDate ; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean active) { isActive = active; }
    
    public List<String> getColors() { return colors; }
    public void setColors(List<String> colors) { this.colors = colors; }
    
    public List<String> getSizes() { return sizes; }
    public void setSizes(List<String> sizes) { this.sizes = sizes; }
    
    public Boolean getIsFeatured() { return isFeatured; }
    public void setIsFeatured(Boolean featured) { isFeatured = featured; }
    
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    
    public Integer getReviewCount() { return reviewCount; }
    public void setReviewCount(Integer reviewCount) { this.reviewCount = reviewCount; }
    
    public LocalDateTime getUpdatedDate() { return updatedDate; }
    public void setUpdatedDate(LocalDateTime updatedDate) { this.updatedDate = updatedDate; }
    
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    
    public List<CartItem> getCartItems() { return cartItems; }
    public void setCartItems(List<CartItem> cartItems) { this.cartItems = cartItems; }
}