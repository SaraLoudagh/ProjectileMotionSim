package com.example.projectilemotionsim;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.css.Stylesheet;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
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

import javax.swing.text.html.StyleSheet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class HelloApplication extends Application {

    private static final double gravity = 9.81;
    private Pane rectanglePane; // Pane to hold the trajectory and objects
    private Rectangle ledge; // The ledge object
//    private Person person; // The person object
    private Polyline trajectory; // The trajectory line

    private List<Double> precomputedPoints; // Stores the precomputed trajectory points
    private Timeline animationTimeline;    // Timeline for animating the points

    private Line ground;

    private Circle projectile;
    private Node currentObject; // Can hold any JavaFX Node (Person or Cannon)
    private Label xDistanceLabel, yDistanceLabel; // Labels for distances


    private String lessonText = """
            Projectile motion is a form of motion where an object is thrown. It goes in a curved motion and the trajectory depends on various variables such as angle, initial velocity, height and gravitational acceleration. The vertical motion is affected by the gravitational acceleration while the horizontal motion is only affected by air friction, which is insignificant. In other words, the horizontal motion is constant.
                        
            Key Concepts:
            1. The horizontal motion and vertical motion are independent.
            2. The horizontal velocity remains constant.
            3. The vertical motion is affected by gravity, with an acceleration of 9.8 m/s².
                        
            Examples:
            - A human throwing a ball.
            - A cannon firing a cannonball.
            - A fountain spraying water.
            """;

    @Override
    public void start(Stage stage) throws IOException {

        currentObject = new Person(); // Default to Person
        currentObject.setLayoutY(416);
        currentObject.setLayoutX(90);

        ground = new Line(0, 550, 2000, 550);

        ledge = new Rectangle(-100, 450, 200, 100);

        xDistanceLabel = new Label("Distance X: 0.00");
        yDistanceLabel = new Label("Max Height: 0.00");


        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem exitMenuItem = new MenuItem("Exit");
        fileMenu.getItems().add(exitMenuItem);

        exitMenuItem.setOnAction(e -> stage.close());

        menuBar.getMenus().add(fileMenu);

        Button lessonButton = new Button("Lesson");
        lessonButton.setOnAction(e -> {
            Stage lessonStage = new Stage();

            TextArea textArea = new TextArea(lessonText);
            textArea.setWrapText(true); // Wraps text for better readability
            textArea.setEditable(false); // Makes it read-only

            Image horizontalImage = new Image("File:HorizontalMotion.png");
            Image verticalImage = new Image("File:VerticalMotion.png");

            ImageView horizontalImageView = new ImageView(horizontalImage);
            horizontalImageView.setFitHeight(50);
            horizontalImageView.setPreserveRatio(true);
            ImageView verticalImageView = new ImageView(verticalImage);
            verticalImageView.setFitHeight(50);
            verticalImageView.setPreserveRatio(true);

            Label horizontalLabel = new Label("Horizontal motion formula:");
            horizontalLabel.getStylesheets().add("Stylesheet.css");
            HBox horizontalBox = new HBox(horizontalLabel, horizontalImageView);
            Label verticalLabel = new Label("Vertical motion formula:");
            verticalLabel.getStylesheets().add("Stylesheet.css");
            HBox verticalBox = new HBox(verticalLabel, verticalImageView);

            VBox lessonMainVBox = new VBox(textArea, horizontalBox, verticalBox);

            Scene lessonScene = new Scene(lessonMainVBox, 400, 300);
            lessonStage.setTitle("Lesson: Projectile Motion");
            lessonStage.setScene(lessonScene);
            lessonStage.showAndWait();
        });

        // combobox to choose the projectile type
        ComboBox<String> comboBox = new ComboBox();
        comboBox.getItems().addAll("Human", "Cannon");
        comboBox.getSelectionModel().selectFirst(); // automatically select first option

        Label veloLabel = new Label("Velocity");
        Slider veloSlider = new Slider(0, 100, 0);
        veloSlider.setMaxWidth(500);
        veloSlider.setShowTickMarks(true);
        veloSlider.setShowTickLabels(true);
        veloSlider.setMajorTickUnit(100);
        veloSlider.setBlockIncrement(1);
        Label chosenVelo = new Label("0.00 m/s");


        Label angleLabel = new Label("Angle");
        Slider angleSlider = new Slider(-90, 90, 0);
        angleSlider.setMaxWidth(500);
        angleSlider.setShowTickMarks(true);
        angleSlider.setShowTickLabels(true);
        angleSlider.setMajorTickUnit(20);
        angleSlider.setBlockIncrement(5);
        Label chosenAngle = new Label("0.00 degrees");


        Label heightLabel = new Label("Height");
        Slider heightSlider = new Slider(0, 300, 50);
        heightSlider.setMaxWidth(500);
        heightSlider.setShowTickMarks(true);
        heightSlider.setShowTickLabels(true);
        heightSlider.setMajorTickUnit(100);
        heightSlider.setBlockIncrement(1);
        Label chosenHeight = new Label("0.00 m");


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

//        Person
//        person = new Person();
//        person.setLayoutY(416);
//        person.setLayoutX(90);
//        person.setScaleX(3);
//        person.setScaleY(3);

        // combobox event handling
        comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Remove the current object from the pane
            if (currentObject != null) {
                rectanglePane.getChildren().remove(currentObject);
            }

            // Create and add the new object based on the selection
            if (newValue.equals("Human")) {
                currentObject = new Person();
                updateObjectPosition(currentObject);
                currentObject.setLayoutX(90);
                currentObject.setTranslateY(currentObject.getTranslateY()-18);
                rectanglePane.getChildren().add(currentObject);
            } else if (newValue.equals("Cannon")) {
                currentObject = new Cannon();
                currentObject.setLayoutX(60);
                rectanglePane.getChildren().add(currentObject);
                updateObjectPosition(currentObject);
                currentObject.setTranslateY(currentObject.getTranslateY() - 10);
            }

            // Set layout and add the new object to the pane
//            currentObject.setLayoutY(ledge.getY() - ((Node) currentObject).getBoundsInParent().getHeight());
//            if (currentObject != null) {
//                rectanglePane.getChildren().add(currentObject);
//                updateObjectPosition(currentObject);
//            }
            // Update the trajectory
            updateTrajectory(veloSlider, angleSlider, heightSlider);
        });



        trajectory = new Polyline();
        trajectory.setStroke(Color.BLUE);
        trajectory.setStrokeWidth(2);
        trajectory.setOpacity(0.5);

        projectile = new Circle(5, Color.RED); // Radius 5, red color
        projectile.setVisible(false); // Initially hidden until animation starts


        veloSlider.valueProperty().addListener((observable, oldvalue, newvalue) -> {
            chosenVelo.setText(String.format("Velocity: %.2f m/s", newvalue));
            updateTrajectory(veloSlider, angleSlider, heightSlider);
        });

        angleSlider.valueProperty().addListener((observable, oldvalue, newvalue) -> {
            chosenAngle.setText(String.format("Angle: %.2f degrees", newvalue));
            updateTrajectory(veloSlider, angleSlider, heightSlider);

        });


        heightSlider.valueProperty().addListener((observable, oldvalue, newvalue) -> {
            chosenHeight.setText(String.format("Height: %.2f m", newvalue));
            double bottomY = ledge.getY() + ledge.getHeight();
            ledge.setHeight(newvalue.doubleValue());
            ledge.setY(bottomY - newvalue.doubleValue());
            if (currentObject != null) {
//                currentObject.setLayoutY(ledge.getY() - ((Node) currentObject).getBoundsInParent().getHeight());
                updateObjectPosition(currentObject);
            }
            updateTrajectory(veloSlider, angleSlider, heightSlider);

        });


        rectanglePane = new Pane(currentObject, ledge, ground, trajectory, projectile);
        rectanglePane.setMinSize(400, 400);


