package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.CustaValorDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class CustaValorPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -6501244166779245151L;

	//---------------------------------------------------------
	public CustaValorPsGen() {

	}



//---------------------------------------------------------
	public void inserir(CustaValorDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psCustaValorinserir()");
		stSqlCampos= "INSERT INTO projudi.CUSTA_VALOR ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getCustaValor().length()>0)) {
			 stSqlCampos+=   stVirgula + "CUSTA_VALOR " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getCustaValor());  

			stVirgula=",";
		}
		if ((dados.getCustaValorCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "CUSTA_VALOR_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getCustaValorCodigo());  

			stVirgula=",";
		}
		if ((dados.getLimiteMin().length()>0)) {
			 stSqlCampos+=   stVirgula + "LIMITE_MIN " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getLimiteMin());  

			stVirgula=",";
		}
		if ((dados.getLimiteMax().length()>0)) {
			 stSqlCampos+=   stVirgula + "LIMITE_MAX " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getLimiteMax());  

			stVirgula=",";
		}
		if ((dados.getValorCusta().length()>0)) {
			 stSqlCampos+=   stVirgula + "VALOR_CUSTA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getValorCusta());  

			stVirgula=",";
		}
		if ((dados.getId_Custa().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_CUSTA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Custa());  

			stVirgula=",";
		}
		if ((dados.getCodigoRegimento().length()>0)) {
			 stSqlCampos+=   stVirgula + "CODIGO_REGIMENTO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getCodigoRegimento());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_CUSTA_VALOR",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(CustaValorDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psCustaValoralterar()");

		stSql= "UPDATE projudi.CUSTA_VALOR SET  ";
		stSql+= "CUSTA_VALOR = ?";		 ps.adicionarString(dados.getCustaValor());  

		stSql+= ",CUSTA_VALOR_CODIGO = ?";		 ps.adicionarLong(dados.getCustaValorCodigo());  

		stSql+= ",LIMITE_MIN = ?";		 ps.adicionarString(dados.getLimiteMin());  

		stSql+= ",LIMITE_MAX = ?";		 ps.adicionarString(dados.getLimiteMax());  

		stSql+= ",VALOR_CUSTA = ?";		 ps.adicionarString(dados.getValorCusta());  

		stSql+= ",ID_CUSTA = ?";		 ps.adicionarLong(dados.getId_Custa());  

		stSql+= ",CODIGO_REGIMENTO = ?";		 ps.adicionarString(dados.getCodigoRegimento());  

		stSql += " WHERE ID_CUSTA_VALOR  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psCustaValorexcluir()");

		stSql= "DELETE FROM projudi.CUSTA_VALOR";
		stSql += " WHERE ID_CUSTA_VALOR = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public CustaValorDt consultarId(String id_custavalor )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		CustaValorDt Dados=null;
		////System.out.println("....ps-ConsultaId_CustaValor)");

		stSql= "SELECT * FROM projudi.VIEW_CUSTA_VALOR WHERE ID_CUSTA_VALOR = ?";		ps.adicionarLong(id_custavalor); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_CustaValor  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new CustaValorDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( CustaValorDt Dados, ResultSetTJGO rs )  throws Exception {
		
		Dados.setId(rs.getString("ID_CUSTA_VALOR"));
		Dados.setCustaValor(rs.getString("CUSTA_VALOR"));
		Dados.setCustaValorCodigo( rs.getString("CUSTA_VALOR_CODIGO"));
		Dados.setLimiteMin( rs.getString("LIMITE_MIN"));
		Dados.setLimiteMax( rs.getString("LIMITE_MAX"));
		Dados.setValorCusta( rs.getString("VALOR_CUSTA"));
		Dados.setId_Custa( rs.getString("ID_CUSTA"));
		Dados.setCusta( rs.getString("CUSTA"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));

	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoCustaValor()");

		stSql= "SELECT ID_CUSTA_VALOR, CUSTA_VALOR FROM projudi.VIEW_CUSTA_VALOR WHERE CUSTA_VALOR LIKE ?";
		stSql+= " ORDER BY CUSTA_VALOR ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoCustaValor  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				CustaValorDt obTemp = new CustaValorDt();
				obTemp.setId(rs1.getString("ID_CUSTA_VALOR"));
				obTemp.setCustaValor(rs1.getString("CUSTA_VALOR"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE FROM projudi.VIEW_CUSTA_VALOR WHERE CUSTA_VALOR LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..CustaValorPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
