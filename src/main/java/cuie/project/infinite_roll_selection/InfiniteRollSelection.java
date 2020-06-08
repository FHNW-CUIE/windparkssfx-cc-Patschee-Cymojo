package cuie.project.infinite_roll_selection;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class InfiniteRollSelection extends Control {

    private ArrayList<String> values;

    private final IntegerProperty index = new SimpleIntegerProperty();
    private final StringProperty prevText = new SimpleStringProperty();
    private final StringProperty selectedText = new SimpleStringProperty();
    private final StringProperty nextText = new SimpleStringProperty();
    private final StringProperty tempText = new SimpleStringProperty();

    public InfiniteRollSelection(){
        values = new ArrayList<>();
        values.add("Im Umbau");
        values.add("In Betrieb");
        values.add("Ausser Betrieb");
        values.add("Keine Angaben");
        initializeSelf();
        addValueChangeListener();
    }

    public InfiniteRollSelection(ArrayList<String> values) {
        this.values = values;
        initializeSelf();
        addValueChangeListener();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new BusinessSkin(this);
    }

    public void increase() {
        int newValue = getIndex() + 1;

        setTempLabelText(newValue,true);

        if (newValue >= values.size()) {
            newValue = 0;
        }

        setIndex(newValue);
    }

    public void decrease() {
        int newValue = getIndex() - 1;

        setTempLabelText(newValue, false);

        if (newValue < 0) {
            newValue = values.size() - 1;
        }

        setIndex(newValue);

    }

    public void setTempLabelText(int value, boolean increase){
        int offset = increase ? 1 : -1;
        value += offset;
        if (value > values.size() -1 ){
            value -= values.size();
        } else if ( value < 0 ){
            value += values.size();
        }

        setTempText(values.get(value));
    }

    private void initializeSelf() {
        getStyleClass().add("infinite-roll-selection");
    }

    private void addValueChangeListener() {
    }

    public void setAllTexts(int newValue) {
        //Auswahl anpassen
        setSelectedText(values.get(newValue));
        setTempLabelText(newValue, true);
        //prev und next Text anpassen:
        if (newValue == 0) {
            setPrevText(values.get(values.size() - 1));
            setNextText(values.get(newValue + 1));
        } else if (newValue == values.size() - 1) {
            setPrevText(values.get(newValue - 1));
            setNextText(values.get(0));
        } else {
            setPrevText(values.get(newValue - 1));
            setNextText(values.get(newValue + 1));
        }
    }

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
    public int getIndex() {
        return index.get();
    }

    public IntegerProperty indexProperty() {
        return index;
    }

    public void setIndex(int index) {
        this.index.set(index);
    }

    public String getSelectedText() {
        return selectedText.get();
    }

    public StringProperty selectedTextProperty() {
        return selectedText;
    }

    public void setSelectedText(String selectedText) {
        this.selectedText.set(selectedText);
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

    public String getTempText() {
        return tempText.get();
    }

    public StringProperty tempTextProperty() {
        return tempText;
    }

    public void setTempText(String tempText) {
        this.tempText.set(tempText);
    }

    public ArrayList<String> getValues() {
        return values;
    }
}
