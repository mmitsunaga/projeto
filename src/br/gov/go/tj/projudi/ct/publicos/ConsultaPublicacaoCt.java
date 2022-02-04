package br.gov.go.tj.projudi.ct.publicos;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;

import br.gov.go.tj.projudi.ct.Controle;
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.PesquisaAvancadaDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaGrupoDt;
import br.gov.go.tj.projudi.dt.indexacao.ResponseHits;
import br.gov.go.tj.projudi.ne.ElasticSearchNe;
import br.gov.go.tj.projudi.ne.PendenciaNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.ValidacaoUtil;

public class ConsultaPublicacaoCt extends Controle {

	private static final long serialVersionUID = -7481263979062032031L;
	
	protected static final int QTDE_RESULTADOS_POR_PAGINA = 15;
	
	protected static final int QTDE_LINKS_PAGINACAO = 15;
	
	protected static final int PERMISSAO_LOCALIZAR_SERVENTIA = (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar);
	
	protected static final int PERMISSAO_LOCALIZAR_ARQUIVO_TIPO = (ArquivoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar);
	
	protected static final int PERMISSAO_LOCALIZAR_USUARIO_SERVENTIA_GRUPO = (UsuarioServentiaGrupoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar);
	
	@Override
	public int Permissao() {
		return 871;
	}

	protected String getId_GrupoPublico() {
		return GrupoDt.ID_GRUPO_PUBLICO;
	}

