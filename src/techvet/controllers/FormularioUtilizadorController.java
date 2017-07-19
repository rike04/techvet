/*
 *
 */
package techvet.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import model.Utilizador;
import techvet.DocFXML;
import techvet.Utils;

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
    private ChoiceBox boxFuncao;
    
    private final Pane content;
    
    public FormularioUtilizadorController(Pane content) {
        this.content = content;
    }
    
    public FormularioUtilizadorController() {
        this.content = null;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        popularChoiceBox();
    } 
    
    public String getNomeUtilizador() {
        return fieldNomeUtilizador.getText();
    }
    
    private void popularChoiceBox() {
        ObservableList<String> escolhasFuncao = FXCollections.observableArrayList();
        escolhasFuncao.add("Administrador");
        escolhasFuncao.add("Enfermeiro");
        escolhasFuncao.add("Médico");
        boxFuncao.getItems().addAll(escolhasFuncao);
        boxFuncao.getSelectionModel().select(0);
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
            Utils.mudaContentPara(DocFXML.LISTAUTILIZADORES, controller, content);
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
        
        if (!fieldPasse.getCharacters().toString().contentEquals(fieldPasseConfirmar.getCharacters())) {
            saoValidos = false;
            System.out.println("Passes incorrectas");
        }
        
        return saoValidos;
    }
    
    private void inserirUtilizadorBD() {
        Utilizador user = new Utilizador();
        user.setNome(fieldNome.getText());
        user.setUsername(fieldNomeUtilizador.getText());
        user.setPassword(fieldPasse.getCharacters().toString());
        user.setFuncao(boxFuncao.getSelectionModel().getSelectedItem().toString());
        user.createT();
    }
    
    private void mudarContent(){
        Initializable controller = new ListaUtilizadoresController(false);
        try {
            Utils.mudaContentPara(DocFXML.LOGIN, controller, content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
