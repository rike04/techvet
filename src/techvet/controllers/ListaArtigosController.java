/*
 *
 */

package techvet.controllers;

import java.io.IOException;
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
import model.Produto;
import techvet.DocFXML;
import techvet.GUIUtils;
import techvet.Utils;

/**
 * @author rike4
 */
public class ListaArtigosController implements Initializable {
        
    @FXML
    private TableView<Produto> tabela;
    @FXML
    private TableColumn<Produto, String> colNome;
    @FXML
    private TableColumn<Produto, String> colTipoProduto;
    @FXML
    private TableColumn<Produto, Integer> colStock;
    @FXML
    private TableColumn<Produto, Integer> colStockM;
    @FXML
    private TableColumn<Produto, Double> colPreco;
    @FXML
    private TableColumn<Produto, String> colDescricao;

    @FXML
    private Button botaoSelecionar;
    @FXML
    private Button botaoCancelar;
    
    private boolean foiConfirmado;
    private final boolean devolveEscolha;
    private final Pane content;
    
    public ListaArtigosController(boolean devolveEscolha) {
        this.foiConfirmado = false;
        this.devolveEscolha = devolveEscolha;
        this.content = null;
    }
    
    public ListaArtigosController (boolean devolveEscolha, Pane content) { 
        this.foiConfirmado = false;
        this.devolveEscolha = devolveEscolha;
        this.content = content;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        botaoSelecionar.setVisible(devolveEscolha);
        botaoSelecionar.setDisable(!devolveEscolha);
        
        botaoCancelar.setVisible(devolveEscolha);
        botaoCancelar.setDisable(!devolveEscolha);
        
        tabela.setPlaceholder(new Label("Não foram encontrados produtos."));
        
        tabela.setRowFactory( tr -> {
            TableRow<Produto> linha = new TableRow<>();
            linha.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!linha.isEmpty()) && content != null) {
                    Produto p = linha.getItem();
                    abrirEditarProduto(p);
                }
            });
            return linha;
        });
        
        colNome.setCellValueFactory(dadosCell -> 
                new SimpleStringProperty(dadosCell.getValue().getNome()));
        colTipoProduto.setCellValueFactory(dadosCell -> 
                new SimpleStringProperty(dadosCell.getValue().getCodTipo().getNome()));
        colStock.setCellValueFactory(dadosCell -> 
                new SimpleIntegerProperty(dadosCell.getValue().getStock()).asObject());
        colStockM.setCellValueFactory(dadosCell -> 
                new SimpleIntegerProperty(dadosCell.getValue().getStockmin()).asObject());        
        colPreco.setCellValueFactory(dadosCell -> 
                new SimpleDoubleProperty(dadosCell.getValue().getPreco()).asObject());
        colDescricao.setCellValueFactory(dadosCell -> 
                new SimpleStringProperty(dadosCell.getValue().getDescricao()));
        
        tabela.setItems(FXCollections.observableList(leListaProdutos()));
        
        GUIUtils.autoFitTable(tabela);
    }

    private List<Produto> leListaProdutos() {
        List<Produto> lista = new ArrayList<>();
        try {
            lista = Produto.retrieveAll();
        } catch (Exception e) {
        }
        return lista;
    }
    
    private void abrirEditarProduto(Produto p) {
        EditarProdutoController controller = new EditarProdutoController(p, content);
        try {
            Utils.mudaContentPara(DocFXML.EDITARPRODUTO, controller, content);
        }catch (IOException e) {
        }
    }
    
    public boolean foiSelecionadaOpcao() {
        return foiConfirmado;
    }
    
    public Produto getProdutoSelecionado() {
        return tabela.getSelectionModel().getSelectedItem();
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
