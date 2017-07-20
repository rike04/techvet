
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Cliente;
import model.Paciente;
import techvet.DocFXML;
import techvet.Utils;

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
    @FXML
    private CheckBox boxVivo;
    
    private final Pane content;
    private Cliente cliente;
    private Paciente paciente;
    
    public FormularioPacienteController(Pane content) {
        this.content = content;
        cliente = null;
    }

    public FormularioPacienteController(Pane content, Paciente paciente) {
        this.content = content;
        this.cliente = null;
        this.paciente = paciente;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        popularChoiceBox();
       
        boxVivo.setSelected(true);
        
        if(paciente != null) preencheCampos();
    }    
    
    private void preencheCampos() {
        fieldPeso.setText(paciente.getPeso().toString());
        fieldIdade.setText(String.valueOf(paciente.getIdade()));
        fieldNome.setText(paciente.getNome());
        fieldCliente.setText(paciente.getIdCliente().getNome());
        fieldEspecie.setText(paciente.getEspecie());
        boxSexo.getSelectionModel().select(paciente.getSexo());
        
        boxVivo.setSelected( paciente.getEstado() == (short) 1 );  
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
        Stage stage = Utils.preparaNovaJanela(owner);
        stage.setScene(scene);
        stage.showAndWait();
        return controller;
    }
    
    @FXML
    public void cliqueConfirmar(ActionEvent event) {
        if (osDadosSaoValidos()) {
            try {
                if (cliente == null) cliente = buscaCliente();
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
        ListaPacientesController controller = new ListaPacientesController(false, content);
        Utils.mudaContentPara(DocFXML.LISTAPACIENTES, controller, content);
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
        boolean isNovoPaciente = paciente == null;
        if (isNovoPaciente) paciente = new Paciente();
        
        paciente.setNome(fieldNome.getText());
        paciente.setEspecie(fieldEspecie.getText());
        paciente.setIdade(Integer.parseInt(fieldIdade.getText()));
        paciente.setPeso(Double.parseDouble(fieldPeso.getText()));
        paciente.setIdCliente(c);
        paciente.setRaca("Raca");
        paciente.setCor("Cor");
        paciente.setSexo(boxSexo.getSelectionModel().getSelectedItem().toString());     
        if (boxVivo.isSelected()) paciente.setEstado((short) 1);
        else paciente.setEstado((short) 0);
        
        if (isNovoPaciente) paciente.createT();
        else paciente.updateT();
    } 
    
    private Cliente buscaCliente() throws Exception {
        List<Cliente> clientes = Cliente.readByNome(fieldCliente.getText());
        return clientes.get(0);
    }
    
}
