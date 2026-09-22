package com.vetSystem.vet_system.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Datos de un medicamento registrado en la clínica veterinaria")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicamentoResponseDTO {

    @Schema(description = "Identificador único del medicamento", example = "1")
    private Long id;

    @Schema(description = "Nombre del medicamento", example = "Ibuprofeno 500 mg")
    private String nombre;

    @Schema(description = "Principio activo del medicamento", example = "Ibuprofeno")
    private String principioActivo;

    @Schema(description = "Cantidad disponible del medicamento", example = "20")
    private Integer stock;

    @Schema(description = "Precio por unidad del medicamento", example = "1500.50")
    private Double precioUnitario;
}
