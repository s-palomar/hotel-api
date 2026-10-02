// =========================================================
// HOTEL API · FRONTEND
// Aplicación principal
// =========================================================


// =========================================================
// ELEMENTOS DEL DOM
// =========================================================

const content = document.getElementById("content");
const headerTitle = document.querySelector(".header h2");
const headerSubtitle = document.querySelector(".header p");

const navLinks = document.querySelectorAll(".nav-link");


// =========================================================
// CONFIGURACIÓN DE LAS PÁGINAS
// =========================================================

const pages = {

    dashboard: {
        title: "Dashboard",
        subtitle: "Panel de recepción",

        render: () => `
            <div class="welcome">

                <h2>Bienvenido a HOTEL API</h2>

                <p>
                    Panel de gestión para recepción.
                </p>

                <p>
                    Desde aquí podrás gestionar reservas,
                    habitaciones, clientes y hoteles.
                </p>

            </div>
        `
    },

    reservas: {
        title: "Reservas",
        subtitle: "Gestión de reservas",

        render: async () => {

            try {

                const reservas = await obtenerReservas();
                const clientes = await obtenerClientes();
                const hoteles = await obtenerHoteles();

                return `
                    <div class="page-header">
                        <div>
                            <h2>Reservas</h2>
                            <p>
                                Reservas registradas en el sistema
                            </p>
                        </div>

                        <button class="btn btn-primary" id="btn-crear-reserva">
                            + Crear reserva
                        </button>
                    </div>

                    <div class="table-container">
                        <table>
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Cliente</th>
                                    <th>Hotel</th>
                                    <th>Habitación</th>
                                    <th>Entrada</th>
                                    <th>Salida</th>
                                    <th>Huéspedes</th>
                                    <th>Estado</th>
                                    <th>Total</th>
                                    <th>Pagado</th>
                                    <th>Acciones</th>
                                </tr>
                            </thead>
                            <tbody>
                                ${reservas.map(reserva => {
                                    const hotel = hoteles.find(
                                        hotel => hotel.id === reserva.hotelId
                                    );

                                    let acciones = "";

                                    if (reserva.estadoReserva === "PENDIENTE") {
                                        acciones = `
                                            <select class="acciones-reserva">
                                                <option value="">Seleccionar acción</option>
                                                <option value="confirmar">Confirmar</option>
                                                <option value="pago">Registrar pago</option>
                                                <option value="cancelar">Cancelar</option>
                                            </select>
                                        `;

                                    } else if (reserva.estadoReserva === "CONFIRMADA") {

                                        acciones = `
                                            <select class="acciones-reserva">
                                                <option value="">Seleccionar acción</option>
                                                <option value="pago">Registrar pago</option>
                                                <option value="checkin">Check-in</option>
                                                <option value="cancelar">Cancelar</option>
                                            </select>
                                        `;

                                    } else if (reserva.estadoReserva === "OCUPADA") {

                                        acciones = `
                                            <select class="acciones-reserva">
                                                <option value="">Seleccionar acción</option>
                                                <option value="checkout">Checkout</option>
                                            </select>
                                        `;

                                    }

                                    return `
                                        <tr>
                                            <td>${reserva.id}</td>
                                            <td>${reserva.clienteDni}</td>
                                            <td>${hotel ? hotel.nombre : "—"}</td>
                                            <td>
                                                ${reserva.habitacion
                                                    ? `${reserva.habitacion.numero} · ${reserva.tipoHabitacion}`
                                                    : reserva.tipoHabitacion}
                                            </td>
                                            <td>${reserva.fechaEntrada}</td>
                                            <td>${reserva.fechaSalida}</td>
                                            <td>${reserva.numPax}</td>
                                            <td>${reserva.estadoReserva}</td>
                                            <td>${reserva.precioTotal} €</td>
                                            <td>${reserva.importePagado} €</td>

                                            <td>
                                                ${acciones}
                                            </td>
                                        </tr>
                                    `;

                                }).join("")}

                            </tbody>
                        </table>
                    </div>

                    <!-- MODAL CREAR RESERVA -->
                    <div class="modal-overlay" id="modal-crear-reserva">
                        <div class="modal">
                            <div class="modal-header">
                                <h3>Nueva reserva</h3>
                                <button
                                    type="button"
                                    class="modal-close"
                                    id="cerrar-modal-reserva">
                                    ×
                                </button>
                            </div>

                            <form id="form-crear-reserva">
                                <div class="form-group">
                                    <label for="cliente-reserva">Cliente</label>
                                    <select id="cliente-reserva" required>
                                        ${clientes.map(cliente => `
                                            <option value="${cliente.id}">
                                                ${cliente.nombre} — ${cliente.apellidos}
                                            </option>
                                            <option value="${cliente.id}">
                                                ${cliente.nombre} — ${cliente.apellidos} (${cliente.dni})
                                            </option>
                                        `).join("")}
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label for="hotel-reserva">Hotel</label>

                                    <select id="hotel-reserva" required>
                                        ${hoteles.map(hotel => `
                                            <option value="${hotel.id}">
                                                ${hotel.nombre} — ${hotel.ciudad}
                                            </option>
                                        `).join("")}
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label for="tipo-habitacion-reserva">Tipo habitación</label>
                                    <select id="tipo-habitacion-reserva" required>
                                    <option value="INDIVIDUAL">Individual</option>
                                    <option value="DOBLE">Doble</option>
                                    <option value="SUITE">Suite</option>
                                </select>
                                </div>

                                <div class="form-group">
                                    <label for="fechaEntrada-reserva">Fecha de entrada</label>
                                    <input type="date" id="fechaEntrada-reserva" required>
                                </div>

                                <div class="form-group">
                                    <label for="fechaSalida-reserva">Fecha de salida</label>
                                    <input type="date" id="fechaSalida-reserva" required>
                                </div>

                                <div class="form-group">
                                    <label for="num-pax-reserva">Número de huéspedes</label>
                                    <input
                                        type="number"
                                        id="num-pax-reserva"
                                        min="1"
                                        required>
                                </div>

                                <div class="modal-actions">
                                    <button
                                        type="button"
                                        class="btn btn-secondary"
                                        id="cancelar-modal-reserva">
                                        Cancelar
                                    </button>
                                    <button
                                        type="submit"
                                        class="btn btn-primary">
                                        Crear reserva
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                `;

            } catch (error) {

                return `
                    <div class="welcome">

                        <h2>Error al cargar las reservas</h2>

                        <p>
                            ${error.message}
                        </p>

                    </div>
                `;
            }
        }
    },

    hoteles: {
        title: "Hoteles",
        subtitle: "Gestión de hoteles",

        render: async () => {

            try {

                const hoteles = await obtenerHoteles();

                return `
                    <div class="page-header">

                        <div>
                            <h2>Hoteles</h2>
                            <p>
                                Hoteles registrados en el sistema
                            </p>
                        </div>

                        <button class="btn btn-primary" id="btn-crear-hotel">
                            + Crear hotel
                        </button>

                    </div>

                    <div class="table-container">

                        <table>

                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Hotel</th>
                                    <th>Ciudad</th>
                                    <th>Categoría</th>
                                </tr>
                            </thead>

                            <tbody>

                                ${hoteles.map(hotel => `
                                    <tr>

                                        <td>${hotel.id}</td>

                                        <td>
                                            <strong>
                                                ${hotel.nombre}
                                            </strong>
                                        </td>

                                        <td>
                                            ${hotel.ciudad}
                                        </td>

                                        <td>
                                            ${hotel.categoria} estrellas
                                        </td>

                                    </tr>
                                `).join("")}

                            </tbody>
                        </table>
                    </div>

                    <!-- MODAL CREAR HOTEL -->

                    <div class="modal-overlay" id="modal-crear-hotel">

                        <div class="modal">

                            <div class="modal-header">

                                <h3>Nuevo hotel</h3>

                                <button
                                    type="button"
                                    class="modal-close"
                                    id="cerrar-modal-hotel">
                                    ×
                                </button>

                            </div>

                            <form id="form-crear-hotel">

                                <div class="form-group">

                                    <label for="nombre-hotel">
                                        Nombre
                                    </label>

                                    <input
                                        type="text"
                                        id="nombre-hotel"
                                        required
                                    >

                                </div>

                                <div class="form-group">

                                    <label for="ciudad-hotel">
                                        Ciudad
                                    </label>

                                    <input
                                        type="text"
                                        id="ciudad-hotel"
                                        required
                                    >

                                </div>

                                <div class="form-group">
                                    <label for="categoria-hotel">
                                        Categoría
                                    </label>

                                    <select id="categoria-hotel" required>
                                        <option value="2">
                                            2 estrellas
                                        </option>                                        

                                        <option value="3">
                                            3 estrellas
                                        </option>

                                        <option value="4">
                                            4 estrellas
                                        </option>

                                        <option value="5">
                                            5 estrellas
                                        </option>
                                    </select>
                                </div>

                                <div class="modal-actions">
                                    <button
                                        type="button"
                                        class="btn btn-secondary"
                                        id="cancelar-modal-hotel">
                                        Cancelar
                                    </button>

                                    <button
                                        type="submit"
                                        class="btn btn-primary">
                                        Crear hotel
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                `;

            } catch (error) {

                return `
                    <div class="welcome">
                        <h2>Error al cargar los hoteles</h2>
                        <p>
                            ${error.message}
                        </p>
                    </div>
                `;
            }
        }
    },

    habitaciones: {
        title: "Habitaciones",
        subtitle: "Gestión de habitaciones",

        render: async () => {

            try {

                const habitaciones = await obtenerHabitaciones();
                const hoteles = await obtenerHoteles();

                return `
                    <div class="page-header">
                        <div>
                            <h2>Habitaciones</h2>
                            <p>
                                Habitaciones registradas en el sistema
                            </p>
                        </div>

                        <button class="btn btn-primary" id="btn-crear-habitacion">
                            + Crear habitación
                        </button>
                    </div>

                    <div class="table-container">
                        <table>
                            <thead>
                                <tr>                                    
                                    <th>ID</th>
                                    <th>Hotel</th>
                                    <th>Número</th>
                                    <th>Tipo</th>
                                    <th>Precio base</th>
                                    <th>Máx. huéspedes</th>
                                    <th>Acciones</th>
                                </tr>
                            </thead>

                            <tbody>
                                ${habitaciones.map(habitacion => {

                                    let acciones = `
                                            <select class="acciones-habitacion"
                                                data-habitacion-id="${habitacion.id}">
                                                <option value="">Seleccionar acción</option>
                                                <option value="modificar">Modificar</option>
                                                <option value="eliminar">Eliminar</option>
                                            </select>
                                        `;

                                    return `
                                        <tr>
                                            <td>${habitacion.id}</td>
                                            <td>${habitacion.hotelNombre}</td>
                                            <td><strong>${habitacion.numero}</strong></td>
                                            <td>${habitacion.tipoHabitacion}</td>
                                            <td>${habitacion.precioBase} €</td>
                                            <td>${habitacion.maxPax}</td>
                                            <td>${acciones}</td>
                                        </tr>
                                    `;
                                }).join("")}

                            </tbody>
                        </table>
                    </div>

                    <!-- MODAL CREAR HABITACION -->
                    <div class="modal-overlay" id="modal-crear-habitacion">
                        <div class="modal">
                            <div class="modal-header">
                                <h3>Nueva habitación</h3>
                                <button
                                    type="button"
                                    class="modal-close"
                                    id="cerrar-modal-habitacion">
                                    ×
                                </button>
                            </div>

                            <form id="form-crear-habitacion">
                                <div class="form-group">
                                    <label for="hotel-habitacion">Hotel</label>
                                    <select id="hotel-habitacion" required>
                                        ${hoteles.map(hotel => `
                                            <option value="${hotel.id}">
                                                ${hotel.nombre} — ${hotel.ciudad}
                                            </option>
                                        `).join("")}
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label for="numero-habitacion">Numero</label>
                                    <input type="text" id="numero-habitacion" required>
                                </div>

                                <div class="form-group">
                                    <label for="tipo-habitacion">Tipo habitación</label>
                                    <select id="tipo-habitacion" required>
                                        <option value="INDIVIDUAL">Individual</option>
                                        <option value="DOBLE">Doble</option>
                                        <option value="SUITE">Suite</option>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label for="precio-habitacion">Precio</label>
                                    <input type="number" id="precio-habitacion" required>
                                </div>

                                <div class="form-group">
                                    <label for="maxpax-habitacion">Máximo ocupantes</label>
                                    <input type="number" id="maxpax-habitacion" required>
                                </div>

                                <div class="modal-actions">
                                    <button
                                        type="button"
                                        class="btn btn-secondary"
                                        id="cancelar-modal-habitacion">
                                        Cancelar
                                    </button>
                                    <button
                                        type="submit"
                                        class="btn btn-primary">
                                        Crear habitacion
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>

                    <!-- MODAL MODIFICAR HABITACION -->
                    <div id="modal-modificar-habitacion" class="modal-overlay">
                        <div class="modal">
                            <div class="modal-header">
                                <h3>Modificar habitación</h3>
                                <button
                                    type="button"
                                    class="modal-close"
                                    id="cerrar-modal-modificar-habitacion">
                                    &times;
                                </button>
                            </div>

                            <form id="form-modificar-habitacion">
                                <div class="form-group">
                                    <label for="numero-modificar-habitacion">
                                        Número
                                    </label>
                                    <input
                                        type="text"
                                        id="numero-modificar-habitacion"
                                        required>
                                </div>

                                <div class="form-group">
                                    <label for="tipo-modificar-habitacion">
                                        Tipo de habitación
                                    </label>
                                    <select
                                        id="tipo-modificar-habitacion"
                                        required>
                                        <option value="INDIVIDUAL">
                                            Individual
                                        </option>
                                        <option value="DOBLE">
                                            Doble
                                        </option>
                                        <option value="SUITE">
                                            Suite
                                        </option>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label for="precio-modificar-habitacion">
                                        Precio base
                                    </label>
                                    <input
                                        type="number"
                                        id="precio-modificar-habitacion"
                                        min="0"
                                        step="0.01"
                                        required>
                                </div>

                                <div class="form-group">
                                    <label for="maxpax-modificar-habitacion">
                                        Máximo de huéspedes
                                    </label>
                                    <input
                                        type="number"
                                        id="maxpax-modificar-habitacion"
                                        min="1"
                                        required>
                                </div>

                                <div class="modal-actions">
                                    <button
                                        type="button"
                                        class="btn btn-secondary"
                                        id="cancelar-modal-modificar-habitacion">
                                        Cancelar
                                    </button>

                                    <button
                                        type="submit"
                                        class="btn btn-primary">
                                        Guardar cambios
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                `;

            } catch (error) {

                return `
                    <div class="welcome">
                        <h2>Error al cargar las habitaciones</h2>
                        <p>
                            ${error.message}
                        </p>
                    </div>
                `;
            }
        }
    }, 

    clientes: {
        title: "Clientes",
        subtitle: "Gestión de clientes",

        render: async () => {

            try {

                const clientes = await obtenerClientes();

                return `
                    <div class="page-header">
                        <div>
                            <h2>Clientes</h2>
                            <p>
                                Clientes registrados en el sistema
                            </p>
                        </div>

                        <button class="btn btn-primary" id="btn-crear-cliente">
                            + Crear cliente
                        </button>
                    </div>

                    <div class="table-container">
                        <table>
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>DNI</th>
                                    <th>Nombre</th>
                                    <th>Apellidos</th>
                                    <th>E-mail</th>
                                    <th>Telefono</th>
                                    <th>Nacionalidad</th>
                                    <th>Forma de Pago</th>
                                </tr>
                            </thead>

                            <tbody>
                                ${clientes.map(cliente => `
                                    <tr>
                                        <td>
                                            ${cliente.id}
                                        </td>

                                        <td>
                                            <strong>
                                                ${cliente.dni}
                                            </strong>
                                        </td>

                                        <td>
                                            ${cliente.nombre}
                                        </td>

                                        <td>
                                            ${cliente.apellidos}
                                        </td>

                                        <td>
                                            ${cliente.email}
                                        </td>

                                        <td>
                                            ${cliente.telefono}
                                        </td>

                                        <td>
                                            ${cliente.nacionalidad}
                                        </td>

                                        <td>
                                            ${cliente.formaPago}
                                        </td>
                                    </tr>
                                `).join("")}

                            </tbody>
                        </table>
                    </div>

                    <!-- MODAL CREAR CLIENTE -->
                    <div class="modal-overlay" id="modal-crear-cliente">
                        <div class="modal">
                            <div class="modal-header">
                                <h3>Nuevo cliente</h3>
                                <button
                                    type="button"
                                    class="modal-close"
                                    id="cerrar-modal-cliente">
                                    ×
                                </button>
                            </div>

                            <form id="form-crear-cliente">
                                <div class="form-group">
                                    <label for="dni-cliente">DNI</label>
                                    <input type="text" id="dni-cliente" required>
                                </div>
                                <div class="form-group">
                                    <label for="nombre-cliente">Nombre</label>
                                    <input type="text" id="nombre-cliente" required>
                                </div>
                                <div class="form-group">
                                    <label for="apellidos-cliente">Apellidos</label>
                                    <input type="text" id="apellidos-cliente" required>
                                </div>
                                <div class="form-group">
                                    <label for="email-cliente">E-mail</label>
                                    <input type="text" id="email-cliente" required>
                                </div>
                                <div class="form-group">
                                    <label for="telefono-cliente">Telefono</label>
                                    <input type="text" id="telefono-cliente" required>
                                </div>
                                <div class="form-group">
                                    <label for="nacionalidad-cliente">Nacionalidad</label>
                                    <input type="text" id="nacionalidad-cliente" required>
                                </div>
                                <div class="form-group">
                                    <label for="formaPago-cliente">Forma de pago</label>
                                    <select id="formaPago-cliente" required>
                                        <option value="EFECTIVO">Efectivo</option>
                                        <option value="TARJETA">Tarjeta</option>
                                        <option value="BIZUM">Bizum</option>
                                    </select>
                                </div>

                                <div class="modal-actions">
                                    <button
                                        type="button"
                                        class="btn btn-secondary"
                                        id="cancelar-modal-cliente">
                                        Cancelar
                                    </button>
                                    <button
                                        type="submit"
                                        class="btn btn-primary">
                                        Crear cliente
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                `;

            } catch (error) {

                return `
                    <div class="welcome">

                        <h2>Error al cargar los clientes</h2>

                        <p>
                            ${error.message}
                        </p>

                    </div>
                `;
            }
        }
    }
};


