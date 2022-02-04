package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoResponsavelDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ProcessoResponsavelPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 8712289280470932842L;

	//---------------------------------------------------------
	public ProcessoResponsavelPsGen() {

	}



//---------------------------------------------------------
	public void inserir(ProcessoResponsavelDt dados ) throws Exception{

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProcessoResponsavelinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.PROC_RESP ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_ServentiaCargo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV_CARGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ServentiaCargo());  

			stVirgula=",";
		}
		if ((dados.getId_Processo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Processo());  

			stVirgula=",";
		}
		if ((dados.getId_CargoTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_CARGO_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_CargoTipo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_PROC_RESP",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(ProcessoResponsavelDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psProcessoResponsavelalterar()");

		stSql= "UPDATE PROJUDI.PROC_RESP SET  ";
		stSql+= "ID_SERV_CARGO = ?";		 ps.adicionarLong(dados.getId_ServentiaCargo());  

		stSql+= ",ID_PROC = ?";		 ps.adicionarLong(dados.getId_Processo());  

		stSql+= ",ID_CARGO_TIPO = ?";		 ps.adicionarLong(dados.getId_CargoTipo());  

		stSql += " WHERE ID_PROC_RESP  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProcessoResponsavelexcluir()");

		stSql= "DELETE FROM PROJUDI.PROC_RESP";
		stSql += " WHERE ID_PROC_RESP = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ProcessoResponsavelDt consultarId(String id_processoresponsavel )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ProcessoResponsavelDt Dados=null;
		////System.out.println("....ps-ConsultaId_ProcessoResponsavel)");

		stSql= "SELECT * FROM PROJUDI.VIEW_PROC_RESP WHERE ID_PROC_RESP = ?";		ps.adicionarLong(id_processoresponsavel); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_ProcessoResponsavel  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ProcessoResponsavelDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		} finally {
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ProcessoResponsavelDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_PROC_RESP"));
		Dados.setServentiaCargo(rs.getString("SERV_CARGO"));
		Dados.setId_ServentiaCargo( rs.getString("ID_SERV_CARGO"));
		Dados.setId_Processo( rs.getString("ID_PROC"));
		Dados.setProcessoNumero( rs.getString("PROC_NUMERO"));
		Dados.setId_CargoTipo( rs.getString("ID_CARGO_TIPO"));
		Dados.setCargoTipo( rs.getString("CARGO_TIPO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		Dados.setId_Grupo( rs.getString("ID_GRUPO"));
		Dados.setGrupoCodigo( rs.getString("GRUPO_CODIGO"));
		Dados.setCargoTipoCodigo( rs.getString("CARGO_TIPO_CODIGO"));	
		
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoProcessoResponsavel()");

		stSql= "SELECT ID_PROC_RESP, SERV_CARGO FROM PROJUDI.VIEW_PROC_RESP WHERE SERV_CARGO LIKE ?";
		stSql+= " ORDER BY SERV_CARGO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoProcessoResponsavel  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ProcessoResponsavelDt obTemp = new ProcessoResponsavelDt();
				obTemp.setId(rs1.getString("ID_PROC_RESP"));
				obTemp.setServentiaCargo(rs1.getString("SERV_CARGO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PROC_RESP WHERE SERV_CARGO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..ProcessoResponsavelPsGen.consultarDescricao() Operação realizada com sucesso");
		} finally {
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
