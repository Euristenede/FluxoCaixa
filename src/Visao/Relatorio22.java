/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Visao;

import Controle.ConectaBanco;
import Controle.ModeloTabela;
import Utilitarios.Utilitarios;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

/**
 *
 * @author euris
 */
public class Relatorio22 extends javax.swing.JFrame {
    ConectaBanco conexao = new ConectaBanco();
    Date dataInicio;
    Date dataFimC;
    
    int FK_TRANSLUK = 0;
    int FK_BUSLOG = 0;
    int FK_NAGA = 0;
    int FK_INTRACARGO = 0;
    int FK_LUZZA = 0;
    int FK_KMS = 0;
    
    float totalReceitaTransluk;
    float totalReceitaBuslog;
    float totalReceitaNaga;
    float totalReceitaIntercargo;
    float totalReceitaLuzza;
    float totalReceitaKms;
    /**
     * Creates new form Relatorio1
     */
    public Relatorio22(Date dataIni, Date dataFim) {
        this.dataInicio = dataIni;
        this.dataFimC = dataFim;
        conexao.conecta();
        Utilitarios u = new Utilitarios();
        u.inserirIcone(this);
        initComponents();
        retornaFks();
        preencherTabelaTransluk();
        preencherTabelaBuslog();
        preencherTabelaNaga();
        preencherTabelaIntercargo();
        preencherTabelaLuzza();
        preencherTabelaKms();
        totalizadores();
    }

    private Relatorio22() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void retornaFks(){
        conexao.executaSQL("SELECT CD_EMPRESA FROM EMPRESA WHERE NM_EMPRESA LIKE '%Transluk%';");
        try {
            conexao.rs.first();
            do{  
                FK_TRANSLUK = conexao.rs.getInt("CD_EMPRESA");
            }while(conexao.rs.next());

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar a FK da Transluk. \nErro: "+ ex);
        }
        conexao.executaSQL("SELECT CD_EMPRESA FROM EMPRESA WHERE NM_EMPRESA LIKE '%Buslog%';");
        try {
            conexao.rs.first();
            do{  
                FK_BUSLOG = conexao.rs.getInt("CD_EMPRESA");
            }while(conexao.rs.next());

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar a FK da Buslog. \nErro: "+ ex);
        }
        conexao.executaSQL("SELECT CD_EMPRESA FROM EMPRESA WHERE NM_EMPRESA LIKE '%Naga%';");
        try {
            conexao.rs.first();
            do{  
                FK_NAGA = conexao.rs.getInt("CD_EMPRESA");
            }while(conexao.rs.next());

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar a FK da Naga. \nErro: "+ ex);
        }
        conexao.executaSQL("SELECT CD_EMPRESA FROM EMPRESA WHERE NM_EMPRESA LIKE '%Intracargo%';");
        try {
            conexao.rs.first();
            do{  
                FK_INTRACARGO = conexao.rs.getInt("CD_EMPRESA");
            }while(conexao.rs.next());

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar a FK da Intracargo. \nErro: "+ ex);
        }
        conexao.executaSQL("SELECT CD_EMPRESA FROM EMPRESA WHERE NM_EMPRESA LIKE '%Luzza%';");
        try {
            conexao.rs.first();
            do{  
                FK_LUZZA = conexao.rs.getInt("CD_EMPRESA");
            }while(conexao.rs.next());

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar a FK da Luzza. \nErro: "+ ex);
        }
        conexao.executaSQL("SELECT CD_EMPRESA FROM EMPRESA WHERE NM_EMPRESA LIKE '%KMs%';");
        try {
            conexao.rs.first();
            do{  
                FK_KMS = conexao.rs.getInt("CD_EMPRESA");
            }while(conexao.rs.next());

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar a FK dos KMs. \nErro: "+ ex);
        }
    }

