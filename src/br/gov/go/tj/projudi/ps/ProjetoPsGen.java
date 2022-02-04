package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProjetoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ProjetoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -4750567544183737329L;

	//---------------------------------------------------------
	public ProjetoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(ProjetoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProjetoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.PROJETO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getProjeto().length()>0)) {
			 stSqlCampos+=   stVirgula + "PROJETO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getProjeto());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_PROJETO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(ProjetoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psProjetoalterar()");

		stSql= "UPDATE PROJUDI.PROJETO SET  ";
		stSql+= "PROJETO = ?";		 ps.adicionarString(dados.getProjeto());  

		stSql += " WHERE ID_PROJETO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProjetoexcluir()");

		stSql= "DELETE FROM PROJUDI.PROJETO";
		stSql += " WHERE ID_PROJETO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ProjetoDt consultarId(String id_projeto )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ProjetoDt Dados=null;
		////System.out.println("....ps-ConsultaId_Projeto)");

		stSql= "SELECT * FROM PROJUDI.VIEW_PROJETO WHERE ID_PROJETO = ?";		ps.adicionarLong(id_projeto); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_Projeto  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ProjetoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ProjetoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_PROJETO"));
		Dados.setProjeto(rs.getString("PROJETO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoProjeto()");

		stSql= "SELECT ID_PROJETO, PROJETO FROM PROJUDI.VIEW_PROJETO WHERE PROJETO LIKE ?";
		stSql+= " ORDER BY PROJETO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoProjeto  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ProjetoDt obTemp = new ProjetoDt();
				obTemp.setId(rs1.getString("ID_PROJETO"));
				obTemp.setProjeto(rs1.getString("PROJETO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PROJETO WHERE PROJETO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..ProjetoPsGen.consultarDescricao() Operação realizada com sucesso");
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

} 
