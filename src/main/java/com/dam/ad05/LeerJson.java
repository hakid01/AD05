/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dam.ad05;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hak
 */
public class LeerJson {

  public static Datos jsonToObj() {

    try {
      //para que lo lea el .jar
//      InputStream input = getClass().getResourceAsStream("/classpath/to/my/file");
      File file = new File("src/main/resources/data.json");
      FileReader fr = new FileReader(file, StandardCharsets.UTF_8);//utf8 para no tener problemas con ñ o tildes
      BufferedReader br = new BufferedReader(fr);

      //Leer linea a linea
      StringBuilder jsonBuiler = new StringBuilder();
      String linea;
      while ((linea = br.readLine()) != null) {
        jsonBuiler.append(linea).append("\n");
      }

      br.close();

      //Construimos el string con todas las lineas leídas
      String json = jsonBuiler.toString();
      //Debug: comprobamos si lee y construye bien el json
      //System.out.println("archivo json: " + json);

      //Pasamos json a clase Java
      Gson gson = new GsonBuilder().create();
      return gson.fromJson(json, Datos.class);

    } catch (FileNotFoundException ex) {
      System.err.println("Error al intentar abrir el archivo");
    } catch (IOException ex) {
      System.err.println("Error IOException");
    }
    return null;
  }

}
