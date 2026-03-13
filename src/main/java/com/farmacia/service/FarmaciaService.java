package com.farmacia.service;

import com.farmacia.model.Medicamento;
import com.farmacia.model.Usuario;
import com.farmacia.repository.MedicamentoRepository;
import com.farmacia.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FarmaciaService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    public Usuario login(String username, String password) {
        Optional<Usuario> usuario = usuarioRepository.findByUsername(username);
        // Validamos si existe y si la contraseña coincide
        if (usuario.isPresent() && usuario.get().getPassword().equals(password)) {
            return usuario.get();
        }
        return null;
    }

    public Usuario registrarUsuario(Usuario u) {
        return usuarioRepository.save(u);
    }

    public List<Medicamento> obtenerInventario() {
        return medicamentoRepository.findAll();
    }

    public Medicamento agregarMedicamento(Medicamento m) {
        return medicamentoRepository.save(m);
    }

    public String predecirStock(Long id) {
        Medicamento med = medicamentoRepository.findById(id).orElse(null);
        if (med == null) return "Medicamento no encontrado";

        // Evitar error si no hay datos de ventas (asumimos 0)
        int ventas = med.getVentasUltimoMes() != null ? med.getVentasUltimoMes() : 0;
        double demandaProyectada = ventas * 1.2; // Factor de seguridad del 20%

        if (med.getStockActual() < demandaProyectada) {
            int faltante = (int) (demandaProyectada - med.getStockActual());
            return "ALERTA: Stock CRÍTICO para " + med.getNombre() + ". Se proyectan agotar existencias. Comprar: " + faltante + " unidades.";
        }
        return "Stock SALUDABLE para " + med.getNombre() + ". Cubre la demanda proyectada.";
    }
}