package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoStatusDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ProcessoStatusPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 3457745673647083398L;

	//---------------------------------------------------------
	public ProcessoStatusPsGen() {

	}



//---------------------------------------------------------
	public void inserir(ProcessoStatusDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProcessoStatusinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.PROC_STATUS ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getProcessoStatus().length()>0)) {
			 stSqlCampos+=   stVirgula + "PROC_STATUS " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getProcessoStatus());  

			stVirgula=",";
		}
		if ((dados.getProcessoStatusCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "PROC_STATUS_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getProcessoStatusCodigo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_PROC_STATUS",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(ProcessoStatusDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psProcessoStatusalterar()");

		stSql= "UPDATE PROJUDI.PROC_STATUS SET  ";
		stSql+= "PROC_STATUS = ?";		 ps.adicionarString(dados.getProcessoStatus());  

		stSql+= ",PROC_STATUS_CODIGO = ?";		 ps.adicionarLong(dados.getProcessoStatusCodigo());  

		stSql += " WHERE ID_PROC_STATUS  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProcessoStatusexcluir()");

		stSql= "DELETE FROM PROJUDI.PROC_STATUS";
		stSql += " WHERE ID_PROC_STATUS = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ProcessoStatusDt consultarId(String id_processostatus )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ProcessoStatusDt Dados=null;
		////System.out.println("....ps-ConsultaId_ProcessoStatus)");

		stSql= "SELECT * FROM PROJUDI.VIEW_PROC_STATUS WHERE ID_PROC_STATUS = ?";		ps.adicionarLong(id_processostatus); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_ProcessoStatus  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ProcessoStatusDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ProcessoStatusDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_PROC_STATUS"));
		Dados.setProcessoStatus(rs.getString("PROC_STATUS"));
		Dados.setProcessoStatusCodigo( rs.getString("PROC_STATUS_CODIGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoProcessoStatus()");

		stSql= "SELECT ID_PROC_STATUS, PROC_STATUS FROM PROJUDI.VIEW_PROC_STATUS WHERE PROC_STATUS LIKE ?";
		stSql+= " ORDER BY PROC_STATUS ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoProcessoStatus  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ProcessoStatusDt obTemp = new ProcessoStatusDt();
				obTemp.setId(rs1.getString("ID_PROC_STATUS"));
				obTemp.setProcessoStatus(rs1.getString("PROC_STATUS"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PROC_STATUS WHERE PROC_STATUS LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..ProcessoStatusPsGen.consultarDescricao() Operação realizada com sucesso");
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

} 
