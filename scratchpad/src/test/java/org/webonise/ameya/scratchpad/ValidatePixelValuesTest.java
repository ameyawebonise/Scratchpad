package org.webonise.ameya.scratchpad;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ValidatePixelValuesTest {
    public static float DEFAULT_SCALE = 1.25f;
    public static int DEFAULT_SCREEN_WIDTH = 1008;
    public static Double DELTA = 0.001;

    @Test
    public void validateSetOne() throws Exception {
        int x = 513;
        int y = 384;
        float expectedXValue = 1251;
        float expectedYValue = 1029;

        assertEquals(expectedXValue, getCalculatedXValue(x), DELTA);
        assertEquals(expectedYValue, getCalculatedYValue(y), DELTA);
    }

    @Test
    public void validateSetTwo() throws Exception {
        int x = 471;
        int y = 256;
        float expectedXValue = 1118;
        float expectedYValue = 628;

        assertEquals(expectedXValue, getCalculatedXValue(x), DELTA);
        assertEquals(expectedYValue, getCalculatedYValue(y), DELTA);
    }

    @Test
    public void validateSetThree() throws Exception {
        int x = 648;
        int y = 421;
        float expectedXValue = 1674;
        float expectedYValue = 1147;

        assertEquals(expectedXValue, getCalculatedXValue(x), DELTA);
        assertEquals(expectedYValue, getCalculatedYValue(y), DELTA);
    }

    public float getCalculatedXValue(int x) {
        return DEFAULT_SCALE * (x + (DEFAULT_SCREEN_WIDTH/2));
    }

    public float getCalculatedYValue(int y) {
        return DEFAULT_SCALE * ((-1 * y) + (DEFAULT_SCREEN_WIDTH/2));
    }
}
