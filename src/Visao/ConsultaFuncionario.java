/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Visao;

import Visao.*;
import Controle.ConectaBanco;
import Controle.ModeloTabela;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

/**
 *
 * @author euris
 */
public class ConsultaFuncionario extends javax.swing.JDialog {
    ConectaBanco conexao = new ConectaBanco();
    
    String SQL_consultaGeneria = "SELECT CD_FUNCIONARIO, NM_FUNCIONARIO FROM FUNCIONARIO";
    
    String codigo;
    String nome;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    
    /**
     * Creates new form ConsultaCCusto
     */
    public ConsultaFuncionario(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        conexao.conecta();
        initComponents();
        preencherTabela(SQL_consultaGeneria);
        //Abrir o frame centralizado
        setLocationRelativeTo(null);
    }
    
    public void preencherTabela(String SQL){
        ArrayList dados = new ArrayList();
        
        String [] Colunas = new String[]{"Código","Descrição"};
    
        conexao.executaSQL(SQL);
        try {
            conexao.rs.first();
            do{
                dados.add(new Object[]{conexao.rs.getString("CD_FUNCIONARIO"),
                                       conexao.rs.getString("NM_FUNCIONARIO")});
            }while(conexao.rs.next());
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao preencher o ArrayList da consulta do Funcionario. \nErro: "+ ex);
        }
       
        ModeloTabela modelo = new ModeloTabela(dados, Colunas);//Monta a tabela
        jTableConsulta.setModel(modelo);
        jTableConsulta.getColumnModel().getColumn(0).setPreferredWidth(50);//Largura da coluna
        jTableConsulta.getColumnModel().getColumn(0).setResizable(false);
        jTableConsulta.getColumnModel().getColumn(1).setPreferredWidth(330);//Largura da coluna
        jTableConsulta.getColumnModel().getColumn(1).setResizable(false);
        jTableConsulta.getTableHeader().setReorderingAllowed(false);// Para a tabela não ser reorganizada
        jTableConsulta.setAutoResizeMode(jTableConsulta.AUTO_RESIZE_OFF);// Parametros de seleção
        jTableConsulta.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// Parametros de seleção
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableConsulta = new javax.swing.JTable();
        jTextFieldConsulta = new javax.swing.JTextField();
        jButtonPesquisar = new javax.swing.JButton();
        jButtonOk = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        MenuLogo.setBackground(new java.awt.Color(47, 79, 79));
        MenuLogo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        MenuLogo.setToolTipText("");
        MenuLogo.setPreferredSize(new java.awt.Dimension(2, 82));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Consulta Funcionários");

        javax.swing.GroupLayout MenuLogoLayout = new javax.swing.GroupLayout(MenuLogo);
        MenuLogo.setLayout(MenuLogoLayout);
        MenuLogoLayout.setHorizontalGroup(
            MenuLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MenuLogoLayout.createSequentialGroup()
                .addGap(97, 97, 97)
                .addComponent(jLabel1)
                .addContainerGap(101, Short.MAX_VALUE))
        );
        MenuLogoLayout.setVerticalGroup(
            MenuLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MenuLogoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jTableConsulta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTableConsulta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableConsultaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableConsulta);

        jButtonPesquisar.setText("Pesquisar");
        jButtonPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPesquisarActionPerformed(evt);
            }
        });

        jButtonOk.setText("Ok");
        jButtonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextFieldConsulta, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(jButtonPesquisar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonOk))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldConsulta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonPesquisar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonOk)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MenuLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MenuLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTableConsultaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableConsultaMouseClicked
        codigo = ""+jTableConsulta.getValueAt(jTableConsulta.getSelectedRow(), 0);
        nome = ""+jTableConsulta.getValueAt(jTableConsulta.getSelectedRow(), 1);
    }//GEN-LAST:event_jTableConsultaMouseClicked

    private void jButtonPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPesquisarActionPerformed
        String SQL_consulta = "SELECT CD_FUNCIONARIO, NM_FUNCIONARIO FROM FUNCIONARIO WHERE NM_FUNCIONARIO LIKE '%"+jTextFieldConsulta.getText()+"%';";
        preencherTabela(SQL_consulta);
    }//GEN-LAST:event_jButtonPesquisarActionPerformed

    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed
        dispose();
    }//GEN-LAST:event_jButtonOkActionPerformed

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
            java.util.logging.Logger.getLogger(ConsultaFuncionario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ConsultaFuncionario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ConsultaFuncionario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ConsultaFuncionario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ConsultaFuncionario dialog = new ConsultaFuncionario(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel MenuLogo;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JButton jButtonPesquisar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableConsulta;
    private javax.swing.JTextField jTextFieldConsulta;
    // End of variables declaration//GEN-END:variables
}
