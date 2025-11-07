
const API_LOCAL = "http://localhost:8080/usuarios";
const API_FIREBASE = "http://localhost:8080/usuarios/firebase";

const tablaBody = document.querySelector("#tablaUsuarios tbody");
const modal = document.getElementById("modal");
const form = document.getElementById("formUsuario");
const btnAbrirCrear = document.getElementById("btnAbrirCrear");
const btnCancelar = document.getElementById("btnCancelar");
const crearPassGroup = document.getElementById("crearPasswordGroup");

const inputLocalId = document.getElementById("localId");
const inputFirebaseUid = document.getElementById("firebaseUid");
const inputNombre = document.getElementById("nombre");
const inputEmail = document.getElementById("email");
const inputPassword = document.getElementById("password");
const inputFoto = document.getElementById("foto");
const inputRoles = document.getElementById("roles");
const modalTitle = document.getElementById("modalTitle");

// ---------- Obtener token firebase del usuario actual ----------
async function getToken() {
  const user = firebase.auth().currentUser;
  if (!user) return null;
  return await user.getIdToken(true);
}

// ---------- abrir/cerrar modal ----------
function abrirModalCrear() {
  modalTitle.textContent = "Crear usuario";
  inputLocalId.value = "";
  inputFirebaseUid.value = "";
  inputNombre.value = "";
  inputEmail.value = "";
  inputPassword.value = "";
  inputFoto.value = "";
  inputRoles.value = "USER";
  crearPassGroup.style.display = "block";
  modal.style.display = "flex";
}
function abrirModalEditar(localUser) {
  modalTitle.textContent = "Editar usuario";
  inputLocalId.value = localUser.idUsuario;
  inputFirebaseUid.value = localUser.firebaseUid || "";
  inputNombre.value = localUser.nombre || "";
  inputEmail.value = localUser.email || "";
  inputFoto.value = localUser.fotoPerfil || "";
  inputRoles.value = (localUser.roles && localUser.roles[0]) ? localUser.roles[0] : "USER";
  // En edición NO pedimos contraseña
  inputPassword.value = "";
  crearPassGroup.style.display = "none";
  modal.style.display = "flex";
}
function cerrarModal() {
  modal.style.display = "none";
}

// ---------- cargar usuarios locales (BD) ----------
async function cargarUsuarios() {
  try {
    const token = await getToken();
    if (!token) { alert("Debes iniciar sesión con un admin."); return; }

    const res = await fetch(API_LOCAL, {
      method: "GET",
      headers: { "Authorization": "Bearer " + token }
    });

    if (!res.ok) {
      if (res.status === 403) alert("No tienes permisos de administrador.");
      else alert("Error cargando usuarios: " + res.status);
      return;
    }

    const lista = await res.json();
    console.log("DEBUG usuarios recibidos:", lista); //Pruebas de error en obtener el id. 
    tablaBody.innerHTML = "";

    lista.forEach(u => {
      const tr = document.createElement("tr");

      tr.innerHTML = `
        <td>${u.idUsuario}</td>
        <td>${u.firebaseUid || "-"}</td>
        <td>${escapeHtml(u.nombre)}</td>
        <td>${escapeHtml(u.email)}</td>
        <td>${Array.isArray(u.roles) ? u.roles.join(", ") : (u.roles || "-")}</td>
        <td>${u.fechaRegistro ? u.fechaRegistro : "-"}</td>
        <td class="acciones">
          <button class="editar" data-id="${u.idUsuario}">Editar</button>
          <button class="eliminar" data-id="${u.idUsuario}" data-uid="${u.firebaseUid || ""}">Eliminar</button>
        </td>
      `;

      // Eventos de los btn.
      tr.querySelector(".editar").addEventListener("click", () => abrirModalEditar(u));
      tr.querySelector(".eliminar").addEventListener("click", () => eliminarUsuarioConfirm(u));

      tablaBody.appendChild(tr);
    });

  } catch (err) {
    console.error(err);
    alert("Error inesperado cargando usuarios.");
  }
}

// ---------- crear usuario: 1) crear en Firebase vía backend 2) crear registro local ----------
async function crearUsuario(event) {
  event.preventDefault();

  const nombre = inputNombre.value.trim();
  const email = inputEmail.value.trim();
  const password = inputPassword.value;
  const foto = inputFoto.value.trim();
  const rol = inputRoles.value;

  if (!nombre || !email || !password) {
    alert("Nombre, email y contraseña son obligatorios al crear.");
    return;
  }

  try {
    const token = await getToken();
    if (!token) { alert("No estás autenticado."); return; }

    // 1) Crear en Firebase (backend admin endpoint)
    const createFirebaseResp = await fetch(API_FIREBASE, {
      method: "POST",
      headers: {
        "Authorization": "Bearer " + token,
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        email,
        password,
        nombre,
        foto
      })
    });

    if (!createFirebaseResp.ok) {
      const text = await createFirebaseResp.text();
      throw new Error("Error creando en Firebase: " + createFirebaseResp.status + " " + text);
    }

    const userRecord = await createFirebaseResp.json(); // espera UserRecord (contendrá uid)
    const firebaseUid = userRecord.uid || userRecord.uidString || userRecord.localId || null;

    // 2) Crear registro local en BD
    const localResp = await fetch(API_LOCAL, {
      method: "POST",
      headers: {
        "Authorization": "Bearer " + token,
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        firebaseUid,
        nombre,
        email,
        fotoPerfil: foto || null,
        roles: [rol]
      })
    });

    if (!localResp.ok) {
      // si falló la creación local, intenta eliminar el usuario de Firebase para no dejar inconsistencia
      try { await fetch(`${API_FIREBASE}/${firebaseUid}`, { method: "DELETE", headers: { "Authorization": "Bearer " + token } }); } catch(e){/*ignore*/}

      const text = await localResp.text();
      throw new Error("Error creando registro local: " + localResp.status + " " + text);
    }

    cerrarModal();
    cargarUsuarios();

  } catch (err) {
    console.error(err);
    alert(err.message || "Error creando usuario.");
  }
}

