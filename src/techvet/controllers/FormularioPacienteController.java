/*
 * 
 */

package techvet.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
 * FXML Controller class
 *
 * @author rike4
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
    
    private final Pane content;
    
    public FormularioPacienteController(Pane content) {
        this.content = content;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        popularChoiceBox();
    }    
    
    @FXML
    public void cliqueProcurarCliente(ActionEvent event) {
        ListaClientesController controller;
        try {
            controller = abrirListaClientes();
        } catch (IOException ex) {
            return ;
        }
        if (controller.foiSelecionadaOpcao()) {
            fieldCliente.setText(controller.getClienteSelecionado().getNome());
        }
    }
    
    private ListaClientesController abrirListaClientes() throws IOException {
        Stage stage = new Stage();
        ListaClientesController controller = new ListaClientesController(true);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(DocFXML.LISTACLIENTES.getPath()));
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();
        return controller;
    }
    
    @FXML
    public void cliqueConfirmar(ActionEvent event) {
        if (osDadosSaoValidos()) {
            Cliente c;
            try {
                c = buscaCliente();
                inserirPacienteBD(c);
                mudarContent();
            } catch (Exception ex) {
                Logger.getLogger(FormularioPacienteController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @FXML 
    public void cliqueCancelar(ActionEvent event) {
        try {
            mudarContent();
        } catch (IOException e) {
            
        }
    }
    
    private void mudarContent() throws IOException{
        ListaConsultasController controller = new ListaConsultasController();
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
        p.setDatanasc(new Date());
        p.setIdCliente(c);
        p.setRaca("Raca");
        p.setCor("Cor");
        p.setSexo(boxSexo.getSelectionModel().getSelectedItem().toString());
        p.setEstado((short) 1);
        p.createT();
    } 
    
    //ALTERAR: forma como o cliente e encontrado. Clientes com o mesmo nome darao problemas
    private Cliente buscaCliente() throws Exception {
        List<Cliente> clientes = Cliente.readByNome( fieldCliente.getText() );
        return clientes.get(0);
    }
    
}
