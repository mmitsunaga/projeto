package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.AreaDistribuicaoDt;
import br.gov.go.tj.projudi.dt.DescritorComboDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;

import java.sql.Connection;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ProjudiConstantes;
import br.gov.go.tj.utils.ResultSetTJGO;

public class AreaDistribuicaoPs extends AreaDistribuicaoPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = 339138364575106125L;
    
    @SuppressWarnings("unused")
	private AreaDistribuicaoPs( ) {}
    	
    public AreaDistribuicaoPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Sobrescrevendo método inserir
	 */
	public void inserir(AreaDistribuicaoDt dados) throws Exception {

		String SqlCampos;
		String SqlValores;
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		SqlCampos = "INSERT INTO PROJUDI.AREA_DIST (";
		SqlValores = " VALUES (";
		if ((dados.getAreaDistribuicao().length() > 0)){
			SqlCampos += "AREA_DIST ";
			SqlValores += "?";
			ps.adicionarString(dados.getAreaDistribuicao());			
		}
		if ((dados.getAreaDistribuicaoCodigo().length() > 0)){
			SqlCampos += ",AREA_DIST_CODIGO ";
			SqlValores += ",?";			
			ps.adicionarLong(dados.getAreaDistribuicaoCodigo());
		}
		if ((dados.getId_Forum().length() > 0)){
			SqlCampos += ",ID_FORUM ";
			SqlValores += ",?";			
			ps.adicionarLong(dados.getId_Forum());
		}
		if ((dados.getId_ServentiaSubtipo().length() > 0)){
			SqlCampos += ",ID_SERV_SUBTIPO ";
			SqlValores += ",?";			
			ps.adicionarLong(dados.getId_ServentiaSubtipo());
		}
		if ((dados.getId_AreaDistribuicaoRelacionada().length() > 0)){
			SqlCampos += ",ID_AREA_DIST_RELACIONADA ";
			SqlValores += ",?";			
			ps.adicionarLong(dados.getId_AreaDistribuicaoRelacionada());
		}
		if ((dados.getCodigoTemp().length() > 0)){
			SqlCampos += ",CODIGO_TEMP ";
			SqlValores += ",?";			
			ps.adicionarLong(dados.getCodigoTemp());
		}		
		SqlValores += ") ";
		SqlCampos += ")";		

		Sql = SqlCampos.replace("(,", "(") + SqlValores.replace("(,", "(");	
				
		dados.setId(executarInsert(Sql, "ID_AREA_DIST", ps));
		
		
	}

	/**
	 * Consulta geral de Áreas de Distribuição disponíveis em uma Comarca
	 * 
	 * @param nome: descrição para filtro
	 * @param id_comarca: identificação da comarca
	 * @param posicao: pagina solicitada
	 * 
	 * @author msapaula
	 */
	public List consultarAreasDistribuicao(String nome, String id_comarca, ServentiaSubtipoDt subtipoDt, String posicao) throws Exception {
		String Sql;
		String SqlComun;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		SqlComun = " FROM PROJUDI.VIEW_AREA_DIST WHERE AREA_DIST LIKE ?";
		ps.adicionarString( nome +"%");
		
		if (id_comarca.length() > 0) {
			SqlComun += " AND ID_COMARCA = ?";
			ps.adicionarLong(id_comarca);
		}
		if (subtipoDt != null) {
			if (subtipoDt.getId().length() > 0){
				SqlComun += " AND ID_SERV_SUBTIPO = ?";
				ps.adicionarLong(subtipoDt.getId());
			}
			else if (subtipoDt.getServentiaSubtipoCodigo().length() > 0){
				SqlComun += " AND SERV_SUBTIPO_CODIGO = ?";
				ps.adicionarLong(subtipoDt.getServentiaSubtipoCodigo());
			}
		}

		Sql = "SELECT ID_AREA_DIST,AREA_DIST, FORUM_CODIGO";
		Sql += SqlComun + " ORDER BY AREA_DIST";		
		try{

			rs1 = consultarPaginacao(Sql, ps, posicao);

			while (rs1.next()) {
				AreaDistribuicaoDt obTemp = new AreaDistribuicaoDt();
				obTemp.setId(rs1.getString("ID_AREA_DIST"));
				obTemp.setAreaDistribuicao(rs1.getString("AREA_DIST"));
				obTemp.setForumCodigo(rs1.getString("FORUM_CODIGO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			Sql = "SELECT COUNT(*) as QUANTIDADE " + SqlComun;
			rs2 = consultar(Sql, ps);
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));			
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
			try{
				if (rs2 != null) rs2.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}
	
	/**
	 * Consulta geral de Áreas de Distribuição disponíveis em uma Comarca
	 * 
	 * @param nome: descrição para filtro
	 * @param id_comarca: identificação da comarca
	 * @param posicao: pagina solicitada
	 * 
	 * @author wcsilva
	 */
	public List consultarAreasDistribuicaoExecucao(String nome, String id_comarca, String posicao) throws Exception {
		String Sql;
		String SqlComun;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		SqlComun = " FROM PROJUDI.VIEW_AREA_DIST WHERE AREA_DIST LIKE ?";
		ps.adicionarString( nome +"%");
		
		if (id_comarca.length() > 0) {
			SqlComun += " AND ID_COMARCA = ?";
			ps.adicionarLong(id_comarca);
		}
		SqlComun += " AND SERV_SUBTIPO_CODIGO in (?,";		ps.adicionarLong(ServentiaSubtipoDt.VARAS_EXECUCAO_PENAL);
		SqlComun += "?,";									ps.adicionarLong(ServentiaSubtipoDt.VARAS_EXECUCAO_PENAL_ALTERNATIVA);
		SqlComun += "?)";									ps.adicionarLong(ServentiaSubtipoDt.EXECPENWEB);
				

		Sql = "SELECT ID_AREA_DIST,AREA_DIST, FORUM_CODIGO";
		Sql += SqlComun + " ORDER BY AREA_DIST";		
		try{

			rs1 = consultarPaginacao(Sql, ps, posicao);

			while (rs1.next()) {
				AreaDistribuicaoDt obTemp = new AreaDistribuicaoDt();
				obTemp.setId(rs1.getString("ID_AREA_DIST"));
				obTemp.setAreaDistribuicao(rs1.getString("AREA_DIST"));
				obTemp.setForumCodigo(rs1.getString("FORUM_CODIGO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			Sql = "SELECT COUNT(*) as QUANTIDADE " + SqlComun;
			rs2 = consultar(Sql, ps);
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));			
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
			try{
				if (rs2 != null) rs2.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}
	
	public String consultarAreasDistribuicaoExecucaoJSON(String nome, String id_comarca, String posicao) throws Exception {
		String Sql="";
		String Sql1="";
		String stTemp="";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 1;

		Sql1 = " FROM PROJUDI.VIEW_AREA_DIST WHERE AREA_DIST LIKE ?";
		ps.adicionarString( nome +"%");
		
		if (id_comarca.length() > 0) {
			Sql1 += " AND ID_COMARCA = ?";
			ps.adicionarLong(id_comarca);
		}
		Sql1 += " AND SERV_SUBTIPO_CODIGO in (?,";		ps.adicionarLong(ServentiaSubtipoDt.VARAS_EXECUCAO_PENAL);
		Sql1 += "?,";									ps.adicionarLong(ServentiaSubtipoDt.VARAS_EXECUCAO_PENAL_ALTERNATIVA);
		Sql1 += "?)";									ps.adicionarLong(ServentiaSubtipoDt.EXECPENWEB);
		
		

		Sql = "SELECT ID_AREA_DIST as id,AREA_DIST as descricao1, FORUM_CODIGO as descricao2";
		Sql += Sql1 + " ORDER BY AREA_DIST";		
		try{
			rs1 = consultarPaginacao(Sql, ps, posicao);
			Sql = "SELECT COUNT(*) as QUANTIDADE " + Sql1;
//			Sql +=Sql1;
			rs2 = consultar(Sql, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
			try{
				if (rs2 != null) rs2.close();
			} catch(Exception e) {
			}
		}
		return stTemp;
	}

	/**
	 * Consulta de Áreas de Distribuição Cíveis disponíveis em uma Comarca
	 * 
	 * @param nome, descrição para filtro
	 * @param id_comarca, identificação da comarca
	 * @param posicao, pagina solicitada
	 * 
	 * @author msapaula
	 */
	public List consultarAreasDistribuicaoCivel(String nome, String id_comarca, String posicao) throws Exception {
		String Sql;
		String SqlComun = "";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		SqlComun = " FROM PROJUDI.VIEW_AREA_DIST ";
		SqlComun += " WHERE (SERV_SUBTIPO_CODIGO = ? ";	ps.adicionarLong(+ ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL);		
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";		ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL_CRIMINAL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";		ps.adicionarLong(ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";		ps.adicionarLong(ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL_EXECUACAO_FISCAL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";		ps.adicionarLong(ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL_INTERIOR);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";		ps.adicionarLong(ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";		ps.adicionarLong(ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL_EXECUACAO_FISCAL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";		ps.adicionarLong(ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL_INTERIOR);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";		ps.adicionarLong(ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL_ESTADUAL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";		ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_FAZENDA_PUBLICA);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";		ps.adicionarLong(ServentiaSubtipoDt.UPJ_JUIZADO_ESPECIAL_FAZENDA_PUBLICA);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";		ps.adicionarLong(ServentiaSubtipoDt.PREPROCESSUAL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";		ps.adicionarLong(ServentiaSubtipoDt.FAMILIA_CAPITAL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";		ps.adicionarLong(ServentiaSubtipoDt.UPJ_FAMILIA);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";		ps.adicionarLong(ServentiaSubtipoDt.FAMILIA_INTERIOR);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";		ps.adicionarLong(ServentiaSubtipoDt.UPJ_SUCESSOES);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";		ps.adicionarLong(ServentiaSubtipoDt.VARAS_CIVEL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";		ps.adicionarLong(ServentiaSubtipoDt.AUDITORIA_MILITAR_CIVEL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";		ps.adicionarLong(ServentiaSubtipoDt.PLANTAO_PRIMEIRO_GRAU);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?)";		ps.adicionarLong(ServentiaSubtipoDt.PLANTAO_AUDIENCIA_CUSTODIA);
		
		SqlComun += " AND CODIGO_TEMP = ?";
		ps.adicionarLong(AreaDistribuicaoDt.ATIVO);
		
		if (nome != null && nome.trim().length() > 0)
		{
			SqlComun += " AND AREA_DIST LIKE ? ";
			ps.adicionarString( nome.trim() +"%");
		}		
		if (id_comarca != null && id_comarca.trim().length() > 0 && Funcoes.StringToLong(id_comarca.trim()) > 0) {
			SqlComun += " AND ID_COMARCA = ? ";
			ps.adicionarLong(id_comarca.trim());			 
		}

		Sql = "SELECT ID_AREA_DIST,AREA_DIST, FORUM_CODIGO, ID_SERV_SUBTIPO ";
		Sql += SqlComun + " ORDER BY AREA_DIST";		
		try{

			rs1 = consultarPaginacao(Sql, ps, posicao);

			while (rs1.next()) {
				AreaDistribuicaoDt obTemp = new AreaDistribuicaoDt();
				obTemp.setId(rs1.getString("ID_AREA_DIST"));
				obTemp.setAreaDistribuicao(rs1.getString("AREA_DIST"));
				obTemp.setForumCodigo(rs1.getString("FORUM_CODIGO"));
				obTemp.setId_ServentiaSubtipo(rs1.getString("ID_SERV_SUBTIPO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			Sql = "SELECT COUNT(*) as QUANTIDADE " + SqlComun;
			rs2 = consultar(Sql, ps);
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));			
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
			try{
				if (rs2 != null) rs2.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}

	/**
	 * Consulta de Áreas de Distribuição Criminais disponíveis em uma Comarca
	 * 
	 * @param nome, descrição para filtro
	 * @param id_comarca, identificação da comarca
	 * @param posicao, pagina solicitada
	 * 
	 * @author msapaula
	 */
	public List consultarAreasDistribuicaoCriminal(String nome, String id_comarca, String posicao) throws Exception {
		String Sql;
		String SqlComun = "";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
				
		SqlComun = " FROM PROJUDI.VIEW_AREA_DIST ";		
		SqlComun += " WHERE (SERV_SUBTIPO_CODIGO = ?";		ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CRIMINAL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";			ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL_CRIMINAL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";			ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CRIMINAL_FAZENDA_PUBLICA);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";			ps.adicionarLong(ServentiaSubtipoDt.VARA_CRIMINAL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";			ps.adicionarLong(ServentiaSubtipoDt.UPJ_CUSTODIA);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";			ps.adicionarLong(ServentiaSubtipoDt.SECAO_CRIMINAL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";			ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CRIMINAL);	
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";			ps.adicionarLong(ServentiaSubtipoDt.AUDITORIA_MILITAR_CRIMINAL);	
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";			ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_VIOLENCIA_DOMESTICA);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";			ps.adicionarLong(ServentiaSubtipoDt.PLANTAO_AUDIENCIA_CUSTODIA);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";			ps.adicionarLong(ServentiaSubtipoDt.UPJ_CRIMINAL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";			ps.adicionarLong(ServentiaSubtipoDt.UPJ_VIOLENCIA_DOMESTICA);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?)";			ps.adicionarLong(ServentiaSubtipoDt.PLANTAO_PRIMEIRO_GRAU);
		SqlComun += " AND CODIGO_TEMP = ?";
		ps.adicionarLong(AreaDistribuicaoDt.ATIVO);
		
		if (nome != null && nome.trim().length() > 0){
			SqlComun += " AND AREA_DIST LIKE ? ";
			ps.adicionarString( nome.trim() +"%");	
		}		
		if (id_comarca != null && id_comarca.trim().length() > 0 && Funcoes.StringToLong(id_comarca.trim()) > 0){
			SqlComun += " AND ID_COMARCA =?";
			ps.adicionarLong(id_comarca.trim());
		}		

		Sql = "SELECT ID_AREA_DIST,AREA_DIST, FORUM_CODIGO, ID_SERV_SUBTIPO ";
		Sql += SqlComun + " ORDER BY AREA_DIST";		
		try{

			rs1 = consultarPaginacao(Sql, ps, posicao);

			while (rs1.next()) {
				AreaDistribuicaoDt obTemp = new AreaDistribuicaoDt();
				obTemp.setId(rs1.getString("ID_AREA_DIST"));
				obTemp.setAreaDistribuicao(rs1.getString("AREA_DIST"));
				obTemp.setForumCodigo(rs1.getString("FORUM_CODIGO"));
				obTemp.setId_ServentiaSubtipo(rs1.getString("ID_SERV_SUBTIPO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			Sql = "SELECT COUNT(*) as QUANTIDADE " + SqlComun;
			rs2 = consultar(Sql, ps);
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));			
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
			try{
				if (rs2 != null) rs2.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}

	/**
	 * Consulta todas as áreas de distribuição ativas 
	 * 
	 * @author msapaula
	 */
	public List consultarAreasDistribuicaoAtivas() throws Exception {
		String sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql = "SELECT  ad.ID_AREA_DIST, ad.AREA_DIST, ad.AREA_DIST_CODIGO, ad.ID_COMARCA, ad.ID_SERV_SUBTIPO ";
		sql += " FROM VIEW_AREA_DIST ad ";
		sql += " WHERE ad.CODIGO_TEMP = ?";
		ps.adicionarLong(AreaDistribuicaoDt.ATIVO);
		sql += " ORDER BY AREA_DIST_CODIGO, AREA_DIST";

		try{
			rs1 = consultar(sql, ps);
			while (rs1.next()) {
				AreaDistribuicaoDt obTemp = new AreaDistribuicaoDt();
				obTemp.setId(rs1.getString("ID_AREA_DIST"));
				obTemp.setAreaDistribuicao(rs1.getString("AREA_DIST"));
				obTemp.setAreaDistribuicaoCodigo(rs1.getString("AREA_DIST_CODIGO"));
				obTemp.setId_Comarca(rs1.getString("ID_COMARCA"));
				obTemp.setId_ServentiaSubtipo(rs1.getString("ID_SERV_SUBTIPO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}

//	/**
//	 * Consulta as áreas de distribuição ativas para Varas, para caso de redistribuição de um processo,
//	 * com uso de paginação
//	 * 
//	 * @author msapaula
//	 */
//	public List consultarAreasDistribuicaoVaraAtivas(String descricao, String posicao) throws Exception {
//		String sql;
//		String sqlComum = "";
//		List liTemp = new ArrayList();
//		ResultSetTJGO rs1 = null;
//		ResultSetTJGO rs2 = null;
//		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
//
//		sql = "SELECT  ad.ID_AREA_DIST, ad.AREA_DIST, ad.AREA_DIST_CODIGO, ad.ID_COMARCA, ad.ID_SERV_SUBTIPO ";
//		sqlComum += " FROM PROJUDI.VIEW_AREA_DIST ad WHERE ad.AREA_DIST LIKE ? ";
//		ps.adicionarString("%"+ descricao +"%");
//		sqlComum += " AND ad.CODIGO_TEMP = ?";
//		ps.adicionarLong(AreaDistribuicaoDt.ATIVO);		
//		sqlComum += " AND (ad.SERV_SUBTIPO_CODIGO = ?";		ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL);
//		sqlComum += " OR ad.SERV_SUBTIPO_CODIGO = ?";		ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CRIMINAL);
//		sqlComum += " OR ad.SERV_SUBTIPO_CODIGO = ?";		ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL_CRIMINAL);
//		sqlComum += " OR ad.SERV_SUBTIPO_CODIGO = ?";		ps.adicionarLong(ServentiaSubtipoDt.FAMILIA);
//		sqlComum += " OR ad.SERV_SUBTIPO_CODIGO = ?";		ps.adicionarLong(ServentiaSubtipoDt.VARAS_CIVEL);
//		sqlComum += " OR ad.SERV_SUBTIPO_CODIGO = ?";		ps.adicionarLong(ServentiaSubtipoDt.AUDITORIA_MILITAR_CIVEL);		
//		sqlComum += " OR ad.SERV_SUBTIPO_CODIGO = ?";		ps.adicionarLong(ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL);
//		sqlComum += " OR ad.SERV_SUBTIPO_CODIGO = ?";		ps.adicionarLong(ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL_EXECUACAO_FISCAL);
//		sqlComum += " OR ad.SERV_SUBTIPO_CODIGO = ?";		ps.adicionarLong(ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL_INTERIOR);
//		sqlComum += " OR ad.SERV_SUBTIPO_CODIGO = ?";		ps.adicionarLong(ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL);
//		sqlComum += " OR ad.SERV_SUBTIPO_CODIGO = ?";		ps.adicionarLong(ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL_EXECUACAO_FISCAL);
//		sqlComum += " OR ad.SERV_SUBTIPO_CODIGO = ?";		ps.adicionarLong(ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL_INTERIOR);
//		sqlComum += " OR ad.SERV_SUBTIPO_CODIGO = ?)";		ps.adicionarLong(ServentiaSubtipoDt.VARAS_EXECUCAO_PENAL);
//
//		sql += sqlComum;
//		sql += " ORDER BY AREA_DIST_CODIGO, AREA_DIST";
//		
//		try{
//			rs1 = consultarPaginacao(sql, ps, posicao);
//			while (rs1.next()) {
//				AreaDistribuicaoDt obTemp = new AreaDistribuicaoDt();
//				obTemp.setId(rs1.getString("ID_AREA_DIST"));
//				obTemp.setAreaDistribuicao(rs1.getString("AREA_DIST"));
//				obTemp.setAreaDistribuicaoCodigo(rs1.getString("AREA_DIST_CODIGO"));
//				obTemp.setId_Comarca(rs1.getString("ID_COMARCA"));
//				obTemp.setId_ServentiaSubtipo(rs1.getString("ID_SERV_SUBTIPO"));
//				liTemp.add(obTemp);
//			}
//			//rs1.close();
//			sql = "SELECT COUNT(*) as QUANTIDADE " + sqlComum;
//			rs2 = consultar(sql, ps);
//			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));			
//			//rs1.close();
//		
//		} finally{
//			try{
//				if (rs1 != null) rs1.close();
//			} catch(Exception e) {
//			}
//			try{
//				if (rs2 != null) rs2.close();
//			} catch(Exception e) {
//			}
//		}
//		return liTemp;
//	}

	/**
	 * Recupera todas as áreas de distribuição de processos ativas.
	 * @param descricao
	 * @param posicao
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List consultarAreasDistribuicaoAtivas(String descricao, String posicao) throws Exception {
		String sql;
		String sqlComum = "";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		sql = "SELECT  ad.ID_AREA_DIST, ad.AREA_DIST, ad.AREA_DIST_CODIGO, ad.ID_COMARCA, ad.ID_SERV_SUBTIPO ";
		sqlComum += " FROM PROJUDI.VIEW_AREA_DIST ad WHERE ad.AREA_DIST LIKE ? ";
		ps.adicionarString("%"+ descricao +"%");
		sqlComum += " AND ad.CODIGO_TEMP <> ? ";
		ps.adicionarLong(AreaDistribuicaoDt.INATIVO);
		
		sqlComum += " AND ad.SERV_SUBTIPO_CODIGO <> ? "; //alteração solicitada por Ana Cláudia atendendo solicitação do Dr. Aureliano.
		ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CRIMINAL);
		
		sql += sqlComum;
		sql += " ORDER BY AREA_DIST_CODIGO, AREA_DIST";
				
		try{
			rs1 = consultarPaginacao(sql, ps, posicao);
			while (rs1.next()) {
				AreaDistribuicaoDt obTemp = new AreaDistribuicaoDt();
				obTemp.setId(rs1.getString("ID_AREA_DIST"));
				obTemp.setAreaDistribuicao(rs1.getString("AREA_DIST"));
				obTemp.setAreaDistribuicaoCodigo(rs1.getString("AREA_DIST_CODIGO"));
				obTemp.setId_Comarca(rs1.getString("ID_COMARCA"));
				obTemp.setId_ServentiaSubtipo(rs1.getString("ID_SERV_SUBTIPO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			sql = "SELECT COUNT(*) as QUANTIDADE " + sqlComum;
			rs2 = consultar(sql, ps);
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));			
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
			try{
				if (rs2 != null) rs2.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}

	/**
	 * Consulta as áreas de distribuição ativas de acordo com o SERV_SUBTIPO_CODIGO passado,
	 * com uso de paginação
	 * 
	 * @author msapaula
	 */
	/*
	public List consultarAreasDistribuicaoAtivas(String descricao, String posicao, String SERV_SUBTIPO_CODIGO){
		String sql;
		String sqlComum = "";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;

		sql = "SELECT  ad.ID_AREA_DIST, ad.AREA_DIST, ad.AREA_DIST_CODIGO, ad.ID_COMARCA, ad.ID_SERV_SUBTIPO ";
		sqlComum += " FROM PROJUDI.VIEW_AREA_DIST ad WHERE ad.AREA_DIST LIKE '%" + descricao + "%'";
		sqlComum += " AND ad.CODIGO_TEMP " + AreaDistribuicaoDt.ATIVO;
		
		if (SERV_SUBTIPO_CODIGO != null && SERV_SUBTIPO_CODIGO.length() > 0) sqlComum += " AND ad.SERV_SUBTIPO_CODIGO = " + SERV_SUBTIPO_CODIGO;

		sql += sqlComum;
		sql += " ORDER BY AREA_DIST_CODIGO, AREA_DIST";
		sql += " LIMIT " + Funcoes.StringToInt(Configuracao.TamanhoRetornoConsulta).toString();
		sql += " OFFSET " + Funcoes.StringToInt(Configuracao.TamanhoRetornoConsulta * Funcoes.StringToInt(posicao)).toString() + ";";

		try{
			rs1 = consultar(sql);
			while (rs1.next()) {
				AreaDistribuicaoDt obTemp = new AreaDistribuicaoDt();
				obTemp.setId(rs1.getString("ID_AREA_DIST"));
				obTemp.setAreaDistribuicao(rs1.getString("AREA_DIST"));
				obTemp.setAreaDistribuicaoCodigo(rs1.getString("AREA_DIST_CODIGO"));
				obTemp.setId_Comarca(rs1.getString("ID_COMARCA"));
				obTemp.setId_ServentiaSubtipo(rs1.getString("ID_SERV_SUBTIPO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			sql = "SELECT COUNT(*) as QUANTIDADE " + sqlComum;
			rs2 = consultar(sql);
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
			try{
				if (rs2 != null) rs2.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}
	*/

	/**
	 * Consulta as áreas de distribuição ativas para Segundo Grau
	 * 
	 * @author msapaula
	 */
	public List consultarAreasDistribuicaoSegundoGrauAtivas(String descricao, String posicao) throws Exception {
		String sql;
		String sqlComum = "";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql = "SELECT  ad.ID_AREA_DIST, ad.AREA_DIST, ad.AREA_DIST_CODIGO, ad.ID_COMARCA, ad.ID_SERV_SUBTIPO ";
		sqlComum += " FROM PROJUDI.VIEW_AREA_DIST ad WHERE ad.AREA_DIST LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		sqlComum += " AND ( ad.CODIGO_TEMP = ? OR ad.CODIGO_TEMP = ? )";	ps.adicionarLong(AreaDistribuicaoDt.ATIVO);		ps.adicionarLong(AreaDistribuicaoDt.BLOQUEADO);
		sqlComum += " AND (ad.SERV_SUBTIPO_CODIGO = ?";						ps.adicionarLong(ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL);
		sqlComum += " OR ad.SERV_SUBTIPO_CODIGO = ?";						ps.adicionarLong(ServentiaSubtipoDt.UPJ_TURMA_RECURSAL);
		sqlComum += " OR ad.SERV_SUBTIPO_CODIGO = ?";						ps.adicionarLong(ServentiaSubtipoDt.TURMA_RECURSAL_CRIMINAL);
		sqlComum += " OR ad.SERV_SUBTIPO_CODIGO = ?";						ps.adicionarLong(ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL_CRIMINAL);
		sqlComum += " OR ad.SERV_SUBTIPO_CODIGO = ?";						ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CIVEL);
		sqlComum += " OR ad.SERV_SUBTIPO_CODIGO = ?";						ps.adicionarLong(ServentiaSubtipoDt.PLANTAO_SEGUNDO_GRAU);
		sqlComum += " OR ad.SERV_SUBTIPO_CODIGO = ?";						ps.adicionarLong(ServentiaSubtipoDt.SECAO_CIVEL);
		sqlComum += " OR ad.SERV_SUBTIPO_CODIGO = ?";						ps.adicionarLong(ServentiaSubtipoDt.CORTE_ESPECIAL);
		sqlComum += " OR ad.SERV_SUBTIPO_CODIGO = ?";						ps.adicionarLong(ServentiaSubtipoDt.CONSELHO_SUPERIOR_MAGISTRATURA);
		sqlComum += " OR ad.SERV_SUBTIPO_CODIGO = ?";						ps.adicionarLong(ServentiaSubtipoDt.SECAO_CRIMINAL);	
		sqlComum += " OR ad.SERV_SUBTIPO_CODIGO = ?)";						ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CRIMINAL);

		sql += sqlComum;
		sql += " ORDER BY AREA_DIST_CODIGO, AREA_DIST";
		
		try{
			rs1 = consultarPaginacao(sql, ps, posicao);
			while (rs1.next()) {
				AreaDistribuicaoDt obTemp = new AreaDistribuicaoDt();
				obTemp.setId(rs1.getString("ID_AREA_DIST"));
				obTemp.setAreaDistribuicao(rs1.getString("AREA_DIST"));
				obTemp.setAreaDistribuicaoCodigo(rs1.getString("AREA_DIST_CODIGO"));
				obTemp.setId_Comarca(rs1.getString("ID_COMARCA"));
				obTemp.setId_ServentiaSubtipo(rs1.getString("ID_SERV_SUBTIPO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			sql = "SELECT COUNT(*) as QUANTIDADE " + sqlComum;
			rs2 = consultar(sql, ps);
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));			
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
			try{
				if (rs2 != null) rs2.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}

	/**
	 * Retorna a área de distribuição relacionada a área passada.
	 * No primeiro momento essa área de distribuição relacionada será somente do tipo recursal.
	 * 
	 * @param ID_AREA_DIST, identificação da área principal
	 * @author msapaula
	 */
	/*public AreaDistribuicaoDt getAreaDistribuicaoRelacionada(String ID_AREA_DIST){
		String Sql;
		AreaDistribuicaoDt areaRelacionada = null;
		ResultSetTJGO rs1 = null;
		Sql = "SELECT ID_AREA_DIST, AREA_DIST, FORUM_CODIGO, ID_COMARCA, ID_SERV_SUBTIPO, SERV_SUBTIPO_CODIGO FROM PROJUDI.VIEW_AREA_DIST a WHERE ID_AREA_DIST = ";
		Sql += " (SELECT ad.ID_AREA_DIST_RELACIONADA FROM PROJUDI.AREA_DIST ad ";
		Sql += " WHERE ad.ID_AREA_DIST = " + ID_AREA_DIST + ")";

		try{
			rs1 = consultar(Sql);
			if (rs1.next()) {
				areaRelacionada = new AreaDistribuicaoDt();
				areaRelacionada.setId(rs1.getString("ID_AREA_DIST"));
				areaRelacionada.setAreaDistribuicao(rs1.getString("AREA_DIST"));
				areaRelacionada.setId_Comarca(rs1.getString("ID_COMARCA"));
				areaRelacionada.setForumCodigo(rs1.getString("FORUM_CODIGO"));
				areaRelacionada.setServentiaSubtipoCodigo(rs1.getString("SERV_SUBTIPO_CODIGO"));
				areaRelacionada.setId_ServentiaSubtipo(rs1.getString("ID_SERV_SUBTIPO"));
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return areaRelacionada;
	}*/

	/**
	 * Busca uma área de distribuição baseado no código passado
	 * 
	 * @param AREA_DIST_CODIGO, código da área a ser pesquisada
	 * @return AreaDistribuicaoDt: área de distribuição solicitada
	 * 
	 * @author msapaula
	 * 
	 */
	public AreaDistribuicaoDt consultarAreaDistribuicaoCodigo(String AREA_DIST_CODIGO) throws Exception {
		AreaDistribuicaoDt areaDistribuicaoDt = null;
		String sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		try{

			sql = "SELECT ID_AREA_DIST, AREA_DIST, FORUM_CODIGO, ID_COMARCA, ID_SERV_SUBTIPO, CODIGO_TEMP, SERV_SUBTIPO_CODIGO";					
			sql += " FROM PROJUDI.VIEW_AREA_DIST a WHERE a.AREA_DIST_CODIGO = ?";
			ps.adicionarLong(AREA_DIST_CODIGO);

			rs1 = consultar(sql, ps);
			if (rs1.next()) {
				areaDistribuicaoDt = new AreaDistribuicaoDt();
				areaDistribuicaoDt.setId(rs1.getString("ID_AREA_DIST"));
				areaDistribuicaoDt.setAreaDistribuicao(rs1.getString("AREA_DIST"));
				areaDistribuicaoDt.setForumCodigo(rs1.getString("FORUM_CODIGO"));
				areaDistribuicaoDt.setId_Comarca(rs1.getString("ID_COMARCA"));
				areaDistribuicaoDt.setId_ServentiaSubtipo(rs1.getString("ID_SERV_SUBTIPO"));
				areaDistribuicaoDt.setServentiaSubtipoCodigo(rs1.getString("SERV_SUBTIPO_CODIGO"));
				areaDistribuicaoDt.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
			}
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return areaDistribuicaoDt;
	}

	public String consultarIdAreaDistribuicao(String AREA_DIST_CODIGO) throws Exception {
		String id = "";
		String sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		try{
			sql = "SELECT ID_AREA_DIST FROM PROJUDI.VIEW_AREA_DIST a WHERE a.AREA_DIST_CODIGO = ?";
			ps.adicionarLong(AREA_DIST_CODIGO);
			rs1 = consultar(sql, ps);
			if (rs1.next()) {
				id = rs1.getString("ID_AREA_DIST");
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return id;
	}
	/**
	 * Consulta de Áreas de Distribuição disponíveis em uma Comarca.
	 * @param descricao - descrição da área de distribuição (opcional)
	 * @param ID_COMARCA, identificação da comarca (obrigatório)
	 * @param idServentiaSubTipo - identificação da serventia sub-tipo (obrigatório)
	 * @return lista de áreas de distribuição
	 * 
	 * @author hmgodinho
	 */
	public List consultarAreasDistribuicaoComarcaServentiaSubTipo(String descricao, String idComarca, String idServentiaSubTipo, String posicao) throws Exception {
		String Sql;	
		String SqlComum;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT ID_AREA_DIST,AREA_DIST, FORUM_CODIGO ";
		SqlComum = " FROM PROJUDI.VIEW_AREA_DIST WHERE ID_COMARCA = ?";
		ps.adicionarLong(idComarca);		
		if(descricao != null && !descricao.equalsIgnoreCase("")){
			SqlComum += " AND AREA_DIST LIKE ? ";
			ps.adicionarString("%"+ descricao +"%");
		}
		
		if (idServentiaSubTipo != null && !idServentiaSubTipo.equalsIgnoreCase("")){
			SqlComum += " AND ID_SERV_SUBTIPO = ?";
			ps.adicionarLong(idServentiaSubTipo);
		}
		
		SqlComum += " AND CODIGO_TEMP = ?";
		ps.adicionarLong(AreaDistribuicaoDt.ATIVO);
		
		Sql += SqlComum + " ORDER BY AREA_DIST";		
		try{

			rs1 = consultarPaginacao(Sql, ps, posicao);

			while (rs1.next()) {
				AreaDistribuicaoDt obTemp = new AreaDistribuicaoDt();
				obTemp.setId(rs1.getString("ID_AREA_DIST"));
				obTemp.setAreaDistribuicao(rs1.getString("AREA_DIST"));
				obTemp.setForumCodigo(rs1.getString("FORUM_CODIGO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			Sql = "SELECT COUNT(*) as QUANTIDADE " + SqlComum;
			rs2 = consultar(Sql, ps);
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));			
			//rs1.close();
		
			//rs1.close();
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
			try{
				if (rs2 != null) rs2.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}

	/**
	 * Consulta a Áreas de Distribuição do tipo Precatória disponível em uma Comarca
	 * 
	 * @param ID_COMARCA, identificação da comarca
	 * 
	 * @author msapaula
	 */
	public String consultarAreaDistribuicaoPrecatoria(String ID_COMARCA) throws Exception {
		String sql = "";
		String ID_AREA_DIST = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql = "SELECT ID_AREA_DIST FROM PROJUDI.VIEW_AREA_DIST ";
		sql += " WHERE ID_COMARCA = ?"; 		ps.adicionarLong(ID_COMARCA);
		sql += " AND SERV_SUBTIPO_CODIGO = ?"; 	ps.adicionarLong(ServentiaSubtipoDt.PRECATORIA);
		sql += " AND CODIGO_TEMP = ?";			ps.adicionarLong(AreaDistribuicaoDt.ATIVO);

		try{
			rs1 = consultar(sql, ps);

			if (rs1.next()) {
				ID_AREA_DIST = rs1.getString("ID_AREA_DIST");
			}

		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return ID_AREA_DIST;
	}
	
	/**
	 * Consulta as Áreas de Distribuição de acordo com o SubTipo da Serventia e Comarca passados
	 * 
	 * @param SERV_SUBTIPO_CODIGO, identificação do sub-tipo da serventia
	 * @param ID_COMARCA, identificação da comarca
	 * 
	 * @author msapaula
	 */
	public List consultarAreasDistribuicaoServentiaSubTipo(String SERV_SUBTIPO_CODIGO, String ID_COMARCA) throws Exception {
		String sql = "";
		List listaAreasDistribuicao = null;
		String ID_AREA_DIST = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql = "SELECT ID_AREA_DIST FROM PROJUDI.VIEW_AREA_DIST ";
		sql += " WHERE ID_COMARCA = ?"; 		ps.adicionarLong(ID_COMARCA);
		sql += " AND SERV_SUBTIPO_CODIGO = ?"; 	ps.adicionarLong(SERV_SUBTIPO_CODIGO);
		sql += " AND CODIGO_TEMP = ?"; 			ps.adicionarLong(AreaDistribuicaoDt.ATIVO);

		try{
			rs1 = consultar(sql, ps);

			while (rs1.next()) {
				if (listaAreasDistribuicao == null) {
					listaAreasDistribuicao = new ArrayList();
				}
				ID_AREA_DIST = rs1.getString("ID_AREA_DIST");
				listaAreasDistribuicao.add(ID_AREA_DIST);
			}

		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return listaAreasDistribuicao;
	}
	
	/**
	 * Consulta de Áreas de Distribuição Cíveis no Segundo Grau disponíveis em uma Comarca
	 * 
	 * @param nome, descrição para filtro
	 * @param ID_COMARCA, identificação da comarca
	 * @param posicao, pagina solicitada
	 * 
	 * @author msapaula
	 */
	public List consultarAreasDistribuicaoSegundoGrauCivel(String nome, String ID_COMARCA, String posicao) throws Exception {
		String Sql;
		String SqlComun;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		SqlComun = " FROM PROJUDI.VIEW_AREA_DIST WHERE AREA_DIST LIKE ?";
		ps.adicionarString( nome +"%");
		if (ID_COMARCA.length() > 0){
			SqlComun += " AND ID_COMARCA = ?";
			ps.adicionarLong(ID_COMARCA);
		}
		SqlComun += " AND (SERV_SUBTIPO_CODIGO = ?";					ps.adicionarLong(ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";						ps.adicionarLong(ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL_CRIMINAL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";						ps.adicionarLong(ServentiaSubtipoDt.UPJ_TURMA_RECURSAL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";						ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CIVEL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";						ps.adicionarLong(ServentiaSubtipoDt.PLANTAO_SEGUNDO_GRAU);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";						ps.adicionarLong(ServentiaSubtipoDt.SECAO_CIVEL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";						ps.adicionarLong(ServentiaSubtipoDt.CONSELHO_SUPERIOR_MAGISTRATURA);		
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?)";						ps.adicionarLong(ServentiaSubtipoDt.CORTE_ESPECIAL);
		SqlComun += " AND CODIGO_TEMP = ?";
		ps.adicionarLong(AreaDistribuicaoDt.ATIVO);

		Sql = "SELECT ID_AREA_DIST,AREA_DIST, FORUM_CODIGO, ID_SERV_SUBTIPO ";
		Sql += SqlComun + " ORDER BY AREA_DIST";		
		try{

			rs1 = consultarPaginacao(Sql, ps, posicao);

			while (rs1.next()) {
				AreaDistribuicaoDt obTemp = new AreaDistribuicaoDt();
				obTemp.setId(rs1.getString("ID_AREA_DIST"));
				obTemp.setAreaDistribuicao(rs1.getString("AREA_DIST"));
				obTemp.setForumCodigo(rs1.getString("FORUM_CODIGO"));
				obTemp.setId_ServentiaSubtipo(rs1.getString("ID_SERV_SUBTIPO"));
				liTemp.add(obTemp);
			}
			Sql = "SELECT COUNT(*) as QUANTIDADE " + SqlComun;
			rs2 = consultar(Sql, ps);
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));			
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
			try{
				if (rs2 != null) rs2.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}
	
	/**
	 * Consulta de Áreas de Distribuição Criminais no Segundo Grau disponíveis em uma Comarca
	 * 
	 * @param nome, descrição para filtro
	 * @param ID_COMARCA, identificação da comarca
	 * @param posicao, pagina solicitada
	 * 
	 * @author msapaula
	 */
	public List consultarAreasDistribuicaoSegundoGrauCriminal(String nome, String ID_COMARCA, String posicao) throws Exception {
		String Sql;
		String SqlComun;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		SqlComun = " FROM PROJUDI.VIEW_AREA_DIST WHERE AREA_DIST LIKE ?";
		ps.adicionarString( nome +"%");
		if (ID_COMARCA.length() > 0){
			SqlComun += " AND ID_COMARCA = ?";
			ps.adicionarLong(ID_COMARCA);
		}
		SqlComun += " AND (SERV_SUBTIPO_CODIGO = ?";		ps.adicionarLong(ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL_CRIMINAL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";			ps.adicionarLong(ServentiaSubtipoDt.TURMA_RECURSAL_CRIMINAL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";			ps.adicionarLong(ServentiaSubtipoDt.UPJ_TURMA_RECURSAL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";			ps.adicionarLong(ServentiaSubtipoDt.PLANTAO_SEGUNDO_GRAU);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";			ps.adicionarLong(ServentiaSubtipoDt.SECAO_CRIMINAL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?)";			ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CRIMINAL);
		SqlComun += " AND CODIGO_TEMP = ?";					ps.adicionarLong(AreaDistribuicaoDt.ATIVO);

		Sql = "SELECT ID_AREA_DIST, AREA_DIST, FORUM_CODIGO, ID_SERV_SUBTIPO ";
		Sql += SqlComun + " ORDER BY AREA_DIST";		
		try{

			rs1 = consultarPaginacao(Sql, ps, posicao);

			while (rs1.next()) {
				AreaDistribuicaoDt obTemp = new AreaDistribuicaoDt();
				obTemp.setId(rs1.getString("ID_AREA_DIST"));
				obTemp.setAreaDistribuicao(rs1.getString("AREA_DIST"));
				obTemp.setForumCodigo(rs1.getString("FORUM_CODIGO"));
				obTemp.setId_ServentiaSubtipo(rs1.getString("ID_SERV_SUBTIPO"));
				liTemp.add(obTemp);
			}
			Sql = "SELECT COUNT(*) as QUANTIDADE " + SqlComun;
			rs2 = consultar(Sql, ps);
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));
			
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
			try{
				if (rs2 != null) rs2.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}
	
	/**
	 * Consulta de Áreas de Distribuição Ativas disponíveis em uma Comarca
	 * 
	 * @param nome, descrição para filtro
	 * @param ID_COMARCA, identificação da comarca
	 * @param posicao, pagina solicitada
	 * 
	 * @author Márcio Gomes
	 */
	public List consultarAreasDistribuicao(String nome, String ID_COMARCA, String posicao) throws Exception {
		String Sql;
		String SqlComun = "";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		SqlComun = " FROM PROJUDI.VIEW_AREA_DIST WHERE AREA_DIST LIKE ?";
		ps.adicionarString( "%" + nome + "%");
		if (ID_COMARCA.length() > 0){
			SqlComun += " AND ID_COMARCA =?";	
			ps.adicionarLong(ID_COMARCA);
		}
		SqlComun += " AND CODIGO_TEMP = ?";
		ps.adicionarLong(AreaDistribuicaoDt.ATIVO);

		Sql = "SELECT ID_AREA_DIST, AREA_DIST, FORUM_CODIGO, ID_SERV_SUBTIPO,  AREA_DIST_CODIGO ";
		Sql += SqlComun + " ORDER BY AREA_DIST";		
		try{

			rs1 = consultarPaginacao(Sql, ps, posicao);

			while (rs1.next()) {
				AreaDistribuicaoDt obTemp = new AreaDistribuicaoDt();
				obTemp.setId(rs1.getString("ID_AREA_DIST"));
				obTemp.setAreaDistribuicao(rs1.getString("AREA_DIST"));
				obTemp.setForumCodigo(rs1.getString("FORUM_CODIGO"));
				obTemp.setId_ServentiaSubtipo(rs1.getString("ID_SERV_SUBTIPO"));
				obTemp.setAreaDistribuicaoCodigo(rs1.getString("AREA_DIST_CODIGO"));
				liTemp.add(obTemp);
			}
			Sql = "SELECT COUNT(*) as QUANTIDADE " + SqlComun;
			rs2 = consultar(Sql, ps);
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));			
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
			try{
				if (rs2 != null) rs2.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}

	/**
	 * Consulta a descrição do fórum a partir do idAreaDistribuicao
	 * @author wcsilva
	 */
	public String consultarDescricaoForum(String idAreaDistribuicao) throws Exception {
		String sql;
		String descricao = "";
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql = "SELECT a.FORUM FROM PROJUDI.VIEW_AREA_DIST a WHERE a.ID_AREA_DIST = ?";
		ps.adicionarLong(idAreaDistribuicao);
		try{
			rs = consultar(sql, ps);
			if (rs.next()) {
				descricao = rs.getString("FORUM");
			}
		
		} finally{
			rs.close();
		}
		return descricao;
	}
		
	/**
	 * Consulta codigo do fórum a partir do idAreaDistribuicao
	 * @author lsbernardes
	 */
	public String consultarCodigoForum(String idAreaDistribuicao) throws Exception {
		String sql;
		String descricao = "";
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql = "SELECT a.FORUM_CODIGO FROM PROJUDI.VIEW_AREA_DIST a WHERE a.ID_AREA_DIST = ?";
		ps.adicionarLong(idAreaDistribuicao);
		try{
			rs = consultar(sql, ps);
			if (rs.next()) {
				descricao = rs.getString("FORUM_CODIGO");
			}
		
		} finally{
			rs.close();
		}
		return descricao;
	}
	
	
	
	/**
	 * Método que consulta as Áreas de Distribuição relacionadas à Serventia.
	 * @param idServentia - ID da Serventia
	 * @return lista de Áreas de Distribuição
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarAreasDistribuicaoServentia(String idServentia) throws Exception {
		List listaAreasDistribuicao = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "SELECT vsad.ID_AREA_DIST, vsad.AREA_DIST FROM PROJUDI.VIEW_SERV_AREA_DIST vsad WHERE vsad.ID_SERV = ?";
		ps.adicionarLong(idServentia);

		try{
			rs1 = consultar(sql, ps);

			while (rs1.next()) {
				if (listaAreasDistribuicao == null){
					listaAreasDistribuicao = new ArrayList();
				}
				AreaDistribuicaoDt obTemp = new AreaDistribuicaoDt();
				obTemp.setId(rs1.getString("ID_AREA_DIST"));
				obTemp.setAreaDistribuicao(rs1.getString("AREA_DIST"));
				listaAreasDistribuicao.add(obTemp);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return listaAreasDistribuicao;
	}	
	
	/**
	 * Altera o status de uma area de distribuição.
	 * @param idAREA_DISTribuicao
	 * @param status
	 * @throws Exception
	 * @author mmgomes
	 */
	public void atualizarStatus(String idAREA_DISTribuicao, String status) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.AREA_DIST SET  ";
		Sql += "CODIGO_TEMP = ?";
		ps.adicionarLong(status);	
		Sql += " WHERE ID_AREA_DIST = ?";
		ps.adicionarLong(idAREA_DISTribuicao); 

		executarUpdateDelete(Sql,ps);
	}

	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {

		String stSql="";
		String stSqlFrom="";
		String stSqlOrder="";
		String stTemp = "";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;

		stSql= "SELECT ID_AREA_DIST AS ID, AREA_DIST AS DESCRICAO1";
		stSqlFrom = " FROM PROJUDI.VIEW_AREA_DIST";
		stSqlFrom += " WHERE AREA_DIST LIKE ?";
		stSqlOrder = " ORDER BY AREA_DIST ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);
			stSql= "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		} finally {
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return stTemp; 
	}	
	
	public String consultarDescricaoJSON(String descricao, String posicao, String ordenacao, String quantidadeRegistros) throws Exception {

		int qtdeColunas;
		String stSql="";
		String stSqlFrom="";
		String stSqlOrder="";
		String stTemp = "";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		
		stSql = "SELECT ID_AREA_DIST AS ID, AREA_DIST AS DESCRICAO1, FORUM AS descricao2, SERV_SUBTIPO AS descricao3";
		stSqlFrom = " FROM PROJUDI.VIEW_AREA_DIST";
		
		qtdeColunas = 3;
		stSqlFrom += " WHERE (AREA_DIST LIKE ? OR SERV_SUBTIPO LIKE ? OR FORUM LIKE ?) ORDER BY " + ordenacao;
		ps.adicionarString("%" + descricao + "%");
		ps.adicionarString("%" + descricao + "%");
		ps.adicionarString("%" + descricao + "%");

		try{
			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao, quantidadeRegistros);
			stSql= "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		} finally {
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return stTemp; 
	}
		
	public String consultarAreasDistribuicaoComarcaServentiaSubTipoJSON(String descricao, String idComarca, String idServentiaSubTipo, String posicao) throws Exception {
		String Sql;	
		String SqlComum;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 2;

		Sql = "SELECT ID_AREA_DIST AS ID, AREA_DIST AS DESCRICAO1, FORUM_CODIGO AS DESCRICAO2 ";
		SqlComum = " FROM PROJUDI.VIEW_AREA_DIST WHERE ID_COMARCA = ?";
		ps.adicionarLong(idComarca);		
		if(descricao != null && !descricao.equalsIgnoreCase("")){
			SqlComum += " AND AREA_DIST LIKE ? ";
			ps.adicionarString("%"+ descricao +"%");
		}
		
		if (idServentiaSubTipo != null && !idServentiaSubTipo.equalsIgnoreCase("")){
			SqlComum += " AND ID_SERV_SUBTIPO = ?";
			ps.adicionarLong(idServentiaSubTipo);
		}
		
		SqlComum += " AND CODIGO_TEMP = ?";
		ps.adicionarLong(AreaDistribuicaoDt.ATIVO);
		
		Sql += SqlComum + " ORDER BY AREA_DIST";		
		try{

			rs1 = consultarPaginacao(Sql, ps, posicao);
			Sql = "SELECT COUNT(*) as QUANTIDADE " + SqlComum;
			rs2 = consultar(Sql, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
	
	public String consultarAreasDistribuicaoSegundoGrauAtivasJSON(String descricao, String posicao) throws Exception {
		String sql;
		String sqlComum = "";
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 4;

		sql = "SELECT  ad.ID_AREA_DIST AS ID, ad.AREA_DIST AS DESCRICAO1, ad.AREA_DIST_CODIGO AS DESCRICAO2, ad.ID_COMARCA AS DESCRICAO3, ad.ID_SERV_SUBTIPO AS DESCRICAO4 ";
		sqlComum += " FROM PROJUDI.VIEW_AREA_DIST ad WHERE ad.AREA_DIST LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		sqlComum += " AND ( ad.CODIGO_TEMP = ? OR ad.CODIGO_TEMP = ? )";
		ps.adicionarLong(AreaDistribuicaoDt.ATIVO);
		ps.adicionarLong(AreaDistribuicaoDt.BLOQUEADO);
		sqlComum += " AND (ad.SERV_SUBTIPO_CODIGO = ?";					ps.adicionarLong(ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL);
		sqlComum += " OR ad.SERV_SUBTIPO_CODIGO = ?";					ps.adicionarLong(ServentiaSubtipoDt.TURMA_RECURSAL_CRIMINAL);
		sqlComum += " OR ad.SERV_SUBTIPO_CODIGO = ?";					ps.adicionarLong(ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL_CRIMINAL);
		sqlComum += " OR ad.SERV_SUBTIPO_CODIGO = ?";					ps.adicionarLong(ServentiaSubtipoDt.UPJ_TURMA_RECURSAL);
		sqlComum += " OR ad.SERV_SUBTIPO_CODIGO = ?";					ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CIVEL);
		sqlComum += " OR ad.SERV_SUBTIPO_CODIGO = ?";					ps.adicionarLong(ServentiaSubtipoDt.PLANTAO_SEGUNDO_GRAU);
		sqlComum += " OR ad.SERV_SUBTIPO_CODIGO = ?";					ps.adicionarLong(ServentiaSubtipoDt.CORTE_ESPECIAL);
		sqlComum += " OR ad.SERV_SUBTIPO_CODIGO = ?";					ps.adicionarLong(ServentiaSubtipoDt.SECAO_CIVEL);
		sqlComum += " OR ad.SERV_SUBTIPO_CODIGO = ?";					ps.adicionarLong(ServentiaSubtipoDt.CONSELHO_SUPERIOR_MAGISTRATURA);
		sqlComum += " OR ad.SERV_SUBTIPO_CODIGO = ?";					ps.adicionarLong(ServentiaSubtipoDt.SECAO_CRIMINAL);
		sqlComum += " OR ad.SERV_SUBTIPO_CODIGO = ?)";					ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CRIMINAL);

		sql += sqlComum;
		sql += " ORDER BY AREA_DIST_CODIGO, AREA_DIST";
		
		try{
			rs1 = consultarPaginacao(sql, ps, posicao);
			sql = "SELECT COUNT(*) as QUANTIDADE " + sqlComum;
			rs2 = consultar(sql, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
	
	public String consultarAreasDistribuicaoAtivasJSON(String descricao, String posicao) throws Exception {
		String sql;
		String sqlComum = "";
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 1;
		
		sql = "SELECT  ad.ID_AREA_DIST AS ID, ad.AREA_DIST AS DESCRICAO1 ";
		sqlComum += " FROM PROJUDI.VIEW_AREA_DIST ad WHERE ad.AREA_DIST LIKE ? ";		ps.adicionarString("%"+ descricao +"%");
		sqlComum += " AND ad.CODIGO_TEMP <> ? ";										ps.adicionarLong(AreaDistribuicaoDt.INATIVO);
		sql += sqlComum;
		sql += " ORDER BY AREA_DIST_CODIGO, AREA_DIST";
				
		try{
			rs1 = consultarPaginacao(sql, ps, posicao);
			sql = "SELECT COUNT(*) as QUANTIDADE " + sqlComum;
			rs2 = consultar(sql, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
	
	public String consultarAreasDistribuicaoPreprocessualAtivasJSON(String descricao, String posicao) throws Exception {
		String sql;
		String sqlComum = "";
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 1;
		
		sql = "SELECT  ad.ID_AREA_DIST AS ID, ad.AREA_DIST AS DESCRICAO1 ";
		sqlComum += " FROM PROJUDI.VIEW_AREA_DIST ad WHERE ad.AREA_DIST LIKE ? ";		ps.adicionarString("%"+ descricao +"%");
		sqlComum += " AND ad.CODIGO_TEMP <> ? ";										ps.adicionarLong(AreaDistribuicaoDt.INATIVO);
		 //alteração solicitada por Ana Cláudia atendendo solicitação do Dr. Aureliano.
		sqlComum += " AND ad.SERV_SUBTIPO_CODIGO <> ? ";								ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CRIMINAL);
		sqlComum += " AND ad.SERV_SUBTIPO_CODIGO = ? ";									ps.adicionarLong(ServentiaSubtipoDt.PREPROCESSUAL);
		
		sql += sqlComum;
		sql += " ORDER BY AREA_DIST_CODIGO, AREA_DIST";
				
		try{
			rs1 = consultarPaginacao(sql, ps, posicao);
			sql = "SELECT COUNT(*) as QUANTIDADE " + sqlComum;
			rs2 = consultar(sql, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
	
	public String consultarAreasDistribuicaoCivelJSON(String nome, String id_comarca, String id_Serventia_Usuario, String posicao) throws Exception {
		String Sql;
		String SqlComun = "";
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 3;
		
		SqlComun = " FROM PROJUDI.VIEW_AREA_DIST ";
		SqlComun += " WHERE SERV_SUBTIPO_CODIGO in (  ? ,";	ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL);		
		SqlComun += "  ? , ";									ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL_CRIMINAL);
		SqlComun += "  ? , ";									ps.adicionarLong(ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL);
		SqlComun += "  ? , ";									ps.adicionarLong(ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL_EXECUACAO_FISCAL);
		SqlComun += "  ? , ";									ps.adicionarLong(ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL_INTERIOR);
		SqlComun += "  ? , ";									ps.adicionarLong(ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL);
		SqlComun += "  ? , ";									ps.adicionarLong(ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL_EXECUACAO_FISCAL);
		SqlComun += "  ? , ";									ps.adicionarLong(ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL_INTERIOR);
		SqlComun += "  ? , ";									ps.adicionarLong(ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL_ESTADUAL);
		SqlComun += "  ? , ";									ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_FAZENDA_PUBLICA);
		SqlComun += "  ? , ";									ps.adicionarLong(ServentiaSubtipoDt.UPJ_JUIZADO_ESPECIAL_FAZENDA_PUBLICA);
		SqlComun += "  ? , ";									ps.adicionarLong(ServentiaSubtipoDt.FAMILIA_CAPITAL);
		SqlComun += "  ? , ";									ps.adicionarLong(ServentiaSubtipoDt.UPJ_FAMILIA);
		SqlComun += "  ? , ";									ps.adicionarLong(ServentiaSubtipoDt.FAMILIA_INTERIOR);
		SqlComun += "  ? , ";									ps.adicionarLong(ServentiaSubtipoDt.UPJ_SUCESSOES);
		SqlComun += "  ? , ";									ps.adicionarLong(ServentiaSubtipoDt.VARAS_CIVEL);
		SqlComun += "  ? , ";									ps.adicionarLong(ServentiaSubtipoDt.AUDITORIA_MILITAR_CIVEL);		
		SqlComun += "  ? , ";									ps.adicionarLong(ServentiaSubtipoDt.PLANTAO_PRIMEIRO_GRAU);
		SqlComun += "  ? , ";									ps.adicionarLong(ServentiaSubtipoDt.PLANTAO_AUDIENCIA_CUSTODIA);
		SqlComun += "  ? , ";									ps.adicionarLong(ServentiaSubtipoDt.PRECATORIA);
		SqlComun += "  ? , ";									ps.adicionarLong(ServentiaSubtipoDt.INFANCIA_JUVENTUDE_CIVEL);		
		SqlComun += "  ? ) ";									ps.adicionarLong(ServentiaSubtipoDt.PREPROCESSUAL);
		SqlComun += " AND CODIGO_TEMP = ?";					ps.adicionarLong(AreaDistribuicaoDt.ATIVO);
		
		if (nome != null && nome.trim().length() > 0)	{
			SqlComun += " AND AREA_DIST LIKE ? ";
			ps.adicionarString("%" + nome.trim() + "%");
		}		
		if (id_comarca != null && id_comarca.trim().length() > 0 && Funcoes.StringToLong(id_comarca.trim()) > 0) {
			SqlComun += " AND ID_COMARCA = ? ";
			ps.adicionarLong(id_comarca.trim());			 
		}
		//Se for informado o id_Serventia_Usuario, serão apresentadas
		//as áreas de distribuição ligadas à serventia do usuário.
		if(id_Serventia_Usuario != null && id_Serventia_Usuario.trim().length() > 0) {
			SqlComun += "  OR ID_AREA_DIST IN (SELECT ID_AREA_DIST FROM SERV_AREA_DIST WHERE ID_SERV = ?) ";
			ps.adicionarLong(id_Serventia_Usuario.trim());
		}

		Sql = "SELECT ID_AREA_DIST AS ID, AREA_DIST AS DESCRICAO1, FORUM_CODIGO AS DESCRICAO2, ID_SERV_SUBTIPO AS DESCRICAO3 ";
		Sql += SqlComun + " ORDER BY AREA_DIST";		
		try{

			rs1 = consultarPaginacao(Sql, ps, posicao);
			Sql = "SELECT COUNT(*) as QUANTIDADE " + SqlComun;
			rs2 = consultar(Sql, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {
			}
		}
		return stTemp;
	}
	
	public String consultarAreasDistribuicaoCriminalJSON(String nome, String id_comarca, String posicao) throws Exception {
		String Sql;
		String SqlComun = "";
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 3;
		
		SqlComun = " FROM PROJUDI.VIEW_AREA_DIST ";		
		SqlComun += " WHERE (SERV_SUBTIPO_CODIGO = ?";		ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CRIMINAL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";			ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL_CRIMINAL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";			ps.adicionarLong(ServentiaSubtipoDt.PRECATORIA);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";			ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_VIOLENCIA_DOMESTICA);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";			ps.adicionarLong(ServentiaSubtipoDt.PLANTAO_PRIMEIRO_GRAU);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";			ps.adicionarLong(ServentiaSubtipoDt.PLANTAO_AUDIENCIA_CUSTODIA);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";			ps.adicionarLong(ServentiaSubtipoDt.PLANTAO_SEGUNDO_GRAU);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";		    ps.adicionarLong(ServentiaSubtipoDt.VARAS_EXECUCAO_PENAL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";		    ps.adicionarLong(ServentiaSubtipoDt.VARAS_EXECUCAO_PENAL_ALTERNATIVA);
		
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";			ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CRIMINAL_FAZENDA_PUBLICA);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";			ps.adicionarLong(ServentiaSubtipoDt.VARA_CRIMINAL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";			ps.adicionarLong(ServentiaSubtipoDt.UPJ_CUSTODIA);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";			ps.adicionarLong(ServentiaSubtipoDt.SECAO_CRIMINAL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";			ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CRIMINAL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";			ps.adicionarLong(ServentiaSubtipoDt.AUDITORIA_MILITAR_CRIMINAL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";			ps.adicionarLong(ServentiaSubtipoDt.UPJ_CRIMINAL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";			ps.adicionarLong(ServentiaSubtipoDt.UPJ_VIOLENCIA_DOMESTICA);
		
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?)";			ps.adicionarLong(ServentiaSubtipoDt.INFANCIA_JUVENTUDE_INFRACIONAL);
		SqlComun += " AND CODIGO_TEMP = ?";					ps.adicionarLong(AreaDistribuicaoDt.ATIVO);
		
		if (nome != null && nome.trim().length() > 0){
			SqlComun += " AND AREA_DIST LIKE ? ";		ps.adicionarString("%"+ nome.trim() +"%");	
		}		
		if (id_comarca != null && id_comarca.trim().length() > 0 && Funcoes.StringToLong(id_comarca.trim()) > 0){
			SqlComun += " AND ID_COMARCA =?";				ps.adicionarLong(id_comarca.trim());
		}		

		Sql = "SELECT ID_AREA_DIST AS ID, AREA_DIST AS DESCRICAO1, FORUM_CODIGO AS DESCRICAO2, ID_SERV_SUBTIPO AS DESCRICAO3 ";
		Sql += SqlComun + " ORDER BY AREA_DIST";		
		try{
			rs1 = consultarPaginacao(Sql, ps, posicao);
			Sql = "SELECT COUNT(*) as QUANTIDADE " + SqlComun;
			rs2 = consultar(Sql, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
	
	/**
	 * Método que consulta áreas de distribuição criminais de primeiro grau.
	 * @param nome - nome da área de distribuição
	 * @param id_comarca - Id da comarca da área de distribuição
	 * @param posicao - posição da paginação
	 * @return lista de áreas de distribuição criminais de primeiro grau.
	 * @throws Exception
	 * @author hmgodinho
	 */
	public String consultarAreasDistribuicaoPrimeiroGrauCriminalJSON(String nome, String id_comarca, String posicao) throws Exception {
		String Sql;
		String SqlComun = "";
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 3;
		
		SqlComun = " FROM PROJUDI.VIEW_AREA_DIST ";		
		SqlComun += " WHERE (SERV_SUBTIPO_CODIGO = ?";		ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CRIMINAL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";			ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL_CRIMINAL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";			ps.adicionarLong(ServentiaSubtipoDt.PRECATORIA);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";			ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_VIOLENCIA_DOMESTICA);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";			ps.adicionarLong(ServentiaSubtipoDt.PLANTAO_PRIMEIRO_GRAU);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";			ps.adicionarLong(ServentiaSubtipoDt.PLANTAO_AUDIENCIA_CUSTODIA);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";			ps.adicionarLong(ServentiaSubtipoDt.AUDITORIA_MILITAR_CRIMINAL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";		    ps.adicionarLong(ServentiaSubtipoDt.VARAS_EXECUCAO_PENAL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";		    ps.adicionarLong(ServentiaSubtipoDt.VARAS_EXECUCAO_PENAL_ALTERNATIVA);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";			ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CRIMINAL_FAZENDA_PUBLICA);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";			ps.adicionarLong(ServentiaSubtipoDt.VARA_CRIMINAL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";			ps.adicionarLong(ServentiaSubtipoDt.UPJ_CRIMINAL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";			ps.adicionarLong(ServentiaSubtipoDt.UPJ_VIOLENCIA_DOMESTICA);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";			ps.adicionarLong(ServentiaSubtipoDt.UPJ_CUSTODIA);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?)";			ps.adicionarLong(ServentiaSubtipoDt.INFANCIA_JUVENTUDE_INFRACIONAL);
		SqlComun += " AND CODIGO_TEMP = ?";					ps.adicionarLong(AreaDistribuicaoDt.ATIVO);
		
		if (nome != null && nome.trim().length() > 0){
			SqlComun += " AND AREA_DIST LIKE ? ";		ps.adicionarString("%"+ nome.trim() +"%");	
		}		
		if (id_comarca != null && id_comarca.trim().length() > 0 && Funcoes.StringToLong(id_comarca.trim()) > 0){
			SqlComun += " AND ID_COMARCA =?";				ps.adicionarLong(id_comarca.trim());
		}		

		Sql = "SELECT ID_AREA_DIST AS ID, AREA_DIST AS DESCRICAO1, FORUM_CODIGO AS DESCRICAO2, ID_SERV_SUBTIPO AS DESCRICAO3 ";
		Sql += SqlComun + " ORDER BY AREA_DIST";		
		try{
			rs1 = consultarPaginacao(Sql, ps, posicao);
			Sql = "SELECT COUNT(*) as QUANTIDADE " + SqlComun;
			rs2 = consultar(Sql, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
	
	/**
	 * Gera Json para distribuição de segundo grau civiel
	 * 25/06/2013
	 * @throws Exception
	 * @author Jesus Rodrigo
	 */
	
	public String consultarAreasDistribuicaoSegundoGrauCivelJSON(String tempNomeBusca, String id_Comarca, String posicaoPaginaAtual) throws Exception {
		String Sql;
		String SqlComun;		
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		String stTemp = "";
		int qtdeColunas = 3;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		SqlComun = " FROM PROJUDI.VIEW_AREA_DIST " ;
		SqlComun += " WHERE (SERV_SUBTIPO_CODIGO = ?";		ps.adicionarLong(ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";  		ps.adicionarLong(ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL_CRIMINAL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";  		ps.adicionarLong(ServentiaSubtipoDt.UPJ_TURMA_RECURSAL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";  		ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CIVEL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";  		ps.adicionarLong(ServentiaSubtipoDt.PLANTAO_SEGUNDO_GRAU);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";  		ps.adicionarLong(ServentiaSubtipoDt.SECAO_CIVEL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";  		ps.adicionarLong(ServentiaSubtipoDt.CONSELHO_SUPERIOR_MAGISTRATURA);		
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?)";  		ps.adicionarLong(ServentiaSubtipoDt.CORTE_ESPECIAL);
		SqlComun += " AND CODIGO_TEMP = ?";         		ps.adicionarLong(AreaDistribuicaoDt.ATIVO);
		
		if (tempNomeBusca != null && tempNomeBusca.length() > 0){
			SqlComun += " AND AREA_DIST LIKE ?";		ps.adicionarString( tempNomeBusca +"%");
		}
		
		if (id_Comarca.length() > 0){
			SqlComun += " AND ID_COMARCA = ?";		ps.adicionarLong(id_Comarca);
		}
		
		Sql = "SELECT ID_AREA_DIST as ID, AREA_DIST AS DESCRICAO1, FORUM_CODIGO AS DESCRICAO2, ID_SERV_SUBTIPO AS DESCRICAO3 ";
		Sql += SqlComun + " ORDER BY AREA_DIST";		
		try{
			
			rs1 = consultarPaginacao(Sql, ps, posicaoPaginaAtual);
			
			Sql = "SELECT COUNT(*) as QUANTIDADE " + SqlComun;
			rs2 = consultar(Sql, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicaoPaginaAtual, rs1, qtdeColunas);		
			
			
		} finally{
			try{	if (rs1 != null) rs1.close();	} catch(Exception e) {		}
			try{	if (rs2 != null) rs2.close();	} catch(Exception e) {		}
		}
		return stTemp;
	}

	/**
	 * Gera Json para distribuição de segundo grau civiel
	 * 25/06/2013
	 * @throws Exception
	 * @author Jesus Rodrigo
	 */
	
	public String consultarAreasDistribuicaoSegundoGrauCivelJSON(String tempNomeBusca, String id_Comarca, String posicaoPaginaAtual, boolean turmaJulgadora) throws Exception {
		String Sql;
		String SqlComun;		
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		String stTemp = "";
		int qtdeColunas = 3;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		SqlComun = " FROM PROJUDI.VIEW_AREA_DIST " ;
		if(turmaJulgadora) {
			SqlComun += " WHERE (SERV_SUBTIPO_CODIGO = ?";		ps.adicionarLong(ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL);
			SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";  		ps.adicionarLong(ServentiaSubtipoDt.UPJ_TURMA_RECURSAL);	
			SqlComun += " OR SERV_SUBTIPO_CODIGO = ?)";  		ps.adicionarLong(ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL_CRIMINAL);			
		}else {
			SqlComun += " WHERE (SERV_SUBTIPO_CODIGO = ?";  	ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CIVEL);
			SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";  		ps.adicionarLong(ServentiaSubtipoDt.PLANTAO_SEGUNDO_GRAU);
			SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";  		ps.adicionarLong(ServentiaSubtipoDt.SECAO_CIVEL);
			SqlComun += " OR SERV_SUBTIPO_CODIGO = ?";  		ps.adicionarLong(ServentiaSubtipoDt.CONSELHO_SUPERIOR_MAGISTRATURA);		
			SqlComun += " OR SERV_SUBTIPO_CODIGO = ?)";  		ps.adicionarLong(ServentiaSubtipoDt.CORTE_ESPECIAL);
		}
		SqlComun += " AND CODIGO_TEMP = ?";         		ps.adicionarLong(AreaDistribuicaoDt.ATIVO);

		if (tempNomeBusca != null && tempNomeBusca.length() > 0){
			SqlComun += " AND AREA_DIST LIKE ?";		ps.adicionarString( tempNomeBusca +"%");
		}
		
		if (id_Comarca!=null && id_Comarca.length() > 0){
			SqlComun += " AND ID_COMARCA = ?";		ps.adicionarLong(id_Comarca);
		}

		Sql = "SELECT ID_AREA_DIST as ID, AREA_DIST AS DESCRICAO1, FORUM_CODIGO AS DESCRICAO2, ID_SERV_SUBTIPO AS DESCRICAO3 ";
		Sql += SqlComun + " ORDER BY AREA_DIST";		
		try{

			rs1 = consultarPaginacao(Sql, ps, posicaoPaginaAtual);
			
			Sql = "SELECT COUNT(*) as QUANTIDADE " + SqlComun;
			rs2 = consultar(Sql, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicaoPaginaAtual, rs1, qtdeColunas);		
			
		
		} finally{
			try{	if (rs1 != null) rs1.close();	} catch(Exception e) {		}
			try{	if (rs2 != null) rs2.close();	} catch(Exception e) {		}
		}
		return stTemp;
	}
	
	/**
	 * Gera Json para distribuição de segundo grau criminal
	 * 25/06/2013
	 * @throws Exception
	 * @author Jesus Rodrigo
	 */
	public String consultarAreasDistribuicaoSegundoGrauCriminalJSON(String tempNomeBusca, String id_Comarca, String posicaoPaginaAtual) throws Exception {
		String Sql;
		String SqlComun;
		
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		String stTemp = "";
		int qtdeColunas = 3;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		SqlComun = " FROM PROJUDI.VIEW_AREA_DIST WHERE AREA_DIST LIKE ?";		ps.adicionarString( tempNomeBusca +"%");
		if (id_Comarca.length() > 0){
			SqlComun += " AND ID_COMARCA = ?";			ps.adicionarLong(id_Comarca);
		}
		
		SqlComun += " AND (SERV_SUBTIPO_CODIGO = ?";		ps.adicionarLong(ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL_CRIMINAL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?"; 			ps.adicionarLong(ServentiaSubtipoDt.TURMA_RECURSAL_CRIMINAL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?"; 			ps.adicionarLong(ServentiaSubtipoDt.UPJ_TURMA_RECURSAL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?"; 			ps.adicionarLong(ServentiaSubtipoDt.PLANTAO_SEGUNDO_GRAU);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?"; 			ps.adicionarLong(ServentiaSubtipoDt.SECAO_CRIMINAL);
		SqlComun += " OR SERV_SUBTIPO_CODIGO = ?)";			ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CRIMINAL);
		SqlComun += " AND CODIGO_TEMP = ?";					ps.adicionarLong(AreaDistribuicaoDt.ATIVO);

		Sql = "SELECT ID_AREA_DIST as ID, AREA_DIST as DESCRICAO1, FORUM_CODIGO AS DESCRICAO2, ID_SERV_SUBTIPO AS DESCRICAO3 ";
		Sql += SqlComun + " ORDER BY AREA_DIST";		
		try{

			rs1 = consultarPaginacao(Sql, ps, posicaoPaginaAtual);

			Sql = "SELECT COUNT(*) as QUANTIDADE " + SqlComun;
			rs2 = consultar(Sql, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicaoPaginaAtual, rs1, qtdeColunas);		
			
		
		} finally{
			try{	if (rs1 != null) rs1.close();	} catch(Exception e) {			}
			try{	if (rs2 != null) rs2.close();	} catch(Exception e) {			}
		}
		return stTemp;
	}

	/**
	 * Gera Json para distribuição de segundo grau criminal
	 * 25/06/2013
	 * @throws Exception
	 * @author Jesus Rodrigo
	 */
	public String consultarAreasDistribuicaoSegundoGrauCriminalJSON(String tempNomeBusca, String id_Comarca, String posicaoPaginaAtual, boolean turmaJulgadora) throws Exception {
		String Sql;
		String SqlComun;
		
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		String stTemp = "";
		int qtdeColunas = 3;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		SqlComun = " FROM PROJUDI.VIEW_AREA_DIST WHERE AREA_DIST LIKE ?";		ps.adicionarString( tempNomeBusca +"%");
		if (id_Comarca.length() > 0){
			SqlComun += " AND ID_COMARCA = ? AND ";			ps.adicionarLong(id_Comarca);
		}
		
		if(turmaJulgadora) {
			SqlComun += "(SERV_SUBTIPO_CODIGO = ?"; 			ps.adicionarLong(ServentiaSubtipoDt.TURMA_RECURSAL_CRIMINAL);
			SqlComun += " OR SERV_SUBTIPO_CODIGO = ? ";  		ps.adicionarLong(ServentiaSubtipoDt.UPJ_TURMA_RECURSAL);		
			SqlComun += " OR SERV_SUBTIPO_CODIGO = ?)";  		ps.adicionarLong(ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL_CRIMINAL);			
		}else {
			SqlComun += " (SERV_SUBTIPO_CODIGO = ?"; 			ps.adicionarLong(ServentiaSubtipoDt.PLANTAO_SEGUNDO_GRAU);
			SqlComun += " OR SERV_SUBTIPO_CODIGO = ? ";			ps.adicionarLong(ServentiaSubtipoDt.SECAO_CRIMINAL);
			SqlComun += " OR SERV_SUBTIPO_CODIGO = ? ";			ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CRIMINAL);
			// Adicionando Corte Especial em atendimento ao BO 2018/10264
			SqlComun += " OR SERV_SUBTIPO_CODIGO = ?)";  		ps.adicionarLong(ServentiaSubtipoDt.CORTE_ESPECIAL);
		}
		SqlComun += " AND CODIGO_TEMP = ?";					ps.adicionarLong(AreaDistribuicaoDt.ATIVO);
		

		Sql = "SELECT ID_AREA_DIST as ID, AREA_DIST as DESCRICAO1, FORUM_CODIGO AS DESCRICAO2, ID_SERV_SUBTIPO AS DESCRICAO3 ";
		Sql += SqlComun + " ORDER BY AREA_DIST";		
		try{

			rs1 = consultarPaginacao(Sql, ps, posicaoPaginaAtual);

			Sql = "SELECT COUNT(*) as QUANTIDADE " + SqlComun;
			rs2 = consultar(Sql, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicaoPaginaAtual, rs1, qtdeColunas);		
			
		
		} finally{
			try{	if (rs1 != null) rs1.close();	} catch(Exception e) {			}
			try{	if (rs2 != null) rs2.close();	} catch(Exception e) {			}
		}
		return stTemp;
	}

	public String consultarDescricaoServentiaJSON(String descricao, String idServentia, String posicao) throws Exception {
		
		String sql = "";
		String sqlFrom = "";		
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 1;
		
		sql = "SELECT vsad.ID_AREA_DIST AS ID, vsad.AREA_DIST AS DESCRICAO1";
		sqlFrom = " FROM PROJUDI.VIEW_SERV_AREA_DIST vsad";
		sqlFrom += " WHERE vsad.AREA_DIST LIKE ? AND vsad.ID_SERV = ?";
		
		ps.adicionarString("%"+ descricao +"%");
		ps.adicionarLong(idServentia);

		try{
			rs1 = consultar(sql + sqlFrom, ps);
			sql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(sql + sqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return stTemp;
	}
	
	public List<DescritorComboDt> consultarDescricaoCombo(String descricao, String id_comarca, boolean civeis, boolean primeiroGrau) throws Exception {

		String Sql;
		List<DescritorComboDt> listaTemp = new ArrayList<DescritorComboDt>();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();		
		String valorFiltro = super.obtenhaValorFiltroConsultarDescricaoCombo(descricao);
				
		Sql = "SELECT ID_AREA_DIST, AREA_DIST FROM PROJUDI.VIEW_AREA_DIST ";
		Sql += " WHERE SERV_SUBTIPO_CODIGO in (  ";
		if (primeiroGrau) {
			Sql += "  ? , ";ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL_CRIMINAL);
			Sql += "  ? , ";ps.adicionarLong(ServentiaSubtipoDt.PLANTAO_PRIMEIRO_GRAU);
			Sql += "  ? , ";ps.adicionarLong(ServentiaSubtipoDt.PLANTAO_AUDIENCIA_CUSTODIA);
			if (civeis) {
				Sql += "  ? ,";	ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL);
				Sql += "  ? , ";ps.adicionarLong(ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL);
				Sql += "  ? , ";ps.adicionarLong(ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL);
				Sql += "  ? , ";ps.adicionarLong(ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL_ESTADUAL);
				Sql += "  ? , ";ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_FAZENDA_PUBLICA);
				Sql += "  ? , ";ps.adicionarLong(ServentiaSubtipoDt.UPJ_JUIZADO_ESPECIAL_FAZENDA_PUBLICA);
				Sql += "  ? , ";ps.adicionarLong(ServentiaSubtipoDt.FAMILIA_CAPITAL);
				Sql += "  ? , ";ps.adicionarLong(ServentiaSubtipoDt.UPJ_FAMILIA);
				Sql += "  ? , ";ps.adicionarLong(ServentiaSubtipoDt.FAMILIA_INTERIOR);	
				Sql += "  ? , ";ps.adicionarLong(ServentiaSubtipoDt.UPJ_SUCESSOES);	
				Sql += "  ? ";	ps.adicionarLong(ServentiaSubtipoDt.PREPROCESSUAL);
			} else {
				Sql += "  ? ";	ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CRIMINAL);				
				Sql += "  ? ";	ps.adicionarLong(ServentiaSubtipoDt.UPJ_CRIMINAL);
				Sql += "  ? ";	ps.adicionarLong(ServentiaSubtipoDt.UPJ_VIOLENCIA_DOMESTICA);
				Sql += "  ? ";	ps.adicionarLong(ServentiaSubtipoDt.UPJ_CUSTODIA);
			}
		} else {
			Sql += "  ? , ";ps.adicionarLong(ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL_CRIMINAL);
			Sql += "  ? , ";ps.adicionarLong(ServentiaSubtipoDt.UPJ_TURMA_RECURSAL);
			if (civeis) {
				Sql += "  ? ,";	ps.adicionarLong(ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL);				
				Sql += "  ? , ";ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CIVEL);
				Sql += "  ? , ";ps.adicionarLong(ServentiaSubtipoDt.CONSELHO_SUPERIOR_MAGISTRATURA);				
				Sql += "  ? , ";ps.adicionarLong(ServentiaSubtipoDt.SECAO_CIVEL);
				Sql += "  ?   ";ps.adicionarLong(ServentiaSubtipoDt.CORTE_ESPECIAL);	
			} else {
				Sql += "  ? ,";	ps.adicionarLong(ServentiaSubtipoDt.TURMA_RECURSAL_CRIMINAL);
				Sql += "  ? ";	ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CRIMINAL);
			}
		}
		Sql += " ) ";
		Sql += " AND CODIGO_TEMP = ?";	ps.adicionarLong(AreaDistribuicaoDt.ATIVO);
		if (valorFiltro != null) {
			Sql += " AND AREA_DIST LIKE ?"; 
			ps.adicionarString(valorFiltro);	
		}		
		if (id_comarca != null && id_comarca.trim().length() > 0 && Funcoes.StringToLong(id_comarca.trim()) > 0) {
			Sql += " AND ID_COMARCA = ? ";
			ps.adicionarLong(id_comarca.trim());			 
		}		
		Sql += " ORDER BY AREA_DIST";

		try {
			rs1 = consultar(Sql, ps);

			while (rs1.next() && listaTemp.size() <= ProjudiConstantes.QUANTIDADE_OBJETOS_COMBO) {
				DescritorComboDt obTemp = new DescritorComboDt();
				obTemp.setId(rs1.getString("ID_AREA_DIST"));
				obTemp.setDescricao(rs1.getString("AREA_DIST"));				
				listaTemp.add(obTemp);
			}			
		} finally{
             try {if (rs1 != null) rs1.close();} catch (Exception e1) {}             
        }
		return listaTemp;
	}
	
	public boolean isAreaDistribuicaoComCusta(String ID_AREA_DIST )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		boolean retorno = false;

		stSql = "SELECT * FROM PROJUDI.VIEW_AREA_DIST WHERE ID_AREA_DIST = ?  AND (";		ps.adicionarLong(ID_AREA_DIST); 
		stSql += "    SERV_SUBTIPO_CODIGO <>  ? AND ";										ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL); 
		stSql += "    SERV_SUBTIPO_CODIGO <>  ? AND ";										ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL_CRIMINAL); 
		stSql += "    SERV_SUBTIPO_CODIGO <>  ? AND ";										ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CRIMINAL);
		stSql += "    SERV_SUBTIPO_CODIGO <>  ? AND ";										ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_VIOLENCIA_DOMESTICA);
		stSql += "    SERV_SUBTIPO_CODIGO <>  ? AND ";										ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CRIMINAL_FAZENDA_PUBLICA); 
		stSql += "    SERV_SUBTIPO_CODIGO <>  ? AND ";										ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_FAZENDA_PUBLICA);
		stSql += "    SERV_SUBTIPO_CODIGO <>  ? AND ";										ps.adicionarLong(ServentiaSubtipoDt.UPJ_JUIZADO_ESPECIAL_FAZENDA_PUBLICA);
		stSql += "    SERV_SUBTIPO_CODIGO <>  ? AND ";										ps.adicionarLong(ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL); 
		stSql += "    SERV_SUBTIPO_CODIGO <>  ? AND ";										ps.adicionarLong(ServentiaSubtipoDt.TURMA_RECURSAL_CRIMINAL); 
		stSql += "    SERV_SUBTIPO_CODIGO <>  ? AND ";										ps.adicionarLong(ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL_CRIMINAL); 
		stSql += "    SERV_SUBTIPO_CODIGO <>  ? AND ";										ps.adicionarLong(ServentiaSubtipoDt.UPJ_TURMA_RECURSAL);
		stSql += "    SERV_SUBTIPO_CODIGO <>  ? AND ";										ps.adicionarLong(ServentiaSubtipoDt.PREPROCESSUAL); 
		stSql += "    SERV_SUBTIPO_CODIGO <>  ? AND ";										ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CRIMINAL);
		stSql += "    SERV_SUBTIPO_CODIGO <>  ? AND ";										ps.adicionarLong(ServentiaSubtipoDt.SECAO_CRIMINAL); 
		stSql += "    SERV_SUBTIPO_CODIGO <>  ? AND ";										ps.adicionarLong(ServentiaSubtipoDt.VARAS_EXECUCAO_PENAL);
		stSql += "    SERV_SUBTIPO_CODIGO <>  ? AND ";										ps.adicionarLong(ServentiaSubtipoDt.VARAS_EXECUCAO_PENAL_ALTERNATIVA);
		stSql += "    SERV_SUBTIPO_CODIGO <>  ? AND ";										ps.adicionarLong(ServentiaSubtipoDt.VARA_CRIMINAL);
		stSql += "    SERV_SUBTIPO_CODIGO <>  ? AND ";										ps.adicionarLong(ServentiaSubtipoDt.UPJ_CRIMINAL);
		stSql += "    SERV_SUBTIPO_CODIGO <>  ? AND ";										ps.adicionarLong(ServentiaSubtipoDt.UPJ_VIOLENCIA_DOMESTICA);
		stSql += "    SERV_SUBTIPO_CODIGO <>  ? AND ";										ps.adicionarLong(ServentiaSubtipoDt.UPJ_CUSTODIA);
		stSql += "    SERV_SUBTIPO_CODIGO <>  ? AND ";										ps.adicionarLong(ServentiaSubtipoDt.INFANCIA_JUVENTUDE_CIVEL);
		stSql += "    SERV_SUBTIPO_CODIGO <>  ? AND ";										ps.adicionarLong(ServentiaSubtipoDt.INFANCIA_JUVENTUDE_INFRACIONAL);
		stSql += "    SERV_SUBTIPO_CODIGO <>  ? AND ";										ps.adicionarLong(ServentiaSubtipoDt.AUDITORIA_MILITAR_CIVEL);
		stSql += "    SERV_SUBTIPO_CODIGO <>  ?  )";										ps.adicionarLong(ServentiaSubtipoDt.AUDITORIA_MILITAR_CRIMINAL);

		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				retorno = true;
			}
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return retorno; 
	}
	
	public String consultarAreasDistribuicaoCivelJSON(String nome, String id_comarca, String id_Serventia_Usuario, boolean comCusta, String posicao) throws Exception {
		String Sql;
		String SqlComun = "";
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 3;
		
		SqlComun = " FROM PROJUDI.VIEW_AREA_DIST ";
		SqlComun += " WHERE SERV_SUBTIPO_CODIGO in (  ? ,";	ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL);		
		SqlComun += "  ? , ";									ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL_CRIMINAL);
		SqlComun += "  ? , ";									ps.adicionarLong(ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL);
		SqlComun += "  ? , ";									ps.adicionarLong(ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL_EXECUACAO_FISCAL);
		SqlComun += "  ? , ";									ps.adicionarLong(ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL_INTERIOR);
		SqlComun += "  ? , ";									ps.adicionarLong(ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL);
		SqlComun += "  ? , ";									ps.adicionarLong(ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL_EXECUACAO_FISCAL);
		SqlComun += "  ? , ";									ps.adicionarLong(ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL_INTERIOR);
		SqlComun += "  ? , ";									ps.adicionarLong(ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL_ESTADUAL);
		SqlComun += "  ? , ";									ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_FAZENDA_PUBLICA);
		SqlComun += "  ? , ";									ps.adicionarLong(ServentiaSubtipoDt.UPJ_JUIZADO_ESPECIAL_FAZENDA_PUBLICA);
		SqlComun += "  ? , ";									ps.adicionarLong(ServentiaSubtipoDt.FAMILIA_CAPITAL);
		SqlComun += "  ? , ";									ps.adicionarLong(ServentiaSubtipoDt.UPJ_FAMILIA);
		SqlComun += "  ? , ";									ps.adicionarLong(ServentiaSubtipoDt.FAMILIA_INTERIOR);
		SqlComun += "  ? , ";									ps.adicionarLong(ServentiaSubtipoDt.UPJ_SUCESSOES);
		SqlComun += "  ? , ";									ps.adicionarLong(ServentiaSubtipoDt.VARAS_CIVEL);
		SqlComun += "  ? , ";									ps.adicionarLong(ServentiaSubtipoDt.AUDITORIA_MILITAR_CIVEL);		
		SqlComun += "  ? , ";									ps.adicionarLong(ServentiaSubtipoDt.PLANTAO_PRIMEIRO_GRAU);
		SqlComun += "  ? , ";									ps.adicionarLong(ServentiaSubtipoDt.PLANTAO_AUDIENCIA_CUSTODIA);
		SqlComun += "  ? , ";									ps.adicionarLong(ServentiaSubtipoDt.PRECATORIA);
		SqlComun += "  ? , ";									ps.adicionarLong(ServentiaSubtipoDt.INFANCIA_JUVENTUDE_CIVEL);		
		SqlComun += "  ? ) ";									ps.adicionarLong(ServentiaSubtipoDt.PREPROCESSUAL);
		SqlComun += " AND CODIGO_TEMP = ?";					ps.adicionarLong(AreaDistribuicaoDt.ATIVO);
		
		//Alteracao devido as cartorios nao oficializados
		if(comCusta){
			SqlComun += " AND  AREA_DIST_CODIGO NOT IN (?,?,?,?) ";
			ps.adicionarLong(AreaDistribuicaoDt.GOIANIA_VARAS_CIVEIS_COM_ASSISTENCIA);
			ps.adicionarLong(AreaDistribuicaoDt.GOIANIA_FAMILIA_COM_ASSISTENCIA);
			ps.adicionarLong(AreaDistribuicaoDt.GOIANIA_ARBITRAGEM_COM_ASSISTENCIA);
			ps.adicionarLong(AreaDistribuicaoDt.GOIANIA_AMBIENTAL_COM_ASSISTENCIA);
		} else {
			SqlComun += " AND  AREA_DIST_CODIGO NOT IN (?,?,?,?) ";
			ps.adicionarLong(AreaDistribuicaoDt.GOIANIA_VARAS_CIVEIS_COM_CUSTAS);
			ps.adicionarLong(AreaDistribuicaoDt.GOIANIA_FAMILIA_COM_CUSTAS);
			ps.adicionarLong(AreaDistribuicaoDt.GOIANIA_ARBITRAGEM_COM_CUSTAS);
			ps.adicionarLong(AreaDistribuicaoDt.GOIANIA_AMBIENTAL_COM_CUSTAS);
		}
		
		if (nome != null && nome.trim().length() > 0)	{
			SqlComun += " AND AREA_DIST LIKE ? ";
			ps.adicionarString("%" + nome.trim() + "%");
		}		
		if (id_comarca != null && id_comarca.trim().length() > 0 && Funcoes.StringToLong(id_comarca.trim()) > 0) {
			SqlComun += " AND ID_COMARCA = ? ";
			ps.adicionarLong(id_comarca.trim());			 
		}
		//Se for informado o id_Serventia_Usuario, serão apresentadas
		//as áreas de distribuição ligadas à serventia do usuário.
		if(id_Serventia_Usuario != null && id_Serventia_Usuario.trim().length() > 0) {
			SqlComun += "  OR ID_AREA_DIST IN (SELECT ID_AREA_DIST FROM SERV_AREA_DIST WHERE ID_SERV = ?) ";
			ps.adicionarLong(id_Serventia_Usuario.trim());
		}

		Sql = "SELECT ID_AREA_DIST AS ID, AREA_DIST AS DESCRICAO1, FORUM_CODIGO AS DESCRICAO2, ID_SERV_SUBTIPO AS DESCRICAO3 ";
		Sql += SqlComun + " ORDER BY AREA_DIST";		
		try{

			rs1 = consultarPaginacao(Sql, ps, posicao);
			Sql = "SELECT COUNT(*) as QUANTIDADE " + SqlComun;
			rs2 = consultar(Sql, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {
			}
		}
		return stTemp;
	}
	
}
