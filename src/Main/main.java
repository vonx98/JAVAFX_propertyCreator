package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class main extends Application {


    @Override
    public void start(Stage stage) throws Exception {


        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/mainScreen.fxml")));
        stage.setTitle("JAVAFX Property Creator");
        stage.getIcons().add(new javafx.scene.image.Image(Objects.requireNonNull(Objects.requireNonNull(this.getClass().getResource("/ICONS/finalIcon.png")).toExternalForm())));
        Scene scene = new Scene(root);
        //stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(true);
        stage.setMaximized(false);
        stage.setScene(scene);
        stage.show();

    }


}
