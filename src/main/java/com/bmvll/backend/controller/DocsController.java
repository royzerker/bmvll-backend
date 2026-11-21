package com.bmvll.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.v3.oas.annotations.Hidden;

/** La raíz del dominio lleva a la documentación de la API. */
@Hidden
@Controller
public class DocsController {

    @GetMapping("/")
    public String root() {
        return "redirect:/swagger-ui/index.html";
    }
}
