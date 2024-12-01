package com.example.projectilemotionsim;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        final double LEFT_X = 50.0, RIGHT_X =60,  BOTTOM_Y = 600;

        double TOP_Y = 200;

        Rectangle ledge = new Rectangle(-100, 450, 200, 100);

        // velocity
        Label veloLabel = new Label("Velocity");
        Slider veloSlider = new Slider(0, 1000, 0);
        veloSlider.setMaxWidth(200);
        veloSlider.setShowTickMarks(true);
        veloSlider.setShowTickLabels(true);
        veloSlider.setMajorTickUnit(100);
        veloSlider.setBlockIncrement(1);
        Label chosenVelo = new Label("0.00");


        // angle
        Label angleLabel = new Label("Angle");
        Slider angleSlider = new Slider(0, 360, 0);
        angleSlider.setMaxWidth(200);
        angleSlider.setShowTickMarks(true);
        angleSlider.setShowTickLabels(true);
        angleSlider.setMajorTickUnit(20);
        angleSlider.setBlockIncrement(5);
        Label chosenAngle = new Label("0.00");

        // height
        Label heightLabel = new Label("Height");
        Slider heightSlider = new Slider(0, 500, 50);
        heightSlider.setMaxWidth(200);
        heightSlider.setShowTickMarks(true);
        heightSlider.setShowTickLabels(true);
        heightSlider.setMajorTickUnit(100);
        heightSlider.setBlockIncrement(1);
        Label chosenHeight = new Label("0.00");


        // velocity vbox
        VBox veloVbox = new VBox(veloLabel, veloSlider, chosenVelo);
        veloVbox.setAlignment(Pos.TOP_RIGHT);
        veloVbox.setSpacing(10);
        veloVbox.setPadding(new Insets(20));

        // angle vbox
        VBox angleVbox = new VBox(angleLabel, angleSlider);
        angleVbox.setAlignment(Pos.TOP_RIGHT);
        angleVbox.setSpacing(10);
        angleVbox.setPadding(new Insets(20));

        // height vbox
        VBox heightVbox = new VBox(heightLabel, heightSlider);
        heightVbox.setAlignment(Pos.TOP_RIGHT);
        heightVbox.setSpacing(10);
        heightVbox.setPadding(new Insets(20));

        // person
        Person person = new Person();
//        person.setScaleX(3);
//        person.setScaleY(3);


        // slider listeners
        veloSlider.valueProperty().addListener((observable, oldvalue, newvalue) -> {
            chosenVelo.setText(String.format("Velocity: %.2f", newvalue));
        });

        angleSlider.valueProperty().addListener((observable, oldvalue, newvalue) -> {
            chosenAngle.setText(String.format("Velocity: %.2f", newvalue));
        });



        heightSlider.valueProperty().addListener((observable, oldvalue, newvalue) -> {
            chosenHeight.setText(String.format("Velocity: %.2f", newvalue));
            double bottomY = ledge.getY() + ledge.getHeight();
            ledge.setHeight(newvalue.doubleValue());
            ledge.setY(bottomY - newvalue.doubleValue());


            person.setLayoutY(ledge.getY() - person.getHeight());

        });

        // pane for person or something
        Pane rectanglePane = new Pane(person,ledge);


        //main vbox
        VBox mainVbox = new VBox(veloVbox, chosenVelo, angleVbox, heightVbox);

        // main hbox
        HBox mainHbox = new HBox(rectanglePane, mainVbox);

        mainHbox.setAlignment(Pos.CENTER);

        mainVbox.setAlignment(Pos.CENTER_RIGHT);

        mainVbox.setPadding(new Insets(20));



        Scene scene = new Scene(mainHbox, 800, 600);
        stage.setTitle("Projectile Motion Simulator");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}