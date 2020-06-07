package cuie.project.infinite_roll_selection;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


class DropDownChooser extends VBox {
    private static final String STYLE_CSS = "dropDownChooser.css";

    private final infinite_roll_selection infiniterollselection;

    private Label tobeReplacedLabel;

    DropDownChooser(infinite_roll_selection infiniterollselection) {
        this.infiniterollselection = infiniterollselection;
        initializeSelf();
        initializeParts();
        layoutParts();
        setupBindings();
    }

    private void initializeSelf() {
        getStyleClass().add("drop-down-chooser");

        String stylesheet = getClass().getResource(STYLE_CSS).toExternalForm();
        getStylesheets().add(stylesheet);
    }

    private void initializeParts() {
        tobeReplacedLabel = new Label("to be replaced");
    }

    private void layoutParts() {
        getChildren().addAll(tobeReplacedLabel);
    }

    private void setupBindings() {
    }
}
