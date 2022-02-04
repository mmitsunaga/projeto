package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.MandadoTipoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class MandadoTipoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -5496511072080483610L;

	//---------------------------------------------------------
	public MandadoTipoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(MandadoTipoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psMandadoTipoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.MAND_TIPO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getMandadoTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "MAND_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getMandadoTipo());  

			stVirgula=",";
		}
		if ((dados.getMandadoTipoCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "MAND_TIPO_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getMandadoTipoCodigo());  

			stVirgula=",";
		}
//		if ((dados.getQuantidadeLocomocao() > -1)) {
//			 stSqlCampos+=   stVirgula + "QTDE_LOCOMOCAO " ;
//			 stSqlValores+=   stVirgula + "? " ;
//			 ps.adicionarLong(dados.getQuantidadeLocomocao());  
//
//			stVirgula=",";
//		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_MAND_TIPO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(MandadoTipoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psMandadoTipoalterar()");

		stSql= "UPDATE PROJUDI.MAND_TIPO SET  ";
		stSql+= "MAND_TIPO = ?";		 ps.adicionarString(dados.getMandadoTipo());  

		stSql+= ",MAND_TIPO_CODIGO = ?";		 ps.adicionarLong(dados.getMandadoTipoCodigo());  
		
//		stSql+= ",QTDE_LOCOMOCAO = ?";		 ps.adicionarLong(dados.getQuantidadeLocomocao());

		stSql += " WHERE ID_MAND_TIPO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psMandadoTipoexcluir()");

		stSql= "DELETE FROM PROJUDI.MAND_TIPO";
		stSql += " WHERE ID_MAND_TIPO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public MandadoTipoDt consultarId(String id_mandadotipo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		MandadoTipoDt Dados=null;
		////System.out.println("....ps-ConsultaId_MandadoTipo)");

		stSql= "SELECT * FROM PROJUDI.VIEW_MAND_TIPO WHERE ID_MAND_TIPO = ?";		ps.adicionarLong(id_mandadotipo); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_MandadoTipo  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new MandadoTipoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( MandadoTipoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_MAND_TIPO"));
		Dados.setMandadoTipo(rs.getString("MAND_TIPO"));
		Dados.setMandadoTipoCodigo( rs.getString("MAND_TIPO_CODIGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoMandadoTipo()");

		stSql= "SELECT ID_MAND_TIPO, MAND_TIPO FROM PROJUDI.VIEW_MAND_TIPO WHERE MAND_TIPO LIKE ?";
		stSql+= " ORDER BY MAND_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoMandadoTipo  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				MandadoTipoDt obTemp = new MandadoTipoDt();
				obTemp.setId(rs1.getString("ID_MAND_TIPO"));
				obTemp.setMandadoTipo(rs1.getString("MAND_TIPO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_MAND_TIPO WHERE MAND_TIPO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..MandadoTipoPsGen.consultarDescricao() Operação realizada com sucesso");
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


		stSql= "SELECT ID_MAND_TIPO as id, MAND_TIPO as descricao1 FROM PROJUDI.VIEW_MAND_TIPO WHERE MAND_TIPO LIKE ?";
		stSql+= " ORDER BY MAND_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_MAND_TIPO WHERE MAND_TIPO LIKE ?";
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
