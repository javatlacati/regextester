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

import net.java.html.json.Model;
import net.java.html.json.Property;

/**
 *
 * @author ruslan.lopez
 */
 /**
     * Data model for holding configuration needed to generate sample code.
     */
    @Model(targetId = "", className = "GeneratedCode", instance = false, properties = {
        @Property(name = "language", type = JavaBasedLanguage.class)
        , @Property(name = "operation", type = RegexOperation.class)
    })
    class GenerationCode {

    }
