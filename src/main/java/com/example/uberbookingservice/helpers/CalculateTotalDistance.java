package com.example.uberbookingservice.helpers;

public class CalculateTotalDistance {
    public static double calculateDistance(
            double latitude1,
            double longitude1,
            double latitude2,
            double longitude2) {

        final double EARTH_RADIUS_KM = 6371.0;

        double latDistance = Math.toRadians(latitude2 - latitude1);
        double lonDistance = Math.toRadians(longitude2 - longitude1);

        double a = Math.pow(Math.sin(latDistance / 2), 2)
                + Math.cos(Math.toRadians(latitude1))
                * Math.cos(Math.toRadians(latitude2))
                * Math.pow(Math.sin(lonDistance / 2), 2);

        double c = 2 * Math.atan2(
                Math.sqrt(a),
                Math.sqrt(1 - a)
        );

        return EARTH_RADIUS_KM * c;
    }
}
