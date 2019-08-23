package test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import java.util.Base64;

public class TestBase64Foto
{
    public static void main(String[] args) throws IOException
    {
        byte[] btts = readFile(new File("foto-001.gif"));
        System.out.println(Base64.getEncoder().encode(btts));
    }
    
    public static byte[] readFile (String file) throws IOException {
        return readFile(new File(file));
    }

    public static byte[] readFile (File file) throws IOException {
        // Open file
        RandomAccessFile f = new RandomAccessFile(file, "r");

        try {
            // Get and check length
            long longlength = f.length();
            int length = (int) longlength;
            if (length != longlength) throw new IOException("File size >= 2 GB");

            // Read file and return data
            byte[] data = new byte[length];
            f.readFully(data);
            return data;
        }
        finally {
            f.close();
        }
    }
    
}
