package br.gov.go.tj.projudi.ne;

import javax.servlet.http.HttpServletRequest;

import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaStatusDt;
import br.gov.go.tj.projudi.dt.ProcessoSPGDt;
import br.gov.go.tj.projudi.dt.ProcessoSSGDt;
import br.gov.go.tj.projudi.ne.boletos.BoletoDt;
import br.gov.go.tj.projudi.ne.boletos.BoletoNe;
import br.gov.go.tj.projudi.ne.boletos.PagadorBoleto;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;


public class GerarBoletoNe extends GuiaEmissaoNe {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1458389148072613495L;

	public GuiaEmissaoDt consultarGuiaEmissaoParaConversao(String numeroGuiaCompleto) throws Exception {
		
		if (numeroGuiaCompleto == null || numeroGuiaCompleto.trim().isEmpty()) return null;
		
		GuiaEmissaoDt guiaEmissaoDt = consultarGuiaEmissaoParaConversaoInterno(numeroGuiaCompleto);	
		
		if (guiaEmissaoDt == null &&
			 numeroGuiaCompleto.length() > 1 &&
			!numeroGuiaCompleto.endsWith("0") &&
			!numeroGuiaCompleto.substring(numeroGuiaCompleto.length() - 2, numeroGuiaCompleto.length() - 1).equals("0")) {			
			String novoCodigo = numeroGuiaCompleto.substring(0, numeroGuiaCompleto.length()-1);//retira último caractere.
			novoCodigo += "0";//acrescente o zero.
			novoCodigo += numeroGuiaCompleto.substring(numeroGuiaCompleto.length()-1, numeroGuiaCompleto.length());//acrescenta o último caractere.			
			guiaEmissaoDt = consultarGuiaEmissaoParaConversaoInterno(novoCodigo);
		}
		
		return guiaEmissaoDt;
	}
	
	private GuiaEmissaoDt consultarGuiaEmissaoParaConversaoInterno(String numeroGuiaCompleto) throws Exception {
		//Consulta guia PJD
		GuiaEmissaoDt guiaEmissaoDt = super.consultarGuiaEmissaoNumeroGuiaNumeroProcesso(numeroGuiaCompleto);	
		
		if( numeroGuiaCompleto.contains(".") ) {
			throw new MensagemException("Número da Guia informado não pode conter pontos(.) [Erro5]");
		}
		
		//Consulta guia SPG
		if (guiaEmissaoDt == null) {
			guiaEmissaoDt = consultarGuiaEmissaoSPGComProcesso(numeroGuiaCompleto);
		} 
		
		//Consulta guia SSG
		if (guiaEmissaoDt == null) {
			guiaEmissaoDt = consultarGuiaEmissaoSSGComProcesso(numeroGuiaCompleto);
		} 
		
		return guiaEmissaoDt;
	}
	
