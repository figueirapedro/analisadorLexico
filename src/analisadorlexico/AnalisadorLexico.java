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
        String addSpacesComma = withoutBreakLines.replaceAll(",", " ,");
        String withoutTabs = addSpacesComma.trim().replaceAll(" +", " ");
        
        String[] words = withoutTabs.split(" ");
        String[] alreadyFind = new String[words.length];
        String type = "";
        
        Integer cont = 0;
        
        try (PrintWriter out = new PrintWriter("src/analisadorlexico/saida.lex.csv")) {
            out.println("Numero Coluna,Token, Tipo");
            
            for(int i = 0; i < words.length; i++) {
                type = "";
                Integer anterior = i-1;
                
                if(Arrays.asList(alreadyFind).contains(words[i])){
                    continue;
                }
                if( Arrays.asList(reserved).contains(words[i]) ){
                    type = "Reservada";
                }
                else if( words[i].equals("==") || words[i].equals("===") || words[i].equals("!==") || words[i].equals(">=") || words[i].equals("<=") || words[i].equals(">") || words[i].equals("<") || words[i].equals("||") || words[i].equals("&&") ){
                    type = "Operador";
                }
                else if( words[i].equals("null") ){
                    type = "Literal Nulo";
                }
                else if( words[i].equals("true") || words[i].equals("false") ){
                    type = "Boleano";
                }
                else if( isNumeric(words[i]) ){
                    type = "Numero";
                }
                else if( anterior > -1 && (words[anterior].equals("var") || words[anterior].equals("let")) && words[i+1].equals("=") ){
                    type = "Variavel";
                }
                else if( anterior > -1 && words[anterior].equals("const") ){
                    type = "Constante";
                }
                else if( words[i].substring(0,1).equals("\"") ){
                    type = "String";
                    Integer stringcont = 1;
                    while( i+stringcont < words.length && ( Character.toString(words[i+stringcont].charAt(words[i+stringcont].length()-1)).equals("\"") || ( words[i+stringcont].length() > 1 && Character.toString(words[i+stringcont].charAt(words[i+stringcont].length()-2)).equals("\"") ) ) ){
                        words[i]+= " "+words[i+stringcont];
                        stringcont++;
                    }
                }
                else if( anterior > -1 && words[anterior].substring(words[anterior].length() - 1).equals("(") && (words[i+1].equals(")") || words[i+1].equals(",")) ){
                    type = "Parametro funcao";
                }
                else if( anterior > -1 && words[anterior].equals(",") && (words[i+1].equals(")") || words[i+1].equals(",")) ){
                    type = "Parametro funcao";
                }
                
                if( type != "" ){
                    alreadyFind[cont] = words[i];
                    out.println(cont+","+words[i].replaceAll(",", " ")+","+type);
                    cont++;
                }
            }
        }
    }
    
    private static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
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
