package test;

import info.semantictext.utils.PropertiesParser;

import java.util.Properties;

public class TestPropertiesParser
{
    public static void main(String[] args)
    {
        Properties props = new Properties();
        props.setProperty("v1", "Value 1");
        props.setProperty("v2", "Value 2");
        props.setProperty("v3", "Value 3");
        props.setProperty("v4", "Value 4");
        
        System.out.println(PropertiesParser.replace(props, "${v4} Hola ${v1} esto. Ahora no existente:${v5} es un valor: $v2, y esto otro ${v3}"));
    }

}
