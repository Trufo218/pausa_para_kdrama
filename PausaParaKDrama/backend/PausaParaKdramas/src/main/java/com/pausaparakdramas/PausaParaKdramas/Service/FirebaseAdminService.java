package com.pausaparakdramas.PausaParaKdramas.Service;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ListUsersPage;
import com.google.firebase.auth.UserRecord;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FirebaseAdminService {

    /** Lista todos los usuarios de Firebase Auth */
    public List<UserRecord> listarUsuariosFirebase() throws Exception {
        List<UserRecord> lista = new ArrayList<>();
        ListUsersPage page = FirebaseAuth.getInstance().listUsers(null);

        while (page != null) {
            for (UserRecord user : page.getValues()) {
                lista.add(user);
            }
            page = page.getNextPage();
        }

        return lista;
    }

    /** Obtiene un usuario por UID */
    public UserRecord obtenerUsuario(String uid) throws Exception {
        return FirebaseAuth.getInstance().getUser(uid);
    }

    /** Elimina un usuario */
    public void eliminarUsuario(String uid) throws Exception {
        FirebaseAuth.getInstance().deleteUser(uid);
    }

    /** Actualiza usuario */
    public UserRecord actualizarUsuarioFirebase(String uid, String nombre, String email, String foto) throws Exception {
        UserRecord.UpdateRequest req = new UserRecord.UpdateRequest(uid)
                .setDisplayName(nombre)
                .setEmail(email);
        if (foto != null && !foto.isBlank()) {
            req.setPhotoUrl(foto);
        }

        return FirebaseAuth.getInstance().updateUser(req);
    }

    /** Crear un usuario desde admin */
    public UserRecord crearUsuarioFirebase(String email, String password, String nombre, String foto) throws Exception {
        UserRecord.CreateRequest req = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password)
                .setDisplayName(nombre);

        if (foto != null && !foto.isBlank()) {
            req.setPhotoUrl(foto);
        }

        return FirebaseAuth.getInstance().createUser(req);
    }
}
