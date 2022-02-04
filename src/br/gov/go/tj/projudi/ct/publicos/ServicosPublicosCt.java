package br.gov.go.tj.projudi.ct.publicos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.ct.Controle;
import br.gov.go.tj.projudi.dt.BuscaProcessoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.RecursoDt;
import br.gov.go.tj.projudi.dt.UsuarioFoneDt;
import br.gov.go.tj.projudi.ne.MovimentacaoNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.UsuarioFoneNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ServicosPublicosCt extends Controle {

	private static final long serialVersionUID = -16208421625822233L;

	public int Permissao() {
		return 882;
	}
	
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {
		
		String stOperacao = "";
		List tempList = new ArrayList();
		String stTag = "";
		String a, b, c, d, numeroProcesso;
		ProcessoNe processoNe;
		ProcessoDt processo;
		String idProcesso, idServentia;
		List lisValores = null;
		String stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
		
		try{
			switch (paginaatual) {
			case 1: //CONSULTAR CUSTAS PROCESSUAIS FAZENDA PÚBLICA
				a = "";
				String[] stDescricaoProcesso = {"NumeroProcesso", "DataGuia", "Valor", "NumeroGuia" };
				stTag = "Processo";

				if (request.getParameter("a") != null && !request.getParameter("a").equals("")) a = request.getParameter("a");
				else throw new Exception("<{Número de processo não encontrado.}>");
				
				numeroProcesso = a.replaceAll("\\.", "");
				while (numeroProcesso.length() < 20)
					numeroProcesso = "0" + numeroProcesso;
				numeroProcesso = numeroProcesso.substring(0, 7) + "." + numeroProcesso.substring(7, 9) + "." + numeroProcesso.substring(9, 13) + "." + numeroProcesso.substring(13, 14) + "." + numeroProcesso.substring(14, 16) + "." + numeroProcesso.substring(16, 20);
				processoNe = new ProcessoNe();
				processo = processoNe.consultarProcessoNumeroCompleto(numeroProcesso,null);
				if(processo == null) throw new Exception("<{Número de processo não encontrado.}>");
				GuiaEmissaoDt guia = processoNe.consultarCustas(processo.getId());
				if(guia == null) throw new Exception("<{Processo não possui guia emitida.}>");
				String numeroGuia = guia.getNumeroGuiaCompleto();
				numeroGuia = numeroGuia.substring(0, 5) + "-" + numeroGuia.substring(5, 6) + "/" + numeroGuia.substring(6, 8);
				List lisTemp = new ArrayList();
				String[] stTemp = {numeroProcesso, guia.getDataEmissao(), guia.getValorTotalGuia(), numeroGuia};
				lisTemp.add(stTemp);
				
				request.setAttribute("Paginacao", "0");
				request.setAttribute("AtributosTag", stDescricaoProcesso);
				request.setAttribute("Tag", stTag);
				request.setAttribute("Operacao", "61");
				request.setAttribute("Valores", lisTemp);
				stAcao = "/WEB-INF/jsptjgo/Padroes/ListaDadosXml.jsp";
				break;
				
			case 2: //LISTAR DADOS DE PROCESSO
				a="";b="";c="";
				processoNe = new ProcessoNe();
				ProcessoDt processoDt;
				
				if (request.getParameter("a") != null && !request.getParameter("a").equals(""))	a = request.getParameter("a"); //Número Processo Simplificado
				else throw new Exception("<{Número de processo não encontrado.}>");
//				if (request.getParameter("b") != null && !request.getParameter("b").equals(""))	b = request.getParameter("b"); //Cpf/Cnpj
//				if (request.getParameter("c") != null && !request.getParameter("c").equals(""))	c = request.getParameter("c"); //Nome da Parte
				BuscaProcessoDt buscaProcesso = new BuscaProcessoDt();
				buscaProcesso.setProcessoNumero(a);
				
				tempList = processoNe.consultarProcessosPublica(buscaProcesso, posicaopaginaatual);
				
				for(int i = 0; i < tempList.size(); i++) {
					processoDt = (ProcessoDt)tempList.get(i);
					processoDt = processoNe.consultarDadosProcessoAcessoExterno(processoDt.getId(),UsuarioSessao.getUsuarioDt() , true, false, UsuarioSessao.getNivelAcesso());
					if(processoDt.isSegredoJustica()) processoDt.setId("");
					else processoDt.setHash(UsuarioSessao.getCodigoHash(processoDt.getId()));
					if (processoDt.getId_Recurso().length() > 0) {
						RecursoDt recursoDt = processoNe.consultarDadosRecurso(processoDt.getId_Recurso(), processoDt, UsuarioSessao.getUsuarioDt(), true);
						processoDt.setRecursoDt(recursoDt);
					}
					tempList.set(i, processoDt);
				}
				
				request.setAttribute("Operacao", "74");
				request.setAttribute("ListaProcessos", tempList);
				stAcao = "/WEB-INF/jsptjgo/DadosProcessoWebserviceXml.jsp";
				break;
				
			case 3: //LISTAR MOVIMENTAÇÕES DE PROCESSO
				String hashProcesso, complemento;
				String[] stDescricaoListaMovimentacoes = {"IdMovimentacao", "HashMovimentacao", "DescricaoMovimentacao", "Complemento", "DataRealizacao", "NomeUsuarioRealizador"};
				stTag = "Movimentacao";

				if (request.getParameter("a") != null && !request.getParameter("a").equals("")) idProcesso = request.getParameter("a");
				else throw new Exception("<{Processo não encontrado.}> Local Exception: " + this.getClass().getName() + ".executar()");
				if (request.getParameter("b") != null && !request.getParameter("b").equals("")) hashProcesso = request.getParameter("b");
				else throw new Exception("<{Hash não encontrado.}> Local Exception: " + this.getClass().getName() + ".executar()");

				if (UsuarioSessao.VerificarCodigoHash(idProcesso, hashProcesso)) {
					MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
					tempList = movimentacaoNe.consultarMovimentacoesProcesso(UsuarioSessao.getUsuarioDt(), idProcesso, UsuarioSessao.getNivelAcesso());
					
					lisValores = new ArrayList();
					if (tempList != null) {
						for (int i = 0; i < tempList.size(); i++) {
							MovimentacaoDt obj = (MovimentacaoDt) tempList.get(i);
							complemento = "";
							if(obj.getComplemento().indexOf("<a") != -1 && obj.getComplemento().indexOf("a>") != -1) {
								complemento = obj.getComplemento().substring(0, obj.getComplemento().indexOf("<a")) + obj.getComplemento().substring(obj.getComplemento().indexOf("a>") + 2, obj.getComplemento().length());		
							} else {
								complemento = obj.getComplemento();
							}
							String[] temp = {obj.getId(), UsuarioSessao.getCodigoHash(obj.getId()), Funcoes.substituirCaracteresEspeciaisXML(obj.getMovimentacaoTipo(), true), Funcoes.substituirCaracteresEspeciaisXML(complemento, true), obj.getDataRealizacao(), Funcoes.substituirCaracteresEspeciaisXML(obj.getNomeUsuarioRealizador(), true)};
							lisValores.add(temp);
						}
					}
				} else
					throw new Exception("<{Operação Não Autorizada.}> Local Exception: " + this.getClass().getName() + ".executar()");

				request.setAttribute("AtributosTag", stDescricaoListaMovimentacoes);
				request.setAttribute("Tag", stTag);
				request.setAttribute("Operacao", "75");
				request.setAttribute("Valores", lisValores);
				stAcao = "/WEB-INF/jsptjgo/Padroes/ListaDadosXml.jsp";
				break;
				
			case 4: //LISTAR PENDÊNCIAS DE PROCESSO
				List[] listaPendencias = null;
				List conclusoesPendentes = null;
				List<String[]> audienciaPendente = null;
				
				if (request.getParameter("a") != null && !request.getParameter("a").equals("")) idProcesso = request.getParameter("a");
				else throw new Exception("<{Id Processo não encontrado.}> Local Exception: " + this.getClass().getName() + ".executar()");
				if (request.getParameter("b") != null && !request.getParameter("b").equals("")) hashProcesso = request.getParameter("b");
				else throw new Exception("<{Hash não encontrado.}> Local Exception: " + this.getClass().getName() + ".executar()");

				if (UsuarioSessao.VerificarCodigoHash(idProcesso, hashProcesso)) {
					processoNe = new ProcessoNe();
					listaPendencias = processoNe.consultarPendenciasProcesso(idProcesso, true);
					conclusoesPendentes = processoNe.consultarConclusoesPendentesProcessoPublico(idProcesso, true);
					audienciaPendente = processoNe.consultarAudienciasPendentes(idProcesso, true);
				} else
					throw new Exception("<{Operação Não Autorizada.}> Local Exception: " + this.getClass().getName() + ".executar()");

				request.setAttribute("Operacao", "76");
				request.setAttribute("ListaAudiencias", audienciaPendente);
				request.setAttribute("ListaPendencias", listaPendencias);
				request.setAttribute("ListaConclusoes", conclusoesPendentes);
				stAcao = "/WEB-INF/jsptjgo/PendenciasWebserviceXml.jsp";
				break;
				
			case 6: //LISTAR RESPONSÁVEIS DE PROCESSO
				List listaResponsaveis = null;
				List listaAdvogados = null;
				
				if (request.getParameter("a") != null && !request.getParameter("a").equals("")) idProcesso = request.getParameter("a");
				else throw new Exception("<{Id Processo não encontrado.}> Local Exception: " + this.getClass().getName() + ".executar()");
				if (request.getParameter("b") != null && !request.getParameter("b").equals("")) hashProcesso = request.getParameter("b");
				else throw new Exception("<{Hash não encontrado.}> Local Exception: " + this.getClass().getName() + ".executar()");

				if (UsuarioSessao.VerificarCodigoHash(idProcesso, hashProcesso)) {
					ProcessoNe processone = new ProcessoNe();
					idServentia = processone.consultarServentiaProcesso(idProcesso);
					listaResponsaveis = processone.consultarResponsaveisProcesso(idProcesso, idServentia, UsuarioSessao.getGrupoCodigo());
					listaAdvogados = processone.consultarAdvogadosProcessoPublico(idProcesso);
				} else
					throw new Exception("<{Operação Não Autorizada.}> Local Exception: " + this.getClass().getName() + ".executar()");

				request.setAttribute("Operacao", "77");
				request.setAttribute("ListaAdvogados", listaAdvogados);
				request.setAttribute("ListaResponsaveis", listaResponsaveis);
				stAcao = "/WEB-INF/jsptjgo/ResponsaveisWebserviceXml.jsp";
				break;
			case Configuracao.Curinga9: //RETORNA CODIGO DE VALIDAÇÃO DE LOGON em duas etapas
				UsuarioFoneDt usuFone = new UsuarioFoneDt();
				usuFone.setUsuario(request.getParameter("usuario"));
				usuFone.setFone(request.getParameter("numero"));
				usuFone.setImei(request.getParameter("imei"));
				UsuarioFoneNe ufNe = new UsuarioFoneNe();
				usuFone.setIpComputadorLog(getIpCliente(request));
				String resultado = ufNe.pegarCodigoCadastrar(usuFone);
				response.getWriter().print(resultado);
				response.flushBuffer();
				return;
			default:
				request.setAttribute("Mensagem", "Operação não definida");
				request.setAttribute("RespostaTipo", "ERRO");
				request.setAttribute("Operacao", "-1");
		
			}
		} catch(Exception e) {
			request.setAttribute("RespostaTipo", "ERRO");
			request.setAttribute("Mensagem", e.getMessage());
			request.setAttribute("Operacao", stOperacao);
			stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
		}
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
		return;
		
	}
	
	//este método deve ser sobrescrito pelos ct_publicos retornando o id do grupo publico
    protected String getId_GrupoPublico() {		
    	return GrupoDt.ID_GRUPO_PUBLICO;
	}
    
}
