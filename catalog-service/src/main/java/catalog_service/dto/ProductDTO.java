package catalog_service.dto;

public class ProductDTO {
    private String name;
    private String description;
    private String category;
    private double price;
    private boolean available;
    private Long userId;

    public ProductDTO() {
    }

    public ProductDTO(String name, String description, String category, double price, boolean available, Long userId) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.available = available;
        this.userId = userId;
    }

    // Getters e Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
