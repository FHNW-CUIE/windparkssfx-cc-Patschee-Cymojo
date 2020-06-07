package cuie.project.infinite_roll_selection;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

//todo: durch eigenen Skin ersetzen
class BusinessSkin extends SkinBase<Infinite_roll_selection> {
    private static final int IMG_SIZE   = 12;
    private static final int IMG_OFFSET = 4;


    private static final int BORDER_WIDTH = 10;

    private static final String STYLE_CSS = "style.css";

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
    }

    private void initializeSelf() {
        getSkinnable().addStylesheetFiles(STYLE_CSS);
    }

    private void initializeParts() {

        border = new Rectangle(300+(BORDER_WIDTH*2), 100+(BORDER_WIDTH*2), Color.BLACK);

        background = new Rectangle(300, 100, Color.GRAY);
        background.getStyleClass().add("background-rect");

        prevLabel = new Label(getSkinnable().getValues().get(0));
        prevLabel.getStyleClass().add("label");
        userFacingLabel = new Label(getSkinnable().getValues().get(1));
        userFacingLabel.getStyleClass().add("label");
        nextLabel = new Label(getSkinnable().getValues().get(2));
        nextLabel.getStyleClass().add("label");
        tempLabel = new Label(getSkinnable().getValues().get(3));
        tempLabel.getStyleClass().add("label");

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
        StackPane.setAlignment(prevLabel, Pos.CENTER);
        StackPane.setAlignment(userFacingLabel, Pos.TOP_CENTER);
        StackPane.setAlignment(nextLabel, Pos.BOTTOM_CENTER);

        getChildren().add(drawingPane);
    }

    private void setupAnimations() {
    }

    private void setupEventHandlers() {
        background.setOnKeyPressed(event -> {
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
        // Todo: on index change, play animation and update skinnable setAllTexts(newValue)
    }

    private void setupBindings() {
    }

    /*private void loadFonts(String... font){
        for(String f : font){
            Font.loadFont(getClass().getResourceAsStream(f), 0);
        }
    }*/
}
