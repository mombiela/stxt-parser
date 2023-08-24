package info.semantictext.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileUtils {
    public static byte[] readFile (File file) throws IOException {
        try (RandomAccessFile f = new RandomAccessFile(file, "r")) {
            // Get and check length
            long longlength = f.length();
            int length = (int) longlength;
            if (length != longlength) throw new IOException("File size >= 2 GB");

            // Read file and return data
            byte[] data = new byte[length];
            f.readFully(data);
            return data;
        }
    }  
    
    public static void saveByteArrayToFile(File file, byte[] byteArray) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(byteArray);
        }
    }    
}
