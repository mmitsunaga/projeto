package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.GrupoPendenciaTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class GrupoPendenciaTipoPs extends GrupoPendenciaTipoPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = -5493318553863534537L;

    @SuppressWarnings("unused")
	private GrupoPendenciaTipoPs( ) {}
    
    public GrupoPendenciaTipoPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Consultar os tipos de pendência definidos para um determinado grupo de usuários
	 * 
	 * @param descricao, filtro para grupo
	 * @param grupoCodigo, código do grupo para filtrar as pendências que esse pode utilizar
	 * @param posicao, parametro para paginação
	 * @param limite, booleano que define se consulta deve ser limitada
	 * 
	 * 
	 * @author msapaula
	 */
	public List consultarGrupoPendenciaTipo(String descricao, String grupoCodigo, String posicao, boolean limite) throws Exception {

		String Sql;
		String sqlComum;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "SELECT g.ID_PEND_TIPO,g.PEND_TIPO, g.PEND_TIPO_CODIGO ";
		sqlComum = "FROM PROJUDI.VIEW_GRUPO_PEND_TIPO g ";
		sqlComum += " WHERE g.GRUPO_CODIGO = ?";
		ps.adicionarLong(grupoCodigo);
		sqlComum += " AND g.PEND_TIPO LIKE ?";
		ps.adicionarString( descricao +"%");
		Sql += sqlComum + " ORDER BY g.PEND_TIPO";
		
		try{
			rs1 = limite ? consultarPaginacao(Sql,ps,posicao) : consultar(Sql,ps);

			while (rs1.next()) {
				PendenciaTipoDt obTemp = new PendenciaTipoDt();
				obTemp.setId(rs1.getString("ID_PEND_TIPO"));
				obTemp.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				obTemp.setPendenciaTipoCodigo(rs1.getString("PEND_TIPO_CODIGO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			Sql = "SELECT COUNT(*) as QUANTIDADE " + sqlComum;
			rs2 = consultar(Sql,ps);
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
     Este método foi criado com o intuito de retornar uma lista ordenada pelo atributo PendenciaTipo     
	 @Autor Márcio Gomes
	 @Data 27/08/2010
    */
	public List consultarPendenciaTipoGrupoGeral(String id_grupo ) throws Exception {

		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql= "SELECT t2.ID_GRUPO_PEND_TIPO, t1.ID_PEND_TIPO, t1.PEND_TIPO, t3.ID_GRUPO, t3.GRUPO";
		Sql+= " FROM PROJUDI.PEND_TIPO t1 ";
		Sql+= " LEFT JOIN PROJUDI.GRUPO_PEND_TIPO t2 ON t1.ID_PEND_TIPO = t2.ID_PEND_TIPO AND t2.ID_GRUPO = ?";
		ps.adicionarLong(id_grupo);
		Sql+= " LEFT JOIN PROJUDI.GRUPO t3 ON t3.ID_GRUPO = t2.ID_GRUPO";
		Sql+= " ORDER BY t1.PEND_TIPO";
		
		
		try{
		
			rs1 = consultar(Sql,ps);

			while (rs1.next()) {
				GrupoPendenciaTipoDt obTemp = new GrupoPendenciaTipoDt();
				obTemp.setId(rs1.getString("ID_GRUPO_PEND_TIPO"));
				obTemp.setGrupo(rs1.getString("GRUPO"));
				obTemp.setId_PendenciaTipo (rs1.getString("ID_PEND_TIPO"));
				obTemp.setPendenciaTipo(rs1.getString("PEND_TIPO"));
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
