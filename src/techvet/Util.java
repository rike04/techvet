/*
 * 
 */
package techvet;

import java.io.IOException;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author rike4
 */
public class Util {
    
    private Util(){}
    
    public static void mudaScenePara(DocFXML doc, Initializable controller, Event event) throws IOException{
        Stage stage =(Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(Util.class.getResource(doc.getPath()));
        loader.setController(controller);
        Parent root =  loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();  
    }
    
    public static void mudaContentPara(DocFXML doc, Initializable controller, Pane content) throws IOException {
        FXMLLoader loader = new FXMLLoader(Util.class.getResource(doc.getPath()));
        loader.setController(controller);
        content.getChildren().clear();
        content.getChildren().add(loader.load());
    }
    
    public static Stage preparaNovaJanela(Stage owner) {
        Stage stage = new Stage();
        stage.initOwner(owner);
        stage.setAlwaysOnTop(true);
        stage.initModality(Modality.WINDOW_MODAL);
        return stage;
    }
    
    public static EventHandler<KeyEvent> validacaoNumerica(int limite) {
        return (KeyEvent event) -> {
            TextField txtField = (TextField) event.getSource();                
            if (txtField.getText().length() >= limite) {                    
                event.consume();
            }
            if(event.getCharacter().matches("[0-9.]")){ 
                if(txtField.getText().contains(".") && event.getCharacter().matches("[.]")){
                    event.consume();
                }else if(txtField.getText().length() == 0 && event.getCharacter().matches("[.]")){
                    event.consume(); 
                }
            } else{
                event.consume();
            }
        };
    }
    
    public static EventHandler<KeyEvent> validacaoPrecos(int limite) {
        return (KeyEvent event) -> {
            TextField txtField = (TextField) event.getSource();                
            if (txtField.getText().length() >= limite) {                    
                event.consume();
            }
            if(!event.getCharacter().matches("[0-9.]")){ 
                event.consume();
            }             
        };
    }
    
    public static EventHandler<KeyEvent> validacaoLimiteMax(int limite) {
        return (KeyEvent event) -> {
            TextField txtField = (TextField) event.getSource();                
            if (txtField.getText().length() >= limite) {                    
                event.consume();
            }
        };
    }
}
