package nl.praegus.fitnesse.responders.allTags;

import fitnesse.wiki.WikiPage;
import fitnesse.wiki.WikiPageDummy;
import fitnesse.wiki.WikiPageProperty;
import fitnesse.wiki.WikiSourcePage;
import org.junit.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class AllTagsResponderTest {

    @Test
    public void check_If_Response_Is_Equal_To_Expected_List_With_One_Page() {
        AllTagsResponder tagResponder = new AllTagsResponder();
        WikiPageProperty testTagProperty = new WikiPageProperty();
        testTagProperty.set("Suites", "testTag");
        TestWikiPageDummy testWikiPageDummy = new TestWikiPageDummy("dummyPage", "", new WikiPageDummy(), testTagProperty);
        WikiSourcePage testWikiSourcePage = new WikiSourcePage(testWikiPageDummy);

        Set<String> receivedValue = tagResponder.getPageTags(testWikiSourcePage);

        assertThat(receivedValue).isEqualTo(new HashSet<>(Collections.singletonList("testTag")));
    }

    @Test
    public void check_If_Response_Is_Equal_To_Expected_List_With_Children() {
        AllTagsResponder tagResponder = new AllTagsResponder();
        WikiPageProperty testTagPropertyMainSuite = new WikiPageProperty();
        testTagPropertyMainSuite.set("Suites", "mainTag");
        WikiPageProperty testTagPropertyChildSuite = new WikiPageProperty();
        testTagPropertyChildSuite.set("Suites", "childTag");
        TestWikiPageDummy testWikiPageDummyChild = new TestWikiPageDummy("childPageTest", "", new WikiPageDummy(), testTagPropertyChildSuite);
        List<WikiPage> children = Collections.singletonList(testWikiPageDummyChild);
        TestWikiPageDummy testWikiPageDummy = new TestWikiPageDummy("dummyPage", "", new WikiPageDummy(), testTagPropertyMainSuite, children);
        WikiSourcePage testWikiSourcePage = new WikiSourcePage(testWikiPageDummy);

        Set<String> receivedValue = tagResponder.getPageTags(testWikiSourcePage);

        assertThat(receivedValue).isEqualTo(new HashSet<>(Arrays.asList("mainTag", "childTag")));
    }

    @Test
    public void check_If_Response_Is_Equal_To_Expected_List_With_EmptyChildren() {
        AllTagsResponder tagResponder = new AllTagsResponder();
        WikiPageProperty testTagPropertyMainSuite = new WikiPageProperty();
        testTagPropertyMainSuite.set("Suites", "mainTag");
        TestWikiPageDummy testWikiPageDummyChild = new TestWikiPageDummy("childPageTest", "", new WikiPageDummy(), new WikiPageProperty());
        List<WikiPage> children = Collections.singletonList(testWikiPageDummyChild);
        TestWikiPageDummy testWikiPageDummy = new TestWikiPageDummy("dummyPage", "", new WikiPageDummy(), testTagPropertyMainSuite, children);
        WikiSourcePage testWikiSourcePage = new WikiSourcePage(testWikiPageDummy);

        Set<String> receivedValue = tagResponder.getPageTags(testWikiSourcePage);

        assertThat(receivedValue).isEqualTo(new HashSet<>(Collections.singletonList("mainTag")));
    }

    @Test
    public void check_If_Response_Is_Equal_To_Empty_If_TagList_Is_Empty() {
        AllTagsResponder tagResponder = new AllTagsResponder();
        TestWikiPageDummy testWikiPageDummy = new TestWikiPageDummy("dummyPage", "", new WikiPageDummy(), new WikiPageProperty());
        WikiSourcePage testWikiSourcePage = new WikiSourcePage(testWikiPageDummy);

        Set<String> receivedValue = tagResponder.getPageTags(testWikiSourcePage);

        assertThat(receivedValue).isEmpty();
    }
}

