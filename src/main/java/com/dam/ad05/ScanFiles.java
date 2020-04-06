/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dam.ad05;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author hak
 */
public class ScanFiles {

    private ArrayList<String> dirs = new ArrayList<>();
    private ArrayList<String> files = new ArrayList<>();
    int rootPathLength;

    public void scan(String path) {
        /*calculamos la longitud del path de nuestra raiz 
    para más adelante eliminar ese número de caracteres 
    y quedarnos solo con una ruta relativa*/
        rootPathLength = path.length();

        File file = new File(path);

        if (!file.exists()) {//Comprobamos si existe el path a escanear
            System.err.println("No existe la ruta especifidada");
        } else {
            if (!file.isDirectory()) {//Comprobamos si es un directorio
                System.err.println("La ruta especificada no es un directorio");
                return;
            } else {
                scanRecursivo(path);
            }
        }

    }

    private void scanRecursivo(String path) {
        File file = new File(path);
        String[] tempFiles = file.list();
        dirs.add(".");

        for (int i = 0; i < tempFiles.length; i++) {
            String iPath = path + File.separator + tempFiles[i];
            File iFile = new File(iPath);

            String relativePath = "." + File.separator + iFile.getAbsolutePath().substring(rootPathLength + 1);

            if (iFile.isDirectory()) {
                dirs.add(relativePath);
                scanRecursivo(iFile.getAbsolutePath());
            } else {
                files.add(relativePath);
            }
//      System.out.println(iFile);
        }
    }

    public ArrayList<String> getDirs() {
        return dirs;
    }

    public ArrayList<String> getFiles() {
        return files;
    }

    // Metodos para debug
    public void printDirs() {
        System.out.println("Directorios: ");
        int n = 1;
        for (String dir : dirs) {
            System.out.println(n + ": " + dir);
            n++;
        }
    }

    public void printFiles() {
        System.out.println("Arquivos: ");
        int n = 1;
        for (String file : files) {
            System.out.println(n + ": " + file);
            n++;
        }
    }

}
