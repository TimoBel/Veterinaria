package com.vetSystem.vet_system.service;

import com.vetSystem.vet_system.DTO.TurnoRequestDTO;
import com.vetSystem.vet_system.DTO.TurnoResponseDTO;
import com.vetSystem.vet_system.Entity.Mascota;
import com.vetSystem.vet_system.Entity.Turno;
import com.vetSystem.vet_system.Entity.Veterinario;
import com.vetSystem.vet_system.Exception.TurnoSuperpuestoException;
import com.vetSystem.vet_system.Mapper.TurnoMapper;
import com.vetSystem.vet_system.Repository.MascotaRepository;
import com.vetSystem.vet_system.Repository.TurnoRepository;
import com.vetSystem.vet_system.Repository.VeterinarioRepository;
import com.vetSystem.vet_system.Service.TurnoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TurnoServiceTest {

    @Mock
    private TurnoRepository turnoRepository;

    @Mock
    private MascotaRepository mascotaRepository;

    @Mock
    private VeterinarioRepository veterinarioRepository;

    @Mock
    private TurnoMapper turnoMapper;

    @InjectMocks
    private TurnoService turnoService;

    @Test
    void crearTurno_cuandoNoHaySuperposicion_guardaYRetornaDTO() {
        // DADO
        TurnoRequestDTO request = crearRequest();
        Mascota mascota = crearMascota();
        Veterinario veterinario = crearVeterinario();
        Turno turnoGuardado = new Turno();
        TurnoResponseDTO respuesta = new TurnoResponseDTO();
        respuesta.setId(3L);

        when(mascotaRepository.findById(request.getMascotaId())).thenReturn(Optional.of(mascota));
        when(veterinarioRepository.findById(request.getVeterinarioId())).thenReturn(Optional.of(veterinario));
        when(turnoRepository.existsByVeterinarioIdAndFechaAndHora(
                request.getVeterinarioId(), request.getFecha(), request.getHora())).thenReturn(false);
        when(turnoRepository.save(any(Turno.class))).thenReturn(turnoGuardado);
        when(turnoMapper.toDto(turnoGuardado)).thenReturn(respuesta);

        // CUANDO
        TurnoResponseDTO resultado = turnoService.crearTurno(request);

        // ENTONCES
        assertThat(resultado).isEqualTo(respuesta);
        verify(turnoRepository, times(1)).save(any(Turno.class));
    }

    @Test
    void crearTurno_cuandoHaySuperposicion_lanzaExcepcionYNoGuarda() {
        // DADO
        TurnoRequestDTO request = crearRequest();
        when(mascotaRepository.findById(request.getMascotaId())).thenReturn(Optional.of(crearMascota()));
        when(veterinarioRepository.findById(request.getVeterinarioId())).thenReturn(Optional.of(crearVeterinario()));
        when(turnoRepository.existsByVeterinarioIdAndFechaAndHora(
                request.getVeterinarioId(), request.getFecha(), request.getHora())).thenReturn(true);

        // CUANDO
        TurnoSuperpuestoException excepcion = assertThrows(
                TurnoSuperpuestoException.class,
                () -> turnoService.crearTurno(request)
        );

        // ENTONCES
        assertThat(excepcion).hasMessage("El veterinario ya tiene un turno en ese horario");
        verify(turnoRepository, never()).save(any(Turno.class));
    }

    private TurnoRequestDTO crearRequest() {
        TurnoRequestDTO request = new TurnoRequestDTO();
        request.setFecha(LocalDate.of(2026, 9, 20));
        request.setHora(LocalTime.of(10, 0));
        request.setMotivo("Control anual");
        request.setObservaciones("Sin observaciones");
        request.setMascotaId(1L);
        request.setVeterinarioId(2L);
        return request;
    }

    private Mascota crearMascota() {
        Mascota mascota = new Mascota();
        mascota.setId(1L);
        mascota.setNombre("Luna");
        return mascota;
    }

    private Veterinario crearVeterinario() {
        Veterinario veterinario = new Veterinario();
        veterinario.setId(2L);
        veterinario.setNombre("Ana");
        return veterinario;
    }
}
