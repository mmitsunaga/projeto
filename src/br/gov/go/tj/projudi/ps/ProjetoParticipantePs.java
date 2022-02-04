package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProjetoParticipanteDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

@SuppressWarnings("unchecked")
public class ProjetoParticipantePs extends ProjetoParticipantePsGen{

    private static final long serialVersionUID = -5925104898740359849L;

    public ProjetoParticipantePs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Sobrescrita do método de listagem visto que o original
	 * seleciona apenas o nome do projeto na View, e faz-se necessário
	 * trazer informações adicionais contidas na mesma.
	 */
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs=null;	
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql= "SELECT ID_PROJETO_PARTICIPANTE, PROJETO, NOME, SERV_CARGO";
		SqlFrom = " FROM PROJUDI.VIEW_PROJETO_PARTICIPANTE";
		SqlFrom += " WHERE PROJETO LIKE ?";
		SqlOrder = " ORDER BY PROJETO ";
		ps.adicionarString( descricao +"%");
		
		try{
			rs = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			while (rs.next()) {
				ProjetoParticipanteDt obTemp = new ProjetoParticipanteDt();
				obTemp.setId(rs.getString("ID_PROJETO_PARTICIPANTE"));
				obTemp.setProjeto(rs.getString("PROJETO"));
				obTemp.setNome(rs.getString("NOME"));
				obTemp.setServentiaCargo(rs.getString("SERV_CARGO"));
				liTemp.add(obTemp);
			}
			Sql= "SELECT COUNT(*) as QUANTIDADE";
			rs = consultar(Sql + SqlFrom, ps);
			while (rs.next()) {
				liTemp.add(rs.getLong("QUANTIDADE"));
			}
		}finally{
				try{if (rs != null) rs.close();} catch(Exception e) {}
			}
			return liTemp; 
	}
	
	/**
	 * Recupera o Identificador do participante com base na id da
	 * serventia cargo.
	 * 
	 * @param idServentiaCargo
	 * @return String
	 * @throws Exception
	 */
	public String consultarId_ParticipanteServentiaCargo(String idServentiaCargo)  throws Exception {
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql= "SELECT ID_PROJETO_PARTICIPANTE FROM PROJUDI.VIEW_PROJETO_PARTICIPANTE WHERE ID_SERV_CARGO = ?";
		ps.adicionarLong(idServentiaCargo);
		
		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				return rs1.getString("ID_PROJETO_PARTICIPANTE");
			}
		}finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return null;
	}
	
	public void inserir(String idProjeto, ProjetoParticipanteDt dados ) throws Exception {

		String SqlCampos;
		String SqlValores;
		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();		
		
		SqlCampos= "INSERT INTO PROJUDI.PROJETO_PARTICIPANTE (";
		SqlValores = " Values (";
		SqlCampos += "ID_PROJETO";
		SqlValores += "?";
		ps.adicionarLong(idProjeto);
		 
		if ((dados.getId_ServentiaCargo().length()>0)) {			
			SqlCampos+= ",ID_SERV_CARGO " ;
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_ServentiaCargo());
		}
		SqlCampos+=")";
 		SqlValores+=")"; 		

 		Sql = SqlCampos + SqlValores;		

		dados.setId(executarInsert(Sql, "ID_PROJETO_PARTICIPANTE", ps));

		 
	}
	
	/**
	 * Lista os usuários vinculados à serventia informada.
	 * @param idServentia - ID da Serventia
	 * @return lista de Serventia Cargo cadastrados na serventia
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarServentiaCargoProjetoGeral(String idServentia) throws Exception {

		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql= "SELECT pp.ID_PROJETO_PARTICIPANTE, sc.ID_SERV_CARGO, sc.SERV_CARGO, p.ID_PROJETO, p.PROJETO " +
				" FROM PROJUDI.SERV_CARGO sc " +
				" INNER JOIN PROJUDI.SERV s ON sc.ID_SERV = s.ID_SERV " +
				" LEFT JOIN PROJUDI.PROJETO_PARTICIPANTE pp ON sc.ID_SERV_CARGO = pp.ID_SERV_CARGO " +
				" LEFT JOIN PROJUDI.PROJETO p ON p.ID_PROJETO = pp.ID_PROJETO " +
				" WHERE s.ID_SERV = ? " +  
				" ORDER BY sc.SERV_CARGO ";
		ps.adicionarLong(idServentia);
		
		try{
			rs = consultar(Sql, ps);
			while (rs.next()) {
				ProjetoParticipanteDt obTemp = new ProjetoParticipanteDt();
				obTemp.setId(rs.getString("ID_PROJETO_PARTICIPANTE"));
				obTemp.setProjeto(rs.getString("PROJETO"));
				obTemp.setId_ServentiaCargo (rs.getString("ID_SERV_CARGO"));
				obTemp.setServentiaCargo(rs.getString("SERV_CARGO"));
				obTemp.setId_Projeto (rs.getString("ID_PROJETO"));
				obTemp.setProjeto(rs.getString("PROJETO"));
				liTemp.add(obTemp);
			}
		}finally{
				try{if (rs != null) rs.close();} catch(Exception e) {}
			}
			return liTemp; 
	}
	
	/**
	 * Método que consulta se um determinado usuário do projeto possui tarefas criadas/alocadas para ele.
	 * @param idProjeto - ID do projeto
	 * @param idUsuario - ID do usuário
	 * @return true se tiver tarefa e false se não tiver
	 * @throws Exception
	 * @author hmgodinho
	 */
	public boolean consultarExisteTarefasUsuarioProjeto(String idProjeto, String idUsuario)  throws Exception {
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql= "SELECT COUNT(*) as QUANTIDADE FROM PROJUDI.VIEW_TAREFA WHERE ID_PROJETO = ? " + 
			" AND (ID_USU_CRIADOR = ? OR ID_USU_FINALIZADOR = ? OR ID_PROJ_PARTIC_RESPONSAVEL = ?)";
		ps.adicionarLong(idProjeto);
		ps.adicionarLong(idUsuario);
		ps.adicionarLong(idUsuario);
		ps.adicionarLong(idUsuario);
		
		try{
			rs1 = consultar(Sql, ps);
			if(rs1.next()) {
				int quantidade = rs1.getInt("QUANTIDADE");
				if(quantidade > 0){
					return true;
				}
			}
		}finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return false;
	}
	
	/**
	 * Consulta os participantes do projeto segundo as informações repassadas.
	 * @param descricao - nome do projeto
	 * @param idProjeto - id do projeto
	 * @param posicao - posição da página
	 * @return lista de participantes
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarParticipanteDescricaoProjeto(String descricao, String idProjeto, String posicao) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT ID_PROJETO_PARTICIPANTE, PROJETO, NOME, SERV_CARGO";
		SqlFrom = " FROM PROJUDI.VIEW_PROJETO_PARTICIPANTE ";
		SqlFrom += " WHERE NOME LIKE ? AND ID_PROJETO = ? ";						ps.adicionarString( descricao +"%");
		
		ps.adicionarLong(idProjeto);
		SqlOrder = " ORDER BY PROJETO ";

		try{
			rs = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			while (rs.next()) {
				ProjetoParticipanteDt obTemp = new ProjetoParticipanteDt();
				obTemp.setId(rs.getString("ID_PROJETO_PARTICIPANTE"));
				obTemp.setProjeto(rs.getString("PROJETO"));
				obTemp.setNome(rs.getString("NOME"));
				obTemp.setServentiaCargo(rs.getString("SERV_CARGO"));
				liTemp.add(obTemp);
			}
			Sql = "SELECT COUNT(*) as QUANTIDADE";
			rs = consultar(Sql + SqlFrom, ps);
			while (rs.next()) {
				liTemp.add(rs.getLong("QUANTIDADE"));
			}
		
		} finally{
			try{
				if (rs != null)
					rs.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}
	
	/**
	 * Lista os usuários vinculados à serventia informada.
	 * @param idServentia - Id da Serventia a ser consultada
	 * @return lista de usuários
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarServentiaCargoGeral(String idServentia) throws Exception {
		
		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql= "SELECT t1.ID_SERV_CARGO, t1.SERV_CARGO";
		Sql+= " FROM PROJUDI.SERV_CARGO t1 ";
		Sql+= " INNER JOIN PROJUDI.SERV t4 ON t1.ID_SERV = t4.ID_SERV ";
		Sql+= " 		WHERE t4.ID_SERV = ? ";
		ps.adicionarLong(idServentia);
		
		try{
			rs = consultar(Sql, ps);
			while (rs.next()) {
				ProjetoParticipanteDt obTemp = new ProjetoParticipanteDt();
				obTemp.setId_ServentiaCargo (rs.getString("ID_SERV_CARGO"));
				obTemp.setServentiaCargo(rs.getString("SERV_CARGO"));
				liTemp.add(obTemp);
			}
		}finally{
			try{if (rs != null) rs.close();} catch(Exception e) {}
		}
		return liTemp; 
	}
	
	/**
	 * Lista os registros vinculados à serventia do usuário logado
	 * @param id_projeto
	 * @param idServentia
	 * @return
	 * @throws Exception
	 */
	public List consultarServentiaCargoProjetoGeral(String id_projeto, String idServentia) throws Exception {

		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		//System.out.println("..ps-consultarServentiaCargoProjetoGeralProjetoParticipante()");

		Sql= "SELECT t2.ID_PROJETO_PARTICIPANTE, t1.ID_SERV_CARGO, t1.SERV_CARGO, t3.ID_PROJETO, t3.PROJETO";
		Sql+= " FROM PROJUDI.SERV_CARGO t1 ";
		Sql+= " INNER JOIN PROJUDI.SERV t4 ON t1.ID_SERV = t4.ID_SERV ";
		Sql+= " LEFT JOIN PROJUDI.PROJETO_PARTICIPANTE t2 ON t1.ID_SERV_CARGO = t2.ID_SERV_CARGO ";
		Sql+= " LEFT JOIN PROJUDI.PROJETO t3 ON t3.ID_PROJETO = t2.ID_PROJETO";
		Sql+= " 		WHERE t2.ID_PROJETO = ? ";
		ps.adicionarLong(id_projeto);
		Sql+= " 		AND   t4.ID_SERV = ? ";
		ps.adicionarLong(idServentia);
		
		try{
			//System.out.println("..ps-consultarServentiaCargoProjetoGeralProjetoParticipante  " + Sql);

			rs = consultar(Sql, ps);
			//System.out.println("....Execução Query OK"  );

			while (rs.next()) {
				ProjetoParticipanteDt obTemp = new ProjetoParticipanteDt();
				obTemp.setId(rs.getString("ID_PROJETO_PARTICIPANTE"));
				obTemp.setProjeto(rs.getString("PROJETO"));
				obTemp.setId_ServentiaCargo (rs.getString("ID_SERV_CARGO"));
				obTemp.setServentiaCargo(rs.getString("SERV_CARGO"));
				obTemp.setId_Projeto (id_projeto);
				obTemp.setProjeto(rs.getString("PROJETO"));
				liTemp.add(obTemp);
			}
			//System.out.println("..ProjetoParticipantePsGen.consultarServentiaCargoProjetoGeral() Operação realizada com sucesso");
		}finally{
				try{if (rs != null) rs.close();} catch(Exception e) {}
			}
			return liTemp; 
	}	

}