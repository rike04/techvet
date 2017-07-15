/*
 * Aplicação  
 */

package techvet;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.Utilizador;

/**
 *
 * @author Henrique Faria e Sérgio Araújo 
 */
public class Aplicacao extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/techvet/views/Login.fxml"));
        
        Scene scene = new Scene(root);
        
        verificaAdminBD();
        
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
    
    private void verificaAdminBD() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Utilizador u = new Utilizador();
                try {
                    u.readByNome("Administrador");
                } catch (Exception e) {
                    System.out.println("Inserindo Administrador");
                   insereAdminBD();
                }
                return null;
            }
        };
        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
    }
    
    private void insereAdminBD() {
        Utilizador u = new Utilizador();
        u.setNome("Administrador");
        u.setUsername("Administrador");
        u.setFuncao("Administrador");
        u.setPassword("passe");
        u.createT();
    }
    
}
