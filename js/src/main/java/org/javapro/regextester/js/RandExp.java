/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.javapro.regextester.js;

import net.java.html.js.JavaScriptBody;
import net.java.html.js.JavaScriptResource;

/**
 * @author ruslan.lopez
 */
@JavaScriptResource("randexp.min.js")
public class RandExp {

    @JavaScriptBody(args = {"regex"}, body = "return new RandExp(new window.RegExp(regex)).gen()")
    public static native String gen(String regex);

    // private final Object randexp;
//
//    private RandExp(Object randexp) {
//        this.randexp = randexp;
//    }
//
//    public static RandExp create(String regex) {
//        return new RandExp(createImpl(regex));
//    }
//
//    @JavaScriptBody(args = {"regex"}, body = "return new RandExp(new RegExp(regex))")
//    private static native Object createImpl(String regex);
//
//    @JavaScriptBody(args = {}, body = "")
//    public static native void init();
//
//    public String gen() {
//        return genImpl(this);
//    }
//
//    @JavaScriptBody(args = {"randexp"}, body = "return randexp.gen()")
//    private static native String genImpl(Object randexp);
}
