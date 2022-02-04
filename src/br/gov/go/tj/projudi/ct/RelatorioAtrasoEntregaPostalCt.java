package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioAtrasoEntregaPostalDt;
import br.gov.go.tj.projudi.ne.RelatorioAtrasoEntregaPostalNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.ValidacaoUtil;

/**
 * Relatório de consulta de processos com Pendência de postagem e que ainda não tiveram o recebimento do AR, com visualização do tempo decorrido de espera
 * @author mmitsunaga
 *
 */
public class RelatorioAtrasoEntregaPostalCt extends Controle {

	private static final long serialVersionUID = -3431220072697799842L;

	@Override
	public int Permissao() {		
		return RelatorioAtrasoEntregaPostalDt.CodigoPermissao;		
	}

	@Override
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaAtual, String nomebusca, String posicaoPaginaAtual) throws Exception, ServletException, IOException {
		
		String nomeBusca1 = "";
		
		List<RelatorioAtrasoEntregaPostalDt> listaResultado = null;
		
		RelatorioAtrasoEntregaPostalNe relatorioAtrasoEntregaPostalNe = new RelatorioAtrasoEntregaPostalNe();
		
		RelatorioAtrasoEntregaPostalDt relatorioAtrasoEntregaPostalDt = (RelatorioAtrasoEntregaPostalDt) request.getSession().getAttribute("RelatorioAtrasoEntregaPostalDt");
		
		if (relatorioAtrasoEntregaPostalDt == null){
			relatorioAtrasoEntregaPostalDt = new RelatorioAtrasoEntregaPostalDt();
		}		
		
		if (request.getParameter("nomeBusca1") != null){
			nomeBusca1 = request.getParameter("nomeBusca1");
		}
		
		if (request.getParameter("Id_Comarca") != null){
			if (request.getParameter("Id_Comarca").equals("null")){
				relatorioAtrasoEntregaPostalDt.setIdComarca("");
			} else {
				relatorioAtrasoEntregaPostalDt.setIdComarca(request.getParameter("Id_Comarca"));	
			}						
		}
		
		if (request.getParameter("Comarca") != null){
			if (request.getParameter("Comarca").equals("null")){
				relatorioAtrasoEntregaPostalDt.setComarca("");
			} else {
				relatorioAtrasoEntregaPostalDt.setComarca(request.getParameter("Comarca"));
			}			
		}
		
		if (request.getParameter("Id_Serventia") != null){
			if (request.getParameter("Id_Serventia").equals("null")){
				relatorioAtrasoEntregaPostalDt.setIdServentia("");
			} else {
				relatorioAtrasoEntregaPostalDt.setIdServentia(request.getParameter("Id_Serventia"));	
			}						
		}
		
		if (request.getParameter("Serventia") != null){
			if (request.getParameter("Serventia").equals("null")){
				relatorioAtrasoEntregaPostalDt.setServentia("");
			} else {
				relatorioAtrasoEntregaPostalDt.setServentia(request.getParameter("Serventia"));
			}			
		}
		
		if (request.getParameter("DiasEspera") != null){
			if (!request.getParameter("DiasEspera").equals("null")){
				relatorioAtrasoEntregaPostalDt.setDiasEspera(request.getParameter("DiasEspera"));
			}
		}
		
		String stAcao = "/WEB-INF/jsptjgo/RelatorioAtrasoEntregaPostal.jsp";
		
		request.setAttribute("tempPrograma", "Relatório Analítico de Atraso na Entrega Postal - [E-Cartas]");
		request.setAttribute("tempRetorno", "RelatorioAtrasoEntregaPostal");		
		
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		
		switch (paginaAtual){
		
			case Configuracao.Novo:
				relatorioAtrasoEntregaPostalDt.limpar();
				break;
				
			case Configuracao.Localizar:
				
				if (ValidacaoUtil.isVazio(relatorioAtrasoEntregaPostalDt.getIdComarca())){
					request.setAttribute("MensagemErro", "É necessário informar a Comarca.");
					
				} else if (isValorZerado(relatorioAtrasoEntregaPostalDt.getDiasEspera())){
					request.setAttribute("MensagemErro", "É necessário informar um prazo de espera maior que zero.");
					
				} else {
				
					listaResultado = relatorioAtrasoEntregaPostalNe.consultarAtrasoEntregaPostal(
							relatorioAtrasoEntregaPostalDt.getIdComarca(), 
							relatorioAtrasoEntregaPostalDt.getIdServentia(), 
							relatorioAtrasoEntregaPostalDt.getDiasEspera());
			
					Map<String, List<RelatorioAtrasoEntregaPostalDt>> mapa = relatorioAtrasoEntregaPostalNe.agruparProcessosComAtrasoEntregaPostal(listaResultado);
																							
					if (ValidacaoUtil.isVazio(listaResultado)){
						request.setAttribute("MensagemErro", "Não existe resultado para as informações solicitadas.");
						request.setAttribute("PaginaAtual", Configuracao.Editar);					
					} else {
						request.setAttribute("ListaResultados", mapa);
						request.setAttribute("QtdeResultados", listaResultado.size());
					}					
				} 
				
				break;											
			
			case (ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):				
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Comarca"};
					String[] lisDescricao = {"Comarca"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Comarca");
					request.setAttribute("tempBuscaDescricao","Comarca");
					request.setAttribute("tempBuscaPrograma","Comarca");			
					request.setAttribute("tempRetorno","RelatorioAtrasoEntregaPostal");		
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);					
				} else {
					String stTemp="";
					stTemp = relatorioAtrasoEntregaPostalNe.consultarDescricaoComarcaJSON(nomeBusca1, posicaoPaginaAtual);
					enviarJSON(response, stTemp);					
					return;								
				}
				break;	
				
				
			case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (ValidacaoUtil.isVazio(relatorioAtrasoEntregaPostalDt.getIdComarca())){
					request.setAttribute("MensagemErro", "É necessário informar a Comarca.");
				} else {									
					if (request.getParameter("Passo")==null){					
						String[] lisNomeBusca = {"Serventia"};
						String[] lisDescricao = {"Serventia","Estado"};
						stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
						request.setAttribute("tempBuscaId","Id_Serventia");
						request.setAttribute("tempBuscaDescricao","Serventia");
						request.setAttribute("tempBuscaPrograma","Serventia");			
						request.setAttribute("tempRetorno","RelatorioAtrasoEntregaPostal");					
						request.setAttribute("tempDescricaoId","Id");
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
						request.setAttribute("PaginaAtual", (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
						request.setAttribute("PosicaoPaginaAtual", "0");
						request.setAttribute("QuantidadePaginas", "0");
						request.setAttribute("lisNomeBusca", lisNomeBusca);
						request.setAttribute("lisDescricao", lisDescricao);
						break;
					} else {					
						String stTemp = relatorioAtrasoEntregaPostalNe.consultarServentiasAtivasComarcaJSON(
								nomeBusca1, posicaoPaginaAtual, relatorioAtrasoEntregaPostalDt.getIdComarca(), String.valueOf(ServentiaTipoDt.VARA));
						enviarJSON(response, stTemp);
						return;								
					}
				}
				break;
				
			default:
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				break;
		
		}
		
		request.setAttribute("PaginaAtual", paginaAtual);
		request.setAttribute("PosicaoPaginaAtual", posicaoPaginaAtual);		
		request.setAttribute("QtdeResultados", listaResultado != null ? listaResultado.size() : 0);
		request.getSession().setAttribute("RelatorioAtrasoEntregaPostalDt", relatorioAtrasoEntregaPostalDt);
				
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
		
	}
	
	private boolean isValorZerado(String diasEspera){
		return ValidacaoUtil.isNaoVazio(diasEspera) && Integer.valueOf(diasEspera) == 0; 
	}
	
}
