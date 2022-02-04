package br.gov.go.tj.projudi.ne;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.PrazoSuspensoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.ps.PrazoSuspensoPs;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;

public class PrazoSuspensoNe extends PrazoSuspensoNeGen {

    /**
     * 
     */
    private static final long serialVersionUID = -3163065495246239402L;

    public String Verificar(PrazoSuspensoDt dados)  throws Exception {

        String stRetorno = "";

        if (dados.getData().equalsIgnoreCase("") && dados.getDataInicial().equalsIgnoreCase("") && dados.getDataFinal().equalsIgnoreCase(""))
            stRetorno += "Informe o Campo Data ou Per�odo Data Inicial e Final.";
        else if (!dados.getData().equalsIgnoreCase("") && (!dados.getDataInicial().equalsIgnoreCase("") || !dados.getDataFinal().equalsIgnoreCase("")))
            stRetorno += "Informe o Campo Data ou Per�odo Data Inicial e Final.";
        else if (dados.getData().equalsIgnoreCase("") && !verificaPeriodo(dados.getDataInicial(), dados.getDataFinal())) stRetorno += "Per�odo informado Inv�lido.";
        return stRetorno;

    }

    /**
     * CALCULA A DIFEREN�A DE DUAS DATAS EM DIAS IMPORTANTE: QUANDO REALIZA A
     * DIFEREN�A EM DIAS ENTRE DUAS DATAS, ESTE M�TODO CONSIDERA AS HORAS
     * RESTANTES E AS CONVERTE EM FRA��O DE DIAS.
     * 
     * @param dataInicial
     * @param dataFinal
     * @return QUANTIDADE DE DIAS EXISTENTES ENTRE a dataInicial e dataFinal.
     */
    public static double diferencaEmDias(Date dataInicial, Date dataFinal) {
        double result = 0;
        long diferenca = dataFinal.getTime() - dataInicial.getTime();
        double diferencaEmDias = (diferenca / 1000) / 60 / 60 / 24;
        long horasRestantes = (diferenca / 1000) / 60 / 60 % 24;
        result = diferencaEmDias + (horasRestantes / 24d);

        return result;
    }

    /**
     * Calcula a diferen�a de duas datas em minutos <br>
     * <b>Importante:</b> Quando realiza a diferen�a em minutos entre duas
     * datas, este m�todo considera os segundos restantes e os converte em
     * fra��o de minutos.
     * 
     * @param dataInicial
     * @param dataFinal
     * @return quantidade de minutos existentes entre a dataInicial e dataFinal.
     */
    public static double diferencaEmMinutos(Date dataInicial, Date dataFinal) {
        double result = 0;
        long diferenca = dataFinal.getTime() - dataInicial.getTime();
        double diferencaEmMinutos = (diferenca / 1000) / 60; // resultado �
        // diferen�a
        // entre as
        // datas em
        // minutos
        long segundosRestantes = (diferenca / 1000) % 60; // calcula os
        // segundos
        // restantes
        result = diferencaEmMinutos + (segundosRestantes / 60d); // transforma
        // os
        // segundos
        // restantes
        // em
        // minutos

        return result;
    }

    public static boolean verificaPeriodo(String dataInicial, String dataFinal) throws ParseException{
        boolean retorno = true;
        
            if (!dataInicial.equalsIgnoreCase("") && !dataFinal.equalsIgnoreCase("")) {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                Date dateInicial = format.parse(dataInicial);
                Date dateFinal;

                dateFinal = format.parse(dataFinal);

                if (dateFinal.before(dateInicial)) {
                    retorno = false;
                }
            } else
                retorno = false;

        return retorno;
    }

