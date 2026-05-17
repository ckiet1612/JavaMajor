package com.orbital.controller;

import com.orbital.model.SpaceObject;
import com.orbital.service.PhysicsService;
import javafx.animation.AnimationTimer;
import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

final class OrbitSceneManager {

    // Scale real distances down so the scene fits on screen.
    private static final double SCALE = 0.001;
    private static final Color SCENE_BACKGROUND = Color.web("#03060A");
    private static final Color HIGHLIGHT_COLOR = Color.web("#6AA6AD");
    private static final Color ROUTE_COLOR = Color.web("#C6A15B");

    private final PhysicsService physicsService;
    private final StackPane sceneContainer;
    private final Runnable sceneClickHandler;
    private final Map<SpaceObject, Sphere> visualObjects = new HashMap<>();
    private final Map<SpaceObject, Sphere> highlightSpheres = new HashMap<>();
    private final Map<SpaceObject, Group> highlightOrbits = new HashMap<>();
    private final List<Cylinder> routeLines = new ArrayList<>();

    private Group root3D;
    private SubScene subScene;
    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;
    private double velocityX;
    private double velocityY;
    private boolean dragging;

    OrbitSceneManager(PhysicsService physicsService, StackPane sceneContainer, Runnable sceneClickHandler) {
        this.physicsService = physicsService;
        this.sceneContainer = sceneContainer;
        this.sceneClickHandler = sceneClickHandler;
    }

