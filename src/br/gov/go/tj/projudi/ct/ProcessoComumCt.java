package br.gov.go.tj.projudi.ct;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import br.gov.go.tj.projudi.dt.AreaDistribuicaoDt;
import br.gov.go.tj.projudi.dt.AssuntoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.dt.ProcessoAssuntoDt;
import br.gov.go.tj.projudi.dt.ProcessoCadastroDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoParteTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaOabDt;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.ProcessoParteTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

/**
 * Servlet que contém os métodos em comum 
 * dos processos
 * @author tamaralsantos
 */
public abstract class ProcessoComumCt extends Controle {

	private static final long serialVersionUID = 6757477987269926374L;
	
	/**
	 * Método responsável em adicionar assuntos a um processo
	 */
	public void adicionarAssuntoProcesso(ProcessoCadastroDt processoCadastroDt, HttpServletRequest request) {
		if (processoCadastroDt.getId_Assunto().length() > 0) {
			ProcessoAssuntoDt processoAssuntoDt = new ProcessoAssuntoDt();
			processoAssuntoDt.setId_Assunto(processoCadastroDt.getId_Assunto());
			processoAssuntoDt.setAssunto(processoCadastroDt.getAssunto());
			processoCadastroDt.addListaAssuntos(processoAssuntoDt);

			processoCadastroDt.setId_Assunto("null");
			processoCadastroDt.setAssunto("null");
		} else request.setAttribute("MensagemErro", "Selecione um Assunto para ser adicionado.");
	}


	/**
	 * Realiza a retirada de uma parte da lista de partes do processo
	 * @throws MensagemException 
	 */
	public String excluirParteProcesso(ProcessoCadastroDt processoCadastroDt, int posicao, int parteTipo) throws MensagemException {
		ProcessoParteDt processoParteDt = null;
		String msg = "";
		switch (parteTipo) {
			case ProcessoParteTipoDt.POLO_ATIVO_CODIGO:
				processoParteDt = (ProcessoParteDt) processoCadastroDt.getPoloAtivo(posicao);
				if (processoParteDt.getCodigoTemp() != null && processoParteDt.getCodigoTemp().equals("GuiaInicial")){
					msg = "Não é possível remover a parte Informada na guia inicial!";
				} else {
					processoCadastroDt.getListaPolosAtivos().remove(processoParteDt);
					if (processoParteDt.getAdvogadoDt() != null && processoParteDt.getAdvogadoDt().getId_UsuarioServentiaAdvogado().length() > 0) {
						processoCadastroDt.getListaAdvogados().remove(processoParteDt.getAdvogadoDt());
					}
				}
				break;

			case ProcessoParteTipoDt.POLO_PASSIVO_CODIGO:
				processoParteDt = (ProcessoParteDt) processoCadastroDt.getPoloPassivo(posicao);
				if (processoParteDt.getCodigoTemp() != null && processoParteDt.getCodigoTemp().equals("GuiaInicial")){
					msg = "Não é possível remover a parte Informada na guia inicial!";
				} else {
					processoCadastroDt.getListaPolosPassivos().remove(processoParteDt);
					if (processoParteDt.getAdvogadoDt() != null && processoParteDt.getAdvogadoDt().getId_UsuarioServentiaAdvogado().length() > 0) {
						processoCadastroDt.getListaAdvogados().remove(processoParteDt.getAdvogadoDt());
					}
				}
				break;

			default:
				processoParteDt = (ProcessoParteDt) processoCadastroDt.getOutraParte(posicao);
				processoCadastroDt.getListaOutrasPartes().remove(processoParteDt);
				break;
		}
		
		return msg;
	}

	/**
	 * Quando o cadastro está sendo feito por um advogado já adiciona esse à lista de advogados
	 */
	public void adicionaAdvogadoProcesso(ProcessoCadastroDt processoDt, UsuarioNe usuarioSessao) {
		UsuarioServentiaOabDt advogado = new UsuarioServentiaOabDt();
		advogado.setId_UsuarioServentia(usuarioSessao.getUsuarioDt().getId_UsuarioServentia());
		advogado.setNomeUsuario(usuarioSessao.getUsuarioDt().getNome());
		advogado.setOabNumero(usuarioSessao.getUsuarioDt().getOabNumero() + "-" + usuarioSessao.getUsuarioDt().getOabComplemento() + " " + usuarioSessao.getUsuarioDt().getEstadoRepresentacao());
		

		if (Funcoes.StringToInt(usuarioSessao.getUsuarioDt().getGrupoCodigo()) == GrupoDt.MINISTERIO_PUBLICO){
			advogado.setPromotor(true);
		}
		
		processoDt.addListaAdvogados(advogado);
	}

