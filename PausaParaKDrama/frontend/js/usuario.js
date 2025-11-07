import { initializeApp } from "https://www.gstatic.com/firebasejs/12.4.0/firebase-app.js";
import { getAuth, onAuthStateChanged } from "https://www.gstatic.com/firebasejs/12.4.0/firebase-auth.js";

const firebaseConfig = {
    apiKey: "AIzaSyC3mYaJLSEOU6yU3CZBa6uAUUMZrByt224",
    authDomain: "pausaparakdrama.firebaseapp.com",
    projectId: "pausaparakdrama",
    storageBucket: "pausaparakdrama.appspot.com",
    messagingSenderId: "1041723137654",
    appId: "1:1041723137654:web:7b3fac057cb3cf2a6e0957",
    measurementId: "G-D83GTPWEM3"
};
const app = initializeApp(firebaseConfig);
const auth = getAuth(app);

onAuthStateChanged(auth, async (user) => {
  if (!user) { window.location.href = "login.html"; return; }

  document.getElementById("nombreUsuario").textContent = user.displayName || "Usuario";
  document.getElementById("correoUsuario").textContent = user.email || "";
  document.getElementById("fotoUsuario").src = user.photoURL || "./assets/img/avatar_usuario/usuario_default.jpg";

  try {
    const token = await user.getIdToken();
    const res = await fetch(`http://localhost:8080/usuarios/me`, {
      headers: { "Authorization": `Bearer ${token}` }
    });
    if (res.ok) {
      const data = await res.json();
      document.getElementById("bioUsuario").textContent = data.bio || "";
    }
  } catch (err) { console.error(err); }
});
