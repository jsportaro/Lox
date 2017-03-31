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
            "Unary    : Token operator, Expression right"
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

        defineVisitor(writer, baseName, types);

        for (String type : types) {
            String [] parts = type.split(":");
            String className = parts[0].trim();
            String fields = parts[1].trim();

            defineType(writer, baseName, className, fields);
        }

        writer.println("");
        writer.println("    abstract <R> R accept(Visitor<R> visitor);");

        writer.println("}");
        writer.close();
    }

    private static void defineVisitor(
        PrintWriter writer,
        String baseName,
        List<String> types)
    {
        writer.println("    interface Visitor<R> {");

        for (String type : types) {
            String typeName = type.split(":")[0].trim();
            writer.println("        R visit" + typeName + baseName + "(" + typeName + " " + baseName.toLowerCase() + ");");
        }

        writer.println("    }");
    }

    private static void defineType(
            PrintWriter writer,
            String baseName,
            String className,
            String fieldList) {
        writer.println("");
        writer.println("    static class " + className + " extends " + baseName + "{");

        // Ctor
        writer.println("        " + className + "(" + fieldList + ") {");

        //  Parameters
        String [] fields = fieldList.split(",");
        for (String field : fields) {
            String name = field.trim().split(" ")[1];
            writer.println("            this." + name + " = " + name + ";");
        }
        writer.println("        }");

        //  Visitor pattern
        writer.println("");
        writer.println("        <R> R accept(Visitor<R> visitor) {");
        writer.println("            return visitor.visit" + className + baseName + "(this);");
        writer.println("        }");

        //  Fields
        writer.println();
        for (String field : fields) {
            writer.println("        final " + field.trim() + ";");
        }
        writer.println("    }");
    }
}
