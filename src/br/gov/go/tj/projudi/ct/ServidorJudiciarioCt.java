package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.projudi.dt.CidadeDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.RgOrgaoExpedidorDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.LogNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.MensagemException;

public class ServidorJudiciarioCt extends Controle {

	private static final long serialVersionUID = -1043335119065471473L;

	public int Permissao() {
		return UsuarioDt.CodigoPermissaoServidorJudiciario;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		UsuarioDt ServidorJudiciariodt;
		UsuarioNe ServidorJudiciarione;		
		
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		String stNomeBusca3 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		if(request.getParameter("nomeBusca3") != null) stNomeBusca3 = request.getParameter("nomeBusca3");
		
		List tempList = null;
		String Mensagem = "";
		String stId = "";
		String stAcao = "/WEB-INF/jsptjgo/ServidorJudiciario.jsp";

		request.setAttribute("tempPrograma", "ServidorJudiciario");
		request.setAttribute("tempBuscaId_Usuario", "Id_Usuario");
		request.setAttribute("tempBuscaId_UsuarioServentia", "Id_UsuarioServentia");
		request.setAttribute("tempBuscaUsuario", "Usuario");
		request.setAttribute("tempFluxo1", "vazio");
		request.setAttribute("tempRetorno", "ServidorJudiciario");
			
		UsuarioNe usuarioNe = new UsuarioNe();
		
		ServidorJudiciarione = (UsuarioNe) request.getSession().getAttribute("ServidorJudiciarione");
		if (ServidorJudiciarione == null) ServidorJudiciarione = new UsuarioNe();

		ServidorJudiciariodt = (UsuarioDt) request.getSession().getAttribute("ServidorJudiciariodt");
		if (ServidorJudiciariodt == null) ServidorJudiciariodt = new UsuarioDt();

		setDadosServidorJudiciario(request, ServidorJudiciariodt, UsuarioSessao);

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {

			case Configuracao.Localizar:
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Nome","Usuario"};
					String[] lisDescricao = {"Nome","Usuario","RG","CPF"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Usuario");
					request.setAttribute("tempBuscaDescricao", "Usuario");
					request.setAttribute("tempBuscaPrograma", "Usuario");
					request.setAttribute("tempRetorno", "ServidorJudiciario");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempDescricaoDescricao", "Usuario");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					break;
				}else{
					String stTemp = "";
					
					if((stNomeBusca1 == null || stNomeBusca1.equalsIgnoreCase("")) && (stNomeBusca2 == null || stNomeBusca2.equalsIgnoreCase("")) ) {
						throw new MensagemException("Informe NOME ou USUÁRIO para realizar a busca.");
					}
					stTemp = ServidorJudiciarione.consultarDescricaoServidorJudiciarioJSON(stNomeBusca1, stNomeBusca2, PosicaoPaginaAtual);
					
					enviarJSON(response, stTemp);
						

					return;
				}
				
			case Configuracao.Novo:
				if (request.getParameter("tempFluxo1") != null && request.getParameter("tempFluxo1").equalsIgnoreCase("L")) {
					ServidorJudiciariodt.setServentiaUf("");
					ServidorJudiciariodt.setId_UsuarioServentia("");
					ServidorJudiciariodt.setId_Serventia("");
					ServidorJudiciariodt.setServentia("");
					ServidorJudiciariodt.setId_UsuarioServentiaGrupo("");
					ServidorJudiciariodt.setId_Grupo("");
					ServidorJudiciariodt.setGrupo("");
					ServidorJudiciariodt.setGrupoCodigo("");
				} else {
					ServidorJudiciariodt.limpar();
				}
				break;

			case Configuracao.Salvar:
				//Se o usuário que está cadastrando for do perfil CADASTADORES
				//Ele não poderá alterar seu próprio cadastro ou cadastrar usuários em outra comarca
				if(UsuarioSessao.getUsuarioDt().getGrupoCodigo().equals(String.valueOf(GrupoDt.CADASTRADORES))) {
					if(ServidorJudiciariodt.getUsuario().equals(UsuarioSessao.getUsuarioDt().getUsuario())) {
						request.setAttribute("MensagemErro", "O cadastrador não pode alterar o prório cadastro. Para efetuar qualquer alteração no seu cadastro entre em contato com a Corregedoria.");	
						request.setAttribute("PaginaAnterior", Configuracao.Editar);
						break;
					}
				}				
				if (request.getParameter("tempFluxo1").equalsIgnoreCase("S")) {
					if(ServidorJudiciariodt.getId() != null && !ServidorJudiciariodt.getId().equals("") && ServidorJudiciariodt.getAtivo().equalsIgnoreCase("false")){
						request.setAttribute("MensagemErro", "O usuário está Bloqueado.");	
						request.setAttribute("PaginaAnterior", Configuracao.Editar);
						break;
					}
					request.setAttribute("tempFluxo1", "S");
				} else if (request.getParameter("tempFluxo1").equals("L")) {
					request.setAttribute("Mensagem", "Clique para Confirmar a geração de nova senha de acesso para " + ServidorJudiciariodt.getNome());
					request.setAttribute("tempFluxo1", "L");
				} else if (request.getParameter("tempFluxo1").equals("A")) {
					request.setAttribute("Mensagem", "Clique para Desbloquear o usuário " + ServidorJudiciariodt.getNome());
					request.setAttribute("tempFluxo1", "A");					
				} else if (request.getParameter("tempFluxo1").equals("D")) {
					request.setAttribute("Mensagem", "Clique para Bloquear o usuário " + ServidorJudiciariodt.getNome());
					request.setAttribute("tempFluxo1", "D");					
				} else if(ServidorJudiciariodt.getId() != null && !ServidorJudiciariodt.getId().equals("") && ServidorJudiciariodt.getAtivo().equalsIgnoreCase("false")){
					request.setAttribute("MensagemErro", "O usuário está Bloqueado.");	
					request.setAttribute("PaginaAnterior", Configuracao.Editar);
				}
				break;

			case Configuracao.SalvarResultado:
				// Se tempFluxo1 S refere-se a Habilitação em Serventia e Grupo
				if (request.getParameter("tempFluxo1").equalsIgnoreCase("S")) {				
					Mensagem = ServidorJudiciarione.verificarServentiaGrupoUsuario(ServidorJudiciariodt);
					if(Mensagem.length() == 0 && ServidorJudiciarione.verificarHabilitacaoServidor(ServidorJudiciariodt)){
						Mensagem = "Usuário já cadastrado na Serventia e Cargo informados.";
					}
					if (Mensagem.length() == 0) {
						ServidorJudiciarione.salvarUsuarioServentiaGrupo(ServidorJudiciariodt);
						request.setAttribute("tempFluxo1", "OK");
						request.setAttribute("MensagemOk", "Dados Serventia e Grupo Salvos com Sucesso.");
					}
				} else if (request.getParameter("tempFluxo1").equals("L")) {
					usuarioNe.limparSenha(ServidorJudiciariodt);
					request.setAttribute("MensagemOk", "Senha Alterada com sucesso. (senha 12345)");
				} else if (request.getParameter("tempFluxo1").equals("A")){
					usuarioNe.ativarUsuario(ServidorJudiciariodt);
					request.setAttribute("MensagemOk", "Usuário Desbloqueado com sucesso.");
				} else if (request.getParameter("tempFluxo1").equals("D")){
					usuarioNe.desativarUsuario(ServidorJudiciariodt);
					request.setAttribute("MensagemOk", "Usuário Bloqueado com sucesso.");
				} else {
					// Salva dados do Servidor Judiciário
					Mensagem = usuarioNe.VerificarServidorJudiciario(ServidorJudiciariodt);
					if (Mensagem.length() == 0) {
						String stRetorno = ServidorJudiciarione.salvarServidorJudiciario(ServidorJudiciariodt);
						
						request.setAttribute("MensagemOk", "Dados Salvos com Sucesso. " + stRetorno);
							
						request.getSession().removeAttribute("UsuarioAnterior");
					}
				}

				if (Mensagem.length() == 0) {
					ServidorJudiciariodt = ServidorJudiciarione.consultarServidorJudiciarioId(ServidorJudiciariodt.getId());
					tempList = ServidorJudiciarione.consultarUsuarioServentiasGrupos(ServidorJudiciariodt.getId());
					if (tempList.size() > 0) ServidorJudiciariodt.setListaUsuarioServentias(tempList);
				} else request.setAttribute("MensagemErro", Mensagem);
				break;

			// tempFluxo1 = "F" Desativa, tempFluxo1 = "G" Ativa
			case Configuracao.Excluir:
				if(ServidorJudiciariodt.getId() != null && !ServidorJudiciariodt.getId().equals("") && ServidorJudiciariodt.getAtivo().equalsIgnoreCase("false")){
					request.setAttribute("MensagemErro", "O usuário está Bloqueado.");
					request.setAttribute("PaginaAnterior", Configuracao.Editar);
				}else{
					//Se o usuário que está cadastrando for do perfil CADASTADORES
					//Ele não poderá alterar seu próprio cadastro ou cadastrar usuários em outra comarca
					if(UsuarioSessao.getUsuarioDt().getGrupoCodigo().equals(String.valueOf(GrupoDt.CADASTRADORES))) {
						if(ServidorJudiciariodt.getUsuario().equals(UsuarioSessao.getUsuarioDt().getUsuario())) {
							request.setAttribute("MensagemErro", "O cadastrador não pode alterar o prório cadastro. Para efetuar qualquer alteração no seu cadastro entre em contato com a Corregedoria.");	
							request.setAttribute("PaginaAnterior", Configuracao.Editar);
							break;
						}
						stId = request.getParameter("Id_UsuarioServentiaGrupo");
						if (stId != null && stId.length() > 0) {
							ServidorJudiciariodt.setId_UsuarioServentiaGrupo(stId);
							UsuarioDt usuarioDt = ServidorJudiciarione.consultarUsuarioServentiaGrupoId(stId);
							if(!usuarioDt.getId_Comarca().equals(UsuarioSessao.getUsuarioDt().getId_Comarca())) {
								request.setAttribute("MensagemErro", "O cadastrador não pode cadastrar/alterar usuários em serventias de outras comarcas. Para alterar este cadastro, entre em contato com a Corregedoria");	
								request.setAttribute("PaginaAnterior", Configuracao.Editar);
								break;
							}						
						}
					}	
					stAcao = "/WEB-INF/jsptjgo/ServidorJudiciarioMensagemConfirmacao.jsp";
					stId = request.getParameter("Id_UsuarioServentiaGrupo");
					//Confirma Desativação ou Ativação do Servidor Judiciário
					if (stId != null && stId.length() > 0) {
						ServidorJudiciariodt.setId_UsuarioServentiaGrupo(stId);
						request.setAttribute("servidorJudiciarioDt", ServidorJudiciarione.consultarUsuarioServentiaGrupoId(stId));
						if (request.getParameter("tempFluxo1").equalsIgnoreCase("F")) {
							request.setAttribute("tempFluxo1", "CF");
						}else if (request.getParameter("tempFluxo1").equalsIgnoreCase("G")) {
							request.setAttribute("tempFluxo1", "CG");
						}
					}
				}
				break;

			// tempFluxo1 = "F" Desativa, tempFluxo1 = "G" Ativa
			case Configuracao.ExcluirResultado:

				if (ServidorJudiciariodt.getId_UsuarioServentiaGrupo().length() > 0) {
					if (request.getParameter("tempFluxo1").equalsIgnoreCase("F")) {
						ServidorJudiciarione.desativarUsuarioServentiaGrupo(ServidorJudiciariodt);
						ServidorJudiciariodt.setId_UsuarioServentiaGrupo("");
						//Desabilitação do UsuarioServentia decidida por Henrique e Leandro em 12/01/2017
						//devido aos vários impactos que ocorrem por conta de o UsuarioServentia não ser desabilitado
						//no momento que se desabilita o UsuarioServentiaGrupo. Exemplo: BO Ocomon 2017/519
						ServidorJudiciarione.desativarUsuarioServentia(ServidorJudiciariodt);
						ServidorJudiciariodt.setId_UsuarioServentia("");
						Mensagem = "Servidor Judiciário Desabilitado na Serventia com sucesso.";
					} else if (request.getParameter("tempFluxo1").equalsIgnoreCase("G")) {
						//Validação para impedir que usuários comuns cadastrem administradores. Somente usuário 
						//Administrador poderá cadastrar outro.
						Mensagem = ServidorJudiciarione.podeHabilitarUsuarioPerfilEspecifico(ServidorJudiciariodt, UsuarioSessao);
						if(Mensagem.equals("")){
							ServidorJudiciarione.ativarUsuarioServentiaGrupo(ServidorJudiciariodt);
							Mensagem = "Servidor Judiciário Habilitado na Serventia com sucesso.";
						}
					}
				} else request.setAttribute("MensagemErro", "Não foi possível obter Serventia/Grupo para Desabilitação");

				if (Mensagem.length() > 0) {
					request.setAttribute("tempFluxo1", "OK");
					request.setAttribute("MensagemOk", Mensagem);
					ServidorJudiciariodt.setId_Serventia("null");
					ServidorJudiciariodt.setId_Grupo("null");
				}
				// Atualiza lista de serventias e grupos
				tempList = ServidorJudiciarione.consultarUsuarioServentiasGrupos(ServidorJudiciariodt.getId());
				if (tempList.size() > 0) ServidorJudiciariodt.setListaUsuarioServentias(tempList);
				break;

			//Redireciona para servlet de Documentos do Usuário
			case Configuracao.LocalizarAutoPai:
				if (ServidorJudiciariodt != null && ServidorJudiciariodt.getId().length() > 0) {
					request.getSession().setAttribute("usuarioDt", ServidorJudiciariodt);
					redireciona(response, "UsuarioArquivo?PaginaAtual=" + Configuracao.Novo);
					return;
				} else request.setAttribute("MensagemErro", "Nenhum Usuário foi selecionado");
				break;

			case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar): // Localizar Serventia
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Serventia"};
					String[] lisDescricao = {"Serventia","Estado"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Serventia");
					request.setAttribute("tempBuscaDescricao", "Serventia");
					request.setAttribute("tempBuscaPrograma", "Serventia");
					request.setAttribute("tempRetorno", "ServidorJudiciario");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					request.setAttribute("ServentiaTipoCodigo", ServidorJudiciariodt.getServentiaTipoCodigo());
				}else{
					String stTemp = "";
					//ser o usuário que está cadastrando for do perfil CADASTADORES
					//ele só poderá consultar serventias da comarca em que ele próprio está cadastrado
					//O usuário cadastrador não poderá mais cadastrar pessoas em outras comarcas que não seja a sua.
					
					if(UsuarioSessao.getUsuarioDt().getGrupoCodigo().equals(String.valueOf(GrupoDt.CADASTRADORES))) {						
						stTemp = usuarioNe.consultarServentiasHabilitacaoUsuariosJSON(stNomeBusca1, PosicaoPaginaAtual, ServidorJudiciariodt.getServentiaTipoCodigo(), UsuarioSessao.getUsuarioDt().getGrupoCodigo(), UsuarioSessao.getUsuarioDt().getId_Comarca());
					} else {						
						stTemp = usuarioNe.consultarServentiasHabilitacaoUsuariosJSON(stNomeBusca1, PosicaoPaginaAtual, ServidorJudiciariodt.getServentiaTipoCodigo(), UsuarioSessao.getUsuarioDt().getGrupoCodigo(), null);
					}
					
					enviarJSON(response, stTemp);					
					
					return;
				}				
				break;
			case (GrupoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar): // Localizar Grupo
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Grupo"};
					String[] lisDescricao = {"Grupo","TipoServentia"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Grupo");
					request.setAttribute("tempBuscaDescricao", "Grupo");
					request.setAttribute("tempBuscaPrograma", "Grupo");
					request.setAttribute("tempRetorno", "ServidorJudiciario");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (GrupoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					ServidorJudiciariodt.setId_Serventia("");
					ServidorJudiciariodt.setServentia("");
				}else{
					String stTemp = "";
					stTemp = usuarioNe.consultarGruposHabilitacaoServidoresJSON(stNomeBusca1, PosicaoPaginaAtual, UsuarioSessao.getUsuarioDt().getGrupoCodigo());
						
						enviarJSON(response, stTemp);
						
					
					return;
				}				
				break;

			case (CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Cidade","Uf"};
					String[] lisDescricao = {"Cidade","Uf"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Cidade");
					request.setAttribute("tempBuscaDescricao", "Cidade");
					request.setAttribute("tempBuscaPrograma", "Cidade");
					request.setAttribute("tempRetorno", "ServidorJudiciario");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = ServidorJudiciarione.consultarDescricaoCidadeJSON(stNomeBusca1, stNomeBusca2, PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;

			case (RgOrgaoExpedidorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Sigla","Nome"};
					String[] lisDescricao = {"Sigla","Nome","Estado"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_RgOrgaoExpedidor");
					request.setAttribute("tempBuscaDescricao", "RgOrgaoExpedidor");
					request.setAttribute("tempBuscaPrograma", "RgOrgaoExpedidor");
					request.setAttribute("tempRetorno", "ServidorJudiciario");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", RgOrgaoExpedidorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					stTemp = ServidorJudiciarione.consultarDescricaoRgOrgaoExpedidorJSON(stNomeBusca1, stNomeBusca2, PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;

			case (BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar): // Localizar Bairro
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Bairro","Cidade","Uf"};
					String[] lisDescricao = {"Bairro","Cidade","Uf"};
					String[] camposHidden = {"BairroCidade","BairroUf"};
					request.setAttribute("camposHidden",camposHidden);
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Bairro");
					request.setAttribute("tempBuscaDescricao", "Bairro");
					request.setAttribute("tempBuscaPrograma", "Bairro");
					request.setAttribute("tempRetorno", "ServidorJudiciario");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else{
					String stTemp = "";
					stTemp = ServidorJudiciarione.consultarDescricaoBairroJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3, PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
				
			// Consulta o log de usuário (Botao 'Visualizar Log')
			case Configuracao.Curinga6:
			
				List listaLogDt = null;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				String ano = sdf.format(new Date());
				LogNe Logne = new LogNe();
				
				stAcao = "/WEB-INF/jsptjgo/UsuarioLog.jsp";
				
				if (request.getSession().getAttribute("Logne") != null) {
					Logne = (LogNe) request.getSession().getAttribute("Logne");
				}				
				if (request.getParameter("AnoBusca") != null) {
					ano = request.getParameter("AnoBusca");
				}							
				listaLogDt = (List<LogDt>) Logne.obterListaLog(ServidorJudiciariodt.getId(), ano, "Usuario");				
				request.setAttribute("listaLogDt", listaLogDt);	
				request.setAttribute("AnoBuscaSelecionado", ano);				
				break;
				
			default:
				stId = request.getParameter("Id_Usuario");
				if (stId != null && !stId.isEmpty()) if (!stId.equalsIgnoreCase("")) {
					ServidorJudiciariodt.limpar();
					ServidorJudiciariodt = ServidorJudiciarione.consultarServidorJudiciarioId(stId);
					tempList = ServidorJudiciarione.consultarUsuarioServentiasGrupos(stId);
					if (tempList.size() > 0) ServidorJudiciariodt.setListaUsuarioServentias(tempList);

				}
				stId = request.getParameter("Id_UsuarioServentiaGrupo");
				if (stId != null && !stId.isEmpty()) if (!stId.equalsIgnoreCase("")) {
					ServidorJudiciariodt.limpar();
					ServidorJudiciariodt = ServidorJudiciarione.consultarUsuarioServentiaGrupoId(stId);
					tempList = ServidorJudiciarione.consultarUsuarioServentiasGrupos(ServidorJudiciariodt.getId());
					if (tempList.size() > 0) ServidorJudiciariodt.setListaUsuarioServentias(tempList);

				}
				stId = request.getParameter("Id_Grupo");
				if (stId != null && !stId.isEmpty()) if (!stId.equalsIgnoreCase("")) {
					GrupoDt grupoDt =  ServidorJudiciarione.consultarGrupoId(stId);
					if(grupoDt != null)
						ServidorJudiciariodt.setServentiaTipoCodigo(grupoDt.getServentiaTipoCodigo());
				}
				break;
		}

		request.getSession().setAttribute("ServidorJudiciariodt", ServidorJudiciariodt);
		request.getSession().setAttribute("ServidorJudiciarione", ServidorJudiciarione);
		request.getSession().setAttribute("Enderecodt", ServidorJudiciariodt.getEnderecoUsuario());

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	/**
	 * Seta os dados do usuário e seu endereço
	 * 
	 * @param request
	 * @param ServidorJudiciariodt
	 * @param Enderecodt
	 * @param usuarioSessao
	 */
	protected void setDadosServidorJudiciario(HttpServletRequest request, UsuarioDt ServidorJudiciariodt, UsuarioNe usuarioSessao) {
		//ServidorJudiciariodt.setUsuario(request.getParameter("Usuario"));
		ServidorJudiciariodt.setId_CtpsUf(request.getParameter("Id_CtpsUf"));
		ServidorJudiciariodt.setCtpsEstado(request.getParameter("CtpsEstado"));
		ServidorJudiciariodt.setId_Naturalidade(request.getParameter("Id_Cidade"));
		ServidorJudiciariodt.setNaturalidade(request.getParameter("Cidade"));
		ServidorJudiciariodt.setId_Endereco(request.getParameter("Id_Endereco"));
		ServidorJudiciariodt.setEndereco(request.getParameter("Endereco"));
		ServidorJudiciariodt.setId_RgOrgaoExpedidor(request.getParameter("Id_RgOrgaoExpedidor"));
		ServidorJudiciariodt.setRgOrgaoExpedidor(request.getParameter("RgOrgaoExpedidor"));
		ServidorJudiciariodt.setSenha(request.getParameter("Senha"));
		ServidorJudiciariodt.setNome(request.getParameter("Nome"));
		ServidorJudiciariodt.setSexo(request.getParameter("Sexo"));
		ServidorJudiciariodt.setDataNascimento(request.getParameter("DataNascimento"));
		ServidorJudiciariodt.setRg(request.getParameter("Rg"));
		ServidorJudiciariodt.setRgDataExpedicao(request.getParameter("RgDataExpedicao"));
		ServidorJudiciariodt.setCpf(request.getParameter("Cpf"));
		ServidorJudiciariodt.setTituloEleitor(request.getParameter("TituloEleitor"));
		ServidorJudiciariodt.setTituloEleitorZona(request.getParameter("TituloEleitorZona"));
		ServidorJudiciariodt.setTituloEleitorSecao(request.getParameter("TituloEleitorSecao"));
		ServidorJudiciariodt.setCtps(request.getParameter("Ctps"));
		ServidorJudiciariodt.setCtpsSerie(request.getParameter("CtpsSerie"));
		ServidorJudiciariodt.setPis(request.getParameter("Pis"));
		ServidorJudiciariodt.setMatriculaTjGo(request.getParameter("MatriculaTjGo"));
		ServidorJudiciariodt.setNumeroConciliador(request.getParameter("NumeroConciliador"));
		ServidorJudiciariodt.setDataCadastro(request.getParameter("DataCadastro"));
		ServidorJudiciariodt.setId_Serventia(request.getParameter("Id_Serventia"));
		ServidorJudiciariodt.setServentia(request.getParameter("Serventia"));
		ServidorJudiciariodt.setServentiaTipoCodigo(request.getParameter("ServentiaTipoCodigo"));
		ServidorJudiciariodt.setId_Grupo(request.getParameter("Id_Grupo"));
		ServidorJudiciariodt.setGrupo(request.getParameter("Grupo"));
		ServidorJudiciariodt.setDataAtivo(request.getParameter("DataAtivo"));
		ServidorJudiciariodt.setDataExpiracao(request.getParameter("DataExpiracao"));
		ServidorJudiciariodt.setEMail(request.getParameter("EMail"));
		ServidorJudiciariodt.setTelefone(request.getParameter("Telefone"));
		ServidorJudiciariodt.setCelular(request.getParameter("Celular"));
		ServidorJudiciariodt.setId_UsuarioLog(usuarioSessao.getId_Usuario());
		ServidorJudiciariodt.setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());

		// ENDEREÇO DO USUÁRIO
		ServidorJudiciariodt.getEnderecoUsuario().setLogradouro(request.getParameter("Logradouro"));
		ServidorJudiciariodt.getEnderecoUsuario().setNumero(request.getParameter("Numero"));
		ServidorJudiciariodt.getEnderecoUsuario().setComplemento(request.getParameter("Complemento"));
		ServidorJudiciariodt.getEnderecoUsuario().setId_Bairro(request.getParameter("Id_Bairro"));
		ServidorJudiciariodt.getEnderecoUsuario().setBairro(request.getParameter("Bairro"));
		ServidorJudiciariodt.getEnderecoUsuario().setId_Cidade(request.getParameter("BairroId_Cidade"));
		ServidorJudiciariodt.getEnderecoUsuario().setCidade(request.getParameter("BairroCidade"));
		ServidorJudiciariodt.getEnderecoUsuario().setUf(request.getParameter("BairroUf"));
		ServidorJudiciariodt.getEnderecoUsuario().setCep(request.getParameter("Cep"));
	}
}
