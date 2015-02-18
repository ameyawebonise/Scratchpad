package org.webonise.ameya.scratchpad.utm;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Webonise on 18/02/15.
 */
public class UTMUtilsTest {

    @Test
    public void test_WSGToUTM(){

        UTMModel expected =  new UTMModel(636048.019206825,4879612.49585956);
        UTMUtils utils =  new UTMUtils();
        UTMModel actual = utils.convertWSGtoUTM(44.0570565571113, -79.3014729022962);
        Assert.assertEquals(actual,expected);

    }

    /*

      <geoCoord2D lat="44.0512890068392" lng="-79.3036186695081" x="635889.338703761" y="4878968.34910823"/>
  <geoCoord2D lat="44.0495925616224" lng="-79.2957651615124" x="636522.351664571" y="4878792.90895392"/>
  <geoCoord2D lat="44.0561930024532" lng="-79.2954218387585" x="636534.684906553" y="4879526.59243869"/>
  <geoCoord2D lat="44.0570565571113" lng="-79.3014729022962" x="636048.019206825" y="4879612.49585956"/>

     */
}
