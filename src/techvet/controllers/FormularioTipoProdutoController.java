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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import model.TipoProduto;
import techvet.DocFXML;
import techvet.Utils;

/**
 * @author Henrique Faria e Sergio Araujo
 */
public class FormularioTipoProdutoController implements Initializable {

    @FXML 
    private TextField fieldNome;
    @FXML
    private TextArea txtDesc;

    private final Pane content;
    
    public FormularioTipoProdutoController(Pane content) {
        this.content = content;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fieldNome.addEventFilter(KeyEvent.KEY_TYPED, Utils.validacaoLimiteMax(40));
//        fieldDesc.addEventFilter(KeyEvent.KEY_TYPED, Util.validacaoLimiteMax(200));
    }    
    
    @FXML
    public void cliqueConfirmar(ActionEvent event) {
        if (osDadosSaoValidos()) {
            insereTipoProdutoBD();
            mudaContent();
        }
    }
    
    @FXML
    public void cliqueCancelar(ActionEvent event) {
        
    }
    
    private boolean osDadosSaoValidos() {
        boolean saoValidos = true;
        
        if (fieldNome.getText().trim().isEmpty()) {
            saoValidos = false;
        }
        
        
        return saoValidos;
    }
    
    private void insereTipoProdutoBD() {
        TipoProduto tipo = new TipoProduto();
        tipo.setNome(fieldNome.getText());
        tipo.setDescricao(txtDesc.getText());
        tipo.createT();
    }
    
    private void mudaContent() {
        Initializable controller = new ListaArtigosController(false, content);
        try {
            Utils.mudaContentPara(DocFXML.LISTAARTIGOS, controller, content);
        } catch (IOException e) {
        }
    }
    
}
