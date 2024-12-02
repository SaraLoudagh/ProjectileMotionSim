package com.example.projectilemotionsim;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Cannon extends VBox {

    public Cannon() {
        // Create the base of the cannon
        Circle base = new Circle(15); // Radius of the base
        base.setFill(Color.DARKGRAY);
        base.setStroke(Color.BLACK);

        // Create the barrel of the cannon
        Rectangle barrel = new Rectangle(40, 10); // Width and height of the barrel
        barrel.setFill(Color.GRAY);
        barrel.setStroke(Color.BLACK);
        barrel.setArcWidth(5); // Rounded edges
        barrel.setArcHeight(5);
        barrel.setTranslateY(-10); // Move it up to align with the base

        // Align everything in a vertical layout
        VBox cannonBody = new VBox(base, barrel);
        cannonBody.setAlignment(Pos.CENTER);

        // Add the cannon parts to this VBox
        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(cannonBody);
    }
}
