package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ContaUsuarioDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ContaUsuarioPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 2287736160642494361L;

	//---------------------------------------------------------
	public ContaUsuarioPsGen() {

	}



//---------------------------------------------------------
	public void inserir(ContaUsuarioDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psContaUsuarioinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.CONTA_USU ("; 

		stSqlValores +=  " Values (";
		
		if ((dados.getOperacaoContaUsuario().length()>0)) {
			 stSqlCampos+=   stVirgula + "CONTA_USU_OPERACAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getOperacaoContaUsuario());  

			stVirgula=",";
		}
 
		if ((dados.getContaUsuario().length()>0)) {
			 stSqlCampos+=   stVirgula + "CONTA_USU " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getContaUsuario());  

			stVirgula=",";
		}
		
		if ((dados.getDvContaUsuario().length()>0)) {
			 stSqlCampos+=   stVirgula + "CONTA_USU_DV " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getDvContaUsuario());  

			stVirgula=",";
		}
		
		
		if ((dados.getId_Usuario().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Usuario());  

			stVirgula=",";
		}
		if ((dados.getId_Agencia().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_AGENCIA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Agencia());  

			stVirgula=",";
		}
		if ((dados.getAtiva().length()>0)) {
			 stSqlCampos+=   stVirgula + "ATIVA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBoolean(dados.getAtiva());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_CONTA_USU",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(ContaUsuarioDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psContaUsuarioalterar()");

		stSql= "UPDATE PROJUDI.CONTA_USU SET  ";
		
		stSql+= "CONTA_USU_OPERACAO = ?"; ps.adicionarLong(dados.getOperacaoContaUsuario()); 
			
		stSql+= ",CONTA_USU = ?";		  ps.adicionarLong(dados.getContaUsuario());  
		
		stSql+= ",CONTA_USU_DV = ?";		  ps.adicionarLong(dados.getDvContaUsuario()); 

		stSql+= ",ID_USU = ?";		      ps.adicionarLong(dados.getId_Usuario());  

		stSql+= ",ID_AGENCIA = ?";		  ps.adicionarLong(dados.getId_Agencia());  

		stSql+= ",ATIVA = ?";		      ps.adicionarBoolean(dados.getAtiva());  

		stSql += " WHERE ID_CONTA_USU  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psContaUsuarioexcluir()");

		stSql= "DELETE FROM PROJUDI.CONTA_USU";
		stSql += " WHERE ID_CONTA_USU = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ContaUsuarioDt consultarId(String id_contausuario )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ContaUsuarioDt Dados=null;
		////System.out.println("....ps-ConsultaId_ContaUsuario)");

		stSql= "SELECT * FROM PROJUDI.VIEW_CONTA_USU WHERE ID_CONTA_USU = ?";		ps.adicionarLong(id_contausuario); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_ContaUsuario  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ContaUsuarioDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ContaUsuarioDt Dados, ResultSetTJGO rs )  throws Exception {
		
		Dados.setId(rs.getString("ID_CONTA_USU"));
		Dados.setOperacaoContaUsuario(rs.getString("CONTA_USU_OPERACAO"));
		Dados.setContaUsuario(rs.getString("CONTA_USU"));
		Dados.setDvContaUsuario(rs.getString("CONTA_USU_DV"));
		Dados.setId_Usuario( rs.getString("ID_USU"));
		Dados.setUsuario( rs.getString("NOME"));
		Dados.setId_Agencia( rs.getString("ID_AGENCIA"));
		Dados.setAgencia( rs.getString("AGENCIA"));
		Dados.setAtiva( Funcoes.FormatarLogico(rs.getString("ATIVA")));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoContaUsuario()");

		stSql= "SELECT ID_CONTA_USU, CONTA_USU FROM PROJUDI.VIEW_CONTA_USU WHERE CONTA_USU LIKE ?";
		stSql+= " ORDER BY CONTA_USU ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoContaUsuario  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ContaUsuarioDt obTemp = new ContaUsuarioDt();
				obTemp.setId(rs1.getString("ID_CONTA_USU"));
				obTemp.setContaUsuario(rs1.getString("CONTA_USU"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_CONTA_USU WHERE CONTA_USU LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..ContaUsuarioPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
