package org.javapro.regextester;

import net.java.html.junit.BrowserRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import org.javapro.regextester.js.PlatformServices;

import static org.junit.Assert.*;

/**
 * Tests for behavior of your application in isolation. Verify behavior of your
 * MVVC code in a unit test.
 */
@RunWith(BrowserRunner.class)
public class DataModelTest {
    
    private class MockedPLatformServices extends PlatformServices{

        @Override
        public Set<String> nPossibilities(String regexText) {
            return null; //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    
    @Test
    public void testNoGeneratedPossibilities() {
        RegexTesting model = new RegexTesting("", "", "", false, false, false, false);
        PlatformServices mockedServices =new MockedPLatformServices();
        model.setPreferences(mockedServices);
        DataModel dm= new DataModel();
        dm.setPreferences(model, mockedServices);
        dm.nPossibilities(model);
        assertEquals(0, model.getPossibilities().size());
        assertEquals(mockedServices, dm.getServices());
        assertTrue(model.isMatches());
    }

    @Test
    public void testEscaping() {
        RegexTesting model = new RegexTesting("f.o,bar,hello,world", "f\\.o,(bar)", "$1", false, false, false, false);
        model.setTestCase("123");
        model.setReplacementText("");
        DataModel.setEscapedRegexText(model, "\\\\d+");
        assertEquals("\\d+", model.getRegexText());
        assertTrue(model.isMatches());
    }

    /**
     * Tests error message displaying when replacement is requested for non existing group.
     * <br>
     * Input: <code>f.o,bar,hello,world</code><br>
     * Regex: <code>\d+</code><br>
     * Replacement: <code>$1</code><br>
     */
    @Test
    public void testFailedEscaping() {
        RegexTesting model = new RegexTesting("f.o,bar,hello,world", "f\\.o,(bar)", "$1", false, false, false, false);
        model.setTestCase("123");
        DataModel.setEscapedRegexText(model, "\\\\d+");
        assertEquals("No group 1", model.getRegexText());
        assertFalse(model.isMatches());
    }

    /**
     * Tests capture groups for searching.
     * <br>
     * Input: <code>f.o,bar,hello,world</code><br>
     * Regex: <code>f\.o,(bar)</code><br>
     * Replacement: <code>$1</code><br>
     * Expected replacement result: <code>bar,hello,world</code><br>
     * Capture groups matched: <code>[bar]</code>
     */
    @Test
    public void testCapturingGroup() {
        RegexTesting model = new RegexTesting("f.o,bar,hello,world", "f\\.o,(bar)", "$1", false, false, false, false);
        assertFalse(model.isMatches());
        assertEquals("bar,hello,world", model.getReplaced());
        assertEquals("f\\\\.o,(bar)", DataModel.escapedRegexText(model.getRegexText()));
        DataModel.allGroups(model);
        assertEquals(Collections.singletonList("bar"), model.getGroupsMatching());
    }

    @Test
    public void testGreedyOption() {
        RegexTesting model = new RegexTesting("humbapumpa jim", ".*(jim|joe).*", "", false, false, false, false);
        assertTrue(model.isMatches());
        DataModel.allGroups(model);
        assertEquals(Collections.singletonList("jim"), model.getGroupsMatching());
    }

    /**
     * Tests Unicode blocks in replacement.
     * <br>
     * Input: <code>abcdefghijkTYYtyyQ</code><br>
     * Regex: <code>(\p{Lu})</code><br>
     * Replacement: <code>_</code><br>
     * Expected replacement result: <code>abcdefghijk___tyy_</code>
     */
    @Test
    public void testUnicode() {
        RegexTesting model = new RegexTesting("abcdefghijkTYYtyyQ", "(\\p{Lu})", "_", true, false, false, false);
        assertFalse(model.isMatches());
        assertEquals("(\\\\p{Lu})", DataModel.escapedRegexText(model.getRegexText()));
        assertEquals("abcdefghijk___tyy_", model.getReplaced());
        DataModel.allGroups(model);
        assertEquals(Arrays.asList("T", "Y", "Y", "Q"), model.getGroupsMatching());
    }

    /**
     * Tests look ahead in replacement.
     * <br>
     * Input: <code>abcdefghijkTYYtyyQ</code><br>
     * Regex: <code>(?=\p{Lu})</code><br>
     * Replacement: <code>_</code><br>
     * Expected replacement result: <code>abcdefghijk_T_Y_Ytyy_Q</code>
     */
    @Test
    public void testUnicodeLookahead() {
        RegexTesting model = new RegexTesting("abcdefghijkTYYtyyQ", "(?=\\p{Lu})", "_", true, false, false, false);
        assertFalse(model.isMatches());
        assertEquals("(?=\\\\p{Lu})", DataModel.escapedRegexText(model.getRegexText()));
        assertEquals("abcdefghijk_T_Y_Ytyy_Q", model.getReplaced());
        DataModel.allGroups(model);
        assertEquals(Collections.emptyList(), model.getGroupsMatching());
    }
}
