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
public class DatosApp implements Serializable{
    String directory;

    public DatosApp() {
    }

    public DatosApp(String directory) {
        this.directory = directory;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    @Override
    public String toString() {
        return "DatosApp{" + "directory=" + directory + '}';
    }
    
}
