package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.projudi.dt.EventoRegimeDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class EventoRegimePs extends EventoRegimePsGen{

	/**
     * 
     */
    private static final long serialVersionUID = 3407764936157909173L;

    public EventoRegimePs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Sobrescrevento o método do gerador, para alterar apenas os dados informados
	 */
	public void alterar(EventoRegimeDt dados) throws Exception{
		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql= "UPDATE PROJUDI.EVENTO_REGIME SET  ";
		if (dados.getId_ProcessoEventoExecucao().length() > 0) {
			Sql+= "ID_PROC_EVENTO_EXE  = ?";
			ps.adicionarLong(dados.getId_ProcessoEventoExecucao()); 
		} else {
			
		}
		if (dados.getId_RegimeExecucao().length() > 0) {
			Sql+= ",ID_REGIME_EXE  = ?";
			ps.adicionarLong( dados.getId_RegimeExecucao()); 
		}
		Sql=Sql.replace("SET  ,","SET  ");
		Sql = Sql + " WHERE ID_EVENTO_REGIME  = ?"; 
		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(Sql,ps);
	} 
	
	/**
	 * sobrescreveu o método do gen, acrescentando o RegimeExecucaoDt
	 */
	protected void associarDt( EventoRegimeDt Dados, ResultSetTJGO rs1 )  throws Exception {

		
		Dados.setId(rs1.getString("ID_EVENTO_REGIME"));
		Dados.setRegimeExecucao(rs1.getString("REGIME_EXE"));
		Dados.setId_ProcessoEventoExecucao( rs1.getString("ID_PROC_EVENTO_EXE"));
		Dados.setEventoExecucao( rs1.getString("EVENTO_EXE"));
		Dados.setId_RegimeExecucao( rs1.getString("ID_REGIME_EXE"));
		Dados.setCodigoTemp( rs1.getString("CODIGO_TEMP"));
		Dados.setId_ProximoRegimeExecucao(rs1.getString("ID_PROXIMO_REGIME_EXE"));
		Dados.setId_PenaExecucaoTipo( rs1.getString("ID_PENA_EXE_TIPO"));
		Dados.setPenaExecucaoTipo( rs1.getString("PENA_EXE_TIPO"));
		
		//dados regimeExecucao
		Dados.getRegimeExecucaoDt().setRegimeExecucao(rs1.getString("REGIME_EXE"));
		Dados.getRegimeExecucaoDt().setId(rs1.getString("ID_REGIME_EXE"));
		Dados.getRegimeExecucaoDt().setId_ProximoRegimeExecucao(rs1.getString("ID_PROXIMO_REGIME_EXE"));
		Dados.getRegimeExecucaoDt().setCodigoTemp(rs1.getString("CODIGO_TEMP"));
		Dados.getRegimeExecucaoDt().setId_PenaExecucaoTipo(rs1.getString("ID_PENA_EXE_TIPO"));
		Dados.getRegimeExecucaoDt().setPenaExecucaoTipo(rs1.getString("PENA_EXE_TIPO"));
		
	}

	public void excluirId_ProcessoEventoExecucao(String idProcessoEventoExecucao) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		//System.out.println("....psEventoRegimeExcluirIdProcessoEventoExecucao()");

		stSql= "DELETE FROM PROJUDI.EVENTO_REGIME";
		stSql += " WHERE ID_PROC_EVENTO_EXE = ?";
		ps.adicionarLong(idProcessoEventoExecucao); 

		executarUpdateDelete(stSql,ps);

	} 
}
