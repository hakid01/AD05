/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dam.ad05;

import java.io.Serializable;

/**
 *
 * @author hak
 */
public class Datos implements Serializable{
    DatosDB dbConnection;
    DatosApp app;

    public Datos() {
    }

    public Datos(DatosDB dbConnection, DatosApp app) {
        this.dbConnection = dbConnection;
        this.app = app;
    }

    public DatosDB getDbConnection() {
        return dbConnection;
    }

    public void setDbConnection(DatosDB dbConnection) {
        this.dbConnection = dbConnection;
    }

    public DatosApp getApp() {
        return app;
    }

    public void setApp(DatosApp app) {
        this.app = app;
    }

    @Override
    public String toString() {
        return "Datos{" + "dbConnection=" + dbConnection + ", app=" + app + '}';
    }
    
    
}

