package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ParametroComutacaoExecucaoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class ParametroComutacaoExecucaoPs extends ParametroComutacaoExecucaoPsGen{

	private static final long serialVersionUID = 819956211041448490L;

	public ParametroComutacaoExecucaoPs(Connection conexao){
		Conexao = conexao;
	}


	/**
	 * Lista os ParâmetrosComutacaoExecucao
	 * @param posicao
	 * @return List
	 * @throws Exception
	 */
	public List listarParametroComutacaoExecucao() throws Exception {
		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql= "SELECT * FROM PROJUDI.VIEW_PARAMETRO_COMUTACAO_EXE";
		Sql+= " ORDER BY DATA_DECRETO DESC";		
		try{
			rs1 = consultar(Sql, ps);

			while (rs1.next()) {
				ParametroComutacaoExecucaoDt obTemp = new ParametroComutacaoExecucaoDt();
				associarDt(obTemp, rs1);
				liTemp.add(obTemp);
			}
			
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }
		return liTemp; 
	}
	
	/**
	 * Lista os ParâmetrosComutacaoExecucao de acordo com os Id's passados
	 * @param posicao
	 * @return List
	 * @throws Exception
	 */
	public List listarParametroComutacaoExecucao(List listaIdParametroComutacao) throws Exception {
		String Sql = "";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		if (listaIdParametroComutacao != null && listaIdParametroComutacao.size() > 0){
			Sql= "SELECT * FROM PROJUDI.VIEW_PARAMETRO_COMUTACAO_EXE WHERE ID_PARAMETRO_COMUTACAO_EXE IN(";
			for (int i=0; i<listaIdParametroComutacao.size(); i++){
				Sql += "?";
				if (i<listaIdParametroComutacao.size()-1) Sql += ",";
				ps.adicionarLong(listaIdParametroComutacao.get(i).toString());
			}
			Sql+= " ) ORDER BY DATA_DECRETO DESC";		
		}
		try{
			rs1 = consultar(Sql, ps);

			while (rs1.next()) {
				ParametroComutacaoExecucaoDt obTemp = new ParametroComutacaoExecucaoDt();
				associarDt(obTemp, rs1);
				liTemp.add(obTemp);
			}
			
		}  finally {
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }
		return liTemp; 
	}
	
	
	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {

		String stSql="";
		String stSqlFrom ="";
		String stSqlOrder ="";
		String stTemp = "";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 5;

		stSql= "SELECT ID_PARAMETRO_COMUTACAO_EXE AS ID, DATA_DECRETO AS DESCRICAO1, FRACAO_HEDIONDO AS DESCRICAO2, FRACAO_COMUM AS DESCRICAO3, FRACAO_COMUM_REINC AS DESCRICAO4, PENA_UNIFICADA AS DESCRICAO5";
		stSqlFrom = " FROM PROJUDI.VIEW_PARAMETRO_COMUTACAO_EXE ";
		if (descricao.length() > 0) {
			stSqlFrom += "WHERE DATA_DECRETO = ?";
			ps.adicionarDate(descricao);
		}
		stSqlOrder = " ORDER BY DATA_DECRETO DESC";

		try{
			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade";			
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSONParametroComutacao(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp; 
	}
	
	public String gerarJSONParametroComutacao( long qtdPaginas, String posicaoAtual, ResultSetTJGO rs, int qtdeColunas) throws Exception{
		StringBuilder stTemp = new StringBuilder();
		stTemp.append("[");
		stTemp.append("{\"id\":\"-50000\",\"desc1\":\"").append(qtdPaginas);
		for (int i = 2; i <= qtdeColunas; i++) {
			stTemp.append("\",\"desc" + i + "\":\"");
		}
		stTemp.append("\"}");
		stTemp.append(",{\"id\":\"-60000\",\"desc1\":\"").append(posicaoAtual);
		for (int i = 2; i <= qtdeColunas; i++) {
			stTemp.append("\",\"desc" + i + "\":\"");
		}
		stTemp.append("\"}");
		while (rs.next()){			
			stTemp.append(",{\"id\":\"").append(rs.getString("Id")).append("\",\"desc1\":\"").append(Funcoes.FormatarData(rs.getDateTime("descricao1")));
			if (rs.getString("descricao2") == "null" || rs.getString("descricao2") == null) stTemp.append("\",\"desc2\":\"").append("--");
			else stTemp.append("\",\"desc2\":\"").append(rs.getString("descricao2"));
			for (int i = 3; i < 5; i++) {
				stTemp.append("\",\"desc" + i + "\":\"").append(rs.getString("descricao" + i));
			}
			if (Funcoes.FormatarLogico(rs.getString("descricao5")) == "true") stTemp.append("\",\"desc5\":\"").append("Sim");
			else stTemp.append("\",\"desc5\":\"").append("Não");
			stTemp.append("\"}");
		}
		stTemp.append("]");
		return stTemp.toString();
	}
	
	public String gerarJSONParametroComutacaoVazio( long qtdPaginas, String posicaoAtual, int qtdeColunas) throws Exception{
		StringBuilder stTemp = new StringBuilder();
		stTemp.append("[");
		stTemp.append("{\"id\":\"-50000\",\"desc1\":\"").append(qtdPaginas);
		for (int i = 2; i <= qtdeColunas; i++) {
			stTemp.append("\",\"desc" + i + "\":\"");
		}
		stTemp.append("\"}");
		stTemp.append(",{\"id\":\"-60000\",\"desc1\":\"").append(posicaoAtual);
		for (int i = 2; i <= qtdeColunas; i++) {
			stTemp.append("\",\"desc" + i + "\":\"");
		}
		stTemp.append("\"}");
		stTemp.append("]");
		return stTemp.toString();
	}
	
}
