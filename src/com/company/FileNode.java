package com.company;

import java.io.File;

public class FileNode {

    File file;
    String fileName;

    public FileNode(String fName){
        file = new File(fName);
    }

    public FileNode(String name, File f){
        fileName = name;
        file = f;
    }

    public File getFile(){
        return file;
    }

    public String toString(){
        if(file.getName().equals(""))
            return file.getAbsolutePath();
        return file.getName();
    }

    public boolean isDirectory(){
        return file.isDirectory();
    }

}
