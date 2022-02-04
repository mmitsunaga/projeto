package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.BairroGuiaLocomocaoDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaItemDt;
import br.gov.go.tj.projudi.dt.LocomocaoDt;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class GuiaLocomocaoNe extends GuiaEmissaoNe {

	private static final long serialVersionUID = -2219587735528188707L;
	
	public String valideLocomocaoFinalidadeEModelo(GuiaEmissaoDt guiaEmissaoDt) {
		String mensagem = "";
		
		if( guiaEmissaoDt.getFinalidade() == null || guiaEmissaoDt.getFinalidade().trim().length() == 0 ) {			
			mensagem = "Informe a finalidade.";
		} else if (guiaEmissaoDt.getGuiaModeloDt() == null) {
			mensagem = Configuracao.getMensagem(Configuracao.MENSAGEM_MODELO_GUIA_LOCOMOCAO_NAO_EXISTE);
		}
		
		return mensagem;
	}


	public String valideCalculoItensGuia(List<GuiaItemDt> listaGuiaItemDt) throws Exception {
		String mensagem = "";
		
		if (this.isGuiaZeradaOuNegativa()) {
			mensagem = Configuracao.getMensagem(Configuracao.MENSAGEM_ADVERTENCIA_GUIA_ZERADA);								
		} else if( listaGuiaItemDt == null || listaGuiaItemDt.size() == 0 ) { 
			//Deve haver no mínimo 1 item de guia
			mensagem = "Nenhum Item de Guia Localizado.<br/> Informe o oficial de justiça.<br/> A Guia deve Conter 1 ou mais Itens de Custa.";			
		}
		
		return mensagem;
	}


	public String recalcularItensGuiaLocomocaoComplementar(List<GuiaItemDt> listaGuiaItemDt, List<LocomocaoDt> listaLocomocaoNaoUtilizadaDt) throws Exception {
		
		if (listaLocomocaoNaoUtilizadaDt == null || listaLocomocaoNaoUtilizadaDt.size() == 0 || listaGuiaItemDt == null || listaGuiaItemDt.size() == 0) return "";
		
		Double saldoValorCalculadoLocomocoes = 0d;
		Double saldoValorCalculadoTJGO = 0d;
		Double totalGuia = this.getGuiaCalculoNe().getTotalGuia();
		
		int numeroDaLocomocao = 0;
		for (LocomocaoDt locomocaoDt : listaLocomocaoNaoUtilizadaDt) {
			if ((numeroDaLocomocao + 1) > 1 && numeroDaLocomocao < listaLocomocaoNaoUtilizadaDt.size() && (saldoValorCalculadoLocomocoes + saldoValorCalculadoTJGO) >= totalGuia) {
				if (numeroDaLocomocao == 1) return "Somente a primeira Locomoção Não Utilizada é suficiente para quitar essa nova guia emitida. Favor retirar as demais Locomoções Não Utilizadas."; 
				return "Somente as " + numeroDaLocomocao + " primeiras Locomoções Não Utilizadas são suficientes para quitar essa nova guia emitida. Favor retirar as demais Locomoções Não Utilizadas.";
			}				
			
			numeroDaLocomocao += 1;
			
			saldoValorCalculadoLocomocoes += locomocaoDt.getValorCalculadoLocomocoes();
			saldoValorCalculadoTJGO += locomocaoDt.getValorCalculadoTJGO();
		}
		
		if (saldoValorCalculadoLocomocoes == 0 && saldoValorCalculadoTJGO == 0) return "";
				
		for (GuiaItemDt guiaItemDt : listaGuiaItemDt) {
			Double valorCalculadoItem = Funcoes.StringToDouble(guiaItemDt.getValorCalculado());
			guiaItemDt.setValorCalculadoOriginal(valorCalculadoItem.toString());
			if (guiaItemDt.isLocomocaoParaOficialDeJustica() && saldoValorCalculadoLocomocoes > 0) {
				if (saldoValorCalculadoLocomocoes >= valorCalculadoItem) {
					saldoValorCalculadoLocomocoes -= valorCalculadoItem;
					valorCalculadoItem = 0d;					
				} else {
					valorCalculadoItem -= saldoValorCalculadoLocomocoes;
					saldoValorCalculadoLocomocoes = 0d;
				}
				saldoValorCalculadoLocomocoes = Funcoes.retirarCasasDecimais(saldoValorCalculadoLocomocoes);
			} else if (guiaItemDt.isLocomocaoParaTribunal() && saldoValorCalculadoTJGO > 0) {
				if (saldoValorCalculadoTJGO >= valorCalculadoItem) {
					saldoValorCalculadoTJGO -= valorCalculadoItem;
					valorCalculadoItem = 0d;
				} else {
					valorCalculadoItem -= saldoValorCalculadoTJGO;
					saldoValorCalculadoTJGO = 0d;
				}
				saldoValorCalculadoTJGO = Funcoes.retirarCasasDecimais(saldoValorCalculadoTJGO);
			}
			
			valorCalculadoItem = Funcoes.retirarCasasDecimais(valorCalculadoItem);
			guiaItemDt.setValorCalculado(valorCalculadoItem.toString());
		}		
		
		super.recalcularTotalGuia(listaGuiaItemDt);
		
		return "";
	}
	
	
	public String validarLocomocoesIguaisValorZerado(List<GuiaItemDt> listaGuiaItemDt, List<BairroGuiaLocomocaoDt> listaBairroLocomocaoDt) throws Exception {
		
		String mensagem = "";
		
		if( this.getGuiaCalculoNe().getTotalGuia() == 0d && listaGuiaItemDt != null && listaBairroLocomocaoDt != null ) {
			for (GuiaItemDt guiaItemDt : listaGuiaItemDt) {
				for (BairroGuiaLocomocaoDt bairroGuiaLocomocaoDt : listaBairroLocomocaoDt) {
					
					if( guiaItemDt.getLocomocaoDt() != null
						&& guiaItemDt.getLocomocaoDt().getBairroDt() != null
						&& guiaItemDt.getLocomocaoDt().getBairroDt().getId() != null
						&& bairroGuiaLocomocaoDt.getBairroDt() != null
						&& bairroGuiaLocomocaoDt.getBairroDt().getId() != null
						&& guiaItemDt.getLocomocaoDt().getBairroDt().getId().equals(bairroGuiaLocomocaoDt.getBairroDt().getId()) ) {
						
						mensagem = "Por favor, avalie se a complementação é necessária. O Bairro adicionado é igual ao já existente.";
						break;
						
					}
					
				}
			}
		}
		
		return mensagem;
	}
}
