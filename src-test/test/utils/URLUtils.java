package test.utils;

import java.io.IOException;
import java.net.URL;

import org.junit.jupiter.api.Test;

import info.old.Utils;

public class URLUtils
{
    @Test
    public void mainTest() throws IOException
    {
        System.out.println("Inici");
        
        System.out.println("Start");
        
        String content = Utils.getUrlContent(new URL("https://www.semantictext.info/page.stxt"));
        System.out.println(content);
        
        System.out.println("End");
    }

}
