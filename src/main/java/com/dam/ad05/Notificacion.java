/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dam.ad05;

import java.awt.Component;
import java.io.File;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.postgresql.PGConnection;
import org.postgresql.PGNotification;

/**
 *
 * @author hak
 */
public class Notificacion extends Thread {
  
  static boolean ejecutar = true;
  Connection conn = null;
  PGConnection pgconn = null;
  
  public Notificacion(Connection conn) {
    this.conn = conn;
  }
  
  public void notCon() {
    
    funcionPSQL();
    triggerPSQL();
    
    try {
      pgconn = conn.unwrap(PGConnection.class);
      try (Statement stmt = conn.createStatement()) {
        stmt.execute("LISTEN novamensaxe");
      }
      System.out.println("Esperando mensaxes...");
    } catch (SQLException ex) {
      Logger.getLogger(Notificacion.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  private void funcionPSQL() {
    String sqlCreateFunction = "CREATE OR REPLACE FUNCTION notificacion() "
        + "RETURNS trigger AS $$ "
        + "BEGIN "
        + "PERFORM pg_notify('novamensaxe',NEW.id::text); "
        + "RETURN NEW; "
        + "END; "
        + "$$ LANGUAGE plpgsql; ";
    try (CallableStatement createFunction = conn.prepareCall(sqlCreateFunction)) {
      createFunction.execute();
//      System.out.println("Funcion creada con exito!!!!!!");
    } catch (SQLException ex) {
      Logger.getLogger(Notificacion.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  private void triggerPSQL() {
    String sqlCreateTrigger = "DROP TRIGGER IF EXISTS trigger_not ON arquivos; "
        + "CREATE TRIGGER trigger_not "
        + "AFTER INSERT "
        + "ON arquivos "
        + "FOR EACH ROW "
        + "EXECUTE PROCEDURE notificacion(); ";
    try (CallableStatement createTrigger = conn.prepareCall(sqlCreateTrigger)) {
      createTrigger.execute();
//      System.out.println("Trigger creado con exito!!!!");
    } catch (SQLException ex) {
      Logger.getLogger(Notificacion.class.getName()).log(Level.SEVERE, null, ex);
    }
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
      try {
        
        PGNotification notifications[] = pgconn.getNotifications();
        
        if (notifications != null) {
          for (int i = 0; i < notifications.length; i++) {
            
            int id = Integer.parseInt(notifications[i].getParameter());
            
            PreparedStatement sqlMensaxe = conn.prepareStatement("SELECT d.nome, a.nome "
                + "FROM directorios d, arquivos a "
                + "WHERE a.id_directorio=d.id AND a.id = ?");
            
            sqlMensaxe.setInt(1, id);
            try (ResultSet rs = sqlMensaxe.executeQuery()) {
              rs.next();
              
              Array pathArray = rs.getArray(1);
                String[] strPathArray = (String[]) pathArray.getArray();

                String dirPath = "";
                for (String s : strPathArray) {
                    dirPath += s;
                    dirPath += File.separator;
                }
              
              JOptionPane.showMessageDialog(null, "Se ha añadido "
                  + dirPath + rs.getString(2), Main.getPath(),
                  JOptionPane.INFORMATION_MESSAGE);
//              System.out.println(rs.getString(1) + ":" + rs.getString(2));
              DownloadData.start(conn, Main.getPath());
            }
          }
        }

        //Creo que lo puedo cambiar declarando el tiempo de pausa en getNotificacions(15000);
        System.out.println("Comprobación de mensajes pausada 15s");
        Thread.sleep(15000);
        
      } catch (SQLException | InterruptedException ex) {
        Logger.getLogger(Notificacion.class.getName()).log(Level.SEVERE, null, ex);
      }
      
    }
  }
}
