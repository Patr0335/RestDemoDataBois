package com.example.restdemo.repository;

import com.example.restdemo.model.Assignment;
import com.example.restdemo.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface AssignmentRepository extends CrudRepository<Assignment, Long>{
}
