package com.cjsaba.hack;

import java.io.*;
import java.util.*;




public class Assembler {
    // IO Files
    private String inputFile;
    private PrintWriter output;
    //Symbol Table
    private SymbolTable table = new SymbolTable();

    public Assembler(String file)throws IOException{
        //define IO Files
        inputFile = file;
        String outputFile = inputFile.replaceAll("\\..*", "") + ".hack";
        output = new PrintWriter(new FileWriter(outputFile));
        //init symboltable
        table.initialize();
    }


    //assembler pass 1, build symbol table, labels handled.
    public void assemblePass1() throws FileNotFoundException, IOException{
        Parse parser = new Parse(inputFile);
        int romAddress = 0;
        String symbol;

        while(parser.advance()){
            if (parser.commandType() == Parse.CommandType.L_COMMAND)
            {
                symbol = parser.symbol();
                if (!table.contains(symbol))
                    table.addSymbol(symbol, romAddress);
            } else
            {
                romAddress++;
                // print warning when memory is all used
                if (romAddress > 32768)
                    System.err.println("Warning: all ROM is in use");
            }
        }
        parser.close();
        return;
    }

    //pass 2 assembler handles variables, generates code, replace symbols with symbol table values
    public void assemblePass2() throws IOException, FileNotFoundException{
        Parse parser = new Parse(inputFile);
        String dest, comp, jump;
        String symbol, value;
        // starting address for variables
        int ramAddress = 16;

        while (parser.advance())
        {
            try
            {
                if (parser.commandType() == Parse.CommandType.C_COMMAND)
                {
                    dest = parser.dest();
                    comp = parser.comp();
                    jump = parser.jump();

                    output.println("111" + Code.comp(comp) + Code.dest(dest) + Code.jump(jump));
                } else if (parser.commandType() == Parse.CommandType.A_COMMAND)
                {
                    symbol = parser.symbol();
                    if (Character.isDigit(symbol.charAt(0)))
                    {
                        value = Code.toBinary(symbol);
                    }
                    else if (table.contains(symbol))
                    {
                        value = Integer.toString(table.getAddress(symbol));
                        value = Code.toBinary(value);
                    }
                    else
                    {
                        // print warnings about memory usage
                        if (ramAddress > 16383)
                            System.err.println("Warning: allocating variable in I/O memory map");
                        if (ramAddress > 24576)
                            System.err.println("Warning: no more RAM left");

                        table.addSymbol(symbol, ramAddress);
                        value = Code.toBinary("" + ramAddress);
                        ramAddress++;
                    }

                    output.println("0" + value);
                }
            }
            catch (InvalidDestException ex) {
               Error.error("Invalid destination", inputFile, parser.lineNumber, parser.currentLine);
            }
            catch (InvalidCompException ex) {
                Error.error("Invalid computation", inputFile, parser.lineNumber, parser.currentLine);
            }
            catch (InvalidJumpException ex) {
                Error.error("Invalid jump", inputFile, parser.lineNumber, parser.currentLine);
            }
        }
        parser.close();
        return;
    }

    // close output file
    public void close() throws IOException
    {
        output.close();
        return;
    }






}
