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
    ,@Property(name = "generatedCodeConfig", type = GeneratedCodeConfig.class)
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
    
    private static String unEscapeText(String value){
        return value.replaceAll("[\\\\]{2}", Matcher.quoteReplacement("\\")).replaceAll("\\\\\"", Matcher.quoteReplacement("\""));
    }

    @Function
    static void generateCode(RegexTesting model) {
        switch (model.getGeneratedCodeConfig().getSelectedLanguage()) {
            case JAVA:
                model.setGeneratedCode(generateJavaCode(model.getGeneratedCodeConfig().getSelectedOperation(), model.getRegexText(), model.getTestCase(), model.getReplacementText()));
                break;
            case KOTLIN:
                model.setGeneratedCode(generateKotlinCode(model.getGeneratedCodeConfig().getSelectedOperation(), model.getRegexText(), model.getTestCase(), model.getReplacementText()));
                break;
            default:
                break;
        }
    }

//    @ComputedProperty
//    static String generateCode(GeneratedCodeConfig generatedCodeConfig, String regexText, String testCase,String replacementText) {
//        switch (generatedCodeConfig.getSelectedLanguage()) {
//            case JAVA:
//                return generateJavaCode(generatedCodeConfig.getSelectedOperation(), regexText, testCase,replacementText);
//            case KOTLIN:
//                return generateKotlinCode(generatedCodeConfig.getSelectedOperation(), regexText, testCase,replacementText);
//            default:
//                return "";
//        }
//    }
    private static String generateKotlinCode(RegexOperation selectedOperation, String regexText, String testCase, String replacementText) {
        switch (selectedOperation) {
            case VALIDATION:
                return generateKotlinValidationCode(regexText, testCase);
            case SEARCHING:
                return generateKotlinSearchingCode(regexText, testCase);
            case SPLITTING:
                return generateKotlinSplittingCode(regexText, testCase);
            case REPLACEMENT:
                return generateKotlinReplacementCode(regexText, testCase, replacementText);
            default:
                return ""; //NOI18N
        }
    }

    private static String generateJavaCode(RegexOperation selectedOperation, String regexText, String testCase, String replacementText) {
        switch (selectedOperation) {
            case VALIDATION:
                return generateJavaValidationCode(regexText, testCase);
            case SEARCHING:
                return generateJavaSearchingCode(regexText, testCase);
            case SPLITTING:
                return generateJavaSplittingCode(regexText, testCase);
            case REPLACEMENT:
                return generateJavaReplacementCode(regexText, testCase, replacementText);
            default:
                return ""; //NOI18N
        }
    }

    private static String generateJavaSearchingCode(String regexText, String testCase) {
        return MessageFormat.format(
                java.util.ResourceBundle.getBundle("codegeneration").getString("JAVA_SEARCHING")
                , escapedRegexText(regexText), escapedRegexText(testCase));
    }

    private static String generateKotlinSearchingCode(String regexText, String testCase) {
        return MessageFormat.format(java.util.ResourceBundle.getBundle("codegeneration").getString("KOTLIN_SEARCHING"), escapedRegexText(regexText), escapedRegexText(testCase));
    }

    private static String generateJavaSplittingCode(String regexText, String testCase) {
        return MessageFormat.format(java.util.ResourceBundle.getBundle("codegeneration").getString("JAVA_SPLIT"), escapedRegexText(regexText), escapedRegexText(testCase));
    }

    private static String generateKotlinSplittingCode(String regexText, String testCase) {
        return MessageFormat.format(java.util.ResourceBundle.getBundle("codegeneration").getString("KOTLIN_SPLIT"), escapedRegexText(regexText), escapedRegexText(testCase));
    }

    private static String generateJavaReplacementCode(String regexText, String testCase, String replacementText) {
        return MessageFormat.format(java.util.ResourceBundle.getBundle("codegeneration").getString("JAVA_REPLACE"), escapedRegexText(regexText), escapedRegexText(testCase), replacementText);
    }

    private static String generateKotlinReplacementCode(String regexText, String testCase, String replacementText) {
        return MessageFormat.format(java.util.ResourceBundle.getBundle("codegeneration").getString("KOTLIN_REPLACE"), escapedRegexText(regexText), escapedRegexText(testCase), replacementText);
    }

    private static String generateJavaValidationCode(String regexText, String testCase) {
        String sb = MessageFormat.format(java.util.ResourceBundle.getBundle("codegeneration").getString("JAVA_VALIDATION"), escapedRegexText(regexText), escapedRegexText(testCase));
        return sb;
    }

    private static String generateKotlinValidationCode(String regexText, String testCase) {
        String sb = MessageFormat.format(java.util.ResourceBundle.getBundle("codegeneration").getString("KOTLIN_VALIDATION"), escapedRegexText(regexText), escapedRegexText(testCase));
        return sb;
    }
    
        static void setEscapedRegexText(RegexTesting model, String value) {
        try {
            String excaped= unEscapeText(value);
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