	@Override
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {
				
		String stIdArquivo = "";			
		String textoDigitado = "";
		String tipoConsulta = "texto";		
		int posicaoPaginaAtual = 0;
		
		ResponseHits responseHits = null;
		
		String stAcao = "/WEB-INF/jsptjgo/ConsultaPublicacaoElasticSearch.jsp";
								
		if (request.getParameter("textoDigitado") != null){
			textoDigitado = request.getParameter("textoDigitado");
		}
				
		if (ValidacaoUtil.isNaoVazio(posicaopaginaatual)){
			posicaoPaginaAtual = Integer.valueOf(posicaopaginaatual);
		}
		
		String stNomeBusca1 = "";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		// Tipo de consulta: Olhar o campo no request, se não existir, olhar na sessão.
		if(request.getParameter("tipoConsulta") != null){
			tipoConsulta = request.getParameter("tipoConsulta");
			request.getSession().removeAttribute("tipoConsulta");
		} else if(request.getSession().getAttribute("tipoConsulta") != null){
			tipoConsulta = String.valueOf(request.getSession().getAttribute("tipoConsulta"));			
		}
		
		// Seta os campos da pesquisa avançada
		PesquisaAvancadaDt pesquisaAvancadaDt = (PesquisaAvancadaDt) request.getSession().getAttribute("pesquisaAvancadaDt");
		if (pesquisaAvancadaDt == null) pesquisaAvancadaDt = new PesquisaAvancadaDt();
		pesquisaAvancadaDt.setNumeroProcesso(request.getParameter("ProcessoNumero"));
		pesquisaAvancadaDt.setId_serventia(request.getParameter("Id_Serventia"));
		pesquisaAvancadaDt.setServentia(request.getParameter("Serventia"));
		pesquisaAvancadaDt.setId_realizador(request.getParameter("Id_Usuario"));
		pesquisaAvancadaDt.setRealizador(request.getParameter("Usuario"));
		pesquisaAvancadaDt.setDataPublicacaoIni(request.getParameter("DataInicial"));
		pesquisaAvancadaDt.setDataPublicacaoFim(request.getParameter("DataFinal"));
		pesquisaAvancadaDt.setId_tipoArquivo(request.getParameter("Id_ArquivoTipo"));
		pesquisaAvancadaDt.setTipoArquivo(request.getParameter("ArquivoTipo"));
		pesquisaAvancadaDt.setTexto(request.getParameter("Texto"));
		
		// Seta os parâmetros de entrada
		response.setContentType("text/html");
		response.setCharacterEncoding("iso-8859-1");
		
		request.setAttribute("tempPrograma", "ConsultaPublicacao");
		request.setAttribute("tempRetorno", "ConsultaPublicacao");
		request.setAttribute("action", "ConsultaPublicacao");
		
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		
		request.setAttribute("tipoConsulta", tipoConsulta);
		request.setAttribute("PermissaoLocalizarServentia", PERMISSAO_LOCALIZAR_SERVENTIA);
		request.setAttribute("PermissaoLocalizarArquivoTipo", PERMISSAO_LOCALIZAR_ARQUIVO_TIPO);
		request.setAttribute("PermissaoLocalizaruUsuario", PERMISSAO_LOCALIZAR_USUARIO_SERVENTIA_GRUPO);
		
		switch (paginaatual){
		
			case Configuracao.Novo:
				
				pesquisaAvancadaDt.limpar();
				request.setAttribute("tipoConsulta", tipoConsulta);
				request.setAttribute("TempoResposta", "");
				request.setAttribute("ListaDocumentos", null);
				request.setAttribute("QtdeResultados", "0");
				request.setAttribute("textoDigitado", "");
				request.setAttribute("PaginaInicial", 0);
				request.setAttribute("PaginaFinal", 0);
				request.setAttribute("QuantidadePaginas", 0);
				
				break;
		
			case Configuracao.Localizar:
												
				// Para a pesquisa avançada, mostrar apenas 1 resultado por página.				
				int qtdeRegistrosPagina = tipoConsulta.equals("texto") ? QTDE_RESULTADOS_POR_PAGINA : 1;
				
				ElasticSearchNe elasticSearchNe = new ElasticSearchNe();
				if (tipoConsulta.equals("texto")){
					if (ValidacaoUtil.isNaoVazio(textoDigitado)){
						responseHits = elasticSearchNe.consultarPublicacoesPorTexto(textoDigitado, qtdeRegistrosPagina, posicaoPaginaAtual * qtdeRegistrosPagina);
					} else {						
						request.setAttribute("MensagemErro", "É necessário informar algum texto.");
					}				
				} else if (tipoConsulta.equals("campo")){
					if (isCamposValidos(pesquisaAvancadaDt)){
						responseHits = elasticSearchNe.consultarPublicacoesPorCamposEspecificos(getParamsQuery(pesquisaAvancadaDt), qtdeRegistrosPagina, posicaoPaginaAtual * qtdeRegistrosPagina);
					} else {
						request.setAttribute("MensagemErro", "É necessário informar algum filtro da pesquisa.");
					}					
				}
				if (ValidacaoUtil.isNaoNulo(responseHits)){
					int qtdeResultados = responseHits.getHits().getTotal().intValue();
					request.setAttribute("TempoResposta", responseHits.getTook());
					request.setAttribute("ListaDocumentos", responseHits.getHits().getItems());
					request.setAttribute("QtdeResultados", qtdeResultados);
					request.setAttribute("textoDigitado", textoDigitado);
					request.setAttribute("PaginaInicial", getPaginaInicial(qtdeResultados, qtdeRegistrosPagina, QTDE_LINKS_PAGINACAO, posicaoPaginaAtual));
					request.setAttribute("PaginaFinal", getPaginaFinal(qtdeResultados, qtdeRegistrosPagina, QTDE_LINKS_PAGINACAO, posicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", getQdePaginas(qtdeResultados, qtdeRegistrosPagina));					
				}					
				
				break;
				
			case (PERMISSAO_LOCALIZAR_SERVENTIA):
				request.getSession().setAttribute("tipoConsulta", "campo");
				if (request.getParameter("Passo") == null) {
					String[] lisNomeBusca = {"Serventia"};
					String[] lisDescricao = {"Serventia", "Estado"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Serventia");
					request.setAttribute("tempBuscaDescricao", "Serventia");
					request.setAttribute("tempBuscaPrograma", "Serventia");
					request.setAttribute("tempRetorno", "ConsultaPublicacao");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", PERMISSAO_LOCALIZAR_SERVENTIA);
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					ProcessoNe processoNe = new ProcessoNe();
					stTemp = processoNe.consultarServentiaJSON(stNomeBusca1, String.valueOf(posicaoPaginaAtual), UsuarioSessao.getUsuarioDt());					
					enviarJSON(response, stTemp);									
					return;
				}			
				break;
							
			case (PERMISSAO_LOCALIZAR_ARQUIVO_TIPO):
				request.getSession().setAttribute("tipoConsulta", "campo");
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ArquivoTipo"};
					String[] lisDescricao = {"ArquivoTipo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ArquivoTipo");
					request.setAttribute("tempBuscaDescricao","ArquivoTipo");
					request.setAttribute("tempBuscaPrograma","ArquivoTipo");
					request.setAttribute("tempRetorno","ConsultaPublicacao");		
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (PERMISSAO_LOCALIZAR_ARQUIVO_TIPO));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);					
				} else {
					String stTemp="";
					ProcessoNe processoNe = new ProcessoNe();
					stTemp = processoNe.consultarGrupoArquivoTipoJSON(UsuarioSessao.getUsuarioDt().getGrupoCodigo(), stNomeBusca1, String.valueOf(posicaoPaginaAtual));					
					enviarJSON(response, stTemp);											
					return;								
				}
				break;
			
			case (PERMISSAO_LOCALIZAR_USUARIO_SERVENTIA_GRUPO):
				request.getSession().setAttribute("tipoConsulta", "campo");
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Usuário"};
					String[] lisDescricao = {"Usuario", "Grupo"};										
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Usuario");
					request.setAttribute("tempBuscaDescricao", "Usuario");
					request.setAttribute("tempBuscaPrograma", "Usuario");
					request.setAttribute("tempRetorno", "ConsultaPublicacao");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (PERMISSAO_LOCALIZAR_USUARIO_SERVENTIA_GRUPO));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = new ElasticSearchNe().consultarServentiaCargosJSON(stNomeBusca1, String.valueOf(posicaoPaginaAtual));
					enviarJSON(response, stTemp);
					return;
				}
				break;
				
