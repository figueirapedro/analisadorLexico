package analisadorlexico;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;


public class AnalisadorLexico {

    public static void main(String[] args) throws IOException {
        String filePath = "/home/upper/Área de Trabalho/AnalisadorLexico/src/analisadorlexico/jquery-3.6.0.js";
        String fileString = readLineByLineJava8( filePath );
        String withoutComments = fileString.replaceAll("(?s:/\\*.*?\\*/)|//.*", "");
        String withoutSpaces = withoutComments.replaceAll("(?s)\\s|/\\*.*?\\*/|//[^\\r\\n]*", "" );

        System.out.println( withoutSpaces );
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
