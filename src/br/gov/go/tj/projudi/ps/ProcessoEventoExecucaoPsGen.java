package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoEventoExecucaoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ProcessoEventoExecucaoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -7145746213762266793L;

	//---------------------------------------------------------
	public ProcessoEventoExecucaoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(ProcessoEventoExecucaoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProcessoEventoExecucaoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.PROC_EVENTO_EXE ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getDataInicio().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_INICIO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDate(dados.getDataInicio());  

			stVirgula=",";
		}
//		if ((dados.getDataFim().length()>0)) {
//			 stSqlCampos+=   stVirgula + "DATA_FIM " ;
//			 stSqlValores+=   stVirgula + "? " ;
//			 ps.adicionarDate(dados.getDataFim());  
//
//			stVirgula=",";
//		}
		if ((dados.getQuantidade().length()>0)) {
			 stSqlCampos+=   stVirgula + "QUANTIDADE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getQuantidade());  

			stVirgula=",";
		}
		if ((dados.getObservacao().length()>0)) {
			 stSqlCampos+=   stVirgula + "OBSERVACAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getObservacao());  

			stVirgula=",";
		}
		if ((dados.getId_LivramentoCondicional().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_LIV_CONDICIONAL " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_LivramentoCondicional());  

			stVirgula=",";
		}
		if ((dados.getId_Movimentacao().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_MOVI " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Movimentacao());  

			stVirgula=",";
		}
		if ((dados.getId_EventoExecucao().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_EVENTO_EXE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_EventoExecucao());  

			stVirgula=",";
		}
		if ((dados.getId_ProcessoExecucao().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC_EXE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ProcessoExecucao());  

			stVirgula=",";
		}
		if (!(dados.getConsiderarTempoLivramentoCondicional().length()==0)){
			stSqlCampos+=   stVirgula + "CONS_TEMPO_LIV_CONDICIONAL ";
			stSqlValores+=   stVirgula +"?";
			ps.adicionarBoolean(dados.getConsiderarTempoLivramentoCondicional());
			
			stVirgula=",";
		}	
		
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_PROC_EVENTO_EXE",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(ProcessoEventoExecucaoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psProcessoEventoExecucaoalterar()");

		stSql= "UPDATE PROJUDI.PROC_EVENTO_EXE SET  ";
		stSql+= "DATA_INICIO = ?";		 ps.adicionarDate(dados.getDataInicio());  

		if (dados.getDataPrisaoRevogacaoLC().length() > 0){
			stSql+= ",DATA_PRISAO_REVOG_LC = ? ";
			ps.adicionarDate(dados.getDataPrisaoRevogacaoLC());
		}  

		stSql+= ",QUANTIDADE = ?";		 ps.adicionarLong(dados.getQuantidade());
		
		stSql+= ",OBSERVACAO = ?";		 ps.adicionarString(dados.getObservacao());
		
		stSql+= ",ID_LIV_CONDICIONAL = ?";		 ps.adicionarLong(dados.getId_LivramentoCondicional());

		stSql+= ",ID_MOVI = ?";		 ps.adicionarLong(dados.getId_Movimentacao());
		
		stSql+= ",ID_EVENTO_EXE = ?";		 ps.adicionarLong(dados.getId_EventoExecucao());

		stSql+= ",ID_PROC_EXE = ?";		 ps.adicionarLong(dados.getId_ProcessoExecucao());
		
		stSql+= ",CONS_TEMPO_LIV_CONDICIONAL = ?";		 ps.adicionarBoolean(dados.getConsiderarTempoLivramentoCondicional());

		stSql += " WHERE ID_PROC_EVENTO_EXE  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProcessoEventoExecucaoexcluir()");

		stSql= "DELETE FROM PROJUDI.PROC_EVENTO_EXE";
		stSql += " WHERE ID_PROC_EVENTO_EXE = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ProcessoEventoExecucaoDt consultarId(String id_processoeventoexecucao )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ProcessoEventoExecucaoDt Dados=null;
		////System.out.println("....ps-ConsultaId_ProcessoEventoExecucao)");

		stSql= "SELECT * FROM PROJUDI.VIEW_PROC_EVENTO_EXE WHERE ID_PROC_EVENTO_EXE = ?";		ps.adicionarLong(id_processoeventoexecucao); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_ProcessoEventoExecucao  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ProcessoEventoExecucaoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ProcessoEventoExecucaoDt Dados, ResultSetTJGO rs )  throws Exception {


		Dados.setId(rs.getString("ID_PROC_EVENTO_EXE"));
		Dados.setEventoExecucao(rs.getString("EVENTO_EXE"));
		Dados.setDataInicio( Funcoes.FormatarData(rs.getDateTime("DATA_INICIO")));
//			Dados.setDataFim( Funcoes.FormatarData(rs.getDate("DATA_FIM")));
		Dados.setQuantidade( rs.getString("QUANTIDADE"));
		Dados.setObservacao( rs.getString("OBSERVACAO"));
		Dados.setId_LivramentoCondicional( rs.getString("ID_LIV_CONDICIONAL"));
		Dados.setConsiderarTempoLivramentoCondicional( Funcoes.FormatarLogico(rs.getString("CONS_TEMPO_LIV_CONDICIONAL")));
		Dados.setId_Movimentacao( rs.getString("ID_MOVI"));
		Dados.setId_ProcessoExecucao( rs.getString("ID_PROC_EXE"));
		Dados.setMovimentacaoTipo( rs.getString("MOVI_TIPO"));
		Dados.setId_EventoExecucao( rs.getString("ID_EVENTO_EXE"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));

	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoProcessoEventoExecucao()");

		stSql= "SELECT ID_PROC_EVENTO_EXE, EVENTO_EXE FROM PROJUDI.VIEW_PROC_EVENTO_EXE WHERE EVENTO_EXE LIKE ?";
		stSql+= " ORDER BY EVENTO_EXE ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoProcessoEventoExecucao  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ProcessoEventoExecucaoDt obTemp = new ProcessoEventoExecucaoDt();
				obTemp.setId(rs1.getString("ID_PROC_EVENTO_EXE"));
				obTemp.setEventoExecucao(rs1.getString("EVENTO_EXE"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PROC_EVENTO_EXE WHERE EVENTO_EXE LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..ProcessoEventoExecucaoPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
