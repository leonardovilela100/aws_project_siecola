package br.com.siecola.aws_project_siecola.repository;

import br.com.siecola.aws_project_siecola.entity.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Long> {

    Optional<Product> findByCode(String code);

}