	private GuiaEmissaoDt consultarGuiaEmissaoSPGComProcesso(String numeroGuiaCompleto) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = new GuiaSPGNe().consultarGuiaEmissaoSPG(numeroGuiaCompleto);
		if (guiaEmissaoDt != null) {
			if (guiaEmissaoDt.getNumeroProcessoSPG() != null && guiaEmissaoDt.getNumeroProcessoSPG().trim().length() > 0) {
				try {
					ProcessoSPGDt processoSPGDt = new ProcessoSPGNe().consulteProcessoNumeroSPG(guiaEmissaoDt.getNumeroProcessoSPG()); 
					if (processoSPGDt != null && processoSPGDt.getNumeroProcessoCompletoDt() != null) {
						//getNumeroCompletoProcesso
						guiaEmissaoDt.setNumeroCompletoProcesso(processoSPGDt.getNumeroProcessoCompletoDt().getNumeroCompletoProcesso());	
					} else {
						guiaEmissaoDt.setNumeroCompletoProcesso(guiaEmissaoDt.getNumeroProcessoSPG());	
					}
				} catch (Exception e) {
					guiaEmissaoDt.setNumeroCompletoProcesso(guiaEmissaoDt.getNumeroProcessoSPG());
				}
			} else if (guiaEmissaoDt.getNumeroProcesso() != null) {
				guiaEmissaoDt.setNumeroCompletoProcesso(guiaEmissaoDt.getNumeroProcesso());
			}
		}		
		return guiaEmissaoDt;
	}
	
	private GuiaEmissaoDt consultarGuiaEmissaoSSGComProcesso(String numeroGuiaCompleto) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = new GuiaSSGNe().consultarGuiaEmissaoSSG(numeroGuiaCompleto);
		if (guiaEmissaoDt != null) {
			if (guiaEmissaoDt.getNumeroProcessoSPG() != null && guiaEmissaoDt.getNumeroProcessoSPG().trim().length() > 0) {
				try {
					ProcessoSSGDt processoSSGDt = new ProcessoSSGNe().consulteProcessoNumeroSSG(guiaEmissaoDt.getNumeroProcessoSPG()); 
					if (processoSSGDt != null && processoSSGDt.getNumeroProcessoCompletoDt() != null) {
						guiaEmissaoDt.setNumeroCompletoProcesso(processoSSGDt.getNumeroProcessoCompletoDt().getNumeroCompletoProcesso());	
					} else {
						guiaEmissaoDt.setNumeroCompletoProcesso(guiaEmissaoDt.getNumeroProcessoSPG());	
					}
				} catch (Exception e) {
					guiaEmissaoDt.setNumeroCompletoProcesso(guiaEmissaoDt.getNumeroProcessoSPG());
				}
			} else if (guiaEmissaoDt.getNumeroProcesso() != null) {
				guiaEmissaoDt.setNumeroCompletoProcesso(guiaEmissaoDt.getNumeroProcesso());
			}	
		}
		return guiaEmissaoDt;
	}
	
	public BoletoDt consultarBoleto(GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		BoletoDt boletoRegistrado = null;
		if (guiaEmissaoDt.isGuiaEmitidaSPG()) {
			GuiaSPGNe guiaSpgNe = new GuiaSPGNe();
			boletoRegistrado = guiaSpgNe.buscaBoletoPorNumero(guiaEmissaoDt.getNumeroGuiaCompleto());
			if (boletoRegistrado != null) boletoRegistrado.setNumeroCompletoProcesso(guiaEmissaoDt.getNumeroCompletoProcesso());
		} else if (guiaEmissaoDt.isGuiaEmitidaSSG()) {
			GuiaSSGNe guiaSsgNe = new GuiaSSGNe();
			boletoRegistrado = guiaSsgNe.buscaBoletoPorNumero(guiaEmissaoDt.getNumeroGuiaCompleto());
			if (boletoRegistrado != null) boletoRegistrado.setNumeroCompletoProcesso(guiaEmissaoDt.getNumeroCompletoProcesso());
		} else {
			boletoRegistrado = super.BuscarBoletoPorId(guiaEmissaoDt.getId());
		}
		if (boletoRegistrado != null) return boletoRegistrado;
		return BoletoNe.get().transformar(guiaEmissaoDt, new PagadorBoleto());
	}

	public String verificarDadosPagador(BoletoDt guiaEmissaoBoletoDt) {
		String mensagem = "";
		if (guiaEmissaoBoletoDt.getPagador().isPessoaFisica()) {
			if (guiaEmissaoBoletoDt.getPagador().getNome() == null || guiaEmissaoBoletoDt.getPagador().getNome().trim().length() == 0) {
				mensagem = "Informe o nome!";
			}
			if (guiaEmissaoBoletoDt.getPagador().getCpf() == null || guiaEmissaoBoletoDt.getPagador().getCpf().trim().length() == 0) {
				if (mensagem.length() > 0) mensagem += "\n";
				mensagem += "Informe o CPF!";
			} else if (!Funcoes.testaCPFCNPJ(guiaEmissaoBoletoDt.getPagador().getCpf())) {
				if (mensagem.length() > 0) mensagem += "\n";
				mensagem += "CPF inválido!";
			}
		} else {
			if (guiaEmissaoBoletoDt.getPagador().getRazaoSocial() == null || guiaEmissaoBoletoDt.getPagador().getRazaoSocial().trim().length() == 0) {
				mensagem = "Informe a razão social!";
			}
			if (guiaEmissaoBoletoDt.getPagador().getCnpj() == null || guiaEmissaoBoletoDt.getPagador().getCnpj().trim().length() == 0) {
				if (mensagem.length() > 0) mensagem += "\n";
				mensagem += "Informe o CNPJ!";
			} else if (!Funcoes.testaCPFCNPJ(guiaEmissaoBoletoDt.getPagador().getCnpj()) || guiaEmissaoBoletoDt.getPagador().getCnpj().trim().length() <= 11) {
				if (mensagem.length() > 0) mensagem += "\n";
				mensagem += "CNPJ inválido!";
			}
		}
		return mensagem;
	}
	
	public BoletoDt emitirBoleto(BoletoDt guiaEmissaoBoletoDt, UsuarioNe usuarioSessao) throws Exception {
		return BoletoNe.get().emiteBoleto(guiaEmissaoBoletoDt, guiaEmissaoBoletoDt.getPagador(), usuarioSessao);
	}
	
	public void reemitirBoleto(BoletoDt guiaEmissaoBoletoDt, UsuarioNe usuarioSessao) throws Exception {
		BoletoNe.get().reemiteBoleto(guiaEmissaoBoletoDt, usuarioSessao);
	}
	
	public boolean isGuiaStatusAguardandoPagamento(GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		if (guiaEmissaoDt == null) return false;
		
		if (guiaEmissaoDt.isGuiaEmitidaSPG() || guiaEmissaoDt.isGuiaEmitidaSSG()) {
			return guiaEmissaoDt.getId_GuiaStatus() != null && 
				   (guiaEmissaoDt.getId_GuiaStatus().equalsIgnoreCase(String.valueOf(GuiaStatusDt.AGUARDANDO_PAGAMENTO)) ||
				    guiaEmissaoDt.getId_GuiaStatus().equalsIgnoreCase(String.valueOf(GuiaStatusDt.ESTORNO_BANCARIO)));
		} else {
			return isGuiaStatusAguardandoPagamento(guiaEmissaoDt.getNumeroGuiaCompleto());	
		}		
	}

	public String obtenhaUrlPdf(BoletoDt guiaEmissaoBoletoDt) throws Exception {
		return BoletoNe.get().consultaUrlPdf(guiaEmissaoBoletoDt);
	}
	
	public void limparGuiaSessao(HttpServletRequest request) {
		request.getSession().removeAttribute("ListaGuiaItemDt");
		request.getSession().removeAttribute("TotalGuia");
		request.getSession().removeAttribute("GuiaEmissaoBoletoDt");
		request.getSession().removeAttribute("GuiaEmissaoDtBase");
		if( request.getSession().getAttribute("guiaInicialPublica") != null && Integer.parseInt(request.getSession().getAttribute("guiaInicialPublica").toString()) > 0 ) {
			for( int i = 0; i < Integer.parseInt(request.getSession().getAttribute("guiaInicialPublica").toString()); i++ ) {
				request.getSession().removeAttribute("guiaInicialPublicaQuantidadeLocomocao"+i);
			}
		}
		request.getSession().removeAttribute("guiaInicialPublica");		
	}
}
