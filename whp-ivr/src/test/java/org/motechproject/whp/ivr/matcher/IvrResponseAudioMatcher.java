package org.motechproject.whp.ivr.matcher;


import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeMatcher;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class IvrResponseAudioMatcher extends TypeSafeMatcher<List<String>> {

    private List<String> expectedAudioList;

    public IvrResponseAudioMatcher(List<String> expectedAudioList) {
        this.expectedAudioList = expectedAudioList;
    }

    @Factory
    public static <T> org.hamcrest.Matcher audioList(List<String>... audioList) {
        return new IvrResponseAudioMatcher(merge(audioList));
    }

    @Override
    protected boolean matchesSafely(List<String> actualPrompts) {
        assertPrompts(actualPrompts);
        return true;
    }

    @Override
    public void describeTo(Description description) {
    }

    private static List<String> merge(List<String>... list) {
        List<String> audioFiles = new ArrayList<>();
        for (List<String> prompts : list) {
            audioFiles.addAll(prompts);
        }
        return audioFiles;
    }

    protected void assertPrompts(List<String> actualPrompts) {
        int index = actualPrompts.indexOf(expectedAudioList.get(0));
        for (int i = index; i < index + expectedAudioList.size(); i++) {
            assertThat(actualPrompts.get(i), is(expectedAudioList.get(i - index)));
        }
    }

}