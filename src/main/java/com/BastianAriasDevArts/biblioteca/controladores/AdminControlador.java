package com.BastianAriasDevArts.biblioteca.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

 
@Controller
@RequestMapping("/admin")           //solo para admins
public class AdminControlador {
    
    @GetMapping("/dashboard")       //localhost:8080/admin/dashboard
    public String panelAdmin(){
        return "panel.html";        //es un inicio.html pero con mas opciones
    }
    
}
