package com.orbital.controller;

import com.orbital.model.SpaceObject;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

final class SpaceObjectCellFactory {

    private SpaceObjectCellFactory() {
    }

    static ListCell<SpaceObject> createAssetListCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(SpaceObject item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }

                Circle swatch = new Circle(5);
                swatch.setFill(SpaceObjectUiFormatter.parseColor(
                        item.getColorHex(),
                        SpaceObjectUiFormatter.DEFAULT_ASSET_COLOR
                ));

                Label nameLabel = new Label(item.getName());
                nameLabel.getStyleClass().add("asset-name");

                Label metaLabel = new Label(SpaceObjectUiFormatter.formatAssetMeta(item));
                metaLabel.getStyleClass().add("asset-meta");

                VBox copy = new VBox(2, nameLabel, metaLabel);
                HBox.setHgrow(copy, Priority.ALWAYS);

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Label altitudeLabel = new Label(String.format("%.0f km", item.getAltitude()));
                altitudeLabel.getStyleClass().add("asset-meta");

                HBox row = new HBox(8, swatch, copy, spacer, altitudeLabel);
                row.setAlignment(Pos.CENTER_LEFT);
                row.setPadding(new Insets(1, 0, 1, 0));

                setText(null);
                setGraphic(row);
            }
        };
    }

    static ListCell<SpaceObject> createCompactAssetCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(SpaceObject item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(null);
                setText(empty || item == null
                        ? null
                        : item.getName() + " · " + SpaceObjectUiFormatter.formatOrbitBand(item.getAltitude()));
            }
        };
    }
}
