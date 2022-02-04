package br.gov.go.tj.utils;

import java.text.DateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Esta classe cont�m m�todos para manipula��o, constru��o visualiza��o, ou
 * representa��o de datas para os eventos que podem ocorrer no PROJUDI
 * 
 * @author Leandro de Lima Lira
 * @author Andr� Luis Cavalcanti Moreira
 * 
 */
public class DiaHoraEventos {

    private GregorianCalendar calenGreg;

    /*
    private int dia;
    private int mes;
    private int ano;
    private int hora;
    private int minuto;
    private int segundo; **/

    /**
     * Cria um DiaHoraEventos com a hora atual
     * 
     */
    public DiaHoraEventos() {
        this.calenGreg = new GregorianCalendar();
    }

    /**
     * Cria um DiaHoraEventos com a hora fornecida
     * 
     * @param date
     *            a hora relativa
     */
    public DiaHoraEventos(Date date) {
        this.calenGreg = new GregorianCalendar();
        this.setData(date);
    }

    /**
     * Altera a hora deste DiaHoraEventos
     * 
     * @param data
     *            a hora relativa
     */
    public void setData(Date data) {
        this.calenGreg.setTime(data);

    }

    /**
     * Pega o n�mero de milissegundos decorridos desde 1� de janeiro de 1970 �s
     * 00:00:00 at� a hora que este DiaHoraEventos representa
     * 
     * @return o valor em milissegundos
     */
    public long getMilis() {
        return this.calenGreg.getTimeInMillis();
    }

    /**
     * Converte uma data para uma representa��o em String no formato "1� de
     * janeiro de 1970 �s 00:00"
     * 
     * @param date
     *            a data a ser convertida
     * @return uma String com a representa��o
     */
    public static String converteFormatoMinutos(Date date) {
        DiaHoraEventos diaTemp = new DiaHoraEventos();
        diaTemp.setData(date);
        return diaTemp.diaHoraMinutos();
    }

    /**
     * Converte uma data para uma representa��o em String no formato "1� de
     * janeiro de 1970 �s 00:00:00"
     * 
     * @param date
     *            a data a ser convertida
     * @return uma String com a representa��o
     */
    public static String converteFormatoSegundos(Date date) {
        DiaHoraEventos diaTemp = new DiaHoraEventos();
        diaTemp.setData(date);
        return diaTemp.diaHoraSegundos();
    }

    /**
     * Converte uma data para uma representa��o em String no formato "1� de
     * janeiro de 1970"
     * 
     * @param date
     *            a data a ser convertida
     * @return uma String com a representa��o
     */
    public static String converteFormatoDataNacional(Date date) {
        DiaHoraEventos diaTemp = new DiaHoraEventos();
        diaTemp.setData(date);
        return diaTemp.dataNacional();
    }

    /**
     * Converte uma data para uma representa��o em String no formato
     * "01/01/1970"
     * 
     * @param date
     *            a data a ser convertida
     * @return uma String com a representa��o
     */
    public static String converteFormatoDataNacionalCurto(Date date) {
        DiaHoraEventos diaTemp = new DiaHoraEventos();
        diaTemp.setData(date);
        return diaTemp.dataNacionalCurto();
    }

    /**
     * Converte a data deste DiaHoraEventos para uma representa��o em String no
     * formato "00:00"
     * 
     * @return uma String com a representa��o
     */
    public static String converteHorario(Date date) {
        return DateFormat.getTimeInstance(DateFormat.SHORT, new Locale("pt", "BR")).format(date);
    }

    /**
     * Converte a data deste DiaHoraEventos para uma representa��o em String no
     * formato "01/01/1970"
     * 
     * @return uma String com a representa��o
     */
    public String dataNacionalCurto() {
        return DateFormat
        // .getDateInstance(DateFormat.SHORT, new Locale("pt", "BR"))
                .getDateInstance(DateFormat.MEDIUM, new Locale("pt", "BR")).format(this.getDate());
    }

    /**
     * Converte a data deste DiaHoraEventos para uma representa��o em String no
     * formato "1� de janeiro de 1970"
     * 
     * @return uma String com a representa��o
     */
    public String dataNacional() {
        return DateFormat.getDateInstance(DateFormat.LONG, new Locale("pt", "BR")).format(this.getDate());
    }

