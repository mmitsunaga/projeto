package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;
import br.gov.go.tj.projudi.dt.AreaDt;
import java.sql.Connection;
import br.gov.go.tj.projudi.dt.DescritorComboDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ProjudiConstantes;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class AreaPs extends AreaPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = 8213463609158008112L;
    
    public AreaPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Consultar objeto AreaDt equivalente ao código passado 
	 */
	public AreaDt consultarAreaCodigo(int areaCodigo) throws Exception {

		String Sql;
		AreaDt Dados = new AreaDt();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		Sql = "SELECT a.ID_AREA, a.AREA, a.AREA_CODIGO, a.CODIGO_TEMP FROM PROJUDI.VIEW_AREA a WHERE a.AREA_CODIGO = ?";
		ps.adicionarLong(areaCodigo);

		try{
			 rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				Dados.setId(rs1.getString("ID_AREA"));
				Dados.setArea(rs1.getString("AREA"));
				Dados.setAreaCodigo(rs1.getString("AREA_CODIGO"));
				Dados.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
			}
			//rs1.close();
		
	      }finally{
	             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
	        }
		return Dados;
	}
	
	//---------------------------------------------------------
		
	public String consultarDescricaoJSON(String descricao, String posicao, String ordenacao, String quantidadeRegistros ) throws Exception {
			
			if (ORDENACAO_PADRAO.equals(ordenacao)) //ordenação PROJUDI
				ordenacao = " AREA ";

			String stSql="";
			String stSqlFrom ="";
			String stSqlOrder ="";
			String stTemp = "";
			ResultSetTJGO rs1=null;
			ResultSetTJGO rs2=null;
			PreparedStatementTJGO ps = new PreparedStatementTJGO();
			int qtdeColunas = 1;

			stSql= "SELECT ID_AREA as id , AREA as descricao1";
			stSqlFrom = " FROM PROJUDI.VIEW_AREA";
			stSqlFrom += " WHERE AREA LIKE ?";
			stSqlOrder = " ORDER BY "+ordenacao;
			ps.adicionarString("%"+descricao+"%"); 

			try{

				rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao, quantidadeRegistros);
				stSql= "SELECT COUNT(*) as QUANTIDADE";
				rs2 = consultar(stSql + stSqlFrom,ps);
				rs2.next();
				stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
				
			}finally{
					try{if (rs1 != null) rs1.close();} catch(Exception e) {}
					try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return stTemp; 
		}
	
	public List<DescritorComboDt> consultarDescricaoCombo(String descricao) throws Exception {
		
		String Sql;
		List<DescritorComboDt> listaTemp = new ArrayList<DescritorComboDt>();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();		
		String valorFiltro = super.obtenhaValorFiltroConsultarDescricaoCombo(descricao);
		
		Sql = "SELECT * FROM PROJUDI.AREA";
		if (valorFiltro != null) {
			Sql += " WHERE AREA LIKE ?"; 
			ps.adicionarString(valorFiltro);	
		}		 
		Sql += " ORDER BY AREA";		
		
		try {
			rs1 = consultar(Sql, ps);

			while (rs1.next() && listaTemp.size() <= ProjudiConstantes.QUANTIDADE_OBJETOS_COMBO) {
				DescritorComboDt obTemp = new DescritorComboDt();
				obTemp.setId(rs1.getString("ID_AREA"));
				obTemp.setDescricao(rs1.getString("AREA"));				
				listaTemp.add(obTemp);
			}			
		} finally{
             try {if (rs1 != null) rs1.close();} catch (Exception e1) {}             
        }
		return listaTemp;
			
	}
	
}
