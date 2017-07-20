
package techvet.controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Cliente;
import model.LinhaArtigo;
import model.Produto;
import model.Venda;
import techvet.DocFXML;
import techvet.GUIUtils;
import techvet.Utils;

/**
 * @author Henrique Faria e Sergio Araujo
 */

public class FormularioVendaController implements Initializable {

    @FXML 
    private TableView<LinhaArtigo> tabelaProdutos;
    @FXML
    private TableColumn<LinhaArtigo, String> colNomeProduto;
    @FXML
    private TableColumn<LinhaArtigo, String> colQuantidade;
    @FXML
    private TableColumn<LinhaArtigo, Integer> colStock;
    @FXML
    private TableColumn<LinhaArtigo, Double> colPreco;
    @FXML
    private TextField fieldNomeCliente;
    @FXML
    private TextField fieldTotal;
    @FXML
    private DatePicker fieldData;
    @FXML
    private Button botaoRemoveProd;
    @FXML
    private Button botaoAdicionar;
    @FXML
    private Button botaoProcurar;
    @FXML
    private Button botaoCancelar;
    @FXML
    private Button botaoFechar;
    @FXML
    private Button botaoConfirmar;
    
    private Cliente cliente; 
    private Venda venda;
    
    private final Pane content;
    private final ObservableList<LinhaArtigo> listaLinhasArtigo;
    
    public FormularioVendaController(Pane content) {
        this.content = content;
        this.listaLinhasArtigo =  FXCollections.observableArrayList();
        this.venda = null;
    }
    
    public FormularioVendaController(Pane content, Venda venda) {
        this.content = content;
        this.listaLinhasArtigo =  FXCollections.observableArrayList();
        this.venda = venda;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LocalDate ld = LocalDate.now();
        fieldData.setValue(ld);
        
        tabelaProdutos.setPlaceholder(new Label("Nao foram adicionados produtos."));
        
        colNomeProduto.setCellValueFactory(dadosCell -> 
                new SimpleStringProperty(dadosCell.getValue().getProduto().getNome()));
        colQuantidade.setCellValueFactory(dadosCell -> 
                new SimpleStringProperty(Integer.toString(dadosCell.getValue().getQuantidade())));
        colPreco.setCellValueFactory(dadosCell -> 
                new SimpleDoubleProperty(dadosCell.getValue().getProduto().getPreco() * dadosCell.getValue().getQuantidade()).asObject());    
        colStock.setCellValueFactory(dadosCell -> 
                new SimpleIntegerProperty(dadosCell.getValue().getProduto().getStock()).asObject());
        
        botaoFechar.setOnAction( v -> {
            mudarContent();
        });
        
        
        //Permite editar a coluna de quantidade para inserir o valor pretendido
        colQuantidade.setCellFactory(TextFieldTableCell.forTableColumn());
        colQuantidade.setOnEditCommit((CellEditEvent<LinhaArtigo, String> t) -> {
            atribuiQuantidadeAColuna(t);
            atualizaTotal();
        });
        
        if (venda != null) preencheCampos();
        else {
            //Desativa o botao de remover produtos caso nao esteja selecionada nenhuma linha
            BooleanBinding desativaBotao = tabelaProdutos.getSelectionModel().selectedItemProperty().isNull();
            botaoRemoveProd.disableProperty().bind(desativaBotao);
        }
        
        tabelaProdutos.setItems(listaLinhasArtigo);
        
        atualizaTotal();
        
        GUIUtils.autoFitTable(tabelaProdutos);
    }    
    
    private void atribuiQuantidadeAColuna(CellEditEvent<LinhaArtigo, String> t) {
        int quantidade;
        try {
            quantidade = Integer.parseInt(t.getNewValue());
        } catch (NumberFormatException e) {
            return ;
        }
        if (quantidade == 0) {
            return ;
        }
        if(quantidade > 0 && quantidade <= t.getRowValue().getProduto().getStock()) {
            t.getRowValue().setQuantidade(quantidade);
        }
        t.getTableView().refresh();
    }
    
    private void atualizaTotal() {
        double total = 0;
        for(LinhaArtigo la : listaLinhasArtigo) {
            total += la.getQuantidade() * la.getValor();
        }
        fieldTotal.setText(String.valueOf(total));
    }
    
    private void preencheCampos() {
        fieldTotal.setText(String.valueOf(venda.getTotal()));
        fieldNomeCliente.setText(venda.getIdCliente().getNome());
        cliente = venda.getIdCliente();
        listaLinhasArtigo.setAll(venda.getLinhaArtigoList());
        fieldData.setValue(Utils.toLocalDate(venda.getData()));
        desativaCampos();
    }
    
