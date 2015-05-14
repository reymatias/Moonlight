package helios.moonlight_android;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by roshiniThiagarajan on 5/10/15.
 */
public class lostBikeLatLng {
    public String id;
    public LatLng latLng;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

}
