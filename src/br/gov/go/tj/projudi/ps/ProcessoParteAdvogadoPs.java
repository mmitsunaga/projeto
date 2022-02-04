package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;

import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteAdvogadoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteAdvogadoSPGDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;

import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class ProcessoParteAdvogadoPs extends ProcessoParteAdvogadoPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = 1002151485720315037L;

    public ProcessoParteAdvogadoPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Consultar advogados vinculados a um processo
	 */
	public List consultarAdvogadosProcesso(String id_processo) throws Exception {
		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT * FROM VIEW_PROC_PARTE_ADVOGADO p WHERE p.ID_PROC = ?";
		ps.adicionarLong(id_processo);
		Sql += " AND p.DATA_SAIDA IS NULL";
		Sql += " AND p.GRUPO_CODIGO NOT IN(?,?,?,?,?,?,?,?) "; 
		ps.adicionarLong(GrupoDt.COORDENADOR_HABILITACAO_MP);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROMOTORIA);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROCURADORIA_ESTADUAL);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROCURADORIA_MUNICIPAL);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROCURADORIA_FEDERAL);
		ps.adicionarLong(GrupoDt.COORDENADOR_ESCRITORIO_JURIDICO);
		ps.adicionarLong(GrupoDt.COORDENADOR_DEFENSORIA_PUBLICA);
		ps.adicionarLong(GrupoDt.COORDENADOR_ADVOCACIA_PUBLICA);
		Sql += " ORDER BY p.ID_PROC_PARTE_TIPO";
		try{
			rs1 = consultar(Sql, ps);
			while (rs1.next()) {
				ProcessoParteAdvogadoDt obTemp = new ProcessoParteAdvogadoDt();
				this.associarDados(obTemp, rs1);
				obTemp.setProcessoParteTipo(rs1.getString("PROC_PARTE_TIPO"));
				obTemp.setDativo(rs1.getString("DATIVO"));
				obTemp.setEmail(rs1.getString("EMAIL"));
				liTemp.add(obTemp);
			}
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}
	
	/**
	 * Consulta os advogados que foram habilitados E desabilitados no processo.
	 * @param id_processo - ID do processo
	 * @return lista de advogados desabilitados
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarAdvogadosDesabilitadosProcesso(String id_processo) throws Exception {
		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT * FROM VIEW_PROC_PARTE_ADVOGADO p WHERE p.ID_PROC = ?";
		ps.adicionarLong(id_processo);
		Sql += " AND p.DATA_SAIDA IS NOT NULL";
		Sql += " AND p.GRUPO_CODIGO NOT IN(?,?,?,?,?,?,?,?) "; 
		ps.adicionarLong(GrupoDt.COORDENADOR_HABILITACAO_MP);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROMOTORIA);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROCURADORIA_ESTADUAL);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROCURADORIA_MUNICIPAL);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROCURADORIA_FEDERAL);
		ps.adicionarLong(GrupoDt.COORDENADOR_ESCRITORIO_JURIDICO);
		ps.adicionarLong(GrupoDt.COORDENADOR_DEFENSORIA_PUBLICA);
		ps.adicionarLong(GrupoDt.COORDENADOR_ADVOCACIA_PUBLICA);
		Sql += " ORDER BY p.ID_PROC_PARTE_TIPO";
		try{
			rs1 = consultar(Sql, ps);
			while (rs1.next()) {
				ProcessoParteAdvogadoDt obTemp = new ProcessoParteAdvogadoDt();
				this.associarDados(obTemp, rs1);
				obTemp.setProcessoParteTipo(rs1.getString("PROC_PARTE_TIPO"));
				obTemp.setDativo(rs1.getString("DATIVO"));
				liTemp.add(obTemp);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}	
	
	  /**
	 * Consultar partes vincualados a um procurador
	 */
	public List consultarAdvogadoParteProcesso(String Id_Processo, String Id_ProcessoParte) throws Exception {
		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT * FROM PROJUDI.VIEW_PROC_PARTE_ADVOGADO p ";
		Sql += " WHERE p.ID_PROC = ? AND p.ID_PROC_PARTE = ? AND p.DATA_SAIDA IS NULL ";
		ps.adicionarLong(Id_Processo);
		ps.adicionarLong(Id_ProcessoParte);
		Sql += " AND p.GRUPO_CODIGO NOT IN(?,?,?,?,?,?,?,?) "; 
		ps.adicionarLong(GrupoDt.COORDENADOR_HABILITACAO_MP);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROMOTORIA);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROCURADORIA_ESTADUAL);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROCURADORIA_MUNICIPAL);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROCURADORIA_FEDERAL);
		ps.adicionarLong(GrupoDt.COORDENADOR_ESCRITORIO_JURIDICO);
		ps.adicionarLong(GrupoDt.COORDENADOR_DEFENSORIA_PUBLICA);
		ps.adicionarLong(GrupoDt.COORDENADOR_ADVOCACIA_PUBLICA);
		
		try{
			rs1 = consultar(Sql, ps);
			while (rs1.next()) {
				ProcessoParteAdvogadoDt parteAdvogadoDt = new ProcessoParteAdvogadoDt();
				parteAdvogadoDt.setId(rs1.getString("ID_PROC_PARTE_ADVOGADO"));
				parteAdvogadoDt.setNomeAdvogado(rs1.getString("NOME_ADVOGADO"));
				parteAdvogadoDt.setId_UsuarioServentiaAdvogado(rs1.getString("ID_USU_SERV_ADVOGADO"));
				parteAdvogadoDt.setUsuarioAdvogado(rs1.getString("USU_ADVOGADO"));
				parteAdvogadoDt.setId_ProcessoParte(rs1.getString("ID_PROC_PARTE"));
				parteAdvogadoDt.setNomeParte(rs1.getString("NOME_PARTE"));
				parteAdvogadoDt.setDataEntrada(Funcoes.FormatarData(rs1.getDateTime("DATA_ENTRADA")));
				parteAdvogadoDt.setDataSaida(Funcoes.FormatarData(rs1.getDateTime("DATA_SAIDA")));
				parteAdvogadoDt.setPrincipal(Funcoes.FormatarLogico(rs1.getString("PRINC")));
				parteAdvogadoDt.setOabNumero(rs1.getString("OAB_NUMERO"));
				parteAdvogadoDt.setOabComplemento(rs1.getString("OAB_COMPLEMENTO"));
				parteAdvogadoDt.setId_Processo(rs1.getString("ID_PROC"));
				parteAdvogadoDt.setProcessoNumero(Funcoes.formataNumeroProcesso(rs1.getString("PROC_NUMERO")));
				parteAdvogadoDt.setSegredoJusticao(rs1.getBoolean("SEGREDO_JUSTICA"));
				parteAdvogadoDt.setRecebeIntimacao(rs1.getBoolean("RECEBE_INTIMACAO"));
				liTemp.add(parteAdvogadoDt);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}

	  /**
	 * Consultar ProcessoPatesAdvogados de um determinado processo, partes vinculado a um id_Usuario_serventia
	 */
	public List consultarProcessoParteAdvogado(String Id_Processo, String Id_UsuarioServentia) throws Exception {
		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT p.ID_PROC_PARTE, p.NOME_PARTE, p.NOME_ADVOGADO FROM PROJUDI.VIEW_PROC_PARTE_ADVOGADO p ";
		Sql += " WHERE p.ID_PROC = ? AND p.ID_USU_SERV_ADVOGADO = ?";
		ps.adicionarLong(Id_Processo);
		ps.adicionarLong(Id_UsuarioServentia);
		Sql += " AND p.GRUPO_CODIGO NOT IN(?,?,?,?,?,?,?,?) "; 
		ps.adicionarLong(GrupoDt.COORDENADOR_HABILITACAO_MP);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROMOTORIA);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROCURADORIA_ESTADUAL);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROCURADORIA_MUNICIPAL);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROCURADORIA_FEDERAL);
		ps.adicionarLong(GrupoDt.COORDENADOR_ESCRITORIO_JURIDICO);
		ps.adicionarLong(GrupoDt.COORDENADOR_DEFENSORIA_PUBLICA);
		ps.adicionarLong(GrupoDt.COORDENADOR_ADVOCACIA_PUBLICA);
		
		try{
			rs1 = consultar(Sql, ps);
			while (rs1.next()) {
				ProcessoParteAdvogadoDt obTemp = new ProcessoParteAdvogadoDt();
				obTemp.setId_ProcessoParte(rs1.getString("ID_PROC_PARTE"));
				obTemp.setNomeAdvogado(rs1.getString("NOME_ADVOGADO"));
				obTemp.setNomeParte(rs1.getString("NOME_PARTE"));
				liTemp.add(obTemp);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}
	
	  /**
	 * Consultar partes vincualados a um promotor
	 */
	public List consultarProcessoPartePromotorSubstitutoProcessual(String Id_Processo, String Id_UsuarioServentiaPromotor) throws Exception {
		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT p.ID_PROC_PARTE_ADVOGADO, ID_USU_SERV_ADVOGADO, p.NOME_PARTE, p.NOME_ADVOGADO FROM PROJUDI.VIEW_PROC_PARTE_ADVOGADO p ";
		Sql += " WHERE p.ID_PROC = ? AND p.ID_USU_SERV_ADVOGADO = ? AND p.DATA_SAIDA IS NULL ";
		ps.adicionarLong(Id_Processo);
		ps.adicionarLong(Id_UsuarioServentiaPromotor);
		Sql += " AND p.GRUPO_CODIGO NOT IN(?,?,?,?,?,?,?,?) "; 
		ps.adicionarLong(GrupoDt.COORDENADOR_HABILITACAO_MP);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROMOTORIA);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROCURADORIA_ESTADUAL);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROCURADORIA_MUNICIPAL);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROCURADORIA_FEDERAL);
		ps.adicionarLong(GrupoDt.COORDENADOR_ESCRITORIO_JURIDICO);
		ps.adicionarLong(GrupoDt.COORDENADOR_DEFENSORIA_PUBLICA);
		ps.adicionarLong(GrupoDt.COORDENADOR_ADVOCACIA_PUBLICA);
		
		try{
			rs1 = consultar(Sql, ps);
			while (rs1.next()) {
				ProcessoParteAdvogadoDt obTemp = new ProcessoParteAdvogadoDt();
				obTemp.setId_ProcessoParte(rs1.getString("ID_PROC_PARTE_ADVOGADO"));
				obTemp.setId_UsuarioServentiaAdvogado(rs1.getString("ID_USU_SERV_ADVOGADO"));
				obTemp.setNomeAdvogado(rs1.getString("NOME_ADVOGADO"));
				obTemp.setNomeParte(rs1.getString("NOME_PARTE"));
				liTemp.add(obTemp);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}
	
	/**
	 * Associa os dados da parte atraves do resultset
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 11/04/2008 - 14:12
	 * @param ProcessoParte
	 *            parte, Parte do processo a ser vinculada os dados
	 * @param ResultSet
	 *            rs1, ResultSetTJGO com os dados do banco de dados
	 * @return boolean
	 * @throws Exception 
	 */
	private void associarDados(ProcessoParteAdvogadoDt parteAdvogadoDt, ResultSetTJGO rs1) throws Exception{

		parteAdvogadoDt.setId(rs1.getString("ID_PROC_PARTE_ADVOGADO"));
		parteAdvogadoDt.setNomeAdvogado(rs1.getString("NOME_ADVOGADO"));
		parteAdvogadoDt.setId_UsuarioServentiaAdvogado(rs1.getString("ID_USU_SERV_ADVOGADO"));
		parteAdvogadoDt.setUsuarioAdvogado(rs1.getString("USU_ADVOGADO"));
		parteAdvogadoDt.setId_ProcessoParte(rs1.getString("ID_PROC_PARTE"));
		parteAdvogadoDt.setNomeParte(rs1.getString("NOME_PARTE"));
		parteAdvogadoDt.setDataEntrada(Funcoes.FormatarData(rs1.getDateTime("DATA_ENTRADA")));
		parteAdvogadoDt.setDataSaida(Funcoes.FormatarData(rs1.getDateTime("DATA_SAIDA")));
		parteAdvogadoDt.setPrincipal(Funcoes.FormatarLogico(rs1.getString("PRINC")));
		parteAdvogadoDt.setOabNumero(rs1.getString("OAB_NUMERO"));
		parteAdvogadoDt.setOabComplemento(rs1.getString("OAB_COMPLEMENTO"));
		parteAdvogadoDt.setId_Processo(rs1.getString("ID_PROC"));
		parteAdvogadoDt.setProcessoNumero(Funcoes.formataNumeroProcesso(rs1.getString("PROC_NUMERO")));
		parteAdvogadoDt.setSegredoJusticao(rs1.getBoolean("SEGREDO_JUSTICA"));
		parteAdvogadoDt.setRecebeIntimacao(rs1.getBoolean("RECEBE_INTIMACAO"));
		
		if (parteAdvogadoDt.getOabNumero().equals("")){
			parteAdvogadoDt.setOabNumero("MP");
			parteAdvogadoDt.setOabComplemento("GO");
		}

	}
	
	/**
	 * Sobrescrevendo método da classe Gen para controlar atributo RecebeIntimacao  
	 */
	protected void associarDt( ProcessoParteAdvogadoDt Dados, ResultSetTJGO rs1 )  throws Exception {

		Dados.setId(rs1.getString("ID_PROC_PARTE_ADVOGADO"));
		Dados.setNomeParte(rs1.getString("NOME_PARTE"));
		Dados.setNomeAdvogado( rs1.getString("NOME_ADVOGADO"));
		Dados.setId_UsuarioServentiaAdvogado( rs1.getString("ID_USU_SERV_ADVOGADO"));
		Dados.setUsuarioAdvogado( rs1.getString("USU_ADVOGADO"));
		Dados.setId_ProcessoParte( rs1.getString("ID_PROC_PARTE"));
		Dados.setDataEntrada( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_ENTRADA")));
		Dados.setDataSaida( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_SAIDA")));
		Dados.setPrincipal( Funcoes.FormatarLogico(rs1.getString("PRINC")));
		Dados.setCodigoTemp( rs1.getString("CODIGO_TEMP"));
		Dados.setOabNumero( rs1.getString("OAB_NUMERO"));
		Dados.setOabComplemento( rs1.getString("OAB_COMPLEMENTO"));
		Dados.setId_Processo( rs1.getString("ID_PROC"));
		Dados.setProcessoNumero( rs1.getString("PROC_NUMERO"));
		Dados.setId_ProcessoParteTipo( rs1.getString("ID_PROC_PARTE_TIPO"));
		Dados.setProcessoParteTipo( rs1.getString("PROC_PARTE_TIPO"));
		Dados.setSegredoJusticao(rs1.getBoolean("SEGREDO_JUSTICA"));
		Dados.setRecebeIntimacao(rs1.getBoolean("RECEBE_INTIMACAO"));

	}

	/**
	 * Buscar Advogados Ativos vinculados a um parte no processo.
	 */
	public List consultarAdvogadosParte(String id_processoParte) throws Exception {
		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT * FROM VIEW_PROC_PARTE_ADVOGADO p WHERE p.ID_PROC_PARTE = ?";
		ps.adicionarLong(id_processoParte);
		Sql += " AND p.DATA_SAIDA IS NULL";
		Sql += " AND p.GRUPO_CODIGO NOT IN(?,?,?,?,?,?,?,?) "; 
		ps.adicionarLong(GrupoDt.COORDENADOR_HABILITACAO_MP);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROMOTORIA);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROCURADORIA_ESTADUAL);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROCURADORIA_MUNICIPAL);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROCURADORIA_FEDERAL);
		ps.adicionarLong(GrupoDt.COORDENADOR_ESCRITORIO_JURIDICO);
		ps.adicionarLong(GrupoDt.COORDENADOR_DEFENSORIA_PUBLICA);
		ps.adicionarLong(GrupoDt.COORDENADOR_ADVOCACIA_PUBLICA);
		Sql += " ORDER BY p.ID_PROC_PARTE_TIPO";
		try{
			rs1 = consultar(Sql, ps);
			while (rs1.next()) {
					ProcessoParteAdvogadoDt obTemp = new ProcessoParteAdvogadoDt();
					this.associarDados(obTemp, rs1);
					obTemp.setGrupoCodigo(rs1.getString("GRUPO_CODIGO"));
					obTemp.setGrupo(rs1.getString("GRUPO"));
					obTemp.setDativo(String.valueOf(rs1.getBoolean("dativo")));

					liTemp.add(obTemp);
				}
			// rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}
	
	/**
	 * Buscar Advogados Ativos vinculados a um parte no processo que estão
     * autorizados a receber intimação.
	 */
	public List consultarAdvogadosRecebemIntimacaoParte(String id_processoParte) throws Exception {
		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		Sql = "SELECT * FROM VIEW_PROC_PARTE_ADVOGADO p WHERE p.ID_PROC_PARTE=?";
		ps.adicionarLong(id_processoParte);
		Sql += " AND p.DATA_SAIDA IS NULL AND p.RECEBE_INTIMACAO = ? ";
		ps.adicionarLong(1);
		Sql += " AND p.GRUPO_CODIGO NOT IN(?,?,?,?,?,?,?,?) "; 
		ps.adicionarLong(GrupoDt.COORDENADOR_HABILITACAO_MP);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROMOTORIA);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROCURADORIA_ESTADUAL);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROCURADORIA_MUNICIPAL);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROCURADORIA_FEDERAL);
		ps.adicionarLong(GrupoDt.COORDENADOR_ESCRITORIO_JURIDICO);
		ps.adicionarLong(GrupoDt.COORDENADOR_DEFENSORIA_PUBLICA);
		ps.adicionarLong(GrupoDt.COORDENADOR_ADVOCACIA_PUBLICA);
		Sql += " ORDER BY p.ID_PROC_PARTE_TIPO";
		try{
			rs1 = consultar(Sql, ps);
			while (rs1.next()) {
				ProcessoParteAdvogadoDt obTemp = new ProcessoParteAdvogadoDt();
				this.associarDados(obTemp, rs1);
				obTemp.setGrupoCodigo(rs1.getString("GRUPO_CODIGO"));
				obTemp.setGrupo(rs1.getString("GRUPO"));
				obTemp.setDativo(String.valueOf(rs1.getBoolean("dativo")));
				obTemp.setId_Serventia(String.valueOf(rs1.getString("ID_SERV")));
				obTemp.setServentia(String.valueOf(rs1.getString("SERV")));
				
				liTemp.add(obTemp);
			}
			// rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}

	/**
	 * Buscar processos partes advogados Ativos vinculados a um determinado id_usu_serv
	 * 
	 * @param id_Processo, identificação do processo
	 * @param id_Serventia, identificação da serventia do usuário logado
	 * @param id_usu_serv, indentificação do usuario serventia vinculado a parte
	 * @author lsbernardes
	 */
	public List consultarProcessosPartesAdvogados(String id_processo, String id_Serventia, String idUsuarioServentia) throws Exception {
		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT DISTINCT p.NOME_ADVOGADO, p.ID_USU_SERV_ADVOGADO, p.OAB_NUMERO, p.OAB_COMPLEMENTO ";
		Sql += " FROM VIEW_PROC_PARTE_ADVOGADO p ";
		Sql += " WHERE p.ID_PROC = ? AND p.ID_SERV = ? AND p.DATA_SAIDA IS NULL ";
		ps.adicionarLong(id_processo);
		ps.adicionarLong(id_Serventia);
		if(idUsuarioServentia != null) {
			Sql += " AND p.ID_USU_SERV_ADVOGADO = ? ";
			ps.adicionarLong(idUsuarioServentia);
		}
		Sql += " AND p.GRUPO_CODIGO NOT IN(?,?,?,?,?,?,?,?) "; 
		ps.adicionarLong(GrupoDt.COORDENADOR_HABILITACAO_MP);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROMOTORIA);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROCURADORIA_ESTADUAL);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROCURADORIA_MUNICIPAL);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROCURADORIA_FEDERAL);
		ps.adicionarLong(GrupoDt.COORDENADOR_ESCRITORIO_JURIDICO);
		ps.adicionarLong(GrupoDt.COORDENADOR_DEFENSORIA_PUBLICA);
		ps.adicionarLong(GrupoDt.COORDENADOR_ADVOCACIA_PUBLICA);
		Sql += " ORDER BY p.NOME_ADVOGADO ";
		
		try{
			rs1 = consultar(Sql, ps);
			while (rs1.next()) {
				ProcessoParteAdvogadoDt obTemp = new ProcessoParteAdvogadoDt();
				obTemp.setNomeAdvogado(rs1.getString("NOME_ADVOGADO"));
				obTemp.setId_UsuarioServentiaAdvogado(rs1.getString("ID_USU_SERV_ADVOGADO"));
				obTemp.setOabNumero(rs1.getString("OAB_NUMERO"));
				obTemp.setOabComplemento(rs1.getString("OAB_COMPLEMENTO"));

				liTemp.add(obTemp);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}

	/**
	 * Sobrescrevendo método inserir para setar DataEntrada com a data do banco
	 */
	public void inserir(ProcessoParteAdvogadoDt dados) throws Exception {

		String SqlCampos;
		String SqlValores;
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		dados.setCodigoTemp(String.valueOf(Math.round(Math.random() * 100000)));

		SqlCampos = "INSERT INTO PROJUDI.PROC_PARTE_ADVOGADO ("; 
		SqlValores = " Values (";		
		// if (!(dados.getNomeAdvogado().length() == 0)) Sql +=
		// "NOME_ADVOGADO";
		if (!(dados.getId_UsuarioServentiaAdvogado().length() == 0)){			
			SqlCampos+= "ID_USU_SERV " ;
			SqlValores+="?";
			ps.adicionarLong(dados.getId_UsuarioServentiaAdvogado());
		}
		if (!(dados.getId_ProcessoParte().length() == 0)){
			SqlCampos += ",ID_PROC_PARTE";
			SqlValores+=",?";
			ps.adicionarLong(dados.getId_ProcessoParte());
		}
		SqlCampos += ",DATA_ENTRADA";
		SqlValores+=",?";
		ps.adicionarDate(new Date());
		if (!(dados.getDataSaida().length() == 0)){
			SqlCampos += ",DATA_SAIDA";
			SqlValores+=",?";
			ps.adicionarDate(dados.getDataSaida());
		}
		if (!(dados.getPrincipal().length() == 0)){
			SqlCampos += ",PRINC";
			SqlValores+=",?";
			ps.adicionarBoolean(dados.getPrincipal());
		}
		if (!(dados.getDativo().length() == 0)){
			SqlCampos += ",DATIVO";
			SqlValores+=",?";
			ps.adicionarBoolean(dados.getDativo());
		}
		
		SqlCampos += ",RECEBE_INTIMACAO";
		SqlValores+=",?";
		ps.adicionarBoolean(dados.getRecebeIntimacao());		
		SqlCampos += ",CODIGO_TEMP ";
		SqlValores+=",?";
		ps.adicionarLong(dados.getCodigoTemp());
		// if (!(dados.getNomeAdvogado().length() == 0)) Sql +=
		// dados.getNomeAdvogado());		
		SqlCampos+=")";
 		SqlValores+=")"; 		

 		Sql = SqlCampos.replace("(,", "(") + SqlValores.replace("(,", "(");	

		dados.setId(executarInsert(Sql, "ID_PROC_PARTE_ADVOGADO", ps));

	}
	
	/**
	 * Sobrescrevendo método alterar
	 */
	public void alterar(ProcessoParteAdvogadoDt dados) throws Exception{
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql= "UPDATE PROJUDI.PROC_PARTE_ADVOGADO SET  ";
		Sql+= "NOME_ADVOGADO  = ?";
		ps.adicionarString(dados.getNomeAdvogado()); 
		Sql+= ",ID_USU_SERV_ADVOGADO  = ?";
		ps.adicionarLong(dados.getId_UsuarioServentiaAdvogado()); 
		Sql+= ",ID_PROC_PARTE  = ?";
		ps.adicionarLong(dados.getId_ProcessoParte()); 
		Sql+= ",DATA_ENTRADA  = ?";
		ps.adicionarDateTime(dados.getDataEntrada()); 
		Sql+= ",DATA_SAIDA  = ?";
		ps.adicionarDateTime(dados.getDataSaida()); 
		Sql+= ",PRINC  = ?";
		ps.adicionarBoolean(dados.getPrincipal()); 
		Sql += ",RECEBE_INTIMACAO = ?";
		ps.adicionarBoolean(dados.getRecebeIntimacao());
		Sql=Sql.replace("SET  ,","SET  ");
		Sql = Sql + " WHERE ID_PROC_PARTE_ADVOGADO  = ?";
		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(Sql,ps);
	}
	
	/**
	 * Método que atualiza o processo parte advogado de um processo
	 */
	public void atualizarProcessoParteAdvogado(String id_Processo, String id_UsuarioServentiaNovo, String id_UsuarioServentiaAtual, String id_ProcessoParte) throws Exception{
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql= "UPDATE PROJUDI.PROC_PARTE_ADVOGADO SET  ";
		Sql+= ",ID_USU_SERV  = ? ";
		ps.adicionarLong(id_UsuarioServentiaNovo);
		Sql=Sql.replace("SET  ,","SET  ");
		Sql = Sql + " WHERE ID_PROC_PARTE  = ? AND ID_USU_SERV = ?";
		ps.adicionarLong(id_ProcessoParte);
		ps.adicionarLong(id_UsuarioServentiaAtual);

		executarUpdateDelete(Sql, ps);

	} 

	/**
	 * Método que registra a saída de um advogado do processo Armazena data
	 * atual em campo DataSaida
	 */
	public void desabilitaAdvogado(String id_ProcessoParteAdvogado) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.PROC_PARTE_ADVOGADO SET DATA_SAIDA = ?";
		ps.adicionarDateTime(new Date());
		Sql += " WHERE ID_PROC_PARTE_ADVOGADO = ?";
		ps.adicionarLong(id_ProcessoParteAdvogado);
		
		executarUpdateDelete(Sql, ps);

	}
	
	/**
	 * Método que altera o promotor Substituto processual
	 */
	public void atualizarPromotorSubstitutoProcessual(String id_ProcessoParteAdvogado, String id_UsuarioServentiaAtual, String id_UsuarioServentiaNovo) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.PROC_PARTE_ADVOGADO SET ID_USU_SERV = ?";
		ps.adicionarLong(id_UsuarioServentiaNovo);
		Sql += " WHERE ID_PROC_PARTE_ADVOGADO = ? AND ID_USU_SERV =  ?";
		ps.adicionarLong(id_ProcessoParteAdvogado); ps.adicionarLong(id_UsuarioServentiaAtual);
		
		executarUpdateDelete(Sql, ps);

	}
	

	/**
	 * Método que registra que determinado advogado será principal em um
	 * processo
	 * 
	 * @throws Exception
	 */
	public void defineAdvogadoPrincipal(ProcessoParteAdvogadoDt processoParteAdvogadoDt) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.PROC_PARTE_ADVOGADO SET PRINC = ? ";
		ps.adicionarLong(1);
		Sql += " WHERE ID_PROC_PARTE_ADVOGADO = ?";
		ps.adicionarLong(processoParteAdvogadoDt.getId());
		
		executarUpdateDelete(Sql, ps);

	}

	/**
	 * Método que retira os advogado principal de uma parte
	 */
	public void removeAdvogadoPrincipalProcesso(String id_ProcessoParte) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		Sql = "UPDATE PROJUDI.PROC_PARTE_ADVOGADO pa";
		Sql += " set pa.PRINC = ? ";
		ps.adicionarLong(0);
		Sql += " WHERE pa.ID_PROC_PARTE = ?";
		ps.adicionarLong(id_ProcessoParte);

		executarUpdateDelete(Sql, ps);

	}

	/**
	 * Verifica se um usuário passado é advogado de um processo
	 * 
	 * @param id_UsuarioServentia
	 *            , usuário a verificar
	 * @param id_Processo
	 *            , identificação do processo
	 */
	public boolean isAdvogadoProcesso(String id_UsuarioServentia, String id_Processo) throws Exception {
		boolean boRetorno = false;
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT ppa.ID_PROC_PARTE_ADVOGADO FROM PROJUDI.PROC_PARTE_ADVOGADO ppa";
		Sql += " INNER JOIN PROJUDI.PROC_PARTE pp on ppa.ID_PROC_PARTE = pp.ID_PROC_PARTE";
		Sql += " WHERE pp.ID_PROC= ?";
		ps.adicionarLong(id_Processo);
		Sql += " AND ppa.ID_USU_SERV= ?";
		ps.adicionarLong(id_UsuarioServentia);
		Sql += "  AND ppa.DATA_SAIDA IS NULL";

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) boRetorno = true;
			// rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return boRetorno;
	}

	/**
	 * Consulta se determinado advogado (id_UsuarioServentia) é advogado de um
	 * processo e caso seja, quais os outros advogados habilitados para a mesma
	 * parte
	 */
	public List consultarAdvogadosProcessoParte(String id_processo, String id_UsuarioServentia) throws Exception {
		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = " SELECT * FROM PROJUDI.VIEW_PROC_PARTE_ADVOGADO ppa1";
		Sql += " WHERE ppa1.DATA_SAIDA IS NULL AND ppa1.ID_PROC_PARTE = ";
		Sql += " (SELECT MAX(ppa.ID_PROC_PARTE) FROM PROJUDI.PROC_PARTE_ADVOGADO ppa";
		Sql += " 	INNER JOIN PROJUDI.PROC_PARTE pp  on ppa.ID_PROC_PARTE = pp.ID_PROC_PARTE";
		Sql += " 	WHERE pp.ID_PROC=?";
		ps.adicionarLong(id_processo);
		Sql += " 	AND ppa.ID_USU_SERV=?";
		ps.adicionarLong(id_UsuarioServentia);
		Sql += "	AND ppa.DATA_SAIDA IS NULL)";
		Sql += " AND ppa1.GRUPO_CODIGO NOT IN(?,?,?,?,?,?,?,?) "; 
		ps.adicionarLong(GrupoDt.COORDENADOR_HABILITACAO_MP);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROMOTORIA);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROCURADORIA_ESTADUAL);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROCURADORIA_MUNICIPAL);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROCURADORIA_FEDERAL);
		ps.adicionarLong(GrupoDt.COORDENADOR_ESCRITORIO_JURIDICO);
		ps.adicionarLong(GrupoDt.COORDENADOR_DEFENSORIA_PUBLICA);
		ps.adicionarLong(GrupoDt.COORDENADOR_ADVOCACIA_PUBLICA);

		try{
			rs1 = consultar(Sql, ps);
			while (rs1.next()) {
				ProcessoParteAdvogadoDt obTemp = new ProcessoParteAdvogadoDt();
				obTemp.setId(rs1.getString("ID_PROC_PARTE_ADVOGADO"));
				obTemp.setNomeAdvogado(rs1.getString("NOME_ADVOGADO"));
				obTemp.setId_UsuarioServentiaAdvogado(rs1.getString("ID_USU_SERV_ADVOGADO"));
				obTemp.setUsuarioAdvogado(rs1.getString("USU_ADVOGADO"));
				obTemp.setId_ProcessoParte(rs1.getString("ID_PROC_PARTE"));
				obTemp.setNomeParte(rs1.getString("NOME_PARTE"));
				obTemp.setDataEntrada(Funcoes.FormatarData(rs1.getDateTime("DATA_ENTRADA")));
				obTemp.setDataSaida(Funcoes.FormatarData(rs1.getDateTime("DATA_SAIDA")));
				obTemp.setPrincipal(Funcoes.FormatarLogico(rs1.getString("PRINC")));
				obTemp.setOabNumero(rs1.getString("OAB_NUMERO"));
				obTemp.setOabComplemento(rs1.getString("OAB_COMPLEMENTO"));
				obTemp.setId_Processo(rs1.getString("ID_PROC"));
				obTemp.setProcessoNumero(Funcoes.formataNumeroProcesso(rs1.getString("PROC_NUMERO")));
				obTemp.setSegredoJusticao(rs1.getBoolean("SEGREDO_JUSTICA"));
				obTemp.setRecebeIntimacao(rs1.getBoolean("RECEBE_INTIMACAO"));
				liTemp.add(obTemp);
			}
			// rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}
	
	/**
	 * Método que altera o atributo Receber Intimação do advogado
	 * @param processoParteAdvogadoDt - dados do advogado a ser alterado
	 * @throws Exception
	 * @author hmgodinho
	 */
	public void modificarRecebimentoIntimacaoAdvogado(ProcessoParteAdvogadoDt processoParteAdvogadoDt) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.PROC_PARTE_ADVOGADO SET RECEBE_INTIMACAO = ?";
		ps.adicionarBoolean(processoParteAdvogadoDt.getRecebeIntimacao());
		Sql += " WHERE ID_PROC_PARTE_ADVOGADO = ?";
		ps.adicionarLong(processoParteAdvogadoDt.getId());

		executarUpdateDelete(Sql, ps);

	}
	
	/**
	 * Método que altera o atributo Dativo do advogado
	 * @param processoParteAdvogadoDt - dados do advogado a ser alterado
	 * @throws Exception
	 * @author lsbernardes
	 */
	public void modificarAdvogadoDativo(ProcessoParteAdvogadoDt processoParteAdvogadoDt) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.PROC_PARTE_ADVOGADO SET DATIVO = ?";
		ps.adicionarBoolean(processoParteAdvogadoDt.getDativo());
		Sql += " WHERE ID_PROC_PARTE_ADVOGADO = ?";
		ps.adicionarLong(processoParteAdvogadoDt.getId());

		executarUpdateDelete(Sql, ps);

	}
	
	/**
	 * Método que retorna a serventia e UF onde um advogado está habilitado para um id usuário serventia.
	 * @param Id_UsuarioServentiaAdvogado
	 * @return Serventia + UF
	 * @author mmgomes
	 * @throws Exception
	 */
	public String[] consultarServentiaUFHabilitacaoAdvogados(String Id_UsuarioServentiaAdvogado) throws Exception {
		String Sql;
		ResultSetTJGO rs1 = null;
		String[] serventia = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT s.ID_SERV, s.SERV, e.UF";
		Sql += " FROM PROJUDI.USU_SERV_OAB usoab";
		Sql += " JOIN PROJUDI.USU_SERV us ON usoab.ID_USU_SERV = us.ID_USU_SERV";
		Sql += " JOIN PROJUDI.USU u ON us.ID_USU = u.ID_USU";
		Sql += " JOIN PROJUDI.SERV s ON s.ID_SERV = us.ID_SERV";
		Sql += " JOIN PROJUDI.ESTADO e ON e.ID_ESTADO = s.ID_ESTADO_REPRESENTACAO";
		Sql += " WHERE usoab.ID_USU_SERV = ?";
		ps.adicionarLong(Id_UsuarioServentiaAdvogado);
		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				serventia = new String[2];
				serventia[0] = rs1.getString("ID_SERV");
				serventia[1] = rs1.getString("SERV") + " - " + rs1.getString("UF");
			}		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return serventia;
	}
	
	/**
	 * Método que retorna a serventia e UF onde um promotor está habilitado para um id usuário serventia.
	 * @param Id_UsuarioServentiaPromotor
	 * @return Serventia + UF
	 * @author lsbernardes
	 * @throws Exception
	 */
	public String[] consultarServentiaUFHabilitacaoPromotores(String Id_UsuarioServentiaPromotor) throws Exception {
		String Sql;
		ResultSetTJGO rs1 = null;
		String[] serventia = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT s.ID_SERV, s.SERV, e.UF";
		Sql += " FROM PROJUDI.USU_SERV us ";
		Sql += " JOIN PROJUDI.SERV s ON s.ID_SERV = us.ID_SERV";
		Sql += " JOIN PROJUDI.ESTADO e ON e.ID_ESTADO = s.ID_ESTADO_REPRESENTACAO";
		Sql += " WHERE us.ID_USU_SERV = ?";
		ps.adicionarLong(Id_UsuarioServentiaPromotor);
		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				serventia = new String[2];
				serventia[0] = rs1.getString("ID_SERV");
				serventia[1] = rs1.getString("SERV") + " - " + rs1.getString("UF");		
			}
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return serventia;
	}
	
	public void inserirSPG(ProcessoParteAdvogadoSPGDt dados) throws Exception {

		String SqlCampos;
		String SqlValores;
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		SqlCampos = "INSERT INTO PROJUDI.PROC_PARTE_ADVOGADO_SPG ("; 
		SqlValores = " Values (";		
								
		SqlCampos += ",ID_PROC";
		SqlValores+= ",?";
		ps.adicionarLong(dados.getId_Processo());		
		
		SqlCampos += ",ID_PROC_PARTE ";
		SqlValores+= ",?";
		ps.adicionarLong(dados.getId_Processo_Parte());
		
		SqlCampos += ",OAB_NUMERO ";
		SqlValores+= ",?";
		ps.adicionarLong(dados.getOAB_Numero());
		
		SqlCampos += ",OAB_UF ";
		SqlValores+= ",?";
		ps.adicionarString(dados.getOAB_UF());
		
		SqlCampos += ",NOME_COMPLETO ";
		SqlValores+= ",?";
		ps.adicionarString(dados.getNomeCompleto());
		
		SqlCampos += ",NOME_SIMPLIFICADO ";
		SqlValores+= ",?";
		ps.adicionarString(dados.getNomeSimplificado());
		
		SqlCampos += ",DATA_IMPORTACAO";
		SqlValores+= ",?";
		ps.adicionarDate(dados.getDataImportacao());
		
		if (dados.getOAB_Complemento() != null){
			SqlCampos += ",OAB_COMPLEMENTO";
			SqlValores+= ",?";
			ps.adicionarString(dados.getOAB_Complemento());
		}
		if (dados.getCPF() != null){
			SqlCampos += ",CPF";
			SqlValores+= ",?";
			ps.adicionarString(dados.getCPF());
		}
		if (dados.getEmail() != null){
			SqlCampos += ",EMAIL";
			SqlValores+= ",?";
			ps.adicionarString(dados.getEmail());
		}
		
		SqlCampos+=")";
 		SqlValores+=")"; 		

 		Sql = SqlCampos.replace("(,", "(") + SqlValores.replace("(,", "(");	

 		executarUpdateDelete(Sql, ps);
	}
	
	public List<ProcessoParteAdvogadoSPGDt> consulteSPG(String oabNumero, String oabUF, String oabComplemento, String nomeAdvogado, String cpfAdvogado) throws Exception {
		
		List<ProcessoParteAdvogadoSPGDt> listaDeAdvogadosSPG = new ArrayList<ProcessoParteAdvogadoSPGDt>();		
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = " SELECT * FROM PROJUDI.PROC_PARTE_ADVOGADO_SPG";
		Sql += " 	WHERE OAB_NUMERO = ?";
		ps.adicionarLong(oabNumero);
		Sql += " 	AND OAB_UF = ?";
		ps.adicionarString(oabUF);
		Sql += " 	AND OAB_COMPLEMENTO = ?";
		ps.adicionarString(oabComplemento);
		Sql += " UNION";
		Sql += " SELECT * FROM PROJUDI.PROC_PARTE_ADVOGADO_SPG";
		Sql += " 	WHERE OAB_NUMERO = ?";
		ps.adicionarLong(oabNumero);
		Sql += " 	AND OAB_UF = ?";
		ps.adicionarString(oabUF);
		Sql += " 	AND CPF = ?";
		ps.adicionarString(cpfAdvogado);
		Sql += " 	AND OAB_COMPLEMENTO IS NULL";
		Sql += " UNION";
		Sql += " SELECT * FROM PROJUDI.PROC_PARTE_ADVOGADO_SPG";
		Sql += " 	WHERE OAB_NUMERO = ?";
		ps.adicionarLong(oabNumero);
		Sql += " 	AND OAB_UF = ?";
		ps.adicionarString(oabUF);
		Sql += " 	AND NOME_SIMPLIFICADO = ?";
		ps.adicionarString(Funcoes.converteNomeSimplificado(nomeAdvogado));
		Sql += " 	AND CPF IS NULL";		
		Sql += " 	AND OAB_COMPLEMENTO IS NULL";

		try{
			rs1 = consultar(Sql, ps);
			
			while (rs1.next()) {
				ProcessoParteAdvogadoSPGDt obTemp = new ProcessoParteAdvogadoSPGDt();
				
				obTemp.setId_Processo(rs1.getString("ID_PROC"));
				obTemp.setId_Processo_Parte(rs1.getString("ID_PROC_PARTE"));
				obTemp.setOAB_Numero(rs1.getString("OAB_NUMERO"));
				obTemp.setOAB_UF(rs1.getString("OAB_UF"));
				obTemp.setNomeCompleto(rs1.getString("NOME_COMPLETO"));
				obTemp.setNomeSimplificado(rs1.getString("NOME_SIMPLIFICADO"));
				obTemp.setDataImportacao(Funcoes.FormatarData(rs1.getString("DATA_IMPORTACAO")));
				obTemp.setOAB_Complemento(rs1.getString("OAB_COMPLEMENTO"));
				obTemp.setCPF(rs1.getString("CPF"));
				obTemp.setEmail(rs1.getString("EMAIL"));
				
				listaDeAdvogadosSPG.add(obTemp);
			}		
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {}
		}
			
		
		return listaDeAdvogadosSPG;
	}
	
	
	public void excluirSPG(ProcessoParteAdvogadoSPGDt dados) throws Exception {

		String Sql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql= "DELETE FROM PROJUDI.PROC_PARTE_ADVOGADO_SPG";
		Sql += " 	WHERE ID_PROC = ?";
		ps.adicionarLong(dados.getId_Processo());
		Sql += " 	AND ID_PROC_PARTE = ?";
		ps.adicionarLong(dados.getId_Processo_Parte());
		Sql += " 	AND OAB_NUMERO = ?";
		ps.adicionarLong(dados.getOAB_Numero());
		Sql += " 	AND OAB_UF = ?";
		ps.adicionarString(dados.getOAB_UF());
		Sql += " 	AND NOME_SIMPLIFICADO = ?";
		ps.adicionarString(dados.getNomeSimplificado());
		if (dados.getOAB_Complemento() != null){
			Sql += " 	AND OAB_COMPLEMENTO = ?";
			ps.adicionarString(dados.getOAB_Complemento());
		}
		if (dados.getCPF() != null){
			Sql += " 	AND CPF = ?";
			ps.adicionarString(dados.getCPF());			
		}
		if (dados.getEmail() != null){
			Sql += " 	AND EMAIL = ?";
			ps.adicionarString(dados.getEmail());
		}
		
		executarUpdateDelete(Sql, ps);
	}

	public HashMap<String, HashMap<String, String>>  consultaAdvogadoComSustentacaoOralAbertoPJD(String idAudiProc) throws Exception {
		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		//lrcampos 12/07/2019 * Alterado a busca do advogado para a tabela de SUSTENTACAO_ORAL
		StringBuilder sql = new StringBuilder();
		HashMap<String, HashMap<String, String>> advogadosSustentacaoOralAbertaList = new HashMap<String, HashMap<String, String>>();
		
		sql.append(" SELECT DISTINCT  so.ID_SUSTENTACAO_ORAL, vppa.NOME_ADVOGADO,	vppa.NOME_PARTE FROM SUSTENTACAO_ORAL SO  ");
		sql.append(" INNER JOIN AUDI_PROC AP ON AP.ID_AUDI_PROC = SO.ID_AUDI_PROC  ");
		sql.append(" INNER JOIN PEND P ON P.ID_PEND = SO.ID_PEND ");
		sql.append(" INNER JOIN PROC_PARTE_ADVOGADO PPA ON PPA.ID_PROC_PARTE_ADVOGADO = SO.ID_PROC_PARTE_ADVOGADO ");
		sql.append(" INNER JOIN VIEW_PROC_PARTE_ADVOGADO VPPA ON (VPPA.ID_USU_SERV_ADVOGADO =  PPA.ID_USU_SERV AND VPPA.ID_PROC_PARTE = PPA.ID_PROC_PARTE) ");
		sql.append(" WHERE SO.ID_AUDI_PROC = ? ");
		ps.adicionarLong(idAudiProc);
		sql.append(" AND vppa.GRUPO_CODIGO NOT IN(?,?,?,?,?,?,?,?) "); 
		ps.adicionarLong(GrupoDt.COORDENADOR_HABILITACAO_MP);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROMOTORIA);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROCURADORIA_ESTADUAL);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROCURADORIA_MUNICIPAL);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROCURADORIA_FEDERAL);
		ps.adicionarLong(GrupoDt.COORDENADOR_ESCRITORIO_JURIDICO);
		ps.adicionarLong(GrupoDt.COORDENADOR_DEFENSORIA_PUBLICA);
		ps.adicionarLong(GrupoDt.COORDENADOR_ADVOCACIA_PUBLICA);

		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql.toString(), ps);
			
			while (rs1.next()) {
				HashMap<String, String> advogadosSustentacaoOralAberta = new HashMap<String, String>();
				advogadosSustentacaoOralAberta.put(rs1.getString("NOME_ADVOGADO"), rs1.getString("NOME_PARTE"));
				advogadosSustentacaoOralAbertaList.put(rs1.getString("ID_SUSTENTACAO_ORAL"), advogadosSustentacaoOralAberta);
			}
		} finally {
			if (rs1 != null)
				rs1.close();
		}
		return advogadosSustentacaoOralAbertaList;
		
	} 
	
	public HashMap<Long, List<ProcessoParteAdvogadoDt>> consultarAdvogadosProcessoMNI(String id_processo) throws Exception {
		String Sql;
		HashMap<Long, List<ProcessoParteAdvogadoDt>> dicAdvogados = new HashMap<Long, List<ProcessoParteAdvogadoDt>>();
		List<ProcessoParteAdvogadoDt> liTemp = null;
		Long ultimaParte = -1l;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = " SELECT p.*, ";
		
		Sql += " (SELECT s.ID_SERV || '__@--' || s.SERV || '-' || e.UF";
		Sql += "   FROM PROJUDI.USU_SERV_OAB usoab";
		Sql += "  JOIN PROJUDI.USU_SERV us ON usoab.ID_USU_SERV = us.ID_USU_SERV";
		Sql += "  JOIN PROJUDI.USU u ON us.ID_USU = u.ID_USU";
		Sql += "  JOIN PROJUDI.SERV s ON s.ID_SERV = us.ID_SERV";
		Sql += "  JOIN PROJUDI.ESTADO e ON e.ID_ESTADO = s.ID_ESTADO_REPRESENTACAO";
		Sql += "  WHERE usoab.ID_USU_SERV = p.ID_USU_SERV_ADVOGADO";
		Sql += "   AND ROWNUM = 1) AS DADOS_OAB_ADVOGADO,";
		
		Sql += " (SELECT s.ID_SERV || '__@--' || s.SERV || '-' || e.UF";
		Sql += "   FROM PROJUDI.USU_SERV us ";
		Sql += "  JOIN PROJUDI.SERV s ON s.ID_SERV = us.ID_SERV";
		Sql += "  JOIN PROJUDI.ESTADO e ON e.ID_ESTADO = s.ID_ESTADO_REPRESENTACAO";
		Sql += "  WHERE us.ID_USU_SERV = p.ID_USU_SERV_ADVOGADO";
		Sql += "   AND ROWNUM = 1) AS DADOS_OAB_PROMOTOR";
		
		Sql += " FROM VIEW_PROC_PARTE_ADVOGADO p ";
		Sql += " WHERE p.ID_PROC = ?"; ps.adicionarLong(id_processo);
		Sql += " AND p.DATA_SAIDA IS NULL";
		Sql += " AND p.GRUPO_CODIGO NOT IN(?,?,?,?,?,?,?,?) "; 
		ps.adicionarLong(GrupoDt.COORDENADOR_HABILITACAO_MP);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROMOTORIA);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROCURADORIA_ESTADUAL);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROCURADORIA_MUNICIPAL);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROCURADORIA_FEDERAL);
		ps.adicionarLong(GrupoDt.COORDENADOR_ESCRITORIO_JURIDICO);
		ps.adicionarLong(GrupoDt.COORDENADOR_DEFENSORIA_PUBLICA);
		ps.adicionarLong(GrupoDt.COORDENADOR_ADVOCACIA_PUBLICA);
		Sql += " ORDER BY p.ID_PROC_PARTE_TIPO";
		
		try{
			rs1 = consultar(Sql, ps);
			while (rs1.next()) {
				ProcessoParteAdvogadoDt obTemp = new ProcessoParteAdvogadoDt();
				
				this.associarDados(obTemp, rs1);
				
				obTemp.setProcessoParteTipo(rs1.getString("PROC_PARTE_TIPO"));
				obTemp.setDativo(rs1.getString("DATIVO"));
				obTemp.setEmail(rs1.getString("EMAIL"));
				
				String vetorDadosOab[] = null;
				if (!rs1.isNull("DADOS_OAB_ADVOGADO") && rs1.getString("DADOS_OAB_ADVOGADO").trim().length() > 0) {
					vetorDadosOab = rs1.getString("DADOS_OAB_ADVOGADO").split("__@--");
				}
				
				if (vetorDadosOab == null || vetorDadosOab.length != 2) {
					if (!rs1.isNull("DADOS_OAB_PROMOTOR") && rs1.getString("DADOS_OAB_PROMOTOR").trim().length() > 0) {
						vetorDadosOab = rs1.getString("DADOS_OAB_PROMOTOR").split("__@--");
					}
				}
				
				if (vetorDadosOab != null && vetorDadosOab.length == 2) {
					obTemp.setId_ServentiaHabilitacao(vetorDadosOab[0]);
					obTemp.setServentiaHabilitacao(vetorDadosOab[1]);
				}
				
				if (Funcoes.StringToLong(obTemp.getId_ProcessoParte()) != ultimaParte || liTemp == null) {
					ultimaParte = Funcoes.StringToLong(obTemp.getId_ProcessoParte());
					liTemp = new ArrayList<>();
					dicAdvogados.put(ultimaParte, liTemp);
				}
				
				liTemp.add(obTemp);
			}
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return dicAdvogados;
	}
}
