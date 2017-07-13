/*
 * 
 */

package techvet.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Cliente;
import model.Consulta;
import model.Paciente;
import model.TipoConsulta;
import techvet.DocFXML;
import techvet.Util;

/**
 * @author rike4
 */

public class FormularioConsultaController implements Initializable {

    @FXML
    private TextField fieldNomePaciente;
    @FXML
    private TextField fieldNomeCliente;
    @FXML
    private TextField dataConsulta;
    @FXML 
    private TextArea descricao;
    @FXML 
    private ChoiceBox<Choice> boxTipoConsulta;
    @FXML
    private ChoiceBox boxLocal;
    @FXML
    private TextField localConsulta;
    
    private Cliente cliente;
    private Paciente paciente;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<Choice> escolhasTipoConsulta = FXCollections.observableArrayList();
        List<TipoConsulta> tiposConsulta = TipoConsulta.retrieveAll();
        tiposConsulta.forEach((TipoConsulta tipo) -> {
            escolhasTipoConsulta.add(new Choice(tipo.getId(), tipo.getNome()));
        });
        boxTipoConsulta.getItems().addAll(escolhasTipoConsulta);
        boxTipoConsulta.getSelectionModel().select(0);
        
        ObservableList<String> escolhasLocal = FXCollections.observableArrayList();
        escolhasLocal.add("Local");
        escolhasLocal.add("Exterior");
        boxLocal.getItems().addAll(escolhasLocal);
        
        //O textfield do local da consulta só é activado caso seja selecionada 
        //a opção "Exterior" 
        boxLocal.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (newValue.equals(0)) {
                localConsulta.setDisable(true);
                localConsulta.clear();
            } else {
                localConsulta.setDisable(false);
            }
        });
        
        boxLocal.getSelectionModel().select(0);
        
        //Listeners para os textfields
        //https://stackoverflow.com/questions/30249493/using-threads-to-make-database-requests
        fieldNomePaciente.focusedProperty().addListener(
                (ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue == false && !fieldNomePaciente.getText().trim().isEmpty()) {
                Task<Void> task = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        String nome = fieldNomePaciente.getText();
                        List<Paciente> pacientes = Paciente.retrievePacientesbyNome(nome);
                        if (pacientes.size() == 1) {
                            paciente = pacientes.get(0);
                            cliente = paciente.getIdCliente();
                            fieldNomeCliente.setText(cliente.getNome());
                        } 
                        return null;
                    }
                };
                Thread t = new Thread(task);
                t.setDaemon(true);
                t.start();
            }
        });
        
        fieldNomeCliente.focusedProperty().addListener(
                (ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    if (newValue == false && !fieldNomeCliente.getText().trim().isEmpty()) {
                        Task<Void> task = new Task<Void>() {
                            @Override
                            protected Void call() throws Exception {
                                String nome = fieldNomeCliente.getText();
                                List<Cliente> clientes = Cliente.readByNome(nome);
                                
                                if (clientes.size() == 1) {
                                    cliente = clientes.get(0);
                                    if (cliente.getPacienteCollection().size() == 1) {
                                        paciente = cliente.getPacienteCollection().get(0);
                                        fieldNomePaciente.setText(paciente.getNome());
                                    }
                                }
                                return null;
                            }
                        };
                        Thread t = new Thread(task);
                        t.setDaemon(true);
                        t.start();
                }
        });
    }    
    
    @FXML
    public void cliqueConfirmar(ActionEvent event) {
        if (osCamposPreenchidos()) {
            inserirConsultaBD();
        } else {
            
        }
    }
    
    @FXML
    public void cliqueCancelar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
    }
    
    @FXML
    private void cliqueProcurarPaciente(ActionEvent event) {
        ListaPacientesController controller;
        try {
            controller = abrirListaPacientes(event);
        } catch (IOException ex) {
            return ;
        }
        if (controller.foiSelecionadaOpcao()) {
            paciente = controller.getPacienteSelecionado();
            fieldNomePaciente.setText(paciente.getNome());
        }
    }
    
    private ListaPacientesController abrirListaPacientes(Event event) throws IOException {
        ListaPacientesController controller = new ListaPacientesController(true);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(DocFXML.LISTAPACIENTES.getPath()));
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
        Stage stage = Util.preparaNovaJanela(owner);
        stage.setScene(scene);
        stage.showAndWait();
        return controller;
    }
    
    private boolean osCamposPreenchidos() {
        boolean eValido = true;
        
        if(fieldNomePaciente.getText().trim().isEmpty()) {
           eValido = false;
        }
        
        if (fieldNomeCliente.getText().trim().isEmpty()) {
           eValido = false;
        }
        
        if(dataConsulta.getText().trim().isEmpty()) {
            eValido = false;
        }
        
        if (boxTipoConsulta.getValue() == null) {
            eValido = false;
        }
        
        if (!boxLocal.getValue().equals("Local")) {
            eValido = false;   
        }
        
        return eValido;
    }
    
    private boolean osDadosSaoValidos(){
        boolean saoValidos = true;
        
        
        
        return saoValidos;
    }
    
    private void inserirConsultaBD() {
        Consulta c = new Consulta();     
        
    }
    
    /*
        Exemplo retirado de: https://gist.github.com/jewelsea/1422104
        Serve para popular a ChoiceBox com os valores do TipoConsulta de forma a 
        que seja possivel arranjar o ID correspondente a opcao escolhida
    */
    private class Choice {
        private final int id;
        private final String displayString;
        
        public Choice(int id, String displayString) {
           this.id = id;
           this.displayString = displayString;
        }
       
        public int getIdTipoConsulta() {
            return id;
        }
        
        @Override 
        public String toString() {
            return displayString;
        }    
    }
    
}
