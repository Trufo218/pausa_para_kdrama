package com.pausaparakdramas.PausaParaKdramas.Controller;

import com.google.firebase.auth.UserRecord;
import com.pausaparakdramas.PausaParaKdramas.Model.DTO.UsuarioFirebaseDTO;
import com.pausaparakdramas.PausaParaKdramas.Model.Usuario;
import com.pausaparakdramas.PausaParaKdramas.Service.FirebaseAdminService;
import com.pausaparakdramas.PausaParaKdramas.Service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private FirebaseAdminService firebaseAdminService;


    // PERFIL DEL USUARIO


    @GetMapping("/me")
    public Usuario obtenerPerfil(HttpServletRequest request) {
        String uid = (String) request.getAttribute("uidFirebase");
        return usuarioService.buscarPorUidFirebase(uid)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @PostMapping("/sync")
    public Usuario sincronizarUsuario(HttpServletRequest request, @RequestBody UsuarioFirebaseDTO datos) {
        String uid = (String) request.getAttribute("uidFirebase");
        return usuarioService.crearOActualizarDesdeFirebase(
                uid,
                datos.getNombre(),
                datos.getEmail(),
                datos.getFotoPerfil()
        );
    }

    @PutMapping("/me")
    public Usuario actualizarPerfil(HttpServletRequest request, @RequestBody UsuarioFirebaseDTO cambios) {
        String uid = (String) request.getAttribute("uidFirebase");
        return usuarioService.actualizarPerfil(uid, cambios);
    }


    // ADMIN: CRUD LOCAL


    @GetMapping
    public List<Usuario> listarUsuarios(HttpServletRequest request) {
        usuarioService.validarAdmin(request);
        return usuarioService.listarUsuarios();
    }

    @GetMapping("/{id}")
    public Usuario obtenerUsuario(@PathVariable Long id, HttpServletRequest request) {
        usuarioService.validarAdmin(request);
        return usuarioService.buscarUsuarioPorId(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @PostMapping
    public Usuario crearUsuario(@RequestBody Usuario usuario, HttpServletRequest request) {
        usuarioService.validarAdmin(request);
        return usuarioService.save(usuario);
    }

    @PutMapping("/{id}")
    public Usuario actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario, HttpServletRequest request) {
        usuarioService.validarAdmin(request);
        return usuarioService.actualizarUsuarioAdmin(id, usuario);
    }

    @DeleteMapping("/{id}")
    public void eliminarUsuario(@PathVariable Long id, HttpServletRequest request) {
        usuarioService.validarAdmin(request);
        usuarioService.eliminarUsuario(id);
    }


    // ADMIN: CRUD FIREBASE


    @GetMapping("/firebase")
    public List<UserRecord> listarUsuariosFirebase(HttpServletRequest request) throws Exception {
        usuarioService.validarAdmin(request);
        return firebaseAdminService.listarUsuariosFirebase();
    }

    @GetMapping("/firebase/{uid}")
    public UserRecord obtenerUsuarioFirebase(@PathVariable String uid, HttpServletRequest request) throws Exception {
        usuarioService.validarAdmin(request);
        return firebaseAdminService.obtenerUsuario(uid);
    }

    @PostMapping("/firebase")
    public UserRecord crearUsuarioFirebase(@RequestBody UsuarioFirebaseDTO body, HttpServletRequest request) throws Exception {
        usuarioService.validarAdmin(request);
        return firebaseAdminService.crearUsuarioFirebase(
                body.getEmail(),
                body.getPassword(),
                body.getNombre(),
                body.getFotoPerfil()
        );
    }

    @PutMapping("/firebase/{uid}")
    public UserRecord actualizarUsuarioFirebase(@PathVariable String uid, @RequestBody UsuarioFirebaseDTO body, HttpServletRequest request) throws Exception {
        usuarioService.validarAdmin(request);
        return firebaseAdminService.actualizarUsuarioFirebase(
                uid,
                body.getNombre(),
                body.getEmail(),
                body.getFotoPerfil()
        );
    }

    @DeleteMapping("/firebase/{uid}")
    public void eliminarUsuarioFirebase(@PathVariable String uid, HttpServletRequest request) throws Exception {
        usuarioService.validarAdmin(request);
        firebaseAdminService.eliminarUsuario(uid);
    }
}
