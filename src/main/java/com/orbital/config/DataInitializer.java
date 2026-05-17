package com.orbital.config;

import com.orbital.model.SpaceObject;
import com.orbital.repository.SpaceObjectRepository;
import com.orbital.service.PhysicsService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final SpaceObjectRepository repository;
    private final PhysicsService physicsService;

    public DataInitializer(SpaceObjectRepository repository, PhysicsService physicsService) {
        this.repository = repository;
        this.physicsService = physicsService;
    }

    @Override
    public void run(String... args) {
        if (repository.count() == 0) {
            System.out.println("Initializing Demo Database with 10 Satellites...");

            List<SpaceObject> demoObjects = Arrays.asList(
                // GEO satellites sit high enough to cover large regions.
                createSat("GEO-Asia", 0, 35000, 0, 0.8, "#FF3333"),
                createSat("GEO-America", 120, 35000, 0, 0.8, "#FF3333"),
                createSat("GEO-Europe", 240, 35000, 0, 0.8, "#FF3333"),

                // LEO satellites are close to Earth, so this chain moves fast.
                createSat("Starlink-1", 0, 400, 0, 0.3, "#33CCFF"),
                createSat("Starlink-2", 30, 400, 0, 0.3, "#33CCFF"),
                createSat("Starlink-3", 60, 400, 0, 0.3, "#33CCFF"),
                createSat("Starlink-4", 90, 400, 0, 0.3, "#33CCFF"),
                createSat("Starlink-5", 120, 400, 0, 0.3, "#33CCFF"),

                // MEO satellites are a simple GPS-like example with tilted orbits.
                createSat("GPS-Alpha", 45, 20200, 55, 0.5, "#FFFF33"),
                createSat("GPS-Beta", 180, 20200, 55, 0.5, "#FFFF33")
            );

            repository.saveAll(demoObjects);
            System.out.println("Demo Database initialized successfully.");
        }
    }

    private SpaceObject createSat(String name, double longitude, double altitude, double inclination, double size, String color) {
        SpaceObject obj = new SpaceObject(name, "SATELLITE", 0, longitude, altitude, inclination);
        obj.setSize(size);
        obj.setColorHex(color);
        obj.setVelocity(physicsService.calculateOrbitalVelocity(altitude));
        return obj;
    }
}
