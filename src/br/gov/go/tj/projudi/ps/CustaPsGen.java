package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.CustaDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class CustaPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 5643177961573678187L;

	//---------------------------------------------------------
	public CustaPsGen() {

	}



//---------------------------------------------------------
	public void inserir(CustaDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psCustainserir()");
		stSqlCampos= "INSERT INTO projudi.CUSTA ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getCusta().length()>0)) {
			 stSqlCampos+=   stVirgula + "CUSTA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getCusta());  

			stVirgula=",";
		}
		if ((dados.getCustaCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "CUSTA_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getCustaCodigo());  

			stVirgula=",";
		}
		if ((dados.getCodigoRegimento().length()>0)) {
			 stSqlCampos+=   stVirgula + "CODIGO_REGIMENTO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getCodigoRegimento());  

			stVirgula=",";
		}
		if ((dados.getCodigoRegimentoValor().length()>0)) {
			 stSqlCampos+=   stVirgula + "CODIGO_REGIMENTO_VALOR " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getCodigoRegimentoValor());  

			stVirgula=",";
		}
		if ((dados.getPorcentagem().length()>0)) {
			 stSqlCampos+=   stVirgula + "PORCENTAGEM " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getPorcentagem());  

			stVirgula=",";
		}
		if ((dados.getMinimo().length()>0)) {
			 stSqlCampos+=   stVirgula + "MINIMO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getMinimo());  

			stVirgula=",";
		}
		if ((dados.getId_ArrecadacaoCusta().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_ARRECADACAO_CUSTA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ArrecadacaoCusta());  

			stVirgula=",";
		}
		if ((dados.getId_ArrecadacaoCustaGenerica().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_ARRECADACAO_CUSTA_GENERICA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ArrecadacaoCustaGenerica());  

			stVirgula=",";
		}
		if ((dados.getValorAcrescimo().length()>0)) {
			 stSqlCampos+=   stVirgula + "VALOR_ACRESCIMO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getValorAcrescimo());  

			stVirgula=",";
		}
		if ((dados.getValorMaximo().length()>0)) {
			 stSqlCampos+=   stVirgula + "VALOR_MAXIMO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getValorMaximo());  

			stVirgula=",";
		}
		if ((dados.getReferenciaCalculo().length()>0)) {
			 stSqlCampos+=   stVirgula + "REFERENCIA_CALCULO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getReferenciaCalculo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_CUSTA",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(CustaDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psCustaalterar()");

		stSql= "UPDATE projudi.CUSTA SET  ";
		stSql+= "CUSTA = ?";		 ps.adicionarString(dados.getCusta());  

		stSql+= ",CUSTA_CODIGO = ?";		 ps.adicionarLong(dados.getCustaCodigo());  

		stSql+= ",CODIGO_REGIMENTO = ?";		 ps.adicionarString(dados.getCodigoRegimento());  

		stSql+= ",CODIGO_REGIMENTO_VALOR = ?";		 ps.adicionarString(dados.getCodigoRegimentoValor());  

		stSql+= ",PORCENTAGEM = ?";		 ps.adicionarString(dados.getPorcentagem());  

		stSql+= ",MINIMO = ?";		 ps.adicionarLong(dados.getMinimo());  

		stSql+= ",ID_ARRECADACAO_CUSTA = ?";		 ps.adicionarLong(dados.getId_ArrecadacaoCusta());  

		stSql+= ",VALOR_ACRESCIMO = ?";		 ps.adicionarString(dados.getValorAcrescimo());  

		stSql+= ",VALOR_MAXIMO = ?";		 ps.adicionarString(dados.getValorMaximo());  
		
		stSql+= ",REFERENCIA_CALCULO = ?";		 ps.adicionarString(dados.getReferenciaCalculo());  

		stSql += " WHERE ID_CUSTA  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psCustaexcluir()");

		stSql= "DELETE FROM projudi.CUSTA";
		stSql += " WHERE ID_CUSTA = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public CustaDt consultarId(String id_custa )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		CustaDt Dados=null;
		////System.out.println("....ps-ConsultaId_Custa)");

		stSql= "SELECT * FROM projudi.VIEW_CUSTA WHERE ID_CUSTA = ?";		ps.adicionarLong(id_custa); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_Custa  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new CustaDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( CustaDt Dados, ResultSetTJGO rs )  throws Exception {

		
		Dados.setId(rs.getString("ID_CUSTA"));
		Dados.setCusta(rs.getString("CUSTA"));
		Dados.setCustaCodigo( rs.getString("CUSTA_CODIGO"));
		Dados.setCodigoRegimento( rs.getString("CODIGO_REGIMENTO"));
		Dados.setCodigoRegimentoValor( rs.getString("CODIGO_REGIMENTO_VALOR"));
		Dados.setPorcentagem( rs.getString("PORCENTAGEM"));
		Dados.setMinimo( rs.getString("MINIMO"));
		Dados.setId_ArrecadacaoCusta( rs.getString("ID_ARRECADACAO_CUSTA"));
		Dados.setId_ArrecadacaoCustaGenerica( rs.getString("ID_ARRECADACAO_CUSTA_GENERICA"));
		Dados.setArrecadacaoCusta( rs.getString("ARRECADACAO_CUSTA"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		Dados.setReferenciaCalculo( rs.getString("REFERENCIA_CALCULO") );
		
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoCusta()");

		stSql= "SELECT ID_CUSTA, CUSTA FROM projudi.VIEW_CUSTA WHERE CUSTA LIKE ?";
		stSql+= " ORDER BY CUSTA ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoCusta  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				CustaDt obTemp = new CustaDt();
				obTemp.setId(rs1.getString("ID_CUSTA"));
				obTemp.setCusta(rs1.getString("CUSTA"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM projudi.VIEW_CUSTA WHERE CUSTA LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..CustaPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
