package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ProcessoTipoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -6008111131655050763L;

	//---------------------------------------------------------
	public ProcessoTipoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(ProcessoTipoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProcessoTipoinserir()");
		stSqlCampos= "INSERT INTO projudi.PROC_TIPO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getProcessoTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "PROC_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getProcessoTipo());  

			stVirgula=",";
		}
		if ((dados.getProcessoTipoCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "PROC_TIPO_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getProcessoTipoCodigo());  

			stVirgula=",";
		}
		if ((dados.getOrdem2Grau().length()>0)) {
			 stSqlCampos+=   stVirgula + "ORDEM_2_GRAU " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getOrdem2Grau());  

			stVirgula=",";
		}
		if ((dados.getPublico().length()>0)) {
			 stSqlCampos+=   stVirgula + "PUBLICO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBoolean(dados.getPublico());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_PROC_TIPO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(ProcessoTipoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psProcessoTipoalterar()");

		stSql= "UPDATE projudi.PROC_TIPO SET  ";
		stSql+= "PROC_TIPO = ?";		 ps.adicionarString(dados.getProcessoTipo());  

		stSql+= ",PROC_TIPO_CODIGO = ?";		 ps.adicionarLong(dados.getProcessoTipoCodigo()); 
		
		stSql+= ",ORDEM_2_GRAU = ?";		 ps.adicionarLong(dados.getOrdem2Grau()); 
		
		stSql+= ",PUBLICO = ?";		 ps.adicionarBoolean(dados.getPublico());  

		stSql += " WHERE ID_PROC_TIPO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProcessoTipoexcluir()");

		stSql= "DELETE FROM projudi.PROC_TIPO";
		stSql += " WHERE ID_PROC_TIPO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ProcessoTipoDt consultarId(String id_processotipo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ProcessoTipoDt Dados=null;
		////System.out.println("....ps-ConsultaId_ProcessoTipo)");

		stSql= "SELECT * FROM projudi.VIEW_PROC_TIPO WHERE ID_PROC_TIPO = ?";		ps.adicionarLong(id_processotipo); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_ProcessoTipo  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ProcessoTipoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ProcessoTipoDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_PROC_TIPO"));
		Dados.setProcessoTipo(rs.getString("PROC_TIPO"));
		Dados.setProcessoTipoCodigo( rs.getString("PROC_TIPO_CODIGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		Dados.setPublico( Funcoes.FormatarLogico(rs.getString("PUBLICO")));
		Dados.setOrdem2Grau( rs.getString("ORDEM_2_GRAU"));
		
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoProcessoTipo()");

		stSql= "SELECT ID_PROC_TIPO, PROC_TIPO FROM projudi.VIEW_PROC_TIPO WHERE PROC_TIPO LIKE ?";
		stSql+= " ORDER BY PROC_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoProcessoTipo  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ProcessoTipoDt obTemp = new ProcessoTipoDt();
				obTemp.setId(rs1.getString("ID_PROC_TIPO"));
				obTemp.setProcessoTipo(rs1.getString("PROC_TIPO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM projudi.VIEW_PROC_TIPO WHERE PROC_TIPO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..ProcessoTipoPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
