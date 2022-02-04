package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.projudi.dt.EventoLocalDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;

//---------------------------------------------------------
public class EventoLocalPs extends EventoLocalPsGen{


	/**
     * 
     */
    private static final long serialVersionUID = 5621670743928756619L;

    public EventoLocalPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Sobrescrevento o método do gerador, para alterar apenas os dados informados
	 */
	public void alterar(EventoLocalDt dados) throws Exception{
		String Sql;
		boolean existeAlteracao = false;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql= "UPDATE PROJUDI.EVENTO_LOCAL SET  ";
		if (dados.getId_ProcessoEventoExecucao().length() > 0){
			Sql+= "ID_PROC_EVENTO_EXE  = ?";
			ps.adicionarLong(dados.getId_ProcessoEventoExecucao());			
			existeAlteracao = true;
		}
		if (dados.getId_LocalCumprimentoPena().length() > 0){
			Sql+= ",ID_LOCAL_CUMP_PENA  = ?";
			ps.adicionarLong(dados.getId_LocalCumprimentoPena());			
			existeAlteracao = true;
		}
		Sql=Sql.replace("SET  ,","SET  ");
		Sql = Sql + " WHERE ID_EVENTO_LOCAL  = ?";
		ps.adicionarLong(dados.getId());
		
		if (!existeAlteracao) return;

		executarUpdateDelete(Sql,ps);
	} 

	public void excluirId_ProcessoEventoExecucao(String idProcessoEventoExecucao) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		//System.out.println("....psEventoLocalexcluirId_ProcessoEventoExecucao()");

		stSql= "DELETE FROM PROJUDI.EVENTO_LOCAL";
		stSql += " WHERE ID_PROC_EVENTO_EXE = ?";
		ps.adicionarLong(idProcessoEventoExecucao); 

		executarUpdateDelete(stSql,ps);

	} 
}
