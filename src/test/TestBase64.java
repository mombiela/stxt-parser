package test;

import java.io.UnsupportedEncodingException;

import java.util.Base64;

public class TestBase64
{
    public static void main(String[] args) throws UnsupportedEncodingException
    {
        String text = "Hello World!!!!!!\n";
        for (int i = 0; i<10; i++)
        {
            text = text + "Hola Mundo!!!!!!";
        }
        System.out.println(new String(Base64.getEncoder().encode(text.getBytes("UTF-8"))));
    }
}
