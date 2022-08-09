package com.company;

import java.io.File;

public class DIRead {

    public static File[] getAllDrives() {
        File[] drives;
        drives = File.listRoots();
        return drives;
    }

    public static File[] readDirectory(String path) {
        File file = new File(path);
        File[] files;
        files = file.listFiles();
        return files;
    }

}
