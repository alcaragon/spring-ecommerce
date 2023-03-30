package com.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.ecommerce.model.Producto;


public interface ProductoRepository extends JpaRepository<Producto, Integer>{

}
