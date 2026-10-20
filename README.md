# Patitas Felices

Proyecto educativo con cuatro microservicios. Cada uno tiene su propia base MySQL.

| Modulo | Puerto | Base | API |
| --- | --- | --- | --- |
| ms-duenio | 8081 | dueno_db | /api/duenos |
| ms-mascota | 8082 | mascota_db | /api/mascotas |
| ms-veterinario | 8083 | veterinario_db | /api/veterinarios |
| ms-turno | 8084 | turno_db | /api/turnos |

## Ejecutar

Se necesitan Java 21, Maven y MySQL activo en localhost:3306. Las cuatro bases deben existir. El usuario y la clave se configuran en el application.yaml de cada modulo.

Desde esta carpeta:

```shell
mvn clean package
```

Ejecutar cada modulo en una terminal distinta, por ejemplo:

```shell
mvn -pl ms-mascota spring-boot:run
```

Tambien se puede ejecutar su clase principal desde IntelliJ. Swagger esta en `http://localhost:PUERTO/swagger-ui/index.html`. Detener la instancia anterior antes de volver a iniciarla para liberar el puerto.

## Arquitectura: que hace cada parte

Los tres nuevos servicios repiten el mismo patron que duenio:

| Archivo o paquete | Responsabilidad |
| --- | --- |
| Ms...Application | Inicia Spring Boot. |
| domain/model | Datos de la entidad. Turno incluye el enum EstadoTurno. |
| domain/port/in/...Service | Interfaz con las operaciones que ofrece el servicio. |
| domain/port/out/...RepositoryPort | Interfaz para consultar y guardar datos. |
| application/...ServiceImpl | Ejecuta las operaciones y valida las reglas de negocio. |
| adapter/out/persistence/...Repository | Usa Spring Data JPA para acceder a MySQL. |
| adapter/in/web/...Controller | Recibe pedidos HTTP y llama al servicio. |
| dto/...Request y ...Response | Datos de entrada y salida de la API. |
| mapper/...Mapper | Convierte entre DTOs y entidad, con asignaciones de campos. |
| domain/exception | Excepciones para registro inexistente y conflicto de negocio. |
| ...ExceptionHandler | Convierte esos errores y los de validacion en respuestas HTTP. |

El controller usa la interfaz Service porque solo necesita conocer las operaciones disponibles. Spring le entrega la implementacion ServiceImpl. A su vez, ServiceImpl usa RepositoryPort; el adaptador JPA implementa ese contrato. Las dependencias apuntan hacia las interfaces del dominio.

Recorrido de un pedido: Controller -> ServiceImpl -> Repository -> MySQL.

## Operaciones y reglas

- CRUD basico: GET y POST sobre la ruta principal; GET, PUT y DELETE sobre /{id}.
- Mascotas: filtro `/api/mascotas?duenoId=1`. La fecha de nacimiento no puede ser futura y no se repite nombre para el mismo dueno.
- Veterinarios: filtro `/api/veterinarios?especialidad=Clinica`. Matricula con formato MV-1234, sin duplicados, y email valido.
- Turnos: consultas `/api/turnos/veterinario/{id}` y `/api/turnos/mascota/{id}`. Fecha futura y sin otro turno activo del mismo veterinario a esa hora.
- Un turno nuevo queda PENDIENTE. `PATCH /api/turnos/{id}/cancelar` lo marca CANCELADO y libera ese horario.

Los DTOs usan las validaciones vistas en la cursada. Las respuestas son 200 para consultas/actualizaciones, 201 para altas, 204 para bajas, 400 para datos invalidos, 404 para registros inexistentes y 409 para conflictos.

## Referencias entre servicios

Mascota guarda duenoId; Turno guarda mascotaId y veterinarioId. Son IDs Long y no relaciones @ManyToOne, porque las bases estan separadas. Por ahora se valida que sean positivos; consultar al otro servicio para comprobar su existencia corresponde a la etapa de comunicacion por API.
