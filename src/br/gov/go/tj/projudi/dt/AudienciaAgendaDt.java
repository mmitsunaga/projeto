package br.gov.go.tj.projudi.dt;

/**
 * Classe que cont�m os campos que ser�o informados pelos usu�rio no cadastro de agendas de audi�ncias [quantidade de
 * audi�ncias simult�neas, datas inicial e final e os dados (hor�rios inicial e final e dura��o das audi�ncias) dos dias
 * da semana compat�veis com o per�odo de datas informado]. Essa classe tamb�m prepara a propriedade "logHorarioDuracao"
 * (conter� os dados da semana informados) para fins de log
 * 
 * @author Keila Sousa Silva
 * 
 */
public class AudienciaAgendaDt extends AudienciaDt {

	/**
     * 
     */
    private static final long serialVersionUID = 2311932323348961105L;

    public static final int CodigoPermissao = 222;
    
    //Limite aumentado para 15, solicitado no BO 2018/15780
    public static final int QUANTIDADE_MAXIMA_AUDIENCIAS_SIMULTANEAS_PADRAO = 15;

	private String quantidadeAudienciasSimultaneas;

	private String dataInicial;

	private String dataFinal;

	private String[] horariosDuracao;

	private String logHorariosDuracao;
	
	private String quantidadeMaximaAudienciasSimultaneas;

	public AudienciaAgendaDt() {
		limpar();
	}

	public void limpar() {
		super.limpar();
		quantidadeAudienciasSimultaneas = "";
		dataInicial = "";
		dataFinal = "";
		logHorariosDuracao = "";
		horariosDuracao = null;
		quantidadeAudienciasSimultaneas = "1";
	}

	public String getQuantidadeAudienciasSimultaneas() {
		return quantidadeAudienciasSimultaneas;
	}

	public void setQuantidadeAudienciasSimultaneas(String quantidadeAudienciasSimultaneas) {
		if (quantidadeAudienciasSimultaneas != null) {
			this.quantidadeAudienciasSimultaneas = quantidadeAudienciasSimultaneas;
		}
	}

	public String getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(String dataInicial) {
		if (dataInicial != null) {
			this.dataInicial = dataInicial;
		}
	}

	public String getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(String dataFinal) {
		if (dataFinal != null) {
			this.dataFinal = dataFinal;
		}
	}

	public String[] getHorariosDuracao() {
		return horariosDuracao;
	}

	public void setHorariosDuracao(String[] horariosDuracao) {
		if (horariosDuracao != null) {
			this.horariosDuracao = horariosDuracao;
		}
	}

	public String getLogHorariosDuracao() {
		return logHorariosDuracao;
	}

	public void setLogHorariosDuracao(String logHorariosDuracao) {
		this.logHorariosDuracao = logHorariosDuracao;
	}

	/**
	 * M�todo respons�vel por preparar e retornar os dados necess�rios para log
	 * 
	 * @author Keila Sousa Silva
	 * @return String
	 */
	public String getPropriedades() {
		return "[Id_UsuarioLog: " + getId_UsuarioLog() + " IpComputadorLog" + getIpComputadorLog() + ". VALORES PADR�O --> " + " Id_AudienciaTipo: " + getId_AudienciaTipo() + "; AudienciaTipo: " + getAudienciaTipo() + "; DataMovimentacao: " + getDataMovimentacao() + "; CodigoTemp: " + getCodigoTemp() + ". PER�ODO -->" + " Quantidade: " + getQuantidadeAudienciasSimultaneas() + "; Data Inicial: " + getDataInicial() + "; Data Final: " + getDataFinal() + ". HOR�RIOS/DURA��O --> "
				+ getLogHorariosDuracao() + "]";
	}

	public String getQuantidadeMaximaAudienciasSimultaneas() {
		return quantidadeMaximaAudienciasSimultaneas;
	}

	public void setQuantidadeMaximaAudienciasSimultaneas(String quantidadeMaximaAudienciasSimultaneas) {
		this.quantidadeMaximaAudienciasSimultaneas = quantidadeMaximaAudienciasSimultaneas;
	}
}