    /**
     * Converte a data deste DiaHoraEventos para uma representa��o em String no
     * formato "1� de janeiro de 1970 �s 00:00:00"
     * 
     * @return uma String com a representa��o
     */
    public String diaHoraSegundos() {
        return DateFormat.getDateInstance(DateFormat.LONG, new Locale("pt", "BR")).format(this.getDate()) + " �s " + DateFormat.getTimeInstance(DateFormat.MEDIUM, new Locale("pt", "BR")).format(this.getDate());
    }

    /**
     * Converte a data deste DiaHoraEventos para uma representa��o em String no
     * formato "1� de janeiro de 1970 �s 00:00"
     * 
     * @return uma String com a representa��o
     */
    public String diaHoraMinutos() {
        return DateFormat.getDateInstance(DateFormat.LONG, new Locale("pt", "BR")).format(this.getDate()) + " �s " + DateFormat.getTimeInstance(DateFormat.SHORT, new Locale("pt", "BR")).format(this.getDate());
    }

    /**
     * Converte a data deste DiaHoraEventos para uma representa��o em String no
     * formato "01/01/1970 �s 00:00"
     * 
     * @return uma String com a representa��o
     */
    public String diaHorario() {
        return DateFormat.getDateInstance(DateFormat.SHORT, new Locale("pt", "BR")).format(this.getDate()) + " �s " + DateFormat.getTimeInstance(DateFormat.SHORT, new Locale("pt", "BR")).format(this.getDate());
    }

    /**
     * Pega o ano deste DiaHoraEventos
     * 
     * @return o ano
     */
    public int getAno() {
        return this.calenGreg.get(GregorianCalendar.YEAR);
    }

    /**
     * Atualiza o ano deste DiaHoraEventos
     * 
     * @param novoAno
     */
    public void setAno(int novoAno) {
        this.calenGreg.set(GregorianCalendar.YEAR, novoAno);
    }

    /**
     * Atualiza o m�s deste DiaHoraEventos
     * 
     * @param novoMes
     */
    public void setMes(int novoMes) {
        this.calenGreg.set(GregorianCalendar.MONTH, --novoMes);
    }

    /**
     * Pega o m�s deste DiaHoraEventos
     * 
     * @return o m�s
     */
    public int getMes() {
        return this.calenGreg.get(GregorianCalendar.MONTH) + 1;
    }

    /**
     * Pega o dia deste DiaHoraEventos
     * 
     * @return o dia
     */
    public int getDia() {
        return this.calenGreg.get(GregorianCalendar.DAY_OF_MONTH);
    }

    /**
     * Atualiza o dia deste DiaHoraEventos
     * 
     * @param novoDia
     */
    public void setDia(int novoDia) {
        this.calenGreg.set(GregorianCalendar.DAY_OF_MONTH, novoDia);
    }

    /**
     * Pega a hora deste DiaHoraEventos
     * 
     * @return a hora
     */
    public int getHora() {
        return this.calenGreg.get(GregorianCalendar.HOUR_OF_DAY);
    }

    /**
     * Atualiza a hora deste DiaHoraEventos
     * 
     * @param novaHora
     */
    public void setHora(int novaHora) {
        this.calenGreg.set(GregorianCalendar.HOUR_OF_DAY, novaHora);
    }

    /**
     * Pega os minutos deste DiaHoraEventos
     * 
     * @return os minutos
     */
    public int getMinutos() {
        return this.calenGreg.get(GregorianCalendar.MINUTE);
    }

    /**
     * Atualiza os minutos deste DiaHoraEventos
     * 
     * @param novoMinuto
     */
    public void setMinuto(int novoMinuto) {
        this.calenGreg.set(GregorianCalendar.MINUTE, novoMinuto);
    }

    /**
     * Pega os segundos deste DiaHoraEventos
     * 
     * @return os segundos
     */
    public int getSegundo() {
        return this.calenGreg.get(GregorianCalendar.SECOND);
    }

    /**
     * Atualiza os segundos deste DiaHoraEventos
     * 
     * @param novoSegundo
     */
    public void setSegundo(int novoSegundo) {
        this.calenGreg.set(GregorianCalendar.SECOND, novoSegundo);
    }

    /**
     * Atualiza os misissegundos deste DiaHoraEventos
     * 
     * @param minissegundos
     */
    public void setMiliSegundos(int minissegundos) {
        this.calenGreg.set(GregorianCalendar.MILLISECOND, minissegundos);
    }

    /**
     * Pega o calend�rio associado a este DiaHoraEventos
     * 
     * @return um Calend�rio Gregoriano
     */
    public GregorianCalendar getCalenGreg() {
        return calenGreg;
    }

    /**
     * Pega a data associada a este DiaHoraEventos
     * 
     * @return
     */
    public Date getDate() {
        return new Date(this.getMilis());
    }

