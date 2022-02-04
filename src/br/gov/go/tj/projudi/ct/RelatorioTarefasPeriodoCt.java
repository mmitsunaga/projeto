package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ParametroRelatorioTarefaDt;
import br.gov.go.tj.projudi.dt.ProjetoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.ne.RelatorioTarefasPeriodoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
/**
 *  Finalidade: Realizar a lógica de acordo com os estados (passoBusca) das páginas
 *  
 * Class:     RelatorioTarefasPeriodoCt
 * Author:    Márcio Mendonça Gomes 
 * Data:      02/2012
 *  
 */
public class RelatorioTarefasPeriodoCt extends Controle {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5799942231399338389L;
	
	private final String idParamRelTarefaDt = "ParamRelTarefaDt";
	private final String idRelatorioTarefaNe = "ParamRelTarefaNe";
	@Override
	public int Permissao() {		
		return ParametroRelatorioTarefaDt.CodigoPermissao;
	}
	
	/**
     * Responsável em executar os processamentos de acordo com o estado da tela
     *      
     * @param HttpServletRequest request
     * @param HttpServletResponse response
     *  
     */
	@Override
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {

		List tempList = null;
		
		request.setAttribute("tempPrograma", "RelatorioTarefaPeriodo");
				
		ParametroRelatorioTarefaDt paramRelTarefasDt = (ParametroRelatorioTarefaDt) request.getSession().getAttribute(idParamRelTarefaDt);
		if (paramRelTarefasDt == null) paramRelTarefasDt = new ParametroRelatorioTarefaDt();
		
		RelatorioTarefasPeriodoNe relatorioTarefasPeriodoNe = (RelatorioTarefasPeriodoNe) request.getSession().getAttribute(idRelatorioTarefaNe);
		if (relatorioTarefasPeriodoNe == null) relatorioTarefasPeriodoNe = new RelatorioTarefasPeriodoNe();
				
		//Obtem os parâmetros selecionados...			
		paramRelTarefasDt.setPeriodoInicialUtilizado(request.getParameter("DataInicial"));		
		paramRelTarefasDt.setPeriodoFinalUtilizado(request.getParameter("DataFinal"));	
		paramRelTarefasDt.setId_Projeto(request.getParameter("Id_Projeto"));
		paramRelTarefasDt.setProjeto(request.getParameter("Projeto"));
		paramRelTarefasDt.setId_ServentiaCargo(request.getParameter("Id_ServentiaCargo"));
		paramRelTarefasDt.setServentiaCargo(request.getParameter("ServentiaCargo"));
		
		String sPagina = "RelatorioTarefasPeriodo";			
		request.setAttribute("PaginaAnterior", paginaatual);		
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		
		switch (paginaatual) {
		
			case Configuracao.Novo:
				paramRelTarefasDt = new ParametroRelatorioTarefaDt();
				request.removeAttribute("ListaTarefa");
				break;			
				
			case Configuracao.Localizar: // Emitir em Tela
				
				if (validaParametros(paramRelTarefasDt, request)) {
					paramRelTarefasDt.setListaTarefas(relatorioTarefasPeriodoNe.obtenhaRelatorioListaTarefas(paramRelTarefasDt.getPeriodoInicialUtilizado(), paramRelTarefasDt.getPeriodoFinalUtilizado(), paramRelTarefasDt.getId_Projeto(), paramRelTarefasDt.getId_ServentiaCargo()));
					if (paramRelTarefasDt.getListaTarefas() == null || paramRelTarefasDt.getListaTarefas().size() == 0) super.exibaMensagemInconsistenciaErro(request, "Nenhuma tarefa disponível com os parâmetros informados.");
				}
				break;
				
			case (ProjetoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				request.setAttribute("tempBuscaId_Projeto", "Id_Projeto");
				request.setAttribute("tempBuscaProjeto", "Projeto");
				request.setAttribute("tempRetorno", "RelatorioTarefasPeriodo");
				sPagina = "ProjetoLocalizar";
				tempList = relatorioTarefasPeriodoNe.consultarDescricaoProjeto(nomebusca, posicaopaginaatual);
				if (tempList != null && tempList.size() > 0) {
					request.setAttribute("ListaProjeto", tempList);
					request.setAttribute("PaginaAtual", paginaatual);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaopaginaatual));
					request.setAttribute("QuantidadePaginas", relatorioTarefasPeriodoNe.getQuantidadePaginas());
				} else {
					super.exibaMensagemInconsistenciaErro(request, "Dados Não Localizados");
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
				}
				break;	
				
			case (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				request.setAttribute("tempBuscaId_ServentiaCargo", "Id_ServentiaCargo");
				request.setAttribute("tempBuscaServentiaCargo", "ServentiaCargo");
				request.setAttribute("tempRetorno", "RelatorioTarefasPeriodo");
				sPagina = "ServentiaCargoResponsavelLocalizar";
				tempList = relatorioTarefasPeriodoNe.consultarServentiaCargos(nomebusca, posicaopaginaatual, UsuarioSessao.getUsuarioDt().getId_Serventia(), String.valueOf(ServentiaTipoDt.INFORMATICA), UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo());
				if (tempList != null && tempList.size() > 0) {
					request.setAttribute("ListaServentiaCargo", tempList);
					request.setAttribute("PaginaAtual", paginaatual);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaopaginaatual));
					request.setAttribute("QuantidadePaginas", relatorioTarefasPeriodoNe.getQuantidadePaginas());
					request.setAttribute("ServentiaTipoCodigo", ServentiaTipoDt.INFORMATICA);
					request.setAttribute("Id_Serventia", UsuarioSessao.getUsuarioDt().getId_Serventia());
				} else {
					super.exibaMensagemInconsistenciaErro(request, "Nenhum Cargo Disponível.");
				}
				break;
		}
		
		request.getSession().setAttribute(idParamRelTarefaDt, paramRelTarefasDt);
		request.getSession().setAttribute(idRelatorioTarefaNe, relatorioTarefasPeriodoNe);	
		
		super.redirecione(super.obtenhaAcaoJSP(sPagina), request, response);
		
	}	
	
	/**
     * Valida os intervalos de data informados na tela
     *      
     * @param ParametroRelatorioTarefaDt parametroRelTarefaDt
     * @param HttpServletRequest request
     *  
     */
	private boolean validaParametros(ParametroRelatorioTarefaDt parametroRelTarefaDt, HttpServletRequest request) throws Exception{				
		String mensagemRetorno = "";	
				
		if (parametroRelTarefaDt.getPeriodoInicialUtilizado().ehApos(parametroRelTarefaDt.getPeriodoFinalUtilizado())) {
			mensagemRetorno = "Data Inicial deve ser anterior ou igual a Data Final. \n";
		}
		if(parametroRelTarefaDt.getId_Projeto() == null || parametroRelTarefaDt.getId_Projeto().trim().length() == 0 || parametroRelTarefaDt.getId_Projeto().trim().equalsIgnoreCase("null")) {
			mensagemRetorno += "O Projeto deve ser informado. ";
		}
		
		super.exibaMensagemInconsistenciaErro(request, mensagemRetorno);	
		
		return (mensagemRetorno.equals(""));
	}	
			
}
