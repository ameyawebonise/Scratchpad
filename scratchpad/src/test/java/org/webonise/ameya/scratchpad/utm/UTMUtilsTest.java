package org.webonise.ameya.scratchpad.utm;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Webonise on 18/02/15.
 */
public class UTMUtilsTest {

    @Test
    public void test_WSGToUTM(){

        UTMModel expected =  new UTMModel(635949.412877697,4879391.13270945);
        UTMUtils utils =  new UTMUtils();
        UTMModel actual = utils.convertWSGtoUTM(44.0550826993879, -79.3027603626233);
        Assert.assertEquals(actual,expected);

    }
}
