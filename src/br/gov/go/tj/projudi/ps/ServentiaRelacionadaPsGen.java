package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ServentiaRelacionadaDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ServentiaRelacionadaPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -4352103956328084306L;

	//---------------------------------------------------------
	public ServentiaRelacionadaPsGen() {

	}



//---------------------------------------------------------
	public void inserir(ServentiaRelacionadaDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psServentiaRelacionadainserir()");
		stSqlCampos= "INSERT INTO PROJUDI.SERV_RELACIONADA ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getServentiaRelacionada().length()>0)) {
			 stSqlCampos+=   stVirgula + "SERV_RELACIONADA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getServentiaRelacionada());  

			stVirgula=",";
		}
		if ((dados.getId_ServentiaPrincipal().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV_PRINC " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ServentiaPrincipal());  

			stVirgula=",";
		}
		if ((dados.getId_ServentiaRelacao().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV_REL " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ServentiaRelacao());  

			stVirgula=",";
		}		
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_SERV_RELACIONADA",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(ServentiaRelacionadaDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psServentiaRelacionadaalterar()");

		stSql= "UPDATE PROJUDI.SERV_RELACIONADA SET  ";
		stSql+= "SERV_RELACIONADA = ?";		 ps.adicionarString(dados.getServentiaRelacionada());  

		stSql+= ",ID_SERV_PRINC = ?";		 ps.adicionarLong(dados.getId_ServentiaPrincipal());  

		stSql+= ",ID_SERV_REL = ?";		 ps.adicionarLong(dados.getId_ServentiaRelacao());  
		
		stSql += " WHERE ID_SERV_RELACIONADA  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psServentiaRelacionadaexcluir()");

		stSql= "DELETE FROM PROJUDI.SERV_RELACIONADA";
		stSql += " WHERE ID_SERV_RELACIONADA = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ServentiaRelacionadaDt consultarId(String id_serventiarelacionada )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ServentiaRelacionadaDt Dados=null;
		////System.out.println("....ps-ConsultaId_ServentiaRelacionada)");

		stSql= "SELECT * FROM PROJUDI.VIEW_SERV_RELACIONADA WHERE ID_SERV_RELACIONADA = ?";		ps.adicionarLong(id_serventiarelacionada); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_ServentiaRelacionada  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ServentiaRelacionadaDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ServentiaRelacionadaDt Dados, ResultSetTJGO rs )  throws Exception {
		
		Dados.setId(rs.getString("ID_SERV_RELACIONADA"));
		Dados.setServentiaRelacionada(rs.getString("SERV_RELACIONADA"));
		Dados.setId_ServentiaPrincipal( rs.getString("ID_SERV_PRINC"));
		Dados.setServentiaPrincipal( rs.getString("ID_SERV_PRINC"));
		Dados.setId_ServentiaRelacao( rs.getString("ID_SERV_RELACAO"));
		Dados.setServentiaRelacao( rs.getString("SERV_RELACAO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));

	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoServentiaRelacionada()");

		stSql= "SELECT ID_SERV_RELACIONADA, SERV_RELACIONADA FROM PROJUDI.VIEW_SERV_RELACIONADA WHERE SERV_RELACIONADA LIKE ?";
		stSql+= " ORDER BY SERV_RELACIONADA ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoServentiaRelacionada  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ServentiaRelacionadaDt obTemp = new ServentiaRelacionadaDt();
				obTemp.setId(rs1.getString("ID_SERV_RELACIONADA"));
				obTemp.setServentiaRelacionada(rs1.getString("SERV_RELACIONADA"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_SERV_RELACIONADA WHERE SERV_RELACIONADA LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..ServentiaRelacionadaPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
