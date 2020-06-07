package cuie.project.infinite_roll_selection.demo;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import cuie.project.infinite_roll_selection.Infinite_roll_selection;

import java.util.ArrayList;

class DemoPane extends BorderPane {
    private Infinite_roll_selection infiniterollselection;

    private Slider indexSlider;
    private Label indexLabel;

    private PresentationModel model;

    DemoPane(PresentationModel model) {
        this.model = model;

        initializeControls();
        layoutControls();
        setupValueChangeListeners();
        setupBindings();
    }

    private void initializeControls() {
        setPadding(new Insets(10));

        // Todo: Richtige arraylist mitgeben
        ArrayList<String> values = new ArrayList<>();
        values.add("Im Umbau");
        values.add("In Betrieb");
        values.add("Ausser Betrieb");
        values.add("Keine Angaben");

        infiniterollselection = new Infinite_roll_selection(values);


        indexSlider = new Slider(0, values.size()-1, 0);
        indexLabel = new Label();
    }

    private void layoutControls() {
        setCenter(infiniterollselection);
        VBox box = new VBox(10,
                            new Label("Business Control Properties"),
                           indexLabel,
                            new Label("Index")      , indexSlider);
        box.setPadding(new Insets(10));
        box.setSpacing(10);
        setRight(box);
    }

    private void setupValueChangeListeners() {
    }

    private void setupBindings() {
        indexLabel.textProperty().bind(model.selectedIndexProperty().asString());

        indexSlider.valueProperty()      .bindBidirectional(model.selectedIndexProperty());

        infiniterollselection.indexProperty()    .bindBidirectional(model.selectedIndexProperty());


    }

}
