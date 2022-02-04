package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoDebitoStatusDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ProcessoDebitoStatusPsGen extends Persistencia {

	/**
	 * 
	 */
	private static final long serialVersionUID = -290260362200365153L;

	//---------------------------------------------------------
	public ProcessoDebitoStatusPsGen() {

	}

//---------------------------------------------------------
	public void inserir(ProcessoDebitoStatusDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProcessoDebitoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.PROC_DEBITO_STATUS ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getProcessoDebitoStatus().length()>0)) {
			 stSqlCampos+=   stVirgula + "PROC_DEBITO_STATUS " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getProcessoDebitoStatus());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_PROC_DEBITO_STATUS",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(ProcessoDebitoStatusDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psProcessoDebitoalterar()");

		stSql= "UPDATE PROJUDI.PROC_DEBITO_STATUS SET  ";
		stSql+= "PROC_DEBITO_STATUS = ?";		 ps.adicionarString(dados.getProcessoDebitoStatus());  
		stSql+= ", CODIGO_TEMP = ?"; ps.adicionarLong(dados.getCodigoTemp());

		stSql += " WHERE ID_PROC_DEBITO_STATUS  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProcessoDebitoexcluir()");

		stSql= "DELETE FROM PROJUDI.PROC_DEBITO_STATUS";
		stSql += " WHERE ID_PROC_DEBITO_STATUS = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ProcessoDebitoStatusDt consultarId(String id_processodebitostatus )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ProcessoDebitoStatusDt Dados=null;
		////System.out.println("....ps-ConsultaId_ProcessoDebito)");

		stSql= "SELECT * FROM PROJUDI.VIEW_PROC_DEBITO_STATUS WHERE ID_PROC_DEBITO_STATUS = ?";		ps.adicionarLong(id_processodebitostatus); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_ProcessoDebito  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ProcessoDebitoStatusDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ProcessoDebitoStatusDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_PROC_DEBITO_STATUS"));
		Dados.setProcessoDebitoStatus(rs.getString("PROC_DEBITO_STATUS"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoProcessoDebito()");

		stSql= "SELECT ID_PROC_DEBITO_STATUS, PROC_DEBITO_STATUS FROM PROJUDI.VIEW_PROC_DEBITO_STATUS WHERE PROC_DEBITO_STATUS LIKE ?";
		stSql+= " ORDER BY PROC_DEBITO_STATUS ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoProcessoDebito  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ProcessoDebitoStatusDt obTemp = new ProcessoDebitoStatusDt();
				obTemp.setId(rs1.getString("ID_PROC_DEBITO_STATUS"));
				obTemp.setProcessoDebitoStatus(rs1.getString("PROC_DEBITO_STATUS"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PROC_DEBITO_STATUS WHERE PROC_DEBITO_STATUS LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..ProcessoDebitoPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}
} 
