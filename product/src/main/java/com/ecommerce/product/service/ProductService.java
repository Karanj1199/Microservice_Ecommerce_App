package com.ecommerce.product.service;



import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.repository.ProductRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRespository productRepsitory;

    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = new Product();
        updateProductFromRequest(product,productRequest);
        Product savedProduct = productRepsitory.save(product);
        return maptoProductResponse(savedProduct);
    }

    private ProductResponse maptoProductResponse(Product savedProduct) {
        ProductResponse response = new ProductResponse();
        response.setId(savedProduct.getId());
        response.setName(savedProduct.getName());
        response.setActive(savedProduct.getActive());
        response.setCategory(savedProduct.getCategory());
        response.setDescription(savedProduct.getDescription());
        response.setPrice(savedProduct.getPrice());
        response.setImageUrl(savedProduct.getImageUrl());
        response.setStockQuantity(savedProduct.getStockQuantity());
        return response;
    }

    private void updateProductFromRequest(Product product, ProductRequest productRequest) {
        product.setName(productRequest.getName());
        product.setCategory(productRequest.getCategory());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setImageUrl(productRequest.getImageUrl());
        product.setStockQuantity(productRequest.getStockQuantity());
    }


    public Optional<ProductResponse> updateProduct(Long id, ProductRequest productRequest) {
        return productRepsitory.findById(id)
                .map(existingProduct -> {
                    updateProductFromRequest(existingProduct,productRequest);
                    Product savedProduct = productRepsitory.save(existingProduct);
                    return maptoProductResponse(savedProduct);
                });
    }

    public List<ProductResponse> getAllProducts() {
        return productRepsitory.findByActiveTrue()
                .stream()
                .map(this::maptoProductResponse)
                .collect(Collectors.toList());
    }

    public boolean deleteProduct(Long id) {
        return productRepsitory.findById(id)
                .map(product -> {
                    product.setActive(false);
                    productRepsitory.save(product);
                    return true;
                }).orElse(false);
    }

    public List<ProductResponse> searchProducts(String keyword) {
        return productRepsitory.searchProducts(keyword).stream()
                .map(this::maptoProductResponse)
                .collect(Collectors.toList());
    }
}
