package cuie.project.infinite_roll_selection;

import javafx.animation.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import javafx.util.Duration;

class BusinessSkin extends SkinBase<InfiniteRollSelection> {
    private static final int BORDER_WIDTH = 2;
    private static final int CONTROL_HEIGHT = 50;
    private static final int CONTROL_WIDTH = 150;
    private static final int ARTBOARD_HEIGHT = CONTROL_HEIGHT + (2*BORDER_WIDTH);
    private static final int ARTBOARD_WIDTH = CONTROL_WIDTH + (2*BORDER_WIDTH);

    private static final double prevPosY = BORDER_WIDTH + (ARTBOARD_HEIGHT / 8);
    private static final double uFPosY = (CONTROL_HEIGHT/2) + BORDER_WIDTH ;
    private static final double nextPosY = ARTBOARD_HEIGHT - prevPosY;
    private static final double tempPosY = 0;

    private static final String STYLE_CSS = "style.css";

    private final BooleanProperty focused = new SimpleBooleanProperty();

    private Pane drawingPane;
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

    private Timeline tl;

    BusinessSkin(InfiniteRollSelection control) {
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
        background.setY(BORDER_WIDTH);
        background.setX(BORDER_WIDTH);
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

        tempLabel = createCenteredText((ARTBOARD_WIDTH)/2, tempPosY, "label");
        //tempLabel.getStyleClass().add("label");
        tempLabel.setMouseTransparent(true);
        tempLabel.setVisible(false);


        drawingPane = new Pane();
        drawingPane.getStyleClass().add("drawing-pane");
        drawingPane.setPrefSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
        drawingPane.setMaxSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
        drawingPane.setMinSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);

        contentBox = new Pane();
        contentBox.setMouseTransparent(true);
        contentBox.setPrefSize(CONTROL_WIDTH, CONTROL_HEIGHT);
        contentBox.setMaxSize(CONTROL_WIDTH, CONTROL_HEIGHT);
        contentBox.setMinSize(CONTROL_WIDTH, CONTROL_HEIGHT);
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
        Duration duration = Duration.millis(200);
        Duration shortDuration = Duration.millis(50);
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
        userFacingUpScale.setToY(0.75);
        userFacingUpScale.setFromX(1);
        userFacingUpScale.setToX(0.75);

        TranslateTransition nextUpTrans = new TranslateTransition(duration, nextLabel);
        nextUpTrans.setByY(uFPosY-nextPosY);

        ScaleTransition nextUpScale = new ScaleTransition(duration, nextLabel);
        nextUpScale.setFromY(1);
        nextUpScale.setToY(1.3333333333);
        nextUpScale.setFromX(1);
        nextUpScale.setToX(1.3333333333);

        TranslateTransition tempUpTrans = new TranslateTransition(duration, tempLabel);
        tempUpTrans.setFromY(ARTBOARD_HEIGHT);
        tempUpTrans.setToY(ARTBOARD_HEIGHT-prevPosY);

        ScaleTransition tempUpScale = new ScaleTransition(duration, tempLabel);
        tempUpScale.setFromY(0);
        tempUpScale.setToY(1);

        upAnimation = new ParallelTransition(prevUpTrans, prevUpScale, userFacingUpTrans, userFacingUpScale, nextUpTrans, nextUpScale, tempUpTrans, tempUpScale);
        upAnimation.setOnFinished(event -> resetAnimations());

        // Animation for scrolling down
        TranslateTransition tempDownTrans = new TranslateTransition(duration, tempLabel);
        tempDownTrans.setFromY(-tempPosY);
        tempDownTrans.setToY(prevPosY-tempPosY);

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
        userFacingDownScale.setToY(0.75);
        userFacingDownScale.setFromX(1);
        userFacingDownScale.setToX(0.75);

        TranslateTransition nextDownTrans = new TranslateTransition(duration, nextLabel);
        nextDownTrans.setByY(ARTBOARD_HEIGHT-nextPosY);

        ScaleTransition nextDownScale = new ScaleTransition(duration, nextLabel);
        nextDownScale.setFromY(1);
        nextDownScale.setToY(0);


        downAnimation = new ParallelTransition(tempDownTrans, tempDownScale, prevDownTrans, prevDownScale, userFacingDownTrans, userFacingDownScale, nextDownTrans, nextDownScale);
        downAnimation.setOnFinished(event -> resetAnimations());

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
            newIndex = newValue.intValue();
            int max = getSkinnable().getValues().size() -1;
            boolean inc = false;


            if (newIndex == 0){
                if (oldValue.intValue() == max){
                    inc = true;
                }
            } else if (newIndex == max){
                if (oldValue.intValue() == 0){
                    inc = false;
                } else {
                    inc = true;
                }
            } else if (newValue.intValue() > oldValue.intValue()) {
                inc = true;
            } else {
                inc = false;
            }

            getSkinnable().setTempLabelText(newIndex, inc);
            tempLabel.setVisible(true);
            if (inc){
                upAnimation.play();
            } else{
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
        userFacingLabel.textProperty().bind(getSkinnable().selectedTextProperty());
        nextLabel.textProperty().bind(getSkinnable().nextTextProperty());
        tempLabel.textProperty().bind(getSkinnable().tempTextProperty());
        focused.bind( border.focusedProperty() );
    }

    private void updateUI() {
        getSkinnable().setAllTexts(getSkinnable().indexProperty().getValue());
    }
}
