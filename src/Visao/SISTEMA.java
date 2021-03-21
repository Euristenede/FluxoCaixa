/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Visao;

import Controle.ConectaBanco;
import Controle.ControleBanco;
import Controle.ControleCCusto;
import Controle.ControleCarro;
import Controle.ControleDespesa;
import Controle.ControleDespesaCCusto;
import Controle.ControleFuncionario;
import Controle.ControleReceita;
import Controle.ModeloTabela;
import Modelo.ModeloBanco;
import Modelo.ModeloCCusto;
import Modelo.ModeloCarro;
import Modelo.ModeloDespesa;
import Modelo.ModeloDespesaCCusto;
import Modelo.ModeloFuncionario;
import Modelo.ModeloReceita;
import java.awt.Color;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.text.MaskFormatter;


/**
 *
 * @author EuristenedeSantos
 */
public class SISTEMA extends javax.swing.JInternalFrame {
    ConectaBanco conexao = new ConectaBanco();
    java.util.Date dataSistema = new Date();
    
    //---VARIÁVEIS PARA IMPORTAR AS TELAS DE CONSULTAS---//
    ConsultaCCusto consultaCCusto;
    ConsultaFuncionario consultaFuncionario;
    ConsultaVeiculo consultaVeiculo;
    ConsultaEmpresa consultaEmpresa;
    ConsultaBanco consultaBanco;
    ConsultaDespCCusto consultaDespCCusto;
    //---------------------------------------------------//
    
    //------VARIÁVEIS COM AS CONSULTAS DAS TABELAS-------//
    String SQL_tabelaBanco = "SELECT * FROM BANCO";
    String SQL_tabelaCCusto = "SELECT * FROM CENTROCUSTO";
    String SQL_tabelaFuncionario = "SELECT * FROM FUNCIONARIO";
    String SQL_tabelaCarro = "SELECT * FROM CARRO";
    String SQL_tabelaDespCCusto = "SELECT * FROM DESPESACCUSTO";
    String SQL_tabelaReceita = "SELECT * FROM RECEITA";
    String SQL_tabelaDespesa = "SELECT * FROM DESPESA";
    String SubSQL_tabelaDespCCustoDsCusto = null;
    String SubSQL_tabelaDespCCustoNmFunc = null;
    String SubSQL_tabelaDespCCustoDsVeic = null;
    String SubSQL_tabelaReceitaDsEmpr = null;
    String SubSQL_tabelaReceitaDsBanco = null;
    String SQL_UpdateSoma = null;
    String SQL_UpdateAltera_Receita = null;
    String SQL_UpdateSubtrai = null;
    String SubSQL_tabelaDespesaDsDespCCusto = null;
    String SQL_UpdateAltera_Despesa = null;
    private void montaSqlDsCCusto(int codigo){
        SubSQL_tabelaDespCCustoDsCusto = "SELECT DS_CCUSTO FROM CENTROCUSTO WHERE CD_CCUSTO = "+codigo+";";
    }
    private void montaSqlNmFunc(int codigo){
        SubSQL_tabelaDespCCustoNmFunc = "SELECT NM_FUNCIONARIO FROM FUNCIONARIO WHERE CD_FUNCIONARIO = "+codigo+";";
    }
    private void montaSqlDsVeic(int codigo){
        SubSQL_tabelaDespCCustoDsVeic = "SELECT DS_MODELO FROM CARRO WHERE CD_CARRO = "+codigo+";";
    }
    private void montaSqlDsEmpr(int codigo){
        SubSQL_tabelaReceitaDsEmpr = "SELECT NM_EMPRESA FROM EMPRESA WHERE CD_EMPRESA = "+codigo+";";
    }
    private void montaSqlDsBanco(int codigo){
        SubSQL_tabelaReceitaDsBanco = "SELECT DS_BANCO FROM BANCO WHERE CD_BANCO = "+codigo+";";
    }
    private void montaSqlUpdateSoma(int codigoBanco, float valor){
        SQL_UpdateSoma = "UPDATE BANCO SET VL_SALDO = (SELECT VL_SALDO + "+valor+" FROM BANCO WHERE CD_BANCO = "+codigoBanco+") WHERE CD_BANCO = "+codigoBanco+";";
    }
    private void montaSqlUpdateAltera_Receita(int codigoBanco, float novoValor){
        SQL_UpdateAltera_Receita = "UPDATE BANCO SET VL_SALDO = (SELECT VL_SALDO - "+valorOriginal+" + "+novoValor+" FROM BANCO WHERE CD_BANCO = "+codigoBanco+") WHERE CD_BANCO = "+codigoBanco+";";
    }
    private void montaSqlUpdateAltera_Despesa(int codigoBanco, float novoValor){
        SQL_UpdateAltera_Despesa = "UPDATE BANCO SET VL_SALDO = (SELECT VL_SALDO + "+valorOriginal+" - "+novoValor+" FROM BANCO WHERE CD_BANCO = "+codigoBanco+") WHERE CD_BANCO = "+codigoBanco+";";
    }
    private void montaSqlUpdateSubtrai(int codigoBanco, float valor){
        SQL_UpdateSubtrai = "UPDATE BANCO SET VL_SALDO = (SELECT VL_SALDO - "+valor+" FROM BANCO WHERE CD_BANCO = "+codigoBanco+") WHERE CD_BANCO = "+codigoBanco+";";
    }
    private void montaSqlDsDespesa(int codigo){
        SubSQL_tabelaDespesaDsDespCCusto = "SELECT DS_DESPCCUSTO FROM DESPESACCUSTO WHERE CD_DESPCCUSTO = "+codigo+";";
    }
    //---------------------------------------------------//
    
    //-------------VARIÁVEIS GLOBAIS---------------------//
    //Essas variáveis guarda o código das tabelas que tem nas abas, esse código
    //é usado pra fazer update ou delete;
    int codigoBanco;
    int codigoCCusto;
    int codigoFuncionario;
    int codigoCarro;
    int codigoDespCCusto;
    int codigoReceita;
    int codigoDespesa;
    int fkCCusto;//Usado no parâmetro da tela de consulta de despesas por centro de custos.
    boolean aPagar;//Variável guardo o valor list box a pagar da despesa.
    //---------------------------------------------------//
    
    //-------------VARIÁVEIS PARA UPDATE-----------------//
    //Essa variável, guarda o valor da receita ou despesa,
    //quando clicado na tabela, serve para atualizar o saldo
    //do banco, quando a operação for de alteração ou exclusão
    //da Receita ou Despesa.
    String valorOriginal;
    //---------------------------------------------------//
    
    //-------------MASCARAS PARA CAMPOS------------------//
    String telefoneMask = "(##)# #### ####";
    String cpfMask = "###.###.###-##";
    String dataMask = "##/##/####";
    //---------------------------------------------------//
    /**
     * Creates new form SISTEMA
     * @throws java.text.ParseException
     */
    //--VARIÁVEIS QUE RECEBE AS DESCRIÇÕES DA CONSULTA---//
    String jtDS_CCusto = null;
    String jtNM_func = null;
    String jtDS_modelo = null; //carro
    String jtDS_Empresa = null;
    String jtDS_Banco = null;
    String jtDS_DespCCusto = null;
    //---------------------------------------------------//
    
    public SISTEMA() throws ParseException {
        conexao.conecta();
        consultaCCusto = new ConsultaCCusto(new javax.swing.JFrame(), true);
        consultaFuncionario = new ConsultaFuncionario(new javax.swing.JFrame(), true);
        consultaVeiculo = new ConsultaVeiculo(new javax.swing.JFrame(), true);
        consultaEmpresa = new ConsultaEmpresa(new javax.swing.JFrame(), true);
        consultaBanco = new ConsultaBanco(new javax.swing.JFrame(), true);
        initComponents();
        preencherTabelaBanco(SQL_tabelaBanco);
        preencherTabelaCCusto(SQL_tabelaCCusto);
        preencherTabelaFuncionario(SQL_tabelaFuncionario);
        preencherTabelaCarro(SQL_tabelaCarro);
        preencherTabelaDespesaccusto(SQL_tabelaDespCCusto);
        preencherTabelaReceita(SQL_tabelaReceita);
        preencherTabelaDespesa(SQL_tabelaDespesa);
        jFormattedTextFieldDataCCusto.setText(java.text.DateFormat.getDateInstance(DateFormat.MEDIUM).format(dataSistema));
        jFormattedTextFieldDataDespCCusto.setText(java.text.DateFormat.getDateInstance(DateFormat.MEDIUM).format(dataSistema));
        jFormattedTextFieldDtReceita.setText(java.text.DateFormat.getDateInstance(DateFormat.MEDIUM).format(dataSistema));
        jFormattedTextFieldDtDespesa.setText(java.text.DateFormat.getDateInstance(DateFormat.MEDIUM).format(dataSistema));
    }

    //---------------------Aba Banco--------------------------//
    
    public void preencherTabelaBanco(String SQL){
        ArrayList dados = new ArrayList();
        
        String [] Colunas = new String[]{"Código","Banco","Agência","Conta","Saldo"};
    
        conexao.executaSQL(SQL);
        try {
            conexao.rs.first();
            do{
                dados.add(new Object[]{conexao.rs.getString("CD_BANCO"),conexao.rs.getString("DS_BANCO"), conexao.rs.getInt("NR_AGENCIA"),conexao.rs.getString("NR_CONTA"),conexao.rs.getFloat("VL_SALDO")});
            }while(conexao.rs.next());
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList do Banco. \nErro: "+ ex);
        }
       
        ModeloTabela modelo = new ModeloTabela(dados, Colunas);//Monta a tabela
        jTableBANCO.setModel(modelo);
        jTableBANCO.getColumnModel().getColumn(0).setPreferredWidth(50);//Largura da coluna
        jTableBANCO.getColumnModel().getColumn(0).setResizable(false);
        jTableBANCO.getColumnModel().getColumn(1).setPreferredWidth(310);//Largura da coluna
        jTableBANCO.getColumnModel().getColumn(1).setResizable(false);
        jTableBANCO.getColumnModel().getColumn(2).setPreferredWidth(150);//Largura da coluna
        jTableBANCO.getColumnModel().getColumn(2).setResizable(false);
        jTableBANCO.getColumnModel().getColumn(3).setPreferredWidth(200);//Largura da coluna
        jTableBANCO.getColumnModel().getColumn(3).setResizable(false);
        jTableBANCO.getColumnModel().getColumn(4).setPreferredWidth(200);//Largura da coluna
        jTableBANCO.getColumnModel().getColumn(4).setResizable(false);
        jTableBANCO.getTableHeader().setReorderingAllowed(false);// Para a tabela não ser reorganizada
        jTableBANCO.setAutoResizeMode(jTableBANCO.AUTO_RESIZE_OFF);// Parametros de seleção
        jTableBANCO.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// Parametros de seleção
    }
    
    public void limpar_camposBanco(){
        jFormattedTextFieldDS_BANCO.setText("");
        jTextFieldNR_AGENCIA.setText("");
        jTextFieldNR_CONTA.setText("");
        jTextFieldVL_SALDO.setText("");
    }
    
    public boolean verifica_campos_vazio_banco(){
        if(jFormattedTextFieldDS_BANCO.getText().isEmpty() || jTextFieldNR_AGENCIA.getText().isEmpty() ||
                jTextFieldNR_CONTA.getText().isEmpty() || jTextFieldVL_SALDO.getText().isEmpty()){
            return true;
        }else{
            return false;
        }
    }
    
    public void habilitar_campo_banco(boolean valor){
        jButtonAlterarBanco.setEnabled(valor);
        jButtonExcluiBanco.setEnabled(valor);
    }
    
    //---------------------Fim Aba Banco--------------------------//
    
    //---------------------Aba Centro Custo--------------------------//
    
    public void preencherTabelaCCusto(String SQL){
        ArrayList dados = new ArrayList();
        
        String [] Colunas = new String[]{"Código","Descição","Data"};
    
        conexao.executaSQL(SQL);
        try {
            conexao.rs.first();
            do{
                dados.add(new Object[]{conexao.rs.getString("CD_CCUSTO"),conexao.rs.getString("DS_CCUSTO"), conexao.rs.getDate("DT_RECORD")});
            }while(conexao.rs.next());
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList do Centro de Custo. \nErro: "+ ex);
        }
       
        ModeloTabela modelo = new ModeloTabela(dados, Colunas);//Monta a tabela
        jTableCentroCusto.setModel(modelo);
        jTableCentroCusto.getColumnModel().getColumn(0).setPreferredWidth(120);//Largura da coluna
        jTableCentroCusto.getColumnModel().getColumn(0).setResizable(false);
        jTableCentroCusto.getColumnModel().getColumn(1).setPreferredWidth(650);//Largura da coluna
        jTableCentroCusto.getColumnModel().getColumn(1).setResizable(false);
        jTableCentroCusto.getColumnModel().getColumn(2).setPreferredWidth(200);//Largura da coluna
        jTableCentroCusto.getColumnModel().getColumn(2).setResizable(false);
        jTableCentroCusto.getTableHeader().setReorderingAllowed(false);// Para a tabela não ser reorganizada
        jTableCentroCusto.setAutoResizeMode(jTableCentroCusto.AUTO_RESIZE_OFF);// Parametros de seleção
        jTableCentroCusto.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// Parametros de seleção
    }
    
    public void limpar_camposCCusto(){
        jTextFieldDS_CentroCusto.setText("");
    }
    
    public boolean verifica_campos_vazio_ccusto(){
        if(jFormattedTextFieldDataCCusto.getText().isEmpty() || jTextFieldDS_CentroCusto.getText().isEmpty()){
            return true;
        }else{
            return false;
        }
    }
    
    public void habilitar_campo_ccusto(boolean valor){
        jButtonAlterarCCusto.setEnabled(valor);
        jButtonExcluirCCusto.setEnabled(valor);
    }
    
    //---------------------Fim Aba Centro Custo--------------------------//
    
    //--------------------------Aba Funcionario--------------------------//
    
    public void preencherTabelaFuncionario(String SQL) throws ParseException{
        ArrayList dados = new ArrayList();
        
        String [] Colunas = new String[]{"CD","Nome","RG","CPF","DT_Nasc","DT_Admis","Salário","Telefone","Cargo"};
    
        conexao.executaSQL(SQL);
        try {
            conexao.rs.first();
            do{
               MaskFormatter telefoneFormatter = new MaskFormatter(telefoneMask);
               telefoneFormatter.setValueContainsLiteralCharacters(false);
               MaskFormatter cpfFormatter= new MaskFormatter(cpfMask);
               cpfFormatter.setValueContainsLiteralCharacters(false);
               
                dados.add(new Object[]{conexao.rs.getInt("CD_FUNCIONARIO"),
                                       conexao.rs.getString("NM_FUNCIONARIO"), 
                                       conexao.rs.getLong("NR_RG"),
                                       cpfFormatter.valueToString(conexao.rs.getLong("NR_CPF")),
                                       conexao.rs.getDate("DT_NASCIMENTO"),
                                       conexao.rs.getDate("DT_ADMISSAO"),
                                       conexao.rs.getFloat("VL_SALARIO"),
                                       telefoneFormatter.valueToString(conexao.rs.getLong("NR_TELEFONE")),
                                       conexao.rs.getString("DS_CARGO"),});
            }while(conexao.rs.next());
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList do Funcionario. \nErro: "+ ex);
        }
       
        ModeloTabela modelo = new ModeloTabela(dados, Colunas);//Monta a tabela
        jTableFuncionario.setModel(modelo);
        jTableFuncionario.getColumnModel().getColumn(0).setPreferredWidth(30);//Largura da coluna
        jTableFuncionario.getColumnModel().getColumn(0).setResizable(false);
        jTableFuncionario.getColumnModel().getColumn(1).setPreferredWidth(300);//Largura da coluna
        jTableFuncionario.getColumnModel().getColumn(1).setResizable(false);
        jTableFuncionario.getColumnModel().getColumn(2).setPreferredWidth(100);//Largura da coluna
        jTableFuncionario.getColumnModel().getColumn(2).setResizable(false);
        jTableFuncionario.getColumnModel().getColumn(3).setPreferredWidth(100);//Largura da coluna
        jTableFuncionario.getColumnModel().getColumn(3).setResizable(false);
        jTableFuncionario.getColumnModel().getColumn(4).setPreferredWidth(80);//Largura da coluna
        jTableFuncionario.getColumnModel().getColumn(4).setResizable(false);
        jTableFuncionario.getColumnModel().getColumn(5).setPreferredWidth(80);//Largura da coluna
        jTableFuncionario.getColumnModel().getColumn(5).setResizable(false);
        jTableFuncionario.getColumnModel().getColumn(6).setPreferredWidth(60);//Largura da coluna
        jTableFuncionario.getColumnModel().getColumn(6).setResizable(false);
        jTableFuncionario.getColumnModel().getColumn(7).setPreferredWidth(110);//Largura da coluna
        jTableFuncionario.getColumnModel().getColumn(7).setResizable(false);
        jTableFuncionario.getColumnModel().getColumn(8).setPreferredWidth(100);//Largura da coluna
        jTableFuncionario.getColumnModel().getColumn(8).setResizable(false);
        jTableFuncionario.getTableHeader().setReorderingAllowed(false);// Para a tabela não ser reorganizada
        jTableFuncionario.setAutoResizeMode(jTableFuncionario.AUTO_RESIZE_OFF);// Parametros de seleção
        jTableFuncionario.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// Parametros de seleção
    }
    
