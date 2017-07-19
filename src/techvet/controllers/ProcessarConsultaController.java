/*
 * 
 */

package techvet.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.ArtigoConsulta;
import model.Consulta;
import model.Produto;
import techvet.DocFXML;
import techvet.GUIUtils;
import techvet.Utils;

/**
 * @author Henrique Faria e Sérgio Araújo
 */
public class ProcessarConsultaController implements Initializable {

    @FXML
    private TextField fieldNomePaciente;
    @FXML 
    private TableView<ArtigoConsulta> tabelaProdutosConsulta;
    @FXML
    private TableColumn<ArtigoConsulta, String> colNomeProduto;
    @FXML
    private TableColumn<ArtigoConsulta, String> colQuantidade;
    @FXML
    private TableColumn<ArtigoConsulta, Integer> colStock;
    @FXML
    private TableColumn<ArtigoConsulta, Double> colPreco;
    @FXML
    private Button botaoRemoveProd;
    @FXML
    private Label labelTipoConsulta;
    @FXML
    private TextArea fieldDescricao;
    
    private final ObservableList<ArtigoConsulta> listaArtigosConsulta;
    
    private final Consulta consulta;
    private final PseudoClass classErro;
    private final Pane content;
    
    public ProcessarConsultaController(Consulta c, Pane content) {
        this.consulta = c;
        this.listaArtigosConsulta = FXCollections.observableArrayList();
        classErro = PseudoClass.getPseudoClass("error");
        this.content = content;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        preencherLabels();
    
        tabelaProdutosConsulta.setPlaceholder(new Label("Nao foram adicionados produtos."));
        tabelaProdutosConsulta.setRowFactory((TableView<ArtigoConsulta> param) -> new TableRow<ArtigoConsulta>() {
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
       
        //Desativa o botao de remover produtos caso nao esteja selecionada nenhuma linha
        BooleanBinding desativaBotao = tabelaProdutosConsulta.getSelectionModel().selectedItemProperty().isNull();
        botaoRemoveProd.disableProperty().bind(desativaBotao);
        
        //Permite editar a coluna de quantidade para inserir o valor pretendido
        colQuantidade.setCellFactory(TextFieldTableCell.forTableColumn());
        colQuantidade.setOnEditCommit((CellEditEvent<ArtigoConsulta, String> t) -> {
            atribuiQuantidadeAColuna(t);
        });
        
        colNomeProduto.setCellValueFactory(dadosCell -> 
                new SimpleStringProperty(dadosCell.getValue().getIdProduto().getNome()));
        colQuantidade.setCellValueFactory(dadosCell -> 
                new SimpleStringProperty(Integer.toString(dadosCell.getValue().getQuantidade())));
        colPreco.setCellValueFactory(dadosCell -> 
                new SimpleDoubleProperty(dadosCell.getValue().getIdProduto().getPreco() * dadosCell.getValue().getQuantidade()).asObject());    
        colStock.setCellValueFactory(dadosCell -> 
                new SimpleIntegerProperty(dadosCell.getValue().getIdProduto().getStock()).asObject());
        
        preencherLista();
        
        tabelaProdutosConsulta.setItems(listaArtigosConsulta);
        
        GUIUtils.autoFitTable(tabelaProdutosConsulta);
    }   
    
    private void preencherLista() {
        listaArtigosConsulta.setAll(consulta.getListaArtigosConsulta());
    }
    
    private void preencherLabels() {
        labelTipoConsulta.setText(consulta.getTipoConsulta().getNome());
        fieldNomePaciente.setText(consulta.getPaciente().getNome());
        
        if (!consulta.getDesctratamento().trim().isEmpty()) {
            fieldDescricao.setText(consulta.getDesctratamento());
        }
    }
    
    private void atribuiQuantidadeAColuna(CellEditEvent<ArtigoConsulta, String> t) {
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
        for(ArtigoConsulta ac: listaArtigosConsulta) {
            if (ac.getIdProduto().equals(p)) {
                return false;
            }
        }
        return true;
    }
    
    private void adicionaProduto(Produto p) {
        if (verificaProdutoUnico(p)) {
            ArtigoConsulta ac = new ArtigoConsulta();
            ac.setIdProduto(p);
            listaArtigosConsulta.add(ac);
            tabelaProdutosConsulta.refresh();
        }
    }
    
    @FXML
    public void cliqueConfirmar(ActionEvent event) {
        if(osDadosSaoValidos()) {
            atualizaConsultaBD();
            mudarContent();
        }

    } 
    
    private boolean osDadosSaoValidos() {
        boolean saoValidos = true;
        
        if (fieldDescricao.getText().trim().isEmpty()) {
            saoValidos = false;
        }
        
        return saoValidos;
    }
    
    private void atualizaConsultaBD() {
        consulta.setDesctratamento(fieldDescricao.getText());
        consulta.setEstado((short) 1);
        consulta.setPago((short) 0);
        if (listaArtigosConsulta.size() > 0) {
            consulta.setListaArtigosConsulta(listaArtigosConsulta);
            for (ArtigoConsulta ac: listaArtigosConsulta) {
                 ac.setConsulta(consulta);
            }
        } 
        consulta.updateT();
    }
    
    @FXML
    public void cliqueCancelar(ActionEvent event) {

    }
    
    private void mudarContent() {
        Initializable controller = new HorarioConsultasController(content);
        try {
            Utils.mudaContentPara(DocFXML.HORARIO, controller, content);
        } catch (IOException e) {
        }
    }

    @FXML
    public void cliqueRemoveProd(ActionEvent event) {
        ArtigoConsulta ac = tabelaProdutosConsulta.getSelectionModel().getSelectedItem();
        listaArtigosConsulta.remove(ac);
        tabelaProdutosConsulta.refresh();
    }
    
    @FXML
    public void cliquePrescreverReceita(ActionEvent event) {
        boolean notReadOnly = false;
        Initializable controller = new FormularioReceitaController(consulta, notReadOnly);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(DocFXML.FORMULARIORECEITA.getPath()));
        loader.setController(controller);
        Stage owner = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Stage stage = Utils.preparaNovaJanela(owner);
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void cliqueInternar(ActionEvent event) {
        if (osDadosSaoValidos()) {
            atualizaConsultaBD();
            iniciarInternamento();
        }
    }
    
    @FXML
    private void iniciarInternamento() {
        Initializable controller = new FormularioInternamentoController(content, consulta);
        try {
            Utils.mudaContentPara(DocFXML.FORMULARIOINTERNAMENTO, controller, content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
