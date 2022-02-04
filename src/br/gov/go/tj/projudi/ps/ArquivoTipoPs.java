package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;

import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class ArquivoTipoPs extends ArquivoTipoPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = -30857562718073891L;

    public ArquivoTipoPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
     * Consultar descrição de um tipo de arquivo baseado no código passado
     * 
     * @param arquivoTipoCodigo:
     *            código do arquivo tipo desejado
     */
	public String consultarDescricaoArquivoTipo(String arquivoTipoCodigo) throws Exception {
		String retorno = "";
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		Sql = "SELECT a.ARQ_TIPO FROM VIEW_ARQ_TIPO a WHERE a.ARQ_TIPO_CODIGO=?";
		ps.adicionarLong(arquivoTipoCodigo);

		try{
			 rs1 = consultar(Sql,ps);
			if (rs1.next()) {
				retorno = rs1.getString("ARQ_TIPO");
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return retorno;
	}
	
	/**
     * Consultar id de um tipo de arquivo baseado no código passado
     * @param arquivoTipoCodigo: código do arquivo tipo desejado
     */
	public String consultarIdArquivoTipo(String arquivoTipoCodigo) throws Exception {
		String retorno = "";
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		Sql = "SELECT a.ID_ARQ_TIPO FROM PROJUDI.VIEW_ARQ_TIPO a WHERE a.ARQ_TIPO_CODIGO=?";
		ps.adicionarLong(arquivoTipoCodigo);

		try{
			 rs1 = consultar(Sql,ps);
			if (rs1.next()) {
				retorno = rs1.getString("ID_ARQ_TIPO");
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return retorno;
	}
	
	/**
	 * Consultar se o arquivoTipo é público através de um dado ArquivoTipoCodigo
	 * 
	 * @param arquivoTipoCodigo
	 * @return true
	 * @throws Exception
	 */
	public boolean consultarPublicoArquivoTipo(String arquivoTipoCodigo) throws Exception {
		boolean retorno = false;
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		Sql = "SELECT a.PUBLICO FROM VIEW_ARQ_TIPO a WHERE a.ARQ_TIPO_CODIGO=?";
		ps.adicionarLong(arquivoTipoCodigo);

		try{
			 rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				if (rs1.getString("PUBLICO") != null) {
					retorno = rs1.getString("PUBLICO").equalsIgnoreCase("1");
				} else {
					retorno = false;
				}

			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return retorno;
	}
	

	/**
	 * Consulta pelo codigo do aruqiov tipo e retorna o id
	 * @author Ronneesley Moura Teles
	 * @since 13/10/2008 11:02
	 * @param String arquivoTipoCodigo, codigo desejado
	 * @return List
	 * @throws Exception
	 */
	public List consultarPeloArquivoTipoCodigo(String arquivoTipoCodigo) throws Exception {
		List lista = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "SELECT a.ID_ARQ_TIPO FROM PROJUDI.ARQ_TIPO a WHERE a.ARQ_TIPO_CODIGO = ?";
		ps.adicionarLong(arquivoTipoCodigo);

		try{
			 rs1 = consultar(sql, ps);
			if (rs1.next()) {
				//Verifica se a lista esta vazia
				if (lista == null) lista = new ArrayList();
				
				lista.add( rs1.getString("ID_ARQ_TIPO") );
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return lista;
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {

		String stSql, stSqlFrom, stSqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;

		stSql= "SELECT ID_ARQ_TIPO AS ID, ARQ_TIPO AS DESCRICAO1 ";
		stSqlFrom = " FROM PROJUDI.VIEW_ARQ_TIPO";
		stSqlFrom += " WHERE ARQ_TIPO LIKE ?";
		ps.adicionarString("%"+descricao+"%");
		stSqlOrder = " ORDER BY ARQ_TIPO ";
		 
		try{
			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);

			stSql= "SELECT COUNT(*) as QUANTIDADE ";
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);		
			
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return stTemp; 
	}

	public boolean isPublico(String id_ArquivoTipo) throws Exception {
		boolean boRetorno = false;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "SELECT 1 FROM PROJUDI.ARQ_TIPO a WHERE a.publico = ? and  a.ID_ARQ_TIPO = ?";			ps.adicionarLong(1); 	ps.adicionarLong(id_ArquivoTipo);

		try{
			rs1 = consultar(sql, ps);
			if (rs1.next()) {
				boRetorno = true;
			}		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return boRetorno;
	}
	
	public List<ArquivoTipoDt> consultarTodos() throws Exception {

		String stSql="";
		List<ArquivoTipoDt> liTemp = new ArrayList<ArquivoTipoDt>();
		ResultSetTJGO rs1=null;
		
		stSql= "SELECT ID_ARQ_TIPO, ARQ_TIPO FROM PROJUDI.VIEW_ARQ_TIPO";
		stSql+= " ORDER BY ARQ_TIPO ";
		
		try{
			rs1 = consultarSemParametros(stSql);
			
			while (rs1.next()) {
				ArquivoTipoDt obTemp = new ArquivoTipoDt();
				obTemp.setId(rs1.getString("ID_ARQ_TIPO"));
				obTemp.setArquivoTipo(rs1.getString("ARQ_TIPO"));
				liTemp.add(obTemp);
			}			
		}finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}				
		}
		return liTemp; 
	}
}