    public void limpar_camposFuncionario(){
        jTextFieldNomeFuncionario.setText("");
        jFormattedTextFieldRGFuncionario.setText("");
        jFormattedTextFieldCPFuncionario.setText("");
        jFormattedTextFieldDT_NacFunc.setText("");
        jFormattedTextFieldDT_AdmiFuncionario.setText("");
        jFormattedTextFieldSalarioFunc.setText("");
        jFormattedTextFieldTelFunc.setText("");
        jFormattedTextFieldCargoFunc.setText("");
    }
    
    public boolean verifica_campos_vazio_funcionario(){
        if(jTextFieldNomeFuncionario.getText().isEmpty() || jFormattedTextFieldRGFuncionario.getText().isEmpty()||
           jFormattedTextFieldCPFuncionario.getText().isEmpty() || jFormattedTextFieldDT_NacFunc.getText().isEmpty()||
           jFormattedTextFieldDT_AdmiFuncionario.getText().isEmpty() || jFormattedTextFieldSalarioFunc.getText().isEmpty()||
           jFormattedTextFieldTelFunc.getText().isEmpty() || jFormattedTextFieldCargoFunc.getText().isEmpty()){
            return true;
        }else{
            return false;
        }
    }
    
    public void habilitar_campo_funcionario(boolean valor){
        jButtonAlterarFuncionario.setEnabled(valor);
        jButtonExcluirFuncionario.setEnabled(valor);
    }
    
    //---------------------Fim Aba Funcionario--------------------------//
    
    //--------------------------Aba Carro--------------------------//
    
    public void preencherTabelaCarro(String SQL) throws ParseException{
        ArrayList dados = new ArrayList();
        
        String [] Colunas = new String[]{"Código","Placa","Marca","Modelo","Ano","Combustível","Documento","Vencimento"};
    
        conexao.executaSQL(SQL);
        try {
            conexao.rs.first();
            do{  String combustivel = null;
                if(conexao.rs.getInt("TP_COMBUSTIVEL") == 0){
                    combustivel = "Gasolina";
                }else if(conexao.rs.getInt("TP_COMBUSTIVEL") == 1){
                    combustivel = "Disel";
                }else if(conexao.rs.getInt("TP_COMBUSTIVEL") == 2){
                    combustivel = "Etanol";
                }else{
                    combustivel = "Flex";
                }
                dados.add(new Object[]{conexao.rs.getInt("CD_CARRO"),
                                       conexao.rs.getString("NR_PLACA"), 
                                       conexao.rs.getString("DS_MARCA"),
                                       conexao.rs.getString("DS_MODELO"),
                                       conexao.rs.getInt("NR_ANO"),
                                       combustivel,
                                       conexao.rs.getLong("NR_DOCUMENTO"),
                                       conexao.rs.getDate("DT_VECDOCUMENTO")});
            }while(conexao.rs.next());
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList do Carro. \nErro: "+ ex);
        }
       
        ModeloTabela modelo = new ModeloTabela(dados, Colunas);//Monta a tabela
        jTableCarro.setModel(modelo);
        jTableCarro.getColumnModel().getColumn(0).setPreferredWidth(60);//Largura da coluna
        jTableCarro.getColumnModel().getColumn(0).setResizable(false);
        jTableCarro.getColumnModel().getColumn(1).setPreferredWidth(100);//Largura da coluna
        jTableCarro.getColumnModel().getColumn(1).setResizable(false);
        jTableCarro.getColumnModel().getColumn(2).setPreferredWidth(100);//Largura da coluna
        jTableCarro.getColumnModel().getColumn(2).setResizable(false);
        jTableCarro.getColumnModel().getColumn(3).setPreferredWidth(250);//Largura da coluna
        jTableCarro.getColumnModel().getColumn(3).setResizable(false);
        jTableCarro.getColumnModel().getColumn(4).setPreferredWidth(80);//Largura da coluna
        jTableCarro.getColumnModel().getColumn(4).setResizable(false);
        jTableCarro.getColumnModel().getColumn(5).setPreferredWidth(120);//Largura da coluna
        jTableCarro.getColumnModel().getColumn(5).setResizable(false);
        jTableCarro.getColumnModel().getColumn(6).setPreferredWidth(150);//Largura da coluna
        jTableCarro.getColumnModel().getColumn(6).setResizable(false);
        jTableCarro.getColumnModel().getColumn(7).setPreferredWidth(110);//Largura da coluna
        jTableCarro.getColumnModel().getColumn(7).setResizable(false);
        jTableCarro.getTableHeader().setReorderingAllowed(false);// Para a tabela não ser reorganizada
        jTableCarro.setAutoResizeMode(jTableCarro.AUTO_RESIZE_OFF);// Parametros de seleção
        jTableCarro.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// Parametros de seleção
    }
    
    public void limpar_camposCarro(){
        jTextFieldNrPlacaCarro.setText("");
        jTextFieldMarcaCarro.setText("");
        jTextFieldModeloCarro.setText("");
        jTextFieldAnoCarro.setText("");
        jTextFieldNrDocCarro.setText("");
        jFormattedTextFieldDtVencDocCarro.setText("");
        jComboBoxTpCombustivel.setSelectedIndex(0);
    }
    
    public boolean verifica_campos_vazio_carro(){
        if(jTextFieldNrPlacaCarro.getText().isEmpty() || jTextFieldMarcaCarro.getText().isEmpty()||
           jTextFieldModeloCarro.getText().isEmpty() || jTextFieldAnoCarro.getText().isEmpty()||
           jTextFieldNrDocCarro.getText().isEmpty() || jFormattedTextFieldDtVencDocCarro.getText().isEmpty()){
            return true;
        }else{
            return false;
        }
    }
    
    public void habilitar_campo_carro(boolean valor){
        jButtonAlterarCarro.setEnabled(valor);
        jButtonExcluirCarro.setEnabled(valor);
    }
    
    //---------------------Fim Aba Carro--------------------------//
    
//---------------Aba Despesa por Centro de Custo------------------//
    
    public void preencherTabelaDespesaccusto(String SQL) throws ParseException{
        ArrayList dados = new ArrayList();
        String dsCCusto;
        String dsVeiculo;
        String dsFuncionario;
        
        String [] Colunas = new String[]{"Código","Descrição","Centro de Custo","Funcionário","Veículo"};
    
        conexao.executaSQL(SQL);
        try {
            conexao.rs.first();
            do{  
                dsCCusto = null;
                dsVeiculo = null;
                dsFuncionario = null;
                montaSqlDsCCusto(conexao.rs.getInt("FK_CCUSTO"));
                montaSqlDsVeic(conexao.rs.getInt("FK_CARRO"));
                montaSqlNmFunc(conexao.rs.getInt("FK_FUNCIONARIO"));
                
                conexao.executaSqlSubConsulta(SubSQL_tabelaDespCCustoDsCusto);
                try {
                    conexao.rsSubConsulta.first();
                    do{  
                        dsCCusto = conexao.rsSubConsulta.getString("DS_CCUSTO");
                    }while(conexao.rsSubConsulta.next());

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList da Sub Consulta do Centro de Custo. \nErro: "+ ex);
                }
                
                conexao.executaSqlSubConsulta(SubSQL_tabelaDespCCustoDsVeic);
                try {
                    conexao.rsSubConsulta.first();
                    do{  
                        dsVeiculo = conexao.rsSubConsulta.getString("DS_MODELO");
                    }while(conexao.rsSubConsulta.next());

                } catch (SQLException ex) {
                    //JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList da Sub Consulta do Veículo. \nErro: "+ ex);
                }
                
                conexao.executaSqlSubConsulta(SubSQL_tabelaDespCCustoNmFunc);
                try {
                    conexao.rsSubConsulta.first();
                    do{  
                        dsFuncionario = conexao.rsSubConsulta.getString("NM_FUNCIONARIO");
                    }while(conexao.rsSubConsulta.next());

                } catch (SQLException ex) {
                    //JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList da Sub Consulta do Centro de Custo. \nErro: "+ ex);
                }
                
                dados.add(new Object[]{conexao.rs.getInt("CD_DESPCCUSTO"),
                                       conexao.rs.getString("DS_DESPCCUSTO"), 
                                       dsCCusto,
                                       dsFuncionario,
                                       dsVeiculo});
            }while(conexao.rs.next());
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList da Despesa por Centro de Custo. \nErro: "+ ex);
        }
       
        ModeloTabela modelo = new ModeloTabela(dados, Colunas);//Monta a tabela
        jTableDespesaCCusto.setModel(modelo);
        jTableDespesaCCusto.getColumnModel().getColumn(0).setPreferredWidth(60);//Largura da coluna
        jTableDespesaCCusto.getColumnModel().getColumn(0).setResizable(false);
        jTableDespesaCCusto.getColumnModel().getColumn(1).setPreferredWidth(200);//Largura da coluna
        jTableDespesaCCusto.getColumnModel().getColumn(1).setResizable(false);
        jTableDespesaCCusto.getColumnModel().getColumn(2).setPreferredWidth(250);//Largura da coluna
        jTableDespesaCCusto.getColumnModel().getColumn(2).setResizable(false);
        jTableDespesaCCusto.getColumnModel().getColumn(3).setPreferredWidth(250);//Largura da coluna
        jTableDespesaCCusto.getColumnModel().getColumn(3).setResizable(false);
        jTableDespesaCCusto.getColumnModel().getColumn(4).setPreferredWidth(200);//Largura da coluna
        jTableDespesaCCusto.getColumnModel().getColumn(4).setResizable(false);
        jTableDespesaCCusto.getTableHeader().setReorderingAllowed(false);// Para a tabela não ser reorganizada
        jTableDespesaCCusto.setAutoResizeMode(jTableDespesaCCusto.AUTO_RESIZE_OFF);// Parametros de seleção
        jTableDespesaCCusto.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// Parametros de seleção
    }
    
    public void limpar_camposdespesaccusto(){
        jTextFieldCdCCusto.setText("");
        jTextFieldDsCCusto.setText("");
        jTextFieldDsDespCCusto.setText("");
        jTextFieldCodFuncDespCCusto.setText("");
        jTextFieldNomeFuncDespCCusto.setText("");
        jTextFieldCodVeicDespCCusto.setText("");
        jTextFieldDescVeicDespCCusto.setText("");
        jCheckBoxFuncDespCCusto.setSelected(false);
        jCheckBoxVeicDespCCusto.setSelected(false);
    }
    
    public boolean verifica_campos_vazio_despesaccusto(){
        if(jTextFieldCdCCusto.getText().isEmpty() || jTextFieldDsDespCCusto.getText().isEmpty()){
            return true;
        }else if((jCheckBoxFuncDespCCusto.isSelected() == true) &&(jTextFieldCdCCusto.getText().isEmpty() || 
                                                                   jTextFieldDsDespCCusto.getText().isEmpty() ||
                                                                   jTextFieldCodFuncDespCCusto.getText().isEmpty())){
            return true;
        }else if((jCheckBoxVeicDespCCusto.isSelected() == true) &&(jTextFieldCdCCusto.getText().isEmpty() || 
                                                                   jTextFieldDsDespCCusto.getText().isEmpty() ||
                                                                   jTextFieldCodVeicDespCCusto.getText().isEmpty())){
            return true;
        }else if(((jCheckBoxFuncDespCCusto.isSelected() == true)&&(jCheckBoxVeicDespCCusto.isSelected() == true)) &&
                                                                   (jTextFieldCdCCusto.getText().isEmpty() || 
                                                                    jTextFieldDsDespCCusto.getText().isEmpty() ||
                                                                    jTextFieldCodVeicDespCCusto.getText().isEmpty() ||
                                                                    jTextFieldCodVeicDespCCusto.getText().isEmpty())){
            return true;
        }else{
            return false;
        }
    }
    
    public void habilitar_campo_despesaccusto(boolean valor){
        jButtonAlterarDespCCusto.setEnabled(valor);
        jButtonExcluirDespCCusto.setEnabled(valor);
    }
    
    public void retornaFks(int codigo){
        int FK_CCUSTO = 0;
        int FK_FUNCIONARIO = 0;
        int FK_CARRO = 0;
        conexao.executaSQL("SELECT * FROM DESPESACCUSTO WHERE CD_DESPCCUSTO = "+codigo+";");
        try {
            conexao.rs.first();
            do{  
                FK_CCUSTO = conexao.rs.getInt("FK_CCUSTO");
                FK_FUNCIONARIO = conexao.rs.getInt("FK_FUNCIONARIO");
                FK_CARRO = conexao.rs.getInt("FK_CARRO");
            }while(conexao.rs.next());

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar as FKs das Despesas por centro de custo. \nErro: "+ ex);
        }
        jTextFieldCdCCusto.setText(FK_CCUSTO+"");
        if(FK_FUNCIONARIO != 0){
            jTextFieldCodFuncDespCCusto.setText(FK_FUNCIONARIO+"");
            jCheckBoxFuncDespCCusto.setSelected(true);
            jTextFieldCodFuncDespCCusto.setEditable(true);
        }else{
            jTextFieldCodFuncDespCCusto.setEditable(false);
        }
        if(FK_CARRO != 0){
            jTextFieldCodVeicDespCCusto.setText(FK_CARRO+"");
            jCheckBoxVeicDespCCusto.setSelected(true);
            jTextFieldCodVeicDespCCusto.setEditable(true);
        }else{
            jTextFieldCodVeicDespCCusto.setEditable(false);
        }
    }
    //------------Fim Aba Despesa por Centro de Custo------------------//
    
    //--------------------------Aba Receita--------------------------//
    
    public void preencherTabelaReceita(String SQL) throws ParseException{
        ArrayList dados = new ArrayList();
        String dsEmpresa;
        String dsBanco;
        String [] Colunas = new String[]{"Código","Valor","Descrição","Data","Empresa","Banco"};
    
        conexao.executaSQL(SQL);
        try {
            conexao.rs.first();
            do{  
                dsEmpresa = null;
                dsBanco = null;
                montaSqlDsBanco(conexao.rs.getInt("FK_BANCO"));
                montaSqlDsEmpr(conexao.rs.getInt("FK_EMPRESA"));
                
                conexao.executaSqlSubConsulta(SubSQL_tabelaReceitaDsBanco);
                try {
                    conexao.rsSubConsulta.first();
                    do{  
                        dsBanco = conexao.rsSubConsulta.getString("DS_BANCO");
                    }while(conexao.rsSubConsulta.next());

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList da Sub Consulta do Banco. \nErro: "+ ex);
                }
                
                conexao.executaSqlSubConsulta(SubSQL_tabelaReceitaDsEmpr);
                try {
                    conexao.rsSubConsulta.first();
                    do{  
                        dsEmpresa = conexao.rsSubConsulta.getString("NM_EMPRESA");
                    }while(conexao.rsSubConsulta.next());

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList da Sub Consulta da Empresa. \nErro: "+ ex);
                }
                
                dados.add(new Object[]{conexao.rs.getInt("CD_RECEITA"),
                                       conexao.rs.getString("VL_RECEITA"), 
                                       conexao.rs.getString("DS_RECEITA"),
                                       conexao.rs.getDate("DT_RECEITA"),
                                       dsEmpresa,
                                       dsBanco});
            }while(conexao.rs.next());
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList da Receita. \nErro: "+ ex);
        }
       
        ModeloTabela modelo = new ModeloTabela(dados, Colunas);//Monta a tabela
        jTableReceita.setModel(modelo);
        jTableReceita.getColumnModel().getColumn(0).setPreferredWidth(60);//Largura da coluna
        jTableReceita.getColumnModel().getColumn(0).setResizable(false);
        jTableReceita.getColumnModel().getColumn(1).setPreferredWidth(100);//Largura da coluna
        jTableReceita.getColumnModel().getColumn(1).setResizable(false);
        jTableReceita.getColumnModel().getColumn(2).setPreferredWidth(200);//Largura da coluna
        jTableReceita.getColumnModel().getColumn(2).setResizable(false);
        jTableReceita.getColumnModel().getColumn(3).setPreferredWidth(80);//Largura da coluna
        jTableReceita.getColumnModel().getColumn(3).setResizable(false);
        jTableReceita.getColumnModel().getColumn(4).setPreferredWidth(250);//Largura da coluna
        jTableReceita.getColumnModel().getColumn(4).setResizable(false);
        jTableReceita.getColumnModel().getColumn(5).setPreferredWidth(250);//Largura da coluna
        jTableReceita.getColumnModel().getColumn(5).setResizable(false);
        jTableReceita.getTableHeader().setReorderingAllowed(false);// Para a tabela não ser reorganizada
        jTableReceita.setAutoResizeMode(jTableReceita.AUTO_RESIZE_OFF);// Parametros de seleção
        jTableReceita.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// Parametros de seleção
    }
    
    public void limpar_camposReceita(){
        jFormattedTextFieldDtReceita.setText("");
        jTextFieldCodEmpresa.setText("");
        jTextFieldDsEmpresa.setText("");
        jTextFieldCodBanco.setText("");
        jTextFieldDsBanco.setText("");
        jTextFieldVlReceita.setText("");
        jTextFieldDsReceita.setText("");
    }
    
