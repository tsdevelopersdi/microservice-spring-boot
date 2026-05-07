package com.alpha.product_service.controller;

import com.alpha.product_service.dto.ProductRequest;
import com.alpha.product_service.dto.ProductResponse;
import com.alpha.product_service.model.Product;
import com.alpha.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@RequestBody ProductRequest productRequest) {
        return productService.createProduct(productRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts() {
//        try {
//            Thread.sleep(5000); // Simulatedelay
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
        return productService.getAllProducts();
    }
}
