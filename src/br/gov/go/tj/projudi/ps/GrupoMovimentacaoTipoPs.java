package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.GrupoMovimentacaoTipoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoTipoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class GrupoMovimentacaoTipoPs extends GrupoMovimentacaoTipoPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = -3778345749399491600L;

    @SuppressWarnings("unused")
	private GrupoMovimentacaoTipoPs( ) {}
    
    public GrupoMovimentacaoTipoPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Consultar tipos de movimentação definidos para um determinado grupo
	 */
	public List consultarGrupoMovimentacaoTipo(String grupoCodigo, String descricao, String posicao) throws Exception {
		
		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "SELECT ID_MOVI_TIPO,MOVI_TIPO, ID_CNJ_MOVI ";
		SqlFrom = " FROM PROJUDI.VIEW_GRUPO_MOVI_TIPO";
		SqlFrom += " WHERE GRUPO_CODIGO = ?";
		ps.adicionarLong(grupoCodigo);
		SqlFrom += " AND MOVI_TIPO LIKE ?";
		ps.adicionarString( descricao +"%");
		SqlFrom += " AND ID_CNJ_MOVI is not null";
		SqlOrder = " ORDER BY MOVI_TIPO";
		
		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			while (rs1.next()) {
				MovimentacaoTipoDt obTemp = new MovimentacaoTipoDt();
				obTemp.setId(rs1.getString("ID_MOVI_TIPO"));
				obTemp.setMovimentacaoTipo(rs1.getString("MOVI_TIPO") + " (CNJ:" + rs1.getString("ID_CNJ_MOVI") + ")");
				liTemp.add(obTemp);
			}
			//rs1.close();
			Sql = "SELECT COUNT(*) AS QUANTIDADE";			
			rs2 = consultar(Sql + SqlFrom, ps);

			if (rs2.next()) 
				liTemp.add(rs2.getLong("QUANTIDADE"));
			
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp;
	}

	/**
	 * Consulta geral de Tipos de Movimentação por Grupo
	 */
	public List consultarDescricao(String descricao, String posicao) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "SELECT ID_GRUPO_MOVI_TIPO, MOVI_TIPO,GRUPO, ID_CNJ_MOVI";
		SqlFrom = " FROM PROJUDI.VIEW_GRUPO_MOVI_TIPO ";
		SqlFrom += " WHERE GRUPO LIKE ?";
		ps.adicionarString( descricao +"%");
		SqlFrom += " AND ID_CNJ_MOVI is not null";
		SqlOrder = " ORDER BY GRUPO, MOVI_TIPO ";
		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			while (rs1.next()) {
				GrupoMovimentacaoTipoDt obTemp = new GrupoMovimentacaoTipoDt();
				obTemp.setId(rs1.getString("ID_GRUPO_MOVI_TIPO"));
				obTemp.setMovimentacaoTipo(rs1.getString("MOVI_TIPO")+ " (CNJ:" + rs1.getString("ID_CNJ_MOVI") + ")");
				obTemp.setGrupo(rs1.getString("GRUPO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			Sql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom,ps);
			if (rs2.next()) 
				liTemp.add(rs2.getLong("QUANTIDADE"));
			
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp;
	}
	
    /*
     Este método foi criado com o intuito de retornar uma lista ordenada pelo atributo MovimentacaoTipo     
	 @Autor Márcio Gomes
	 @Data 27/08/2010
    */
	public List consultarMovimentacaoTipoGrupoGeral(String id_grupo ) throws Exception {

		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		Sql= "SELECT t2.ID_GRUPO_MOVI_TIPO, t1.ID_MOVI_TIPO, t1.MOVI_TIPO, t1.ID_CNJ_MOVI, t3.ID_GRUPO, t3.GRUPO";		
		Sql+= " FROM PROJUDI.MOVI_TIPO t1 ";
		Sql+= " LEFT JOIN PROJUDI.GRUPO_MOVI_TIPO t2 ON t1.ID_MOVI_TIPO = t2.ID_MOVI_TIPO AND t2.ID_GRUPO = ?";
		ps.adicionarLong(id_grupo);
		Sql+= " LEFT JOIN PROJUDI.GRUPO t3 ON t3.ID_GRUPO = t2.ID_GRUPO";
		Sql+= " WHERE ID_CNJ_MOVI is not null";
		Sql+= " ORDER BY t1.MOVI_TIPO";
		
		try{
			
			rs1 = consultar(Sql, ps);

			while (rs1.next()) {
				GrupoMovimentacaoTipoDt obTemp = new GrupoMovimentacaoTipoDt();
				obTemp.setId(rs1.getString("ID_GRUPO_MOVI_TIPO"));
				obTemp.setGrupo(rs1.getString("GRUPO"));
				obTemp.setId_MovimentacaoTipo (rs1.getString("ID_MOVI_TIPO"));
				obTemp.setMovimentacaoTipo(rs1.getString("MOVI_TIPO")+ " (CNJ:" + rs1.getString("ID_CNJ_MOVI") + ")");
				obTemp.setCodigoTemp(rs1.getString("ID_CNJ_MOVI"));
				obTemp.setId_Grupo (id_grupo);
				obTemp.setGrupo(rs1.getString("GRUPO"));
				liTemp.add(obTemp);
			}			
		}finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return liTemp; 
	}

	public String consultarGrupoMovimentacaoTipoJSON(String grupoCodigo, String descricao, String posicao) throws Exception {
		
		String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;
		
		Sql = "SELECT ID_MOVI_TIPO AS ID, MOVI_TIPO || ' (CNJ:' || ID_CNJ_MOVI || ')' AS DESCRICAO1";
		SqlFrom = " FROM PROJUDI.VIEW_GRUPO_MOVI_TIPO";
		SqlFrom += " WHERE GRUPO_CODIGO = ?";
		ps.adicionarLong(grupoCodigo);
		SqlFrom += " AND MOVI_TIPO LIKE ?";
		ps.adicionarString( descricao +"%");
		SqlFrom += " AND ID_CNJ_MOVI is not null";
		SqlOrder = " ORDER BY MOVI_TIPO";
		
		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			Sql = "SELECT COUNT(*) AS QUANTIDADE";
			
			rs2 = consultar(Sql + SqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
			
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return stTemp;
	}
	
	public String consultarGrupoMovimentacaoTipoSentencaJSON(String grupoCodigo, String descricao, String posicao) throws Exception {
		
		String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;
		
		Sql = "SELECT ID_MOVI_TIPO AS ID, MOVI_TIPO || ' (CNJ:' || ID_CNJ_MOVI || ')' AS DESCRICAO1";
		SqlFrom = " FROM PROJUDI.VIEW_GRUPO_MOVI_TIPO";
		SqlFrom += " WHERE GRUPO_CODIGO = ?";
		ps.adicionarLong(grupoCodigo);
		SqlFrom += " AND MOVI_TIPO LIKE ?";
		ps.adicionarString( descricao +"%");
		//SqlFrom += " AND ID_CNJ_MOVI is not null";
		SqlFrom += " AND ID_CNJ_MOVI in(select id_movi from cnj_movi connect by prior id_movi = id_movi_pai start with id_movi_pai = ? )";
		ps.adicionarLong(MovimentacaoTipoDt.MOVI_TIPO_CNJ_PAI_SENTENCA);
		SqlOrder = " ORDER BY MOVI_TIPO";
		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			Sql = "SELECT COUNT(*) AS QUANTIDADE";
			
			rs2 = consultar(Sql + SqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
			
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return stTemp;
	}

	
	
			
			
}
