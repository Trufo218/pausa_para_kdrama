
// Importar Firebase

import { initializeApp } from "https://www.gstatic.com/firebasejs/11.0.1/firebase-app.js";
import { getAuth, signInWithEmailAndPassword } from "https://www.gstatic.com/firebasejs/11.0.1/firebase-auth.js";


// Configuración de Firebase 

const firebaseConfig = {
    apiKey: "AIzaSyC3mYaJLSEOU6yU3CZBa6uAUUMZrByt224",
    authDomain: "pausaparakdrama.firebaseapp.com",
    projectId: "pausaparakdrama",
    storageBucket: "pausaparakdrama.firebasestorage.app",
    messagingSenderId: "1041723137654",
    appId: "1:1041723137654:web:7b3fac057cb3cf2a6e0957",
    measurementId: "G-D83GTPWEM3"
};


// Inicializar Firebase

const app = initializeApp(firebaseConfig);
const auth = getAuth(app);


// Obtener elementos del DOM

const form = document.getElementById("formLogin");
const mensaje = document.getElementById("mensaje");


// Evento de envío del formulario

form.addEventListener("submit", async (e) => {
    e.preventDefault();
    let valid = true;

    const email = form.email.value.trim();
    const password = form.password.value.trim();

    // === Validación de campos ===
    if (!email || !email.includes("@")) {
        valid = false;
        document.getElementById("error-email").textContent = "Email inválido";
    } else {
        document.getElementById("error-email").textContent = "";
    }

    if (!password) {
        valid = false;
        document.getElementById("error-password").textContent = "Contraseña requerida";
    } else {
        document.getElementById("error-password").textContent = "";
    }

    
    // Si los datos son válidos → iniciar sesión
   
    if (valid) {
        try {
            mensaje.style.color = "black";
            mensaje.textContent = "Iniciando sesión...";

            // === Autenticar con Firebase ===
            const cred = await signInWithEmailAndPassword(auth, email, password);
            const user = cred.user;
            const token = await user.getIdToken();

            // === Llamada al backend para sincronizar el usuario ===
            const response = await fetch("http://localhost:8080/usuarios/sync", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify({
                    nombre: user.displayName || "Usuario sin nombre",
                    email: user.email,
                    fotoPerfil: user.photoURL || null
                })
            });

            if (!response.ok) {
                throw new Error("Error al sincronizar el usuario con el servidor.");
            }

            const usuario = await response.json();
            console.log("✅ Usuario sincronizado:", usuario);

            // === Guardar token, email y roles en localStorage ===
            localStorage.setItem("token", token);
            localStorage.setItem("usuarioEmail", user.email);
            localStorage.setItem("roles", JSON.stringify(usuario.roles || []));

            // === Mostrar mensaje de éxito ===
            mensaje.style.color = "green";
            mensaje.textContent = "Inicio de sesión exitoso. Redirigiendo...";

            
            // Redirección según rol
            
            setTimeout(() => {
                const roles = usuario.roles || [];

                if (roles.includes("ADMIN")) {
                    window.location.href = "admin.html";
                } else if (roles.includes("USER")) {
                    window.location.href = "usuario.html";
                } else {
                    // Si no tiene roles definidos, redirigir al inicio
                    window.location.href = "index.html";
                }
            }, 1200);

        } catch (error) {
            console.error("❌ Error al iniciar sesión:", error);
            mensaje.style.color = "red";

            // Mensajes personalizados según error
            if (error.code === "auth/user-not-found") {
                mensaje.textContent = "Usuario no encontrado.";
            } else if (error.code === "auth/wrong-password") {
                mensaje.textContent = "Contraseña incorrecta.";
            } else if (error.code === "auth/invalid-email") {
                mensaje.textContent = "Email inválido.";
            } else {
                mensaje.textContent = "Error al iniciar sesión. Inténtalo de nuevo.";
            }
        }
    }
});
