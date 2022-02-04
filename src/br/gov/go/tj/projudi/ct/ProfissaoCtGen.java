package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProfissaoDt;
import br.gov.go.tj.projudi.ne.ProfissaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class ProfissaoCtGen extends Controle {

	private static final long serialVersionUID = -7769753816945965207L;

	public int Permissao() {
		return ProfissaoDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProfissaoDt Profissaodt;
		ProfissaoNe Profissaone;
		String stNomeBusca1 = "";
		String Mensagem = "";
		String stId = "";
		String stAcao = "/WEB-INF/jsptjgo/Profissao.jsp";

		// --------------------------------------------------------------------------
		// Faz o mapeamento do id e da descrição das buscas externas com as
		// variaveis locais.
		// Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		// cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e
		// outra
		// tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		// --------------------------------------------------------------------------
		request.setAttribute("tempPrograma", "Profissao");

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		Profissaone = (ProfissaoNe) request.getSession().getAttribute("Profissaone");
		if (Profissaone == null) Profissaone = new ProfissaoNe();

		Profissaodt = (ProfissaoDt) request.getSession().getAttribute("Profissaodt");
		if (Profissaodt == null) Profissaodt = new ProfissaoDt();

		Profissaodt.setProfissaoCodigo(request.getParameter("ProfissaoCodigo"));
		Profissaodt.setProfissao(request.getParameter("Profissao"));

		Profissaodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Profissaodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {
		case Configuracao.ExcluirResultado: // Excluir
			Profissaone.excluir(Profissaodt);
			request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
			break;

		case Configuracao.Imprimir:
			break;
		case Configuracao.Localizar: // localizar
			if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"Profissao"};										
				String[] lisDescricao = {"Profissao"};
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_Profissao");
				request.setAttribute("tempBuscaDescricao", "Profissao");
				request.setAttribute("tempBuscaPrograma", "Profissao");
				request.setAttribute("tempRetorno", "Profissao");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", Configuracao.Localizar);
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			}else{
				String stTemp = "";
				stTemp = Profissaone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
				
				try{
					enviarJSON(response, stTemp);
					
				}
				catch(Exception e) {}
				return;
			}
			break;
		case Configuracao.Novo:
			Profissaodt.limpar();
			break;
		case Configuracao.SalvarResultado:
			Mensagem = Profissaone.Verificar(Profissaodt);
			if (Mensagem.length() == 0) {
				Profissaone.salvar(Profissaodt);
				request.setAttribute("MensagemOk", "Dados Salvos com sucesso");
			} else
				request.setAttribute("MensagemErro", Mensagem);
			break;
		default:
			stId = request.getParameter("Id_Profissao");
			if (stId != null && !stId.isEmpty())
				if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(Profissaodt.getId())) {
					Profissaodt.limpar();
					Profissaodt = Profissaone.consultarId(stId);
				}
			break;
		}

		request.getSession().setAttribute("Profissaodt", Profissaodt);
		request.getSession().setAttribute("Profissaone", Profissaone);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
