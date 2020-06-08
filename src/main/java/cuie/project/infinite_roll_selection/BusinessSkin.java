package cuie.project.infinite_roll_selection;

import javafx.animation.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import javafx.scene.transform.Scale;
import javafx.util.Duration;

class BusinessSkin extends SkinBase<Infinite_roll_selection> {
    private static final int BORDER_WIDTH = 2;
    private static final int CONTROL_HEIGHT = 50;
    private static final int CONTROL_WIDTH = 150;
    private static final int ARTBOARD_HEIGHT = CONTROL_HEIGHT + (2*BORDER_WIDTH);
    private static final int ARTBOARD_WIDTH = CONTROL_WIDTH + (2*BORDER_WIDTH);

    private static final double prevPosY = 2*BORDER_WIDTH;
    private static final double uFPosY = ARTBOARD_HEIGHT/2;
    private static final double nextPosY = ARTBOARD_HEIGHT - (2*BORDER_WIDTH);

    private static final String STYLE_CSS = "style.css";

    private final BooleanProperty focused = new SimpleBooleanProperty();

    private StackPane drawingPane;
    private Pane contentBox;

    private Rectangle border;
    private Rectangle background;
    private Text prevLabel;
    private Text userFacingLabel;
    private Text nextLabel;
    private Text tempLabel;

    //used for delayed setting of Texts
    private int newIndex;

    private Animation upAnimation;
    private Animation downAnimation;
    private Animation upReverse;
    private Animation downReverse;

    private Timeline tl;

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

        prevLabel = createCenteredText(ARTBOARD_WIDTH/2, prevPosY, "label");
        //prevLabel.getStyleClass().add("label");
        prevLabel.setMouseTransparent(true);

        userFacingLabel = createCenteredText(ARTBOARD_WIDTH/2, uFPosY, "label");
        //userFacingLabel.getStyleClass().add("label");
        userFacingLabel.getStyleClass().add("user-facing-label");
        userFacingLabel.setMouseTransparent(true);

        nextLabel = createCenteredText(ARTBOARD_WIDTH/2, nextPosY, "label");
        //nextLabel.getStyleClass().add("label");
        nextLabel.setMouseTransparent(true);

        tempLabel = createCenteredText((ARTBOARD_WIDTH)/2, ARTBOARD_HEIGHT, "label");
        //tempLabel.getStyleClass().add("label");
        tempLabel.setMouseTransparent(true);


        drawingPane = new StackPane();
        drawingPane.getStyleClass().add("drawing-pane");

