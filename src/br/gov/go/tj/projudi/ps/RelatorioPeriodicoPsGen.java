package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.RelatorioPeriodicoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class RelatorioPeriodicoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 1845989521656376082L;

	//---------------------------------------------------------
	public RelatorioPeriodicoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(RelatorioPeriodicoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psRelatorioPeriodicoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.RELATORIO_PERIODICO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getRelatorioPeriodico().length()>0)) {
			 stSqlCampos+=   stVirgula + "RELATORIO_PERIODICO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getRelatorioPeriodico());  

			stVirgula=",";
		}
		if ((dados.getCodigoSql().length()>0)) {
			 stSqlCampos+=   stVirgula + "CODIGO_SQL " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getCodigoSql());  

			stVirgula=",";
		}
		if ((dados.getCamposSql().length()>0)) {
			 stSqlCampos+=   stVirgula + "CAMPOS_SQL " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getCamposSql());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_RELATORIO_PERIODICO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(RelatorioPeriodicoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psRelatorioPeriodicoalterar()");

		stSql= "UPDATE PROJUDI.RELATORIO_PERIODICO SET  ";
		stSql+= "RELATORIO_PERIODICO = ?";		 ps.adicionarString(dados.getRelatorioPeriodico());  

		stSql+= ",CODIGO_SQL = ?";		 ps.adicionarString(dados.getCodigoSql());  

		stSql+= ",CAMPOS_SQL = ?";		 ps.adicionarString(dados.getCamposSql());  

		stSql += " WHERE ID_RELATORIO_PERIODICO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psRelatorioPeriodicoexcluir()");

		stSql= "DELETE FROM PROJUDI.RELATORIO_PERIODICO";
		stSql += " WHERE ID_RELATORIO_PERIODICO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public RelatorioPeriodicoDt consultarId(String id_relatorioperiodico )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		RelatorioPeriodicoDt Dados=null;
		////System.out.println("....ps-ConsultaId_RelatorioPeriodico)");

		stSql= "SELECT * FROM PROJUDI.VIEW_RELATORIO_PERIODICO WHERE ID_RELATORIO_PERIODICO = ?";		ps.adicionarLong(id_relatorioperiodico); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_RelatorioPeriodico  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new RelatorioPeriodicoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( RelatorioPeriodicoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_RELATORIO_PERIODICO"));
		Dados.setRelatorioPeriodico(rs.getString("RELATORIO_PERIODICO"));
		Dados.setCodigoSql( rs.getString("CODIGO_SQL"));
		Dados.setCamposSql( rs.getString("CAMPOS_SQL"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoRelatorioPeriodico()");

		stSql= "SELECT ID_RELATORIO_PERIODICO, RELATORIO_PERIODICO FROM PROJUDI.VIEW_RELATORIO_PERIODICO WHERE RELATORIO_PERIODICO LIKE ?";
		stSql+= " ORDER BY RELATORIO_PERIODICO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoRelatorioPeriodico  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				RelatorioPeriodicoDt obTemp = new RelatorioPeriodicoDt();
				obTemp.setId(rs1.getString("ID_RELATORIO_PERIODICO"));
				obTemp.setRelatorioPeriodico(rs1.getString("RELATORIO_PERIODICO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_RELATORIO_PERIODICO WHERE RELATORIO_PERIODICO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..RelatorioPeriodicoPsGen.consultarDescricao() Operação realizada com sucesso");
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

} 
