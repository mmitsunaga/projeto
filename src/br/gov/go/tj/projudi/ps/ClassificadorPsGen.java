package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ClassificadorDt;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ClassificadorPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -7674969561882939796L;

	//---------------------------------------------------------
	public ClassificadorPsGen() {

	}



//---------------------------------------------------------
	public void inserir(ClassificadorDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psClassificadorinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.Classificador ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getClassificador().length()>0)) {
			 stSqlCampos+=   stVirgula + "CLASSIFICADOR " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getClassificador());  

			stVirgula=",";
		}
		if ((dados.getId_Serventia().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Serventia());  

			stVirgula=",";
		}
		if ((dados.getPrioridade().length()>0)) {
			 stSqlCampos+=   stVirgula + "PRIORI " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getPrioridade());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_CLASSIFICADOR",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(ClassificadorDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psClassificadoralterar()");

		stSql= "UPDATE PROJUDI.Classificador SET  ";
		stSql+= "CLASSIFICADOR = ?";		 ps.adicionarString(dados.getClassificador());  

		stSql+= ",ID_SERV = ?";		 ps.adicionarLong(dados.getId_Serventia());  

		stSql+= ",PRIORI = ?";		 ps.adicionarLong(dados.getPrioridade());  

		stSql += " WHERE ID_CLASSIFICADOR  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

	public void excluir( String chave) throws Exception {
		String stSql="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "DELETE FROM PROJUDI.Classificador";
		stSql += " WHERE ID_CLASSIFICADOR = ?";		ps.adicionarLong(chave); 

		try {
		executarUpdateDelete(stSql,ps);
		} catch (Exception e) {
			throw new MensagemException(e.getMessage());
		}
		
	} 

	public ClassificadorDt consultarId(String id_classificador )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ClassificadorDt Dados=null;
		////System.out.println("....ps-ConsultaId_Classificador)");

		stSql= "SELECT * FROM PROJUDI.VIEW_CLASSIFICADOR WHERE ID_CLASSIFICADOR = ?";		ps.adicionarLong(id_classificador); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_Classificador  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ClassificadorDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ClassificadorDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_CLASSIFICADOR"));
		Dados.setClassificador(rs.getString("CLASSIFICADOR"));
		Dados.setId_Serventia( rs.getString("ID_SERV"));
		Dados.setServentia( rs.getString("SERV"));
		Dados.setPrioridade( rs.getString("PRIORI"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		Dados.setServentiaCodigo( rs.getString("SERV_CODIGO"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoClassificador()");

		stSql= "SELECT ID_CLASSIFICADOR, CLASSIFICADOR FROM PROJUDI.VIEW_CLASSIFICADOR WHERE CLASSIFICADOR LIKE ?";
		stSql+= " ORDER BY CLASSIFICADOR ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoClassificador  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ClassificadorDt obTemp = new ClassificadorDt();
				obTemp.setId(rs1.getString("ID_CLASSIFICADOR"));
				obTemp.setClassificador(rs1.getString("CLASSIFICADOR"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_CLASSIFICADOR WHERE CLASSIFICADOR LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..ClassificadorPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
