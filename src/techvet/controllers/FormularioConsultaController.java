/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package techvet.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Cliente;
import model.Consulta;
import model.Paciente;
import model.TipoConsulta;

/**
 * FXML Controller class
 *
 * @author rike4
 */

public class FormularioConsultaController implements Initializable {

    @FXML
    private TextField nomePaciente;
    @FXML
    private TextField nomeCliente;
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
        nomePaciente.focusedProperty().addListener(
                (ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue == false && !nomePaciente.getText().trim().isEmpty()) {
                Task<Void> task = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        String nome = nomePaciente.getText();
                        List<Paciente> pacientes = Paciente.retrievePacientesbyNome(nome);
                        if (pacientes.size() == 1) {
                            nomeCliente.setText(pacientes.get(0).getIdCliente().getNome());
                        }
                        return null;
                    }
                };
                Thread t = new Thread(task);
                t.setDaemon(true);
                t.start();
            }
        });
        
        nomeCliente.focusedProperty().addListener(
                (ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    if (newValue == false && !nomeCliente.getText().trim().isEmpty()) {
                        Task<Void> task = new Task<Void>() {
                            @Override
                            protected Void call() throws Exception {
                                String nome = nomeCliente.getText();
                                List<Cliente> clientes = Cliente.readByNome(nome);
                                
                                if (clientes.size() == 1) {
                                    Cliente cli = clientes.get(0);
                                    if (cli.getPacienteCollection().size() == 1) {
                                        Paciente p = cli.getPacienteCollection().get(0);
                                        nomePaciente.setText(p.getNome());
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
            criarConsulta();
        } else {
            
        }
    }
    
    @FXML
    public void cliqueCancelar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
    }
    
    private boolean osCamposPreenchidos() {
        boolean eValido = true;
        
        if(nomePaciente.getText().trim().isEmpty()) {
           eValido = false;
        }
        
        if (nomeCliente.getText().trim().isEmpty()) {
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
    
    private void criarConsulta() {
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
