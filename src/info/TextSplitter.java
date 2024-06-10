package info;

public class TextSplitter {
    private String prefix;
    private String centralText;
    private String suffix;

    public TextSplitter(String input) {
        splitText(input);
    }

    private void splitText(String input) {
        int prefixStart = input.indexOf("(");
        int prefixEnd = input.indexOf(")");

        if (prefixStart != -1 && prefixEnd != -1 && prefixEnd > prefixStart) {
            prefix = input.substring(prefixStart + 1, prefixEnd).trim();
            input = input.substring(prefixEnd + 1).trim();
        } else {
            prefix = "";
        }

        int suffixStart = input.lastIndexOf("(");
        int suffixEnd = input.lastIndexOf(")");

        if (suffixStart != -1 && suffixEnd != -1 && suffixEnd > suffixStart) {
            centralText = input.substring(0, suffixStart).trim();
            suffix = input.substring(suffixStart + 1, suffixEnd).trim();
        } else {
            centralText = input.trim();
            suffix = "";
        }
    }

    public String getPrefix() {
        return prefix;
    }

    public String getCentralText() {
        return centralText;
    }

    public String getSuffix() {
        return suffix;
    }

    public static void main(String[] args) {
        String input1 = "(texto prefix) texto central (texto sufijo)";
        String input2 = "texto central (texto sufijo)";
        String input3 = "(texto prefix) texto central";
        String input4 = "texto central";

        TextSplitter splitter1 = new TextSplitter(input1);
        System.out.println("*** INPUT: " + input1);
        System.out.println("Prefix: " + splitter1.getPrefix());
        System.out.println("Central Text: " + splitter1.getCentralText());
        System.out.println("Suffix: " + splitter1.getSuffix());

        TextSplitter splitter2 = new TextSplitter(input2);
        System.out.println("*** INPUT: " + input2);
        System.out.println("Prefix: " + splitter2.getPrefix());
        System.out.println("Central Text: " + splitter2.getCentralText());
        System.out.println("Suffix: " + splitter2.getSuffix());

        TextSplitter splitter3 = new TextSplitter(input3);
        System.out.println("*** INPUT: " + input3);
        System.out.println("Prefix: " + splitter3.getPrefix());
        System.out.println("Central Text: " + splitter3.getCentralText());
        System.out.println("Suffix: " + splitter3.getSuffix());

        TextSplitter splitter4 = new TextSplitter(input4);
        System.out.println("*** INPUT: " + input4);
        System.out.println("Prefix: " + splitter4.getPrefix());
        System.out.println("Central Text: " + splitter4.getCentralText());
        System.out.println("Suffix: " + splitter4.getSuffix());
    }
}
