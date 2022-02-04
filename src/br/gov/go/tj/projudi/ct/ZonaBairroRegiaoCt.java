package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.projudi.dt.RegiaoDt;
import br.gov.go.tj.projudi.dt.ZonaBairroRegiaoDt;
import br.gov.go.tj.projudi.dt.ZonaDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.ne.ZonaBairroRegiaoNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ZonaBairroRegiaoCt extends Controle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4665204438225024401L;

	public int Permissao() {
		return ZonaBairroRegiaoDt.CodigoPermissao;
	}
	
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ZonaBairroRegiaoDt zonaBairroRegiaoDt;
		ZonaBairroRegiaoNe zonaBairroRegiaoNe;
		String mensagem = "";
		int paginaAnterior = Funcoes.StringToInt(request.getParameter("PaginaAnterior"));
		String stAcao = "/WEB-INF/jsptjgo/ZonaBairroRegiao.jsp";
		
		//-Variáveis para controlar as buscas utilizando ajax
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		String stNomeBusca3 = "";
		String stNomeBusca4 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		if(request.getParameter("nomeBusca3") != null) stNomeBusca3 = request.getParameter("nomeBusca3");
		if(request.getParameter("nomeBusca4") != null) stNomeBusca4 = request.getParameter("nomeBusca4");

		zonaBairroRegiaoNe = (ZonaBairroRegiaoNe) request.getSession().getAttribute("zonaBairroRegiaoNe");
		if (zonaBairroRegiaoNe == null)
			zonaBairroRegiaoNe = new ZonaBairroRegiaoNe();

		zonaBairroRegiaoDt = (ZonaBairroRegiaoDt) request.getSession().getAttribute("zonaBairroRegiaoDt");
		if (zonaBairroRegiaoDt == null)
			zonaBairroRegiaoDt = new ZonaBairroRegiaoDt();
		
		zonaBairroRegiaoDt.setId_Bairro(request.getParameter("Id_Bairro"));
		zonaBairroRegiaoDt.setBairro(request.getParameter("Bairro"));		
		if (zonaBairroRegiaoDt.getId_Bairro() == null || zonaBairroRegiaoDt.getId_Bairro().trim().length() == 0) {
			zonaBairroRegiaoDt.setId_Cidade("");
			zonaBairroRegiaoDt.setCidade("");						
		}		
		zonaBairroRegiaoDt.setId_Zona(request.getParameter("Id_Zona"));
		zonaBairroRegiaoDt.setZona(request.getParameter("Zona"));
		zonaBairroRegiaoDt.setId_Regiao(request.getParameter("Id_Regiao"));
		zonaBairroRegiaoDt.setRegiao(request.getParameter("Regiao"));
		zonaBairroRegiaoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		zonaBairroRegiaoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.setAttribute("tempPrograma", "ZonaBairroRegiao");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {
		
		case Configuracao.ExcluirResultado: 
			if(zonaBairroRegiaoDt.getId() != null && zonaBairroRegiaoDt.getId().length() > 0 ) {
				zonaBairroRegiaoNe.excluir(zonaBairroRegiaoDt);
				request.getSession().removeAttribute("zonaBairroRegiaoDt");
				request.setAttribute("MensagemOk", "Zona/Bairro/Região excluída com sucesso.");
			} else  {
				request.setAttribute("MensagemErro", "Para excluir uma Zona/Bairro/Região é necessário consultá-la antes" );
			}
			break;

		case Configuracao.Localizar: 
			if (request.getParameter("Passo") == null) {
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				String[] lisNomeBusca = {"Zona", "Cidade", "Bairro", "Região"};
				String[] lisDescricao = {"Zona", "Cidade", "Bairro", "Região"};
				request.setAttribute("tempBuscaId", "Id_ZonaBairroRegiao");
				request.setAttribute("tempBuscaDescricao", "Zona");
				request.setAttribute("tempBuscaPrograma", "Zona");
				request.setAttribute("tempRetorno", "ZonaBairroRegiao");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", Configuracao.Localizar);
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				String stTemp = "";
				stTemp = zonaBairroRegiaoNe.consultarDescricaoJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3, stNomeBusca4, PosicaoPaginaAtual);								
				enviarJSON(response, stTemp);									
				return;
			}
			break;
		case Configuracao.Novo: //conferido
			request.getSession().removeAttribute("zonaBairroRegiaoDt");
			zonaBairroRegiaoDt.limpar();
			break;
			
		case Configuracao.SalvarResultado: //conferido
			mensagem = zonaBairroRegiaoNe.VerificarSalvar(zonaBairroRegiaoDt);			
			if (mensagem.length() == 0) {
				zonaBairroRegiaoNe.salvar(zonaBairroRegiaoDt);
				request.setAttribute("MensagemOk", "Dados Salvos com sucesso");				
			} else {
				request.setAttribute("MensagemErro", mensagem);
			}
			break;
		
		// Consulta de bairros
		case (BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar): 
			if (request.getParameter("Passo") == null) {
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				String[] lisNomeBusca = { "Bairro", "Cidade", "UF", "ZONA" };
				String[] lisDescricao = { "Bairro", "Cidade", "UF", "Zona", "Região" };
				request.setAttribute("tempBuscaId", "Id_Bairro");
				request.setAttribute("tempBuscaDescricao", "Bairro");
				request.setAttribute("tempBuscaPrograma", "Bairro");
				request.setAttribute("tempRetorno", "ZonaBairroRegiao");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				String stTemp = "";
				stTemp = zonaBairroRegiaoNe.consultarDescricaoBairroJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3, stNomeBusca4, PosicaoPaginaAtual);								
				enviarJSON(response, stTemp);												
				return;
			}
			break;
			
		// Consulta de zonas
		case (ZonaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar): 
			if (request.getParameter("Passo") == null) {
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				String[] lisNomeBusca = { "Zona", "Comarca" };
				String[] lisDescricao = { "Zona", "Comarca" };
				request.setAttribute("tempBuscaId", "Id_Zona");
				request.setAttribute("tempBuscaDescricao", "Zona");
				request.setAttribute("tempBuscaPrograma", "Zona");
				request.setAttribute("tempRetorno", "ZonaBairroRegiao");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (ZonaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				String stTemp = "";
				stTemp = zonaBairroRegiaoNe.consultarDescricaoZonaJSON(stNomeBusca1, stNomeBusca2,  PosicaoPaginaAtual);								
				enviarJSON(response, stTemp);					
				return;
			}
			break;
			
		// Consulta de regiões
		case (RegiaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar): 
			if (request.getParameter("Passo") == null) {
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				String[] lisNomeBusca = { "Regiao", "Comarca" };
				String[] lisDescricao = { "Regiao", "Comarca" };
				request.setAttribute("tempBuscaId", "Id_Regiao");
				request.setAttribute("tempBuscaDescricao", "Regiao");
				request.setAttribute("tempBuscaPrograma", "Regiao");
				request.setAttribute("tempRetorno", "ZonaBairroRegiao");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (RegiaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				String stTemp = "";
				stTemp = zonaBairroRegiaoNe.consultarDescricaoRegiaoJSON(stNomeBusca1, stNomeBusca2,  PosicaoPaginaAtual);								
				enviarJSON(response, stTemp);					
				return;
			}
			break;
		
		default:
			switch (paginaAnterior) {
				case Configuracao.Localizar: 
					String stId = request.getParameter("Id_ZonaBairroRegiao");
					if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(zonaBairroRegiaoDt.getId())) {
						zonaBairroRegiaoDt.limpar();
						zonaBairroRegiaoDt = zonaBairroRegiaoNe.consultarId(stId);
					}
					break;
					
				case (BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar): 
					if (zonaBairroRegiaoDt.getId_Bairro() != null && zonaBairroRegiaoDt.getId_Bairro().trim().length() > 0) {
						
						zonaBairroRegiaoDt.setId_Cidade("");
						zonaBairroRegiaoDt.setCidade("");
						
						ZonaBairroRegiaoDt zonaBairroRegiaoDtAux = zonaBairroRegiaoNe.consultarIdBairro(zonaBairroRegiaoDt.getId_Bairro());
						if (zonaBairroRegiaoDtAux != null) {
							zonaBairroRegiaoDt = zonaBairroRegiaoDtAux;
						} else {
							BairroDt bairroDt = zonaBairroRegiaoNe.consultarBairroId(zonaBairroRegiaoDt.getId_Bairro());
							if (bairroDt != null) {
								zonaBairroRegiaoDt.limpar();
								zonaBairroRegiaoDt.setId_Bairro(bairroDt.getId());
								zonaBairroRegiaoDt.setBairro(bairroDt.getBairro());
								zonaBairroRegiaoDt.setId_Cidade(bairroDt.getId_Cidade());
								zonaBairroRegiaoDt.setCidade(bairroDt.getCidade());	
							}
						}			
					}
					break;
			}
			
			break;
		}

		request.getSession().setAttribute("zonaBairroRegiaoDt", zonaBairroRegiaoDt);
		request.getSession().setAttribute("zonaBairroRegiaoNe", zonaBairroRegiaoNe);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