    /**
     * Adiciona minutos � data associada a este DiaHoraEventos
     * 
     * @param minutosAAcrescentar
     *            a quantidade de minutos a ser acrescentada
     */
    public void addMinutos(int minutosAAcrescentar) {
        this.calenGreg.add(GregorianCalendar.MINUTE, minutosAAcrescentar);
    }

    /**
     * Adiciona dias � data associada a este DiaHoraEventos
     * 
     * @param diasAAcrescentar
     *            a quantidade de dias a ser acrescentada
     */
    public void addDias(int diasAAcrescentar) {
        this.calenGreg.add(GregorianCalendar.DATE, diasAAcrescentar);
    }

    /**
     * Verifica se uma data � deste m�s
     * 
     * @param data
     *            a data a ser verificada
     * @return true se a data � deste m�s
     */
    public static boolean ehDoMesAtual(Date data) {
        DiaHoraEventos hoje = new DiaHoraEventos(new Date());
        DiaHoraEventos teste = new DiaHoraEventos(data);
        return (hoje.getMes() == teste.getMes()) && (hoje.getAno() == teste.getAno());
    }

    /**
     * Retornar se a data passada como par�metro � do mesmo dia (ano, mes e dia)
     * 
     * @param data
     *            � a data que se quer comparar
     * @return true se for do mesmo dia, false caso contr�rio
     */
    public boolean ehDoMesmoDia(DiaHoraEventos data) {
        return ((this.getDia() == data.getDia()) && (this.getMes() == data.getMes()) && (this.getAno() == data.getAno()));
    }

    /**
     * Verifica se este DiaHoraEventos representa uma data posterior � passada
     * como par�metro
     * 
     * @param termoFinal
     *            a data para compara��o
     * @return true caso seja posterior
     */
    public boolean ehMaior(DiaHoraEventos termoFinal) {
        GregorianCalendar este = (GregorianCalendar) this.getCalenGreg().clone();
        GregorianCalendar tfinal = (GregorianCalendar) termoFinal.getCalenGreg().clone();
        este.set(GregorianCalendar.HOUR_OF_DAY, 0);
        este.set(GregorianCalendar.MINUTE, 0);
        este.set(GregorianCalendar.SECOND, 0);
        este.set(GregorianCalendar.MILLISECOND, 0);
        tfinal.set(GregorianCalendar.HOUR_OF_DAY, 0);
        tfinal.set(GregorianCalendar.MINUTE, 0);
        tfinal.set(GregorianCalendar.SECOND, 0);
        tfinal.set(GregorianCalendar.MILLISECOND, 0);
        return (este.getTimeInMillis() > tfinal.getTimeInMillis());
    }

    /**
     * Pega um DiaHoraEventos representando uma data que corresponde ao primeiro
     * hor�rio de hoje. Por exemplo, o primeiro hor�rio da data 01/01/1970 �s
     * 15:30:01 � 01/01/1970 �s 00:00:00
     * 
     * @return um DiaHoraEventos com a representa��o
     */
    public static DiaHoraEventos getDataInicioDiaHoje() {
        DiaHoraEventos data = new DiaHoraEventos();
        data.setHora(0);
        data.setMinuto(0);
        data.setSegundo(0);
        data.setMiliSegundos(0);
        return data;
    }

    /**
     * Pega um DiaHoraEventos representando uma data que corresponde ao �ltimo
     * hor�rio de hoje. Por exemplo, o primeiro hor�rio da data 01/01/1970 �s
     * 15:30:01 � 01/01/1970 �s 23:59:59,999
     * 
     * @return um DiaHoraEventos com a representa��o
     */
    public static DiaHoraEventos getDataFimDiaHoje() {
        DiaHoraEventos data = new DiaHoraEventos();
        data.setHora(23);
        data.setMinuto(59);
        data.setSegundo(59);
        data.setMiliSegundos(999);
        return data;
    }

    /**
     * Pega um DiaHoraEventos representando uma data que corresponde ao in�cio
     * desta semana. Primeiro dia da semana e primeiro hor�rio desse dia
     * 
     * @see #getDataInicioDiaHoje()
     * @return um DiaHoraEventos com a representa��o
     */
    public static DiaHoraEventos getDataInicioSemana() {
        DiaHoraEventos data = new DiaHoraEventos();
        // Vamos diminuir a data at� encontrar um domindo
        while ((data.getCalenGreg().get(GregorianCalendar.DAY_OF_WEEK)) != GregorianCalendar.SUNDAY) {
            data.getCalenGreg().add(GregorianCalendar.DAY_OF_MONTH, -1);
        }
        data.setHora(0);
        data.setMinuto(0);
        data.setSegundo(0);
        data.setMiliSegundos(0);
        return data;
    }

