package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoDebitoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ProcessoDebitoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -6217534806643956268L;

	//---------------------------------------------------------
	public ProcessoDebitoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(ProcessoDebitoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProcessoDebitoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.PROC_DEBITO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getProcessoDebito().length()>0)) {
			 stSqlCampos+=   stVirgula + "PROC_DEBITO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getProcessoDebito());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_PROC_DEBITO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(ProcessoDebitoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psProcessoDebitoalterar()");

		stSql= "UPDATE PROJUDI.PROC_DEBITO SET  ";
		stSql+= "PROC_DEBITO = ?";		 ps.adicionarString(dados.getProcessoDebito());  

		stSql += " WHERE ID_PROC_DEBITO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProcessoDebitoexcluir()");

		stSql= "DELETE FROM PROJUDI.PROC_DEBITO";
		stSql += " WHERE ID_PROC_DEBITO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ProcessoDebitoDt consultarId(String id_processodebito )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ProcessoDebitoDt Dados=null;
		////System.out.println("....ps-ConsultaId_ProcessoDebito)");

		stSql= "SELECT * FROM PROJUDI.VIEW_PROC_DEBITO WHERE ID_PROC_DEBITO = ?";		ps.adicionarLong(id_processodebito); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_ProcessoDebito  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ProcessoDebitoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ProcessoDebitoDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_PROC_DEBITO"));
		Dados.setProcessoDebito(rs.getString("PROC_DEBITO"));
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

		stSql= "SELECT ID_PROC_DEBITO, PROC_DEBITO FROM PROJUDI.VIEW_PROC_DEBITO WHERE PROC_DEBITO LIKE ?";
		stSql+= " ORDER BY PROC_DEBITO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoProcessoDebito  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ProcessoDebitoDt obTemp = new ProcessoDebitoDt();
				obTemp.setId(rs1.getString("ID_PROC_DEBITO"));
				obTemp.setProcessoDebito(rs1.getString("PROC_DEBITO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PROC_DEBITO WHERE PROC_DEBITO LIKE ?";
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
