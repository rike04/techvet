/*
 * 
 */
package techvet.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import model.Cliente;
import model.Paciente;
import techvet.DocFXML;
import techvet.Util;

/*
 * @author rike4
 */
public class FormularioClienteController implements Initializable {
    
    @FXML 
    private TextField fieldNomeCliente;
    @FXML
    private TextField fieldNIF;
    @FXML
    private TextField fieldCodigoPostal;
    @FXML
    private TextField fieldMorada;
    @FXML
    private TextField fieldTele;
    @FXML 
    private TextField fieldMail;
    
    private final Initializable controller;
    private final DocFXML doc;
    private final Pane content;
    
    private final PseudoClass erro = PseudoClass.getPseudoClass("error");
    
    public FormularioClienteController(Pane content) {
        this.controller = new ListaClientesController(false);
        this.doc = DocFXML.LISTACLIENTES;
        this.content = content;
    }
    
    public FormularioClienteController(Initializable controller, DocFXML doc, Pane content) {
        this.controller = controller;
        this.doc = doc;
        this.content = content;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fieldNomeCliente.addEventFilter(KeyEvent.KEY_TYPED, validacaoLimiteMax(100));
        fieldMorada.addEventFilter(KeyEvent.KEY_TYPED, validacaoLimiteMax(100));
        fieldMail.addEventFilter(KeyEvent.KEY_TYPED, validacaoLimiteMax(40));
        fieldCodigoPostal.addEventFilter(KeyEvent.KEY_TYPED, validacaoLimiteMax(8));        
        fieldNIF.addEventFilter(KeyEvent.KEY_TYPED, validacaoNumerica(9));
        fieldTele.addEventFilter(KeyEvent.KEY_TYPED, validacaoNumerica(9));
        
        //Verifica na BD se o NIF ja se encontra registado 
        fieldNIF.focusedProperty().addListener(
                                (ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue == false && !fieldNIF.getText().trim().isEmpty()) {
                Task<Void> task = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        String nif = fieldNIF.getText();
                        List<Cliente> clientes = Cliente.retrieveByNIF(nif);
                        if(!clientes.isEmpty()) {
                            fieldNIF.pseudoClassStateChanged(erro, true);
                        }
                        return null;
                    }
                };
                Thread t = new Thread(task);
                t.setDaemon(true);
                t.start();
            }
        });
        
        //Insere o caracter "-" na posição 5 da string do código postal e mantém-no lá
        //Ver se da para alterar de modod a ficar como Event Filter 
        fieldCodigoPostal.lengthProperty().addListener((
                ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            
            String stringCPostal = fieldCodigoPostal.getText();
            
            if (stringCPostal.length() >= 4 && !stringCPostal.contains("-")) {
                stringCPostal = stringCPostal.substring(0, 4) + "-" 
                        + stringCPostal.substring(4, stringCPostal.length());
                fieldCodigoPostal.setText(stringCPostal);
                
            } else if (stringCPostal.indexOf("-") != 4) {
                fieldCodigoPostal.setText(stringCPostal.replace("-", ""));
            }
        });    
            
    }    
    
    @FXML
    public void cliqueConfirmar(ActionEvent event) {
        resetErros();
        if (osDadosSaoValidos()) {
                inserirClienteBD();
                mudarContent();
        }
        
    }
    
    private void inserirClienteBD() {
        Cliente cli = new Cliente();
        cli.setNome(fieldNomeCliente.getText());
        cli.setEmail(fieldMail.getText());
        cli.setMorada(fieldMorada.getText());
        cli.setNif(fieldNIF.getText());
        cli.setTelemovel(fieldTele.getText());
        cli.createT();
    }
    
    private void resetErros() {
        fieldNomeCliente.pseudoClassStateChanged(erro, false);
        fieldMail.pseudoClassStateChanged(erro, false);
        fieldMorada.pseudoClassStateChanged(erro, false);
        fieldNIF.pseudoClassStateChanged(erro, false);
        fieldTele.pseudoClassStateChanged(erro, false);
    }
    
    private void mudarContent() {
        ListaClientesController c = new ListaClientesController(false);
        try {
            Util.mudaContentPara(doc, c, content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void cliqueCancelar(ActionEvent event) {
        resetErros();
        mudarContent();
    }
    
    private boolean osDadosSaoValidos() {
        boolean saoValidos = true;
        
        if (!eEmailValido(fieldMail.getText())) {
            fieldMail.pseudoClassStateChanged(erro, true);
            saoValidos = false;
        }
        
        if (fieldNomeCliente.getText().isEmpty()) {
            fieldNomeCliente.pseudoClassStateChanged(erro, true);
            saoValidos = false;
        }
        
        if (fieldMorada.getText().isEmpty()) {
            fieldMorada.pseudoClassStateChanged(erro, true);
            saoValidos = false;
        }
        
        if (fieldCodigoPostal.getText().isEmpty()) {
            fieldCodigoPostal.pseudoClassStateChanged(erro, true);
            saoValidos = false;
        }
        
        if (fieldNIF.getText().isEmpty()) {
            fieldNIF.pseudoClassStateChanged(erro, true);
            saoValidos = false;
        } else {
            if (fieldNIF.getText().length() < 9) {
                fieldNIF.pseudoClassStateChanged(erro, true);
                saoValidos = false;
            }
        }
        
        if (!fieldTele.getText().isEmpty() && fieldTele.getText().length() < 9) {
            fieldTele.pseudoClassStateChanged(erro, true);
            saoValidos = false;
        }
        
        return saoValidos;
    }
    
    private boolean eEmailValido(String email) {
        try {
            InternetAddress enderecoEmail = new InternetAddress(email);
            enderecoEmail.validate();
        } catch (AddressException e) {
            return false;
        }
        return true;
    }
    
    //Sempre que for inserido um caracter vai ser verificado se é um número
    //e se não ultrapassa o limite definido 
    private EventHandler<KeyEvent> validacaoNumerica(int limite) {
        return (KeyEvent event) -> {
            TextField txtField = (TextField) event.getSource();                
            if (txtField.getText().length() >= limite) {                    
                event.consume();
            }
            if(event.getCharacter().matches("[0-9.]")){ 
                if(txtField.getText().contains(".") && event.getCharacter().matches("[.]")){
                    event.consume();
                }else if(txtField.getText().length() == 0 && event.getCharacter().matches("[.]")){
                    event.consume(); 
                }
            } else{
                event.consume();
            }
        };
    }
    
    //Impede que o limite definido para o número de caracteres seja ultrapassado
    private EventHandler<KeyEvent> validacaoLimiteMax(int limite) {
        return (KeyEvent event) -> {
            TextField txtField = (TextField) event.getSource();                
            if (txtField.getText().length() >= limite) {                    
                event.consume();
            }
        };
    }
     
}
