/*
 * Copyright (C) 2019 ruslan.lopez
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.javapro.regextester;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author ruslan.lopez
 */
public enum JavaBasedLanguage {
    JAVA("JAVA")
    , KOTLIN("KOTLIN")
     , GROOVY("GROOVY");
//    , SCALA("SCALA")
//    ,CLOJURE("CLOJURE")
//    ,JAVA_JS("JAVA_JS") //that means Bison or Nashorn ot whatever Java implemented JS compatible versión

    private final String name;

    private JavaBasedLanguage(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public JavaBasedLanguage languageFromString(String allegedName) {
        JavaBasedLanguage[] validValues = JavaBasedLanguage.values();
        for (JavaBasedLanguage validValue : validValues) {
            if (validValue.toString().equalsIgnoreCase(allegedName)) {
                return validValue;
            }
        }
        throw new IllegalArgumentException("Specified language doesn't exist yet");
    }
    
    public static List<JavaBasedLanguage> supportedLanguages(){
        JavaBasedLanguage[] validValues = JavaBasedLanguage.values();
        List<JavaBasedLanguage> result = new ArrayList<>(validValues.length);
        Collections.addAll(result, validValues);
        return result;
    }
}
