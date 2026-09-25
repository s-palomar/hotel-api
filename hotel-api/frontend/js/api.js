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


// =========================================================
// HABITACIONES
// =========================================================

async function obtenerHabitaciones() {

    return await getJson(`${API_URL}/habitaciones`);
}


// =========================================================
// RESERVAS
// =========================================================

async function obtenerReservas() {

    return await getJson(`${API_URL}/reservas`);
}

// =========================================================
// CLIENTES
// =========================================================

async function obtenerClientes() {

    return await getJson(`${API_URL}/clientes`);
}

