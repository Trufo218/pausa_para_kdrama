

// Importar Firebase
import { initializeApp } from "https://www.gstatic.com/firebasejs/12.4.0/firebase-app.js";
import { getAuth, onAuthStateChanged } from "https://www.gstatic.com/firebasejs/12.4.0/firebase-auth.js";

// Configuración de Firebase
const firebaseConfig = {
  apiKey: "AIzaSyC3mYaJLSEOU6yU3CZBa6uAUUMZrByt224",
  authDomain: "pausaparakdrama.firebaseapp.com",
  projectId: "pausaparakdrama",
  storageBucket: "pausaparakdrama.appspot.com",
  messagingSenderId: "1041723137654",
  appId: "1:1041723137654:web:7b3fac057cb3cf2a6e0957",
  measurementId: "G-D83GTPWEM3"
};

// Inicializar Firebase
const app = initializeApp(firebaseConfig);
const auth = getAuth(app);

// Detectar usuario autenticado y cargar datos
onAuthStateChanged(auth, async (user) => {
  if (!user) {
    window.location.href = "login.html"; // Redirige si no está logueado
    return;
  }

  // Mostrar datos básicos de Firebase
  document.getElementById("nombreUsuario").textContent = user.displayName || "Usuario sin nombre";
  document.getElementById("correoUsuario").textContent = user.email || "";
  document.getElementById("fotoUsuario").src = user.photoURL || "./assets/img/avatar_usuario/usuario_default.jpg";

  try {
    const token = await user.getIdToken();

    // Llamada al backend para obtener bio, fecha de registro y favoritos
    const res = await fetch("http://localhost:8080/usuarios/me", {
      headers: { "Authorization": `Bearer ${token}` }
    });

    if (!res.ok) throw new Error("Error al obtener datos del perfil");

    const data = await res.json();

    document.getElementById("bioUsuario").textContent = data.bio || "“Aún no tienes una frase guardada.”";
    document.getElementById("numFavoritos").textContent = data.favoritos || 0;
    document.getElementById("fechaRegistro").textContent = data.fechaRegistro || "Desconocida";

  } catch (err) {
    console.error(err);
    document.getElementById("bioUsuario").textContent = "Error al cargar información adicional";
  }
});
