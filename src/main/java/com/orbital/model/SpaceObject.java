package com.orbital.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SpaceObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String name;
    private String type; // SATELLITE for now, but this leaves room for stations later.
    
    private double latitude; // degrees
    private double longitude; // degrees
    private double altitude; // km

    private double velocity; // km/s

    // The animation keeps this angle in radians.
    private double currentAngle;
    
    // Orbit tilt in degrees.
    private double inclination;

    private Double size = 0.3;
    private String colorHex = "#FFA500";

    public SpaceObject(String name, String type, double latitude, double longitude, double altitude, double inclination) {
        this.name = name;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.inclination = inclination;
        this.currentAngle = Math.toRadians(longitude); // Start at the saved longitude.
    }
}