	/**
	 * Resgata lista de arquivos inseridos no passo 2 usando DWR
	 * Converte de Map para List
	 */
	public List getListaArquivos(HttpServletRequest request) {
		Map mapArquivos = (Map) request.getSession().getAttribute("ListaArquivos");
		return Funcoes.converterMapParaList(mapArquivos);
	}

	/**
	 * Busca uma parte de acordo com os parâmetros passados
	 * @throws Exception 
	 */
	public void buscaParte(HttpServletRequest request, ProcessoParteDt processoPartedt, int parteTipo, int passoEditar, ProcessoNe processoNe) throws Exception{
		String cpfCnpj = "", Rg = "", Ctps = "", TituloEleitor = "", Pis = "";

		if (request.getParameter("CpfCnpj") != null) cpfCnpj = request.getParameter("CpfCnpj").replaceAll("[/.-]", "");
		if (request.getParameter("Rg") != null) Rg = request.getParameter("Rg");
		if (request.getParameter("Ctps") != null) Ctps = request.getParameter("Ctps");
		if (request.getParameter("TituloEleitor") != null) TituloEleitor = request.getParameter("TituloEleitor");
		if (request.getParameter("Pis") != null) Pis = request.getParameter("Pis");

		processoPartedt = new ProcessoParteDt();
		if ((cpfCnpj.length() > 0) || (Rg.length() > 0) || (Ctps.length() > 0) || (TituloEleitor.length() > 0) || (Pis.length() > 0)) {

			processoPartedt = processoNe.consultarParte(cpfCnpj, Rg, Ctps, TituloEleitor, Pis);

			if (processoPartedt.getId_ProcessoParte().length() == 0) {
				if (cpfCnpj.length() == 11) processoPartedt.setCpf(cpfCnpj);
				if (cpfCnpj.length() == 14) processoPartedt.setCnpj(cpfCnpj);
				processoPartedt.setRg(Rg);
				processoPartedt.setCtps(Ctps);
				processoPartedt.setTituloEleitor(TituloEleitor);
				processoPartedt.setPis(Pis);
				processoPartedt.setParteIsenta(processoNe.isParteIsenta(processoPartedt.getCnpj()));

			}
		}
		processoPartedt.setProcessoParteTipoCodigo(String.valueOf(parteTipo));
		if (parteTipo > -1){
			// consultar a descrição do tipo da parte
			ProcessoParteTipoDt processoParteTipoDt = new ProcessoParteTipoNe().consultarProcessoParteTipoCodigo(String.valueOf(parteTipo));
			processoPartedt.setProcessoParteTipo(processoParteTipoDt.getProcessoParteTipo());
		}
		if (passoEditar == 9) processoPartedt.setParteNaoPersonificavel(true);
		request.getSession().setAttribute("Enderecodt", processoPartedt.getEnderecoParte());
		request.getSession().setAttribute("ProcessoPartedt", processoPartedt);
	}

