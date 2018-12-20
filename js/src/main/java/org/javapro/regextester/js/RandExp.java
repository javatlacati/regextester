/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.javapro.regextester.js;

import net.java.html.js.JavaScriptBody;
import net.java.html.js.JavaScriptResource;

/**
 *
 * @author ruslan.lopez
 */
@JavaScriptResource("randexp.min.js")
public class RandExp {
    
    @JavaScriptBody(args = {"regex"},body="return new RandExp(new RegExp(regex)).gen()")
    public static native String gen(String regex);
}
