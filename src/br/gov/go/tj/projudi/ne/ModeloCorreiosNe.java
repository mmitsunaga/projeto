package br.gov.go.tj.projudi.ne;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;

import br.gov.go.tj.projudi.dt.CorreiosDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.PendenciaArquivoDt;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Cifrar;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.pdf.ConverterHtmlPdf;

public class ModeloCorreiosNe {

	private static final long serialVersionUID = 3475455296265004136L;
	
	public static final String BRASAO_GOIAS 				= "brasao.goias";
	public static final String GUIA_NUMERO 					= "guia.numero";
	public static final String PROCESSO_NUMERO 				= "processo.numero";
	public static final String PROCESSO_TIPO 				= "processo.tipo";
	public static final String SERVENTIA_COMARCA 			= "serventia.comarca";
	public static final String SERVENTIA_NOME 				= "serventia.nome";
	public static final String SERVENTIA_ENDERECO			= "serventia.endereco";
	public static final String SERVENTIA_TELEFONE			= "serventia.telefone";
	public static final String SERVENTIA_EMAIL				= "serventia.email";
	public static final String PROCESSO_VALOR 				= "processo.valor";
	public static final String PROCESSO_POLOATIVO 			= "processo.poloativo";
	public static final String PROCESSO_POLOPASSIVO 		= "processo.polopassivo";
	public static final String MOVIMENTACAO_DESCRICAO		= "movimentacao.descricao";
	public static final String MOVIMENTACAO_DATA			= "movimentacao.data";
	public static final String MOVIMENTACAO_CODIGOACESSO	= "movimentacao.codigoacesso";
	public static final String PROCESSO_AUDIENCIA			= "processo.audiencia";
	public static final String PROCESSO_DESTINATARIO		= "processo.destinatario";
	public static final String PROCESSO_CODIGOACESSO		= "processo.codigoacesso";
	public static final String AUDIENCIA_DATA				= "audiencia.data";
	public static final String AUDIENCIA_HORA				= "audiencia.hora";
	public static final String AUDIENCIA_ENDERECO			= "audiencia.endereco";
	public static final String DATA 						= "data";
	public static final String CADASTRADOR_NOME				= "cadastrador.nome";
	public static final String CADASTRADOR_CARGO			= "cadastrador.cargo";

