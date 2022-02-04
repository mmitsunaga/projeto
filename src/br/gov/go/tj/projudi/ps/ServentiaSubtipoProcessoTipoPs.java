package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoProcessoTipoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class ServentiaSubtipoProcessoTipoPs extends ServentiaSubtipoProcessoTipoPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = 231551530012095272L;

    public ServentiaSubtipoProcessoTipoPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Consulta as classes disponíveis a partir da área de distribuição, chegando assim
	 * ao ServentiaSubTipo em questão.
	 * 
	 * @param id_AreaDistribuicao, area de distribuição da serventia em questão
	 * @param descricao, filtro de descrição
	 * @param posicao, parametro usado na paginação
	 * 
	 * @author msapaula
	 */
	public List consultarProcessoTipos(String id_AreaDistribuicao, String descricao, String posicao) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT ss.ID_PROC_TIPO, ss.PROC_TIPO, ss.PROC_TIPO_CODIGO, ss.ID_CNJ_CLASSE";
		SqlFrom = " FROM PROJUDI.VIEW_SERV_SUBTIPO_PROC_TIPO ss";
		SqlFrom += " LEFT JOIN PROJUDI.AREA_DIST a on a.ID_SERV_SUBTIPO=ss.ID_SERV_SUBTIPO ";
		SqlFrom += " WHERE ss.PROC_TIPO like ?";
		ps.adicionarString( descricao +"%");
		
		if (id_AreaDistribuicao.length() > 0) {
			SqlFrom += " AND a.ID_AREA_DIST = ?";
			ps.adicionarLong(id_AreaDistribuicao);
		}
		SqlOrder = " ORDER BY PROC_TIPO";

		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);

			while (rs1.next()) {
				ProcessoTipoDt obTemp = new ProcessoTipoDt();
				obTemp.setId(rs1.getString("ID_PROC_TIPO"));
				obTemp.setProcessoTipo(rs1.getString("PROC_TIPO"));
				obTemp.setProcessoTipoCodigo(rs1.getString("PROC_TIPO_CODIGO"));
				obTemp.setCnjCodigo(rs1.getString("ID_CNJ_CLASSE"));
				liTemp.add(obTemp);
			}
			Sql = "SELECT COUNT(*) AS QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			if (rs2.next())liTemp.add(rs2.getLong("QUANTIDADE"));
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp;
	}
	
	/**
	 * Consulta as classes disponíveis a partir da área de distribuição, chegando assim
	 * ao ServentiaSubTipo em questão.
	 * 
	 * @param id_AreaDistribuicao, area de distribuição da serventia em questão
	 * @param descricao, filtro de descrição
	 * @param posicao, parametro usado na paginação
	 * 
	 */
	public List consultarProcessoTiposPublicos(String id_AreaDistribuicao, String descricao, String posicao) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT ss.ID_PROC_TIPO, ss.PROC_TIPO";
		SqlFrom = " FROM PROJUDI.VIEW_SERV_SUBTIPO_PROC_TIPO ss";
		SqlFrom += " LEFT JOIN PROJUDI.AREA_DIST a on a.ID_SERV_SUBTIPO=ss.ID_SERV_SUBTIPO ";
		SqlFrom += " WHERE ss.PROC_TIPO like ? AND ss.PUBLICO = ? ";
		ps.adicionarString( descricao +"%");
		ps.adicionarBoolean(true);
		
		if (id_AreaDistribuicao.length() > 0) {
			SqlFrom += " AND a.ID_AREA_DIST = ?";
			ps.adicionarLong(id_AreaDistribuicao);
		}
		SqlOrder = " ORDER BY PROC_TIPO";

		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);

			while (rs1.next()) {
				ProcessoTipoDt obTemp = new ProcessoTipoDt();
				obTemp.setId(rs1.getString("ID_PROC_TIPO"));
				obTemp.setProcessoTipo(rs1.getString("PROC_TIPO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			Sql = "SELECT COUNT(*) AS QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			if (rs2.next())liTemp.add(rs2.getLong("QUANTIDADE"));
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp;
	}

	/**
	 * Consulta os tipos de processo disponíveis em uma determinada Serventia Subtipo,
	 * com uso de paginação
	 * 
	 */
	public List consultarDescricaoProcessoTipos(String id_ServentiaSubtipo, String descricao, String posicao) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT ID_PROC_TIPO,PROC_TIPO";
		SqlFrom = " FROM VIEW_SERV_SUBTIPO_PROC_TIPO";
		SqlFrom += " WHERE PROC_TIPO like ?";
		ps.adicionarString( descricao +"%");
		
		if (id_ServentiaSubtipo.length() > 0) {
			SqlFrom += " AND ID_SERV_SUBTIPO = ?";
			ps.adicionarLong(id_ServentiaSubtipo);
		}
		SqlOrder = " ORDER BY PROC_TIPO";

		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);

			while (rs1.next()) {
				ProcessoTipoDt obTemp = new ProcessoTipoDt();
				obTemp.setId(rs1.getString("ID_PROC_TIPO"));
				obTemp.setProcessoTipo(rs1.getString("PROC_TIPO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			Sql = "SELECT COUNT(*) AS QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			if (rs2.next())	liTemp.add(rs2.getLong("QUANTIDADE"));
			
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp;
	}

	/**
	 * Consulta os tipos de processo disponíveis em um determinado ServentiaSubtipo.
	 * 
	 * @param id_ServentiaSubTipo, identificação do sub-tipo de serventia
	 * @return lista de tipos de processo
	 * 
	 * @author msapaula
	 */
	public List consultarTiposProcesso(int id_ServentiaSubtipo) throws Exception {

		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT ID_PROC_TIPO,PROC_TIPO FROM VIEW_SERV_SUBTIPO_PROC_TIPO";
		Sql += " WHERE ID_SERV_SUBTIPO = ?";
		ps.adicionarLong(id_ServentiaSubtipo);
		Sql += " ORDER BY PROC_TIPO";
		try{
			rs1 = consultar(Sql, ps);

			while (rs1.next()) {
				ProcessoTipoDt obTemp = new ProcessoTipoDt();
				obTemp.setId(rs1.getString("ID_PROC_TIPO"));
				obTemp.setProcessoTipo(rs1.getString("PROC_TIPO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return liTemp;
	}
	
	public List consultarProcessoTipoServentiaSubtipoGeral(String id_serventiasubtipo ) throws Exception {

        String Sql;
        List liTemp = new ArrayList();
        ResultSetTJGO rs1 = null;
        PreparedStatementTJGO ps = new PreparedStatementTJGO();
        ////System.out.println("..ps-consultarProcessoTipoServentiaSubtipoGeralServentiaSubtipoProcessoTipo()");

        Sql= "SELECT t2.ID_SERV_SUBTIPO_PROC_TIPO, t1.ID_PROC_TIPO, t1.PROC_TIPO, t1.ID_CNJ_CLASSE, t3.ID_SERV_SUBTIPO, t3.SERV_SUBTIPO";
        Sql+= " FROM PROJUDI.PROC_TIPO t1 ";
        Sql+= " LEFT JOIN PROJUDI.SERV_SUBTIPO_PROC_TIPO t2 ON t1.ID_PROC_TIPO = t2.ID_PROC_TIPO AND t2.ID_SERV_SUBTIPO = ?";
        ps.adicionarLong(id_serventiasubtipo);
        Sql+= " LEFT JOIN PROJUDI.SERV_SUBTIPO t3 ON t3.ID_SERV_SUBTIPO = t2.ID_SERV_SUBTIPO";
        Sql+= " WHERE (t1.CODIGO_TEMP<> ? OR t1.CODIGO_TEMP IS NULL) ";													ps.adicionarLong(-100);
        		//Adicionando ID_CNJ_CLASSE IS NOT NULL para trazer somente os PROC_TIPO que contém esse atributo
        Sql+= " AND t1.ATIVO = ? ";																						ps.adicionarLong(1);
        
        Sql+= " ORDER BY t1.PROC_TIPO";
        try{
            ////System.out.println("..ps-consultarProcessoTipoServentiaSubtipoGeralServentiaSubtipoProcessoTipo  " + Sql);

            rs1 = consultar(Sql,ps);
            ////System.out.println("....Execução Query OK"  );

            while (rs1.next()) {
                ServentiaSubtipoProcessoTipoDt obTemp = new ServentiaSubtipoProcessoTipoDt();
                obTemp.setId(rs1.getString("ID_SERV_SUBTIPO_PROC_TIPO"));
                obTemp.setServentiaSubtipo(rs1.getString("SERV_SUBTIPO"));
                obTemp.setId_ProcessoTipo (rs1.getString("ID_PROC_TIPO"));
                obTemp.setIdCnjClasse(rs1.getString("ID_CNJ_CLASSE"));
                obTemp.setProcessoTipo(rs1.getString("PROC_TIPO"));
                obTemp.setId_ServentiaSubtipo (id_serventiasubtipo);
                obTemp.setServentiaSubtipo(rs1.getString("SERV_SUBTIPO"));
                liTemp.add(obTemp);
            }
            //rs1.close();
            ////System.out.println("..ServentiaSubtipoProcessoTipoPsGen.consultarProcessoTipoServentiaSubtipoGeral() Operação realizada com sucesso");
        }finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }
        return liTemp; 
    }
	
	/**
	 * Método que verifica se um existe um determinado processo tipo para uma serventia subtipo
	 * 
	 * @param id_ServentiaSubTipo
	 *            , id do subtipo da Serventia
	 * @param id_ProcessoTipo
	 *            , id do processo tipo
	 * @return boolean            
	 * 
	 * @author lsbernardes
	 */	
	public boolean isProcessoTipoValido(String id_ServentiaSubTipo, String id_ProcessoTipo ) throws Exception {

        String Sql;
        boolean retorno = false;
        ResultSetTJGO rs1 = null;
        PreparedStatementTJGO ps = new PreparedStatementTJGO();

        Sql= "SELECT sspt.ID_SERV_SUBTIPO_PROC_TIPO ";
        Sql+= " FROM PROJUDI.SERV_SUBTIPO_PROC_TIPO sspt ";
        Sql+= " WHERE sspt.ID_SERV_SUBTIPO = ? and sspt.ID_PROC_TIPO = ? "; 
        ps.adicionarLong(id_ServentiaSubTipo); ps.adicionarLong(id_ProcessoTipo);
        try{
            rs1 = consultar(Sql,ps);

            if (rs1.next()) {
            	retorno = true;
            }
        }finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }
        return retorno; 
    }
	
	public String consultarProcessoTiposJSON(String id_AreaDistribuicao, String descricao, String posicao) throws Exception {

		String Sql, SqlFrom, SqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;

		Sql = "SELECT ss.ID_PROC_TIPO AS ID, ss.PROC_TIPO AS DESCRICAO1, ss.PROC_TIPO_CODIGO AS DESCRICAO2";
		SqlFrom = " FROM PROJUDI.VIEW_SERV_SUBTIPO_PROC_TIPO ss LEFT JOIN PROJUDI.AREA_DIST a on a.ID_SERV_SUBTIPO=ss.ID_SERV_SUBTIPO ";
		SqlFrom += " WHERE ss.PROC_TIPO like ?";												ps.adicionarString("%" + descricao +"%");
		if ((id_AreaDistribuicao != null) && (id_AreaDistribuicao.length() > 0)) {
			SqlFrom += " AND a.ID_AREA_DIST = ?";												ps.adicionarLong(id_AreaDistribuicao);
		}
		SqlOrder = " ORDER BY PROC_TIPO";

		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder,ps,posicao);
			Sql = "SELECT COUNT(*) AS QUANTIDADE ";
			rs2 = consultar(Sql + SqlFrom ,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return stTemp;
	}
	
	public String consultarProcessoTiposPublicosJSON(String id_AreaDistribuicao, String descricao, String posicao) throws Exception {

		String Sql, SqlFrom, SqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;

		Sql = "SELECT ss.ID_PROC_TIPO AS ID, ss.PROC_TIPO AS DESCRICAO1, ss.PROC_TIPO_CODIGO AS DESCRICAO2";
		SqlFrom = " FROM PROJUDI.VIEW_SERV_SUBTIPO_PROC_TIPO ss LEFT JOIN PROJUDI.AREA_DIST a on a.ID_SERV_SUBTIPO=ss.ID_SERV_SUBTIPO ";
		SqlFrom += " WHERE ss.PROC_TIPO like ? AND ss.PUBLICO = ? ";						ps.adicionarString("%" + descricao +"%");
		ps.adicionarBoolean(true);
		if (id_AreaDistribuicao!=null & id_AreaDistribuicao.length() > 0) {
			SqlFrom += " AND a.ID_AREA_DIST = ?";											ps.adicionarLong(id_AreaDistribuicao);
		}
		SqlOrder = " ORDER BY PROC_TIPO";

		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder,ps,posicao);
			Sql = "SELECT COUNT(*) AS QUANTIDADE ";
			rs2 = consultar(Sql + SqlFrom ,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return stTemp;
	}
}
