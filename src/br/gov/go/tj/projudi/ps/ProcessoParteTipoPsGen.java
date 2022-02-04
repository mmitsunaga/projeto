package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoParteTipoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ProcessoParteTipoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 3906114364942208233L;

	//---------------------------------------------------------
	public ProcessoParteTipoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(ProcessoParteTipoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProcessoParteTipoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.PROC_PARTE_TIPO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getProcessoParteTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "PROC_PARTE_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getProcessoParteTipo());  

			stVirgula=",";
		}
		if ((dados.getProcessoParteTipoCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "PROC_PARTE_TIPO_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getProcessoParteTipoCodigo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_PROC_PARTE_TIPO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(ProcessoParteTipoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psProcessoParteTipoalterar()");

		stSql= "UPDATE PROJUDI.PROC_PARTE_TIPO SET  ";
		stSql+= "PROC_PARTE_TIPO = ?";		 ps.adicionarString(dados.getProcessoParteTipo());  

		stSql+= ",PROC_PARTE_TIPO_CODIGO = ?";		 ps.adicionarLong(dados.getProcessoParteTipoCodigo());  

		stSql += " WHERE ID_PROC_PARTE_TIPO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProcessoParteTipoexcluir()");

		stSql= "DELETE FROM PROJUDI.PROC_PARTE_TIPO";
		stSql += " WHERE ID_PROC_PARTE_TIPO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ProcessoParteTipoDt consultarId(String id_processopartetipo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ProcessoParteTipoDt Dados=null;
		////System.out.println("....ps-ConsultaId_ProcessoParteTipo)");

		stSql= "SELECT * FROM PROJUDI.VIEW_PROC_PARTE_TIPO WHERE ID_PROC_PARTE_TIPO = ?";		ps.adicionarLong(id_processopartetipo); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_ProcessoParteTipo  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ProcessoParteTipoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ProcessoParteTipoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_PROC_PARTE_TIPO"));
		Dados.setProcessoParteTipo(rs.getString("PROC_PARTE_TIPO"));
		Dados.setProcessoParteTipoCodigo( rs.getString("PROC_PARTE_TIPO_CODIGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoProcessoParteTipo()");

		stSql= "SELECT ID_PROC_PARTE_TIPO, PROC_PARTE_TIPO FROM PROJUDI.VIEW_PROC_PARTE_TIPO WHERE PROC_PARTE_TIPO LIKE ?";
		stSql+= " ORDER BY PROC_PARTE_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoProcessoParteTipo  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ProcessoParteTipoDt obTemp = new ProcessoParteTipoDt();
				obTemp.setId(rs1.getString("ID_PROC_PARTE_TIPO"));
				obTemp.setProcessoParteTipo(rs1.getString("PROC_PARTE_TIPO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PROC_PARTE_TIPO WHERE PROC_PARTE_TIPO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..ProcessoParteTipoPsGen.consultarDescricao() Operação realizada com sucesso");
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

} 
