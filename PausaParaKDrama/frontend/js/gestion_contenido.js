document.addEventListener("DOMContentLoaded", () => {
  const btnBuscar = document.getElementById("btnBuscar");
  const inputTitulo = document.getElementById("tituloBuscar");
  const mensaje = document.getElementById("mensaje");
  const listaKdramas = document.getElementById("listaKdramas");

  // Elementos del modal
  const modal = document.getElementById("modalDetalles");
  const cerrarModal = document.getElementById("cerrarModal");
  const modalImagen = document.getElementById("modalImagen");
  const modalTitulo = document.getElementById("modalTitulo");
  const modalAnio = document.getElementById("modalAnio");
  const modalGenero = document.getElementById("modalGenero");
  const modalSinopsis = document.getElementById("modalSinopsis");

  //Cargar K-Dramas al iniciar
  cargarKdramas();

  
  // Buscar + Guardar K-Drama
  
  btnBuscar.addEventListener("click", async () => {
    const titulo = inputTitulo.value.trim();

    if (!titulo) {
      mostrarMensaje("Introduce un título para buscar.", "red");
      return;
    }

    mostrarMensaje("Buscando en TMDB...", "black");

    try {
      const response = await fetch(
        `http://localhost:8080/api/kdramas/buscar?q=${encodeURIComponent(
          titulo
        )}`
      );

      if (!response.ok) {
        throw new Error("No se pudo obtener el K-Drama");
      }

      const kdrama = await response.json();

      mostrarMensaje(`✔ Guardado: ${kdrama.tituloKdrama}`, "green");

      agregarFila(kdrama);
    } catch (err) {
      console.error(err);
      mostrarMensaje("Error al buscar el K-Drama.", "red");
    }
  });

    // Mostrar mensajes
  
  function mostrarMensaje(texto, color) {
    mensaje.textContent = texto;
    mensaje.style.color = color;
  }

  
  //  Cargar K-Dramas almacenados
  
  async function cargarKdramas() {
    try {
      const response = await fetch("http://localhost:8080/api/kdramas/listar");

      if (!response.ok) throw new Error();

      const data = await response.json();
      listaKdramas.innerHTML = "";

      data.forEach((kdrama) => agregarFila(kdrama));
    } catch (error) {
      console.error("Error cargando K-Dramas:", error);
    }
  }

  
  // Dibujar fila en tabla
  
  function agregarFila(k) {
    const tr = document.createElement("tr");

    tr.innerHTML = `
      <td>${k.idKdrama}</td>
      <td>${k.tituloKdrama}</td>
      <td>${k.anio ?? "Sin año"}</td>
      <td>${k.genero ?? "?"}</td>
      <td>
        <button class="ver">Ver</button>
        <button class="editar">Editar</button>
        <button class="eliminar">Eliminar</button>
      </td>
    `;

    tr.querySelector(".ver").addEventListener("click", () => verDetalles(k));
    tr.querySelector(".editar").addEventListener("click", () => editarKdrama(k));
    tr.querySelector(".eliminar").addEventListener("click", () =>
      eliminarKdrama(k.idKdrama)
    );

    listaKdramas.appendChild(tr);
  }

  // Ver detalles en modal
  
  function verDetalles(k) {
    modalTitulo.textContent = k.tituloKdrama;
    modalAnio.textContent = k.anio ?? "Sin año";
    modalGenero.textContent = k.genero ?? "Sin género";
    modalSinopsis.textContent = k.sinopsis ?? "Sin sinopsis disponible";

    // Aquí se usa DIRECTAMENTE la URL que viene de la BD
    const urlImagen = k.imagenUrl && k.imagenUrl.trim().length > 0
      ? k.imagenUrl
      : "img/no-image.png";

    modalImagen.src = urlImagen;
    modalImagen.alt = "Póster de " + k.tituloKdrama;

    modal.style.display = "flex";
  }

  
  // Cerrar modal
 
  cerrarModal.addEventListener("click", () => {
    modal.style.display = "none";
  });

  window.addEventListener("click", (e) => {
    if (e.target === modal) modal.style.display = "none";
  });

  
  // Editar (placeholder)
  
  function editarKdrama(k) {
    alert("Aquí podrás editar: " + k.tituloKdrama);
  }

  
  // Eliminar K-Drama
  
  async function eliminarKdrama(id) {
    if (!confirm("¿Eliminar este K-Drama?")) return;

    try {
      const res = await fetch(`http://localhost:8080/api/kdramas/${id}`, {
        method: "DELETE",
      });

      if (!res.ok) {
        alert("No se pudo eliminar.");
        return;
      }

      cargarKdramas();
    } catch (e) {
      console.error("Error al eliminar", e);
    }
  }
});
