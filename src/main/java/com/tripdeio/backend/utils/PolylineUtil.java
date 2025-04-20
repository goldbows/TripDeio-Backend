package com.tripdeio.backend.utils;

import java.util.List;

public class PolylineUtil {

    public static String encodePolyline(List<double[]> path) {
        StringBuilder encoded = new StringBuilder();
        long lastLat = 0;
        long lastLng = 0;

        for (double[] point : path) {
            long lat = Math.round(point[0] * 1e5);
            long lng = Math.round(point[1] * 1e5);

            long dLat = lat - lastLat;
            long dLng = lng - lastLng;

            encodeNumber(dLat, encoded);
            encodeNumber(dLng, encoded);

            lastLat = lat;
            lastLng = lng;
        }

        return encoded.toString();
    }

    private static void encodeNumber(long num, StringBuilder encoded) {
        num = num < 0 ? ~(num << 1) : num << 1;

        while (num >= 0x20) {
            encoded.append((char)((0x20 | (num & 0x1f)) + 63));
            num >>= 5;
        }
        encoded.append((char)(num + 63));
    }
}
