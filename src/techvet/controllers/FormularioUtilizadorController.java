/*
 *
 */
package techvet.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Utilizador;
import techvet.DocFXML;
import techvet.Utils;

/**
 * @author Henrique Faria e Sergio Araujo
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
    private Utilizador utilizador;
    
    public FormularioUtilizadorController(Pane content) {
        this.content = content;
    }
    
    public FormularioUtilizadorController(Pane content, Utilizador utilizador) {
        this.content = content;
        this.utilizador = utilizador;
    }
    
    public FormularioUtilizadorController() {
        this.content = null;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        popularChoiceBox();

        if (utilizador != null) preencherCampos(); 
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
    
    private void preencherCampos() {
        fieldNome.setText(utilizador.getNome());
        fieldNomeUtilizador.setText(utilizador.getUsername());
        fieldPasse.setText(utilizador.getPassword());
        fieldPasseConfirmar.setText(utilizador.getPassword());
        boxFuncao.getSelectionModel().select(utilizador.getFuncao());
    }
    
    @FXML
    public void cliqueConfirmar(ActionEvent event) {
        if (osDadosSaoValidos()) {
            inserirUtilizadorBD();
            mudarContent(event);
        }
    }
    
    @FXML
    public void cliqueCancelar(ActionEvent event) {
        mudarContent(event);
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
        boolean isNovoUtilizador = utilizador == null;
        if (isNovoUtilizador) utilizador = new Utilizador();
        
        utilizador.setNome(fieldNome.getText());
        utilizador.setUsername(fieldNomeUtilizador.getText());
        utilizador.setPassword(fieldPasse.getCharacters().toString());
        utilizador.setFuncao(boxFuncao.getSelectionModel().getSelectedItem().toString());
        
        if (isNovoUtilizador) utilizador.createT();
        else utilizador.updateT();
    }
    
    private void mudarContent(Event event){
        if (content == null) {
            abrirLogin(event);
        } else {
            Initializable controller = new ListaUtilizadoresController(false, content);
            try {
                Utils.mudaContentPara(DocFXML.LISTAUTILIZADORES, controller, content);
            } catch (IOException e) {
            }
        }
    }
    
    private void abrirLogin(Event event) {
        Stage stage =(Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getResource(DocFXML.LOGIN.getPath()));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(FormularioUtilizadorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
