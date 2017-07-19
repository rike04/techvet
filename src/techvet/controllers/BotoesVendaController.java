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
public class BotoesVendaController implements Initializable {

    private final Pane content;
    
    public BotoesVendaController(Pane content) {
        this.content = content;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }    
    
    @FXML 
    public void cliqueListaVendas(ActionEvent event) {
        Initializable controller = new ListaVendasController(false);
        try {
            Utils.mudaContentPara(DocFXML.LISTAVENDAS, controller, content);
        } catch (IOException e) {
        }
    }
    
    @FXML
    public void cliqueCriarVenda(ActionEvent event) { 
        Initializable controller = new FormularioVendasController();
        try {
            Utils.mudaContentPara(DocFXML.FORMULARIOVENDA, controller, content);
        } catch (IOException e) {
        }
    }
    
}