    /**
     * Pega um DiaHoraEventos representando uma data que corresponde ao fim
     * desta semana. �ltimo dia da semana e �ltimo hor�rio desse dia
     * 
     * @see #getDataFimDiaHoje()
     * @return um DiaHoraEventos com a representa��o
     */
    public static DiaHoraEventos getDataFimSemana() {
        DiaHoraEventos data = new DiaHoraEventos();
        // Vamos diminuir a data at� encontrar um domindo
        while ((data.getCalenGreg().get(GregorianCalendar.DAY_OF_WEEK)) != GregorianCalendar.SATURDAY) {
            data.getCalenGreg().add(GregorianCalendar.DAY_OF_MONTH, +1);
        }
        data.setHora(23);
        data.setMinuto(59);
        data.setSegundo(59);
        data.setMiliSegundos(999);
        return data;
    }

    /**
     * Pega um DiaHoraEventos representando uma data que corresponde ao in�cio
     * deste m�s. Primeiro dia do m�s e primeiro hor�rio desse dia
     * 
     * @see #getDataInicioDiaHoje()
     * @return um DiaHoraEventos com a representa��o
     */
    public static DiaHoraEventos getDataInicioMes() {
        GregorianCalendar c = new GregorianCalendar();
        c.set(GregorianCalendar.DAY_OF_MONTH, 1);

        DiaHoraEventos data = new DiaHoraEventos(c.getTime());

        data.setHora(0);
        data.setMinuto(0);
        data.setSegundo(0);
        data.setMiliSegundos(0);
        return data;
    }

    /**
     * Pega um DiaHoraEventos representando uma data que corresponde ao fim
     * deste m�s. �ltimo dia do m�s e �ltimo hor�rio desse dia
     * 
     * @see #getDataFimDiaHoje()
     * @return um DiaHoraEventos com a representa��o
     */
    public static DiaHoraEventos getDataFimMes() {
        GregorianCalendar c = new GregorianCalendar();
        c.add(GregorianCalendar.MONTH, 1);
        c.set(GregorianCalendar.DAY_OF_MONTH, 1);
        c.add(GregorianCalendar.DATE, -1);

        DiaHoraEventos data = new DiaHoraEventos(c.getTime());

        data.setHora(23);
        data.setMinuto(59);
        data.setSegundo(59);
        data.setMiliSegundos(999);
        return data;
    }

    /**
     * Pega um DiaHoraEventos representando uma data que corresponde ao in�cio
     * deste ano. Primeiro dia do ano e primeiro hor�rio desse dia
     * 
     * @see #getDataInicioDiaHoje()
     * @return um DiaHoraEventos com a representa��o
     */
    public static DiaHoraEventos getDataInicioAno() {
        DiaHoraEventos data = new DiaHoraEventos();
        data.setDia(1);
        data.setMes(1);
        data.setHora(0);
        data.setMinuto(0);
        data.setSegundo(0);
        data.setMiliSegundos(0);
        return data;
    }

    /**
     * Pega um DiaHoraEventos representando uma data que corresponde ao fim
     * deste ano. �ltimo dia do ano e �ltimo hor�rio desse dia
     * 
     * @see #getDataFimDiaHoje()
     * @return um DiaHoraEventos com a representa��o
     */
    public static DiaHoraEventos getDataFimAno() {
        DiaHoraEventos data = new DiaHoraEventos();
        data.setDia(31);
        data.setMes(12);
        data.setHora(23);
        data.setMinuto(59);
        data.setSegundo(59);
        data.setMiliSegundos(999);
        return data;
    }

    /**
     * Pega um DiaHoraEventos com uma representa��o de uma data que � o in�cio
     * do dia posterior ao dia que representa este DiaHoraEventos
     * 
     * @return um DiaHoraEventos com a representa��o
     */
    public DiaHoraEventos getDataZeroHoraPosterior() {
        DiaHoraEventos zeroHoraPosterior = new DiaHoraEventos(this.getDate());
        zeroHoraPosterior.addDias(1);
        zeroHoraPosterior.setHora(0);
        zeroHoraPosterior.setMinuto(0);
        zeroHoraPosterior.setSegundo(0);
        zeroHoraPosterior.setMiliSegundos(0);
        return zeroHoraPosterior;
    }

