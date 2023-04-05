package com.ecommerce.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.model.Producto;
import com.ecommerce.model.Usuario;
import com.ecommerce.service.ProductoService;
import com.ecommerce.service.UploadFileService;

@Controller
@RequestMapping("/productos")
public class ProductoController {
	
	private final Logger LOG = LoggerFactory.getLogger(ProductoController.class);
	
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private UploadFileService upload;
	
	@GetMapping("")
	public String show(Model model) {
		model.addAttribute("productos", productoService.findAll());
		return "productos/show";
	}
	
	@GetMapping("/create")
	public String create() {
		return "productos/create";
	}
	
	@PostMapping("/create")
	public String save(Producto producto, @RequestParam("img") MultipartFile file) throws IOException { // "img" es el campo del formulario create.html con id="img"
		LOG.info("Este es el objeto Producto: {}", producto);
		Usuario us = new Usuario(1, "", "", "", "", "", "", "");
		producto.setUsuario(us);
		
		// imagen
		if(producto.getId() == null) {  // cuando se crea un producto por primera vez
			String nombreImagen = upload.saveImage(file);
			producto.setImagen(nombreImagen);
		} else {
			
		}
		
		productoService.save(producto);
		
		return "redirect:/productos";
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Integer id, Model model) {
		Producto producto = new Producto();
		Optional<Producto> optionalProduct = productoService.get(id);
		producto = optionalProduct.get();
		model.addAttribute("producto", producto);
		LOG.info("Objeto Producto: {}", producto);
		return "productos/edit";
	}

	
	@PostMapping("/update")
	public String update(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {
		
		if(file.isEmpty()) { // editamos el producto pero no cambiamos la imagen
			Producto p = new Producto();
			p = productoService.get(producto.getId()).get();
			p.setImagen(p.getImagen());
			} else { // editamos el producto y la imagen
				
				Producto p = new Producto();
				p = productoService.get(producto.getId()).get();
				
				// eliminamos la imagen cuando ésta no sea por defecto
				if(!p.getImagen().equals("default.jpg")) {
					upload.deleteImage(p.getImagen());
				}
				String nombreImagen = upload.saveImage(file);
				producto.setImagen(nombreImagen);
			}
		
		productoService.update(producto);
		return "redirect:/productos";
	}
	
	@GetMapping("delete/{id}")
	public String delete(@PathVariable Integer id) {
		Producto p = new Producto();
		p = productoService.get(id).get();
		
		// eliminamos la imagen cuando ésta no sea por defecto
		if(!p.getImagen().equals("default.jpg")) {
			upload.deleteImage(p.getImagen());
		}
		
		productoService.delete(id);
		return "redirect:/productos";
	}

}
