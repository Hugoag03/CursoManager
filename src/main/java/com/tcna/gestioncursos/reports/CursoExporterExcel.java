package com.tcna.gestioncursos.reports; // Paquete que contiene la clase

import com.tcna.gestioncursos.entity.Curso; // Importación de la clase Curso del paquete com.tcna.gestioncursos.entity
import jakarta.servlet.ServletOutputStream; // Importación de la clase ServletOutputStream del paquete jakarta.servlet
import jakarta.servlet.http.HttpServletResponse; // Importación de la clase HttpServletResponse del paquete jakarta.servlet.http
import org.apache.poi.ss.usermodel.Cell; // Importación de la clase Cell del paquete org.apache.poi.ss.usermodel
import org.apache.poi.ss.usermodel.CellStyle; // Importación de la clase CellStyle del paquete org.apache.poi.ss.usermodel
import org.apache.poi.ss.usermodel.Row; // Importación de la clase Row del paquete org.apache.poi.ss.usermodel
import org.apache.poi.xssf.usermodel.XSSFFont; // Importación de la clase XSSFFont del paquete org.apache.poi.xssf.usermodel
import org.apache.poi.xssf.usermodel.XSSFSheet; // Importación de la clase XSSFSheet del paquete org.apache.poi.xssf.usermodel
import org.apache.poi.xssf.usermodel.XSSFWorkbook; // Importación de la clase XSSFWorkbook del paquete org.apache.poi.xssf.usermodel

import java.io.IOException; // Importación de la clase IOException del paquete java.io
import java.util.List; // Importación de la clase List del paquete java.util

public class CursoExporterExcel { // Declaración de la clase CursoExporterExcel

    private XSSFWorkbook workbook; // Declaración de la variable workbook de tipo XSSFWorkbook
    private XSSFSheet sheet; // Declaración de la variable sheet de tipo XSSFSheet
    private List<Curso> cursos; // Declaración de la lista cursos de tipo Curso

    public CursoExporterExcel(List<Curso> cursos) { // Declaración del constructor de la clase con un parámetro de tipo Lista de Cursos
        this.cursos = cursos; // Inicialización de la lista cursos con el valor pasado como parámetro
        workbook = new XSSFWorkbook(); // Inicialización del objeto workbook como una nueva instancia de XSSFWorkbook
    }

    private void writeHeaderLine() { // Declaración del método writeHeaderLine
        sheet = workbook.createSheet("Cursos"); // Creación de una nueva hoja en el workbook con el nombre "Cursos"

        Row row = sheet.createRow(0); // Creación de una nueva fila en la hoja

        CellStyle style = workbook.createCellStyle(); // Creación de un nuevo estilo para las celdas
        XSSFFont font = workbook.createFont(); // Creación de una nueva fuente para el estilo
        font.setBold(true); // Configuración de la fuente como negrita
        font.setFontHeight(16); // Configuración del tamaño de la fuente
        style.setFont(font); // Configuración del estilo de celda con la fuente creada

        createCell(row, 0, "ID", style); // Creación de la celda para el encabezado "ID"
        createCell(row, 1, "Título", style); // Creación de la celda para el encabezado "Título"
        createCell(row, 2, "Descripción", style); // Creación de la celda para el encabezado "Descripción"
        createCell(row, 3, "Nivel", style); // Creación de la celda para el encabezado "Nivel"
        createCell(row, 4, "Estado de publicación", style); // Creación de la celda para el encabezado "Estado de publicación"
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) { // Declaración del método createCell con parámetros fila, índice de columna, valor y estilo de celda
        sheet.autoSizeColumn(columnCount); // Ajuste automático del ancho de la columna
        Cell cell = (row.createCell(columnCount)); // Creación de una nueva celda en la fila y columna especificadas

        if (value instanceof Integer) { // Verificación del tipo de valor
            cell.setCellValue((Integer) value); // Configuración del valor de la celda como entero
        } else if (value instanceof Boolean) { // Verificación del tipo de valor
            cell.setCellValue((Boolean) value); // Configuración del valor de la celda como booleano
        } else { // Si no es entero ni booleano
            cell.setCellValue((String) value); // Configuración del valor de la celda como cadena
        }
        cell.setCellStyle(style); // Aplicación del estilo de celda
    }

    private void writeDataLines() { // Declaración del método writeDataLines
        int rowCount = 1; // Inicialización del contador de filas

        CellStyle style = workbook.createCellStyle(); // Creación de un nuevo estilo de celda
        XSSFFont font = workbook.createFont(); // Creación de una nueva fuente para el estilo

        font.setFontHeight(14); // Configuración del tamaño de la fuente
        style.setFont(font); // Configuración del estilo de celda con la fuente creada

        for (Curso curso : cursos) { // Iteración sobre la lista de cursos
            Row row = sheet.createRow(rowCount++); // Creación de una nueva fila en la hoja y aumento del contador de filas
            int columnCount = 0; // Inicialización del contador de columnas

            createCell(row, columnCount++, curso.getId(), style); // Creación de la celda para el ID del curso
            createCell(row, columnCount++, curso.getTitulo(), style); // Creación de la celda para el título del curso
            createCell(row, columnCount++, curso.getDescripcion(), style); // Creación de la celda para la descripción del curso
            createCell(row, columnCount++, curso.getNivel(), style); // Creación de la celda para el nivel del curso
            createCell(row, columnCount++, curso.isPublicado(), style); // Creación de la celda para el estado de publicación del curso
        }
    }

    public void export(HttpServletResponse response) throws IOException { // Declaración del método export que recibe como parámetro un objeto HttpServletResponse y puede lanzar una IOException
        writeHeaderLine(); // Llamada al método para escribir la línea de encabezado
        writeDataLines(); // Llamada al método para escribir las líneas de datos

        ServletOutputStream outputStream = response.getOutputStream(); // Obtención del flujo de salida del objeto HttpServletResponse
        workbook.write(outputStream); // Escritura del workbook en el flujo de salida
        workbook.close(); // Cierre del workbook

        outputStream.close(); // Cierre del flujo de salida
    }
}
