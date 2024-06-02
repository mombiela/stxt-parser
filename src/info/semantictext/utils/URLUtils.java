package info.semantictext.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class URLUtils {
    public static String getUrlContent(URL url) throws IOException
    {
        InputStream in = url.openStream();
        InputStreamReader reader = new InputStreamReader(in, Constants.ENCODING);

        StringBuilder sb = new StringBuilder(Math.max(16, in.available()));
        char[] tmp = new char[4096];
        for(int cnt; (cnt = reader.read(tmp)) > 0; ) sb.append(tmp, 0, cnt);
        
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
