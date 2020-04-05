/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dam.ad05;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hak
 */
public class Main {

  static String url;
  static String db;
  static String user;
  static String pass;
  static String path;

//  public static void main(String[] args) {
  public static void start() {

    Notificacion.activarHilo();
    CheckFiles.activarHilo();
    cargarDatos();

    Connection conn = crearConexion();
    DownloadData.start(conn, path);

    SaveData saving = new SaveData();
    saving.insertData(conn, path);

    //Cerramos la conexión con la db
    if (conn != null) {
      try {
        conn.close();
        System.out.println("cerramos conexión");
      } catch (SQLException ex) {
        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    
    Notificacion not = new Notificacion(crearConexion());
    not.notCon();
    not.start();

    CheckFiles checking = new CheckFiles(crearConexion(),path);
    checking.start();
  }

  private static void cargarDatos() {
    LeerJson data = new LeerJson();
    Datos d = data.jsonToObj();

    url = d.getDbConnection().getAddress();
    db = d.getDbConnection().getName();
    user = d.getDbConnection().getUser();
    pass = d.getDbConnection().getPassword();

    path = d.getApp().getDirectory();
    //DEbug
        System.out.println("pass db: " + d.getDbConnection().getPassword());
  }

  private static Connection crearConexion() {
    Connection conn = null;

    //Propiedades de la conexión con la db
    Properties props = new Properties();
    props.setProperty("user", user);
    props.setProperty("password", pass);

    //Dirección de conexión con db
    String postgres = "jdbc:postgresql://" + url + "/" + db;

    try {
      conn = DriverManager.getConnection(postgres, props);
        System.out.println("Hay conexión");
      return conn;
    } catch (SQLException ex) {
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    }

    return conn;
  }

  public static String getPath() {
    return path;
  }

  public static void setPath(String path) {
    Main.path = path;
  }
  
  
  
}
