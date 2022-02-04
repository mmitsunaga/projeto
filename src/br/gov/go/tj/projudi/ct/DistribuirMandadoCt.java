package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.MandadoJudicialDt;
import br.gov.go.tj.projudi.dt.MandadoJudicialStatusDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoEscalaDt;
import br.gov.go.tj.projudi.ne.DistribuirMandadoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class DistribuirMandadoCt extends Controle {

	/**
	 * Propriedades
	 */
	private static final long serialVersionUID = 3431356199966568991L;
	
	private static final String PAGINA_MANDADO_JUDICIAL_DISTRIBUICAO 		= "/WEB-INF/jsptjgo/MandadoJudicialDistribuicao.jsp";
	private static final String PAGINA_MANDADO_JUDICIAL_CONSULTAR 			= "/WEB-INF/jsptjgo/MandadoJudicialConsultar.jsp";
	private static final String PAGINA_LOCALIZAR_USUARIO_SERVENTIA_ESCALA 	= "/WEB-INF/jsptjgo/ServentiaCargoEscalaLocalizar.jsp";
	private static final String PAGINA_MANDADO_LOCALIZAR 					= "/WEB-INF/jsptjgo/MandadoLocalizar.jsp";

	public int Permissao() {
		return MandadoJudicialDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {
    	
		DistribuirMandadoNe distribuirMandadoNe = new DistribuirMandadoNe();
		
		String codigoMandado = "";
		
		String mensagem = "";
		String tipoMensagem = "";
		
		String stAcao = PAGINA_MANDADO_LOCALIZAR;
		
		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descri��o das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma vari�vel para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		
		request.setAttribute("tempPrograma", 						"Mandado Judicial");
		request.setAttribute("tempRetorno", 						"DistribuirMandado");
		request.setAttribute("tempTituloFormulario", 				"Busca de Mandado Judicial");
		request.setAttribute("tempBuscaMandadoJudicial", 			"MandadoJudicial");
		request.setAttribute("tempBuscaId_ServentiaCargoEscala", 	"tempBuscaId_ServentiaCargoEscala");
		request.setAttribute("tempBuscaServentiaCargoEscala", 	"tempBuscaServentiaCargoEscala");
		String stId = request.getParameter("tempBuscaId_ServentiaCargoEscala");
		if( stId != null ) {
			String ServentiaCargoEscala = request.getParameter("tempBuscaServentiaCargoEscala");
			
			if( ServentiaCargoEscala == null )
				ServentiaCargoEscala = "";
			
			request.setAttribute("tempBuscaId_ServentiaCargoEscala", stId);
			request.setAttribute("tempBuscaServentiaCargoEscala", ServentiaCargoEscala);
		}
		else {
			request.setAttribute("tempBuscaId_ServentiaCargoEscala", 	"");
			request.setAttribute("tempBuscaServentiaCargoEscala", 	"");
		}
		
		request.setAttribute("PaginaAtual", paginaatual);
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		
		if( request.getParameter("codigoMandado") != null ) {
			codigoMandado = request.getParameter("codigoMandado");
		}
		else {
			codigoMandado = "";
		}
		request.setAttribute("codigoMandado", codigoMandado);
		
		
		switch(paginaatual) {
			//Abre formul�rio para consultar de pend�ncia em mandado judicial
			case Configuracao.Curinga6 : {
				request.setAttribute("PaginaAtual", Configuracao.Curinga6);
				request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
				
				break;
			}
			
			//Localizar Mandado Judicial pelo Id
			case Configuracao.Localizar : {
				request.setAttribute("PaginaAtual", Configuracao.Localizar);
				
				//Vari�veis para Consultas
				MandadoJudicialDt mandadoJudicialDt = null;
				PendenciaDt pendenciaDt 			= null;
				ProcessoParteDt processoParteDt 	= null;
				ProcessoDt processoDt 				= null;
				
				List listaTiposMandados 			= null;
				List listaStatusMandadoJudicial 	= null;
				
				//*********************************
				//Consultas
				mandadoJudicialDt = distribuirMandadoNe.consultarPorIdPendencia(codigoMandado);
				
				//Consulta Pendencia(PendenciaDt)
				pendenciaDt = distribuirMandadoNe.consultarPendenciaTipoMandado(Funcoes.StringToInt(codigoMandado));
				
				if( pendenciaDt != null ) {
					//Consultar Processo Parte com o seu endereco completo
					processoParteDt = distribuirMandadoNe.consultarProcessoParteId( pendenciaDt.getId_ProcessoParte() );
					
					//Consultar dados completo do Processo, e retira somente os dados necess�rio para n�o manter os dados do processo inteiro.
					ProcessoDt processoDt_aux = distribuirMandadoNe.consultarProcessoId(pendenciaDt.getId_Processo());
					processoDt = new ProcessoDt();
					processoDt.setId( processoDt_aux.getId() );
					processoDt.setValor( processoDt_aux.getValor() );
					processoDt.setProcessoTipo( processoDt_aux.getProcessoTipo() );
					
					//Consulta lista de tipos de mandados
					listaTiposMandados = distribuirMandadoNe.consultarListaTiposMandados();
					
					request.getSession().setAttribute("processoDt", processoDt);
					request.getSession().setAttribute("pendenciaDt", pendenciaDt);
					request.getSession().setAttribute("processoParteDt", processoParteDt);
					request.setAttribute("listaTiposMandados", listaTiposMandados);
					request.setAttribute("prazoMandadoJudicial", "");
				}
				
				//Consulta lista de status de mandado
				List listaMandadoStatusExcluidos = new ArrayList();
				listaMandadoStatusExcluidos.add(MandadoJudicialStatusDt.DISTRIBUIDO);
				listaStatusMandadoJudicial = distribuirMandadoNe.consultarListaMandadoStatus(listaMandadoStatusExcluidos);
				
				
				//*********************************
				//L�gica para adicionar os dados na requisi��o e/ou sess�o
				if( mandadoJudicialDt != null ) {
					
					/**
					 * Mandado j� est� cadastrado!
					 */
					//Coloca os dados necess�rios no request
					request.setAttribute("tempRetorno","DistribuirMandado");
					request.setAttribute("listaStatusMandadoJudicial", listaStatusMandadoJudicial);
					
					//Coloca o mandadoJudicialDt na sess�o
					request.getSession().setAttribute("mandadoJudicialDt", mandadoJudicialDt);
					
					stAcao = PAGINA_MANDADO_JUDICIAL_CONSULTAR;
				}
				else {
					if( pendenciaDt != null ) {
						//Encaminha para a p�gina que ser� confirmado a distribui��o
						stAcao = PAGINA_MANDADO_JUDICIAL_DISTRIBUICAO;
					}
					else {
						//N�o foi encontrado a pendencia
						request.setAttribute("PaginaAtual", Configuracao.Curinga6);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						
						mensagem = "C�digo mandado n�o encontrado!";
						request.setAttribute("MensagemErro", mensagem);
					}
				}
				
				break;
			}
			
			//Distribuir Mandado Judicial
			case Configuracao.Curinga8 : {
				
				//String prazo = request.getParameter("prazoMandadoJudicial");
				PendenciaDt pendenciaDt 			= (PendenciaDt)request.getSession().getAttribute("pendenciaDt");
				ProcessoParteDt processoParteDt 	= (ProcessoParteDt)request.getSession().getAttribute("processoParteDt");
				ProcessoDt processoDt 				= (ProcessoDt)request.getSession().getAttribute("processoDt");
				
				String oficialCompanheiro 	= request.getParameter("oficialCompanheiro");
				String idMandadoTipo 		= request.getParameter("idMandadoTipo");
				String prazoMandadoJudicial = request.getParameter("prazoMandadoJudicial");
				Integer assistencia = null;
				if( request.getParameter("assistenciaRadio") != null )
					assistencia = Funcoes.StringToInt(request.getParameter("assistenciaRadio"));
				
				LogDt logDt = new LogDt(UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog());
				
				int retornoDistribuicao = distribuirMandadoNe.distribuirMandado(pendenciaDt, processoParteDt, processoDt, logDt, idMandadoTipo, prazoMandadoJudicial, oficialCompanheiro, assistencia);
				
				switch(retornoDistribuicao) {
					case DistribuirMandadoNe.SUCESSO : {
						mensagem = "Mandado judicial distribu�do com sucesso!";
						tipoMensagem = "MensagemOk";
						break;
					}
					case DistribuirMandadoNe.ERRO :
					case DistribuirMandadoNe.SEM_SUCESSO : {
						mensagem = "Mandado judicial n�o foi distribu�do!";
						tipoMensagem = "MensagemErro";
						break;
					}
					case DistribuirMandadoNe.JA_EXISTE : {
						mensagem = "Mandado judicial n�mero " + codigoMandado + " j� foi distribu�do!";
						tipoMensagem = "MensagemErro";
						break;
					}
					case DistribuirMandadoNe.NAO_ENCONTRADO_ESCALA : {
						mensagem = "N�o foi encontrado escala para distribuir Mandado Judicial!";
						tipoMensagem = "MensagemErro";
						break;
					}
					case DistribuirMandadoNe.NAO_TEM_GUIA : {
						mensagem = "N�o possui Guia OU aguardando o pagamento da Guia!";
						tipoMensagem = "MensagemErro";
						break;
					}
					case DistribuirMandadoNe.GUIAS_SEM_LOCOMOCAO_LIVRE : {
						mensagem = "Guias n�o possui itens de locomo��o N�O utilizada ou livre.";
						tipoMensagem = "MensagemErro";
						break;
					}
					case DistribuirMandadoNe.ENDERECO_LOCOMOCAO_DIFERENTE : {
						mensagem = "Itens de locomo��o encontrado nas Guias n�o � igual ao endere�o do Mandado Judicial!";
						tipoMensagem = "MensagemErro";
						break;
					}
				}
				request.setAttribute(tipoMensagem,mensagem);
				
				request.getSession().removeAttribute("processoDt");
				request.getSession().removeAttribute("pendenciaDt");
				request.getSession().removeAttribute("processoParteDt");
				
				request.setAttribute("PaginaAtual", Configuracao.Curinga8);
				request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
				
				break;
			}
			
			//Cancelar a distribui��o do Mandado Judicial
			case Configuracao.Curinga7 : {
				request.setAttribute("PaginaAtual", Configuracao.Curinga6);
				request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
				
				request.getSession().removeAttribute("processoDt");
				request.getSession().removeAttribute("pendenciaDt");
				request.getSession().removeAttribute("processoParteDt");
				
				mensagem = "Distribui��o de Mandado Judicial Cancelado!";
				request.setAttribute("MensagemOk", mensagem);
				
				break;
			}
			
			//Alterar Status do Mandado Judicial
			case Configuracao.Curinga9 : {
				
				Integer novoStatus 					= Funcoes.StringToInt(request.getParameter("novoStatus"));
				String locomocoesFrutiferas 		= request.getParameter("idLocomocaoFrutiferas");
				String locomocoesInfrutiferas 		= request.getParameter("idLocomocaoInfrutiferas");
				String locomocaoHoraMarcada 		= request.getParameter("idLocomocaoHoraMarcada");
				
				//Obtem mandado judicial da sess�o
				MandadoJudicialDt mandadoJudicialDt = (MandadoJudicialDt)request.getSession().getAttribute("mandadoJudicialDt");
				
				//Encapsula valores e dados das locomo��es
				mandadoJudicialDt.setLocomocoesFrutiferas(locomocoesFrutiferas);
				mandadoJudicialDt.setLocomocoesInfrutiferas(locomocoesInfrutiferas);
				mandadoJudicialDt.setLocomocaoHoraMarcada(locomocaoHoraMarcada);
				
				//Altera Status
				boolean sucessoAlteracao = distribuirMandadoNe.alterarStatusMandadoJudicial(mandadoJudicialDt, novoStatus);
				
				if( sucessoAlteracao )
					mensagem = "Status do Mandado Judicial Alterado com Sucesso!";
				else
					mensagem = "Status do Mandado Judicial N�o foi Alterado. Favor verificar!";
				
				//Remove dados da Sess�o que n�o ser�o necess�rios ap�s este passo
				request.getSession().removeAttribute("processoDt");
				request.getSession().removeAttribute("pendenciaDt");
				request.getSession().removeAttribute("processoParteDt");
				request.setAttribute("MensagemOk", mensagem);
				
				//Volta para a tela de consulta
				request.setAttribute("PaginaAtual", Configuracao.Curinga8);
				request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
				
				break;
			}
			
			case ServentiaCargoEscalaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar : {
				
				stAcao = PAGINA_LOCALIZAR_USUARIO_SERVENTIA_ESCALA;
				request.setAttribute("tempBuscaId_ServentiaCargoEscala","tempBuscaId_ServentiaCargoEscala");
				request.setAttribute("tempBuscaServentiaCargoEscala","tempBuscaServentiaCargoEscala");
				request.setAttribute("tempRetorno","DistribuirMandado");
				List tempList = distribuirMandadoNe.consultarDescricaoServentiaCargoEscalaLocalizar(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0) {
					request.setAttribute("ListaServentiaCargoEscala", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", distribuirMandadoNe.getQuantidadePaginasServentiaCargoEscala());
				} else {
					request.setAttribute("MensagemErro", "Dados N�o Localizados"); 
				}
				
				break;
			}
			
			default : {
				//Busca ServentiaCargoEscala
				stId = request.getParameter("tempBuscaId_ServentiaCargoEscala");
				if( stId != null ) {
					stAcao = PAGINA_MANDADO_JUDICIAL_DISTRIBUICAO;
					
					request.setAttribute("listaTiposMandados", distribuirMandadoNe.consultarListaTiposMandados());
					if( request.getParameter("prazoMandadoJudicial") == null )
						request.setAttribute("prazoMandadoJudicial", "");
				}
			}
		}
		
		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
    }
}
