package org.javapro.regextester;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

/**
 * Data model for holding application data.
 */
@Model(targetId = "", className = "RegexTesting", instance = true, properties = {
        @Property(name = "testCase", type = String.class)
        , @Property(name = "regexText", type = String.class)
        , @Property(name = "replacementText", type = String.class)
        , @Property(name = "generatedCodeConfig", type = GeneratedCodeConfig.class)
        , @Property(name = "generatedCode", type = String.class)
        , @Property(name = "partialMatches", type = String.class, array = true)
        , @Property(name = "possibilities", type = String.class, array = true)
        , @Property(name = "groupsMatching", type = String.class, array = true)
        , @Property(name = "displayReplacement", type = boolean.class)
        , @Property(name = "displayMatches", type = boolean.class)
        , @Property(name = "displayGroups", type = boolean.class)
        , @Property(name = "displayGeneration", type = boolean.class)
        , @Property(name = "displaySampleCode", type = boolean.class)
        , @Property(name = "languages", type = JavaBasedLanguage.class, array = true)
        , @Property(name = "operations", type = RegexOperation.class, array = true)
})
final class DataModel {

    /**
     * Platform dependent services.
     */
    private PlatformServices services;

    public PlatformServices getServices() {
        return services;
    }

    @ComputedProperty
    static String replaced(String regexText, String testCase, String replacementText) {
        try {
            Pattern pattern = Pattern.compile(regexText);
            return pattern.matcher(testCase).replaceAll(replacementText);
        } catch (IndexOutOfBoundsException ioobe) {
            return ioobe.getMessage();
        }
    }

    @ComputedProperty(write = "setEscapedRegexText")
    static String escapedRegexText(String regexText) {
        return regexText.replaceAll("[\\\\]", "\\\\\\\\").replaceAll("\"", "\\\\\""); //NOI18N
    }

    private static String unEscapeText(String value) {
        return value.replaceAll("[\\\\]{2}", Matcher.quoteReplacement("\\")).replaceAll("\\\\\"", Matcher.quoteReplacement("\""));
    }

    @Function
    static void generateCode(RegexTesting model) {
        RegexOperation selectedOperation = model.getGeneratedCodeConfig().getSelectedOperation();
        String regexText = model.getRegexText();
        String testCase = model.getTestCase();
        String replacementText = model.getReplacementText();
        switch (model.getGeneratedCodeConfig().getSelectedLanguage()) {
            case JAVA: {
                generateCodeForPlatform(model, selectedOperation, regexText, testCase, replacementText, "JAVA_VALIDATION", "JAVA_SEARCHING", "JAVA_SPLIT", "JAVA_REPLACE");
                break;
            }
            case KOTLIN: {
                generateCodeForPlatform(model, selectedOperation, regexText, testCase, replacementText, "KOTLIN_VALIDATION", "KOTLIN_SEARCHING", "KOTLIN_SPLIT", "KOTLIN_REPLACE");
                break;
            }
            case GROOVY: {
                generateCodeForPlatform(model, selectedOperation, regexText, testCase, replacementText, "GROOVY_VALIDATION", "GROOVY_SEARCHING", "GROOVY_SPLIT", "GROOVY_REPLACE");
                break;
            }
            default:
                break;
        }
    }

    private static void generateCodeForPlatform(RegexTesting model, RegexOperation selectedOperation, String regexText, String testCase, String replacementText, String validationKey, String searchingKey, String splitKey, String replaceKey) {
        String generatedCode;
        switch (selectedOperation) {
            case VALIDATION: {
                generatedCode = MessageFormat.format(java.util.ResourceBundle.getBundle("codegeneration").getString(validationKey), escapedRegexText(regexText), escapedRegexText(testCase));
                break;
            }
            case SEARCHING: {
                generatedCode = MessageFormat.format(
                        java.util.ResourceBundle.getBundle("codegeneration").getString(searchingKey)
                        , escapedRegexText(regexText), escapedRegexText(testCase));
                break;
            }
            case SPLITTING: {
                generatedCode = MessageFormat.format(java.util.ResourceBundle.getBundle("codegeneration").getString(splitKey), escapedRegexText(regexText), escapedRegexText(testCase));
                break;
            }
            case REPLACEMENT: {
                generatedCode = MessageFormat.format(java.util.ResourceBundle.getBundle("codegeneration").getString(replaceKey), escapedRegexText(regexText), escapedRegexText(testCase), replacementText);
                break;
            }
            default: {
                generatedCode = ""; //NOI18N
                break;
            }
        }

        model.setGeneratedCode(generatedCode);
    }

    static void setEscapedRegexText(RegexTesting model, String value) {
        try {
            String excaped = unEscapeText(value);
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

    /*@Function
    static void supportedLanguages(RegexTesting model){
        model.getLanguages().clear();
        
    }*/
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
        RegexTesting model = new RegexTesting("", "", "", new GeneratedCodeConfig(JavaBasedLanguage.JAVA, RegexOperation.VALIDATION), "", false, false, false, false, false);
        model.setPreferences(services);
        model.applyBindings();
        List<JavaBasedLanguage> languages = JavaBasedLanguage.supportedLanguages();
        model.getLanguages().addAll(languages);
        model.getOperations().addAll(Arrays.asList(RegexOperation.values()));
    }
}
