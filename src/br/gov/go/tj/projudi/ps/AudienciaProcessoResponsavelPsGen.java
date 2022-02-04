package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.AudienciaProcessoResponsavelDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class AudienciaProcessoResponsavelPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -2554625126215393584L;

	//---------------------------------------------------------
	public AudienciaProcessoResponsavelPsGen() {

	}



//---------------------------------------------------------
	public void inserir(AudienciaProcessoResponsavelDt dados ) throws Exception{

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psAudienciaProcessoResponsavelinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.AUDI_PROC_RESP ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_AudienciaProcesso().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_AUDI_PROC " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_AudienciaProcesso());  

			stVirgula=",";
		}
		if ((dados.getId_UsuarioServentia().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU_SERV " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_UsuarioServentia());  

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

		
			dados.setId(executarInsert(stSql,"ID_AUDI_PROC_RESPONSAVEL",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(AudienciaProcessoResponsavelDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psAudienciaProcessoResponsavelalterar()");

		stSql= "UPDATE PROJUDI.AUDI_PROC_RESP SET  ";
		stSql+= "ID_AUDI_PROC = ?";		 ps.adicionarLong(dados.getId_AudienciaProcesso());  

		stSql+= ",ID_USU_SERV = ?";		 ps.adicionarLong(dados.getId_UsuarioServentia());  

		stSql+= ",ID_CARGO_TIPO = ?";		 ps.adicionarLong(dados.getId_CargoTipo());  

		stSql += " WHERE ID_AUDI_PROC_RESPONSAVEL  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psAudienciaProcessoResponsavelexcluir()");

		stSql= "DELETE FROM PROJUDI.AUDI_PROC_RESP";
		stSql += " WHERE ID_AUDI_PROC_RESPONSAVEL = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public AudienciaProcessoResponsavelDt consultarId(String id_audienciaprocessoresponsavel )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		AudienciaProcessoResponsavelDt Dados=null;
		////System.out.println("....ps-ConsultaId_AudienciaProcessoResponsavel)");

		stSql= "SELECT * FROM PROJUDI.VIEW_AUDI_PROC_RESP WHERE ID_AUDI_PROC_RESPONSAVEL = ?";		ps.adicionarLong(id_audienciaprocessoresponsavel); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_AudienciaProcessoResponsavel  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new AudienciaProcessoResponsavelDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		} finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( AudienciaProcessoResponsavelDt Dados, ResultSetTJGO rs )  throws Exception {
		
		Dados.setId(rs.getString("ID_AUDI_PROC_RESPONSAVEL"));
		Dados.setProcessoNumero(rs.getString("PROC_NUMERO"));
		Dados.setId_AudienciaProcesso( rs.getString("ID_AUDI_PROC"));
		Dados.setId_UsuarioServentia( rs.getString("ID_USU_SERV"));
		Dados.setUsuario( rs.getString("USU"));
		Dados.setId_CargoTipo( rs.getString("ID_CARGO_TIPO"));
		Dados.setCargoTipo( rs.getString("CARGO_TIPO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		Dados.setDataAgendada( Funcoes.FormatarDataHora(rs.getDateTime("DATA_AGENDADA")));
		Dados.setNome( rs.getString("NOME"));
		
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoAudienciaProcessoResponsavel()");

		stSql= "SELECT ID_AUDI_PROC_RESPONSAVEL, PROC_NUMERO FROM PROJUDI.VIEW_AUDI_PROC_RESP WHERE PROC_NUMERO LIKE ?";
		stSql+= " ORDER BY PROC_NUMERO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoAudienciaProcessoResponsavel  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				AudienciaProcessoResponsavelDt obTemp = new AudienciaProcessoResponsavelDt();
				obTemp.setId(rs1.getString("ID_AUDI_PROC_RESPONSAVEL"));
				obTemp.setProcessoNumero(rs1.getString("PROC_NUMERO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_AUDI_PROC_RESP WHERE PROC_NUMERO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..AudienciaProcessoResponsavelPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
