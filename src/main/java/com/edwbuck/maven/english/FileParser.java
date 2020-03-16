/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edwbuck.maven.english;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author edwbuck
 */
class FileParser {

    enum ParseState {
        READY,
        NUMBER,
        WORD,
        SUFFIX,
        CARRIAGE_RETURN;
    }

    private ParseState state;

    private long number;

    FileParser() {
        state = ParseState.READY;
    }

    List<Ordinal> parse(File file) {
        state = ParseState.READY;
        List<Ordinal> ordinals = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            int value;
            int lineNumber = 1;
            StringBuilder numberBuilder = new StringBuilder();
            StringBuilder suffixBuilder = new StringBuilder();
            while ((value = in.read()) != -1) {
                switch (state) {
                    case READY:
                        if (value == '\n') {
                            lineNumber++;
                        } else if (Character.isDigit(value)) {
                            state = ParseState.NUMBER;
                            numberBuilder.append((char) value);
                        } else if (Character.isAlphabetic(value)) {
                            state = ParseState.WORD;
                        }
                        break;
                    case NUMBER:
                        if (value == '\n') {
                            state = ParseState.READY;
                            numberBuilder.setLength(0);
                            suffixBuilder.setLength(0);
                            lineNumber++;
                        } else if (Character.isDigit(value)) {
                            numberBuilder.append((char) value);
                        } else if (Character.isAlphabetic(value)) {
                            state = ParseState.SUFFIX;
                            suffixBuilder.append((char) value);
                        } else {
                            state = ParseState.READY;
                            numberBuilder.setLength(0);
                            suffixBuilder.setLength(0);
                        }
                        break;
                    case WORD:
                        if (value == '\n') {
                            state = ParseState.READY;
                            numberBuilder.setLength(0);
                            suffixBuilder.setLength(0);
                            lineNumber++;
                        } else if (!Character.isLetterOrDigit(value)) {
                            state = ParseState.READY;
                        }
                        break;
                    case SUFFIX:
                        if (value == '\n') {
                            state = ParseState.READY;
                            ordinals.add(new Ordinal(file, lineNumber, numberBuilder.toString(), suffixBuilder.toString()));
                            numberBuilder.setLength(0);
                            suffixBuilder.setLength(0);
                            lineNumber++;
                        } else if (Character.isLetter(value)) {
                            suffixBuilder.append((char) value);
                        } else if (Character.isWhitespace(value)) {
                            state = ParseState.READY;
                            ordinals.add(new Ordinal(file, lineNumber, numberBuilder.toString(), suffixBuilder.toString()));
                            numberBuilder.setLength(0);
                            suffixBuilder.setLength(0);
                        } else {
                            state = ParseState.WORD;
                            numberBuilder.setLength(0);
                            suffixBuilder.setLength(0);
                        }
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ordinals;
    }
}
