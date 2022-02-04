package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class GrupoTipoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -4622422845239886641L;

	//---------------------------------------------------------
	public GrupoTipoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(GrupoTipoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psGrupoTipoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.GRUPO_TIPO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getGrupoTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "GRUPO_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getGrupoTipo());  

			stVirgula=",";
		}
		if ((dados.getGrupoTipoCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "GRUPO_TIPO_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getGrupoTipoCodigo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_GRUPO_TIPO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(GrupoTipoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psGrupoTipoalterar()");

		stSql= "UPDATE PROJUDI.GRUPO_TIPO SET  ";
		stSql+= "GRUPO_TIPO = ?";		 ps.adicionarString(dados.getGrupoTipo());  

		stSql+= ",GRUPO_TIPO_CODIGO = ?";		 ps.adicionarLong(dados.getGrupoTipoCodigo());  

		stSql += " WHERE ID_GRUPO_TIPO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psGrupoTipoexcluir()");

		stSql= "DELETE FROM PROJUDI.GRUPO_TIPO";
		stSql += " WHERE ID_GRUPO_TIPO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public GrupoTipoDt consultarId(String id_grupotipo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		GrupoTipoDt Dados=null;
		////System.out.println("....ps-ConsultaId_GrupoTipo)");

		stSql= "SELECT * FROM PROJUDI.VIEW_GRUPO_TIPO WHERE ID_GRUPO_TIPO = ?";		ps.adicionarLong(id_grupotipo); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_GrupoTipo  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new GrupoTipoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return Dados; 
	}

	protected void associarDt( GrupoTipoDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_GRUPO_TIPO"));
		Dados.setGrupoTipo(rs.getString("GRUPO_TIPO"));
		Dados.setGrupoTipoCodigo( rs.getString("GRUPO_TIPO_CODIGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));

	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoGrupoTipo()");

		stSql= "SELECT ID_GRUPO_TIPO, GRUPO_TIPO FROM PROJUDI.VIEW_GRUPO_TIPO WHERE GRUPO_TIPO LIKE ?";
		stSql+= " ORDER BY GRUPO_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoGrupoTipo  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				GrupoTipoDt obTemp = new GrupoTipoDt();
				obTemp.setId(rs1.getString("ID_GRUPO_TIPO"));
				obTemp.setGrupoTipo(rs1.getString("GRUPO_TIPO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_GRUPO_TIPO WHERE GRUPO_TIPO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..GrupoTipoPsGen.consultarDescricao() Operação realizada com sucesso");
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception{

		String stSql="";
		String stTemp="";
		int qtdeColunas = 1;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_GRUPO_TIPO as id, GRUPO_TIPO as descricao1 FROM PROJUDI.VIEW_GRUPO_TIPO WHERE GRUPO_TIPO LIKE ?";
		stSql+= " ORDER BY GRUPO_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_GRUPO_TIPO WHERE GRUPO_TIPO LIKE ?";
			rs2 = consultar(stSql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp; 
	}

} 
