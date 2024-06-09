package info.ia;

public class IndentResult {
    private final int indentLevel;
    private final String lineWithoutIndent;

    public IndentResult(int indentLevel, String lineWithoutIndent) {
        this.indentLevel = indentLevel;
        this.lineWithoutIndent = lineWithoutIndent;
    }

    public int getIndentLevel() {
        return indentLevel;
    }

    public String getLineWithoutIndent() {
        return lineWithoutIndent;
    }
}