	public String montaConteudo(CorreiosDt carta, String texto) throws Exception{
		ProjudiPropriedades projudiPropriedades = ProjudiPropriedades.getInstance();
		ProcessoNe processoNe = new ProcessoNe();
		
		if(carta.getNumeroGuia() != null && carta.getNumeroGuia().length()>0) {  			 	texto = texto.replaceAll("[$][{][\\s]*" + GUIA_NUMERO + "[\\s]*[}]", 				format(carta.getNumeroGuia())); }else {																				texto = texto.replaceAll("[$][{][\\s]*" + GUIA_NUMERO + "[\\s]*[}]", "");};
		if(carta.getProcessoNumero() != null && carta.getProcessoNumero().length()>0) {		 	texto = texto.replaceAll("[$][{][\\s]*" + PROCESSO_NUMERO + "[\\s]*[}]", 			format(carta.getProcessoNumeroCompleto())); }else {																	texto = texto.replaceAll("[$][{][\\s]*" + PROCESSO_NUMERO + "[\\s]*[}]", "");};
		if(carta.getProcessoTipo() != null && carta.getProcessoTipo().length()>0) { 		 	texto = texto.replaceAll("[$][{][\\s]*" + PROCESSO_TIPO + "[\\s]*[}]", 				format(carta.getProcessoTipo())); }else {																			texto = texto.replaceAll("[$][{][\\s]*" + PROCESSO_TIPO + "[\\s]*[}]", "");};
		if(carta.getComarca() != null && carta.getComarca().length()>0) { 					 	texto = texto.replaceAll("[$][{][\\s]*" + SERVENTIA_COMARCA + "[\\s]*[}]", 			format(carta.getComarca())); }else {																				texto = texto.replaceAll("[$][{][\\s]*" + SERVENTIA_COMARCA + "[\\s]*[}]", "");};
		if(carta.getNomeRemetente() != null && carta.getNomeRemetente().length()>0) {  		 	texto = texto.replaceAll("[$][{][\\s]*" + SERVENTIA_NOME + "[\\s]*[}]", 			format(carta.getNomeRemetente())); }else {																			texto = texto.replaceAll("[$][{][\\s]*" + SERVENTIA_NOME + "[\\s]*[}]", "");};
		if(carta.getEnderecoRemetente() != null && carta.getEnderecoRemetente().length()>0) {  	texto = texto.replaceAll("[$][{][\\s]*" + SERVENTIA_ENDERECO + "[\\s]*[}]", 		format(Funcoes.capitularNome(carta.getEnderecoRemetente() + ", " + carta.getCidadeRemetente()) + "-" + carta.getUfRemetente())); }else {	texto = texto.replaceAll("[$][{][\\s]*" + SERVENTIA_ENDERECO + "[\\s]*[}]", "");};
		if(carta.getTelefoneServentia() != null && carta.getTelefoneServentia().length()>0) {	texto = texto.replaceAll("[$][{][\\s]*" + SERVENTIA_TELEFONE + "[\\s]*[}]", 		format(carta.getTelefoneServentia())); }else {																		texto = texto.replaceAll("[$][{][\\s]*" + SERVENTIA_TELEFONE + "[\\s]*[}]", "");};
		if(carta.getEmailRemetente() != null && carta.getEmailRemetente().length()>0) {			texto = texto.replaceAll("[$][{][\\s]*" + SERVENTIA_EMAIL + "[\\s]*[}]", 			format(carta.getEmailRemetente())); }else {																			texto = texto.replaceAll("[$][{][\\s]*" + SERVENTIA_EMAIL + "[\\s]*[}]", "");};
		if(carta.getValorCausa() != null && carta.getValorCausa().length()>0) {  			 	texto = texto.replaceAll("[$][{][\\s]*" + PROCESSO_VALOR + "[\\s]*[}]", 			format(carta.getValorCausa())); }else {																				texto = texto.replaceAll("[$][{][\\s]*" + PROCESSO_VALOR + "[\\s]*[}]", "");};
		if(carta.getPoloAtivo() != null && carta.getPoloAtivo().length()>0) {			 	 	texto = texto.replaceAll("[$][{][\\s]*" + PROCESSO_POLOATIVO + "[\\s]*[}]", 		format(Funcoes.capitularNome(carta.getPoloAtivo()))); }else {																				texto = texto.replaceAll("[$][{][\\s]*" + PROCESSO_POLOATIVO + "[\\s]*[}]", "");};
		if(carta.getPoloPassivo() != null && carta.getPoloPassivo().length()>0) {  			 	texto = texto.replaceAll("[$][{][\\s]*" + PROCESSO_POLOPASSIVO + "[\\s]*[}]", 		format(Funcoes.capitularNome(carta.getPoloPassivo()))); }else {																			texto = texto.replaceAll("[$][{][\\s]*" + PROCESSO_POLOPASSIVO + "[\\s]*[}]", "");};
		if(carta.getMovimentacaoTipo() != null && carta.getMovimentacaoTipo().length()>0) {	 	texto = texto.replaceAll("[$][{][\\s]*" + MOVIMENTACAO_DESCRICAO + "[\\s]*[}]", 	format(carta.getMovimentacaoTipo() + " " + carta.getMovimentacaoComplemento())); }else {							texto = texto.replaceAll("[$][{][\\s]*" + MOVIMENTACAO_DESCRICAO + "[\\s]*[}]", "");};
		if(carta.getMovimentacaoData() != null && carta.getMovimentacaoData().length()>0) {	 	texto = texto.replaceAll("[$][{][\\s]*" + MOVIMENTACAO_DATA + "[\\s]*[}]", 			format(carta.getMovimentacaoData())); }else {																		texto = texto.replaceAll("[$][{][\\s]*" + MOVIMENTACAO_DATA + "[\\s]*[}]", "");};
		if(carta.getId_Arquivo() != null && carta.getId_Arquivo().size()>0) {  			 		texto = texto.replaceAll("[$][{][\\s]*" + MOVIMENTACAO_CODIGOACESSO + "[\\s]*[}]",	format(gerarCodigoMovimentacao(carta.getId_Arquivo()))); }else {													texto = texto.replaceAll("[$][{][\\s]*" + MOVIMENTACAO_CODIGOACESSO + "[\\s]*[}]", "");};
		if(carta.getDataAudiencia() != null && carta.getDataAudiencia().length()>0) { 			texto = texto.replaceAll("[$][{][\\s]*" + PROCESSO_AUDIENCIA + "[\\s]*[}]", 		format("Audiência: "+carta.getDataAudiencia()+" às "+carta.getHoraAudiencia()+", "+Funcoes.capitularNome(carta.getEnderecoRemetente()) + ", " + Funcoes.capitularNome(carta.getCidadeRemetente()) + "-" + carta.getUfRemetente())); }else {texto = texto.replaceAll("[$][{][\\s]*" + PROCESSO_AUDIENCIA + "[\\s]*[}]", "");};
		if(carta.getNomeDestinatario() != null && carta.getNomeDestinatario().length()>0) {	 	texto = texto.replaceAll("[$][{][\\s]*" + PROCESSO_DESTINATARIO + "[\\s]*[}]", 		format(Funcoes.capitularNome(carta.getNomeDestinatario()))); }else {																		texto = texto.replaceAll("[$][{][\\s]*" + PROCESSO_DESTINATARIO + "[\\s]*[}]", "");};
		if(carta.getDataAudiencia() != null && carta.getDataAudiencia().length()>0) {	 		texto = texto.replaceAll("[$][{][\\s]*" + AUDIENCIA_DATA + "[\\s]*[}]", 			format(carta.getDataAudiencia())); }else {																			texto = texto.replaceAll("[$][{][\\s]*" + AUDIENCIA_DATA + "[\\s]*[}]", "");};
		if(carta.getHoraAudiencia() != null && carta.getHoraAudiencia().length()>0) {	 		texto = texto.replaceAll("[$][{][\\s]*" + AUDIENCIA_HORA + "[\\s]*[}]", 			format(carta.getHoraAudiencia())); }else {																			texto = texto.replaceAll("[$][{][\\s]*" + AUDIENCIA_HORA + "[\\s]*[}]", "");};
		if(carta.getEnderecoRemetente() != null && carta.getEnderecoRemetente().length()>0) {	texto = texto.replaceAll("[$][{][\\s]*" + AUDIENCIA_ENDERECO + "[\\s]*[}]", 		format(Funcoes.capitularNome(carta.getEnderecoRemetente()) + ", " + Funcoes.capitularNome(carta.getCidadeRemetente()) + "-" + carta.getUfRemetente())); }else {	texto = texto.replaceAll("[$][{][\\s]*" + AUDIENCIA_ENDERECO + "[\\s]*[}]", "");};
		if(carta.getId_Processo() != null && carta.getId_ProcessoParte() != null) {			 	texto = texto.replaceAll("[$][{][\\s]*" + PROCESSO_CODIGOACESSO + "[\\s]*[}]", 		format(processoNe.gerarCodigoAcessoProcesso(carta.getId_Processo(), carta.getId_ProcessoParte()))); }else {			texto = texto.replaceAll("[$][{][\\s]*" + PROCESSO_CODIGOACESSO + "[\\s]*[}]", "");};
		if(carta.getUsuarioCadastrador() != null && carta.getUsuarioCadastrador().length()>0) {	texto = texto.replaceAll("[$][{][\\s]*" + CADASTRADOR_NOME + "[\\s]*[}]", 			format(Funcoes.capitularNome(carta.getUsuarioCadastrador()))); }else {												texto = texto.replaceAll("[$][{][\\s]*" + CADASTRADOR_NOME + "[\\s]*[}]", "");};
		if(carta.getCargoCadastrador() != null && carta.getCargoCadastrador().size()>0) {		texto = texto.replaceAll("[$][{][\\s]*" + CADASTRADOR_CARGO + "[\\s]*[}]", 			format(abreviarCargo(carta.getCargoCadastrador()))); }else {														texto = texto.replaceAll("[$][{][\\s]*" + CADASTRADOR_CARGO + "[\\s]*[}]", "");};
		if(carta.getDataExpedicao() != null && carta.getDataExpedicao().length()>0) {
	        Date date = new SimpleDateFormat("dd/MM/yyyy").parse(carta.getDataExpedicao());
			texto = texto.replaceAll("[$][{][\\s]*" + DATA + "[\\s]*[}]", format(DateFormat.getDateInstance(DateFormat.LONG, new Locale("pt", "BR")).format(date)).toLowerCase());
		} else {
			Date data = new Date();
			carta.setDataExpedicao(Funcoes.FormatarData(data));
			texto = texto.replaceAll("[$][{][\\s]*" + DATA + "[\\s]*[}]", format(DateFormat.getDateInstance(DateFormat.LONG, new Locale("pt", "BR")).format(data)).toLowerCase());
		}		
		texto = texto.replaceAll("[$][{][\\s]*" + BRASAO_GOIAS + "[\\s]*[}]", "<img src=\"" +ConverterHtmlPdf.BRASAO_BASE64_REDUZIDO+"\"></img>");
		

		//Retorna o tratamento do texto
		return this.tratamento(texto);
	}
	
