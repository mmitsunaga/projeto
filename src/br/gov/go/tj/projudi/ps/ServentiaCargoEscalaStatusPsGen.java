package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ServentiaCargoEscalaStatusDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class ServentiaCargoEscalaStatusPsGen extends Persistencia {

	private static final long serialVersionUID = 1164353818644449553L;

	public ServentiaCargoEscalaStatusPsGen() {}

	public void inserir(ServentiaCargoEscalaStatusDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		stSqlCampos= "INSERT INTO PROJUDI.SERV_CARGO_ESC_STATUS ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getServentiaCargoEscalaStatus().length()>0)) {
			 stSqlCampos+=   stVirgula + "SERV_CARGO_ESC_STATUS " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getServentiaCargoEscalaStatus());  

			stVirgula=",";
		}
		if ((dados.getServentiaCargoEscalaStatusCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "SERV_CARGO_ESC_STATUS_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getServentiaCargoEscalaStatusCodigo());  

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
		
			dados.setId(executarInsert(stSql,"ID_SERV_CARGO_ESC_STATUS",ps));
		 
	} 

	public void alterar(ServentiaCargoEscalaStatusDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";


		stSql= "UPDATE PROJUDI.SERV_CARGO_ESC_STATUS SET  ";
		stSql+= "SERV_CARGO_ESC_STATUS = ?";		 ps.adicionarString(dados.getServentiaCargoEscalaStatus());  

		stSql+= ",SERV_CARGO_ESC_STATUS_CODIGO = ?";		 ps.adicionarLong(dados.getServentiaCargoEscalaStatusCodigo());  

		stSql+= ",ATIVO = ?";		 ps.adicionarBoolean(dados.getAtivo());  

		stSql += " WHERE ID_SERV_CARGO_ESC_STATUS  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "DELETE FROM PROJUDI.SERV_CARGO_ESC_STATUS";
		stSql += " WHERE ID_SERV_CARGO_ESC_STATUS = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

	public ServentiaCargoEscalaStatusDt consultarId(String id_ServentiaCargoescalastatus )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ServentiaCargoEscalaStatusDt Dados=null;

		stSql= "SELECT * FROM PROJUDI.VIEW_SERV_CARGO_ESC_STATUS WHERE ID_SERV_CARGO_ESC_STATUS = ?";		ps.adicionarLong(id_ServentiaCargoescalastatus); 

		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ServentiaCargoEscalaStatusDt();
				associarDt(Dados, rs1);
			}
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ServentiaCargoEscalaStatusDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_SERV_CARGO_ESC_STATUS"));
		Dados.setServentiaCargoEscalaStatus(rs.getString("SERV_CARGO_ESC_STATUS"));
		Dados.setServentiaCargoEscalaStatusCodigo(rs.getString("SERV_CARGO_ESC_STATUS_CODIGO"));
		Dados.setAtivo( Funcoes.FormatarLogico(rs.getString("ATIVO")));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "SELECT ID_SERV_CARGO_ESC_STATUS, SERV_CARGO_ESC_STATUS FROM PROJUDI.VIEW_SERV_CARGO_ESC_STATUS WHERE SERV_CARGO_ESC_STATUS LIKE ?";
		stSql+= " ORDER BY SERV_CARGO_ESC_STATUS ";
		ps.adicionarString("%"+descricao+"%"); 

		try{

			rs1 = consultarPaginacao(stSql, ps, posicao);

			while (rs1.next()) {
				ServentiaCargoEscalaStatusDt obTemp = new ServentiaCargoEscalaStatusDt();
				obTemp.setId(rs1.getString("ID_SERV_CARGO_ESC_STATUS"));
				obTemp.setServentiaCargoEscalaStatus(rs1.getString("SERV_CARGO_ESC_STATUS"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_SERV_CARGO_ESC_STATUS WHERE SERV_CARGO_ESC_STATUS LIKE ?";
			rs2 = consultar(stSql,ps);
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

} 
