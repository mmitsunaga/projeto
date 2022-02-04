package br.gov.go.tj.projudi.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.gov.go.tj.projudi.dt.AudienciaAgendaDt;
import br.gov.go.tj.utils.Funcoes;

/**
 * Classe responsável por executar as validações dos campos informados pelo usuário para a geração de agendas para
 * audiências
 * 
 * @author Keila Sousa Silva
 * @author Tribunal de Justiça de Goiás
 */
public class AudienciaAgendaValidacao {
    private Date dataInicial = new Date();

    private Date horarioInicial = new Date();

    private boolean validacao = false;

    /**
     * Método responsável por validar se a data foi informada
     * 
     * @author Keila Sousa Silva
     * @param data
     * @return validacao
     */
    public boolean validarDataObrigatoria(String data) {
	validacao = false;
	if (data.length() > 0) {
	    validacao = true;
	}
	return validacao;
    }

    /**
     * Método responsável por validar se a data é igual ou superior à data corrente
     * 
     * @author Keila Sousa Silva
     * @param data
     * @return validacao
     * @throws Exception
     */
    public boolean validarDataIgualSuperiorCorrente(String data) {
		validacao = false;
		try{
			Date dataAtual = Funcoes.StringToDate(Funcoes.dateToStringSoData(new Date()));
			Date dataTeste = Funcoes.StringToDate(data);
			if (dataAtual.getTime()<= dataTeste.getTime())			
				validacao = true;			
		}catch(Exception e){
			return false;
		}
		return validacao;
    }

    /**
     * Método responsável por validar se as datas de um período informado são consistentes, ou seja, a data final é
     * igual ou superior à data inicial
     * 
     * @author hmgodinho
     * @param audienciaAgendaDt
     * @return Mensagem de Retorno
     * @throws ParseException 
     * @throws Exception
     */
    public String validarDatasPeriodo(AudienciaAgendaDt audienciaAgendaDt) throws ParseException{
		String mensagemRetorno = "";
    	SimpleDateFormat FormatoData = new SimpleDateFormat("dd/MM/yyyy");
		Date dataInicial = null;
		Date dataFinal = null;

		dataInicial = FormatoData.parse(audienciaAgendaDt.getDataInicial());
		dataFinal = FormatoData.parse(audienciaAgendaDt.getDataFinal());
		
		long[] diferencaDias = Funcoes.diferencaDatas(dataFinal, dataInicial);
		if (diferencaDias[0] < 0) {
			mensagemRetorno = "Data Inicial não pode ser maior que Data Final. ";
		} else if (diferencaDias[0] > 90) {
			mensagemRetorno = "Período entre Data Inicial e Data Final não pode superar 90(noventa) dias. ";
		}
		
		return mensagemRetorno;
	}

    /**
     * Método responsável por validar se o horário foi informado
     * 
     * @author Keila Sousa Silva
     * @param horario
     * @return validacao
     */
    public boolean validarHorarioObrigatorio(String horario) {
	validacao = false;
	if (horario.length() > 0) {
	    validacao = true;
	}
	return validacao;
    }

    /**
     * Método responsável por validar o formato do horário. O formato aceito é: hh:MM, ou seja, ele possuirá 4 dígitos
     * 
     * @author Keila Sousa Silva
     * @param horario
     * @return validacao
     */
    public boolean validarFormatoHorario(String horario) {
	validacao = false;

	if (horario.length() > 4) {
	    validacao = true;
	}
	return validacao;
    }

    /**
     * Método responsável por validar se o horário inicial é consistente, ou seja, é igual ou superior ao horário
     * corrente
     * 
     * @author Keila Sousa Silva
     * @param diaDaSemana
     * @param dataInicial
     * @param horarioInicial
     * @return validacao
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
	public boolean validarHorarioInicialConsistente(String diaSemana, String dataInicial, String horarioInicial)
	    throws Exception {
	validacao = true;

	// Convertendo a data inicial de String para Date
	this.dataInicial = Funcoes.StringToDate(dataInicial);

	// Convertendo o horário inicial de String para Date
	this.horarioInicial = Funcoes.stringToTime(horarioInicial);

	// Data corrente
	String dataCorrente = Funcoes.dateToStringSoData(new Date());

	/*
	 * O horário inicial será verificado quando a data inicial for corrente e o dia da semana do horário inicial a
	 * ser testando for igual ao dia da semana da data inicial
	 */
	if ((this.dataInicial.equals(Funcoes.StringToDate(dataCorrente)))
		&& (diaSemana.equalsIgnoreCase(String.valueOf(this.dataInicial.getDay())))) {
	    /*
	     * Verificar se o valor do horário inicial é válido, ou seja, é igual ou superior ao horário corrente
	     */
	    String horarioCorrente = Funcoes.dateToStringSoHorario(new Date());
	    if (this.horarioInicial.before(Funcoes.stringToTime(horarioCorrente))) {
		validacao = false;
	    }
	}
	return validacao;
    }

    /**
     * Método responsável por validar se o horário final é consistente, ou seja, é igual ou superior ao somatório:
     * horário inicial(em minutos) + duracao(em minutos)
     * 
     * @author Keila Sousa Silva
     * @param horarioInicial
     * @param horarioFinal
     * @param duracaoAudiencia
     * @return validacao
     * @throws Exception
     */
    public boolean validarHorarioFinalConsistente(String horarioInicial, String horarioFinal, String duracaoAudiencia)
	    throws Exception {
	validacao = false;

	/*
	 * Verfificar se horário final possui o valor mínimo aceitável que é: horário inicial(em minutos) + duração(em
	 * minutos)
	 */
	int somatorioHorarioFinalDuracao = Funcoes.horaToMinuto(horarioInicial) + Funcoes.StringToInt(duracaoAudiencia);
	if (((Funcoes.horaToMinuto(horarioFinal)) >= somatorioHorarioFinalDuracao)) {
	    validacao = true;
	}
	return validacao;
    }

    /**
     * Método responsável por verificar se a duração da audiência é um valor válido, ou seja, é maior do que zero
     * 
     * @author Keila Sousa Silva
     * @param duracaoAudiencia
     * @return validacao
     */
    public boolean validarDuracaoConsistente(String duracaoAudiencia) {
	validacao = false;
	if (Funcoes.StringToInt(duracaoAudiencia) > 0) {
	    validacao = true;
	}
	return validacao;
    }
}