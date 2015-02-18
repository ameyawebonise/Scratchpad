package org.webonise.ameya.scratchpad.utm;

import org.jscience.geography.coordinates.LatLong;
import org.jscience.geography.coordinates.UTM;
import org.jscience.geography.coordinates.crs.ReferenceEllipsoid;

import javax.measure.converter.UnitConverter;
import javax.measure.quantity.Angle;
import javax.measure.quantity.Length;
import javax.measure.quantity.Pressure;
import javax.measure.unit.Unit;
import java.util.Collections;
import java.util.Map;
import static javax.measure.unit.SI.*;

import static javax.measure.unit.SI.HECTO;
import static javax.measure.unit.SI.METER;

/**
 * Created by Webonise on 18/02/15.
 */
public class UTMUtils {


    public UTMModel convertWSGtoUTM(double latitude, double longitude){
        Unit<Length> meters = METER;
        Unit<Angle> rads = RADIAN;
        LatLong latLon =  LatLong.valueOf(toRadians(latitude), toRadians(longitude),rads);
        UTM utm  = UTM.latLongToUtm(latLon, ReferenceEllipsoid.WGS84);
        return  new UTMModel(utm.eastingValue(meters),utm.northingValue(meters));


    }

    public Double toRadians(Double val){
        return val*Math.PI/180;
    }

}
