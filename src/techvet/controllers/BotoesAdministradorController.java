/*
 * 
 */
package techvet.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import techvet.DocFXML;
import techvet.Utils;

/**
 * FXML Controller class
 *
 * @author rike4
 */
public class BotoesAdministradorController implements Initializable {

    
    private final Pane content;
    
    public BotoesAdministradorController(Pane content) {
        this.content = content;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }  
    
        @FXML 
    public void cliqueCriarUtilizador(ActionEvent event) {
        Initializable controller = new FormularioUtilizadorController(content);
        try {
            Utils.mudaContentPara(DocFXML.FORMULARIOUTILIZADOR, controller, content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void cliqueVerUtilizadores(ActionEvent event) {
        Initializable controller = new ListaUtilizadoresController(false, content);
        try {
            Utils.mudaContentPara(DocFXML.LISTAUTILIZADORES, controller, content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