    void initialize() {
        root3D = new Group();

        Sphere earth = new Sphere(PhysicsService.EARTH_RADIUS_KM * SCALE);
        PhongMaterial earthMaterial = new PhongMaterial();
        try {
            earthMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("/earth.jpg")));
        } catch (Exception e) {
            earthMaterial.setDiffuseColor(Color.web("#1C4C67"));
        }
        earth.setMaterial(earthMaterial);

        AmbientLight light = new AmbientLight(Color.web("#B9C5CC"));
        Group starfield = createStarfield();
        root3D.getChildren().addAll(earth, starfield, light);

        subScene = new SubScene(root3D, 800, 800, true, SceneAntialiasing.BALANCED);
        subScene.setFill(SCENE_BACKGROUND);

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.setTranslateZ(-30);

        Group cameraXform = new Group();
        Group cameraXform2 = new Group();
        cameraXform.getChildren().add(cameraXform2);
        cameraXform2.getChildren().add(camera);
        root3D.getChildren().add(cameraXform);

        subScene.setCamera(camera);
        subScene.widthProperty().bind(sceneContainer.widthProperty());
        subScene.heightProperty().bind(sceneContainer.heightProperty());
        sceneContainer.getChildren().add(0, subScene);

        setupCameraControls(cameraXform, cameraXform2, camera);
    }

    void addObject(SpaceObject object) {
        double objectSize = object.getSize() != null ? object.getSize() : 0.3;
        Sphere sphere = new Sphere(objectSize);
        PhongMaterial material = new PhongMaterial(SpaceObjectUiFormatter.parseColor(
                object.getColorHex(),
                SpaceObjectUiFormatter.DEFAULT_ASSET_COLOR
        ));

        sphere.setMaterial(material);
        root3D.getChildren().add(sphere);
        visualObjects.put(object, sphere);
    }

    void removeObject(SpaceObject object) {
        Sphere sphere = visualObjects.remove(object);
        if (sphere != null) {
            root3D.getChildren().remove(sphere);
        }

        Sphere highlight = highlightSpheres.remove(object);
        if (highlight != null) {
            root3D.getChildren().remove(highlight);
        }

        Group orbit = highlightOrbits.remove(object);
        if (orbit != null) {
            root3D.getChildren().remove(orbit);
        }
    }

    void clearAllObjects() {
        root3D.getChildren().removeAll(visualObjects.values());
        visualObjects.clear();
        clearHighlights();
    }

    void showHighlights(Collection<SpaceObject> selectedObjects) {
        clearHighlights();

        for (SpaceObject object : selectedObjects) {
            if (!visualObjects.containsKey(object)) {
                continue;
            }

            double objectSize = object.getSize() != null ? object.getSize() : 0.3;
            Sphere highlight = new Sphere(objectSize * 1.6, 12);
            highlight.setMaterial(new PhongMaterial(HIGHLIGHT_COLOR));
            highlight.setDrawMode(DrawMode.LINE);
            root3D.getChildren().add(highlight);
            highlightSpheres.put(object, highlight);

            double radius = (PhysicsService.EARTH_RADIUS_KM + object.getAltitude()) * SCALE;
            Color orbitColor = SpaceObjectUiFormatter.parseColor(
                    object.getColorHex(),
                    SpaceObjectUiFormatter.DEFAULT_ASSET_COLOR
            ).interpolate(Color.web("#A7B6B2"), 0.35);
            Group orbitRing = createOrbitRing(radius, object.getInclination(), orbitColor);
            root3D.getChildren().add(orbitRing);
            highlightOrbits.put(object, orbitRing);
        }
    }

    void updateObjectPositions(Collection<SpaceObject> objects) {
        for (SpaceObject object : objects) {
            Point3D position = physicsService.calculatePosition(
                    object.getAltitude(),
                    object.getCurrentAngle(),
                    object.getInclination()
            );

            Sphere sphere = visualObjects.get(object);
            if (sphere != null) {
                moveSphere(sphere, position);
            }

            Sphere highlight = highlightSpheres.get(object);
            if (highlight != null) {
                moveSphere(highlight, position);
            }
        }
    }

    void updateRouteLines(List<SpaceObject> route) {
        clearRouteLines();

        for (int i = 0; i < route.size() - 1; i++) {
            SpaceObject first = route.get(i);
            SpaceObject second = route.get(i + 1);

            Point3D p1 = physicsService.calculatePosition(
                    first.getAltitude(),
                    first.getCurrentAngle(),
                    first.getInclination()
            );
            Point3D p2 = physicsService.calculatePosition(
                    second.getAltitude(),
                    second.getCurrentAngle(),
                    second.getInclination()
            );

            Cylinder line = createLine(scalePoint(p1), scalePoint(p2), 0.045, ROUTE_COLOR);
            routeLines.add(line);
            root3D.getChildren().add(line);
        }
    }

    void clearRouteLines() {
        root3D.getChildren().removeAll(routeLines);
        routeLines.clear();
    }

    private void clearHighlights() {
        root3D.getChildren().removeAll(highlightSpheres.values());
        highlightSpheres.clear();

        root3D.getChildren().removeAll(highlightOrbits.values());
        highlightOrbits.clear();
    }

    private Group createOrbitRing(double scaledRadius, double inclination, Color color) {
        Group ring = new Group();
        int segments = 64;
        double angleStep = 2 * Math.PI / segments;

        for (int i = 0; i < segments; i++) {
            double a1 = i * angleStep;
            double a2 = (i + 1) * angleStep;
            double altitude = scaledRadius / SCALE - PhysicsService.EARTH_RADIUS_KM;

            Point3D p1 = physicsService.calculatePosition(altitude, a1, inclination);
            Point3D p2 = physicsService.calculatePosition(altitude, a2, inclination);
            Cylinder segment = createLine(scalePoint(p1), scalePoint(p2), 0.012, color);
            ring.getChildren().add(segment);
        }

        return ring;
    }

    private Group createStarfield() {
        Group starfield = new Group();
        Random random = new Random(42L);

        PhongMaterial whiteStar = new PhongMaterial(Color.web("#D9E0E4"));
        PhongMaterial blueStar = new PhongMaterial(Color.web("#9DB7C2"));
        PhongMaterial amberStar = new PhongMaterial(Color.web("#C9B277"));

        // Keep the star layout stable between runs.
        for (int i = 0; i < 900; i++) {
            double theta = 2 * Math.PI * random.nextDouble();
            double phi = Math.acos(2 * random.nextDouble() - 1);
            double radius = 4000 + random.nextDouble() * 2000;

            double size = 0.9 + random.nextDouble() * 2.2;
            Box star = new Box(size, size, size);
            star.setTranslateX(radius * Math.sin(phi) * Math.cos(theta));
            star.setTranslateY(radius * Math.sin(phi) * Math.sin(theta));
            star.setTranslateZ(radius * Math.cos(phi));

            double colorRoll = random.nextDouble();
            if (colorRoll > 0.9) {
                star.setMaterial(blueStar);
            } else if (colorRoll > 0.8) {
                star.setMaterial(amberStar);
            } else {
                star.setMaterial(whiteStar);
            }

            starfield.getChildren().add(star);
        }

        return starfield;
    }

    private void setupCameraControls(Group cameraXform, Group cameraXform2, PerspectiveCamera camera) {
        Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
        Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
        cameraXform.getTransforms().add(rotateY);
        cameraXform2.getTransforms().add(rotateX);

        subScene.setOnMousePressed(event -> {
            dragging = true;
            velocityX = 0;
            velocityY = 0;
            mousePosX = event.getSceneX();
            mousePosY = event.getSceneY();
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
        });

        subScene.setOnMouseDragged(event -> {
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
            mousePosX = event.getSceneX();
            mousePosY = event.getSceneY();

            if (event.isPrimaryButtonDown()) {
                velocityX = (mousePosX - mouseOldX) * 0.2;
                velocityY = -(mousePosY - mouseOldY) * 0.2;
                rotateX.setAngle(clamp(rotateX.getAngle() + velocityY, -90, 90));
                rotateY.setAngle(rotateY.getAngle() + velocityX);
            }
        });

        subScene.setOnMouseReleased(event -> dragging = false);
        subScene.setOnMouseClicked(event -> {
            if (event.isStillSincePress()) {
                sceneClickHandler.run();
            }
        });
        subScene.setOnScroll(event -> camera.setTranslateZ(clamp(
                camera.getTranslateZ() + event.getDeltaY() * 0.05,
                -500,
                -8
        )));

        AnimationTimer inertiaTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (dragging || (Math.abs(velocityX) <= 0.01 && Math.abs(velocityY) <= 0.01)) {
                    return;
                }

                rotateX.setAngle(clamp(rotateX.getAngle() + velocityY, -90, 90));
                rotateY.setAngle(rotateY.getAngle() + velocityX);

                velocityX *= 0.92;
                velocityY *= 0.92;
            }
        };
        inertiaTimer.start();
    }

    private Cylinder createLine(Point3D origin, Point3D target, double radius, Color color) {
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D diff = target.subtract(origin);
        double height = diff.magnitude();

        if (height < 0.0001) {
            height = 0.0001;
            diff = yAxis;
        }

        Point3D mid = target.midpoint(origin);
        Point3D axisOfRotation = diff.crossProduct(yAxis);
        double dot = Math.max(-1.0, Math.min(1.0, diff.normalize().dotProduct(yAxis)));
        double angle = Math.acos(dot);

        if (axisOfRotation.magnitude() == 0) {
            axisOfRotation = new Point3D(1, 0, 0);
        }

        Cylinder line = new Cylinder(radius, height);
        line.setMaterial(new PhongMaterial(color));
        line.getTransforms().addAll(
                new Translate(mid.getX(), mid.getY(), mid.getZ()),
                new Rotate(-Math.toDegrees(angle), axisOfRotation)
        );
        return line;
    }

    private void moveSphere(Sphere sphere, Point3D position) {
        Point3D scaled = scalePoint(position);
        sphere.setTranslateX(scaled.getX());
        sphere.setTranslateY(scaled.getY());
        sphere.setTranslateZ(scaled.getZ());
    }

    private Point3D scalePoint(Point3D point) {
        return new Point3D(point.getX() * SCALE, point.getY() * SCALE, point.getZ() * SCALE);
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
