package cuie.project.infinite_roll_selection;

import java.util.ArrayList;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.text.Font;

public class Infinite_roll_selection extends Control {

    private ArrayList<String> values;

    //todo: Integer bei Bedarf ersetzen
    private final IntegerProperty index = new SimpleIntegerProperty();
    private final StringProperty userFacingText = new SimpleStringProperty();



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
        setIndex(getIndex() + 1);
    }

    public void decrease() {
        setIndex(getIndex() - 1);
    }

    private void initializeSelf() {
         getStyleClass().add("infinite-roll-selection");
    }

    //todo: durch geeignete Konvertierungslogik ersetzen
    private void addValueChangeListener() {

    }

    //todo: Forgiving Format implementieren

    public void loadFonts(String... font){
        for(String f : font){
            Font.loadFont(getClass().getResourceAsStream(f), 0);
        }
    }

    public void addStylesheetFiles(String... stylesheetFile){
        for(String file : stylesheetFile){
            String stylesheet = getClass().getResource(file).toExternalForm();
            getStylesheets().add(stylesheet);
        }
    }


    private int convertToInt(String userInput) {
        return Integer.parseInt(userInput);
    }



    // alle  Getter und Setter
    public int getIndex() {
        return index.get();
    }

    public IntegerProperty indexProperty() {
        return index;
    }

    public void setIndex(int index) {
        this.index.set(index);
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

    public ArrayList<String> getValues() {
        return values;
    }
}