    /**
     * M�todo que adiciona um zero � esquerda caso o valor s� possua 1 d�gito
     * 
     * @param valor
     *            valor a ser analisado
     * @return Valor com um zero � esquerda, se necess�rio
     */
    public static String adicionaZeroEsquerdaNecessario(int valorInteiro) {
        String valor = Integer.toString(valorInteiro);
        if (valor.length() == 1)
            valor = "0" + valor;
        return valor;
    }
    
    /**
     * Retorna a data no formato String que o sistema Projudi trabalha dd/MM/yyyy HH:mm:ss
     * @return
     */
    public String getDataFormatoDiaMesAnoHoraMinuntoSegundo(){
    	return Funcoes.getDataDiaMesAnoHoraMinuntoSegundo(this.calenGreg);	
    }

    /**
     * M�todo que auxilia na exibi��o, numa �nica linha, dos hor�rios de um
     * objeto que implemente a interface PautaHorariosIF.
     * 
     * @param pautaIF
     *            Objeto cujos configura��o hor�rios se deseja visualizar numa
     *            �nica linha
     * @return COnfigura��o de um objeto do tipo PautaHorariosIF, numa �nica
     *         linha
     * @throws TorqueException
     */
    /*
     * public static String
     * formataExibicaoFaixaHorariosAudiencia(PautaHorariosIF pautaIF ) throws
     * TorqueException {return "<b>Hora de Inicio: </b> "+
     * DiaHoraEventos.adicionaZeroEsquerdaNecessario(pautaIF.getHoraInicio())+":"+DiaHoraEventos.adicionaZeroEsquerdaNecessario(pautaIF.getMinutoInicial()) + "<b>
     * Hora Final: </b> " +
     * DiaHoraEventos.adicionaZeroEsquerdaNecessario(pautaIF.getHoraFinal())+":"+DiaHoraEventos.adicionaZeroEsquerdaNecessario(pautaIF.getMinutoFinal()) + "<b>
     * Hor�rio Refei��o: </b> " +
     * DiaHoraEventos.adicionaZeroEsquerdaNecessario(pautaIF.getHoraInicioRefeicao())+":"+DiaHoraEventos.adicionaZeroEsquerdaNecessario(pautaIF.getMinutoInicialRefeicao()) +
     * "-" +
     * DiaHoraEventos.adicionaZeroEsquerdaNecessario(pautaIF.getHoraFinalRefeicao())+":"+DiaHoraEventos.adicionaZeroEsquerdaNecessario(pautaIF.getMinutoFinalRefeicao()) + "
     * <b> Minutos Intervalo: </b>" +pautaIF.getMinutosIntervalo(); }
     * 
     */

    /**
     * M�todo que modifica o objeto para uma data onde se possa marcar a leitura
     * de uma intima��o ou cita��o Isto envolve checar o hor�rio do fato, bem
     * como n�o se tratar de domingo, feriado, dia n�o �til *
     * 
     * @param codVara
     *            Caso n�o seja informado, n�o ser� levado em considera��o. Caso
     *            seja informado, retornar� apenas as datas vinculadas � vara e
     *            a nenhuma turma e as datas n�o vinculadas a nenhuma vara nem
     *            nenhuma turma
     * @param codTurma
     *            Caso n�o seja informado, n�o ser� levado em considera��o. Caso
     *            seja informado, retornar� apenas as datas vinculadas � turma e
     *            a nenhuma vara e as datas n�o vinculadas a nenhuma vara nem
     *            nenhuma turma
     * @param numeroProcesso
     *            � o n�mero de um processo espec�fico
     * 
     */
    /*
     * public void configuraProximoDiaParaCitacaoOuIntimacao(Integer codVara,
     * Integer codTurma, Long numeroProcesso) throws TorqueException {
     * 
     * 
     * //se o hor�rio ultrapassa o legal, vamos logo coloc�-lo para o p�roximo
     * dia �s 8 da manh� if(this.getHora() >
     * CalculadorDePrazos.HORA_LEGAL_LIMITE_PARA_ATOS_EXTERNOS){
     * this.addDias(1); this.setHora(8);//ficar� para as 8 da manh� do dia
     * seguinte this.setMinuto(0); }
     * 
     * //agora vamos incrementar at� encontrar um dia v�lido int incrementada =
     * 1;
     * while(CalculadorDePrazos.ehFeriadoDomingoDiaNaoUtilOuAposHorarioLegal(this,
     * codVara, codTurma,numeroProcesso)){this.addDias(1);
     *  }
     *  }
     */
}
