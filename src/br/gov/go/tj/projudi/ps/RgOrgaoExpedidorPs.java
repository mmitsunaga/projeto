package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.RgOrgaoExpedidorDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class RgOrgaoExpedidorPs extends RgOrgaoExpedidorPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = 7121352328991764414L;

    public RgOrgaoExpedidorPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Consulta geral de Órgãos Expedidores
	 */
	public List consultarDescricao(String descricao, String sigla, String posicao) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT ID_RG_ORGAO_EXP,RG_ORGAO_EXP,SIGLA, UF";
		SqlFrom = " FROM PROJUDI.VIEW_RGORGAO_EXP ";
		SqlFrom += " WHERE RG_ORGAO_EXP LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		SqlFrom += " AND SIGLA LIKE ?";
		ps.adicionarString("%"+ sigla +"%");
		SqlOrder = " ORDER BY RG_ORGAO_EXP";		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			while (rs1.next()) {
				RgOrgaoExpedidorDt obTemp = new RgOrgaoExpedidorDt();
				obTemp.setId(rs1.getString("ID_RG_ORGAO_EXP"));
				obTemp.setRgOrgaoExpedidor(rs1.getString("RG_ORGAO_EXP"));
				obTemp.setEstado(rs1.getString("UF"));
				obTemp.setSigla(rs1.getString("SIGLA"));
				obTemp.setSiglaCompleta(rs1.getString("SIGLA") + "-" + rs1.getString("UF"));
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

	/**
	 * Método responsável em buscar um Órgão Expedidor, baseado na descrição e Uf passados
	 * @throws Exception 
	 */
	public RgOrgaoExpedidorDt buscaOrgaoExpedidor(String descricao, String uf) throws Exception {

		RgOrgaoExpedidorDt expedidorDt = null;
		ResultSetTJGO rs1 = null;
		String sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		try{

			sql = "SELECT * FROM PROJUDI.VIEW_RGORGAO_EXP o WHERE o.RG_ORGAO_EXP = ?";
			sql += " AND o.UF = ?";			
			ps.adicionarString(descricao);
			ps.adicionarString(uf);

			rs1 = consultar(sql, ps);
			if (rs1.next()) {
				expedidorDt = new RgOrgaoExpedidorDt();
				expedidorDt.setId(rs1.getString("ID_RG_ORGAO_EXP"));
				expedidorDt.setRgOrgaoExpedidor(rs1.getString("RG_ORGAO_EXP"));
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return expedidorDt;
	}
	
	public String consultarDescricaoJSON(String sigla, String descricao, String posicao) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 3;

		Sql = "SELECT ID_RG_ORGAO_EXP AS ID, SIGLA AS DESCRICAO1, RG_ORGAO_EXP AS DESCRICAO2, UF AS DESCRICAO3";
		SqlFrom = " FROM PROJUDI.VIEW_RGORGAO_EXP ";
		SqlFrom += " WHERE RG_ORGAO_EXP LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		SqlFrom += " AND SIGLA LIKE ?";
		ps.adicionarString("%"+ sigla +"%");
		SqlOrder = " ORDER BY RG_ORGAO_EXP";
		
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
	
	public String consultarDescricaoSiglaJSON(String sigla, String descricao, String posicao) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 2;

		Sql = "SELECT ID_RG_ORGAO_EXP AS ID, SIGLA AS DESCRICAO1, UF AS DESCRICAO2";
		SqlFrom = " FROM PROJUDI.VIEW_RGORGAO_EXP ";
		SqlFrom += " WHERE RG_ORGAO_EXP LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		SqlFrom += " AND SIGLA LIKE ?";
		ps.adicionarString("%"+ sigla +"%");
		SqlOrder = " ORDER BY RG_ORGAO_EXP";
		
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
