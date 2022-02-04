package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.go.tj.projudi.dt.EnderecoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoParteTipoDt;
import br.gov.go.tj.projudi.dt.RecursoDt;
import br.gov.go.tj.projudi.dt.RecursoParteDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class RecursoPs extends RecursoPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = -8334687620172713872L;

    public RecursoPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Sobrescrevendo método para setar DataEnvio como data atual do banco
	 */
	public void inserir(RecursoDt dados) throws Exception {

		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "INSERT INTO PROJUDI.RECURSO (";
		if (!(dados.getId_Processo().length() == 0)) {
			Sql += "ID_PROC ";
		}
		if (!(dados.getId_ServentiaOrigem().length() == 0)) {
			Sql += ",ID_SERV_ORIGEM ";
		}
		if (!(dados.getId_AreaDistribuicaoOrigem().length() == 0)) {
			Sql += ",ID_AREA_DIST_ORIGEM ";
		}
		if (!(dados.getId_ServentiaRecurso().length() == 0)){
			Sql += ",ID_SERV_RECURSO ";
		}
		if ((dados.getId_ProcessoTipo().length() > 0) || (dados.getProcessoTipoCodigo().length() > 0)){
			Sql += ",ID_PROC_TIPO";
		}
		Sql += ",DATA_ENVIO";
		if (!(dados.getDataRecebimento().length() == 0)) {
			Sql += ",DATA_RECEBIMENTO ";
		}
		if (!(dados.getDataRetorno().length() == 0)) {
			Sql += ",DATA_RETORNO ";
		}
		Sql += ") ";
		Sql += " Values (?";
		if (!(dados.getId_Processo().length() == 0)) {
			ps.adicionarLong(dados.getId_Processo());
		}
		if (!(dados.getId_ServentiaOrigem().length() == 0)) {
			Sql += ", ?";
			ps.adicionarLong(dados.getId_ServentiaOrigem());
		}
		if (!(dados.getId_AreaDistribuicaoOrigem().length() == 0)) {
			Sql += ", ?";
			ps.adicionarLong(dados.getId_AreaDistribuicaoOrigem());
		}
		if (!(dados.getId_ServentiaRecurso().length() == 0)) {
			Sql += ", ?";
			ps.adicionarLong(dados.getId_ServentiaRecurso());
		}
		if (dados.getId_ProcessoTipo().length() > 0) {
			Sql += ", ?";
			ps.adicionarLong(dados.getId_ProcessoTipo());
		}
		else if (dados.getProcessoTipoCodigo().length() > 0) {
			Sql += ", (SELECT ID_PROC_TIPO FROM PROC_TIPO WHERE PROC_TIPO_CODIGO = ?)";
			 ps.adicionarLong(dados.getProcessoTipoCodigo());
		}
		Sql += ", ?";
		ps.adicionarDate(new Date());
		if (!(dados.getDataRecebimento().length() == 0)) {
			Sql += ", ?";
			ps.adicionarDateTime(dados.getDataRecebimento());
		}
		if (!(dados.getDataRetorno().length() == 0)) {
			Sql += ", ?";
			ps.adicionarDateTime(dados.getDataRetorno());
		}
		Sql += ")";

		Sql = Sql.replace("(,", "(");

		dados.setId(executarInsert(Sql, "ID_RECURSO", ps));
	}

	/**
	 * Método que verifica se determinado processo já esteve na turma anteriormente, e retorna a serventia desse
	 * para que o recurso vá para a mesma serventia
	 * @param id_Processo, identificação do processo
	 * @param id_AreaDistribuicaoRecursal, incluida para filtrar a instancia superior do 2º grau
	 * @author msapaula
	 * @author jrcorrea
	 */
	public String getServentiaRecursoAnterior(String id_AreaDistribuicaoRecursal, String id_Processo) throws Exception {
		String id_serv_recurso = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
				

		String sql = " Select  max (r.Id_Serv_Recurso) as Id_Serv_Recurso ";
		sql += " From Recurso R ";       
		sql += "  Where R.Id_Proc = ? ";																ps.adicionarLong(id_Processo);
		sql += " and r.data_retorno is not null";
		sql += " And Exists ( Select 1 From Serv_Area_Dist Ad ";
		sql += "                       Inner Join Serv S On Ad.Id_serv = S.Id_serv ";
		sql += "                where Ad.Id_Area_Dist = ? and s.id_serv = r.Id_Serv_Recurso)";			ps.adicionarLong(id_AreaDistribuicaoRecursal);
							
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql,ps);
			if (rs1.next()) {
				id_serv_recurso = rs1.getString("Id_Serv_Recurso");
			}
		
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }
		
		return id_serv_recurso;
	}
	
	/**
	 * Método que verifica se determinado processo teve algum processo dependente no segundo grau
	 * para que o recurso vá para a mesma serventia
	 * @param id_Processo, identificação do processo
	 * @param id_areaDistribuicao, are distribuição
	 * 
	 * @author jrcorrea, lsbernardes
	 */
	public String getServentiaRecursoDependente(String id_Processo, String id_ProcessoDependente, String id_areaDistribuicao) throws Exception {
		String id_Serventia = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();		

		String 	sql = " SELECT r.id_serv_recurso FROM RECURSO r ";
		sql += " INNER JOIN PROC p ON (r.id_proc = p.id_proc or r.id_proc = p.id_proc_dependente) ";
		sql += " INNER JOIN SERV s ON r.id_serv_recurso = s.id_serv ";    
		sql += " INNER JOIN SERV_AREA_DIST sad ON s.id_serv = sad.id_serv AND sad.id_area_dist = ? ";  
		ps.adicionarLong(id_areaDistribuicao);
		
		if (id_ProcessoDependente != null && id_ProcessoDependente.length()>0){
			sql += " WHERE p.id_proc = ?  AND p.id_proc_dependente = ? ";
			ps.adicionarLong(id_Processo); 
			ps.adicionarLong(id_ProcessoDependente);
			
		} else{
			sql += " WHERE p.id_proc_dependente = ? ";
			ps.adicionarLong(id_Processo);
		}
		
		sql += " UNION ";

		sql += " SELECT p.id_serv  FROM PROC p ";
		sql += " INNER JOIN SERV s ON p.id_serv = s.id_serv ";  
		sql += " INNER JOIN SERV_AREA_DIST sad ON s.id_serv = sad.id_serv AND sad.id_area_dist = ? ";
		ps.adicionarLong(id_areaDistribuicao);
		
		if (id_ProcessoDependente != null && id_ProcessoDependente.length()>0){
			sql += " WHERE p.id_proc = ?  AND p.id_proc_dependente = ? ";
			ps.adicionarLong(id_Processo); 
			ps.adicionarLong(id_ProcessoDependente);
			
		} else{
			sql += " WHERE p.id_proc_dependente = ? ";
			ps.adicionarLong(id_Processo);
		}
		
//		String 	sql = " SELECT p.id_serv FROM proc p inner join serv s on p.id_serv=s.id_serv ";
//				sql += "  INNER JOIN SERV_AREA_DIST sad on s.id_serv = sad.id_serv and sad.id_area_dist = ? "; ps.adicionarLong(id_areaDistribuicao);
//				
//				if (id_ProcessoDependente != null && id_ProcessoDependente.length() > 0){
//					sql += " WHERE  p.id_proc_dependente = ? or p.id_proc = ? or p.id_proc_dependente = ? ";  
//					ps.adicionarLong(id_Processo); 
//					ps.adicionarLong(id_ProcessoDependente); 
//					ps.adicionarLong(id_ProcessoDependente);
//				}  else {
//					sql += " WHERE p.id_proc_dependente = ? "; ps.adicionarLong(id_Processo);
//				}
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql,ps);
			if (rs1.next()) {
				id_Serventia = rs1.getString("id_serv_recurso");
			}
		
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }
		
		return id_Serventia;
	}
	
	/**
	 * Método que verifica se existe um recurso ativo para o processo passado
	 * 
	 * @param id_Processo, identificação do processo
	 * @param id_Serventia, id da serventia do processo - não obrigatório
	 * 
	 * @author msapaula
	 */
	public String getRecursoAtivo(String id_Processo, String id_Serventia) throws Exception {
		String id_Recurso = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = " SELECT r.ID_RECURSO FROM PROJUDI.VIEW_RECURSO r";
		sql += " WHERE r.ID_PROC = ?";
		ps.adicionarLong(id_Processo);
		if(id_Serventia != null && !id_Serventia.equalsIgnoreCase("")){
			sql += " AND r.ID_SERV_RECURSO = ?";
			ps.adicionarLong(id_Serventia);
		}
		sql += " AND r.DATA_RETORNO IS NULL";
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql,ps);
			if (rs1.next()) {
				id_Recurso = rs1.getString("ID_RECURSO");
			}
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }
		
		return id_Recurso;
	}

	/**
	 * Consulta os recursos para autuar em uma turma recursal
	 * 
	 * @param id_serventia, identificação da serventia para localizar os recursos
	 * @param numeroProcesso, filtro para número do processo
	 * @param digitoVerificador, dígito verificador do processo
	 * @param posicao, utilizada na paginação
	 * 
	 * @author msapaula
	 */
	public List consultarRecursosAutuar(String id_Serventia, String numeroProcesso, String digitoVerificador, String posicao) throws Exception {
		String Sql;
		String SqlComum;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		SqlComum = " FROM PROJUDI.VIEW_RECURSO r WHERE r.DATA_RECEBIMENTO IS NULL AND r.ID_SERV_RECURSO = ?";
			ps.adicionarLong(id_Serventia);
		if (numeroProcesso != null && numeroProcesso.length() > 0) {
			SqlComum += " AND PROC_NUMERO = ?";
			ps.adicionarLong(numeroProcesso);
		}
		if (digitoVerificador != null && digitoVerificador.length() > 0) {
			SqlComum += " AND DIGITO_VERIFICADOR = ?";
			ps.adicionarLong(digitoVerificador);
		}

		Sql = " SELECT r.ID_RECURSO, r.ID_PROC, r.PROC_NUMERO_COMPLETO, r.DATA_ENVIO, r.SERV_ORIGEM " + SqlComum;
		Sql += " ORDER BY DATA_ENVIO ";
		

		try{
			rs1 = consultarPaginacao(Sql, ps, posicao);
			while (rs1.next()) {
				RecursoDt obTemp = new RecursoDt();
				obTemp.setId(rs1.getString("ID_RECURSO"));
				obTemp.setId_Processo(rs1.getString("ID_PROC"));
				obTemp.setProcessoNumero(Funcoes.formataNumeroProcesso(rs1.getString("PROC_NUMERO_COMPLETO")));
				obTemp.setDataEnvio(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_ENVIO")));
				obTemp.setServentiaOrigem(rs1.getString("SERV_ORIGEM"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			Sql = "SELECT COUNT(*) as QUANTIDADE " + SqlComum;
			rs2 = consultar(Sql,ps);
			if (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp;
	}

	public String consultarRecursosAutuarJSON(String id_Serventia, String numeroProcesso, String digitoVerificador, String posicao) throws Exception {
		String Sql;
		String SqlComum;
		String stTemp="";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 4;

		SqlComum = " FROM PROJUDI.VIEW_RECURSO r WHERE r.DATA_RECEBIMENTO IS NULL AND r.ID_SERV_RECURSO = ?";
			ps.adicionarLong(id_Serventia);
		if (numeroProcesso != null && numeroProcesso.length() > 0) {
			SqlComum += " AND PROC_NUMERO = ?";
			ps.adicionarLong(numeroProcesso);
		}
		if (digitoVerificador != null && digitoVerificador.length() > 0) {
			SqlComum += " AND DIGITO_VERIFICADOR = ?";
			ps.adicionarLong(digitoVerificador);
		}

		Sql = " SELECT r.ID_RECURSO as id, r.ID_PROC as descricao1, r.PROC_NUMERO_COMPLETO as descricao2, r.DATA_ENVIO as descricao3, r.SERV_ORIGEM as descricao4 " + SqlComum;
		Sql += " ORDER BY DATA_ENVIO ";
		

		try{
			rs1 = consultarPaginacao(Sql, ps, posicao);
			Sql = "SELECT COUNT(*) as QUANTIDADE " + SqlComum;
			rs2 = consultar(Sql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return stTemp;
	}
	
	
	/**
	 * Método que verifica se há recurso (aguardando autuação) referente ao processo.
	 * @param idProcesso - ID do processo
	 * @param idServentia - ID da serventia onde corre o recurso
	 * @return true se tiver recurso e false se não tiver
	 * @throws Exception
	 * @author hmgodinho
	 */
	public boolean existeRecursoNaoAutuadoProcesso(String idProcesso, String idServentia) throws Exception {
		int qtde = 0;
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		try{
			stSql = "SELECT COUNT(1) AS QUANTIDADE FROM PROJUDI.RECURSO r";
			stSql += " WHERE r.DATA_RECEBIMENTO IS NULL";
			stSql += " AND r.ID_SERV_RECURSO = ?";
			ps.adicionarLong(idServentia);
			stSql += " AND r.ID_PROC = ?";
			ps.adicionarLong(idProcesso);

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				qtde = rs1.getInt("QUANTIDADE");
				if(qtde > 0) {
					return true;
				} else {
					return false;
				}
			}
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return false;
	}
	
	
	/**
	 * Consulta a quantidade de processos para autuar em uma turma recursal.
	 * Esse método é utilizado na consulta feita na página inicial dos usuários internos.
	 * 
	 * @param id_Serventia identificação da serventia
	 * @author msapaula
	 */
	public int consultarQuantidadeRecursosAutuar(String id_Serventia) throws Exception {
		int qtde = 0;
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		try{
			stSql = "SELECT COUNT(*) AS QUANTIDADE FROM PROJUDI.RECURSO r";
			stSql += " WHERE r.DATA_RECEBIMENTO IS NULL";
			stSql += " AND r.ID_SERV_RECURSO = ?";
			ps.adicionarLong(id_Serventia);

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				qtde = rs1.getInt("QUANTIDADE");
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return qtde;
	}

	/**
	 * Atualiza os dados de um recurso em virtude da autuação do mesmo 
	 * 
	 * @param id_Recurso, identificação do recurso
	 * @param dataAutuacao, data da autuação
	 * 
	 * @author msapaula
	 */
	public void alterarRecursoAutuacao(String id_Recurso, String dataAutuacao, String id_ProcessoTipo) throws Exception {
		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "UPDATE PROJUDI.RECURSO SET DATA_RECEBIMENTO = ?";
		if (dataAutuacao != null && !dataAutuacao.isEmpty())
			ps.adicionarDateTime(dataAutuacao);
		else
			ps.adicionarDateTimeNull();
		if (id_ProcessoTipo != null && id_ProcessoTipo.trim().length() > 0) {
			Sql += " , ID_PROC_TIPO = ?";
			ps.adicionarLong(id_ProcessoTipo);
		}		
		Sql += " WHERE ID_RECURSO = ?";
		ps.adicionarLong(id_Recurso);

			executarUpdateDelete(Sql, ps);
	}
	
	/**
	 * Atualiza os dados de um recurso em virtude da autuação do mesmo 
	 * 
	 * @param id_Recurso, identificação do recurso
	 * @param dataAutuacao, data da autuação
	 * 
	 * @author lsbernardes
	 */
	public void alterarProcessoTipoRecurso(String id_Recurso, String id_ProcessoTipo) throws Exception {
		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "UPDATE PROJUDI.Recurso SET ID_PROC_TIPO = ?";
		if (id_ProcessoTipo != null && !id_ProcessoTipo.isEmpty())
			ps.adicionarLong(id_ProcessoTipo);
		
		Sql += " WHERE ID_RECURSO = ?";
		ps.adicionarLong(id_Recurso);

			executarUpdateDelete(Sql, ps);
	}

	/**
	 * Atualiza os dados de um recurso em virtude do retorno à serventia de
	 * origem
	 * 
	 * @param id_Recurso
	 *            , identificação do recurso
	 * @param dataRetorno
	 *            , data de retorno à origem
	 * 
	 * @author msapaula
	 */
	public void alterarRecursoRetornoOrigem(String id_Recurso, String dataRetorno) throws Exception {
		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.RECURSO SET DATA_RETORNO = ?";
		if (dataRetorno != null && !dataRetorno.isEmpty())
			ps.adicionarDateTime(dataRetorno);
		else
			ps.adicionarDateTimeNull();
		Sql += " WHERE ID_RECURSO = ?";
		ps.adicionarLong(id_Recurso);

			executarUpdateDelete(Sql, ps);
	}
	
	/**
	 * Atualiza a serventia do recurso
	 * @param id_Recurso, identificação do recurso
	 * @param id_ServentiaNova, nova serventia do processo
	 * 
	 * @author msapaula
	 */
	public void alterarServentiaRecurso(String id_Recurso, String id_ServentiaNova) throws Exception {
		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.RECURSO SET ID_SERV_RECURSO = ?";
		if (id_ServentiaNova != null && !id_ServentiaNova.isEmpty())
			ps.adicionarLong(id_ServentiaNova);
		else
			ps.adicionarLongNull();
		Sql += " WHERE ID_RECURSO = ?";
		ps.adicionarLong(id_Recurso);

			executarUpdateDelete(Sql, ps);
	}

	/**
	 * Consultar dados completos do recurso de acordo com id_Recurso passado
	 * @param id_Recurso: identificação do recurso
	 * 
	 * @author msapaula
	 */
	public RecursoDt consultarIdCompleto(String id_Recurso) throws Exception {
		String Sql;
		RecursoDt dados = new RecursoDt();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		// Retirada do beforeFirst()
		boolean EhPrimeiraVez = true;

		Sql = "SELECT RC.* FROM PROJUDI.VIEW_RECURSO_COMPLETO RC JOIN RECURSO_PARTE R ON R.ID_RECURSO_PARTE = RC.ID_RECURSO_PARTE ";
		Sql += "WHERE RC.ID_RECURSO = ?";
		Sql += " AND RC.DATA_BAIXA IS NULL ORDER BY R.ORDEM_PARTE, RC.NOME";
		ps.adicionarLong(id_Recurso);

		try{
			rs1 = consultar(Sql,ps);
			/*if (rs1.next()) {
				associarDt(dados, rs1);

				rs1.beforeFirst();
			*/
				while (rs1.next()) {
					
					if (EhPrimeiraVez){
						associarDt(dados, rs1);
						dados.setDescricaoPoloAtivo(rs1.getString("POLO_ATIVO"));
						dados.setDescricaoPoloPassivo(rs1.getString("POLO_PASSIVO"));
						
						EhPrimeiraVez = false;
					}
					
					//Setando dados das Partes
					RecursoParteDt recursoParteDt = new RecursoParteDt();
					recursoParteDt.setId(rs1.getString("ID_RECURSO_PARTE"));
					recursoParteDt.setId_Recurso(rs1.getString("ID_RECURSO"));
					recursoParteDt.setId_ProcessoParte(rs1.getString("ID_PROC_PARTE"));
					recursoParteDt.setId_ProcessoTipo(rs1.getString("ID_PROC_TIPO_RECURSO_PARTE"));
					recursoParteDt.setProcessoTipo(rs1.getString("PROC_TIPO_RECURSO_PARTE"));
					recursoParteDt.setDescricaoPoloAtivo(rs1.getString("POLO_ATIVO_RECURSO_PARTE"));
					recursoParteDt.setDescricaoPoloPassivo(rs1.getString("POLO_PASSIVO_RECURSO_PARTE"));

					ProcessoParteDt parteDt = new ProcessoParteDt();
					parteDt.setId(rs1.getString("ID_PROC_PARTE"));
					parteDt.setNome(rs1.getString("NOME"));
					parteDt.setProcessoParteTipoCodigo(rs1.getString("PROC_PARTE_TIPO_CODIGO"));
					parteDt.setCpf(rs1.getString("CPF"));
					parteDt.setCnpj(rs1.getString("CNPJ"));
					parteDt.setDataBaixa(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_BAIXA")));

					//Setando endereço da parte
					EnderecoDt enderecoDt = new EnderecoDt();
					enderecoDt.setId(rs1.getString("ID_ENDERECO"));
					enderecoDt.setLogradouro(rs1.getString("LOGRADOURO"));
					enderecoDt.setNumero(rs1.getString("NUMERO"));
					enderecoDt.setComplemento(rs1.getString("COMPLEMENTO"));
					enderecoDt.setCep(rs1.getString("CEP"));
					enderecoDt.setBairro(rs1.getString("BAIRRO"));
					enderecoDt.setCidade(rs1.getString("CIDADE"));
					enderecoDt.setUf(rs1.getString("UF"));
					parteDt.setEnderecoParte(enderecoDt);

					recursoParteDt.setProcessoParteDt(parteDt);

					String strTipo = rs1.getString("PROC_PARTE_TIPO_CODIGO");
					// Adiciona parte a lista correspondente
					if (strTipo != null){
						int tipo = Funcoes.StringToInt(strTipo);
						switch (tipo) {							
							case (ProcessoParteTipoDt.POLO_ATIVO_CODIGO):
								dados.addListaRecorrentes(recursoParteDt);
								break;							
							case (ProcessoParteTipoDt.POLO_PASSIVO_CODIGO):
								dados.addListaRecorridos(recursoParteDt);
								break;
						}
					}
				}
			//}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return dados;
	}
	
	/**
	 * O recurso pode ser cancelado antes de ser autuado
	 * @param id_Recurso: identificação do recurso
	 * 
	 * @author jrcorrea
	 */

	public void CancelarRecurso(String id_recurso) throws Exception {
		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		stSql= "DELETE FROM PROJUDI.RECURSO";
		stSql += " WHERE ID_RECURSO = ?";	ps.adicionarLong(id_recurso);
		stSql += " AND DATA_RECEBIMENTO IS NULL";	

		executarUpdateDelete(stSql,ps);		
	}
	
	/**
	 * O recurso será excluído para conversão em processo
	 * @param id_Recurso: identificação do recurso
	 * 
	 * @author lsbernardes
	 */

	public void excluirRecurso(String id_recurso) throws Exception {
		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		stSql= "DELETE FROM PROJUDI.RECURSO";
		stSql += " WHERE ID_RECURSO = ?";	ps.adicionarLong(id_recurso);
		stSql += " AND DATA_RETORNO IS NULL";	

		executarUpdateDelete(stSql,ps);			
	}
	
	/**
	 * Retorna lista de possíveis preventos para um processo passado.
	 * Verifica as partes, valor e tipo da ação.
	 * 
	 * @param recursoDt, obj para o qual serão consultados os possíveis conexos
	 * @param id_Comarca, identificação da comarca do processo
	 * @param id_ServentiaSubTipo, sub tipo da serventia
	 * 
	 * @return lista de possíveis conexos
	 * @throws Exception 
	 * @author jrcorrea
	 */
	public List consultarConexaoRecurso(String id_aredistribuicao, String id_processoTipo, List lisRecorrente, List lisRecorrido) throws Exception {
		List liConexo = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String stSql="";

		try{
			//ID_PROC, PROC_NUMERO, DIGITO_VERIFICADOR, PROC_NUMERO_COMPLETO, CPFCNPJ_RECORRENTE, CPFCNPJ_RECORRIDO, RECORRENTE, RECORRIDO, ID_PROC_TIPO, PROC_TIPO, DATA_RECEBIMENTO, ID_SERV_RECURSO, SERV, ID_SERV_SUBTIPO, ID_COMARCA
			stSql="SELECT  rp.ID_PROC, rp.PROC_NUMERO_COMPLETO, "; 
			stSql+= " rp.ID_PROC_TIPO, rp.PROC_TIPO, rp.DATA_RECEBIMENTO, rp.ID_SERV_RECURSO, rp.SERV_RECURSO,  rp.DATA_RETORNO ";
			stSql +=" FROM view_recurso_parte_conexao rp ";
			
			stSql += " WHERE rp.ID_AREA_DIST = ?";		ps.adicionarLong(id_aredistribuicao);
			//sql += " WHERE (pp.PROC_NUMERO <> " + processoDt.getProcessoNumeroSimples() + " AND pp.DIGITO_VERIFICADOR <> " + processoDt.getDigitoVerificador() + ")";
			stSql += " ";
			String stSql1 = "";

			stSql += " AND ( rp.ID_PROC_TIPO = ?) AND (";		ps.adicionarLong(id_processoTipo);
			
			
			for (int i = 0; i < lisRecorrente.size(); i++) {
				ProcessoParteDt promovente = (ProcessoParteDt) lisRecorrente.get(i);

				String cpfPromovente = "12345";

				if (promovente.getCpfCnpj() != null && !promovente.getCpfCnpj().equals("")) cpfPromovente = promovente.getCpfCnpj();
				if (lisRecorrido != null && lisRecorrido.size() > 0) {
					for (int j = 0; j < lisRecorrido.size(); j++) {
						String cpfPromovido = "12345";
						ProcessoParteDt promovido = (ProcessoParteDt) lisRecorrido.get(j);

						if (promovido.getCpfCnpj() != null && !promovido.getCpfCnpj().equals("")) cpfPromovido = promovido.getCpfCnpj();

						if (stSql1.length() > 0) stSql1 += " OR ";

						//Somente se parte tem CPF e é diferente de 999.999.999-99
						stSql1 += " ((rp.CPF_CNPJ_recorrente = ? AND rp.CPF_CNPJ_recorrido= ?) OR";
						ps.adicionarLong(cpfPromovente);
						ps.adicionarLong(cpfPromovido);
						stSql1 += " (rp.CPF_CNPJ_recorrente = ? AND rp.CPF_CNPJ_recorrido= ?) OR";
						ps.adicionarLong(cpfPromovido);
						ps.adicionarLong(cpfPromovente);
						stSql1 += " (rp.recorrente = ? AND rp.recorrido = ?) OR";
						ps.adicionarString(promovente.getNome());
						ps.adicionarString(promovido.getNome());
						stSql1 += " (rp.recorrente = ? AND rp.recorrido = ?) )";
						ps.adicionarString(promovido.getNome());
						ps.adicionarString(promovente.getNome());
					}
				} else {
					if (stSql1.length() > 0) stSql1 += " OR ";
					stSql1 += " (rp.CPF_CNPJ_recorrente = ? AND rp.CPF_CNPJ_recorrido IS NULL) OR ";
					ps.adicionarLong(cpfPromovente);
					stSql1 += " (rp.recorrente = ? AND rp.recorrido IS NULL) ";
					ps.adicionarString(promovente.getNome());
				}
			}

			rs1 = this.consultar(stSql+ stSql1 + ")", ps);

			while (rs1.next()) {
				if (liConexo == null) liConexo = new ArrayList();

				RecursoDt recurso = new RecursoDt();
				recurso.setId_Processo(rs1.getString("ID_PROC"));
				recurso.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				recurso.setId_ProcessoTipo(rs1.getString("ID_PROC_TIPO"));
				recurso.setProcessoTipo(rs1.getString("PROC_TIPO"));
				recurso.setDataRecebimento(Funcoes.FormatarData(rs1.getDateTime("DATA_RECEBIMENTO")));
				recurso.setId_ServentiaRecurso(rs1.getString("ID_SERV_RECURSO"));
				recurso.setServentiaRecurso(rs1.getString("SERV_RECURSO"));
				recurso.setDataRetorno(Funcoes.FormatarData(rs1.getDateTime("DATA_RETORNO")));
				liConexo.add(recurso);
			}

		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
				
			}
		}

		return liConexo;
	}


	/**
	 * Método para consultar recurso mais novo do processo.
	 * @param String idProcesso
	 * @return String idRecurso
	 * @throws Exception
	 */
	public String consultarRecursoMaisNovo(String idProcesso) throws Exception {
		String retorno = null;
		
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		try{
			stSql = "SELECT MAX(ID_RECURSO) AS ID_RECURSO FROM PROJUDI.RECURSO WHERE ID_PROC = ?";
			ps.adicionarLong(idProcesso);

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				retorno = rs1.getString("ID_RECURSO");
			}
		
        }
		finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return retorno;
	}
	
	/**
	 * Método para consultar recurso ativo mais antigo de um processo.
	 * @param String idProcesso
	 * @return String idRecurso
	 * @throws Exception
	 */
	public String consultarRecursoAtivoMaisAntigo(String idProcesso) throws Exception {
		String retorno = null;
		
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		try{
			stSql = "SELECT MIN(ID_RECURSO) AS ID_RECURSO FROM PROJUDI.RECURSO WHERE ID_PROC = ? AND DATA_RETORNO IS NULL";
			ps.adicionarLong(idProcesso);

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				retorno = rs1.getString("ID_RECURSO");
			}
		
        }
		finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return retorno;
	}
	
	/**
	 * Método para verificar se recurso já foi autuado
	 * @param String idProcesso
	 * @param String idServentiaRecurso
	 * @return boolean retorno
	 * @throws Exception
	 */
	public boolean isRecursoAutuado(String idProcesso, String idServentiaRecurso) throws Exception {
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		try{
			stSql = "SELECT ID_RECURSO AS ID_RECURSO FROM PROJUDI.RECURSO WHERE ID_PROC = ? AND DATA_RECEBIMENTO IS NOT NULL AND DATA_RETORNO IS NULL AND ID_SERV_RECURSO = ?";
			ps.adicionarLong(idProcesso);
			ps.adicionarLong(idServentiaRecurso);
			
			rs1 = consultar(stSql,ps);
			return rs1.next();
		
        }
		finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
	}

	public long consultarQuantidadeRecursos(String id_Serventia) throws Exception {
		long retorno = 0;
		
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		try{
			stSql = "SELECT count(1) AS QUANTIDADE FROM PROJUDI.RECURSO WHERE ID_SERV_ORIGEM  = ?";			ps.adicionarLong(id_Serventia);
			stSql +=" AND DATA_RETORNO is null";

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				retorno = rs1.getLong("QUANTIDADE");
			}
		
        }
		finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return retorno;
	}

	public boolean isExisteRecurso(String id_Processo, String id_Serventia) throws Exception {
		boolean boRetorno = false;
		
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		try{
			stSql = "SELECT id_recurso FROM PROJUDI.RECURSO WHERE ID_PROC=? AND ID_SERV_ORIGEM  = ?";		ps.adicionarLong(id_Processo); 	ps.adicionarLong(id_Serventia);
			stSql +=" AND DATA_RETORNO is null";

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				boRetorno = true;
			}
		
        }
		finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return boRetorno;
	}

	public RecursoDt consultarRecursoPorProcessoTipo(String id_Proc, String id_ProcessoTipo) throws Exception {		
		String Sql;
		RecursoDt dados = new RecursoDt();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		// Retirada do beforeFirst()
		boolean EhPrimeiraVez = true;
		
		Sql = " SELECT " + 
				"	RC.*, " + 
				"	 pt.POLO_ATIVO AS POLO_ATIVO_NVL, " + 
				"	 pt.POLO_PASSIVO AS POLO_PASSIVO_NVL " + 
				" FROM " + 
				"	PROJUDI.VIEW_RECURSO_COMPLETO RC " + 
				" JOIN RECURSO_PARTE R ON " + 
				"	R.ID_RECURSO_PARTE = RC.ID_RECURSO_PARTE " + 
				" JOIN PROC_TIPO PT ON PT.ID_PROC_TIPO = RC.ID_PROC_TIPO " + 				
				" WHERE " + 
				"	RC.DATA_BAIXA IS NULL " + 
				"	AND RC.ID_PROC = ? " +  
				"	AND RC.ID_PROC_TIPO_RECURSO_PARTE = ? " + 
				" ORDER BY " + 
				"	R.ORDEM_PARTE, " + 
				"	RC.NOME";
		
		ps.adicionarLong(id_Proc); 
		ps.adicionarLong(id_ProcessoTipo);

		try{
			rs1 = consultar(Sql,ps);
			/*if (rs1.next()) {
				associarDt(dados, rs1);

				rs1.beforeFirst();
			*/
				while (rs1.next()) {
					
					if (EhPrimeiraVez){
						associarDt(dados, rs1);
						dados.setDescricaoPoloAtivo(rs1.getString("POLO_ATIVO_NVL"));
						dados.setDescricaoPoloPassivo(rs1.getString("POLO_PASSIVO_NVL"));
						
						EhPrimeiraVez = false;
					}
					
					//Setando dados das Partes
					RecursoParteDt recursoParteDt = new RecursoParteDt();
					recursoParteDt.setId(rs1.getString("ID_RECURSO_PARTE"));
					recursoParteDt.setId_Recurso(rs1.getString("ID_RECURSO"));
					recursoParteDt.setId_ProcessoParte(rs1.getString("ID_PROC_PARTE"));
					recursoParteDt.setId_ProcessoTipo(rs1.getString("ID_PROC_TIPO_RECURSO_PARTE"));
					recursoParteDt.setProcessoTipo(rs1.getString("PROC_TIPO_RECURSO_PARTE"));
					recursoParteDt.setDescricaoPoloAtivo(rs1.getString("POLO_ATIVO_RECURSO_PARTE"));
					recursoParteDt.setDescricaoPoloPassivo(rs1.getString("POLO_PASSIVO_RECURSO_PARTE"));

					ProcessoParteDt parteDt = new ProcessoParteDt();
					parteDt.setId(rs1.getString("ID_PROC_PARTE"));
					parteDt.setNome(rs1.getString("NOME"));
					parteDt.setProcessoParteTipoCodigo(rs1.getString("PROC_PARTE_TIPO_CODIGO"));
					parteDt.setCpf(rs1.getString("CPF"));
					parteDt.setCnpj(rs1.getString("CNPJ"));
					parteDt.setDataBaixa(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_BAIXA")));

					//Setando endereço da parte
					EnderecoDt enderecoDt = new EnderecoDt();
					enderecoDt.setId(rs1.getString("ID_ENDERECO"));
					enderecoDt.setLogradouro(rs1.getString("LOGRADOURO"));
					enderecoDt.setNumero(rs1.getString("NUMERO"));
					enderecoDt.setComplemento(rs1.getString("COMPLEMENTO"));
					enderecoDt.setCep(rs1.getString("CEP"));
					enderecoDt.setBairro(rs1.getString("BAIRRO"));
					enderecoDt.setCidade(rs1.getString("CIDADE"));
					enderecoDt.setUf(rs1.getString("UF"));
					parteDt.setEnderecoParte(enderecoDt);

					recursoParteDt.setProcessoParteDt(parteDt);

					String strTipo = rs1.getString("PROC_PARTE_TIPO_CODIGO");
					// Adiciona parte a lista correspondente
					if (strTipo != null){
						int tipo = Funcoes.StringToInt(strTipo);
						switch (tipo) {							
							case (ProcessoParteTipoDt.POLO_ATIVO_CODIGO):
								dados.addListaRecorrentes(recursoParteDt);
								break;							
							case (ProcessoParteTipoDt.POLO_PASSIVO_CODIGO):
								dados.addListaRecorridos(recursoParteDt);
								break;
						}
					}
				}
			//}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return dados;
	}
}
