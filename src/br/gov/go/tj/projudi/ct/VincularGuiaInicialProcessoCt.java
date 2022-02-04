package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.ne.GuiaEmissaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class VincularGuiaInicialProcessoCt extends Controle {

	private static final long serialVersionUID = -7851959963851353726L;

	@Override
	public int Permissao() {
		return 876;
	}

	@Override
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {
		GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();	
		ProcessoDt processodt = null;
		String numeroCompletoGuiaInicial = null;
		String numeroProcessoCompleto = null;
		String stAcao = "/WEB-INF/jsptjgo/VincularGuiaInicialProcesso.jsp";
		
		processodt = (ProcessoDt)request.getSession().getAttribute("processoDt");
		if (processodt == null) processodt = new ProcessoDt();
		
		numeroCompletoGuiaInicial = (String)request.getParameter("numeroCompletoGuiaInicial");
		if (numeroCompletoGuiaInicial == null) numeroCompletoGuiaInicial = "";
		
		numeroProcessoCompleto = (String)request.getParameter("numeroProcessoCompleto");
		if (numeroProcessoCompleto == null) numeroProcessoCompleto = "";
		
		request.setAttribute("tempPrograma", "Vincular Guia");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("numeroCompletoGuiaInicial", numeroCompletoGuiaInicial);
		request.setAttribute("numeroProcessoCompleto", numeroProcessoCompleto);
		
		switch (paginaatual) {
			case Configuracao.Novo:	{
				
				List listaGuiaInicial_Complementar = guiaEmissaoNe.consultarGuiaEmissaoInicial_Complementar(processodt.getId());
				
				String mensagemOk = "";
				
				if( listaGuiaInicial_Complementar != null && !listaGuiaInicial_Complementar.isEmpty() ) {
					mensagemOk += Configuracao.getMensagem(Configuracao.MENSAGEM_VINCULAR_GUIA_INICIAL_COMPLEMENTAR_JA_EXISTE);
				}
				
				request.setAttribute("MensagemOk", mensagemOk);
				request.setAttribute("MensagemErro", "");
				
				if( guiaEmissaoNe.possuiGuiaEmitida(processodt.getId(), GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU)
					||
					guiaEmissaoNe.possuiGuiaEmitida(processodt.getId(), GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU)
				) 
				{
					request.setAttribute("MensagemOk", Configuracao.getMensagem(Configuracao.MENSAGEM_VINCULAR_GUIA_INICIAL_COMPLEMENTAR_JA_EXISTE));		
				}
				
				break;
			}
			case Configuracao.Salvar: {
				
				if( guiaEmissaoNe.isCodigoServentiaProcessoDiferenteCodigoServentiaGuia(processodt, numeroCompletoGuiaInicial) ) {
					request.setAttribute("MensagemOk", "Foi identificado que o código da serventia da guia é diferente do código da serventia do processo. Por favor, confira se o número da guia pertence a este processo. Caso esteja seguro de que esta guia realmente pertence à este processo, basta prosseguir clicando em salvar. <br /><br /> Número da guia informado: <b>" + Funcoes.FormatarNumeroSerieGuia(numeroCompletoGuiaInicial.replace("/", "").replace("-", "").replace(" ", "")) + "</b>.");
				}
				
				if( numeroCompletoGuiaInicial == null || numeroCompletoGuiaInicial.isEmpty()
					|| numeroProcessoCompleto == null || numeroProcessoCompleto.isEmpty() ) {
					throw new MensagemException("Por favor, informe o número da guia completo e o número do processo completo.");
				}
				
				request.setAttribute("Mensagem", "Clique para confirmar a vinculação da guia inicial ao processo");				
				
				break;
			}
			case Configuracao.SalvarResultado: {
				try {
					if( numeroCompletoGuiaInicial == null || numeroCompletoGuiaInicial.isEmpty()
						|| numeroProcessoCompleto == null || numeroProcessoCompleto.isEmpty() ) {
						throw new MensagemException("Por favor, informe o número da guia completo e o número do processo completo.");
					}
					
					if( !guiaEmissaoNe.isNumeroProcessoInformadoIgualProcesso(numeroProcessoCompleto, processodt.getId()) ) {
						throw new MensagemException("Atenção, o número do processo informado não corresponde ao processo atual.");
					}
					
					guiaEmissaoNe.vinculeGuia(UsuarioSessao.getUsuarioDt(), numeroCompletoGuiaInicial, processodt, null);
					request.setAttribute("MensagemOk", "Guia Vinculada ao processo com sucesso!");	
				} catch(MensagemException ex) {
					request.setAttribute("MensagemErro", ex.getMessage());
				}
				
				break;
			}
		}		
		
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
