package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.MandadoTipoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class MandadoTipoPs extends MandadoTipoPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = -3126881372638942252L;

    @SuppressWarnings("unused")
	private MandadoTipoPs( ) {}
    
    public MandadoTipoPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Consulta os tipos de mandados
	 * @return List<MandadoTipoDt>
	 * @throws Exception
	 */
	public List consultarTiposMandados() throws Exception {
		ResultSetTJGO rs1 = null;

		List listaTiposMandado = null;
		String Sql = "SELECT * FROM PROJUDI.VIEW_MAND_TIPO ORDER BY MAND_TIPO";
		
		try{
			rs1 = consultarSemParametros(Sql);
			
			MandadoTipoDt mandadoTipoDt;
			while( rs1.next() ) {
				if( listaTiposMandado == null ) 
					listaTiposMandado = new ArrayList();
				
				mandadoTipoDt = new MandadoTipoDt();
				
				mandadoTipoDt.setId(rs1.getString("ID_MAND_TIPO"));
				mandadoTipoDt.setMandadoTipo(rs1.getString("MAND_TIPO"));
				mandadoTipoDt.setMandadoTipoCodigo(rs1.getString("MAND_TIPO_CODIGO"));
				mandadoTipoDt.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
//				mandadoTipoDt.setQuantidadeLocomocao(rs1.getInt("QTDE_LOCOMOCAO"));
				
				listaTiposMandado.add(mandadoTipoDt);
			}
			
		
		} finally{
			 rs1.close();
		}
		return listaTiposMandado;
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {

		String stSql="";
		String stSqlFrom ="";
		String stSqlOrder ="";
		String stTemp = "";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;
		
		stSql= "SELECT ID_MAND_TIPO AS ID, MAND_TIPO AS DESCRICAO1";
		stSqlFrom = " FROM PROJUDI.VIEW_MAND_TIPO";
		stSqlFrom += " WHERE MAND_TIPO LIKE ?";
		stSqlOrder = " ORDER BY MAND_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{

			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);
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
