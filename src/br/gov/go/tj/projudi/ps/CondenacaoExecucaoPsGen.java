package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.CondenacaoExecucaoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class CondenacaoExecucaoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -2279985139828387050L;

	//---------------------------------------------------------
	public CondenacaoExecucaoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(CondenacaoExecucaoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psCondenacaoExecucaoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.CONDENACAO_EXE ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getTempoPena().length()>0)) {
			 stSqlCampos+=   stVirgula + "TEMPO_PENA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getTempoPena());  

			stVirgula=",";
		}
		if ((dados.getReincidente().length()>0)) {
			 stSqlCampos+=   stVirgula + "REINCIDENTE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBoolean(dados.getReincidente());  

			stVirgula=",";
		}
		if ((dados.getDataFato().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_FATO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDate(dados.getDataFato());  

			stVirgula=",";
		}
		if ((dados.getId_ProcessoExecucao().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC_EXE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ProcessoExecucao());  

			stVirgula=",";
		}
		if ((dados.getId_CrimeExecucao().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_CRIME_EXE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_CrimeExecucao());  

			stVirgula=",";
		}
		if ((dados.getId_CondenacaoExecucaoSituacao().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_CONDENACAO_EXE_SIT " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_CondenacaoExecucaoSituacao());  

			stVirgula=",";
		}
		
		stSqlCampos+=   stVirgula + "TEMPO_CUMPRIDO_EXTINTO " ;
		stSqlValores+=   stVirgula + "? " ;
		ps.adicionarLong(dados.getTempoCumpridoExtintoDias());  
		stVirgula=",";
		
		stSqlCampos+=   stVirgula + "OBSERVACAO " ;
		stSqlValores+=   stVirgula + "? " ;
		ps.adicionarString(dados.getObservacao());  
		stVirgula=",";
		
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_CONDENACAO_EXE",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(CondenacaoExecucaoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psCondenacaoExecucaoalterar()");

		stSql= "UPDATE PROJUDI.CONDENACAO_EXE SET  ";
		stSql+= "TEMPO_PENA = ?";		 ps.adicionarLong(dados.getTempoPena());  

		stSql+= ",REINCIDENTE = ?";		 ps.adicionarBoolean(dados.getReincidente());  

		stSql+= ",DATA_FATO = ?";		 ps.adicionarDate(dados.getDataFato());  

		stSql+= ",ID_PROC_EXE = ?";		 ps.adicionarLong(dados.getId_ProcessoExecucao());  
		
		stSql+= ",ID_CRIME_EXE = ?";		 ps.adicionarLong(dados.getId_CrimeExecucao());  

		stSql+= ",ID_CONDENACAO_EXE_SIT = ?";		 ps.adicionarLong(dados.getId_CondenacaoExecucaoSituacao());  

		stSql += " WHERE ID_CONDENACAO_EXE  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psCondenacaoExecucaoexcluir()");

		stSql= "DELETE FROM PROJUDI.CONDENACAO_EXE";
		stSql += " WHERE ID_CONDENACAO_EXE = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public CondenacaoExecucaoDt consultarId(String id_condenacaoexecucao )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		CondenacaoExecucaoDt Dados=null;
		////System.out.println("....ps-ConsultaId_CondenacaoExecucao)");

		stSql= "SELECT * FROM PROJUDI.VIEW_CONDENACAO_EXE WHERE ID_CONDENACAO_EXE = ?";		ps.adicionarLong(id_condenacaoexecucao); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_CondenacaoExecucao  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new CondenacaoExecucaoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( CondenacaoExecucaoDt Dados, ResultSetTJGO rs )  throws Exception {
		
		Dados.setId(rs.getString("ID_CONDENACAO_EXE"));
		Dados.setCrimeExecucao(rs.getString("CRIME_EXE"));
		Dados.setTempoPena( rs.getString("TEMPO_PENA"));
		Dados.setReincidente( Funcoes.FormatarLogico(rs.getString("REINCIDENTE")));
		Dados.setDataFato( Funcoes.FormatarData(rs.getDateTime("DATA_FATO")));
		Dados.setId_ProcessoExecucao( rs.getString("ID_PROC_EXE"));
		Dados.setProcessoNumero( rs.getString("PROC_NUMERO"));
		Dados.setId_CrimeExecucao( rs.getString("ID_CRIME_EXE"));		
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoCondenacaoExecucao()");

		stSql= "SELECT ID_CONDENACAO_EXE, CRIME_EXE FROM PROJUDI.VIEW_CONDENACAO_EXE WHERE CRIME_EXE LIKE ?";
		stSql+= " ORDER BY CRIME_EXE ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoCondenacaoExecucao  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				CondenacaoExecucaoDt obTemp = new CondenacaoExecucaoDt();
				obTemp.setId(rs1.getString("ID_CONDENACAO_EXE"));
				obTemp.setCrimeExecucao(rs1.getString("CRIME_EXE"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_CONDENACAO_EXE WHERE CRIME_EXE LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..CondenacaoExecucaoPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
