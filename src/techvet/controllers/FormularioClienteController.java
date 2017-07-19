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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import model.Cliente;
import techvet.DocFXML;
import techvet.Utils;

/**
 * @author Henrique Faria e Sergio Araujo
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
    @FXML
    private GridPane tabelaPacientes;
    
    private final Initializable controller;
    private final DocFXML doc;
    private final Pane content;
    
    private final PseudoClass erro = PseudoClass.getPseudoClass("error");
    
    private Cliente cliente;
    
    public FormularioClienteController(Pane content) {
        this.controller = new ListaClientesController(false);
        this.doc = DocFXML.LISTACLIENTES;
        this.content = content;
    }
    
    public FormularioClienteController(Pane content, Cliente cliente) {
        this.controller = new ListaClientesController(false);
        this.doc = DocFXML.LISTACLIENTES;
        this.content = content;
        this.cliente = cliente;
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
        
        fieldCodigoPostal.lengthProperty().addListener((
                ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            String stringCPostal = fieldCodigoPostal.getText();
            //Insere o caracter "-" na posição 5 da string do código postal e mantém-no lá
            preparaCodigoPostal(stringCPostal);
        });    
        
        if(cliente != null) {
            montarTabelaPacientes();
            preencheCampos();
        }
    }    
    
    private void montarTabelaPacientes() {
        Initializable controllerx = new ListaPacientesController(false, cliente);
        try {
            Utils.mudaContentPara(DocFXML.LISTAPACIENTES, controllerx, tabelaPacientes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void preparaCodigoPostal(String stringCPostal) {
        if (stringCPostal.length() >= 4 && !stringCPostal.contains("-")) {
            stringCPostal = stringCPostal.substring(0, 4) + "-" 
                        + stringCPostal.substring(4, stringCPostal.length());
            fieldCodigoPostal.setText(stringCPostal);      
        } else if (stringCPostal.indexOf("-") != 4) {
                   fieldCodigoPostal.setText(stringCPostal.replace("-", ""));
            }
    }
    
    private void preencheCampos() {
        fieldNomeCliente.setText(cliente.getNome());
//        fieldCodigoPostal.setText(cliente.g);
        fieldMorada.setText(cliente.getMorada());
        fieldMail.setText(cliente.getEmail());
        fieldTele.setText(cliente.getTelemovel());
        fieldNIF.setText(cliente.getNif());
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
        boolean isNovoCliente = cliente == null;
        if (isNovoCliente) cliente = new Cliente();
        
        cliente.setNome(fieldNomeCliente.getText());
        cliente.setEmail(fieldMail.getText());
        cliente.setMorada(fieldMorada.getText());
        cliente.setNif(fieldNIF.getText());
        cliente.setTelemovel(fieldTele.getText());
        
        if (isNovoCliente) cliente.createT();
        else cliente.updateT();
    }
    
    private void resetErros() {
        fieldNomeCliente.pseudoClassStateChanged(erro, false);
        fieldMail.pseudoClassStateChanged(erro, false);
        fieldMorada.pseudoClassStateChanged(erro, false);
        fieldNIF.pseudoClassStateChanged(erro, false);
        fieldTele.pseudoClassStateChanged(erro, false);
    }
    
    private void mudarContent() {
        ListaClientesController c = new ListaClientesController(false, content);
        try {
            Utils.mudaContentPara(doc, c, content);
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
