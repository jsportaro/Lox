package tools;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class GenerateAst {
    public static void main(String [] args) throws IOException {
        String outputDirectory;
        if (args.length != 1) {
            outputDirectory = "././././src/com/lox";
        } else {
            outputDirectory = args[0];
        }

        defineAst(outputDirectory, "Expression", Arrays.asList(
            "Binary   : Expression left, Token operator, Expression right",
            "Grouping : Expression expression",
            "Literal  : Object value",
            "Unary    : Token Operator, Expression right"
        ));
    }

    private static void defineAst(
            String outputDirectory,
            String baseName,
            List<String> types) throws IOException {
        String path = outputDirectory + "/" + baseName + ".java";
        PrintWriter writer = new PrintWriter(path, "UTF-8");

        writer.println("package com.lox;");
        writer.println("");
        writer.println("import java.util.List;");
        writer.println("");
        writer.println("abstract class " + baseName + " {");

        for (String type : types) {
            String [] parts = type.split(":");
            String className = parts[0].trim();
            String fields = parts[1].trim();

            defineType(writer, baseName, className, fields);
        }

        writer.println("}");
        writer.close();
    }

    private static void defineType(
            PrintWriter writer,
            String basename,
            String className,
            String fieldList) {
        writer.println("");
        writer.println("    static class " + className + " extends " + basename + "{");

        // Ctor
        writer.println("        " + className + "(" + fieldList + ") {");

        //  Parameters
        String [] fields = fieldList.split(",");
        for (String field : fields) {
            String name = field.trim().split(" ")[1];
            writer.println("            this." + name + " = " + name + ";");
        }
        writer.println("        }");

        writer.println();
        for (String field : fields) {
            writer.println("        final " + field.trim() + ";");
        }
        writer.println("    }");
    }
}
