package br.gov.go.tj.projudi.dt;

public class MovimentacaoDt extends MovimentacaoDtGen {

	/**
     * 
     */
    private static final long serialVersionUID = 8202550783646733200L;

    public static final int CodigoPermissao = 269;

	// Variáveis para controlar possíveis status de Movimentacao
	public static final int VALIDA = 0;
	public static final int INVALIDA = 1;

	private boolean temArquivos; //Define se movimentação possui arquivos
	private boolean valida; // Define se movimentação é válida
	private String nomeUsuarioRealizador;
	private ProcessoDt processoDt; //Processo vinculado a movimentação
	
	//variávies que serão utilizadas para montar a lista de movimentações
	//na JPS da capa do processo completo
	private boolean mostrarColunaManterEventos;
	private boolean mostrarColunaGerarPendencias;
	
	
	public MovimentacaoDt() {
		limpar();
	}

	public void limpar() {
		super.limpar();
		temArquivos = false;
		valida = true;
		nomeUsuarioRealizador = "";
	}

	public void copiar(MovimentacaoDt objeto) {
		super.copiar(objeto);
		nomeUsuarioRealizador = objeto.getNomeUsuarioRealizador();
	}

	public ProcessoDt getProcessoDt() {
		return processoDt;
	}

	public void setProcessoDt(ProcessoDt processoDt) {
		this.processoDt = processoDt;
	}

	public boolean temArquivos() {
		return temArquivos;
	}

	public void setTemArquivos(boolean temArquivos) {
		this.temArquivos = temArquivos;
	}

	public boolean isValida() {
		return valida;
	}

	public void setValida(boolean valida) {
		this.valida = valida;
	}

	public String getNomeUsuarioRealizador() {
		return nomeUsuarioRealizador;
	}

	public void setNomeUsuarioRealizador(String nomeUsuario) {
		nomeUsuarioRealizador = nomeUsuario;
	}

	public boolean getMostrarColunaManterEventos() {
		return mostrarColunaManterEventos;
	}

	public void setMostrarColunaManterEventos(boolean mostrarColunaManterEventos) {
		this.mostrarColunaManterEventos = mostrarColunaManterEventos;
	}

	public boolean getMostrarColunaGerarPendencias() {
		return mostrarColunaGerarPendencias;
	}

	public void setMostrarColunaGerarPendencias(boolean mostrarColunaGerarPendencias) {
		this.mostrarColunaGerarPendencias = mostrarColunaGerarPendencias;
	}
	
	/**
	 * Retorna a descrição do tipo da movimentação mais o complemento.
	 * Se o tipo da movimentação do do tipo AUDIENCIA_PUBLICADA, retornar apenas o tipo da movimentação
	 * @return 
	 */
	public String getDescricaoMovimentacaoTipoComplemento(){		
		if (this.getComplemento() != null && this.getComplemento().length() > 0){
			if (this.getMovimentacaoTipoCodigo().equalsIgnoreCase(String.valueOf(MovimentacaoTipoDt.AUDIENCIA_PUBLICADA))){
				return this.getMovimentacaoTipo();
			} else {
				return this.getMovimentacaoTipo() + " - " + this.getComplemento(); 
			}
		} else {
			return this.getMovimentacaoTipo();
		}		
	}
	
	public String getMovimentacaoTipoLimpa()  {
		String stTempo = this.getMovimentacaoTipo();
		if (stTempo == null){
			stTempo = "";
		} else {
			stTempo = stTempo.replaceAll("\"","");
		}
		return stTempo;
	}
	
	public String getManterEvento() {
		String stTemp="";
		if (getDataRealizacao()!=null && getDataRealizacao().length()>=10) {
			stTemp = getDataRealizacao().substring(0,10) + " - " + getMovimentacaoTipoLimpa();
		}
		return stTemp;
	}
}
