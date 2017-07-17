
package techvet.controllers;

import java.io.IOException;
import java.net.URL;
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
import techvet.Util;

/**
 * @author Henrique Faria e Sergio Araujo
 */ 

public class FormularioArtigoController implements Initializable {
    
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
    
    private final Pane content;
    
    public FormularioArtigoController(Pane content) {
        this.content = content;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        popularChoiceBox();
        
        fieldNome.addEventFilter(KeyEvent.KEY_TYPED, Util.validacaoLimiteMax(100));
//        fieldDescricao.addEventFilter(KeyEvent.KEY_TYPED, Util.validacaoLimiteMax(200));
        fieldPreco.addEventFilter(KeyEvent.KEY_TYPED, Util.validacaoPrecos(12));
        fieldStock.addEventFilter(KeyEvent.KEY_TYPED, Util.validacaoNumerica(5));
        fieldStockMin.addEventFilter(KeyEvent.KEY_TYPED, Util.validacaoNumerica(5));
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
        ListaArtigosController controller = new ListaArtigosController(false);
        Util.mudaContentPara(DocFXML.LISTAARTIGOS, controller, content);
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
        tiposProduto.forEach( (TipoProduto tipo) -> {
            escolhasTipoProduto.add(new Choice(tipo));
        });
        boxTipoProduto.getItems().addAll(escolhasTipoProduto);
        boxTipoProduto.getSelectionModel().select(0);
    }
    
    private void inserirProdutoBD() {
        Produto p = new Produto();
        p.setNome(fieldNome.getText());
        p.setPreco(Double.parseDouble(fieldPreco.getText()));
        p.setStock(Integer.parseInt(fieldStock.getText()));
        p.setStockmin(Integer.parseInt(fieldStockMin.getText()));
        p.setDescricao(fieldDescricao.getText());
        p.setCodTipo(boxTipoProduto.getSelectionModel().getSelectedItem().getTipoProduto());
        p.createT();
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
