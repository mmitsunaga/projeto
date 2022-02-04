package br.gov.go.tj.projudi.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.gov.go.tj.projudi.dt.AudienciaAgendaDt;
import br.gov.go.tj.utils.Funcoes;

/**
 * Classe respons�vel por executar as valida��es dos campos informados pelo usu�rio para a gera��o de agendas para
 * audi�ncias
 * 
 * @author Keila Sousa Silva
 * @author Tribunal de Justi�a de Goi�s
 */
public class AudienciaAgendaValidacao {
    private Date dataInicial = new Date();

    private Date horarioInicial = new Date();

    private boolean validacao = false;

    /**
     * M�todo respons�vel por validar se a data foi informada
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
     * M�todo respons�vel por validar se a data � igual ou superior � data corrente
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
     * M�todo respons�vel por validar se as datas de um per�odo informado s�o consistentes, ou seja, a data final �
     * igual ou superior � data inicial
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
			mensagemRetorno = "Data Inicial n�o pode ser maior que Data Final. ";
		} else if (diferencaDias[0] > 90) {
			mensagemRetorno = "Per�odo entre Data Inicial e Data Final n�o pode superar 90(noventa) dias. ";
		}
		
		return mensagemRetorno;
	}

    /**
     * M�todo respons�vel por validar se o hor�rio foi informado
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
     * M�todo respons�vel por validar o formato do hor�rio. O formato aceito �: hh:MM, ou seja, ele possuir� 4 d�gitos
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
     * M�todo respons�vel por validar se o hor�rio inicial � consistente, ou seja, � igual ou superior ao hor�rio
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

	// Convertendo o hor�rio inicial de String para Date
	this.horarioInicial = Funcoes.stringToTime(horarioInicial);

	// Data corrente
	String dataCorrente = Funcoes.dateToStringSoData(new Date());

	/*
	 * O hor�rio inicial ser� verificado quando a data inicial for corrente e o dia da semana do hor�rio inicial a
	 * ser testando for igual ao dia da semana da data inicial
	 */
	if ((this.dataInicial.equals(Funcoes.StringToDate(dataCorrente)))
		&& (diaSemana.equalsIgnoreCase(String.valueOf(this.dataInicial.getDay())))) {
	    /*
	     * Verificar se o valor do hor�rio inicial � v�lido, ou seja, � igual ou superior ao hor�rio corrente
	     */
	    String horarioCorrente = Funcoes.dateToStringSoHorario(new Date());
	    if (this.horarioInicial.before(Funcoes.stringToTime(horarioCorrente))) {
		validacao = false;
	    }
	}
	return validacao;
    }

    /**
     * M�todo respons�vel por validar se o hor�rio final � consistente, ou seja, � igual ou superior ao somat�rio:
     * hor�rio inicial(em minutos) + duracao(em minutos)
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
	 * Verfificar se hor�rio final possui o valor m�nimo aceit�vel que �: hor�rio inicial(em minutos) + dura��o(em
	 * minutos)
	 */
	int somatorioHorarioFinalDuracao = Funcoes.horaToMinuto(horarioInicial) + Funcoes.StringToInt(duracaoAudiencia);
	if (((Funcoes.horaToMinuto(horarioFinal)) >= somatorioHorarioFinalDuracao)) {
	    validacao = true;
	}
	return validacao;
    }

    /**
     * M�todo respons�vel por verificar se a dura��o da audi�ncia � um valor v�lido, ou seja, � maior do que zero
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