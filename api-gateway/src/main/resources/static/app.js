"use strict";

// Los nombres de los campos coinciden con los DTO de cada microservicio.
const sections = {
    duenos: {
        title: "Dueños", singular: "dueño", dependencies: [],
        fields: [
            { name: "nombre", label: "Nombre" },
            { name: "apellido", label: "Apellido" },
            { name: "cedula", label: "Cédula" },
            { name: "telefono", label: "Teléfono", type: "number", max: 2147483647 },
            { name: "email", label: "Email", type: "email" }
        ]
    },
    mascotas: {
        title: "Mascotas", singular: "mascota", dependencies: ["duenos"],
        fields: [
            { name: "nombre", label: "Nombre" },
            { name: "especie", label: "Especie", placeholder: "Perro, gato..." },
            { name: "raza", label: "Raza", optional: true },
            { name: "fechaNacimiento", label: "Fecha de nacimiento", type: "date" },
            { name: "duenoId", label: "Dueño", reference: "duenos" }
        ]
    },
    veterinarios: {
        title: "Veterinarios", singular: "veterinario", dependencies: [],
        fields: [
            { name: "nombre", label: "Nombre" },
            { name: "apellido", label: "Apellido" },
            { name: "matricula", label: "Matrícula", pattern: "MV-[0-9]+", placeholder: "MV-1234", hint: "Formato: MV- seguido de números." },
            { name: "especialidad", label: "Especialidad" },
            { name: "email", label: "Email", type: "email" }
        ]
    },
    turnos: {
        title: "Turnos", singular: "turno", dependencies: ["mascotas", "veterinarios"],
        fields: [
            { name: "fechaHora", label: "Fecha y hora", type: "datetime-local" },
            { name: "mascotaId", label: "Mascota", reference: "mascotas" },
            { name: "veterinarioId", label: "Veterinario", reference: "veterinarios" },
            { name: "motivo", label: "Motivo" }
        ]
    }
};

const form = document.querySelector("#record-form");
const fieldsContainer = document.querySelector("#form-fields");
const tableBody = document.querySelector("#table-body");
const message = document.querySelector("#message");
const saveButton = document.querySelector("#save");
const state = { section: "duenos", records: [], references: {}, editingId: null, busy: false, loaded: false, pendingAction: null };

function element(tag, text, className) {
    const node = document.createElement(tag);
    if (text !== undefined) node.textContent = text;
    if (className) node.className = className;
    return node;
}

function notify(text, type = "danger") {
    message.textContent = text;
    message.className = `alert alert-${type}`;
}

function clearMessage() {
    message.textContent = "";
    message.className = "alert d-none";
    state.pendingAction = null;
    document.querySelector("#confirmation").classList.add("d-none");
}

async function request(path, method = "GET", data) {
    let response;
    try {
        response = await fetch(`/api/${path}`, {
            method,
            headers: { Accept: "application/json", ...(data === undefined ? {} : { "Content-Type": "application/json" }) },
            body: data === undefined ? undefined : JSON.stringify(data)
        });
    } catch {
        throw new Error("No se pudo conectar con la aplicación. Volvé a intentarlo.");
    }
    if (response.status === 204) return null;
    const text = await response.text();
    let body;
    try { body = text ? JSON.parse(text) : null; } catch { body = null; }
    if (!response.ok) {
        const errors = body?.errores;
        const details = errors && typeof errors === "object" ? Object.values(errors).join(". ") : "";
        throw new Error(details || body?.mensaje || body?.detail || body?.message || `No se pudo completar la operación (error ${response.status}).`);
    }
    return body;
}

