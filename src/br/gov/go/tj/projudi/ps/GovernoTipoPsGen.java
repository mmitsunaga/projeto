package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.GovernoTipoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class GovernoTipoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 1836039942965278594L;

	//---------------------------------------------------------
	public GovernoTipoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(GovernoTipoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psGovernoTipoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.GOVERNO_TIPO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getGovernoTipoCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "GOVERNO_TIPO_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getGovernoTipoCodigo());  

			stVirgula=",";
		}
		if ((dados.getGovernoTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "GOVERNO_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getGovernoTipo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_GOVERNO_TIPO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(GovernoTipoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psGovernoTipoalterar()");

		stSql= "UPDATE PROJUDI.GOVERNO_TIPO SET  ";
		stSql+= "GOVERNO_TIPO_CODIGO = ?";		 ps.adicionarLong(dados.getGovernoTipoCodigo());  

		stSql+= ",GOVERNO_TIPO = ?";		 ps.adicionarString(dados.getGovernoTipo());  

		stSql += " WHERE ID_GOVERNO_TIPO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psGovernoTipoexcluir()");

		stSql= "DELETE FROM PROJUDI.GOVERNO_TIPO";
		stSql += " WHERE ID_GOVERNO_TIPO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public GovernoTipoDt consultarId(String id_governotipo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		GovernoTipoDt Dados=null;
		////System.out.println("....ps-ConsultaId_GovernoTipo)");

		stSql= "SELECT * FROM PROJUDI.VIEW_GOVERNO_TIPO WHERE ID_GOVERNO_TIPO = ?";		ps.adicionarLong(id_governotipo); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_GovernoTipo  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new GovernoTipoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( GovernoTipoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_GOVERNO_TIPO"));
		Dados.setGovernoTipo(rs.getString("GOVERNO_TIPO"));
		Dados.setGovernoTipoCodigo( rs.getString("GOVERNO_TIPO_CODIGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoGovernoTipo()");

		stSql= "SELECT ID_GOVERNO_TIPO, GOVERNO_TIPO FROM PROJUDI.VIEW_GOVERNO_TIPO WHERE GOVERNO_TIPO LIKE ?";
		stSql+= " ORDER BY GOVERNO_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoGovernoTipo  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				GovernoTipoDt obTemp = new GovernoTipoDt();
				obTemp.setId(rs1.getString("ID_GOVERNO_TIPO"));
				obTemp.setGovernoTipo(rs1.getString("GOVERNO_TIPO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_GOVERNO_TIPO WHERE GOVERNO_TIPO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..GovernoTipoPsGen.consultarDescricao() Operação realizada com sucesso");
		} finally {
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


		stSql= "SELECT ID_GOVERNO_TIPO as id, GOVERNO_TIPO as descricao1 FROM PROJUDI.VIEW_GOVERNO_TIPO WHERE GOVERNO_TIPO LIKE ?";
		stSql+= " ORDER BY GOVERNO_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_GOVERNO_TIPO WHERE GOVERNO_TIPO LIKE ?";
			rs2 = consultar(stSql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp; 
	}

} 
