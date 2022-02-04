package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.GrupoArquivoTipoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class GrupoArquivoTipoPs extends GrupoArquivoTipoPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = -7797249035273794540L;

    public GrupoArquivoTipoPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Consultar os tipos de arquivo definidos para um determinado grupo de usuários
	 */
	public List consultarGrupoArquivoTipo(String grupoCodigo, String descricao, String posicao) throws Exception {
		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT v.ID_ARQ_TIPO,v.ARQ_TIPO";
		SqlFrom = " FROM VIEW_GRUPO_ARQ_TIPO v ";
		SqlFrom += " WHERE v.GRUPO_CODIGO=?";
		ps.adicionarLong(grupoCodigo);
		SqlFrom += " AND v.ARQ_TIPO LIKE ?";
		ps.adicionarString( descricao +"%");		
		SqlOrder = " ORDER BY v.ARQ_TIPO";		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);

			while (rs1.next()) {
				ArquivoTipoDt obTemp = new ArquivoTipoDt();
				obTemp.setId(rs1.getString("ID_ARQ_TIPO"));
				obTemp.setArquivoTipo(rs1.getString("ARQ_TIPO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			Sql = "SELECT COUNT(*) AS QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));			
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp;
	}

	public String consultarGrupoArquivoTipoJSON(String grupoCodigo, String descricao, String posicao) throws Exception {
		String Sql, sqlFrom, sqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 1;

		Sql = "SELECT v.ID_ARQ_TIPO as id, v.ARQ_TIPO descricao1 ";
		sqlFrom = " FROM VIEW_GRUPO_ARQ_TIPO v  WHERE v.GRUPO_CODIGO=?";				ps.adicionarLong(grupoCodigo);
		sqlFrom += " AND v.ARQ_TIPO LIKE ?";											ps.adicionarString( "%" + descricao +"%");		
		sqlOrder = " ORDER BY v.ARQ_TIPO";		
		try{
			rs1 = consultarPaginacao(Sql + sqlFrom + sqlOrder, ps, posicao);

			Sql = "SELECT COUNT(*) AS QUANTIDADE ";
			rs2 = consultar(Sql + sqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
			
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return stTemp;
	}

	
	/**
	 * Consulta geral de Tipos de Arquivo por Grupo
	 */
	public List consultarDescricao(String descricao, String posicao) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT ID_GRUPO_ARQ_TIPO,ARQ_TIPO,GRUPO";
		SqlFrom = " FROM PROJUDI.VIEW_GRUPO_ARQ_TIPO";
		SqlFrom += " WHERE GRUPO LIKE ?";
		ps.adicionarString( descricao +"%");
		SqlOrder = " ORDER BY GRUPO";		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);

			while (rs1.next()) {
				GrupoArquivoTipoDt obTemp = new GrupoArquivoTipoDt();
				obTemp.setId(rs1.getString("ID_GRUPO_ARQ_TIPO"));
				obTemp.setArquivoTipo(rs1.getString("ARQ_TIPO"));
				obTemp.setGrupo(rs1.getString("GRUPO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			Sql = "SELECT COUNT(*) AS QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));			
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp;
	}

    /*
     Este método foi criado com o intuito de retornar uma lista ordenada pelo atributo ArquivoTipo     
	 @Autor Márcio Gomes
	 @Data 27/08/2010
    */
	
	public List consultarArquivoTipoGrupoGeral(String id_grupo ) throws Exception {

		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		Sql= "SELECT t2.ID_GRUPO_ARQ_TIPO, t1.ID_ARQ_TIPO, t1.ARQ_TIPO, t3.ID_GRUPO, t3.GRUPO";
		Sql+= " FROM PROJUDI.ARQ_TIPO t1 ";
		Sql+= " LEFT JOIN PROJUDI.GRUPO_ARQ_TIPO t2 ON t1.ID_ARQ_TIPO = t2.ID_ARQ_TIPO AND t2.ID_GRUPO = ?";
		Sql+= " LEFT JOIN PROJUDI.GRUPO t3 ON t3.ID_GRUPO = t2.ID_GRUPO";
		Sql+= " ORDER BY t1.ARQ_TIPO";
		ps.adicionarLong(id_grupo);
		try{

			rs1 = consultar(Sql, ps);
			
			while (rs1.next()) {
				GrupoArquivoTipoDt obTemp = new GrupoArquivoTipoDt();
				obTemp.setId(rs1.getString("ID_GRUPO_ARQ_TIPO"));
				obTemp.setGrupo(rs1.getString("GRUPO"));
				obTemp.setId_ArquivoTipo (rs1.getString("ID_ARQ_TIPO"));
				obTemp.setArquivoTipo(rs1.getString("ARQ_TIPO"));
				obTemp.setId_Grupo (id_grupo);
				obTemp.setGrupo(rs1.getString("GRUPO"));
				liTemp.add(obTemp);
			}			
		}finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return liTemp; 
	}	

}
