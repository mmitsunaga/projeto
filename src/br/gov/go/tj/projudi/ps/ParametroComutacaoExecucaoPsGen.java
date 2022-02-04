package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ParametroComutacaoExecucaoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ParametroComutacaoExecucaoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 3453655756533115574L;

	//---------------------------------------------------------
	public ParametroComutacaoExecucaoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(ParametroComutacaoExecucaoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		//System.out.println("....psParametroComutacaoExecucaoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.PARAMETRO_COMUTACAO_EXE ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getDataDecreto().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_DECRETO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDate(dados.getDataDecreto());  

			stVirgula=",";
		}
		if ((dados.getFracaoHediondo().length()>0)) {
			 stSqlCampos+=   stVirgula + "FRACAO_HEDIONDO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getFracaoHediondo());  

			stVirgula=",";
		}
		if ((dados.getFracaoComum().length()>0)) {
			 stSqlCampos+=   stVirgula + "FRACAO_COMUM " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getFracaoComum());  

			stVirgula=",";
		}
		if ((dados.getFracaoComumReinc().length()>0)) {
			 stSqlCampos+=   stVirgula + "FRACAO_COMUM_REINC " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getFracaoComumReinc());  

			stVirgula=",";
		}
		if ((dados.getFracaoComumFeminino().length()>0)) {
			 stSqlCampos+=   stVirgula + "FRACAO_COMUM_FEMININO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getFracaoComumFeminino());  

			stVirgula=",";
		}
		if ((dados.getFracaoComumReincFeminino().length()>0)) {
			 stSqlCampos+=   stVirgula + "FRACAO_COMUM_REINC_FEMININO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getFracaoComumReincFeminino());  

			stVirgula=",";
		}
		if ((dados.getPenaUnificada().length()>0)) {
			 stSqlCampos+=   stVirgula + "PENA_UNIFICADA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBoolean(dados.getPenaUnificada());  

			stVirgula=",";
		}
		if ((dados.getBeneficioAcumulado().length()>0)) {
			 stSqlCampos+=   stVirgula + "BENEFICIO_ACUMULADO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBoolean(dados.getBeneficioAcumulado());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 

		dados.setId(executarInsert(stSql,"ID_PARAMETRO_COMUTACAO_EXE",ps));
	} 

//---------------------------------------------------------
	public void alterar(ParametroComutacaoExecucaoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		//System.out.println("....psParametroComutacaoExecucaoalterar()");

		stSql= "UPDATE PROJUDI.PARAMETRO_COMUTACAO_EXE SET  ";
		stSql+= "DATA_DECRETO = ?";		 ps.adicionarDate(dados.getDataDecreto());  

		stSql+= ",FRACAO_HEDIONDO = ?";		 ps.adicionarString(dados.getFracaoHediondo());  

		stSql+= ",FRACAO_COMUM = ?";		 ps.adicionarString(dados.getFracaoComum());  

		stSql+= ",FRACAO_COMUM_REINC = ?";		 ps.adicionarString(dados.getFracaoComumReinc());  

		stSql+= ",FRACAO_COMUM_FEMININO = ?";		 ps.adicionarString(dados.getFracaoComumFeminino());  

		stSql+= ",FRACAO_COMUM_REINC_FEMININO = ?";		 ps.adicionarString(dados.getFracaoComumReincFeminino());  

		stSql+= ",PENA_UNIFICADA = ?";		 ps.adicionarBoolean(dados.getPenaUnificada());  
		
		stSql+= ",BENEFICIO_ACUMULADO = ?";		 ps.adicionarBoolean(dados.getBeneficioAcumulado());  

		stSql += " WHERE ID_PARAMETRO_COMUTACAO_EXE  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		//System.out.println("....psParametroComutacaoExecucaoexcluir()");

		stSql= "DELETE FROM PROJUDI.PARAMETRO_COMUTACAO_EXE";
		stSql += " WHERE ID_PARAMETRO_COMUTACAO_EXE = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);
	} 

//---------------------------------------------------------
	public ParametroComutacaoExecucaoDt consultarId(String id_parametrocomutacaoexecucao )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ParametroComutacaoExecucaoDt Dados=null;
		//System.out.println("....ps-ConsultaId_ParametroComutacaoExecucao)");

		stSql= "SELECT * FROM PROJUDI.VIEW_PARAMETRO_COMUTACAO_EXE WHERE ID_PARAMETRO_COMUTACAO_EXE = ?";	
		ps.adicionarLong(id_parametrocomutacaoexecucao); 

		//System.out.println("....Sql  " + stSql  );

		try{
			//System.out.println("..ps-ConsultaId_ParametroComutacaoExecucao  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ParametroComutacaoExecucaoDt();
				associarDt(Dados, rs1);
			}
			//System.out.println("..ps-ConsultaId");
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return Dados; 
	}

	protected void associarDt( ParametroComutacaoExecucaoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_PARAMETRO_COMUTACAO_EXE"));
		Dados.setDataDecreto(Funcoes.FormatarData(rs.getDateTime("DATA_DECRETO")));			
		Dados.setFracaoHediondo( rs.getString("FRACAO_HEDIONDO"));
		Dados.setFracaoComum( rs.getString("FRACAO_COMUM"));
		Dados.setFracaoComumReinc( rs.getString("FRACAO_COMUM_REINC"));
		Dados.setFracaoComumFeminino( rs.getString("FRACAO_COMUM_FEMININO"));
		Dados.setFracaoComumReincFeminino( rs.getString("FRACAO_COMUM_REINC_FEMININO"));
		Dados.setPenaUnificada( Funcoes.FormatarLogico(rs.getString("PENA_UNIFICADA")));
		Dados.setBeneficioAcumulado( Funcoes.FormatarLogico(rs.getString("BENEFICIO_ACUMULADO")));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		//System.out.println("..ps-ConsultaDescricaoParametroComutacaoExecucao()");

		stSql= "SELECT ID_PARAMETRO_COMUTACAO_EXE, DATA_DECRETO, FRACAO_HEDIONDO, FRACAO_COMUM, FRACAO_COMUM_REINC, FRACAO_COMUM_FEMININO, FRACAO_COMUM_REINC_FEMININO, PENA_UNIFICADA, BENEFICIO_ACUMULADO, CODIGO_TEMP" +
				" FROM PROJUDI.VIEW_PARAMETRO_COMUTACAO_EXE ";
		if (descricao.length() > 0) {
			stSql += "WHERE DATA_DECRETO = ?";
			ps.adicionarDate(descricao);
		}
		stSql+= " ORDER BY DATA_DECRETO ";

		try{
			//System.out.println("..ps-ConsultaDescricaoParametroComutacaoExecucao  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			//System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ParametroComutacaoExecucaoDt obTemp = new ParametroComutacaoExecucaoDt();
				associarDt(obTemp, rs1);
//				obTemp.setId(rs1.getString("ID_PARAMETRO_COMUTACAO_EXE"));
//				obTemp.setDataDecreto(Funcoes.FormatarData(rs1.getDate("DATA_DECRETO")));
				liTemp.add(obTemp);
			}
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_PARAMETRO_COMUTACAO_EXE ";
			if (descricao.length() > 0) stSql += "WHERE DATA_DECRETO = ?";
			rs2 = consultar(stSql,ps);
			//System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("Quantidade"));
			}
			//System.out.println("..ParametroComutacaoExecucaoPsGen.consultarDescricao() Operação realizada com sucesso");
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

} 
