/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dam.ad05;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Array;
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
public class DownloadData {

    static ArrayList<String> dirList = new ArrayList<>();
    static ArrayList<String> archList = new ArrayList<>();

    public static void start(Connection conn, String path) {
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT nome FROM directorios;");
            while (rs.next()) {
                Array pathArray = rs.getArray("nome");
                String[] strPathArray = (String[]) pathArray.getArray();

                String strPath = "";
                for (String s : strPathArray) {
                    strPath += s;
                    strPath += File.separator;
                }
                dirList.add(strPath);
            }
            
            for(String s : dirList){
              System.out.println(s);
            }

            rs = st.executeQuery("SELECT d.nome, a.nome "
                    + "FROM directorios d, arquivos a WHERE d.id = a.id_directorio");
            while (rs.next()) {
                Array pathArray = rs.getArray(1);
                String[] strPathArray = (String[]) pathArray.getArray();

                String dirString = "";
                for (String s : strPathArray) {
                    dirString += s;
                    dirString += File.separator;
                }
                String archString = rs.getString(2);
                archList.add(dirString + archString);
            }
            
            for (String s : archList){
              System.out.println(s);
            }

            rs.close();
            st.close();

        } catch (SQLException ex) {
            Logger.getLogger(DownloadData.class.getName()).log(Level.SEVERE, null, ex);
        }

        crearDirectorios(path);

        crearArchivos(conn, path);

    }

    private static void crearDirectorios(String path) {
        dirList.forEach((String dir) -> {
            File directorio = new File(path + dir.substring(1));
//          System.out.println("llegamos a crear directorios");
            if (!directorio.exists()) {
                directorio.mkdirs();
            }
        });
    }

    private static void crearArchivos(Connection conn, String path) {
        archList.forEach((String arch) -> {
            File archivo = new File(path + arch.substring(1));

            if (!archivo.exists()) {
                try {
                    PreparedStatement ps = conn.prepareStatement("SELECT arquivo "
                            + "FROM arquivos WHERE id_directorio = ? AND nome = ?");

                    Statement st = conn.createStatement();

                    //convertimos el path del archivo arch en array para poder pasarlo a la base de datos
                    String pathDir = arch.substring(0, arch.length() - (archivo.getName().length() + 1));
//                    System.out.println("pathDir: " + pathDir);
                    
                    String[] pathDirarr = Main.pathToArray(pathDir);
//                    System.out.println("hola");
//                    System.out.println("pathDir array : " + Arrays.toString(pathDirarr));
                    
                    ResultSet rs2 = st.executeQuery("SELECT id FROM directorios "
                            + "WHERE nome = '" + Arrays.toString(pathDirarr).replace("[", "{").replace("]", "}") + "';");

                    int id_dir = 0;
                    while (rs2.next()) {
                        id_dir = rs2.getInt(1);
                    }

                    ps.setInt(1, id_dir);
                    ps.setString(2, archivo.getName());
                    ResultSet rs = ps.executeQuery();

                    byte[] archivoBytes = null;
                    while (rs.next()) {
                        archivoBytes = rs.getBytes(1);
                    }

                    rs2.close();
                    st.close();

                    rs.close();
                    ps.close();

                    //Creamos o fluxo de datos para gardar o arquivo recuperado
                    FileOutputStream fluxoDatos = new FileOutputStream(archivo);

                    //Gardamos o arquivo recuperado
                    if (archivoBytes != null) {
                        fluxoDatos.write(archivoBytes);
                    }
                    //cerramos o fluxo de datos de saida
                    fluxoDatos.close();

                } catch (SQLException | IOException ex) {
                    Logger.getLogger(DownloadData.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

}
