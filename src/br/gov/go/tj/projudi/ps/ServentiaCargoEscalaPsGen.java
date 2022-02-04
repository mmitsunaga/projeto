package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ServentiaCargoEscalaDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class ServentiaCargoEscalaPsGen extends Persistencia {

	private static final long serialVersionUID = -7734947633461713486L;

	public ServentiaCargoEscalaPsGen() {}

	public void inserir(ServentiaCargoEscalaDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		stSqlCampos= "INSERT INTO PROJUDI.SERV_CARGO_ESC ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getServentiaCargoEscala().length()>0)) {
			 stSqlCampos+=   stVirgula + "SERV_CARGO_ESC " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getServentiaCargoEscala());  

			stVirgula=",";
		}
		if ((dados.getId_ServentiaCargo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV_CARGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ServentiaCargo());  

			stVirgula=",";
		}
		if ((dados.getId_Escala().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_ESC " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Escala());  

			stVirgula=",";
		}
		if ((dados.getAtivo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ATIVO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBoolean(dados.getAtivo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		
			dados.setId(executarInsert(stSql,"ID_SERV_CARGO_ESC",ps));

		 
	} 

	public void alterar(ServentiaCargoEscalaDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		stSql= "UPDATE PROJUDI.SERV_CARGO_ESC SET  ";
		stSql+= "SERV_CARGO_ESC = ?";		 ps.adicionarString(dados.getServentiaCargoEscala());  

		stSql+= ",ID_SERV_CARGO = ?";		 ps.adicionarLong(dados.getId_ServentiaCargo());  

		stSql+= ",ID_ESC = ?";		 ps.adicionarLong(dados.getId_Escala());  

		stSql+= ",ATIVO = ?";		 ps.adicionarBoolean(dados.getAtivo());  

		stSql += " WHERE ID_SERV_CARGO_ESC  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "DELETE FROM PROJUDI.SERV_CARGO_ESC";
		stSql += " WHERE ID_SERV_CARGO_ESC = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

	public ServentiaCargoEscalaDt consultarId(String id_ServentiaCargoescala )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ServentiaCargoEscalaDt Dados=null;

		stSql= "SELECT * FROM PROJUDI.VIEW_SERV_CARGO_ESC WHERE ID_SERV_CARGO_ESC = ?";		ps.adicionarLong(id_ServentiaCargoescala); 

		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ServentiaCargoEscalaDt();
				associarDt(Dados, rs1);
			}
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ServentiaCargoEscalaDt Dados, ResultSetTJGO rs )  throws Exception {
		
			Dados.setId(rs.getString("ID_SERV_CARGO_ESC"));
			Dados.setServentiaCargoEscala(rs.getString("SERV_CARGO_ESC"));
			Dados.setId_ServentiaCargo( rs.getString("ID_SERV_CARGO"));
			Dados.setServentiaCargo( rs.getString("SERV_CARGO"));
			Dados.setId_Escala( rs.getString("ID_ESC"));
			Dados.setEscala( rs.getString("ESC"));
			Dados.setAtivo( Funcoes.FormatarLogico(rs.getString("ATIVO")));
			Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));

	}

	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "SELECT ID_SERV_CARGO_ESC, SERV_CARGO_ESC FROM PROJUDI.VIEW_SERV_CARGO_ESC WHERE SERV_CARGO_ESC LIKE ?";
		stSql+= " ORDER BY SERV_CARGO_ESC ";
		ps.adicionarString("%"+descricao+"%"); 

		try{

			rs1 = consultarPaginacao(stSql, ps, posicao);

			while (rs1.next()) {
				ServentiaCargoEscalaDt obTemp = new ServentiaCargoEscalaDt();
				obTemp.setId(rs1.getString("ID_SERV_CARGO_ESC"));
				obTemp.setServentiaCargoEscala(rs1.getString("SERV_CARGO_ESC"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_SERV_CARGO_ESC WHERE SERV_CARGO_ESC LIKE ?";
			rs2 = consultar(stSql,ps);
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
