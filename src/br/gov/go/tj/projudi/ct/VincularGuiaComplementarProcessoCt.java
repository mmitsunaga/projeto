package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.VincularGuiaComplementarProcessoDt;
import br.gov.go.tj.projudi.ne.GuiaEmissaoNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class VincularGuiaComplementarProcessoCt extends Controle {

	private static final long serialVersionUID = 8561914925893179050L;

	@Override
	public int Permissao() {
		return 879;
	}

	@Override
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {
		GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();;	
		ProcessoDt processodt = null;
		VincularGuiaComplementarProcessoDt vincularGuiaComplementarProcessodt = null;
		String stAcao = "/WEB-INF/jsptjgo/VincularGuiaComplementarProcesso.jsp";
		
		processodt = (ProcessoDt)request.getSession().getAttribute("processoDt");
		if (processodt == null) processodt = new ProcessoDt();
		
		vincularGuiaComplementarProcessodt = (VincularGuiaComplementarProcessoDt)request.getSession().getAttribute("VincularGuiaComplementarProcessodt");
		if (vincularGuiaComplementarProcessodt == null) vincularGuiaComplementarProcessodt = new VincularGuiaComplementarProcessoDt();
		
		String stNomeBusca1 = "";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		vincularGuiaComplementarProcessodt.setNumeroGuiaCompleto(request.getParameter("NumeroCompletoGuiaComplementarVincular"));
		vincularGuiaComplementarProcessodt.setId_ProcessoTipo(request.getParameter("Id_ProcessoTipo"));
		vincularGuiaComplementarProcessodt.setProcessoTipo(request.getParameter("ProcessoTipo"));
		
		request.setAttribute("tempPrograma", "Vincular Guia Complementar ao Processo");
		request.setAttribute("PaginaAnterior", paginaatual);		
		
		switch (paginaatual) {
			case Configuracao.Novo: {
				
				List listaGuiaInicial_Complementar = guiaEmissaoNe.consultarGuiaEmissaoInicial_Complementar(processodt.getId());
				
				String mensagemOk = "";
				
				if( listaGuiaInicial_Complementar != null && !listaGuiaInicial_Complementar.isEmpty() ) {
					mensagemOk += Configuracao.getMensagem(Configuracao.MENSAGEM_VINCULAR_GUIA_INICIAL_COMPLEMENTAR_JA_EXISTE);
				}
				
				request.setAttribute("MensagemOk", mensagemOk);
				request.setAttribute("MensagemErro", "");
				
				vincularGuiaComplementarProcessodt = new VincularGuiaComplementarProcessoDt();
				
				break;
			}
			case Configuracao.Salvar: 
				
				if( new GuiaEmissaoNe().isCodigoServentiaProcessoDiferenteCodigoServentiaGuia(processodt, vincularGuiaComplementarProcessodt.getNumeroGuiaCompleto()) ) {
					request.setAttribute("MensagemOk", "Foi identificado que o código da serventia da guia é diferente do código da serventia do processo. Por favor, confira se o número da guia pertence a este processo. Caso esteja seguro de que esta guia realmente pertence à este processo, basta prosseguir clicando em salvar. <br /><br /> Número da guia informado: <b>" + Funcoes.FormatarNumeroSerieGuia(vincularGuiaComplementarProcessodt.getNumeroGuiaCompleto().replace("/", "").replace("-", "").replace(" ", "")) + "</b>.");
				}
				
				request.setAttribute("Mensagem", "Clique para confirmar a vinculação da guia complementar ao processo");				
				break;
				
			case Configuracao.SalvarResultado:	
				try {
					guiaEmissaoNe.vincularGuiaComplementar(UsuarioSessao.getUsuarioDt(), vincularGuiaComplementarProcessodt, processodt);
					request.setAttribute("MensagemOk", "Guia Vinculada ao processo com sucesso!");	
				} catch(MensagemException ex) {
					request.setAttribute("MensagemErro", ex.getMessage());
				}
				break;
				
			case (ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (processodt != null && processodt.getAreaDistribuicao().length() > 0) {
					
					if (request.getParameter("Passo")==null){
						String[] lisNomeBusca = {"Classe"};
						String[] lisDescricao = {"Classe"};
						stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
						request.setAttribute("tempBuscaId","Id_ProcessoTipo");
						request.setAttribute("tempBuscaDescricao","ProcessoTipo");
						request.setAttribute("tempBuscaPrograma","Classe");			
						request.setAttribute("tempRetorno","VincularGuiaComplementarProcesso");		
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
						request.setAttribute("PaginaAtual", (ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
						request.setAttribute("PosicaoPaginaAtual", "0");
						request.setAttribute("QuantidadePaginas", "0");
						request.setAttribute("lisNomeBusca", lisNomeBusca);
						request.setAttribute("lisDescricao", lisDescricao);
					} else {
						String stTemp="";
						
						if (processodt.getId_AreaDistribuicao() != null & processodt.getId_AreaDistribuicao().length() > 0){							
							stTemp = new ProcessoNe().consultarDescricaoProcessoTipoJSON(stNomeBusca1, processodt.getId_AreaDistribuicao(), UsuarioSessao.getUsuarioDt(), posicaopaginaatual);																					
							enviarJSON(response, stTemp);								
						} else {
							throw new MensagemException("Erro! Processo não possui area de distribuição, entre em contato com o suporte.");
						}
						return;								
					}
				} else {
					request.setAttribute("MensagemErro", "Área de Distribuição não encontrada no processo.");
				}	
				break;
		}		
		
		request.getSession().setAttribute("VincularGuiaComplementarProcessodt", vincularGuiaComplementarProcessodt);
				
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
