package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt;
import br.gov.go.tj.projudi.dt.AudienciaTipoDt;
import br.gov.go.tj.projudi.dt.DescritorComboDt;
import br.gov.go.tj.projudi.dt.EnderecoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.PendenciaStatusDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoFaseDt;
import br.gov.go.tj.projudi.dt.ServentiaAreaDistribuicaoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.relatorios.ServentiaProcessosDt;
import br.gov.go.tj.utils.Configuracao;
import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ProjudiConstantes;
import br.gov.go.tj.utils.ResultSetTJGO;

// ---------------------------------------------------------
public class ServentiaPs extends ServentiaPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = 3923405197348796312L;

    public ServentiaPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Sobrescrevendo método para setar atributo EnderecoDt
	 */
	public ServentiaDt consultarId(String id_serventia) throws Exception {

		String Sql;
		ServentiaDt Dados = new ServentiaDt();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "SELECT * FROM PROJUDI.VIEW_SERV WHERE ID_SERV = ?";
		ps.adicionarLong(id_serventia);

		try{
			rs1 = consultar(Sql,ps);
			if (rs1.next()) {
				associarServentiaDt(Dados, rs1);
			}
			// rs.close();
			// //System.out.println("..ps-ConsultaId");
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return Dados;
	}
	
	private void associarServentiaDt(ServentiaDt Dados, ResultSetTJGO rs1 )  throws Exception {
		Dados.setId(rs1.getString("ID_SERV"));
		Dados.setServentia(rs1.getString("SERV"));
		Dados.setServentiaCodigo(rs1.getString("SERV_CODIGO"));
		Dados.setServentiaCodigoExterno(rs1.getString("SERV_CODIGO_EXTERNO"));
		Dados.setId_ServentiaTipo(rs1.getString("ID_SERV_TIPO"));
		Dados.setServentiaTipo(rs1.getString("SERV_TIPO"));
		Dados.setId_ServentiaSubtipo(rs1.getString("ID_SERV_SUBTIPO"));
		Dados.setServentiaSubtipo(rs1.getString("SERV_SUBTIPO"));
		Dados.setId_Area(rs1.getString("ID_AREA"));
		Dados.setArea(rs1.getString("AREA"));
		Dados.setId_Comarca(rs1.getString("ID_COMARCA"));
		Dados.setComarca(rs1.getString("COMARCA"));
		Dados.setId_AreaDistribuicao(rs1.getString("ID_AREA_DIST"));
		Dados.setAreaDistribuicao(rs1.getString("AREA_DIST"));
		
		Dados.setId_AreaDistribuicaoSecundaria(rs1.getString("ID_AREA_DIST_2"));
		Dados.setAreaDistribuicaoSecundaria(rs1.getString("AREA_DIST_2"));
		
		Dados.setTelefone(rs1.getString("TELEFONE"));
		Dados.setEmail(rs1.getString("EMAIL"));
		Dados.setId_AudienciaTipo(rs1.getString("ID_AUDI_TIPO"));
		Dados.setAudienciaTipo(rs1.getString("AUDI_TIPO"));
		Dados.setId_EstadoRepresentacao(rs1.getString("ID_ESTADO_REPRESENTACAO"));
		Dados.setEstadoRepresentacao(rs1.getString("ESTADO_REPRESENTACAO"));
		Dados.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
		Dados.setServentiaTipoCodigo(rs1.getString("SERV_TIPO_CODIGO"));
		Dados.setServentiaSubtipoCodigo(rs1.getString("SERV_SUBTIPO_CODIGO"));
		Dados.setAreaCodigo(rs1.getString("AREA_CODIGO"));
		Dados.setComarcaCodigo(rs1.getString("COMARCA_CODIGO"));
		Dados.setDataCadastro(Funcoes.FormatarData(rs1.getDateTime("DATA_CADASTRO")));
		Dados.setDataImplantacao(Funcoes.FormatarData(rs1.getDateTime("DATA_IMPLANTACAO")));		
		Dados.setId_CNJServentia(rs1.getString("ID_CNJ_SERV"));
		Dados.setConclusoDireto(rs1.getString("CONCLUSO_DIRETO"));
		Dados.setQuantidadeDistribuicao(rs1.getString("QUANTIDADE_DIST"));
		
		/**
		 * Setando endereço da serventia
		 */
		
		EnderecoDt enderecoDt = new EnderecoDt();
		enderecoDt.setId(rs1.getString("ID_ENDERECO"));
		enderecoDt.setLogradouro(rs1.getString("LOGRADOURO"));
		enderecoDt.setNumero(rs1.getString("NUMERO"));
		enderecoDt.setComplemento(rs1.getString("COMPLEMENTO"));
		enderecoDt.setCep(rs1.getString("CEP"));
		enderecoDt.setId_Bairro(rs1.getString("ID_BAIRRO"));
		enderecoDt.setBairro(rs1.getString("BAIRRO"));
		enderecoDt.setId_Cidade(rs1.getString("ID_CIDADE"));
		enderecoDt.setCidade(rs1.getString("CIDADE"));
		enderecoDt.setUf(rs1.getString("ESTADO"));
		Dados.setEnderecoDt(enderecoDt);
	}

	/**
	 * Consulta dados básicos de uma serventia pelo Id passado
	 * 
	 * @param id_serventia
	 *            , identificação da serventia
	 * 
	 * @author msapaula
	 */
	public ServentiaDt consultarIdSimples(String id_serventia) throws Exception {
		String Sql;
		ServentiaDt Dados = new ServentiaDt();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT ID_SERV,  SERV,  SERV_TIPO_CODIGO, SERV_CODIGO_EXTERNO FROM PROJUDI.VIEW_SERV WHERE ID_SERV = ?";
		ps.adicionarLong(id_serventia);

		try{
			rs1 = consultar(Sql,ps);
			if (rs1.next()) {
				Dados.setId(rs1.getString("ID_SERV"));
				Dados.setServentia(rs1.getString("SERV"));
				Dados.setServentiaTipoCodigo(rs1.getString("SERV_TIPO_CODIGO"));
				Dados.setServentiaCodigoExterno(rs1.getString("SERV_CODIGO_EXTERNO"));
			}
		
		} finally{
			try{
				  if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return Dados;
	}
	
	/**
	 * Consulta Serventia SubTipo Codigo de uma serventia pelo Id passado
	 * 
	 * @param id_serventia
	 *            , identificação da serventia
	 * 
	 * @author lsbernardes
	 */
	public String consultarServentiaSubTipoCodigo(String id_serventia) throws Exception {
		String Sql;
		String serventiaSubTipoCodigo = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT SERV_SUBTIPO_CODIGO FROM PROJUDI.VIEW_SERV WHERE ID_SERV = ?";
		ps.adicionarLong(id_serventia);

		try{
			rs1 = consultar(Sql,ps);
			if (rs1.next()) {
				serventiaSubTipoCodigo = rs1.getString("SERV_SUBTIPO_CODIGO");
			}
		
		} finally{
			try{
				  if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return serventiaSubTipoCodigo;
	}

	/**
	 * Consultar a descrição de uma serventia de acordo com Id passado
	 * 
	 * @param id_Serventia
	 *            , identificação da serventia
	 * 
	 * @author msapaula
	 */
	public String consultarServentiaDescricao(String id_Serventia) throws Exception {
		String Sql;
		String descricaoServentia = null;
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "SELECT SERV FROM PROJUDI.VIEW_SERV WHERE ID_SERV = ?";
		ps.adicionarLong(id_Serventia);

		try{
			rs = consultar(Sql,ps);
			if (rs.next()) {
				descricaoServentia = rs.getString("SERV");
			}
			// rs.close();
		
		} finally{
			try{
				if (rs != null) rs.close();
			} catch(Exception e) {
			}
		}
		return descricaoServentia;
	}

	/**
	 * Consultar determinada serventia de acordo com Código (ServentiaCodigo)
	 * 
	 * @author msapaula
	 */
	public ServentiaDt consultarServentiaCodigo(String serventiaCodigo) throws Exception {
		String Sql;
		ServentiaDt Dados = null;
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "SELECT * FROM PROJUDI.VIEW_SERV WHERE SERV_CODIGO = ?";
		ps.adicionarLong(serventiaCodigo);

		try{
			rs = consultar(Sql,ps);
			if (rs.next()) {
				Dados = new ServentiaDt();
				Dados.setId(rs.getString("ID_SERV"));
				Dados.setServentia(rs.getString("SERV"));
				Dados.setServentiaCodigo(rs.getString("SERV_CODIGO"));
				Dados.setId_ServentiaTipo(rs.getString("ID_SERV_TIPO"));
				Dados.setServentiaTipo(rs.getString("SERV_TIPO"));
				Dados.setId_Area(rs.getString("ID_AREA"));
				Dados.setArea(rs.getString("AREA"));
				Dados.setId_Endereco(rs.getString("ID_ENDERECO"));
				Dados.setCodigoTemp(rs.getString("CODIGO_TEMP"));
				Dados.setComarca(rs.getString("COMARCA"));
				Dados.setId_Comarca(rs.getString("ID_COMARCA"));
				Dados.setLogradouro(rs.getString("LOGRADOURO"));
				Dados.setNumero(rs.getString("NUMERO"));
				Dados.setComplemento(rs.getString("COMPLEMENTO"));
				Dados.setCep(rs.getString("CEP"));
				Dados.setBairro(rs.getString("BAIRRO"));
				Dados.setCidade(rs.getString("CIDADE"));
				Dados.setEstado(rs.getString("ESTADO"));
				Dados.setId_EstadoRepresentacao(rs.getString("ID_ESTADO_REPRESENTACAO"));
				Dados.setEstadoRepresentacao(rs.getString("ESTADO_REPRESENTACAO"));
				Dados.setDataCadastro(Funcoes.FormatarData(rs.getDateTime("DATA_CADASTRO")));
				Dados.setDataImplantacao(Funcoes.FormatarData(rs.getDateTime("DATA_IMPLANTACAO")));				
				Dados.setComarcaCodigo(rs.getString("COMARCA_CODIGO"));
				Dados.setConclusoDireto(rs.getString("CONCLUSO_DIRETO"));
				Dados.setQuantidadeDistribuicao(rs.getString("QUANTIDADE_DIST"));
			}
		
		} finally{
			try{
				if (rs != null) rs.close();
			} catch(Exception e) {
			}
		}
		return Dados;
	}

	/**
	 * Sobrescrevendo método para retornar mais campos
	 */
	public List consultarDescricao(String descricao, String posicao) throws Exception {
		
		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT ID_SERV,SERV, ESTADO_REPRESENTACAO";
		SqlFrom = " FROM PROJUDI.VIEW_SERV";
		SqlFrom += " WHERE SERV LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		SqlOrder = " ORDER BY SERV";
		
		try{
			rs = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);

			while (rs.next()) {
				ServentiaDt obTemp = new ServentiaDt();
				obTemp.setId(rs.getString("ID_SERV"));
				obTemp.setServentia(rs.getString("SERV"));
				obTemp.setEstadoRepresentacao(rs.getString("ESTADO_REPRESENTACAO"));
				liTemp.add(obTemp);
			}
			// rs.close();
			Sql = "SELECT COUNT(*) AS QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			if (rs2.next()) 
				liTemp.add(rs2.getLong("QUANTIDADE"));
			
			// rs2.close();
		
		} finally{
			try{
				if (rs != null) rs.close();
			} catch(Exception e) {
			}
			try{
				  if (rs2 != null) rs2.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}
	
	public List consultarDescricaoExata(String descricao, String posicao) throws Exception {
		
		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT ID_SERV,SERV, ESTADO_REPRESENTACAO";
		SqlFrom = " FROM PROJUDI.VIEW_SERV";
		SqlFrom += " WHERE SERV = ?";
		ps.adicionarString(descricao);
		SqlOrder = " ORDER BY SERV";
		
		try{
			rs = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);

			while (rs.next()) {
				ServentiaDt obTemp = new ServentiaDt();
				obTemp.setId(rs.getString("ID_SERV"));
				obTemp.setServentia(rs.getString("SERV"));
				obTemp.setEstadoRepresentacao(rs.getString("ESTADO_REPRESENTACAO"));
				liTemp.add(obTemp);
			}
			// rs.close();
			Sql = "SELECT COUNT(*) AS QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			if (rs2.next()) 
				liTemp.add(rs2.getLong("QUANTIDADE"));
			
			// rs2.close();
		
		} finally{
			try{
				if (rs != null) rs.close();
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
	 * Consultar serventias ativas, podendo filtrar pelo Tipo
	 * (ServentiaTipoCodigo) ou pela Comarca, com uso de paginação
	 * 
	 * @param descricao
	 *            filtro da pesquisa
	 * @param posicao
	 *            parametro para paginação
	 * @param serventiaTipoCodigo
	 *            tipo da serventia a ser filtrada
	 * @param id_Comarca
	 *            identificação da comarca para filtrar as serventias
	 * 
	 * @author msapaula
	 */
	public List consultarServentiasAtivas(String descricao, String posicao, String serventiaTipoCodigo, String id_Comarca, String serventiaSubTipoCodigo) throws Exception {
		String Sql;
		String sqlComum;
		List liTemp = new ArrayList();
		ResultSetTJGO rs = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT ID_SERV,SERV,ESTADO_REPRESENTACAO,ID_SERV_SUBTIPO,SERV_SUBTIPO ";
		sqlComum = " FROM PROJUDI.VIEW_SERV WHERE SERV LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		sqlComum += " AND CODIGO_TEMP = ?";
		ps.adicionarLong(ServentiaDt.ATIVO);

		if (serventiaTipoCodigo != null && serventiaTipoCodigo.length() > 0) {
			sqlComum += " AND SERV_TIPO_CODIGO = ?";
			ps.adicionarLong(serventiaTipoCodigo);
		}
		if (id_Comarca != null && id_Comarca.length() > 0) {
			sqlComum += " AND ID_COMARCA = ?"; 
			ps.adicionarLong(id_Comarca);			
		}
		if (serventiaSubTipoCodigo != null && serventiaSubTipoCodigo.length() > 0) {
			sqlComum += " AND SERV_SUBTIPO_CODIGO = ?";
			ps.adicionarLong(serventiaSubTipoCodigo);
		}

		Sql += sqlComum;
		Sql += " ORDER BY SERV";
		
		try{
			rs = consultarPaginacao(Sql, ps, posicao);

			while (rs.next()) {
				ServentiaDt obTemp = new ServentiaDt();
				obTemp.setId(rs.getString("ID_SERV"));
				obTemp.setServentia(rs.getString("SERV"));
				obTemp.setEstadoRepresentacao(rs.getString("ESTADO_REPRESENTACAO"));
				obTemp.setId_ServentiaSubtipo(rs.getString("ID_SERV_SUBTIPO"));
				obTemp.setServentiaSubtipo(rs.getString("SERV_SUBTIPO"));
				liTemp.add(obTemp);
			}
			// rs.close();
			Sql = "SELECT COUNT(*) AS QUANTIDADE " + sqlComum;
			rs2 = consultar(Sql,ps);
			if (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			// rs.close();
		
		} finally{
			try{
				if (rs != null) rs.close();
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
	 * Consultar serventias ativas, podendo filtrar pelo Tipo
	 * (ServentiaTipoCodigo) com uso de paginação
	 * 
	 * @param descricao
	 *            filtro da pesquisa
	 * @param posicao
	 *            parametro para paginação
	 * @param serventiaTipoCodigo
	 *            tipo da serventia a ser filtrada
	 * 
	 * @author asrocha 20/11/2014
	 * 
	 */
	public String consultarServentiasJuridicasJSON(String descricao, String posicao, int[] serventiaTipoCodigo) throws Exception {
		String Sql;
		String sqlComum;
		String sqlComum2="";
		String stTemp = "";
		ResultSetTJGO rs = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 3;

		Sql = "SELECT ID_SERV AS ID, SERV AS DESCRICAO1 ,SERV_TIPO AS DESCRICAO2 ,ESTADO_REPRESENTACAO AS DESCRICAO3 ";
		sqlComum = " FROM PROJUDI.VIEW_SERV WHERE SERV LIKE ?";		ps.adicionarString("%"+ descricao +"%");
		sqlComum += " AND CODIGO_TEMP = ?";							ps.adicionarLong(ServentiaDt.ATIVO);

		for (int i = 0; i < serventiaTipoCodigo.length; i++) {
			if (serventiaTipoCodigo != null) {
				if (sqlComum2.length()==0){
					sqlComum2+= " AND ( SERV_TIPO_CODIGO = ?";			ps.adicionarLong(serventiaTipoCodigo[i]);
				}else{
					sqlComum2+= " OR SERV_TIPO_CODIGO = ?";			ps.adicionarLong(serventiaTipoCodigo[i]);
				}
			}
		}
		
		if (sqlComum2.length() > 0){
			sqlComum2+= ")";
		}

		Sql += sqlComum + sqlComum2 ;
		Sql += " ORDER BY SERV";
		
		try{
			rs = consultarPaginacao(Sql, ps, posicao);

			Sql = "SELECT COUNT(*) AS QUANTIDADE " + sqlComum + sqlComum2;
			rs2 = consultar(Sql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs, qtdeColunas);
			
		
		} finally{
			try{if (rs != null) rs.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
	/**
	 * Consultar serventias ativas, podendo filtrar pelo Tipo
	 * (ServentiaTipoCodigo) ou pela Comarca, com uso de paginação
	 * 
	 * @param descricao
	 *            filtro da pesquisa
	 * @param posicao
	 *            parametro para paginação
	 * @param serventiaTipoCodigo
	 *            tipo da serventia a ser filtrada
	 * @param id_Comarca
	 *            identificação da comarca para filtrar as serventias
	 * 
	 * @author lsbernardes
	 */
	public List consultarServentiasAtivasRedistribuicaoLote(String descricao, String posicao, String serventiaTipoCodigo, String id_Comarca) throws Exception {
		String Sql;
		String sqlComum;
		List liTemp = new ArrayList();
		ResultSetTJGO rs = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT ID_SERV,SERV,ESTADO_REPRESENTACAO,ID_SERV_SUBTIPO,SERV_SUBTIPO ";
		sqlComum = " FROM PROJUDI.VIEW_SERV WHERE SERV LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		sqlComum += " AND CODIGO_TEMP = ?";
		ps.adicionarLong(ServentiaDt.ATIVO);

		if (serventiaTipoCodigo != null && serventiaTipoCodigo.length() > 0) {
			sqlComum += " AND SERV_TIPO_CODIGO = ?";
			ps.adicionarLong(serventiaTipoCodigo);
		}
		if (id_Comarca != null && id_Comarca.length() > 0) {
			sqlComum += " AND ID_COMARCA = ?"; 
			ps.adicionarLong(id_Comarca);			
		}

		Sql += sqlComum;
		Sql += " ORDER BY SERV";
		
		try{
			rs = consultarPaginacao(Sql, ps, posicao);

			while (rs.next()) {
				ServentiaDt obTemp = new ServentiaDt();
				obTemp.setId(rs.getString("ID_SERV"));
				obTemp.setServentia(rs.getString("SERV"));
				obTemp.setEstadoRepresentacao(rs.getString("ESTADO_REPRESENTACAO"));
				obTemp.setId_ServentiaSubtipo(rs.getString("ID_SERV_SUBTIPO"));
				obTemp.setServentiaSubtipo(rs.getString("SERV_SUBTIPO"));
				liTemp.add(obTemp);
			}
			// rs.close();
			Sql = "SELECT COUNT(*) AS QUANTIDADE " + sqlComum;
			rs2 = consultar(Sql,ps);
			if (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			// rs.close();
		
		} finally{
			try{
				if (rs != null) rs.close();
			} catch(Exception e) {
			}
			try{
				  if (rs2 != null) rs2.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}
	
	public String consultarServentiasAtivasRedistribuicaoLoteJSON(String descricao, String posicao, List listaServentiaTipoCodigo, String id_Comarca) throws Exception {
		String Sql;
		String sqlComum;
		String stTemp="";
		ResultSetTJGO rs = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;

		Sql = "SELECT ID_SERV as id,SERV as descricao1,ESTADO_REPRESENTACAO as descricao2,ID_SERV_SUBTIPO as descricao3,SERV_SUBTIPO as descricao4 ";
		sqlComum = " FROM PROJUDI.VIEW_SERV WHERE SERV LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		sqlComum += " AND CODIGO_TEMP = ?";
		ps.adicionarLong(ServentiaDt.ATIVO);

		for (int i = 0; i < listaServentiaTipoCodigo.size(); i++) {
			if(i == 0) {
				sqlComum += " AND SERV_SUBTIPO_CODIGO IN (?";
			} else {
				sqlComum += ",?";
			}
			ps.adicionarLong(Long.valueOf(new Long(listaServentiaTipoCodigo.get(i).toString())));
			if(listaServentiaTipoCodigo.size()-1 == i) {
				sqlComum += ")";
			}
		}

		if (id_Comarca != null && id_Comarca.length() > 0) {
			sqlComum += " AND ID_COMARCA = ?"; 
			ps.adicionarLong(id_Comarca);			
		}

		Sql += sqlComum;
		Sql += " ORDER BY SERV";
		
		try{
			rs = consultarPaginacao(Sql, ps, posicao);

			Sql = "SELECT COUNT(*) AS QUANTIDADE " + sqlComum;
			rs2 = consultar(Sql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs, qtdeColunas);
		
		} finally{
			try{
				if (rs != null) rs.close();
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
	 * Consultar serventias ativas, podendo filtrar pelo Tipo
	 * (ServentiaTipoCodigo) ou pela Comarca, com uso de paginação
	 * 
	 * @param descricao
	 *            filtro da pesquisa
	 * @param posicao
	 *            parametro para paginação
	 * @param serventiaTipoCodigo
	 *            tipo da serventia a ser filtrada
	 * @param id_Comarca
	 *            identificação da comarca para filtrar as serventias
	 * 
	 * @author lsbernardes
	 */
	public List consultarServentiasAtivas(String descricao, String posicao, String serventiaTipoCodigo1Grau , String serventiaTipoCodigo2Grau, String id_Comarca, String serventiaSubTipoCodigo) throws Exception {
		String Sql;
		String sqlComum;
		List liTemp = new ArrayList();
		ResultSetTJGO rs = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
//
		Sql = "SELECT ID_SERV,SERV,ESTADO_REPRESENTACAO,ID_SERV_SUBTIPO,SERV_SUBTIPO ";
		sqlComum = " FROM PROJUDI.VIEW_SERV WHERE SERV LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		sqlComum += " AND CODIGO_TEMP = ?";
		ps.adicionarLong(ServentiaDt.ATIVO);
		
		if (serventiaTipoCodigo1Grau != null && serventiaTipoCodigo1Grau.length() > 0
				&& serventiaTipoCodigo2Grau != null && serventiaTipoCodigo2Grau.length() > 0) {
			sqlComum += " AND (SERV_TIPO_CODIGO = ? OR SERV_TIPO_CODIGO = ? )";
			ps.adicionarLong(serventiaTipoCodigo1Grau); ps.adicionarLong(serventiaTipoCodigo2Grau);
		
		} else if (serventiaTipoCodigo1Grau != null && serventiaTipoCodigo1Grau.length() > 0) {
			sqlComum += " AND SERV_TIPO_CODIGO = ?";
			ps.adicionarLong(serventiaTipoCodigo1Grau);
		
		} else if (serventiaTipoCodigo2Grau != null && serventiaTipoCodigo2Grau.length() > 0) {
			sqlComum += " AND SERV_TIPO_CODIGO = ?";
			ps.adicionarLong(serventiaTipoCodigo2Grau);
		}
		
		if (id_Comarca != null && id_Comarca.length() > 0) {
			sqlComum += " AND ID_COMARCA = ?"; 
			ps.adicionarLong(id_Comarca);			
		}
		if (serventiaSubTipoCodigo != null && serventiaSubTipoCodigo.length() > 0) {
			sqlComum += " AND SERV_SUBTIPO_CODIGO = ?";
			ps.adicionarLong(serventiaSubTipoCodigo);
		}

		Sql += sqlComum;
		Sql += " ORDER BY SERV";
		
		try{
			rs = consultarPaginacao(Sql, ps, posicao);

			while (rs.next()) {
				ServentiaDt obTemp = new ServentiaDt();
				obTemp.setId(rs.getString("ID_SERV"));
				obTemp.setServentia(rs.getString("SERV"));
				obTemp.setEstadoRepresentacao(rs.getString("ESTADO_REPRESENTACAO"));
				obTemp.setId_ServentiaSubtipo(rs.getString("ID_SERV_SUBTIPO"));
				obTemp.setServentiaSubtipo(rs.getString("SERV_SUBTIPO"));
				liTemp.add(obTemp);
			}
			// rs.close();
			Sql = "SELECT COUNT(*) AS QUANTIDADE " + sqlComum;
			rs2 = consultar(Sql,ps);
			if (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			// rs.close();
		
		} finally{
			try{
				if (rs != null) rs.close();
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
	 * Consultar todas as serventias ativas de um determinado Tipo
	 * (ServentiaTipoCodigo), sem uso de paginação
	 * 
	 * @param serventiaTipoCodigo
	 *            tipo da serventia a ser filtrada
	 * 
	 * @author msapaula
	 */
	public List consultarServentiasAtivas(String serventiaTipoCodigo) throws Exception {
		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
				
		Sql = "SELECT ID_SERV,SERV,ESTADO_REPRESENTACAO FROM PROJUDI.VIEW_SERV ";
		Sql += " WHERE CODIGO_TEMP = ?";
		ps.adicionarLong(ServentiaDt.ATIVO);
		if (serventiaTipoCodigo != null && serventiaTipoCodigo.length() > 0){
			Sql += " AND SERV_TIPO_CODIGO = ?";
			ps.adicionarLong(serventiaTipoCodigo);
		}
		Sql += " ORDER BY SERV";
		try{
			rs = consultar(Sql,ps);

			while (rs.next()) {
				ServentiaDt obTemp = new ServentiaDt();
				obTemp.setId(rs.getString("ID_SERV"));
				obTemp.setServentia(rs.getString("SERV"));
				obTemp.setEstadoRepresentacao(rs.getString("ESTADO_REPRESENTACAO"));
				liTemp.add(obTemp);
			}
			// rs.close();
		
		} finally{
			try{
				if (rs != null) rs.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}

	/**
	 * Consultar serventias que sejam do tipo OAB, PROCURADORIA_MUNICIPAL,
	 * PROCURADORIA_ESTADUAL, PROCURADORIA_ASSISTENCIA_JUDICIARIA ou
	 * PROCURADORIA_UNIAO
	 * 
	 * @author leandro
	 */
	public List consultarServentiasHabilitacaoAdvogado(String descricao, String posicao) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		
		Sql = "SELECT ID_SERV,SERV,ESTADO_REPRESENTACAO";
		SqlFrom = " FROM PROJUDI.VIEW_SERV";
		SqlFrom += " WHERE SERV LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		SqlFrom += " AND ( SERV_TIPO_CODIGO = ?";
		ps.adicionarLong(ServentiaTipoDt.ORDEM_ADVOGADOS_BRASIL);
		SqlFrom += " OR SERV_TIPO_CODIGO = ?";
		ps.adicionarLong(ServentiaTipoDt.PROCURADORIA_GERAL_MUNICIPAL);
		SqlFrom += " OR SERV_TIPO_CODIGO = ?";
		ps.adicionarLong(ServentiaTipoDt.PROCURADORIA_GERAL_ESTADO);
		SqlFrom += " OR SERV_TIPO_CODIGO = ?";
		ps.adicionarLong(ServentiaTipoDt.PROCURADORIA_UNIAO);
		SqlFrom += " OR SERV_TIPO_CODIGO = ?";
		ps.adicionarLong(ServentiaTipoDt.ADVOGADO_PUBLICO_AUTARQUIAS);
		SqlFrom += " OR SERV_TIPO_CODIGO = ?)"; 
		ps.adicionarLong(ServentiaTipoDt.DEFENSORIA_PUBLICA);
		SqlOrder = " ORDER BY SERV";
		
		try{
			rs = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);

			while (rs.next()) {
				ServentiaDt obTemp = new ServentiaDt();
				obTemp.setId(rs.getString("ID_SERV"));
				obTemp.setServentia(rs.getString("SERV"));
				obTemp.setEstadoRepresentacao(rs.getString("ESTADO_REPRESENTACAO"));
				liTemp.add(obTemp);
			}
			// rs.close();
			Sql = "SELECT COUNT(*) AS QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			if (rs2.next()) 
				liTemp.add(rs2.getLong("QUANTIDADE"));
			
			// rs.close();
		
		} finally{
			try{
				if (rs != null) rs.close();
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
	 * Consultar serventias que não sejam do tipo OAB, Sistema Projudi ou
	 * Administração
	 * 
	 * @author leandro
	 */
	public List consultarServentiasHabilitacaoUsuarios(String descricao, String posicao, String serventiaTipo) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "SELECT ID_SERV,SERV,ESTADO_REPRESENTACAO";
		SqlFrom = " FROM PROJUDI.VIEW_SERV";
		SqlFrom += " WHERE SERV LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		
		if (serventiaTipo != null && serventiaTipo.length() > 0) {
			SqlFrom += " AND SERV_TIPO_CODIGO = ?";
			ps.adicionarLong(serventiaTipo);
		}
		SqlFrom += " AND SERV_TIPO_CODIGO <> ?";
		ps.adicionarLong(ServentiaTipoDt.ORDEM_ADVOGADOS_BRASIL);
		SqlFrom += " AND SERV_TIPO_CODIGO <> ?";
		ps.adicionarLong(ServentiaTipoDt.SISTEMA_PROJUDI);
		SqlFrom += " AND SERV_TIPO_CODIGO <> ?";
		ps.adicionarLong(ServentiaTipoDt.ADMINISTRACAO_SISTEMA_PROJUDI);
		SqlOrder = " ORDER BY SERV";

		try{
			rs = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);

			while (rs.next()) {
				ServentiaDt obTemp = new ServentiaDt();
				obTemp.setId(rs.getString("ID_SERV"));
				obTemp.setServentia(rs.getString("SERV"));
				obTemp.setEstadoRepresentacao(rs.getString("ESTADO_REPRESENTACAO"));
				liTemp.add(obTemp);
			}
			// rs.close();
			Sql = "SELECT COUNT(*) AS QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			if (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			// rs.close();
		
		} finally{
			try{
				if (rs != null) rs.close();
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
	 * Consultar serventias filtrando pela Serventia Tipo
	 * 
	 * @author leandro
	 */
	public List consultarDescricaoServentia(String descricao, String serventiaTipo, UsuarioDt usuarioDt, String posicao) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "SELECT ID_SERV,SERV,ESTADO_REPRESENTACAO";
		SqlFrom = " FROM PROJUDI.VIEW_SERV";
		SqlFrom += " WHERE SERV LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
	
		if ((usuarioDt != null && usuarioDt.getGrupoCodigo().length() > 0 ) &&
				(Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.ADMINISTRADORES 
				|| (Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.ANALISTA_TI)
				||  (Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.ANALISTA_COORDENARODIRA_JUDICIARIA)
				||  (Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.ESTATISTICA) )){
			
			SqlFrom +=" AND SERV_TIPO_CODIGO <> ?";
			ps.adicionarLong(ServentiaTipoDt.PARTE);
			SqlFrom += " AND  CODIGO_TEMP = ?";
			ps.adicionarLong(ServentiaDt.ATIVO);
			if (serventiaTipo != null && !serventiaTipo.equals("") && !serventiaTipo.equals("")) {
				SqlFrom += " AND SERV_TIPO_CODIGO = ?"; 
				ps.adicionarLong(serventiaTipo);
			}
		
		} else {
			SqlFrom += " AND SERV_TIPO_CODIGO <> ?";
			ps.adicionarLong(ServentiaTipoDt.ORDEM_ADVOGADOS_BRASIL);
			SqlFrom += " AND SERV_TIPO_CODIGO <> ?";
			ps.adicionarLong(ServentiaTipoDt.SISTEMA_PROJUDI);
			SqlFrom += " AND SERV_TIPO_CODIGO <> ?";
			ps.adicionarLong(ServentiaTipoDt.ADMINISTRACAO_SISTEMA_PROJUDI);
			SqlFrom +=" AND SERV_TIPO_CODIGO <> ?";
			ps.adicionarLong(ServentiaTipoDt.PARTE);
			SqlFrom += " AND  CODIGO_TEMP = ?";
			ps.adicionarLong(ServentiaDt.ATIVO);
			if (serventiaTipo != null && !serventiaTipo.equals("") && !serventiaTipo.equals("")) {
				SqlFrom += " AND SERV_TIPO_CODIGO = ?"; 
				ps.adicionarLong(serventiaTipo);
			}
		}	
		SqlOrder = " ORDER BY SERV";

		
		try{
			rs = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);

			while (rs.next()) {
				ServentiaDt obTemp = new ServentiaDt();
				obTemp.setId(rs.getString("ID_SERV"));
				obTemp.setServentia(rs.getString("SERV"));
				obTemp.setEstadoRepresentacao(rs.getString("ESTADO_REPRESENTACAO"));
				liTemp.add(obTemp);
			}
			// rs.close();
			Sql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			if (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			// rs.close();
		
		} finally{
			try{
				if (rs != null) rs.close();
			} catch(Exception e) {
			}
			try{
				  if (rs2 != null) rs2.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}
	
	public String consultarDescricaoServentiaJSON(String descricao, String serventiaTipo, UsuarioDt usuarioDt, String posicao) throws Exception {
		
		String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp = "";		
		ResultSetTJGO rs = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;
		
		Sql = "SELECT ID_SERV AS ID,SERV AS DESCRICAO1,ESTADO_REPRESENTACAO AS DESCRICAO2";
		SqlFrom = " FROM PROJUDI.VIEW_SERV";
		SqlFrom += " WHERE SERV LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
	
		if ((usuarioDt != null && usuarioDt.getGrupoCodigo().length() > 0 ) &&
				(Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.ADMINISTRADORES 
				|| (Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.ANALISTA_TI)
				||  (Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.ANALISTA_COORDENARODIRA_JUDICIARIA)
				||  (Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.ESTATISTICA) )){
			
			SqlFrom +=" AND SERV_TIPO_CODIGO <> ?";
			ps.adicionarLong(ServentiaTipoDt.PARTE);
			SqlFrom += " AND  CODIGO_TEMP = ?";
			ps.adicionarLong(ServentiaDt.ATIVO);
			if (serventiaTipo != null && !serventiaTipo.equals("") && !serventiaTipo.equals("")) {
				SqlFrom += " AND SERV_TIPO_CODIGO = ?"; 
				ps.adicionarLong(serventiaTipo);
			}
		
		} else {
			SqlFrom += " AND SERV_TIPO_CODIGO <> ?";
			ps.adicionarLong(ServentiaTipoDt.ORDEM_ADVOGADOS_BRASIL);
			SqlFrom += " AND SERV_TIPO_CODIGO <> ?";
			ps.adicionarLong(ServentiaTipoDt.SISTEMA_PROJUDI);
			SqlFrom += " AND SERV_TIPO_CODIGO <> ?";
			ps.adicionarLong(ServentiaTipoDt.ADMINISTRACAO_SISTEMA_PROJUDI);
			SqlFrom +=" AND SERV_TIPO_CODIGO <> ?";
			ps.adicionarLong(ServentiaTipoDt.PARTE);
			SqlFrom += " AND  CODIGO_TEMP = ?";
			ps.adicionarLong(ServentiaDt.ATIVO);
			if (serventiaTipo != null && !serventiaTipo.equals("") && !serventiaTipo.equals("")) {
				SqlFrom += " AND SERV_TIPO_CODIGO = ?"; 
				ps.adicionarLong(serventiaTipo);
			}
		}	
		SqlOrder = " ORDER BY SERV";

		
		try{
			rs = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);

			Sql = "SELECT COUNT(*) as QUANTIDADE";			
			rs2 = consultar(Sql + SqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs, qtdeColunas);
		
		} finally{
			try{
				if (rs != null) rs.close();
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
	 * Consultar serventias filtrando pela Serventia Tipo
	 * 
	 * @author leandro
	 */
	public List consultarDescricaoServentiaAlterarResponsavelConclusao(String descricao, UsuarioDt usuarioDt, String posicao) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "SELECT s.ID_SERV,s.SERV,s.ESTADO_REPRESENTACAO";
		SqlFrom = " FROM PROJUDI.VIEW_SERV s INNER JOIN PROJUDI.SERV_RELACIONADA sr on s.ID_SERV = sr.ID_SERV_REL ";
		SqlFrom += " WHERE s.SERV LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
	
		SqlFrom += " AND  s.CODIGO_TEMP = ?";			ps.adicionarLong(ServentiaDt.ATIVO);
		SqlFrom += " AND s.SERV_TIPO_CODIGO = ?";		ps.adicionarLong(ServentiaTipoDt.GABINETE);
		SqlFrom += " AND sr.ID_SERV_PRINC = ? ";		ps.adicionarLong(usuarioDt.getId_Serventia());
		
		SqlOrder = " ORDER BY s.SERV";
		
		try{
			rs = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);

			while (rs.next()) {
				ServentiaDt obTemp = new ServentiaDt();
				obTemp.setId(rs.getString("ID_SERV"));
				obTemp.setServentia(rs.getString("SERV"));
				obTemp.setEstadoRepresentacao(rs.getString("ESTADO_REPRESENTACAO"));
				liTemp.add(obTemp);
			}
			// rs.close();
			
			Sql = "SELECT COUNT(*) as QUANTIDADE";			
			rs2 = consultar(Sql + SqlFrom, ps);
			if (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			// rs.close();
		
		} finally{
			try{
				if (rs != null) rs.close();
			} catch(Exception e) {
			}
			try{
				  if (rs2 != null) rs2.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}

	public String consultarDescricaoServentiaAlterarResponsavelConclusaoJSON(String descricao, UsuarioDt usuarioDt, String posicao) throws Exception {
		
		String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp = "";
		ResultSetTJGO rs = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;
		
		Sql = "SELECT s.ID_SERV AS ID,s.SERV AS DESCRICAO1,s.ESTADO_REPRESENTACAO AS DESCRICAO2";
		SqlFrom = " FROM PROJUDI.VIEW_SERV s INNER JOIN PROJUDI.SERV_RELACIONADA sr on s.ID_SERV = sr.ID_SERV_REL";
		SqlFrom += " WHERE s.SERV LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
	
		SqlFrom += " AND  s.CODIGO_TEMP = ?";					ps.adicionarLong(ServentiaDt.ATIVO);
		SqlFrom += " AND s.SERV_TIPO_CODIGO = ?";				ps.adicionarLong(ServentiaTipoDt.GABINETE);
		SqlFrom += " AND sr.ID_SERV_PRINC = ? ";				ps.adicionarLong(usuarioDt.getId_Serventia());
		
		SqlOrder = " ORDER BY s.SERV";
		
		try{
			rs = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);

			Sql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs, qtdeColunas);
		
		} finally{
			try{
				if (rs != null) rs.close();
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
	 * Consulta as serventias de uma determinada área de distribuição
	 * 			dada uma area de distribuição, um tipo de processo, pego a serventia que recebeu menos processo deste tipo, se for passado uma id_serventia retiro-a das consulta
	 * 			uso o validar para o caso de distribuição 2/1, 3/1 ou 4/1	
	 * @param id_AreaDistribuicao
	 *            identificação da área de distribuição
	 * 
	 * @author msapaula
	 * @author jrcorrea
	 * alteração em 07/04/2016
	 * São 3 casos a serem considerados 
	 * 
	 * 1 - processo originario: para todas as instâncias devem ser contado todos os registros que estão na tabela proc com o id_serv igual a serventia em analise e que não esteja em fase de recurso.
	 * 2 - processo originário com recurso: 
	 *  		- para serventias de origem: devem ser contados todos os recursos onde a id_serventia_origem do recurso é igual a serventia em analise E a data de recebimento do processo está dentro do prazo do ponteiro. 
	 * 			- para serventias recursal: devem ser contados todos os recursos onde a id_serventia_origem do recurso atual seja igual a serventia em analise, a data de recebimento do processo está dentro do prazo do ponteiro, 
	 * 															  e não exista um recurso anterior com a id_serv_origem igual a id_serv_recurso do atual.   
	 * 3 - recurso - deve ser contar para a serventia recursal todos os recursos onde o id_serv_recurso do recurso esteja igual ao id_serventia em analise.
	 * 
	 * obs. sempre que alterar essa rotinar verificar os relatórios de ponteiro de distribuição
	 * 
	 * @author jrcorrea
	 */
	
	public String consultarServentiasAreaDistribuicao(String id_AreaDistribuicao, String id_ProcessoTipo,  String Id_Serventia) throws Exception {
		String Sql;
		String stTemp = null;
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Calendar cal = Calendar.getInstance();		
		cal.add(Calendar.DATE, -Configuracao.TEMPO_VALIDADE_PONTEIRO);		
		
		Sql = "SELECT * FROM (";
		Sql += "     SELECT s.ID_SERV, DBMS_RANDOM.value(0, 1) as validar, s.QUANTIDADE_DIST  ,";  
		//processo originário, conto somente para a serventia de origem
		Sql += "       ((SELECT COUNT(1) FROM PROC p INNER JOIN PROC_FASE pf on p.id_proc_fase=pf.id_proc_fase WHERE p.ID_PROC_TIPO = ?";					ps.adicionarLong(id_ProcessoTipo);	
		Sql += "       	AND p.ID_SERV=s.ID_SERV  AND p.data_recebimento >= ?  " ;																			ps.adicionarDate(cal.getTime()); 
		Sql += "       	AND p.proc_numero_antigo_temp IS NULL " ; //Retirando do ponteiro os processos digitalizados
		Sql += "		AND pf.proc_fase_codigo <> ? ) + ";					    																			ps.adicionarLong(ProcessoFaseDt.RECURSO);
		//processo originário em recurso, conto somente para a serventia de origem
		Sql += "        (SELECT COUNT(1) FROM RECURSO r inner join proc p ON r.id_proc= p.id_proc  AND p.ID_PROC_TIPO = ? "; 								ps.adicionarLong(id_ProcessoTipo);		
		Sql += "              WHERE  r.id_serv_origem=s.ID_SERV  AND  p.data_recebimento >= ?   "; 															ps.adicionarDate(cal.getTime());
		Sql += "             	AND p.proc_numero_antigo_temp IS NULL " ; //Retirando do ponteiro os processos digitalizados
		Sql += "				AND NOT EXISTS (SELECT 1 FROM PROJUDI.RECURSO r_anterior WHERE r.ID_PROC = r_anterior.ID_PROC AND r_anterior.ID_RECURSO < r.ID_RECURSO AND (r_anterior.id_serv_recurso = r.id_serv_origem or r_anterior.id_serv_origem = r.id_serv_origem) ) ) + "; 					
		//recurso, conto somete para a serventia de recurso
		Sql += "        (SELECT COUNT(1) FROM RECURSO r inner join proc p on r.id_proc= p.id_proc and p.ID_PROC_TIPO = ? "; 								ps.adicionarLong(id_ProcessoTipo); 				 		
		Sql += "              WHERE  r.id_serv_recurso=s.ID_SERV  AND r.data_recebimento >= ?  "; 														ps.adicionarDate(cal.getTime());
		Sql += "              	AND p.proc_numero_antigo_temp IS NULL " ; //Retirando do ponteiro os processos digitalizados
		Sql += "				AND NOT EXISTS (SELECT 1 FROM PROJUDI.RECURSO r_anterior WHERE r.ID_PROC = r_anterior.ID_PROC AND r_anterior.ID_RECURSO < r.ID_RECURSO AND r_anterior.id_serv_recurso = r.id_serv_recurso) )) as Qtd, ";
	                        		
		Sql += "       (DBMS_RANDOM.RANDOM) as ordem "; 
		Sql += "     FROM  SERV s INNER JOIN SERV_AREA_DIST sad on s.ID_SERV = sad.ID_SERV ";
		Sql += "       WHERE sad.ID_AREA_DIST = ?";																											ps.adicionarLong(id_AreaDistribuicao);
		if (Id_Serventia.length()>0) {
			Sql += "       AND s.ID_SERV <> ?";																												ps.adicionarLong(Id_Serventia);
		}
		Sql += "       AND s.QUANTIDADE_DIST > 0 ";
		Sql += "       AND s.CODIGO_TEMP = ?";																												ps.adicionarLong(ServentiaDt.ATIVO);
		Sql += "   ) tab ";
		Sql += "   WHERE tab.validar<=tab.quantidade_dist ";    
		Sql += "   ORDER BY Qtd, ordem";				

		try{
			rs = consultar(Sql,ps);			
			if (rs.next()) {	
			    //pego a primeira serventia da area distribuição
				stTemp = rs.getString("ID_SERV") ;				
			}
			// rs.close();
		
		} finally{
			try{
				if (rs != null) rs.close();
			} catch(Exception e) {
			}
		}
		return stTemp;
	}
	
	/**
	 * Consulta as serventias de uma determinada área de distribuição
	 * 			dada uma area de distribuição, pego a serventia que recebeu menos processo deste tipo, se for passado uma id_serventia retiro-a das consulta
	 * 			uso o validar para o caso de distribuição 2/1, 3/1 ou 4/1	
	 * 			alteração para atender o provimento 16 de 2012 da corregedoria
	 * @param id_AreaDistribuicao
	 *            identificação da área de distribuição
	 * 
	 * 09/10/2013
	 * alteração em 07/04/2016
	 * São 3 casos a serem considerados 
	 * 
	 * 1 - processo originario: para todas as instâncias devem ser contado todos os registros que estão na tabela proc com o id_serv igual a serventia em analise e que não esteja em fase de recurso.
	 * 2 - processo originário com recurso: 
	 *  		- para serventias de origem: devem ser contados todos os recursos onde a id_serventia_origem do recurso é igual a serventia em analise E a data de recebimento do processo está dentro do prazo do ponteiro. 
	 * 			- para serventias recursal: devem ser contados todos os recursos onde a id_serventia_origem do recurso atual seja igual a serventia em analise, a data de recebimento do processo está dentro do prazo do ponteiro, 
	 * 															  e não exista um recurso anterior com a id_serv_origem igual a id_serv_recurso do atual.   
	 * 3 - recurso - deve ser contar para a serventia recursal todos os recursos onde o id_serv_recurso do recurso esteja igual ao id_serventia em analise.
	 * 
	 * obs. sempre que alterar essa rotinar verificar os relatórios de ponteiro de distribuição
	 * 
	 * @author jrcorrea
	 */
	
	public String consultarServentiasAreaDistribuicao(String id_AreaDistribuicao, String Id_Serventia) throws Exception {
		String Sql;
		String stTemp = null;
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Calendar cal = Calendar.getInstance();		
		cal.add(Calendar.DATE, -Configuracao.TEMPO_VALIDADE_PONTEIRO);				
		
		Sql = "SELECT * FROM (";
		Sql += "     SELECT s.ID_SERV, DBMS_RANDOM.value(0, 1) as validar, s.QUANTIDADE_DIST  ,";  
		//processo originário, conto somente para a serventia de origem
		Sql += "       ((SELECT COUNT(1) FROM PROC p INNER JOIN PROC_FASE pf on p.id_proc_fase=pf.id_proc_fase WHERE ";		
		Sql += "       		p.proc_numero_antigo_temp IS NULL " ; //Retirando do ponteiro os processos digitalizados"
		Sql +=  "			AND	p.ID_SERV=s.ID_SERV  AND p.data_recebimento >= ?  " ; 																				ps.adicionarDate(cal.getTime()); 
		Sql += "       AND pf.proc_fase_codigo <> ? ) + ";					    																			ps.adicionarLong(ProcessoFaseDt.RECURSO);
		//processo originário em recurso, conto somente para a serventia de origem
		Sql += "        (SELECT COUNT(1) FROM RECURSO r inner join proc p on r.id_proc= p.id_proc  ";		
		Sql += "              WHERE  r.id_serv_origem=s.ID_SERV  AND  p.data_recebimento >= ?   "; 															ps.adicionarDate(cal.getTime());
		Sql += "              	AND p.proc_numero_antigo_temp IS NULL " ; //Retirando do ponteiro os processos digitalizados
		Sql += "				AND NOT EXISTS (SELECT 1 FROM PROJUDI.RECURSO r_anterior WHERE r.ID_PROC = r_anterior.ID_PROC AND r_anterior.ID_RECURSO < r.ID_RECURSO AND (r_anterior.id_serv_recurso = r.id_serv_origem or r_anterior.id_serv_origem = r.id_serv_origem) ) ) + "; 					
		//recurso, conto somete para a serventia de recurso
		Sql += "        (SELECT COUNT(1) FROM RECURSO r inner join proc p on r.id_proc= p.id_proc "; 				 		
		Sql += "              WHERE  r.id_serv_recurso=s.ID_SERV  AND r.data_recebimento >= ?  "; 														   ps.adicionarDate(cal.getTime());
		Sql += "              	AND p.proc_numero_antigo_temp IS NULL " ; //Retirando do ponteiro os processos digitalizados
		Sql += "              	AND NOT EXISTS (SELECT 1 FROM PROJUDI.RECURSO r_anterior WHERE r.ID_PROC = r_anterior.ID_PROC AND r_anterior.ID_RECURSO < r.ID_RECURSO AND r_anterior.id_serv_recurso = r.id_serv_recurso) )) as Qtd, ";
		Sql += "       (DBMS_RANDOM.RANDOM) as ordem "; 
		Sql += "     FROM  SERV s INNER JOIN SERV_AREA_DIST sad on s.ID_SERV = sad.ID_SERV ";
		Sql += "       WHERE sad.ID_AREA_DIST = ?";																											ps.adicionarLong(id_AreaDistribuicao);
		if (Id_Serventia.length()>0) {
			Sql += "       AND s.ID_SERV <> ?";																												ps.adicionarLong(Id_Serventia);
		}
		Sql += "       AND s.QUANTIDADE_DIST > 0 ";
		Sql += "       AND s.CODIGO_TEMP = ?";																												ps.adicionarLong(ServentiaDt.ATIVO);
		Sql += "   ) tab ";
		Sql += "   WHERE tab.validar<=tab.quantidade_dist ";    
		Sql += "   ORDER BY Qtd, ordem";
		
		try{
			rs = consultar(Sql,ps);			
			if (rs.next()) {	
			    //pego a primeira serventia da area distribuição
				stTemp = rs.getString("ID_SERV");				
			}
			// rs.close();
		
		} finally{
			try{
				if (rs != null) rs.close();
			} catch(Exception e) {
			}
		}
		return stTemp;
		//return "617";
	}

//	/**
//	 * Retorna as serventias relacionadas a uma serventia passada
//	 * 
//	 * @param idServentiaPrincipal
//	 *            serventia principal
//	 */
//	public List consultarServentiasRelacionadas(String idServentiaPrincipal) throws Exception {
//		String Sql;
//		List liTemp = new ArrayList();
//		ResultSetTJGO rs = null;
//		PreparedStatementTJGO ps = new PreparedStatementTJGO();
//
//		Sql = "SELECT sr.ID_SERV_RELACIONADA, s.ID_SERV, s.SERV_CODIGO, s.SERV, es.UF, sr.PRESIDENCIA, ss.SERV_SUBTIPO_CODIGO, st.SERV_TIPO_CODIGO, ";
//		Sql += " sr.ID_SERV_SUBSTITUICAO, s1.SERV AS SERV_SUBSTITUICAO, sr.DATA_INICIAL_SUBSTITUICAO, sr.DATA_FINAL_SUBSTITUICAO, sr.RECEBE_PROC ";
//		Sql += " FROM PROJUDI.SERV s ";
//		Sql += " JOIN PROJUDI.SERV_RELACIONADA sr on s.ID_SERV = sr.ID_SERV_REL  ";
//		Sql += " JOIN PROJUDI.ESTADO es on s.ID_ESTADO_REPRESENTACAO = es.ID_ESTADO ";
//		Sql += " LEFT JOIN PROJUDI.SERV_SUBTIPO ss on s.ID_SERV_SUBTIPO = ss.ID_SERV_SUBTIPO ";
//		Sql += " LEFT JOIN PROJUDI.SERV_TIPO st on s.ID_SERV_TIPO = st.ID_SERV_TIPO ";
//		Sql += " LEFT JOIN PROJUDI.SERV s1 on sr.ID_SERV_SUBSTITUICAO = s1.ID_SERV ";
//		Sql += " WHERE sr.ID_SERV_PRINC = ?";
//		ps.adicionarLong(idServentiaPrincipal);
//		Sql += " ORDER BY s.SERV ";
//
//		try{
//			rs = consultar(Sql,ps);
//			while (rs.next()) {
//				ServentiaDt obTemp = new ServentiaDt();
//				obTemp.setId_ServentiaRelacaoEdicao(rs.getString("ID_SERV_RELACIONADA"));
//				obTemp.setId(rs.getString("ID_SERV"));
//				obTemp.setServentiaCodigo(rs.getString("SERV_CODIGO"));
//				obTemp.setServentia(rs.getString("SERV"));
//				obTemp.setEstadoRepresentacao(rs.getString("UF"));
//				obTemp.setPresidencia(Funcoes.FormatarLogico(rs.getString("PRESIDENCIA")));		
//				obTemp.setServentiaTipoCodigo(rs.getString("SERV_TIPO_CODIGO"));
//				obTemp.setServentiaSubtipoCodigo(rs.getString("SERV_SUBTIPO_CODIGO"));
//				obTemp.setId_ServentiaSubstituicao(rs.getString("ID_SERV_SUBSTITUICAO"));
//	            obTemp.setServentiaSubstituicao(rs.getString("SERV_SUBSTITUICAO"));
//	            obTemp.setDataInicialSubstituicao(Funcoes.FormatarData(rs.getDateTime("DATA_INICIAL_SUBSTITUICAO")));
//	            obTemp.setDataFinalSubstituicao(Funcoes.FormatarData(rs.getDateTime("DATA_FINAL_SUBSTITUICAO")));
//	            obTemp.setRecebeProcesso(Funcoes.FormatarLogico(rs.getString("RECEBE_PROC")));
//				liTemp.add(obTemp);
//			}
//			// rs.close();
//		
//		} finally{
//			try{
//				if (rs != null) rs.close();
//			} catch(Exception e) {
//			}
//		}
//		return liTemp;
//	}
	
	/**
	 * Retorna as areas distribuições relacionadas a uma serventia passada
	 * 
	 * @param idServentia
	 *            serventia
	 */
	public List consultarAreasDistribuicoesServentia(String idServentia) throws Exception {
		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "SELECT sad.ID_SERV_AREA_DIST, sad.ID_SERV, sad.ID_AREA_DIST, ad.AREA_DIST, ad.AREA_DIST_CODIGO, sad.PROBABILIDADE, ss.SERV_SUBTIPO ";
		Sql += " FROM PROJUDI.AREA_DIST ad ";
		Sql += " JOIN PROJUDI.SERV_AREA_DIST sad on ad.ID_AREA_DIST = sad.ID_AREA_DIST  ";
		Sql += " JOIN PROJUDI.SERV_SUBTIPO ss on ad.ID_SERV_SUBTIPO = ss.ID_SERV_SUBTIPO";
		Sql += " WHERE sad.ID_SERV = ?";																							ps.adicionarLong(idServentia);
		Sql += " ORDER BY ad.AREA_DIST";

		try{
			rs = consultar(Sql,ps);
			while (rs.next()) {
				ServentiaAreaDistribuicaoDt obTemp = new ServentiaAreaDistribuicaoDt();
				obTemp.setId(rs.getString("ID_SERV_AREA_DIST"));
				obTemp.setServAreaDist(rs.getString("AREA_DIST") + "  -  " + rs.getString("SERV_SUBTIPO"));
				obTemp.setCodigoTemp(rs.getString("AREA_DIST_CODIGO"));
				obTemp.setProbabilidade(Funcoes.FormatarDecimal(rs.getString("PROBABILIDADE")));
				liTemp.add(obTemp);
			}
			// rs.close();
		
		} finally{
			try{
				if (rs != null) rs.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}

	/**
	 * Método que retorna todas as serventias do tipo Vara e Turma Recursal sem
	 * uso de filtro para pesquisa e paginação
	 * 
	 * @author msapaula
	 */
	public List consultarServentiasVaraTurma() throws Exception {
		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "SELECT * FROM VIEW_SERV s WHERE s.CODIGO_TEMP = ?";
		ps.adicionarLong(ServentiaDt.ATIVO);		
		Sql += " AND s.SERV_TIPO_CODIGO = ?";
		ps.adicionarLong(ServentiaTipoDt.VARA);
		Sql += " OR s.SERV_TIPO_CODIGO = ?";
		ps.adicionarLong(ServentiaTipoDt.SEGUNDO_GRAU);
		Sql += " ORDER BY s.SERV";

		try{
			rs = consultar(Sql,ps);
			while (rs.next()) {
				ServentiaDt obTemp = new ServentiaDt();
				obTemp.setId(rs.getString("ID_SERV"));
				obTemp.setServentia(rs.getString("SERV"));
				obTemp.setServentiaTipoCodigo(rs.getString("SERV_TIPO_CODIGO"));
				obTemp.setDataCadastro(Funcoes.FormatarData(rs.getDateTime("DATA_CADASTRO")));
				obTemp.setDataImplantacao(Funcoes.FormatarData(rs.getDateTime("DATA_IMPLANTACAO")));
				liTemp.add(obTemp);
			}
			// rs.close();
		
		} finally{
			try{
				if (rs != null) rs.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}

	/**
	 * Método que retorna todas as serventias ativas que sejam do tipo vara ou
	 * turma recursal com uso de paginação
	 * 
	 * @param descricao
	 *            filtro para pesquisa
	 * @param posicao
	 *            parâmetro para paginação
	 * 
	 * @author msapaula
	 */
	public List consultarServentiasVaraTurma(String descricao, String posicao) throws Exception {
		String Sql;
		String sqlComum;
		List liTemp = new ArrayList();
		ResultSetTJGO rs = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = " SELECT v.ID_SERV, v.SERV, v.SERV_TIPO_CODIGO, v.ESTADO_REPRESENTACAO ";
		sqlComum = " FROM VIEW_SERV v WHERE SERV LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		sqlComum += " AND v.CODIGO_TEMP = ?";
		ps.adicionarLong(ServentiaDt.ATIVO);
		sqlComum += " AND (v.SERV_TIPO_CODIGO = ?";
		ps.adicionarLong(ServentiaTipoDt.VARA);
		sqlComum += " OR v.SERV_TIPO_CODIGO= ?)";
		ps.adicionarLong(ServentiaTipoDt.SEGUNDO_GRAU);
		Sql += sqlComum;
		Sql += " ORDER BY SERV";
		

		try{
			rs = consultarPaginacao(Sql, ps, posicao);
			while (rs.next()) {
				ServentiaDt obTemp = new ServentiaDt();
				obTemp.setId(rs.getString("ID_SERV"));
				obTemp.setServentia(rs.getString("SERV"));
				obTemp.setServentiaTipoCodigo(rs.getString("SERV_TIPO_CODIGO"));
				obTemp.setEstado(rs.getString("ESTADO_REPRESENTACAO"));
				liTemp.add(obTemp);
			}
			// rs.close();
			Sql = "SELECT COUNT(*) AS QUANTIDADE " + sqlComum;
			rs2 = consultar(Sql,ps);
			if (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			// rs.close();
		
		} finally{
			try{
				if (rs != null) rs.close();
			} catch(Exception e) {
			}
			try{
				  if (rs2 != null) rs2.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}
	
	public String consultarServentiasVaraTurmaJSON(String descricao, String posicao) throws Exception {
		String Sql;
		String sqlComum;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;

		Sql = "SELECT v.ID_SERV AS ID, v.SERV  AS DESCRICAO1, v.ESTADO_REPRESENTACAO AS DESCRICAO2, v.SERV_TIPO_CODIGO  AS DESCRICAO3 ";
		sqlComum = " FROM VIEW_SERV v WHERE SERV LIKE ?";
		ps.adicionarString("%"+ descricao +"%");

		sqlComum += " AND v.CODIGO_TEMP = ?";
		ps.adicionarLong(ServentiaDt.ATIVO);
		sqlComum += " AND (v.SERV_TIPO_CODIGO = ?";
		ps.adicionarLong(ServentiaTipoDt.VARA);
		sqlComum += " OR v.SERV_TIPO_CODIGO= ?)";
		ps.adicionarLong(ServentiaTipoDt.SEGUNDO_GRAU);
		Sql += sqlComum;
		Sql += " ORDER BY SERV";
		
		try{
			rs1 = consultarPaginacao(Sql, ps, posicao);
			Sql = "SELECT COUNT(*) AS QUANTIDADE " + sqlComum;
			rs2 = consultar(Sql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}

	/**
	 * Método que retorna todas as serventias do tipo Vara, Turma Recursal e
	 * Promotoria para serem utilizadas na distribuição de processos
	 * 
	 * @author msapaula
	 */
	public List consultarServentiasDistribuicaoProcesso() throws Exception {
		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT * FROM VIEW_SERV s WHERE s.CODIGO_TEMP = ?";
		ps.adicionarLong(ServentiaDt.ATIVO);
		Sql += " AND (s.SERV_TIPO_CODIGO = ?";
		ps.adicionarLong(ServentiaTipoDt.VARA);
		Sql += " OR s.SERV_TIPO_CODIGO = ?";
		ps.adicionarLong(ServentiaTipoDt.SEGUNDO_GRAU);
		Sql += " OR s.SERV_TIPO_CODIGO = ?)";
		ps.adicionarLong(ServentiaTipoDt.PROMOTORIA);
		Sql += " ORDER BY s.SERV";

		try{
			rs = consultar(Sql, ps);
			while (rs.next()) {
				ServentiaDt obTemp = new ServentiaDt();
				obTemp.setId(rs.getString("ID_SERV"));
				obTemp.setServentia(rs.getString("SERV"));
				obTemp.setServentiaTipoCodigo(rs.getString("SERV_TIPO_CODIGO"));
				obTemp.setServentiaSubtipoCodigo(rs.getString("SERV_SUBTIPO_CODIGO"));
				obTemp.setDataCadastro(Funcoes.FormatarData(rs.getDateTime("DATA_CADASTRO")));
				obTemp.setDataImplantacao(Funcoes.FormatarData(rs.getDateTime("DATA_IMPLANTACAO")));
				liTemp.add(obTemp);
			}
			// rs.close();
		
		} finally{
			try{
				if (rs != null) rs.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}

	public List relProcessoServentia() throws Exception {
		List liTemp = new ArrayList();
		String stSql;
		ResultSetTJGO rs = null;

		stSql = " SELECT ID_SERV, SERV, QTD_PROCS_DIST, QTD_PROCS_ARQUIVADOS, QTD_PROCS_ATIVOS ";
		stSql += " FROM VIEW_PROCS_SERV_GERAL ";

		try{
			rs = consultarSemParametros(stSql);
			while (rs.next()) {
				ServentiaProcessosDt obTemp = new ServentiaProcessosDt();
				obTemp.setId_Serventia(rs.getString("ID_SERV"));
				obTemp.setServentia(rs.getString("SERV"));
				obTemp.setQtdProcessosDistribuidos(rs.getLong("QTD_PROCS_DIST"));
				obTemp.setQtdProcessosArquivados(rs.getLong("QTD_PROCS_ARQUIVADOS"));
				obTemp.setQtdProcessosAtivos(rs.getLong("QTD_PROCS_ATIVOS"));
				liTemp.add(obTemp);
			}
			// rs.close();

		
		} finally{
			try{
				if (rs != null) rs.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}

	/**
	 * Consultar determinada serventia de acordo com Código Externo
	 * (ServentiaCodigoExterno)
	 * 
	 * @author msapaula
	 */
	public ServentiaDt consultarServentiaCodigoExterno(String serventiaCodigoExterno) throws Exception {
		String Sql;
		ServentiaDt Dados = null;
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "SELECT * FROM PROJUDI.VIEW_SERV WHERE SERV_CODIGO_EXTERNO = ?";
		ps.adicionarLong(serventiaCodigoExterno);

		try{
			rs = consultar(Sql, ps);
			if (rs.next()) {
				Dados = new ServentiaDt();
				Dados.setId(rs.getString("ID_SERV"));
				Dados.setServentia(rs.getString("SERV"));
				Dados.setServentiaCodigo(rs.getString("SERV_CODIGO"));
				Dados.setId_ServentiaTipo(rs.getString("ID_SERV_TIPO"));
				Dados.setServentiaTipo(rs.getString("SERV_TIPO"));
				Dados.setId_Area(rs.getString("ID_AREA"));
				Dados.setArea(rs.getString("AREA"));
				Dados.setId_Endereco(rs.getString("ID_ENDERECO"));
				Dados.setCodigoTemp(rs.getString("CODIGO_TEMP"));
				Dados.setComarca(rs.getString("COMARCA"));
				Dados.setLogradouro(rs.getString("LOGRADOURO"));
				Dados.setNumero(rs.getString("NUMERO"));
				Dados.setComplemento(rs.getString("COMPLEMENTO"));
				Dados.setCep(rs.getString("CEP"));
				Dados.setBairro(rs.getString("BAIRRO"));
				Dados.setCidade(rs.getString("CIDADE"));
				Dados.setEstado(rs.getString("ESTADO"));
				Dados.setDataCadastro(Funcoes.FormatarData(rs.getDateTime("DATA_CADASTRO")));
				Dados.setDataImplantacao(Funcoes.FormatarData(rs.getDateTime("DATA_IMPLANTACAO")));
				
			}
			// rs.close();
		
		} finally{
			try{
				if (rs != null) rs.close();
			} catch(Exception e) {
			}
		}
		return Dados;
	}

	/**
	 * Consulta o código externo de uma serventia, de acordo com o código
	 * passado (ServentiaCodigo)
	 * 
	 * @param serventiaCodigo
	 *            , código a ser consultado
	 * @author msapaula
	 */
	public String consultarCodigoExterno(String serventiaCodigo) throws Exception {
		String Sql;
		String stRetorno = null;
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT SERV_CODIGO_EXTERNO FROM PROJUDI.VIEW_SERV WHERE SERV_CODIGO = ?";
		ps.adicionarLong(serventiaCodigo);

		try{
			rs = consultar(Sql,ps);
			if (rs.next()) {
				stRetorno = rs.getString("SERV_CODIGO_EXTERNO");
			}
		
		} finally{
			try{
				if (rs != null) rs.close();
			} catch(Exception e) {
			}
		}
		return stRetorno;
	}

	/**
	 * Consulta serventias com base no código do tipo de serventia(webservice)
	 * 
	 * @param serventiaTipoCodigo
	 * @param posicao
	 *            paginação
	 * @return lista com as serventias
	 */
	public List consultarServentiaTipoCodigo(String posicao) throws Exception {
		
		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "SELECT ID_SERV, SERV, SERV_CODIGO";
		SqlFrom = " FROM PROJUDI.VIEW_SERV";
		SqlFrom += " WHERE SERV_TIPO_CODIGO IN(?,?,?,?) ";
		ps.adicionarLong("5"); 	// Corregedoria
		ps.adicionarLong("15");	// 2º Grau
		ps.adicionarLong("16");	// Vara
		ps.adicionarLong("20");	// Presidência
		SqlOrder = " ORDER BY SERV";
		
		try{
			rs = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);

			while (rs.next()) {
				ServentiaDt obTemp = new ServentiaDt();
				obTemp.setId(rs.getString("ID_SERV"));
				obTemp.setServentia(rs.getString("SERV"));
				obTemp.setServentiaCodigo(rs.getString("SERV_CODIGO"));
				liTemp.add(obTemp);
			}
			Sql = "SELECT COUNT(*) AS QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			if (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
		
		} finally{
			try{
				if (rs != null) rs.close();
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
	 * Método que consulta a lista de Serventias de uma Comarca.
	 * @param idComarca - ID da Comarca
	 * @return lista de Serventias
	 * @throws Exception
	 * @author hmgodinho
	 * @author jrcorrea
	 * @data 15/09/2015
	 * */	
	public List consultarServentiasComarca(String idComarca, String serventiaTipoCodigo) throws Exception {
		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT s.ID_SERV, s.SERV FROM PROJUDI.VIEW_SERV s ";
		Sql += " WHERE s.ID_COMARCA = ? ";			ps.adicionarLong(idComarca);
		Sql += " AND   s.SERV_TIPO_CODIGO = ? ";	ps.adicionarLong(serventiaTipoCodigo);
		Sql += " ORDER BY s.SERV";

		try{
			rs = consultar(Sql, ps);
			while (rs.next()) {
				ServentiaDt obTemp = new ServentiaDt();
				obTemp.setId(rs.getString("ID_SERV"));
				obTemp.setServentia(rs.getString("SERV"));
				liTemp.add(obTemp);
			}
		
		} finally{
			try{
				if (rs != null) rs.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}
	/*
	 * Retorna o processo de primeiro grau onde nasceu o processo
	 */
	public String consultarServentiaAnteriorProcesso(String id_Processo, String id_ServentiaAtual) throws Exception {

		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String id_ServentiaAnterior = "";
		
		Sql = " Select ";
		Sql += "	m.ID_MOVI, s.ID_SERV, s.SERV ";
		Sql += " from MOVI m ";
		Sql += "    inner join USU_SERV us on (m.ID_USU_REALIZADOR = us.ID_USU_SERV) ";
		Sql += "    inner join Serv s on (us.ID_SERV = s.ID_SERV) ";
		Sql += "    inner join SERV_TIPO st on (s.id_serv_tipo = st.id_serv_tipo and st.serv_tipo_codigo = ?) ";  		ps.adicionarLong(ServentiaDt.VARA_CODIGO);
		Sql += "    inner join SERV_SUBTIPO sst on (s.ID_SERV_SUBTIPO = sst.ID_SERV_SUBTIPO )"; 
		Sql += " where  ";
		Sql += "   m.ID_PROC = ? ";																						ps.adicionarLong(id_Processo);
		Sql += "   and s.id_serv <>  ? ";																				ps.adicionarLong(id_ServentiaAtual);
		Sql += "   and sst.SERV_SUBTIPO_CODIGO not in (?,?,?,?,?,?,?,?)   ";											ps.adicionarLong(ServentiaSubtipoDt.AUDITORIA_MILITAR_CIVEL);
																														ps.adicionarLong(ServentiaSubtipoDt.AUDITORIA_MILITAR_CRIMINAL);
																														ps.adicionarLong(ServentiaSubtipoDt.EXECPENWEB);
																														ps.adicionarLong(ServentiaSubtipoDt.PLANTAO_PRIMEIRO_GRAU);
																														ps.adicionarLong(ServentiaSubtipoDt.PLANTAO_AUDIENCIA_CUSTODIA);
																														ps.adicionarLong(ServentiaSubtipoDt.PRECATORIA);
																														ps.adicionarLong(ServentiaSubtipoDt.PROTOCOLO_JUDICIAL);
																														ps.adicionarLong(ServentiaSubtipoDt.PREPROCESSUAL);
		Sql += " order by m.ID_MOVI desc ";						

		try{
			rs1 = consultar(Sql,ps);
			if (rs1.next()) {
				id_ServentiaAnterior = rs1.getString("ID_SERV");
			}
		
		} finally{
			try{
				  if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return id_ServentiaAnterior;
	}

	public String consultarServentiaAreaDistribuicaoPassada(String id_AreaDistribuicao, String id_Processo, String id_serventia) throws Exception {
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String id_ServentiaAnterior = null;
		
		
		stSql = " SELECT s.id_serv, s.serv ,sad.id_area_dist from ";
		stSql += " (select s.id_serv from movi m "; 
		stSql += " inner join usu_serv us on m.id_usu_realizador = us.id_usu_serv ";
		stSql += " inner join serv s on us.id_serv = s.id_serv ";
		stSql += " where m.id_proc = ? "; ps.adicionarLong(id_Processo);
		stSql += "  and s.id_serv <> ? "; ps.adicionarLong(id_serventia);
		stSql += "  group by s.id_serv ";
		stSql += "  order by max(m.id_movi) desc ) t1";
		stSql += "  inner join serv s on t1.id_serv =s.id_serv";
		stSql += "  inner join serv_area_dist sad on s.id_serv=sad.id_serv";
		stSql += " where sad.id_area_dist = ? ";
		 ps.adicionarLong(id_AreaDistribuicao);

		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				id_ServentiaAnterior = rs1.getString("ID_SERV");
			}
		
		} finally{
			try{
				  if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return id_ServentiaAnterior;
	}
	
	public String consultarServentiasHabilitacaoAdvogadoJSON(String descricao, String posicao) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;
		
		Sql = "SELECT ID_SERV AS ID, SERV AS DESCRICAO1, ESTADO_REPRESENTACAO AS DESCRICAO2";
		SqlFrom = " FROM PROJUDI.VIEW_SERV";
		SqlFrom += " WHERE SERV LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		SqlFrom += " AND ( SERV_TIPO_CODIGO = ?";
		ps.adicionarLong(ServentiaTipoDt.ORDEM_ADVOGADOS_BRASIL);
		SqlFrom += " OR SERV_TIPO_CODIGO = ?";
		ps.adicionarLong(ServentiaTipoDt.PROCURADORIA_GERAL_MUNICIPAL);
		SqlFrom += " OR SERV_TIPO_CODIGO = ?";
		ps.adicionarLong(ServentiaTipoDt.PROCURADORIA_GERAL_ESTADO);
		SqlFrom += " OR SERV_TIPO_CODIGO = ?";
		ps.adicionarLong(ServentiaTipoDt.PROCURADORIA_UNIAO);
		SqlFrom += " OR SERV_TIPO_CODIGO = ?";
		ps.adicionarLong(ServentiaTipoDt.ADVOGADO_PUBLICO_AUTARQUIAS);
		SqlFrom += " OR SERV_TIPO_CODIGO = ?"; 
		ps.adicionarLong(ServentiaTipoDt.DEFENSORIA_PUBLICA);
		SqlFrom += " OR SERV_TIPO_CODIGO = ?)";
		ps.adicionarLong(ServentiaTipoDt.ESCRITORIO_JURIDICO);
		SqlOrder = " ORDER BY SERV";
		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);

			Sql = "SELECT COUNT(*) AS QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
			
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
	
	/**
	 * Método para verificar se a serventia é de segundo grau.
	 * @param String id_serventia
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isServentiaSegundoGrau(String id_serventia) throws Exception {
		boolean retorno = false;
		
		String sql = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sql = "SELECT ID_SERV FROM PROJUDI.SERV WHERE ID_SERV = ? AND ID_SERV_TIPO = ?";
		ps.adicionarLong(id_serventia);
		ps.adicionarLong(ServentiaTipoDt.SEGUNDO_GRAU);
		
		try{
			rs1 = consultar(sql, ps);
			if( rs1 != null ) {
				while (rs1.next()) {
					if( rs1.getString("ID_SERV") != null && rs1.getString("ID_SERV").equals(id_serventia) ) {
						retorno = true;
					}
				}
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return retorno;
	}

	public String consultarDescricaoJSON(String descricao, String posicao, String ordenacao, String quantidadeRegistros) throws Exception {
		
		if(ORDENACAO_PADRAO.equals(ordenacao))
			ordenacao = " SERV ";
		
		String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;

		Sql = "SELECT ID_SERV AS ID, SERV AS DESCRICAO1, ESTADO_REPRESENTACAO AS DESCRICAO2";
		SqlFrom = " FROM PROJUDI.VIEW_SERV";
		SqlFrom += " WHERE SERV LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		SqlOrder = " ORDER BY " + ordenacao;
		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao, quantidadeRegistros);
			Sql = "SELECT COUNT(*) AS QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
	
	public String consultarServentiasDistribuidorAtivoJSON(String descricao, String posicao, String ordenacao, String quantidadeRegistros, String idComarca) throws Exception {
		
		if(ORDENACAO_PADRAO.equals(ordenacao))
			ordenacao = " SERV ";
		
		String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;

		Sql = "SELECT ID_SERV AS ID, SERV AS DESCRICAO1, ESTADO_REPRESENTACAO AS DESCRICAO2";
		SqlFrom = " FROM PROJUDI.VIEW_SERV";
		SqlFrom += " WHERE SERV LIKE ?"; ps.adicionarString("%"+ descricao +"%");
		SqlFrom += " AND ID_COMARCA = ?"; ps.adicionarLong(idComarca); 
		SqlFrom += " AND SERV_TIPO_CODIGO = ?"; ps.adicionarLong(ServentiaTipoDt.DISTRIBUIDOR);
		SqlFrom += " AND CODIGO_TEMP = ?"; ps.adicionarLong(ServentiaDt.ATIVO);
		SqlOrder = " ORDER BY " + ordenacao;
		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao, quantidadeRegistros);
			Sql = "SELECT COUNT(*) AS QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
	
	public String consultarServentiasCentralMandadosJSON(String descricao, String posicao) throws Exception {
		String Sql;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;

		Sql = "SELECT ID_SERV AS ID, SERV AS DESCRICAO1, ESTADO_REPRESENTACAO AS DESCRICAO2 FROM PROJUDI.VIEW_SERV WHERE SERV LIKE ?";
		ps.adicionarString("%" + descricao + "%");
		Sql += " AND SERV_TIPO_CODIGO = ?";
		ps.adicionarLong(ServentiaTipoDt.CENTRAL_MANDADOS);
		Sql += " ORDER BY SERV";
		
		try{
			rs1 = consultarPaginacao(Sql, ps, posicao);
			Sql = "SELECT COUNT(*) AS QUANTIDADE  FROM PROJUDI.VIEW_SERV WHERE SERV LIKE ? AND SERV_TIPO_CODIGO = ?";
			rs2 = consultar(Sql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
	
	/**
	 * Método que efetua a consula de serventias habilitadas
	 * @param descricao - nome da serventia
	 * @param posicao - em caso de paginação é preciso informar a página
	 * @param serventiaTipo - codigo da serventia tipo
	 * @param idComarca - id da comarca para restringir a consulta dentro de uma determinada comarca (campo opcional)
	 * @return lista de serventias
	 * @throws Exception
	 */
	public String consultarServentiasHabilitacaoUsuariosJSON(String descricao, String posicao, String serventiaTipo, String idComarca) throws Exception {

		String sql, sqlFrom, sqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;
		
		sql = "SELECT ID_SERV AS ID, SERV AS DESCRICAO1, ESTADO_REPRESENTACAO AS DESCRICAO2 ";
		sqlFrom = " FROM PROJUDI.VIEW_SERV WHERE SERV LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		if (serventiaTipo != null && serventiaTipo.length() > 0) {
			sqlFrom += " AND SERV_TIPO_CODIGO = ?";
			ps.adicionarLong(serventiaTipo);
		}
		sqlFrom += " AND SERV_TIPO_CODIGO <> ?";
		ps.adicionarLong(ServentiaTipoDt.ORDEM_ADVOGADOS_BRASIL);
		sqlFrom += " AND SERV_TIPO_CODIGO <> ?";
		ps.adicionarLong(ServentiaTipoDt.SISTEMA_PROJUDI);
		sqlFrom += " AND SERV_TIPO_CODIGO <> ?";
		ps.adicionarLong(ServentiaTipoDt.ADMINISTRACAO_SISTEMA_PROJUDI);
		if(idComarca != null && !idComarca.equals("")) {
			sqlFrom += " AND ID_COMARCA = ? ";
			ps.adicionarLong(idComarca);
		}
		sqlOrder = " ORDER BY SERV";

		try{
			rs1 = consultarPaginacao(sql + sqlFrom + sqlOrder, ps, posicao);
			sql = "SELECT COUNT(*) AS QUANTIDADE ";
			rs2 = consultar(sql + sqlFrom + sqlOrder,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
	
	public String consultarServentiasHabilitacaoMasterJSON(String descricao, String posicao, String serventiaTipo) throws Exception {

		String sql, sqlFrom, sqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;
		
		sql = "SELECT ID_SERV AS ID, SERV AS DESCRICAO1, ESTADO_REPRESENTACAO AS DESCRICAO2 ";
		sqlFrom = "FROM PROJUDI.VIEW_SERV WHERE SERV LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		if (serventiaTipo != null && serventiaTipo.length() > 0) {
			sqlFrom += " AND SERV_TIPO_CODIGO = ?";
			ps.adicionarLong(serventiaTipo);
		}
		sqlFrom += " AND SERV_TIPO_CODIGO <> ?";
		ps.adicionarLong(ServentiaTipoDt.SISTEMA_PROJUDI);
		sqlFrom += " AND SERV_TIPO_CODIGO <> ?";
		ps.adicionarLong(ServentiaTipoDt.ADMINISTRACAO_SISTEMA_PROJUDI);
		sqlOrder = " ORDER BY SERV";

		try{
			rs1 = consultarPaginacao(sql + sqlFrom + sqlOrder, ps, posicao);
			sql = "SELECT COUNT(*) AS QUANTIDADE ";
			rs2 = consultar(sql + sqlFrom + sqlOrder,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
	
	public String consultarServentiasAtivasJSON(String descricao, String posicao, String serventiaTipoCodigo) throws Exception {
		String sql, sqlFrom, sqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;

		sql = "SELECT ID_SERV AS ID, SERV AS DESCRICAO1, ESTADO_REPRESENTACAO AS DESCRICAO2 ";
		sqlFrom = " FROM PROJUDI.VIEW_SERV WHERE SERV LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		sqlFrom += " AND CODIGO_TEMP = ?";
		ps.adicionarLong(ServentiaDt.ATIVO);

		if (serventiaTipoCodigo != null && serventiaTipoCodigo.length() > 0) {
			sqlFrom += " AND SERV_TIPO_CODIGO = ?";
			ps.adicionarLong(serventiaTipoCodigo);
		}
		sqlOrder = " ORDER BY SERV";
		
		try{
			rs1 = consultarPaginacao(sql + sqlFrom + sqlOrder, ps, posicao);
			sql = "SELECT COUNT(*) AS QUANTIDADE ";
			rs2 = consultar(sql + sqlFrom + sqlOrder,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
	
	/**
	 * Altera status da serventia
	 * 
	 * @param idServentia
	 * @param statusNovo
	 * @throws Exception 
	 * @author mmgomes
	 */
	public void alterarStatusServentia(String idServentia, String novoStatus) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.SERV SET CODIGO_TEMP = ? ";
		ps.adicionarLong(novoStatus);
		Sql += " WHERE ID_SERV = ? ";
		ps.adicionarLong(idServentia);
		
		executarUpdateDelete(Sql, ps);
		
	}
	
	/**
	 * Consulta uma serventia de um determinado serventiaSubTipoCodigo.
	 * 
	 * @param serventiaSubTipoCodigo
	 * @return
	 * @throws Exception
	 * @author mmgomes
	 */
	public ServentiaDt consultarServentiaPeloSubTipoCodigo(String serventiaSubTipoCodigo) throws Exception {

		String Sql;
		ServentiaDt Dados = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "SELECT * FROM PROJUDI.VIEW_SERV WHERE ID_SERV_SUBTIPO = (SELECT ID_SERV_SUBTIPO FROM PROJUDI.SERV_SUBTIPO WHERE SERV_SUBTIPO_CODIGO = ? AND ROWNUM = 1)";
		ps.adicionarLong(serventiaSubTipoCodigo);

		try{
			rs1 = consultar(Sql,ps);
			if (rs1.next()) {
				Dados = new ServentiaDt();
				associarServentiaDt(Dados, rs1);
			}			
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return Dados;
	}
	
	/**
	 * Método que consulta os IDs das Serventias cadastradas na Área de Distribuição.
	 * @param idAreaDistribuicao - ID da Área de Distribuição
	 * @return lista de IDs de Serventias
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List listarServentiasAreaDistribuicao(String idAreaDistribuicao) throws Exception {
		String Sql;
		List lista = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT ID_SERV FROM PROJUDI.VIEW_SERV_AREA_DIST WHERE ID_AREA_DIST = ?";
		ps.adicionarLong(idAreaDistribuicao);

		try{
			rs1 = consultar(Sql,ps);
			while (rs1.next()) {
				lista.add(rs1.getString("ID_SERV"));
			}
		
		} finally{
			try{
				  if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return lista;
	}
	
	public String consultarServentiasAtivasJSON(String descricao, String posicao, String serventiaTipoCodigo, String id_Comarca, String serventiaSubTipoCodigo) throws Exception {
		String Sql;
		String sqlComum;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;

		Sql = "SELECT ID_SERV AS ID, SERV AS DESCRICAO1, ESTADO_REPRESENTACAO AS DESCRICAO2 ";
		sqlComum = " FROM PROJUDI.VIEW_SERV WHERE SERV LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		sqlComum += " AND CODIGO_TEMP = ?";
		ps.adicionarLong(ServentiaDt.ATIVO);

		if (serventiaTipoCodigo != null && serventiaTipoCodigo.length() > 0) {
			sqlComum += " AND SERV_TIPO_CODIGO = ?";
			ps.adicionarLong(serventiaTipoCodigo);
		}
		if (id_Comarca != null && id_Comarca.length() > 0) {
			sqlComum += " AND ID_COMARCA = ?"; 
			ps.adicionarLong(id_Comarca);			
		}
		if (serventiaSubTipoCodigo != null && serventiaSubTipoCodigo.length() > 0) {
			sqlComum += " AND SERV_SUBTIPO_CODIGO = ?";
			ps.adicionarLong(serventiaSubTipoCodigo);
		}

		Sql += sqlComum;
		Sql += " ORDER BY SERV";
		
		try{
			rs1 = consultarPaginacao(Sql, ps, posicao);
			Sql = "SELECT COUNT(*) AS QUANTIDADE " + sqlComum;
			rs2 = consultar(Sql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
	
	public String consultarServentiasAtivasAreaDistribuicaoJSON(String descricao, String idAreaDistribuicao, String posicao) throws Exception {
		String Sql;
		String sqlComum;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;

		Sql = "SELECT s.ID_SERV AS ID, s.SERV AS DESCRICAO1 ";
		sqlComum = " FROM SERV s INNER JOIN SERV_AREA_DIST sad ON sad.id_serv = s.id_serv WHERE s.SERV LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		sqlComum += " AND s.CODIGO_TEMP = ?";
		ps.adicionarLong(ServentiaDt.ATIVO);
		if (idAreaDistribuicao != null && idAreaDistribuicao.length() > 0) {
			sqlComum += " AND sad.ID_AREA_DIST = ?";
			ps.adicionarLong(idAreaDistribuicao);
		}
		Sql += sqlComum;
		Sql += " ORDER BY s.SERV";
		
		try{
			rs1 = consultarPaginacao(Sql, ps, posicao);
			Sql = "SELECT COUNT(*) AS QUANTIDADE " + sqlComum;
			rs2 = consultar(Sql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
	
	public String consultarServentiasAtivasJSON(String descricao, String posicao, String serventiaTipoCodigo1Grau , String serventiaTipoCodigo2Grau, String id_Comarca, String serventiaSubTipoCodigo) throws Exception {
		String Sql;
		String sqlComum;
		String stTemp = "";
		ResultSetTJGO rs = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;

		Sql = "SELECT ID_SERV AS ID, SERV AS DESCRICAO1, ESTADO_REPRESENTACAO AS DESCRICAO2 ";
		sqlComum = " FROM PROJUDI.VIEW_SERV WHERE SERV LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		sqlComum += " AND CODIGO_TEMP = ?";
		ps.adicionarLong(ServentiaDt.ATIVO);
		
		if (serventiaTipoCodigo1Grau != null && serventiaTipoCodigo1Grau.length() > 0
				&& serventiaTipoCodigo2Grau != null && serventiaTipoCodigo2Grau.length() > 0) {
			sqlComum += " AND (SERV_TIPO_CODIGO = ? OR SERV_TIPO_CODIGO = ? )";
			ps.adicionarLong(serventiaTipoCodigo1Grau); ps.adicionarLong(serventiaTipoCodigo2Grau);
		
		} else if (serventiaTipoCodigo1Grau != null && serventiaTipoCodigo1Grau.length() > 0) {
			sqlComum += " AND SERV_TIPO_CODIGO = ?";
			ps.adicionarLong(serventiaTipoCodigo1Grau);
		
		} else if (serventiaTipoCodigo2Grau != null && serventiaTipoCodigo2Grau.length() > 0) {
			sqlComum += " AND SERV_TIPO_CODIGO = ?";
			ps.adicionarLong(serventiaTipoCodigo2Grau);
		}
		
		if (id_Comarca != null && id_Comarca.length() > 0) {
			sqlComum += " AND ID_COMARCA = ?"; 
			ps.adicionarLong(id_Comarca);			
		}
		if (serventiaSubTipoCodigo != null && serventiaSubTipoCodigo.length() > 0) {
			sqlComum += " AND SERV_SUBTIPO_CODIGO = ?";
			ps.adicionarLong(serventiaSubTipoCodigo);
		}

		Sql += sqlComum;
		Sql += " ORDER BY SERV";
		
		try{
			rs = consultarPaginacao(Sql, ps, posicao);
			Sql = "SELECT COUNT(*) AS QUANTIDADE " + sqlComum;
			rs2 = consultar(Sql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs, qtdeColunas);
		
		} finally{
			try{if (rs != null) rs.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
	
	public String consultarDescricaoServentiaPublicacaoJSON(String descricao, String posicao) throws Exception {

		String Sql;
		String stTemp = "";
		ResultSetTJGO rs = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;
		
		Sql = "SELECT ID_SERV AS ID, SERV AS DESCRICAO1, ESTADO_REPRESENTACAO AS DESCRICAO2 FROM PROJUDI.VIEW_SERV WHERE SERV LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		
		Sql += " AND SERV_TIPO_CODIGO IN (?, ?, ?)";
		ps.adicionarLong(ServentiaTipoDt.VARA);
		ps.adicionarLong(ServentiaTipoDt.SEGUNDO_GRAU);
		ps.adicionarLong(ServentiaTipoDt.CORREGEDORIA);
		Sql += " ORDER BY SERV";
		
		try{
			rs = consultarPaginacao(Sql, ps, posicao);

			Sql = "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_SERV WHERE SERV LIKE ?";
			Sql += " AND SERV_TIPO_CODIGO IN (?, ?, ?)";
			rs2 = consultar(Sql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs, qtdeColunas);
		
		} finally{
			try{if (rs != null) rs.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
	
	public String consultarServentiasComarcaJSON(String descricao, String idComarca, String posicao) throws Exception {
		
		String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp = "";
		ResultSetTJGO rs = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;

		Sql = "SELECT s.ID_SERV AS ID, s.SERV AS DESCRICAO1, ESTADO_REPRESENTACAO AS DESCRICAO2";
		SqlFrom = " FROM PROJUDI.VIEW_SERV s";
		SqlFrom += " WHERE s.ID_COMARCA = ?";
		ps.adicionarLong(idComarca);
		SqlFrom += " AND SERV LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		SqlOrder = " ORDER BY s.SERV";

		try{
			rs = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			
			Sql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs, qtdeColunas);
		
		} finally{try{if (rs != null) rs.close();} catch(Exception e) {}}
		return stTemp;
	}
	
	public String consultarDescricaoServentiaPreProcessualComarcaJSON(String descricao, String idComarca, String posicao) throws Exception {
		
		String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp = "";
		ResultSetTJGO rs = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;

		Sql = "SELECT s.ID_SERV AS ID, s.SERV AS DESCRICAO1, ESTADO_REPRESENTACAO AS DESCRICAO2";
		SqlFrom = " FROM PROJUDI.VIEW_SERV s";
		SqlFrom += " WHERE s.SERV_SUBTIPO_CODIGO = ? AND s.ID_COMARCA = ? ";
		ps.adicionarLong(ServentiaSubtipoDt.PREPROCESSUAL);
		ps.adicionarLong(idComarca);
		SqlFrom += " AND SERV LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		SqlOrder = " ORDER BY s.SERV";

		try {
			rs = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			
			Sql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs, qtdeColunas);
		} catch (Exception e) {
			throw new Exception("<{ Erro ao consultar Serventias por Comarca. }> " + this.getClass().getName() + ".consultarServentiasComarca(): " + e.getMessage(), e);
		} finally {try {if (rs != null) rs.close();} catch (Exception e) {}}
		return stTemp;
	}
	
	public String consultarDescricaoServentiaPreProcessualJSON(String descricao, String posicao) throws Exception {
		
		String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp = "";
		ResultSetTJGO rs = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;

		Sql = "SELECT s.ID_SERV AS ID, s.SERV AS DESCRICAO1, ESTADO_REPRESENTACAO AS DESCRICAO2";
		SqlFrom = " FROM PROJUDI.VIEW_SERV s";
		SqlFrom += " WHERE s.SERV_SUBTIPO_CODIGO = ? ";
		ps.adicionarLong(ServentiaSubtipoDt.PREPROCESSUAL);		
		SqlFrom += " AND SERV LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		SqlOrder = " ORDER BY s.SERV";

		try {
			rs = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			
			Sql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs, qtdeColunas);
		} catch (Exception e) {
			throw new Exception("<{ Erro ao consultar Serventias do tipo preprocessual. }> " + this.getClass().getName() + ".consultarDescricaoServentiaPreProcessualJSON(): " + e.getMessage(), e);
		} finally {try {if (rs != null) rs.close();} catch (Exception e) {}}
		return stTemp;
	}
	
	/**
	 * Retorna a serventia pelo código externo, tipo e subtipo.
	 * @param serventiaCodigoExterno
	 * @param idServentiaTipo
	 * @param idSeventiaSubTipo
	 * @param obFabricaConexao
	 * @return
	 * @throws Exception
	 * @author mmgomes
	 */
	public ServentiaDt consultarServentiaCodigoExterno(String serventiaCodigoExterno, String idServentiaTipo, String idServentiaSubTipo) throws Exception {
		String Sql;
		ServentiaDt Dados = null;
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "SELECT * FROM PROJUDI.VIEW_SERV WHERE SERV_CODIGO_EXTERNO = ? ";
		ps.adicionarLong(serventiaCodigoExterno);
		Sql += "AND ID_SERV_TIPO = ? ";
		ps.adicionarLong(idServentiaTipo);
		Sql += "AND ID_SERV_SUBTIPO = ? ";
		ps.adicionarLong(idServentiaSubTipo);

		try{
			rs = consultar(Sql, ps);
			if (rs.next()) {
				Dados = new ServentiaDt();
				Dados.setId(rs.getString("ID_SERV"));
				Dados.setServentia(rs.getString("SERV"));
				Dados.setServentiaCodigo(rs.getString("SERV_CODIGO"));
				Dados.setId_ServentiaTipo(rs.getString("ID_SERV_TIPO"));
				Dados.setServentiaTipo(rs.getString("SERV_TIPO"));
				Dados.setId_Area(rs.getString("ID_AREA"));
				Dados.setArea(rs.getString("AREA"));
				Dados.setId_Endereco(rs.getString("ID_ENDERECO"));
				Dados.setCodigoTemp(rs.getString("CODIGO_TEMP"));
				Dados.setComarca(rs.getString("COMARCA"));
				Dados.setLogradouro(rs.getString("LOGRADOURO"));
				Dados.setNumero(rs.getString("NUMERO"));
				Dados.setComplemento(rs.getString("COMPLEMENTO"));
				Dados.setCep(rs.getString("CEP"));
				Dados.setBairro(rs.getString("BAIRRO"));
				Dados.setCidade(rs.getString("CIDADE"));
				Dados.setEstado(rs.getString("ESTADO"));
				Dados.setDataCadastro(Funcoes.FormatarData(rs.getDateTime("DATA_CADASTRO")));
				Dados.setDataImplantacao(Funcoes.FormatarData(rs.getDateTime("DATA_IMPLANTACAO")));
				
			}
		
		} finally{
			try{
				if (rs != null) rs.close();
			} catch(Exception e) {
			}
		}
		return Dados;
	}
	
	public List consultarListaServentias(String idAreaDistribuicao) throws Exception {

		String stSql = "";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql = "SELECT ID_SERV, SERV FROM PROJUDI.VIEW_SERV_AREA_DIST WHERE ID_AREA_DIST = ?";
		stSql += " ORDER BY SERV ";
		ps.adicionarLong(idAreaDistribuicao);

		try {

			rs1 = consultar(stSql, ps);

			while (rs1.next()) {
				ServentiaDt obTemp = new ServentiaDt();
				obTemp.setId(rs1.getString("ID_SERV"));
				obTemp.setServentia(rs1.getString("SERV"));
				liTemp.add(obTemp);
			}
		} finally {
			try {if (rs1 != null)rs1.close();} catch (Exception e) {}			
		}
		return liTemp;
	}	

	/**
	 * Listagem de serventias para ser usado na pesquisa digitada no select2.
	 * @param descricao
	 * @return
	 * @throws Exception
	 */
	public List<DescritorComboDt> consultarDescricaoCombo(String descricao) throws Exception {
		
		String Sql;
		List<DescritorComboDt> listaTemp = new ArrayList<DescritorComboDt>();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();		
		String valorFiltro = super.obtenhaValorFiltroConsultarDescricaoCombo(descricao);
		
		Sql = "SELECT * FROM PROJUDI.SERV";
		if (valorFiltro != null) {
			Sql += " WHERE SERV LIKE ?";
			ps.adicionarString(valorFiltro);
		}		 
		Sql += " ORDER BY SERV";
		
		try {
			rs1 = consultar(Sql, ps);
	
			while (rs1.next() && listaTemp.size() <= ProjudiConstantes.QUANTIDADE_OBJETOS_COMBO) {
				DescritorComboDt obTemp = new DescritorComboDt();
				obTemp.setId(rs1.getString("ID_SERV"));
				obTemp.setDescricao(rs1.getString("SERV"));				
				listaTemp.add(obTemp);
			}			
		} finally{
	         try {if (rs1 != null) rs1.close();} catch (Exception e1) {}             
	    }
		return listaTemp;
		
	}
	
	//---------------------------------------------------------
	public void inserir(ServentiaDt dados) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		stSqlCampos= "INSERT INTO PROJUDI.SERV ("; 

		stSqlValores +=  " Values (";
		
		if ((dados.getId().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getId());  

			stVirgula=",";
		}
 
		if ((dados.getServentia().length()>0)) {
			 stSqlCampos+=   stVirgula + "SERV " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getServentia());  

			stVirgula=",";
		}
		if ((dados.getServentiaCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "SERV_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getServentiaCodigo());  

			stVirgula=",";
		}
		if ((dados.getServentiaCodigoExterno().length()>0)) {
			 stSqlCampos+=   stVirgula + "SERV_CODIGO_EXTERNO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getServentiaCodigoExterno());  

			stVirgula=",";
		}
		if ((dados.getId_ServentiaTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ServentiaTipo());  

			stVirgula=",";
		}
		if ((dados.getId_ServentiaSubtipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV_SUBTIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ServentiaSubtipo());  

			stVirgula=",";
		}
		if ((dados.getId_Area().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_AREA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Area());  

			stVirgula=",";
		}
		if ((dados.getId_Comarca().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_COMARCA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Comarca());  

			stVirgula=",";
		}
		if ((dados.getId_AreaDistribuicao().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_AREA_DIST " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_AreaDistribuicao());  

			stVirgula=",";
		}
		if ((dados.getId_AreaDistribuicaoSecundaria().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_AREA_DIST_2 " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_AreaDistribuicaoSecundaria());  

			stVirgula=",";
		}
		if ((dados.getId_EstadoRepresentacao().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_ESTADO_REPRESENTACAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_EstadoRepresentacao());  

			stVirgula=",";
		}
		if ((dados.getId_AudienciaTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_AUDI_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_AudienciaTipo());  

			stVirgula=",";
		}
		if ((dados.getQuantidadeDistribuicao().length()>0)) {
			 stSqlCampos+=   stVirgula + "QUANTIDADE_DIST " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBoolean(dados.getQuantidadeDistribuicao());  

			stVirgula=",";
		}
		if ((dados.getId_Endereco().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_ENDERECO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Endereco());  

			stVirgula=",";
		}
		if ((dados.getTelefone().length()>0)) {
			 stSqlCampos+=   stVirgula + "TELEFONE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getTelefone());  

			stVirgula=",";
		}
		if ((dados.getEmail().length()>0)) {
			 stSqlCampos+=   stVirgula + "EMAIL " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getEmail());  

			stVirgula=",";
		}
		
		if ((dados.getConclusoDireto().length()>0)) {
			 stSqlCampos+=   stVirgula + "CONCLUSO_DIRETO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBoolean(dados.getConclusoDireto());  

			stVirgula=",";
		}
		if ((dados.getOnline().length()>0)) {
			 stSqlCampos+=   stVirgula + "ON_LINE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBoolean(dados.getOnline());  

			stVirgula=",";
		}
		stSqlCampos  +=  stVirgula + "DATA_CADASTRO";
		stSqlValores += stVirgula + "? ";
		ps.adicionarDateTime(new Date());  

		stVirgula=",";
		if ((dados.getDataImplantacao().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_IMPLANTACAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDate(dados.getDataImplantacao());  

			stVirgula=",";
		}		
		
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		
		dados.setId(executarInsert(stSql,"ID_SERV",ps));
	} 

//---------------------------------------------------------
	public void alterar(ServentiaDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		stSql= "UPDATE PROJUDI.SERV SET  ";
		
		stSql+= "SERV = ?";		 ps.adicionarString(dados.getServentia());  

		stSql+= ",SERV_CODIGO = ?";		 ps.adicionarLong(dados.getServentiaCodigo());  

		stSql+= ",SERV_CODIGO_EXTERNO = ?";		 ps.adicionarLong(dados.getServentiaCodigoExterno());  
		
		stSql+= ",ID_CNJ_SERV = ?";		 ps.adicionarString(dados.getId_CNJServentia());  

		stSql+= ",ID_SERV_TIPO = ?";		 ps.adicionarLong(dados.getId_ServentiaTipo());  

		stSql+= ",ID_SERV_SUBTIPO = ?";		 ps.adicionarLong(dados.getId_ServentiaSubtipo());  

		stSql+= ",ID_AREA = ?";		 ps.adicionarLong(dados.getId_Area());  

		stSql+= ",ID_COMARCA = ?";		 ps.adicionarLong(dados.getId_Comarca());  

		stSql+= ",ID_AREA_DIST = ?";		 ps.adicionarLong(dados.getId_AreaDistribuicao());  
		
		stSql+= ",ID_AREA_DIST_2 = ?";		 ps.adicionarLong(dados.getId_AreaDistribuicaoSecundaria());

		stSql+= ",ID_ESTADO_REPRESENTACAO = ?";		 ps.adicionarLong(dados.getId_EstadoRepresentacao());  

		stSql+= ",ID_AUDI_TIPO = ?";		 ps.adicionarLong(dados.getId_AudienciaTipo());  

		stSql+= ",QUANTIDADE_DIST = ?";		 ps.adicionarBoolean(dados.getQuantidadeDistribuicao());

		stSql+= ",ID_ENDERECO = ?";		 ps.adicionarLong(dados.getId_Endereco());  

		stSql+= ",TELEFONE = ?";		 ps.adicionarString(dados.getTelefone());  

		stSql+= ",ON_LINE = ?";		 ps.adicionarBoolean(dados.getOnline());
		
		stSql+= ",DATA_IMPLANTACAO  = ? ";      ps.adicionarDate(dados.getDataImplantacao());

		stSql+= ",CONCLUSO_DIRETO  = ? ";      ps.adicionarBoolean(dados.getConclusoDireto());
		
		stSql+= ",EMAIL  = ? ";      ps.adicionarString(dados.getEmail());
		
		stSql += " WHERE ID_SERV  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 

	//---------------------------------------------------------
	
	protected void associarDt( ServentiaDt Dados, ResultSetTJGO rs1 )  throws Exception {
		
		Dados.setId(rs1.getString("ID_SERV"));
		Dados.setServentia(rs1.getString("SERV"));
		Dados.setServentiaCodigo( rs1.getString("SERV_CODIGO"));
		Dados.setServentiaCodigoExterno( rs1.getString("SERV_CODIGO_EXTERNO"));
		Dados.setId_ServentiaTipo( rs1.getString("ID_SERV_TIPO"));
		Dados.setServentiaTipo( rs1.getString("SERV_TIPO"));
		Dados.setId_ServentiaSubtipo( rs1.getString("ID_SERV_SUBTIPO"));
		Dados.setServentiaSubtipo( rs1.getString("SERV_SUBTIPO"));
		Dados.setId_Area( rs1.getString("ID_AREA"));
		Dados.setArea( rs1.getString("AREA"));
		Dados.setId_Comarca( rs1.getString("ID_COMARCA"));
		Dados.setComarca( rs1.getString("COMARCA"));
		Dados.setId_AreaDistribuicao( rs1.getString("ID_AREA_DIST"));
		Dados.setAreaDistribuicao( rs1.getString("AREA_DIST"));		
		Dados.setId_AreaDistribuicaoSecundaria( rs1.getString("ID_AREA_DIST_2"));
		Dados.setAreaDistribuicaoSecundaria( rs1.getString("AREA_DIST_2"));		
		Dados.setId_EstadoRepresentacao( rs1.getString("ID_ESTADO_REPRESENTACAO"));
		Dados.setEstadoRepresentacao( rs1.getString("ESTADO_REPRESENTACAO"));
		Dados.setId_AudienciaTipo( rs1.getString("ID_AUDI_TIPO"));
		Dados.setAudienciaTipo( rs1.getString("AUDI_TIPO"));		
		Dados.setId_Endereco( rs1.getString("ID_ENDERECO"));
		Dados.setLogradouro( rs1.getString("LOGRADOURO"));
		Dados.setTelefone( rs1.getString("TELEFONE"));
		Dados.setEmail( rs1.getString("EMAIL"));
		Dados.setOnline( Funcoes.FormatarLogico(rs1.getString("ON_LINE")));
		Dados.setCodigoTemp( rs1.getString("CODIGO_TEMP"));
		Dados.setNumero( rs1.getString("NUMERO"));
		Dados.setComplemento( rs1.getString("COMPLEMENTO"));
		Dados.setId_Bairro( rs1.getString("ID_BAIRRO"));
		Dados.setBairro( rs1.getString("BAIRRO"));
		Dados.setCidade( rs1.getString("CIDADE"));
		Dados.setEstado( rs1.getString("ESTADO"));
		Dados.setCep( rs1.getString("CEP"));
		Dados.setServentiaTipoCodigo( rs1.getString("SERV_TIPO_CODIGO"));
		Dados.setServentiaSubtipoCodigo( rs1.getString("SERV_SUBTIPO_CODIGO"));
		Dados.setAreaCodigo( rs1.getString("AREA_CODIGO"));
		Dados.setComarcaCodigo( rs1.getString("COMARCA_CODIGO"));
		Dados.setDataCadastro(Funcoes.FormatarData(rs1.getDateTime("DATA_CADASTRO")));
		Dados.setDataImplantacao(Funcoes.FormatarData(rs1.getDateTime("DATA_IMPLANTACAO")));
		Dados.setConclusoDireto(Funcoes.FormatarLogico(rs1.getString("CONCLUSO_DIRETO")));
		Dados.setQuantidadeDistribuicao( Funcoes.FormatarLogico(rs1.getString("QUANTIDADE_DIST")));
	}
	
	public String obtenhaProximoId() throws Exception {
		String Sql;
		ResultSetTJGO rs1 = null;
		String proximoId = "1";
		
		Sql = "SELECT SERVENTIA_ID_SERVENTIA_SEQ.NEXTVAL AS PROXIMO_VALOR FROM DUAL";		

		try{
			rs1 = consultarSemParametros(Sql);
			if (rs1.next()) {
				proximoId = rs1.getString("PROXIMO_VALOR");
			}			
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return proximoId;
	}
	
	/**
	 * Recebe o id de uma serventia e retorna o id da comarca dela.
	 * @param idServentia
	 * @return
	 * @throws Exception
	 */
	public String consultarIdComarca(String idServentia) throws Exception {

		String stSql = "";
		String idComarca = null;
		ResultSetTJGO rs1 = null;		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql = "SELECT ID_COMARCA FROM PROJUDI.SERV WHERE ID_SERV = ?";
		ps.adicionarLong(idServentia);

		try {

			rs1 = consultar(stSql, ps);

			if (rs1.next()) {
				idComarca = rs1.getString("ID_COMARCA");
			}
		} finally {
			try {if (rs1 != null)rs1.close();} catch (Exception e) {}			
		}
		return idComarca;
	}
	
	/**
	 * Recebe o id da serventia e retorna o código do serv_tipo dela.
	 * @param idServentia
	 * @return
	 * @throws Exception
	 * @author hrrosa
	 */
	public String consultarCodigoServentiaTipo(String idServentia) throws Exception {
		String stSql = "";
		String servTipoCodigo = null;
		ResultSetTJGO rs1 = null;		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql = "SELECT SERV_TIPO_CODIGO FROM PROJUDI.VIEW_SERV WHERE ID_SERV = ?";
		ps.adicionarLong(idServentia);

		try {

			rs1 = consultar(stSql, ps);

			if (rs1.next()) {
				servTipoCodigo = rs1.getString("SERV_TIPO_CODIGO");
			}
		} finally {
			try {if (rs1 != null)rs1.close();} catch (Exception e) {}			
		}
		return servTipoCodigo;
	}
	
	/**
	 *Método para verificar se a serventia é um escritório jurídico ou procuradoria ou defensoria.
	 * @param String id_serventia
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isServentiaEscritorioJuridicoProcuradoriaDefensoria(String id_serventia) throws Exception {
		boolean retorno = false;
		
		String sql = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sql = "SELECT ID_SERV FROM PROJUDI.VIEW_SERV WHERE ID_SERV = ? AND SERV_TIPO_CODIGO IN (?,?,?,?,?)";
		ps.adicionarLong(id_serventia);
		ps.adicionarLong(ServentiaTipoDt.PROCURADORIA_GERAL_MUNICIPAL);
		ps.adicionarLong(ServentiaTipoDt.PROCURADORIA_GERAL_ESTADO);
		ps.adicionarLong(ServentiaTipoDt.PROCURADORIA_UNIAO);
		ps.adicionarLong(ServentiaTipoDt.DEFENSORIA_PUBLICA);
		ps.adicionarLong(ServentiaTipoDt.ESCRITORIO_JURIDICO);
		
		try{
			rs1 = consultar(sql, ps);
			if( rs1 != null ) {
				while (rs1.next()) {
					if( rs1.getString("ID_SERV") != null && rs1.getString("ID_SERV").equals(id_serventia) ) {
						retorno = true;
					}
				}
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return retorno;
	}
	
	public String consultarDescricaoServentiaUnidaTrabalhoJSON(String descricao, int grupoCodigo, String posicao, String ordenacao, String quantidadeRegistros) throws Exception {
		
		if(ORDENACAO_PADRAO.equals(ordenacao))
			ordenacao = " SERV ";
		
		String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;

		Sql = "SELECT ID_SERV AS ID, SERV AS DESCRICAO1, ESTADO_REPRESENTACAO AS DESCRICAO2";
		SqlFrom = " FROM PROJUDI.VIEW_SERV";
		SqlFrom += " WHERE SERV LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		
		if (grupoCodigo == GrupoDt.CADASTRADOR_MASTER){
			SqlFrom += " AND SERV_SUBTIPO_CODIGO = ? "; ps.adicionarLong(ServentiaSubtipoDt.GABINETE_FLUXO_UPJ); 
		} else {
			SqlFrom += " AND SERV_SUBTIPO_CODIGO in ( ?, ? ) "; ps.adicionarLong(ServentiaSubtipoDt.GABINETE_PRESIDENCIA_TJGO);  ps.adicionarLong(ServentiaSubtipoDt.GABINETE_VICE_PRESIDENCIA_TJGO); 
		}
		
		SqlOrder = " ORDER BY " + ordenacao;
		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao, quantidadeRegistros);
			Sql = "SELECT COUNT(*) AS QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
	
	/**
	 * Método para Listar todas as serventias existentes na minuta não analisada de uma sessão virtual para o desembargador.
	 * @param String stNomeBusca1
	 * @param String posicaoPaginaAtual
	 * @param UsuarioDt usuarioDt, 
	 * @return String
	 * @throws Exception
	 * @author lrcampos
	 * @since 19/03/2020
	 */
	public String consultarDescricaoServentiaMinutaNaoAnalisadaJSON(String stNomeBusca1, String posicaoPaginaAtual,
			String id_ServentiaCargo, Boolean isIniciada) throws Exception {

		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder();
		String sqlQtd = "";
		String stTemp = "";
		int qtdeColunas = 1;
		
		sql.append(" SELECT DISTINCT S.ID_SERV AS ID, S.SERV AS DESCRICAO1");
		sql.append(" FROM PROJUDI.VIEW_BUSCA_CONCLUSOES_PEND PEN ");
		sql.append(" INNER JOIN PROJUDI.AUDI_PROC AP ON	(AP.ID_PEND_VOTO = pen.ID_PEND OR AP.ID_PEND_EMENTA = pen.ID_PEND OR AP.ID_PEND_VOTO_REDATOR = pen.ID_PEND OR AP.ID_PEND_EMENTA_REDATOR = pen.ID_PEND) AND AP.DATA_MOVI IS NULL ");
		sql.append(" INNER JOIN PROJUDI.PROC_TIPO PT ON (AP.ID_PROC_TIPO = PT.ID_PROC_TIPO) ");
		sql.append(" INNER JOIN AUDI A ON ( A.ID_AUDI = AP.ID_AUDI AND A.VIRTUAL = 1 AND A.SESSAO_INICIADA ");
		if(isIniciada) 
			sql.append(" = 1 )");
		else
			sql.append(" IS NULL )");
		sql.append(" INNER JOIN SERV S ON S.ID_SERV = A.ID_SERV ");
		sql.append(" INNER JOIN PEND_TIPO PT ON PT.ID_PEND_TIPO = PEN.ID_PEND_TIPO ");
		sql.append(" WHERE pen.ID_SERV_CARGO = ? "); ps.adicionarLong(id_ServentiaCargo);
		sql.append(" AND PT.PEND_TIPO_CODIGO = ?"); ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VOTO);
		sql.append(" AND (pen.CODIGO_TEMP_PEND IS NULL OR pen.CODIGO_TEMP_PEND <> -4) ");
		sql.append(" AND NOT EXISTS ( ");
		sql.append(" 					SELECT 1 FROM PROJUDI.AUDI_PROC aci");
		sql.append("					INNER JOIN AUDI AUD ON AUD.ID_AUDI  = ACI.ID_AUDI ");
		sql.append("					INNER JOIN AUDI_TIPO AT1 ON AT1.ID_AUDI_TIPO  = AUD.ID_AUDI_TIPO ");
		sql.append("					INNER JOIN AUDI_PROC_STATUS APS1 ON APS1.ID_AUDI_PROC_STATUS  = ACI.ID_AUDI_PROC_STATUS");
		sql.append("					WHERE ");
		sql.append("					at1.AUDI_TIPO_CODIGO = ? "); ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo());
		sql.append("					AND APS1.AUDI_PROC_STATUS_CODIGO = ? "); ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		sql.append("					AND aci.ID_PROC IS NOT NULL AND aci.DATA_MOVI IS NULL AND (aci.ID_PEND_VOTO = pen.ID_PEND OR aci.ID_PEND_VOTO_REDATOR = pen.ID_PEND) ");
		sql.append("					AND (aci.ID_SERV_CARGO = ? OR aci.ID_SERV_CARGO_REDATOR = ? )"); 
		ps.adicionarLong(id_ServentiaCargo); 
		ps.adicionarLong(id_ServentiaCargo);
		sql.append("					AND (NOT aci.ID_ARQ_ATA IS NULL AND aci.ID_ARQ_ATA > 0) AND aci.ID_PROC = pen.ID_PROC )");
		sql.append(" AND AP.ID_AUDI_PROC_STATUS = ?	AND A.VIRTUAL IS NOT NULL "); ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		
		try{
			rs1 = consultarPaginacao(sql.toString(), ps, posicaoPaginaAtual);

			sqlQtd = "SELECT COUNT(*) AS QUANTIDADE";			
			rs2 = consultar(sqlQtd + " FROM ( " + sql.toString() + ")", ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicaoPaginaAtual, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}					
		}
		return stTemp;
	}
	
	/**
	 * Método para Listar todas as serventias existentes na minuta pré analisada de uma sessão virtual para o desembargador.
	 * @param String stNomeBusca1
	 * @param String posicaoPaginaAtual
	 * @param UsuarioDt usuarioDt, 
	 * @return String
	 * @throws Exception
	 * @author lrcampos
	 * @since 19/03/2020
	 */
	
	public String consultarDescricaoPreAnalisadaServentiaMinutaJSON(String stNomeBusca1, String posicaoPaginaAtual,
			String id_ServentiaCargo, Boolean isIniciada) throws Exception {

		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder();
		String sqlQtd = "";
		String stTemp = "";
		int qtdeColunas = 1;
		sql.append(" SELECT DISTINCT S.ID_SERV AS ID, S.SERV AS DESCRICAO1");
		sql.append(" FROM PROJUDI.PEND P ");
		sql.append(" INNER JOIN AUDI_PROC_PEND APP ON APP.ID_PEND = P.ID_PEND");
		sql.append(" INNER JOIN AUDI_PROC AP ON (AP.ID_AUDI_PROC = APP.ID_AUDI_PROC AND AP.DATA_MOVI IS NULL)");
		sql.append(" INNER JOIN PROJUDI.PEND_TIPO PT ON PT.ID_PEND_TIPO = P.ID_PEND_TIPO ");
		sql.append(" INNER JOIN PROJUDI.PEND_ARQ pa ON (p.ID_PEND = pa.ID_PEND AND pa.RESPOSTA = 1 AND pa.CODIGO_TEMP <> -3 ) ");
		sql.append(" INNER JOIN PROJUDI.PEND_RESP pr ON	p.ID_PEND = pr.ID_PEND ");
		sql.append(" INNER JOIN AUDI AD ON (AD.ID_AUDI = AP.ID_AUDI AND AD.SESSAO_INICIADA  ");
		if(isIniciada) 
			sql.append(" = 1 )");
		else
			sql.append(" IS NULL )");
		sql.append(" INNER JOIN ARQ A ON	PA.ID_ARQ = A.ID_ARQ ");
		sql.append(" INNER JOIN PROC PRO ON	P.ID_PROC = PRO.ID_PROC ");
		sql.append(" INNER JOIN SERV S ON S.ID_SERV = AD.ID_SERV ");
		sql.append(" WHERE");
		sql.append(" pt.PEND_TIPO_CODIGO = ?"); ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VOTO);
		sql.append(" AND AP.ID_AUDI_PROC_STATUS = ? "); ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		sql.append(" AND (p.CODIGO_TEMP IS NULL	OR p.CODIGO_TEMP <> -4) "); 
		sql.append(" AND NOT EXISTS ( ");
		sql.append("				SELECT 1 FROM PROJUDI.AUDI_PROC aci");
		sql.append("					INNER JOIN AUDI A1 ON A1.ID_AUDI = ACI.ID_AUDI ");
		sql.append("					INNER JOIN AUDI_TIPO AT1 ON AT1.ID_AUDI_TIPO = A1.ID_AUDI_TIPO ");
		sql.append("					INNER JOIN AUDI_PROC_STATUS APS1 ON	APS1.ID_AUDI_PROC_STATUS = ACI.ID_AUDI_PROC_STATUS "); 
		sql.append("					WHERE AT1.AUDI_TIPO_CODIGO = ?"); ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo());
		sql.append(" 					AND APS1.AUDI_PROC_STATUS_CODIGO = ?"); ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		sql.append("					AND aci.ID_PROC IS NOT NULL AND aci.DATA_MOVI IS NULL AND (aci.ID_PEND_VOTO = P.ID_PEND OR aci.ID_PEND_VOTO_REDATOR = P.ID_PEND)");
		sql.append("					AND (aci.ID_SERV_CARGO = ? OR aci.ID_SERV_CARGO_REDATOR = ?)");
		ps.adicionarLong(id_ServentiaCargo); 
		ps.adicionarLong(id_ServentiaCargo);
		sql.append("					AND (NOT aci.ID_ARQ_ATA IS NULL AND aci.ID_ARQ_ATA > 0)");
		sql.append("					AND aci.ID_AUDI_PROC = app.ID_AUDI_PROC )");
		sql.append(" AND NOT EXISTS (");
		sql.append("				 SELECT 1 FROM PROJUDI.PEND P2");
		sql.append("				 INNER JOIN AUDI_PROC_PEND APP1 ON APP1.ID_PEND = P2.ID_PEND");
		sql.append("				 WHERE APP1.ID_AUDI_PROC = APP.ID_AUDI_PROC ");
		sql.append("					AND P2.ID_PEND_TIPO IN (");
		sql.append("											SELECT ID_PEND_TIPO	FROM PROJUDI.PEND_TIPO "); 
		sql.append("												WHERE PEND_TIPO_CODIGO IN (?,?)))");
		ps.adicionarLong(PendenciaTipoDt.VOTO_SESSAO);
		ps.adicionarLong(PendenciaTipoDt.PROCLAMACAO_VOTO);
		sql.append(" AND NOT EXISTS ( SELECT 1 FROM PEND_VOTO_VIRTUAL PVV ");
		sql.append("				  INNER JOIN AUDI_PROC_PEND APP1 ON	(APP1.ID_AUDI_PROC = PVV.ID_AUDI_PROC AND PVV.ID_PEND = APP1.ID_PEND)");
		sql.append("				  WHERE APP1.ID_AUDI_PROC = APP.ID_AUDI_PROC AND APP1.ID_PEND = APP.ID_PEND )");
		sql.append(" AND NOT EXISTS ( SELECT 1 FROM PROJUDI.AUDI_PROC ACI");
		sql.append(" 				  	INNER JOIN PROJUDI.PEND_VOTO_VIRTUAL PV ON	ACI.ID_AUDI_PROC = PV.ID_AUDI_PROC");
		sql.append("					INNER JOIN AUDI A2 ON A2.ID_AUDI = ACI.ID_AUDI");
		sql.append(" 					INNER JOIN AUDI_TIPO AT2 ON AT2.ID_AUDI_TIPO = A2.ID_AUDI_TIPO ");
		sql.append("					INNER JOIN AUDI_PROC_STATUS APS2 ON APS2.ID_AUDI_PROC_STATUS = ACI.ID_AUDI_PROC_STATUS");
		sql.append("					WHERE AT2.AUDI_TIPO_CODIGO = ?"); ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo());
		sql.append("					AND APS2.AUDI_PROC_STATUS_CODIGO = ?"); ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		sql.append("					AND aci.ID_AUDI_PROC = APP.ID_AUDI_PROC AND (aci.ID_PEND_VOTO = APP.ID_PEND OR aci.ID_PEND_VOTO_REDATOR = APP.ID_PEND) )");
		sql.append(" AND p.ID_PROC IS NOT NULL AND p.DATA_FIM IS NULL");
		sql.append(" AND ( SELECT COUNT(1) FROM PEND_ARQ PA1 WHERE PA1.ID_ARQ = PA.ID_ARQ) = 1");
		sql.append(" AND pr.ID_SERV_CARGO = ?"); ps.adicionarLong(id_ServentiaCargo);
		sql.append(" AND P.ID_PEND_STATUS <> ?"); ps.adicionarLong(PendenciaStatusDt.ID_PRE_ANALISADA);
		sql.append(" AND AD.VIRTUAL = 1 ");
		try{
			rs1 = consultarPaginacao(sql.toString(), ps, posicaoPaginaAtual);

			sqlQtd = "SELECT COUNT(*) AS QUANTIDADE";			
			rs2 = consultar(sqlQtd + " FROM ( " + sql.toString() + ")", ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicaoPaginaAtual, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}					
		}
		return stTemp;
	}
	
	/**
	 * Método para Listar todas as serventias existentes no voto de uma sessão virtual para o desembargador.
	 * @param String stNomeBusca1
	 * @param String posicaoPaginaAtual
	 * @param UsuarioDt usuarioDt, 
	 * @return String
	 * @throws Exception
	 * @author lrcampos
	 * @since 19/03/2020
	 */
	
	public String consultarDescricaoServentiaVotoJSON(String stNomeBusca1, String posicaoPaginaAtual,
			String id_ServentiaCargo, Integer pendStatusCodigo, Integer codigoTemp) throws Exception {

		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sqlQtd = "";
		String stTemp = "";
		int qtdeColunas = 1;
		StringBuilder sql = obtemSqlConsultarServentiaVoto(id_ServentiaCargo, pendStatusCodigo, codigoTemp, ps);
		try{
			rs1 = consultarPaginacao(sql.toString(), ps, posicaoPaginaAtual);

			sqlQtd = "SELECT COUNT(*) AS QUANTIDADE";			
			rs2 = consultar(sqlQtd + " FROM ( " + sql.toString() + ")", ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicaoPaginaAtual, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}					
		}
		return stTemp;
	}

	public StringBuilder obtemSqlConsultarServentiaVoto(String id_ServentiaCargo, Integer pendStatusCodigo, Integer codigoTemp,
			PreparedStatementTJGO ps) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT S.ID_SERV AS ID, S.SERV AS DESCRICAO1");
		sql.append(" FROM PROJUDI.VIEW_PEND P ");
		sql.append(" INNER JOIN PROJUDI.PEND_RESP PR ON P.ID_PEND = PR.ID_PEND ");
		sql.append(" INNER JOIN PROJUDI.AUDI_PROC AP ON P.ID_PROC = AP.ID_PROC ");
		sql.append(" INNER JOIN PROJUDI.AUDI A ON A.ID_AUDI = AP.ID_AUDI");
		sql.append(" INNER JOIN PROJUDI.AUDI_PROC_PEND APP ON (APP.ID_PEND = P.ID_PEND AND AP.ID_AUDI_PROC = APP.ID_AUDI_PROC)");
		sql.append(" INNER JOIN PROJUDI.VIEW_SERV_CARGO SC ON SC.ID_SERV_CARGO = AP.ID_SERV_CARGO");
		sql.append(" INNER JOIN PROC_TIPO PT ON PT.ID_PROC_TIPO = AP.ID_PROC_TIPO"); 
		sql.append(" INNER JOIN SERV S ON S.ID_SERV = A.ID_SERV"); 
		sql.append(" WHERE ");
		sql.append(" P.PEND_TIPO_CODIGO = ?");ps.adicionarLong(PendenciaTipoDt.VOTO_SESSAO);
		sql.append(" AND PR.ID_SERV_CARGO = ?"); ps.adicionarLong(id_ServentiaCargo);
		sql.append(" AND P.PEND_STATUS_CODIGO = ? "); ps.adicionarLong(pendStatusCodigo);
		if(codigoTemp != null) {
			sql.append("AND P.CODIGO_TEMP = ? "); ps.adicionarLong(codigoTemp);
		}else {
			sql.append(" AND AP.DATA_MOVI IS NULL");
		}
		return sql;
	}
}
