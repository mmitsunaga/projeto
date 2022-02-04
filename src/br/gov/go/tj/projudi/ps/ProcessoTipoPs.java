package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.DescritorComboDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ProjudiConstantes;
import br.gov.go.tj.utils.ResultSetTJGO;

public class ProcessoTipoPs extends ProcessoTipoPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = -6208335029897241097L;

    public ProcessoTipoPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Método que contém a sql que irá buscar os tipos de processo (tipos de ação) existentes
	 * 
	 * @author Keila
	 * @return listaProcessoTipos: lista contendo os tipos de processos existentes
	 * @throws Exception
	 */
	public List getProcessoTipos() throws Exception {
		List listaProcessoTipos = new ArrayList();
		String sql;
		ResultSetTJGO rs1 = null;
	

		sql = "SELECT * FROM projudi.VIEW_PROC_TIPO pt ORDER BY pt.PROC_TIPO";

		try{
			rs1 = consultarSemParametros(sql);
			while (rs1.next()) {
				ProcessoTipoDt processoTipoDt = new ProcessoTipoDt();
				processoTipoDt.setId(rs1.getString("ID_PROC_TIPO"));
				processoTipoDt.setProcessoTipoCodigo(rs1.getString("PROC_TIPO_CODIGO"));
				processoTipoDt.setProcessoTipo(rs1.getString("PROC_TIPO"));
				processoTipoDt.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
				processoTipoDt.setPublico(Funcoes.FormatarLogico(rs1.getString("PUBLICO")));
				processoTipoDt.setCnjCodigo(rs1.getString("ID_CNJ_CLASSE"));
				listaProcessoTipos.add(processoTipoDt);
			}
			//rs1.close();
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }
		return listaProcessoTipos;
	}

	/**
	 * Método que contém a sql que irá buscar o tipos de processo (tipo de ação) solicitado
	 * 
	 * @param processoTipoCodigo
	 * @return processoTipoDt: tipo de processo solicitado
	 * @throws Exception
	 */
	public ProcessoTipoDt consultarProcessoTipoCodigo(String processoTipoCodigo) throws Exception {
		ProcessoTipoDt processoTipoDt = null;
		String sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		try{

			sql = "SELECT * FROM projudi.VIEW_PROC_TIPO pt WHERE pt.PROC_TIPO_CODIGO =  ?";
			ps.adicionarLong(processoTipoCodigo);

			rs1 = consultar(sql, ps);
			if (rs1.next()) {
				processoTipoDt = new ProcessoTipoDt();  
				processoTipoDt.setId(rs1.getString("ID_PROC_TIPO"));
				processoTipoDt.setProcessoTipoCodigo(rs1.getString("PROC_TIPO_CODIGO"));
				processoTipoDt.setProcessoTipo(rs1.getString("PROC_TIPO"));
				processoTipoDt.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
				processoTipoDt.setPublico(Funcoes.FormatarLogico(rs1.getString("PUBLICO")));
				processoTipoDt.setCnjCodigo(rs1.getString("ID_CNJ_CLASSE"));
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return processoTipoDt;
	}
	
	/** Método que contém a sql que irá buscar o tipos de processo (tipo de ação) solicitado
	 * 
	 * @param processoTipoCodigo
	 * @return processoTipoDt: tipo de processo solicitado
	 * @throws Exception
	 */
	public String consultarIdProcessoTipo(String processoTipoCodigo) throws Exception {
		String id = "";
		String sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
			
		try{
			sql = "SELECT ID_PROC_TIPO FROM projudi.VIEW_PROC_TIPO pt WHERE pt.PROC_TIPO_CODIGO = ?";
			ps.adicionarLong(processoTipoCodigo);

			rs1 = consultar(sql,ps);
			if (rs1.next()) {
				id = rs1.getString("ID_PROC_TIPO");
			}
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return id;
	}
	
	/**
	
	/**
	 * Método que contém a sql que irá buscar os tipos de processo (tipos de ação) existentes pela descrição informada
	 * 
	 * @return listaProcessoTipos: lista contendo os tipos de processos encontrados
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarProcessoTipoDescricao(String nomeBusca) throws Exception {
		List listaProcessoTipos = new ArrayList();
		String sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql = "SELECT * FROM projudi.VIEW_PROC_TIPO pt WHERE PROC_TIPO LIKE ? ORDER BY pt.PROC_TIPO";
		ps.adicionarString("%" + nomeBusca + "%" );

		try{
			rs1 = consultar(sql,ps);
			while (rs1.next()) {
				ProcessoTipoDt processoTipoDt = new ProcessoTipoDt();
				processoTipoDt.setId(rs1.getString("ID_PROC_TIPO"));
				processoTipoDt.setProcessoTipoCodigo(rs1.getString("PROC_TIPO_CODIGO"));
				processoTipoDt.setProcessoTipo(rs1.getString("PROC_TIPO"));
				processoTipoDt.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
				processoTipoDt.setPublico(Funcoes.FormatarLogico(rs1.getString("PUBLICO")));
				processoTipoDt.setCnjCodigo(rs1.getString("ID_CNJ_CLASSE"));
				listaProcessoTipos.add(processoTipoDt);
			}
		
		} finally{
			try{
				if (rs1 != null)
					rs1.close();
			} catch(Exception e) {
			}
		}
		return listaProcessoTipos;
	}
	
	/**
	 * Método para consultar o código pelo id.
	 * @param String idProcessoTipo
	 * @return String 
	 * @throws Exception
	 */
	public String consultarCodigo(String idProcessoTipo) throws Exception {
		String processoTipoCodigo = null;
		
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		try{
			String sql = "SELECT PROC_TIPO_CODIGO FROM projudi.PROC_TIPO WHERE ID_PROC_TIPO = ?";
			ps.adicionarLong(idProcessoTipo);
			
			rs1 = consultar(sql,ps);
			if( rs1 != null ) {
				while(rs1.next()) 
					processoTipoCodigo = rs1.getString("PROC_TIPO_CODIGO");
			}
		
		}
		finally{
			 rs1.close();
		}
		return processoTipoCodigo;
	}
	
	/**
	 * Método para consultar lista de processo tipo pela descrição.
	 * @param String descricao
	 * @return List
	 * @throws Exception
	 */
	public List consultarDescricao(String descricao) throws Exception {
		List listaProcessoTipoDt = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		try{
			String sql = "SELECT * FROM projudi.VIEW_PROC_TIPO WHERE PROC_TIPO LIKE ?";
			ps.adicionarString("%"+ descricao +"%");

			rs1 = consultar(sql,ps);
			if (rs1.next()) {
				while(rs1.next()) {
					if( listaProcessoTipoDt == null ) {
						listaProcessoTipoDt = new ArrayList();
					}
					ProcessoTipoDt processoTipoDt = new ProcessoTipoDt();  
					processoTipoDt.setId(rs1.getString("ID_PROC_TIPO"));
					processoTipoDt.setProcessoTipoCodigo(rs1.getString("PROC_TIPO_CODIGO"));
					processoTipoDt.setProcessoTipo(rs1.getString("PROC_TIPO"));
					processoTipoDt.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
					processoTipoDt.setPublico(Funcoes.FormatarLogico(rs1.getString("PUBLICO")));
					processoTipoDt.setCnjCodigo(rs1.getString("ID_CNJ_CLASSE"));
					
					listaProcessoTipoDt.add(processoTipoDt);
				}
			}
		
		}
		finally{
			 rs1.close();
		}
		
		return listaProcessoTipoDt;
	}
	
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		////System.out.println("..ps-ConsultaDescricaoProcessoTipo()");
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql= "SELECT ID_PROC_TIPO, PROC_TIPO, PROC_TIPO_CODIGO, PUBLICO, ID_CNJ_CLASSE FROM projudi.VIEW_PROC_TIPO WHERE PROC_TIPO LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		Sql+= " ORDER BY PROC_TIPO ";
		try{
			////System.out.println("..ps-ConsultaDescricaoProcessoTipo  " + Sql);

			rs1 = consultarPaginacao(Sql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ProcessoTipoDt obTemp = new ProcessoTipoDt();
				obTemp.setId(rs1.getString("ID_PROC_TIPO"));
				obTemp.setProcessoTipo(rs1.getString("PROC_TIPO"));
				obTemp.setProcessoTipoCodigo(rs1.getString("PROC_TIPO_CODIGO"));
				obTemp.setPublico(Funcoes.FormatarLogico(rs1.getString("PUBLICO")));
				obTemp.setCnjCodigo(rs1.getString("ID_CNJ_CLASSE"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			Sql= "SELECT COUNT(*) as QUANTIDADE  FROM projudi.VIEW_PROC_TIPO WHERE PROC_TIPO LIKE ?";
			rs2 = consultar(Sql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			if (rs2.next()) 
				liTemp.add(rs2.getLong("QUANTIDADE"));
			
			//rs1.close();
			////System.out.println("..ProcessoTipoPsGen.consultarDescricao() Operação realizada com sucesso");
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp; 
	}

	/**
	 * Método que consulta os Tipos de Processo ligados à Serventia através de suas Áreas de Distribuição.
	 * @param descricao - descrição do tipo de processo
	 * @param listaAreaDistribuicao - lista de IDs das Áreas de Distribuição vinculadas à Serventia
	 * @param posicaoPaginaAtual - posição da paginação
	 * @return lista de tipos de processo
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarProcessoTipoServentia(String descricao, List listaAreaDistribuicao, String posicao) throws Exception {
		String sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql = "SELECT ss.ID_PROC_TIPO, ss.PROC_TIPO FROM projudi.VIEW_SERV_SUBTIPO_PROC_TIPO ss";
		sql += " LEFT JOIN AREA_DIST a on a.ID_SERV_SUBTIPO=ss.ID_SERV_SUBTIPO ";
		for (int i = 0; i < listaAreaDistribuicao.size(); i++) {
			if(i == 0){
				sql += "WHERE a.ID_AREA_DIST in(";
			}
			
			sql += "?"; ps.adicionarLong((String)listaAreaDistribuicao.get(i));
			
			if(i == listaAreaDistribuicao.size() - 1){
				sql += ") ";
			} else {
				sql += ", ";
			}
		}
		if(descricao != null && !descricao.equalsIgnoreCase("")) {
			sql += " AND ss.PROC_TIPO like ?";			ps.adicionarString("%"+ descricao +"%");
		}
		sql += " ORDER BY PROC_TIPO";

		try{
			rs1 = consultarPaginacao(sql, ps, posicao);

			while (rs1.next()) {
				ProcessoTipoDt obTemp = new ProcessoTipoDt();
				obTemp.setId(rs1.getString("ID_PROC_TIPO"));
				obTemp.setProcessoTipo(rs1.getString("PROC_TIPO"));
				liTemp.add(obTemp);
			}
			sql = "SELECT COUNT(*) AS QUANTIDADE FROM projudi.VIEW_SERV_SUBTIPO_PROC_TIPO ss";
			sql += " LEFT JOIN AREA_DIST a on a.ID_SERV_SUBTIPO=ss.ID_SERV_SUBTIPO ";
			for (int i = 0; i < listaAreaDistribuicao.size(); i++) {
				if(i == 0){
					sql += "WHERE a.ID_AREA_DIST in(";
				}
				
				sql += "?";
				
				if(i == listaAreaDistribuicao.size() - 1){
					sql += ") ";
				} else {
					sql += ", ";
				}
			}
			if(descricao != null && !descricao.equalsIgnoreCase("")) {
				sql += " AND ss.PROC_TIPO like ? ";
			}

			rs2 = consultar(sql,ps);
			if (rs2.next()) 
				liTemp.add(rs2.getLong("QUANTIDADE"));
			
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp;
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {

		String Sql;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 3;

		Sql= "SELECT ID_PROC_TIPO AS ID, PROC_TIPO DESCRICAO1, PROC_TIPO_CODIGO DESCRICAO2, PUBLICO DESCRICAO3 FROM PROJUDI.VIEW_PROC_TIPO WHERE PROC_TIPO LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		Sql+= " ORDER BY PROC_TIPO ";
		try{

			rs1 = consultarPaginacao(Sql, ps, posicao);
			Sql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PROC_TIPO WHERE PROC_TIPO LIKE ?";
			rs2 = consultar(Sql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
			
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return stTemp; 
	}
	
	public String consultarProcessoTipoJSON(String descricao, String posicao ) throws Exception {

		String Sql;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;

		Sql= "SELECT ID_PROC_TIPO AS ID, PROC_TIPO DESCRICAO1, PROC_TIPO_CODIGO DESCRICAO2, PUBLICO FROM PROJUDI.VIEW_PROC_TIPO WHERE PROC_TIPO LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		Sql+= " ORDER BY PROC_TIPO ";
		try{

			rs1 = consultarPaginacao(Sql, ps, posicao);
			Sql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PROC_TIPO WHERE PROC_TIPO LIKE ?";
			rs2 = consultar(Sql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
			
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return stTemp; 
	}
	
	public String consultarProcessoTipoServentiaJSON(String descricao, String idServentia, String posicao) throws Exception {
		String sql, sqlFrom, sqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 3;

		sql = "SELECT DISTINCT ss.ID_PROC_TIPO AS ID, ss.PROC_TIPO AS DESCRICAO1, ss.proc_tipo_codigo as DESCRICAO2, ss.ID_CNJ_CLASSE as DESCRICAO3 ";
		sqlFrom = " FROM projudi.VIEW_SERV_SUBTIPO_PROC_TIPO ss LEFT JOIN AREA_DIST a on a.ID_SERV_SUBTIPO=ss.ID_SERV_SUBTIPO ";
		sqlFrom += " WHERE a.ID_AREA_DIST in(SELECT ID_AREA_DIST FROM SERV_AREA_DIST WHERE ID_SERV=?) ";	ps.adicionarLong(idServentia);
		
		if(descricao != null && !descricao.equalsIgnoreCase("")) {
			sqlFrom += " AND ss.PROC_TIPO like ?";															ps.adicionarString("%" + descricao +"%");
		}
		sqlOrder = " ORDER BY PROC_TIPO";

		try{
			rs1 = consultarPaginacao(sql + sqlFrom + sqlOrder, ps, posicao);
			sql = "SELECT COUNT(DISTINCT ss.ID_PROC_TIPO) AS QUANTIDADE ";

			rs2 = consultar(sql + sqlFrom ,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return stTemp;
	}
	
	public String consultarProcessoTipoPublicoServentiaJSON(String descricao, String idServentia, String posicao) throws Exception {
		String sql, sqlFrom, sqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 3;

		sql = " SELECT DISTINCT ss.ID_PROC_TIPO AS ID, ss.PROC_TIPO AS DESCRICAO1, ss.PROC_TIPO_CODIGO AS DESCRICAO2, ss.ID_CNJ_CLASSE AS DESCRICAO3 ";
		sqlFrom = " FROM projudi.VIEW_SERV_SUBTIPO_PROC_TIPO ss  LEFT JOIN AREA_DIST a on a.ID_SERV_SUBTIPO=ss.ID_SERV_SUBTIPO ";
		sqlFrom += " WHERE a.ID_AREA_DIST in(SELECT ID_AREA_DIST FROM SERV_AREA_DIST WHERE ID_SERV=?) ";		ps.adicionarLong(idServentia);
		if(descricao != null && !descricao.equalsIgnoreCase("")) {
			sqlFrom += " AND ss.PROC_TIPO like ?";																ps.adicionarString( "%" + descricao +"%");
		}
		sqlFrom += " AND ss.PUBLICO = ? ";																		ps.adicionarBoolean(true);
		
		sqlOrder = " ORDER BY PROC_TIPO ";

		try{
			rs1 = consultarPaginacao(sql + sqlFrom + sqlOrder, ps, posicao);
			sql =  " SELECT COUNT(DISTINCT ss.ID_PROC_TIPO) AS QUANTIDADE ";
			
			rs2 = consultar(sql+sqlFrom,ps);
			
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return stTemp;
	}
	
	/**
	 * Método para consultar o código CNJ pelo id.
	 * @param String idProcessoTipo
	 * @return String 
	 * @throws Exception
	 */
	public String consultarCodigoCNJ(String idProcessoTipo) throws Exception {
		String processoTipoCodigoCNJ = null;
		
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		try{
			String sql = "SELECT ID_CNJ_CLASSE FROM projudi.PROC_TIPO WHERE ID_PROC_TIPO = ?";
			ps.adicionarLong(idProcessoTipo);
			
			rs1 = consultar(sql,ps);
			if( rs1 != null ) {
				while(rs1.next()) 
					processoTipoCodigoCNJ = rs1.getString("ID_CNJ_CLASSE");
			}
		
		}
		finally{
			 rs1.close();
		}
		return processoTipoCodigoCNJ;
	}
	
	public String consultarProcessoTipoServentiaRecursoJSON(String descricao, String idRecurso, boolean somenteAtivos, String posicao) throws Exception {
		String sql, sqlFrom, sqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;

		sql = "SELECT pt.ID_PROC_TIPO AS ID, pt.PROC_TIPO AS DESCRICAO1, pt.proc_tipo_codigo as DESCRICAO2 ";
		sqlFrom = " FROM projudi.VIEW_PROC_TIPO pt ";
		sqlFrom += " WHERE pt.ID_PROC_TIPO in (SELECT ID_PROC_TIPO FROM projudi.RECURSO_PARTE WHERE ID_RECURSO = ? ";	ps.adicionarLong(idRecurso);
		if (somenteAtivos) {
			sqlFrom += " AND DATA_BAIXA IS NULL ";
		}
		sqlFrom += " ) ";
		
		if(descricao != null && !descricao.equalsIgnoreCase("")) {
			sqlFrom += " AND pt.PROC_TIPO like ?";															ps.adicionarString("%" + descricao +"%");
		}
		
		sqlOrder = " ORDER BY PROC_TIPO";

		try{
			rs1 = consultarPaginacao(sql + sqlFrom + sqlOrder, ps, posicao);
			sql = "SELECT COUNT(*) AS QUANTIDADE ";

			rs2 = consultar(sql + sqlFrom ,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return stTemp;
	}
	
	public List<ProcessoTipoDt> consultarProcessoTipoServentiaRecurso(String descricao, String idRecurso, boolean somenteRecursosAtivos) throws Exception {
		String sql, sqlFrom, sqlOrder;
		List<ProcessoTipoDt> listaProcessosTipos = new ArrayList<ProcessoTipoDt>();
		ResultSetTJGO rs1 = null;		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();		

		sql = "SELECT * ";
		sqlFrom = " FROM projudi.VIEW_PROC_TIPO pt ";
		sqlFrom += " WHERE pt.ID_PROC_TIPO in (SELECT rp.ID_PROC_TIPO   ";	
		sqlFrom += "                             FROM projudi.RECURSO_PARTE RP INNER JOIN projudi.RECURSO R ON R.ID_RECURSO = RP.ID_RECURSO ";
		sqlFrom += "                             INNER JOIN projudi.PROC P ON P.ID_PROC = R.ID_PROC ";
		sqlFrom += "                            WHERE R.ID_RECURSO = ? "; ps.adicionarLong(idRecurso);
		if (somenteRecursosAtivos) {
			sqlFrom += "                         AND R.DATA_RETORNO IS NULL ";
			sqlFrom += "                         AND R.ID_SERV_RECURSO = P.ID_SERV ";
			sqlFrom += "                         AND RP.DATA_BAIXA IS NULL ";
		}		
		sqlFrom += " ) ";
		
		if(descricao != null && !descricao.equalsIgnoreCase("")) {
			sqlFrom += " AND pt.PROC_TIPO like ?";															ps.adicionarString("%" + descricao +"%");
		}
		sqlOrder = " ORDER BY pt.PROC_TIPO";

		try{
			rs1 = consultar(sql + sqlFrom + sqlOrder, ps);
			
			while (rs1.next()) {
				ProcessoTipoDt processoTipoDt = new ProcessoTipoDt();
				processoTipoDt.setId(rs1.getString("ID_PROC_TIPO"));
				processoTipoDt.setProcessoTipoCodigo(rs1.getString("PROC_TIPO_CODIGO"));
				processoTipoDt.setProcessoTipo(rs1.getString("PROC_TIPO"));
				processoTipoDt.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
				processoTipoDt.setPublico(Funcoes.FormatarLogico(rs1.getString("PUBLICO")));
				processoTipoDt.setCnjCodigo(rs1.getString("ID_CNJ_CLASSE"));				
				listaProcessosTipos.add(processoTipoDt);
			}
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}             
        }
		return listaProcessosTipos;
	}
	
	public List<DescritorComboDt> consultarDescricaoCombo(String descricao, String id_AreaDistribuicao, String id_Serventia, boolean ehAdvogado) throws Exception {
		String Sql;
		List<DescritorComboDt> listaTemp = new ArrayList<DescritorComboDt>();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();		
		String valorFiltro = super.obtenhaValorFiltroConsultarDescricaoCombo(descricao);
		String filtroWhereAnd = "WHERE";
		
		Sql = "SELECT ss.ID_PROC_TIPO, ss.PROC_TIPO";
		Sql += " FROM PROJUDI.VIEW_SERV_SUBTIPO_PROC_TIPO ss";
		Sql += " LEFT JOIN PROJUDI.AREA_DIST a on a.ID_SERV_SUBTIPO=ss.ID_SERV_SUBTIPO ";		
		if (valorFiltro != null) {
			Sql += filtroWhereAnd + " ss.PROC_TIPO LIKE ? "; 
			ps.adicionarString(valorFiltro);
			filtroWhereAnd = "AND";
		}		
		if (id_AreaDistribuicao != null && id_AreaDistribuicao.trim().length() > 0 && Funcoes.StringToLong(id_AreaDistribuicao.trim()) > 0) {
			Sql += filtroWhereAnd + " a.ID_AREA_DIST = ? ";
			ps.adicionarLong(id_AreaDistribuicao.trim());	
			filtroWhereAnd = "AND";
		}
		if (id_Serventia != null && id_Serventia.trim().length() > 0 && Funcoes.StringToLong(id_Serventia.trim()) > 0) {
			Sql += filtroWhereAnd + " a.ID_AREA_DIST in(SELECT ID_AREA_DIST FROM SERV_AREA_DIST WHERE ID_SERV=?) ";
			ps.adicionarLong(id_Serventia.trim());	
			filtroWhereAnd = "AND";
		}
		if (ehAdvogado) {
			Sql += filtroWhereAnd + " ss.PUBLICO = ? ";
			ps.adicionarBoolean(true);	
			filtroWhereAnd = "AND";
		}
		Sql += " ORDER BY PROC_TIPO";
		
		try {
			rs1 = consultar(Sql, ps);

			while (rs1.next() && listaTemp.size() <= ProjudiConstantes.QUANTIDADE_OBJETOS_COMBO) {
				DescritorComboDt obTemp = new DescritorComboDt();
				obTemp.setId(rs1.getString("ID_PROC_TIPO"));
				obTemp.setDescricao(rs1.getString("PROC_TIPO"));				
				listaTemp.add(obTemp);
			}			
		} finally{
             try {if (rs1 != null) rs1.close();} catch (Exception e1) {}             
        }
		return listaTemp;
	}
	
	//---------------------------------------------------------
		public void inserir(ProcessoTipoDt dados ) throws Exception {

			String stSqlCampos="";
			String stSqlValores="";
			String stSql="";
			String stVirgula="";
			PreparedStatementTJGO ps = new PreparedStatementTJGO();
			////System.out.println("....psProcessoTipoinserir()");
			stSqlCampos= "INSERT INTO projudi.PROC_TIPO ("; 

			stSqlValores +=  " Values (";
	 
			if ((dados.getProcessoTipo().length()>0)) {
				 stSqlCampos+=   stVirgula + "PROC_TIPO " ;
				 stSqlValores+=   stVirgula + "? " ;
				 ps.adicionarString(dados.getProcessoTipo());  

				stVirgula=",";
			}
			if ((dados.getProcessoTipoCodigo().length()>0)) {
				 stSqlCampos+=   stVirgula + "PROC_TIPO_CODIGO " ;
				 stSqlValores+=   stVirgula + "? " ;
				 ps.adicionarLong(dados.getProcessoTipoCodigo());  

				stVirgula=",";
			}
			if ((dados.getOrdem2Grau().length()>0)) {
				 stSqlCampos+=   stVirgula + "ORDEM_2_GRAU " ;
				 stSqlValores+=   stVirgula + "? " ;
				 ps.adicionarLong(dados.getOrdem2Grau());  

				stVirgula=",";
			}
			if ((dados.getPublico().length()>0)) {
				 stSqlCampos+=   stVirgula + "PUBLICO " ;
				 stSqlValores+=   stVirgula + "? " ;
				 ps.adicionarBoolean(dados.getPublico());  

				stVirgula=",";
			}
			
			if ((dados.getCnjCodigo().length()>0)) {
				 stSqlCampos+=   stVirgula + "ID_CNJ_CLASSE " ;
				 stSqlValores+=   stVirgula + "? " ;
				 ps.adicionarLong(dados.getCnjCodigo());  

				stVirgula=",";
			}

			
			stSqlCampos+= ")";
			stSqlValores+= ")";
			stSql+= stSqlCampos + stSqlValores; 
			////System.out.println("....Sql " + stSql);

			
				dados.setId(executarInsert(stSql,"ID_PROC_TIPO",ps));
				////System.out.println("....Execução OK"  );

			 
		} 

	//---------------------------------------------------------
		public void alterar(ProcessoTipoDt dados) throws Exception{

			PreparedStatementTJGO ps = new PreparedStatementTJGO();
			String stSql="";

			////System.out.println("....psProcessoTipoalterar()");

			stSql= "UPDATE projudi.PROC_TIPO SET  ";
			stSql+= "PROC_TIPO = ?";		 ps.adicionarString(dados.getProcessoTipo());  

			stSql+= ",PROC_TIPO_CODIGO = ?";		 ps.adicionarLong(dados.getProcessoTipoCodigo()); 
			
			stSql+= ",ORDEM_2_GRAU = ?";		 ps.adicionarLong(dados.getOrdem2Grau());
			
			stSql+= ",ID_CNJ_CLASSE = ?";		 ps.adicionarLong(dados.getCnjCodigo());			
			
			stSql+= ",PUBLICO = ?";		 ps.adicionarBoolean(dados.getPublico());  

			stSql += " WHERE ID_PROC_TIPO  = ? "; 		ps.adicionarLong(dados.getId()); 

			executarUpdateDelete(stSql,ps); 
		
		}
		
		protected void associarDt( ProcessoTipoDt Dados, ResultSetTJGO rs )  throws Exception {

			Dados.setId(rs.getString("ID_PROC_TIPO"));
			Dados.setProcessoTipo(rs.getString("PROC_TIPO"));
			Dados.setProcessoTipoCodigo( rs.getString("PROC_TIPO_CODIGO"));
			Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
			Dados.setPublico( Funcoes.FormatarLogico(rs.getString("PUBLICO")));
			Dados.setOrdem2Grau( rs.getString("ORDEM_2_GRAU"));
			Dados.setCnjCodigo( rs.getString("ID_CNJ_CLASSE"));
			
		}
		
		//---------------------------------------------------------
		public ProcessoTipoDt consultarId(String id_processotipo )  throws Exception {

			String stSql="";
			ResultSetTJGO rs1 = null;
			PreparedStatementTJGO ps = new PreparedStatementTJGO();
			ProcessoTipoDt Dados=null;
			////System.out.println("....ps-ConsultaId_ProcessoTipo)");

			stSql= "SELECT * FROM projudi.VIEW_PROC_TIPO WHERE ID_PROC_TIPO = ?";		ps.adicionarLong(id_processotipo); 

			////System.out.println("....Sql  " + stSql  );

			try{
				////System.out.println("..ps-ConsultaId_ProcessoTipo  " + stSql);
				rs1 = consultar(stSql,ps);
				if (rs1.next()) {
					Dados= new ProcessoTipoDt();
					associarDt(Dados, rs1);
				}
				////System.out.println("..ps-ConsultaId");
			}finally{
					try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				}
				return Dados; 
		}
		
		//Metodo criado para retornar a descricao da parte (CNJ) a partir da classe (id_proc_tipo) do projudi.
		public String consultarDescricaoPoloAtivoPassivoJSON(String idProcTipo) throws Exception {
			String sql, sqlFrom; 
			String stTemp = "";
			List<String> listaPolos = new ArrayList<String>();
			ResultSetTJGO rs1 = null;		
			PreparedStatementTJGO ps = new PreparedStatementTJGO();		

			sql = "SELECT PT.POLO_ATIVO, PT.POLO_PASSIVO ";
			sqlFrom = " FROM PROJUDI.PROC_TIPO PT ";
			sqlFrom += " WHERE PT.ID_PROC_TIPO = ? ";						ps.adicionarString(idProcTipo);			

			try{
				rs1 = consultar(sql + sqlFrom, ps);
				if (rs1.next()) {
					stTemp = "{\"poloAtivo\": \""+rs1.getString("POLO_ATIVO")+"\", \"poloPassivo\": \""+rs1.getString("POLO_PASSIVO")+"\"}";
				}
	        } finally{
	             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}             
	        }
			return stTemp;
		}
		
		public List<ProcessoTipoDt> consultarPorCodigos(List<Integer> codigos)  throws Exception {

			String stSql="";
			ResultSetTJGO rs1 = null;
			PreparedStatementTJGO ps = new PreparedStatementTJGO();
			ProcessoTipoDt dt=null;
			List<ProcessoTipoDt> lista = new ArrayList<>();

			stSql= "SELECT * FROM projudi.VIEW_PROC_TIPO WHERE PROC_TIPO_CODIGO IN (";
			StringBuilder builder = new StringBuilder();
			for (Integer integer : codigos) {
				builder.append(",?");
				ps.adicionarLong(integer);
			}
			stSql += builder.replace(0, 1, "").append(")").toString();
			stSql += " ORDER BY PROC_TIPO";

			try{
				rs1 = consultar(stSql,ps);
				while (rs1.next()) {
					dt= new ProcessoTipoDt();
					associarDt(dt, rs1);
					lista.add(dt);
				}
			}finally{
					try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				}
				return lista; 
		}
}
