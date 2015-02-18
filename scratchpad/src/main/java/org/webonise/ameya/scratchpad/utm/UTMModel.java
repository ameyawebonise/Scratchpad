package org.webonise.ameya.scratchpad.utm;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created by Webonise on 18/02/15.
 */
public class UTMModel {

    private Double utmX;
    private Double utmY;


    public UTMModel(Double x,Double y){
        this.utmX = x;
        this.utmY = y;
    }

    public Double getUtmX() {
        return utmX;
    }

    public void setUtmX(Double utmX) {
        this.utmX = utmX;
    }

    public Double getUtmY() {
        return utmY;
    }

    public void setUtmY(Double utmY) {
        this.utmY = utmY;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