    public boolean verifica_campos_vazio_Receita(){
        if(jFormattedTextFieldDtReceita.getText().isEmpty() || jTextFieldCodEmpresa.getText().isEmpty()||
           jTextFieldCodBanco.getText().isEmpty() || jTextFieldVlReceita.getText().isEmpty()||
           jTextFieldDsReceita.getText().isEmpty()){
            return true;
        }else{
            return false;
        }
    }
    
    public void habilitar_campo_Receita(boolean valor){
        jButtonAlterarReceita.setEnabled(valor);
        jButtonExcluirReceita.setEnabled(valor);
    }
    
    public void retornaFksReceita(int codigo){
        int FK_BANCO = 0;
        int FK_EMPRESA = 0;
        conexao.executaSQL("SELECT * FROM RECEITA WHERE CD_RECEITA = "+codigo+";");
        try {
            conexao.rs.first();
            do{  
                FK_BANCO = conexao.rs.getInt("FK_BANCO");
                FK_EMPRESA = conexao.rs.getInt("FK_EMPRESA");
            }while(conexao.rs.next());

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar as FKs da Receita. \nErro: "+ ex);
        }
        jTextFieldCodEmpresa.setText(FK_EMPRESA+"");
        jTextFieldCodBanco.setText(FK_BANCO+"");
    }
    
    //---------------------Fim Aba Receita--------------------------//
    
    //------------------------Aba Despesa---------------------------//
    
    public void preencherTabelaDespesa(String SQL) throws ParseException{
        ArrayList dados = new ArrayList();
        String dsCCusto;
        String dsDespesa;
        String dsBanco;
        String situacao;
        
        String [] Colunas = new String[]{"Código","Valor","Descrição","Centro de Custo","Data","Situação","Banco"};
    
        conexao.executaSQL(SQL);
        try {
            conexao.rs.first();
            do{  
                dsCCusto = null;
                dsDespesa = null;
                dsBanco = null;
                situacao = null;
                montaSqlDsCCusto(conexao.rs.getInt("FK_CCUSTO"));
                montaSqlDsDespesa(conexao.rs.getInt("FK_DESPCCUSTO"));
                montaSqlDsBanco(conexao.rs.getInt("FK_BANCO"));
                
                if(conexao.rs.getInt("ST_DESPESA") == 0){
                    situacao = "Pago";
                }else if(conexao.rs.getInt("ST_DESPESA") == 1){
                    situacao = "A Pagar";
                }
                
                conexao.executaSqlSubConsulta(SubSQL_tabelaDespCCustoDsCusto);
                try {
                    conexao.rsSubConsulta.first();
                    do{  
                        dsCCusto = conexao.rsSubConsulta.getString("DS_CCUSTO");
                    }while(conexao.rsSubConsulta.next());

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList da Sub Consulta do Centro de Custo. \nErro: "+ ex);
                }
                
                conexao.executaSqlSubConsulta(SubSQL_tabelaDespesaDsDespCCusto);
                try {
                    conexao.rsSubConsulta.first();
                    do{  
                        dsDespesa = conexao.rsSubConsulta.getString("DS_DESPCCUSTO");
                    }while(conexao.rsSubConsulta.next());

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList da Sub Consulta da Despesa por Centro de Custo. \nErro: "+ ex);
                }
                
                conexao.executaSqlSubConsulta(SubSQL_tabelaReceitaDsBanco);
                try {
                    conexao.rsSubConsulta.first();
                    do{  
                        dsBanco = conexao.rsSubConsulta.getString("DS_BANCO");
                    }while(conexao.rsSubConsulta.next());

                } catch (SQLException ex) {
                    //JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList do Banco. \nErro: "+ ex);
                }
                
                dados.add(new Object[]{conexao.rs.getInt("CD_DESPESA"),
                                       conexao.rs.getString("VL_DESPESA"), 
                                       dsDespesa,
                                       dsCCusto,
                                       conexao.rs.getString("DT_DESPESA"),
                                       situacao,
                                       dsBanco});
            }while(conexao.rs.next());
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList da Despesa. \nErro: "+ ex);
        }
       
        ModeloTabela modelo = new ModeloTabela(dados, Colunas);//Monta a tabela
        jTableDespesa.setModel(modelo);
        jTableDespesa.getColumnModel().getColumn(0).setPreferredWidth(60);//Largura da coluna
        jTableDespesa.getColumnModel().getColumn(0).setResizable(false);
        jTableDespesa.getColumnModel().getColumn(1).setPreferredWidth(80);//Largura da coluna
        jTableDespesa.getColumnModel().getColumn(1).setResizable(false);
        jTableDespesa.getColumnModel().getColumn(2).setPreferredWidth(250);//Largura da coluna
        jTableDespesa.getColumnModel().getColumn(2).setResizable(false);
        jTableDespesa.getColumnModel().getColumn(3).setPreferredWidth(200);//Largura da coluna
        jTableDespesa.getColumnModel().getColumn(3).setResizable(false);
        jTableDespesa.getColumnModel().getColumn(4).setPreferredWidth(90);//Largura da coluna
        jTableDespesa.getColumnModel().getColumn(4).setResizable(false);
        jTableDespesa.getColumnModel().getColumn(5).setPreferredWidth(70);//Largura da coluna
        jTableDespesa.getColumnModel().getColumn(5).setResizable(false);
        jTableDespesa.getColumnModel().getColumn(6).setPreferredWidth(200);//Largura da coluna
        jTableDespesa.getColumnModel().getColumn(6).setResizable(false);
        jTableDespesa.getTableHeader().setReorderingAllowed(false);// Para a tabela não ser reorganizada
        jTableDespesa.setAutoResizeMode(jTableDespesa.AUTO_RESIZE_OFF);// Parametros de seleção
        jTableDespesa.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// Parametros de seleção
    }
    
    public void limpar_camposdespesa(){
        jFormattedTextFieldDtDespesa.setText("");
        jTextFieldCdCCustoDesp.setText("");
        jTextFieldDsCCuscoDesp.setText("");
        jTextFieldCdDespCCusto.setText("");
        jTextFieldDsDespCCustoDesp.setText("");
        jTextFieldVlDesp.setText("");
        jComboBoxStDesp.setSelectedIndex(0);
        jTextFieldCdBancoDesp.setText("");
        jTextFieldDsBancoDesp.setText("");
    }
    
    public boolean verifica_campos_vazio_despesa(){
        if(jComboBoxStDesp.getSelectedIndex() == 0){
            if(jFormattedTextFieldDtDespesa.getText().isEmpty() || jTextFieldCdCCustoDesp.getText().isEmpty() ||
                jTextFieldCdDespCCusto.getText().isEmpty() || jTextFieldVlDesp.getText().isEmpty() || jTextFieldCdBancoDesp.getText().isEmpty()){
                 return true;
             }else{
                 return false;
             }
        }else{
            if(jFormattedTextFieldDtDespesa.getText().isEmpty() || jTextFieldCdCCustoDesp.getText().isEmpty() ||
                jTextFieldCdDespCCusto.getText().isEmpty() || jTextFieldVlDesp.getText().isEmpty()){
                 return true;
             }else{
                 return false;
             }
        }
        
    }
    
    public void habilitar_campo_despesa(boolean valor){
        jButtonAlterarDesp.setEnabled(valor);
        jButtonExcluirDesp.setEnabled(valor);
    }
    
    public void retornaFksDespesa(int codigo){
        int FK_CCUSTO = 0;
        int FK_DESPCCUSTO = 0;
        int FK_BANCO = 0;
        conexao.executaSQL("SELECT * FROM DESPESA WHERE CD_DESPESA = "+codigo+";");
        try {
            conexao.rs.first();
            do{  
                FK_CCUSTO = conexao.rs.getInt("FK_CCUSTO");
                FK_DESPCCUSTO = conexao.rs.getInt("FK_DESPCCUSTO");
                FK_BANCO = conexao.rs.getInt("FK_BANCO");
            }while(conexao.rs.next());

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar as FKs das Despesas. \nErro: "+ ex);
        }
        jTextFieldCdCCustoDesp.setText(FK_CCUSTO+"");
        jTextFieldCdDespCCusto.setText(FK_DESPCCUSTO+"");
        if(FK_BANCO != 0){
            jTextFieldCdBancoDesp.setText(FK_BANCO+"");
        }
    }
    //----------------------Fim Aba Despesa ---------------------------//
    
    public boolean verificaSaldo(int codigoBanco, float valor){
        String SQL = "SELECT * FROM BANCO WHERE CD_BANCO = "+codigoBanco+";";
        Float saldo = null;
        conexao.executaSqlSubConsulta(SQL);
        try {
            conexao.rsSubConsulta.first();
            do{  
                saldo = conexao.rsSubConsulta.getFloat("VL_SALDO");
            }while(conexao.rsSubConsulta.next());

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar o saldo do Banco. \nErro: "+ ex);
        }
        if((saldo - valor) < 0){
            JOptionPane.showMessageDialog(null, "Não é possível pagar essa despesa, pois o saldo da conta é de: R$ "+saldo+". Por favor, tente pagar usando outra conta bancária.");
            return false;
        }else{
            return true;
        }
    }
    
    public boolean verificaSaldoAlteracao(int codigoBanco, float vlOriginal, float valor){
        String SQL = "SELECT * FROM BANCO WHERE CD_BANCO = "+codigoBanco+";";
        Float saldo = null;
        conexao.executaSqlSubConsulta(SQL);
        try {
            conexao.rsSubConsulta.first();
            do{  
                saldo = conexao.rsSubConsulta.getFloat("VL_SALDO");
            }while(conexao.rsSubConsulta.next());

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar o saldo do Banco. \nErro: "+ ex);
        }
        if(((saldo + vlOriginal)- valor) < 0){
            JOptionPane.showMessageDialog(null, "Não é possível fazer essa alteração na despesa, pois o saldo da conta é de: R$ "+saldo+". Por favor, tente pagar usando outra conta bancária.");
            return false;
        }else{
            return true;
        }
    }
    
    public void estornaSaldo(int codigoBanco, float valor){
        montaSqlUpdateSoma(codigoBanco, valor);
        conexao.executaSqlUpdate(SQL_UpdateSoma);
        preencherTabelaBanco(SQL_tabelaBanco);
    }
    
    public boolean verificaData(Date dataVerificar){
        String dia, dia2;
        String mes, mes2;
        String ano, ano2;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); 
	Date date = new Date(); 
        String dataAtual = dateFormat.format(date);
        String dataAVerificar = dateFormat.format(dataVerificar);
        dia = dataAtual.substring(0, 2);
        mes = dataAtual.substring(3, 5);
        ano = dataAtual.substring(6);
        dia2 = dataAVerificar.substring(0, 2);
        mes2 = dataAVerificar.substring(3, 5);
        ano2 = dataAVerificar.substring(6);

