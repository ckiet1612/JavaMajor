package com.orbital.service;

import javafx.geometry.Point3D;
import org.springframework.stereotype.Service;

@Service
public class PhysicsService {

    public static final double EARTH_RADIUS_KM = 6371.0;
    // Earth's standard gravitational parameter, in km^3/s^2.
    public static final double MU = 398600.4418;

    /**
     * Gives the speed needed for a simple circular orbit.
     */
    public double calculateOrbitalVelocity(double altitudeKm) {
        double r = EARTH_RADIUS_KM + altitudeKm;
        return Math.sqrt(MU / r);
    }

    /**
     * Converts an orbit angle into a 3D point around Earth.
     */
    public Point3D calculatePosition(double altitudeKm, double angleRads, double inclinationDeg) {
        double r = EARTH_RADIUS_KM + altitudeKm;
        double incRads = Math.toRadians(inclinationDeg);

        // Basic circular orbit formula, tilted by inclination.
        double x = r * Math.cos(angleRads);
        double y = r * Math.sin(angleRads) * Math.sin(incRads);
        double z = r * Math.sin(angleRads) * Math.cos(incRads);

        return new Point3D(x, y, z);
    }

    /**
     * Converts linear orbital speed into radians per second.
     */
    public double calculateAngularVelocity(double velocityKmS, double altitudeKm) {
        double r = EARTH_RADIUS_KM + altitudeKm;
        return velocityKmS / r;
    }
}
