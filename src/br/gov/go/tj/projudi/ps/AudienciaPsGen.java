package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import br.gov.go.tj.projudi.dt.AudienciaDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class AudienciaPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -841962992911704080L;

	//---------------------------------------------------------
	public AudienciaPsGen() {

	}



//---------------------------------------------------------
	public void inserir(AudienciaDt dados ) throws Exception{

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psAudienciainserir()");
		stSqlCampos= "INSERT INTO PROJUDI.AUDI ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_AudienciaTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_AUDI_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_AudienciaTipo());  

			stVirgula=",";
		}
		if ((dados.getId_Serventia().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Serventia());  

			stVirgula=",";
		}
		if ((dados.getDataAgendada().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_AGENDADA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataAgendada());  

			stVirgula=",";
		}
		if ((dados.getDataMovimentacao().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_MOVI " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataMovimentacao());  

			stVirgula=",";
		}
		if ((dados.getReservada().length()>0)) {
			 stSqlCampos+=   stVirgula + "RESERVADA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBoolean(dados.getReservada());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_AUDI",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(AudienciaDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psAudienciaalterar()");

		stSql= "UPDATE PROJUDI.AUDI SET  ";
		stSql+= "ID_AUDI_TIPO = ?";		 ps.adicionarLong(dados.getId_AudienciaTipo());  

		stSql+= ",ID_SERV = ?";		 ps.adicionarLong(dados.getId_Serventia());  

		stSql+= ",DATA_AGENDADA = ?";		 ps.adicionarDateTime(dados.getDataAgendada());  

		stSql+= ",DATA_MOVI = ?";		 ps.adicionarDateTime(dados.getDataMovimentacao());  

		stSql+= ",RESERVADA = ?";		 ps.adicionarBoolean(dados.getReservada());  
		
		stSql += ",OBSERVACOES = ?";
		ps.adicionarClob(dados.getObservacoes());

		stSql += " WHERE ID_AUDI  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psAudienciaexcluir()");

		stSql= "DELETE FROM PROJUDI.AUDI";
		stSql += " WHERE ID_AUDI = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public AudienciaDt consultarId(String id_audiencia )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		AudienciaDt Dados=null;
		////System.out.println("....ps-ConsultaId_Audiencia)");

		stSql= "SELECT * FROM PROJUDI.VIEW_AUDI WHERE ID_AUDI = ?";		ps.adicionarLong(id_audiencia); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_Audiencia  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new AudienciaDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( AudienciaDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_AUDI"));
		Dados.setAudienciaTipo(rs.getString("AUDI_TIPO"));
		Dados.setId_AudienciaTipo( rs.getString("ID_AUDI_TIPO"));
		Dados.setId_Serventia( rs.getString("ID_SERV"));
		Dados.setServentia( rs.getString("SERV"));
		Dados.setDataAgendada( Funcoes.FormatarDataHoraMinuto(rs.getString("DATA_AGENDADA")));
		Dados.setDataMovimentacao( Funcoes.FormatarDataHora(rs.getDateTime("DATA_MOVI")));
		Dados.setReservada( Funcoes.FormatarLogico(rs.getString("RESERVADA")));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		Dados.setAudienciaTipoCodigo( rs.getString("AUDI_TIPO_CODIGO"));
		Dados.setId_ArquivoFinalizacaoSessao(rs.getString("ID_ARQ_FINALIZACAO"));
		Dados.setVirtual(Funcoes.FormatarLogico(rs.getString("VIRTUAL")));
		if(rs.contains("OBSERVACOES")) Dados.setObservacoes(rs.getString("OBSERVACOES"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoAudiencia()");

		stSql= "SELECT ID_AUDI, AUDI_TIPO FROM PROJUDI.VIEW_AUDI_COMPLETA WHERE AUDI_TIPO LIKE ?";
		stSql+= " ORDER BY AUDI_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoAudiencia  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				AudienciaDt obTemp = new AudienciaDt();
				obTemp.setId(rs1.getString("ID_AUDI"));
				obTemp.setAudienciaTipo(rs1.getString("AUDI_TIPO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_AUDI WHERE AUDI_TIPO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..AudienciaPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
