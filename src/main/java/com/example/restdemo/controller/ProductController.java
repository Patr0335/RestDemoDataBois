package com.example.restdemo.controller;

import com.example.restdemo.model.Product;
import com.example.restdemo.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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
        //ResponseEntitiy builder - først status OK/200- så til sidst body. body=collection of products
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    //findById anden metode
    @GetMapping("/products/{id}")
    public ResponseEntity<Optional<Product>> findById(@PathVariable Long id) {
        //Hent product fra repository
        Optional<Product> optionalProduct = productRepository.findById(id);

        if (optionalProduct.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(optionalProduct);
        } else {
            //not found 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(optionalProduct);
        }
    }

    //HTTP POST (/products) - create
    @CrossOrigin(origins = "*", exposedHeaders = "Location")
    @PostMapping(value = "")
    public ResponseEntity<Product> create(@RequestBody Product product) {
// hvis id sat, så return "bad request"
        if (product.getId() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        //opret et nyt product i JPA
        Product newProduct = productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).header("Location", "/products/" + newProduct.getId()).body(newProduct);
    }

    @PutMapping("products/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody Product product){
        //findes produktet?
        Optional<Product> optionalStudent = productRepository.findById(id);
        if (optionalStudent.isPresent()){
            //er path id og product object id identiske? ellers returner BAD_REQUEST
            if (id.equals(product.getId())){
                //product findes så opdater
                product.setId(id);
                productRepository.save(product);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            else{
                //forskel på path id og product object id
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @DeleteMapping("products/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        Optional<Product> optionalStudent = productRepository.findById(id);
        if (optionalStudent.isPresent()){
            productRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        else{
            //not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }




}
