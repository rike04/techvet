/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package techvet.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.ArtigoConsulta;
import model.Consulta;
import model.Produto;
import techvet.DocFXML;
import techvet.GUIUtils;
import techvet.Util;

/**
 * FXML Controller class
 *
 * @author Henrique Faria e Sérgio Araújo
 */
public class ProcessarConsultaController implements Initializable {

    @FXML
    private TextField fieldNomePaciente;
    @FXML 
    private TableView<ArtigoConsulta> listaProdutosConsulta;
    @FXML
    private TableColumn<ArtigoConsulta, String> colNomeProduto;
    @FXML
    private TableColumn<ArtigoConsulta, String> colQuantidade;
    @FXML
    private TableColumn<ArtigoConsulta, Integer> colStock;
    @FXML
    private TableColumn<ArtigoConsulta, Double> colPreco;
    
    private final ObservableList<ArtigoConsulta> listaArtigoConsulta;
    
    private final Consulta consulta;
    
    private final PseudoClass classErro;
    
    public ProcessarConsultaController(Consulta c) {
        this.consulta = c;
        this.listaArtigoConsulta = FXCollections.observableArrayList();
        classErro = PseudoClass.getPseudoClass("error");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fieldNomePaciente.setText(consulta.getPaciente().getNome());
    
        listaProdutosConsulta.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        listaProdutosConsulta.setPlaceholder(new Label("Nao foram adicionados produtos."));
        
        listaProdutosConsulta.setEditable(true);
        
        listaProdutosConsulta.setRowFactory((TableView<ArtigoConsulta> param) -> new TableRow<ArtigoConsulta>() {
            @Override
            protected void updateItem(ArtigoConsulta ac, boolean isEmpty) {
                super.updateItem(ac, isEmpty);
                if (!isEmpty) {
                    if (ac.getQuantidade() == 0) {
                        this.pseudoClassStateChanged(classErro, true);
                    }
                }
            }
        });
        
        colQuantidade.setCellFactory(TextFieldTableCell.forTableColumn());
        colQuantidade.setOnEditCommit((CellEditEvent<ArtigoConsulta, String> t) -> {
            int quantidade;
            try {
                quantidade = Integer.parseInt(t.getNewValue());
            } catch (NumberFormatException e) {
                return ;
            }
            if (quantidade == 0) {
                return ;
            }
            
            if(quantidade > 0 && quantidade < t.getRowValue().getIdProduto().getStock()) {
                t.getRowValue().setQuantidade(quantidade);
            }
            t.getTableView().refresh();
        });
        colQuantidade.setEditable(true);
        
        colNomeProduto.setCellValueFactory(dadosCell -> 
                new SimpleStringProperty(dadosCell.getValue().getIdProduto().getNome()));
        colQuantidade.setCellValueFactory(dadosCell -> 
                new SimpleStringProperty(Integer.toString(dadosCell.getValue().getQuantidade())));
        colPreco.setCellValueFactory(dadosCell -> 
                new SimpleDoubleProperty(dadosCell.getValue().getIdProduto().getPreco() * dadosCell.getValue().getQuantidade()).asObject());    
        colStock.setCellValueFactory(dadosCell -> 
                new SimpleIntegerProperty(dadosCell.getValue().getIdProduto().getStock()).asObject());
        
        listaProdutosConsulta.setItems(listaArtigoConsulta);
        
        GUIUtils.autoFitTable(listaProdutosConsulta);
    }   
    
    @FXML
    public void cliqueAdicionarProduto(ActionEvent event) {
        ListaArtigosController controller;
        try {
            controller = abrirListaProdutos(event);
            
        } catch (IOException e) {
            return ;
        }
        
        if (controller.foiSelecionadaOpcao()) {
            Produto p = controller.getProdutoSelecionado();
            if (verificaProdutoUnico(p)) {
                ArtigoConsulta ac = new ArtigoConsulta();
                ac.setIdProduto(p);
                listaArtigoConsulta.add(ac);
                listaProdutosConsulta.refresh();
            }
        }
    }
    
    private boolean verificaProdutoUnico(Produto p) {
        for(ArtigoConsulta ac: listaArtigoConsulta) {
            if (ac.getIdProduto().equals(p)) {
                return false;
            }
        }
        return true;
    }
    
    private ListaArtigosController abrirListaProdutos(Event event) throws IOException {
        ListaArtigosController controller = new ListaArtigosController(true);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(DocFXML.LISTAARTIGOS.getPath()));
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        
        Stage owner = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Stage stage = Util.preparaNovaJanela(owner);
        stage.setScene(scene);
        stage.showAndWait();
        return controller;
    }
    
    @FXML
    public void cliqueConfirmar(ActionEvent event) {
        
    } 
    
    @FXML
    public void cliqueCancelar(ActionEvent event) {
//        Initializable controller = new ListaConsultasController(false, );
    }

    
}
