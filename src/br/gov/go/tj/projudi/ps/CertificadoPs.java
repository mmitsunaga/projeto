package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.go.tj.projudi.dt.CertificadoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;

import java.sql.Connection;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class CertificadoPs extends CertificadoPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = -7555814520458134700L;

    public CertificadoPs(Connection conexao){
    	Conexao = conexao;
	}

	public void inserir(CertificadoDt dados) throws Exception {
    	PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "INSERT INTO CERTIFICADO (RAIZ, EMISSOR, LIBERADO, DATA_EMIS, DATA_EXPIRACAO, MOTIVO_REVOGACAO, ID_USU_CERTIFICADO, ID_USU_LIBERADOR, ID_USU_REVOGADOR)" + " values(?, ?, ?, ?, ?, ?, ?, ?, ?)";

		if (dados.getRaiz().length() == 0) ps.adicionarBooleanNull();
		else ps.adicionarBoolean(dados.getRaiz());

		if (dados.getEmissor().length() == 0) ps.adicionarBooleanNull();
		else ps.adicionarBoolean(dados.getEmissor());

		if (dados.getLiberado().length() == 0) ps.adicionarBooleanNull();
		else ps.adicionarBoolean(dados.getLiberado());

		if (dados.getDataEmissao().length() == 0) ps.adicionarDateTimeNull();
		else ps.adicionarDateTime(dados.getDataEmissao());

		if (dados.getDataExpiracao().length() == 0) ps.adicionarDateTimeNull();
		else ps.adicionarDateTime(dados.getDataExpiracao());

		if (dados.getMotivoRevogacao().length() == 0) ps.adicionarStringNull();
		else ps.adicionarString(dados.getMotivoRevogacao());

		if (dados.getId_UsuarioCertificado().length() == 0) ps.adicionarLongNull();
		else ps.adicionarLong(dados.getId_UsuarioCertificado());

		if (dados.getId_UsuarioLiberador().length() == 0) ps.adicionarLongNull();
		else ps.adicionarLong(dados.getId_UsuarioLiberador());

		if (dados.getId_UsuarioRevogador().length() == 0) ps.adicionarLongNull();
		else ps.adicionarLong(dados.getId_UsuarioRevogador());		

		
		dados.setId(executarInsert(sql, "ID_CERTIFICADO", ps));
		
	}

	public void inserirCaConfiavel(CertificadoDt dados) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();		

		String sql = "INSERT INTO CERTIFICADO_CONFIAVEL (CERTIFICADO_CONFIAVEL, ID_USU_CADASTRADOR, DATA_EMIS, DATA_EXPIRACAO, CERTIFICADO)" + " values(?, ?, ?, ?, ?)";
		
		if (dados.getDescricao().length() == 0) ps.adicionarStringNull();
		else ps.adicionarString(dados.getDescricao());

		if (dados.getId_UsuarioCertificado().length() == 0) ps.adicionarLongNull();
		else ps.adicionarLong(dados.getId_UsuarioCertificado());

		if (dados.getDataEmissao().length() == 0) ps.adicionarDateTimeNull();
		else ps.adicionarDateTime(dados.getDataEmissao());

		if (dados.getDataExpiracao().length() == 0) ps.adicionarDateTimeNull();
		else ps.adicionarDateTime(dados.getDataExpiracao());
		
		if (dados.getConteudo() == null || dados.getConteudo().length == 0) ps.adicionarByteNull();
		else ps.adicionarByte(dados.getConteudo());
		
		dados.setId(executarInsert(sql, "ID_CERTIFICADO_CONFIAVEL", ps));
		
	}
	
	public void alterar(CertificadoDt dados) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "UPDATE CERTIFICADO set LIBERADO = ?, DATA_REVOGACAO = ? , MOTIVO_REVOGACAO = ? , ID_USU_LIBERADOR = ?, ID_USU_REVOGADOR = ? WHERE ID_CERTIFICADO = ?";		

		if (dados.getLiberado().length() == 0) ps.adicionarBooleanNull();
		else ps.adicionarBoolean(dados.getLiberado());

		if (dados.getDataRevogacao().length() == 0) ps.adicionarDateTimeNull();
		else ps.adicionarDateTime(dados.getDataRevogacao());

		if (dados.getMotivoRevogacao().length() == 0) ps.adicionarStringNull();
		else ps.adicionarString(dados.getMotivoRevogacao());

		if (dados.getId_UsuarioLiberador().length() == 0) ps.adicionarLongNull();
		else ps.adicionarLong(dados.getId_UsuarioLiberador());

		if (dados.getId_UsuarioRevogador().length() == 0) ps.adicionarLongNull();
		else ps.adicionarLong(dados.getId_UsuarioRevogador());

		ps.adicionarLong(dados.getId());

		executarUpdateDelete(sql, ps);
			
	}

	public void atualizaConteudoArquivoP12(CertificadoDt dados) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "UPDATE CERTIFICADO set CERTIFICADO = ? WHERE ID_CERTIFICADO = ?";		

		if (dados.getConteudo() == null || dados.getConteudo().length == 0) ps.adicionarByteNull();
		else ps.adicionarByte(dados.getConteudo());

		ps.adicionarLong(dados.getId());
		
		executarUpdateDelete(sql, ps);
		
	}
	
	/*
	public void atualizaConteudoCertificadoConfiavel(CertificadoDt dados) throws Exception {
		try{
			String sql = "UPDATE CertificadoConfiavel set Certificado = ? WHERE Id_CertificadoConfiavel = ?";

			PreparedStatement stmt = Conexao.prepareStatement(sql);

			if (dados.getConteudo() == null || dados.getConteudo().length == 0) stmt.setNull(1, Types.BLOB);
			else stmt.setBytes(1, dados.getConteudo());

			stmt.setString(2, dados.getId());

			stmt.executeUpdate();
		
		}
	} */

	public int consultarCertificadoValidoUsuario(String idUsuario) throws Exception {

		String Sql;
		ResultSetTJGO rs1 = null;
		Integer qtdCertificadosValidos = 0;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		////System.out.println("..ps-consultarCertificadoValidoUsuario()");

		Sql = "SELECT COUNT(*) as qtdCertificadosValidos  FROM PROJUDI.VIEW_CERTIFICADO "; 
		Sql +=  "WHERE ID_USU_CERTIFICADO = ?";  		ps.adicionarLong(idUsuario);
		Sql +=  " AND RAIZ = ?";						ps.adicionarLong(0);
		Sql +=  " AND EMISSOR = ? ";		 			ps.adicionarLong(0);
		Sql +=  " AND DATA_EXPIRACAO > ?";				ps.adicionarDateTime(new Date());				 
		Sql +=  " AND DATA_REVOGACAO IS NULL ";
										

		try{
			rs1 = consultar(Sql, ps);
			////System.out.println("....Execução Query OK");

			if (rs1.next()) {
				qtdCertificadosValidos = rs1.getInt("qtdCertificadosValidos");
			}
			//rs1.close();
			////System.out.println("..ps-consultarCertificadoValidoUsuario Operação realizada com sucesso");
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return qtdCertificadosValidos;
	}

	public CertificadoDt consultarCertificadoSistema() throws Exception {
		String Sql;
		ResultSetTJGO rs1 = null;
		CertificadoDt Dados = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = " SELECT * FROM PROJUDI.VIEW_CERTIFICADO "; 
		Sql += " WHERE ID_USU_CERTIFICADO = ?"; ps.adicionarLong(UsuarioDt.SistemaProjudi );
		Sql += " AND RAIZ = ?";					ps.adicionarLong(0);
		Sql += " AND EMISSOR = ?";				ps.adicionarLong(0);
		Sql += " AND LIBERADO = ?";				ps.adicionarLong(1);
		Sql += " AND DATA_EXPIRACAO > ?";		ps.adicionarDateTime(new Date()); 
		Sql += " AND DATA_REVOGACAO IS NULL ";							
		

		try{
			 rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				Dados = new CertificadoDt();
				associarDt(Dados, rs1);
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return Dados;
	}
	
	public CertificadoDt consultarCertificadoRaizSistema() throws Exception {
		String Sql;
		ResultSetTJGO rs1 = null;
		CertificadoDt Dados = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = " SELECT * FROM PROJUDI.VIEW_CERTIFICADO "; 
		Sql += " WHERE RAIZ = ?";					ps.adicionarLong(1);
		Sql += " AND EMISSOR = ?";					ps.adicionarLong(0);
		Sql += " AND DATA_EXPIRACAO > ?";			ps.adicionarDateTime(new Date()); 
		Sql += " AND DATA_REVOGACAO IS NULL ";
							
		try{
			 rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				Dados = new CertificadoDt();
				associarDt(Dados, rs1);
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return Dados;
	}

	public CertificadoDt consultarCertificadoEmissorSistema() throws Exception {
		String Sql;
		ResultSetTJGO rs1 = null;
		CertificadoDt Dados = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = " SELECT * FROM PROJUDI.VIEW_CERTIFICADO " ; 
		Sql += " WHERE RAIZ = ?"; 					ps.adicionarLong(0);
		Sql += " AND EMISSOR = ?";					ps.adicionarLong(1);
		Sql += " AND DATA_EXPIRACAO > ?";			ps.adicionarDateTime(new Date()); 
		Sql += " AND DATA_REVOGACAO IS NULL ";
						

		try{
			 rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				Dados = new CertificadoDt();
				associarDt(Dados, rs1);
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return Dados;
	}
	
	/**
	 * Consulta um certificado válido para um usuário, mesmo que não esteja liberado.
	 * 
	 * @param usuarioDt, identificação do usuário
	 * @author msapaula
	 */
	public CertificadoDt consultarCertificadoUsuario(String id_Usuario) throws Exception {

		String Sql;
		ResultSetTJGO rs1 = null;
		CertificadoDt Dados = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = " SELECT * FROM PROJUDI.VIEW_CERTIFICADO vc";
		Sql += " WHERE ID_USU_CERTIFICADO = ?";								ps.adicionarLong(id_Usuario);
		Sql += " AND DATA_EXPIRACAO > ? AND DATA_REVOGACAO IS NULL ";		ps.adicionarDateTime(new Date());
		Sql += " AND LIBERADO = ? "; 										ps.adicionarLong(1);			

		try{
			 rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				Dados = new CertificadoDt();
				associarDt(Dados, rs1);
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return Dados;
	}

	public List consultarDescricaoCertificadoConfiavel(String descricao) throws Exception {

		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		////System.out.println("..ps-consultarDescricaoCertificadoConfiavel()");
		
		Sql = "SELECT ID_CERTIFICADO_CONFIAVEL, CERTIFICADO_CONFIAVEL, CERTIFICADO, ID_USU_CADASTRADOR, DATA_EMIS, DATA_EXPIRACAO ";
		Sql += " FROM PROJUDI.CERTIFICADO_CONFIAVEL WHERE DATA_EXPIRACAO > ?";
		Sql+= "   AND CERTIFICADO_CONFIAVEL LIKE ? ORDER BY CERTIFICADO_CONFIAVEL ";
		
		ps.adicionarDateTime(new Date());
		ps.adicionarString( descricao +"%");
		
		try{
			////System.out.println("..ps-ConsultaDescricaoCertificado  " + Sql);

			rs1 = consultar(Sql, ps);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				CertificadoDt obTemp = new CertificadoDt();
				obTemp.setId(rs1.getString("ID_CERTIFICADO_CONFIAVEL"));
				obTemp.setDescricao(rs1.getString("CERTIFICADO_CONFIAVEL"));
				obTemp.setId_UsuarioCertificado(rs1.getString("ID_USU_CADASTRADOR"));
				obTemp.setDataEmissao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_EMIS")));
				obTemp.setDataExpiracao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_EXPIRACAO")));
				obTemp.setConteudo(rs1.getBytes("CERTIFICADO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			////System.out.println("..CertificadoPs.consultarDescricaoCertificadoConfiavel() Operação realizada com sucesso");
		}finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return liTemp; 
	}
	
	public CertificadoDt consultaCertificadoConfiavel(String id_certificado_confiavel) throws Exception {
		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		CertificadoDt Dados=null;

		stSql= "SELECT * FROM PROJUDI.CERTIFICADO_CONFIAVEL WHERE ID_CERTIFICADO_CONFIAVEL = ?";		
		ps.adicionarLong(id_certificado_confiavel); 
		try{
			rs1 = consultar(stSql,ps);
			while (rs1.next()) {
				Dados = new CertificadoDt();
				Dados.setId(rs1.getString("ID_CERTIFICADO_CONFIAVEL"));
				Dados.setId_UsuarioCertificado(rs1.getString("ID_USU_CADASTRADOR"));
				Dados.setDataEmissao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_EMIS")));
				Dados.setDataExpiracao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_EXPIRACAO")));
				Dados.setCertificado(new String(rs1.getBytes("CERTIFICADO")));
				Dados.setConteudo(rs1.getBytes("CERTIFICADO"));
			}
		}finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return Dados; 
	}
	
	public List consultaCertificadosConfiaveis() throws Exception {

		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		////System.out.println("..ps-consultaCertificadosConfiaveis()");

		Sql = "SELECT ID_CERTIFICADO_CONFIAVEL, CERTIFICADO_CONFIAVEL, CERTIFICADO, ID_USU_CADASTRADOR, DATA_EMIS, DATA_EXPIRACAO ";
		Sql += " FROM PROJUDI.CERTIFICADO_CONFIAVEL WHERE DATA_EXPIRACAO > ?";		
		ps.adicionarDateTime(new Date());
		
		try{
			////System.out.println("..ps-consultaCertificadosConfiaveis  " + Sql);

			rs1 = consultar(Sql, ps);
			////System.out.println("....Execução Query OK");

			while (rs1.next()) {
				CertificadoDt obTemp = new CertificadoDt();
				obTemp.setId(rs1.getString("ID_CERTIFICADO_CONFIAVEL"));
				obTemp.setId_UsuarioCertificado(rs1.getString("ID_USU_CADASTRADOR"));
				obTemp.setDataEmissao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_EMIS")));
				obTemp.setDataExpiracao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_EXPIRACAO")));
				obTemp.setCertificado(new String(rs1.getBytes("CERTIFICADO")));
				obTemp.setConteudo(rs1.getBytes("CERTIFICADO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			////System.out.println("..ps-consultaCertificadosConfiaveis Operação realizada com sucesso");
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return liTemp;
	}
	
	public List consultaCertificadosNaoLiberados(String usuario, String posicao) throws Exception {

		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		////System.out.println("..ps-consultaCertificadosNaoLiberados()");

		Sql = "SELECT ID_CERTIFICADO, USU_CERTIFICADO, NOME_USU, RAIZ, EMISSOR, ID_USU_CERTIFICADO, LIBERADO, ID_USU_LIBERADOR,";
		Sql += " DATA_EMIS, DATA_EXPIRACAO, DATA_REVOGACAO, MOTIVO_REVOGACAO, ID_USU_REVOGADOR";
		Sql += " FROM PROJUDI.VIEW_CERTIFICADO WHERE LIBERADO = ? AND RAIZ = ? AND EMISSOR = ? AND USU_CERTIFICADO LIKE ?";
		Sql += " ORDER BY ID_CERTIFICADO Desc ";
		ps.adicionarLong(0);
		ps.adicionarLong(0);
		ps.adicionarLong(0);
		ps.adicionarString( usuario +"%");		
		try{
			rs1 = consultarPaginacao(Sql, ps, posicao);
			////System.out.println("....Execução Query OK");

			while (rs1.next()) {
				CertificadoDt obTemp = new CertificadoDt();
				obTemp.setId(rs1.getString("ID_CERTIFICADO"));
				obTemp.setUsuarioCertificado(rs1.getString("USU_CERTIFICADO"));
				obTemp.setNomeUsuario(rs1.getString("NOME_USU"));
				obTemp.setRaiz(Funcoes.FormatarLogico(rs1.getString("RAIZ")));
				obTemp.setEmissor(Funcoes.FormatarLogico(rs1.getString("EMISSOR")));
				obTemp.setId_UsuarioCertificado(rs1.getString("ID_USU_CERTIFICADO"));
				obTemp.setLiberado(Funcoes.FormatarLogico(rs1.getString("LIBERADO")));
				obTemp.setId_UsuarioLiberador(rs1.getString("ID_USU_LIBERADOR"));
				obTemp.setDataEmissao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_EMIS")));
				obTemp.setDataExpiracao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_EXPIRACAO")));
				obTemp.setDataRevogacao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_REVOGACAO")));
				obTemp.setMotivoRevogacao(rs1.getString("MOTIVO_REVOGACAO"));
				obTemp.setId_UsuarioRevogador(rs1.getString("ID_USU_REVOGADOR"));

				if (obTemp.ehValidoHoje()) {
					obTemp.setSituacao("VÁLIDO");
				} else {
					obTemp.setSituacao("INVÁLIDO");
				}

				liTemp.add(obTemp);
			}
			//rs1.close();
			////System.out.println("..ps-consultaCertificadosNaoLiberados Operação realizada com sucesso");

			Sql = "SELECT COUNT(*) AS QUANTIDADE FROM PROJUDI.VIEW_CERTIFICADO " + "WHERE LIBERADO = ? AND RAIZ = ? AND EMISSOR = ? AND USU_CERTIFICADO LIKE ?";
			rs2 = consultar(Sql, ps);
			////System.out.println("....2 - Consulta quantidade OK");
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));			
			//rs1.close();

		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp;
	}

	public String consultaCertificadosNaoLiberadosJSON(String usuario, String posicao) throws Exception {

		String Sql;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 6;

		Sql = "SELECT ID_CERTIFICADO AS ID, USU_CERTIFICADO AS DESCRICAO1, NOME_USU AS DESCRICAO2,";
		Sql += " to_char(DATA_EMIS, 'DD/MM/YYYY hh24:mi:ss') AS DESCRICAO3, to_char(DATA_EXPIRACAO, 'DD/MM/YYYY hh24:mi:ss') AS DESCRICAO4,";
		Sql += " to_char(DATA_EXPIRACAO, 'MM DD,YYYY hh24:mi:ss') AS DESCRICAO5, to_char(DATA_REVOGACAO, 'DD/MM/YYYY hh24:mi:ss') AS DESCRICAO6";
		Sql += " FROM PROJUDI.VIEW_CERTIFICADO WHERE LIBERADO = ? AND RAIZ = ? AND EMISSOR = ? AND USU_CERTIFICADO LIKE ?";
		Sql += " ORDER BY ID_CERTIFICADO Desc ";
		ps.adicionarLong(0);
		ps.adicionarLong(0);
		ps.adicionarLong(0);
		ps.adicionarString( usuario +"%");		
		try{
			rs1 = consultarPaginacao(Sql, ps, posicao);

			Sql = "SELECT COUNT(*) AS QUANTIDADE FROM PROJUDI.VIEW_CERTIFICADO " + "WHERE LIBERADO = ? AND RAIZ = ? AND EMISSOR = ? AND USU_CERTIFICADO LIKE ?";
			rs2 = consultar(Sql, ps);
			rs2.next();			
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return stTemp;
	}

	public List consultaCertificadosNaoRevogados(String usuario, String posicao) throws Exception {

		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		////System.out.println("..ps-consultaCertificadosUsuarios()");

		Sql = "SELECT ID_CERTIFICADO,USU_CERTIFICADO, NOME_USU, RAIZ, EMISSOR, ID_USU_CERTIFICADO, LIBERADO, ID_USU_LIBERADOR,";
		Sql += " DATA_EMIS, DATA_EXPIRACAO, DATA_REVOGACAO, MOTIVO_REVOGACAO, ID_USU_REVOGADOR";
		Sql += " FROM PROJUDI.VIEW_CERTIFICADO WHERE RAIZ = ? AND EMISSOR = ? AND LIBERADO = ? AND DATA_REVOGACAO IS NULL AND USU_CERTIFICADO LIKE ?";		
		Sql += " ORDER BY ID_CERTIFICADO Desc ";
		ps.adicionarLong(0);
		ps.adicionarLong(0);
		ps.adicionarLong(1);
		ps.adicionarString( usuario +"%");
		
		try{
			////System.out.println("..ps-consultaCertificadosUsuarios  " + Sql);

			rs1 = consultarPaginacao(Sql, ps, posicao);
			////System.out.println("....Execução Query OK");

			while (rs1.next()) {
				CertificadoDt obTemp = new CertificadoDt();
				obTemp.setId(rs1.getString("ID_CERTIFICADO"));
				obTemp.setUsuarioCertificado(rs1.getString("USU_CERTIFICADO"));
				obTemp.setNomeUsuario(rs1.getString("NOME_USU"));
				obTemp.setRaiz(Funcoes.FormatarLogico(rs1.getString("RAIZ")));
				obTemp.setEmissor(Funcoes.FormatarLogico(rs1.getString("EMISSOR")));
				obTemp.setId_UsuarioCertificado(rs1.getString("ID_USU_CERTIFICADO"));
				obTemp.setLiberado(Funcoes.FormatarLogico(rs1.getString("LIBERADO")));
				obTemp.setId_UsuarioLiberador(rs1.getString("ID_USU_LIBERADOR"));
				obTemp.setDataEmissao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_EMIS")));
				obTemp.setDataExpiracao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_EXPIRACAO")));
				obTemp.setDataRevogacao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_REVOGACAO")));
				obTemp.setMotivoRevogacao(rs1.getString("MOTIVO_REVOGACAO"));
				obTemp.setId_UsuarioRevogador(rs1.getString("ID_USU_REVOGADOR"));

				if (obTemp.ehValidoHoje()) {
					obTemp.setSituacao("VÁLIDO");
				} else {
					obTemp.setSituacao("INVÁLIDO");
				}
				if (obTemp.getDataRevogacao().equalsIgnoreCase("")) {
					obTemp.setDataRevogacao("-");
				}
				// ****
				liTemp.add(obTemp);
			}
			//rs1.close();
			////System.out.println("..ps-consultaCertificadosUsuarios Operação realizada com sucesso");

			Sql = "SELECT COUNT(*) AS QUANTIDADE FROM PROJUDI.VIEW_CERTIFICADO WHERE RAIZ = ? AND EMISSOR = ? AND LIBERADO = ? AND DATA_REVOGACAO IS NULL AND USU_CERTIFICADO LIKE ?";

			rs2 = consultar(Sql,ps);
			////System.out.println("....2 - Consulta quantidade OK");
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));			
			//rs1.close();

		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp;
	}

	public String consultaCertificadosNaoRevogadosJSON(String usuario, String posicao) throws Exception {

		String Sql;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 5;

		Sql = "SELECT ID_CERTIFICADO AS ID, USU_CERTIFICADO AS DESCRICAO1, NOME_USU AS DESCRICAO2,";
		Sql += " to_char(DATA_EMIS, 'DD/MM/YYYY hh24:mi:ss') AS DESCRICAO3, to_char(DATA_EXPIRACAO, 'DD/MM/YYYY hh24:mi:ss') AS DESCRICAO4,";
		Sql += " to_char(DATA_EXPIRACAO, 'MM DD,YYYY hh24:mi:ss') AS DESCRICAO5";
		Sql += " FROM PROJUDI.VIEW_CERTIFICADO WHERE RAIZ = ? AND EMISSOR = ? AND LIBERADO = ? AND DATA_REVOGACAO IS NULL AND USU_CERTIFICADO LIKE ?";		
		Sql += " ORDER BY ID_CERTIFICADO Desc ";
		ps.adicionarLong(0);
		ps.adicionarLong(0);
		ps.adicionarLong(1);
		ps.adicionarString( usuario +"%");
		
		try{
			rs1 = consultarPaginacao(Sql, ps, posicao);

			Sql = "SELECT COUNT(*) AS QUANTIDADE FROM PROJUDI.VIEW_CERTIFICADO WHERE RAIZ = ? AND EMISSOR = ? AND LIBERADO = ? AND DATA_REVOGACAO IS NULL AND USU_CERTIFICADO LIKE ?";

			rs2 = consultar(Sql,ps);
			rs2.next();			
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return stTemp;
	}

	public List consultaCertificadosUsuario(String idUsuario) throws Exception {

		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		////System.out.println("..ps-consultaCertificadosUsuarios()");

		Sql = "SELECT ID_CERTIFICADO, USU_CERTIFICADO, RAIZ, EMISSOR, ID_USU_CERTIFICADO, LIBERADO, ID_USU_LIBERADOR,";
		Sql += " DATA_EMIS, DATA_EXPIRACAO, DATA_REVOGACAO, MOTIVO_REVOGACAO, ID_USU_REVOGADOR";
		Sql += " FROM PROJUDI.VIEW_CERTIFICADO WHERE RAIZ = ? AND EMISSOR = ? AND ID_USU_CERTIFICADO = ?";
		Sql += " ORDER BY ID_CERTIFICADO ";
		ps.adicionarLong(0);
		ps.adicionarLong(0);
		ps.adicionarLong(idUsuario);
		try{
			////System.out.println("..ps-consultaCertificadosUsuarios  " + Sql);

			rs1 = consultar(Sql, ps);
			////System.out.println("....Execução Query OK");

			while (rs1.next()) {
				CertificadoDt obTemp = new CertificadoDt();
				obTemp.setId(rs1.getString("ID_CERTIFICADO"));
				obTemp.setUsuarioCertificado(rs1.getString("USU_CERTIFICADO"));
				obTemp.setRaiz(Funcoes.FormatarLogico(rs1.getString("RAIZ")));
				obTemp.setEmissor(Funcoes.FormatarLogico(rs1.getString("EMISSOR")));
				obTemp.setId_UsuarioCertificado(rs1.getString("ID_USU_CERTIFICADO"));
				obTemp.setLiberado(Funcoes.FormatarLogico(rs1.getString("LIBERADO")));
				obTemp.setId_UsuarioLiberador(rs1.getString("ID_USU_LIBERADOR"));
				obTemp.setDataEmissao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_EMIS")));
				obTemp.setDataExpiracao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_EXPIRACAO")));
				obTemp.setDataRevogacao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_REVOGACAO")));
				obTemp.setMotivoRevogacao(rs1.getString("MOTIVO_REVOGACAO"));
				obTemp.setId_UsuarioRevogador(rs1.getString("ID_USU_REVOGADOR"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			////System.out.println("..ps-consultaCertificadosUsuarios Operação realizada com sucesso");
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return liTemp;
	}
	
	public String consultaCertificadosUsuarioJSON(String idUsuario, String posicao) throws Exception {

		String Sql;
		String stTemp="";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 4;

		Sql = "SELECT ID_CERTIFICADO AS ID, USU_CERTIFICADO AS DESCRICAO1, DATA_EMIS AS DESCRICAO2 , DATA_EXPIRACAO AS DESCRICAO3, DATA_REVOGACAO AS DESCRICAO4";
		Sql += " FROM PROJUDI.VIEW_CERTIFICADO WHERE RAIZ = ? AND EMISSOR = ? AND ID_USU_CERTIFICADO = ?";
		Sql += " ORDER BY ID_CERTIFICADO ";
		ps.adicionarLong(0);
		ps.adicionarLong(0);
		ps.adicionarLong(idUsuario);
		try {

			rs1 = consultar(Sql, ps);
			
			Sql = "SELECT COUNT(*) AS QUANTIDADE  FROM PROJUDI.VIEW_CERTIFICADO WHERE RAIZ = ? AND EMISSOR = ? AND ID_USU_CERTIFICADO = ?";
			
			rs2 = consultar(Sql, ps);
			rs2.next();
			
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

			
		} finally{
             try {if (rs1 != null) rs1.close();} catch (Exception e1) {}
        }  
		return stTemp;
	}

	public List consultaCertificadosRevogados() throws Exception {

		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-consultaCertificadosRevogados()");

		Sql = "SELECT ID_CERTIFICADO, USU_CERTIFICADO, RAIZ, EMISSOR, ID_USU_CERTIFICADO, LIBERADO, ID_USU_LIBERADOR,";
		Sql += " DATA_EMIS, DATA_EXPIRACAO, DATA_REVOGACAO, MOTIVO_REVOGACAO, ID_USU_REVOGADOR";
		Sql += " FROM PROJUDI.VIEW_CERTIFICADO WHERE RAIZ = ? AND EMISSOR = ? AND DATA_REVOGACAO  is not null";
		ps.adicionarLong(0);
		ps.adicionarLong(0);
		try{
			////System.out.println("..ps-consultaCertificadosUsuarios  " + Sql);

			rs1 = consultar(Sql, ps);
			////System.out.println("....Execução Query OK");

			while (rs1.next()) {
				CertificadoDt obTemp = new CertificadoDt();
				obTemp.setId(rs1.getString("ID_CERTIFICADO"));
				obTemp.setUsuarioCertificado(rs1.getString("USU_CERTIFICADO"));
				obTemp.setRaiz(Funcoes.FormatarLogico(rs1.getString("RAIZ")));
				obTemp.setEmissor(Funcoes.FormatarLogico(rs1.getString("EMISSOR")));
				obTemp.setId_UsuarioCertificado(rs1.getString("ID_USU_CERTIFICADO"));
				obTemp.setLiberado(Funcoes.FormatarLogico(rs1.getString("LIBERADO")));
				obTemp.setId_UsuarioLiberador(rs1.getString("ID_USU_LIBERADOR"));
				obTemp.setDataEmissao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_EMIS")));
				obTemp.setDataExpiracao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_EXPIRACAO")));
				obTemp.setDataRevogacao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_REVOGACAO")));
				obTemp.setMotivoRevogacao(rs1.getString("MOTIVO_REVOGACAO"));
				obTemp.setId_UsuarioRevogador(rs1.getString("ID_USU_REVOGADOR"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			////System.out.println("..ps-consultaCertificadosRevogados Operação realizada com sucesso");
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return liTemp;
	}
}