	/**
	 * Método que verifica se determinada parte já foi inserida em um processo.
	 * Se está sendo inserido um promovente, verifica se já estava cadastrada como promovente.
	 * Se está sendo inserido um promovido, verifica se já estava cadastrado como promovido.
	 * Pois há casos em que a mesma parte pode atuar como promovente e promovida no processo.
	 */
	public boolean parteProcessoCadastrada(ProcessoDt processoDt, ProcessoParteDt processoPartedt) {
		List<ProcessoParteDt> listaPromoventes 	= processoDt.getListaPolosAtivos();
		List<ProcessoParteDt> listaPromovidos 	= processoDt.getListaPolosPassivos();

		
		if (processoPartedt.getProcessoParteTipoCodigo().length() > 0) {
			//Se está sendo inserido um promovente, verifica se essa parte já tinha sido cadastrada como promovente
			if (Funcoes.StringToInt(processoPartedt.getProcessoParteTipoCodigo()) == ProcessoParteTipoDt.POLO_ATIVO_CODIGO) {

				if (listaPromoventes != null) {
					for (int i = 0; i < listaPromoventes.size(); i++) {
						ProcessoParteDt dt = (ProcessoParteDt) listaPromoventes.get(i);
						if (((dt.getCpf().length() > 0) && (dt.getCpf().equals(processoPartedt.getCpf()))) || ((dt.getCnpj().length() > 0) && (dt.getCnpj().equals(processoPartedt.getCnpj())))) {return true; }
					}
				}
			} else if (Funcoes.StringToInt(processoPartedt.getProcessoParteTipoCodigo()) == ProcessoParteTipoDt.POLO_PASSIVO_CODIGO) {
				//Se está sendo inserido um promovido, verifica se essa parte já tinha sido cadastrada como promovida
				if (listaPromovidos != null) {
					for (int i = 0; i < listaPromovidos.size(); i++) {
						ProcessoParteDt dt = (ProcessoParteDt) listaPromovidos.get(i);
						if (((dt.getCpf().length() > 0) && (dt.getCpf().equals(processoPartedt.getCpf()))) || ((dt.getCnpj().length() > 0) && (dt.getCnpj().equals(processoPartedt.getCnpj())))) {return true; }
					}
				}
			}
		}

		return false;
	}

	/**
	 * Resgata atributos da Parte e seu Endereço
	 */
	public void setDadosProcessoParte(HttpServletRequest request, ProcessoParteDt processoParteDt, UsuarioNe usuarioSessao) {

		processoParteDt.setNome(Funcoes.capitularNome(request.getParameter("Nome")));
		processoParteDt.setCpf(request.getParameter("Cpf"));
		processoParteDt.setCnpj(request.getParameter("Cnpj"));
		processoParteDt.setRg(request.getParameter("Rg"));
		processoParteDt.setId_ProcessoParteTipo(request.getParameter("Id_ProcessoParteTipo"));
		processoParteDt.setProcessoParteTipo(request.getParameter("ProcessoParteTipo"));
		processoParteDt.setId_RgOrgaoExpedidor(request.getParameter("Id_RgOrgaoExpedidor"));
		processoParteDt.setRgOrgaoExpedidor(request.getParameter("RgOrgaoExpedidor"));
		processoParteDt.setRgDataExpedicao(request.getParameter("RgDataExpedicao"));
		processoParteDt.setTituloEleitor(request.getParameter("TituloEleitor"));
		processoParteDt.setTituloEleitorZona(request.getParameter("TituloEleitorZona"));
		processoParteDt.setTituloEleitorSecao(request.getParameter("TituloEleitorSecao"));
		processoParteDt.setCtps(request.getParameter("Ctps"));
		processoParteDt.setCtpsSerie(request.getParameter("CtpsSerie"));
		processoParteDt.setId_CtpsUf(request.getParameter("Id_CtpsUf"));
		processoParteDt.setEstadoCtpsUf(request.getParameter("EstadoCtpsUf"));
		processoParteDt.setPis(request.getParameter("Pis"));
		processoParteDt.setSexo(request.getParameter("Sexo"));
		processoParteDt.setNomeMae(Funcoes.capitularNome(request.getParameter("NomeMae")));
		processoParteDt.setNomePai(Funcoes.capitularNome(request.getParameter("NomePai")));	
		processoParteDt.setDataNascimento(request.getParameter("DataNascimento"));
		processoParteDt.setId_Naturalidade(request.getParameter("Id_Cidade"));
		processoParteDt.setCidadeNaturalidade(request.getParameter("Cidade"));
		processoParteDt.setId_EstadoCivil(request.getParameter("Id_EstadoCivil"));
		processoParteDt.setEstadoCivil(request.getParameter("EstadoCivil"));
		processoParteDt.setId_Escolaridade(request.getParameter("Id_Escolaridade"));
		processoParteDt.setEscolaridade(request.getParameter("Escolaridade"));
		processoParteDt.setId_Profissao(request.getParameter("Id_Profissao"));
		processoParteDt.setProfissao(request.getParameter("Profissao"));
		processoParteDt.setTelefone(request.getParameter("Telefone"));
		processoParteDt.setCelular(request.getParameter("Celular"));
		processoParteDt.setEMail(request.getParameter("EMail"));
		processoParteDt.setId_GovernoTipo(request.getParameter("Id_GovernoTipo"));
		processoParteDt.setGovernoTipo(request.getParameter("GovernoTipo"));	
		processoParteDt.setId_EmpresaTipo(request.getParameter("Id_EmpresaTipo"));
		processoParteDt.setEmpresaTipo(request.getParameter("EmpresaTipo"));
		processoParteDt.setId_UsuarioLog(usuarioSessao.getId_Usuario());
		processoParteDt.setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());

