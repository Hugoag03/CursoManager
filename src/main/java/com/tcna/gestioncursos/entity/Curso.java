package com.tcna.gestioncursos.entity; // Paquete que contiene la clase

import jakarta.persistence.*; // Importación de las anotaciones de JPA
import lombok.AllArgsConstructor; // Importación de la anotación lombok para constructor con todos los argumentos
import lombok.Data; // Importación de la anotación lombok para la generación de métodos getter, setter, equals, hashCode y toString
import lombok.NoArgsConstructor; // Importación de la anotación lombok para constructor sin argumentos
import lombok.ToString; // Importación de la anotación lombok para generar el método toString

@Entity // Anotación que indica que la clase es una entidad
@Table(name = "cursos") // Anotación que especifica el nombre de la tabla en la base de datos
@AllArgsConstructor // Anotación lombok para generar un constructor con todos los argumentos
@NoArgsConstructor // Anotación lombok para generar un constructor sin argumentos
@Data // Anotación lombok que combina @ToString, @EqualsAndHashCode, @Getter, @Setter y @RequiredArgsConstructor
@ToString // Anotación lombok para generar el método toString
public class Curso { // Declaración de la clase Curso

    @Id // Anotación que indica que el atributo es una clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Anotación que especifica la estrategia de generación de valores para la clave primaria
    private Integer id; // Atributo para el identificador del curso

    @Column(length = 128, nullable = false) // Anotación que especifica las propiedades de la columna en la base de datos
    private String titulo; // Atributo para el título del curso

    @Column(length = 256) // Anotación que especifica las propiedades de la columna en la base de datos
    private String descripcion; // Atributo para la descripción del curso

    @Column(nullable = false) // Anotación que especifica las propiedades de la columna en la base de datos
    private int nivel; // Atributo para el nivel del curso

    @Column(name = "estado_publicacion") // Anotación que especifica las propiedades de la columna en la base de datos
    private boolean isPublicado; // Atributo para el estado de publicación del curso
}
