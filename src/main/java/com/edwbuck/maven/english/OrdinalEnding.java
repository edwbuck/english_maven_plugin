/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edwbuck.maven.english;

/**
 *
 * @author edwbuck
 */
public enum OrdinalEnding {
    FIRST("st", "sts"),
    SECOND("nd", "nds"),
    THIRD("rd", "rds"),
    OTHER("th", "ths");

    private final String singular;

    private final String plural;

    static OrdinalEnding getEnding(String number) {
        if (number.length() > 1 && number.charAt(number.length() - 2) == '1') {
            return OTHER;
        } else if (number.length() > 0) {
            switch (number.charAt(number.length() - 1)) {
                case '1':
                    return FIRST;
                case '2':
                    return SECOND;
                case '3':
                    return THIRD;
                default:
                    return OTHER;
            }
        }
        throw new IllegalArgumentException("number must contain digits");
    }

    private OrdinalEnding(String singular, String plural) {
        this.singular = singular;
        this.plural = plural;
    }

    boolean matches(String ending) {
        return singular.matches(ending) || plural.matches(ending);
    }
    
    public String singular() {
        return singular;
    }
    
    public String plural() {
        return plural;
    }

}
