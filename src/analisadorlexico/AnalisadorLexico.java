package analisadorlexico;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.Arrays;


public class AnalisadorLexico {

    public static void main(String[] args) throws IOException {
        String[] reserved = {"break", "case", "catch", "continue", "debugger", "default", "delete", "do", "else", "finally", "for", "function", "if", "in", "instanceof", "new", "return", "switch", "this", "throw", "try", "typeof", "var", "void", "while", "with", "class", "const", "enum", "export", "extends", "import", "super", "implements", "interface", "let", "package", "private", "protected", "public", "static", "yield"};
        
        String filePath = "src/analisadorlexico/jquery-3.6.0.js";
        String fileString = readLineByLineJava8( filePath );
        String withoutComments = fileString.replaceAll("(?s:/\\*.*?\\*/)|//.*", "");
        String withoutBreakLines = withoutComments.replaceAll("(?s)\\s|/\\*.*?\\*/|//[^\\r\\n]*", " " );
        String withoutTabs = withoutBreakLines.trim().replaceAll(" +", " ");
        
        String[] words = withoutTabs.split(" ");
        String[] alreadyFind = new String[words.length];

        Integer cont = 0;
        
        try (PrintWriter out = new PrintWriter("src/analisadorlexico/saida.lex.csv")) {
            out.println("Numero Coluna,Token, Tipo");
            
            for(int i = 0; i < words.length; i++) {
                
                if(Arrays.asList(alreadyFind).contains(words[i])){
                    continue;
                }
                if( Arrays.asList(reserved).contains(words[i]) ){
                    alreadyFind[cont] = words[i];
                    out.println(cont+","+words[i]+",Reservada");
                    cont++;
                    continue;
                }
                
            }
        }
    }
    
     
    private static String readLineByLineJava8(String filePath) 
    {
        StringBuilder contentBuilder = new StringBuilder();
 
        try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8)) 
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }
 
        return contentBuilder.toString();
    }
    
}
