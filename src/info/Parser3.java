package info;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;

public class Parser3 
{
    // -------------------
    // Internal properties
    // -------------------

    // Actual parser line and lineNum
    private int lineNum = 0;
    private String line = null;
    private boolean executed;
    private boolean lastNodeText = false;
    private int level = 0;

    // Procesadores
    private Processor mainProcessor;
    
    public void setProcessor(Processor processor)
    {
        this.mainProcessor = processor;
    }

    // ----------------------------
    // Principal methods of parsing
    // ----------------------------

    public void parseURI(String uri) throws IOException
    {
        String content = Utils.getUrlContent(new URL(uri));
        parse(content);
    }
    
    public void parseFile(File srcFile) throws IOException
    {
        String content = UtilsFile.readFileContent(srcFile);
        parse(content);
    }
    
    public void parse(String content) throws IOException 
    {
        // Only one execution
        checkOnlyOneExecution();

        // Get reader
        BufferedReader in = new BufferedReader(new StringReader(content));

        // Iterate
        while ((line = in.readLine()) != null) 
        {
            // Remove UTF8BOM if first line
            removeUTFBOM();
            
            // New line
            lineNum++;
            line = LineNormalizer.normalize(line, lastNodeText, level);
            if (line != null)
            {
                // Get num, level and line
                System.out.println("(" + lineNum + ") " + line);
                int i = line.indexOf(':');
                level = Integer.parseInt(line.substring(0,i));
                line = line.substring(i+1);
                processLine();
            }
        }
    }

    private void processLine()
    {
        System.out.println("(" + lineNum + ") " + level + " => " + line);
        
    }

    public Node getDocumentNode() 
    {
        // TODO Hacer correctamente
        return null;
    }

    // -------------------
    // Métodos utilitarios
    // -------------------
    
    private void checkOnlyOneExecution() throws ParseException
    {
        if (executed)
            throw new ParseException("Parser is not thread safe. Only one execution allowed");
        else
            executed = true;
    }
    
    private void removeUTFBOM()
    {
        if (lineNum==0) // First line
        {
            line = LineNormalizer.removeUTF8BOM(line);
        }
    }    
}
