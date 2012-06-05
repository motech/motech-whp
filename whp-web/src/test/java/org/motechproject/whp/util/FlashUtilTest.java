package org.motechproject.whp.util;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class FlashUtilTest {

    @Mock
    HttpServletRequest request;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void shouldFlashOutAllMessagesInList_ByAppendingToBaseNameToCreateAttributes() {
        FlashUtil.flashAllOut("baseName", Arrays.asList("message1", "message2"), request);

        verify(request).setAttribute("flash.out.baseName0", "message1");
        verify(request).setAttribute("flash.out.baseName1", "message2");
    }

    @Test
    public void shouldFlashInAllMessages_TillAttributePrefixedByBaseNameInRequestObjectInEmpty() {
        when(request.getAttribute("flash.in.baseName0")).thenReturn("message1");
        when(request.getAttribute("flash.in.baseName1")).thenReturn("message2");
        when(request.getAttribute("flash.in.baseName2")).thenReturn("");

        List<String> messages = FlashUtil.flashAllIn("baseName", request);

        assertArrayEquals(new String[] {"message1", "message2"}, messages.toArray());
    }

}
