package cuie.project.infinite_roll_selection;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

//todo: durch eigenen Skin ersetzen
class BusinessSkin extends SkinBase<Infinite_roll_selection> {
    private static final int BORDER_WIDTH = 10;

    private static final String STYLE_CSS = "style.css";

    private final BooleanProperty focused = new SimpleBooleanProperty();

    private StackPane drawingPane;

    private Rectangle border;
    private Rectangle background;
    private Label prevLabel;
    private Label userFacingLabel;
    private Label nextLabel;
    private Label tempLabel;

    BusinessSkin(Infinite_roll_selection control) {
        super(control);
        initializeSelf();
        initializeParts();
        layoutParts();
        setupAnimations();
        setupEventHandlers();
        setupValueChangedListeners();
        setupBindings();

        getSkinnable().setAllTexts(getSkinnable().indexProperty().getValue());
    }

    private void initializeSelf() {
        getSkinnable().addStylesheetFiles(STYLE_CSS);
    }

    private void initializeParts() {

        border = new Rectangle(300+(BORDER_WIDTH*2), 100+(BORDER_WIDTH*2));
        border.getStyleClass().add("border");

        background = new Rectangle(300, 100, Color.GRAY);
        background.getStyleClass().add("background-rect");
        background.setMouseTransparent(true);

        prevLabel = new Label();
        prevLabel.getStyleClass().add("label");
        prevLabel.setMouseTransparent(true);
        userFacingLabel = new Label();
        userFacingLabel.getStyleClass().add("label");
        userFacingLabel.setMouseTransparent(true);
        nextLabel = new Label();
        nextLabel.getStyleClass().add("label");
        nextLabel.setMouseTransparent(true);
        tempLabel = new Label();
        tempLabel.getStyleClass().add("label");
        tempLabel.setMouseTransparent(true);

        drawingPane = new StackPane();
        drawingPane.getStyleClass().add("drawing-pane");
    }

    private void layoutParts() {
       drawingPane.getChildren().add(border);
       drawingPane.getChildren().add(background);
       drawingPane.getChildren().add(prevLabel);
       drawingPane.getChildren().add(userFacingLabel);
       drawingPane.getChildren().add(nextLabel);
       //drawingPane.getChildren().add(tempLabel);

        StackPane.setAlignment(border, Pos.CENTER);
        StackPane.setAlignment(background, Pos.CENTER);
        StackPane.setAlignment(prevLabel, Pos.TOP_CENTER);
        StackPane.setAlignment(userFacingLabel, Pos.CENTER);
        StackPane.setAlignment(nextLabel, Pos.BOTTOM_CENTER);

        getChildren().add(drawingPane);
    }

    private void setupAnimations() {
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
        getSkinnable().indexProperty().addListener((observable, oldValue, newValue) -> getSkinnable().setAllTexts(newValue.intValue()));
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
}
