package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.utils.Funcoes;

public class ArquivoTipoDt extends ArquivoTipoDtGen {

	private static final long serialVersionUID = -8885251148608711921L;

	public static final int CodigoPermissao = 180;

	public static final int OUTROS = 1;
	public static final int CONCLUSAO = 3;
	public static final int PETICAO_INICIAL = 6;
	public static final int CERTIDAO = 13;
	public static final int SENTENCA = 14;
	public static final int DECISAO = 15;
	public static final int ACORDAO = 22;
	public static final int TERMO_CIRCUNSTANCIADO = 24;
	public static final int DESPACHO = 38;
	public static final int SENTENCA_HOMOLOGACAO = 50;
	public static final int ID_INQUERITO_POLICIAL = 51;
	public static final int VERIFICAR_PREVENCAO = 52;
	public static final int PUBLICACAO_PUBLICA = 53; // Tipo de arquivo especifico para pendencia
	public static final int CERTIDAO_ANTECEDENTE_CRIMINAL = 65;
	public static final int CONFIGURACAO_PRE_ANALISE = 100;
	public static final int CERTIDAO_NEGATIVA_POSITIVA_CIVEL = 112;
	public static final int CERTIDAO_NEGATIVA_POSITIVA_CRIMINAL = 113;
	public static final int CODIGO_ACESSO = 114;
	public static final int CERTIDAO_CIRCUNSTANCIADA_CODIGO = 116;
	public static final int RELATORIO = 123;
	public static final int EMENTA = 124;
	public static final int RELATORIO_VOTO = 125;
	public static final int EXTRATO_ATA_SESSAO = 134;
	public static final int MANDADO = 33;
	public static final int MANDADO_PRISAO = 148;
	public static final int REQUISICAO_PEQUENO_VALOR = 150;
	public static final int INTIMACAO_VIA_DIARIO_ELETRONICO = 190;
	public static final int ID_AUTO_INVESTIGACAO = 197;
	public static final int ID_ALVARA_SOLTURA = 198;
	public static final int ID_CORREIO_RECIBO = 220;
	public static final int ID_CORREIO_INCONSISTENCIA = 221;
	public static final int ID_CORREIO_AVISO_RECEBIMENTO = 222;
	public static final int ID_INTIMACAO = 5;
	public static final int ID_CARTA_CITACAO = 2;
	public static final int ID_MODELO_ECARTA = 223;
	public static final int ID_CORREIO_ENTREGA = 224;
	public static final int ID_CORREIO_POSTAGEM = 225;
	public static final int MIDIA_DIGITAL = 226;
	public static final int ID_CORREIO_DEVOLUCAO_AR = 227;
	
	// jvosantos - 09/07/2019 15:13 - Mapear tipo de pendencia de VOTO
	public static final int VOTO = 118;

	public void setId_ArquivoTipo(String id_ArquivoTipo) {
		if (id_ArquivoTipo != null)
			if (id_ArquivoTipo.equalsIgnoreCase("null")) {
				super.setId("");
				super.setArquivoTipo("");
			} else if (!id_ArquivoTipo.equalsIgnoreCase(""))
				super.setId(id_ArquivoTipo);
	}

	public void setArquivoTipo(String arquivoTipo) {
		if (arquivoTipo != null)
			if (arquivoTipo.equalsIgnoreCase("null"))
				super.setArquivoTipo("");
			else if (!arquivoTipo.equalsIgnoreCase(""))
				super.setArquivoTipo(arquivoTipo);
	}
	
	public static boolean isMidiaDigitalUpload(String arquivoTipoIdCodigo) {
		return (Funcoes.StringToLong(arquivoTipoIdCodigo, -100) == ArquivoTipoDt.MIDIA_DIGITAL);
	}
}