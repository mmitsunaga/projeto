package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.gov.go.tj.projudi.types.VotoTipo;

public class VotoTipoDt extends Dados {
	
	private String codigo;
	private String descricao;
	
	public static final int CodigoPermissao = 99999;
	private static final long serialVersionUID = 3630725746167361491L;
	
	public static final int ACOMPANHA_RELATOR = 1;
	public static final int ACOMPANHA_RELATOR_RESSALVA = 2;
	public static final int DIVERGE = 3;
	public static final int ACOMPANHA_DIVERGENCIA = 4;
	public static final int PEDIDO_VISTA = 5;
	public static final int JULGAMENTO_ADIADO = 6;
	public static final int OBSERVACAO = 7;
	public static final int PRAZO_EXPIRADO = 8;
	public static final int IMPEDIDO = 9;
	// jvosantos - 04/06/2019 09:48 - Adicionar o tipo de voto "Proclamação da Decisão" (serve para armazenar os arquivos de voto e ementa do relator)
	public static final int PROCLAMACAO_DECISAO = 10;
	// jvosantos - 29/07/2019 11:33 - Adicionar o tipo de voto "Verificar Resultado da Votação" (gerado ao realizar a pré-analise do verificar resultado da votação)
	public static final int VERIFICAR_RESULTADO_VOTACAO = 11;
	public static final int AGUARDANDO_PEDIDO_VISTA = 12;
	public static final int SUSPEICAO = 13;
	// jvosantos - 06/01/2020 16:27 - Adicionar o tipo de voto "Retorno para o Relator verificar possível erro material" 
	public static final int ERRO_MATERIAL = 14;
	// jvosantos - 06/01/2020 16:27 - Adicionar o tipo de voto para armazenar a analise da pendência de Verificar Erro Material
	public static final int ANALISE_ERRO_MATERIAL = 15;
	// jvosantos - 02/03/2020 16:27 - Adicionar o tipo de voto para armazenar se o julgamento foi reiniciado
	public static final int JULGAMENTO_REINICIADO = 16;
	
	public static final int[] VOTOS_DE_IMPEDIMENTO = {IMPEDIDO, SUSPEICAO};
	
	public static int[] getListaVotoTipoReativar() {
		int[] listaTipoReativar = Arrays.copyOf(VOTOS_DE_IMPEDIMENTO, VOTOS_DE_IMPEDIMENTO.length + 1);
		listaTipoReativar[listaTipoReativar.length - 1] = OBSERVACAO;

		return listaTipoReativar;
	}
	
	//jsantonelli - 16/12/2019 16:52 - Colocado prazo expirado como um tipo de voto que acompanha o relator
	//lrcampos - 29/10/2019 16:52 - Tirado prazo expirado como um tipo de voto que acompanha o relator
	public static boolean acompanha(int tipo) {
		return tipo == ACOMPANHA_RELATOR || tipo == ACOMPANHA_RELATOR_RESSALVA || tipo == PRAZO_EXPIRADO;
	}
	
	public static boolean diverge(int tipo) {
		return tipo == DIVERGE || tipo == ACOMPANHA_DIVERGENCIA;
	}
	
	public static int valorVoto(int tipo) {
		if(acompanha(tipo)) return 1;
		else if(diverge(tipo)) return -1;
		else return 0;
	}

	@Override
	public void setId(String id) {}

	@Override
	public String getId() {
		return null;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
