package cuie.project.infinite_roll_selection.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class DemoStarter extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        PresentationModel model = new PresentationModel();
        Region rootPanel = new DemoPane(model);

        Scene scene = new Scene(rootPanel);

        primaryStage.setTitle("Super cool infinite roll selection custom business control demo");
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
