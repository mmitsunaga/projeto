package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.RegimeExecucaoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class RegimeExecucaoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -8480508675579692529L;

	//---------------------------------------------------------
	public RegimeExecucaoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(RegimeExecucaoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psRegimeExecucaoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.REGIME_EXE ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getRegimeExecucao().length()>0)) {
			 stSqlCampos+=   stVirgula + "REGIME_EXE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getRegimeExecucao());  

			stVirgula=",";
		}
		if ((dados.getId_PenaExecucaoTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PENA_EXE_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_PenaExecucaoTipo());  

			stVirgula=",";
		}
		if ((dados.getId_ProximoRegimeExecucao().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROXIMO_REGIME_EXE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ProximoRegimeExecucao());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_REGIME_EXE",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(RegimeExecucaoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psRegimeExecucaoalterar()");

		stSql= "UPDATE PROJUDI.REGIME_EXE SET  ";
		stSql+= "REGIME_EXE = ?";		 ps.adicionarString(dados.getRegimeExecucao());  

		stSql+= ",ID_PENA_EXE_TIPO = ?";		 ps.adicionarLong(dados.getId_PenaExecucaoTipo());
		
		stSql+= ",ID_PROXIMO_REGIME_EXE = ?";		 ps.adicionarLong(dados.getId_ProximoRegimeExecucao()); 

		stSql += " WHERE ID_REGIME_EXE  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psRegimeExecucaoexcluir()");

		stSql= "DELETE FROM PROJUDI.REGIME_EXE";
		stSql += " WHERE ID_REGIME_EXE = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);
	} 

//---------------------------------------------------------
	public RegimeExecucaoDt consultarId(String id_regimeexecucao )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		RegimeExecucaoDt Dados=null;
		////System.out.println("....ps-ConsultaId_RegimeExecucao)");

		stSql= "SELECT * FROM PROJUDI.VIEW_REGIME_EXE WHERE ID_REGIME_EXE = ?";		ps.adicionarLong(id_regimeexecucao); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_RegimeExecucao  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new RegimeExecucaoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( RegimeExecucaoDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_REGIME_EXE"));
		Dados.setRegimeExecucao(rs.getString("REGIME_EXE"));
		Dados.setId_PenaExecucaoTipo( rs.getString("ID_PENA_EXE_TIPO"));
		Dados.setId_ProximoRegimeExecucao( rs.getString("ID_PROXIMO_REGIME_EXE"));
		Dados.setProximoRegimeExecucao( rs.getString("PROXIMO_REGIME_EXE"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		Dados.setPenaExecucaoTipo( rs.getString("PENA_EXE_TIPO"));

	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoRegimeExecucao()");

		stSql= "SELECT ID_REGIME_EXE, REGIME_EXE FROM PROJUDI.VIEW_REGIME_EXE WHERE REGIME_EXE LIKE ?";
		stSql+= " ORDER BY REGIME_EXE ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoRegimeExecucao  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				RegimeExecucaoDt obTemp = new RegimeExecucaoDt();
				obTemp.setId(rs1.getString("ID_REGIME_EXE"));
				obTemp.setRegimeExecucao(rs1.getString("REGIME_EXE"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_REGIME_EXE WHERE REGIME_EXE LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..RegimeExecucaoPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
