package info.semantictext.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class URLUtils {
    public static String getUrlContent(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        connection.setRequestProperty("Accept", "text/plain");

        InputStream in = connection.getInputStream();
        InputStreamReader reader = new InputStreamReader(in, "UTF-8");

        StringBuilder sb = new StringBuilder();
        char[] buffer = new char[4096];
        int length;
        while ((length = reader.read(buffer)) != -1) {
            sb.append(buffer, 0, length);
        }

        reader.close();
        return sb.toString();
    }
    
    public static void main(String[] args) throws IOException
    {
        System.out.println("Start");
        
        String content = getUrlContent(new URL("https://www.semantictext.info/page.stxt"));
        System.out.println(content);
        
        System.out.println("End");
    }
    
}
