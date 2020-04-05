package com.cjsaba.hack;

import java.io.*;



public class Parse {
    // current command
    public String currentCommand = "";
    // file being assembled
    public String inputFile;
    // current line number in the file
    public int lineNumber = 0;
    // current line in file
    public String currentLine;
    // buffered file reader
    private BufferedReader fileReader;



    //command Types
    enum CommandType{
        A_COMMAND,      //@xxx where xxx is dec num or symbol
        C_COMMAND,      // dest=comp; jump
        L_COMMAND       // (xxx) where xxx is symbol
    }

    //open input file to parse
    public Parse(String file) throws FileNotFoundException{
        inputFile = file;
        fileReader = new BufferedReader(new FileReader(file));
        lineNumber= 0;

    }
    //read next command from input and makes current command
    //returns true if cmd found
    //returns false at file end

    public boolean advance() throws IOException{
        while(true){
            currentLine = fileReader.readLine();
            lineNumber += 1;
            if(currentLine ==null)
                return false;
            currentCommand= currentLine.replaceAll("//.*$", "").trim();
            if(currentCommand.equals(""))
                continue;
            return true;
        }
    }
    public CommandType commandType(){
        if(currentCommand.startsWith("@")){
            return CommandType.A_COMMAND;
        }else if (currentCommand.startsWith("(")){
            return CommandType.L_COMMAND;
        }else{
            return CommandType.C_COMMAND;
        }
    }

    public String symbol(){
        return currentCommand.substring(1).replace(")","");
    }

    public String dest(){
        String dest = "";
        if(currentCommand.contains("=")){
            String[] array1 = currentCommand.split("=");
            dest = array1[0];

        }
        return  dest;
    }

    public String comp(){
        String comp;
        if (currentCommand.contains("=")){
            String[] array1 = currentCommand.split("=");
            String[] array2 = array1[1].split(";");
            comp = array2[0];
        }else{
            String[] array1 = currentCommand.split(";");
            comp = array1[0];
        }
        return comp;
    }

    public String jump(){
        String jump = "";
        if(currentCommand.contains(";")){
            String[] array = currentCommand.split(";");
            jump = array[1];
        }
        return jump;
    }


    //close input
    public void close() throws IOException{
        fileReader.close();
        return;
    }


}
