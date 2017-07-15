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
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import model.Utilizador;
import techvet.DocFXML;
import techvet.Util;

/**
 * @author rike4
 */
public class FormularioUtilizadorController implements Initializable {

    @FXML
    private TextField fieldNome;
    @FXML
    private TextField fieldNomeUtilizador;
    @FXML
    private PasswordField fieldPasse;
    @FXML
    private PasswordField fieldPasseConfirmar;
    @FXML
    private TextField fieldFuncao;
    
    private final Pane content;
    
    public FormularioUtilizadorController(Pane content) {
        this.content = content;
    }
    
    public FormularioUtilizadorController() {
        this.content = null;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    } 
    
    public String getNomeUtilizador() {
        return fieldNomeUtilizador.getText();
    }
    
    @FXML
    public void cliqueConfirmar(ActionEvent event) {
        if (osDadosSaoValidos()) {
            inserirUtilizadorBD();
            mudarContent();
        }
    }
    
    @FXML
    public void cliqueCancelar(ActionEvent event) {
        ListaUtilizadoresController controller = new ListaUtilizadoresController(false);
        try {
            Util.mudaContentPara(DocFXML.LISTAUTILIZADORES, controller, content);
        } catch (IOException e) {
        }
    }
    
    private boolean osDadosSaoValidos() {
        boolean saoValidos = true;
        
        if (fieldNome.getText().trim().isEmpty()) {
            saoValidos = false;
        }
        
        if (fieldPasse.getCharacters().length() <= 0) {
            saoValidos = false;
        }
        
        if (fieldPasseConfirmar.getCharacters().length() <= 0) {
            saoValidos = false;
        }
        
        if (fieldFuncao.getText().trim().isEmpty()) {
            saoValidos = false;
        }
        
        return saoValidos;
    }
    
    private void inserirUtilizadorBD() {
        Utilizador user = new Utilizador();
        user.setNome(fieldNome.getText());
        user.setUsername(fieldNomeUtilizador.getText());
        user.setPassword(fieldPasse.getCharacters().toString());
        user.setFuncao(fieldFuncao.getText());
        user.createT();
    }
    
    private void mudarContent(){
        Initializable controller = new ListaUtilizadoresController(false);
        try {
            Util.mudaContentPara(DocFXML.LOGIN, controller, content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
}
