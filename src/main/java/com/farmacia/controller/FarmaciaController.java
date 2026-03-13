package com.farmacia.controller;

import com.farmacia.model.Medicamento;
import com.farmacia.model.Usuario;
import com.farmacia.service.FarmaciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Permite conexión desde cualquier app frontend
public class FarmaciaController {

    @Autowired
    private FarmaciaService service;

    // Login
    @PostMapping("/login")
    public String login(@RequestBody Usuario u) {
        Usuario usuarioLogueado = service.login(u.getUsername(), u.getPassword());
        if (usuarioLogueado != null) {
            return "Bienvenido al sistema: " + usuarioLogueado.getNombreCompleto();
        }
        return "Error: Credenciales incorrectas";
    }

    // Registro
    @PostMapping("/registro")
    public Usuario registrar(@RequestBody Usuario u) {
        return service.registrarUsuario(u);
    }

    // Panel de usuario: Ver inventario
    @GetMapping("/panel/medicamentos")
    public List<Medicamento> listarMedicamentos() {
        return service.obtenerInventario();
    }

    // Panel de usuario: Agregar medicamento (Para probar)
    @PostMapping("/panel/agregar-medicamento")
    public Medicamento agregar(@RequestBody Medicamento m) {
        return service.agregarMedicamento(m);
    }

    // Módulo Predictivo
    @GetMapping("/prediccion/{id}")
    public String obtenerPrediccion(@PathVariable Long id) {
        return service.predecirStock(id);
    }
}
