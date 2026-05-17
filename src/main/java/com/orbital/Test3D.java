package com.orbital;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;

public class Test3D extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Group root3D = new Group();
        Sphere s = new Sphere(100);
        Circle c = new Circle(150);
        c.setStroke(Color.RED);
        c.setFill(Color.TRANSPARENT);
        root3D.getChildren().addAll(s, c);
        
        SubScene subScene = new SubScene(root3D, 400, 400, true, SceneAntialiasing.BALANCED);
        Group mainRoot = new Group(subScene);
        Scene scene = new Scene(mainRoot, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        System.out.println("Test3D started successfully");
        System.exit(0);
    }
}
