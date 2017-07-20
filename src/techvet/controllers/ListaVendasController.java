/*
 * 
 */
package techvet.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Venda;
import techvet.DocFXML;
import techvet.GUIUtils;
import techvet.Utils;

/**
 * @author Henrique Faria e Sergio Araujo
 */
public class ListaVendasController implements Initializable{
    
      @FXML
    private TableView<Venda> tabelaVendas;
    @FXML
    private TableColumn<Venda, Integer> colCodigo;
    @FXML
    private TableColumn<Venda, String> colData;
    @FXML
    private TableColumn<Venda, String> colCliente;
    @FXML
    private TableColumn<Venda, Double> colTotal;
    @FXML
    private Button botaoSelecionar;
    @FXML
    private Button botaoCancelar;

    private final Pane content;
    
    private final boolean devolveEscolha;
    private boolean foiConfirmado;
    
    public ListaVendasController(boolean devolveEscolha,Pane content) {
        this.devolveEscolha = devolveEscolha;
        this.foiConfirmado = false;
        this.content = content;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
         
        botaoSelecionar.setVisible(devolveEscolha);
        botaoSelecionar.setDisable(!devolveEscolha);        
        botaoCancelar.setVisible(devolveEscolha);
        botaoCancelar.setDisable(!devolveEscolha);
        
        tabelaVendas.setPlaceholder(new Label("Nao existem vendas registadas."));
        
        tabelaVendas.setRowFactory( tv -> {
            TableRow<Venda> linha = new TableRow<>();
            linha.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!linha.isEmpty())) {
                    Venda v = linha.getItem();
                    abrirFormConsulta(v);
                }
            });
          return linha;
        });
        
        //Atribui o valor que cada coluna ira ter 
        colCodigo.setCellValueFactory(dadosCell -> 
                new SimpleIntegerProperty(dadosCell.getValue().getId()).asObject());
        colData.setCellValueFactory(dadosCell ->
                new SimpleStringProperty(dadosCell.getValue().getData().toString()));
        colCliente.setCellValueFactory(dadosCell ->
                new SimpleStringProperty(dadosCell.getValue().getIdCliente().getNome()));
        colTotal.setCellValueFactory(dadosCell ->
                new SimpleDoubleProperty(dadosCell.getValue().getTotal()).asObject());
        
        tabelaVendas.setItems(FXCollections.observableList(leListaVendas()));
        
        GUIUtils.autoFitTable(tabelaVendas);
    }
    
    private void abrirFormConsulta(Venda v) {
        Initializable controller = new FormularioVendaController(content, v);
        try {
            Utils.mudaContentPara(DocFXML.FORMULARIOVENDA, controller, content);
        } catch (Exception e) { e.printStackTrace();
        }
    }
    
    private List<Venda> leListaVendas() {
        List<Venda> lista = new ArrayList<>();
        try {
            lista = Venda.retrieveAll();
        } catch (Exception e) {
        }
        return lista;
    }
    
    public boolean foiSelecionadaOpcao() {
        return foiConfirmado;
    }
    
    public Venda getVendaSelecionada() {
        return tabelaVendas.getSelectionModel().getSelectedItem();
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
