package com.vetSystem.vet_system.service;

import com.vetSystem.vet_system.DTO.DuenioDTO;
import com.vetSystem.vet_system.Entity.Duenio;
import com.vetSystem.vet_system.Exception.DuplicateResourceException;
import com.vetSystem.vet_system.Exception.ResourceNotFoundException;
import com.vetSystem.vet_system.Mapper.DuenioMapper;
import com.vetSystem.vet_system.Repository.DuenioRepository;
import com.vetSystem.vet_system.Service.DuenioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DuenioServiceTest {

    @Mock
    private DuenioRepository duenioRepository;

    @Mock
    private DuenioMapper duenioMapper;

    @InjectMocks
    private DuenioService duenioService;

    @Test
    void getAllDuenios_cuandoListaVacia_retornaListaVacia() {
        // DADO
        when(duenioRepository.findAll()).thenReturn(List.of());

        // CUANDO
        List<DuenioDTO> resultado = duenioService.getAllDuenios();

        // ENTONCES
        assertThat(resultado).isEmpty();
        verify(duenioRepository).findAll();
        verifyNoInteractions(duenioMapper);
    }

    @Test
    void getAllDuenios_cuandoHayDatos_retornaDTOsMapeados() {
        // DADO
        Duenio duenio = crearDuenio();
        DuenioDTO duenioDTO = crearDuenioDTO();
        when(duenioRepository.findAll()).thenReturn(List.of(duenio));
        when(duenioMapper.toDto(duenio)).thenReturn(duenioDTO);

        // CUANDO
        List<DuenioDTO> resultado = duenioService.getAllDuenios();

        // ENTONCES
        assertThat(resultado)
                .hasSize(1)
                .first()
                .extracting(DuenioDTO::getNombre, DuenioDTO::getDni)
                .containsExactly("Juan", "12345678");
        verify(duenioRepository).findAll();
        verify(duenioMapper).toDto(duenio);
    }

    @Test
    void getDuenioById_cuandoExiste_retornaDTO() {
        // DADO
        Duenio duenio = crearDuenio();
        DuenioDTO duenioDTO = crearDuenioDTO();
        when(duenioRepository.findById(1L)).thenReturn(Optional.of(duenio));
        when(duenioMapper.toDto(duenio)).thenReturn(duenioDTO);

        // CUANDO
        DuenioDTO resultado = duenioService.getDuenioById(1L);

        // ENTONCES
        assertThat(resultado)
                .extracting(DuenioDTO::getId, DuenioDTO::getNombre)
                .containsExactly(1L, "Juan");
        verify(duenioRepository).findById(1L);
        verify(duenioMapper).toDto(duenio);
    }

    @Test
    void getDuenioById_cuandoNoExiste_lanzaResourceNotFoundException() {
        // DADO
        when(duenioRepository.findById(99L)).thenReturn(Optional.empty());

        // CUANDO
        ResourceNotFoundException excepcion = assertThrows(
                ResourceNotFoundException.class,
                () -> duenioService.getDuenioById(99L)
        );

        // ENTONCES
        assertThat(excepcion).hasMessage("Duenio con id 99 no fue encontrado");
        verify(duenioRepository).findById(99L);
        verifyNoInteractions(duenioMapper);
    }

    @Test
    void createDuenio_cuandoDniNoExiste_guardaYRetornaDTO() {
        // DADO
        DuenioDTO dtoEntrada = crearDuenioDTO();
        Duenio entidad = crearDuenio();
        entidad.setId(50L);
        Duenio entidadGuardada = crearDuenio();
        entidadGuardada.setId(2L);
        DuenioDTO dtoEsperado = crearDuenioDTO();
        dtoEsperado.setId(2L);
        when(duenioRepository.existsByDni(dtoEntrada.getDni())).thenReturn(false);
        when(duenioMapper.toEntity(dtoEntrada)).thenReturn(entidad);
        when(duenioRepository.save(entidad)).thenReturn(entidadGuardada);
        when(duenioMapper.toDto(entidadGuardada)).thenReturn(dtoEsperado);

        // CUANDO
        DuenioDTO resultado = duenioService.createDuenio(dtoEntrada);

        // ENTONCES
        assertThat(resultado).isEqualTo(dtoEsperado);
        assertThat(entidad.getId()).isNull();
        verify(duenioRepository).existsByDni(dtoEntrada.getDni());
        verify(duenioMapper).toEntity(dtoEntrada);
        verify(duenioRepository).save(entidad);
        verify(duenioMapper).toDto(entidadGuardada);
    }

    @Test
    void createDuenio_cuandoDniDuplicado_lanzaDuplicateResourceException() {
        // DADO
        DuenioDTO dtoEntrada = crearDuenioDTO();
        when(duenioRepository.existsByDni(dtoEntrada.getDni())).thenReturn(true);

        // CUANDO
        DuplicateResourceException excepcion = assertThrows(
                DuplicateResourceException.class,
                () -> duenioService.createDuenio(dtoEntrada)
        );

        // ENTONCES
        assertThat(excepcion).hasMessage("Ya existe un dueño con DNI: 12345678");
        verify(duenioRepository).existsByDni(dtoEntrada.getDni());
        verify(duenioRepository, never()).save(any(Duenio.class));
        verifyNoInteractions(duenioMapper);
    }

    private Duenio crearDuenio() {
        Duenio duenio = new Duenio();
        duenio.setId(1L);
        duenio.setNombre("Juan");
        duenio.setApellido("Perez");
        duenio.setDni("12345678");
        duenio.setEmail("juan@gmail.com");
        duenio.setTelefono("3454051512");
        return duenio;
    }

    private DuenioDTO crearDuenioDTO() {
        DuenioDTO duenioDTO = new DuenioDTO();
        duenioDTO.setId(1L);
        duenioDTO.setNombre("Juan");
        duenioDTO.setApellido("Perez");
        duenioDTO.setDni("12345678");
        duenioDTO.setEmail("juan@gmail.com");
        duenioDTO.setTelefono("3454051512");
        return duenioDTO;
    }
}
