package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.EstatisticaProdutividadeItemDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class EstatisticaProdutividadeItemPs extends EstatisticaProdutividadeItemPsGen{

	/**
     * 
     */
    private static final long serialVersionUID = -5251303230396876008L;

    public EstatisticaProdutividadeItemPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Método que lista todos os registros da tabela EstatisticaProdutividadeItem a partir de uma descrição.
	 * Esta consulta não aplica limitação de tela para paginação.
	 * param descricao - descrição do registro (não obrigatório)
	 * return lista de Estatistica Produtividade Item
	 * throws Exception
	 * author hmgodinho
	 */
	public List listarEstatisticasProdutividadeItem(String descricao) throws Exception {
		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "SELECT ID_EST_PROD_ITEM, EST_PROD_ITEM FROM projudi.VIEW_EST_PROD_ITEM WHERE EST_PROD_ITEM LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		Sql += " ORDER BY EST_PROD_ITEM ";
		try{
			rs = consultar(Sql,ps);
			while (rs.next()) {
				EstatisticaProdutividadeItemDt obTemp = new EstatisticaProdutividadeItemDt();
				obTemp.setId(rs.getString("ID_EST_PROD_ITEM"));
				obTemp.setEstatisticaProdutividadeItem(rs.getString("EST_PROD_ITEM"));
				liTemp.add(obTemp);
			}
		
		} finally{			
			rs.close();						
		}
		return liTemp;
	}

	public List consultarEstatisticaProdutividadeItem(String descricao, String posicao) throws Exception {
		
		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		
		Sql = "SELECT  ID_EST_PROD_ITEM, EST_PROD_ITEM";
		SqlFrom = " FROM projudi.VIEW_EST_PROD_ITEM";
		SqlFrom += " WHERE EST_PROD_ITEM LIKE  ?";
		ps.adicionarString("%"+ descricao +"%");
		SqlOrder = " ORDER BY EST_PROD_ITEM ";
				try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
				while (rs1.next()) {
				EstatisticaProdutividadeItemDt obTemp = new EstatisticaProdutividadeItemDt();
				obTemp.setId(rs1.getString("ID_EST_PROD_ITEM"));
				obTemp.setEstatisticaProdutividadeItem(rs1.getString("EST_PROD_ITEM"));
				liTemp.add(obTemp);
			}
			Sql = "SELECT COUNT(*) AS QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));
					
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp;
	}
	
	/**
	 * Consulta EstatisticaProdutividadeItem (utilizando filtro por descricao).
	 * 
	 * @param descricao - descrição do item a ser buscado
	 * @param posicao - posição da página
	 * @return lista de itens
	 * @throws Exception
	 * @author hmgodinho
	 */
	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {
		
		String Sql="";
		String SqlFrom ="";
		String SqlOrder ="";
		String stTemp="";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;
				
		Sql = "SELECT  ID_EST_PROD_ITEM AS ID, EST_PROD_ITEM AS DESCRICAO1";
		SqlFrom = " FROM projudi.VIEW_EST_PROD_ITEM";
		SqlFrom += " WHERE EST_PROD_ITEM LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		SqlOrder = " ORDER BY EST_PROD_ITEM ";
		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			
			ps = new PreparedStatementTJGO();
			Sql = "SELECT COUNT(*) AS QUANTIDADE";  
			ps.adicionarString("%"+ descricao +"%");

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
