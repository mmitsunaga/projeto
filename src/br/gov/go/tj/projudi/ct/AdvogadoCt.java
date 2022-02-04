package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AdvogadoDt;
import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.projudi.dt.CidadeDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.RgOrgaoExpedidorDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaOabDt;
import br.gov.go.tj.projudi.ne.LogNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.ne.UsuarioServentiaOabNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class AdvogadoCt extends Controle {

	private static final long serialVersionUID = -4405365774398240654L;

	@Override
	public int Permissao() {
		return AdvogadoDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		UsuarioDt Advogadodt;
		UsuarioNe Advogadone;
		
		UsuarioServentiaOabDt UsuarioServentiaOabdt;
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		String stNomeBusca3 = "";
		String stNomeBusca4 = "";
		String stNomeBusca5 = "";
		List tempList = null;
		String Mensagem = "";
		String stId = "";
		String stAcao = "/WEB-INF/jsptjgo/Advogado.jsp";

		request.setAttribute("tempPrograma", "Advogado");
		request.setAttribute("tempBuscaId_Usuario", "Id_Usuario");
		request.setAttribute("tempBuscaId_UsuarioServentia", "Id_UsuarioServentia");
		request.setAttribute("tempBuscaUsuario", "Usuario");
		request.setAttribute("Curinga", "vazio");
		request.setAttribute("tempRetorno", "Advogado");

		Advogadone = (UsuarioNe) request.getSession().getAttribute("Advogadone");
		if (Advogadone == null) Advogadone = new UsuarioNe();

		Advogadodt = (UsuarioDt) request.getSession().getAttribute("Advogadodt");
		if (Advogadodt == null) Advogadodt = new AdvogadoDt();

		UsuarioServentiaOabdt = (UsuarioServentiaOabDt) request.getSession().getAttribute("UsuarioServentiaOabdt");
		if (UsuarioServentiaOabdt == null) UsuarioServentiaOabdt = new UsuarioServentiaOabDt();

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		if(request.getParameter("nomeBusca3") != null) stNomeBusca3 = request.getParameter("nomeBusca3");
		if(request.getParameter("nomeBusca4") != null) stNomeBusca4 = request.getParameter("nomeBusca4");
		if(request.getParameter("nomeBusca5") != null) stNomeBusca5 = request.getParameter("nomeBusca5");

		setDadosAdvogado(request, Advogadodt, UsuarioServentiaOabdt, UsuarioSessao);

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);
		

		
		switch (paginaatual) {
			case Configuracao.Localizar:
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Nome Completo","Usuário","OAB","RG","CPF"};										
					String[] lisDescricao = {"Nome","Usuário","OAB","Comp. OAB","UF Órgão","RG","CPF"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Usuario");
					request.setAttribute("tempBuscaDescricao", "Usuario");
					request.setAttribute("tempBuscaPrograma", "Advogado");
					request.setAttribute("tempRetorno", "Advogado");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					if (stNomeBusca1.length()==0 && stNomeBusca2.length()==0 && stNomeBusca3.length()==0 && stNomeBusca4.length()==0 && stNomeBusca5.length()==0 ){
						response.sendError(HttpServletResponse.SC_BAD_REQUEST, "É obrigatório informar um dos campos: Usuário, Oab, Nome, CPF ou RG");
						return;
					}
					stTemp = Advogadone.consultarDescricaoAdvogadoJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3, stNomeBusca4, stNomeBusca5, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
				
			case Configuracao.Novo:
				if (request.getParameter("Curinga") != null && request.getParameter("Curinga").equalsIgnoreCase("L")) {
					//Nesse caso o Limpar refere-se ao cadastro de OAB's
					UsuarioServentiaOabdt.limpar();
					Advogadodt.setServentiaUf("");
					Advogadodt.setId_UsuarioServentia("");
					Advogadodt.setId_Serventia("");
					Advogadodt.setServentia("");
				} else {
					//Limpar Advogado
					Advogadodt.limpar();
					UsuarioServentiaOabdt.limpar();
				}
				break;

			case Configuracao.Salvar:
				if (request.getParameter("Curinga").equals("L")) {
					request.setAttribute("Mensagem", "Clique para Confirmar a geração de nova senha de acesso para " + Advogadodt.getNome());
					request.setAttribute("Curinga", "L");
				} else if (request.getParameter("Curinga").equalsIgnoreCase("S")) {
					request.setAttribute("Curinga", "S");
				}
				break;

			case Configuracao.SalvarResultado:
				Advogadodt.setUsuarioServentiaOab(UsuarioServentiaOabdt);

				if (request.getParameter("Curinga").equalsIgnoreCase("S")) {
					Mensagem = Advogadone.verificarServentiaOAB(Advogadodt);
					
					if(!Advogadodt.getId_Serventia().equalsIgnoreCase("") && !Advogadodt.getId().equalsIgnoreCase("")){
						if (Advogadone.verificarHabilitacaoUsuario(Advogadodt)) {
							//Se o usuário estiver cadastrado na serventia, é preciso verificar se está sendo feita alteração da
							//OAB. Se estiver sendo feita a alteração, é preciso liberar. Entrando neste if, quer dizer que não é
							//alteração.
							if(UsuarioServentiaOabdt.getId() == null || UsuarioServentiaOabdt.getId().equalsIgnoreCase("")) {
								request.setAttribute("MensagemErro", "Usuário já está habilitado na Serventia selecionada.");
								break;
							} else {
								UsuarioServentiaOabNe ne = new UsuarioServentiaOabNe();
								UsuarioServentiaOabDt usuarioAtualBanco = ne.consultarId(UsuarioServentiaOabdt.getId());
								if(UsuarioServentiaOabdt.getOabNumero().equals(usuarioAtualBanco.getOabNumero()) 
										&& UsuarioServentiaOabdt.getOabComplemento().equals(usuarioAtualBanco.getOabComplemento())){
									request.setAttribute("MensagemErro", "Usuário já está habilitado na Serventia selecionada.");
									break;
								}
							}
						}
					} else {
						request.setAttribute("MensagemErro", "Preencher os campos Número, Complemento e Serventia corretamente.");
						break;
					}
					
					if (Mensagem.length() == 0) {
						Advogadone.salvarUsuarioServentiaOab(Advogadodt);
						request.setAttribute("Curinga", "OK");
						request.setAttribute("MensagemOk", "Dados da OAB Salvos com Sucesso.");
					}
				} else if (request.getParameter("Curinga").equals("L")) {
					UsuarioSessao.limparSenha(Advogadodt);
					request.setAttribute("MensagemOk", "Senha Alterada com sucesso. (senha 12345)");
				} else {
					Mensagem = Advogadone.VerificarAdvogado(Advogadodt);
					if (Mensagem.length() == 0) {
						String stRetorno = Advogadone.salvarAdvogado(Advogadodt);
						
						request.setAttribute("MensagemOk", "Dados do Advogado Salvos com Sucesso. " + stRetorno);
						request.getSession().removeAttribute("UsuarioAnterior");
					}
				}

				if (Mensagem.length() == 0) {
					UsuarioServentiaOabdt.limpar();
					Advogadodt = Advogadone.consultarAdvogadoId(Advogadodt.getId());
					tempList = Advogadone.consultarServentiaOabAdvogado(Advogadodt.getId());
					if (tempList.size() > 0) Advogadodt.setListaUsuarioServentias(tempList);
				} else request.setAttribute("MensagemErro", Mensagem);
				break;

			// CURINGA = "F" Desativa, CURINGA = "G" Ativa
			case Configuracao.Excluir:
				stId = request.getParameter("Id_UsuarioServentia");
				if (stId != null && stId.length() > 0) {
					stAcao = "/WEB-INF/jsptjgo/AdvogadoMensagemConfirmacao.jsp";
					Advogadodt.setId_UsuarioServentia(stId);
					request.setAttribute("advogadoDt", Advogadone.consultarAdvogadoServentiaId(stId));
					if (request.getParameter("Curinga").equalsIgnoreCase("F")) request.setAttribute("Curinga", "CF");
					else if (request.getParameter("Curinga").equalsIgnoreCase("G")) request.setAttribute("Curinga", "CG");
				} else {
					request.setAttribute("MensagemErro", "Não é possível excluir advogado");
				}
				break;

			// CURINGA = "F" Desativa, CURINGA = "G" Ativa
			case Configuracao.ExcluirResultado:
				if (Advogadodt.getId_UsuarioServentia().length() > 0) {
					if (request.getParameter("Curinga").equalsIgnoreCase("F")) {
						Advogadone.desativarUsuarioServentia(Advogadodt);
						Mensagem = "Advogado(a) Desabilitado(a) na Serventia (OAB) com sucesso.";

					} else if (request.getParameter("Curinga").equalsIgnoreCase("G")) {
						Advogadone.ativarAdvogado(Advogadodt);
						Mensagem = "Advogado(a) Habilitado(a) na Serventia (OAB) com sucesso.";
					}
				} else request.setAttribute("MensagemErro", "Não foi possível obter Serventia/Grupo para Desabilitação");

				if (Mensagem.length() > 0) {
					request.setAttribute("Curinga", "OK");
					request.setAttribute("MensagemOk", Mensagem);
					UsuarioServentiaOabdt.limpar();
					Advogadodt.setId_Serventia("null");
					Advogadodt.setId_Grupo("null");
				}
				// Atualiza lista de serventias e grupos
				Advogadodt = Advogadone.consultarAdvogadoId(Advogadodt.getId());
				tempList = Advogadone.consultarServentiaOabAdvogado(Advogadodt.getId());
				if (tempList.size() > 0) Advogadodt.setListaUsuarioServentias(tempList);
				break;

			//Redireciona para servlet de Documentos do Usuário
			case Configuracao.LocalizarAutoPai:
				if (Advogadodt != null && Advogadodt.getId().length() > 0) {
					request.getSession().setAttribute("usuarioDt", Advogadodt);
					redireciona(response, "UsuarioArquivo?PaginaAtual=" + Configuracao.Novo);
					return;
				} else request.setAttribute("MensagemErro", "Nenhum Usuário foi selecionado");
				break;

			case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar): 
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Serventia"};
					String[] lisDescricao = {"Serventia","Estado"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Serventia");
					request.setAttribute("tempBuscaDescricao", "Serventia");
					request.setAttribute("tempBuscaPrograma", "Serventia");
					request.setAttribute("tempRetorno", "Advogado");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = Advogadone.consultarServentiasHabilitacaoAdvogadoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;

			case (CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):{
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Cidade","Uf"};
					String[] lisDescricao = {"Cidade","Uf"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Cidade");
					request.setAttribute("tempBuscaDescricao", "Cidade");
					request.setAttribute("tempBuscaPrograma", "Cidade");
					request.setAttribute("tempRetorno", "Advogado");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = Advogadone.consultarDescricaoCidadeJSON(stNomeBusca1, stNomeBusca2, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			}

			case (RgOrgaoExpedidorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar): {
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Sigla","Nome"};
					String[] lisDescricao = {"Sigla","Órgão Expedidor","Estado"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_RgOrgaoExpedidor");
					request.setAttribute("tempBuscaDescricao", "RgOrgaoExpedidor");
					request.setAttribute("tempBuscaPrograma", "RgOrgaoExpedidor");
					request.setAttribute("tempRetorno", "Advogado");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", RgOrgaoExpedidorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					stTemp = Advogadone.consultarDescricaoRgOrgaoExpedidorJSON(stNomeBusca1, stNomeBusca2, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			}

			case (BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar): {
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Bairro","Cidade","UF"};
					String[] lisDescricao = {"Bairro","Cidade","UF"};
					String[] camposHidden = {"BairroCidade","BairroUf"};
					request.setAttribute("camposHidden",camposHidden);
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Bairro");
					request.setAttribute("tempBuscaDescricao", "Bairro");
					request.setAttribute("tempBuscaPrograma", "Bairro");
					request.setAttribute("tempRetorno", "Advogado");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else{
					String stTemp = "";
					stTemp = Advogadone.consultarDescricaoBairroJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			}
								
			case (Configuracao.Curinga7): {
				int Passo = 0;
				if (request.getParameter("Passo")!=null){
					Passo = Integer.parseInt((String) request.getParameter("Passo"));
				}
				switch (Passo) {
				//consultar
				case (1): {
//					stAcao = "/WEB-INF/jsptjgo/HabilitacaoProcurador.jsp";
//					String oab = (request.getParameter("OabNumero").toString());
//					String id_serv = (request.getParameter("serventiaCodigoHddn").toString());
//					tempList = Advogadone.consultarAdvogadoPublicoServentia(oab, id_serv);
//					if (tempList.size() > 0) Advogadodt.setListaUsuarioServentias(tempList);
//					break;

					if (request.getParameter("tempFluxo1")==null){
						String[] lisNomeBusca = {"OAB"};
						String[] lisDescricao = {"Nome","OAB", "Complemento", "UF"};
						stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
						request.setAttribute("tempFluxo1", "1");
						request.setAttribute("tempBuscaId", "Id_Usuario");
						request.setAttribute("tempBuscaDescricao", "Usuario");
						request.setAttribute("tempBuscaPrograma", "Serventia");
						request.setAttribute("tempRetorno", "Advogado");
						request.setAttribute("tempDescricaoId", "Id");
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Curinga7);
						request.setAttribute("PaginaAtual", (Configuracao.Curinga7));
						request.setAttribute("PosicaoPaginaAtual", "0");
						request.setAttribute("QuantidadePaginas", "0");
						request.setAttribute("lisNomeBusca", lisNomeBusca);
						request.setAttribute("lisDescricao", lisDescricao);
					}else{
						String stTemp = "";
						stTemp = Advogadone.consultarAdvogadoPublicoJSON(stNomeBusca1, PosicaoPaginaAtual);
													
							enviarJSON(response, stTemp);
							
						
						return;
					}
					break;
				}
				
				//incluir procurador
				case (2): {
					stAcao = "/WEB-INF/jsptjgo/HabilitacaoProcurador.jsp";
					tempList = Advogadone.consultarAdvogadoPublicoServentiaJSON(UsuarioSessao.getUsuarioDt().getId_Serventia());
					if (null != tempList && tempList.size() > 0) request.setAttribute("listaProcuradores", tempList);
					
					
					UsuarioServentiaOabdt.setOabComplemento(request.getParameter("OabComplemento"));
					UsuarioServentiaOabdt.setOabNumero(request.getParameter("OabNumero"));
					
					
//					UsuarioServentiaOabdt.setId(UsuarioSessao.getUsuarioDt().getId_Serventia());
					UsuarioServentiaOabdt.setId_UsuarioServentia(request.getParameter("serventiaCodigoHddn").toString());
					
					
					
					
					if(null != request.getParameter("Id_Usuario") && request.getParameter("Id_Usuario").length() > 0){
						Advogadodt.setId(request.getParameter("Id_Usuario"));
						UsuarioServentiaOabdt.setOabComplemento(request.getParameter("OabComplementoRe"));
						UsuarioServentiaOabdt.setOabNumero(request.getParameter("OabNumeroRe"));
						UsuarioServentiaOabdt.setId_UsuarioServentia(request.getParameter("Id_UsuarioServentia").toString());
					}
					
					
					Advogadodt.setUsuarioServentiaOab(UsuarioServentiaOabdt);
					
					
					Advogadodt.setId_Serventia(UsuarioSessao.getUsuarioDt().getId_Serventia());
					
					
					Mensagem = Advogadone.verificarServentiaOAB(Advogadodt);
					
					
//					String oab = (request.getParameter("OabNumero").toString());
//					String id_serv = (UsuarioSessao.getUsuarioDt().getServentiaCodigo());
//					Advogadodt.setId_Serventia(id_serv);
//					Advogadodt.setId("4");
						
						if(!Advogadodt.getId_Serventia().equalsIgnoreCase("") && !Advogadodt.getId().equalsIgnoreCase("")){
							if (Advogadone.verificarHabilitacaoUsuario(Advogadodt)) {
								//Se o usuário estiver cadastrado na serventia, é preciso verificar se está sendo feita alteração da
								//OAB. Se estiver sendo feita a alteração, é preciso liberar. Entrando neste if, quer dizer que não é
								//alteração.
								if(UsuarioServentiaOabdt.getId() == null || UsuarioServentiaOabdt.getId().equalsIgnoreCase("")) {
									request.setAttribute("MensagemErro", "Usuário já está habilitado na Serventia selecionada.");
									break;
								} else {
									UsuarioServentiaOabNe ne = new UsuarioServentiaOabNe();
									UsuarioServentiaOabDt usuarioAtualBanco = ne.consultarId(UsuarioServentiaOabdt.getId());
									if(UsuarioServentiaOabdt.getOabNumero().equals(usuarioAtualBanco.getOabNumero()) 
											&& UsuarioServentiaOabdt.getOabComplemento().equals(usuarioAtualBanco.getOabComplemento())){
										if(null != request.getParameter("Id_Usuario") && request.getParameter("Id_Usuario").length() > 0){
											Advogadone.ativarAdvogado(Advogadodt);
											request.setAttribute("Curinga", "OK");
											request.setAttribute("MensagemOk", "Usuário reabilitado na Serventia selecionada.");
											


											
											tempList = Advogadone.consultarAdvogadoPublicoServentiaJSON(UsuarioSessao.getUsuarioDt().getId_Serventia());
											if (null != tempList && tempList.size() > 0) request.setAttribute("listaProcuradores", tempList);
											break;
										}
										request.setAttribute("MensagemErro", "Usuário já está habilitado na Serventia selecionada.");
										break;
									}
								}
							}
						} else {
							request.setAttribute("MensagemErro", "Preencher os campos Número, Complemento e Serventia corretamente.");
							break;
						}						
						if (Mensagem.length() == 0) {
							Advogadone.salvarUsuarioServentiaOab(Advogadodt);
							request.setAttribute("Curinga", "OK");
							request.setAttribute("MensagemOk", "Dados da OAB Salvos com Sucesso.");
							
							tempList = Advogadone.consultarAdvogadoPublicoServentiaJSON(UsuarioSessao.getUsuarioDt().getId_Serventia());
							if (null != tempList && tempList.size() > 0) request.setAttribute("listaProcuradores", tempList);
						}
					break;
					}
				
				case (3): {
//					stAcao = "/WEB-INF/jsptjgo/HabilitacaoProcurador.jsp";
//					tempList = Advogadone.consultarAdvogadoPublicoServentiaJSON(UsuarioSessao.getUsuarioDt().getId_Serventia());
//					if (null != tempList && tempList.size() > 0) request.setAttribute("listaProcuradores", tempList);
					
					
//					UsuarioServentiaOabdt.setOabComplemento(request.getParameter("OabComplemento"));
//					UsuarioServentiaOabdt.setOabNumero(request.getParameter("OabNumero"));
//					Advogadodt.setUsuarioServentiaOab(UsuarioServentiaOabdt);
//					Mensagem = Advogadone.verificarServentiaOAB(Advogadodt);
//					Advogadodt.setId_Serventia(UsuarioSessao.getUsuarioDt().getId_Serventia());
					
					Advogadodt.setId_UsuarioServentia(request.getParameter("Id_UsuarioServentia").toString());
					
					if (Advogadodt.getId_UsuarioServentia().length() > 0) {
							Advogadone.desativarUsuarioServentia(Advogadodt);
							Mensagem = "Advogado(a) Desabilitado(a) na Serventia (OAB) com sucesso.";
					} else request.setAttribute("MensagemErro", "Não foi possível obter Serventia/Grupo para Desabilitação");

					if (Mensagem.length() > 0) {
						request.setAttribute("Curinga", "OK");
						request.setAttribute("MensagemOk", Mensagem);
						UsuarioServentiaOabdt.limpar();
						Advogadodt.setId_Serventia("null");
						Advogadodt.setId_Grupo("null");
					}
					
					stAcao = "/WEB-INF/jsptjgo/HabilitacaoProcurador.jsp";
					tempList = Advogadone.consultarAdvogadoPublicoServentiaJSON(UsuarioSessao.getUsuarioDt().getId_Serventia());
					if (null != tempList && tempList.size() > 0) request.setAttribute("listaProcuradores", tempList);

					break;
					}
				//abre pagina
				default: {
					stAcao = "/WEB-INF/jsptjgo/HabilitacaoProcurador.jsp";
					tempList = Advogadone.consultarAdvogadoPublicoServentiaJSON(UsuarioSessao.getUsuarioDt().getId_Serventia());
					if (null != tempList && tempList.size() > 0) request.setAttribute("listaProcuradores", tempList);
					
					break;
					}
				}				
			}
			
			// Consulta o log de advogado (Botao 'Visualizar Log')
			case Configuracao.Curinga8:
	
				List listaLogDt = null;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				String ano = sdf.format(new Date());
				LogNe Logne = new LogNe();
	
				stAcao = "/WEB-INF/jsptjgo/AdvogadoLog.jsp";
	
				if (request.getSession().getAttribute("Logne") != null) {
					Logne = (LogNe) request.getSession().getAttribute("Logne");
				}	
				if (request.getParameter("AnoBusca") != null) {
					ano = request.getParameter("AnoBusca");
				}	
				listaLogDt = (List<LogDt>) Logne.obterListaLog(Advogadodt.getId(), ano, "Advogado");	
				request.setAttribute("listaLogDt", listaLogDt);
				request.setAttribute("AnoBuscaSelecionado", ano);	
				break;
				
			case Configuracao.Curinga9:
				
				//LOGICA PARA MARCAR OU DESMARCAR ADVOGADO/PROCURADOR EM UMA SERVENTIA COMO MASTER *****************************************************************************************************
				//(HAP3)->DESMARCAR MASTER   (HAP2)->MARCAR MASTER   (HAP5)->Confirmar DESMARCAR MASTER   (HAP4)->Confirmar MARCAR MASTER
				if (request.getParameter("tempFluxo1")!=null && (request.getParameter("tempFluxo1").equalsIgnoreCase("HAP2") || request.getParameter("tempFluxo1").equalsIgnoreCase("HAP3") )){
					request.getSession().removeAttribute("advogadoDt");
					Advogadodt.limpar();
					request.setAttribute("advogadoDt", Advogadone.consultarAdvogadoServentiaId(request.getParameter("Id_UsuarioServentia")));
					request.setAttribute("tempFluxo1", request.getParameter("tempFluxo1"));
					stAcao = "/WEB-INF/jsptjgo/AdvogadoProcuradorMasterMensagemConfirmacao.jsp";
				
				} else if (request.getParameter("tempFluxo1")!=null && (request.getParameter("tempFluxo1").equalsIgnoreCase("HAP4") || request.getParameter("tempFluxo1").equalsIgnoreCase("HAP5")) ){
					request.getSession().removeAttribute("advogadoDt");
					Advogadodt.limpar();
					
					String id_UsuarioServentia_Oab = request.getParameter("Id_UsuarioServentia_Oab");
					String statusAtualMaster = request.getParameter("statusAtualMaster");
					
					if (request.getParameter("tempFluxo1").equalsIgnoreCase("HAP4")){
						Advogadone.alterarStatusAdvovogadoMaster(UsuarioSessao.getUsuarioDt(), statusAtualMaster, UsuarioServentiaOabDt.MASTER, id_UsuarioServentia_Oab);
					
					} else if (request.getParameter("tempFluxo1").equalsIgnoreCase("HAP5")) {
						Advogadone.alterarStatusAdvovogadoMaster(UsuarioSessao.getUsuarioDt(), statusAtualMaster, UsuarioServentiaOabDt.NOT_MASTER, id_UsuarioServentia_Oab);
					}
					
					tempList = Advogadone.consultarDescricaoAdvogadosProcuradoresServentia( UsuarioSessao.getUsuarioDt().getId_Serventia());
					stAcao = "/WEB-INF/jsptjgo/AdvogadoProcuradorLocalizar.jsp";
					request.setAttribute("MensagemOk", "Operação realizada com Sucesso. ");
					
					if (tempList.size() > 0) {
						request.setAttribute("ListaAdvogados", tempList);
						request.setAttribute("tempFluxo1", "HAP1");
						request.setAttribute("PaginaAtual", Configuracao.Curinga9);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", 0);
					} else {
						request.setAttribute("MensagemErro", "Dados Não Localizados");
					}
					
				}
				// FIM LOGICA PARA MARCAR OU DESMARCAR ADVOGADO/PROCURADOR EM UMA SERVENTIA COMO MASTER ***********************************************************************************************************************  
				
				//LOGICA PARA ATIVAR OU INATIVAR ADVOGADO/PROCURADOR EM UMA SERVENTIA *****************************************************************************************************************************************
				//(HS2)->Inativar   (HS3)->Ativar   (HS4)->Confirmar Inativar   (HS5)->Confirmar Ativar
				else if (request.getParameter("tempFluxo1")!=null && (request.getParameter("tempFluxo1").equalsIgnoreCase("HS2") || request.getParameter("tempFluxo1").equalsIgnoreCase("HS3") )){
					request.getSession().removeAttribute("advogadoDt");
					Advogadodt.limpar();
					request.setAttribute("advogadoDt", Advogadone.consultarAdvogadoServentiaId(request.getParameter("Id_UsuarioServentia")));
					request.setAttribute("tempFluxo1", request.getParameter("tempFluxo1"));
					stAcao = "/WEB-INF/jsptjgo/AdvogadoProcuradorMasterMensagemConfirmacao.jsp";
					
					if (request.getParameter("tempFluxo1").equalsIgnoreCase("HS2")) {
						int qtd = 	Advogadone.possuiIntimacoesCitacoesAbertasUsuario(request.getParameter("Id_UsuarioServentia"));
						if(qtd>0){
							request.setAttribute("MensagemErro", "ALERTA: O usúario possuí "+qtd+" pendência(s) aguardando Leitura! (Intimações ou Citações)");
						}
					}
				
				} else if (request.getParameter("tempFluxo1")!=null && (request.getParameter("tempFluxo1").equalsIgnoreCase("HS4") || request.getParameter("tempFluxo1").equalsIgnoreCase("HS5")) ){
					request.getSession().removeAttribute("advogadoDt");
					Advogadodt.limpar();
					
					UsuarioServentiaDt usuarioServentiaDt = new UsuarioServentiaDt();
					usuarioServentiaDt.setId(request.getParameter("Id_UsuarioServentia"));
					usuarioServentiaDt.setId_UsuarioLog(UsuarioSessao.getUsuarioDt().getId());
					usuarioServentiaDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
					
					String statusAtualUsuarioServentia = request.getParameter("statusAtualUsuarioServentia");
					
					if (request.getParameter("Id_UsuarioServentia") == null || request.getParameter("Id_UsuarioServentia").length() == 0 || statusAtualUsuarioServentia == null || statusAtualUsuarioServentia.length()==0){
						request.setAttribute("MensagemErro", "Dados insuficientes para realizar operação!");
					
					} else if (request.getParameter("tempFluxo1").equalsIgnoreCase("HS4")){
						Advogadone.ativarDesativarUsuarioServentiaAdvogadoProcurador(usuarioServentiaDt, Integer.valueOf(statusAtualUsuarioServentia), UsuarioServentiaDt.INATIVO, LogTipoDt.DesabilitacaoUsuario);
					
					} else if (request.getParameter("tempFluxo1").equalsIgnoreCase("HS5")) {
						Advogadone.ativarDesativarUsuarioServentiaAdvogadoProcurador(usuarioServentiaDt, Integer.valueOf(statusAtualUsuarioServentia), UsuarioServentiaDt.ATIVO, LogTipoDt.HabilitacaoUsuario);
					}
					
					tempList = Advogadone.consultarDescricaoAdvogadosProcuradoresServentia( UsuarioSessao.getUsuarioDt().getId_Serventia());
					stAcao = "/WEB-INF/jsptjgo/AdvogadoProcuradorLocalizar.jsp";
					request.setAttribute("MensagemOk", "Operação realizada com Sucesso. ");
					
					if (tempList.size() > 0) {
						request.setAttribute("ListaAdvogados", tempList);
						request.setAttribute("tempFluxo1", "HAP1");
						request.setAttribute("PaginaAtual", Configuracao.Curinga9);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", 0);
					} else {
						request.setAttribute("MensagemErro", "Dados Não Localizados");
					}
					
				}
				// FIM LOGICA PARA ATIVAR OU INATIVAR ADVOGADO/PROCURADOR EM UMA SERVENTIA****************************************************************************************************************************************
				
				//LOGICA PARA HABILITAR ADVOGADO/PROCURADOR EM UMA SERVENTIA *****************************************************************************************************************************************************
				//(LA1)->IR PARA TELA DE HABILITACAO ADVOGADO/PROCURADOR   (LA2)->CONFIRMAR HABILITACAO ADVOGADO/PROCURADOR   (LA3)->CONFIRMACAO DE HABILITACAO ADVOGADO/PROCURADOR
				else if (request.getParameter("tempFluxo1")!=null && (request.getParameter("tempFluxo1").equalsIgnoreCase("LA1"))){
					request.getSession().removeAttribute("advogadoDt");
					Advogadodt.limpar();
					stAcao = "/WEB-INF/jsptjgo/AdvogadoProcuradorHabilitacao.jsp";
					
				} else	if (request.getParameter("tempFluxo1")!=null && (request.getParameter("tempFluxo1").equalsIgnoreCase("LA2"))){
					Mensagem = Advogadone.VerificarAdvogadoHabilitacao(Advogadodt);
					if (Mensagem.length()==0){
						Advogadodt = Advogadone.consultarAdvogadoOabCPF(Advogadodt.getOabNumero(), Advogadodt.getOabComplemento(), Advogadodt.getOabEstado(), Advogadodt.getCpf());
						
						if (Advogadodt != null && Advogadone.verificarHabilitacaoUsuarioEscritorioJuridico(Advogadodt.getId(), UsuarioSessao.getId_Serventia())) {
							request.setAttribute("MensagemErro", "Usuário já está habilitado na serventia: "+UsuarioSessao.getServentia());
						} else if (Advogadodt != null ){
							request.setAttribute("Curinga", "LA2");
							request.setAttribute("MensagemConfirmacao", "Confirmar Habilitação");
							request.setAttribute("Mensagem", "Confirmar Habilitação de "+Advogadodt.getNome().toUpperCase()+" na serventia: "+ UsuarioSessao.getUsuarioDt().getServentia());
							
						} else {
							request.getSession().removeAttribute("advogadoDt");
							Advogadodt = new UsuarioDt();
							request.setAttribute("MensagemErro","Usuário não foi encontrado, confirme os dados!");
							stAcao = "/WEB-INF/jsptjgo/AdvogadoProcuradorHabilitacao.jsp";
						}
						
					} else {
						request.setAttribute("MensagemErro", Mensagem);
					}
					stAcao = "/WEB-INF/jsptjgo/AdvogadoProcuradorHabilitacao.jsp";
					
				} else if(request.getParameter("tempFluxo1")!=null && (request.getParameter("tempFluxo1").equalsIgnoreCase("LA3"))){
					Advogadone.salvarHabilitacaoAdvogadoProcuradorServentia(Advogadodt, UsuarioSessao.getUsuarioDt());
					request.getSession().removeAttribute("advogadoDt");
					Advogadodt.limpar();
					tempList = Advogadone.consultarDescricaoAdvogadosProcuradoresServentia( UsuarioSessao.getUsuarioDt().getId_Serventia());
					stAcao = "/WEB-INF/jsptjgo/AdvogadoProcuradorLocalizar.jsp";
					if (tempList.size() > 0) {
						request.setAttribute("ListaAdvogados", tempList);
						request.setAttribute("tempFluxo1", "HAP1");
						request.setAttribute("PaginaAtual", Configuracao.Curinga9);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", 0);
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					} else {
						request.setAttribute("MensagemErro", "Dados Não Localizados");
					}
				//FIM LOGICA PARA HABILITAR ADVOGADO/PROCURADOR EM UMA SERVENTIA ************************************************************************************************************************	
				
				//TELA DEFAULT DE HABILITAR, (ATIVAR/INATIVAR) E (MARCAR/DESMARCAR MASTER) DE ADVOGADO/PROCURADOR NA SERVENTIA -> LISTA TODOS OS ADVOGADOS/PROCURADORES DA SERVENTIA ********************
				} else {
					request.getSession().removeAttribute("advogadoDt");
					Advogadodt.limpar();
					tempList = Advogadone.consultarDescricaoAdvogadosProcuradoresServentia( UsuarioSessao.getUsuarioDt().getId_Serventia());
					stAcao = "/WEB-INF/jsptjgo/AdvogadoProcuradorLocalizar.jsp";
					if (tempList.size() > 0) {
						request.setAttribute("ListaAdvogados", tempList);
						request.setAttribute("tempFluxo1", "HAP1");
						request.setAttribute("PaginaAtual", Configuracao.Curinga9);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", 0);
					} else {
						request.setAttribute("MensagemErro", "Dados Não Localizados");
					}
				}
				//FIM TELA DEFAULT DE HABILITAR, (ATIVAR/INATIVAR) E (MARCAR/DESMARCAR MASTER) DE ADVOGADO/PROCURADOR -> LISTA TODOS OS ADVOGADOS/PROCURADORES DA SERVENTIA *********************************
				
				break;

			default:
				stId = request.getParameter("Id_Usuario");
				//if (stId != null && !stId.isEmpty()) if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(Advogadodt.getId())) {
				if (stId != null && !stId.isEmpty()) if (!stId.equalsIgnoreCase("")) {
					Advogadodt.limpar();
					UsuarioServentiaOabdt.limpar();
					Advogadodt = Advogadone.consultarAdvogadoId(stId);
					tempList = Advogadone.consultarServentiaOabAdvogado(stId);
					if (tempList.size() > 0) Advogadodt.setListaUsuarioServentias(tempList);
				}
				stId = request.getParameter("Id_UsuarioServentia");
				if (stId != null && !stId.isEmpty()) if (!stId.equalsIgnoreCase("")) {
					Advogadodt.limpar();
					Advogadodt = Advogadone.consultarAdvogadoServentiaId(stId);
					UsuarioServentiaOabdt = Advogadodt.getUsuarioServentiaOab();
					tempList = Advogadone.consultarServentiaOabAdvogado(Advogadodt.getId());
					if (tempList.size() > 0) Advogadodt.setListaUsuarioServentias(tempList);
				}
				break;
		}

		request.getSession().setAttribute("Advogadodt", Advogadodt);
		request.getSession().setAttribute("Advogadone", Advogadone);
		request.getSession().setAttribute("Enderecodt", Advogadodt.getEnderecoUsuario());
		request.getSession().setAttribute("UsuarioServentiaOabdt", UsuarioServentiaOabdt);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	/**
	 * Seta dados do advogado e seu endereço
	 * @param request
	 * @param Advogadodt
	 * @param UsuarioServentiaOabdt
	 * @param usuarioSessao
	 */
	protected void setDadosAdvogado(HttpServletRequest request, UsuarioDt Advogadodt, UsuarioServentiaOabDt UsuarioServentiaOabdt, UsuarioNe usuarioSessao) {
		//Advogadodt.setUsuario(request.getParameter("Usuario"));
		Advogadodt.setId_Naturalidade(request.getParameter("Id_Cidade"));
		Advogadodt.setNaturalidade(request.getParameter("Cidade"));
		Advogadodt.setId_Endereco(request.getParameter("Id_Endereco"));
		Advogadodt.setEndereco(request.getParameter("Endereco"));
		Advogadodt.setId_RgOrgaoExpedidor(request.getParameter("Id_RgOrgaoExpedidor"));
		Advogadodt.setRgOrgaoExpedidor(request.getParameter("RgOrgaoExpedidor"));
		Advogadodt.setSenha(request.getParameter("Senha"));
		Advogadodt.setNome(request.getParameter("Nome"));
		Advogadodt.setSexo(request.getParameter("Sexo"));
		Advogadodt.setDataNascimento(request.getParameter("DataNascimento"));
		Advogadodt.setRg(request.getParameter("Rg"));
		Advogadodt.setRgDataExpedicao(request.getParameter("RgDataExpedicao"));
		Advogadodt.setCpf(request.getParameter("Cpf"));
		Advogadodt.setDataCadastro(request.getParameter("DataCadastro"));
		Advogadodt.setId_Serventia(request.getParameter("Id_Serventia"));
		Advogadodt.setServentia(request.getParameter("Serventia"));
		Advogadodt.setDataAtivo(request.getParameter("DataAtivo"));
		Advogadodt.setDataExpiracao(request.getParameter("DataExpiracao"));
		Advogadodt.setEMail(request.getParameter("EMail"));
		Advogadodt.setTelefone(request.getParameter("Telefone"));
		Advogadodt.setCelular(request.getParameter("Celular"));
		Advogadodt.setId_UsuarioLog(usuarioSessao.getId_Usuario());
		Advogadodt.setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());

		//OAB ADVOGADO
		Advogadodt.setServentiaUf(request.getParameter("EstadoOab"));
		UsuarioServentiaOabdt.setOabNumero(request.getParameter("OabNumero"));
		UsuarioServentiaOabdt.setOabComplemento(request.getParameter("OabComplemento"));
		
		//novos dados
		Advogadodt.setOabNumero(request.getParameter("OabNumero"));
		Advogadodt.setOabComplemento(request.getParameter("OabComplemento"));
		Advogadodt.setOabEstado(request.getParameter("EstadoOab"));
		
		// ENDEREÇO DO USUÁRIO
		Advogadodt.getEnderecoUsuario().setLogradouro(request.getParameter("Logradouro"));
		Advogadodt.getEnderecoUsuario().setNumero(request.getParameter("Numero"));
		Advogadodt.getEnderecoUsuario().setComplemento(request.getParameter("Complemento"));
		Advogadodt.getEnderecoUsuario().setId_Bairro(request.getParameter("Id_Bairro"));
		Advogadodt.getEnderecoUsuario().setBairro(request.getParameter("Bairro"));
		Advogadodt.getEnderecoUsuario().setId_Cidade(request.getParameter("BairroId_Cidade"));
		Advogadodt.getEnderecoUsuario().setCidade(request.getParameter("BairroCidade"));
		Advogadodt.getEnderecoUsuario().setUf(request.getParameter("BairroUf"));
		Advogadodt.getEnderecoUsuario().setCep(request.getParameter("Cep"));
	}

}
