package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.RecursoAssuntoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class RecursoAssuntoPs extends RecursoAssuntoPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = -6148673080353534894L;

    public RecursoAssuntoPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Método que retorna os assuntos de um determinado recurso
	 * @param processoDt: recurso para o qual serão consultados os assuntos
	 * @author msapaula
	 */
	public List getAssuntosRecurso(String id_Recurso) throws Exception {
		String Sql;
		List listaAssuntos = new ArrayList();
		RecursoAssuntoDt Dados = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "SELECT * FROM PROJUDI.VIEW_RECURSO_ASSUNTO WHERE ID_RECURSO = ?";
		ps.adicionarLong(id_Recurso);
		Sql += " ORDER BY ASSUNTO";
		try{
			rs1 = consultar(Sql,ps);
			while (rs1.next()) {
				Dados = new RecursoAssuntoDt();
				Dados.setId(rs1.getString("ID_RECURSO_ASSUNTO"));
				Dados.setId_Recurso(rs1.getString("ID_RECURSO"));
				Dados.setId_Assunto(rs1.getString("ID_ASSUNTO"));
				Dados.setIsAtivo(rs1.getString("IS_ATIVO"));
				Dados.setAssunto(rs1.getString("ASSUNTO") + " - " + rs1.getString("DISPOSITIVO_LEGAL"));

				listaAssuntos.add(Dados);
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return listaAssuntos;
	}
	
	/**
	 * Exclui assuntos de recurso quer será convertido em processo
	 * @param id_Recurso: identificação do recurso
	 * 
	 * @author lsbernardades
	 */
	public void excluirRecursoAssunto(String id_recurso) throws Exception {
		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		stSql= "DELETE FROM PROJUDI.RECURSO_ASSUNTO";
		stSql += " WHERE ID_RECURSO = ?";	ps.adicionarLong(id_recurso);
		
		executarUpdateDelete(stSql,ps);
	}

}
