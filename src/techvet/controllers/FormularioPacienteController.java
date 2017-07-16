/*
 * 
 */

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
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Cliente;
import model.Paciente;
import techvet.DocFXML;
import techvet.Util;

/**
 * @author Henrique Faria e Sergio Araujo
 */ 

public class FormularioPacienteController implements Initializable {
    
    @FXML 
    private TextField fieldNome;
    @FXML
    private TextField fieldEspecie;
    @FXML
    private TextField fieldIdade;
    @FXML
    private ChoiceBox boxSexo;
    @FXML 
    private TextField fieldCliente;
    @FXML
    private TextField fieldPeso;
    
    private final Pane content;
    private Cliente cliente;
    
    public FormularioPacienteController(Pane content) {
        this.content = content;
        cliente = null;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        popularChoiceBox();
    }    
    
    @FXML
    public void cliqueProcurarCliente(ActionEvent event) {
        ListaClientesController controller;
        try {
            controller = abrirListaClientes(event);
        } catch (IOException ex) {
            return ;
        }
        if (controller.foiSelecionadaOpcao()) {
            cliente = controller.getClienteSelecionado();
            fieldCliente.setText(cliente.getNome());
        }
    }
    
    private ListaClientesController abrirListaClientes(Event event) throws IOException {
        ListaClientesController controller = new ListaClientesController(true);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(DocFXML.LISTACLIENTES.getPath()));
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
        if (osDadosSaoValidos()) {
            try {
                if (cliente == null) {
                    cliente = buscaCliente();
                }
                inserirPacienteBD(cliente);
                mudarContent();
            } catch (Exception e) {
                Logger.getLogger(FormularioPacienteController.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
    
    @FXML 
    public void cliqueCancelar(ActionEvent event) {
        try {
            mudarContent();
        } catch (IOException e) {
            Logger.getLogger(FormularioPacienteController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    private void mudarContent() throws IOException{
        ListaConsultasController controller = new ListaConsultasController(false);
        Util.mudaContentPara(DocFXML.LISTAPACIENTES, controller, content);
    }
    
    private Boolean osDadosSaoValidos() {
        return true;
    }
    
    private void popularChoiceBox() {
        ObservableList<String> opcoes = FXCollections.observableArrayList();
        opcoes.add("Masculino");
        opcoes.add("Feminino");
        
        boxSexo.getItems().setAll(opcoes);
        boxSexo.getSelectionModel().select(0);
    }
    
    private void inserirPacienteBD(Cliente c) {
        Paciente p = new Paciente();
        p.setNome(fieldNome.getText());
        p.setEspecie(fieldEspecie.getText());
        p.setIdade(Integer.parseInt(fieldIdade.getText()));
        p.setPeso(Double.parseDouble(fieldPeso.getText()));
        p.setIdCliente(c);
        p.setRaca("Raca");
        p.setCor("Cor");
        p.setSexo(boxSexo.getSelectionModel().getSelectedItem().toString());
        p.setEstado((short) 1);
        p.createT();
    } 
    
    //ALTERAR: forma como o cliente e encontrado. Clientes com o mesmo nome darao problemas
    private Cliente buscaCliente() throws Exception {
        List<Cliente> clientes = Cliente.readByNome(fieldCliente.getText());
        return clientes.get(0);
    }
    
}
