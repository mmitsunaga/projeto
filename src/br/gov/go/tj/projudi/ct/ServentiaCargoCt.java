package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.CargoTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaGrupoDt;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.ServentiaCargoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ServentiaCargoCt extends ServentiaCargoCtGen {

	private static final long serialVersionUID = -145116577325329924L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ServentiaCargoDt ServentiaCargodt;
		ServentiaCargoNe ServentiaCargone;
		List tempList = null;

		ProcessoNe Processone = (ProcessoNe) request.getSession().getAttribute("Processone");
		if (Processone == null)
			Processone = new ProcessoNe();
		
		String stNomeBusca1 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		String stId = "";
		int passoEditar = -1;
		String filtroId_Serventia = "";
		String filtroServentia = "";
		String stAcao = "/WEB-INF/jsptjgo/ServentiaCargo.jsp";

		request.setAttribute("tempPrograma", "ServentiaCargo");
		request.setAttribute("tempBuscaId_ServentiaCargo", "Id_ServentiaCargo");
		request.setAttribute("tempBuscaServentiaCargo", "ServentiaCargo");
		request.setAttribute("tempRetorno", "ServentiaCargo");
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		ServentiaCargone = (ServentiaCargoNe) request.getSession().getAttribute("ServentiaCargone");
		if (ServentiaCargone == null) ServentiaCargone = new ServentiaCargoNe();

		ServentiaCargodt = (ServentiaCargoDt) request.getSession().getAttribute("ServentiaCargodt");
		if (ServentiaCargodt == null) ServentiaCargodt = new ServentiaCargoDt();

		ServentiaCargodt.setId(request.getParameter("Id_ServentiaCargo"));
		ServentiaCargodt.setServentiaCargo(request.getParameter("ServentiaCargo"));
		ServentiaCargodt.setId_Serventia(request.getParameter("Id_Serventia"));
		ServentiaCargodt.setServentia(request.getParameter("Serventia"));
		ServentiaCargodt.setId_CargoTipo(request.getParameter("Id_CargoTipo"));
		ServentiaCargodt.setCargoTipo(request.getParameter("CargoTipo"));
		ServentiaCargodt.setCargoTipoCodigo(request.getParameter("Id_CargoTipo"));
		ServentiaCargodt.setId_ServentiaSubtipo(request.getParameter("Id_ServentiaSubtipo"));
		ServentiaCargodt.setServentiaSubtipo(request.getParameter("ServentiaSubtipo"));
		ServentiaCargodt.setServentiaSubtipoCodigo(request.getParameter("ServentiaSubtipoCodigo"));
		ServentiaCargodt.setId_UsuarioServentiaGrupo(request.getParameter("Id_UsuarioServentiaGrupo"));
		ServentiaCargodt.setUsuarioServentiaGrupo(request.getParameter("UsuarioServentiaGrupo"));
		ServentiaCargodt.setQuantidadeDistribuicao(request.getParameter("QuantidadeDistribuicao"));
		ServentiaCargodt.setServentiaCodigo(request.getParameter("ServentiaCodigo"));
		ServentiaCargodt.setCargoTipoCodigo(request.getParameter("CargoTipoCodigo"));
		ServentiaCargodt.setServentiaUsuario(request.getParameter("ServentiaUsuario"));
		ServentiaCargodt.setNomeUsuario(request.getParameter("NomeUsuario"));
		ServentiaCargodt.setGrupoUsuario(request.getParameter("GrupoUsuario"));
		ServentiaCargodt.setGrupoUsuarioCodigo(request.getParameter("GrupoUsuarioCodigo"));
		ServentiaCargodt.setDataInicialSubstituicao(request.getParameter("dataInicio"));
		ServentiaCargodt.setDataFinalSubstituicao(request.getParameter("dataFinal"));
		ServentiaCargodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ServentiaCargodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		if (ServentiaCargodt.isSubstituicao()) ServentiaCargodt.setPrazoAgenda(request.getParameter("PrazoAgendaSub"));
		else ServentiaCargodt.setPrazoAgenda(request.getParameter("PrazoAgenda"));

		if (request.getParameter("PassoEditar") != null && request.getParameter("PassoEditar").length() > 0 && !request.getParameter("PassoEditar").equalsIgnoreCase("null")) passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));
		//if (request.getParameter("Id_Serventia") != null) filtroId_Serventia = request.getParameter("Id_Serventia");		
		//if (request.getParameter("Serventia") != null) filtroServentia = request.getParameter("Serventia");
		
		filtroId_Serventia = ServentiaCargodt.getId_Serventia();
		filtroServentia = ServentiaCargodt.getServentia();

		request.setAttribute("tempFluxo1", filtroId_Serventia);
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);
		
		//campo de controle de criação de estrutura padrão
		request.setAttribute("estruturaPadrao", request.getParameter("estruturaPadrao"));
		

		// Verifica a opção que o usuário informou em substituição
		if (request.getParameter("emSubstituicao") != null) ServentiaCargodt.setSubstituicao(request.getParameter("emSubstituicao"));

		switch (paginaatual) {
			case Configuracao.Localizar: // localizar
				if (request.getParameter("Passo") == null) {
										
					stAcao = "/WEB-INF/jsptjgo/Localizar.jsp";
					String[] lisNomeBusca = {"ServentiaCargo"};
					String[] lisDescricao = {"ServentiaCargo","CargoTipo","Nome Usuário","Serventia"};
					
					request.setAttribute("tempBuscaId", "Id_ServentiaCargo");
					request.setAttribute("tempBuscaDescricao", "ServentiaCargo");
					request.setAttribute("tempBuscaPrograma", "ServentiaCargo");
					request.setAttribute("tempRetorno", "ServentiaCargo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					if(request.getParameter("tempFluxo2")!=null && request.getParameter("tempFluxo2").equalsIgnoreCase("2")) {
						request.setAttribute("Id_Serventia", filtroId_Serventia);
						request.setAttribute("Serventia", filtroServentia);
					} else {
						request.setAttribute("Id_Serventia", "");
						request.setAttribute("Serventia", "");
					}
				} else {
					String stTemp = "";
					if (request.getParameter("tempFluxo1") != null) {
						if (request.getParameter("tempFluxo1").equalsIgnoreCase("undefined")) {
							stTemp = Processone.consultarServentiaCargosJSON(stNomeBusca1, PosicaoPaginaAtual, UsuarioSessao.getUsuarioDt().getId_Serventia(), UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo(), UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo());
						} else {
							filtroId_Serventia = request.getParameter("tempFluxo1");
							stTemp = ServentiaCargone.consultarServentiaCargosJSON(stNomeBusca1, filtroId_Serventia, PosicaoPaginaAtual);
						}
					}
					enviarJSON(response, stTemp);
					return;
				}
				break;
			case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo") == null) {
					String[] lisNomeBusca = {"Serventia"};
					String[] lisDescricao = {"Serventia","Estado"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Serventia");
					request.setAttribute("tempBuscaDescricao", "Serventia");
					request.setAttribute("tempBuscaPrograma", "Serventia");
					request.setAttribute("tempRetorno", "ServentiaCargo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("PaginaAtual", (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					if(request.getParameter("tempFluxo2")!=null && request.getParameter("tempFluxo2").equalsIgnoreCase("2")) {
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Localizar);
						request.setAttribute("tempFluxo2", "2");
					} else if (request.getParameter("tempFluxo2")!=null && request.getParameter("tempFluxo2").equalsIgnoreCase("1")){
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
						ServentiaCargodt.limpar();
					}
				} else {
					String stTemp = "";
					stTemp = ServentiaCargone.consultarDescricaoServentiaJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			// Consulta de SubTipos de Serventia
			case (ServentiaSubtipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo") == null) {
					
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					String[] lisNomeBusca = {"ServentiaSubtipo"};
					String[] lisDescricao = {"ServentiaSubtipo"};
					
					request.setAttribute("tempBuscaId", "Id_ServentiaSubtipo");
					request.setAttribute("tempBuscaDescricao", "ServentiaSubtipo");
					request.setAttribute("tempBuscaPrograma", "ServentiaSubtipo");
					request.setAttribute("tempRetorno", "ServentiaCargo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ServentiaSubtipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					stTemp = ServentiaCargone.consultarDescricaoServentiaSubtipoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
				
			case (CargoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo") == null) {
					
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					String[] lisNomeBusca = {"CargoTipo"};
					String[] lisDescricao = {"CargoTipo"};
					request.setAttribute("tempBuscaId", "Id_CargoTipo");
					request.setAttribute("tempBuscaDescricao", "CargoTipo");
					request.setAttribute("tempBuscaPrograma", "CargoTipo");
					request.setAttribute("tempRetorno", "ServentiaCargo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (CargoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					stTemp = ServentiaCargone.consultarDescricaoCargoTipoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			// Função utilizada apenas para limpar filtro de serventia na consulta
			case Configuracao.LocalizarAutoPai:
				request.setAttribute("id_Serventia", "");
				request.setAttribute("Serventia", "");
				this.executar(request, response, UsuarioSessao, Configuracao.Localizar, tempNomeBusca, PosicaoPaginaAtual);
				return;
	
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
						request.setAttribute("tempRetorno", "ServentiaCargo");
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
			case Configuracao.Editar:
				// Significa que está vindo da consulta de cargo e deve retornar para a listagem de cargos já filtrado
				if (passoEditar == 1) {
					this.executar(request, response, UsuarioSessao, Configuracao.Localizar, tempNomeBusca, PosicaoPaginaAtual);
					return;
				} else {
					stId = request.getParameter("Id_ServentiaCargo");
					if (stId != null && !stId.equalsIgnoreCase("")) {
						ServentiaCargodt.limpar();
						ServentiaCargodt = ServentiaCargone.consultarId(stId);
						if (ServentiaCargodt.getDataInicialSubstituicao() != null && ServentiaCargodt.getDataInicialSubstituicao().length() > 0 && ServentiaCargodt.getDataFinalSubstituicao() != null && ServentiaCargodt.getDataFinalSubstituicao().length() > 0) {
							ServentiaCargodt.setSubstituicao("true");
						}
					}
					if (ServentiaCargodt.getId_Serventia() != null && !ServentiaCargodt.getId_Serventia().equalsIgnoreCase("")) {
						tempList = ServentiaCargone.consultarServentiaCargos(ServentiaCargodt.getId_Serventia());
						if (tempList.size() > 0) {
							ServentiaCargodt.setListaCargosServentia(tempList);
						}
					}
	
				}
				break;
	
			// Prepara cópia de Cargo
			case Configuracao.Curinga6:
				ServentiaCargodt.setId("");
				ServentiaCargodt.setUsuarioServentiaGrupo("");
				ServentiaCargodt.setId_UsuarioServentiaGrupo("null");
				request.setAttribute("MensagemOk", "Cargo copiado.");
				break;
				
			// Prepara criação da Estrutura Padrão
			case Configuracao.Curinga7:
				if(ServentiaCargodt.getId_Serventia() == null || ServentiaCargodt.getId_Serventia().equals("")){
					request.setAttribute("MensagemErro", "Selecione a serventia antes de criar a estrutura padrão.");
					request.setAttribute("estruturaPadrao", "0");
					break;
				}
				if(ServentiaCargodt.getListaCargosServentia() != null && !ServentiaCargodt.getListaCargosServentia().isEmpty()) {
					request.setAttribute("MensagemErro", "A estrutura padrão só pode ser criada em serventias que não possuem cargos cadastrados.");
					request.setAttribute("estruturaPadrao", "0");
					break;
				}
				request.setAttribute("estruturaPadrao", "1");
				request.setAttribute("MensagemOk", "Estrutura validada com sucesso. Clique em SALVAR para gravar a criação.");
				break;

			case Configuracao.Excluir:
				// Confirma Desativação ou Ativação do Servidor Judiciário
				if (ServentiaCargodt.getId() != null && ServentiaCargodt.getId().length() > 0) {
					stAcao = "/WEB-INF/jsptjgo/CargoServentiaMensagemConfirmacao.jsp";
					request.setAttribute("serventiaCargoDt", ServentiaCargone.consultarServentiaCargoId(ServentiaCargodt.getId()));
				} else {
					stAcao = "/WEB-INF/jsptjgo/ServentiaCargo.jsp";
					paginaatual = Configuracao.Editar;
					request.setAttribute("MensagemErro", "Não foi possível obter Cargo da Serventia para exclusão.");
				}
				break;
	
			case Configuracao.ExcluirResultado:
				String mensagemRetorno = "";
				String idServentia = ServentiaCargodt.getId_Serventia();
				String serventia = ServentiaCargodt.getServentia();
				//Validando se pode excluir o cargo. Passa-se o parâmetro "E" indicando que é exclusão de cargo.
				mensagemRetorno = ServentiaCargone.verificarDesabilitacaoCargo(ServentiaCargodt, "E");
				if(mensagemRetorno.equals("")) {
					if (ServentiaCargodt.getId() != null && ServentiaCargodt.getId().length() > 0) {
						ServentiaCargone.excluir(ServentiaCargodt);
						mensagemRetorno = "Cargo da Serventia excluído com sucesso.";
					} else
						request.setAttribute("MensagemErro", "Não foi possível obter Cargo da Serventia para exclusão.");
					
					if (mensagemRetorno.length() > 0) {
						request.setAttribute("MensagemOk", mensagemRetorno);
					}
				} else {
					request.setAttribute("MensagemErro", "Não é possível excluir o cargo. Motivo(s): \n" + mensagemRetorno);
				}
				ServentiaCargodt.setId_Serventia(idServentia);
				ServentiaCargodt.setServentia(serventia);
				// Atualiza lista de cargos
				tempList = ServentiaCargone.consultarServentiaCargos(ServentiaCargodt.getId_Serventia());
				if (tempList.size() > 0)
					ServentiaCargodt.setListaCargosServentia(tempList);
				stAcao = "/WEB-INF/jsptjgo/ServentiaCargo.jsp";
				break;
	
			case Configuracao.SalvarResultado:
				//Se estiver usando a funcionalidade de CRIAR ESTRUTURA PADRÃO
				//não será necessário fazer as validações
				if(request.getParameter("estruturaPadrao") != null && request.getParameter("estruturaPadrao").equals("1")){
					ServentiaCargone.salvarEstruturaPadrao(ServentiaCargodt);
					request.setAttribute("estruturaPadrao", "0");
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso");
					if (ServentiaCargodt.getId_Serventia() != null && !ServentiaCargodt.getId_Serventia().equalsIgnoreCase("")) {
						tempList = ServentiaCargone.consultarServentiaCargos(ServentiaCargodt.getId_Serventia());
						if (tempList.size() > 0)
							ServentiaCargodt.setListaCargosServentia(tempList);
					}
				} else {
					//Para as demais funcionalidades, este fluxo será executado
					String Mensagem = ServentiaCargone.Verificar(ServentiaCargodt);
					if (Mensagem.length() == 0) {
						ServentiaCargone.salvar(ServentiaCargodt);
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso");
						if (ServentiaCargodt.getId_Serventia() != null && !ServentiaCargodt.getId_Serventia().equalsIgnoreCase("")) {
							tempList = ServentiaCargone.consultarServentiaCargos(ServentiaCargodt.getId_Serventia());
							if (tempList.size() > 0)
								ServentiaCargodt.setListaCargosServentia(tempList);
						}
					} else {
						request.setAttribute("MensagemErro", Mensagem);
					}
				}
				 
				break;
	
			default:
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
		}

		request.setAttribute("emSubstituicao", String.valueOf(ServentiaCargodt.isSubstituicao()));
		request.getSession().setAttribute("ServentiaCargodt", ServentiaCargodt);
		request.getSession().setAttribute("ServentiaCargone", ServentiaCargone);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
