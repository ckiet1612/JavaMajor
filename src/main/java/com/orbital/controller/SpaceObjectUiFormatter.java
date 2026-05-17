package com.orbital.controller;

import com.orbital.model.SpaceObject;
import javafx.scene.paint.Color;

final class SpaceObjectUiFormatter {

    static final Color DEFAULT_ASSET_COLOR = Color.web("#D9A441");

    private SpaceObjectUiFormatter() {
    }

    static String formatSatelliteDetails(SpaceObject object) {
        return "CALLSIGN : " + object.getName() + "\n"
                + "PHÂN LỚP : " + formatOrbitBand(object.getAltitude()) + " / " + object.getType() + "\n"
                + "KINH ĐỘ  : " + String.format("%.2f", object.getLongitude()) + "°\n"
                + "ĐỘ CAO   : " + String.format("%.0f", object.getAltitude()) + " km\n"
                + "NGHIÊNG  : " + String.format("%.1f", object.getInclination()) + "°\n"
                + "VẬN TỐC  : " + String.format("%.2f", object.getVelocity()) + " km/s\n"
                + "KÍCH CỠ  : " + object.getSize() + "\n"
                + "MÀU HEX  : " + object.getColorHex();
    }

    static String formatAssetMeta(SpaceObject object) {
        return formatOrbitBand(object.getAltitude())
                + " · " + String.format("%.2f km/s", object.getVelocity())
                + " · " + String.format("%.1f°", object.getInclination());
    }

    static String formatOrbitBand(double altitude) {
        if (altitude < 2_000) {
            return "LEO";
        }
        if (altitude < 35_786) {
            return "MEO";
        }
        return "GEO";
    }

    static Color parseColor(String colorHex, Color fallback) {
        try {
            return colorHex == null || colorHex.isBlank() ? fallback : Color.web(colorHex);
        } catch (IllegalArgumentException ex) {
            return fallback;
        }
    }

    static String toHex(Color color) {
        int red = (int) Math.round(color.getRed() * 255);
        int green = (int) Math.round(color.getGreen() * 255);
        int blue = (int) Math.round(color.getBlue() * 255);
        return String.format("#%02X%02X%02X", red, green, blue);
    }
}
