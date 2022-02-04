package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
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

public class PrazoSuspensoCtGen extends Controle {

	private static final long serialVersionUID = 8181506034584462440L;

	public int Permissao() {
		return PrazoSuspensoDt.CodigoPermissao;
	}

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
		PrazoSuspensodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		PrazoSuspensodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");

		switch (paginaatual) {
			case Configuracao.ExcluirResultado: // Excluir
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				PrazoSuspensone.excluir(PrazoSuspensodt);
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;
	
			case Configuracao.Imprimir:
				break;
			case Configuracao.Localizar: // localizar
				if (request.getParameter("Passo")==null){
					nomeBusca = new ArrayList();
					descricao = new ArrayList();
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					nomeBusca.add("Motivo");
					descricao.add("PrazoSuspenso");
					descricao.add("Motivo");
					descricao.add("Data");
					request.setAttribute("tempBuscaId", "Id_PrazoSuspenso");
					request.setAttribute("tempBuscaDescricao", "PrazoSuspenso");
					request.setAttribute("tempBuscaPrograma", "PrazoSuspenso");
					request.setAttribute("tempRetorno", "PrazoSuspenso");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("nomeBusca", nomeBusca);
					request.setAttribute("descricao", descricao);
				}else{
					String stTemp = "";
					request.setAttribute("tempRetorno", "PrazoSuspenso");
					stTemp = PrazoSuspensone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					try{
						enviarJSON(response, stTemp);
						
					}
					catch(Exception e) {}
					return;
				}
				break;
			case Configuracao.Novo:
				PrazoSuspensodt.limpar();
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				break;
			case Configuracao.SalvarResultado:
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				Mensagem = PrazoSuspensone.Verificar(PrazoSuspensodt);
				if (Mensagem.length() == 0) {
					PrazoSuspensone.salvar(PrazoSuspensodt);
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso");
				} else
					request.setAttribute("MensagemErro", Mensagem);
				break;
			case (PrazoSuspensoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					nomeBusca = new ArrayList();
					descricao = new ArrayList();
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					nomeBusca.add("PrazoSuspensoTipo");
					descricao.add("PrazoSuspensoTipo");
					request.setAttribute("tempBuscaId", "Id_PrazoSuspensoTipo");
					request.setAttribute("tempBuscaDescricao", "PrazoSuspensoTipo");
					request.setAttribute("tempBuscaPrograma", "PrazoSuspensoTipo");
					request.setAttribute("tempRetorno", "PrazoSuspenso");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", String.valueOf(PrazoSuspensoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("nomeBusca", nomeBusca);
					request.setAttribute("descricao", descricao);
				}else{
					String stTemp = "";
					request.setAttribute("tempRetorno", "PrazoSuspenso");
					stTemp = PrazoSuspensone.consultarDescricaoPrazoSuspensoTipoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					try{
						enviarJSON(response, stTemp);
						
					}
					catch(Exception e) {}
					return;
				}
				break;
			case (ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					nomeBusca = new ArrayList();
					descricao = new ArrayList();
					nomeBusca.add("Comarca");
					descricao.add("Comarca");
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Comarca");
					request.setAttribute("tempBuscaDescricao","Comarca");
					request.setAttribute("tempBuscaPrograma","Comarca");			
					request.setAttribute("tempRetorno","PrazoSuspenso");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", String.valueOf(ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("nomeBusca", nomeBusca);
					request.setAttribute("descricao", descricao);
					break;
				} else {
					String stTemp="";
					request.setAttribute("tempRetorno","PrazoSuspenso");	
					stTemp = PrazoSuspensone.consultarDescricaoComarcaJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					try{
						enviarJSON(response, stTemp);
						
					}
					catch(Exception e) {
					}
					return;								
				}
			case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					nomeBusca = new ArrayList();
					descricao = new ArrayList();
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					nomeBusca.add("Serventia");
					descricao.add("Serventia");
					descricao.add("Uf");
					request.setAttribute("tempBuscaId", "Id_Serventia");
					request.setAttribute("tempBuscaDescricao", "Serventia");
					request.setAttribute("tempBuscaPrograma", "Serventia");
					request.setAttribute("tempRetorno", "PrazoSuspenso");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("nomeBusca", nomeBusca);
					request.setAttribute("descricao", descricao);
				}else{
					String stTemp = "";
					request.setAttribute("tempRetorno", "PrazoSuspenso");
					stTemp = PrazoSuspensone.consultarDescricaoServentiaJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					try{
						enviarJSON(response, stTemp);
						
					}
					catch(Exception e) {}
					return;
				}
				break;
			case (CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					nomeBusca = new ArrayList();
					descricao = new ArrayList();
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					nomeBusca.add("Cidade");
					descricao.add("Cidade");
					descricao.add("Uf");
					request.setAttribute("tempBuscaId", "Id_Cidade");
					request.setAttribute("tempBuscaDescricao", "Cidade");
					request.setAttribute("tempBuscaPrograma", "Cidade");
					request.setAttribute("tempRetorno", "PrazoSuspenso");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", String.valueOf(CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("nomeBusca", nomeBusca);
					request.setAttribute("descricao", descricao);
				}else{
					String stTemp = "";
					request.setAttribute("tempRetorno", "PrazoSuspenso");
					stTemp = PrazoSuspensone.consultarDescricaoCidadeJSON(stNomeBusca1, stNomeBusca2, PosicaoPaginaAtual);
					
					try{
						enviarJSON(response, stTemp);
						
					}
					catch(Exception e) {}
					return;
				}
				break;
			default:
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				stId = request.getParameter("Id_PrazoSuspenso");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(PrazoSuspensodt.getId())) {
						PrazoSuspensodt.limpar();
						PrazoSuspensodt = PrazoSuspensone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("PrazoSuspensodt", PrazoSuspensodt);
		request.getSession().setAttribute("PrazoSuspensone", PrazoSuspensone);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
