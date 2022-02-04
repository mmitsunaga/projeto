package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.CargoTipoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoResponsavelDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class ProcessoResponsavelPs extends ProcessoResponsavelPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = 881402503753044662L;

    public ProcessoResponsavelPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Sobrescrevendo método inserir para selecionar Id_CargoTipo de acordo com CargoTipoCodigo passado
	 */
	public void inserir(ProcessoResponsavelDt dados) throws Exception {

		String SqlCampos;
		String SqlValores;
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		SqlCampos = "INSERT INTO PROJUDI.PROC_RESP ("; 
		SqlValores = " Values (";
		if ((dados.getId_ServentiaCargo().length() > 0)){
			SqlCampos += "ID_SERV_CARGO ";
			SqlValores += "?";			ps.adicionarLong(dados.getId_ServentiaCargo());
		}
		if ((dados.getId_Processo().length() > 0)){
			SqlCampos += ",ID_PROC ";
			SqlValores += ",?";			ps.adicionarLong(dados.getId_Processo());
		}
		if ((dados.getRedator().length() > 0)){
			SqlCampos += ",Redator ";
			SqlValores += ",?";			ps.adicionarBoolean(dados.getRedator());
		}
		
		if ((dados.getId_CargoTipo().length() > 0) || dados.getId_CargoTipo().length() > 0){
			SqlCampos += ",ID_CARGO_TIPO ";
			SqlValores += ",?";			ps.adicionarLong(dados.getId_CargoTipo());
		}else if (dados.getCargoTipoCodigo().length() > 0) {
			SqlCampos += ",ID_CARGO_TIPO ";
			SqlValores += ", (SELECT ID_CARGO_TIPO FROM CARGO_TIPO WHERE CARGO_TIPO_CODIGO = ?)";
			ps.adicionarLong(dados.getCargoTipoCodigo());			
		}	
		
		SqlCampos+=")";
 		SqlValores+=")"; 		

 		Sql = SqlCampos.replace("(,", "(") + SqlValores.replace("(,", "(");	
				
		dados.setId(executarInsert(Sql, "ID_PROC_RESP", ps));
		
	}

	/**
	 * Verifica se determinado usuário é responsável em um processo, validando o serventiaCargo do usuário
	 * @param id_ServentiaCargo, serventiaCargo do usuário
	 * @param id_Processo, identificação do processo
	 * @author msapaula
	 * @throws Exception 
	 */
	public boolean isResponsavelProcesso(String id_ServentiaCargo, String id_Processo) throws Exception {
		boolean boRetorno = false;
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT pr.ID_PROC_RESP FROM PROJUDI.PROC_RESP pr";
		Sql += " WHERE pr.ID_PROC = ?";
		ps.adicionarLong(id_Processo);
		Sql += " AND pr.ID_SERV_CARGO = ?";
		ps.adicionarLong(id_ServentiaCargo);

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) boRetorno = true;
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return boRetorno;
	}

	/**
	 * Retorna o ServentiaCargo responsável por um processo, de um determinado tipo e em uma determinada serventia.
	 * 
	 * @param processoDt - identificação do processo
	 * @param id_serventia - identificação da serventia
	 * @param cargoTipoCodigo - tipo do responsável a ser retornado
	 * @param boAtivo - se o responsável está ativo ou inativo no processo
	 * @author msapaula, hmgodinho
	 */
	public ServentiaCargoDt consultarServentiaCargoResponsavelProcesso(String id_Processo, String id_Serventia, String grupoTipoCodigo, boolean boAtivo) throws Exception {
		ServentiaCargoDt serventiaCargoDt = null;
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT pr.ID_SERV_CARGO, sc.ID_USU_SERV, sc.SERV_CARGO, sc.NOME_USU, sc.ID_SERV, sc.SERV, sc.CARGO_TIPO_CODIGO";
		Sql += " FROM PROJUDI.VIEW_PROC_RESP pr";
		Sql += " JOIN PROJUDI.VIEW_SERV_CARGO sc on pr.ID_SERV_CARGO = sc.ID_SERV_CARGO";
		Sql += " WHERE pr.ID_PROC = ?";										ps.adicionarLong(id_Processo);
		if (boAtivo){
			Sql += " AND pr.CODIGO_TEMP = ?";								ps.adicionarLong(ProcessoResponsavelDt.ATIVO);
		}else{
			Sql += " AND pr.CODIGO_TEMP = ?";								ps.adicionarLong(ProcessoResponsavelDt.INATIVO);
		}
		if (grupoTipoCodigo != null && !grupoTipoCodigo.equals("")){
			Sql += " AND GRUPO_TIPO_USU_CODIGO = ?";						ps.adicionarLong(grupoTipoCodigo);
		}
		if (id_Serventia != null){
			Sql += " AND sc.ID_SERV = ?";									ps.adicionarLong(id_Serventia);
		}

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				serventiaCargoDt = new ServentiaCargoDt();
				serventiaCargoDt.setId(rs1.getString("ID_SERV_CARGO"));
				serventiaCargoDt.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
				serventiaCargoDt.setServentiaCargo(rs1.getString("SERV_CARGO"));
				serventiaCargoDt.setNomeUsuario(rs1.getString("NOME_USU"));
				serventiaCargoDt.setId_Serventia(rs1.getString("ID_SERV"));
				serventiaCargoDt.setServentia(rs1.getString("SERV"));
				serventiaCargoDt.setCargoTipoCodigo(rs1.getString("CARGO_TIPO_CODIGO"));
			}
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return serventiaCargoDt;
	}
	
