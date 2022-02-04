package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.AudienciaProcessoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class AudienciaProcessoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 498868196635619930L;

	//---------------------------------------------------------
	public AudienciaProcessoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(AudienciaProcessoDt dados ) throws Exception{

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psAudienciaProcessoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.AUDI_PROC ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_Audiencia().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_AUDI " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Audiencia());  

			stVirgula=",";
		}
		if ((dados.getId_AudienciaProcessoStatus().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_AUDI_PROC_STATUS " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_AudienciaProcessoStatus());  

			stVirgula=",";
		}
		if ((dados.getId_ServentiaCargo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV_CARGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ServentiaCargo());  

			stVirgula=",";
		}
		if ((dados.getId_ServentiaCargoRedator().length()>0)) { // jvosantos - 04/06/2019 10:33 - Adiciona o redator
			 stSqlCampos+=   stVirgula + "ID_SERV_CARGO_REDATOR " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ServentiaCargoRedator());  

			stVirgula=",";
		}
		if ((dados.getId_Processo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Processo());  

			stVirgula=",";
		}
		if ((dados.getDataMovimentacao().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_MOVI " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataMovimentacao());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_AUDI_PROC",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(AudienciaProcessoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psAudienciaProcessoalterar()");

		stSql= "UPDATE PROJUDI.AUDI_PROC SET  ";
		stSql+= "ID_AUDI = ?";		 ps.adicionarLong(dados.getId_Audiencia());  

		stSql+= ",ID_AUDI_PROC_STATUS = ?";		 ps.adicionarLong(dados.getId_AudienciaProcessoStatus());  

		stSql+= ",ID_SERV_CARGO = ?";		 ps.adicionarLong(dados.getId_ServentiaCargo());  

		stSql+= ",ID_SERV_CARGO_REDATOR = ?";		 ps.adicionarLong(dados.getId_ServentiaCargoRedator());  // jvosantos - 04/06/2019 10:33 - Adiciona o redator
		
		stSql+= ",ID_SERV_CARGO_PRESIDENTE = ?";		 ps.adicionarLong(dados.getId_ServentiaCargoPresidente());  // jvosantos - 25/09/2019 09:25 - Adiciona o presidente
		
		stSql+= ",ID_SERV_CARGO_MP = ?";		 ps.adicionarLong(dados.getId_ServentiaCargoMP());  // mrbatista - 05/03/2020 09:25 - Adiciona o MP

		stSql+= ",ID_PROC = ?";		 ps.adicionarLong(dados.getId_Processo());

		stSql+= ",DATA_MOVI = ?";		 ps.adicionarDateTime(dados.getDataMovimentacao());  

		stSql += " WHERE ID_AUDI_PROC  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psAudienciaProcessoexcluir()");

		stSql= "DELETE FROM PROJUDI.AUDI_PROC";
		stSql += " WHERE ID_AUDI_PROC = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public AudienciaProcessoDt consultarId(String id_audienciaprocesso )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		AudienciaProcessoDt Dados=null;
		////System.out.println("....ps-ConsultaId_AudienciaProcesso)");

		stSql= "SELECT * FROM PROJUDI.VIEW_AUDI_PROC WHERE ID_AUDI_PROC = ?";		ps.adicionarLong(id_audienciaprocesso); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_AudienciaProcesso  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new AudienciaProcessoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( AudienciaProcessoDt Dados, ResultSetTJGO rs )  throws Exception {
		
		Dados.setId(rs.getString("ID_AUDI_PROC"));
		Dados.setAudienciaTipo(rs.getString("AUDI_TIPO"));
		Dados.setId_Audiencia( rs.getString("ID_AUDI"));
		Dados.setId_AudienciaProcessoStatus( rs.getString("ID_AUDI_PROC_STATUS"));
		Dados.setAudienciaProcessoStatus( rs.getString("AUDI_PROC_STATUS"));
		Dados.setId_ServentiaCargo( rs.getString("ID_SERV_CARGO"));
		Dados.setServentiaCargo( rs.getString("SERV_CARGO"));
		Dados.setId_Processo( rs.getString("ID_PROC"));
		Dados.setProcessoNumero( rs.getString("PROC_NUMERO"));
		Dados.setDataMovimentacao( Funcoes.FormatarDataHora(rs.getDateTime("DATA_MOVI")));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		Dados.setAudienciaTipoCodigo( rs.getString("AUDI_TIPO_CODIGO"));
		Dados.setAudienciaProcessoStatusCodigo( rs.getString("AUDI_PROC_STATUS_CODIGO"));		
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoAudienciaProcesso()");

		stSql= "SELECT ID_AUDI_PROC, AUDI_TIPO FROM PROJUDI.VIEW_AUDI_PROC WHERE AUDI_TIPO LIKE ?";
		stSql+= " ORDER BY AUDI_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoAudienciaProcesso  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				AudienciaProcessoDt obTemp = new AudienciaProcessoDt();
				obTemp.setId(rs1.getString("ID_AUDI_PROC"));
				obTemp.setAudienciaTipo(rs1.getString("AUDI_TIPO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_AUDI_PROC WHERE AUDI_TIPO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..AudienciaProcessoPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
	