package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.PaisDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class PaisPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -5113987922521738344L;

	//---------------------------------------------------------
	public PaisPsGen() {

	}



//---------------------------------------------------------
	public void inserir(PaisDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psPaisinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.Pais ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getPais().length()>0)) {
			 stSqlCampos+=   stVirgula + "PAIS " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getPais());  

			stVirgula=",";
		}
		if ((dados.getPaisCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "PAIS_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getPaisCodigo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_PAIS",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(PaisDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psPaisalterar()");

		stSql= "UPDATE PROJUDI.Pais SET  ";
		stSql+= "PAIS = ?";		 ps.adicionarString(dados.getPais());  

		stSql+= ",PAIS_CODIGO = ?";		 ps.adicionarLong(dados.getPaisCodigo());  

		stSql += " WHERE ID_PAIS  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psPaisexcluir()");

		stSql= "DELETE FROM PROJUDI.Pais";
		stSql += " WHERE ID_PAIS = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public PaisDt consultarId(String id_pais )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		PaisDt Dados=null;
		////System.out.println("....ps-ConsultaId_Pais)");

		stSql= "SELECT * FROM PROJUDI.VIEW_PAIS WHERE ID_PAIS = ?";		ps.adicionarLong(id_pais); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_Pais  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new PaisDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( PaisDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_PAIS"));
		Dados.setPais(rs.getString("PAIS"));
		Dados.setPaisCodigo( rs.getString("PAIS_CODIGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoPais()");

		stSql= "SELECT ID_PAIS, PAIS FROM PROJUDI.VIEW_PAIS WHERE PAIS LIKE ?";
		stSql+= " ORDER BY PAIS ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoPais  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				PaisDt obTemp = new PaisDt();
				obTemp.setId(rs1.getString("ID_PAIS"));
				obTemp.setPais(rs1.getString("PAIS"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PAIS WHERE PAIS LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..PaisPsGen.consultarDescricao() Operação realizada com sucesso");
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

} 
