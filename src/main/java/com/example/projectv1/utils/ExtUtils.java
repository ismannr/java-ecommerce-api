package com.example.projectv1.utils;

import com.google.common.io.Files;

public class ExtUtils {
    public static String getExtension(String filename){
        return Files.getFileExtension(filename);
    }

    public static String getFilename(String filename){
        return Files.getNameWithoutExtension(filename);
    }
}
