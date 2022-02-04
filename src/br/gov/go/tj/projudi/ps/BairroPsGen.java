package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class BairroPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 9030657500453086776L;

	//---------------------------------------------------------
	public BairroPsGen() {

	}



//---------------------------------------------------------
	public void inserir(BairroDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psBairroinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.BAIRRO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getBairro().length()>0)) {
			 stSqlCampos+=   stVirgula + "BAIRRO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getBairro());  

			stVirgula=",";
		}
		if ((dados.getBairroCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "BAIRRO_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getBairroCodigo());  

			stVirgula=",";
		}
		if ((dados.getCodigoSPG().length()>0)) {
			 stSqlCampos+=   stVirgula + "CODIGO_SPG " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getCodigoSPG());  

			stVirgula=",";
		}
		if ((dados.getId_Cidade().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_CIDADE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Cidade());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_BAIRRO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(BairroDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psBairroalterar()");

		stSql= "UPDATE PROJUDI.BAIRRO SET  ";
		stSql+= "BAIRRO = ?";		 ps.adicionarString(dados.getBairro());  

		stSql+= ",BAIRRO_CODIGO = ?";		 ps.adicionarLong(dados.getBairroCodigo());  

		stSql+= ",CODIGO_SPG = ?";		 ps.adicionarLong(dados.getCodigoSPG());  

		stSql+= ",ID_CIDADE = ?";		 ps.adicionarLong(dados.getId_Cidade());  

		stSql += " WHERE ID_BAIRRO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psBairroexcluir()");

		stSql= "DELETE FROM PROJUDI.BAIRRO";
		stSql += " WHERE ID_BAIRRO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public BairroDt consultarId(String id_bairro )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		BairroDt Dados=null;
		////System.out.println("....ps-ConsultaId_Bairro)");

		stSql= "SELECT * FROM PROJUDI.VIEW_BAIRRO WHERE ID_BAIRRO = ?";		ps.adicionarLong(id_bairro); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_Bairro  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new BairroDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( BairroDt Dados, ResultSetTJGO rs1 )  throws Exception {
		
		Dados.setId(rs1.getString("ID_BAIRRO"));
		Dados.setBairro(rs1.getString("BAIRRO"));
		Dados.setBairroCodigo( rs1.getString("BAIRRO_CODIGO"));
		Dados.setId_Cidade( rs1.getString("ID_CIDADE"));
		Dados.setCidade( rs1.getString("CIDADE"));
		Dados.setCodigoTemp( rs1.getString("CODIGO_TEMP"));
		Dados.setUf( rs1.getString("UF"));
		Dados.setCodigoSPG(rs1.getString("CODIGO_SPG"));
		
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoBairro()");

		stSql= "SELECT ID_BAIRRO, BAIRRO FROM PROJUDI.VIEW_BAIRRO WHERE BAIRRO LIKE ?";
		stSql+= " ORDER BY BAIRRO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoBairro  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				BairroDt obTemp = new BairroDt();
				obTemp.setId(rs1.getString("ID_BAIRRO"));
				obTemp.setBairro(rs1.getString("BAIRRO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_BAIRRO WHERE BAIRRO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..BairroPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
