package cuie.project.infinite_roll_selection;

import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

class BusinessSkin extends SkinBase<Infinite_roll_selection> {
    private static final int BORDER_WIDTH = 10;
    private static final int CONTROL_HEIGHT = 100;
    private static final int CONTROL_WIDTH = 300;

    private static final String STYLE_CSS = "style.css";

    private final BooleanProperty focused = new SimpleBooleanProperty();

    private StackPane drawingPane;
    private VBox contentBox;

    private Rectangle border;
    private Rectangle background;
    private Label prevLabel;
    private Label userFacingLabel;
    private Label nextLabel;
    private Label tempLabel;

    private Animation upAnimation;
    private Animation downAnimation;

    BusinessSkin(Infinite_roll_selection control) {
        super(control);
        initializeSelf();
        initializeParts();
        initializeAnimations();
        layoutParts();
        setupEventHandlers();
        setupValueChangedListeners();
        setupBindings();

        updateUI();
    }

    private void initializeSelf() {
        getSkinnable().addStylesheetFiles(STYLE_CSS);
    }

    private void initializeParts() {

        border = new Rectangle(CONTROL_WIDTH+(BORDER_WIDTH*2), CONTROL_HEIGHT+(BORDER_WIDTH*2), Color.BLACK);
        border.getStyleClass().add("border");


        background = new Rectangle(CONTROL_WIDTH, CONTROL_HEIGHT, Color.GRAY);
        background.getStyleClass().add("background-rect");
        background.setMouseTransparent(true);

        prevLabel = new Label();
        prevLabel.getStyleClass().add("label");
        prevLabel.setMouseTransparent(true);
        userFacingLabel = new Label();
        userFacingLabel.getStyleClass().add("label");
        userFacingLabel.getStyleClass().add("user-facing-label");
        userFacingLabel.setMouseTransparent(true);
        nextLabel = new Label();
        nextLabel.getStyleClass().add("label");
        nextLabel.setMouseTransparent(true);
        tempLabel = new Label();
        tempLabel.getStyleClass().add("label");
        tempLabel.setMouseTransparent(true);

        drawingPane = new StackPane();
        drawingPane.getStyleClass().add("drawing-pane");

        contentBox = new VBox();
    }

    private void initializeAnimations() {
        Duration duration = Duration.millis(200);
        double offset = 0;

        // Animation for scrolling up
        TranslateTransition prevTransitionUp = new TranslateTransition(duration, prevLabel);
        prevTransitionUp.setFromY(prevLabel.getLayoutY());
        prevTransitionUp.setToY(prevLabel.getLayoutY());

        TranslateTransition userFacingTransitionUp = new TranslateTransition(duration, userFacingLabel);
        userFacingTransitionUp.setFromY(userFacingLabel.getLayoutY());
        userFacingTransitionUp.setToY(prevLabel.getLayoutY());

        TranslateTransition nextTransitionUp = new TranslateTransition(duration, nextLabel);
        nextTransitionUp.setFromY(nextLabel.getLayoutY());
        nextTransitionUp.setToY(userFacingLabel.getLayoutY());

        TranslateTransition tempTransitionUp = new TranslateTransition(duration, tempLabel);
        tempTransitionUp.setFromY(nextLabel.getLayoutY() + nextLabel.getHeight());
        tempTransitionUp.setToY(nextLabel.getLayoutY());

        upAnimation = new ParallelTransition(prevTransitionUp, userFacingTransitionUp, nextTransitionUp, tempTransitionUp);

        // Animation for scrolling down
        TranslateTransition tempTransitionDown = new TranslateTransition(duration, tempLabel);
        tempTransitionDown.setFromY(prevLabel.getLayoutY() - prevLabel.getHeight());
        tempTransitionDown.setToY(prevLabel.getLayoutY());

        TranslateTransition prevTransitionDown = new TranslateTransition(duration, prevLabel);
        prevTransitionDown.setFromY(prevLabel.getLayoutY());
        prevTransitionDown.setToY(userFacingLabel.getLayoutY());

        TranslateTransition userFacingTransitionDown = new TranslateTransition(duration, userFacingLabel);
        userFacingTransitionDown.setFromY(userFacingLabel.getLayoutY());
        userFacingTransitionDown.setToY(nextLabel.getLayoutY());

        TranslateTransition nextTransitionDown = new TranslateTransition(duration, nextLabel);
        nextTransitionDown.setFromY(nextLabel.getLayoutY());
        nextTransitionDown.setToY(nextLabel.getLayoutY());

        downAnimation = new ParallelTransition(prevTransitionDown, userFacingTransitionDown, nextTransitionDown, tempTransitionDown);
    }

    private void layoutParts() {

        contentBox.getChildren().addAll(prevLabel, userFacingLabel, nextLabel);
        contentBox.setAlignment(Pos.CENTER);
        drawingPane.getChildren().addAll(border, background, contentBox);

        StackPane.setAlignment(border, Pos.CENTER);
        StackPane.setAlignment(background, Pos.CENTER);
        StackPane.setAlignment(contentBox, Pos.CENTER);

        getChildren().add(drawingPane);
    }

    private void setupEventHandlers() {
        border.setOnScroll(event -> {
            if( event.getDeltaY() < 0 ){
                getSkinnable().increase();
            }else{
                getSkinnable().decrease();
            }
        });

        border.setOnMouseClicked(event -> border.requestFocus());
        border.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case UP:
                    getSkinnable().increase();
                    event.consume();
                    break;
                case DOWN:
                    getSkinnable().decrease();
                    event.consume();
                    break;
            }
        });
    }

    private void setupValueChangedListeners() {
        getSkinnable().indexProperty().addListener((observable, oldValue, newValue) -> {
            getSkinnable().setAllTexts(newValue.intValue());
            if (newValue.intValue() > oldValue.intValue()) {
                upAnimation.play();
            } else if (newValue.intValue() < oldValue.intValue()) {
                downAnimation.play();
            }

        });
        focused.addListener(((observable, oldValue, newValue) -> {
            if (newValue){
                border.setStroke(Color.YELLOW);
            } else {
                border.setStroke(Color.BLACK);
            }
        }));
    }

    private void setupBindings() {
        prevLabel.textProperty().bind(getSkinnable().prevTextProperty());
        userFacingLabel.textProperty().bind(getSkinnable().userFacingTextProperty());
        nextLabel.textProperty().bind(getSkinnable().nextTextProperty());
        tempLabel.textProperty().bind(getSkinnable().tempTextProperty());
        focused.bind( border.focusedProperty() );
    }

    private void updateUI() {
        getSkinnable().setAllTexts(getSkinnable().indexProperty().getValue());
    }
}
