package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.TarefaDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class TarefaPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -1602334155807203105L;

	//---------------------------------------------------------
	public TarefaPsGen() {

	}



//---------------------------------------------------------
	public void inserir(TarefaDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psTarefainserir()");
		stSqlCampos= "INSERT INTO PROJUDI.Tarefa ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getTarefa().length()>0)) {
			 stSqlCampos+=   stVirgula + "TAREFA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getTarefa());  

			stVirgula=",";
		}
		if ((dados.getDescricao().length()>0)) {
			 stSqlCampos+=   stVirgula + "DESCRICAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getDescricao());  

			stVirgula=",";
		}
		if ((dados.getResposta().length()>0)) {
			 stSqlCampos+=   stVirgula + "RESPOSTA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getResposta());  

			stVirgula=",";
		}
		if ((dados.getDataInicio().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_INICIO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDate(dados.getDataInicio());  

			stVirgula=",";
		}
		if ((dados.getPrevisao().length()>0)) {
			 stSqlCampos+=   stVirgula + "PREVISAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getPrevisao());  

			stVirgula=",";
		}
		if ((dados.getDataFim().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_FIM " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDate(dados.getDataFim());  

			stVirgula=",";
		}
		if ((dados.getDataCriacao().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_CRIACAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDate(dados.getDataCriacao());  

			stVirgula=",";
		}
		if ((dados.getId_TarefaPai().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_TAREFA_PAI " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_TarefaPai());  

			stVirgula=",";
		}
		if ((dados.getPontosApf().length()>0)) {
			 stSqlCampos+=   stVirgula + "PONTOS_APF " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getPontosApf());  

			stVirgula=",";
		}
		if ((dados.getPontosApg().length()>0)) {
			 stSqlCampos+=   stVirgula + "PONTOS_APG " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getPontosApg());  

			stVirgula=",";
		}
		if ((dados.getId_TarefaPrioridade().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_TAREFA_PRIOR " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_TarefaPrioridade());  

			stVirgula=",";
		}
		if ((dados.getId_TarefaStatus().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_TAREFA_STATUS " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_TarefaStatus());  

			stVirgula=",";
		}
		if ((dados.getTarefaStatusCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "TAREFA_STATUS_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getTarefaStatusCodigo());  

			stVirgula=",";
		}
		if ((dados.getId_TarefaTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_TAREFA_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_TarefaTipo());  

			stVirgula=",";
		}
		if ((dados.getId_Projeto().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROJETO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Projeto());  

			stVirgula=",";
		}
		if ((dados.getId_ProjetoParticipanteResponsavel().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROJ_PARTIC_RESPONSAVEL " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ProjetoParticipanteResponsavel());  

			stVirgula=",";
		}
		if ((dados.getId_UsuarioCriador().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU_CRIADOR " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_UsuarioCriador());  

			stVirgula=",";
		}
		if ((dados.getId_UsuarioFinalizador().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU_FINALIZADOR " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_UsuarioFinalizador());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_TAREFA",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(TarefaDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psTarefaalterar()");

		stSql= "UPDATE PROJUDI.Tarefa SET  ";
		stSql+= "TAREFA = ?";		 ps.adicionarString(dados.getTarefa());  

		stSql+= ",DESCRICAO = ?";		 ps.adicionarString(dados.getDescricao());  

		stSql+= ",RESPOSTA = ?";		 ps.adicionarString(dados.getResposta());  

		stSql+= ",DATA_INICIO = ?";		 ps.adicionarDate(dados.getDataInicio());  

		stSql+= ",PREVISAO = ?";		 ps.adicionarLong(dados.getPrevisao());  

		stSql+= ",DATA_FIM = ?";		 ps.adicionarDate(dados.getDataFim());  

		stSql+= ",DATA_CRIACAO = ?";		 ps.adicionarDate(dados.getDataCriacao());  

		stSql+= ",ID_TAREFA_PAI = ?";		 ps.adicionarLong(dados.getId_TarefaPai());  

		stSql+= ",PONTOS_APF = ?";		 ps.adicionarLong(dados.getPontosApf());  

		stSql+= ",PONTOS_APG = ?";		 ps.adicionarLong(dados.getPontosApg());  

		stSql+= ",ID_TAREFA_PRIOR = ?";		 ps.adicionarLong(dados.getId_TarefaPrioridade());  

		stSql+= ",ID_TAREFA_STATUS = ?";		 ps.adicionarLong(dados.getId_TarefaStatus());  

		stSql+= ",TAREFA_STATUS_CODIGO = ?";		 ps.adicionarLong(dados.getTarefaStatusCodigo());  

		stSql+= ",ID_TAREFA_TIPO = ?";		 ps.adicionarLong(dados.getId_TarefaTipo());  

		stSql+= ",ID_PROJETO = ?";		 ps.adicionarLong(dados.getId_Projeto());  

		stSql+= ",ID_PROJ_PARTIC_RESPONSAVEL = ?";		 ps.adicionarLong(dados.getId_ProjetoParticipanteResponsavel());  

		stSql+= ",ID_USU_CRIADOR = ?";		 ps.adicionarLong(dados.getId_UsuarioCriador());  

		stSql+= ",ID_USU_FINALIZADOR = ?";		 ps.adicionarLong(dados.getId_UsuarioFinalizador());  

		stSql += " WHERE ID_TAREFA  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psTarefaexcluir()");

		stSql= "DELETE FROM PROJUDI.Tarefa";
		stSql += " WHERE ID_TAREFA = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public TarefaDt consultarId(String id_tarefa )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		TarefaDt Dados=null;
		////System.out.println("....ps-ConsultaId_Tarefa)");

		stSql= "SELECT * FROM PROJUDI.VIEW_TAREFA WHERE ID_TAREFA = ?";		ps.adicionarLong(id_tarefa); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_Tarefa  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new TarefaDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( TarefaDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_TAREFA"));
		Dados.setTarefa(rs.getString("TAREFA"));
		Dados.setDescricao( rs.getString("DESCRICAO"));
		Dados.setResposta( rs.getString("RESPOSTA"));
		Dados.setDataInicio( Funcoes.FormatarData(rs.getDateTime("DATA_INICIO")));
		Dados.setPrevisao( rs.getString("PREVISAO"));
		Dados.setDataFim( Funcoes.FormatarData(rs.getDateTime("DATA_FIM")));
		Dados.setId_TarefaPai( rs.getString("ID_TAREFA_PAI"));
		Dados.setTarefaPai( rs.getString("TAREFA_PAI"));
		Dados.setPontosApf( rs.getString("PONTOS_APF"));
		Dados.setPontosApg( rs.getString("PONTOS_APG"));
		Dados.setId_TarefaPrioridade( rs.getString("ID_TAREFA_PRIOR"));
		Dados.setTarefaPrioridade( rs.getString("TAREFA_PRIOR"));
		Dados.setId_TarefaStatus( rs.getString("ID_TAREFA_STATUS"));
		Dados.setTarefaStatus( rs.getString("TAREFA_STATUS"));
		Dados.setTarefaStatusCodigo( rs.getString("TAREFA_STATUS_CODIGO"));
		Dados.setId_TarefaTipo( rs.getString("ID_TAREFA_TIPO"));
		Dados.setTarefaTipo( rs.getString("TAREFA_TIPO"));
		Dados.setId_Projeto( rs.getString("ID_PROJETO"));
		Dados.setProjeto( rs.getString("PROJETO"));
		Dados.setId_ProjetoParticipanteResponsavel( rs.getString("ID_PROJ_PARTIC_RESPONSAVEL"));
		Dados.setProjetoParticipanteResponsavel( rs.getString("PROJETO_PARTICIPANTE_RESP"));
		Dados.setId_UsuarioCriador( rs.getString("ID_USU_CRIADOR"));
		Dados.setUsuarioCriador( rs.getString("USU_CRIADOR"));
		Dados.setId_UsuarioFinalizador( rs.getString("ID_USU_FINALIZADOR"));
		Dados.setUsuarioFinalizador( rs.getString("USU_FINALIZADOR"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));

	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoTarefa()");

		stSql= "SELECT ID_TAREFA, TAREFA FROM PROJUDI.VIEW_TAREFA WHERE TAREFA LIKE ?";
		stSql+= " ORDER BY TAREFA ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoTarefa  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				TarefaDt obTemp = new TarefaDt();
				obTemp.setId(rs1.getString("ID_TAREFA"));
				obTemp.setTarefa(rs1.getString("TAREFA"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_TAREFA WHERE TAREFA LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..TarefaPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
