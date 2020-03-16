/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edwbuck.maven.english;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author edwbuck
 */
public class DirectoryScanner {

    public DirectoryScanner() {
    }

    List<File> scan(File basedir) throws IOException {
        if (basedir == null) {
            throw new IllegalArgumentException("Basedir must be set");
        }

        List<File> results = new ArrayList<>();
        File[] files = basedir.listFiles();
        if (files == null) {
            files = new File[0];
        }
        
        // TODO replace with filevisitor implementation
        for (File fileOrDir : files) {
            if (fileOrDir.getName().length() <= 0) {
                continue;
            }
            String name = fileOrDir.getName();
            if (fileOrDir.isFile()) {
                results.add(new File(fileOrDir.getCanonicalPath()));
            }
            if (fileOrDir.isDirectory()) {
                results.addAll(scan(fileOrDir));
            }
        }
        return results;
    }

}
