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
 * @author Henrique Faria e Sergio Araujo
 */
public class BotoesInternamentoController implements Initializable{
    
    private final Pane content;
    
    public BotoesInternamentoController(Pane content) {
        this.content = content;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    @FXML 
    public void cliqueVerInternamentos(ActionEvent event) {
        Initializable controller = new ListaInternamentosController(false);
        try {
            Utils.mudaContentPara(DocFXML.LISTAINTERNAMENTOS, controller, content);
        } catch (IOException e) {
        }
    }
    
    @FXML
    public void abreCriarInternamento(ActionEvent event) { 
//        FormularioInternamentoController controller = new FormularioInternamentoController(content);
//        try {
//            Utils.mudaContentPara(DocFXML.FORMULARIOINTERNAMENTO, controller, content);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
       
    }

}
