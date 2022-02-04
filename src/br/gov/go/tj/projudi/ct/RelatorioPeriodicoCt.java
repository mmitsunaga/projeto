package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.RelatorioPeriodicoDt;
import br.gov.go.tj.projudi.ne.RelatorioPeriodicoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class RelatorioPeriodicoCt extends RelatorioPeriodicoCtGen implements Serializable {

	private static final long serialVersionUID = 1238853454119758797L;

	public RelatorioPeriodicoCt() {
	}

	public int Permissao() {
		return RelatorioPeriodicoDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		RelatorioPeriodicoDt RelatorioPeriodicodt;
		RelatorioPeriodicoNe RelatorioPeriodicone;

		List tempList = null;
		String Mensagem = "";
		String stId = "";
		int paginaAnterior = -1;

		String stNomeBusca1 = "";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		String stAcao = "/WEB-INF/jsptjgo/RelatorioPeriodico.jsp";

		// --------------------------------------------------------------------------
		// Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		// Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		// cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		// tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		// --------------------------------------------------------------------------
		request.setAttribute("tempPrograma", "RelatorioPeriodico");
		
		if (request.getParameter("PaginaAnterior") != null)
			paginaAnterior = Funcoes.StringToInt(request.getParameter("PaginaAnterior"));

		RelatorioPeriodicone = (RelatorioPeriodicoNe) request.getSession().getAttribute("RelatorioPeriodicone");
		if (RelatorioPeriodicone == null)
			RelatorioPeriodicone = new RelatorioPeriodicoNe();

		RelatorioPeriodicodt = (RelatorioPeriodicoDt) request.getSession().getAttribute("RelatorioPeriodicodt");
		if (RelatorioPeriodicodt == null)
			RelatorioPeriodicodt = new RelatorioPeriodicoDt();

		RelatorioPeriodicodt.setRelatorioPeriodico(request.getParameter("RelatorioPeriodico"));
		RelatorioPeriodicodt.setCodigoSql(request.getParameter("CodigoSql"));

		RelatorioPeriodicodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		RelatorioPeriodicodt.setIpComputadorLog(request.getRemoteAddr());

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		// é a página padrão
		request.setAttribute("PaginaAtual", Configuracao.Editar);
		// --------------------------------------------------------------------------------//
		switch (paginaatual) {
		case Configuracao.Excluir:
			RelatorioPeriodicone.excluir(RelatorioPeriodicodt);
			request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
			break;
		case Configuracao.Imprimir:
			String mensagemErro = "";
			
			if(RelatorioPeriodicodt.getId() == null || RelatorioPeriodicodt.getId().equalsIgnoreCase("")) {
				mensagemErro = "Selecione um Relatório Periódico.";
			}
			
			Map mapCampos = new HashMap();
			List listaKeysMapCampos = new ArrayList();
			List listaTiposCampos = new ArrayList();
			if (request.getParameter("ListaCampos") != null && !request.getParameter("ListaCampos").equalsIgnoreCase("null")) {
				String[] idsParametros = request.getParameter("ListaCampos").toString().split(",");

				// Se esse registro estiver vazio significa que não há parâmetros a serem informados
				if (idsParametros[0] != null && !idsParametros[0].equals("")) {
					for (int i = 0; i < idsParametros.length; i++) {
						String nomeCampo[] = idsParametros[i].split(";");
						// TODO MELHORAR ISTO
						// O primeiro caracter sempre vem com "[" no começo. É preciso melhorar a
						// implementação desse if
						if (i == 0) {
							mapCampos.put(nomeCampo[0].substring(1), request.getParameter(nomeCampo[0].substring(1)));
							listaKeysMapCampos.add(nomeCampo[0].substring(1));
							if(nomeCampo[3].charAt(nomeCampo[3].length()-1) == ']'){
								listaTiposCampos.add(nomeCampo[3].substring(0, nomeCampo[3].length()-1));
							} else {
								listaTiposCampos.add(nomeCampo[3].substring(0, nomeCampo[3].length()));
							}
							if (new Boolean(nomeCampo[2])) {
								if (request.getParameter(nomeCampo[0].substring(1)) == null || request.getParameter(nomeCampo[0].substring(1)).equals("")) {
									mensagemErro = "Todos os campos obrigatórios devem ser preenchidos. ";
									break;
								}
							}
						} else {
							nomeCampo[0] = nomeCampo[0].replaceAll(" ", "");
							mapCampos.put(nomeCampo[0], request.getParameter(nomeCampo[0].substring(0)));
							listaKeysMapCampos.add(nomeCampo[0]);
							if(nomeCampo[3].charAt(nomeCampo[3].length()-1) == ']'){
								listaTiposCampos.add(nomeCampo[3].substring(0, nomeCampo[3].length()-1));
							} else {
								listaTiposCampos.add(nomeCampo[3].substring(0, nomeCampo[3].length()));
							}
							if (new Boolean(nomeCampo[2])) {
								if (request.getParameter(nomeCampo[0].substring(0)) == null || request.getParameter(nomeCampo[0].substring(0)).equals("")) {
									mensagemErro = "Todos os campos obrigatórios devem ser preenchidos.";
									break;
								}
							}
						}
					}
				}

			}
			
			if (mensagemErro.equals("")) {

				// Usando a lista listaCampos gerada para substituir no SQL os campos
				// da clásula WHERE
				String sql = RelatorioPeriodicodt.getCodigoSql();
				for (int i = 0; i < listaKeysMapCampos.size(); i++) {
					String chave = listaKeysMapCampos.get(i).toString();
					String valor = (String) mapCampos.get(listaKeysMapCampos.get(i));
					String tipo = listaTiposCampos.get(i).toString();
					
					//Se o tipo for Número, não precisa ser feito nada
					//Se o tipo for Data, não precisa ser feito nada
					//Se o tipo for Boolean, não precisa ser feito nada
					//Se o tipo for Texto, verificar se veio vazio e, caso sim, colocar % para consultar no LIKE do sql
					if(tipo.equals("Texto")){
						if(valor.equals(""))
							valor = "%";
						valor = "'" + valor + "'";
					}
					
					sql = sql.replaceAll(chave,valor);
				}
				RelatorioPeriodicodt.setCodigoSql(sql);

				byte[] byTemp = RelatorioPeriodicone.gerarRelatorioPeriodico(RelatorioPeriodicodt);				
				
				enviarTXT(response, byTemp,"RelatorioPeriodico" );												

				return;

			} else {
				stAcao = "/WEB-INF/jsptjgo/RelatorioPeriodicoImpressao.jsp";
				request.setAttribute("MensagemErro", mensagemErro);
				request.setAttribute("PaginaAtual", Configuracao.Curinga6);
				request.setAttribute("PaginaAnterior", Configuracao.Curinga6);
			}
			
			break;
		case Configuracao.Localizar:
        	if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"Nome"};
				String[] lisDescricao = {"Relatório Periódico"};
				stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId","Id_RelatorioPeriodico");
				request.setAttribute("tempBuscaDescricao","RelatorioPeriodico");		
				request.setAttribute("tempRetorno","RelatorioPeriodico");		
				request.setAttribute("tempDescricaoId","Id");
				
				if(paginaAnterior == Configuracao.Curinga6){
				request.setAttribute("tempBuscaPrograma", "Impressão de Relatório Periódico");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Curinga6);
			} else if(paginaAnterior == Configuracao.Editar) {
				request.setAttribute("tempBuscaPrograma","Consulta de Relatório Periódico");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
			}
				
				request.setAttribute("tempRetorno", "RelatorioPeriodico");
				request.setAttribute("PaginaAtual", (Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				String stTemp="";
				stTemp = RelatorioPeriodicone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					enviarJSON(response, stTemp);
					
				
				return;								
			}   
			
			break;
		case Configuracao.Novo:
			RelatorioPeriodicodt.limpar();
			if(paginaAnterior == Configuracao.Curinga6){
				request.setAttribute("tempPrograma", "Impressão de Relatório Periódico");						
				request.setAttribute("tempRetorno", "RelatorioPeriodico?PaginaAtual=6");
				request.setAttribute("PaginaAnterior", Configuracao.Curinga6);
				stAcao = "/WEB-INF/jsptjgo/RelatorioPeriodicoImpressao.jsp";
			} else if(paginaAnterior == Configuracao.Editar) {
				request.setAttribute("tempRetorno", "RelatorioPeriodico");
			}
			break;
		case Configuracao.Salvar:
			Mensagem = RelatorioPeriodicone.Verificar(RelatorioPeriodicodt);
			RelatorioPeriodicodt.setCamposSql(lerTabelaCampos(request));
			if (Mensagem.length() == 0) {
				RelatorioPeriodicodt.setCodigoSql(this.inserirCaracterEspecial(RelatorioPeriodicodt.getCodigoSql()));
				RelatorioPeriodicone.salvar(RelatorioPeriodicodt);
				request.setAttribute("MensagemOk", "Dados Salvos com sucesso");
				RelatorioPeriodicodt.limpar();
			} else
				request.setAttribute("MensagemErro", Mensagem);
			break;
		//Funcionalidade de imprimir o relatório periódico
		case Configuracao.Curinga6:
			stId = request.getParameter("Id_RelatorioPeriodico");
			if (stId != null && !stId.isEmpty())
				if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(RelatorioPeriodicodt.getId())) {
					RelatorioPeriodicodt.limpar();
					RelatorioPeriodicodt = RelatorioPeriodicone.consultarId(stId);
					RelatorioPeriodicodt.setCodigoSql(this.removerCaracterEspecial(RelatorioPeriodicodt.getCodigoSql()));
				}
			request.setAttribute("PaginaAnterior", paginaatual);
			stAcao = "/WEB-INF/jsptjgo/RelatorioPeriodicoImpressao.jsp";
			break;

		default:
			stId = request.getParameter("Id_RelatorioPeriodico");
			if (stId != null && !stId.isEmpty())
				if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(RelatorioPeriodicodt.getId())) {
					RelatorioPeriodicodt.limpar();
					RelatorioPeriodicodt = RelatorioPeriodicone.consultarId(stId);
					RelatorioPeriodicodt.setCodigoSql(this.removerCaracterEspecial(RelatorioPeriodicodt.getCodigoSql()));
				}
			break;
		}

		request.getSession().setAttribute("RelatorioPeriodicodt", RelatorioPeriodicodt);
		request.getSession().setAttribute("RelatorioPeriodicone", RelatorioPeriodicone);
		
		request = this.montarTabelaCamposJsp(request, RelatorioPeriodicodt);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
	/**
	 * Método que realiza a leitura da tabela de Campos gerada na tela JSP e cria a string
	 * que será gravada na coluna CamposSql do relatório.
	 * @param request - request da tela
	 * @return String campos preenchidos
	 * @throws Exception
	 * @author hmgodinho
	 */
	protected String lerTabelaCampos(HttpServletRequest request){
		String descricaoCampos = "";
		String[] idsLinhasTabelaCampos = request.getParameter("idsLinhasTabelaCampos").toString().split(";");
		//Se esse registro estiver vazio significa que a tabela está vazia
		if (!idsLinhasTabelaCampos[0].equals("")) {
			for (int i = 0; i < idsLinhasTabelaCampos.length; i++) {
				if (!descricaoCampos.equals("")) {
					descricaoCampos += "#";
				}
				String nomeCampo = idsLinhasTabelaCampos[i];
				descricaoCampos += request.getParameter(nomeCampo);
			}
		}
		return descricaoCampos;
	}
	
	String caracterEspecial = "@";
	protected String inserirCaracterEspecial(String sql){
		String sqlModificado = "";
		
		for (int i = 0; i < sql.length(); i++) {
			sqlModificado += sql.charAt(i) + caracterEspecial;
		}
		return sqlModificado;
	}
	
	protected String removerCaracterEspecial(String sql){
		return sql.replaceAll(caracterEspecial, "");
	}
	
	/**
	 * Método que faz a conversão dos dados do controle para atualizar a JSP.
	 * @param request - requisição que veio com a sessão
	 * @param relatorioPeriodicoDt - relatório que está sendo tratado
	 * @param lerTabelaOperacoesRequest - boolean que informa se deverá ler a tabela de operações da tela ou não. Para o caso de o usuário estiver atualizando
	 * a tela e não recarregando ela depois de uma consulta.
	 * @param listaConsultaEstatisticaItens - lista de Itens de Estatistica de Produtividade 
	 * @throws Exception
	 * @author hmgodinho
	 */
	protected HttpServletRequest montarTabelaCamposJsp(HttpServletRequest request, RelatorioPeriodicoDt relatorioPeriodicoDt) {
		if (relatorioPeriodicoDt.getCamposSql() != null && !relatorioPeriodicoDt.getCamposSql().equals("null") && !relatorioPeriodicoDt.getCamposSql().equals("")) {
			String[] camposTmp = relatorioPeriodicoDt.getCamposSql().split("#");
			ArrayList listaCamposTemp = new ArrayList();
			for (int i = 0; i < camposTmp.length; i++) {
				listaCamposTemp.add(camposTmp[i]);
			}
			request.setAttribute("ListaCampos", listaCamposTemp);
		}
		return request;
	}

}
