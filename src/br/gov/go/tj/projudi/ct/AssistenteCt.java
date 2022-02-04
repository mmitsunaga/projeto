package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AssistenteDt;
import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.projudi.dt.CidadeDt;
import br.gov.go.tj.projudi.dt.RgOrgaoExpedidorDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class AssistenteCt extends Controle {

    private static final long serialVersionUID = 4962099742365181790L;


	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		UsuarioDt Assistentedt;
		UsuarioNe Assistentene;
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		String stNomeBusca3 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		if(request.getParameter("nomeBusca3") != null) stNomeBusca3 = request.getParameter("nomeBusca3");
		
		List tempList = null;
		String Mensagem = "";
		String stId = "";
		String stAcao = "/WEB-INF/jsptjgo/Assistente.jsp";

		request.setAttribute("tempPrograma", "Assessor");
		request.setAttribute("tempBuscaId_Usuario", "Id_Usuario");
		request.setAttribute("tempBuscaId_UsuarioServentiaGrupo", "Id_UsuarioServentiaGrupo");
		request.setAttribute("tempBuscaUsuario", "Usuario");
		request.setAttribute("tempRetorno", "Assistente");
		request.setAttribute("tempFluxo1", "vazio");

		Assistentene = (UsuarioNe) request.getSession().getAttribute("Assistentene");
		if (Assistentene == null) Assistentene = new UsuarioNe();

		Assistentedt = (UsuarioDt) request.getSession().getAttribute("Assistentedt");
		if (Assistentedt == null) Assistentedt = new AssistenteDt();

		Assistentedt.setUsuario(request.getParameter("Cpf"));
		//ID USUARIOSERVENTIA CHEFE
		Assistentedt.setId_UsuarioServentiaChefe(UsuarioSessao.getUsuarioDt().getId_UsuarioServentia());
		Assistentedt.setGrupoUsuarioChefe(UsuarioSessao.getUsuarioDt().getGrupoCodigo());
		Assistentedt.setId_Serventia(UsuarioSessao.getUsuarioDt().getId_Serventia());
		Assistentedt.setId_Naturalidade(request.getParameter("Id_Cidade"));
		Assistentedt.setNaturalidade(request.getParameter("Cidade"));
		Assistentedt.setId_Endereco(request.getParameter("Id_Endereco"));
		Assistentedt.setEndereco(request.getParameter("Endereco"));
		Assistentedt.setId_RgOrgaoExpedidor(request.getParameter("Id_RgOrgaoExpedidor"));
		Assistentedt.setRgOrgaoExpedidor(request.getParameter("RgOrgaoExpedidor"));
		Assistentedt.setSenha(request.getParameter("Senha"));
		Assistentedt.setNome(request.getParameter("Nome"));
		Assistentedt.setSexo(request.getParameter("Sexo"));
		Assistentedt.setDataNascimento(request.getParameter("DataNascimento"));
		Assistentedt.setRg(request.getParameter("Rg"));
		Assistentedt.setRgDataExpedicao(request.getParameter("RgDataExpedicao"));
		Assistentedt.setCpf(request.getParameter("Cpf"));
		Assistentedt.setDataCadastro(request.getParameter("DataCadastro"));
		Assistentedt.setDataAtivo(request.getParameter("DataAtivo"));
		Assistentedt.setDataExpiracao(request.getParameter("DataExpiracao"));
		Assistentedt.setEMail(request.getParameter("EMail"));
		Assistentedt.setTelefone(request.getParameter("Telefone"));
		Assistentedt.setCelular(request.getParameter("Celular"));
		Assistentedt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Assistentedt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		// ENDEREÇO DO USUÁRIO
		Assistentedt.getEnderecoUsuario().setLogradouro(request.getParameter("Logradouro"));
		Assistentedt.getEnderecoUsuario().setNumero(request.getParameter("Numero"));
		Assistentedt.getEnderecoUsuario().setComplemento(request.getParameter("Complemento"));
		Assistentedt.getEnderecoUsuario().setId_Bairro(request.getParameter("Id_Bairro"));
		Assistentedt.getEnderecoUsuario().setBairro(request.getParameter("Bairro"));
		Assistentedt.getEnderecoUsuario().setId_Cidade(request.getParameter("BairroId_Cidade"));
		Assistentedt.getEnderecoUsuario().setCidade(request.getParameter("BairroCidade"));
		Assistentedt.getEnderecoUsuario().setUf(request.getParameter("BairroUf"));
		Assistentedt.getEnderecoUsuario().setCep(request.getParameter("Cep"));

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {
			case Configuracao.Imprimir:
				break;
			case Configuracao.Localizar:
				if(request.getParameter("Passo") != null && request.getParameter("Passo").equalsIgnoreCase("1")) {
					String stTemp = "";
					stTemp = Assistentene.consultarTodosUsuariosJSON(stNomeBusca1, stNomeBusca2, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				} else if (request.getParameter("tempFluxo1") != null && request.getParameter("tempFluxo1").equalsIgnoreCase("C1")) {
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					String[] lisNomeBusca = {"Nome","Usuario"};
					String[] lisDescricao = {"Nome", "Usuário", "RG", "CPF"};
					request.setAttribute("tempFluxo1", "S");
					request.setAttribute("tempBuscaId", "Id_Usuario");
					request.setAttribute("tempBuscaDescricao", "Usuario");
					request.setAttribute("tempBuscaPrograma", "Usuario");
					request.setAttribute("tempRetorno", "Assistente");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Salvar);
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					tempList = Assistentene.consultarDescricaoAssistenteServentia("", UsuarioSessao.getUsuarioDt().getId_UsuarioServentia(), UsuarioSessao.getUsuarioDt().getId_Serventia());
					stAcao = "/WEB-INF/jsptjgo/AssistenteLocalizar.jsp";
					if (tempList.size() > 0) {
						request.setAttribute("ListaAssistente", tempList);
						request.setAttribute("tempFluxo1", "C2");
						request.setAttribute("PaginaAtual", Configuracao.Localizar);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", 0);
						Assistentedt.limpar();
					} else request.setAttribute("MensagemErro", "Dados Não Localizados");
				}
				break;

			case Configuracao.Novo:
				if (request.getParameter("tempFluxo1").equalsIgnoreCase("L")) {
					Assistentedt.setServentiaUf("");
					Assistentedt.setId_UsuarioServentia("");
					Assistentedt.setId_Serventia("");
					Assistentedt.setServentia("");
					Assistentedt.setId_UsuarioServentiaGrupo("");
					Assistentedt.setId_Grupo("");
					Assistentedt.setGrupo("");
					Assistentedt.setGrupoCodigo("");
				} else {
					Assistentedt.limpar();
				}
				break;

			case Configuracao.Salvar:
				if (request.getParameter("tempFluxo1").equalsIgnoreCase("S")) {
					request.setAttribute("assistenteDt", Assistentene.consultarAssistenteId(request.getParameter("Id_Usuario")));
					request.setAttribute("tempFluxo1", "S");
					stAcao = "/WEB-INF/jsptjgo/AssistenteMensagemConfirmacao.jsp";
				} else if (request.getParameter("tempFluxo1").equalsIgnoreCase("ASSINARNAO")) {
					request.setAttribute("tempFluxo1", "ASSINARNAO");
				} else if (request.getParameter("tempFluxo1").equalsIgnoreCase("ASSINARSIM")) {
					request.setAttribute("tempFluxo1", "ASSINARSIM");
				}
				break;

			case Configuracao.SalvarResultado:
				if (request.getParameter("tempFluxo1").equalsIgnoreCase("S")) {
					stAcao = "/WEB-INF/jsptjgo/AssistenteLocalizar.jsp";

					Assistentedt = Assistentene.consultarAssistenteId(request.getParameter("Id_Usuario"));
					Assistentedt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
					Assistentedt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
					Assistentedt.setId_Serventia(UsuarioSessao.getUsuarioDt().getId_Serventia());
					Assistentedt.setId_UsuarioServentiaChefe(UsuarioSessao.getUsuarioDt().getId_UsuarioServentia());
					Assistentedt.setGrupoUsuarioChefe(UsuarioSessao.getUsuarioDt().getGrupoCodigo());
					Assistentedt.setGrupoTipoUsuarioChefe(UsuarioSessao.getUsuarioDt().getGrupoTipoCodigo());
					Mensagem = Assistentene.VerificarAssistenteServentia(Assistentedt);
					if (Mensagem.length() == 0) {
						Assistentene.salvarAssistenteServentia(Assistentedt);
						request.setAttribute("tempFluxo1", "OK");
						request.setAttribute("MensagemOk", "Assessor Salvo na Serventia com sucesso");
						tempList = Assistentene.consultarDescricaoAssistenteServentia("", UsuarioSessao.getUsuarioDt().getId_UsuarioServentia(), UsuarioSessao.getUsuarioDt().getId_Serventia());
						if (tempList.size() > 0) {
							request.setAttribute("ListaAssistente", tempList);
							//request.setAttribute("tempFluxo1", "C2");
							request.setAttribute("PaginaAtual", Configuracao.Localizar);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", 0);
							Assistentedt.limpar();
						}
					} else {
						request.setAttribute("MensagemErro", Mensagem);
						tempList = Assistentene.consultarDescricaoAssistenteServentia("", UsuarioSessao.getUsuarioDt().getId_UsuarioServentia(), UsuarioSessao.getUsuarioDt().getId_Serventia());
						if (tempList.size() > 0) {
							request.setAttribute("ListaAssistente", tempList);
							//request.setAttribute("tempFluxo1", "C2");
							request.setAttribute("PaginaAtual", Configuracao.Localizar);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", 0);
							Assistentedt.limpar();
						} else request.setAttribute("MensagemErro", "Dados Não Localizados");
					}
				} else if (request.getParameter("tempFluxo1").equalsIgnoreCase("ASSINARNAO")) {
					Assistentene.bloqueieAssistenteGuardarParaAssinar(Assistentedt);
					request.setAttribute("MensagemOk", "Usuário bloqueado para a ação guardar para assinar");
				} else if (request.getParameter("tempFluxo1").equalsIgnoreCase("ASSINARSIM")) {
					Assistentene.libereAssistenteGuardarParaAssinar(Assistentedt);
					request.setAttribute("MensagemOk", "Usuário liberado para a ação guardar para assinar");					
				} else {
					Mensagem = Assistentene.VerificarAssistente(Assistentedt);
					if (Mensagem.length() == 0) {
						Assistentene.salvarAssistente(Assistentedt);
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso. <br /><br />Lembrando que para novos usuários, a senha gerada é de 12345. Por favor, acesse o sistema e mude a senha.");
						Assistentedt.getEnderecoUsuario();
					} else request.setAttribute("MensagemErro", Mensagem);
				}
				break;

			// Confirmação CURINGA = "F" Desativa, CURINGA = "G" Ativa
			case Configuracao.Excluir:
				stAcao = "/WEB-INF/jsptjgo/AssistenteMensagemConfirmacao.jsp";
				stId = request.getParameter("Id_UsuarioServentiaGrupo");
				if (stId != null && stId.length() > 0) {
					Assistentedt.setId_UsuarioServentiaGrupo(stId);
					request.setAttribute("assistenteDt", Assistentene.consultarAssistenteServentiaGrupoId(stId));
					if (request.getParameter("tempFluxo1").equalsIgnoreCase("F")) request.setAttribute("tempFluxo1", "CF");
					else if (request.getParameter("tempFluxo1").equalsIgnoreCase("G")) request.setAttribute("tempFluxo1", "CG");
				}
				break;

			// CURINGA = "F" Desativa, CURINGA = "G" Ativa Assistente
			case Configuracao.ExcluirResultado:
				if (Assistentedt.getId_UsuarioServentiaGrupo().length() > 0) {
					if (request.getParameter("tempFluxo1").equalsIgnoreCase("F")) {
						Assistentene.desativarUsuarioServentiaGrupo(Assistentedt);
						Mensagem = "Assessor Desabilitado na Serventia com sucesso.";
					} else if (request.getParameter("tempFluxo1").equalsIgnoreCase("G")) {
						Assistentene.ativarUsuarioServentiaGrupo(Assistentedt);
						Mensagem = "Assessor Habilitado na Serventia com sucesso.";
					}
				} else request.setAttribute("MensagemErro", "Não foi possível obter Serventia para Desabilitação");

				if (Mensagem.length() > 0) {
					request.setAttribute("tempFluxo1", "C2");
					request.setAttribute("MensagemOk", Mensagem);
				}

				this.executar(request, response, UsuarioSessao, Configuracao.Localizar, tempNomeBusca, PosicaoPaginaAtual);
				return;

			case (CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar): 
				if (request.getParameter("Passo")==null){
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					String[] lisNomeBusca = {"Cidade","Uf"};
					String[] lisDescricao = {"Cidade","Uf"};
					request.setAttribute("tempBuscaId", "Id_Cidade");
					request.setAttribute("tempBuscaDescricao", "Cidade");
					request.setAttribute("tempBuscaPrograma", "Cidade");
					request.setAttribute("tempRetorno", "Assistente");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = Assistentene.consultarDescricaoCidadeJSON(stNomeBusca1, stNomeBusca2, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			
			case (RgOrgaoExpedidorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					String[] lisNomeBusca = {"Sigla","Nome"};
					String[] lisDescricao = {"Sigla","Nome","Estado"};
					request.setAttribute("tempBuscaId", "Id_RgOrgaoExpedidor");
					request.setAttribute("tempBuscaDescricao", "RgOrgaoExpedidor");
					request.setAttribute("tempBuscaPrograma", "RgOrgaoExpedidor");
					request.setAttribute("tempRetorno", "Assistente");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", RgOrgaoExpedidorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					stTemp = Assistentene.consultarDescricaoRgOrgaoExpedidorJSON(stNomeBusca1, stNomeBusca2, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;

			case (BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					String[] lisNomeBusca = {"Bairro","Cidade","UF"};
					String[] lisDescricao = {"Bairro","Cidade","UF"};
					String[] camposHidden = {"BairroCidade","BairroUf"};
					request.setAttribute("camposHidden",camposHidden);
					request.setAttribute("tempBuscaId", "Id_Bairro");
					request.setAttribute("tempBuscaDescricao", "Bairro");
					request.setAttribute("tempBuscaPrograma", "Bairro");
					request.setAttribute("tempRetorno", "Assistente");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else{
					String stTemp = "";
					stTemp = Assistentene.consultarDescricaoBairroJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;

			default:
				stId = request.getParameter("Id_Usuario");
				if (stId != null && !stId.isEmpty()) {
					if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(Assistentedt.getId())) {
						Assistentedt.limpar();
						Assistentedt = Assistentene.consultarAssistenteId(stId);
						Assistentedt.getEnderecoUsuario();
						tempList = Assistentene.consultarDescricaoAssistenteServentia(stId, UsuarioSessao.getUsuarioDt().getId_UsuarioServentia(), UsuarioSessao.getUsuarioDt().getId_Serventia());
						if (tempList.size() > 0) {
							Assistentedt.setListaUsuarioServentias(tempList);
							
							UsuarioDt usuarioServentiaDt = (UsuarioDt)tempList.get(0);
							Assistentedt.setPodeGuardarAssinarUsuarioServentiaChefe(usuarioServentiaDt.getPodeGuardarAssinarUsuarioServentiaChefe());
							Assistentedt.setGrupoCodigo(usuarioServentiaDt.getGrupoCodigo());
							Assistentedt.setId_UsuarioServentia(usuarioServentiaDt.getId_UsuarioServentia());
						}
					}
				}
				break;
		}

		request.getSession().setAttribute("Assistentedt", Assistentedt);
		request.getSession().setAttribute("Assistentene", Assistentene);
		request.getSession().setAttribute("Enderecodt", Assistentedt.getEnderecoUsuario());

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	public int Permissao() {
		return AssistenteDt.CodigoPermissao;
	}
}