	private String format(String s) {
		if (s == null) return "";
		//O escapeHtml foi usado para evitar que caracteres especiais (ex: $,&,etc) interfiram nas tags do HTML
		return Matcher.quoteReplacement(org.apache.commons.lang.StringEscapeUtils.escapeHtml(s.trim()));
	}	
	
	private String tratamento(String texto) {
		return texto.replace("\'", "\\'");
	}
	
	private String abreviarCargo(List<String> cargos) {
		Map<String, String> cargo = null;
		for (String cargoCodigo : cargos) {
			cargo = new HashMap<String, String>();
			switch (Funcoes.StringToInt(cargoCodigo)) {
			case GrupoDt.ESTAGIARIO_GABINETE:
			case GrupoDt.ESTAGIARIO_PRIMEIRO_GRAU:
			case GrupoDt.ESTAGIARIO_SEGUNDO_GRAU:
				cargo.put("Estagiário", "Estagiário") ;
				break;	
			case GrupoDt.ASSISTENTE_GABINETE_FLUXO:
				cargo.put("Assistente", "Assistente");
				break;
			case GrupoDt.ANALISTA_JUDICIARIO_INFANCIA_JUVENTUDE_CIVEL:
			case GrupoDt.ANALISTA_JUDICIARIO_INFANCIA_JUVENTUDE_INFRACIONAL:
			case GrupoDt.ANALISTA_JUDICIARIO_PRESIDENCIA:
			case GrupoDt.ANALISTA_JUDICIARIO_PRIMEIRO_GRAU_CIVEL:
			case GrupoDt.ANALISTA_JUDICIARIO_PRIMEIRO_GRAU_CRIMINAL:
			case GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CIVEL:
			case GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CRIMINAL:
			case GrupoDt.ANALISTAS_JUDICIARIOS_TURMA_RECURSAL:
			case GrupoDt.ANALISTAS_EXECUCAO_PENAL:
			case GrupoDt.ANALISTAS_CEJUSC:
			case GrupoDt.ANALISTA_PRE_PROCESSUAL:
			case GrupoDt.ANALISTA_CALCULO_EXECUCAO_PENAL:
			case GrupoDt.ANALISTA_FINANCEIRO:
			case GrupoDt.ANALISTA_CALCULO_PROCESSO_FISICO:
			case GrupoDt.ANALISTA_FORENSE:
			case GrupoDt.ANALISTA_FORENSE_2_GRAU:
				cargo.put("Analista Judiciário", "Analista Judiciário");
				break;
			case GrupoDt.TECNICO_EXECUCAO_PENAL:
			case GrupoDt.TECNICO_JUDICIARIO_PRIMEIRO_GRAU_CIVEL:
			case GrupoDt.TECNICO_JUDICIARIO_PRIMEIRO_GRAU_CRIMINAL:
			case GrupoDt.TECNICO_JUDICIARIO_SEGUNDO_GRAU_CIVEL:
			case GrupoDt.TECNICO_JUDICIARIO_SEGUNDO_GRAU_CRIMINAL:
			case GrupoDt.TECNICOS_JUDICIARIOS_TURMA_RECURSAL:
				cargo.put("Técnico Judiciário", "Técnico Judiciário");
				break;
			case GrupoDt.DISTRIBUIDOR_PRIMEIRO_GRAU:
			case GrupoDt.DISTRIBUIDOR_CAMARA:
			case GrupoDt.DISTRIBUIDOR_CRIMINAL:
			case GrupoDt.DISTRIBUIDOR_GABINETE:
			case GrupoDt.DISTRIBUIDOR_SEGUNDO_GRAU:
				cargo.put("Distribuidor", "Distribuidor");
				break;
			case GrupoDt.JUIZ_AUXILIAR_PRESIDENCIA:
			case GrupoDt.JUIZ_CORREGEDOR:
			case GrupoDt.JUIZ_EXECUCAO_PENAL:
			case GrupoDt.JUIZ_INFANCIA_JUVENTUDE_CIVEL:
			case GrupoDt.JUIZ_INFANCIA_JUVENTUDE_INFRACIONAL:
			case GrupoDt.JUIZ_LEIGO:
			case GrupoDt.JUIZES_TURMA_RECURSAL:
			case GrupoDt.MAGISTRADO_UPJ_PRIMEIRO_GRAU:
			case GrupoDt.JUIZES_VARA:
				cargo.put("Juiz", "Juiz");
				break;
			case GrupoDt.OFICIAL_JUSTICA:
				cargo.put("Oficial de Justiça", "Oficial de Justiça");
				break;	
			case GrupoDt.ASSESSOR:
			case GrupoDt.ASSESSOR_DESEMBARGADOR:
			case GrupoDt.ASSESSOR_GABINETE_PRESIDENTE:
			case GrupoDt.ASSESSOR_JUIZES_SEGUNDO_GRAU:
			case GrupoDt.ASSESSOR_JUIZES_VARA:
			case GrupoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:
				cargo.put("Assessor", "Assessor");
				break;	
			case GrupoDt.DESEMBARGADOR:
				cargo.put("Desembargador", "Desembargador");
				break;
			default:
				cargo.put("Servidor Judiciário", "Servidor Judiciário");
			}
		}
		if(cargo.containsKey("Desembargador")) 				return "Desembargador";
		else if(cargo.containsKey("Juiz")) 					return "Juiz";
		else if(cargo.containsKey("Analista Judiciário")) 	return "Analista Judiciário";
		else if(cargo.containsKey("Técnico Judiciário")) 	return "Técnico Judiciário";
		else if(cargo.containsKey("Oficial de Justiça")) 	return "Oficial de Justiça";
		else if(cargo.containsKey("Distribuidor")) 			return "Distribuidor";
		else if(cargo.containsKey("Assistente")) 			return "Assistente";
		else if(cargo.containsKey("Estagiário")) 			return "Estagiário";
		else 												return "Servidor Judiciário";
	}
	
	private String gerarCodigoMovimentacao(List<String> id_Arquivo) {
		String codigoAcesso = "";
		for (int i = 0; i < id_Arquivo.size(); i++) {
			if(i > 0) {
				codigoAcesso += ", ";
			}
			codigoAcesso += Cifrar.codificar(id_Arquivo.get(i), PendenciaArquivoDt.CodigoPermissao);
		}
		return codigoAcesso;
	}
	
}