// =========================================================
// HOTEL API · COMUNICACIÓN CON EL BACKEND
// =========================================================

const API_URL = "http://localhost:8080/api";


// =========================================================
// FUNCIÓN AUXILIAR PARA LAS PETICIONES GET
// =========================================================

async function getJson(url) {

    const response = await fetch(url);

    if (!response.ok) {
        throw new Error(
            `Error HTTP ${response.status}: ${response.statusText}`
        );
    }

    return await response.json();
}


// =========================================================
// HOTELES
// =========================================================

async function obtenerHoteles() {

    return await getJson(`${API_URL}/hoteles`);
}

async function crearHotel(hotel) {

    const response = await fetch(`${API_URL}/hoteles`, {

        method: "POST",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify(hotel)

    });

    if (!response.ok) {

        throw new Error(
            `Error HTTP ${response.status}: ${response.statusText}`
        );

    }

    return await response.json();
}


// =========================================================
// HABITACIONES
// =========================================================

async function obtenerHabitaciones() {

    return await getJson(`${API_URL}/habitaciones`);
}

async function obtenerMensajeError(response) {

    const texto = await response.text();

    try {

        const errorData = JSON.parse(texto);

        return errorData.mensaje ||
                errorData.message ||
                errorData.error ||
                texto ||
                `Error HTTP ${response.status}`;

    } catch {

        return texto ||
               `Error HTTP ${response.status}`;
    }
}

async function crearHabitacion(hotelId, habitacion) {

    const response = await fetch(
        `${API_URL}/habitaciones/hotel/${hotelId}`,
        {

            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(habitacion)

        }
    );

    if (!response.ok) {

        throw new Error(
            `Error HTTP ${response.status}: ${response.statusText}`
        );

    }

    return await response.json();
}

async function obtenerHabitacion(id) {
    return await getJson(`${API_URL}/habitaciones/${id}`);
}

async function actualizarHabitacion(id, habitacion) {

    const response = await fetch(`${API_URL}/habitaciones/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(habitacion)
    });

    if (!response.ok) {

        const mensaje =
            await obtenerMensajeError(response);

        throw new Error(mensaje);
    }

    return await response.json();
}

async function eliminarHabitacion(id) {

    const response = await fetch(`${API_URL}/habitaciones/${id}`, {
        method: "DELETE"
    });

    if (!response.ok) {
        throw new Error(
            `Error HTTP ${response.status}: ${response.statusText}`
        );
    }
}

// =========================================================
// RESERVAS
// =========================================================

async function obtenerReservas() {

    return await getJson(`${API_URL}/reservas`);
}

async function crearReserva(reserva) {

    const response = await fetch(`${API_URL}/reservas`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(reserva)
    });

    if (!response.ok) {

        throw new Error(
            `Error HTTP ${response.status}: ${response.statusText}`
        );
    }

    return await response.json();
}

async function registrarPagoReserva(reservaId, importe) {

    const response = await fetch(
        `${API_URL}/reservas/${reservaId}/pagos`,
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                importe: importe
            })
        }
    );

    if (!response.ok) {
        throw new Error(
            `Error HTTP ${response.status}: ${response.statusText}`
        );
    }

    return await response.json();
}

// =========================================================
// CLIENTES
// =========================================================

async function obtenerClientes() {

    return await getJson(`${API_URL}/clientes`);
}

async function crearCliente(cliente) {

    const response = await fetch(`${API_URL}/clientes`,
        {

            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(cliente)

        }
    );

    if (!response.ok) {

        throw new Error(
            `Error HTTP ${response.status}: ${response.statusText}`
        );

    }

    return await response.json();
}

