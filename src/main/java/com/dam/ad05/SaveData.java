/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dam.ad05;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hak
 */
public class SaveData {

    ScanFiles sf = new ScanFiles();

    ArrayList<String> tempDir = new ArrayList<>();

    ArrayList<String> tempFiles = new ArrayList<>();

    //Creamos las consultas para insertar dir y archivos en la base de datos
    String sqlInsertDir = "INSERT INTO directorios (nome) "
            + "VALUES (?) ON CONFLICT (nome) DO NOTHING;";

    String sqlInsertFile = "INSERT INTO arquivos (nome, id_directorio, arquivo) "
            + "VALUES (?,?,?) ON CONFLICT ON CONSTRAINT arquivos_pkey DO NOTHING;";

    //Consulta para obtener id de los directorios
    String sqlSelectID = "SELECT id FROM directorios WHERE  nome = '";

    public void insertData(Connection conn, String path) {
        sf.scan(path);//Escaneamos directorios y archivos
        try {
            //Insertamos datos en tabla directorios
            PreparedStatement ps = conn.prepareStatement(sqlInsertDir);
            tempDir = sf.getDirs();//ArrayList de directorios

//            System.out.println("tempDir: " + tempDir);

            for (String dir : tempDir) {
//                System.out.println("dir string:" + dir);
                ps.setArray(1, conn.createArrayOf("TEXT", Main.pathToArray(dir)));
                ps.executeUpdate();
            }

            ps.close();

            //Insertamos datos en tabla arquivos
            Statement st = conn.createStatement();
            ps = conn.prepareStatement(sqlInsertFile);
            tempFiles = sf.getFiles();//Arraylist de archivos

            for (String file : tempFiles) {

                File f = new File(path + file.substring(1));
                try (FileInputStream fis = new FileInputStream(f)) {
                    String tempPath = file.substring(0, file.length() - (f.getName().length() + 1));
//          System.out.println("tempPath: " + tempPath);
                    try (ResultSet rs = st.executeQuery(sqlSelectID + Arrays.toString(Main.pathToArray(tempPath)).replace("[", "{").replace("]", "}") + "';")) {
                        int tempId = 0;
                        while (rs.next()) {
                            tempId = rs.getInt(1);
//              System.out.println("tempId: " + tempId);
                        }
                        ps.setString(1, f.getName());
                        ps.setInt(2, tempId);
                        ps.setBinaryStream(3, fis, (int) f.length());
                        ps.executeUpdate();
                    }
                }
            }
            st.close();
            ps.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(SaveData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException | IOException ex) {
            Logger.getLogger(SaveData.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


}
