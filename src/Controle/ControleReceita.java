/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import Modelo.ModeloReceita;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author euris
 */
public class ControleReceita {
    ConectaBanco conexaoBanco = new ConectaBanco();
    
    public void grava_receita(ModeloReceita mod){
        conexaoBanco.conecta();
        try {
            PreparedStatement pst = conexaoBanco.conn.prepareStatement("INSERT INTO RECEITA (DS_RECEITA, VL_RECEITA, DT_RECEITA, FK_EMPRESA, FK_BANCO) VALUES(?, ?, ?, ?, ?)");
            pst.setString(1, mod.getDescricao());
            pst.setFloat(2, mod.getValor());
            pst.setDate(3, (Date) mod.getData());
            pst.setInt(4, mod.getFkEmpresa());
            pst.setInt(5, mod.getFkBanco());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Dados armazenados com sucesso");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao gravar dados da Receita. \nErro:"+ ex);
        }
        conexaoBanco.desconecta();
    }
    
    public void altera_receita(ModeloReceita mod){
        conexaoBanco.conecta();
        try {
            PreparedStatement pst = conexaoBanco.conn.prepareStatement("UPDATE RECEITA SET DS_RECEITA = ?, VL_RECEITA = ?, DT_RECEITA = ?, FK_EMPRESA = ?, FK_BANCO = ? WHERE CD_RECEITA = ?;");
            pst.setString(1, mod.getDescricao());
            pst.setFloat(2, mod.getValor());
            pst.setDate(3, (Date) mod.getData());
            pst.setInt(4, mod.getFkEmpresa());
            pst.setInt(5, mod.getFkBanco());
            pst.setFloat(6, mod.getCodigo());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Dados alterados com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao alterar dados da Receita.\nErro:"+ ex);
        }
        conexaoBanco.desconecta();
    }
    
    public void exclui_receita(ModeloReceita mod){
        conexaoBanco.conecta();
        try {
            PreparedStatement pst = conexaoBanco.conn.prepareStatement("DELETE FROM RECEITA WHERE CD_RECEITA = ?;");
            pst.setInt(1, mod.getCodigo());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Dados excluídos com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir dados da Receita.\nErro:"+ ex);
        }
        conexaoBanco.desconecta();
    }
}
