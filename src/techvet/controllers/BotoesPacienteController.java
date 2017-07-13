/*
 * 
 */
package techvet.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import techvet.DocFXML;
import techvet.Util;

/**
 * FXML Controller class
 *
 * @author Henrique Faria e Sergio Araujo
 */

public class BotoesPacienteController implements Initializable {

    private final Pane content;
    
    public BotoesPacienteController(Pane content) {
        this.content = content;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }   
    
    public Pane getContent() {
        return content;
    }
    
    @FXML 
    public void cliqueVerPacientes(ActionEvent event) {  
        Initializable controller = new ListaPacientesController(false);
        try {
            Util.mudaContentPara(DocFXML.LISTAPACIENTES, controller, getContent());
        } catch (IOException e) {
            Logger.getLogger(BotoesPacienteController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    @FXML 
    public void cliqueRegistarPaciente(ActionEvent event) {
        FormularioPacienteController controller = new FormularioPacienteController(getContent());
        try {
            Util.mudaContentPara(DocFXML.FORMULARIOPACIENTE, controller, getContent());
        } catch (IOException ex) {
            Logger.getLogger(BotoesPacienteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
