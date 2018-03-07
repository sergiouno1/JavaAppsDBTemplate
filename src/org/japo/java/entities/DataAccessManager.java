/*
 * Copyright 2018 José A. Pacheco Ondoño - joanpaon@gmail.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.japo.java.entities;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author José A. Pacheco Ondoño - joanpaon@gmail.com
 */
public class DataAccessManager {

    // Sentencias SQL - Módulos
    private static final String DEF_MOD_SQL1 = "SELECT * FROM modulo";
    private static final String DEF_MOD_SQL2 = "DELETE FROM modulo WHERE acronimo='SI'";
    private static final String DEF_MOD_SQL3
            = "INSERT INTO modulo "
            + "(id, acronimo, nombre, codigo, horasCurso, curso) "
            + "VALUES "
            + "(1234, 'SI', 'Sistemas Informáticos', '0482', 96, 1)";
    private static final String DEF_MOD_SQL4 = "UPDATE modulo SET curso=2 WHERE horasCurso<100";

    // Sentencias SQL - Alumnos
    private static final String DEF_ALU_SQL1 = "SELECT * FROM alumno";

    // Sentencias SQL - Profesores
    private static final String DEF_PRF_SQL1 = "SELECT * FROM profesor";

    // Cabecera Módulos
    private static final String CAB_LST_MOD1
            = " #  Id          Acrónimo   Nombre                    Código     Horas Curso";
    private static final String CAB_LST_MOD2
            = "=== =========== ========== ========================= ========== ===== =====";

    // Cabecera Alumnos
    private static final String CAB_LST_ALU1
            = " #  ";
    private static final String CAB_LST_ALU2
            = "=== ";

    // Cabecera Profesores
    private static final String CAB_LST_PRO1
            = " #  ";
    private static final String CAB_LST_PRO2
            = "=== ";

    // Referencias
    private Connection con;
    private Statement stmt;

    // Constructor - Connection
    public DataAccessManager(Connection con) {
        this.con = con;
    }

    // Constructor - Connection + Statement
    public DataAccessManager(Connection con, Statement stmt) {
        this.con = con;
        this.stmt = stmt;
    }

    // --- Accesores ---
    public Connection getCon() {
        return con;
    }

    public void setCon(Connection con) {
        this.con = con;
    }

    public Statement getStmt() {
        return stmt;
    }

    public void setStmt(Statement stmt) {
        this.stmt = stmt;
    }
    // --- Accesores ---

    // Metadatos de la base de datos >> Pantalla
    public final void mostrarMetadatos() throws SQLException {
        // Metadatos de la base de datos
        DatabaseMetaData dmd = con.getMetaData();

        // Cabecera
        System.out.println("Información");
        System.out.println("===========");

        // Información de la base de datos
        System.out.printf("Usuario .........: %s%n", dmd.getUserName());
        System.out.printf("Base de datos ...: %s%n", dmd.getDatabaseProductName());
        System.out.printf("Versión SGBD ....: %s%n", dmd.getDatabaseProductVersion());
        System.out.printf("Driver JDBC .....: %s%n", dmd.getDriverName());
        System.out.printf("Versión JDBC ....: %d.%d%n",
                dmd.getJDBCMajorVersion(),
                dmd.getJDBCMinorVersion());
    }

    // Módulos BD >> Módulos Listado
    public final void listarModulos() throws SQLException {
        // Proceso de listado
        try (ResultSet rs = stmt.executeQuery(DEF_MOD_SQL1)) {
            // Mensaje de inicio de listado
            System.out.println("Listado de módulos ...");

            // Separación
            System.out.println("---");

            // Comprueba si hay datos
            if (rs.next()) {
                // Cabecera
                System.out.println(CAB_LST_MOD1);
                System.out.println(CAB_LST_MOD2);

                // Generación del informe - Fila a fila
                do {
                    // Línea de texto con los datos de la fila actual
                    // Los campos se refieren por su nombre o por su posición
                    // Por su nombre, debe ser exactamente el mismo en la tabla
                    // Por su posición, los campos se numeran a partir de 1
                    int fila = rs.getRow();
                    int id = rs.getInt("id");
                    String acronimo = rs.getString("acronimo");
                    String nombre = rs.getString("nombre");
                    String codigo = rs.getString("codigo");
                    int horas = rs.getInt("horasCurso");
                    int curso = rs.getInt("curso");
                    System.out.printf("%03d %-11d %-10s %-25s %-10s %4d %4d%n",
                            fila, id, acronimo, nombre, codigo, horas, curso);
// ---
//                    String cadFormato = "%03d %-11d %-10s %-25s %-10s %4d %4d%n";
//                    String fila = String.format("%03d ", rs.getRow());
//                    String id = String.format("%-11d ", rs.getInt("id"));
//                    String acronimo = String.format("%-10s ", rs.getString("acronimo"));
//                    String nombre = String.format("%-25s ", rs.getString("nombre"));
//                    String codigo = String.format("%-10s ", rs.getString("codigo"));
//                    String horas = String.format("%4d ", rs.getInt("horasCurso"));
//                    String curso = String.format("%4d", rs.getInt("curso"));
//                    System.out.println(fila + id + acronimo + nombre + codigo + horas + curso);
// ---
//                    System.out.println(
//                            String.format("%03d ", rs.getRow())
//                            + String.format("%-11d ", rs.getInt("id"))
//                            + String.format("%-10s ", rs.getString("acronimo"))
//                            + String.format("%-25s ", rs.getString("nombre"))
//                            + String.format("%-10s ", rs.getString("codigo"))
//                            + String.format("%4d ", rs.getInt("horasCurso"))
//                            + String.format("%4d", rs.getInt("curso")));
                } while (rs.next());
            } else {
                System.out.println("No hay módulos que mostrar ...");
            }
        }
    }

    // Módulos BD >> Módulos Eliminación
    public void borrarModulos() throws SQLException {
        // Mensaje de inicio de listado
        System.out.println("Borrado de módulos ...");

        // Separación
        System.out.println("---");

        // Borrado de datos
        int filas = stmt.executeUpdate(DEF_MOD_SQL2);

        // Muestra las filas borradas
        System.out.println(filas + " fila/s borrada/s");
    }

    // Módulos BD >> Módulos Inserción
    public void insertarModulos() throws SQLException {
        // Mensaje de inicio de listado
        System.out.println("Inserción de módulos ...");

        // Separación
        System.out.println("---");

        // Inserción de datos
        int filas = stmt.executeUpdate(DEF_MOD_SQL3);

        // Muestra las filas borradas
        System.out.println(filas + " fila/s insertadas/s");
    }

    // Módulos BD >> Módulos Actualización
    public void modificarModulos() throws SQLException {
        // Mensaje de inicio de listado
        System.out.println("Modificación de módulos ...");

        // Separación
        System.out.println("---");

        // Inserción de datos
        int filas = stmt.executeUpdate(DEF_MOD_SQL4);

        // Muestra las filas borradas
        System.out.println(filas + " fila/s modificadas/s");
    }
}
