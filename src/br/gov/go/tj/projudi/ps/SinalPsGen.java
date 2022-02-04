package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.SinalDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class SinalPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -6246559253264865264L;

	//---------------------------------------------------------
	public SinalPsGen() {

	}



//---------------------------------------------------------
	public void inserir(SinalDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psSinalinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.Sinal ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getSinal().length()>0)) {
			 stSqlCampos+=   stVirgula + "SINAL " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getSinal());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_SINAL",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(SinalDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psSinalalterar()");

		stSql= "UPDATE PROJUDI.Sinal SET  ";
		stSql+= "SINAL = ?";		 ps.adicionarString(dados.getSinal());  

		stSql += " WHERE ID_SINAL  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psSinalexcluir()");

		stSql= "DELETE FROM PROJUDI.Sinal";
		stSql += " WHERE ID_SINAL = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public SinalDt consultarId(String id_sinal )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		SinalDt Dados=null;
		////System.out.println("....ps-ConsultaId_Sinal)");

		stSql= "SELECT * FROM PROJUDI.VIEW_SINAL WHERE ID_SINAL = ?";		ps.adicionarLong(id_sinal); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_Sinal  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new SinalDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( SinalDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_SINAL"));
		Dados.setSinal(rs.getString("SINAL"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoSinal()");

		stSql= "SELECT ID_SINAL, SINAL FROM PROJUDI.VIEW_SINAL WHERE SINAL LIKE ?";
		stSql+= " ORDER BY SINAL ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoSinal  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				SinalDt obTemp = new SinalDt();
				obTemp.setId(rs1.getString("ID_SINAL"));
				obTemp.setSinal(rs1.getString("SINAL"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_SINAL WHERE SINAL LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..SinalPsGen.consultarDescricao() Operação realizada com sucesso");
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

} 