function localDate(date = new Date()) {
    return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, "0")}-${String(date.getDate()).padStart(2, "0")}`;
}

function recordName(record) {
    return [record.nombre, record.apellido].filter(Boolean).join(" ");
}

function missingDependencies() {
    return sections[state.section].dependencies.filter(key => !state.references[key]?.length);
}

function setBusy(busy) {
    state.busy = busy;
    document.querySelectorAll("#navigation button, #refresh, #reset-form, #table-body button, #confirmation button").forEach(button => {
        button.disabled = busy;
    });
    const formDisabled = busy || !state.loaded || missingDependencies().length > 0;
    fieldsContainer.disabled = formDisabled;
    saveButton.disabled = formDisabled;
    saveButton.textContent = busy ? "Esperá..." : "Guardar";
}

function renderForm(record = null) {
    const config = sections[state.section];
    state.editingId = record?.id ?? null;
    const newLabel = state.section === "mascotas" ? "Nueva" : "Nuevo";
    document.querySelector("#form-title").textContent = record ? `Editar ${config.singular} #${record.id}` : `${newLabel} ${config.singular}`;
    document.querySelector("#reset-form").textContent = record ? "Cancelar edición" : "Limpiar";
    fieldsContainer.replaceChildren();

    for (const field of config.fields) {
        const group = element("div", undefined, "mb-3");
        const label = element("label", `${field.label}${field.optional ? "" : " *"}`, "form-label");
        label.htmlFor = field.name;
        let input;
        if (field.reference) {
            input = element("select", undefined, "form-select");
            input.append(new Option(`Seleccioná ${field.label.toLowerCase()}`, ""));
            for (const item of state.references[field.reference] || []) {
                input.append(new Option(`${recordName(item)} (#${item.id})`, String(item.id)));
            }
            if (record && record[field.name] != null && !Array.from(input.options).some(option => option.value === String(record[field.name]))) {
                input.append(new Option(`#${record[field.name]} (no disponible)`, String(record[field.name])));
            }
        } else {
            input = element("input", undefined, "form-control");
            input.type = field.type || "text";
            if (["text", "email"].includes(input.type)) input.maxLength = 255;
            if (field.type === "number") { input.min = "1"; input.step = "1"; input.max = String(field.max); }
            if (field.type === "date") input.max = localDate();
            if (field.type === "datetime-local") {
                const nextMinute = new Date(Date.now() + 60000);
                input.min = `${localDate(nextMinute)}T${String(nextMinute.getHours()).padStart(2, "0")}:${String(nextMinute.getMinutes()).padStart(2, "0")}`;
            }
            if (field.pattern) { input.pattern = field.pattern; input.title = field.hint; }
            if (field.placeholder) input.placeholder = field.placeholder;
        }
        input.id = field.name;
        input.name = field.name;
        input.required = !field.optional;
        input.value = record?.[field.name] == null ? "" : String(record[field.name]);
        if (field.type === "datetime-local" && record) input.value = String(record[field.name]).slice(0, 16);
        group.append(label, input);
        if (field.hint) {
            const hint = element("div", field.hint, "form-text");
            hint.id = `${field.name}-help`;
            input.setAttribute("aria-describedby", hint.id);
            group.append(hint);
        }
        fieldsContainer.append(group);
    }
    const missing = missingDependencies();
    const help = document.querySelector("#form-help");
    help.textContent = missing.length ? `Primero registrá ${missing.map(key => sections[key].title.toLowerCase()).join(" y ")} desde el menú.` : "";
    help.classList.toggle("d-none", missing.length === 0);
    setBusy(state.busy);
}

function displayValue(record, field) {
    const value = record[field.name];
    if (value == null || value === "") return "—";
    if (field.reference) {
        const related = state.references[field.reference]?.find(item => String(item.id) === String(value));
        return related ? `${recordName(related)} (#${value})` : `#${value}`;
    }
    if (field.type === "date") return String(value).split("-").reverse().join("/");
    if (field.type === "datetime-local") return new Date(value).toLocaleString("es-AR", { dateStyle: "short", timeStyle: "short" });
    return String(value);
}

function renderTable(statusText) {
    const config = sections[state.section];
    const columns = [{ name: "id", label: "ID" }, ...config.fields];
    if (state.section === "turnos") columns.push({ name: "estado", label: "Estado" });
    const head = document.querySelector("#table-head");
    head.replaceChildren();
    for (const column of [...columns, { label: "Acciones" }]) {
        const th = element("th", column.label);
        th.scope = "col";
        head.append(th);
    }
    tableBody.replaceChildren();
    document.querySelector("#record-count").textContent = statusText ? "" : `${state.records.length} registro(s)`;
    if (statusText || state.records.length === 0) {
        const cell = element("td", statusText || "Todavía no hay registros. Usá el formulario para agregar uno.", "text-secondary text-center py-4");
        cell.colSpan = columns.length + 1;
        const row = element("tr");
        row.append(cell);
        tableBody.append(row);
        return;
    }
    for (const record of state.records) {
        const row = element("tr");
        for (const field of columns) row.append(element("td", displayValue(record, field)));
        const actions = element("td");
        const buttons = element("div", undefined, "d-flex flex-wrap gap-1");
        const choices = [["edit", "Editar", "outline-primary"], ["delete", "Eliminar", "outline-danger"]];
        if (state.section === "turnos" && record.estado !== "CANCELADO") choices.push(["cancel", "Cancelar turno", "outline-secondary"]);
        for (const [action, label, style] of choices) {
            const button = element("button", label, `btn btn-sm btn-${style}`);
            button.type = "button";
            button.dataset.action = action;
            button.dataset.id = String(record.id);
            button.setAttribute("aria-label", action === "cancel" ? `${label} #${record.id}` : `${label} ${config.singular} #${record.id}`);
            buttons.append(button);
        }
        actions.append(buttons);
        row.append(actions);
        tableBody.append(row);
    }
}

