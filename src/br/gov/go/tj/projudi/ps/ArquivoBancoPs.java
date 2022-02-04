package br.gov.go.tj.projudi.ps;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.Connection;

import br.gov.go.tj.projudi.dt.AreaDt;
import br.gov.go.tj.projudi.dt.ArquivoBancoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import br.gov.go.tj.utils.TJDataHora;


public class ArquivoBancoPs extends Persistencia {

	private static final long serialVersionUID = 3303531040157673011L;
		
	public ArquivoBancoPs(Connection conexao){
		Conexao = conexao;
	}

	/**
	 * Método para salvar arquivo lido no banco.
	 * @param String nomeArquivo
	 * @param int idBanco
	 * @param int statusLeitura
	 * @param Strring mensagem
	 * @param String conteudo
	 * @throws Exception
	 */
	public void salvarArquivoLido(String nomeArquivo, int idBanco, int statusLeitura, String mensagem, String conteudo) throws Exception{
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql= "INSERT INTO projudi.ARQUIVO_BANCO (ARQUIVO_BANCO,ID_BANCO,ERRO,DATA_REALIZACAO,MENSAGEM,CONTEUDO) VALUES (?,?,?,?,?,?)";
		
		ps.adicionarString(nomeArquivo.trim());
		ps.adicionarLong(idBanco);
		ps.adicionarLong(statusLeitura);
		ps.adicionarDateTime(new Date());
		ps.adicionarClob(mensagem);
		ps.adicionarClob(conteudo);
		
		executarInsert(sql,"ID_ARQUIVO_BANCO" ,ps);				
	}
	
