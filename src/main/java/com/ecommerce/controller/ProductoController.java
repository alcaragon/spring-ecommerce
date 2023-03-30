package com.ecommerce.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecommerce.model.Producto;
import com.ecommerce.model.Usuario;
import com.ecommerce.service.ProductoService;

@Controller
@RequestMapping("/productos")
public class ProductoController {
	
	private final Logger LOG = LoggerFactory.getLogger(ProductoController.class);
	
	@Autowired
	private ProductoService productoService;
	
	@GetMapping("")
	public String show() {
		return "productos/show";
	}
	
	@GetMapping("/create")
	public String create() {
		return "productos/create";
	}
	
	@PostMapping("/create")
	public String save(Producto producto) {
		LOG.info("Este es el objeto Producto: {}", producto);
		Usuario us = new Usuario(1, "", "", "", "", "", "", "");
		producto.setUsuario(us);
		productoService.save(producto);
		return "redirect:/productos";
	}


}
