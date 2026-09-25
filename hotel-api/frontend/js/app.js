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

                return `
                    <div class="page-header">

                        <div>
                            <h2>Reservas</h2>

                            <p>
                                Reservas registradas en el sistema
                            </p>
                        </div>

                    </div>


                    <div class="table-container">

                        <table>

                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Cliente</th>
                                    <th>Entrada</th>
                                    <th>Salida</th>
                                    <th>Estado</th>
                                    <th>Total</th>
                                </tr>
                            </thead>

                            <tbody>

                                ${reservas.map(reserva => `
                                    <tr>

                                        <td>
                                            ${reserva.id}
                                        </td>

                                        <td>
                                            ${reserva.clienteDni}
                                        </td>

                                        <td>
                                            ${reserva.fechaEntrada}
                                        </td>

                                        <td>
                                            ${reserva.fechaSalida}
                                        </td>

                                        <td>
                                            ${reserva.estadoReserva}
                                        </td>

                                        <td>
                                            ${reserva.precioTotal} €
                                        </td>

                                    </tr>
                                `).join("")}

                            </tbody>

                        </table>

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

                return `
                    <div class="page-header">

                        <div>
                            <h2>Habitaciones</h2>
                            <p>
                                Habitaciones registradas en el sistema
                            </p>
                        </div>

                    </div>


                    <div class="table-container">

                        <table>

                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Número</th>
                                    <th>Tipo</th>
                                    <th>Precio base</th>
                                    <th>Máx. huéspedes</th>
                                </tr>
                            </thead>

                            <tbody>

                                ${habitaciones.map(habitacion => `
                                    <tr>

                                        <td>
                                            ${habitacion.id}
                                        </td>

                                        <td>
                                            <strong>
                                                ${habitacion.numero}
                                            </strong>
                                        </td>

                                        <td>
                                            ${habitacion.tipoHabitacion}
                                        </td>

                                        <td>
                                            ${habitacion.precioBase} €
                                        </td>

                                        <td>
                                            ${habitacion.maxPax}
                                        </td>

                                    </tr>
                                `).join("")}

                            </tbody>

                        </table>

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

