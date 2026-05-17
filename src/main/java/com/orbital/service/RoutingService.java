package com.orbital.service;

import com.orbital.model.SpaceObject;
import javafx.geometry.Point3D;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

@Service
public class RoutingService {

    private final PhysicsService physicsService;

    public RoutingService(PhysicsService physicsService) {
        this.physicsService = physicsService;
    }

    /**
     * Returns true when Earth is not blocking the straight line between two objects.
     */
    public boolean hasLineOfSight(SpaceObject obj1, SpaceObject obj2) {
        Point3D p1 = physicsService.calculatePosition(obj1.getAltitude(), obj1.getCurrentAngle(), obj1.getInclination());
        Point3D p2 = physicsService.calculatePosition(obj2.getAltitude(), obj2.getCurrentAngle(), obj2.getInclination());

        Point3D d = p2.subtract(p1);
        double a = d.dotProduct(d);
        double b = 2 * p1.dotProduct(d);
        double c = p1.dotProduct(p1) - Math.pow(PhysicsService.EARTH_RADIUS_KM, 2);

        // This tells us if the line can hit Earth's sphere at all.
        double discriminant = b * b - 4 * a * c;

        // Same or almost same point, so there is nothing to block.
        if (a < 1e-6) {
            return true;
        }

        // No sphere intersection means Earth is not in the way.
        if (discriminant < 0) {
            return true;
        }

        // Only intersections between p1 and p2 count as blocked signal.
        double t1 = (-b - Math.sqrt(discriminant)) / (2 * a);
        double t2 = (-b + Math.sqrt(discriminant)) / (2 * a);

        if ((t1 >= 0 && t1 <= 1) || (t2 >= 0 && t2 <= 1)) {
            return false;
        }
        return true;
    }

    /**
     * Finds the shortest relay path with Dijkstra's algorithm.
     */
    public List<SpaceObject> findShortestPath(SpaceObject start, SpaceObject end, List<SpaceObject> allObjects) {
        Map<SpaceObject, Double> distances = new HashMap<>();
        Map<SpaceObject, SpaceObject> previousNodes = new HashMap<>();
        PriorityQueue<SpaceObject> queue = new PriorityQueue<>(Comparator.comparingDouble(distances::get));

        for (SpaceObject obj : allObjects) {
            distances.put(obj, Double.MAX_VALUE);
            previousNodes.put(obj, null);
        }

        distances.put(start, 0.0);
        queue.add(start);

        while (!queue.isEmpty()) {
            SpaceObject current = queue.poll();

            if (current.equals(end)) {
                break; // Dijkstra can stop once the target is reached.
            }

            for (SpaceObject neighbor : allObjects) {
                if (current.equals(neighbor)) continue;

                if (hasLineOfSight(current, neighbor)) {
                    Point3D p1 = physicsService.calculatePosition(current.getAltitude(), current.getCurrentAngle(), current.getInclination());
                    Point3D p2 = physicsService.calculatePosition(neighbor.getAltitude(), neighbor.getCurrentAngle(), neighbor.getInclination());
                    double distance = p1.distance(p2);
                    
                    double newDist = distances.get(current) + distance;
                    if (newDist < distances.get(neighbor)) {
                        distances.put(neighbor, newDist);
                        previousNodes.put(neighbor, current);
                        
                        queue.remove(neighbor);
                        queue.add(neighbor);
                    }
                }
            }
        }

        List<SpaceObject> path = new ArrayList<>();
        SpaceObject current = end;
        if (previousNodes.get(current) != null || current.equals(start)) {
            while (current != null) {
                path.add(current);
                current = previousNodes.get(current);
            }
            Collections.reverse(path);
        }
        
        return path.size() > 1 ? path : Collections.emptyList();
    }
}
