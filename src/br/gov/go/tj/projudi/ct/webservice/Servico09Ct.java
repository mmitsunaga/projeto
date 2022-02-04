package br.gov.go.tj.projudi.ct.webservice;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.ct.Controle;
import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.projudi.dt.CidadeDt;
import br.gov.go.tj.projudi.dt.EstadoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.PaisDt;
import br.gov.go.tj.projudi.ne.BairroNe;
import br.gov.go.tj.projudi.ne.CidadeNe;
import br.gov.go.tj.projudi.ne.EstadoNe;
import br.gov.go.tj.projudi.ne.PaisNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.ProcessoResponsavelNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.WebServiceException;

public class Servico09Ct extends Controle {

	private static final long serialVersionUID = 6574817466999853758L;

	public int Permissao() {
		return 760;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception {

		String stOperacao = "";
		List lisValores = null;
		String stTag = "";
		List tempList = new ArrayList();
		String a, b;
		String stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
		ProcessoNe processoNe;
		String Mensagem = "";
		
		response.setContentType("text/xml; charset=UTF-8");

		switch (paginaatual) {

		case 1: // Listar Países
			stOperacao = "53";
			String[] stDescricaoListaPais = {"IdPais", "DescricaoPais"};
			stTag = "Pais";

			if (request.getParameter("a") != null) a = request.getParameter("a");
			else throw new WebServiceException("Descrição não encontrada");
			
			tempList = new PaisNe().consultarDescricao(a);

			lisValores = new ArrayList();
			if (tempList != null) {
				for (int i = 0; i < tempList.size(); i++) {
					PaisDt paisDt = (PaisDt) tempList.get(i);
					String[] stTemp = {paisDt.getId(), Funcoes.substituirCaracteresEspeciaisXML(paisDt.getPais(), true)};
					lisValores.add(stTemp);
				}
			}

			request.setAttribute("AtributosTag", stDescricaoListaPais);
			request.setAttribute("Tag", stTag);
			request.setAttribute("Operacao", stOperacao);
			request.setAttribute("Valores", lisValores);
			stAcao = "/WEB-INF/jsptjgo/Padroes/ListaDadosXml.jsp";

			break;

		case 2: // Listar Estados
			stOperacao = "54";
			String[] stDescricaoListaEstados = {"IdEstado", "DescricaoEstado"};
			stTag = "Estado";

			if (request.getParameter("a") != null) a = request.getParameter("a");
			else throw new WebServiceException("Id não encontrado");
			
			tempList = new EstadoNe().consultarIdPais(a);

			lisValores = new ArrayList();
			if (tempList != null) {
				for (int i = 0; i < tempList.size(); i++) {
					EstadoDt estadoDt = (EstadoDt) tempList.get(i);
					String[] stTemp = {estadoDt.getId(), Funcoes.substituirCaracteresEspeciaisXML(estadoDt.getEstado(), true)};
					lisValores.add(stTemp);
				}
			}

			request.setAttribute("AtributosTag", stDescricaoListaEstados);
			request.setAttribute("Tag", stTag);
			request.setAttribute("Operacao", stOperacao);
			request.setAttribute("Valores", lisValores);
			stAcao = "/WEB-INF/jsptjgo/Padroes/ListaDadosXml.jsp";

			break;

		case 3: // Listar Cidades
			stOperacao = "55";
			String[] stDescricaoListaCidades = {"IdCidade", "DescricaoCidade"};
			stTag = "Cidade";

			if (request.getParameter("a") != null) a = request.getParameter("a");
			else throw new WebServiceException("Id não encontrado");
			
			tempList = new CidadeNe().consultarIdEstado(a);

			lisValores = new ArrayList();
			if (tempList != null) {
				for (int i = 0; i < tempList.size(); i++) {
					CidadeDt cidadeDt = (CidadeDt) tempList.get(i);
					String[] stTemp = {cidadeDt.getId(), Funcoes.substituirCaracteresEspeciaisXML(cidadeDt.getCidade(), true)};
					lisValores.add(stTemp);
				}
			}

			request.setAttribute("AtributosTag", stDescricaoListaCidades);
			request.setAttribute("Tag", stTag);
			request.setAttribute("Operacao", stOperacao);
			request.setAttribute("Valores", lisValores);
			stAcao = "/WEB-INF/jsptjgo/Padroes/ListaDadosXml.jsp";

			break;

		case 4: // Listar Bairros
			stOperacao = "56";
			String[] stDescricaoListaBairros = {"IdBairro", "DescricaoBairro" };
			stTag = "Bairro";

			if (request.getParameter("a") != null) a = request.getParameter("a");
			else throw new WebServiceException("Id não encontrada");
			
			tempList = new BairroNe().consultarIdCidade(a);

			lisValores = new ArrayList();
			if (tempList != null) {
				for (int i = 0; i < tempList.size(); i++) {
					BairroDt bairroDt = (BairroDt) tempList.get(i);
					String[] stTemp = {bairroDt.getId(), Funcoes.substituirCaracteresEspeciaisXML(bairroDt.getBairro(), true)};
					lisValores.add(stTemp);
				}
			}

			request.setAttribute("AtributosTag", stDescricaoListaBairros);
			request.setAttribute("Tag", stTag);
			request.setAttribute("Operacao", stOperacao);
			request.setAttribute("Valores", lisValores);
			stAcao = "/WEB-INF/jsptjgo/Padroes/ListaDadosXml.jsp";

			break;
			
		case 6: // Listar Processos de Advogados/Procuradores por Id
			stOperacao = "59";
			String idProcessoTipo = "";
			String dataRecebimento = "";
			
			if (request.getParameter("a") != null) a = request.getParameter("a");
			else throw new WebServiceException("Id não encontrado");
			if (request.getParameter("b") != null) idProcessoTipo = request.getParameter("b");
			if (request.getParameter("c") != null) dataRecebimento = request.getParameter("c");
			
			UsuarioSessao.getUsuarioDt().setId_UsuarioServentia(a);
			
			processoNe = new ProcessoNe();
			tempList = processoNe.consultarProcessosAdvogadoWebService(UsuarioSessao, idProcessoTipo, dataRecebimento, posicaopaginaatual);

			if (tempList != null) request.setAttribute("ListaProcessos", tempList);

			request.setAttribute("Paginacao", qtdPaginas(processoNe.getQuantidadePaginas()));
			request.setAttribute("Operacao", stOperacao);
			stAcao = "/WEB-INF/jsptjgo/ListaProcessosXml.jsp";
			break;
			
		case 7: // Trocar Advogado/Procurador Responsável por Processo
			String stIdProcesso,
			stIdNovoResponsavel,
			stIdAtualResponsavel,
			hash;
			stOperacao = "60";
			Mensagem = "";

			if (request.getParameter("a") != null && !request.getParameter("a").equals("")) stIdProcesso = request.getParameter("a");
			else throw new WebServiceException("Processo não encontrado");
			if (request.getParameter("b") != null && !request.getParameter("b").equals("")) stIdNovoResponsavel = request.getParameter("b");
			else throw new WebServiceException("Novo Responsável não encontrado");
			if (request.getParameter("c") != null && !request.getParameter("c").equals("")) stIdAtualResponsavel = request.getParameter("c");
			else throw new WebServiceException("Responsável Atual não encontrado");
			if (request.getParameter("d") != null && !request.getParameter("d").equals("")) hash = request.getParameter("d");
			else throw new WebServiceException("Código Hash do processo não encontrado");

			if (UsuarioSessao.VerificarCodigoHashWebService(stIdProcesso, hash)) {
				ProcessoResponsavelNe ProcessoResponsavelne = new ProcessoResponsavelNe(); 
				ProcessoResponsavelne.salvarTrocaResponsavelProcuradorProcesso(stIdNovoResponsavel, stIdAtualResponsavel, UsuarioSessao.getUsuarioDt().getGrupoCodigo(), stIdProcesso, new LogDt(UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog()));
				
				request.setAttribute("Mensagem", "Responsável por Processo alterado com sucesso.");
				request.setAttribute("RespostaTipo", "OK");
				request.setAttribute("Operacao", stOperacao);
				stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
			} else throw new WebServiceException("Operação Não Autorizada");
			break;
			
		case 8: // Informar pagamento de Custas
			String numProcesso,
			numGuia,
			valor,
			dataPagamento,
			dataMovimentoBancario;
			stOperacao = "62";
			Mensagem = "";

			if (request.getParameter("a") != null && !request.getParameter("a").equals("")) numProcesso = request.getParameter("a");
			else throw new WebServiceException("Número do processo não encontrado");
			if (request.getParameter("b") != null && !request.getParameter("b").equals("")) numGuia = request.getParameter("b");
			else throw new WebServiceException("Número da guia não encontrado");
			if (request.getParameter("c") != null && !request.getParameter("c").equals("")) valor = request.getParameter("c");
			else throw new WebServiceException("Valor pago não encontrado");
			if (request.getParameter("d") != null && !request.getParameter("d").equals("")) dataPagamento = request.getParameter("d");
			else throw new WebServiceException("Data de pagamento não encontrada");
			if (request.getParameter("e") != null && !request.getParameter("e").equals("")) dataMovimentoBancario = request.getParameter("e");
			else throw new WebServiceException("Data de movimento não encontrada");
			
			processoNe = new ProcessoNe();
			Mensagem = processoNe.informarPagamentoCustas(numProcesso, numGuia, valor, dataPagamento, dataMovimentoBancario, UsuarioSessao.getUsuarioDt());
			
			if (Mensagem.length() == 0) {
				request.setAttribute("Mensagem", "Pagamento informado com sucesso.");
				request.setAttribute("RespostaTipo", "OK");
			} else {
				request.setAttribute("RespostaTipo", "ERRO");
				request.setAttribute("Mensagem", Mensagem);
			}
			request.setAttribute("Operacao", stOperacao);
			stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";

			break;
			
		case 9: // Consultar processo por número
			stOperacao = "63";

			if (request.getParameter("a") != null) a = request.getParameter("a");
			else throw new WebServiceException("Número do processo não encontrado");
			
			tempList =  new ProcessoNe().consultarTodosProcessosWebservice(a, UsuarioSessao);

			if (tempList != null) request.setAttribute("ListaProcessos", tempList);

			request.setAttribute("Operacao", stOperacao);
			stAcao = "/WEB-INF/jsptjgo/ListaProcessosXml.jsp";

			break;
			
		default:
			request.setAttribute("Mensagem", "Operação não definida");
			request.setAttribute("RespostaTipo", "ERRO");
			request.setAttribute("Operacao", "-1");

		}
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
