package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.projudi.dt.CidadeDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.RgOrgaoExpedidorDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaOabDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.MensagemException;

public class AdvogadoCertificadoCt extends Controle {

	private static final long serialVersionUID = -4405365775398240654L;
	
	protected final int CONSULTA_ORGAO_EXPEDIDOR = RgOrgaoExpedidorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar;
	protected final int CONSULTA_CIDADE = CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar;
	protected final int CONSULTA_BAIRRO = BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar;

	@Override
	public int Permissao() {
		return UsuarioDt.CodigoPermissao;
	}
	
	@Override
	protected String getId_GrupoPublico() {	
		return GrupoDt.ID_GRUPO_PUBLICO;
	}



	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		UsuarioDt Advogadodt;
		UsuarioServentiaOabDt UsuarioServentiaOabdt;
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		String stNomeBusca3 = "";
		String stNomeBusca4 = "";
		String stNomeBusca5 = "";
		List tempList = null;
		String Mensagem = "";
		String stId = "";
		String stAcao = "/WEB-INF/jsptjgo/AdvogadoCertificado.jsp";

		request.setAttribute("tempPrograma", "AdvogadoCertificado");
		request.setAttribute("tempRetorno", "AdvogadoCertificado");

		Advogadodt = (UsuarioDt) request.getSession().getAttribute("NovoAdvogado");
		if(Advogadodt == null){
			throw new MensagemException("Ocorreu um erro na sessão, favor sair e refazer o login.");
		}
		UsuarioServentiaOabdt = Advogadodt.getUsuarioServentiaOab();
		
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		if(request.getParameter("nomeBusca3") != null) stNomeBusca3 = request.getParameter("nomeBusca3");
		if(request.getParameter("nomeBusca4") != null) stNomeBusca4 = request.getParameter("nomeBusca4");
		if(request.getParameter("nomeBusca5") != null) stNomeBusca5 = request.getParameter("nomeBusca5");

		setDadosAdvogado(request, Advogadodt, UsuarioServentiaOabdt);

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Curinga6);
		
		switch (paginaatual) {				
			case Configuracao.Curinga6:
				break;

			case Configuracao.Curinga7:
				Advogadodt.setListaUsuarioServentias((List<UsuarioServentiaOabDt>) request.getSession().getAttribute("OabsSuplementaresNovoAdvogado"));
				Mensagem = UsuarioSessao.VerificarAdvogado(Advogadodt);
				if (Mensagem.length() == 0) {
					String stRetorno = UsuarioSessao.salvarAdvogadoComCertificado(Advogadodt);
					request.setAttribute("MensagemOk", "Dados do Advogado Atualizados com Sucesso. " + stRetorno);
					LimparSessao(request.getSession());
					request.getSession().removeAttribute("UsuarioSessao");
					redireciona(response, "certificado");
					return;
				} else {
					request.setAttribute("MensagemErro", Mensagem);
					break;
				}

			case CONSULTA_CIDADE:{
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Cidade","Uf"};
					String[] lisDescricao = {"Cidade","Uf"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Cidade");
					request.setAttribute("tempBuscaDescricao", "Cidade");
					request.setAttribute("tempBuscaPrograma", "Cidade");
					request.setAttribute("tempRetorno", "AdvogadoCertificado");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = UsuarioSessao.consultarDescricaoCidadeJSON(stNomeBusca1, stNomeBusca2, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			}

			case CONSULTA_ORGAO_EXPEDIDOR: {
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Sigla","Nome"};
					String[] lisDescricao = {"Sigla","Órgão Expedidor","Estado"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_RgOrgaoExpedidor");
					request.setAttribute("tempBuscaDescricao", "RgOrgaoExpedidor");
					request.setAttribute("tempBuscaPrograma", "RgOrgaoExpedidor");
					request.setAttribute("tempRetorno", "AdvogadoCertificado");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", RgOrgaoExpedidorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					stTemp = UsuarioSessao.consultarDescricaoRgOrgaoExpedidorJSON(stNomeBusca1, stNomeBusca2, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			}

			case CONSULTA_BAIRRO: {
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Bairro","Cidade","UF"};
					String[] lisDescricao = {"Bairro","Cidade","UF"};
					String[] camposHidden = {"BairroCidade","BairroUf"};
					request.setAttribute("camposHidden",camposHidden);
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Bairro");
					request.setAttribute("tempBuscaDescricao", "Bairro");
					request.setAttribute("tempBuscaPrograma", "Bairro");
					request.setAttribute("tempRetorno", "AdvogadoCertificado");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else{
					String stTemp = "";
					stTemp = UsuarioSessao.consultarDescricaoBairroJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			}

			default:
		}

		request.getSession().setAttribute("Advogadodt", Advogadodt);
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
	 */
	protected void setDadosAdvogado(HttpServletRequest request, UsuarioDt Advogadodt, UsuarioServentiaOabDt UsuarioServentiaOabdt) {
		//Advogadodt.setUsuario(request.getParameter("Usuario"));
		Advogadodt.setId_Naturalidade(request.getParameter("Id_Cidade"));
		Advogadodt.setNaturalidade(request.getParameter("Cidade"));
		Advogadodt.setEndereco(request.getParameter("Endereco"));
		Advogadodt.setId_RgOrgaoExpedidor(request.getParameter("Id_RgOrgaoExpedidor"));
		Advogadodt.setRgOrgaoExpedidor(request.getParameter("RgOrgaoExpedidor"));
		Advogadodt.setNome(request.getParameter("Nome"));
		Advogadodt.setSexo(request.getParameter("Sexo"));
		Advogadodt.setDataNascimento(request.getParameter("DataNascimento"));
		Advogadodt.setRg(request.getParameter("Rg"));
		Advogadodt.setRgDataExpedicao(request.getParameter("RgDataExpedicao"));
		Advogadodt.setCpf(request.getParameter("Cpf"));
		Advogadodt.setEMail(request.getParameter("EMail"));
		Advogadodt.setTelefone(request.getParameter("Telefone"));
		Advogadodt.setCelular(request.getParameter("Celular"));

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
