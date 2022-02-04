package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.MovimentacaoTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioAnaliticoDt;
import br.gov.go.tj.projudi.ne.RelatorioEstatisticaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class RelatorioAnaliticoProdutividadeCt extends Controle {

	private static final long serialVersionUID = 58752094293520253L;

	public int Permissao() {
		return RelatorioAnaliticoDt.CodigoPermissaoAnaliticoProdutividade;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String posicaoPaginaAtual) throws Exception, ServletException, IOException {

		RelatorioAnaliticoDt relatorioAnaliticoDt;
		RelatorioEstatisticaNe relatorioEstatisticaNe;
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		List tempList = null;
		String mensagemRetorno = "";
		String stAcao = "";
		
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		relatorioEstatisticaNe = (RelatorioEstatisticaNe) request.getSession().getAttribute("RelatorioEstatisticane");
		if (relatorioEstatisticaNe == null)
			relatorioEstatisticaNe = new RelatorioEstatisticaNe();

		relatorioAnaliticoDt = (RelatorioAnaliticoDt) request.getSession().getAttribute("RelatorioAnaliticodt");
		if (relatorioAnaliticoDt == null)
			relatorioAnaliticoDt = new RelatorioAnaliticoDt();
		
		if (request.getParameter("Mes") != null && !request.getParameter("Mes").equals(""))
			relatorioAnaliticoDt.setMes(request.getParameter("Mes"));
		
		if (request.getParameter("Ano") != null && !request.getParameter("Ano").equals("")) 
			relatorioAnaliticoDt.setAno(request.getParameter("Ano"));
		
		if (request.getParameter("Id_Serventia") != null && !request.getParameter("Id_Serventia").equals(""))
			relatorioAnaliticoDt.setId_Serventia(request.getParameter("Id_Serventia"));

		if (request.getParameter("Serventia") != null && !request.getParameter("Serventia").equals(""))
			relatorioAnaliticoDt.setServentia(request.getParameter("Serventia"));
		
		if (request.getParameter("Id_Usuario") != null && !request.getParameter("Id_Usuario").equals(""))
			relatorioAnaliticoDt.getUsuario().setId(request.getParameter("Id_Usuario"));
		
		if (request.getParameter("Usuario") != null && !request.getParameter("Usuario").equals(""))
			relatorioAnaliticoDt.getUsuario().setNome(request.getParameter("Usuario"));
		
		if (request.getParameter("Comarca") != null && !request.getParameter("Comarca").equals(""))
			relatorioAnaliticoDt.setComarca(request.getParameter("Comarca"));

		if (request.getParameter("Id_Comarca") != null && !request.getParameter("Comarca").equals(""))
			relatorioAnaliticoDt.setId_Comarca(request.getParameter("Id_Comarca"));

		if (request.getParameter("MovimentacaoTipo") != null && !request.getParameter("MovimentacaoTipo").equals(""))
			relatorioAnaliticoDt.setMovimentacaoTipo(request.getParameter("MovimentacaoTipo"));

		if (request.getParameter("Id_MovimentacaoTipo") != null && !request.getParameter("Id_MovimentacaoTipo").equals(""))
			relatorioAnaliticoDt.setId_MovimentacaoTipo(request.getParameter("Id_MovimentacaoTipo"));

		request.setAttribute("tempRetorno", "RelatorioAnaliticoProdutividade");
		request.setAttribute("tempBuscaSistema", "Sistema");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("Curinga", "vazio");
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("tempPrograma", "Relatório Analítico Produtividade");

		switch (paginaatual) {
			case Configuracao.Localizar:
	
				mensagemRetorno = "";
				boolean acessoEspecial = false;
				if(request.getSession().getAttribute("acessoEspecial") != null) {
					acessoEspecial = new Boolean(request.getSession().getAttribute("acessoEspecial").toString());
				}
	
				//Validações para o usuário com acesso especial
				if(acessoEspecial){
					if(relatorioAnaliticoDt.getId_Comarca() == null || relatorioAnaliticoDt.getId_Comarca().equalsIgnoreCase("") 
							|| relatorioAnaliticoDt.getId_Serventia() == null || relatorioAnaliticoDt.getId_Serventia().equalsIgnoreCase("")){
						mensagemRetorno = "Comarca e Serventia devem ser informados.";
					}
					// Se informar o ID do servidor/usuário, deverá ser informada também a Serventia.
					if (relatorioAnaliticoDt.getUsuario().getId() != null && !relatorioAnaliticoDt.getUsuario().getId().equalsIgnoreCase("")) {
						if (relatorioAnaliticoDt.getId_Serventia() == null || relatorioAnaliticoDt.getId_Serventia().equalsIgnoreCase("")) {
							mensagemRetorno = "Ao informar o Servidor Judiciário, Serventia deve ser informada.";
						}
					}
				}
				
				//Validações para usuário com acesso comum
				if(!acessoEspecial) {
					//o usuário comum é obrigado a informar servidor judiciário ou estatística de produtividade
					if (relatorioAnaliticoDt.getUsuario().getId() == null || relatorioAnaliticoDt.getUsuario().getId().equalsIgnoreCase("")) {
						if (relatorioAnaliticoDt.getId_MovimentacaoTipo() == null || relatorioAnaliticoDt.getId_MovimentacaoTipo().equalsIgnoreCase("")) {
							mensagemRetorno = "Servidor Judiciário ou Estatística de Produtividade devem ser informados.";
						}
					}
				}
	
				if (mensagemRetorno.equals("")) {
					request.getParameter("tipo_Arquivo");
	
					//Se o usuário tiver acesso especial, ele poderá consultar outras comarcas e serventias
					//se for usuário comum poderá consultar apenas sua própria comarca e serventia
					if(acessoEspecial){
						tempList = relatorioEstatisticaNe.relAnaliticoProdutividade(relatorioAnaliticoDt.getAno(), relatorioAnaliticoDt.getMes(), relatorioAnaliticoDt.getId_Comarca(), relatorioAnaliticoDt.getId_Serventia(), relatorioAnaliticoDt.getUsuario().getId(), relatorioAnaliticoDt.getId_MovimentacaoTipo(), posicaoPaginaAtual);
					} else {
						tempList = relatorioEstatisticaNe.relAnaliticoProdutividade(relatorioAnaliticoDt.getAno(), relatorioAnaliticoDt.getMes(), UsuarioSessao.getUsuarioDt().getId_Comarca(), UsuarioSessao.getUsuarioDt().getId_Serventia(), relatorioAnaliticoDt.getUsuario().getId(), relatorioAnaliticoDt.getId_MovimentacaoTipo(), posicaoPaginaAtual);
					}
					
					if (tempList.size() > 0) {
						request.setAttribute("listaProcessos", tempList);
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", relatorioEstatisticaNe.getQuantidadePaginas());
					} else {
						request.setAttribute("MensagemErro", "Dados Não Localizados");
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					
					
				} else {
					request.setAttribute("MensagemErro", mensagemRetorno);
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
				
				if(request.getSession().getAttribute("stAcaoRetorno") != null && !request.getSession().getAttribute("stAcaoRetorno").equals("")){
					stAcao = request.getSession().getAttribute("stAcaoRetorno").toString();
				}
				break;
				
			case Configuracao.Novo:
				relatorioAnaliticoDt.limparCamposConsulta();
				relatorioAnaliticoDt = this.atribuirDataAtual(relatorioAnaliticoDt);
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				if(request.getSession().getAttribute("stAcaoRetorno") != null && !request.getSession().getAttribute("stAcaoRetorno").equals("")){
					stAcao = request.getSession().getAttribute("stAcaoRetorno").toString();
				}
				break;
				
			case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Serventia"};
					String[] lisDescricao = {"Serventia", "Estado"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Serventia");
					request.setAttribute("tempBuscaDescricao", "Serventia");
					request.setAttribute("tempBuscaPrograma", "Serventia");
					request.setAttribute("tempRetorno", "RelatorioAnaliticoProdutividade");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = relatorioEstatisticaNe.consultarDescricaoServentiaJSON(stNomeBusca1, posicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			case (ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Comarca"};
					String[] lisDescricao = {"Comarca"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Comarca");
					request.setAttribute("tempBuscaDescricao","Comarca");
					request.setAttribute("tempBuscaPrograma","Forum");			
					request.setAttribute("tempRetorno","RelatorioAnaliticoProdutividade");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", String.valueOf(ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = relatorioEstatisticaNe.consultarDescricaoComarcaJSON(stNomeBusca1, posicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}
				break;
				
//			//No case abaixo, se o usuário buscar por Estatística de Produtividade (que é o agrupamento dos MovimentacaoTipo), 
//			//o componente buscará em MovimentacaoTipo pois nos relatórios analíticos não pode ser feito agrupamento dos MovimentacaoTipo.
			case (MovimentacaoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Movimentacao Tipo"};
					String[] lisDescricao = {"Movimentacao Tipo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_MovimentacaoTipo");
					request.setAttribute("tempBuscaDescricao","MovimentacaoTipo");
					request.setAttribute("tempBuscaPrograma","Tipo de Movimentação");			
					request.setAttribute("tempRetorno","RelatorioAnaliticoProdutividade");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", String.valueOf(MovimentacaoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = relatorioEstatisticaNe.consultarDescricaoMovimentacaoTipoJSON(stNomeBusca1, posicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}
				break;
				
			case (UsuarioDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if(request.getSession().getAttribute("acessoEspecial") != null && new Boolean(request.getSession().getAttribute("acessoEspecial").toString())){
					
					if (request.getParameter("Passo")==null){
						String[] lisNomeBusca = {"Nome", "Usuário"};
						String[] lisDescricao = {"Nome", "Usuário", "RG", "CPF"};
						stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
						request.setAttribute("tempBuscaId","Id_Usuario");
						request.setAttribute("tempBuscaDescricao","Usuario");
						request.setAttribute("tempBuscaPrograma","Servidor Judiciário");			
						request.setAttribute("tempRetorno","RelatorioAnaliticoProdutividade");		
						request.setAttribute("tempDescricaoId","Id");
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
						request.setAttribute("PaginaAtual", (UsuarioDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
						request.setAttribute("PosicaoPaginaAtual", "0");
						request.setAttribute("QuantidadePaginas", "0");
						request.setAttribute("lisNomeBusca", lisNomeBusca);
						request.setAttribute("lisDescricao", lisDescricao);
					} else {
						String stTemp="";
						stTemp = relatorioEstatisticaNe.consultarDescricaoServidorJudiciarioJSON(stNomeBusca1, stNomeBusca2, posicaoPaginaAtual);
											
							enviarJSON(response, stTemp);
							
						
						return;								
					}
					
				} else {
						request.setAttribute("Id_Serventia", UsuarioSessao.getUsuarioDt().getId_Serventia());
						relatorioAnaliticoDt.setId_Serventia(UsuarioSessao.getUsuarioDt().getId_Serventia());
						
						if (request.getParameter("Passo")==null){
							String[] lisNomeBusca = {"Nome", "Usuário"};
							String[] lisDescricao = {"Nome", "Usuário", "RG", "CPF"};
							stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
							request.setAttribute("tempBuscaId","Id_Usuario");
							request.setAttribute("tempBuscaDescricao","Usuario");
							request.setAttribute("tempBuscaPrograma","Servidor Judiciário");			
							request.setAttribute("tempRetorno","RelatorioAnaliticoProdutividade");		
							request.setAttribute("tempDescricaoId","Id");
							request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
							request.setAttribute("PaginaAtual", (UsuarioDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
							request.setAttribute("PosicaoPaginaAtual", "0");
							request.setAttribute("QuantidadePaginas", "0");
							request.setAttribute("lisNomeBusca", lisNomeBusca);
							request.setAttribute("lisDescricao", lisDescricao);
						} else {
							String stTemp="";
							
							stTemp = relatorioEstatisticaNe.consultarDescricaoServidorJudiciarioServentiaJSON(stNomeBusca1, stNomeBusca2, posicaoPaginaAtual, UsuarioSessao.getUsuarioDt().getId_Serventia());
								
							enviarJSON(response, stTemp);
								
							
							return;								
						}
				}
				break;
	
			case Configuracao.Curinga6:
				//Curinga de acesso comum
				request.setAttribute("PaginaAtual", Configuracao.Editar);
	
				if (relatorioAnaliticoDt.getMes().equals("") && relatorioAnaliticoDt.getAno().equals("")) {
					relatorioAnaliticoDt = this.atribuirDataAtual(relatorioAnaliticoDt);
				}
				
				stAcao = "WEB-INF/jsptjgo/RelatorioAnaliticoProdutividade.jsp";
				request.getSession().setAttribute("stAcaoRetorno", stAcao);
				request.getSession().setAttribute("acessoEspecial", false);
				
				break;
			
			case Configuracao.Curinga7:
				//Curinga para acesso especial
				request.setAttribute("PaginaAtual", Configuracao.Editar);
	
				if (relatorioAnaliticoDt.getMes().equals("") && relatorioAnaliticoDt.getAno().equals("")) {
					relatorioAnaliticoDt = this.atribuirDataAtual(relatorioAnaliticoDt);
				}
				
				stAcao = "WEB-INF/jsptjgo/RelatorioAnaliticoProdutividadeCorregedoria.jsp";
				request.getSession().setAttribute("stAcaoRetorno", stAcao);
				request.getSession().setAttribute("acessoEspecial", true);
				
				break;
			case Configuracao.Curinga8:
				redireciona(response, "BuscaProcesso?Id_Processo=" + request.getParameter("Id_Processo").toString());
				break;
				
			default:
				request.setAttribute("PaginaAtual", Configuracao.Editar);
	
			if (relatorioAnaliticoDt.getMes().equals("") && relatorioAnaliticoDt.getAno().equals(""))
				relatorioAnaliticoDt = this.atribuirDataAtual(relatorioAnaliticoDt);
				
			if(request.getSession().getAttribute("stAcaoRetorno") != null && !request.getSession().getAttribute("stAcaoRetorno").equals(""))
				stAcao = request.getSession().getAttribute("stAcaoRetorno").toString();
	
			break;
		}
		
		request.setAttribute("tempPrograma", "Relatório Analítico de Produtividade");

		request.getSession().setAttribute("RelatorioAnaliticodt", relatorioAnaliticoDt);
		request.getSession().setAttribute("RelatorioAnaliticone", relatorioEstatisticaNe);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
	/**
	 * Método que atribui a data atual ao relatório ao DT.
	 * 
	 * @param relatorioAnaliticoDt
	 * @return DT com data atualizada
	 * @author hmgodinho
	 */
	protected RelatorioAnaliticoDt atribuirDataAtual(RelatorioAnaliticoDt relatorioAnaliticoDt) {
		Calendar dataAtual = Calendar.getInstance();
		relatorioAnaliticoDt.setMes(String.valueOf(dataAtual.get(Calendar.MONTH) + 1));
		relatorioAnaliticoDt.setAno(String.valueOf(dataAtual.get(Calendar.YEAR)));
		return relatorioAnaliticoDt;
	}

}