    public void salvar(PrazoSuspensoDt dados) throws Exception{

        LogDt obLogDt;
        FabricaConexao obFabricaConexao = null;

        ////System.out.println("..nePrazoSuspensosalvar()");
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            obFabricaConexao.iniciarTransacao();
            PrazoSuspensoPs obPersistencia = new PrazoSuspensoPs(obFabricaConexao.getConexao());

            if (!dados.getData().equalsIgnoreCase("")) {
                /*
                 * use o iu do objeto para saber se os dados ja est�o ou n�o
                 * salvos
                 */
                if (dados.getId().equalsIgnoreCase("")) {
                    obPersistencia.inserir(dados);
                    obLogDt = new LogDt("PrazoSuspenso", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
                } else {
                    obPersistencia.alterar(dados);
                    obLogDt = new LogDt("PrazoSuspenso", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());
                }
                obDados.copiar(dados);
                obLog.salvar(obLogDt, obFabricaConexao);
            } else {
                // PEGAR INTERVALOS DE DATAS
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date dateInicial = dateFormat.parse(dados.getDataInicial());
                Date dateFinal = dateFormat.parse(dados.getDataFinal());
                double diferencaDias = diferencaEmDias(dateInicial, dateFinal);

                Calendar data = Calendar.getInstance();
                Date d = dateFormat.parse(dados.getDataInicial());
                data.setTime(d);
                List datas = new ArrayList();

                for (int i = 0; i <= diferencaDias; i++) {
                    datas.add(data.getTime());
                    data.add(Calendar.DAY_OF_MONTH, 1);
                }

                for (Iterator iterator = datas.iterator(); iterator.hasNext();) {
                    Date dataList = (Date) iterator.next();
                    PrazoSuspensoDt prazoSuspensoDt = new PrazoSuspensoDt();
                    prazoSuspensoDt.setMotivo(dados.getMotivo());
                    prazoSuspensoDt.setId_PrazoSuspensoTipo(dados.getId_PrazoSuspensoTipo());
                    prazoSuspensoDt.setPrazoSuspensoTipo(dados.getPrazoSuspensoTipo());
                    prazoSuspensoDt.setId_Comarca(dados.getId_Comarca());
                    prazoSuspensoDt.setComarca(dados.getComarca());
                    prazoSuspensoDt.setId_Serventia(dados.getId_Serventia());
                    prazoSuspensoDt.setServentia(dados.getServentia());
                    prazoSuspensoDt.setId_Cidade(dados.getId_Cidade());
                    prazoSuspensoDt.setCidade(dados.getCidade());
                    prazoSuspensoDt.setData(Funcoes.dateToStringSoData(dataList));
                    prazoSuspensoDt.setIpComputadorLog(dados.getIpComputadorLog());
                    prazoSuspensoDt.setId_UsuarioLog(dados.getId_UsuarioLog());
                    obLogDt = new LogDt("PrazoSuspenso", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
                    obPersistencia.inserir(prazoSuspensoDt);
                    obLog.salvar(obLogDt, obFabricaConexao);
                }
            }

            obFabricaConexao.finalizarTransacao();

        } catch(Exception e) {
            obFabricaConexao.cancelarTransacao();
            throw e;
        } finally{
            obFabricaConexao.fecharConexao();
        }
    }

    public String getProximaDataValidaNovoCPC(Date datainicial, int prazo, ServentiaDt serventia, FabricaConexao conexao) throws Exception {
        return getProximaDataValidaNovoCPC(Funcoes.dateToStringSoData(datainicial), prazo, serventia, conexao);
    }

    /**
     * Retorna a data v�lida ap�s contagem de prazo passado, e a partir da data
     * passada.
     * 
     * @param datainicial:
     *            data para in�cio de contagem de prazo (data de leitura da
     *            intima��o, por exemplo)
     * @param prazo:
     *            prazo para cumprimento
     * @param id_comarca:
     *            identifica��o da comarca
     * @param id_cidade:
     *            identifica��o da cidade
     * @param id_serventia:
     *            identifica��o da serventia
     * 
     * @author jrcorrea
     */
    public String getProximaDataValidaNovoCPC(String datainicial, int prazo, ServentiaDt serventia, FabricaConexao obFabricaConexao) throws Exception {
        String stRetorno = "";

        // List lisDatas = new ArrayList();

        Calendar calCalendario = Calendar.getInstance();

        // Atribue a data inicial
        calCalendario.setTime(Funcoes.StringToDate(datainicial));

        /*
         * O prazo � contado da seguinte forma: deve-se contar somente dias uteis
         */     

        boolean boTeste = false;
        PrazoSuspensoPs obPersistencia = new PrazoSuspensoPs(obFabricaConexao.getConexao());
        int intAux = 0;

        do{
        	// Proximo dia, a contagem se inicia no dia seguinte
            calCalendario.add(Calendar.DATE, 1);
            // Verifico se n�o � s�bado ou domigo, passando para  segunda-feira
            passouParaSegundaFeira(calCalendario);
            // verifico se esta com prazo suspenso ou feriado
            boTeste = obPersistencia.isDataValida(calCalendario.getTime(), serventia);
                                    
            if (boTeste) {                           
                intAux++;                                
            }                        
                        
        }while(intAux < prazo);

        stRetorno = Funcoes.dateToStringSoData(calCalendario.getTime())+" 23:59:59";

        return stRetorno;

    }
    
    public String getProximaDataValidaPrazoCorrido(Date datainicial, int prazo, ServentiaDt serventia, FabricaConexao conexao) throws Exception {
        return getProximaDataValidaPrazoCorrido(Funcoes.dateToStringSoData(datainicial), prazo, serventia, conexao);
    }
    
    /**
     * Retorna a data v�lida ap�s contagem de prazo passado, e a partir da data
     * passada.
     * 
     * @param datainicial:
     *            data para in�cio de contagem de prazo (data de leitura da
     *            intima��o, por exemplo)
     * @param prazo:
     *            prazo para cumprimento
     * @param id_comarca:
     *            identifica��o da comarca
     * @param id_cidade:
     *            identifica��o da cidade
     * @param id_serventia:
     *            identifica��o da serventia
     * 
     * @author jrcorrea
     */
    public String getProximaDataValidaPrazoCorrido(String datainicial, int prazo, ServentiaDt serventia, FabricaConexao obFabricaConexao) throws Exception {
        String stRetorno = "";

        // List lisDatas = new ArrayList();

        Calendar calCalendario = Calendar.getInstance();

        // Atribue a data inicial
        calCalendario.setTime(Funcoes.StringToDate(datainicial));

        /*
         * O prazo � contado da seguinte forma: deve iniciar em um dia util
         * e terminar em outro util. Assim, dada uma data pegue o proximo dia
         * util (retire s�bado, domigo e feriados) acresente o numero de
         * dias se caiu em um dia n�o util passe para o pr�ximo util
         */

        // Proximo dia, a contagem se inicia no dia seguinte
        calCalendario.add(Calendar.DATE, 1);

        /*
         * do{while ( lisDatas.size()<prazo){//crio uma lista com todas
         * as datas do periodo e vejo se existe prazo suspenso
         * lisDatas.add(calCalendario.getTime());
         * calCalendario.add(calCalendario.DATE, 1); } // testo se algum das
         * Datas da lista n�o s�o validas
         * PrazoSuspensoPs obPersistencia = new PrazoSuspensoPs(obFabricaConexao.getConexao()); inRetorno =
         * obPersistencia.getProximaDataValida(lisDatas , id_comarca,
         * id_cidade, id_serventia); // o prazo recebe a quantidade de Datas
         * n�o validas prazo = inRetorno; // limpo a lista de lisDatas para
         * o novo periodo lisDatas.clear(); }while(inRetorno>0);
         */

        boolean boTeste = false;
        PrazoSuspensoPs obPersistencia = new PrazoSuspensoPs(obFabricaConexao.getConexao());

        while (!boTeste) {
            // Verifico se n�o � s�bado ou domigo, passando para
            // segunda-feira
            passouParaSegundaFeira(calCalendario);
            // verifico se esta com prazo suspenso ou feriado
            boTeste = obPersistencia.isDataValida(calCalendario.getTime(), serventia);
            if (!boTeste) {
                calCalendario.add(Calendar.DATE, 1);
            } else if (prazo != -1) {
                // depois do proximo dia valido pego e coloco o calendario
                // na data X
                calCalendario.add(Calendar.DATE, (prazo-1));
                boTeste = false;
                prazo = -1;
            }

        }

        stRetorno = Funcoes.dateToStringSoData(calCalendario.getTime())+" 23:59:59";


        return stRetorno;

    }

    /**
     * Retorna a data v�lida ap�s a subtra��o do prazo passado, e a partir da data
     * passada.
     * 
     * @param datainicial:
     *            data para in�cio de contagem de prazo (data de inicio da audiencia, por exemplo)
     * @param prazo:
     *            prazo para cumprimento
     * @param id_comarca:
     *            identifica��o da comarca
     * @param id_cidade:
     *            identifica��o da cidade
     * @param id_serventia:
     *            identifica��o da serventia
     * 
     * @author lrcampos
     */
    public String getAnteriorDataValidaPrazoCorridoPJD(String datainicial, int prazo, ServentiaDt serventia, FabricaConexao obFabricaConexao) throws Exception {
        String stRetorno = "";

        Calendar calCalendario = Calendar.getInstance();

        // Atribue a data inicial
        calCalendario.setTime(Funcoes.StringToDate(datainicial));

        /*
         * O prazo � contado da seguinte forma: deve iniciar a partir da dataInicial
         * e terminar em um dia util. Assim, dada uma data subtraia o numero de
         * dias se caiu em um dia n�o util passe para o anterior util
         */

        boolean boTeste = false;
        PrazoSuspensoPs obPersistencia = new PrazoSuspensoPs(obFabricaConexao.getConexao());

        while (!boTeste) {
            // Verifico se n�o � s�bado ou domigo, passando para sexta-feira
        	passouParaSextaFeira(calCalendario);
            // verifico se esta com prazo suspenso ou feriado
	    //lrcampos 24/03/2020 14:21 - Alterando a chamada do m�todo, excluindo Resolu��o do CNJ
            boTeste = obPersistencia.isDataValidaSesaoVirtualPJD(calCalendario.getTime(), serventia);
            if (!boTeste) {
                calCalendario.add(Calendar.DATE, -1);
            } else if (prazo != 0) {
                // depois do dia anterior valido pego e coloco o calendario
                // na data X
                calCalendario.add(Calendar.DATE, (prazo));
                boTeste = false;
                prazo = 0;
            }

        }

        //lrcampos 11/11/2019 16:48 - Corre��o na hora pois deve ser fixada em 10:00
        String horaMinuto = " 10:00";
        stRetorno = Funcoes.dateToStringSoData(calCalendario.getTime()) + horaMinuto;


        return stRetorno;

    }
    
    
    /**
     * Retorna a data v�lida ap�s contagem de prazo passado, e a partir da data
     * passada. Seta o horario como o parametro hora.
     * 
     * @param datainicial:
     *            data para in�cio de contagem de prazo (data de leitura da
     *            intima��o, por exemplo)
     * @param prazo:
     *            prazo para cumprimento
     * @param hora:
     *            horario
     * @param id_comarca:
     *            identifica��o da comarca
     * @param id_cidade:
     *            identifica��o da cidade
     * @param id_serventia:
     *            identifica��o da serventia
     * 
     * @author jvosantos (copiado de getProximaDataValidaNovoCPC ~ jrcorrea)
     */
    // jvosantos - 25/07/2019 11:38 - Cria��o de m�todo novo para setar o horario final
    public String getProximaDataValidaNovoCPCComHoraPJD(String datainicial, int prazo, String hora, ServentiaDt serventia, FabricaConexao obFabricaConexao) throws Exception {
        String stRetorno = "";

        Calendar calCalendario = Calendar.getInstance();

        calCalendario.setTime(Funcoes.StringToDate(datainicial));

        /*
         * O prazo � contado da seguinte forma: deve-se contar somente dias uteis
         */     

        boolean boTeste = false;
        PrazoSuspensoPs obPersistencia = new PrazoSuspensoPs(obFabricaConexao.getConexao());
        int intAux = 0;

        do{
        	// Proximo dia, a contagem se inicia no dia seguinte
            calCalendario.add(Calendar.DATE, 1);
            // Verifico se n�o � s�bado ou domigo, passando para  segunda-feira
            passouParaSegundaFeira(calCalendario);
            // verifico se esta com prazo suspenso ou feriado
	    //lrcampos 24/03/2020 14:21 - Alterando a chamada do m�todo, excluindo Resolu��o do CNJ
            boTeste = obPersistencia.isDataValidaSesaoVirtualPJD(calCalendario.getTime(), serventia);
                                    
            if (boTeste) {                           
                intAux++;                                
            }                        
                        
        }while(intAux < prazo);

        stRetorno = Funcoes.dateToStringSoData(calCalendario.getTime())+" "+hora;

        return stRetorno;

    }
    
    /**
     * Retorna a data para leitura autom�tica de intima��es. Acrescenta a data
     * passada o prazo definido para leitura autom�tica (10 dias no caso).
     * 
     * @param datainicial:
     *            data envio intima��o
     * @param id_comarca:
     *            identifica��o da comarca
     * @param id_cidade:
     *            identifica��o da cidade
     * @param id_serventia:
     *            identifica��o da serventia
     * 
     * @author jrcorrea e msapaula
     */
    public String getDataLeituraAutomatica(String datainicial,  ServentiaDt serventia, FabricaConexao obFabricaConexao) throws Exception {
        String stRetorno = "";
        
        Calendar calCalendario = Calendar.getInstance();

        // atribuo a data inicial
        calCalendario.setTime(Funcoes.StringToDate(datainicial));
        /*
         * O prazo � contado da seguinte forma: deve contar a partir da data
         * de envio, independente de ser sabado, domingo ou feriado e
         * terminar em um dia �til. Assim, dada uma data, acrescente o
         * n�mero de dias para leitura automatica, se caiu em um dia n�o
         * util passe para o pr�ximo util
         */

        // Acrescenta o prazo X na data passada
        calCalendario.add(Calendar.DATE, Configuracao.PRAZO_LEITURA_AUTOMATICA);

        boolean boTeste;
        PrazoSuspensoPs obPersistencia = new PrazoSuspensoPs(obFabricaConexao.getConexao());

        // Verifica se est� com prazo suspenso, feriado ou se n�o � s�bado
        // ou domigo
        do {
            boTeste = obPersistencia.isDataValida(calCalendario.getTime(), serventia);
            if (!boTeste) calCalendario.add(Calendar.DATE, 1);

        } while (passouParaSegundaFeira(calCalendario) || !boTeste);

        stRetorno = Funcoes.dateToStringSoData(calCalendario.getTime());

        return stRetorno;
    }
    
    /**
     * M�todo que recebe a quantidade de dias que ser� adicionado na data inicial. Ir� retornar o pr�ximo dia �til apartir da data inicial mais(+) 
     * a quantidade de dias(quantidadeDias) necess�rio.
     * @param datainicial � a data inicial para calcular o dia final.
     * @param quantidadeDias � a quantidade de dias que ser� adicionado a data inicial para contar o prazo.
     * @param id_comarca � o id da comarca.
     * @param id_cidade � o id da cidade.
     * @param id_serventia � o id da serventia.
     * @param conexao � o objeto com os dados de conex�o.
     * @return String com a data final contado a quantidade de dias.
     * @throws Exception
     */
    public String getProximoDiaUtil(String datainicial, Integer quantidadeDias, ServentiaDt serventia, FabricaConexao obFabricaConexao) throws Exception {
        String stRetorno = "";

        Calendar calCalendario = Calendar.getInstance();

        // atribuo a data inicial
        calCalendario.setTime(Funcoes.StringToDate(datainicial));
        /*
         * O prazo � contado da seguinte forma: deve contar a partir da data
         * de envio, independente de ser sabado, domingo ou feriado e
         * terminar em um dia �til. Assim, dada uma data, acrescente o
         * n�mero de dias para leitura automatica, se caiu em um dia n�o
         * util passe para o pr�ximo dia util
         */

        // Acrescenta o prazo 'quantidadeDias' em dias na data passada
        calCalendario.add(Calendar.DATE, quantidadeDias);

        boolean boTeste;
        PrazoSuspensoPs obPersistencia = new PrazoSuspensoPs(obFabricaConexao.getConexao());

        // Verifica se est� com prazo suspenso, feriado ou se n�o � s�bado
        // ou domigo
        do {
            boTeste = obPersistencia.isDataValida(calCalendario.getTime(), serventia);
            if (!boTeste) calCalendario.add(Calendar.DATE, 1);

        } while (passouParaSegundaFeira(calCalendario) || !boTeste);

        stRetorno = Funcoes.dateToStringSoData(calCalendario.getTime());

        return stRetorno;
    }
    
    

    /**
     * M�todo que retorna a data de leitura v�lida para uma pend�ncia do tipo
     * Intima��o. Quando a leitura ocorre em um dia n�o �til, deve considerar
     * como data de leitura o primeiro dia �til seguinte.
     * 
     * @param datainicial:
     *            data atual da leitura
     * @param id_comarca:
     *            identifica��o da comarca
     * @param id_cidade:
     *            identifica��o da cidade
     * @param id_serventia:
     *            identifica��o da serventia
     * 
     * @author msapaula
     */
    public String getDataLeitura(String datainicial, ServentiaDt serventia, FabricaConexao obFabricaConexao) throws Exception {
        String stRetorno = "";
        boolean boTeste;
        
        PrazoSuspensoPs obPersistencia = new PrazoSuspensoPs(obFabricaConexao.getConexao());
        // Atribuo a data inicial ao Calendar
        Calendar calCalendario = Calendar.getInstance();
        calCalendario.setTime(Funcoes.StringToDate(datainicial));

        // Verifica se a data � dia n�o �til (prazo suspenso, feriado,
        // s�bado ou domigo)
        do {
            boTeste = obPersistencia.isDataValida(calCalendario.getTime(), serventia);
            if (!boTeste) calCalendario.add(Calendar.DATE, 1);

        } while (passouParaSegundaFeira(calCalendario) || !boTeste);

        stRetorno = Funcoes.dateToStringSoData(calCalendario.getTime());
        
        return stRetorno;
    }

    /**
     * M�todo que verifica se determinada data � sabado ou domingo, retornando
     * assim o pr�ximo dia �til.
     */
    private boolean passouParaSegundaFeira(Calendar calCalendario) {
        boolean boRetorno = false;
        // verico se o proximo dia � sabado
        if (calCalendario.get(Calendar.DAY_OF_WEEK) == 7) {
            // se for sabado passo para segunda, acrescentado 2 dias
            calCalendario.add(Calendar.DATE, 2);
            boRetorno = true;
        } else if (calCalendario.get(Calendar.DAY_OF_WEEK) == 1) {
            // se for domigo passo para segunda, acrescentado 1 dias
            calCalendario.add(Calendar.DATE, 1);
            boRetorno = true;
        }
        return boRetorno;
    }
    
    /**
     * lrcampos 18/06/2019
     * M�todo que verifica se determinada data � sabado ou domingo, retornando
     * assim o dia �til anterior.
     */
    private boolean passouParaSextaFeira(Calendar calCalendario) {
        boolean boRetorno = false;
        // verico se o proximo dia � sabado
        if (calCalendario.get(Calendar.DAY_OF_WEEK) == 7) {
            // se for sabado passo para sexta, subtraindo 1 dia
            calCalendario.add(Calendar.DATE, -1);
            boRetorno = true;
        } else if (calCalendario.get(Calendar.DAY_OF_WEEK) == 1) {
            // se for domigo passo para sexta, subtraindo 2 dias
            calCalendario.add(Calendar.DATE, -2);
            boRetorno = true;
        }
        return boRetorno;
    }
    
    

    /**
     * M�todo que consulta Prazos Suspenso pelo motivo
     * @throws Exception 
     */
    public List consultarDescricao(String descricao, String posicao) throws Exception{
        List tempList = null;
        FabricaConexao obFabricaConexao = null;
        ////System.out.println("..ne-ConsultaPrazoSuspenso");

        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PrazoSuspensoPs obPersistencia = new PrazoSuspensoPs(obFabricaConexao.getConexao());
            tempList = obPersistencia.consultarDescricao(descricao, posicao);
            // stUltimaConsulta=descricao;
            setQuantidadePaginas(tempList);
        
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return tempList;
    }
    
    public String consultarDescricaoJSON(String descricao, String posicao) throws Exception{
    	String stTemp = "";
        FabricaConexao obFabricaConexao = null;

        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PrazoSuspensoPs obPersistencia = new PrazoSuspensoPs(obFabricaConexao.getConexao());
            stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);        
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return stTemp;
    }
    
