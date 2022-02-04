package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoTipoProcessoSubtipoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ProcessoTipoProcessoSubtipoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 4440444105100188942L;

	//---------------------------------------------------------
	public ProcessoTipoProcessoSubtipoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(ProcessoTipoProcessoSubtipoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProcessoTipoProcessoSubtipoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.PROC_TIPO_PROC_SUBTIPO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_ProcessoSubtipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC_SUBTIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ProcessoSubtipo());  

			stVirgula=",";
		}
		if ((dados.getId_ProcessoTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ProcessoTipo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_PROC_TIPO_PROC_SUBTIPO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(ProcessoTipoProcessoSubtipoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psProcessoTipoProcessoSubtipoalterar()");

		stSql= "UPDATE PROJUDI.PROC_TIPO_PROC_SUBTIPO SET  ";
		stSql+= "ID_PROC_SUBTIPO = ?";		 ps.adicionarLong(dados.getId_ProcessoSubtipo());  

		stSql+= ",ID_PROC_TIPO = ?";		 ps.adicionarLong(dados.getId_ProcessoTipo());  

		stSql += " WHERE ID_PROC_TIPO_PROC_SUBTIPO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProcessoTipoProcessoSubtipoexcluir()");

		stSql= "DELETE FROM PROJUDI.PROC_TIPO_PROC_SUBTIPO";
		stSql += " WHERE ID_PROC_TIPO_PROC_SUBTIPO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ProcessoTipoProcessoSubtipoDt consultarId(String id_processotipoprocessosubtipo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ProcessoTipoProcessoSubtipoDt Dados=null;
		////System.out.println("....ps-ConsultaId_ProcessoTipoProcessoSubtipo)");

		stSql= "SELECT * FROM PROJUDI.VIEW_PROC_TIPO_PROC_SUBTIPO WHERE ID_PROC_TIPO_PROC_SUBTIPO = ?";		ps.adicionarLong(id_processotipoprocessosubtipo); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_ProcessoTipoProcessoSubtipo  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ProcessoTipoProcessoSubtipoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ProcessoTipoProcessoSubtipoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_PROC_TIPO_PROC_SUBTIPO"));
		Dados.setProcessoSubtipo(rs.getString("PROC_SUBTIPO"));
		Dados.setId_ProcessoSubtipo( rs.getString("ID_PROC_SUBTIPO"));
		Dados.setId_ProcessoTipo( rs.getString("ID_PROC_TIPO"));
		Dados.setProcessoTipo( rs.getString("PROC_TIPO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoProcessoTipoProcessoSubtipo()");

		stSql= "SELECT ID_PROC_TIPO_PROC_SUBTIPO, PROC_SUBTIPO FROM PROJUDI.VIEW_PROC_TIPO_PROC_SUBTIPO WHERE PROC_SUBTIPO LIKE ?";
		stSql+= " ORDER BY PROC_SUBTIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoProcessoTipoProcessoSubtipo  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ProcessoTipoProcessoSubtipoDt obTemp = new ProcessoTipoProcessoSubtipoDt();
				obTemp.setId(rs1.getString("ID_PROC_TIPO_PROC_SUBTIPO"));
				obTemp.setProcessoSubtipo(rs1.getString("PROC_SUBTIPO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PROC_TIPO_PROC_SUBTIPO WHERE PROC_SUBTIPO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..ProcessoTipoProcessoSubtipoPsGen.consultarDescricao() Operação realizada com sucesso");
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

} 
