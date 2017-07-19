/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package techvet.controllers;

import java.awt.Choice;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import model.Produto;
import model.TipoProduto;
import techvet.DocFXML;
import techvet.Utils;

/**
 * FXML Controller class
 *
 * @author Sérgio Araújo
 */
public class EditarProdutoController implements Initializable {
    
    @FXML 
    private TextField fieldNome;
    @FXML
    private TextArea fieldDescricao;
    @FXML
    private TextField fieldPreco;
    @FXML 
    private TextField fieldStock;
    @FXML
    private TextField fieldStockMin;
    @FXML
    private ChoiceBox<Choice> boxTipoProduto;
    
    
    private final Produto p;
    private final Pane content;

    public EditarProdutoController(Produto p, Pane content) {
       this.p = p; 
       this.content = content;
    }
    
    
   
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        popularChoiceBox();
        
        TipoProduto tp = p.getCodTipo();
        
        fieldNome.setText(p.getNome());
        fieldDescricao.setText(p.getDescricao());
        boxTipoProduto.getSelectionModel().select(tp.getId()-2); 
        fieldPreco.setText(Double.toString(p.getPreco()));
        fieldStock.setText(Integer.toString(p.getStock()));
        fieldStockMin.setText(Integer.toString(p.getStock()));
        
        fieldNome.addEventFilter(KeyEvent.KEY_TYPED, Utils.validacaoLimiteMax(100));
//        fieldDescricao.addEventFilter(KeyEvent.KEY_TYPED, Util.validacaoLimiteMax(200));
        fieldPreco.addEventFilter(KeyEvent.KEY_TYPED, Utils.validacaoPrecos(12));
        fieldStock.addEventFilter(KeyEvent.KEY_TYPED, Utils.validacaoNumerica(5));
        fieldStockMin.addEventFilter(KeyEvent.KEY_TYPED, Utils.validacaoNumerica(5));
    }
    
    @FXML
    public void cliqueConfirmar(ActionEvent event) {
        if (osDadosSaoValidos()) {
            try {
                inserirProdutoBD();
                mudarContent();
            } catch (IOException e) {
                Logger.getLogger(FormularioArtigoController.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
    
    @FXML 
    public void cliqueCancelar(ActionEvent event) {
        try {
            mudarContent();
        } catch (IOException e) {
            Logger.getLogger(FormularioArtigoController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    private void mudarContent() throws IOException{
        ListaArtigosController controller = new ListaArtigosController(false, content);
        Utils.mudaContentPara(DocFXML.LISTAARTIGOS, controller, content);
    }
    
    private Boolean osDadosSaoValidos() {
        boolean saoValidos = true;
        
        if (fieldNome.getText().trim().isEmpty()) {
            saoValidos = false;
        }
        
        if (fieldPreco.getText().trim().isEmpty()) {
            saoValidos = false;
        } else {
            try {
                Double.parseDouble(fieldPreco.getText());
            } catch (NumberFormatException e) {
                saoValidos = false;
            }
        }
        
        if (fieldStock.getText().trim().isEmpty()) {
            saoValidos = false;
        } else {
            try {
                Integer.parseInt(fieldStock.getText());
            } catch (NumberFormatException e) {
                saoValidos = false;
            }
        }
        
        if (fieldStockMin.getText().trim().isEmpty()) {
            saoValidos = false;
        } else {
            try {
                Integer.parseInt(fieldStockMin.getText());
            } catch (NumberFormatException e) {
                saoValidos = false;
            }
        }
        
        return saoValidos;
    }
    
    private void popularChoiceBox() {
        ObservableList<Choice> escolhasTipoProduto = FXCollections.observableArrayList();
        List<TipoProduto> tiposProduto = TipoProduto.retrieveAll();
        
        // Ordena a lista de forma ascendente 
        Collections.sort(tiposProduto, new Comparator<TipoProduto>() {

            public int compare(TipoProduto o1, TipoProduto o2) {
                return o1.getId().compareTo(o2.getId());
            }   
        });
        
        tiposProduto.forEach( (TipoProduto tipo) -> {
            escolhasTipoProduto.add(new Choice(tipo));
        });
        
        boxTipoProduto.getItems().addAll(escolhasTipoProduto);
    }
    
    private void inserirProdutoBD() {
        p.setNome(fieldNome.getText());
        p.setPreco(Double.parseDouble(fieldPreco.getText()));
        p.setStock(Integer.parseInt(fieldStock.getText()));
        p.setStockmin(Integer.parseInt(fieldStockMin.getText()));
        p.setDescricao(fieldDescricao.getText());
        p.setCodTipo(boxTipoProduto.getSelectionModel().getSelectedItem().getTipoProduto());
        p.updateT();
    } 

   
    
    private class Choice {
        private final TipoProduto tp;
        
        public Choice(TipoProduto tp) {
            this.tp = tp;
        }
        
        @Override
        public String toString() {
            return tp.getNome();
        }
        
        public TipoProduto getTipoProduto() {
            return tp;
        }
    }
    
}
