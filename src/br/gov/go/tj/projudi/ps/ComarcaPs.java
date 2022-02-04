package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import java.sql.Connection;
import br.gov.go.tj.projudi.dt.DescritorComboDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ProjudiConstantes;
import br.gov.go.tj.utils.ResultSetTJGO;

public class ComarcaPs extends ComarcaPsGen {

    private static final long serialVersionUID = -400134851751064438L;

    public ComarcaPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Sobrescrevendo método para retornar também o Código da comarca
	 */
	public List consultarDescricao(String descricao, String posicao) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT *";
		SqlFrom = " FROM PROJUDI.VIEW_COMARCA";
		SqlFrom += " WHERE COMARCA LIKE ?";
		SqlOrder = " ORDER BY COMARCA";
		ps.adicionarString("%"+ descricao +"%");
		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);

			while (rs1.next()) {
				ComarcaDt obTemp = new ComarcaDt();
				obTemp.setId(rs1.getString("ID_COMARCA"));
				obTemp.setComarca(rs1.getString("COMARCA"));
				obTemp.setComarcaCodigo(rs1.getString("COMARCA_CODIGO"));
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
	
	public String consultarNomeCodigo(String comarcaCodigo)  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ComarcaDt Dados=null;

		stSql= "SELECT * FROM PROJUDI.VIEW_COMARCA WHERE COMARCA_CODIGO = ?";		
		ps.adicionarLong(comarcaCodigo); 


		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ComarcaDt();
				associarDt(Dados, rs1);
			}
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados != null ? Dados.getComarca() : ""; 
	}
	
	public ComarcaDt consultarComarcaCodigo(String comarcaCodigo)  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ComarcaDt Dados=null;

		stSql= "SELECT * FROM PROJUDI.VIEW_COMARCA WHERE COMARCA_CODIGO = ?";		
		ps.adicionarLong(comarcaCodigo); 


		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ComarcaDt();
				associarDt(Dados, rs1);
			}
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao, String ordenacao, String quantidadeRegistros) throws Exception {
		
		if (ORDENACAO_PADRAO.equals(ordenacao)) //ordenacao PROJUDI
			ordenacao = " COMARCA ";
		
		String stSql="";
		String stSqlFrom ="";
		String stSqlOrder ="";
		String stTemp="";
		int qtdeColunas = 1;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_COMARCA as id, COMARCA as descricao1";
		stSqlFrom = " FROM PROJUDI.VIEW_COMARCA";
		stSqlFrom += " WHERE COMARCA LIKE ?";
		stSqlOrder += " ORDER BY " + ordenacao;
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao, quantidadeRegistros);
			stSql= "SELECT Count(*) as Quantidade";
			rs2 = consultar(stSql + stSqlFrom,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp; 
	}
	
	/**
	 * @author jrcorrea
	 * @return List com as comarcas
	 * @throws Exception
	 * @data 10/09/2015 as 09:55
	 */
	public List listar() throws Exception {
		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT ID_COMARCA, COMARCA, COMARCA_CODIGO FROM PROJUDI.VIEW_COMARCA";
				
		try{
			rs1 = consultar(Sql, ps);

			while (rs1.next()) {
				ComarcaDt obTemp = new ComarcaDt();
				obTemp.setId(rs1.getString("ID_COMARCA"));
				obTemp.setComarca(rs1.getString("COMARCA"));
				obTemp.setComarcaCodigo(rs1.getString("COMARCA_CODIGO"));
				liTemp.add(obTemp);
			}
			//
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}             
        }
		return liTemp;
	}
	
	public List<DescritorComboDt> consultarDescricaoCombo(String descricao) throws Exception {
		
		String Sql;
		List<DescritorComboDt> listaTemp = new ArrayList<DescritorComboDt>();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String valorFiltro = super.obtenhaValorFiltroConsultarDescricaoCombo(descricao);
		
		Sql = "SELECT * FROM PROJUDI.COMARCA";
		if (valorFiltro != null) {
			Sql += " WHERE COMARCA LIKE ?";
			ps.adicionarString(valorFiltro);
		}
		Sql += " ORDER BY COMARCA";
		
		try {
			rs1 = consultar(Sql, ps);
			
			while (rs1.next() && listaTemp.size() <= ProjudiConstantes.QUANTIDADE_OBJETOS_COMBO) {
				DescritorComboDt obTemp = new DescritorComboDt();
				obTemp.setId(rs1.getString("ID_COMARCA"));
				obTemp.setDescricao(rs1.getString("COMARCA"));
				listaTemp.add(obTemp);
			}
		} finally {
			try {
				if (rs1 != null)
					rs1.close();
			} catch (Exception e1) {
			}
		}
		return listaTemp;
	}
}