// =========================================================
// RENDERIZAR PÁGINA
// =========================================================

async function renderPage(pageName) {

    const page = pages[pageName];

    // Si la página no existe, mostramos Dashboard
    if (!page) {
        renderPage("dashboard");
        return;
    }

    // Cambiamos la cabecera
    headerTitle.textContent = page.title;
    headerSubtitle.textContent = page.subtitle;

    // Cambiamos el contenido
    content.innerHTML = await page.render();

    // Crear reserva
    if (pageName === "reservas") {

        const botonCrearReserva =
            document.getElementById("btn-crear-reserva");

        const modal =
            document.getElementById("modal-crear-reserva");

        const botonCerrar =
            document.getElementById("cerrar-modal-reserva");

        const botonCancelar =
            document.getElementById("cancelar-modal-reserva");

        const formulario =
            document.getElementById("form-crear-reserva");

        botonCrearReserva.addEventListener("click", () => {
            modal.classList.add("active");
        });

        botonCerrar.addEventListener("click", () => {
            modal.classList.remove("active");
        });

        botonCancelar.addEventListener("click", () => {
            modal.classList.remove("active");
        });

        formulario.addEventListener("submit", async (event) => {

            event.preventDefault();

            const clienteDni = document.getElementById("cliente-reserva").value;

            const reserva = {

                clienteDni: clienteDni,

                fechaEntrada:
                    document.getElementById("fechaEntrada-reserva").value,

                fechaSalida:
                    document.getElementById("fechaSalida-reserva").value,

                numPax:
                    Number(document.getElementById("numPax-reserva").value)
            };

            try {

                await crearReserva(reserva);
                modal.classList.remove("active");
                formulario.reset();
                await renderPage("reservas");

            } catch (error) {

                alert(error.message);
            }
        });
    }

    // Crear reserva
    if (pageName === "reservas") {

        const botonCrearReserva =
            document.getElementById("btn-crear-reserva");

        const modal =
            document.getElementById("modal-crear-reserva");

        const botonCerrar =
            document.getElementById("cerrar-modal-reserva");

        const botonCancelar =
            document.getElementById("cancelar-modal-reserva");

        const formulario =
            document.getElementById("form-crear-reserva");

        botonCrearReserva.addEventListener("click", () => {

            modal.classList.add("active");

        });

        botonCerrar.addEventListener("click", () => {

            modal.classList.remove("active");

        });

        botonCancelar.addEventListener("click", () => {

            modal.classList.remove("active");

        });

        formulario.addEventListener("submit", async (event) => {

            event.preventDefault();

            const reserva = {

                clienteId:
                    Number(
                        document.getElementById("cliente-reserva").value
                    ),

                hotelId:
                    Number(
                        document.getElementById("hotel-reserva").value
                    ),

                tipoHabitacion:
                    document.getElementById("tipo-habitacion-reserva").value,

                fechaEntrada:
                    document.getElementById("fechaEntrada-reserva").value,

                fechaSalida:
                    document.getElementById("fechaSalida-reserva").value,

                numPax:
                    Number(
                        document.getElementById("num-pax-reserva").value
                    )
            };

            try {

                await crearReserva(reserva);

                modal.classList.remove("active");
                formulario.reset();

                await renderPage("reservas");

            } catch (error) {

                alert(error.message);
            }
        });
    }

    // Crear hotel
    if (pageName === "hoteles") {

        const botonCrearHotel =
            document.getElementById("btn-crear-hotel");

        const modal =
            document.getElementById("modal-crear-hotel");

        const botonCerrar =
            document.getElementById("cerrar-modal-hotel");

        const botonCancelar =
            document.getElementById("cancelar-modal-hotel");

        const formulario =
            document.getElementById("form-crear-hotel");

        botonCrearHotel.addEventListener("click", () => {

            modal.classList.add("active");

        });

        botonCerrar.addEventListener("click", () => {

            modal.classList.remove("active");

        });

        botonCancelar.addEventListener("click", () => {

            modal.classList.remove("active");

        });

        formulario.addEventListener("submit", async (event) => {

            event.preventDefault();

            const hotel = {

                nombre: document.getElementById("nombre-hotel").value,

                ciudad: document.getElementById("ciudad-hotel").value,

                categoria: Number(
                    document.getElementById("categoria-hotel").value
                )

            };

            try {

                await crearHotel(hotel);

                modal.classList.remove("active");

                formulario.reset();

                await renderPage("hoteles");

            } catch (error) {

                alert(error.message);

            }
        });
    }

    // Acciones habitacion
    if (pageName === "habitaciones") {

        const botonCrearHabitacion =
            document.getElementById("btn-crear-habitacion");

        const modal =
            document.getElementById("modal-crear-habitacion");

        const botonCerrar =
            document.getElementById("cerrar-modal-habitacion");

        const botonCancelar =
            document.getElementById("cancelar-modal-habitacion");

        const formulario =
            document.getElementById("form-crear-habitacion");

        botonCrearHabitacion.addEventListener("click", () => {
            modal.classList.add("active");
        });

        botonCerrar.addEventListener("click", () => {
            modal.classList.remove("active");
        });

        botonCancelar.addEventListener("click", () => {
            modal.classList.remove("active");
        });

        formulario.addEventListener("submit", async (event) => {

            event.preventDefault();

            const hotelId = Number(document.getElementById("hotel-habitacion").value);

            const habitacion = {

                numero:
                    document.getElementById("numero-habitacion").value,

                tipoHabitacion:
                    document.getElementById("tipo-habitacion").value.toUpperCase(),

                precioBase:
                    Number(document.getElementById("precio-habitacion").value),

                maxPax:
                    Number(document.getElementById("maxpax-habitacion").value)

            };

            try {

                await crearHabitacion(hotelId, habitacion);
                modal.classList.remove("active");
                formulario.reset();
                await renderPage("habitaciones");

            } catch (error) {

                alert(error.message);
            }
        });

        const selectsAcciones =
            document.querySelectorAll(".acciones-habitacion");

        selectsAcciones.forEach(select => {

            select.addEventListener("change", async () => {

                const accion = select.value;

                const habitacionId =
                    Number(select.dataset.habitacionId);

                if (accion === "modificar") {
                    try {
                        const habitacion =
                            await obtenerHabitacion(habitacionId);

                        const modalModificar =
                            document.getElementById(
                                "modal-modificar-habitacion"
                            );

                        modalModificar.dataset.habitacionId = habitacionId;

                        document.getElementById(
                            "numero-modificar-habitacion"
                        ).value = habitacion.numero;

                        document.getElementById(
                            "tipo-modificar-habitacion"
                        ).value = habitacion.tipoHabitacion;

                        document.getElementById(
                            "precio-modificar-habitacion"
                        ).value = habitacion.precioBase;

                        document.getElementById(
                            "maxpax-modificar-habitacion"
                        ).value = habitacion.maxPax;

                        modalModificar.classList.add("active");

                    } catch (error) {
                        alert(error.message);
                    }

                    select.value = "";
                }

                if (accion === "eliminar") {

                    const confirmar = confirm(
                        `¿Seguro que quieres eliminar la habitación ${habitacionId}?`
                    );

                    if (!confirmar) {
                        select.value = "";
                        return;
                    }

                    try {
                        await eliminarHabitacion(habitacionId);
                        await renderPage("habitaciones");

                    } catch (error) {
                        alert(error.message);
                        select.value = "";
                    }
                }
            });

            const modalModificar =
                document.getElementById(
                    "modal-modificar-habitacion"
                );

            const botonCerrarModificar =
                document.getElementById(
                    "cerrar-modal-modificar-habitacion"
                );

            const botonCancelarModificar =
                document.getElementById(
                    "cancelar-modal-modificar-habitacion"
                );

            botonCerrarModificar.addEventListener("click", () => {
                modalModificar.classList.remove("active");
            });

            botonCancelarModificar.addEventListener("click", () => {
                modalModificar.classList.remove("active");
            });

            const formularioModificar =
                document.getElementById(
                    "form-modificar-habitacion"
                );

            formularioModificar.onsubmit = async (event) => {

                event.preventDefault();

                const modalModificar =
                    document.getElementById(
                        "modal-modificar-habitacion"
                    );

                const habitacionId =
                    Number(modalModificar.dataset.habitacionId);

                const habitacion = {

                    numero:
                        document.getElementById(
                            "numero-modificar-habitacion"
                        ).value,

                    tipoHabitacion:
                        document.getElementById(
                            "tipo-modificar-habitacion"
                        ).value,

                    precioBase:
                        Number(
                            document.getElementById(
                                "precio-modificar-habitacion"
                            ).value
                        ),

                    maxPax:
                        Number(
                            document.getElementById(
                                "maxpax-modificar-habitacion"
                            ).value
                        )
                };

                try {

                    await actualizarHabitacion(
                        habitacionId,
                        habitacion
                    );

                    modalModificar.classList.remove("active");

                    formularioModificar.reset();

                    await renderPage("habitaciones");

                } catch (error) {

                    alert(error.message);
                }
            };
        });
    }

    // Crear cliente
    if (pageName === "clientes") {

        const botonCrearCliente =
            document.getElementById("btn-crear-cliente");

        const modal =
            document.getElementById("modal-crear-cliente");

        const botonCerrar =
            document.getElementById("cerrar-modal-cliente");

        const botonCancelar =
            document.getElementById("cancelar-modal-cliente");

        const formulario =
            document.getElementById("form-crear-cliente");

        botonCrearCliente.addEventListener("click", () => {
            modal.classList.add("active");
        });

        botonCerrar.addEventListener("click", () => {
            modal.classList.remove("active");
        });

        botonCancelar.addEventListener("click", () => {
            modal.classList.remove("active");
        });

        formulario.addEventListener("submit", async (event) => {

            event.preventDefault();

            const cliente = {

                dni:
                    document.getElementById("dni-cliente").value,

                nombre:
                    document.getElementById("nombre-cliente").value,

                apellidos:
                    document.getElementById("apellidos-cliente").value,

                email:
                    document.getElementById("email-cliente").value,

                telefono:
                    document.getElementById("telefono-cliente").value,

                nacionalidad:
                    document.getElementById("nacionalidad-cliente").value,
                    
                formaPago:
                    document.getElementById("formaPago-cliente").value
            };

            try {

                await crearCliente(cliente);
                modal.classList.remove("active");
                formulario.reset();
                await renderPage("clientes");

            } catch (error) {

                alert(error.message);
            }
        });
    }

    // Actualizamos el enlace activo
    navLinks.forEach(link => {

        link.classList.remove("active");

        if (link.dataset.page === pageName) {
            link.classList.add("active");
        }

    });
}

// =========================================================
// NAVEGACIÓN
// =========================================================

navLinks.forEach(link => {

    link.addEventListener("click", event => {

        event.preventDefault();

        const pageName = link.dataset.page;

        renderPage(pageName);

    });

});

// =========================================================
// PÁGINA INICIAL
// =========================================================

renderPage("dashboard");