//        person.setLayoutY(416);
//        person.setLayoutX(90);

        Label typeLabel = new Label("Type of projectile");
        typeLabel.setPadding(new Insets(20));
        VBox typeVBox = new VBox(typeLabel, comboBox);
        typeVBox.setAlignment(Pos.CENTER);
        typeVBox.setPadding(new Insets(20));

        // vbox for lesson button
        Label lessonButtonLabel = new Label("Click on the button for theory explanation");
        lessonButtonLabel.setPadding(new Insets(20));
        VBox lessonButtonVbox = new VBox(lessonButtonLabel, lessonButton);
        lessonButtonVbox.setAlignment(Pos.CENTER);
        lessonButtonVbox.setPadding(new Insets(20));



//        HBox mainHbox = new HBox(rectanglePane, mainVbox);
//        mainHbox.setAlignment(Pos.CENTER);


        HBox typeAndTheory = new HBox(lessonButtonVbox, typeVBox, xDistanceLabel, yDistanceLabel);
        typeAndTheory.setAlignment(Pos.TOP_CENTER);
        typeAndTheory.setSpacing(10);

        HBox parameters = new HBox(veloVbox, angleVbox, heightVbox);
        parameters.setAlignment(Pos.CENTER);
        VBox mainVbox = new VBox(typeAndTheory, rectanglePane, parameters);

        // BorderPane
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(mainVbox);
        borderPane.setTop(menuBar);

        mainVbox.setAlignment(Pos.CENTER_RIGHT);
        mainVbox.setPadding(new Insets(20));


        Scene scene = new Scene(borderPane, 800, 700);

        stage.setScene(scene);
        stage.setTitle("Projectile Motion Simulator");
        stage.show();

        updateTrajectory(veloSlider, angleSlider, heightSlider);
    }


    private void updateTrajectory(Slider veloSlider, Slider angleSlider, Slider heightSlider) {
        double velocity = veloSlider.getValue();
        double angle = angleSlider.getValue();
        double ledgeTopY = 615 - (ledge.getY());
        double height = heightSlider.getValue();

        double totalDistance = calculateTotalDistance(velocity, angle, height);
        double maxHeight = calculateMaxHeight(velocity, angle, height);

        xDistanceLabel.setText(String.format("Distance X: %.2f m", totalDistance));
        yDistanceLabel.setText(String.format("Max Height: %.2f m", maxHeight));

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

    private double calculateTotalDistance(double velocity, double angle, double height) {


        double angleRadians = Math.toRadians(angle);

        // Horizontal velocity
        double velocityX = velocity * Math.cos(angleRadians);

        // Vertical velocity
        double velocityY = velocity * Math.sin(angleRadians);

        // Time to hit the ground (quadratic formula for y = 0)
        double discriminant = Math.sqrt(velocityY * velocityY + 2 * gravity* height);
        double timeToGround = (velocityY + discriminant) / gravity;

        // Total horizontal distance
        return velocityX * timeToGround;
    }


    private double calculateMaxHeight(double velocity, double angle, double height) {
        double angleRadians = Math.toRadians(angle);

        // Vertical velocity
        double velocityY = velocity * Math.sin(angleRadians);

        // Maximum height calculation
        return height + (velocityY * velocityY) / (2 * gravity);
    }


    private void updateProjectilePosition(double x, double y) {
        projectile.setCenterX(x);
        projectile.setCenterY(y);
    }

    private void updateObjectPosition(Node object) {
        if (object instanceof Person) {
            // Specific logic for Person
            double newYPosition = ledge.getY() - object.getBoundsInParent().getHeight();
            object.setLayoutY(newYPosition);
            object.setTranslateY(0);
            object.setLayoutX(90); // Adjust X position for the person
        } else if (object instanceof Cannon) {
            // Specific logic for Cannon
            double newYPosition = ledge.getY() - object.getBoundsInParent().getHeight();
            object.setLayoutY(newYPosition + 10); // Adjust Y position to slightly raise the cannon
            object.setLayoutX(60); // Adjust X position for the cannon
            currentObject.setTranslateY(0);
        }
    }



    public static void main(String[] args) {
        launch();
    }
}