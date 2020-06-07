package cuie.project.infinite_roll_selection;

import java.util.ArrayList;
import java.util.regex.Pattern;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.css.PseudoClass;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.text.Font;

public class Infinite_roll_selection extends Control {

    private ArrayList<String> values;

    //todo: Integer bei Bedarf ersetzen
    private final IntegerProperty value = new SimpleIntegerProperty();
    private final StringProperty prevText = new SimpleStringProperty();
    private final StringProperty userFacingText = new SimpleStringProperty();
    private final StringProperty nextText = new SimpleStringProperty();

    public Infinite_roll_selection(ArrayList<String> values) {
        this.values = values;
        initializeSelf();
        addValueChangeListener();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new BusinessSkin(this);
    }

    public void increase() {
        int newValue = getValue() + 1;

        if (newValue >= values.size()) {
            newValue = 0;
        }

        setValue(newValue);
    }

    public void decrease() {
        int newValue = getValue() - 1;

        if (newValue < 0) {
            newValue = values.size() - 1;
        }

        setValue(newValue);
    }

    private void initializeSelf() {
        getStyleClass().add("infinite-roll-selection");
    }

    //todo: durch geeignete Konvertierungslogik ersetzen
    private void addValueChangeListener() {
        value.addListener((observable, oldValue, newValue) -> setAllTexts(newValue.intValue()));
    }

    private void setAllTexts(int newValue) {
        //Auswahl anpassen
        setUserFacingText(values.get(newValue));
        //prev und next Text anpassen:
        if (newValue == 0) {
            setPrevText(values.get(values.size() - 1));
            setNextText(values.get(newValue + 1));
        }
        if (newValue == values.size() - 1) {
            setPrevText(values.get(newValue - 1));
            setNextText(values.get(0));
        }
    }

    //todo: Forgiving Format implementieren

    public void loadFonts(String... font) {
        for (String f : font) {
            Font.loadFont(getClass().getResourceAsStream(f), 0);
        }
    }

    public void addStylesheetFiles(String... stylesheetFile) {
        for (String file : stylesheetFile) {
            String stylesheet = getClass().getResource(file).toExternalForm();
            getStylesheets().add(stylesheet);
        }
    }


    private int convertToInt(String userInput) {
        return Integer.parseInt(userInput);
    }


    // alle  Getter und Setter
    public int getValue() {
        return value.get();
    }

    public IntegerProperty valueProperty() {
        return value;
    }

    public void setValue(int value) {
        this.value.set(value);
    }

    public String getUserFacingText() {
        return userFacingText.get();
    }

    public StringProperty userFacingTextProperty() {
        return userFacingText;
    }

    public void setUserFacingText(String userFacingText) {
        this.userFacingText.set(userFacingText);
    }

    public String getPrevText() {
        return prevText.get();
    }

    public StringProperty prevTextProperty() {
        return prevText;
    }

    public void setPrevText(String prevText) {
        this.prevText.set(prevText);
    }

    public String getNextText() {
        return nextText.get();
    }

    public StringProperty nextTextProperty() {
        return nextText;
    }

    public void setNextText(String nextText) {
        this.nextText.set(nextText);
    }

}
