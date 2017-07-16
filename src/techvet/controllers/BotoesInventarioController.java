/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * FXML Controller class
 *
 * @author rike4
 */
public class BotoesInventarioController implements Initializable {

    
    private final Pane content;
    
    public BotoesInventarioController(Pane content) {
        this.content = content;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    @FXML 
    public void cliqueListaArtigos(ActionEvent event) {
        Initializable controller = new ListaArtigosController(false);
        try {
            Util.mudaContentPara(DocFXML.LISTAARTIGOS, controller, content);
        } catch (IOException e) {
        }
    }
    
    @FXML
    public void cliqueCriarArtigo(ActionEvent event) { 
        Initializable controller = new FormularioArtigoController(content);
        try {
            Util.mudaContentPara(DocFXML.FORMULARIOARTIGO, controller, content);
        } catch (IOException e) {
        }
    }
    
    @FXML
    public void abre(ActionEvent event) {

    }
    
}
