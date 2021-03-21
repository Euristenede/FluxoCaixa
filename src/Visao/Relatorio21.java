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
public class Relatorio21 extends javax.swing.JFrame {
    ConectaBanco conexao = new ConectaBanco();
    Date dataInicio;
    Date dataFimC;
    
    int FK_PRINCESA = 0;
    int FK_TNT = 0;
    int FK_SULAMERICANA = 0;
    int FK_RODONAVES = 0;
    int FK_SUDOESTE = 0;
    
    float totalReceitaPrincesa;
    float totalReceitaTnt;
    float totalReceitaSulamericana;
    float totalReceitaRodonaves;
    float totalReceitaSudoeste;
    /**
     * Creates new form Relatorio1
     */
    public Relatorio21(Date dataIni, Date dataFim) {
        this.dataInicio = dataIni;
        this.dataFimC = dataFim;
        conexao.conecta();
        Utilitarios u = new Utilitarios();
        u.inserirIcone(this);
        initComponents();
        retornaFks();
        preencherTabelaPrincesa();
        preencherTabelaTnt();
        preencherTabelaSulamericana();
        preencherTabelaRodonaves();
        preencherTabelaSudoeste();
        totalizadores();
    }

    private Relatorio21() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void retornaFks(){
        conexao.executaSQL("SELECT CD_EMPRESA FROM EMPRESA WHERE NM_EMPRESA LIKE '%Princesa dos Campos%';");
        try {
            conexao.rs.first();
            do{  
                FK_PRINCESA = conexao.rs.getInt("CD_EMPRESA");
            }while(conexao.rs.next());

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar a FK Da Princesa dos Campos. \nErro: "+ ex);
        }
        conexao.executaSQL("SELECT CD_EMPRESA FROM EMPRESA WHERE NM_EMPRESA LIKE '%TNT%';");
        try {
            conexao.rs.first();
            do{  
                FK_TNT = conexao.rs.getInt("CD_EMPRESA");
            }while(conexao.rs.next());

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar a FK da TNT. \nErro: "+ ex);
        }
        conexao.executaSQL("SELECT CD_EMPRESA FROM EMPRESA WHERE NM_EMPRESA LIKE '%Sulamericana%';");
        try {
            conexao.rs.first();
            do{  
                FK_SULAMERICANA = conexao.rs.getInt("CD_EMPRESA");
            }while(conexao.rs.next());

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar a FK da Sulamericana. \nErro: "+ ex);
        }
        conexao.executaSQL("SELECT CD_EMPRESA FROM EMPRESA WHERE NM_EMPRESA LIKE '%Rodonaves%';");
        try {
            conexao.rs.first();
            do{  
                FK_RODONAVES = conexao.rs.getInt("CD_EMPRESA");
            }while(conexao.rs.next());

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar a FK da Rodonaves. \nErro: "+ ex);
        }
        conexao.executaSQL("SELECT CD_EMPRESA FROM EMPRESA WHERE NM_EMPRESA LIKE '%Sudoeste%';");
        try {
            conexao.rs.first();
            do{  
                FK_SUDOESTE = conexao.rs.getInt("CD_EMPRESA");
            }while(conexao.rs.next());

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar a FK da Sudoeste. \nErro: "+ ex);
        }
    }

