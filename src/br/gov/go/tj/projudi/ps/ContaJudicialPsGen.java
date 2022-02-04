package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import java.util.List;
import java.util.ArrayList;
import br.gov.go.tj.projudi.dt.ContaJudicialDt;


public class ContaJudicialPsGen extends Persistencia {


//---------------------------------------------------------
	public ContaJudicialPsGen() {


	}



//---------------------------------------------------------
	public void inserir(ContaJudicialDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.CONTA_JUDICIAL ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_ProcParte().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC_PARTE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ProcParte());  

			stVirgula=",";
		}
		if ((dados.getProcNumero().length()>0)) {
			 stSqlCampos+=   stVirgula + "PROC_NUMERO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getProcNumero());  

			stVirgula=",";
		}
		if ((dados.getId_Banco().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_BANCO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Banco());  

			stVirgula=",";
		}
		if ((dados.getContaJudicialNumero().length()>0)) {
			 stSqlCampos+=   stVirgula + "CONTA_JUDICIAL_NUMERO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getContaJudicialNumero());  

			stVirgula=",";
		}
		if ((dados.getId_Comarca().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_COMARCA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Comarca());  

			stVirgula=",";
		}
		if ((dados.getId_Serv().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Serv());  

			stVirgula=",";
		}
		if ((dados.getPessoaTipoDepositante().length()>0)) {
			 stSqlCampos+=   stVirgula + "PESSOA_TIPO_DEPOSITANTE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getPessoaTipoDepositante());  

			stVirgula=",";
		}
		if ((dados.getCpfCnpjDepositante().length()>0)) {
			 stSqlCampos+=   stVirgula + "CPF_CNPJ_DEPOSITANTE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getCpfCnpjDepositante());  

			stVirgula=",";
		}
		if ((dados.getNomeDepositante().length()>0)) {
			 stSqlCampos+=   stVirgula + "NOME_DEPOSITANTE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getNomeDepositante());  

			stVirgula=",";
		}
		if ((dados.getPessoaTipoReclamado().length()>0)) {
			 stSqlCampos+=   stVirgula + "PESSOA_TIPO_RECLAMADO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getPessoaTipoReclamado());  

			stVirgula=",";
		}
		if ((dados.getCpfCnpjReclamado().length()>0)) {
			 stSqlCampos+=   stVirgula + "CPF_CNPJ_RECLAMADO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getCpfCnpjReclamado());  

			stVirgula=",";
		}
		if ((dados.getNomeReclamado().length()>0)) {
			 stSqlCampos+=   stVirgula + "NOME_RECLAMADO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getNomeReclamado());  

			stVirgula=",";
		}
		if ((dados.getPessoalTipoReclamante().length()>0)) {
			 stSqlCampos+=   stVirgula + "PESSOAL_TIPO_RECLAMANTE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getPessoalTipoReclamante());  

			stVirgula=",";
		}
		if ((dados.getCpfCnpjReclamante().length()>0)) {
			 stSqlCampos+=   stVirgula + "CPF_CNPJ_RECLAMANTE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getCpfCnpjReclamante());  

			stVirgula=",";
		}
		if ((dados.getNomeReclamante().length()>0)) {
			 stSqlCampos+=   stVirgula + "NOME_RECLAMANTE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getNomeReclamante());  

			stVirgula=",";
		}
		if ((dados.getPessoaTipoAdvReclamado().length()>0)) {
			 stSqlCampos+=   stVirgula + "PESSOA_TIPO_ADV_RECLAMADO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getPessoaTipoAdvReclamado());  

			stVirgula=",";
		}
		if ((dados.getCpfCnpjAdvReclamado().length()>0)) {
			 stSqlCampos+=   stVirgula + "CPF_CNPJ_ADV_RECLAMADO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getCpfCnpjAdvReclamado());  

			stVirgula=",";
		}
		if ((dados.getNomeAdvReclamado().length()>0)) {
			 stSqlCampos+=   stVirgula + "NOME_ADV_RECLAMADO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getNomeAdvReclamado());  

			stVirgula=",";
		}
		if ((dados.getPessoaTipoAdvReclamante().length()>0)) {
			 stSqlCampos+=   stVirgula + "PESSOA_TIPO_ADV_RECLAMANTE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getPessoaTipoAdvReclamante());  

			stVirgula=",";
		}
		if ((dados.getCpfCnpjAdvReclamante().length()>0)) {
			 stSqlCampos+=   stVirgula + "CPF_CNPJ_ADV_RECLAMANTE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getCpfCnpjAdvReclamante());  

			stVirgula=",";
		}
		if ((dados.getNomeAdvReclamante().length()>0)) {
			 stSqlCampos+=   stVirgula + "NOME_ADV_RECLAMANTE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getNomeAdvReclamante());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 

		dados.setId(executarInsert(stSql,"ID_CONTA_JUDICIAL",ps));
	} 

