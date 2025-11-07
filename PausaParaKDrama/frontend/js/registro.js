import { initializeApp } from "https://www.gstatic.com/firebasejs/12.4.0/firebase-app.js";
import { getAuth, createUserWithEmailAndPassword, updateProfile } from "https://www.gstatic.com/firebasejs/12.4.0/firebase-auth.js";

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

const form = document.getElementById('formRegistro');
const mensaje = document.getElementById('mensaje');

form.addEventListener('submit', async (event) => {
    event.preventDefault();
    limpiarErrores();

    const nombre = document.getElementById('nombre').value.trim();
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value.trim();
    const confirm_password = document.getElementById('confirm_password').value.trim();

    let hayErrores = false;
    if (!nombre) { mostrarError('nombre', "El campo nombre es obligatorio."); hayErrores = true; }
    if (!email) { mostrarError("email", "El email es obligatorio."); hayErrores = true; }
    else if (!validarEmail(email)) { mostrarError("email", "Formato de email incorrecto."); hayErrores = true; }
    if (!password) { mostrarError("password", "La contraseña es obligatoria."); hayErrores = true; }
    else if (password.length < 8) { mostrarError("password", "La contraseña debe tener al menos 8 caracteres."); hayErrores = true; }
    if (!confirm_password) { mostrarError("confirm_password", "Debes confirmar la contraseña."); hayErrores = true; }
    else if (password !== confirm_password) { mostrarError("confirm_password", "Las contraseñas no coinciden."); hayErrores = true; }

    if (hayErrores) {
        mensaje.textContent = "Por favor, corrige los errores.";
        mensaje.style.color = "red";
        return;
    }

    try {
        mensaje.style.color = "black";
        mensaje.textContent = "Registrando usuario...";

        const userCredential = await createUserWithEmailAndPassword(auth, email, password);
        const user = userCredential.user;

        await updateProfile(user, { displayName: nombre });
        const token = await user.getIdToken(true);

        const response = await fetch("http://localhost:8080/usuarios/sync", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify({
                nombre: nombre,
                email: email,
                fotoPerfil: user.photoURL || null
            })
        });

        if (!response.ok) throw new Error("Error al sincronizar con el backend");

        mensaje.style.color = "green";
        mensaje.textContent = "✅ Registro exitoso! Redirigiendo...";
        setTimeout(() => window.location.href = "usuario.html", 2000);

    } catch (error) {
        console.error(error);
        mensaje.style.color = "red";
        mensaje.textContent = error.message || "Error desconocido al registrar usuario";
    }
});

function validarEmail(email) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

function mostrarError(campo, mensajeError) {
    const span = document.getElementById(`error-${campo}`);
    const input = document.getElementById(campo);
    if(span) span.textContent = mensajeError;
    if(input) input.classList.add("error-input");
}

function limpiarErrores() {
    document.querySelectorAll(".error").forEach(span => span.textContent = "");
    document.querySelectorAll(".error-input").forEach(input => input.classList.remove("error-input"));
}
