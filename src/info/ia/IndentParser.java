package info.ia;

public class IndentParser 
{
    public static IndentResult getIndentLevelAndLine(String line) {
        int indentLevel = 0;
        while (line.startsWith("    ") || line.startsWith("\t")) {
            if (line.startsWith("    ")) {
                indentLevel++;
                line = line.substring(4);
            } else if (line.startsWith("\t")) {
                indentLevel++;
                line = line.substring(1);
            }
        }
        return new IndentResult(indentLevel, line);
    }
}
