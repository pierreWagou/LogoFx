package logogui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LogoApplication extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/logogui/logoview.fxml"));
		Parent root = loader.load();
		LogoController controller = loader.getController();
		stage.setTitle("Logo Application");
		stage.setScene(new Scene(root,1200,600));
		stage.show();
		stage.setOnCloseRequest(evt -> controller.closeApplication());
	}

	public static void main(String[] args) {
		launch(args);
	}
}
