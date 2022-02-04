package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.AgenciaDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class AgenciaPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 4859423431267429803L;

	//---------------------------------------------------------
	public AgenciaPsGen() {

	}



//---------------------------------------------------------
	public void inserir(AgenciaDt dados ) throws Exception{

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psAgenciainserir()");
		stSqlCampos= "INSERT INTO PROJUDI.AGENCIA ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getAgencia().length()>0)) {
			 stSqlCampos+=   stVirgula + "AGENCIA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getAgencia());  

			stVirgula=",";
		}
		if ((dados.getAgenciaCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "AGENCIA_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getAgenciaCodigo());  

			stVirgula=",";
		}
		if ((dados.getId_Banco().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_BANCO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Banco());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_AGENCIA",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(AgenciaDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psAGENCIAalterar()");

		stSql= "UPDATE PROJUDI.AGENCIA SET  ";
		stSql+= "AGENCIA = ?";		 ps.adicionarString(dados.getAgencia());  

		stSql+= ",AGENCIA_CODIGO = ?";		 ps.adicionarLong(dados.getAgenciaCodigo());  

		stSql+= ",ID_BANCO = ?";		 ps.adicionarLong(dados.getId_Banco());  

		stSql += " WHERE ID_AGENCIA  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psAGENCIAexcluir()");

		stSql= "DELETE FROM PROJUDI.AGENCIA";
		stSql += " WHERE Id_AGENCIA = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public AgenciaDt consultarId(String id_AGENCIA )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		AgenciaDt Dados=null;
		////System.out.println("....ps-ConsultaId_AGENCIA)");

		stSql= "SELECT * FROM PROJUDI.VIEW_AGENCIA WHERE ID_AGENCIA = ?";		ps.adicionarLong(id_AGENCIA); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_AGENCIA  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new AgenciaDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return Dados; 
	}

	protected void associarDt( AgenciaDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("Id_AGENCIA"));
		Dados.setAgencia(rs.getString("AGENCIA"));
		Dados.setAgenciaCodigo( rs.getString("AGENCIA_CODIGO"));
		Dados.setId_Banco( rs.getString("ID_BANCO"));
		Dados.setBanco( rs.getString("BANCO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		Dados.setBancoCodigo( rs.getString("BANCO_CODIGO"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoAGENCIA()");

		stSql= "SELECT ID_AGENCIA, AGENCIA FROM PROJUDI.VIEW_AGENCIA WHERE AGENCIA LIKE ?";
		stSql+= " ORDER BY AGENCIA ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoAGENCIA  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				AgenciaDt obTemp = new AgenciaDt();
				obTemp.setId(rs1.getString("ID_AGENCIA"));
				obTemp.setAgencia(rs1.getString("AGENCIA"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_AGENCIA WHERE AGENCIA LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..AGENCIAPsGen.consultarDescricao() Operação realizada com sucesso");
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

} 
