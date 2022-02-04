package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoExecucaoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ProcessoExecucaoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -3297517034883534945L;

	//---------------------------------------------------------
	public ProcessoExecucaoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(ProcessoExecucaoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProcessoExecucaoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.PROC_EXE ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_ProcessoExecucaoPenal().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC_EXE_PENAL " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ProcessoExecucaoPenal());  

			stVirgula=",";
		}
		if ((dados.getId_ProcessoAcaoPenal().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC_ACAOPENAL " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ProcessoAcaoPenal());  

			stVirgula=",";
		}		
		if ((dados.getId_CidadeOrigem().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_CIDADE_ORIGEM " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_CidadeOrigem());  

			stVirgula=",";
		}
		if ((dados.getEstadoOrigem().length()>0)) {
			 stSqlCampos+=   stVirgula + "ESTADO_ORIGEM " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getEstadoOrigem());  

			stVirgula=",";
		}
		if ((dados.getUfOrigem().length()>0)) {
			 stSqlCampos+=   stVirgula + "UF_ORIGEM " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getUfOrigem());  

			stVirgula=",";
		}
		if ((dados.getDataAcordao().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_ACORDAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDate(dados.getDataAcordao());  

			stVirgula=",";
		}
		if ((dados.getDataDistribuicao().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_DIST " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDate(dados.getDataDistribuicao());  

			stVirgula=",";
		}
		if ((dados.getDataPronuncia().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_PRONUNCIA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDate(dados.getDataPronuncia());  

			stVirgula=",";
		}
		if ((dados.getDataSentenca().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_SENTENCA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDate(dados.getDataSentenca());  

			stVirgula=",";
		}
		if ((dados.getDataTransitoJulgado().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_TRANS_JULGADO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDate(dados.getDataTransitoJulgado());  

			stVirgula=",";
		}
		if ((dados.getDataDenuncia().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_DENUNCIA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDate(dados.getDataDenuncia());  

			stVirgula=",";
		}
		if ((dados.getDataAdmonitoria().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_ADMONITORIA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDate(dados.getDataAdmonitoria());  

			stVirgula=",";
		}
		if ((dados.getDataInicioCumprimentoPena().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_INICIO_CUMP_PENA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDate(dados.getDataInicioCumprimentoPena());  

			stVirgula=",";
		}
		if ((dados.getNumeroAcaoPenal().length()>0)) {
			 stSqlCampos+=   stVirgula + "NUMERO_ACAO_PENAL " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getNumeroAcaoPenal());  

			stVirgula=",";
		}
		if ((dados.getVaraOrigem().length()>0)) {
			 stSqlCampos+=   stVirgula + "VARA_ORIGEM " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getVaraOrigem());  

			stVirgula=",";
		}			
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_PROC_EXE",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(ProcessoExecucaoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psProcessoExecucaoalterar()");

		stSql= "UPDATE PROJUDI.PROC_EXE SET  ";
		stSql+= "ID_PROC_EXE_PENAL = ?";		 ps.adicionarLong(dados.getId_ProcessoExecucaoPenal());
		
		stSql+= ",ID_PROC_ACAOPENAL = ?";		 ps.adicionarLong(dados.getId_ProcessoAcaoPenal());  

		stSql+= ",ID_CIDADE_ORIGEM = ?";		 ps.adicionarLong(dados.getId_CidadeOrigem());  

		stSql+= ",ESTADO_ORIGEM = ?";		 ps.adicionarString(dados.getEstadoOrigem());  

		stSql+= ",UF_ORIGEM = ?";		 ps.adicionarString(dados.getUfOrigem());  

		stSql+= ",DATA_ACORDAO = ?";		 ps.adicionarDate(dados.getDataAcordao());  

		stSql+= ",DATA_DIST = ?";		 ps.adicionarDate(dados.getDataDistribuicao());  

		stSql+= ",DATA_PRONUNCIA = ?";		 ps.adicionarDate(dados.getDataPronuncia());  

		stSql+= ",DATA_SENTENCA = ?";		 ps.adicionarDate(dados.getDataSentenca());  

		stSql+= ",DATA_TRANS_JULGADO = ?";		 ps.adicionarDate(dados.getDataTransitoJulgado());  

		stSql+= ",DATA_DENUNCIA = ?";		 ps.adicionarDate(dados.getDataDenuncia());  

		stSql+= ",DATA_ADMONITORIA = ?";		 ps.adicionarDate(dados.getDataAdmonitoria());  

		stSql+= ",DATA_INICIO_CUMP_PENA = ?";		 ps.adicionarDate(dados.getDataInicioCumprimentoPena());  

		stSql+= ",NUMERO_ACAO_PENAL = ?";		 ps.adicionarLong(dados.getNumeroAcaoPenal());  

		stSql+= ",VARA_ORIGEM = ?";		 ps.adicionarString(dados.getVaraOrigem());  

		stSql += " WHERE ID_PROC_EXE  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProcessoExecucaoexcluir()");

		stSql= "DELETE FROM PROJUDI.PROC_EXE";
		stSql += " WHERE ID_PROC_EXE = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ProcessoExecucaoDt consultarId(String id_processoexecucao )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ProcessoExecucaoDt Dados=null;
		////System.out.println("....ps-ConsultaId_ProcessoExecucao)");

		stSql= "SELECT * FROM PROJUDI.VIEW_PROC_EXE WHERE ID_PROC_EXE = ?";		ps.adicionarLong(id_processoexecucao); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_ProcessoExecucao  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ProcessoExecucaoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ProcessoExecucaoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_PROC_EXE"));
		Dados.setProcessoExecucaoPenalNumero(rs.getString("PROC_EXE_PENAL_NUMERO"));
		Dados.setProcessoAcaoPenalNumero( rs.getString("PROC_ACAO_PENAL_NUMERO"));
		Dados.setId_ProcessoExecucaoPenal( rs.getString("ID_PROC_EXE_PENAL"));
		Dados.setId_ProcessoAcaoPenal( rs.getString("ID_PROC_ACAO_PENAL"));
		Dados.setId_CidadeOrigem( rs.getString("ID_CIDADE_ORIGEM"));
		Dados.setCidadeOrigem( rs.getString("CIDADE_ORIGEM"));
		Dados.setEstadoOrigem( rs.getString("ESTADO_ORIGEM"));
		Dados.setUfOrigem( rs.getString("UF_ORIGEM"));
		Dados.setDataAcordao( Funcoes.FormatarData(rs.getDateTime("DATA_ACORDAO")));
		Dados.setDataDistribuicao( Funcoes.FormatarData(rs.getDateTime("DATA_DIST")));
		Dados.setDataPronuncia( Funcoes.FormatarData(rs.getDateTime("DATA_PRONUNCIA")));
		Dados.setDataSentenca( Funcoes.FormatarData(rs.getDateTime("DATA_SENTENCA")));
		Dados.setDataTransitoJulgado( Funcoes.FormatarData(rs.getDateTime("DATA_TRANS_JULGADO")));
		Dados.setDataTransitoJulgadoMP( Funcoes.FormatarData(rs.getDateTime("DATA_TRANS_JULGADO_MP")));
		Dados.setDataDenuncia( Funcoes.FormatarData(rs.getDateTime("DATA_DENUNCIA")));
		Dados.setDataAdmonitoria( Funcoes.FormatarData(rs.getDateTime("DATA_ADMONITORIA")));
		Dados.setDataInicioCumprimentoPena( Funcoes.FormatarData(rs.getDateTime("DATA_INICIO_CUMP_PENA")));
		Dados.setNumeroAcaoPenal( rs.getString("NUMERO_ACAO_PENAL"));
		Dados.setVaraOrigem( rs.getString("VARA_ORIGEM"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoProcessoExecucao()");

		stSql= "SELECT ID_PROC_EXE, PROC_EXE_PENAL_NUMERO FROM PROJUDI.VIEW_PROC_EXE WHERE PROC_EXE_PENAL_NUMERO LIKE ?";
		stSql+= " ORDER BY ProcessoExecucaoPenalNumero ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoProcessoExecucao  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ProcessoExecucaoDt obTemp = new ProcessoExecucaoDt();
				obTemp.setId(rs1.getString("ID_PROC_EXE"));
				obTemp.setProcessoExecucaoPenalNumero(rs1.getString("PROC_EXE_PENAL_NUMERO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PROC_EXE WHERE ProcessoExecucaoPenalNumero LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..ProcessoExecucaoPsGen.consultarDescricao() Operação realizada com sucesso");
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

} 
