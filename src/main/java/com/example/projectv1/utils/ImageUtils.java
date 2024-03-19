package com.example.projectv1.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ImageUtils {

    public static byte[] imageCompressor(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] temp = new byte[4 * 1024];
        while (!deflater.finished()) {
            int size = deflater.deflate(temp);
            outputStream.write(temp, 0, size);
        }
        try {
            outputStream.close();
        } catch (Exception ignored) {}
        return outputStream.toByteArray();
    }

    public static byte[] imageDecompressor(byte[] data){
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] temp = new byte[4*1024];
        try {
            while (!inflater.finished()){
                int count = inflater.inflate(temp);
                outputStream.write(temp, 0, count);
            }
            outputStream.close();
        }catch (Exception ignored){}
        return outputStream.toByteArray();
    }

    public static Boolean isImage(MultipartFile file){
        String fileName = file.getOriginalFilename();
        return ExtUtils.getExtension(fileName).equals("jpg") || ExtUtils.getExtension(fileName).equals("png") || ExtUtils.getExtension(fileName).equals("jpeg");
    }

    public static String imageName(MultipartFile file){
        String fileName = file.getOriginalFilename();
        return ExtUtils.getFilename(fileName);
    }
}
