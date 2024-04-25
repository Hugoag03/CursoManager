package com.tcna.gestioncursos.controller; // Define el paquete del controlador

import com.tcna.gestioncursos.entity.Curso; // Importa la clase Curso del paquete entity
import com.tcna.gestioncursos.reports.CursoExporterExcel; // Importa la clase CursoExporterExcel del paquete reports
import com.tcna.gestioncursos.reports.CursoExporterPDF; // Importa la clase CursoExporterPDF del paquete reports
import com.tcna.gestioncursos.repository.CursoRepository; // Importa la interfaz CursoRepository del paquete repository
import jakarta.servlet.http.HttpServletResponse; // Importa la clase HttpServletResponse de jakarta.servlet.http
import org.springframework.beans.factory.annotation.Autowired; // Importa la anotación @Autowired de Spring
import org.springframework.data.domain.Page; // Importa la clase Page de Spring Data
import org.springframework.data.domain.PageRequest; // Importa la clase PageRequest de Spring Data
import org.springframework.data.domain.Pageable; // Importa la interfaz Pageable de Spring Data
import org.springframework.data.repository.query.Param; // Importa la anotación @Param de Spring Data
import org.springframework.stereotype.Controller; // Importa la anotación @Controller de Spring
import org.springframework.ui.Model; // Importa la clase Model de Spring
import org.springframework.web.bind.annotation.GetMapping; // Importa la anotación @GetMapping de Spring
import org.springframework.web.bind.annotation.PathVariable; // Importa la anotación @PathVariable de Spring
import org.springframework.web.bind.annotation.PostMapping; // Importa la anotación @PostMapping de Spring
import org.springframework.web.bind.annotation.RequestParam; // Importa la anotación @RequestParam de Spring
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // Importa la clase RedirectAttributes de Spring

import java.io.IOException; // Importa la excepción IOException de Java
import java.text.DateFormat; // Importa la clase DateFormat de Java
import java.text.SimpleDateFormat; // Importa la clase SimpleDateFormat de Java
import java.util.ArrayList; // Importa la clase ArrayList de Java
import java.util.Date; // Importa la clase Date de Java
import java.util.List; // Importa la clase List de Java

@Controller // Anota la clase como un controlador de Spring
public class CursoController { // Define la clase CursoController

    @Autowired // Indica a Spring que inyecte automáticamente una instancia de CursoRepository
    private CursoRepository cursoRepository; // Declara una instancia de CursoRepository

    @GetMapping // Mapea la ruta raíz del servidor a este método
    public String home() { // Define el método home
        return "redirect:/cursos"; // Redirige a la ruta "/cursos"
    }

