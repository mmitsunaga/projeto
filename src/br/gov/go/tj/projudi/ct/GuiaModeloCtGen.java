package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.GuiaModeloDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.ne.GuiaModeloNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class GuiaModeloCtGen extends Controle {

	private static final long serialVersionUID = 5311636130869976800L;

	public int Permissao() {
		return GuiaModeloDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		GuiaModeloDt GuiaModelodt;
		GuiaModeloNe GuiaModelone;
		List nomeBusca;
		List descricao;
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		String stNomeBusca3 = "";
		String Mensagem = "";
		String stId = "";
		String stAcao = "/WEB-INF/jsptjgo/GuiaModelo.jsp";

		// --------------------------------------------------------------------------
		// Faz o mapeamento do id e da descrição das buscas externas com as
		// variaveis locais.
		// Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		// cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e
		// outra
		// tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		// --------------------------------------------------------------------------

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		if(request.getParameter("nomeBusca3") != null) stNomeBusca3 = request.getParameter("nomeBusca3");
		
		GuiaModelone = (GuiaModeloNe) request.getSession().getAttribute("GuiaModelone");
		if (GuiaModelone == null) GuiaModelone = new GuiaModeloNe();

		GuiaModelodt = (GuiaModeloDt) request.getSession().getAttribute("GuiaModelodt");
		if (GuiaModelodt == null) GuiaModelodt = new GuiaModeloDt();

		GuiaModelodt.setGuiaModelo(request.getParameter("GuiaModelo"));
		GuiaModelodt.setGuiaModeloCodigo(request.getParameter("GuiaModeloCodigo"));
		GuiaModelodt.setId_GuiaTipo(request.getParameter("Id_GuiaTipo"));
		GuiaModelodt.setGuiaTipo(request.getParameter("GuiaTipo"));
		GuiaModelodt.setId_ProcessoTipo(request.getParameter("Id_ProcessoTipo"));
		GuiaModelodt.setProcessoTipo(request.getParameter("ProcessoTipo"));
		GuiaModelodt.setId_NaturezaSPG(request.getParameter("Id_NaturezaSPG"));
		GuiaModelodt.setNaturezaSPG(request.getParameter("NaturezaSPG"));
		GuiaModelodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		GuiaModelodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.setAttribute("tempPrograma", "GuiaModelo");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);
		
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: // Excluir
				GuiaModelone.excluir(GuiaModelodt);
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;
	
			case Configuracao.Imprimir:
				break;
			case Configuracao.Localizar: // localizar
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Guia Tipo","Classe"};
					String[] lisDescricao = {"Guia Tipo","Classe"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_GuiaModelo");
					request.setAttribute("tempBuscaDescricao", "GuiaModelo");
					request.setAttribute("tempBuscaPrograma", "GuiaModelo");
					request.setAttribute("tempRetorno", "GuiaModelo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					GuiaModelodt.limpar();
				}else{
					String stTemp = "";
					stTemp = GuiaModelone.consultarDescricaoJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3, PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
			case Configuracao.Novo:
				GuiaModelodt.limpar();
				break;
			case Configuracao.SalvarResultado:
				Mensagem = GuiaModelone.Verificar(GuiaModelodt);
				if (Mensagem.length() == 0) {
					GuiaModelone.salvar(GuiaModelodt);
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso");
				} else
					request.setAttribute("MensagemErro", Mensagem);
				break;
			case (GuiaTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					nomeBusca = new ArrayList();
					descricao = new ArrayList();
					nomeBusca.add("GuiaTipo");
					descricao.add("GuiaTipo");
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_GuiaTipo");
					request.setAttribute("tempBuscaDescricao","GuiaTipo");
					request.setAttribute("tempBuscaPrograma","GuiaTipo");			
					request.setAttribute("tempRetorno","GuiaModelo");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", String.valueOf(GuiaTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("nomeBusca", nomeBusca);
					request.setAttribute("descricao", descricao);
					break;
				} else {
					String stTemp="";
					stTemp = GuiaModelone.consultarDescricaoGuiaTipoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					try{
						enviarJSON(response, stTemp);
						
					}
					catch(Exception e) {
					}
					return;								
				}
			case (ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					nomeBusca = new ArrayList();
					descricao = new ArrayList();
					nomeBusca.add("ProcessoTipo");
					descricao.add("ProcessoTipo");
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ProcessoTipo");
					request.setAttribute("tempBuscaDescricao","ProcessoTipo");
					request.setAttribute("tempBuscaPrograma","ProcessoTipo");			
					request.setAttribute("tempRetorno","GuiaModelo");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", String.valueOf(ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("nomeBusca", nomeBusca);
					request.setAttribute("descricao", descricao);
					break;
				} else {
					String stTemp="";
					stTemp = GuiaModelone.consultarDescricaoProcessoTipoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					try{
						enviarJSON(response, stTemp);
						
					}
					catch(Exception e) {
					}
					return;								
				}
			default:
				stId = request.getParameter("Id_GuiaModelo");
				if (stId != null && !stId.isEmpty()) {
					if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(GuiaModelodt.getId())) {
						GuiaModelodt.limpar();
						GuiaModelodt = GuiaModelone.consultarId(stId);
					}
				}
				break;
		}

		request.getSession().setAttribute("GuiaModelodt", GuiaModelodt);
		request.getSession().setAttribute("GuiaModelone", GuiaModelone);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
