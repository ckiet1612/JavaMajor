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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class JavaFxApplication extends Application {

    private static final String JPACKAGE_MARKER = "jpackage.app-version";
    private static final String DATASOURCE_URL = "spring.datasource.url";
    private static final String APP_DATA_DIR = ".orbital-simulation";
    private static final String FONT_RESOURCE_DIR = "/fonts/";
    private static final String[] BUNDLED_FONT_FILES = {
            "B612-Regular.ttf",
            "B612-Bold.ttf",
            "B612Mono-Regular.ttf",
            "B612Mono-Bold.ttf"
    };

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
        for (String fontFile : BUNDLED_FONT_FILES) {
            if (!loadFont(fontFile)) {
                System.err.println("Could not load bundled font, falling back for: " + fontFile);
            }
        }
    }

    private boolean loadFont(String fontFile) {
        String resourcePath = FONT_RESOURCE_DIR + fontFile;
        return loadFontFromResource(resourcePath) || loadFontFromPackagedApp(fontFile);
    }

    private boolean loadFontFromResource(String resourcePath) {
        try (InputStream fontStream = getClass().getResourceAsStream(resourcePath)) {
            return fontStream != null && Font.loadFont(fontStream, 12) != null;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean loadFontFromPackagedApp(String fontFile) {
        String classPath = System.getProperty("java.class.path", "");
        for (String classPathEntry : classPath.split(File.pathSeparator)) {
            if (classPathEntry.isBlank()) {
                continue;
            }

            try {
                Path appDirectory = resolveAppDirectory(classPathEntry);
                Path fontPath = appDirectory.resolve("fonts").resolve(fontFile);
                if (Files.isRegularFile(fontPath) && Font.loadFont(fontPath.toUri().toString(), 12) != null) {
                    return true;
                }
            } catch (Exception ignored) {
                // Some launchers put non-file entries on the classpath. Those can be skipped.
            }
        }

        return false;
    }

    private Path resolveAppDirectory(String classPathEntry) {
        Path path = Path.of(classPathEntry);
        return Files.isRegularFile(path) ? path.getParent() : path;
    }

    @Override
    public void stop() {
        this.applicationContext.close();
        Platform.exit();
    }
}
