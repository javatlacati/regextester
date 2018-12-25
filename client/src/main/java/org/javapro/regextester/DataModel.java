package org.javapro.regextester;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import net.java.html.json.ComputedProperty;
import net.java.html.json.Function;
import net.java.html.json.Model;
import net.java.html.json.Property;
import net.java.html.json.ModelOperation;
import org.javapro.regextester.js.PlatformServices;
/** Model annotation generates class Data with
 * one message property, boolean property and read only words property
 */
@Model(targetId = "", className = "RegexTesting", instance = true, properties = {
        @Property(name = "testCase", type = String.class)
        , @Property(name = "regexText", type = String.class)
        , @Property(name = "replacementText", type = String.class)
        , @Property(name = "partialMatches", type = String.class, array = true)
        , @Property(name = "possibilities", type = String.class, array = true)
        , @Property(name = "groupsMatching", type = String.class, array = true)
        , @Property(name = "displayReplacement", type = boolean.class)
        , @Property(name = "displayMatches", type = boolean.class)
        , @Property(name = "displayGroups", type = boolean.class)
        , @Property(name = "displayGeneration", type = boolean.class)
})
final class DataModel {
    private PlatformServices services;

    public PlatformServices getServices() {
        return services;
    }

    @ComputedProperty
    static String replaced(String regexText, String testCase, String replacementText) {
        Pattern pattern = Pattern.compile(regexText);
        return pattern.matcher(testCase).replaceAll(replacementText);
    }

    @ComputedProperty(write = "setEscapedRegexText")
    static String escapedRegexText(String regexText) {
        return regexText.replaceAll("[\\\\]", "\\\\\\\\");
    }

    static void setEscapedRegexText(RegexTesting model, String value) {
        try {
            String excaped = value.replaceAll("[\\\\]{2}", Matcher.quoteReplacement("\\"));
            model.setRegexText(excaped);
        } catch (PatternSyntaxException | IndexOutOfBoundsException pse) {
            model.setRegexText(pse.getMessage());
        }
    }

//    @ComputedProperty
//    static String generateExample(String regexText) {
//        Generex generex = new Generex(regexText);
//        return generex.random();
//    }

    @ComputedProperty
    static boolean matches(String regexText, String testCase) {
        //return testCase.matches(regexText);
        Pattern pattern = Pattern.compile(regexText);
        return pattern.matcher(testCase).matches();
    }

    @Function
    static void allMatches(RegexTesting model) {
        List<String> allMatches = new ArrayList<>();
        try {
            Matcher m = Pattern.compile(model.getRegexText()).matcher(model.getTestCase());
            while (m.find()) {
                allMatches.add(m.group());
            }
        } catch (PatternSyntaxException pse) {
            allMatches.add(pse.getMessage());
        }
        model.getPartialMatches().clear();
        model.getPartialMatches().addAll(allMatches);
    }

    @Function
    static void allGroups(RegexTesting model) {
        List<String> allGroups = new ArrayList<>();
        try {
            Matcher m = Pattern.compile(model.getRegexText()).matcher(model.getTestCase());

            int groupsNum = m.groupCount();
            while (m.find()) {
                for (int i = 1; i <= groupsNum; i++) {
                    try {
                        String group = m.group(i);
                        allGroups.add(group);
                    } catch (IllegalStateException ise) {
                        allGroups.add("No Matches");
                    }
                }
            }

        } catch (PatternSyntaxException pse) {
            allGroups.add(pse.getMessage());
        }
        model.getGroupsMatching().clear();
        model.getGroupsMatching().addAll(allGroups);
    }

    @Function
    void nPossibilities(RegexTesting model) {
        Set<String> allPossibilities = services.nPossibilities(model.getRegexText());
        if (null == allPossibilities) {
            allPossibilities = new HashSet<>();
        }
        model.getPossibilities().clear();
        model.getPossibilities().addAll(allPossibilities);
    }

    @Function
    void openWebBrowser(RegexTesting model, String data) { //, PlatformServices services
        services.openWebBrowser(data);
    }

    @ModelOperation
    public void setPreferences(RegexTesting model, PlatformServices services) {
        this.services = services;
    }

    /**
     * Called when the page is ready.
     */
    static void onPageLoad(PlatformServices services) {
        RegexTesting model = new RegexTesting("", "", "", false, false, false, false);
        model.setPreferences(services);
        model.applyBindings();
    }
}