			case Configuracao.Imprimir:
				if (checkRecaptcha(request)) {
					stIdArquivo = request.getParameter("Id_Arquivo");						
					if (stIdArquivo != null && stIdArquivo.length() > 0){
						PendenciaNe pendenciaNe = new PendenciaNe();
						byte[] byTemp = pendenciaNe.gerarPdfPublicacao(ProjudiPropriedades.getInstance().getCaminhoAplicacao(), stIdArquivo);
						if (byTemp!=null){
							try{
								response.setStatus(HttpStatus.SC_OK);
								response.setHeader("Content-Disposition", "attachment; filename=" +System.currentTimeMillis()+".pdf");
								response.setContentType("application/pdf");
								response.getOutputStream().write(byTemp);
								response.flushBuffer();											
							} catch(Exception e) {}
							return;
						} else {
							request.setAttribute("MensagemErro", "Erro, arquivo não disponível ou bloqueado.");
							RequestDispatcher dis = request.getRequestDispatcher("/WEB-INF/jsptjgo/Erro.jsp");
							dis.include(request, response);
						}
					}
				}
				break;
				
			default:				
				request.setAttribute("TempoResposta", "");
				request.setAttribute("ListaDocumentos", null);
				request.setAttribute("QtdeResultados", "0");				
				request.setAttribute("PaginaInicial", 0);
				request.setAttribute("PaginaFinal", 0);
				request.setAttribute("QuantidadePaginas", 0);
				break;
				
		}
				
		request.setAttribute("PaginaAtual", paginaatual);
		request.setAttribute("PosicaoPaginaAtual", posicaoPaginaAtual);
		request.getSession().setAttribute("pesquisaAvancadaDt", pesquisaAvancadaDt);
		
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);

	}
	
	/**
	 * Verifica se pelo menos um campo da pesquisa avançada foi informado. 
	 * @return
	 */
	protected boolean isCamposValidos(PesquisaAvancadaDt pesquisa){
		return 	ValidacaoUtil.isNaoVazio(pesquisa.getTexto()) ||
				ValidacaoUtil.isNaoVazio(pesquisa.getNumeroProcesso()) ||
				ValidacaoUtil.isNaoVazio(pesquisa.getId_serventia()) ||
				ValidacaoUtil.isNaoVazio(pesquisa.getId_realizador()) ||
				ValidacaoUtil.isNaoVazio(pesquisa.getId_tipoArquivo()) ||
				ValidacaoUtil.isNaoVazio(pesquisa.getDataPublicacaoIni()) ||
				ValidacaoUtil.isNaoVazio(pesquisa.getDataPublicacaoFim());		
	}
	
	
	/**
	 * Monta um hash com os parâmetros de tela informado.
	 * A chave é o campo do documento dentro do elasticsearch.
	 * @param bean
	 * @return
	 */
	protected Map<String, String> getParamsQuery(PesquisaAvancadaDt bean){
		Map<String, String> params = new HashMap<>();
		if (ValidacaoUtil.isNaoVazio(bean.getTexto())){
			params.put("texto", bean.getTexto());
		}
		if (ValidacaoUtil.isNaoVazio(bean.getNumeroProcesso())){
			params.put("numero_processo", bean.getNumeroProcesso());
		}
		if (ValidacaoUtil.isNaoVazio(bean.getId_serventia())){
			 params.put("id_serv", bean.getId_serventia());
		}
		if (ValidacaoUtil.isNaoVazio(bean.getId_realizador())){
			 params.put("id_usu", bean.getId_realizador());
		}
		if (ValidacaoUtil.isNaoVazio(bean.getId_tipoArquivo())){
			params.put("id_tipo_arq", bean.getId_tipoArquivo());
		}
		if (ValidacaoUtil.isNaoVazio(bean.getDataPublicacaoIni())){
			params.put("data_pub_ini", bean.getDataPublicacaoIni());
		}
		if (ValidacaoUtil.isNaoVazio(bean.getDataPublicacaoFim())){
			params.put("data_pub_fim", bean.getDataPublicacaoFim());
		}
		return params;
	}
	
	/**
	 * 
	 * @param total
	 * @param tamanho
	 * @param paginas_visiveis
	 * @param pos
	 * @return
	 */
	protected int getPaginaInicial(int total, int tamanho, int paginas_visiveis, int pos){
		return getPaginaInicialEFinal(total, tamanho, paginas_visiveis, pos, "I");
	}
		
	/**
	 * 
	 * @param total
	 * @param tamanho
	 * @param paginas_visiveis
	 * @param pos
	 * @return
	 */
	protected int getPaginaFinal(int total, int tamanho, int paginas_visiveis, int pos){
		return getPaginaInicialEFinal(total, tamanho, paginas_visiveis, pos, "F");
	}
	
	/**
	 * 	
	 * @param total
	 * @param tamanho
	 * @return
	 */
	protected int getQdePaginas(int total, int tamanho){
		Double qtdePaginas = Math.ceil(total/tamanho);
		int resto = (total % tamanho);
		return resto > 0 ? qtdePaginas.intValue() + 1 : qtdePaginas.intValue();
	}
	
	/**
	 * 
	 * @param total
	 * @param tamanho
	 * @param paginas_visiveis
	 * @param pos
	 * @param tipoRetorno
	 * @return
	 */
	protected int getPaginaInicialEFinal(int total, int tamanho, int paginas_visiveis, int pos, String tipoRetorno){
		int totalPaginas = getQdePaginas(total, tamanho);		
		int paginaInicial = pos - Math.floorDiv(paginas_visiveis, 2);
		if (paginaInicial < 1) paginaInicial = 1;		
		int paginaFinal = paginaInicial + paginas_visiveis - 1;
		if (paginaFinal > totalPaginas){
			paginaFinal = totalPaginas;
		}		
		if (paginaInicial > (paginaFinal - (paginas_visiveis - 1))){
			paginaInicial = paginaFinal - (paginas_visiveis - 1);
		}		
		if (paginaInicial < 1) paginaInicial = 1;		
		if (totalPaginas == 1) return 0;
		return tipoRetorno.equals("I") ? paginaInicial : paginaFinal;
	}
	
}

