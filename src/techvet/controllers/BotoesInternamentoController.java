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
import techvet.Util;

/**
 *
 * @author rike4
 */
public class BotoesInternamentoController implements Initializable{
    
    private final Pane content;
    
    public BotoesInternamentoController(Pane content) {
        this.content = content;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    @FXML 
    public void cliqueVerInternamentos(ActionEvent event) {
        Initializable controller = new ListaInternamentosController(false);
        try {
            Util.mudaContentPara(DocFXML.LISTAINTERNAMENTOS, controller, content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void abreCriarInternamento(ActionEvent event) { 
        FormularioInternamentoController controller = new FormularioInternamentoController(content);
        try {
            Util.mudaContentPara(DocFXML.FORMULARIOINTERNAMENTO, controller, content);
        } catch (IOException e) {
            e.printStackTrace();
        }
       
    }
    
    @FXML
    public void abre(ActionEvent event) {

    }
}
