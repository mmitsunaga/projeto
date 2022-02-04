package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoSituacaoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ProcessoSituacaoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -3529203228141277220L;

	//---------------------------------------------------------
	public ProcessoSituacaoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(ProcessoSituacaoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProcessoSituacaoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.PROC_SIT ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getProcessoSituacao().length()>0)) {
			 stSqlCampos+=   stVirgula + "PROC_SIT " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getProcessoSituacao());  

			stVirgula=",";
		}
		if ((dados.getProcessoSituacaoCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "PROC_SIT_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getProcessoSituacaoCodigo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_PROC_SIT",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(ProcessoSituacaoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psProcessoSituacaoalterar()");

		stSql= "UPDATE PROJUDI.PROC_SIT SET  ";
		stSql+= "PROC_SIT = ?";		 ps.adicionarString(dados.getProcessoSituacao());  

		stSql+= ",PROC_SIT_CODIGO = ?";		 ps.adicionarLong(dados.getProcessoSituacaoCodigo());  

		stSql += " WHERE ID_PROC_SIT  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProcessoSituacaoexcluir()");

		stSql= "DELETE FROM PROJUDI.PROC_SIT";
		stSql += " WHERE ID_PROC_SIT = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ProcessoSituacaoDt consultarId(String id_processosituacao )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ProcessoSituacaoDt Dados=null;
		////System.out.println("....ps-ConsultaId_ProcessoSituacao)");

		stSql= "SELECT * FROM PROJUDI.VIEW_PROC_SIT WHERE ID_PROC_SIT = ?";		ps.adicionarLong(id_processosituacao); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_ProcessoSituacao  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ProcessoSituacaoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ProcessoSituacaoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_PROC_SIT"));
		Dados.setProcessoSituacao(rs.getString("PROC_SIT"));
		Dados.setProcessoSituacaoCodigo( rs.getString("PROC_SIT_CODIGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoProcessoSituacao()");

		stSql= "SELECT ID_PROC_SIT, PROC_SIT FROM PROJUDI.VIEW_PROC_SIT WHERE PROC_SIT LIKE ?";
		stSql+= " ORDER BY PROC_SIT ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoProcessoSituacao  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ProcessoSituacaoDt obTemp = new ProcessoSituacaoDt();
				obTemp.setId(rs1.getString("ID_PROC_SIT"));
				obTemp.setProcessoSituacao(rs1.getString("PROC_SIT"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PROC_SIT WHERE PROC_SIT LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..ProcessoSituacaoPsGen.consultarDescricao() Operação realizada com sucesso");
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

} 
