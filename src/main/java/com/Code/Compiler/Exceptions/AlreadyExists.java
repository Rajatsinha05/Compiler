package com.Code.Compiler.Exceptions;

public class AlreadyExists extends RuntimeException{
    public AlreadyExists(String message){
        super(message);
    }
}
