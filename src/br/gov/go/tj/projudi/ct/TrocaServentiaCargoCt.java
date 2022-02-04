package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaGrupoDt;
import br.gov.go.tj.projudi.ne.ServentiaCargoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

/**
 * Gerencia as trocas de usuários em cargos existentes. Um cargo pode ter o
 * usuário trocado ou até mesmo retirado, deixando o cargo vazio.
 * 
 * @author msapaula 20/08/2009 11:37:18
 */
public class TrocaServentiaCargoCt extends Controle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5375856807912197111L;

	@Override
	public int Permissao() {
		return 383;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ServentiaCargoDt ServentiaCargodt;
		ServentiaCargoNe ServentiaCargone;

		List tempList = null;
		String stId = "";
		String Mensagem = "";
		String stAcao = "/WEB-INF/jsptjgo/TrocaServentiaCargo.jsp";
		String stNomeBusca1 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("tempPrograma", "TrocaServentiaCargo");
		request.setAttribute("tempRetorno", "TrocaServentiaCargo");

		ServentiaCargone = (ServentiaCargoNe) request.getSession().getAttribute("ServentiaCargone");
		if (ServentiaCargone == null) ServentiaCargone = new ServentiaCargoNe();

		ServentiaCargodt = (ServentiaCargoDt) request.getSession().getAttribute("ServentiaCargodt");
		if (ServentiaCargodt == null) ServentiaCargodt = new ServentiaCargoDt();

		ServentiaCargodt.setId_UsuarioServentiaGrupo(request.getParameter("Id_UsuarioServentiaGrupo"));
		ServentiaCargodt.setUsuarioServentiaGrupo(request.getParameter("UsuarioServentiaGrupo"));
		ServentiaCargodt.setNomeUsuario(request.getParameter("UsuarioServentiaGrupo"));
		ServentiaCargodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ServentiaCargodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
		//É preciso setar o TipoConsulta na sessão para que ele não se perca ao tentar alterar um usuário.
		if(request.getParameter("TipoConsulta") != null && !request.getParameter("TipoConsulta").equals("")){
			request.getSession().setAttribute("TipoConsulta", request.getParameter("TipoConsulta"));
			request.removeAttribute("TipoConsulta");
		}

		request.setAttribute("PaginaAnterior", paginaatual);
		if (request.getAttribute("MensagemOk") != null) request.setAttribute("MensagemOk", request.getAttribute("MensagemOk"));
		else request.setAttribute("MensagemOk", "");
		if (request.getAttribute("MensagemErro") != null) request.setAttribute("MensagemErro", request.getAttribute("MensagemErro"));
		else request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {

			case Configuracao.Novo:
				ServentiaCargodt.limpar();
				break;

			// Função utilizada para listar a Estrutura de Cargos de uma Serventia
			// e direcionar o retorno para a página correta.
			case Configuracao.Localizar:
				Integer tipoConsulta = Funcoes.StringToInt(request.getSession().getAttribute("TipoConsulta").toString());
				switch (tipoConsulta) {
					case 1:
						tempList = ServentiaCargone.consultarServentiaCargos(UsuarioSessao);
						request.setAttribute("listaServentiaCargos", tempList);
						stAcao = "/WEB-INF/jsptjgo/EstruturaServentiaCargo.jsp";
						break;
					case 2:
						tempList = ServentiaCargone.consultarCargosServentiasRelacionadas(UsuarioSessao.getUsuarioDt().getId_Serventia());
						request.setAttribute("listaServentiaCargos", tempList);
						stAcao = "/WEB-INF/jsptjgo/EstruturaServentiaRelacionadaCargo.jsp";
						break;
				}
				break;
	
			case Configuracao.Salvar:
				request.setAttribute("Mensagem", "Clique para confirmar a Habilitação do Usuário " + ServentiaCargodt.getNomeUsuario() + " no Cargo " + ServentiaCargodt.getServentiaCargo() + ".");
				break;
	
			case Configuracao.SalvarResultado:
				Mensagem = ServentiaCargone.verificarHabilitacaoCargo(ServentiaCargodt);
				if (Mensagem.length() == 0) {
					ServentiaCargone.habilitaUsuarioServentiaCargo(ServentiaCargodt);
					request.setAttribute("MensagemOk", "Usuário " + ServentiaCargodt.getNomeUsuario() + " foi habilitado no Cargo " + ServentiaCargodt.getServentiaCargo() + " com Sucesso.");
					ServentiaCargodt.limpar();
					// Redireciona para tela de Estrutura de cargos
					this.executar(request, response, UsuarioSessao, Configuracao.Localizar, tempNomeBusca, PosicaoPaginaAtual);
					return;
				} else request.setAttribute("MensagemErro", Mensagem);
				break;

			// Confirmação da ação Excluir Cargo
			case Configuracao.Excluir:
				stId = request.getParameter("Id_ServentiaCargo");
				if (stId != null && !stId.isEmpty()) if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(ServentiaCargodt.getId())) ServentiaCargodt = ServentiaCargone.consultarId(stId);
				request.setAttribute("Mensagem", "Clique para confirmar a Exclusão do Cargo " + ServentiaCargodt.getServentiaCargo() + ".");
				request.setAttribute("Curinga", "E");
				break;

			// Função utilizada para Limpar um Cargo ou Excluir um Cargo
			//Se Curinga for L - Limpar, E - Excluir
			case Configuracao.ExcluirResultado:
				if (ServentiaCargodt.getId() != null && ServentiaCargodt.getId().length() > 0) {

					Mensagem = ServentiaCargone.verificarDesabilitacaoCargo(ServentiaCargodt, request.getParameter("Curinga"));
					if (Mensagem.length() == 0) {
						if (request.getParameter("Curinga").equals("L")) {
							//Limpando Cargo
							String nome = ServentiaCargodt.getNomeUsuario();

							ServentiaCargone.desabilitaUsuarioServentiaCargo(ServentiaCargodt);
							request.setAttribute("MensagemOk", "Usuário " + nome + " foi retirado do Cargo " + ServentiaCargodt.getServentiaCargo() + " com Sucesso.");
							ServentiaCargodt.limpar();

						} else if (request.getParameter("Curinga").equals("E")) {
							//Excluindo cargo
							ServentiaCargone.excluir(ServentiaCargodt);
							request.setAttribute("MensagemOk", " Cargo " + ServentiaCargodt.getServentiaCargo() + " excluído com Sucesso.");
							ServentiaCargodt.limpar();
						}
					} else request.setAttribute("MensagemErro", "Cargo não pode ser Desabilitado. Motivo(s): " + Mensagem);

				} else request.setAttribute("MensagemErro", "Não foi possível obter Cargo para Desabilitar.");

				// Redireciona para tela de Estrutura de cargos
				this.executar(request, response, UsuarioSessao, Configuracao.Localizar, tempNomeBusca, PosicaoPaginaAtual);
				return;

				//Função utilizada para Confirmar a Retirada de Usuário de um Cargo
			case Configuracao.LocalizarAutoPai:
				stId = request.getParameter("Id_ServentiaCargo");
				if (stId != null && !stId.isEmpty()) if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(ServentiaCargodt.getId())) ServentiaCargodt = ServentiaCargone.consultarId(stId);
				request.setAttribute("Mensagem", "Clique para confirmar a Retirada do Usuário " + ServentiaCargodt.getNomeUsuario() + " do Cargo " + ServentiaCargodt.getServentiaCargo() + ".");
				request.setAttribute("PaginaAnterior", Configuracao.Excluir);
				request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
				request.setAttribute("Curinga", "L");
				break;

			// Função utilizada para Limpar um Cargo - Retirar Usuário
			case Configuracao.Curinga6:
				if (ServentiaCargodt.getId() != null && ServentiaCargodt.getId().length() > 0) {
					//Passa-se o parâmetro "L" indicando que é uma limpeza do cargo.
					Mensagem = ServentiaCargone.verificarDesabilitacaoCargo(ServentiaCargodt, "L");
					if (Mensagem.length() == 0) {
						String nome = ServentiaCargodt.getNomeUsuario();

						ServentiaCargone.desabilitaUsuarioServentiaCargo(ServentiaCargodt);
						request.setAttribute("MensagemOk", "Usuário " + nome + " foi retirado do Cargo " + ServentiaCargodt.getServentiaCargo() + " com Sucesso.");

						ServentiaCargodt.limpar();

					} else request.setAttribute("MensagemErro", Mensagem);
				} else request.setAttribute("MensagemErro", "Não foi possível obter Cargo para limpar.");

				// Redireciona para tela de Estrutura de cargos
				this.executar(request, response, UsuarioSessao, Configuracao.Localizar, tempNomeBusca, PosicaoPaginaAtual);
				break;

			// Consulta usuários que ocuparão um cargo da serventia
			case (UsuarioServentiaGrupoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (!ServentiaCargodt.getId_Serventia().equals("") && !ServentiaCargodt.getId_CargoTipo().equals("")) {
					if (request.getParameter("Passo") == null) {
						
						stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
						String[] lisNomeBusca = {"Usuário"};
						String[] lisDescricao = {"Usuário","Nome","Grupo","Serventia"};
						
						String[] camposHidden = {"NomeUsuario"};
						request.setAttribute("tempBuscaId", "Id_UsuarioServentiaGrupo");
						request.setAttribute("tempBuscaDescricao", "UsuarioServentiaGrupo");
						request.setAttribute("tempBuscaPrograma", "UsuarioServentiaGrupo");
						request.setAttribute("tempRetorno", "TrocaServentiaCargo");
						request.setAttribute("tempDescricaoId", "Id");
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
						request.setAttribute("PaginaAtual", (UsuarioServentiaGrupoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
						request.setAttribute("PosicaoPaginaAtual", "0");
						request.setAttribute("QuantidadePaginas", "0");
						request.setAttribute("lisNomeBusca", lisNomeBusca);
						request.setAttribute("lisDescricao", lisDescricao);
						request.setAttribute("camposHidden", camposHidden);
					} else {
						String stTemp = "";
						stTemp = ServentiaCargone.consultarUsuarioServentiaCargoJSON(ServentiaCargodt.getId_CargoTipo(), ServentiaCargodt.getId_Serventia(), stNomeBusca1, PosicaoPaginaAtual);
													
							enviarJSON(response, stTemp);
							
						
						return;
					}
				}
				break;		
				
			default:
				stId = request.getParameter("Id_ServentiaCargo");
				if (stId != null && !stId.isEmpty()) if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(ServentiaCargodt.getId())) {
					ServentiaCargodt.limpar();
					ServentiaCargodt = ServentiaCargone.consultarId(stId);
				}
				break;

		}

		request.getSession().setAttribute("ServentiaCargodt", ServentiaCargodt);
		request.getSession().setAttribute("ServentiaCargone", ServentiaCargone);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
