package com.pausaparakdramas.PausaParaKdramas.Service;

import com.pausaparakdramas.PausaParaKdramas.Exception.Usuario.UsuarioNoEncontradoException;
import com.pausaparakdramas.PausaParaKdramas.Model.DTO.UsuarioFirebaseDTO;
import com.pausaparakdramas.PausaParaKdramas.Model.Usuario;
import com.pausaparakdramas.PausaParaKdramas.Repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // =======================
    // MÉTODOS BÁSICOS
    // =======================

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> buscarPorUidFirebase(String uidFirebase) {
        return usuarioRepository.findByFirebaseUid(uidFirebase);
    }

    // =======================
    // CREAR / SINCRONIZAR USUARIO LOCAL
    // =======================

    public Usuario crearOActualizarDesdeFirebase(String uidFirebase, String nombre, String email, String fotoPerfil) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByFirebaseUid(uidFirebase);

        if (usuarioOpt.isPresent()) {
            Usuario existente = usuarioOpt.get();
            existente.setNombre(nombre);
            existente.setEmail(email);
            existente.setFotoPerfil(fotoPerfil);
            return usuarioRepository.save(existente);
        }

        Usuario nuevo = new Usuario();
        nuevo.setFirebaseUid(uidFirebase);
        nuevo.setNombre(nombre);
        nuevo.setEmail(email);
        nuevo.setFotoPerfil(fotoPerfil);
        nuevo.setRoles(List.of("USER"));
        return usuarioRepository.save(nuevo);
    }

    public Usuario actualizarPerfil(String uidFirebase, UsuarioFirebaseDTO cambios) {
        Usuario usuario = usuarioRepository.findByFirebaseUid(uidFirebase)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        usuario.setNombre(cambios.getNombre());
        usuario.setFotoPerfil(cambios.getFotoPerfil());
        return usuarioRepository.save(usuario);
    }

    // =======================
    // ADMIN: CRUD LOCAL
    // =======================

    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void eliminarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));
        usuarioRepository.delete(usuario);
    }

    public Usuario actualizarUsuarioAdmin(Long id, Usuario update) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        usuario.setNombre(update.getNombre());
        usuario.setEmail(update.getEmail());
        usuario.setFotoPerfil(update.getFotoPerfil());
        usuario.setRoles(update.getRoles());

        return usuarioRepository.save(usuario);
    }

    // =======================
    // VALIDACIÓN DE ADMIN
    // =======================

    public void validarAdmin(HttpServletRequest request) {
        String uid = (String) request.getAttribute("uidFirebase");

        Usuario actor = usuarioRepository.findByFirebaseUid(uid)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        boolean esAdmin = actor.getRoles() != null && actor.getRoles().contains("ADMIN");

        if (!esAdmin) {
            throw new RuntimeException("No tiene permisos para realizar esta acción");
        }
    }
}
