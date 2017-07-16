/*
 * 
 */
package techvet.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import techvet.DocFXML;
import techvet.Util;

/**
 * FXML Controller class
 *
 * @author rike4
 */

public class BotoesConsultaController implements Initializable {
    
    private final Pane content;
    
    public BotoesConsultaController(Pane content) {
        this.content= content;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    @FXML 
    public void cliqueListaConsultas(ActionEvent event) {
        Initializable controller = new ListaConsultasController(false);
        try {
            Util.mudaContentPara(DocFXML.LISTACONSULTAS, controller, content);
        } catch (IOException e) {
        }
    }
    
    @FXML
    public void abreCriarConsulta(ActionEvent event) { 
        FormularioConsultaController m = new FormularioConsultaController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(DocFXML.FORMULARIOCONSULTA.getPath()));
        loader.setController(m);
        content.getChildren().clear();
        try {
            content.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }        
    }
    
    @FXML
    public void abre(ActionEvent event) {

    }
    
    @FXML
    public void cliqueCriarTipoConsulta(ActionEvent event) {
        Initializable controller = new FormularioTipoConsultaController(content);
        try {
            Util.mudaContentPara(DocFXML.FORMULARIOTIPOCONSULTA, controller, content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