        if(Integer.parseInt(ano2) >= Integer.parseInt(ano)){
            if(Integer.parseInt(ano2) > Integer.parseInt(ano)){
                return true;
            }
            if(Integer.parseInt(mes2) >= Integer.parseInt(mes)){
                if((Integer.parseInt(ano2) <= Integer.parseInt(ano)) && (Integer.parseInt(mes2) > Integer.parseInt(mes))){
                    return true;
                }
                if(Integer.parseInt(dia2) > Integer.parseInt(dia)){
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        MenuLogo = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jFormattedTextFieldDtReceita = new javax.swing.JFormattedTextField();
        jLabel1 = new javax.swing.JLabel();
        jButtonExcluirReceita = new javax.swing.JButton();
        jButtonAlterarReceita = new javax.swing.JButton();
        jTextFieldVlReceita = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldCodEmpresa = new javax.swing.JTextField();
        jTextFieldDsEmpresa = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        jTextFieldCodBanco = new javax.swing.JTextField();
        jTextFieldDsBanco = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        jTextFieldDsReceita = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableReceita = new javax.swing.JTable();
        jButtonSalvarReceita = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jFormattedTextFieldDtDespesa = new javax.swing.JFormattedTextField();
        jLabel8 = new javax.swing.JLabel();
        jButtonExcluirDesp = new javax.swing.JButton();
        jButtonAlterarDesp = new javax.swing.JButton();
        jTextFieldVlDesp = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jComboBoxStDesp = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldCdCCustoDesp = new javax.swing.JTextField();
        jTextFieldDsCCuscoDesp = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jTextFieldDsDespCCustoDesp = new javax.swing.JTextField();
        jTextFieldCdDespCCusto = new javax.swing.JTextField();
        jTextFieldCdBancoDesp = new javax.swing.JTextField();
        jTextFieldDsBancoDesp = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableDespesa = new javax.swing.JTable();
        jButtonSalvarDespesa = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jFormattedTextFieldDataCCusto = new javax.swing.JFormattedTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextFieldDS_CentroCusto = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jButtonAlterarCCusto = new javax.swing.JButton();
        jButtonExcluirCCusto = new javax.swing.JButton();
        jPanel20 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableCentroCusto = new javax.swing.JTable();
        jButtonSalvarCCusto = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jFormattedTextFieldDataDespCCusto = new javax.swing.JFormattedTextField();
        jLabel14 = new javax.swing.JLabel();
        jTextFieldDsDespCCusto = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jButtonAlterarDespCCusto = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jButtonExcluirDespCCusto = new javax.swing.JButton();
        jTextFieldCodFuncDespCCusto = new javax.swing.JTextField();
        jTextFieldNomeFuncDespCCusto = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldDescVeicDespCCusto = new javax.swing.JTextField();
        jTextFieldCodVeicDespCCusto = new javax.swing.JTextField();
        jTextFieldCdCCusto = new javax.swing.JTextField();
        jTextFieldDsCCusto = new javax.swing.JTextField();
        jCheckBoxFuncDespCCusto = new javax.swing.JCheckBox();
        jCheckBoxVeicDespCCusto = new javax.swing.JCheckBox();
        jPanel23 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableDespesaCCusto = new javax.swing.JTable();
        jButtonSalvarDespCCusto = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jTextFieldNomeFuncionario = new javax.swing.JFormattedTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jButtonAlterarFuncionario = new javax.swing.JButton();
        jButtonExcluirFuncionario = new javax.swing.JButton();
        jFormattedTextFieldRGFuncionario = new javax.swing.JFormattedTextField();
        jFormattedTextFieldCPFuncionario = new javax.swing.JFormattedTextField();
        jFormattedTextFieldDT_NacFunc = new javax.swing.JFormattedTextField();
        jFormattedTextFieldDT_AdmiFuncionario = new javax.swing.JFormattedTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jFormattedTextFieldTelFunc = new javax.swing.JFormattedTextField();
        jFormattedTextFieldCargoFunc = new javax.swing.JFormattedTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jFormattedTextFieldSalarioFunc = new javax.swing.JFormattedTextField();
        jPanel26 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTableFuncionario = new javax.swing.JTable();
        jButtonSalvarFuncionario = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jButtonAlterarCarro = new javax.swing.JButton();
        jButtonExcluirCarro = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jFormattedTextFieldDtVencDocCarro = new javax.swing.JFormattedTextField();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jComboBoxTpCombustivel = new javax.swing.JComboBox();
        jTextFieldNrPlacaCarro = new javax.swing.JTextField();
        jTextFieldMarcaCarro = new javax.swing.JTextField();
        jTextFieldModeloCarro = new javax.swing.JTextField();
        jTextFieldAnoCarro = new javax.swing.JTextField();
        jTextFieldNrDocCarro = new javax.swing.JTextField();
        jPanel29 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTableCarro = new javax.swing.JTable();
        jButtonSalvarCarro = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        jPanel32 = new javax.swing.JPanel();
        jFormattedTextFieldDS_BANCO = new javax.swing.JFormattedTextField();
        jLabel33 = new javax.swing.JLabel();
        jTextFieldNR_AGENCIA = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        jButtonAlterarBanco = new javax.swing.JButton();
        jTextFieldNR_CONTA = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        jTextFieldVL_SALDO = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        jButtonExcluiBanco = new javax.swing.JButton();
        jPanel33 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTableBANCO = new javax.swing.JTable();
        jButtonSalvarBanco = new javax.swing.JButton();
        jButtonTransferenciaBancaria = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jFormattedDataIni1 = new javax.swing.JFormattedTextField();
        jFormattedFim1 = new javax.swing.JFormattedTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jButtonGerarRel1 = new javax.swing.JButton();
        jPanel34 = new javax.swing.JPanel();
        jFormattedDataIni2 = new javax.swing.JFormattedTextField();
        jFormattedDataFim2 = new javax.swing.JFormattedTextField();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jButtonGerarRel21 = new javax.swing.JButton();
        jButtonGerarRel22 = new javax.swing.JButton();
        jPanel35 = new javax.swing.JPanel();
        jFormattedDataIni3 = new javax.swing.JFormattedTextField();
        jFormattedDataFim3 = new javax.swing.JFormattedTextField();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jTextFieldCdCCustoRel1 = new javax.swing.JTextField();
        jTextFieldDsCCustoRel1 = new javax.swing.JTextField();
        jButtonGerarRel3 = new javax.swing.JButton();

        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Fluxo de Caixa");
        setToolTipText("");

        MenuLogo.setBackground(new java.awt.Color(47, 79, 79));
        MenuLogo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        MenuLogo.setToolTipText("");
        MenuLogo.setPreferredSize(new java.awt.Dimension(2, 82));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Fluxo de Caixa");

        javax.swing.GroupLayout MenuLogoLayout = new javax.swing.GroupLayout(MenuLogo);
        MenuLogo.setLayout(MenuLogoLayout);
        MenuLogoLayout.setHorizontalGroup(
            MenuLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MenuLogoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 497, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(380, 380, 380))
        );
        MenuLogoLayout.setVerticalGroup(
            MenuLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
        );

        jPanel2.setBackground(new java.awt.Color(58, 65, 84));
        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jPanel2.setToolTipText("");

        jButton1.setText("Saída");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton1MouseExited(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Cadastrar um Centro de Custo");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton2MouseExited(evt);
            }
        });
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Entrada");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton3MouseExited(evt);
            }
        });
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Cadastrar Funcionários");
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton4MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton4MouseExited(evt);
            }
        });
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Cadastrar Carros");
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton5MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton5MouseExited(evt);
            }
        });
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Cadastrar Banco");
        jButton6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton6MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton6MouseExited(evt);
            }
        });
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("Filtros de Relatórios");
        jButton7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton7MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton7MouseExited(evt);
            }
        });
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText("Relatórios");
        jButton8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton8MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton8MouseExited(evt);
            }
        });
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setText("Cadastrar uma depesa por Centro de Custo");
        jButton9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton9MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton9MouseExited(evt);
            }
        });
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
            .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
            .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
            .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
            .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
            .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.setPreferredSize(new java.awt.Dimension(944, 490));

        jPanel12.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Inserir Receita", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        try {
            jFormattedTextFieldDtReceita.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel1.setText("Data da Receita:");

        jButtonExcluirReceita.setText("Excluir");
        jButtonExcluirReceita.setEnabled(false);
        jButtonExcluirReceita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExcluirReceitaActionPerformed(evt);
            }
        });

        jButtonAlterarReceita.setText("Alterar");
        jButtonAlterarReceita.setEnabled(false);
        jButtonAlterarReceita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAlterarReceitaActionPerformed(evt);
            }
        });

        jLabel7.setText("Valor:");

        jTextFieldCodEmpresa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldCodEmpresaKeyPressed(evt);
            }
        });

        jTextFieldDsEmpresa.setEditable(false);
        jTextFieldDsEmpresa.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldDsEmpresaFocusGained(evt);
            }
        });

        jLabel38.setText("Codigo:     Empresa:");

        jTextFieldCodBanco.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldCodBancoKeyPressed(evt);
            }
        });

        jTextFieldDsBanco.setEditable(false);
        jTextFieldDsBanco.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldDsBancoFocusGained(evt);
            }
        });

        jLabel39.setText("Codigo:     Banco:");

        jLabel2.setText("Descrição da Receita:");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jTextFieldDsReceita)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jFormattedTextFieldDtReceita, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldCodEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addGap(48, 48, 48)
                                .addComponent(jTextFieldDsEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldCodBanco, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addGap(48, 48, 48)
                                .addComponent(jTextFieldDsBanco, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldVlReceita, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addComponent(jButtonExcluirReceita, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonAlterarReceita, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldVlReceita, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFormattedTextFieldDtReceita, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel38)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldDsEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldCodEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel39)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldDsBanco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldCodBanco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(3, 3, 3)
                .addComponent(jLabel2)
                .addGap(1, 1, 1)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonAlterarReceita)
                        .addComponent(jButtonExcluirReceita))
                    .addComponent(jTextFieldDsReceita, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Receitas", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jTableReceita.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTableReceita.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableReceitaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableReceita);

        jButtonSalvarReceita.setText("Salvar");
        jButtonSalvarReceita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalvarReceitaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 910, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonSalvarReceita, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonSalvarReceita)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 32, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Receita", jPanel3);

        jPanel13.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Inserir Despesa", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        try {
            jFormattedTextFieldDtDespesa.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel8.setText("Data da Despesa:");

        jButtonExcluirDesp.setText("Excluir");
        jButtonExcluirDesp.setEnabled(false);
        jButtonExcluirDesp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExcluirDespActionPerformed(evt);
            }
        });

        jButtonAlterarDesp.setText("Alterar");
        jButtonAlterarDesp.setEnabled(false);
        jButtonAlterarDesp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAlterarDespActionPerformed(evt);
            }
        });

        jLabel11.setText("Valor:");

        jComboBoxStDesp.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pago", "A Pagar" }));

        jLabel4.setText("Situação:");

        jTextFieldCdCCustoDesp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldCdCCustoDespKeyPressed(evt);
            }
        });

        jTextFieldDsCCuscoDesp.setEditable(false);
        jTextFieldDsCCuscoDesp.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldDsCCuscoDespFocusGained(evt);
            }
        });

        jLabel32.setText("Codigo:     Centro de Custo:");

        jLabel37.setText("Codigo:    Despesa:");

        jTextFieldDsDespCCustoDesp.setEditable(false);
        jTextFieldDsDespCCustoDesp.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldDsDespCCustoDespFocusGained(evt);
            }
        });

        jTextFieldCdDespCCusto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldCdDespCCustoKeyPressed(evt);
            }
        });

        jTextFieldCdBancoDesp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldCdBancoDespKeyPressed(evt);
            }
        });

        jTextFieldDsBancoDesp.setEditable(false);
        jTextFieldDsBancoDesp.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldDsBancoDespFocusGained(evt);
            }
        });

        jLabel40.setText("Codigo:     Banco:");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel8)
                        .addComponent(jFormattedTextFieldDtDespesa, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
                        .addComponent(jComboBoxStDesp, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldCdCCustoDesp, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldCdBancoDesp, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldDsCCuscoDesp, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldDsBancoDesp, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonExcluirDesp, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonAlterarDesp, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldCdDespCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addGap(48, 48, 48)
                                .addComponent(jTextFieldDsDespCCustoDesp, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldVlDesp, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)))))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldVlDesp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel37)
                        .addGap(26, 26, 26)))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldDsCCuscoDesp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldCdCCustoDesp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldDsDespCCustoDesp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldCdDespCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFormattedTextFieldDtDespesa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldDsBancoDesp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonExcluirDesp)
                        .addComponent(jButtonAlterarDesp))
                    .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldCdBancoDesp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboBoxStDesp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Despesas", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jTableDespesa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTableDespesa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableDespesaMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTableDespesa);

        jButtonSalvarDespesa.setText("Salvar");
        jButtonSalvarDespesa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalvarDespesaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 910, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonSalvarDespesa, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonSalvarDespesa)
                .addGap(5, 5, 5))
        );

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Despesa", jPanel4);

        jPanel18.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jPanel19.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Inserir um Centro de Custo", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jFormattedTextFieldDataCCusto.setEditable(false);

        jLabel12.setText("Data:");

        jLabel13.setText("Descrição:");

        jButtonAlterarCCusto.setText("Alterar");
        jButtonAlterarCCusto.setEnabled(false);
        jButtonAlterarCCusto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAlterarCCustoActionPerformed(evt);
            }
        });

        jButtonExcluirCCusto.setText("Excluir");
        jButtonExcluirCCusto.setEnabled(false);
        jButtonExcluirCCusto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExcluirCCustoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jFormattedTextFieldDataCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addComponent(jTextFieldDS_CentroCusto, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonExcluirCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonAlterarCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFormattedTextFieldDataCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldDS_CentroCusto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonAlterarCCusto)
                    .addComponent(jButtonExcluirCCusto))
                .addContainerGap())
        );

        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Centro de Custo", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jTableCentroCusto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTableCentroCusto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableCentroCustoMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTableCentroCusto);

        jButtonSalvarCCusto.setText("Salvar");
        jButtonSalvarCCusto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalvarCCustoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 910, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonSalvarCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonSalvarCCusto))
        );

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Centro de Custo", jPanel5);

        jPanel21.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jPanel22.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Inserir uma Despesa por Centro de Custo", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jFormattedTextFieldDataDespCCusto.setEditable(false);

        jLabel14.setText("Data:");

        jLabel15.setText("Descrição:");

        jButtonAlterarDespCCusto.setText("Alterar");
        jButtonAlterarDespCCusto.setEnabled(false);
        jButtonAlterarDespCCusto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAlterarDespCCustoActionPerformed(evt);
            }
        });

        jLabel16.setText("Codigo:     Centro de Custo:");

        jButtonExcluirDespCCusto.setText("Excluir");
        jButtonExcluirDespCCusto.setEnabled(false);
        jButtonExcluirDespCCusto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExcluirDespCCustoActionPerformed(evt);
            }
        });

        jTextFieldCodFuncDespCCusto.setEditable(false);
        jTextFieldCodFuncDespCCusto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldCodFuncDespCCustoKeyPressed(evt);
            }
        });

        jTextFieldNomeFuncDespCCusto.setEditable(false);
        jTextFieldNomeFuncDespCCusto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldNomeFuncDespCCustoFocusGained(evt);
            }
        });

        jLabel5.setText("Cod.         Veículo: ");

        jLabel6.setText("Cod.         Funcionário: ");

        jTextFieldDescVeicDespCCusto.setEditable(false);
        jTextFieldDescVeicDespCCusto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldDescVeicDespCCustoFocusGained(evt);
            }
        });

        jTextFieldCodVeicDespCCusto.setEditable(false);
        jTextFieldCodVeicDespCCusto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldCodVeicDespCCustoKeyPressed(evt);
            }
        });

        jTextFieldCdCCusto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldCdCCustoKeyPressed(evt);
            }
        });

        jTextFieldDsCCusto.setEditable(false);
        jTextFieldDsCCusto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldDsCCustoFocusGained(evt);
            }
        });

        jCheckBoxFuncDespCCusto.setText("Incluir Despesa de Funcionário");
        jCheckBoxFuncDespCCusto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jCheckBoxFuncDespCCustoMouseClicked(evt);
            }
        });
        jCheckBoxFuncDespCCusto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxFuncDespCCustoActionPerformed(evt);
            }
        });

        jCheckBoxVeicDespCCusto.setText("Incluir Despesa de Veículo");
        jCheckBoxVeicDespCCusto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jCheckBoxVeicDespCCustoMouseClicked(evt);
            }
        });
        jCheckBoxVeicDespCCusto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxVeicDespCCustoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addComponent(jFormattedTextFieldDataDespCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldCdCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel22Layout.createSequentialGroup()
                                .addGap(48, 48, 48)
                                .addComponent(jTextFieldDsCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(50, 50, 50)
                        .addComponent(jTextFieldDsDespCCusto))
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(95, 95, 95)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(83, 83, 83)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBoxFuncDespCCusto)
                            .addComponent(jCheckBoxVeicDespCCusto))
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel22Layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel22Layout.createSequentialGroup()
                                        .addGap(48, 48, 48)
                                        .addComponent(jTextFieldNomeFuncDespCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jTextFieldCodFuncDespCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel22Layout.createSequentialGroup()
                                        .addGap(48, 48, 48)
                                        .addComponent(jTextFieldDescVeicDespCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jTextFieldCodVeicDespCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5)))
                            .addGroup(jPanel22Layout.createSequentialGroup()
                                .addGap(423, 423, 423)
                                .addComponent(jButtonExcluirDespCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(jButtonAlterarDespCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(10, 10, 10))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(jLabel16)
                    .addComponent(jLabel15))
                .addGap(6, 6, 6)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jFormattedTextFieldDataDespCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldCdCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldDsCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldDsDespCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldDescVeicDespCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldCodVeicDespCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldNomeFuncDespCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldCodFuncDespCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonExcluirDespCCusto)
                            .addComponent(jButtonAlterarDespCCusto)))
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jCheckBoxFuncDespCCusto)
                        .addGap(0, 0, 0)
                        .addComponent(jCheckBoxVeicDespCCusto))))
        );

        jPanel23.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Despesas por Centro de Custos", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jTableDespesaCCusto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTableDespesaCCusto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableDespesaCCustoMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTableDespesaCCusto);

        jButtonSalvarDespCCusto.setText("Salvar");
        jButtonSalvarDespCCusto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalvarDespCCustoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonSalvarDespCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonSalvarDespCCusto)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(54, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Despesa C.Custo", jPanel6);

        jPanel24.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jPanel25.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cadastrar um Funcionário", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabel17.setText("Nome:");

        jLabel18.setText("RG:");

        jButtonAlterarFuncionario.setText("Alterar");
        jButtonAlterarFuncionario.setEnabled(false);
        jButtonAlterarFuncionario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAlterarFuncionarioActionPerformed(evt);
            }
        });

        jButtonExcluirFuncionario.setText("Excluir");
        jButtonExcluirFuncionario.setEnabled(false);
        jButtonExcluirFuncionario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExcluirFuncionarioActionPerformed(evt);
            }
        });

        try {
            jFormattedTextFieldCPFuncionario.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            jFormattedTextFieldDT_NacFunc.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jFormattedTextFieldDT_NacFunc.setText("");

        try {
            jFormattedTextFieldDT_AdmiFuncionario.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jFormattedTextFieldDT_AdmiFuncionario.setText("");
        jFormattedTextFieldDT_AdmiFuncionario.setToolTipText("");

        jLabel19.setText("CPF:");

        jLabel20.setText("Data de Nascimento:");

        jLabel21.setText("Data de Admissão:");

        try {
            jFormattedTextFieldTelFunc.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##)# #### ####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel22.setText("Telefone:");

        jLabel23.setText("Salário:");

        jLabel24.setText("Cargo:");

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldNomeFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jFormattedTextFieldRGFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jFormattedTextFieldCPFuncionario)
                            .addGroup(jPanel25Layout.createSequentialGroup()
                                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jFormattedTextFieldDT_NacFunc, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jFormattedTextFieldDT_AdmiFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21)))
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel25Layout.createSequentialGroup()
                                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(72, 72, 72))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                                .addComponent(jFormattedTextFieldSalarioFunc, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jFormattedTextFieldTelFunc, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel25Layout.createSequentialGroup()
                                .addComponent(jFormattedTextFieldCargoFunc, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(186, 186, 186)
                                .addComponent(jButtonExcluirFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonAlterarFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 17, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel18)
                        .addComponent(jLabel19)
                        .addComponent(jLabel20)
                        .addComponent(jLabel21)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldNomeFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jFormattedTextFieldRGFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jFormattedTextFieldCPFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jFormattedTextFieldDT_NacFunc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jFormattedTextFieldDT_AdmiFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(jLabel23)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFormattedTextFieldTelFunc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jFormattedTextFieldCargoFunc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonExcluirFuncionario)
                    .addComponent(jButtonAlterarFuncionario)
                    .addComponent(jFormattedTextFieldSalarioFunc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel26.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Funcionários", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jTableFuncionario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTableFuncionario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableFuncionarioMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(jTableFuncionario);

        jButtonSalvarFuncionario.setText("Salvar");
        jButtonSalvarFuncionario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalvarFuncionarioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 910, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonSalvarFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonSalvarFuncionario))
        );

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Funcionário", jPanel7);

        jPanel27.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jPanel28.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cadastrar um Veículo", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabel25.setText("Placa:");

        jLabel26.setText("Marca:");

        jButtonAlterarCarro.setText("Alterar");
        jButtonAlterarCarro.setEnabled(false);
        jButtonAlterarCarro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAlterarCarroActionPerformed(evt);
            }
        });

        jButtonExcluirCarro.setText("Excluir");
        jButtonExcluirCarro.setEnabled(false);
        jButtonExcluirCarro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExcluirCarroActionPerformed(evt);
            }
        });

        jLabel27.setText("Modelo:");

        jLabel28.setText("Ano:");

        jLabel29.setText("Tipo de Combustível:");

        try {
            jFormattedTextFieldDtVencDocCarro.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel30.setText("Vencimento do Documento:");

        jLabel31.setText("Documento:");

        jComboBoxTpCombustivel.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Gasolina", "Disel", "Etanol", "Flex" }));

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addComponent(jTextFieldNrDocCarro, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6))
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(26, 26, 26)))
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addComponent(jFormattedTextFieldDtVencDocCarro, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonExcluirCarro, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonAlterarCarro, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldNrPlacaCarro, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldMarcaCarro, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldModeloCarro, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldAnoCarro, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBoxTpCombustivel, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel25)
                        .addComponent(jLabel26)
                        .addComponent(jLabel27))
                    .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel28)
                        .addComponent(jLabel29)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBoxTpCombustivel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldNrPlacaCarro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextFieldMarcaCarro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextFieldModeloCarro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextFieldAnoCarro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(jLabel31))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFormattedTextFieldDtVencDocCarro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonExcluirCarro)
                    .addComponent(jButtonAlterarCarro)
                    .addComponent(jTextFieldNrDocCarro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel29.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Veículos", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jTableCarro.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTableCarro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableCarroMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(jTableCarro);

        jButtonSalvarCarro.setText("Salvar");
        jButtonSalvarCarro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalvarCarroActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 910, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonSalvarCarro, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonSalvarCarro))
        );

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Carro", jPanel8);

        jPanel31.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jPanel32.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Inserir uma conta Bancária", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabel33.setText("Banco:");

        jLabel34.setText("Agência:");

        jButtonAlterarBanco.setText("Alterar");
        jButtonAlterarBanco.setEnabled(false);
        jButtonAlterarBanco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAlterarBancoActionPerformed(evt);
            }
        });

        jLabel35.setText("Número da Conta:");

        jLabel36.setText("Saldo:");

        jButtonExcluiBanco.setText("Excluir");
        jButtonExcluiBanco.setEnabled(false);
        jButtonExcluiBanco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExcluiBancoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonExcluiBanco, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonAlterarBanco, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addGap(272, 272, 272)
                        .addComponent(jLabel34)
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel32Layout.createSequentialGroup()
                .addComponent(jFormattedTextFieldDS_BANCO, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addComponent(jTextFieldNR_AGENCIA, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldNR_CONTA, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel35))
                .addGap(18, 18, 18)
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel36)
                    .addComponent(jTextFieldVL_SALDO, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(jLabel34)
                    .addComponent(jLabel35)
                    .addComponent(jLabel36))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFormattedTextFieldDS_BANCO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldNR_AGENCIA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldNR_CONTA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldVL_SALDO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonAlterarBanco)
                    .addComponent(jButtonExcluiBanco))
                .addGap(0, 0, 0))
        );

        jPanel33.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Bancos", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jTableBANCO.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Banco", "Agência", "Número da Conta", "Saldo"
            }
        ));
        jTableBANCO.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableBANCOMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(jTableBANCO);

        jButtonSalvarBanco.setText("Salvar");
        jButtonSalvarBanco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalvarBancoActionPerformed(evt);
            }
        });

        jButtonTransferenciaBancaria.setText("Transferência");
        jButtonTransferenciaBancaria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTransferenciaBancariaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 910, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel33Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonTransferenciaBancaria, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonSalvarBanco, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSalvarBanco)
                    .addComponent(jButtonTransferenciaBancaria)))
        );

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addComponent(jPanel32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 944, Short.MAX_VALUE)
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 472, Short.MAX_VALUE)
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Banco", jPanel9);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Receitas/Despesas/Totalizadores", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        try {
            jFormattedDataIni1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            jFormattedFim1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel9.setText("Data de início :");

        jLabel10.setText("Até :");

        jButtonGerarRel1.setText("Gerar Relatório");
        jButtonGerarRel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGerarRel1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jFormattedDataIni1, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonGerarRel1, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                    .addComponent(jFormattedFim1))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jFormattedDataIni1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jFormattedFim1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonGerarRel1)
                .addContainerGap())
        );

        jPanel34.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Receitas/Totalizadores Por Empresa", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        try {
            jFormattedDataIni2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            jFormattedDataFim2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel41.setText("Data de início :");

        jLabel42.setText("Até :");

        jButtonGerarRel21.setText("Gerar Relatório Parte 1");
        jButtonGerarRel21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGerarRel21ActionPerformed(evt);
            }
        });

        jButtonGerarRel22.setText("Gerar Relatório Parte 2");
        jButtonGerarRel22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGerarRel22ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel41)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFormattedDataIni2, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFormattedDataFim2, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addGap(110, 110, 110)
                        .addComponent(jButtonGerarRel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonGerarRel22)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(jFormattedDataIni2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42)
                    .addComponent(jFormattedDataFim2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonGerarRel21)
                    .addComponent(jButtonGerarRel22))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel35.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Despesas Por Centro de Custo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        try {
            jFormattedDataIni3.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            jFormattedDataFim3.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel43.setText("Data de início :");

        jLabel44.setText("Até :");

        jLabel45.setText("Codigo:     Centro de Custo:");

        jTextFieldCdCCustoRel1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldCdCCustoRel1KeyPressed(evt);
            }
        });

        jTextFieldDsCCustoRel1.setEditable(false);
        jTextFieldDsCCustoRel1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldDsCCustoRel1FocusGained(evt);
            }
        });

        jButtonGerarRel3.setText("Gerar Relatório");
        jButtonGerarRel3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGerarRel3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel43)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jFormattedDataIni3, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jFormattedDataFim3, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel35Layout.createSequentialGroup()
                        .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldCdCCustoRel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel35Layout.createSequentialGroup()
                                .addGap(48, 48, 48)
                                .addComponent(jTextFieldDsCCustoRel1, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonGerarRel3, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addComponent(jLabel45)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jFormattedDataFim3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jFormattedDataIni3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel43)
                        .addComponent(jLabel44))
                    .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldDsCCustoRel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonGerarRel3))
                    .addComponent(jTextFieldCdCCustoRel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(283, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Relatórios", jPanel11);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1))
            .addComponent(MenuLogo, javax.swing.GroupLayout.DEFAULT_SIZE, 1224, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(MenuLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        this.jTabbedPane1.setSelectedIndex(0);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.jTabbedPane1.setSelectedIndex(2);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        this.jTabbedPane1.setSelectedIndex(3);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        this.jTabbedPane1.setSelectedIndex(4);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        this.jTabbedPane1.setSelectedIndex(5);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        this.jTabbedPane1.setSelectedIndex(6);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        this.jTabbedPane1.setSelectedIndex(7);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        this.jTabbedPane1.setSelectedIndex(7);
    }//GEN-LAST:event_jButton8ActionPerformed
/********************************************************************************
***************************Aba Bancos********************************************  
*********************************************************************************/  
    private void jButtonSalvarBancoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalvarBancoActionPerformed
        if(verifica_campos_vazio_banco() == true){
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        }else{
            ModeloBanco mod = new ModeloBanco();
            ControleBanco controleBanco = new ControleBanco();
            mod.setBanco(jFormattedTextFieldDS_BANCO.getText());
            mod.setAgencia(Integer.parseInt(jTextFieldNR_AGENCIA.getText()));
            mod.setConta(Integer.parseInt(jTextFieldNR_CONTA.getText()));
            mod.setSaldo(Float.parseFloat(jTextFieldVL_SALDO.getText().replaceAll(",", ".")));
            controleBanco.grava_banco(mod);
            preencherTabelaBanco(SQL_tabelaBanco);
            limpar_camposBanco();
        }
    }//GEN-LAST:event_jButtonSalvarBancoActionPerformed

    private void jTableBANCOMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableBANCOMouseClicked
        String codigo = ""+jTableBANCO.getValueAt(jTableBANCO.getSelectedRow(), 0);
        codigoBanco = Integer.parseInt(codigo);
        String banco = ""+jTableBANCO.getValueAt(jTableBANCO.getSelectedRow(), 2);
        String saldo = ""+ jTableBANCO.getValueAt(jTableBANCO.getSelectedRow(), 4);
        jFormattedTextFieldDS_BANCO.setText((String) jTableBANCO.getValueAt(jTableBANCO.getSelectedRow(), 1));
        jTextFieldNR_AGENCIA.setText(banco);
        jTextFieldNR_CONTA.setText((String) jTableBANCO.getValueAt(jTableBANCO.getSelectedRow(), 3));
        jTextFieldVL_SALDO.setText(saldo);
        habilitar_campo_banco(true);
    }//GEN-LAST:event_jTableBANCOMouseClicked

    private void jButtonAlterarBancoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAlterarBancoActionPerformed
        if(verifica_campos_vazio_banco()== true){
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        }else{
            ModeloBanco mod = new ModeloBanco();
            ControleBanco controleBanco = new ControleBanco();
            mod.setBanco(jFormattedTextFieldDS_BANCO.getText());
            mod.setAgencia(Integer.parseInt(jTextFieldNR_AGENCIA.getText()));
            mod.setConta(Integer.parseInt(jTextFieldNR_CONTA.getText()));
            mod.setSaldo(Float.parseFloat(jTextFieldVL_SALDO.getText().replaceAll(",", ".")));
            mod.setCodigo(codigoBanco);
            controleBanco.altera_banco(mod);
            preencherTabelaBanco(SQL_tabelaBanco);
            limpar_camposBanco();
            habilitar_campo_banco(false);
        }
    }//GEN-LAST:event_jButtonAlterarBancoActionPerformed
