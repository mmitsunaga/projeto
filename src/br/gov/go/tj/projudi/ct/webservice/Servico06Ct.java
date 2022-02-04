package br.gov.go.tj.projudi.ct.webservice;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.ct.Controle;
import br.gov.go.tj.projudi.dt.AdvogadoDt;
import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.BuscaProcessoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaResponsavelDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoStatusDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaOabDt;
import br.gov.go.tj.projudi.ne.PendenciaNe;
import br.gov.go.tj.projudi.ne.PendenciaResponsavelNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.ServentiaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.GerenciaArquivo;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.WebServiceException;
import br.gov.go.tj.utils.Certificado.Base64Utils;

public class Servico06Ct extends Controle {

	private static final long serialVersionUID = 7847237158278441388L;

	public int Permissao() {
		return 608;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaoPaginaAtual) throws Exception {

		String stOperacao = "";
		String stTag = "";
		String stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
		List lisValores = null;
		List tempList = new ArrayList();
		
		response.setContentType("text/xml; charset=UTF-8");

		PendenciaNe Pendenciane = (PendenciaNe) request.getSession().getAttribute("PendenciaNe");
		if (Pendenciane == null) Pendenciane = new PendenciaNe();

		PendenciaDt Pendenciadt = (PendenciaDt) request.getSession().getAttribute("PendenciaDt");
		if (Pendenciadt == null) Pendenciadt = new PendenciaDt();

		Pendenciadt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Pendenciadt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		switch (paginaatual) {

		case 1: // Listar Processos Usuário Externo
			BuscaProcessoDt processoDt = new BuscaProcessoDt();
			ProcessoNe processoNe = new ProcessoNe();
			processoDt.setProcessoStatusCodigo(String.valueOf(ProcessoStatusDt.ATIVO));
			
			if (request.getParameter("a") != null && !request.getParameter("a").equals("")) processoDt.setTcoNumero(request.getParameter("a"));
			if (request.getParameter("b") != null && !request.getParameter("b").equals("")) processoDt.setPromovente(request.getParameter("b"));
			if (request.getParameter("c") != null && !request.getParameter("c").equals("")) processoDt.setCpfCnpjParte(request.getParameter("c"));
			processoDt.setId_ServentiaSubTipo(UsuarioSessao.getId_ServentiaSubtipo());
			processoDt.setId_ServentiaUsuario(UsuarioSessao.getId_Serventia());
			
			tempList = processoNe.consultarProcessosUsuariosExternos(processoDt, UsuarioSessao.getUsuarioDt() , posicaoPaginaAtual, String.valueOf(Configuracao.TamanhoRetornoConsulta));
			if (tempList != null) {
				UsuarioSessao.setHashIdProcesso(tempList);
				request.setAttribute("ListaProcessos", tempList);
			}

			request.setAttribute("Paginacao", qtdPaginas(processoNe.getQuantidadePaginas()));
			request.setAttribute("Operacao", "36");
			stAcao = "/WEB-INF/jsptjgo/ListaProcessosXml.jsp";
			break;

		case 2: // Listar Tipos de Pendência
			String[] stDescricaoListaProcessoTipo = {"IdPendenciaTipo", "DescricaoPendenciaTipo"};
			stTag = "PendenciaTipo";
			String stIdPendenciaTipo = "";

			tempList = Pendenciane.consultarDescricaoGrupoPendenciaTipo(stIdPendenciaTipo, UsuarioSessao, posicaoPaginaAtual);

			if (tempList.size() > 0) {

				lisValores = new ArrayList();
				if (tempList != null) {
					for (int i = 0; i < tempList.size(); i++) {
						PendenciaTipoDt obj = (PendenciaTipoDt) tempList.get(i);
						String[] stTemp = {obj.getId(), obj.getPendenciaTipo() };
						lisValores.add(stTemp);
					}
				}

				request.setAttribute("Paginacao", qtdPaginas(Pendenciane.getQuantidadePaginas()));
				request.setAttribute("AtributosTag", stDescricaoListaProcessoTipo);
				request.setAttribute("Tag", stTag);
				request.setAttribute("Operacao", "37");
				request.setAttribute("Valores", lisValores);
				stAcao = "/WEB-INF/jsptjgo/Padroes/ListaDadosXml.jsp";
			} else throw new WebServiceException("Nenhum Tipo de Pendência foi localizado. Verifique os parâmetros informados (página e Id)");
			break;

		case 3: // Listar Serventias(5-Corregedoria, 15-2ºGrau, 16-Vara, 20-Presidencia)
			String[] stDescricaoListaServentia = {"IdServentia", "DescricaoServentia"};
			stTag = "Serventia";

			ServentiaNe serventiaNe = new ServentiaNe();
			tempList = serventiaNe.consultarServentiaTipoCodigo(posicaoPaginaAtual);

			if (tempList.size() > 0) {
				lisValores = new ArrayList();
				if (tempList != null) {
					for (int i = 0; i < tempList.size(); i++) {
						ServentiaDt obj = (ServentiaDt) tempList.get(i);
						String[] stTemp = {obj.getId(), obj.getServentia() };
						lisValores.add(stTemp);
					}
				}
				request.setAttribute("Paginacao", qtdPaginas(serventiaNe.getQuantidadePaginas()));
				request.setAttribute("AtributosTag", stDescricaoListaServentia);
				request.setAttribute("Tag", stTag);
				request.setAttribute("Operacao", "38");
				request.setAttribute("Valores", lisValores);
				stAcao = "/WEB-INF/jsptjgo/Padroes/ListaDadosXml.jsp";
			} else throw new WebServiceException("Nenhuma Serventia foi localizada. Verifique os parâmetros informados (página e Id)");
			break;

		case 4: // Inserir Arquivos na Pendência
			String stNomeArquivo,
			arquivoAssinado64,
			stIdArquivoTipo;

			if (request.getParameter("a") != null && !request.getParameter("a").equals("")) stIdArquivoTipo = request.getParameter("a");
			else throw new WebServiceException("Tipo do Arquivo não encontrado");
			if (request.getParameter("b") != null && !request.getParameter("b").equals("")) stNomeArquivo = request.getParameter("b");
			else throw new WebServiceException("Nome do Arquivo não encontrado");
			if (request.getParameter("c") != null && !request.getParameter("c").equals("")) arquivoAssinado64 = request.getParameter("c");
			else throw new WebServiceException("Arquivo Assinado não encontrado");

			ArquivoDt arquivoDt = new ArquivoDt();
			arquivoDt.setId_ArquivoTipo(stIdArquivoTipo);
			arquivoDt.setArquivo(Base64Utils.base64Decode(arquivoAssinado64));
			if(stNomeArquivo.contains(".p7s"))
				arquivoDt.setNomeArquivo(stNomeArquivo);
			else
				arquivoDt.setNomeArquivo(stNomeArquivo + ".p7s");
			GerenciaArquivo.getInstancia().getArquivoP7s(arquivoDt);

			Pendenciadt.addListaArquivos(arquivoDt);
			request.setAttribute("Mensagem", "Arquivo inserido com sucesso.");
			request.setAttribute("RespostaTipo", "OK");
			request.setAttribute("Operacao", "39");
			stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
			break;

		case 6: // Gerar Pendência para Serventia de um Processo
			PendenciaResponsavelDt responsavel = new PendenciaResponsavelDt();

			if (request.getParameter("a") != null && !request.getParameter("a").equals("")) Pendenciadt.setId_PendenciaTipo(request.getParameter("a"));
			else throw new WebServiceException("Tipo de Pendência não encontrado");
			if (request.getParameter("b") != null && !request.getParameter("b").equals("")) responsavel.setId_Serventia(request.getParameter("b"));
			else throw new WebServiceException("Código da Serventia não encontrado");

			Pendenciadt.addResponsavel(responsavel);
			Pendenciane.criarPendenciaWebservice(Pendenciadt, UsuarioSessao.getUsuarioDt());

			request.setAttribute("Mensagem", "Pendência criada com sucesso.");
			request.setAttribute("RespostaTipo", "OK");
			request.setAttribute("Operacao", "40");
			stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
			break;

		case 7: // Listar Intimações da Serventia

			String[] stDescricaoListaPendenciasIntimacoes = {"IdPendencia", "HashPendencia", "IdProcesso", "NumeroProcesso", "HashProcesso", "IdMovimentacao", "Movimentacao", "TipoPendencia", "DataInicio", "DataLimite", "Status", "IdResponsavel", "NomeResponsavel"};

			stTag = "PendenciasIntimacoes";
			PendenciaNe pendenciaNe = new PendenciaNe();

			tempList = pendenciaNe.consultarIntimacoesCitacoesServentia(UsuarioSessao);

			lisValores = new ArrayList();
			if (tempList != null) {
				for (int i = 0; i < tempList.size(); i++) {
					PendenciaDt objPendencia = (PendenciaDt) tempList.get(i);
					String[] stTemp = {objPendencia.getId(), objPendencia.getHash(), objPendencia.getId_Processo(), objPendencia.getProcessoNumero(), objPendencia.getProcessoDt().getHash(), objPendencia.getId_Movimentacao(), objPendencia.getMovimentacao(), objPendencia.getPendenciaTipo(), objPendencia.getDataInicio(), objPendencia.getDataLimite(), objPendencia.getPendenciaStatus(), objPendencia.getId_Procurador(), objPendencia.getProcurador()};
					lisValores.add(stTemp);
				}
			}

			request.setAttribute("AtributosTag", stDescricaoListaPendenciasIntimacoes);
			request.setAttribute("Tag", stTag);
			request.setAttribute("Operacao", "41");
			request.setAttribute("Valores", lisValores);
			stAcao = "/WEB-INF/jsptjgo/Padroes/ListaDadosXml.jsp";
			break;

		case 8: // Trocar Procurador Responsável por intimação

			String idIntimacao,
			idProcuradorAtual,
			idProcuradorNovo,
			hash;

			if (request.getParameter("a") != null && !request.getParameter("a").equals("")) idIntimacao = request.getParameter("a");
			else throw new WebServiceException("Id da intimação não encontrado");
			if (request.getParameter("b") != null && !request.getParameter("b").equals("")) idProcuradorAtual = request.getParameter("b");
			else throw new WebServiceException("Id do atual procurador não encontrado");
			if (request.getParameter("c") != null && !request.getParameter("c").equals("")) idProcuradorNovo = request.getParameter("c");
			else throw new WebServiceException("Id do novo procurador não encontrado");
			if (request.getParameter("d") != null && !request.getParameter("d").equals("")) hash = request.getParameter("d");
			else throw new WebServiceException("Hash da intimação não encontrado");

			PendenciaResponsavelNe pendenciaResponsavelne = new PendenciaResponsavelNe();
			PendenciaResponsavelDt pendenciaResponsavelDt = new PendenciaResponsavelDt();
			pendenciaResponsavelDt.setId_UsuarioResponsavel(idProcuradorNovo);
			pendenciaResponsavelDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
			pendenciaResponsavelDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
			pendenciaResponsavelDt.setId_Pendencia(idIntimacao);

			String Mensagem = pendenciaResponsavelne.verificarTrocaResponsavelProcessoParteAdvogado(pendenciaResponsavelDt, idProcuradorAtual, UsuarioSessao.getUsuarioDt());

			if (Mensagem.length() == 0) {
				if (UsuarioSessao.VerificarCodigoHashWebService(idIntimacao, hash)) {
					pendenciaResponsavelne.salvarTrocaResponsavelPendencia(pendenciaResponsavelDt, idProcuradorAtual, UsuarioSessao.getUsuarioDt().getGrupoCodigo(), idIntimacao);
					request.setAttribute("Mensagem", "Responsável por intimação alterado com sucesso.");
					request.setAttribute("RespostaTipo", "OK");
					request.setAttribute("Operacao", "45");
					stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
				} else throw new WebServiceException("Operação Não Autorizada");
			} else {
				request.setAttribute("Mensagem", Mensagem);
			}
			break;

		case 9: // Habilitar/Ativar Procurador em Procuradoria

			if (Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getGrupoCodigo()) == GrupoDt.COORDENADOR_PROCURADORIA_ESTADUAL) {
				stOperacao = "46";
				String cpfUsuario, idServentia, oabAdvogado, complementoAdvogado, stId;
				boolean isInativado = false;
				UsuarioDt usuarioAnterior = null;

				if (request.getParameter("a") != null && !request.getParameter("a").equals("")) cpfUsuario = request.getParameter("a");
				else throw new WebServiceException("CPF não encontrado");
				if (request.getParameter("b") != null && !request.getParameter("b").equals("")) idServentia = request.getParameter("b");
				else throw new WebServiceException("Id da Serventia não encontrado");
				if (request.getParameter("c") != null && !request.getParameter("c").equals("")) oabAdvogado = request.getParameter("c");
				else throw new WebServiceException("OAB não encontrado");
				if (request.getParameter("d") != null && !request.getParameter("d").equals("")) complementoAdvogado = request.getParameter("d");
				else throw new WebServiceException("Complemento não encontrado");

				UsuarioDt Advogadodt = new AdvogadoDt();
				UsuarioNe Advogadone = new UsuarioNe();
				UsuarioServentiaOabDt UsuarioServentiaOabdt = new UsuarioServentiaOabDt();

				Advogadodt = Advogadone.consultarUsuarioCpf(cpfUsuario);

				stId = Advogadodt.getId();
				if (stId != null && !stId.equalsIgnoreCase("")) {
					Advogadodt.limpar();
					Advogadodt = Advogadone.consultarAdvogadoId(stId);
					tempList = Advogadone.consultarServentiaOabAdvogado(stId);
					if (tempList.size() > 0) Advogadodt.setListaUsuarioServentias(tempList);
				}

				if (usuarioAnterior == null) usuarioAnterior = new UsuarioDt();
				usuarioAnterior.copiar(Advogadodt);

				UsuarioServentiaOabdt.setOabNumero(oabAdvogado);
				UsuarioServentiaOabdt.setOabComplemento(complementoAdvogado);
				Advogadodt.setUsuarioServentiaOab(UsuarioServentiaOabdt);
				Advogadodt.setId_Serventia(idServentia);
				Advogadodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
				Advogadodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

				Mensagem = Advogadone.verificarServentiaOAB(Advogadodt);

				if (!Advogadodt.getId_Serventia().equalsIgnoreCase("") && !Advogadodt.getId().equalsIgnoreCase("")) {
					if (Advogadone.verificarHabilitacaoUsuario(Advogadodt)) {
						for (int i = 0; i < Advogadodt.getListaUsuarioServentias().size(); i++) {
							UsuarioDt dt = (UsuarioDt) Advogadodt.getListaUsuarioServentias().get(i);
							if (dt.getUsuarioServentiaOab().getOabNumero().equalsIgnoreCase(oabAdvogado) && dt.getUsuarioServentiaOab().getOabComplemento().equalsIgnoreCase(complementoAdvogado) && dt.getId_Serventia().equalsIgnoreCase(idServentia) && dt.getUsuarioServentiaAtivo().equalsIgnoreCase("false")) {
								dt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
								dt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
								Advogadone.ativarAdvogado(dt);
								request.setAttribute("RespostaTipo", "OK");
								request.setAttribute("Mensagem", "Procurador ativado com sucesso.");
								isInativado = true;
								break;
							} 
						}
						if(!isInativado) {
							request.setAttribute("RespostaTipo", "ERRO");
							request.setAttribute("Mensagem", "Procurador já está habilitado na Serventia selecionada.");
							break;
						}
					} else if (Mensagem.length() == 0) {
						Advogadone.salvarUsuarioServentiaOab(Advogadodt);
						request.setAttribute("RespostaTipo", "OK");
						request.setAttribute("Mensagem", "Procurador habilitado com sucesso.");
					}
				}
				request.setAttribute("Operacao", stOperacao);
				stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
			} else {
				request.setAttribute("RespostaTipo", "ERRO");
				request.setAttribute("Mensagem", "Sem permissão para habilitar procurador. Necessário ser coordenador de procuradoria.");
			}
			break;

		default:
			throw new WebServiceException("Operação não definida");

		}
		request.getSession().setAttribute("PendenciaNe", Pendenciane);
		request.getSession().setAttribute("PendenciaDt", Pendenciadt);
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
		return;

	}

	private String qtdPaginas(long qtdRegistros) {
		long loTotal = 0;
		if ((qtdRegistros % Configuracao.TamanhoRetornoConsulta) != 0) {
			loTotal = qtdRegistros / Configuracao.TamanhoRetornoConsulta + 1;
		} else {
			loTotal = qtdRegistros / Configuracao.TamanhoRetornoConsulta;
		}
		return String.valueOf(loTotal);
	}

}