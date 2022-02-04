package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ProcessoCertidaoPraticaForenseDt extends Dados {
	
	/**
     * 
     */
    private static final long serialVersionUID = 3633792902372277022L;
    private String dataRecebimento;
	private String inicioAtuacao;
	private String processoNumero;
	private String digitoVerificador;
	private String forumCodigo;
	private String ano;
	private String processoTipo;
	private List promovente;
	private List promovido;
	private List promoventeAdvogado;
	private List promovidoAdvogado;
	private String  serventia;
	private boolean segredoJustica;
	private boolean processoSPG;

	/**
	 * 
	 */
	public ProcessoCertidaoPraticaForenseDt() {
		super();
		dataRecebimento= "";
		inicioAtuacao= "";
		processoNumero= "";
		digitoVerificador= "";
		forumCodigo= "";
		ano= "";
		processoTipo= "";
		promovente = new ArrayList(10);
		promovido = new ArrayList(10);
		promoventeAdvogado = new ArrayList(10);
		promovidoAdvogado = new ArrayList(10);
		segredoJustica = false;
		processoSPG = false;

	}

	/**
	 * Retorna o número completo de um processo, obedecendo a padronização do CNJ
	 */
	public String getProcessoNumeroCompleto() {
		if (isProcessoSPG()) return getProcessoNumero();
		return (Funcoes.completarZeros(getProcessoNumero(), 7) + "." + Funcoes.completarZeros(getDigitoVerificador(), 2) + "." + getAno() + "." + Configuracao.JTR + "." + Funcoes.completarZeros(getForumCodigo(), 4));
	}

	public String getDigitoVerificador() {
		return digitoVerificador;
	}
	
	public String getForumCodigo() {
		return forumCodigo;
	}

	public void setForumCodigo(String forumCodigo) {
		this.forumCodigo = forumCodigo;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public void setDigitoVerificador(String digitoVerificador) {
		this.digitoVerificador = digitoVerificador;
	}

	
	public String getProcessoNumero() {
		return processoNumero;
	}

	public void setProcessoNumero(String processoNumero) {
		this.processoNumero = processoNumero;
	}

	public List getPromoventeAdvogado() {
		return promoventeAdvogado;
	}

	public void setDataRecebimento(String dataRecebimento) {
		this.dataRecebimento = dataRecebimento;
	}

	public void setInicioAtuacao(String inicioAtuacao) {
		this.inicioAtuacao = inicioAtuacao;
	}

	public void setProcessoTipo(String processoTipo) {
		this.processoTipo = processoTipo;
	}

	public void setPromovente(List promovente) {
		this.promovente = promovente;
	}

	public void setPromovido(List promovido) {
		this.promovido = promovido;
	}

	public void setPromovidoAdvogado(List promovidoAdvogado) {
		this.promovidoAdvogado = promovidoAdvogado;
	}

	public void setServentia(String serventia) {
		this.serventia = serventia;
	}

	public String getServentia() {
		return serventia;
	}

	public String getProcessoTipo() {
		return processoTipo;
	}

	public String getInicioAtuacao() {
		return inicioAtuacao;
	}
	
	public String getInicioAtuacaoFormatada() {
		if (processoSPG) {
			if (this.getInicioAtuacao() == null || Funcoes.retiraEspacosInicioFim(this.getInicioAtuacao().replaceAll("[/]", "")).length() == 0) {
				return "";
			}
			return this.getInicioAtuacao();
		} else {
			return Funcoes.FormatarDataHoraMinuto(this.getInicioAtuacao());
		}
	}

	public String getPromovidoProcessoParte() {
		StringBuilder sb = new StringBuilder();
		Iterator it;
		if (isSegredoJustica()) {
			List novaLista = new ArrayList(5);
			Iterator i = promovido.iterator();
			while (i.hasNext()) {
				String nome = (String) i.next();
				if(nome != null)
				novaLista.add(Funcoes.iniciaisNome(nome));
			}
			it = novaLista.iterator();
		} else {
			it = promovido.iterator();
		}
		while (it.hasNext()) {
			String ParteContraria = (String) it.next();
			sb.append(ParteContraria + ", ");
		}
		if (promovido.size() >= 1) {
		int tamanho = sb.length();
		sb.delete(tamanho - 2, tamanho - 1);
		}
		return sb.toString();
	}

	public String getDataRecebimento() {
		return dataRecebimento;
	}
	
	public String getDataRecebimentoFormatada() {
		if (processoSPG) {
			if (this.getDataRecebimento() == null || Funcoes.retiraEspacosInicioFim(this.getDataRecebimento().replaceAll("[/]", "")).length() == 0) {
				return "";
			}
			return this.getDataRecebimento();
		} else {
			return Funcoes.FormatarDataHoraMinuto(this.getDataRecebimento());
		}
	}

	public String getPromoventeProcessoParte() {
		StringBuilder sb = new StringBuilder();
		Iterator it;
		if (isSegredoJustica()) {
			List novaLista = new ArrayList(5);
			Iterator i = promovente.iterator();
			while (i.hasNext()) {
				String nome = (String) i.next();
				if(nome != null)
				novaLista.add(Funcoes.iniciaisNome(nome));
			}
			it = novaLista.iterator();
		} else {
			it = promovente.iterator();
		}
		while (it.hasNext()) {
			String ParteContraria = (String) it.next();
			sb.append(ParteContraria + ", ");
		}
		if (promovente.size() >= 1) {
		int tamanho = sb.length();
		sb.delete(tamanho - 2, tamanho - 1);
		}
		return sb.toString();
	}

	public String getPromoventeAdvogadoProcessoParte() {
		if(promoventeAdvogado.size() == 0)
			return null;
		StringBuilder sb = new StringBuilder();
		Iterator it = promoventeAdvogado.iterator();
		while (it.hasNext()) {
			String advgd = (String) it.next();
			sb.append(advgd + ", ");
		}
		int tamanho = sb.length();
		sb.delete(tamanho - 2, tamanho - 1);
		return sb.toString();
	}

	public String getPromovidoAdvogadoProcessoParte() {
		if(promovidoAdvogado.size() == 0)
			return null;
		StringBuilder sb = new StringBuilder();
		Iterator it = promovidoAdvogado.iterator();
		while (it.hasNext()) {
			String advgd = (String) it.next();
			sb.append(advgd + ", ");
		}
		int tamanho = sb.length();
		sb.delete(tamanho - 2, tamanho - 1);
		return sb.toString();
	}

	@Override
	public String getId() {
		// TODO jpcpresa: Auto-generated method stub
		return null;
	}

	@Override
	public void setId(String id) {
		// TODO jpcpresa: Auto-generated method stub

	}

	public void addPromovente(String string) {
		this.promovente.add(string);		
	}
	
	public void addPromovido(String string) {
		this.promovido.add(string);
	}
	
	public void addPromoventeAdvogado(String advogado) {
		this.promoventeAdvogado.add(advogado);
	}
	
	public void addPromovidoAdvogado(String advogado) {
		this.promovidoAdvogado.add(advogado);
	}
	
	public boolean isSegredoJustica() {
		return segredoJustica;
	}

	public void setSegredoJustica(boolean segredoJustica) {
		this.segredoJustica = segredoJustica;
	}

	public boolean isProcessoSPG() {
		return processoSPG;
	}

	public void setProcessoSPG(boolean processoSPG) {
		this.processoSPG = processoSPG;
	}
}
