/*
 * 
 */

package techvet.controllers;

import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.util.Date;
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
import model.ArtigoConsulta;
import model.Cliente;
import model.Consulta;
import static model.Consulta_.paciente;
import model.LinhaArtigo;
import model.Produto;
import static model.Produto_.descricao;
import model.TipoConsulta;
import model.Venda;
import techvet.DocFXML;
import techvet.GUIUtils;
import techvet.Utils;

/**
 * @author rike4
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
        
        tabelaProdutos.setPlaceholder(new Label("Nao foram adicionados produtos."));
        
        colNomeProduto.setCellValueFactory(dadosCell -> 
                new SimpleStringProperty(dadosCell.getValue().getProduto().getNome()));
        colQuantidade.setCellValueFactory(dadosCell -> 
                new SimpleStringProperty(Integer.toString(dadosCell.getValue().getQuantidade())));
        colPreco.setCellValueFactory(dadosCell -> 
                new SimpleDoubleProperty(dadosCell.getValue().getProduto().getPreco() * dadosCell.getValue().getQuantidade()).asObject());    
        colStock.setCellValueFactory(dadosCell -> 
                new SimpleIntegerProperty(dadosCell.getValue().getProduto().getStock()).asObject());
        
                //Desativa o botao de remover produtos caso nao esteja selecionada nenhuma linha
        BooleanBinding desativaBotao = tabelaProdutos.getSelectionModel().selectedItemProperty().isNull();
        botaoRemoveProd.disableProperty().bind(desativaBotao);
        
        //Permite editar a coluna de quantidade para inserir o valor pretendido
        colQuantidade.setCellFactory(TextFieldTableCell.forTableColumn());
        colQuantidade.setOnEditCommit((CellEditEvent<LinhaArtigo, String> t) -> {
            atribuiQuantidadeAColuna(t);
        });
        
        if (venda != null) listaLinhasArtigo.setAll(venda.getLinhaArtigoList());
        
        tabelaProdutos.setItems(listaLinhasArtigo);
        
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
            
        if(quantidade > 0 && quantidade < t.getRowValue().getProduto().getStock()) {
            t.getRowValue().setQuantidade(quantidade);
        }
        t.getTableView().refresh();
    }
    
    @FXML
    public void cliqueConfirmar(ActionEvent event) {
        if (osCamposPreenchidos()) {
            inserirVendaBD();
        } else {
            
        }
    }
    
    @FXML
    public void cliqueCancelar(ActionEvent event) {
        Initializable controller = new ListaVendasController(false);
        try {
            Utils.mudaContentPara(DocFXML.LISTAVENDAS, controller, content);
        } catch (Exception e) {
        }
        
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
            LinhaArtigo ac = new LinhaArtigo();
            ac.setProduto(p);
            listaLinhasArtigo.add(ac);
            tabelaProdutos.refresh();
        }
    }
    
    @FXML
    public void cliqueRemoveProd(ActionEvent event) {
        LinhaArtigo ac = tabelaProdutos.getSelectionModel().getSelectedItem();
        listaLinhasArtigo.remove(ac);
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
            
        } else {
            eValido = false;
        }
        
        return eValido;
    }
    
    
    private void inserirVendaBD() {
        
        boolean isNovaVenda = venda == null;
        if (isNovaVenda) venda = new Venda();
        
        venda.setIdCliente(cliente);
        venda.setLinhaArtigoList(listaLinhasArtigo);
        venda.setTotal(Double.valueOf(fieldTotal.getText()));
        Date data = Date.from(fieldData.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        venda.setData(data);
        
        if (isNovaVenda) venda.createT();
        else venda.updateT();
    }
    
}
