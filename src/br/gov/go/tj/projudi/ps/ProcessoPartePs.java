package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.go.tj.projudi.dt.CidadeDt;
import br.gov.go.tj.projudi.dt.EnderecoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteAlcunhaDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoParteSinalDt;
import br.gov.go.tj.projudi.dt.ProcessoParteTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoStatusDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class ProcessoPartePs extends ProcessoPartePsGen {

	/**
     * 
     */
    private static final long serialVersionUID = 7304647952415990939L;

    public ProcessoPartePs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Consultar um promovente do processo
	 * @author Ronneesley Moura Teles
	 * @since 11/04/2008 - 14:07
	 * @return ProcessoParteDt
	 */
	public ProcessoParteDt consultarUmPromovente(String id_processo) throws Exception {
		return this.consultarParteTipo(id_processo, ProcessoParteTipoDt.POLO_ATIVO_CODIGO);
	}

	/**
	 * Consultar um promovido do processo
	 * @author Ronneesley Moura Teles
	 * @since 18/04/2008 - 09:35
	 * @param String id_processo, id do proceso
	 * @return ProcessoParteDt
	 * @throws Exception
	 */
	public ProcessoParteDt consultarUmPromovido(String id_processo) throws Exception{
		return this.consultarParteTipo(id_processo, ProcessoParteTipoDt.POLO_PASSIVO_CODIGO);
	}

	/**
	 * Consultar uma parte pelo tipo
	 * @param String id_processo, id do processo
	 * @param int codigoProcessoParteTipo, codigo do tipo da parte do processo 
	 * @return ProcessoParteDt
	 * @throws Exception
	 */
	public ProcessoParteDt consultarParteTipo(String id_processo, int codigoProcessoParteTipo) throws Exception {
		ProcessoParteDt parte = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM PROJUDI.VIEW_PROC_PARTE_COMPLETA";
		sql += " WHERE ID_PROC = ? AND DATA_BAIXA IS NULL";
		ps.adicionarLong(id_processo);
		sql += " AND PROC_PARTE_TIPO_CODIGO = ?";
		ps.adicionarLong(codigoProcessoParteTipo);

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			if (rs1.next()) {
				parte = new ProcessoParteDt();

				this.associarDados(parte, rs1);
			}
		
		}
		finally{
			try{if(rs1 != null) rs1.close();}catch(Exception e){}
		}

		return parte;
	}

	/**
	 * Consulta dados de uma parte
	 * 
	 * @param id_ProcessoParte, identificação da parte
	 * 
	 * @author msapaula
	 */
	public ProcessoParteDt consultarId(String id_ProcessoParte) throws Exception {
		String Sql;
		ProcessoParteDt Dados = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT * FROM PROJUDI.VIEW_PROC_PARTE_COMPLETA WHERE ID_PROC_PARTE = ?";
		ps.adicionarLong(id_ProcessoParte);

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				Dados = new ProcessoParteDt();
				this.associarDados(Dados, rs1);
				Dados.setCulpado(Funcoes.FormatarLogico(rs1.getString("CULPADO")));
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return Dados;
	}

	/**
	 * Associa os dados da parte atraves do resultset
	 * @author Ronneesley Moura Teles
	 * @since 11/04/2008 - 14:12
	 * @param ProcessoParte parte, Parte do processo a ser vinculada os dados
	 * @param ResultSetTJGO rs1, ResultSetTJGO com os dados do banco de dados
	 * @return boolean
	 * @throws Exception 
	 */
	private boolean associarDados(ProcessoParteDt parte, ResultSetTJGO rs1) throws Exception{

		parte.setId_ProcessoParte(rs1.getString("ID_PROC_PARTE"));

		parte.setNome(rs1.getString("NOME"));
		parte.setSexo(rs1.getString("SEXO"));
		parte.setNomeMae(rs1.getString("NOME_MAE"));
		parte.setNomePai(rs1.getString("NOME_PAI"));
		parte.setId_ProcessoParteTipo(rs1.getString("ID_PROC_PARTE_TIPO"));
		parte.setProcessoParteTipo(rs1.getString("PROC_PARTE_TIPO"));
		parte.setId_Processo(rs1.getString("ID_PROC"));
		parte.setProcessoNumero(rs1.getString("PROC_NUMERO"));
		parte.setId_ProcessoParteAusencia(rs1.getString("ID_PROC_PARTE_AUS"));
		parte.setProcessoParteAusencia(rs1.getString("PROC_PARTE_AUS"));
		parte.setId_Naturalidade(rs1.getString("ID_NATURALIDADE"));
		parte.setCidadeNaturalidade(rs1.getString("CIDADE_NATURALIDADE"));
		parte.setDataNascimento(Funcoes.FormatarData(rs1.getDateTime("DATA_NASCIMENTO")));
		parte.setId_EstadoCivil(rs1.getString("ID_ESTADO_CIVIL"));
		parte.setEstadoCivil(rs1.getString("ESTADO_CIVIL"));
		parte.setId_Profissao(rs1.getString("ID_PROFISSAO"));
		parte.setProfissao(rs1.getString("PROFISSAO"));
		parte.setId_Endereco(rs1.getString("ID_ENDERECO"));
		parte.setRg(rs1.getString("RG"));
		parte.setId_RgOrgaoExpedidor(rs1.getString("ID_RG_ORGAO_EXP"));
		parte.setOrgaoExpedidor(rs1.getString("SIGLA_ORGAO_EXPEDIDOR"), rs1.getString("ORGAO_EXPEDIDOR_UF"));
		parte.setRgDataExpedicao(Funcoes.FormatarData(rs1.getDateTime("RG_DATA_EXPEDICAO")));
		parte.setCpf(rs1.getString("CPF"));
		parte.setCnpj(Funcoes.completaCNPJZeros(rs1.getString("CNPJ")));
		parte.setTituloEleitor(rs1.getString("TITULO_ELEITOR"));
		parte.setTituloEleitorZona(rs1.getString("TITULO_ELEITOR_ZONA"));
		parte.setTituloEleitorSecao(rs1.getString("TITULO_ELEITOR_SECAO"));
		parte.setCtps(rs1.getString("CTPS"));
		parte.setCtpsSerie(rs1.getString("CTPS_SERIE"));
		parte.setId_CtpsUf(rs1.getString("ID_CTPS_UF"));
		parte.setEstadoCtpsUf(rs1.getString("ESTADO_CTPS_UF"));
		parte.setSiglaCtpsUf(rs1.getString("SIGLA_CTPS_UF"));
		parte.setPis(rs1.getString("PIS"));
		parte.setDataCadastro(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_CADASTRO")));
		parte.setEMail(rs1.getString("EMAIL"));
		parte.setTelefone(rs1.getString("TELEFONE"));
		parte.setCelular(rs1.getString("CELULAR"));
		parte.setCitacaoOnline(Funcoes.FormatarLogico(rs1.getString("CITACAO_ONLINE")));
		parte.setIntimacaoOnline(Funcoes.FormatarLogico(rs1.getString("INTIMACAO_ONLINE")));
		parte.setRecebeEMail(Funcoes.FormatarLogico(rs1.getString("RECEBE_EMAIL")));
		parte.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
		parte.setProcessoParteTipoCodigo(rs1.getString("PROC_PARTE_TIPO_CODIGO"));
		parte.setProcessoParteAusenciaCodigo(rs1.getString("PROC_PARTE_AUS_CODIGO"));
		parte.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
		parte.setUsuario(rs1.getString("USU"));
		parte.setId_Usuario(rs1.getString("ID_USU"));
		parte.setId_EmpresaTipo(rs1.getString("ID_EMPRESA_TIPO"));
		parte.setEmpresaTipo(rs1.getString("EMPRESA_TIPO"));
		parte.setId_GovernoTipo(rs1.getString("ID_GOVERNO_TIPO"));
		parte.setGovernoTipo(rs1.getString("GOVERNO_TIPO"));
		parte.setId_Escolaridade(rs1.getString("ID_ESCOLARIDADE"));
		parte.setEscolaridade(rs1.getString("ESCOLARIDADE"));
		parte.setId_Raca(rs1.getString("ID_RACA"));
		parte.setRaca(rs1.getString("RACA"));
		parte.setExcluido(rs1.getString("EXCLUIDO"));
		parte.setDataSentenca(Funcoes.FormatarData(rs1.getDateTime("DATA_SENTENCA")));
		parte.setDataPronuncia(Funcoes.FormatarData(rs1.getDateTime("DATA_PRONUNCIA")));
		//Endereco
		EnderecoDt enderecoDt = new EnderecoDt();
		enderecoDt.setId(rs1.getString("ID_ENDERECO"));
		enderecoDt.setLogradouro(rs1.getString("LOGRADOURO"));
		enderecoDt.setNumero(rs1.getString("NUMERO"));
		enderecoDt.setComplemento(rs1.getString("COMPLEMENTO"));
		enderecoDt.setCep(rs1.getString("CEP"));
		enderecoDt.setId_Bairro(rs1.getString("ID_BAIRRO"));
		enderecoDt.setBairro(rs1.getString("BAIRRO"));
		enderecoDt.setId_Cidade(rs1.getString("ID_CIDADE"));
		enderecoDt.setCidade(rs1.getString("CIDADE"));
		enderecoDt.setUf(rs1.getString("ESTADO"));

		parte.setEnderecoParte(enderecoDt);
		

		return true;
	}

	/**
	 * Consulta partes do processo
	 * @author Ronneesley Moura Teles
	 * @since 14/04/2008 - 16:25
	 * @param String id_processo, id do processo
	 * @param String processoParteTipoCodigo, codigo do tipo da parte do processo
	 * @return List
	 * @throws Exception
	 */
	public List consultarPartes(String id_processo, String processoParteTipoCodigo) throws Exception {
		List partes = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "SELECT * FROM PROJUDI.VIEW_PROC_PARTE_COMPLETA";
		sql += " WHERE ID_PROC = ? AND PROC_PARTE_TIPO_CODIGO = ?";
		ps.adicionarLong(id_processo);
		ps.adicionarLong(processoParteTipoCodigo);
		sql += " AND DATA_BAIXA IS NULL";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				ProcessoParteDt parte = new ProcessoParteDt();
				this.associarDados(parte, rs1);
				partes.add(parte);
			}
		
		}
		finally{
			try{if( rs1 != null ) rs1.close(); }catch(Exception e){}
		}

		return partes;
	}

	/**
	 * Consulta os dados da parte de acordo com Id_ProcessoParte passado
	 */
	public ProcessoParteDt consultarIdCompleto(String id_processoparte) throws Exception {

		String Sql, Sql1, Sql2;
		ProcessoParteDt Dados = new ProcessoParteDt();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT * FROM PROJUDI.VIEW_PROC_PARTE_COMPLETA WHERE ID_PROC_PARTE = ?";
		Sql1 = "SELECT * FROM PROJUDI.VIEW_PROC_PARTE_ALCUNHA WHERE ID_PROC_PARTE = ?";
		Sql2 = "SELECT * FROM PROJUDI.VIEW_PROC_PARTE_SINAL WHERE ID_PROC_PARTE = ?";		
		ps.adicionarLong(id_processoparte);

		try{
			//consulta dados da parte
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				this.associarDados(Dados, rs1);
			}
			//rs1.close();

			//consulta alcunhas da parte e monta lista
			rs1 = consultar(Sql1, ps);
			while (rs1.next()) {
				ProcessoParteAlcunhaDt ppa = new ProcessoParteAlcunhaDt();
				ppa.setAlcunha(rs1.getString("ALCUNHA"));
				ppa.setId(rs1.getString("ID_PROC_PARTE_ALCUNHA"));
				ppa.setId_Alcunha(rs1.getString("ID_ALCUNHA"));
				ppa.setId_ProcessoParte(rs1.getString("ID_PROC_PARTE"));
				Dados.addListaAlcunhaParte(ppa);
			}
			//rs1.close();

			//consulta sinais particular da parte e monta lista
			rs1 = consultar(Sql2, ps);
			while (rs1.next()) {
				ProcessoParteSinalDt ppa = new ProcessoParteSinalDt();
				ppa.setSinal(rs1.getString("SINAL"));
				ppa.setId(rs1.getString("ID_PROC_PARTE_SINAL"));
				ppa.setId_Sinal(rs1.getString("ID_SINAL"));
				ppa.setId_ProcessoParte(rs1.getString("ID_PROC_PARTE"));
				Dados.addListaSinalParte(ppa);
			}
			//rs1.close();

		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return Dados;
	}

	/**
	 * Consultar partes de um determinado processo, levando em consideração o tipo e status da parte.
	 * Consulta simples para listagem dos dados do processo.
	 * 
	 * @param id_Processo: identificação do processo
	 * @param parteTipoCodigo: tipo da parte
	 * @param ativa: booleano que especifica se devem ser retornadas somente as partes ativas
	 * 
	 * @author msapaula
	 */
	public List getListaPartes(String id_Processo, int parteTipoCodigo, boolean ativa) throws Exception {

		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT * FROM PROJUDI.VIEW_PROC_PARTE_COMPLETA WHERE ID_PROC = ?";
		ps.adicionarLong(id_Processo);
		if (parteTipoCodigo != -1) {
			Sql += " AND PROC_PARTE_TIPO_CODIGO = ?";
			ps.adicionarLong(parteTipoCodigo);
		} else {
			Sql += " AND PROC_PARTE_TIPO_CODIGO <> ?";
			ps.adicionarLong(ProcessoParteTipoDt.POLO_ATIVO_CODIGO);
			Sql += " AND PROC_PARTE_TIPO_CODIGO <> ?";
			ps.adicionarLong(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO);
			Sql += " AND PROC_PARTE_TIPO_CODIGO <> ?";
			ps.adicionarLong(ProcessoParteTipoDt.POLO_ATIVO_CODIGO);
			Sql += " AND PROC_PARTE_TIPO_CODIGO <> ?";
			ps.adicionarLong(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO);
		}
		if (ativa) Sql += " AND DATA_BAIXA IS NULL";
		Sql += " ORDER BY NOME";

		try{
			rs1 = consultar(Sql, ps);

			while (rs1.next()) {
				ProcessoParteDt obTemp = new ProcessoParteDt();
				obTemp.setId_ProcessoParte(rs1.getString("ID_PROC_PARTE"));
				obTemp.setNome(rs1.getString("NOME"));
				obTemp.setProcessoParteTipo(rs1.getString("PROC_PARTE_TIPO"));
				obTemp.setProcessoParteTipoCodigo(rs1.getString("PROC_PARTE_TIPO_CODIGO"));
				obTemp.setRg(rs1.getString("RG"));
				obTemp.setOrgaoExpedidor(rs1.getString("SIGLA_ORGAO_EXPEDIDOR"), rs1.getString("ORGAO_EXPEDIDOR_UF"));
				obTemp.setCpf(rs1.getString("CPF"));
				obTemp.setCnpj(Funcoes.completaCNPJZeros(rs1.getString("CNPJ")));
				obTemp.setTelefone(rs1.getString("TELEFONE"));
				obTemp.setDataBaixa(Funcoes.FormatarData(rs1.getDateTime("DATA_BAIXA")));
				obTemp.setDataNascimento(Funcoes.FormatarData(rs1.getDateTime("DATA_NASCIMENTO")));
				obTemp.setIdade(String.valueOf(Funcoes.calculeIdade(obTemp.getDataNascimento())));
				obTemp.setId_ProcessoParteAusencia(rs1.getString("ID_PROC_PARTE_AUS"));
				obTemp.setEMail(rs1.getString("EMAIL"));

				//Setando endereço
				EnderecoDt enderecoDt = new EnderecoDt();
				enderecoDt.setId(rs1.getString("ID_ENDERECO"));
				enderecoDt.setLogradouro(rs1.getString("LOGRADOURO"));
				enderecoDt.setNumero(rs1.getString("NUMERO"));
				enderecoDt.setComplemento(rs1.getString("COMPLEMENTO"));
				enderecoDt.setCep(rs1.getString("CEP"));
				enderecoDt.setId_Bairro(rs1.getString("ID_BAIRRO"));
				enderecoDt.setBairro(rs1.getString("BAIRRO"));
				enderecoDt.setCidade(rs1.getString("CIDADE"));
				enderecoDt.setUf(rs1.getString("ESTADO"));
				obTemp.setEnderecoParte(enderecoDt);
				liTemp.add(obTemp);
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return liTemp;
	}

	/**
	 * Retorna o RG da parte com o orgao expedidor e o UF
	 * @author Ronneesley Moura Teles
	 * @since 28/03/2008
	 * @return String
	 */
	public String consultarRgCompleto(String id_processoparte) throws Exception {
		String rgCompleto = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "SELECT oe.RG_ORGAO_EXP, e.UF, pp.RG FROM PROJUDI.PROC_PARTE pp " + " left outer JOIN PROJUDI.RG_ORGAO_EXP oe " + " on oe.ID_RG_ORGAO_EXP = pp.ID_RG_ORGAO_EXP " + " left outer JOIN PROJUDI.ESTADO e " + " on e.ID_ESTADO = oe.ID_ESTADO " + " WHERE ID_PROC_PARTE = ?";
		ps.adicionarLong(id_processoparte);

		try{
			rs1 = this.consultar(sql, ps);

			//Se possui proximo registro
			if (rs1.next()) {
				String rg = rs1.getString("RG");

				if (rg != "" && rg != null) rgCompleto = rg + " " + rs1.getString("RG_ORGAO_EXP") + "/" + rs1.getString("UF");
			}

			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 

		return rgCompleto;
	}

	/**
	 * Sobrecarga do metodo para consultarRgCompleto, pelo int do id
	 * @author Ronneesley Moura Teles
	 * @param id_processoparte
	 * @since 28/03/2008
	 * @return String
	 * @throws Exception 
	 */
	public String consultarRgCompleto(int id_processoparte) throws Exception{
		return this.consultarRgCompleto(String.valueOf(id_processoparte));
	}

	/**
	 * Sobrecarga do metodo para consultarRgCompleto, pelo bean
	 * @author Ronneesley Moura Teles
	 * @param processoParte
	 * @since 28/03/2008
	 * @return String
	 * @throws Exception 
	 */
	public String consultarRgCompleto(ProcessoParteDt processoParte) throws Exception{
		//Evita a tentativa de pesquisa quando o rg e nulo
		if (processoParte.getRg() == null) return "";

		return this.consultarRgCompleto(processoParte.getId_Processo());
	}

	/**
	 * Retorna o endereco de uma parte
	 * @author Ronneesley Moura Teles
	 * @since 28/03/2008
	 * @return EnderecoDt
	 * @throws Exception
	 */
	/**
	public EnderecoDt consultarEndereco(String id_endereco){
		EnderecoPs enderecoPs = new EnderecoPs();		
		EnderecoDt endereco = enderecoPs.consultarId(id_endereco);
		return endereco;
	}
	*/

	/**
	 * Sobrecarga do metodo consultarEndereco
	 * @author Ronneesley Moura Teles
	 * @since 28/03/2008
	 * @param id_endereco
	 * @return EnderecoDt
	 * @throws Exception
	 */
	/**
	public EnderecoDt consultarEndereco(int id_endereco){
		return this.consultarEndereco(String.valueOf(id_endereco));
	}
	*/

	/**
	 * Retorna o endereco de uma parte, atribuindo a propriedade endereco parte
	 * @author Ronneesley Moura Teles
	 * @since 28/03/2008
	 * @param processoParte
	 * @return EnderecoDt
	 * @throws Exception
	 */
	/**
	public EnderecoDt consultarEndereco(ProcessoParteDt processoParte){
		EnderecoDt enderecoDt = this.consultarEndereco(processoParte.getEndereco());
		processoParte.setEnderecoParte(enderecoDt);
		return enderecoDt;
	}
	*/

	/**
	 * Consulta geral de partes usada no Cadastro de Processo.
	 * Procura se determinada parte já é cadastrada e retorna alguns dados
	 * que poderão ser reutilizados
	 */
	public ProcessoParteDt consultarParte(String cpfCnpj, String rg, String ctps, String tituloEleitor, String pis) throws Exception {

		String Sql1;
		String Sql2 = "";
		ProcessoParteDt Dados = new ProcessoParteDt();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql1 = " SELECT * FROM VIEW_PROC_PARTE_COMPLETA p";

		if (cpfCnpj.length() > 0){
			Sql2 += " WHERE (p.CPF = ? OR p.CNPJ= ?)";
			ps.adicionarLong(cpfCnpj);
			ps.adicionarLong(cpfCnpj);
		}
		if (rg.length() > 0){
			Sql2 += (Sql2.length() > 0 ? " AND " : " WHERE ") + " p.RG = ?";
			ps.adicionarString(rg);
		}
		if (ctps.length() > 0){
			Sql2 += (Sql2.length() > 0 ? " AND " : " WHERE ") + " p.CTPS = ?";
			ps.adicionarLong(ctps);
		}
		if (tituloEleitor.length() > 0){
			Sql2 += (Sql2.length() > 0 ? " AND " : " WHERE ") + " p.TITULO_ELEITOR = ?";
			ps.adicionarString(tituloEleitor);
		}
		if (pis.length() > 0){
			Sql2 += (Sql2.length() > 0 ? " AND " : " WHERE ") + " p.PIS = ?";
			ps.adicionarString(pis);			
		}
		
		
		Sql1 += Sql2 + " ORDER BY ID_PROC_PARTE DESC ";
		Sql1 +=  " OFFSET 1 ROWS FETCH NEXT  1 ROWS ONLY";
		
		try{
			rs1 = consultar(Sql1, ps);
			if (rs1.next()) {
				Dados.setNome(rs1.getString("NOME"));
				Dados.setSexo(rs1.getString("SEXO"));
				Dados.setNomeMae(rs1.getString("NOME_MAE"));
				Dados.setNomePai(rs1.getString("NOME_PAI"));
				Dados.setId_Naturalidade(rs1.getString("ID_NATURALIDADE"));
				Dados.setCidadeNaturalidade(rs1.getString("CIDADE_NATURALIDADE"));
				Dados.setDataNascimento(Funcoes.FormatarData(rs1.getDateTime("DATA_NASCIMENTO")));
				Dados.setId_EstadoCivil(rs1.getString("ID_ESTADO_CIVIL"));
				Dados.setEstadoCivil(rs1.getString("ESTADO_CIVIL"));
				Dados.setId_Profissao(rs1.getString("ID_PROFISSAO"));
				Dados.setProfissao(rs1.getString("PROFISSAO"));
				Dados.setId_Endereco(rs1.getString("ID_ENDERECO"));
				Dados.setRg(rs1.getString("RG"));
				Dados.setId_RgOrgaoExpedidor(rs1.getString("ID_RG_ORGAO_EXP"));
				Dados.setOrgaoExpedidor(rs1.getString("SIGLA_ORGAO_EXPEDIDOR"), rs1.getString("ORGAO_EXPEDIDOR_UF"));
				Dados.setRgDataExpedicao(Funcoes.FormatarData(rs1.getDateTime("RG_DATA_EXPEDICAO")));
				Dados.setCpf(rs1.getString("CPF"));
				Dados.setCnpj(Funcoes.completaCNPJZeros(rs1.getString("CNPJ")));
				Dados.setTituloEleitor(rs1.getString("TITULO_ELEITOR"));
				Dados.setTituloEleitorZona(rs1.getString("TITULO_ELEITOR_ZONA"));
				Dados.setTituloEleitorSecao(rs1.getString("TITULO_ELEITOR_SECAO"));
				Dados.setCtps(rs1.getString("CTPS"));
				Dados.setCtpsSerie(rs1.getString("CTPS_SERIE"));
				Dados.setId_CtpsUf(rs1.getString("ID_CTPS_UF"));
				Dados.setEstadoCtpsUf(rs1.getString("SIGLA_CTPS_UF"));
				Dados.setPis(rs1.getString("PIS"));
				Dados.setEMail(rs1.getString("EMAIL"));
				Dados.setTelefone(rs1.getString("TELEFONE"));
				Dados.setCelular(rs1.getString("CELULAR"));
				Dados.setCitacaoOnline(Funcoes.FormatarLogico(rs1.getString("CITACAO_ONLINE")));
				Dados.setIntimacaoOnline(Funcoes.FormatarLogico(rs1.getString("INTIMACAO_ONLINE")));
				Dados.setRecebeEMail(Funcoes.FormatarLogico(rs1.getString("RECEBE_EMAIL")));
				Dados.setProcessoParteTipoCodigo(rs1.getString("PROC_PARTE_TIPO_CODIGO"));
				//Dados.setProcessoParteAusenciaCodigo(rs1.getString("PROC_PARTE_AUS_CODIGO"));
				//Dados.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
				//Dados.setCitada(Funcoes.FormatarLogico(rs1.getString("CITADA")));
				//Dados.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
				//Dados.setUsuario(rs1.getString("USU"));
				Dados.setId_EmpresaTipo(rs1.getString("ID_EMPRESA_TIPO"));
				Dados.setEmpresaTipo(rs1.getString("EMPRESA_TIPO"));
				Dados.setId_GovernoTipo(rs1.getString("ID_GOVERNO_TIPO"));
				Dados.setGovernoTipo(rs1.getString("GOVERNO_TIPO"));

				// Setando endereço
				EnderecoDt enderecoDt = new EnderecoDt();
				enderecoDt.setLogradouro(rs1.getString("LOGRADOURO"));
				enderecoDt.setNumero(rs1.getString("NUMERO"));
				enderecoDt.setComplemento(rs1.getString("COMPLEMENTO"));
				enderecoDt.setCep(rs1.getString("CEP"));
				enderecoDt.setId_Bairro(rs1.getString("ID_BAIRRO"));
				enderecoDt.setBairro(rs1.getString("BAIRRO"));
				enderecoDt.setId_Cidade(rs1.getString("ID_CIDADE"));
				enderecoDt.setCidade(rs1.getString("CIDADE"));
				enderecoDt.setUf(rs1.getString("ESTADO"));
				Dados.setEnderecoParte(enderecoDt);
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return Dados;
	}

	/**
	 * Consulta geral de partes utilizada no Cadastro de Processo de Execucao Penal.
	 * Verifica as partes que possuem Processo de Execucao não arquivado, e retorna 
	 * uma lista com os dados da parte, inclusive alcunha e sinal particular
	 */
	public List listarPartesProcessoExecucao(String cpfCnpj, String nome, String mae, String dataNascimento, String numeroProcesso, String digito, String idServentia, String posicao) throws Exception {

		String Sql1;
		String SqlCondicao = "";
		List lista = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		// seleciona as partes
		Sql1 = " SELECT distinct p.* FROM VIEW_PROC_EXE pe";
//		Sql1 += " INNER JOIN VIEW_PROC pro on pe.ID_PROC_EXE_PENAL = pro.ID_PROC";
		Sql1 += " INNER JOIN VIEW_PROC_PARTE_COMPLETA p on pe.ID_PROC_EXE_PENAL = p.ID_PROC";
		Sql1 += " WHERE pe.PROC_STATUS_CODIGO <> ?";
		ps.adicionarLong(ProcessoStatusDt.ARQUIVADO);		
		
		if (numeroProcesso.length() > 0){
			SqlCondicao += " AND p.PROC_NUMERO = ?";
			ps.adicionarLong(numeroProcesso);
		}
		if (digito.length() > 0){
			SqlCondicao += " AND pe.DIGITO_VERIFICADOR = ?";
			ps.adicionarLong(digito);
		}
		if (cpfCnpj.length() > 0){
			SqlCondicao += " AND p.CPF = ?";
			ps.adicionarLong(cpfCnpj);
		}
		if (nome.length() > 0){
			SqlCondicao += " AND p.NOME LIKE ? ";
			ps.adicionarString( nome + "%");
		}
		if (mae.length() > 0) {
			SqlCondicao += " AND p.NOME_MAE LIKE ? ";
			ps.adicionarString("%"+ mae + "%");
		}
		if (dataNascimento.length() > 0){
			SqlCondicao += " AND p.DATA_NASCIMENTO = ?";
			ps.adicionarDate(dataNascimento);
		}
		if (idServentia.length() > 0){
			SqlCondicao += " AND pe.ID_SERV = ?";
			ps.adicionarLong(idServentia);
		}
		
		SqlCondicao += " AND pe.PROC_TIPO_CODIGO in (?,?,?)"; 
		ps.adicionarLong(ProcessoTipoDt.EXECUCAO_DA_PENA);
		ps.adicionarLong(ProcessoTipoDt.CALCULO_DE_LIQUIDACAO_DE_PENA);
		ps.adicionarLong(ProcessoTipoDt.EXECUCAO_DA_PENA);
		
		SqlCondicao += " ORDER BY p.NOME";
		
		Sql1 += SqlCondicao;	

		try{
			//posicao == null: quando não é necessário a paginação
			if (posicao != null) rs1 = consultarPaginacao(Sql1, ps, posicao);
			else rs1 = consultar(Sql1, ps);			
			while (rs1.next()) {
				ProcessoParteDt parte = new ProcessoParteDt();
				this.associarDados(parte, rs1);

				/**
				 * retirada a pesquisa, pois está tornando a consulta lenta e os usuários não estão cadastrando sinal particular e alcunha, pois é um cadastro separado.
				 */
//				//pesquisa e inclui a alcunha na parte
//				String sql3 = "SELECT * FROM VIEW_PROC_PARTE_ALCUNHA ppa WHERE ppa.ID_PROC_PARTE = " + parte.getId_ProcessoParte();
//				rs3 = consultar(sql3);
//				while (rs3.next()) {
//					ProcessoParteAlcunhaDt ppa = new ProcessoParteAlcunhaDt();
//					ppa.setId(rs3.getString("ID_PROC_PARTE_ALCUNHA"));
//					ppa.setId_Alcunha(rs3.getString("ID_ALCUNHA"));
//					ppa.setId_ProcessoParte(rs3.getString("ID_PROC_PARTE"));
//					ppa.setAlcunha(rs3.getString("ALCUNHA"));
//
//					//inclui a alcunha na parte
//					parte.addListaAlcunhaParte(ppa);
//				}
//
//				// pesquisa e inclui o sinal particular na parte
//				String sql4 = "SELECT * FROM VIEW_PROC_PARTE_SINAL pps WHERE pps.ID_PROC_PARTE = " + parte.getId_ProcessoParte();
//				rs4 = consultar(sql4);
//				while (rs4.next()) {
//					ProcessoParteSinalDt pps = new ProcessoParteSinalDt();
//					pps.setId(rs4.getString("ID_PROC_PARTE_SINAL"));
//					pps.setId_Sinal(rs4.getString("ID_SINAL"));
//					pps.setId_ProcessoParte(rs4.getString("ID_PROC_PARTE"));
//					pps.setSinal(rs4.getString("SINAL"));
//
//					//inclui o sinal particular na parte
//					parte.addListaSinalParte(pps);
//				}

				//Inclui a parte na lista
				lista.add(parte);
			}

			//posicao == null: quando não é necessário a paginação
			if (posicao != null){
				String sql3 = " SELECT COUNT(*) AS QUANTIDADE FROM " +
						"(SELECT distinct p.* FROM VIEW_PROC_EXE pe" + 
						" INNER JOIN VIEW_PROC pro on pe.ID_PROC_EXE_PENAL = pro.ID_PROC" +
						" INNER JOIN VIEW_PROC_PARTE_COMPLETA p on pro.ID_PROC = p.ID_PROC" + 
						"  WHERE pro.PROC_STATUS_CODIGO <> ?" ;				
				sql3 += SqlCondicao;
				sql3 += ") lista";
	
				rs2 = consultar(sql3, ps);
				if (rs2.next()) lista.add(rs2.getLong("QUANTIDADE"));				
			}
		
        } finally{
        	try{if(rs1 != null) rs1.close(); } catch(Exception e){}
        	try{if(rs2 != null) rs2.close(); } catch(Exception e){}
        } 
		return lista;
	}

	/**
	 * Sobrescrevendo método inserir para setar DataCadastro com a data do banco
	 */
	public void inserir(ProcessoParteDt dados) throws Exception {
		String SqlCampos;
		String SqlValores;
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		SqlCampos = "INSERT INTO PROJUDI.PROC_PARTE ("; 
		SqlValores = " Values (";
		
		if (!(dados.getNome().length() == 0)){
			SqlCampos += "NOME";
			SqlValores += "?";
			ps.adicionarString(dados.getNome());
		}
		if (!(dados.getSexo().length() == 0)){
			SqlCampos += ",SEXO";
			SqlValores += ",?";
			ps.adicionarString(dados.getSexo());
		}
		if (!(dados.getNomeMae().length() == 0)){
			SqlCampos += ",NOME_MAE";
			SqlValores += ",?";
			ps.adicionarString(dados.getNomeMae());
		}		
		if (!(dados.getNomePai().length() == 0)){
			SqlCampos += ",NOME_PAI";
			SqlValores += ",?";
			ps.adicionarString(dados.getNomePai());
		}
		
		if (dados.getId_ProcessoParteTipo().length() > 0){
			SqlCampos += ",ID_PROC_PARTE_TIPO";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_ProcessoParteTipo());
		} else if (dados.getProcessoParteTipoCodigo().length() > 0){
			SqlCampos += ",ID_PROC_PARTE_TIPO";
			SqlValores += ", (SELECT ID_PROC_PARTE_TIPO FROM PROC_PARTE_TIPO WHERE PROC_PARTE_TIPO_CODIGO = ?)";
			ps.adicionarLong(dados.getProcessoParteTipoCodigo());
		}	
		if (!(dados.getId_Processo().length() == 0)){
			SqlCampos += ",ID_PROC";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_Processo());
		}
		if (!(dados.getId_ProcessoParteAusencia().length() == 0)){
			SqlCampos += ",ID_PROC_PARTE_AUS";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_ProcessoParteAusencia());
		}
		if (!(dados.getId_Naturalidade().length() == 0)){
			SqlCampos += ",ID_NATURALIDADE";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_Naturalidade());
		}
		if (!(dados.getDataNascimento().length() == 0)){
			SqlCampos += ",DATA_NASCIMENTO";
			SqlValores += ",?";
			ps.adicionarDate(dados.getDataNascimento());
		}
		if (!(dados.getId_EstadoCivil().length() == 0)){
			SqlCampos += ",ID_ESTADO_CIVIL";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_EstadoCivil());
		}
		if (!(dados.getId_Profissao().length() == 0)){
			SqlCampos += ",ID_PROFISSAO";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_Profissao());
		}
		if (!(dados.getId_Endereco().length() == 0)){
			SqlCampos += ",ID_ENDERECO";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_Endereco());
		}
		if (!(dados.getId_Escolaridade().length() == 0)){
			SqlCampos += ",ID_ESCOLARIDADE";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_Escolaridade());
		}
		if (!(dados.getRg().length() == 0)){
			SqlCampos += ",RG";
			SqlValores += ",?";
			ps.adicionarString(dados.getRg());
		}
		if (!(dados.getId_RgOrgaoExpedidor().length() == 0)){
			SqlCampos += ",ID_RG_ORGAO_EXP";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_RgOrgaoExpedidor());
		}
		if (!(dados.getRgDataExpedicao().length() == 0)){
			SqlCampos += ",RG_DATA_EXPEDICAO";
			SqlValores += ",?";
			ps.adicionarDate(dados.getRgDataExpedicao());
		}
		if (!(dados.getCpf().length() == 0)){
			SqlCampos += ",CPF";
			SqlValores += ",?";
			ps.adicionarLong(dados.getCpf());
		}
		if (!(dados.getCnpj().length() == 0)){
			SqlCampos += ",CNPJ";
			SqlValores += ",?";
			ps.adicionarLong(dados.getCnpj());
		}
		if (!(dados.getTituloEleitor().length() == 0)){
			SqlCampos += ",TITULO_ELEITOR";
			SqlValores += ",?";
			ps.adicionarString(dados.getTituloEleitor());
		}
		if (!(dados.getTituloEleitorZona().length() == 0)){
			SqlCampos += ",TITULO_ELEITOR_ZONA";
			SqlValores += ",?";
			ps.adicionarLong(dados.getTituloEleitorZona());
		}
		if (!(dados.getTituloEleitorSecao().length() == 0)){
			SqlCampos += ",TITULO_ELEITOR_SECAO";
			SqlValores += ",?";
			ps.adicionarLong(dados.getTituloEleitorSecao());
		}
		if (!(dados.getCtps().length() == 0)){
			SqlCampos += ",CTPS";
			SqlValores += ",?";
			ps.adicionarLong(dados.getCtps());
		}
		if (!(dados.getCtpsSerie().length() == 0)){
			SqlCampos += ",CTPS_SERIE";
			SqlValores += ",?";
			ps.adicionarLong(dados.getCtpsSerie());
		}
		if (!(dados.getId_CtpsUf().length() == 0)){
			SqlCampos += ",ID_CTPS_UF";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_CtpsUf());
		}
		if (!(dados.getPis().length() == 0)){
			SqlCampos += ",PIS";
			SqlValores += ",?";
			ps.adicionarString(dados.getPis());
		}
		SqlCampos += " ,DATA_CADASTRO";
		SqlValores += ",?";
		ps.adicionarDateTime(new Date());
		if (!(dados.getEMail().length() == 0)){
			SqlCampos += ",EMAIL";
			SqlValores += ",?";
			ps.adicionarString(dados.getEMail());
		}
		if (!(dados.getTelefone().length() == 0)){
			SqlCampos += ",TELEFONE";
			SqlValores += ",?";
			ps.adicionarString(dados.getTelefone());
		}
		if (!(dados.getCelular().length() == 0)){
			SqlCampos += ",CELULAR";
			SqlValores += ",?";
			ps.adicionarString(dados.getCelular());
		}
		if (!(dados.getCitacaoOnline().length() == 0)){
			SqlCampos += ",CITACAO_ONLINE";
			SqlValores += ",?";
			ps.adicionarBoolean(dados.getCitacaoOnline());
		}
		if (!(dados.getIntimacaoOnline().length() == 0)){
			SqlCampos += ",INTIMACAO_ONLINE";
			SqlValores += ",?";
			ps.adicionarBoolean(dados.getIntimacaoOnline());
		}
		if (!(dados.getRecebeEMail().length() == 0)){
			SqlCampos += ",RECEBE_EMAIL";
			SqlValores += ",?";
			ps.adicionarBoolean(dados.getRecebeEMail());
		}
		if ((dados.getCitada().length() > 0)){
			SqlCampos += ",CITADA ";
			SqlValores += ",?";
			ps.adicionarBoolean(dados.getCitada());
		}
		if ((dados.getEmpresaTipo().length() > 0)){
			SqlCampos += ",ID_EMPRESA_TIPO ";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_EmpresaTipo());
		}
		if ((dados.getGovernoTipo().length() > 0)){
			SqlCampos += ",ID_GOVERNO_TIPO ";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_GovernoTipo());
		}
		if ((dados.getNome().length() > 0)){
			SqlCampos += ",NOME_SIMPLIFICADO ";
			SqlValores += ",?";
			ps.adicionarString(Funcoes.converteNomeSimplificado(dados.getNome()));
		}	
		if (!(dados.getDataSentenca().length() == 0)){
			SqlCampos += ",DATA_SENTENCA";
			SqlValores += ",?";
			ps.adicionarDate(dados.getDataSentenca());
		}
		if (!(dados.getDataPronuncia().length() == 0)){
			SqlCampos += ",DATA_PRONUNCIA";
			SqlValores += ",?";
			ps.adicionarDate(dados.getDataPronuncia());
		}
		
		SqlCampos+=")";
 		SqlValores+=")"; 		

 		Sql = SqlCampos.replace("(,", "(") + SqlValores.replace("(,", "(");	

			
			dados.setId(executarInsert(Sql, "ID_PROC_PARTE", ps));

	}

	/**
	 * Sobrescrevendo método alterar para que baseado no ProcessoParteTipoCodigo possa obter o Id_ProcessoParteTipo a ser gravado
	 */
	public void alterar(ProcessoParteDt dados) throws Exception {

		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.PROC_PARTE SET  ";
		if (!(dados.getNome().length() == 0)){
			Sql += "NOME = ?";
			ps.adicionarString(dados.getNome());
		}
		if (!(dados.getSexo().length() == 0)){
			Sql += ",SEXO = ?";
			ps.adicionarString(dados.getSexo());
		}
		if (dados.getNomeMae() != null && !dados.getNomeMae().equalsIgnoreCase("null")){
			Sql += ",NOME_MAE = ?";
			ps.adicionarString(dados.getNomeMae());
		}
		if (dados.getNomePai() != null && !dados.getNomePai().equalsIgnoreCase("null")){
			Sql += ",NOME_PAI = ?";
			ps.adicionarString(dados.getNomePai());
		}

		if (dados.getId_ProcessoParteTipo().length() > 0) {
			Sql += ",ID_PROC_PARTE_TIPO = ?";
			ps.adicionarLong(dados.getId_ProcessoParteTipo());
		} else if (dados.getProcessoParteTipoCodigo().length()>0){
			Sql += ",ID_PROC_PARTE_TIPO = (SELECT ID_PROC_PARTE_TIPO FROM PROJUDI.PROC_PARTE_TIPO WHERE PROC_PARTE_TIPO_CODIGO = ?)";
			ps.adicionarLong(dados.getProcessoParteTipoCodigo());
		}

		if (!(dados.getId_Processo().length() == 0)){
			Sql += ",ID_PROC = ?";
			ps.adicionarLong(dados.getId_Processo());
		}
		if (!(dados.getId_ProcessoParteAusencia().length() == 0)){
			Sql += ",ID_PROC_PARTE_AUS = ?";
			ps.adicionarLong(dados.getId_ProcessoParteAusencia());
		}
		if (dados.getId_Naturalidade() != null && !dados.getId_Naturalidade().equalsIgnoreCase("null")){
			Sql += ",ID_NATURALIDADE = ?";
			ps.adicionarLong(dados.getId_Naturalidade());
		}
		if (dados.getDataNascimento() != null && !dados.getDataNascimento().equalsIgnoreCase("null")){
			Sql += ",DATA_NASCIMENTO = ?";
			ps.adicionarDate(dados.getDataNascimento());
		}
		if (dados.getEstadoCivil() != null && !dados.getEstadoCivil().equalsIgnoreCase("null")){
			Sql += ",ID_ESTADO_CIVIL = ?";
			ps.adicionarLong(dados.getId_EstadoCivil());
		}
		if (dados.getId_Escolaridade() != null && !dados.getId_Escolaridade().equalsIgnoreCase("null")){
			Sql += ",ID_ESCOLARIDADE = ?";
			ps.adicionarLong(dados.getId_Escolaridade());
		}
		if (dados.getId_Profissao() != null && !dados.getId_Profissao().equalsIgnoreCase("null")){
			Sql += ",ID_PROFISSAO = ?";
			ps.adicionarLong(dados.getId_Profissao());
		}
		if (!(dados.getId_Endereco().length() == 0)){
			Sql += ",ID_ENDERECO = ?";
			ps.adicionarLong(dados.getId_Endereco());
		}
		if (dados.getRg() != null && !dados.getRg().equalsIgnoreCase("null")){
			Sql += ",RG = ?";
			ps.adicionarString(dados.getRg());
		}
		if (dados.getId_RgOrgaoExpedidor() != null && !dados.getId_RgOrgaoExpedidor().equalsIgnoreCase("null")){
			Sql += ",ID_RG_ORGAO_EXP = ?";
			ps.adicionarLong(dados.getId_RgOrgaoExpedidor());
		}
		if (dados.getRgDataExpedicao() != null && !dados.getRgDataExpedicao().equalsIgnoreCase("null")){
			Sql += ",RG_DATA_EXPEDICAO = ?";
			ps.adicionarDate(dados.getRgDataExpedicao());
		}
		if (dados.getCpf() != null && !dados.getCpf().equalsIgnoreCase("null")){
			Sql += ",CPF = ?";
			ps.adicionarLong(dados.getCpf());
		}
		if (dados.getCnpj() != null && !dados.getCnpj().equalsIgnoreCase("null")){
			Sql += ",CNPJ = ?";
			ps.adicionarLong(dados.getCnpj());
		}
		if (!(dados.getCulpado().length() == 0)){
			Sql += ",CULPADO = ?";
			ps.adicionarBoolean(dados.getCulpado());
		} else{
			Sql += ",CULPADO = ?";
			ps.adicionarBoolean("0");
		}
		if (dados.getTituloEleitor() != null && !dados.getTituloEleitor().equalsIgnoreCase("null")){
			Sql += ",TITULO_ELEITOR = ?";
			ps.adicionarString(dados.getTituloEleitor());
		}
		if (dados.getTituloEleitorZona() != null && !dados.getTituloEleitorZona().equalsIgnoreCase("null")){
			Sql += ",TITULO_ELEITOR_ZONA = ?";
			ps.adicionarLong(dados.getTituloEleitorZona());
		}
		if (dados.getTituloEleitorSecao() != null && !dados.getTituloEleitorSecao().equalsIgnoreCase("null")){
			Sql += ",TITULO_ELEITOR_SECAO = ?";
			ps.adicionarLong(dados.getTituloEleitorSecao());
		}
		if (dados.getCtps() != null && !dados.getCtps().equalsIgnoreCase("null")){
			Sql += ",CTPS = ?";
			ps.adicionarLong(dados.getCtps());
		}
		if (dados.getCtpsSerie() != null && !dados.getCtpsSerie().equalsIgnoreCase("null")){
			Sql += ",CTPS_SERIE = ?";
			ps.adicionarLong(dados.getCtpsSerie());
		}
		if (dados.getId_CtpsUf() != null && !dados.getId_CtpsUf().equalsIgnoreCase("null")){
			Sql += ",ID_CTPS_UF = ?";
			ps.adicionarLong(dados.getId_CtpsUf());
		}
		if (dados.getPis() != null && !dados.getPis().equalsIgnoreCase("null")){
			Sql += ",PIS = ?";
			ps.adicionarString(dados.getPis());
		}
		if (!(dados.getDataCadastro().length() == 0)){
			Sql += ",DATA_CADASTRO = ?";
			ps.adicionarDateTime(dados.getDataCadastro());
		}
		if (dados.getEMail() != null && !dados.getEMail().equalsIgnoreCase("null")){
			Sql += ",EMAIL = ?";
			ps.adicionarString(dados.getEMail());
		}
		if (dados.getTelefone() != null && !dados.getTelefone().equalsIgnoreCase("null")){
			Sql += ",TELEFONE = ?";
			ps.adicionarString(dados.getTelefone());
		}
		if (dados.getCelular() != null && !dados.getCelular().equalsIgnoreCase("null")){
			Sql += ",CELULAR = ?";
			ps.adicionarString(dados.getCelular());
		}
		if (!(dados.getCitacaoOnline().length() == 0)){
			Sql += ",CITACAO_ONLINE = ?";
			ps.adicionarBoolean(dados.getCitacaoOnline());
		}
		if (!(dados.getIntimacaoOnline().length() == 0)){
			Sql += ",INTIMACAO_ONLINE = ?";
			ps.adicionarBoolean(dados.getIntimacaoOnline());
		}
		if (!(dados.getRecebeEMail().length() == 0)){
			Sql += ",RECEBE_EMAIL = ?";
			ps.adicionarBoolean(dados.getRecebeEMail());
		}
		Sql += ",CODIGO_TEMP = null";
		if (!(dados.getId_EmpresaTipo().length() == 0)){
			Sql += ",ID_EMPRESA_TIPO = ?";
			ps.adicionarLong(dados.getId_EmpresaTipo());
		}
		if (!(dados.getId_GovernoTipo().length() == 0)){
			Sql += ",ID_GOVERNO_TIPO = ?";
			ps.adicionarLong(dados.getId_GovernoTipo());
		}
		if (!(dados.getNome().length() == 0)){
			Sql += ",NOME_SIMPLIFICADO = ?";
			ps.adicionarString(Funcoes.converteNomeSimplificado(dados.getNome()));
		}
		if (!(dados.getId_Raca().length() == 0)){
			Sql += ",ID_RACA = ?";
			ps.adicionarLong(dados.getId_Raca());
		}
		if (dados.getDataSentenca() != null && !dados.getDataSentenca().equalsIgnoreCase("null")){
			Sql += ",DATA_SENTENCA = ?";
			ps.adicionarDate(dados.getDataSentenca());
		}
		if (dados.getDataPronuncia() != null && !dados.getDataPronuncia().equalsIgnoreCase("null")){
			Sql += ",DATA_PRONUNCIA = ?";
			ps.adicionarDate(dados.getDataPronuncia());
		}
		Sql = Sql.replace("SET  ,", "SET  ");
		Sql = Sql + " WHERE ID_PROC_PARTE = ?";
		ps.adicionarLong(dados.getId_ProcessoParte());

			executarUpdateDelete(Sql, ps);
	}

	/**
	 * Método que registra a baixa de uma parte em um processo
	 * Armazena data em campo DataBaixa
	 * @throws Exception 
	 */
	public void desabilitaParteProcesso(String id_ProcessoParte) throws Exception {

		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.PROC_PARTE SET DATA_BAIXA = ? ";
		ps.adicionarDateTime(new Date());
		Sql += ", EXCLUIDO = ?";
		ps.adicionarLong(0);
		Sql += " WHERE ID_PROC_PARTE = ?";
		ps.adicionarLong(id_ProcessoParte);

			executarUpdateDelete(Sql, ps);

	}
	
	/**
	 * Realiza a exclusão de uma parte de um determinado processo.
	 * @param id_ProcessoParte - ID da parte a ser excluída
	 * @throws Exception
	 * @author hmgodinho
	 */
	public void excluirParteProcesso(String id_ProcessoParte) throws Exception {

		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.PROC_PARTE SET DATA_BAIXA = ? ";
		ps.adicionarDateTime(new Date());
		Sql += ", EXCLUIDO = ?";
		ps.adicionarLong(ProcessoParteDt.EXCLUIDO);
		Sql += " WHERE ID_PROC_PARTE = ?";
		ps.adicionarLong(id_ProcessoParte);

			executarUpdateDelete(Sql, ps);

	}

	/**
	 * Restaura uma parte de um determinado processo.
	 * @param id_ProcessoParte - ID da parte a ser restaurada
	 * @throws Exception
	 * @author hmgodinho
	 */
	public void restauraParteProcesso(String id_ProcessoParte) throws Exception {

		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.PROC_PARTE SET EXCLUIDO = ?";
		ps.adicionarLong(0);
		Sql += ", DATA_BAIXA = ?";
		ps.adicionarNull(0);
		Sql += " WHERE ID_PROC_PARTE = ?";
		ps.adicionarLong(id_ProcessoParte);

			executarUpdateDelete(Sql, ps);

	}

	/**
	 * Consulta se parte é cadastrada como usuário. 
	 * Retorna o Id_UsuarioServentia da parte 
	 * @param String id_processoparte, id da parte
	 */
	public String consultarUsuarioServentiaParte(String id_ProcessoParte) throws Exception {
		String id_UsuarioServentia = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "SELECT p.ID_USU_SERV FROM PROJUDI.VIEW_PROC_PARTE p WHERE p.ID_PROC_PARTE = ?";
		ps.adicionarLong(id_ProcessoParte);
		try{
			rs1 = this.consultar(sql, ps);

			//Se possui proximo registro
			if (rs1.next()) id_UsuarioServentia = rs1.getString("ID_USU_SERV");

			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 

		return id_UsuarioServentia;
	}

	/**
	 * Método que habilita ou desabilita um Usuario de uma parte no processo
	 * @param processoParteDt dt da parte
	 * 
	 * @author msapaula
	 */
	public void vinculaUsuarioParte(ProcessoParteDt processoParteDt) throws Exception {
		String Sql;
		String id_UsuarioServentia;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		//Se for pra desabilitar seta null
		if (processoParteDt.getId_UsuarioServentia().equals("")) id_UsuarioServentia = null;
		else id_UsuarioServentia = processoParteDt.getId_UsuarioServentia();

		Sql = "UPDATE PROJUDI.PROC_PARTE SET ID_USU_SERV = ?";
		ps.adicionarLong(id_UsuarioServentia);
		Sql += " WHERE ID_PROC_PARTE = ?";
		ps.adicionarLong(processoParteDt.getId_ProcessoParte());

			executarUpdateDelete(Sql, ps);
	}

	/**
	 * Método que marca a citação para um parte de processo, para que da próxima vez essa seja intimada.
	 * 
	 * @param id_ProcessoParte, identificação da parte
	 * 
	 * @author msapaula
	 */
	public void marcarCitacaoParteProcesso(String id_ProcessoParte) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.PROC_PARTE SET CITADA = ? ";
		ps.adicionarLong(1);
		Sql += " WHERE ID_PROC_PARTE = ?";
		ps.adicionarLong(id_ProcessoParte);

			executarUpdateDelete(Sql, ps);
	}

	/**
	 * Verifica se determinado usuário é parte ativa em um processo
	 * @param id_UsuarioServentia, identificação do usuário
	 * @param id_Processo, identificação do processo
	 * @author msapaula
	 */
	public boolean isParteProcesso(String id_UsuarioServentia, String id_Processo) throws Exception {
		boolean boRetorno = false;
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT pp.ID_PROC_PARTE FROM PROJUDI.PROC_PARTE pp";
		Sql += " WHERE pp.ID_USU_SERV = ?";
		ps.adicionarLong(id_UsuarioServentia);
		Sql += " AND pp.ID_PROC = ?";
		ps.adicionarLong(id_Processo);
		Sql += " AND pp.DATA_BAIXA IS NULL";

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) boRetorno = true;
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return boRetorno;
	}

	/**
	 * Consulta as partes de um processo, podendo trazer todas ativas ou não
	 * @param id_processo, identificação do processo
	 * @param ativas, define se devem ser retornadas somente as partes ativas ou não
	 * 
	 * @author msapaula
	 */
	public List consultarPartesProcesso(String id_Processo, boolean ativas) throws Exception {
		List partes = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "SELECT * FROM PROJUDI.VIEW_PROC_PARTE_COMPLETA WHERE ID_PROC = ?";										ps.adicionarLong(id_Processo);
		if (ativas) sql += " AND DATA_BAIXA IS NULL";
		sql += " ORDER BY NOME";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				ProcessoParteDt obTemp = new ProcessoParteDt();
				obTemp.setId_ProcessoParte(rs1.getString("ID_PROC_PARTE"));
				obTemp.setNome(rs1.getString("NOME"));
				obTemp.setProcessoParteTipo(rs1.getString("PROC_PARTE_TIPO"));
				obTemp.setProcessoParteTipoCodigo(rs1.getString("PROC_PARTE_TIPO_CODIGO"));
				obTemp.setRg(rs1.getString("RG"));
				obTemp.setOrgaoExpedidor(rs1.getString("SIGLA_ORGAO_EXPEDIDOR"), rs1.getString("ORGAO_EXPEDIDOR_UF"));
				obTemp.setCpf(rs1.getString("CPF"));
				obTemp.setCnpj(Funcoes.completaCNPJZeros(rs1.getString("CNPJ")));
				obTemp.setTelefone(rs1.getString("TELEFONE"));
				obTemp.setDataBaixa(Funcoes.FormatarData(rs1.getDateTime("DATA_BAIXA")));
				obTemp.setId_ProcessoParteAusencia(rs1.getString("ID_PROC_PARTE_AUS"));
				obTemp.setExcluido(rs1.getString("EXCLUIDO"));
				obTemp.setReuPreso(rs1.getString("reu_preso"));
				
				//Setando endereço
				EnderecoDt enderecoDt = new EnderecoDt();
				enderecoDt.setId(rs1.getString("ID_ENDERECO"));
				enderecoDt.setLogradouro(rs1.getString("LOGRADOURO"));
				enderecoDt.setNumero(rs1.getString("NUMERO"));
				enderecoDt.setComplemento(rs1.getString("COMPLEMENTO"));
				enderecoDt.setCep(rs1.getString("CEP"));
				enderecoDt.setId_Bairro(rs1.getString("ID_BAIRRO"));
				enderecoDt.setBairro(rs1.getString("BAIRRO"));
				enderecoDt.setCidade(rs1.getString("CIDADE"));
				enderecoDt.setUf(rs1.getString("ESTADO"));
				obTemp.setEnderecoParte(enderecoDt);

				partes.add(obTemp);
			}
		
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 

		return partes;
	}

    public List consultarParteSimplificar() throws Exception {
        List partes = new ArrayList();        

        String sql = "SELECT ID_PROC_PARTE, NOME FROM PROJUDI.PROC_PARTE WHERE NOME_SIMPLIFICADO IS NULL";
        ResultSetTJGO rs1 = null;
        try{
            rs1 = this.consultarSemParametros(sql);
            if (rs1.next()) {
                ProcessoParteDt obTemp = new ProcessoParteDt();
                obTemp.setId_ProcessoParte(rs1.getString("ID_PROC_PARTE"));
                obTemp.setNome(rs1.getString("NOME"));
                partes.add(obTemp);
            }
        
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }     
        
        return partes;
    }
    
    /**
     * 
     * @param nome
     * @param processoParteTipoCodigo
     * @return
     * @throws Exception
     */
    public String consultarParteNome(String nome, String codigoComarca, String posicao) throws Exception {
		String stTemp = "";
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		
		String sql = "SELECT * FROM PROJUDI.VIEW_DADOS_COMP_DIST";
		sql += " WHERE NOME_SIMPLIFICADO = ? ";
		ps.adicionarString(nome);
		sql += "AND COD_COMARCA = ?";
		ps.adicionarLong(codigoComarca);

		try{
			rs1 = this.consultarPaginacao(sql, ps, posicao);
			while (rs1.next()) {
				stTemp += rs1.getString("NUMR_PROC")+";";
				stTemp += rs1.getString("DIGITO_VERIFICADOR")+";";
				stTemp += rs1.getString("ANO")+";";
				stTemp += rs1.getString("FIXO")+";";
				stTemp += rs1.getString("UNIDADE_ORIGEM")+";";
				stTemp += rs1.getString("SERV")+";";
				stTemp += rs1.getString("NATUREZA")+";";
				stTemp += rs1.getString("DATA_DISTRIBUICAO")+";";
				stTemp += rs1.getString("TIPO_DISTRIBUICAO")+";";
				stTemp += rs1.getString("POSICAO_JUIZ")+";";
				stTemp += rs1.getString("VALOR_ACAO")+";";
				stTemp += rs1.getString("VALOR_CUSTAS")+";";
				stTemp += rs1.getString("DATA_BAIXA_PROC")+";";
				stTemp += rs1.getString("OAB")+";";
				stTemp += rs1.getString("NOME_ADVOGADO")+";";
				stTemp += rs1.getString("TIPO_PARTE")+";";
				stTemp += rs1.getString("TIPO_PESSOA")+";";
				stTemp += rs1.getString("NOME")+";";
				stTemp += rs1.getString("CPF_CGC")+";";
				stTemp += rs1.getString("SEXO")+";";
				stTemp += rs1.getString("IDENTIDADE")+";";
				stTemp += rs1.getString("ORGAO_EXPEDIDOR")+";";
				stTemp += rs1.getString("DATA_BAIXA_PARTE")+";";
				stTemp += rs1.getString("COD_COMARCA")+";";
				stTemp += rs1.getString("NOME_SIMPLIFICADO")+";";				
				stTemp += "\n";
			}
			sql = "SELECT COUNT(*) AS QUANTIDADE FROM PROJUDI.VIEW_DADOS_COMP_DIST WHERE NOME_SIMPLIFICADO = ? AND COD_COMARCA = ?";
			rs2 = consultar(sql, ps);
			if (rs2.next()) {
			    stTemp += Funcoes.completarZeros(rs2.getString("QUANTIDADE"), 5);
			}
		
		}
		finally{
			try{if( rs1 != null ) rs1.close(); }catch(Exception e){}
		}

		return stTemp;
	}
    
    public String consultarParteCpf(String cpf, String codigoComarca, String posicao) throws Exception {
		String stTemp = "";
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		
		String sql = "SELECT * FROM PROJUDI.VIEW_DADOS_COMP_DIST";
		sql += " WHERE CPF_CGC = ? ";
		ps.adicionarString(cpf);
		sql += "AND COD_COMARCA = ?";
		ps.adicionarLong(codigoComarca);

		try{
			rs1 = this.consultarPaginacao(sql, ps, posicao);
			while (rs1.next()) {
				stTemp += rs1.getString("NUMR_PROC")+";";
				stTemp += rs1.getString("DIGITO_VERIFICADOR")+";";
				stTemp += rs1.getString("ANO")+";";
				stTemp += rs1.getString("FIXO")+";";
				stTemp += rs1.getString("UNIDADE_ORIGEM")+";";
				stTemp += rs1.getString("SERV")+";";
				stTemp += rs1.getString("NATUREZA")+";";
				stTemp += rs1.getString("DATA_DISTRIBUICAO")+";";
				stTemp += rs1.getString("TIPO_DISTRIBUICAO")+";";
				stTemp += rs1.getString("POSICAO_JUIZ")+";";
				stTemp += rs1.getString("VALOR_ACAO")+";";
				stTemp += rs1.getString("VALOR_CUSTAS")+";";
				stTemp += rs1.getString("DATA_BAIXA_PROC")+";";
				stTemp += rs1.getString("OAB")+";";
				stTemp += rs1.getString("NOME_ADVOGADO")+";";
				stTemp += rs1.getString("TIPO_PARTE")+";";
				stTemp += rs1.getString("TIPO_PESSOA")+";";
				stTemp += rs1.getString("NOME")+";";
				stTemp += rs1.getString("CPF_CGC")+";";
				stTemp += rs1.getString("SEXO")+";";
				stTemp += rs1.getString("IDENTIDADE")+";";
				stTemp += rs1.getString("ORGAO_EXPEDIDOR")+";";
				stTemp += rs1.getString("DATA_BAIXA_PARTE")+";";
				stTemp += rs1.getString("COD_COMARCA")+";";
				stTemp += rs1.getString("NOME_SIMPLIFICADO")+";";				
				stTemp += "\n";
			}
			sql = "SELECT COUNT(*) AS QUANTIDADE FROM PROJUDI.VIEW_DADOS_COMP_DIST WHERE CPF_CGC = ? AND COD_COMARCA = ?";
			rs2 = consultar(sql, ps);
			if (rs2.next()) {
			    stTemp += Funcoes.completarZeros(rs2.getString("QUANTIDADE"), 5);
			}
		}
		finally{
			try{if( rs1 != null ) rs1.close(); }catch(Exception e){}
		}

		return stTemp;
	}

    /**
     * Consulta a quantidade de partes com o mesmo id de endereço
     * 
     * @param id_EnderecoParte
     * @return
     * @throws Exception
     */
	public int consultarQuantidadePartesUtilizamEsseEndereco(String id_EnderecoParte) throws Exception {
		int retorno = 0;
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		try{
			stSql = "SELECT COUNT(1) AS QUANTIDADE";			
			stSql += " FROM PROJUDI.PROC_PARTE ";
			stSql += "  WHERE ID_ENDERECO = ?";				ps.adicionarLong(id_EnderecoParte);			
			rs1 = consultar(stSql, ps);
			if (rs1.next()) retorno = rs1.getInt("QUANTIDADE");				
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return retorno;
	}

	public String consultarIdBairro(String id_ProcessoParte) throws Exception {
		String Sql;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT * FROM PROJUDI.VIEW_PROC_PARTE_COMPLETA WHERE ID_PROC_PARTE = ?";
		ps.adicionarLong(id_ProcessoParte);

		try {
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				stTemp = rs1.getString("ID_BAIRRO");
			}
		} finally{
             try {if (rs1 != null) rs1.close();} catch (Exception e1) {}
        } 
		return stTemp;
	}
	
	public List consultarPartes( int id_proc_parte) throws Exception {
		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
	
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT id_proc_parte, nome FROM PROJUDI.PROC_PARTE";		
		//Sql += " WHERE id_proc_parte > " + posicao * 1000 + " AND id_proc_parte <= " + ((posicao * 1000) + 1000);
		Sql += " WHERE id_proc_parte > " + id_proc_parte  + " AND id_proc_parte <= " + (id_proc_parte  + 1000);
		
		try{
			rs1 = consultar(Sql, ps);

			while (rs1.next()) {
				ProcessoParteDt obTemp = new ProcessoParteDt();
				
				obTemp.setId(rs1.getString("id_proc_parte"));
				obTemp.setNome(rs1.getString("nome"));
				
				liTemp.add(obTemp);
			}
									
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
   
        }
		return liTemp;
	}
	
	public void salvarNomeSimmplificado(ProcessoParteDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psProcessoPartealterar()");

		stSql= "UPDATE PROJUDI.PROC_PARTE SET  ";
				
		stSql += " NOME_SIMPLIFICADO = ?";			ps.adicionarString(Funcoes.converteNomeSimplificado(dados.getNome()));
		
		stSql += " WHERE ID_PROC_PARTE  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 
	
	// lrcampos - 11/10/2019 14:23 - Consultar classe dos processos
	public String consultaClasseProcesso(String idAudiProc,  Integer procTipoRecurso) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder();
		ResultSetTJGO rs = null;
		String retorno = "";

		Integer SEM_RECURSO = 1;
	    Integer RECURSO = 2;
	    Integer RECURSO_SECUNDARIO = 3;
		
		sql.append(" SELECT ");
		sql.append(" CASE WHEN RECURSO_SECUNDARIO IS NOT NULL AND RECURSO IS NOT NULL THEN RECURSO_SECUNDARIO || ' - ' || RECURSO ");
		sql.append(" 	  WHEN RECURSO_SECUNDARIO IS NOT NULL AND RECURSO IS NULL THEN RECURSO_SECUNDARIO || ' - ' || PRINCIPAL ");
		sql.append("      WHEN RECURSO IS NOT NULL THEN RECURSO ");
		sql.append(" 	  ELSE PRINCIPAL END TIPO_PROC   ");
		sql.append(" FROM ( ");
		sql.append(" 	SELECT DISTINCT PT_Prin.PROC_TIPO PRINCIPAL, Pt_Rec.PROC_TIPO RECURSO,	Pt_Rec_Sec.PROC_TIPO RECURSO_SECUNDARIO ");
		sql.append(" 		FROM AUDI_PROC AP ");
		sql.append(" 		INNER JOIN PROC P ON P.ID_PROC = AP.ID_PROC ");
		sql.append(" 		LEFT JOIN RECURSO R ON	R.ID_PROC = AP.ID_PROC AND r.DATA_RETORNO IS NULL ");
		sql.append(" 		LEFT JOIN RECURSO_SECUNDARIO_PARTE RSP ON RSP.ID_AUDI_PROC = AP.ID_AUDI_PROC ");
		sql.append(" 		LEFT JOIN PROC_TIPO PT_Prin ON PT_Prin.ID_PROC_TIPO = AP.ID_PROC_TIPO ");
		sql.append(" 		LEFT JOIN PROC_TIPO PT_Rec ON Pt_Rec.ID_PROC_TIPO = AP.ID_PROC_TIPO ");
		sql.append(" 		LEFT JOIN PROC_TIPO PT_Rec_Sec ON	Pt_Rec_Sec.ID_PROC_TIPO = rsp.ID_PROC_TIPO_RECURSO_SEC");
		sql.append(" 		WHERE AP.ID_AUDI_PROC = ? "); ps.adicionarString(idAudiProc);			
		sql.append(" 		AND (CASE WHEN RSP.ID_RECURSO_SECUNDARIO_PARTE IS NOT NULL THEN  ").append(RECURSO_SECUNDARIO); 
		sql.append("			  WHEN R.ID_RECURSO IS NOT NULL THEN " ).append(RECURSO);
		sql.append(" 					ELSE ").append(SEM_RECURSO);
		sql.append(" END) = ? )"); ps.adicionarString(String.valueOf(procTipoRecurso));


		try {
			rs = this.consultar(sql.toString(), ps);

			if (rs.next()) {
				retorno = rs.getString("TIPO_PROC");
			}
		} finally {
			if (rs != null)
				rs.close();
		}
		return retorno;
	}
	
	// lrcampos - 11/10/2019 14:23 - Consultar tipo de recurso do processo
	public Integer consultaTipoRecursoProcessoIdAudiProc(String idAudiProc) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder();
		ResultSetTJGO rs = null;
		Integer recurso = null;

		sql.append(" SELECT DISTINCT CASE WHEN RECURSO_SECUNDARIO IS NOT NULL THEN 3 ");
		sql.append(" WHEN RECURSO IS NOT NULL THEN 2 ");
		sql.append(" ELSE 1 END TIPO_RECURSO  FROM");
		sql.append(
				" (SELECT DISTINCT AP.ID_AUDI_PROC PRINCIPAL, R.ID_RECURSO RECURSO, rsp.ID_RECURSO_SECUNDARIO_PARTE RECURSO_SECUNDARIO FROM AUDI_PROC AP ");
		sql.append(" INNER JOIN PROC_PARTE PP ON PP.ID_PROC = AP.ID_PROC ");
		sql.append(" LEFT JOIN Projudi.RECURSO_SECUNDARIO_PARTE rsp ON");
		sql.append(" rsp.ID_AUDI_PROC = AP.ID_AUDI_PROC");
		sql.append(" LEFT JOIN Projudi.VIEW_RECURSO_COMPLETO vrc ON ");
		sql.append("  (vrc.ID_PROC = AP.ID_PROC AND VRC.DATA_RETORNO IS NULL AND ap.ID_PROC_TIPO = vrc.ID_PROC_TIPO) ");
		sql.append(" LEFT JOIN RECURSO R ON R.ID_PROC = AP.ID_PROC ");
		sql.append(" WHERE AP.ID_AUDI_PROC = ? )"); ps.adicionarString(idAudiProc);
	
		try {
			rs = this.consultar(sql.toString(), ps);

			if (rs.next()) {
				recurso = rs.getInt("TIPO_RECURSO");

			}
		} finally {
			if (rs != null)
				rs.close();
		}

		return recurso;
	}
	
	
	
	// mrbatista - 15/06/2020 14:49 - Consultar processoTipo da Audiencia Processo
	public Integer consultaCodigoProcessoTipoPeloIdAudiProc(String idAudiProc,  Integer procTipoRecurso) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder();
		ResultSetTJGO rs = null;
		Integer retorno = 0;

		Integer SEM_RECURSO = 1;
	    Integer RECURSO = 2;
	    Integer RECURSO_SECUNDARIO = 3;
		
		sql.append(" SELECT ");
		sql.append(" CASE WHEN RECURSO_SECUNDARIO IS NOT NULL AND RECURSO IS NOT NULL THEN RECURSO_SECUNDARIO ");
		sql.append(" 	  WHEN RECURSO_SECUNDARIO IS NOT NULL AND RECURSO IS NULL THEN RECURSO_SECUNDARIO ");
		sql.append("      WHEN RECURSO IS NOT NULL THEN RECURSO ");
		sql.append(" 	  ELSE PRINCIPAL END TIPO_PROC   ");
		sql.append(" FROM ( ");
		sql.append(" 	SELECT DISTINCT PT_Prin.PROC_TIPO_CODIGO PRINCIPAL, Pt_Rec.PROC_TIPO_CODIGO RECURSO,	Pt_Rec_Sec.PROC_TIPO_CODIGO RECURSO_SECUNDARIO ");
		sql.append(" 		FROM AUDI_PROC AP ");
		sql.append(" 		INNER JOIN PROC P ON P.ID_PROC = AP.ID_PROC ");
		sql.append(" 		LEFT JOIN RECURSO R ON	R.ID_PROC = AP.ID_PROC AND r.DATA_RETORNO IS NULL ");
		sql.append(" 		LEFT JOIN RECURSO_SECUNDARIO_PARTE RSP ON RSP.ID_AUDI_PROC = AP.ID_AUDI_PROC ");
		sql.append(" 		LEFT JOIN PROC_TIPO PT_Prin ON PT_Prin.ID_PROC_TIPO = AP.ID_PROC_TIPO ");
		sql.append(" 		LEFT JOIN PROC_TIPO PT_Rec ON Pt_Rec.ID_PROC_TIPO = AP.ID_PROC_TIPO ");
		sql.append(" 		LEFT JOIN PROC_TIPO PT_Rec_Sec ON	Pt_Rec_Sec.ID_PROC_TIPO = rsp.ID_PROC_TIPO_RECURSO_SEC");
		sql.append(" 		WHERE AP.ID_AUDI_PROC = ? "); ps.adicionarString(idAudiProc);			
		sql.append(" 		AND (CASE WHEN RSP.ID_RECURSO_SECUNDARIO_PARTE IS NOT NULL THEN  ").append(RECURSO_SECUNDARIO); 
		sql.append("			  WHEN R.ID_RECURSO IS NOT NULL THEN " ).append(RECURSO);
		sql.append(" 					ELSE ").append(SEM_RECURSO);
		sql.append(" END) = ? )"); ps.adicionarString(String.valueOf(procTipoRecurso));


		try {
			rs = this.consultar(sql.toString(), ps);

			if (rs.next()) {
				retorno = rs.getInt("TIPO_PROC");
			}
		} finally {
			if (rs != null)
				rs.close();
		}
		return retorno;
	}

}
