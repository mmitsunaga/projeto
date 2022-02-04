package br.gov.go.tj.projudi.temp;

import java.io.ByteArrayInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import br.gov.go.tj.projudi.ne.CertificadoNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.Certificado.PKCS12Parser;

@SuppressWarnings("unchecked")
public class ArquivosImportar  {
    private long loQtdTotal = 0;
    private long loQtdPronto = 0;
    private Date dataInicio=null;
    private Date dataFim=null;
    private String stStatus = "Parado";
    private boolean boContinuar = true;
    private static ArquivosImportar obArquivoImportar = null;
    private static PKCS12Parser p12parser;
    private Iterator iterator=null;
    private AtualizacaoNe atualizacaoNe =null;
    private static int loQtdThreads = 0;
    private long qtdBytes = 0;
    private long loTempoLeitura = 0;
    private long loTempoAssinatura = 0;
    private long loTempoEscrita = 0;
    private List lisThreads;    
    
    public synchronized void addQtdBytes(long qtdbytes) throws Exception {
        qtdBytes+= qtdbytes;
    }
    public synchronized void addTempoLeitura(long tempo){
        loTempoLeitura+= tempo;
    }
    public synchronized void addTempoAssinatura(long tempo){
        loTempoAssinatura+= tempo;
    }
    public synchronized void addTempoEscrita(long tempo){
        loTempoEscrita+= tempo;
    }    
    
    public void removeThread(){
//        //System.out.println("Thread  " + loQtdThreads + " Parada ") ;
        loQtdThreads--;
        if (loQtdThreads==0) {
            stStatus="Parado";
            qtdBytes = 0;
            loTempoLeitura = 0;
            loTempoAssinatura = 0;
            loTempoEscrita = 0;
            lisThreads.clear();            
        }
    }
    
    private  ArquivosImportar() {
                 
    }
    
    public PKCS12Parser getParse(){
        return p12parser;
    }
    
    public synchronized  AtualizacaoDt getData() throws Exception{
        AtualizacaoDt tempRetorno = null;
        if (iterator==null || !iterator.hasNext()){
            long loId_Inicial = atualizacaoNe.consultarIdInicial();
            List listAtualizacaoDt = atualizacaoNe.consultarArquivosProjudi(loId_Inicial);
            if (listAtualizacaoDt!=null && listAtualizacaoDt.size()>0){
                iterator= listAtualizacaoDt.iterator();
                tempRetorno=(AtualizacaoDt)iterator.next();                
            }    
        }else{
            tempRetorno=(AtualizacaoDt)iterator.next();            
        }
        return tempRetorno;
    }
    
    public synchronized static ArquivosImportar getInstacia() throws Exception{
        
        if (obArquivoImportar ==null) {
            obArquivoImportar = new ArquivosImportar();
            
            ByteArrayInputStream pkcs12 = new ByteArrayInputStream( (new CertificadoNe()).consultaCertificadoSistema().getConteudo());
            p12parser = new PKCS12Parser(pkcs12, ProjudiPropriedades.getInstance().getSenhaIdentidadeDigitalSistema());
        }
        
        return obArquivoImportar;
        
    }
        
    public long getLoQtdTotal() {
        return loQtdTotal;
    }

    public long getLoQtdPronto() {
        return loQtdPronto;
    }
    
    public void setStatus(String status){
        stStatus = status;
    }
    
    public synchronized String getStStatus() {
        return stStatus;
    }

    public String getPrevisao() {
        Date tempPrevisto = null;
        if (loQtdPronto > 0) {
            long tempAtual = (new Date()).getTime() - dataInicio.getTime();
            long tempTotal = (tempAtual / loQtdPronto) * (loQtdTotal - loQtdPronto);
            tempPrevisto = new Date((new Date()).getTime() + tempTotal);
            return Funcoes.DataHora(tempPrevisto);
        } else
            return "";
    }
    
    
    public String getPrevisao1() {
        Date tempPrevisto = null;
        DecimalFormat df = new DecimalFormat("###,###,##0.00");  
                  
        if (loQtdPronto > 0) {
            long tempAtual = (new Date()).getTime() - dataInicio.getTime();
            long tempTotal = (tempAtual / loQtdPronto) * (loQtdTotal - loQtdPronto);
            tempPrevisto = new Date((new Date()).getTime() + tempTotal);
            long[] loTemp=Funcoes.diferencaDatas(tempPrevisto, new Date());
            
            float flMediaBytes =  ((qtdBytes/1048576) / (tempAtual/1000));
            
            return  loTemp[0] + " dia(s) " + loTemp[1] + " hora(s) " + loTemp[2] + "  minuto(s) " + loTemp[3] + " segundo(s) | " + df.format(flMediaBytes) + " MB/seg | Total de " + df.format(qtdBytes/1048576) + " MB |  "  ;
        } else
            return "";
    }
    
    public String getPorcentagem() {
        String stPorcentagem = "0";
        if (loQtdTotal > 0) {
            stPorcentagem = String.valueOf((loQtdPronto * 100) / loQtdTotal);
        }
        return stPorcentagem;
    }

    public String getDataInicio() {
        if (dataInicio == null)
            return "";
        else
            return Funcoes.DataHora(dataInicio);
    }

    public String getDataFim() {
        if (dataFim == null)
            return "";
        else
            return Funcoes.DataHora(dataFim);
    }

    public synchronized void   aumentarPronto() {
        if (dataInicio==null) dataInicio = new Date();
        dataFim = new Date();
        loQtdPronto++;        
    }

    public boolean isContinuar() {

        return boContinuar;
    }

    public void Rodar() throws Exception {
    	atualizacaoNe= new AtualizacaoNe();
        lisThreads = new ArrayList();
        loQtdTotal= atualizacaoNe.consultarQtdTotalArquivos();              
        loQtdPronto = 0;
        dataInicio=null;
        dataFim = null;
        stStatus = "Rodando";      
        boContinuar = true;
    }
    
    public void Parar() {
        boContinuar = false;
        stStatus = "Parado";   
    }
    
    public int getQtdThreads() {
        return loQtdThreads;
    }

    public String getTempoLeitura() {

        return Funcoes.formatarTempo(loTempoLeitura);
    }

    public String getTempoEscrita() {

        return Funcoes.formatarTempo(loTempoEscrita);
    }

    public String getTempoAssinatura() {

        return Funcoes.formatarTempo(loTempoAssinatura);
    }
    public String getTempoTotal() {
        long tempAtual = 0;
        
        if (dataInicio!=null)    tempAtual = (new Date()).getTime() - dataInicio.getTime();
        
        return Funcoes.formatarTempo(tempAtual);
    }
   
	public void addThrad(Thread thread) {
        thread.start();
        lisThreads.add(thread);   
        loQtdThreads++;
    }
}