//	/**
//	 * Retorna o ServentiaCargo de JUIZ UPJ responsável por um processo, de um determinado tipo e em uma determinada serventia.
//	 * OBS.: Deve ser retornado o ServentiaCargo responsável que está na serventia relacionada à serventia do processo.
//	 * 
//	 * @param processoDt identificação do processo
//	 * @param id_serventia identificação da serventia
//	 * @param cargoTipoCodigo tipo do responsável a ser retornado
//	 * @param boAtivo se cargo está ativo ou inativo
//	 * @author hmgodinho
//	 */
//	public ServentiaCargoDt consultarServentiaCargoResponsavelProcessoUPJ(String id_Processo, String id_Serventia, String grupoTipoCodigo, boolean boAtivo) throws Exception {
//		ServentiaCargoDt serventiaCargoDt = null;
//		String Sql;
//		ResultSetTJGO rs1 = null;
//		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
//
//		Sql = "SELECT pr.ID_SERV_CARGO, sc.ID_USU_SERV, sc.SERV_CARGO, sc.NOME_USU, sc.ID_SERV, sc.SERV, sc.CARGO_TIPO_CODIGO, sc.GRUPO_TIPO_USU_CODIGO";
//		Sql += " FROM PROJUDI.VIEW_PROC_RESP pr ";
//		Sql += " JOIN PROJUDI.VIEW_SERV_CARGO sc on sc.id_serv_cargo = pr.id_serv_cargo ";
//		Sql += " JOIN PROJUDI.SERV_RELACIONADA sr ON sr.id_serv_rel = sc.id_serv ";
//		Sql += " WHERE pr.ID_PROC = ?";										ps.adicionarLong(id_Processo);
//		if (boAtivo){
//			Sql += " AND pr.CODIGO_TEMP = ?";								ps.adicionarLong(ProcessoResponsavelDt.ATIVO);
//		}else{
//			Sql += " AND pr.CODIGO_TEMP = ?";								ps.adicionarLong(ProcessoResponsavelDt.INATIVO);
//		}
//		if (grupoTipoCodigo != null && !grupoTipoCodigo.equals("")){
//			Sql += " AND sc.GRUPO_TIPO_USU_CODIGO =  ?";						ps.adicionarLong(grupoTipoCodigo);
//		}
//		if (id_Serventia != null){
//			Sql += " AND sr.id_serv_princ = ?";									ps.adicionarLong(id_Serventia);
//		}
//		
//		try{
//			rs1 = consultar(Sql, ps);
//			if (rs1.next()) {
//				serventiaCargoDt = new ServentiaCargoDt();
//				serventiaCargoDt.setId(rs1.getString("ID_SERV_CARGO"));
//				serventiaCargoDt.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
//				serventiaCargoDt.setServentiaCargo(rs1.getString("SERV_CARGO"));
//				serventiaCargoDt.setNomeUsuario(rs1.getString("NOME_USU"));
//				serventiaCargoDt.setId_Serventia(rs1.getString("ID_SERV"));
//				serventiaCargoDt.setServentia(rs1.getString("SERV"));
//				serventiaCargoDt.setCargoTipoCodigo(rs1.getString("CARGO_TIPO_CODIGO"));
//			}
//		
//		} finally{
//			try{
//				if (rs1 != null) rs1.close();
//			} catch(Exception e) {
//			}
//		}
//		return serventiaCargoDt;
//	}
	
	/**
	 * Retorna o ServentiaCargo ativo responsável por um processo, de um determinado tipo e em uma determinada serventia.
	 * OBS.: Deve ser retornado o ServentiaCargo responsável que está ativo
	 * 
	 * @param processoDt identificação do processo
	 * @param id_serventia identificação da serventia
	 * @param cargoTipoCodigo tipo do responsável a ser retornado
	 * @author lsbernardes
	 */
	public ServentiaCargoDt consultarServentiaCargoAtivoResponsavelProcesso(String id_Processo, String id_Serventia, String grupoTipoCodigo, boolean boAtivo) throws Exception {
		ServentiaCargoDt serventiaCargoDt = null;
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT pr.ID_SERV_CARGO, sc.ID_USU_SERV, sc.SERV_CARGO, sc.NOME_USU, sc.ID_SERV, sc.SERV, sc.CARGO_TIPO_CODIGO";
		Sql += " FROM PROJUDI.VIEW_PROC_RESP pr";
		Sql += " JOIN PROJUDI.VIEW_SERV_CARGO sc on pr.ID_SERV_CARGO = sc.ID_SERV_CARGO";
		Sql += " WHERE pr.ID_PROC = ?";
		ps.adicionarLong(id_Processo);
		Sql += " AND sc.quantidade_dist > ?";
		ps.adicionarLong(0);
		
		if (boAtivo){
			Sql += " AND pr.CODIGO_TEMP = ?";
			ps.adicionarLong(ProcessoResponsavelDt.ATIVO);
		}else{
			Sql += " AND pr.CODIGO_TEMP = ?";
			ps.adicionarLong(ProcessoResponsavelDt.INATIVO);
		}
		if (grupoTipoCodigo != null && !grupoTipoCodigo.equals("")){
			Sql += " AND GRUPO_TIPO_USU_CODIGO = ?";
			ps.adicionarLong(grupoTipoCodigo);
		}
		if (id_Serventia != null){
			Sql += " AND sc.ID_SERV = ?";
			ps.adicionarLong(id_Serventia);
		}

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				serventiaCargoDt = new ServentiaCargoDt();
				serventiaCargoDt.setId(rs1.getString("ID_SERV_CARGO"));
				serventiaCargoDt.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
				serventiaCargoDt.setServentiaCargo(rs1.getString("SERV_CARGO"));
				serventiaCargoDt.setNomeUsuario(rs1.getString("NOME_USU"));
				serventiaCargoDt.setId_Serventia(rs1.getString("ID_SERV"));
				serventiaCargoDt.setServentia(rs1.getString("SERV"));
				serventiaCargoDt.setCargoTipoCodigo(rs1.getString("CARGO_TIPO_CODIGO"));
			}
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return serventiaCargoDt;
	}
	
	/**
	 * Retorna o ServentiaCargo responsável por um processo, de um determinado tipo e em uma determinada serventia.
	 * 
	 * @param id_Processo identificação do processo
	 * @param boSomenteAtivo identificação se deve consultar somente ativos serventia
	 * @author msapaula / mmgomes
	 */
	public List consultarServentiaCargoResponsavelProcesso(String id_Processo, boolean boSomenteAtivo) throws Exception {
		
		List tempList = new ArrayList<>();
		ServentiaCargoDt serventiaCargoDt = null;
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		
		Sql = "SELECT pr.ID_SERV_CARGO, us.ID_USU_SERV, sc.SERV_CARGO, u.NOME, s.ID_SERV, s.SERV, ct.CARGO_TIPO_CODIGO, pr.CODIGO_TEMP, S.ID_SERV_SUBTIPO";
		Sql += " FROM PROJUDI.PROC_RESP pr ";
		Sql += " INNER JOIN PROJUDI.SERV_CARGO sc on pr.ID_SERV_CARGO = sc.ID_SERV_CARGO";
		Sql += " LEFT JOIN PROJUDI.USU_SERV_GRUPO usg on usg.ID_USU_SERV_GRUPO = sc.ID_USU_SERV_GRUPO";
		Sql += " LEFT JOIN PROJUDI.USU_SERV us on us.ID_USU_SERV = usg.ID_USU_SERV";
		Sql += " LEFT JOIN PROJUDI.USU u on u.ID_USU = us.ID_USU";
		Sql += " INNER JOIN PROJUDI.CARGO_TIPO ct on ct.ID_CARGO_TIPO = sc.ID_CARGO_TIPO AND ct.CARGO_TIPO_CODIGO  in (?,?) "; ps.adicionarLong(CargoTipoDt.MINISTERIO_PUBLICO ); ps.adicionarLong(CargoTipoDt.MINISTERIO_PUBLICO_TCE ); 
		Sql += " LEFT JOIN PROJUDI.SERV s on s.ID_SERV = sc.ID_SERV";
		
		Sql += " WHERE pr.ID_PROC = ?";
		ps.adicionarLong(id_Processo);
		
		if (boSomenteAtivo){
			Sql += " AND pr.CODIGO_TEMP = ?"; ps.adicionarLong(ProcessoResponsavelDt.ATIVO);
		}
		
		try{
			rs1 = consultar(Sql, ps);
			while (rs1.next()) {
				serventiaCargoDt = new ServentiaCargoDt();
				serventiaCargoDt.setId(rs1.getString("ID_SERV_CARGO"));
				serventiaCargoDt.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
				serventiaCargoDt.setServentiaCargo(rs1.getString("SERV_CARGO"));
				serventiaCargoDt.setNomeUsuario(rs1.getString("NOME"));
				serventiaCargoDt.setId_Serventia(rs1.getString("ID_SERV"));
				serventiaCargoDt.setServentia(rs1.getString("SERV"));
				serventiaCargoDt.setCargoTipoCodigo(rs1.getString("CARGO_TIPO_CODIGO"));
				serventiaCargoDt.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
				serventiaCargoDt.setId_ServentiaSubtipo(rs1.getString("ID_SERV_SUBTIPO"));
				tempList.add(serventiaCargoDt);
			}
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return tempList;
	}

	/**
	 * Retorna o ServentiaCargo responsável por um processo do segundo Grau
	 * 
	 * @param processoDt identificação do processo
	 * @param id_serventia identificação da serventia
	 * @author lsbernardes
	 */
	public ServentiaCargoDt consultarServentiaCargoResponsavelProcessoSegundoGrau(String id_Processo, String id_Serventia, boolean boAtivo) throws Exception {
		ServentiaCargoDt serventiaCargoDt = null;
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		Sql = "	SELECT DISTINCT pr.ID_PROC_RESP, sc.ID_SERV_CARGO, sc.SERV_CARGO, sc.ID_SERV, ct.CARGO_TIPO_CODIGO ";
		Sql += " FROM  PROC_RESP pr ";
		Sql += " INNER JOIN SERV_CARGO sc on sc.ID_SERV_CARGO = pr.ID_SERV_CARGO ";
		Sql += " INNER JOIN SERV_RELACIONADA sr on sr.ID_SERV_REL = sc.ID_SERV  and sr.Recebe_proc = ? ";						ps.adicionarLong(1);      
		Sql += " INNER JOIN CARGO_TIPO ct on ct.ID_CARGO_TIPO = pr.ID_CARGO_TIPO ";
		Sql += " WHERE pr.ID_PROC = ? AND ct.CARGO_TIPO_CODIGO = ? ";
		Sql += " 	 AND sr.ID_SERV_PRINC = ? ";
		ps.adicionarLong(id_Processo);
		ps.adicionarLong(CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU);
		ps.adicionarLong(id_Serventia);
		if (boAtivo){
			Sql += " AND pr.CODIGO_TEMP = ?";
			ps.adicionarLong(ProcessoResponsavelDt.ATIVO);
		}else{
			Sql += " AND pr.CODIGO_TEMP = ?";
			ps.adicionarLong(ProcessoResponsavelDt.INATIVO);
		}
		
		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				serventiaCargoDt = new ServentiaCargoDt();
				serventiaCargoDt.setId(rs1.getString("ID_SERV_CARGO"));
				serventiaCargoDt.setServentiaCargo(rs1.getString("SERV_CARGO"));
				serventiaCargoDt.setId_Serventia(rs1.getString("ID_SERV"));
				serventiaCargoDt.setCargoTipoCodigo(rs1.getString("CARGO_TIPO_CODIGO"));
				serventiaCargoDt.setCodigoTemp(rs1.getString("ID_PROC_RESP"));
			}
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return serventiaCargoDt;
	}
	
	/**
	 * Retorna o ServentiaCargo responsável ATIVO por um processo do segundo Grau.
	 * 
	 * @param processoDt identificação do processo
	 * @param id_serventia identificação da serventia
	 * @author hmgodinho
	 */
	public ServentiaCargoDt consultarServentiaCargoResponsavelProcessoSegundoGrauAtivo(String id_Processo) throws Exception {
		ServentiaCargoDt serventiaCargoDt = null;
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		Sql = "	SELECT DISTINCT pr.ID_PROC_RESP, sc.ID_SERV_CARGO, sc.SERV_CARGO, sc.ID_SERV, ct.CARGO_TIPO_CODIGO ";
		Sql += "	FROM  PROC_RESP pr ";
		Sql += " INNER JOIN SERV_CARGO sc on sc.ID_SERV_CARGO = pr.ID_SERV_CARGO ";
		Sql += " INNER JOIN CARGO_TIPO ct on ct.ID_CARGO_TIPO = pr.ID_CARGO_TIPO ";
		Sql += " WHERE pr.ID_PROC = ? AND pr.CODIGO_TEMP =  ? AND ct.CARGO_TIPO_CODIGO = ?";
		ps.adicionarLong(id_Processo);
		ps.adicionarLong(ProcessoResponsavelDt.ATIVO);
		ps.adicionarLong(CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU);
		
		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				serventiaCargoDt = new ServentiaCargoDt();
				serventiaCargoDt.setId(rs1.getString("ID_SERV_CARGO"));
				serventiaCargoDt.setServentiaCargo(rs1.getString("SERV_CARGO"));
				serventiaCargoDt.setId_Serventia(rs1.getString("ID_SERV"));
				serventiaCargoDt.setCargoTipoCodigo(rs1.getString("CARGO_TIPO_CODIGO"));
				serventiaCargoDt.setCodigoTemp(rs1.getString("ID_PROC_RESP"));
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return serventiaCargoDt;
	}

	/**
	 * Retorna o ServentiaCargo Redator do processo, o que será o novo relator
	 * ou seja, que ficou por ultimo com o redator de um processo será o relator no retorno do processo.
	 * @param processoDt identificação do processo 
	 * @author jrcorrea
	 */
	
	public ServentiaCargoDt consultarProcessoResponsavelRedatorTurma(String id_Processo) throws Exception {
		ServentiaCargoDt serventiaCargoDt = null;
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		stSql = "SELECT pr.ID_SERV_CARGO, sc.SERV_CARGO, sc.NOME_USUARIO, sc.ID_SERV, sc.SERV  ";
		stSql += " FROM PROJUDI.VIEW_PROC_RESP pr";
		stSql += " JOIN PROJUDI.VIEW_SERV_CARGO sc on pr.ID_SERV_CARGO = sc.ID_SERV_CARGO";
		stSql += " WHERE pr.ID_PROC = ?";		ps.adicionarLong(id_Processo);
		stSql += " AND pr.CODIGO_TEMP = ?";		ps.adicionarLong(ProcessoResponsavelDt.ATIVO);
		stSql += " AND (pr.CARGO_TIPO_CODIGO = ? ";	ps.adicionarLong( CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU);
		stSql += " OR pr.CARGO_TIPO_CODIGO   = ? ";	ps.adicionarLong( CargoTipoDt.REVISOR_SEGUNDO_GRAU);
		stSql += " OR pr.CARGO_TIPO_CODIGO   = ?)";	ps.adicionarLong( CargoTipoDt.VOGAL_SEGUNDO_GRAU);
		stSql += " AND pr.REDATOR = ?";			ps.adicionarLong(1);				
		
		try{
			rs1 = consultar(stSql, ps);
			if (rs1.next()) {
				serventiaCargoDt = new ServentiaCargoDt();
				serventiaCargoDt.setId(rs1.getString("ID_SERV_CARGO"));
				serventiaCargoDt.setServentiaCargo(rs1.getString("SERV_CARGO"));
				serventiaCargoDt.setNomeUsuario(rs1.getString("NOME_USUARIO"));
				serventiaCargoDt.setId_Serventia(rs1.getString("ID_SERV"));
				serventiaCargoDt.setServentia(rs1.getString("SERV"));
			}
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return serventiaCargoDt;
	}
	
	/**
	 * Retorna o ServentiaCargo Redator do processo, o que será o novo relator
	 * ou seja, que ficou por ultimo como redator de um processo será o relator no retorno do processo.
	 * @param processoDt identificação do processo 
	 * @author jrcorrea
	 */
	
	public ServentiaCargoDt consultarProcessoResponsavel2Grau(String id_Processo, int cargoTipo, boolean boAtivo) throws Exception {
		ServentiaCargoDt serventiaCargoDt = null;
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		stSql = "SELECT DISTINCT pr.ID_SERV_CARGO, sc.SERV_CARGO, u.NOME as NOME_USUARIO, sc.ID_SERV, sc.QUANTIDADE_DIST, s.SERV";   
		stSql += " FROM PROJUDI.PROC_RESP pr ";
		stSql += " INNER JOIN PROJUDI.PROC p on pr.ID_PROC = p.ID_PROC "; 
		stSql += " INNER JOIN PROJUDI.CARGO_TIPO ct on ct.ID_CARGO_TIPO = pr.ID_CARGO_TIPO";
		stSql += " INNER JOIN PROJUDI.SERV_CARGO sc on sc.ID_SERV_CARGO = pr.ID_SERV_CARGO";  
		stSql += " INNER JOIN PROJUDI.SERV s on s.ID_SERV = sc.ID_SERV";
		stSql += " INNER JOIN PROJUDI.USU_SERV_GRUPO usg on usg.ID_USU_SERV_GRUPO = sc.ID_USU_SERV_GRUPO"; 
		stSql += " INNER JOIN PROJUDI.USU_SERV us on us.ID_USU_SERV = usg.ID_USU_SERV";
		stSql += " INNER JOIN PROJUDI.USU u on u.ID_USU = us.ID_USU";
		    	
		stSql += " WHERE pr.ID_PROC = ?";		ps.adicionarLong(id_Processo);
		stSql += " AND pr.CODIGO_TEMP = ?";		
		if(boAtivo) {
			ps.adicionarLong(ProcessoResponsavelDt.ATIVO);
		} else {
			ps.adicionarLong(ProcessoResponsavelDt.INATIVO);
		}
		stSql += " AND ct.CARGO_TIPO_CODIGO = ? ";	ps.adicionarLong( cargoTipo);							
		
		try{
			rs1 = consultar(stSql, ps);
			if (rs1.next()) {
				serventiaCargoDt = new ServentiaCargoDt();
				serventiaCargoDt.setId(rs1.getString("ID_SERV_CARGO"));
				serventiaCargoDt.setServentiaCargo(rs1.getString("SERV_CARGO"));
				serventiaCargoDt.setNomeUsuario(rs1.getString("NOME_USUARIO"));
				serventiaCargoDt.setId_Serventia(rs1.getString("ID_SERV"));
				serventiaCargoDt.setServentia(rs1.getString("SERV"));
				serventiaCargoDt.setQuantidadeDistribuicao(rs1.getString("QUANTIDADE_DIST"));
			}
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return serventiaCargoDt;
	}
	
	/**
	 * Retorna o ServentiaCargo Redator do processo, o que será o novo relator
	 * ou seja, que ficou por ultimo com o redator de um processo será o relator no retorno do processo.
	 * @param processoDt identificação do processo
	 * @param id_serventia identificação da serventia
	 * @author jrcorrea
	 */
	
	public ServentiaCargoDt consultarProcessoResponsavelRedator2Grau(String id_Processo) throws Exception {
		ServentiaCargoDt serventiaCargoDt = null;
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		stSql = "SELECT pr.ID_SERV_CARGO, sc.SERV_CARGO, sc.NOME_USU, sc.ID_SERV, sc.SERV  ";
		stSql += " FROM PROJUDI.VIEW_PROC_RESP pr";
		stSql += " JOIN PROJUDI.VIEW_SERV_CARGO sc on pr.ID_SERV_CARGO = sc.ID_SERV_CARGO";
		stSql += " WHERE pr.ID_PROC = ?";		ps.adicionarLong(id_Processo);
		stSql += " AND pr.REDATOR = ?";			ps.adicionarLong(1);			
		stSql += " AND pr.CODIGO_TEMP = ?";		ps.adicionarLong(ProcessoResponsavelDt.ATIVO);
		stSql += " AND (pr.CARGO_TIPO_CODIGO = ? ";	ps.adicionarLong( CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU);
		stSql += " OR pr.CARGO_TIPO_CODIGO   = ? ";	ps.adicionarLong( CargoTipoDt.REVISOR_SEGUNDO_GRAU);
		stSql += " OR pr.CARGO_TIPO_CODIGO   = ?)";	ps.adicionarLong( CargoTipoDt.VOGAL_SEGUNDO_GRAU);
					
		try{
			rs1 = consultar(stSql, ps);
			if (rs1.next()) {
				serventiaCargoDt = new ServentiaCargoDt();
				serventiaCargoDt.setId(rs1.getString("ID_SERV_CARGO"));
				serventiaCargoDt.setServentiaCargo(rs1.getString("SERV_CARGO"));
				serventiaCargoDt.setNomeUsuario(rs1.getString("NOME_USU"));
				serventiaCargoDt.setId_Serventia(rs1.getString("ID_SERV"));
				serventiaCargoDt.setServentia(rs1.getString("SERV"));
			}
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return serventiaCargoDt;
	}
	
	/**
	 * Retorna o ServentiaCargo Redator do processo, o que será o novo relator
	 * ou seja, que ficou por ultimo com o redator de um processo será o relator no retorno do processo.
	 * @param processoDt identificação do processo
	 * @param id_serventia identificação da serventia
	 * @author jrcorrea
	 */
	
	public ServentiaCargoDt consultarRelator2Grau(String id_Processo) throws Exception {
		ServentiaCargoDt serventiaCargoDt = null;
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		stSql = "SELECT pr.ID_SERV_CARGO, sc.SERV_CARGO, sc.NOME_USU, sc.ID_SERV, sc.SERV  ";
		stSql += " FROM PROJUDI.VIEW_PROC_RESP pr";
		stSql += " JOIN PROJUDI.VIEW_SERV_CARGO sc on pr.ID_SERV_CARGO = sc.ID_SERV_CARGO";
		stSql += " WHERE pr.ID_PROC = ?";		ps.adicionarLong(id_Processo);
		stSql += " AND pr.CODIGO_TEMP = ?";		ps.adicionarLong(ProcessoResponsavelDt.ATIVO);
		stSql += " AND pr.CARGO_TIPO_CODIGO = ? ";	ps.adicionarLong( CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU);
					
		try{
			rs1 = consultar(stSql, ps);
			if (rs1.next()) {
				serventiaCargoDt = new ServentiaCargoDt();
				serventiaCargoDt.setId(rs1.getString("ID_SERV_CARGO"));
				serventiaCargoDt.setServentiaCargo(rs1.getString("SERV_CARGO"));
				serventiaCargoDt.setNomeUsuario(rs1.getString("NOME_USU"));
				serventiaCargoDt.setId_Serventia(rs1.getString("ID_SERV"));
				serventiaCargoDt.setServentia(rs1.getString("SERV"));
			}
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return serventiaCargoDt;
	}
	
	/**
	 * Retorna o ServentiaCargo responsável por um processo, de um determinado tipo e em uma determinada serventia.
	 * Esse método irá buscar o CargoTipo da própria tabela ProcessoResponsavel, para os casos do 2º grau
	 * onde o Juiz pode atuar como Relator, Vogal e Revisor e seu ServentiaCargo é somente do tipo Desembargador
	 * 
	 * @param processoDt identificação do processo
	 * @param id_serventia identificação da serventia
	 * @param cargoTipoCodigo tipo do responsável a ser retornado
	 * @author msapaula
	 */
	public ServentiaCargoDt consultarProcessoResponsavelCargoTipo1(String id_Processo, String id_Serventia, String cargoTipoCodigo) throws Exception {
		ServentiaCargoDt serventiaCargoDt = null;
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT pr.ID_SERV_CARGO, sc.SERV_CARGO, sc.NOME_USU, sc.ID_SERV, sc.SERV  ";
		Sql += " FROM PROJUDI.VIEW_PROC_RESP pr";
		Sql += " JOIN PROJUDI.VIEW_SERV_CARGO sc on pr.ID_SERV_CARGO = sc.ID_SERV_CARGO";
		Sql += " WHERE pr.ID_PROC = ?";					ps.adicionarLong(id_Processo);
		Sql += " AND pr.CODIGO_TEMP = ?";					ps.adicionarLong(ProcessoResponsavelDt.ATIVO);
		Sql += " AND pr.CARGO_TIPO_CODIGO = ?";			ps.adicionarLong(cargoTipoCodigo);
		if (id_Serventia != null) {
			Sql += " AND sc.ID_SERV = ?";
			ps.adicionarLong(id_Serventia);
		}

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				serventiaCargoDt = new ServentiaCargoDt();
				serventiaCargoDt.setId(rs1.getString("ID_SERV_CARGO"));
				serventiaCargoDt.setServentiaCargo(rs1.getString("SERV_CARGO"));
				serventiaCargoDt.setNomeUsuario(rs1.getString("NOME_USU"));
				serventiaCargoDt.setId_Serventia(rs1.getString("ID_SERV"));
				serventiaCargoDt.setServentia(rs1.getString("SERV"));
			}
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return serventiaCargoDt;
	}

	/**
	 * Consulta os responsáveis por um processo
	 * 
	 * @param id_Processo, identificação do processo
	 * @author msapaula
	 */
	public List consultarResponsaveisProcesso(String id_Processo) throws Exception {
		String sql;
		String cargoTipoCodigoResponsavel;
		List lista = new ArrayList();
		ResultSetTJGO rs1 = null;		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql = "SELECT pr.ID_SERV_CARGO, sc.CARGO_TIPO, sc.QUANTIDADE_DIST, sc.NOME_USU, sc.ID_SERV, sc.SERV, ";
		sql += " pr.CARGO_TIPO as CARGO_TIPO_RESP, pr.CARGO_TIPO_CODIGO as CARGO_TIPO_CODIGO_RESP, sc.SERV_CARGO, sc.GRUPO_TIPO_USU_CODIGO, s.SERV_SUBTIPO_CODIGO ";
		sql += " FROM PROJUDI.VIEW_PROC_RESP pr";
		sql += " JOIN PROJUDI.VIEW_SERV_CARGO sc on pr.ID_SERV_CARGO = sc.ID_SERV_CARGO";
		sql += " JOIN PROJUDI.VIEW_SERV s on s.ID_SERV = sc.ID_SERV";
		sql += " WHERE pr.ID_PROC = ?" ;
		ps.adicionarLong(id_Processo);
		sql += " AND sc.CARGO_TIPO_CODIGO <> ?";
		ps.adicionarLong(CargoTipoDt.ASSISTENTE_GABINETE);
//		sql += " AND pr.CargoTipoCodigo not IN ( ?, ? )";
//		ps.adicionarLong(CargoTipoDt.REVISOR_SEGUNDO_GRAU);
//		ps.adicionarLong(CargoTipoDt.VOGAL_SEGUNDO_GRAU);
		sql += " AND pr.CODIGO_TEMP = ?";
		ps.adicionarLong(ProcessoResponsavelDt.ATIVO);

		try{
			rs1 = consultar(sql, ps);
			while (rs1.next()) {
				ServentiaCargoDt serventiaCargoDt = new ServentiaCargoDt();
				serventiaCargoDt.setId(rs1.getString("ID_SERV_CARGO"));
				cargoTipoCodigoResponsavel = rs1.getString("CARGO_TIPO_CODIGO_RESP");
				serventiaCargoDt.setCargoTipoCodigo(rs1.getString("CARGO_TIPO_CODIGO_RESP"));
				//Se for o presidente ou o vice-presidente do tribunal retorna o serventia cargo
				if (rs1.getString("GRUPO_TIPO_USU_CODIGO") != null && rs1.getString("GRUPO_TIPO_USU_CODIGO").equalsIgnoreCase(String.valueOf(GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU)) && 
					rs1.getString("SERV_SUBTIPO_CODIGO") != null && (rs1.getString("SERV_SUBTIPO_CODIGO").equalsIgnoreCase(String.valueOf(ServentiaSubtipoDt.GABINETE_PRESIDENCIA_TJGO)) ||
							                                         rs1.getString("SERV_SUBTIPO_CODIGO").equalsIgnoreCase(String.valueOf(ServentiaSubtipoDt.GABINETE_VICE_PRESIDENCIA_TJGO)) ||
							                                         rs1.getString("SERV_SUBTIPO_CODIGO").equalsIgnoreCase(String.valueOf(ServentiaSubtipoDt.GABINETE_PRESIDENCIA_ORGAO))) ) {								
					serventiaCargoDt.setCargoTipo(rs1.getString("SERV_CARGO"));
				} else if (cargoTipoCodigoResponsavel != null && cargoTipoCodigoResponsavel.length() > 0 && 
						(Funcoes.StringToInt(cargoTipoCodigoResponsavel) == CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU || 
					     Funcoes.StringToInt(cargoTipoCodigoResponsavel) == CargoTipoDt.REVISOR_SEGUNDO_GRAU || Funcoes.StringToInt(cargoTipoCodigoResponsavel) == CargoTipoDt.VOGAL_SEGUNDO_GRAU)) {
					//Se for revisor ou vogal retorna a descrição concatenada
					serventiaCargoDt.setCargoTipo(rs1.getString("CARGO_TIPO") + "-" + rs1.getString("CARGO_TIPO_RESP"));
				} else {
					serventiaCargoDt.setCargoTipo(rs1.getString("CARGO_TIPO"));
				}

				serventiaCargoDt.setQuantidadeDistribuicao(rs1.getString("QUANTIDADE_DIST"));
				serventiaCargoDt.setNomeUsuario(rs1.getString("NOME_USU"));
				serventiaCargoDt.setId_Serventia(rs1.getString("ID_SERV"));
				serventiaCargoDt.setServentia(rs1.getString("SERV"));
				lista.add(serventiaCargoDt);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return lista;
	}
	
	/**
	 * Método que consulta todos os responsáveis pelo processo, exceto usuários do MP.
	 * @param id_Processo - ID do processo
	 * @return lista de responsáveis
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarTodosResponsaveisProcesso(String id_Processo) throws Exception {
		String sql;
		ResultSetTJGO rs1 = null;
		List lista = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		sql = "SELECT pr.ID_PROC_RESP, pr.SERV_CARGO, pr.CARGO_TIPO, sc.NOME_USU, sc.SERV, pr.CODIGO_TEMP "; 
		sql += " FROM PROJUDI.VIEW_PROC_RESP pr";
		sql += " JOIN PROJUDI.VIEW_SERV_CARGO sc on pr.ID_SERV_CARGO = sc.ID_SERV_CARGO";
		sql += " WHERE pr.ID_PROC = ?" ;
		ps.adicionarLong(id_Processo);
		sql += " 	AND pr.CARGO_TIPO_CODIGO <> ?" ;
		ps.adicionarLong(CargoTipoDt.MINISTERIO_PUBLICO);

		try{
			rs1 = consultar(sql, ps);
			while (rs1.next()) {							
				ProcessoResponsavelDt processoResponsavelDt = new ProcessoResponsavelDt();
				processoResponsavelDt.setId(rs1.getString("ID_PROC_RESP"));
				processoResponsavelDt.setCargoTipo(rs1.getString("SERV_CARGO") + " - " + rs1.getString("CARGO_TIPO"));
				processoResponsavelDt.setNomeUsuario(rs1.getString("NOME_USU"));
				processoResponsavelDt.setServentiaCargo(rs1.getString("SERV"));
				processoResponsavelDt.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
				lista.add(processoResponsavelDt);
			}			
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return lista;
	}
	
	/**
	 * Consulta os responsáveis desabilitados de um processo
	 * 
	 * @param id_Processo, identificação do processo
	 * @return lista de responsáveis
	 * @author hmgodinho
	 */
	public List consultarResponsaveisDesabilitadosProcesso(String id_Processo) throws Exception {
		String sql;
		String cargoTipoCodigoResponsavel;
		List lista = new ArrayList();
		ResultSetTJGO rs1 = null;		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql = "SELECT pr.ID_SERV_CARGO, sc.CARGO_TIPO, sc.QUANTIDADE_DIST, sc.NOME_USU, sc.ID_SERV, sc.SERV, ";
		sql += " pr.CARGO_TIPO as CARGO_TIPO_RESP, pr.CARGO_TIPO_CODIGO as CARGO_TIPO_CODIGO_RESP, sc.SERV_CARGO, sc.GRUPO_TIPO_USU_CODIGO, s.SERV_SUBTIPO_CODIGO ";
		sql += " FROM PROJUDI.VIEW_PROC_RESP pr";
		sql += " JOIN PROJUDI.VIEW_SERV_CARGO sc on pr.ID_SERV_CARGO = sc.ID_SERV_CARGO";
		sql += " JOIN PROJUDI.VIEW_SERV s on s.ID_SERV = sc.ID_SERV";
		sql += " WHERE pr.ID_PROC = ?" ;
		ps.adicionarLong(id_Processo);
		sql += " AND sc.CARGO_TIPO_CODIGO <> ?";
		ps.adicionarLong(CargoTipoDt.ASSISTENTE_GABINETE);
		sql += " AND pr.CODIGO_TEMP = ?";
		ps.adicionarLong(ProcessoResponsavelDt.INATIVO);

		try{
			rs1 = consultar(sql, ps);
			while (rs1.next()) {
				ServentiaCargoDt serventiaCargoDt = new ServentiaCargoDt();
				serventiaCargoDt.setId(rs1.getString("ID_SERV_CARGO"));
				cargoTipoCodigoResponsavel = rs1.getString("CARGO_TIPO_CODIGO_RESP");
				serventiaCargoDt.setCargoTipoCodigo(rs1.getString("CARGO_TIPO_CODIGO_RESP"));
				//Se for o presidente ou o vice-presidente do tribunal retorna o serventia cargo
				if (rs1.getString("GRUPO_TIPO_USU_CODIGO") != null && rs1.getString("GRUPO_TIPO_USU_CODIGO").equalsIgnoreCase(String.valueOf(GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU)) && 
					rs1.getString("SERV_SUBTIPO_CODIGO") != null && (rs1.getString("SERV_SUBTIPO_CODIGO").equalsIgnoreCase(String.valueOf(ServentiaSubtipoDt.GABINETE_PRESIDENCIA_TJGO)) ||
							                                         rs1.getString("SERV_SUBTIPO_CODIGO").equalsIgnoreCase(String.valueOf(ServentiaSubtipoDt.GABINETE_VICE_PRESIDENCIA_TJGO))) ) {								
					serventiaCargoDt.setCargoTipo(rs1.getString("SERV_CARGO"));
				} else if (cargoTipoCodigoResponsavel != null && cargoTipoCodigoResponsavel.length() > 0 && 
						(Funcoes.StringToInt(cargoTipoCodigoResponsavel) == CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU || 
					     Funcoes.StringToInt(cargoTipoCodigoResponsavel) == CargoTipoDt.REVISOR_SEGUNDO_GRAU || Funcoes.StringToInt(cargoTipoCodigoResponsavel) == CargoTipoDt.VOGAL_SEGUNDO_GRAU)) {
					//Se for revisor ou vogal retorna a descrição concatenada
					serventiaCargoDt.setCargoTipo(rs1.getString("CARGO_TIPO") + "-" + rs1.getString("CARGO_TIPO_RESP"));
				} else {
					serventiaCargoDt.setCargoTipo(rs1.getString("CARGO_TIPO"));
				}

				serventiaCargoDt.setQuantidadeDistribuicao(rs1.getString("QUANTIDADE_DIST"));
				serventiaCargoDt.setNomeUsuario(rs1.getString("NOME_USU"));
				serventiaCargoDt.setId_Serventia(rs1.getString("ID_SERV"));
				serventiaCargoDt.setServentia(rs1.getString("SERV"));
				lista.add(serventiaCargoDt);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return lista;
	}
	
	public List consultarJuizesSegundoGrauResponsaveisProcesso(String id_Processo) throws Exception {
		String stSql;
		List lista = new ArrayList();
		ResultSetTJGO rs1 = null;
		String cargoTipoCodigoResponsavel = "";
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		stSql = "SELECT pr.ID_SERV_CARGO, sc.CARGO_TIPO, sc.NOME_USU, sc.ID_SERV, sc.SERV, ";
		stSql += " pr.CARGO_TIPO as CARGO_TIPO_RESP, pr.CARGO_TIPO_CODIGO as CARGO_TIPO_CODIGO_RESP";
		stSql += " FROM PROJUDI.VIEW_PROC_RESP pr";
		stSql += " JOIN PROJUDI.VIEW_SERV_CARGO sc on pr.ID_SERV_CARGO = sc.ID_SERV_CARGO";
		stSql += " WHERE pr.ID_PROC = ?";				ps.adicionarLong(id_Processo);
		stSql += " AND sc.GRUPO_TIPO_USU_CODIGO = ?";	ps.adicionarLong(GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU);
		stSql += " AND pr.CODIGO_TEMP = ?";			ps.adicionarLong(ProcessoResponsavelDt.ATIVO);
//		stSql += " and pr.Redator = ?";				ps.adicionarLong(1);
		try{
			rs1 = consultar(stSql, ps);
			while (rs1.next()) {
				ServentiaCargoDt serventiaCargoDt = new ServentiaCargoDt();
				serventiaCargoDt.setId(rs1.getString("ID_SERV_CARGO"));
				cargoTipoCodigoResponsavel = rs1.getString("CARGO_TIPO_CODIGO_RESP");
				//Se for revisor ou vogal retorna a descrição concatenada
				serventiaCargoDt.setCargoTipoCodigo(cargoTipoCodigoResponsavel);
				if (cargoTipoCodigoResponsavel != null && cargoTipoCodigoResponsavel.length() > 0 && (Funcoes.StringToInt(cargoTipoCodigoResponsavel) == CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU || Funcoes.StringToInt(cargoTipoCodigoResponsavel) == CargoTipoDt.REVISOR_SEGUNDO_GRAU || Funcoes.StringToInt(cargoTipoCodigoResponsavel) == CargoTipoDt.VOGAL_SEGUNDO_GRAU)) {
					serventiaCargoDt.setCargoTipo(rs1.getString("CARGO_TIPO") + "-" + rs1.getString("CARGO_TIPO_RESP"));
				} else serventiaCargoDt.setCargoTipo(rs1.getString("CARGO_TIPO"));

				serventiaCargoDt.setNomeUsuario(rs1.getString("NOME_USU"));
				serventiaCargoDt.setId_Serventia(rs1.getString("ID_SERV"));
				serventiaCargoDt.setServentia(rs1.getString("SERV"));
				lista.add(serventiaCargoDt);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return lista;
	}
	
	public List consultarJuizSegundoGrauGabinete(String id_Serventia) throws Exception {
		String sql;
		List lista = new ArrayList();
		ResultSetTJGO rs1 = null;
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		
		sql = "Select Sc.Id_Serv_Cargo, C.Cargo_Tipo, C.Cargo_Tipo_Codigo , u.nome, s.serv, s.id_serv";
		sql += "  From Serv_Cargo Sc "; 
		sql += "      Inner Join Cargo_Tipo C On Sc.Id_Cargo_Tipo = C.Id_Cargo_Tipo ";
		sql += "      Inner Join Usu_Serv_Grupo Usg On Sc.Id_Usu_Serv_Grupo = Usg.Id_Usu_Serv_Grupo ";
		sql += "      Inner Join Usu_Serv Us On Usg.Id_Usu_Serv =Us.Id_Usu_Serv ";
		sql += "      Inner Join Usu U On Us.Id_Usu = U.Id_Usu ";
		sql += "      inner join serv s on us.id_serv = s.id_serv ";
		sql += " where sc.id_serv = ? and c.cargo_tipo_codigo = ? "; 		ps.adicionarLong(id_Serventia);			ps.adicionarLong(CargoTipoDt.DESEMBARGADOR);
	    
		try{
			rs1 = consultar(sql, ps);
			while (rs1.next()) {
				ServentiaCargoDt serventiaCargoDt = new ServentiaCargoDt();
				serventiaCargoDt.setId(rs1.getString("ID_SERV_CARGO"));				
				serventiaCargoDt.setCargoTipo(rs1.getString("CARGO_TIPO"));

				serventiaCargoDt.setNomeUsuario(rs1.getString("nome"));
				serventiaCargoDt.setId_Serventia(rs1.getString("ID_SERV"));
				serventiaCargoDt.setServentia(rs1.getString("SERV"));
				lista.add(serventiaCargoDt);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return lista;
	}
	
	public List consultarJuizesTurma(String id_Serventia) throws Exception {
		String sql;
		List lista = new ArrayList();
		ResultSetTJGO rs1 = null;
		String cargoTipoCodigoResponsavel = "";
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql = "SELECT sc.ID_SERV_CARGO, sc.CARGO_TIPO, sc.NOME_USU, sc.ID_SERV, sc.SERV, ";
		sql += " sc.CARGO_TIPO as CARGO_TIPO_RESP, sc.CARGO_TIPO_CODIGO as CARGO_TIPO_CODIGO_RESP";
		sql += " FROM PROJUDI.VIEW_SERV_CARGO sc ";
		sql += " WHERE sc.ID_SERV  = ?";
		ps.adicionarLong(id_Serventia);
		sql += " AND sc.GRUPO_TIPO_USU_CODIGO = ?";		
		ps.adicionarLong(GrupoTipoDt.JUIZ_TURMA);
		

		try{
			rs1 = consultar(sql, ps);
			while (rs1.next()) {
				ServentiaCargoDt serventiaCargoDt = new ServentiaCargoDt();
				serventiaCargoDt.setId(rs1.getString("ID_SERV_CARGO"));
				cargoTipoCodigoResponsavel = rs1.getString("CARGO_TIPO_CODIGO_RESP");
				//Se for revisor ou vogal retorna a descrição concatenada
				if (cargoTipoCodigoResponsavel != null && cargoTipoCodigoResponsavel.length() > 0 && (Funcoes.StringToInt(cargoTipoCodigoResponsavel) == CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU || Funcoes.StringToInt(cargoTipoCodigoResponsavel) == CargoTipoDt.REVISOR_SEGUNDO_GRAU || Funcoes.StringToInt(cargoTipoCodigoResponsavel) == CargoTipoDt.VOGAL_SEGUNDO_GRAU)) {
					serventiaCargoDt.setCargoTipo(rs1.getString("CARGO_TIPO"));
				} else serventiaCargoDt.setCargoTipo(rs1.getString("CARGO_TIPO"));

				serventiaCargoDt.setNomeUsuario(rs1.getString("NOME_USU"));
				serventiaCargoDt.setId_Serventia(rs1.getString("ID_SERV"));
				serventiaCargoDt.setServentia(rs1.getString("SERV"));
				lista.add(serventiaCargoDt);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return lista;
	}
	
	/**
	 * Consulta os responsáveis por um processo, que estarão disponíveis para acesso externo.
	 * Até agora não serão visíveis para acesso externo os Assistentes de Gabinete responsáveis pelo processo
	 * 
	 * @param id_Processo, identificação do processo
	 * @author msapaula
	 * @author jrcorrea
	 */
	public List consultarResponsaveisProcessoAcessoExterno(String id_Processo) throws Exception {
		String stSql;
		List lista = new ArrayList();
		ResultSetTJGO rs1 = null;
		String cargoTipoResponsavel = "";
		String cargoTipo = "";
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		stSql = "SELECT pr.ID_SERV_CARGO, ct.CARGO_TIPO, ct1.CARGO_TIPO as CARGO_TIPO_RESPONSAVEL, sc.QUANTIDADE_DIST, u.NOME, s.ID_SERV, s.SERV, ct.CARGO_TIPO_CODIGO as CARGO_TIPO_CODIGO ";		
		stSql += " FROM PROJUDI.PROC_RESP pr";
		stSql += " INNER JOIN PROJUDI.SERV_CARGO sc on pr.ID_SERV_CARGO = sc.ID_SERV_CARGO";
		stSql += " INNER JOIN PROJUDI.CARGO_TIPO ct on sc.ID_CARGO_TIPO = ct.ID_CARGO_TIPO";
		stSql += " INNER JOIN PROJUDI.CARGO_TIPO ct1 on pr.ID_CARGO_TIPO = ct1.ID_CARGO_TIPO";		
		stSql += " INNER JOIN PROJUDI.USU_SERV_GRUPO usg on sc.ID_USU_SERV_GRUPO = usg.ID_USU_SERV_GRUPO"; 
		stSql += " INNER JOIN PROJUDI.USU_SERV us on usg.ID_USU_SERV = us.ID_USU_SERV"; 
		stSql += " INNER JOIN PROJUDI.USU u on us.ID_USU = u.ID_USU";
		stSql += " INNER JOIN PROJUDI.PROC p on pr.ID_PROC = p.ID_PROC";
		stSql += " INNER JOIN PROJUDI.SERV s on s.ID_SERV = sc.ID_SERV";		
		
		stSql += " WHERE pr.ID_PROC = ?"; ps.adicionarLong(id_Processo);
		
		stSql += " AND ct.CARGO_TIPO_CODIGO <> ?";ps.adicionarLong(CargoTipoDt.ASSISTENTE_GABINETE);
		stSql += " AND ct.CARGO_TIPO_CODIGO <> ?";ps.adicionarLong(CargoTipoDt.REVISOR_SEGUNDO_GRAU);
		stSql += " AND ct.CARGO_TIPO_CODIGO <> ?";ps.adicionarLong(CargoTipoDt.VOGAL_SEGUNDO_GRAU);
		stSql += " AND pr.CODIGO_TEMP = ?"; ps.adicionarLong(ProcessoResponsavelDt.ATIVO);

		try{
			rs1 = consultar(stSql, ps);
			while (rs1.next()) {
				ServentiaCargoDt serventiaCargoDt = new ServentiaCargoDt();		
				serventiaCargoDt.setId(rs1.getString("ID_SERV_CARGO"));
				cargoTipoResponsavel = rs1.getString("CARGO_TIPO_RESPONSAVEL");
				cargoTipo = rs1.getString("CARGO_TIPO");
				
//				//Se for revisor ou vogal retorna a descrição concatenada
//				if (cargoTipoCodigoResponsavel != null && cargoTipoCodigoResponsavel.length() > 0 && (Funcoes.StringToInt(cargoTipoCodigoResponsavel) == CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU || Funcoes.StringToInt(cargoTipoCodigoResponsavel) == CargoTipoDt.REVISOR_SEGUNDO_GRAU || Funcoes.StringToInt(cargoTipoCodigoResponsavel) == CargoTipoDt.VOGAL_SEGUNDO_GRAU)) {
//					serventiaCargoDt.setCargoTipo(rs1.getString("CARGO_TIPO") + "-" + rs1.getString("CARGO_TIPO_RESPONSAVEL"));
//				} else 
				if (cargoTipoResponsavel.equalsIgnoreCase(cargoTipo))
					serventiaCargoDt.setCargoTipo(rs1.getString("CARGO_TIPO"));
				else
					serventiaCargoDt.setCargoTipo(cargoTipo + " - " + cargoTipoResponsavel );
				
				serventiaCargoDt.setCargoTipoCodigo(rs1.getString("CARGO_TIPO_CODIGO"));				
				serventiaCargoDt.setQuantidadeDistribuicao(rs1.getString("QUANTIDADE_DIST"));
				serventiaCargoDt.setNomeUsuario(rs1.getString("NOME"));
				serventiaCargoDt.setId_Serventia(rs1.getString("ID_SERV"));
				serventiaCargoDt.setServentia(rs1.getString("SERV"));

				lista.add(serventiaCargoDt);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return lista;
	}
	
	/**
	 * Método responsável por desativar os servidores do gabinete responsáveis por um processo.
	 * 
	 * @param idProcesso, identificação de processo
	 * @param idServentia, identificação do gabinete
	 * 
	 * @author hmgodinho
	 */
	public void desativarGabineteResponsavelProcesso(String idProcesso, String idServentia) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE (SELECT pr.CODIGO_TEMP, pr.ID_PROC, sc.ID_SERV FROM PROJUDI.PROC_RESP pr";
		Sql += " JOIN PROJUDI.SERV_CARGO sc on pr.ID_SERV_CARGO = sc.ID_SERV_CARGO AND sc.ID_SERV = ?) tb"; ps.adicionarLong(idServentia);	
		Sql += " SET tb.CODIGO_TEMP = ?";			ps.adicionarLong(ProcessoResponsavelDt.INATIVO);
		Sql += " WHERE tb.ID_PROC = ?";   			ps.adicionarLong(idProcesso);	
		Sql += " AND tb.CODIGO_TEMP = ?";         ps.adicionarLong(ProcessoResponsavelDt.ATIVO);

		executarUpdateDelete(Sql, ps);
	}
	
	/**
	 * Método responsável por reativar os servidores do gabinete responsáveis por um processo.
	 * 
	 * @param idProcesso, identificação de processo
	 * @param idServentia, identificação do gabinete
	 * @param obFabricaConexao conexão ativa
	 * 
	 * @author hmgodinho
	 */
	public void ativarGabineteResponsavelProcesso(String idProcesso, String idServentia) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE (SELECT pr.CODIGO_TEMP, pr.ID_PROC, sc.ID_SERV FROM PROJUDI.PROC_RESP pr";
		Sql += " JOIN PROJUDI.SERV_CARGO sc on pr.ID_SERV_CARGO = sc.ID_SERV_CARGO AND sc.ID_SERV = ?) tb"; ps.adicionarLong(idServentia);	
		Sql += " SET tb.CODIGO_TEMP = ?";			ps.adicionarLong(ProcessoResponsavelDt.ATIVO);
		Sql += " WHERE tb.ID_PROC = ?";   			ps.adicionarLong(idProcesso);	
		Sql += " AND tb.CODIGO_TEMP = ?";         ps.adicionarLong(ProcessoResponsavelDt.INATIVO);

		executarUpdateDelete(Sql, ps);
	}

	/**
	 * Método responsável em desativar os responsáveis de um processo de acordo com as serventias passadas
	 * 
	 * @param id_Processo, identificação do processo
	 * @param serventiasDesativar, serventias na qual os responsáveis deverão ser desativados
	 * @author msapaula
	 */
	public void desativarResponsaveisProcesso(String id_Processo, List serventiasDesativar) throws Exception {
		String Sql;
		String Sql1 = "";
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE (SELECT pr.CODIGO_TEMP, pr.ID_PROC, pr.REDATOR, sc.ID_SERV FROM PROJUDI.PROC_RESP pr";
		Sql += " JOIN PROJUDI.SERV_CARGO sc on pr.ID_SERV_CARGO = sc.ID_SERV_CARGO) tb";
		Sql += " SET tb.CODIGO_TEMP = ?";			ps.adicionarLong(ProcessoResponsavelDt.INATIVO);
        Sql += " , tb.Redator = ? ";				ps.adicionarLong(ProcessoResponsavelDt.PROCESSO_RESPONSAVEL_REDATOR_INATIVO);
		Sql += " WHERE tb.ID_PROC = ?";   			ps.adicionarLong(id_Processo);	
		
		for (int i = 0; i < serventiasDesativar.size(); i++) {
			Sql1 += + Sql1.length()>0?" OR tb.ID_SERV = ? ":" tb.ID_SERV = ? " ;	    ps.adicionarString(serventiasDesativar.get(i).toString());			
		}
		if (Sql1.length()>0) Sql+= " AND ( " + Sql1 + " )";
		
		Sql += " AND tb.CODIGO_TEMP = ?";         ps.adicionarLong(ProcessoResponsavelDt.ATIVO);

			executarUpdateDelete(Sql, ps);
	}
	
	/**
	 * Método responsável em ativar os responsáveis de um processo de acordo com as serventias passadas
	 * 
	 * @param id_Processo, identificação do processo
	 * @param serventiasAtivar, serventias na qual os responsáveis deverão ser ativados
	 * @author msapaula
	 */
	public int ativarResponsaveisProcesso(String id_Processo, List serventiasAtivar) throws Exception {
		String Sql;
		String Sql1 = "";
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int inTemp = 0;
		
		try{
			Sql = "UPDATE (SELECT pr.CODIGO_TEMP, pr.ID_PROC, sc.ID_SERV FROM PROJUDI.PROC_RESP pr";
			Sql += " JOIN PROJUDI.SERV_CARGO sc on pr.ID_SERV_CARGO = sc.ID_SERV_CARGO) tb";
			Sql += " SET tb.CODIGO_TEMP = ?"; ps.adicionarLong(ProcessoResponsavelDt.ATIVO);
			Sql += " WHERE tb.ID_PROC = ?"; ps.adicionarLong(id_Processo);
			
			for (int i = 0; i < serventiasAtivar.size(); i++) {
				Sql1 += Sql1.length()>0?" OR tb.ID_SERV = ? ":" tb.ID_SERV = ? " ;	    ps.adicionarString(serventiasAtivar.get(i).toString());			
			}
			if (Sql1.length()>0) Sql+= " AND ( " + Sql1 + " )";
			
			Sql += " AND tb.CODIGO_TEMP = ?"; ps.adicionarLong(ProcessoResponsavelDt.INATIVO);
			
			inTemp=executarUpdateDelete(Sql, ps);
			
		} catch (Exception e){
			throw new MensagemException("Não foi possível localizar responsável para o processo na serventia de destino. Favor contatar a Corregedoria.");
		}
		return inTemp;
	}
	
	/**
	 * Método responsável por reativar os responsáveis de um determinado processo de todas as serventias relacionadas à serventia destino do processo, 
	 * EXCETO DESEMBARGADORES e JUIZES DE TURMAS RECURSAIS que serão ativados separadamente.
	 * @param id_Processo - ID do processo
	 * @param serventiasAtivar - lista de serventias relacionadas
	 * @param obFabricaConexao - conexão
	 * @return qtde de registros alterados
	 * @throws Exception
	 */
	public int reativarResponsaveisProcesso(String id_Processo, List serventiasAtivar) throws Exception {
		String Sql;
		String Sql1 = "";
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int inTemp = 0;
		
		try{
			Sql = "UPDATE PROC_RESP pr1 SET pr1.CODIGO_TEMP = ? "; ps.adicionarLong(ProcessoResponsavelDt.ATIVO);
			Sql += " WHERE pr1.ID_PROC = ? AND pr1.ID_PROC_RESP IN (SELECT MAX(pr.ID_PROC_RESP) AS ID_PROC_RESP FROM PROJUDI.PROC_RESP pr "; ps.adicionarLong(id_Processo);
			Sql += " JOIN PROJUDI.SERV_CARGO sc on pr.ID_SERV_CARGO = sc.ID_SERV_CARGO ";
			Sql += " WHERE pr.ID_PROC = ?"; ps.adicionarLong(id_Processo);
			
			for (int i = 0; i < serventiasAtivar.size(); i++) {
				Sql1 += Sql1.length()>0?" OR sc.ID_SERV = ? ":" sc.ID_SERV = ? " ;	    ps.adicionarString(serventiasAtivar.get(i).toString());			
			}
			if (Sql1.length()>0) Sql+= " AND ( " + Sql1 + " )";
			
			Sql += " AND pr.CODIGO_TEMP = ?"; ps.adicionarLong(ProcessoResponsavelDt.INATIVO);
			Sql += " AND pr.ID_CARGO_TIPO <> ? "; ps.adicionarLong(CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU);
			
			Sql += "GROUP BY pr.ID_CARGO_TIPO, pr.CODIGO_TEMP ) ";
			
			inTemp=executarUpdateDelete(Sql, ps);
			
		} catch (Exception e){
			throw new MensagemException("Não foi possível localizar responsável para o processo na serventia de destino. Favor contatar a Corregedoria.");
		}
		return inTemp;
	}
	
	/**
	 * Método responsável por reativar o desembargador responsável por um determinado processo.
	 * @param id_Processo - ID do processo
	 * @param id_NovaServentia - ID da serventia de destino
	 * @param obFabricaConexao - conexão
	 * @return qtde de registros alterados
	 * @throws Exception
	 */
	public int reativarMagistradoResposavelProcesso(String id_Processo, String id_serventiaCargo_novo) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int inTemp = 0;
		
		try{
			Sql = "UPDATE PROC_RESP SET CODIGO_TEMP = ? "; 	ps.adicionarLong(ProcessoResponsavelDt.ATIVO);
			Sql += ", REDATOR = ? ";						ps.adicionarLong(ProcessoResponsavelDt.PROCESSO_RESPONSAVEL_REDATOR_ATIVO);
			Sql += " WHERE ID_PROC = ?"; 					ps.adicionarLong(id_Processo);
			Sql += " AND ID_SERV_CARGO = ? "; 				ps.adicionarLong(id_serventiaCargo_novo);
			
			inTemp=executarUpdateDelete(Sql, ps);
			
		} catch (Exception e){
			throw new MensagemException("Não foi possível localizar responsável para o processo na serventia de destino. Favor contatar a Corregedoria.");
		}
		return inTemp;
	}
	
	/**
	 * Método para consultar relator ativo do processo. Método foi criado para atender necessidade de se consultar
	 * os dados do relator enquanto se realiza envio para instância superior, por isso se passa a conexão como parâmetro.
	 * @param idServentia
	 * @param conexao
	 * @return id do relator
	 * @throws Exception
	 */
	public String consultarRelatorProcesso(String idProcesso) throws Exception {
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String idRelator = null;

		Sql = "SELECT pr.ID_PROC_RESP, sc.ID_SERV_CARGO FROM PROJUDI.PROC_RESP pr ";
		Sql += " JOIN PROJUDI.SERV_CARGO sc on pr.ID_SERV_CARGO = sc.ID_SERV_CARGO ";
		Sql += " JOIN PROJUDI.CARGO_TIPO ct on sc.ID_CARGO_TIPO = ct.ID_CARGO_TIPO ";
		Sql += " WHERE pr.CODIGO_TEMP = ?"; ps.adicionarLong(ProcessoResponsavelDt.ATIVO);
		Sql += "  	AND pr.ID_PROC = ?"; ps.adicionarLong(idProcesso);
		Sql += "	AND pr.ID_CARGO_TIPO IN (SELECT id_cargo_tipo FROM cargo_tipo WHERE CARGO_TIPO_CODIGO = ?) "; ps.adicionarLong(CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU);			

		try{
			rs1 = consultar(Sql,ps);
			while (rs1.next()) {
				idRelator = rs1.getString("ID_SERV_CARGO");
			}
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return idRelator;
	}
	
	/**
	 * Método responsável em ativar os redator de um processo de acordo com as serventias passadas
	 * 
	 * @param id_Processo, identificação do processo
	 * @param serventiasAtivar, serventias na qual os responsáveis deverão ser ativados
	 * @author lsbernardes
	 */
	public void ativarRedatorProcesso(String id_Processo, List serventiasAtivar) throws Exception {
		String Sql;
		String Sql1 = "";
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE (SELECT pr.CODIGO_TEMP, pr.ID_PROC, sc.ID_SERV, ct.CARGO_TIPO_CODIGO, pr.REDATOR FROM PROJUDI.PROC_RESP pr";
		Sql += " JOIN PROJUDI.SERV_CARGO sc on pr.ID_SERV_CARGO = sc.ID_SERV_CARGO";
		Sql += " JOIN PROJUDI.CARGO_TIPO ct on sc.ID_CARGO_TIPO = ct.ID_CARGO_TIPO ) tb";
		Sql += " SET  tb.Redator = ? ";		ps.adicionarLong(ProcessoResponsavelDt.PROCESSO_RESPONSAVEL_REDATOR_ATIVO);
		Sql += " WHERE tb.ID_PROC = ?"; ps.adicionarLong(id_Processo);
		
		for (int i = 0; i < serventiasAtivar.size(); i++) {
			Sql1 += Sql1.length()>0?" OR tb.ID_SERV = ? ":" tb.ID_SERV = ? " ;	ps.adicionarString(serventiasAtivar.get(i).toString());			
		}
		if (Sql1.length()>0) Sql+= " AND ( " + Sql1 + " )";
	
		Sql += " AND tb.CODIGO_TEMP = ?"; ps.adicionarLong(ProcessoResponsavelDt.ATIVO);
		Sql += " AND tb.CARGO_TIPO_CODIGO IN (?,?)";
		ps.adicionarLong(CargoTipoDt.JUIZ_1_GRAU);
		ps.adicionarLong(CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU);

			executarUpdateDelete(Sql, ps);
	}
	
	/**
	 * Método responsável por retirar um redator do processo
	 * 
	 * @param id_Processo, identificação do processo
	 * @author lsbernardes
	 */
	public void desativarRedatorProcesso(String id_Processo) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = " update PROC_RESP pr SET pr.REDATOR = ? where pr.ID_PROC_RESP IN ";
		Sql += " ( select  pr.id_Proc_Resp from PROC_RESP pr inner join CARGO_TIPO ct on pr.ID_CARGO_TIPO = ct.ID_CARGO_TIPO where pr.ID_PROC = ? and pr.REDATOR = ?  and pr.CODIGO_TEMP <> ? and ct.CARGO_TIPO_CODIGO in ( ?, ?, ? ) ) ";
		
		ps.adicionarLong(0);
		ps.adicionarLong(id_Processo);
		ps.adicionarLong(1);
		ps.adicionarLong(-1);
		ps.adicionarLong(8); ps.adicionarLong(17); ps.adicionarLong(18);

			executarUpdateDelete(Sql, ps);
	}

	/**
	 * Atualiza os dados de um ProcessoResponsavel em virtude de uma troca de responsável pelo processo
	 * 
	 * @param id_Processo, identificação do processo
	 * @param id_ResponsavelAnterior, responsável anterior
	 * @param id_NovoResponsavel, novo responsável
	 * 
	 * @author msapaula
	 */
	public void alterarResponsavelProcesso(String id_Processo, String id_ResponsavelAnterior, String id_NovoResponsavel) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		if (id_NovoResponsavel==null || id_NovoResponsavel.length()==0){
			throw new MensagemException("Não foi possível determinar o novo Responsável. Favor tentar novamente.");
		}
		Sql = "UPDATE PROJUDI.PROC_RESP SET ID_SERV_CARGO = ?";			ps.adicionarLong(id_NovoResponsavel);
		Sql += " WHERE ID_PROC = ?";									ps.adicionarLong(id_Processo);
		Sql += " AND ID_SERV_CARGO = ?";								ps.adicionarLong(id_ResponsavelAnterior);

			executarUpdateDelete(Sql, ps);
	}
	
	/**
	 * Método que faz a altera o status do redator do processo.
	 * @param idProcesso -  ID do processo
	 * @param idResponsavelProcesso - ID do responsável que deixa de ser ou se tornará redator
	 * @param novoStatus - true se for redator e false se for deixar de ser redator
	 * @throws Exception
	 * @author hmgodinho
	 */
	public void alterarStatusRedatorProcesso(String idProcesso, String idResponsavelProcesso, boolean novoStatus) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.PROC_RESP SET REDATOR = ?";
		ps.adicionarBoolean(novoStatus);
		Sql += " WHERE ID_SERV_CARGO = ?";
		ps.adicionarLong(idResponsavelProcesso);
		Sql += " AND ID_PROC = ?";
		ps.adicionarLong(idProcesso);

			executarUpdateDelete(Sql, ps);
	}

	/**
	 * Retorna o Id_UsuarioServentia de um tipo de Responsável pelo processo em uma determinada serventia.
	 * 
	 * @param id_processo, identificação de processo
	 * @param id_serventia, serventia para a qual quer encontrar o juiz responsável pelo processo
	 * @param cargoTipoCodigo, tipo do responsável a ser retornado
	 * @author msapaula
	 */
	public String consultarUsuarioResponsavelProcesso(String id_Processo, String id_Serventia, int cargoTipoCodigo) throws Exception {
		String sql;
		String stRetorno = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql = "SELECT sc.ID_USU_SERV FROM PROJUDI.VIEW_PROC_RESP pr";
		sql += " JOIN PROJUDI.VIEW_SERV_CARGO sc on pr.ID_SERV_CARGO = sc.ID_SERV_CARGO";
		sql += " WHERE pr.ID_PROC = ?";
		ps.adicionarLong(id_Processo);
		sql += " AND sc.CARGO_TIPO_CODIGO = ?";
		ps.adicionarLong(cargoTipoCodigo);
		if (id_Serventia != null) {
			sql += " AND sc.ID_SERV = ?";
			ps.adicionarLong(id_Serventia);
		}
		sql += " AND pr.CODIGO_TEMP = ?";
		ps.adicionarLong(ProcessoResponsavelDt.ATIVO);

		try{
			rs1 = consultar(sql, ps);
			if (rs1.next()) stRetorno = rs1.getString("ID_USU_SERV");
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return stRetorno;
	}

	/**
	 * Retorna o Id_UsuarioServentia de um tipo de Responsável pelo processo em uma determinada serventia.
	 * 
	 * @param id_processo, identificação de processo
	 * @param id_serventia, serventia para a qual quer encontrar o juiz responsável pelo processo 
	 * @author jesus Rodrigo
	 * @date 10/10/2017
	 */
	public String consultarJuizResponsavelProcesso(String id_Processo, String id_Serventia) throws Exception {
		String sql;
		String stRetorno = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql = "SELECT sc.ID_USU_SERV FROM PROJUDI.VIEW_PROC_RESP pr";
		sql += " JOIN PROJUDI.VIEW_SERV_CARGO sc on pr.ID_SERV_CARGO = sc.ID_SERV_CARGO";
		sql += " WHERE pr.ID_PROC = ?";
		ps.adicionarLong(id_Processo);
		sql += " AND sc.CARGO_TIPO_CODIGO in (?,?,?,?,?)";	
		ps.adicionarLong(CargoTipoDt.JUIZ_EXECUCAO); ps.adicionarLong(CargoTipoDt.JUIZ_1_GRAU); ps.adicionarLong(CargoTipoDt.JUIZ_INFANCIA_JUVENTUDE_CIVEL); 
		ps.adicionarLong(CargoTipoDt.JUIZ_INFANCIA_JUVENTUDE_INFRACIONAL); ps.adicionarLong(CargoTipoDt.JUIZ_UPJ);
		if (id_Serventia != null) {
			sql += " AND sc.ID_SERV = ?";
			ps.adicionarLong(id_Serventia);
		}
		sql += " AND pr.CODIGO_TEMP = ?";
		ps.adicionarLong(ProcessoResponsavelDt.ATIVO);

		try{
			rs1 = consultar(sql, ps);
			if (rs1.next()) stRetorno = rs1.getString("ID_USU_SERV");
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return stRetorno;
	}
	/**
	 * Retorna o Id_UsuarioServentia de um responsável pelo processo, de um determinado tipo e em uma determinada serventia.
	 * Esse método irá buscar o CargoTipo da própria tabela ProcessoResponsavel, para os casos do 2º grau
	 * onde o Juiz pode atuar como Relator, Vogal e Revisor e seu ServentiaCargo é somente do tipo Desembargador
	 * 
	 * @param processoDt identificação do processo
	 * @param id_serventia identificação da serventia
	 * @param cargoTipoCodigo tipo do responsável a ser retornado
	 * @author msapaula
	 */
	public String consultarUsuarioResponsavelCargoTipo(String id_Processo, String id_Serventia, int cargoTipoCodigo) throws Exception {
		String stRetorno = null;
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT sc.ID_USU_SERV FROM PROJUDI.VIEW_PROC_RESP pr";
		Sql += " JOIN PROJUDI.VIEW_SERV_CARGO sc on pr.ID_SERV_CARGO = sc.ID_SERV_CARGO";
		Sql += " WHERE pr.ID_PROC = ?"; ps.adicionarLong(id_Processo);
		Sql += " AND pr.CODIGO_TEMP = ?"; ps.adicionarLong(ProcessoResponsavelDt.ATIVO);
		Sql += " AND pr.CARGO_TIPO_CODIGO = ?"; ps.adicionarLong(cargoTipoCodigo);
		if (id_Serventia != null){
			Sql += " AND sc.ID_SERV = ?";
			ps.adicionarLong(id_Serventia);
		}

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) stRetorno = rs1.getString("ID_USU_SERV");
			
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return stRetorno;
	}
	
	/**
	 * Retorna uma lista de ServentiaCargo responsável por um processo, de um determinado tipo e em uma determinada serventia.
	 * OBS.: Deve ser retornado uma lista de ServentiaCargo responsável que estão ativos
	 * 
	 * @param processoDt identificação do processo
	 * @param id_serventia identificação da serventia
	 * @param cargoTipoCodigo tipo do responsável a ser retornado
	 * @author Márcio Gomes
	 */
	public List consultarListaServentiaCargoResponsavelProcesso(String id_Processo, String id_Serventia, int cargoTipoCodigo, boolean boAtivo) throws Exception {
		ServentiaCargoDt serventiaCargoDt = null;
		String Sql;
		ResultSetTJGO rs1 = null;
		List lista = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT pr.ID_SERV_CARGO, sc.SERV_CARGO, sc.NOME_USU, sc.ID_SERV, sc.SERV, sc.CARGO_TIPO_CODIGO, ";
		Sql += " s.ID_SERV_TIPO, s.SERV_TIPO, s.ID_SERV_SUBTIPO, s.SERV_SUBTIPO, s.SERV_SUBTIPO_CODIGO ";
		Sql += " FROM PROJUDI.VIEW_PROC_RESP pr";
		Sql += " JOIN PROJUDI.VIEW_SERV_CARGO sc on pr.ID_SERV_CARGO = sc.ID_SERV_CARGO";
		Sql += " JOIN PROJUDI.VIEW_SERV s on s.ID_SERV = sc.ID_SERV";
		Sql += " WHERE pr.ID_PROC = ?";
		ps.adicionarLong(id_Processo);
		if (boAtivo) {
			Sql += " AND pr.CODIGO_TEMP = ?";
			ps.adicionarLong(ProcessoResponsavelDt.ATIVO);
		} else {
			Sql += " AND pr.CODIGO_TEMP = ?";
			ps.adicionarLong(ProcessoResponsavelDt.INATIVO);
		}
		Sql += " AND sc.CARGO_TIPO_CODIGO = ?";
		ps.adicionarLong(cargoTipoCodigo);
		if (id_Serventia != null){
			Sql += " AND sc.ID_SERV = ?";
			ps.adicionarLong(id_Serventia);		
		}

		try{
			rs1 = consultar(Sql,ps);
			while (rs1.next()) {
				serventiaCargoDt = new ServentiaCargoDt();
				serventiaCargoDt.setId(rs1.getString("ID_SERV_CARGO"));
				serventiaCargoDt.setServentiaCargo(rs1.getString("SERV_CARGO"));
				serventiaCargoDt.setNomeUsuario(rs1.getString("NOME_USU"));
				serventiaCargoDt.setId_Serventia(rs1.getString("ID_SERV"));
				serventiaCargoDt.setServentia(rs1.getString("SERV"));
				serventiaCargoDt.setCargoTipoCodigo(rs1.getString("CARGO_TIPO_CODIGO"));
				serventiaCargoDt.setId_ServentiaSubtipo(rs1.getString("ID_SERV_SUBTIPO"));
				serventiaCargoDt.setServentiaSubtipo(rs1.getString("SERV_SUBTIPO"));
				serventiaCargoDt.setServentiaSubtipoCodigo(rs1.getString("SERV_SUBTIPO_CODIGO"));
				lista.add(serventiaCargoDt);
			}
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return lista;
	}
	
	/**
	 * Retorna uma lista de ServentiaCargo responsável por um processo, de um determinado tipo e em uma determinada serventia.
	 * OBS.: Deve ser retornado uma lista de ServentiaCargo responsável que estão ativos
	 * 
	 * @param processoDt identificação do processo
	 * @param id_serventia identificação da serventia
	 * @param cargoTipoCodigo tipo do responsável a ser retornado
	 * @author Márcio Gomes
	 */
	public List consultarListaPromotoresProcesso(String id_Processo, String id_Serventia, String serventiaSubTipoCodigo, int cargoTipoCodigo, boolean boAtivo) throws Exception {
		ServentiaCargoDt serventiaCargoDt = null;
		String Sql;
		ResultSetTJGO rs1 = null;
		List lista = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT pr.ID_SERV_CARGO, sc.SERV_CARGO, sc.NOME_USU, sc.ID_SERV, sc.SERV, sc.CARGO_TIPO_CODIGO  ";
		Sql += " FROM PROJUDI.PROC_RESP pr";
		Sql += " JOIN PROJUDI.VIEW_SERV_CARGO sc on pr.ID_SERV_CARGO = sc.ID_SERV_CARGO";
		Sql += " JOIN PROJUDI.SERV s on sc.ID_SERV = s.ID_SERV";
		Sql += " JOIN PROJUDI.SERV_SUBTIPO ss on s.ID_SERV_SUBTIPO = ss.ID_SERV_SUBTIPO";
		Sql += " WHERE pr.ID_PROC = ?";
		ps.adicionarLong(id_Processo);
		if (boAtivo) {
			Sql += " AND pr.CODIGO_TEMP = ?";
			ps.adicionarLong(ProcessoResponsavelDt.ATIVO);
		} else {
			Sql += " AND pr.CODIGO_TEMP = ?";
			ps.adicionarLong(ProcessoResponsavelDt.INATIVO);
		}
		Sql += " AND sc.CARGO_TIPO_CODIGO = ?";
		ps.adicionarLong(cargoTipoCodigo);
		if (id_Serventia != null){
			Sql += " AND sc.ID_SERV = ?";
			ps.adicionarLong(id_Serventia);		
		}
		if (serventiaSubTipoCodigo != null){
			Sql += " AND ss.SERV_SUBTIPO_CODIGO = ?";
			ps.adicionarLong(serventiaSubTipoCodigo);		
		}
		//Impedindo localizar promotorias que estejam com o status BLOQUEADO
		Sql += " AND s.CODIGO_TEMP = ?";
		ps.adicionarLong(ServentiaDt.ATIVO);

		try{
			rs1 = consultar(Sql,ps);
			while (rs1.next()) {
				serventiaCargoDt = new ServentiaCargoDt();
				serventiaCargoDt.setId(rs1.getString("ID_SERV_CARGO"));
				serventiaCargoDt.setServentiaCargo(rs1.getString("SERV_CARGO"));
				serventiaCargoDt.setNomeUsuario(rs1.getString("NOME_USU"));
				serventiaCargoDt.setId_Serventia(rs1.getString("ID_SERV"));
				serventiaCargoDt.setServentia(rs1.getString("SERV"));
				serventiaCargoDt.setCargoTipoCodigo(rs1.getString("CARGO_TIPO_CODIGO"));				
				lista.add(serventiaCargoDt);
			}
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return lista;
	}
	
	/**
	 * Consulta o ralator responsável pelo processo de segundo grau
	 * 
	 * @param id_Processo = identificação do processo
	 * @author mmgomes
	 * @throws Exception 
	 */
	public ServentiaCargoDt consultarRelatorResponsavelProcessoSegundoGrau(String id_Processo) throws Exception{		
		return consultarResponsavelProcessoSegundoGrau(id_Processo, CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU);
	}
	
	/**
	 * Consulta o revisor responsável pelo processo de segundo grau
	 * 
	 * @param id_Processo = identificação do processo
	 * @author mmgomes
	 * @throws Exception 
	 */
	public ServentiaCargoDt consultarRevisorResponsavelProcessoSegundoGrau(String id_Processo) throws Exception{		
		return consultarResponsavelProcessoSegundoGrau(id_Processo, CargoTipoDt.REVISOR_SEGUNDO_GRAU);
	}
	
	/**
	 * Consulta o vogal responsável pelo processo de segundo grau
	 * 
	 * @param id_Processo = identificação do processo
	 * @author mmgomes
	 * @throws Exception 
	 */
	public ServentiaCargoDt consultarVogalResponsavelProcessoSegundoGrau(String id_Processo) throws Exception{		
		return consultarResponsavelProcessoSegundoGrau(id_Processo, CargoTipoDt.VOGAL_SEGUNDO_GRAU);
	}
	
	/***
	 * Consulta o responsável do processo de segundo grau
	 * @param id_Processo
	 * @param cargoTipoCodigo
	 * @return
	 * @throws Exception
	 */
	private ServentiaCargoDt consultarResponsavelProcessoSegundoGrau(String id_Processo, int cargoTipoCodigo) throws Exception{
		String sql;
		ResultSetTJGO rs1 = null;
		ServentiaCargoDt serventiaCargoDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql = "SELECT pr.ID_SERV_CARGO, sc.CARGO_TIPO, sc.NOME_USU, sc.ID_SERV, sc.SERV, ";
		sql += " pr.CARGO_TIPO as CARGO_TIPO_RESP, pr.CARGO_TIPO_CODIGO as CARGO_TIPO_CODIGO_RESP, pr.REDATOR as REDATOR, sc.ID_USU_SERV";
		sql += " FROM PROJUDI.VIEW_PROC_RESP pr";
		sql += " JOIN PROJUDI.VIEW_SERV_CARGO sc on pr.ID_SERV_CARGO = sc.ID_SERV_CARGO";
		sql += " WHERE pr.ID_PROC = ?";
		ps.adicionarLong(id_Processo);
		sql += " AND pr.CARGO_TIPO_CODIGO = ?";
		ps.adicionarLong(cargoTipoCodigo);
		sql += " AND pr.CODIGO_TEMP = ?";
		ps.adicionarLong(ProcessoResponsavelDt.ATIVO);

		try{
			rs1 = consultar(sql, ps);
			if (rs1.next()) {
				serventiaCargoDt = new ServentiaCargoDt();
				serventiaCargoDt.setId(rs1.getString("ID_SERV_CARGO"));				
				serventiaCargoDt.setCargoTipo(rs1.getString("CARGO_TIPO") + "-" + rs1.getString("CARGO_TIPO_RESP"));
				serventiaCargoDt.setNomeUsuario(rs1.getString("NOME_USU"));
				serventiaCargoDt.setId_Serventia(rs1.getString("ID_SERV"));
				serventiaCargoDt.setServentia(rs1.getString("SERV"));	
				serventiaCargoDt.setRedator(rs1.getString("REDATOR"));
				serventiaCargoDt.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return serventiaCargoDt;
	}
	
	/***
	 * Consultar Lista de Id de processo responsável 
	 * @param id_ServentiaCargo
	 * @param id_Processo
	 * @return
	 * @throws Exception
	 * @author mmgomes
	 */
	public List consultarIdProcessoResponsavel(String id_ServentiaCargo, String id_Processo) throws Exception {
		String Sql;
		ResultSetTJGO rs1 = null;
		List lista = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT pr.ID_PROC_RESP FROM PROJUDI.PROC_RESP pr";
		Sql += " WHERE pr.ID_PROC = ?";
		ps.adicionarLong(id_Processo);
		Sql += " AND pr.ID_SERV_CARGO = ?";
		ps.adicionarLong(id_ServentiaCargo);

		try{
			rs1 = consultar(Sql, ps);
			while (rs1.next()) {							
				lista.add(rs1.getString("ID_PROC_RESP"));
			}			
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return lista;
	}
	
	/**
	 * Atualiza os dados de um ProcessoResponsavel em virtude de uma troca de responsável pelo processo
	 * 
	 * @param Id_ProcessoResponsavel, identificação do processo responsável 
	 * @param id_NovoResponsavel, novo responsável
	 * 
	 * @author mmgomes
	 */
	public void alterarResponsavelProcesso(String Id_ProcessoResponsavel, String id_NovoResponsavel) throws Exception {
		String stSql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		stSql = "UPDATE PROJUDI.PROC_RESP SET ID_SERV_CARGO = ?";		ps.adicionarLong(id_NovoResponsavel);
		stSql += " WHERE ID_PROC_RESP = ?";								ps.adicionarLong(Id_ProcessoResponsavel);		
		executarUpdateDelete(stSql, ps);
	}
	
	/**
	 * Limpar os Responsáveis de um em virtude de uma troca de responsável pelo processo
	 * 
	 * @param Id_ProcessoResponsavel, identificação do processo responsável 
	 * 
	 * @author mmgomes
	 */
	public void limparResponsavelProcesso(String Id_ProcessoResponsavel) throws Exception {
		String stSql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		stSql = "UPDATE PROJUDI.PROC_RESP SET ID_SERV_CARGO = NULL";
		stSql += " WHERE ID_PROC_RESP = ?";
		ps.adicionarLong(Id_ProcessoResponsavel);		

			executarUpdateDelete(stSql, ps);
	}
	
	/**
	 * Limpar os Responsáveis de um processo não conhecido
	 * 
	 * @param Id_ProcessoResponsavel, identificação do processo responsável 
	 * 
	 * @author lsbernardes
	 */
	public void limparResponsaveisProcessoSegundoGrau(String Id_ProcessoResponsavel) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql= "DELETE FROM PROJUDI.PROC_RESP";
		Sql += " WHERE ID_PROC_RESP = ?";		ps.adicionarLong(Id_ProcessoResponsavel); 

			executarUpdateDelete(Sql, ps);
	}
	
	/**
	 * Consultar Responsáveis pelo processo no segundo Grau
	 * 
	 * @param idProcesso, Lidentificação do processo responsável 
	 * 
	 * @author lsbernardes
	 */
	public List consultarResponsaveisProcessoSegundoGrau(String id_Processo) throws Exception {
		String Sql;
		ResultSetTJGO rs1 = null;
		List lista = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "select pr.ID_PROC_RESP, pr.ID_SERV_CARGO, pr.ID_PROC, pr.ID_CARGO_TIPO, pr.CODIGO_TEMP ";
		Sql += " from PROJUDI.VIEW_PROC_RESP pr";
		Sql += " where pr.ID_PROC = ?";
		ps.adicionarLong(id_Processo);
		Sql += " and ( pr.GRUPO_CODIGO = ?  or pr.GRUPO_CODIGO = ? )";
		ps.adicionarLong(GrupoDt.DESEMBARGADOR);
		ps.adicionarLong(GrupoDt.JUIZES_TURMA_RECURSAL);

		try{
			rs1 = consultar(Sql, ps);
			while (rs1.next()) {							
				lista.add(rs1.getString("ID_PROC_RESP"));
			}			
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return lista;

	}
	
	/**
	 * Remove os dados de um ProcessoResponsavel em virtude de uma exclusão de responsável pelo processo
	 * 
	 * @param id_Processo, identificação do processo
	 * @param id_Responsavel, responsável anterior
	 * 
	 * @author mmgomes
	 */
	public void removeResponsavelProcesso(String id_Processo, String id_Responsavel) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "DELETE FROM PROJUDI.PROC_RESP WHERE ID_SERV_CARGO = ?";
		ps.adicionarLong(id_Responsavel);
		Sql += " AND ID_PROC = ?";
		ps.adicionarLong(id_Processo);		

			executarUpdateDelete(Sql, ps);
	}
	
	/**
	 * Desativar o Responsável pelo processo de um determinado CargoTipoCodigo
	 * 
	 * @param Id_ProcessoResponsavel, identificação do processo responsável 
	 * 
	 * @author mmgomes
	 */
	public void inativarResponsavelProcesso(String id_ProcessoResponsavel) throws Exception {
		String stSql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		stSql = "UPDATE PROJUDI.PROC_RESP SET CODIGO_TEMP = ? ";
		stSql += " WHERE ID_PROC_RESP = ? ";		
		ps.adicionarLong(ProcessoResponsavelDt.INATIVO);
		ps.adicionarLong(id_ProcessoResponsavel);			

			executarUpdateDelete(stSql, ps);
	}
	
	/**
	 * Método que habilita e desabilita responsável por processo.
	 * @param processoResponsavelDt - dados do responsável
	 * @throws Exception
	 * @author hmgodinho
	 */
	public void habilitarDesabilitarResponsavelProcesso(String id_ProcessoResponsavel, String status) throws Exception {
		String stSql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		stSql = "UPDATE PROJUDI.PROC_RESP SET CODIGO_TEMP = ? ";
		stSql += " WHERE ID_PROC_RESP = ? ";		
		ps.adicionarLong(status);
		ps.adicionarLong(id_ProcessoResponsavel);			

		executarUpdateDelete(stSql, ps);
	}
	
	/***
	 * Consultar Id de processo responsável por cargoTipoCodigo	 
	 * @param id_Processo
	 * @param cargoTipoCodigo
	 * @return
	 * @throws Exception
	 * @author mmgomes
	 */
	public String consultarIdProcessoResponsavelProcessoCargoTipoCodigo(String id_Processo, String cargoTipoCodigo) throws Exception {
		String Sql;
		ResultSetTJGO rs1 = null;
		String idProcessoResponsavel = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT pr.ID_PROC_RESP FROM PROJUDI.PROC_RESP pr";
		Sql += " WHERE pr.ID_PROC = ?";
		ps.adicionarLong(id_Processo);		
		Sql += " AND pr.ID_CARGO_TIPO = (SELECT ID_CARGO_TIPO FROM PROJUDI.CARGO_TIPO WHERE CARGO_TIPO_CODIGO = ? AND ROWNUM = 1)";
		ps.adicionarLong(cargoTipoCodigo);
		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {							
				idProcessoResponsavel = rs1.getString("ID_PROC_RESP");
			}			
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return idProcessoResponsavel;
	}	
	
	public void alterar(ProcessoResponsavelDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";		

		stSql = "UPDATE PROJUDI.PROC_RESP SET  ";
		stSql += " ID_SERV_CARGO = ? ";		 ps.adicionarLong(dados.getId_ServentiaCargo());  

		stSql += " ,ID_PROC = ? ";		 ps.adicionarLong(dados.getId_Processo());
		
		stSql += " ,REDATOR = ? ";		 ps.adicionarBoolean(dados.getRedator());
		
		if ((dados.getId_CargoTipo().length() > 0) || dados.getId_CargoTipo().length() > 0){
			stSql += " ,ID_CARGO_TIPO = ? "; ps.adicionarLong(dados.getId_CargoTipo());			
		}else if (dados.getCargoTipoCodigo().length() > 0) {
			stSql += " ,ID_CARGO_TIPO = (SELECT ID_CARGO_TIPO FROM CARGO_TIPO WHERE CARGO_TIPO_CODIGO = ?) ";
			ps.adicionarLong(dados.getCargoTipoCodigo());			
		}
		
		stSql += " ,CODIGO_TEMP = ? ";		 ps.adicionarLong(dados.getCodigoTemp());

		stSql += " WHERE ID_PROC_RESP  = ? ";  ps.adicionarLong(dados.getId());

		executarUpdateDelete(stSql,ps); 
	
	}
	
	public boolean temMagistradoInativoProcessoServentia(String idProc, String idServ) throws Exception {
		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ResultSetTJGO rs = null;
		String sql = "";
		
		sql += "select pr.ID_PROC_RESP, pr.ID_SERV_CARGO, pr.ID_PROC, pr.CODIGO_TEMP, sc.ID_SERV, sc.ID_CARGO_TIPO, ct.CARGO_TIPO, ct.CARGO_TIPO_CODIGO from proc_resp pr "; 
		sql += "inner join SERV_CARGO sc on pr.ID_SERV_CARGO = sc.ID_SERV_CARGO ";
		sql += "inner join CARGO_TIPO ct on sc.ID_CARGO_TIPO = ct.ID_CARGO_TIPO ";
		sql += "where sc.id_serv = ? "; ps.adicionarLong(idServ);
		sql += "AND pr.CODIGO_TEMP = -1 ";
		sql += "AND pr.ID_PROC = ? "; ps.adicionarLong(idProc);
		sql += "AND ct.CARGO_TIPO_CODIGO in (";
		sql += "?,"; ps.adicionarLong(CargoTipoDt.JUIZ_1_GRAU);
		sql += "?,"; ps.adicionarLong(CargoTipoDt.JUIZ_AUXILIAR_PRESIDENCIA);
		sql += "?,"; ps.adicionarLong(CargoTipoDt.JUIZ_CORREGEDOR);
		sql += "?,"; ps.adicionarLong(CargoTipoDt.JUIZ_EXECUCAO);
		sql += "?,"; ps.adicionarLong(CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU);
		sql += "?)"; ps.adicionarLong(CargoTipoDt.DESEMBARGADOR);
				
		try{
			rs = consultar(sql, ps);
			if (rs != null && rs.next()) {							
				return true;
			}			
		
		} finally{
			try{if (rs != null) rs.close();} catch(Exception e) {}
		}
		
		return false;
	}
	
	
	public void alterarCodigoTemp(String id_serv_cargo, String id_proc, String codigoTemp) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";		

		stSql = "UPDATE PROJUDI.PROC_RESP SET  ";
		
		stSql += " CODIGO_TEMP = ? ";		 ps.adicionarLong(codigoTemp);

		stSql += " WHERE ID_SERV_CARGO  = ? ";  ps.adicionarLong(id_serv_cargo);
		
		stSql += "AND ID_PROC  = ? ";  ps.adicionarLong(id_proc);
		
		executarUpdateDelete(stSql,ps); 
	
	}

	public ServentiaCargoDt consultarRelatorSessaoVirtual(String idAudiProc, FabricaConexao obFabricaConexao) throws Exception {
		ServentiaCargoDt serventiaCargoDt = null;
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
	
		Sql = "SELECT APV.ID_SERV_CARGO, sc.ID_USU_SERV, sc.SERV_CARGO, sc.NOME_USU, sc.ID_SERV, sc.SERV, sc.CARGO_TIPO_CODIGO";
		Sql += " FROM PROJUDI.AUDI_PROC_VOTANTES APV";
		Sql += " JOIN PROJUDI.VIEW_SERV_CARGO sc on APV.ID_SERV_CARGO = sc.ID_SERV_CARGO";
		Sql += " WHERE APV.ID_AUDI_PROC = ?"; ps.adicionarLong(idAudiProc);
		Sql += " AND ID_VOTANTE_TIPO = ( SELECT ID_VOTANTE_TIPO FROM VOTANTE_TIPO WHERE VOTANTE_TIPO_CODIGO = ? )"; ps.adicionarLong(1);
	
		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				serventiaCargoDt = new ServentiaCargoDt();
				serventiaCargoDt.setId(rs1.getString("ID_SERV_CARGO"));
				serventiaCargoDt.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
				serventiaCargoDt.setServentiaCargo(rs1.getString("SERV_CARGO"));
				serventiaCargoDt.setNomeUsuario(rs1.getString("NOME_USU"));
				serventiaCargoDt.setId_Serventia(rs1.getString("ID_SERV"));
				serventiaCargoDt.setServentia(rs1.getString("SERV"));
				serventiaCargoDt.setCargoTipoCodigo(rs1.getString("CARGO_TIPO_CODIGO"));
			}
			//rs1.close();
	
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return serventiaCargoDt;
	}
	
	/**
	 * Retorna o ServentiaCargo responsável pelo processo
	 * @param processoDt identificação do processo 
	 * @author lsbernardes
	 */
	
	public ServentiaCargoDt consultarJuizUPJ_Relator(String id_Processo, int cargoTipo, boolean boAtivo) throws Exception {
		ServentiaCargoDt serventiaCargoDt = null;
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		stSql = "SELECT DISTINCT pr.ID_SERV_CARGO, sc.SERV_CARGO, u.NOME as NOME_USUARIO, sc.ID_SERV, sc.QUANTIDADE_DIST, s.SERV";   
		stSql += " FROM PROJUDI.PROC_RESP pr ";
		stSql += " INNER JOIN PROJUDI.PROC p on pr.ID_PROC = p.ID_PROC "; 
		stSql += " INNER JOIN PROJUDI.CARGO_TIPO ct on ct.ID_CARGO_TIPO = pr.ID_CARGO_TIPO";
		stSql += " INNER JOIN PROJUDI.SERV_CARGO sc on sc.ID_SERV_CARGO = pr.ID_SERV_CARGO";  
		stSql += " INNER JOIN PROJUDI.SERV s on s.ID_SERV = sc.ID_SERV";
		stSql += " INNER JOIN PROJUDI.USU_SERV_GRUPO usg on usg.ID_USU_SERV_GRUPO = sc.ID_USU_SERV_GRUPO"; 
		stSql += " INNER JOIN PROJUDI.USU_SERV us on us.ID_USU_SERV = usg.ID_USU_SERV";
		stSql += " INNER JOIN PROJUDI.USU u on u.ID_USU = us.ID_USU";
		    	
		stSql += " WHERE pr.ID_PROC = ?";		ps.adicionarLong(id_Processo);
		stSql += " AND pr.CODIGO_TEMP = ?";		
		if(boAtivo) {
			ps.adicionarLong(ProcessoResponsavelDt.ATIVO);
		} else {
			ps.adicionarLong(ProcessoResponsavelDt.INATIVO);
		}
		stSql += " AND ct.CARGO_TIPO_CODIGO = ? ";	ps.adicionarLong( cargoTipo);							
		
		try{
			rs1 = consultar(stSql, ps);
			if (rs1.next()) {
				serventiaCargoDt = new ServentiaCargoDt();
				serventiaCargoDt.setId(rs1.getString("ID_SERV_CARGO"));
				serventiaCargoDt.setServentiaCargo(rs1.getString("SERV_CARGO"));
				serventiaCargoDt.setNomeUsuario(rs1.getString("NOME_USUARIO"));
				serventiaCargoDt.setId_Serventia(rs1.getString("ID_SERV"));
				serventiaCargoDt.setServentia(rs1.getString("SERV"));
				serventiaCargoDt.setQuantidadeDistribuicao(rs1.getString("QUANTIDADE_DIST"));
			}
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return serventiaCargoDt;
	}
	
	/**
	 * Retorna o Id_ServentiaCargo do magistrado Responsável pelo processo em uma determinada serventia.
	 * 
	 * @param id_processo, identificação de processo
	 * @param id_serventia, serventia para a qual quer encontrar o juiz responsável pelo processo 
	 * @author lsbernardes
	 */
	public String consultarMagistradoResponsavelProcesso(String id_Processo, String id_Serventia) throws Exception {
		String sql;
		String stRetorno = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql = "SELECT sc.ID_SERV_CARGO FROM PROJUDI.VIEW_PROC_RESP pr";
		sql += " JOIN PROJUDI.VIEW_SERV_CARGO sc on pr.ID_SERV_CARGO = sc.ID_SERV_CARGO";
		sql += " WHERE pr.ID_PROC = ?";
		ps.adicionarLong(id_Processo);
		sql += " AND sc.CARGO_TIPO_CODIGO in (?,?,?,?,?,?)";	
		ps.adicionarLong(CargoTipoDt.JUIZ_EXECUCAO); ps.adicionarLong(CargoTipoDt.JUIZ_1_GRAU); ps.adicionarLong(CargoTipoDt.JUIZ_INFANCIA_JUVENTUDE_CIVEL); 
		ps.adicionarLong(CargoTipoDt.JUIZ_INFANCIA_JUVENTUDE_INFRACIONAL); ps.adicionarLong(CargoTipoDt.JUIZ_UPJ);  ps.adicionarLong(CargoTipoDt.DESEMBARGADOR);
		if (id_Serventia != null) {
			sql += " AND sc.ID_SERV = ?";
			ps.adicionarLong(id_Serventia);
		}
		sql += " AND pr.CODIGO_TEMP = ?";
		ps.adicionarLong(ProcessoResponsavelDt.ATIVO);

		try{
			rs1 = consultar(sql, ps);
			if (rs1.next()) stRetorno = rs1.getString("ID_SERV_CARGO");
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return stRetorno;
	}
	
	/**
	 * Retorna true caso o processo possua algum promotor como responsável na instância especificada pela serventia Subtipo informada.
	 * 
	 * @param conexao conexão ativa
	 */
	public boolean temPromotorNaInstanciaProcesso(String id_Processo, String serventiaSubtipoCodigo) throws Exception {
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql =  " SELECT 1 ";
		Sql += " FROM PROJUDI.PROC_RESP pr ";
		Sql += " JOIN PROJUDI.SERV_CARGO sc on pr.ID_SERV_CARGO = sc.ID_SERV_CARGO ";
		Sql += " JOIN PROJUDI.USU_SERV_GRUPO usg on usg.ID_USU_SERV_GRUPO = sc.ID_USU_SERV_GRUPO ";
		Sql += " JOIN PROJUDI.GRUPO g on g.ID_GRUPO = usg.ID_GRUPO ";
		Sql += " JOIN PROJUDI.GRUPO_TIPO gt on gt.ID_GRUPO_TIPO = g.ID_GRUPO_TIPO ";
		Sql += " JOIN PROJUDI.SERV s on s.id_serv = sc.ID_SERV ";
		Sql += " JOIN PROJUDI.SERV_SUBTIPO ss on ss.ID_SERV_SUBTIPO = s.ID_SERV_SUBTIPO ";
		Sql += "	WHERE pr.ID_PROC = ? "; 								ps.adicionarLong(id_Processo);
		Sql += "	AND pr.CODIGO_TEMP = ? "; 								ps.adicionarLong(ProcessoResponsavelDt.ATIVO);
		Sql += "	AND gt.GRUPO_TIPO_CODIGO = ? ";							ps.adicionarLong(GrupoTipoDt.MP);
		Sql += "	AND ss.SERV_SUBTIPO_CODIGO = ? ";						ps.adicionarLong(serventiaSubtipoCodigo);
		
		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				return true;
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return false;
	}

	/**
	 * Reativa especificamente um responsável pelo processo, identificado pelo id do processo e pelo idServCargo do responsável.
	 * @param idProc
	 * @param idServCargo
	 * @return
	 * @throws Exception
	 */
	public boolean reativarResponsavelProcesso(String idProc, String idServCargo) throws Exception {
		String sql = "";
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdLinhasAlteradas = 0;
		
		sql += " UPDATE PROC_RESP SET ";
		sql += " CODIGO_TEMP = ?, "; 		ps.adicionarLong(ProcessoResponsavelDt.ATIVO);
		sql += " REDATOR = ? "; 			ps.adicionarLong(ProcessoResponsavelDt.PROCESSO_RESPONSAVEL_REDATOR_ATIVO);
		sql += " WHERE ID_PROC = ? ";		ps.adicionarLong(idProc);
		sql += " AND ID_SERV_CARGO = ? ";	ps.adicionarLong(idServCargo);
		
		qtdLinhasAlteradas = executarUpdateDelete(sql, ps);
			
		return qtdLinhasAlteradas != 0;
	}
}