// ---------- editar usuario: actualizar Firebase (si hay uid) y actualizar local ----------
async function guardarEdicion(event) {
  event.preventDefault();

  const id = inputLocalId.value;
  const firebaseUid = inputFirebaseUid.value || null;
  const nombre = inputNombre.value.trim();
  const email = inputEmail.value.trim();
  const foto = inputFoto.value.trim();
  const rol = inputRoles.value;

  if (!id) { alert("Falta id local."); return; }

  try {
    const token = await getToken();
    if (!token) { alert("No estás autenticado."); return; }

    // 1) Si hay firebaseUid, actualiza Firebase primero
    if (firebaseUid) {
      const respFirebase = await fetch(`${API_FIREBASE}/${firebaseUid}`, {
        method: "PUT",
        headers: {
          "Authorization": "Bearer " + token,
          "Content-Type": "application/json"
        },
        body: JSON.stringify({ nombre, email, foto })
      });

      if (!respFirebase.ok) {
        const text = await respFirebase.text();
        throw new Error("Error actualizando Firebase: " + respFirebase.status + " " + text);
      }
    }

    // 2) Actualiza registro local
    const respLocal = await fetch(`${API_LOCAL}/${id}`, {
      method: "PUT",
      headers: {
        "Authorization": "Bearer " + token,
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        nombre,
        email,
        fotoPerfil: foto || null,
        roles: [rol]
      })
    });

    if (!respLocal.ok) {
      const text = await respLocal.text();
      throw new Error("Error actualizando registro local: " + respLocal.status + " " + text);
    }

    cerrarModal();
    cargarUsuarios();

  } catch (err) {
    console.error(err);
    alert(err.message || "Error actualizando usuario.");
  }
}

// ---------- eliminar usuario: elimina en local y en Firebase (si uid) ----------
async function eliminarUsuarioConfirm(localUser) {
  if (!confirm(`Eliminar usuario ${localUser.nombre} ?`)) return;

  try {
    const token = await getToken();
    if (!token) { alert("No estás autenticado."); return; }

    // 1) Eliminar local (primero) para mantener consistencia si backend previene borrar local si existe en Firebase
    const respLocal = await fetch(`${API_LOCAL}/${localUser.idUsuario}`, {
      method: "DELETE",
      headers: { "Authorization": "Bearer " + token }
    });

    if (!respLocal.ok) {
      const txt = await respLocal.text();
      throw new Error("Error eliminando local: " + respLocal.status + " " + txt);
    }

    // 2) Si existía en Firebase, eliminar también
    if (localUser.firebaseUid) {
      const respFb = await fetch(`${API_FIREBASE}/${localUser.firebaseUid}`, {
        method: "DELETE",
        headers: { "Authorization": "Bearer " + token }
      });
      if (!respFb.ok) {
        console.warn("No se pudo borrar en Firebase, pero local sí borrado.", await respFb.text());
      }
    }

    cargarUsuarios();

  } catch (err) {
    console.error(err);
    alert(err.message || "Error eliminando usuario.");
  }
}

// ---------- util: escapar html para evitar inyección simple en la tabla ----------
function escapeHtml(str) {
  if (!str && str !== 0) return "";
  return String(str).replace(/[&<>"'`=\/]/g, s => ({
    '&': '&amp;','<': '&lt;','>': '&gt;','"': '&quot;',"'": '&#39;','/': '&#47;','`': '&#96;','=': '&#61;'
  })[s]);
}

// ---------- eventos ----------
btnAbrirCrear.addEventListener("click", abrirModalCrear);
btnCancelar.addEventListener("click", (e) => { e.preventDefault(); cerrarModal(); });
form.addEventListener("submit", (e) => {
  if (inputLocalId.value) return guardarEdicion(e); // edición
  return crearUsuario(e); // creación
});

// cargar al inicio (si ya hay sesión)
firebase.auth().onAuthStateChanged(user => {
  if (user) cargarUsuarios();
  else {
    // opcional: redirigir al login si no logueado
    console.log("No autenticado en Firebase.");
  }
});
