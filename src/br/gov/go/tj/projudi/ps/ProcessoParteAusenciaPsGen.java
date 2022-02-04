package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoParteAusenciaDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ProcessoParteAusenciaPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 6116479376737379735L;

	//---------------------------------------------------------
	public ProcessoParteAusenciaPsGen() {

	}



//---------------------------------------------------------
	public void inserir(ProcessoParteAusenciaDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProcessoParteAusenciainserir()");
		stSqlCampos= "INSERT INTO PROJUDI.PROC_PARTE_AUS ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getProcessoParteAusencia().length()>0)) {
			 stSqlCampos+=   stVirgula + "PROC_PARTE_AUS " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getProcessoParteAusencia());  

			stVirgula=",";
		}
		if ((dados.getProcessoParteAusenciaCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "PROC_PARTE_AUS_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getProcessoParteAusenciaCodigo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_PROC_PARTE_AUS",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(ProcessoParteAusenciaDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psProcessoParteAusenciaalterar()");

		stSql= "UPDATE PROJUDI.PROC_PARTE_AUS SET  ";
		stSql+= "PROC_PARTE_AUS = ?";		 ps.adicionarString(dados.getProcessoParteAusencia());  

		stSql+= ",PROC_PARTE_AUS_CODIGO = ?";		 ps.adicionarLong(dados.getProcessoParteAusenciaCodigo());  

		stSql += " WHERE ID_PROC_PARTE_AUS  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProcessoParteAusenciaexcluir()");

		stSql= "DELETE FROM PROJUDI.PROC_PARTE_AUS";
		stSql += " WHERE ID_PROC_PARTE_AUS = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ProcessoParteAusenciaDt consultarId(String id_processoparteausencia )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ProcessoParteAusenciaDt Dados=null;
		////System.out.println("....ps-ConsultaId_ProcessoParteAusencia)");

		stSql= "SELECT * FROM PROJUDI.VIEW_PROC_PARTE_AUS WHERE ID_PROC_PARTE_AUS = ?";		ps.adicionarLong(id_processoparteausencia); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_ProcessoParteAusencia  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ProcessoParteAusenciaDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ProcessoParteAusenciaDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_PROC_PARTE_AUS"));
		Dados.setProcessoParteAusencia(rs.getString("PROC_PARTE_AUS"));
		Dados.setProcessoParteAusenciaCodigo( rs.getString("PROC_PARTE_AUS_CODIGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));

	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoProcessoParteAusencia()");

		stSql= "SELECT ID_PROC_PARTE_AUS, PROC_PARTE_AUS FROM PROJUDI.VIEW_PROC_PARTE_AUS WHERE PROC_PARTE_AUS LIKE ?";
		stSql+= " ORDER BY PROC_PARTE_AUS ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoProcessoParteAusencia  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ProcessoParteAusenciaDt obTemp = new ProcessoParteAusenciaDt();
				obTemp.setId(rs1.getString("ID_PROC_PARTE_AUS"));
				obTemp.setProcessoParteAusencia(rs1.getString("PROC_PARTE_AUS"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PROC_PARTE_AUS WHERE PROC_PARTE_AUS LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..ProcessoParteAusenciaPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
