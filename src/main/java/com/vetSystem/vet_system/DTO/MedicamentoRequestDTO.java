package com.vetSystem.vet_system.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Datos necesarios para registrar o actualizar un medicamento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicamentoRequestDTO {

    @Schema(description = "Nombre del medicamento", example = "Ibuprofeno 500 mg")
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Schema(description = "Principio activo del medicamento", example = "Ibuprofeno")
    @NotBlank(message = "El principio activo es obligatorio")
    private String principioActivo;

    @Schema(description = "Cantidad disponible del medicamento", example = "20")
    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    @Schema(description = "Precio por unidad del medicamento", example = "1500.50")
    @NotNull(message = "El precio unitario es obligatorio")
    @PositiveOrZero(message = "El precio unitario no puede ser negativo")
    private Double precioUnitario;
}
