package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AssuntoDt;
import br.gov.go.tj.projudi.dt.TemaAssuntoDt;
import br.gov.go.tj.projudi.dt.TemaDt;
import br.gov.go.tj.projudi.ne.TemaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.ValidacaoUtil;

public class TemaCt extends TemaCtGen{

	private static final long serialVersionUID = 7061542784952178084L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		TemaDt Temadt;
		TemaNe Temane;
		String stNomeBusca1="";
		String stNomeBusca2 = "";
		String stNomeBusca3 = "";
		String stNomeBusca4 = "";
		String posicaoLista = "";
		String Mensagem="";
		int paginaAnterior = 0;
		int passoEditar = 0;
		String stAcao="/WEB-INF/jsptjgo/Tema.jsp";
		request.setAttribute("tempPrograma","Tema");

		Temane =(TemaNe)request.getSession().getAttribute("Temane");
		if (Temane == null )  Temane = new TemaNe();  

		Temadt =(TemaDt)request.getSession().getAttribute("Temadt");
		if (Temadt == null )  Temadt = new TemaDt();  
		passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));

		Temadt.setTitulo( request.getParameter("Titulo")); 
		Temadt.setTemaCodigo( request.getParameter("TemaCodigo")); 
		Temadt.setQuesDireito( request.getParameter("QuesDireito")); 
		Temadt.setVinculantes( request.getParameter("Vinculantes")); 
		Temadt.setId_TemaSituacao( request.getParameter("Id_TemaSituacao")); 
		Temadt.setId_TemaOrigem( request.getParameter("Id_TemaOrigem")); 
		Temadt.setId_TemaTipo( request.getParameter("Id_TemaTipo")); 
		Temadt.setTemaSituacao( request.getParameter("TemaSituacao")); 
		Temadt.setTemaOrigem( request.getParameter("TemaOrigem")); 
		Temadt.setTemaTipo( request.getParameter("TemaTipo"));
		Temadt.setDataTransito( request.getParameter("DataTransito"));
		Temadt.setDataAdmissao(request.getParameter("DataAdmissao"));
		Temadt.setTeseFirmada(request.getParameter("TeseFirmada"));
		Temadt.setInfoLegislativa(request.getParameter("InfoLegislativa"));
		Temadt.setNumeroIrdrCnj(request.getParameter("NumeroIRDR"));
		Temadt.setSuspensao(request.getParameter("Suspensao"));
		Temadt.setOpcaoProcessual(request.getParameter("OpcaoProcessual"));
		Temadt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Temadt.setIpComputadorLog(request.getRemoteAddr());
		
		if (paginaatual == Configuracao.Salvar){			
			Temadt.setOpcaoProcessual(request.getParameter("OpcaoProcessual") == null ? "" : request.getParameter("OpcaoProcessual"));
		}
		
		if (request.getParameter("PaginaAnterior") != null) paginaAnterior = Funcoes.StringToInt(request.getParameter("PaginaAnterior"));
		
		//Quando um assunto é selecionado já insere na lista de assuntos
		if (request.getParameter("Id_Assunto") != null && request.getParameter("Id_Assunto").length() > 0 && paginaAnterior == (AssuntoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)) {
			this.adicionarAssuntoTema(Temadt, request);
		}
		posicaoLista = request.getParameter("posicaoLista");
		
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		if(request.getParameter("nomeBusca3") != null) stNomeBusca3 = request.getParameter("nomeBusca3");
		if(request.getParameter("nomeBusca4") != null) stNomeBusca4 = request.getParameter("nomeBusca4");
		request.setAttribute("PassoEditar",passoEditar);
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual",Configuracao.Editar);
		
		switch (paginaatual) {
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Tema", "Origem", "Situação", "Código"};
					String[] lisDescricao = {"Tema", "Tipo", "Origem", "Situação"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Tema");
					request.setAttribute("tempBuscaDescricao","Tema");
					request.setAttribute("tempBuscaPrograma","Tema");
					request.setAttribute("tempRetorno","Tema");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = Temane.consultarDescricaoJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3, stNomeBusca4, PosicaoPaginaAtual);
					enviarJSON(response, stTemp);
					return;
				}
				break;
			case Configuracao.SalvarResultado:
				Mensagem=Temane.Verificar(Temadt); 
				if (Mensagem.length()==0){
					Temane.salvar(Temadt);
					Temadt = Temane.consultarId(Temadt.getId());
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso");
				} else	{
					request.setAttribute("MensagemErro", Mensagem );
				}				
				break;
				
			case Configuracao.ExcluirResultado: //Excluir
				if(passoEditar == 2) {//exclui assunto
					posicaoLista = (String) request.getSession().getAttribute("posicaoLista");
					TemaAssuntoDt temaAssunto = Temadt.getItemListaAssuntos(posicaoLista);
					temaAssunto.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
					temaAssunto.setIpComputadorLog(request.getRemoteAddr());
					Temane.excluirAssunto(temaAssunto);
					Temadt = Temane.consultarId(Temadt.getId());
					request.getSession().removeAttribute("posicaoLista");
					request.setAttribute("PassoEditar", "0");
					request.setAttribute("MensagemOk", "Assunto Excluido com Sucesso");
				} else {//exclui tema
					Temane.excluir(Temadt); 
					request.setAttribute("MensagemOk", "Tema Excluido com Sucesso");
				}
				break;
				
			// Consulta Assuntos
			case (AssuntoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Assunto"};
					String[] lisDescricao = {"Assunto","Pai","Disp. Legal"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Assunto");
					request.setAttribute("tempBuscaDescricao","Assunto");
					request.setAttribute("tempBuscaPrograma","Assunto");			
					request.setAttribute("tempRetorno","Tema");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (AssuntoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					break;
				} else {
					String stTemp = "";
					stTemp = Temane.consultarDescricaoAssuntoJSON(stNomeBusca1, PosicaoPaginaAtual);
					enviarJSON(response, stTemp);
					return;								
				}	
			case Configuracao.Excluir:
				request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
				request.getSession().setAttribute("posicaoLista", posicaoLista);
				break;
									
			case Configuracao.Curinga7:
				// Envia o tema do tipo IRDR para o CNJ, através do webservice.
				// Como retorno, obtém o número NUT gerado e salva na tabela TEMA
				try {
					Temane.BnprEnviarNovoPrecedenteTJGO(Temadt);
					request.setAttribute("MensagemOk", "Dados enviados com sucesso");
				} catch (Exception ex){
					request.setAttribute("MensagemErro", ex.getMessage());
				}				
				break;
				
			default:
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
		}
		
		request.setAttribute("isPodeEnviarTemaParaCNJ", isPodeEnviarTemaTJGO(Temadt));
		
		request.getSession().setAttribute("Temadt",Temadt );
		request.getSession().setAttribute("Temane",Temane );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
	/**
	 * Verifica se o tema já está cadastrado (tem Id) e é do tipo "IRDR" ou "IAC"
	 * @param temaDt
	 * @return
	 */
	private boolean isPodeEnviarTemaTJGO(TemaDt temaDt){
		return ValidacaoUtil.isNaoNulo(temaDt) && 
				ValidacaoUtil.isNaoVazio(temaDt.getId()) &&				
				(temaDt.getTemaTipoCnj().equals("IRDR") || temaDt.getTemaTipoCnj().equals("IAC"));
	}
	
	/**
	 * Método responsável em adicionar assuntos a um tema
	 */
	private void adicionarAssuntoTema(TemaDt temaDt, HttpServletRequest request) {
		if (request.getParameter("Id_Assunto") != null && request.getParameter("Id_Assunto").length() > 0) {
			TemaAssuntoDt temaAssuntoDt = new TemaAssuntoDt();
			temaAssuntoDt.setId_Assunto(request.getParameter("Id_Assunto"));
			temaAssuntoDt.setAssunto(request.getParameter("Assunto"));
			temaDt.adicionarAssunto(temaAssuntoDt);
		} else request.setAttribute("MensagemErro", "Selecione um Assunto para ser adicionado.");
	}
	
}
