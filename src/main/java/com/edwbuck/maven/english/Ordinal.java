/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edwbuck.maven.english;

import java.io.File;

/**
 *
 * @author edwbuck
 */
public class Ordinal {
    
    private File file;
    
    private int line;
    
    private String number;
    
    private String suffix;
    
    public Ordinal(File file, int line, String number, String suffix) {
        this.file = file;
        this.line = line;
        this.number = number;
        this.suffix = suffix;
    }
    
    public File getFile() {
        return file;
    }
    
    public int getLine() {
        return line;
    }
    
    public String getNumber() {
        return number;
    }
    
    public String getSuffix() {
        return suffix;
    }
    
    public boolean passes() {
        return OrdinalEnding.getEnding(number).matches(suffix);
    }
    
    
}
