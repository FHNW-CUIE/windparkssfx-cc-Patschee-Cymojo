package cuie.project.infinite_roll_selection.demo;

import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import cuie.project.infinite_roll_selection.infinite_roll_selection;

class DemoPane extends BorderPane {
    private infinite_roll_selection infiniterollselection;

    private Slider ageSlider;

    private CheckBox  readOnlyBox;
    private CheckBox  mandatoryBox;
    private TextField labelField;

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

        infiniterollselection = new infinite_roll_selection();

        ageSlider = new Slider(0, 130, 0);

        readOnlyBox = new CheckBox();
        readOnlyBox.setSelected(false);

        mandatoryBox = new CheckBox();
        mandatoryBox.setSelected(true);

        labelField = new TextField();
    }

    private void layoutControls() {
        setCenter(infiniterollselection);
        VBox box = new VBox(10,
                            new Label("Business Control Properties"),
                            new Label("Age")      , ageSlider,
                            new Label("readOnly") , readOnlyBox,
                            new Label("mandatory"), mandatoryBox,
                            new Label("Label")    , labelField);
        box.setPadding(new Insets(10));
        box.setSpacing(10);
        setRight(box);
    }

    private void setupValueChangeListeners() {
    }

    private void setupBindings() {
        ageSlider.valueProperty()      .bindBidirectional(model.ageProperty());
        labelField.textProperty()      .bindBidirectional(model.age_LabelProperty());
        readOnlyBox.selectedProperty() .bindBidirectional(model.age_readOnlyProperty());
        mandatoryBox.selectedProperty().bindBidirectional(model.age_mandatoryProperty());

        infiniterollselection.valueProperty()    .bindBidirectional(model.ageProperty());
        infiniterollselection.labelProperty()    .bind(model.age_LabelProperty());
        infiniterollselection.readOnlyProperty() .bind(model.age_readOnlyProperty());
        infiniterollselection.mandatoryProperty().bind(model.age_mandatoryProperty());
    }

}
