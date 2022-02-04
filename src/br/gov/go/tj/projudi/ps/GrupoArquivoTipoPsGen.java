package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.GrupoArquivoTipoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class GrupoArquivoTipoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 8851874409393904821L;

	//---------------------------------------------------------
	public GrupoArquivoTipoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(GrupoArquivoTipoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psGrupoArquivoTipoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.GRUPO_ARQ_TIPO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_Grupo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_GRUPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Grupo());  

			stVirgula=",";
		}
		if ((dados.getId_ArquivoTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_ARQ_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ArquivoTipo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_GRUPO_ARQ_TIPO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(GrupoArquivoTipoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psGrupoArquivoTipoalterar()");

		stSql= "UPDATE PROJUDI.GRUPO_ARQ_TIPO SET  ";
		stSql+= "ID_GRUPO = ?";		 ps.adicionarLong(dados.getId_Grupo());  

		stSql+= ",ID_ARQ_TIPO = ?";		 ps.adicionarLong(dados.getId_ArquivoTipo());  

		stSql += " WHERE ID_GRUPO_ARQ_TIPO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psGrupoArquivoTipoexcluir()");

		stSql= "DELETE FROM PROJUDI.GRUPO_ARQ_TIPO";
		stSql += " WHERE ID_GRUPO_ARQ_TIPO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public GrupoArquivoTipoDt consultarId(String id_grupoarquivotipo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		GrupoArquivoTipoDt Dados=null;
		////System.out.println("....ps-ConsultaId_GrupoArquivoTipo)");

		stSql= "SELECT * FROM PROJUDI.VIEW_GRUPO_ARQ_TIPO WHERE ID_GRUPO_ARQ_TIPO = ?";		ps.adicionarLong(id_grupoarquivotipo); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_GrupoArquivoTipo  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new GrupoArquivoTipoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( GrupoArquivoTipoDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_GRUPO_ARQ_TIPO"));
		Dados.setGrupo(rs.getString("GRUPO"));
		Dados.setId_Grupo( rs.getString("ID_GRUPO"));
		Dados.setId_ArquivoTipo( rs.getString("ID_ARQ_TIPO"));
		Dados.setArquivoTipo( rs.getString("ARQ_TIPO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		Dados.setGrupoCodigo( rs.getString("GRUPO_CODIGO"));
		Dados.setArquivoTipoCodigo( rs.getString("ARQ_TIPO_CODIGO"));

	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoGrupoArquivoTipo()");

		stSql= "SELECT ID_GRUPO_ARQ_TIPO, GRUPO FROM PROJUDI.VIEW_GRUPO_ARQ_TIPO WHERE GRUPO LIKE ?";
		stSql+= " ORDER BY GRUPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoGrupoArquivoTipo  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				GrupoArquivoTipoDt obTemp = new GrupoArquivoTipoDt();
				obTemp.setId(rs1.getString("ID_GRUPO_ARQ_TIPO"));
				obTemp.setGrupo(rs1.getString("GRUPO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_GRUPO_ARQ_TIPO WHERE GRUPO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..GrupoArquivoTipoPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
