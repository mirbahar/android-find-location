package com.maps.rahat.mapswithlocation;

import java.io.Serializable;

/**
 * Created by rahat on 2/5/17.
 */

public class Address{

    private String placeName;
    private String vicinity;

    public Address(String placeName, String vicinity) {
        this.placeName = placeName;
        this.vicinity = vicinity;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }
}
