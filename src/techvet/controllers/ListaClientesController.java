/*
 * 
 */

package techvet.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Cliente;
import techvet.DocFXML;
import techvet.GUIUtils;
import techvet.Utils;

/**
 * @author Henrique Faria e Sergio Araujo

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
    
    private boolean foiConfirmado;
    private final boolean devolveEscolha;
    private Pane content;

    public ListaClientesController(boolean devolveEscolha, Pane content) {
        this.foiConfirmado = false;
        this.devolveEscolha = devolveEscolha;
        this.content = content;
    }   
    
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
        
        tabelaClientes.setPlaceholder(new Label("Nao existem clientes registados."));
        
        tabelaClientes.setRowFactory( tv -> {
            TableRow<Cliente> linha = new TableRow<>();
            linha.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!linha.isEmpty()) && content != null) {
                    Cliente c = linha.getItem();
                    abreFormCliente(c);
                }
            });
          return linha;
        });
        
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
        
        /*
         * http://code.makery.ch/blog/javafx-8-tableview-sorting-filtering/
         */
        ObservableList<Cliente> listaClientes = FXCollections.observableList(leListaClientes());
        FilteredList<Cliente> listaFiltrada = new FilteredList<>(listaClientes, p -> true);
        filtroProcurar.textProperty().addListener((observable, oldValue, newValue) -> {
            listaFiltrada.setPredicate(cliente -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }         
                String filtro = newValue.toLowerCase();
                if (cliente.getNome().toLowerCase().contains(filtro)) {
                    return true;
                }
                return false;
            });
        }); 
        SortedList<Cliente> sortedList = new SortedList<>(listaFiltrada);
        sortedList.comparatorProperty().bind(tabelaClientes.comparatorProperty());
        
        tabelaClientes.setItems(sortedList);
        
        GUIUtils.autoFitTable(tabelaClientes);
    }  
    
    private void abreFormCliente(Cliente c) {
        Initializable controller = new FormularioClienteController(content, c); 
        try {
            Utils.mudaContentPara(DocFXML.FORMULARIOCLIENTE, controller, content);
        } catch (IOException e) {}
    }
    
    private List<Cliente> leListaClientes() {
        List<Cliente> listaClientes = new ArrayList<>();
        try {
            listaClientes = Cliente.retrieveAll();
        } catch (Exception e) {
            System.out.println("Oops. Nao foi possivel encontrar clientes.");
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
 