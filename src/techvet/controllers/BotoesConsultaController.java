
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
import techvet.Utils;

/**
 * @author Henrique Faria e Sergio Araujo
 */
public class BotoesConsultaController implements Initializable {
    
    private final Pane content;
    
    public BotoesConsultaController(Pane content) {
        this.content = content;
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    @FXML 
    public void cliqueListaConsultas(ActionEvent event) {
        Initializable controller = new ListaConsultasController(false, content);
        try {
            Utils.mudaContentPara(DocFXML.LISTACONSULTAS, controller, content);
        } catch (IOException e) {
        }
    }
    
    @FXML
    public void cliqueCriarConsulta(ActionEvent event) { 
        FormularioConsultaController m = new FormularioConsultaController(content);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(DocFXML.FORMULARIOCONSULTA.getPath()));
        loader.setController(m);
        content.getChildren().clear();
        try {
            content.getChildren().add(loader.load());
        } catch (IOException e) {
        }        
    }
    
    @FXML
    public void cliqueHorario(ActionEvent event) {
       Initializable controller = new HorarioConsultasController(content);
        try {
            Utils.mudaContentPara(DocFXML.HORARIO, controller, content);
        } catch (IOException e) {
        }
    }
    
    @FXML
    public void cliqueCriarTipoConsulta(ActionEvent event) {
        Initializable controller = new FormularioTipoConsultaController(content);
        try {
            Utils.mudaContentPara(DocFXML.FORMULARIOTIPOCONSULTA, controller, content);
        } catch (IOException e) {
        }
    }
    
}
