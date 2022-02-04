package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.PalavraDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class PalavraPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 1833920218153606666L;

	//---------------------------------------------------------
	public PalavraPsGen() {

	}



//---------------------------------------------------------
	public void inserir(PalavraDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psPalavrainserir()");
		stSqlCampos= "INSERT INTO PROJUDI.Palavra ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getPalavra().length()>0)) {
			 stSqlCampos+=   stVirgula + "PALAVRA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getPalavra());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_PALAVRA",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(PalavraDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psPalavraalterar()");

		stSql= "UPDATE PROJUDI.Palavra SET  ";
		stSql+= "PALAVRA = ?";		 ps.adicionarString(dados.getPalavra());  

		stSql += " WHERE ID_PALAVRA  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psPalavraexcluir()");

		stSql= "DELETE FROM PROJUDI.Palavra";
		stSql += " WHERE ID_PALAVRA = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public PalavraDt consultarId(String id_palavra )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		PalavraDt Dados=null;
		////System.out.println("....ps-ConsultaId_Palavra)");

		stSql= "SELECT * FROM PROJUDI.VIEW_PALAVRA WHERE ID_PALAVRA = ?";		ps.adicionarLong(id_palavra); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_Palavra  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new PalavraDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		} finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( PalavraDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_PALAVRA"));
		Dados.setPalavra(rs.getString("PALAVRA"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));		
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoPalavra()");

		stSql= "SELECT ID_PALAVRA, PALAVRA FROM PROJUDI.VIEW_PALAVRA WHERE PALAVRA LIKE ?";
		stSql+= " ORDER BY PALAVRA ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoPalavra  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				PalavraDt obTemp = new PalavraDt();
				obTemp.setId(rs1.getString("ID_PALAVRA"));
				obTemp.setPalavra(rs1.getString("PALAVRA"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PALAVRA WHERE PALAVRA LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..PalavraPsGen.consultarDescricao() Operação realizada com sucesso");
		} finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
