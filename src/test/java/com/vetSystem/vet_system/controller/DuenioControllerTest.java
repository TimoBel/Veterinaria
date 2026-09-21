package com.vetSystem.vet_system.controller;

import com.vetSystem.vet_system.Controller.DuenioController;
import com.vetSystem.vet_system.DTO.DuenioDTO;
import com.vetSystem.vet_system.Exception.ResourceNotFoundException;
import com.vetSystem.vet_system.Service.DuenioService;
import com.vetSystem.vet_system.Service.MascotaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DuenioController.class)
class DuenioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DuenioService duenioService;

    @MockitoBean
    private MascotaService mascotaService;

    @Test
    void listarTodos_cuandoNoHayDuenios_retorna200YListaVacia() throws Exception {
        // DADO
        when(duenioService.getAllDuenios()).thenReturn(List.of());

        // CUANDO / ENTONCES
        mockMvc.perform(get("/api/duenios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void buscarPorId_cuandoExiste_retorna200YDueno() throws Exception {
        // DADO
        when(duenioService.getDuenioById(1L)).thenReturn(crearDuenioDTO());

        // CUANDO / ENTONCES
        mockMvc.perform(get("/api/duenios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    void buscarPorId_cuandoNoExiste_retorna404() throws Exception {
        // DADO
        when(duenioService.getDuenioById(99L))
                .thenThrow(new ResourceNotFoundException("Duenio", 99L));

        // CUANDO / ENTONCES
        mockMvc.perform(get("/api/duenios/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void registrarDuenio_conBodyValido_retorna201() throws Exception {
        // DADO
        DuenioDTO duenioCreado = crearDuenioDTO();
        duenioCreado.setId(1L);
        when(duenioService.createDuenio(any(DuenioDTO.class))).thenReturn(duenioCreado);

        // CUANDO / ENTONCES
        mockMvc.perform(post("/api/duenios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDuenioValido()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    void registrarDuenio_conEmailVacio_retorna400() throws Exception {
        // DADO
        String duenioInvalido = jsonDuenioConEmailVacio();

        // CUANDO / ENTONCES
        mockMvc.perform(post("/api/duenios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(duenioInvalido))
                .andExpect(status().isBadRequest());
    }

    private DuenioDTO crearDuenioDTO() {
        DuenioDTO duenioDTO = new DuenioDTO();
        duenioDTO.setNombre("Juan");
        duenioDTO.setApellido("Perez");
        duenioDTO.setDni("12345678");
        duenioDTO.setTelefono("3454051512");
        duenioDTO.setEmail("juan@gmail.com");
        return duenioDTO;
    }

    private String jsonDuenioValido() {
        return """
                {
                  "nombre": "Juan",
                  "apellido": "Perez",
                  "dni": "12345678",
                  "telefono": "3454051512",
                  "email": "juan@gmail.com"
                }
                """;
    }

    private String jsonDuenioConEmailVacio() {
        return """
                {
                  "nombre": "Juan",
                  "apellido": "Perez",
                  "dni": "12345678",
                  "telefono": "3454051512",
                  "email": ""
                }
                """;
    }
}
