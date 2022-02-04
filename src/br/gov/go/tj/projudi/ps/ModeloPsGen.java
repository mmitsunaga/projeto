package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ModeloPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 8552683480425728088L;

	//---------------------------------------------------------
	public ModeloPsGen() {

	}



//---------------------------------------------------------
	public void inserir(ModeloDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psModeloinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.MODELO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getModeloCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "MODELO_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getModeloCodigo());  

			stVirgula=",";
		}
		if ((dados.getModelo().length()>0)) {
			 stSqlCampos+=   stVirgula + "MODELO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getModelo());  

			stVirgula=",";
		}
		if ((dados.getTexto().length()>0)) {
			 stSqlCampos+=   stVirgula + "TEXTO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getTexto());  

			stVirgula=",";
		}
		if ((dados.getId_ArquivoTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_ARQ_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ArquivoTipo());  

			stVirgula=",";
		}
		if ((dados.getId_Serventia().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Serventia());  

			stVirgula=",";
		}
		if ((dados.getId_UsuarioServentia().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU_SERV " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_UsuarioServentia());  

			stVirgula=",";
		}
		if ((dados.getQtdLocomocao() != null && dados.getQtdLocomocao().length()>0)) {
			 stSqlCampos+=   stVirgula + "QTD_LOCOMOCAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getQtdLocomocao());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_MODELO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(ModeloDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psModeloalterar()");

		stSql= "UPDATE PROJUDI.MODELO SET  ";
		stSql+= "MODELO_CODIGO = ?";		 ps.adicionarLong(dados.getModeloCodigo());  

		stSql+= ",MODELO = ?";		 ps.adicionarString(dados.getModelo());  

		stSql+= ",TEXTO = ?";		 ps.adicionarString(dados.getTexto());  

		stSql+= ",ID_ARQ_TIPO = ?";		 ps.adicionarLong(dados.getId_ArquivoTipo());  

		stSql+= ",ID_SERV = ?";		 ps.adicionarLong(dados.getId_Serventia());  

		stSql+= ",QTD_LOCOMOCAO = ?"; ps.adicionarLong(dados.getQtdLocomocao());  
		
		stSql+= ",ID_USU_SERV = ?";		 ps.adicionarLong(dados.getId_UsuarioServentia());  

		stSql += " WHERE ID_MODELO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psModeloexcluir()");

		stSql= "DELETE FROM PROJUDI.MODELO";
		stSql += " WHERE ID_MODELO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ModeloDt consultarId(String id_modelo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ModeloDt Dados=null;
		////System.out.println("....ps-ConsultaId_Modelo)");

		stSql= "SELECT * FROM PROJUDI.VIEW_MODELO WHERE ID_MODELO = ?";		ps.adicionarLong(id_modelo); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_Modelo  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ModeloDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ModeloDt Dados, ResultSetTJGO rs1 )  throws Exception {
		
		Dados.setId(rs1.getString("ID_MODELO"));
		Dados.setModelo(rs1.getString("MODELO"));
		Dados.setId_Serventia( rs1.getString("ID_SERV"));
		Dados.setServentia( rs1.getString("SERV"));
		Dados.setId_ArquivoTipo( rs1.getString("ID_ARQ_TIPO"));
		Dados.setArquivoTipo( rs1.getString("ARQ_TIPO"));
		Dados.setId_UsuarioServentia( rs1.getString("ID_USU_SERV"));
		Dados.setUsuarioServentia( rs1.getString("USU_SERV"));
		Dados.setTexto( rs1.getString("TEXTO"));
		Dados.setCodigoTemp( rs1.getString("CODIGO_TEMP"));
		Dados.setServentiaCodigo( rs1.getString("SERV_CODIGO"));
		Dados.setModeloCodigo(rs1.getString( "MODELO_CODIGO" ) );
		Dados.setQtdLocomocao(rs1.getString( "QTD_LOCOMOCAO" ) );

	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoModelo()");

		stSql= "SELECT ID_MODELO, MODELO FROM PROJUDI.VIEW_MODELO WHERE MODELO LIKE ?";
		stSql+= " ORDER BY MODELO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoModelo  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ModeloDt obTemp = new ModeloDt();
				obTemp.setId(rs1.getString("ID_MODELO"));
				obTemp.setModelo(rs1.getString("MODELO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_MODELO WHERE MODELO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..ModeloPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
