package br.gov.go.tj.projudi.ct.webservice;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.ct.Controle;
import br.gov.go.tj.projudi.dt.AssuntoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteAdvogadoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.AssuntoNe;
import br.gov.go.tj.projudi.ne.GrupoNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.ProcessoParteAdvogadoNe;
import br.gov.go.tj.projudi.ne.ProcessoParteNe;
import br.gov.go.tj.projudi.ne.ServentiaNe;
import br.gov.go.tj.projudi.ne.ServentiaSubtipoAssuntoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.WebServiceException;

public class Servico08Ct extends Controle {

	private static final long serialVersionUID = 6574817466999853758L;

	public int Permissao() {
		return 679;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception {

		String stOperacao = "";
		String Mensagem;
		List tempList = new ArrayList();
		String stTag = "";
		UsuarioNe ServidorJudiciarione;
		UsuarioDt ServidorJudiciariodt;
		ServentiaNe serventiaNe;
		ServentiaDt serventiaDt;
		String codigoComarca;
		ProcessoParteNe parte;
		UsuarioNe advogado;
		String dados;
		String a, b, c;
		GrupoNe grupoNe;
		List lisValores = null;
		String stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
		
		response.setContentType("text/xml; charset=UTF-8");

		switch (paginaatual) {
		case 1: // Habilitar/Ativar Coordenador de Procuradoria
			stOperacao = "48";
			Mensagem = "";
			ServidorJudiciarione = new UsuarioNe();
			ServidorJudiciariodt = new UsuarioDt();
			grupoNe = new GrupoNe();

			if (request.getParameter("a") != null && !request.getParameter("a").equals("")) a = request.getParameter("a");
			else throw new WebServiceException("CPF não encontrado");
			if (request.getParameter("b") != null && !request.getParameter("b").equals("")) b = request.getParameter("b");
			else throw new WebServiceException("Id da Serventia não encontrado");

			ServidorJudiciariodt = ServidorJudiciarione.consultarUsuarioCpf(a);
			ServidorJudiciariodt.setId_Serventia(b);
			GrupoDt grupoDt = grupoNe.consultarGrupoCodigo(UsuarioSessao.getUsuarioDt().getGrupoCodigo());
			ServidorJudiciariodt.setId_Grupo(grupoDt.getId());
			ServidorJudiciariodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
			ServidorJudiciariodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

			Mensagem = ServidorJudiciarione.verificarServentiaGrupoUsuario(ServidorJudiciariodt);
			if (Mensagem.length() == 0 && ServidorJudiciarione.verificarHabilitacaoServidor(ServidorJudiciariodt)) {
				Mensagem = "Usuário já cadastrado na Serventia e Cargo informados.";
			}
			if (Mensagem.length() == 0) {
				ServidorJudiciarione.salvarUsuarioServentiaGrupo(ServidorJudiciariodt);
				request.setAttribute("RespostaTipo", "OK");
				request.setAttribute("Mensagem", "Dados Serventia e Grupo Salvos com Sucesso.");
			} else {
				request.setAttribute("RespostaTipo", "ERRO");
				request.setAttribute("Mensagem", Mensagem);
			}

			request.setAttribute("Operacao", stOperacao);
			stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
			break;

		case 2: // Desativar Coordenador de Procuradoria
			stOperacao = "49";
			Mensagem = "";
			String idUsuarioServentiaGrupo = "";
			ServidorJudiciarione = new UsuarioNe();
			ServidorJudiciariodt = new UsuarioDt();
			grupoNe = new GrupoNe();

			if (request.getParameter("a") != null && !request.getParameter("a").equals("")) a = request.getParameter("a");
			else throw new WebServiceException("CPF não encontrado");
			if (request.getParameter("b") != null && !request.getParameter("b").equals("")) b = request.getParameter("b");
			else throw new WebServiceException("Id da Serventia não encontrado");

			ServidorJudiciariodt = ServidorJudiciarione.consultarUsuarioCpf(a);
			ServidorJudiciariodt.setId_Serventia(b);
			grupoDt = grupoNe.consultarGrupoCodigo(String.valueOf(GrupoDt.COORDENADOR_PROCURADORIA_ESTADUAL));
			ServidorJudiciariodt.setId_Grupo(grupoDt.getId());
			ServidorJudiciariodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
			ServidorJudiciariodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

			idUsuarioServentiaGrupo = ServidorJudiciarione.consultarIdUsuarioServentiaGrupo(grupoDt.getId(), b, ServidorJudiciariodt.getId());
			ServidorJudiciariodt.setId_UsuarioServentiaGrupo(idUsuarioServentiaGrupo);
			
			Mensagem = ServidorJudiciarione.verificarServentiaGrupoUsuario(ServidorJudiciariodt);
			if (Mensagem.length() == 0 && !ServidorJudiciarione.verificarHabilitacaoServidor(ServidorJudiciariodt)) {
				Mensagem = "Usuário não cadastrado na Serventia e Cargo informados.";
			}
			if (Mensagem.length() == 0) {
				ServidorJudiciarione.desativarUsuarioServentiaGrupo(ServidorJudiciariodt);
				request.setAttribute("RespostaTipo", "OK");
				request.setAttribute("Mensagem", "Procurador Desabilitado na Serventia com sucesso.");
			} else {
				request.setAttribute("RespostaTipo", "ERRO");
				request.setAttribute("Mensagem", Mensagem);
			}

			request.setAttribute("Operacao", stOperacao);
			stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
			break;
			
		case 3: // Habilitar Procurador em Processo
			stOperacao = "50";
			Mensagem = "";
			String idProcesso = "";
			String[] partesSelecionadas = new String[1];
			ServidorJudiciarione = new UsuarioNe();
			UsuarioDt usuarioDt = new UsuarioDt();
			ProcessoParteAdvogadoNe ProcessoParteAdvogadone = new ProcessoParteAdvogadoNe();
			ProcessoParteAdvogadoDt ProcessoParteAdvogadodt = new ProcessoParteAdvogadoDt();
			
			if (request.getParameter("a") != null && !request.getParameter("a").equals("")) a = request.getParameter("a");
			else throw new WebServiceException("CPF não encontrado");
			if (request.getParameter("b") != null && !request.getParameter("b").equals("")) idProcesso = request.getParameter("b");
			else throw new WebServiceException("Id do Processo não encontrado");
			if (request.getParameter("c") != null && !request.getParameter("c").equals("")) idProcesso = request.getParameter("c");
			else throw new WebServiceException("Id da Parte do Processo não encontrado");
			
			usuarioDt = ServidorJudiciarione.consultarUsuarioCpf(a);
			
			ProcessoParteAdvogadodt.setId_UsuarioServentiaAdvogado(usuarioDt.getId_UsuarioServentia());
			ProcessoParteAdvogadodt.setId_ProcessoParte(partesSelecionadas[0]);
			ProcessoParteAdvogadodt.setId_Processo(idProcesso);
			ProcessoParteAdvogadodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
			ProcessoParteAdvogadodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
			ProcessoParteAdvogadodt.setRecebeIntimacao(true);
							
			Mensagem = ProcessoParteAdvogadone.verificarAdvogadoHabilitacao(ProcessoParteAdvogadodt, partesSelecionadas);

			if (Mensagem.length() == 0) {
				ProcessoParteAdvogadone.salvarHabilitacaoAdvogado(ProcessoParteAdvogadodt, partesSelecionadas);
				request.setAttribute("RespostaTipo", "OK");
				request.setAttribute("Mensagem", "Habilitação de Procurador realizada com sucesso.");
			} else {
				request.setAttribute("RespostaTipo", "ERRO");
				request.setAttribute("Mensagem", Mensagem);
			}
			request.setAttribute("Operacao", stOperacao);
			break;
			
		case 4: // Consultar Processo por Nome da Parte
			stOperacao = "51";
			a = "";
			dados = "";
			if (request.getParameter("a") != null && !request.getParameter("a").equals("")) a = request.getParameter("a");
			else throw new WebServiceException("Nome não encontrado");
			
			serventiaNe = new ServentiaNe();
			serventiaDt = serventiaNe.consultarServentiaCodigo(UsuarioSessao.getUsuarioDt().getServentiaCodigo());
			codigoComarca = serventiaDt.getComarcaCodigo();
			
			parte = new ProcessoParteNe();
			dados = parte.consultarParteNome(Funcoes.converteNomeSimplificado(a), codigoComarca, posicaopaginaatual);
			request.setAttribute("Paginacao", qtdPaginas(parte.getQuantidadePaginas()));
			request.setAttribute("RespostaTipo", "OK");
			request.setAttribute("Operacao", stOperacao);
			request.setAttribute("Mensagem", dados);
			stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
			break;
			
		case 6: // Consultar Processo por CPF da Parte
			stOperacao = "52";
			a = "";
			dados = "";
			if (request.getParameter("a") != null && !request.getParameter("a").equals("")) a = request.getParameter("a");
			else throw new WebServiceException("CPF não encontrado");
			serventiaNe = new ServentiaNe();
			serventiaDt = serventiaNe.consultarServentiaCodigo(UsuarioSessao.getUsuarioDt().getServentiaCodigo());
			codigoComarca = serventiaDt.getComarcaCodigo();
			
			parte = new ProcessoParteNe();
			dados = parte.consultarParteCpf(a, codigoComarca, posicaopaginaatual);
			request.setAttribute("Paginacao", qtdPaginas(parte.getQuantidadePaginas()));
			request.setAttribute("RespostaTipo", "OK");
			request.setAttribute("Operacao", stOperacao);
			request.setAttribute("Mensagem", dados);
			stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
			break;

		case 7: // Listar Assuntos
			stOperacao = "57";
			
			if (request.getParameter("a") != null && !request.getParameter("a").equals("")) a = request.getParameter("a");
			else throw new WebServiceException("Id Área de Distribuição não encontrado");
			
			String[] stDescricaoListaArquivoTipo = {"IdAssunto", "DescricaoAssunto", "Pai", "DispositivoLegal", "CodigoAssunto"};			
			stTag = "Assunto";
			ServentiaSubtipoAssuntoNe serventiaSubtipoAssuntoNe = new ServentiaSubtipoAssuntoNe();
			tempList = serventiaSubtipoAssuntoNe.consultarAssuntosAreaDistribuicao(a, "", posicaopaginaatual);

			lisValores = new ArrayList();
			if (tempList != null) {
				for (int i = 0; i < tempList.size(); i++) {
					AssuntoDt obj = (AssuntoDt) tempList.get(i);
					String[] stTemp = {obj.getId(), Funcoes.substituirCaracteresEspeciaisXML(obj.getAssunto(), true), Funcoes.substituirCaracteresEspeciaisXML(obj.getAssuntoPai(), true), obj.getDispositivoLegal(), obj.getAssuntoCodigo()};
					lisValores.add(stTemp);
				}
			}

			request.setAttribute("Paginacao", qtdPaginas(serventiaSubtipoAssuntoNe.getQuantidadePaginas()));
			request.setAttribute("AtributosTag", stDescricaoListaArquivoTipo);
			request.setAttribute("Tag", stTag);
			request.setAttribute("Operacao", stOperacao);
			request.setAttribute("Valores", lisValores);
			stAcao = "/WEB-INF/jsptjgo/Padroes/ListaDadosXml.jsp";
			break;
			
		case 8: // Consultar Processo por OAB do Advogado
			stOperacao = "58";
			a = "";
			dados = "";
			if (request.getParameter("a") != null && !request.getParameter("a").equals("")) a = request.getParameter("a");
			else throw new WebServiceException("OAB não encontrado");
			if (request.getParameter("b") != null && !request.getParameter("b").equals("")) b = request.getParameter("b");
			else throw new WebServiceException("Complemento não encontrado");
			if (request.getParameter("c") != null && !request.getParameter("c").equals("")) c = request.getParameter("c");
			else throw new WebServiceException("Estado não encontrado");
			
			serventiaNe = new ServentiaNe();
			serventiaDt = serventiaNe.consultarServentiaCodigo(UsuarioSessao.getUsuarioDt().getServentiaCodigo());
			codigoComarca = serventiaDt.getComarcaCodigo();
			
			advogado = new UsuarioNe();
			dados = advogado.consultarAdvogadoOAB(codigoComarca, a, b, c);
			request.setAttribute("RespostaTipo", "OK");
			request.setAttribute("Operacao", stOperacao);
			request.setAttribute("Mensagem", dados);
			stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
			break;
			
		case 9: // Consultar Custas Processuais Fazenda Pública
			stOperacao = "61";
			a = "";
			String[] stDescricaoProcesso = {"NumeroProcesso", "DataGuia", "Valor", "NumeroGuia" };
			stTag = "Processo";

			if (request.getParameter("a") != null && !request.getParameter("a").equals("")) a = request.getParameter("a");
			else throw new WebServiceException("Número de processo não encontrado");
			String numeroProcesso = a.replaceAll("\\.", "");
			
			while (numeroProcesso.length() < 20)
				numeroProcesso = "0" + numeroProcesso;
			numeroProcesso = numeroProcesso.substring(0, 7) + "." + numeroProcesso.substring(7, 9) + "." + 
				numeroProcesso.substring(9, 13) + "." + numeroProcesso.substring(13, 14) + "." + 
				numeroProcesso.substring(14, 16) + "." + numeroProcesso.substring(16, 20);
			ProcessoNe processoNe = new ProcessoNe();
			ProcessoDt processo = processoNe.consultarProcessoNumeroCompleto(numeroProcesso, null);
			if(processo == null) throw new WebServiceException("Número de processo não encontrado");
			GuiaEmissaoDt guia = processoNe.consultarCustas(processo.getId());
			if(guia == null) throw new WebServiceException("Processo não possui guia emitida");
			String numeroGuia = guia.getNumeroGuiaCompleto();
			numeroGuia = numeroGuia.substring(0, 5) + "-" + numeroGuia.substring(5, 6) + "/" + numeroGuia.substring(6, 8);
			List lisTemp = new ArrayList();
			String[] stTemp = { numeroProcesso, guia.getDataEmissao(), guia.getValorTotalGuia(), numeroGuia };
			lisTemp.add(stTemp);
			
			request.setAttribute("Paginacao", "0");
			request.setAttribute("AtributosTag", stDescricaoProcesso);
			request.setAttribute("Tag", stTag);
			request.setAttribute("Operacao", stOperacao);
			request.setAttribute("Valores", lisTemp);
			stAcao = "/WEB-INF/jsptjgo/Padroes/ListaDadosXml.jsp";
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
