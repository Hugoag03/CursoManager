package com.tcna.gestioncursos.reports; // Paquete que contiene la clase

import com.lowagie.text.Font; // Importación de la clase Font del paquete com.lowagie.text
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell; // Importación de la clase PdfPCell del paquete com.lowagie.text.pdf
import com.lowagie.text.pdf.PdfPTable; // Importación de la clase PdfPTable del paquete com.lowagie.text.pdf
import com.lowagie.text.pdf.PdfWriter; // Importación de la clase PdfWriter del paquete com.lowagie.text.pdf
import com.tcna.gestioncursos.entity.Curso; // Importación de la clase Curso del paquete com.tcna.gestioncursos.entity
import jakarta.servlet.http.HttpServletResponse; // Importación de la clase HttpServletResponse del paquete jakarta.servlet.http

import java.awt.*; // Importación del paquete java.awt
import java.io.IOException; // Importación de la clase IOException del paquete java.io
import java.util.List; // Importación de la clase List del paquete java.util

public class CursoExporterPDF { // Declaración de la clase CursoExporterPDF

    private List<Curso> listaCursos; // Declaración de la lista listaCursos de tipo Curso

    public CursoExporterPDF(List<Curso> listaCursos) { // Declaración del constructor de la clase con un parámetro de tipo Lista de Cursos
        this.listaCursos = listaCursos; // Inicialización de la lista listaCursos con el valor pasado como parámetro
    }

    private void writeTableHeader(PdfPTable table) { // Declaración del método writeTableHeader con un parámetro de tipo PdfPTable
        PdfPCell cell = new PdfPCell(); // Declaración de la celda
        cell.setBackgroundColor(Color.BLUE); // Establecimiento del color de fondo de la celda
        cell.setPadding(5); // Establecimiento del relleno de la celda

        Font font = FontFactory.getFont(FontFactory.HELVETICA); // Creación de una nueva fuente
        font.setColor(Color.WHITE); // Configuración del color de la fuente

        cell.setPhrase(new Phrase("ID", font)); // Configuración del contenido de la celda para "ID"
        table.addCell(cell); // Agregar la celda a la tabla

        cell.setPhrase(new Phrase("Título", font)); // Configuración del contenido de la celda para "Título"
        table.addCell(cell); // Agregar la celda a la tabla

        cell.setPhrase(new Phrase("Descripción", font)); // Configuración del contenido de la celda para "Descripción"
        table.addCell(cell); // Agregar la celda a la tabla

        cell.setPhrase(new Phrase("Nivel", font)); // Configuración del contenido de la celda para "Nivel"
        table.addCell(cell); // Agregar la celda a la tabla

        cell.setPhrase(new Phrase("Publicado", font)); // Configuración del contenido de la celda para "Publicado"
        table.addCell(cell); // Agregar la celda a la tabla
    }

    private void writeTableData(PdfPTable table) { // Declaración del método writeTableData con un parámetro de tipo PdfPTable
        for (Curso curso : listaCursos) { // Iteración sobre la lista de cursos
            table.addCell(String.valueOf(curso.getId())); // Agregar el ID del curso a la tabla
            table.addCell(curso.getTitulo()); // Agregar el título del curso a la tabla
            table.addCell(curso.getDescripcion()); // Agregar la descripción del curso a la tabla
            table.addCell(String.valueOf(curso.getNivel())); // Agregar el nivel del curso a la tabla
            table.addCell(String.valueOf(curso.isPublicado())); // Agregar el estado de publicación del curso a la tabla
        }
    }

    public void export(HttpServletResponse response) throws IOException { // Declaración del método export que recibe como parámetro un objeto HttpServletResponse y puede lanzar una IOException
        Document document = new Document(PageSize.A4); // Creación de un nuevo documento con tamaño de página A4
        PdfWriter.getInstance(document, response.getOutputStream()); // Obtención de la instancia de PdfWriter con el flujo de salida del objeto HttpServletResponse

        document.open(); // Apertura del documento

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD); // Creación de una nueva fuente en negrita
        font.setSize(18); // Configuración del tamaño de la fuente
        font.setColor(Color.BLUE); // Configuración del color de la fuente

        Paragraph p = new Paragraph("Lista de cursos", font); // Creación de un nuevo párrafo con el texto "Lista de cursos" y la fuente especificada
        p.setAlignment(Paragraph.ALIGN_CENTER); // Alineación del párrafo al centro

        document.add(p); // Agregar el párrafo al documento

        PdfPTable table = new PdfPTable(5); // Creación de una nueva tabla con 5 columnas
        table.setWidthPercentage(100); // Configuración del ancho de la tabla al 100%
        table.setWidths(new float[]{1.3f, 3.5f, 3.5f, 2.0f, 1.5f}); // Configuración de los anchos relativos de las columnas
        table.setSpacingBefore(10); // Configuración del espacio antes de la tabla

        writeTableHeader(table); // Llamada al método para escribir la cabecera de la tabla
        writeTableData(table); // Llamada al método para escribir los datos de la tabla

        document.add(table); // Agregar la tabla al documento

        document.close(); // Cierre del documento
    }
}