    public void preencherTabelaTransluk(){
        ArrayList dados = new ArrayList();
        String [] Colunas = new String[]{"Data","Valor"};
    
        conexao.executaSQL("SELECT DT_RECEITA, VL_RECEITA FROM RECEITA WHERE FK_EMPRESA = "+FK_TRANSLUK+" AND DT_RECEITA BETWEEN '"+dataInicio+"' AND '"+dataFimC+"' ORDER BY DT_RECEITA DESC;");
        try {
            conexao.rs.first();
            do{  totalReceitaTransluk = totalReceitaTransluk + conexao.rs.getFloat("VL_RECEITA");
                dados.add(new Object[]{conexao.rs.getDate("DT_RECEITA"),
                                       conexao.rs.getFloat("VL_RECEITA")});
            }while(conexao.rs.next());
            
        } catch (SQLException ex) {
            //JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList da Transluk. \nErro: "+ ex);
        }
       
        ModeloTabela modelo = new ModeloTabela(dados, Colunas);//Monta a tabela
        jTableTransluk.setModel(modelo);
        jTableTransluk.getColumnModel().getColumn(0).setPreferredWidth(100);//Largura da coluna
        jTableTransluk.getColumnModel().getColumn(0).setResizable(false);
        jTableTransluk.getColumnModel().getColumn(1).setPreferredWidth(110);//Largura da coluna
        jTableTransluk.getColumnModel().getColumn(1).setResizable(false);
        jTableTransluk.getTableHeader().setReorderingAllowed(false);// Para a tabela não ser reorganizada
        jTableTransluk.setAutoResizeMode(jTableTransluk.AUTO_RESIZE_OFF);// Parametros de seleção
        jTableTransluk.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);;// Parametros de seleção
    }
    
    public void preencherTabelaBuslog(){
        ArrayList dados = new ArrayList();
        String [] Colunas = new String[]{"Data","Valor"};
    
        conexao.executaSQL("SELECT DT_RECEITA, VL_RECEITA FROM RECEITA WHERE FK_EMPRESA = "+FK_BUSLOG+" AND DT_RECEITA BETWEEN '"+dataInicio+"' AND '"+dataFimC+"' ORDER BY DT_RECEITA DESC;");
        try {
            conexao.rs.first();
            do{  totalReceitaBuslog = totalReceitaBuslog + conexao.rs.getFloat("VL_RECEITA");
                dados.add(new Object[]{conexao.rs.getDate("DT_RECEITA"),
                                       conexao.rs.getFloat("VL_RECEITA")});
            }while(conexao.rs.next());
            
        } catch (SQLException ex) {
            //JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList da Buslog. \nErro: "+ ex);
        }
       
        ModeloTabela modelo = new ModeloTabela(dados, Colunas);//Monta a tabela
        jTableBuslog.setModel(modelo);
        jTableBuslog.getColumnModel().getColumn(0).setPreferredWidth(100);//Largura da coluna
        jTableBuslog.getColumnModel().getColumn(0).setResizable(false);
        jTableBuslog.getColumnModel().getColumn(1).setPreferredWidth(110);//Largura da coluna
        jTableBuslog.getColumnModel().getColumn(1).setResizable(false);
        jTableBuslog.getTableHeader().setReorderingAllowed(false);// Para a tabela não ser reorganizada
        jTableBuslog.setAutoResizeMode(jTableBuslog.AUTO_RESIZE_OFF);// Parametros de seleção
        jTableBuslog.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);;// Parametros de seleção
    }
    
    public void preencherTabelaNaga(){
        ArrayList dados = new ArrayList();
        String [] Colunas = new String[]{"Data","Valor"};
    
        conexao.executaSQL("SELECT DT_RECEITA, VL_RECEITA FROM RECEITA WHERE FK_EMPRESA = "+FK_NAGA+" AND DT_RECEITA BETWEEN '"+dataInicio+"' AND '"+dataFimC+"' ORDER BY DT_RECEITA DESC;");
        try {
            conexao.rs.first();
            do{  totalReceitaNaga = totalReceitaNaga + conexao.rs.getFloat("VL_RECEITA");
                dados.add(new Object[]{conexao.rs.getDate("DT_RECEITA"),
                                       conexao.rs.getFloat("VL_RECEITA")});
            }while(conexao.rs.next());
            
        } catch (SQLException ex) {
            //JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList da Naga. \nErro: "+ ex);
        }
       
        ModeloTabela modelo = new ModeloTabela(dados, Colunas);//Monta a tabela
        jTableNaga.setModel(modelo);
        jTableNaga.getColumnModel().getColumn(0).setPreferredWidth(100);//Largura da coluna
        jTableNaga.getColumnModel().getColumn(0).setResizable(false);
        jTableNaga.getColumnModel().getColumn(1).setPreferredWidth(110);//Largura da coluna
        jTableNaga.getColumnModel().getColumn(1).setResizable(false);
        jTableNaga.getTableHeader().setReorderingAllowed(false);// Para a tabela não ser reorganizada
        jTableNaga.setAutoResizeMode(jTableNaga.AUTO_RESIZE_OFF);// Parametros de seleção
        jTableNaga.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);;// Parametros de seleção
    }
    
    public void preencherTabelaIntercargo(){
        ArrayList dados = new ArrayList();
        String [] Colunas = new String[]{"Data","Valor"};
    
        conexao.executaSQL("SELECT DT_RECEITA, VL_RECEITA FROM RECEITA WHERE FK_EMPRESA = "+FK_INTRACARGO+" AND DT_RECEITA BETWEEN '"+dataInicio+"' AND '"+dataFimC+"' ORDER BY DT_RECEITA DESC;");
        try {
            conexao.rs.first();
            do{  totalReceitaIntercargo = totalReceitaIntercargo + conexao.rs.getFloat("VL_RECEITA");
                dados.add(new Object[]{conexao.rs.getDate("DT_RECEITA"),
                                       conexao.rs.getFloat("VL_RECEITA")});
            }while(conexao.rs.next());
            
        } catch (SQLException ex) {
            //JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList da Intercargo. \nErro: "+ ex);
        }
       
        ModeloTabela modelo = new ModeloTabela(dados, Colunas);//Monta a tabela
        jTableIntracargo.setModel(modelo);
        jTableIntracargo.getColumnModel().getColumn(0).setPreferredWidth(100);//Largura da coluna
        jTableIntracargo.getColumnModel().getColumn(0).setResizable(false);
        jTableIntracargo.getColumnModel().getColumn(1).setPreferredWidth(110);//Largura da coluna
        jTableIntracargo.getColumnModel().getColumn(1).setResizable(false);
        jTableIntracargo.getTableHeader().setReorderingAllowed(false);// Para a tabela não ser reorganizada
        jTableIntracargo.setAutoResizeMode(jTableIntracargo.AUTO_RESIZE_OFF);// Parametros de seleção
        jTableIntracargo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);;// Parametros de seleção
    }
    
    public void preencherTabelaLuzza(){
        ArrayList dados = new ArrayList();
        String [] Colunas = new String[]{"Data","Valor"};
    
        conexao.executaSQL("SELECT DT_RECEITA, VL_RECEITA FROM RECEITA WHERE FK_EMPRESA = "+FK_LUZZA+" AND DT_RECEITA BETWEEN '"+dataInicio+"' AND '"+dataFimC+"' ORDER BY DT_RECEITA DESC;");
        try {
            conexao.rs.first();
            do{  totalReceitaLuzza = totalReceitaLuzza + conexao.rs.getFloat("VL_RECEITA");
                dados.add(new Object[]{conexao.rs.getDate("DT_RECEITA"),
                                       conexao.rs.getFloat("VL_RECEITA")});
            }while(conexao.rs.next());
            
        } catch (SQLException ex) {
            //JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList da Luzza. \nErro: "+ ex);
        }
       
        ModeloTabela modelo = new ModeloTabela(dados, Colunas);//Monta a tabela
        jTableLuzza.setModel(modelo);
        jTableLuzza.getColumnModel().getColumn(0).setPreferredWidth(100);//Largura da coluna
        jTableLuzza.getColumnModel().getColumn(0).setResizable(false);
        jTableLuzza.getColumnModel().getColumn(1).setPreferredWidth(110);//Largura da coluna
        jTableLuzza.getColumnModel().getColumn(1).setResizable(false);
        jTableLuzza.getTableHeader().setReorderingAllowed(false);// Para a tabela não ser reorganizada
        jTableLuzza.setAutoResizeMode(jTableLuzza.AUTO_RESIZE_OFF);// Parametros de seleção
        jTableLuzza.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);;// Parametros de seleção
    }
    
    public void preencherTabelaKms(){
        ArrayList dados = new ArrayList();
        String [] Colunas = new String[]{"Data","Valor"};
    
        conexao.executaSQL("SELECT DT_RECEITA, VL_RECEITA FROM RECEITA WHERE FK_EMPRESA = "+FK_KMS+" AND DT_RECEITA BETWEEN '"+dataInicio+"' AND '"+dataFimC+"' ORDER BY DT_RECEITA DESC;");
        try {
            conexao.rs.first();
            do{  totalReceitaKms = totalReceitaKms + conexao.rs.getFloat("VL_RECEITA");
                dados.add(new Object[]{conexao.rs.getDate("DT_RECEITA"),
                                       conexao.rs.getFloat("VL_RECEITA")});
            }while(conexao.rs.next());
            
        } catch (SQLException ex) {
            //JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList da KMs. \nErro: "+ ex);
        }
       
        ModeloTabela modelo = new ModeloTabela(dados, Colunas);//Monta a tabela
        jTableKms.setModel(modelo);
        jTableKms.getColumnModel().getColumn(0).setPreferredWidth(100);//Largura da coluna
        jTableKms.getColumnModel().getColumn(0).setResizable(false);
        jTableKms.getColumnModel().getColumn(1).setPreferredWidth(110);//Largura da coluna
        jTableKms.getColumnModel().getColumn(1).setResizable(false);
        jTableKms.getTableHeader().setReorderingAllowed(false);// Para a tabela não ser reorganizada
        jTableKms.setAutoResizeMode(jTableKms.AUTO_RESIZE_OFF);// Parametros de seleção
        jTableKms.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);;// Parametros de seleção
    }
   
    public void totalizadores(){
        jTextFieldTotalTransluk.setText(totalReceitaTransluk+"");
        jTextFieldTotalBuslog.setText(totalReceitaBuslog+"");
        jTextFieldTotalNaga.setText(totalReceitaNaga+"");
        jTextFieldTotalIntracargo.setText(totalReceitaIntercargo+"");
        jTextFieldTotalLuzza.setText(totalReceitaLuzza+"");
        jTextFieldTotalKms.setText(totalReceitaKms+"");
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
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableTransluk = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableBuslog = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableNaga = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTableIntracargo = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTableLuzza = new javax.swing.JTable();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTableKms = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldTotalTransluk = new javax.swing.JTextField();
        jTextFieldTotalBuslog = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldTotalNaga = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldTotalIntracargo = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldTotalLuzza = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldTotalKms = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        MenuLogo.setBackground(new java.awt.Color(47, 79, 79));
        MenuLogo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        MenuLogo.setToolTipText("");
        MenuLogo.setPreferredSize(new java.awt.Dimension(2, 82));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("Relatório de Receitas por Empresa Parte 2");

        javax.swing.GroupLayout MenuLogoLayout = new javax.swing.GroupLayout(MenuLogo);
        MenuLogo.setLayout(MenuLogoLayout);
        MenuLogoLayout.setHorizontalGroup(
            MenuLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MenuLogoLayout.createSequentialGroup()
                .addGap(305, 305, 305)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        MenuLogoLayout.setVerticalGroup(
            MenuLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MenuLogoLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel1)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "                      Transluk                                                       Buslog                                                           Naga                                                      Intracargo                                                     Luzza                                                            KMs", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

        jTableTransluk.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(jTableTransluk);

        jPanel2.add(jScrollPane2);

        jTableBuslog.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(jTableBuslog);

        jPanel2.add(jScrollPane3);

        jTableNaga.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane4.setViewportView(jTableNaga);

        jPanel2.add(jScrollPane4);

        jTableIntracargo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane5.setViewportView(jTableIntracargo);

        jPanel2.add(jScrollPane5);

        jTableLuzza.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane6.setViewportView(jTableLuzza);

        jPanel2.add(jScrollPane6);

        jTableKms.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane7.setViewportView(jTableKms);

        jPanel2.add(jScrollPane7);

        jLabel2.setText("Total : ");

        jLabel3.setText("Total : ");

        jLabel4.setText("Total : ");

        jLabel5.setText("Total : ");

        jLabel6.setText("Total : ");

        jLabel7.setText("Total : ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldTotalTransluk, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldTotalBuslog, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldTotalNaga, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldTotalIntracargo, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldTotalLuzza, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldTotalKms, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 1252, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(jTextFieldTotalKms, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(jTextFieldTotalLuzza, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(jTextFieldTotalIntracargo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(jTextFieldTotalNaga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jTextFieldTotalBuslog, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jTextFieldTotalTransluk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(32, 32, 32))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MenuLogo, javax.swing.GroupLayout.DEFAULT_SIZE, 1262, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(MenuLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Relatorio22.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Relatorio22.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Relatorio22.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Relatorio22.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Relatorio22().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel MenuLogo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTable jTableBuslog;
    private javax.swing.JTable jTableIntracargo;
    private javax.swing.JTable jTableKms;
    private javax.swing.JTable jTableLuzza;
    private javax.swing.JTable jTableNaga;
    private javax.swing.JTable jTableTransluk;
    private javax.swing.JTextField jTextFieldTotalBuslog;
    private javax.swing.JTextField jTextFieldTotalIntracargo;
    private javax.swing.JTextField jTextFieldTotalKms;
    private javax.swing.JTextField jTextFieldTotalLuzza;
    private javax.swing.JTextField jTextFieldTotalNaga;
    private javax.swing.JTextField jTextFieldTotalTransluk;
    // End of variables declaration//GEN-END:variables
}