/********************************************************************************
***************************Fim Aba Bancos****************************************
*********************************************************************************/
    
/********************************************************************************
***************************Aba Centro de Custo***********************************
*********************************************************************************/
    private void jButtonSalvarCCustoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalvarCCustoActionPerformed
        if(verifica_campos_vazio_ccusto() == true){
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        }else{
            ModeloCCusto mod = new ModeloCCusto();
            ControleCCusto controleCCusto = new ControleCCusto();
            mod.setDescricao(jTextFieldDS_CentroCusto.getText());
            controleCCusto.grava_ccusto(mod);
            preencherTabelaCCusto(SQL_tabelaCCusto);
            limpar_camposCCusto();
        }
    }//GEN-LAST:event_jButtonSalvarCCustoActionPerformed

    private void jTableCentroCustoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableCentroCustoMouseClicked
        String codigo = ""+jTableCentroCusto.getValueAt(jTableCentroCusto.getSelectedRow(), 0);
        codigoCCusto = Integer.parseInt(codigo);
        jTextFieldDS_CentroCusto.setText((String) jTableCentroCusto.getValueAt(jTableCentroCusto.getSelectedRow(), 1));
        habilitar_campo_ccusto(true);
    }//GEN-LAST:event_jTableCentroCustoMouseClicked

    private void jButtonAlterarCCustoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAlterarCCustoActionPerformed
        if(verifica_campos_vazio_ccusto() == true){
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        }else{
            ModeloCCusto mod = new ModeloCCusto();
            ControleCCusto controleCCusto = new ControleCCusto();
            mod.setDescricao(jTextFieldDS_CentroCusto.getText());
            mod.setCodigo(codigoCCusto);
            controleCCusto.altera_ccusto(mod);
            preencherTabelaCCusto(SQL_tabelaCCusto);
            limpar_camposCCusto();
            habilitar_campo_ccusto(false);
        }
    }//GEN-LAST:event_jButtonAlterarCCustoActionPerformed

    private void jButtonExcluirCCustoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExcluirCCustoActionPerformed
        ModeloCCusto mod = new ModeloCCusto();
        ControleCCusto controleCCusto = new ControleCCusto();
        mod.setCodigo(codigoCCusto);
        controleCCusto.exclui_ccusto(mod);
        preencherTabelaCCusto(SQL_tabelaCCusto);
        limpar_camposCCusto();
        habilitar_campo_ccusto(false);
    }//GEN-LAST:event_jButtonExcluirCCustoActionPerformed
/********************************************************************************
***************************Fim Aba Centro de Custo*******************************
*********************************************************************************/
    
/********************************************************************************
*******************************Aba Funcionario***********************************
*********************************************************************************/
    private void jButtonSalvarFuncionarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalvarFuncionarioActionPerformed
        String dia;
        String mes;
        String ano;
        String data;

        if(verifica_campos_vazio_funcionario()== true){
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        }else{
            ModeloFuncionario mod = new ModeloFuncionario();
            ControleFuncionario controleFuncionario = new ControleFuncionario();
            mod.setNome(jTextFieldNomeFuncionario.getText());
            mod.setCpf(Long.parseLong(jFormattedTextFieldCPFuncionario.getText().replaceAll("[.-]", "")));
            mod.setRg(Long.parseLong(jFormattedTextFieldRGFuncionario.getText()));
            
            dia = jFormattedTextFieldDT_NacFunc.getText().substring(0, 2);
            mes = jFormattedTextFieldDT_NacFunc.getText().substring(3, 5);
            ano = jFormattedTextFieldDT_NacFunc.getText().substring(6);
            data = ano+"-"+mes+"-"+dia;
            java.sql.Date dtNasc = java.sql.Date.valueOf(data);
            
            mod.setDataNascimento(dtNasc);
            
            dia = jFormattedTextFieldDT_AdmiFuncionario.getText().substring(0, 2);
            mes = jFormattedTextFieldDT_AdmiFuncionario.getText().substring(3, 5);
            ano = jFormattedTextFieldDT_AdmiFuncionario.getText().substring(6);
            data = ano+"-"+mes+"-"+dia;
            java.sql.Date dtAdmi = java.sql.Date.valueOf(data);
            
            mod.setDataAdimissao(dtAdmi);
            
            mod.setSalario(Float.parseFloat(jFormattedTextFieldSalarioFunc.getText().replaceAll(",", ".")));
            mod.setTelefone(Long.parseLong(jFormattedTextFieldTelFunc.getText().replaceAll("[() ]", "")));
            mod.setCargo(jFormattedTextFieldCargoFunc.getText());
            controleFuncionario.grava_funcionario(mod);
            try {
                preencherTabelaFuncionario(SQL_tabelaFuncionario);
            } catch (ParseException ex) {
                Logger.getLogger(SISTEMA.class.getName()).log(Level.SEVERE, null, ex);
            }
            limpar_camposFuncionario();
        }
    }//GEN-LAST:event_jButtonSalvarFuncionarioActionPerformed

    private void jTableFuncionarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableFuncionarioMouseClicked
        String dia;
        String mes;
        String ano;
        String data;
  
        codigoFuncionario = Integer.parseInt(""+jTableFuncionario.getValueAt(jTableFuncionario.getSelectedRow(), 0));
        jTextFieldNomeFuncionario.setText(""+jTableFuncionario.getValueAt(jTableFuncionario.getSelectedRow(), 1));
        jFormattedTextFieldRGFuncionario.setText(""+jTableFuncionario.getValueAt(jTableFuncionario.getSelectedRow(), 2));
        jFormattedTextFieldCPFuncionario.setText((""+jTableFuncionario.getValueAt(jTableFuncionario.getSelectedRow(), 3)).replaceAll("[.-]", ""));
        
        ano = (""+jTableFuncionario.getValueAt(jTableFuncionario.getSelectedRow(), 4)).substring(0, 4);
        mes = (""+jTableFuncionario.getValueAt(jTableFuncionario.getSelectedRow(), 4)).substring(5, 7);
        dia = (""+jTableFuncionario.getValueAt(jTableFuncionario.getSelectedRow(), 4)).substring(8);
        data = dia+""+mes+""+ano;
        jFormattedTextFieldDT_NacFunc.setText(data);
        
        ano = (""+jTableFuncionario.getValueAt(jTableFuncionario.getSelectedRow(), 5)).substring(0, 4);
        mes = (""+jTableFuncionario.getValueAt(jTableFuncionario.getSelectedRow(), 5)).substring(5, 7);
        dia = (""+jTableFuncionario.getValueAt(jTableFuncionario.getSelectedRow(), 5)).substring(8);
        data = dia+""+mes+""+ano;
        jFormattedTextFieldDT_AdmiFuncionario.setText(data);
        
        jFormattedTextFieldSalarioFunc.setText((""+jTableFuncionario.getValueAt(jTableFuncionario.getSelectedRow(), 6)));
        jFormattedTextFieldTelFunc.setText((""+jTableFuncionario.getValueAt(jTableFuncionario.getSelectedRow(), 7)).replaceAll("[() ]", ""));
        jFormattedTextFieldCargoFunc.setText((""+jTableFuncionario.getValueAt(jTableFuncionario.getSelectedRow(), 8)));
        habilitar_campo_funcionario(true);
    }//GEN-LAST:event_jTableFuncionarioMouseClicked

    private void jButtonAlterarFuncionarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAlterarFuncionarioActionPerformed
        String dia;
        String mes;
        String ano;
        String data;
        if(verifica_campos_vazio_funcionario() == true){
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        }else{
            ModeloFuncionario mod = new ModeloFuncionario();
            ControleFuncionario controleFuncionario = new ControleFuncionario();
            mod.setCodigo(codigoFuncionario);
            mod.setNome(jTextFieldNomeFuncionario.getText());
            mod.setCpf(Long.parseLong(jFormattedTextFieldCPFuncionario.getText().replaceAll("[.-]", "")));
            mod.setRg(Long.parseLong(jFormattedTextFieldRGFuncionario.getText()));
            
            dia = jFormattedTextFieldDT_NacFunc.getText().substring(0, 2);
            mes = jFormattedTextFieldDT_NacFunc.getText().substring(3, 5);
            ano = jFormattedTextFieldDT_NacFunc.getText().substring(6);
            data = ano+"-"+mes+"-"+dia;
            java.sql.Date dtNasc = java.sql.Date.valueOf(data);
            
            mod.setDataNascimento(dtNasc);
            
            dia = jFormattedTextFieldDT_AdmiFuncionario.getText().substring(0, 2);
            mes = jFormattedTextFieldDT_AdmiFuncionario.getText().substring(3, 5);
            ano = jFormattedTextFieldDT_AdmiFuncionario.getText().substring(6);
            data = ano+"-"+mes+"-"+dia;
            java.sql.Date dtAdmi = java.sql.Date.valueOf(data);
            
            mod.setDataAdimissao(dtAdmi);
            
            mod.setSalario(Float.parseFloat(jFormattedTextFieldSalarioFunc.getText().replaceAll(",", ".")));
            mod.setTelefone(Long.parseLong(jFormattedTextFieldTelFunc.getText().replaceAll("[() ]", "")));
            mod.setCargo(jFormattedTextFieldCargoFunc.getText());
            controleFuncionario.altera_funcionario(mod);
            try {
                preencherTabelaFuncionario(SQL_tabelaFuncionario);
            } catch (ParseException ex) {
                Logger.getLogger(SISTEMA.class.getName()).log(Level.SEVERE, null, ex);
            }
            limpar_camposFuncionario();
            habilitar_campo_funcionario(false);
        }
    }//GEN-LAST:event_jButtonAlterarFuncionarioActionPerformed

    private void jButtonExcluirFuncionarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExcluirFuncionarioActionPerformed
        ModeloFuncionario mod = new ModeloFuncionario();
        ControleFuncionario controleFuncionario = new ControleFuncionario();
        mod.setCodigo(codigoFuncionario);
        controleFuncionario.exclui_funcionario(mod);
        try {
            preencherTabelaFuncionario(SQL_tabelaFuncionario);
        } catch (ParseException ex) {
            Logger.getLogger(SISTEMA.class.getName()).log(Level.SEVERE, null, ex);
        }
        limpar_camposFuncionario();
        habilitar_campo_funcionario(false);
    }//GEN-LAST:event_jButtonExcluirFuncionarioActionPerformed