		// Endereço da parte
		processoParteDt.getEnderecoParte().setId(request.getParameter("Id_Endereco"));
		processoParteDt.getEnderecoParte().setLogradouro(request.getParameter("Logradouro"));
		processoParteDt.getEnderecoParte().setNumero(request.getParameter("Numero"));
		processoParteDt.getEnderecoParte().setComplemento(request.getParameter("Complemento"));
		processoParteDt.getEnderecoParte().setId_Bairro(request.getParameter("Id_Bairro"));
		processoParteDt.getEnderecoParte().setBairro(request.getParameter("Bairro"));
		processoParteDt.getEnderecoParte().setId_Cidade(request.getParameter("BairroId_Cidade"));
		processoParteDt.getEnderecoParte().setCidade(request.getParameter("BairroCidade"));
		processoParteDt.getEnderecoParte().setUf(request.getParameter("BairroUf"));
		processoParteDt.getEnderecoParte().setCep(request.getParameter("Cep"));
		
		//leandro*********************************************************************************************************************************
		if (request.getParameter("quantidadeLocomocao") != null && request.getParameter("quantidadeLocomocao").length()>0)
			processoParteDt.getLocomocaoDt().setQtdLocomocao(Integer.valueOf(request.getParameter("quantidadeLocomocao")));
		
		if (request.getParameter("finalidade") != null && request.getParameter("finalidade").toString().length() > 0) {
			processoParteDt.getLocomocaoDt().setFinalidadeCodigo(Integer.valueOf(request.getParameter("finalidade").toString()));
		}
		
		if (request.getParameter("penhora") != null && request.getParameter("penhora").toString().length() > 0) {
			processoParteDt.getLocomocaoDt().setPenhora(Boolean.valueOf(request.getParameter("penhora").toString()));
		}
		
		if (request.getParameter("intimacao") != null && request.getParameter("intimacao").toString().length() > 0) {
			processoParteDt.getLocomocaoDt().setIntimacao(Boolean.valueOf(request.getParameter("intimacao").toString()));
		}
		
		if (request.getParameter("oficialCompanheiro") != null && request.getParameter("oficialCompanheiro").toString().length() > 0) {
			processoParteDt.getLocomocaoDt().setOficialCompanheiro(Boolean.valueOf(request.getParameter("oficialCompanheiro")));
		}
		//leandro*********************************************************************************************************************************
		