    public void preencherTabelaPrincesa(){
        ArrayList dados = new ArrayList();
        String [] Colunas = new String[]{"Data","Valor"};
    
        conexao.executaSQL("SELECT DT_RECEITA, VL_RECEITA FROM RECEITA WHERE FK_EMPRESA = "+FK_PRINCESA+" AND DT_RECEITA BETWEEN '"+dataInicio+"' AND '"+dataFimC+"' ORDER BY DT_RECEITA DESC;");
        try {
            conexao.rs.first();
            do{  totalReceitaPrincesa = totalReceitaPrincesa + conexao.rs.getFloat("VL_RECEITA");
                dados.add(new Object[]{conexao.rs.getDate("DT_RECEITA"),
                                       conexao.rs.getFloat("VL_RECEITA")});
            }while(conexao.rs.next());
            
        } catch (SQLException ex) {
            //JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList da Princesa dos Campos. \nErro: "+ ex);
        }
       
        ModeloTabela modelo = new ModeloTabela(dados, Colunas);//Monta a tabela
        jTablePrincesa.setModel(modelo);
        jTablePrincesa.getColumnModel().getColumn(0).setPreferredWidth(100);//Largura da coluna
        jTablePrincesa.getColumnModel().getColumn(0).setResizable(false);
        jTablePrincesa.getColumnModel().getColumn(1).setPreferredWidth(110);//Largura da coluna
        jTablePrincesa.getColumnModel().getColumn(1).setResizable(false);
        jTablePrincesa.getTableHeader().setReorderingAllowed(false);// Para a tabela não ser reorganizada
        jTablePrincesa.setAutoResizeMode(jTablePrincesa.AUTO_RESIZE_OFF);// Parametros de seleção
        jTablePrincesa.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);;// Parametros de seleção
    }
    
    public void preencherTabelaTnt(){
        ArrayList dados = new ArrayList();
        String [] Colunas = new String[]{"Data","Valor"};
    
        conexao.executaSQL("SELECT DT_RECEITA, VL_RECEITA FROM RECEITA WHERE FK_EMPRESA = "+FK_TNT+" AND DT_RECEITA BETWEEN '"+dataInicio+"' AND '"+dataFimC+"' ORDER BY DT_RECEITA DESC;");
        try {
            conexao.rs.first();
            do{  totalReceitaTnt = totalReceitaTnt + conexao.rs.getFloat("VL_RECEITA");
                dados.add(new Object[]{conexao.rs.getDate("DT_RECEITA"),
                                       conexao.rs.getFloat("VL_RECEITA")});
            }while(conexao.rs.next());
            
        } catch (SQLException ex) {
            //JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList da TNT. \nErro: "+ ex);
        }
       
        ModeloTabela modelo = new ModeloTabela(dados, Colunas);//Monta a tabela
        jTableTnt.setModel(modelo);
        jTableTnt.getColumnModel().getColumn(0).setPreferredWidth(100);//Largura da coluna
        jTableTnt.getColumnModel().getColumn(0).setResizable(false);
        jTableTnt.getColumnModel().getColumn(1).setPreferredWidth(110);//Largura da coluna
        jTableTnt.getColumnModel().getColumn(1).setResizable(false);
        jTableTnt.getTableHeader().setReorderingAllowed(false);// Para a tabela não ser reorganizada
        jTableTnt.setAutoResizeMode(jTableTnt.AUTO_RESIZE_OFF);// Parametros de seleção
        jTableTnt.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);;// Parametros de seleção
    }
    
    public void preencherTabelaSulamericana(){
        ArrayList dados = new ArrayList();
        String [] Colunas = new String[]{"Data","Valor"};
    
        conexao.executaSQL("SELECT DT_RECEITA, VL_RECEITA FROM RECEITA WHERE FK_EMPRESA = "+FK_SULAMERICANA+" AND DT_RECEITA BETWEEN '"+dataInicio+"' AND '"+dataFimC+"' ORDER BY DT_RECEITA DESC;");
        try {
            conexao.rs.first();
            do{  totalReceitaSulamericana = totalReceitaSulamericana + conexao.rs.getFloat("VL_RECEITA");
                dados.add(new Object[]{conexao.rs.getDate("DT_RECEITA"),
                                       conexao.rs.getFloat("VL_RECEITA")});
            }while(conexao.rs.next());
            
        } catch (SQLException ex) {
            //JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList da Sulamericana. \nErro: "+ ex);
        }
       
        ModeloTabela modelo = new ModeloTabela(dados, Colunas);//Monta a tabela
        jTableSulamericana.setModel(modelo);
        jTableSulamericana.getColumnModel().getColumn(0).setPreferredWidth(100);//Largura da coluna
        jTableSulamericana.getColumnModel().getColumn(0).setResizable(false);
        jTableSulamericana.getColumnModel().getColumn(1).setPreferredWidth(110);//Largura da coluna
        jTableSulamericana.getColumnModel().getColumn(1).setResizable(false);
        jTableSulamericana.getTableHeader().setReorderingAllowed(false);// Para a tabela não ser reorganizada
        jTableSulamericana.setAutoResizeMode(jTableSulamericana.AUTO_RESIZE_OFF);// Parametros de seleção
        jTableSulamericana.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);;// Parametros de seleção
    }
    
    public void preencherTabelaRodonaves(){
        ArrayList dados = new ArrayList();
        String [] Colunas = new String[]{"Data","Valor"};
    
        conexao.executaSQL("SELECT DT_RECEITA, VL_RECEITA FROM RECEITA WHERE FK_EMPRESA = "+FK_RODONAVES+" AND DT_RECEITA BETWEEN '"+dataInicio+"' AND '"+dataFimC+"' ORDER BY DT_RECEITA DESC;");
        try {
            conexao.rs.first();
            do{  totalReceitaRodonaves = totalReceitaRodonaves + conexao.rs.getFloat("VL_RECEITA");
                dados.add(new Object[]{conexao.rs.getDate("DT_RECEITA"),
                                       conexao.rs.getFloat("VL_RECEITA")});
            }while(conexao.rs.next());
            
        } catch (SQLException ex) {
            //JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList da Rodonaves. \nErro: "+ ex);
        }
       
        ModeloTabela modelo = new ModeloTabela(dados, Colunas);//Monta a tabela
        jTableRodonaves.setModel(modelo);
        jTableRodonaves.getColumnModel().getColumn(0).setPreferredWidth(100);//Largura da coluna
        jTableRodonaves.getColumnModel().getColumn(0).setResizable(false);
        jTableRodonaves.getColumnModel().getColumn(1).setPreferredWidth(110);//Largura da coluna
        jTableRodonaves.getColumnModel().getColumn(1).setResizable(false);
        jTableRodonaves.getTableHeader().setReorderingAllowed(false);// Para a tabela não ser reorganizada
        jTableRodonaves.setAutoResizeMode(jTableRodonaves.AUTO_RESIZE_OFF);// Parametros de seleção
        jTableRodonaves.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);;// Parametros de seleção
    }
    
    public void preencherTabelaSudoeste(){
        ArrayList dados = new ArrayList();
        String [] Colunas = new String[]{"Data","Valor"};
    
        conexao.executaSQL("SELECT DT_RECEITA, VL_RECEITA FROM RECEITA WHERE FK_EMPRESA = "+FK_SUDOESTE+" AND DT_RECEITA BETWEEN '"+dataInicio+"' AND '"+dataFimC+"' ORDER BY DT_RECEITA DESC;");
        try {
            conexao.rs.first();
            do{  totalReceitaSudoeste = totalReceitaSudoeste + conexao.rs.getFloat("VL_RECEITA");
                dados.add(new Object[]{conexao.rs.getDate("DT_RECEITA"),
                                       conexao.rs.getFloat("VL_RECEITA")});
            }while(conexao.rs.next());
            
        } catch (SQLException ex) {
            //JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList da Sudoeste. \nErro: "+ ex);
        }
       
        ModeloTabela modelo = new ModeloTabela(dados, Colunas);//Monta a tabela
        jTableSudoeste.setModel(modelo);
        jTableSudoeste.getColumnModel().getColumn(0).setPreferredWidth(100);//Largura da coluna
        jTableSudoeste.getColumnModel().getColumn(0).setResizable(false);
        jTableSudoeste.getColumnModel().getColumn(1).setPreferredWidth(110);//Largura da coluna
        jTableSudoeste.getColumnModel().getColumn(1).setResizable(false);
        jTableSudoeste.getTableHeader().setReorderingAllowed(false);// Para a tabela não ser reorganizada
        jTableSudoeste.setAutoResizeMode(jTableSudoeste.AUTO_RESIZE_OFF);// Parametros de seleção
        jTableSudoeste.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);;// Parametros de seleção
    }
   
    public void totalizadores(){
        jTextFieldTotalPrincesa.setText(totalReceitaPrincesa+"");
        jTextFieldTotalTnt.setText(totalReceitaTnt+"");
        jTextFieldTotalSulamericana.setText(totalReceitaSulamericana+"");
        jTextFieldRodonaves.setText(totalReceitaRodonaves+"");
        jTextFieldSudoeste.setText(totalReceitaSudoeste+"");
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
        jTablePrincesa = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableTnt = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableSulamericana = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTableRodonaves = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTableSudoeste = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldTotalPrincesa = new javax.swing.JTextField();
        jTextFieldTotalTnt = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldTotalSulamericana = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldRodonaves = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldSudoeste = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        MenuLogo.setBackground(new java.awt.Color(47, 79, 79));
        MenuLogo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        MenuLogo.setToolTipText("");
        MenuLogo.setPreferredSize(new java.awt.Dimension(2, 82));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("Relatório de Receitas por Empresa Parte 1");

        javax.swing.GroupLayout MenuLogoLayout = new javax.swing.GroupLayout(MenuLogo);
        MenuLogo.setLayout(MenuLogoLayout);
        MenuLogoLayout.setHorizontalGroup(
            MenuLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MenuLogoLayout.createSequentialGroup()
                .addGap(252, 252, 252)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        MenuLogoLayout.setVerticalGroup(
            MenuLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MenuLogoLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "            Princesa dos Campos                                              TNT                                                        Sulamericana                                            Rodonaves                                                 Sudoeste", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

        jTablePrincesa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(jTablePrincesa);

        jPanel2.add(jScrollPane2);

        jTableTnt.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(jTableTnt);

        jPanel2.add(jScrollPane3);

        jTableSulamericana.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane4.setViewportView(jTableSulamericana);

        jPanel2.add(jScrollPane4);

        jTableRodonaves.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane5.setViewportView(jTableRodonaves);

        jPanel2.add(jScrollPane5);

        jTableSudoeste.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane6.setViewportView(jTableSudoeste);

        jPanel2.add(jScrollPane6);

        jLabel2.setText("Total : ");

        jLabel3.setText("Total : ");

        jLabel4.setText("Total : ");

        jLabel5.setText("Total : ");

        jLabel6.setText("Total : ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldTotalPrincesa, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldTotalTnt, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldTotalSulamericana, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldRodonaves, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldSudoeste, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(jTextFieldSudoeste, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(jTextFieldRodonaves, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(jTextFieldTotalSulamericana, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jTextFieldTotalTnt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jTextFieldTotalPrincesa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(32, 32, 32))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MenuLogo, javax.swing.GroupLayout.DEFAULT_SIZE, 1035, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
            java.util.logging.Logger.getLogger(Relatorio21.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Relatorio21.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Relatorio21.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Relatorio21.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Relatorio21().setVisible(true);
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTable jTablePrincesa;
    private javax.swing.JTable jTableRodonaves;
    private javax.swing.JTable jTableSudoeste;
    private javax.swing.JTable jTableSulamericana;
    private javax.swing.JTable jTableTnt;
    private javax.swing.JTextField jTextFieldRodonaves;
    private javax.swing.JTextField jTextFieldSudoeste;
    private javax.swing.JTextField jTextFieldTotalPrincesa;
    private javax.swing.JTextField jTextFieldTotalSulamericana;
    private javax.swing.JTextField jTextFieldTotalTnt;
    // End of variables declaration//GEN-END:variables
}