    private void desativaCampos() {
        fieldTotal.setDisable(true);
        fieldNomeCliente.setDisable(true);
        fieldData.setDisable(true);
        tabelaProdutos.setEditable(false);
        botaoProcurar.setDisable(true);
        botaoRemoveProd.setDisable(true);
        botaoAdicionar.setDisable(true);
        botaoConfirmar.setDisable(true);
        botaoConfirmar.setVisible(false);
        botaoCancelar.setDisable(true);
        botaoCancelar.setVisible(false);
        botaoFechar.setVisible(true);
        botaoFechar.setDisable(false);
    }
    
    @FXML
    public void cliqueConfirmar(ActionEvent event) {
        if (osCamposPreenchidos() && venda == null) {
            inserirVendaBD();
        } 
        mudarContent();
    }
    
    private void mudarContent() {
        Initializable controller = new ListaVendasController(false, content);
        try {
            Utils.mudaContentPara(DocFXML.LISTAVENDAS, controller, content);
        } catch (IOException e) {
        }
    }
    
    @FXML
    public void cliqueCancelar(ActionEvent event) {
        mudarContent();
    }
    
    @FXML
    private void cliqueProcurarCliente(ActionEvent event) {
        ListaClientesController controller;
        try {
            controller = abrirListaClientes(event);
        } catch (IOException ex) {
            return ;
        }
        if (controller.foiSelecionadaOpcao()) {
            cliente = controller.getClienteSelecionado();
            fieldNomeCliente.setText(cliente.getNome());
        }
    }
    
    private ListaClientesController abrirListaClientes(Event event) throws IOException {
        ListaClientesController controller = new ListaClientesController(true);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(DocFXML.LISTACLIENTES.getPath()));
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        
        Stage owner = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Stage stage = Utils.preparaNovaJanela(owner);
        stage.setScene(scene);
        stage.showAndWait();
        return controller;
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
            adicionaProduto(controller.getProdutoSelecionado());
        }
    }
    
    private ListaArtigosController abrirListaProdutos(Event event) throws IOException {
        ListaArtigosController controller = new ListaArtigosController(true);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(DocFXML.LISTAARTIGOS.getPath()));
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        
        Stage owner = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Stage stage = Utils.preparaNovaJanela(owner);
        stage.setScene(scene);
        stage.showAndWait();
        return controller;
    }
    
    private boolean verificaProdutoUnico(Produto p) {
        for(LinhaArtigo ac: listaLinhasArtigo) {
            if (ac.getProduto().equals(p)) {
                return false;
            }
        }
        return true;
    }
    
    private void adicionaProduto(Produto p) {
        if (verificaProdutoUnico(p)) {
            LinhaArtigo la = new LinhaArtigo();
            la.setProduto(p);
            la.setValor(p.getPreco());
            listaLinhasArtigo.add(la);
            tabelaProdutos.refresh();
        }
    }
    
    @FXML
    public void cliqueRemoveProd(ActionEvent event) {
        LinhaArtigo ac = tabelaProdutos.getSelectionModel().getSelectedItem();
        listaLinhasArtigo.remove(ac);
        atualizaTotal();
        tabelaProdutos.refresh();
    }
    
    private boolean osCamposPreenchidos() {
        boolean eValido = true;
        
        if (fieldData.getValue() == null) {
            eValido = false;
        }
        
        if (fieldNomeCliente.getText().trim().isEmpty()) {
            eValido = false;
        } 
        if (listaLinhasArtigo.size() > 0) {
            for(LinhaArtigo l: listaLinhasArtigo) {
                if (l.getQuantidade() <= 0) {
                    eValido = false;
                }
            }   
        } else {
            eValido = false;
        } 
        if (fieldTotal.getText().trim().isEmpty()) {
            eValido = false;
        }
        return eValido;
    }

    private void inserirVendaBD() {
        boolean isNovaVenda = venda == null;
        if (isNovaVenda) venda = new Venda();
        venda.setIdCliente(cliente);
        venda.setLinhaArtigoList(listaLinhasArtigo);
        listaLinhasArtigo.forEach((l) -> {
            l.setIdVenda(venda);
        });
        venda.setTotal(Double.valueOf(fieldTotal.getText()));
        venda.setData(Utils.toDate(fieldData.getValue()));
        if (isNovaVenda) venda.createT();
        else venda.updateT();
    }
    
}
