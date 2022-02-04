package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.PendenciaStatusDt;
import br.gov.go.tj.projudi.ne.PendenciaStatusNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class PendenciaStatusCtGen extends Controle {

	private static final long serialVersionUID = -3162351676884899519L;

	public int Permissao() {
		return PendenciaStatusDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		PendenciaStatusDt PendenciaStatusdt;
		PendenciaStatusNe PendenciaStatusne;
		List nomeBusca;
		List descricao;
		String stNomeBusca1 = "";
		String Mensagem = "";
		String stId = "";

		String stAcao = "/WEB-INF/jsptjgo/PendenciaStatus.jsp";

		// --------------------------------------------------------------------------
		// Faz o mapeamento do id e da descrição das buscas externas com as
		// variaveis locais.
		// Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		// cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e
		// outra
		// tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		// --------------------------------------------------------------------------
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		PendenciaStatusne = (PendenciaStatusNe) request.getSession().getAttribute("PendenciaStatusne");
		if (PendenciaStatusne == null) PendenciaStatusne = new PendenciaStatusNe();

		PendenciaStatusdt = (PendenciaStatusDt) request.getSession().getAttribute("PendenciaStatusdt");
		if (PendenciaStatusdt == null) PendenciaStatusdt = new PendenciaStatusDt();

		PendenciaStatusdt.setPendenciaStatus(request.getParameter("PendenciaStatus"));
		PendenciaStatusdt.setPendenciaStatusCodigo(request.getParameter("PendenciaStatusCodigo"));
		PendenciaStatusdt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		PendenciaStatusdt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.setAttribute("tempPrograma", "PendenciaStatus");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {
			case Configuracao.ExcluirResultado: // Excluir
				PendenciaStatusne.excluir(PendenciaStatusdt);
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;
				case Configuracao.Imprimir:
				break;
			case Configuracao.Localizar: // localizar
				if (request.getParameter("Passo")==null){
					nomeBusca = new ArrayList();
					descricao = new ArrayList();
					nomeBusca.add("PendenciaStatus");
					descricao.add("PendenciaStatus");
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_PendenciaStatus");
					request.setAttribute("tempBuscaDescricao","PendenciaStatus");
					request.setAttribute("tempBuscaPrograma","PendenciaStatus");			
					request.setAttribute("tempRetorno","PendenciaStatus");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", String.valueOf(Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("nomeBusca", nomeBusca);
					request.setAttribute("descricao", descricao);
					break;
				} else {
					String stTemp="";
					request.setAttribute("tempRetorno","PendenciaStatus");	
					stTemp = PendenciaStatusne.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					try{
						enviarJSON(response, stTemp);
						
					}
					catch(Exception e) {
					}
					return;								
				}
			case Configuracao.Novo:
				PendenciaStatusdt.limpar();
				break;
			case Configuracao.SalvarResultado:
				Mensagem = PendenciaStatusne.Verificar(PendenciaStatusdt);
				if (Mensagem.length() == 0) {
					PendenciaStatusne.salvar(PendenciaStatusdt);
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso");
				} else
					request.setAttribute("MensagemErro", Mensagem);
				break;
			default:
				stId = request.getParameter("Id_PendenciaStatus");
				if (stId != null && !stId.isEmpty()) {
					if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(PendenciaStatusdt.getId())) {
						PendenciaStatusdt.limpar();
						PendenciaStatusdt = PendenciaStatusne.consultarId(stId);
					}
				}
				break;
		}

		request.getSession().setAttribute("PendenciaStatusdt", PendenciaStatusdt);
		request.getSession().setAttribute("PendenciaStatusne", PendenciaStatusne);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
