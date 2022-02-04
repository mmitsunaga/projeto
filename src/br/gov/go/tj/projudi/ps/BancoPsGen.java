package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.BancoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class BancoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 4137749082328601928L;

	//---------------------------------------------------------
	public BancoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(BancoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psBancoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.Banco ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getBancoCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "BANCO_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getBancoCodigo());  

			stVirgula=",";
		}
		if ((dados.getBanco().length()>0)) {
			 stSqlCampos+=   stVirgula + "BANCO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getBanco());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_BANCO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(BancoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psBancoalterar()");

		stSql= "UPDATE PROJUDI.Banco SET  ";
		stSql+= "BANCO_CODIGO = ?";		 ps.adicionarLong(dados.getBancoCodigo());  

		stSql+= ",BANCO = ?";		 ps.adicionarString(dados.getBanco());  

		stSql += " WHERE ID_BANCO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psBancoexcluir()");

		stSql= "DELETE FROM PROJUDI.Banco";
		stSql += " WHERE ID_BANCO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public BancoDt consultarId(String id_banco )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		BancoDt Dados=null;
		////System.out.println("....ps-ConsultaId_Banco)");

		stSql= "SELECT * FROM PROJUDI.VIEW_BANCO WHERE ID_BANCO = ?";		ps.adicionarLong(id_banco); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_Banco  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new BancoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( BancoDt Dados, ResultSetTJGO rs )  throws Exception {
		
		Dados.setId(rs.getString("ID_BANCO"));
		Dados.setBanco(rs.getString("BANCO"));
		Dados.setBancoCodigo( rs.getString("BANCO_CODIGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoBanco()");

		stSql= "SELECT ID_BANCO, BANCO FROM PROJUDI.VIEW_BANCO WHERE BANCO LIKE ?";
		stSql+= " ORDER BY BANCO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoBanco  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				BancoDt obTemp = new BancoDt();
				obTemp.setId(rs1.getString("ID_BANCO"));
				obTemp.setBanco(rs1.getString("BANCO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_BANCO WHERE BANCO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..BancoPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
