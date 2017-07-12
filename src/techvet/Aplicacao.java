/*
 * Aplicação  
 */

package techvet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Henrique Faria e Sérgio Araújo 
 */
public class Aplicacao extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/techvet/views/Login.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setTitle("TechVet");
        stage.getIcons().add(new Image("/resources/icon.png"));
        stage.setScene(scene);
        stage.show();  
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
