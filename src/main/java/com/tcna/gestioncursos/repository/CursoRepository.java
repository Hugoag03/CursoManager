package com.tcna.gestioncursos.repository; // Paquete que contiene la interfaz

import com.tcna.gestioncursos.entity.Curso; // Importación de la clase Curso del paquete com.tcna.gestioncursos.entity
import org.springframework.data.domain.Page; // Importación de la clase Page del paquete org.springframework.data.domain
import org.springframework.data.domain.Pageable; // Importación de la clase Pageable del paquete org.springframework.data.domain
import org.springframework.data.jpa.repository.JpaRepository; // Importación de la interfaz JpaRepository del paquete org.springframework.data.jpa.repository
import org.springframework.stereotype.Repository; // Importación de la anotación Repository del paquete org.springframework.stereotype

@Repository // Anotación que indica que la clase es un repositorio
public interface CursoRepository extends JpaRepository<Curso, Integer> { // Declaración de la interfaz CursoRepository que extiende JpaRepository para la clase Curso y el tipo de su clave primaria (Integer)
    Page<Curso> findByTituloContainingIgnoreCase(String keyword, Pageable pageable); // Método que busca cursos por título que contiene la palabra clave (ignorando mayúsculas y minúsculas), con paginación
}
