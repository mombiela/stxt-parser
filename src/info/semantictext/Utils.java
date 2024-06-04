package info.semantictext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class Utils
{
    public static String getUrlContent(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        connection.setRequestProperty("Accept", "text/html");

        try (InputStream in = connection.getInputStream();
             InputStreamReader isr = new InputStreamReader(in, "UTF-8");
             BufferedReader reader = new BufferedReader(isr)) {

            StringBuilder sb = new StringBuilder();
            char[] buffer = new char[4096];
            int length;
            while ((length = reader.read(buffer)) != -1) {
                sb.append(buffer, 0, length);
            }
            return sb.toString();
        }
    }   
    public static String uniform(String name)
    {
        return name.trim().toLowerCase(Locale.ENGLISH);
    }
    public static String cleanupString(String input) {
        return input.replaceAll("[\\r\\n\\t]+|\\s+", "");
    }
    

}
