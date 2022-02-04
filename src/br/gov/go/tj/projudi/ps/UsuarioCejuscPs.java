package br.gov.go.tj.projudi.ps;

import java.util.Date;
import java.util.List;

import br.gov.go.tj.projudi.dt.UsuarioCejuscDt;
import br.gov.go.tj.projudi.ne.ConfirmacaoEmailInscricaoNe;
import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class UsuarioCejuscPs extends Persistencia {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4459800201978743247L;

	public UsuarioCejuscPs(Connection conexao){
		Conexao = conexao;
	}
	
	/**
	 * Método para inserir UsuarioCejuscDt
	 * @param UsuarioCejuscDt usuarioCejuscDt
	 * @throws Exception
	 */
	public void inserir(UsuarioCejuscDt usuarioCejuscDt) throws Exception {
		String SqlCampos;
		String SqlValores;
		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		SqlCampos = "INSERT INTO PROJUDI.USU_CEJUSC (";
		SqlValores = " Values (";
		if (!(usuarioCejuscDt.getCurriculo().length() == 0)){
			SqlCampos += ",CURRICULO";
			SqlValores+= ",?";
			ps.adicionarString(usuarioCejuscDt.getCurriculo());
		}
		if (!(usuarioCejuscDt.getDataInscricao().length() == 0)){
			SqlCampos += ",DATA_INSCRICAO";
			SqlValores+= ",?";
			ps.adicionarDate(Funcoes.DataHora(usuarioCejuscDt.getDataInscricao()));
		}
		if (!(usuarioCejuscDt.getNumeroConta().length() == 0)){
			SqlCampos += ",NUMERO_CONTA";
			SqlValores+= ",?";
			ps.adicionarString(usuarioCejuscDt.getNumeroConta());
		}
		if (!(usuarioCejuscDt.getNumeroAgencia().length() == 0)){
			SqlCampos += ",NUMERO_AGENCIA";
			SqlValores+= ",?";
			ps.adicionarString(usuarioCejuscDt.getNumeroAgencia());
		}
		if (!(usuarioCejuscDt.getCodigoBanco().length() == 0)){
			SqlCampos += ",CODIGO_BANCO";
			SqlValores+= ",?";
			ps.adicionarString(usuarioCejuscDt.getCodigoBanco());
		}
		if (usuarioCejuscDt.getObservacaoStatus() != null && !(usuarioCejuscDt.getObservacaoStatus().length() == 0)){
			SqlCampos += ",OBSERVACAO_STATUS";
			SqlValores+= ",?";
			ps.adicionarString(usuarioCejuscDt.getObservacaoStatus());
		}
		if (!(usuarioCejuscDt.getCodigoStatusAtual().length() == 0)){
			SqlCampos += ",CODIGO_STATUS_ATUAL";
			SqlValores+= ",?";
			ps.adicionarString(usuarioCejuscDt.getCodigoStatusAtual());
		}
		if (!(usuarioCejuscDt.getDataStatusAtual().length() == 0)){
			SqlCampos += ",DATA_STATUS_ATUAL";
			SqlValores+= ",?";
			ps.adicionarDate(Funcoes.DataHora(usuarioCejuscDt.getDataStatusAtual()));
		}
		if (!(usuarioCejuscDt.getCodigoStatusAnterior().length() == 0)){
			SqlCampos += ",CODIGO_STATUS_ANTERIOR";
			SqlValores+= ",?";
			ps.adicionarString(usuarioCejuscDt.getCodigoStatusAnterior());
		}
		if (!(usuarioCejuscDt.getDataStatusAtual().length() == 0)){
			SqlCampos += ",DATA_STATUS_ANTERIOR";
			SqlValores+= ",?";
			ps.adicionarDate(Funcoes.DataHora(usuarioCejuscDt.getDataStatusAnterior()));
		}
		if (!(usuarioCejuscDt.getUsuarioDt().getId().length() == 0)){
			SqlCampos += ",ID_USU";
			SqlValores+= ",?";
			ps.adicionarLong(usuarioCejuscDt.getUsuarioDt().getId());
		}
		if (!(usuarioCejuscDt.getOpcaoConciliador().length() == 0)){
			SqlCampos += ",OPCAO_CONCILIADOR";
			SqlValores+= ",?";
			ps.adicionarString(usuarioCejuscDt.getOpcaoConciliador());
		}
		if (!(usuarioCejuscDt.getOpcaoMediador().length() == 0)){
			SqlCampos += ",OPCAO_MEDIADOR";
			SqlValores+= ",?";
			ps.adicionarString(usuarioCejuscDt.getOpcaoMediador());
		}
		if (!(usuarioCejuscDt.getVoluntario().length() == 0)){
			SqlCampos += ",VOLUNTARIO";
			SqlValores+= ",?";
			ps.adicionarString(usuarioCejuscDt.getVoluntario());
		}
		
		SqlCampos+=")";
 		SqlValores+=")";

 		Sql = SqlCampos.replace("(,", "(") + SqlValores.replace("(,", "(");

 		usuarioCejuscDt.setId(executarInsert(Sql, "ID_USU_CEJUSC", ps));
	}
	
	/**
	 * Método para atualizar status do candidato
	 * @param UsuarioCejuscDt dados
	 * @throws Exception
	 */
	public void atualizarStatus(UsuarioCejuscDt dados) throws Exception {
		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.USU_CEJUSC SET ";
		
		Sql += "CODIGO_STATUS_ANTERIOR = CODIGO_STATUS_ATUAL ";
		Sql += ",DATA_STATUS_ANTERIOR = DATA_STATUS_ATUAL ";
		
		if (dados.getCodigoStatusAtual() != null && dados.getCodigoStatusAtual().length() > 0){
			Sql += ",CODIGO_STATUS_ATUAL = ? ";
			ps.adicionarLong(dados.getCodigoStatusAtual());
		}
		
		if (dados.getDataStatusAtual() != null && dados.getDataStatusAtual().length() > 0){
			Sql += ",DATA_STATUS_ATUAL = ? ";
			ps.adicionarDate(new Date());
		}
		
		if (dados.getObservacaoStatus() != null && dados.getObservacaoStatus().length() > 0){
			Sql += ",OBSERVACAO_STATUS = ? ";
			ps.adicionarString(dados.getObservacaoStatus());
		}
		
		if (dados.getOpcaoMediador() != null && 
			dados.getOpcaoMediador().length() > 0 && 
			!dados.getOpcaoMediador().equals(ConfirmacaoEmailInscricaoNe.CODIGO_PERFIL_NAO_ESCOLHIDO)){
			
			Sql += ",OPCAO_MEDIADOR = ? ";
			ps.adicionarLong(dados.getOpcaoMediador());
		}
		
		if (dados.getOpcaoConciliador() != null && 
			dados.getOpcaoConciliador().length() > 0 && 
			!dados.getOpcaoConciliador().equals(ConfirmacaoEmailInscricaoNe.CODIGO_PERFIL_NAO_ESCOLHIDO)){
			
			Sql += ",OPCAO_CONCILIADOR = ? ";
			ps.adicionarLong(dados.getOpcaoConciliador());
		}
		
		Sql = Sql + " WHERE ID_USU_CEJUSC = ? ";
		ps.adicionarLong(dados.getId());

		Sql = Sql.replace("SET ,", "SET ");
		
		executarUpdateDelete(Sql, ps);
	}
	
	/**
	 * Método para alterar UsuarioCejuscDt
	 * @param UsuarioCejuscDt dados
	 * @throws Exception
	 */
	public void alterar(UsuarioCejuscDt dados) throws Exception {
		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.USU_CEJUSC SET ";
		
		Sql += "ID_USU = ? ";
		ps.adicionarLong(dados.getUsuarioDt().getId());
		
		Sql += ",CURRICULO = ? ";
		ps.adicionarString(dados.getCurriculo());
		
		Sql += ",NUMERO_CONTA = ? ";
		ps.adicionarString(dados.getNumeroConta());
		
		Sql += ",NUMERO_AGENCIA = ? ";
		ps.adicionarString(dados.getNumeroAgencia());
		
		Sql += ",CODIGO_BANCO = ? ";
		ps.adicionarString(dados.getCodigoBanco());
		
		Sql += ",OBSERVACAO_STATUS = ? ";
		ps.adicionarString(dados.getObservacaoStatus());
		
		Sql += ",CODIGO_STATUS_ATUAL = ? ";
		ps.adicionarString(dados.getCodigoStatusAtual());
		
		Sql += ",DATA_STATUS_ATUAL = ? ";
		ps.adicionarDate(Funcoes.DataHora(dados.getDataStatusAtual()));
		
		Sql += ",CODIGO_STATUS_ANTERIOR = ? ";
		ps.adicionarString(dados.getCodigoStatusAnterior());
		
		Sql += ",DATA_STATUS_ANTERIOR = ? ";
		ps.adicionarDate(Funcoes.DataHora(dados.getDataStatusAnterior()));
		
		Sql += ",OPCAO_CONCILIADOR = ? ";
		ps.adicionarString(dados.getOpcaoConciliador());
		
		Sql += ",OPCAO_MEDIADOR = ? ";
		ps.adicionarString(dados.getOpcaoMediador());
		
		Sql += ",VOLUNTARIO = ? ";
		ps.adicionarString(dados.getVoluntario());
		
		Sql = Sql + " WHERE ID_USU_CEJUSC = ? ";
		ps.adicionarLong(dados.getId());

		executarUpdateDelete(Sql, ps);
	}

	/**
	 * Método para consultar usuarioCejuscDt por ID_USU
	 * @param String idUsuario
	 * @return UsuarioCejuscDt
	 * @throws Exception
	 */
	public UsuarioCejuscDt consultarUsuarioCejuscDtIdUsuario(String id) throws Exception {
		UsuarioCejuscDt usuarioCejuscDt = null;
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM PROJUDI.USU_CEJUSC WHERE ID_USU = ?";
		
		ps.adicionarLong(id);
		
		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);
			
			if( rs1 != null ) {
				
				while(rs1.next()) {
					
					if( usuarioCejuscDt == null ) {
						usuarioCejuscDt = new UsuarioCejuscDt();
					}
					
					this.associarDt(usuarioCejuscDt, rs1);
				}
			}
		}
		finally {
			 try {if (rs1 != null) rs1.close();} catch (Exception e1) {}
		}
		
		return usuarioCejuscDt;
	}
	
	/**
	 * Método para consultar usuarioCejuscDt por ID_USU_CEJUSC
	 * @param String idUsuarioCejusc
	 * @return UsuarioCejuscDt
	 * @throws Exception
	 */
	public UsuarioCejuscDt consultarUsuarioCejuscDtIdUsuarioCejusc(String idUsuCejusc) throws Exception {
		UsuarioCejuscDt usuarioCejuscDt = null;
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM PROJUDI.USU_CEJUSC WHERE ID_USU_CEJUSC = ?";
		
		ps.adicionarLong(idUsuCejusc);
		
		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);
			
			if( rs1 != null ) {
				
				while(rs1.next()) {
					
					if( usuarioCejuscDt == null ) {
						usuarioCejuscDt = new UsuarioCejuscDt();
					}
					
					this.associarDt(usuarioCejuscDt, rs1);
				}
			}
		}
		finally {
			 try {if (rs1 != null) rs1.close();} catch (Exception e1) {}
		}
		
		return usuarioCejuscDt;
	}
	
	/**
	 * Método para associarDt da consulta do ResultSet.
	 * @param UsuarioCejuscDt usuarioCejuscDt
	 * @param ResultSetTJGO rs
	 * @throws Exception
	 */
	protected void associarDt(UsuarioCejuscDt usuarioCejuscDt, ResultSetTJGO rs) throws Exception {
		
		usuarioCejuscDt.setId(rs.getString("ID_USU_CEJUSC"));
		usuarioCejuscDt.setCurriculo(rs.getString("CURRICULO"));
		usuarioCejuscDt.setDataInscricao(rs.getString("DATA_INSCRICAO"));
		usuarioCejuscDt.setNumeroConta(rs.getString("NUMERO_CONTA"));
		usuarioCejuscDt.setNumeroAgencia(rs.getString("NUMERO_AGENCIA"));
		usuarioCejuscDt.setCodigoBanco(rs.getString("CODIGO_BANCO"));
		
		usuarioCejuscDt.setObservacaoStatus(rs.getString("OBSERVACAO_STATUS"));
		usuarioCejuscDt.setCodigoStatusAtual(rs.getString("CODIGO_STATUS_ATUAL"));
		usuarioCejuscDt.setCodigoStatusAnterior(rs.getString("CODIGO_STATUS_ANTERIOR"));
		
		usuarioCejuscDt.setDataStatusAtual(rs.getString("DATA_STATUS_ATUAL"));
		usuarioCejuscDt.setDataStatusAnterior(rs.getString("DATA_STATUS_ANTERIOR"));
		
		usuarioCejuscDt.setOpcaoConciliador(rs.getString("OPCAO_CONCILIADOR"));
		usuarioCejuscDt.setOpcaoMediador(rs.getString("OPCAO_MEDIADOR"));
		
		usuarioCejuscDt.setVoluntario(rs.getString("VOLUNTARIO"));
		
		usuarioCejuscDt.getUsuarioDt().setId(rs.getString("ID_USU"));
	}
	
	/**
	 * Método para consulta lista de usuarioCejuscDt no formato JSON
	 * @param String nome
	 * @param String cpf
	 * @param String statusAtualUsuario
	 * @param String idServ
	 * @param String apenasVoluntarios
	 * @param String posicaopaginaatual
	 * @return String
	 * @throws Exception
	 */
	public String consultarListaUsuarioCejuscDtJSON(String nome, String cpf, String statusAtualUsuario, String idServ, String apenasVoluntarios, String posicaopaginaatual) throws Exception {

		String sql = "";
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 4;

		sql += "SELECT DISTINCT U.ID_USU AS ID, U.NOME AS DESCRICAO1, U.CPF AS DESCRICAO2, ";
		sql += " (CASE UC.CODIGO_STATUS_ATUAL ";
        sql += " WHEN ? THEN 'Aguardando avaliação' "; 		ps.adicionarLong(UsuarioCejuscDt.CODIGO_STATUS_AGUARDANDO_AVALIACAO);
        sql += " WHEN ? THEN 'Aprovado' "; 					ps.adicionarLong(UsuarioCejuscDt.CODIGO_STATUS_APROVADO);
        sql += " WHEN ? THEN 'Pendente' ";					ps.adicionarLong(UsuarioCejuscDt.CODIGO_STATUS_PENDENTE);
        sql += " WHEN ? THEN 'Reprovado' ";					ps.adicionarLong(UsuarioCejuscDt.CODIGO_STATUS_REPROVADO);
        sql += " ELSE TO_CHAR(UC.CODIGO_STATUS_ATUAL) ";
        sql += " END) AS DESCRICAO3, ";
        
        sql += " (CASE UC.VOLUNTARIO ";
        sql += " WHEN 1 THEN 'Sim' ";
        sql += " ELSE 'Não' ";
        sql += " END) AS DESCRICAO4 ";
        
        
		sql += "FROM (PROJUDI.USU_CEJUSC UC LEFT JOIN PROJUDI.USU U ON (UC.ID_USU = U.ID_USU) LEFT JOIN PROJUDI.CEJUSC_DISPONIBILIDADE CD ON (CD.ID_USU_CEJUSC = UC.ID_USU_CEJUSC)) WHERE";
        
		if( nome != null && nome.length() > 0 ) {
			sql += " AND U.NOME LIKE ?";				ps.adicionarString(nome+"%");
		}
		if( cpf != null && cpf.length() > 0 ) {
			sql += " AND U.CPF = ?";
			ps.adicionarString(cpf);
		}
		if( statusAtualUsuario != null && statusAtualUsuario.length() > 0 ) {
			sql += " AND UC.CODIGO_STATUS_ATUAL = ?";
			ps.adicionarString(statusAtualUsuario);
		}
		if( apenasVoluntarios != null && apenasVoluntarios.length() > 0 && apenasVoluntarios.equals("sim")) {
			sql += " AND UC.VOLUNTARIO = 1";
		}
		if( idServ != null && idServ.length() > 0 ) {
			sql += " AND CD.ID_SERV = ?";
			ps.adicionarLong(idServ);
		}
		
		sql += " ORDER BY U.NOME";
		
		sql = sql.replace("WHERE AND", "WHERE");
		sql = sql.replace("WHERE ORDER", "ORDER");

		try{
			rs1 = consultarPaginacao(sql, ps, posicaopaginaatual);

			sql = " SELECT COUNT(*) AS QUANTIDADE ";
			sql += " FROM (";
			sql += "SELECT DISTINCT U.ID_USU AS ID, U.NOME AS DESCRICAO1, U.CPF AS DESCRICAO2, ";
			sql += " (CASE UC.CODIGO_STATUS_ATUAL ";
	        sql += " WHEN ? THEN 'Aguardando avaliação' ";
	        sql += " WHEN ? THEN 'Aprovado' ";
	        sql += " WHEN ? THEN 'Pendente' ";
	        sql += " WHEN ? THEN 'Reprovado' ";
	        sql += " ELSE TO_CHAR(UC.CODIGO_STATUS_ATUAL) ";
	        sql += " END) AS DESCRICAO3, ";
	        
	        
	        sql += " (CASE UC.VOLUNTARIO ";
	        sql += " WHEN 1 THEN 'Sim' ";
	        sql += " ELSE 'Não' ";
	        sql += " END) AS DESCRICAO4 ";
	        
	        
	        
			sql += "FROM (PROJUDI.USU_CEJUSC UC LEFT JOIN PROJUDI.USU U ON (UC.ID_USU = U.ID_USU) LEFT JOIN PROJUDI.CEJUSC_DISPONIBILIDADE CD ON (CD.ID_USU_CEJUSC = UC.ID_USU_CEJUSC)) WHERE";
	        
			if( nome != null && nome.length() > 0 ) {
				sql += " AND U.NOME LIKE ?";
			}
			if( cpf != null && cpf.length() > 0 ) {
				sql += " AND U.CPF = ?";
			}
			if( cpf != null && statusAtualUsuario.length() > 0 ) {
				sql += " AND UC.CODIGO_STATUS_ATUAL = ?";
			}
			if( apenasVoluntarios != null && apenasVoluntarios.length() > 0 && apenasVoluntarios.equals("sim")) {
				sql += " AND UC.VOLUNTARIO = 1";
			}
			if( idServ != null && idServ.length() > 0 ) {
				sql += " AND CD.ID_SERV = ?";
			}
			sql += ") Total";
			
			sql = sql.replace("WHERE AND", "WHERE");
			sql = sql.replace("WHERE)", ")");
			
			rs2 = consultar(sql, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicaopaginaatual, rs1, qtdeColunas);
			
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
	
	/**
	 * Método para consulta lista de usuarioCejuscDt no formato JSON
	 * @param String nome
	 * @param String posicaopaginaatual
	 * @return String
	 * @throws Exception
	 */
	public String consultarDescricaoJSON(String nome, String posicaopaginaatual) throws Exception {

		String sql = "";
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;

		sql += "SELECT U.ID_USU AS ID, U.NOME AS DESCRICAO1, U.CPF AS DESCRICAO2 ";
		sql += "FROM (PROJUDI.USU_CEJUSC UC LEFT JOIN PROJUDI.USU U ON (UC.ID_USU = U.ID_USU)) WHERE";
        
		if( nome != null && nome.length() > 0 ) {
			sql += " AND U.NOME LIKE ?";			ps.adicionarString(nome+"%");
		}
		
		sql += " ORDER BY U.NOME";
		
		sql = sql.replace("WHERE AND", "WHERE");
		sql = sql.replace("WHERE ORDER", "ORDER");

		try{
			rs1 = consultarPaginacao(sql, ps, posicaopaginaatual);

			sql = " SELECT COUNT(*) AS QUANTIDADE ";
			sql += " FROM (";
			sql += "SELECT U.ID_USU AS ID, U.NOME AS DESCRICAO1, U.CPF AS DESCRICAO2 ";
			sql += "FROM (PROJUDI.USU_CEJUSC UC LEFT JOIN PROJUDI.USU U ON (UC.ID_USU = U.ID_USU)) WHERE";
	        
			if( nome != null && nome.length() > 0 ) {
				sql += " AND U.NOME LIKE ?";
			}
			sql += ") Total";
			
			sql = sql.replace("WHERE AND", "WHERE");
			sql = sql.replace("WHERE)", ")");
			
			rs2 = consultar(sql, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicaopaginaatual, rs1, qtdeColunas);
			
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
	
	/**
	 * Método para consulta lista de usuarioCejuscDt no formato JSON
	 * @param String nome
	 * @param String posicaopaginaatual
	 * @return List<UsuarioCejuscDt>
	 * @throws Exception
	 */
	public List<UsuarioCejuscDt> consultarDescricao(String nome, String posicaopaginaatual) throws Exception {
		String sql = "";
		List<UsuarioCejuscDt> listaUsuarioCejuscDt = null;
		return listaUsuarioCejuscDt;
	}
	
	/**
	 * Método para consulta lista de usuarioCejuscDt aprovado no formato JSON
	 * @param String nome
	 * @param String posicaopaginaatual
	 * @return String
	 * @throws Exception
	 */
	public String consultarDescricaoStatusAprovadoJSON(String nome, String posicaopaginaatual) throws Exception {

		String sql = "";
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;

		sql += "SELECT UC.ID_USU_CEJUSC AS ID, U.NOME AS DESCRICAO1, U.CPF AS DESCRICAO2 ";
		sql += "FROM (PROJUDI.USU_CEJUSC UC LEFT JOIN PROJUDI.USU U ON (UC.ID_USU = U.ID_USU)) ";
		sql += " WHERE";
        
		if( nome != null && nome.length() > 0 ) {
			sql += " AND U.NOME LIKE ?";			ps.adicionarString(nome+"%");
		}
		
		sql += " AND UC.CODIGO_STATUS_ATUAL = ?";
		ps.adicionarLong(UsuarioCejuscDt.CODIGO_STATUS_APROVADO);
		
		sql += " AND (UC.OPCAO_CONCILIADOR = ? OR UC.OPCAO_MEDIADOR = ?)";	
		ps.adicionarLong(ConfirmacaoEmailInscricaoNe.CODIGO_PERFIL_APROVADO);
		ps.adicionarLong(ConfirmacaoEmailInscricaoNe.CODIGO_PERFIL_APROVADO);
		
		sql += " ORDER BY U.NOME";
		
		sql = sql.replace("WHERE AND", "WHERE");
		sql = sql.replace("WHERE ORDER", "ORDER");

		try{
			rs1 = consultarPaginacao(sql, ps, posicaopaginaatual);

			sql = " SELECT COUNT(*) AS QUANTIDADE ";
			sql += " FROM (";
			sql += "SELECT UC.ID_USU_CEJUSC AS ID, U.NOME AS DESCRICAO1, U.CPF AS DESCRICAO2 ";
			sql += "FROM (PROJUDI.USU_CEJUSC UC LEFT JOIN PROJUDI.USU U ON (UC.ID_USU = U.ID_USU)) WHERE";
	        
			if( nome != null && nome.length() > 0 ) {
				sql += " AND U.NOME LIKE ?";
			}
			
			sql += " AND UC.CODIGO_STATUS_ATUAL = ?";	
			sql += " AND (UC.OPCAO_CONCILIADOR = ? OR UC.OPCAO_MEDIADOR = ?)";	
			
			sql += ") Total";
			
			sql = sql.replace("WHERE AND", "WHERE");
			sql = sql.replace("WHERE)", ")");
			
			rs2 = consultar(sql, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicaopaginaatual, rs1, qtdeColunas);
			
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}

		}
		return stTemp;
	}
	
	/**
	 * Método para consultar ID_USU pelo ID_USU_CEJUSC
	 * @param String idUsuCejusc
	 * @return IdUsu
	 * @throws Exception
	 */
	public String consultarIdUsu(String idUsuCejusc) throws Exception {
		String idUsu = null;
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT ID_USU FROM PROJUDI.USU_CEJUSC WHERE ID_USU_CEJUSC = ?";
		
		ps.adicionarLong(idUsuCejusc);
		
		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);
			
			if( rs1 != null ) {
				
				while(rs1.next()) {
					idUsu = rs1.getString("ID_USU");
				}
			}
		}
		finally {
			 try {if (rs1 != null) rs1.close();} catch (Exception e1) {}
		}
		
		return idUsu;
	}
	
}
