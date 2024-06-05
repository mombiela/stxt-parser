package info.old;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class UtilsFile
{
    public static byte[] readFile(File file) throws IOException
    {
        try (RandomAccessFile f = new RandomAccessFile(file, "r"))
        {
            // Get and check length
            long longlength = f.length();
            int length = (int) longlength;
            if (length != longlength)
                throw new IOException("File size >= 2 GB");

            // Read file and return data
            byte[] data = new byte[length];
            f.readFully(data);
            return data;
        }
    }

    public static String readFileContent(File file) throws IOException
    {
        return new String(readFile(file), StandardCharsets.UTF_8);
    }

    public static List<File> getStxtFiles(File directory)
    {
        List<File> stxtFiles = new ArrayList<>();
        Path startPath = Paths.get(directory.getAbsolutePath());

        try
        {
            Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                {
                    if (file.toString().endsWith(".stxt"))
                    {
                        stxtFiles.add(file.toFile());
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc)
                {
                    System.err.println("Error access file: " + file.toString());
                    return FileVisitResult.CONTINUE;
                }
            });
        }
        catch (IOException e)
        {
            System.err.println("Error search: " + e.getMessage());
        }

        return stxtFiles;
    }

    public static String getFileContentFromClasspath(String filePath) throws IOException
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(filePath))
        {
            if (inputStream == null)
            {
                throw new IOException("File not found: " + filePath);
            }
            return new String(readAllBytes(inputStream), Constants.ENCODING);
        }
    }

    private static byte[] readAllBytes(InputStream inputStream) throws IOException
    {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = inputStream.read(data, 0, data.length)) != -1)
        {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        return buffer.toByteArray();
    }
}
