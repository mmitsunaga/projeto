package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ArquivoTipoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -6434629204216453956L;

	//---------------------------------------------------------
	public ArquivoTipoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(ArquivoTipoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psArquivoTipoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.ARQ_TIPO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getArquivoTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ARQ_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getArquivoTipo());  

			stVirgula=",";
		}
		if ((dados.getArquivoTipoCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ARQ_TIPO_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getArquivoTipoCodigo());  

			stVirgula=",";
		}
		if ((dados.getPublico().length()>0)) {
			 stSqlCampos+=   stVirgula + "PUBLICO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBoolean(dados.getPublico());  

			stVirgula=",";
		}
		if ((dados.getDje().length()>0)) {
			 stSqlCampos+=   stVirgula + "DJE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBoolean(dados.getDje());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_ARQ_TIPO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(ArquivoTipoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psArquivoTipoalterar()");

		stSql= "UPDATE PROJUDI.ARQ_TIPO SET  ";
		stSql+= "ARQ_TIPO = ?";		 ps.adicionarString(dados.getArquivoTipo());  

		stSql+= ",ARQ_TIPO_CODIGO = ?";		 ps.adicionarLong(dados.getArquivoTipoCodigo());  

		stSql+= ",PUBLICO = ?";		 ps.adicionarBoolean(dados.getPublico());  
		
		stSql+= ",DJE = ?";		 ps.adicionarBoolean(dados.getDje());

		stSql += " WHERE ID_ARQ_TIPO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psArquivoTipoexcluir()");

		stSql= "DELETE FROM PROJUDI.ARQ_TIPO";
		stSql += " WHERE ID_ARQ_TIPO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ArquivoTipoDt consultarId(String id_arquivotipo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ArquivoTipoDt Dados=null;
		////System.out.println("....ps-ConsultaId_ArquivoTipo)");

		stSql= "SELECT * FROM PROJUDI.VIEW_ARQ_TIPO WHERE ID_ARQ_TIPO = ?";		ps.adicionarLong(id_arquivotipo); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_ArquivoTipo  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ArquivoTipoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ArquivoTipoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_ARQ_TIPO"));
		Dados.setArquivoTipo(rs.getString("ARQ_TIPO"));
		Dados.setArquivoTipoCodigo( rs.getString("ARQ_TIPO_CODIGO"));
		Dados.setPublico( Funcoes.FormatarLogico(rs.getString("PUBLICO")));
		Dados.setDje( Funcoes.FormatarLogico(rs.getString("DJE")));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoArquivoTipo()");

		stSql= "SELECT ID_ARQ_TIPO, ARQ_TIPO FROM PROJUDI.VIEW_ARQ_TIPO WHERE ARQ_TIPO LIKE ?";
		stSql+= " ORDER BY ARQ_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoArquivoTipo  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ArquivoTipoDt obTemp = new ArquivoTipoDt();
				obTemp.setId(rs1.getString("ID_ARQ_TIPO"));
				obTemp.setArquivoTipo(rs1.getString("ARQ_TIPO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_ARQ_TIPO WHERE ARQ_TIPO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..ArquivoTipoPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
