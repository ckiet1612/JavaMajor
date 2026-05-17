package com.orbital;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class JavaFxApplication extends Application {

    private static final String JPACKAGE_MARKER = "jpackage.app-version";
    private static final String DATASOURCE_URL = "spring.datasource.url";
    private static final String APP_DATA_DIR = ".orbital-simulation";

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        configurePackagedDatabase();

        String[] args = getParameters().getRaw().toArray(new String[0]);
        this.applicationContext = new SpringApplicationBuilder()
                .sources(OrbitalApp.class)
                .run(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        loadBundledFonts();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        fxmlLoader.setControllerFactory(applicationContext::getBean);
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        
        stage.setTitle("Orbital Simulation");
        stage.setScene(scene);
        stage.show();
    }

    private void configurePackagedDatabase() {
        if (System.getProperty(JPACKAGE_MARKER) == null || System.getProperty(DATASOURCE_URL) != null) {
            return;
        }

        try {
            Path dataDirectory = Path.of(
                    System.getProperty("user.home"),
                    APP_DATA_DIR
            );
            Files.createDirectories(dataDirectory);
            System.setProperty(DATASOURCE_URL, "jdbc:h2:file:" + dataDirectory.resolve("orbitaldb") + ";AUTO_SERVER=TRUE");
        } catch (IOException e) {
            throw new IllegalStateException("Could not prepare app database directory.", e);
        }
    }

    private void loadBundledFonts() {
        loadFont("/fonts/B612-Regular.ttf");
        loadFont("/fonts/B612-Bold.ttf");
        loadFont("/fonts/B612Mono-Regular.ttf");
        loadFont("/fonts/B612Mono-Bold.ttf");
    }

    private void loadFont(String resourcePath) {
        try (InputStream fontStream = getClass().getResourceAsStream(resourcePath)) {
            if (fontStream == null || Font.loadFont(fontStream, 12) == null) {
                throw new IllegalStateException("Could not load font: " + resourcePath);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Could not load font: " + resourcePath, e);
        }
    }

    @Override
    public void stop() {
        this.applicationContext.close();
        Platform.exit();
    }
}
