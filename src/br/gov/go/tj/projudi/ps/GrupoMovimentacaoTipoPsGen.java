package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.GrupoMovimentacaoTipoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class GrupoMovimentacaoTipoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 7282166465994484965L;

	//---------------------------------------------------------
	public GrupoMovimentacaoTipoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(GrupoMovimentacaoTipoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psGrupoMovimentacaoTipoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.GRUPO_MOVI_TIPO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_Grupo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_GRUPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Grupo());  

			stVirgula=",";
		}
		if ((dados.getId_MovimentacaoTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_MOVI_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_MovimentacaoTipo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_GRUPO_MOVI_TIPO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(GrupoMovimentacaoTipoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psGrupoMovimentacaoTipoalterar()");

		stSql= "UPDATE PROJUDI.GRUPO_MOVI_TIPO SET  ";
		stSql+= "ID_GRUPO = ?";		 ps.adicionarLong(dados.getId_Grupo());  

		stSql+= ",ID_MOVI_TIPO = ?";		 ps.adicionarLong(dados.getId_MovimentacaoTipo());  

		stSql += " WHERE ID_GRUPO_MOVI_TIPO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psGrupoMovimentacaoTipoexcluir()");

		stSql= "DELETE FROM PROJUDI.GRUPO_MOVI_TIPO";
		stSql += " WHERE ID_GRUPO_MOVI_TIPO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public GrupoMovimentacaoTipoDt consultarId(String id_grupomovimentacaotipo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		GrupoMovimentacaoTipoDt Dados=null;
		////System.out.println("....ps-ConsultaId_GrupoMovimentacaoTipo)");

		stSql= "SELECT * FROM PROJUDI.VIEW_GRUPO_MOVI_TIPO WHERE ID_GRUPO_MOVI_TIPO = ?";		ps.adicionarLong(id_grupomovimentacaotipo); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_GrupoMovimentacaoTipo  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new GrupoMovimentacaoTipoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( GrupoMovimentacaoTipoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_GRUPO_MOVI_TIPO"));
		Dados.setGrupo(rs.getString("GRUPO"));
		Dados.setId_Grupo( rs.getString("ID_GRUPO"));
		Dados.setId_MovimentacaoTipo( rs.getString("ID_MOVI_TIPO"));
		Dados.setMovimentacaoTipo( rs.getString("MOVI_TIPO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		Dados.setGrupoCodigo( rs.getString("GRUPO_CODIGO"));
		Dados.setMovimentacaoTipoCodigo( rs.getString("MOVI_TIPO_CODIGO"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoGrupoMovimentacaoTipo()");

		stSql= "SELECT ID_GRUPO_MOVI_TIPO, GRUPO FROM PROJUDI.VIEW_GRUPO_MOVI_TIPO WHERE GRUPO LIKE ?";
		stSql+= " ORDER BY GRUPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoGrupoMovimentacaoTipo  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				GrupoMovimentacaoTipoDt obTemp = new GrupoMovimentacaoTipoDt();
				obTemp.setId(rs1.getString("ID_GRUPO_MOVI_TIPO"));
				obTemp.setGrupo(rs1.getString("GRUPO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_GRUPO_MOVI_TIPO WHERE GRUPO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..GrupoMovimentacaoTipoPsGen.consultarDescricao() Operação realizada com sucesso");
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

} 
