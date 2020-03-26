package geolocator;

import java.net.URL;

import java.io.IOException;

import com.google.gson.Gson;

import com.google.common.net.UrlEscapers;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for obtaining geolocation of an IP address
 * The class uses the <a href="http://ip-api.com">IP-API.com </a> service
 */
public class GeoLocator {

    static Logger logger = LoggerFactory.getLogger(GeoLocator.class);

    /**
     * URI of the geolocation service
     */
    public static final String GEOLOCATOR_SERVICE_URI = "http://ip-api.com/json/";

    private static Gson GSON = new Gson();

    /**
     * Creates a <code>GeoLocator</code> object.
     */
    public GeoLocator() {}

    /**
     * Returns geolocation informationa bout the JVM running the application
     * @return an object wapping the geolcation information
     * @throws IOException if any I/O errors occur
     */
    public GeoLocation getGeoLocation() throws IOException {
        logger.info("Information about the geolocation of the JVM running the app sent");
        return getGeoLocation(null);
    }

    /**
     * Return geolocation informationa about the specified IP address or host name specified.
     * If the argument is <code>null</code> the method returns the geolocation information of the JVM running the application
     * @param ipAddrOrHost the IP address or host name, may be {@code null}
     * @return the object wrapping the geolcation information
     * @throws IOException if any I/O error occured
     */
    public GeoLocation getGeoLocation(String ipAddrOrHost) throws IOException {
        URL url;
        if (ipAddrOrHost != null) {
            logger.info("IP address or host name is found");
            ipAddrOrHost = UrlEscapers.urlPathSegmentEscaper().escape(ipAddrOrHost);
            url = new URL(GEOLOCATOR_SERVICE_URI + ipAddrOrHost);
            logger.debug("URL is {}", url);
            logger.debug("URL updated according to the specified IP address or the specified host name");
        } else {
            logger.info("IP address or host name isn't found, program will use the geolocation of the JVM running the application");
            url = new URL(GEOLOCATOR_SERVICE_URI);
            logger.debug("URL is {}", url);
            logger.info("URL updated according to the JVM running application");
        }
        String s = IOUtils.toString(url, "UTF-8");
        logger.info("Returning geolocation according to the updated URL");
        return GSON.fromJson(s, GeoLocation.class);
    }

    public static void main(String[] args) throws IOException {
        try {
            String arg = args.length > 0 ? args[0] : null;
            logger.debug("Argument is {}", arg);
            logger.info("Location saved if there was user input");
            GeoLocation geoLocation = new GeoLocator().getGeoLocation(arg);

            System.out.println(geoLocation.toString());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
