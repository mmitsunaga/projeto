package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaResponsavelDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;

import java.sql.Connection;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class PendenciaResponsavelPs extends PendenciaResponsavelPsGen{

	/**
     * 
     */
    private static final long serialVersionUID = 4832470017650392069L;

    public PendenciaResponsavelPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Verifica se um usuario ja e responsavel da pendencia
	 * @author Ronneesley Moura Teles
	 * @since 20/01/2009 14:23
	 * @param pendenciaDt vo da pendencia
	 * @param usuarioDt vo do usuario que se deseja verificar 
	 * @return String
	 */
	public boolean eUsuarioResponsavel(PendenciaDt pendenciaDt, UsuarioDt usuarioDt) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "SELECT 1 FROM PROJUDI.PEND_RESP pr WHERE pr.ID_PEND = ?";
		ps.adicionarLong(pendenciaDt.getId());
		sql += " AND pr.ID_USU_RESP = ?";	
		if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equals(""))
			ps.adicionarLong(usuarioDt.getId_UsuarioServentiaChefe());			
		else
			ps.adicionarLong(usuarioDt.getId_UsuarioServentia());		
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			if (rs1.next()) return true;
		
		}
		finally{
			 rs1.close();
		}
		
		
		return false;
	}
	
	/**
	 * Consulta todos os responsaveis de uma determinada pendencia
	 * @author Ronneesley Moura Teles
	 * @since 14/10/2008 11:43
	 * @param PendenciaDt pendenciaDt
	 * @return List
	 * @throws Exception
	 */
	public List consultarResponsaveis(String id_PendenciaDt) throws Exception {
		List responsaveis = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM VIEW_PEND_RESP pr WHERE pr.ID_PEND = ?";
		ps.adicionarLong(id_PendenciaDt);	
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				if (responsaveis == null) responsaveis = new ArrayList();

				PendenciaResponsavelDt dados = new PendenciaResponsavelDt();
				this.associarDt(dados, rs1);
				
				responsaveis.add(dados);
			}			
		
		}
		finally{
			 rs1.close();
		}
		
		return responsaveis;
	}
	
	/**
	 * Consulta todos os responsaveis de uma determinada pendencia
	 * @param PendenciaDt pendenciaDt
	 * @return List
	 * @throws Exception
	 */
	public List consultarResponsaveisFinais(String id_PendenciaDt) throws Exception {
		List responsaveis = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM VIEW_PEND_FINAL_RESP pr WHERE pr.ID_PEND = ?";
		ps.adicionarLong(id_PendenciaDt);	
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				if (responsaveis == null) responsaveis = new ArrayList();

				PendenciaResponsavelDt dados = new PendenciaResponsavelDt();
				this.associarDt(dados, rs1);
				
				responsaveis.add(dados);
			}			
		
		}
		finally{
			 rs1.close();
		}
		
		return responsaveis;
	}

	/**
	 * Consulta todos os responsaveis de uma determinada pendencia com detalhes de usuario
	 * @author Ronneesley Moura Teles
	 * @since 19/01/2009 17:11
	 * @param pendenciaDt vo de pendencia
	 * @return lista dos responsaveis
	 * @throws Exception
	 */
	public List consultarResponsaveisDetalhado(String  id_Pendencia) throws Exception {
		List responsaveis = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM VIEW_PEND_RESP_DETALHADA pr WHERE pr.ID_PEND = ?";
		ps.adicionarLong(id_Pendencia);
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			
			
			
			while (rs1.next()) {
				if (responsaveis == null) responsaveis = new ArrayList();

				PendenciaResponsavelDt dados = new PendenciaResponsavelDt();
				this.associarDt(dados, rs1);
				
				dados.setUsuarioResponsavel(rs1.getString("USUARIO_SERV_CARGO"));
				dados.setNomeUsuarioServentiaCargo(rs1.getString("NOME_USU_SERV_CARGO"));
				dados.setNomeUsuarioResponsavel(rs1.getString("NOME_USU_RESP"));
				dados.setCargoTipoCodigo(rs1.getString("CARGO_TIPO_CODIGO"));
				if (!rs1.isNull("SERVENTIA_CARGO")) {
					dados.setServentia(rs1.getString("SERVENTIA_CARGO"));
					dados.setId_ServentiaCargo(rs1.getString("ID_SERV_CARGO"));
				} else {
					dados.setId_Serventia(rs1.getString("ID_SERV"));
					dados.setServentia(rs1.getString("SERV"));
				}			
				
				responsaveis.add(dados);
			}			
		
		}
		finally{
			 rs1.close();
		}
		
		return responsaveis;
	}
	
	/**
	 * Consulta todos os responsaveis de uma determinada pendencia Finalizada com detalhes de usuario
	 * @author Jesus Rodrigo Corrêa
	 * @since 21/18/2014 
	 * @param pendenciaDt vo de pendencia
	 * @return lista dos responsaveis
	 * @throws Exception
	 */
	public List consultarResponsaveisDetalhadoPendenciaFinalizada(String  id_Pendencia) throws Exception {
		List responsaveis = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM VIEW_PEND_FINAL_RESP_DETALHADA pr WHERE pr.ID_PEND = ?";
		ps.adicionarLong(id_Pendencia);
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				if (responsaveis == null) responsaveis = new ArrayList();

				PendenciaResponsavelDt dados = new PendenciaResponsavelDt();
				this.associarDt(dados, rs1);	
				dados.setNomeUsuarioResponsavel(rs1.getString("NOME_USU_RESP"));
				dados.setNomeUsuarioServentiaCargo(rs1.getString("NOME_USU_SERV_CARGO"));
				dados.setCargoTipoCodigo(rs1.getString("CARGO_TIPO_CODIGO"));
				
				responsaveis.add(dados);
			}			
		
		}
		finally{
			 rs1.close();
		}
		
		return responsaveis;
	}
	
	/**
	 * Consulta procuradores responsável por uma determinada pendência em uma serventia
	 * @param String id_Pendencia, indentificador da pendencia
	 * @param String id_Serventia, indentificador da serventia
	 * @return lista dos responsaveis
	 * @throws Exception
	 */
	public List consultarResponsaveisIntimacoesCitacoes(String id_Pendencia, String id_Serventia) throws Exception {
		List responsaveis = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		String sql = "SELECT pr.ID_PEND_RESP AS ID_PEND_RESP, pr.ID_PEND AS ID_PEND, ";
	    sql += " pt.PEND_TIPO AS PEND, pr.ID_USU_RESP AS ID_USU_RESP, u.USU AS USU_RESP, ";
	    sql += " u.NOME As NOME_USU_RESP, pp.NOME AS PARTE_PROC ";
	    sql += " FROM PEND_RESP pr ";
	    sql += "JOIN PEND p on pr.ID_PEND = p.ID_PEND ";
	    sql += " JOIN PEND_TIPO pt on p.ID_PEND_TIPO = pt.ID_PEND_TIPO ";
	    sql += " LEFT JOIN USU_SERV us on pr.ID_USU_RESP = us.ID_USU_SERV ";
	    sql += " LEFT JOIN USU u on us.ID_USU = u.ID_USU ";
	    sql += " LEFT JOIN SERV s on us.ID_SERV = s.ID_SERV ";
	    sql += " LEFT JOIN PROC_PARTE pp on p.ID_PROC_PARTE = pp.ID_PROC_PARTE ";
	    sql += " WHERE pr.ID_PEND = ? AND s.ID_SERV = ? ";
	    ps.adicionarLong(id_Pendencia);
	    ps.adicionarLong(id_Serventia);
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql,ps);

			while (rs1.next()) {
				if (responsaveis == null) responsaveis = new ArrayList();

				PendenciaResponsavelDt dados = new PendenciaResponsavelDt();
				dados.setId(rs1.getString("ID_PEND_RESP"));
				dados.setPendencia(rs1.getString("PEND"));
				dados.setId_Pendencia( rs1.getString("ID_PEND"));
				dados.setId_UsuarioResponsavel( rs1.getString("ID_USU_RESP"));
				dados.setUsuarioResponsavel( rs1.getString("USU_RESP"));
				dados.setNomeUsuarioResponsavel(rs1.getString("NOME_USU_RESP"));
				dados.setCodigoTemp( rs1.getString("PARTE_PROC"));
				
				responsaveis.add(dados);
			}			
		
		}
		finally{
			 rs1.close();
		}
		
		return responsaveis;
	}
	
	public PendenciaResponsavelDt consultarResponsavelId(String id_pendenciaresponsavel )  throws Exception {

		String Sql;
		PendenciaResponsavelDt Dados=null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		////System.out.println("....ps-ConsultaId_PendenciaResponsavel)");

		Sql= "SELECT * FROM PROJUDI.VIEW_PEND_RESP_DETALHADA WHERE ID_PEND_RESP = ?";
		ps.adicionarLong(id_pendenciaresponsavel);
		////System.out.println("....Sql  " + Sql  );

		try{
			////System.out.println("..ps-ConsultaId_PendenciaResponsavel  " + Sql);
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				Dados= new PendenciaResponsavelDt();
				associarDt(Dados, rs1);
				Dados.setUsuarioResponsavel(rs1.getString("USUARIO_SERV_CARGO"));
				Dados.setNomeUsuarioServentiaCargo(rs1.getString("NOME_USU_SERV_CARGO"));
				Dados.setNomeUsuarioResponsavel(rs1.getString("NOME_USU_RESP"));
			}
			//rs1.close();
			////System.out.println("..ps-ConsultaId");
		}finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return Dados; 
	}
	
	/**
	 * Consulta o responsavel do grupo distribuidor gabinete
	 * @param String id_Pendencia, id_Pendencia
	 * @param int cargoTipo, cargoTipo
	 * @return lista dos responsaveis
	 * @throws Exception
	 */
	public List consultarResponsaveisCargoTipo(String  id_Pendencia, int cargoTipo) throws Exception {
		List responsaveis = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM VIEW_PEND_RESP_DETALHADA pr WHERE pr.ID_PEND = ? AND pr.CARGO_TIPO_CODIGO <> ?";
		ps.adicionarLong(id_Pendencia);
		ps.adicionarLong(cargoTipo);
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				if (responsaveis == null) responsaveis = new ArrayList<>();

				PendenciaResponsavelDt dados = new PendenciaResponsavelDt();
				this.associarDt(dados, rs1);
								
				responsaveis.add(dados);
			}			
		
		}
		finally{
			 rs1.close();
		}
		
		return responsaveis;
	}
	
	/**
	 * Método para consultar pendencia por id e por status da pendencia. Consulta somente as pendencias abertas e o seu responsavel com a 
	 * respectiva Serventia(Id_Serventia).
	 * @param idPendencia
	 * @param pendenciaStatusCodigo
	 * @return PendenciaDt
	 * @throws Exception
	 */
	public PendenciaDt consultarPendenciaPorIdAbertaComResponsavel(Integer idPendencia, Integer pendenciaStatusCodigo) throws Exception {
		PendenciaDt pendenciaDt = null;
		PendenciaResponsavelDt pendenciaResponsavelDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM VIEW_PEND_ABERTAS_RESP vp ");
		
		ResultSetTJGO rs1 = null;
		try{
			sql.append(" WHERE vp.ID_SERV_CARGO IS NULL");
			sql.append(" AND vp.ID_SERV_TIPO IS NULL");
			sql.append(" AND vp.ID_SERV_CARGO IS NULL");
			sql.append(" AND vp.ID_SERV IS NOT NULL");
			
			if( idPendencia != null ) {
				sql.append(" AND vp.ID_PEND = ?");
				ps.adicionarLong(idPendencia);
			}
			if( pendenciaStatusCodigo != null ) {
				sql.append(" AND vp.PEND_STATUS_CODIGO = ?");
				ps.adicionarLong(pendenciaStatusCodigo);
			}
			
			rs1 = this.consultar(sql.toString(), ps);
			
			while(rs1.next()) {
				if( pendenciaDt == null ) {
					pendenciaDt = new PendenciaDt();
					pendenciaResponsavelDt = new PendenciaResponsavelDt();
				}
				
			    pendenciaDt.setId( rs1.getString("ID_PEND"));
				pendenciaDt.setPendencia( rs1.getString("PEND"));
				pendenciaDt.setDataVisto( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_VISTO")));
				pendenciaDt.setId_PendenciaTipo( rs1.getString("ID_PEND_TIPO"));
				pendenciaDt.setPendenciaTipo( rs1.getString("PEND_TIPO"));
				pendenciaDt.setId_Movimentacao( rs1.getString("ID_MOVI"));
				pendenciaDt.setMovimentacao( rs1.getString("MOVI"));
				pendenciaDt.setId_Processo( rs1.getString("ID_PROC"));
				pendenciaDt.setProcessoNumero( rs1.getString("PROC_NUMERO_COMPLETO"));
				pendenciaDt.setId_ProcessoPrioridade( rs1.getString("ID_PROC_PRIOR"));
				pendenciaDt.setProcessoPrioridade( rs1.getString("PROC_PRIOR"));
				pendenciaDt.setId_ProcessoParte( rs1.getString("ID_PROC_PARTE"));
				pendenciaDt.setNomeParte( rs1.getString("NOME_PARTE"));
				pendenciaDt.setId_PendenciaStatus( rs1.getString("ID_PEND_STATUS"));
				pendenciaDt.setPendenciaStatus( rs1.getString("PEND_STATUS"));
				pendenciaDt.setId_UsuarioCadastrador( rs1.getString("ID_USU_CADASTRADOR"));
				pendenciaDt.setUsuarioCadastrador( rs1.getString("ID_USU_CADASTRADOR"));
				pendenciaDt.setId_UsuarioFinalizador( rs1.getString("ID_USU_FINALIZADOR"));
				pendenciaDt.setUsuarioFinalizador( rs1.getString("USU_FINALIZADOR"));
				pendenciaDt.setDataInicio( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setDataFim( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_FIM")));
				pendenciaDt.setDataLimite( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_LIMITE")));
				pendenciaDt.setDataDistribuicao( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_DIST")));
				pendenciaDt.setPrazo( rs1.getString("PRAZO"));
				pendenciaDt.setDataTemp( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_TEMP")));
				pendenciaDt.setId_PendenciaPai( rs1.getString("ID_PEND_PAI"));
				pendenciaDt.setCodigoTemp( rs1.getString("CODIGO_TEMP"));
				pendenciaDt.setPendenciaTipoCodigo( rs1.getString("PEND_TIPO_CODIGO"));
				pendenciaDt.setPendenciaStatusCodigo( rs1.getString("PEND_STATUS_CODIGO"));
				
				pendenciaResponsavelDt.setId_Serventia( rs1.getString("ID_SERV"));
				pendenciaResponsavelDt.setId_ServentiaTipo( rs1.getString("ID_SERV_TIPO"));
				pendenciaResponsavelDt.setId_UsuarioResponsavel( rs1.getString("ID_USU_RESP"));
				pendenciaResponsavelDt.setId_ServentiaCargo( rs1.getString("ID_SERV_CARGO"));
				
				pendenciaDt.addResponsavel(pendenciaResponsavelDt);
			}
		
		}
		finally{
			 rs1.close();
		}
		
		return pendenciaDt;
	}

	/**
	 * Atualiza os dados de uma  PendenciaResponsavel em virtude de uma troca de responsável da pendencia
	 * 
	 * @param id_Pendencia, identificação da pendencia
	 * @param id_ResponsavelAnterior, responsável anterior
	 * @param id_NovoResponsavel, novo responsável
	 * 
	 * @author lsbernardes
	 */
	public void alterarResponsavelPendencia(String id_Pendencia, String id_ResponsavelAnterior, String id_NovoResponsavel) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.PEND_RESP SET ID_SERV_CARGO = ?";
		ps.adicionarLong(id_NovoResponsavel);
		Sql += " WHERE ID_PEND = ?";
		ps.adicionarLong(id_Pendencia);
		Sql += " AND ID_SERV_CARGO = ?";
		ps.adicionarLong(id_ResponsavelAnterior);

		executarUpdateDelete(Sql, ps);
	}
	
	/**
	 * Atualiza os dados de uma  PendenciaResponsavel em virtude de uma troca de responsável da pendencia
	 * 
	 * @param id_Pendencia, identificação da pendencia
	 * @param id_ServentiaAnterior, responsável anterior
	 * @param id_NovaServentia, novo responsável
	 * 
	 * @author lsbernardes
	 */
	public void alterarResponsavelPendenciaRedistribuicaoLote(String id_Pendencia, String id_Pend_Resp, String id_ServentiaAnterior, String id_NovaServentia) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.PEND_RESP SET ID_SERV = ?";
		ps.adicionarLong(id_NovaServentia);
		Sql += " WHERE ID_PEND = ?";
		ps.adicionarLong(id_Pendencia);
		Sql += " AND ID_SERV = ?";
		ps.adicionarLong(id_ServentiaAnterior);
		Sql += " AND ID_PEND_RESP = ?";
		ps.adicionarLong(id_Pend_Resp);

		executarUpdateDelete(Sql, ps);		
	}
	
	/**
	 * Atualiza os dados de uma  PendenciaResponsavel em virtude de uma troca de responsável da pendencia
	 * 
	 * @param id_Pendencia, identificação da pendencia
	 * @param id_ResponsavelAnterior, responsável anterior
	 * @param id_NovoResponsavel, novo responsável
	 * 
	 * @author lsbernardes
	 */
	public void alterarResponsavelUsuarioServentiaPendencia(String id_Pendencia, String id_ResponsavelAnterior, String id_NovoResponsavel) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.PEND_RESP SET ID_USU_RESP = ?";
		ps.adicionarLong(id_NovoResponsavel);
		Sql += " WHERE ID_PEND = ?";
		ps.adicionarLong(id_Pendencia);
		Sql += " AND ID_USU_RESP = ?";
		ps.adicionarLong(id_ResponsavelAnterior);

		executarUpdateDelete(Sql, ps);		
	}
	
	/**
	 * Atualiza os dados de uma  PendenciaResponsavel em virtude de uma troca de responsável de uma intimação ou citação
	 * 
	 * @param id_Pendencia, identificação da pendencia
	 * @param id_ResponsavelAnterior, responsável anterior
	 * @param id_NovoResponsavel, novo responsável
	 * 
	 * @author lsbernardes
	 */
	public void alterarResponsavelPendenciaIntimacaoCitacao(String id_Pendencia, String id_ResponsavelAnterior, String id_NovoResponsavel) throws Exception {
		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.PEND_RESP SET ID_USU_RESP = ? ";
		ps.adicionarLong(id_NovoResponsavel);
		Sql += " WHERE ID_PEND = ? ";
		ps.adicionarLong(id_Pendencia);
		Sql += " AND ID_USU_RESP = ? ";
		ps.adicionarLong(id_ResponsavelAnterior);

		executarUpdateDelete(Sql, ps);
	}
	
	/**
	 * Atualiza os dados de uma  PendenciaResponsavel em virtude de uma troca de responsável de uma intimação da procuradoria
	 * 
	 * @param id_Pendencia, identificação da pendencia
	 * @param id_ServentiaCargoAnterior, responsável anterior
	 * @param id_ServentiaCargoNovo, novo responsável
	 * 
	 * @author lsbernardes
	 */
	public void alterarServentiaCargoResponsavel(String id_Pendencia, String id_ServentiaCargoAnterior, String id_ServentiaCargoNovo) throws Exception {
		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.PEND_RESP SET ID_SERV_CARGO = ? ";
		ps.adicionarLong(id_ServentiaCargoNovo);
		Sql += " WHERE ID_PEND = ? ";
		ps.adicionarLong(id_Pendencia);
		Sql += " AND ID_SERV_CARGO = ? ";
		ps.adicionarLong(id_ServentiaCargoAnterior);

		executarUpdateDelete(Sql, ps);
	}
	
	
	public List consultarIdPendenciaResponsavel(String id_ServentiaCargo, String id_Processo) throws Exception {
		String Sql;
		ResultSetTJGO rs1 = null;
		List lista = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT pr.ID_PROC_RESP FROM PROJUDI.PROC_RESP pr";
		Sql += " WHERE pr.ID_PROC = ?";
		ps.adicionarLong(id_Processo);
		Sql += " AND pr.ID_SERV_CARGO = ?";
		ps.adicionarLong(id_ServentiaCargo);

		try{
			rs1 = consultar(Sql, ps);
			while (rs1.next()) {							
				lista.add(rs1.getString("ID_PROC_RESP"));
			}			
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return lista;
	}
	
	/**
	 * Remove uma  PendenciaResponsavel - inicialmente em virtude de retirada do assistente pelo distribuidor
	 * 
	 * @param id_Pendencia, identificação da pendencia
	 * @param id_Responsavel, identificação do responsável 
	 * 
	 * @author mmgomes
	 */
	public void retirarResponsavelPendencia(String id_Pendencia, String id_Responsavel) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		Sql = "DELETE FROM PROJUDI.PEND_RESP ";
		Sql += " WHERE ID_PEND = ?";
		ps.adicionarLong(id_Pendencia);
		Sql += " AND ID_SERV_CARGO = ?";
		ps.adicionarLong(id_Responsavel);		
		
		executarUpdateDelete(Sql, ps);
	}

	public boolean isPendenciaResponsavelMesmaServentia(String id_pendencia, String id_Serv)  throws Exception {
		String Sql;
		ResultSetTJGO rs1 = null;
		boolean retorno = false;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		try{
			Sql = "SELECT pr.id_pend_resp, pr.id_pend,sc.id_serv_cargo, sc.ID_SERV, sc.SERV_CARGO,ct.CARGO_TIPO_CODIGO, ct.CARGO_TIPO "; 
			Sql += " FROM PEND_RESP pr "; 
			Sql += " INNER JOIN SERV_CARGO sc on pr.ID_SERV_CARGO = sc.ID_SERV_CARGO ";
			Sql += " INNER JOIN CARGO_TIPO ct on sc.ID_CARGO_TIPO = ct.ID_CARGO_TIPO ";
			Sql += " WHERE pr.ID_PEND = ?  AND sc.ID_SERV = ? AND ct.CARGO_TIPO_CODIGO in (8,17,18,19) ";
			ps.adicionarLong(id_pendencia);
			ps.adicionarLong(id_Serv);

			rs1 = consultar(Sql, ps);
			if (rs1.next()) {							
				retorno = true;
			}			
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return retorno;
	}
	
	/**
	 * Atualiza a serventia responsável por uma pendência do tipo Verificar Guia Pendente
	 * 
	 * @param String id_Processo: id do processo que tera o responsável pela conclusão modificado
	 * @param String id_ServentiaAtual, id do serventia que sera atualizado por um novo
	 * @param String id_ServentiaNovo, id do novo serventia 
	 * @param LogDt logDt, log do usuario
	 * @param FabricaConexao fabrica, conexao
	 * 
	 * @author lsebernardes
	 */
	public void atualizarServentiaResponsavelPendenciaVerificarGuiaPendente(String id_Processo, String id_ServentiaAtual, String id_ServentiaNova) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = " UPDATE (select pr.ID_PEND_RESP, pr.ID_SERV from PEND_RESP pr "; 
		Sql += " 		 inner join PEND p  on pr.ID_PEND = p.ID_PEND ";
		Sql += " 		 inner join PEND_TIPO pt on p.ID_PEND_TIPO = pt.ID_PEND_TIPO ";
		Sql += " 		 where pt.PEND_TIPO_CODIGO = ? and p.ID_PROC = ? and pr.ID_SERV= ? ) ";
		ps.adicionarLong(PendenciaTipoDt.VERIFICAR_GUIA_PENDENTE); ps.adicionarLong(id_Processo); ps.adicionarLong(id_ServentiaAtual);
		Sql += " SET id_serv = ? ";
		ps.adicionarLong(id_ServentiaNova);
		
		executarUpdateDelete(Sql, ps);
	}
	
	public PendenciaResponsavelDt consultarIdPendenciaResponsavel(String id_pendencia)  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		PendenciaResponsavelDt Dados=null;

		stSql= "SELECT * FROM PROJUDI.VIEW_PEND_RESP WHERE ID_PEND = ?";		ps.adicionarLong(id_pendencia); 

		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new PendenciaResponsavelDt();
				associarDt(Dados, rs1);
			}
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return Dados; 
	}
	
	/**
	 * Quando a pendência do tipo Mandado for resolvida pelo oficial de justiça a pendência deverá ser transferida para a serventia que deu origem à ela.
	 * 
	 * @param idPend
	 * @param idServCargo
	 * @param idServ
	 * @throws Exception
	 * @author hrrosa
	 */
	public void alterarResponsavelMandadoResolvido(String idPend, String idServCargo) throws Exception {
		String sql = null;
		String idPendResp = null;
		String prazo = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		if(Funcoes.StringToInt(idPend) == 0 || Funcoes.StringToInt(idServCargo) == 0) {
			throw new MensagemException("Parâmetros incorretos");
		}
		
		sql =  "(SELECT PR.ID_PEND_RESP, P.PRAZO FROM PROJUDI.PEND_RESP PR";
		sql += " JOIN PROJUDI.PEND P ON PR.ID_PEND = P.ID_PEND ";
		sql += " WHERE PR.ID_PEND = ? AND PR.ID_SERV_CARGO = ?)";
		ps.adicionarLong(idPend);
		ps.adicionarLong(idServCargo);
		sql += " UNION (";
		sql +=  "(SELECT PFR.ID_PEND_RESP, PF.PRAZO FROM PROJUDI.PEND_FINAL_RESP PFR";
		sql += " JOIN PROJUDI.PEND_FINAL PF ON PFR.ID_PEND = PF.ID_PEND ";
		sql += " WHERE PFR.ID_PEND = ? AND PFR.ID_SERV_CARGO = ?))";
		ps.adicionarLong(idPend);
		ps.adicionarLong(idServCargo);
		
		try{
			
			rs1 = consultar(sql,ps);
			if(rs1.next()){
				idPendResp = rs1.getString("ID_PEND_RESP");
				prazo = rs1.getString("PRAZO");
			}
			
			if(idPendResp == null) {
				throw new MensagemException("Erro ao alterar a pendência");
			}
			
		}finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		
		ps.limpar();
		
		//O prazo é o parâmetro para saber se a pendência foi, dentro desta transação,
		//movida para a pend_final ou não.
		if(prazo != null) {
			//Atualiza registro quando ele ainda estivar na tabela PEND
			sql =  "UPDATE PEND_RESP SET ID_SERV = ";
			sql += "(SELECT ID_SERV_CADASTRADOR FROM VIEW_PEND_FINAL WHERE ID_PEND = ";
			sql += " (SELECT ID_PEND_PAI FROM PEND WHERE ID_PEND = ?)";
			sql += ")";
			sql += ", ID_SERV_CARGO = null WHERE ID_PEND_RESP = ?";
		
		} else {
			//Atualiza registro quando ele estiver na PEND_FINAL
			sql =  "UPDATE PEND_FINAL_RESP SET ID_SERV = ";
			sql += "(SELECT ID_SERV_CADASTRADOR FROM VIEW_PEND_FINAL WHERE ID_PEND = ";
			sql += " (SELECT ID_PEND_PAI FROM PEND_FINAL WHERE ID_PEND = ?)";
			sql += ")";
			sql += ", ID_SERV_CARGO = null WHERE ID_PEND_RESP = ?";
		}
		
		ps.adicionarLong(idPend);
		ps.adicionarLong(idPendResp);
		
		executarUpdateDelete(sql, ps);
		
	}
}
