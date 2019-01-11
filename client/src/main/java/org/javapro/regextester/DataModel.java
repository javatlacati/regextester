package org.javapro.regextester;

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
        Pattern pattern = Pattern.compile(regexText);
        return pattern.matcher(testCase).replaceAll(replacementText);
    }

    @ComputedProperty(write = "setEscapedRegexText")
    static String escapedRegexText(String regexText) {
        return regexText.replaceAll("[\\\\]", "\\\\\\\\").replaceAll("\"", "\\\\\"");
    }

    @Function
    void generateCode(RegexTesting model) {
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
                return "";
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
                return "";
        }
    }

    private static String generateJavaSearchingCode(String regexText, String testCase) {
        return new StringBuilder("List<String> allGroups = new ArrayList<>();\n")
                .append("try {\n")
                .append("   Matcher m = Pattern.compile(\"")
                .append(escapedRegexText(regexText))
                .append("\").matcher(\"")
                .append(escapedRegexText(testCase))
                .append("\");\n")
                .append("   int groupsNum = m.groupCount();\n")
                .append("   while (m.find()) {\n")
                .append("       for (int i = 1; i <= groupsNum; i++) {\n")
                .append("           try {\n")
                .append("               String group = m.group(i);\n")
                .append("               allGroups.add(group);\n")
                .append("           } catch (IllegalStateException ise) {\n")
                .append("               allGroups.add(\"No Matches\");\n")
                .append("           }\n")
                .append("       }\n")
                .append("   }\n")
                .append("} catch (PatternSyntaxException pse) {\n")
                .append("   allGroups.add(pse.getMessage());\n")
                .append("}")
                .toString();

    }

    private static String generateKotlinSearchingCode(String regexText, String testCase) {
        return new StringBuilder("val allGroups = ArrayList()\n")
                .append("try {\n")
                .append("    val m = Pattern.compile(\"")
                .append(escapedRegexText(regexText))
                .append("\").matcher(\"")
                .append(escapedRegexText(testCase))
                .append("\")\n")
                .append("   val groupsNum = m.groupCount()\n")
                .append("   while (m.find()) {\n")
                .append("       for (i in 1..groupsNum) {\n")
                .append("           try {\n")
                .append("               val group = m.group(i)\n")
                .append("               allGroups.add(group)\n")
                .append("           } catch (ise:IllegalStateException) {\n")
                .append("               allGroups.add(\"No Matches\")\n")
                .append("           }\n")
                .append("       }\n")
                .append("   }\n")
                .append("} catch (pse:PatternSyntaxException) {\n")
                .append("   allGroups.add(pse.getMessage())\n")
                .append("}")
                .toString();
    }

    private static String generateJavaSplittingCode(String regexText, String testCase) {
        return new StringBuilder("Pattern pattern = Pattern.compile(\"")
                .append(escapedRegexText(regexText))
                .append("\");\nString[] splitted= pattern.split(\"")
                .append(escapedRegexText(testCase))
                .append("\",0);")
                .toString();
    }

    private static String generateKotlinSplittingCode(String regexText, String testCase) {
        return new StringBuilder("var pattern = Pattern.compile(\"")
                .append(escapedRegexText(regexText))
                .append("\")\nvar splitted= pattern.split(\"")
                .append(escapedRegexText(testCase))
                .append("\",0)")
                .toString();
    }

    private static String generateJavaReplacementCode(String regexText, String testCase, String replacementText) {
        return new StringBuilder("Pattern pattern = Pattern.compile(\"")
                .append(escapedRegexText(regexText))
                .append("\");\nString replaced = pattern.matcher(\"")
                .append(escapedRegexText(testCase))
                .append("\").replaceAll(")
                .append(replacementText)
                .append(");").toString();
    }

    private static String generateKotlinReplacementCode(String regexText, String testCase, String replacementText) {
        return new StringBuilder("var pattern = Pattern.compile(\"")
                .append(escapedRegexText(regexText))
                .append("\")\nvar replaced = pattern.matcher(\"")
                .append(escapedRegexText(testCase))
                .append("\").replaceAll(")
                .append(replacementText)
                .append(")").toString();
    }

    private static String generateJavaValidationCode(String regexText, String testCase) {
        StringBuilder sb
                = new StringBuilder("Pattern pattern = Pattern.compile(\"")
                        .append(escapedRegexText(regexText))
                        .append("\");\nboolean isValid = pattern.matcher(\"")
                        .append(escapedRegexText(testCase))
                        .append("\").matches();");
        return sb.toString();
    }

    private static String generateKotlinValidationCode(String regexText, String testCase) {
        StringBuilder sb
                = new StringBuilder("var pattern = Pattern.compile(\"")
                        .append(escapedRegexText(regexText))
                        .append("\")\nvar isValid = pattern.matcher(\"")
                        .append(escapedRegexText(testCase))
                        .append("\").matches()");
        return sb.toString();
    }

    static void setEscapedRegexText(RegexTesting model, String value) {
        try {
            String excaped = value.replaceAll("[\\\\]{2}", Matcher.quoteReplacement("\\")).replaceAll("\\\\\"", Matcher.quoteReplacement("\""));
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