    public String consultarDescricaoPrazoSuspensoTipoJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		PrazoSuspensoTipoNe PrazoSuspensoTipone = new PrazoSuspensoTipoNe(); 
		stTemp = PrazoSuspensoTipone.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}
    
    public String consultarDescricaoComarcaJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		ComarcaNe Comarcane = new ComarcaNe(); 
		stTemp = Comarcane.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}
    
    public String consultarDescricaoServentiaJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		ServentiaNe Serventiane = new ServentiaNe(); 
		stTemp = Serventiane.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}
    
    public String consultarDescricaoCidadeJSON(String tempNomeBusca, String uf, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		CidadeNe Cidadene = new CidadeNe(); 
		stTemp = Cidadene.consultarDescricaoJSON(tempNomeBusca, uf, PosicaoPaginaAtual);
		
		return stTemp;
	}
    
    public boolean isDataValida(Date data, ServentiaDt serventia) throws Exception{
    	FabricaConexao obFabricaConexao = null;
        try{
        	obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PrazoSuspensoPs obPersistencia = new PrazoSuspensoPs(obFabricaConexao.getConexao());
            
            return obPersistencia.isDataValida(data, serventia);
                    
        } finally{
            if (obFabricaConexao != null) obFabricaConexao.fecharConexao();
        }
    }
    
    public boolean isDataValidaProtocolo(Date data, ServentiaDt serventia) throws Exception{
    	FabricaConexao obFabricaConexao = null;
        try{
        	obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PrazoSuspensoPs obPersistencia = new PrazoSuspensoPs(obFabricaConexao.getConexao());
            
            return obPersistencia.isDataValidaProtocolo(data, serventia);
                    
        } finally{
            if (obFabricaConexao != null) obFabricaConexao.fecharConexao();
        }
    }
}
