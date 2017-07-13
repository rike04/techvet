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
public class BotoesClienteController implements Initializable {

    private final Pane content;
    
    public BotoesClienteController(Pane content) {
        this.content = content;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }    
    
    @FXML 
    public void cliqueCriarCliente(ActionEvent event) {
        Initializable controller = new FormularioClienteController(content);
        try {
            Util.mudaContentPara(DocFXML.FORMULARIOCLIENTE, controller, content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void cliqueVerClientes(ActionEvent event) {
        Initializable controller = new ListaClientesController(false);
        try {
            Util.mudaContentPara(DocFXML.LISTACLIENTES, controller, content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
