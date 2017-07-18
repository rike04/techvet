/*
 * 
 */

package techvet;

/**
 * @author Henrique Faria e Sérgio Araújo 
 */

public enum DocFXML {
    
    LOGIN("/techvet/views/Login.fxml"),
    TEMPLATE("/techvet/views/MainMenu.fxml"),
    
    //Botoes da sidebar 
    BOTOESCONSULTA("/techvet/views/BotoesConsulta.fxml"),
    BOTOESCLIENTE("/techvet/views/BotoesCliente.fxml"),
    BOTOESPACIENTE("/techvet/views/BotoesPaciente.fxml"),
    BOTOESADMINISTRADOR("/techvet/views/BotoesAdministrador.fxml"),
    BOTOESINTERNAMENTOS("/techvet/views/BotoesInternamentos.fxml"),
    BOTOESINVENTARIO("/techvet/views/BotoesInventario.fxml"),
    BOTOESVENDA("/techvet/views/BotoesVenda.fxml"),
    
    //Formularios para introduzir um novo objecto na BD 
    FORMULARIOPACIENTE("/techvet/views/FormularioPaciente.fxml"),
    FORMULARIOCLIENTE("/techvet/views/FormularioCliente.fxml"),
    FORMULARIOCONSULTA("/techvet/views/FormularioConsulta.fxml"),
    FORMULARIOUTILIZADOR("/techvet/views/FormularioUtilizador.fxml"),
    FORMULARIOVENDA("/techvet/views/FormularioVenda.fxml"),
    FORMULARIOARTIGO("/techvet/views/FormularioArtigo.fxml"),
    FORMULARIOINTERNAMENTO("/techvet/views/FormularioInternamento.fxml"),
    FORMULARIOTIPOCONSULTA("/techvet/views/FormularioTipoConsulta.fxml"),
    FORMULARIOTIPOPRODUTO("/techvet/views/FormularioTipoProduto.fxml"),

    
    //Listagem dos objectos armazenados na BD
    LISTACLIENTES("/techvet/views/ListaClientes.fxml"),
    LISTAPACIENTES("/techvet/views/ListaPacientes.fxml"),
    LISTACONSULTAS("/techvet/views/ListaConsultas.fxml"),
    LISTAUTILIZADORES("/techvet/views/ListaUtilizadores.fxml"),
    LISTAINTERNAMENTOS("/techvet/views/ListaInternamentos.fxml"),
    LISTAVENDAS("/techvet/views/ListaVendas.fxml"),
    LISTAARTIGOS("/techvet/views/ListaArtigos.fxml"),
    PROCESSARCONSULTA("/techvet/views/ProcessarConsulta.fxml");
    
    private final String caminhoParaDoc;
    
    DocFXML(String caminhoParaDoc) {
        this.caminhoParaDoc = caminhoParaDoc;
    }
    
    public String getPath() {
        return caminhoParaDoc;
    }
    
}
