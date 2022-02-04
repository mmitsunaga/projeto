package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class PendenciaTipoPs extends PendenciaTipoPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = 952365466422099979L;

    @SuppressWarnings("unused")
	private PendenciaTipoPs( ) {}
    
    public PendenciaTipoPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Consultar objeto PendenciaTipoDt equivalente ao código passado  
	 */
	public PendenciaTipoDt consultarPendenciaTipoCodigo(int pendenciaTipoCodigo) throws Exception {

		String Sql;
		PendenciaTipoDt Dados = new PendenciaTipoDt();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT * FROM PROJUDI.VIEW_PEND_TIPO WHERE PEND_TIPO_CODIGO = ?";
		ps.adicionarLong(pendenciaTipoCodigo);

		try{
			rs1 = consultar(Sql,ps);
			if (rs1.next()) {
				Dados.setId(rs1.getString("ID_PEND_TIPO"));
				Dados.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				Dados.setPendenciaTipoCodigo(rs1.getString("PEND_TIPO_CODIGO"));
				Dados.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return Dados;
	}

	/**
	 * Consultar os tipos de pendência disponíveis na movimentação do processo
	 */
	public List consultarTiposPendenciaMovimentacao() throws Exception {

		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		

		Sql = "SELECT ID_PEND_TIPO, PEND_TIPO, PEND_TIPO_CODIGO FROM PROJUDI.VIEW_PEND_TIPO ORDER BY PEND_TIPO ";
		try{
			rs1 = consultarSemParametros(Sql);
			while (rs1.next()) {
				PendenciaTipoDt obTemp = new PendenciaTipoDt();
				obTemp.setId(rs1.getString("ID_PEND_TIPO"));
				obTemp.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				obTemp.setPendenciaTipoCodigo(rs1.getString("PEND_TIPO_CODIGO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return liTemp;
	}
	
	/**
	 * Método que contém a sql que irá buscar os tipos de pendência existentes pela descrição informada.
	 * 
	 * @return listaPendenciaTipo: lista contendo os tipos de pendências encontradas
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarPendenciaTipoDescricao(String nomeBusca) throws Exception {
		List listaPendenciaTipo = new ArrayList();
		String sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sql = "SELECT * FROM PROJUDI.VIEW_PEND_TIPO pt WHERE PEND_TIPO LIKE ? ORDER BY pt.PEND_TIPO";
			ps.adicionarString( nomeBusca +"%"); 

		try{
			rs1 = consultar(sql,ps);
			while (rs1.next()) {
				PendenciaTipoDt pendenciaTipoDt = new PendenciaTipoDt();
				pendenciaTipoDt.setId(rs1.getString("ID_PEND_TIPO"));
				pendenciaTipoDt.setPendenciaTipoCodigo(rs1.getString("PEND_TIPO_CODIGO"));
				pendenciaTipoDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				listaPendenciaTipo.add(pendenciaTipoDt);
			}
		
		} finally{
			try{
				if (rs1 != null)
					rs1.close();
			} catch(Exception e) {
			}
		}
		return listaPendenciaTipo;
	}
	
	public String consultarDescricaoJSON (String descricao, String posicao, String ordenacao, String quantidadeRegistros) throws Exception {
		
		if (ORDENACAO_PADRAO.equals(ordenacao)) //ordenacao PROJUDI
			ordenacao = " PEND_TIPO ";
			
		String stSql="";
		String stSqlFrom ="";
		String stSqlOrder ="";
		String stTemp = "";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;

		stSql= "SELECT ID_PEND_TIPO AS ID, PEND_TIPO AS DESCRICAO1";
		stSqlFrom = " FROM PROJUDI.VIEW_PEND_TIPO";
		stSqlFrom += " WHERE PEND_TIPO LIKE ?";
		stSqlOrder = " ORDER BY " + ordenacao;
		ps.adicionarString("%"+descricao+"%"); 

		try{

			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao, quantidadeRegistros);

			stSql= "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
			
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return stTemp; 
	}
}
