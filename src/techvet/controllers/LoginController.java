package techvet.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import model.Utilizador;
import techvet.DocFXML;
import techvet.Utils;

/**
 * @author Henrique Faria e Sergio Araujo
 */
public class LoginController implements Initializable {
    
    @FXML 
    private TextField fieldNomeUtilizador;
    @FXML 
    private PasswordField fieldPalavraPasse;
    @FXML 
    private Label labelErroPasse;
    @FXML 
    private Label labelErroNome;
    
    //Mensagens de erro para os campos de textos do formulário
    private final String erroFieldUtilizadorVazio = "O nome de utilizador deve ter entre 3 a 9 caracteres.";
    private final String erroFieldPasseVazia = "A palavra-passe deve ter entre 3 a 9 caracteres.";
   
    //Tipo de estilo que mete a caixa de texto vermelho
    private final PseudoClass errorClass = PseudoClass.getPseudoClass("error");
    
    @FXML
    private void cliqueIniciarSessao(ActionEvent event) {
        resetErros();
        if(isCamposValidos()) {
            String nomeUtilizador = fieldNomeUtilizador.getText();
            String palavraPasse = fieldPalavraPasse.getCharacters().toString();
            iniciarSessao(nomeUtilizador, palavraPasse, event);
        } 
    }
    
    private void resetErros() {
        labelErroNome.setVisible(false);
        labelErroPasse.setVisible(false);
        fieldNomeUtilizador.pseudoClassStateChanged(errorClass, false);
        fieldPalavraPasse.pseudoClassStateChanged(errorClass, false);
    }
    
    @FXML
    public void cliqueRegistar(ActionEvent event) {
        FormularioUtilizadorController controller = new FormularioUtilizadorController();
        try {
            Utils.mudaScenePara(DocFXML.FORMULARIOUTILIZADOR, controller, event);
        } catch (IOException e) {
        }
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {}    
    
    /*
      Verifica se os campos do utilizador e palavra-passe tem valores validos
    */
    private boolean isCamposValidos() {
        boolean saoValidos = true;
        
        if (fieldNomeUtilizador.getText().trim().isEmpty()) {
            labelErroNome.setText(erroFieldUtilizadorVazio);
            labelErroNome.setVisible(true);
            fieldNomeUtilizador.requestFocus();
            fieldNomeUtilizador.pseudoClassStateChanged(errorClass, true);
            saoValidos = false;
        } 
        
        if(fieldPalavraPasse.getCharacters().toString().isEmpty()) {
            labelErroPasse.setText(erroFieldPasseVazia);
            labelErroPasse.setVisible(true);
            fieldPalavraPasse.requestFocus();
            fieldPalavraPasse.pseudoClassStateChanged(errorClass, true);
            saoValidos = false;
        }
        
        return saoValidos;
    }
    
    private void iniciarSessao(String nomeUtilizador, String palavraPasse, ActionEvent event) {
        Utilizador utilizador = new Utilizador();
        try {
            utilizador.readByNome(nomeUtilizador);            
        } catch (Exception e) {
            labelErroNome.setText("Nome de utilizador não encontrado.");
            labelErroNome.setVisible(true);
            fieldNomeUtilizador.requestFocus();
            fieldPalavraPasse.clear();
            return;
        }
        
        if (palavraPasse.equals(utilizador.getPassword())) {
            try {
                Initializable mainTemplate = new Template(utilizador);
                Utils.mudaScenePara(DocFXML.TEMPLATE, mainTemplate, event);
            } catch (IOException e) {
                e.printStackTrace();
            } 
        } else {
            labelErroPasse.setText("Palavra-passe incorrecta.");
            labelErroPasse.setVisible(true);
            fieldPalavraPasse.requestFocus();
        }
    }
    
}
