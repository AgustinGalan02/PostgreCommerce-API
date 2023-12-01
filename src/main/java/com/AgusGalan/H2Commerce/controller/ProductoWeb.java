package com.AgusGalan.H2Commerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductoWeb {
    @GetMapping("/productosWeb")
    public String home(Model model) {
        model.addAttribute("titulo", "Lista de productos");
        return "productos";
    }
}
