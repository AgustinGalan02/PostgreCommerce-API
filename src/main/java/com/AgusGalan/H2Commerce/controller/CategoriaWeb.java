package com.AgusGalan.H2Commerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CategoriaWeb {
    @GetMapping("/categoriasWeb")
    public String home(Model model) {
        model.addAttribute("titulo", "Lista de categorías");
        return "categorias";
    }
}
