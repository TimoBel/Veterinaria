# Vet System

Sistema de gestión para la Clínica Veterinaria Patitas Felices.

## Alumno
- Timoteo Beltrán

## Requisitos

- JDK 21 instalado y la variable `JAVA_HOME` configurada.
- MySQL en ejecución en `localhost:3306`.
- Conexión a Internet para descargar Maven y las dependencias la primera vez.

El proyecto incluye Maven Wrapper, por lo que no es necesario instalar Maven por separado.

## Cómo ejecutar el proyecto

Primero hay que tener MySQL encendido, crear la base `vet_system` y poner el usuario y contraseña en `src/main/resources/application.properties`.

Después, desde una terminal en la carpeta `Veterinaria`, ejecutar:

```powershell
.\mvnw.cmd spring-boot:run
```

Las tablas se crean automáticamente al iniciar. Para probar los endpoints, entrar a swagger (http://localhost:8080/swagger-ui/index.html).


## Parcial 1 — Decisiones de diseño

### Relación Turno–Medicamento
Decidí utilizar la relacion muchos a muchos unidereccional desde Turno hacia Medicamento.
Un turno puede tener varios medicamentos recetados.
Un medicamento puede recetarse en varios turnos.
Use una tabla intermedia turno_medicamento, que guarda los identificadores del turno y del medicamento. Así puedo reutilizar un medicamento del catálogo sin duplicar sus datos para cada turno. 
es unidereccional ya que las solicitudes necesitarian consultar los medicamentos que tiene un turno, pero no es necesario consultar los turnos que tienen un cierto medicamento.

Con respecto a spring.jpa.hibernate.ddl-auto=update, ya lo tenia en mi proyecto y me parece apropiado ya que Hibernate actualiza el esquema a partir de las entidades al iniciar la aplicación, lo cual me parece sencillo y practico.

### Validación de stock
implementé la validación de stock en TurnoService, dentro del método asociarMedicamento, porque es una regla de negocio. Primero busco el turno y el medicamento, y si alguno no existe salta con al exception: ResourceNotFoundException
Antes de asociarlos compruebo que el stock del medicamento sea mayor que cero. Si no hay stock disponible usa la exception: StockInsuficienteException y devuelve un error 422
Si hay stock, asocio el medicamento al turno y disminuyo el stock en 1.

### Solapamiento
Para el solapamiento de turnos, implementé la validación en TurnoService, dentro del método crearTurno, ya que es una regla de negocio.
Antes de guardar un turno, busco si el veterinario ya tiene otro en la misma fecha y hora. Para eso uso la consulta findFirstByVeterinarioIdAndFechaAndHora, que compara el ID del veterinario, la fecha y la hora solicitados. Si encuentra una coincidencia, se lanza TurnoSuperpuestoException y no se guarda el nuevo turno

### Cupo de mascotas
Mi entidad mascota no tiene un campo "activo", y no lo considero necesario para el control de cupo de mascotas, simplemente decidí que cualquier mascota realacionada a un duenio es considerada activa, de esta manera, evito tener que agregar un campo adicional modificando la entidad y la base de datos, y simplifico la lógica de negocio.
Para controlar el cupo, uso la consulta countByDuenioId, que cuenta cuántas mascotas tiene el dueño indicado. La validación está en MascotaService en el metodo "createMascota"
Si el dueño ya tiene cinco mascotas o más se lanza CupoMascotasException con un error 422


### Decisión más difícil
Lo que más me costó fue recordar cómo crear y manejar las excepciones personalizadas. Tuve que repasar cómo lanzar una excepción desde el service y cómo capturarla en el GlobalExceptionHandler para devolver el código HTTP correspondiente. Seguí el patrón de las excepciones que ya tenía en el proyecto para mantener una estructura similar, lo cual me ayudó a separar las validaciones de negocio de la construcción de las respuestas de error.
Otro desafío fue organizar el tiempo para completar el trabajo en menos de dos horas. Generalmente algo asi me suele llevar un poco mas de tiempo, pero pude organizarme y completarlo a tiempo, sacrificando algun que otro detalle y no pudiendo probar todas las funcionalidades como me hubiera gustado.