async function loadSection() {
    const config = sections[state.section];
    state.loaded = false;
    state.records = [];
    state.references = {};
    setBusy(true);
    document.querySelector("#section-title").textContent = config.title;
    document.querySelector("#table-caption").textContent = `Listado de ${config.title.toLowerCase()}`;
    renderTable("Cargando...");
    try {
        const keys = [state.section, ...config.dependencies];
        const results = await Promise.all(keys.map(key => request(key)));
        if (!results.every(Array.isArray)) throw new Error("No se pudieron leer los listados. Volvé a intentarlo.");
        state.records = results[0];
        config.dependencies.forEach((key, index) => { state.references[key] = results[index + 1]; });
        state.loaded = true;
        renderTable();
    } catch (error) {
        notify(error.message);
        renderTable("No se pudo cargar el listado. Probá con «Actualizar listado».");
    } finally {
        renderForm();
        setBusy(false);
    }
}

document.querySelector("#navigation").addEventListener("click", async event => {
    const button = event.target.closest("button[data-section]");
    if (!button || state.busy) return;
    state.section = button.dataset.section;
    document.querySelectorAll("#navigation button").forEach(item => {
        item.classList.toggle("active", item === button);
        if (item === button) item.setAttribute("aria-current", "page");
        else item.removeAttribute("aria-current");
    });
    clearMessage();
    await loadSection();
});

document.querySelector("#refresh").addEventListener("click", () => { clearMessage(); loadSection(); });
document.querySelector("#reset-form").addEventListener("click", () => { clearMessage(); renderForm(); });

form.addEventListener("submit", async event => {
    event.preventDefault();
    if (state.busy || !state.loaded || !form.reportValidity()) return;
    const data = {};
    for (const field of sections[state.section].fields) {
        const value = form.elements.namedItem(field.name).value.trim();
        data[field.name] = field.reference || field.type === "number" ? Number(value) : (value || null);
    }
    const editing = state.editingId !== null;
    const path = editing ? `${state.section}/${encodeURIComponent(state.editingId)}` : state.section;
    setBusy(true);
    clearMessage();
    try {
        await request(path, editing ? "PUT" : "POST", data);
    } catch (error) {
        notify(error.message);
        setBusy(false);
        return;
    }
    await loadSection();
    if (state.loaded) notify(editing ? "Cambios guardados." : "Registro creado.", "success");
});

tableBody.addEventListener("click", event => {
    const button = event.target.closest("button[data-action]");
    if (!button || state.busy) return;
    const record = state.records.find(item => String(item.id) === button.dataset.id);
    if (!record) return;
    clearMessage();
    if (button.dataset.action === "edit") {
        renderForm(record);
        fieldsContainer.querySelector("input, select").focus();
        return;
    }
    const cancel = button.dataset.action === "cancel";
    state.pendingAction = { section: state.section, id: record.id, cancel };
    document.querySelector("#confirmation-text").textContent = cancel ? `¿Cancelar el turno #${record.id}?` : `¿Eliminar ${sections[state.section].singular} #${record.id}? Esta acción no se puede deshacer.`;
    document.querySelector("#confirm-action").textContent = cancel ? "Sí, cancelar turno" : "Sí, eliminar";
    document.querySelector("#confirmation").classList.remove("d-none");
    document.querySelector("#confirm-action").focus();
});

document.querySelector("#dismiss-action").addEventListener("click", clearMessage);

document.querySelector("#confirm-action").addEventListener("click", async () => {
    const pending = state.pendingAction;
    if (!pending || state.busy) return;
    clearMessage();
    setBusy(true);
    try {
        await request(`${pending.section}/${encodeURIComponent(pending.id)}${pending.cancel ? "/cancelar" : ""}`, pending.cancel ? "PATCH" : "DELETE");
    } catch (error) {
        notify(error.message);
        setBusy(false);
        return;
    }
    await loadSection();
    if (state.loaded) notify(pending.cancel ? "Turno cancelado." : "Registro eliminado.", "success");
});

loadSection();
