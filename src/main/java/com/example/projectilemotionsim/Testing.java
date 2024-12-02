package com.example.projectilemotionsim;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Testing extends Application {

    private static final double gravity = 9.81;
    private Pane rectanglePane; // Pane to hold the trajectory and objects
    private Rectangle ledge; // The ledge object
    private Person person; // The person object
    private Polyline trajectory; // The trajectory line

    private List<Double> precomputedPoints; // Stores the precomputed trajectory points
    private Timeline animationTimeline;    // Timeline for animating the points

    private Line ground;

    private Circle projectile;

    private String lessonText = """
            Projectile motion is a form of motion experienced by an object that is thrown near the Earth's surface.
            It moves along a curved path under the action of gravity only.
            
            Key Concepts:
            1. The horizontal motion and vertical motion are independent.
            2. The horizontal velocity remains constant.
            3. The vertical motion is affected by gravity, with an acceleration of 9.8 m/s².
            
            Examples:
            - Throwing a ball.
            - A cannonball fired from a cannon.
            - Water sprayed from a fountain.
            """;


    @Override
    public void start(Stage stage) throws IOException {

        ground = new Line(0, 550, 800, 550);


        ledge = new Rectangle(-100, 450, 200, 100);

//<<<<<<< HEAD
//=======
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem exitMenuItem = new MenuItem("Exit");
        fileMenu.getItems().add(exitMenuItem);

        exitMenuItem.setOnAction(e -> stage.close());

        menuBar.getMenus().add(fileMenu);

        // theory button
        Button lessonButton = new Button("Lesson");
        lessonButton.setOnAction(e -> {
            Stage lessonStage = new Stage();

            TextArea textArea = new TextArea(lessonText);
            textArea.setWrapText(true); // Wraps text for better readability
            textArea.setEditable(false); // Makes it read-only


            Scene lessonScene = new Scene(textArea, 400, 300);
            lessonStage.setTitle("Lesson: Projectile Motion");
            lessonStage.setScene(lessonScene);
            lessonStage.showAndWait();
        });

        // combobox to choose the projectile type
        ComboBox comboBox = new ComboBox();
        comboBox.getItems().addAll( "Human", "Cannon");
        comboBox.getSelectionModel().selectFirst(); // automatically select first option

//>>>>>>> b08e8808e89bdcdd834b8ad68eff4638e8642e1b
        Label veloLabel = new Label("Velocity");
        Slider veloSlider = new Slider(0, 100, 0);
        veloSlider.setMaxWidth(500);
        veloSlider.setShowTickMarks(true);
        veloSlider.setShowTickLabels(true);
        veloSlider.setMajorTickUnit(100);
        veloSlider.setBlockIncrement(1);
        Label chosenVelo = new Label("0.00");



        Label angleLabel = new Label("Angle");
        Slider angleSlider = new Slider(-90, 90, 0);
        angleSlider.setMaxWidth(500);
        angleSlider.setShowTickMarks(true);
        angleSlider.setShowTickLabels(true);
        angleSlider.setMajorTickUnit(20);
        angleSlider.setBlockIncrement(5);
        Label chosenAngle = new Label("0.00");


        Label heightLabel = new Label("Height");
        Slider heightSlider = new Slider(0, 300, 50);
        heightSlider.setMaxWidth(500);
        heightSlider.setShowTickMarks(true);
        heightSlider.setShowTickLabels(true);
        heightSlider.setMajorTickUnit(100);
        heightSlider.setBlockIncrement(1);
        Label chosenHeight = new Label("0.00");



        VBox veloVbox = new VBox(veloLabel, veloSlider, chosenVelo);
        veloVbox.setAlignment(Pos.TOP_RIGHT);
        veloVbox.setSpacing(10);
        veloVbox.setPadding(new Insets(20));

        VBox angleVbox = new VBox(angleLabel, angleSlider, chosenAngle);
        angleVbox.setAlignment(Pos.TOP_RIGHT);
        angleVbox.setSpacing(10);
        angleVbox.setPadding(new Insets(20));

        VBox heightVbox = new VBox(heightLabel, heightSlider, chosenHeight);
        heightVbox.setAlignment(Pos.TOP_RIGHT);
        heightVbox.setSpacing(10);
        heightVbox.setPadding(new Insets(20));


        person = new Person();


        trajectory = new Polyline();
        trajectory.setStroke(Color.BLUE);
        trajectory.setStrokeWidth(2);
        trajectory.setOpacity(0.5);

        projectile = new Circle(5, Color.RED); // Radius 5, red color
        projectile.setVisible(false); // Initially hidden until animation starts




        veloSlider.valueProperty().addListener((observable, oldvalue, newvalue) -> {
            chosenVelo.setText(String.format("Velocity: %.2f", newvalue));
            updateTrajectory(veloSlider, angleSlider, heightSlider);


        });

        angleSlider.valueProperty().addListener((observable, oldvalue, newvalue) -> {
            chosenAngle.setText(String.format("Angle: %.2f", newvalue));
            updateTrajectory(veloSlider, angleSlider, heightSlider);


        });



        heightSlider.valueProperty().addListener((observable, oldvalue, newvalue) -> {
            chosenHeight.setText(String.format("Height: %.2f", newvalue));
            double bottomY = ledge.getY() + ledge.getHeight();
            ledge.setHeight(newvalue.doubleValue());
            ledge.setY(bottomY - newvalue.doubleValue());
            person.setLayoutY(ledge.getY() - person.getHeight());
//            clearTrajectory();
            updateTrajectory(veloSlider, angleSlider, heightSlider);

        });



        rectanglePane = new Pane(person, ledge, ground, trajectory, projectile);
        rectanglePane.setMinSize(600, 600);

        rectanglePane.setMaxSize(900, 900);


        person.setLayoutY(416);
        person.setLayoutX(90);



        HBox parameters = new HBox(veloVbox, angleVbox, heightVbox);
        parameters.setAlignment(Pos.CENTER);
        VBox mainVbox = new VBox(rectanglePane, parameters);


//        HBox mainHbox = new HBox(rectanglePane, parameters);

        mainVbox.setAlignment(Pos.CENTER);

        parameters.setAlignment(Pos.CENTER);

        parameters.setPadding(new Insets(20));

        Scene scene = new Scene(mainVbox, 800, 600);

        stage.setScene(scene);
        stage.show();

        updateTrajectory(veloSlider, angleSlider, heightSlider);
    }


    private void updateTrajectory(Slider veloSlider, Slider angleSlider, Slider heightSlider) {
        double velocity = veloSlider.getValue();
        double angle = angleSlider.getValue();
        double ledgeTopY = 615 - (ledge.getY());

        calculateProjectilePath(velocity, angle, ledgeTopY); // Precompute points
        trajectory.getPoints().clear();

        if (!rectanglePane.getChildren().contains(trajectory)) {
            rectanglePane.getChildren().add(trajectory);
        }
        animateTrajectory();
    }

    private void calculateProjectilePath(double velocity, double angle, double height) {
        precomputedPoints = new ArrayList<>(); // Reset the list of points

        trajectory.setStroke(Color.BLUE);
        trajectory.setStrokeWidth(2);

        // Convert angle to radians
        double angleRadians = Math.toRadians(angle);

        // Initial velocity components
        double velocityX = velocity * Math.cos(angleRadians);
        double velocityY = velocity * Math.sin(angleRadians);

        double ledgeTopY = ledge.getY();
        double ledgeBottomY = ledge.getY() + ledge.getHeight();

        // Time step for simulation
        double timeStep = 0.1;

        // Calculate points until the projectile hits the ground
        double t = 0; // start time
        double x, y;
        do {
            x = velocityX * t;
            y = height + (velocityY * t) - (0.5 * gravity * t * t);
            double adjustedY = 594 - y; // Adjust for JavaFX pane coordinates

            if (adjustedY > ledgeBottomY) break;

            precomputedPoints.add(x + 102);  // Adjust for pane coordinates (x)
            precomputedPoints.add(594 - y); // Adjust for pane coordinates (y)
            t += timeStep;
        } while (true); // Continue until it hits the ground
    }

    private void animateTrajectory() {
        if (animationTimeline != null) {
            animationTimeline.stop(); // Stop any existing animation
        }

        projectile.setVisible(true);
        projectile.setCenterX(precomputedPoints.get(0));
        projectile.setCenterY(precomputedPoints.get(1));

        trajectory.getPoints().clear(); // Clear previous points

        // Initialize the timeline
        animationTimeline = new Timeline();
        for (int i = 0; i < precomputedPoints.size(); i += 2) {
            final int index = i; // Final variable for lambda
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(index / 20.0), event -> {
                trajectory.getPoints().addAll(precomputedPoints.get(index), precomputedPoints.get(index + 1));
                updateProjectilePosition(precomputedPoints.get(index), precomputedPoints.get(index + 1));
            });
            animationTimeline.getKeyFrames().add(keyFrame);
        }

        animationTimeline.setOnFinished(event -> projectile.setVisible(false));
        animationTimeline.play(); // Start the animation
    }


    private void updateProjectilePosition(double x, double y) {
        projectile.setCenterX(x);
        projectile.setCenterY(y);
    }



    public static void main(String[] args) {
        launch();
    }
}