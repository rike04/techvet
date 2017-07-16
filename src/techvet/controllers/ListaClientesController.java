/*
 * 
 */

package techvet.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Cliente;
import techvet.GUIUtils;

/**
 * @author rike4
 */
public class ListaClientesController implements Initializable {

    @FXML
    private TableView<Cliente> tabelaClientes;
    @FXML
    private TableColumn<Cliente, String> colNome;
    @FXML
    private TableColumn<Cliente, String> colMorada;
    @FXML
    private TableColumn<Cliente, String> colTele;
    @FXML
    private TableColumn<Cliente, String> colMail;
    @FXML
    private TableColumn<Cliente, String> colNIF;
    @FXML
    private Button botaoSelecionar;
    @FXML
    private Button botaoCancelar;
    @FXML
    private TextField filtroProcurar;
    @FXML
    private CheckBox checkPacientes;
    
    private boolean foiConfirmado;
    private final boolean devolveEscolha;

    public ListaClientesController(boolean devolveEscolha) {
        this.foiConfirmado = false;
        this.devolveEscolha = devolveEscolha;
    }        
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        botaoSelecionar.setVisible(devolveEscolha);
        botaoSelecionar.setDisable(!devolveEscolha);        
        botaoCancelar.setVisible(devolveEscolha);
        botaoCancelar.setDisable(!devolveEscolha);
        
        tabelaClientes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabelaClientes.setPlaceholder(new Label("Nao existem clientes registados."));
        
        //Atribui o valor que cada coluna ira ter 
        colNome.setCellValueFactory(dadosCell -> 
                new SimpleStringProperty(dadosCell.getValue().getNome()));
        colMorada.setCellValueFactory(dadosCell ->
                new SimpleStringProperty(dadosCell.getValue().getMorada()));
        colTele.setCellValueFactory(dadosCell ->
                new SimpleStringProperty(dadosCell.getValue().getTelemovel()));
        colMail.setCellValueFactory(dadosCell ->
                new SimpleStringProperty(dadosCell.getValue().getEmail()));
        colNIF.setCellValueFactory(dadosCell -> 
                new SimpleStringProperty(dadosCell.getValue().getNif()));
        
        tabelaClientes.setItems(FXCollections.observableList(leListaClientes()));
        
        GUIUtils.autoFitTable(tabelaClientes);
    }  
    
    private List<Cliente> leListaClientes() {
        List<Cliente> listaClientes = new ArrayList<>();
        try {
            listaClientes = Cliente.retrieveAll();
        } catch (Exception e) {
            System.out.println("Erro impossivel.");
            e.printStackTrace();
        }
        return listaClientes;
    }
    
    public boolean foiSelecionadaOpcao() {
        return foiConfirmado;
    }
    
    public Cliente getClienteSelecionado() {
        return tabelaClientes.getSelectionModel().getSelectedItem();
    }
    
    @FXML 
    public void cliqueSelecionar(ActionEvent event) {
        foiConfirmado = true;
        fechaJanela(event);
    }
    
    @FXML 
    public void cliqueCancelar(ActionEvent event) {
        foiConfirmado = false;
        fechaJanela(event);
    }

    private void fechaJanela(Event event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }
}
 