//---------------------------------------------------------
	public void alterar(ContaJudicialDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";



		stSql= "UPDATE PROJUDI.CONTA_JUDICIAL SET  ";
		stSql+= "ID_PROC_PARTE = ?";		 ps.adicionarLong(dados.getId_ProcParte());  

		stSql+= ",PROC_NUMERO = ?";		 ps.adicionarString(dados.getProcNumero());  

		stSql+= ",ID_BANCO = ?";		 ps.adicionarLong(dados.getId_Banco());  

		stSql+= ",CONTA_JUDICIAL_NUMERO = ?";		 ps.adicionarLong(dados.getContaJudicialNumero());  

		stSql+= ",ID_COMARCA = ?";		 ps.adicionarLong(dados.getId_Comarca());  

		stSql+= ",ID_SERV = ?";		 ps.adicionarLong(dados.getId_Serv());  

		stSql+= ",PESSOA_TIPO_DEPOSITANTE = ?";		 ps.adicionarLong(dados.getPessoaTipoDepositante());  

		stSql+= ",CPF_CNPJ_DEPOSITANTE = ?";		 ps.adicionarLong(dados.getCpfCnpjDepositante());  

		stSql+= ",NOME_DEPOSITANTE = ?";		 ps.adicionarString(dados.getNomeDepositante());  

		stSql+= ",PESSOA_TIPO_RECLAMADO = ?";		 ps.adicionarLong(dados.getPessoaTipoReclamado());  

		stSql+= ",CPF_CNPJ_RECLAMADO = ?";		 ps.adicionarString(dados.getCpfCnpjReclamado());  

		stSql+= ",NOME_RECLAMADO = ?";		 ps.adicionarString(dados.getNomeReclamado());  

		stSql+= ",PESSOAL_TIPO_RECLAMANTE = ?";		 ps.adicionarLong(dados.getPessoalTipoReclamante());  

		stSql+= ",CPF_CNPJ_RECLAMANTE = ?";		 ps.adicionarLong(dados.getCpfCnpjReclamante());  

		stSql+= ",NOME_RECLAMANTE = ?";		 ps.adicionarString(dados.getNomeReclamante());  

		stSql+= ",PESSOA_TIPO_ADV_RECLAMADO = ?";		 ps.adicionarString(dados.getPessoaTipoAdvReclamado());  

		stSql+= ",CPF_CNPJ_ADV_RECLAMADO = ?";		 ps.adicionarLong(dados.getCpfCnpjAdvReclamado());  

		stSql+= ",NOME_ADV_RECLAMADO = ?";		 ps.adicionarString(dados.getNomeAdvReclamado());  

		stSql+= ",PESSOA_TIPO_ADV_RECLAMANTE = ?";		 ps.adicionarLong(dados.getPessoaTipoAdvReclamante());  

		stSql+= ",CPF_CNPJ_ADV_RECLAMANTE = ?";		 ps.adicionarLong(dados.getCpfCnpjAdvReclamante());  

		stSql+= ",NOME_ADV_RECLAMANTE = ?";		 ps.adicionarString(dados.getNomeAdvReclamante());  

		stSql += " WHERE ID_CONTA_JUDICIAL  = ? "; 		ps.adicionarLong(dados.getId()); 



			executarUpdateDelete(stSql,ps);


	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "DELETE FROM PROJUDI.CONTA_JUDICIAL";
		stSql += " WHERE ID_CONTA_JUDICIAL = ?";		ps.adicionarLong(chave); 



			executarUpdateDelete(stSql,ps);


	} 