		processoParteDt.getEnderecoParte().setId_UsuarioLog(usuarioSessao.getId_Usuario());
		processoParteDt.getEnderecoParte().setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.getSession().setAttribute("Enderecodt", processoParteDt.getEnderecoParte());
		request.getSession().setAttribute("ProcessoPartedt", processoParteDt);
	}
	

	/**
	 * Resgata atributos da Parte Isenta
	 */
	public void setDadosProcessoParteIsenta(HttpServletRequest request, ProcessoParteDt processoParteDt, UsuarioNe usuarioSessao) {

		processoParteDt.setNome(Funcoes.capitularNome(request.getParameter("Nome")));
		processoParteDt.setCnpj(request.getParameter("Cnpj"));
		processoParteDt.setId_UsuarioLog(usuarioSessao.getId_Usuario());
		processoParteDt.setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());

		
		// Endereço da parte
		processoParteDt.getEnderecoParte().setLogradouro(request.getParameter("Logradouro"));
		processoParteDt.getEnderecoParte().setNumero(request.getParameter("Numero"));
		processoParteDt.getEnderecoParte().setComplemento(request.getParameter("Complemento"));
		processoParteDt.getEnderecoParte().setId_Bairro(request.getParameter("Id_Bairro"));
		processoParteDt.getEnderecoParte().setBairro(request.getParameter("Bairro"));
		processoParteDt.getEnderecoParte().setId_Cidade(request.getParameter("BairroId_Cidade"));
		processoParteDt.getEnderecoParte().setCidade(request.getParameter("BairroCidade"));
		processoParteDt.getEnderecoParte().setUf(request.getParameter("BairroUf"));
		processoParteDt.getEnderecoParte().setCep(request.getParameter("Cep"));
		
		processoParteDt.getEnderecoParte().setId_UsuarioLog(usuarioSessao.getId_Usuario());
		processoParteDt.getEnderecoParte().setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.getSession().setAttribute("ProcessoPartedt", processoParteDt);
	}
	
	public void getPartesIntimadasDelegacia(HttpServletRequest request, ProcessoCadastroDt processoCadastroDt) {
		//Captura partes selecionadas pelo usuario
		String[] partesIntimadas = request.getParameterValues("partesIntimadas");
		if (partesIntimadas != null) {
			List partesProcesso = processoCadastroDt.getPartesAtivasPassivas();
			for (int i = 0; i < partesProcesso.size(); i++) {
				ProcessoParteDt parte = (ProcessoParteDt) partesProcesso.get(i);
				for (int j = 0; j < partesIntimadas.length; j++) {
					if (partesIntimadas[j].equals(parte.getNome())) processoCadastroDt.addListaPartesIntimadas(parte);
				}
			}
		}
	}
	
	/**
	 * Método que faz tratamentos necessários com parâmetros auxiliares no cadastro de processo
	 * @throws Exception 
	 */
	public void setParametrosAuxiliares(ProcessoCadastroDt processoCadastroDt, ProcessoParteDt processoParteDt, int passoEditar, int paginaatual, HttpServletRequest request, int paginaAnterior, ProcessoNe processoNe, UsuarioNe usuarioSessao) throws Exception{
		// Se passoEditar for 3 ou 4 refere-se a cadastro de parte, portanto resgata os dados desta somente nesse caso
		
		if (Funcoes.StringToInt(processoCadastroDt.getId_Custa_Tipo(),0)  == ProcessoDt.ISENTO && passoEditar == 3 && processoNe.isParteIsenta(request.getParameter("Cnpj"))){
			processoParteDt.setProcessoParteTipoCodigo(String.valueOf(ProcessoParteTipoDt.POLO_ATIVO_CODIGO));
			
			if (!this.verificaBairroPreenchido(processoParteDt, request)){
				processoParteDt.setParteNaoPersonificavel(true);
			}
			this.setDadosProcessoParteIsenta(request, processoParteDt, usuarioSessao);
		
		} else 	if (passoEditar == 2 || passoEditar == 3 || passoEditar == 4){
			this.setDadosProcessoParte(request, processoParteDt, usuarioSessao);
		}
		
		//Quando modelo foi selecionado monta conteúdo para aparecer no editor e já carrego o tipo do arquivo
		if (!processoCadastroDt.getId_Modelo().equals("") && paginaAnterior == (ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)) {
			ModeloDt modeloDt = processoNe.consultarModeloId(processoCadastroDt.getId_Modelo(), processoCadastroDt, usuarioSessao.getUsuarioDt());
			processoCadastroDt.setId_ArquivoTipo(modeloDt.getId_ArquivoTipo());
			processoCadastroDt.setArquivoTipo(modeloDt.getArquivoTipo());
			processoCadastroDt.setTextoEditor(modeloDt.getTexto());
		}

		//Quando um assunto é selecionado já insere na lista de assuntos
		if (!processoCadastroDt.getId_Assunto().equals("") && paginaAnterior == (AssuntoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)) {
			this.adicionarAssuntoProcesso(processoCadastroDt, request);
		}
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("Id_ArquivoTipo", processoCadastroDt.getId_ArquivoTipo());
		request.setAttribute("ArquivoTipo", processoCadastroDt.getArquivoTipo());
		request.setAttribute("Modelo", processoCadastroDt.getModelo());
		request.setAttribute("TextoEditor", processoCadastroDt.getTextoEditor());
		request.setAttribute("PaginaAtual", Configuracao.Editar);
	}
	
	
	/**
	 * Método que faz tratamentos necessários com parâmetros auxiliares no cadastro de processo
	 */
	public abstract String obtenhaAcaoDefault(ProcessoCadastroDt processoCadastroDt);

	/**
	 * Método responsável em remover um assunto de um processo
	 */
	public void removerAssuntoProcesso(ProcessoCadastroDt processoCriminalDt, String posicaoLista) {
		if (posicaoLista != null && posicaoLista.length() > 0) {
			processoCriminalDt.getListaAssuntos().remove(Funcoes.StringToInt(posicaoLista));
		}
	}
	
	/**
	 * Método que faz a limpeza do objeto ProcessoCiveldt
	 * @throws Exception 
	 */
	protected void limpaProcesso(UsuarioNe usuarioSessao, ProcessoCadastroDt processoCadastroDt, ProcessoNe processoNe) throws Exception{
		
		//Guarda se processo é dependente, para depois de limpar setar de novo
		boolean dependente = processoCadastroDt.isProcessoDependente();
		// Guarda se processo é fisico, para depois de limpar setar de novo
		boolean fisico = processoCadastroDt.isProcessoFisico();	
		// Guarda id Area distribuição - Processo Físico
		String id_AreaDistribuicao = processoCadastroDt.getId_AreaDistribuicao();
		// Guarda Area distribuição - Processo Físico
		String areaDistribuicao = processoCadastroDt.getAreaDistribuicao();		
		// Guarda Area distribuição Código - Processo Físico
		String areaDistribuicaoCodigo = processoCadastroDt.getAreaDistribuicaoCodigo();
		// Guarda Fórum Código - Processo Físico
		String forumCodigo = processoCadastroDt.getForumCodigo();
		
		//Limpa o objeto
		processoCadastroDt.limpar();	
		
		// Caso seja processo físico a serventia será obtida através da serventia selecionada pelo usuário 
		if (fisico){
			processoCadastroDt.setProcessoFisico(fisico);
			processoCadastroDt.setId_Serventia(usuarioSessao.getUsuarioDt().getId_Serventia());
			processoCadastroDt.setServentia(usuarioSessao.getUsuarioDt().getServentia());
			
			//Se as informações de Area de Distribuição e/ou Fórum ainda não
			// estiverem sido carregadas, as mesmas são obtidas da base...
			if (id_AreaDistribuicao == null || id_AreaDistribuicao == "" ||
				areaDistribuicao == null || areaDistribuicao == "" ||
				areaDistribuicaoCodigo == null || areaDistribuicaoCodigo == "" ||
				forumCodigo == null || forumCodigo == ""){
			
				ServentiaDt serventiaDt = processoNe.consultarServentia(usuarioSessao.getUsuarioDt().getId_Serventia());
				if (serventiaDt != null) {
					List tempList = processoNe.consultarAreasDistribuicaoServentia(serventiaDt.getId());
					if (tempList !=  null && tempList.size() > 0) {
						AreaDistribuicaoDt areaDistribuicaoDt = (AreaDistribuicaoDt) tempList.get(0);
						processoCadastroDt.setId_AreaDistribuicao(areaDistribuicaoDt.getId());
						processoCadastroDt.setAreaDistribuicao(areaDistribuicaoDt.getAreaDistribuicao());
						processoCadastroDt.setAreaDistribuicaoCodigo(areaDistribuicaoDt.getAreaDistribuicaoCodigo());
						processoCadastroDt.setForumCodigo(areaDistribuicaoDt.getForumCodigo());							
					}
				}
			}
			else{
				//Caso já tenha sido carregadas as mesmas são recuperadas...
				processoCadastroDt.setId_AreaDistribuicao(id_AreaDistribuicao);
				processoCadastroDt.setAreaDistribuicao(areaDistribuicao);
				processoCadastroDt.setAreaDistribuicaoCodigo(areaDistribuicaoCodigo);
				processoCadastroDt.setForumCodigo(forumCodigo);	
			}
		}
		else{
			processoCadastroDt.setProcessoDependente(dependente);
		}			
	}
	
	/**
	 * Captura partes selecionadas
	 * @param partes
	 * @param processoDt
	 * @return
	 */
	protected List getPartesRecorrentes(String[] partes, ProcessoCadastroDt processoDt) {
		ProcessoDt processo1Grau = processoDt.getProcessoDependenteDt();

		List partesSelecionadas = new ArrayList();
		//Para cada parte selecionada pelo usuário, busca os dados dessa no processo de 1º grau
		for (int i = 0; i < partes.length; i++) {
			List lista = processo1Grau.getPartesProcesso();
			for (int j = 0; j < lista.size(); j++) {
				ProcessoParteDt dt = (ProcessoParteDt) lista.get(j);
				if (dt.getId_ProcessoParte().equals(partes[i])) {
					dt.setId_UsuarioLog(processoDt.getId_UsuarioLog());
					dt.setIpComputadorLog(processoDt.getIpComputadorLog());
					//limpo o id da parte 
					//e o id do processo
					dt.setId("");
					dt.setId_Processo("");
					dt.setId_Endereco("");
					dt.setId_ProcessoParteTipo(String.valueOf(ProcessoParteTipoDt.ID_POLO_ATIVO));
					partesSelecionadas.add(dt);
					break;
				}
			}
		}
		return partesSelecionadas;
	}
	
	/**
	 * Captura as partes recorrentes selecionadas pelo usuário e já vincula o usuário advogado
	 */
	protected void getPartesRecorrentes(HttpServletRequest request, ProcessoCadastroDt processoDt, UsuarioNe UsuarioSessao) {
		String[] recorrentes = request.getParameterValues("Recorrente");
		if (recorrentes != null){
			processoDt.setListaPolosAtivos(getPartesRecorrentes(recorrentes, processoDt));
		}else{
			processoDt.setListaPolosAtivos(new ArrayList());
		}
	}
	
	/**
	 * Adiciona novas partes a lista de promoventes, sem limpar aquelas já existentes na lista
	 * @param request
	 * @param processoDt
	 */
	protected void addPartesRecorrentes(HttpServletRequest request, ProcessoCadastroDt processoDt, UsuarioNe UsuarioSessao){		
		String[] recorrentes = request.getParameterValues("Recorrente");
		if (recorrentes != null){
			List partesRecorrentes = getPartesRecorrentes(recorrentes, processoDt);
			for (int i = 0; i < partesRecorrentes.size(); i++){
				ProcessoParteDt processoParteDt = (ProcessoParteDt) partesRecorrentes.get(i);
				processoDt.addListaPoloAtivo(processoParteDt);
			}
		} else {
			processoDt.setListaPolosAtivos(new ArrayList());
		}
	}
	
	/**
	 * Captura os assuntos do processo originário e adiciona ao recurso
	 * @throws Exception 
	 */
	protected void getAssuntosProcessoOriginario(ProcessoCadastroDt processoDt, String id_Processo1Grau, ProcessoNe processoNe) throws Exception{

		List assuntosProcesso = processoNe.getAssuntosProcesso(id_Processo1Grau);

		for (int i = 0; i < assuntosProcesso.size(); i++) {
			ProcessoAssuntoDt processoAssuntoDt = (ProcessoAssuntoDt) assuntosProcesso.get(i);
			ProcessoAssuntoDt objAssunto = new ProcessoAssuntoDt();
			objAssunto.setId_Assunto(processoAssuntoDt.getId_Assunto());
			objAssunto.setAssunto(processoAssuntoDt.getAssunto());
			processoDt.addListaAssuntos(objAssunto);
		}
	}
	
	protected boolean verificaBairroPreenchido(ProcessoParteDt processoParteDt, HttpServletRequest request){
		if ( (request.getParameter("Id_Bairro") != null && request.getParameter("Id_Bairro").length()>0)
			 || (processoParteDt != null && processoParteDt.getEnderecoParte() != null && processoParteDt.getEnderecoParte().getId_Bairro() != null &&  processoParteDt.getEnderecoParte().getId_Bairro().length()>0)
		   ){
				 return true;
			 }
		return false;
	}
	
}