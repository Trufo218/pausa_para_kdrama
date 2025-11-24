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

onAuthStateChanged(auth, (user) => {
  if (!user) {
    window.location.href = "login.html";
  }
});


document.addEventListener("DOMContentLoaded", cargarKdramas);

function cargarKdramas() {
  fetch("http://localhost:8080/api/kdramas/listar")
    .then(response => {
      if (!response.ok) {
        throw new Error("Error al obtener los K-Dramas: " + response.status);
      }
      return response.json();
    })
    .then(lista => pintarGrid(lista))
    .catch(err => console.error(err));
}

function pintarGrid(lista) {
  const grid = document.getElementById("gridKdramas");
  grid.innerHTML = "";

  lista.forEach(kdrama => {
    const rutaPortada = kdrama.imagenUrl; // ✔️ ahora sí

    grid.innerHTML += `
      <div class="col">
        <div class="card h-100 text-center border-0">
          <img 
            src="${rutaPortada}" 
            class="card-img-top rounded shadow-sm"
            style="height: 220px; object-fit: cover;"
            alt="${kdrama.tituloKdrama}"
          >
          <div class="card-body p-2">
            <h6 class="card-title mt-2">${kdrama.tituloKdrama}</h6>
          </div>
        </div>
      </div>
    `;
  });
}


// Menú hamburguesa
const iconoMenu = document.getElementById("iconoMenu");
const menu = document.getElementById("menuDesplegable");

iconoMenu.addEventListener("click", () => {
  menu.style.display = menu.style.display === "flex" ? "none" : "flex";
});

//Fuera del menú
document.addEventListener("click", (e) => {
  if (!iconoMenu.contains(e.target) && !menu.contains(e.target)) {
    menu.style.display = "none";
  }
});