        contentBox = new Pane();
        contentBox.setMouseTransparent(true);
        contentBox.setPrefHeight(CONTROL_HEIGHT);
        contentBox.setPrefWidth(CONTROL_WIDTH);
    }

    private Text createCenteredText(double cx, double cy, String styleClass) {
        Text text = new Text();
        text.getStyleClass().add(styleClass);
        text.setTextOrigin(VPos.CENTER);
        text.setTextAlignment(TextAlignment.CENTER);
        double width = cx > CONTROL_WIDTH * 0.5 ? ((CONTROL_WIDTH - cx) * 2.0) : cx * 2.0;
        text.setWrappingWidth(width);
        text.setBoundsType(TextBoundsType.VISUAL);
        text.setY(cy);
        text.setX(cx - (width / 2.0));

        return text;
    }

    private void initializeAnimations() {
        Duration duration = Duration.millis(500);
//        Duration reverse = Duration.millis(5);
        double offset = 0;

        // Animation for scrolling up
        TranslateTransition prevUpTrans = new TranslateTransition(duration, prevLabel);
        prevUpTrans.setByY(-prevPosY);

        ScaleTransition prevUpScale = new ScaleTransition(duration, prevLabel);
        prevUpScale.setFromY(1);
        prevUpScale.setToY(0);

        TranslateTransition userFacingUpTrans = new TranslateTransition(duration, userFacingLabel);
        userFacingUpTrans.setByY(prevPosY-uFPosY);

        ScaleTransition userFacingUpScale = new ScaleTransition(duration, userFacingLabel);
        userFacingUpScale.setFromY(1);
        // Todo: Scale berechnen
        userFacingUpScale.setToY(0.6666666666);
        userFacingUpScale.setFromX(1);
        userFacingUpScale.setToX(0.6666666666);

        TranslateTransition nextUpTrans = new TranslateTransition(duration, nextLabel);
        nextUpTrans.setByY(uFPosY-nextPosY);

        ScaleTransition nextUpScale = new ScaleTransition(duration, nextLabel);
        nextUpScale.setFromY(1);
        nextUpScale.setToY(1.3333333333);
        nextUpScale.setFromX(1);
        nextUpScale.setToX(1.3333333333);

        TranslateTransition tempUpTrans = new TranslateTransition(duration, tempLabel);
        tempUpTrans.setFromY(0);
        tempUpTrans.setToY(nextPosY -ARTBOARD_HEIGHT);

        ScaleTransition tempUpScale = new ScaleTransition(duration, tempLabel);
        tempUpScale.setFromY(0);
        tempUpScale.setToY(1);

        upAnimation = new ParallelTransition(prevUpTrans, prevUpScale, userFacingUpTrans, userFacingUpScale, nextUpTrans, nextUpScale, tempUpTrans, tempUpScale);
        upAnimation.setOnFinished(event -> resetAnimations());

        // Animation for scrolling down
        TranslateTransition tempDownTrans = new TranslateTransition(duration, tempLabel);
        tempDownTrans.setFromY(-CONTROL_HEIGHT);
        tempDownTrans.setToY(prevPosY - CONTROL_HEIGHT);

        ScaleTransition tempDownScale = new ScaleTransition(duration, tempLabel);
        tempDownScale.setFromY(0);
        tempDownScale.setToY(1);


        TranslateTransition prevDownTrans = new TranslateTransition(duration, prevLabel);
        prevDownTrans.setByY(uFPosY-prevPosY);

        ScaleTransition prevDownScale = new ScaleTransition(duration, prevLabel);
        prevDownScale.setFromY(1);
        prevDownScale.setToY(1.3333333333);
        prevDownScale.setFromX(1);
        prevDownScale.setToX(1.3333333333);

        TranslateTransition userFacingDownTrans = new TranslateTransition(duration, userFacingLabel);
        userFacingDownTrans.setByY(nextPosY - uFPosY);

        ScaleTransition userFacingDownScale = new ScaleTransition(duration, userFacingLabel);
        userFacingDownScale.setFromY(1);
        // Todo: Scale berechnen
        userFacingDownScale.setToY(0.6666666666);
        userFacingDownScale.setFromX(1);
        userFacingDownScale.setToX(0.6666666666);

        TranslateTransition nextDownTrans = new TranslateTransition(duration, nextLabel);
        tempDownTrans.setByY(CONTROL_HEIGHT-nextPosY);

        ScaleTransition nextDownScale = new ScaleTransition(duration, nextLabel);
        nextDownScale.setFromY(1);
        nextDownScale.setToY(0);


        downAnimation = new ParallelTransition(tempDownTrans, tempDownScale, prevDownTrans, prevDownScale, userFacingDownTrans, userFacingDownScale, nextDownTrans, nextDownScale);
        downAnimation.setOnFinished(event -> resetAnimations());
//        downReverse = new ParallelTransition();

    }

    private void resetAnimations(){
        getSkinnable().setAllTexts(newIndex);
        prevLabel.setTranslateY(0);
        prevLabel.setScaleX(1);
        prevLabel.setScaleY(1);

        userFacingLabel.setTranslateY(0);
        userFacingLabel.setScaleY(1);
        userFacingLabel.setScaleX(1);

        nextLabel.setTranslateY(0);
        nextLabel.setScaleY(1);
        nextLabel.setScaleX(1);

        tempLabel.setVisible(false);
        tempLabel.setTranslateY(0);
        prevLabel.setScaleX(1);
        prevLabel.setScaleY(1);

    }


    private void layoutParts() {

        contentBox.getChildren().addAll(prevLabel, userFacingLabel, nextLabel, tempLabel);
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
            tempLabel.setVisible(true);
            int tmp = newValue.intValue();
            int max = getSkinnable().getValues().size() -1;
            if (tmp == 0){
                if (oldValue.intValue() == max){
                    upAnimation.play();
                } else {
                    downAnimation.play();
                }
            } else if (tmp == max){
                if (oldValue.intValue() == 0){
                    downAnimation.play();
                } else {
                    upAnimation.play();
                }
            } else if (newValue.intValue() > oldValue.intValue()) {
                upAnimation.play();
            } else {
                downAnimation.play();
            }
            newIndex = newValue.intValue();
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
