package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoAssuntoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class ProcessoAssuntoPs extends ProcessoAssuntoPsGen {

    private static final long serialVersionUID = -2956982832518596985L;

    public ProcessoAssuntoPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Método que retorna os assuntos de um determinado processo
	 * 
	 * @param processoDt: processo para o qual serão consultados os assuntos
	 * @author msapaula
	 */
	public List consultarAssuntosProcesso(String id_Processo) throws Exception {
		String Sql;
		List listaAssuntos = new ArrayList();
		ProcessoAssuntoDt Dados = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT * FROM PROJUDI.VIEW_PROC_ASSUNTO WHERE ID_PROC = ?";
		ps.adicionarLong(id_Processo);
		Sql += " ORDER BY ASSUNTO";
		try{
			rs1 = consultar(Sql, ps);
			while (rs1.next()) {
				Dados = new ProcessoAssuntoDt();
				Dados.setId(rs1.getString("ID_PROC_ASSUNTO"));
				Dados.setProcessoNumero(rs1.getString("PROC_NUMERO"));
				Dados.setId_Processo(rs1.getString("ID_PROC"));
				Dados.setId_Assunto(rs1.getString("ID_ASSUNTO"));
				Dados.setAssuntoCodigo(rs1.getString("ASSUNTO_CODIGO"));
				Dados.setIsAtivo(rs1.getString("IS_ATIVO"));
				if (rs1.contains("ID_CNJ_ASSUNTO")) {
					Dados.setIdCNJAssunto(rs1.getString("ID_CNJ_ASSUNTO"));
				}				
				if (rs1.getString("DISPOSITIVO_LEGAL") != null)
					Dados.setAssunto(rs1.getString("ASSUNTO") + " - " + rs1.getString("DISPOSITIVO_LEGAL"));
				else 
					Dados.setAssunto(rs1.getString("ASSUNTO"));

				listaAssuntos.add(Dados);
			}
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return listaAssuntos;
	}

	public int excluirAssuntoProcesso(String id_Processo, String id_Assunto, String id_ProcessoParte) throws Exception {
		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProcessoAssuntoexcluir()");

		stSql= "DELETE FROM PROJUDI.PROC_ASSUNTO";
		stSql += " where id_proc = ? and id_assunto = ? and not exists "; 														ps.adicionarLong(id_Processo); ps.adicionarLong(id_Assunto);
		stSql += " ( select 1 from proc_parte pp  inner join proc_parte_assunto ppa on pp.id_proc_parte=ppa.id_proc_parte ";
		stSql += " where pp.id_proc=? and ppa.id_assunto = ? and ppa.id_proc_parte<>?)";								      ps.adicionarLong(id_Processo); ps.adicionarLong(id_Assunto); ps.adicionarLong(id_ProcessoParte);
						
		return executarUpdateDelete(stSql,ps);
		
	}

}