	/**
	 * Consulta se arquivo já foi lido.
	 * Retorna false caso não foi lido e true se sim.
	 * 
	 * @param String nomeArquivo
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isArquivoJaLido(String nomeArquivo) throws Exception{
		boolean retorno = false;
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "SELECT ARQUIVO_BANCO FROM PROJUDI.ARQUIVO_BANCO WHERE ARQUIVO_BANCO = ? AND TO_CHAR(DATA_REALIZACAO, 'YYYY') = TO_CHAR(SYSDATE, 'YYYY') ";
		ps.adicionarString(nomeArquivo);
				
		ResultSetTJGO rs1 = null;
		
		try{
			rs1 = consultar(sql, ps);
			
			if( rs1 != null ) {
				while( rs1.next() ) {
					retorno = true;
				}
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return retorno;
	}
	
	/**
	 * Consulta se arquivo já foi lido.
	 * 
	 * @param String idArquivoBanco
	 * @return String
	 * @throws Exception
	 */
	public String consulteArquivoLido(String idArquivoBanco) throws Exception{
		String retorno = "";
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "SELECT CONTEUDO FROM PROJUDI.ARQUIVO_BANCO WHERE ID_ARQUIVO_BANCO = ?";
		ps.adicionarString(idArquivoBanco);
				
		ResultSetTJGO rs1 = null;
		
		try{
			rs1 = consultar(sql, ps);
			
			if( rs1 != null ) {
				while( rs1.next() ) {
					retorno = rs1.getString("CONTEUDO");
				}
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return retorno;
	}
	
	/**
	 * Consulta os arquivos já lidos e não reprocessados.
	 * 
	 * @return List<ArquivoBancoDt>
	 * @throws Exception
	 */
	public List<ArquivoBancoDt> consulteArquivosLidosParaReprocessamento() throws Exception{
		List<ArquivoBancoDt> retorno = new ArrayList<ArquivoBancoDt>();
		
		String sql = "SELECT ID_ARQUIVO_BANCO, ARQUIVO_BANCO, ID_BANCO, DATA_REALIZACAO, ERRO, CODIGO_TEMP, ATIVO, MENSAGEM, CONTEUDO, REPROCESSADO ";
		sql += " FROM PROJUDI.ARQUIVO_BANCO ";
		sql += " WHERE REPROCESSADO = 0 ";
		sql += " AND ID_ARQUIVO_BANCO > (SELECT MAX(ID_ARQUIVO_BANCO) FROM PROJUDI.ARQUIVO_BANCO WHERE REPROCESSADO = 1) ";
		sql += " AND ERRO = 1 ";
		sql += " AND TO_NUMBER(TO_CHAR(DATA_REALIZACAO, 'YYYYMMDD')) < TO_NUMBER(TO_CHAR(SYSDATE, 'YYYYMMDD')) ";
		sql += " ORDER BY ID_ARQUIVO_BANCO ";
				
		ResultSetTJGO rs1 = null;
		
		try{
			rs1 = consultarSemParametros(sql);
			
			if( rs1 != null ) {
				while( rs1.next() ) {
					ArquivoBancoDt arquivoBanco = new ArquivoBancoDt();
					associarDt(arquivoBanco, rs1);
					retorno.add(arquivoBanco);					
				}
			}		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return retorno;
	}
	
	private void associarDt(ArquivoBancoDt arquivoBanco, ResultSetTJGO rs1)  throws Exception {
		arquivoBanco.setId(rs1.getString("ID_ARQUIVO_BANCO"));
		arquivoBanco.setArquivoBanco(rs1.getString("ARQUIVO_BANCO"));
		arquivoBanco.setId_Banco(rs1.getString("ID_BANCO"));
		arquivoBanco.setDataRealizacao(new TJDataHora(rs1.getDateTime("DATA_REALIZACAO")));
		arquivoBanco.setErro(rs1.getString("ERRO"));
		arquivoBanco.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
		arquivoBanco.setAtivo(rs1.getString("ATIVO"));
		arquivoBanco.setMensagem(rs1.getString("MENSAGEM"));
		arquivoBanco.setConteudo(rs1.getString("CONTEUDO"));
		arquivoBanco.setReprocessado(rs1.getString("REPROCESSADO"));
	}
	
	/**
	 * Método para atualizar o arquivo reprocessado.
	 * @param String idArquivoBanco	 
	 * @throws Exception
	 */
	public void atualizarArquivoReprocessado(String idArquivoBanco) throws Exception{
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql= "UPDATE projudi.ARQUIVO_BANCO SET REPROCESSADO = 1 WHERE ID_ARQUIVO_BANCO = ?";
		ps.adicionarLong(idArquivoBanco);
		
		executarUpdateDelete(sql, ps);				
	}
	
	/**
	 * Método que consulta as ultimas 5 ocorrencias de arquivos recebido da Caixa economina.
	 * Também conhecido como arquivos de "Rajada".
	 * 
	 * @return List<ArquivoBancoDt>
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public List<ArquivoBancoDt> consulta5UltimosArquivosProcessados() throws Exception {
		List<ArquivoBancoDt> retorno = new ArrayList<ArquivoBancoDt>();
		
		String sql = "SELECT * ";
		sql += "FROM ( ";
		sql += "SELECT a.* , ROW_NUMBER() OVER (ORDER BY a.ID_ARQUIVO_BANCO desc) AS ROW_NUMBER ";
		sql += "FROM PROJUDI.ARQUIVO_BANCO a ";
		sql += "WHERE TO_NUMBER(TO_CHAR(a.data_realizacao, 'YYYYMMDD')) >= TO_NUMBER(TO_CHAR(SYSDATE - 7, 'YYYYMMDD')) ";
		sql += ") ";
		sql += "WHERE ROW_NUMBER <= 5 ";
		
		ResultSetTJGO rs1 = null;
		
		try{
			rs1 = consultarSemParametros(sql);
			
			if( rs1 != null ) {
				while( rs1.next() ) {
					ArquivoBancoDt arquivoBanco = new ArquivoBancoDt();
					associarDt(arquivoBanco, rs1);
					retorno.add(arquivoBanco);					
				}
			}		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return retorno;
	}
}
