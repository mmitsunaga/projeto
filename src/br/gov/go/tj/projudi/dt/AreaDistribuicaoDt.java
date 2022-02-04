package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.utils.Funcoes;

public class AreaDistribuicaoDt extends AreaDistribuicaoDtGen {

	private static final long serialVersionUID					   = -5262788144366447075L;
	public static final int	  CodigoPermissao					   = 225;
	public static final int	  INATIVO							   = -2;
	public static final int	  BLOQUEADO							   = -1;
	public static final int	  ATIVO								   = 0;
	public static final int	  VARA_CRIMINAL_4VARA_CODIGO		   = 13000391;
	public static final int	  VARA_CRIMINAL_8JUIZADO_CODIGO		   = 13000392;
	public static final int	  VARA_CRIMINAL_6VARA_CODIGO		   = 13000393;
	public static final int	  GOIANIA_VARAS_CIVEIS_COM_CUSTAS	   = 9200;
	public static final int	  GOIANIA_FAMILIA_COM_CUSTAS		   = 130;
	public static final int	  GOIANIA_ARBITRAGEM_COM_CUSTAS		   = 92002;
	public static final int	  GOIANIA_AMBIENTAL_COM_CUSTAS		   = 92001;
	public static final int	  GOIANIA_VARAS_CIVEIS_COM_ASSISTENCIA = 9203;
	public static final int	  GOIANIA_FAMILIA_COM_ASSISTENCIA	   = 129;
	public static final int	  GOIANIA_ARBITRAGEM_COM_ASSISTENCIA   = 92003;
	public static final int	  GOIANIA_AMBIENTAL_COM_ASSISTENCIA	   = 93004;

	// lista contendo as serventias contidas em uma determinada área de distribuição
	private List			  listaServentias;

	public AreaDistribuicaoDt() {
		limpar();
	}

	public List getListaServentias() {
		return this.listaServentias;
	}

	public void setListaServentias(List listaServentias) {

		if (listaServentias != null) {
			this.listaServentias = listaServentias;
		}
	}

	public void limpar() {
		super.limpar();
		listaServentias = new ArrayList();
	}

	public boolean isCriminal() {
		int codigo = Funcoes.StringToInt(this.getServentiaSubtipoCodigo(), -1);
		if (codigo == ServentiaSubtipoDt.CAMARA_CRIMINAL || 
			codigo == ServentiaSubtipoDt.SECAO_CRIMINAL || 
			codigo == ServentiaSubtipoDt.VARA_CRIMINAL || 
			codigo == ServentiaSubtipoDt.JUIZADO_ESPECIAL_CRIMINAL || 
			codigo == ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL_CRIMINAL || 
			codigo == ServentiaSubtipoDt.JUIZADO_VIOLENCIA_DOMESTICA || 
			codigo == ServentiaSubtipoDt.JUIZADO_ESPECIAL_CRIMINAL_FAZENDA_PUBLICA || 
			codigo == ServentiaSubtipoDt.AUDITORIA_MILITAR_CRIMINAL || 
			codigo == ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL_CRIMINAL || 
			codigo == ServentiaSubtipoDt.UPJ_TURMA_RECURSAL ||
			codigo == ServentiaSubtipoDt.UPJ_CRIMINAL ||
			codigo == ServentiaSubtipoDt.UPJ_VIOLENCIA_DOMESTICA ||
			codigo == ServentiaSubtipoDt.UPJ_CUSTODIA ||
			codigo == ServentiaSubtipoDt.TURMA_RECURSAL_CRIMINAL) {
			return true;
		}
		return false;
	}

	public boolean isCivel() {
		int codigo = Funcoes.StringToInt(this.getServentiaSubtipoCodigo(), -1);
		if (codigo == ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL || 
			codigo == ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL_CRIMINAL || 
			codigo == ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL || 
			codigo == ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL_CRIMINAL || 
			codigo == ServentiaSubtipoDt.UPJ_TURMA_RECURSAL ||
			codigo == ServentiaSubtipoDt.CAMARA_CIVEL ||
			codigo == ServentiaSubtipoDt.UPJ_FAMILIA || 
			codigo == ServentiaSubtipoDt.UPJ_JUIZADO_ESPECIAL_FAZENDA_PUBLICA || 
			codigo == ServentiaSubtipoDt.UPJ_SUCESSOES || 
			codigo == ServentiaSubtipoDt.SECAO_CIVEL || 
			codigo == ServentiaSubtipoDt.VARAS_CIVEL || 
			codigo == ServentiaSubtipoDt.INFANCIA_JUVENTUDE_CIVEL || 
			codigo == ServentiaSubtipoDt.AUDITORIA_MILITAR_CIVEL) {
			return true;
		}
		return false;
	}

	public boolean isPrimeiroGrau() {
		return ServentiaSubtipoDt.isPrimeiroGrau(this.getServentiaSubtipoCodigo());
	}
	
	public boolean isSegundoGrau() {
		return ServentiaSubtipoDt.isSegundoGrau(this.getServentiaSubtipoCodigo());
	}

}
