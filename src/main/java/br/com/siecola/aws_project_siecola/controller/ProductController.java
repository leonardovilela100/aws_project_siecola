package br.com.siecola.aws_project_siecola.controller;

import br.com.siecola.aws_project_siecola.config.local.SnsCreate;
import br.com.siecola.aws_project_siecola.entity.Product;
import br.com.siecola.aws_project_siecola.enums.EventType;
import br.com.siecola.aws_project_siecola.repository.ProductRepository;
import br.com.siecola.aws_project_siecola.service.ProductPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/products")
public class ProductController {


    public ProductRepository productRepository;

    public ProductPublisher productPublisher;

    public final Logger LOG = LoggerFactory.getLogger(SnsCreate.class);

    @Autowired
    public ProductController(ProductRepository productRepository, ProductPublisher productPublisher) {
        this.productRepository = productRepository;
        this.productPublisher = productPublisher;
    }

    @GetMapping
    public Iterable<Product> findAll () {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        return optionalProduct.map(product -> new ResponseEntity<>(product, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Product> saveProduct(@RequestBody Product product) {
        Product productCreated = productRepository.save(product);
        productPublisher.PublisherProductEvent(productCreated, EventType.PRODUCT_CREATED, "User");
        LOG.info("Sucess in create Product:  {}", product.toString());
        return new ResponseEntity<Product>(productCreated, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product, @PathVariable("id") long id) {
        if(productRepository.existsById(id)) {
            product.setId(id);
            Product productUpdated = productRepository.save(product);
            productPublisher.PublisherProductEvent(productUpdated, EventType.PRODUCT_UPDATE, "subUser");
            LOG.info("Sucess updated Product:  {}", productUpdated.toString());
            return new ResponseEntity<Product>(productUpdated,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable("id") long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if(optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            productRepository.delete(product);
            productPublisher.PublisherProductEvent(product, EventType.PRODUCT_DELETED, "ADM");
            LOG.info("Sucess  delete Product:  {}", product.toString());
            return new ResponseEntity<Product>(product, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/byCode")
    public ResponseEntity<Product> findByCode(@RequestParam String code) {
        Optional<Product> optionalProduct = productRepository.findByCode(code);
        return optionalProduct.map(product -> new ResponseEntity<>(product, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }



}
