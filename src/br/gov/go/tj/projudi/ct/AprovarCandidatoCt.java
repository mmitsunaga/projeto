package br.gov.go.tj.projudi.ct;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioCejuscArquivoDt;
import br.gov.go.tj.projudi.dt.UsuarioCejuscDt;
import br.gov.go.tj.projudi.ne.AprovarCandidatoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class AprovarCandidatoCt extends Controle {

	private static final long serialVersionUID = -1971993100103733672L;
	
	private static final String PAGINA_APROVAR_CANDIDATO 	= "/WEB-INF/jsptjgo/AprovarCandidato.jsp";
	private static final String PAGINA_PADRAO_LOCALIZAR 	= "/WEB-INF/jsptjgo/AprovarCandidatoLocalizar.jsp";
	
	private static final String NOME_CONTROLE_WEB_XML = AprovarCandidatoNe.NOME_CONTROLE_WEB_XML;
	
	private AprovarCandidatoNe aprovarCandidatoNe = new AprovarCandidatoNe();

	@Override
	public int Permissao() {
		return UsuarioCejuscDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe usuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException {
		String stAcao;
		int passoEditar 	= -1;
		String stId;
		
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		String stNomeBusca3 = "";
		String stNomeBusca4 = "";
		String stNomeBusca5 = "";

		stAcao = PAGINA_APROVAR_CANDIDATO;
		
		//********************************************
		// Variáveis utilizadas pela página jsp
		
		//********************************************
		//Variáveis objetos
		UsuarioCejuscDt usuarioCejuscDt;
		ServentiaDt serventiaDt;
		
		String mensagemErro = "MensagemErro";
		String parameterPassoEditar = "PassoEditar";
		
		//********************************************
		//Variáveis de sessão
		
		//ATTRIBUTES
		request.setAttribute("tempPrograma" 			, "Aprovar Candidato");
		request.setAttribute("tempRetorno" 				, NOME_CONTROLE_WEB_XML);
		request.setAttribute("PaginaAtual" 				, posicaopaginaatual);
		request.setAttribute("PosicaoPaginaAtual" 		, Funcoes.StringToLong(posicaopaginaatual));
		request.setAttribute("MensagemOk", "");
		request.setAttribute(mensagemErro, "");
		request.setAttribute("MensagemAlerta", "");
		
		//********************************************
		//Requests 
		if(request.getParameter("nomeBusca1") != null) 
			stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) 
			stNomeBusca2 = request.getParameter("nomeBusca2");
		if(request.getParameter("nomeBusca3") != null) 
			stNomeBusca3 = request.getParameter("nomeBusca3");
		if(request.getParameter("nomeBusca4") != null) 
			stNomeBusca4 = request.getParameter("nomeBusca4");
		if(request.getParameter("nomeBusca5") != null) 
			stNomeBusca5 = request.getParameter("nomeBusca5");
		
		if( (request.getParameter(parameterPassoEditar) != null) && !("null".equals(request.getParameter(parameterPassoEditar))) ) {
			passoEditar = Funcoes.StringToInt(request.getParameter(parameterPassoEditar));
		}
		
		switch(paginaatual) {
		
			//pagina inicial do controle que acessa a página para localizar os Candidatos
			case Configuracao.Novo: {
				
				if( request.getParameter(mensagemErro) != null && request.getParameter(mensagemErro).toString().length() > 0 ) {
					request.setAttribute(mensagemErro, request.getParameter(mensagemErro));
				}
				else {
					request.getSession().removeAttribute("usuarioCejuscDt");
				}
				
				stAcao = PAGINA_APROVAR_CANDIDATO;
				
				break;
			}
			
			//consulta pelos filtros
			case Configuracao.Localizar: {
				
				request.getSession().removeAttribute("usuarioCejuscDt");
				
				if (request.getParameter("Passo")==null){
					
					request.getSession().removeAttribute("serventiaDt");
					String idServ = request.getParameter("Id_Serventia");
					if( idServ != null && !idServ.equals("") ) {
						serventiaDt = aprovarCandidatoNe.consultarServentiaId(idServ);
						request.getSession().setAttribute("serventiaDt", serventiaDt);
					}
					
					String[] lisNomeBusca = {"Nome", "CPF"};										
					String[] lisDescricao = {"Nome", "CPF", "STATUS", "VOLUNTARIO"};
					String[] camposHidden = {"nomeBusca4"};
					stAcao = PAGINA_PADRAO_LOCALIZAR;
					request.setAttribute("camposHidden",camposHidden);
					request.setAttribute("tempBuscaId", "idUsuario");
					request.setAttribute("tempBuscaDescricao", "usuarioCejusc");
					request.setAttribute("tempBuscaPrograma", NOME_CONTROLE_WEB_XML);
					request.setAttribute("tempRetorno", NOME_CONTROLE_WEB_XML);
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					
					String stTemp = aprovarCandidatoNe.consultarListaUsuarioCejuscDtJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3, stNomeBusca4, stNomeBusca5, posicaopaginaatual);
					
						
						enviarJSON(response, stTemp);
						
					
					return;
										
				}
				
				break;
			}
			
			//Case para abrir arquivo
			case Configuracao.Curinga6 : {
				
				String idArquivo = request.getParameter("id");
				if( idArquivo != null && idArquivo.length() > 0 ) {
					UsuarioCejuscArquivoDt usuarioCejuscArquivoDt = aprovarCandidatoNe.consultarUsuarioCejuscArquivoDtId(idArquivo);
					
					if( usuarioCejuscArquivoDt != null ) {												
						enviarPDF(response, usuarioCejuscArquivoDt.getBytes(),"documento");																						
						return;
					}
				}
				
				break;
			}
			
			//Salvar como Aprovado(PassoEditar = Curinga6), Pendente(PassoEditar = Curinga7), Reprovado(PassoEditar = Curinga8)
			case Configuracao.Salvar : {
				
				stAcao = PAGINA_APROVAR_CANDIDATO;
				validarOpcaoEscolhida(request, usuarioSessao, passoEditar);
				
				break;
			}
			
			case Configuracao.SalvarResultado : {
				
				String observacaoStatus = request.getParameter("editor");
				
				if( observacaoStatus != null && observacaoStatus.trim().length() > 0 ) {
					
					usuarioCejuscDt = (UsuarioCejuscDt) request.getSession().getAttribute("usuarioCejuscDt");
					
					usuarioCejuscDt.setCodigoStatusAnterior( usuarioCejuscDt.getCodigoStatusAtual() );
					
					String codigoStatusAtual = null;
					
					if( usuarioCejuscDt != null ) {
						switch(passoEditar) {
							//Aprovado
							case Configuracao.Curinga6 : {
								codigoStatusAtual = UsuarioCejuscDt.CODIGO_STATUS_APROVADO;
								break;
							}
							//Pendente
							case Configuracao.Curinga7 : {
								codigoStatusAtual = UsuarioCejuscDt.CODIGO_STATUS_PENDENTE;
								break;
							}
							//Reprovado
							case Configuracao.Curinga8 : {
								codigoStatusAtual = UsuarioCejuscDt.CODIGO_STATUS_REPROVADO;
								break;
							}
						}
						
						//novos dados
						usuarioCejuscDt.setCodigoStatusAtual(codigoStatusAtual);
						usuarioCejuscDt.setObservacaoStatus(observacaoStatus);
						
						//Log
						usuarioCejuscDt.setId_UsuarioLog(usuarioSessao.getId_Usuario());
						usuarioCejuscDt.setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());
						
						//aprovarCandidatoNe.alterarStatusCejusc(usuarioCejuscDt);
						//aprovarCandidatoNe.alterarStatusCejusc(idUsuCejusc, novoStatus, observacaoStatus);
						aprovarCandidatoNe.alterarStatusCejusc(usuarioCejuscDt.getId(), codigoStatusAtual, observacaoStatus, usuarioSessao.getId_Usuario());
						
						request.setAttribute("MensagemOk", "Usuário Candidato Atualizado com Sucesso!");
					}
				}
				else {
					redireciona(response, NOME_CONTROLE_WEB_XML + "?PaginaAtual=" + Configuracao.Novo + "&"+ mensagemErro +"=Preencha a Observação do Status Escolhido!");
				}
				
				break;
			}
			
			
			
			
			
			
			
			
			case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Serventia"};
					String[] lisDescricao = {"Serventia", "UF"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Serventia");
					request.setAttribute("tempBuscaDescricao","Serv");
					request.setAttribute("tempBuscaPrograma","Serventia");
					request.setAttribute("tempRetorno","AprovarCandidato");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Localizar);
					request.setAttribute("PaginaAtual", (ServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {						
					String stTemp = aprovarCandidatoNe.consultarDescricaoServentiaJSON(stNomeBusca1, posicaopaginaatual);
					
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
			
			break;
			
			
			
			
			
			
			
			default : {
				
				if( request.getParameter(mensagemErro) != null ) {
					request.setAttribute(mensagemErro,request.getParameter(mensagemErro));
				}
				
				//Busca UsuarioCejusc
				stId = request.getParameter("idUsuario");
				if( stId != null ) {
					stAcao = PAGINA_APROVAR_CANDIDATO;
					
					usuarioCejuscDt = aprovarCandidatoNe.consultaUsuarioCejuscDtId(stId);
					request.getSession().setAttribute("usuarioCejuscDt", usuarioCejuscDt);
				}
				
				break;
			}
			
		}
		
		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	protected void validarOpcaoEscolhida(HttpServletRequest request, UsuarioNe usuarioSessao, int passoEditar) {
		String labelButton;
		
		UsuarioCejuscDt usuarioCejuscDt = (UsuarioCejuscDt) request.getSession().getAttribute("usuarioCejuscDt");
		
		if( usuarioCejuscDt != null && usuarioCejuscDt.getId() != null ) {
			switch(passoEditar) {
				//Aprovado
				case Configuracao.Curinga6 : {
					labelButton = "Confirmar Aprovação";
					break;
				}
				
				//Pendente
				case Configuracao.Curinga7 : {
					labelButton = "Confirmar Pendência";
					break;
				}
				
				//Reprovado
				case Configuracao.Curinga8 : {
					labelButton = "Confirmar Reprovação";
					break;
				}
				
				default : {
					labelButton = "";
					break;
				}
			}
			
			request.setAttribute("__Pedido__", usuarioSessao.getPedido());
			request.setAttribute("labelButton", labelButton);
			request.setAttribute("Curinga", passoEditar);
			request.setAttribute("Mensagem", "Clique para confirmar a operação");
		}
		else {
			request.setAttribute("MensagemErro", "Por favor, escolha um candidato primeiramente.");
		}
	}
	
}