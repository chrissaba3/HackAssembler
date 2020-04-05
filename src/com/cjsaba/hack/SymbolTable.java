package com.cjsaba.hack;

import java.util.*;

public class SymbolTable {
    private HashMap<String, Integer> symbolTable;

    public SymbolTable(){
        symbolTable = new HashMap<String, Integer>();
    }


    public void addSymbol(String symbol, int address){
        symbolTable.put(symbol, address);
        return;
    }
    // check if symbol table has symbol
    public boolean contains(String symbol){
        return symbolTable.containsKey(symbol);

    }
    //return address of symbol in table
    public int getAddress(String symbol){
        return symbolTable.get(symbol);

    }
    public void initialize(){
        //Virtual machine registers
        for(int i = 0; i<16; i++){
            this.addSymbol("R"+ i, i);
        }
        //IO pointers keyboard and screen
        this.addSymbol("KBD", 24576);
        this.addSymbol("SCREEN", 16384);

        //other
        this.addSymbol("SP", 0);
        this.addSymbol("LCL", 1);
        this.addSymbol("ARG", 2);
        this.addSymbol("THIS", 3);
        this.addSymbol("THAT", 4);
        return;
    }
}
