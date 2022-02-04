package br.gov.go.tj.projudi.ct.webservice;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.ct.Controle;
import br.gov.go.tj.projudi.dt.AreaDistribuicaoDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.PendenciaResponsavelDt;
import br.gov.go.tj.projudi.dt.ProcessoParteAdvogadoDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.ne.AreaDistribuicaoNe;
import br.gov.go.tj.projudi.ne.ComarcaNe;
import br.gov.go.tj.projudi.ne.PendenciaResponsavelNe;
import br.gov.go.tj.projudi.ne.ProcessoParteAdvogadoNe;
import br.gov.go.tj.projudi.ne.ServentiaNe;
import br.gov.go.tj.projudi.ne.ServentiaSubtipoProcessoTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.ne.UsuarioServentiaNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.WebServiceException;

public class Servico05Ct extends Controle {

	private static final long serialVersionUID = -8168808657538874694L;

	public int Permissao() {
		return 596;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaoPaginaAtual) throws Exception {

		String stOperacao = "";
		String stTag = "";
		String stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
		List lisValores = null;
		List tempList = new ArrayList();

		response.setContentType("text/xml; charset=UTF-8");
		
		switch (paginaatual) {

		case 1: //Listar Comarcas
			String[] stDescricaoListaComarca = {"IdComarca", "DescricaoComarca", "CodigoComarca"};
			stTag = "Comarca";
			stOperacao = "28";

			ComarcaNe comarcaNe = new ComarcaNe();
			tempList = comarcaNe.consultarDescricao("", qtdPaginasEntrada(posicaoPaginaAtual));

			if (tempList.size() > 0) {
				lisValores = new ArrayList();
				if (tempList != null) {
					for (int i = 0; i < tempList.size(); i++) {
						ComarcaDt obj = (ComarcaDt) tempList.get(i);
						String[] stTemp = {obj.getId(), obj.getComarca(), obj.getComarcaCodigo() };
						lisValores.add(stTemp);
					}
				}

				request.setAttribute("Paginacao", qtdPaginasSaida(comarcaNe.getQuantidadePaginas()));
				request.setAttribute("AtributosTag", stDescricaoListaComarca);
				request.setAttribute("Tag", stTag);
				request.setAttribute("Operacao", stOperacao);
				request.setAttribute("Valores", lisValores);
				stAcao = "/WEB-INF/jsptjgo/Padroes/ListaDadosXml.jsp";
			} else throw new WebServiceException("Nenhuma Comarca foi localizada. Verifique os parâmetros informados (página)");
			break;

		case 2: // Listar Áreas de Distribuição
			String[] stDescricaoListaAreaDistribuicao = {"IdAreaDistribuicao", "DescricaoAreaDistribuicao", "CodigoForum", "IdServentiaSubTipo", "CodigoAreaDistribuicao"};
			stTag = "AreaDistribuicao";
			stOperacao = "29";
			String stIdComarca = "";

			if (request.getParameter("a") != null && !request.getParameter("a").equals("")) stIdComarca = request.getParameter("a");
			else throw new WebServiceException("Id Comarca não encontrado");

			AreaDistribuicaoNe areaDistribuicaoNe = new AreaDistribuicaoNe();
			tempList = areaDistribuicaoNe.consultarAreasDistribuicao("", stIdComarca, qtdPaginasEntrada(posicaoPaginaAtual));

			if (tempList.size() > 0) {

				lisValores = new ArrayList();
				if (tempList != null) {
					for (int i = 0; i < tempList.size(); i++) {
						AreaDistribuicaoDt obj = (AreaDistribuicaoDt) tempList.get(i);
						String[] stTemp = {obj.getId(), obj.getAreaDistribuicao(), obj.getForumCodigo(), obj.getId_ServentiaSubtipo(), obj.getAreaDistribuicaoCodigo()};
						lisValores.add(stTemp);
					}
				}

				request.setAttribute("Paginacao", qtdPaginasSaida(areaDistribuicaoNe.getQuantidadePaginas()));
				request.setAttribute("AtributosTag", stDescricaoListaAreaDistribuicao);
				request.setAttribute("Tag", stTag);
				request.setAttribute("Operacao", stOperacao);
				request.setAttribute("Valores", lisValores);
				stAcao = "/WEB-INF/jsptjgo/Padroes/ListaDadosXml.jsp";
			} else throw new WebServiceException("Nenhuma Área de Distribuição foi localizada. Verifique os parâmetros informados (página e Id)");
			break;

		case 3: // Listar Tipos de Processo por Área de Distribuição
			String[] stDescricaoListaProcessoTipo = {"IdProcessoTipo", "DescricaoProcessoTipo", "CodigoProcessoTipo", "CodigoCNJ" };
			stTag = "Classe";
			stOperacao = "30";
			String stIdAreaDistribuicao = "";

			if (request.getParameter("a") != null && !request.getParameter("a").equals("")) stIdAreaDistribuicao = request.getParameter("a");
			else throw new WebServiceException("Id Área de Distribuição não encontrado");

			ServentiaSubtipoProcessoTipoNe serventiaSubTipoProcessoTipoNe = new ServentiaSubtipoProcessoTipoNe();
			tempList = serventiaSubTipoProcessoTipoNe.consultarProcessoTipos(stIdAreaDistribuicao, "", qtdPaginasEntrada(posicaoPaginaAtual));

			if (tempList.size() > 0) {
				lisValores = new ArrayList();
				if (tempList != null) {
					for (int i = 0; i < tempList.size(); i++) {
						ProcessoTipoDt obj = (ProcessoTipoDt) tempList.get(i);
						String[] stTemp = {obj.getId(), obj.getProcessoTipo(), obj.getProcessoTipoCodigo(), obj.getCnjCodigo()};
						lisValores.add(stTemp);
					}
				}

				request.setAttribute("Paginacao", qtdPaginasSaida(serventiaSubTipoProcessoTipoNe.getQuantidadePaginas()));
				request.setAttribute("AtributosTag", stDescricaoListaProcessoTipo);
				request.setAttribute("Tag", stTag);
				request.setAttribute("Operacao", stOperacao);
				request.setAttribute("Valores", lisValores);
				stAcao = "/WEB-INF/jsptjgo/Padroes/ListaDadosXml.jsp";
			} else throw new WebServiceException("Nenhuma Classe foi localizada. Verifique os parâmetros informados (página e Id)");
			break;

		case 4: // Listar Advogados de um Processo
			String[] stDescricaoListaProcessoParteAdvogado = {"IdProcessoParteAdvogado", "HashProcessoParteAdvogado", "NomeAdvogado", "CpfAdvogado", "Serventia", "Parte", "Principal"};
			stTag = "ProcessoParteAdvogados";
			stOperacao = "34";
			String stIdProcesso = "";

			if (request.getParameter("a") != null && !request.getParameter("a").equals("")) stIdProcesso = request.getParameter("a");
			else throw new WebServiceException("Id Processo não encontrado");

			ProcessoParteAdvogadoNe ProcessoParteAdvogadone = new ProcessoParteAdvogadoNe();
			tempList = ProcessoParteAdvogadone.consultarAdvogadosProcessoComHash(stIdProcesso, UsuarioSessao);

			if (tempList.size() > 0) {

				lisValores = new ArrayList();
				if (tempList != null) {
					for (int i = 0; i < tempList.size(); i++) {
						ProcessoParteAdvogadoDt obj = (ProcessoParteAdvogadoDt) tempList.get(i);
						String[] stTemp = {obj.getId(), obj.getHash(), Funcoes.substituirCaracteresEspeciaisXML(obj.getNomeAdvogado(), true), obj.getUsuarioAdvogado(), obj.getServentiaHabilitacao(), Funcoes.substituirCaracteresEspeciaisXML(obj.getNomeParte(), true), obj.getPrincipal()};
						lisValores.add(stTemp);
					}
				}

				request.setAttribute("AtributosTag", stDescricaoListaProcessoParteAdvogado);
				request.setAttribute("Tag", stTag);
				request.setAttribute("Operacao", stOperacao);
				request.setAttribute("Valores", lisValores);
				stAcao = "/WEB-INF/jsptjgo/Padroes/ListaDadosXml.jsp";
			} else throw new WebServiceException("Nenhum Advogado foi localizado. Verifique o parâmetro informado (Id)");
			break;

		case 6: // Definir Advogado Principal
			stOperacao = "35";
			String stIdProcessoParteAdvogado,
			stHash;

			if (request.getParameter("a") != null && !request.getParameter("a").equals("")) stIdProcessoParteAdvogado = request.getParameter("a");
			else throw new WebServiceException("Id ProcessoParteAdvogado não encontrado");
			if (request.getParameter("b") != null && !request.getParameter("b").equals("")) stHash = request.getParameter("b");
			else throw new WebServiceException("Código Hash do processo não encontrado");

			if (UsuarioSessao.VerificarCodigoHashWebService(stIdProcessoParteAdvogado, stHash)) {
				ProcessoParteAdvogadoNe processoParteAdvogadoNe = new ProcessoParteAdvogadoNe();
				ProcessoParteAdvogadoDt processoParteAdvogadoDt = new ProcessoParteAdvogadoDt();
				processoParteAdvogadoDt = processoParteAdvogadoNe.consultarId(stIdProcessoParteAdvogado);
				processoParteAdvogadoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
				processoParteAdvogadoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
				processoParteAdvogadoDt.setId(stIdProcessoParteAdvogado);
				processoParteAdvogadoNe.defineAdvogadoPrincipal(processoParteAdvogadoDt);
				request.setAttribute("Mensagem", "Advogado principal alterado com sucesso.");
				request.setAttribute("RespostaTipo", "OK");
				request.setAttribute("Operacao", stOperacao);
				stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
			} else throw new WebServiceException("Operação Não Autorizada");
			break;
			
		case 7: // Listar Serventias do Tipo Procuradoria Geral do Estado
			stOperacao = "42";
			String[] stDescricaoListaServentia = {"IdServentia", "DescricaoServentia"};
			stTag = "Serventia";
			String stIdServentiaTipoCodigo = "11"; // 11-Procuradoria Geral do Estado

			ServentiaNe serventiaNe = new ServentiaNe();
			tempList = serventiaNe.consultarDescricaoServentia("", stIdServentiaTipoCodigo,  UsuarioSessao.getUsuarioDt(), posicaoPaginaAtual);

			if (tempList.size() > 0) {
				lisValores = new ArrayList();
				if (tempList != null) {
					for (int i = 0; i < tempList.size(); i++) {
						ServentiaDt obj = (ServentiaDt) tempList.get(i);
						String[] stTemp = {obj.getId(), obj.getServentia() };
						lisValores.add(stTemp);
					}
				}
				request.setAttribute("Paginacao", qtdPaginasSaida(serventiaNe.getQuantidadePaginas()));
				request.setAttribute("AtributosTag", stDescricaoListaServentia);
				request.setAttribute("Tag", stTag);
				request.setAttribute("Operacao", stOperacao);
				request.setAttribute("Valores", lisValores);
				stAcao = "/WEB-INF/jsptjgo/Padroes/ListaDadosXml.jsp";
			} else throw new WebServiceException("Nenhuma Serventia foi localizada. Verifique os parâmetros informados (página e Id)");
			break;
			
		case 8: //Listar Procuradores Responsáveis por Intimação
			stOperacao = "43";
			String[] stDescricaoListaResponsavel = {"IdProcurador", "NomeProcurador", "CpfProcurador"};
			stTag = "Procurador";
			String idPendencia;
			
			if (request.getParameter("a") != null && !request.getParameter("a").equals("")) idPendencia = request.getParameter("a");
			else throw new WebServiceException("Id Intimação não encontrado");

			PendenciaResponsavelNe pendenciaResponsavelne = new PendenciaResponsavelNe();
			tempList = pendenciaResponsavelne.consultarResponsaveisIntimacoesCitacoes(idPendencia, UsuarioSessao.getUsuarioDt().getId_Serventia());
			
			if (tempList.size() > 0) {
				lisValores = new ArrayList();
				if (tempList != null) {
					for (int i = 0; i < tempList.size(); i++) {
						PendenciaResponsavelDt obj = (PendenciaResponsavelDt) tempList.get(i);
						String[] stTemp = {obj.getId_UsuarioResponsavel(), Funcoes.substituirCaracteresEspeciaisXML(obj.getNomeUsuarioResponsavel(), true), obj.getUsuarioResponsavel()};
						lisValores.add(stTemp);
					}
				}
				request.setAttribute("AtributosTag", stDescricaoListaResponsavel);
				request.setAttribute("Tag", stTag);
				request.setAttribute("Operacao", stOperacao);
				request.setAttribute("Valores", lisValores);
				stAcao = "/WEB-INF/jsptjgo/Padroes/ListaDadosXml.jsp";
			} else throw new WebServiceException("Nenhum Responsável foi localizado. Verifique os parâmetros informados (Id)");
			break;
			
		case 9: //Listar Procuradores na Procuradoria
			stOperacao = "44";
			String[] stDescricaoListaProcuradoresServentia = {"IdProcurador", "NomeProcurador", "CpfProcurador"};
			stTag = "Procurador";
			
			UsuarioServentiaNe usuarioServentiaNe = new UsuarioServentiaNe();
			tempList = usuarioServentiaNe.consultarProcuradoresServentia(posicaoPaginaAtual, UsuarioSessao.getUsuarioDt().getId_Serventia());
			
			if (tempList.size() > 0) {
				lisValores = new ArrayList();
				if (tempList != null) {
					for (int i = 0; i < tempList.size(); i++) {
						UsuarioServentiaDt obj = (UsuarioServentiaDt) tempList.get(i);
						String[] stTemp = {obj.getId(), Funcoes.substituirCaracteresEspeciaisXML(obj.getNome(), true), obj.getUsuario()};
						lisValores.add(stTemp);
					}
				}				
				request.setAttribute("Paginacao", qtdPaginasSaida(usuarioServentiaNe.getQuantidadePaginas()));
				request.setAttribute("AtributosTag", stDescricaoListaProcuradoresServentia);
				request.setAttribute("Tag", stTag);
				request.setAttribute("Operacao", stOperacao);
				request.setAttribute("Valores", lisValores);
				stAcao = "/WEB-INF/jsptjgo/Padroes/ListaDadosXml.jsp";
			} else throw new WebServiceException("Nenhum Procurador Disponível");
			break;
			
		default:
			throw new WebServiceException("Operação não definida");
		}

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
		return;

	}

	private String qtdPaginasSaida(long qtdRegistros) {
		long loTotal = 0;
		if ((qtdRegistros % Configuracao.TamanhoRetornoConsulta) != 0) {
			loTotal = qtdRegistros / Configuracao.TamanhoRetornoConsulta + 1;
		} else {
			loTotal = qtdRegistros / Configuracao.TamanhoRetornoConsulta;
		}
		return String.valueOf(loTotal);
	}

	private String qtdPaginasEntrada(String posicaoPaginaAtual) {
		long loPaginaAtual = Funcoes.StringToLong(posicaoPaginaAtual);
		if (loPaginaAtual <= 0) return "0";
		return posicaoPaginaAtual;
	}

}