/********************************************************************************
***************************Fim Aba Funcionario***********************************
*********************************************************************************/
    
/********************************************************************************
***********************************Aba Carro*************************************
*********************************************************************************/
    private void jButtonSalvarCarroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalvarCarroActionPerformed
        String dia;
        String mes;
        String ano;
        String data;
        if(verifica_campos_vazio_carro() == true){
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        }else{
            ModeloCarro mod = new ModeloCarro();
            ControleCarro controleCarro = new ControleCarro();
            mod.setPlaca(jTextFieldNrPlacaCarro.getText());
            mod.setMarca(jTextFieldMarcaCarro.getText());
            mod.setModelo(jTextFieldModeloCarro.getText());
            mod.setAno(Integer.parseInt(jTextFieldAnoCarro.getText()));
            mod.setCombustivel(jComboBoxTpCombustivel.getSelectedIndex());
            mod.setDocumento(Long.parseLong(jTextFieldNrDocCarro.getText()));
            
            dia = jFormattedTextFieldDtVencDocCarro.getText().substring(0, 2);
            mes = jFormattedTextFieldDtVencDocCarro.getText().substring(3, 5);
            ano = jFormattedTextFieldDtVencDocCarro.getText().substring(6);
            data = ano+"-"+mes+"-"+dia;
            java.sql.Date dtVenc = java.sql.Date.valueOf(data);
            mod.setVencimento(dtVenc);
            
            controleCarro.grava_carro(mod);
            try {
                preencherTabelaCarro(SQL_tabelaCarro);
            } catch (ParseException ex) {
                Logger.getLogger(SISTEMA.class.getName()).log(Level.SEVERE, null, ex);
            }
            limpar_camposCarro();
        }
    }//GEN-LAST:event_jButtonSalvarCarroActionPerformed

    private void jTableCarroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableCarroMouseClicked
        String dia;
        String mes;
        String ano;
        String data;
        String codigo = ""+jTableCarro.getValueAt(jTableCarro.getSelectedRow(), 0);
        codigoCarro = Integer.parseInt(codigo);
        jTextFieldNrPlacaCarro.setText((String) jTableCarro.getValueAt(jTableCarro.getSelectedRow(), 1));
        jTextFieldMarcaCarro.setText((String) jTableCarro.getValueAt(jTableCarro.getSelectedRow(), 2));
        jTextFieldModeloCarro.setText((String) jTableCarro.getValueAt(jTableCarro.getSelectedRow(), 3));
        jTextFieldAnoCarro.setText(""+jTableCarro.getValueAt(jTableCarro.getSelectedRow(), 4));
        
        if(jTableCarro.getValueAt(jTableCarro.getSelectedRow(), 5) == "Gasolina"){
            jComboBoxTpCombustivel.setSelectedIndex(0);
        }else if(jTableCarro.getValueAt(jTableCarro.getSelectedRow(), 5) == "Disel"){
            jComboBoxTpCombustivel.setSelectedIndex(1);
        }else if(jTableCarro.getValueAt(jTableCarro.getSelectedRow(), 5) == "Etanol"){
            jComboBoxTpCombustivel.setSelectedIndex(2);
        }else if(jTableCarro.getValueAt(jTableCarro.getSelectedRow(), 5) == "Flex"){
            jComboBoxTpCombustivel.setSelectedIndex(3);
        }
        
        jTextFieldNrDocCarro.setText(""+jTableCarro.getValueAt(jTableCarro.getSelectedRow(), 6));
        
        ano = (""+jTableCarro.getValueAt(jTableCarro.getSelectedRow(), 7)).substring(0, 4);
        mes = (""+jTableCarro.getValueAt(jTableCarro.getSelectedRow(), 7)).substring(5, 7);
        dia = (""+jTableCarro.getValueAt(jTableCarro.getSelectedRow(), 7)).substring(8);
        data = dia+""+mes+""+ano;
        jFormattedTextFieldDtVencDocCarro.setText(data);
        
        habilitar_campo_carro(true);
    }//GEN-LAST:event_jTableCarroMouseClicked

    private void jButtonAlterarCarroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAlterarCarroActionPerformed
        String dia;
        String mes;
        String ano;
        String data;
        if(verifica_campos_vazio_carro() == true){
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        }else{
            ModeloCarro mod = new ModeloCarro();
            ControleCarro controleCarro = new ControleCarro();
            mod.setPlaca(jTextFieldNrPlacaCarro.getText());
            mod.setMarca(jTextFieldMarcaCarro.getText());
            mod.setModelo(jTextFieldModeloCarro.getText());
            mod.setAno(Integer.parseInt(jTextFieldAnoCarro.getText()));
            mod.setCombustivel(jComboBoxTpCombustivel.getSelectedIndex());
            mod.setDocumento(Long.parseLong(jTextFieldNrDocCarro.getText()));
            
            dia = jFormattedTextFieldDtVencDocCarro.getText().substring(0, 2);
            mes = jFormattedTextFieldDtVencDocCarro.getText().substring(3, 5);
            ano = jFormattedTextFieldDtVencDocCarro.getText().substring(6);
            data = ano+"-"+mes+"-"+dia;
            java.sql.Date dtVenc = java.sql.Date.valueOf(data);
            mod.setVencimento(dtVenc);
            mod.setCodigo(codigoCarro);
            controleCarro.altera_carro(mod);
            try {
                preencherTabelaCarro(SQL_tabelaCarro);
            } catch (ParseException ex) {
                Logger.getLogger(SISTEMA.class.getName()).log(Level.SEVERE, null, ex);
            }
            limpar_camposCarro();
            habilitar_campo_carro(false);
        }
    }//GEN-LAST:event_jButtonAlterarCarroActionPerformed
/********************************************************************************
********************************Fim Aba Carro************************************
*********************************************************************************/
    
/********************************************************************************
********************** Aba Despesa por Centro de Custo***************************
*********************************************************************************/
    private void jButtonSalvarDespCCustoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalvarDespCCustoActionPerformed
        if(verifica_campos_vazio_despesaccusto() == true){
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        }else{
            ModeloDespesaCCusto mod = new ModeloDespesaCCusto();
            ControleDespesaCCusto controleDespesaCCusto = new ControleDespesaCCusto();
            mod.setFkCCusto(Integer.parseInt(jTextFieldCdCCusto.getText()));
            mod.setDescricao(jTextFieldDsDespCCusto.getText());
            if(jCheckBoxFuncDespCCusto.isSelected() == true){    
                mod.setFkFuncionario(Integer.parseInt(jTextFieldCodFuncDespCCusto.getText()));
            }
            if(jCheckBoxVeicDespCCusto.isSelected() == true){
                mod.setFkVeiculo(Integer.parseInt(jTextFieldCodVeicDespCCusto.getText()));
            }
            controleDespesaCCusto.grava_despesaccusto(mod);
            try {
                preencherTabelaDespesaccusto(SQL_tabelaDespCCusto);
            } catch (ParseException ex) {
                Logger.getLogger(SISTEMA.class.getName()).log(Level.SEVERE, null, ex);
            }
            limpar_camposdespesaccusto();
        }
    }//GEN-LAST:event_jButtonSalvarDespCCustoActionPerformed

    private void jTableDespesaCCustoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableDespesaCCustoMouseClicked
        limpar_camposdespesaccusto();
        String codigo = ""+jTableDespesaCCusto.getValueAt(jTableDespesaCCusto.getSelectedRow(), 0);
        codigoDespCCusto = Integer.parseInt(codigo);
        jTextFieldDsDespCCusto.setText((String) jTableDespesaCCusto.getValueAt(jTableDespesaCCusto.getSelectedRow(), 1));
        jTextFieldDsCCusto.setText((String) jTableDespesaCCusto.getValueAt(jTableDespesaCCusto.getSelectedRow(), 2));
        jTextFieldNomeFuncDespCCusto.setText((String) jTableDespesaCCusto.getValueAt(jTableDespesaCCusto.getSelectedRow(), 3));
        jTextFieldDescVeicDespCCusto.setText((String) jTableDespesaCCusto.getValueAt(jTableDespesaCCusto.getSelectedRow(), 4));
        retornaFks(codigoDespCCusto);
        habilitar_campo_despesaccusto(true);
    }//GEN-LAST:event_jTableDespesaCCustoMouseClicked

    private void jButtonAlterarDespCCustoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAlterarDespCCustoActionPerformed
        if(verifica_campos_vazio_despesaccusto() == true){
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        }else{
            ModeloDespesaCCusto mod = new ModeloDespesaCCusto();
            ControleDespesaCCusto controleDespesaCCusto = new ControleDespesaCCusto();
            mod.setCodigo(codigoDespCCusto);
            mod.setFkCCusto(Integer.parseInt(jTextFieldCdCCusto.getText()));
            mod.setDescricao(jTextFieldDsDespCCusto.getText());
            if(jCheckBoxFuncDespCCusto.isSelected() == true){    
                mod.setFkFuncionario(Integer.parseInt(jTextFieldCodFuncDespCCusto.getText()));
            }
            if(jCheckBoxVeicDespCCusto.isSelected() == true){
                mod.setFkVeiculo(Integer.parseInt(jTextFieldCodVeicDespCCusto.getText()));
            }
            controleDespesaCCusto.altera_despesaccusto(mod);
            try {
                preencherTabelaDespesaccusto(SQL_tabelaDespCCusto);
            } catch (ParseException ex) {
                Logger.getLogger(SISTEMA.class.getName()).log(Level.SEVERE, null, ex);
            }
            limpar_camposdespesaccusto();
            habilitar_campo_despesaccusto(false);
        }
    }//GEN-LAST:event_jButtonAlterarDespCCustoActionPerformed

    private void jButtonExcluirDespCCustoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExcluirDespCCustoActionPerformed
        ModeloDespesaCCusto mod = new ModeloDespesaCCusto();
        ControleDespesaCCusto controleDespesaCCusto = new ControleDespesaCCusto();
        mod.setCodigo(codigoDespCCusto);
        controleDespesaCCusto.exclui_despesaccusto(mod);
        try {
            preencherTabelaDespesaccusto(SQL_tabelaDespCCusto);
        } catch (ParseException ex) {
            Logger.getLogger(SISTEMA.class.getName()).log(Level.SEVERE, null, ex);
        }
        limpar_camposdespesaccusto();
        habilitar_campo_despesaccusto(false);
    }//GEN-LAST:event_jButtonExcluirDespCCustoActionPerformed
    
    private void jTextFieldCdCCustoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCdCCustoKeyPressed
        if(evt.getKeyCode() == evt.VK_F9){
            consultaCCusto.setVisible(true);
            jTextFieldCdCCusto.setText(this.consultaCCusto.getCodigo());
            jtDS_CCusto = this.consultaCCusto.getDescricao();
        }
    }//GEN-LAST:event_jTextFieldCdCCustoKeyPressed

    private void jTextFieldDsCCustoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldDsCCustoFocusGained
        jTextFieldDsCCusto.setText(jtDS_CCusto);
    }//GEN-LAST:event_jTextFieldDsCCustoFocusGained

    private void jCheckBoxFuncDespCCustoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCheckBoxFuncDespCCustoMouseClicked
        if (jCheckBoxFuncDespCCusto.isSelected() == true) {
            jTextFieldCodFuncDespCCusto.setEditable(true);
        }else if (jCheckBoxFuncDespCCusto.isSelected() == false){
            jTextFieldCodFuncDespCCusto.setEditable(false);
        }
    }//GEN-LAST:event_jCheckBoxFuncDespCCustoMouseClicked

    private void jCheckBoxFuncDespCCustoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxFuncDespCCustoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBoxFuncDespCCustoActionPerformed

    private void jCheckBoxVeicDespCCustoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCheckBoxVeicDespCCustoMouseClicked
        if (jCheckBoxVeicDespCCusto.isSelected() == true) {
            jTextFieldCodVeicDespCCusto.setEditable(true);
        }else if (jCheckBoxVeicDespCCusto.isSelected() == false){
            jTextFieldCodVeicDespCCusto.setEditable(false);
        }
    }//GEN-LAST:event_jCheckBoxVeicDespCCustoMouseClicked

    private void jCheckBoxVeicDespCCustoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxVeicDespCCustoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBoxVeicDespCCustoActionPerformed

    private void jTextFieldCodFuncDespCCustoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCodFuncDespCCustoKeyPressed
        if(evt.getKeyCode() == evt.VK_F9){
            if (jCheckBoxFuncDespCCusto.isSelected() == true) {
                consultaFuncionario.setVisible(true);
                jTextFieldCodFuncDespCCusto.setText(this.consultaFuncionario.getCodigo());
                jtNM_func = this.consultaFuncionario.getNome();
            }
        }
    }//GEN-LAST:event_jTextFieldCodFuncDespCCustoKeyPressed

    private void jTextFieldNomeFuncDespCCustoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldNomeFuncDespCCustoFocusGained
        jTextFieldNomeFuncDespCCusto.setText(jtNM_func);
    }//GEN-LAST:event_jTextFieldNomeFuncDespCCustoFocusGained

    private void jTextFieldCodVeicDespCCustoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCodVeicDespCCustoKeyPressed
        if(evt.getKeyCode() == evt.VK_F9){
            if (jCheckBoxVeicDespCCusto.isSelected() == true) {
                consultaVeiculo.setVisible(true);
                jTextFieldCodVeicDespCCusto.setText(this.consultaVeiculo.getCodigo());
                jtDS_modelo = this.consultaVeiculo.getDescricao();
            }
        }
    }//GEN-LAST:event_jTextFieldCodVeicDespCCustoKeyPressed

    private void jTextFieldDescVeicDespCCustoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldDescVeicDespCCustoFocusGained
        jTextFieldDescVeicDespCCusto.setText(jtDS_modelo);
    }//GEN-LAST:event_jTextFieldDescVeicDespCCustoFocusGained
/********************************************************************************
**********************Fim Aba Despesa por Centro de Custo************************
*********************************************************************************/
    
/********************************************************************************
**********************Exclui dos dados da Aba Carro******************************
*********************************************************************************/
    private void jButtonExcluirCarroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExcluirCarroActionPerformed
        ModeloCarro mod = new ModeloCarro();
        ControleCarro controleCarro = new ControleCarro();
        mod.setCodigo(codigoCarro);
        controleCarro.exclui_carro(mod);
        try {
            preencherTabelaCarro(SQL_tabelaCarro);
        } catch (ParseException ex) {
            Logger.getLogger(SISTEMA.class.getName()).log(Level.SEVERE, null, ex);
        }
        limpar_camposCarro();
        habilitar_campo_carro(false);
    }//GEN-LAST:event_jButtonExcluirCarroActionPerformed
/********************************************************************************
********************Fim da Exclusão dos dados da Aba Carro***********************
*********************************************************************************/
    
/********************************************************************************
**************************Exclui dos dados da Aba Banco**************************
*********************************************************************************/
    private void jButtonExcluiBancoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExcluiBancoActionPerformed
        ModeloBanco mod = new ModeloBanco();
        ControleBanco controleBanco = new ControleBanco();
        mod.setCodigo(codigoBanco);
        controleBanco.exclui_banco(mod);
        preencherTabelaBanco(SQL_tabelaBanco);
        limpar_camposBanco();
        habilitar_campo_banco(false);
    }//GEN-LAST:event_jButtonExcluiBancoActionPerformed
/********************************************************************************
********************Fim da Exclusão dos dados da Aba Banco***********************
*********************************************************************************/
    
