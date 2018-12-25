package org.javapro.regextester;

import net.java.html.junit.BrowserRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

/**
 * Tests for behavior of your application in isolation. Verify behavior of your
 * MVVC code in a unit test.
 */
@RunWith(BrowserRunner.class)
public class DataModelTest {
    
    @Test
    public void testEscaping() {
        RegexTesting model = new RegexTesting("f.o,bar,hello,world", "f\\.o,(bar)", "$1", false, false, false, false);
        model.setTestCase("123");
        model.setReplacementText("");
        DataModel.setEscapedRegexText(model, "\\\\d+");
        assertEquals("\\d+",model.getRegexText());
        assertTrue(model.isMatches());
    }
    
    @Test
    public void testFailedEscaping() {
        RegexTesting model = new RegexTesting("f.o,bar,hello,world", "f\\.o,(bar)", "$1", false, false, false, false);
        model.setTestCase("123");
        DataModel.setEscapedRegexText(model, "\\\\d+");
        assertEquals("No group 1",model.getRegexText());
        assertFalse(model.isMatches());
    }

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

    @Test
    public void testUnicode() {
        RegexTesting model = new RegexTesting("abcdefghijkTYYtyyQ", "(\\p{Lu})", "_", true, false, false, false);
        assertFalse(model.isMatches());
        assertEquals("(\\\\p{Lu})", DataModel.escapedRegexText(model.getRegexText()));
        assertEquals("abcdefghijk___tyy_", model.getReplaced());
        DataModel.allGroups(model);
        assertEquals(Arrays.asList("T","Y","Y","Q"), model.getGroupsMatching());
    }

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
