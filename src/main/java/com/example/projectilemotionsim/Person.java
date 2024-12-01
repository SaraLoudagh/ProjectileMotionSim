package com.example.projectilemotionsim;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;

public class Person extends VBox {


    public Person(){
        Circle head = new Circle(5);
        head.setFill(Color.TRANSPARENT);
        head.setStroke(Color.BLACK);

        Line torso = new Line(0, 3 , 0,-5 );

        Polyline legs = new Polyline(-2, -9, 0, -5, 2, -9);
        legs.setRotate(180);
        Polyline arms = new Polyline(-5, -9, 0, -5, 5, -9);


        VBox body = new VBox(head, arms, torso, legs);
        body.setAlignment(Pos.CENTER);

        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(head, arms, torso, legs);









    }


    public void getHeight(double v) {
    }

    public void setY(double v) {

    }
}
