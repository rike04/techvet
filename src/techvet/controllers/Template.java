/*
 * Controlador do template do menu principal 
 */

package techvet.controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Utilizador;
import techvet.DocFXML;
import techvet.Util;

/**
 * @author Henrique Faria e Sergio Araujo
 */

public class Template implements Initializable {
    
    private final Utilizador utilizador;
    
    @FXML 
    private Label dataSistema;   
    @FXML 
    private Label labelNomeUtilizador;    
    @FXML
    private StackPane content;
    @FXML 
    private VBox sideBar;
    
    public Template(Utilizador utilizador) {
        this.utilizador = utilizador;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {    
        mudarEcra(DocFXML.BOTOESCLIENTE); 
        preencherLabels();
    }
    
    public Pane getContent() {
        return content;
    }
    
    public VBox getSideBar() {
        return sideBar;
    }
    
    public Utilizador getUtilizador() {
        return utilizador;
    }
    
    //Labels no canto superior direito que sao o nome e data 
    private void preencherLabels() {
        LocalDate dataLocal = LocalDate.now();
        dataSistema.setText(DateTimeFormatter.ofPattern("yyy/MM/dd").format(dataLocal));
        
        labelNomeUtilizador.setText(getUtilizador().getNome());
    }

    @FXML 
    public void cliqueTerminarSessao(ActionEvent event) {
        ButtonType botaoConfirmar = new ButtonType("Sim", ButtonBar.ButtonData.OK_DONE);
        ButtonType botaoNegar = new ButtonType("Nao", ButtonBar.ButtonData.CANCEL_CLOSE);
        
        //Caixa de confirmação para terminar a sessão
        Alert alerta = new Alert(AlertType.CONFIRMATION);
        alerta.setTitle("Saída");
        alerta.setHeaderText(null);
        alerta.setContentText("Pretende fechar a sessão?");
        Stage stage =(Stage) ((Node) event.getSource()).getScene().getWindow();
        alerta.initOwner(stage);
        
        alerta.getButtonTypes().setAll(botaoConfirmar, botaoNegar);
        
        Optional<ButtonType> result = alerta.showAndWait();
        if(result.get() == botaoConfirmar) {
            try {
                 Parent root = FXMLLoader.load(getClass().getResource("/techvet/views/Login.fxml"));
                 Scene scene = new Scene(root);
                 stage.setScene(scene);
                 stage.show();      
            } catch (IOException e) {
                 e.printStackTrace();
            }
        } 
    }
    
    @FXML
    public void cliqueClientes(ActionEvent event) {
        mudarEcra(DocFXML.BOTOESCLIENTE);
    }
    
    @FXML
    public void cliquePacientes(ActionEvent event) {
        mudarEcra(DocFXML.BOTOESPACIENTE);
    }
    
    @FXML
    public void cliqueConsultas(ActionEvent event) {
        mudarEcra(DocFXML.BOTOESCONSULTA);
    }
    
    @FXML
    public void cliqueInventario(ActionEvent event) {
        
    }
    
    private void mudarEcra(final DocFXML docSideBar) {
        final Initializable controllerSideBar;
        final Initializable controllerContent;
        final DocFXML docContent;
        
        switch (docSideBar) {
            case BOTOESCONSULTA: 
                    docContent = DocFXML.LISTACONSULTAS;
                    controllerSideBar = new BotoesConsultaController(getContent());
                    controllerContent = new ListaConsultasController();
            break;
            
            case BOTOESPACIENTE: 
                    docContent = DocFXML.LISTAPACIENTES;
                    controllerSideBar = new BotoesPacienteController(getContent());
                    controllerContent = new ListaPacientesController();
            break;
            
            case BOTOESCLIENTE: 
                    docContent = DocFXML.LISTACLIENTES;
                    controllerSideBar = new BotoesClienteController(getContent());
                    controllerContent = new ListaClientesController();
            break;
            
            default:
                throw new AssertionError();
        }
        
        try {
            Util.mudaContentPara(docSideBar, controllerSideBar, getSideBar());
            Util.mudaContentPara(docContent, controllerContent, getContent());
        } catch (IOException ex) {
            Logger.getLogger(Template.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
