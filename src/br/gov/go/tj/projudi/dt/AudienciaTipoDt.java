package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.utils.Funcoes;

public class AudienciaTipoDt extends AudienciaTipoDtGen {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8061720121364688433L;
	public static final int CodigoPermissao = 230;

	public enum Codigo {
		NENHUM(0), ADMONITORIA(1), CONCILIACAO(2), INTERROGATORIO(3), JUSTIFICACAO(4), EXECUCAO(5), INICIAL(6), 
		INSTRUCAO(7), INSTRUCAO_JULGAMENTO(8), JULGAMENTO(9), PRELIMINAR(10), UNA(11), SESSAO_SEGUNDO_GRAU(12), 
		SINE_DIE(13), INQUIRICAO(14), SUSPENSAO_CONDICIONAL(15), INQUIRICAO_TESTEMUNHA(16), 
		CONCILIACAO_CEJUSC(17), MEDIACAO_CEJUSC(18), CONCILIACAO_CEJUSC_DPVAT(19), PRELIMINAR_CONCILIADOR(21),
		CUSTODIA(22), CONCILIACAO_JULGAMENTO(23), INSPENCAO_JUDICIAL(24), RETRATACAO(25), APRESENTACAO(27), 
		SORTEIO_CEJ(28), CONTINUACAO(29), PERICIA(32), DEPOIMENTO_ESPECIAL(33), ART334(34);

		private int audienciaTipoCodigo;

		private Codigo(int audienciaTipoCodigo) {
			this.audienciaTipoCodigo = audienciaTipoCodigo;
		}

		public int getCodigo() {
			return this.audienciaTipoCodigo;
		}

		public static Codigo getCodigo(int audienciaTipoCodigo) {
			switch (audienciaTipoCodigo) {
				case 0:		return Codigo.NENHUM;
				case 1:		return Codigo.ADMONITORIA;
				case 2:		return Codigo.CONCILIACAO;
				case 3:		return Codigo.INTERROGATORIO;
				case 4:		return Codigo.JUSTIFICACAO;
				case 5:		return Codigo.EXECUCAO;
				case 6:		return Codigo.INICIAL;
				case 7:		return Codigo.INSTRUCAO;
				case 8:		return Codigo.INSTRUCAO_JULGAMENTO;
				case 9:		return Codigo.JULGAMENTO;
				case 10:	return Codigo.PRELIMINAR;
				case 11:	return Codigo.UNA;
				case 12:	return Codigo.SESSAO_SEGUNDO_GRAU;
				case 13:	return Codigo.SINE_DIE;
				case 14:	return Codigo.INQUIRICAO;
				case 15:	return Codigo.SUSPENSAO_CONDICIONAL;
				case 16:	return Codigo.INQUIRICAO_TESTEMUNHA;
				case 17:	return Codigo.CONCILIACAO_CEJUSC;
				case 18:	return Codigo.MEDIACAO_CEJUSC;
				case 19:	return Codigo.CONCILIACAO_CEJUSC_DPVAT;
				case 21:	return Codigo.PRELIMINAR_CONCILIADOR;
				case 22:	return Codigo.CUSTODIA;
				case 23:	return Codigo.CONCILIACAO_JULGAMENTO;
				case 24:	return Codigo.INSPENCAO_JUDICIAL;
				case 25:	return Codigo.RETRATACAO;
				case 27:	return Codigo.APRESENTACAO;				
				case 28:	return Codigo.SORTEIO_CEJ;
				case 29:	return Codigo.CONTINUACAO;
				case 32:	return Codigo.PERICIA;
				case 33:	return Codigo.DEPOIMENTO_ESPECIAL;
				case 34:	return Codigo.ART334;
			}
			return null;
		}
		
		public static String getDescricao(String audienciaTipoCodigo) {
			int codigo = Funcoes.StringToInt(audienciaTipoCodigo);
			return getDescricao(codigo);
		}
		
		public static String getDescricao(int audienciaTipoCodigo) {
			switch (getCodigo(audienciaTipoCodigo)) {
				case ADMONITORIA:				return "ADMONITÓRIA";
				case CONCILIACAO:				return "CONCILIAÇÃO";
				case INTERROGATORIO:			return "INTERROGATÓRIO";
				case JUSTIFICACAO:				return "JUSTIFICAÇÃO";
				case EXECUCAO:					return "EXECUÇÃO";
				case INICIAL:					return "INICIAL";
				case INSTRUCAO:					return "INSTRUÇÃO";
				case INSTRUCAO_JULGAMENTO:		return "INSTRUÇÃO E JULGAMENTO";
				case JULGAMENTO:				return "JULGAMENTO";
				case PRELIMINAR:				return "PRELIMINAR";
				case UNA:						return "UNA";
				case SESSAO_SEGUNDO_GRAU:		return "SESSÃO 2º GRAU";
				case SINE_DIE:					return "SINE DIE";
				case INQUIRICAO:				return "INQUIRIÇÃO";
				case SUSPENSAO_CONDICIONAL:		return "SUSPENSÃO CONDICIONAL";
				case INQUIRICAO_TESTEMUNHA:		return "INQUIRICAO DE TESTEMUNHA";
				case CONCILIACAO_CEJUSC:		return "CONCILIAÇÃO CEJUSC";
				case MEDIACAO_CEJUSC:			return "MEDIAÇÃO CEJUSC";
				case CONCILIACAO_CEJUSC_DPVAT:	return "CONCILIAÇÃO CEJUSC DPVAT";
				case PRELIMINAR_CONCILIADOR:	return "PRELIMINAR COM CONCILIADOR";
				case CUSTODIA:					return "CUSTÓDIA";
				case CONCILIACAO_JULGAMENTO:	return "CONCILIAÇÃO E JULGAMENTO";
				case INSPENCAO_JUDICIAL:		return "INSPEÇÃO JUDICIAL";
				case RETRATACAO:				return "RETRATAÇÃO";
				case APRESENTACAO:				return "APRESENTAÇÃO";
				case CONTINUACAO:				return "CONTINUAÇÃO";
				case SORTEIO_CEJ:				return "SORTEIO DE CEJ";
				case PERICIA:					return "Perícia";
				case DEPOIMENTO_ESPECIAL:		return "Depoimento Especia (Sem Dano)";
				case ART334:					return "Artigo 334 CPC";
				default:						return "";
				
			}
			
		}
	}
}
