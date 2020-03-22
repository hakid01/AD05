/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dam.ad05;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hak
 */
public class CheckFiles extends Thread {

  static boolean ejecutar = true;
  private final Connection conn;
  private final String path;

  public CheckFiles(Connection conn, String path) {
    this.conn = conn;
    this.path = path;
  }

  public static void desactivarHilo() {
    ejecutar = false;
  }
  
  public static void activarHilo() {
    ejecutar = true;
  }

  @Override
  public void run() {

    while (ejecutar) {
      System.out.println("Buscando nuevos archivos");
      SaveData saving = new SaveData();
      saving.insertData(conn, path);

      try {
        System.out.println("Hilo dormido 30s");
        Thread.sleep(30000);
      } catch (InterruptedException ex) {
        Logger.getLogger(CheckFiles.class.getName()).log(Level.SEVERE, null, ex);
      }
    }

  }

}
