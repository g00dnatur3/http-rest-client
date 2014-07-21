package test;

import java.io.IOException;

import org.apache.commons.io.IOUtils;

public class Settings {

    public static final String geocoderUrl = "http://maps.googleapis.com/maps/api/geocode/json";

    public static final String mockLocationHeader = "http://mock.org/created/1";

    public static final String geocodeJson;

    static {
	geocodeJson = loadMockJson("/geocode.json");
    }

    public static String loadMockJson(String path) {
	try {
	    return IOUtils.toString(Settings.class.getResourceAsStream(path));
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
    }
}