//---------------------------------------------------------
	public ContaJudicialDt consultarId(String id_contajudicial )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ContaJudicialDt Dados=null;


		stSql= "SELECT * FROM PROJUDI.VIEW_CONTA_JUDICIAL WHERE ID_CONTA_JUDICIAL = ?";		ps.adicionarLong(id_contajudicial); 



		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ContaJudicialDt();
				associarDt(Dados, rs1);
			}

		}finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
			return Dados; 
	}

	protected void associarDt( ContaJudicialDt Dados, ResultSetTJGO rs )  throws Exception {

			Dados.setId(rs.getString("ID_CONTA_JUDICIAL"));
			Dados.setContaJudicialNumero(rs.getString("CONTA_JUDICIAL_NUMERO"));
			Dados.setId_ProcParte( rs.getString("ID_PROC_PARTE"));
			Dados.setNome( rs.getString("NOME"));
			Dados.setProcNumero( rs.getString("PROC_NUMERO"));
			Dados.setId_Banco( rs.getString("ID_BANCO"));
			Dados.setBanco( rs.getString("BANCO"));
			Dados.setId_Comarca( rs.getString("ID_COMARCA"));
			Dados.setComarca( rs.getString("COMARCA"));
			Dados.setId_Serv( rs.getString("ID_SERV"));
			Dados.setServ( rs.getString("SERV"));
			Dados.setPessoaTipoDepositante( rs.getString("PESSOA_TIPO_DEPOSITANTE"));
			Dados.setCpfCnpjDepositante( rs.getString("CPF_CNPJ_DEPOSITANTE"));
			Dados.setNomeDepositante( rs.getString("NOME_DEPOSITANTE"));
			Dados.setPessoaTipoReclamado( rs.getString("PESSOA_TIPO_RECLAMADO"));
			Dados.setCpfCnpjReclamado( rs.getString("CPF_CNPJ_RECLAMADO"));
			Dados.setNomeReclamado( rs.getString("NOME_RECLAMADO"));
			Dados.setPessoalTipoReclamante( rs.getString("PESSOAL_TIPO_RECLAMANTE"));
			Dados.setCpfCnpjReclamante( rs.getString("CPF_CNPJ_RECLAMANTE"));
			Dados.setNomeReclamante( rs.getString("NOME_RECLAMANTE"));
			Dados.setPessoaTipoAdvReclamado( rs.getString("PESSOA_TIPO_ADV_RECLAMADO"));
			Dados.setCpfCnpjAdvReclamado( rs.getString("CPF_CNPJ_ADV_RECLAMADO"));
			Dados.setNomeAdvReclamado( rs.getString("NOME_ADV_RECLAMADO"));
			Dados.setPessoaTipoAdvReclamante( rs.getString("PESSOA_TIPO_ADV_RECLAMANTE"));
			Dados.setCpfCnpjAdvReclamante( rs.getString("CPF_CNPJ_ADV_RECLAMANTE"));
			Dados.setNomeAdvReclamante( rs.getString("NOME_ADV_RECLAMANTE"));
			Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_CONTA_JUDICIAL, CONTA_JUDICIAL_NUMERO FROM PROJUDI.VIEW_CONTA_JUDICIAL WHERE CONTA_JUDICIAL_NUMERO LIKE ?";
		stSql+= " ORDER BY CONTA_JUDICIAL_NUMERO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);


			while (rs1.next()) {
				ContaJudicialDt obTemp = new ContaJudicialDt();
				obTemp.setId(rs1.getString("ID_CONTA_JUDICIAL"));
				obTemp.setContaJudicialNumero(rs1.getString("CONTA_JUDICIAL_NUMERO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_CONTA_JUDICIAL WHERE CONTA_JUDICIAL_NUMERO LIKE ?";
			rs2 = consultar(stSql,ps);

			while (rs2.next()) {
				liTemp.add(rs2.getLong("Quantidade"));
			}

		}finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

//---------------------------------------------------------
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {

		String stSql="";
		String stTemp="";
		int qtdeColunas = 1;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_CONTA_JUDICIAL as id, CONTA_JUDICIAL_NUMERO as descricao1 FROM PROJUDI.VIEW_CONTA_JUDICIAL WHERE CONTA_JUDICIAL_NUMERO LIKE ?";
		stSql+= " ORDER BY CONTA_JUDICIAL_NUMERO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_CONTA_JUDICIAL WHERE CONTA_JUDICIAL_NUMERO LIKE ?";
			rs2 = consultar(stSql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		}finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
			return stTemp; 
	}

} 
