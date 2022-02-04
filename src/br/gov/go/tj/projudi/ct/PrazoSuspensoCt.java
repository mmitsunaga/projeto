package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.CidadeDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.PrazoSuspensoDt;
import br.gov.go.tj.projudi.dt.PrazoSuspensoTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.ne.PrazoSuspensoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class PrazoSuspensoCt extends PrazoSuspensoCtGen {

	private static final long serialVersionUID = 2712511897395672675L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		PrazoSuspensoDt PrazoSuspensodt;
		PrazoSuspensoNe PrazoSuspensone;
		List nomeBusca;
		List descricao;
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		String Mensagem = "";
		String stId = "";

		String stAcao = "/WEB-INF/jsptjgo/PrazoSuspenso.jsp";

		// --------------------------------------------------------------------------
		// Faz o mapeamento do id e da descrição das buscas externas com as
		// variaveis locais.
		// Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		// cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e
		// outra
		// tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		// --------------------------------------------------------------------------
		request.setAttribute("tempPrograma", "PrazoSuspenso");
		request.setAttribute("tempBuscaId_PrazoSuspenso", "Id_PrazoSuspenso");
		request.setAttribute("tempBuscaPrazoSuspenso", "PrazoSuspenso");
		request.setAttribute("tempBuscaId_PrazoSuspensoTipo", "Id_PrazoSuspensoTipo");
		request.setAttribute("tempBuscaPrazoSuspensoTipo", "PrazoSuspensoTipo");
		request.setAttribute("tempBuscaId_Comarca", "Id_Comarca");
		request.setAttribute("tempBuscaComarca", "Comarca");
		request.setAttribute("tempBuscaId_Serventia", "Id_Serventia");
		request.setAttribute("tempBuscaServentia", "Serventia");
		request.setAttribute("tempBuscaId_Cidade", "Id_Cidade");
		request.setAttribute("tempBuscaCidade", "Cidade");
		request.setAttribute("tempRetorno", "PrazoSuspenso");
		
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		PrazoSuspensone = (PrazoSuspensoNe) request.getSession().getAttribute("PrazoSuspensone");
		if (PrazoSuspensone == null) PrazoSuspensone = new PrazoSuspensoNe();

		PrazoSuspensodt = (PrazoSuspensoDt) request.getSession().getAttribute("PrazoSuspensodt");
		if (PrazoSuspensodt == null) PrazoSuspensodt = new PrazoSuspensoDt();

		PrazoSuspensodt.setMotivo(request.getParameter("Motivo"));
		PrazoSuspensodt.setId_PrazoSuspensoTipo(request.getParameter("Id_PrazoSuspensoTipo"));
		PrazoSuspensodt.setPrazoSuspensoTipo(request.getParameter("PrazoSuspensoTipo"));
		PrazoSuspensodt.setId_Comarca(request.getParameter("Id_Comarca"));
		PrazoSuspensodt.setComarca(request.getParameter("Comarca"));
		PrazoSuspensodt.setId_Serventia(request.getParameter("Id_Serventia"));
		PrazoSuspensodt.setServentia(request.getParameter("Serventia"));
		PrazoSuspensodt.setId_Cidade(request.getParameter("Id_Cidade"));
		PrazoSuspensodt.setCidade(request.getParameter("Cidade"));
		PrazoSuspensodt.setData(request.getParameter("DataFeriado"));
		PrazoSuspensodt.setDataInicial(request.getParameter("DataInicial"));
		PrazoSuspensodt.setDataFinal(request.getParameter("DataFinal"));
		PrazoSuspensodt.setPrazoSuspensoTipoCodigo(request.getParameter("PrazoSuspensoTipoCodigo"));
		PrazoSuspensodt.setComarcaCodigo(request.getParameter("ComarcaCodigo"));
		PrazoSuspensodt.setServentiaCodigo(request.getParameter("ServentiaCodigo"));
		PrazoSuspensodt.setCidadeCodigo(request.getParameter("CidadeCodigo"));
		PrazoSuspensodt.setPlantaoLiberado(request.getParameter("PlantaoLiberado"));	
		PrazoSuspensodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		PrazoSuspensodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");

		switch (paginaatual) {
			case Configuracao.Localizar: // localizar
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Motivo"};
					String[] lisDescricao = {"PrazoSuspenso","Motivo", "Data"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_PrazoSuspenso");
					request.setAttribute("tempBuscaDescricao", "PrazoSuspenso");
					request.setAttribute("tempBuscaPrograma", "PrazoSuspenso");
					request.setAttribute("tempRetorno", "PrazoSuspenso");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = PrazoSuspensone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;

			case (PrazoSuspensoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"PrazoSuspensoTipo"};
					String[] lisDescricao = {"PrazoSuspensoTipo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_PrazoSuspensoTipo");
					request.setAttribute("tempBuscaDescricao", "PrazoSuspensoTipo");
					request.setAttribute("tempBuscaPrograma", "PrazoSuspensoTipo");
					request.setAttribute("tempRetorno", "PrazoSuspenso");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", String.valueOf(PrazoSuspensoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = PrazoSuspensone.consultarDescricaoPrazoSuspensoTipoJSON(stNomeBusca1, PosicaoPaginaAtual);
					enviarJSON(response, stTemp);
					return;
				}
				break;
			case (ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Comarca"};
					String[] lisDescricao = {"Comarca"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Comarca");
					request.setAttribute("tempBuscaDescricao", "Comarca");
					request.setAttribute("tempBuscaPrograma", "Comarca");
					request.setAttribute("tempRetorno", "PrazoSuspenso");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", String.valueOf(ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = PrazoSuspensone.consultarDescricaoComarcaJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
				
			case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Serventia"};
					String[] lisDescricao = {"Serventia","Uf"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Serventia");
					request.setAttribute("tempBuscaDescricao", "Serventia");
					request.setAttribute("tempBuscaPrograma", "Serventia");
					request.setAttribute("tempRetorno", "PrazoSuspenso");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = PrazoSuspensone.consultarDescricaoServentiaJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
				
			case (CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Cidade"};
					String[] lisDescricao = {"Cidade","Uf"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Cidade");
					request.setAttribute("tempBuscaDescricao", "Cidade");
					request.setAttribute("tempBuscaPrograma", "Cidade");
					request.setAttribute("tempRetorno", "PrazoSuspenso");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", String.valueOf(CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = PrazoSuspensone.consultarDescricaoCidadeJSON(stNomeBusca1, stNomeBusca2, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
				
			default:
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
			return;
		}

		request.getSession().setAttribute("PrazoSuspensodt", PrazoSuspensodt);
		request.getSession().setAttribute("PrazoSuspensone", PrazoSuspensone);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
