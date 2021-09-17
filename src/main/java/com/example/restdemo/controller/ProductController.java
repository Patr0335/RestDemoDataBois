package com.example.restdemo.controller;

import com.example.restdemo.model.Product;
import com.example.restdemo.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductController {

    private ProductRepository productRepository;

    //Constructor injection i stedet for field injection (Autowired)
    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    //HTTP GET (/products)
    @GetMapping("/products")
    public ResponseEntity<List<Product>> findAll() {
        List<Product> products = new ArrayList<>();
        productRepository.findAll().forEach(products::add);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    //HTTP POST (/products)
    @PostMapping("/createProduct")
    Product newProduct(@RequestBody Product newProduct) {
        return productRepository.save(newProduct);
    }

    @GetMapping("/products/{id}")
    Product one(@PathVariable Long id) {

        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @PutMapping("/products/{id}")
    Product updateProduct(@RequestBody Product newProduct, @PathVariable Long id) {

        return productRepository.findById(id)
                .map(product -> {
                    product.setName(newProduct.getName());
                    product.setPrice(newProduct.getPrice());
                    return productRepository.save(product);
                })
                .orElseGet(() -> {
                    newProduct.setId(id);
                    return productRepository.save(newProduct);
                });
    }

    @DeleteMapping("/delete/{id}")
    void deleteEmployee(@PathVariable Long id) {
        productRepository.deleteById(id);
    }

//    @RequestMapping("/")
//    public ModelAndView index () {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("index");
//        return modelAndView;
//    }




}
