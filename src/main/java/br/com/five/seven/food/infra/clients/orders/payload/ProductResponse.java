package br.com.five.seven.food.infra.clients.orders.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean active;
    private List<ImageResponse> images;
    private CategoryResponse category;

    public static ProductResponse fromDomain(Product createdProduct) {
        ProductResponse response = new ProductResponse();
        response.setId(createdProduct.getId());
        response.setName(createdProduct.getName());
        response.setDescription(createdProduct.getDescription());
        response.setPrice(createdProduct.getPrice());
        response.setActive(createdProduct.isActive());
        response.setImages(ImageResponse.fromDomainList(createdProduct.getImages()));
        response.setCategory(CategoryResponse.fromDomain(createdProduct.getCategory()));
        return response;
    }
}
