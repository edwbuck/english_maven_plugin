/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edwbuck.maven.english;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 *
 * @author edwbuck
 */
class Report {

    Report() {
    }

    void writeResults(File file, List<Ordinal> ordinals) {
        file.getParentFile().mkdirs();
        file.delete();
        try {
            file.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
        }
        try (BufferedWriter out = new BufferedWriter(new FileWriter(file))) {
            XMLStreamWriter xml = XMLOutputFactory.newInstance().createXMLStreamWriter(new FileWriter(file));
            xml.writeStartDocument("UTF-8", "1.0");
            xml.writeCharacters(" \n");
            xml.writeStartElement("ordinals");
            xml.writeDefaultNamespace("http://maven.edwinbuck.com/ordinals/1.0");
            xml.writeNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
            xml.writeAttribute("http://www.w3.org/2001/XMLSchema-instance", "schemaLocation", "http://maven.edwinbuck.com/ordinals/1.0 http://maven.edwinbuck.com/xsd/ordinals-1.0.xsd");
            xml.writeCharacters("\n");
            for (Ordinal ordinal : ordinals) {
                xml.writeCharacters("  ");
                xml.writeEmptyElement("http://maven.edwinbuck.com/ordinals/1.0", "ordinal");
                xml.writeAttribute("http://maven.edwinbuck.com/ordinals/1.0", "passed", ordinal.passes() ? "true" : "false");
                xml.writeAttribute("http://maven.edwinbuck.com/ordinals/1.0", "text", ordinal.getNumber() + ordinal.getSuffix());
                xml.writeAttribute("http://maven.edwinbuck.com/ordinals/1.0", "file", ordinal.getFile().toString());
                xml.writeAttribute("http://maven.edwinbuck.com/ordinals/1.0", "line", Integer.toString(ordinal.getLine()));
                xml.writeCharacters("\n");
            }
            xml.writeEndElement();
            xml.writeEndDocument();
            xml.close();
        } catch (IOException ex) {
            Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XMLStreamException ex) {
            Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