/********************************************************************************
******************************Aba Receitas*********************************
*********************************************************************************/
    private void jButtonSalvarReceitaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalvarReceitaActionPerformed
        String dia;
        String mes;
        String ano;
        String data;
        
        dia = jFormattedTextFieldDtReceita.getText().substring(0, 2);
        mes = jFormattedTextFieldDtReceita.getText().substring(3, 5);
        ano = jFormattedTextFieldDtReceita.getText().substring(6);
        data = ano+"-"+mes+"-"+dia;
        java.sql.Date dtReceita = java.sql.Date.valueOf(data);
        
        if(verifica_campos_vazio_Receita() == true){
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        }else if(verificaData(dtReceita) == true){
            JOptionPane.showMessageDialog(this, "A data não pode ser maior que a data atual. Verifique!");
        }else{
            ModeloReceita mod = new ModeloReceita();
            ControleReceita controleReceita = new ControleReceita();
            
            mod.setData(dtReceita);
            
            mod.setFkEmpresa(Integer.parseInt(jTextFieldCodEmpresa.getText()));
            mod.setFkBanco(Integer.parseInt(jTextFieldCodBanco.getText()));
            mod.setValor(Float.parseFloat(jTextFieldVlReceita.getText().replaceAll(",", ".")));
            mod.setDescricao(jTextFieldDsReceita.getText());
            controleReceita.grava_receita(mod);
            try {
                preencherTabelaReceita(SQL_tabelaReceita);
            } catch (ParseException ex) {
                Logger.getLogger(SISTEMA.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            montaSqlUpdateSoma(Integer.parseInt(jTextFieldCodBanco.getText()), Float.parseFloat(jTextFieldVlReceita.getText().replaceAll(",", ".")));
            conexao.executaSqlUpdate(SQL_UpdateSoma);
            preencherTabelaBanco(SQL_tabelaBanco);
            limpar_camposReceita();
        }
    }//GEN-LAST:event_jButtonSalvarReceitaActionPerformed

    private void jTableReceitaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableReceitaMouseClicked
        String dia;
        String mes;
        String ano;
        String data;
        limpar_camposReceita();
        String codigo = ""+jTableReceita.getValueAt(jTableReceita.getSelectedRow(), 0);
        codigoReceita = Integer.parseInt(codigo);
  
        ano = (""+jTableReceita.getValueAt(jTableReceita.getSelectedRow(), 3)).substring(0, 4);
        mes = (""+jTableReceita.getValueAt(jTableReceita.getSelectedRow(), 3)).substring(5, 7);
        dia = (""+jTableReceita.getValueAt(jTableReceita.getSelectedRow(), 3)).substring(8);
        data = dia+""+mes+""+ano;
        jFormattedTextFieldDtReceita.setText(data);
        
        jTextFieldDsEmpresa.setText((String) jTableReceita.getValueAt(jTableReceita.getSelectedRow(), 4));
        jTextFieldDsBanco.setText((String) jTableReceita.getValueAt(jTableReceita.getSelectedRow(), 5));
        jTextFieldVlReceita.setText((String) jTableReceita.getValueAt(jTableReceita.getSelectedRow(), 1));
        jTextFieldDsReceita.setText((String) jTableReceita.getValueAt(jTableReceita.getSelectedRow(), 2));
        retornaFksReceita(codigoReceita);
        habilitar_campo_Receita(true);
        jButtonSalvarReceita.setEnabled(false);
        valorOriginal = (String) jTableReceita.getValueAt(jTableReceita.getSelectedRow(), 1);
    }//GEN-LAST:event_jTableReceitaMouseClicked

    private void jTextFieldCodEmpresaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCodEmpresaKeyPressed
        if(evt.getKeyCode() == evt.VK_F9){
            consultaEmpresa.setVisible(true);
            jTextFieldCodEmpresa.setText(this.consultaEmpresa.getCodigo());
            jtDS_Empresa = this.consultaEmpresa.getDescricao();
        }
    }//GEN-LAST:event_jTextFieldCodEmpresaKeyPressed

    private void jTextFieldCodBancoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCodBancoKeyPressed
        if(evt.getKeyCode() == evt.VK_F9){
            consultaBanco.setVisible(true);
            jTextFieldCodBanco.setText(this.consultaBanco.getCodigo());
            jtDS_Banco = this.consultaBanco.getDescricao();
        }
    }//GEN-LAST:event_jTextFieldCodBancoKeyPressed

    private void jButtonAlterarReceitaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAlterarReceitaActionPerformed
        String dia;
        String mes;
        String ano;
        String data;
        
        dia = jFormattedTextFieldDtReceita.getText().substring(0, 2);
        mes = jFormattedTextFieldDtReceita.getText().substring(3, 5);
        ano = jFormattedTextFieldDtReceita.getText().substring(6);
        data = ano+"-"+mes+"-"+dia;
        java.sql.Date dtReceita = java.sql.Date.valueOf(data);
        
        if(verifica_campos_vazio_Receita() == true){
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        }else if(verificaData(dtReceita) == true){
            JOptionPane.showMessageDialog(this, "A data não pode ser maior que a data atual. Verifique!");
        }else{
            ModeloReceita mod = new ModeloReceita();
            ControleReceita controleReceita = new ControleReceita();
            mod.setCodigo(codigoReceita);
            
            mod.setData(dtReceita);
            
            mod.setFkEmpresa(Integer.parseInt(jTextFieldCodEmpresa.getText()));
            mod.setFkBanco(Integer.parseInt(jTextFieldCodBanco.getText()));
            mod.setValor(Float.parseFloat(jTextFieldVlReceita.getText().replaceAll(",", ".")));
            mod.setDescricao(jTextFieldDsReceita.getText());
            
            controleReceita.altera_receita(mod);
            try {
                preencherTabelaReceita(SQL_tabelaReceita);
            } catch (ParseException ex) {
                Logger.getLogger(SISTEMA.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            montaSqlUpdateAltera_Receita(Integer.parseInt(jTextFieldCodBanco.getText()), Float.parseFloat(jTextFieldVlReceita.getText().replaceAll(",", ".")));
            conexao.executaSqlUpdate(SQL_UpdateAltera_Receita);
            preencherTabelaBanco(SQL_tabelaBanco);
            limpar_camposReceita();
            jButtonSalvarReceita.setEnabled(true);
            habilitar_campo_Receita(false);
        }
    }//GEN-LAST:event_jButtonAlterarReceitaActionPerformed

    private void jButtonExcluirReceitaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExcluirReceitaActionPerformed
        ModeloReceita mod = new ModeloReceita();
        ControleReceita controleReceita = new ControleReceita();
        mod.setCodigo(codigoReceita);
        controleReceita.exclui_receita(mod);
        try {
            preencherTabelaReceita(SQL_tabelaReceita);
        } catch (ParseException ex) {
            Logger.getLogger(SISTEMA.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        montaSqlUpdateSubtrai(Integer.parseInt(jTextFieldCodBanco.getText()), Float.parseFloat(jTextFieldVlReceita.getText().replaceAll(",", ".")));
        conexao.executaSqlUpdate(SQL_UpdateSubtrai);
        preencherTabelaBanco(SQL_tabelaBanco);

        limpar_camposReceita();
        habilitar_campo_Receita(false);
    }//GEN-LAST:event_jButtonExcluirReceitaActionPerformed

    private void jTextFieldDsEmpresaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldDsEmpresaFocusGained
        jTextFieldDsEmpresa.setText(jtDS_Empresa);
    }//GEN-LAST:event_jTextFieldDsEmpresaFocusGained

    private void jTextFieldDsBancoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldDsBancoFocusGained
        jTextFieldDsBanco.setText(jtDS_Banco);
    }//GEN-LAST:event_jTextFieldDsBancoFocusGained
/********************************************************************************
******************************Fim da Aba Receita*********************************
*********************************************************************************/
    
