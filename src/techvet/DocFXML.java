/*
 * 
 */

package techvet;

/**
 * @author Henrique Faria e Sérgio Araújo 
 */

public enum DocFXML {
    
    LOGIN("/techvetA/views/Login.fxml"),
    TEMPLATE("/techvet/views/MainMenu.fxml"),
    BOTOESCONSULTA("/techvet/views/BotoesConsulta.fxml"),
    BOTOESCLIENTE("/techvet/views/BotoesCliente.fxml"),
    BOTOESPACIENTE("/techvet/views/BotoesPaciente.fxml"),
    BOTOESADMINISTRADOR("/techvet/views/BotoesAdministrador.fxml"),
    FORMULARIOPACIENTE("/techvet/views/FormularioPaciente.fxml"),
    FORMULARIOCLIENTE("/techvet/views/FormularioCliente.fxml"),
    FORMULARIOCONSULTA("/techvet/views/FormularioConsulta.fxml"),
    LISTACLIENTES("/techvet/views/ListaClientes.fxml"),
    LISTAPACIENTES("/techvet/views/ListaPacientes.fxml"),
    LISTACONSULTAS("/techvet/views/ListaConsultas.fxml"),
    LISTAUTILIZADORES("/techvet/views/ListaUtilizadores.fxml");

    private final String caminhoParaDoc;
    
    DocFXML(String caminhoParaDoc) {
        this.caminhoParaDoc = caminhoParaDoc;
    }
    
    public String getPath() {
        return caminhoParaDoc;
    }
    
}
