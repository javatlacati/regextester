package org.javapro.regextester;

import net.java.html.junit.BrowserRunner;
import net.java.html.junit.HTMLContent;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for behavior of your application in real systems. The {@link BrowserRunner}
 * selects all possible presenters from your <code>pom.xml</code> and
 * runs the tests inside of them. By default there are two:
 * <ul>
 * <li>JavaFX WebView presenter - verifies behavior in HotSpot JVM</li>
 * <li>Bck2Brwsr presenter - runs the test in a pluginless browser</li>
 * </ul>
 * <p>
 * See your <code>pom.xml</code> dependency section for details.
 */
@RunWith(BrowserRunner.class)
@HTMLContent(
        "<!DOCTYPE html>\n" +
                "<html>\n" +
                "    <head>\n" +
                "        <title>Regex tester</title>\n" +
                "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +

                "    </head>\n" +
                "    <body>\n" +
                "    <row>\n" +
                "        <column class=\"desktop-10\">\n" +
                "            <label>Test case</label>\n" +
                "            <textarea\n" +
                "                data-bind=\"value: testCase, valueUpdate: 'afterkeydown'\"\n" +
                "                placeholder=\"Write your test case text...\">\n" +
                "            </textarea>\n" +
                "        </column>\n" +
                "        <column class=\"desktop-10\">\n" +
                "            <label>Regex</label>\n" +
                "            <textarea\n" +
                "                data-bind=\"value: regexText, valueUpdate: 'afterkeydown'\"\n" +
                "                placeholder=\"Write your Java RegExp here...\">\n" +
                "            </textarea>\n" +
                "        </column>\n" +
                "    </row>\n" +
                "    <row>\n" +
                "        <column class=\"desktop-5 is-center\">\n" +
                "            <pre data-bind=\"text: matches() ? 'Match':'No match', valueUpdate: 'input',style: { color: matches() ? 'green' : 'red' }\"></pre>\n" +
                "            <label><input type=\"checkbox\" data-bind=\"checked: displayReplacement\" /> Test Regex Replacement</label>\n" +
                "        </column>\n" +
                "    </row>\n" +
                "\n" +
                "    <row>\n" +
                "        <column data-bind=\"if: displayReplacement\">\n" +
                "            <label>replacement regex</label>\n" +
                "            <textarea\n" +
                "                data-bind=\"value: replacementText, valueUpdate: 'afterkeydown'\"\n" +
                "                placeholder=\"Write the replacement regex...\">\n" +
                "            </textarea>\n" +
                "            <br>\n" +
                "            <pre data-bind=\"text: replaced\"></pre>\n" +
                "        </column>\n" +
                "    </row>\n" +
                "    <!-- ${browser.bootstrap} -->\n" +
                "</body>\n" +
                "</html>\n" +
                "\n"
)
public class IntegrationTest {
    @Test
    public void testUIModelUI() {
        RegexTesting model = new RegexTesting("f.o,bar,hello,world", ",", "_", false, false, false,false);
        model.applyBindings();
    }
}