/********************************************************************************
*************************************Aba Despesa*********************************
*********************************************************************************/
    private void jButtonSalvarDespesaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalvarDespesaActionPerformed
        String dia;
        String mes;
        String ano;
        String data;
        
        dia = jFormattedTextFieldDtDespesa.getText().substring(0, 2);
        mes = jFormattedTextFieldDtDespesa.getText().substring(3, 5);
        ano = jFormattedTextFieldDtDespesa.getText().substring(6);
        data = ano+"-"+mes+"-"+dia;
        java.sql.Date dtDespesa = java.sql.Date.valueOf(data);
        
        if(verifica_campos_vazio_despesa()== true){
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        }else if(verificaData(dtDespesa) == true){
            JOptionPane.showMessageDialog(this, "A data não pode ser maior que a data atual. Verifique!");
        }else{
            ModeloDespesa mod = new ModeloDespesa();
            ControleDespesa controleDespesa = new ControleDespesa();
            
            mod.setData(dtDespesa);
            
            mod.setFkCCusto(Integer.parseInt(jTextFieldCdCCustoDesp.getText()));
            mod.setFkDespCCusto(Integer.parseInt(jTextFieldCdDespCCusto.getText()));
            mod.setValor(Float.parseFloat(jTextFieldVlDesp.getText().replaceAll(",", ".")));
            mod.setSituacao(jComboBoxStDesp.getSelectedIndex());
            if(jComboBoxStDesp.getSelectedIndex() == 0){
                mod.setFkBanco(Integer.parseInt(jTextFieldCdBancoDesp.getText()));
                
                if(verificaSaldo(Integer.parseInt(jTextFieldCdBancoDesp.getText()), Float.parseFloat(jTextFieldVlDesp.getText().replaceAll(",", ".")))){
                    controleDespesa.grava_despesa(mod);
                    montaSqlUpdateSubtrai(Integer.parseInt(jTextFieldCdBancoDesp.getText()), Float.parseFloat(jTextFieldVlDesp.getText().replaceAll(",", ".")));
                    conexao.executaSqlUpdate(SQL_UpdateSubtrai);
                    preencherTabelaBanco(SQL_tabelaBanco);
                    limpar_camposdespesa();
                }
            }else{
                controleDespesa.grava_despesa(mod);
                limpar_camposdespesa();
            }
            
            try {
                preencherTabelaDespesa(SQL_tabelaDespesa);
            } catch (ParseException ex) {
                Logger.getLogger(SISTEMA.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButtonSalvarDespesaActionPerformed

    private void jButtonAlterarDespActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAlterarDespActionPerformed
        String dia;
        String mes;
        String ano;
        String data;
        
        dia = jFormattedTextFieldDtDespesa.getText().substring(0, 2);
        mes = jFormattedTextFieldDtDespesa.getText().substring(3, 5);
        ano = jFormattedTextFieldDtDespesa.getText().substring(6);
        data = ano+"-"+mes+"-"+dia;
        java.sql.Date dtDespesa = java.sql.Date.valueOf(data);
        
        if(verifica_campos_vazio_despesa()== true){
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        }else if(verificaData(dtDespesa) == true){
            JOptionPane.showMessageDialog(this, "A data não pode ser maior que a data atual. Verifique!");
        }else{
            ModeloDespesa mod = new ModeloDespesa();
            ControleDespesa controleDespesa = new ControleDespesa();
            mod.setCodigo(codigoDespesa);
            
            mod.setData(dtDespesa);
            
            mod.setFkCCusto(Integer.parseInt(jTextFieldCdCCustoDesp.getText()));
            mod.setFkDespCCusto(Integer.parseInt(jTextFieldCdDespCCusto.getText()));
            mod.setValor(Float.parseFloat(jTextFieldVlDesp.getText().replaceAll(",", ".")));
            mod.setSituacao(jComboBoxStDesp.getSelectedIndex());
            if(jComboBoxStDesp.getSelectedIndex() == 0){
                mod.setFkBanco(Integer.parseInt(jTextFieldCdBancoDesp.getText()));
                if(verificaSaldoAlteracao(Integer.parseInt(jTextFieldCdBancoDesp.getText()), Float.parseFloat(valorOriginal), Float.parseFloat(jTextFieldVlDesp.getText().replaceAll(",", ".")))){
                    controleDespesa.altera_despesa(mod);
                    float result = (Float.parseFloat(valorOriginal)) - (Float.parseFloat(jTextFieldVlDesp.getText().replaceAll(",", ".")));
                    //soma caso o valor da despesa é menor que a anterior
                    if(result > 0){
                        montaSqlUpdateSoma(Integer.parseInt(jTextFieldCdBancoDesp.getText()), result);
                        conexao.executaSqlUpdate(SQL_UpdateSoma);
                        preencherTabelaBanco(SQL_tabelaBanco);
                    //esse else abaixo, subtrai o saldo do banco, quando estou alterando a despesa de a pagar para pago.
                    }else if(aPagar == true && result == 0){
                        montaSqlUpdateSubtrai(Integer.parseInt(jTextFieldCdBancoDesp.getText()), (Float.parseFloat(jTextFieldVlDesp.getText().replaceAll(",", "."))));
                        conexao.executaSqlUpdate(SQL_UpdateSubtrai);
                        preencherTabelaBanco(SQL_tabelaBanco);
                    }
                    //esse else subtrai o saldo do banco, quando o valor da despesa a ser alterado é maior do que o valor anterior
                    else{
                        montaSqlUpdateSubtrai(Integer.parseInt(jTextFieldCdBancoDesp.getText()), (result*(-1)));
                        conexao.executaSqlUpdate(SQL_UpdateSubtrai);
                        preencherTabelaBanco(SQL_tabelaBanco);
                    }
                    limpar_camposdespesa();
                }
            }else{
                controleDespesa.altera_despesa(mod);
                limpar_camposdespesa();
            }
            
            
            
            try {
                preencherTabelaDespesa(SQL_tabelaDespesa);
            } catch (ParseException ex) {
                Logger.getLogger(SISTEMA.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            habilitar_campo_despesa(false);
            jButtonSalvarDespesa.setEnabled(true);
        }
    }//GEN-LAST:event_jButtonAlterarDespActionPerformed

    private void jTextFieldCdCCustoDespKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCdCCustoDespKeyPressed
        if(evt.getKeyCode() == evt.VK_F9){
            consultaCCusto.setVisible(true);
            jTextFieldCdCCustoDesp.setText(this.consultaCCusto.getCodigo());
            fkCCusto = Integer.parseInt(this.consultaCCusto.getCodigo());
            jtDS_CCusto = this.consultaCCusto.getDescricao();
        }
    }//GEN-LAST:event_jTextFieldCdCCustoDespKeyPressed

    private void jTextFieldCdDespCCustoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCdDespCCustoKeyPressed
        if(evt.getKeyCode() == evt.VK_F9){
            String consulta = "SELECT CD_DESPCCUSTO, DS_DESPCCUSTO FROM DESPESACCUSTO WHERE FK_CCUSTO = "+fkCCusto+"";
            consultaDespCCusto = new ConsultaDespCCusto(new javax.swing.JFrame(), true, consulta);
            consultaDespCCusto.setVisible(true);
            jTextFieldCdDespCCusto.setText(this.consultaDespCCusto.getCodigo());
            jtDS_DespCCusto = this.consultaDespCCusto.getDescricao();
        }
    }//GEN-LAST:event_jTextFieldCdDespCCustoKeyPressed

    private void jTextFieldCdBancoDespKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCdBancoDespKeyPressed
        if(evt.getKeyCode() == evt.VK_F9){
            consultaBanco.setVisible(true);
            jTextFieldCdBancoDesp.setText(this.consultaBanco.getCodigo());
            jtDS_Banco = this.consultaBanco.getDescricao();
        }
    }//GEN-LAST:event_jTextFieldCdBancoDespKeyPressed

    private void jTextFieldDsBancoDespFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldDsBancoDespFocusGained
        jTextFieldDsBancoDesp.setText(jtDS_Banco);
    }//GEN-LAST:event_jTextFieldDsBancoDespFocusGained

    private void jTextFieldDsCCuscoDespFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldDsCCuscoDespFocusGained
        jTextFieldDsCCuscoDesp.setText(jtDS_CCusto);
    }//GEN-LAST:event_jTextFieldDsCCuscoDespFocusGained

    private void jTextFieldDsDespCCustoDespFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldDsDespCCustoDespFocusGained
        jTextFieldDsDespCCustoDesp.setText(jtDS_DespCCusto);
    }//GEN-LAST:event_jTextFieldDsDespCCustoDespFocusGained

    private void jTableDespesaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableDespesaMouseClicked
        String dia;
        String mes;
        String ano;
        String data;
        limpar_camposdespesa();
        String codigo = ""+jTableDespesa.getValueAt(jTableDespesa.getSelectedRow(), 0);
        codigoDespesa = Integer.parseInt(codigo);
  
        ano = (""+jTableDespesa.getValueAt(jTableDespesa.getSelectedRow(), 4)).substring(0, 4);
        mes = (""+jTableDespesa.getValueAt(jTableDespesa.getSelectedRow(), 4)).substring(5, 7);
        dia = (""+jTableDespesa.getValueAt(jTableDespesa.getSelectedRow(), 4)).substring(8);
        data = dia+""+mes+""+ano;
        jFormattedTextFieldDtDespesa.setText(data);
        
        jTextFieldDsCCuscoDesp.setText((String) jTableDespesa.getValueAt(jTableDespesa.getSelectedRow(), 3));
        jTextFieldDsDespCCustoDesp.setText((String) jTableDespesa.getValueAt(jTableDespesa.getSelectedRow(), 2));
        jTextFieldVlDesp.setText((String) jTableDespesa.getValueAt(jTableDespesa.getSelectedRow(), 1));
        jTextFieldDsBancoDesp.setText((String) jTableDespesa.getValueAt(jTableDespesa.getSelectedRow(), 6));
        if((String) jTableDespesa.getValueAt(jTableDespesa.getSelectedRow(), 5) == "Pago"){
            jComboBoxStDesp.setSelectedIndex(0);
        }else if((String) jTableDespesa.getValueAt(jTableDespesa.getSelectedRow(), 5) == "A Pagar"){
            aPagar = true;
            jComboBoxStDesp.setSelectedIndex(1);
        }
        retornaFksDespesa(codigoDespesa);
        habilitar_campo_despesa(true);
        jButtonSalvarDespesa.setEnabled(false);
        valorOriginal = (String) jTableDespesa.getValueAt(jTableDespesa.getSelectedRow(), 1);
    }//GEN-LAST:event_jTableDespesaMouseClicked

    private void jButtonExcluirDespActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExcluirDespActionPerformed
        ModeloDespesa mod = new ModeloDespesa();
        ControleDespesa controleDespesa = new ControleDespesa();
        mod.setCodigo(codigoDespesa);
        if(jComboBoxStDesp.getSelectedIndex() == 0){
            estornaSaldo(Integer.parseInt(jTextFieldCdBancoDesp.getText()), Float.parseFloat(valorOriginal));
            controleDespesa.exclui_despesa(mod);
            limpar_camposdespesa();
        }else{
            controleDespesa.exclui_despesa(mod);
            limpar_camposdespesa();
        }
        try {
            preencherTabelaDespesa(SQL_tabelaDespesa);
        } catch (ParseException ex) {
            Logger.getLogger(SISTEMA.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        /*montaSqlUpdateExclui_Receita(Integer.parseInt(jTextFieldCodBanco.getText()), Float.parseFloat(jTextFieldVlReceita.getText()));
        conexao.executaSqlUpdate(SQL_UpdateExclui_Receita);
        preencherTabelaBanco(SQL_tabelaBanco);*/

        limpar_camposdespesa();
        habilitar_campo_despesa(false);
    }//GEN-LAST:event_jButtonExcluirDespActionPerformed

    private void jButton3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseEntered
        jButton3.setBackground(new Color(0, 0, 205));
    }//GEN-LAST:event_jButton3MouseEntered

    private void jButton3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseExited
        jButton3.setBackground(new Color(240, 240, 240));
    }//GEN-LAST:event_jButton3MouseExited

    private void jButton1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseEntered
       jButton1.setBackground(new Color(0, 0, 205));
    }//GEN-LAST:event_jButton1MouseEntered

    private void jButton1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseExited
        jButton1.setBackground(new Color(240, 240, 240));
    }//GEN-LAST:event_jButton1MouseExited

    private void jButton2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseEntered
        jButton2.setBackground(new Color(0, 0, 205));
    }//GEN-LAST:event_jButton2MouseEntered

    private void jButton2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseExited
        jButton2.setBackground(new Color(240, 240, 240));
    }//GEN-LAST:event_jButton2MouseExited

    private void jButton9MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton9MouseEntered
        jButton9.setBackground(new Color(0, 0, 205));
    }//GEN-LAST:event_jButton9MouseEntered

    private void jButton9MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton9MouseExited
        jButton9.setBackground(new Color(240, 240, 240));
    }//GEN-LAST:event_jButton9MouseExited

    private void jButton4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseEntered
        jButton4.setBackground(new Color(0, 0, 205));
    }//GEN-LAST:event_jButton4MouseEntered

    private void jButton4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseExited
        jButton4.setBackground(new Color(240, 240, 240));
    }//GEN-LAST:event_jButton4MouseExited

    private void jButton5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseEntered
        jButton5.setBackground(new Color(0, 0, 205));
    }//GEN-LAST:event_jButton5MouseEntered

    private void jButton5MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseExited
        jButton5.setBackground(new Color(240, 240, 240));
    }//GEN-LAST:event_jButton5MouseExited

    private void jButton6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton6MouseEntered
        jButton6.setBackground(new Color(0, 0, 205));
    }//GEN-LAST:event_jButton6MouseEntered

    private void jButton6MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton6MouseExited
        jButton6.setBackground(new Color(240, 240, 240));
    }//GEN-LAST:event_jButton6MouseExited

    private void jButton7MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton7MouseEntered
        jButton7.setBackground(new Color(0, 0, 205));
    }//GEN-LAST:event_jButton7MouseEntered

    private void jButton7MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton7MouseExited
        jButton7.setBackground(new Color(240, 240, 240));
    }//GEN-LAST:event_jButton7MouseExited

    private void jButton8MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton8MouseEntered
        jButton8.setBackground(new Color(0, 0, 205));
    }//GEN-LAST:event_jButton8MouseEntered

    private void jButton8MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton8MouseExited
        jButton8.setBackground(new Color(240, 240, 240));
    }//GEN-LAST:event_jButton8MouseExited

    private void jButtonTransferenciaBancariaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTransferenciaBancariaActionPerformed
        TransferenciaBancaria transferencia = new TransferenciaBancaria(new javax.swing.JFrame(), true);
        transferencia.setVisible(true);
        preencherTabelaBanco(SQL_tabelaBanco);
    }//GEN-LAST:event_jButtonTransferenciaBancariaActionPerformed
    
    public boolean verificaCampoRel1(){
        if(jFormattedDataIni1.getText().isEmpty() || jFormattedFim1.getText().isEmpty()){
            return false;
        }else{
            return true;
        }
    }
    
    private void jButtonGerarRel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGerarRel1ActionPerformed
        if(verificaCampoRel1() == false){
            JOptionPane.showMessageDialog(null, "Preencha a data de inicio e fim.");
        }else if(verificaCampoRel1() == true){
            String dia;
            String mes;
            String ano;
            String data;

            dia = jFormattedDataIni1.getText().substring(0, 2);
            mes = jFormattedDataIni1.getText().substring(3, 5);
            ano = jFormattedDataIni1.getText().substring(6);
            data = ano+"-"+mes+"-"+dia;
            java.sql.Date dtInicio = java.sql.Date.valueOf(data);

            dia = jFormattedFim1.getText().substring(0, 2);
            mes = jFormattedFim1.getText().substring(3, 5);
            ano = jFormattedFim1.getText().substring(6);
            data = ano+"-"+mes+"-"+dia;
            java.sql.Date dtFim = java.sql.Date.valueOf(data);

            String SQLReceita = "SELECT DT_RECEITA, DS_RECEITA, VL_RECEITA FROM RECEITA WHERE DT_RECEITA BETWEEN '"+dtInicio+"' AND '"+dtFim+"' ORDER BY DT_RECEITA DESC;";
            String SQLDespesa = "SELECT DT_DESPESA, FK_CCUSTO, VL_DESPESA FROM DESPESA WHERE DT_DESPESA BETWEEN '"+dtInicio+"' AND '"+dtFim+"' ORDER BY DT_DESPESA DESC;";
            Relatorio1 rel1 = new Relatorio1(SQLReceita, SQLDespesa);
            rel1.setVisible(true);
        }
    }//GEN-LAST:event_jButtonGerarRel1ActionPerformed
    
    public boolean verificaCampoRel2(){
        if(jFormattedDataIni2.getText().isEmpty() || jFormattedDataFim2.getText().isEmpty()){
            return false;
        }else{
            return true;
        }
    }
    
    private void jButtonGerarRel21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGerarRel21ActionPerformed
        if(verificaCampoRel2() == false){
            JOptionPane.showMessageDialog(null, "Preencha a data de inicio e fim.");
        }else if(verificaCampoRel2() == true){
            String dia;
            String mes;
            String ano;
            String data;

            dia = jFormattedDataIni2.getText().substring(0, 2);
            mes = jFormattedDataIni2.getText().substring(3, 5);
            ano = jFormattedDataIni2.getText().substring(6);
            data = ano+"-"+mes+"-"+dia;
            java.sql.Date dtInicio = java.sql.Date.valueOf(data);

            dia = jFormattedDataFim2.getText().substring(0, 2);
            mes = jFormattedDataFim2.getText().substring(3, 5);
            ano = jFormattedDataFim2.getText().substring(6);
            data = ano+"-"+mes+"-"+dia;
            java.sql.Date dtFim = java.sql.Date.valueOf(data);

            Relatorio21 rel21 = new Relatorio21(dtInicio, dtFim);
            rel21.setVisible(true);
        }
    }//GEN-LAST:event_jButtonGerarRel21ActionPerformed
    
    private void jButtonGerarRel22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGerarRel22ActionPerformed
        if(verificaCampoRel2() == false){
            JOptionPane.showMessageDialog(null, "Preencha a data de inicio e fim.");
        }else if(verificaCampoRel2() == true){
            String dia;
            String mes;
            String ano;
            String data;

            dia = jFormattedDataIni2.getText().substring(0, 2);
            mes = jFormattedDataIni2.getText().substring(3, 5);
            ano = jFormattedDataIni2.getText().substring(6);
            data = ano+"-"+mes+"-"+dia;
            java.sql.Date dtInicio = java.sql.Date.valueOf(data);

            dia = jFormattedDataFim2.getText().substring(0, 2);
            mes = jFormattedDataFim2.getText().substring(3, 5);
            ano = jFormattedDataFim2.getText().substring(6);
            data = ano+"-"+mes+"-"+dia;
            java.sql.Date dtFim = java.sql.Date.valueOf(data);

            Relatorio22 rel22 = new Relatorio22(dtInicio, dtFim);
            rel22.setVisible(true);
        }
    }//GEN-LAST:event_jButtonGerarRel22ActionPerformed
    
    public boolean verificaCampoRel3(){
        if(jFormattedDataIni3.getText().isEmpty() || jFormattedDataFim3.getText().isEmpty() || jTextFieldCdCCustoRel1.getText().isEmpty()){
            return false;
        }else{
            return true;
        }
    }
    
    private void jButtonGerarRel3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGerarRel3ActionPerformed
        if(verificaCampoRel3() == false){
            JOptionPane.showMessageDialog(null, "Preencha a data de inicio e fim.");
        }else if(verificaCampoRel3() == true){
            String dia;
            String mes;
            String ano;
            String data;

            dia = jFormattedDataIni3.getText().substring(0, 2);
            mes = jFormattedDataIni3.getText().substring(3, 5);
            ano = jFormattedDataIni3.getText().substring(6);
            data = ano+"-"+mes+"-"+dia;
            java.sql.Date dtInicio = java.sql.Date.valueOf(data);

            dia = jFormattedDataFim3.getText().substring(0, 2);
            mes = jFormattedDataFim3.getText().substring(3, 5);
            ano = jFormattedDataFim3.getText().substring(6);
            data = ano+"-"+mes+"-"+dia;
            java.sql.Date dtFim = java.sql.Date.valueOf(data);
            
            int codCCusto = Integer.parseInt(jTextFieldCdCCustoRel1.getText());
            
            Relatorio3 rel3 = new Relatorio3(dtInicio, dtFim, codCCusto);
            rel3.setVisible(true);
        }
    }//GEN-LAST:event_jButtonGerarRel3ActionPerformed

    private void jTextFieldCdCCustoRel1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCdCCustoRel1KeyPressed
        if(evt.getKeyCode() == evt.VK_F9){
            consultaCCusto.setVisible(true);
            jTextFieldCdCCustoRel1.setText(this.consultaCCusto.getCodigo());
            jtDS_CCusto = this.consultaCCusto.getDescricao();
        }
    }//GEN-LAST:event_jTextFieldCdCCustoRel1KeyPressed

    private void jTextFieldDsCCustoRel1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldDsCCustoRel1FocusGained
        jTextFieldDsCCustoRel1.setText(jtDS_CCusto);
    }//GEN-LAST:event_jTextFieldDsCCustoRel1FocusGained

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel MenuLogo;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JButton jButtonAlterarBanco;
    private javax.swing.JButton jButtonAlterarCCusto;
    private javax.swing.JButton jButtonAlterarCarro;
    private javax.swing.JButton jButtonAlterarDesp;
    private javax.swing.JButton jButtonAlterarDespCCusto;
    private javax.swing.JButton jButtonAlterarFuncionario;
    private javax.swing.JButton jButtonAlterarReceita;
    private javax.swing.JButton jButtonExcluiBanco;
    private javax.swing.JButton jButtonExcluirCCusto;
    private javax.swing.JButton jButtonExcluirCarro;
    private javax.swing.JButton jButtonExcluirDesp;
    private javax.swing.JButton jButtonExcluirDespCCusto;
    private javax.swing.JButton jButtonExcluirFuncionario;
    private javax.swing.JButton jButtonExcluirReceita;
    private javax.swing.JButton jButtonGerarRel1;
    private javax.swing.JButton jButtonGerarRel21;
    private javax.swing.JButton jButtonGerarRel22;
    private javax.swing.JButton jButtonGerarRel3;
    private javax.swing.JButton jButtonSalvarBanco;
    private javax.swing.JButton jButtonSalvarCCusto;
    private javax.swing.JButton jButtonSalvarCarro;
    private javax.swing.JButton jButtonSalvarDespCCusto;
    private javax.swing.JButton jButtonSalvarDespesa;
    private javax.swing.JButton jButtonSalvarFuncionario;
    private javax.swing.JButton jButtonSalvarReceita;
    private javax.swing.JButton jButtonTransferenciaBancaria;
    private javax.swing.JCheckBox jCheckBoxFuncDespCCusto;
    private javax.swing.JCheckBox jCheckBoxVeicDespCCusto;
    private javax.swing.JComboBox jComboBoxStDesp;
    private javax.swing.JComboBox jComboBoxTpCombustivel;
    private javax.swing.JFormattedTextField jFormattedDataFim2;
    private javax.swing.JFormattedTextField jFormattedDataFim3;
    private javax.swing.JFormattedTextField jFormattedDataIni1;
    private javax.swing.JFormattedTextField jFormattedDataIni2;
    private javax.swing.JFormattedTextField jFormattedDataIni3;
    private javax.swing.JFormattedTextField jFormattedFim1;
    private javax.swing.JFormattedTextField jFormattedTextFieldCPFuncionario;
    private javax.swing.JFormattedTextField jFormattedTextFieldCargoFunc;
    private javax.swing.JFormattedTextField jFormattedTextFieldDS_BANCO;
    private javax.swing.JFormattedTextField jFormattedTextFieldDT_AdmiFuncionario;
    private javax.swing.JFormattedTextField jFormattedTextFieldDT_NacFunc;
    private javax.swing.JFormattedTextField jFormattedTextFieldDataCCusto;
    private javax.swing.JFormattedTextField jFormattedTextFieldDataDespCCusto;
    private javax.swing.JFormattedTextField jFormattedTextFieldDtDespesa;
    private javax.swing.JFormattedTextField jFormattedTextFieldDtReceita;
    private javax.swing.JFormattedTextField jFormattedTextFieldDtVencDocCarro;
    private javax.swing.JFormattedTextField jFormattedTextFieldRGFuncionario;
    private javax.swing.JFormattedTextField jFormattedTextFieldSalarioFunc;
    private javax.swing.JFormattedTextField jFormattedTextFieldTelFunc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTableBANCO;
    private javax.swing.JTable jTableCarro;
    private javax.swing.JTable jTableCentroCusto;
    private javax.swing.JTable jTableDespesa;
    private javax.swing.JTable jTableDespesaCCusto;
    private javax.swing.JTable jTableFuncionario;
    private javax.swing.JTable jTableReceita;
    private javax.swing.JTextField jTextFieldAnoCarro;
    private javax.swing.JTextField jTextFieldCdBancoDesp;
    private javax.swing.JTextField jTextFieldCdCCusto;
    private javax.swing.JTextField jTextFieldCdCCustoDesp;
    private javax.swing.JTextField jTextFieldCdCCustoRel1;
    private javax.swing.JTextField jTextFieldCdDespCCusto;
    private javax.swing.JTextField jTextFieldCodBanco;
    private javax.swing.JTextField jTextFieldCodEmpresa;
    private javax.swing.JTextField jTextFieldCodFuncDespCCusto;
    private javax.swing.JTextField jTextFieldCodVeicDespCCusto;
    private javax.swing.JTextField jTextFieldDS_CentroCusto;
    private javax.swing.JTextField jTextFieldDescVeicDespCCusto;
    private javax.swing.JTextField jTextFieldDsBanco;
    private javax.swing.JTextField jTextFieldDsBancoDesp;
    private javax.swing.JTextField jTextFieldDsCCuscoDesp;
    private javax.swing.JTextField jTextFieldDsCCusto;
    private javax.swing.JTextField jTextFieldDsCCustoRel1;
    private javax.swing.JTextField jTextFieldDsDespCCusto;
    private javax.swing.JTextField jTextFieldDsDespCCustoDesp;
    private javax.swing.JTextField jTextFieldDsEmpresa;
    private javax.swing.JTextField jTextFieldDsReceita;
    private javax.swing.JTextField jTextFieldMarcaCarro;
    private javax.swing.JTextField jTextFieldModeloCarro;
    private javax.swing.JTextField jTextFieldNR_AGENCIA;
    private javax.swing.JTextField jTextFieldNR_CONTA;
    private javax.swing.JTextField jTextFieldNomeFuncDespCCusto;
    private javax.swing.JFormattedTextField jTextFieldNomeFuncionario;
    private javax.swing.JTextField jTextFieldNrDocCarro;
    private javax.swing.JTextField jTextFieldNrPlacaCarro;
    private javax.swing.JTextField jTextFieldVL_SALDO;
    private javax.swing.JTextField jTextFieldVlDesp;
    private javax.swing.JTextField jTextFieldVlReceita;
    // End of variables declaration//GEN-END:variables
}