    @GetMapping("/cursos") // Mapea la ruta "/cursos" a este método con el verbo GET
    public String listarCursos(Model model, @Param("keyword") String keyword, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "3") int size) { // Define el método listarCursos que recibe un objeto Model, una palabra clave, una página y un tamaño como parámetros
        try { // Inicia un bloque try-catch para manejar excepciones
            List<Curso> cursos = new ArrayList<>(); // Crea una nueva lista de cursos
            Pageable paging = PageRequest.of(page - 1, size); // Crea un objeto Pageable para la paginación

            Page<Curso> pageCursos = null; // Declara una página de cursos inicialmente como nula

            if (keyword == null) { // Comprueba si la palabra clave es nula
                pageCursos = cursoRepository.findAll(paging); // Obtiene todos los cursos sin filtrar por palabra clave
            } else { // Si la palabra clave no es nula
                pageCursos = cursoRepository.findByTituloContainingIgnoreCase(keyword, paging); // Obtiene los cursos que contienen la palabra clave ignorando mayúsculas y minúsculas
                model.addAttribute("keyword", keyword); // Agrega la palabra clave al modelo
            }

            cursos = pageCursos.getContent(); // Obtiene la lista de cursos de la página actual

            model.addAttribute("cursos", cursos); // Agrega la lista de cursos al modelo

            model.addAttribute("currentPage", pageCursos.getNumber() + 1); // Agrega el número de la página actual al modelo
            model.addAttribute("totalItems", pageCursos.getTotalElements()); // Agrega el número total de elementos al modelo
            model.addAttribute("totalPages", pageCursos.getTotalPages()); // Agrega el número total de páginas al modelo
            model.addAttribute("pageSize", size); // Agrega el tamaño de página al modelo
        } catch (Exception e) { // Captura cualquier excepción
            model.addAttribute("message", e.getMessage()); // Agrega el mensaje de error al modelo
        }

        return "cursos"; // Devuelve la vista "cursos"
    }

    @GetMapping("/cursos/nuevo") // Mapea la ruta "/cursos/nuevo" a este método con el verbo GET
    public String agregarCurso(Model model) { // Define el método agregarCurso que recibe un objeto Model como parámetro

        Curso curso = new Curso(); // Crea una nueva instancia de Curso
        curso.setPublicado(true); // Establece el atributo "publicado" de curso como true
        model.addAttribute("curso", curso); // Agrega el curso al modelo con el nombre "curso"
        model.addAttribute("pageTitle", "Nuevo curso"); // Agrega el título de la página al modelo con el nombre "pageTitle"
        return "curso_form"; // Devuelve la vista "curso_form"
    }

    @PostMapping("/cursos/save") // Mapea la ruta "/cursos/save" a este método con el verbo POST
    public String guardarCurso(Curso curso, RedirectAttributes redirectAttributes) { // Define el método guardarCurso que recibe un objeto Curso y RedirectAttributes como parámetros
        try { // Inicia un bloque try-catch para manejar excepciones
            cursoRepository.save(curso); // Guarda el curso en el repositorio
            redirectAttributes.addFlashAttribute("message", "El curso ha sido guardado con éxito"); // Agrega un mensaje al atributo flash para mostrar en la vista siguiente
        } catch (Exception e) { // Captura cualquier excepción
            redirectAttributes.addFlashAttribute("message", e.getMessage()); // Agrega el mensaje de error al atributo flash para mostrar en la vista siguiente
        }
        return "redirect:/cursos"; // Redirige a la ruta "/cursos"
    }

    @GetMapping("/cursos/{id}") // Mapea la ruta "/cursos/{id}" a este método con el verbo GET
    public String editarCurso(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) { // Define el método editarCurso que recibe un ID de curso, un objeto Model y RedirectAttributes como parámetros
        try { // Inicia un bloque try-catch para manejar excepciones
            Curso curso = cursoRepository.findById(id).get(); // Obtiene el curso con el ID proporcionado
            model.addAttribute("pageTitle", "Editar curso: " + id); // Agrega el título de la página al modelo con el nombre "pageTitle"
            model.addAttribute("curso", curso); // Agrega el curso al modelo con el nombre "curso"

            return "curso_form"; // Devuelve la vista "curso_form"
        } catch (Exception e) { // Captura cualquier excepción
            redirectAttributes.addFlashAttribute("message", e.getMessage()); // Agrega el mensaje de error al atributo flash para mostrar en la vista siguiente
        }
        return "redirect:/cursos"; // Redirige a la ruta "/cursos"
    }

    @GetMapping("/cursos/delete/{id}") // Mapea la ruta "/cursos/delete/{id}" a este método con el verbo GET
    public String eliminarCurso(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) { // Define el método eliminarCurso que recibe un ID de curso, un objeto Model y RedirectAttributes como parámetros
        try { // Inicia un bloque try-catch para manejar excepciones
            cursoRepository.deleteById(id); // Elimina el curso con el ID proporcionado
            redirectAttributes.addFlashAttribute("message", "El curso ha sido eliminado con éxito"); // Agrega un mensaje al atributo flash para mostrar en la vista siguiente

        } catch (Exception e) { // Captura cualquier excepción
            redirectAttributes.addFlashAttribute("message", e.getMessage()); // Agrega el mensaje de error al atributo flash para mostrar en la vista siguiente
        }
        return "redirect:/cursos"; // Redirige a la ruta "/cursos"
    }

    @GetMapping("/export/pdf") // Mapea la ruta "/export/pdf" a este método con el verbo GET
    public void generarReportePdf(HttpServletResponse response) throws IOException { // Define el método generarReportePdf que recibe un objeto HttpServletResponse y puede lanzar IOException
        response.setContentType("application/pdf"); // Establece el tipo de contenido de la respuesta como PDF
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss"); // Define el formato de fecha y hora
        String currentDateTime = dateFormat.format(new Date()); // Obtiene la fecha y hora actual formateada

        String headerKey = "Content-Disposition"; // Define la clave del encabezado
        String headerValue = "attachment; filename = cursos" + currentDateTime + ".pdf"; // Define el valor del encabezado con el nombre del archivo PDF
        response.setHeader(headerKey, headerValue); // Establece el encabezado de la respuesta

        List<Curso> cursos = cursoRepository.findAll(); // Obtiene todos los cursos desde el repositorio

        CursoExporterPDF exporterPDF = new CursoExporterPDF(cursos); // Crea un objeto exportador PDF de cursos
        exporterPDF.export(response); // Exporta los cursos como un archivo PDF
    }

    @GetMapping("/export/excel") // Mapea la ruta "/export/excel" a este método con el verbo GET
    public void generarReporteExcel(HttpServletResponse response) throws IOException { // Define el método generarReporteExcel que recibe un objeto HttpServletResponse y puede lanzar IOException
        response.setContentType("application/octet-stream"); // Establece el tipo de contenido de la respuesta como octeto-stream
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss"); // Define el formato de fecha y hora
        String currentDateTime = dateFormat.format(new Date()); // Obtiene la fecha y hora actual formateada

        String headerKey = "Content-Disposition"; // Define la clave del encabezado
        String headerValue = "attachment; filename = cursos" + currentDateTime + ".xlsx"; // Define el valor del encabezado con el nombre del archivo Excel
        response.setHeader(headerKey, headerValue); // Establece el encabezado de la respuesta

        List<Curso> cursos = cursoRepository.findAll(); // Obtiene todos los cursos desde el repositorio

        CursoExporterExcel exporterExcel = new CursoExporterExcel(cursos); // Crea un objeto exportador Excel de cursos
        exporterExcel.export(response); // Exporta los cursos como un archivo Excel
    }
}
