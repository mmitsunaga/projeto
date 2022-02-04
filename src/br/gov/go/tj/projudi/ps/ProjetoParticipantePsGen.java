package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProjetoParticipanteDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ProjetoParticipantePsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 6261344054468740732L;

	//---------------------------------------------------------
	public ProjetoParticipantePsGen() {

	}



//---------------------------------------------------------
	public void inserir(ProjetoParticipanteDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProjetoParticipanteinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.PROJETO_PARTICIPANTE ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_Projeto().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROJETO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Projeto());  

			stVirgula=",";
		}
		if ((dados.getId_ServentiaCargo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV_CARGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ServentiaCargo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_PROJETO_PARTICIPANTE",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(ProjetoParticipanteDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psProjetoParticipantealterar()");

		stSql= "UPDATE PROJUDI.PROJETO_PARTICIPANTE SET  ";
		stSql+= "ID_PROJETO = ?";		 ps.adicionarLong(dados.getId_Projeto());  

		stSql+= ",ID_SERV_CARGO = ?";		 ps.adicionarLong(dados.getId_ServentiaCargo());  

		stSql += " WHERE ID_PROJETO_PARTICIPANTE  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProjetoParticipanteexcluir()");

		stSql= "DELETE FROM PROJUDI.PROJETO_PARTICIPANTE";
		stSql += " WHERE ID_PROJETO_PARTICIPANTE = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ProjetoParticipanteDt consultarId(String id_projetoparticipante )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ProjetoParticipanteDt Dados=null;
		////System.out.println("....ps-ConsultaId_ProjetoParticipante)");

		stSql= "SELECT * FROM PROJUDI.VIEW_PROJETO_PARTICIPANTE WHERE ID_PROJETO_PARTICIPANTE = ?";		ps.adicionarLong(id_projetoparticipante); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_ProjetoParticipante  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ProjetoParticipanteDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ProjetoParticipanteDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_PROJETO_PARTICIPANTE"));
		Dados.setProjeto(rs.getString("PROJETO"));
		Dados.setId_Projeto( rs.getString("ID_PROJETO"));
		Dados.setId_ServentiaCargo( rs.getString("ID_SERV_CARGO"));
		Dados.setServentiaCargo( rs.getString("SERV_CARGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		Dados.setNome( rs.getString("NOME"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoProjetoParticipante()");

		stSql= "SELECT ID_PROJETO_PARTICIPANTE, PROJETO FROM PROJUDI.VIEW_PROJETO_PARTICIPANTE WHERE PROJETO LIKE ?";
		stSql+= " ORDER BY PROJETO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoProjetoParticipante  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ProjetoParticipanteDt obTemp = new ProjetoParticipanteDt();
				obTemp.setId(rs1.getString("ID_PROJETO_PARTICIPANTE"));
				obTemp.setProjeto(rs1.getString("PROJETO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PROJETO_PARTICIPANTE WHERE PROJETO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..ProjetoParticipantePsGen.consultarDescricao() Operação realizada com sucesso");
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

} 
