package br.gov.go.tj.projudi.ne;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import br.gov.go.tj.projudi.Projudi;
import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.AudienciaDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoVotantesDt;
import br.gov.go.tj.projudi.dt.AudienciaTipoDt;
import br.gov.go.tj.projudi.dt.CargoTipoDt;
import br.gov.go.tj.projudi.dt.CidadeDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.CorreiosDt;
import br.gov.go.tj.projudi.dt.EnderecoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.ImpedimentoTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.MandadoJudicialDt;
import br.gov.go.tj.projudi.dt.MandadoJudicialStatusDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoIntimacaoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoProcessoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaArquivoDt;
import br.gov.go.tj.projudi.dt.PendenciaCorreiosDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaResponsavelDt;
import br.gov.go.tj.projudi.dt.PendenciaStatusDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoCadastroDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoEncaminhamentoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteAdvogadoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoPartePrisaoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoPrioridadeDt;
import br.gov.go.tj.projudi.dt.ProcessoTemaDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.RecursoParteDt;
import br.gov.go.tj.projudi.dt.RecursoSecundarioParteDt;
import br.gov.go.tj.projudi.dt.RedistribuicaoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.dt.SustentacaoOralDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.dt.VotanteTipoDt;
import br.gov.go.tj.projudi.dt.ZonaBairroRegiaoDt;
import br.gov.go.tj.projudi.dt.dwrMovimentarProcesso;
import br.gov.go.tj.projudi.ps.PendenciaPs;
import br.gov.go.tj.projudi.util.DistribuicaoProcesso;
import br.gov.go.tj.projudi.util.DistribuicaoProcessoServentiaCargo;
import br.gov.go.tj.projudi.util.EscreverTextoPDF;
import br.gov.go.tj.projudi.util.GerarCapaIntimacaoPDF;
import br.gov.go.tj.projudi.util.GerenciadorEmail;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Cifrar;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.Relatorios;
import br.gov.go.tj.utils.pdf.ConcatenatePDF;
import br.gov.go.tj.utils.pdf.ConverteImagemPDF;
import br.gov.go.tj.utils.pdf.ConverterHtmlPdf;
import br.gov.go.tj.utils.pdf.GerarPDF;
import br.gov.go.tj.utils.pdf.InterfaceJasper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;


//---------------------------------------------------------
public class PendenciaNe extends PendenciaNeGen {

    /**
     * 
     */
    private static final long serialVersionUID = 7309685480790405893L;
    
    private static Logger logger = Logger.getLogger(Projudi.class);
    /**
     * Metodo de verifica��o padr�o do gerador de c�digo
     * 
     * @author Leandro Bernardes
    
     * @return String com mensagem de retorno
     * @throws Exception
     */
    public String Verificar(PendenciaDt dados) {

        String stRetorno = "";

        ////System.out.println("..nePendenciaVerificar()");
        return stRetorno;

    }

    /**
     * Verifica se uma pend�ncia pode ser encaminhada
     * 
     * @author Leandro Bernardes
    
     * @return String com mensagem de retorno
     * @throws Exception
     */
    public String verificarEncaminhar(PendenciaDt dados, ServentiaDt serventiaDt, ServentiaCargoDt serventiaCargoDt) {

        String stRetorno = "";

        if (dados.getId().equalsIgnoreCase("")) {
            stRetorno += "Pend�ncia N�o encontrada.";
        }
        if (serventiaDt.getId().equals("") && serventiaCargoDt.getId().equals("")) {
            stRetorno += "Informe o destino da pend�ncia.";
        }

        return stRetorno;

    }
    
    /**
     * Verifica se uma pend�ncia pode ter seu tipo trocado
     * 
     * @author Leandro Bernardes
    
     * @return String com mensagem de retorno
     * @throws Exception
     */
    public String verificarAlteracaoPendenciaTipo(PendenciaDt dados, String codPendenciaTipo) {

        String stRetorno = "";

        if (dados.getId().equalsIgnoreCase("")) {
            stRetorno += "Pend�ncia N�o encontrada.";
        }
        if (dados.getPendenciaTipoCodigo() == null || dados.getPendenciaTipoCodigo().equals("")) {
            stRetorno += "N�o foi Poss�vel identificar o tipo da pend�ncia.";
        }
        if (codPendenciaTipo == null || codPendenciaTipo.length()==0 || codPendenciaTipo.equalsIgnoreCase("-1")){
        	   stRetorno += "Informe o Novo tipo da pend�ncia.";
        } 
        
        if (!dados.isTrocarTipo()){
        	  stRetorno += "N�o � poss�vel alterar o Tipo dessa pend�ncia";
        }
   
        if (dados.getPendenciaTipoCodigo() != null && !dados.getPendenciaTipoCodigo().equals("") && codPendenciaTipo != null && dados.getPendenciaTipoCodigo().equalsIgnoreCase(codPendenciaTipo)) {
            stRetorno += "O Novo tipo de pend�ncia deve ser diferente do atual.";
        }

        return stRetorno;

    }

    /**
     * Verifica se uma pend�ncia pode ser expedida
     * 
     * @author Leandro Bernardes
    
     * @return String com mensagem de retorno
     * @throws Exception
     */
    public String verificarExpedir(UsuarioDt usuarioDt, ServentiaDt serventiaDt, PendenciaDt pendenciaDt, List arquivos) throws Exception {

        String stRetorno = "";

        if (usuarioDt.getId_Serventia().equalsIgnoreCase(serventiaDt.getId())) {
            stRetorno += "Pend�ncia n�o pode ser expedida para serventia do usu�rio logado. ";
        }        
        if(Funcoes.StringToInt(pendenciaDt.getPendenciaTipoCodigo()) == PendenciaTipoDt.MANDADO && this.temCentralProjudiImplantada(serventiaDt.getId())) { 
           if (serventiaDt.getId().isEmpty()) {
                stRetorno += "\n Informe a serventia para expedir o mandado.";
            } else {
            		if(serventiaDt.getServentiaTipoCodigo() == null || serventiaDt.getServentiaTipoCodigo().isEmpty()) {
            			serventiaDt = new ServentiaNe().consultarId(serventiaDt.getId());
            		}
					if (Funcoes.StringToInt(serventiaDt.getServentiaTipoCodigo()) != (ServentiaTipoDt.CENTRAL_MANDADOS)) {
						stRetorno += "\n Serventia escolhida nao � do tipo Central de Mandados.";
					}
            }
        }        
        if (serventiaDt.getId() != null && !serventiaDt.getId().equals("") && serventiaDt.getServentiaTipoCodigo().equalsIgnoreCase(String.valueOf(ServentiaTipoDt.CENTRAL_MANDADOS)) && pendenciaDt.getPendenciaTipoCodigo().equalsIgnoreCase(String.valueOf(PendenciaTipoDt.MANDADO))) {
        	ProcessoParteNe processoParteNe = new ProcessoParteNe();
        	ProcessoParteDt processoParteDt = null;
        	processoParteDt = processoParteNe.consultarId(pendenciaDt.getId_ProcessoParte());
        	if(processoParteDt.getEnderecoParte().getId_Bairro() != null && !processoParteDt.getEnderecoParte().getId_Bairro().equalsIgnoreCase("")) {
        		ZonaBairroRegiaoNe zoNe = new ZonaBairroRegiaoNe();
        		ZonaBairroRegiaoDt zoDt = zoNe.consultarIdBairro(processoParteDt.getEnderecoParte().getId_Bairro());
        		if(zoDt != null) {
        			pendenciaDt.setId_Bairro(processoParteDt.getEnderecoParte().getId_Bairro());
        			pendenciaDt.setBairro(processoParteDt.getEnderecoParte().getBairro());
        			pendenciaDt.setId_EnderecoParte(processoParteDt.getId_Endereco());
        		}  else {
            		stRetorno += "Bairro da parte: " + pendenciaDt.getNomeParte() + ", n�o zoneado. ";
            	}
        	} else {
        		stRetorno += "Bairro da parte: " + pendenciaDt.getNomeParte() + ", n�o encontrado. ";
        	}
        	if(processoParteDt.getId_Endereco() != null && !processoParteDt.getId_Endereco().equalsIgnoreCase("")) {
        		 EnderecoNe enderecoNe = new EnderecoNe();
        		 EnderecoDt enderecoDt = new EnderecoDt();
        		 enderecoDt = enderecoNe.consultarId(processoParteDt.getId_Endereco());
        		 if(enderecoDt.getLogradouro().equalsIgnoreCase("") || enderecoDt.getNumero().equalsIgnoreCase("")) {
        		 	stRetorno += "Faltam dados do Endere�o da Parte. Logradouro ou N�mero.\n\n";
        		 }
        		  		
        	} else {
        		stRetorno += "\n Endere�o da Parte n�o encontrado.";
        	}
        	
        }

        stRetorno += this.validarArquivosPendencia(pendenciaDt, arquivos);

        return stRetorno;

    }

    /**
     * Quando clica em resolver pend�ncia, faz uma verifica��o pr�via que retorna uma mensagem
     * informativa. Pode ser utilizada para acusar, por exemplo, se o endere�o da parte de uma
     * uma pend�ncia de mandado est� preenchida. Isto possibilita n�o precisar chegar no final
     * da resolu��o da pend�ncia para acusar algo que impede a resolu��o da mesma.
     * 
     * @return String
     * @author hrrosa
     */
    public String verificarPreviaResolverPendencia(UsuarioDt usuarioDt, PendenciaDt pendenciaDt, String idServentia) throws Exception {
    	String stRetorno = "";
    	
        if ( pendenciaDt.isPendenciaTipoMandado()) {
        	
        	ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();
        	ServentiaNe serventiaNe = new ServentiaNe();
        	//Este if garante que a verifica��o ser� feita apenas para serventias realacionadas com uma Central de Mandados,
        	//impedindo portanto que cause impacto nos mandados offline que atualmente est�o sendo utilizados. Ap�s a implanta��o
        	//da Central em todas as serventias e uso exclusivo dos mandados online, este if poder� ser retirado.
        	if(idServentia != null && serventiaRelacionadaNe.consultarCentralMandadosRelacionada(idServentia) != null && serventiaNe.temCentralProjudiImplantada(idServentia)) {
        	
	        	ProcessoParteNe processoParteNe = new ProcessoParteNe();
	        	ProcessoParteDt processoParteDt = null;
	        	processoParteDt = processoParteNe.consultarId(pendenciaDt.getId_ProcessoParte());
	        	if(processoParteDt.getEnderecoParte().getId_Bairro() != null && !processoParteDt.getEnderecoParte().getId_Bairro().equalsIgnoreCase("")) {
	        		ZonaBairroRegiaoNe zoNe = new ZonaBairroRegiaoNe();
	        		ZonaBairroRegiaoDt zoDt = zoNe.consultarIdBairro(processoParteDt.getEnderecoParte().getId_Bairro());
	        		if(zoDt != null) {
	        			pendenciaDt.setId_Bairro(processoParteDt.getEnderecoParte().getId_Bairro());
	        			pendenciaDt.setBairro(processoParteDt.getEnderecoParte().getBairro());
	        			pendenciaDt.setId_EnderecoParte(processoParteDt.getId_Endereco());
	        		}  else {
	            		stRetorno += " - Bairro da parte \"" + pendenciaDt.getNomeParte() + "\" n�o zoneado. \n";
	            	}
	        	} else {
	        		stRetorno += " - Bairro da parte \"" + pendenciaDt.getNomeParte() + "\" n�o encontrado. \n";
	        	}
	        	if(processoParteDt.getId_Endereco() != null && !processoParteDt.getId_Endereco().equalsIgnoreCase("")) {
	        		 EnderecoNe enderecoNe = new EnderecoNe();
	        		 EnderecoDt enderecoDt = new EnderecoDt();
	        		 enderecoDt = enderecoNe.consultarId(processoParteDt.getId_Endereco());
	        		 if(enderecoDt.getLogradouro().equalsIgnoreCase("") || enderecoDt.getNumero().equalsIgnoreCase("")) {
	        			stRetorno +=  "\n Faltam dados do Endere�o da Parte. Logradouro ou N�mero.\n";
	        		 }   	
	        	
	        	
	        	} else {
	        		stRetorno += " - Endere�o da Parte n�o encontrado. \n";
	        	}
	        	
	        	if(!stRetorno.equals("")){
	        		stRetorno = "Antes de continuar com a expedi��o voc� deve corrigir:\n\n" + stRetorno; 
	        	}
        	}
        	
        }
    	
    	return stRetorno;
    }
    
    /**
     * Verifica se uma determinada pendencia e publica
     * 
     * @author Leandro Bernardes
     * @param pendenciaDt
     *            vo de pendencia
     * @param UsuarioDt
     *            usu�rio logado
     * @return boolean
     * @throws Exception
     */
    public boolean verificarDescartarPendenciaNormal(PendenciaDt dados, UsuarioDt usuarioDt){
    	boolean retorno = false;
    	
    	if (!dados.isPendenciaDeProcesso() && dados.getId_UsuarioCadastrador().equals(usuarioDt.getId_UsuarioServentia()))
    		retorno = true;
    	
    	return retorno;
    }
    
    /**
     * Marcar leitura automatica
     * 
     * @author Leandro Bernardes
     * @since 26/10/2009
     * @throws Exception
     */
    public void marcarLeituraAutomatica() throws MensagemException, Exception {
        List listaPendencias = this.consultarPendenciasLeituraAutomatica();
        if (listaPendencias != null && listaPendencias.size() > 0){
	        for (Iterator iterator = listaPendencias.iterator(); iterator.hasNext();) {
	        	 String stComplemento ="";
	        	 String valorNovo="";
	        	 String valorAtual = "";
	        	try{
		            PendenciaDt pendencia = (PendenciaDt) iterator.next();
		            UsuarioDt usuarioDt = new UsuarioDt();
		            usuarioDt.setId(UsuarioDt.SistemaProjudi);
		            usuarioDt.setId_UsuarioServentia(UsuarioServentiaDt.SistemaProjudi);
		            usuarioDt.setNome("Sistema PROJUDI");
		
		            valorAtual = "[Id_Pendencia:" + pendencia.getId() + "; Id_UsuarioFinalizador:" + pendencia.getId_UsuarioFinalizador() + "; Data_Fim:" + pendencia.getDataFim() + "]";
		            valorNovo = "[Id_Pendencia:" + pendencia.getId() + "; Id_UsuarioFinalizador:" + usuarioDt.getId_UsuarioServentia() + "; Data_Fim:" + Funcoes.DataHora(new Date()) + "]";
		
		            LogDt obLogDt = new LogDt("LeituraAutomaticaPendencia", pendencia.getId(), UsuarioDt.SistemaProjudi, "Servidor", String.valueOf(LogTipoDt.LeituraAutomaticaPendencia), valorAtual, valorNovo);
		
		            String responsavel = "";
		            
		            if ( pendencia.getNomeParte() != null && !pendencia.getNomeParte().equalsIgnoreCase("")){
		            	responsavel = pendencia.getNomeParte();
		            }else{
		            	responsavel = "Minist�rio P�blico";
		            }
		            //altera��o para limitar o tamanho do complemento na leitura automatica
		            stComplemento = "Automaticamente para " + obtenhaComplementoLeituraPendenciaIntimacao(pendencia) + responsavel + " (Referente � Mov. " + pendencia.getMovimentacao() + ")";
		           
		            if (stComplemento.length()>255)	{            	
		            	if (responsavel.length() > stComplemento.length()-255) {
		            		stComplemento = "Automaticamente para " + obtenhaComplementoLeituraPendenciaIntimacao(pendencia) + responsavel.substring(0,(responsavel.length() -  (stComplemento.length()-255))) + " (Referente � Mov. " + pendencia.getMovimentacao() + ")";
		            	}
		            }
		            
		            this.intimacaoOuCitacaoLidaAutomatica(pendencia, usuarioDt, stComplemento, obLogDt);
	        	}catch(Exception e){
	        		System.out.print("Erro na marcarLeituraAutomatica(): " + valorNovo + " " + stComplemento ); 
	        		e.printStackTrace( );
	        	}
	        }
        }
    }
    
    /**
     * Fechar pendencias de liberacao de acesso automaticamente
     * 
     * @author Leandro Bernardes
     * @throws Exception
     */
    public void fecharPendenciaAutomatica() throws Exception {
        List listaPendencias = this.consultarPendenciasFechamentoAutomatica();
        if (listaPendencias != null && listaPendencias.size() > 0){
	        for (Iterator iterator = listaPendencias.iterator(); iterator.hasNext();) {
	            PendenciaDt pendencia = (PendenciaDt) iterator.next();
	            UsuarioDt usuarioDt = new UsuarioDt();
	            usuarioDt.setId(UsuarioDt.SistemaProjudi);
	            usuarioDt.setId_UsuarioServentia(UsuarioServentiaDt.SistemaProjudi);
	            usuarioDt.setNome("Sistema PROJUDI");
	
	            String valorAtual = "[Id_Pendencia:" + pendencia.getId() + "; Id_UsuarioFinalizador:" + pendencia.getId_UsuarioFinalizador() + "; Data_Fim:" + pendencia.getDataFim() + "]";
	            String valorNovo = "[Id_Pendencia:" + pendencia.getId() + "; Id_UsuarioFinalizador:" + usuarioDt.getId_UsuarioServentia() + "; Data_Fim:" + Funcoes.DataHora(new Date()) + "]";
	
	            LogDt obLogDt = new LogDt("FechamentoAutomaticoPendencia", pendencia.getId(), UsuarioDt.SistemaProjudi, "Servidor", String.valueOf(LogTipoDt.FechamentoAutomaticoPendencia), valorAtual, valorNovo);
	            this.fechamentoAutomaticoPendecia(pendencia, usuarioDt, obLogDt);
	          
	        }
        }
    }
    
    /**
     * Fechar pendencias de solicita��o de carga automaticamente
     * 
     * @author Leandro Bernardes
     * @throws Exception
     */
    public void fecharPendenciasSolicitacaoCargaAutomatica() throws Exception {
        List listaPendencias = this.consultarPendenciasSolicitacaoCargaFechamentoAutomatica();
        if (listaPendencias != null && listaPendencias.size() > 0){
	        for (Iterator iterator = listaPendencias.iterator(); iterator.hasNext();) {
	            PendenciaDt pendencia = (PendenciaDt) iterator.next();
	            pendencia.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_CANCELADA));
	            UsuarioDt usuarioDt = new UsuarioDt();
	            usuarioDt.setId(UsuarioDt.SistemaProjudi);
	            usuarioDt.setId_UsuarioServentia(UsuarioServentiaDt.SistemaProjudi);
	            usuarioDt.setNome("Sistema PROJUDI");
	
	            String valorAtual = "[Id_Pendencia:" + pendencia.getId() + "; Id_UsuarioFinalizador:" + pendencia.getId_UsuarioFinalizador() + "; Data_Fim:" + pendencia.getDataFim() + "]";
	            String valorNovo = "[Id_Pendencia:" + pendencia.getId() + "; Id_UsuarioFinalizador:" + usuarioDt.getId_UsuarioServentia() + "; Data_Fim:" + Funcoes.DataHora(new Date()) + "]";
	
	            LogDt obLogDt = new LogDt("FechamentoAutomaticoPendencia", pendencia.getId(), UsuarioDt.SistemaProjudi, "Servidor", String.valueOf(LogTipoDt.FechamentoAutomaticoPendencia), valorAtual, valorNovo);
	            this.fechamentoAutomaticoPendecia(pendencia, usuarioDt, obLogDt);
	          
	        }
        }
    }
    
    /**
     * Fechar pendencias de liberacao de acesso
     * @param PendenciaDt
     *            pendenciaDt
     * @param UsuarioDt
     *            usuarioDt
     * @param LogDt
     *            logDt
     * @author Leandro Bernardes
     * @throws Exception
     */
    private void fechamentoAutomaticoPendecia(PendenciaDt pendenciaDt, UsuarioDt usuarioDt, LogDt logDt) throws Exception {
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            obFabricaConexao.iniciarTransacao();

            obPersistencia.fecharPendencia(pendenciaDt, usuarioDt);
            obLog.salvar(logDt, obFabricaConexao);
            
           // if (pendenciaDt != null && pendenciaDt.getDataFim()!= null && pendenciaDt.getDataFim().length()>0 && pendenciaDt.getDataVisto() != null && pendenciaDt.getDataVisto().length()>0){
            obPersistencia.moverPendencia(pendenciaDt.getId());
           // }

            obFabricaConexao.finalizarTransacao();
        } catch (Exception e) {
            obFabricaConexao.cancelarTransacao();
            throw e;
        } finally {
            obFabricaConexao.fecharConexao();
        }
    }
    
    public void reaberturaAutomaticaPendencia(PendenciaDt pendencia) throws Exception {
        UsuarioDt usuarioDt = new UsuarioDt();
        usuarioDt.setId(UsuarioDt.SistemaProjudi);
        usuarioDt.setId_UsuarioServentia(UsuarioServentiaDt.SistemaProjudi);
        usuarioDt.setNome("Sistema PROJUDI");

        String valorAtual = "[Id_Pendencia:" + pendencia.getId() + "; Id_UsuarioFinalizador:" + pendencia.getId_UsuarioFinalizador() + "; Data_Fim:" + pendencia.getDataFim() + "]";
        String valorNovo = "[Id_Pendencia:" + pendencia.getId() + "; Id_UsuarioFinalizador:" + usuarioDt.getId_UsuarioServentia() + "; Data_Fim:" + Funcoes.DataHora(new Date()) + "]";

        LogDt obLogDt = new LogDt("ReaberturaAutomaticaPendencia", pendencia.getId(), UsuarioDt.SistemaProjudi, "Servidor", String.valueOf(LogTipoDt.ReaberturaAutomaticaPendencia), valorAtual, valorNovo);
        this.reaberturaAutomaticaPendencia(pendencia, obLogDt);	    
    }
    
    /**
     * Fechar pendencias 
     * @param PendenciaDt
     *            pendenciaDt
     * @param LogDt
     *            logDt
     * @author mmgomes
     * @throws Exception
     */
    private void reaberturaAutomaticaPendencia(PendenciaDt pendenciaDt, LogDt logDt) throws Exception {
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            obFabricaConexao.iniciarTransacao();

            obPersistencia.moverPendenciaPendFinalParaPend(pendenciaDt.getId());
            
            pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_EM_ANDAMENTO));
            obPersistencia.reAbrirPendencia(pendenciaDt);
            obLog.salvar(logDt, obFabricaConexao);

            obFabricaConexao.finalizarTransacao();
        } catch (Exception e) {
            obFabricaConexao.cancelarTransacao();
            throw e;
        } finally {
            obFabricaConexao.fecharConexao();
        }

    }
    
    public void reaberturaAutomaticaPendencia(PendenciaDt pendenciaDt, UsuarioDt usuarioDt, FabricaConexao obFabricaConexao) throws Exception {
        String valorAtual = "[Id_Pendencia:" + pendenciaDt.getId() + "; Id_UsuarioFinalizador:" + pendenciaDt.getId_UsuarioFinalizador() + "; Data_Fim:" + pendenciaDt.getDataFim()  + "; Status:" + pendenciaDt.getPendenciaStatusCodigo() + "]";
        
        pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_EM_ANDAMENTO));
        
        String valorNovo = "[Id_Pendencia:" + pendenciaDt.getId() + "; Id_UsuarioFinalizador:; Data_Fim:" + "; Status:" + pendenciaDt.getPendenciaStatusCodigo() +"]";

        LogDt obLogDt = new LogDt("ReaberturaAutomaticaPendencia", pendenciaDt.getId(), usuarioDt.getId(), "Servidor", String.valueOf(LogTipoDt.ReaberturaAutomaticaPendencia), valorAtual, valorNovo);

        PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());  
        
        obPersistencia.moverPendenciaPendFinalParaPend(pendenciaDt.getId());        
        
        obPersistencia.reAbrirPendencia(pendenciaDt);
        
        obLog.salvar(obLogDt, obFabricaConexao);
    }

    /**
     * Nao expedir
     * 
     * @author Ronneesley Moura Teles
     * @since 13/01/2009 10:24
     * @param idPendencia
     *            id da pendencia a nao expedir
     * @throws Exception
     */
    public void naoExpedir(String idPendencia, UsuarioDt usuarioDt) throws MensagemException, Exception {
        PendenciaDt pendenciaDt = this.consultarId(idPendencia);
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            LogDt obLogDt = new LogDt("Pendencia", pendenciaDt.getId(), pendenciaDt.getId_UsuarioLog(), pendenciaDt.getIpComputadorLog(), String.valueOf(LogTipoDt.FinalizarPendencia), obDados.getPropriedades(), pendenciaDt.getPropriedades());

            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
            obPersistencia.responder(pendenciaDt.getId(), usuarioDt.getId_UsuarioServentia(), PendenciaStatusDt.ID_NAO_CUMPRIDA, df.format(new Date()), df.format(new Date()));

            obLog.salvar(obLogDt, obFabricaConexao);
        
        } finally {
            obFabricaConexao.fecharConexao();
        }
    }

    /**
     * Nao expedir
     * 
     * @author Leandro Bernardes
     * @since 01/06/2009
     * @param pendenciaDt,
     *            pendencia a nao expedir
     * @param usuarioDt,
     *            usuario efetuando opera��o
     * @param arquivos,
     *            arquivos vinculado a pend�ncia
     * @throws Exception
     */
    public void naoExpedirComArquivos(PendenciaDt pendenciaDt, UsuarioDt usuarioDt, List arquivos) throws MensagemException, Exception {
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            obFabricaConexao.iniciarTransacao();
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            LogDt obLogDt = new LogDt("Pendencia", pendenciaDt.getId(), pendenciaDt.getId_UsuarioLog(), pendenciaDt.getIpComputadorLog(), String.valueOf(LogTipoDt.FinalizarPendencia), obDados.getPropriedades(), pendenciaDt.getPropriedades());

            // Se tem arquivos
            if (arquivos != null && arquivos.size() > 0) {
                this.inserirArquivos(pendenciaDt, arquivos, true, usuarioDt, obFabricaConexao);
            }

            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
            pendenciaDt.setDataFim(df.format(new Date()));
            obPersistencia.responder(pendenciaDt.getId(), usuarioDt.getId_UsuarioServentia(), PendenciaStatusDt.ID_NAO_CUMPRIDA, df.format(new Date()), df.format(new Date()));

            obLog.salvar(obLogDt, obFabricaConexao);
            
            if (pendenciaDt != null && pendenciaDt.getDataFim()!= null && pendenciaDt.getDataFim().length()>0
            		&& pendenciaDt.getDataVisto() != null && pendenciaDt.getDataVisto().length()>0){
            	obPersistencia.moverPendencia(pendenciaDt.getId());
            }
            
            obFabricaConexao.finalizarTransacao();
        
        } catch (Exception e) {
            obFabricaConexao.cancelarTransacao();
            throw e;
        } finally {
            obFabricaConexao.fecharConexao();
        }

    }

    /**
     * Marcar a leitura de uma pendencia
     * 
     * @author Ronneesley Moura Teles
     * @since 21/01/2009 11:26
     * @param pendenciaDt
     *            vo para pendencias
     * @param usuarioDt
     *            usuario
     * @throws Exception
     */
    public PendenciaDt marcarLido(String idPendencia, String idUsuarioLog, String ipUsuarioLog, UsuarioDt usuarioDt, boolean aguardandoParecer) throws MensagemException, Exception {
        PendenciaDt pendenciaDt = new PendenciaDt();
        pendenciaDt = this.consultarId(idPendencia);
        if (pendenciaDt!=null && pendenciaDt.isIntimacaCitacao()) {
            pendenciaDt.setLido(true);
            LogDt obLogDt = new LogDt("Pendencia", pendenciaDt.getId(), idUsuarioLog, ipUsuarioLog, String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), pendenciaDt.getPropriedades());
            this.intimacaoOuCitacaoLidaUsuario(pendenciaDt, usuarioDt, obLogDt, aguardandoParecer);
        } else {
            throw new MensagemException("Pend�ncia n�o � do tipo intima��o ou cita��o.");
        }
        return pendenciaDt;
    }

    /**
     * Marcar a leitura de uma pendencia
     * 
     * @author Leandro Bernardes
     * @since 26/10/2009
     * @param pendenciaDt
     *            vo para pendencias
     * @param usuarioDt
     *            usuario
     * @param LogDt
     *            logDt
     * @throws Exception
     */
    /*public void marcarLido(PendenciaDt pendenciaDt, UsuarioDt usuarioDt, LogDt logDt) throws Exception {
        pendenciaDt.setLido(true);
        this.intimacaoOuCitacaoLida(pendenciaDt, usuarioDt, "", logDt, false, 0);
    }*/

    /**
     * Intimacao ou citacao lida pelo sistema
     * 
     * @param pendenciaDt
     *            vo de pendencia
     * @param usuarioDt
     *            vo de usuario
     * @throws Exception
     */
    private void intimacaoOuCitacaoLidaAutomatica(PendenciaDt pendenciaDt, UsuarioDt usuarioDt, String comp, LogDt logDt) throws MensagemException, Exception {
        pendenciaDt.setLido(true);
        this.intimacaoOuCitacaoLidaAutomaticamente(pendenciaDt, usuarioDt, comp, logDt);
    }

    /**
     * Intimacao ou citacao lida por usuario
     * 
     * @param pendenciaDt
     *            vo de pendencia
     * @param usuarioDt
     *            vo de usuario
     * @throws Exception
     */
    private void intimacaoOuCitacaoLidaUsuario(PendenciaDt pendenciaDt, UsuarioDt usuarioDt, LogDt logDt, boolean aguardandoParecer) throws MensagemException, Exception {
       	this.intimacaoOuCitacaoLida(pendenciaDt, usuarioDt, "", logDt, aguardandoParecer);
    }

    /**
     * Intimacao ou citacao visualizada
     * 
     * @since 22/01/2009 08:30
     * @param pendenciaDt
     *            vo de pendencia
     * @param usuarioDt
     *            vo de usuario
     * @throws Exception
     */
    private void intimacaoOuCitacaoLida(PendenciaDt pendenciaDt, UsuarioDt usuarioDt, String comp, LogDt logDt, boolean aguardandoParecer) throws MensagemException, Exception {
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            obFabricaConexao.iniciarTransacao();

            obPersistencia.FinalizarPendencia(pendenciaDt.getId(), usuarioDt.getId_UsuarioServentia(), PendenciaStatusDt.ID_CUMPRIDA, false);
            
            if (aguardandoParecer){
	            // gerar pendencia filha pedido de mainifestacao
	            this.gerarPendenciaFilhaManifestacao(pendenciaDt, usuarioDt, obFabricaConexao);
            }

            this.verificarRegrasResposta(pendenciaDt, usuarioDt, null, null, comp, logDt, obFabricaConexao);

            if ( Funcoes.StringToInt(pendenciaDt.getPrazo()) > 0) {
                PrazoSuspensoNe prazoSuspensoNe = new PrazoSuspensoNe();
                ServentiaNe serventiaNe = new ServentiaNe();
                ProcessoNe processoNe = new ProcessoNe();

                ProcessoDt processoDt = processoNe.consultarId(pendenciaDt.getId_Processo());
                ServentiaDt serventiaDt = serventiaNe.consultarId(processoDt.getId_Serventia());
                
                if (this.isUtilizaPrazoCorrido(pendenciaDt.getId_Processo(), obFabricaConexao)) {
                	pendenciaDt.setDataLimite(prazoSuspensoNe.getProximaDataValidaPrazoCorrido(new Date(), Funcoes.StringToInt(pendenciaDt.getPrazo()), serventiaDt , obFabricaConexao) + " 00:00:00");
                
                } else if (this.isProcessoVaraPrecatoria(pendenciaDt.getId_Processo(), obFabricaConexao)) {
                	// Em caso de vara de precatoria deve-se testar o processo que originou a precatoria
                	if (this.isUtilizaPrazoCorrido(processoDt.getId_ProcessoPrincipal(), obFabricaConexao)) {
                		pendenciaDt.setDataLimite(prazoSuspensoNe.getProximaDataValidaPrazoCorrido(new Date(), Funcoes.StringToInt(pendenciaDt.getPrazo()), serventiaDt , obFabricaConexao) + " 00:00:00");
                	} else {
                		pendenciaDt.setDataLimite(prazoSuspensoNe.getProximaDataValidaNovoCPC(new Date(), Funcoes.StringToInt(pendenciaDt.getPrazo()), serventiaDt , obFabricaConexao) + " 00:00:00");
                	}
                
                } else {
                	pendenciaDt.setDataLimite(prazoSuspensoNe.getProximaDataValidaNovoCPC(new Date(), Funcoes.StringToInt(pendenciaDt.getPrazo()), serventiaDt , obFabricaConexao) + " 00:00:00");
                }

                obPersistencia.alterarLimite(pendenciaDt);
                obPersistencia.retirarVisto(pendenciaDt);

                // Limpa dados do objeto
                pendenciaDt.setDataVisto("");
            } else {
                // Se n�o possuir prazo limpa data limite
                pendenciaDt.setDataLimite("");
                obPersistencia.alterarLimite(pendenciaDt);
                obPersistencia.moverPendencia(pendenciaDt.getId());
            }

            obLog.salvar(logDt, obFabricaConexao);

            obFabricaConexao.finalizarTransacao();
        } catch (Exception e) {
            obFabricaConexao.cancelarTransacao();
            throw e;
        } finally {
            obFabricaConexao.fecharConexao();
        }

    }
    
    /**
     * Leitura de Intimacao automaticamente pelo sistema
     * 
     * @param pendenciaDt
     *            vo de pendencia
     * @param usuarioDt
     *            vo de usuario
     * @throws Exception
     */
    private void intimacaoOuCitacaoLidaAutomaticamente(PendenciaDt pendenciaDt, UsuarioDt usuarioDt, String comp, LogDt logDt) throws MensagemException, Exception {
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            obFabricaConexao.iniciarTransacao();

            obPersistencia.FinalizarPendencia(pendenciaDt.getId(), usuarioDt.getId_UsuarioServentia(), PendenciaStatusDt.ID_CUMPRIDA, true);

            this.verificarRegrasResposta(pendenciaDt, usuarioDt, null, null, comp, logDt, obFabricaConexao);

            if ( Funcoes.StringToInt(pendenciaDt.getPrazo()) > 0) {
                PrazoSuspensoNe prazoSuspensoNe = new PrazoSuspensoNe();
                ServentiaNe serventiaNe = new ServentiaNe();
                ProcessoNe processoNe = new ProcessoNe();

                ProcessoDt processoDt = processoNe.consultarId(pendenciaDt.getId_Processo());
                ServentiaDt serventiaDt = serventiaNe.consultarId(processoDt.getId_Serventia());

                if (this.isUtilizaPrazoCorrido(pendenciaDt.getId_Processo(), obFabricaConexao)) {
                	pendenciaDt.setDataLimite(prazoSuspensoNe.getProximaDataValidaPrazoCorrido(new Date(), Funcoes.StringToInt(pendenciaDt.getPrazo()), serventiaDt , obFabricaConexao) + " 00:00:00");
                
                } else if (this.isProcessoVaraPrecatoria(pendenciaDt.getId_Processo(), obFabricaConexao)) {
                	// Em caso de vara de precatoria deve-se testar o processo que originou a precatoria
              	  	if (this.isUtilizaPrazoCorrido(processoDt.getId_ProcessoPrincipal(), obFabricaConexao))
              	  		pendenciaDt.setDataLimite(prazoSuspensoNe.getProximaDataValidaPrazoCorrido(new Date(), Funcoes.StringToInt(pendenciaDt.getPrazo()), serventiaDt , obFabricaConexao) + " 00:00:00");
              	  	else
              	  		pendenciaDt.setDataLimite(prazoSuspensoNe.getProximaDataValidaNovoCPC(new Date(), Funcoes.StringToInt(pendenciaDt.getPrazo()), serventiaDt , obFabricaConexao) + " 00:00:00");
              
                } else
                	pendenciaDt.setDataLimite(prazoSuspensoNe.getProximaDataValidaNovoCPC(new Date(), Funcoes.StringToInt(pendenciaDt.getPrazo()), serventiaDt , obFabricaConexao) + " 00:00:00");

                obPersistencia.alterarLimite(pendenciaDt);
                obPersistencia.retirarVisto(pendenciaDt);

                // Limpa dados do objeto
                pendenciaDt.setDataVisto("");
            } else {
                // Se n�o possuir prazo limpa data limite
                pendenciaDt.setDataLimite("");
                obPersistencia.alterarLimite(pendenciaDt);
            }

            obLog.salvar(logDt, obFabricaConexao);

            obFabricaConexao.finalizarTransacao();
        } catch (Exception e) {
             obFabricaConexao.cancelarTransacao();
             throw e;
        } finally {
            obFabricaConexao.fecharConexao();
        }

    }

    /**
     * Verifica o status que uma pend�ncia aguardando retorno deve receber.
     * 
     * @param pendenciaDt
     * @return int pendenciaStatusCodigo, status da pend�ncia
     */
    public int getPendenciaStatusCodigo(PendenciaDt pendenciaDt) {
        int pendenciaStatusCodigo = 0;

        if (pendenciaDt.getStatusPendenciaRetorno().equalsIgnoreCase("Efetivada")) {
            pendenciaStatusCodigo = PendenciaStatusDt.ID_CUMPRIDA;
        } else if (pendenciaDt.getStatusPendenciaRetorno().equalsIgnoreCase("N�o Efetivada")) {
            pendenciaStatusCodigo = PendenciaStatusDt.ID_NAO_CUMPRIDA;
        } else if (pendenciaDt.getStatusPendenciaRetorno().equalsIgnoreCase("Efetivada em Parte")) {
            pendenciaStatusCodigo = PendenciaStatusDt.ID_CUMPRIDA_PARCIALMENTE;
        } else if (pendenciaDt.getStatusPendenciaRetorno().equalsIgnoreCase("Cancelada")) {
            pendenciaStatusCodigo = PendenciaStatusDt.ID_CANCELADA;
        }

        return pendenciaStatusCodigo;
    }

    /**
     * Pend�ncia aguardando retorno fechada
     * 
     * @author Leandro Bernardes
     * @since 04/03/2009 08:30
     * @param pendenciaDt
     *            vo de pendencia
     * @param usuarioDt
     *            vo de usuario
     * @param FabricaConexao
     *            fabrica
     * @throws Exception
     */
    private void concluirAguardandoRetorno(PendenciaDt pendenciaDt, UsuarioDt usuarioDt, FabricaConexao obFabricaConexao) throws Exception {
        PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
        
        if ( Funcoes.StringToInt(pendenciaDt.getPrazo()) > 0) {
        	
        	obPersistencia.retirarVisto(pendenciaDt);
            // Limpa dados do objeto
            pendenciaDt.setDataVisto("");
        	this.responder(pendenciaDt, usuarioDt, this.getPendenciaStatusCodigo(pendenciaDt), null, null, obFabricaConexao);
            PrazoSuspensoNe prazoSuspensoNe = new PrazoSuspensoNe();
            ServentiaNe serventiaNe = new ServentiaNe();
            ProcessoNe processoNe = new ProcessoNe();

            ProcessoDt processoDt = processoNe.consultarId(pendenciaDt.getId_Processo());
            ServentiaDt serventiaDt = serventiaNe.consultarId(processoDt.getId_Serventia());

            if (this.isUtilizaPrazoCorrido(pendenciaDt.getId_Processo(), obFabricaConexao)) {
            	pendenciaDt.setDataLimite(prazoSuspensoNe.getProximaDataValidaPrazoCorrido(new Date(), Funcoes.StringToInt(pendenciaDt.getPrazo()), serventiaDt , obFabricaConexao) + " 00:00:00");
            
            } else if (this.isProcessoVaraPrecatoria(pendenciaDt.getId_Processo(), obFabricaConexao)) {
            	// Em caso de vara de precatoria deve-se testar o processo que originou a precatoria
          	  	if (this.isUtilizaPrazoCorrido(processoDt.getId_ProcessoPrincipal(), obFabricaConexao))
          	  		pendenciaDt.setDataLimite(prazoSuspensoNe.getProximaDataValidaPrazoCorrido(new Date(), Funcoes.StringToInt(pendenciaDt.getPrazo()), serventiaDt , obFabricaConexao) + " 00:00:00");
          	  	else
          	  		pendenciaDt.setDataLimite(prazoSuspensoNe.getProximaDataValidaNovoCPC(new Date(), Funcoes.StringToInt(pendenciaDt.getPrazo()), serventiaDt , obFabricaConexao) + " 00:00:00");
          
            } else
            	pendenciaDt.setDataLimite(prazoSuspensoNe.getProximaDataValidaNovoCPC(Funcoes.DataHora(pendenciaDt.getDataFim()), Funcoes.StringToInt(pendenciaDt.getPrazo()), serventiaDt , obFabricaConexao) + " 00:00:00");
            
            obPersistencia.alterarLimite(pendenciaDt);
            
        } else{
        	this.responder(pendenciaDt, usuarioDt, this.getPendenciaStatusCodigo(pendenciaDt), null, null, obFabricaConexao);
        }
    }
    
    // mrbatista - 02/10/2019 14:42 - Refatorar m�todo
	public void gerarPendenciaSustentacaoOralDeferida(String idPendencia, UsuarioDt usuario) throws Exception {
		AudienciaProcessoPendenciaNe audienciaProcessoPendenciaNe = new AudienciaProcessoPendenciaNe();
		PendenciaDt pendenciaDt = new PendenciaDt();
		ProcessoDt processoDt = new ProcessoDt();
		//lrcampos 12/07/2019 * Incluindo bloco de c�digo para alterar a pendencia na tabela de Sustentaao_oral
		SustentacaoOralNe sustentacaoOralNe = new SustentacaoOralNe();
		PendenciaDt pendenciaDeferida;
		PendenciaDt pendencia;
		List<SustentacaoOralDt> listaPedidosSustentacaoOralDef = new ArrayList<SustentacaoOralDt>();
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			SustentacaoOralDt sustentacaoOralDt = sustentacaoOralNe.consultarSustentacaoOralAbertaIdPendencia(idPendencia, obFabricaConexao);
			listaPedidosSustentacaoOralDef = sustentacaoOralNe.consultarSustentacaoOralIdAudiProc(sustentacaoOralDt.getAudienciaProcessoDt().getId(), obFabricaConexao);
			obFabricaConexao.iniciarTransacao();
			if (listaPedidosSustentacaoOralDef != null && listaPedidosSustentacaoOralDef.size() > 0){
				for (SustentacaoOralDt sustentOralDt : listaPedidosSustentacaoOralDef) {	
					pendenciaDt = consultarId(sustentOralDt.getPendenciaDt().getId());
					processoDt = new ProcessoNe().consultarId(pendenciaDt.getId_Processo());
					
					pendenciaDeferida = gerarPendenciaPedidoSustentacaoOralDeferido(pendenciaDt.getId_UsuarioCadastrador(), null, usuario, pendenciaDt.getId_Processo(), obFabricaConexao);
					
					//mrbatista 02/10/2019 14:41  - gravando audi_proc_pende na gera��o do pedido de S.O., para que a pend�ncia possa ser rastreada .
					if(pendenciaDeferida != null)
						audienciaProcessoPendenciaNe.salvar(pendenciaDeferida.getId(), sustentOralDt.getAudienciaProcessoDt().getId(), obFabricaConexao);
					
					pendencia = gerarPendenciaPedidoSustentacaoOralDeferido(null, processoDt.getId_Serventia(), usuario, pendenciaDt.getId_Processo(), obFabricaConexao);
					
					//mrbatista 02/10/2019 14:41 - gravando audi_proc_pende na gera��o do pedido de S.O., para que a pend�ncia possa ser rastreada .
					if(pendencia != null)
						audienciaProcessoPendenciaNe.salvar(pendencia.getId(), sustentOralDt.getAudienciaProcessoDt().getId(), obFabricaConexao);
			
			
					finalizarPendenciaTomarConhecimento(pendenciaDt.getId(), usuario);
					sustentOralDt.setPendenciaDt(pendenciaDeferida);
					sustentacaoOralNe.alterarStatusSustentacaoOral(sustentOralDt, obFabricaConexao);
				}
			}

			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}

	}
    /**
     * Gerar pendencia de aguardando resposta
     * 
     * @author Ronneesley Moura Teles
     * @since 17/12/2008 11:49
     * @param pendenciaDt
     *            vo de pendencia
     * @param arquivos
     *            lista de arquivos
     * @param usuarioDt
     *            vo de usuario, usuario que realiza a solicitacao
     * @param fabConexao
     *            fabrica para poder continuar uma trasacao
     * @throws Exception
     */
    public void gerarPendenciaAguardandoRetorno(PendenciaDt pendenciaDt, ProcessoDt processoDt, List arquivos, UsuarioDt usuarioDt, FabricaConexao fabConexao) throws Exception {
        
        FabricaConexao obFabricaConexao = null;
        
        try {
            // Verifica se a conexao sera criada internamente
            if (fabConexao == null) {
                obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
                obFabricaConexao.iniciarTransacao();
            } else {
                // Caso da conexao criada em um nivel superior
                obFabricaConexao = fabConexao;
            }

            PendenciaDt pendenciaRetornoDt = pendenciaDt.criarFilha();
            PendenciaResponsavelDt responsavel = new PendenciaResponsavelDt();
            
            processoDt =  new ProcessoNe().consultarId(processoDt.getId(), fabConexao);
            
            String id_serventia = usuarioDt.getId_Serventia();
        	if ( (usuarioDt.isGabinete() || usuarioDt.isGabineteUPJ()) && !processoDt.isSigiloso()) {
        		id_serventia = processoDt.getId_Serventia();
        	}
            
        	responsavel.setId_Serventia(id_serventia);
            pendenciaRetornoDt.addResponsavel(responsavel);

            // Limpa os dados e atribui o novo tipo
            pendenciaRetornoDt.setId_PendenciaStatus("null");
            pendenciaRetornoDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_RETORNO));

            pendenciaRetornoDt.setId_UsuarioLog(usuarioDt.getId());
            pendenciaRetornoDt.setIpComputadorLog(usuarioDt.getIpComputadorLog());

            this.gerarPendencia(pendenciaRetornoDt, obFabricaConexao);

            // vincula arquivos da pendencia pai a pendencia filha no caso de
            // pendencia aguardando retorno.
            // this.vincularArquivosResposta(pendenciaDt, pendenciaRetornoDt,
            // obFabricaConexao);

            if (fabConexao == null) obFabricaConexao.finalizarTransacao();
        } catch (Exception e) {
            // Se a conexao foi criada dentro do metodo, entao cancela a conexao
            if (fabConexao == null) obFabricaConexao.cancelarTransacao();

            throw e;
        } finally {
            // Se a conexao foi criada dentro do metodo, entao finaliza a
            // conexao
            if (fabConexao == null) obFabricaConexao.fecharConexao();
        }
    }
    
    /**
     * Gerar pendencia de aguardando resposta
     * 
     * @author Ronneesley Moura Teles
     * @since 17/12/2008 11:49
     * @param pendenciaDt
     *            vo de pendencia
     * @param arquivos
     *            lista de arquivos
     * @param usuarioDt
     *            vo de usuario, usuario que realiza a solicitacao
     * @param fabConexao
     *            fabrica para poder continuar uma trasacao
     * @throws Exception
     */
    public String gerarPendenciaAguardandoRetornoCentralMandados(PendenciaDt pendenciaDt, List arquivos, UsuarioDt usuarioDt, FabricaConexao fabConexao) throws Exception {
        
        FabricaConexao obFabricaConexao = null;
        String idMandadoJudicial = "";
        
        try {
            // Verifica se a conexao sera criada internamente
            if (fabConexao == null) {
                obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
                obFabricaConexao.iniciarTransacao();
            } else {
                // Caso da conexao criada em um nivel superior
                obFabricaConexao = fabConexao;
            }
            
            EscalaNe escala = new EscalaNe();
            String idAreaProcesso = new ProcessoNe().consultarIdArea(pendenciaDt.getId_Processo(), obFabricaConexao);
            
            String[] oficial;
            
            if(pendenciaDt.getId_OficialAdhoc() == null || pendenciaDt.getId_OficialAdhoc().isEmpty()) {
            	//OFICIAL COMUM
            	oficial = escala.distribuicaoEscala(pendenciaDt.getId_Bairro(), pendenciaDt.getId_ServentiaFinalizador(), pendenciaDt.getId_MandadoTipo(), idAreaProcesso, pendenciaDt.getAssistencia()); //(ID_BAIRRO, ID_SERVENTIACENTRALMANDADO, ID_MANDADOTIPO)
            } else {
            	//OFICIAL AD HOC
            	oficial = new ServentiaCargoEscalaNe().retornarOficialAdhocEscolhido(pendenciaDt.getId_OficialAdhoc(), obFabricaConexao);
            }
            
            
            PendenciaDt pendenciaRetornoDt = pendenciaDt.criarFilha();
            PendenciaResponsavelDt responsavel = new PendenciaResponsavelDt();
            if(oficial[1] != null && !oficial[1].equalsIgnoreCase("")) {
            	responsavel.setId_ServentiaCargo(oficial[1]); //ID_SERV_CARGO
            } else throw new MensagemException (" <{ N�o foi possivel encontrar oficial para esta regi�o. }> Local Exception: " + this.getClass().getName() + ".gerarPendenciaAguardandoRetornoCentralMandados(): ");
            
            pendenciaRetornoDt.addResponsavel(responsavel);

            // Limpa os dados e atribui o novo tipo
            pendenciaRetornoDt.setId_PendenciaStatus("null");
            pendenciaRetornoDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_RETORNO));

            pendenciaRetornoDt.setId_UsuarioLog(usuarioDt.getId());
            pendenciaRetornoDt.setIpComputadorLog(usuarioDt.getIpComputadorLog());
            
            // Reservar a pendencia - define a data dois meses pra frente para manter a pendencia reservada
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, 2);
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
            pendenciaRetornoDt.setDataTemp(df.format(cal.getTime()));
            
            if(oficial[0] != null && !oficial[0].equalsIgnoreCase("")) {
            	pendenciaRetornoDt.setId_UsuarioFinalizador(oficial[0]); //ID_USU_SERV
            	pendenciaRetornoDt.setId_UsuarioServentia1(oficial[0]);
            }

            ZonaBairroRegiaoNe zoNe = new ZonaBairroRegiaoNe();
            ZonaBairroRegiaoDt zoDt = zoNe.consultarIdBairro(pendenciaDt.getId_Bairro());
            pendenciaRetornoDt.setId_Zona(zoDt.getId_Zona());
            pendenciaRetornoDt.setId_Regiao(zoDt.getId_Regiao());
            pendenciaRetornoDt.setId_Escala(oficial[3]);
            pendenciaRetornoDt.setAssistencia(pendenciaDt.getAssistencia());
            ProcessoNe processoNe = new ProcessoNe();
            pendenciaRetornoDt.setId_Area(processoNe.consultarIdArea(pendenciaDt.getId_Processo(), obFabricaConexao));
            MandadoJudicialStatusNe judicialStatusNe = new MandadoJudicialStatusNe();
            
            pendenciaRetornoDt.setId_MandadoJudicialStatus(judicialStatusNe.consultarCodigo(String.valueOf(MandadoJudicialStatusDt.DISTRIBUIDO), obFabricaConexao));
            
            pendenciaRetornoDt.setDataDistribuicao(processoNe.consultarDataDistribuicao(pendenciaDt.getId_Processo(), obFabricaConexao));
            
            pendenciaRetornoDt.setIdModelo(pendenciaDt.getIdModelo());
            
            this.gerarPendencia(pendenciaRetornoDt, obFabricaConexao);
            
            idMandadoJudicial = this.preencherMandado(pendenciaDt.getNumeroReservadoMandadoExpedir(), pendenciaRetornoDt, oficial[2], obFabricaConexao);

            if (fabConexao == null) obFabricaConexao.finalizarTransacao();
            
            return idMandadoJudicial;
            
        } catch (Exception e) {
            // Se a conexao foi criada dentro do metodo, entao cancela a conexao
            if (fabConexao == null) obFabricaConexao.cancelarTransacao();

            throw e;
        } finally {
            // Se a conexao foi criada dentro do metodo, entao finaliza a conexao
            if (fabConexao == null) obFabricaConexao.fecharConexao();
        }
    }
    
    private String preencherMandado(String numeroReservadoMandadoExpedir, PendenciaDt pendenciaRetornoDt, String id_ServentiaCargoEscala, FabricaConexao obFabricaConexao) throws Exception {

    	MandadoJudicialNe mandadoJudicialNe = new MandadoJudicialNe();
		MandadoJudicialDt mandadoJudicialDt = new MandadoJudicialDt();
		
		if(Funcoes.StringToInt(numeroReservadoMandadoExpedir) == 0){
			throw new MensagemException("Erro: N�o foi poss�vel identificar o n�mero de mandado reservado para a expedi��o.");
		}
		else {
			mandadoJudicialDt.setId(numeroReservadoMandadoExpedir);
		}
		
		mandadoJudicialDt.setId_MandadoTipo(pendenciaRetornoDt.getId_MandadoTipo());
		mandadoJudicialDt.setId_ProcessoParte(pendenciaRetornoDt.getId_ProcessoParte());
		mandadoJudicialDt.setId_EnderecoParte(pendenciaRetornoDt.getId_EnderecoParte());
		mandadoJudicialDt.setId_Pendencia(pendenciaRetornoDt.getId());
		mandadoJudicialDt.setId_Area(pendenciaRetornoDt.getId_Area());
		mandadoJudicialDt.setId_Zona(pendenciaRetornoDt.getId_Zona());
		mandadoJudicialDt.setId_Regiao(pendenciaRetornoDt.getId_Regiao());
		mandadoJudicialDt.setId_Bairro(pendenciaRetornoDt.getId_Bairro());
		mandadoJudicialDt.setId_Escala(pendenciaRetornoDt.getId_Escala());
		mandadoJudicialDt.setValor(pendenciaRetornoDt.getValor());
		mandadoJudicialDt.setAssistencia(pendenciaRetornoDt.getAssistencia());
		mandadoJudicialDt.setCodigoTemp(pendenciaRetornoDt.getCodigoTemp());
		mandadoJudicialDt.setId_MandadoJudicialStatus(pendenciaRetornoDt.getId_MandadoJudicialStatus());
		mandadoJudicialDt.setId_UsuarioServentia_1(pendenciaRetornoDt.getId_UsuarioServentia1());
		mandadoJudicialDt.setId_UsuarioServentia_1(pendenciaRetornoDt.getId_UsuarioFinalizador());
		mandadoJudicialDt.setDataDistribuicao(pendenciaRetornoDt.getDataDistribuicao());
//		mandadoJudicialDt.setDataRetorno("");
//		mandadoJudicialDt.setLocomocoesFrutiferas("");
//		mandadoJudicialDt.setLocomocoesInfrutiferas("");
//		mandadoJudicialDt.setLocomocaoHoraMarcada("");
		mandadoJudicialDt.setDataLimite(pendenciaRetornoDt.getDataLimite());
		mandadoJudicialDt.setId_ServentiaCargoEscala(id_ServentiaCargoEscala);
		mandadoJudicialDt.setIpComputadorLog(pendenciaRetornoDt.getIpComputadorLog());
		mandadoJudicialDt.setId_UsuarioLog(pendenciaRetornoDt.getId_UsuarioLog());
		mandadoJudicialDt.setIdMandJudPagamentoStatus(MandadoJudicialDt.ID_PAGAMENTO_PENDENTE);
		mandadoJudicialDt.setId_Modelo(pendenciaRetornoDt.getIdModelo());
		
		mandadoJudicialNe.inserir(mandadoJudicialDt, obFabricaConexao);
		
		return mandadoJudicialDt.getId();
	}

    /**
     * Responde uma pend�ncia (aguardando retorno) com arquivos
     * 
     * @author Leandro Bernardes
     * @since 04/03/2009 14:15
     * @param pendenciaDt
     *            vo para pendencias
     * @param usuarioDt
     *            usuario
     * @param arquivos
     *            vinculado a pend�ncia
     * @throws Exception
     */
    public void concluirAguardandoRetorno(PendenciaDt pendenciaDt, UsuarioDt usuarioDt, List arquivos) throws Exception {

        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            obFabricaConexao.iniciarTransacao();

            // Se tem arquivos
            if (arquivos != null && arquivos.size() > 0) {
                this.inserirArquivos(pendenciaDt, arquivos, true, usuarioDt, obFabricaConexao);
            }

            this.concluirAguardandoRetorno(pendenciaDt, usuarioDt, obFabricaConexao);
            
            if(usuarioDt.getCargoTipoCodigo().equalsIgnoreCase(String.valueOf(CargoTipoDt.OFICIAL_JUSTICA))) {
            	
            	LogDt logDt = new LogDt(usuarioDt.getId(), usuarioDt.getIpComputadorLog());
            	String prioridadeProcesso = new ProcessoNe().getPrioridade(pendenciaDt.getId_Processo(), obFabricaConexao);
            	
            	// Se for um oficial resolvendo uma pend�ncia do tipo "Mandado"
            	if(Funcoes.StringToInt(pendenciaDt.getPendenciaTipoCodigo()) == PendenciaTipoDt.MANDADO) {
            		//Atualizar o mandado no banco, finalizando e defininido o status e o oficial companheiro
            		MandadoJudicialNe mandadoJudicialNe = new MandadoJudicialNe();
            		mandadoJudicialNe.finalizaMandadoAbertoIdPend(pendenciaDt.getId(), usuarioDt.getId_UsuarioServentia(), pendenciaDt.getId_UsuarioServentia2(), pendenciaDt.getStatusPendenciaRetorno(), obFabricaConexao);
            		PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
            		pendenciaResponsavelNe.alterarResponsavelMandadoResolvido(pendenciaDt.getId(), usuarioDt.getId_ServentiaCargo(), obFabricaConexao);
            	}
            	
            	gerarPendenciaVerificarProcesso(pendenciaDt.getId_Processo(), UsuarioServentiaDt.SistemaProjudi, pendenciaDt.getId_ServentiaCadastrador(), pendenciaDt.getId_Movimentacao(), arquivos, null, logDt, obFabricaConexao, prioridadeProcesso);
            	
            	MandadoJudicialNe mandadoJudicialNe = new MandadoJudicialNe();
            	mandadoJudicialNe.salvarDataRetorno(pendenciaDt.getDataFim(), pendenciaDt.getId(), obFabricaConexao);
            	//******************************************************************
//            	fecharLocomocao(pendenciaDt.getId());
            	//TODO Helleno, verificar!
            	//******************************************************************
            }

            obFabricaConexao.finalizarTransacao();
        } catch (Exception e) {
            obFabricaConexao.cancelarTransacao();
            throw e;
        } finally {
            obFabricaConexao.fecharConexao();
        }
    }

    /**
     * Retorna se o documento e para ser visto ou se e para ser respondido
     * 
     * @author Ronneesley Moura Teles
     * @since 27/01/2009 10:10
     * @param pendenciaDt
     *            vo da pendencia
     * @throws Exception
     */
    private boolean documentoASerVisto(PendenciaDt pendenciaDt) throws Exception {
        if (pendenciaDt.getPendenciaStatusCodigo().equals(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_VISTO))) {
            return true;
        }

        return false;
    }

    /**
     * Verifica as regras de negocio para a resposta de pendencia
     * 
     * @author Ronneesley Moura Teles / Leandro Bernardes
     * @since 09/12/2008 10:36
     * @param pendenciaDt
     *            vo para pendencias
     * @param usuarioDt
     *            usuario
     * @param obFabricaConexao
     *            fabrica de conexao
     * @throws Exception
     */
    public void verificarRegrasResposta(PendenciaDt pendenciaDt, UsuarioDt usuarioDt, ServentiaDt serventiaDt, ComarcaDt comarcaDt, String complemento, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
        // Somente em casos de pendencia de processo
        if (pendenciaDt.isPendenciaDeProcesso()) {
            // Cria um objeto de regras
            MovimentacaoNe movimentacaoNe = new MovimentacaoNe();

            // Configura objeto de processo
            ProcessoDt processoDt = new ProcessoDt();
            processoDt.setId_Processo(pendenciaDt.getId_Processo());
            processoDt.setProcessoNumero(pendenciaDt.getProcessoNumero());

            // preeche complementa para intima��o e cita��o
            String comp = "";
            if (complemento != null && complemento.equals("")) {
            	String responsavel = "";
            	String stComplemento = "";
            	 if (this.documentoASerVisto(pendenciaDt)) {
            		 responsavel = usuarioDt.getNome();
            		 stComplemento = "Por " + obtenhaComplementoLeituraPendenciaIntimacao(pendenciaDt) + " " + responsavel + " (Referente � Mov. " + pendenciaDt.getMovimentacao() + ")";
            		 if (stComplemento.length()>255)	{            	
     		           	if (responsavel.length() > stComplemento.length()-255) {
     		           		stComplemento = "Por " + obtenhaComplementoLeituraPendenciaIntimacao(pendenciaDt) + " " + responsavel.substring(0,(responsavel.length() -  (stComplemento.length()-255))) + " (Referente � Mov. " + pendenciaDt.getMovimentacao() + ")";
     		           	}
     		         }
            	 } else {
            		 responsavel = pendenciaDt.getNomeParte();
            		 stComplemento = "Para " + obtenhaComplementoLeituraPendenciaIntimacao(pendenciaDt) + " "  + responsavel;
            		 if (stComplemento.length()>255)	{            	
     		           	if (responsavel.length() > stComplemento.length()-255) {
     		           		stComplemento = "Para " + obtenhaComplementoLeituraPendenciaIntimacao(pendenciaDt) + " " + responsavel.substring(0,(responsavel.length() -  (stComplemento.length()-255)));
     		           	}
     		         }
            	 }
		         comp = stComplemento;
            } else {
            	comp = complemento;
            }

            // Se era uma pendencia de aguardando retorno
            if (pendenciaDt.getPendenciaStatusCodigo().equals(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_RETORNO))) {
                List arqs = this.consultarArquivosRespostaAssinados(pendenciaDt, true, obFabricaConexao);

                if (pendenciaDt.getStatusPendenciaRetorno().equalsIgnoreCase("Efetivada")) {
                    this.gerarMovimentacaoLida(movimentacaoNe, processoDt, pendenciaDt, usuarioDt, arqs, logDt, obFabricaConexao);
                } else if (pendenciaDt.getStatusPendenciaRetorno().equalsIgnoreCase("N�o Efetivada")) {
                    this.gerarMovimentacaoNaoLida(movimentacaoNe, processoDt, pendenciaDt, usuarioDt, arqs, logDt, obFabricaConexao);
                } else if (pendenciaDt.getStatusPendenciaRetorno().equalsIgnoreCase("Efetivada em Parte")) {
                    this.gerarMovimentacaoEfetivadaParcialmente(movimentacaoNe, processoDt, pendenciaDt, usuarioDt, arqs, logDt, obFabricaConexao);
                } else if (pendenciaDt.getStatusPendenciaRetorno().equalsIgnoreCase("Cancelada")) {
                    // Cancelada n�o gera movimenta��o ex: parte desistiu da
                    // a��o, a pend�ncia � cancelada, a n�o ser alvarar
                    this.gerarMovimentacaoCancelada(movimentacaoNe, processoDt, pendenciaDt, usuarioDt, arqs, logDt, obFabricaConexao);
                }

            } else { // Se nao for uma pendencia de aguardando retorno
                // Verifica o tipo de pendencia
            	int GrupoCodigo = Funcoes.StringToInt(usuarioDt.getGrupoCodigo());
                switch (pendenciaDt.getPendenciaTipoCodigoToInt()) {
                case PendenciaTipoDt.ALVARA:
                    if (this.pendenciaTipoExpedir(pendenciaDt)) {
                        List arqs = this.consultarArquivosRespostaAssinados(pendenciaDt, true, obFabricaConexao);

                        if (serventiaDt.getId() != null && !serventiaDt.getId().equals("")) {
                            // gerar alvara para serventia on-line
                            movimentacaoNe.gerarMovimentacaoAlvaraExpedido(processoDt.getId(), processoDt.getProcessoNumero(), arqs, usuarioDt, "Para " + serventiaDt.getServentia(), logDt, obFabricaConexao);
                            this.gerarPendenciaFilhaExpedir(pendenciaDt, serventiaDt, usuarioDt, obFabricaConexao);
                        } else {
                            // gerar alvara para serventia off-line
                            movimentacaoNe.gerarMovimentacaoAlvaraExpedido(processoDt.getId(), processoDt.getProcessoNumero(), arqs, usuarioDt, "", logDt, obFabricaConexao);
                            this.gerarPendenciaAguardandoRetorno(pendenciaDt, processoDt, arqs, usuarioDt, obFabricaConexao);
                        }
                    }

                    break;
                case PendenciaTipoDt.ALVARA_SOLTURA:
                    if (this.pendenciaTipoExpedir(pendenciaDt)) {
                        List arqs = this.consultarArquivosRespostaAssinados(pendenciaDt, true, obFabricaConexao);

                        if (serventiaDt.getId() != null && !serventiaDt.getId().equals("")) {
                            // gerar alvara para serventia on-line
                            movimentacaoNe.gerarMovimentacaoAlvaraSolturaExpedido(processoDt.getId(), processoDt.getProcessoNumero(), arqs, usuarioDt, "Para " + serventiaDt.getServentia(), logDt, obFabricaConexao);
                            this.gerarPendenciaFilhaExpedir(pendenciaDt, serventiaDt, usuarioDt, obFabricaConexao);
                        } else {
                            // gerar alvara para serventia off-line
                            movimentacaoNe.gerarMovimentacaoAlvaraSolturaExpedido(processoDt.getId(), processoDt.getProcessoNumero(), arqs, usuarioDt, "", logDt, obFabricaConexao);
                            this.gerarPendenciaAguardandoRetorno(pendenciaDt, processoDt, arqs, usuarioDt, obFabricaConexao);
                        }
                    }

                    break;
                case PendenciaTipoDt.INTIMACAO:
                case PendenciaTipoDt.INTIMACAO_AUDIENCIA:
                    if (this.documentoASerVisto(pendenciaDt)) {
                        // parte ou advogado ler intimacao (on-line)
                        movimentacaoNe.gerarMovimentacaoIntimacaoEfetivada(processoDt.getId(), processoDt.getProcessoNumero(), null, usuarioDt, comp, logDt, obFabricaConexao);
                    } else if ( GrupoCodigo!= GrupoDt.ADVOGADO_PARTICULAR 
        					&& GrupoCodigo != GrupoDt.MINISTERIO_PUBLICO && GrupoCodigo != GrupoDt.MP_TCE){

                        // cartorio expede intimacao para parte (off-line)
                        List arqs = this.consultarArquivosRespostaAssinados(pendenciaDt, true, obFabricaConexao);

                        movimentacaoNe.gerarMovimentacaoIntimacaoExpedida(processoDt.getId(), processoDt.getProcessoNumero(), arqs, usuarioDt, comp, logDt, obFabricaConexao);

                        this.gerarIntimacaoCitacaoEfetivada(pendenciaDt, processoDt,  arqs, usuarioDt, obFabricaConexao);
                    }

                    break;
                case PendenciaTipoDt.CARTA_CITACAO:
                case PendenciaTipoDt.CARTA_CITACAO_AUDIENCIA:
                    if (this.documentoASerVisto(pendenciaDt)) {
                        // parte ou advogado ler citacao (on-line)
                        movimentacaoNe.gerarMovimentacaoCitacaoEfetivada(processoDt.getId(), processoDt.getProcessoNumero(), null, usuarioDt, comp, logDt, obFabricaConexao);
                    } else if ( GrupoCodigo!= GrupoDt.ADVOGADO_PARTICULAR 
            					&& GrupoCodigo != GrupoDt.MINISTERIO_PUBLICO && GrupoCodigo != GrupoDt.MP_TCE){

                        // cartorio expede carta de citacao para parte (off-line)
                        List arqs = this.consultarArquivosRespostaAssinados(pendenciaDt, true, obFabricaConexao);

                        movimentacaoNe.gerarMovimentacaoCitacaoExpedida(processoDt.getId(), processoDt.getProcessoNumero(), arqs, usuarioDt, comp, logDt, obFabricaConexao);

                        this.gerarIntimacaoCitacaoEfetivada(pendenciaDt, processoDt, arqs, usuarioDt, obFabricaConexao);
                    }

                    break;
                case PendenciaTipoDt.MANDADO:
                    if (this.pendenciaTipoExpedir(pendenciaDt)) {
                    	                    	
                        List arqs = this.consultarArquivosRespostaAssinados(pendenciaDt, true, obFabricaConexao);
                        String idMandadoJudicial = "";
                        // SE ESTIVER EXPEDIENDO PARA UMA CENTRAL DE MANDADOS
                        if (serventiaDt.getId() != null && !serventiaDt.getId().equals("") && new PendenciaNe().isCentralMandados(serventiaDt.getId(), obFabricaConexao) && this.temCentralProjudiImplantada(serventiaDt.getId())) {
                        	
                        	//******************************************************************
                        	boolean isAssistencia;
                        	if( new MandadoTipoNe().isTipoExecucaoFiscal(pendenciaDt.getId_MandadoTipo(), obFabricaConexao) ) {
                        		// De acordo com o BO 2020/10535, reuni�o realizad ano dia 27/11/2020 e e-mail recebido da Maria de F�tima no dia 30/11/2020,
                        		// os mandados do tipo Execu��o Fiscal ser�o considerados com custas independente do tipo de custa cadastrado no processo.
                        		isAssistencia = false;
                        	} else {
                        		// Se n�o for um mandado de Execu��o Fiscal, verifica o tipo de custa do processo.
                        		isAssistencia = new ProcessoNe().isAssistencia(pendenciaDt.getId_Processo(), obFabricaConexao);
                        	}
                        	
                        	if( isAssistencia ) {
                        		pendenciaDt.setAssistencia(String.valueOf(MandadoJudicialDt.SIM_ASSISTENCIA));
                        	} else {
                        		pendenciaDt.setAssistencia(String.valueOf(MandadoJudicialDt.NAO_ASSISTENCIA));
                        	}
                        	//******************************************************************
                        	
	                        // gerar mandado para serventia central de mandados
	                        pendenciaDt.setId_ServentiaFinalizador(serventiaDt.getId()); //ID_SERVENTIA central de mandados
	                        idMandadoJudicial = this.gerarPendenciaAguardandoRetornoCentralMandados(pendenciaDt, arqs, usuarioDt, obFabricaConexao);
	                        movimentacaoNe.gerarMovimentacaoMandadoExpedido(processoDt.getId(), processoDt.getProcessoNumero(), arqs, usuarioDt, "Para " + serventiaDt.getServentia() + " (Mandado n� " + idMandadoJudicial + " / Para: " + pendenciaDt.getNomeParte() +")", logDt, obFabricaConexao);
	                        
	                        //******************************************************************
	                        // Se o processo n�o for assist�ncia ou for do tipo Execu��o Fiscal, reserva locomo��o.
	                        if(idMandadoJudicial != null && !idMandadoJudicial.isEmpty() && !isAssistencia) 
	                        	reservarLocomocao(pendenciaDt.getId_Processo(), idMandadoJudicial, pendenciaDt.getId_Bairro(), pendenciaDt.getQtdLocomocoesMandado(), usuarioDt.getId(), null, obFabricaConexao);
	                        //******************************************************************
	                        
                        } else if (serventiaDt.getId() != null && !serventiaDt.getId().equals("")) {
                            // gerar mandado para serventia on-line
                            movimentacaoNe.gerarMovimentacaoMandadoExpedido(processoDt.getId(), processoDt.getProcessoNumero(), arqs, usuarioDt, "Para " + serventiaDt.getServentia(), logDt, obFabricaConexao);
                            this.gerarPendenciaFilhaExpedir(pendenciaDt, serventiaDt, usuarioDt, obFabricaConexao);
                        } else {
                            // gerar mandado off-line
                            movimentacaoNe.gerarMovimentacaoMandadoExpedido(processoDt.getId(), processoDt.getProcessoNumero(), arqs, usuarioDt, "Para " + pendenciaDt.getNomeParte(), logDt, obFabricaConexao);
                            this.gerarPendenciaAguardandoRetorno(pendenciaDt, processoDt, arqs, usuarioDt, obFabricaConexao);
                        }
                        
                       //Limpa o n�mero de mandado usado na expedi��o para garantir
                       //que n�o ser� usado novamente em um pr�ximo mandado.
                       pendenciaDt.setNumeroReservadoMandadoExpedir(null);
                    }
                    
                    break;
                case PendenciaTipoDt.CARTA_PRECATORIA:
                	String id_AreaDistribuicao = "";
           		 	String id_Serventia = "";
                	
        		   if (this.pendenciaTipoExpedir(pendenciaDt)) {
            	      ProcessoDt processo = new ProcessoNe().consultarIdSimples(pendenciaDt.getId_Processo());
                   	// serventia que � a de origem do processo
                       List arqs = this.consultarArquivosRespostaAssinados(pendenciaDt, true, obFabricaConexao);

                       if (comarcaDt.getId() != null && !comarcaDt.getId().equals("")) {
                           // gerar Carta Precatoria para serventia on-line
                    	   AreaDistribuicaoNe areaDistribuicaoNe = new AreaDistribuicaoNe();
                    	   //consulta area de distribuicao do subtipo carta precatoria
                    	   id_AreaDistribuicao = areaDistribuicaoNe.consultarAreaDistribuicaoPrecatoria(comarcaDt.getId());
                    	   
                    	   if (id_AreaDistribuicao == null){
                    		   //consulta a area de distribuicao do destino pelo mesmo subtipo da origem
//                    		   ProcessoDt procTempDt = new ProcessoNe().consultarId(processo.getId_Serventia());
                    		   ServentiaDt serventiaProcessoDt = new ServentiaNe().consultarId(processo.getId_Serventia());
                    		   List listAreasDistibuicao =  areaDistribuicaoNe.consultarAreasDistribuicaoServentiaSubTipo(serventiaProcessoDt.getId_ServentiaSubtipo(), comarcaDt.getId());
                    		   
                    		   if (listAreasDistibuicao==null){
                    			   int serventiaSubtipoCodigo = Funcoes.StringToInt(serventiaProcessoDt.getServentiaSubtipoCodigo());
                    			   switch (serventiaSubtipoCodigo) {
                    			   		case ServentiaSubtipoDt.CAMARA_CIVEL:
                    			   		case ServentiaSubtipoDt.SECAO_CIVEL:
                    			   			listAreasDistibuicao =  areaDistribuicaoNe.consultarAreasDistribuicaoServentiaSubTipo(String.valueOf(ServentiaSubtipoDt.VARAS_CIVEL), comarcaDt.getId());
                    			   			break;
                    			   		case ServentiaSubtipoDt.AUDITORIA_MILITAR_CIVEL:
                    			   			listAreasDistibuicao =  areaDistribuicaoNe.consultarAreasDistribuicaoServentiaSubTipo(String.valueOf(ServentiaSubtipoDt.AUDITORIA_MILITAR_CIVEL), comarcaDt.getId());
                    			   			if(listAreasDistibuicao == null){
                    			   				listAreasDistibuicao =  areaDistribuicaoNe.consultarAreasDistribuicaoServentiaSubTipo(String.valueOf(ServentiaSubtipoDt.VARAS_CIVEL), comarcaDt.getId());
                    			   			}
                    			   			break;     
                    			   		case ServentiaSubtipoDt.AUDITORIA_MILITAR_CRIMINAL:
                    			   			listAreasDistibuicao =  areaDistribuicaoNe.consultarAreasDistribuicaoServentiaSubTipo(String.valueOf(ServentiaSubtipoDt.AUDITORIA_MILITAR_CRIMINAL), comarcaDt.getId());
                    			   			if(listAreasDistibuicao == null){
                    			   				listAreasDistibuicao =  areaDistribuicaoNe.consultarAreasDistribuicaoServentiaSubTipo(String.valueOf(ServentiaSubtipoDt.VARA_CRIMINAL), comarcaDt.getId());
                    			   			}
                    			   			break;                       			   			
                    			   		case ServentiaSubtipoDt.UPJ_CRIMINAL:
                    			   			listAreasDistibuicao =  areaDistribuicaoNe.consultarAreasDistribuicaoServentiaSubTipo(String.valueOf(ServentiaSubtipoDt.UPJ_CRIMINAL), comarcaDt.getId());
                    			   			if(listAreasDistibuicao == null){
                    			   				listAreasDistibuicao =  areaDistribuicaoNe.consultarAreasDistribuicaoServentiaSubTipo(String.valueOf(ServentiaSubtipoDt.VARA_CRIMINAL), comarcaDt.getId());
                    			   			}
                    			   			break;
                    			   		case ServentiaSubtipoDt.UPJ_VIOLENCIA_DOMESTICA:
                    			   			listAreasDistibuicao =  areaDistribuicaoNe.consultarAreasDistribuicaoServentiaSubTipo(String.valueOf(ServentiaSubtipoDt.UPJ_VIOLENCIA_DOMESTICA), comarcaDt.getId());
                    			   			if(listAreasDistibuicao == null){
                    			   				listAreasDistibuicao =  areaDistribuicaoNe.consultarAreasDistribuicaoServentiaSubTipo(String.valueOf(ServentiaSubtipoDt.VARA_CRIMINAL), comarcaDt.getId());
                    			   			}
                    			   			break;
                    			   		case ServentiaSubtipoDt.UPJ_CUSTODIA:
                    			   			listAreasDistibuicao =  areaDistribuicaoNe.consultarAreasDistribuicaoServentiaSubTipo(String.valueOf(ServentiaSubtipoDt.UPJ_CUSTODIA), comarcaDt.getId());
                    			   			if(listAreasDistibuicao == null){
                    			   				listAreasDistibuicao =  areaDistribuicaoNe.consultarAreasDistribuicaoServentiaSubTipo(String.valueOf(ServentiaSubtipoDt.VARA_CRIMINAL), comarcaDt.getId());
                    			   			}
                    			   			break;
                    			   		case ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL:
                    			   		case ServentiaSubtipoDt.JUIZADO_ESPECIAL_CRIMINAL:
                    			   		case ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL_CRIMINAL:
	                        			   if (processo.isCivel() ){
	                        				   listAreasDistibuicao =  areaDistribuicaoNe.consultarAreasDistribuicaoServentiaSubTipo(String.valueOf(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL), comarcaDt.getId());
	                        			   }else if (processo.isCriminal()){ 
	                        				   listAreasDistibuicao =  areaDistribuicaoNe.consultarAreasDistribuicaoServentiaSubTipo(String.valueOf(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CRIMINAL), comarcaDt.getId());	 		                        			   	                        			   			
                    			   			}
	                        			   if(listAreasDistibuicao == null){
	                        				   listAreasDistibuicao =  areaDistribuicaoNe.consultarAreasDistribuicaoServentiaSubTipo(String.valueOf(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL_CRIMINAL), comarcaDt.getId());
	                        			   }
	                        			   break;
                    			   		case   ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL: 
                    			   		case   ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL_EXECUACAO_FISCAL: 
                    			   		case   ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL_INTERIOR:  
                    			   		case   ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL_INTERIOR:
                    			   		case   ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL: 
                    			   		case   ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL_EXECUACAO_FISCAL: 
                    			   		case   ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL_ESTADUAL:  
                    			   			
                    			   			listAreasDistibuicao =  areaDistribuicaoNe.consultarAreasDistribuicaoServentiaSubTipo(String.valueOf(ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL_ESTADUAL), comarcaDt.getId());
                    			   			
                    			   			if (listAreasDistibuicao==null && serventiaSubtipoCodigo!=ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL){
                    			   				listAreasDistibuicao =  areaDistribuicaoNe.consultarAreasDistribuicaoServentiaSubTipo(String.valueOf(ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL), comarcaDt.getId());
                    			   			}	
                    			   		 
                    			   			if (listAreasDistibuicao==null && serventiaSubtipoCodigo!=ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL_EXECUACAO_FISCAL){
                    			   				listAreasDistibuicao =  areaDistribuicaoNe.consultarAreasDistribuicaoServentiaSubTipo(String.valueOf(ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL_EXECUACAO_FISCAL), comarcaDt.getId());
                    			   			}
                    			   			if (listAreasDistibuicao==null && serventiaSubtipoCodigo!=ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL_INTERIOR){
                    			   				listAreasDistibuicao =  areaDistribuicaoNe.consultarAreasDistribuicaoServentiaSubTipo(String.valueOf(ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL_INTERIOR), comarcaDt.getId());
                    			   			}
                    			   			if (listAreasDistibuicao==null && serventiaSubtipoCodigo!=ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL_INTERIOR){
                    			   				listAreasDistibuicao =  areaDistribuicaoNe.consultarAreasDistribuicaoServentiaSubTipo(String.valueOf(ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL_INTERIOR), comarcaDt.getId());
                    			   			}
                    			   			if (listAreasDistibuicao==null && serventiaSubtipoCodigo!=ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL_EXECUACAO_FISCAL){
                    			   				listAreasDistibuicao =  areaDistribuicaoNe.consultarAreasDistribuicaoServentiaSubTipo(String.valueOf(ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL_EXECUACAO_FISCAL), comarcaDt.getId());
                    			   			}	
                    			   			if (listAreasDistibuicao==null && serventiaSubtipoCodigo!=ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL){
                    			   				listAreasDistibuicao =  areaDistribuicaoNe.consultarAreasDistribuicaoServentiaSubTipo(String.valueOf(ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL), comarcaDt.getId());
                    			   			}
                    			   			break;
                    			   		case ServentiaSubtipoDt.FAMILIA_INTERIOR:
                    			   			//As �reas de Fam�lia foram desmembradas em Fam�lia - Capital e Fam�lia - Interior. Quando o interior for enviar pra capital, � preciso consultar separado.
                    			   			listAreasDistibuicao =  areaDistribuicaoNe.consultarAreasDistribuicaoServentiaSubTipo(String.valueOf(ServentiaSubtipoDt.FAMILIA_CAPITAL), comarcaDt.getId());
	                        			   break;
                    			   		case ServentiaSubtipoDt.UPJ_FAMILIA:
                    			   		case ServentiaSubtipoDt.FAMILIA_CAPITAL:
                    			   		case ServentiaSubtipoDt.UPJ_SUCESSOES:
                    			   			//As �reas de Fam�lia foram desmembradas em Fam�lia - Capital e Fam�lia - Interior. Quando a capital for enviar pro interior, � preciso consultar separado.
                    			   			listAreasDistibuicao =  areaDistribuicaoNe.consultarAreasDistribuicaoServentiaSubTipo(String.valueOf(ServentiaSubtipoDt.FAMILIA_INTERIOR), comarcaDt.getId());
	                        			   break;
                    			   		case ServentiaSubtipoDt.JUIZADO_VIOLENCIA_DOMESTICA:
                    			   			listAreasDistibuicao =  areaDistribuicaoNe.consultarAreasDistribuicaoServentiaSubTipo(String.valueOf(ServentiaSubtipoDt.VARA_CRIMINAL), comarcaDt.getId());
	                        			   break;
									}
                    			   
                    		   }
                    		   	                        			   
                    		   
                    		   if (listAreasDistibuicao != null) {
                        		   if (listAreasDistibuicao.size() > 1){
                        			   //CALCULO PARA BUSCAR AREA DE DISTRIBUI��O ALEATORIA
                        			   double flNumero = Math.random();
                        			   double flPosicao = (double) 1/listAreasDistibuicao.size();
                        			   int inPosicao = (int)(flNumero / flPosicao);
                        			   id_AreaDistribuicao = (String) listAreasDistibuicao.get(inPosicao);
                        		   } else{
                        			   id_AreaDistribuicao = (String) listAreasDistibuicao.get(0);
                        		   }
                    		   }else {
                    			   throw new MensagemException("N�o foi poss�vel consultar �rea Distribui��o para Carta Precat�ria.");
                    		   }
                    	   }
                    	   
                    	   //String id_proc_tipo_precatoria = new ProcessoTipoNe().consultarIdProcessoTipo(String.valueOf(ProcessoTipoDt.CARTA_PRECATORIA), obFabricaConexao);
                    	   //em cumprimento ao provimento 16/2012 da CGJ
                    	   //id_Serventia = DistribuicaoProcesso.getInstance().getDistribuicao(id_AreaDistribuicao, id_proc_tipo_precatoria);
                    	   id_Serventia = DistribuicaoProcesso.getInstance().getDistribuicao(id_AreaDistribuicao);
                    	   
                    	   if (id_Serventia == null || id_Serventia.length() == 0)
                    		   throw new MensagemException("N�o foi poss�vel localizar Serventia cadastrada na �rea de Distribui��o. Favor entrar em contato com o suporte. (C�digo da �rea de Distribui��o: "+id_AreaDistribuicao+" , C�digo do Tipo de Processo: "+  processo.getId_ProcessoTipo() +").");
                    	   
                    	   // Consulta dados da serventia
                    	   String serventia = new ServentiaNe().consultarServentiaDescricao(id_Serventia, obFabricaConexao);
                           movimentacaoNe.gerarMovimentacaoCartaPrecatoriaExpedida(processoDt.getId(), processoDt.getProcessoNumero(), arqs, usuarioDt, "Para " + serventia, logDt, obFabricaConexao);
                           
                           //Daqui pra baixo muda*****************************************************************************************************************************
                           // Distribui processo para um juiz e salva como respons�vel
               			   //String id_ServentiaCargo = DistribuicaoProcessoServentiaCargo.getInstance().getDistribuicao1Grau(id_Serventia, processo.getId_ProcessoTipo());
                           //this.gerarPendenciaFilhaDistribuirCartaPrecatoria(pendenciaDt, id_ServentiaCargo, usuarioDt, fabrica);
                           //*************************************************************************************************************************************************
                           
                           ProcessoNe processoNe = new ProcessoNe();
                           ProcessoDt procCompletoDt = processoNe.consultarDadosProcessoPrecatoria(pendenciaDt.getId_Processo(), obFabricaConexao);

                           //configurando processo
                           procCompletoDt.setId_Serventia(id_Serventia);
                           procCompletoDt.setId_AreaDistribuicao(id_AreaDistribuicao);
                           procCompletoDt.setForumCodigo(new AreaDistribuicaoNe().consultarCodigoForum(id_AreaDistribuicao, obFabricaConexao));
                           
                           procCompletoDt.setId_UsuarioLog(logDt.getId_UsuarioLog());
                           procCompletoDt.setIpComputadorLog(logDt.getIpComputadorLog());
                           
                           processoNe.cadastrarProcessoPrecatoria(procCompletoDt, arqs, usuarioDt, processo.getId_Serventia(), obFabricaConexao);
                       
                       } else {
                           // gerar Carta Precatoria para serventia off-line
                           movimentacaoNe.gerarMovimentacaoCartaPrecatoriaExpedida(processoDt.getId(), processoDt.getProcessoNumero(), arqs, usuarioDt, "", logDt, obFabricaConexao);
                           this.gerarPendenciaAguardandoRetorno(pendenciaDt, processoDt, arqs, usuarioDt, obFabricaConexao);
                       }
                   }


                    break;
                case PendenciaTipoDt.OFICIO_DELEGACIA:
                    if (this.pendenciaTipoExpedir(pendenciaDt)) {
                        List arqs = this.consultarArquivosRespostaAssinados(pendenciaDt, true, obFabricaConexao);

                        if (serventiaDt.getId() != null && !serventiaDt.getId().equals("")) {
                            // gerar oficio para serventia on-line
                            movimentacaoNe.gerarMovimentacaoOficioExpedido(processoDt.getId(), processoDt.getProcessoNumero(), arqs, usuarioDt, "Para " + serventiaDt.getServentia(), logDt, obFabricaConexao);
                            this.gerarPendenciaFilhaExpedir(pendenciaDt, serventiaDt, usuarioDt, obFabricaConexao);
                        } else {
                            // gerar oficicio para serventia off-line
                            movimentacaoNe.gerarMovimentacaoOficioExpedido(processoDt.getId(), processoDt.getProcessoNumero(), arqs, usuarioDt, "", logDt, obFabricaConexao);
                            this.gerarPendenciaAguardandoRetorno(pendenciaDt, processoDt, arqs, usuarioDt, obFabricaConexao);
                        }
                    }

                    break;
                case PendenciaTipoDt.OFICIO:
                    if (this.pendenciaTipoExpedir(pendenciaDt)) {
                        List arqs = this.consultarArquivosRespostaAssinados(pendenciaDt, true, obFabricaConexao);

                        if (serventiaDt.getId() != null && !serventiaDt.getId().equals("")) {
                            // gerar oficio para serventia on-line
                            movimentacaoNe.gerarMovimentacaoOficioExpedido(processoDt.getId(), processoDt.getProcessoNumero(), arqs, usuarioDt, "Para " + serventiaDt.getServentia(), logDt, obFabricaConexao);
                            this.gerarPendenciaFilhaExpedir(pendenciaDt, serventiaDt, usuarioDt, obFabricaConexao);
                        } else {
                            // gerar oficicio para serventia off-line
                            movimentacaoNe.gerarMovimentacaoOficioExpedido(processoDt.getId(), processoDt.getProcessoNumero(), arqs, usuarioDt, "", logDt, obFabricaConexao);
                            this.gerarPendenciaAguardandoRetorno(pendenciaDt, processoDt, arqs, usuarioDt, obFabricaConexao);
                        }
                    }

                    break;
                case PendenciaTipoDt.REQUISICAO_PEQUENO_VALOR:
                    List arqsRPV = this.consultarArquivosRespostaAssinados(pendenciaDt, true, obFabricaConexao);
                    movimentacaoNe.gerarMovimentacaoRequisicaoPequenoValor(processoDt.getId(), processoDt.getProcessoNumero(), arqsRPV, usuarioDt, "", logDt, obFabricaConexao);
                	break;
//                case PendenciaTipoDt.RELATORIO:
//                    // gerar pendencia de revisao para o serventia cargo do revisor
//                    this.gerarPendenciaFilhaRelatorio(pendenciaDt, usuarioDt, fabrica);
//                    break;
               default:
            	   if (!this.pendenciaTipoVerificar(pendenciaDt)){
	                    if (this.pendenciaTipoExpedir(pendenciaDt)) {
	                        List arqs = this.consultarArquivosRespostaAssinados(pendenciaDt, true, obFabricaConexao);
	
	                        if (serventiaDt.getId() != null && !serventiaDt.getId().equals("")) {
	                            // gerar pendencia para serventia on-line
	                            movimentacaoNe.gerarMovimentacaoDocumentoExpedido(processoDt.getId(), processoDt.getProcessoNumero(), arqs, usuarioDt, pendenciaDt.getPendenciaTipo() + " Para " + serventiaDt.getServentia(), logDt, obFabricaConexao);
	                            this.gerarPendenciaFilhaExpedir(pendenciaDt, serventiaDt, usuarioDt, obFabricaConexao);
	                        } else {
	                        	String complementoGenerio = pendenciaDt.getPendenciaTipo();
	                        	if (pendenciaDt.getNomeParte() != null && pendenciaDt.getNomeParte().length() > 0)
	                        		complementoGenerio += " para "+pendenciaDt.getNomeParte();
	                        	
	                            // gerar pendencia para serventia off-line
	                            movimentacaoNe.gerarMovimentacaoDocumentoExpedido(processoDt.getId(), processoDt.getProcessoNumero(), arqs, usuarioDt, complementoGenerio, logDt, obFabricaConexao);
	                            this.gerarPendenciaAguardandoRetorno(pendenciaDt, processoDt, arqs, usuarioDt, obFabricaConexao);
	                        }
	                    }
            	   }
                    break;
                }
            }
        }
    }

	/**
     * Gera a pendencia de expedicao de uma intimacao ou citacao utilizando a
     * seguinte logica
     * 
     * @author Ronneesley Moura Teles
     * @since 27/01/2009 09:24
     * @param pendenciaDt
     *            vo da pendencia
     * @param arquivos
     *            arquivos a serem vinculados
     * @param usuarioDt
     *            usuario que realiza a operacao
     * @param fabrica
     *            fabrica de conexao para continuar uma transacao
     * @throws Exception
     */
    private void gerarIntimacaoCitacaoEfetivada(PendenciaDt pendenciaDt, ProcessoDt processoDt, List arquivos, UsuarioDt usuarioDt, FabricaConexao fabrica) throws Exception {
        String idUsuarioParte = this.consultarParteOnline(pendenciaDt);

        // Se a parte esta cadastrada
        if (idUsuarioParte != null) {
            ServentiaDt serventiaDt = new ServentiaNe().consultarId(usuarioDt.getId_Serventia());
            this.gerarIntimacaoOuCitacaoParte(pendenciaDt, idUsuarioParte, arquivos, serventiaDt, false, fabrica);
        } else {
            // Gerar pendencia aguardando retorno
            this.gerarPendenciaAguardandoRetorno(pendenciaDt, processoDt, arquivos, usuarioDt, fabrica);
        }
    }

    /**
     * Gera movimentacao para pendencia lida
     * 
     * @author Ronneesley Moura Teles
     * @since 07/01/2008 17:31
     * @param movimentacaoNe
     *            objeto de negocios de movimentacao
     * @param processoDt
     *            objeto de dados de processo
     * @param pendenciaDt
     *            objeto de dados de pendencia
     * @param usuarioDt
     *            objeto de dados do usuario, normalmente o usuario logado
     * @param arquivos
     *            lista de arquivos a serem anexados
     * @param logDt
     *            objeto de log
     * @param fabrica
     *            fabrica de conexao para continuar uma transacao existente
     * @throws Exception
     */
    public void gerarMovimentacaoLida(MovimentacaoNe movimentacaoNe, ProcessoDt processoDt, PendenciaDt pendenciaDt, UsuarioDt usuarioDt, List arquivos, LogDt logDt, FabricaConexao fabrica) throws Exception {
    	String complemento = "";
    	String responsavel = "";    	
    	// Verifica o tipo de pendencia
        switch (pendenciaDt.getPendenciaTipoCodigoToInt()) {
        case PendenciaTipoDt.ALVARA:
            movimentacaoNe.gerarMovimentacaoAlvaraEntregue(processoDt.getId(), processoDt.getProcessoNumero(), arquivos, usuarioDt, "(Referente � Mov. " + pendenciaDt.getMovimentacao() + ")", logDt, fabrica);
            break;
        case PendenciaTipoDt.ALVARA_SOLTURA:
            String id_Movi_AlvaraSolutra = movimentacaoNe.gerarMovimentacaoAlvaraSolturaEntregue(processoDt.getId(), processoDt.getProcessoNumero(), arquivos, usuarioDt, "(Referente � Mov. " + pendenciaDt.getMovimentacao() + ")", logDt, fabrica);
            //codigo novo salvando evento ***************************************************************************************************************************************************************************
            ProcessoPartePrisaoDt processoPartePrisaoDt = new ProcessoPartePrisaoDt();
            ProcessoPartePrisaoNe processoPartePrisaoNe = new ProcessoPartePrisaoNe();
            processoPartePrisaoDt = processoPartePrisaoNe.consultarUltimaPrisoesParte(pendenciaDt.getId_ProcessoParte());
            
            if (processoPartePrisaoDt == null){
            	throw new MensagemException( "N�o foi poss�vel encontrar os dados da pris�o da parte para serem atualizados! Por favor o usu�rio deve lan�ar a pris�o antes do alvar� de soltura" );
            }
            
            processoPartePrisaoDt.setId_EventoTipo(pendenciaDt.getId_EventoTipo());
            processoPartePrisaoDt.setEventoTipo(pendenciaDt.getEventoTipo());
            processoPartePrisaoDt.setDataEvento(pendenciaDt.getDataEvento());
            processoPartePrisaoDt.setId_MoviEvento(id_Movi_AlvaraSolutra);
            
            processoPartePrisaoDt.setIpComputadorLog(pendenciaDt.getIpComputadorLog());
            processoPartePrisaoDt.setId_UsuarioLog(pendenciaDt.getId_UsuarioLog());
            
            String strVerificar = processoPartePrisaoNe.VerificarFinalizacaoAlvaraSoltura(processoPartePrisaoDt);
			if (strVerificar.length()!= 0){
				throw new MensagemException( strVerificar );
			}
			processoPartePrisaoNe.salvar(processoPartePrisaoDt, fabrica);
			//codigo novo***************************************************************************************************************************************************************************
            break;
        case PendenciaTipoDt.INTIMACAO:
        case PendenciaTipoDt.INTIMACAO_AUDIENCIA:
        	complemento = "";
        	responsavel = pendenciaDt.getNomeParte();
        	complemento = "Para " + obtenhaComplementoLeituraPendenciaIntimacao(pendenciaDt) + " " + responsavel + " (Referente � Mov. " + pendenciaDt.getMovimentacao() + ")";
	   		if (complemento.length()>255)	{            	
	   			if (responsavel.length() > complemento.length()-255) {
	   				complemento = "Por " + obtenhaComplementoLeituraPendenciaIntimacao(pendenciaDt) + " " + responsavel.substring(0,(responsavel.length() -  (complemento.length()-255))) + " (Referente � Mov. " + pendenciaDt.getMovimentacao() + ")";
		        }
		    }   		 
            movimentacaoNe.gerarMovimentacaoIntimacaoEfetivada(processoDt.getId(), processoDt.getProcessoNumero(), arquivos, usuarioDt, complemento , logDt, fabrica);
            break;
        case PendenciaTipoDt.CARTA_CITACAO:
        case PendenciaTipoDt.CARTA_CITACAO_AUDIENCIA:
            movimentacaoNe.gerarMovimentacaoCitacaoEfetivada(processoDt.getId(), processoDt.getProcessoNumero(), arquivos, usuarioDt, "Para " + pendenciaDt.getNomeParte() + " (Referente � Mov. " + pendenciaDt.getMovimentacao() + ")", logDt, fabrica);
            break;
        case PendenciaTipoDt.CARTA_PRECATORIA:
            movimentacaoNe.gerarMovimentacaoCartaPrecatoriaCumprida(processoDt.getId(), processoDt.getProcessoNumero(), arquivos, usuarioDt, "(Referente � Mov. " + pendenciaDt.getMovimentacao() + ")", logDt, fabrica);
            break;
        case PendenciaTipoDt.OFICIO_DELEGACIA:
            movimentacaoNe.gerarMovimentacaoOficioEfetivado(processoDt.getId(), processoDt.getProcessoNumero(), arquivos, usuarioDt, "(Referente � Mov. " + pendenciaDt.getMovimentacao() + ")", logDt, fabrica);
            break;
        case PendenciaTipoDt.OFICIO:
            movimentacaoNe.gerarMovimentacaoOficioEfetivado(processoDt.getId(), processoDt.getProcessoNumero(), arquivos, usuarioDt, "(Referente � Mov. " + pendenciaDt.getMovimentacao() + ")", logDt, fabrica);
            break;
        case PendenciaTipoDt.MANDADO:
        	String idMandado = new MandadoJudicialNe().consultaIdMandadoPorIdPendencia(pendenciaDt.getId(), fabrica);
        	if(idMandado != null) {
        		// Mandados expedidos para a Central de Mandados do Projudi
        		movimentacaoNe.gerarMovimentacaoMandadoCumprido(processoDt.getId(), processoDt.getProcessoNumero(), arquivos, usuarioDt, "Para " + pendenciaDt.getNomeParte() + " (Mandado n� " + idMandado + " / Referente � Mov. " + pendenciaDt.getMovimentacao() + ")", logDt, fabrica);
        	} else {
        		// Mandados expedidos onde a Central do Projudi n�o se encontra implantada
        		movimentacaoNe.gerarMovimentacaoMandadoCumprido(processoDt.getId(), processoDt.getProcessoNumero(), arquivos, usuarioDt, "Para " + pendenciaDt.getNomeParte() + " (Referente � Mov. " + pendenciaDt.getMovimentacao() + ")", logDt, fabrica);
        	}
            break;
        case PendenciaTipoDt.PENHORA_ONLINE:
            movimentacaoNe.gerarMovimentacaoPenhoraRealizada(processoDt.getId(), processoDt.getProcessoNumero(), arquivos, usuarioDt, "(Referente � Mov. " + pendenciaDt.getMovimentacao() + ")", logDt, fabrica);
            break;
        case PendenciaTipoDt.INTIMACAO_VIA_TELEFONE:
        	complemento = "";
        	responsavel = pendenciaDt.getNomeParte();
        	complemento = "Para " + obtenhaComplementoLeituraPendenciaIntimacao(pendenciaDt) + " " + responsavel + " (Referente � Mov. " + pendenciaDt.getMovimentacao() + ")";
	   		if (complemento.length()>255)	{            	
	   			if (responsavel.length() > complemento.length()-255) {
	   				complemento = "Por " + obtenhaComplementoLeituraPendenciaIntimacao(pendenciaDt) + " " + responsavel.substring(0,(responsavel.length() -  (complemento.length()-255))) + " (Referente � Mov. " + pendenciaDt.getMovimentacao() + ")";
		        }
		    }   
            movimentacaoNe.gerarMovimentacaoIntimacaoViaTelefoneEfetivada(processoDt.getId(), processoDt.getProcessoNumero(), arquivos, usuarioDt, complemento, logDt, fabrica);
            break;
//        case PendenciaTipoDt.EDITAL:
//        case PendenciaTipoDt.CARTA_ADJUDICACAO:
//        case PendenciaTipoDt.CUMPRIMENTO_GENERICO:
//        case PendenciaTipoDt.PEDIDO_CONTADORIA:
//        case PendenciaTipoDt.PEDIDO_LAUDO:
        default:
            movimentacaoNe.gerarMovimentacaoDocumentoCumprido(processoDt.getId(), processoDt.getProcessoNumero(), arquivos, usuarioDt, pendenciaDt.getPendenciaTipo()+" - (Referente � Mov. " + pendenciaDt.getMovimentacao() + ")", logDt, fabrica);
            break;        
        }

    }

    /**
     * Gera movimentacao para pendencia n�o lida
     * 
     * @author Leandro Bernardes
     * @since 23/06/2009 17:31
     * @param movimentacaoNe
     *            objeto de negocios de movimentacao
     * @param processoDt
     *            objeto de dados de processo
     * @param pendenciaDt
     *            objeto de dados de pendencia
     * @param usuarioDt
     *            objeto de dados do usuario, normalmente o usuario logado
     * @param arquivos
     *            lista de arquivos a serem anexados
     * @param logDt
     *            objeto de log
     * @param fabrica
     *            fabrica de conexao para continuar uma transacao existente
     * @throws Exception
     */
    public void gerarMovimentacaoNaoLida(MovimentacaoNe movimentacaoNe, ProcessoDt processoDt, PendenciaDt pendenciaDt, UsuarioDt usuarioDt, List arquivos, LogDt logDt, FabricaConexao fabrica) throws Exception {
        // Verifica o tipo de pendencia
        switch (pendenciaDt.getPendenciaTipoCodigoToInt()) {
        case PendenciaTipoDt.INTIMACAO:
        case PendenciaTipoDt.INTIMACAO_AUDIENCIA:
        	String complemento = "(Referente � Mov. " + pendenciaDt.getMovimentacao() + ")";
        	complemento += obtenhaComplementoLeituraPendenciaIntimacao(pendenciaDt);
        	movimentacaoNe.gerarMovimentacaoIntimacaoNaoEfetivada(processoDt.getId(), processoDt.getProcessoNumero(), arquivos, usuarioDt, complemento.trim(), logDt, fabrica);
            break;
        case PendenciaTipoDt.CARTA_CITACAO:
        case PendenciaTipoDt.CARTA_CITACAO_AUDIENCIA:
            movimentacaoNe.gerarMovimentacaoCitacaoNaoEfetivada(processoDt.getId(), processoDt.getProcessoNumero(), arquivos, usuarioDt, "(Referente � Mov. " + pendenciaDt.getMovimentacao() + ")", logDt, fabrica);
            break;
        case PendenciaTipoDt.CARTA_PRECATORIA:
            movimentacaoNe.gerarMovimentacaoCartaPrecatoriaNaoCumprida(processoDt.getId(), processoDt.getProcessoNumero(), arquivos, usuarioDt, "(Referente � Mov. " + pendenciaDt.getMovimentacao() + ")", logDt, fabrica);
            break;
        case PendenciaTipoDt.OFICIO_DELEGACIA:
            movimentacaoNe.gerarMovimentacaoOficioNaoEfetivado(processoDt.getId(), processoDt.getProcessoNumero(), arquivos, usuarioDt, "(Referente � Mov. " + pendenciaDt.getMovimentacao() + ")", logDt, fabrica);
            break;
        case PendenciaTipoDt.OFICIO:
            movimentacaoNe.gerarMovimentacaoOficioNaoEfetivado(processoDt.getId(), processoDt.getProcessoNumero(), arquivos, usuarioDt, "(Referente � Mov. " + pendenciaDt.getMovimentacao() + ")", logDt, fabrica);
            break;
        case PendenciaTipoDt.MANDADO:
        	String idMandado = new MandadoJudicialNe().consultaIdMandadoPorIdPendencia(pendenciaDt.getId(), fabrica);
        	if(idMandado != null) {
        		// Mandados expedidos para a Central de Mandados do Projudi
            	movimentacaoNe.gerarMovimentacaoMandadoNaoCumprido(processoDt.getId(), processoDt.getProcessoNumero(), arquivos, usuarioDt, "Para " + pendenciaDt.getNomeParte() + " (Mandado n� " + idMandado + " / Referente � Mov. " + pendenciaDt.getMovimentacao() + ")", logDt, fabrica);
        	} else {
        		// Mandados expedidos onde a Central do Projudi n�o se encontra implantada
            	movimentacaoNe.gerarMovimentacaoMandadoNaoCumprido(processoDt.getId(), processoDt.getProcessoNumero(), arquivos, usuarioDt, "Para " + pendenciaDt.getNomeParte() + " (Referente � Mov. " + pendenciaDt.getMovimentacao() + ")", logDt, fabrica);
        	}
            break;
        case PendenciaTipoDt.PENHORA_ONLINE:
            movimentacaoNe.gerarMovimentacaoPenhoraNaoRealizada(processoDt.getId(), processoDt.getProcessoNumero(), arquivos, usuarioDt, "(Referente � Mov. " + pendenciaDt.getMovimentacao() + ")", logDt, fabrica);
            break;
        case PendenciaTipoDt.INTIMACAO_VIA_TELEFONE:
        	complemento = "Para " + pendenciaDt.getNomeParte();        	
        	complemento += " (Referente � Mov. " + pendenciaDt.getMovimentacao() + ")";
        	complemento += obtenhaComplementoLeituraPendenciaIntimacao(pendenciaDt);        
            movimentacaoNe.gerarMovimentacaoIntimacaoViaTelefoneNaoEfetivada(processoDt.getId(), processoDt.getProcessoNumero(), arquivos, usuarioDt, complemento, logDt, fabrica);
            break; 
           
//        case PendenciaTipoDt.EDITAL:
//        case PendenciaTipoDt.CARTA_ADJUDICACAO:
//        case PendenciaTipoDt.CUMPRIMENTO_GENERICO:
//        case PendenciaTipoDt.PEDIDO_CONTADORIA:
//        case PendenciaTipoDt.PEDIDO_LAUDO:
        default:
            movimentacaoNe.gerarMovimentacaoDocumentoNaoCumprido(processoDt.getId(), processoDt.getProcessoNumero(), arquivos, usuarioDt, pendenciaDt.getPendenciaTipo()+" - (Referente � Mov. " + pendenciaDt.getMovimentacao() + ")", logDt, fabrica);
            break;
        }
    }

    /**
     * Gera movimentacao para pendencia efetivada em parte
     * 
     * @author Leandro Bernardes
     * @since 23/06/2009 17:31
     * @param movimentacaoNe
     *            objeto de negocios de movimentacao
     * @param processoDt
     *            objeto de dados de processo
     * @param pendenciaDt
     *            objeto de dados de pendencia
     * @param usuarioDt
     *            objeto de dados do usuario, normalmente o usuario logado
     * @param arquivos
     *            lista de arquivos a serem anexados
     * @param logDt
     *            objeto de log
     * @param fabrica
     *            fabrica de conexao para continuar uma transacao existente
     * @throws Exception
     */
    public void gerarMovimentacaoEfetivadaParcialmente(MovimentacaoNe movimentacaoNe, ProcessoDt processoDt, PendenciaDt pendenciaDt, UsuarioDt usuarioDt, List arquivos, LogDt logDt, FabricaConexao fabrica) throws Exception {
        // Verifica o tipo de pendencia
        switch (pendenciaDt.getPendenciaTipoCodigoToInt()) {
        case PendenciaTipoDt.OFICIO_DELEGACIA:
            movimentacaoNe.gerarMovimentacaoOficioEfetivadoParcialmente(processoDt.getId(), processoDt.getProcessoNumero(), arquivos, usuarioDt, "(Referente � Mov. " + pendenciaDt.getMovimentacao() + ")", logDt, fabrica);
            break;
        case PendenciaTipoDt.OFICIO:
            movimentacaoNe.gerarMovimentacaoOficioEfetivadoParcialmente(processoDt.getId(), processoDt.getProcessoNumero(), arquivos, usuarioDt, "(Referente � Mov. " + pendenciaDt.getMovimentacao() + ")", logDt, fabrica);
            break;
        case PendenciaTipoDt.MANDADO:
        	String idMandado = new MandadoJudicialNe().consultaIdMandadoPorIdPendencia(pendenciaDt.getId(), fabrica);
        	if(idMandado != null) {
        		movimentacaoNe.gerarMovimentacaoMandadoCumpridoParcialmente(processoDt.getId(), processoDt.getProcessoNumero(), arquivos, usuarioDt, "Para " + pendenciaDt.getNomeParte() + " (Mandado n� " + idMandado + " / Referente � Mov. " + pendenciaDt.getMovimentacao() + ")", logDt, fabrica);
        	} else {
        		movimentacaoNe.gerarMovimentacaoMandadoCumpridoParcialmente(processoDt.getId(), processoDt.getProcessoNumero(), arquivos, usuarioDt, "Para " + pendenciaDt.getNomeParte() + " (Referente � Mov. " + pendenciaDt.getMovimentacao() + ")", logDt, fabrica);
        	}
            break;
        case PendenciaTipoDt.PENHORA_ONLINE:
            movimentacaoNe.gerarMovimentacaoPenhoraRealizadaParcialmente(processoDt.getId(), processoDt.getProcessoNumero(), arquivos, usuarioDt, "(Referente � Mov. " + pendenciaDt.getMovimentacao() + ")", logDt, fabrica);
            break;
        default:
        	movimentacaoNe.gerarMovimentacaoDocumentoCumpridoParcialmente(processoDt.getId(), processoDt.getProcessoNumero(), arquivos, usuarioDt, pendenciaDt.getPendenciaTipo()+" - (Referente � Mov. " + pendenciaDt.getMovimentacao() + ")", logDt, fabrica);
        	break;
        }

    }

    /**
     * Gera movimentacao para pendencia cancelada
     * 
     * @author Leandro Bernardes
     * @since 31/08/2009
     * @param movimentacaoNe
     *            objeto de negocios de movimentacao
     * @param processoDt
     *            objeto de dados de processo
     * @param pendenciaDt
     *            objeto de dados de pendencia
     * @param usuarioDt
     *            objeto de dados do usuario, normalmente o usuario logado
     * @param arquivos
     *            lista de arquivos a serem anexados
     * @param logDt
     *            objeto de log
     * @param fabrica
     *            fabrica de conexao para continuar uma transacao existente
     * @throws Exception
     */
    public void gerarMovimentacaoCancelada(MovimentacaoNe movimentacaoNe, ProcessoDt processoDt, PendenciaDt pendenciaDt, UsuarioDt usuarioDt, List arquivos, LogDt logDt, FabricaConexao fabrica) throws Exception {
        // Verifica o tipo de pendencia
        switch (pendenciaDt.getPendenciaTipoCodigoToInt()) {
	        case PendenciaTipoDt.ALVARA:
	            movimentacaoNe.gerarMovimentacaoAlvaraCancelado(processoDt.getId(), processoDt.getProcessoNumero(), arquivos, usuarioDt, "(Referente � Mov. " + pendenciaDt.getMovimentacao() + ")", logDt, fabrica);
	            break;
	        case PendenciaTipoDt.ALVARA_SOLTURA:
	        	movimentacaoNe.gerarMovimentacaoAlvaraSolturaCancelado(processoDt.getId(), processoDt.getProcessoNumero(), arquivos, usuarioDt, "(Referente � Mov. " + pendenciaDt.getMovimentacao() + ")", logDt, fabrica);
	        	break;
        }
    }
        
    /**
     * Consultar prazos decorridos ou a decorrer
     * 
     * @author Ronneesley Moura Teles
     * @since 23/01/2009 13:48
     * @param usuarioNe
     *            usuario da sessao
     * @param tipoPendencia
     *            tipo da pendencia, 1 - intimacao, 2 - citacao, outros - ambos
     * @param numero_processo
     *            numero do processo
     * @param posicao
     *            posicao de paginacao
     * @return lista de pendencias com o hash
     * @throws Exception
     */
    public List consultarPrazosDecorridosADecorrerComHash(UsuarioNe usuarioNe, Integer tipoPendencia, String numero_processo, String posicao, boolean aDecorrer) throws Exception {
        List pendencias = this.consultarPrazosDecorridosADecorrer(usuarioNe.getUsuarioDt(), tipoPendencia, numero_processo, posicao, aDecorrer);

        if (pendencias != null && pendencias.size() > 0){
	        Iterator iterator = pendencias.iterator();
	
	        while (iterator.hasNext()) {
	            PendenciaDt pendenciaDt = (PendenciaDt) iterator.next();
	
	            pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
	        }
        }

        return pendencias;
    }
    
    /**
     * Consultar pendencias de prazos decorridos ou a decorer
     * 
     * @author Ronneesley Moura Teles
     * @since 23/01/2009 13:48
     * @param usuarioDt
     *            usuario que executa a acao
     * @param tipoPendencia
     *            tipo da pendencia, 1 - intimacao, 2 - citacao, outros
     * @param numero_processo
     *            numero do processo
     * @param posicao
     *            posicao da paginacao
     * @return lista de pendencias
     * @throws Exception
     */
    public List consultarPrazosDecorridosADecorrer(UsuarioDt usuarioDt, Integer tipoPendencia, String numero_processo, String posicao, boolean aDecorrer) throws Exception {
        List pendencias = null;
        
        FabricaConexao obFabricaConexao = null;
        
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            PendenciaTipoNe pendenciaTipoNe = new PendenciaTipoNe();
            PendenciaTipoDt pendenciaTipoDt = new PendenciaTipoDt();
            if (tipoPendencia != null)
                pendenciaTipoDt = pendenciaTipoNe.consultarId(String.valueOf(tipoPendencia));
            else
                pendenciaTipoDt = null;

            List tempList = obPersistencia.consultarPrazosDecorridosADecorrer(usuarioDt, pendenciaTipoDt, numero_processo, posicao, aDecorrer);

            setQuantidadePaginas(tempList);

            pendencias = (List) tempList;
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }
    
    /**
     * Consultar pendencias de prazos decorridos
     * 
     * @param usuarioDt
     *            usuario que executa a acao
     * @param tipoPendencia
     *            tipo da pendencia, 1 - intimacao, 2 - citacao, outros
     * @param numero_processo
     *            numero do processo
     * @param posicao
     *            posicao da paginacao
     * @return lista de pendencias
     * @throws Exception
     */
    public List consultarPrazosDecorridosDevolucaoAutos(UsuarioDt usuarioDt, String numero_processo, String posicao) throws Exception {
        List pendencias = null;
        
        FabricaConexao obFabricaConexao = null;
        
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            List tempList = obPersistencia.consultarPrazosDecorridosDevolucaoAutos(usuarioDt, numero_processo, posicao);
            setQuantidadePaginas(tempList);

            pendencias = (List) tempList;
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }

    /**
     * Consultar pendencias para leitura automatica
     * 
     * @author Leandro Bernardes
     * @return lista de pendencias
     * @throws Exception
     */
    public List consultarPendenciasLeituraAutomatica() throws Exception {
        List tempList = null;

        FabricaConexao obFabricaConexao = null;
        
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

            tempList = obPersistencia.consultarPendenciasLeituraAutomatica();

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return tempList;
    }
    
    /**
     * Consultar pendencias para fechamento automatica
     * 
     * @author Leandro Bernardes
     * @return lista de pendencias
     * @throws Exception
     */
    public List consultarPendenciasFechamentoAutomatica() throws Exception {
        List tempList = null;
        
        FabricaConexao obFabricaConexao = null;
        
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

            tempList = obPersistencia.consultarPendenciasFechamentoAutomatica();

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return tempList;
    }
    
    /**
     * Consultar pendencias para fechamento automatica
     * 
     * @author Leandro Bernardes
     * @return lista de pendencias
     * @throws Exception
     */
    public List consultarPendenciasSolicitacaoCargaFechamentoAutomatica() throws Exception {
        List tempList = null;
        
        FabricaConexao obFabricaConexao = null;
        
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

            tempList = obPersistencia.consultarPendenciasSolicitacaoCargaFechamentoAutomatica();

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return tempList;
    }

    /**
     * Consultar pendencias de prazos decorridos da serventia
     * 
     * @author Leandro Bernardes
     * @since 21/08/2009
     * @param usuarioDt
     *            usuario que executa a acao
     * @return quantidade de pendencia com prazo decorrido na serventia
     * @throws Exception
     */
    public Long consultarQtdPrazosDecorridos(UsuarioDt usuarioDt) throws Exception {
        Long qtdPrazoDecorrido = null;
        
        FabricaConexao obFabricaConexao = null;
        
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            qtdPrazoDecorrido = obPersistencia.consultarQtdPrazosDecorridos(usuarioDt);
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return qtdPrazoDecorrido;
    }
    
    /**
     * Consultar pendencias de prazos decorridos da serventia
     * 
     * @author Leandro Bernardes
     * @param usuarioDt
     *            usuario que executa a acao
     * @return quantidade de pendencia com prazo decorrido na serventia
     * @throws Exception
     */
    public Long consultarQtdPrazosDecorridosDevolucaoAutos(UsuarioDt usuarioDt) throws Exception {
        Long qtdPrazoDecorrido = null;
        
        FabricaConexao obFabricaConexao = null;
        
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            qtdPrazoDecorrido = obPersistencia.consultarQtdPrazosDecorridosDevolucaoAutos(usuarioDt);
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return qtdPrazoDecorrido;
    }
    
    /**
     * Consultar pendencias que liberam acesso ao processo 
     * 
     * @author Leandro Bernardes
     * @param usuarioDt
     *            usuario que executa a acao
     * @return quantidade de pendencia de libera��o de acesso
     * @throws Exception
     */
    public Long consultarQtdPendenciaLiberaAcesso(UsuarioDt usuarioDt) throws Exception {
        
        FabricaConexao obFabricaConexao = null;
        
        Long qtdPendenciaLiberaAcesso = new Long(0);
//        if(usuarioDt.getId_ServentiaCargo() != null && !usuarioDt.getId_ServentiaCargo().equalsIgnoreCase("")){
//        	if (((usuarioDt.getId_ServentiaCargo() != null && !usuarioDt.getId_ServentiaCargo().trim().equals(""))
//        			|| (usuarioDt.getId_ServentiaCargoUsuarioChefe() != null && !usuarioDt.getId_ServentiaCargoUsuarioChefe().trim().equals("")))
//        			|| ((usuarioDt.getId_UsuarioServentia() != null && !usuarioDt.getId_UsuarioServentia().trim().equals(""))
//        					|| (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().trim().equals("")))){
//        		try {
//        			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
//        			PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
//        			qtdPendenciaLiberaAcesso = obPersistencia.consultarQtdPendenciaLiberaAcesso(usuarioDt);
//        		} finally {
//        			obFabricaConexao.fecharConexao();
//        		}
//        	}
//        } else if(usuarioDt.getGrupoTipoCodigo().equals(String.valueOf(GrupoTipoDt.ADVOGADO))){
        	try {
    			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
    			PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
    			qtdPendenciaLiberaAcesso = obPersistencia.consultarQtdPendenciaLiberaAcesso(usuarioDt);
    		} finally {
    			obFabricaConexao.fecharConexao();
    		}
//        }

        return qtdPendenciaLiberaAcesso;
    }
    
    /**
      * Consultar pendencias informativas de um tipo de serventia
     * 
     * @author Leandro Bernardes
     * @param usuarioDt
     *            usuario que executa a acao
     * @return quantidade de pendencia informativas
     * @throws Exception
     */
    public Long consultarQtdPendenciaInformativa(UsuarioDt usuarioDt) throws Exception {
        FabricaConexao obFabricaConexao = null;
        Long qtdPendenciaInformativa = new Long(0);
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            qtdPendenciaInformativa = obPersistencia.consultarQtdPendenciaInformativa(usuarioDt);
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return qtdPendenciaInformativa;
    }
    
    /**
     * Consultar pendencias de libera��o de acesso com hash
    * 
    * @author Leandro Bernardes
    * @param usuarioNe
    *            usuarioNe comusuario que executa a acao
    * @return lista de pend�ncias
    * @throws Exception
    */
    public List consultarPendenciasLiberarAcessoComHash(UsuarioNe usuarioNe) throws Exception {
    	 List pendencias = null;
    	 
    	 FabricaConexao obFabricaConexao = null;
    	 
         try {
             obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
             PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
             List tempList = obPersistencia.consultarPendenciasLiberarAcessoComHash(usuarioNe);
             pendencias = (List) tempList;
         } finally {
             obFabricaConexao.fecharConexao();
         }

         return pendencias;
    }
    
    public boolean consultarPendenciasLiberarAcessoWebservice(UsuarioNe usuarioNe, String idProcesso) throws Exception {
    	boolean existeLiberarAcesso = false;
   	 
   	 	FabricaConexao obFabricaConexao = null;
   	 
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            existeLiberarAcesso = obPersistencia.consultarPendenciasLiberarAcessoWebservice(usuarioNe, idProcesso);
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return existeLiberarAcesso;
   }
    
    /**
     * Consultar pendencias de informativas
    * 
    * @author Leandro Bernardes
    * @param usuarioNe
    *            usuarioNe comusuario que executa a acao
    * @return lista de pend�ncias
    * @throws Exception
    */
    public List consultarPendenciasInformativas(UsuarioNe usuarioNe) throws Exception {
   	     List pendencias = null;
   	    
   	     FabricaConexao obFabricaConexao = null;
   	
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            List tempList = obPersistencia.consultarPendenciasInformativas(usuarioNe);
            pendencias = (List) tempList;
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
   }

    /**
     * Consulta quantidade de pend�ncias em andamentos
     * 
     * @author Leandro Bernardes
     * @param usuarioDt
     *            usuario que executa a acao
     * @return Consulta quantidade de pend�ncias em andamentos
     * @throws Exception
     */
    public Long consultarQtdPendenciasAndamento(UsuarioDt usuarioDt) throws Exception {
        Long qtdPrazoDecorrido = null;
        
        FabricaConexao obFabricaConexao = null;
        
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            qtdPrazoDecorrido = obPersistencia.consultarQtdPendenciasAndamento(usuarioDt);
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return qtdPrazoDecorrido;
    }

    /**
     * Consultar prazos decorridos de um usu�rio
     * 
     * @author Leandro Bernardes
     * @since 13/08/2009
     * @param usuarioNe
     *            usuario da sessao
     * @param tipoPendencia
     *            tipo da pendencia, 1 - intimacao, 2 - citacao, outros - ambos
     * @param numero_processo
     *            numero do processo
     * @param posicao
     *            posicao de paginacao
     * @return lista de pendencias com o hash
     * @throws Exception
     */
    public List consultarPrazosDecorridosUsuarioComHash(UsuarioNe usuarioNe, Integer tipoPendencia, String numero_processo, String posicao) throws Exception {
        List pendencias = this.consultarPrazosDecorridosUsuario(usuarioNe.getUsuarioDt(), tipoPendencia, numero_processo, posicao);

        if (pendencias != null && pendencias.size() > 0){
	        Iterator it = pendencias.iterator();
	
	        while (it.hasNext()) {
	            PendenciaDt pendenciaDt = (PendenciaDt) it.next();
	
	            pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
	        }
        }

        return pendencias;
    }

    /**
     * Consultar prazos decorridos de um usu�rio
     * 
     * @author Leandro Bernardes
     * @since 13/08/2009
     * @param usuarioDt
     *            usuario que executa a acao
     * @param tipoPendencia
     *            tipo da pendencia, 1 - intimacao, 2 - citacao, outros - ambos
     * @param numero_processo
     *            numero do processo
     * @param posicao
     *            posicao da paginacao
     * @return lista de pendencias
     * @throws Exception
     */
    public List consultarPrazosDecorridosUsuario(UsuarioDt usuarioDt, Integer tipoPendencia, String numero_processo, String posicao) throws Exception {
        List pendencias = null;
        
        FabricaConexao obFabricaConexao = null;
        
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            PendenciaTipoNe pendenciaTipoNe = new PendenciaTipoNe();
            PendenciaTipoDt pendenciaTipoDt = new PendenciaTipoDt();

            if (tipoPendencia != null)
                pendenciaTipoDt = pendenciaTipoNe.consultarId(String.valueOf(tipoPendencia));
            else
                pendenciaTipoDt = null;

            pendencias = obPersistencia.consultarPrazosDecorridosUsuario(usuarioDt, pendenciaTipoDt, numero_processo, posicao);

            QuantidadePaginas = (Long) pendencias.get(pendencias.size() - 1);
            pendencias.remove(pendencias.size() - 1);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }

    /**
     * Listagem de pendencias finalizadas
     * 
     * @author Ronneesley Moura Teles
     * @since 23/01/2009 13:48 [muito antes]
     * @param dataInicio
     *            data de inicio
     * @param dataFinal
     *            data de fim
     * @param usuarioDt
     *            vo de usuario
     * @param posicao
     *            posicao para paginacao
     * @return List
     * @throws Exception
     */
    public List consultarFinalizadas(UsuarioNe usuarioNe, String id_UsuarioServentia, String id_ServentiaUsuario, PendenciaTipoDt pendenciaTipoDt, PendenciaStatusDt pendenciaStatusDt, boolean prioridade, Integer filtroTipo, String numero_processo, String dataInicialInicio, String dataFinalInicio, String dataInicioFim, String dataFinalFim, String posicao) throws Exception {
        List tempList = null;
        
        FabricaConexao obFabricaConexao = null;
        
        try {
            // Deixa a data inicial no inicio do dia
            if (dataInicialInicio != null) dataInicialInicio += " 00:00:00";
            // Deixa a data final ate o fim do dia
            if (dataFinalInicio != null) dataFinalInicio += " 23:59:59";

            // Deixa a data inicial no inicio do dia
            if (dataInicioFim != null) dataInicioFim += " 00:00:00";
            // Deixa a data final ate o fim do dia
            if (dataFinalFim != null) dataFinalFim += " 23:59:59";

            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

            tempList = obPersistencia.consultarFinalizadas(usuarioNe, id_UsuarioServentia, id_ServentiaUsuario, pendenciaTipoDt, pendenciaStatusDt, prioridade, filtroTipo, numero_processo, dataInicialInicio, dataFinalInicio, dataInicioFim, dataFinalFim, posicao);

            setQuantidadePaginas(tempList);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return tempList;
    }
    
    /**
     * Listagem de pendencias fechadas independente de estarem vistadas ou n�o.
     * 
     * @param dataInicio
     *            data de inicio
     * @param dataFinal
     *            data de fim
     * @param posicao
     *            posicao para paginacao
     * @return List
     * @throws Exception
     */
    public List consultarPendenciasFechadas(UsuarioNe usuarioNe, String id_UsuarioServentia, String id_ServentiaUsuario, PendenciaTipoDt pendenciaTipoDt, PendenciaStatusDt pendenciaStatusDt, boolean prioridade, Integer filtroTipo, String numero_processo, String dataInicialInicio, String dataFinalInicio, String dataInicioFim, String dataFinalFim, String posicao) throws Exception {
        List tempList = null;
        
        FabricaConexao obFabricaConexao = null;
        
        try {
            // Deixa a data inicial no inicio do dia
            if (dataInicialInicio != null) dataInicialInicio += " 00:00:00";
            // Deixa a data final ate o fim do dia
            if (dataFinalInicio != null) dataFinalInicio += " 23:59:59";

            // Deixa a data inicial no inicio do dia
            if (dataInicioFim != null) dataInicioFim += " 00:00:00";
            // Deixa a data final ate o fim do dia
            if (dataFinalFim != null) dataFinalFim += " 23:59:59";

            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

            tempList = obPersistencia.consultarPendenciasFechadas(usuarioNe, id_UsuarioServentia, id_ServentiaUsuario, pendenciaTipoDt, pendenciaStatusDt, prioridade, filtroTipo, numero_processo, dataInicialInicio, dataFinalInicio, dataInicioFim, dataFinalFim, posicao);

            setQuantidadePaginas(tempList);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return tempList;
    }
    
    /**
     * Listagem de pendencias respondidas da serventia
     * 
     * @author Leandro Bernardes
     * @param dataInicio
     *            data de inicio
     * @param dataFinal
     *            data de fim
     * @param usuarioDt
     *            vo de usuario
     * @param posicao
     *            posicao para paginacao
     * @return List
     * @throws Exception
     */
    public List consultarRespondidasServentia(UsuarioNe usuarioNe, PendenciaTipoDt pendenciaTipoDt, PendenciaStatusDt pendenciaStatusDt, boolean prioridade, Integer filtroTipo, String numero_processo, String dataInicialInicio, String dataFinalInicio, String dataInicioFim, String dataFinalFim, String posicao) throws Exception {
        List tempList = null;
        
        FabricaConexao obFabricaConexao = null;
        
        try {
            // Deixa a data inicial no inicio do dia
            if (dataInicialInicio != null) dataInicialInicio += " 00:00:00";
            // Deixa a data final ate o fim do dia
            if (dataFinalInicio != null) dataFinalInicio += " 23:59:59";

            // Deixa a data inicial no inicio do dia
            if (dataInicioFim != null) dataInicioFim += " 00:00:00";
            // Deixa a data final ate o fim do dia
            if (dataFinalFim != null) dataFinalFim += " 23:59:59";

            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

            tempList = obPersistencia.consultarRespondidasServentia(usuarioNe, pendenciaTipoDt, pendenciaStatusDt, prioridade, filtroTipo, numero_processo, dataInicialInicio, dataFinalInicio, dataInicioFim, dataFinalFim, posicao);

            setQuantidadePaginas(tempList);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return tempList;
    }
    
    /**
     * Listagem de pendencias respondidas da serventia
     * 
     * @author Leandro Bernardes
     * @param dataInicio
     *            data de inicio
     * @param dataFinal
     *            data de fim
     * @param usuarioDt
     *            vo de usuario
     * @param posicao
     *            posicao para paginacao
     * @return List
     * @throws Exception
     */
    public List consultarRespondidasUsuario(UsuarioNe usuarioNe, PendenciaTipoDt pendenciaTipoDt, PendenciaStatusDt pendenciaStatusDt, boolean prioridade, Integer filtroTipo, String numero_processo, String dataInicialInicio, String dataFinalInicio, String dataInicioFim, String dataFinalFim, String posicao) throws Exception {
        List tempList = null;
        
        FabricaConexao obFabricaConexao = null;
        
        try {
            // Deixa a data inicial no inicio do dia
            if (dataInicialInicio != null) dataInicialInicio += " 00:00:00";
            // Deixa a data final ate o fim do dia
            if (dataFinalInicio != null) dataFinalInicio += " 23:59:59";

            // Deixa a data inicial no inicio do dia
            if (dataInicioFim != null) dataInicioFim += " 00:00:00";
            // Deixa a data final ate o fim do dia
            if (dataFinalFim != null) dataFinalFim += " 23:59:59";

            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

            tempList = obPersistencia.consultarRespondidasUsuario(usuarioNe, pendenciaTipoDt, pendenciaStatusDt, prioridade, filtroTipo, numero_processo, dataInicialInicio, dataFinalInicio, dataInicioFim, dataFinalFim, posicao);

            setQuantidadePaginas(tempList);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return tempList;
    }

    /**
     * Listagem de intima��es lidas de um usuario serventia ou de cargo
     * serventia
     * 
     * @since 26/11/2009
     * @param usuarioDt
     *            vo de usuario
     * @param pendenciaTipoDt
     *            vo de pendencia
     * @param numero_processo
     *            numero do processo
     * @param dataInicialInicio
     *            data inicial de inicio
     * @param dataFinalInicio
     *            data final de inicio
     * @param dataInicioFim
     *            data inicial de fim
     * @param dataFinalFim
     *            data final de fim
     * @param posicao
     *            posicao para paginacao
     * @return lista de pendencias onde estao finalizadas e o cargo do usuario e
     *         um dos responsaveis
     * @throws Exception
     */
    public List consultarIntimacoesLidas(UsuarioNe usuarioNe, String numero_processo, String dataInicialInicio, String dataFinalInicio, String dataInicioFim, String dataFinalFim, String posicao) throws Exception {
        List pendencias;
        PendenciaTipoDt pendenciaTipoDt = new PendenciaTipoDt();
        pendenciaTipoDt.setPendenciaTipoCodigo(String.valueOf(PendenciaTipoDt.INTIMACAO));
        int GrupoCodigo = Funcoes.StringToInt(usuarioNe.getUsuarioDt().getGrupoCodigo());
        if (GrupoCodigo == GrupoDt.MINISTERIO_PUBLICO || GrupoCodigo== GrupoDt.MP_TCE){
            pendencias = this.consultarFinalizadasResponsavelServentiaCargo(usuarioNe, pendenciaTipoDt, numero_processo, null, null, null, null, posicao);
        }else{
            pendencias = this.consultarFinalizadasResponsavelServentia(usuarioNe, pendenciaTipoDt, numero_processo, null, null, null, null, posicao);
        }
        return pendencias;
    }

    /**
     * Listagem de intima��es distribu�das de uma serventia
     * 
     * @since 23/02/2011
     * @param usuarioDt
     *            vo de usuario
     * @param pendenciaTipoDt
     *            vo de pendencia
     * @param numero_processo
     *            numero do processo
     * @param dataInicialInicio
     *            data inicial de inicio
     * @param dataFinalInicio
     *            data final de inicio
     * @param dataInicioFim
     *            data inicial de fim
     * @param dataFinalFim
     *            data final de fim
     * @param posicao
     *            posicao para paginacao
     * @return lista de pendencias onde estao finalizadas e o cargo do usuario e
     *         um dos responsaveis
     * @throws Exception
     */
    public List consultarIntimacoesDistribuidas(UsuarioNe usuarioNe, String numero_processo, String dataInicialInicio, String dataFinalInicio, String posicao) throws Exception {
        List pendencias = new ArrayList();

        if (Funcoes.StringToInt(usuarioNe.getUsuarioDt().getGrupoCodigo()) == GrupoDt.COORDENADOR_PROMOTORIA)
            pendencias = this.consultarIntimacoesDistribuidasPromotoria(usuarioNe, numero_processo, dataInicialInicio, dataFinalInicio, posicao);
        else if (usuarioNe.getUsuarioDt().isCoordenadorJuridico()){        		
            pendencias = this.consultarIntimacoesDistribuidasProcuradoria(usuarioNe, numero_processo, dataInicialInicio, dataFinalInicio, posicao);
        }
        
        return pendencias;
    }
    
    public String consultarIntimacoesDistribuidasJSON(UsuarioNe usuarioNe, String numero_processo, String dataInicialInicio, String dataFinalInicio, String posicao) throws Exception {
    	String pendencias = "";

        if (usuarioNe.getUsuarioDt().isCoordenadorPromotoria()) {
            pendencias = this.consultarIntimacoesDistribuidasPromotoriaJSON(usuarioNe, numero_processo, dataInicialInicio, dataFinalInicio, posicao);
        }else if (usuarioNe.getUsuarioDt().isCoordenadorJuridico()){
            pendencias = this.consultarIntimacoesDistribuidasProcuradoriaJSON(usuarioNe, numero_processo, dataInicialInicio, dataFinalInicio, posicao);
        }    
        return pendencias;
    }
    
    /**
     * Listagem de pendencias finalizadas
     * 
     * @author Ronneesley Moura Teles
     * @since 16/01/2009 16:05
     * @param usuarioDt
     *            vo de usuario
     * @param pendenciaTipoDt
     *            vo de pendencia
     * @param numero_processo
     *            numero do processo
     * @param dataInicialInicio
     *            data inicial de inicio
     * @param dataFinalInicio
     *            data final de inicio
     * @param dataInicioFim
     *            data inicial de fim
     * @param dataFinalFim
     *            data final de fim
     * @param posicao
     *            posicao para paginacao
     * @return lista de pendencias onde estao finalizadas e o usuario e um dos
     *         responsaveis
     * @throws Exception
     */
    public List consultarFinalizadasResponsavelServentia(UsuarioNe usuarioNe, PendenciaTipoDt pendenciaTipoDt, String numero_processo, String dataInicialInicio, String dataFinalInicio, String dataInicioFim, String dataFinalFim, String posicao) throws Exception {
        List tempList = null;
        
        FabricaConexao obFabricaConexao = null;
                
        try {
            // Deixa a data inicial no inicio do dia
            if (dataInicialInicio != null) dataInicialInicio += " 00:00:00";
            // Deixa a data final ate o fim do dia
            if (dataFinalInicio != null) dataFinalInicio += " 23:59:59";

            // Deixa a data inicial no inicio do dia
            if (dataInicioFim != null) dataInicioFim += " 00:00:00";
            // Deixa a data final ate o fim do dia
            if (dataFinalFim != null) dataFinalFim += " 23:59:59";

            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            
            int GrupoCodigo = Funcoes.StringToInt(usuarioNe.getUsuarioDt().getGrupoCodigo());            
            if (GrupoCodigo == GrupoDt.MINISTERIO_PUBLICO || GrupoCodigo == GrupoDt.MP_TCE ){
                tempList = obPersistencia.consultarIntimacoesCitacoesLidas(usuarioNe, usuarioNe.getUsuarioDt().getId_UsuarioServentia(), usuarioNe.getUsuarioDt().getId_ServentiaCargo(), pendenciaTipoDt, numero_processo, dataInicialInicio, dataFinalInicio, dataInicioFim, dataFinalFim, posicao);
            }else{
                tempList = obPersistencia.consultarIntimacoesCitacoesLidas(usuarioNe, usuarioNe.getUsuarioDt().getId_UsuarioServentia(), "", pendenciaTipoDt, numero_processo, dataInicialInicio, dataFinalInicio, dataInicioFim, dataFinalFim, posicao);
            }
            
            setQuantidadePaginas(tempList);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return tempList;
    }

    /**
     * Listagem de pendencias finalizadas
     * 
     * @since 26/11/2009
     * @param usuarioDt
     *            vo de usuario
     * @param pendenciaTipoDt
     *            vo de pendencia
     * @param numero_processo
     *            numero do processo
     * @param dataInicialInicio
     *            data inicial de inicio
     * @param dataFinalInicio
     *            data final de inicio
     * @param dataInicioFim
     *            data inicial de fim
     * @param dataFinalFim
     *            data final de fim
     * @param posicao
     *            posicao para paginacao
     * @return lista de pendencias onde estao finalizadas e o cargo do usuario e
     *         um dos responsaveis
     * @throws Exception
     */
    public List consultarFinalizadasResponsavelServentiaCargo(UsuarioNe usuarioNe, PendenciaTipoDt pendenciaTipoDt, String numero_processo, String dataInicialInicio, String dataFinalInicio, String dataInicioFim, String dataFinalFim, String posicao) throws Exception {
        List tempList = null;
        
        FabricaConexao obFabricaConexao = null;
        
        try {
            // Deixa a data inicial no inicio do dia
            if (dataInicialInicio != null) dataInicialInicio += " 00:00:00";
            // Deixa a data final ate o fim do dia
            if (dataFinalInicio != null) dataFinalInicio += " 23:59:59";

            // Deixa a data inicial no inicio do dia
            if (dataInicioFim != null) dataInicioFim += " 00:00:00";
            // Deixa a data final ate o fim do dia
            if (dataFinalFim != null) dataFinalFim += " 23:59:59";

            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

            tempList = obPersistencia.consultarIntimacoesCitacoesLidas(usuarioNe, usuarioNe.getUsuarioDt().getId_UsuarioServentia(), usuarioNe.getUsuarioDt().getId_ServentiaCargo(), pendenciaTipoDt, numero_processo, dataInicialInicio, dataFinalInicio, dataInicioFim, dataFinalFim, posicao);

            setQuantidadePaginas(tempList);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return tempList;
    }

    /**
     * Listagem de pendencias intima��es distribu�das
     * 
     * @since 23/02/2011
     * @param usuarioDt
     *            vo de usuario
     * @param pendenciaTipoDt
     *            vo de pendencia
     * @param numero_processo
     *            numero do processo
     * @param dataInicialInicio
     *            data inicial de inicio
     * @param dataFinalInicio
     *            data final de inicio
     * @param dataInicioFim
     *            data inicial de fim
     * @param dataFinalFim
     *            data final de fim
     * @param posicao
     *            posicao para paginacao
     * @return lista de pendencias onde estao finalizadas e o cargo do usuario e
     *         um dos responsaveis
     * @throws Exception
     */
    public List consultarIntimacoesDistribuidasPromotoria(UsuarioNe usuarioNe, String numero_processo, String dataInicialInicio, String dataFinalInicio, String posicao) throws Exception {
        List tempList = null;
        
        FabricaConexao obFabricaConexao = null;
        
        try {
            // Deixa a data inicial no inicio do dia
            if (dataInicialInicio != null) dataInicialInicio += " 00:00:00";
            // Deixa a data final ate o fim do dia
            if (dataFinalInicio != null) dataFinalInicio += " 23:59:59";

            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

            tempList = obPersistencia.consultarIntimacoesDistribuidasPromotoria(usuarioNe, "", usuarioNe.getUsuarioDt().getId_ServentiaCargo(), numero_processo, dataInicialInicio, dataFinalInicio, posicao);

            setQuantidadePaginas(tempList);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return tempList;
    }
    
    /**
     * Listagem de pendencias intima��es distribu�das
     * 
     * @since 21/03/2011
     * @param usuarioDt
     *            vo de usuario
     * @param pendenciaTipoDt
     *            vo de pendencia
     * @param numero_processo
     *            numero do processo
     * @param dataInicialInicio
     *            data inicial de inicio
     * @param dataFinalInicio
     *            data final de inicio
     * @param dataInicioFim
     *            data inicial de fim
     * @param dataFinalFim
     *            data final de fim
     * @param posicao
     *            posicao para paginacao
     * @return lista de pendencias onde estao finalizadas e o cargo do usuario e
     *         um dos responsaveis
     * @throws Exception
     */
    public List consultarIntimacoesDistribuidasProcuradoria(UsuarioNe usuarioNe, String numero_processo, String dataInicialInicio, String dataFinalInicio, String posicao) throws Exception {
        List tempList = null;
        
        FabricaConexao obFabricaConexao = null;
        
        try {
            // Deixa a data inicial no inicio do dia
            if (dataInicialInicio != null) dataInicialInicio += " 00:00:00";
            // Deixa a data final ate o fim do dia
            if (dataFinalInicio != null) dataFinalInicio += " 23:59:59";

            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

            tempList = obPersistencia.consultarIntimacoesDistribuidasProcuradoria(usuarioNe, "", usuarioNe.getUsuarioDt().getId_ServentiaCargo(), numero_processo, dataInicialInicio, dataFinalInicio, posicao);

            setQuantidadePaginas(tempList);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return tempList;
    }
    
    /**
     * Retorna a pendencia filha da pendencia
     * 
     * @author Ronneesley Moura Teles
     * @since 02/12/2008 09:30
     * @param pendenciaDt
     *            vo da pendencia
     * @return PendenciaDt
     * @throws Exception
     */
    public PendenciaDt consultarFilha(PendenciaDt pendenciaDt) throws Exception {
        List filhas = this.consultarFilhas(pendenciaDt);

        // Se tem pendencia
        if (filhas != null && filhas.size() > 0) return (PendenciaDt) filhas.get(0);

        return null;
    }

    /*
     private boolean pendenciaCartaPrecatoriaSolucionar(PendenciaDt pendenciaDt) {
        boolean retorno = false;
        Integer tipoPendencia = (pendenciaDt.getPendenciaTipoCodigoToInt();
        if (tipoPendencia == PendenciaTipoDt.CARTA_PRECATORIA && pendenciaDt.getId_PendenciaPai() != null && Funcoes.StringToInt(pendenciaDt.getPendenciaStatusCodigo()) != PendenciaStatusDt.EmAndamento && !pendenciaDt.getId_PendenciaPai().equals("")) retorno = true;

        return retorno;
    }
    */

    /**
     * Reabri uma pendencia
     * 
     * @author Ronneesley Moura Teles
     * @since 27/11/2008 11:18
     * @param pendenciaDt
     *            vo de pendencia
     * @param usuarioDt
     *            vo de usuario
     * @param arquivos
     *            lista de arquivos
     * @return void
     * @throws Exception
     */
    public void reabrir(PendenciaDt pendenciaDt, Map arquivos, UsuarioDt usuarioDt) throws Exception {

        FabricaConexao obFabricaConexao = null;
        
        try {
            // Abre uma conexao
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_CORRECAO));

            this.gerarPendencia(pendenciaDt, pendenciaDt.getResponsaveis(), null, (List) Funcoes.converterMapParaList(arquivos), false, false, obFabricaConexao);

            // vistar pendencia pai
            PendenciaDt pendenciaPai = new PendenciaDt();
            pendenciaPai.setId(pendenciaDt.getId_PendenciaPai());
            pendenciaPai.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_CORRECAO));
            this.finalizarPendenciaReaberta(pendenciaPai, usuarioDt, obFabricaConexao);

            pendenciaPai.limpar();
            pendenciaDt.limpar();
            arquivos.clear();
        } finally {
            // Se a conexao foi criada dentro do metodo, entao finaliza a
            // conexao
            obFabricaConexao.fecharConexao();
        }
    }

    /**
     * Verifica se o usuario pode reabrir uma pendencia
     * 
     * @param pendenciaDt
     *            vo de pendencia
     * @param Map
     *           map com arquivos da pendencia
     * @param usuarioDt
     *            vo de usuario
     * @return String com mensagem de retorno
     * @throws Exception
     */
    public String verificarReabrirPendencia(PendenciaDt pendenciaDt, Map arquivos, UsuarioDt usuarioDt) throws Exception {
        String msgErro = "";
        // Verifica se a pendencia possui uma pendencia pai
        if (pendenciaDt.getId_PendenciaPai() == null || pendenciaDt.getId_PendenciaPai().equals("")) msgErro = "N�o foi poss�vel determinar a pend�ncia que ir� dar origem a esta.";

        // Verifica se possui arquivos
        if (arquivos == null || arquivos.size() == 0) msgErro += " � necess�rio anexar arquivos para reabrir a pend�ncia";

        // Verifica se a pendencia pai esta finalizada
        PendenciaDt pendenciaPaiDt = this.consultarId(pendenciaDt.getId_PendenciaPai());

        if (!this.verificaFinalizada(pendenciaPaiDt)) msgErro += " A pend�ncia que deseja reabrir n�o est� finalizada";

        // Verifica se o usuario pode reabrir
        if (!this.podeReabrir(pendenciaPaiDt, usuarioDt)) msgErro += " O usu�rio n�o tem permiss�o para reabrir a pend�ncia";
        return msgErro;
    }

    /**
     * Verifica se o usuario pode reabrir uma pendencia
     * 
     * @author Ronneesley Moura Teles
     * @since 24/11/2008 13:42
     * @param pendenciaDt
     *            vo de pendencia
     * @param usuarioDt
     *            vo de usuario
     * @return boolean
     * @throws Exception
     */
    public boolean podeReabrir(PendenciaDt pendenciaDt, UsuarioDt usuarioDt) throws Exception {
        if (pendenciaDt != null) {
            // Somente caso o usuario seja o cadastrador e a pendencia esteja finalizada
            //if (pendenciaDt.getId_UsuarioCadastrador().equals(usuarioDt.getId_UsuarioServentia()) && this.verificaFinalizada(pendenciaDt)) return true;
        	   if (this.verificaFinalizada(pendenciaDt)) return true;
            
        }

        return false;
    }

    /**
     * Finaliza pendencia prazo decorrido (data visto)
     * 
     * @author Leandro Bernardes
     * @since 05/08/2009
     * @param pendenciaDt
     *            vo da pendencia
     * @param usuarioDt
     *            vo usuario que deseja consultar a pendencia
     * @return PendenciaDt
     * @throws Exception
     */
    public PendenciaDt visualizarPendenciaPrazoDecorrido(PendenciaDt pendenciaDt, UsuarioDt usuarioDt) throws Exception {
        if (pendenciaDt == null || pendenciaDt.getId() == null || pendenciaDt.getId().trim().equals("")) throw new MensagemException("Falta dados para a visualiza��o.");

        if ((pendenciaDt.getDataFim() != null && !pendenciaDt.getDataFim().equals("")) && (pendenciaDt.getDataVisto() == null || pendenciaDt.getDataVisto().equals(""))) { // Finaliza
            this.finalizarPendenciaPrazoDecorrido(pendenciaDt, usuarioDt);
        }

        return pendenciaDt;
    }

    /**
     * Adiciona em uma lista geral, todas as pendencias de uma lista parcial
     * 
     * @author Ronneesley Moura Teles
     * @since 19/11/2008 14:01
     * @param geral
     * @param parcial
     */
    private void adicionarLista(List geral, List parcial) {
        if (parcial != null && parcial.size() > 0) {
            Iterator itParcial = parcial.iterator();

            while (itParcial.hasNext()) {
                PendenciaDt pendenciaDt = (PendenciaDt) itParcial.next();

                geral.add(pendenciaDt);
            }
        }
    }

    /**
     * Consultar pendencia pai recursivamente com os responsaveis
     * 
     * @author Ronneesley Moura Teles
     * @since 19/01/2009 16:15
     * @param pendenciaDt
     *            vo de pendencia
     * @return lista das pendencias pai e os seus respectivos responsaveis
     * @throws Exception
     */
    public List consultarPaiResponsavelRecursiva(PendenciaDt pendenciaDt) throws Exception {
        List lisPendencias = new ArrayList();
        PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
        
        PendenciaDt aux = pendenciaDt;

        do {
            aux = this.consultarFinalizadaId(aux.getId_PendenciaPai());
            if(aux!=null && aux.getId()!=null){
	            List LisResps =pendenciaResponsavelNe.consultarResponsaveisDetalhado(aux.getId());
	            if (LisResps!=null && LisResps.size()>0){	  
	            	aux.setResponsaveis(LisResps);
	            	lisPendencias.add(aux);
	            }
            }
        }while (aux!=null && aux.getId_PendenciaPai() != null && !aux.getId_PendenciaPai().trim().equals(""));
    
        return lisPendencias;
    }
    
    /**
     * Consultar pendencia pai recursivamente com os responsaveis de pendencias finalizadas
     * 
     * @author Jesus Rodrigo
     * @since 21/08/2014
     * @param pendenciaDt     *            vo de pendencia
     * @return lista das pendencias pai e os seus respectivos responsaveis
     * @throws Exception
     */
    public List consultarPaiResponsavelRecursivaFinalizado(PendenciaDt pendenciaDt) throws Exception {
        List lisPendencias = new ArrayList();
        PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();

        PendenciaDt aux = pendenciaDt;

        do{
            aux = this.consultarFinalizadaId(aux.getId_PendenciaPai());
            if(aux!=null && aux.getId()!=null){
            	List lisResps = pendenciaResponsavelNe.consultarResponsaveisDetalhadoPendenciaFinalizada(aux.getId(), null, null);
            	if (lisResps!=null && lisResps.size()>0){	   
            		aux.setResponsaveis(lisResps);
            		lisPendencias.add(aux);
            	}
            }
        }while (aux!=null && aux.getId_PendenciaPai() != null && !aux.getId_PendenciaPai().trim().equals(""));
    
        return lisPendencias;
    }

    /**
     * Consultar pendencia pai recursivamente
     * 
     * @author Ronneesley Moura Teles
     * @since 24/11/2008 14:56
     * @param pendenciaDt
     * @return List
     * @throws Exception
     */
    public List consultarPaiRecursiva(PendenciaDt pendenciaDt, UsuarioNe usuarioNe) throws Exception {
        List pendencias = new ArrayList();

        PendenciaDt aux = pendenciaDt;

        while (aux != null && aux.getId_PendenciaPai() != null && !aux.getId_PendenciaPai().trim().equals("")) {
            aux = this.consultarFinalizadaId(aux.getId_PendenciaPai());
            if (aux!=null){
            	aux.setHash(usuarioNe.getCodigoHash(aux.getId()));
            	pendencias.add(aux);
            }
        }

        return pendencias;
    }

    /**
     * Consultar todas as pendencias filhas de uma pendencia, este metodo ira
     * encontrar todas as pendencias, ate mesmo filhas das filhas da pendencia
     * original
     * 
     * @author Ronneesley Moura Teles
     * @since 19/11/2008 13:29
     * @param pendenciaDt
     *            VO para pendencia
     * @return List
     * @throws Exception
     */
    public List consultarFilhasRecursivas(PendenciaDt pendenciaDt) throws Exception {
        List pendencias = this.consultarFilhas(pendenciaDt);

        // Se tem filhas
        if (pendencias != null && pendencias.size() > 0) {
            Iterator itFilhas = pendencias.iterator();

            // Lista temporaria para todas as filhas da pendencia
            List tempFilhas = new ArrayList();

            // Percorre todas as pendencias filhas
            while (itFilhas.hasNext()) {
                PendenciaDt pendenciaFilha = (PendenciaDt) itFilhas.next();

                List filhas = this.consultarFilhasRecursivas(pendenciaFilha);

                this.adicionarLista(tempFilhas, filhas);
            }

            // Adiciona na lista geral todas as temporarias
            this.adicionarLista(pendencias, tempFilhas);

            // Limpa a memoria da lista temporaria
            tempFilhas = null;
        }

        return pendencias;
    }

    /**
     * Consultar as pendencias filhas que sao autos de uma pendencia
     * recursivamente adicionando a lista de responsaveis
     * 
     * @author Ronneesley Moura Teles
     * @since 22/01/2009 09:12
     * @param pendenciaDt    vo de pendencia
     * @return lista de pendencias filhas que sao autos com os arquivos
     * @throws Exception
     */
    public List consultarFilhasAutosArquivosRecursivas(PendenciaDt pendenciaDt, UsuarioNe usuarioNe) throws Exception {
        List pendencias = this.consultarFilhasAutosRecursivas(pendenciaDt, false);

        if (pendencias != null && pendencias.size() > 0){
	        Iterator it = pendencias.iterator();
	
	        while (it.hasNext()) {
	            PendenciaDt pendencia = (PendenciaDt) it.next();
	
	            pendencia.setListaArquivos(this.consultarArquivosResposta(pendencia, usuarioNe));
	        }
        }

        return pendencias;
    }
    
    /**
     * Consultar as pendencias filhas finalizadas que sao autos de uma pendencia
     * recursivamente adicionando a lista de responsaveis
     * 
     * @author Jesus Rodrigo
     * @since 22/01/2009 09:12
     * @param pendenciaDt    vo de pendencia
     * @return lista de pendencias filhas que sao autos com os arquivos
     * @throws Exception
     */
    public List consultarFilhasFinalizadasAutosArquivosRecursivas(PendenciaDt pendenciaDt, UsuarioNe usuarioNe) throws Exception {
        List pendencias = this.consultarFilhasFinalizadasAutosRecursivas(pendenciaDt);

        if (pendencias != null && pendencias.size() > 0){
	        Iterator it = pendencias.iterator();
	
	        while (it.hasNext()) {
	            PendenciaDt pendencia = (PendenciaDt) it.next();
	
	            pendencia.setListaArquivos(this.consultarArquivosRespostaFinalizado(pendencia, usuarioNe));
	        }
        }

        return pendencias;
    }

    /**
     * Consultar todas as pendencias filhas de uma pendencia de autos, este
     * metodo ira encontrar todas as pendencias, ate mesmo filhas das filhas da
     * pendencia original
     * 
     * @author Ronneesley Moura Teles
     * @since 22/01/2009 09:09
     * @param pendenciaDt
     *            VO para pendencia
     * @param somenteFinalizadas
     *            somente as pendencias finalizadas
     * @return lista das pendencias filhas do tipo autos de toda arvore
     * @throws Exception
     */
    public List consultarFilhasAutosRecursivas(PendenciaDt pendenciaDt, boolean somenteFinalizadas) throws Exception {
        List pendencias = this.consultarFilhasAutos(pendenciaDt, somenteFinalizadas);

        // Se tem filhas
        if (pendencias != null && pendencias.size() > 0) {
            Iterator itFilhas = pendencias.iterator();

            // Lista temporaria para todas as filhas da pendencia
            List tempFilhas = new ArrayList();

            // Percorre todas as pendencias filhas
            while (itFilhas.hasNext()) {
                PendenciaDt pendenciaFilha = (PendenciaDt) itFilhas.next();

                List filhas = this.consultarFilhasAutosRecursivas(pendenciaFilha, somenteFinalizadas);
                if (filhas!=null && filhas.size()>0){
                	this.adicionarLista(tempFilhas, filhas);
                }
            }

            // Adiciona na lista geral todas as temporarias
            this.adicionarLista(pendencias, tempFilhas);

            // Limpa a memoria da lista temporaria
            tempFilhas = null;
        }

        return pendencias;
    }
    
    /**
     * Consultar todas as pendencias filhas de uma pendencia de autos, este
     * metodo ira encontrar todas as pendencias, ate mesmo filhas das filhas da
     * pendencia original
     * 
     * @author Jesus Rodrigo
     * @since 25/08/2014
     * @param pendenciaDt   VO para pendencia
     * @param somenteFinalizadas     somente as pendencias finalizadas
     * @return lista das pendencias filhas do tipo autos de toda arvore
     * @throws Exception
     */
    public List consultarFilhasFinalizadasAutosRecursivas(PendenciaDt pendenciaDt) throws Exception {
        List pendencias = this.consultarFilhasFinalizadaAutos(pendenciaDt);

        // Se tem filhas
        if (pendencias != null && pendencias.size() > 0) {
            Iterator itFilhas = pendencias.iterator();

            // Lista temporaria para todas as filhas da pendencia
            List tempFilhas = new ArrayList();

            // Percorre todas as pendencias filhas
            while (itFilhas.hasNext()) {
                PendenciaDt pendenciaFilha = (PendenciaDt) itFilhas.next();

                List filhas = this.consultarFilhasFinalizadasAutosRecursivas(pendenciaFilha);
                if (filhas!=null && filhas.size()>0){
                	this.adicionarLista(tempFilhas, filhas);
                }
            }

            // Adiciona na lista geral todas as temporarias
            this.adicionarLista(pendencias, tempFilhas);

            // Limpa a memoria da lista temporaria
            tempFilhas = null;
        }

        return pendencias;
    }

    /**
     * Consulta as pendencias filhas de uma pendencia
     * 
     * @author Ronneesley Moura Teles
     * @since 19/11/2008 13:27
     * @param pendenciaDt
     *            VO de pendencia
     * @return List
     * @throws Exception
     */
    public List consultarFilhas(PendenciaDt pendenciaDt) throws Exception {
        List pendencias = new ArrayList();

        FabricaConexao obFabricaConexao = null;
        
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            pendencias = obPersistencia.consultarFilhas(pendenciaDt);
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }

    /**
     * Consulta as pendencias filhas de autos de uma pendencia
     * 
     * @author Ronneesley Moura Teles
     * @since 22/01/2009 09:07
     * @param pendenciaDt
     *            VO de pendencia
     * @param somenteFinalizadas
     *            somente as pendencias finalizadas
     * @return lista de pendencias filhas de uma pendencia do tipo autos
     * @throws Exception
     */
    public List consultarFilhasAutos(PendenciaDt pendenciaDt, boolean somenteFinalizadas) throws Exception {
        List pendencias = new ArrayList();
        
        FabricaConexao obFabricaConexao = null;
        
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            pendencias = obPersistencia.consultarFilhasAutos(pendenciaDt, somenteFinalizadas);
        } finally {
            obFabricaConexao.fecharConexao();
		}

        return pendencias;
    }

    /**
     * Consulta as pendencias filhas de autos de uma pendencia
     * 
     * @author Jesus Rodrigo
     * @since 25/08/2014
     * @param pendenciaDt         VO de pendencia
     * @param somenteFinalizadas
     *            somente as pendencias finalizadas
     * @return lista de pendencias filhas de uma pendencia do tipo autos
     * @throws Exception
     */
    public List consultarFilhasFinalizadaAutos(PendenciaDt pendenciaDt) throws Exception {
        List pendencias = new ArrayList();
        
        FabricaConexao obFabricaConexao = null;
        
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            pendencias = obPersistencia.consultarFilhasFinalizadasAutos(pendenciaDt);
        } finally {
            obFabricaConexao.fecharConexao();
		}

        return pendencias;
    }
    
    /**
     * Consulta todos os responsaveis de uma determinada pendencia
     * 
     * @author Ronneesley Moura Teles
     * @since 14/10/2008 11:39
     * @param pendenciaDt
     *            vo de pendencia
     * @return List
     * @throws Exception
     */
    public List consultarResponsaveis(PendenciaDt pendenciaDt) throws Exception {
        if (pendenciaDt.getId() == null || pendenciaDt.getId().trim().equals("")) throw new MensagemException("� necess�rio informar o ID da pend�ncia.");

        PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
        return pendenciaResponsavelNe.consultarResponsaveis(pendenciaDt.getId());
    }
    
    /**
     * Consulta todos os responsaveis de uma determinada pendencia
     * 
     * @param pendenciaDt
     *            vo de pendencia
     * @return List
     * @throws Exception
     */
    public List consultarResponsaveisFinais(PendenciaDt pendenciaDt) throws Exception {
        if (pendenciaDt.getId() == null || pendenciaDt.getId().trim().equals("")) throw new MensagemException("� necess�rio informar o ID da pend�ncia.");

        PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
        return pendenciaResponsavelNe.consultarResponsaveisFinais(pendenciaDt.getId());
    }
    
    /**
     * Consulta todos os responsaveis de uma determinada pendencia
     * 
     * @param pendenciaDt
     *            vo de pendencia
     * @return List
     * @throws Exception
     */
    public List consultarHistoricosPendenciaFinal(String id_Pendencia) throws Exception {
        if (id_Pendencia == null || id_Pendencia.trim().equals("")) throw new MensagemException("� necess�rio informar o ID da pend�ncia.");

        PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
        return pendenciaResponsavelNe.consultarHistoricosPendenciaFinal(id_Pendencia);
    }

    /**
     * Consulta as pendencias de um determinado usuario
     * 
     * @author Ronneesley Moura Teles
     * @since 12/11/2008 15:17
     * @param usuarioDt
     *            vo do usuario
     * @param posicao
     *            posicao da paginacao
     * @return List
     * @throws Exception
     */
    public List consultarMinhas(UsuarioNe usuarioNe, PendenciaTipoDt pendenciaTipoDt, PendenciaStatusDt pendenciaStatusDt, boolean prioridade, Integer filtroTipo, String numero_processo, String dataInicialInicio, String dataFinalInicio, String posicao) throws Exception {
        List pendencias = null;
        
        FabricaConexao obFabricaConexao = null;
        
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

            // Deixa a data inicial no inicio do dia
            if (dataInicialInicio != null && !dataInicialInicio.trim().equals("")) dataInicialInicio += " 00:00:00";
            // Deixa a data final ate o fim do dia
            if (dataFinalInicio != null && !dataFinalInicio.trim().equals("")) dataFinalInicio += " 23:59:59";

            List tempList = obPersistencia.consultarMinhas(usuarioNe, pendenciaTipoDt, pendenciaStatusDt, prioridade, filtroTipo, numero_processo, dataInicialInicio, dataFinalInicio, posicao);

            setQuantidadePaginas(tempList);

            pendencias = (List) tempList;
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }

    /**
     * Consulta as pendencias expedidas para serventias (on-line)
     * 
     * @author Leandro Bernardes
     * @since 13/08/2009
     * @param usuarioDt
     *            vo do usuario
     * @param posicao
     *            posicao da paginacao
     * @return List
     * @throws Exception
     */
    public List consultarExpedidasServentia(UsuarioNe usuarioNe, PendenciaTipoDt pendenciaTipoDt, PendenciaStatusDt pendenciaStatusDt, boolean prioridade, Integer filtroTipo, String numero_processo, String dataInicialInicio, String dataFinalInicio, String posicao) throws Exception {
        List pendencias = null;

        FabricaConexao obFabricaConexao = null;
        
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

            // Deixa a data inicial no inicio do dia
            if (dataInicialInicio != null && !dataInicialInicio.trim().equals("")) dataInicialInicio += " 00:00:00";
            // Deixa a data final ate o fim do dia
            if (dataFinalInicio != null && !dataFinalInicio.trim().equals("")) dataFinalInicio += " 23:59:59";

            List tempList = obPersistencia.consultarExpedidasServentia(usuarioNe, pendenciaTipoDt, pendenciaStatusDt, prioridade, filtroTipo, numero_processo, dataInicialInicio, dataFinalInicio, posicao);

            setQuantidadePaginas(tempList);

            pendencias = tempList;
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }

    /**
     * Consulta a quantidade de pendencias expedidas para serventias (on-line) e
     * que est�o aguardando visto
     * 
     * @author Leandro Bernardes
     * @since 26/08/2009
     * @param UsuarioDt
     *            usuarioDt, vo do usuario
     * @return Long
     * @throws Exception
     */
    public Long consultarQtdExpedidasAguardandoVisto(UsuarioDt usuarioDt) throws Exception {
        Long qtdQtdExpedidasAguardandoVisto = null;
        
        FabricaConexao obFabricaConexao = null;
        
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

            qtdQtdExpedidasAguardandoVisto = obPersistencia.consultarQtdExpedidasAguardandoVisto(usuarioDt);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return qtdQtdExpedidasAguardandoVisto;
    }

    /**
     * Retorna um lista de tipos de pendencia abertas, reservada, pre-analisadas
     * por serventia cargo
     * 
     * @author Leandro Bernardes
     * @since 10/08/2009
     * @param String id_ServentiaCargo
     * @param boolean ehPendenteAssinatura
     * @return Map
     * @throws Exception
     */
    public Map consultarTiposServentiaCargoJuiz(String id_ServentiaCargo, boolean ehPendenteAssinatura) throws Exception {
        Map tiposPendencia = new HashMap();
        
        FabricaConexao obFabricaConexao = null;
        
        if (id_ServentiaCargo != null && !id_ServentiaCargo.equals("")) {
            try {
                obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

                PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
                
                tiposPendencia = pendenciaPs.consultarTiposServentiaCargoJuiz(id_ServentiaCargo, ehPendenteAssinatura);

            } finally {
                obFabricaConexao.fecharConexao();
            }
        }
        return tiposPendencia;
    }

    /**
     * Cria uma nova pendencia
     * 
     * @author Ronneesley Moura Teles
     * @since 21/10/2008 14:51
     * @param pendenciaDt
     *            vo de pendencia
     * @param arquivos
     *            lista de arquivos para a pendencia
     * @param usuarioDt
     *            usuario que esta realizando o cadastro
     * @throws Exception
     */
    public void criar(PendenciaDt pendenciaDt, Map arquivos, UsuarioDt usuarioDt) throws Exception {
        // Configura o cadastrador
        pendenciaDt.setId_UsuarioCadastrador(usuarioDt.getId_UsuarioServentia());
        pendenciaDt.setId_UsuarioFinalizador("null");
        pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_EM_ANDAMENTO));

        if (pendenciaDt.getResponsaveis() != null) {
            if (pendenciaDt.getResponsaveis().size() == 0) {
                throw new MensagemException("Selecione o destino da pend�ncia");
            } else {
                if (pendenciaDt.getResponsaveis().size() > 1) {
                    throw new MensagemException("A cria��o de pend�ncia aceita apenas um respons�vel, total informado.");
                }
            }
        }

        if (pendenciaDt.getId_PendenciaTipo() == null || pendenciaDt.getId_PendenciaTipo().equals("")) 
        	throw new MensagemException("� necess�rio selecionar o tipo da pend�ncia.");

        if (arquivos == null || arquivos.size() == 0) 
        	throw new MensagemException("� necess�rio anexar arquivos para criar a pend�ncia.");

        PendenciaTipoNe pendenciaTipoNe = new PendenciaTipoNe();
        PendenciaTipoDt pendenciaTipoDt = pendenciaTipoNe.consultarId(pendenciaDt.getId_PendenciaTipo());
        
        if (pendenciaDt != null && Funcoes.StringToInt(pendenciaTipoDt.getPendenciaTipoCodigo()) == PendenciaTipoDt.INFORMATIVO){
        	// Proximo dia, a contagem se inicia no dia seguinte
        	Calendar calCalendario = Calendar.getInstance();
            calCalendario.setTime(new Date());
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
            pendenciaDt.setDataInicio(df.format(new Date()));
            pendenciaDt.setDataVisto(df.format(new Date()));
            calCalendario.add(Calendar.DATE, 30);
            pendenciaDt.setDataLimite(df.format(calCalendario.getTime()));
        	pendenciaDt.setPrazo("30");
        }
        
        // Inicia a transacao real
        this.gerarPendencia(pendenciaDt, pendenciaDt.getResponsaveis(), null, Funcoes.converterMapParaList(arquivos) , false, false, null);
    }

    /**
     * Cria uma nova pendencia a partir do webservice
     * 
     * @author asrocha
     * @param pendenciaDt
     *            vo de pendencia
     * @param usuarioDt
     *            usuario que esta realizando o cadastro
     * @throws Exception
     */
    public void criarPendenciaWebservice(PendenciaDt pendenciaDt, UsuarioDt usuarioDt) throws Exception {
        // Configura o cadastrador
        pendenciaDt.setId_UsuarioCadastrador(usuarioDt.getId_UsuarioServentia());
        pendenciaDt.setId_UsuarioFinalizador("null");
        pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_EM_ANDAMENTO));

        if (pendenciaDt.getResponsaveis() != null) {
            if (pendenciaDt.getResponsaveis().size() == 0) {
                throw new MensagemException("Selecione o destino da pend�ncia.");
            } else {
                if (pendenciaDt.getResponsaveis().size() > 1) {
                    throw new MensagemException("A cria��o de pend�ncia aceita apenas um respons�vel, total informado: " + pendenciaDt.getResponsaveis().size() + ".");
                }
            }
        }

        if (pendenciaDt.getId_PendenciaTipo() == null || pendenciaDt.getId_PendenciaTipo().equals("")) 
        	throw new MensagemException("<{ � necess�rio selecionar o tipo da pend�ncia.");

        if (pendenciaDt.getListaArquivos() == null || pendenciaDt.getListaArquivos().size() == 0) 
        	throw new MensagemException("� necess�rio anexar arquivos para criar a pend�ncia.");

        PendenciaTipoNe pendenciaTipoNe = new PendenciaTipoNe();
        PendenciaTipoDt pendenciaTipoDt = pendenciaTipoNe.consultarId(pendenciaDt.getId_PendenciaTipo());
        
        if (pendenciaDt != null && Funcoes.StringToInt(pendenciaTipoDt.getPendenciaTipoCodigo()) == PendenciaTipoDt.INFORMATIVO){
        	// Proximo dia, a contagem se inicia no dia seguinte
        	Calendar calCalendario = Calendar.getInstance();
            calCalendario.setTime(new Date());
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
            pendenciaDt.setDataInicio(df.format(new Date()));
            pendenciaDt.setDataVisto(df.format(new Date()));
            calCalendario.add(Calendar.DATE, 30);
            pendenciaDt.setDataLimite(df.format(calCalendario.getTime()));
        	pendenciaDt.setPrazo("30");
        }
        
        // Inicia a transacao real
        this.gerarPendencia(pendenciaDt, pendenciaDt.getResponsaveis(), null, pendenciaDt.getListaArquivos() , false, false, null);
    }
    
    public PendenciaDt criarPendenciaDesembargador(PendenciaDt pendenciaDesembargadorDt, String pendenciaTipoCodigo, UsuarioDt usuarioDt, String idAudienciaProcessoSessaoSegundoGrau, String Id_ServentiaCargoRelatorSessao) throws Exception {
    	FabricaConexao obFabricaConexao = null;
    	
    	try {
    		
	    	obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			obFabricaConexao.iniciarTransacao();
			
			PendenciaDt pendenciaDt = criarPendenciaDesembargador(pendenciaDesembargadorDt, pendenciaTipoCodigo, usuarioDt, idAudienciaProcessoSessaoSegundoGrau, Id_ServentiaCargoRelatorSessao, obFabricaConexao);
	        
	    	obFabricaConexao.finalizarTransacao();
	    	
	    	return pendenciaDt;
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
        
    }
    
    public PendenciaDt criarPendenciaDesembargador(PendenciaDt pendenciaDesembargadorDt, String pendenciaTipoCodigo, UsuarioDt usuarioDt, String idAudienciaProcessoSessaoSegundoGrau, String Id_ServentiaCargoRelatorSessao, FabricaConexao obFabricaConexao) throws Exception {
    	PendenciaDt pendenciaDt = new PendenciaDt();    	
    	
		// Configura o cadastrador
        pendenciaDt.setId_UsuarioCadastrador(usuarioDt.getId_UsuarioServentia());
        pendenciaDt.setId_UsuarioFinalizador("null");
        // Configura outras informa��es
        pendenciaDt.setPendenciaTipoCodigo(pendenciaTipoCodigo);
        pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_EM_ANDAMENTO));
        pendenciaDt.setId_Movimentacao(pendenciaDesembargadorDt.getId_Movimentacao());
        pendenciaDt.setId_Processo(pendenciaDesembargadorDt.getId_Processo());
        pendenciaDt.setId_ProcessoPrioridade(pendenciaDesembargadorDt.getId_ProcessoPrioridade());
		pendenciaDt.setDataVisto(Funcoes.DataHora(new Date()));
		pendenciaDt.setId_UsuarioLog(usuarioDt.getId());
		pendenciaDt.setIpComputadorLog(usuarioDt.getIpComputadorLog());
		
		String Id_ServentiaCargo = usuarioDt.getId_ServentiaCargo();
		
		if (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSESSOR_DESEMBARGADOR) Id_ServentiaCargo = usuarioDt.getId_ServentiaCargoUsuarioChefe();				
		
//        List listPendenciasEmentaVoto = this.consultarPendenciasVotoEmentaProcesso(pendenciaDt.getId_Processo(), Id_ServentiaCargo, pendenciaTipoCodigo, idAudienciaProcessoSessaoSegundoGrau, obFabricaConexao);
//        if (listPendenciasEmentaVoto != null && listPendenciasEmentaVoto.size() > 0){
//        	throw new MensagemException("Processo j� possui pend�ncia do tipo selecionado.");
//        }
		
		// Configurara Respons�vel
    	PendenciaResponsavelDt responsavel = new PendenciaResponsavelDt();
    	responsavel.setId_ServentiaCargo(Id_ServentiaCargo);
	    pendenciaDt.addResponsavel(responsavel);	    		    	
    	
    	if (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU || Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSESSOR_DESEMBARGADOR){
    		 //Verifica se processo j� possui um assistente de gabinete respons�vel, pois esse ser� respons�vel pela pend�ncia
            responsavel = new PendenciaResponsavelDt();
            ServentiaCargoDt serventiaCargoAssistente = new ProcessoResponsavelNe().getAssistenteGabineteResponsavelProcesso(pendenciaDt.getId_Processo(), usuarioDt.getId_Serventia(), obFabricaConexao);
            if (serventiaCargoAssistente != null) {
            	responsavel.setId_ServentiaCargo(serventiaCargoAssistente.getId());	
            	pendenciaDt.addResponsavel(responsavel);
            }  else {
            	//Captura o Distribuidor do Gabinete do Desembargador e adiciona como respons�vel pela pend�ncia
            	ServentiaCargoDt serventiaCargoDistribuidor = (new ServentiaCargoNe()).getDistribuidorGabinete(usuarioDt.getId_Serventia(), obFabricaConexao);
            	if(serventiaCargoDistribuidor == null) throw new MensagemException("N�o foi poss�vel identificar o Cargo de Distribuidor do Gabinete da serventia " + usuarioDt.getId_Serventia() + ".");
            	responsavel.setId_ServentiaCargo(serventiaCargoDistribuidor.getId());
            	pendenciaDt.addResponsavel(responsavel);
            }
    	} else if (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSISTENTE_GABINETE){
    		responsavel = new PendenciaResponsavelDt();
    		responsavel.setId_ServentiaCargo(Id_ServentiaCargoRelatorSessao);	
            pendenciaDt.addResponsavel(responsavel); 	
    	} 

        if (pendenciaDt.getResponsaveis() != null) {
            if (pendenciaDt.getResponsaveis().size() == 0) {
                throw new MensagemException("N�o foi poss�vel encontrar o cargo respons�vel pela pend�ncia.");
            } 
        }
        
        this.gerarPendencia(pendenciaDt, pendenciaDt.getResponsaveis(), null, pendenciaDt.getListaArquivos() , false, false, obFabricaConexao);
        
        return pendenciaDt;
    }
    
    public PendenciaDt consultarPendenciaVotoRelatorSessao(String idAudienciaProcessoSessaoSegundoGrau, String idServentiaCargoRelator, FabricaConexao obFabricaConexao) throws Exception {
    	PendenciaPs pendenciaPs = new PendenciaPs(obFabricaConexao.getConexao());
    	return pendenciaPs.consultarPendenciaVotoRelatorSessao(idAudienciaProcessoSessaoSegundoGrau, idServentiaCargoRelator);
    }
    
    public PendenciaDt consultarPendenciaEmentaRelatorSessao(String idAudienciaProcessoSessaoSegundoGrau, String idServentiaCargoRelator, FabricaConexao obFabricaConexao) throws Exception {
    	PendenciaPs pendenciaPs = new PendenciaPs(obFabricaConexao.getConexao());
    	return pendenciaPs.consultarPendenciaEmentaRelatorSessao(idAudienciaProcessoSessaoSegundoGrau, idServentiaCargoRelator);
    }
    
    public PendenciaDt consultarPendenciaVotoRedatorSessao(String idAudienciaProcessoSessaoSegundoGrau, String idServentiaCargoRedator, FabricaConexao obFabricaConexao) throws Exception {
    	PendenciaPs pendenciaPs = new PendenciaPs(obFabricaConexao.getConexao());
    	return pendenciaPs.consultarPendenciaVotoRedatorSessao(idAudienciaProcessoSessaoSegundoGrau, idServentiaCargoRedator);
    }
    
    public PendenciaDt consultarPendenciaEmentaRedatorSessao(String idAudienciaProcessoSessaoSegundoGrau, String idServentiaCargoRedator, FabricaConexao obFabricaConexao) throws Exception {
    	PendenciaPs pendenciaPs = new PendenciaPs(obFabricaConexao.getConexao());
    	return pendenciaPs.consultarPendenciaEmentaRedatorSessao(idAudienciaProcessoSessaoSegundoGrau, idServentiaCargoRedator);
    }
    
    public List consultarPendenciasVotoEmentaProcesso(String idProcesso, String idServentiaCargo, String pendenciaTipoCodigo, String idAudienciaProcessoSessaoSegundoGrau, FabricaConexao obFabricaConexao) throws Exception {
		
		PendenciaPs pendenciaPs = new PendenciaPs(obFabricaConexao.getConexao());
		
		List pendencias = pendenciaPs.consultarPendenciasVotoEmentaProcesso(idProcesso, idServentiaCargo, pendenciaTipoCodigo, idAudienciaProcessoSessaoSegundoGrau);

		return pendencias;
	}
    
    public List consultarPendenciasVotoEmentaProcessoPeloIdAudiProc(String idAudienciaProcessoSessaoSegundoGrau, String idServentiaCargo, String pendenciaTipoCodigo, FabricaConexao obFabricaConexao) throws Exception {
		// jvosantos - 02/09/2019 15:04 - Remover linha morta
 		List pendencias = consultarPendenciasAudienciaProcessoPorListaTipo(idAudienciaProcessoSessaoSegundoGrau, obFabricaConexao, Integer.valueOf(pendenciaTipoCodigo));

 		return pendencias;
 	}
    
    /**
	 * Verifica se uma determinada pendencia e publica
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 15/10/2008 16:37
	 * @param pendenciaDt
	 *            vo de pendencia
	 * @return boolean
	 * @throws Exception
	 */
    public boolean verificarPendenciaPublica(PendenciaDt pendenciaDt) throws Exception {
        // Primeiramente pesquisa os dados da pendencia
        pendenciaDt = this.consultarId(pendenciaDt.getId());

        // Verifica se tem o tipo
        if (pendenciaDt.getPendenciaTipoCodigo() == null) return false;

        // Verifica se o tipo codigo da pendencia e pendencia publica
        return pendenciaDt.getPendenciaTipoCodigo().equals(String.valueOf(PendenciaTipoDt.PUBLICACAO_PUBLICA));
    }


    /**
     * Consulta as publiacoes publicas com pagina��o
     * @author jesus Rodrigo
     * @param dataInicio
     *            data de inicio
     * @param dataFina,
     *            data de fim
     * @param id_Serventia
     *            id de uma serventia
     * @param texto
     *            para a pesquisa
     * @param posicao
     *            posicaionamento da pesquisa
     * @return List
     * @throws Exception
     */
    public List consultarTextoPublicaoPublica(String dataInicio, String dataFinal, String id_Serventia, String texto, String posicao) throws Exception {

        FabricaConexao obFabricaConexao = null;
        
        if (dataInicio != null && dataInicio.trim().equals("")) dataInicio = null;
        if (dataFinal != null && dataFinal.trim().equals("")) dataFinal = null;

        // limpo o texto preparando-o para a busca de palavras
        String stTextoLimpo[] = new ArquivoPalavraNe().limparTexto(texto).split(" ");

        List pendencias = null;

        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            // Deixa a data inicial no inicio do dia
            if (dataInicio != null) dataInicio += " 00:00:00";
            // Deixa a data final ate o fim do dia
            if (dataFinal != null) dataFinal += " 23:59:59";

            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            List tempList = obPersistencia.consultarTextoPublicacaoPublica(dataInicio, dataFinal, id_Serventia, stTextoLimpo, posicao);

            QuantidadePaginas = (Long) tempList.remove(tempList.size() - 1);
            //tempList.remove(tempList.size() - 1);

            pendencias = tempList;

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }

    /**
     * Consulta as publiacoes publicas
     * 
     * @author Ronneesley Moura Teles
     * @since 14/10/2008 10:52
     * @param dataInicio
     *            data de inicio
     * @param dataFina,
     *            data de fim
     * @param posicao
     *            posicaionamento da pesquisa
     * @return List
     * @throws Exception
     */
    public List consultarPublicaoPublica(String dataInicio, String dataFinal, String id_Serventia, String posicao) throws Exception {
        if (dataInicio != null && dataInicio.trim().equals("")) dataInicio = null;
        if (dataFinal != null && dataFinal.trim().equals("")) dataFinal = null;

        List pendencias = null;
        
        FabricaConexao obFabricaConexao = null;
        
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            // Deixa a data inicial no inicio do dia
            if (dataInicio != null) dataInicio += " 00:00:00";
            // Deixa a data final ate o fim do dia
            if (dataFinal != null) dataFinal += " 23:59:59";

            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            List tempList = obPersistencia.consultarPublicacaoPublica(dataInicio, dataFinal, id_Serventia, posicao);

            setQuantidadePaginas(tempList);

            pendencias = tempList;

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }

    /**
     * Consulta o arquivo tipo de publicacao
     * 
     * @author Ronneesley Moura Teles
     * @since 13/10/2008 15:16
     * @return ArquivoTipoDt
     * @throws Exception
     */
    public ArquivoTipoDt consultarArquivoTipoPublicacao() throws Exception {
        String id = this.consultarIdArquivoTipoPublicacao();

        ArquivoTipoNe arquivoTipoNe = new ArquivoTipoNe();
        ArquivoTipoDt arquivoTipoDt = arquivoTipoNe.consultarId(id);

        return arquivoTipoDt;
    }

    /**
     * Retorna o id de arquivo tipo para uma publicacao
     * 
     * @author Ronneesley Moura Teles
     * @since 13/10/2008 11:06
     * @return String
     * @throws Exception
     */
    public String consultarIdArquivoTipoPublicacao() throws Exception {
        String id = "-1";

        ArquivoTipoNe arquivoTipoNe = new ArquivoTipoNe();
        List ids = arquivoTipoNe.consultarPeloArquivoTipoCodigo(String.valueOf(ArquivoTipoDt.PUBLICACAO_PUBLICA));

        // Funcionamento atual, retorna o primeiro id de tipo de arquivo que
        // seja publicacao publica
        if (ids.size() > 0) id = (String) ids.get(0);

        return id;
    }

//    /**
//     * Encapsulamento para criar uma Publica��o a partir de uma conex�o j�
//     * existente. Ser� utilizado no caso de Finalizar uma Sess�o de 2� grau que
//     * deve criar uma publica��o.
//     * 
//     * @param arquivos,
//     *            arquivos inseridos na movimenta��o da sess�o
//     * @param usuarioDt,
//     *            usu�rio que est� movimentando uma sess�o
//     * @param logDt,
//     *            objeto com dados do log
//     * @param fabricaConexao,
//     *            conex�o ativa
//     * 
//     * @author msapaula
//     */
//    public boolean criarPublicacao(List arquivos, UsuarioDt usuarioDt, LogDt logDt, FabricaConexao fabricaConexao) throws Exception {
//        PendenciaDt pendenciaDt = new PendenciaDt();
//        pendenciaDt.setId_UsuarioLog(logDt.getId_Usuario());
//        pendenciaDt.setIpComputadorLog(logDt.getIpComputador());
//        return this.criarPublicacao(pendenciaDt, arquivos, usuarioDt, fabricaConexao);
//    }

    /**
     * Encapsulamento para criar uma publica��o quando a conex�o deve ser
     * iniciada
     * 
     * @param pendenciaDt,
     *            vo de pendencias
     * @param arquivos,
     *            mapeamento de arquivos [Codigo Arquivo, ArquivoDt]
     * @param usuarioDt,
     *            usuario de sesssao
     * 
     * @author msapaula
     */
    public void criarPublicacao(PendenciaDt pendenciaDt, List arquivos, UsuarioDt usuarioDt) throws Exception {
    	FabricaConexao obFabricaConexao = null;
    	obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
        obFabricaConexao.iniciarTransacao();
        try {
        	
        	Iterator itArquivos = arquivos.iterator();

  			ArquivoNe arquivoNe = new ArquivoNe();

  			LogDt logDt = new LogDt(usuarioDt.getId(), usuarioDt.getIpComputadorLog());
  			//salvo todos os arquivos que ser�o publicados
  			while (itArquivos.hasNext()) {
   				// Pega o arquivo da lista
   				ArquivoDt arquivo = (ArquivoDt) itArquivos.next();

   				// Inseri o novo arquivo
   				arquivoNe.inserir(arquivo, logDt, obFabricaConexao);
  			}
        	
        	
        	this.salvarPublicacao(pendenciaDt, arquivos, usuarioDt, logDt , obFabricaConexao);
            obFabricaConexao.finalizarTransacao();
        } catch (Exception e) {
             obFabricaConexao.cancelarTransacao();
            throw e;
        } finally {
             obFabricaConexao.fecharConexao();
        }      
    }

//    /**
//     * Cria uma publicacao, adicionando seus respectivos arquivos
//     * 
//     * @author Ronneesley Moura Teles
//     * @since 13/10/2008 10:12
//     * @param pendenciaDt,
//     *            vo de pendencias
//     * @param arquivos,
//     *            mapeamento de arquivos [Codigo Arquivo, ArquivoDt]
//     * @param usuarioDt,
//     *            usuario de sesssao
//     * @return boolean
//     * @throws Exception
//     */
//    private boolean criarPublicacao(PendenciaDt pendenciaDt, List arquivos, UsuarioDt usuarioDt, FabricaConexao obFabricaConexao) throws Exception {
//
//    	if (arquivos == null || arquivos.size() == 0){
//        	throw new MensagemException("N�o foi poss�vel criar publica��o, pois a lista de arquivos est� vazia.");
//        }
//                                    
//        // Configura a pendencia de publicacao
//        pendenciaDt.setPendenciaTipoCodigo(String.valueOf(PendenciaTipoDt.PUBLICACAO_PUBLICA));
//        pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.Cumprida));
//        pendenciaDt.setId_UsuarioCadastrador(usuarioDt.getId_UsuarioServentia());
//        pendenciaDt.setId_UsuarioFinalizador(usuarioDt.getId_UsuarioServentia());
//        Date data = new Date();
//        pendenciaDt.setDataFim(Funcoes.DataHora(data));
//        pendenciaDt.setDataVisto(Funcoes.DataHora(data));
//
//        PendenciaResponsavelDt pendenciaResponsavelDt = new PendenciaResponsavelDt();
//        pendenciaResponsavelDt.setId_Serventia(usuarioDt.getId_Serventia());
//
//        pendenciaDt.addResponsavel(pendenciaResponsavelDt);
//
//        // Inseri a pendencia
//        this.gerarPendencia(pendenciaDt, obFabricaConexao);
//        
//        LogDt logDt = new LogDt();
//        logDt.setId_Usuario(usuarioDt.getId());
//        logDt.setIpComputadorLog(usuarioDt.getIpComputadorLog());
//
//        PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
//        pendenciaArquivoNe.inserirArquivosPublicacao(pendenciaDt, arquivos, true, logDt, obFabricaConexao);
//        
//        //� necess�rio finalizar a pend�ncia de publica��o para 
//        //evitar inconsist�ncia e j� inseri-la na tabela PEND_FINAL
//        this.finalizarPublicacao(pendenciaDt, usuarioDt, obFabricaConexao);
//      
//        return false;
//    }

    /**
     * Inserir arquivos
     * 
     * @author Ronneesley Moura Teles
     * @since 15/12/2008 11:20
     * @param pendenciaDt
     *            pendencia em que sera inserido os arquivos
     * @param arquivos
     *            arquivos a serem inseridos
     * @param usuarioDt
     *            usuario que realiza a operacao
     * @return true, se inseriu com sucesso e false se nao conseguiu inserir
     * @throws Exception
     */
    public boolean salvaResolucao(PendenciaDt pendenciaDt, List arquivos, boolean resposta, UsuarioDt usuarioDt, ComarcaDt comarcaDt, ServentiaDt serventiaExpedirDt, ServentiaTipoDt serventiaTipoExpedirDt) throws Exception {
        FabricaConexao obFabricaConexao = null;
        // Verifica se o usuario que esta inserindo o arquivo ja e um responsavel
        PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
        boolean resultado = false;
        try {
        	obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            obFabricaConexao.iniciarTransacao();

            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

            // Se nao for responsavel inclui-o como um
            if (!pendenciaResponsavelNe.eUsuarioResponsavel(pendenciaDt, usuarioDt)) {
                PendenciaResponsavelDt responsavel = new PendenciaResponsavelDt();

                if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equals(""))
                    responsavel.setId_UsuarioResponsavel(usuarioDt.getId_UsuarioServentiaChefe());
                else
                    responsavel.setId_UsuarioResponsavel(usuarioDt.getId_UsuarioServentia());

                responsavel.setId_Pendencia(pendenciaDt.getId());
                responsavel.setId_UsuarioLog(usuarioDt.getId());
                responsavel.setIpComputadorLog(usuarioDt.getIpComputadorLog());
                pendenciaResponsavelNe.inserir(responsavel, obFabricaConexao);
            }
            
            LogDt logDt = new LogDt();
            logDt.setId_Usuario(usuarioDt.getId());
            logDt.setIpComputadorLog(usuarioDt.getIpComputadorLog());
            
            
            if (pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.MANDADO && temCentralProjudiImplantada(usuarioDt.getId_Serventia()) ){
            	PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
            	ArquivoDt arquivoDt = (ArquivoDt) arquivos.get(0);
            	pendenciaArquivoNe.montaArquivoPreAnaliseMandado(pendenciaDt, serventiaExpedirDt, arquivoDt);
            	resultado = pendenciaArquivoNe.inserirArquivos(pendenciaDt, arquivos, resposta, false, logDt, obFabricaConexao);
            }
            else if (pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.CARTA_PRECATORIA){
            	PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
            	ArquivoDt arquivoDt = (ArquivoDt) arquivos.get(0);
            	pendenciaArquivoNe.montaArquivoPreAnalisePrecatoria(comarcaDt,arquivoDt);
            	resultado = pendenciaArquivoNe.inserirArquivos(pendenciaDt, arquivos, resposta, false, logDt, obFabricaConexao);
            } else {
            	PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
            	ArquivoDt arquivoDt = (ArquivoDt) arquivos.get(0);
            	pendenciaArquivoNe.montaArquivoPreAnalisePendencias(serventiaExpedirDt, arquivoDt, serventiaTipoExpedirDt);
            	resultado = pendenciaArquivoNe.inserirArquivos(pendenciaDt, arquivos, resposta, false, logDt, obFabricaConexao);
            }
            
            //altera o status da pendencia para pr�-anlisadas
            if (Funcoes.StringToInt(pendenciaDt.getPendenciaStatusCodigo()) == PendenciaStatusDt.ID_EM_ANDAMENTO) {
            	pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_PRE_ANALISADA));
            	obPersistencia.AlterarStatusPendencia(pendenciaDt);
            } else if (Funcoes.StringToInt(pendenciaDt.getPendenciaTipoCodigo()) == PendenciaTipoDt.INTIMACAO_VIA_TELEFONE &&
            		Funcoes.StringToInt(pendenciaDt.getPendenciaStatusCodigo()) == PendenciaStatusDt.ID_AGUARDANDO_RETORNO) {
            	pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_PRE_ANALISADA));
            	obPersistencia.AlterarStatusPendencia(pendenciaDt);
            }

            obFabricaConexao.finalizarTransacao();

            return resultado;

        } catch (Exception e) {
             obFabricaConexao.cancelarTransacao();
            throw e;
        } finally {
           obFabricaConexao.fecharConexao();
        }
    }

//    /**
//     * Inserir varios arquivos em uma pendencia Este metodo foi criado para
//     * facilitar a manipulacao, tendo em vista que o dwr utiliza-se de um ID
//     * (Codigo Arquivo) ficticio para manipular os arquivos da lista
//     * 
//     * OBS: O codigo que e passado no map nao sera utilizado para gravar o
//     * arquivo
//     * 
//     * @author Ronneesley Moura Teles
//     * @since 16/09/2008 09:26
//     * @param pendenciaDt
//     *            pojo de pendencia
//     * @param arquivos
//     *            mapeamento de arquivos [Codigo Arquivo, ArquivoDt]
//     * @param resposta
//     *            se os arquivos sao de resposta
//     * @param usuarioDt
//     *            usuario que esta inserindo os arquivos
//     * @return boolean
//     * @throws Exception
//     */
//    
//    public void inserirArquivos(List arquivos, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
//		ArquivoNe arquivoNe = new ArquivoNe();
//		for (int i = 0; i < arquivos.size(); i++) {
//			ArquivoDt arquivoDt = (ArquivoDt) arquivos.get(i);
//			// Se o arquivo j� tem id n�o � necess�rio salvar novamente
//			if (arquivoDt.getId().equalsIgnoreCase("")) {
//				arquivoDt.setConteudoSemRecibo(arquivoDt.conteudoBytes());
//				byte[] teste = null;
//				arquivoDt.setArquivo(teste);
//				arquivoDt.setId_UsuarioLog(logDt.getId_Usuario());
//				arquivoDt.setIpComputadorLog(logDt.getIpComputador());
//				// Salva arquivo
//				arquivoNe.inserir(arquivoDt, logDt, obFabricaConexao);
//			} else if (arquivoDt.conteudoBytes() == null) {
//				arquivoDt.setConteudoSemRecibo(arquivoNe.consultarConteudoArquivo(arquivoDt, obFabricaConexao));
//			}
//		}
//	}
//    
    public boolean inserirArquivos(PendenciaDt pendenciaDt, List arquivos, boolean resposta, UsuarioDt usuarioDt, FabricaConexao fabrica) throws Exception {
        FabricaConexao obFabricaConexao = null;
        // Verifica se o usuario que esta inserindo o arquivo ja e um
        // responsavel
        PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
        boolean resultado = false;
        try {
            if (fabrica == null) {
                obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
                obFabricaConexao.iniciarTransacao();
            } else
                obFabricaConexao = fabrica;            

            // Se nao for responsavel inclui-o como um
            if (!pendenciaResponsavelNe.eUsuarioResponsavel(pendenciaDt, usuarioDt)) {
                PendenciaResponsavelDt responsavel = new PendenciaResponsavelDt();

                if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equals(""))
                    responsavel.setId_UsuarioResponsavel(usuarioDt.getId_UsuarioServentiaChefe());
                else
                    responsavel.setId_UsuarioResponsavel(usuarioDt.getId_UsuarioServentia());

                responsavel.setId_UsuarioLog(usuarioDt.getId());
                responsavel.setIpComputadorLog(usuarioDt.getIpComputadorLog());
                responsavel.setId_Pendencia(pendenciaDt.getId());
                pendenciaResponsavelNe.inserir(responsavel, obFabricaConexao);
            }
            
            LogDt logDt = new LogDt();
            logDt.setId_Usuario(usuarioDt.getId());
            logDt.setIpComputadorLog(usuarioDt.getIpComputadorLog());
            
            PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
            resultado = pendenciaArquivoNe.inserirArquivos(pendenciaDt, arquivos, resposta, false, logDt, obFabricaConexao);
            
            if (fabrica == null) obFabricaConexao.finalizarTransacao();

            return resultado;

        } catch (Exception e) {
            if (fabrica == null) obFabricaConexao.cancelarTransacao();
            throw e;
        } finally {
            if (fabrica == null) obFabricaConexao.fecharConexao();
        }

    }

    /**
     * Consultar a pendencia pai de uma determinada pendencia, esta funcao
     * podera ser otimizada futuramente pelo ponto de vista que ha como fazer
     * uma consulta unica para a necessidade
     * 
     * @author Ronneesley Moura Teles
     * @since 27/08/2008 17:32 Alteracoes Carregar pai, somente quando o numero
     *        do pai estiver preenchido
     * @author Ronneesley Moura Teles
     * @since 01/09/2008 14:11
     * @param id_pendencia
     *            id da pendencia que se deseja encontrar a pendencia pai
     * @return PendenciaDt
     * @throws Exception
     */
    public PendenciaDt consultarPendenciaPai(String id_pendencia) throws Exception {
        PendenciaDt pendenciaDt = this.consultarId(id_pendencia);
        
        if (pendenciaDt == null)
        	pendenciaDt = this.consultarFinalizadaId(id_pendencia);

        if (pendenciaDt.getId_PendenciaPai() != null && !pendenciaDt.getId_PendenciaPai().equals(""))
            pendenciaDt = this.consultarFinalizadaId(pendenciaDt.getId_PendenciaPai());
        else
            pendenciaDt = null;

        return pendenciaDt;
    }
    
	public PendenciaDt consultarFinalizadaId(String id_pendencia ) throws Exception {

		PendenciaDt dtRetorno=null;
		//////System.out.println("..ne-ConsultaId_Pendencia" );
		FabricaConexao obFabricaConexao = null;
		
		try{ 
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			PendenciaPs obPersistencia = new PendenciaPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarFinalizadaId(id_pendencia );
			if( dtRetorno != null )
				obDados.copiar(dtRetorno);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	public PendenciaDt consultarFinalizadaId(String id_pendencia, FabricaConexao obFabricaConexao) throws Exception {
		PendenciaDt dtRetorno=null;
		PendenciaPs obPersistencia = new PendenciaPs(obFabricaConexao.getConexao());
		dtRetorno = obPersistencia.consultarFinalizadaId(id_pendencia );
		if( dtRetorno != null )
			obDados.copiar(dtRetorno);
		return dtRetorno;
	}

    /**
     * Consultar a pendencia por id
     * @param id_pendencia
     *            id da pendencia que se deseja encontrar a pendencia pai
     * @param usuarioNe
     *            usuarioNe para gerar o hash da pendencia
     * @return PendenciaDt
     * @throws Exception
     */
    public PendenciaDt consultarPendenciaId(String id_pendencia, UsuarioNe usuarioNe) throws Exception, MensagemException {
    	PendenciaDt pendenciaDt = this.consultarId(id_pendencia);
    	if(pendenciaDt == null) {
        	throw new MensagemException("Erro: Favor tentar novamente, pois a pend�ncia pode ter sido finalizada.");
        }
        pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
        return pendenciaDt;
    }
    
    /**
     * Consultar a pendencia por id
     * @param id_pendencia
     *            id da pendencia que se deseja encontrar a pendencia pai
     * @param usuarioNe
     *            usuarioNe para gerar o hash da pendencia
     * @return PendenciaDt
     * @throws Exception
     */
    public PendenciaDt consultarPendenciaFinalizadaId(String id_pendencia, UsuarioNe usuarioNe) throws Exception {
        PendenciaDt pendenciaDt = this.consultarFinalizadaId(id_pendencia);
        if(pendenciaDt != null){
        	pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
        }
        return pendenciaDt;
    }

    /**
     * Grava o log da operacao na pendencia Depende que ja tenha uma conexao
     * aberta
     * 
     * @author Ronneesley Moura Teles
     * @since 06/06/2008 10:22
     * @param dados
     *            dados da pendencia
     * @param conexao
     *            fabrica de conexao utilizada
     * @throws Exception
     */
    private boolean gravarLog(PendenciaDt dados, FabricaConexao conexao) throws Exception {
        LogDt obLogDt;

        if (dados.getId().trim().equalsIgnoreCase("") || dados.getId() == null) {
            obLogDt = new LogDt("Pendencia", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
        } else {
            obLogDt = new LogDt("Pendencia", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());
        }

        this.obLog.salvar(obLogDt, conexao);

        return true;
    }

    /**
     * Cria uma pendencia para verificar o recebimento de um novo processo
     * 
     * @author Ronneesley Moura Teles
     * @since 10/06/2008 14:01
     * @param processoDt, bean do processo
     * @param id_UsuarioServentia, id do usuario da serventia
     * @param id_ServentiaResponsavel, id da serventia responsavel
     * @param obFabricaConexao, fabrica de conexao para continar uma trasacao existente
     * @return void
     * @throws Exception
     */
    public void gerarPendenciaVerificarNovoProcesso(ProcessoDt processoDt, String id_UsuarioServentia, String id_ServentiaResponsavel, String id_Movimentacao, List arquivosVincular, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
    	
    	ArquivoDt arquivoDt = new ArquivoNe().salvarArquivoVerificarCpfParte(processoDt, obFabricaConexao);
    	if (arquivoDt != null) arquivosVincular.add(arquivoDt);
    	
        this.gerarPendenciaProcesso(processoDt.getId(), arquivosVincular, PendenciaTipoDt.VERIFICAR_NOVO_PROCESSO, id_Movimentacao, id_UsuarioServentia, "", "", "", PendenciaStatusDt.ID_EM_ANDAMENTO, "", id_ServentiaResponsavel, "", "", processoDt.getId_ProcessoPrioridade(), null, logDt, obFabricaConexao);

    }
    
    /**
     * Cria uma pendencia para verificar o recebimento de um novo processo
     * 
     * @author lsbernardes
     * @since 03/09/2015 14:01
     * @param processoDt, bean do processo
     * @param id_UsuarioServentia, id do usuario da serventia
     * @param id_ServentiaResponsavel, id da serventia responsavel
     * @param obFabricaConexao, fabrica de conexao para continar uma trasacao existente
     * @return void
     * @throws Exception
     */
    public void gerarPendenciaVerificarGuiaPendente(ProcessoDt processoDt, String id_UsuarioServentia, String id_ServentiaResponsavel, String id_Movimentacao, List arquivosVincular, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
    	
        this.gerarPendenciaProcesso(processoDt.getId(), arquivosVincular, PendenciaTipoDt.VERIFICAR_GUIA_PENDENTE, id_Movimentacao, id_UsuarioServentia, "", "", "", PendenciaStatusDt.ID_EM_ANDAMENTO, "", id_ServentiaResponsavel, "", "", processoDt.getId_ProcessoPrioridade(), null, logDt, obFabricaConexao);

    }
    
    /**
     * Cria uma pendencia para verificar o recebimento de um novo processo tipo precat�ria
     * 
     * @author lsbernardes
     * @param processoDt, bean do processo
     * @param id_UsuarioServentia, id do usuario da serventia
     * @param id_ServentiaResponsavel, id da serventia responsavel
     * @param obFabricaConexao, fabrica de conexao para continar uma trasacao existente
     * @return void
     * @throws Exception
     */
    public void gerarPendenciaVerificarNovaPrecatoria(ProcessoDt processoDt, String id_UsuarioServentia, String id_ServentiaResponsavel, String id_Movimentacao, List arquivosVincular, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
    	
    	ArquivoDt arquivoDt = new ArquivoNe().salvarArquivoVerificarCpfParte(processoDt, obFabricaConexao);
    	if (arquivoDt != null) arquivosVincular.add(arquivoDt);
    	
        this.gerarPendenciaProcesso(processoDt.getId(), arquivosVincular, PendenciaTipoDt.VERIFICAR_NOVA_PRECATORIA, id_Movimentacao, id_UsuarioServentia, "", "", "", PendenciaStatusDt.ID_EM_ANDAMENTO, "", id_ServentiaResponsavel, "", "", processoDt.getId_ProcessoPrioridade(), null, logDt, obFabricaConexao);

    }
    
    /**
     * Cria uma pendencia para verificar andamento no processo deprecante
     * 
     * @author lsbernardes
     * @param processoDt, bean do processo
     * @param id_UsuarioServentia, id do usuario da serventia
     * @param id_ServentiaResponsavel, id da serventia responsavel
     * @param obFabricaConexao, fabrica de conexao para continar uma trasacao existente
     * @return void
     * @throws Exception
     */
    public void gerarPendenciaVerificarPrecatoria(ProcessoDt processoDt, String id_UsuarioServentia, String id_ServentiaResponsavel, String id_Movimentacao, List arquivosVincular, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
    	
    	ArquivoDt arquivoDt = new ArquivoNe().salvarArquivoVerificarCpfParte(processoDt, obFabricaConexao);
    	if (arquivoDt != null) arquivosVincular.add(arquivoDt);
    	
        this.gerarPendenciaProcesso(processoDt.getId(), arquivosVincular, PendenciaTipoDt.VERIFICAR_PRECATORIA, id_Movimentacao, id_UsuarioServentia, "", "", "", PendenciaStatusDt.ID_EM_ANDAMENTO, "", id_ServentiaResponsavel, "", "", processoDt.getId_ProcessoPrioridade(), null, logDt, obFabricaConexao);

    }
    
    /**
     * Cria uma pendencia para Marcar Audi�ncia em processo
     * 
     * @author msapaula
     * @param processoDt, bean do processo
     * @param id_UsuarioServentia, id do usuario da serventia
     * @param id_ServentiaResponsavel, id da serventia responsavel
     * @param id_Movimentacao, movimenta��o vinculada a pend�ncia
     * @param processoPrioridade, prioridade do processo
     * @param arquivosVincular, lista de arquivos a ser vinculado com pend�ncia
     * @param logDt, objeto com dados do log
     * @param obFabricaConexao, fabrica de conexao para continar uma transacao existente
     * @return void
     * @throws Exception
     */
    public void gerarPendenciaMarcarAudienciaProcesso(ProcessoDt processoDt, String id_UsuarioServentia, String id_ServentiaResponsavel, String id_Movimentacao, String processoPrioridade, List arquivosVincular, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {

    	this.gerarPendenciaProcesso(processoDt.getId(), arquivosVincular, PendenciaTipoDt.MARCAR_AUDIENCIA, id_Movimentacao, id_UsuarioServentia, "", "", "", PendenciaStatusDt.ID_EM_ANDAMENTO, "", id_ServentiaResponsavel, "", "", processoPrioridade, null, logDt, obFabricaConexao);
    }
    
    /**
     * Cria uma pendencia para Averba��o de Custas em processo
     * 
     * @author lsbernardes
     * @param processoDt, bean do processo
     * @param id_UsuarioServentia, id do usuario da serventia
     * @param id_ServentiaResponsavel, id da serventia responsavel
     * @param id_Movimentacao, movimenta��o vinculada a pend�ncia
     * @param processoPrioridade, prioridade do processo
     * @param arquivosVincular, lista de arquivos a ser vinculado com pend�ncia
     * @param logDt, objeto com dados do log
     * @param obFabricaConexao, fabrica de conexao para continar uma transacao existente
     * @return void
     * @throws Exception
     */
    public void gerarPendenciaAverbacaoCustas(ProcessoDt processoDt, String id_UsuarioServentia, String id_ServentiaResponsavel, String id_Movimentacao, String processoPrioridade, List arquivosVincular, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {

    	this.gerarPendenciaProcesso(processoDt.getId(), arquivosVincular, PendenciaTipoDt.AVERBACAO_CUSTAS, id_Movimentacao, id_UsuarioServentia, "", "", "", PendenciaStatusDt.ID_EM_ANDAMENTO, "", id_ServentiaResponsavel, "", "", processoPrioridade, null, logDt, obFabricaConexao);
    }

	/**
	 * Cria uma pendencia para Marcar Audi�ncia Concilia��o CEJUSC
	 * 
	 * @author lsbernardes
	 * @param processoDt, bean do processo
	 * @param id_UsuarioServentia, id do usuario da serventia
	 * @param id_ServentiaResponsavel, id da serventia responsavel
	 * @param id_Movimentacao, movimenta��o vinculada a pend�ncia
	 * @param processoPrioridade, prioridade do processo
	 * @param arquivosVincular, lista de arquivos a ser vinculado com pend�ncia
	 * @param logDt, objeto com dados do log
	 * @param obFabricaConexao, fabrica de conexao para continar uma transacao existente
	 * @return void
	 * @throws Exception
	 */
    public void gerarPendenciaMarcarAudienciaConciliacaoCEJUSC(ProcessoDt processoDt, String id_UsuarioServentia, String id_ServentiaResponsavel, String id_Movimentacao, String processoPrioridade, List arquivosVincular, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		  // Configura a pendencia para ser persistida
	    PendenciaDt pendencia = new PendenciaDt();
	
	    pendencia.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_EM_ANDAMENTO));
	    pendencia.setPendenciaTipoCodigo(String.valueOf(PendenciaTipoDt.MARCAR_AUDIENCIA_CONCILIACAO_CEJUSC));
	    if (processoPrioridade != null && processoPrioridade.length() > 0) 
	    	pendencia.setProcessoPrioridadeCodigo(String.valueOf(processoPrioridade));
	    pendencia.setId_Movimentacao(id_Movimentacao);
	    pendencia.setId_Processo(processoDt.getId());
	    // Todas as pend�ncias de processo devem ter DataVisto setada
	    pendencia.setDataVisto(Funcoes.DataHora(new Date()));
	    pendencia.setId_UsuarioCadastrador(id_UsuarioServentia);
	    pendencia.setId_UsuarioLog(logDt.getId_Usuario());
	    pendencia.setIpComputadorLog(logDt.getIpComputador());
	
	    PendenciaResponsavelDt responsavel = new PendenciaResponsavelDt();
	    ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();
        ServentiaDt CEJUSC = serventiaRelacionadaNe.consultarServentiaPreprocessualRelacionada(processoDt.getId_Serventia());
	    
	    if (CEJUSC != null) {
	        // Se encontrou uma serventia relacionada, essa ser� a respons�vel pela pend�ncia
	        responsavel.setId_Serventia(CEJUSC.getId());	
	    } else {
	    	throw new MensagemException(ServentiaDt.MENSAGEM_AUSENCIA_SERVENTIA_RELACIONADA_CEJUSC);
	    }
	    
	    //adiciona responsavel
	    pendencia.addResponsavel(responsavel);
	    // Gera a pendencia
	    this.gerarPendencia(pendencia, arquivosVincular, false, obFabricaConexao);
    
    }
    
    /**
	 * Cria uma pendencia para Marcar Audi�ncia Media��o CEJUSC
	 * 
	 * @author lsbernardes
	 * @param processoDt, bean do processo
	 * @param id_UsuarioServentia, id do usuario da serventia
	 * @param id_ServentiaResponsavel, id da serventia responsavel
	 * @param id_Movimentacao, movimenta��o vinculada a pend�ncia
	 * @param processoPrioridade, prioridade do processo
	 * @param arquivosVincular, lista de arquivos a ser vinculado com pend�ncia
	 * @param logDt, objeto com dados do log
	 * @param obFabricaConexao, fabrica de conexao para continar uma transacao existente
	 * @return void
	 * @throws Exception
	 */
    public void gerarPendenciaMarcarAudienciaMediacaoCEJUSC(ProcessoDt processoDt, String id_UsuarioServentia, String id_ServentiaResponsavel, String id_Movimentacao, String processoPrioridade, List arquivosVincular, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		  // Configura a pendencia para ser persistida
	    PendenciaDt pendencia = new PendenciaDt();
	
	    pendencia.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_EM_ANDAMENTO));
	    pendencia.setPendenciaTipoCodigo(String.valueOf(PendenciaTipoDt.MARCAR_AUDIENCIA_MEDIACAO_CEJUSC));
	    if (processoPrioridade != null && processoPrioridade.length() > 0) 
	    	pendencia.setProcessoPrioridadeCodigo(String.valueOf(processoPrioridade));
	    pendencia.setId_Movimentacao(id_Movimentacao);
	    pendencia.setId_Processo(processoDt.getId());
	    // Todas as pend�ncias de processo devem ter DataVisto setada
	    pendencia.setDataVisto(Funcoes.DataHora(new Date()));
	    pendencia.setId_UsuarioCadastrador(id_UsuarioServentia);
	    pendencia.setId_UsuarioLog(logDt.getId_Usuario());
	    pendencia.setIpComputadorLog(logDt.getIpComputador());
	
	    PendenciaResponsavelDt responsavel = new PendenciaResponsavelDt();
	    ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();
        ServentiaDt CEJUSC = serventiaRelacionadaNe.consultarServentiaPreprocessualRelacionada(processoDt.getId_Serventia());
	    
	    if (CEJUSC != null) {
	        // Se encontrou uma serventia relacionada, essa ser� a respons�vel pela pend�ncia
	        responsavel.setId_Serventia(CEJUSC.getId());	
	    } else {
	    	throw new MensagemException(ServentiaDt.MENSAGEM_AUSENCIA_SERVENTIA_RELACIONADA_CEJUSC);
	    }
	    
	    //adiciona responsavel
	    pendencia.addResponsavel(responsavel);
	    // Gera a pendencia
	    this.gerarPendencia(pendencia, arquivosVincular, false, obFabricaConexao);
    
    }
    
    /**
	 * Cria uma pendencia para Marcar Audi�ncia Concilia��o DPVAT CEJUSC
	 * 
	 * @author mmgomes
	 * @param processoDt, bean do processo
	 * @param id_UsuarioServentia, id do usuario da serventia
	 * @param id_ServentiaResponsavel, id da serventia responsavel
	 * @param id_Movimentacao, movimenta��o vinculada a pend�ncia
	 * @param processoPrioridade, prioridade do processo
	 * @param arquivosVincular, lista de arquivos a ser vinculado com pend�ncia
	 * @param logDt, objeto com dados do log
	 * @param obFabricaConexao, fabrica de conexao para continar uma transacao existente
	 * @return void
	 * @throws Exception
	 */
    public void gerarPendenciaMarcarAudienciaConciliacaoCEJUSCDPVAT(ProcessoDt processoDt, String id_UsuarioServentia, String id_ServentiaResponsavel, String id_Movimentacao, String processoPrioridade, List arquivosVincular, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		  // Configura a pendencia para ser persistida
	    PendenciaDt pendencia = new PendenciaDt();
	
	    pendencia.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_EM_ANDAMENTO));
	    pendencia.setPendenciaTipoCodigo(String.valueOf(PendenciaTipoDt.MARCAR_AUDIENCIA_CONCILIACAO_CEJUSC_DPVAT));
	    if (processoPrioridade != null && processoPrioridade.length() > 0) 
	    	pendencia.setProcessoPrioridadeCodigo(String.valueOf(processoPrioridade));
	    pendencia.setId_Movimentacao(id_Movimentacao);
	    pendencia.setId_Processo(processoDt.getId());
	    // Todas as pend�ncias de processo devem ter DataVisto setada
	    pendencia.setDataVisto(Funcoes.DataHora(new Date()));
	    pendencia.setId_UsuarioCadastrador(id_UsuarioServentia);
	    pendencia.setId_UsuarioLog(logDt.getId_Usuario());
	    pendencia.setIpComputadorLog(logDt.getIpComputador());
	
	    PendenciaResponsavelDt responsavel = new PendenciaResponsavelDt();
	    ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();
        ServentiaDt CEJUSC = serventiaRelacionadaNe.consultarServentiaPreprocessualRelacionada(processoDt.getId_Serventia());
	    
	    if (CEJUSC != null) {
	        // Se encontrou uma serventia relacionada, essa ser� a respons�vel pela pend�ncia
	        responsavel.setId_Serventia(CEJUSC.getId());	
	    } else {
	    	throw new MensagemException(ServentiaDt.MENSAGEM_AUSENCIA_SERVENTIA_RELACIONADA_CEJUSC);
	    }
	    
	    //adiciona responsavel
	    pendencia.addResponsavel(responsavel);
	    // Gera a pendencia
	    this.gerarPendencia(pendencia, arquivosVincular, false, obFabricaConexao);
    
    }
    
    /**
     * Cria uma pendencia para Oficio de notifica��o
     * 
     * @author lsbernardes
     * @param processoDt, bean do processo
     * @param id_UsuarioServentia, id do usuario da serventia
     * @param id_ServentiaResponsavel, id da serventia responsavel
     * @param id_Movimentacao, movimenta��o vinculada a pend�ncia
     * @param processoPrioridade, prioridade do processo
     * @param arquivosVincular, lista de arquivos a ser vinculado com pend�ncia
     * @param logDt, objeto com dados do log
     * @param obFabricaConexao, fabrica de conexao para continar uma transacao existente
     * @return void
     * @throws Exception
     */
    public void gerarPendenciaOficioNotificacaoProcesso(ProcessoDt processoDt, String id_UsuarioServentia, String id_ServentiaResponsavel, String id_Movimentacao, String processoPrioridade, List arquivosVincular, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {

    	this.gerarPendenciaProcesso(processoDt.getId(), arquivosVincular, PendenciaTipoDt.OFICIO_COMUNICATORIO, id_Movimentacao, id_UsuarioServentia, "", "", "", PendenciaStatusDt.ID_EM_ANDAMENTO, "", id_ServentiaResponsavel, "", "", processoPrioridade, null, logDt, obFabricaConexao);
    }

//    /**
//     N�u usar a conclus�o generica - use concluso decis�o       
//     * M�todo que gera pend�ncia do tipo Conclusao Gen�rica. Esse m�todo ser�
//     * utilizado quando o processo for concluso no momento do cadastro.
//     * Internamente, chama classe Movimentacao para gerar movimenta��o
//     * correspondente.
//     * 
//     * @author Ronneesley Moura Teles
//     * @since 09/06/2008 08:54
//     * @param processoDt
//     *            bean do processo
//     * @param id_UsuarioLog
//     *            id do usuario log
//     * @param id_UsuarioServentia
//     *            id do usuario da serventia
//     * @param arquivosVincular
//     *            lista de arquivos a serem vinculados com pend�ncia
//     * @param obFabricaConexao
//     *            fabrica de conexao para continuar uma transacao existente
//     * @return void
//     * @throws Exception
//     */
//    public void gerarConclusao Generica(ProcessoDt processoDt, String id_UsuarioServentia, String id_ServentiaCargo, String processoPrioridade, List arquivosVincular, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
//        MovimentacaoDt movimentacao = new MovimentacaoNe().gerarMovimentacaoConclusaoGenerica(processoDt.getId(), id_UsuarioServentia, new LogDt(processoDt.getId_UsuarioLog(), processoDt.getIpComputadorLog()), obFabricaConexao);
//
//        this.gerarPendenciaProcesso(processoDt.getId(), arquivosVincular, PendenciaTipoDt.CONCLUSO_GENERICO, movimentacao.getId(), id_UsuarioServentia, "", "", "", PendenciaStatusDt.EmAndamento, "", "", "", id_ServentiaCargo, processoPrioridade, null, logDt, obFabricaConexao);
//
//    }
    
    /**
     * M�todo que gera pend�ncia do tipo Conclusao Gen�rica. Esse m�todo ser�
     * utilizado quando o processo for concluso no momento do cadastro.
     * Internamente, chama classe Movimentacao para gerar movimenta��o
     * correspondente.
     * 
     * @author Jesus Rodrigo Corr�a
     * @since 29/11/2012
     * @param processoDt    *            bean do processo
     * @param id_UsuarioLog
     *            id do usuario log
     * @param id_UsuarioServentia
     *            id do usuario da serventia
     * @param arquivosVincular
     *            lista de arquivos a serem vinculados com pend�ncia
     * @param obFabricaConexao
     *            fabrica de conexao para continuar uma transacao existente
     * @return void
     * @throws Exception
     */
    public void gerarConclusaoDecisao(ProcessoDt processoDt, String id_UsuarioServentia, String id_ServentiaCargo, String processoPrioridade, List arquivosVincular, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
        MovimentacaoDt movimentacao = new MovimentacaoNe().gerarMovimentacaoConclusaoGenerica(processoDt.getId(), id_UsuarioServentia, new LogDt(processoDt.getId_UsuarioLog(), processoDt.getIpComputadorLog()), obFabricaConexao);

        this.gerarPendenciaProcesso(processoDt.getId(), arquivosVincular, PendenciaTipoDt.CONCLUSO_DECISAO, movimentacao.getId(), id_UsuarioServentia, "", "", "", PendenciaStatusDt.ID_EM_ANDAMENTO, "", "", "", id_ServentiaCargo, processoPrioridade, null, logDt, obFabricaConexao);

    }
    
    public void gerarConclusaoDecisaoPedidoAssistencia(ProcessoDt processoDt, String id_UsuarioServentia, String id_ServentiaCargo, String processoPrioridade, List arquivosVincular, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
        MovimentacaoDt movimentacao = new MovimentacaoNe().gerarMovimentacaoConclusaoGenerica(processoDt.getId(), id_UsuarioServentia, new LogDt(processoDt.getId_UsuarioLog(), processoDt.getIpComputadorLog()), obFabricaConexao);

        this.gerarPendenciaProcesso(processoDt.getId(), arquivosVincular, PendenciaTipoDt.CONCLUSO_DECISAO_PEDIDO_ASSISTENCIA, movimentacao.getId(), id_UsuarioServentia, "", "", "", PendenciaStatusDt.ID_EM_ANDAMENTO, "", "", "", id_ServentiaCargo, processoPrioridade, null, logDt, obFabricaConexao);

    }

    /**
     * M�todo que gera pend�ncia do tipo Conclusao. Internamente, consulta o
     * ServentiaCargo que ser� respons�vel pela conclus�o e chama classe
     * MovimentacaoNe para gerar movimenta��o correspondente.
     * 
     * @param pendenciaDt, bean de pendencia
     * @param processoDt, bean do processo
     * @param pendenciaTipoCodigo, tipo da conclus�o a ser gerada
     * @param arquivosVincular, lista de arquivos a serem vinculados com pend�ncia
     * @param obFabricaConexao, fabrica de conexao para continuar uma transacao existente
     * 
     * @author msapaula
     */
    public void gerarConclusao(PendenciaDt pendenciaDt, ProcessoDt processoDt, String pendenciaTipoCodigo, String idServentiaCargo, String cargoTipo, List arquivosVincular, String id_Classificador,LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
    	ProcessoResponsavelNe processoResponsavelNe = new ProcessoResponsavelNe();
    	ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
    	
    	//Consulta dados da serventia
		String codServentiaSubTipoCodigo = new ServentiaNe().consultarServentiaSubTipoCodigo(processoDt.getId_Serventia(), obFabricaConexao);
    	PendenciaResponsavelDt responsavel = new PendenciaResponsavelDt();    	
    	
    	//Se Serventia � do Tipo Segundo Grau todo o tratamento ser� diferenciado
    	if (ServentiaSubtipoDt.isSegundoGrau(codServentiaSubTipoCodigo)){
    		ServentiaCargoDt serventiaCargo = new ServentiaCargoDt();
    		
    		 if (Funcoes.StringToInt(pendenciaTipoCodigo) == PendenciaTipoDt.CONCLUSO_GENERICO_SEGUNDO_GRAU) {
    			 this.gerarPendenciaConclusaoGenericaSegundoGrau(pendenciaDt, processoDt, idServentiaCargo, cargoTipo, arquivosVincular, obFabricaConexao);
    		 
    		 } else {
    			 
    			 if (Funcoes.StringToInt(pendenciaTipoCodigo) == PendenciaTipoDt.CONCLUSO_PRESIDENTE_TJGO) {
        			 boolean possuiConclusaoPresidente = this.verificarExistenciaConclusoPresidenteTJGO(processoDt.getId(), obFabricaConexao);
        			 if (possuiConclusaoPresidente){
        				 throw new MensagemException("Processo j� possui Conclus�o para o Presidente TJGO!");
        			 } else {
        				 serventiaCargo = serventiaCargoNe.getPresidenteTribunalDeJustica(obFabricaConexao);
        				 if (!(serventiaCargo != null && serventiaCargo.getId().length() > 0)){
        					 throw new MensagemException("Presidente do Tribunal de Justi�a n�o encontrado.");
            			 } 
        			 }
    			 
    			 } else if (Funcoes.StringToInt(pendenciaTipoCodigo) == PendenciaTipoDt.CONCLUSO_VICE_PRESIDENTE_TJGO) {
    				 boolean possuiConclusaoVicePresidente = this.verificarExistenciaConclusoVicePresidenteTJGO(processoDt.getId(), obFabricaConexao);
        			 if (possuiConclusaoVicePresidente){
        				 throw new MensagemException("Processo j� possui Conclus�o para o Vice-Presidente TJGO!");
        			 } else {
        				 serventiaCargo = serventiaCargoNe.getVicePresidenteTribunalDeJustica(obFabricaConexao);
        				 if (!(serventiaCargo != null && serventiaCargo.getId().length() > 0)){
        					 throw new MensagemException("Vice-Presidente do Tribunal de Justi�a n�o encontrado.");
            			 } 
        			 }
    			 
    			 } else if (Funcoes.StringToInt(pendenciaTipoCodigo) == PendenciaTipoDt.CONCLUSO_PRESIDENTE) {
        			 serventiaCargo = serventiaCargoNe.getPresidenteSegundoGrau(processoDt.getId_Serventia(), obFabricaConexao);
        			 if (serventiaCargo != null && serventiaCargo.getId().length() > 0){
        				 responsavel.setId_ServentiaCargo(serventiaCargo.getId());
        				 responsavel.setServentiaCargo(serventiaCargo.getServentiaCargo());
        				 responsavel.setNomeUsuarioResponsavel(serventiaCargo.getNomeUsuario());
        			 } else {
        				 throw new MensagemException("Presidente n�o encontrado.");
        			 }
    			 
    			 } else {
    				// Se n�o for concluso ao presidente consulta o Relator do processo que ser� o respons�vel pela pend�ncia
		        	// Nesse caso consulta o tipo de ProcessoResponsavel com CargoTipo Relator
	    			serventiaCargo = processoResponsavelNe.consultarRelator2GrauConsideraSubstituicao(processoDt.getId(), processoDt.getId_Serventia(), codServentiaSubTipoCodigo , obFabricaConexao);   			
	    				    			
    				responsavel.setId_ServentiaCargo(serventiaCargo.getId());
    				responsavel.setServentiaCargo(serventiaCargo.getServentiaCargo());
    				responsavel.setNomeUsuarioResponsavel(serventiaCargo.getNomeUsuario());	    			
    			} 
    			 
    			if (verificaConclusoesAbertasProcessoServentiaCargo(processoDt.getId(), responsavel.getId_ServentiaCargo(), obFabricaConexao)) 
    				throw new MensagemException("Processo " + Funcoes.formataNumeroProcesso(processoDt.getProcessoNumero()) + " j� est� Concluso para " + responsavel.getServentiaCargo() + ". "); 			
    			
    			if (responsavel != null && responsavel.getId_ServentiaCargo() != null && responsavel.getId_ServentiaCargo().length()>0)
    				pendenciaDt.addResponsavel(responsavel);
    		 
	            //Verifica se processo j� possui um assistente de gabinete respons�vel, pois esse ser� respons�vel pela pend�ncia
	            responsavel = new PendenciaResponsavelDt();
	            ServentiaCargoDt serventiaCargoAssistente = processoResponsavelNe.getAssistenteAtivoGabineteResponsavelProcesso(processoDt.getId(), serventiaCargo.getId_Serventia(), obFabricaConexao);
	            if (serventiaCargoAssistente != null) {
	            	responsavel.setId_ServentiaCargo(serventiaCargoAssistente.getId());	
	            } else {
	            	//Captura o Distribuidor do Gabinete do Desembargador e adiciona como respons�vel pela pend�ncia
	            	//Entretanto, se for PLANT�O DE 2 GRAU, n�o precisa localizar distribuidor, j� que n�o existe esse cargo no plant�o.
	            	ServentiaDt serventiaDt = new ServentiaNe().consultarId(processoDt.getId_Serventia());
	            	if(!serventiaDt.getServentiaSubtipoCodigo().equals(String.valueOf(ServentiaSubtipoDt.PLANTAO_SEGUNDO_GRAU))){
		            	ServentiaCargoDt serventiaCargoDistribuidor = new ServentiaCargoNe().getDistribuidorGabinete(serventiaCargo.getId_Serventia(), obFabricaConexao);
		            	if(serventiaCargoDistribuidor == null) throw new MensagemException("N�o foi poss�vel identificar o Cargo de Distribuidor do Gabinete.");
		            	responsavel.setId_ServentiaCargo(serventiaCargoDistribuidor.getId());	
	            	}
	            }
	            if(responsavel.getId_ServentiaCargo() != null && !responsavel.getId_ServentiaCargo().equalsIgnoreCase("")) {
	            	pendenciaDt.addResponsavel(responsavel);
	            }
	            
	            // Gera movimenta��o do tipo Autos Conclusos
   		     	new MovimentacaoNe().gerarMovimentacaoConclusaoSegundoGrau(processoDt.getId(), pendenciaDt.getId_UsuarioCadastrador(), pendenciaTipoCodigo, new LogDt(pendenciaDt.getId_UsuarioLog(), pendenciaDt.getIpComputadorLog()), obFabricaConexao);
   		     	this.gerarPendencia(pendenciaDt, arquivosVincular, false, obFabricaConexao);
    		 }
    	} else if (ServentiaSubtipoDt.isUPJs(codServentiaSubTipoCodigo)){
			ServentiaCargoDt serventiaCargoDt = new ProcessoResponsavelNe().getServentiaCargoConclusaoUPJ(processoDt, pendenciaTipoCodigo, obFabricaConexao);    		
			if (serventiaCargoDt == null) {
				throw new MensagemException("N�o foi poss�vel identificar o Cargo solicitado.");
			}
			
//			ServentiaCargoDt serventia CargoDistribuidor = new ServentiaCargoNe().getDistribuidorGabinete(serventiaCargoDt.getId_Serventia(), obFabricaConexao);
//			if(serventiaCargoDistribuidor == null){ 
//        		throw new MensagemException("N�o foi poss�vel identificar o Cargo de Distribuidor do Gabinete.");
//        	}
			//ServentiaCargoDt serventiaCargoResponsavel = new ServentiaCargoNe().consultarServentiaCargpoDistribuicaoGabineteFluxo(serventiaCargoDt.getId_Serventia(), CargoTipoDt.DISTRIBUIDOR_GABINETE, obFabricaConexao);
//			if (serventiaCargoResponsavel==null) {
//				serventiaCargoResponsavel = new ServentiaCargoNe().consultarServentiaCargpoDistribuicaoGabineteFluxo(serventiaCargoDt.getId_Serventia(), CargoTipoDt.ASSISTENTE_GABINETE_FLUXO, obFabricaConexao);
//				if (serventiaCargoResponsavel==null) {
//					throw new MensagemException("N�o foi poss�vel determinar o Respons�vel no gabinete com fluxo.");
//				} 
//				gerarHistoricoPendencia = true;
//			}
				
			boolean gerarHistoricoPendencia = false;
			ServentiaCargoDt serventiaCargoResponsavel = null;
			if(processoDt.isSigiloso()) {
				serventiaCargoResponsavel = new ServentiaCargoNe().consultarServentiaCargpoDistribuicaoGabineteFluxo(serventiaCargoDt.getId_Serventia(), CargoTipoDt.JUIZ_UPJ, obFabricaConexao);		
				gerarHistoricoPendencia = true;
			}else {		
				 serventiaCargoResponsavel = new ServentiaCargoNe().consultarServentiaCargpoDistribuicaoGabineteFluxo(serventiaCargoDt.getId_Serventia(), CargoTipoDt.DISTRIBUIDOR_GABINETE, obFabricaConexao);
				if (serventiaCargoResponsavel==null) {
					serventiaCargoResponsavel = new ServentiaCargoNe().consultarServentiaCargpoDistribuicaoGabineteFluxo(serventiaCargoDt.getId_Serventia(), CargoTipoDt.ASSISTENTE_GABINETE_FLUXO, obFabricaConexao);
					if (serventiaCargoResponsavel==null) {
						serventiaCargoResponsavel = new ServentiaCargoNe().consultarServentiaCargpoDistribuicaoGabineteFluxo(serventiaCargoDt.getId_Serventia(), CargoTipoDt.JUIZ_UPJ, obFabricaConexao);							
					} 
					gerarHistoricoPendencia = true;
				}		
			}
			
			if (serventiaCargoResponsavel==null) {
				throw new MensagemException("N�o foi poss�vel determinar o Respons�vel no gabinete com fluxo.");							
			}				
		
			if (verificaConclusoesAbertas(processoDt.getId(), obFabricaConexao)) {
				throw new MensagemException("Processo " + Funcoes.formataNumeroProcesso(processoDt.getProcessoNumero()) + " j� est� concluso. ");
			}
			
        	responsavel.setId_ServentiaCargo(serventiaCargoResponsavel.getId());
        	pendenciaDt.addResponsavel(responsavel);
        	  
        	 // Gera movimenta��o do tipo Autos Conclusos
		    new MovimentacaoNe().gerarMovimentacaoConclusao(processoDt.getId(), pendenciaDt.getId_UsuarioCadastrador(), pendenciaTipoCodigo, new LogDt(pendenciaDt.getId_UsuarioLog(), pendenciaDt.getIpComputadorLog()), obFabricaConexao);
		    this.gerarPendencia(pendenciaDt, arquivosVincular, false, obFabricaConexao);
		    
		    //gera o historico do fluxo da pendencia
		    if(gerarHistoricoPendencia) {
				SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
	            String data_inicio = df.format(new Date());
				new PendenciaResponsavelHistoricoNe().salvarHistorioco(getIdPendencia(), data_inicio, serventiaCargoResponsavel.getId(), serventiaCargoResponsavel.getId_ServentiaGrupo(), logDt.getId_Usuario(), logDt.getIpComputador(), obFabricaConexao);
			}
            
    	} else {
    		 if (Funcoes.StringToInt(pendenciaTipoCodigo) == PendenciaTipoDt.CONCLUSO_GENERICO_SEGUNDO_GRAU) {
    			 this.gerarPendenciaConclusaoGenericaTurma(pendenciaDt, processoDt, idServentiaCargo, cargoTipo, arquivosVincular, obFabricaConexao);
    		 
    		 } else {
		        // Consulta o serventiaCargo que ser� o respons�vel pela pend�ncia de conclus�o nos casos de Varas e Turmas Recursais
    			ServentiaCargoDt serventiaCargoDt = new ProcessoResponsavelNe().getServentiaCargoConclusao(processoDt, pendenciaTipoCodigo, obFabricaConexao);    			
    			if (serventiaCargoDt == null) {
    				throw new MensagemException("N�o foi poss�vel identificar o Cargo solicitado.");
    			}
    			
    			if (verificaConclusoesAbertasProcessoServentiaCargo(processoDt.getId(), serventiaCargoDt.getId(), obFabricaConexao)) {
    				throw new MensagemException("Processo " + Funcoes.formataNumeroProcesso(processoDt.getProcessoNumero()) + " j� est� Concluso para " + serventiaCargoDt.getServentiaCargo() + ". ");
    			}
    			
		        responsavel.setId_ServentiaCargo(serventiaCargoDt.getId());
		        pendenciaDt.addResponsavel(responsavel);
		        
		        // Gera movimenta��o do tipo Autos Conclusos
			    new MovimentacaoNe().gerarMovimentacaoConclusao(processoDt.getId(), pendenciaDt.getId_UsuarioCadastrador(), pendenciaTipoCodigo, new LogDt(pendenciaDt.getId_UsuarioLog(), pendenciaDt.getIpComputadorLog()), obFabricaConexao);
			    this.gerarPendencia(pendenciaDt, arquivosVincular, false, obFabricaConexao);
    		}
    	}
    	
    	//ALTERAR CLASSIFICADOR DA CONCLUS�O***********************************************************************
    	this.alterarClassificadorPendencia(pendenciaDt.getId(), "", id_Classificador, logDt, obFabricaConexao);
    	//*********************************************************************************************************
    	
    }
    
    /**
     * M�todo que gera pend�ncia do tipo "Pedido Vista". Internamente, consulta o ServentiaCargo que ser� respons�vel 
     * pela pend�ncia de acordo com o Tipo de Cargo selecionado pelo usu�rio no momento de gerar a pend�ncia.
     * 
     * @param pendenciaDt, bean de pendencia
     * @param processoDt, bean do processo
     * @param cargoTipoCodigo, tipo do cargo a ser consultado
     * @param arquivosVincular, lista de arquivos a serem vinculados com pend�ncia
     * @param obFabricaConexao, fabrica de conexao para continuar uma transacao existente
     * 
     * @author msapaula
     */
    public void gerarPendenciaPedidoVistaSegundoGrau(PendenciaDt pendenciaDt, ProcessoDt processoDt, String idServentiaCargo, String cargoTipo, List arquivosVincular, FabricaConexao obFabricaConexao) throws Exception {
    	ServentiaCargoDt serventiaCargoDt = null;
        PendenciaResponsavelDt responsavel = new PendenciaResponsavelDt();
        
//        if (cargoTipoCodigo != null && Funcoes.StringToInt(cargoTipoCodigo) == CargoTipoDt.PRESIDENTE_TURMA){
//        	//Se foi selecionado o presidente, captura o presidente da turma
//        	serventiaCargoDt =  new ServentiaCargoNe().getPresidenteTurmaRecursal(processoDt.getId_Serventia(), obFabricaConexao);
//        } else {
//        	// Consulta o serventiaCargo que ser� o respons�vel pela pend�ncia, de acordo com o tipo selecionado pelo usu�rio
//        	serventiaCargoDt = new ProcessoResponsavelNe().consultarProcessoResponsavelCargoTipo(processoDt.getId(), null, cargoTipoCodigo, obFabricaConexao);
//        }
        
        serventiaCargoDt = new  ServentiaCargoNe().consultarServentiaCargoId(idServentiaCargo, obFabricaConexao);
        
        if (serventiaCargoDt != null) {
        	//Gera movimenta��o "Pedido de Vista (Ao xxx)"
        	new MovimentacaoNe().gerarMovimentacaoPedidoVista(processoDt.getId(), pendenciaDt.getId_UsuarioCadastrador(), "(Ao " + cargoTipo + ")", new LogDt(pendenciaDt.getId_UsuarioLog(), pendenciaDt.getIpComputadorLog()), obFabricaConexao);
        
        	responsavel.setId_ServentiaCargo(serventiaCargoDt.getId());
        	pendenciaDt.addResponsavel(responsavel);
        	
        	 //Verifica se processo j� possui um assistente de gabinete respons�vel, pois esse ser� respons�vel pela pend�ncia
            responsavel = new PendenciaResponsavelDt();
            ServentiaCargoDt serventiaCargoAssistente = new ProcessoResponsavelNe().getAssistenteGabineteResponsavelProcesso(processoDt.getId(), serventiaCargoDt.getId_Serventia(), obFabricaConexao);
            if (serventiaCargoAssistente != null) responsavel.setId_ServentiaCargo(serventiaCargoAssistente.getId());	
            else {
            	//Captura o Distribuidor do Gabinete do Desembargador e adiciona como respons�vel pela pend�ncia
            	ServentiaCargoDt serventiaCargoDistribuidor = new ServentiaCargoNe().getDistribuidorGabinete(serventiaCargoDt.getId_Serventia(), obFabricaConexao);
            	if (serventiaCargoDistribuidor != null) responsavel.setId_ServentiaCargo(serventiaCargoDistribuidor.getId());
            	else throw new MensagemException("N�o foi poss�vel identificar o Cargo de Distribuidor do Gabinete.");
            }
            pendenciaDt.addResponsavel(responsavel);
            
        	this.gerarPendencia(pendenciaDt, arquivosVincular, false, obFabricaConexao);
        } else throw new MensagemException("N�o foi poss�vel obter Cargo respons�vel pela pend�ncia.");
    }
    
    
    /**
     * M�todo que gera pend�ncia do tipo "Conclus�o a um desembargador". Internamente, consulta o ServentiaCargo que ser� respons�vel 
     * pela pend�ncia de acordo com o Tipo de Cargo selecionado pelo usu�rio no momento de gerar a pend�ncia.
     * 
     * @param pendenciaDt, bean de pendencia
     * @param processoDt, bean do processo
     * @param cargoTipoCodigo, tipo do cargo a ser consultado
     * @param arquivosVincular, lista de arquivos a serem vinculados com pend�ncia
     * @param obFabricaConexao, fabrica de conexao para continuar uma transacao existente
     * 
     * @author jrcorrea
     */
    public void gerarPendenciaConclusaoGenericaSegundoGrau(PendenciaDt pendenciaDt, ProcessoDt processoDt, String idServentiaCargo, String cargoTipo, List arquivosVincular, FabricaConexao obFabricaConexao) throws Exception {
    	ServentiaCargoDt serventiaCargoDt = null;
        PendenciaResponsavelDt responsavel = new PendenciaResponsavelDt();
        
        serventiaCargoDt = new  ServentiaCargoNe().consultarServentiaCargoId(idServentiaCargo, obFabricaConexao);
        
        if (serventiaCargoDt != null) {
        	
        	if (verificaConclusoesAbertasProcessoServentiaCargo(processoDt.getId(), serventiaCargoDt.getId(), obFabricaConexao)) 
				throw new MensagemException("Processo " + Funcoes.formataNumeroProcesso(processoDt.getProcessoNumero()) + " j� est� Concluso para " + serventiaCargoDt.getServentiaCargo() + ". ");
        	//
        	new MovimentacaoNe().gerarMovimentacaoConclusaoGenerica(processoDt.getId(), pendenciaDt.getId_UsuarioCadastrador(), "(Ao " + cargoTipo + ")", new LogDt(pendenciaDt.getId_UsuarioLog(), pendenciaDt.getIpComputadorLog()), obFabricaConexao);
        
        	responsavel.setId_ServentiaCargo(serventiaCargoDt.getId());
        	pendenciaDt.addResponsavel(responsavel);
        	
        	 //Verifica se processo j� possui um assistente de gabinete respons�vel, pois esse ser� respons�vel pela pend�ncia
            responsavel = new PendenciaResponsavelDt();
            ServentiaCargoDt serventiaCargoAssistente = new ProcessoResponsavelNe().getAssistenteGabineteResponsavelProcesso(processoDt.getId(), serventiaCargoDt.getId_Serventia(), obFabricaConexao);
            if (serventiaCargoAssistente != null) responsavel.setId_ServentiaCargo(serventiaCargoAssistente.getId());	
            else {
            	//Captura o Distribuidor do Gabinete do Desembargador e adiciona como respons�vel pela pend�ncia
            	ServentiaCargoDt serventiaCargoDistribuidor = new ServentiaCargoNe().getDistribuidorGabinete(serventiaCargoDt.getId_Serventia(), obFabricaConexao);
            	if (serventiaCargoDistribuidor != null) responsavel.setId_ServentiaCargo(serventiaCargoDistribuidor.getId());
            	else throw new MensagemException("N�o foi poss�vel identificar o Cargo de Distribuidor do Gabinete.");
            }
            pendenciaDt.addResponsavel(responsavel);
            
        	this.gerarPendencia(pendenciaDt, arquivosVincular, false, obFabricaConexao);
        } else throw new MensagemException("N�o foi poss�vel obter Cargo respons�vel pela pend�ncia.");
    }
    
    /**
     * M�todo que gera pend�ncia do tipo "Conclus�o a um juiz". Internamente, consulta o ServentiaCargo que ser� respons�vel 
     * pela pend�ncia de acordo com o Tipo de Cargo selecionado pelo usu�rio no momento de gerar a pend�ncia.
     * 
     * @param pendenciaDt, bean de pendencia
     * @param processoDt, bean do processo
     * @param cargoTipoCodigo, tipo do cargo a ser consultado
     * @param arquivosVincular, lista de arquivos a serem vinculados com pend�ncia
     * @param obFabricaConexao, fabrica de conexao para continuar uma transacao existente
     * 
     * @author lsbernardes
     */
    public void gerarPendenciaConclusaoGenericaTurma(PendenciaDt pendenciaDt, ProcessoDt processoDt, String idServentiaCargo, String cargoTipo, List arquivosVincular, FabricaConexao obFabricaConexao) throws Exception {
    	ServentiaCargoDt serventiaCargoDt = null;
        PendenciaResponsavelDt responsavel = new PendenciaResponsavelDt();
        
        serventiaCargoDt = new  ServentiaCargoNe().consultarServentiaCargoId(idServentiaCargo, obFabricaConexao);
        
        if (serventiaCargoDt != null) {
        	
        	if (verificaConclusoesAbertasProcessoServentiaCargo(processoDt.getId(), serventiaCargoDt.getId(), obFabricaConexao)) 
				throw new MensagemException("Processo " + Funcoes.formataNumeroProcesso(processoDt.getProcessoNumero()) + " j� est� Concluso para " + serventiaCargoDt.getServentiaCargo() + ". ");        	
        	//
        	new MovimentacaoNe().gerarMovimentacaoConclusaoGenerica(processoDt.getId(), pendenciaDt.getId_UsuarioCadastrador(), "(Ao " + cargoTipo + ")", new LogDt(pendenciaDt.getId_UsuarioLog(), pendenciaDt.getIpComputadorLog()), obFabricaConexao);
        
        	responsavel.setId_ServentiaCargo(serventiaCargoDt.getId());
        	pendenciaDt.addResponsavel(responsavel);
        	
        	this.gerarPendencia(pendenciaDt, arquivosVincular, false, obFabricaConexao);
        } else throw new MensagemException("N�o foi poss�vel obter Cargo respons�vel pela pend�ncia.");
    }
    
    /**
     * M�todo que gera pend�ncia do tipo "visto a um juiz". Internamente, consulta o ServentiaCargo que ser� respons�vel 
     * pela pend�ncia de acordo com o Tipo de Cargo selecionado pelo usu�rio no momento de gerar a pend�ncia.
     * 
     * @param pendenciaDt, bean de pendencia
     * @param processoDt, bean do processo
     * @param cargoTipoCodigo, tipo do cargo a ser consultado
     * @param arquivosVincular, lista de arquivos a serem vinculados com pend�ncia
     * @param obFabricaConexao, fabrica de conexao para continuar uma transacao existente
     * 
     * @author lsbernardes
     */
    public void gerarPendenciaPedidoVistaTurma(PendenciaDt pendenciaDt, ProcessoDt processoDt, String idServentiaCargo, String cargoTipo, List arquivosVincular, FabricaConexao obFabricaConexao) throws Exception {
    	ServentiaCargoDt serventiaCargoDt = null;
        PendenciaResponsavelDt responsavel = new PendenciaResponsavelDt();
        
        serventiaCargoDt = new  ServentiaCargoNe().consultarServentiaCargoId(idServentiaCargo, obFabricaConexao);
        
        if (serventiaCargoDt != null) {
        	//Gera movimenta��o "Pedido de Vista (Ao xxx)"
        	new MovimentacaoNe().gerarMovimentacaoPedidoVista(processoDt.getId(), pendenciaDt.getId_UsuarioCadastrador(), "(Ao " + cargoTipo + ")", new LogDt(pendenciaDt.getId_UsuarioLog(), pendenciaDt.getIpComputadorLog()), obFabricaConexao);
        
        	responsavel.setId_ServentiaCargo(serventiaCargoDt.getId());
        	pendenciaDt.addResponsavel(responsavel);
            
        	this.gerarPendencia(pendenciaDt, arquivosVincular, false, obFabricaConexao);
        } else throw new MensagemException("N�o foi poss�vel obter Cargo respons�vel pela pend�ncia.");
    }
    
    /**
     * M�todo que gera pend�ncia do tipo "Relat�rio" no 2� grau. Internamente, consulta o Relator do processo pois
     * esse ser� o respons�vel pela pend�ncia.
     * 
     * @param pendenciaDt, bean de pendencia
     * @param processoDt, bean do processo
     * @param arquivosVincular, lista de arquivos a serem vinculados com pend�ncia
     * @param obFabricaConexao, fabrica de conexao para continuar uma transacao existente
     * 
     * @author msapaula
     */
    public void gerarPendenciaRelatorioSegundoGrau(PendenciaDt pendenciaDt, ProcessoDt processoDt, List arquivosVincular, FabricaConexao obFabricaConexao) throws Exception {
    	ProcessoResponsavelNe processoResponsavelNe = new ProcessoResponsavelNe();
    	
    	// Consulta o Relator do processo que ser� o respons�vel pela pend�ncia
    	// Nesse caso o tipo de ProcessoResponsavel que ser� CargoTipo Relator
        ServentiaCargoDt serventiaCargoRelator = new ProcessoResponsavelNe().consultarRelator2Grau(processoDt.getId(), obFabricaConexao);
        
        PendenciaResponsavelDt responsavel = new PendenciaResponsavelDt();
        responsavel.setId_ServentiaCargo(serventiaCargoRelator.getId());
        pendenciaDt.addResponsavel(responsavel);
        
        //Verifica se processo j� possui um assistente de gabinete respons�vel, pois esse ser� respons�vel pela pend�ncia
        responsavel = new PendenciaResponsavelDt();
        ServentiaCargoDt serventiaCargoAssistente = processoResponsavelNe.getAssistenteGabineteResponsavelProcesso(processoDt.getId(), serventiaCargoRelator.getId_Serventia(), obFabricaConexao);
        if (serventiaCargoAssistente != null) responsavel.setId_ServentiaCargo(serventiaCargoAssistente.getId());	
        else {
        	//Captura o Distribuidor do Gabinete do Desembargador e adiciona como respons�vel pela pend�ncia
        	ServentiaCargoDt serventiaCargoDistribuidor = new ServentiaCargoNe().getDistribuidorGabinete(serventiaCargoRelator.getId_Serventia(), obFabricaConexao);
        	if (serventiaCargoDistribuidor != null) responsavel.setId_ServentiaCargo(serventiaCargoDistribuidor.getId());
        	else throw new MensagemException("N�o foi poss�vel identificar o Cargo de Distribuidor do Gabinete.");
        }
        pendenciaDt.addResponsavel(responsavel);
        
        this.gerarPendencia(pendenciaDt, arquivosVincular, false, obFabricaConexao);
    }
    
    public void gerarPendenciaRevisaoSegundoGrau(PendenciaDt pendenciaDt, ProcessoDt processoDt, List arquivosVincular, FabricaConexao obFabricaConexao) throws Exception {
    	ProcessoResponsavelNe processoResponsavelNe = new ProcessoResponsavelNe();
    	
    	// Consulta o Relator do processo que ser� o respons�vel pela pend�ncia
    	// Nesse caso o tipo de ProcessoResponsavel que ser� CargoTipo Relator 
        ServentiaCargoDt serventiaCargoRevisor = processoResponsavelNe.consultarRevisor2Grau(pendenciaDt.getId_Processo(), obFabricaConexao);
        
        PendenciaResponsavelDt responsavel = new PendenciaResponsavelDt();
        responsavel.setId_ServentiaCargo(serventiaCargoRevisor.getId());
        pendenciaDt.addResponsavel(responsavel);
        
        //Verifica se processo j� possui um assistente de gabinete respons�vel, pois esse ser� respons�vel pela pend�ncia
        responsavel = new PendenciaResponsavelDt();
        ServentiaCargoDt serventiaCargoAssistente = processoResponsavelNe.getAssistenteGabineteResponsavelProcesso(processoDt.getId(), serventiaCargoRevisor.getId_Serventia(), obFabricaConexao);
        if (serventiaCargoAssistente != null) responsavel.setId_ServentiaCargo(serventiaCargoAssistente.getId());	
        else {
        	//Captura o Distribuidor do Gabinete do Desembargador e adiciona como respons�vel pela pend�ncia
        	ServentiaCargoDt serventiaCargoDistribuidor = new ServentiaCargoNe().getDistribuidorGabinete(serventiaCargoRevisor.getId_Serventia(), obFabricaConexao);
        	if (serventiaCargoDistribuidor != null) responsavel.setId_ServentiaCargo(serventiaCargoDistribuidor.getId());
        	else throw new MensagemException("N�o foi poss�vel identificar o Cargo de Distribuidor do Gabinete.");
        }
        pendenciaDt.addResponsavel(responsavel);
        
        this.gerarPendencia(pendenciaDt, arquivosVincular, false, obFabricaConexao);
    }

    /**
     * Gera pendencia de expeca-se a carta de citacao
     * 
     * @author Ronneesley Moura Teles
     * @since 09/06/2008 14:18
     * @param processoDt
     *            bean do processo
     * @param id_MovimentacaoAudiencia
     *            id da audiencia de movimentacao
     * @param id_UsuarioServentia
     *            id do usuario da serventia
     * @param id_ProcessoParte
     *            id da parte do processo
     * @param id_responsavel
     *            id da serventia responsavel
     * @param obFabricaConexao
     *            fabrica de conexao para continuar uma transacao existente
     * @throws Exception
     */
    public void gerarCitacao(ProcessoDt processoDt, String id_MovimentacaoAudiencia, String id_UsuarioServentia, String id_ProcessoParte, String id_Serventiaresponsavel, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {

        //this.gerarPendenciaProcesso(processoDt.getId(), PendenciaTipoDt.CARTA_CITACAO, id_MovimentacaoAudiencia, id_UsuarioServentia, "", "", id_ProcessoParte, PendenciaStatusDt.EmAndamento, "", id_responsavel, "", "", null, obFabricaConexao);
        this.gerarPendenciaProcesso(processoDt.getId(), null, PendenciaTipoDt.CARTA_CITACAO, id_MovimentacaoAudiencia, id_UsuarioServentia, "", "", id_ProcessoParte, PendenciaStatusDt.ID_EM_ANDAMENTO, "", id_Serventiaresponsavel, "", "", processoDt.getId_ProcessoPrioridade(), null, logDt, obFabricaConexao);
    }

    /**
     * M�todo respons�vel em gerar pend�ncias no caso de agendamento de
     * audi�ncias
     * 
     * @param processoDt,
     *            bean do processo
     * @param processoParteDt,
     *            dt da parte do processo
     * @param movimentacaoAudiencia,
     *            movimenta��o vinculada a audi�ncia
     * @param id_UsuarioServentia,
     *            usu�rio que est� marcando a audi�ncia
     * @param id_Serventia,
     *            serventia que ser� vinculada as pend�ncias
     * @param obFabricaConexao,
     *            fabrica de conexao para continuar uma transacao existente
     * 
     * @author msapaula
     */
    public void gerarPendenciasAgendamentoAudiencia(ProcessoDt processoDt, ProcessoParteDt processoParteDt, MovimentacaoDt movimentacaoAudiencia, String id_UsuarioServentia, String id_Serventia, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
        
        // Configura a pendencia para ser persistida
        PendenciaDt pendencia = new PendenciaDt();
        pendencia.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_EM_ANDAMENTO));
        pendencia.setId_Movimentacao(movimentacaoAudiencia.getId());
        pendencia.setMovimentacao(movimentacaoAudiencia.getMovimentacaoTipo());
        pendencia.setId_Processo(processoDt.getId());
        pendencia.setId_ProcessoParte(processoParteDt.getId());
        pendencia.setNomeParte(processoParteDt.getNome());
        pendencia.setDataVisto(Funcoes.DataHora(new Date()));
        pendencia.setId_UsuarioCadastrador(id_UsuarioServentia);
        pendencia.setId_UsuarioLog(logDt.getId_Usuario());
        pendencia.setIpComputadorLog(logDt.getIpComputador());
        
        // pendencia para parte
        PendenciaDt pendenciaParteDt = new PendenciaDt();
        pendenciaParteDt.copiar(pendencia);
        pendenciaParteDt.setId_UsuarioFinalizador("null");
       	pendenciaParteDt.setDataFim("");
        pendenciaParteDt.setCodigoTemp("");

        // Se parte possui advogados cadastrados, esses ser�o respons�veis pela pend�ncia
        ProcessoParteAdvogadoNe advogadoNe = new ProcessoParteAdvogadoNe();
        List listaAdvogados = advogadoNe.consultarAdvogadosParte(processoParteDt.getId());

        if (listaAdvogados != null && listaAdvogados.size() > 0) {
            // Se for para advogados sempre ser� Intima��o
            pendencia.setPendenciaTipoCodigo(String.valueOf(PendenciaTipoDt.INTIMACAO));
            
            boolean utilizarDiarioEletronico = advogadoNe.isUtilizarDiarioEletronico(listaAdvogados, false, PendenciaTipoDt.INTIMACAO);
            
            if (utilizarDiarioEletronico) {
                this.criarIntimacaoCitacaoAdvogadosDiarioEletronico(pendencia, id_Serventia, listaAdvogados, null, obFabricaConexao);
            } else{
            	this.criarIntimacaoCitacaoAdvogados(pendencia, id_Serventia, listaAdvogados, null, false, processoDt, null, logDt, obFabricaConexao);
            }
            
            // B.O 2020/4097 - Tamb�m ir� gerar intima��o ou cita��o se o representante da parte for ADVOGADO_PARTICULAR e o mesmo for Dativo ou se o mesmo for ADVOGADO_DEFENSOR_PUBLICO 
            // ou se o mesmo for promotor substituto processual
            if (advogadoNe.isAdvogadoDativoDefensorPublico(listaAdvogados)){
                if (processoDt.isCivel() && !processoParteDt.getCitada().equalsIgnoreCase("true")) {
                    // Se a parte n�o havia sido Citada, deve gerar Cita��o para essa
                	pendenciaParteDt.setPendenciaTipoCodigo(String.valueOf(PendenciaTipoDt.CARTA_CITACAO_AUDIENCIA));
                    new ProcessoParteNe().marcarCitacaoParteProcesso(processoParteDt.getId(), obFabricaConexao);
                } else {
                	pendenciaParteDt.setPendenciaTipoCodigo(String.valueOf(PendenciaTipoDt.INTIMACAO_AUDIENCIA));
                }

                this.criarPendenciaParte(pendenciaParteDt, id_Serventia, null, false, null,null, obFabricaConexao);
            }
            
        } else {
            // Se n�o tiver advogados gera para parte e serventia ou somente serventia 
            if (processoDt.isCivel() && !processoParteDt.getCitada().equalsIgnoreCase("true")) {
                // Se a parte n�o havia sido Citada, deve gerar Cita��o para essa
                pendencia.setPendenciaTipoCodigo(String.valueOf(PendenciaTipoDt.CARTA_CITACAO_AUDIENCIA));
                new ProcessoParteNe().marcarCitacaoParteProcesso(processoParteDt.getId(), obFabricaConexao);
            } else {
                pendencia.setPendenciaTipoCodigo(String.valueOf(PendenciaTipoDt.INTIMACAO_AUDIENCIA));
            }

            this.criarPendenciaParte(pendencia, id_Serventia, null, false, processoDt , null, obFabricaConexao);
        }
    }

    /**
     * Gera pendencia do tipo intimacao para uma serventia
     * 
     * @author Ronneesley Moura Teles
     * @since 10/06/2008 09:33
     * @param processoDt
     *            bean do processo
     * @param id_MovimentacaoAudiencia
     *            id da movimentacao de audiencia
     * @param id_UsuarioServentia
     *            id do usuario da serventia
     * @param id_ProcessoParte
     *            id da parte do processo
     * @param id_responsavel
     *            id do responsavel
     * @param obFabricaConexao
     *            fabrica de conexao para continuar uma transacao existente
     * @return void
     * @throws Exception
     */
    public void gerarIntimacaoServentia(ProcessoDt processoDt, String id_MovimentacaoAudiencia, String id_UsuarioServentia, String id_ProcessoParte, String id_ServentiaResponsavel, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {

        //this.gerarPendenciaProcesso(processoDt.getId(), PendenciaTipoDt.INTIMACAO, id_MovimentacaoAudiencia, id_UsuarioServentia, "", "", id_ProcessoParte, PendenciaStatusDt.EmAndamento, "", id_ServentiaResponsavel, "", "", null, obFabricaConexao);
        this.gerarPendenciaProcesso(processoDt.getId(), null, PendenciaTipoDt.INTIMACAO, id_MovimentacaoAudiencia, id_UsuarioServentia, "", "", id_ProcessoParte, PendenciaStatusDt.ID_EM_ANDAMENTO, "", id_ServentiaResponsavel, "", "", processoDt.getId_ProcessoPrioridade(), null, logDt, obFabricaConexao);
    }

    /**
     * Cria uma pendencia para verificar poss�vel preven��o em um processo
     * 
     * @author msapaula
     * @since 18/06/2009 18:00
     * @param processoDt, bean do processo
     * @param id_UsuarioServentia, id do usuario cadastrador
     * @param id_ServentiaResponsavel, id da serventia responsavel
     * @param id_Movimentacao, movimenta��o vinculada
     * @param logDt, objeto com dados do log
     * @param obFabricaConexao, conex�o ativa
     */
    public void gerarPendenciaVerificarConexao(ProcessoCadastroDt processoDt, String id_UsuarioServentia, String id_ServentiaResponsavel, String id_Movimentacao, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
    	// Monta conte�do do arquivo que ter� detalhes da preven��o e ser� vinculado a pend�ncia
    	List arquivos = new ArrayList();
		ArquivoDt arquivoDt = new ArquivoNe().salvarArquivoVerificarConexao(processoDt, obFabricaConexao);
		arquivos.add(arquivoDt);
        this.gerarPendenciaProcesso(processoDt.getId(), arquivos, PendenciaTipoDt.VERIFICAR_CONEXAO, id_Movimentacao, id_UsuarioServentia, "", "", "", PendenciaStatusDt.ID_EM_ANDAMENTO, "", id_ServentiaResponsavel, "", "", processoDt.getId_ProcessoPrioridade(), null, logDt, obFabricaConexao);
    }

    /**
     * Fechar uma pendencia ja aberta
     * 
     * @author Ronneesley Moura Teles
     * @since 06/06/2008 16:38
     * @param pendenciaDt
     *            vo de pendencia
     * @param usuarioDt
     *            vo de usuario
     * @param obFabricaConexao
     *            fabrica de conexao
     * @throws Exception
     */
    public void fecharPendencia(PendenciaDt pendenciaDt, UsuarioDt usuarioDt, FabricaConexao obFabricaConexao) throws Exception {
    	PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
        
        if (Funcoes.StringToInt(usuarioDt.getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.GABINETE_FLUXO_UPJ){
        	PendenciaResponsavelHistoricoNe pendenciaResponsavelHistoricoNe = new PendenciaResponsavelHistoricoNe();
        	pendenciaResponsavelHistoricoNe.fecharHistorico(pendenciaDt.getId(), pendenciaDt.getDataFim(), usuarioDt.getId(), usuarioDt.getIpComputadorLog(), obFabricaConexao);
        }
        
        obPersistencia.fecharPendencia(pendenciaDt, usuarioDt);
        
        if (pendenciaDt != null && 
        	pendenciaDt.getDataFim()!= null && 
        	pendenciaDt.getDataFim().length()>0 && 
        	pendenciaDt.getDataVisto() != null && 
        	pendenciaDt.getDataVisto().length()>0){
        	
        	obPersistencia.moverPendencia(pendenciaDt.getId());
        	
        }
    }

    /**
     * Modificacao pela modificacao do fechar pendencia Nao gostei da solucao e
     * que o funcionamento nao esta muito bom.
     * 
     * @author Ronneesley Moura Teles
     * @since 26/11/2008 09:37 Fecha uma intimacao
     * @author Ronneesley Moura Teles
     * @since 26/06/2008 08:47 [antes]
     * @param obFabricaConexao
     *            fabrica de conexao
     * @throws Exception
     */
    public void fecharIntimacao(FabricaConexao obFabricaConexao) throws Exception {
        UsuarioDt usuarioDt = new UsuarioDt();
        usuarioDt.setId_UsuarioServentia(obDados.getId_UsuarioCadastrador());

        PendenciaDt pendenciaDt = obDados;
        pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_CUMPRIDA));

        fecharPendencia(pendenciaDt, usuarioDt, obFabricaConexao);
    }

    /**
     * Gera uma pendencia baseada em um processo, juntamente com outros
     * parametros necessarios
     * 
     * @author Ronneesley Moura Teles
     * @since 06/06/2008 10:51
     * @param processo,
     *            dados do processo
     * @param arquivosVincular
     *            lista de arquivos a serem vinculados com pend�ncia
     * @param pendenciaTipo
     *            tipo da pendencia
     * @param id_Movimentacao
     *            id da movimentacao
     * @param id_UsuarioServentia
     *            id do usuario cadastrador
     * @param dataLimite
     *            Data limite para cumprimento da pendencia
     * @param prazo
     *            prazo em dias para conclusao da pendencia
     * @param id_ProcessoParte
     *            parte vinculada a pendencia
     * @param id_pendenciaStatus
     *            status da pendencia a ser criada
     * @param id_responsavel
     *            id do usuario responsavel
     * @param id_serventiaResponsavel
     *            id da serventia responsavel
     * @param id_serventiaTipoResponsavel
     *            id do tipo da serventia responsavel
     * @param id_serventiaCargo
     *            id do serventiaCargo responsavel
     * @param fabConexao
     *            fabrica de conexao
     * @return boolean
     * @throws Exception
     */
   /* public boolean gerarPendenciaProcesso(ProcessoDt processo, List arquivosVincular, int pendenciaTipo, String id_Movimentacao, String id_UsuarioServentia, String dataLimite, String prazo, String id_ProcessoParte, int id_pendenciaStatus, String id_responsavel, String id_serventiaResponsavel, String id_serventiaTipoResponsavel, String id_serventiaCargo, String processoPrioridade, PendenciaDt pendenciaPai, FabricaConexao fabConexao) throws Exception {
        LogDt logDt = new LogDt();
        logDt.setId_Usuario(processo.getId_UsuarioLog());
        logDt.setIpComputador(processo.getIpComputadorLog());

        return this.gerarPendenciaProcesso(processo, arquivosVincular, pendenciaTipo, id_Movimentacao, id_UsuarioServentia, dataLimite, prazo, id_ProcessoParte, id_pendenciaStatus, id_responsavel, id_serventiaResponsavel, id_serventiaTipoResponsavel, id_serventiaCargo, processoPrioridade, pendenciaPai, logDt, fabConexao);
    }*/
    
    /*public boolean gerarPendenciaProcesso(ProcessoDt processo, int pendenciaTipo, String id_Movimentacao, String id_UsuarioServentia, String dataLimite, String prazo, String id_ProcessoParte, int id_pendenciaStatus, String id_responsavel, String id_serventiaResponsavel, String id_serventiaTipoResponsavel, String id_serventiaCargo, String processoPrioridade, FabricaConexao fabConexao) throws Exception {

        return this.gerarPendenciaProcesso(processo, null, pendenciaTipo, id_Movimentacao, id_UsuarioServentia, dataLimite, prazo, id_ProcessoParte, id_pendenciaStatus, id_responsavel, id_serventiaResponsavel, id_serventiaTipoResponsavel, id_serventiaCargo, processoPrioridade, null, fabConexao);
    }*/

    /*public boolean gerarPendenciaProcesso(ProcessoDt processo, int pendenciaTipo, String id_Movimentacao, String id_UsuarioServentia, String dataLimite, String prazo, String id_ProcessoParte, int id_pendenciaStatus, String id_responsavel, String id_serventiaResponsavel, String id_serventiaTipoResponsavel, String id_serventiaCargo, String processoPrioridade, PendenciaDt pendenciaPai, LogDt logDt, FabricaConexao fabConexao) throws Exception {

        return this.gerarPendenciaProcesso(processo, null, pendenciaTipo, id_Movimentacao, id_UsuarioServentia, dataLimite, prazo, id_ProcessoParte, id_pendenciaStatus, id_responsavel, id_serventiaResponsavel, id_serventiaTipoResponsavel, id_serventiaCargo, processoPrioridade, pendenciaPai, logDt, fabConexao);
    }*/

    /**
     * Gera uma pendencia baseada em um processo, juntamente com outros
     * parametros necessarios
     * 
     * @author Ronneesley Moura Teles
     * @since 11/11/2008 11:17
     * @param processo
     *            dados do processo
     * @param arquivosVincular
     *            lista de arquivos a serem vinculados com pend�ncia
     * @param pendenciaTipo
     *            tipo da pendencia
     * @param id_Movimentacao
     *            id da movimentacao
     * @param id_UsuarioServentia
     *            id do usuario cadastrador
     * @param dataLimite
     *            Data limite para cumprimento da pendencia
     * @param prazo
     *            prazo em dias para conclusao da pendencia
     * @param id_ProcessoParte
     *            parte vinculada a pendencia
     * @param id_pendenciaStatus
     *            status da pendencia a ser criada
     * @param id_responsavel
     *            id do usuario responsavel
     * @param id_serventiaResponsavel,id
     *            da serventia responsavel
     * @param id_serventiaTipoResponsavel
     *            id do tipo da serventia responsavel
     * @param id_serventiaCargo
     *            id do serventiaCargo responsavel
     * @param processoPrioridade,
     *            prioridade da pend�ncia
     * @param pendenciaPai,
     *            pai da pend�ncia que ser� criada
     * @param logDt
     *            objeto de log
     * @param fabConexao
     *            fabrica de conexao
     * @return boolean
     * @throws Exception
     */
    public boolean gerarPendenciaProcesso(String id_processo, List arquivosVincular, int pendenciaTipo, String id_Movimentacao, String id_UsuarioServentia, String dataLimite, String prazo, String id_ProcessoParte, int id_pendenciaStatus, String id_responsavel, String id_serventiaResponsavel, String id_serventiaTipoResponsavel, String id_serventiaCargo, String processoPrioridade, PendenciaDt pendenciaPai, LogDt logDt, FabricaConexao fabConexao) throws Exception {

        // Configura a pendencia para ser persistida
        PendenciaDt pendencia = new PendenciaDt();

        pendencia.setPendenciaStatusCodigo(String.valueOf(id_pendenciaStatus));
        pendencia.setPendenciaTipoCodigo(String.valueOf(pendenciaTipo));
        if (processoPrioridade != null && processoPrioridade.length() > 0) pendencia.setProcessoPrioridadeCodigo(String.valueOf(processoPrioridade));
        pendencia.setId_Movimentacao(id_Movimentacao);
        pendencia.setId_Processo(id_processo);
        pendencia.setId_ProcessoParte(id_ProcessoParte);
        if (pendenciaPai != null) pendencia.setId_PendenciaPai(pendenciaPai.getId());
        // Todas as pend�ncias de processo devem ter DataVisto setada
        pendencia.setDataVisto(Funcoes.DataHora(new Date()));
        pendencia.setId_UsuarioCadastrador(id_UsuarioServentia);
        pendencia.setDataLimite(dataLimite);
        pendencia.setPrazo(prazo);
        pendencia.setId_UsuarioLog(logDt.getId_Usuario());
        pendencia.setIpComputadorLog(logDt.getIpComputador());

        PendenciaResponsavelDt responsavel = new PendenciaResponsavelDt();
        responsavel.setId_UsuarioResponsavel(id_responsavel);
        responsavel.setId_Serventia(id_serventiaResponsavel);
        responsavel.setId_ServentiaTipo(id_serventiaTipoResponsavel);
        responsavel.setId_ServentiaCargo(id_serventiaCargo);
        pendencia.addResponsavel(responsavel);

        // Gera a pendencia
        return this.gerarPendencia(pendencia, arquivosVincular, false, fabConexao);
    }
    
    /**
     * Gera uma pendencia baseada em um processo, juntamente com outros
     * parametros necessarios
     * 
     * @author Ronneesley Moura Teles
     * @since 11/11/2008 11:17
     * @param processo
     *            dados do processo
     * @param arquivosVincular
     *            lista de arquivos a serem vinculados com pend�ncia
     * @param pendenciaTipo
     *            tipo da pendencia
     * @param id_Movimentacao
     *            id da movimentacao
     * @param id_UsuarioServentia
     *            id do usuario cadastrador
     * @param dataLimite
     *            Data limite para cumprimento da pendencia
     * @param prazo
     *            prazo em dias para conclusao da pendencia
     * @param id_ProcessoParte
     *            parte vinculada a pendencia
     * @param id_pendenciaStatus
     *            status da pendencia a ser criada
     * @param id_responsavel
     *            id do usuario responsavel
     * @param id_serventiaResponsavel,id
     *            da serventia responsavel
     * @param id_serventiaTipoResponsavel
     *            id do tipo da serventia responsavel
     * @param id_serventiaCargo
     *            id do serventiaCargo responsavel
     * @param processoPrioridade,
     *            prioridade da pend�ncia
     * @param pendenciaPai,
     *            pai da pend�ncia que ser� criada
     * @param logDt
     *            objeto de log
     * @param fabConexao
     *            fabrica de conexao
     * @return boolean
     * @throws Exception
     */
    public boolean gerarPendenciaProcesso(String id_processo, List arquivosVincular, int pendenciaTipo, String id_Movimentacao, String id_UsuarioServentia, String dataLimite, String prazo, String id_ProcessoParte, int id_pendenciaStatus, String id_responsavel, String id_serventiaResponsavel, String id_serventiaTipoResponsavel, String id_serventiaCargo, String processoPrioridade, PendenciaDt pendenciaPai, String id_usuario, String ipComputador, FabricaConexao fabConexao) throws Exception {

        // Configura a pendencia para ser persistida
        PendenciaDt pendencia = new PendenciaDt();

        pendencia.setPendenciaStatusCodigo(String.valueOf(id_pendenciaStatus));
        pendencia.setPendenciaTipoCodigo(String.valueOf(pendenciaTipo));
        if (processoPrioridade != null && processoPrioridade.length() > 0) pendencia.setProcessoPrioridadeCodigo(String.valueOf(processoPrioridade));
        pendencia.setId_Movimentacao(id_Movimentacao);
        pendencia.setId_Processo(id_processo);
        pendencia.setId_ProcessoParte(id_ProcessoParte);
        if (pendenciaPai != null) pendencia.setId_PendenciaPai(pendenciaPai.getId());
        // Todas as pend�ncias de processo devem ter DataVisto setada
        pendencia.setDataVisto(Funcoes.DataHora(new Date()));
        pendencia.setId_UsuarioCadastrador(id_UsuarioServentia);
        pendencia.setDataLimite(dataLimite);
        pendencia.setPrazo(prazo);
        pendencia.setId_UsuarioLog(id_usuario);
        pendencia.setIpComputadorLog(ipComputador);

        PendenciaResponsavelDt responsavel = new PendenciaResponsavelDt();
        responsavel.setId_UsuarioResponsavel(id_responsavel);
        responsavel.setId_Serventia(id_serventiaResponsavel);
        responsavel.setId_ServentiaTipo(id_serventiaTipoResponsavel);
        responsavel.setId_ServentiaCargo(id_serventiaCargo);
        pendencia.addResponsavel(responsavel);

        // Gera a pendencia
        return this.gerarPendencia(pendencia, arquivosVincular, false, fabConexao);
    }
   
    /**
     * Gera uma pendencia verificar novo processo baseada na pendencia verificar distribui��o, juntamente com outros
     * parametros necessarios
     */ 
    public boolean gerarPendenciaVerificarNovoProcesso( PendenciaDt pendenciaPai , UsuarioDt usuarioDt, List arquivosVincular, LogDt logDt, FabricaConexao fabConexao) throws Exception {

        // Configura a pendencia para ser persistida
        PendenciaDt pendencia = new PendenciaDt();

        pendencia.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_EM_ANDAMENTO));
        pendencia.setPendenciaTipoCodigo(String.valueOf(PendenciaTipoDt.VERIFICAR_NOVO_PROCESSO));
        pendencia.setId_Movimentacao(pendenciaPai.getId_Movimentacao());
        pendencia.setId_Processo(pendenciaPai.getId_Processo());
        // Todas as pend�ncias de processo devem ter DataVisto setada
        pendencia.setDataVisto(Funcoes.DataHora(new Date()));
        pendencia.setId_UsuarioCadastrador(usuarioDt.getId_UsuarioServentia());
        pendencia.setId_UsuarioLog(logDt.getId_Usuario());
        pendencia.setIpComputadorLog(logDt.getIpComputador());

        PendenciaResponsavelDt responsavel = new PendenciaResponsavelDt();
        responsavel.setId_Serventia(usuarioDt.getId_Serventia());
        pendencia.addResponsavel(responsavel);

        // Gera a pendencia
        return this.gerarPendencia(pendencia, arquivosVincular, false, fabConexao);
    }
    
    /**
     * Gera uma pendencia verificar novo processo com pedido de assist�ncia baseada na pendencia verificar distribui��o, juntamente com outros
     * parametros necessarios
     */ 
    public boolean gerarPendenciaVerificarNovoProcessoPedidoAssistencia( PendenciaDt pendenciaPai , UsuarioDt usuarioDt, List arquivosVincular, LogDt logDt, FabricaConexao fabConexao) throws Exception {

        // Configura a pendencia para ser persistida
        PendenciaDt pendencia = new PendenciaDt();

        pendencia.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_EM_ANDAMENTO));
        pendencia.setPendenciaTipoCodigo(String.valueOf(PendenciaTipoDt.VERIFICAR_NOVO_PROCESSO_PEDIDO_ASSISTENCIA));
        pendencia.setId_Movimentacao(pendenciaPai.getId_Movimentacao());
        pendencia.setId_Processo(pendenciaPai.getId_Processo());
        // Todas as pend�ncias de processo devem ter DataVisto setada
        pendencia.setDataVisto(Funcoes.DataHora(new Date()));
        pendencia.setId_UsuarioCadastrador(usuarioDt.getId_UsuarioServentia());
        pendencia.setId_UsuarioLog(logDt.getId_Usuario());
        pendencia.setIpComputadorLog(logDt.getIpComputador());

        PendenciaResponsavelDt responsavel = new PendenciaResponsavelDt();
        responsavel.setId_Serventia(usuarioDt.getId_Serventia());
        pendencia.addResponsavel(responsavel);

        // Gera a pendencia
        return this.gerarPendencia(pendencia, arquivosVincular, false, fabConexao);
    }

    /**
     * Adiciona uma pendencia Utilizando dados da pendencia, e lista de
     * responsaveis Deve ser utilizando quando ja se tem uma transacao
     * 
     * @author Ronneesley Moura Teles
     * @since 27/01/2009 09:29 [muito antes]
     * @param pendencia
     *            dados da pendencia
     * @param arquivos
     *            lista de arquivos
     * @param respostaArquivoVincular
     *            true se os arquivos sao de resposta e false se nao
     * @param fabConexao
     *            fabrica de conexao
     * @return boolean
     * @throws Exception
     */
    public boolean gerarPendencia(PendenciaDt pendencia, List arquivos, boolean respostaArquivoVincular, FabricaConexao fabConexao) throws Exception {
        return this.gerarPendencia(pendencia, pendencia.getResponsaveis(), arquivos, respostaArquivoVincular, fabConexao);
    }

    /**
     * Adiciona uma pendencia Utilizando dados da pendencia, e lista de
     * responsaveis Deve ser utilizando quando ja se tem uma transacao
     * 
     * @author Ronneesley Moura Teles
     * @since 27/01/2009 09:29 [muito antes]
     * @param pendencia
     *            dados da pendencia
     * @param fabConexao
     *            fabrica de conexao
     * @return boolean
     * @throws Exception
     */
    public boolean gerarPendencia(PendenciaDt pendencia, FabricaConexao fabConexao) throws Exception {
        return this.gerarPendencia(pendencia, pendencia.getResponsaveis(), null, false, fabConexao);
    }

    /**
     * Adaptador para implementacao real
     * 
     * @author Ronneesley Moura Teles
     * @since 28/11/2008 09:39
     * @param pendencia,
     *            dados da pendencia a ser gerada
     * @param responsaveis,
     *            lista de responsaveis da pendencia
     * @param arquivosVincular,
     *            arquivos a serem vinculados com a pendencia
     * @param respostaArquivoVincular
     *            true se os arquivos sao de resposta e false se nao
     * @param fabConexao,
     *            Fabrica de conexao
     * @return boolean
     * @throws Exception
     */
    public boolean gerarPendencia(PendenciaDt pendencia, List responsaveis, List arquivosVincular, boolean respostaArquivoVincular, FabricaConexao fabConexao) throws Exception {
        return this.gerarPendencia(pendencia, responsaveis, arquivosVincular, null, respostaArquivoVincular, false, fabConexao);
    }

    /**
     * Modificado para suportar uma lista de arquivos
     * 
     * @author Ronneesley Moura Teles
     * @since 26/11/2008 09:49 Adiciona uma pendencia no banco de dados Os dados
     *        da pendencia e dos responsaveis da pendencia ser�o inseridos Deve
     *        ser utilizado quando esta preocupado com a transacao externa a
     *        pendencia a ser gerada
     * @author Ronneesley Moura Teles
     * @since 06/06/2008 - 09:21
     * @param pendencia
     *            dados da pendencia a ser gerada
     * @param responsaveis
     *            lista de responsaveis da pendencia
     * @param arquivosVincular
     *            arquivos a serem vinculados com a pendencia
     * @param arquivosInserir
     *            arquivos a inserir
     * @param respostaArquivoVincular
     *            true se os arquivos sao de resposta e false se nao
     * @param respostaArquivoInserir
     *            true se os arquivos sao de resposta e false se nao
     * @param fabConexao
     *            Fabrica de conexao
     * @return boolean
     * @throws Exception
     */
    public boolean gerarPendencia(PendenciaDt pendencia, List responsaveis, List arquivosVincular, List arquivosInserir, boolean respostaArquivoVincular, boolean respostaArquivoInserir, FabricaConexao fabConexao) throws Exception {
        FabricaConexao obFabricaConexao = null;
        try {
            if (pendencia != null) {
                if (responsaveis != null) {

                    // Verifica se a conexao sera criada internamente
                    if (fabConexao == null) {
                        obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
                        obFabricaConexao.iniciarTransacao();
                    } else {
                        // Caso da conexao criada em um nivel superior
                        obFabricaConexao = fabConexao;
                    }

                    PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
                    
                    if (pendencia.getId_PendenciaPai() != null && !pendencia.getId_PendenciaPai().equals("")
                    	&& Funcoes.StringToInt(pendencia.getPendenciaTipoCodigo()) != PendenciaTipoDt.PEDIDO_MANIFESTACAO
                    	&& !pendencia.isConclusao()) {
                        // se a pendencia pai tiver um usuario responsavel
                        // distribui a pendencia filha para o mesmo (reabrir pendencia)
                        for (int i = 0; i < responsaveis.size(); i++) {
                            PendenciaResponsavelDt peResponsavelDt = (PendenciaResponsavelDt) responsaveis.get(i);
                            if (peResponsavelDt.getId_UsuarioResponsavel() != null && !peResponsavelDt.getId_UsuarioResponsavel().equals("")) pendencia.setId_UsuarioFinalizador(peResponsavelDt.getId_UsuarioResponsavel());
                        }
                    }

                    obPersistencia.inserir(pendencia);
                    // Grava o log da operacao
                    this.gravarLog(pendencia, obFabricaConexao);
                    
                    obDados.copiar(pendencia);
                    
                    // Inserir a lista de responsaveis na pendencia
                    PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
                    pendenciaResponsavelNe.inserirResponsaveis(pendencia, responsaveis, obFabricaConexao);

                    // Se possui objetos na lista de arquivos a vincular
                    if (arquivosVincular != null && arquivosVincular.size() > 0) {
                        PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
                        pendenciaArquivoNe.vincularArquivos(pendencia, arquivosVincular, respostaArquivoVincular, obFabricaConexao);
                    }

                    // Se possui objetos na lista de arquivos a inserir
                    if (arquivosInserir != null && arquivosInserir.size() > 0) {
                        PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
                        LogDt logDt = new LogDt();
                        logDt.setId_Usuario(pendencia.getId_UsuarioLog());
                        logDt.setIpComputadorLog(pendencia.getIpComputadorLog());
                        pendenciaArquivoNe.inserirArquivos(pendencia, arquivosInserir, respostaArquivoInserir, false, logDt, obFabricaConexao);
                    }

                    if (fabConexao == null) obFabricaConexao.finalizarTransacao();
                } else {
                    throw new MensagemException("O sistema n�o conseguiu determinar os responsaveis da pend�ncia.");
                }
            } else {
                throw new MensagemException("Pend�ncia n�o encontrada.");
            }
        } catch (Exception e) {
            // Se a conexao foi criada dentro do metodo, entao cancela a conexao
            if (fabConexao == null) obFabricaConexao.cancelarTransacao();
            throw e;
        } finally {
            // Se a conexao foi criada dentro do metodo, entao finaliza a
            // conexao
            if (fabConexao == null) obFabricaConexao.fecharConexao();
        }

        // Operacoes realizadas com sucesso
        return true;
    }
    
    /**
     * 
     * @author Leandro bernardes
    
     * @param pendencia
     *            dados da pendencia a ser gerada
     * @param responsaveis
     *            lista de responsaveis da pendencia
     * @param arquivosVincular
     *            arquivos a serem vinculados com a pendencia
     * @param arquivosInserir
     *            arquivos a inserir
     * @param respostaArquivoVincular
     *            true se os arquivos sao de resposta e false se nao
     * @param respostaArquivoInserir
     *            true se os arquivos sao de resposta e false se nao
     * @param fabConexao
     *            Fabrica de conexao
     * @return boolean
     * @throws Exception
     */
    public String gerarPendenciaFilha(PendenciaDt pendencia, List responsaveis, List arquivosVincular, List arquivosInserir, boolean respostaArquivoVincular, boolean respostaArquivoInserir, boolean aguardandoAssinatura, FabricaConexao fabConexao) throws Exception {
    	// jvosantos - 05/11/2019 12:22 - Alterar retorno para algo util, o boolean n�o era utilizado e era inutil.
    	FabricaConexao obFabricaConexao = null;
        try {
            if (pendencia != null) {
                if (responsaveis != null) {

                    // Verifica se a conexao sera criada internamente
                    if (fabConexao == null) {
                        obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
                        obFabricaConexao.iniciarTransacao();
                    } else {
                        // Caso da conexao criada em um nivel superior
                        obFabricaConexao = fabConexao;
                    }

                    PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
                    

                    pendenciaPs.inserir(pendencia);
                    // Grava o log da operacao
                    this.gravarLog(pendencia, obFabricaConexao);
                    
                    obDados.copiar(pendencia);
                    
                    // Inserir a lista de responsaveis na pendencia
                    PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
                    pendenciaResponsavelNe.inserirResponsaveis(pendencia, responsaveis, obFabricaConexao);

                    // Se possui objetos na lista de arquivos a vincular
                    if (arquivosVincular != null && arquivosVincular.size() > 0) {
                        PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
                        pendenciaArquivoNe.vincularArquivos(pendencia, arquivosVincular, respostaArquivoVincular, obFabricaConexao);
                    }

                    // Se possui objetos na lista de arquivos a inserir
                    if (arquivosInserir != null && arquivosInserir.size() > 0) {
                    	LogDt logDt = new LogDt();
                        logDt.setId_Usuario(pendencia.getId_UsuarioLog());
                        logDt.setIpComputadorLog(pendencia.getIpComputadorLog());
                        PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
                        pendenciaArquivoNe.inserirArquivos(pendencia, arquivosInserir, respostaArquivoInserir, aguardandoAssinatura, logDt, obFabricaConexao);
                    }

                    if (fabConexao == null) obFabricaConexao.finalizarTransacao();
                } else {
                    throw new MensagemException("O sistema n�o conseguiu determinar os responsaveis da pend�ncia.");
                }
            } else {
                throw new MensagemException("Pend�ncia n�o encontrada.");
            }
        } catch (Exception e) {
            // Se a conexao foi criada dentro do metodo, entao cancela a conexao
            if (fabConexao == null) obFabricaConexao.cancelarTransacao();
            throw e;
        } finally {
            // Se a conexao foi criada dentro do metodo, entao finaliza a
            // conexao
            if (fabConexao == null) obFabricaConexao.fecharConexao();
        }

        // Operacoes realizadas com sucesso
        return pendencia != null ? pendencia.getId() :null;
    }
    
    /**
     * Gera uma pend�ncia para um serventia cargo que permite acesso a processos de outras serventias
     * 
     * @author leandro bernardes
     * @param UsuarioDt
     *            usuario logado
     * @param String
     *            id do processo que ter� acesso
     * @return boolean
     * @throws Exception
     */
    public boolean gerarPendenciaLiberaAcesso(UsuarioDt usuarioDt, String id_ProcessoCompletoDt, boolean segredoJustica) throws Exception {
        FabricaConexao obFabricaConexao = null;
        obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
        
        //conform ata 11� Reuni�o (Extraordin�ria) � Comiss�o de Informatiza��o (05/02/2018)
		// a ata esta salva na documenta��o do sistema Ata da 11� Reuni�o Extraordin�ria - Comiss�o de Informatiza��o.pdf
        if (segredoJustica && !usuarioDt.isDesembargador()) {
        	throw new MensagemException("N�o � poss�vel realizar esta a��o. Motivo: Processo Segredo de Justi�a.");	
        }
        //est� iniciando a transa��o antes de entrar no if, para evitar erro quando a mensagem de erro de cargo
        //do else � gerada. Acontecia erro ao tentar cancelar a transa��o no final do m�todo, sendo que a transa��o
        //n�o havia sido iniciada.
        obFabricaConexao.iniciarTransacao();
        try {
                if (usuarioDt != null && !(usuarioDt.isAssessor() || usuarioDt.isAssessorAdvogado() || usuarioDt.isAssessorMP()) ) {
                
                    PendenciaDt pendenciaDt = new PendenciaDt();
                    
                    pendenciaDt.setId_UsuarioCadastrador(usuarioDt.getId_UsuarioServentia());
                    pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_EM_ANDAMENTO));
                    pendenciaDt.setId_Processo(id_ProcessoCompletoDt);
                    pendenciaDt.setPrazo("1");
                    
                    pendenciaDt.setIpComputadorLog(usuarioDt.getIpComputadorLog());
                    pendenciaDt.setId_UsuarioLog(usuarioDt.getId());
                    
                    Calendar calCalendario = Calendar.getInstance(); //Funcoes.BancoDataHora(new Date())
                    calCalendario.setTime(new Date());

                    // Proximo dia, a contagem se inicia no dia seguinte
                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
                    pendenciaDt.setDataInicio(df.format(new Date()));
                    pendenciaDt.setDataVisto(df.format(new Date()));
                    calCalendario.add(Calendar.DATE, 1);
                    pendenciaDt.setDataLimite(df.format(calCalendario.getTime()));
                    
                    PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
                    
                    
                    PendenciaTipoNe pendenciaTipoNe = new PendenciaTipoNe();
                    PendenciaTipoDt pendenciaTipoDt = pendenciaTipoNe.consultarPendenciaTipoCodigo(PendenciaTipoDt.LIBERACAO_ACESSO);
                    if (pendenciaTipoDt.getId() == null){
                    	throw new MensagemException("Pend�ncia do tipo liberar acesso ao processo n�o encontrada");
                    } else {
                    	pendenciaDt.setId_PendenciaTipo(pendenciaTipoDt.getId());
                    }

                    pendenciaPs.inserirPendenciaLiberarAcesso(pendenciaDt);
                    obDados.copiar(pendenciaDt);
                    
                    // Grava o log da operacao
                    this.gravarLog(pendenciaDt, obFabricaConexao);

                    // Inserir responsavel na pendencia
                    PendenciaResponsavelDt pendenciaResponsavelDt = new PendenciaResponsavelDt();
                    PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
                    //pendencia liberar acesso sempre para o usu�rio serventia
                    //if ( usuarioDt.isAdvogado() ||  usuarioDt.isAssessorMagistrado() ){
                    	pendenciaResponsavelDt.setId_UsuarioResponsavel(usuarioDt.getId_UsuarioServentia());
                    //}else{
//                    	pendenciaResponsavelDt.setId_ServentiaCargo(usuarioDt.getId_ServentiaCargo());
//                    }
                    
                    pendenciaResponsavelDt.setId_Pendencia(pendenciaDt.getId());                    
                    pendenciaResponsavelDt.setId_UsuarioLog(usuarioDt.getId());
                    pendenciaResponsavelDt.setIpComputadorLog(usuarioDt.getIpComputadorLog());
                    pendenciaResponsavelNe.inserir(pendenciaResponsavelDt, obFabricaConexao);                 
                } else {
                    throw new MensagemException("O sistema n�o conseguiu determinar o responsavel da pend�ncia que libera acesso ao processo. � necess�rio pertencer a um cargo na serventia logada ou ser um advogado.");
                }
            
            obFabricaConexao.finalizarTransacao();
        } catch (Exception e) {
            // Se a conexao foi criada dentro do metodo, entao cancela a conexao
            obFabricaConexao.cancelarTransacao();
            throw e;
        } finally {
            // Se a conexao foi criada dentro do metodo, entao finaliza a
            // conexao
            obFabricaConexao.fecharConexao();
        }

        // Operacoes realizadas com sucesso
        return true;
    }
    
    /**
     * Gera uma pend�ncia aguardando parecer para advogados
     * 
     * @author leandro bernardes
     * @param UsuarioDt
     *            usuario logado
     * @param String
     *            id do processo que ter� acesso
     * @throws Exception
     */
    public PendenciaDt gerarAguardandoParecer(UsuarioDt usuarioDt, String id_ProcessoCompletoDt) throws Exception {
        FabricaConexao obFabricaConexao = null;
        PendenciaDt pendenciaDt = new PendenciaDt();
        try {
                if ((usuarioDt != null && usuarioDt.getId_ServentiaCargo() != null 
                		&& !usuarioDt.getId_ServentiaCargo().equals("")) 
                		|| (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ADVOGADO) ) {

                    // Verifica se a conexao sera criada internamente
                    obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
                    obFabricaConexao.iniciarTransacao();
                    
                    pendenciaDt.setId_UsuarioCadastrador(usuarioDt.getId_UsuarioServentia());
                    pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_EM_ANDAMENTO));
                    pendenciaDt.setId_Processo(id_ProcessoCompletoDt);
                    
                    pendenciaDt.setIpComputadorLog(usuarioDt.getIpComputadorLog());
                    pendenciaDt.setId_UsuarioLog(usuarioDt.getId());
                    
                    Calendar calCalendario = Calendar.getInstance(); //Funcoes.BancoDataHora(new Date())
                    calCalendario.setTime(new Date());

                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
                    pendenciaDt.setDataInicio(df.format(new Date()));
                    pendenciaDt.setDataVisto(df.format(new Date()));
                    
                    PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
                    
                    
                    PendenciaTipoNe pendenciaTipoNe = new PendenciaTipoNe();
                    PendenciaTipoDt pendenciaTipoDt = pendenciaTipoNe.consultarPendenciaTipoCodigo(PendenciaTipoDt.PEDIDO_MANIFESTACAO);
                    if (pendenciaTipoDt.getId() == null){
                    	throw new MensagemException("Pend�ncia do tipo Aguardando Parecer n�o encontrada.");
                    } else {
                    	pendenciaDt.setId_PendenciaTipo(pendenciaTipoDt.getId());
                    }

                    // Grava o log da operacao
                    this.gravarLog(pendenciaDt, obFabricaConexao);

                    pendenciaPs.inserir(pendenciaDt);
                    obDados.copiar(pendenciaDt);

                    // Inserir responsavel na pendencia
                    PendenciaResponsavelDt pendenciaResponsavelDt = new PendenciaResponsavelDt();
                    PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
                    
                    if (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ADVOGADO)
                    	pendenciaResponsavelDt.setId_UsuarioResponsavel(usuarioDt.getId_UsuarioServentia());
                    else
                    	pendenciaResponsavelDt.setId_ServentiaCargo(usuarioDt.getId_ServentiaCargo());
                    
                    pendenciaResponsavelDt.setId_Pendencia(pendenciaDt.getId());
                    pendenciaResponsavelDt.setId_UsuarioLog(usuarioDt.getId());
                    pendenciaResponsavelDt.setIpComputadorLog(usuarioDt.getIpComputadorLog());
                    pendenciaResponsavelNe.inserir(pendenciaResponsavelDt, obFabricaConexao);

                    obFabricaConexao.finalizarTransacao();
                } else {
                    throw new MensagemException("O sistema n�o conseguiu determinar o responsavel da pend�ncia  aguardando parecer.");
                }
        } catch (Exception e) {
            // Se a conexao foi criada dentro do metodo, entao cancela a conexao
            obFabricaConexao.cancelarTransacao();
            throw e;
        } finally {
            // Se a conexao foi criada dentro do metodo, entao finaliza a
            // conexao
            obFabricaConexao.fecharConexao();
        }

        // Operacoes realizadas com sucesso
        return pendenciaDt;
    }
    
    /**
     * Gera uma pend�ncia elabora��o de voto para ju�zes
     * 
     * @author leandro bernardes
     * @param UsuarioDt
     *            usuario logado
     * @param String
     *            id do processo que ter� acesso
     * @throws Exception
     */
    public PendenciaDt gerarElaboracaoVoto(UsuarioDt usuarioDt, String id_ProcessoCompletoDt) throws Exception {
        FabricaConexao obFabricaConexao = null;
        PendenciaDt pendenciaDt = new PendenciaDt();
        try {
                if (usuarioDt != null && usuarioDt.getId_ServentiaCargo() != null 
                		&& !usuarioDt.getId_ServentiaCargo().equals("")) {

                    // Verifica se a conexao sera criada internamente
                    obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
                    obFabricaConexao.iniciarTransacao();
                    
                    pendenciaDt.setId_UsuarioCadastrador(usuarioDt.getId_UsuarioServentia());
                    pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_EM_ANDAMENTO));
                    pendenciaDt.setId_Processo(id_ProcessoCompletoDt);
                    
                    pendenciaDt.setIpComputadorLog(usuarioDt.getIpComputadorLog());
                    pendenciaDt.setId_UsuarioLog(usuarioDt.getId());
                    
                    Calendar calCalendario = Calendar.getInstance(); //Funcoes.BancoDataHora(new Date())
                    calCalendario.setTime(new Date());

                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
                    pendenciaDt.setDataInicio(df.format(new Date()));
                    pendenciaDt.setDataVisto(df.format(new Date()));
                    
                    PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
                    
                    
                    PendenciaTipoNe pendenciaTipoNe = new PendenciaTipoNe();
                    PendenciaTipoDt pendenciaTipoDt = pendenciaTipoNe.consultarPendenciaTipoCodigo(PendenciaTipoDt.ELABORACAO_VOTO);
                    if (pendenciaTipoDt.getId() == null){
                    	throw new MensagemException("Pend�ncia do tipo Elabora��o de Voto n�o encontrada.");
                    } else {
                    	pendenciaDt.setId_PendenciaTipo(pendenciaTipoDt.getId());
                    }

                    // Grava o log da operacao
                    this.gravarLog(pendenciaDt, obFabricaConexao);

                    pendenciaPs.inserir(pendenciaDt);
                    obDados.copiar(pendenciaDt);

                    // Inserir responsavel na pendencia
                    PendenciaResponsavelDt pendenciaResponsavelDt = new PendenciaResponsavelDt();
                    PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
                   	pendenciaResponsavelDt.setId_ServentiaCargo(usuarioDt.getId_ServentiaCargo());
                    pendenciaResponsavelDt.setId_Pendencia(pendenciaDt.getId());
                    pendenciaResponsavelDt.setId_UsuarioLog(usuarioDt.getId());
                    pendenciaResponsavelDt.setIpComputadorLog(usuarioDt.getIpComputadorLog());
                    pendenciaResponsavelNe.inserir(pendenciaResponsavelDt, obFabricaConexao);

                    obFabricaConexao.finalizarTransacao();
                } else {
                    throw new MensagemException("O sistema n�o conseguiu determinar o responsavel da pend�ncia elabora��o de voto.");
                }
        } catch (Exception e) {
            // Se a conexao foi criada dentro do metodo, entao cancela a conexao
            obFabricaConexao.cancelarTransacao();
            throw e;
        } finally {
            // Se a conexao foi criada dentro do metodo, entao finaliza a
            // conexao
            obFabricaConexao.fecharConexao();
        }

        // Operacoes realizadas com sucesso
        return pendenciaDt;
    }

    /**
     * Retorna ultima pendencia para o usuario
     * 
     * @author Ronneesley Moura Teles
     * @since 17/06/2008 11:29 [antes]
     * @param int
     *            tipo, tipo da pendencia
     * @param usuarioDt
     *            pojo do usuario
     * @return PendenciaDt
     */
    /*
      public PendenciaDt consultarUltimaPendencia(int tipo, UsuarioDt usuarioDt) throws Exception {
		PendenciaDt pendenciaDt = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{


			PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
			//pendenciaPs.setConexao(obFabricaConexao);
			pendenciaDt = pendenciaPs.consultarUltimaPendencia(tipo, usuarioDt);
		} finally {
			obFabricaConexao.fecharConexao();
		}

		return pendenciaDt;
	}*/
     
    /**
	 * Consulta a estatistica dos tipos das pendencias ativas pagina Inicial
	 * 
	 * @author Leandro Bernardes
	 * @since 31/03/2009
	 * @param usuarioDt
	 *            usuario de referencia
	 * @return
	 * @throws Exception
	 */
    /*public Map consultarTiposAtivasServentiaPaginaInicial(UsuarioDt usuarioDt) throws Exception {
        Map tiposPendencia = new HashMap();

        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            if (usuarioDt.getId_ServentiaCargo() != null && !usuarioDt.getId_ServentiaCargo().trim().equals("")) tiposPendencia = pendenciaPs.consultarTiposAtivasServentiaPaginaInicial(usuarioDt);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return tiposPendencia;
    }*/

    /**
     * Consulta pendencias ativas pagina Inicial assistente
     * 
     * @author Leandro Bernardes
     * @since 31/03/2009
     * @param usuarioDt
     *            usuario de referencia
     * @return
     * @throws Exception
     */
    /*public Map consultarTiposAtivasAssistentePaginaInicial(UsuarioDt usuarioDt) throws Exception {
        Map tiposPendencia = null;

        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            tiposPendencia = pendenciaPs.consultarTiposAtivasAssistentePaginaInicial(usuarioDt);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return tiposPendencia;
    }*/

    /**
     * Consulta a estatistica dos tipos das pendencias ativas para cargo
     * serventia pagina Inicial
     * 
     * @author Leandro Bernardes
     * @since 10/08/2009
     * @param usuarioDt
     *            usuario de referencia
     * @return
     * @throws Exception
     */
    /*public Map consultarTiposAtivasCargoServentiaPaginaInicial(UsuarioDt usuarioDt) throws Exception {
        Map tiposPendencia = null;

        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            tiposPendencia = pendenciaPs.consultarTiposAtivasCargoServentiaPaginaInicial(usuarioDt);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return tiposPendencia;
    }*/

    /**
     * Consulta a estatistica dos tipos das pendencias ativas para cargo
     * serventia pagina Inicial promotor
     * 
     * @author Leandro Bernardes
     * @since 28/09/2009
     * @param usuarioDt
     *            usuario de referencia
     * @return
     * @throws Exception
     */
    /*public Map consultarTiposAtivasCargoServentiaPaginaInicialPromotor(UsuarioDt usuarioDt) throws Exception {
        Map tiposPendencia = new HashMap();

        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            if (usuarioDt.getId_ServentiaCargo() != null && !usuarioDt.getId_ServentiaCargo().trim().equals("")) tiposPendencia = pendenciaPs.consultarTiposAtivasCargoServentiaPaginaInicialPromotor(usuarioDt);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return tiposPendencia;
    }*/

    /**
     * Consulta a estatistica dos tipos das pendencias ativas para cargo
     * serventia pagina Inicial
     * 
     * @author Leandro Bernardes
     * @since 10/08/2009
     * @param usuarioDt
     *            usuario de referencia
     * @return
     * @throws Exception
     */
    public Map consultarTiposAtivasServentiaTipoPaginaInicial(UsuarioDt usuarioDt) throws Exception {
        Map tiposPendencia = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            
            tiposPendencia = pendenciaPs.consultarTiposAtivasServentiaTipoPaginaInicial(usuarioDt);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return tiposPendencia;
    }

    /**
     * Consulta a quantidade de pendencias em andamento de um determinado tipo
     * para o usuario - Consulta inclusive para a serventia, para o tipo de
     * serventia, para o cargo do usuario, para o usuario resposavel
     * 
     * @author Leandro Bernardes
     * @since 03/12/2008 10:35
     * @param usuarioDt
     *            usuario que deseja a quantidade
     * @param tipo
     *            tipo da pendencia
     * @return int
     * @throws Exception
     */
    /*public int consultarQuantidadeServentiaAbertasEmAndamento(UsuarioDt usuarioDt, String tipo) throws Exception {
        int qtd = 0;

        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            qtd = pendenciaPs.consultarQuantidadeServentiaAbertasEmAndamento(usuarioDt, tipo);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return qtd;
    }*/
    
    /**
     * Consulta a lista de pendencias em andamento de uma determinada serventia 
     * 
     * @author Leandro Bernardes
     * @param usuarioDt
     *            usuario que deseja a quantidade
    
     * @return List lista de pendencias
     * @throws Exception
     */
    public List consultarPendenciasServentiaAbertasEmAndamento(UsuarioDt usuarioDt) throws Exception {
    	List pendencias = new ArrayList();
    	FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            
            pendencias = pendenciaPs.consultarPendenciasServentiaAbertasEmAndamento(usuarioDt);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }
    
    /**
     * Consulta a lista de pendencias em andamento para distribuidor C�mara 
     * 
     * @author Leandro Bernardes
     * @param usuarioDt
     *            usuario que deseja a quantidade
    
     * @return List lista de pendencias
     * @throws Exception
     */
    public List consultarPendenciasServentiaDistribuidorCamara(UsuarioDt usuarioDt) throws Exception {
    	List pendencias = new ArrayList();
    	FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            
            pendencias = pendenciaPs.consultarPendenciasServentiaDistribuidorCamara(usuarioDt);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }
    
    public List consultarPendenciasServentiaAbertasPreAnalisada(UsuarioDt usuarioDt) throws Exception {
    	List pendencias = new ArrayList();
    	FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            
            pendencias = pendenciaPs.consultarPendenciasServentiaAbertasPreAnalisada(usuarioDt);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }
    
    /**
     * Consulta a lista de pendencias em andamento de uma determinada serventia 
     * 
     * @author Leandro Bernardes
     * @param usuarioDt
     *            usuario que deseja a quantidade
    
     * @return List lista de pendencias
     * @throws Exception
     */
    public List consultarPendenciasServentiaDRCAbertasEmAndamento(UsuarioDt usuarioDt) throws Exception {
    	List pendencias = new ArrayList();
    	FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            
            pendencias = pendenciaPs.consultarPendenciasServentiaDRCAbertasEmAndamento(usuarioDt);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }

    /**
     * Consulta a quantidade de pendencias em andamento de um determinado tipo
     * para o cargo serventia - Consulta inclusive para a serventia, para o tipo
     * de serventia, para o cargo do usuario, para o usuario resposavel
     * 
     * @author Leandro Bernardes
     * @since 10/08/2009
     * @param usuarioDt
     *            usuario que deseja a quantidade
     * @param tipo
     *            tipo da pendencia
     * @return int
     * @throws Exception
     */
   /*public int consultarQuantidadeCargoServentiaAbertasEmAndamento(UsuarioDt usuarioDt, String tipo) throws Exception {
        int qtd = 0;

        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            qtd = pendenciaPs.consultarQuantidadeCargoServentiaAbertasEmAndamento(usuarioDt, tipo);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return qtd;
    }*/
    
    /**
     * Consulta a lista de pendencias em andamento de um determinado cargo serventia 
     * 
     * @author Leandro Bernardes
     * @param usuarioDt
     *            usuario que deseja a quantidade
    
     * @return List lista de pendencias
     * @throws Exception
     */
    public List consultarPendenciasCargoServentiaAbertasEmAndamento(UsuarioDt usuarioDt) throws Exception {
    	List pendencias = new ArrayList();
    	FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            
            pendencias = pendenciaPs.consultarPendenciasCargoServentiaAbertasEmAndamento(usuarioDt);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }
    
    /**
     * Consulta a lista de pendencias em andamento de um determinado cargo serventia 
     * 
     * @author Alex Rocha
     * @param usuarioDt
     *            usuario que deseja a quantidade
    
     * @return List lista de pendencias
     * @throws Exception
     */
    public List consultarPendenciasCargoServentiaAbertasEmAndamentoOficial(UsuarioDt usuarioDt) throws Exception {
    	List pendencias = new ArrayList();
    	FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            
            pendencias = pendenciaPs.consultarPendenciasCargoServentiaAbertasEmAndamentoOficial(usuarioDt);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }

    /**
     * Consulta a quantidade de pendencias em andamento de um determinado tipo
     * para o serventia tipo - Consulta inclusive para a serventia, para o tipo
     * de serventia, para o cargo do usuario, para o usuario resposavel
     * 
     * @author Leandro Bernardes
     * @since 10/08/2009
     * @param usuarioDt
     *            usuario que deseja a quantidade
     * @param tipo
     *            tipo da pendencia
     * @return int
     * @throws Exception
     */
    public int consultarQuantidadeServentiaTipoAbertasEmAndamento(UsuarioDt usuarioDt, String tipo) throws Exception {
        int qtd = 0;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            qtd = pendenciaPs.consultarQuantidadeServentiaTipoAbertasEmAndamento(usuarioDt, tipo);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return qtd;
    }

    /**
     * Consulta pendencias reservadas
     * 
     * @author Ronneesley Moura Teles
     * @since 20/01/2009 16:04
     * @param usuarioNe
     *            usuario da sessao
     * @return lista de pendencias com o atributo pode liberar e o hash
     * @throws Exception
     */
    public List consultarReservadasPreAnalisadasComHash(UsuarioNe usuarioNe) throws Exception {
        List pendencias = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            pendencias = pendenciaPs.consultarReservadasPreAnalisadasComHash(usuarioNe);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }

    /**
     * Consulta pendencias reservadas por tipo
     * 
     * @author lsbernardes
     * @since 24/03/2009
     * @param usuarioNe
     *            usuario da sessao
     * @param tipo
     *            tipo da pendencia
     * @return lista de pendencias com o atributo pode liberar e o hash
     * @throws Exception
     */
    public List consultarReservadasComHash(UsuarioNe usuarioNe, String tipo, String filtro) throws Exception {
        List pendencias = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            pendencias = pendenciaPs.consultarReservadasComHash(usuarioNe, tipo, filtro);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }
    
    /**
     * Consulta de pend�ncias de mandados reservadas para o oficial. Utiliza a tabela mand_jud como refer�ncia. Consulta elaborada
	 * especificamente para o oficial de justi�a da Central de Mandados do Projudi. 
     * @param usuarioNe
     * @param tipo
     * @param filtro
     * @return
     * @throws Exception
     * @author hrrosa
     */
    public List consultarMandadosReservadosComHash(UsuarioNe usuarioNe, String tipo, String filtro, String ordenacao) throws Exception {
        List pendencias = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            pendencias = pendenciaPs.consultarMandadosReservadosComHash(usuarioNe, tipo, filtro, ordenacao);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }

    /**
     * Consulta pendencias reservadas
     * 
     * @author lsbernardes
     * @since 24/03/2009
     * @param usuarioNe
     *            usuario da sessao
     * @param tipo
     *            tipo da pendencia
     * @return lista de pendencias com o atributo pode liberar e o hash
     * @throws Exception
     */
    public List consultarReservadasComHash(UsuarioNe usuarioNe) throws Exception {
        List pendencias = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            pendencias = pendenciaPs.consultarReservadasComHash(usuarioNe, "", "");

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }

    /**
     * Consulta pendencias PreAnalisadas por tipo
     * 
     * @author lsbernardes
     * @since 24/03/2009
     * @param usuarioNe
     *            usuario da sessao
     * @param tipo
     *            tipo da pendencia
     * @return lista de pendencias com o atributo pode liberar e o hash
     * @throws Exception
     */
    public List consultarPreAnalisadasComHash(UsuarioNe usuarioNe, String tipo, String filtro) throws Exception {
        List pendencias = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            pendencias = pendenciaPs.consultarPreAnalisadasComHash(usuarioNe, tipo, filtro);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }

    /**
     * Consulta pendencias Abertas serventia cargo por tipo
     * 
     * @author lsbernardes
     * @since 10/08/2009
     * @param usuarioNe
     *            usuario da sessao
     * @param tipo
     *            tipo da pendencia
     * @param ehPendenteAssinatura
     * @return lista de pendencias
     * @throws Exception
     */
    public List consultarAbertasServentiaCargoComHash(UsuarioNe usuarioNe, String tipo, boolean ehPendenteAssinatura) throws Exception {
        List pendencias = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            pendencias = pendenciaPs.consultarAbertasServentiaCargoComHash(usuarioNe, tipo, ehPendenteAssinatura);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }

    /**
     * Consulta pendencias PreAnalisadas
     * 
     * @author lsbernardes
     * @since 24/03/2009
     * @param usuarioNe
     *            usuario da sessao
     * @param tipo
     *            tipo da pendencia
     * @return lista de pendencias com o atributo pode liberar e o hash
     * @throws Exception
     */
    public List consultarPreAnalisadasComHash(UsuarioNe usuarioNe) throws Exception {
        List pendencias = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            pendencias = pendenciaPs.consultarPreAnalisadasComHash(usuarioNe, "", "4");

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }

    /**
     * Consulta pendencias abertas da serventia
     * 
     * @author Ronneesley Moura Teles
     * @since 07/05/2008 - 16:34 Alteracoes 1 - Melhorias
     * @author Ronneesley Moura Teles
     * @since 08/05/2008 - 11:11
     * 
     * 2 - Incrementar paginacao
     * @author Ronneesley Moura Teles
     * @since 28/08/2008 16:54
     * 
     * 3 - Incrementar filtro de pendencia
     * @author Ronneesley Moura Teles
     * @since 09/12/2008 14:45
     * @param usuarioDt
     *            usuario da sessao
     * @param pendenciaTipoDt
     *            vo de tipo de pendencia
     * @param pendenciaStatusDt
     *            vo de status de pendencia
     * @param prioridade
     *            prioridade da listagem
     * @param filtroTipo
     *            filtro por tipo de pendencia, 1 somente processo, 2 somente
     *            normais os demais todas
     * @param numero_processo
     *            filtro para o numero do processo
     * @param dataInicialInicio
     *            data inicial para o inicio
     * @param dataFinalInicio
     *            data final para o inicio
     * @param posicao
     *            posicao da paginacao
     * @return List
     * @throws Exception
     */
    public List consultarAbertas(UsuarioNe usuarioNe, PendenciaTipoDt pendenciaTipoDt, PendenciaStatusDt pendenciaStatusDt, boolean prioridade, Integer filtroTipo, Integer filtroCivelCriminal, String numero_processo, String dataInicialInicio, String dataFinalInicio, String posicao) throws Exception {
        List pendencias = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            // Deixa a data inicial no inicio do dia
            if (dataInicialInicio != null && !dataInicialInicio.trim().equals("")) dataInicialInicio += " 00:00:00";
            // Deixa a data final ate o fim do dia
            if (dataFinalInicio != null && !dataFinalInicio.trim().equals("")) dataFinalInicio += " 23:59:59";

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            List tempList = pendenciaPs.consultarAbertas(usuarioNe, pendenciaTipoDt, pendenciaStatusDt, prioridade, filtroTipo, filtroCivelCriminal, numero_processo, dataInicialInicio, dataFinalInicio, posicao);

            setQuantidadePaginas(tempList);

            pendencias = (List) tempList;
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }

    public List consultarAbertasServentiaCargo(UsuarioNe usuarioNe, PendenciaTipoDt pendenciaTipoDt, PendenciaStatusDt pendenciaStatusDt, boolean prioridade, Integer filtroTipo, String numero_processo, String dataInicialInicio, String dataFinalInicio, String posicao) throws Exception {
        List pendencias = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            // Deixa a data inicial no inicio do dia
            if (dataInicialInicio != null && !dataInicialInicio.trim().equals("")) dataInicialInicio += " 00:00:00";
            // Deixa a data final ate o fim do dia
            if (dataFinalInicio != null && !dataFinalInicio.trim().equals("")) dataFinalInicio += " 23:59:59";

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            List tempList = pendenciaPs.consultarAbertasServentiaCargo(usuarioNe, pendenciaTipoDt, pendenciaStatusDt, prioridade, filtroTipo, numero_processo, dataInicialInicio, dataFinalInicio, posicao);

            setQuantidadePaginas(tempList);

            pendencias = (List) tempList;
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }
    
    /**
     * Consulta as pendencias do tipo conclus�o que est�o em aberto, e que o
     * respons�vel seja o serventiaCargo passado
     * 
     * @param usuarioDt, usuario que est� realizando a consulta
     * @param numeroProcesso, filtro por n�mero de processo
     * @param id_Classificador, filtro por classificador
     * @param id_PendenciaTipo, filtro para tipo de conclus�o a ser mostrada
     * @author msapaula / mmgomes
     */
    public List consultarConclusoesPendentesProjudi(UsuarioNe usuarioSessao, String numeroProcesso, String id_Classificador, String id_PendenciaTipo, boolean ehVoto, boolean ehVotoVencido, boolean ehIniciada) throws Exception {
        List pendencias = null;
        String id_ServentiaCargo = null;
        String digitoVerificador = null;
        FabricaConexao obFabricaConexao = null;        
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            //int grupo = Funcoes.StringToInt(usuarioDt.getGrupoCodigo());
            int grupoTipo =usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt();

            // Divide n�mero de processo do d�gito verificador
            String[] stTemp = numeroProcesso.split("[\\.-]");
            if (stTemp.length >= 1)
                numeroProcesso = stTemp[0];
            else
                numeroProcesso = "";
            if (stTemp.length >= 2) digitoVerificador = stTemp[1];

            switch (grupoTipo) {
//            case GrupoDt.JUIZES_VARA:
//            case GrupoDt.JUIZES_TURMA_RECURSAL:
//            case GrupoDt.DESEMBARGADOR:
//            case GrupoDt.ASSISTENTES_GABINETE:
              case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
              case GrupoTipoDt.JUIZ_TURMA:
              case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
              case GrupoTipoDt.PRESIDENTE_SEGUNDO_GRAU:
              case GrupoTipoDt.ASSISTENTE_GABINETE:
              case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
                // Se usu�rio � juiz busca os autos conclusos para o serventia cargo dele
                id_ServentiaCargo = usuarioSessao.getUsuarioDt().getId_ServentiaCargo();
                break;
                
			case GrupoTipoDt.JUIZ_LEIGO:
				
	            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());				
				String id_Serventia = usuarioSessao.getUsuarioDt().getId_Serventia();
		        List listaServentiaCargo = new ArrayList();

		        listaServentiaCargo = obPersistencia.consultarJuizesServentia(id_Serventia);
				
		        pendencias = new ArrayList();
		        
		        if (listaServentiaCargo != null){
		        	for(int i = 0; i < listaServentiaCargo.size(); i++) {
		        		pendencias.addAll(obPersistencia.consultarConclusoesPendentesProjudi(usuarioSessao, (String)listaServentiaCargo.get(i), numeroProcesso, digitoVerificador, id_Classificador, id_PendenciaTipo, ehVoto, ehVotoVencido, ehIniciada));
			        }
		        }
                
			break;
                

//            case GrupoDt.ASSISTENTES_JUIZES_VARA:
//            case GrupoDt.ASSISTENTES_JUIZES_SEGUNDO_GRAU:
              case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
              case GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:
              case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
                // Sendo assistente, busca serventiaCargo do Usu�rio chefe
                id_ServentiaCargo = usuarioSessao.getUsuarioDt().getId_ServentiaCargoUsuarioChefe();
                break;
            }

            if ( (id_ServentiaCargo == null || id_ServentiaCargo.equals("")) && !usuarioSessao.isJuizLeigo()){
            	throw new MensagemException("N�o foi poss�vel consultar as conclus�es pendentes. Usu�rio n�o est� em um Cargo da Serventia.");
            }
        	
            	
            if(!usuarioSessao.isJuizLeigo()){
            	PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            	pendencias = obPersistencia.consultarConclusoesPendentesProjudi(usuarioSessao, id_ServentiaCargo, numeroProcesso, digitoVerificador, id_Classificador, id_PendenciaTipo, ehVoto, ehVotoVencido, ehIniciada);
            }
            
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }
    
    /**
     * Consulta as pendencias do tipo conclus�o que est�o em aberto, e que o
     * respons�vel seja o serventiaCargo passado
     * 
     * @param usuarioDt, usuario que est� realizando a consulta
     * @param numeroProcesso, filtro por n�mero de processo
     * @param id_Classificador, filtro por classificador
     * @param id_PendenciaTipo, filtro para tipo de conclus�o a ser mostrada
     * @author msapaula / mmgomes
     */
    public List consultarConclusoesPendentesPJD(UsuarioNe usuarioSessao, String numeroProcesso, String id_Classificador, String id_PendenciaTipo, boolean ehVoto, boolean ehVotoVencido, boolean ehIniciada) throws Exception {
    	List pendencias = null;
        String id_ServentiaCargo = null;
        String digitoVerificador = null;
        FabricaConexao obFabricaConexao = null;        
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            //int grupo = Funcoes.StringToInt(usuarioDt.getGrupoCodigo());
            int grupoTipo =usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt();

            // Divide n�mero de processo do d�gito verificador
            String[] stTemp = numeroProcesso.split("\\.");
            if (stTemp.length >= 1)
                numeroProcesso = stTemp[0];
            else
                numeroProcesso = "";
            if (stTemp.length >= 2) digitoVerificador = stTemp[1];

            switch (grupoTipo) {
//            case GrupoDt.JUIZES_VARA:
//            case GrupoDt.JUIZES_TURMA_RECURSAL:
//            case GrupoDt.DESEMBARGADOR:
//            case GrupoDt.ASSISTENTES_GABINETE:
              case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
              case GrupoTipoDt.JUIZ_TURMA:
              case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
              case GrupoTipoDt.PRESIDENTE_SEGUNDO_GRAU:
              case GrupoTipoDt.ASSISTENTE_GABINETE:
              case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
                // Se usu�rio � juiz busca os autos conclusos para o serventia cargo dele
                id_ServentiaCargo = usuarioSessao.getUsuarioDt().getId_ServentiaCargo();
                break;
                
			case GrupoTipoDt.JUIZ_LEIGO:
				
	            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());				
				String id_Serventia = usuarioSessao.getUsuarioDt().getId_Serventia();
		        List listaServentiaCargo = new ArrayList();

		        listaServentiaCargo = obPersistencia.consultarJuizesServentia(id_Serventia);
				
		        pendencias = new ArrayList();
		        
		        if (listaServentiaCargo != null){
		        	for(int i = 0; i < listaServentiaCargo.size(); i++) {
		        		pendencias.addAll(obPersistencia.consultarConclusoesPendentesPJD(usuarioSessao, (String)listaServentiaCargo.get(i), numeroProcesso, digitoVerificador, id_Classificador, id_PendenciaTipo, ehVoto, ehVotoVencido, ehIniciada));
			        }
		        }
                
			break;
                

//            case GrupoDt.ASSISTENTES_JUIZES_VARA:
//            case GrupoDt.ASSISTENTES_JUIZES_SEGUNDO_GRAU:
              case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
              case GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:
              case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
                // Sendo assistente, busca serventiaCargo do Usu�rio chefe
                id_ServentiaCargo = usuarioSessao.getUsuarioDt().getId_ServentiaCargoUsuarioChefe();
                break;
            }

            if ( (id_ServentiaCargo == null || id_ServentiaCargo.equals("")) && !usuarioSessao.isJuizLeigo()){
            	throw new MensagemException("N�o foi poss�vel consultar as conclus�es pendentes. Usu�rio n�o est� em um Cargo da Serventia.");
            }
        	
            	
            if(!usuarioSessao.isJuizLeigo()){
            	PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            	pendencias = obPersistencia.consultarConclusoesPendentesPJD(usuarioSessao, id_ServentiaCargo, numeroProcesso, digitoVerificador, id_Classificador, id_PendenciaTipo, ehVoto, ehVotoVencido, ehIniciada);
            }
            
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }
    
    /**
     * Consulta as pendencias do tipo conclus�o que est�o em aberto na sess�o virtual
     * lrcampos 19/03/2020 - Incluindo parametro para filtrar pelo idServentia
     * @param usuarioDt, usuario que est� realizando a consulta
     * @param idServentia, filtro para serventia a ser mostrada
     * @param numeroProcesso, filtro por n�mero de processo
     * @param id_Classificador, filtro por classificador
     * @param id_PendenciaTipo, filtro para tipo de conclus�o a ser mostrada
     * @author lrcampos
	 * @since 14/10/2019 15:30
     */
    public List consultarConclusoesPendentesVirtual(UsuarioNe usuarioSessao, String idServentia, String numeroProcesso, String id_Classificador, String id_PendenciaTipo, boolean ehVoto, boolean ehVotoVencido, boolean ehIniciada) throws Exception {
    	  List pendencias = null;
          String id_ServentiaCargo = null;
          String digitoVerificador = null;
          Boolean isVirtual = true;
          FabricaConexao obFabricaConexao = null;
          try {
              obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
              //int grupo = Funcoes.StringToInt(usuarioDt.getGrupoCodigo());
              int grupoTipo =usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt();

              // Divide n�mero de processo do d�gito verificador
              String[] stTemp = numeroProcesso.split("[\\.-]");
              if (stTemp.length >= 1)
                  numeroProcesso = stTemp[0];
              else
                  numeroProcesso = "";
              if (stTemp.length >= 2) digitoVerificador = stTemp[1];

              switch (grupoTipo) {
                case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
                case GrupoTipoDt.JUIZ_TURMA:
                case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
                case GrupoTipoDt.PRESIDENTE_SEGUNDO_GRAU:
                case GrupoTipoDt.ASSISTENTE_GABINETE:
                case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
                  // Se usu�rio � juiz busca os autos conclusos para o serventia cargo dele
                  id_ServentiaCargo = usuarioSessao.getUsuarioDt().getId_ServentiaCargo();
                  break;
                  
  			case GrupoTipoDt.JUIZ_LEIGO:
  				
  	            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
  				String id_Serventia = usuarioSessao.getUsuarioDt().getId_Serventia();
  		        List listaServentiaCargo = new ArrayList();

  		        listaServentiaCargo = obPersistencia.consultarJuizesServentia(id_Serventia);
  				
  		        pendencias = new ArrayList();
  		        
  		        if (listaServentiaCargo != null){
  		        	for(int i = 0; i < listaServentiaCargo.size(); i++) {
  		        		pendencias.addAll(obPersistencia.consultarConclusoesPendentesProjudi(usuarioSessao, (String)listaServentiaCargo.get(i), numeroProcesso, digitoVerificador, id_Classificador, id_PendenciaTipo, ehVoto, ehVotoVencido, ehIniciada));
  			        }
  		        }
                  
  			break;
                  

                case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
                case GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:
                case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
                  // Sendo assistente, busca serventiaCargo do Usu�rio chefe
                  id_ServentiaCargo = usuarioSessao.getUsuarioDt().getId_ServentiaCargoUsuarioChefe();
                  break;
              }

              if ( (id_ServentiaCargo == null || id_ServentiaCargo.equals("")) && !usuarioSessao.isJuizLeigo()){
              	throw new MensagemException("N�o foi poss�vel consultar as conclus�es pendentes. Usu�rio n�o est� em um Cargo da Serventia.");
              }
          	
              	
              if(!usuarioSessao.isJuizLeigo()){
              	PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
              	pendencias = obPersistencia.consultarConclusoesPendentesVirtual(usuarioSessao.getUsuarioDt(),idServentia, id_ServentiaCargo, numeroProcesso, digitoVerificador, id_Classificador, id_PendenciaTipo, ehVoto, ehVotoVencido, ehIniciada);
              }
              
          } finally {
              obFabricaConexao.fecharConexao();
          }

          return pendencias;
    }
    
    public List consultarConclusoesPendentesAssistenteGabinete(UsuarioNe usuarioSessao, String numeroProcesso, String id_Classificador, String id_PendenciaTipo, String id_ServentiaGrupo,  boolean ehVoto, boolean ehVotoVencido) throws Exception {
        List pendencias = null;
        String id_ServentiaCargo = null;
        String digitoVerificador = null;
        FabricaConexao obFabricaConexao = null;        
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            int grupoTipo = usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt();

            // Divide n�mero de processo do d�gito verificador
            String[] stTemp = numeroProcesso.split("\\.");
            if (stTemp.length >= 1)
                numeroProcesso = stTemp[0];
            else
                numeroProcesso = "";
            if (stTemp.length >= 2) digitoVerificador = stTemp[1];

            switch (grupoTipo) {
              case GrupoTipoDt.ASSISTENTE_GABINETE:
              case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
                // Se usu�rio � juiz busca os autos conclusos para o serventia cargo dele
                id_ServentiaCargo = usuarioSessao.getUsuarioDt().getId_ServentiaCargo();
                break;
            }

            if (id_ServentiaCargo == null || id_ServentiaCargo.equals("") && !usuarioSessao.isJuizLeigo()) throw new MensagemException("N�o foi poss�vel consultar as conclus�es pendentes. Usu�rio n�o est� em um Cargo da Serventia.");

            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            pendencias = obPersistencia.consultarConclusoesPendentesAssistenteGabinete(usuarioSessao, id_ServentiaCargo, numeroProcesso, digitoVerificador, id_Classificador, id_PendenciaTipo, id_ServentiaGrupo, ehVoto, ehVotoVencido);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }
    
    /**
     * Consulta as pendencias que est�o em aberto, e que n�o possuem nenhuma pr�-an�lise registrada, e que o respons�vel seja o serventiaCargo passado.
     * Trata as pendencias que ter�o tratamento semelhantes �s conclus�es
     * 
     * @param usuarioDt, usuario que est� realizando a consulta
     * @param numeroProcesso, filtro por n�mero de processo
     * @param id_PendenciaTipo, filtro para tipo de conclus�o a ser mostrada
     * @author msapaula
     */
    public List consultarPendenciasNaoAnalisadas(UsuarioDt usuarioDt, String numeroProcesso, String id_PendenciaTipo) throws Exception {
        List pendencias = null;
        String digitoVerificador = null;
        FabricaConexao obFabricaConexao = null;        
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            //int grupo = Funcoes.StringToInt(usuarioDt.getGrupoCodigo());
            int grupoTipo = Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo());

            // Divide n�mero de processo do d�gito verificador
            String[] stTemp = numeroProcesso.split("\\.");
            if (stTemp.length >= 1)
                numeroProcesso = stTemp[0];
            else
                numeroProcesso = "";
            if (stTemp.length >= 2) digitoVerificador = stTemp[1];

            switch (grupoTipo) {
//            case GrupoDt.JUIZES_VARA:
//            case GrupoDt.DESEMBARGADOR:
//            case GrupoDt.ASSISTENTES_GABINETE:
//            case GrupoDt.MINISTERIO_PUBLICO:
	          case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
	          case GrupoTipoDt.JUIZ_TURMA:
	          case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
	          case GrupoTipoDt.ASSISTENTE_GABINETE:
	          case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
                if (usuarioDt.getId_ServentiaCargo() != null && usuarioDt.getId_ServentiaCargo().length() > 0){
                	pendencias = obPersistencia.consultarPendenciasNaoAnalisadas(usuarioDt.getId_ServentiaCargo(), null, numeroProcesso, digitoVerificador, id_PendenciaTipo);
                }
                break;
             
	        case GrupoTipoDt.MP:
	        	if (usuarioDt.getId_ServentiaCargo() != null && usuarioDt.getId_ServentiaCargo().length() > 0){
                	pendencias = obPersistencia.consultarPendenciasNaoAnalisadasCargo(usuarioDt.getId_ServentiaCargo(), numeroProcesso, digitoVerificador, id_PendenciaTipo);
                }
	        	break;
                
            //case GrupoDt.ADVOGADOS:
	        case GrupoTipoDt.ADVOGADO:
               	pendencias = obPersistencia.consultarPendenciasNaoAnalisadas(usuarioDt.getId_UsuarioServentia(), numeroProcesso, digitoVerificador, id_PendenciaTipo);
            	break;
            	
            //case GrupoDt.ASSISTENTES_JUIZES_VARA:
	         case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
	         case GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:
	         case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
            	if (usuarioDt.getId_ServentiaCargoUsuarioChefe() != null && !usuarioDt.getId_ServentiaCargoUsuarioChefe().equals("")) {
            		pendencias = obPersistencia.consultarPendenciasNaoAnalisadas(usuarioDt.getId_ServentiaCargoUsuarioChefe(), null, numeroProcesso, digitoVerificador, id_PendenciaTipo);
				}
				break;
                
            //case GrupoDt.ASSISTENTES_ADVOGADOS_PROMOTORES:
	        case GrupoTipoDt.ASSESSOR_MP:
				if (usuarioDt.getId_ServentiaCargoUsuarioChefe() != null && !usuarioDt.getId_ServentiaCargoUsuarioChefe().equals("")) {
					pendencias = obPersistencia.consultarPendenciasNaoAnalisadasCargo(usuarioDt.getId_ServentiaCargoUsuarioChefe(), numeroProcesso, digitoVerificador, id_PendenciaTipo);
				}
				break;	        	
            case GrupoTipoDt.ASSESSOR_ADVOGADO:
            	if (usuarioDt.getGrupoUsuarioChefe() != null && usuarioDt.getGrupoUsuarioChefe().length() > 0) {
					pendencias = obPersistencia.consultarPendenciasNaoAnalisadas(usuarioDt.getId_UsuarioServentiaChefe(), numeroProcesso, digitoVerificador, id_PendenciaTipo);
             	}
            	break;
            }
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }

    /**
     * Distribui uma pendencia para um determinado usuario da serventia
     * 
     * @author Ronneesley Moura Teles
     * @since 16/05/2008 17:04
     * @param idPendencia
     *            id da pendencia
     * @param idUsuarioServentia
     *            id do usuario da serventia
     * @return boolean
     */
    public boolean distribuir(String idPendencia, String idUsuarioServentia, int grupoCodigo) throws Exception {
        boolean resposta = false;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            
            DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Calendar calCalendario = Calendar.getInstance();
            calCalendario.setTime(new Date());
            
            switch (grupoCodigo) {
			case GrupoDt.CONTADORES_VARA:
				calCalendario.add(Calendar.HOUR, 120); //Ocorrencia 2021/3030
				break;
			case GrupoDt.CONTADOR_PROCURADORIA_MUNICIPAL:
				calCalendario.add(Calendar.HOUR, 48);
				break;
			default:
				calCalendario.add(Calendar.HOUR, 1);
				break;
			}

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            resposta = pendenciaPs.distribuir(idPendencia, idUsuarioServentia,calCalendario.getTime());

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return resposta;
    }

    public int[] consultarUltimaDistribuicaoMandado(String id_escala) throws Exception {
        int inUltimo[];
        ////System.out.println("..ne-consultarUltimaDistribuicaoMandado");
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            inUltimo = obPersistencia.consultarUltimaDistribuicaoMandado(id_escala);

        } finally {
            obFabricaConexao.fecharConexao();
        }
        return inUltimo;
    }

    /**
     * Liberar uma pendencia que o usuario reservou
     * 
     * @author Ronneesley Moura Teles
     * @since 23/06/2008 14:28
     * @param String
     *            id
     * @return boolean
     */
    public boolean liberar(String id) throws Exception {
        boolean resposta = false;
        FabricaConexao obFabricaConexao = null;        
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            resposta = pendenciaPs.liberar(id);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return resposta;

    }

    /**
     * Consulta modelos que poder�o ser utilizados na resolu��o de pend�ncias.
     * Retorna os modelos que o usu�rio passado poder� visualizar e de um
     * determinado tipo de arquivo
     * 
     * @param descricao
     *            filtro para consulta
     * @param posicao
     *            par�metro para pagina��o
     * @param id_ArquivoTipo
     *            id do tipo de arquivo
     * @param usuarioDt
     *            dt do usu�rio que est� consultando os modelos
     * @return List
     * @author Ronneesley Moura Teles
     * @author msapaula
     * 
     * @since 07/04/2009 09:27
     */
    public List consultarDescricaoModeloPorTipo(String descricao, String posicao, String id_ArquivoTipo, UsuarioDt usuarioDt) throws Exception {
        ModeloNe modeloNe = new ModeloNe();
        List lista = modeloNe.consultarModelos(descricao,  posicao, id_ArquivoTipo, usuarioDt);
        this.QuantidadePaginas = modeloNe.getQuantidadePaginas();
        modeloNe = null;
        return lista;
    }

    /**
     * Consulta modelos que poder�o ser utilizados na resolu��o de pend�ncias.
     * Retorna os modelos que o usu�rio passado poder� visualizar e de um
     * determinado tipo de arquivo
     * 
     * @param descricao
     *            filtro para consulta
     * @param posicao
     *            par�metro para pagina��o
     * @param id_ArquivoTipo
     *            id do tipo de arquivo
     * @param usuarioDt
     *            dt do usu�rio que est� consultando os modelos
     * @return List
     * @author Ronneesley Moura Teles
     * @author msapaula
     * @author jrcorrea 01/10/2014
     * 
     * @since 07/04/2009 09:27
     */
    public String consultarDescricaoModeloTipoJSON(String descricao, String posicao, String id_ArquivoTipo, UsuarioDt usuarioDt) throws Exception {
        ModeloNe modeloNe = new ModeloNe();
        String stTemp = modeloNe.consultarModelosJSON(descricao,  posicao, id_ArquivoTipo, usuarioDt);
        this.QuantidadePaginas = modeloNe.getQuantidadePaginas();
        modeloNe = null;
        return stTemp;
    }
    
    /**
     * Monta modelo de uma pendencia
     * 
     * @author Ronneesley Moura Teles
     * @since 23/06/2008 16:49
     * @param pendenciaDt
     *            pojo da pendencia
     * @param modeloDt
     *            pojo do modelo
     * @param usuarioNe
     *            objeto de negocio do usuario [Utiliza-se o usuario logado]
     * @return String
     * @throws Exception
     */
    public String montaModelo(PendenciaDt pendenciaDt, ModeloDt modeloDt, UsuarioNe usuarioNe) throws Exception {
        ModeloNe modeloNe = new ModeloNe();

        return modeloNe.montaConteudoPorModelo(pendenciaDt, usuarioNe.getUsuarioDt(), modeloDt.getId());
    }

    /**
     * Compementa a montagem do modelo com a substitui��o das vari�veis referentes � expedi��o
     * de mandado para a Central de Mandados.
     * @param numeroReservadoMandadoExpedir
     * @param texto
     * @return
     */
	public String montaConteudoMandadoExpedicao(String numeroReservadoMandadoExpedir, String texto) {
		return new ModeloNe().montaConteudoMandadoExpedicao(numeroReservadoMandadoExpedir, texto);
	}
    
    /**
     * Consulta de tipos de serventia
     * 
     * @author Ronneesley Moura Teles
     * @since 20/10/2008 10:54
     * @param descricao
     *            descricao da serventia tipo
     * @return List
     * @throws Exception
     */
    public List consultarDescricaoServentiaTipo(String descricao, UsuarioDt usuarioDt) throws Exception {
        ServentiaTipoNe serventiaTipoNe = new ServentiaTipoNe();
        List lista = serventiaTipoNe.consultarServentiaTipo(descricao, usuarioDt);
        this.QuantidadePaginas = serventiaTipoNe.getQuantidadePaginas();
        serventiaTipoNe = null;
        return lista;
    }

    /**
     * Consulta de tipos de arquivos
     * 
     * @author Ronneesley Moura Teles
     * @since 26/06/2008 10:45
     * @param codigoGrupoUsuario
     *            codigo do grupo do usuario logado
     * @param descricao
     *            descricao do arquivo
     * @param posicao
     *            posicionamento da paginacao
     * @return List
     * @throws Exception
     */
    public List consultarDescricaoArquivoTipo(String codigoGrupoUsuario, String descricao, String posicao) throws Exception {
        ArquivoTipoNe arquivoTipoNe = new ArquivoTipoNe();
        List lista = arquivoTipoNe.consultarGrupoArquivoTipo(codigoGrupoUsuario, descricao, posicao);
        this.QuantidadePaginas = arquivoTipoNe.getQuantidadePaginas();
        arquivoTipoNe = null;
        return lista;
    }

    /**
     * Consulta de serventias
     * 
     * @author Ronneesley Moura Teles
     * @since 26/09/2008 14:29
     * @param descricao
     *            descricao da serventia
     * @param id_serventiaTipo
     *            id Serventia Tipo da Serventia
     * @param posicao
     *            posicionamento da paginacao
     * @return List
     * @throws Exception
     */
    public List consultarDescricaoServentia(String descricao, String id_serventiaTipo, UsuarioDt usuarioDt, String posicao) throws Exception {
        ServentiaNe serventiaNe = new ServentiaNe();
        ServentiaTipoNe serventiaTipoNe = new ServentiaTipoNe();
        String serventiaTipoCodigo = "";

        if (id_serventiaTipo != null && !id_serventiaTipo.equals("")) serventiaTipoCodigo = serventiaTipoNe.consultarCodigoServentiaTipo(id_serventiaTipo);

        List lista = serventiaNe.consultarDescricaoServentia(descricao, serventiaTipoCodigo, usuarioDt, posicao);
        this.QuantidadePaginas = serventiaNe.getQuantidadePaginas();
        serventiaNe = null;
        return lista;
    }
    
    public String consultarDescricaoServentiaJSON(String descricao, String id_serventiaTipo, UsuarioDt usuarioDt, String posicao) throws Exception {
    	String stTemp = "";
    	
    	ServentiaTipoNe serventiaTipoNe = new ServentiaTipoNe();
        String serventiaTipoCodigo = "";
        if (id_serventiaTipo != null && !id_serventiaTipo.equals("")) serventiaTipoCodigo = serventiaTipoNe.consultarCodigoServentiaTipo(id_serventiaTipo);
    	
    	ServentiaNe serventiaNe = new ServentiaNe();
		stTemp = serventiaNe.consultarDescricaoServentiaJSON(descricao, serventiaTipoCodigo, usuarioDt, posicao);
		
		return stTemp;
    }
    
    public String consultarDescricaoCentralMandadoRelacionadasJSON(String idCentralMandadosRelacionada, String descricao, UsuarioDt usuarioDt, String posicao) throws Exception {
    	String stTemp = "";
    	
    	ServentiaTipoNe serventiaTipoNe = new ServentiaTipoNe();
        String serventiaTipoCodigo = "";
    	
    	ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();
		stTemp = serventiaRelacionadaNe.consultarDescricaoCentralMandadoRelacionadasJSON(idCentralMandadosRelacionada, descricao, usuarioDt, posicao);
		
		return stTemp;
    }
    
    /**
     * Consulta de comarcas
     * 
     * @author Leandro Bernardes
     * @param descricao
     *            descricao da comarca
     * @param posicao
     *            posicionamento da paginacao
     * @return List
     * @throws Exception
     */
    public List consultarDescricaoComarca(String descricao, String posicao) throws Exception {
        ComarcaNe comarcaNe = new ComarcaNe();

        List lista = comarcaNe.consultarDescricao(descricao, posicao);
        this.QuantidadePaginas = comarcaNe.getQuantidadePaginas();
        comarcaNe = null;
        return lista;
    }    
	
	public String consultarDescricaoComarcaJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		ComarcaNe Comarcane = new ComarcaNe(); 
		stTemp = Comarcane.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}

    /**
     * Consultar os cargos pela descricao, somente os cargos da serventai do
     * usuario informado
     * 
     * @author Ronnneesley Moura Teles
     * @since 28/11/2008 17:02
     * @param idServentia
     *            id da serventia
     * @param descricao
     *            descricao da serventia
     * @param posicao
     *            posicao da paginacao
     * @return List
     * @throws Exception
     */
    public List consultarDescricaoServentiaCargoPorServentia(String idServentia, String descricao, String posicao) throws Exception {
        ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
        List lista = serventiaCargoNe.consultarServentiaCargos(descricao, posicao, idServentia);
        this.QuantidadePaginas = serventiaCargoNe.getQuantidadePaginas();
        serventiaCargoNe = null;
        return lista;
    }
    
    /**
     * Consultar os cargos pela descricao, somente os cargos da serventia informada
     * 
     * @param idServentia
     *            id da serventia
     * @param descricao
     *            descricao da serventia
     * @param posicao
     *            posicao da paginacao
     * @return String
     * @throws Exception
     */
    public String consultarDescricaoServentiaCargoPorServentiaJSON(String descricao, String idServentia,  String posicao) throws Exception {
        ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
        String stTemp = "";
        stTemp = serventiaCargoNe.consultarServentiaCargosJSON(descricao, idServentia, posicao);
        serventiaCargoNe = null;
        return stTemp;
    }
    
    /**
     * Consultar os cargos pela descricao, somente os cargos da serventia informada e que s�o ocupados por juizes
     * 
     * @param idServentia
     *            id da serventia
     * @param descricao
     *            descricao da serventia
     * @param posicao
     *            posicao da paginacao
     * @return String
     * @throws Exception
     */
    public String consultarDescricaoServentiaCargoMagistradosPorServentiaJSON(String descricao, String idServentia,  String posicao) throws Exception {
        ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
        String stTemp = "";
        stTemp = serventiaCargoNe.consultarDescricaoServentiaCargoMagistradosPorServentiaJSON(descricao, idServentia, posicao);
        serventiaCargoNe = null;
        return stTemp;
    }

    /**
     * Consulta de usuarios da serventia que podem analisar pend�ncias em uma determinada serventia, para poder exibir
     * na lista de direcionamento de pendencia
     * 
     * @author Ronneesley Moura Teles
     * @since 04/07/2008 16:53
     * @param String
     *            idServentia, id da serventia do usuario que esta logado
     * @return List
     * @throws Exception
     */
    public List consultarTodosUsuariosAnalisamPendencias(String idServentia) throws Exception {
        UsuarioServentiaNe usuariosServentiaNe = new UsuarioServentiaNe();
        return usuariosServentiaNe.consultarTodosUsuariosAnalisamPendencias(idServentia);
    }

    /**
     * M�todo respons�vel em salvar pend�ncia determinadas por um usu�rio, seja na movimenta��o gen�rica de um processo ou 
     * na an�lise/pr�-analise de autos conclusos.
     * 
     * A partir de um objeto "dwrMovimentarProcesso" que prov�m da lista de pend�ncias selecionadas pelo usu�rio, ser� gerada a pend�ncia
     * correspondente. 
     * Para cada dwrMovimentarProcesso verificar : 
     * 		Atributo CodDestinatario = [identificador - codigo] composto pelo identificador do 
     * 			tipo de destinat�rio + c�digo da parte ou serventia 
     * 		Identificador=>
     * 			Identificador 1 = todos promoventes 
     * 			Identificador 2 = todos promovidos
     * 			Identificador 3 = pend�ncia vinculada a uma parte espec�fica (deve ser passado o Id da parte) 
     * 			Identificador 4 = pend�ncia para Minist�rio P�blico
     * 
     * @param dwrMovimentarProcesso, objeto com dados das pend�ncias que devem ser geradas
     * @param movimentacaoDt, movimenta��o que ser� vinculada a pendencia
     * @param processoDt, processo vinculado a pend�ncia
     * @param pendenciaPai, pend�ncia pai das pend�ncias que ser�o geradas
     * @param arquivos, arquivos que devem ser vinculados as pend�ncias que ser�o geradas
     * @param usuarioCadastrador, usu�rio que est� gerando as pend�ncias
     * @param logDt, objeto com dados do log
     * @param obFabricaConexao, conexao ativa
     * 
     * @author msapaula
     */
    public void salvarPendencias(dwrMovimentarProcesso dados, MovimentacaoDt movimentacaoDt, ProcessoDt processoDt, PendenciaDt pendenciaPai, List arquivos, UsuarioDt usuarioCadastrador, String id_Classificador, LogDt logDt, FabricaConexao obFabricaConexao) throws MensagemException, Exception {
        boolean pessoalAdvogados = false;
        boolean pessoal = false;
        boolean intimacaoAudiencia = false;
        PendenciaDt pendenciaDt = new PendenciaDt();
        // Monta objeto pend�ncia a partir do dwrMovimentarProcesso
        pendenciaDt.setPendenciaTipoCodigo(dados.getCodPendenciaTipo());
        pendenciaDt.setId_Movimentacao(movimentacaoDt.getId());
        pendenciaDt.setMovimentacao(movimentacaoDt.getMovimentacaoTipo() + " - " + movimentacaoDt.getDataRealizacao());
        pendenciaDt.setId_Processo(processoDt.getId());
        pendenciaDt.setProcessoDt(processoDt);
        pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_EM_ANDAMENTO));
        pendenciaDt.setPrazo(dados.getPrazo());
        pendenciaDt.setDataLimite(dados.getDataLimite());
        pendenciaDt.setId_UsuarioCadastrador(usuarioCadastrador.getId_UsuarioServentia());
        
        //Atributos Correios
        pendenciaDt.setExpedicaoAutomatica(dados.isExpedicaoAutomatica());
        pendenciaDt.setId_ProcessoCustaTipo(processoDt.getId_Custa_Tipo());
        pendenciaDt.setCodModeloCorreio(dados.getCodExpedicaoAutomatica());
        pendenciaDt.setMaoPropriaCorreio(Boolean.toString(dados.getMaoPropria()));
        pendenciaDt.setOrdemServico(Boolean.toString(dados.getOrdemServico()));
        
        // Todas as pend�ncias de processo devem ter DataVisto setada
        pendenciaDt.setDataVisto(Funcoes.DataHora(new Date()));
        pendenciaDt.setId_UsuarioLog(logDt.getId_Usuario());
        pendenciaDt.setIpComputadorLog(logDt.getIpComputador());
        if (pendenciaPai != null) pendenciaDt.setId_PendenciaPai(pendenciaPai.getId());
        //Altera��o... Devido todo processo agora possuir prioridada de formar obrigat�ria, mesmo que seja NORMAL. Assim sendo invertemos a l�gica de urg�ncia da pend�ncia.
        if (dados.getUrgencia()!= null && dados.getUrgencia().equalsIgnoreCase("Sim")){
        	pendenciaDt.setProcessoPrioridadeCodigo(String.valueOf(ProcessoPrioridadeDt.LIMINAR));
        } else if (processoDt.getProcessoPrioridadeCodigo() != null && processoDt.getProcessoPrioridadeCodigo().length() > 0) {
        	pendenciaDt.setProcessoPrioridadeCodigo(processoDt.getProcessoPrioridadeCodigo());
        }
        if (dados.getPessoalAdvogado().equalsIgnoreCase("Sim")) pessoalAdvogados = true;
        if (dados.getPessoal().equalsIgnoreCase("Sim")) pessoal = true;
        if (dados.getIntimacaoAudiencia().equalsIgnoreCase("Sim")) intimacaoAudiencia = true;
        
        if (dados.isExpedicaoAutomatica()) {
        	pessoal = true;
        	pessoalAdvogados = false;
        	intimacaoAudiencia = false;
        }

        PendenciaResponsavelDt responsavel = new PendenciaResponsavelDt();
        PendenciaTipoNe pendenciaTipoNe = new PendenciaTipoNe();

        // Trata se � um tipo de Conclus�o
        if (pendenciaTipoNe.Conclusao(dados.getCodPendenciaTipo())){
            this.gerarConclusao(pendenciaDt, processoDt, dados.getCodPendenciaTipo(), dados.getIdDestinatario(), dados.getDestinatario(), arquivos, id_Classificador, logDt, obFabricaConexao);
        } else if (PendenciaTipoDt.isCalculoContadoria(Funcoes.StringToInt(dados.getCodPendenciaTipo()))) {
            // Para pedido de c�lculo redireciona fluxo
            this.gerarPendenciaPedidoCalculoContadoria(pendenciaDt, processoDt, responsavel, arquivos, obFabricaConexao);
        } else if (Funcoes.StringToInt(dados.getCodPendenciaTipo()) == PendenciaTipoDt.PEDIDO_CENOPES) {
            // Para pedido CENOPES redireciona fluxo
            this.gerarPendenciaPedidoCENOPES(pendenciaDt, processoDt, responsavel, arquivos, obFabricaConexao);
        } else if (Funcoes.StringToInt(dados.getCodPendenciaTipo()) == PendenciaTipoDt.PEDIDO_NATJUS_SEGUNDO_GRAU) {
            // Para pedido de camara de saude redireciona fluxo
            this.gerarPendenciaPedidoCamaraSaude(pendenciaDt, processoDt, responsavel, arquivos, obFabricaConexao);
        } else if (Funcoes.StringToInt(dados.getCodPendenciaTipo()) == PendenciaTipoDt.PEDIDO_LAUDO || Funcoes.StringToInt(dados.getCodPendenciaTipo()) == PendenciaTipoDt.AGENDAMENTO_PERICIA) {
            // Para pedido de Laudo redireciona fluxo
            this.gerarPendenciaServentiaEquipeInterProfissional(pendenciaDt, processoDt, responsavel, arquivos, obFabricaConexao);
        } else if (Funcoes.StringToInt(dados.getCodPendenciaTipo()) == PendenciaTipoDt.PEDIDO_JUSTICA_RESTAURATIVA) {
            // Para pedido de Justi�a Restaurativa redireciona fluxo
            this.gerarPendenciaServentiaEquipeInterProfissionalJusticaRestaurativa(pendenciaDt, processoDt, responsavel, arquivos, obFabricaConexao);
        } else if (Funcoes.StringToInt(dados.getCodPendenciaTipo()) == PendenciaTipoDt.RELATORIO) {
            // Para Relat�rio redireciona fluxo
            this.gerarPendenciaRelatorioSegundoGrau(pendenciaDt, processoDt, arquivos, obFabricaConexao);
        } else if (Funcoes.StringToInt(dados.getCodPendenciaTipo()) == PendenciaTipoDt.REVISAO) {
            // Para Revisao redireciona fluxo
            this.gerarPendenciaRevisaoSegundoGrau(pendenciaDt, processoDt, arquivos, obFabricaConexao);
        } else {
            // Trata pend�ncias normais com destinat�rio
            switch (dados.getTipoDestinatario()) {
            case 1: // TODOS PROMOVENTES
                this.gerarPendenciaPartesProcesso1N(processoDt, pendenciaDt, ProcessoParteTipoDt.POLO_ATIVO_CODIGO, pessoalAdvogados, pessoal, intimacaoAudiencia, arquivos, usuarioCadastrador, responsavel, obFabricaConexao);
                break;

            case 2: // TODOS PROMOVIDOS
                this.gerarPendenciaPartesProcesso1N(processoDt, pendenciaDt, ProcessoParteTipoDt.POLO_PASSIVO_CODIGO, pessoalAdvogados, pessoal, intimacaoAudiencia, arquivos, usuarioCadastrador, responsavel, obFabricaConexao);
                break;

            case 3: // PARTE ESPEC�FICA
                ProcessoParteDt parteDt = new ProcessoParteDt();
                parteDt.setId(dados.getIdDestinatario());
                parteDt.setNome(dados.getDestinatario());
                
                String id_serventia = usuarioCadastrador.getId_Serventia();
            	if (usuarioCadastrador.isGabinete() || usuarioCadastrador.isGabineteUPJ()) {
            		id_serventia = processoDt.getId_Serventia();
            	}
            	
                this.gerarPendenciaParteProcesso2N(pendenciaDt, parteDt, pessoalAdvogados, pessoal, intimacaoAudiencia, id_serventia, arquivos, usuarioCadastrador, processoDt, responsavel, obFabricaConexao);
                break;

            case 4: // MINIST�RIO P�BLICO 1 Grau
            	this.gerarPendenciaIntimacaoMinisterioPublicoPrimeiroSegundoGrau(pendenciaDt, processoDt, responsavel, arquivos, usuarioCadastrador, String.valueOf(ServentiaSubtipoDt.MP_PRIMEIRO_GRAU), intimacaoAudiencia, logDt, obFabricaConexao);
                break;
            case 7:// MINIST�RIO P�BLICO Turma Julgadora
            	this.gerarPendenciaIntimacaoMinisterioPublicoPrimeiroSegundoGrau(pendenciaDt, processoDt, responsavel, arquivos, usuarioCadastrador, String.valueOf(ServentiaSubtipoDt.MP_TURMA_JULGADORA), intimacaoAudiencia, logDt, obFabricaConexao);
            	break;
            case 8:// MINIST�RIO P�BLICO 2 Grau
            	this.gerarPendenciaIntimacaoMinisterioPublicoPrimeiroSegundoGrau(pendenciaDt, processoDt, responsavel, arquivos, usuarioCadastrador, String.valueOf(ServentiaSubtipoDt.MP_SEGUNDO_GRAU), intimacaoAudiencia, logDt, obFabricaConexao);
            	break;
                
            case 5: // OP��ES PEDIDO DE VISTA
            	if (ServentiaSubtipoDt.isSegundoGrau( usuarioCadastrador.getServentiaSubtipoCodigo()) ){ 
            		this.gerarPendenciaPedidoVistaSegundoGrau(pendenciaDt, processoDt, dados.getIdDestinatario(), dados.getDestinatario(), arquivos, obFabricaConexao);
            	
            	} else if (ServentiaSubtipoDt.isTurma(usuarioCadastrador.getServentiaSubtipoCodigo())){
            		this.gerarPendenciaPedidoVistaTurma(pendenciaDt, processoDt, dados.getIdDestinatario(), dados.getDestinatario(), arquivos, obFabricaConexao);
            	}
            	break;
            	
            case 6: // AMBAS AS PARTES -> TODOS PROMOVENTES e TODOS PROMOVIDOS
                this.gerarPendenciaPartesProcesso1N(processoDt, pendenciaDt, ProcessoParteTipoDt.POLO_ATIVO_CODIGO, pessoalAdvogados, pessoal, intimacaoAudiencia, arquivos, usuarioCadastrador, responsavel, obFabricaConexao);      
                this.gerarPendenciaPartesProcesso1N(processoDt, pendenciaDt, ProcessoParteTipoDt.POLO_PASSIVO_CODIGO, pessoalAdvogados, pessoal, intimacaoAudiencia, arquivos, usuarioCadastrador, responsavel, obFabricaConexao);
                break;

            default: 
            	
            	if (processoDt.isSigiloso()) {
            		// GERA PEND�NCIA PARA SERVENTIA_CARGO DO MAGISTRADO RESPONS�VEL DO PROCESSO
            		ProcessoResponsavelNe ProcessoResponsavelNe = new ProcessoResponsavelNe();
            		responsavel.setId_ServentiaCargo(ProcessoResponsavelNe.consultarMagistradoResponsavelProcesso(processoDt.getId(), usuarioCadastrador.getId_Serventia(),obFabricaConexao));// tratamento novo
            	} else {
            		// GERA PEND�NCIA PARA SERVENTIA DO USU�RIO
            		responsavel.setId_Serventia(processoDt.getId_Serventia());
            	}
                pendenciaDt.addResponsavel(responsavel);

                // Se � uma pend�ncia do tipo "Verificar Processo" com prazo, significa que � futura, tem que calcular DataLimite
                if (dados.getPrazo() != null && !dados.getPrazo().equals("") && Funcoes.StringToInt(dados.getPrazo()) > 0 
                		&& (Funcoes.StringToInt(dados.getCodPendenciaTipo()) == PendenciaTipoDt.VERIFICAR_PROCESSO
                				|| Funcoes.StringToInt(dados.getCodPendenciaTipo()) == PendenciaTipoDt.AGUARDANDO_DECURSO_PRAZO
                				|| Funcoes.StringToInt(dados.getCodPendenciaTipo()) == PendenciaTipoDt.AGUARDANDO_DECURSO_PRAZO_PRISAO_CIVIL
                				|| Funcoes.StringToInt(dados.getCodPendenciaTipo()) == PendenciaTipoDt.AGUARDANDO_PRAZO_DECADENCIAL
                				|| Funcoes.StringToInt(dados.getCodPendenciaTipo()) == PendenciaTipoDt.AGUARDANDO_CUMPRIMENTO_PENA)
                	) {
                    // Conta data limite da pend�ncia
                    Calendar calCalendario = Calendar.getInstance();
                    calCalendario.setTime(new Date());
                    calCalendario.add(Calendar.DATE, Funcoes.StringToInt(dados.getPrazo()));
                    pendenciaDt.setDataLimite(Funcoes.dateToStringSoData(calCalendario.getTime()));
                }
                this.gerarPendencia(pendenciaDt, arquivos, false, obFabricaConexao);
                break;
            }
        }        
    }

    /**
     * Gera pend�ncias para as Partes de um Processo. Internamente verifica se
     * trata de um recurso inominado, pois neste caso as pend�ncias devem ser
     * geradas para as partes do recurso (recorrentes e recorridas).
     * 
     * @param processoDt,
     *            processo para o qual ser�o geradas pend�ncias para suas partes
     * @param pendenciaDt,
     *            dt com dados da Pend�ncia que dever� ser gerada
     * @param processoParteTipo,
     *            tipo da parte que ser� vinculada a pend�ncia
     * @param pessoalAdvogados,
     *            define se � pessoal e para advogados ao mesmo tempo
     * @param pessoal,
     *            define se pend�ncia � somente pessoal independente se tiver
     *            advogados
     * @param intimacaoAudiencia,
     *            define se intima��o foi realizada em cart�rio/audi�ncia
     * @param arquivos,
     *            arquivos a serem vinculados
     * @param usuarioDt,
     *            usuario que realiza a operacao, neste momento adicionara a
     *            serventia como responsavel
     * @param responsavel,
     *            respons�vel pela pend�ncia, caso seja o MP como substituto processual
     * @param conexao,
     *            conex�o ativa
     * @author msapaula
     */
    public void gerarPendenciaPartesProcesso1N(ProcessoDt processoDt, PendenciaDt pendenciaDt, int processoParteTipo, boolean pessoalAdvogados, boolean pessoal, boolean intimacaoAudiencia, List arquivos, UsuarioDt usuarioDt, PendenciaResponsavelDt responsavel, FabricaConexao conexao) throws Exception {
        List listaPartesProcesso = null;

        // Quando for recurso inominado deve gerar Pend�ncias para as partes do
        // recurso (recorrentes)
        if (processoDt.getId_Recurso() != null && processoDt.getId_Recurso().length() > 0) {
            this.gerarPendenciaPartesRecurso1N(processoDt, pendenciaDt, processoParteTipo, pessoalAdvogados, pessoal, intimacaoAudiencia, arquivos, usuarioDt, responsavel, conexao);
        } else {
            // Nos casos em que � Processo de 1� e 2� grau deve gerar pend�ncias
            // para as partes do processo normalmente
            if (processoParteTipo == ProcessoParteTipoDt.POLO_ATIVO_CODIGO)
                listaPartesProcesso = processoDt.getListaPolosAtivos();
            else if (processoParteTipo == ProcessoParteTipoDt.POLO_PASSIVO_CODIGO) listaPartesProcesso = processoDt.getListaPolosPassivos();

            if (listaPartesProcesso != null) {
                for (int j = 0; j < listaPartesProcesso.size(); j++) {
                	String id_serventia = usuarioDt.getId_Serventia();
                	if (usuarioDt.isGabinete() || usuarioDt.isGabineteUPJ()) {
                		id_serventia = processoDt.getId_Serventia();
                	}
                		
                    // Gera pend�ncia para cada uma das partes do processo
                    ProcessoParteDt parteDt = (ProcessoParteDt) listaPartesProcesso.get(j);
                    if (parteDt.getId_Processo() != null && parteDt.getId_Processo().equalsIgnoreCase(processoDt.getId()))
                    	this.gerarPendenciaParteProcesso2N(pendenciaDt, parteDt, pessoalAdvogados, pessoal, intimacaoAudiencia, id_serventia, arquivos, usuarioDt, processoDt, responsavel, conexao);
                }
            }
        }
    }

    /**
     * Gera pend�ncias para as Partes de um Recurso, devendo consultar dados das
     * partes recorrentes e recorridas para que as pend�ncias sejam geradas
     * corretamente. Essa op��o ser� utilizada nos casos em que for recurso
     * inominado
     * 
     * @param processoDt,
     *            processo para o qual ser�o geradas pend�ncias para suas partes
     * @param pendenciaDt,
     *            dt com dados da Pend�ncia que dever� ser gerada
     * @param processoParteTipo,
     *            tipo da parte que ser� vinculada a pend�ncia
     * @param pessoalAdvogados,
     *            define se � pessoal e para advogados ao mesmo tempo
     * @param pessoal,
     *            define se pend�ncia � somente pessoal independente de ter
     *            advogados
     * @param intimacaoAudiencia,
     *            define se intima��o foi realizada em cart�rio/audi�ncia
     * @param arquivos,
     *            arquivos a serem vinculados
     * @param usuarioDt,
     *            usuario que realiza a operacao, neste momento adicionara a
     *            serventia como responsavel
     * @param conexao,
     *            conex�o ativa
     * @author msapaula
     */
    public void gerarPendenciaPartesRecurso1N(ProcessoDt processoDt, PendenciaDt pendenciaDt, int processoParteTipo, boolean pessoalAdvogados, boolean pessoal, boolean intimacaoAudiencia, List arquivos, UsuarioDt usuarioDt, PendenciaResponsavelDt responsavel, FabricaConexao conexao) throws Exception {
        List listaPartesRecurso = null;
        ProcessoParteDt parteDt = null;

        // Consulta dados completos para retornar tamb�m os dados das partes
        // recorrentes e recorridas
        if (processoDt.getRecursoDt() == null || processoDt.getRecursoDt().getListaRecorrentes() == null) processoDt.setRecursoDt(new RecursoNe().consultarIdCompleto(processoDt.getId_Recurso()));

        if (processoParteTipo == ProcessoParteTipoDt.POLO_ATIVO_CODIGO)
            listaPartesRecurso = processoDt.getRecursoDt().getListaRecorrentes();
        else if (processoParteTipo == ProcessoParteTipoDt.POLO_PASSIVO_CODIGO) listaPartesRecurso = processoDt.getRecursoDt().getListaRecorridos();

        if (listaPartesRecurso != null) {
            for (int j = 0; j < listaPartesRecurso.size(); j++) {
            	String id_serventia = usuarioDt.getId_Serventia();
            	if (usuarioDt.isGabinete() || usuarioDt.isGabineteUPJ()) 
            		id_serventia = processoDt.getId_Serventia();
            	
                // Gera pend�ncia para cada uma das partes do recurso
                RecursoParteDt recursoParteDt = (RecursoParteDt) listaPartesRecurso.get(j);
                parteDt = recursoParteDt.getProcessoParteDt();
                this.gerarPendenciaParteProcesso2N(pendenciaDt, parteDt, pessoalAdvogados, pessoal, intimacaoAudiencia, id_serventia, arquivos, usuarioDt, processoDt, responsavel, conexao);
            }
        }
    }

    /**
     * M�todo que gera pend�ncia para partes do processo. Se pessoalAdvogados �
     * true, significa que uma pend�ncia dever� ser gerada para os advogados da
     * parte e outra pend�ncia para a parte (se essa � cadastrada no sistema) ou
     * para a serventia do processo. Se pessoalAdvogados � false, verifica se
     * parte tem advogados pois esses ser�o respons�veis pela pend�ncia, caso
     * contr�rio, ser� a parte ou serventia os respons�veis.
     * 
     * @author msapaula
     * @author Ronneesley Moura Teles
     * @param pendenciaDt,
     *            dt com dados da Pend�ncia que dever� ser gerada
     * @param parteDt,
     *            dt com dados da parte vinculada a pend�ncia
     * @param pessoalAdvogados,
     *            determina se pend�ncia � pessoal e para advogados ao mesmo
     *            tempo
     * @param pessoal,
     *            determina se pend�ncia � somente pessoal independente de ter
     *            advogados
     * @param intimacaoAudiencia,
     *            define se intima��o foi realizada em cart�rio/audi�ncia
     * @param id_Serventia,
     *            serventia que ser� respons�vel pela pend�ncia, caso essa n�o
     *            seja on-line
     * @param arquivos,
     *            arquivos a serem vinculados
     * @param usuarioDt,
     *            usuario que realiza a operacao, neste momento adicionara a
     *            serventia como responsavel
     * @param processoDt
     * 			  processo vinculada � pend�ncia que ser� gerada
     * @param responsavel,
     *            respons�vel pela pend�ncia, caso seja o MP como substituto processual
     * @param obFabrica,
     *            fabrica de conexao
     */
    public void gerarPendenciaParteProcesso2N(PendenciaDt pendenciaDt, ProcessoParteDt parteDt, boolean pessoalAdvogados, boolean pessoal, boolean intimacaoAudiencia, String id_Serventia, List arquivos, UsuarioDt usuarioDt, ProcessoDt processoDt, PendenciaResponsavelDt responsavel, FabricaConexao obFabrica) throws Exception {
        //todos chegam aqui? Sim
    	
        PendenciaDt objPendencia = new PendenciaDt();
        objPendencia.copiar(pendenciaDt);

        objPendencia.setId_ProcessoParte(parteDt.getId());
        objPendencia.setNomeParte(parteDt.getNome());

        // Se pend�ncia � do tipo Cita��o ou Intima��o pode ser on-line para
        // advogados
        switch (Funcoes.StringToInt(objPendencia.getPendenciaTipoCodigo())) {
        case PendenciaTipoDt.INTIMACAO:
        case PendenciaTipoDt.CARTA_CITACAO:
            // Se parte possui advogados cadastrados marcados para receberem 
            // intima��o, esses ser�o respons�veis pela pend�ncia
            ProcessoParteAdvogadoNe advogadoNe = new ProcessoParteAdvogadoNe();
            List listaAdvogados = advogadoNe.consultarAdvogadosRecebemIntimacaoParte(parteDt.getId());

            boolean temAdvogados = false;
            if (listaAdvogados != null && listaAdvogados.size() > 0) 
            	temAdvogados = true;
            
            boolean utilizarDiarioEletronico = advogadoNe.isUtilizarDiarioEletronico(listaAdvogados, intimacaoAudiencia, Funcoes.StringToInt(objPendencia.getPendenciaTipoCodigo()));
            
            String id_serventia_Gabinete = null;
            if (processoDt != null && processoDt.isSigiloso() && usuarioDt != null && usuarioDt.isGabineteUPJ()) {
            	id_serventia_Gabinete = usuarioDt.getId_Serventia();
            }
            
            if ((!pessoal && temAdvogados && utilizarDiarioEletronico)) {
                this.criarIntimacaoCitacaoAdvogadosDiarioEletronico(objPendencia, id_Serventia, listaAdvogados, arquivos, id_serventia_Gabinete, obFabrica);
            
            } else if ((!pessoal && temAdvogados)) {
            	LogDt logDt = new LogDt();
                logDt.setId_Usuario(pendenciaDt.getId_UsuarioLog());
                logDt.setIpComputadorLog(pendenciaDt.getIpComputadorLog());
            	this.criarIntimacaoCitacaoAdvogados(objPendencia, id_Serventia, listaAdvogados, arquivos, intimacaoAudiencia, processoDt, responsavel, id_serventia_Gabinete, logDt, obFabrica);
            }

            // pendencia
            PendenciaDt pendenciaParteDt = new PendenciaDt();
            pendenciaParteDt.copiar(objPendencia);

            if (pessoal || pessoalAdvogados || !temAdvogados) {
            	pendenciaParteDt.setId_UsuarioFinalizador("null");
            	pendenciaParteDt.setDataFim("");
            	
             	if (pendenciaDt.isExpedicaoAutomatica()){
            		if(pendenciaParteDt.getId_ProcessoCustaTipo() == null || pendenciaParteDt.getId_ProcessoCustaTipo().equalsIgnoreCase("")) {
            			throw new MensagemException("N�o foi poss�vel identificar o tipo de custas do processo " + processoDt.getProcessoNumeroCompleto() + ". N�o sendo poss�vel o uso do e-Carta");
            		}
            		this.criarPendenciaCorreios(pendenciaParteDt, id_Serventia, arquivos, obFabrica);
            	} else {
            		pendenciaParteDt.setCodigoTemp("");
            		this.criarPendenciaParte(pendenciaParteDt, id_Serventia, arquivos, intimacaoAudiencia, processoDt, usuarioDt, obFabrica);
            	}
            }

            break;
        default:
        	if (pendenciaDt.isExpedicaoAutomatica() && (Funcoes.StringToInt(pendenciaDt.getPendenciaTipoCodigo()) == PendenciaTipoDt.INTIMACAO || Funcoes.StringToInt(pendenciaDt.getPendenciaTipoCodigo()) == PendenciaTipoDt.CARTA_CITACAO)) {
        		if(objPendencia.getId_ProcessoCustaTipo() == null || objPendencia.getId_ProcessoCustaTipo().equalsIgnoreCase("")) {
        			throw new MensagemException("N�o foi poss�vel identificar o tipo de custas do processo " + processoDt.getProcessoNumeroCompleto() + ". N�o sendo poss�vel o uso do e-Carta");
        		}
        		this.criarPendenciaCorreios(objPendencia, id_Serventia, arquivos, obFabrica);
        	} else {
        		this.criarPendenciaParte(objPendencia, id_Serventia, arquivos, intimacaoAudiencia, processoDt, usuarioDt, obFabrica);
        	}
        }
    }
    
    /**
     * Cria uma pendencia para uma parte que ser� enviada pelo correio, n�o gera movimenta��o no processo
     * 
     * @author lsbernardes
     * @param pendenciaDt,
     *            vo da pendencia
     * @param id_Serventia,
     *            id do cartorio que ira redigir caso a parte nao possua usuario
     * @param arquivos,
     *            lista de arquivos
     * @param obFabrica,
     *            fabrica para continuar uma transacao
     * @throws Exception
     */
	private void criarPendenciaCorreios(PendenciaDt pendenciaDt, String id_Serventia, List arquivosVincular, FabricaConexao obFabrica) throws Exception {
		PendenciaCorreiosDt pendenciaCorreiosDt = new PendenciaCorreiosDt();
		GuiaItemDisponivelNe guiaItemDisponivelNe = new GuiaItemDisponivelNe();
		PendenciaResponsavelDt responsavelDt = new PendenciaResponsavelDt();
		ObjectMapper mapper = new ObjectMapper();
		PendenciaCorreiosNe pendenciaCorreiosNe = new PendenciaCorreiosNe();
		responsavelDt.setId_Serventia(id_Serventia);
		pendenciaDt.addResponsavel(responsavelDt);
		pendenciaDt.setId_PendenciaStatus("null");
		
		this.gerarPendencia(pendenciaDt, arquivosVincular, false, obFabrica);
	
		if(Funcoes.StringToInt(pendenciaDt.getId_ProcessoCustaTipo()) == ProcessoDt.COM_CUSTAS) {
			if(pendenciaDt.getOrdemServico().equalsIgnoreCase("true")) {
				pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_ENVIO_CORREIOS));
			} else if (guiaItemDisponivelNe.vincularGuiaItemDepesaPostalPendencia(pendenciaDt.getId(), pendenciaDt.getId_Processo(), "1", obFabrica)) {
				pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_ENVIO_CORREIOS));
			} else {
				pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_PAGAMENTO_POSTAGEM));
			}
		} else {
			pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_ENVIO_CORREIOS));
		}

		alterarStatus(pendenciaDt, obFabrica);
		
		pendenciaCorreiosDt.setId_ProcessoCustaTipo(pendenciaDt.getId_ProcessoCustaTipo());
		pendenciaCorreiosDt.setId_Pend(pendenciaDt.getId());
		pendenciaCorreiosDt.setCodigoModelo(pendenciaDt.getCodModeloCorreio());
		pendenciaCorreiosDt.setMaoPropria(pendenciaDt.getMaoPropriaCorreio());
		pendenciaCorreiosDt.setOrdemServico(pendenciaDt.getOrdemServico());
		pendenciaCorreiosDt.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
		pendenciaCorreiosDt.setIpComputadorLog("Servidor");
		pendenciaCorreiosNe.salvar(pendenciaCorreiosDt, obFabrica);
		
		List listaPendencias = (List<CorreiosDt>) consultarPendenciasCorreiosCompleta(pendenciaDt.getId(), "", -1, "", false, obFabrica);
		if(listaPendencias.size()>0)
			pendenciaCorreiosDt.setMetaDados(mapper.writeValueAsString(listaPendencias.get(0)));
		pendenciaCorreiosNe.salvar(pendenciaCorreiosDt, obFabrica);
	}

    private void criarIntimacaoCitacaoAdvogados(PendenciaDt pendenciaDt, String idServentia, List listaAdvogados, List arquivos, boolean intimacaoAudiencia, ProcessoDt processoDt, PendenciaResponsavelDt responsavel, LogDt logDt, FabricaConexao fabrica) throws Exception {
    	this.criarIntimacaoCitacaoAdvogados(pendenciaDt, idServentia, listaAdvogados, arquivos, intimacaoAudiencia, processoDt, responsavel, null, logDt, fabrica);
    }
    
	/**
     * Criar intimacao ou citacao para os advogados
     * 
     * @author Ronneesley Moura Teles
     * @since 16/01/2009 11:59
     * @param pendenciaDt,
     *            vo de pendencia
     * @param idServentia,
     *            id da serventia
     * @param listaAdvogados,
     *            lista dos advogados
     * @param arquivos,
     *            arquivos a vincular
     * @param usuarioDt,
     *            usuario que realiza a operacao, neste momento adicionara a
     *            serventia como responsavel
     * @param intimacaoAudiencia,
     *            define se intima��o foi realizada em cart�rio/audi�ncia
     * @param processoDt
     * 			  processo vinculada � pend�ncia que ser� gerada
     * @param responsavel,
     *            respons�vel pela pend�ncia, caso seja o MP como substituto processual
     * @param fabrica,
     *            fabrica de conexao para continuar uma transacao
     * @throws Exception
     */
    private void criarIntimacaoCitacaoAdvogados(PendenciaDt pendenciaDt, String idServentia, List listaAdvogados, List arquivos, boolean intimacaoAudiencia, ProcessoDt processoDt, PendenciaResponsavelDt responsavel, String id_ServentiaGabinete, LogDt logDt, FabricaConexao fabrica) throws Exception {
        PrazoSuspensoNe prazoSuspensoNe = new PrazoSuspensoNe();
        ProcessoResponsavelNe responsavelNe = new ProcessoResponsavelNe();
        ServentiaDt serventiaDt = new ServentiaNe().consultarId(idServentia);
                
//        // Carrega o modelo de e-mail para notifica��o via email dos advogados.
//        ModeloNe modeloNe = new ModeloNe();
//        ModeloDt modeloEmailDt =modeloNe.consultarModeloCodigo( "1001" );
        // Nesse caso deve gerar Movimenta��o de Intima��o ou Cita��o Expedida
        // para Advogados da Parte
        if (!possuiMPSubstitutoProcessual(listaAdvogados)) {
        	MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
            movimentacaoNe.gerarMovimentacaoIntimacaoOuCitacaoAdvogado(pendenciaDt, intimacaoAudiencia, fabrica);	
        }        

        if (intimacaoAudiencia) {
            // Intima��o em Audi�ncia com prazo gera Intima��o Lida (pend�ncia fechada)
            if (Funcoes.StringToInt(pendenciaDt.getPrazo()) > 0) {
            	
            	if (this.isUtilizaPrazoCorrido(pendenciaDt.getId_Processo(), fabrica)) {
                	pendenciaDt.setDataLimite(prazoSuspensoNe.getProximaDataValidaPrazoCorrido(new Date(), Funcoes.StringToInt(pendenciaDt.getPrazo()), serventiaDt , fabrica) + " 00:00:00");
            	
            	} else if (this.isProcessoVaraPrecatoria(pendenciaDt.getId_Processo(), fabrica)) {
            		// Em caso de vara de precatoria deve-se testar o processo que originou a precatoria
            		if (processoDt == null) processoDt = new ProcessoNe().consultarId(pendenciaDt.getId_Processo(), fabrica);
                 	if (this.isUtilizaPrazoCorrido(processoDt.getId_ProcessoPrincipal(), fabrica)) {
                 		pendenciaDt.setDataLimite(prazoSuspensoNe.getProximaDataValidaPrazoCorrido(new Date(), Funcoes.StringToInt(pendenciaDt.getPrazo()), serventiaDt , fabrica) + " 00:00:00");
                 	} else {
                 		pendenciaDt.setDataLimite(prazoSuspensoNe.getProximaDataValidaNovoCPC(new Date(), Funcoes.StringToInt(pendenciaDt.getPrazo()), serventiaDt , fabrica) + " 00:00:00");
                 	}
                 
                } else {
                	pendenciaDt.setDataLimite(new PrazoSuspensoNe().getProximaDataValidaNovoCPC(new Date(), Funcoes.StringToInt(pendenciaDt.getPrazo()), serventiaDt , fabrica) + " 00:00:00");
                }
            	
            	 pendenciaDt.setDataVisto("");
            	 
            } /*else {
                // Quando for Intima��o em Audi�ncia sem prazo dever� somente
                // gerar movimenta��o "Intima��o Realizada em Audi�ncia"
                pendenciaDt = null;
                return;
            }*/
            
            pendenciaDt.setDataFim(Funcoes.DataHora(new Date()));
            pendenciaDt.setId_UsuarioFinalizador(pendenciaDt.getId_UsuarioCadastrador());
            pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_CUMPRIDA));
            pendenciaDt.setCodigoTemp(String.valueOf(PendenciaStatusDt.AGUARDANDO_PARECER_LEITURA_AUTOMATICA_CODIGO_TEMP));
            
        } else {
            // Nesse caso a pend�ncia ter� como data limite, a data de leitura
            // autom�tica
            pendenciaDt.setDataLimite(prazoSuspensoNe.getDataLeituraAutomatica(Funcoes.dateToStringSoData(new Date()), serventiaDt , fabrica));
            // Troca o status
            pendenciaDt.setId_PendenciaStatus("null");
            pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_VISTO));
        }

        List advogadosResponsaveis = new ArrayList();
        for (int j = 0; j < listaAdvogados.size(); j++) {
            ProcessoParteAdvogadoDt advogadoDt = (ProcessoParteAdvogadoDt) listaAdvogados.get(j);
            
            int grupoCodigo = Funcoes.StringToInt(advogadoDt.getGrupoCodigo());
            if (grupoCodigo == GrupoDt.MINISTERIO_PUBLICO) { //substituto processual
            	if (responsavel == null) responsavel = new PendenciaResponsavelDt();
            	
            	String serventiaSubTipoCodigo = String.valueOf(ServentiaSubtipoDt.MP_PRIMEIRO_GRAU);
            	
            	//Verifica se o processo possui um encaminhamento aberto
            	ProcessoEncaminhamentoNe procEncaminhamentoNe = new ProcessoEncaminhamentoNe();
            	String idProcEncaminhamento = procEncaminhamentoNe.consultarEncaminhamentoSemDevolucao(processoDt.getId(), fabrica);
            	
            	//Se n�o possui encaminhamento aberto segue a l�gica normal de testar o tipo da serventia
            	//usando o id do processoDt...
            	if(idProcEncaminhamento == null) {
	            	
            		if (processoDt.isTurmaRecursal()) {
	            		serventiaSubTipoCodigo = String.valueOf(ServentiaSubtipoDt.MP_TURMA_JULGADORA);
	            	} else if (processoDt.isSegundoGrau()) {
	            		serventiaSubTipoCodigo = String.valueOf(ServentiaSubtipoDt.MP_SEGUNDO_GRAU);
	            	}
            	
            	}
            	else {
            		//Caso contr�rio, se possuir um encaminhamento aberto, precisa testar o tipo da serventia
            		//que encaminhou o processo para o Cejusc, buscando o id desta serventia na tabela proc_encaminhamento.
            		ProcessoEncaminhamentoDt procEncaminhamentoDt = procEncaminhamentoNe.consultarId(idProcEncaminhamento);
            		ServentiaDt servOrigemEncaminhamentoDt = new ServentiaNe().consultarId(procEncaminhamentoDt.getId_ServOrigem(), fabrica);
            		
            		if (servOrigemEncaminhamentoDt.isTurma()) {
	            		serventiaSubTipoCodigo = String.valueOf(ServentiaSubtipoDt.MP_TURMA_JULGADORA);
	            	} else if (servOrigemEncaminhamentoDt.isSegundoGrau()) {
	            		serventiaSubTipoCodigo = String.valueOf(ServentiaSubtipoDt.MP_SEGUNDO_GRAU);
	            	}
            	}
            	
            	List listaPromotores = responsavelNe.consultarListaPromotoresProcesso(processoDt.getId(), null, serventiaSubTipoCodigo, CargoTipoDt.MINISTERIO_PUBLICO, true, fabrica);
            	if (listaPromotores == null || listaPromotores.size() == 0) {
            		// Se n�o tiver o MP como respons�vel no processo, salva o substituto processual como respons�vel...
            		ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
            		List serventiaCargos = serventiaCargoNe.consultarServentiasCargos(advogadoDt.getId_UsuarioServentiaAdvogado());
            		
            		String idServentiaCargoMP = "";
            		for (int k = 0; k < serventiaCargos.size(); k++) {
            			ServentiaCargoDt serventiaCargoDt = (ServentiaCargoDt) serventiaCargos.get(k);
                		if (serventiaCargoDt != null && 
                			serventiaCargoDt.getCargoTipoCodigo() != null && 
                			Funcoes.StringToInt(serventiaCargoDt.getCargoTipoCodigo()) == CargoTipoDt.MINISTERIO_PUBLICO) {
                			idServentiaCargoMP = serventiaCargoDt.getId();
                		}
                	}
            		
            		if (idServentiaCargoMP != null && idServentiaCargoMP.trim().length() > 0) {
            			responsavelNe.salvarProcessoResponsavel(processoDt.getId(), idServentiaCargoMP, true, CargoTipoDt.MINISTERIO_PUBLICO, logDt, fabrica);	
            		}  else {
            			throw new MensagemException("N�o foi localizado nenhum cargo do tipo minist�rio p�blico na promotoria do substituto processual.");
            		}
            	}
            	
            	this.gerarPendenciaIntimacaoMinisterioPublicoPrimeiroSegundoGrau(pendenciaDt, processoDt, responsavel, arquivos, idServentia, serventiaSubTipoCodigo, intimacaoAudiencia, id_ServentiaGabinete, logDt, fabrica);           	
            } else {
            	PendenciaResponsavelDt responsavelDt = new PendenciaResponsavelDt();
                responsavelDt.setId_UsuarioResponsavel(advogadoDt.getId_UsuarioServentiaAdvogado());
                advogadosResponsaveis.add(responsavelDt);
                
                //Verifica��o para intimar ou citar usu�rio master que n�o est� habilitado no processo ************************************************************
                List<PendenciaResponsavelDt> usuariosMaster = new ArrayList();
                usuariosMaster = possuiAdvogadoProcuradorDefensorMaster(advogadoDt.getId_UsuarioServentiaAdvogado(), advogadoDt.getId_Serventia(), fabrica);
                for (Iterator iterator = usuariosMaster.iterator(); iterator.hasNext();) {
                	PendenciaResponsavelDt pendenciaResponsavelDt = (PendenciaResponsavelDt) iterator.next();
                	advogadosResponsaveis.add(pendenciaResponsavelDt);
				}
                //**************************************************************************************************************************************************
            }
            //deixar comentado, funcionalidade em desenvolvimento
            //ProjudiWebsocketMap.getInstance().enviarMensagemUsuarioServentia(advogadoDt.getId_UsuarioServentiaAdvogado() , "teste de intima��o");
        }
        
        if (listaAdvogados.size() == 0 || (advogadosResponsaveis != null && advogadosResponsaveis.size() > 0)) {
        	// Adiciona a serventia como responsavel
            PendenciaResponsavelDt responsavelDt = new PendenciaResponsavelDt();
            
            if (id_ServentiaGabinete != null && id_ServentiaGabinete.length()>0){ // tratamento novo
            	responsavelDt.setId_Serventia(id_ServentiaGabinete);
            } else {
            	responsavelDt.setId_Serventia(idServentia);
            }
            
            advogadosResponsaveis.add(responsavelDt);

            // Se pessoalAdvogados ser� gerada uma pend�ncia separada para advogados
            pendenciaDt.setResponsaveis(advogadosResponsaveis);

            this.gerarPendencia(pendenciaDt, arquivos, false, fabrica);        

            // Envia e-mail para os advogados vinculados ao processo caso o envio de e-mail a partir do sistema projudi esteja habilitado.
            if( ProjudiPropriedades.getInstance().getEnvioDeEmailHabilitado() == true ){
            	String id_processo = pendenciaDt.getId_Processo();
            	for (int j = 0; j < listaAdvogados.size(); j++) {
            		String id_usuarioServentia = ( (ProcessoParteAdvogadoDt) listaAdvogados.get(j) ).getId_UsuarioServentiaAdvogado();
            		this.enviarAvisoDeIntimacaoCitacaoViaEmail(id_processo, id_usuarioServentia);
            	}
            }  
        }      
    }
    
    /**
     * Verifica se a serventia do usu�rio pode possuir um usu�rio master, caso positivo verifica se o advogado/procurador/defensor que ser� intimado ou citado � master, caso negativo tentar consultar
     * poss�veis usu�rios master na serventia do advogado/procurador/defensor que ser� intimado ou citado.
     * 
     * @author lsbernardes
     
     * @param id_Usu_Serv,
     *            id do usu�rio serventia
     * @param id_Serv,
     *            id da serventia
     * @param fabrica,
     *            fabrica de conexao para continuar uma transacao
     *            
     * @return List,
     *           lista contendo objetos do tipo PendenciaResponsavelDt para compor a lista de respons�veis pela intima��o ou cita��o
     *            
     * @throws Exception
     */
    private List<PendenciaResponsavelDt> possuiAdvogadoProcuradorDefensorMaster(String id_Usu_Serv, String id_Serv, FabricaConexao fabrica)  throws Exception {
    	List<PendenciaResponsavelDt> lista = new ArrayList();
    	ServentiaNe serventiaNe = new ServentiaNe();
    	UsuarioServentiaOabNe usuarioServentiaOabNe = new UsuarioServentiaOabNe();
    	
    	if(serventiaNe.isServentiaEscritorioJuridicoProcuradoriaDefensoria(id_Serv, fabrica) && !usuarioServentiaOabNe.isUsuarioServentiaOabMaster(id_Usu_Serv, fabrica)){
    		lista = usuarioServentiaOabNe.consultarUsuarioServentiaOabMaster(id_Serv, fabrica);
    	} 
    	
		return lista;
    }
    
    private boolean possuiMPSubstitutoProcessual(List listaAdvogados) {
    	for (int j = 0; j < listaAdvogados.size(); j++) {
            ProcessoParteAdvogadoDt advogadoDt = (ProcessoParteAdvogadoDt) listaAdvogados.get(j);
            
            int grupoCodigo = Funcoes.StringToInt(advogadoDt.getGrupoCodigo());
            if (grupoCodigo == GrupoDt.MINISTERIO_PUBLICO) return true; //substituto processual            	
        }
    	return false;
    }
    
    private void criarIntimacaoCitacaoAdvogadosDiarioEletronico(PendenciaDt pendenciaDt, String idServentia, List listaAdvogados, List arquivos, FabricaConexao fabrica) throws Exception {
    	this.criarIntimacaoCitacaoAdvogadosDiarioEletronico(pendenciaDt, idServentia, listaAdvogados, arquivos, null, fabrica);
    }
    
    /**
     * Criar intimacao ou citacao para os advogados publicando no Di�rio Eletronico
     * 
     * @param pendenciaDt,
     *            vo de pendencia
     * @param idServentia,
     *            id da serventia
     * @param listaAdvogados,
     *            lista dos advogados
     * @param arquivos,
     *            arquivos a vincular
     * @param usuarioDt,
     *            usuario que realiza a operacao, neste momento adicionara a
     *            serventia como responsavel
     * @param intimacaoAudiencia,
     *            define se intima��o foi realizada em cart�rio/audi�ncia
     * @param fabrica,
     *            fabrica de conexao para continuar uma transacao
     * @throws Exception
     */
    private void criarIntimacaoCitacaoAdvogadosDiarioEletronico(PendenciaDt pendenciaDt, String idServentia, List listaAdvogados, List arquivos, String id_ServentiaGabinete, FabricaConexao fabrica) throws Exception {
        PrazoSuspensoNe prazoSuspensoNe = new PrazoSuspensoNe();
        ProcessoNe processoNe = new ProcessoNe();

        ProcessoDt processoDt = processoNe.consultarId(pendenciaDt.getId_Processo());
        ServentiaDt serventiaDt = new ServentiaNe().consultarId(idServentia);
        
        //CONSULTA NECESSARIA POIS PARA COME�AR A CONTAR O PRAZO NO D.E. DEVE SEMPRE CONSIDERAR A COMARCA E A CIDADE DE GOI�NIA
        ComarcaDt comarcaDt = new ComarcaNe().consultarComarcaCodigo(String.valueOf(ComarcaDt.GOIANIA)); 
        CidadeDt cidadeDt = new CidadeNe().consultarCidadeCodigo(String.valueOf(CidadeDt.CODIGO_CIDADE_GOIANIA));
        
        ServentiaDt serventiaDiarioDt = new ServentiaDt();
        		
        serventiaDiarioDt.setId_Cidade(cidadeDt.getId());
        serventiaDiarioDt.setId_Comarca(comarcaDt.getId());
        
        //para verificar se deve usar ou n�o a nova contagem de prazo do novo cpc
        if (processoDt.isCivel()){
        	serventiaDiarioDt.setServentiaSubtipoCodigo(String.valueOf(ServentiaSubtipoDt.VARAS_CIVEL));
        } else {
        	serventiaDiarioDt.setServentiaSubtipoCodigo(String.valueOf(ServentiaSubtipoDt.VARA_CRIMINAL));
        }
        
        String dataPublicacao = prazoSuspensoNe.getProximaDataValidaNovoCPC(Funcoes.dateToStringSoData(new Date()), Configuracao.INICIO_CONTAGEM_PRAZO_DIARIO_ELETRONICO, serventiaDiarioDt, fabrica);
        
        LogDt logDt = new LogDt(pendenciaDt.getId_UsuarioLog(), pendenciaDt.getIpComputadorLog());
        
        //complemento para informar que a mesma foi gerada para o DE
        String stComplemento = "A ser publicada no Di�rio Eletr�nico nos pr�ximos 2 (dois) dias �teis - "+ pendenciaDt.getNomeParte() + " (Referente � Mov. " + pendenciaDt.getMovimentacao() + ")";
      
        MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
        movimentacaoNe.gerarMovimentacaoIntimacaoCitacaoEfetivadaDiarioEletronico(pendenciaDt, stComplemento, logDt, fabrica);
        
        // Nesse caso a pend�ncia ter� como data limite, a data de leitura autom�tica
    	if ( Funcoes.StringToInt(pendenciaDt.getPrazo()) > 0) {
    		
            if (this.isUtilizaPrazoCorrido(pendenciaDt.getId_Processo(), fabrica)) {
            	pendenciaDt.setDataLimite(prazoSuspensoNe.getProximaDataValidaPrazoCorrido(dataPublicacao, Funcoes.StringToInt(pendenciaDt.getPrazo()), serventiaDt , fabrica) + " 00:00:00");
            
            } else if (this.isProcessoVaraPrecatoria(pendenciaDt.getId_Processo(), fabrica)) {
            	// Em caso de vara de precatoria deve-se testar o processo que originou a precatoria
          	  	if (this.isUtilizaPrazoCorrido(processoDt.getId_ProcessoPrincipal(), fabrica))
          	  		pendenciaDt.setDataLimite(prazoSuspensoNe.getProximaDataValidaPrazoCorrido(dataPublicacao, Funcoes.StringToInt(pendenciaDt.getPrazo()), serventiaDt , fabrica) + " 00:00:00");
          	  	else
          	  		pendenciaDt.setDataLimite(prazoSuspensoNe.getProximaDataValidaNovoCPC(dataPublicacao, Funcoes.StringToInt(pendenciaDt.getPrazo()), serventiaDt , fabrica) + " 00:00:00");
          
            } else
            	pendenciaDt.setDataLimite(prazoSuspensoNe.getProximaDataValidaNovoCPC(dataPublicacao, Funcoes.StringToInt(pendenciaDt.getPrazo()), serventiaDt , fabrica) + " 00:00:00");

            // Limpa dados do objeto
            pendenciaDt.setDataVisto("");
        } else {
            // Se n�o possuir prazo limpa data limite
            pendenciaDt.setDataLimite("");
        }

        List advogadosResponsaveis = new ArrayList();
        for (int j = 0; j < listaAdvogados.size(); j++) {
            ProcessoParteAdvogadoDt advogadoDt = (ProcessoParteAdvogadoDt) listaAdvogados.get(j);
            PendenciaResponsavelDt responsavelDt = new PendenciaResponsavelDt();
            responsavelDt.setId_UsuarioResponsavel(advogadoDt.getId_UsuarioServentiaAdvogado());
            advogadosResponsaveis.add(responsavelDt);
        }

        // Adiciona a serventia como responsavel tratamento novo talvez....
        PendenciaResponsavelDt responsavelDt = new PendenciaResponsavelDt();
        if (id_ServentiaGabinete != null && id_ServentiaGabinete.length()>0){
        	responsavelDt.setId_Serventia(id_ServentiaGabinete);
        } else {
        	responsavelDt.setId_Serventia(idServentia);
        }
        advogadosResponsaveis.add(responsavelDt);

        // Se pessoalAdvogados ser� gerada uma pend�ncia separada para advogados
        pendenciaDt.setResponsaveis(advogadosResponsaveis);

        //pendencia do diario eletronico continuara na tela 
        pendenciaDt.setCodigoTemp(String.valueOf(PendenciaStatusDt.AGUARDANDO_PARECER_DIARIO_ELETRONICO_CODIGO_TEMP));
        // Troca o status
        pendenciaDt.setId_PendenciaStatus("null");
        // Fecha a pendencia
        pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_CUMPRIDA));
        pendenciaDt.setId_UsuarioFinalizador(UsuarioDt.SistemaProjudi);
        pendenciaDt.setDataFim(dataPublicacao+" 00:00:00");
        
        this.gerarPendencia(pendenciaDt, arquivos, false, fabrica);
        

        // Envia e-mail para os advogados vinculados ao processo caso o envio de e-mail a partir do sistema projudi esteja habilitado.
        if( ProjudiPropriedades.getInstance().getEnvioDeEmailHabilitado() == true ){
        	String id_processo = pendenciaDt.getId_Processo();
        	for (int j = 0; j < listaAdvogados.size(); j++) {
        		String id_usuarioServentia = ( (ProcessoParteAdvogadoDt) listaAdvogados.get(j) ).getId_UsuarioServentiaAdvogado();
        		this.enviarAvisoDeIntimacaoCitacaoViaEmail(id_processo, id_usuarioServentia);
        	}
        }

        
    }

    /**
     * Envia um e-mail avisando os advogados do processo sobre as Intima��es e Cita��es. Faz a checagem da propriedade
     * "envioDeEmailHabilitado" da tabela PROPRIEDADE somomente ao envia o e-mail. Ou seja, vai executar todo o c�digo e
     * consultas ao banco de dados auxiliares e s� depois checa se tem permiss�o para enviar o e-mail. Por este motivo, � 
     * interessante antes de executar este m�todo checar se o envio de e-mail est� habilitado.
     * @param id_processo Id do Processo
     * @param id_usuarioServentia Id do Usu�rioServentia do Advogado destinat�rio.
     * @throws Exception
     * @author vfosantos
     */
    private void enviarAvisoDeIntimacaoCitacaoViaEmail( String id_processo, String id_usuarioServentia ) throws Exception{
        // Carrega o modelo pelo o c�digo do modelo( C�digo do modelo de aviso de Intima��o/Cita��o == 1001 ).
    	ModeloDt modeloEmailDt = new ModeloNe().consultarModeloCodigo( "1001" );
    	// Carrega o n�mero do processo.
        String processoNumero = new ProcessoNe().getNumeroDoProcesso( id_processo );
        
        // Carrega os dados do processo.
        UsuarioDt usuarioAdvogado = new UsuarioNe().consultarUsuarioServentiaIdParaEnvioDeEmail( id_usuarioServentia );
        
        // Verifica se existe o modelo do e-mail, se este possui conte�do para enviar para enviar e se os dados do usu�rio est�o completos.
        if( modeloEmailDt != null
        		&& usuarioAdvogado != null
        		&& modeloEmailDt.getTexto() != null 
        		&& usuarioAdvogado.getEMail() != null
        		&& usuarioAdvogado.getNome() != null 
        		&& modeloEmailDt.getTexto().length() > 0
        		&& usuarioAdvogado.getEMail().length() > 0
        		&& usuarioAdvogado.getNome().length() > 0 ){

        	/*Substituis as maraca��es no modelo do e-email.
        	 *	[NOME_ADV] - Nome do Advogado.
        	 *	[NUMERO_PROCESSO] - N�mero do processo.
        	 *	[DATAHORA] - Data e hora de envio do e-mail.
        	 */
        	String textoEmail = modeloEmailDt.getTexto();
        	textoEmail = textoEmail.replace( "[NOME_ADV]", usuarioAdvogado.getNome() );
        	textoEmail = textoEmail.replace( "[NUMERO_PROCESSO]", processoNumero );
        	textoEmail = textoEmail.replace( "[DATAHORA]", Funcoes.DataHora( new Date() ) );
    		
    		new GerenciadorEmail(usuarioAdvogado.getNome() , usuarioAdvogado.getEMail(), "Informa��o de intima��o/cita��o", textoEmail, GerenciadorEmail.ENVIAR_EMAIL_HTML_ADM).start();
    		
    			}
    		}
    
    
    /**
     * Cria uma pendencia para uma parte
     * 
     * @author Ronneesley Moura Teles
     * @since 16/01/2009 11:43
     * @param pendenciaDt,
     *            vo da pendencia
     * @param idServentia,
     *            id do cartorio que ira redigir caso a parte nao possua usuario
     * @param arquivos,
     *            lista de arquivos
     * @param usuarioDt,
     *            usuario que realiza a operacao, neste momento adicionara a
     *            serventia como responsavel caso seja usuario online
     * @param fabrica,
     *            fabrica para continuar uma transacao
     * @throws Exception
     */
    private void criarPendenciaParte(PendenciaDt pendenciaDt, String idServentia, List arquivos, boolean intimacaoAudiencia, ProcessoDt processoDt, UsuarioDt usuarioDt, FabricaConexao fabrica) throws Exception {
        ServentiaDt serventiaDt = new ServentiaNe().consultarId(idServentia);

        // Se parte � cadastrada, ser� respons�vel pela pend�ncia, caso
        // contr�rio o cart�rio ser� o respons�vel
        String id_UsuarioParte = this.consultarParteOnline(pendenciaDt);

        if (id_UsuarioParte != null)
            this.gerarIntimacaoOuCitacaoParte(pendenciaDt, id_UsuarioParte, arquivos, serventiaDt, intimacaoAudiencia, fabrica);
        else {

            if (intimacaoAudiencia) {
                new MovimentacaoNe().gerarMovimentacaoIntimacaoAudienciaProcessoParte(pendenciaDt, intimacaoAudiencia, fabrica);
                // Intima��o em Audi�ncia com prazo gera Intima��o Lida
                // (pend�ncia fechada)
                if ( Funcoes.StringToInt(pendenciaDt.getPrazo()) > 0) {
                	
                	if (this.isUtilizaPrazoCorrido(pendenciaDt.getId_Processo(), fabrica)) {
                    	pendenciaDt.setDataLimite(new PrazoSuspensoNe().getProximaDataValidaPrazoCorrido(new Date(), Funcoes.StringToInt(pendenciaDt.getPrazo()), serventiaDt , fabrica) + " 00:00:00");
                	
                	} else if (this.isProcessoVaraPrecatoria(pendenciaDt.getId_Processo(), fabrica)) {
                		// Em caso de vara de precatoria deve-se testar o processo que originou a precatoria
                		ProcessoDt processo = new ProcessoNe().consultarId(pendenciaDt.getId_Processo(), fabrica);
                   	  	if (this.isUtilizaPrazoCorrido(processo.getId_ProcessoPrincipal(), fabrica))
                   	  		pendenciaDt.setDataLimite(new PrazoSuspensoNe().getProximaDataValidaPrazoCorrido(new Date(), Funcoes.StringToInt(pendenciaDt.getPrazo()), serventiaDt , fabrica) + " 00:00:00");
                   	  	else
                   	  		pendenciaDt.setDataLimite(new PrazoSuspensoNe().getProximaDataValidaNovoCPC(new Date(), Funcoes.StringToInt(pendenciaDt.getPrazo()), serventiaDt , fabrica) + " 00:00:00");
                   
                  } else
                    	pendenciaDt.setDataLimite(new PrazoSuspensoNe().getProximaDataValidaNovoCPC(new Date(), Funcoes.StringToInt(pendenciaDt.getPrazo()), serventiaDt , fabrica) + " 00:00:00");
                   
                	pendenciaDt.setDataFim(Funcoes.DataHora(new Date()));
                    pendenciaDt.setId_UsuarioFinalizador(pendenciaDt.getId_UsuarioCadastrador());
                    pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_CUMPRIDA));
                    pendenciaDt.setDataVisto("");
                } else {
                    // Quando for Intima��o em Audi�ncia sem prazo dever� somente gerar movimenta��o "Intima��o Realizada em Audi�ncia"
                    pendenciaDt = null;
                    return;
                }
            } else {
                // Troca o status
                pendenciaDt.setId_PendenciaStatus("null");
                pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_EM_ANDAMENTO));
            }

            PendenciaResponsavelDt responsavel = new PendenciaResponsavelDt(); // tratamento novo
            if (processoDt != null && processoDt.isSigiloso() && usuarioDt != null) {
        		// GERA PEND�NCIA PARA SERVENTIA_CARGO DO MAGISTRADO RESPONS�VEL DO PROCESSO
        		ProcessoResponsavelNe ProcessoResponsavelNe = new ProcessoResponsavelNe();
        		responsavel.setId_ServentiaCargo(ProcessoResponsavelNe.consultarMagistradoResponsavelProcesso(processoDt.getId(), usuarioDt.getId_Serventia(), fabrica));
        	} else if (processoDt != null) {
        		// GERA PEND�NCIA PARA SERVENTIA DO USU�RIO
        		responsavel.setId_Serventia(processoDt.getId_Serventia());
        	} else {
        		responsavel.setId_Serventia(idServentia);
        	}

            pendenciaDt.addResponsavel(responsavel);

            this.gerarPendencia(pendenciaDt, arquivos, false, fabrica);
        }

    }

    /**
     * Gera uma intimacao ou citacao para a parte
     * 
     * @author Ronneesley Moura Teles
     * @since 27/01/2009 14:22
     * @param pendenciaDt,
     *            os dados da pendencia a ser gerada
     * @param idUsuarioParte,
     *            id do usuario da parte
     * @param arquivos,
     *            lista de arquivos a serem vinculados na pendencia
     * @param id_Serventia,
     *            neste momento adicionara a serventia como responsavel
     * @param fabrica,
     *            fabrica de conexao para continuar uma transacao
     * @throws Exception
     */
    private void gerarIntimacaoOuCitacaoParte(PendenciaDt pendenciaDt, String idUsuarioParte, List arquivos, ServentiaDt serventiaDt, boolean intimacaoAudiencia, FabricaConexao fabrica) throws Exception {
        PrazoSuspensoNe prazoSuspensoNe = new PrazoSuspensoNe();

        // Nesse caso deve gerar Movimenta��o de Intima��o ou Cita��o Expedida
        // para Parte
        MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
        movimentacaoNe.gerarMovimentacaoIntimacaoOuCitacaoParte(pendenciaDt, intimacaoAudiencia, fabrica);

        if (intimacaoAudiencia) {
            // Intima��o em Audi�ncia com prazo gera Intima��o Lida (pend�ncia
            // fechada)
            if ( Funcoes.StringToInt(pendenciaDt.getPrazo()) > 0) {
            	
            	if (this.isUtilizaPrazoCorrido(pendenciaDt.getId_Processo(), fabrica)) {
                	pendenciaDt.setDataLimite(prazoSuspensoNe.getProximaDataValidaPrazoCorrido(new Date(), Funcoes.StringToInt(pendenciaDt.getPrazo()), serventiaDt , fabrica) + " 00:00:00");
            	
            	} else if (this.isProcessoVaraPrecatoria(pendenciaDt.getId_Processo(), fabrica)) {
            		// Em caso de vara de precatoria deve-se testar o processo que originou a precatoria
            		ProcessoDt processoDt = new ProcessoNe().consultarId(pendenciaDt.getId_Processo(), fabrica);
                 	if (this.isUtilizaPrazoCorrido(processoDt.getId_ProcessoPrincipal(), fabrica))
                 		pendenciaDt.setDataLimite(new PrazoSuspensoNe().getProximaDataValidaPrazoCorrido(new Date(), Funcoes.StringToInt(pendenciaDt.getPrazo()), serventiaDt , fabrica) + " 00:00:00");
                 	else
                 		pendenciaDt.setDataLimite(new PrazoSuspensoNe().getProximaDataValidaNovoCPC(new Date(), Funcoes.StringToInt(pendenciaDt.getPrazo()), serventiaDt , fabrica) + " 00:00:00");
                 
                } else
                	pendenciaDt.setDataLimite(new PrazoSuspensoNe().getProximaDataValidaNovoCPC(new Date(), Funcoes.StringToInt(pendenciaDt.getPrazo()), serventiaDt , fabrica) + " 00:00:00");
                
            	pendenciaDt.setDataFim(Funcoes.DataHora(new Date()));
                pendenciaDt.setId_UsuarioFinalizador(pendenciaDt.getId_UsuarioCadastrador());
                pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_CUMPRIDA));
                pendenciaDt.setDataVisto("");
            } else {
                // Quando for Intima��o em Audi�ncia sem prazo dever� somente
                // gerar movimenta��o "Intima��o Realizada em Audi�ncia"
                pendenciaDt = null;
                return;
            }
        } else {
            // Nesse caso a pend�ncia ter� como data limite, a data de leitura
            // autom�tica
            pendenciaDt.setDataLimite(prazoSuspensoNe.getDataLeituraAutomatica(Funcoes.dateToStringSoData(new Date()), serventiaDt , fabrica));
            // Troca o status
            pendenciaDt.setId_PendenciaStatus("null");
            pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_VISTO));
        }

        if (pendenciaDt.getResponsaveis() != null) pendenciaDt.getResponsaveis().clear();

        PendenciaResponsavelDt responsavelDt = new PendenciaResponsavelDt();
        responsavelDt.setId_UsuarioResponsavel(idUsuarioParte);
        pendenciaDt.addResponsavel(responsavelDt);

        PendenciaResponsavelDt responsavelServentiaDt = new PendenciaResponsavelDt();
        responsavelServentiaDt.setId_Serventia(serventiaDt.getId());
        pendenciaDt.addResponsavel(responsavelServentiaDt);

        this.gerarPendencia(pendenciaDt, arquivos, false, fabrica);

    }

    /**
     * Consulta o id da parte caso ela seja online
     * 
     * @author Ronneesley Moura Teles
     * @since 26/01/2009 16:51
     * @param pendenciaDt
     *            vo de pendencia
     * @return id da parte ou null caso nao seja cadastrada
     * @throws Exception
     */
    private String consultarParteOnline(PendenciaDt pendenciaDt) throws Exception {
        ProcessoParteNe processoParteNe = new ProcessoParteNe();

        // Se parte � cadastrada, ser� respons�vel pela pend�ncia, caso
        // contr�rio o cart�rio ser� o respons�vel
        String id_UsuarioParte = processoParteNe.consultarUsuarioServentiaParte(pendenciaDt.getId_ProcessoParte());

        return id_UsuarioParte;
    }

    /**
     * Consulta os arquivos de uma pendencia
     * 
     * @author Ronneesley Moura Teles
     * @since 16/12/2008 09:51
     * @param pendenciaDt
     *            pojo da pendencia
     * @param conexao
     *            conexao para ser iniciada ou null para iniciar uma nova
     * @return Lista de arquivos de pendencia
     * @throws Exception
     */
    public List consultarArquivos(PendenciaDt pendenciaDt, FabricaConexao conexao) throws Exception {
        PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
        return pendenciaArquivoNe.consultarArquivosPendencia(pendenciaDt, null, true, false, conexao);
    }

    /**
     * Consulta Arquivo por id
     * 
     * @author Leandro Bernardes
     * @since 14/05/2009
     * @param idArquivo
     *            id do Aquivo
     * @return AquivoDT
     * @throws Exception
     */
    public ArquivoDt consultarArquivoPendenciaId(String idArquivo) throws Exception {
        ArquivoNe arquivoNe = new ArquivoNe();
        ArquivoDt arquivoDt = arquivoNe.consultarId(idArquivo);
        arquivoNe = null;
        return arquivoDt;
    }

    /**
     * Consulta os arquivos de resposta de uma pendencia
     * 
     * @author Ronneesley Moura Teles
     * @since 15/01/2009 10:49
     * @param pendenciaDt
     *            pojo da pendencia
     * @param conexao
     *            conexao para ser iniciada ou null para iniciar uma nova
     * @return Lista de arquivos de pendencia
     * @throws Exception
     */
    public List consultarArquivosResposta(PendenciaDt pendenciaDt, UsuarioNe usuarioNe) throws Exception {
        PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
        return pendenciaArquivoNe.consultarPendenciaRespostaComHash(pendenciaDt, true, usuarioNe, null);
    }
    
    /**
     * Consulta os arquivos de resposta de uma pendencia
     * 
     * @author Jesus Rodrigo
     * @since 26/08/2014
     * @param pendenciaDt  pojo da pendencia
     * @param conexao conexao para ser iniciada ou null para iniciar uma nova
     * @return Lista de arquivos de pendencia
     * @throws Exception
     */
    public List consultarArquivosRespostaFinalizado(PendenciaDt pendenciaDt, UsuarioNe usuarioNe) throws Exception {
        PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
        return pendenciaArquivoNe.consultarPendenciaFinalizadaRespostaComHash(pendenciaDt, true, usuarioNe, null);
    }

//    /**
//     * Consulta os arquivos de resposta de uma pendencia
//     * 
//     * @author Ronneesley Moura Teles
//     * @since 15/01/2009 10:56
//     * @param PendenciaDt
//     *            pendenciaDt pojo da pendencia
//     * @return Lista de arquivos de pendencia
//     * @throws Exception
//     */
//    public List consultarArquivosResposta(PendenciaDt pendenciaDt, UsuarioNe usuarioNe) throws Exception {
//        return this.consultarArquivosResposta(pendenciaDt, usuarioNe, null);
//    }

    /**
     * Consultar arquivos de uma determinada pendencia com o hash do arquivo
     * 
     * @author Ronneesley Moura Teles
     * @since 19/11/2008 11:09
     * @param PendenciaDt
     *            pendenciaDt, pojo da pendencia
     * @param UsuarioNe
     *            usuarioSessao, usuario da sessao
     * @throws Exception
     */
    public List consultarArquivosPendenciaComHash(PendenciaDt pendenciaDt, UsuarioNe usuarioNe) throws Exception {
        PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
        return pendenciaArquivoNe.consultarArquivosPendenciaComHash(pendenciaDt, true, false, usuarioNe);
    }
    
    /**
     * Consultar arquivos de uma determinada pendencia com o hash do arquivo
     * 
     * @param PendenciaDt
     *            pendenciaDt, pojo da pendencia
     * @param UsuarioNe
     *            usuarioSessao, usuario da sessao
     * @throws Exception
     */
    public List consultarArquivosPendenciaFinalizadaComHash(PendenciaDt pendenciaDt, UsuarioNe usuarioNe) throws Exception {
        PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
        return pendenciaArquivoNe.consultarArquivosPendenciaFinalizadaComHash(pendenciaDt, true, false, usuarioNe);
    }

    /**
     * Consultar arquivos assinados de uma determinada pendencia com o hash do
     * arquivo
     * 
     * @author Ronneesley Moura Teles
     * @since 21/01/2009 16:38
     * @param pendenciaDt
     *            pojo da pendencia
     * @param usuarioSessao
     *            usuario da sessao
     * @throws Exception
     */
    public List consultarArquivosAssinadosComHash(PendenciaDt pendenciaDt, UsuarioNe usuarioNe) throws Exception {
        PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
        
        //verifica se a pendencia j� foi movida para as tabelas de pendencias finalizadas
        if (pendenciaDt.getDataFim() != null && pendenciaDt.getDataFim().length()>0 
        		&& pendenciaDt.getDataVisto() != null && pendenciaDt.getDataVisto().length()>0)
        	return pendenciaArquivoNe.consultarArquivosPendenciaFinalizadaComHash(pendenciaDt, true, true, usuarioNe);
        else
        	return pendenciaArquivoNe.consultarArquivosPendenciaComHash(pendenciaDt, true, true, usuarioNe);
    }

    /**
     * Consultar arquivos assinados de uma determinada pendencia com o hash do
     * arquivo
     * 
     * @param idPendencia
     *            id da pendencia
     * @param usuarioSessao
     *            usuario da sessao
     * @throws Exception
     */
    public List consultarArquivosAssinadosComHash(String idPendencia, UsuarioNe usuarioNe) throws Exception {
        PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
        PendenciaDt pendenciaDt = new PendenciaDt();
        pendenciaDt.setId(idPendencia);
        return pendenciaArquivoNe.consultarArquivosPendenciaComHash(pendenciaDt, true, true, usuarioNe);
    }

    /**
     * Inseri uma copia da pendencia (Alterado por Leandro Beranrdes 18/06/2009)
     * O pai da pendencia, sera a pendencia passada como parametr O status e
     * outros dados serao copiados A data de fim sera zerada
     * 
     * @author Ronneesley Moura Teles
     * @since 01/10/2008 10:25
     * @param PendenciaDt
     *            pendenciaDt, pojo da pendencia [Pendencia pai]
     * @param FabricaConexao
     *            fabrica, fabrica de conexao
     * @throws Exception
     */
    public PendenciaDt inserirFilhaExpedir(PendenciaDt pendenciaDt, FabricaConexao fabrica) throws Exception {
        PendenciaDt novaPendenciaDt = null;
        FabricaConexao obFabricaConexao = null;        
        try {
            PendenciaPs pendenciaPs = null;

            if (fabrica == null) {
                obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
                pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
                //pendenciaPs.setConexao(obFabricaConexao);
            } else {
            	pendenciaPs = new PendenciaPs(fabrica.getConexao());                
            }

            // Copia os dados da pendencia original
            if (this.pendenciaTipoExpedir(pendenciaDt)) {
                PendenciaStatusNe pendenciaStatusNe = new PendenciaStatusNe();
                PendenciaStatusDt pendenciaStatusDt = pendenciaStatusNe.consultarPendenciaStatusCodigo(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO);
                novaPendenciaDt = pendenciaDt.criarPendenciaFilhaExpedida();
                novaPendenciaDt.setId_PendenciaStatus(pendenciaStatusDt.getId());
            } else
                novaPendenciaDt = pendenciaDt.criarFilha();

            // Inseri a nova pendencia
            pendenciaPs.inserir(novaPendenciaDt);
        } finally {
            if (fabrica == null) obFabricaConexao.fecharConexao();
        }

        return novaPendenciaDt;
    }

    /**
     * Inseri uma copia da pendencia O pai da pendencia, sera a pendencia
     * passada como parametro O status e outros dados serao copiados A data de
     * fim sera zerada
     * 
     * @author Ronneesley Moura Teles
     * @since 01/10/2008 10:25
     * @param PendenciaDt
     *            pendenciaDt, pojo da pendencia [Pendencia pai]
     * @param FabricaConexao
     *            fabrica, fabrica de conexao
     * @throws Exception
     */
    public PendenciaDt inserirFilhaEncaminhar(PendenciaDt pendenciaDt, FabricaConexao fabrica) throws Exception {
        PendenciaDt novaPendenciaDt = null;
        FabricaConexao obFabricaConexao = null;
        try {
            if (fabrica == null) {
                obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
               
            } else {
            	obFabricaConexao = fabrica;
            }
            
            PendenciaPs pendenciaPs  = new  PendenciaPs(obFabricaConexao.getConexao());

            // Copia os dados da pendencia original
            novaPendenciaDt = pendenciaDt.criarFilha();

            // Inseri a nova pendencia
            pendenciaPs.inserir(novaPendenciaDt);
        } finally {
            if (fabrica == null) obFabricaConexao.fecharConexao();
        }

        return novaPendenciaDt;
    }

    /**
     * Encaminhar uma pendencia para outro destino O ato de encaminhar uma
     * pendencia, realiza as seguintes acoes: 1. Fecha a pendencia que esta
     * sendo encaminhada 2. Cria uma nova pendencia a parir dos dados da
     * pendencia original 2.1. Todos os dados da nova pendencia serao iguais,
     * exceto 2.1.1. Data de Inicio, que possuira o valor do dia do
     * encaminhamento 2.1.2. Data de Fim, que devera estar aberta, ou seja, null
     * 2.1.3. Id da pendencia pai (Id_PendenciaPai), possuira o valor do id da
     * pendencia original
     * 
     * @author Ronneesley Moura Teles
     * @since 26/09/2008 16:11
     * @param PendenciaDt
     *            pendenciaDt, pojo de pendencia
     * @param ServentiaDt
     *            serventiaDt, pojo de serventia
     * @param ServentiaCargoDt
     *            serventiaCargoDt, pojo de serventia cargo
     * @param UsuarioNe
     *            usuarioNe, pojo do usuario para dados de fechamento e log
     * @return PendenciaDt, Retorna a nova pendencia encaminhada
     * @throws Exception
     */
    public PendenciaDt encaminhar(PendenciaDt pendenciaDt, ServentiaDt serventiaDt, ServentiaCargoDt serventiaCargoDt, UsuarioNe usuarioNe) throws Exception {
        PendenciaDt novaPendenciaDt = null;
        FabricaConexao obFabricaConexao = null;        
        try {
            // Verifica se e uma serventia externa
            ServentiaNe serventiaNe = new ServentiaNe();
            ServentiaDt serventiaUsuarioDt = serventiaNe.consultarId(usuarioNe.getUsuarioDt().getId_Serventia());
            ServentiaTipoNe serventiaTipoNe = new ServentiaTipoNe();
            ServentiaTipoDt serventiaTipoDt = serventiaTipoNe.consultarId(serventiaUsuarioDt.getId_ServentiaTipo());
            
            if (pendenciaDt.getPendenciaStatusCodigo().equals(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_RETORNO))){
            	throw new MensagemException("N�o � poss�vel encaminhar pend�ncias com status Aguardando Retorno.");
            }

            if (serventiaTipoDt.getExterna() != null && serventiaTipoDt.getExterna().equals("true")) {
                if (serventiaCargoDt.getId() == null || serventiaCargoDt.getId().equals("")) {
                    throw new MensagemException("Serventias externas podem encaminhar somente para a mesma serventia, preencha o cargo.");
                }

                ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
                serventiaCargoDt = serventiaCargoNe.consultarId(serventiaCargoDt.getId());

                if (!serventiaCargoDt.getId_Serventia().equals(serventiaUsuarioDt.getId())) {
                    throw new MensagemException("Serventias externas podem encaminhar somente para a mesma serventia.");
                }
            }

            // Abre uma conexao
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);

            // Inicia uma transacao
            obFabricaConexao.iniciarTransacao();

            LogDt obLogDt = new LogDt("Pendencia", pendenciaDt.getId(), pendenciaDt.getId_UsuarioLog(), pendenciaDt.getIpComputadorLog(), String.valueOf(LogTipoDt.EncaminharPendencia), obDados.getPropriedades(), pendenciaDt.getPropriedades());

            // Consulta todos os dados da pendencia original
            pendenciaDt = pendenciaPs.consultarId(pendenciaDt.getId());

            // Verifica se a pendencia ja esta encaminhada
            if (!pendenciaDt.getPendenciaStatus().equals(String.valueOf(PendenciaStatusDt.ID_ENCAMINHADA))) {
            	//Data da operacao
            	Date data = new Date();

                // Inseri uma copia
                novaPendenciaDt = this.inserirFilhaEncaminhar(pendenciaDt, obFabricaConexao);

                // Configura o responsavel
                PendenciaResponsavelDt pendenciaResponsavelDt = new PendenciaResponsavelDt();
                pendenciaResponsavelDt.setId_Pendencia(novaPendenciaDt.getId());

                // Pela logica de responsabilidade
                if (serventiaCargoDt.getId().equals("") && serventiaDt.getId().equals(""))
                    throw new MensagemException("Selecione o destino da pend�ncia.");
                else if (serventiaCargoDt.getId() == null || serventiaCargoDt.getId().equals(""))
                    pendenciaResponsavelDt.setId_Serventia(serventiaDt.getId());
                else
                    pendenciaResponsavelDt.setId_ServentiaCargo(serventiaCargoDt.getId());

				// pendenciaResponsavelDt.setId_UsuarioLog(usuarioNe.getUsuarioDt().getId());
				// pendenciaResponsavelDt.setIpComputadorLog(usuarioNe.getUsuarioDt().getIpComputadorLog());

                // Inseri os responsaveis da pendencia
                PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();

                pendenciaResponsavelDt.setId_UsuarioLog(obLogDt.getId_UsuarioLog());
                pendenciaResponsavelDt.setIpComputadorLog(obLogDt.getIpComputadorLog());

                pendenciaResponsavelNe.inserir(pendenciaResponsavelDt, obFabricaConexao);

                // Consulta arquivos de resposta assinados para vincular com a
                // pendencia filha
                this.vincularArquivosResposta(pendenciaDt, novaPendenciaDt, obFabricaConexao);

             	// Fecha a pendencia original
                pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_ENCAMINHADA));
                pendenciaPs.encaminhar(pendenciaDt, usuarioNe.getUsuarioDt(), data);
                
                pendenciaPs.moverPendencia(pendenciaDt.getId());
                
                obLog.salvar(obLogDt, obFabricaConexao);

                // Finaliza a transacao
                obFabricaConexao.finalizarTransacao();
            } else {
                throw new MensagemException("Pend�ncia j� encaminhada.");
            }
        } catch (Exception e) {
            // Cancela a transacao iniciada
        	if(obFabricaConexao != null) {
        		obFabricaConexao.cancelarTransacao();
        	}
        	throw new MensagemException(e.getMessage());
        } finally {
        	if(obFabricaConexao != null) {
        		obFabricaConexao.fecharConexao();
        	}
        }
        pendenciaDt.setDataFim(Funcoes.DataHora(new Date()));
        return pendenciaDt;
    }

    /**
     * Consulta os arquivos assinados que sao resposta
     * 
     * @author Ronneesley Moura Teles
     * @since 05/02/2009 10:38
     * @param pendencia
     *            vo da pendencia
     * @param comConteudo
     *            se o arquivo tera o conteudo
     * @param conexao
     *            conexao
     * @return lista de vinculos
     * @throws Exception
     */
    private List consultarVinculosRespostaAssinados(PendenciaDt pendencia, boolean comConteudo, FabricaConexao conexao) throws Exception {
        PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
        List vinculos = pendenciaArquivoNe.consultarRespostaAssinados(pendencia, comConteudo, conexao);

        return vinculos;
    }

    /**
     * Consulta os arquivos assinados que sao resposta
     * 
     * @author Leandro Bernardes
     * @since 03/09/2009
     * @param pendencia
     *            vo da pendencia
     * @param comConteudo
     *            se o arquivo tera o conteudo
     * @param conexao
     *            conexao
     * @return lista de vinculos
     * @throws Exception
     */
    private List consultarVinculosResposta(PendenciaDt pendencia, boolean comConteudo, FabricaConexao fabrica ) throws Exception {
        PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
        List vinculos = pendenciaArquivoNe.consultarResposta(pendencia, comConteudo, fabrica);

        return vinculos;
    }

    /**
     * Consultar arquivos assinados que sao resposta de uma pendencia publicacao
     * 
     * @author Leandro Bernardes
     * @since 10/09/2009
     * @param pendencia
     *            vo de pendencia
     * @param comConteudo
     *            se o arquivo tera o conteudo
     * @param fabrica
     *            fabrica de conexao
     * @return lista de arquivos
     * @throws Exception
     */
    public List consultarArquivosPublicacoes(String Id_Pendencia) throws Exception {
        PendenciaDt pendenciaDt = new PendenciaDt();
        pendenciaDt.setId(Id_Pendencia);
        PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();

        List vinculos = pendenciaArquivoNe.consultarArquivosAssinadosPublicacao(pendenciaDt, false, null);
        //Retira somente os arquivos
        List arquivos = PendenciaArquivoNe.extrairArquivos(vinculos); 

        return arquivos;
    }

    /**
     * Consultar arquivos assinados que sao resposta de uma pendencia
     * 
     * @author Ronneesley Moura Teles
     * @since 05/02/2009 10:40
     * @param pendencia
     *            vo de pendencia
     * @param comConteudo
     *            se o arquivo tera o conteudo
     * @param fabrica
     *            fabrica de conexao
     * @return lista de arquivos
     * @throws Exception
     */
    private List consultarArquivosRespostaAssinados(PendenciaDt pendencia, boolean comConteudo, FabricaConexao fabrica) throws Exception {
        List vinculos = this.consultarVinculosRespostaAssinados(pendencia, comConteudo, fabrica);
        List arquivos = PendenciaArquivoNe.extrairArquivos(vinculos); // Retira somente os arquivos

        return arquivos;
    }

    /**
     * Consultar arquivos que sao resposta de uma pendencia
     * 
     * @author Leandro Bernardes
     * @since 03/09/2009
     * @param pendencia
     *            vo de pendencia
     * @param comConteudo
     *            se o arquivo tera o conteudo
     * @param fabrica
     *            fabrica de conexao
     * @return lista de arquivos
     * @throws Exception
     */
    private List consultarArquivosResposta(PendenciaDt pendencia, boolean comConteudo,FabricaConexao fabrica) throws Exception {
        List vinculos = this.consultarVinculosResposta(pendencia, comConteudo, fabrica);
        List arquivos = PendenciaArquivoNe.extrairArquivos(vinculos); // Retira
                                                                        // somente
                                                                        // os
                                                                        // arquivos

        return arquivos;
    }

    /**
     * Vincula os arquivos de resposta de uma pendencia para outra
     * 
     * @author Ronneesley Moura Teles
     * @since 05/02/2009 09:24
     * @param origem
     *            pendencia de origem
     * @param destino
     *            pendencia de destino
     * @param fabrica
     *            fabrica de conexao
     * @throws Exception
     */
    private void vincularArquivosResposta(PendenciaDt origem, PendenciaDt destino, FabricaConexao fabrica) throws Exception {
        // Consulta arquivos de resposta assinados para vincular com a pendencia de destino
        List arquivos = this.consultarArquivosRespostaAssinados(origem, false, fabrica);

        if (arquivos == null || arquivos.size() == 0) {
        	arquivos = this.consultarArquivosResposta(origem, false, fabrica);
        }

        // Vincula os arquivos
        PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
        pendenciaArquivoNe.vincularArquivos(destino, arquivos, true, fabrica);
    }

    /**
     * Verifica se � uma pendencia de processo a ser respondida
     * 
     * @author Leandro Bernardes
     * @param pendenciaDt
     *            pendencia a ser verificada
     * @return boolean
     */
    private boolean pendenciaProcessoResponder(PendenciaDt pendenciaDt) {
        boolean retorno = false;
        if ((pendenciaDt.isPendenciaDeProcesso() && !pendenciaDt.estaRespondida()) && (pendenciaDt.getPendenciaStatusCodigo().equals(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO)) || pendenciaDt.getPendenciaStatusCodigo().equals(String.valueOf(PendenciaStatusDt.ID_CORRECAO)))) {// NAO
                                                                                                                                                                                                                                                                                                // EXPEDE
            retorno = true;
        }
        return retorno;
    }
    
    /**
     * Verifica se for resposta simples n�o deve gerar movimenta��o e nem pend�ncias
     * 
     * @author Leandro Bernardes
     * @param pendenciaDt
     *            pendencia a ser verificada
     * @return boolean
     */
    private boolean gerarMovimentacaoPendencias(PendenciaDt pendenciaDt) {
        boolean retorno = false;
        Integer statusPendenciaCodigo = Funcoes.StringToInt(pendenciaDt.getPendenciaStatusCodigo());
        if (pendenciaDt.isPendenciaDeProcesso() && statusPendenciaCodigo != PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO 
        		&& statusPendenciaCodigo != PendenciaStatusDt.ID_CORRECAO) {
            if (!this.pendenciaTipoVerificar(pendenciaDt))
            	retorno = true;
        }
        return retorno;
    }
    
    /**
     * Verifica se for resposta simples n�o deve gerar movimenta��o e nem pend�ncias
     * 
     * @author Leandro Bernardes
     * @param pendenciaDt
     *            pendencia a ser verificada
     * @return boolean
     */
    private boolean isFinalizacaoCalculo(PendenciaDt pendenciaDt, UsuarioDt usuarioDt) {
        boolean retorno = false;
        Integer statusPendenciaCodigo = Funcoes.StringToInt(pendenciaDt.getPendenciaStatusCodigo());
        Integer pendenciaTipoCodigo = pendenciaDt.getPendenciaTipoCodigoToInt();
        if (pendenciaDt.isPendenciaDeProcesso() && statusPendenciaCodigo == PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO 
        		&& (PendenciaTipoDt.isCalculoContadoria(pendenciaTipoCodigo)) && Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.CONTADORES_VARA) {
            retorno = true;
        }
        return retorno;
    }
    
    /**
     * Verifica se for resposta simples n�o deve gerar movimenta��o e nem pend�ncias
     * 
     * @author Leandro Bernardes
     * @param pendenciaDt
     *            pendencia a ser verificada
     * @return boolean
     */
    private boolean isFinalizacaoPedidoCamara(PendenciaDt pendenciaDt, UsuarioDt usuarioDt) {
        boolean retorno = false;
        Integer statusPendenciaCodigo = Funcoes.StringToInt(pendenciaDt.getPendenciaStatusCodigo());
        Integer pendenciaTipoCodigo = pendenciaDt.getPendenciaTipoCodigoToInt();
        if (pendenciaDt.isPendenciaDeProcesso() && statusPendenciaCodigo == PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO 
        		&& pendenciaTipoCodigo == PendenciaTipoDt.PEDIDO_NATJUS_SEGUNDO_GRAU && Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.ANALISTA_FORENSE_2_GRAU) {
            retorno = true;
        }
        return retorno;
    }
    
    /**
     * Verifica se for resposta simples n�o deve gerar movimenta��o e nem pend�ncias
     * 
     * @author Leandro Bernardes
     * @param pendenciaDt
     *            pendencia a ser verificada
     * @return boolean
     */
    private boolean isFinalizacaoPedidoLaudo(PendenciaDt pendenciaDt, UsuarioDt usuarioDt) {
        boolean retorno = false;
        Integer statusPendenciaCodigo = Funcoes.StringToInt(pendenciaDt.getPendenciaStatusCodigo());
        Integer pendenciaTipoCodigo = pendenciaDt.getPendenciaTipoCodigoToInt();
        if (pendenciaDt.isPendenciaDeProcesso() && statusPendenciaCodigo == PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO 
        		&& pendenciaTipoCodigo == PendenciaTipoDt.PEDIDO_LAUDO && Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.ANALISTA_FORENSE) {
            retorno = true;
        }
        return retorno;
    }
    
    /**
     * Verifica se for resposta simples n�o deve gerar movimenta��o e nem pend�ncias
     * 
     * @author Leandro Bernardes
     * @param pendenciaDt
     *            pendencia a ser verificada
     * @return boolean
     */
    private boolean isFinalizacaoPedidoJusticaRestaurativa(PendenciaDt pendenciaDt, UsuarioDt usuarioDt) {
        boolean retorno = false;
        Integer statusPendenciaCodigo = Funcoes.StringToInt(pendenciaDt.getPendenciaStatusCodigo());
        Integer pendenciaTipoCodigo = pendenciaDt.getPendenciaTipoCodigoToInt();
        if (pendenciaDt.isPendenciaDeProcesso() && statusPendenciaCodigo == PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO 
        		&& pendenciaTipoCodigo == PendenciaTipoDt.PEDIDO_JUSTICA_RESTAURATIVA && Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.ANALISTA_FORENSE) {
            retorno = true;
        }
        return retorno;
    }
    
    /**
     * Verifica se for resposta simples n�o deve gerar movimenta��o e nem pend�ncias
     * 
     * @author Leandro Bernardes
     * @param pendenciaDt
     *            pendencia a ser verificada
     * @return boolean
     */
    private boolean isFinalizacaoPedidoCENOPES(PendenciaDt pendenciaDt, UsuarioDt usuarioDt) {
        boolean retorno = false;
        Integer statusPendenciaCodigo = Funcoes.StringToInt(pendenciaDt.getPendenciaStatusCodigo());
        Integer pendenciaTipoCodigo = pendenciaDt.getPendenciaTipoCodigoToInt();
        if (pendenciaDt.isPendenciaDeProcesso() && statusPendenciaCodigo == PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO 
        		&& pendenciaTipoCodigo == PendenciaTipoDt.PEDIDO_CENOPES && Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.CONSULTOR_SISTEMAS_EXTERNOS) {
            retorno = true;
        }
        return retorno;
    }

    /**
     * Verifica se uma pend�ncia possui no m�nimo um arquivo assinado
     * 
     * @author Leandro Bernardes
     * @param pendenciaDt
     *            pendencia a ser verificada
     * @param arquivos
     *            lista de arquivos
      @return String com a mensagem de retorno
     */
    public String validarArquivosPendencia(PendenciaDt pendenciaDt, List arquivos) throws Exception {
        String msgValidacao = "";
        boolean retorno = false;
        List temp = new ArrayList();
        
        if (arquivos != null && arquivos.size() > 0)
        	temp.addAll(arquivos);
        
        if (temp != null && temp.size() > 0) {
            for (int i = 0; i < temp.size(); i++) {
                ArquivoDt arquivoDt = (ArquivoDt) temp.get(i);
                if (arquivoDt.isArquivoAssinado()) {
                    retorno = true;
                    //break;
                } else {
                	arquivos.remove(i);
                }
            }

        } else {
            List arqs = this.consultarArquivosRespostaAssinados(pendenciaDt, true, null);
            if (arqs.size() > 0 || (pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.ELABORACAO_VOTO))
                retorno = true;
            else
                retorno = false;
        }

        if (!retorno) msgValidacao = "Pend�ncia deve possuir ao menos um arquivo assinado.";

        return msgValidacao;
    }    
    
    /**
     * Responde com arquivos
     * 
     * @author Ronneesley Moura Teles
     * @since 09/01/2009 14:26
     * @param pendenciaDt
     *            vo de pendencia
     * @param usuarioDt
     *            vo de usuario
     * @param arquivos
     *            Colecao de arquivos a serem inseridos
     * @throws Exception
     */
    public void responderPendencia(PendenciaDt pendenciaDt, UsuarioDt usuarioDt, List arquivos, ServentiaDt serventiaDt) throws MensagemException, Exception {
   	 //Tratamento para salvar mandado no SPG----------------------------------------------------------------------------------------------------------
//       if ((pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.MANDADO &&
//       		Funcoes.StringToInt(usuarioDt.getServentiaTipo()) != ServentiaTipoDt.SEGUNDO_GRAU){
//       	expedirMandadoPrimeiroGrau(pendenciaDt, usuarioDt, arquivos, serventiaDt);
//   	} else {
	        if (this.pendenciaProcessoResponder(pendenciaDt))
	            this.responderPendenciaExpedidaComArquivos(pendenciaDt, usuarioDt, arquivos, serventiaDt, null);
	        else
	            this.responderComArquivos(pendenciaDt, usuarioDt, arquivos, serventiaDt,null,  null);
//   	}
   }
    /**
     * Distribuir carta precat�ria
     * 
     * @author Leandro Bernardes
     * @param pendenciaDt
     *            vo de pendencia
     * @param usuarioDt
     *            vo de usuario
     * @param arquivos
     *            Colecao de arquivos a serem inseridos
     * @param ComarcaDt
     *            comarcaDt onde ser� distribu�da a carta precat�ria para um serventia cargo
     * @throws Exception
     */
    public void distribuirCartaPrecatoria(PendenciaDt pendenciaDt, UsuarioDt usuarioDt, List arquivos, ComarcaDt comarcaDt) throws Exception {
        this.responderComArquivos(pendenciaDt, usuarioDt, arquivos, null, comarcaDt, null);
    }

    /**
     * Responde com arquivos para webservice
     * 
     * @author Leandro Bernardes
     * @param String
     * 			  id da pendencia
     * @param usuarioDt
     *            vo de usuario
     * @param arquivos
     *            Colecao de arquivos a serem inseridos
     * @throws Exception
     */
    public void responderPendencia(String idPendencia, UsuarioDt usuarioDt, List arquivos, ServentiaDt serventiaDt) throws Exception {
        PendenciaDt pendenciaDt = new PendenciaDt();
        pendenciaDt = this.consultarId(idPendencia);
        pendenciaDt.setId_UsuarioLog(usuarioDt.getId());
        pendenciaDt.setIpComputadorLog(usuarioDt.getIpComputadorLog());
        if (pendenciaDt.getPendenciaStatusCodigo().equals(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO)))
            this.responderPendenciaExpedidaComArquivos(pendenciaDt, usuarioDt, arquivos, serventiaDt, null);
        else
            this.responderComArquivos(pendenciaDt, usuarioDt, arquivos, serventiaDt, null, null);
    }

    /**
     * Responde com arquivos
     * 
     * @author Ronneesley Moura Teles
     * @since 15/12/2008 10:43
     * @param pendenciaDt
     *            vo de pendencia
     * @param usuarioDt
     *            vo de usuario
     * @param arquivos
     *            Colecao de arquivos a serem inseridos
     * @param fabrica
     *            fabrica de conexao para poder continuar uma transacao
     * @throws Exception
     */
    public void responderComArquivos(PendenciaDt pendenciaDt, UsuarioDt usuarioDt, List arquivos, ServentiaDt serventiaDt, ComarcaDt comarcaDt, FabricaConexao fabrica) throws MensagemException, Exception {
        FabricaConexao obFabricaConexao = null;
        try {
            if (fabrica == null) {
                obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

                obFabricaConexao.iniciarTransacao();
            } else
                obFabricaConexao = fabrica;

            // Se tem arquivos
            if (arquivos != null && arquivos.size() > 0) {
                this.inserirArquivos(pendenciaDt, arquivos, true, usuarioDt, obFabricaConexao);
            }

            this.responder(pendenciaDt, usuarioDt, PendenciaStatusDt.ID_CUMPRIDA, serventiaDt, comarcaDt, obFabricaConexao);

            if (fabrica == null) obFabricaConexao.finalizarTransacao();
        } catch (Exception e) {
            if (fabrica == null) obFabricaConexao.cancelarTransacao();
            throw e;
        } finally {
            if (fabrica == null) obFabricaConexao.fecharConexao();
        }
    }
    
    /**
     * finaliza pend�ncia de carga em processo
     * 
     * @param pendenciaDt
     *            vo de pendencia
     * @param usuarioDt
     *            vo de usuario
     * @throws Exception
     */
    public void realizarCargaProcesso(PendenciaDt pendenciaDt, UsuarioDt usuarioDt) throws MensagemException, Exception {
        FabricaConexao obFabricaConexao = null;
        try {
                obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
                obFabricaConexao.iniciarTransacao();
                MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
                
                if (!pendenciaDt.estaRespondida()) {

                        PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
                        LogDt obLogDt = new LogDt("Pendencia", pendenciaDt.getId(), pendenciaDt.getId_UsuarioLog(), pendenciaDt.getIpComputadorLog(), String.valueOf(LogTipoDt.FinalizarPendencia), obDados.getPropriedades(), pendenciaDt.getPropriedades());
                        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
                        pendenciaDt.setDataFim(df.format(new Date()));
                        pendenciaDt.setDataVisto(df.format(new Date()));
                        	
                        ProcessoNe processoNe = new ProcessoNe();
                     	pendenciaDt.setProcessoDt(processoNe.consultarId(pendenciaDt.getId_Processo(), obFabricaConexao));
                            
                      	//Gerar movimenta��o da pend�ncia Solicitacao de carga
                      	movimentacaoNe.gerarMovimentacaoCargaProcesso(pendenciaDt.getId_Processo(), pendenciaDt.getProcessoDt().getProcessoNumeroCompleto(), pendenciaDt.getListaArquivos(), usuarioDt, " para "+pendenciaDt.getNomeUsuarioCadastrador(), obLogDt, obFabricaConexao);
                        	
                        if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equals(""))
                            obPersistencia.responder(pendenciaDt.getId(), usuarioDt.getId_UsuarioServentiaChefe(), PendenciaStatusDt.ID_CUMPRIDA, pendenciaDt.getDataFim(), pendenciaDt.getDataVisto());
                        else
                            obPersistencia.responder(pendenciaDt.getId(), usuarioDt.getId_UsuarioServentia(), PendenciaStatusDt.ID_CUMPRIDA, pendenciaDt.getDataFim(), pendenciaDt.getDataVisto());
                        
                        PendenciaDt pendenciaRetornoDt = this.gerarPendenciaFilhaSolicitarCargaProcesso(pendenciaDt, usuarioDt, obFabricaConexao);
                        //Informacao Necessaria para informar na tela de recibo de carga - data devolucao dos autos
                        pendenciaDt.setDataLimite(pendenciaRetornoDt.getDataLimite());
                        pendenciaDt.setNomeUsuarioFinalizador(usuarioDt.getNome());
                        
                        if (pendenciaDt != null && pendenciaDt.getDataFim()!= null && pendenciaDt.getDataFim().length()>0 && pendenciaDt.getDataVisto() != null && pendenciaDt.getDataVisto().length()>0){
                        	obPersistencia.moverPendencia(pendenciaDt.getId());
                        }
                        
                        obLog.salvar(obLogDt, obFabricaConexao);
                        
                } else {
                    throw new MensagemException("Pend�ncia j� foi finalizada.");
                }

            obFabricaConexao.finalizarTransacao();
            
        } catch (Exception e) {
             obFabricaConexao.cancelarTransacao();
            throw e;
        } finally {
             obFabricaConexao.fecharConexao();
        }
    }
    
    /**
     * finaliza pend�ncia Aguardando retorno de carga em processo
     * 
     * @param pendenciaDt
     *            vo de pendencia
     * @param usuarioDt
     *            vo de usuario
     * @throws Exception
     */
    public void realizarDevolucaoAutos(PendenciaDt pendenciaDt, UsuarioDt usuarioDt) throws MensagemException, Exception {
        FabricaConexao obFabricaConexao = null;
        try {
                obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
                obFabricaConexao.iniciarTransacao();
                MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
                
                if (!pendenciaDt.estaRespondida()) {

                        PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
                        LogDt obLogDt = new LogDt("Pendencia", pendenciaDt.getId(), pendenciaDt.getId_UsuarioLog(), pendenciaDt.getIpComputadorLog(), String.valueOf(LogTipoDt.FinalizarPendencia), obDados.getPropriedades(), pendenciaDt.getPropriedades());
                        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
                        pendenciaDt.setDataFim(df.format(new Date()));
                        pendenciaDt.setDataVisto(df.format(new Date()));
                        	
                        ProcessoNe processoNe = new ProcessoNe();
                     	pendenciaDt.setProcessoDt(processoNe.consultarId(pendenciaDt.getId_Processo(), obFabricaConexao));
                        
                     	String complementoMovimentacao = "";
                     	if (pendenciaDt.getId_PendenciaPai() != null && pendenciaDt.getId_PendenciaPai().length()>0){
                     		complementoMovimentacao = " por "+pendenciaDt.getNomeUsuarioCadastrador();
                     	}
                     	
                      	//Gerar movimenta��o da pend�ncia Solicitacao de carga
                      	movimentacaoNe.gerarMovimentacaoDevolucaoAutos(pendenciaDt.getId_Processo(), pendenciaDt.getProcessoDt().getProcessoNumeroCompleto(), pendenciaDt.getListaArquivos(), usuarioDt, complementoMovimentacao, obLogDt, obFabricaConexao);
                        	
                        if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equals(""))
                            obPersistencia.responder(pendenciaDt.getId(), usuarioDt.getId_UsuarioServentiaChefe(), PendenciaStatusDt.ID_CUMPRIDA, pendenciaDt.getDataFim(), pendenciaDt.getDataVisto());
                        else
                            obPersistencia.responder(pendenciaDt.getId(), usuarioDt.getId_UsuarioServentia(), PendenciaStatusDt.ID_CUMPRIDA, pendenciaDt.getDataFim(), pendenciaDt.getDataVisto());
                        
                        if (pendenciaDt != null && pendenciaDt.getDataFim()!= null && pendenciaDt.getDataFim().length()>0 && pendenciaDt.getDataVisto() != null && pendenciaDt.getDataVisto().length()>0){
                        	obPersistencia.moverPendencia(pendenciaDt.getId());
                        }
                        
                        obLog.salvar(obLogDt, obFabricaConexao);
                        
                } else {
                    throw new MensagemException("Pend�ncia j� foi finalizada.");
                }

            obFabricaConexao.finalizarTransacao();
            
        } catch (Exception e) {
             obFabricaConexao.cancelarTransacao();
            throw e;
        } finally {
             obFabricaConexao.fecharConexao();
        }
    }
    
    /**
     * Responde pendencia expedida para uma serventia (on-line) com arquivos
     * 
     * @author Leandro Bernardes
     * @since 15/12/2008 10:43
     * @param pendenciaDt
     *            vo de pendencia
     * @param usuarioDt
     *            vo de usuario
     * @param arquivos
     *            Colecao de arquivos a serem inseridos
     * @param fabrica
     *            fabrica de conexao para poder continuar uma transacao
     * @throws Exception
     */
    public void responderPendenciaExpedidaComArquivos(PendenciaDt pendenciaDt, UsuarioDt usuarioDt, List arquivos, ServentiaDt serventiaDt, FabricaConexao fabrica) throws MensagemException, Exception {
        FabricaConexao obFabricaConexao = null;
        try {
            if (fabrica == null) {
                obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

                obFabricaConexao.iniciarTransacao();
            } else
                obFabricaConexao = fabrica;

            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

            // Se tem arquivos
            if (arquivos != null && arquivos.size() > 0) {
            	pendenciaDt.setListaArquivos(arquivos);
                this.inserirArquivos(pendenciaDt, arquivos, true, usuarioDt, obFabricaConexao);
            }

            this.responder(pendenciaDt, usuarioDt, PendenciaStatusDt.ID_CUMPRIMENTO_AGUARDANDO_VISTO, serventiaDt, null, obFabricaConexao);

            // Limpa data visto da pendencia respondida por uma serventia (on-line), N�o se palica para as pend�ncias tratada no if.
            if (!this.isFinalizacaoCalculo(pendenciaDt, usuarioDt) && !this.isFinalizacaoPedidoCamara(pendenciaDt, usuarioDt) 
            		&& !this.isFinalizacaoPedidoLaudo(pendenciaDt, usuarioDt)  && !this.isFinalizacaoPedidoCENOPES(pendenciaDt, usuarioDt) 
            		 && !this.isFinalizacaoPedidoJusticaRestaurativa(pendenciaDt, usuarioDt) && !this.isFinalizacaoOficioDelegacia(pendenciaDt, usuarioDt)
            	){
            	obPersistencia.retirarVisto(pendenciaDt);
            	pendenciaDt.setDataVisto("");
            }

            if (fabrica == null) obFabricaConexao.finalizarTransacao();
        } catch (Exception e) {
            if (fabrica == null) obFabricaConexao.cancelarTransacao();
            throw e;
        } finally {
            if (fabrica == null) obFabricaConexao.fecharConexao();
        }
    }

    /**
     * Marcar visto para este momento
     * 
     * @author Ronneesley Moura Teles
     * @since 14/01/2008 14:39
     * @param pendenciaDt
     *            vo de pendencia
     * @param usuarioDt
     *            vo de usuario, usuario que marca o visto
     * @throws Exception
     */
    public void marcarVistoAgora(PendenciaDt pendenciaDt, UsuarioDt usuarioDt) throws Exception {
        pendenciaDt.setDataVisto(Funcoes.DataHora(new Date()));

        this.marcarVisto(pendenciaDt, usuarioDt);
    }

    /**
     * Marcar visto
     * 
     * @author Ronneesley Moura Teles
     * @since 14/01/2008 14:32
     * @param pendenciaDt
     *            vo da pendencia
     * @param usuarioDt
     *            usuario que ira marcar
     * @throws Exception
     */
    public void marcarVisto(PendenciaDt pendenciaDt, UsuarioDt usuarioDt) throws Exception {
        this.marcarVisto(pendenciaDt, usuarioDt, null);
    }

    /**
     * Marcar visto
     * 
     * @author Ronneesley Moura Teles
     * @since 09/01/2008 11:02
     * @param pendenciaDt
     *            objeto de pendencias
     * @param usuarioDt
     *            vo de usuario, usuario logado
     * @param fabrica
     *            fabrica para continuar uma trasacao
     * @throws Exception
     */
    public void marcarVisto(PendenciaDt pendenciaDt, UsuarioDt usuarioDt, FabricaConexao fabrica) throws Exception {
        FabricaConexao obFabricaConexao = null;
        try {
            if (fabrica == null) {
                obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
                obFabricaConexao.iniciarTransacao();
            } else
                obFabricaConexao = fabrica;

            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

            obPersistencia.marcarVisto(pendenciaDt);
            
            LogDt obLogDt = new LogDt("Pendencia", pendenciaDt.getId(), usuarioDt.getId(), usuarioDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), "Data visto: "+obDados.getDataVisto(), "Data visto: "+pendenciaDt.getDataVisto());
            obLog.salvar(obLogDt, obFabricaConexao);
            
            if (pendenciaDt != null && pendenciaDt.getDataFim()!= null && pendenciaDt.getDataFim().length()>0
            		&& pendenciaDt.getDataVisto() != null && pendenciaDt.getDataVisto().length()>0){
            	obPersistencia.moverPendencia(pendenciaDt.getId());
            }

            if (fabrica == null) obFabricaConexao.finalizarTransacao();
        } catch (Exception e) {
            if (fabrica == null) obFabricaConexao.cancelarTransacao();
            throw e;
        } finally {
            if (fabrica == null) obFabricaConexao.fecharConexao();
        }
    }

    /**
     * Marcar visto
     * 
     * @author leandro bernardes
     * @param pendenciaDt
     *            vo da pendencia
     * @param usuarioDt
     *            usuario que ira marcar
     * @throws Exception
     */
    public void marcarVistoPendenciaPrazo(PendenciaDt pendenciaDt, UsuarioDt usuarioDt) throws Exception {
        if (pendenciaDt.getDataVisto() == null || pendenciaDt.getDataVisto().equals("")) 
        	this.marcarVistoPendenciaPrazo(pendenciaDt, usuarioDt, null);
    }

    /**
     * Marcar visto
     * 
     * @author leandro bernardes
     * @param pendenciaDt
     *            objeto de pendencias
     * @param usuarioDt
     *            vo de usuario, usuario logado
     * @param fabrica
     *            fabrica para continuar uma trasacao
     * @throws Exception
     */
    public void marcarVistoPendenciaPrazo(PendenciaDt pendenciaDt, UsuarioDt usuarioDt, FabricaConexao fabrica) throws Exception {
        FabricaConexao obFabricaConexao = null;
        try {
            if (fabrica == null) {
                obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
                obFabricaConexao.iniciarTransacao();
            } else
                obFabricaConexao = fabrica;

            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

            if ((pendenciaDt.getPendenciaStatusCodigo().equals(String.valueOf(PendenciaStatusDt.ID_CUMPRIMENTO_AGUARDANDO_VISTO))) && (pendenciaDt.getDataVisto() == null || pendenciaDt.getDataVisto().equals(""))) {
            	
            	//Antes de vistar � preciso criar uma pend�ncia filha
            	pendenciaDt.setId_UsuarioLog(usuarioDt.getId());
            	pendenciaDt.setIpComputadorLog(usuarioDt.getIpComputadorLog());
            	pendenciaDt.setId_PendenciaPai(pendenciaDt.getId());
            	pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_CUMPRIDA));
            	pendenciaDt.setId_UsuarioFinalizador(usuarioDt.getId_UsuarioServentia());
            	this.gerarPendenciaFilha(pendenciaDt, this.consultarResponsaveis(pendenciaDt), null, null, false, false, false, obFabricaConexao);
            	
    			if ( Funcoes.StringToInt(pendenciaDt.getPrazo()) > 0) {
                    PrazoSuspensoNe prazoSuspensoNe = new PrazoSuspensoNe();
                    ServentiaNe serventiaNe = new ServentiaNe();
                    ProcessoNe processoNe = new ProcessoNe();

                    ProcessoDt processoDt = processoNe.consultarId(pendenciaDt.getId_Processo());
                    ServentiaDt serventiaDt = serventiaNe.consultarId(processoDt.getId_Serventia());

                    if (this.isUtilizaPrazoCorrido(pendenciaDt.getId_Processo(), obFabricaConexao)) {
                    	pendenciaDt.setDataLimite(prazoSuspensoNe.getProximaDataValidaPrazoCorrido(new Date(), Funcoes.StringToInt(pendenciaDt.getPrazo()), serventiaDt , obFabricaConexao) + " 00:00:00");
                    
                    } else if (this.isProcessoVaraPrecatoria(pendenciaDt.getId_Processo(), obFabricaConexao)) {
                  	  // Em caso de vara de precatoria deve-se testar o processo que originou a precatoria
                  	  if (this.isUtilizaPrazoCorrido(processoDt.getId_ProcessoPrincipal(), obFabricaConexao))
                  		  pendenciaDt.setDataLimite(prazoSuspensoNe.getProximaDataValidaPrazoCorrido(new Date(), Funcoes.StringToInt(pendenciaDt.getPrazo()), serventiaDt , obFabricaConexao) + " 00:00:00");
                  	  else
                  		  pendenciaDt.setDataLimite(prazoSuspensoNe.getProximaDataValidaNovoCPC(new Date(), Funcoes.StringToInt(pendenciaDt.getPrazo()), serventiaDt , obFabricaConexao) + " 00:00:00");
                  
                    } else
                    	pendenciaDt.setDataLimite(prazoSuspensoNe.getProximaDataValidaNovoCPC(new Date(), Funcoes.StringToInt(pendenciaDt.getPrazo()), serventiaDt , obFabricaConexao) + " 00:00:00");

                    obPersistencia.alterarLimite(pendenciaDt);
                    obPersistencia.retirarVisto(pendenciaDt);
                    // Limpa dados do objeto
                    pendenciaDt.setDataVisto("");
                }
    			
    			// Se nao for responsavel inclui-o como um, para que a pendencia apare�a em prazo decorrido/finalizada da serventia
    			PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
    			PendenciaResponsavelDt responsavel = new PendenciaResponsavelDt();
    			responsavel.setId_Serventia(usuarioDt.getId_Serventia());
    			responsavel.setId_Pendencia(pendenciaDt.getId());
    			responsavel.setId_UsuarioLog(usuarioDt.getId());
                responsavel.setIpComputadorLog(usuarioDt.getIpComputadorLog());
    			pendenciaResponsavelNe.inserir(responsavel, obFabricaConexao);
    			
    			//Finalizando a pend�ncia filha
    			LogDt obLogDt = new LogDt("Pendencia", pendenciaDt.getId(), usuarioDt.getId(), usuarioDt.getIpComputadorLog(), String.valueOf(LogTipoDt.FinalizarPendencia), "", "");
    			this.finalizar(pendenciaDt, usuarioDt, obFabricaConexao);
    			obLog.salvar(obLogDt, obFabricaConexao);
    			//Finalizando a pend�ncia pai
    			pendenciaDt = this.consultarId(pendenciaDt.getId_PendenciaPai());
    			LogDt obLogDt2 = new LogDt("Pendencia", pendenciaDt.getId(), usuarioDt.getId(), usuarioDt.getIpComputadorLog(), String.valueOf(LogTipoDt.FinalizarPendencia), obDados.getPropriedades(), pendenciaDt.getPropriedades());
    			pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_CUMPRIDA));
    			this.finalizar(pendenciaDt, usuarioDt, obFabricaConexao);
                obLog.salvar(obLogDt2, obFabricaConexao);
                

            } else if ((pendenciaDt.getDataFim() != null && !pendenciaDt.getDataFim().equals("")) && (pendenciaDt.getDataVisto() == null || pendenciaDt.getDataVisto().equals(""))) {

                this.finalizar(pendenciaDt, usuarioDt, obFabricaConexao);

            } else {
                throw new MensagemException("N�o foi possivel vistar a pendencia.");
            }

            if (fabrica == null) obFabricaConexao.finalizarTransacao();
        } catch (Exception e) {
            if (fabrica == null) obFabricaConexao.cancelarTransacao();
            throw e;
        } finally {
            if (fabrica == null) obFabricaConexao.fecharConexao();
        }
    }

    /**
     * Reponder uma pendencia utilizando uma transacao existente
     * 
     * @author Ronneesley Moura Teles
     * @since 21/01/2009 11:29
     * @param pendenciaDt
     *            pojo da pendencia
     * @param usuarioDt
     *            vo de usuario
     * @param status
     *            status com que sera fechada a pendencia
     * @throws Exception
     */
    public void responder(PendenciaDt pendenciaDt, UsuarioDt usuarioDt, int status) throws Exception {
        this.responder(pendenciaDt, usuarioDt, status, null, null, null);
    }
    
    /**
     * Reponder uma pendencia utilizando uma transacao existente
     * 
     * @author Ronneesley Moura Teles
     * @since 21/01/2009 11:29
     * @param pendenciaDt
     *            pojo da pendencia
     * @param usuarioDt
     *            vo de usuario
     * @param status
     *            status com que sera fechada a pendencia
     * @param fabrica
     *            fabrica para continuar uma transacao
     * @throws Exception
     */
    public void responder(PendenciaDt pendenciaDt, UsuarioDt usuarioDt, int status, ServentiaDt serventiaDt, ComarcaDt comarcaDt, FabricaConexao fabrica) throws MensagemException, Exception {
        FabricaConexao obFabricaConexao = null;
        MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
        if (!pendenciaDt.estaRespondida()) {

            try {

                if (fabrica == null) {
                    obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
                    obFabricaConexao.iniciarTransacao();
                } else{
                    obFabricaConexao = fabrica;
                }
                
                //S� � possivel finalizar um verificar se o processo estiver com o assunto definido
                if (pendenciaDt.isVerificar()){
             	   if (!(new ProcessoNe()).isTemAssunto(pendenciaDt.getId_Processo())){
             		   throw new MensagemException("Processo Sem Assunto: Para finalizar a pend�ncia � necess�rio definir pelo menos um Assunto no Processo.");
             	   }             	 
                }
                
                // e-Carta
                if (pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.VERIFICAR_ENDERECO_PARTE) {
                	retornarStatusAguardandoEnvioCorreios(pendenciaDt.getId_ProcessoParte(), obFabricaConexao);
                }

                PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

                LogDt obLogDt = new LogDt("Pendencia", pendenciaDt.getId(), pendenciaDt.getId_UsuarioLog(), pendenciaDt.getIpComputadorLog(), String.valueOf(LogTipoDt.FinalizarPendencia), obDados.getPropriedades(), pendenciaDt.getPropriedades());
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 

                if (pendenciaDt.getDataFim() == null || pendenciaDt.getDataFim().equals("")){
                    pendenciaDt.setDataFim(df.format(new Date()));
                } else {
                    pendenciaDt.setDataFim(pendenciaDt.getDataFim()+" 00:00:00");
                }
                
                if (this.isFinalizacaoCalculo(pendenciaDt, usuarioDt)){
                	// Calculo j� � finalizado
                	status = PendenciaStatusDt.ID_CUMPRIDA;
                    pendenciaDt.setDataVisto(df.format(new Date()));
                	
                    ProcessoNe processoNe = new ProcessoNe();
                	pendenciaDt.setProcessoDt(processoNe.consultarId(pendenciaDt.getId_Processo(), obFabricaConexao));
                    
                	//Gerar movimenta��o c�lculo
                	switch (pendenciaDt.getPendenciaTipoCodigoToInt()) {
					case PendenciaTipoDt.CONTADORIA_CALCULO_CONTA:
	                	movimentacaoNe.gerarMovimentacaoCalculo(pendenciaDt.getId_Processo(), pendenciaDt.getProcessoDt().getProcessoNumeroCompleto(),  MovimentacaoTipoDt.REALIZADO_CALCULO_DE_ATUALIZACAO_CONTA, pendenciaDt.getListaArquivos(), usuarioDt, "", obLogDt, obFabricaConexao);
						break;
					case PendenciaTipoDt.CONTADORIA_CALCULO_CUSTAS:
					case PendenciaTipoDt.PEDIDO_CONTADORIA_CRIMINAL:
					case PendenciaTipoDt.PEDIDO_CONTADORIA:
	                	movimentacaoNe.gerarMovimentacaoCalculo(pendenciaDt.getId_Processo(), pendenciaDt.getProcessoDt().getProcessoNumeroCompleto(),  MovimentacaoTipoDt.REALIZADO_CALCULO_DE_CUSTAS, pendenciaDt.getListaArquivos(), usuarioDt, "", obLogDt, obFabricaConexao);					
						break;
					case PendenciaTipoDt.CONTADORIA_CALCULO_LIQUIDACAO:
	                	movimentacaoNe.gerarMovimentacaoCalculo(pendenciaDt.getId_Processo(), pendenciaDt.getProcessoDt().getProcessoNumeroCompleto(),  MovimentacaoTipoDt.REALIZADO_CALCULO_DE_LIQUIDACAO, pendenciaDt.getListaArquivos(), usuarioDt, "", obLogDt, obFabricaConexao);
						break;
					case PendenciaTipoDt.CONTADORIA_CALCULO_TRIBUTOS:
	                	movimentacaoNe.gerarMovimentacaoCalculo(pendenciaDt.getId_Processo(), pendenciaDt.getProcessoDt().getProcessoNumeroCompleto(),  MovimentacaoTipoDt.REALIZADO_CALCULO_DE_TRIBUTOS, pendenciaDt.getListaArquivos(), usuarioDt, "", obLogDt, obFabricaConexao);
						break;
					case PendenciaTipoDt.CONTADORIA_JUNTADA_DOCUMENTO:
	                	movimentacaoNe.gerarMovimentacaoCalculo(pendenciaDt.getId_Processo(), pendenciaDt.getProcessoDt().getProcessoNumeroCompleto(),  MovimentacaoTipoDt.JUNTADA_DE_DOCUMENTO, pendenciaDt.getListaArquivos(), usuarioDt, "", obLogDt, obFabricaConexao);
						break;
					}
                	
                	//Gerar pendencia Verificar C�lculo
                	pendenciaDt.setId_ServentiaProcesso(pendenciaDt.getProcessoDt().getId_Serventia());
                	this.gerarPendenciaVerificarCalculo(pendenciaDt, usuarioDt, pendenciaDt.getListaArquivos(), obLogDt, obFabricaConexao);
                	
                }
                
                if (this.isFinalizacaoPedidoCamara(pendenciaDt, usuarioDt)){
                	// pedido c�mara j� � finalizado
                	status = PendenciaStatusDt.ID_CUMPRIDA;
                    pendenciaDt.setDataVisto(df.format(new Date()));
                	
                    ProcessoNe processoNe = new ProcessoNe();
                	pendenciaDt.setProcessoDt(processoNe.consultarId(pendenciaDt.getId_Processo(), obFabricaConexao));
                    
                	//Gerar movimenta��o da pend�ncia C�mara de Sa�de
                	movimentacaoNe.gerarMovimentacaoRespostaCamaraSaude(pendenciaDt.getId_Processo(), pendenciaDt.getProcessoDt().getProcessoNumeroCompleto(), pendenciaDt.getListaArquivos(), usuarioDt, " - Parecer C�mara de Sa�de ", obLogDt, obFabricaConexao);
                	
                	//Gerar pendencia Verificar C�mara de Sa�de
                	pendenciaDt.setId_ServentiaProcesso(pendenciaDt.getProcessoDt().getId_Serventia());
                	this.gerarPendenciaVericarRespostaCamaraSaude(pendenciaDt, usuarioDt, pendenciaDt.getListaArquivos(), obLogDt, obFabricaConexao);
                	
                }
                
                if (this.isFinalizacaoPedidoLaudo(pendenciaDt, usuarioDt)){
                	// pedido de laudo j� � finalizado
                	status = PendenciaStatusDt.ID_CUMPRIDA;
                    pendenciaDt.setDataVisto(df.format(new Date()));
                	
                    ProcessoNe processoNe = new ProcessoNe();
                	pendenciaDt.setProcessoDt(processoNe.consultarId(pendenciaDt.getId_Processo(), obFabricaConexao));
                    
                	//Gerar movimenta��o da pend�ncia Pedido de Laudo
                	movimentacaoNe.gerarMovimentacaoRespostaPedidoLaudo(pendenciaDt.getId_Processo(), pendenciaDt.getProcessoDt().getProcessoNumeroCompleto(), pendenciaDt.getListaArquivos(), usuarioDt, " - Laudo Equipe Interprofissional ", obLogDt, obFabricaConexao);
                	
                	//Gerar pendencia Verificar resposta pedido de laudo
                	pendenciaDt.setId_ServentiaProcesso(pendenciaDt.getProcessoDt().getId_Serventia());
                	this.gerarPendenciaVericarRespostaPedidoLaudoRelatorio(pendenciaDt, usuarioDt, pendenciaDt.getListaArquivos(), obLogDt, obFabricaConexao);
                	
                }
                
                if (this.isFinalizacaoPedidoJusticaRestaurativa(pendenciaDt, usuarioDt)){
                	// pedido de laudo j� � finalizado
                	status = PendenciaStatusDt.ID_CUMPRIDA;
                    pendenciaDt.setDataVisto(df.format(new Date()));
                	
                    ProcessoNe processoNe = new ProcessoNe();
                	pendenciaDt.setProcessoDt(processoNe.consultarId(pendenciaDt.getId_Processo(), obFabricaConexao));
                    
                	//Gerar movimenta��o da pend�ncia Pedido de Laudo
                	movimentacaoNe.gerarMovimentacaoRespostaPedidoLaudo(pendenciaDt.getId_Processo(), pendenciaDt.getProcessoDt().getProcessoNumeroCompleto(), pendenciaDt.getListaArquivos(), usuarioDt, " - Relat�rio da Justi�a Restaurativa ", obLogDt, obFabricaConexao);
                	
                	//Gerar pendencia Verificar resposta pedido de laudo
                	pendenciaDt.setId_ServentiaProcesso(pendenciaDt.getProcessoDt().getId_Serventia());
                	this.gerarPendenciaVericarRespostaPedidoLaudoRelatorio(pendenciaDt, usuarioDt, pendenciaDt.getListaArquivos(), obLogDt, obFabricaConexao);
                	
                }
                
                if (this.isFinalizacaoPedidoCENOPES(pendenciaDt, usuarioDt)){
                	// pedido de CENOPES j� � finalizado
                	status = PendenciaStatusDt.ID_CUMPRIDA;
                    pendenciaDt.setDataVisto(df.format(new Date()));
                	
                    ProcessoNe processoNe = new ProcessoNe();
                	pendenciaDt.setProcessoDt(processoNe.consultarId(pendenciaDt.getId_Processo(), obFabricaConexao));
                    
                	//Gerar movimenta��o da pend�ncia Pedido de Laudo
                	movimentacaoNe.gerarMovimentacaoRespostaPedidoCENOPES(pendenciaDt.getId_Processo(), pendenciaDt.getProcessoDt().getProcessoNumeroCompleto(), pendenciaDt.getListaArquivos(), usuarioDt, " - CENOPES Central de Operacionaliza��o Sistemas Conveniados ", obLogDt, obFabricaConexao);
                	
                	//Gerar pendencia Verificar resposta pedido CENOPES
                	pendenciaDt.setId_ServentiaProcesso(pendenciaDt.getProcessoDt().getId_Serventia());
                	this.gerarPendenciaVericarRespostaPedidoCENOPES(pendenciaDt, usuarioDt, pendenciaDt.getListaArquivos(), obLogDt, obFabricaConexao);
                	
                }
                
                if (this.isFinalizacaoOficioDelegacia(pendenciaDt, usuarioDt)){
                	// Calculo j� � finalizado
                	status = PendenciaStatusDt.ID_CUMPRIDA;
                    pendenciaDt.setDataVisto(df.format(new Date()));
                	
                    ProcessoNe processoNe = new ProcessoNe();
                	pendenciaDt.setProcessoDt(processoNe.consultarId(pendenciaDt.getId_Processo(), obFabricaConexao));
                    
                	//Gerar movimenta��o 
                	movimentacaoNe.gerarMovimentacaoRespostOficioDelegacia(pendenciaDt.getId_Processo(), pendenciaDt.getProcessoDt().getProcessoNumeroCompleto(), pendenciaDt.getListaArquivos(), usuarioDt, "", obLogDt, obFabricaConexao);
                	
                	//Gerar pendencia Verificar 
                	pendenciaDt.setId_ServentiaProcesso(pendenciaDt.getProcessoDt().getId_Serventia());
                	this.gerarPendenciaVerificarRespostaDelegacia(pendenciaDt, usuarioDt, pendenciaDt.getListaArquivos(), obLogDt, obFabricaConexao);
                	
                }
                	
                if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equals(""))
                    obPersistencia.responder(pendenciaDt.getId(), usuarioDt.getId_UsuarioServentiaChefe(), status, pendenciaDt.getDataFim(), pendenciaDt.getDataVisto());
                else
                    obPersistencia.responder(pendenciaDt.getId(), usuarioDt.getId_UsuarioServentia(), status, pendenciaDt.getDataFim(), pendenciaDt.getDataVisto());

                // Se for resposta simples n�o deve gerar movimenta��o e nem pend�ncias
                if (this.gerarMovimentacaoPendencias(pendenciaDt)){ 
                	this.verificarRegrasResposta(pendenciaDt, usuarioDt, serventiaDt, comarcaDt, "", obLogDt, obFabricaConexao);
                	
                }
                
                if (pendenciaDt != null && pendenciaDt.getDataFim()!= null && pendenciaDt.getDataFim().length()>0
                		&& pendenciaDt.getDataVisto() != null && pendenciaDt.getDataVisto().length()>0){
                	obPersistencia.moverPendencia(pendenciaDt.getId());
                }
                
                obLog.salvar(obLogDt, obFabricaConexao);

                if (fabrica == null) obFabricaConexao.finalizarTransacao();
            
            } catch (Exception e) {
                if (fabrica == null) obFabricaConexao.cancelarTransacao();
                throw e;

            } finally {
                if (fabrica == null) obFabricaConexao.fecharConexao();
            }
        } else {
            throw new MensagemException("Pend�ncia j� respondida.");
        }
    }

    /**
     * Reponder uma pendencia utilizando uma transacao existente
     * 
     * @author asRocha
     * @param pendenciaDt
     *            pojo da pendencia
     * @param usuarioDt
     *            vo de usuario
     * @param status
     *            status com que sera fechada a pendencia
     * @param fabrica
     *            fabrica para continuar uma transacao
     * @throws Exception
     */
    public void concluirPendenciaCorreios(PendenciaDt pendenciaDt, UsuarioDt usuarioDt, int status, ServentiaDt serventiaDt, ComarcaDt comarcaDt, FabricaConexao obFabricaConexao) throws MensagemException, Exception {
        MovimentacaoNe movimentacaoNe = new MovimentacaoNe();

        //S� � possivel finalizar um verificar se o processo estiver com o assunto definido
        if (pendenciaDt.isVerificar()){
     	   if (!(new ProcessoNe()).isTemAssunto(pendenciaDt.getId_Processo())){
     		   throw new MensagemException("Processo Sem Assunto: Para finalizar a pend�ncia � necess�rio definir pelo menos um Assunto no Processo.");
     	   }             	 
        }
        
        // Se era uma pendencia de aguardando retorno
        List arqs = this.consultarArquivosRespostaNaoAssinados(pendenciaDt, true, obFabricaConexao);

        PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

        LogDt obLogDt = new LogDt("Pendencia", pendenciaDt.getId(), pendenciaDt.getId_UsuarioLog(), pendenciaDt.getIpComputadorLog(), String.valueOf(LogTipoDt.FinalizarPendencia), obDados.getPropriedades(), pendenciaDt.getPropriedades());

        // Configura objeto de processo
        ProcessoDt processoDt = new ProcessoDt();
        processoDt.setId_Processo(pendenciaDt.getId_Processo());
        processoDt.setProcessoNumero(pendenciaDt.getProcessoNumero());
        
        if (Funcoes.StringToInt(pendenciaDt.getId_PendenciaStatus()) == PendenciaStatusDt.ID_CUMPRIDA) {
            this.gerarMovimentacaoLida(movimentacaoNe, processoDt, pendenciaDt, usuarioDt, arqs, obLogDt, obFabricaConexao);
        } else if (Funcoes.StringToInt(pendenciaDt.getId_PendenciaStatus()) == PendenciaStatusDt.ID_NAO_CUMPRIDA) {
            this.gerarMovimentacaoNaoLida(movimentacaoNe, processoDt, pendenciaDt, usuarioDt, arqs, obLogDt, obFabricaConexao);
        } else {
        	 throw new MensagemException("Status n�o definido. IdPendenciaStatus: " + pendenciaDt.getId_PendenciaStatus() + ", IdPendencia: " + pendenciaDt.getId());
        }
        
        obPersistencia.fecharPendenciaEcarta(pendenciaDt);
        
        gerarPendenciaVerificarRetornoARCorreios(pendenciaDt.getId_Processo(), UsuarioServentiaDt.SistemaProjudi, pendenciaDt.getId_ServentiaCadastrador(), "", null, null, UsuarioDt.SistemaProjudi, pendenciaDt.getId_ProcessoParte(), "Servidor", obFabricaConexao, pendenciaDt.getId_ProcessoPrioridade());
        
        if (pendenciaDt != null && pendenciaDt.getDataFim()!= null && pendenciaDt.getDataFim().length()>0 && pendenciaDt.getDataVisto() != null && pendenciaDt.getDataVisto().length()>0){
        	obPersistencia.moverPendencia(pendenciaDt.getId());
        }
        
        obLog.salvar(obLogDt, obFabricaConexao);

    }
    
    /**
     * Finalizar uma pendencia
     * 
     * @author Ronneesley Moura Teles
     * @since 07/10/2008 13:45
     * @param PendenciaDt
     *            pendenciaDt, pojo da pendencia
     * @param UsuarioDt
     *            usuariodt, vo do usuario
     * @throws Exception
     */
    public void finalizar(PendenciaDt pendenciaDt, UsuarioDt usuarioDt)throws MensagemException, Exception {
        FabricaConexao obFabricaConexao = null;
        PendenciaPs obPersistencia = null;
        ProcessoNe processoNe = new ProcessoNe();
        if (!pendenciaDt.estaRespondida()) {
        	//S� � possivel finalizar um verificar se o processo estiver com o assunto definido
        	if (pendenciaDt.isVerificar()){
        		if (!processoNe.isTemAssunto(pendenciaDt.getId_Processo())){
        			throw new MensagemException("Processo Sem Assunto: Para finalizar a pend�ncia � necess�rio definir pelo menos um Assunto no Processo.");
        		}        		
        	}
            try {

            	obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            	obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            	obFabricaConexao.iniciarTransacao();
               
                LogDt obLogDt = new LogDt("Pendencia", pendenciaDt.getId(), pendenciaDt.getId_UsuarioLog(), pendenciaDt.getIpComputadorLog(), String.valueOf(LogTipoDt.FinalizarPendencia), obDados.getPropriedades(), pendenciaDt.getPropriedades());
                
                // e-Carta
                if (pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.VERIFICAR_ENDERECO_PARTE) {
                	retornarStatusAguardandoEnvioCorreios(pendenciaDt.getId_ProcessoParte(), obFabricaConexao);
                }

                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
                pendenciaDt.setDataFim(df.format(new Date()));
                pendenciaDt.setDataVisto(df.format(new Date()));
                	
                if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equals(""))
                    obPersistencia.responder(pendenciaDt.getId(), usuarioDt.getId_UsuarioServentiaChefe(), PendenciaStatusDt.ID_CUMPRIDA, pendenciaDt.getDataFim(),pendenciaDt.getDataVisto());
                else
                    obPersistencia.responder(pendenciaDt.getId(), usuarioDt.getId_UsuarioServentia(), PendenciaStatusDt.ID_CUMPRIDA, pendenciaDt.getDataFim(),pendenciaDt.getDataVisto());

                if (pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.VERIFICAR_DISTRIBUICAO ||
                	pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.CONFIRMAR_DISTRIBUICAO ||
                	pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.VERIFICAR_REDISTRIBUICAO){
                	
                	boolean geraPendenciaVerificarNovoProcesso = true;                	
                	if (pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.VERIFICAR_REDISTRIBUICAO) {
                		ProcessoDt processoDt = processoNe.consultarIdCompleto(pendenciaDt.getId_Processo(), obFabricaConexao);
                		if (processoDt != null && !processoDt.isSegundoGrau()) {
                			geraPendenciaVerificarNovoProcesso = false;
                		}
                	}
                	
                	if (geraPendenciaVerificarNovoProcesso) {
                		PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
                    	List arquivosVincular = pendenciaArquivoNe.consultarArquivosPendencia(pendenciaDt.getId());
                    	
                    	GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
						GuiaEmissaoDt guiaEmissaoDt = new GuiaEmissaoDt();
						guiaEmissaoDt = guiaEmissaoNe.consultarGuiaEmissaoInicialAguardandoDeferimento(pendenciaDt.getId_Processo(), obFabricaConexao);
                    	
						if (guiaEmissaoDt != null && guiaEmissaoDt.getId() != null && guiaEmissaoDt.getId().length()>0) {
							this.gerarPendenciaVerificarNovoProcessoPedidoAssistencia(pendenciaDt, usuarioDt, arquivosVincular, obLogDt, obFabricaConexao);
						} else {
							this.gerarPendenciaVerificarNovoProcesso(pendenciaDt, usuarioDt, arquivosVincular, obLogDt, obFabricaConexao);
						}
                	}                	
                }
                
                if (pendenciaDt != null && pendenciaDt.temDataFim() && pendenciaDt.temDataVisto()){
                	obPersistencia.moverPendencia(pendenciaDt.getId());
                }
                
                obLog.salvar(obLogDt, obFabricaConexao);

                obFabricaConexao.finalizarTransacao();
            } catch (Exception e) {
                obFabricaConexao.cancelarTransacao();
                throw e;

            } finally {
                obFabricaConexao.fecharConexao();
            }
        } else {
            throw new MensagemException("Pend�ncia j� finalizada.");
        }
    }
    
    /**
     * Finalizar uma pendencia
     * 
     * @author Leandro Bernardes
     * @since 26/04/2010 13:45
     * @param String
     *            id_Pendencia, id pendencia intima��o aguardando decurso de prazo
     
     * @throws Exception
     */
    public void finalizarPendenciaIntimacaoAguardandoParecer(String id_Pendencia) throws Exception {
        FabricaConexao obFabricaConexao = null;
        try {
            // Abre uma conexao
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            
            PendenciaDt pendenciaDt = obPersistencia.consultarId(id_Pendencia);
            if (pendenciaDt == null) throw new MensagemException("A pend�ncia " + id_Pendencia + " n�o foi encontrada.");
            
            obPersistencia.finalizarPendenciaIntimacaoAguardandoParecer(pendenciaDt.getId());
            
            //se possuir prazo a pendencia n�o deve ser movida para a tabela de finalizadas
            if (!pendenciaDt.temPrazo()){
            	obPersistencia.moverPendencia(pendenciaDt.getId());
            }

        } finally {
            obFabricaConexao.fecharConexao();
        }
    }
    
    /**
     * M�todo que consulta todas as pend�ncias de intima��o aguardando parecer a partir de certo per�odo
     * e, em seguida, finaliza as pend�ncias. M�todo usado pelo scheduling.
     * 
     * @throws Exception
     * @author hmgodinho
     */
    public void finalizarPendenciasIntimacaoAguardandoParecer() throws Exception {
        FabricaConexao obFabricaConexao = null;
        String idPendencia = "";
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            
            List listaPendencias = obPersistencia.consultarPendenciasIntimacaoAguardandoParecer();
            for (int i = 0; i < listaPendencias.size(); i++) {
            	idPendencia = (String)listaPendencias.get(i);
                obPersistencia.finalizarPendenciaIntimacaoAguardandoParecer(idPendencia);
                obPersistencia.moverPendencia(idPendencia);
			}
        } finally {
            obFabricaConexao.fecharConexao();
        }
    }
    
    /*
    public void marcarPendenciaIntimacaoAguardandoParecer(String id_Pendencia) throws Exception {
        try {
            // Abre uma conexao
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            obPersistencia.marcarPendenciaIntimacaoAguardandoParecer(id_Pendencia);

        } finally {
            obFabricaConexao.fecharConexao();
        }
    }*/
    
    /**
     * Gerar pendencia Aguardando parecer ou peticionamento para intima��o lida
     * 
     * @author leandro bernardes
     * @param PendenciaDt
     *            pendenciaDt, pojo da pendencia
     * @param UsuarioDt
     *            usuariodt, vo do usuario
     * @throws Exception
     */
    public void gerarAguardandoParecer(PendenciaDt pendenciaDt, UsuarioDt usuarioDt) throws Exception {
        FabricaConexao obFabricaConexao = null;
        try {
            // Abre uma conexao
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);            
            this.gerarPendenciaFilhaManifestacao(pendenciaDt, usuarioDt, obFabricaConexao);

        } finally {
            obFabricaConexao.fecharConexao();
        }
    }
    
    /**
     * Gerar pendencia pendencia pre-anlise de carta precatoria
     * 
     * @author leandro bernardes
     * @param PendenciaDt
     *            pendenciaDt, pojo da pendencia
     * @param UsuarioDt
     *            usuariodt, vo do usuario
     * @throws Exception
     */
    public PendenciaDt gerarPreAnalisePrecatoria(PendenciaDt pendenciaDt, UsuarioDt usuarioDt) throws Exception {
        FabricaConexao obFabricaConexao = null;
        PendenciaDt pendenciaRetorno;
        try {
            // Abre uma conexao
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);            
            pendenciaRetorno = this.gerarPendenciaFilhaPreAnaliseCartaPrecatoira(pendenciaDt, usuarioDt, obFabricaConexao);

        } finally {
            obFabricaConexao.fecharConexao();
        }
        return pendenciaRetorno;
    }

    /**
     * Finaliza pend�ncia pai ao reabrir pend�ncia
     * 
     * @author leandro bernardes
     * @param PendenciaDt
     *            pendenciaDt, pojo da pendencia
     * @param UsuarioDt
     *            usuariodt, vo do usuario
     * @param FabricaConexao
     *            fabrica, conex�o
     * @throws Exception
     */
    public void finalizarPendenciaReaberta(PendenciaDt pendenciaDt, UsuarioDt usuarioDt, FabricaConexao fabrica) throws Exception {
        FabricaConexao obFabricaConexao = null;
        try {
            // Abre uma conexao
            if (fabrica == null) {
                obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
                obFabricaConexao.iniciarTransacao();
            } else
                obFabricaConexao = fabrica;

            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

            obPersistencia.finalizar(pendenciaDt, usuarioDt);
            
            if (pendenciaDt != null && pendenciaDt.temDataFim() && pendenciaDt.temDataVisto()){
            	obPersistencia.moverPendencia(pendenciaDt.getId());
            }

            if (fabrica == null) obFabricaConexao.finalizarTransacao();
        } catch (Exception e) {
            if (fabrica == null) obFabricaConexao.cancelarTransacao();
            throw e;

        } finally {
            if (fabrica == null) obFabricaConexao.fecharConexao();
        }

    }

    /**
     * Finalizar uma pendencia
     * 
     * @author Leandro Bernardes
     * @since 13/08/2009
     * @param PendenciaDt
     *            pendenciaDt, pojo da pendencia
     * @param UsuarioDt
     *            usuariodt, vo do usuario
     * @param FabricaConexao
     *            fabrica, fabrica de conexao
     * @throws Exception
     */
    public void finalizar(PendenciaDt pendenciaDt, UsuarioDt usuarioDt, FabricaConexao fabrica) throws Exception {
        FabricaConexao obFabricaConexao = null;
        try {
            if (fabrica == null) {
                obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
                obFabricaConexao.iniciarTransacao();
            } else
                obFabricaConexao = fabrica;

            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

            obPersistencia.finalizar(pendenciaDt, usuarioDt);
            
            if (pendenciaDt != null && pendenciaDt.temDataFim() && pendenciaDt.temDataVisto()){
            	obPersistencia.moverPendencia(pendenciaDt.getId());
            }

            if (fabrica == null) obFabricaConexao.finalizarTransacao();
        } catch (Exception e) {
            if (fabrica == null) obFabricaConexao.cancelarTransacao();
            throw e;
        } finally {
            if (fabrica == null) obFabricaConexao.fecharConexao();
        }
    }
    
    /**
     * Finalizar uma publica��o.
     * 
     * @param PendenciaDt - dados da publica��o 
     * @param UsuarioDt - usu�rio da sess�o
     * @param FabricaConexao - fabrica, fabrica de conexao
     * @throws Exception
     * @author hmgodinho
     */
    public void finalizarPublicacao(PendenciaDt pendenciaDt, UsuarioDt usuarioDt, FabricaConexao fabrica) throws Exception {
        FabricaConexao obFabricaConexao = null;
        try {
            if (fabrica == null) {
                obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
                obFabricaConexao.iniciarTransacao();
            } else
                obFabricaConexao = fabrica;

            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

            obPersistencia.finalizar(pendenciaDt, usuarioDt);
           	obPersistencia.moverPendencia(pendenciaDt.getId());

            if (fabrica == null) obFabricaConexao.finalizarTransacao();
        } catch (Exception e) {
            if (fabrica == null) obFabricaConexao.cancelarTransacao();
            throw e;
        } finally {
            if (fabrica == null) obFabricaConexao.fecharConexao();
        }
    }

    /**
     * Finalizar uma pendencia com prazo decorrido
     * 
     * @author Leandro Bernardes
     * @since 05/08/2009
     * @param PendenciaDt
     *            pendenciaDt, pojo da pendencia
     * @param UsuarioDt
     *            usuariodt, vo do usuario
     * @throws Exception
     */
    public void finalizarPendenciaPrazoDecorrido(PendenciaDt pendenciaDt, UsuarioDt usuarioDt) throws Exception {
        FabricaConexao obFabricaConexao = null;
        try {
            // Abre uma conexao
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

            obPersistencia.finalizarPrazoDecorrido(pendenciaDt, usuarioDt);
            
            if (pendenciaDt != null && pendenciaDt.temDataFim() && pendenciaDt.temDataVisto()){
            	obPersistencia.moverPendencia(pendenciaDt.getId());
            }
            
        } finally {
            obFabricaConexao.fecharConexao();
        }
    }

    /**
     * Verifica se a pendencia esta finalizada
     * 
     * @author Ronneesley Moura Teles since 07/10/2008 15:37
     * @param PendenciaDt
     *            pendenciaDt, pojo da pendencia
     * @return boolean
     * @throws Exception
     */
    public boolean verificaFinalizada(PendenciaDt pendenciaDt) throws Exception {
        if (pendenciaDt.getDataFim() != null && !pendenciaDt.getDataFim().trim().equals("")) 
        	return true;

        return false;
    }

    /**
     * Verifica se o usuario corrente pode finalizar a pendencia 20/11/2008 -
     * Mudanca de nome para pode responder
     * 
     * @author Ronneesley Moura Teles
     * @since 07/10/2008 10:02
     * @param PendenciaDt
     *            pendenciaDt, objeto de pendencia
     * @param UsuarioNe
     *            usuarioNe, objeto de negocio do usuario
     * @return boolean
     * @throws Exception
     */
    public boolean podeResponder(PendenciaDt pendenciaDt, UsuarioNe usuarioNe) throws Exception {
        boolean boRetorno = true;

        // se for assistente n�o pode responder
        if (usuarioNe.getUsuarioDt().getId_UsuarioServentiaChefe() != null && !usuarioNe.getUsuarioDt().getId_UsuarioServentiaChefe().equalsIgnoreCase("")) 
        	boRetorno = false;

        return boRetorno;
    }

    /**
     * Reservar ultima pendencia de um serventia cargo
     * 
     * @author Ronneesley Moura Teles
     * @since 29/10/2008 14:15
     * @param String
     *            tipo, tipo da pendencia
     * @param String
     *            idServentiaCargo, id do usuario da serventia
     * @return PendenciaDt
     * @throws Exception
     */
    public PendenciaDt reservarUltimaServentiaCargo(String tipo, UsuarioNe usuarioNe) throws Exception {
        PendenciaDt pendenciaDt = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            pendenciaDt = pendenciaPs.reservarUltimaServentiaCargo(tipo, usuarioNe.getUsuarioDt().getId_ServentiaCargo());
            if( pendenciaDt != null && pendenciaDt.getId() != null ) {
            	pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
            }

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendenciaDt;
    }

    /**
     * Reservar ultima pendencia da serventia
     * 
     * @author Ronneesley Moura Teles
     * @since 29/10/2008 14:17
     * @param String
     *            tipo, tipo da pendencia
     * @param String
     *            idServentia, id da serventia
     * @return PendenciaDt
     * @throws Exception
     */
    public PendenciaDt reservarUltimaServentia(String tipo, UsuarioNe usuarioNe) throws Exception {
        PendenciaDt pendenciaDt = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            
            if (usuarioNe != null && usuarioNe.getUsuarioDt() != null && usuarioNe.getUsuarioDt().getGrupoCodigoToInt() == GrupoDt.DISTRIBUIDOR_CAMARA){
            	pendenciaDt = pendenciaPs.reservarUltimaServentiaDistribuidorCamara(tipo, usuarioNe.getUsuarioDt().getId_Serventia());
            } else {
            	pendenciaDt = pendenciaPs.reservarUltimaServentia(tipo, usuarioNe.getUsuarioDt().getId_Serventia());
            }
            
            if (pendenciaDt == null) 
            	throw new MensagemException("N�o foi possivel reservar a pend�ncia, pois outro usu�rio j� havia reservado. Clique no menu 'P�gina Inicial' para atualizar com as pend�ncias dispon�veis.");
            
            pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
            
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendenciaDt;
    }
    
    public PendenciaDt reservarUltimaPreAnalisadaServentia(String tipo, UsuarioNe usuarioNe) throws Exception {
        PendenciaDt pendenciaDt = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            pendenciaDt = pendenciaPs.reservarUltimaPreAnalisadaServentia(tipo, usuarioNe.getUsuarioDt().getId_Serventia());
            if (pendenciaDt == null) throw new MensagemException("N�o foi possivel reservar a pend�ncia, pois outro usu�rio j� havia reservado. Clique no menu 'P�gina Inicial' para atualizar com as pend�ncias dispon�veis.");
            
            pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
            
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendenciaDt;
    }

    /**
     * Reserva a ultima pendencia para um tipo de serventia
     * 
     * @author Ronneesley Moura Teles
     * @since 29/10/2008 14 :15
     * @param String
     *            tipo, tipo de pendencia
     * @param String
     *            idServentiaTipo, tipo de serventia
     * @return PendenciaDt
     * @throws Exception
     */
    public PendenciaDt reservarUltimaServentiaTipo(String tipo, UsuarioNe usuarioNe) throws Exception {
        PendenciaDt pendenciaDt = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            pendenciaDt = pendenciaPs.reservarUltimaServentiaTipo(tipo, usuarioNe.getUsuarioDt().getId_ServentiaTipo());
            pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendenciaDt;
    }

    /**
     * Consulta as pendencias do tipo conclus�o que foram finalizadas
     * 
     * @param id_ServentiaCargo, serventiaCargo respons�vel pelas pend�ncias
     * @param numeroProcesso, filtro n�mero do processo
     * @param dataInicial, data inicial para consulta
     * @param dataFinal, data final para consulta
     * 
     * @author msapaula
     */
    public List consultarConclusoesFinalizadas(String id_ServentiaCargo, String numeroProcesso, String dataInicial, String dataFinal) throws Exception {
        List pendencias = null;
        String digitoVerificador = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

            // Divide n�mero de processo do d�gito verificador
            String[] stTemp = numeroProcesso.split("\\.");
            if (stTemp.length >= 1)
                numeroProcesso = stTemp[0];
            else
                numeroProcesso = "";
            if (stTemp.length >= 2) digitoVerificador = stTemp[1];

            pendencias = obPersistencia.consultarConclusoesFinalizadas(id_ServentiaCargo, numeroProcesso, digitoVerificador, dataInicial, dataFinal);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }

    /**
     * M�todo que verifica se h� pend�ncias do tipo conclus�o em aberto para o
     * processo passado. Ser� utilizado em verifica��es.
     * 
     * @param id_Processo,
     *            identifica��o de processo
     * @param fabricaConexao,
     *            conex�o ativa
     * 
     * @author msapaula
     */
    public boolean verificaConclusoesAbertas(String id_Processo, FabricaConexao fabricaConexao) throws Exception {
        boolean boRetorno = false;
        FabricaConexao obFabricaConexao = null;
        try {
            if (fabricaConexao == null)
                obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            else
                obFabricaConexao = fabricaConexao;
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

            List conclusoesPendentes = obPersistencia.consultarConclusoesAbertas(id_Processo);

            if (conclusoesPendentes != null && conclusoesPendentes.size() > 0) boRetorno = true;
        } finally {
            if (fabricaConexao == null) obFabricaConexao.fecharConexao();
        }
        return boRetorno;
    }

    /**
     * M�todo que verifica se h� pend�ncias do tipo conclus�o em aberto para o
     * processo passado ignorando uma pend�ncia espec�fica. Ser� utilizado em verifica��es
     * nos casos em que se fizer necess�rio ignorar a pend�ncia que justamente est� sendo fechada.
     * Ex.: No caso do encaminhamento de processos, ser� necess�rio fechar uma conclus�o j� encaminhando
     * o processo e a l�gica de verifica��o ir� impedir � menos que se ignore a pend�ncia em quest�o.
     * 
     * @param id_Processo,
     *            identifica��o de processo
     * @param fabricaConexao,
     *            conex�o ativa
     * @param idPendenciaIgnorar
     * 			  id da pend�ncia que n�o ser� levada em conta
     * 
     * @author hrrosa
     */
    public boolean verificaConclusoesAbertasComExcecao(String id_Processo, String idPendenciaIgnorar, FabricaConexao fabricaConexao) throws Exception {
        boolean boRetorno = false;
        FabricaConexao obFabricaConexao = null;
        int qtdPendenciasIgnorar = 0;
        String[] conclusao = null;
        
        try {
            if (fabricaConexao == null)
                obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            else
                obFabricaConexao = fabricaConexao;
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

            List conclusoesPendentes = obPersistencia.consultarConclusoesAbertas(id_Processo);
            
            //Contabiliza quantas ocorr�ncias na lista de conclus�es pendentes dever�o
            //ser ignoradas na verifica��o por ter o mesmo idPendenciaIgnorar que foi especificado.
    		if(idPendenciaIgnorar != null && conclusoesPendentes != null && conclusoesPendentes.size() > 0){
    			Iterator iteratorConclusoes = conclusoesPendentes.iterator();
    			while (iteratorConclusoes.hasNext()) {
    				conclusao = (String[]) iteratorConclusoes.next();
    				if (conclusao != null) {
    					if(idPendenciaIgnorar.equals(conclusao[0])){
    						qtdPendenciasIgnorar++;
    					}		    	
    				}    	
    			}			   					
    		}
    		
            //Verifica a quantidade de conclus�es pendentes abertas levando em considera��o as que devem ser ignoradas
            // na verifica��o.
            if (conclusoesPendentes != null && (conclusoesPendentes.size()-qtdPendenciasIgnorar) > 0) boRetorno = true;
        } finally {
            if (fabricaConexao == null) obFabricaConexao.fecharConexao();
        }
        return boRetorno;
    }
    
    /**
     * Consultar se h� conclus�es em aberto em um processo
     * 
     * @param id_Processo,
     *            identifica��o do processo
     * 
     * @return List, lista de vetor contendo o tipo de conclus�o e data de in�cio
     * @author msapaula
     */
    public List consultarConclusoesPendentesProcessoPublico(String id_Processo, boolean ehConsultaPublica) throws Exception {
        List conclusoesPendentes = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao((ehConsultaPublica ? FabricaConexao.PERSISTENCIA : FabricaConexao.PERSISTENCIA));
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

            conclusoesPendentes = obPersistencia.consultarConclusoesAbertasPublico(id_Processo);
        } finally {
            obFabricaConexao.fecharConexao();
        }
        return conclusoesPendentes;
    }
    
    /**
     * Consultar as Dez conclus�es mais antingas em aberto para um determinado Serventia Cargo
     * 
     * @param id_UsuarioServentiaCargoAtual,
     *            identifica��o do Cargo
     * 
     * @return List PendenciaDt
     * 
     * @author lsbernardes
     */
    public List consultarConclusoesMaisAntigasMagistado(String id_UsuarioServentiaCargoAtual, String quantidade, FabricaConexao obFabricaConexao) throws Exception {
    	List conclusoesPendentes = null;
    	PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
        conclusoesPendentes = obPersistencia.consultarConclusoesMaisAntigasMagistado(id_UsuarioServentiaCargoAtual, quantidade); 
        
        return conclusoesPendentes;
    }
    
    /**
     * Consultar se h� conclus�es em aberto em um processo
     * 
     * @param id_Processo,
     *            identifica��o do processo
     * 
     * @return String[], vetor contendo o tipo de conclus�o e data de in�cio
     * @author lsbernardes
     */
    public List consultarConclusoesPendentesProcessoHash(String id_Processo, UsuarioNe usuarioSessao) throws Exception {
    	List conclusoesPendentes = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

            conclusoesPendentes = obPersistencia.consultarConclusoesAbertasHash(id_Processo, usuarioSessao );
        } finally {
            obFabricaConexao.fecharConexao();
        }
        return conclusoesPendentes;
    }

    /**
     * Cria uma pendencia para verificar peti��o
     * 
     * @author msapaula
     * @since 12/01/2009 14:05
     * @param processoDt,
     *            bean do processo
     * @param String
     *            id_UsuarioServentia, id do usuario cadastrador
     * @param id_Movimentacao,
     *            id da movimenta��o vinculada
     * @param String
     *            id_ServentiaResponsavel, id da serventia responsavel
     * @param prioridade,
     *            prioridade da pend�ncia
     * @param arquivosVincular,
     *            arquivos a serem vinculados com pend�ncia
     * @param logDt,
     *            objeto com dados de log
     * @param obFabricaConexao,
     *            fabrica de conexao para continar uma transa��o existente
     * @return void
     */
    public void gerarPendenciaVerificarPeticao(ProcessoDt processoDt, String id_UsuarioServentia, String id_Movimentacao, String id_ServentiaResponsavel, String prioridade, List arquivosVincular, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
        this.gerarPendenciaProcesso(processoDt.getId(), arquivosVincular, PendenciaTipoDt.VERIFICAR_PETICAO, id_Movimentacao, id_UsuarioServentia, "", "", "", PendenciaStatusDt.ID_EM_ANDAMENTO, "", id_ServentiaResponsavel, "", "", prioridade, null, logDt, obFabricaConexao);
    }
    
    /**
     * Cria uma pendencia para verificar parecer
     * 
     * @author lsbernardes
     * @since 09/08/2010
     * @param processoDt,
     *            bean do processo
     * @param String
     *            id_UsuarioServentia, id do usuario cadastrador
     * @param id_Movimentacao,
     *            id da movimenta��o vinculada
     * @param String
     *            id_ServentiaResponsavel, id da serventia responsavel
     * @param prioridade,
     *            prioridade da pend�ncia
     * @param arquivosVincular,
     *            arquivos a serem vinculados com pend�ncia
     * @param logDt,
     *            objeto com dados de log
     * @param obFabricaConexao,
     *            fabrica de conexao para continar uma transa��o existente
     * @return void
     */
    public void gerarPendenciaVerificarParecer(ProcessoDt processoDt, String id_UsuarioServentia, String id_Movimentacao, String id_ServentiaResponsavel, String prioridade, List arquivosVincular, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
        this.gerarPendenciaProcesso(processoDt.getId(), arquivosVincular, PendenciaTipoDt.VERIFICAR_PARECER, id_Movimentacao, id_UsuarioServentia, "", "", "", PendenciaStatusDt.ID_EM_ANDAMENTO, "", id_ServentiaResponsavel, "", "", prioridade, null, logDt, obFabricaConexao);
    }
    
    public void gerarPendenciaVerificarParecerProcessoSigiloso(ProcessoDt processoDt, String id_UsuarioServentia, String id_Movimentacao, String id_ServentiaCargoResponsavel, String prioridade, List arquivosVincular, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
        this.gerarPendenciaProcesso(processoDt.getId(), arquivosVincular, PendenciaTipoDt.VERIFICAR_PARECER, id_Movimentacao, id_UsuarioServentia, "", "", "", PendenciaStatusDt.ID_EM_ANDAMENTO, "", "", "", id_ServentiaCargoResponsavel, prioridade, null, logDt, obFabricaConexao);
    }

    /**
     * Cria uma pendencia para verificar o processo ap�s um ato do juiz
     * (decis�o, despacho, senten�a)
     * 
     * @author msapaula
     * @since 12/01/2009 12:11
     * @param processoDt,
     *            bean do processo
     * @param id_UsuarioServentia,
     *            id do usuario cadastrador
     * @param id_ServentiaResponsavel,
     *            id da serventia responsavel
     * @param id_Movimentacao,
     *            movimenta��o vinculada a pend�ncia
     * @param arquivosVincular,
     *            arquivos a serem vinculados com pend�ncia
     * @param pendenciaPai,
     *            pend�ncia pai
     * @param logDt,
     *            objeto com dados do log
     * @param obFabricaConexao,
     *            fabrica de conexao para continar uma transa��cao existente
     * @return void
     */
    public void gerarPendenciaVerificarProcesso(String id_processo, String id_UsuarioServentia, String id_ServentiaResponsavel, String id_Movimentacao, List arquivosVincular, PendenciaDt pendenciaPai, LogDt logDt, FabricaConexao obFabricaConexao, String processo_prioridade) throws Exception {

        this.gerarPendenciaProcesso(id_processo, arquivosVincular, PendenciaTipoDt.VERIFICAR_PROCESSO, id_Movimentacao, id_UsuarioServentia, "", "", "", PendenciaStatusDt.ID_EM_ANDAMENTO, "", id_ServentiaResponsavel, "", "", processo_prioridade, pendenciaPai, logDt, obFabricaConexao);

    }
    
    public void gerarPendenciaConclusoRelator(String id_processo, String id_UsuarioServentia, String id_ServentiaResponsavel, String idServentiaCargoResponsavel, String id_Movimentacao, List arquivosVincular, PendenciaDt pendenciaPai, LogDt logDt, FabricaConexao obFabricaConexao, String processo_prioridade) throws Exception {

        this.gerarPendenciaProcesso(id_processo, arquivosVincular, PendenciaTipoDt.CONCLUSO_RELATOR, id_Movimentacao, id_UsuarioServentia, "", "", "", PendenciaStatusDt.ID_EM_ANDAMENTO, "", id_ServentiaResponsavel, "", idServentiaCargoResponsavel, processo_prioridade, pendenciaPai, logDt, obFabricaConexao);

    }
    
    /**
     * Cria uma pendencia para verificar processo h�brido (migra��o)
     * 
     * @param processoDt,
     *            bean do processo
     * @param id_UsuarioServentia,
     *            id do usuario cadastrador
     * @param id_ServentiaResponsavel,
     *            id da serventia responsavel
     * @param id_Movimentacao,
     *            movimenta��o vinculada a pend�ncia
     * @param arquivosVincular,
     *            arquivos a serem vinculados com pend�ncia
     * @param pendenciaPai,
     *            pend�ncia pai
     * @param logDt,
     *            objeto com dados do log
     * @param obFabricaConexao,
     *            fabrica de conexao para continar uma transa��cao existente
     * @return void
     */
    public void gerarPendenciaVerificarProcessoHibrido(String id_processo, String id_UsuarioServentia, String id_ServentiaResponsavel, String id_Movimentacao, List arquivosVincular, PendenciaDt pendenciaPai, LogDt logDt, FabricaConexao obFabricaConexao, String processo_prioridade) throws Exception {

        this.gerarPendenciaProcesso(id_processo, arquivosVincular, PendenciaTipoDt.VERIFICAR_PROCESSO_HIBRIDO, id_Movimentacao, id_UsuarioServentia, "", "", "", PendenciaStatusDt.ID_EM_ANDAMENTO, "", id_ServentiaResponsavel, "", "", processo_prioridade, pendenciaPai, logDt, obFabricaConexao);

    }
    
    /**
     * Cria uma pendencia para verificar o redistribui��o de processo
     * 
     * @author lsbernardes
     * @param processoDt,
     *            bean do processo
     * @param id_UsuarioServentia,
     *            id do usuario cadastrador
     * @param id_ServentiaResponsavel,
     *            id da serventia responsavel
     * @param id_Movimentacao,
     *            movimenta��o vinculada a pend�ncia
     * @param arquivosVincular,
     *            arquivos a serem vinculados com pend�ncia
     * @param pendenciaPai,
     *            pend�ncia pai
     * @param logDt,
     *            objeto com dados do log
     * @param obFabricaConexao,
     *            fabrica de conexao para continar uma transa��cao existente
     * @return void
     */
    public void gerarPendenciaVerificarRedistribuicaoProcesso(String id_processo, String id_UsuarioServentia, String id_ServentiaResponsavel, String id_Movimentacao, List arquivosVincular, PendenciaDt pendenciaPai, LogDt logDt, FabricaConexao obFabricaConexao, String processo_prioridade) throws Exception {

        this.gerarPendenciaProcesso(id_processo, arquivosVincular, PendenciaTipoDt.VERIFICAR_REDISTRIBUICAO, id_Movimentacao, id_UsuarioServentia, "", "", "", PendenciaStatusDt.ID_EM_ANDAMENTO, "", id_ServentiaResponsavel, "", "", processo_prioridade, pendenciaPai, logDt, obFabricaConexao);

    }
    
    /**
    * Cria uma pendencia para verificar o processo ap�s um ato do juiz
    * (decis�o, despacho, senten�a)
    * 
    * @author jrcorrea
    */
    
    public void gerarPendenciaVerificarProcesso(String id_processo, String id_UsuarioServentia, String id_ServentiaResponsavel, String id_Movimentacao, List arquivosVincular, PendenciaDt pendenciaPai, String id_usuario, String ipComputador, FabricaConexao obFabricaConexao, String processoPrioridade) throws Exception {

        this.gerarPendenciaProcesso(id_processo, arquivosVincular, PendenciaTipoDt.VERIFICAR_PROCESSO, id_Movimentacao, id_UsuarioServentia, "", "", "", PendenciaStatusDt.ID_EM_ANDAMENTO, "", id_ServentiaResponsavel, "", "", processoPrioridade, pendenciaPai, id_usuario,ipComputador, obFabricaConexao);

    }

    /**
     * Cria uma pendencia para verificar guia
     * 
     * @author lsbernardes
     * @param processoDt,
     *            bean do processo
     * @param id_UsuarioServentia,
     *            id do usuario cadastrador
     * @param id_ServentiaResponsavel,
     *            id da serventia responsavel
     * @param id_Movimentacao,
     *            movimenta��o vinculada a pend�ncia
     * @param arquivosVincular,
     *            arquivos a serem vinculados com pend�ncia
     * @param pendenciaPai,
     *            pend�ncia pai
     * @param logDt,
     *            objeto com dados do log
     * @param obFabricaConexao,
     *            fabrica de conexao para continar uma transa��cao existente
     * @return void
     */
    public void gerarPendenciaVerificarGuia(ProcessoDt processoDt, String id_UsuarioServentia, String id_ServentiaResponsavel, String id_Movimentacao, List arquivosVincular, PendenciaDt pendenciaPai, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {

        this.gerarPendenciaProcesso(processoDt.getId(), arquivosVincular, PendenciaTipoDt.VERIFICAR_GUIA, id_Movimentacao, id_UsuarioServentia, "", "", "", PendenciaStatusDt.ID_EM_ANDAMENTO, "", id_ServentiaResponsavel, "", "", processoDt.getId_ProcessoPrioridade(), pendenciaPai, logDt, obFabricaConexao);

    }
    
    /**
     * Cria uma pendencia para verificar guia paga
     * 
     * @author lsbernardes
     * @param processoDt,
     *            bean do processo
     * @param id_UsuarioServentia,
     *            id do usuario cadastrador
     * @param id_ServentiaResponsavel,
     *            id da serventia responsavel
     * @param id_Movimentacao,
     *            movimenta��o vinculada a pend�ncia
     * @param arquivosVincular,
     *            arquivos a serem vinculados com pend�ncia
     * @param pendenciaPai,
     *            pend�ncia pai
     * @param logDt,
     *            objeto com dados do log
     * @param obFabricaConexao,
     *            fabrica de conexao para continar uma transa��cao existente
     * @return void
     */
    public void gerarPendenciaVerificarGuiaPaga(ProcessoDt processoDt, String id_UsuarioServentia, String id_ServentiaResponsavel, String id_Movimentacao, List arquivosVincular, PendenciaDt pendenciaPai, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {

        this.gerarPendenciaProcesso(processoDt.getId(), arquivosVincular, PendenciaTipoDt.VERIFICAR_GUIA_PAGA, id_Movimentacao, id_UsuarioServentia, "", "", "", PendenciaStatusDt.ID_EM_ANDAMENTO, "", id_ServentiaResponsavel, "", "", processoDt.getId_ProcessoPrioridade(), pendenciaPai, logDt, obFabricaConexao);

    }
    
    /**
     * Provimento 34/2019 Provimento de Parcelamento e Desconto de guias.
     * 
     * @param ProcessoDt processoDt
     * @param String id_UsuarioServentia
     * @param String id_ServentiaResponsavel
     * @param String id_Movimentacao
     * @param List arquivosVincular
     * @param LogDt logDt
     * @param FabricaConexao obFabricaConexao
     * @throws Exception
     */
    public void gerarPendenciaVerificarGuiaVencida(ProcessoDt processoDt, String id_UsuarioServentia, String id_ServentiaResponsavel, String id_Movimentacao, List arquivosVincular, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
    	
        this.gerarPendenciaProcesso(processoDt.getId(), arquivosVincular, PendenciaTipoDt.VERIFICAR_GUIA_VENCIDA, id_Movimentacao, id_UsuarioServentia, "", "", "", PendenciaStatusDt.ID_EM_ANDAMENTO, "", id_ServentiaResponsavel, "", "", processoDt.getId_ProcessoPrioridade(), null, logDt, obFabricaConexao);
        
    }
    
    /**
     * Cria uma pendencia para verificar C�lculo
     * 
     * @author lsbernardes
     * @param processoDt,
     *            bean do processo
     * @param id_UsuarioServentia,
     *            id do usuario cadastrador
     * @param id_ServentiaResponsavel,
     *            id da serventia responsavel
     * @param id_Movimentacao,
     *            movimenta��o vinculada a pend�ncia
     * @param arquivosVincular,
     *            arquivos a serem vinculados com pend�ncia
     * @param pendenciaPai,
     *            pend�ncia pai
     * @param logDt,
     *            objeto com dados do log
     * @param obFabricaConexao,
     *            fabrica de conexao para continar uma transa��cao existente
     * @return void
     */
    public boolean gerarPendenciaVerificarCalculo( PendenciaDt pendenciaPai , UsuarioDt usuarioDt, List arquivosVincular, LogDt logDt, FabricaConexao fabConexao) throws Exception {
    	
    	// Configura a pendencia para ser persistida
        PendenciaDt pendencia = new PendenciaDt();
        pendencia.setId_PendenciaPai(pendenciaPai.getId());
        pendencia.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_EM_ANDAMENTO));
        pendencia.setPendenciaTipoCodigo(String.valueOf(PendenciaTipoDt.VERIFICAR_CALCULO));
        pendencia.setId_Movimentacao(pendenciaPai.getId_Movimentacao());
        pendencia.setId_Processo(pendenciaPai.getId_Processo());
        // Todas as pend�ncias de processo devem ter DataVisto setada
        pendencia.setDataVisto(Funcoes.DataHora(new Date()));
        pendencia.setId_UsuarioCadastrador(usuarioDt.getId_UsuarioServentia());
        pendencia.setId_UsuarioLog(logDt.getId_Usuario());
        pendencia.setIpComputadorLog(logDt.getIpComputador());

        PendenciaResponsavelDt responsavel = new PendenciaResponsavelDt();
        responsavel.setId_Serventia(pendenciaPai.getId_ServentiaProcesso());
        pendencia.addResponsavel(responsavel);

        // Gera a pendencia
        return this.gerarPendencia(pendencia, arquivosVincular, false, fabConexao);

    }
    
    /**
     * Cria uma pendencia para verificar Resposta C�mara de Sa�de
     * 
     * @author lsbernardes
     * @param processoDt,
     *            bean do processo
     * @param id_UsuarioServentia,
     *            id do usuario cadastrador
     * @param id_ServentiaResponsavel,
     *            id da serventia responsavel
     * @param id_Movimentacao,
     *            movimenta��o vinculada a pend�ncia
     * @param arquivosVincular,
     *            arquivos a serem vinculados com pend�ncia
     * @param pendenciaPai,
     *            pend�ncia pai
     * @param logDt,
     *            objeto com dados do log
     * @param obFabricaConexao,
     *            fabrica de conexao para continar uma transa��cao existente
     * @return void
     */
    public boolean gerarPendenciaVericarRespostaCamaraSaude( PendenciaDt pendenciaPai , UsuarioDt usuarioDt, List arquivosVincular, LogDt logDt, FabricaConexao fabConexao) throws Exception {
    	
    	// Configura a pendencia para ser persistida
        PendenciaDt pendencia = new PendenciaDt();
        pendencia.setId_PendenciaPai(pendenciaPai.getId());
        pendencia.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_EM_ANDAMENTO));
        pendencia.setPendenciaTipoCodigo(String.valueOf(PendenciaTipoDt.VERIFICAR_RESPOSTA_CAMARA_SAUDE));
        pendencia.setId_Movimentacao(pendenciaPai.getId_Movimentacao());
        pendencia.setId_Processo(pendenciaPai.getId_Processo());
        // Todas as pend�ncias de processo devem ter DataVisto setada
        pendencia.setDataVisto(Funcoes.DataHora(new Date()));
        pendencia.setId_UsuarioCadastrador(usuarioDt.getId_UsuarioServentia());
        pendencia.setId_UsuarioLog(logDt.getId_Usuario());
        pendencia.setIpComputadorLog(logDt.getIpComputador());

        PendenciaResponsavelDt responsavel = new PendenciaResponsavelDt();
        responsavel.setId_Serventia(pendenciaPai.getId_ServentiaProcesso());
        pendencia.addResponsavel(responsavel);

        // Gera a pendencia
        return this.gerarPendencia(pendencia, arquivosVincular, false, fabConexao);

    }
    
    /**
     * Cria uma pendencia para verificar Resposta de Pedido de Laudo
     * 
     * @author lsbernardes
     * @param processoDt,
     *            bean do processo
     * @param id_UsuarioServentia,
     *            id do usuario cadastrador
     * @param id_ServentiaResponsavel,
     *            id da serventia responsavel
     * @param id_Movimentacao,
     *            movimenta��o vinculada a pend�ncia
     * @param arquivosVincular,
     *            arquivos a serem vinculados com pend�ncia
     * @param pendenciaPai,
     *            pend�ncia pai
     * @param logDt,
     *            objeto com dados do log
     * @param obFabricaConexao,
     *            fabrica de conexao para continar uma transa��cao existente
     * @return void
     */
    public boolean gerarPendenciaVericarRespostaPedidoLaudoRelatorio( PendenciaDt pendenciaPai , UsuarioDt usuarioDt, List arquivosVincular, LogDt logDt, FabricaConexao fabConexao) throws Exception {
    	
    	// Configura a pendencia para ser persistida
        PendenciaDt pendencia = new PendenciaDt();
        pendencia.setId_PendenciaPai(pendenciaPai.getId());
        pendencia.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_EM_ANDAMENTO));
        pendencia.setPendenciaTipoCodigo(String.valueOf(PendenciaTipoDt.VERIFICAR_RESPOSTA_PEDIDO_LAUDO_RELATORIO));
        pendencia.setId_Movimentacao(pendenciaPai.getId_Movimentacao());
        pendencia.setId_Processo(pendenciaPai.getId_Processo());
        // Todas as pend�ncias de processo devem ter DataVisto setada
        pendencia.setDataVisto(Funcoes.DataHora(new Date()));
        pendencia.setId_UsuarioCadastrador(usuarioDt.getId_UsuarioServentia());
        pendencia.setId_UsuarioLog(logDt.getId_Usuario());
        pendencia.setIpComputadorLog(logDt.getIpComputador());

        PendenciaResponsavelDt responsavel = new PendenciaResponsavelDt();
        responsavel.setId_Serventia(pendenciaPai.getId_ServentiaProcesso());
        pendencia.addResponsavel(responsavel);

        // Gera a pendencia
        return this.gerarPendencia(pendencia, arquivosVincular, false, fabConexao);

    }
    
    /**
     * Cria uma pendencia para verificar Resposta de Pedido de Laudo
     * 
     * @author lsbernardes
     * @param processoDt,
     *            bean do processo
     * @param id_UsuarioServentia,
     *            id do usuario cadastrador
     * @param id_ServentiaResponsavel,
     *            id da serventia responsavel
     * @param id_Movimentacao,
     *            movimenta��o vinculada a pend�ncia
     * @param arquivosVincular,
     *            arquivos a serem vinculados com pend�ncia
     * @param pendenciaPai,
     *            pend�ncia pai
     * @param logDt,
     *            objeto com dados do log
     * @param obFabricaConexao,
     *            fabrica de conexao para continar uma transa��cao existente
     * @return void
     */
    public boolean gerarPendenciaVericarRespostaPedidoCENOPES( PendenciaDt pendenciaPai , UsuarioDt usuarioDt, List arquivosVincular, LogDt logDt, FabricaConexao fabConexao) throws Exception {
    	
    	// Configura a pendencia para ser persistida
        PendenciaDt pendencia = new PendenciaDt();
        pendencia.setId_PendenciaPai(pendenciaPai.getId());
        pendencia.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_EM_ANDAMENTO));
        pendencia.setPendenciaTipoCodigo(String.valueOf(PendenciaTipoDt.VERIFICAR_RESPOSTA_PEDIDO_CENOPES));
        pendencia.setId_Movimentacao(pendenciaPai.getId_Movimentacao());
        pendencia.setId_Processo(pendenciaPai.getId_Processo());
        // Todas as pend�ncias de processo devem ter DataVisto setada
        pendencia.setDataVisto(Funcoes.DataHora(new Date()));
        pendencia.setId_UsuarioCadastrador(usuarioDt.getId_UsuarioServentia());
        pendencia.setId_UsuarioLog(logDt.getId_Usuario());
        pendencia.setIpComputadorLog(logDt.getIpComputador());

        PendenciaResponsavelDt responsavel = new PendenciaResponsavelDt();
        responsavel.setId_Serventia(pendenciaPai.getId_ServentiaProcesso());
        pendencia.addResponsavel(responsavel);

        // Gera a pendencia
        return this.gerarPendencia(pendencia, arquivosVincular, false, fabConexao);

    }
    
    /**
     * Cria uma pendencia para verificar devolu��o precat�ria
     * 
     * @author lsbernardes
     * @param processoDt,
     *            bean do processo
     * @param id_UsuarioServentia,
     *            id do usuario cadastrador
     * @param id_ServentiaResponsavel,
     *            id da serventia responsavel
     * @param id_Movimentacao,
     *            movimenta��o vinculada a pend�ncia
     * @param arquivosVincular,
     *            arquivos a serem vinculados com pend�ncia
     * @param pendenciaPai,
     *            pend�ncia pai
     * @param logDt,
     *            objeto com dados do log
     * @param obFabricaConexao,
     *            fabrica de conexao para continar uma transa��cao existente
     * @return void
     */
    public boolean gerarPendenciaVerificarDevolucaoPrecatoria(MovimentacaoDt movimentacaoDt, UsuarioDt usuarioDt, String id_ServentiaProcesso, LogDt logDt, FabricaConexao fabConexao) throws Exception {
    	
    	// Configura a pendencia para ser persistida
        PendenciaDt pendencia = new PendenciaDt();
        pendencia.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_EM_ANDAMENTO));
        pendencia.setPendenciaTipoCodigo(String.valueOf(PendenciaTipoDt.VERIFICAR_DEVOLUCAO_PRECATORIA));
        pendencia.setId_Movimentacao(movimentacaoDt.getId());
        pendencia.setId_Processo(movimentacaoDt.getId_Processo());
        // Todas as pend�ncias de processo devem ter DataVisto setada
        pendencia.setDataVisto(Funcoes.DataHora(new Date()));
        pendencia.setId_UsuarioCadastrador(usuarioDt.getId_UsuarioServentia());
        pendencia.setId_UsuarioLog(logDt.getId_Usuario());
        pendencia.setIpComputadorLog(logDt.getIpComputador());

        PendenciaResponsavelDt responsavel = new PendenciaResponsavelDt();
        responsavel.setId_Serventia(id_ServentiaProcesso);
        pendencia.addResponsavel(responsavel);

        // Gera a pendencia
        return this.gerarPendencia(pendencia, null, false, fabConexao);

    }
    
    /**
     * Cria uma pendencia para verificar precat�ria
     * 
     * @author lsbernardes
     * @param processoDt,
     *            bean do processo
     * @param id_UsuarioServentia,
     *            id do usuario cadastrador
     * @param id_ServentiaResponsavel,
     *            id da serventia responsavel
     * @param id_Movimentacao,
     *            movimenta��o vinculada a pend�ncia
     * @param arquivosVincular,
     *            arquivos a serem vinculados com pend�ncia
     * @param pendenciaPai,
     *            pend�ncia pai
     * @param logDt,
     *            objeto com dados do log
     * @param obFabricaConexao,
     *            fabrica de conexao para continar uma transa��cao existente
     * @return void
     */
    public boolean gerarPendenciaVerificarPrecatoria(MovimentacaoDt movimentacaoDt, UsuarioDt usuarioDt, String id_ServentiaProcesso, LogDt logDt, FabricaConexao fabConexao) throws Exception {
    	
    	// Configura a pendencia para ser persistida
        PendenciaDt pendencia = new PendenciaDt();
        pendencia.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_EM_ANDAMENTO));
        pendencia.setPendenciaTipoCodigo(String.valueOf(PendenciaTipoDt.VERIFICAR_PRECATORIA));
        pendencia.setId_Movimentacao(movimentacaoDt.getId());
        pendencia.setId_Processo(movimentacaoDt.getId_Processo());
        // Todas as pend�ncias de processo devem ter DataVisto setada
        pendencia.setDataVisto(Funcoes.DataHora(new Date()));
        pendencia.setId_UsuarioCadastrador(usuarioDt.getId_UsuarioServentia());
        pendencia.setId_UsuarioLog(logDt.getId_Usuario());
        pendencia.setIpComputadorLog(logDt.getIpComputador());

        PendenciaResponsavelDt responsavel = new PendenciaResponsavelDt();
        responsavel.setId_Serventia(id_ServentiaProcesso);
        pendencia.addResponsavel(responsavel);

        // Gera a pendencia
        return this.gerarPendencia(pendencia, null, false, fabConexao);

    }
    
    /**
     * Cria uma pendencia para verificar of�cio de notifica��o
     * 
     * @author lsbernardes
     * @param processoDt,
     *            bean do processo
     * @param id_UsuarioServentia,
     *            id do usuario cadastrador
     * @param id_ServentiaResponsavel,
     *            id da serventia responsavel
     * @param id_Movimentacao,
     *            movimenta��o vinculada a pend�ncia
     * @param arquivosVincular,
     *            arquivos a serem vinculados com pend�ncia
     * @param pendenciaPai,
     *            pend�ncia pai
     * @param logDt,
     *            objeto com dados do log
     * @param obFabricaConexao,
     *            fabrica de conexao para continar uma transa��cao existente
     * @return void
     */
    public boolean gerarPendenciaVerificarOficioNotificacao(MovimentacaoDt movimentacaoDt, UsuarioDt usuarioDt, String id_ServentiaProcesso, LogDt logDt, FabricaConexao fabConexao) throws Exception {
    	
    	// Configura a pendencia para ser persistida
        PendenciaDt pendencia = new PendenciaDt();
        pendencia.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_EM_ANDAMENTO));
        pendencia.setPendenciaTipoCodigo(String.valueOf(PendenciaTipoDt.VERIFICAR_OFICIO_COMUNICATORIO));
        pendencia.setId_Movimentacao(movimentacaoDt.getId());
        pendencia.setId_Processo(movimentacaoDt.getId_Processo());
        // Todas as pend�ncias de processo devem ter DataVisto setada
        pendencia.setDataVisto(Funcoes.DataHora(new Date()));
        pendencia.setId_UsuarioCadastrador(usuarioDt.getId_UsuarioServentia());
        pendencia.setId_UsuarioLog(logDt.getId_Usuario());
        pendencia.setIpComputadorLog(logDt.getIpComputador());

        PendenciaResponsavelDt responsavel = new PendenciaResponsavelDt();
        responsavel.setId_Serventia(id_ServentiaProcesso);
        pendencia.addResponsavel(responsavel);

        // Gera a pendencia
        return this.gerarPendencia(pendencia, null, false, fabConexao);

    }
    
    /**
     * Verifica se h� pend�ncias em um processo e em uma determinada serventia, ou seja, qualquer pend�ncia em
     * aberto (at� mesmo as reservadas), ou pend�ncias finalizadas mas que ainda
     * n�o foram vistadas
     * 
     * @param id_Processo, identifica��o do processo
     * @param id_Serventia, identifica��o da serventia
     * @param fabConexao, conex�o ativa
     * 
     * @author msapaula
     * @since 02/03/2009 10:56
     */
    public boolean verificaPendenciasProcesso(String id_Processo, String id_Serventia, FabricaConexao fabConexao) throws Exception {
        boolean boRetorno = false;
        FabricaConexao obFabricaConexao = null;
        try {
            if (fabConexao == null)
                obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            else
                obFabricaConexao = fabConexao;
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            int qtde = obPersistencia.consultarQuantidadePendenciasProcesso(id_Processo, id_Serventia);

            if (qtde > 0) boRetorno = true;
        } finally {
            if (fabConexao == null) obFabricaConexao.fecharConexao();
        }

        return boRetorno;
    }
    
    /**
     *  Consulta pend�ncias em um processo e em uma determinada serventia, ou seja, qualquer pend�ncia em
     * aberto (at� mesmo as reservadas), ou pend�ncias finalizadas mas que ainda
     * n�o foram vistadas
     * 
     * @param id_Processo, identifica��o do processo
     * @param id_Serventia, identifica��o da serventia
     * @param fabConexao, conex�o ativa
     * 
     * @author lsbernardes
     */
    public List consultarPendenciasProcessoRedistribuicaoLote(String id_Processo, String id_Serventia, FabricaConexao fabConexao) throws Exception {
    	List pendenciasAbertas =  null;
        FabricaConexao obFabricaConexao = null;
        try {
            if (fabConexao == null)
                obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            else
                obFabricaConexao = fabConexao;
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            pendenciasAbertas = obPersistencia.consultarPendenciasProcessoRedistribuicaoLote(id_Processo, id_Serventia);

        } finally {
            if (fabConexao == null) obFabricaConexao.fecharConexao();
        }

        return pendenciasAbertas;
    }

    /**
     * Consulta a quantidade de pendencias reservadas por tipo
     * 
     * @param usuarioDt,
     *            dt de usuario
     * @param tipo,
     *            tipo da pendencia
     * 
     * @author msapaula
     */
    /* 
     * public int consultarQuantidadeServentiaReservadasPorTipo(UsuarioDt usuarioDt, String tipo) throws Exception {
        int qtd = 0;

        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            qtd = pendenciaPs.consultarQuantidadeServentiaReservadasPorTipo(usuarioDt, tipo);
        } finally {
            obFabricaConexao.fecharConexao();
        }
        return qtd;
    }
    */
    
    /** Consulta a lista de pendencias reservadas de uma determinada serventia 
    * 
    * @author Leandro Bernardes
    * @param usuarioDt
    *            usuario que deseja a quantidade
   
    * @return List lista de pendencias
    * @throws Exception
    */
    public List consultarPendenciasServentiaReservadas(UsuarioDt usuarioDt) throws Exception {
    	List pendencias = new ArrayList();
    	FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            pendencias = pendenciaPs.consultarPendenciasServentiaReservadas(usuarioDt);
        } finally {
            obFabricaConexao.fecharConexao();
        }
        return pendencias;
    }
    
    /** Consulta a lista de pendencias reservadas do distribuidor c�mara 
     * 
     * @author Leandro Bernardes
     * @param usuarioDt
     *            usuario que deseja a quantidade
    
     * @return List lista de pendencias
     * @throws Exception
     */
     public List consultarPendenciasServentiaReservadasDistribuidorCamara(UsuarioDt usuarioDt) throws Exception {
     	List pendencias = new ArrayList();
     	FabricaConexao obFabricaConexao = null;
         try {
             obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
             PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
             //pendenciaPs.setConexao(obFabricaConexao);
             pendencias = pendenciaPs.consultarPendenciasServentiaReservadasDistribuidorCamara(usuarioDt);
         } finally {
             obFabricaConexao.fecharConexao();
         }
         return pendencias;
     }

    /**
     * Consulta a quantidade de pendencias cargo serventia reservadas por tipo
     * 
     * @param usuarioDt,
     *            dt de usuario
     * @param tipo,
     *            tipo da pendencia
     * 
     * @author lsbernardes
     */
    /*public int consultarQuantidadeCargoServentiaReservadasPorTipo(UsuarioDt usuarioDt, String tipo) throws Exception {
        int qtd = 0;

        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            qtd = pendenciaPs.consultarQuantidadeCargoServentiaReservadasPorTipo(usuarioDt, tipo);
        } finally {
            obFabricaConexao.fecharConexao();
        }
        return qtd;
    }*/
    
    /** Consulta a lista de pendencias reservadas de um determinado cargo serventia 
    * 
    * @author Leandro Bernardes
    * @param usuarioDt
    *            usuario que deseja a quantidade
   
    * @return List lista de pendencias
    * @throws Exception
    */
    public List consultarPendenciasCargoServentiaReservadas(UsuarioDt usuarioDt) throws Exception {
    	List pendencias = new ArrayList();
    	FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            pendencias = pendenciaPs.consultarPendenciasCargoServentiaReservadas(usuarioDt);
        } finally {
            obFabricaConexao.fecharConexao();
        }
        return pendencias;
    }

    /**
     * Retorna as pend�ncias de mandados reservadas para os oficiais de justi�a.
     * 
     * @param usuarioDt
     * @return
     * @throws Exception
     */
    public List consultarPendenciasMandadosReservadosOficial(UsuarioDt usuarioDt) throws Exception {
    	List pendencias = new ArrayList();
    	FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            pendencias = pendenciaPs.consultarPendenciasMandadosReservadosOficial(usuarioDt);
        } finally {
            obFabricaConexao.fecharConexao();
        }
        return pendencias;
    }
    
    /**
     * Consulta a quantidade de pendencias serventia tipo reservadas por tipo
     * 
     * @param usuarioDt,
     *            dt de usuario
     * @param tipo,
     *            tipo da pendencia
     * 
     * @author lsbernardes
     */
    public int consultarQuantidadeServentiaTipoReservadasPorTipo(UsuarioDt usuarioDt, String tipo) throws Exception {
        int qtd = 0;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            qtd = pendenciaPs.consultarQuantidadeServentiaTipoReservadasPorTipo(usuarioDt, tipo);
        } finally {
            obFabricaConexao.fecharConexao();
        }
        return qtd;
    }

    /**
     * Consulta a quantidade de pendencias reservadas por tipo
     * 
     * @param usuarioDt,
     *            dt de usuario
     * @param tipo,
     *            tipo da pendencia
     * 
     * @author msapaula
     */
    /*public int consultarQuantidadeAssistenteTipoReservadasPorTipo(UsuarioDt usuarioDt, String tipo) throws Exception {
        int qtd = 0;

        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            qtd = pendenciaPs.consultarQuantidadeAssistenteTipoReservadasPorTipo(usuarioDt, tipo);
        } finally {
            obFabricaConexao.fecharConexao();
        }
        return qtd;
    }*/

    /**
     * Consulta a quantidade de pendencias pre-anlisadas por tipo
     * 
     * @param usuarioDt,
     *            dt de usuario
     * @param tipo,
     *            tipo da pendencia
     * 
     * @author lsbernardes
     */
   /*public int consultarQuantidadeServentiaPreAnalisadasPorTipo(UsuarioDt usuarioDt, String tipo) throws Exception {
        int qtd = 0;

        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            qtd = pendenciaPs.consultarQuantidadeServentiaPreAnalisadasPorTipo(usuarioDt, tipo);
        } finally {
            obFabricaConexao.fecharConexao();
        }
        return qtd;
    }*/
    
    public List consultarPendenciasServentiaPreAnalisadas(UsuarioDt usuarioDt) throws Exception {
    	List pendencias = new ArrayList();
    	FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            pendencias = pendenciaPs.consultarPendenciasServentiaPreAnalisadas(usuarioDt);
        } finally {
            obFabricaConexao.fecharConexao();
        }
        return pendencias;
    }

    /**
     * Consulta a quantidade de pendencias cargo serventia pre-anlisadas por
     * tipo
     * 
     * @param usuarioDt,
     *            dt de usuario
     * @param tipo,
     *            tipo da pendencia
     * 
     * @author lsbernardes
     */
   /* public int consultarQuantidadeCargoServentiaPreAnalisadasPorTipo(UsuarioDt usuarioDt, String tipo) throws Exception {
        int qtd = 0;

        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            qtd = pendenciaPs.consultarPendenciasCargoServentiaPreAnalisadasPorTipo(usuarioDt, tipo);
        } finally {
            obFabricaConexao.fecharConexao();
        }
        return qtd;
    }*/
    
    public List consultarPendenciasCargoServentiaPreAnalisadas(UsuarioDt usuarioDt) throws Exception {
    	List pendencias = new ArrayList();
    	FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            pendencias = pendenciaPs.consultarPendenciasCargoServentiaPreAnalisadas(usuarioDt);
        } finally {
            obFabricaConexao.fecharConexao();
        }
        return pendencias;
    }
    
    /**
     * Consulta a quantidade de pendencias serventia tipo pre-anlisadas por tipo
     * 
     * @param usuarioDt,
     *            dt de usuario
     * @param tipo,
     *            tipo da pendencia
     * 
     * @author lsbernardes
     */
    public int consultarQuantidadeServentiaTipoPreAnalisadasPorTipo(UsuarioDt usuarioDt, String tipo) throws Exception {
        int qtd = 0;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            qtd = pendenciaPs.consultarQuantidadeServentiaTipoPreAnalisadasPorTipo(usuarioDt, tipo);
        } finally {
            obFabricaConexao.fecharConexao();
        }
        return qtd;
    }

    /**
     * Consulta a quantidade de pendencias pre-anlisadas por tipo
     * 
     * @param usuarioDt,
     *            dt de usuario
     * @param tipo,
     *            tipo da pendencia
     * 
     * @author lsbernardes
     */
    /*public int consultarQuantidadeAssistentePreAnalisadasPorTipo(UsuarioDt usuarioDt, String tipo) throws Exception {
        int qtd = 0;

        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            qtd = pendenciaPs.consultarQuantidadeAssistentePreAnalisadasPorTipo(usuarioDt, tipo);
        } finally {
            obFabricaConexao.fecharConexao();
        }
        return qtd;
    }*/

    public byte[] relUsuariosPendencias(String stCaminho, String stId_Serventia, String stId_Usuario, String stAno, String stMes) throws Exception {
        byte[] temp = null;
        FabricaConexao obFabricaConexao = null;
        ByteArrayOutputStream baos = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

            List liProcessosServentia = obPersistencia.relUsuariosPendencias(stId_Usuario, stId_Serventia, stAno, stMes);

            InterfaceJasper ei = new InterfaceJasper(liProcessosServentia);

            // PATH PARA OS ARQUIVOS JASPER DO RELATORIO
            // MOVIMENTACAO*****************************************************
            String pathJasper = stCaminho + "WEB-INF" + File.separator + "relatorios" + File.separator + "UsuariosPendencias.jasper";

            // par�metros do relat�rio
            Map parametros = new HashMap();
            parametros.put("DataReferencia", "");

            JasperPrint jp = JasperFillManager.fillReport(pathJasper, parametros, ei);

            JRPdfExporter jr = new JRPdfExporter();
            jr.setParameter(JRExporterParameter.JASPER_PRINT, jp);
            baos = new ByteArrayOutputStream();
            jr.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, baos);
            jr.exportReport();

            temp = baos.toByteArray();
            baos.close();

        } catch (Exception e) {
        	try {if (baos!=null) baos.close(); } catch(Exception ex ) {};
            throw e;
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return temp;
    }
    
    

    public void gerarPendencias(ProcessoDt processo, List pendenciasGerar, MovimentacaoDt movimentacaoDt, List arquivos, UsuarioDt usuarioDt, PendenciaDt pendenciaPai, LogDt logDt, FabricaConexao conexao) throws MensagemException, Exception {
    	this.gerarPendencias( processo, pendenciasGerar, movimentacaoDt, arquivos, usuarioDt, pendenciaPai, "", logDt, conexao);
    }

    /**
     * M�otodo que trata uma lista de pend�ncias a serem geradas provenientes da movimenta��o de processos ou an�lise de autos conclusos.
     * Nesse m�todo ser�o tratadas as pend�ncias que ser�o resolvidas pelo sistema, redirecionando o fluxo 
     * para outras classes, e tamb�m os outros tipos de pend�ncias que ser�o geradas pelo m�todo salvarPendencias()
     * 
     * @param processo, identifica��o do processo para o qual ser�o geradas pend�ncias
     * @param pendenciasGerar, lista de pend�ncias marcadas pelo usu�rio para gerar
     * @param movimentacaoDt, movimenta��o relacionada as pend�ncias
     * @param arquivos, lista de arquivos inseridos e que deve ser vinculada as pend�ncias
     * @param usuarioDt, usu�rio respons�vel
     * @param logDt, objeto com dados do log
     * @param conexao, conex�o ativa
     * 
     * @author msapaula
     */
    public void gerarPendencias(ProcessoDt processo, List pendenciasGerar, MovimentacaoDt movimentacaoDt, List arquivos, UsuarioDt usuarioDt, PendenciaDt pendenciaPai, String id_Classificador, LogDt logDt, FabricaConexao conexao) throws MensagemException, Exception {
        ProcessoNe processoNe = new ProcessoNe();
        AudienciaNe audienciaNe;
        RecursoNe recursoNe;
        boolean boArquivando = false;

        for (int j = 0; j < pendenciasGerar.size(); j++) {
            dwrMovimentarProcesso dt = (dwrMovimentarProcesso) pendenciasGerar.get(j);

            switch (Funcoes.StringToInt(dt.getCodPendenciaTipo())) {

            case PendenciaTipoDt.ARQUIVAMENTO:
            	boArquivando=true;
                processoNe.arquivarProcesso(processo, logDt, usuarioDt.getId_UsuarioServentia(), "", dt.getId_ProcArquivamentoTipo(), conexao);
                break;
                
            case PendenciaTipoDt.ATIVAR_PROCESSO_CALCULO:
            	processoNe.ativarProcessoCalculo(processo, logDt, usuarioDt.getId_UsuarioServentia(), "", conexao);
                break;
                
            case PendenciaTipoDt.ARQUIVAMENTO_PROVISORIO:
            	processoNe.arquivarProcessoProvisoriamente(processo, logDt, usuarioDt.getId_UsuarioServentia(), conexao);
            	break;

            case PendenciaTipoDt.DESARQUIVAMENTO:
                processoNe.desarquivarProcesso(processo, logDt, usuarioDt.getId_UsuarioServentia(), "", conexao);
                break;

            case PendenciaTipoDt.MARCAR_SESSAO:
                audienciaNe = new AudienciaNe();
                List listaRecursoSecundarioParte = dt.getRecursoSecundarioPartesDt();
                if (listaRecursoSecundarioParte != null && !listaRecursoSecundarioParte.isEmpty()){
                	ProcessoTipoDt tipoDt = new ProcessoTipoNe().consultarId(((RecursoSecundarioParteDt) listaRecursoSecundarioParte.get(0)).getId_ProcessoTipoRecursoSecundario());
                	dt.setClasse(tipoDt.getProcessoTipo() + (StringUtils.isEmpty(dt.getClasse()) ? "" : " - " + dt.getClasse()));                	
                }
                AudienciaProcessoDt audienciaProcessoDt = audienciaNe.marcarSessao(processo, dt.getId_Sessao(), dt.getDataSessao(), dt.getId_Classe(), dt.getClasse(), usuarioDt.getId_UsuarioServentia(), logDt, conexao, usuarioDt, dt.getIdDestinatario());
                if (listaRecursoSecundarioParte != null){
            		cadastrarRecursoSecundarioPartes(audienciaProcessoDt.getId(),listaRecursoSecundarioParte,conexao);
            	}
                if (dt.isVirtual()) {
                	cadastrarVotantesSessaoVirtual(audienciaProcessoDt.getId(), dt, usuarioDt, conexao);
                	AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
                	audienciaProcessoNe.alterarPedidoSustentacaoOral(audienciaProcessoDt.getId(), dt.isPermiteSustentacaoOral(), conexao);
                }
                
                break;

            case PendenciaTipoDt.REMARCAR_SESSAO:
                audienciaNe = new AudienciaNe();
                audienciaNe.remarcarSessao(processo, dt.getId_Sessao(), dt.getId_Classe(), dt.getClasse(), dt.getDataSessao(), usuarioDt, logDt, conexao, dt.getIdDestinatario());
                break;
                
            case PendenciaTipoDt.DESMARCAR_SESSAO:
                audienciaNe = new AudienciaNe();
                audienciaNe.desmarcarSessao(processo, dt.getId_Sessao(), usuarioDt, logDt, conexao);
                break;
                
            case PendenciaTipoDt.ENVIAR_INSTANCIA_SUPERIOR:
                recursoNe = new RecursoNe();
                processo.setId_AreaDistribuicaoRecursal(dt.getCodDestinatario());
                recursoNe.salvarRecursoInstaciaSuperior(processo, usuarioDt, logDt, conexao);
                break;
                
            case PendenciaTipoDt.ENVIAR_PROCESSO_PRESIDENTE_UNIDADE:
            	processoNe.enviarProcessoPresidenteUnidade(processo, usuarioDt);
                break;
               
            case PendenciaTipoDt.RETORNAR_AUTOS_RELATOR_PROCESSO:
            	processoNe.retornarAutosRelatorProcesso(processo, usuarioDt);
                break;

            case PendenciaTipoDt.RETORNAR_SERVENTIA_ORIGEM:
            	if (!boArquivando){
            		for (int k=j; k<pendenciasGerar.size(); k++){
            			dwrMovimentarProcesso tempDt = (dwrMovimentarProcesso) pendenciasGerar.get(k);
            			if (tempDt.isArquivamento()){
            				boArquivando=true;
            				break;
            			}
            		}
            	}
                recursoNe = new RecursoNe();
                ProcessoEncaminhamentoNe processoEncaminhamentoNe = new ProcessoEncaminhamentoNe();
                String procEncaminhamentoId = processoEncaminhamentoNe.consultarEncaminhamentoSemDevolucao(processo.getId() );
                if(procEncaminhamentoId != null && !procEncaminhamentoId.isEmpty()) {
                	processoNe.salvarRetornoServentiaOrigemEncaminhamento(processo, usuarioDt, arquivos, logDt, boArquivando, conexao);
                }
                else {
                	recursoNe.salvarRetornoServentiaOrigem(processo, usuarioDt, arquivos, logDt, conexao);
                }
                
                break;

            case PendenciaTipoDt.FINALIZAR_SUSPENSAO_PROCESSO:
                processoNe.finalizaSuspensaoProcessoMovimentacao(processo.getId(), logDt, usuarioDt, conexao);
                break;
            
            case PendenciaTipoDt.FINALIZAR_SUSPENSAO_PROCESSO_ACORDO_OUTROS:
            	processoNe.finalizaSuspensaoProcessoPorAcordoMovimentacao(processo.getId(), logDt, usuarioDt, conexao);
            	break;
               
            case PendenciaTipoDt.SUSPENSAO_PROCESSO:
                processoNe.suspenderProcessoAlterandoFaseProcessual(processo.getId(), dt.getPrazo(), logDt, usuarioDt.getId_UsuarioServentia(), dt.getCodTipoProcessoFase(), conexao);
                this.gerarPendenciaSuspensaoProcesso(dt, movimentacaoDt, processo, arquivos, usuarioDt, logDt, conexao);
                break;
                
            case PendenciaTipoDt.CONTUMACIA:
            	this.marcarAusenciaPartesProcesso(dt, processo, ProcessoParteTipoDt.POLO_ATIVO_CODIGO, logDt, conexao);
            	break;
            	
            case PendenciaTipoDt.REVELIA:
            	this.marcarAusenciaPartesProcesso(dt, processo, ProcessoParteTipoDt.POLO_PASSIVO_CODIGO, logDt, conexao);
            	break;
            	
            case PendenciaTipoDt.MARCAR_AUDIENCIA:{
            	String id_serventia = usuarioDt.getId_Serventia();
            	if (usuarioDt.isServentiaUPJFamilia() || usuarioDt.isServentiaUPJSucessoes() || usuarioDt.isServentiaUPJCriminal()) {
            		id_serventia = processoNe.getGabineteUpjResposavelProcesso(processo.getId(), conexao);
            		if(id_serventia==null) {
            			throw new MensagemException("N�o � poss�vel determinar o gabinete UPJ respos�vel pelo processo.");
            		}
            	}
            	
            	this.gerarPendenciaMarcarAudienciaProcesso(processo, usuarioDt.getId_UsuarioServentia(), id_serventia, movimentacaoDt.getId(), processo.getProcessoPrioridadeCodigo(), arquivos, logDt, conexao);
            	break;
            	
            } case PendenciaTipoDt.MARCAR_AUDIENCIA_AUTOMATICA:
            	AudienciaDt audienciaDt = new AudienciaDt();
            	audienciaNe = new AudienciaNe();
            	
            	audienciaDt = this.preparaAudienciaDt(processo, audienciaNe, dt, usuarioDt, logDt);
        		audienciaDt = audienciaNe.agendarAudienciaAutomaticamenteProcesso(audienciaDt, usuarioDt, conexao);
        		
        		// Caso n�o encontre alguma audi�ncia dispon�vel, gera pend�ncia Marcar Audiencia
        		if (audienciaDt == null) {
        			String id_serventiaPend = usuarioDt.getId_Serventia();
                	if (usuarioDt.isServentiaUPJFamilia() || usuarioDt.isServentiaUPJSucessoes() || usuarioDt.isServentiaUPJCriminal()) {
                		id_serventiaPend = processoNe.getGabineteUpjResposavelProcesso(processo.getId(), conexao);
                		if(id_serventiaPend==null) {
                			throw new MensagemException("N�o � poss�vel determinar o gabinete UPJ respos�vel pelo processo.");
                		}
                	}
                	
	                AudienciaTipoDt.Codigo tipoCodigo = AudienciaTipoDt.Codigo.NENHUM;
	                tipoCodigo = tipoCodigo.getCodigo(Funcoes.StringToInt(dt.getCodTipoAudiencia()));
	            	switch (tipoCodigo) {
	            		case CONCILIACAO_CEJUSC_DPVAT: 
	            			this.gerarPendenciaMarcarAudienciaConciliacaoCEJUSCDPVAT(processo, usuarioDt.getId_UsuarioServentia(), id_serventiaPend, movimentacaoDt.getId(), processo.getProcessoPrioridadeCodigo(), arquivos, logDt, conexao);
	            			break;
	            		 case MEDIACAO_CEJUSC: 
	        				this.gerarPendenciaMarcarAudienciaConciliacaoCEJUSC(processo, usuarioDt.getId_UsuarioServentia(), id_serventiaPend, movimentacaoDt.getId(), processo.getProcessoPrioridadeCodigo(), arquivos, logDt, conexao);
	        				break;
	        			 case CONCILIACAO_CEJUSC: 
	        				this.gerarPendenciaMarcarAudienciaConciliacaoCEJUSC(processo, usuarioDt.getId_UsuarioServentia(), id_serventiaPend, movimentacaoDt.getId(), processo.getProcessoPrioridadeCodigo(), arquivos, logDt, conexao);
	        				break;
	        			
	        			default:
	        				this.gerarPendenciaMarcarAudienciaProcesso(processo, usuarioDt.getId_UsuarioServentia(), id_serventiaPend, movimentacaoDt.getId(), processo.getProcessoPrioridadeCodigo(), arquivos, logDt, conexao);
	        				break;
	            	}
        		}
        		break;
            	
            case PendenciaTipoDt.AVERBACAO_CUSTAS:
            	this.gerarPendenciaAverbacaoCustas(processo, usuarioDt.getId_UsuarioServentia(), usuarioDt.getId_Serventia(), movimentacaoDt.getId(), processo.getProcessoPrioridadeCodigo(), arquivos, logDt, conexao);
            	break;
            
            case PendenciaTipoDt.MARCAR_AUDIENCIA_CONCILIACAO_CEJUSC:{
            	String id_serventia = usuarioDt.getId_Serventia();
            	if (usuarioDt.isServentiaUPJFamilia() || usuarioDt.isServentiaUPJSucessoes() || usuarioDt.isServentiaUPJCriminal()) {
            		id_serventia = processoNe.getGabineteUpjResposavelProcesso(processo.getId(), conexao);
            		if(id_serventia==null) {
            			throw new MensagemException("N�o � poss�vel determinar o gabinete UPJ respos�vel pelo processo.");
            		}
            	}
            	this.gerarPendenciaMarcarAudienciaConciliacaoCEJUSC(processo, usuarioDt.getId_UsuarioServentia(), id_serventia, movimentacaoDt.getId(), processo.getProcessoPrioridadeCodigo(), arquivos, logDt, conexao);
            	break;
            
            } case PendenciaTipoDt.MARCAR_AUDIENCIA_CONCILIACAO_CEJUSC_DPVAT:{
            	String id_serventia = usuarioDt.getId_Serventia();
            	if (usuarioDt.isServentiaUPJFamilia() || usuarioDt.isServentiaUPJSucessoes() || usuarioDt.isServentiaUPJCriminal()) {
            		id_serventia = processoNe.getGabineteUpjResposavelProcesso(processo.getId(), conexao);
            		if(id_serventia==null) {
            			throw new MensagemException("N�o � poss�vel determinar o gabinete UPJ respos�vel pelo processo.");
            		}
            	}
            	this.gerarPendenciaMarcarAudienciaConciliacaoCEJUSCDPVAT(processo, usuarioDt.getId_UsuarioServentia(), id_serventia, movimentacaoDt.getId(), processo.getProcessoPrioridadeCodigo(), arquivos, logDt, conexao);
            	break;
            
            } case PendenciaTipoDt.MARCAR_AUDIENCIA_MEDIACAO_CEJUSC:{
            	String id_serventia = usuarioDt.getId_Serventia();
            	if (usuarioDt.isServentiaUPJFamilia() || usuarioDt.isServentiaUPJSucessoes() || usuarioDt.isServentiaUPJCriminal()) {
            		id_serventia = processoNe.getGabineteUpjResposavelProcesso(processo.getId(), conexao);
            		if(id_serventia==null) {
            			throw new MensagemException("N�o � poss�vel determinar o gabinete UPJ respos�vel pelo processo.");
            		}
            	}
            	this.gerarPendenciaMarcarAudienciaMediacaoCEJUSC(processo, usuarioDt.getId_UsuarioServentia(), id_serventia, movimentacaoDt.getId(), processo.getProcessoPrioridadeCodigo(), arquivos, logDt, conexao);
            	break;
            
            } case PendenciaTipoDt.OFICIO_COMUNICATORIO:
            	this.gerarPendenciaOficioNotificacaoProcesso(processo, usuarioDt.getId_UsuarioServentia(), usuarioDt.getId_Serventia(), movimentacaoDt.getId(), processo.getProcessoPrioridadeCodigo(), arquivos, logDt, conexao);
            	break;
            	
            case PendenciaTipoDt.MARCAR_SESSAO_EXTRA_PAUTA:
                audienciaNe = new AudienciaNe();
                List listaRecursoSecundarioParteExtra = dt.getRecursoSecundarioPartesDt();
                if (listaRecursoSecundarioParteExtra != null && !listaRecursoSecundarioParteExtra.isEmpty()){
                	ProcessoTipoDt tipoDt = new ProcessoTipoNe().consultarId(((RecursoSecundarioParteDt) listaRecursoSecundarioParteExtra.get(0)).getId_ProcessoTipoRecursoSecundario());
                	dt.setClasse(tipoDt.getProcessoTipo() + " - " + dt.getClasse());
                }
                
                AudienciaProcessoDt audienciaProcessoExtraDt = audienciaNe.marcarSessaoEmMesaParaJulgamento(processo, dt.getId_Sessao(), dt.getDataSessao(), dt.getId_Classe(), dt.getClasse(), usuarioDt.getId_UsuarioServentia(), logDt, conexao, usuarioDt, dt.getIdDestinatario());
                if (listaRecursoSecundarioParteExtra != null){
            		cadastrarRecursoSecundarioPartes(audienciaProcessoExtraDt.getId(),listaRecursoSecundarioParteExtra,conexao);
            	}
                
                if(dt.isVirtual()) {
                	// Adicionar votantes
                	cadastrarVotantesSessaoVirtual(audienciaProcessoExtraDt.getId(), dt, usuarioDt, conexao);
                	// Buscar Presidente e MP
                	(new VotoNe()).atualizarMPPresidenteSessao(audienciaProcessoExtraDt, conexao);
                	// Gerar pendencia de Voto / Ementa para Relator
                	audienciaNe.gerarPendenciaVoto(audienciaProcessoExtraDt.getId_ServentiaCargo(), movimentacaoDt.getId(), processo.getId(), processo.getId_ProcessoPrioridade(), false, new PendenciaNe(), usuarioDt, conexao, null, audienciaProcessoExtraDt.getId());
                }
                break;
                
            case PendenciaTipoDt.ENCAMINHAR_PROCESSO:
            	RedistribuicaoDt redistribuicaoDt = new RedistribuicaoDt();
            	redistribuicaoDt.setId_AreaDistribuicao(dt.getIdAreaDistribuicaoDestino());
            	redistribuicaoDt.setId_Serventia(dt.getIdServentiaDestino());
            	String idPendenciaIgnorar = null;
            	
            	if(pendenciaPai != null)
            		idPendenciaIgnorar = pendenciaPai.getId();
            	
            	MovimentacaoProcessoDt movimentacaoProcessoDt = new MovimentacaoProcessoDt();
            	List listaProcessos = new ArrayList();
            	listaProcessos.add(processo);
            	List listaPendencias = null; 
            	redistribuicaoDt.setListaProcessos(listaProcessos);
            	movimentacaoProcessoDt.copiarMovimentacaoDt(movimentacaoDt, listaProcessos, arquivos, listaPendencias);
            	processoNe.salvarEncaminhamentoProcesso(redistribuicaoDt, usuarioDt, movimentacaoProcessoDt, idPendenciaIgnorar, conexao);
            	break;
            	
            case PendenciaTipoDt.ENCAMINHAR_PROCESSO_GABINETE:
            	RedistribuicaoDt redistribuicaoDt2 = new RedistribuicaoDt();
            	redistribuicaoDt2.setId_ServentiaCargo(dt.getIdServentiaCargo());
            	String idPendenciaIgnorar2 = null;
            	
            	if(pendenciaPai != null)
            		idPendenciaIgnorar2 = pendenciaPai.getId();
            	
            	MovimentacaoProcessoDt movimentacaoProcessoDt2 = new MovimentacaoProcessoDt();
            	List listaProcessos2 = new ArrayList();
            	listaProcessos2.add(processo);
            	List listaPendencias2 = null; 
            	redistribuicaoDt2.setListaProcessos(listaProcessos2);
            	movimentacaoProcessoDt2.copiarMovimentacaoDt(movimentacaoDt, listaProcessos2, arquivos, listaPendencias2);
            	processoNe.salvarEncaminhamentoProcessoGabinete(redistribuicaoDt2, usuarioDt, movimentacaoProcessoDt2, idPendenciaIgnorar2, conexao);
            	break;
            	
            case PendenciaTipoDt.TORNAR_SIGILOSO:
            	processoNe.tornarSigiloso(processo, logDt, usuarioDt.getId_UsuarioServentia(), conexao);
            	break;
            	
            case PendenciaTipoDt.RETIRAR_SIGILO:
            	if (!processo.isSigiloso()){
            		throw new MensagemException("N�o � poss�vel retirar o Sigilo de um processo que n�o sigiloso.");            		
            	}
            	processoNe.retirarSigilo(processo, logDt, usuarioDt.getId_UsuarioServentia(), conexao);
            	break;
            	
            	
            default:
                // Quando precisar gerar pend�ncias para o processo, dever�o ser consultados os dados completos do processo com suas partes
                if (processo.getPartesProcesso() == null || processo.getPartesProcesso().size() == 0) {
                    processo = processoNe.consultarIdCompleto(processo.getId());
                }
                this.salvarPendencias(dt, movimentacaoDt, processo, pendenciaPai, arquivos, usuarioDt, id_Classificador , logDt, conexao);
                break;

            }
        }
    }

    /**
     * M�todo respons�vel em verificar se as pend�ncias marcadas para serem geradas, 
     * tanto na movimenta��o quanto na analise de autos conclusos, podem ser efetivadas, ou se h� pend�ncias no processo,
     * como por exemplo, audi�ncias em aberto.
     * 
     * @param processoDt, processo que est� sendo movimentado ou analisado
     * @param pendenciasGerar, lista de pend�ncias marcadas para serem geradas
     * @param grupoCodigo, grupo do usu�rio que est� movimentando ou analisando
     * @param finalizandoConclusao, boolean onde se informa se o usu�rio est� finalizando uma pend�ncia de conclus�o
     * 
     * @author msapaula
     */
    public String verificaPendenciasProcesso(ProcessoDt processoDt, List pendenciasGerar, String grupoCodigo, boolean finalizandoConclusao) throws Exception {
        ProcessoNe processoNe = new ProcessoNe();
        String stRetorno = "";
        for (int j = 0; j < pendenciasGerar.size(); j++) {
            dwrMovimentarProcesso dt = (dwrMovimentarProcesso) pendenciasGerar.get(j);
            int codPendenciaTipo = Funcoes.StringToInt(dt.getCodPendenciaTipo()); 
            switch (codPendenciaTipo) {

            case PendenciaTipoDt.ARQUIVAMENTO:
            case PendenciaTipoDt.ARQUIVAMENTO_PROVISORIO:
                // Verifica se um processo pode ser arquivado
                stRetorno += processoNe.podeArquivar(processoDt, grupoCodigo);
                break;

            case PendenciaTipoDt.DESARQUIVAMENTO:
                // Verifica se um processo pode ser desarquivado
                stRetorno += processoNe.podeDesarquivar(processoDt);
                break;

            case PendenciaTipoDt.MARCAR_SESSAO:
            case PendenciaTipoDt.MARCAR_SESSAO_EXTRA_PAUTA:
                // Verificar se processo j� tem sess�o marcada
                stRetorno += processoNe.podeMarcarSessao(processoDt, codPendenciaTipo, dt.getDataSessao(), dt.getId_Classe());
                break;

            case PendenciaTipoDt.REMARCAR_SESSAO:
            	// Verificar se processo j� tem sess�o marcada
            	String retornoDesmarcar = processoNe.podeDesmarcarSessao(processoDt);
            	if (retornoDesmarcar == null || retornoDesmarcar.trim().length() == 0)  retornoDesmarcar = processoNe.podeMarcarSessao(processoDt, codPendenciaTipo, dt.getDataSessao(), dt.getId_Classe());            		
            	stRetorno += retornoDesmarcar;            	
            	break;
            	
            case PendenciaTipoDt.DESMARCAR_SESSAO:
                // Verificar se processo j� tem sess�o marcada
                stRetorno += processoNe.podeDesmarcarSessao(processoDt);                
                break;

            case PendenciaTipoDt.ENVIAR_INSTANCIA_SUPERIOR:
                // Verificando caso de enviar para turmas recursais
                stRetorno += processoNe.podeEnviarInstanciaSuperior(processoDt, grupoCodigo, finalizandoConclusao);
                break;

            case PendenciaTipoDt.RETORNAR_SERVENTIA_ORIGEM:
                // Verificando caso de retornar � origem
                ProcessoEncaminhamentoNe processoEncaminhamentoNe = new ProcessoEncaminhamentoNe();
                String procEncaminhamentoId = processoEncaminhamentoNe.consultarEncaminhamentoSemDevolucao(processoDt.getId() );
                if(procEncaminhamentoId != null && !procEncaminhamentoId.isEmpty()) {
                	stRetorno += processoNe.podeRetornarServentiaOrigemEncaminhamento(processoDt, grupoCodigo, false);
                }
                else {
                	stRetorno += processoNe.podeRetornarServentiaOrigem(processoDt, grupoCodigo);
                }
                break;
                
            case PendenciaTipoDt.FINALIZAR_SUSPENSAO_PROCESSO:
                // Verifica se um processo pode ter suspens�o finalizada
                stRetorno += processoNe.podeFinalizarSuspensao(processoDt);
                break;

            case PendenciaTipoDt.SUSPENSAO_PROCESSO:
                // Verifica se um processo pode ser suspenso
                stRetorno += processoNe.podeSuspender(processoDt, grupoCodigo);
                break;
                
            case PendenciaTipoDt.ATIVAR_PROCESSO_CALCULO:
                // Verifica se um processo pode ser ativado a partir do c�lculo
                stRetorno += processoNe.podeAtivarProcessoCalculo(processoDt, grupoCodigo);
                break;

//            default:
//                PendenciaTipoNe pendenciaTipoNe = new PendenciaTipoNe();
//                // Se for uma conclus�o, verifica se pode ir concluso
//                if (pendenciaTipoNe.Conclusao(dt.getCodPendenciaTipo())) {
//                    stRetorno += processoNe.podeGerarConclusao(processoDt);
//                }
//                break;
            }
        }
        return stRetorno;
    }
    
    /**
     * M�todo respons�vel em verificar se as pend�ncias marcadas para serem geradas, 
     * tanto na movimenta��o quanto na analise de autos conclusos, podem ser efetivadas, ou se h� pend�ncias no processo,
     * como por exemplo, audi�ncias em aberto.
     * 
     * @param processoDt, processo que est� sendo movimentado ou analisado
     * @param pendenciasGerar, lista de pend�ncias marcadas para serem geradas
     * @param grupoCodigo, grupo do usu�rio que est� movimentando ou analisando
     * 
     * @author msapaula
     */
    public String obtemAlertaMovimentarProcesso(ProcessoDt processoDt, List pendenciasGerar, String grupoCodigo) throws Exception {
        ProcessoNe processoNe = new ProcessoNe();
        String stRetorno = "";
        for (int j = 0; j < pendenciasGerar.size(); j++) {
            dwrMovimentarProcesso dt = (dwrMovimentarProcesso) pendenciasGerar.get(j);
            int codPendenciaTipo = Funcoes.StringToInt(dt.getCodPendenciaTipo()); 
            switch (codPendenciaTipo) {

            case PendenciaTipoDt.MARCAR_SESSAO:
            case PendenciaTipoDt.MARCAR_SESSAO_EXTRA_PAUTA:
                // Verificar se processo j� tem sess�o marcada
                stRetorno += processoNe.obtemAlertaMovimentarProcesso(processoDt, codPendenciaTipo, dt.getDataSessao(), dt.getId_Classe());
                break;            
            }
        }
        return stRetorno;
    }

    /**
     * M�todo respons�vel em verificar se as pend�ncias marcadas para serem
     * geradas na movimenta��o processual decorrente de um Acesso de outra
     * Serventia podem ser efetivadas, como por exemplo, o caso de Carta
     * Precat�ria. Deve barrar casos de arquivamento, Suspens�o, Conclus�o e
     * Envio para Turmas.
     * 
     * @param pendenciasGerar,
     *            lista de pend�ncias marcadas para serem geradas
     * 
     * @author msapaula
     */
    public String verificaPendenciasProcessoOutraServentia(List pendenciasGerar) throws Exception {
        String stRetorno = "";
        for (int j = 0; j < pendenciasGerar.size(); j++) {
            dwrMovimentarProcesso dt = (dwrMovimentarProcesso) pendenciasGerar.get(j);

            switch (Funcoes.StringToInt(dt.getCodPendenciaTipo())) {

            case PendenciaTipoDt.ARQUIVAMENTO:
                stRetorno += "Processo n�o pode ser arquivado pois � de outra Serventia.";
                break;

            case PendenciaTipoDt.DESARQUIVAMENTO:
                stRetorno += "Processo n�o pode ser desarquivado pois � de outra Serventia.";
                break;

            case PendenciaTipoDt.ENVIAR_INSTANCIA_SUPERIOR:
                stRetorno += "Processo n�o pode ser enviado para Turmas Recursais pois � de outra Serventia.";
                break;

            case PendenciaTipoDt.SUSPENSAO_PROCESSO:
                stRetorno += "Processo n�o pode ser suspenso pois � de outra Serventia.";
                break;
                
            case PendenciaTipoDt.ATIVAR_PROCESSO_CALCULO:
                stRetorno += "Processo de c�lculo n�o pode ser ativado pois � de outra Serventia.";
                break;

            default:
                PendenciaTipoNe pendenciaTipoNe = new PendenciaTipoNe();
                if (pendenciaTipoNe.Conclusao(dt.getCodPendenciaTipo())) {
                    stRetorno += "N�o � poss�vel gerar Conclus�o ao juiz pois Processo � de outra Serventia.";
                }
                break;
            }
        }
        return stRetorno;
    }
    
    /**
     * Retorna os tipos de pend�ncia do tipo conclus�o que est�o abertas para
     * listar na p�gina inicial
     * 
     * @author msapaula
     * @since 25/05/2009
     * @param usuarioDt
     *            usuario de referencia
     */
    public Map consultarTiposConclusoesAbertas(UsuarioDt usuarioDt) throws Exception {
        Map tiposPendencia = new HashMap();
        String id_ServentiaCargo = null;
        FabricaConexao obFabricaConexao = null;        
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            //switch (Funcoes.StringToInt(usuarioDt.getGrupoCodigo())) {
           switch (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo())) {
//            case GrupoDt.JUIZES_VARA:
//            case GrupoDt.JUIZES_TURMA_RECURSAL:
//            case GrupoDt.DESEMBARGADOR:
              case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
              case GrupoTipoDt.JUIZ_TURMA:
              case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
                id_ServentiaCargo = usuarioDt.getId_ServentiaCargo();
                break;

//            case GrupoDt.ASSISTENTES_JUIZES_VARA:
//            case GrupoDt.ASSISTENTES_JUIZES_SEGUNDO_GRAU:
              case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
              case GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:
              case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
                id_ServentiaCargo = usuarioDt.getId_ServentiaCargoUsuarioChefe();
                break;
            }
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            if (id_ServentiaCargo != null && !id_ServentiaCargo.equals("")) {
                tiposPendencia = obPersistencia.consultarTiposConclusoesAbertas(id_ServentiaCargo);
            }

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return tiposPendencia;
    }

    /**
     * Consulta a quantidade de pendencias do tipo Conclus�o que n�o foram
     * analisadas, de um determinado tipo para o usuario - Consulta somente as
     * conclus�es que n�o possuem nenhuma pr�-an�lise registrada
     * 
     * @author msapaula
     * @since 25/05/2009 14:44
     * @param usuarioDt, usuario que deseja a quantidade
     * @param tipo, tipo da conclus�o
     */
	public List consultarQuantidadeConclusoesNaoAnalisadas(UsuarioDt usuarioDt, boolean somenteSessao, boolean ehVotoVencido) throws Exception {
        List liTemp = new ArrayList();
        String id_ServentiaCargo = null;
        FabricaConexao obFabricaConexao = null;
        boolean ehAssistente = false;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            //switch (Funcoes.StringToInt(usuarioDt.getGrupoCodigo())) {
            switch (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo())) {
            	
//            case GrupoDt.JUIZES_VARA:
//            case GrupoDt.JUIZES_TURMA_RECURSAL:
//            case GrupoDt.DESEMBARGADOR:
//            case GrupoDt.DISTRIBUIDOR_GABINETE:
//            case GrupoDt.ASSISTENTES_GABINETE:
            case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
            case GrupoTipoDt.JUIZ_TURMA:
            case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
            case GrupoTipoDt.PRESIDENTE_SEGUNDO_GRAU:
            case GrupoTipoDt.DISTRIBUIDOR_GABINETE:
                id_ServentiaCargo = usuarioDt.getId_ServentiaCargo();
                break;
            case GrupoTipoDt.ASSISTENTE_GABINETE:
            case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
                id_ServentiaCargo = usuarioDt.getId_ServentiaCargo();
                ehAssistente = true;
                break;
			case GrupoTipoDt.JUIZ_LEIGO:
				
	            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());				
				String id_Serventia = usuarioDt.getId_Serventia();
		        List listaServentiaCargo = new ArrayList();

		        listaServentiaCargo = obPersistencia.consultarJuizesServentia(id_Serventia);
				
		        if (listaServentiaCargo != null){
		        	for(int i = 0; i < listaServentiaCargo.size(); i++) {
		            	liTemp.addAll(obPersistencia.consultarQuantidadeConclusoesNaoAnalisadas((String)listaServentiaCargo.get(i), somenteSessao, ehAssistente, ehVotoVencido));
			        }
		        }
                
                
				break;

//            case GrupoDt.ASSISTENTES_JUIZES_VARA:
//            case GrupoDt.ASSISTENTES_JUIZES_SEGUNDO_GRAU:
            case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
            case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
            case GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:
            	
                id_ServentiaCargo = usuarioDt.getId_ServentiaCargoUsuarioChefe();                
                break;
            }
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            if (id_ServentiaCargo != null && !id_ServentiaCargo.equals("")) {
            	liTemp = obPersistencia.consultarQuantidadeConclusoesNaoAnalisadas(id_ServentiaCargo, somenteSessao, ehAssistente, ehVotoVencido);
            }
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return liTemp;
    }
    
 /**
     * Consulta a quantidade de pendencias do tipo conclus�o que s�o virtuais e est�o n�o analisadas
     * respons�vel seja o serventiaCargo passado
     * 
     * @param usuarioSessao, usuario que est� realizando a consulta
     * @param somenteSessao, filtro por sessao
     * @param ehVotoVencido, filtro por voto vencido
     * @param isIniciada, filtro para sess�o iniciada
     * @author lrcampos
	 * @since 14/10/2019 15:30
     */

	public List consultarQuantidadeConclusoesNaoAnalisadasVirtual(UsuarioNe usuarioSessao, boolean somenteSessao, boolean ehVotoVencido, boolean isIniciada) throws Exception {
        List liTemp = new ArrayList();
        List teste = new ArrayList<>();
        String id_ServentiaCargo = null;
        String digitoVerificador = null;
        FabricaConexao obFabricaConexao = null;
        boolean ehAssistente = false;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            //switch (Funcoes.StringToInt(usuarioDt.getGrupoCodigo())) {
            int grupoTipo =usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt();
            switch (grupoTipo) {
            case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
            case GrupoTipoDt.JUIZ_TURMA:
            case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
            case GrupoTipoDt.PRESIDENTE_SEGUNDO_GRAU:
            case GrupoTipoDt.DISTRIBUIDOR_GABINETE:
                id_ServentiaCargo = usuarioSessao.getUsuarioDt().getId_ServentiaCargo();
                break;
            case GrupoTipoDt.ASSISTENTE_GABINETE:
            case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
                id_ServentiaCargo = usuarioSessao.getUsuarioDt().getId_ServentiaCargo();
                ehAssistente = true;
                break;

            case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
            case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
            case GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:
                id_ServentiaCargo = usuarioSessao.getUsuarioDt().getId_ServentiaCargoUsuarioChefe();
                break;
            }
            
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            if (id_ServentiaCargo != null && !id_ServentiaCargo.equals("")) {
            	String idPendTipo = new PendenciaTipoNe().consultarPendenciaTipoCodigo(PendenciaTipoDt.CONCLUSO_VOTO).getId();
            	liTemp = obPersistencia.consultarQuantidadeConclusoesNaoAnalisadasVirtual(usuarioSessao, id_ServentiaCargo, "", "", "", idPendTipo, true, ehVotoVencido, isIniciada, ehAssistente);
            }
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return liTemp;
    }
    
    /**
     * Consulta a quantidade de pendencias do tipo Conclus�o que n�o foram
     * analisadas, de um determinado tipo para o usuario - Consulta somente as
     * conclus�es que n�o possuem nenhuma pr�-an�lise registrada
     * 
     * @param usuarioDt, usuario que deseja a quantidade
     * @param tipo, tipo da conclus�o
     */
    public List consultarQuantidadeConclusoesNaoAnalisadasAssistenteGabinete(UsuarioDt usuarioDt, boolean somenteSessao, boolean ehVotoVencido) throws Exception {
        List liTemp = new ArrayList();
        String id_ServentiaCargo = null;
        FabricaConexao obFabricaConexao = null;
        boolean ehAssistente = false;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            id_ServentiaCargo = usuarioDt.getId_ServentiaCargo();
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            
            if (id_ServentiaCargo != null && !id_ServentiaCargo.equals("")) {
            	liTemp = obPersistencia.consultarQuantidadeConclusoesNaoAnalisadasAssistenteGabinete(id_ServentiaCargo, somenteSessao, ehAssistente, ehVotoVencido);
            }
            
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return liTemp;
    }
    
    /**
     * Consulta a quantidade de pendencias que n�o foram analisadas, de um determinado tipo para o usuario 
     * - Consulta somente as pend�ncias que n�o possuem nenhuma pr�-an�lise registrada
     * 
     * @author msapaula
     * @param usuarioDt, usuario que deseja a quantidade
     */
    public List consultarQuantidadePendenciasNaoAnalisadas(UsuarioDt usuarioDt) throws Exception {
        List liTemp = new ArrayList();
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            
            //switch (Funcoes.StringToInt(usuarioDt.getGrupoCodigo())) {
            switch (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo())) {
//            case GrupoDt.JUIZES_VARA:
//            case GrupoDt.DESEMBARGADOR:
//            case GrupoDt.ASSISTENTES_GABINETE:
//            case GrupoDt.PROMOTORES:
              case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
              case GrupoTipoDt.JUIZ_TURMA:
	          case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
	          case GrupoTipoDt.ASSISTENTE_GABINETE:
	          case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
	          case GrupoTipoDt.MP:
                if (usuarioDt.getId_ServentiaCargo() != null && !usuarioDt.getId_ServentiaCargo().equals("")) {
                	liTemp = obPersistencia.consultarQuantidadePendenciasNaoAnalisadas(usuarioDt.getId_ServentiaCargo(), null);
                }
                break;
                
            // case GrupoDt.ADVOGADOS:
	          case GrupoTipoDt.ADVOGADO:
               	liTemp = obPersistencia.consultarQuantidadePendenciasNaoAnalisadas(null, usuarioDt.getId_UsuarioServentia());
               	break;
               	
            //case GrupoDt.ASSISTENTES_JUIZES_VARA:
	          case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
	          case GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:
	          case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
            	if (usuarioDt.getId_ServentiaCargoUsuarioChefe() != null && !usuarioDt.getId_ServentiaCargoUsuarioChefe().equals("")) {
					liTemp = obPersistencia.consultarQuantidadePendenciasNaoAnalisadas(usuarioDt.getId_ServentiaCargoUsuarioChefe(), null);
				}
				break;
               	
			//case GrupoDt.ASSISTENTES_ADVOGADOS_PROMOTORES:
	          case GrupoTipoDt.ASSESSOR_MP:
				if (usuarioDt.getId_ServentiaCargoUsuarioChefe() != null && !usuarioDt.getId_ServentiaCargoUsuarioChefe().equals("")) {
					liTemp = obPersistencia.consultarQuantidadePendenciasNaoAnalisadas(usuarioDt.getId_ServentiaCargoUsuarioChefe(), null);
				}
				break;	        	  
	          case GrupoTipoDt.ASSESSOR_ADVOGADO:
            	if (usuarioDt.getGrupoUsuarioChefe() != null && usuarioDt.getGrupoUsuarioChefe().length() > 0) {
					liTemp = obPersistencia.consultarQuantidadePendenciasNaoAnalisadas(null, usuarioDt.getId_UsuarioServentiaChefe());								
            	}
            	break;
            }
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return liTemp;
    }
    
    /**
     * Consulta a quantidade de pr�-an�lises simples de conclus�es por tipo
     * 
     * @author msapaula
     * @param usuarioDt, dt de usuario
     */
    public List consultarQuantidadeConclusoesPreAnalisadas(UsuarioDt usuarioDt, boolean somenteSessao, boolean ehVotoVencido) throws Exception {
    	List liTemp = new ArrayList();
        String id_ServentiaCargo = null;
        FabricaConexao obFabricaConexao = null;
        boolean ehAssistente = false;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            //switch (Funcoes.StringToInt(usuarioDt.getGrupoCodigo())) {
           switch (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo())) {
//            case GrupoDt.JUIZES_VARA:
//            case GrupoDt.JUIZES_TURMA_RECURSAL:
//            case GrupoDt.DESEMBARGADOR:
//            case GrupoDt.ASSISTENTES_GABINETE:
           case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
           case GrupoTipoDt.JUIZ_TURMA:
           case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
           case GrupoTipoDt.PRESIDENTE_SEGUNDO_GRAU:           
                id_ServentiaCargo = usuarioDt.getId_ServentiaCargo();
                break;           
           
           case GrupoTipoDt.ASSISTENTE_GABINETE:
           case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
                id_ServentiaCargo = usuarioDt.getId_ServentiaCargo();
                ehAssistente = true;
                break;
			case GrupoTipoDt.JUIZ_LEIGO:
				
	            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());				
				String id_Serventia = usuarioDt.getId_Serventia();
		        List listaServentiaCargo = new ArrayList();

		        listaServentiaCargo = obPersistencia.consultarJuizesServentia(id_Serventia);
				
		        if (listaServentiaCargo != null){
		        	for(int i = 0; i < listaServentiaCargo.size(); i++) {
		            	liTemp.addAll(obPersistencia.consultarQuantidadeConclusoesPreAnalisadas((String)listaServentiaCargo.get(i), false, somenteSessao, ehAssistente, ehVotoVencido));
			        }
		        }
                
                
				break;
//            case GrupoDt.ASSISTENTES_JUIZES_VARA:
//            case GrupoDt.ASSISTENTES_JUIZES_SEGUNDO_GRAU:
           case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
           case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
           case GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:
                id_ServentiaCargo = usuarioDt.getId_ServentiaCargoUsuarioChefe();
                //ehAssistente = true;
                break;
            }
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            if (id_ServentiaCargo != null && !id_ServentiaCargo.equals("")) {
            	liTemp = obPersistencia.consultarQuantidadeConclusoesPreAnalisadas(id_ServentiaCargo, false, somenteSessao, ehAssistente, ehVotoVencido);
            }
        } finally {
            obFabricaConexao.fecharConexao();
        }
        return liTemp;
    }
    
    public List consultarQuantidadeConclusoesPreAnalisadasVirtual(UsuarioNe usuarioSessao, boolean somenteSessao, boolean ehVotoVencido, boolean isIniciada) throws Exception {
    	return consultarQuantidadeConclusoesPreAnalisadasVirtual(usuarioSessao, somenteSessao, ehVotoVencido, isIniciada, false);
    }
     /**
     * Consulta a quantidade de pendencias do tipo conclus�o que s�o virtuais e est�o pr�-analisadas
     * 
     * @param usuarioSessao, usuario que est� realizando a consulta
     * @param somenteSessao, filtro por sessao
     * @param ehVotoVencido, filtro por voto vencido
     * @param isIniciada, filtro para sess�o iniciada
     * @author lrcampos
	 * @since 14/10/2019 15:30
     */

    public List consultarQuantidadeConclusoesPreAnalisadasVirtual(UsuarioNe usuarioSessao, boolean somenteSessao, boolean ehVotoVencido, boolean isIniciada, boolean isBoxSessaoVirtualPresencial) throws Exception {
    	List liTemp = new ArrayList();
        String id_ServentiaCargo = null;
        FabricaConexao obFabricaConexao = null;
        boolean ehAssistente = false;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            //switch (Funcoes.StringToInt(usuarioDt.getGrupoCodigo())) {
           switch (Funcoes.StringToInt(usuarioSessao.getUsuarioDt().getGrupoTipoCodigo())) {
           case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
           case GrupoTipoDt.JUIZ_TURMA:
           case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
           case GrupoTipoDt.PRESIDENTE_SEGUNDO_GRAU:           
                id_ServentiaCargo = usuarioSessao.getUsuarioDt().getId_ServentiaCargo();
                break;           
           
           case GrupoTipoDt.ASSISTENTE_GABINETE:
           case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
                id_ServentiaCargo = usuarioSessao.getUsuarioDt().getId_ServentiaCargo();
                ehAssistente = true;
                break;

           case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
           case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
           case GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:
                id_ServentiaCargo = usuarioSessao.getUsuarioDt().getId_ServentiaCargoUsuarioChefe();
                //ehAssistente = true;
                break;
            }
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            if (id_ServentiaCargo != null && !id_ServentiaCargo.equals("")) {
            	String idPendTipo = new PendenciaTipoNe().consultarPendenciaTipoCodigo(PendenciaTipoDt.CONCLUSO_VOTO).getId();
            	if(isBoxSessaoVirtualPresencial)
            		liTemp = obPersistencia.consultarQuantidadeConclusoesPreAnalisadasBoxPresencialPJD(id_ServentiaCargo, somenteSessao, ehAssistente, ehVotoVencido, idPendTipo);
            	else 
            		liTemp = obPersistencia.consultarQuantidadeConclusoesPreAnalisadasVirtual(usuarioSessao, id_ServentiaCargo, "", "", "", idPendTipo, true, ehVotoVencido, isIniciada);
            }
        } finally {
            obFabricaConexao.fecharConexao();
        }
        return liTemp;
    }
    
 /**
     * Consulta a quantidade de pendencias do tipo conclus�o que s�o virtuais e est�o Aguardando Inicio da sess�o
     * 
     * @param usuarioSessao, usuario que est� realizando a consulta
     * @param somenteSessao, filtro por sessao
     * @param ehVotoVencido, filtro por voto vencido
     * @param isIniciada, filtro para sess�o iniciada
     * @author lrcampos
	 * @since 14/10/2019 15:30
     */

    public List consultarQuantidadeConclusoesAguardandoInicioSessaoVirtual(UsuarioNe usuarioSessao, boolean somenteSessao, boolean ehVotoVencido, boolean isIniciada) throws Exception {
    	List liTemp = new ArrayList();
        String id_ServentiaCargo = null;
        FabricaConexao obFabricaConexao = null;
        boolean ehAssistente = false;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            //switch (Funcoes.StringToInt(usuarioDt.getGrupoCodigo())) {
           switch (Funcoes.StringToInt(usuarioSessao.getUsuarioDt().getGrupoTipoCodigo())) {
           case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
           case GrupoTipoDt.JUIZ_TURMA:
           case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
           case GrupoTipoDt.PRESIDENTE_SEGUNDO_GRAU:           
                id_ServentiaCargo = usuarioSessao.getUsuarioDt().getId_ServentiaCargo();
                break;           
           
           case GrupoTipoDt.ASSISTENTE_GABINETE:
           case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
                id_ServentiaCargo = usuarioSessao.getUsuarioDt().getId_ServentiaCargoUsuarioChefe();
                ehAssistente = true;
                break;

           case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
           case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
           case GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:
                id_ServentiaCargo = usuarioSessao.getUsuarioDt().getId_ServentiaCargoUsuarioChefe();
                //ehAssistente = true;
                break;
            }
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            if (id_ServentiaCargo != null && !id_ServentiaCargo.equals("")) {
            	liTemp = obPersistencia.consultarQuantidadeConclusoesAguardandoInicioSessaoVirtual(usuarioSessao, id_ServentiaCargo, "", "", "", true, ehVotoVencido);
            }
        } finally {
            obFabricaConexao.fecharConexao();
        }
        return liTemp;
    }
    
    /**
     * Consulta a quantidade de pr�-an�lises simples de conclus�es por tipo
     * 
     * @author msapaula
     * @param usuarioDt, dt de usuario
     */
    public List consultarQuantidadeConclusoesPreAnalisadasAssistenteGabinete(UsuarioDt usuarioDt, boolean somenteSessao, boolean ehVotoVencido) throws Exception {
    	List liTemp = new ArrayList();
        String id_ServentiaCargo = null;
        FabricaConexao obFabricaConexao = null;
        boolean ehAssistente = false;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
           
            id_ServentiaCargo = usuarioDt.getId_ServentiaCargo();
            ehAssistente = true;
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            if (id_ServentiaCargo != null && !id_ServentiaCargo.equals("")) {
            	liTemp = obPersistencia.consultarQuantidadeConclusoesPreAnalisadasAssistenteGabinete(id_ServentiaCargo, false, somenteSessao, ehAssistente, ehVotoVencido);
            }
        } finally {
            obFabricaConexao.fecharConexao();
        }
        return liTemp;
    }
    
    /**
     * Consulta a quantidade de pr�-an�lises simples de conclus�es por tipo
     * pendentes de assinatura
     * @author mmgomes
     * @param usuarioDt, dt de usuario
     * @param ehVotoVencido, indicador de consulta de voto vencido
     */
    public List consultarQuantidadeConclusoesPreAnalisadasPendentesAssinatura(UsuarioDt usuarioDt, boolean ehVotoVencido) throws Exception {
    	List liTemp = new ArrayList();
        String id_ServentiaCargo = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            //switch (Funcoes.StringToInt(usuarioDt.getGrupoCodigo())) {
           switch (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo())) {
//            case GrupoDt.JUIZES_VARA:
//            case GrupoDt.JUIZES_TURMA_RECURSAL:
//            case GrupoDt.DESEMBARGADOR:
//            case GrupoDt.ASSISTENTES_GABINETE:
           case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
           case GrupoTipoDt.JUIZ_TURMA:
           case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
           case GrupoTipoDt.PRESIDENTE_SEGUNDO_GRAU:
           case GrupoTipoDt.ASSISTENTE_GABINETE:
           case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
                id_ServentiaCargo = usuarioDt.getId_ServentiaCargo();
                break;
			case GrupoTipoDt.JUIZ_LEIGO:
				
	            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());				
				String id_Serventia = usuarioDt.getId_Serventia();
		        List listaServentiaCargo = new ArrayList();

		        listaServentiaCargo = obPersistencia.consultarJuizesServentia(id_Serventia);
				
		        if (listaServentiaCargo != null){
		        	for(int i = 0; i < listaServentiaCargo.size(); i++) {
		            	liTemp.addAll(obPersistencia.consultarQuantidadeConclusoesPreAnalisadasPendentesAssinatura((String)listaServentiaCargo.get(i),  false, ehVotoVencido));
			        }
		        }
                
                
				break;
//            case GrupoDt.ASSISTENTES_JUIZES_VARA:
//            case GrupoDt.ASSISTENTES_JUIZES_SEGUNDO_GRAU:
           case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
           case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
           case GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:
                id_ServentiaCargo = usuarioDt.getId_ServentiaCargoUsuarioChefe();
                break;
            }
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            if (id_ServentiaCargo != null && !id_ServentiaCargo.equals("")) {
            	liTemp = obPersistencia.consultarQuantidadeConclusoesPreAnalisadasPendentesAssinatura(id_ServentiaCargo, false, ehVotoVencido);
            }
        } finally {
            obFabricaConexao.fecharConexao();
        }
        return liTemp;
    }
    
    /**
     * Consulta a quantidade de pr�-an�lises simples de pend�ncias por tipo
     * 
     * @author msapaula
     * @param usuarioDt, dt de usuario
     */
    public List consultarQuantidadePendenciasPreAnalisadas(UsuarioDt usuarioDt) throws Exception {
    	List liTemp = new ArrayList();
    	FabricaConexao obFabricaConexao = null;
    	try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            
            //switch (Funcoes.StringToInt(usuarioDt.getGrupoCodigo())) {
            switch (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo())) {
//            case GrupoDt.JUIZES_VARA:
//            case GrupoDt.DESEMBARGADOR:
//            case GrupoDt.ASSISTENTES_GABINETE:
//            case GrupoDt.PROMOTORES:
            case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
            case GrupoTipoDt.JUIZ_TURMA:
            case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
            case GrupoTipoDt.ASSISTENTE_GABINETE:
            case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
            case GrupoTipoDt.MP:
                if (usuarioDt.getId_ServentiaCargo() != null && !usuarioDt.getId_ServentiaCargo().equals("")) {
                	liTemp = obPersistencia.consultarQuantidadePendenciasPreAnalisadas(usuarioDt.getId_ServentiaCargo(), null, false);
                }
                break;
                
           // case GrupoDt.ADVOGADOS:
            case GrupoTipoDt.ADVOGADO:
             	liTemp = obPersistencia.consultarQuantidadePendenciasPreAnalisadas(null, usuarioDt.getId_UsuarioServentia(), false);
             	break;
                
             	
            //case GrupoDt.ASSISTENTES_JUIZES_VARA:
            case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
            case GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:
            case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
            	if (usuarioDt.getId_ServentiaCargoUsuarioChefe() != null && !usuarioDt.getId_ServentiaCargoUsuarioChefe().equals("")) {
					liTemp = obPersistencia.consultarQuantidadePendenciasPreAnalisadas(usuarioDt.getId_ServentiaCargoUsuarioChefe(), null, false);
				}
				break;
             	
            //case GrupoDt.ASSISTENTES_ADVOGADOS_PROMOTORES:
            case GrupoTipoDt.ASSESSOR_MP:
				if (usuarioDt.getId_ServentiaCargoUsuarioChefe() != null && !usuarioDt.getId_ServentiaCargoUsuarioChefe().equals("")) {
						liTemp = obPersistencia.consultarQuantidadePendenciasPreAnalisadas(usuarioDt.getId_ServentiaCargoUsuarioChefe(), null, false);
				}
				break;            	
            case GrupoTipoDt.ASSESSOR_ADVOGADO:
            	if (usuarioDt.getGrupoUsuarioChefe() != null && usuarioDt.getGrupoUsuarioChefe().length() > 0) { 				
 					liTemp = obPersistencia.consultarQuantidadePendenciasPreAnalisadas(null, usuarioDt.getId_UsuarioServentiaChefe(), false);
            	}
            	break;
            }
           
        } finally {
            obFabricaConexao.fecharConexao();
        }
        return liTemp;
    }
    
    /**
     * Consulta a quantidade de pr�-an�lises simples de pend�ncias por tipo e pendentes de assinatura
     * 
     * @author mmgomes
     * @param usuarioDt, dt de usuario
     */
    public List consultarQuantidadePendenciasPreAnalisadasPendentesAssinatura(UsuarioDt usuarioDt) throws Exception {
    	List liTemp = new ArrayList();
    	FabricaConexao obFabricaConexao = null;
    	try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            
            //switch (Funcoes.StringToInt(usuarioDt.getGrupoCodigo())) {
            switch (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo())) {
//            case GrupoDt.JUIZES_VARA:
//            case GrupoDt.DESEMBARGADOR:
//            case GrupoDt.ASSISTENTES_GABINETE:
//            case GrupoDt.MINISTERIO_PUBLICO:
            case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
            case GrupoTipoDt.JUIZ_TURMA:
            case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
            case GrupoTipoDt.ASSISTENTE_GABINETE:
            case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
            case GrupoTipoDt.MP:
                if (usuarioDt.getId_ServentiaCargo() != null && !usuarioDt.getId_ServentiaCargo().equals("")) {
                	liTemp = obPersistencia.consultarQuantidadePendenciasPreAnalisadasPendentesAssinatura(usuarioDt.getId_ServentiaCargo(), null, false);
                }
                break;
                
           // case GrupoDt.ADVOGADOS:
            case GrupoTipoDt.ADVOGADO:
             	liTemp = obPersistencia.consultarQuantidadePendenciasPreAnalisadasPendentesAssinatura(null, usuarioDt.getId_UsuarioServentia(), false);
             	break;
                
             	
            //case GrupoDt.ASSISTENTES_JUIZES_VARA:
            case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
            case GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:
            case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
            	if (usuarioDt.getId_ServentiaCargoUsuarioChefe() != null && !usuarioDt.getId_ServentiaCargoUsuarioChefe().equals("")) {
					liTemp = obPersistencia.consultarQuantidadePendenciasPreAnalisadasPendentesAssinatura(usuarioDt.getId_ServentiaCargoUsuarioChefe(), null, false);
				}
				break;
             	
            //case GrupoDt.ASSISTENTES_ADVOGADOS_PROMOTORES:
            case GrupoTipoDt.ASSESSOR_MP:
            	if (usuarioDt.getId_ServentiaCargoUsuarioChefe() != null && !usuarioDt.getId_ServentiaCargoUsuarioChefe().equals("")) {
            		liTemp = obPersistencia.consultarQuantidadePendenciasPreAnalisadasPendentesAssinatura(usuarioDt.getId_ServentiaCargoUsuarioChefe(), null, false);
            	}
            	break;            
            case GrupoTipoDt.ASSESSOR_ADVOGADO:
            	if (usuarioDt.getGrupoUsuarioChefe() != null && usuarioDt.getGrupoUsuarioChefe().length() > 0) {
            		liTemp = obPersistencia.consultarQuantidadePendenciasPreAnalisadasPendentesAssinatura(null, usuarioDt.getId_UsuarioServentiaChefe(), false);
             	}
            	break;
            }
           
        } finally {
            obFabricaConexao.fecharConexao();
        }
        return liTemp;
    }

    /**
     * Consulta a quantidade de pr�-an�lises m�ltiplas registradas para pendencias do tipo Conclus�o
     * 
     * @author msapaula
     * @param usuarioDt, dt de usuario
     */
    public int consultarQuantidadeConclusoesPreAnalisadasMultipla(UsuarioDt usuarioDt) throws Exception {
        int qtd = 0;
        String id_ServentiaCargo = null;
        FabricaConexao obFabricaConexao = null;        
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            //switch (Funcoes.StringToInt(usuarioDt.getGrupoCodigo())) {
            switch (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo())) {
//            case GrupoDt.JUIZES_VARA:
//            case GrupoDt.JUIZES_TURMA_RECURSAL:
//            case GrupoDt.DESEMBARGADOR:
            case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
            case GrupoTipoDt.JUIZ_TURMA:
            case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
            case GrupoTipoDt.ASSISTENTE_GABINETE:
            case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
                id_ServentiaCargo = usuarioDt.getId_ServentiaCargo();
                break;

//            case GrupoDt.ASSISTENTES_JUIZES_VARA:
//            case GrupoDt.ASSISTENTES_JUIZES_SEGUNDO_GRAU:
            case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
            case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
            case GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:
                id_ServentiaCargo = usuarioDt.getId_ServentiaCargoUsuarioChefe();             
                break;
            }
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            if (id_ServentiaCargo != null && !id_ServentiaCargo.equals("")) {
                List liTemp = obPersistencia.consultarQuantidadeConclusoesPreAnalisadas(id_ServentiaCargo, true, false, false, false);
                for (int i = 0; i < liTemp.size(); i++) {
                	qtd += Funcoes.StringToInt(((String[])liTemp.get(i))[2]);
				}
            }
        } finally {
            obFabricaConexao.fecharConexao();
        }
        return qtd;
    }
    
    /**
     * Consulta a quantidade de pr�-an�lises m�ltiplas registradas para outras pend�ncias que podem ser analisadas
     * 
     * @author msapaula
     * @param usuarioDt, dt de usuario
     */
    public int consultarQuantidadePendenciasPreAnalisadasMultipla(UsuarioDt usuarioDt) throws Exception {
        int qtd = 0;
        List liTemp = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            
            //switch (Funcoes.StringToInt(usuarioDt.getGrupoCodigo())) {
            switch (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo())) {
//            	case GrupoDt.JUIZES_VARA:
//            	case GrupoDt.DESEMBARGADOR:
//                case GrupoDt.ASSISTENTES_GABINETE:
//                case GrupoDt.MINISTERIO_PUBLICO:
            case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
            case GrupoTipoDt.JUIZ_TURMA:
            case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
            case GrupoTipoDt.ASSISTENTE_GABINETE:
            case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
            case GrupoTipoDt.MP:
                	if (usuarioDt.getId_ServentiaCargo() != null && !usuarioDt.getId_ServentiaCargo().equals("")) {
                		liTemp = obPersistencia.consultarQuantidadePendenciasPreAnalisadas(usuarioDt.getId_ServentiaCargo(), null, true);
                    }
                break;
                
            // case GrupoDt.ADVOGADOS:
            case GrupoTipoDt.ADVOGADO:
                	liTemp = obPersistencia.consultarQuantidadePendenciasPreAnalisadas(null, usuarioDt.getId_UsuarioServentia(), true);
                break;
                
            //case GrupoDt.ASSISTENTES_JUIZES_VARA:
            case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
            case GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:
            case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
                	if (usuarioDt.getId_ServentiaCargoUsuarioChefe() != null && !usuarioDt.getId_ServentiaCargoUsuarioChefe().equals("")) {
    					liTemp = obPersistencia.consultarQuantidadePendenciasPreAnalisadas(usuarioDt.getId_ServentiaCargoUsuarioChefe(), null, true);
    				}
    				break;
                
    	    //case GrupoDt.ASSISTENTES_ADVOGADOS_PROMOTORES:
            case GrupoTipoDt.ASSESSOR_MP:
            	if (usuarioDt.getId_ServentiaCargoUsuarioChefe() != null && !usuarioDt.getId_ServentiaCargoUsuarioChefe().equals("")) {
            		liTemp = obPersistencia.consultarQuantidadePendenciasPreAnalisadas(usuarioDt.getId_ServentiaCargoUsuarioChefe(), null, true);
            	}
            	break;            	
            case GrupoTipoDt.ASSESSOR_ADVOGADO:
                	if (usuarioDt.getGrupoUsuarioChefe() != null && usuarioDt.getGrupoUsuarioChefe().length() > 0) {     					
     					liTemp = obPersistencia.consultarQuantidadePendenciasPreAnalisadas(null, usuarioDt.getId_UsuarioServentiaChefe(), true);
                 	}
                break;
            }

            if (liTemp != null && liTemp.size() > 0) qtd = Funcoes.StringToInt(((String[])liTemp.get(0))[2]);
        } finally {
            obFabricaConexao.fecharConexao();
        }
        return qtd;
    }

    /**
     * Consulta Cargo de autoridade policial respons�vel pelo processo
     * 
     * @param String, id_Processo
     * @return serventia cargo de autoridade policial
     */
    public ServentiaCargoDt consultaAutoridadePolicialResponsavelProcesso(String id_Processo) throws Exception {
        ProcessoResponsavelNe processoResponsavelNe = new ProcessoResponsavelNe();
        ServentiaCargoDt serventiaCargoDt = null;
        serventiaCargoDt = processoResponsavelNe.getAutoridadePolicialResponsavelProcesso(id_Processo, null);
        return serventiaCargoDt;
    }

    /**
     * Verifica se a pend�ncia � do tipo "expedir"
     * 
     * @param pendenciaDt
     * @return verdadeiro ou falso
     */
    private boolean pendenciaTipoExpedir(PendenciaDt pendenciaDt) {
        boolean retorno = false;
        Integer tipoPendencia = pendenciaDt.getPendenciaTipoCodigoToInt();
        if (!pendenciaDt.getPendenciaStatusCodigo().equals(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_RETORNO)) 
        		&& !pendenciaDt.getPendenciaStatusCodigo().equals(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO))
        	) {
            if (tipoPendencia != PendenciaTipoDt.INTIMACAO && tipoPendencia != PendenciaTipoDt.CARTA_CITACAO
            		&& tipoPendencia != PendenciaTipoDt.INTIMACAO_AUDIENCIA && tipoPendencia != PendenciaTipoDt.CARTA_CITACAO_AUDIENCIA 
            		&& pendenciaDt.isPendenciaDeProcesso()) {
            	retorno = true;
            }
        }

        return retorno;
    }

    /**
     * Gera uma pend�ncia filha ao expedir uma pend�ncia
     * 
     * @param pendenciaDt, pend�ncia que ser� gerada
     * @param serventiaDt, serventia destino
     * @param UsuarioDt, UsuarioDt para montar o log
     * @param obFabricaConexao, conex�o ativa
     * 
     * @author lsbernardes
     */
    public PendenciaDt gerarPendenciaFilhaExpedir(PendenciaDt pendenciaDt, ServentiaDt serventiaDt, UsuarioDt usuarioDt, FabricaConexao obFabricaConexao) throws Exception {
        PendenciaDt novaPendenciaDt = null;

        // Inseri uma copia
        novaPendenciaDt = this.inserirFilhaExpedir(pendenciaDt, obFabricaConexao);

        // Configura o responsavel
        PendenciaResponsavelDt pendenciaResponsavelDt = new PendenciaResponsavelDt();
        pendenciaResponsavelDt.setId_Pendencia(novaPendenciaDt.getId());

        // Pela logica de responsabilidade
        pendenciaResponsavelDt.setId_Serventia(serventiaDt.getId());

        pendenciaResponsavelDt.setId_UsuarioLog(usuarioDt.getId());
        pendenciaResponsavelDt.setIpComputadorLog(usuarioDt.getIpComputadorLog());

        // Inseri os responsaveis da pendencia
        PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
        pendenciaResponsavelNe.inserir(pendenciaResponsavelDt, obFabricaConexao);

        // Consulta arquivos de resposta assinados para vincular com a
        // pendencia filha
        this.vincularArquivosResposta(pendenciaDt, novaPendenciaDt, obFabricaConexao);
        
        return novaPendenciaDt;
    }
    
    /**
     * Gera uma pendencia filha pedido de manifesta��o
     * 
     * @param pendenciaDt, pend�ncia que ser� gerada
     * @param UsuarioDt, responsavel pela pend�ncia
     * @param obFabricaConexao, conex�o ativa
     * 
     * @author lsbernardes
     */
    public PendenciaDt gerarPendenciaFilhaManifestacao(PendenciaDt pendenciaDt, UsuarioDt usuarioDt, FabricaConexao obFabricaConexao) throws Exception {
        PendenciaDt novaPendenciaDt = null;

        try {
            PendenciaDt pendenciaRetornoDt = pendenciaDt.criarFilha();
            PendenciaResponsavelDt responsavel = new PendenciaResponsavelDt();
            int GrupoCodigo = Funcoes.StringToInt(usuarioDt.getGrupoCodigo());
            if ( GrupoCodigo== GrupoDt.MINISTERIO_PUBLICO ||  GrupoCodigo== GrupoDt.MP_TCE) {
	            responsavel.setId_ServentiaCargo(usuarioDt.getId_ServentiaCargo());
	            pendenciaRetornoDt.addResponsavel(responsavel);
            } else if (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ADVOGADO){
            	 responsavel.setId_UsuarioResponsavel(usuarioDt.getId_UsuarioServentia());
 	            pendenciaRetornoDt.addResponsavel(responsavel);
            } else {
            	throw new MensagemException("Respons�vel n�o encontrado.");
            }

            // Limpa os dados e atribui o novo tipo
            pendenciaRetornoDt.setId_PendenciaStatus("null");
            pendenciaRetornoDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_EM_ANDAMENTO));
            pendenciaRetornoDt.setId_PendenciaTipo("null");
            pendenciaRetornoDt.setPendenciaTipoCodigo(String.valueOf(PendenciaTipoDt.PEDIDO_MANIFESTACAO));
            pendenciaRetornoDt.setId_UsuarioCadastrador("null");
            pendenciaRetornoDt.setId_UsuarioCadastrador(usuarioDt.getId_UsuarioServentia());
            pendenciaRetornoDt.setId_UsuarioFinalizador("null");
            pendenciaRetornoDt.setId_ProcessoPrioridade("1");
            pendenciaRetornoDt.setDataLimite("");
            pendenciaRetornoDt.setPrazo("");

            pendenciaRetornoDt.setId_UsuarioLog(usuarioDt.getId());
            pendenciaRetornoDt.setIpComputadorLog(usuarioDt.getIpComputadorLog());

            this.gerarPendenciaFilha(pendenciaRetornoDt, pendenciaRetornoDt.getResponsaveis(), null, null, false, false, false, obFabricaConexao);

        } catch (Exception e) {
            throw new MensagemException("Erro ao gerar pend�ncia filha Pedido de Manifesta��o.");
        }
        return novaPendenciaDt;
    }
    
    /**
     * Gera uma pendencia filha ao pr�-anlisar precat�ria
     * 
     * @param pendenciaDt, pend�ncia que ser� gerada
     * @param UsuarioDt, UsuarioDt para pegar o respons�vel
     * @param obFabricaConexao, conex�o ativa
     * 
     * @author lsbernardes
     */
    public PendenciaDt gerarPendenciaFilhaPreAnaliseCartaPrecatoira(PendenciaDt pendenciaDt, UsuarioDt usuarioDt, FabricaConexao obFabricaConexao) throws Exception {
    	PendenciaDt pendenciaRetornoDt = null;

    	pendenciaRetornoDt = pendenciaDt.criarFilhaPreAnalise(pendenciaDt);
        PendenciaResponsavelDt responsavel = new PendenciaResponsavelDt();
        
        //if (Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.ASSISTENTES_JUIZES_VARA) {
        if (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA
        		|| Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU 
        		|| Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSESSOR_DESEMBARGADOR) {
            responsavel.setId_ServentiaCargo(usuarioDt.getId_ServentiaCargoUsuarioChefe());
            pendenciaRetornoDt.addResponsavel(responsavel);
        } else {
        	throw new MensagemException("Respons�vel n�o encontrado.");
        }

        // Limpa os dados e atribui o novo tipo
        pendenciaRetornoDt.setId_PendenciaStatus("null");
        pendenciaRetornoDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_EM_ANDAMENTO));
        pendenciaRetornoDt.setId_PendenciaTipo("null");
        pendenciaRetornoDt.setPendenciaTipoCodigo(String.valueOf(PendenciaTipoDt.PRE_ANALISE_PRECATORIA));
        pendenciaRetornoDt.setId_UsuarioCadastrador("null");
        pendenciaRetornoDt.setId_UsuarioCadastrador(usuarioDt.getId_UsuarioServentia());
        pendenciaRetornoDt.setId_UsuarioFinalizador("null");
        pendenciaRetornoDt.setId_ProcessoPrioridade("1");
        pendenciaRetornoDt.setDataLimite("");
        pendenciaRetornoDt.setPrazo("");

        pendenciaRetornoDt.setId_UsuarioLog(usuarioDt.getId());
        pendenciaRetornoDt.setIpComputadorLog(usuarioDt.getIpComputadorLog());

        this.gerarPendenciaFilha(pendenciaRetornoDt, pendenciaRetornoDt.getResponsaveis(), null, null, false, false, false, obFabricaConexao);
        
        return pendenciaRetornoDt;
    }
    
    /**
     * Gera uma pendencia filha ao distribuir carta precat�ria
     * 
     * @param pendenciaDt, pend�ncia que ser� gerada
     * @param id_ServentiaCargo,  responsavel pela pend�ncia
     * @param UsuarioDt, UsuarioDt para montar o log
     * @param logDt, dados do log
     * @param obFabricaConexao, conex�o ativa
     * 
     * @author lsbernardes
     */
    public PendenciaDt gerarPendenciaFilhaDistribuirCartaPrecatoria(PendenciaDt pendenciaDt, String id_ServentiaCargo, UsuarioDt usuarioDt, FabricaConexao obFabricaConexao) throws Exception {
        PendenciaDt novaPendenciaDt = null;

        // Inseri uma copia
        novaPendenciaDt = this.inserirFilhaExpedir(pendenciaDt, obFabricaConexao);

        // Configura o responsavel
        PendenciaResponsavelDt pendenciaResponsavelDt = new PendenciaResponsavelDt();
        pendenciaResponsavelDt.setId_Pendencia(novaPendenciaDt.getId());

        // Pela logica de responsabilidade
        pendenciaResponsavelDt.setId_ServentiaCargo(id_ServentiaCargo);

        pendenciaResponsavelDt.setId_UsuarioLog(usuarioDt.getId());
        pendenciaResponsavelDt.setIpComputadorLog(usuarioDt.getIpComputadorLog());

        // Inseri os responsaveis da pendencia
        PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
        pendenciaResponsavelNe.inserir(pendenciaResponsavelDt, obFabricaConexao);

        // Consulta arquivos de resposta assinados para vincular com a
        // pendencia filha
        this.vincularArquivosResposta(pendenciaDt, novaPendenciaDt, obFabricaConexao);
        
        return novaPendenciaDt;
    }

    /**
     * Salva uma pend�ncia do tipo Pedido C�lculo para uma Contadoria. Ap�s
     * obter a serventia do tipo Contadoria relacionada com a serventia do
     * processo, deve adicionar essa como respons�vel pela pend�ncia.
     * 
     * @param pendenciaDt, pend�ncia que ser� gerada
     * @param processoDt, processo vinculado a pend�ncia
     * @param responsavel, respons�vel pela pend�ncia
     * @param arquivos, arquivos vinculados a pend�ncia que ser� gerada
     * @param logDt, dados do log
     * @param obFabricaConexao, conex�o ativa
     * 
     * @author msapaula
     */
    private void gerarPendenciaPedidoCalculoContadoria(PendenciaDt pendenciaDt, ProcessoDt processoDt, PendenciaResponsavelDt responsavel, List arquivos, FabricaConexao obFabricaConexao) throws Exception {

        ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();
        ServentiaDt contadoriaRelacionada = serventiaRelacionadaNe.consultarContadoriaRelacionada(processoDt.getId_Serventia());

        if (contadoriaRelacionada != null) {
            // Se encontrou uma serventia relacionada, essa ser� a respons�vel pela pend�ncia
            responsavel.setId_Serventia(contadoriaRelacionada.getId());

            PendenciaStatusNe pendenciaStatusNe = new PendenciaStatusNe();
            PendenciaStatusDt pendenciaStatusDt = pendenciaStatusNe.consultarPendenciaStatusCodigo(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO);
            pendenciaDt = pendenciaDt.criarPendenciaFilhaExpedida();
            pendenciaDt.setId_PendenciaStatus(pendenciaStatusDt.getId());
            pendenciaDt.setDataVisto("");

        } else {
            // Se n�o h� nenhuma contadoria relacionada, a pend�ncia ser� gerada
            // para a serventia do processo expedir
            responsavel.setId_Serventia(processoDt.getId_Serventia());
        }

        pendenciaDt.addResponsavel(responsavel);
        this.gerarPendencia(pendenciaDt, arquivos, false, obFabricaConexao);

    }
    
    /**
     * Salva uma pend�ncia do tipo Pedido CENOPES para uma Undiade administrativa. Ap�s
     * obter a serventia do tipo Unidade Administrativa relacionada com a serventia do
     * processo, deve adicionar essa como respons�vel pela pend�ncia.
     * 
     * @param pendenciaDt, pend�ncia que ser� gerada
     * @param processoDt, processo vinculado a pend�ncia
     * @param responsavel, respons�vel pela pend�ncia
     * @param arquivos, arquivos vinculados a pend�ncia que ser� gerada
     * @param logDt, dados do log
     * @param obFabricaConexao, conex�o ativa
     * 
     * @author lsbernardes
     */
    private void gerarPendenciaPedidoCENOPES(PendenciaDt pendenciaDt, ProcessoDt processoDt, PendenciaResponsavelDt responsavel, List arquivos, FabricaConexao obFabricaConexao) throws Exception {

        ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();
        ServentiaDt CENOPESRelacionado = serventiaRelacionadaNe.consultarUnidadeAdministrativaRelacionadaCENOPES(processoDt.getId_Serventia());

        if (CENOPESRelacionado != null) {
        	if (!this.possuiPedidoCENOPES(processoDt.getId(), obFabricaConexao)){
	        	ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
	        	String id_ServentiaCargoConsultor = serventiaCargoNe.consultarServentiaCargoDistribuicao(CENOPESRelacionado.getId(), String.valueOf(PendenciaTipoDt.PEDIDO_CENOPES), obFabricaConexao);
	            
	        	if (id_ServentiaCargoConsultor != null && id_ServentiaCargoConsultor.length()>0){
	        		responsavel.setId_ServentiaCargo(id_ServentiaCargoConsultor);
	                PendenciaStatusNe pendenciaStatusNe = new PendenciaStatusNe();
	                PendenciaStatusDt pendenciaStatusDt = pendenciaStatusNe.consultarPendenciaStatusCodigo(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO);
	                pendenciaDt = pendenciaDt.criarPendenciaFilhaExpedida();
	                pendenciaDt.setId_PendenciaStatus(pendenciaStatusDt.getId());
	                pendenciaDt.setDataVisto("");
	        	} else {
	        		// Se n�o h� nenhum cargo disponivel na serventia CENOPES relacionada a pend�ncia N�O poder� ser gerada sem responsavel
	            	throw new MensagemException("A pend�ncia Pedido CENOPES n�o pode ser gerada, pois n�o h� cargos dispon�veis na serventia.");
	        	}
        	} else {
        		// Se j� possui um pedido CENOPES para o processo, n�o permite gerar a pend�ncia
            	throw new MensagemException("A pend�ncia Pedido CENOPES n�o pode ser gerada, pois o processo j� possui um Pedido CENOPES aberto.");
        	}
        } else {
            // Se n�o h� nenhuma CENOPES relacionada a pend�ncia N�O poder� ser gerada, defini��o do Gerenciamento de Sistemas
        	throw new MensagemException("A pend�ncia Pedido CENOPES n�o pode ser gerada, pois n�o h� serventia relacionada para esse fim.");
        }

        pendenciaDt.addResponsavel(responsavel);
        this.gerarPendencia(pendenciaDt, arquivos, false, obFabricaConexao);

    }
    
    /**
     * Verifica se o processo j� possui uma pendencia do tipo pedido CENOPES
     * 
     * @author Leandro Bernardes
     * @param  id_Processo, identificado do processo 
     * @param obFabricaConexao, conex�o ativa
     * @return boolean
     */
    private boolean possuiPedidoCENOPES(String id_Processo, FabricaConexao obFabricaConexao) throws Exception {
		PendenciaPs obPersistencia = new PendenciaPs(obFabricaConexao.getConexao());
		return obPersistencia.possuiPedidoCENOPES(id_Processo);
    }
    
    /**
     * Salva uma pend�ncia do tipo Pedido C�mara de Sa�de para uma C�mara de Sa�de. Ap�s
     * obter a serventia do tipo C�mara de Sa�de relacionada com a serventia do
     * processo, deve adicionar essa como respons�vel pela pend�ncia.
     * 
     * @param pendenciaDt, pend�ncia que ser� gerada
     * @param processoDt, processo vinculado a pend�ncia
     * @param responsavel, respons�vel pela pend�ncia
     * @param arquivos, arquivos vinculados a pend�ncia que ser� gerada
     * @param logDt, dados do log
     * @param obFabricaConexao, conex�o ativa
     * 
     * 
     */
    private void gerarPendenciaPedidoCamaraSaude(PendenciaDt pendenciaDt, ProcessoDt processoDt, PendenciaResponsavelDt responsavel, List arquivos, FabricaConexao obFabricaConexao) throws Exception {

        ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();
        ServentiaDt camaraSaudeRelacionada = serventiaRelacionadaNe.consultarCamaraSaudeRelacionada(processoDt.getId_Serventia());

        if (camaraSaudeRelacionada != null) {
            // Se encontrou uma serventia relacionada, essa ser� a respons�vel
            // pela pend�ncia
            responsavel.setId_Serventia(camaraSaudeRelacionada.getId());

            PendenciaStatusNe pendenciaStatusNe = new PendenciaStatusNe();
            PendenciaStatusDt pendenciaStatusDt = pendenciaStatusNe.consultarPendenciaStatusCodigo(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO);
            pendenciaDt = pendenciaDt.criarPendenciaFilhaExpedida();
            pendenciaDt.setId_PendenciaStatus(pendenciaStatusDt.getId());
            pendenciaDt.setDataVisto("");

        } else {
            // Se n�o h� nenhuma contadoria relacionada, a pend�ncia ser� gerada
            // para a serventia do processo expedir
            responsavel.setId_Serventia(processoDt.getId_Serventia());
        }

        pendenciaDt.addResponsavel(responsavel);
        this.gerarPendencia(pendenciaDt, arquivos, false, obFabricaConexao);

    }

    private void gerarPendenciaIntimacaoMinisterioPublicoPrimeiroSegundoGrau(PendenciaDt pendenciaDt, ProcessoDt processoDt, PendenciaResponsavelDt responsavel, List arquivos, UsuarioDt usuarioDt, String serventiaSubTipoCodigo, boolean intimacaoAudiencia, LogDt logDt, FabricaConexao obFabricaConexao) throws MensagemException, Exception {
    	 String id_serventia_Gabinete = null;
         if (processoDt != null && processoDt.isSigiloso() && usuarioDt != null && usuarioDt.isGabineteUPJ()) {
         	id_serventia_Gabinete = usuarioDt.getId_Serventia();
         }
    	this.gerarPendenciaIntimacaoMinisterioPublicoPrimeiroSegundoGrau(pendenciaDt, processoDt, responsavel, arquivos, processoDt.getId_Serventia(), serventiaSubTipoCodigo, intimacaoAudiencia, id_serventia_Gabinete, logDt, obFabricaConexao);
    }
    
    /**
     * Salva uma pend�ncia do tipo Intima��o para o Minist�rio P�blico. Ap�s
     * obter a serventia do tipo Promotoria relacionada com a serventia do
     * processo, deve distribuir o processo para um promotor espec�fico e
     * adicionar esse como respons�vel pelo processo e pela pend�ncia.
     * 
     * @param pendenciaDt,
     *            pend�ncia que ser� gerada
     * @param processoDt,
     *            processo vinculado a pend�ncia
     * @param responsavel,
     *            respons�vel pela pend�ncia
     * @param arquivos,
     *            arquivos vinculados a pend�ncia que ser� gerada
     * @param logDt,
     *            ddos do log
     * @param obFabricaConexao,
     *            conex�o ativa
     * 
     * @author msapaula\leandro
     */
    private void gerarPendenciaIntimacaoMinisterioPublicoPrimeiroSegundoGrau(PendenciaDt pendenciaDt, ProcessoDt processoDt, PendenciaResponsavelDt responsavel, List arquivos, String  id_Serventia, String serventiaSubTipoCodigo, boolean intimacaoAudiencia, String id_ServentiaGabinete, LogDt logDt, FabricaConexao obFabricaConexao) throws MensagemException, Exception {
    	ServentiaNe serventiaNe = new ServentiaNe(); 
    	ProcessoResponsavelNe responsavelNe = new ProcessoResponsavelNe();
        String id_ServentiaCargoPromotor;
        ServentiaDt serventiaDt = serventiaNe.consultarId(id_Serventia);
        ServentiaCargoDt serventiaCargoPromotor = null;
        ServentiaDt promotoriaSelecionada = null;
        List listaPromotores = null;

        // Verifica se trata de intima��o
        switch (pendenciaDt.getPendenciaTipoCodigoToInt()) {
        case PendenciaTipoDt.INTIMACAO:        	
        	
    		// Tenta consultar um promotor independente da serventia, pois existem casos de um promotor 
        	//  criar um processo para uma serventia que n�o possui relacionamento com a serventia
        	// Ex: Um Promotor de Goi�nia criar um processo vinculado � comarca de Rio Verde.
    		// Neste cen�rio, se houver uma intima��o ao MP deve ser enviada para o promotor respons�vel, 
        	// mesmo ele estando vinculado a uma promotoria n�o relacionada
        	listaPromotores = responsavelNe.consultarListaPromotoresProcesso(processoDt.getId(), null, serventiaSubTipoCodigo, CargoTipoDt.MINISTERIO_PUBLICO, true, obFabricaConexao);
        	
        	if (listaPromotores != null){
        		for (Iterator iterator = listaPromotores.iterator(); iterator.hasNext();) {
        			serventiaCargoPromotor = (ServentiaCargoDt) iterator.next();
        			
        			if (serventiaCargoPromotor != null){
            			// Caso tenha encontrado o promotor relacionado ao processo iremos obter tamb�m a serventia para que a movimenta��o seja gerada
            			promotoriaSelecionada = serventiaNe.consultarId(serventiaCargoPromotor.getId_Serventia());
            		}        			
        		}       		
        	}
    		
        	if (serventiaCargoPromotor == null || promotoriaSelecionada == null){
        		// Busca promotoria relacionada
                ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();
                promotoriaSelecionada = serventiaRelacionadaNe.consultarServentiaRelacionadaSubTipo(processoDt.getId_Serventia(), serventiaSubTipoCodigo);
                if (promotoriaSelecionada == null) {
                	if (serventiaSubTipoCodigo !=null && Funcoes.StringToInt(serventiaSubTipoCodigo) == ServentiaSubtipoDt.MP_SEGUNDO_GRAU )
                		throw new MensagemException("N�o h� nenhum Procurador ou procuradoria vinculada � Serventia para gerar o tipo de intima��o solicitado.");
                	else
                		throw new MensagemException("N�o h� nenhum Promotor ou Promotoria vinculada � Serventia para gerar o tipo de intima��o solicitado.");
                }
        	}           

            // Nesse caso deve gerar Movimenta��o de Intima��o para MP
            MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
            movimentacaoNe.gerarMovimentacaoIntimacaoMinisterioPublico(pendenciaDt, promotoriaSelecionada.getServentia(), intimacaoAudiencia, obFabricaConexao);

            if (intimacaoAudiencia) {
                // Intima��o em Audi�ncia com prazo gera Intima��o Lida
                // (pend�ncia fechada)
                if (Funcoes.StringToInt(pendenciaDt.getPrazo()) > 0) {
                	
                	if (this.isUtilizaPrazoCorrido(pendenciaDt.getId_Processo(), obFabricaConexao)) {
                    	pendenciaDt.setDataLimite(new PrazoSuspensoNe().getProximaDataValidaPrazoCorrido(new Date(), Funcoes.StringToInt(pendenciaDt.getPrazo()), serventiaDt , obFabricaConexao) + " 00:00:00");
                	
                	} else if (this.isProcessoVaraPrecatoria(pendenciaDt.getId_Processo(), obFabricaConexao)) {
                		// Em caso de vara de precatoria deve-se testar o processo que originou a precatoria
                     	if (this.isUtilizaPrazoCorrido(processoDt.getId_ProcessoPrincipal(), obFabricaConexao))
                     		pendenciaDt.setDataLimite(new PrazoSuspensoNe().getProximaDataValidaPrazoCorrido(new Date(), Funcoes.StringToInt(pendenciaDt.getPrazo()), serventiaDt , obFabricaConexao) + " 00:00:00");
                     	else
                     		pendenciaDt.setDataLimite(new PrazoSuspensoNe().getProximaDataValidaNovoCPC(new Date(), Funcoes.StringToInt(pendenciaDt.getPrazo()), serventiaDt , obFabricaConexao) + " 00:00:00");
                     
                    } else
                    	pendenciaDt.setDataLimite(new PrazoSuspensoNe().getProximaDataValidaNovoCPC(new Date(), Funcoes.StringToInt(pendenciaDt.getPrazo()), serventiaDt , obFabricaConexao) + " 00:00:00");
                    
                	pendenciaDt.setDataFim(Funcoes.DataHora(new Date()));
                    pendenciaDt.setId_UsuarioFinalizador(pendenciaDt.getId_UsuarioCadastrador());
                    pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_CUMPRIDA));
                    pendenciaDt.setDataVisto("");
                } else {
                    // Quando for Intima��o em Audi�ncia sem prazo dever�
                    // somente gerar movimenta��o "Intima��o Realizada em
                    // Audi�ncia"
                    pendenciaDt = null;
                    return;
                }
            } else {
                // Nesse caso a pend�ncia ter� como data limite, a data de
                // leitura autom�tica
                pendenciaDt.setDataLimite(new PrazoSuspensoNe().getDataLeituraAutomatica(Funcoes.dateToStringSoData(new Date()), serventiaDt , obFabricaConexao));
                // Troca o status
                pendenciaDt.setId_PendenciaStatus("null");
                // pendencia intimacao e citacao para advogado e promotor tem
                // que ter o status aguardando visto
                pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_VISTO));
            }           
            
            //Verifica se ainda n�o obteve o promotor atrav�s de um respons�vel pela abertura do processo de uma outra serventia
            if (serventiaCargoPromotor == null){
            	// Verificar se j� existe um promotor respons�vel pelo processo e manda para o mesmo            	
            	serventiaCargoPromotor = responsavelNe.getPromotorResponsavelProcesso(processoDt.getId(), promotoriaSelecionada.getId(), obFabricaConexao);
            }            

            if (serventiaCargoPromotor == null) {
                // Caso contr�rio, distribui processo para um Promotor respons�vel (serventia Cargo) e salva como respons�vel pelo processo
                id_ServentiaCargoPromotor = DistribuicaoProcessoServentiaCargo.getInstance().getDistribuicaoPromotor(promotoriaSelecionada.getId(),processoDt.getId_ProcessoTipo());
                if (id_ServentiaCargoPromotor == null) {
	                if (serventiaSubTipoCodigo !=null && Funcoes.StringToInt(serventiaSubTipoCodigo) == ServentiaSubtipoDt.MP_SEGUNDO_GRAU )
	            		throw new MensagemException("N�o h� nenhum Procurador ativo com quantidade distribui��o positiva vinculada � Serventia para gerar o tipo de intima��o solicitado.");
	            	else
	            		throw new MensagemException("N�o h� nenhum Promotor ativo com quantidade de distribui��o positiva vinculada � Serventia para gerar o tipo de intima��o solicitado.");
                }
                responsavelNe.salvarProcessoResponsavel(processoDt.getId(), id_ServentiaCargoPromotor, true, CargoTipoDt.MINISTERIO_PUBLICO,logDt, obFabricaConexao);
            } else
                id_ServentiaCargoPromotor = serventiaCargoPromotor.getId();
            
            
            List responsaveis = new ArrayList();
            
            // Seta respons�vel na pend�ncia e salva
            responsavel.setId_ServentiaCargo(id_ServentiaCargoPromotor);
            responsaveis.add(responsavel);
            // Adiciona a serventia como responsavel novo tratamento
            PendenciaResponsavelDt serventiaResponsavelDt = new PendenciaResponsavelDt();
            
            if (id_ServentiaGabinete != null && id_ServentiaGabinete.length()>0){ // tratamento novo
            	serventiaResponsavelDt.setId_Serventia(id_ServentiaGabinete);
            } else {
            	serventiaResponsavelDt.setId_Serventia(processoDt.getId_Serventia());
            }
            
            responsaveis.add(serventiaResponsavelDt);
            
            // Lista de respons�veis
            pendenciaDt.setResponsaveis(responsaveis);
            
            this.gerarPendencia(pendenciaDt, arquivos, false, obFabricaConexao);

            break;
        }
    }
    
    /**
     * Consulta a estatistica dos tipos das pendencias ativas para a p�gina
     * Inicial do advogado. Nessa consulta ser�o desconsideradas as pend�ncias
     * do tipo Intima��o e Carta de Cita��o pois essas s�o tratadas de forma
     * diferente.
     * 
     * @author msapaula
     * @since 15/06/2009
     * @param usuarioDt
     *            usuario de referencia
     */
    public Map consultarTiposAtivasPaginaInicalAdvogado(UsuarioDt usuarioDt) throws Exception {
        Map tiposPendencia = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            tiposPendencia = pendenciaPs.consultarTiposAtivasPaginaInicalAdvogado(usuarioDt);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return tiposPendencia;
    }
    
    public Map consultarTiposAtivasPaginaInicalAssistenteAdvogado(UsuarioDt usuarioDt) throws Exception {
        Map tiposPendencia = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            tiposPendencia = pendenciaPs.consultarTiposAtivasPaginaInicalAssistenteAdvogado(usuarioDt);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return tiposPendencia;
    }

    /**
     * Consulta a quantidade de pendencias em andamento de um determinado tipo
     * para o usuario - Consulta inclusive para a serventia, para o tipo de
     * serventia, para o cargo do usuario, para o usuario resposavel
     * 
     * @author Leandro Bernardes
     * @since 03/12/2008 10:35
     * @param usuarioDt
     *            usuario que deseja a quantidade
     * @param tipo
     *            tipo da pendencia
     * @return int
     * @throws Exception
     */
    public int consultarQuantidadePendenciasAdvogadosEmAndamento(UsuarioDt usuarioDt, String tipo) throws Exception {
        int qtd = 0;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            qtd = pendenciaPs.consultarQuantidadePendenciasAdvogadosEmAndamento(usuarioDt, tipo);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return qtd;
    }

    /**
     * Consulta pendencias do tipo Intima��o advogado e promotor
     * informado para que seja montada a p�gina inicial do Advogado. Retorna a
     * informa��o se o advogado � principal ou n�o
     * 
     * @author lsbernardes
     * @param usuarioNe
     *            usuario da sessao
     * @return lista de pendencias do advogado
     * @throws Exception
     */
    public List consultarIntimacoesPromotorAdvogado(UsuarioNe usuarioNe) throws Exception {
        List pendencias = null;
        int GrupoCodigo = Funcoes.StringToInt(usuarioNe.getUsuarioDt().getGrupoCodigo());
        if ( GrupoCodigo== GrupoDt.MINISTERIO_PUBLICO || GrupoCodigo==GrupoDt.MP_TCE){
        	pendencias =  this.consultarIntimacoesPromotor(usuarioNe);
        } else if (Funcoes.StringToInt(usuarioNe.getUsuarioDt().getGrupoTipoCodigo()) == GrupoTipoDt.ADVOGADO){
        	pendencias =  this.consultarIntimacoesCitacoesAdvogado(usuarioNe);
        }

        return pendencias;
    }
    
    /**
     * Consulta pendencias do tipo Intima��o e Carta de Cita��o para o advogado
     * informado para que seja montada a p�gina inicial do Advogado. Retorna a
     * informa��o se o advogado � principal ou n�o
     * 
     * @author lsbernardes, msapaula
     * @since 10/06/2009
     * @param usuarioDt
     *            usuario da sessao
     * @return lista de pendencias do advogado
     * @throws Exception
     */
    public List consultarIntimacoesCitacoesAdvogado(UsuarioNe usuarioNe) throws Exception {
        List pendencias = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            pendencias = pendenciaPs.consultarIntimacoesCitacoesAdvogado(usuarioNe);
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }
    
    /**
     * Consulta pendencias do tipo Intima��o ou cita��o do id_usuario_serventia informado
     * 
     * @author lsbernardes
     * @since 25/03/2011
     * @param String id usuario serventia
     * @return lista de pendencias
     * @throws Exception
     */
    public List consultarIntimacoesCitacoesUsuarioServentia(String id_Processo, String Id_UsuarioServentia, FabricaConexao fabrica) throws Exception {
        List pendencias = null;
        FabricaConexao obFabricaConexao = null;
		try {

			if (fabrica == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				obFabricaConexao.iniciarTransacao();
			} else {
				obFabricaConexao = fabrica;
			}

			PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            pendencias = obPersistencia.consultarIntimacoesCitacoesUsuarioServentia(id_Processo, Id_UsuarioServentia);
        
            if (fabrica == null) obFabricaConexao.finalizarTransacao();

		} catch (Exception e) {
			if (fabrica == null) obFabricaConexao.cancelarTransacao();
	
			throw e;
		} finally {
			if (fabrica == null) obFabricaConexao.fecharConexao();
		}

        return pendencias;
    }

    /**
     * Consulta pendencias do tipo Intima��o para o cargo promotor informado
     * para que seja montada a p�gina inicial do Promotor.
     * 
     * @author lsbernardes
     * @since 27/09/2009
     * @param usuarioDt
     *            usuario da sessao
     * @return lista de pendencias do promotor
     * @throws Exception
     */
    public List consultarIntimacoesPromotor(UsuarioNe usuarioNe) throws Exception {
        List pendencias = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            if (usuarioNe.getUsuarioDt().getId_ServentiaCargo() != null && !usuarioNe.getUsuarioDt().getId_ServentiaCargo().equals("")) {
            	pendencias = pendenciaPs.consultarIntimacoesPromotor(usuarioNe);
            }
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }
    
    /**
     * Consulta pendencias do tipo Intima��o para o cargo do promotor chefe informado
     * para que seja montada a p�gina inicial do Assistente.
     * 
     * @author lsbernardes
     * @param usuarioDt
     *            usuario da sessao
     * @return lista de pendencias do promotor
     * @throws Exception
     */
    public List consultarIntimacoesPromotorChefe(UsuarioNe usuarioNe) throws Exception {
        List pendencias = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            if (usuarioNe.getUsuarioDt().getId_ServentiaCargoUsuarioChefe() != null && !usuarioNe.getUsuarioDt().getId_ServentiaCargoUsuarioChefe().equals("")) 
            	pendencias = pendenciaPs.consultarIntimacoesPromotorChefe(usuarioNe);
        
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }
    
    /**
     * Consulta pendencias do tipo Intima��o de todos os procuradores da procuradoria
     * do usu�rio logado para webservice
     * 
     * @author asrocha
     * @since 22/10/2010
     * @param usuarioDt
     *            usuario da sessao
     * @return lista de intima��es de todos os procuradores da procuradoria
     * @throws Exception
     */
    public List consultarIntimacoesProcuradoria(UsuarioNe usuarioNe ) throws Exception {
        List pendencias = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            pendencias = pendenciaPs.consultarIntimacoesProcuradoria(usuarioNe);
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }
    
    /**
     * M�todo que consulta as pend�ncias do tipo Carta de Cita��o de todos os advogados
     * do Escrit�rio Jur�dico do usu�rio logado 
     * 
     * @param usuarioNe usu�rio da sess�o
     * @return lista de pend�ncias do tipo carta de cita��o
     * @throws Exception
     * @author hmgodinho
     */
    public List consultarCitacoesEscritorioJuridicoProcuradoria(UsuarioNe usuarioNe) throws Exception {
        List pendencias = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            pendencias = pendenciaPs.consultarCitacoesEscritorioJuridicoProcuradoria(usuarioNe);
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }
    
    /**
     * Consulta pendencias do tipo Intima��o de todos os promotores da promotoria
     * do usu�rio logado 
     * 
     * @author lsbernardes
     * @param usuarioDt
     *            usuario da sessao
     * @return lista de intima��es de todos os procuradores da procuradoria
     * @throws Exception
     */
    public List consultarIntimacoesPromotoria(UsuarioNe usuarioNe ) throws Exception {
        List pendencias = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            pendencias = pendenciaPs.consultarIntimacoesPromotoria(usuarioNe);
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }
    
    /**
     * Consulta pendencias do distribuidor de gabinete para o cargo informado
	 * para que seja montada a p�gina inicial do distribuidor.
     * 
     * @author lsbernardes
     * @param usuarioDt
     *            usuario da sessao
     * @return lista de pendencias do promotor
     * @throws Exception
     */
    public List consultarPendenciasDistribuidorGabinete(UsuarioNe usuarioNe) throws Exception {
        List pendencias = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            if (usuarioNe.getUsuarioDt().getId_ServentiaCargo() != null && !usuarioNe.getUsuarioDt().getId_ServentiaCargo().equals("")) 
            	pendencias = pendenciaPs.consultarPendenciasDistribuidorGabinete(usuarioNe);
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }
    
    /**
     * Consulta pendencias do tipo Conclus�o que est�o para o distribuidor de gabinete,
	 * para que seja montada a p�gina inicial do distribuidor.
     * 
     * @author msapaula
     * @param usuarioNe, usuario da sessao
     * @return lista de pendencias do distribuidor
     * @throws Exception
     */
    public List consultarConclusoesDistribuidorGabinete(UsuarioNe usuarioNe) throws Exception {
        List pendencias = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            if (usuarioNe.getUsuarioDt().getId_ServentiaCargo() != null && !usuarioNe.getUsuarioDt().getId_ServentiaCargo().equals("")) 
            	pendencias = pendenciaPs.consultarConclusoesDistribuidorGabinete(usuarioNe);
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }
    
    /**
	 * Consulta pendencias do tipo Intima��o para o cargo promotor informado que est�o aguardando parecer
	 * para que seja montada a p�gina inicial do Promotor.
	 * 
	 * @author lsbernardes
	 * @since 26/04/2010
	 * @param usuarioDt
	 *            usuario da sessao
	 * @return lista de pendencias do promotor
	 * @throws Exception
	 */
    public List consultarIntimacoesPromotorAguardandoParecer(UsuarioNe usuarioNe) throws Exception {
        List pendencias = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            if ((usuarioNe.getUsuarioDt().getId_ServentiaCargo() != null && !usuarioNe.getUsuarioDt().getId_ServentiaCargo().equals(""))
            		|| (usuarioNe.getUsuarioDt().getId_ServentiaCargoUsuarioChefe() != null && !usuarioNe.getUsuarioDt().getId_ServentiaCargoUsuarioChefe().equals(""))) 
            	pendencias = pendenciaPs.consultarIntimacoesPromotorAguardandoParecer(usuarioNe);
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }
    
    /**
	 * Consulta pendencias do tipo Intima��o lidas pelo sistema para o cargo promotor informado que est�o aguardando parecer
	 * para que seja montada a p�gina inicial do Promotor.
	 * 
	 * @author lsbernardes
	 * @param UsuarioNe
	 *            usuario da sessao
	 * @return lista de pendencias do promotor
	 * @throws Exception
	 */
    public List consultarIntimacoesLidasAutomaticamentePromotorAguardandoParecer(UsuarioNe usuarioNe) throws Exception {
        List pendencias = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            if ((usuarioNe.getUsuarioDt().getId_ServentiaCargo() != null && !usuarioNe.getUsuarioDt().getId_ServentiaCargo().equals(""))
            		|| (usuarioNe.getUsuarioDt().getId_ServentiaCargoUsuarioChefe() != null && !usuarioNe.getUsuarioDt().getId_ServentiaCargoUsuarioChefe().equals(""))) 
            	pendencias = pendenciaPs.consultarIntimacoesLidasAutomaticamentePromotorAguardandoParecer(usuarioNe);
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }
    
    /**
	 * Consulta pendencias do tipo Intima��o lidas do advogado informado que est�o aguardando peticionamento
	 * para que seja montada a p�gina inicial do Promotor.
	 * 
	 * @author lsbernardes
	 * @param UsuarioNe
	 *            usuario da sessao
	 * @return lista de pendencias do promotor
	 * @throws Exception
	 */
    public List consultarIntimacoesAdvogadoAguardandoPeticionamento(UsuarioNe usuarioNe) throws Exception {
        List pendencias = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
           	pendencias = pendenciaPs.consultarIntimacoesAdvogadoAguardandoPeticionamento(usuarioNe);
           	
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }
    
    /**
	 * Consulta pendencias do tipo Intima��o lidas pelo sistema para o advogado informado que est�o aguardando parecer
	 * para que seja montada a p�gina inicial do Promotor.
	 * 
	 * @author lsbernardes
	 * @since 26/04/2010
	 * @param UsuarioNe
	 *            usuario da sessao
	 * @return lista de pendencias do promotor
	 * @throws Exception
	 */
    public List consultarIntimacoesLidasAutomaticamenteAdvogadoAguardandoPeticionamento(UsuarioNe usuarioNe) throws Exception {
        List pendencias = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
           	pendencias = pendenciaPs.consultarIntimacoesLidasAutomaticamenteAdvogadoAguardandoPeticionamento(usuarioNe);
           	
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }
    
    /**
   	 * Consulta pendencias do tipo Intima��o publicadas no Diario Eletronico do advogado informado que est�o aguardando parecer
   	 * para que seja montada a p�gina inicial do Promotor.
   	 * 
   	 * @author lsbernardes
   	 * @since 22/09/2016
   	 * @param UsuarioNe
   	 *            usuario da sessao
   	 * @return lista de pendencias do promotor
   	 * @throws Exception
   	 */
       public List consultarIntimacoesPublicadasDiarioEletronicoAdvogadoAguardandoPeticionamento(UsuarioNe usuarioNe) throws Exception {
           List pendencias = null;
           FabricaConexao obFabricaConexao = null;
           try {
               obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);

               PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
               //pendenciaPs.setConexao(obFabricaConexao);
              	pendencias = pendenciaPs.consultarIntimacoesPublicadasDiarioEletronicoAdvogadoAguardandoPeticionamento(usuarioNe);
              	
           } finally {
               obFabricaConexao.fecharConexao();
           }

           return pendencias;
       }

    /**
     * Gera uma pend�ncia do tipo "Suspens�o de Processo" para serventia do
     * processo. Essa pend�ncia ser� futura e s� aparecer� para o cart�rio
     * quando chegar o dia correspondente a data limite da pend�ncia
     * 
     * @param dados,
     *            dados da pend�ncia
     * @param movimentacaoDt,
     *            movimenta��o a ser vinculada com pend�ncia
     * @param processoDt,
     *            processo vinculado a pend�ncia
     * @param arquivos,
     *            arquivos vinculados a pend�ncia
     * @param usuarioCadastrador,
     *            usu�rio que est� gerando a pend�ncia
     * @param logDt,
     *            objeto com dados do log
     * @param obFabricaConexao,
     *            conex�o ativa
     * 
     * @author msapaula
     */
    public void gerarPendenciaSuspensaoProcesso(dwrMovimentarProcesso dados, MovimentacaoDt movimentacaoDt, ProcessoDt processoDt, List arquivos, UsuarioDt usuarioCadastrador, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {

    	PendenciaDt pendenciaDt = new PendenciaDt();
        // Monta objeto pend�ncia a partir do dwrMovimentarProcesso
        pendenciaDt.setPendenciaTipoCodigo(dados.getCodPendenciaTipo());
        pendenciaDt.setId_Movimentacao(movimentacaoDt.getId());
        pendenciaDt.setId_Processo(processoDt.getId());
        pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_EM_ANDAMENTO));

        if (dados.getDataLimite()!= null && dados.getDataLimite().length()>1){
        	 pendenciaDt.setDataLimite(dados.getDataLimite());
        } else {
        	pendenciaDt.setPrazo(dados.getPrazo());
        	// Conta data limite da pend�ncia
            Calendar calCalendario = Calendar.getInstance();
            calCalendario.setTime(new Date());
            calCalendario.add(Calendar.DATE, Funcoes.StringToInt(dados.getPrazo()));
            pendenciaDt.setDataLimite(Funcoes.dateToStringSoData(calCalendario.getTime()));
        }

        // Todas as pend�ncias de processo devem ter DataVisto setada
        pendenciaDt.setDataVisto(Funcoes.DataHora(new Date()));
        pendenciaDt.setId_UsuarioCadastrador(usuarioCadastrador.getId_UsuarioServentia());
        pendenciaDt.setId_UsuarioLog(logDt.getId_Usuario());
        pendenciaDt.setIpComputadorLog(logDt.getIpComputador());
        pendenciaDt.setId_ProcessoPrioridade(processoDt.getId_ProcessoPrioridade());

        // Adiciona respons�vel a pend�ncia
        PendenciaResponsavelDt responsavel = new PendenciaResponsavelDt();
        responsavel.setId_Serventia(processoDt.getId_Serventia());
        pendenciaDt.addResponsavel(responsavel);
        this.gerarPendencia(pendenciaDt, arquivos, false, obFabricaConexao);
    }

    /**
     * Verifica se h� pend�ncias ativas para um ServentiaCargo, ou seja,
     * qualquer pend�ncia em aberto (at� mesmo as reservadas)
     * 
     * @param id_ServentiaCargo,
     *            identifica��o do ServentiaCargo
     * @param fabConexao,
     *            conex�o ativa
     * 
     * @author msapaula
     * @since 20/08/2009 10:45
     */
    public boolean verificaPendenciasAtivasServentiaCargo(String id_ServentiaCargo, FabricaConexao fabConexao) throws Exception {
        boolean boRetorno = false;
        FabricaConexao obFabricaConexao = null;
        try {
            if (fabConexao == null)
                obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            else
                obFabricaConexao = fabConexao;
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            int qtde = obPersistencia.consultarPendenciasAtivasServentiaCargo(id_ServentiaCargo);

            if (qtde > 0) boRetorno = true;
        } finally {
            if (fabConexao == null) obFabricaConexao.fecharConexao();
        }

        return boRetorno;
    }

    /**
     * M�todo que verifica se h� pend�ncias do tipo conclus�o em aberto para o
     * ServentiaCargo passado.
     * 
     * @param id_ServentiaCargo,
     *            identifica��o do ServentiaCargo
     * @param fabConexao,
     *            conex�o ativa
     * 
     * @author msapaula
     */
    public boolean verificaConclusoesAbertasServentiaCargo(String id_ServentiaCargo, FabricaConexao fabConexao) throws Exception {
        boolean boRetorno = false;
        FabricaConexao obFabricaConexao = null;
        try {
            if (fabConexao == null)
                obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            else
                obFabricaConexao = fabConexao;
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            int qtde = obPersistencia.consultarConclusoesAbertasServentiaCargo(id_ServentiaCargo);

            if (qtde > 0) boRetorno = true;
        } finally {
            if (fabConexao == null) obFabricaConexao.fecharConexao();
        }
        return boRetorno;
    }

    /**
     * Gera uma pend�ncia do tipo Intima��o para um Advogado no momento em que
     * esse est� cadastrando um processo e uma audi�ncia � marcada, sendo que
     * este j� fica intimado. � gerada uma pend�ncia, que j� � fechada e gera
     * movimenta��o Intima��o Efetivada
     * 
     * @param processoDt,
     *            bean do processo
     * @param movimentacaoAudiencia,
     *            bean da da movimentacao vinculada a audiencia
     * @param id_UsuarioCadastrador,
     *            id do usuario serventia que cadastrou
     * @param id_ProcessoParte,
     *            id da parte do processo
     * @param usuarioResponsavel,
     *            dt do usu�rio responsavel
     * @param obFabricaConexao,
     *            fabrica de conexao para continuar uma transacao existente
     * 
     * @author msapaula
     */
    public void gerarIntimacaoEfetivadaAdvogado(ProcessoDt processoDt, MovimentacaoDt movimentacaoAudiencia, String id_UsuarioCadastrador, String id_ProcessoParte, UsuarioDt usuarioResponsavel, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
        //LogDt logDt = new LogDt(processoDt.getId_UsuarioLog(), processoDt.getIpComputadorLog());

        // Gera pend�ncia Intima��o para Advogado
        //this.gerarPendenciaProcesso(processoDt.getId(), PendenciaTipoDt.INTIMACAO, movimentacaoAudiencia.getId(), id_UsuarioCadastrador, "", "", id_ProcessoParte, PendenciaStatusDt.EmAndamento, usuarioResponsavel.getId_UsuarioServentia(), "", "", "", null, obFabricaConexao);
       // this.gerarPendenciaProcesso(processoDt.getId(), null, PendenciaTipoDt.INTIMACAO, movimentacaoAudiencia.getId(), id_UsuarioCadastrador, "", "", id_ProcessoParte, PendenciaStatusDt.EmAndamento, usuarioResponsavel.getId_UsuarioServentia(), "", "", "", processoDt.getId_ProcessoPrioridade(), null, logDt, obFabricaConexao);
        // J� fecha a pend�ncia que acabou de ser criada
        //this.fecharIntimacao(obFabricaConexao);
    	
    	// Configura a pendencia para ser persistida
        PendenciaDt pendenciaDt = new PendenciaDt();
        
        pendenciaDt.setDataFim(Funcoes.DataHora(new Date()));
        pendenciaDt.setId_UsuarioCadastrador(UsuarioDt.SistemaProjudi);
        pendenciaDt.setId_UsuarioFinalizador(pendenciaDt.getId_UsuarioCadastrador());
        pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_CUMPRIDA));
        pendenciaDt.setCodigoTemp(String.valueOf(PendenciaStatusDt.AGUARDANDO_PARECER_LEITURA_AUTOMATICA_CODIGO_TEMP));
        pendenciaDt.setPendenciaTipoCodigo(String.valueOf(PendenciaTipoDt.INTIMACAO));
        pendenciaDt.setDataVisto(Funcoes.DataHora(new Date()));
        pendenciaDt.setId_Movimentacao(movimentacaoAudiencia.getId());
        pendenciaDt.setId_Processo(processoDt.getId());
        pendenciaDt.setId_ProcessoParte(id_ProcessoParte);
        
        if (processoDt.getId_ProcessoPrioridade() != null && processoDt.getId_ProcessoPrioridade().length() > 0) 
        	pendenciaDt.setId_ProcessoPrioridade(String.valueOf(processoDt.getId_ProcessoPrioridade()));
        
        pendenciaDt.setId_UsuarioLog(logDt.getId_Usuario());
        pendenciaDt.setIpComputadorLog(logDt.getIpComputador());

        PendenciaResponsavelDt responsavel = new PendenciaResponsavelDt();
        responsavel.setId_UsuarioResponsavel(usuarioResponsavel.getId_UsuarioServentia());
        pendenciaDt.addResponsavel(responsavel);

        // Gera a pendencia
        this.gerarPendencia(pendenciaDt, null, false, obFabricaConexao);

        // Gera movimenta��o do tipo 'Intima��o efetivada On-line"
        String complemento = "On-line para " + usuarioResponsavel.getNome() + " (Referente � Mov. " + movimentacaoAudiencia.getMovimentacaoTipo() + ")";
        new MovimentacaoNe().gerarMovimentacaoIntimacaoEfetivadaOnLine(processoDt.getId(), UsuarioServentiaDt.SistemaProjudi, complemento, logDt, obFabricaConexao);
    }

    /**
     * Consulta as pendencias de um determinado tipo
     * 
     * @author Leandro Bernardes
     * @since 03/09/2009
     * @param String
     *            idProcesso, id do processo que ter� suas pendencias abertas
     *            consultadas
     * @param String
     *            idServentiaCargo, id do serventia cargo que ter� suas
     *            pendencias abertas consultadas
     * @return List
     * @throws Exception
     */
    public List consultarPendenciasAbertasProcesso(String idProcesso, String idServentiaCargo) throws Exception {
        List pendencias = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            pendencias = pendenciaPs.consultarPendenciasAbertasProcesso(idProcesso, idServentiaCargo);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }
    
    /**
     * M�todo que consulta todas as pend�ncias abertas de um processo.
     * @param idProcesso - ID do processo
     * @return lista de pend�ncias
     * @throws Exception
     * @author hmgodinho
     */
    public List consultarPendenciasAbertasProcesso(String idProcesso) throws Exception {
        List pendencias = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            pendencias = pendenciaPs.consultarPendenciasAbertasProcesso(idProcesso);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }
    

    /**
     * Consulta as pendencias de um determinado tipo baseado no consultarPendenciasAbertasProcesso
     * 
     * @author Leonardo Rocha
     * @since 19/08/2019
     * @param String
     *            idProcesso, id do processo que ter� suas pendencias abertas
     *            consultadas
     * @param String
     *            idServentiaCargo, id do serventia cargo que ter� suas
     *            pendencias abertas consultadas
     * @return List
     * @throws Exception
     */
    public List consultarPendenciasAbertasProcessoPJD(String idProcesso, String idServentiaCargo) throws Exception {
        List pendencias = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            pendencias = pendenciaPs.consultarPendenciasAbertasProcessoPJD(idProcesso, idServentiaCargo);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }
    
    /**
     * Verifica se um determinado processo pussui a pend�ncia concluso presidente tjgo
     * 
     * @author Leandro Bernardes
     * @param String idProcesso, processo que ser� verificado
     * @param conexao, conex�o com o banco de dados  
     * @return boolean
     * @throws Exception
     */
    public boolean verificarExistenciaConclusoPresidenteTJGO(String idProcesso, FabricaConexao obFabricaConexao) throws Exception {
        boolean retorno = false;

        PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
        retorno = pendenciaPs.verificarExistenciaConclusoPresidenteTJGO(idProcesso);

        return retorno;
    }
    
    /**
     * Verifica se um determinado processo pussui a pend�ncia concluso vice-presidente tjgo
     * 
     * @author Leandro Bernardes
     * @param String idProcesso, processo que ser� verificado
     * @param conexao, conex�o com o banco de dados  
     * @return boolean
     * @throws Exception
     */
    public boolean verificarExistenciaConclusoVicePresidenteTJGO(String idProcesso, FabricaConexao obFabricaConexao) throws Exception {
        boolean retorno = false;

        PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
        retorno = pendenciaPs.verificarExistenciaConclusoVicePresidenteTJGO(idProcesso);

        return retorno;
    }
    
    /**
     * Consulta as pendencias de um determinado tipo
     * 
     * @author Leandro Bernardes
     * @since 03/09/2009
     * @param String
     *            idProcesso, id do processo que ter� suas pendencias abertas
     *            consultadas
     * @param String
     *            idServentiaCargo, id do serventia cargo que ter� suas
     *            pendencias abertas consultadas
     * @return List
     * @throws Exception
     */
    public List consultarIntimacoesPromotorSubstitutoProcessual(String idProcesso, String id_UsuarioServentia) throws Exception {
        List pendencias = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            pendencias = obPersistencia.consultarIntimacoesPromotorSubstitutoProcessual(idProcesso, id_UsuarioServentia);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }

    /**
     * M�todo que consulta se h� alguma conclus�o aberta para um processo
     * passado
     * 
     * @param id_Processo,
     *            identifica��o de processo
     * @param id_ServentiaCargo,
     *            identifica��o do cargo
     * 
     * @author msapaula
     */
    public PendenciaDt consultarConclusaoAbertaProcesso(String idProcesso, String idServentiaCargo, FabricaConexao fabricaConexao) throws Exception {
        PendenciaDt conclusao = null;
        FabricaConexao obFabricaConexao = null;
        try {
            if (fabricaConexao == null){
            	obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            } else {
            	obFabricaConexao = fabricaConexao;
            }
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            conclusao = obPersistencia.consultarConclusaoAbertaProcesso(idProcesso, idServentiaCargo);
        } finally {
            if (fabricaConexao == null) obFabricaConexao.fecharConexao();
        }
        return conclusao;
    }

    /**
     * verifica se a pend�ncia � do tipo "verificar"
     * 
     * @param pendenciaDt
     * @return verdadeiro ou falso
     */
    private boolean pendenciaTipoVerificar(PendenciaDt pendenciaDt) {
        boolean retorno = false;
        Integer tipoPendencia = pendenciaDt.getPendenciaTipoCodigoToInt();

        if (tipoPendencia == PendenciaTipoDt.VERIFICAR_NOVO_PROCESSO || tipoPendencia == PendenciaTipoDt.VERIFICAR_PROCESSO || 
        		tipoPendencia == PendenciaTipoDt.VERIFICAR_PETICAO || tipoPendencia == PendenciaTipoDt.SUSPENSAO_PROCESSO || 
        		tipoPendencia == PendenciaTipoDt.VERIFICAR_PARECER  || tipoPendencia == PendenciaTipoDt.VERIFICAR_DISTRIBUICAO ||
        		tipoPendencia == PendenciaTipoDt.VERIFICAR_CONEXAO || tipoPendencia == PendenciaTipoDt.CALCULAR_LIQUIDACAO_PENAS ||
        		tipoPendencia == PendenciaTipoDt.AGUARDANDO_DECURSO_PRAZO || tipoPendencia == PendenciaTipoDt.AGUARDANDO_PRAZO_DECADENCIAL ||
        		tipoPendencia == PendenciaTipoDt.AGUARDANDO_CUMPRIMENTO_PENA || tipoPendencia == PendenciaTipoDt.MARCAR_AUDIENCIA ||  
        		tipoPendencia == PendenciaTipoDt.MARCAR_AUDIENCIA_CONCILIACAO_CEJUSC || tipoPendencia == PendenciaTipoDt.MARCAR_AUDIENCIA_MEDIACAO_CEJUSC ||
        	    tipoPendencia == PendenciaTipoDt.MARCAR_AUDIENCIA_CONCILIACAO_CEJUSC_DPVAT || tipoPendencia == PendenciaTipoDt.AVERBACAO_CUSTAS ||  
        		tipoPendencia == PendenciaTipoDt.VERIFICAR_DOCUMENTO || tipoPendencia == PendenciaTipoDt.EFETIVAR_EVENTO || 
        		tipoPendencia == PendenciaTipoDt.VERIFICAR_GUIA_PAGA || tipoPendencia == PendenciaTipoDt.VERIFICAR_GUIA || tipoPendencia == PendenciaTipoDt.VERIFICAR_CALCULO || 
        		tipoPendencia == PendenciaTipoDt.VERIFICAR_GUIA_VENCIDA || tipoPendencia == PendenciaTipoDt.VERIFICAR_FATO || 
        		tipoPendencia == PendenciaTipoDt.VERIFICAR_DEVOLUCAO_PRECATORIA || tipoPendencia == PendenciaTipoDt.VERIFICAR_NOVA_PRECATORIA || 
        		tipoPendencia == PendenciaTipoDt.VERIFICAR_RESPOSTA_CAMARA_SAUDE || tipoPendencia == PendenciaTipoDt.VERIFICAR_RESPOSTA_PEDIDO_LAUDO_RELATORIO ||
        		tipoPendencia == PendenciaTipoDt.VERIFICAR_PRECATORIA || tipoPendencia == PendenciaTipoDt.VERIFICAR_RECURSO_REPETITIVO ||
        		tipoPendencia == PendenciaTipoDt.VERIFICAR_OFICIO_COMUNICATORIO || tipoPendencia == PendenciaTipoDt.OFICIO_COMUNICATORIO ||
        		tipoPendencia == PendenciaTipoDt.AGUARDANDO_DECURSO_PRAZO_PRISAO_CIVIL ||  tipoPendencia == PendenciaTipoDt.VERIFICAR_RESPOSTA_PEDIDO_CENOPES
        		|| tipoPendencia == PendenciaTipoDt.VERIFICAR_GUIA_PENDENTE || tipoPendencia == PendenciaTipoDt.VERIFICAR_RETORNO_AR_CORREIOS	
        		|| tipoPendencia == PendenciaTipoDt.VERIFICAR_CLASSE_PROCESSUAL || tipoPendencia == PendenciaTipoDt.VERIFICAR_RESPOSTA_OFICIO_DELEGACIA || tipoPendencia == PendenciaTipoDt.VERIFICAR_ENDERECO_PARTE
        		|| tipoPendencia == PendenciaTipoDt.VERIFICAR_REDISTRIBUICAO|| tipoPendencia == PendenciaTipoDt.CONFIRMAR_DISTRIBUICAO || tipoPendencia == PendenciaTipoDt.VERIFICAR_NOVO_PROCESSO_PEDIDO_ASSISTENCIA
        		|| tipoPendencia == PendenciaTipoDt.VERIFICAR_TEMA_TRANSITADO_JULGADO || tipoPendencia == PendenciaTipoDt.VERIFICAR_PROCESSO_HIBRIDO || tipoPendencia == PendenciaTipoDt.VERIFICAR_AUDIENCIAS_REALIZADAS_CEJUSC) 
        	
        	retorno = true;

        return retorno;
    }
    
    /**
     * verifica se a pend�ncia � do tipo "verificar" de um serventia cargo
     * 
     * @param pendenciaDt
     * @return verdadeiro ou falso
     */
    private boolean pendenciaTipoVerificarServentiaCargo(PendenciaDt pendenciaDt) {
        boolean retorno = false;
        Integer tipoPendencia = pendenciaDt.getPendenciaTipoCodigoToInt();

        if (tipoPendencia == PendenciaTipoDt.PEDIDO_VISTA || tipoPendencia == PendenciaTipoDt.REVISAO  
        		|| tipoPendencia == PendenciaTipoDt.RELATORIO) 
        	
        	retorno = true;

        return retorno;
    }

    /**
     * Consulta as pendencias de um processo
     * 
     * @author Leandro Bernardes
     * @since 06/11/2009
     * @param String idProcesso, id do processo que ter� suas pendencias abertas consultadas
     * @return List
     * @throws Exception
     */
    public List[] consultarPendenciasProcesso(String id_Processo, boolean ehConsultaPublica, UsuarioDt usuarioDt) throws Exception {
    	PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
        List pendencias = null;
        List[] retorno = new ArrayList[16];
        retorno[0] = new ArrayList();
        retorno[1] = new ArrayList();
        retorno[2] = new ArrayList();
        retorno[3] = new ArrayList();
        retorno[4] = new ArrayList();
        retorno[5] = new ArrayList();
        retorno[6] = new ArrayList();
        retorno[7] = new ArrayList();
        retorno[8] = new ArrayList();
        retorno[9] = new ArrayList();
        retorno[10] = new ArrayList();
        retorno[11] = new ArrayList();
        retorno[12] = new ArrayList();
        retorno[13] = new ArrayList();
        retorno[14] = new ArrayList();
        retorno[15] = null;
        FabricaConexao obFabricaConexao = null;
        
        try {
            obFabricaConexao = new FabricaConexao((ehConsultaPublica ? FabricaConexao.PERSISTENCIA : FabricaConexao.PERSISTENCIA));
            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);

            pendencias = pendenciaPs.consultarPendenciasProcesso(id_Processo);
           
            if (pendencias != null && pendencias.size() > 0){
	            for (Iterator iterator = pendencias.iterator(); iterator.hasNext();) {
	                PendenciaDt pendencia = (PendenciaDt) iterator.next();
	                String nomeParte = "";
	                if (pendencia.getNomeParte() != null && !pendencia.getNomeParte().equals("") && !pendencia.getNomeParte().equals("null")) nomeParte = " - " + pendencia.getNomeParte();
	                
	                String nomeServentiaCargoReserva = "";             
	                if (pendencia.estaReservada() && pendencia.getNomeUsuarioFinalizador() != null)
	                {
	                	nomeServentiaCargoReserva = pendencia.getNomeUsuarioFinalizador();
	                }
	
	                // AGUARDANDO EXPEDICAO OU AGUARDANDO VERIFICA��O
	                if (pendencia.getDataFim().equals("") && 
	                		(Funcoes.StringToInt(pendencia.getId_PendenciaStatus()) == PendenciaStatusDt.ID_EM_ANDAMENTO 
	                		|| Funcoes.StringToInt(pendencia.getId_PendenciaStatus()) == PendenciaStatusDt.ID_PRE_ANALISADA)) {
	                    String[] pendenciaStr = { pendencia.getPendenciaTipo() + nomeParte, pendencia.getDataInicio() , nomeServentiaCargoReserva};
	                    if (this.pendenciaTipoVerificar(pendencia) && pendencia.getDataLimite() != null 
	                    		&& !pendencia.getDataLimite().equals("")){
	                    	 SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
	                    	 Date dataLimite = format.parse(pendencia.getDataLimite());  
	                    	
	                    	 if (dataLimite.after(new Date())) {  
	   	                        // Ainda vai acontecer o dia  
	                    		String[] pendenciaFuturaStr = {pendencia.getPendenciaTipo() + nomeParte, pendencia.getDataLimite() , nomeServentiaCargoReserva};
	                    		retorno[7].add(pendenciaFuturaStr);
	   	                    } else if (dataLimite.before(new Date())) {  
	   	                        // O dia j� aconteceu  
	   	                    	retorno[4].add(pendenciaStr);
	   	                    }  
	                    	
	                    }
	                    else if (this.pendenciaTipoVerificar(pendencia))
	                        retorno[4].add(pendenciaStr);
	                    else if (this.pendenciaTipoVerificarServentiaCargo(pendencia)) {
	                    	List responsaveis = pendenciaResponsavelNe.consultarResponsaveisDetalhado(pendencia.getId(), null, obFabricaConexao);
	                    	String serventiaCargo = "";
	                    	if (responsaveis != null){
	                    		Iterator iteratorResponsavel = responsaveis.iterator();
	                    		while (iteratorResponsavel.hasNext()) {
	                    			PendenciaResponsavelDt dados = (PendenciaResponsavelDt) iteratorResponsavel.next();
	                    			if (dados != null && dados.getCargoTipoCodigo() != null && (dados.getCargoTipoCodigo().trim().equalsIgnoreCase(String.valueOf(CargoTipoDt.DESEMBARGADOR)) || dados.getCargoTipoCodigo().trim().equalsIgnoreCase(String.valueOf(CargoTipoDt.JUIZ_1_GRAU)))){
	                    				serventiaCargo = dados.getServentiaCargo();
	                    			}
	                    		}
	                    	}
	                    	String[] pendenciaStrServentiaCargo = { pendencia.getId()+"@#!@"+pendencia.getHash(), pendencia.getPendenciaTipo() + nomeParte , serventiaCargo ,pendencia.getDataInicio(), pendencia.getPendenciaTipoCodigo() };
	                        retorno[9].add(pendenciaStrServentiaCargo);
	                    }else if (Funcoes.StringToInt(pendencia.getPendenciaTipoCodigo()) == PendenciaTipoDt.LIBERACAO_ACESSO){
	                    	String usuarioResponsavel = "";
	                    	
	                    	if (pendencia.getDataLimite() != null && pendencia.getDataLimite().length()>0){
	                    		
	                    		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");  
	            			    Date dataLimite = format.parse(pendencia.getDataLimite());
	                    		
	            			    if (dataLimite.after(new Date())) { 
			                    	if (nomeServentiaCargoReserva == null || nomeServentiaCargoReserva.trim().length() == 0){               	
			                    		List responsaveis = pendenciaResponsavelNe.consultarResponsaveisDetalhado(pendencia.getId(), null, obFabricaConexao);
				                    	
			                    		if (responsaveis != null){
				                    		Iterator iteratorResponsavel = responsaveis.iterator();
				                    		while (iteratorResponsavel.hasNext()) {
				                    			PendenciaResponsavelDt dados = (PendenciaResponsavelDt) iteratorResponsavel.next();
				                    			if (dados != null && dados.getId_UsuarioResponsavel() != null && dados.getNomeUsuarioResponsavel() != null){
				                    				usuarioResponsavel = dados.getNomeUsuarioResponsavel();
				                    			}
				                    		}
				                    	}
			                    	}                    	

			                    	if (usuarioResponsavel == null || usuarioResponsavel.trim().length() == 0)
			                    		usuarioResponsavel = nomeServentiaCargoReserva;
			                    	
			                    	//String[] pendenciaLiberarStr = { pendencia.getPendenciaTipo() + " Para "+pendencia.getNomeUsuarioCadastrador(), pendencia.getDataLimite() , usuarioResponsavel};
			                    	String[] pendenciaLiberarStr = { pendencia.getPendenciaTipo(), pendencia.getDataLimite() , usuarioResponsavel};
			                    	retorno[8].add(pendenciaLiberarStr);
	            			    }
		                    }
	                    } else if (Funcoes.StringToInt(pendencia.getPendenciaTipoCodigo()) == PendenciaTipoDt.ELABORACAO_VOTO){

		                    	String usuarioResponsavel = "";
	                    		
			            		if (nomeServentiaCargoReserva == null || nomeServentiaCargoReserva.trim().length() == 0){
			            			List responsaveis = pendenciaResponsavelNe.consultarResponsaveisDetalhado(pendencia.getId(), null, obFabricaConexao);
				                    	
				                    if (responsaveis != null){
				                    	Iterator iteratorResponsavel = responsaveis.iterator();
				                    	while (iteratorResponsavel.hasNext()) {
				                    		PendenciaResponsavelDt dados = (PendenciaResponsavelDt) iteratorResponsavel.next();
				                    		if (dados != null && dados.getId_UsuarioResponsavel() != null && dados.getNomeUsuarioResponsavel() != null){
				                    			usuarioResponsavel = dados.getNomeUsuarioResponsavel();
				                    		}
				                    	}
				                    }	                    	
			                    }
			                    	
			                    if (usuarioResponsavel == null || usuarioResponsavel.trim().length() == 0)
			                    	usuarioResponsavel = nomeServentiaCargoReserva;
			                    	
			                    	String[] pendenciaLiberarStr = { pendencia.getId()+"@#!@"+pendencia.getHash(), pendencia.getPendenciaTipo() + " Para "+pendencia.getNomeUsuarioCadastrador(), pendencia.getDataInicio() , usuarioResponsavel };	                    	
			                    	retorno[11].add(pendenciaLiberarStr);
	                    	
	                    }
	                    else if (Funcoes.StringToInt(pendencia.getPendenciaTipoCodigo()) == PendenciaTipoDt.SOLICITACAO_CARGA && !ehConsultaPublica){
	                    	String[] pendenciaStrSolicitacaoCarga = { pendencia.getId()+"@#!@"+pendencia.getHash(), pendencia.getPendenciaTipo(), pendencia.getNomeUsuarioCadastrador(), pendencia.getDataInicio(), pendencia.getDataLimite(), pendencia.getPendenciaTipoCodigo() };
			                retorno[12].add(pendenciaStrSolicitacaoCarga);
		                }
	                    else {
	                        retorno[0].add(montarArrayPendenciaCapaProcesso(pendencia, nomeServentiaCargoReserva, usuarioDt));
	                    }
	                }
	                
	                // AGUARDANDO VISTO
	                else if (!pendencia.getDataFim().equals("") && pendencia.getDataVisto().equals("") && pendencia.getDataLimite().equals("")) {
	                    String[] pendenciaStr = { pendencia.getPendenciaTipo() + nomeParte, pendencia.getDataInicio(), pendencia.getDataFim(), nomeServentiaCargoReserva };
	                    retorno[1].add(pendenciaStr);
	                }
	
	                // AGUARDANDO LEITURA
	                else if (pendencia.getDataFim().equals("") && !pendencia.getDataVisto().equals("") && Funcoes.StringToInt(pendencia.getId_PendenciaStatus()) == PendenciaStatusDt.ID_AGUARDANDO_VISTO) {
	                    String[] pendenciaStr = { pendencia.getPendenciaTipo() + nomeParte, pendencia.getDataInicio(), pendencia.getDataLimite(), nomeServentiaCargoReserva };
	                    retorno[6].add(pendenciaStr);
	                }
	
	                // AGUARDANDO DECURSO DE PRAZO
	                else if (!pendencia.getDataFim().equals("") && pendencia.getDataVisto().equals("") && !pendencia.getDataLimite().equals("")) {
	                    String[] pendenciaStr = { pendencia.getPendenciaTipo() + nomeParte, pendencia.getDataFim(), pendencia.getDataLimite() , nomeServentiaCargoReserva};
	                    retorno[5].add(pendenciaStr);
	                }
	                
	                else if (pendencia.getDataFim().equals("") && Funcoes.StringToInt(pendencia.getId_PendenciaStatus()) == PendenciaStatusDt.ID_AGUARDANDO_RETORNO
	                		&& Funcoes.StringToInt(pendencia.getPendenciaTipoCodigo()) == PendenciaTipoDt.SOLICITACAO_CARGA && ehConsultaPublica){
	                	//N�o deve aparecer esse tipo de pend�ncia na consulta p�blica
                    	//System.out.println("N�o deve aparecer esse tipo de pend�ncia na consulta p�blica");
	                }
                    
	                else if (pendencia.getDataFim().equals("") && Funcoes.StringToInt(pendencia.getId_PendenciaStatus()) == PendenciaStatusDt.ID_AGUARDANDO_RETORNO
	                		&& Funcoes.StringToInt(pendencia.getPendenciaTipoCodigo()) == PendenciaTipoDt.SOLICITACAO_CARGA && !ehConsultaPublica){
                    	String[] pendenciaStrSolicitacaoCarga = { pendencia.getId()+"@#!@"+pendencia.getHash(), pendencia.getPendenciaTipo(), pendencia.getNomeUsuarioCadastrador(), pendencia.getDataInicio(), pendencia.getDataLimite(), pendencia.getPendenciaTipoCodigo() };
		                retorno[13].add(pendenciaStrSolicitacaoCarga);
	                }
	                
	                // MANDADO JUDICIAL AGUARDANDO CUMPRIMENTO
	                else if((Funcoes.StringToInt(pendencia.getPendenciaTipoCodigo()) == PendenciaTipoDt.MANDADO) && pendencia.getDataFim().equals("") && Funcoes.StringToInt(pendencia.getId_PendenciaStatus()) == PendenciaStatusDt.ID_AGUARDANDO_RETORNO){
	                	String[] pendenciaStr = {pendencia.getId(), pendencia.getPendenciaTipo(), pendencia.getDataInicio(), nomeServentiaCargoReserva};
	                	retorno[14].add(pendenciaStr);
	                }
	
	                // AGUARDANDO RECEBIMENTO
	                else if (pendencia.getDataFim().equals("") && Funcoes.StringToInt(pendencia.getId_PendenciaStatus()) == PendenciaStatusDt.ID_AGUARDANDO_RETORNO) {
	                    String[] pendenciaStr = { pendencia.getPendenciaTipo()+ nomeParte, pendencia.getDataInicio(), nomeServentiaCargoReserva };
	                    retorno[2].add(pendenciaStr);
	                }
	                
	                // AGUARDANDO CUMPRIMENTO
	                else if (pendencia.getDataFim().equals("") && Funcoes.StringToInt(pendencia.getId_PendenciaStatus()) == PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO) {
	                	//Valida��o para apresentar PEDIDO CENOPES somente para o grupo CONSULTOR EXTERNO
	                	if (pendencia.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.PEDIDO_CENOPES && usuarioDt != null && usuarioDt.getGrupoCodigoToInt() == GrupoDt.CONSULTOR_SISTEMAS_EXTERNOS) {
	                		String[] pendenciaStr = { pendencia.getPendenciaTipo(), pendencia.getDataInicio(), nomeServentiaCargoReserva };
	                		retorno[3].add(pendenciaStr);
	                	} else {
	                		String[] pendenciaStr = montarArrayPendenciaCapaProcesso(pendencia, nomeServentiaCargoReserva, usuarioDt);
	                		retorno[3].add(pendenciaStr);
	                	}
	                }
	             // AGUARDANDO CORRECAO
	                else if (pendencia.getDataFim().equals("") && Funcoes.StringToInt(pendencia.getId_PendenciaStatus()) == PendenciaStatusDt.ID_CORRECAO) {
	                    String[] pendenciaStr = { pendencia.getPendenciaTipo(), pendencia.getDataInicio() , nomeServentiaCargoReserva};
	                    retorno[10].add(pendenciaStr);
	                }
	            }

            }

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return retorno;
    }
    
    private String[] montarArrayPendenciaCapaProcesso(PendenciaDt pendencia, String nomeServentiaCargoReserva, UsuarioDt usuarioDt) throws Exception {
    	String res[] = {pendencia.getPendenciaTipo(), pendencia.getDataInicio(), nomeServentiaCargoReserva, ""}; 
    	String botao = "";
    	List tiposPendencia = Arrays.asList("Aguardando Voto Virtual", "Proclama��o de Voto", "Pedido de Vista", "Retirar Pauta", "Julgamento Adiado", "Adiar Julgamento", "Para Conhecimento", "Resultado Un�nime", "Verificar Pedido de Sustenta��o Oral", "Verificar Impedimento", "Apreciados", "Resultado da vota��o"); // Seria bom substituir pelos codigos das pendencias, mas foi mais facil copiar da pagina inicial do relator
    	List responsavel = consultarResponsaveis(pendencia);
    	List servCargoRespo = (List) responsavel.stream().map(x -> ((PendenciaResponsavelDt) x).getId_ServentiaCargo()).filter(x -> StringUtils.isNotBlank((String) x)).collect(Collectors.toCollection(ArrayList::new));
    	
    	if(!tiposPendencia.contains(pendencia.getPendenciaTipo()) || (!servCargoRespo.contains(usuarioDt.getId_ServentiaCargo()) && !servCargoRespo.contains(usuarioDt.getId_ServentiaCargoUsuarioChefe()))) return res;
    	
    	if(StringUtils.equals(pendencia.getPendenciaStatusCodigo(), String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO))
    			|| StringUtils.equals(pendencia.getPendenciaStatusCodigo(), String.valueOf(PendenciaStatusDt.ID_EM_ANDAMENTO))) {
			AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
			
    		switch(Integer.parseInt(pendencia.getPendenciaTipoCodigo())) {
    			case PendenciaTipoDt.PROCLAMACAO_VOTO:
    				String idAudiProc = audienciaProcessoNe.consultarAudienciaProcessoDoProcesso(pendencia.getId_Processo());
    				botao = pendencia.getId() + "," + idAudiProc + "," + pendencia.getId_Processo();
    				break;
    			case PendenciaTipoDt.APRECIADOS:
    				botao = audienciaProcessoNe.consultarAudienciaProcessoDoProcesso(pendencia.getId_Processo()) + "," + pendencia.getId_Processo();
    				break;
    			case PendenciaTipoDt.RETIRAR_PAUTA:
    			case PendenciaTipoDt.RESULTADO_VOTACAO:
    			case PendenciaTipoDt.PEDIDO_VISTA_SESSAO:
    			case PendenciaTipoDt.ADIADO_PELO_RELATOR:
    				botao = pendencia.getId();
    				break;
    			case PendenciaTipoDt.SESSAO_CONHECIMENTO:
    			case PendenciaTipoDt.VOTO_SESSAO:
    				botao = pendencia.getId() + "," + pendencia.getId_Processo();
    				break;
    		}
    	}
    	
    	res[3] = botao;
    		
		return res;
	}

	/**
     * Consulta as pendencias de um processo
     * 
     * @author Leandro Bernardes
     * @since 06/11/2009
     * @param String idProcesso, id do processo que ter� suas pendencias abertas consultadas
     * @return List
     * @throws Exception
     */
    public List[] consultarPendenciasProcessoHash(String id_Processo, UsuarioNe UsuarioSessao) throws Exception {
    	PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe(); 
    	String idPendCorreios = "";
        List pendencias = null;
        List[] retorno = new ArrayList[16];
        retorno[0] = new ArrayList();
        retorno[1] = new ArrayList();
        retorno[2] = new ArrayList();
        retorno[3] = new ArrayList();
        retorno[4] = new ArrayList();
        retorno[5] = new ArrayList();
        retorno[6] = new ArrayList();
        retorno[7] = new ArrayList();
        retorno[8] = new ArrayList();
        retorno[9] = new ArrayList();
        retorno[10] = new ArrayList();
        retorno[11] = new ArrayList();
        retorno[12] = new ArrayList();
        retorno[13] = new ArrayList();
        retorno[14] = new ArrayList();
        retorno[15] = new ArrayList();
        
        FabricaConexao obFabricaConexao = null;
        
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);

            pendencias = pendenciaPs.consultarPendenciasProcessoHash(id_Processo, UsuarioSessao);
           
            if (pendencias != null && pendencias.size() > 0){
	            for (Iterator iterator = pendencias.iterator(); iterator.hasNext();) {
	                PendenciaDt pendencia = (PendenciaDt) iterator.next();
	                String nomeParte = "";
	                if (pendencia.getNomeParte() != null && !pendencia.getNomeParte().equals("") && !pendencia.getNomeParte().equals("null")) 
	                	nomeParte = " - " + pendencia.getNomeParte();
	                
	                String nomeServentiaCargoReserva = "";             
	                if (pendencia.getDataTemp() != null && pendencia.getNomeUsuarioFinalizador() != null) {
	                	nomeServentiaCargoReserva = pendencia.getNomeUsuarioFinalizador();
	                } else if (pendencia.getId_ServentiaCargo() != null && !pendencia.getId_ServentiaCargo().equalsIgnoreCase("")){
	                	UsuarioNe usuarioNe = new UsuarioNe();
	                	nomeServentiaCargoReserva = usuarioNe.consultarNomeCargoUsuarioServentiaCargo(pendencia.getId_ServentiaCargo());
	                	if(nomeServentiaCargoReserva == null || nomeServentiaCargoReserva.length() == 0) {
	                		nomeServentiaCargoReserva = "Cargo vazio. ID: " + pendencia.getId_ServentiaCargo();
	                	}
	                }
	                
	                if ((pendencia.isStatus(PendenciaStatusDt.ID_AGUARDANDO_PAGAMENTO_POSTAGEM) || pendencia.isStatus(PendenciaStatusDt.ID_AGUARDANDO_ENVIO_CORREIOS) || pendencia.isStatus(PendenciaStatusDt.ID_AGUARDANDO_CONFIRMACAO_CORREIOS) 
	                		|| pendencia.isStatus(PendenciaStatusDt.ID_RECEBIDO_CORREIOS) || pendencia.isStatus(PendenciaStatusDt.ID_INCONSISTENCIA_CORREIOS)) 
	                		|| ( ( pendencia.isStatus(PendenciaStatusDt.ID_CUMPRIDA) || pendencia.isStatus(PendenciaStatusDt.ID_NAO_CUMPRIDA) ) && (pendencia.getDataFim() == null || pendencia.getDataFim().length() == 0)) 
	                    ) {
	                	String status = pendencia.getPendenciaStatus();
	                	if (pendencia.isStatus(PendenciaStatusDt.ID_CUMPRIDA) || pendencia.isStatus(PendenciaStatusDt.ID_NAO_CUMPRIDA)) {
	                		status = "Aguardando Aviso de Recebimento";
	                	}
	                	idPendCorreios = new PendenciaCorreiosNe().consultarIdPendenciaCorreios(pendencia.getId());
	                	String[] pendenciaStr = { pendencia.getId()+"@#!@"+pendencia.getHash(), pendencia.getPendenciaTipo() + nomeParte, pendencia.getDataInicio(), status, pendencia.getId_ServentiaCargo(), pendencia.getPendenciaTipoCodigo(), pendencia.getId_PendenciaStatus(), idPendCorreios};
	                	retorno[15].add(pendenciaStr);
	                // AGUARDANDO EXPEDICAO OU AGUARDANDO VERIFICA��O
	            	}else if (pendencia.getDataFim().equals("") && 	(pendencia.isStatus( PendenciaStatusDt.ID_EM_ANDAMENTO )|| pendencia.isStatus( PendenciaStatusDt.ID_PRE_ANALISADA))) {
	                    String[] pendenciaStr = { pendencia.getId()+"@#!@"+pendencia.getHash(), pendencia.getPendenciaTipo() + nomeParte, pendencia.getDataInicio(), nomeServentiaCargoReserva, pendencia.getId_ServentiaCargo(), pendencia.getPendenciaTipoCodigo() };
	                    
	                    if (this.pendenciaTipoVerificar(pendencia) && pendencia.getDataLimite() != null 
	                    		&& !pendencia.getDataLimite().equals("")){
	                    	 SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
	                    	 Date dataLimite = format.parse(pendencia.getDataLimite());  
	                    	
	                    	 if (dataLimite.after(new Date())) {  
	   	                        // Ainda vai acontecer o dia  
	                    		String[] pendenciaFuturaStr = { pendencia.getId()+"@#!@"+pendencia.getHash(), pendencia.getPendenciaTipo() + nomeParte, pendencia.getDataLimite() , nomeServentiaCargoReserva};
	                    		retorno[7].add(pendenciaFuturaStr);
	   	                    } else if (dataLimite.before(new Date())) {  
	   	                        // O dia j� aconteceu  
	   	                    	retorno[4].add(pendenciaStr);
	   	                    }  
	                    	
	                    }else if (this.pendenciaTipoVerificar(pendencia))
	                        retorno[4].add(pendenciaStr);
	                    else if (this.pendenciaTipoVerificarServentiaCargo(pendencia)){
	                    	List responsaveis = pendenciaResponsavelNe.consultarResponsaveisDetalhado(pendencia.getId(), null, obFabricaConexao);
	                    	String serventiaCargo = "";
	                    	if (responsaveis != null){
	                    		Iterator iteratorResponsavel = responsaveis.iterator();
	                    		while (iteratorResponsavel.hasNext()) {
	                    			PendenciaResponsavelDt dados = (PendenciaResponsavelDt) iteratorResponsavel.next();
	                    			if (dados != null && dados.getCargoTipoCodigo() != null && (dados.getCargoTipoCodigo().trim().equalsIgnoreCase(String.valueOf(CargoTipoDt.DESEMBARGADOR)) || dados.getCargoTipoCodigo().trim().equalsIgnoreCase(String.valueOf(CargoTipoDt.JUIZ_1_GRAU)))){
	                    				serventiaCargo = dados.getServentiaCargo() + " - " + dados.getNomeUsuarioServentiaCargo();
	                    			}
	                    		}
	                    	}
	                    	String[] pendenciaStrServentiaCargo = { pendencia.getId()+"@#!@"+pendencia.getHash(), pendencia.getPendenciaTipo() + nomeParte , serventiaCargo ,pendencia.getDataInicio(), pendencia.getPendenciaTipoCodigo() };
	                        retorno[9].add(pendenciaStrServentiaCargo);
	                    
	                    } else if (Funcoes.StringToInt(pendencia.getPendenciaTipoCodigo()) == PendenciaTipoDt.LIBERACAO_ACESSO){
	                    	String usuarioResponsavel = "";
	                    	
	                    	if (pendencia.getDataLimite() != null && pendencia.getDataLimite().length()>0){
					                    		
	                    		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
				            	Date dataLimite = format.parse(pendencia.getDataLimite());
				                    		
				            	if (dataLimite.after(new Date())) {
				            		if (nomeServentiaCargoReserva == null || nomeServentiaCargoReserva.trim().length() == 0){
				            			List responsaveis = pendenciaResponsavelNe.consultarResponsaveisDetalhado(pendencia.getId(), null, obFabricaConexao);
					                    	
					                    if (responsaveis != null){
					                    	Iterator iteratorResponsavel = responsaveis.iterator();
					                    	while (iteratorResponsavel.hasNext()) {
					                    		PendenciaResponsavelDt dados = (PendenciaResponsavelDt) iteratorResponsavel.next();
					                    		if (dados != null && dados.getId_UsuarioResponsavel() != null && dados.getNomeUsuarioResponsavel() != null){
					                    			usuarioResponsavel = dados.getNomeUsuarioResponsavel();
					                    		}
					                    	}
					                    }	                    	
				                    }
				                    	
				                    if (usuarioResponsavel == null || usuarioResponsavel.trim().length() == 0)
				                    	usuarioResponsavel = nomeServentiaCargoReserva;
				                    	
				                    	String[] pendenciaLiberarStr = { pendencia.getId()+"@#!@"+pendencia.getHash(), pendencia.getPendenciaTipo() + " Para "+pendencia.getNomeUsuarioCadastrador(), pendencia.getDataLimite() , usuarioResponsavel };	                    	
				                    	retorno[8].add(pendenciaLiberarStr);
				            	}
	                    	}
	                    }
	                    
	                    else if (Funcoes.StringToInt(pendencia.getPendenciaTipoCodigo()) == PendenciaTipoDt.ELABORACAO_VOTO){

		                    	String usuarioResponsavel = "";
	                    		
			            		if (nomeServentiaCargoReserva == null || nomeServentiaCargoReserva.trim().length() == 0){
			            			List responsaveis = pendenciaResponsavelNe.consultarResponsaveisDetalhado(pendencia.getId(), null, obFabricaConexao);
				                    	
				                    if (responsaveis != null){
				                    	Iterator iteratorResponsavel = responsaveis.iterator();
				                    	while (iteratorResponsavel.hasNext()) {
				                    		PendenciaResponsavelDt dados = (PendenciaResponsavelDt) iteratorResponsavel.next();
				                    		if (dados != null && dados.getId_UsuarioResponsavel() != null && dados.getNomeUsuarioResponsavel() != null){
				                    			usuarioResponsavel = dados.getNomeUsuarioResponsavel();
				                    		}
				                    	}
				                    }	                    	
			                    }
			                    	
			                    if (usuarioResponsavel == null || usuarioResponsavel.trim().length() == 0)
			                    	usuarioResponsavel = nomeServentiaCargoReserva;
			                    	
			                    	String[] pendenciaLiberarStr = { pendencia.getId()+"@#!@"+pendencia.getHash(), pendencia.getPendenciaTipo() + " Para "+pendencia.getNomeUsuarioCadastrador(), pendencia.getDataInicio() , usuarioResponsavel };	                    	
			                    	retorno[11].add(pendenciaLiberarStr);
	                    	
	                    }
	                   else if (Funcoes.StringToInt(pendencia.getPendenciaTipoCodigo()) == PendenciaTipoDt.SOLICITACAO_CARGA){
		                	  String[] pendenciaStrSolicitacaoCarga = { pendencia.getId()+"@#!@"+pendencia.getHash(), pendencia.getPendenciaTipo(), pendencia.getNomeUsuarioCadastrador(), pendencia.getDataInicio(), pendencia.getDataLimite(), pendencia.getPendenciaTipoCodigo() };
			                  retorno[12].add(pendenciaStrSolicitacaoCarga);
		                }
	                  else
	                	 retorno[0].add(pendenciaStr);
	                
	                }
	                // AGUARDANDO VISTO
	                else if (!pendencia.getDataFim().equals("") && pendencia.getDataVisto().equals("") && pendencia.getDataLimite().equals("")) {
	                    String[] pendenciaStr = { pendencia.getId()+"@#!@"+pendencia.getHash(), pendencia.getPendenciaTipo() + nomeParte, pendencia.getDataInicio(), pendencia.getDataFim() , nomeServentiaCargoReserva };
	                    retorno[1].add(pendenciaStr);
	                }
	
	                // AGUARDANDO LEITURA
	                else if (pendencia.getDataFim().equals("") && !pendencia.getDataVisto().equals("") && Funcoes.StringToInt(pendencia.getId_PendenciaStatus()) == PendenciaStatusDt.ID_AGUARDANDO_VISTO) {
	                    String[] pendenciaStr = { pendencia.getId()+"@#!@"+pendencia.getHash(), pendencia.getPendenciaTipo() + nomeParte, pendencia.getDataInicio(), pendencia.getDataLimite() , nomeServentiaCargoReserva };
	                    retorno[6].add(pendenciaStr);
	                }
	
	                // AGUARDANDO DECURSO DE PRAZO
	                else if (!pendencia.getDataFim().equals("") && pendencia.getDataVisto().equals("") && !pendencia.getDataLimite().equals("")) {
	                    String[] pendenciaStr = { pendencia.getId()+"@#!@"+pendencia.getHash(),pendencia.getPendenciaTipo() + nomeParte, pendencia.getDataFim(), pendencia.getDataLimite() , nomeServentiaCargoReserva};
	                    retorno[5].add(pendenciaStr);
	                }
	                
	                // AGUARDANDO RECEBIMENTO DOS AUTOS
	                else if (pendencia.getDataFim().equals("") && Funcoes.StringToInt(pendencia.getId_PendenciaStatus()) == PendenciaStatusDt.ID_AGUARDANDO_RETORNO && Funcoes.StringToInt(pendencia.getPendenciaTipoCodigo()) == PendenciaTipoDt.SOLICITACAO_CARGA) {
	                	String[] pendenciaStrSolicitacaoCarga = { pendencia.getId()+"@#!@"+pendencia.getHash(), pendencia.getPendenciaTipo(), pendencia.getNomeUsuarioCadastrador(), pendencia.getDataInicio(), pendencia.getDataLimite(), pendencia.getPendenciaTipoCodigo() };
		                retorno[13].add(pendenciaStrSolicitacaoCarga);
	                }
	
	                // MANDADO JUDICIAL AGUARDANDO CUMPRIMENTO
	                else if((Funcoes.StringToInt(pendencia.getPendenciaTipoCodigo()) == PendenciaTipoDt.MANDADO) && pendencia.getDataFim().equals("") && Funcoes.StringToInt(pendencia.getId_PendenciaStatus()) == PendenciaStatusDt.ID_AGUARDANDO_RETORNO && (pendencia.getId_ServentiaCargo() != null && !pendencia.getId_ServentiaCargo().isEmpty())){
	                	String[] pendenciaStr = {pendencia.getId(), pendencia.getPendenciaTipo(), pendencia.getId_MandadoJudicial(), pendencia.getNomeUsuarioFinalizador(), pendencia.getDataInicio(), pendencia.getDataLimite(), ""};
	                	retorno[14].add(pendenciaStr);
	                }
	                
	                // AGUARDANDO RECEBIMENTO
	                else if (pendencia.getDataFim().equals("") && Funcoes.StringToInt(pendencia.getId_PendenciaStatus()) == PendenciaStatusDt.ID_AGUARDANDO_RETORNO) {
	                    String[] pendenciaStr = { pendencia.getId()+"@#!@"+pendencia.getHash(),pendencia.getPendenciaTipo()+ nomeParte, pendencia.getDataInicio() , nomeServentiaCargoReserva};
	                    retorno[2].add(pendenciaStr);
	                }
	                // AGUARDANDO CUMPRIMENTO
	                else if (pendencia.getDataFim().equals("") && Funcoes.StringToInt(pendencia.getId_PendenciaStatus()) == PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO) {
	                    String[] pendenciaStr = { pendencia.getId()+"@#!@"+pendencia.getHash(), pendencia.getPendenciaTipo(), pendencia.getDataInicio() , nomeServentiaCargoReserva, pendencia.getPendenciaTipoCodigo()};
	                    retorno[3].add(pendenciaStr);
	                }
	                // AGUARDANDO CORRECAO
	                else if (pendencia.getDataFim().equals("") && Funcoes.StringToInt(pendencia.getId_PendenciaStatus()) == PendenciaStatusDt.ID_CORRECAO) {
	                	String[] pendenciaStr = { pendencia.getId()+"@#!@"+pendencia.getHash(), pendencia.getPendenciaTipo(), pendencia.getDataInicio() , nomeServentiaCargoReserva, pendencia.getPendenciaTipoCodigo()};
	                    retorno[10].add(pendenciaStr);
	                }
	            }
            }

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return retorno;
    }
    
    /**
	 * M�todo utilizado para descartar pend�ncia(s) de um processo.
	 * 
	 * @param String[],
	 *            identifica��o das pend�ncias
	 * @param UsuarioNe,
	 *            objeto com dados do log e para verificar o hash
	 */
    public void descartarPendencias(String[] ids_Pendencias, UsuarioNe usuarioSessao) throws Exception {
        FabricaConexao obFabricaConexao = null;
		if (ids_Pendencias != null){
	    	for (int i = 0; i < ids_Pendencias.length; i++) {
	    		
	    		
				String[] id_Hash = ids_Pendencias[i].split("@#!@");
				if (usuarioSessao.VerificarCodigoHash(id_Hash[0], id_Hash[1])) {
					try {

						//se a pend�ncia for of�cio delegacia e o status for "Cumprimento Aguardando Visto" quer dizer que a pend�ncia foi respondida pelo delegado e, para descartar, � preciso executar seu descarte de maneira espec�fica
						PendenciaDt pendenciaDt = this.consultarPendenciaId(id_Hash[0], usuarioSessao);
						if(pendenciaDt.getDataFim() != null && !pendenciaDt.getDataFim().equals("") && pendenciaDt.getPendenciaStatusCodigo().equals(String.valueOf(PendenciaStatusDt.ID_CUMPRIMENTO_AGUARDANDO_VISTO))) {
							this.descartarPendenciaCumpridaAguardandoVisto(pendenciaDt, usuarioSessao);
							break;
						}
						
						obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
						obFabricaConexao.iniciarTransacao();
						PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
						
						if (pendenciaDt.getPendenciaStatusCodigo().equals(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_PAGAMENTO_POSTAGEM)) || pendenciaDt.getPendenciaStatusCodigo().equals(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_ENVIO_CORREIOS))) {
							new GuiaItemDisponivelNe().liberarGuiaItemPendencia(pendenciaDt.getId(), pendenciaDt.getId_Processo(), obFabricaConexao);
						}
						
						LogDt obLogDt = new LogDt("Pendencia", id_Hash[0], usuarioSessao.getId_Usuario(), usuarioSessao.getUsuarioDt().getIpComputadorLog(), String.valueOf(LogTipoDt.DescartarPendencia), "", "");
	
						obPersistencia.descartarPendencia(id_Hash[0], usuarioSessao.getUsuarioDt().getId_UsuarioServentia(),PendenciaStatusDt.ID_DESCARTADA);
						obPersistencia.moverPendencia(id_Hash[0]);
						
						obLog.salvar(obLogDt, obFabricaConexao);
						obFabricaConexao.finalizarTransacao();
					} catch (Exception e) {
						 obFabricaConexao.cancelarTransacao();
						throw e;
					} finally {
						if(obFabricaConexao != null) {
							obFabricaConexao.fecharConexao();
						}
					}
				}
			}
		} else{
			throw new MensagemException("Erro ao descartar pend�ncias a lista est� vazia.");
		}

	}
    
	public void descartarPendenciaCumpridaAguardandoVisto(PendenciaDt pendenciaDt, UsuarioNe usuarioSessao) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
			
			//Antes de descartar � preciso criar uma pend�ncia filha
			pendenciaDt.setId_UsuarioLog(usuarioSessao.getUsuarioDt().getId());
			pendenciaDt.setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());
			pendenciaDt.setId_PendenciaPai(pendenciaDt.getId());
			this.gerarPendenciaFilha(pendenciaDt, this.consultarResponsaveis(pendenciaDt), null, null, false, false, false, obFabricaConexao);
			
			//Descartando pend�ncia filha
			LogDt obLogDt = new LogDt("Pendencia", pendenciaDt.getId(), usuarioSessao.getId_Usuario(), usuarioSessao.getUsuarioDt().getIpComputadorLog(), String.valueOf(LogTipoDt.DescartarPendencia), "", "");
			obPersistencia.descartarPendencia(pendenciaDt.getId(), usuarioSessao.getUsuarioDt().getId_UsuarioServentia(), PendenciaStatusDt.ID_DESCARTADA);
			obPersistencia.moverPendencia(pendenciaDt.getId());
			obLog.salvar(obLogDt, obFabricaConexao);
			
			//Finalizando pend�cia pai
			pendenciaDt = this.consultarPendenciaId(pendenciaDt.getId_PendenciaPai(), usuarioSessao);
			LogDt obLogDt2 = new LogDt("Pendencia", pendenciaDt.getId(), usuarioSessao.getId_Usuario(), usuarioSessao.getUsuarioDt().getIpComputadorLog(), String.valueOf(LogTipoDt.DescartarPendencia), obDados.getPropriedades(), pendenciaDt.getPropriedades());
            //Apesar de ser um descarte, a pend�ncia pai foi devidamente cumprida, logo seu status deve ser CUMPRIDA
			obPersistencia.fecharPendenciaCumpridaAguardandoVisto(pendenciaDt.getId(), PendenciaStatusDt.ID_CUMPRIDA, new Date());
           	obPersistencia.moverPendencia(pendenciaDt.getId());
            
            obLog.salvar(obLogDt2, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
    
    /**
	 * M�todo utilizado para descartar uma pend�ncia
	 * 
	 * @param pendenciaDt,
	 *            identifica��o da pend�ncia
	 * @param usuarioDt,
	 *            objeto com dados do log
	 */
    public void descartarPendencia(PendenciaDt pendenciaDt, UsuarioDt usuarioDt) throws Exception {
        FabricaConexao obFabricaConexao = null;
    	try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            obFabricaConexao.iniciarTransacao();            
            
            descartarPendencia(pendenciaDt, usuarioDt, obFabricaConexao);
			
			obFabricaConexao.finalizarTransacao();
			
    	} catch (Exception e) {
    		obFabricaConexao.cancelarTransacao();
    		throw e;
    	} finally {
	         obFabricaConexao.fecharConexao();
	    }
    }
    
    /**
	 * M�todo utilizado para descartar uma pend�ncia
	 * 
	 * @param pendenciaDt,
	 *            identifica��o da pend�ncia
	 * @param usuarioDt,
	 *            objeto com dados do log
	 * @param conexao,
	 *            conex�o com o banco de dados        
	 */
    public void descartarPendencia(PendenciaDt pendenciaDt, UsuarioDt usuarioDt, FabricaConexao conexao) throws Exception {                
            
        // Fecha pend�ncia com status Descartada
        pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_DESCARTADA));
        Date data = new Date();
		
		LogDt obLogDt = new LogDt("Pendencia", pendenciaDt.getId(), usuarioDt.getId(), 
				usuarioDt.getIpComputadorLog(), String.valueOf(LogTipoDt.DescartarPendencia),"","");
		
		obLog.salvar(obLogDt, conexao);
		
		// Verifica se a pendencia a ser descartada � do tipo voto, se for verifica se existe uma pend�ncia do tipo Ementa vinculada a este voto
		if (pendenciaDt.getPendenciaTipoCodigo() != null && pendenciaDt.getPendenciaTipoCodigo().equalsIgnoreCase(String.valueOf(PendenciaTipoDt.CONCLUSO_VOTO))) {
			AudienciaProcessoDt audienciaProcesso = new AudienciaProcessoNe().consultarCompletoPelaPendenciaDeVoto(pendenciaDt.getId());
			String idAudienciaProcesso = "";
			if (audienciaProcesso != null) idAudienciaProcesso = audienciaProcesso.getId();
			List listaPendenciasVotoEmentaProcesso = this.consultarPendenciasVotoEmentaProcesso(pendenciaDt.getId_Processo(), usuarioDt.getId_ServentiaCargo(), String.valueOf(PendenciaTipoDt.CONCLUSO_EMENTA), idAudienciaProcesso, conexao); 
			PendenciaDt pendenciaEmentaDt = null;
			if (listaPendenciasVotoEmentaProcesso != null && listaPendenciasVotoEmentaProcesso.size() > 0) pendenciaEmentaDt = (PendenciaDt) listaPendenciasVotoEmentaProcesso.get(0);
			if (pendenciaEmentaDt != null) {
				
				pendenciaEmentaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_DESCARTADA));
				pendenciaEmentaDt.setDataFim(Funcoes.DataHora(data));
				this.fecharPendencia(pendenciaEmentaDt, usuarioDt, conexao);
				
				obLogDt = new LogDt("Pendencia", pendenciaEmentaDt.getId(), usuarioDt.getId(), 
						usuarioDt.getIpComputadorLog(), String.valueOf(LogTipoDt.DescartarPendencia),"","");
				
				obLog.salvar(obLogDt, conexao);
			}
		}
		
		pendenciaDt.setDataFim(Funcoes.DataHora(data));
		this.fecharPendencia(pendenciaDt, usuarioDt, conexao);
    }
    
    public byte[] gerarPdfPublicacao(String diretorioProjeto, String stIdArquivo, UsuarioDt usuarioDt, ProcessoDt processoDt) throws Exception {
        ArquivoNe arquivoNe = new ArquivoNe();
        ArquivoDt arquivoPublicacao = arquivoNe.consultarId(stIdArquivo, false);
        byte[] byTemp = null;
        boolean boHtml = false;
        String pathImage = diretorioProjeto + "imagens" + File.separator + "TesteChave.gif";
        
        byte[] out = null;
        String stNome = arquivoPublicacao.getNomeArquivo().toLowerCase();
        
        if (stNome.indexOf(".mp3") > 0) throw new MensagemException("Imposs�vel converter um arquivo mp3 em pdf.");

        if (stNome.indexOf(".html") > 0) boHtml = true;

        out = arquivoPublicacao.getConteudo();               
               
        try {
        	if (boHtml) out = ConverterHtmlPdf.converteHtmlPDF(out, false);
		} catch (Exception e) {
			try {
	        	if (boHtml) out = ConverterHtmlPdf.converteHtmlPDFAlternativo(out);
			} catch (Exception e2) {
				out = GerarPDF.gerarMensagemPDF(arquivoPublicacao.getNomeArquivoFormatado(), "");
			}
		}
        
        String enderecoConferencia = ProjudiPropriedades.getInstance().getEnderecoValidacaoPublicacao();
        String textoPrimeiraLinhaPDF = "Tribunal de Justi�a do Estado de Goi�s";
        String textoSegundaLinhaPDF = "Documento Assinado e Publicado Digitalmente em " + arquivoPublicacao.getDataInsercao();
        String textoTerceiraLinhaPDF = "Assinado por " + arquivoPublicacao.getUsuarioAssinadorFormatado();
        String textoQuartaLinhaPDF = "Valida��o pelo c�digo: " + Cifrar.codificar(arquivoPublicacao.getId(),  PendenciaArquivoDt.CodigoPermissao) + ", no endere�o: " + enderecoConferencia;

        byTemp = EscreverTextoPDF.escreverTextoPDF(out, pathImage, textoPrimeiraLinhaPDF, textoSegundaLinhaPDF, textoTerceiraLinhaPDF, textoQuartaLinhaPDF, usuarioDt, processoDt);

        return byTemp;
    }
    
    /**
     * Gerar pdf de uma publica��o
     * 
     * @since 15/09/2009
     * @param String
     *            stIdArquivo, id de um arquivo de uma publica��o (pendencia do
     *            tipo publica��o)
     * @return byte[] , retorna bytes contendo a publica��o em pdf
     * @throws Exception
     */
    public byte[] gerarPdfPublicacao(String diretorioProjeto, String stIdArquivo) throws Exception {
        ArquivoNe arquivoNe = new ArquivoNe();
        ArquivoDt arquivoPublicacao = arquivoNe.consultarIdAtivo(stIdArquivo, true, false);
                
        if (arquivoPublicacao==null) return null;
        
        byte[] byTemp = null;
        boolean boHtml = false;
        
        String pathImage = diretorioProjeto + "imagens" + File.separator + "TesteChave.gif";
        
        byte[] out = null;
        String stNome = arquivoPublicacao.getNomeArquivo().toLowerCase();
        
        if (stNome.indexOf(".mp3") > 0) throw new MensagemException("Imposs�vel converter um arquivo mp3 em pdf.");

        if (stNome.indexOf(".html") > 0) boHtml = true;

        String textoSegundaLinhaPDF;
        String textoTerceiraLinhaPDF;
        
        if (arquivoPublicacao.getUsuarioAssinador() != null && arquivoPublicacao.getUsuarioAssinador().length()==0){
        	out = arquivoPublicacao.getConteudoSemAssinar();
        	textoSegundaLinhaPDF = "Documento Publicado Digitalmente em " + arquivoPublicacao.getDataInsercao();
        	textoTerceiraLinhaPDF = "Assinado no Sistema PJE";
     		
        }else {
        	out = arquivoPublicacao.getConteudo(); 
        	textoSegundaLinhaPDF = "Documento Assinado e Publicado Digitalmente em " + arquivoPublicacao.getDataInsercao();
        	textoTerceiraLinhaPDF = "Assinado por " + arquivoPublicacao.getUsuarioAssinadorFormatado();
        }
        
        if (boHtml) out = ConverterHtmlPdf.converteHtmlPDF(out, false);

        String enderecoConferencia = ProjudiPropriedades.getInstance().getEnderecoValidacaoPublicacao();
        String textoPrimeiraLinhaPDF = "Tribunal de Justi�a do Estado de Goi�s";
        String textoQuartaLinhaPDF = "Valida��o pelo c�digo: " + Cifrar.codificar(arquivoPublicacao.getId(), PendenciaArquivoDt.CodigoPermissao) + ", no endere�o: " + enderecoConferencia;

        byTemp = EscreverTextoPDF.escreverTextoPDF(out, pathImage, textoPrimeiraLinhaPDF, textoSegundaLinhaPDF, textoTerceiraLinhaPDF, textoQuartaLinhaPDF, true);
        
        return byTemp;
    }
    
    /**
     * Gerar pdf de uma publica��o
     * @param arquivoPublicacao
     * @param hpc
     * @param diretorioProjeto
     * @return
     * @throws Exception
     */
    public byte[] gerarPdfPublicacao (ArquivoDt arquivoPublicacao, HtmlPipelineContext hpc, String diretorioProjeto) throws Exception {
    	
    	byte[] arrTemp = null;
    	
    	if (arquivoPublicacao.isConteudo()){
			
    		ProcessoNe processoNe = new ProcessoNe();
    		ProcessoDt processoDt = processoNe.consultarPorIdArquivo(arquivoPublicacao.getId());
    		
    		String pathImage = diretorioProjeto + "imagens" + File.separator + "TesteChave.gif";
    		
			String stNome = arquivoPublicacao.getNomeArquivo().toLowerCase();            		
    		String[] extensoes = stNome.split("\\.");
			String extensaoFinal = extensoes[extensoes.length - 2];
			
			byte[] out = arquivoPublicacao.getConteudo();
									
			if (extensaoFinal.equalsIgnoreCase("html")) {
				try {
					out = ConverterHtmlPdf.converteHtmlPDF(out, hpc);
				} catch (Exception e) {
					out = GerarPDF.gerarMensagemPDF(arquivoPublicacao.getNomeArquivoFormatado(), "");
				}						
			} else if (extensaoFinal.equalsIgnoreCase("jpg")){
				out = ConverteImagemPDF.gerarPdfImagem(out);
				
			} else if (!(extensaoFinal.equalsIgnoreCase("pdf"))){
				out = GerarPDF.gerarPDF(arquivoPublicacao.getNomeArquivoFormatado(), "Este arquivo n�o pode ser aberto, pois n�o est� no formato \".pdf\" ou \".html\"");
			}
			
			String enderecoConferencia = ProjudiPropriedades.getInstance().getEnderecoValidacaoPublicacao();
            String textoPrimeiraLinhaPDF = "Tribunal de Justi�a do Estado de Goi�s";
            String textoSegundaLinhaPDF = "Documento Assinado e Publicado Digitalmente em " + arquivoPublicacao.getDataInsercao();
            String textoTerceiraLinhaPDF = "Assinado por " + arquivoPublicacao.getUsuarioAssinadorFormatado();
            String textoQuartaLinhaPDF = "Valida��o pelo c�digo: " + Cifrar.codificar(arquivoPublicacao.getId(), PendenciaArquivoDt.CodigoPermissao) + ", no endere�o: " + enderecoConferencia;
            
            arrTemp = EscreverTextoPDF.escreverTextoPDF(out, pathImage, textoPrimeiraLinhaPDF, textoSegundaLinhaPDF, textoTerceiraLinhaPDF, textoQuartaLinhaPDF, processoDt);
		}
    	
    	return arrTemp;    	
    }
    
    
    
    /**
     * Gerar pdf de uma publica��o
     * 
     * @since 09/09/2014
     * @param String
     *            stIdArquivo, id de um arquivo de uma arquivo de mandado de pris�o (pendencia do tipo publica��o)
     * @return byte[] , retorna bytes contendo o mandado de pris�o
     * @throws Exception
     */
    public byte[] gerarPdfMandadoPrissaoAtivo(String diretorioProjeto, String stIdArquivo) throws Exception {
        ArquivoNe arquivoNe = new ArquivoNe();
        ArquivoDt arquivoPublicacao = arquivoNe.consultarIdMandadoPrisaoAtivo(stIdArquivo, true, false);
                
        if (arquivoPublicacao==null) return null;
        
        byte[] byTemp = null;
        boolean boHtml = false;
        
        String pathImage = diretorioProjeto + "imagens" + File.separator + "TesteChave.gif";
        
        byte[] out = null;
        String stNome = arquivoPublicacao.getNomeArquivo().toLowerCase();
        
        if (stNome.indexOf(".mp3") > 0) throw new MensagemException("Imposs�vel converter um arquivo mp3 em pdf.");

        if (stNome.indexOf(".html") > 0) boHtml = true;

        out = arquivoPublicacao.getConteudo();               
        
        if (boHtml) out = ConverterHtmlPdf.converteHtmlPDF(out, false);

        String enderecoConferencia = ProjudiPropriedades.getInstance().getEnderecoValidacaoPublicacao();
        String textoPrimeiraLinhaPDF = "Tribunal de Justi�a do Estado de Goi�s";
        String textoSegundaLinhaPDF = "Documento Assinado e Publicado Digitalmente em " + arquivoPublicacao.getDataInsercao();
        String textoTerceiraLinhaPDF = "Assinado por " + arquivoPublicacao.getUsuarioAssinadorFormatado();
        String textoQuartaLinhaPDF = "Valida��o pelo c�digo: " + Cifrar.codificar(arquivoPublicacao.getId(), PendenciaArquivoDt.CodigoPermissao) + ", no endere�o: " + enderecoConferencia;

        byTemp = EscreverTextoPDF.escreverTextoPDF(out, pathImage, textoPrimeiraLinhaPDF, textoSegundaLinhaPDF, textoTerceiraLinhaPDF, textoQuartaLinhaPDF, true);
        
        return byTemp;
    }
    
    
    public void gerarPdfPublicacaoDJEPorData(MovimentacaoIntimacaoDt movimentacaoIntimacaoDt, PdfCopy copy, List<HashMap<String, Object>> bookmarks, boolean superior) throws Exception {
    	
    	// Cria a p�gina com os dados da pend�ncia
    	byte[] capa = GerarCapaIntimacaoPDF.gerarPaginaResumoParaPublicacao(movimentacaoIntimacaoDt);
    	if (capa != null) copy = ConcatenatePDF.concatPDFs(copy, capa);
    	
    	// Atualiza a quantidade de p�ginas
    	int pageOffset = copy.getCurrentPageNumber()-1;
        
    	// Define o indice para o arquivo
    	ProcessoDt proc = movimentacaoIntimacaoDt.getProcessoDt();
		String titulo = movimentacaoIntimacaoDt.getTipoMovimentacao() + " - " + Funcoes.FormatarDataHora(movimentacaoIntimacaoDt.getDataRealizacao()); 
		GerarCapaIntimacaoPDF.definirIndice(titulo, proc.getServentia().toUpperCase(), pageOffset, bookmarks, superior);
    	
    }	
    
    /**
     * Cria um arquivo com todas as intima��es expedidas na data espec�fica.
     * Se o processo � segredo de justi�a, os nomes das partes e intimadas ser�o
     * exibidas somente com as iniciais.
     * @param processoIntimacao
     * @param hpc
     * @param diretorioProjeto
     * @return byte[]
     * @throws Exception
     */
    public void gerarPdfIntimacaoPorData(MovimentacaoIntimacaoDt movimentacaoIntimacaoDt, HtmlPipelineContext hpc, String diretorioProjeto, PdfCopy copy, List<HashMap<String, Object>> bookmarks, boolean superior) throws Exception {
    	
    	if (movimentacaoIntimacaoDt.getPendencias() != null){
    	
    		// Cria a p�gina com os dados da pend�ncia
        	byte[] capa = GerarCapaIntimacaoPDF.gerarPaginaIntimacoesProcesso(movimentacaoIntimacaoDt);
        	if (capa != null) copy = ConcatenatePDF.concatPDFs(copy, capa);
            
        	// Atualiza a quantidade de p�ginas
        	int pageOffset = copy.getCurrentPageNumber()-1;
            
        	// Define o indice para o arquivo
        	ProcessoDt proc = movimentacaoIntimacaoDt.getProcessoDt();
    		PendenciaDt pe = movimentacaoIntimacaoDt.getPendencias().get(0);
    		
    		String tipoPendencia = pe.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.CARTA_CITACAO ?  "CARTA DE CITA��O" : "INTIMA��O";
    		String titulo = tipoPendencia + " EFETIVADA REF. � MOV. " + pe.getMovimentacao() + " - " + Funcoes.FormatarDataHora(pe.getDataTemp());        	
    		if (movimentacaoIntimacaoDt.getTipoMovimentacao().toUpperCase().startsWith("AUDI")){
				titulo += movimentacaoIntimacaoDt.getComplemento() != null ? " " + movimentacaoIntimacaoDt.getComplemento() : "";
			}
			titulo += " - Data da Movimenta��o " + Funcoes.FormatarDataHora(pe.getDataTemp());
    		
			GerarCapaIntimacaoPDF.definirIndice(titulo, proc.getServentia().toUpperCase(), pageOffset, bookmarks, superior);
    		
    	} else {
    	
    		/*
    		 * Publica��o sem pend�ncia - advogados externos
    		 */    		    		
    		byte[] capa = GerarCapaIntimacaoPDF.gerarPaginaIntimacoesParaAdvogadoExternoAoProcesso(movimentacaoIntimacaoDt);
        	if (capa != null) copy = ConcatenatePDF.concatPDFs(copy, capa);
            
        	// Atualiza a quantidade de p�ginas
        	int pageOffset = copy.getCurrentPageNumber()-1;
    		    		
    		// Define o indice para o arquivo
    		ProcessoDt proc = movimentacaoIntimacaoDt.getProcessoDt();
    		String titulo = movimentacaoIntimacaoDt.getTipoMovimentacao();
    		    		    		
    		GerarCapaIntimacaoPDF.definirIndice(titulo, proc.getServentia().toUpperCase(), pageOffset, bookmarks, superior);
    		
    	}
    	    	    	
    	// Se tem arquivos, incluir na sa�da
    	if (movimentacaoIntimacaoDt.getArquivos() != null){
    		
        	// Consulta os arquivos da movimenta��o da pend�ncia
        	ArquivoNe arquivoNe = new ArquivoNe();
        	movimentacaoIntimacaoDt.setArquivos(arquivoNe.consultaArquivosComAcessoNormalPublicoParaPublicacao(movimentacaoIntimacaoDt.getId()));
        	
    		for (ArquivoDt arquivoIntimacao : (List<ArquivoDt>) movimentacaoIntimacaoDt.getArquivos()){
    			
    			if (!movimentacaoIntimacaoDt.getProcessoDt().isSegredoJustica()){
    				
    				try {
						
    					// Se n�o tem conte�do, porque os dados n�o est�o no banco e sim no storage
    					if (!arquivoIntimacao.isConteudo()) arquivoIntimacao = arquivoNe.consultarId(arquivoIntimacao.getId(), false);
    					
    					if (arquivoIntimacao.isConteudo()){
    						
    						String pathImage = diretorioProjeto + "imagens" + File.separator + "TesteChave.gif";
        					
        					byte[] out = null; 
        					
        					if (arquivoIntimacao.isECarta()){
        						out = arquivoIntimacao.obterConteudoECarta();
        					} else {
        						out = arquivoIntimacao.getConteudo();
        					}
        					
        					// Se o arquivo � do tipo PDF, n�o precisa ser manipulado. Somente no caso de ser html ou imagem
    						// Se for um tipo diferente desses 3, uma p�gina pdf ser� criado com mensagem de alerta.
    						if (arquivoIntimacao.isArquivoPodeSerHtml()){
    							try {
    								out = ConverterHtmlPdf.converteHtmlPDF(out, hpc);
    							} catch (Exception e) {
    								out = GerarPDF.gerarMensagemPDF(arquivoIntimacao.getNomeArquivoFormatado(), "N�o foi poss�vel gerar o arquivo para o DJ.");
    							}
    						} else if (arquivoIntimacao.isImagemJPEG()){
    							out = ConverteImagemPDF.gerarPdfImagem(out);
    															
    						} else if (!arquivoIntimacao.isArquivoPodeSerPDF()){
    							out = GerarPDF.gerarPDF(arquivoIntimacao.getNomeArquivoFormatado(), "Este arquivo n�o pode ser aberto, pois n�o est� no formato \".pdf\" ou \".html\"");
    						}
    						
    						String enderecoConferencia = ProjudiPropriedades.getInstance().getEnderecoValidacaoPublicacao();
    						String textoPrimeiraLinhaPDF = "Tribunal de Justi�a do Estado de Goi�s";
    						String textoSegundaLinhaPDF = "Documento Assinado e Publicado Digitalmente em " + arquivoIntimacao.getDataInsercao();
    						String textoTerceiraLinhaPDF = "Assinado por " + arquivoIntimacao.getUsuarioAssinadorFormatado();
    						String textoQuartaLinhaPDF = "Valida��o pelo c�digo: " + Cifrar.codificar(arquivoIntimacao.getId(), PendenciaArquivoDt.CodigoPermissao) + ", no endere�o: " + enderecoConferencia;
    						
    						Object[] blocoDireito = GerarCapaIntimacaoPDF.getCarimboLadoDireito(movimentacaoIntimacaoDt);
    						
    						byte[] arrTemp = EscreverTextoPDF.escreverTextoPDF(out, pathImage, textoPrimeiraLinhaPDF, textoSegundaLinhaPDF, textoTerceiraLinhaPDF, textoQuartaLinhaPDF, blocoDireito);
    						if (arrTemp != null) copy = ConcatenatePDF.concatPDFs(copy, arrTemp);
    						
    					}
    					    											
					} catch (Exception e) {
						e.printStackTrace();
						// Se acontecer algum erro ao abrir o arquivo da intima��o, pular para a pr�xima.
						// System.out.println(arquivoIntimacao.getId());
						continue;
					}                        
    				
    			}
    			
    		}
    		    		
    		if (movimentacaoIntimacaoDt.getArquivos() != null){
    			movimentacaoIntimacaoDt.getArquivos().clear();
    			movimentacaoIntimacaoDt.setArquivos(null);
    		}
    		
    	}
        
    }
    
   /**
     * Consulta  tipos de pend�ncia definidos para um grupo 
     * 
     * @author Leandro Bernardes
  
     * @return List
     * @throws Exception
     */
    public List consultarDescricaoGrupoPendenciaTipo(String tempNomeBusca, UsuarioNe usuarioNe, String PosicaoPaginaAtual) throws Exception {
        List tempList = null;
        GrupoPendenciaTipoNe GrupoPendenciaTipone = new GrupoPendenciaTipoNe();
        tempList = GrupoPendenciaTipone.consultarGrupoPendenciaTipo(tempNomeBusca, usuarioNe.getUsuarioDt(), PosicaoPaginaAtual);
        QuantidadePaginas = GrupoPendenciaTipone.getQuantidadePaginas();
        GrupoPendenciaTipone = null;
        return tempList;
    }

    /**
     * Consulta pendencia tipo por id
     * 
     * @author Leandro Bernardes
     * @param String idPendenciaTipo, id da pendencia tipo
     * @return PendenciaTipoDt
     * @throws Exception
     */
    public PendenciaTipoDt consultarPendenciaTipoId(String idPendenciaTipo) throws Exception {
        PendenciaTipoDt pendenciaTipoDt = new PendenciaTipoDt();
        PendenciaTipoNe pendenciaTipoNe = new PendenciaTipoNe();
        pendenciaTipoDt = pendenciaTipoNe.consultarId(idPendenciaTipo);
        pendenciaTipoNe = null;
        return pendenciaTipoDt;
    }

    /**
     * Cria uma publicacao de arquivos j� salvos
     * 
     * @author msapaula
     * @param arquivos,
     *            arquivos inseridos
     * @param usuarioDt,
     *            usuario de sesssao
     * @param logDt,
     *            dados do log
     * @param conexao,
     *            conex�o ativa
     */
	public void salvarPublicacao(PendenciaDt pendenciaDt, List arquivos, UsuarioDt usuarioDt, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
        if (arquivos == null || arquivos.size() == 0) {
        	throw new MensagemException("N�o foi poss�vel publicar o(s) arquivo(s), pois a lista de arquivos est� vazia");
        }

        // Configura a pendencia de publicacao
        pendenciaDt.setPendenciaTipoCodigo(String.valueOf(PendenciaTipoDt.PUBLICACAO_PUBLICA));
        pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_CUMPRIDA));
        pendenciaDt.setId_UsuarioCadastrador(usuarioDt.getId_UsuarioServentia());
        pendenciaDt.setId_UsuarioFinalizador(usuarioDt.getId_UsuarioServentia());
        Date data = new Date();
        pendenciaDt.setDataFim(Funcoes.DataHora(data));
        pendenciaDt.setDataVisto(Funcoes.DataHora(data));
        pendenciaDt.setId_UsuarioLog(logDt.getId_Usuario());
        pendenciaDt.setIpComputadorLog(logDt.getIpComputador());

        PendenciaResponsavelDt pendenciaResponsavelDt = new PendenciaResponsavelDt();
        pendenciaResponsavelDt.setId_Serventia(usuarioDt.getId_Serventia());
        pendenciaDt.addResponsavel(pendenciaResponsavelDt);

        // Inseri a pendencia
        this.gerarPendencia(pendenciaDt, obFabricaConexao);

        // Publica os arquivos
        PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
        pendenciaArquivoNe.publicarArquivos(pendenciaDt, arquivos, true,  obFabricaConexao);
        
        //� necess�rio finalizar a pend�ncia de publica��o para 
        //evitar inconsist�ncia e j� inseri-la na tabela PEND_FINAL
        this.finalizarPublicacao(pendenciaDt, usuarioDt, obFabricaConexao);
    }
	
	public void salvarPublicacao(PendenciaDt pendenciaDt, List arquivos, UsuarioDt usuarioDt, LogDt logDt) throws Exception {
		FabricaConexao fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		
		try {
			fabrica.iniciarTransacao();
			
			salvarPublicacao(pendenciaDt, arquivos, usuarioDt, logDt, fabrica);
			
			fabrica.finalizarTransacao();
		}catch (Exception e) {
			fabrica.cancelarTransacao();
			throw e;
		} finally {
			fabrica.fecharConexao();
		}
    }
    
    /**
	 * Consulta a pend�ncia do tipo "Suspens�o de Processo" para um processo passado
	 * @param id_Processo, identifica��o do processo
	 * @return
	 * @throws Exception
	 */
    public PendenciaDt consultarPendenciaSuspensaoProcesso(String id_Processo, FabricaConexao obFabricaConexao) throws Exception {
		PendenciaDt dtRetorno=null;
		PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
		dtRetorno= obPersistencia.consultarPendenciaSuspensaoProcesso(id_Processo);
		return dtRetorno;
	}
    
    /**
     * M�todo para controlar a marca��o de Aus�ncia das Partes
     * @param dados, objeto com dados das pend�ncias marcadas
     * @param processoDt, processo que ter� partes marcadas com aus�ncia
     * @param processoParteTipo, tipo da parte a ser marcada com aus�ncia
     * @param logDt, objeto com dados do log
     * @param conexao, conex�o ativa
     * 
     * @author msapaula
     */
    public void marcarAusenciaPartesProcesso(dwrMovimentarProcesso dados, ProcessoDt processoDt, int processoParteTipo, LogDt logDt, FabricaConexao conexao) throws Exception {
    	ProcessoParteAusenciaNe processoParteAusenciaNe = new ProcessoParteAusenciaNe();
        List listaPartesProcesso = null;
        if (processoParteTipo == ProcessoParteTipoDt.POLO_ATIVO_CODIGO)
        	listaPartesProcesso = processoDt.getListaPolosAtivos();
        else if (processoParteTipo == ProcessoParteTipoDt.POLO_PASSIVO_CODIGO) listaPartesProcesso = processoDt.getListaPolosPassivos();

        switch (dados.getTipoDestinatario()) {
        case 1: // TODOS PROMOVENTES OU PROMOVIDOS
        	processoParteAusenciaNe.marcarAusenciaProcessoParte(listaPartesProcesso, logDt, conexao);
            break;

        case 2: // PARTE ESPEC�FICA
        	ProcessoParteDt parteDt = new ProcessoParteDt();
            parteDt.setId(dados.getIdDestinatario());
            parteDt.setNome(dados.getDestinatario());
        	processoParteAusenciaNe.marcarAusenciaProcessoParte(parteDt, logDt, conexao);
            break;
        }
    }
    
    /**
	 * Consulta as pend�ncias que foram analisadas, sendo aquelas com tratamento semelhante � conclus�o
	 * 
	 * @param usuarioDt, usu�rio respons�vel pelas pend�ncias
	 * @param numeroProcesso, filtro n�mero do processo
	 * @param dataInicial, data inicial para consulta
	 * @param dataFinal, data final para consulta
	 * 
	 * @author msapaula
	 */
    public List consultarPendenciasAnalisadas(UsuarioDt usuarioDt, String numeroProcesso, String dataInicial, String dataFinal) throws Exception {
        List pendencias = null;
        String digitoVerificador = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

            // Divide n�mero de processo do d�gito verificador
            String[] stTemp = numeroProcesso.split("\\.");
            if (stTemp.length >= 1) numeroProcesso = stTemp[0];
            else numeroProcesso = "";
            if (stTemp.length >= 2) digitoVerificador = stTemp[1];
            
            //int grupo = Funcoes.StringToInt(usuarioDt.getGrupoCodigo());
            int grupoTipo = Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo());
            switch (grupoTipo) {
//                case GrupoDt.DESEMBARGADOR:
//                case GrupoDt.ASSISTENTES_GABINETE:
//                case GrupoDt.PROMOTORES:
	            case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
	            case GrupoTipoDt.ASSISTENTE_GABINETE:
	            case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
	            case GrupoTipoDt.MP:
                    if (usuarioDt.getId_ServentiaCargo() != null && usuarioDt.getId_ServentiaCargo().length() > 0){
                    	 pendencias = obPersistencia.consultarPendenciasAnalisadas(usuarioDt.getId_ServentiaCargo(), null, numeroProcesso, digitoVerificador, dataInicial, dataFinal);
                    }
                    break;                    
	            case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
                    if (usuarioDt.getId_ServentiaCargoUsuarioChefe() != null && usuarioDt.getId_ServentiaCargoUsuarioChefe().length() > 0){
                    	 pendencias = obPersistencia.consultarPendenciasAnalisadas(usuarioDt.getId_ServentiaCargoUsuarioChefe(), null, numeroProcesso, digitoVerificador, dataInicial, dataFinal);
                    }
                    break;
                    
//                case GrupoDt.ADVOGADOS:
	            case GrupoTipoDt.ADVOGADO:
                	 pendencias = obPersistencia.consultarPendenciasAnalisadas(null, usuarioDt.getId_UsuarioServentia(), numeroProcesso, digitoVerificador, dataInicial, dataFinal);
                	break;
            }

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }
    
    /**
     * Cria uma pendencia para verificar a distribui��o de um novo processo
     * 
     * @param processoDt, bean do processo
     * @param id_UsuarioServentia, id do usuario da serventia
     * @param id_Movimentacao, movimenta��o vinculada a pend�ncia
     * @param logDt, objeto com dados do log
     * @param obFabricaConexao, fabrica de conexao para continuar uma trasacao existente
     * @throws Exception
     */
    public void gerarPendenciaVerificarDistribuicao(ProcessoDt processoDt, String id_UsuarioServentia, String id_Movimentacao, LogDt logDt, List arquivos, FabricaConexao obFabricaConexao) throws Exception {
    	
    	//ServentiaCargoDt serventiaCargoDistribuidor = new ServentiaCargoNe().getDistribuidorCamara(processoDt.getId_Serventia(), obFabricaConexao);
    	if (processoDt != null && processoDt.getId_Serventia() != null && !processoDt.getId_Serventia().equals("")){
    		this.gerarPendenciaProcesso(processoDt.getId(), arquivos, PendenciaTipoDt.VERIFICAR_DISTRIBUICAO, id_Movimentacao, id_UsuarioServentia, "", "", "", PendenciaStatusDt.ID_EM_ANDAMENTO, "", processoDt.getId_Serventia(), "", "", processoDt.getId_ProcessoPrioridade(), null, logDt, obFabricaConexao);
    	} else throw new MensagemException("N�o foi poss�vel identificar C�mara do processo.");

    }
    
    /**
     * Cria uma pendencia para verificar a distribui��o de um novo processo
     * 
     * @param processoDt, bean do processo
     * @param id_UsuarioServentia, id do usuario da serventia
     * @param id_ServentiaNova, id do serventia que recebera o processo
     * @param id_Movimentacao, movimenta��o vinculada a pend�ncia
     * @param logDt, objeto com dados do log
     * @param obFabricaConexao, fabrica de conexao para continuar uma trasacao existente
     * @throws Exception
     */
    public void gerarPendenciaVerificarDistribuicao(ProcessoDt processoDt, String id_UsuarioServentia, String id_ServentiaNova, String id_Movimentacao, LogDt logDt, List arquivos, FabricaConexao obFabricaConexao) throws Exception {
    	
    	if (processoDt != null){
    		this.gerarPendenciaProcesso(processoDt.getId(), arquivos, PendenciaTipoDt.VERIFICAR_DISTRIBUICAO, id_Movimentacao, id_UsuarioServentia, "", "", "", PendenciaStatusDt.ID_EM_ANDAMENTO, "", id_ServentiaNova, "", "", processoDt.getId_ProcessoPrioridade(), null, logDt, obFabricaConexao);
    	} else throw new MensagemException("N�o foi poss�vel identificar C�mara do processo.");

    }
    
    /**
     * Salva uma pend�ncia do tipo Pedido Laudo e Agendamento de Per�cia para uma "Psico-Social Forense". Ap�s
     * obter a serventia do tipo "Psico-Social Forense" relacionada com a serventia do
     * processo, deve adicionar essa como respons�vel pela pend�ncia.
     * 
     * @param pendenciaDt, pend�ncia que ser� gerada
     * @param processoDt, processo vinculado a pend�ncia
     * @param responsavel, respons�vel pela pend�ncia
     * @param arquivos, arquivos vinculados a pend�ncia que ser� gerada
     * @param logDt, dados do log
     * @param obFabricaConexao, conex�o ativa
     * 
     * @author M�rcio Gomes
     */
    private void gerarPendenciaServentiaEquipeInterProfissional(PendenciaDt pendenciaDt, ProcessoDt processoDt, PendenciaResponsavelDt responsavel, List arquivos, FabricaConexao obFabricaConexao) throws Exception {

        ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();
        ServentiaDt serventiaEquipeInterProfissional = serventiaRelacionadaNe.consultarServentiaEquipeInterProfissionalRelacionada(processoDt.getId_Serventia());

        if (serventiaEquipeInterProfissional != null) {
            // Se encontrou uma serventia relacionada, essa ser� a respons�vel pela pend�ncia
            responsavel.setId_Serventia(serventiaEquipeInterProfissional.getId());

            PendenciaStatusNe pendenciaStatusNe = new PendenciaStatusNe();
            PendenciaStatusDt pendenciaStatusDt = pendenciaStatusNe.consultarPendenciaStatusCodigo(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO);
            pendenciaDt = pendenciaDt.criarPendenciaFilhaExpedida();
            pendenciaDt.setId_PendenciaStatus(pendenciaStatusDt.getId());
            pendenciaDt.setDataVisto("");

        } else {
            // Se n�o h� nenhuma serventia relacionada, a pend�ncia ser� gerada para a serventia do processo expedir
            responsavel.setId_Serventia(processoDt.getId_Serventia());
        }

        pendenciaDt.addResponsavel(responsavel);
        this.gerarPendencia(pendenciaDt, arquivos, false, obFabricaConexao);

    }
    
    /**
     * Salva uma pend�ncia do tipo Pedido Justi�a Restaurativa para uma "servetian Justi�a Restaurativa". Ap�s
     * obter a serventia do tipo "Justi�a Restaurativa" relacionada com a serventia do processo, deve adicionar essa como respons�vel pela pend�ncia.
     * 
     * @param pendenciaDt, pend�ncia que ser� gerada
     * @param processoDt, processo vinculado a pend�ncia
     * @param responsavel, respons�vel pela pend�ncia
     * @param arquivos, arquivos vinculados a pend�ncia que ser� gerada
     * @param logDt, dados do log
     * @param obFabricaConexao, conex�o ativa
     * 
     * @author lsbernardes
     */
    private void gerarPendenciaServentiaEquipeInterProfissionalJusticaRestaurativa(PendenciaDt pendenciaDt, ProcessoDt processoDt, PendenciaResponsavelDt responsavel, List arquivos, FabricaConexao obFabricaConexao) throws Exception {

        ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();
        ServentiaDt serventiaJusticaRestaurativa = serventiaRelacionadaNe.consultarServentiaJusticaRestaurativaRelacionada(processoDt.getId_Serventia());

        if (serventiaJusticaRestaurativa != null) {
            // Se encontrou uma serventia relacionada, essa ser� a respons�vel pela pend�ncia
            responsavel.setId_Serventia(serventiaJusticaRestaurativa.getId());

            PendenciaStatusNe pendenciaStatusNe = new PendenciaStatusNe();
            PendenciaStatusDt pendenciaStatusDt = pendenciaStatusNe.consultarPendenciaStatusCodigo(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO);
            pendenciaDt = pendenciaDt.criarPendenciaFilhaExpedida();
            pendenciaDt.setId_PendenciaStatus(pendenciaStatusDt.getId());
            pendenciaDt.setDataVisto("");

        } else {
            // Se n�o h� nenhuma serventia relacionada, a pend�ncia ser� gerada para a serventia do processo expedir
            responsavel.setId_Serventia(processoDt.getId_Serventia());
        }

        pendenciaDt.addResponsavel(responsavel);
        this.gerarPendencia(pendenciaDt, arquivos, false, obFabricaConexao);

    }
    
    /**
     * Consulta pendencias abertas de peti��o por processo na serventia
     * 
     * @author M�rcio Gomes 
     * @since 01/09/2010
     * @param usuarioDt
     *            usuario da sessao
     * @param numero_processo
     *            filtro para o numero do processo
     * @param dataHoraInicio
     *            data e hora inicial 
     * @return List
     * @throws Exception
     */
    public List consultarPeticoesEmAndamento(UsuarioNe usuarioNe, String numero_processo, String dataHoraInicio, String Id_Serventia) throws Exception {
        List pendencias = null;
        FabricaConexao obFabricaConexao = null; 
        try {
        	
        	obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);       	
    		
    		//Configura o tipo de pendencia
    		PendenciaTipoDt pendenciaTipoDt = new PendenciaTipoDt();
    		pendenciaTipoDt.setPendenciaTipoCodigo(String.valueOf(PendenciaTipoDt.VERIFICAR_PETICAO));	
    		
    		PendenciaStatusDt pendenciaStatusDt = new PendenciaStatusDt();
    		pendenciaStatusDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_EM_ANDAMENTO));     	
                        

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            List tempList = pendenciaPs.consultarAbertas(usuarioNe, pendenciaTipoDt, pendenciaStatusDt, false, 1, 0, numero_processo, dataHoraInicio, "" , "0", Id_Serventia);

            setQuantidadePaginas(tempList);

            pendencias = (List) tempList;
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }
    
    /**
     * Consulta pendencias abertas de peti��o por processo na serventia
     * 
     * @author M�rcio Gomes 
     * @since 01/09/2010
     * @param usuarioDt
     *            usuario da sessao
     * @param numero_processo
     *            filtro para o numero do processo
     * @param dataHoraInicio
     *            data e hora inicial 
     * @return List
     * @throws Exception
     */
    public List consultarPeticoesEmAndamentoPJD(UsuarioNe usuarioNe, String numero_processo, String dataHoraInicio, String Id_Serventia, FabricaConexao obFabricaConexao) throws Exception {
        List pendencias = null;
    		//Configura o tipo de pendencia
    		PendenciaTipoDt pendenciaTipoDt = new PendenciaTipoDt();
    		pendenciaTipoDt.setPendenciaTipoCodigo(String.valueOf(PendenciaTipoDt.VERIFICAR_PETICAO));	
    		
    		PendenciaStatusDt pendenciaStatusDt = new PendenciaStatusDt();
    		pendenciaStatusDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_EM_ANDAMENTO));     	
                        

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            List tempList = pendenciaPs.consultarAbertas(usuarioNe, pendenciaTipoDt, pendenciaStatusDt, false, 1, 0, numero_processo, dataHoraInicio, "" , "0", Id_Serventia);

            setQuantidadePaginas(tempList);

            pendencias = (List) tempList;

        return pendencias;
    }
    
    /***
     *	Consulta os ids de pend�ncias que n�o foram analisadas, de um determinado tipo para o usuario.
	 * - Consulta somente as pend�ncias que n�o possuem nenhuma pr�-an�lise registrada
     * @param id_ServentiaCargo
     * @param id_Processo
     * @param obFabricaConexao
     * @return
     * @throws Exception
     * @author mmgomes
     */
    public List consultarIdPendenciasProcessoNaoAnalisadas(String id_ServentiaCargo, String id_Processo, FabricaConexao obFabricaConexao) throws Exception {
    	List Idpendencias = null;
       
    	PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());            
        Idpendencias = obPersistencia.consultarIdPendenciasProcessoNaoAnalisadas(id_ServentiaCargo, id_Processo); 

        return Idpendencias;
    }
    
    /***
     * Consulta os ids de pendencias que est�o pr�-analisadas por tipo. Trata os tipos de pend�ncias semelhantes �s conclus�es,
	 * que ter�o an�lise e pr�-an�lise
     * @param id_ServentiaCargo
     * @param id_Processo
     * @param multiplo
     * @param obFabricaConexao
     * @return
     * @throws Exception
     * @author mmgomes
     */
    public List consultarIdPendenciasProcessoPreAnalisadas(String id_ServentiaCargo, String id_Processo, boolean multiplo, FabricaConexao obFabricaConexao) throws Exception {
    	List Idpendencias = null;
        
    	PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());            
        Idpendencias = obPersistencia.consultarIdPendenciasProcessoPreAnalisadas(id_ServentiaCargo, id_Processo, multiplo); 

        return Idpendencias;
    }
    
    /***
     * Consulta os ids de pendencias que est�o pr�-analisadas por tipo e pendentes de assinatura. Trata os tipos de pend�ncias semelhantes �s conclus�es,
	 * que ter�o an�lise e pr�-an�lise
     * @param id_ServentiaCargo
     * @param id_Processo
     * @param multiplo
     * @param obFabricaConexao
     * @return
     * @throws Exception
     * @author mmgomes
     */
    public List consultarIdPendenciasProcessoPreAnalisadasPendentesAssinatura(String id_ServentiaCargo, String id_Processo, boolean multiplo, FabricaConexao obFabricaConexao) throws Exception {
    	List Idpendencias = null;
        
    	PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());            
        Idpendencias = obPersistencia.consultarIdPendenciasProcessoPreAnalisadasPendentesAssinatura(id_ServentiaCargo, id_Processo, multiplo); 

        return Idpendencias;
    }

	/**
	 * Marcar pend�ncia do tipo intima��o  ou cita��o como distribu�da.
	 * 
	 * @author lsbernardes
	 * @since 23/02/2011
	 * @param String id da pend�ncia
	 * @param LogDt objeto de log
	 * @param FabricaConexao fabrica de conex�o
	 * @throws Exception
	 */
    public void marcarIntimacaoCitacaoDistribuida(String idPendencia, LogDt logDt, FabricaConexao obFabricaConexao)  throws Exception {
    	LogDt obLogDt;
    	PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
		obPersistencia.marcarIntimacaoCitacaoDistribuida(idPendencia);
		obLogDt = new LogDt("Pendencia", idPendencia, logDt.getId_UsuarioLog(), logDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), logDt.getPropriedades());
		obLog.salvar(obLogDt, obFabricaConexao);
    }
    
    /**
	 * Consultar o complemento para ser registrado na movimenta��o do tipo leitura de intima��o.
	 * 
	 * @author mmgomes
	 * @since 03/05/2011
	 * @param String id da pend�ncia	
     * @throws Exception 
	 *
	 */
    private String obtenhaComplementoLeituraPendenciaIntimacao(PendenciaDt pendenciaDt) throws Exception{
    	String complemento = "";
    	if (pendenciaDt != null && pendenciaDt.getId_ProcessoParte() != null && !pendenciaDt.getId_ProcessoParte().trim().equalsIgnoreCase("")){
    		ProcessoParteDt processoParteDt = (new ProcessoParteNe()).consultarId(pendenciaDt.getId_ProcessoParte());
        	if (processoParteDt != null && processoParteDt.getProcessoParteTipoCodigo() != null && processoParteDt.getProcessoParteTipoCodigo().trim().length() > 0){
        		switch (Funcoes.StringToInt(processoParteDt.getProcessoParteTipoCodigo().trim())) {
        			case ProcessoParteTipoDt.POLO_ATIVO_CODIGO:        			
        				complemento = " (Polo Ativo)";
        				break;
        			case ProcessoParteTipoDt.POLO_PASSIVO_CODIGO:        			
        				complemento = " (Polo Passivo)";    				
        				break;
        		}    		
        	}
    	}    	
    	return complemento;
    }
    
    /**
     * Consultar as pend�ncias do processo de um determinado tipo
     *
     * @author mmgomes
     * @param id_Processo
     * @param pendenciaTipoCodigo
     * @return
     * @throws Exception
     */
    public List consultarPendenciasNaoAnalisadasProcesso(String id_Processo, String pendenciaTipoCodigo) throws Exception {
    	List pendencias = null;
        FabricaConexao obFabricaConexao = null; 
        try {
        	
        	obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);               

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            pendencias = pendenciaPs.consultarPendenciasNaoAnalisadasProcesso(id_Processo, pendenciaTipoCodigo);
            
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }

	public void alterarCodigoTempPendencia(String idPendencia, String codigoTemp, FabricaConexao fabrica) throws Exception {
		PendenciaDt pendenciaDt = new PendenciaDt();
		
		pendenciaDt.setId(idPendencia);
		pendenciaDt.setCodigoTemp(codigoTemp);
		
		AlterarCodigoTempPendencia(pendenciaDt, fabrica);
	}

    /**
     * Atualiza dados o atributo c�digo temp de uma pend�ncia
     * 
     * @param pendenciaDtVoto
     * 
     * @author mmgomes
     * @throws Exception 
     * 
     */
	public void AlterarCodigoTempPendencia(PendenciaDt pendenciaDt) throws Exception {
		FabricaConexao obFabricaConexao = null; 
        try {
        	
        	obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);               

        	AlterarCodigoTempPendencia(pendenciaDt, obFabricaConexao);
            
        } finally {
            obFabricaConexao.fecharConexao();
        }		
	}
	
	/**
	 * Limpar dados o atributo c�digo temp de uma pend�ncia
	 * 
	 * @param pendenciaDt
	 * @param obFabricaConexao
	 * @author jvosantos
	 * @throws Exception
	 */
	public void limparCodigoTempPendencia(String idPend, FabricaConexao obFabricaConexao) throws Exception {	                
        PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
        pendenciaPs.limparCodigoTempPendencia(idPend);
	}
	
	/**
	 * Atualiza dados o atributo c�digo temp de uma pend�ncia
	 * 
	 * @param pendenciaDt
	 * @param obFabricaConexao
	 * @author mmgomes
	 * @throws Exception
	 */
	public void AlterarCodigoTempPendencia(PendenciaDt pendenciaDt, FabricaConexao obFabricaConexao) throws Exception {	                
        PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
        pendenciaPs.AlterarCodigoTempPendencia(pendenciaDt);   
	}
	
	/**
     * Consulta processocompleto por id processo
     * 
     * @author Leandro Bernardes
     * @param String idProcesso, id do processo
     * @return PendenciaTipoDt
     * @throws Exception
     */
    public ProcessoDt consultarProcessoCompletoId(String idProcesso, UsuarioDt usuarioDt, boolean acessoOutraServentiaOuCodigoDeAcesso, int nivelAcesso) throws Exception {
    	ProcessoDt processoDt = new ProcessoDt();
    	ProcessoNe processoNe = new ProcessoNe();
        processoDt = processoNe.consultarDadosProcesso(idProcesso, usuarioDt, false, acessoOutraServentiaOuCodigoDeAcesso, nivelAcesso);
        processoNe = null;
        return processoDt;
    }
    
    /**
     * M�todo que verifica se h� pend�ncias do tipo conclus�o em aberto para o
     * processo passado. Ser� utilizado em verifica��es.
     * 
     * @param id_Processo,
     *            identifica��o de processo
     * @param fabricaConexao,
     *            conex�o ativa
     * 
     * @author mmgomes
     */
    public boolean verificaConclusoesAbertasProcessoServentiaCargo(String id_Processo, String id_ServentiaCargo, FabricaConexao fabricaConexao) throws Exception {
        boolean boRetorno = false;
        FabricaConexao obFabricaConexao = null;
        try {
            if (fabricaConexao == null)
                obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            else
                obFabricaConexao = fabricaConexao;
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

            String[] conclusaoPendente = obPersistencia.consultarConclusoesAbertasProcessoServentiaCargo(id_Processo, id_ServentiaCargo);

            if (conclusaoPendente != null) boRetorno = true;
        } finally {
            if (fabricaConexao == null) obFabricaConexao.fecharConexao();
        }
        return boRetorno;
    }
    
    /**
     * Listagem de intima��es lidas paara distribui��o de uma serventia
     * 
     * @since 30/08/2011
     * @author mmgomes
     * @param usuarioDt
     *            vo de usuario
     * @param pendenciaTipoDt
     *            vo de pendencia
     * @param numero_processo
     *            numero do processo
     * @param dataInicialInicio
     *            data inicial de inicio
     * @param dataFinalInicio
     *            data final de inicio
     * @param dataInicioFim
     *            data inicial de fim
     * @param dataFinalFim
     *            data final de fim
     * @param posicao
     *            posicao para paginacao
     * @return lista de pendencias onde estao lidas e o cargo do usuario e
     *         um dos responsaveis
     * @throws Exception
     */
    public List consultarIntimacoesLidasDistribuicao(UsuarioNe usuarioNe, String numero_processo, String dataInicialInicio, String dataFinalInicio, String posicao) throws Exception {
        List pendencias = new ArrayList();

        if (usuarioNe.getUsuarioDt().isCoordenadorPromotoria()) {
            pendencias = this.consultarIntimacoesLidasDistribuicaoPromotoria(usuarioNe, numero_processo, dataInicialInicio, dataFinalInicio, posicao);
        }else if (usuarioNe.getUsuarioDt().isCoordenadorJuridico())
            pendencias = this.consultarIntimacoesLidasDistribuicaoProcuradoria(usuarioNe, numero_processo, dataInicialInicio, dataFinalInicio, posicao);
        
        return pendencias;
    }
    
    /**
     * Listagem de pendencias intima��es distribu�das
     * 
     * @since 30/07/2012
     * @author mmgomes
     * @param usuarioDt
     *            vo de usuario
     * @param pendenciaTipoDt
     *            vo de pendencia
     * @param numero_processo
     *            numero do processo
     * @param dataInicialInicio
     *            data inicial de inicio
     * @param dataFinalInicio
     *            data final de inicio
     * @param dataInicioFim
     *            data inicial de fim
     * @param dataFinalFim
     *            data final de fim
     * @param posicao
     *            posicao para paginacao
     * @return lista de pendencias onde estao lidas e o cargo do usuario e
     *         um dos responsaveis
     * @throws Exception
     */
    public List consultarIntimacoesLidasDistribuicaoPromotoria(UsuarioNe usuarioNe, String numero_processo, String dataInicialInicio, String dataFinalInicio, String posicao) throws Exception {
        List tempList = null;
        
        FabricaConexao obFabricaConexao = null;
        
        try {
            // Deixa a data inicial no inicio do dia
            if (dataInicialInicio != null) dataInicialInicio += " 00:00:00";
            // Deixa a data final ate o fim do dia
            if (dataFinalInicio != null) dataFinalInicio += " 23:59:59";

            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

            tempList = obPersistencia.consultarIntimacoesLidasDistribuicaoPromotoria(usuarioNe, "", usuarioNe.getUsuarioDt().getId_ServentiaCargo(), numero_processo, dataInicialInicio, dataFinalInicio, posicao);

            setQuantidadePaginas(tempList);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return tempList;
    }
    
    /**
     * Listagem de pendencias intima��es distribu�das
     * 
     * @since 30/08/2012
     * @author mmgomes
     * @param usuarioDt
     *            vo de usuario
     * @param pendenciaTipoDt
     *            vo de pendencia
     * @param numero_processo
     *            numero do processo
     * @param dataInicialInicio
     *            data inicial de inicio
     * @param dataFinalInicio
     *            data final de inicio
     * @param dataInicioFim
     *            data inicial de fim
     * @param dataFinalFim
     *            data final de fim
     * @param posicao
     *            posicao para paginacao
     * @return lista de pendencias onde estao lidas e o cargo do usuario e
     *         um dos responsaveis
     * @throws Exception
     */
    public List consultarIntimacoesLidasDistribuicaoProcuradoria(UsuarioNe usuarioNe, String numero_processo, String dataInicialInicio, String dataFinalInicio, String posicao) throws Exception {
        List tempList = null;
        
        FabricaConexao obFabricaConexao = null;
        
        try {
            // Deixa a data inicial no inicio do dia
            if (dataInicialInicio != null) dataInicialInicio += " 00:00:00";
            // Deixa a data final ate o fim do dia
            if (dataFinalInicio != null) dataFinalInicio += " 23:59:59";

            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

            tempList = obPersistencia.consultarIntimacoesLidasDistribuicaoProcuradoria(usuarioNe, "", usuarioNe.getUsuarioDt().getId_ServentiaCargo(), numero_processo, dataInicialInicio, dataFinalInicio, posicao);

            setQuantidadePaginas(tempList);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return tempList;
    }
    
    /**
     * Consulta pendencias do tipo Intima��o de todos os promotores da promotoria, que foram lidas e est�o dentro do prazo de cumprimento (data limite)
     * do usu�rio logado 
     * 
     * @author mmgomes
     * @param usuarioDt
     *            usuario da sessao
     * @return lista de intima��es de todos os procuradores da procuradoria
     * @throws Exception
     */
    public List consultarIntimacoesLidasDistribuicaoPromotoria(UsuarioNe usuarioNe ) throws Exception {
        List pendencias = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            pendencias = pendenciaPs.consultarIntimacoesLidasPromotoria(usuarioNe);
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }
    
    /**
     * Consulta pendencias do tipo Intima��o de todos os procuradores da procuradoria
     * do usu�rio logado para webservice
     * 
     * @author mmgomes
     * @since 30/08/2012
     * @param usuarioDt
     *            usuario da sessao
     * @return lista de intima��es lidas de todos os procuradores da procuradoria
     * @throws Exception
     */
    public List consultarIntimacoesLidasDistribuicaoProcuradoria(UsuarioNe usuarioNe ) throws Exception {
        List pendencias = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
            pendencias = pendenciaPs.consultarIntimacoesLidasProcuradoria(usuarioNe);
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }
    
    /**
     * M�todo que consulta as pend�ncias do tipo Carta de Cita��o que foram lidas pelos advogados
     * do Escrit�rio Jur�dico do usu�rio logado 
     * 
     * @param usuarioNe usu�rio da sess�o
     * @return lista de pend�ncias do tipo carta de cita��o
     * @throws Exception
     * @author hmgodinho
     * @author jrcorrea 15/05/2015
     */
    public List consultarCitacoesLidasDistribuicaoEscritorioJuridicoProcuradoria(UsuarioNe usuarioNe ) throws Exception {
        List pendencias = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            //pendenciaPs.setConexao(obFabricaConexao);
             
            pendencias = pendenciaPs.consultarCitacoesLidasEscritorioJuridicoProcuradorias(usuarioNe);
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }
    
    public String consultarTextoPublicacaoPublicaDuplaPalvrasJSON(String dataInicio, String dataFinal, String id_Serventia, String texto, String posicao, UsuarioNe usuarioSessao) throws Exception {

        FabricaConexao obFabricaConexao = null;
        
        if (dataInicio != null && dataInicio.trim().equals("")) dataInicio = null;
        if (dataFinal != null && dataFinal.trim().equals("")) dataFinal = null;

        // limpo o texto preparando-o para a busca de palavras
        String stTextoLimpo[] = new ArquivoPalavraNe().limparTexto(texto).split(" ");

        String stTemp = "";

        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            // Deixa a data inicial no inicio do dia
            if (dataInicio != null) dataInicio += " 00:00:00";
            // Deixa a data final ate o fim do dia
            if (dataFinal != null) dataFinal += " 23:59:59";

            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            stTemp = obPersistencia.consultarTextoPublicacaoDuplaPalvrasJSON(dataInicio, dataFinal, id_Serventia, stTextoLimpo, posicao, usuarioSessao);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return stTemp;
    }
    
    public String consultarTextoPublicacaoQualquerPalavraJSON(String dataInicio, String dataFinal, String id_Serventia, String texto, String posicao, UsuarioNe UsuarioSessao) throws Exception {

        FabricaConexao obFabricaConexao = null;
        
        if (dataInicio != null && dataInicio.trim().equals("")) dataInicio = null;
        if (dataFinal != null && dataFinal.trim().equals("")) dataFinal = null;

        // limpo o texto preparando-o para a busca de palavras
        String stTextoLimpo[] = new ArquivoPalavraNe().limparTexto(texto).split(" ");

        String stTemp = "";

        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            // Deixa a data inicial no inicio do dia
            if (dataInicio != null) dataInicio += " 00:00:00";
            // Deixa a data final ate o fim do dia
            if (dataFinal != null) dataFinal += " 23:59:59";

            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            stTemp = obPersistencia.consultarTextoPublicacaoQualquerPalavraJSON(dataInicio, dataFinal, id_Serventia, stTextoLimpo, posicao, UsuarioSessao);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return stTemp;
    }
    
    /**
     * Consulta as publica��es (pend�ncias tipo PUBLICACAO PUBLICA) de acordo com o per�odo informado.
     * @param dataInicio
     * @param posicao
     * @param UsuarioSessao
     * @return
     * @throws Exception
     */
    public String consultarPublicacaoPublicaPorData(String dataInicio, String posicao, UsuarioNe UsuarioSessao) throws Exception {
    	
    	FabricaConexao obFabricaConexao = null;
    	        
        if (dataInicio != null && dataInicio.trim().equals("")) dataInicio = null;
    	
        String stTemp = "";

        try {
            
        	obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
        	
        	// Gera uma exce��o se os campos de datas estiverem vazios.
        	if (dataInicio == null){
        		throw new Exception ("� necess�rio informar a data de publica��o.");
        	}
            
        	// Deixa a data final at� o fim do dia
            String dataFinal = dataInicio + " 23:59:59";
        	
            // Deixa a data inicial no inicio do dia
            if (dataInicio != null) dataInicio += " 00:00:00";
                          
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            stTemp = obPersistencia.consultarPublicacaoPublicaPorPeriodo(dataInicio, dataFinal, posicao, UsuarioSessao);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return stTemp;
        
    }
    
    /**
	 * Consulta as pendencias do tipo conclus�o que est�o em aberto, e que o respons�vel seja o serventiaCargo passado
	 * 
	 * @param id_ServentiaCargo, serventiaCargo respons�vel pelas pend�ncias
	 * @param id_Processo, id do processo	 
	 * 
	 * @author mmgomes
	 */
	public boolean isResponsavelConclusoesPendentes(String id_ServentiaCargo, String id_Processo) throws Exception {
		FabricaConexao obFabricaConexao = null;
		try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);          
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            return obPersistencia.ehResponsavelConclusoesPendentes(id_ServentiaCargo, id_Processo);
        } finally {
            obFabricaConexao.fecharConexao();
        }
	}
	
	/**
	 * Altera o classificador de uma pendencia
	 * 
	 * @param id_Pendencia
	 *            , identifica��o da pendencia
	 * @param id_ClassificadorAnterior
	 *            , classificador antigo
	 * @param id_ClassificadorNovo
	 *            , novo classificador
	 * @param logDt
	 *            , dados do log
	 * @param conexao
	 *            , conex�o ativa
	 * 
	 * @author lsbernardes
	 */
	public void alterarClassificadorPendencia(String id_Pendencia, String id_ClassificadorAnterior, String id_ClassificadorNovo, LogDt logDt, FabricaConexao conexao) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		try {
			if (conexao == null) obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else
				obFabricaConexao = conexao;
			PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

			String valorAtual = "[Id_Pendencia:" + id_Pendencia + ";Id_Classificador:" + id_ClassificadorAnterior + "]";
			String valorNovo = "[Id_Pendencia:" + id_Pendencia + ";Id_Classificador:" + id_ClassificadorNovo + "]";

			obLogDt = new LogDt("Pendencia", id_Pendencia, logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.Alterar), valorAtual, valorNovo);
			obPersistencia.alterarClassificadorPendencia(id_Pendencia, id_ClassificadorNovo);

			obLog.salvar(obLogDt, obFabricaConexao);
		} finally {
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
	}
	
	public String consultarDescricaoServentiaPublicacaoJSON(String descricao, String posicao) throws Exception {
		String stTemp = "";
		ServentiaNe serventiaNe = new ServentiaNe();
		stTemp= serventiaNe.consultarDescricaoServentiaPublicacaoJSON(descricao, posicao);
		serventiaNe = null;
		return stTemp;
	}
	
	/**
     * Verifica se o usuario corrente pode guardar para assinar
     * 
     * @author M�rcio Mendon�a Gomes
     * @since 16/10/2013
     * @param UsuarioNe
     *            usuarioNe, objeto de negocio do usuario
     * @return boolean
     * @throws Exception
     */
    public boolean podeGuardarParaAssinar(PendenciaDt pendenciaDt, UsuarioNe usuarioNe) throws Exception {
        return ((Funcoes.StringToInt(usuarioNe.getUsuarioDt().getGrupoTipoCodigo()) == GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU
				|| Funcoes.StringToInt(usuarioNe.getUsuarioDt().getGrupoTipoCodigo()) == GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU
				|| Funcoes.StringToInt(usuarioNe.getUsuarioDt().getGrupoTipoCodigo()) == GrupoTipoDt.JUIZ_TURMA
				|| Funcoes.StringToInt(usuarioNe.getUsuarioDt().getGrupoTipoCodigo()) == GrupoTipoDt.PRESIDENTE_SEGUNDO_GRAU
				|| usuarioNe.getUsuarioDt().isPodeGuardarParaAssinar())
				&& (pendenciaDt.getPendenciaTipoCodigo().equalsIgnoreCase(String.valueOf(PendenciaTipoDt.CARTA_PRECATORIA))
					|| pendenciaDt.getPendenciaTipoCodigo().equalsIgnoreCase(String.valueOf(PendenciaTipoDt.ALVARA))
					|| pendenciaDt.getPendenciaTipoCodigo().equalsIgnoreCase(String.valueOf(PendenciaTipoDt.ALVARA_SOLTURA))
					|| pendenciaDt.getPendenciaTipoCodigo().equalsIgnoreCase(String.valueOf(PendenciaTipoDt.CARTA_CITACAO))
					|| pendenciaDt.getPendenciaTipoCodigo().equalsIgnoreCase(String.valueOf(PendenciaTipoDt.CARTA_NOTIFICACAO))
					|| pendenciaDt.getPendenciaTipoCodigo().equalsIgnoreCase(String.valueOf(PendenciaTipoDt.CUMPRIMENTO_GENERICO))
					|| pendenciaDt.getPendenciaTipoCodigo().equalsIgnoreCase(String.valueOf(PendenciaTipoDt.MANDADO))
					|| pendenciaDt.getPendenciaTipoCodigo().equalsIgnoreCase(String.valueOf(PendenciaTipoDt.OFICIO))
					|| pendenciaDt.getPendenciaTipoCodigo().equalsIgnoreCase(String.valueOf(PendenciaTipoDt.OFICIO_COMUNICATORIO))
					|| pendenciaDt.getPendenciaTipoCodigo().equalsIgnoreCase(String.valueOf(PendenciaTipoDt.OFICIO_PRESIDENCIA))
					|| pendenciaDt.getPendenciaTipoCodigo().equalsIgnoreCase(String.valueOf(PendenciaTipoDt.OFICIO_SEGUNDO_GRAU))
					|| pendenciaDt.getPendenciaTipoCodigo().equalsIgnoreCase(String.valueOf(PendenciaTipoDt.OFICIO_TURMA))
					|| pendenciaDt.getPendenciaTipoCodigo().equalsIgnoreCase(String.valueOf(PendenciaTipoDt.OFICIO_DELEGACIA))
					|| pendenciaDt.getPendenciaTipoCodigo().equalsIgnoreCase(String.valueOf(PendenciaTipoDt.RENAJUD))									
					|| pendenciaDt.getPendenciaTipoCodigo().equalsIgnoreCase(String.valueOf(PendenciaTipoDt.TERMO))
					|| pendenciaDt.getPendenciaTipoCodigo().equalsIgnoreCase(String.valueOf(PendenciaTipoDt.EDITAL))));   
    }
	
	/**
     * Guardar para assinar.
     * 
     * @author M�rcio Mendon�a gomes
     * @since 16/10/2013
     * @param pendenciaDt
     *            pendencia em que ser� inserido os arquivos
     * @param arquivos
     *            arquivos a serem inseridos
     * @param usuarioDt
     *            usuario que realiza a operacao
     * @return true, se inseriu com sucesso e false se nao conseguiu inserir
     * @throws Exception
     */
    public void salvaGuardarParaAssinar(PendenciaDt pendenciaDt, List arquivos, UsuarioDt usuarioDt, ComarcaDt comarcaDt, ServentiaDt serventiaExpedirDt, ServentiaTipoDt serventiaTipoExpedirDt) throws Exception {
        FabricaConexao obFabricaConexao = null;  
        PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
        PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
        List listaReponsaveis;
        try {
			
        	obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		    obFabricaConexao.iniciarTransacao();
		
		    PendenciaDt pendenciaRetornoDt = pendenciaDt.criarFilha();
		    
		    pendenciaRetornoDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_PRE_ANALISADA));
		    
		    listaReponsaveis = pendenciaResponsavelNe.consultarResponsaveis(pendenciaDt.getId());
		    
		    if (pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.CARTA_PRECATORIA){
            	ArquivoDt arquivoDt = (ArquivoDt) arquivos.get(0);
            	pendenciaArquivoNe.montaArquivoPreAnalisePrecatoria(comarcaDt, arquivoDt);            	
            } else {            	
            	ArquivoDt arquivoDt = (ArquivoDt) arquivos.get(0);
            	pendenciaArquivoNe.montaArquivoPreAnalisePendencias(serventiaExpedirDt, arquivoDt, serventiaTipoExpedirDt);            	
            }
		    
		    this.gerarPendenciaFilha(pendenciaRetornoDt, listaReponsaveis, null, arquivos, false, true, true, obFabricaConexao);
		    
		    this.fecharPendencia(pendenciaDt, usuarioDt, obFabricaConexao);
		    
		    obFabricaConexao.finalizarTransacao();            

        } catch (Exception e) {
             obFabricaConexao.cancelarTransacao();
            throw e;
        } finally {
           obFabricaConexao.fecharConexao();
        }
    }
	
	 /**
     * Libera pendencia reservada.
     * 
     * @author Jesus Rodrigo
     * @since 22/11/2013
     * @param void
     * Limpa as pendencia reservadas           
     * 
     * @throws Exception
     */
    public void liberarPendenciasReservadas() throws Exception {
                        
		FabricaConexao obFabricaConexao = null;
		try {
			 obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

			PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

			obPersistencia.liberarPendenciasReservadas();
						
		} finally{
            obFabricaConexao.fecharConexao();
        }
    }
    
    /**
	 * Consulta todas as pend�ncias sem data fim para um determinado id serventia cargo.
	 * 	
	 * @param id_ServentiaCargo
	 * @return
	 * @throws Exception
	 */
	public List consultarTodasPendenciasSemDataFim(String id_ServentiaCargo) throws Exception {
		FabricaConexao obFabricaConexao = null;
		try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);          
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            return obPersistencia.consultarTodasPendenciasSemDataFim(id_ServentiaCargo);
        } finally {
            obFabricaConexao.fecharConexao();
        }		
	}

	public List consultarTodasPendenciasSemDataFimUsuarioServentia(String id_UsuarioServentia) throws Exception {
		FabricaConexao obFabricaConexao = null;
		try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);          
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            return obPersistencia.consultarTodasPendenciasSemDataFimUsuarioServentia(id_UsuarioServentia);
        } finally {
            obFabricaConexao.fecharConexao();
        }		
	}

	public String consultarDescricaoServentiaTipoJSON(String tempNomeBusca)  throws Exception {
		String stTemp = "";
		ServentiaTipoNe neObjeto = new ServentiaTipoNe();
		stTemp = neObjeto.consultarDescricaoJSON(tempNomeBusca);
		neObjeto = null;
		return stTemp;
	}
	
	//Consulta Retorna o json com a quantidade de registros no total, o que n�o acontece no projudi
	public String consultarDescricaoServentiaTipoJSONPJD(String tempNomeBusca)  throws Exception {
		String stTemp = "";
		ServentiaTipoNe neObjeto = new ServentiaTipoNe();
		stTemp = neObjeto.consultarDescricaoJSONPJD(tempNomeBusca);
		neObjeto = null;
		return stTemp;
	}

	/**
	 * M�todo que verifica se h� pend�ncias do tipo voto / ementa em aberto para o processo passado. Ser� utilizado em verifica��es.
	 * 
	 * @param id_Processo, identifica��o de processo
	 * 
	 * @author mmgomes - Consultar voto/ementa de um determinado processo.
	 */
    public List consultarVotoEmentaAbertasHash(String id_Processo, UsuarioNe usuarioSessao) throws Exception {
    	List votoEmentaPendentes = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

            votoEmentaPendentes = obPersistencia.consultarVotoEmentaAbertasHash(id_Processo, usuarioSessao );
        } finally {
            obFabricaConexao.fecharConexao();
        }
        return votoEmentaPendentes;
    }
    /**
	 * M�todo que verifica se h� pend�ncias do tipo Elaboracao de Voto em aberto para o processo passado. Ser� utilizado em verifica��es.
	 * 
	 * @param id_Processo, identifica��o de processo
	 * 
	 * @author lsbernardes
	 */
    public boolean verificarExistenciaElaboracaoVoto(String id_Processo) throws Exception {
    	boolean retorno = false;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            retorno = obPersistencia.verificarExistenciaElaboracaoVoto(id_Processo);
        } finally {
            obFabricaConexao.fecharConexao();
        }
        return retorno;
    }

	public String consultarDescricaoPendenciaTipoJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		PendenciaTipoNe neObjeto = new PendenciaTipoNe();
		stTemp = neObjeto.consultarDescricaoJSON(tempNomeBusca,posicaoPaginaAtual);
		neObjeto = null;
		return stTemp;
	}

	public String consultarDescricaoPendenciaStatusJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		PendenciaStatusNe neObjeto = new PendenciaStatusNe();
		stTemp = neObjeto.consultarDescricaoJSON(tempNomeBusca,posicaoPaginaAtual);
		neObjeto = null;
		return stTemp;
	}

	public String consultarGrupoArquivoTipoJSON(String grupoCodigo, String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		ArquivoTipoNe neObjeto = new ArquivoTipoNe();
		stTemp = neObjeto.consultarGrupoArquivoTipoJSON(grupoCodigo, tempNomeBusca, posicaoPaginaAtual);
		neObjeto = null;
		return stTemp;
	}
	
	/**
	 * Consulta os dados de uma audi�ncia processo que ainda n�o foi movimentado
	 * 
	 * @param id_Processo, identifica��o do processo
	 * @param usuarioDt, usu�rio logado
	 * 
	 * @author lsbernardes
	 */
	public AudienciaProcessoDt consultarAudienciaProcessoPendente(String id_Processo, UsuarioDt usuarioDt) throws Exception {
		AudienciaProcessoDt audienciaprocessoDt = null;
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		
		 audienciaprocessoDt = audienciaProcessoNe.consultarAudienciaProcessoPendente(id_Processo, usuarioDt);			

		audienciaProcessoNe = null;
		return audienciaprocessoDt;
	}	
	
	/**
	 * Verifica se o processo est� em uma serventia que possua um dos subTipos que n�o se aplica a contagem de prazo do novo CPC
	 * 
	 * @param id_Processo, identifica��o do processo
	 * @param conexao,
	 *            conex�o com o banco de dados
	 * 
	 * @author lsbernardes
	 */
	public boolean isUtilizaPrazoCorrido(String id_processo, FabricaConexao obFabricaConexao) throws Exception {
		boolean retorno = false;
		ProcessoNe processoNe = new ProcessoNe();
		retorno = processoNe.isUtilizaPrazoCorrido(id_processo, obFabricaConexao);				
		return retorno;		
	}
	
	/**
	 * Verifica se o processo est� em uma serventia que possua subTipo vara de precatorias
	 * 
	 * @param id_Processo, identifica��o do processo
	 * @param conexao,
	 *            conex�o com o banco de dados
	 * 
	 * @author lsbernardes
	 */
	public boolean isProcessoVaraPrecatoria(String id_processo, FabricaConexao obFabricaConexao) throws Exception {
		boolean retorno = false;
		ProcessoNe processoNe = new ProcessoNe();
		retorno = processoNe.isProcessoVaraPrecatoria(id_processo, obFabricaConexao);				
		return retorno;		
	}
	
	/**
	 * Consulta as pend�ncias [tipo INTIMA��O, status CUMPRIDA] do dia, para serem publicadas no di�ria oficial
	 * @param dataInicial
	 * @param dataFinal
	 * @param opcao
	 * @return
	 * @throws Exception
	 */
	public List<MovimentacaoIntimacaoDt> consultarIntimacoesParaPublicacao(String dataInicial, String dataFinal, int opcaoPublicacao) throws Exception {
		FabricaConexao obFabricaConexao = null;
		try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);          
            PendenciaPs obPersistencia = new PendenciaPs(obFabricaConexao.getConexao());
            return obPersistencia.consultarIntimacoesParaPublicacao(dataInicial, dataFinal, opcaoPublicacao);
        } finally {
            obFabricaConexao.fecharConexao();
        }
	}
	
	public List<MovimentacaoIntimacaoDt> consultarIntimacoesParaPublicacaoV2(String dataInicial, String dataFinal, int opcaoPublicacao) throws Exception {
		FabricaConexao obFabricaConexao = null;
		try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);          
            PendenciaPs obPersistencia = new PendenciaPs(obFabricaConexao.getConexao());
            return obPersistencia.consultarIntimacoesParaPublicacaoV2(dataInicial, dataFinal, opcaoPublicacao);
        } finally {
            obFabricaConexao.fecharConexao();
        }
	}
	
	public List<MovimentacaoIntimacaoDt> consultarArquivosParaPublicacaoDJE(String dataInicial, String dataFinal, int opcaoPublicacao) throws Exception {
		FabricaConexao obFabricaConexao = null;
		try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);          
            PendenciaPs obPersistencia = new PendenciaPs(obFabricaConexao.getConexao());
            return obPersistencia.consultarArquivosParaPublicacaoDJE(dataInicial, dataFinal, opcaoPublicacao);
        } finally {
            obFabricaConexao.fecharConexao();
        }
	}
	
	
	public boolean isAcessoLiberado(String idResponsavel, String idProcesso) throws Exception {
		
		FabricaConexao obFabricaConexao = null;
		try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);          
            PendenciaPs obPersistencia = new PendenciaPs(obFabricaConexao.getConexao());
            return obPersistencia.isAcessoLiberado(idResponsavel, idProcesso);
        } finally {
            obFabricaConexao.fecharConexao();
        }
		
	}
	
	public boolean temPendenciaAberta(String idServ, String idProcesso, String idServCargo) throws Exception {
			
			FabricaConexao obFabricaConexao = null;
			try {
	            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);          
	            PendenciaPs obPersistencia = new PendenciaPs(obFabricaConexao.getConexao());
	            return obPersistencia.temPendenciaAberta(idServ, idProcesso, idServCargo);
	        } finally {
	            obFabricaConexao.fecharConexao();
	        }
			
	}

//	public boolean isPendenciaPedidoContadoria(String idServ, String idProcesso, String idServCargo) throws Exception {
//		
//		FabricaConexao obFabricaConexao = null;
//		try {
//	        obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);          
//	        PendenciaPs obPersistencia = new PendenciaPs(obFabricaConexao.getConexao());
//	        return obPersistencia.isPendenciaPedidoContadoria(idServ, idProcesso, idServCargo);
//	    } finally {
//	        obFabricaConexao.fecharConexao();
//	    }
//		
//	}
	
//	public boolean isPendenciaPedidoCamaraSaude(String idServ, String idProcesso, String idServCargo) throws Exception {
//		
//		FabricaConexao obFabricaConexao = null;
//		try {
//            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);          
//            PendenciaPs obPersistencia = new PendenciaPs(obFabricaConexao.getConexao());
//            return obPersistencia.isPendenciaPedidoCamaraSaude(idServ, idProcesso, idServCargo);
//        } finally {
//            obFabricaConexao.fecharConexao();
//        }		
//	}
	
	public void reAbrirPendencia(PendenciaDt pendenciaDt, UsuarioDt usuarioDt, FabricaConexao obFabricaConexao) throws Exception {
    	PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
        obPersistencia.reAbrirPendencia(pendenciaDt);        
    }
	
	/**
	 * Consulta as pendencias do tipo conclus�o que est�o em aberto, e que o respons�vel seja a Serventia passada, qualquer distribuidor pode ver
	 * 
	 * @param id_Serventia, serventia respons�vel pelas pend�ncias
	 * @param id_Processo, 	id do processo	 
	 * 
	 * @author mmgomes
	 */
	public boolean isServentiaResponsavelConclusoesPendentes(String id_Serventia, String id_Processo) throws Exception {
		FabricaConexao obFabricaConexao = null;
		try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);          
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            return obPersistencia.isServentiaResponsavelConclusoesPendentes(id_Serventia, id_Processo);
        } finally {
            obFabricaConexao.fecharConexao();
        }
	}
	
	  /**
     * Cria uma pendencia para verificar o recebimento de um novo processo com assistencia
     * 
     * @author lsbernardes
     * @param processoDt, bean do processo
     * @param id_UsuarioServentia, id do usuario da serventia
     * @param id_ServentiaResponsavel, id da serventia responsavel
     * @param obFabricaConexao, fabrica de conexao para continar uma trasacao existente
     * @return void
     * @throws Exception
     */
    public void gerarPendenciaVerificarNovoProcessoPedidoAssistencia(ProcessoDt processoDt, String id_UsuarioServentia, String id_ServentiaResponsavel, String id_Movimentacao, List arquivosVincular, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
    	
    	ArquivoDt arquivoDt = new ArquivoNe().salvarArquivoVerificarCpfParte(processoDt, obFabricaConexao);
    	if (arquivoDt != null) arquivosVincular.add(arquivoDt);
    	
        this.gerarPendenciaProcesso(processoDt.getId(), arquivosVincular, PendenciaTipoDt.VERIFICAR_NOVO_PROCESSO_PEDIDO_ASSISTENCIA, id_Movimentacao, id_UsuarioServentia, "", "", "", PendenciaStatusDt.ID_EM_ANDAMENTO, "", id_ServentiaResponsavel, "", "", processoDt.getId_ProcessoPrioridade(), null, logDt, obFabricaConexao);

    }
    
    public void alterarPrioridadePendencia(String id_Pendencia, String PrioridadeCodigo, UsuarioNe UsuarioSessao, FabricaConexao conexao) throws Exception
    {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		try
		{
			if (conexao == null)
			{
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			}
			else
			{
				obFabricaConexao = conexao;
			}
			PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
			String valorAtual = "[Id_Pendencia:" + id_Pendencia + ";PrioridadeCodigo:" + PrioridadeCodigo + "]";
			String valorNovo = "[Id_Pendencia:" + id_Pendencia + ";PrioridadeCodigo:" + "Sem Prioridade" + "]";
			obLogDt = new LogDt("Pendencia", id_Pendencia, UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), valorAtual, valorNovo);
			
			obPersistencia.alterarPrioridadePendencia(id_Pendencia, PrioridadeCodigo);
			obLog.salvar(obLogDt, obFabricaConexao);
		} finally
		{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
	}
    
	/**
	 * Verifica se determinado usu�rio � respons�vel em um processo, validando o serventiaCargo do usu�rio
	 * @param id_ServentiaCargo, serventiaCargo do usu�rio
	 * @param id_Processo, identifica��o do processo
	 * @author lsrodrigues
	 */
	public boolean isResponsavelProcesso(String id_ServentiaCargo, String id_Processo) throws Exception {
		return new ProcessoResponsavelNe().isResponsavelProcesso(id_ServentiaCargo, id_Processo);		
	}
	
	/**
     * Cria uma pendencia para verificar o processo quando todos os temas (precedentes) estiverem
     * com a situa��o transitado julgado no CNJ.
     * 
     * @author mmmitsunaga     
     * @param processoDt, bean do processo
     * @param id_UsuarioServentia, id do usuario cadastrador
     * @param id_ServentiaResponsavel, id da serventia responsavel
     * @param id_Movimentacao, movimenta��o vinculada
     * @param logDt, objeto com dados do log
     * @param obFabricaConexao, conex�o ativa
     */
    public void gerarPendenciaVerificarTemaTransitoJulgado (ProcessoDt processoDt, List<ProcessoTemaDt> listaProcessoTemaDt, String id_ServentiaResponsavel) throws Exception {    	
    	LogDt logDt = new LogDt(processoDt.getId_UsuarioLog(), processoDt.getIpComputadorLog());
    	List arquivos = new ArrayList();
    	FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ArquivoDt arquivoDt = new ArquivoNe().salvarArquivoVerificarVerificarTemaTransitoJulgado(processoDt, listaProcessoTemaDt, obFabricaConexao);
			arquivos.add(arquivoDt);
			this.gerarPendenciaProcesso(processoDt.getId(), arquivos, PendenciaTipoDt.VERIFICAR_TEMA_TRANSITADO_JULGADO, null, processoDt.getId_UsuarioServentia(), "", "", ""
					, PendenciaStatusDt.ID_EM_ANDAMENTO, "", id_ServentiaResponsavel, "", "", processoDt.getId_ProcessoPrioridade(), null, logDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		} catch (Exception ex){
			obFabricaConexao.cancelarTransacao();
			throw ex;
		} finally {
			obFabricaConexao.fecharConexao();
		}
    }
    
    /**
     * Gera uma pend�ncia do tipo Solicita��o de Carga para a serventia do processo
     * 
     * @author leandro bernardes
     * @param UsuarioDt
     *            usuario logado
     * @param ProcessoDt
     *            processo para o qual a pend�ncia ser� gerada
     * @return boolean
     * @throws Exception
     */
    public boolean gerarPendeciaSolicitarCargaProcesso(UsuarioDt usuarioDt, ProcessoDt processoDt) throws Exception {
        FabricaConexao obFabricaConexao = null;
        obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
        //est� iniciando a transa��o antes de entrar no if, para evitar erro quando a mensagem de erro de cargo
        //do else � gerada. Acontecia erro ao tentar cancelar a transa��o no final do m�todo, sendo que a transa��o
        //n�o havia sido iniciada.
        obFabricaConexao.iniciarTransacao();
        try {
                
	        PendenciaDt pendenciaDt = new PendenciaDt();
	        
	        pendenciaDt.setId_UsuarioCadastrador(usuarioDt.getId_UsuarioServentia());
	        pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_EM_ANDAMENTO));
	        pendenciaDt.setId_Processo(processoDt.getId());
	        pendenciaDt.setPrazo(String.valueOf(PendenciaDt.PRAZO_PARA_REALIZACAO_CARGA));
	        
	        pendenciaDt.setIpComputadorLog(usuarioDt.getIpComputadorLog());
	        pendenciaDt.setId_UsuarioLog(usuarioDt.getId());
	        
	        Calendar calCalendario = Calendar.getInstance(); //Funcoes.BancoDataHora(new Date())
	        calCalendario.setTime(new Date());
	
	        // Proximo dia, a contagem se inicia no dia seguinte
	        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
	        pendenciaDt.setDataInicio(df.format(new Date()));
	        pendenciaDt.setDataVisto(df.format(new Date()));
	        calCalendario.add(Calendar.DATE, PendenciaDt.PRAZO_PARA_REALIZACAO_CARGA);
	        pendenciaDt.setDataLimite(df.format(calCalendario.getTime()));
	        
	        PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
	        
	        PendenciaTipoNe pendenciaTipoNe = new PendenciaTipoNe();
	        PendenciaTipoDt pendenciaTipoDt = pendenciaTipoNe.consultarPendenciaTipoCodigo(PendenciaTipoDt.SOLICITACAO_CARGA);
	        if (pendenciaTipoDt.getId() == null){
	        	throw new MensagemException("Pend�ncia do tipo Solicita��o de Carga ao processo n�o foi encontrada");
	        } else {
	        	pendenciaDt.setId_PendenciaTipo(pendenciaTipoDt.getId());
	        }
	
	        pendenciaPs.inserir(pendenciaDt);
	        obDados.copiar(pendenciaDt);
	        
	        // Grava o log da operacao
	        this.gravarLog(pendenciaDt, obFabricaConexao);
	
	        // Inserir responsavel na pendencia
	        PendenciaResponsavelDt pendenciaResponsavelDt = new PendenciaResponsavelDt();
	        PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
        	pendenciaResponsavelDt.setId_Serventia(processoDt.getId_Serventia());
	        pendenciaResponsavelDt.setId_Pendencia(pendenciaDt.getId());
	        pendenciaResponsavelDt.setId_UsuarioLog(usuarioDt.getId());
            pendenciaResponsavelDt.setIpComputadorLog(usuarioDt.getIpComputadorLog());
            pendenciaResponsavelNe.inserir(pendenciaResponsavelDt, obFabricaConexao);                 
            
            obFabricaConexao.finalizarTransacao();
        } catch (Exception e) {
            // Se a conexao foi criada dentro do metodo, entao cancela a conexao
            obFabricaConexao.cancelarTransacao();
            throw e;
        } finally {
            // Se a conexao foi criada dentro do metodo, entao finaliza a
            // conexao
            obFabricaConexao.fecharConexao();
        }

        // Operacoes realizadas com sucesso
        return true;
    }
    
    /**
     * Gera uma pend�ncia filha ao realizar carga de processo
     * 
     * @param pendenciaDt, pend�ncia que ser� gerada
     * @param serventiaDt, serventia destino
     * @param UsuarioDt, UsuarioDt para montar o log
     * @param obFabricaConexao, conex�o ativa
     * 
     * @author lsbernardes
     */
    public PendenciaDt gerarPendenciaFilhaSolicitarCargaProcesso(PendenciaDt pendenciaDt, UsuarioDt usuarioDt, FabricaConexao obFabricaConexao) throws Exception {
    	PendenciaDt pendenciaRetornoDt;
    	try {
            // Verifica se a conexao sera criada internamente
            if (obFabricaConexao == null) {
                obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
                obFabricaConexao.iniciarTransacao();
            } else {
                // Caso da conexao criada em um nivel superior
                obFabricaConexao = obFabricaConexao;
            }

            pendenciaRetornoDt = pendenciaDt.criarFilha();
            PendenciaResponsavelDt pendenciaResponsavel = new PendenciaResponsavelDt();
            pendenciaResponsavel.setId_Serventia(pendenciaDt.getProcessoDt().getId_Serventia());
            pendenciaRetornoDt.setPrazo(String.valueOf(PendenciaDt.PRAZO_PARA_DEVOLUCAO_AUTOS));
            pendenciaRetornoDt.addResponsavel(pendenciaResponsavel);

            // Limpa os dados e atribui o novo status
            pendenciaRetornoDt.setId_PendenciaStatus("null");
            pendenciaRetornoDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_RETORNO));
            
            Calendar calCalendario = Calendar.getInstance(); //Funcoes.BancoDataHora(new Date())
            // Proximo dia, a contagem se inicia no dia seguinte
	        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
	        pendenciaRetornoDt.setDataInicio(df.format(new Date()));
	        pendenciaRetornoDt.setDataVisto("");
	        calCalendario.add(Calendar.DATE, PendenciaDt.PRAZO_PARA_DEVOLUCAO_AUTOS);
	        pendenciaRetornoDt.setDataLimite(df.format(calCalendario.getTime()));

            pendenciaRetornoDt.setId_UsuarioLog(usuarioDt.getId());
            pendenciaRetornoDt.setIpComputadorLog(usuarioDt.getIpComputadorLog());

            this.gerarPendencia(pendenciaRetornoDt, obFabricaConexao);

            if (obFabricaConexao == null) 
            	obFabricaConexao.finalizarTransacao();
            
        } catch (Exception e) {
            // Se a conexao foi criada dentro do metodo, entao cancela a conexao
            if (obFabricaConexao == null) 
            	obFabricaConexao.cancelarTransacao();

            throw e;
        } finally {
            // Se a conexao foi criada dentro do metodo, entao finaliza a conexao
            if (obFabricaConexao == null) 
            	obFabricaConexao.fecharConexao();
        }
      
        return pendenciaRetornoDt;
    }
    
    /**
     * Gera uma pend�ncia de retorno ao realizar carga de processo por um usu�rio da serventia
     * 
     * @param UsuarioDt, UsuarioDt para montar o log
     * @param obFabricaConexao, conex�o ativa
     * 
     * @author lsbernardes
     */
    public PendenciaDt gerarPendenciaRetornoSolicitarCargaProcesso(ProcessoDt processoDt, UsuarioDt usuarioDt, FabricaConexao obFabricaConexao) throws Exception {
    	PendenciaDt pendenciaRetornoDt = new PendenciaDt();
    	try {
            // Verifica se a conexao sera criada internamente
            if (obFabricaConexao == null) {
                obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
                obFabricaConexao.iniciarTransacao();
            } else {
                // Caso da conexao criada em um nivel superior
                obFabricaConexao = obFabricaConexao;
            }

            pendenciaRetornoDt.setId_UsuarioCadastrador(usuarioDt.getId_UsuarioServentia());
            pendenciaRetornoDt.setPendenciaTipoCodigo(String.valueOf(PendenciaTipoDt.SOLICITACAO_CARGA));
            pendenciaRetornoDt.setId_Processo(processoDt.getId());
    		
            PendenciaResponsavelDt pendenciaResponsavel = new PendenciaResponsavelDt();
            pendenciaResponsavel.setId_Serventia(processoDt.getId_Serventia());
            pendenciaRetornoDt.setPrazo(String.valueOf(PendenciaDt.PRAZO_PARA_DEVOLUCAO_AUTOS));
            pendenciaRetornoDt.addResponsavel(pendenciaResponsavel);

            pendenciaRetornoDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_RETORNO));
            
            Calendar calCalendario = Calendar.getInstance(); 
            // Proximo dia, a contagem se inicia no dia seguinte
	        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
	        pendenciaRetornoDt.setDataInicio(df.format(new Date()));
	        pendenciaRetornoDt.setDataVisto("");
	        calCalendario.add(Calendar.DATE, PendenciaDt.PRAZO_PARA_DEVOLUCAO_AUTOS);
	        pendenciaRetornoDt.setDataLimite(df.format(calCalendario.getTime()));

            pendenciaRetornoDt.setId_UsuarioLog(usuarioDt.getId());
            pendenciaRetornoDt.setIpComputadorLog(usuarioDt.getIpComputadorLog());

            this.gerarPendencia(pendenciaRetornoDt, obFabricaConexao);

            if (obFabricaConexao == null) 
            	obFabricaConexao.finalizarTransacao();
            
        } catch (Exception e) {
            // Se a conexao foi criada dentro do metodo, entao cancela a conexao
            if (obFabricaConexao == null) 
            	obFabricaConexao.cancelarTransacao();

            throw e;
        } finally {
            // Se a conexao foi criada dentro do metodo, entao finaliza a conexao
            if (obFabricaConexao == null) 
            	obFabricaConexao.fecharConexao();
        }
      
        return pendenciaRetornoDt;
    }
    
    /**
     * Verifica se o processo j� possui uma pendencia do tipo solicita��o de carga para o usu�rio e o processo passado
     * 
     * @author Leandro Bernardes
     * @param  id_Processo, identificado do processo
     * @param  id_UsuarioServentia, identificador do cadastrador
     * @return boolean
     */
    public boolean possuiPendeciaSolicitarCargaProcesso(String id_Processo, String id_UsuarioServentia) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaPs obPersistencia = new PendenciaPs(obFabricaConexao.getConexao()); 
			retorno = obPersistencia.possuiPendeciaSolicitarCargaProcesso(id_Processo, id_UsuarioServentia);
		} finally {
				obFabricaConexao.fecharConexao();
		}
		return retorno;
	}
	
	/**
     * Verifica se o processo j� possui uma pendencia do tipo solicita��o de carga aguardando Retorno gerara pela serventia
     * 
     * @author Leandro Bernardes
     * @param  id_Processo, identificado do processo
     * @return boolean
     */
    public boolean possuiPendeciaSolicitarCargaAguardandoRetornoProcesso(String id_Processo) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaPs obPersistencia = new PendenciaPs(obFabricaConexao.getConexao()); 
			retorno = obPersistencia.possuiPendeciaSolicitarCargaAguardandoRetornoProcesso(id_Processo);
		} finally {
				obFabricaConexao.fecharConexao();
		}
		return retorno;
	}
    
    /**
	 * Consulta as Pendencias do Tipo (Verificar Novo Processo com Pedido de Assist�ncia, Concluso com Pedido de Benef�cio de Assist�ncia) se existirem a partir do idProcesso.
	 * 
	 * @param String idProcesso, id do processo que ter� suas pendencias abertas consultadas
	 * @param obFabricaConexao, conex�o ativa	      
	 * @return List
	 * @throws Exception
	 */
    public List consultarPendenciasProcessoRelacionadasPedidoAssistencia(String id_Processo, int pendenciaTipoCodigo, FabricaConexao obFabricaConexao) throws Exception {
        List tempList = null;
        PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
        tempList = obPersistencia.consultarPendenciasProcessoRelacionadasPedidoAssistencia(id_Processo, pendenciaTipoCodigo);
        return tempList;
    }
    
    /**
	 * M�todo utilizado para alterar o tipo da pendencia de conclus�o ao mudar status de uma guia aguardando deferimento
	 * 
	 * @param pendenciaDt,
	 *            identifica��o da pend�ncia
	 * @param usuarioDt,
	 *            objeto com dados do log
	 * @param conexao,
	 *            conex�o com o banco de dados        
	 */
    public void alterarTipoConclusaoPedidoAssistenciaParaGenericaOuRelator(PendenciaDt pendenciaDt, UsuarioDt usuarioDt, FabricaConexao obFabricaConexao) throws Exception {                
		PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
	    
		pendenciaPs.alterarTipoConclusaoPedidoAssistenciaParaGenericaOuRelator(pendenciaDt, usuarioDt); 
	    
	    String valorAtual = "[Id_Pendencia:" + pendenciaDt.getId() + ";Pend_Tipo_Codigo:" + pendenciaDt.getPendenciaTipoCodigo() + "]";
	    String valorNovo = "";
		
	    if (usuarioDt.isServentiaTipo2Grau()) {
	    	valorNovo = "[Id_Pendencia:" + pendenciaDt.getId() + ";Pend_Tipo_Codigo:" + PendenciaTipoDt.CONCLUSO_RELATOR + "]";
	    } else {
	    	valorNovo = "[Id_Pendencia:" + pendenciaDt.getId() + ";Pend_Tipo_Codigo:" + PendenciaTipoDt.CONCLUSO_GENERICO + "]";
	    }
		
	    LogDt obLogDt = new LogDt("Pendencia", pendenciaDt.getId(), usuarioDt.getId(), usuarioDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), valorAtual, valorNovo);
	    obLog.salvar(obLogDt, obFabricaConexao);
	    
    }
    
    public String consultarServentiaCargoResponsavelPendencia(String id_pendencia) throws Exception{
    	String resultIdPendencia = null;
    	FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);    	
    	try {                		
    		resultIdPendencia = consultarServentiaCargoResponsavelPendencia(id_pendencia, obFabricaConexao);      	            
        } catch (Exception e) {            
            throw e;
        } finally {
        	obFabricaConexao.fecharConexao();
        }
    	
		return resultIdPendencia;   	
    }
    
    public String consultarServentiaCargoResponsavelPendencia(String id_pendencia , FabricaConexao obFabricaConexao) throws Exception{
		PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
		return pendenciaPs.consultarServentiaCargoResponsavelPendencia(id_pendencia); 
    }

	public void alterarReserva(String id_Pendencia, String id_UsuarioServentia, FabricaConexao conexao) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		try {
			if (conexao == null) obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else
				obFabricaConexao = conexao;
			PendenciaPs obPersistencia = new PendenciaPs(obFabricaConexao.getConexao());
//			String valorAtual = "[Id_Pendencia:" + id_Pendencia + ";Id_Classificador:" + id_ClassificadorAnterior + "]";
//			String valorNovo = "[Id_Pendencia:" + id_Pendencia + ";Id_Classificador:" + id_ClassificadorNovo + "]";
//			obLogDt = new LogDt("Pendencia", id_Pendencia, logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.Alterar), valorAtual, valorNovo);
			obPersistencia.alterarReserva(id_Pendencia, id_UsuarioServentia);

//			obLog.salvar(obLogDt, obFabricaConexao);
		} finally {
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
		
	}
	
	/**
	 * M�todo para reservar locomo��o para mandado judicial.
	 * 
	 * @param String idProcesso
	 * @param String idMandadoJudicial
	 * @param String idBairro
	 * @param String quantidade
	 * @param String idUsuarioLog
	 * @param String idUsuarioOficial
	 * @param FabricaConexao obFabricaConexao
	 * 
	 * @return boolean
	 * 
	 * @throws Exception
	 */
	public boolean reservarLocomocao(String idProcesso, String idMandadoJudicial, String idBairro, int quantidade, String idUsuarioLog, String idUsuarioOficial, FabricaConexao obFabricaConexao) throws Exception {
		LocomocaoNe locomocaoNe = new LocomocaoNe();
		return locomocaoNe.reservarLocomocao(idProcesso, idMandadoJudicial, idBairro, quantidade, idUsuarioLog, idUsuarioOficial, obFabricaConexao);
	}
	
	/**
	 * Recebe o id da ServentiaTipo e retorna o seu c�digo
	 * @param idServentiaTipo
	 * @return c�digo da serventia
	 * @throws Exception
	 * @author hrrosa
	 */
	public String consultarCodigoServentiaTipo(String idServentiaTipo) throws Exception {
		ServentiaTipoNe serventiaTipoNe = new ServentiaTipoNe();
		return serventiaTipoNe.consultarCodigoServentiaTipo(idServentiaTipo);
	}

	public ServentiaDt consultarCentralMandadosRelacionada(String id_Serventia) throws Exception {
		ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();
        return serventiaRelacionadaNe.consultarCentralMandadosRelacionada(id_Serventia);
    }
	
	public boolean isCentralMandados(String idServentia, FabricaConexao fabricaConexao) throws Exception {
		ServentiaNe serventiaNe = new ServentiaNe();
		return serventiaNe.isCentralMandados(idServentia, fabricaConexao);
	}
	
	public String consultarOficialJSON(String tempNomeBusca, String posicaoPaginaAtual, String idServentia) throws Exception {
		String stTemp = "";
		MandadoJudicialNe mandadoJudicialNe = new MandadoJudicialNe(); 
		stTemp = mandadoJudicialNe.consultarOficialJSON(tempNomeBusca, posicaoPaginaAtual, idServentia);
		return stTemp;
	}
	
	/**
	 * Utiliza o id da pend�ncia para retornar a quantidade de locomo��es vinculadas ao mandado
	 * ao qual a pend�ncia em quest�o faz refer�ncia.
	 * @param idPend
	 * @return boolean
	 * @throws Exception
	 */
	public boolean teraOficialCompanheiroMandado(String idPend) throws Exception {
		int qtdLocomocao = 0;
		MandadoJudicialDt mandadoJudicialDt = null;
		LocomocaoNe locomocaoNe = new LocomocaoNe();
		MandadoJudicialNe mandadoJudicialNe = new MandadoJudicialNe();
		
		
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);

    	
    	try {
    			//TODO: Mudar m�todo abaixo para aceitar conex�o.
    			mandadoJudicialDt = mandadoJudicialNe.consultarPorIdPendencia(idPend);
    			if(mandadoJudicialDt != null)
				qtdLocomocao = locomocaoNe.consultarQtdLocomocaoVinculadaMandado(mandadoJudicialDt.getId(), obFabricaConexao);
		
    	 } finally {

         	obFabricaConexao.fecharConexao();
    	 }
		
		if(qtdLocomocao >= MandadoJudicialDt.QTD_LOCOMOCAO_OFICIAL_COMPANHEIRO){
			return true;
		} else {
			return false;
		}
	}
	
	
    public List consultarDataSessoesElaboracaoDeVoto(UsuarioNe usuarioNe) throws Exception{
    	List dataSessao = null;
    	FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

    	
    	try {
            obFabricaConexao.iniciarTransacao();

    		PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());

    		dataSessao = pendenciaPs.consultarDataSessoesElaboracaoDeVoto(usuarioNe); 
    		
    		
        	obFabricaConexao.finalizarTransacao();
            
        } catch (Exception e) {
            	obFabricaConexao.cancelarTransacao();

            throw e;
        } finally {

            	obFabricaConexao.fecharConexao();
        }
    	
		return dataSessao;
    }
	
	
	
	
    public PendenciaDt gerarPendenciaProclamarVoto(String idDestinatario, UsuarioDt usuario, String idProcesso) throws Exception {
		FabricaConexao conexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {
			conexao.iniciarTransacao();

			PendenciaDt pendenciaDt = gerarPendenciaProclamarVoto(idDestinatario, usuario, idProcesso, conexao);
			
			conexao.finalizarTransacao();
			
			return pendenciaDt;
		} catch (Exception e) {
			conexao.cancelarTransacao();
			throw e;
		} finally {
			conexao.fecharConexao();
		}
		
	}

	protected PendenciaDt gerarPendenciaProclamarVoto(String idDestinatario, UsuarioDt usuario, String idProcesso,
			FabricaConexao conexao) throws Exception, MensagemException {
		PendenciaDt pendenciaDt = new PendenciaDt();
		
		pendenciaDt.setId_UsuarioCadastrador(usuario.getId_UsuarioServentia());
		pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO));
		pendenciaDt.setId_Processo(idProcesso);
		
		
		pendenciaDt.setIpComputadorLog(usuario.getIpComputadorLog());
		pendenciaDt.setId_UsuarioLog(usuario.getId());
		
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
		pendenciaDt.setDataInicio(df.format(new Date()));
		
		PendenciaPs pendenciaPs = new  PendenciaPs(conexao.getConexao());
		
		PendenciaTipoNe pendenciaTipoNe = new PendenciaTipoNe();
		PendenciaTipoDt pendenciaTipoDt = pendenciaTipoNe.consultarPendenciaTipoCodigo(PendenciaTipoDt.PROCLAMACAO_VOTO);
		if (pendenciaTipoDt.getId() == null){
			throw new MensagemException("Tipo n�o encontrado");
		}
		pendenciaDt.setId_PendenciaTipo(pendenciaTipoDt.getId());
		
		pendenciaPs.inserir(pendenciaDt);
		obDados.copiar(pendenciaDt);
		
		// Grava o log da operacao
		this.gravarLog(pendenciaDt, conexao);
		
		// Inserir responsavel na pendencia
		PendenciaResponsavelDt pendenciaResponsavelDt = new PendenciaResponsavelDt();
		PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
		pendenciaResponsavelDt.setId_ServentiaCargo(idDestinatario);
		pendenciaResponsavelDt.setId_Pendencia(pendenciaDt.getId());
		pendenciaResponsavelDt.setIpComputadorLog(usuario.getIpComputadorLog());
		pendenciaResponsavelDt.setId_UsuarioLog(usuario.getId());		
		pendenciaResponsavelNe.inserir(pendenciaResponsavelDt, conexao);
		return pendenciaDt;
	}
    
    public PendenciaDt gerarPendenciaPedirVista(String idDestinatario, String idServentia, UsuarioDt usuario, String idProcesso) throws Exception {
		FabricaConexao conexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

		conexao.iniciarTransacao();
		try {
			PendenciaDt pendenciaDt = new PendenciaDt();
			
			pendenciaDt.setId_UsuarioCadastrador(usuario.getId_UsuarioServentia());
			pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO));
			pendenciaDt.setId_Processo(idProcesso);
			
			
			pendenciaDt.setIpComputadorLog(usuario.getIpComputadorLog());
			pendenciaDt.setId_UsuarioLog(usuario.getId());
			
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
			pendenciaDt.setDataInicio(df.format(new Date()));
			
			PendenciaPs pendenciaPs = new  PendenciaPs(conexao.getConexao());
			
			PendenciaTipoNe pendenciaTipoNe = new PendenciaTipoNe();
			PendenciaTipoDt pendenciaTipoDt = pendenciaTipoNe.consultarPendenciaTipoCodigo(PendenciaTipoDt.PEDIDO_VISTA_SESSAO);
			if (pendenciaTipoDt.getId() == null){
				throw new MensagemException("Tipo n�o encontrado");
			}
			pendenciaDt.setId_PendenciaTipo(pendenciaTipoDt.getId());
			
			pendenciaPs.inserir(pendenciaDt);
			obDados.copiar(pendenciaDt);
			
			// Grava o log da operacao
			this.gravarLog(pendenciaDt, conexao);
			
			// Inserir responsavel na pendencia
			PendenciaResponsavelDt pendenciaResponsavelDt = new PendenciaResponsavelDt();
			PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
			if(idDestinatario == null) {
				pendenciaResponsavelDt.setId_Serventia(idServentia);
				}
			else {
				pendenciaResponsavelDt.setId_ServentiaCargo(idDestinatario);
				
			}
			
			pendenciaResponsavelDt.setId_Pendencia(pendenciaDt.getId());
			pendenciaResponsavelDt.setIpComputadorLog(usuario.getIpComputadorLog());
			pendenciaResponsavelDt.setId_UsuarioLog(usuario.getId());		
			pendenciaResponsavelNe.inserir(pendenciaResponsavelDt, conexao);
			conexao.finalizarTransacao();
			
			return pendenciaDt;
		} catch (Exception e) {
			conexao.cancelarTransacao();
			throw e;
		} finally {
			conexao.fecharConexao();
		}
		
	}
    
    public void gerarPendenciaTomarConhecimento(String idDestinatario, String idServentia, UsuarioDt usuario, String idProcesso, int tipo, FabricaConexao conexao) throws Exception {
			PendenciaDt pendenciaDt = new PendenciaDt();
			
			pendenciaDt.setId_UsuarioCadastrador(usuario.getId_UsuarioServentia());
			pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO));
			pendenciaDt.setId_Processo(idProcesso);
			
			
			pendenciaDt.setIpComputadorLog(usuario.getIpComputadorLog());
			pendenciaDt.setId_UsuarioLog(usuario.getId());
			
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
			pendenciaDt.setDataInicio(df.format(new Date()));
			
			PendenciaPs pendenciaPs = new  PendenciaPs(conexao.getConexao());
			
			PendenciaTipoNe pendenciaTipoNe = new PendenciaTipoNe();
			PendenciaTipoDt pendenciaTipoDt = pendenciaTipoNe.consultarPendenciaTipoCodigo(tipo);
			if (pendenciaTipoDt.getId() == null){
				throw new MensagemException("Tipo n�o encontrado");
			} else {
				pendenciaDt.setId_PendenciaTipo(pendenciaTipoDt.getId());
			}
			
			pendenciaPs.inserir(pendenciaDt);
			obDados.copiar(pendenciaDt);
			
			// Grava o log da operacao
			this.gravarLog(pendenciaDt, conexao);
			
			// Inserir responsavel na pendencia
			PendenciaResponsavelDt pendenciaResponsavelDt = new PendenciaResponsavelDt();
			PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
			if(idDestinatario == null) {
				pendenciaResponsavelDt.setId_Serventia(idServentia);
				}
			else {
				pendenciaResponsavelDt.setId_ServentiaCargo(idDestinatario);
				
			}
			
			pendenciaResponsavelDt.setId_Pendencia(pendenciaDt.getId());
			pendenciaResponsavelDt.setIpComputadorLog(usuario.getIpComputadorLog());
			pendenciaResponsavelDt.setId_UsuarioLog(usuario.getId());			
			pendenciaResponsavelNe.inserir(pendenciaResponsavelDt, conexao);
	}
    
	public PendenciaDt gerarPendenciaVerificarVotantes(ProcessoDt processo, UsuarioDt usuario, FabricaConexao conexao) throws Exception {
			PendenciaDt pendenciaDt = new PendenciaDt();
			
			pendenciaDt.setId_UsuarioCadastrador(UsuarioDt.SistemaProjudi);
			pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO));
			pendenciaDt.setId_Processo(processo.getId());
			
			
			pendenciaDt.setIpComputadorLog(usuario.getIpComputadorLog());
			pendenciaDt.setId_UsuarioLog(usuario.getId());
			
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
			pendenciaDt.setDataInicio(df.format(new Date()));
			
			PendenciaPs pendenciaPs = new  PendenciaPs(conexao.getConexao());
			
			PendenciaTipoNe pendenciaTipoNe = new PendenciaTipoNe();
			PendenciaTipoDt pendenciaTipoDt = pendenciaTipoNe.consultarPendenciaTipoCodigo(PendenciaTipoDt.VERIFICAR_IMPEDIMENTO_VOTANTES, conexao);
			if (pendenciaTipoDt.getId() == null){
				throw new MensagemException("Tipo n�o encontrado");
			}
			pendenciaDt.setId_PendenciaTipo(pendenciaTipoDt.getId());
			
			pendenciaPs.inserir(pendenciaDt);
			obDados.copiar(pendenciaDt);
			
			// Grava o log da operacao
			this.gravarLog(pendenciaDt, conexao);
			
			// Inserir responsavel na pendencia
			PendenciaResponsavelDt pendenciaResponsavelDt = new PendenciaResponsavelDt();
			PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
			pendenciaResponsavelDt.setId_Pendencia(pendenciaDt.getId());
			pendenciaResponsavelDt.setId_Serventia(processo.getId_Serventia());
			pendenciaResponsavelDt.setIpComputadorLog(usuario.getIpComputadorLog());
			pendenciaResponsavelDt.setId_UsuarioLog(usuario.getId());		
			pendenciaResponsavelNe.inserir(pendenciaResponsavelDt, conexao);
			
			return pendenciaDt;
	}
	
	// jvosantos - 22/08/2019 15:58 - Adicionar ID da AUDI_PROC para gerar a pend�ncia de voto da sess�o virtual, para verificar se existe pend�ncia aberta 
    public PendenciaDt gerarPendenciaVotoSessaoVirtual(String idServentiaCargo, UsuarioDt usuario, String idProcesso, String dataLimite, String idAudienciaProcesso) throws Exception {
		FabricaConexao conexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

		conexao.iniciarTransacao();
		try {
			PendenciaDt pendenciaDt = gerarPendenciaVotoSessaoVirtual(idServentiaCargo, usuario, idProcesso, dataLimite, idAudienciaProcesso,
					conexao);
			
			conexao.finalizarTransacao();
			return pendenciaDt;
		} catch (Exception e) {
			conexao.cancelarTransacao();
			throw e;
		} finally {
			conexao.fecharConexao();
		}
		
	}

	public PendenciaDt gerarPendenciaVotoSessaoVirtual(String idServentiaCargo, UsuarioDt usuario, String idProcesso,
			String dataLimite, String idAudienciaProcesso, FabricaConexao conexao) throws Exception, MensagemException {
		PendenciaDt pendenciaDt = new PendenciaDt();
		
		//lrcampos 04/03/2020 11:55 - Incluindo usuario Cadastrador como o usuario logado no sistema
		pendenciaDt.setId_UsuarioCadastrador(usuario.getId_UsuarioServentia());
		pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO));
		pendenciaDt.setId_Processo(idProcesso);
		
		
		pendenciaDt.setIpComputadorLog(usuario.getIpComputadorLog());
		pendenciaDt.setId_UsuarioLog(usuario.getId());
		DateTimeFormatter formatterSegundos = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		
		pendenciaDt.setDataInicio(LocalDateTime.now().format(formatterSegundos));
		
		pendenciaDt.setDataLimite(dataLimite);
		PendenciaPs pendenciaPs = new  PendenciaPs(conexao.getConexao());
		PendenciaTipoNe pendenciaTipoNe = new PendenciaTipoNe();
		PendenciaTipoDt pendenciaTipoDt = pendenciaTipoNe.consultarPendenciaTipoCodigo(PendenciaTipoDt.VOTO_SESSAO, conexao);
		if (pendenciaTipoDt.getId() == null){
			throw new MensagemException("Tipo n�o encontrado");
		}
		pendenciaDt.setId_PendenciaTipo(pendenciaTipoDt.getId());
		List<PendenciaDt> emAndamento = pendenciaPs.consultarPendenciasAudienciaProcessoResponsavelPorTipo(idAudienciaProcesso, idServentiaCargo, PendenciaTipoDt.VOTO_SESSAO);
		if(emAndamento != null && !emAndamento.isEmpty()) {
			return null;
		}
		
		pendenciaPs.inserir(pendenciaDt);
		obDados.copiar(pendenciaDt);
		
		// Grava o log da operacao
		this.gravarLog(pendenciaDt, conexao);
		
		// Inserir responsavel na pendencia
		PendenciaResponsavelDt pendenciaResponsavelDt = new PendenciaResponsavelDt();
		PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
		pendenciaResponsavelDt.setId_ServentiaCargo(idServentiaCargo);
		pendenciaResponsavelDt.setId_Pendencia(pendenciaDt.getId());
		pendenciaResponsavelDt.setId_UsuarioLog(usuario.getId());
		pendenciaResponsavelDt.setIpComputadorLog(usuario.getIpComputadorLog());
		pendenciaResponsavelNe.inserir(pendenciaResponsavelDt, conexao);
		return pendenciaDt;
	}
    
	public PendenciaDt gerarPendenciaSessaoConhecimento(String idServentiaCargo, UsuarioDt usuario, String idProcesso, String idAudiProc) throws Exception {
		 return gerarPendenciaSessaoConhecimento(idServentiaCargo, usuario, idProcesso, idAudiProc, null);
	 }
	
    public PendenciaDt gerarPendenciaSessaoConhecimento(String idServentiaCargo, UsuarioDt usuario, String idProcesso, String idAudiProc, FabricaConexao obFabricaConexao) throws Exception {
		FabricaConexao conexao = obFabricaConexao;

		try {
			if(conexao == null) {
				conexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				conexao.iniciarTransacao();
			}
			PendenciaDt pendenciaDt = new PendenciaDt();
			
			pendenciaDt.setId_UsuarioCadastrador(usuario.getId_UsuarioServentia());
			pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO));
			pendenciaDt.setId_Processo(idProcesso);
			
			Funcoes.preencheUsuarioLog(pendenciaDt, usuario);
			
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
			pendenciaDt.setDataInicio(df.format(new Date()));
			
			PendenciaPs pendenciaPs = new  PendenciaPs(conexao.getConexao());

			List<PendenciaDt> pendenciasAbertas = pendenciaPs.consultarPendenciasAudienciaProcessoResponsavelPorTipo(idAudiProc, idServentiaCargo, PendenciaTipoDt.SESSAO_CONHECIMENTO);

			if(CollectionUtils.isNotEmpty(pendenciasAbertas)) return null;
			
			PendenciaTipoNe pendenciaTipoNe = new PendenciaTipoNe();
			PendenciaTipoDt pendenciaTipoDt = pendenciaTipoNe.consultarPendenciaTipoCodigo(PendenciaTipoDt.SESSAO_CONHECIMENTO, conexao);
			if (pendenciaTipoDt.getId() == null){
				throw new MensagemException("Tipo n�o encontrado");
			}
			pendenciaDt.setId_PendenciaTipo(pendenciaTipoDt.getId());
			
			pendenciaPs.inserir(pendenciaDt);
			obDados.copiar(pendenciaDt);
			
			// Grava o log da operacao
			this.gravarLog(pendenciaDt, conexao);
			
			// Inserir responsavel na pendencia
			PendenciaResponsavelDt pendenciaResponsavelDt = new PendenciaResponsavelDt();
			PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
			pendenciaResponsavelDt.setId_ServentiaCargo(idServentiaCargo);
			pendenciaResponsavelDt.setId_Pendencia(pendenciaDt.getId());
			
			Funcoes.preencheUsuarioLog(pendenciaResponsavelDt, usuario);
			
			pendenciaResponsavelNe.inserir(pendenciaResponsavelDt, conexao);
			
			new AudienciaProcessoPendenciaNe().salvar(pendenciaDt.getId(), idAudiProc, conexao);
			
			if(obFabricaConexao == null)
				conexao.finalizarTransacao();
			
			return pendenciaDt;
		} catch (Exception e) {
			if(obFabricaConexao == null)conexao.cancelarTransacao();
			throw e;
		} finally {
			if(obFabricaConexao == null)conexao.fecharConexao();
		}
		
	}
    
    public PendenciaDt gerarPendenciaVerificarImpedimento(String idServentiaCargo, UsuarioDt usuario, String idProcesso) throws Exception {
    	return gerarPendenciaVerificarImpedimento(idServentiaCargo, usuario, idProcesso, null);
    	
    }
    
    public PendenciaDt gerarPendenciaVerificarImpedimento(String idServentiaCargo, UsuarioDt usuario, String idProcesso, FabricaConexao obFabricaConexao) throws Exception {
		FabricaConexao conexao = obFabricaConexao;

		try {
			if(conexao == null) {
				conexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				conexao.iniciarTransacao();
			}
			
			PendenciaDt pendenciaDt = new PendenciaDt();
			
			pendenciaDt.setId_UsuarioCadastrador(usuario.getId_UsuarioServentia());
			pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO));
			pendenciaDt.setId_Processo(idProcesso);
			
			
			pendenciaDt.setIpComputadorLog(usuario.getIpComputadorLog());
			pendenciaDt.setId_UsuarioLog(usuario.getId());
			
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
			pendenciaDt.setDataInicio(df.format(new Date()));
			
			PendenciaPs pendenciaPs = new  PendenciaPs(conexao.getConexao());
			List<PendenciaDt> list = pendenciaPs.consultarPendenciasProcessoResponsavelPorTipo(idProcesso, idServentiaCargo, PendenciaTipoDt.VERIFICAR_IMPEDIMENTO);
			if(CollectionUtils.isNotEmpty(list)) return null;
			
			PendenciaTipoNe pendenciaTipoNe = new PendenciaTipoNe();
			PendenciaTipoDt pendenciaTipoDt = pendenciaTipoNe.consultarPendenciaTipoCodigo(PendenciaTipoDt.VERIFICAR_IMPEDIMENTO, conexao);
			
			if (pendenciaTipoDt.getId() == null){
				throw new MensagemException("Tipo n�o encontrado");
			}
			
			pendenciaDt.setId_PendenciaTipo(pendenciaTipoDt.getId());
			
			pendenciaPs.inserir(pendenciaDt);
			obDados.copiar(pendenciaDt);
			
			// Grava o log da operacao
			this.gravarLog(pendenciaDt, conexao);
			
			// Inserir responsavel na pendencia
			PendenciaResponsavelDt pendenciaResponsavelDt = new PendenciaResponsavelDt();
			PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
			pendenciaResponsavelDt.setId_ServentiaCargo(idServentiaCargo);
			pendenciaResponsavelDt.setId_Pendencia(pendenciaDt.getId());
			pendenciaResponsavelDt.setId_UsuarioLog(usuario.getId());
			pendenciaResponsavelDt.setIpComputadorLog(usuario.getIpComputadorLog());
			pendenciaResponsavelNe.inserir(pendenciaResponsavelDt, conexao);
			
			if(obFabricaConexao == null) {
				conexao.finalizarTransacao();
			}
			
			return pendenciaDt;
		} catch (Exception e) {
			if(obFabricaConexao == null) conexao.cancelarTransacao();
			throw e;
		} finally {
			if(obFabricaConexao == null)conexao.fecharConexao();
		}
		
	}
    
	public PendenciaDt gerarPendenciaVerificarImpedimentoMinisterioPublico(String idServentia, UsuarioDt usuario,
			String idProcesso) throws Exception {
		FabricaConexao conexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

		conexao.iniciarTransacao();
		try {
			PendenciaDt pendenciaDt = new PendenciaDt();

			pendenciaDt.setId_UsuarioCadastrador(UsuarioDt.SistemaProjudi);
			pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO));
			pendenciaDt.setId_Processo(idProcesso);

			pendenciaDt.setIpComputadorLog(usuario.getIpComputadorLog());
			pendenciaDt.setId_UsuarioLog(usuario.getId());
			pendenciaDt.setId_UsuarioCadastrador(usuario.getId_UsuarioServentia());

			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			pendenciaDt.setDataInicio(df.format(new Date()));

			PendenciaPs pendenciaPs = new PendenciaPs(conexao.getConexao());

			PendenciaTipoNe pendenciaTipoNe = new PendenciaTipoNe();
			PendenciaTipoDt pendenciaTipoDt = pendenciaTipoNe
					.consultarPendenciaTipoCodigo(PendenciaTipoDt.VERIFICAR_IMPEDIMENTO_MP, conexao);
			if (pendenciaTipoDt.getId() == null) {
				throw new MensagemException("Tipo n�o encontrado");
			} else {
				pendenciaDt.setId_PendenciaTipo(pendenciaTipoDt.getId());
			}

			pendenciaPs.inserir(pendenciaDt);
			obDados.copiar(pendenciaDt);

			// Grava o log da operacao
			this.gravarLog(pendenciaDt, conexao);

			// Inserir responsavel na pendencia
			PendenciaResponsavelDt pendenciaResponsavelDt = new PendenciaResponsavelDt();
			PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();

			pendenciaResponsavelDt.setId_Serventia(idServentia);
			pendenciaResponsavelDt.setIpComputadorLog(usuario.getIpComputadorLog());
			pendenciaResponsavelDt.setId_UsuarioLog(usuario.getId());
			pendenciaResponsavelDt.setId_Pendencia(pendenciaDt.getId());
			pendenciaResponsavelNe.inserir(pendenciaResponsavelDt, conexao);

			conexao.finalizarTransacao();
			
			return pendenciaDt;
		} catch (Exception e) {
			conexao.cancelarTransacao();
			throw e;
		} finally {
			conexao.fecharConexao();
		}

	}
    
    public PendenciaDt gerarPendenciaResultadoVotacao(String idDestinatario, String idServentia, UsuarioDt usuario, String idProcesso, FabricaConexao fabrica) throws Exception {
		PendenciaDt pendenciaDt = new PendenciaDt();
		
		pendenciaDt.setId_UsuarioCadastrador(usuario.getId_UsuarioServentia());
		pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO));
		pendenciaDt.setId_Processo(idProcesso);
		
		
		pendenciaDt.setIpComputadorLog(usuario.getIpComputadorLog());
		pendenciaDt.setId_UsuarioLog(usuario.getId());
		
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
		pendenciaDt.setDataInicio(df.format(new Date()));
		
		PendenciaPs pendenciaPs = new  PendenciaPs(fabrica.getConexao());
		
		PendenciaTipoNe pendenciaTipoNe = new PendenciaTipoNe();
		PendenciaTipoDt pendenciaTipoDt = pendenciaTipoNe.consultarPendenciaTipoCodigo(PendenciaTipoDt.RESULTADO_VOTACAO, fabrica);
		if (pendenciaTipoDt.getId() == null){
			throw new MensagemException("Tipo n�o encontrado");
		}
		pendenciaDt.setId_PendenciaTipo(pendenciaTipoDt.getId());
		
		pendenciaPs.inserir(pendenciaDt);
		obDados.copiar(pendenciaDt);
		
		// Grava o log da operacao
		this.gravarLog(pendenciaDt, fabrica);
		
		// Inserir responsavel na pendencia
		PendenciaResponsavelDt pendenciaResponsavelDt = new PendenciaResponsavelDt();
		PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
		if(idDestinatario == null) {
			pendenciaResponsavelDt.setId_Serventia(idServentia);
			}
		else {
			pendenciaResponsavelDt.setId_ServentiaCargo(idDestinatario);
			
		}
		pendenciaResponsavelDt.setIpComputadorLog(usuario.getIpComputadorLog());
		pendenciaResponsavelDt.setId_UsuarioLog(usuario.getId());		
		pendenciaResponsavelDt.setId_Pendencia(pendenciaDt.getId());
		pendenciaResponsavelNe.inserir(pendenciaResponsavelDt, fabrica);
		
		return pendenciaDt;
	}
	
	// jvosantos - 04/06/2019 10:07 - Gera��o de pendencia de apreciados
    public PendenciaDt gerarPendenciaApreciados(String idDestinatario, String idServentia, UsuarioDt usuario, String idProcesso, String audiProc, FabricaConexao fabrica) throws Exception {
		PendenciaDt pendenciaDt = new PendenciaDt();
		
		pendenciaDt.setId_UsuarioCadastrador(usuario.getId_UsuarioServentia());
		pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO));
		pendenciaDt.setId_Processo(idProcesso);
		
		
		pendenciaDt.setIpComputadorLog(usuario.getIpComputadorLog());
		pendenciaDt.setId_UsuarioLog(usuario.getId());
		pendenciaDt.setCodigoTemp(audiProc);
		
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
		pendenciaDt.setDataInicio(df.format(new Date()));
		
		PendenciaPs pendenciaPs = new  PendenciaPs(fabrica.getConexao());
		
		PendenciaTipoNe pendenciaTipoNe = new PendenciaTipoNe();
		PendenciaTipoDt pendenciaTipoDt = pendenciaTipoNe.consultarPendenciaTipoCodigo(PendenciaTipoDt.APRECIADOS, fabrica);
		if (pendenciaTipoDt.getId() == null){
			throw new MensagemException("Tipo n�o encontrado");
		}
		pendenciaDt.setId_PendenciaTipo(pendenciaTipoDt.getId());
		
		pendenciaPs.inserir(pendenciaDt);
		obDados.copiar(pendenciaDt);
		
		// Grava o log da operacao
		this.gravarLog(pendenciaDt, fabrica);
		
		// Inserir responsavel na pendencia
		PendenciaResponsavelDt pendenciaResponsavelDt = new PendenciaResponsavelDt();
		PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
		if(idDestinatario == null) {
			pendenciaResponsavelDt.setId_Serventia(idServentia);
			}
		else {
			pendenciaResponsavelDt.setId_ServentiaCargo(idDestinatario);
			
		}
		pendenciaResponsavelDt.setIpComputadorLog(usuario.getIpComputadorLog());
		pendenciaResponsavelDt.setId_UsuarioLog(usuario.getId());		
		pendenciaResponsavelDt.setId_Pendencia(pendenciaDt.getId());
		pendenciaResponsavelNe.inserir(pendenciaResponsavelDt, fabrica);
		
		return pendenciaDt;
	}
	
    public PendenciaDt gerarPendenciaPedidoSustentacaoOral(String idServentiaCargo, String idServentia, UsuarioDt usuario, String idProcesso, String procParteAdv) throws Exception {
    	FabricaConexao obFabricaConexao = null;
    	PendenciaDt pendenciaDt = null;
    	try {
    		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
    		obFabricaConexao.iniciarTransacao();
    		pendenciaDt = gerarPendenciaPedidoSustentacaoOral(idServentiaCargo, idServentia, usuario, idProcesso, procParteAdv, obFabricaConexao);
    		obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao(); 
			throw e;
		}finally {
			obFabricaConexao.fecharConexao();
		}
    	
    	return pendenciaDt;

    }
    
    public PendenciaDt gerarPendenciaPedidoSustentacaoOral(String idServentiaCargo, String idServentia, UsuarioDt usuario, String idProcesso, String procParteAdv, FabricaConexao conexao) throws Exception {

		PendenciaDt pendenciaDt = new PendenciaDt();

			pendenciaDt.setId_UsuarioCadastrador(UsuarioDt.SistemaProjudi);
			pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO));
			pendenciaDt.setId_Processo(idProcesso);
			pendenciaDt.setId_ProcessoParte(procParteAdv);
			
			pendenciaDt.setIpComputadorLog(usuario.getIpComputadorLog());
			pendenciaDt.setId_UsuarioLog(usuario.getId());
			pendenciaDt.setId_UsuarioCadastrador(usuario.getId_UsuarioServentia());
			
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
			pendenciaDt.setDataInicio(df.format(new Date()));
			
			PendenciaPs pendenciaPs = new  PendenciaPs(conexao.getConexao());
			
			PendenciaTipoNe pendenciaTipoNe = new PendenciaTipoNe();
			PendenciaTipoDt pendenciaTipoDt = pendenciaTipoNe.consultarPendenciaTipoCodigo(PendenciaTipoDt.VERIFICAR_PEDIDO_SUSTENTACAO_ORAL, conexao);
			if (pendenciaTipoDt.getId() == null){
				throw new MensagemException("Tipo n�o encontrado");
			} else {
				pendenciaDt.setId_PendenciaTipo(pendenciaTipoDt.getId());
			}
			
			pendenciaPs.inserir(pendenciaDt);
			obDados.copiar(pendenciaDt);
			
			// Grava o log da operacao
			this.gravarLog(pendenciaDt, conexao);
			
			// Inserir responsavel na pendencia
			PendenciaResponsavelDt pendenciaResponsavelDt = new PendenciaResponsavelDt();
			PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
			if(idServentiaCargo == null) {
				pendenciaResponsavelDt.setId_Serventia(idServentia);
				}
			else {
				pendenciaResponsavelDt.setId_ServentiaCargo(idServentiaCargo);
				
			}
			pendenciaResponsavelDt.setId_Pendencia(pendenciaDt.getId());
			pendenciaResponsavelDt.setIpComputadorLog(usuario.getIpComputadorLog());
			pendenciaResponsavelDt.setId_UsuarioLog(usuario.getId());		
			pendenciaResponsavelNe.inserir(pendenciaResponsavelDt, conexao);

		return pendenciaDt;
		
	}
    
    public PendenciaDt gerarPendenciaPedidoSustentacaoOralDeferido(String idAdvSolicitante, String idServentia, UsuarioDt usuario, String idProcesso, FabricaConexao conexao) throws Exception {
    	// mrbatista - 03/09/2019 15:00 - Alterado para melhor controle de conex�o.
	
			PendenciaDt pendenciaDt = new PendenciaDt();
			pendenciaDt.setId_UsuarioCadastrador(UsuarioDt.SistemaProjudi);
			pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO));
			pendenciaDt.setId_Processo(idProcesso);

			pendenciaDt.setIpComputadorLog(usuario.getIpComputadorLog());
			pendenciaDt.setId_UsuarioLog(usuario.getId());
			pendenciaDt.setId_UsuarioCadastrador(usuario.getId_UsuarioServentia());
			
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
			pendenciaDt.setDataInicio(df.format(new Date()));
			
			PendenciaPs pendenciaPs = new  PendenciaPs(conexao.getConexao());
			
			PendenciaTipoNe pendenciaTipoNe = new PendenciaTipoNe();
			PendenciaTipoDt pendenciaTipoDt = pendenciaTipoNe.consultarPendenciaTipoCodigo(PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_DEFERIDO, conexao);
			if (pendenciaTipoDt.getId() == null){
				throw new MensagemException("Tipo n�o encontrado");
			} else {
				pendenciaDt.setId_PendenciaTipo(pendenciaTipoDt.getId());
			}
			
			pendenciaPs.inserir(pendenciaDt);
			obDados.copiar(pendenciaDt);
			
			// Grava o log da operacao
			this.gravarLog(pendenciaDt, conexao);
			
			// Inserir responsavel na pendencia
			PendenciaResponsavelDt pendenciaResponsavelDt = new PendenciaResponsavelDt();
			PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
			if(idAdvSolicitante == null) {
				pendenciaResponsavelDt.setId_Serventia(idServentia);
				}
			else {
				pendenciaResponsavelDt.setId_UsuarioResponsavel(idAdvSolicitante);
				
			}
			pendenciaResponsavelDt.setIpComputadorLog(usuario.getIpComputadorLog());
			pendenciaResponsavelDt.setId_UsuarioLog(usuario.getId());		
			pendenciaResponsavelDt.setId_Pendencia(pendenciaDt.getId());
			pendenciaResponsavelNe.inserir(pendenciaResponsavelDt, conexao);
			
		return pendenciaDt;
		
		
		
	}
    
    public PendenciaDt gerarPendenciaPedidoSustentacaoOralIndeferida(String idAdvSolicitante, String idServentia,
			UsuarioDt usuario, String idProcesso) throws Exception {
    	
    	FabricaConexao obFabricaConexao = null;
    	PendenciaDt pendenciaDt = null;
    	try {
    		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
    		obFabricaConexao.iniciarTransacao();
    		pendenciaDt = gerarPendenciaPedidoSustentacaoOralIndeferida(idAdvSolicitante, idServentia, usuario, idProcesso, obFabricaConexao);
    		obFabricaConexao.finalizarTransacao();
    	}catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		}finally {
			obFabricaConexao.fecharConexao();
		}
    	
    	return pendenciaDt;
    }
    
	public PendenciaDt gerarPendenciaPedidoSustentacaoOralIndeferida(String idAdvSolicitante, String idServentia,
			UsuarioDt usuario, String idProcesso, FabricaConexao conexao) throws Exception {

		PendenciaDt pendenciaDt = new PendenciaDt();
		pendenciaDt.setId_UsuarioCadastrador(UsuarioDt.SistemaProjudi);
		pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO));
		pendenciaDt.setId_Processo(idProcesso);

		pendenciaDt.setIpComputadorLog(usuario.getIpComputadorLog());
		pendenciaDt.setId_UsuarioLog(usuario.getId());
		pendenciaDt.setId_UsuarioCadastrador(usuario.getId_UsuarioServentia());

		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		pendenciaDt.setDataInicio(df.format(new Date()));

		PendenciaPs pendenciaPs = new PendenciaPs(conexao.getConexao());

		PendenciaTipoNe pendenciaTipoNe = new PendenciaTipoNe();
		PendenciaTipoDt pendenciaTipoDt = pendenciaTipoNe
				.consultarPendenciaTipoCodigo(PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_INDEFERIDA, conexao);
		if (pendenciaTipoDt.getId() == null) {
			throw new MensagemException("Tipo n�o encontrado");
		} else {
			pendenciaDt.setId_PendenciaTipo(pendenciaTipoDt.getId());
		}

		pendenciaPs.inserir(pendenciaDt);
		obDados.copiar(pendenciaDt);

		// Grava o log da operacao
		this.gravarLog(pendenciaDt, conexao);

		// Inserir responsavel na pendencia
		PendenciaResponsavelDt pendenciaResponsavelDt = new PendenciaResponsavelDt();
		PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
		if (idAdvSolicitante == null) {
			pendenciaResponsavelDt.setId_Serventia(idServentia);
		} else {
			pendenciaResponsavelDt.setId_UsuarioResponsavel(idAdvSolicitante);

		}
		pendenciaResponsavelDt.setId_Pendencia(pendenciaDt.getId());
		pendenciaResponsavelDt.setIpComputadorLog(usuario.getIpComputadorLog());
		pendenciaResponsavelDt.setId_UsuarioLog(usuario.getId());
		pendenciaResponsavelNe.inserir(pendenciaResponsavelDt, conexao);

		return pendenciaDt;

	}
    
	public int consultarQuantidadeAguardandoVotoSessaoVirtual(String idServentiaCargo) throws Exception {
		FabricaConexao obFabricaConexao = null;
		int retorno = 0;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao()); 
			retorno = obPersistencia.consultarQuantidadeVotoSessaoVirtual(idServentiaCargo);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}
	
	
	public void cadastrarVotantesSessaoVirtual(String idAudienciaProcesso, dwrMovimentarProcesso pendMarcarSessao, UsuarioDt usuario, FabricaConexao obFabricaConexao) throws Exception {				
		PendenciaPs obPersistencia = new PendenciaPs(obFabricaConexao.getConexao());	
		
		List listaAudienciaProcessoVotantes = this.getListaAudienciaProcessoVotantes(pendMarcarSessao, usuario, obFabricaConexao);
		for (int i = 0; i < listaAudienciaProcessoVotantes.size(); ++i) {
			AudienciaProcessoVotantesDt audienciaProcessoVotantesDt = (AudienciaProcessoVotantesDt) listaAudienciaProcessoVotantes.get(i);
			audienciaProcessoVotantesDt.setId_AudienciaProcesso(idAudienciaProcesso);
			
			obPersistencia.cadastrarVotantesSessaoVirtual(audienciaProcessoVotantesDt);
		}
	}
	
	public void cadastrarRecursoSecundarioPartes(String idAudienciaProcesso, List<RecursoSecundarioParteDt> listaRecursoSecundarioParte, FabricaConexao obFabricaConexao) throws Exception {				
		PendenciaPs obPersistencia = new PendenciaPs(obFabricaConexao.getConexao());
		
		for(RecursoSecundarioParteDt recursoSecundarioParte : listaRecursoSecundarioParte) {
			recursoSecundarioParte.setId_AudienciaProcesso(idAudienciaProcesso);
			obPersistencia.cadastrarRecursoSecundarioParte(recursoSecundarioParte);
		}
		
	}
	
	public void alterarStatus(PendenciaDt pendenciaDt) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaPs obPersistencia = new PendenciaPs(obFabricaConexao.getConexao());
			obPersistencia.AlterarStatusPendencia(pendenciaDt);
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	public void alterarStatus(PendenciaDt pendenciaDt, FabricaConexao obFabricaConexao) throws Exception {
		PendenciaPs obPersistencia = new PendenciaPs(obFabricaConexao.getConexao());
		obPersistencia.AlterarStatusPendencia(pendenciaDt);
	}

	public int consultarQuantidadeFinalizarVoto(String id_ServentiaCargo) throws Exception {
		FabricaConexao obFabricaConexao = null;
		int retorno = 0;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao()); 
			retorno = obPersistencia.consultarQuantidadeFinalizarVoto(id_ServentiaCargo, PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO);
			retorno += obPersistencia.consultarQuantidadeFinalizarVotoPrazoExpirado(id_ServentiaCargo); // jvosantos - 15/08/2019 15:03 - Buscar sess�es com prazo expirado
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}

	// jvosantos - 10/10/2019 14:18 - Corrigir contador
	public int consultarQuantidadeFinalizarVotoPreAnalisadas(String id_ServentiaCargo) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		int retorno = 0;
		try{
			PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao()); 
			retorno = obPersistencia.consultarQuantidadeFinalizarVotoPreAnalisado(id_ServentiaCargo);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}

	public int consultarQuantidadeFinalizarVotoAssinatura(String id_ServentiaCargo) throws Exception {
		FabricaConexao obFabricaConexao = null;
		int retorno = 0;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao()); 
			retorno = obPersistencia.consultarQuantidadeFinalizarVoto(id_ServentiaCargo, PendenciaStatusDt.ID_PRE_ANALISADA);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}
	
	public int consultarQuantidadeAguardandoLeituraPedidoVista(String id_ServentiaCargo, String idServentia) throws Exception {
		FabricaConexao obFabricaConexao = null;
		int retorno = 0;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao()); 
			retorno = obPersistencia.consultarQuantidadeAguardandoLeituraPedidoVista(id_ServentiaCargo, idServentia, PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}
	
	public List consultarQuantidadeSessaoVirtualTomarConhecimento(String id_ServentiaCargo, String idServentia, int ...codigos) throws Exception {
		List lista = new ArrayList<>();
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
			for(int codigo : codigos) {
				lista.add(obPersistencia.consultarQuantidadeSessaoVirtualPorTipo(id_ServentiaCargo, idServentia, codigo, PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO));
			}
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return lista;
	}
	
	public int consultarQuantidadeResultadoUnanime(String id_ServentiaCargo, String idServentia) throws Exception {
		FabricaConexao obFabricaConexao = null;
		int retorno = 0;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao()); 
			retorno = obPersistencia.consultarQuantidadeResultadoUnanime(id_ServentiaCargo, idServentia, PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}
	
	
	public int consultarQtdJulgamentoAdiadoAguardandoLeitura(String id_ServentiaCargo) throws Exception {
		FabricaConexao obFabricaConexao = null;
		int retorno = 0;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao()); 
			retorno = obPersistencia.consultarQtdJulgamentoAdiadoAguardandoLeitura(id_ServentiaCargo, PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}
	
	public int consultarQtdSustentacaoOral(String idUsuarioServentia) throws Exception {
		FabricaConexao obFabricaConexao = null;
		int retorno = 0;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao()); 
			retorno = obPersistencia.consultarQtdSustentacaoOral(idUsuarioServentia, PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}
	
	
    public PendenciaDt gerarPendenciaRetirarPauta(String idDestinatario, String idServentia, UsuarioDt usuario, String idProcesso, String idAudiProcOrigem, FabricaConexao fabrica) throws Exception {
		PendenciaDt pendenciaDt = new PendenciaDt();
		
		pendenciaDt.setId_UsuarioCadastrador(usuario.getId_UsuarioServentia());
		pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO));
		pendenciaDt.setId_Processo(idProcesso);
		
		
		pendenciaDt.setIpComputadorLog(usuario.getIpComputadorLog());
		pendenciaDt.setId_UsuarioLog(usuario.getId());
		
		pendenciaDt.setCodigoTemp(idAudiProcOrigem);
		
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
		pendenciaDt.setDataInicio(df.format(new Date()));
		
		PendenciaPs pendenciaPs = new  PendenciaPs(fabrica.getConexao());
		
		PendenciaTipoNe pendenciaTipoNe = new PendenciaTipoNe();
		PendenciaTipoDt pendenciaTipoDt = pendenciaTipoNe.consultarPendenciaTipoCodigo(PendenciaTipoDt.RETIRAR_PAUTA, fabrica);
		if (pendenciaTipoDt.getId() == null){
			throw new MensagemException("Tipo n�o encontrado");
		} else {
			pendenciaDt.setId_PendenciaTipo(pendenciaTipoDt.getId());
		}
		
		pendenciaPs.inserir(pendenciaDt);
		obDados.copiar(pendenciaDt);
		
		// Grava o log da operacao
		this.gravarLog(pendenciaDt, fabrica);
		
		// Inserir responsavel na pendencia
		PendenciaResponsavelDt pendenciaResponsavelDt = new PendenciaResponsavelDt();
		PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
		if(idDestinatario == null) {
			pendenciaResponsavelDt.setId_Serventia(idServentia);
			}
		else {
			pendenciaResponsavelDt.setId_ServentiaCargo(idDestinatario);
			
		}
		pendenciaResponsavelDt.setIpComputadorLog(usuario.getIpComputadorLog());
		pendenciaResponsavelDt.setId_UsuarioLog(usuario.getId());		
		pendenciaResponsavelDt.setId_Pendencia(pendenciaDt.getId());
		pendenciaResponsavelNe.inserir(pendenciaResponsavelDt, fabrica);
		
		// jvosantos - 18/12/2019 15:52 - Corre��o para salvar na AUDI_PROC_PEND apenas uma vez (remover daqui)
		
		return pendenciaDt;
	}

	public PendenciaDt gerarPendenciaAdiarJulgamento(String idServentia, String idServentiaCargo, UsuarioDt usuario, String idProcesso, FabricaConexao fabricaConexao, List listaArquivos) throws Exception {
		PendenciaDt pendenciaDt = new PendenciaDt();
		
		pendenciaDt.setId_UsuarioCadastrador(usuario.getId_UsuarioServentia());
		pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO));
		pendenciaDt.setId_Processo(idProcesso);
		
		
		pendenciaDt.setIpComputadorLog(usuario.getIpComputadorLog());
		pendenciaDt.setId_UsuarioLog(usuario.getId());
		
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
		pendenciaDt.setDataInicio(df.format(new Date()));
		
		PendenciaPs pendenciaPs = new  PendenciaPs(fabricaConexao.getConexao());
		
		PendenciaTipoNe pendenciaTipoNe = new PendenciaTipoNe();
		PendenciaTipoDt pendenciaTipoDt = pendenciaTipoNe.consultarPendenciaTipoCodigo(PendenciaTipoDt.ADIAR_JULGAMENTO, fabricaConexao);
		if (pendenciaTipoDt.getId() == null){
			throw new MensagemException("Tipo n�o encontrado");
		} else {
			pendenciaDt.setId_PendenciaTipo(pendenciaTipoDt.getId());
		}
		
		pendenciaPs.inserir(pendenciaDt);
		obDados.copiar(pendenciaDt);
		
		// Grava o log da operacao
		this.gravarLog(pendenciaDt, fabricaConexao);
		
		// Inserir responsavel na pendencia
		PendenciaResponsavelDt pendenciaResponsavelDt = new PendenciaResponsavelDt();
		PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
		pendenciaResponsavelDt.setId_Serventia(idServentia);
		pendenciaResponsavelDt.setId_ServentiaCargo(idServentiaCargo);
		pendenciaResponsavelDt.setIpComputadorLog(usuario.getIpComputadorLog());
		pendenciaResponsavelDt.setId_UsuarioLog(usuario.getId());				
		
		pendenciaResponsavelDt.setId_Pendencia(pendenciaDt.getId());
		pendenciaResponsavelNe.inserir(pendenciaResponsavelDt, fabricaConexao);
		
	
		if(listaArquivos != null) {
		inserirArquivos(pendenciaDt, listaArquivos, false, usuario, fabricaConexao);
		}
		return pendenciaDt;
	
	}

	public PendenciaDt gerarPendenciaAdiadoPeloRelator(String idServentia, String idServentiaCargo, UsuarioDt usuario, String idProcesso, FabricaConexao fabricaConexao, List listaArquivos) throws Exception {
		PendenciaDt pendenciaDt = new PendenciaDt();
		
		pendenciaDt.setId_UsuarioCadastrador(usuario.getId_UsuarioServentia());
		pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO));
		pendenciaDt.setId_Processo(idProcesso);		
		
		pendenciaDt.setIpComputadorLog(usuario.getIpComputadorLog());
		pendenciaDt.setId_UsuarioLog(usuario.getId());
		
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
		pendenciaDt.setDataInicio(df.format(new Date()));
		
		PendenciaPs pendenciaPs = new  PendenciaPs(fabricaConexao.getConexao());
		
		PendenciaTipoNe pendenciaTipoNe = new PendenciaTipoNe();
		PendenciaTipoDt pendenciaTipoDt = pendenciaTipoNe.consultarPendenciaTipoCodigo(PendenciaTipoDt.ADIADO_PELO_RELATOR);
		if (pendenciaTipoDt.getId() == null){
			throw new MensagemException("Tipo n�o encontrado");
		} else {
			pendenciaDt.setId_PendenciaTipo(pendenciaTipoDt.getId());
		}
		
		pendenciaPs.inserir(pendenciaDt);
		obDados.copiar(pendenciaDt);
		
		// Grava o log da operacao
		this.gravarLog(pendenciaDt, fabricaConexao);
		
		// Inserir responsavel na pendencia
		PendenciaResponsavelDt pendenciaResponsavelDt = new PendenciaResponsavelDt();
		PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
		pendenciaResponsavelDt.setId_Serventia(idServentia);
		pendenciaResponsavelDt.setId_ServentiaCargo(idServentiaCargo);
		pendenciaResponsavelDt.setIpComputadorLog(usuario.getIpComputadorLog());
		pendenciaResponsavelDt.setId_UsuarioLog(usuario.getId());				
		
		pendenciaResponsavelDt.setId_Pendencia(pendenciaDt.getId());
		pendenciaResponsavelNe.inserir(pendenciaResponsavelDt, fabricaConexao);
		
		if(listaArquivos != null) {
			PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
			pendenciaArquivoNe.vincularArquivos(pendenciaDt, listaArquivos, false, fabricaConexao);
		}
	
		return pendenciaDt;
	}
	
	public void finalizarPendenciasAdiarJulgamento(String idAudienciaProcesso, UsuarioDt usuario, FabricaConexao fabricaConexao) throws Exception {
		PendenciaPs pendenciaPs = new PendenciaPs(fabricaConexao.getConexao());
		List pendencias = pendenciaPs.consultarPendenciasAudienciaProcessoPorListaTipo(idAudienciaProcesso, PendenciaTipoDt.ADIAR_JULGAMENTO);
		String data = Funcoes.DataHora(Calendar.getInstance().getTime());
		for(int i = 0; i < pendencias.size(); i++) {
			PendenciaDt pendencia = (PendenciaDt) pendencias.get(i);
			pendencia.setDataFim(data);
			pendencia.setDataVisto(data);
			pendencia.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_CUMPRIDA));
			finalizar(pendencia, usuario, fabricaConexao);
		}
		
	}
	
	public List getListaAudienciaProcessoVotantes(dwrMovimentarProcesso dt, UsuarioDt usuario, FabricaConexao fabricaConexao) throws Exception {
		AudienciaProcessoVotantesDt audienciaProcessoVotantesDt = new AudienciaProcessoVotantesDt();
		VotanteTipoNe votanteTipoNe = new VotanteTipoNe();
		ImpedimentoTipoNe impedimentoTipoNe = new ImpedimentoTipoNe();
		ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
		HashMap<String, AudienciaProcessoVotantesDt> mapaAudienciaProcessoVotantesDt = new HashMap<>();
		
		audienciaProcessoVotantesDt.setId_ServentiaCargo(dt.getId_relator());
		audienciaProcessoVotantesDt.setRelator(true);
		audienciaProcessoVotantesDt.setId_ImpedimentoTipo(impedimentoTipoNe.consultarImpedimentoTipoCodigo(String.valueOf(ImpedimentoTipoDt.NAO_IMPEDIDO)).getId());
		audienciaProcessoVotantesDt.setOrdemVotante("0");		
		audienciaProcessoVotantesDt.setId_VotanteTipo(votanteTipoNe.consultarVotanteTipoCodigo(String.valueOf(VotanteTipoDt.RELATOR)).getId());		
				
		mapaAudienciaProcessoVotantesDt.put(audienciaProcessoVotantesDt.getId_ServentiaCargo(),audienciaProcessoVotantesDt);
		
		List votantes = dt.getListaVotantes();
		for (int i = 0; i < votantes.size(); i++) {				
			ServentiaCargoDt votante = (ServentiaCargoDt)votantes.get(i); 
			audienciaProcessoVotantesDt = new AudienciaProcessoVotantesDt();			
			audienciaProcessoVotantesDt.setId_ServentiaCargo(votante.getId());
			audienciaProcessoVotantesDt.setRelator(false);
//			if (dt.verificaImpedimento()) {
//				audienciaProcessoVotantesDt.setId_ImpedimentoTipo(impedimentoTipoNe.consultarImpedimentoTipoCodigo(String.valueOf(ImpedimentoTipoDt.AGUARDANDO_VERIFICACAO_VOTANTE)).getId());
//			}
//			else {
			audienciaProcessoVotantesDt.setId_ImpedimentoTipo(impedimentoTipoNe.consultarImpedimentoTipoCodigo(String.valueOf(ImpedimentoTipoDt.NAO_IMPEDIDO)).getId());					
//			}
			audienciaProcessoVotantesDt.setOrdemVotante(String.valueOf(i+1));
			audienciaProcessoVotantesDt.setId_VotanteTipo(votanteTipoNe.consultarVotanteTipoCodigo(String.valueOf(VotanteTipoDt.VOTANTE)).getId());
		
			mapaAudienciaProcessoVotantesDt.put(audienciaProcessoVotantesDt.getId_ServentiaCargo(),audienciaProcessoVotantesDt);
		}
		
		ServentiaCargoDt servCargoPresidenteCamara = serventiaCargoNe.getPresidenteSegundoGrau(usuario.getId_Serventia(), fabricaConexao);
		if(servCargoPresidenteCamara == null && !usuario.getId_Serventia().equals(String.valueOf(ServentiaDt.SERV_ORGAO_ESPECIAL))) {
			throw new MensagemException("N�o foi encontrado Presidente da C�mara", new NullPointerException());
		}
		if (servCargoPresidenteCamara != null && !mapaAudienciaProcessoVotantesDt.containsKey(servCargoPresidenteCamara.getId())){
			audienciaProcessoVotantesDt = new AudienciaProcessoVotantesDt();			
			audienciaProcessoVotantesDt.setId_ServentiaCargo(servCargoPresidenteCamara.getId());
			audienciaProcessoVotantesDt.setRelator(false);
			audienciaProcessoVotantesDt.setId_ImpedimentoTipo(impedimentoTipoNe.consultarImpedimentoTipoCodigo(String.valueOf(ImpedimentoTipoDt.NAO_IMPEDIDO)).getId());
			audienciaProcessoVotantesDt.setOrdemVotante(String.valueOf(99));
			audienciaProcessoVotantesDt.setId_VotanteTipo(votanteTipoNe.consultarVotanteTipoCodigo(String.valueOf(VotanteTipoDt.PRESIDENTE_CAMARA)).getId());
			mapaAudienciaProcessoVotantesDt.put(audienciaProcessoVotantesDt.getId_ServentiaCargo(),audienciaProcessoVotantesDt);
		}
		
		for (ServentiaCargoDt servCargoIntegranteCamara : serventiaCargoNe.consultarServentiaCargosDesembargadoresOrdemAntiguidade(usuario.getId_Serventia(), fabricaConexao)){
			if (!mapaAudienciaProcessoVotantesDt.containsKey(servCargoIntegranteCamara.getId())){
				audienciaProcessoVotantesDt = new AudienciaProcessoVotantesDt();			
				audienciaProcessoVotantesDt.setId_ServentiaCargo(servCargoIntegranteCamara.getId());
				audienciaProcessoVotantesDt.setRelator(false);
				audienciaProcessoVotantesDt.setId_ImpedimentoTipo(impedimentoTipoNe.consultarImpedimentoTipoCodigo(String.valueOf(ImpedimentoTipoDt.NAO_IMPEDIDO)).getId());
				audienciaProcessoVotantesDt.setOrdemVotante(String.valueOf(99));
				audienciaProcessoVotantesDt.setId_VotanteTipo(votanteTipoNe.consultarVotanteTipoCodigo(String.valueOf(VotanteTipoDt.INTEGRANTE_CAMARA)).getId());
				mapaAudienciaProcessoVotantesDt.put(audienciaProcessoVotantesDt.getId_ServentiaCargo(),audienciaProcessoVotantesDt);
			}
		}
		
		return new ArrayList(mapaAudienciaProcessoVotantesDt.values());
		
	}
	

	@Deprecated
	//TODO analisar a possibilidade de substituir idProcesso por idAudienciaProcesso
	public List consultarPendenciasProcessoPorTipo(String idProcesso, int pendenciaTipoCodigo, FabricaConexao conexao) throws Exception {
		PendenciaPs obPersistencia = new PendenciaPs(conexao.getConexao());
		return obPersistencia.consultarPendenciasProcessoPorTipo(idProcesso, pendenciaTipoCodigo);	
	}

	
	
	
	// mrbatista - 03/09/2019 11:00 - Adicionar fabrica na chamada, j� que temos uma instanciada
	// mrbatista - 27/09/2019 15:12 - Corre��o de NullPointer
	public List consultarPendenciasProcessoPorTipo(String idProcesso, int pendenciaTipoCodigo) throws Exception{
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {
			return consultarPendenciasProcessoPorTipo( idProcesso,  pendenciaTipoCodigo, obFabricaConexao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	
	@Deprecated
	//TODO analisar a possibilidade de substituir idProcesso por idAudienciaProcesso 
	public List consultarPendenciasProcessoResponsavelPorTipo(String idProcesso, String idServentiaCargo, int pendenciaTipoCodigo) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			return consultarPendenciasProcessoResponsavelPorTipo(idProcesso, idServentiaCargo, pendenciaTipoCodigo, obFabricaConexao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	@Deprecated
	//TODO analisar a possibilidade de substituir idProcesso por idAudienciaProcesso
	public List consultarPendenciasProcessoResponsavelPorTipo(String idProcesso, String idServentiaCargo, int pendenciaTipoCodigo,
			FabricaConexao obFabricaConexao) throws Exception {
		PendenciaPs obPersistencia = new PendenciaPs(obFabricaConexao.getConexao());
		return obPersistencia.consultarPendenciasProcessoResponsavelPorTipo(idProcesso, idServentiaCargo, pendenciaTipoCodigo);
	}
	
	// lrcampos 06/01/2020 16:28 - Substituir idProcesso por idAudienciaProcesso
	public List consultarPendenciasProcessoPorListaTipo(String idAudiProc, Integer ...pendenciaTipoCodigo) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			return consultarPendenciasProcessoPorListaTipo(idAudiProc, obFabricaConexao, pendenciaTipoCodigo);
		} finally {
			obFabricaConexao.fecharConexao();
		}		
	}

	// lrcampos 06/01/2020 16:28 - Substituir idProcesso por idAudienciaProcesso
	public List consultarPendenciasProcessoPorListaTipo(String idAudiProc, FabricaConexao obFabricaConexao, Integer... pendenciaTipoCodigo)
			throws Exception {
		PendenciaPs obPersistencia = new PendenciaPs(obFabricaConexao.getConexao());
		return obPersistencia.consultarPendenciasAudienciaProcessoPorListaTipo(idAudiProc, pendenciaTipoCodigo);
	}	

	public void retornarPendenciaParaPreAnalise(String id_Pendencia) throws Exception {
		PendenciaDt pendenciaDt = consultarId(id_Pendencia);
		pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_EM_ANDAMENTO));
		alterarStatus(pendenciaDt);
		
	}
	
	/**
	 * M�todo que gera o relat�rio de intima��es lidas no dia anterior.
	 * @return byte com a lista de intima��es
	 * @throws Exception
	 * @author hmgodinho
	 */
	public byte[] listarIntimacoesDiaAnterior() throws Exception {
		byte[] temp = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaPs obPersistencia = new PendenciaPs(obFabricaConexao.getConexao());
			
			List listaIntimacoes = obPersistencia.listarIntimacoesDiaAnterior();
			
			//se a listaIntimacoes estiver vazia, retornar null para gerar mensagem de erro para o usu�rio.
			if(listaIntimacoes == null || listaIntimacoes.isEmpty()) {
				return null; 
			} else {
				String separadorTxt = Relatorios.getSeparadorRelatorioTxt();
				String conteudoArquivo = "NUMERO_PROCESSO" + separadorTxt + "OAB_ADVOGADO" + separadorTxt + "DATA_EVENTO" + separadorTxt + "TIPO_EVENTO" + separadorTxt + "ATO_PROCESSUAL" + separadorTxt + "COMPLEMENTO_ATO_PROCESSUAL" + separadorTxt + "DATA_ATO_PROCESSUAL" + "\n";
				for (int i = 0; i < listaIntimacoes.size(); i++) {
					PendenciaDt obTemp = (PendenciaDt) listaIntimacoes.get(i);
					conteudoArquivo += obTemp.getProcessoNumero() + separadorTxt + obTemp.getCodigoTemp() + separadorTxt + obTemp.getDataInicio() + separadorTxt + obTemp.getPendenciaTipo() + separadorTxt + obTemp.getMovimentacao() + separadorTxt + obTemp.getComplemento() + separadorTxt + obTemp.getDataTemp() + "\n";
				}
				temp = conteudoArquivo.getBytes();
			}
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return temp;
	}
	
	// jvosantos - 04/06/2019 10:07 - Consulta de pendencia de apreciados
	public PendenciaDt consultarPendenciaApreciados(String idAudiProc, FabricaConexao fabrica) throws Exception {
		FabricaConexao obFabricaConexao = null;
		try {
			if(fabrica == null) obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = fabrica;
			
			PendenciaPs obPersistencia = new PendenciaPs(obFabricaConexao.getConexao());
			
			String id = obPersistencia.consultarPendenciaApreciados(idAudiProc);
			
			return obPersistencia.consultarId(id);
		} finally {
			if(fabrica == null) obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Consulta para verificar se um processo est� concluso
	 * 
	 * @param id_Processo, id do processo	
	 *  
	 * @return boolean
	 * @author lsbernardes
	 */
	public boolean processoConcluso(String idProcesso) throws Exception {
    	boolean processoConcluso = false;
   	 	FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            processoConcluso = obPersistencia.processoConcluso(idProcesso);
        } finally {
            obFabricaConexao.fecharConexao();
        }
        return processoConcluso;
   }
	/**
     * Consulta pendencias do tipo Intima��o ou Cita��o de um procurador/advogado/defensor da serventia do usu�rio logado
	 * 
	 * @author lsbernardes
	 * @param id_UsuarioServentia, identifica��o do usuario para verificar se existe pend�ncias
	 * @return int, quantidade de pend�ncias encontradas
	 * @throws Exception
	 */
    public int possuiIntimacoesCitacoesAbertasUsuario(String id_UsuarioServentia) throws Exception {
    	int retorno = 0;	
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            retorno = pendenciaPs.possuiIntimacoesCitacoesAbertasUsuario(id_UsuarioServentia);
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return retorno;
    }
    
    public List consultarIntimacoesCitacoesServentia(UsuarioNe usuarioNe) throws Exception {
        List pendencias = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            pendencias = pendenciaPs.consultarIntimacoesCitacoesServentia(usuarioNe);
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }
	
	// jvosantos - 10/07/2019 14:56 - Adicionar tipo de pendencia para Verificar Resultado da Vota��o (ap�s pendencia de renovar ou modificar voto ser finalizada)
	public void gerarPendenciaVerificarResultadoVotacao(String idDestinatario, UsuarioDt usuario,
			String idProcesso) throws Exception {
		FabricaConexao conexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

		conexao.iniciarTransacao();
		try {
			PendenciaDt pendenciaDt = new PendenciaDt();
			
			pendenciaDt.setId_UsuarioCadastrador(usuario.getId_UsuarioServentia());
			pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO));
			pendenciaDt.setId_Processo(idProcesso);			
			
			pendenciaDt.setIpComputadorLog(usuario.getIpComputadorLog());
			pendenciaDt.setId_UsuarioLog(usuario.getId());
			
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
			pendenciaDt.setDataInicio(df.format(new Date()));
			
			PendenciaPs pendenciaPs = new  PendenciaPs(conexao.getConexao());
			
			PendenciaTipoNe pendenciaTipoNe = new PendenciaTipoNe();
			PendenciaTipoDt pendenciaTipoDt = pendenciaTipoNe.consultarPendenciaTipoCodigo(PendenciaTipoDt.VERIFICAR_RESULTADO_VOTACAO);
			if (pendenciaTipoDt.getId() == null){
				throw new MensagemException("Tipo n�o encontrado");
			} else {
				pendenciaDt.setId_PendenciaTipo(pendenciaTipoDt.getId());
			}
			
			pendenciaPs.inserir(pendenciaDt);
			obDados.copiar(pendenciaDt);
			
			// Grava o log da operacao
			this.gravarLog(pendenciaDt, conexao);
			
			// Inserir responsavel na pendencia
			PendenciaResponsavelDt pendenciaResponsavelDt = new PendenciaResponsavelDt();
			PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
			pendenciaResponsavelDt.setId_ServentiaCargo(idDestinatario);
			pendenciaResponsavelDt.setId_Pendencia(pendenciaDt.getId());
			pendenciaResponsavelDt.setIpComputadorLog(usuario.getIpComputadorLog());
			pendenciaResponsavelDt.setId_UsuarioLog(usuario.getId());		
			pendenciaResponsavelNe.inserir(pendenciaResponsavelDt, conexao);
			
			conexao.finalizarTransacao();
		} catch (Exception e) {
			conexao.cancelarTransacao();
			throw e;
		} finally {
			conexao.fecharConexao();
		}		
	}

	// jvosantos - 11/07/2019 18:10 - Adicionar pend�ncia de "Verificar Resultado da Vota��o"
	public int consultarQuantidadeVerificarResultadoVotacao(String idServentiaCargo) throws Exception {
		FabricaConexao obFabricaConexao = null;
		int retorno = 0;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao()); 
			retorno = obPersistencia.consultarQuantidadeVerificarResultadoVotacao(idServentiaCargo, PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO);
			retorno += obPersistencia.consultarQuantidadeFinalizarVotoPrazoExpirado(idServentiaCargo); // jvosantos - 15/07/2019 16:49 - Buscar sess�es com prazo expirado
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}
	
	// jvosantos - 11/07/2019 18:10 - Adicionar pend�ncia de "Verificar Resultado da Vota��o"
	public int consultarQuantidadeVerificarResultadoVotacaoPreAnalisadas(String idServentiaCargo) throws Exception {
		FabricaConexao obFabricaConexao = null;
		int retorno = 0;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao()); 
			retorno = obPersistencia.consultarQuantidadeVerificarResultadoVotacao(idServentiaCargo, PendenciaStatusDt.ID_EM_ANDAMENTO);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}
	
	// jvosantos - 11/07/2019 18:10 - Adicionar pend�ncia de "Verificar Resultado da Vota��o"
	public int consultarQuantidadeVerificarResultadoVotacaoAssinatura(String idServentiaCargo) throws Exception {
		FabricaConexao obFabricaConexao = null;
		int retorno = 0;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao()); 
			retorno = obPersistencia.consultarQuantidadeVerificarResultadoVotacao(idServentiaCargo, PendenciaStatusDt.ID_PRE_ANALISADA);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}
	
	public List consultarPendenciasAudienciaProcessoPorListaTipo(String idAudienciaProcesso, Integer... pendenciaTipoCodigo) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			return consultarPendenciasAudienciaProcessoPorListaTipo(idAudienciaProcesso, obFabricaConexao, pendenciaTipoCodigo); // jvosantos - 30/09/2019 18:17 - Corre��o
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	public List consultarPendenciasAudienciaProcessoPorListaTipo(String idAudienciaProcesso, FabricaConexao obFabricaConexao, Integer... pendenciaTipoCodigo)
			throws Exception {
		return consultarPendenciasAudienciaProcessoPorListaTipo(idAudienciaProcesso, obFabricaConexao, null, pendenciaTipoCodigo);
	}
	
	public List consultarPendenciasAudienciaProcessoPorListaTipo(String idAudienciaProcesso, FabricaConexao obFabricaConexao, String idServentiaProcesso, Integer... pendenciaTipoCodigo)
			throws Exception {
		PendenciaPs obPersistencia = new PendenciaPs(obFabricaConexao.getConexao());
		return obPersistencia.consultarPendenciasAudienciaProcessoPorListaTipo(idAudienciaProcesso, idServentiaProcesso, null, pendenciaTipoCodigo);
	}
	
	//mrbatista - 11/12/2019 16:00 - implementando metodo que busque as pendencias finalizadas pelo idAudiProc.
	public List consultarPendenciasFinalizadasAudienciaProcessoPorListaTipo(String idAudienciaProcesso, FabricaConexao obFabricaConexao, String idServentiaProcesso, Integer... pendenciaTipoCodigo)
			throws Exception {
		PendenciaPs obPersistencia = new PendenciaPs(obFabricaConexao.getConexao());
		return obPersistencia.consultarPendenciasFinalizadasAudienciaProcessoPorListaTipo(idAudienciaProcesso, idServentiaProcesso, null, pendenciaTipoCodigo);
	}

	// jvosantos - 27/08/2019 14:21 - Criar m�todo para consultar as pend�ncias da audiencia processo filtrando por Serventia Respons�vel
	public List consultarPendenciasAudienciaProcessoPorListaTipo(String idAudienciaProcesso,
			FabricaConexao obFabricaConexao, String idServentiaProcesso, String idServentiaResponsavel, Integer... pendenciaTipoCodigo) throws Exception {
		PendenciaPs obPersistencia = new PendenciaPs(obFabricaConexao.getConexao());
		return obPersistencia.consultarPendenciasAudienciaProcessoPorListaTipo(idAudienciaProcesso, idServentiaProcesso, idServentiaResponsavel, pendenciaTipoCodigo);
	}
	
	public boolean temPendenciaAbertaVotoSessaoVirtual(String idServCargo, String idProcesso) throws Exception {
		FabricaConexao obFabricaConexao = null;
		try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);          
            PendenciaPs obPersistencia = new PendenciaPs(obFabricaConexao.getConexao());
            return obPersistencia.temPendenciaAbertaVotoSessaoVirtual(idServCargo, idProcesso);
        } finally {
            obFabricaConexao.fecharConexao();
        }
	}

	// jvosantos - 04/09/2019 17:01 - Implementar box de "Voto / Ementa - Aguardando Assinatura"
	public List consultarPendenciasVotoEmentaAguardandoAssinatura(UsuarioNe usuarioSessao) throws Exception {
		FabricaConexao obFabricaConexao = null;
		try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);          
            PendenciaPs obPersistencia = new PendenciaPs(obFabricaConexao.getConexao());
            return obPersistencia.consultarPendenciasVotoEmentaAguardandoAssinatura(usuarioSessao);
        } finally {
            obFabricaConexao.fecharConexao();
        }
	}
	
	//lrcampos - 25/11/2019 15:21 - Consulta Pendencia de para conhecimento abertas de uma audienciaProcesso.
	public List consultarPendenciasAudienciaProcessoParaConhecimento(String idAudienciaProcesso, FabricaConexao obFabricaConexao, Integer... pendenciaTipoCodigo)
			throws Exception {
		PendenciaPs obPersistencia = new PendenciaPs(obFabricaConexao.getConexao());
		return obPersistencia.consultarPendenciasAudienciaProcessoSessaoConhecimento(idAudienciaProcesso, pendenciaTipoCodigo);
		}
	

	public void setInfoPendenciaFinalizar(PendenciaDt pendencia, UsuarioDt usuario)
			throws Exception {
		FabricaConexao fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {
			fabrica.iniciarTransacao();
			setInfoPendenciaFinalizar(pendencia, usuario, fabrica);
			fabrica.finalizarTransacao();
		}catch (Exception e) {
			fabrica.cancelarTransacao();
		}finally {
			fabrica.fecharConexao();
		}
	}

	/**
	* Busca Lista de responsaveis da pendencia.
	* 
	* @author lrcampos
	* @param idPendencia,
	* @return List<PendenciaResponsavelDt>
	* @throws Exception 
	*/ 	
	public List consultaServCargoResponsavelPendencia(String idPendencia) {
		String responsavalPendencia = null;
		List listPendResp= new ArrayList();
		try {
			listPendResp = new PendenciaResponsavelNe().consultarResponsaveis(idPendencia);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return listPendResp;
	}

    /**
     * Verifica se o Usuario logado � o responsavel da pendencia.
     * caso o usuario n�o seja respons�vel retorna uma MensagemException
     * @author lrcampos
     * @param List<PendenciaDt>,
     * @param UsuarioDt,
     * @throws Exception 
     */ 
	public void verificaResponsavelPendencia(List listPendencias, UsuarioDt usuarioDt) throws Exception { 
		List collect = (List) Optional.ofNullable(listPendencias)
				.orElse(java.util.Collections.emptyList())
				.stream()
				.map(x -> ((PendenciaDt) x).getId())
				.collect(Collectors.toList());
		
		verificaResponsavelIdPendencia(collect, usuarioDt);
	}
	
	public void verificaResponsavelPendencia(String idPendencia, UsuarioDt usuarioDt) throws Exception {
		verificaResponsavelIdPendencia(Arrays.asList(idPendencia), usuarioDt);
	}

	public void verificaResponsavelIdPendencia(List pendencias, UsuarioDt usuarioDt) throws Exception {
		try {
			Optional.ofNullable(pendencias).orElse(java.util.Collections.emptyList()).stream()
					.map(idPend -> consultaServCargoResponsavelPendencia((String) idPend)).forEach(idsResps -> {
						List listPendResp = (List) Optional.ofNullable(idsResps)
								.orElse(java.util.Collections.emptyList());
						Optional pendRespIgualUsuLogado = listPendResp.stream().map(x -> ((PendenciaResponsavelDt)x).getId_ServentiaCargo())
								.filter(idResp -> idResp.equals(usuarioDt.getId_ServentiaCargo())).findFirst();
						if (!pendRespIgualUsuLogado.isPresent() && !listPendResp.isEmpty()) {
							try {
								String numeroProcesso = consultarId(((PendenciaResponsavelDt)(listPendResp.get(0))).getId_Pendencia())
										.getProcessoNumeroCompleto();
								throw new RuntimeException(new MensagemException(
										"O desembargador que deseja assinar a pend�ncia do processo " + numeroProcesso
												+ " n�o � o respons�vel pela mesma."));
							} catch (RuntimeException e) {
								throw new RuntimeException(new MensagemException(e.getCause().getMessage()));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
		} catch (RuntimeException e) {
			throw (MensagemException) e.getCause(); 
		}
	}

    /**
     * Cria uma pendencia para verificar o processo ap�s um ato do juiz
     * (decis�o, despacho, senten�a)
     * 
     * @author lsbernardes
     * @param id_processo,
     *             id do processo
     * @param id_UsuarioServentia,
     *            id do usuario cadastrador
     * @param id_ServentiaResponsavel,
     *            id da serventia responsavel
     * @param id_Movimentacao,
     *            movimenta��o vinculada a pend�ncia
     * @param arquivosVincular,
     *            arquivos a serem vinculados com pend�ncia
     * @param pendenciaPai,
     *            pend�ncia pai
     * @param logDt,
     *            objeto com dados do log
     * @param obFabricaConexao,
     *            fabrica de conexao para continar uma transa��cao existente
     * @return void
     */
    public void gerarPendenciaVerificarHomologacaoAudienciaCEJUSC(String id_processo, String id_UsuarioServentia, String id_ServentiaResponsavel, String id_Movimentacao, List arquivosVincular, PendenciaDt pendenciaPai, LogDt logDt, FabricaConexao obFabricaConexao, String processo_prioridade) throws Exception {
        this.gerarPendenciaProcesso(id_processo, arquivosVincular, PendenciaTipoDt.VERIFICAR_AUDIENCIAS_REALIZADAS_CEJUSC, id_Movimentacao, id_UsuarioServentia, "", "", "", PendenciaStatusDt.ID_EM_ANDAMENTO, "", id_ServentiaResponsavel, "", "", processo_prioridade, pendenciaPai, logDt, obFabricaConexao);

    }

	public int consultarQuantidadeConclusoesGabinete(String id_Serventia) throws Exception {
		FabricaConexao obFabricaConexao = null;
		try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);          
            PendenciaPs obPersistencia = new PendenciaPs(obFabricaConexao.getConexao());
            return obPersistencia.consultarQuantidadeConclusoesGabinete(id_Serventia);
        } finally {
            obFabricaConexao.fecharConexao();
        }
	}

	public List consultarListaConclusoesGabinete(String id_Serventia) throws Exception {
		FabricaConexao obFabricaConexao = null;
		try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);          
            PendenciaPs obPersistencia = new PendenciaPs(obFabricaConexao.getConexao());
            return obPersistencia.consultarListaConclusoesGabinete(id_Serventia);
        } finally {
            obFabricaConexao.fecharConexao();
        }
	}
	
	//lrcampos 25/11/2019 15:22 - finaliza todas as pendencia de para conhecimento de uma audiencia
	public void finalizarPendenciasParaConhecimentoSessaoVirtual(AudienciaDt audienciaDt, UsuarioDt usuarioDt, FabricaConexao obFabrica) throws Exception {
		List listAudiProc = new AudienciaProcessoNe().consultarAudienciaProcessos(audienciaDt.getId(), usuarioDt, true);
		VotoNe votoNe = new VotoNe();
		for (int i = 0; i < listAudiProc.size(); ++i) {
			AudienciaProcessoDt audienciaProcessoDt = (AudienciaProcessoDt) listAudiProc.get(i);
			votoNe.finalizarPendenciasConhecimentoVotacao(null, audienciaProcessoDt.getId(),  usuarioDt, obFabrica, PendenciaTipoDt.TIPOS_PENDENCIA_TOMAR_CONHECIMENTO );
		}
		
	}
	
	private AudienciaDt preparaAudienciaDt(ProcessoDt processoDt, AudienciaNe audienciaNe,  dwrMovimentarProcesso dt, UsuarioDt usuarioDt, LogDt logDt) throws Exception {
		// C�digo do tipo da audi�ncia
		AudienciaDt audienciaDt = new AudienciaDt();
		audienciaDt.setAudienciaTipoCodigo(dt.getCodTipoAudiencia());
		
		// Serventia do usu�rio logado
		audienciaDt.setId_Serventia(usuarioDt.getId_Serventia());
		audienciaDt.setServentia(usuarioDt.getServentia());
		
		if ((!usuarioDt.isAdvogado()) &&  audienciaDt.getAudienciaTipoCodigo() != null && 
		    audienciaDt.getAudienciaTipoCodigo().trim().length() > 0 && 
		    Funcoes.StringToInt(usuarioDt.getServentiaSubtipoCodigo()) != ServentiaSubtipoDt.PREPROCESSUAL &&
		    (Funcoes.StringToInt(audienciaDt.getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC.getCodigo() ||
		     Funcoes.StringToInt(audienciaDt.getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC_DPVAT.getCodigo() ||
		     Funcoes.StringToInt(audienciaDt.getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.MEDIACAO_CEJUSC.getCodigo())) {
			
			// Serventia do processo em caso de serventias pre-processuais (busca pauta na serventia relacionada)
			audienciaDt.setId_Serventia(processoDt.getId_Serventia());
			audienciaDt.setServentia(processoDt.getServentia());
			
			ServentiaDt serventiaDt = audienciaNe.consultarServentiaPreprocessualRelacionada(audienciaDt.getId_Serventia());
			
			if (serventiaDt != null) {
				audienciaDt.setId_Serventia(serventiaDt.getId());
				audienciaDt.setServentia(serventiaDt.getServentia());					
			} else {
				throw new MensagemException("N�o foi localizada uma serventia relacionada do tipo Preprocessual.");
			}
			
		} else if ((!usuarioDt.isAdvogado()) && audienciaDt.getId_AudienciaTipo() != null && 
				   audienciaDt.getId_AudienciaTipo().trim().length() > 0 && 
				   Funcoes.StringToInt(usuarioDt.getServentiaSubtipoCodigo()) != ServentiaSubtipoDt.PREPROCESSUAL) {
				
			AudienciaTipoDt audienciaTipoDt = audienciaNe.consultarIdAudienciaTipo(audienciaDt.getId_AudienciaTipo());
			if (audienciaTipoDt != null && 
			    (Funcoes.StringToInt(audienciaTipoDt.getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC.getCodigo() ||
			     Funcoes.StringToInt(audienciaTipoDt.getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC_DPVAT.getCodigo() ||
			     Funcoes.StringToInt(audienciaTipoDt.getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.MEDIACAO_CEJUSC.getCodigo())) {
				
				ServentiaDt serventiaDt = audienciaNe.consultarServentiaPreprocessualRelacionada(audienciaDt.getId_Serventia());
				
				if (serventiaDt != null) {
					audienciaDt.setId_Serventia(serventiaDt.getId());
					audienciaDt.setServentia(serventiaDt.getServentia());					
				} else {
					throw new MensagemException("N�o foi localizada uma serventia relacionada do tipo Preprocessual.");
				}
			} 
		}
		
		AudienciaProcessoDt audienciaProcessoDt = new AudienciaProcessoDt();
		audienciaProcessoDt.setProcessoDt(processoDt);
		audienciaProcessoDt.setId_UsuarioLog(logDt.getId_Usuario());
		audienciaProcessoDt.setIpComputadorLog(logDt.getIpComputadorLog());
		
		audienciaDt.addListaAudienciaProcessoDt(audienciaProcessoDt);
		audienciaDt.setId_UsuarioLog(logDt.getId_Usuario());
		audienciaDt.setIpComputadorLog(logDt.getIpComputadorLog());
		return audienciaDt;
	}
	
	/**
     *  Consulta pend�ncias em um processo e em uma determinada serventia, ou seja, qualquer pend�ncia em
     * aberto (at� mesmo as reservadas)
     * 
     * @param id_Processo, identifica��o do processo
     * @param id_Serventia, identifica��o da serventia
     * @param fabConexao, conex�o ativa
     * 
     * @author lsbernardes
     */
    public List consultarPendenciasProcessoRetornoAutomaticoCEJUSC(String id_Processo, String id_Serventia, FabricaConexao fabConexao) throws Exception {
    	List pendenciasAbertas =  null;
        FabricaConexao obFabricaConexao = null;
        try {
            if (fabConexao == null)
                obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            else
                obFabricaConexao = fabConexao;
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            pendenciasAbertas = obPersistencia.consultarPendenciasProcessoRetornoAutomaticoCEJUSC(id_Processo, id_Serventia);

        } finally {
            if (fabConexao == null) obFabricaConexao.fecharConexao();
        }

        return pendenciasAbertas;
    }
    
	/**
     *  Consulta pend�ncias em um processo e em uma determinada serventia, ou seja, qualquer pend�ncia em
     * aberto (at� mesmo as reservadas)
     * 
     * @param id_Processo, identifica��o do processo
     * @param id_Serventia, identifica��o da serventia
     * @param fabConexao, conex�o ativa
     * 
     * @author lsbernardes
     * @author hrrosa
     */
    public List consultarPendenciasProcessoArquivamentoAutomaticoExecucaoPenal(String id_Processo, String id_Serventia, FabricaConexao fabConexao) throws Exception {
    	List pendenciasAbertas =  null;
        FabricaConexao obFabricaConexao = null;
        try {
            if (fabConexao == null)
                obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            else
                obFabricaConexao = fabConexao;
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            pendenciasAbertas = obPersistencia.consultarPendenciasProcessoArquivamentoAutomaticoExecucaoPenal(id_Processo, id_Serventia);

        } finally {
            if (fabConexao == null) obFabricaConexao.fecharConexao();
        }

        return pendenciasAbertas;
    }
    
    /**
     *  Consulta pend�ncias em um processo e em uma determinada serventia, ou seja, qualquer pend�ncia em
     * aberto (at� mesmo as reservadas)
     * 
     * @param id_Processo, identifica��o do processo
     * @param id_Serventia, identifica��o da serventia
     * @param fabConexao, conex�o ativa
     * 
     * @author lsbernardes
     * @author hrrosa
     */
    public List consultarConclusoesProcessoArquivamentoAutomaticoExecucaoPenal(String id_Processo, String id_Serventia, FabricaConexao fabConexao) throws Exception {
    	List pendenciasAbertas =  null;
        FabricaConexao obFabricaConexao = null;
        try {
            if (fabConexao == null)
                obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            else
                obFabricaConexao = fabConexao;
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            pendenciasAbertas = obPersistencia.consultarConclusoesProcessoArquivamentoAutomaticoExecucaoPenal(id_Processo, id_Serventia);

        } finally {
            if (fabConexao == null) obFabricaConexao.fecharConexao();
        }

        return pendenciasAbertas;
    }
    
    /**
     *  Verifica se h� pend�ncias Aguardando visto em um processo em uma determinada serventia
     * 
     * @param id_Processo, identifica��o do processo
     * @param id_Serventia, identifica��o da serventia
     * @param fabConexao, conex�o ativa
     * 
     * @author lsbernardes
     */
    public List consultarPendenciasAguardandoVistoProcesso(String id_Processo, String id_Serventia, FabricaConexao fabConexao) throws Exception {
    	List pendenciasAbertas =  null;
        FabricaConexao obFabricaConexao = null;
        try {
            if (fabConexao == null)
                obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            else
                obFabricaConexao = fabConexao;
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            pendenciasAbertas = obPersistencia.consultarPendenciasAguardandoVistoProcesso(id_Processo, id_Serventia);

        } finally {
            if (fabConexao == null) obFabricaConexao.fecharConexao();
        }

        return pendenciasAbertas;
    }
	

	/**
	 * Metodo para fechar pend�ncia - Funcioanlidade de retorno autom�tico do CEJUSC
	 * 
	 * @author lsbernardes
	 * @param String
	 *             id_Pendencia, identificador da pendencia
	 * @throws Exception
	 */
    private void fecharPendenciasAutomaticamenteCEJUSC(String id_Pendencia, String nomeTabelaLog, FabricaConexao obFabrica ) throws Exception {
               
        String valorNovo = "[Id_Pendencia:" + id_Pendencia + "; Id_UsuarioFinalizador:" + UsuarioServentiaDt.SistemaProjudi + "; Data_Fim:" + Funcoes.DataHora(new Date()) + "]";
        LogDt obLogDt = new LogDt(nomeTabelaLog, id_Pendencia, UsuarioDt.SistemaProjudi, "Servidor", String.valueOf(LogTipoDt.FechamentoAutomaticoPendencia), "", valorNovo);
        
        PendenciaPs obPersistencia = new  PendenciaPs(obFabrica.getConexao());
        obPersistencia.fecharPendenciaAutomaticamente(id_Pendencia);
       
        obLog.salvar(obLogDt, obFabrica);
        obPersistencia.moverPendencia(id_Pendencia);
            
    }
    
    /**
	 * Metodo para fechar pend�ncia - Funcioanlidade de retorno autom�tico do CEJUSC
	 * 
	 * @author lsbernardes
	 * @author hrrosa
	 * @param String
	 *             id_Pendencia, identificador da pendencia
	 * @throws Exception
	 */
    private void fecharPendenciasAutomaticamenteArquivamentoExecucaoAutomatica(String id_Pendencia, String nomeTabelaLog, FabricaConexao obFabrica ) throws Exception {
               
        String valorNovo = "[Id_Pendencia:" + id_Pendencia + "; Id_UsuarioFinalizador:" + UsuarioServentiaDt.SistemaProjudi + "; Data_Fim:" + Funcoes.DataHora(new Date()) + "]";
        LogDt obLogDt = new LogDt(nomeTabelaLog, id_Pendencia, UsuarioDt.SistemaProjudi, "Servidor", String.valueOf(LogTipoDt.FechamentoAutomaticoPendencia), "", valorNovo);
        
        PendenciaPs obPersistencia = new  PendenciaPs(obFabrica.getConexao());
        obPersistencia.fecharPendenciaAutomaticamente(id_Pendencia);
       
        obLog.salvar(obLogDt, obFabrica);
        obPersistencia.moverPendencia(id_Pendencia);
            
    }
    
    /**
	 * Metodo para vistar pend�ncia - Funcioanlidade de retorno autom�tico do CEJUSC
	 * 
	 * @author lsbernardes
	 * @param String
	 *             id_Pendencia, identificador da pendencia
	 * @throws Exception
	 */
    private void vistandoPendenciasAutomaticamenteCEJUSC(String id_Pendencia, String nomeTabelaLog, FabricaConexao obFabrica ) throws Exception {        
        	
        String valorNovo = "[Id_Pendencia:" + id_Pendencia + "; Id_UsuarioFinalizador:" + UsuarioServentiaDt.SistemaProjudi + "; Data_Visto:" + Funcoes.DataHora(new Date()) + "]";
        LogDt obLogDt = new LogDt(nomeTabelaLog, id_Pendencia, UsuarioDt.SistemaProjudi, "Servidor", String.valueOf(LogTipoDt.FechamentoAutomaticoPendencia), "", valorNovo);
        
        PendenciaPs obPersistencia = new  PendenciaPs(obFabrica.getConexao());
        obPersistencia.vistarPendenciaAutomaticamente(id_Pendencia);
       
        obLog.salvar(obLogDt, obFabrica);
        obPersistencia.moverPendencia(id_Pendencia);
            
    }
	
    /**
	 * Metodo para vistar pend�ncia - Funcioanlidade de retorno autom�tico do CEJUSC
	 * 
	 * @author lsbernardes
	 * @param String
	 *             id_Pendencia, identificador da pendencia
	 * @throws Exception
	 */
    private void vistandoPendenciasAutomaticamenteArquivamentoExecucaoAutomatica(String id_Pendencia, String nomeTabelaLog, FabricaConexao obFabrica ) throws Exception {        
        	
        String valorNovo = "[Id_Pendencia:" + id_Pendencia + "; Id_UsuarioFinalizador:" + UsuarioServentiaDt.SistemaProjudi + "; Data_Visto:" + Funcoes.DataHora(new Date()) + "]";
        LogDt obLogDt = new LogDt(nomeTabelaLog, id_Pendencia, UsuarioDt.SistemaProjudi, "Servidor", String.valueOf(LogTipoDt.FechamentoAutomaticoPendencia), "", valorNovo);
        
        PendenciaPs obPersistencia = new  PendenciaPs(obFabrica.getConexao());
        obPersistencia.vistarPendenciaAutomaticamente(id_Pendencia);
       
        obLog.salvar(obLogDt, obFabrica);
        obPersistencia.moverPendencia(id_Pendencia);
            
    }

    /**
	 * Metodo para fechar pend�ncia - Funcioanlidade de retorno autom�tico do CEJUSC
	 * 
	 * @author lsbernardes
	 * @param ProcessoDt
	 *            processoDt, Dt do processo que ter� as pend�ncias finalizadas
	 * @throws Exception
	 */
	public void fecharPendenciasRetornoAutomaticoCEJUSC(ProcessoDt processoDt, String nomeTabelaLog, FabricaConexao obFabricaConexao)  throws Exception {
		// SE N�O H� PEND�NCIAS EM ABERTO PARA A SERVENTIA
		PendenciaNe pendenciaNe = new PendenciaNe();
		List pendenciasAbertas = pendenciaNe.consultarPendenciasProcessoRetornoAutomaticoCEJUSC(processoDt.getId(), processoDt.getId_Serventia(), obFabricaConexao);
		if (pendenciasAbertas != null) {
			for (Iterator iterator = pendenciasAbertas.iterator(); iterator.hasNext();) {
				String[] pendencias = (String[]) iterator.next();
				this.fecharPendenciasAutomaticamenteCEJUSC(pendencias[0], nomeTabelaLog, obFabricaConexao);
			}
		}
		
		List pendenciasAguardandoVisto = pendenciaNe.consultarPendenciasAguardandoVistoProcesso(processoDt.getId(), processoDt.getId_Serventia(), obFabricaConexao);
		if (pendenciasAguardandoVisto != null) {
			for (Iterator iterator = pendenciasAguardandoVisto.iterator(); iterator.hasNext();) {
				String[] pendencias = (String[]) iterator.next();
				this.vistandoPendenciasAutomaticamenteCEJUSC(pendencias[0], nomeTabelaLog, obFabricaConexao);
			}
		}
	}
	
	
    /**
	 * Metodo para fechar pend�ncia - Funcioanlidade de retorno autom�tico do CEJUSC
	 * 
	 * @author lsbernardes
	 * @author hrrosa
	 * @param ProcessoDt
	 *            processoDt, Dt do processo que ter� as pend�ncias finalizadas
	 * @throws Exception
	 */
	public void fecharPendenciasArquivamentoAutomaticoExecucaoPenal(ProcessoDt processoDt, String nomeTabelaLog, FabricaConexao obFabricaConexao)  throws Exception {
		// SE N�O H� PEND�NCIAS EM ABERTO PARA A SERVENTIA
		PendenciaNe pendenciaNe = new PendenciaNe();
		List pendenciasAbertas = pendenciaNe.consultarPendenciasProcessoArquivamentoAutomaticoExecucaoPenal(processoDt.getId(), processoDt.getId_Serventia(), obFabricaConexao);
		if (pendenciasAbertas != null) {
			for (Iterator iterator = pendenciasAbertas.iterator(); iterator.hasNext();) {
				String[] pendencias = (String[]) iterator.next();
				this.fecharPendenciasAutomaticamenteArquivamentoExecucaoAutomatica(pendencias[0], nomeTabelaLog, obFabricaConexao);
			}
		}
		
		//VERIFICAR SE � PRA FINALIZAR AS CONCLUS�ES
//		List conclusoesAbertas = pendenciaNe.consultarConclusoesProcessoArquivamentoAutomaticoExecucaoPenal(processoDt.getId(), processoDt.getId_Serventia(), obFabricaConexao);
//		if (conclusoesAbertas != null) {
//			for (Iterator iterator = conclusoesAbertas.iterator(); iterator.hasNext();) {
//				String[] pendencias = (String[]) iterator.next();
//				this.fecharPendenciasAutomaticamenteArquivamentoExecucaoAutomatica(pendencias[0], nomeTabelaLog, obFabricaConexao);
//			}
//		}
		
		List pendenciasAguardandoVisto = pendenciaNe.consultarPendenciasAguardandoVistoProcesso(processoDt.getId(), processoDt.getId_Serventia(), obFabricaConexao);
		if (pendenciasAguardandoVisto != null) {
			for (Iterator iterator = pendenciasAguardandoVisto.iterator(); iterator.hasNext();) {
				String[] pendencias = (String[]) iterator.next();
				this.vistandoPendenciasAutomaticamenteArquivamentoExecucaoAutomatica(pendencias[0], nomeTabelaLog, obFabricaConexao);
			}
		}
	}
	
	 /**
     * Alterar tipo da pend�ncia
     * 
     * @author lsbernardes
     * @param pendenciaDt
     *            objeto de pendencias
     * @param novoCodPendenciaTipo
	 *            novo tipo de pend�ncia 
     * @param fabrica
     *            fabrica para continuar uma trasacao
     * @throws Exception
     */
    public void alterarTipoPendencia(PendenciaDt pendenciaDt, String novoCodPendenciaTipo, FabricaConexao fabrica) throws Exception {
        FabricaConexao obFabricaConexao = null;
        try {
            if (fabrica == null) {
                obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
                obFabricaConexao.iniciarTransacao();
            } else
                obFabricaConexao = fabrica;

            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

            LogDt obLogDt = new LogDt("Pendencia", pendenciaDt.getId(), pendenciaDt.getId_UsuarioLog(), pendenciaDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), "Pendencia Tipo Codigo: " + pendenciaDt.getPendenciaTipoCodigo(),  "Novo Pendencia Tipo Codigo: " + novoCodPendenciaTipo);
            obPersistencia.alterarTipoPendencia(pendenciaDt, novoCodPendenciaTipo);
            obLog.salvar(obLogDt, obFabricaConexao);
            
            if (fabrica == null) obFabricaConexao.finalizarTransacao();
        } catch (Exception e) {
            if (fabrica == null) obFabricaConexao.cancelarTransacao();
            throw e;
        } finally {
            if (fabrica == null) obFabricaConexao.fecharConexao();
        }
    }

	public String getIdPendencia() {
		if (obDados != null) {
			return obDados.getId();
		}
		return null;
	}
	
	/**
	 * Alterar o status da pend�ncia de Aguardando envio Correio para Aguardando Confirma��o Correio, N�o gera movimenta��o no processo
	 * 
	 * @author lsbernardes
	 * @param id_Pendencia
	 *            identificador da pendencia
	 *   
	 * @throws Exception
	 */
	public void alterarStatusAguardandoConfirmacaoCorreios(String id_Pendencia) throws Exception {
        FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
        try {
            obFabricaConexao.iniciarTransacao();
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            LogDt obLogDt = new LogDt("Pendencia", id_Pendencia, UsuarioDt.SistemaProjudi, "Servidor", String.valueOf(LogTipoDt.Alterar), "Pendencia Status Anterior: " + PendenciaStatusDt.ID_AGUARDANDO_ENVIO_CORREIOS + " Pendencia Status Novo: " + PendenciaStatusDt.ID_AGUARDANDO_CONFIRMACAO_CORREIOS);
            obPersistencia.alterarStatusAguardandoConfirmacaoCorreios(id_Pendencia);
            obLog.salvar(obLogDt, obFabricaConexao);
            obFabricaConexao.finalizarTransacao();
        } catch (Exception e) {
            obFabricaConexao.cancelarTransacao();
        } finally {
            obFabricaConexao.fecharConexao();
        }
	}

	// jvosantos - 08/01/2020 17:29 - M�todo para buscar a quantida de pend�ncias de Erro Material
	public long consultarQtdErroMaterial(boolean analisado, UsuarioNe usuario) throws Exception {
		return consultarQtdErroMaterial(analisado, false, usuario);
	}

	// jvosantos - 08/01/2020 17:29 - M�todo para buscar a quantida de pend�ncias de Erro Material
	public long consultarQtdErroMaterial(boolean analisado, boolean aguardandoAssinatura, UsuarioNe usuario) throws Exception {
		String idServentiaCargo = Funcoes.getIdServentiaCargo(usuario.getUsuarioDt());
		
		return consultarQtdErroMaterial(analisado, aguardandoAssinatura, idServentiaCargo);
	}

	// jvosantos - 08/01/2020 17:29 - M�todo para buscar a quantida de pend�ncias de Erro Material
	public long consultarQtdErroMaterial(boolean analisado, boolean aguardandoAssinatura, String idServentiaCargo) throws Exception {
		if(!analisado && aguardandoAssinatura) throw new Exception("Estado inv�lido.");

		FabricaConexao fabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		
		try {
			PendenciaPs pendenciaPs = new PendenciaPs(fabricaConexao.getConexao());

			return pendenciaPs.consultarQtdErroMaterial(analisado, aguardandoAssinatura, idServentiaCargo);
		} finally {
			fabricaConexao.fecharConexao();
		}
	}
	
	 /**
     *  Efetuar a confirma��o de recebimendo do Correio, alterando o Status de Aguardando Confirma��o Correio para Recebido Correio, Gera movimenta��o no processo
     * 
     * @author lsbernardes
     * @param pendenciaDt
     *            vo de pendencia
     * @param usuarioDt
     *            vo de usuario
     * @param arquivos
     *            Colecao de arquivos a serem inseridos
     * @param LogDt
     *            Objeto com informa��es do log
     * @param fabrica
     *            fabrica de conexao para poder continuar uma transacao
     * @throws Exception
     */
    public void efetuarConfirmacaoCorreios(PendenciaDt pendenciaDt, UsuarioDt usuarioDt, LogDt obLogDt) throws Exception {
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            obFabricaConexao.iniciarTransacao();

            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            
            // Somente em casos de pendencia de processo
            if (pendenciaDt.isPendenciaDeProcesso()) {
                // Cria um objeto de regras
                MovimentacaoNe movimentacaoNe = new MovimentacaoNe();

                // Configura objeto de processo
                ProcessoDt processoDt = new ProcessoDt();
                processoDt.setId_Processo(pendenciaDt.getId_Processo());
                processoDt.setProcessoNumero(pendenciaDt.getProcessoNumero());

                // preenche complemento para intima��o e cita��o
                String complemento = " - C�digo de Rastreamento Correios: " + pendenciaDt.getComplemento() + " idPendenciaCorreios" + pendenciaDt.getIdPendenciaCorreios() + "idPendenciaCorreios";
                String comp = "";
                if (complemento != null && !complemento.equals("")) {
                	String responsavel = "";
                	String stComplemento = "";
                	 
                		 responsavel = pendenciaDt.getNomeParte();
                		 stComplemento = "Para " + obtenhaComplementoLeituraPendenciaIntimacao(pendenciaDt) + " "  + responsavel;
                		 if (stComplemento.length()>255)	{            	
         		           	if (responsavel.length() > stComplemento.length()-255) {
         		           		stComplemento = "Para " + obtenhaComplementoLeituraPendenciaIntimacao(pendenciaDt) + " " + responsavel.substring(0,(responsavel.length() -  (stComplemento.length()-255)));
         		           	}
         		         }
    		         comp = stComplemento + complemento;
                }
                
                int GrupoCodigo = Funcoes.StringToInt(usuarioDt.getGrupoCodigo());
                switch (pendenciaDt.getPendenciaTipoCodigoToInt()) {
	                case PendenciaTipoDt.INTIMACAO:{
                        // cartorio expede intimacao para parte (off-line)
                        List arqs = this.consultarArquivosRespostaAssinados(pendenciaDt, true, obFabricaConexao);
                        movimentacaoNe.gerarMovimentacaoIntimacaoExpedida(processoDt.getId(), processoDt.getProcessoNumero(), arqs, usuarioDt, comp, obLogDt, obFabricaConexao);
	                break;
	                }
	                case PendenciaTipoDt.CARTA_CITACAO:{
                        // cartorio expede carta de citacao para parte (off-line)
                        List arqs = this.consultarArquivosRespostaAssinados(pendenciaDt, true, obFabricaConexao);
                        movimentacaoNe.gerarMovimentacaoCitacaoExpedida(processoDt.getId(), processoDt.getProcessoNumero(), arqs, usuarioDt, comp, obLogDt, obFabricaConexao);
	                 break;
	                }
                }
            }
           
           obPersistencia.alterarStatusRecebidoCorreios(pendenciaDt.getId());
           
           LogDt logDt = new LogDt("Pendencia", pendenciaDt.getId(), UsuarioDt.SistemaProjudi, "Servidor", String.valueOf(LogTipoDt.Alterar), "Pendencia Status Anterior: " + PendenciaStatusDt.ID_AGUARDANDO_CONFIRMACAO_CORREIOS + " Pendencia Status Novo: " + PendenciaStatusDt.ID_RECEBIDO_CORREIOS);
           obLog.salvar(logDt, obFabricaConexao);

           obFabricaConexao.finalizarTransacao();
        } catch (Exception e) {
        	obFabricaConexao.cancelarTransacao();
        } finally {
        	obFabricaConexao.fecharConexao();
        }
    }
    
	/**
	 * Finaliza a pend�ncia com o retorno final do correio, gera movimenta��o
	 * 
	 * @author lsbernardes
	 * @param pendenciaDt vo para pendencias
	 * @param usuarioDt   usuario
	 * @param arquivos    vinculado a pend�ncia
	 * @param fabrica     fabrica de conexao para poder continuar uma transacao
	 * @throws Exception
	 */
	public void concluirPendenciaCorreios(PendenciaDt pendenciaDt, UsuarioDt usuarioDt, List arquivos) throws Exception {

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();

			// Se tem arquivos
			if (arquivos != null && arquivos.size() > 0) {
				this.inserirArquivos(pendenciaDt, arquivos, true, usuarioDt, obFabricaConexao);
			}

			PendenciaPs obPersistencia = new PendenciaPs(obFabricaConexao.getConexao());

			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			pendenciaDt.setDataFim(df.format(new Date()));

			if (Funcoes.StringToInt(pendenciaDt.getPrazo()) > 0) {

				obPersistencia.retirarVisto(pendenciaDt);
				// Limpa dados do objeto
				pendenciaDt.setDataVisto("");
				this.concluirPendenciaCorreios(pendenciaDt, usuarioDt, Funcoes.StringToInt(pendenciaDt.getId_PendenciaStatus()), null, null, obFabricaConexao);
				PrazoSuspensoNe prazoSuspensoNe = new PrazoSuspensoNe();
				ServentiaNe serventiaNe = new ServentiaNe();
				ProcessoNe processoNe = new ProcessoNe();

				ProcessoDt processoDt = processoNe.consultarId(pendenciaDt.getId_Processo());
				ServentiaDt serventiaDt = serventiaNe.consultarId(processoDt.getId_Serventia());

				if (this.isUtilizaPrazoCorrido(pendenciaDt.getId_Processo(), obFabricaConexao)) {
					pendenciaDt.setDataLimite(prazoSuspensoNe.getProximaDataValidaPrazoCorrido(new Date(), Funcoes.StringToInt(pendenciaDt.getPrazo()), serventiaDt, obFabricaConexao) + " 00:00:00");
				} else if (this.isProcessoVaraPrecatoria(pendenciaDt.getId_Processo(), obFabricaConexao)) {
					// Em caso de vara de precatoria deve-se testar o processo que originou a precatoria
					if (this.isUtilizaPrazoCorrido(processoDt.getId_ProcessoPrincipal(), obFabricaConexao))	pendenciaDt.setDataLimite(prazoSuspensoNe.getProximaDataValidaPrazoCorrido(new Date(), Funcoes.StringToInt(pendenciaDt.getPrazo()), serventiaDt, obFabricaConexao) + " 00:00:00");
					else
						pendenciaDt.setDataLimite(prazoSuspensoNe.getProximaDataValidaNovoCPC(new Date(), Funcoes.StringToInt(pendenciaDt.getPrazo()), serventiaDt, obFabricaConexao) + " 00:00:00");

				} else
					pendenciaDt.setDataLimite(prazoSuspensoNe.getProximaDataValidaNovoCPC(Funcoes.DataHora(pendenciaDt.getDataFim()),	Funcoes.StringToInt(pendenciaDt.getPrazo()), serventiaDt, obFabricaConexao)	+ " 00:00:00");

				obPersistencia.alterarLimite(pendenciaDt);

			} else {
				// Status de retorno da pend�ncia (Efetivada, N�o Efetivada, Cancelada) deve ser setado no campo "statusPendenciaRetorno" do PendenciaDt verificar o metodo getPendenciaStatusCodigo no Pendenciane
				if (pendenciaDt.getId_PendenciaStatus() == null) {
					throw new MensagemException("O Status de Retorno da Pend�ncia deve ser informado!");
				} else {
					this.concluirPendenciaCorreios(pendenciaDt, usuarioDt, Funcoes.StringToInt(pendenciaDt.getId_PendenciaStatus()), null, null, obFabricaConexao);
				}
			}

			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	public List consultarPendenciasCorreiosCompleta(String idPendencia, String pendenciaStatus, int offset, String numeroRegistros, boolean semMetaDados) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {
			return consultarPendenciasCorreiosCompleta(idPendencia, pendenciaStatus, offset, numeroRegistros, semMetaDados, obFabricaConexao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	public List consultarPendenciasCorreiosCompleta(String idPendencia, String pendenciaStatus, int offset, String numeroRegistros, boolean semMetaDados, FabricaConexao obFabricaConexao) throws Exception {
		List<CorreiosDt> tempList= null;
		PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
		GrupoNe grupoNe = new GrupoNe();
		MovimentacaoArquivoNe movimentacaoArquivoNe = new MovimentacaoArquivoNe();
		tempList = obPersistencia.consultarPendenciasCorreiosCompleta(idPendencia, pendenciaStatus, offset, numeroRegistros, semMetaDados);
		for (CorreiosDt correiosDt : tempList) {
			correiosDt.setCargoCadastrador(grupoNe.consultarGruposUsuarioServentia(correiosDt.getId_UsuarioServentiaCadastrador()));
			if(correiosDt.getId_Movimentacao() != null && correiosDt.getId_Movimentacao().length() > 0) {
				List idArquivo = movimentacaoArquivoNe.consultarArquivoMovimentacao(correiosDt.getId_Movimentacao());
				correiosDt.setId_Arquivo(idArquivo);
			}
		}
		return tempList;
	}

	public void finalizarPendenciaTomarConhecimento(String idPendencia, UsuarioDt usuario, FabricaConexao fabrica) throws Exception {
			PendenciaDt pendenciaDt = new PendenciaNe().consultarId(idPendencia, fabrica);
			setInfoPendenciaFinalizar(pendenciaDt, usuario, fabrica);
			
	}
		
	public void setInfoPendenciaFinalizar(PendenciaDt pendencia, UsuarioDt usuario, FabricaConexao fabrica) throws Exception {
		setInfoPendenciaFinalizar(pendencia, usuario, PendenciaStatusDt.ID_CUMPRIDA, fabrica);
	}
	
	public void setInfoPendenciaFinalizar(PendenciaDt pendencia, UsuarioDt usuario, int statusCodigo, FabricaConexao fabrica) throws Exception {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		String data = formatter.format(LocalDateTime.now());
		pendencia.setDataFim(data);
		pendencia.setDataVisto(data);
		if (Funcoes.StringToInt(pendencia.getPendenciaStatusCodigo()) != PendenciaStatusDt.ID_NAO_CUMPRIDA) {
			pendencia.setPendenciaStatusCodigo(String.valueOf(statusCodigo));
		}
			finalizar(pendencia, usuario, fabrica);
	}
	
	public void finalizarPendenciaTomarConhecimento(String idPendencia, UsuarioDt usuario) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			finalizarPendenciaTomarConhecimento(idPendencia, usuario, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	// mrbatista - 02/10/2019 14:42 - Refatorar m�todo
	public void gerarPendenciaSustentacaoOralIndeferida(String idPendencia, UsuarioDt usuario) throws Exception {
			AudienciaProcessoPendenciaNe audienciaProcessoPendenciaNe = new AudienciaProcessoPendenciaNe();
			PendenciaDt pendenciaDt = new PendenciaDt();
			ProcessoDt processoDt = new ProcessoDt();
			SustentacaoOralNe sustentacaoOralNe = new SustentacaoOralNe();
			PendenciaDt pendenciaIndeferida;
			PendenciaDt pendencia;
			List<SustentacaoOralDt> listaPedidosSustentacaoOral = new ArrayList<SustentacaoOralDt>();

			FabricaConexao obFabricaConexao = null;
			
			try {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				obFabricaConexao.iniciarTransacao();
				
				SustentacaoOralDt sustentacaoOralDt = sustentacaoOralNe.consultarSustentacaoOralAbertaIdPendencia(idPendencia, obFabricaConexao);
				listaPedidosSustentacaoOral = sustentacaoOralNe.consultarSustentacaoOralIdAudiProc(sustentacaoOralDt.getAudienciaProcessoDt().getId(), obFabricaConexao);
				if (listaPedidosSustentacaoOral != null && listaPedidosSustentacaoOral.size() > 0){
					for (SustentacaoOralDt susOralDt : listaPedidosSustentacaoOral) {	
						pendenciaDt = consultarId(susOralDt.getPendenciaDt().getId());
						processoDt = new ProcessoNe().consultarId(pendenciaDt.getId_Processo());
						
						pendenciaIndeferida = gerarPendenciaPedidoSustentacaoOralIndeferida(pendenciaDt.getId_UsuarioCadastrador(), null, usuario, pendenciaDt.getId_Processo(), obFabricaConexao);
						//mrbatista 02/10/2019 14:41  - gravando audi_proc_pend na gera��o do pedido de S.O., para que a pend�ncia possa ser rastreada .
						if(pendenciaIndeferida != null)
							audienciaProcessoPendenciaNe.salvar(pendenciaIndeferida.getId(), susOralDt.getAudienciaProcessoDt().getId(), obFabricaConexao);
				
				
						pendencia = gerarPendenciaPedidoSustentacaoOralIndeferida(null, processoDt.getId_Serventia(), usuario, pendenciaDt.getId_Processo(), obFabricaConexao);
						
						//mrbatista 02/10/2019 14:41 - gravando audi_proc_pend na gera��o do pedido de S.O., para que a pend�ncia possa ser rastreada .
						if(pendencia != null)
							audienciaProcessoPendenciaNe.salvar(pendencia.getId(), susOralDt.getAudienciaProcessoDt().getId());
	
						finalizarPendenciaTomarConhecimento(pendenciaDt.getId(), usuario, obFabricaConexao);
						susOralDt.setPendenciaDt(pendenciaIndeferida);
						sustentacaoOralNe.alterarStatusSustentacaoOral(susOralDt, obFabricaConexao);
					}
				}
				obFabricaConexao.finalizarTransacao();
			} catch (Exception e) {
				obFabricaConexao.cancelarTransacao();
				throw e;
			}finally {
				obFabricaConexao.fecharConexao();
		}
	}
	
	/**
     * Cria uma pendencia para verificar documento
     * 
     * @author asrocha
     * @param processoDt,
     *            bean do processo
     * @param String
     *            id_UsuarioServentia, id do usuario cadastrador
     * @param id_Movimentacao,
     *            id da movimenta��o vinculada
     * @param String
     *            id_ServentiaResponsavel, id da serventia responsavel
     * @param prioridade,
     *            prioridade da pend�ncia
     * @param arquivosVincular,
     *            arquivos a serem vinculados com pend�ncia
     * @param logDt,
     *            objeto com dados de log
     * @param obFabricaConexao,
     *            fabrica de conexao para continar uma transa��o existente
     * @return void
     */
    public void gerarPendenciaVerificarDocumento(String id_Processo, String id_UsuarioServentia, String id_Movimentacao, String id_ServentiaResponsavel, String prioridade, List arquivosVincular, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
        this.gerarPendenciaProcesso(id_Processo, arquivosVincular, PendenciaTipoDt.VERIFICAR_DOCUMENTO, id_Movimentacao, id_UsuarioServentia, "", "", "", PendenciaStatusDt.ID_EM_ANDAMENTO, "", id_ServentiaResponsavel, "", "", prioridade, null, logDt, obFabricaConexao);
    }
    
    public void gerarPendenciaVerificarDocumentoProcessoSigiloso(String id_Processo, String id_UsuarioServentia, String id_Movimentacao, String id_ServentiaCargoResponsavel, String prioridade, List arquivosVincular, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
        this.gerarPendenciaProcesso(id_Processo, arquivosVincular, PendenciaTipoDt.VERIFICAR_DOCUMENTO, id_Movimentacao, id_UsuarioServentia, "", "", "", PendenciaStatusDt.ID_EM_ANDAMENTO, "", "", "", id_ServentiaCargoResponsavel, prioridade, null, logDt, obFabricaConexao);
    }
    
    /**
     * Consultar pendencias de prazos a decorrer da serventia
     * 
     * @author Leandro Bernardes
     * @param usuarioDt
     *            usuario que executa a acao
     * @return quantidade de pendencia com prazo decorrido na serventia
     * @throws Exception
     */
    public Long consultarQtdPrazosADecorrer(UsuarioDt usuarioDt) throws Exception {
        Long qtdPrazoDecorrido = null;
        
        FabricaConexao obFabricaConexao = null;
        
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            qtdPrazoDecorrido = obPersistencia.consultarQtdPrazosADecorrer(usuarioDt);
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return qtdPrazoDecorrido;
    }

	/**
     * Consulta as publiacoes publicas PJD
     * 
     * @author lrcampos
     * @since 11/11/2019 10:52
     * @param dataInicio 
     * @param dataFinal
     * @param posicao
     * @return List
     * @throws Exception
     */
	public List consultarPublicaoPublicaPJD(String dataInicio, String dataFinal, String id_Serventia,
			String idArquivoTipo, String posicao, boolean isInterna, Boolean isVirtual) throws Exception {
		if (dataInicio != null && dataInicio.trim().equals(""))
			dataInicio = null;
		if (dataFinal != null && dataFinal.trim().equals(""))
			dataFinal = null;
		List pendencias = null;

		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{


			// Deixa a data inicial no inicio do dia
			if (dataInicio != null)
				dataInicio += " 00:00:00";
			// Deixa a data final ate o fim do dia
			if (dataFinal != null)
				dataFinal += " 23:59:59";

			PendenciaPs obPersistencia = new PendenciaPs(obFabricaConexao.getConexao());
			List tempList = obPersistencia.consultarPublicacaoPublicaPJD(dataInicio, dataFinal, id_Serventia,
					idArquivoTipo, posicao, isInterna, isVirtual);

			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);

			pendencias = tempList;

		} finally {
			obFabricaConexao.fecharConexao();
        }
		
		return pendencias;
    }
    
    /**
     * Verifica se for resposta simples n�o deve gerar movimenta��o e nem pend�ncias
     * 
     * @author Leandro Bernardes
     * @param pendenciaDt
     *            pendencia a ser verificada
     * @return boolean
     */
    private boolean isFinalizacaoOficioDelegacia(PendenciaDt pendenciaDt, UsuarioDt usuarioDt) {
        boolean retorno = false;
        Integer statusPendenciaCodigo = Funcoes.StringToInt(pendenciaDt.getPendenciaStatusCodigo());
        Integer pendenciaTipoCodigo = pendenciaDt.getPendenciaTipoCodigoToInt();
        if (pendenciaDt.isPendenciaDeProcesso() && statusPendenciaCodigo == PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO 
        		&& pendenciaTipoCodigo == PendenciaTipoDt.OFICIO_DELEGACIA && Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.AUTORIDADES_POLICIAIS) {
            retorno = true;
        }
        return retorno;
    }
    
    /**
     * Cria uma pendencia para verificar resposta of�cio delegacia
     * 
     * @author lsbernardes
     * @param processoDt,
     *            bean do processo
     * @param id_UsuarioServentia,
     *            id do usuario cadastrador
     * @param id_ServentiaResponsavel,
     *            id da serventia responsavel
     * @param id_Movimentacao,
     *            movimenta��o vinculada a pend�ncia
     * @param arquivosVincular,
     *            arquivos a serem vinculados com pend�ncia
     * @param pendenciaPai,
     *            pend�ncia pai
     * @param logDt,
     *            objeto com dados do log
     * @param obFabricaConexao,
     *            fabrica de conexao para continar uma transa��cao existente
     * @return void
     */
    public boolean gerarPendenciaVerificarRespostaDelegacia( PendenciaDt pendenciaPai , UsuarioDt usuarioDt, List arquivosVincular, LogDt logDt, FabricaConexao fabConexao) throws Exception {
    	
    	// Configura a pendencia para ser persistida
        PendenciaDt pendencia = new PendenciaDt();
        pendencia.setId_PendenciaPai(pendenciaPai.getId());
        pendencia.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_EM_ANDAMENTO));
        pendencia.setPendenciaTipoCodigo(String.valueOf(PendenciaTipoDt.VERIFICAR_RESPOSTA_OFICIO_DELEGACIA));
        pendencia.setId_Movimentacao(pendenciaPai.getId_Movimentacao());
        pendencia.setId_Processo(pendenciaPai.getId_Processo());
        // Todas as pend�ncias de processo devem ter DataVisto setada
        pendencia.setDataVisto(Funcoes.DataHora(new Date()));
        pendencia.setId_UsuarioCadastrador(usuarioDt.getId_UsuarioServentia());
        pendencia.setId_UsuarioLog(logDt.getId_Usuario());
        pendencia.setIpComputadorLog(logDt.getIpComputador());

        PendenciaResponsavelDt responsavel = new PendenciaResponsavelDt();
        
        if (pendenciaPai.getProcessoDt().isSigiloso()) {
        	// GERA PEND�NCIA PARA SERVENTIA_CARGO DO MAGISTRADO RESPONS�VEL DO PROCESSO
    		ProcessoResponsavelNe ProcessoResponsavelNe = new ProcessoResponsavelNe();
    		responsavel.setId_ServentiaCargo(ProcessoResponsavelNe.consultarMagistradoResponsavelProcesso(pendenciaPai.getProcessoDt().getId(),null, fabConexao));// tratamento novo
        } else{
        	responsavel.setId_Serventia(pendenciaPai.getId_ServentiaProcesso());
        }
        pendencia.addResponsavel(responsavel);

        // Gera a pendencia
        return this.gerarPendencia(pendencia, arquivosVincular, false, fabConexao);

    }

	
	
	public String consultarCodigoModelo(String idModelo) throws Exception {
		ModeloDt modeloDt = null;
		modeloDt = new ModeloNe().consultarId(idModelo);
		if(modeloDt != null) {
			return modeloDt.getModeloCodigo();
		} else {
			return null;
		}
	}
	
	/**
	 * Consulta os oficiais cadastrados na escala Ad Hoc da central de mandados especificada pelo idServ
	 * @param descricao
	 * @param posicao
	 * @param idServ
	 * @return String
	 * @throws Exception
	 * @author hrrosa
	 */
	public String consultarOficialAdhocJSON(String descricao, String posicao, String idServ) throws Exception {
		return new ServentiaCargoEscalaNe().consultarOficialAdhocJSON(descricao, posicao, idServ);
	}

	public boolean isGerouConclusao() {
		// TODO Auto-generated method stub
		return getIdPendencia()!=null && !getIdPendencia().isEmpty();
	}

	public void confirmarEntregaCorreios(PendenciaDt pendenciaDt, String codigoBaixa, String dataEntrega) throws Exception {
		  FabricaConexao obFabricaConexao = null;
	        try {
	           obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
	           obFabricaConexao.iniciarTransacao();
	         
	            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

	            LogDt obLogDt = new LogDt("Pendencia", pendenciaDt.getId(), UsuarioDt.SistemaProjudi, "Servidor", String.valueOf(LogTipoDt.Alterar), "Pendencia Status Anterior: " + PendenciaStatusDt.ID_RECEBIDO_CORREIOS + " Pendencia Status Novo: " + pendenciaDt.getId_PendenciaStatus());
	            obPersistencia.alterarStatusAguardandoRetornoARCorreios(pendenciaDt);
	            obLog.salvar(obLogDt, obFabricaConexao);
	            
	            PendenciaCorreiosNe pendenciaCorreiosNe = new PendenciaCorreiosNe();
	            PendenciaCorreiosDt pendenciaCorreiosDt = pendenciaCorreiosNe.consultarIdPendencia(pendenciaDt.getId());
	            pendenciaCorreiosDt.setDataEntrega(dataEntrega);
	            pendenciaCorreiosDt.setCodigoBaixa(codigoBaixa);
	            pendenciaCorreiosDt.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
	            pendenciaCorreiosDt.setIpComputadorLog("Servidor");
	            pendenciaCorreiosNe.salvar(pendenciaCorreiosDt, obFabricaConexao);
	            
	            obFabricaConexao.finalizarTransacao();
	        } catch (Exception e) {
	            obFabricaConexao.cancelarTransacao();
	        } finally {
	           obFabricaConexao.fecharConexao();
	        }
	}
	
	 /**
    * Cria uma pendencia para verificar o endere�o da parte
    * @author asrocha
    */
    public void gerarPendenciaVerificarEnderecoParteCorreios(String id_processo, String id_UsuarioServentia, String id_ServentiaResponsavel, String id_Movimentacao, List arquivosVincular, PendenciaDt pendenciaPai, String id_usuario, String id_ProcessoParte, String ipComputador, FabricaConexao obFabricaConexao, String processoPrioridade) throws Exception {
        this.gerarPendenciaProcesso(id_processo, arquivosVincular, PendenciaTipoDt.VERIFICAR_ENDERECO_PARTE, id_Movimentacao, id_UsuarioServentia, "", "", id_ProcessoParte, PendenciaStatusDt.ID_EM_ANDAMENTO, "", id_ServentiaResponsavel, "", "", processoPrioridade, pendenciaPai, id_usuario,ipComputador, obFabricaConexao);
    }
    
    /**
     * Cria uma pendencia para verificar o retorno do AR dos correios
     * @author asrocha
     */
     public void gerarPendenciaVerificarRetornoARCorreios(String id_processo, String id_UsuarioServentia, String id_ServentiaResponsavel, String id_Movimentacao, List arquivosVincular, PendenciaDt pendenciaPai, String id_usuario, String id_ProcessoParte, String ipComputador, FabricaConexao obFabricaConexao, String processoPrioridade) throws Exception {
         this.gerarPendenciaProcesso(id_processo, arquivosVincular, PendenciaTipoDt.VERIFICAR_RETORNO_AR_CORREIOS, id_Movimentacao, id_UsuarioServentia, "", "", id_ProcessoParte, PendenciaStatusDt.ID_EM_ANDAMENTO, "", id_ServentiaResponsavel, "", "", processoPrioridade, pendenciaPai, id_usuario,ipComputador, obFabricaConexao);
     }
    
    /**
     * Consulta os arquivos n�o assinados que sao resposta
     * 
     * @author asrocha
     * @param pendencia vo da pendencia
     * @param comConteudo se o arquivo tera o conteudo
     * @param conexao conexao
     * @return lista de vinculos
     * @throws Exception
     */
    private List consultarVinculosRespostaNaoAssinados(PendenciaDt pendencia, boolean comConteudo, FabricaConexao conexao) throws Exception {
        PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
        List vinculos = pendenciaArquivoNe.consultarRespostaNaoAssinados(pendencia, comConteudo, conexao);
        return vinculos;
    }
    
    /**
     * Consultar arquivos n�o assinados que sao resposta de uma pendencia
     * 
     * @author asrocha
     * @param pendencia vo de pendencia
     * @param comConteudo se o arquivo tera o conteudo
     * @param fabrica fabrica de conexao
     * @return lista de arquivos
     * @throws Exception
     */
    private List consultarArquivosRespostaNaoAssinados(PendenciaDt pendencia, boolean comConteudo, FabricaConexao fabrica) throws Exception {
        List vinculos = this.consultarVinculosRespostaNaoAssinados(pendencia, comConteudo, fabrica);
        List arquivos = PendenciaArquivoNe.extrairArquivos(vinculos); // Retira somente os arquivos
        return arquivos;
    }

	public List consultarPendenciasAguardandoPagamentoPostagem(String idProcesso) throws Exception {
		PendenciaDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		List idPendencia;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaPs obPersistencia = new PendenciaPs(obFabricaConexao.getConexao());
			idPendencia = obPersistencia.consultarPendenciasAguardandoPagamentoPostagem(idProcesso);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return idPendencia;
	}
	
	/**
	 * Um item de guia postagem � pago e aguarda o envio para os correios
	 * 
	 * @author asrocha
	 * @param id_Pendencia identificador da pendencia
	 *   
	 * @throws Exception
	 */
	public void alterarStatusAguardandoEnvioCorreios(String id_Pendencia, FabricaConexao fabrica) throws Exception {
        PendenciaPs obPersistencia = new  PendenciaPs(fabrica.getConexao());
        LogDt obLogDt = new LogDt("Pendencia", id_Pendencia, UsuarioDt.SistemaProjudi, "Servidor", String.valueOf(LogTipoDt.Alterar), "Pendencia Status Anterior: " + PendenciaStatusDt.ID_AGUARDANDO_PAGAMENTO_POSTAGEM + " Pendencia Status Novo: " + PendenciaStatusDt.ID_AGUARDANDO_ENVIO_CORREIOS);
        obPersistencia.alterarStatusAguardandoEnvioCorreios(id_Pendencia);
        obLog.salvar(obLogDt, fabrica);
	}
	
    /**
     * Verifica se um determinado processo pussui a pend�ncia Verificar Endere�o Parte
     * 
     * @author asrocha
     * @param String idProcesso, processo que ser� verificado
     * @param conexao, conex�o com o banco de dados  
     * @return boolean
     * @throws Exception
     */
    public boolean verificarExistenciaVerificarEnderecoParte(String idProcessoParte, FabricaConexao obFabricaConexao) throws Exception {
        boolean retorno = false;

        PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
        retorno = pendenciaPs.verificarExistenciaVerificarEnderecoParte(idProcessoParte);

        return retorno;
    }


	// jvosantos - 13/01/2020 15:25 - Gerar pend�ncia de erro material
	public PendenciaDt gerarPendenciaErroMaterial(String idDestinatario, UsuarioDt usuario, String idProcesso,
			String idAudiProc, String dataLimite, FabricaConexao conexao) throws Exception {
		AudienciaProcessoPendenciaNe audienciaProcessoPendenciaNe = new AudienciaProcessoPendenciaNe();
		PendenciaDt pendenciaDt = new PendenciaDt();
		
		pendenciaDt.setId_UsuarioCadastrador(usuario.getId_UsuarioServentia());
		pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_EM_ANDAMENTO));
		pendenciaDt.setId_Processo(idProcesso);
		
		pendenciaDt.setIpComputadorLog(usuario.getIpComputadorLog());
		pendenciaDt.setId_UsuarioLog(usuario.getId());
		
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
		pendenciaDt.setDataInicio(df.format(new Date()));
		
		pendenciaDt.setDataLimite(dataLimite);
		
		PendenciaPs pendenciaPs = new PendenciaPs(conexao.getConexao());
		
		PendenciaTipoNe pendenciaTipoNe = new PendenciaTipoNe();
		PendenciaTipoDt pendenciaTipoDt = pendenciaTipoNe.consultarPendenciaTipoCodigo(PendenciaTipoDt.VERIFICAR_ERRO_MATERIAL);
		if (pendenciaTipoDt.getId() == null){
			throw new MensagemException("Tipo n�o encontrado");
		} else {
			pendenciaDt.setId_PendenciaTipo(pendenciaTipoDt.getId());
		}
		
		pendenciaPs.inserir(pendenciaDt);
		obDados.copiar(pendenciaDt);
		
		// Grava o log da operacao
		this.gravarLog(pendenciaDt, conexao);
		
		// Inserir responsavel na pendencia
		PendenciaResponsavelDt pendenciaResponsavelDt = new PendenciaResponsavelDt();
		PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
		pendenciaResponsavelDt.setId_ServentiaCargo(idDestinatario);
		pendenciaResponsavelDt.setId_Pendencia(pendenciaDt.getId());
		pendenciaResponsavelDt.setIpComputadorLog(usuario.getIpComputadorLog());
		pendenciaResponsavelDt.setId_UsuarioLog(usuario.getId());		
		pendenciaResponsavelNe.inserir(pendenciaResponsavelDt, conexao);
		
		audienciaProcessoPendenciaNe.salvar(pendenciaDt.getId(), idAudiProc, conexao);
		return pendenciaDt;		
	}
	
	/**
	 * ATEN��O: M�TODO FEITO EXCLUSIVAMENTE PARA O SERVI�O DE TROCA AUTOM�TICA DE N�MERO DE PROCESSOS DUPLICADOS (SEEU)
	 * 
	 * Intima todas as partes, incluindo MP caso exista. 
	 * @param processoDt
	 * @param listaArquivos
	 * @param movimentacaoDt
	 * @param usuarioDt
	 * @param logDt
	 * @param obFabricaConexao
	 * @throws Exception
	 */
	public void processoIntimarPartesJobTrocaAutomaticaNumero(ProcessoDt processoDt, List<ArquivoDt> listaArquivos, MovimentacaoDt movimentacaoDt, UsuarioDt usuarioDt, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		
		PendenciaDt pendenciaDt = new PendenciaDt();
        pendenciaDt.setPendenciaTipoCodigo(String.valueOf(PendenciaTipoDt.INTIMACAO));
        pendenciaDt.setId_Movimentacao(movimentacaoDt.getId());
        pendenciaDt.setMovimentacao(movimentacaoDt.getMovimentacaoTipo() + " - " + movimentacaoDt.getDataRealizacao());
        pendenciaDt.setId_Processo(processoDt.getId());
        pendenciaDt.setProcessoDt(processoDt);
        pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_EM_ANDAMENTO));
        pendenciaDt.setPrazo("");
        pendenciaDt.setDataLimite("");
        pendenciaDt.setId_UsuarioCadastrador(usuarioDt.getId_UsuarioServentia());
        pendenciaDt.setId_UsuarioLog(usuarioDt.getId());
        pendenciaDt.setIpComputadorLog(usuarioDt.getIpComputadorLog());
        // Todas as pend�ncias de processo devem ter DataVisto setada
        pendenciaDt.setDataVisto(Funcoes.DataHora(new Date()));
        
        //Para salvar a serventia como respons�vel na pend�ncia gerada ao intimar as partes
        //utilizando o mesmo id da serventia do processo.
        usuarioDt.setId_Serventia(processoDt.getId_Serventia());
        
        PendenciaResponsavelDt responsavel = new PendenciaResponsavelDt();
		
		this.gerarPendenciaPartesProcesso1N(processoDt, pendenciaDt, ProcessoParteTipoDt.POLO_ATIVO_CODIGO, false, false, false, listaArquivos, usuarioDt, responsavel, obFabricaConexao);      
		this.gerarPendenciaPartesProcesso1N(processoDt, pendenciaDt, ProcessoParteTipoDt.POLO_PASSIVO_CODIGO, false, false, false, listaArquivos, usuarioDt, responsavel, obFabricaConexao);
		
		
		int subtipoCodigo = 0;
		if(ServentiaSubtipoDt.isPrimeiroGrau(processoDt.getServentiaSubTipoCodigo())){
			// MINIST�RIO P�BLICO 1 Grau
			subtipoCodigo = ServentiaSubtipoDt.MP_PRIMEIRO_GRAU;
		} else if(ServentiaSubtipoDt.isTurma(processoDt.getServentiaSubTipoCodigo())){
		    // MINIST�RIO P�BLICO Turma Julgadora
			subtipoCodigo =  ServentiaSubtipoDt.MP_TURMA_JULGADORA;
		} else if(ServentiaSubtipoDt.isSegundoGrau(processoDt.getServentiaSubTipoCodigo())) {
		    // MINIST�RIO P�BLICO 2 Grau
			subtipoCodigo = ServentiaSubtipoDt.MP_SEGUNDO_GRAU;
		}
		
		if(new ProcessoResponsavelNe().temPromotorNaInstanciaProcesso(processoDt.getId(), String.valueOf(subtipoCodigo), obFabricaConexao)) {
			this.gerarPendenciaIntimacaoMinisterioPublicoPrimeiroSegundoGrau(pendenciaDt, processoDt, responsavel, listaArquivos, usuarioDt, String.valueOf(subtipoCodigo), false, logDt, obFabricaConexao);
		}

	}
	
	public void retornarStatusAguardandoEnvioCorreios(String lote) throws Exception {
		PendenciaCorreiosNe pendenciaCorreiosNe = new PendenciaCorreiosNe();
        FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
        List<String> tempList = null;
        LogDt obLogDt = null;
        try {
            obFabricaConexao.iniciarTransacao();
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            
            tempList = pendenciaCorreiosNe.consultarPendenciasLote(lote);
            if(tempList != null) {
            	for (String idPendencia : tempList) {
            		obLogDt = new LogDt("Pendencia", idPendencia, UsuarioDt.SistemaProjudi, "Servidor", String.valueOf(LogTipoDt.Alterar), "Pendencia Status Anterior: " + PendenciaStatusDt.ID_AGUARDANDO_CONFIRMACAO_CORREIOS + " Pendencia Status Novo: " + PendenciaStatusDt.ID_AGUARDANDO_ENVIO_CORREIOS);
            		obPersistencia.alterarStatusAguardandoEnvioCorreios(idPendencia);
            		obLog.salvar(obLogDt, obFabricaConexao);
				}
            }
            obFabricaConexao.finalizarTransacao();
        } catch (Exception e) {
            obFabricaConexao.cancelarTransacao();
            throw e;
        } finally {
            obFabricaConexao.fecharConexao();
        }
	}
	
	/**
     * Consultar pendencias de prazos decorridos
     * 
     * @author Ronneesley Moura Teles
     * @since 23/01/2009 13:48
     * @param usuarioDt
     *            usuario que executa a acao
     * @param tipoPendencia
     *            tipo da pendencia, 1 - intimacao, 2 - citacao, outros
     * @param numero_processo
     *            numero do processo
     * @param posicao
     *            posicao da paginacao
     * @return lista de pendencias
     * @throws Exception
     */
    public String consultarPrazosDecorridosADecorrerJSON(UsuarioNe usuarioNe, Integer tipoPendencia, String numero_processo, String posicao, boolean aDecorrer, boolean hash) throws Exception {
    	String stTemp = "";
        FabricaConexao obFabricaConexao = null;
     
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());


            PendenciaTipoNe pendenciaTipoNe = new PendenciaTipoNe();
            PendenciaTipoDt pendenciaTipoDt = new PendenciaTipoDt();
            if (tipoPendencia != null)
                pendenciaTipoDt = pendenciaTipoNe.consultarId(String.valueOf(tipoPendencia));
            else
                pendenciaTipoDt = null;
            
            stTemp = pendenciaPs.consultarPrazosDecorridosADecorrerJSON(usuarioNe, pendenciaTipoDt, numero_processo, posicao, aDecorrer, hash);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return stTemp;
    }
    
    /**
     * Listagem de pendencias finalizadas
     * 
     * @author Ronneesley Moura Teles
     * @since 16/01/2009 16:05
     * @param usuarioDt
     *            vo de usuario
     * @param pendenciaTipoDt
     *            vo de pendencia
     * @param numero_processo
     *            numero do processo
     * @param dataInicialInicio
     *            data inicial de inicio
     * @param dataFinalInicio
     *            data final de inicio
     * @param dataInicioFim
     *            data inicial de fim
     * @param dataFinalFim
     *            data final de fim
     * @param posicao
     *            posicao para paginacao
     * @return lista de pendencias onde estao finalizadas e o usuario e um dos
     *         responsaveis
     * @throws Exception
     */
    public String consultarFinalizadasResponsavelServentiaJSON(UsuarioNe usuarioNe, PendenciaTipoDt pendenciaTipoDt, String numero_processo, String dataInicialInicio, String dataFinalInicio, String dataInicioFim, String dataFinalFim, String posicao) throws Exception {
 		String stTemp = "";
 		FabricaConexao obFabricaConexao = null; 

 		try{
            // Deixa a data inicial no inicio do dia
            if (dataInicialInicio != null && !dataInicialInicio.trim().equals("")) dataInicialInicio += " 00:00:00";
            // Deixa a data final ate o fim do dia
            if (dataFinalInicio != null && !dataFinalInicio.trim().equals("")) dataFinalInicio += " 23:59:59";

            // Deixa a data inicial no inicio do dia
            if (dataInicioFim != null && !dataInicioFim.trim().equals("")) dataInicioFim += " 00:00:00";
            // Deixa a data final ate o fim do dia
            if (dataFinalFim != null && !dataFinalFim.trim().equals("")) dataFinalFim += " 23:59:59";

            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

            if (Funcoes.StringToInt(usuarioNe.getUsuarioDt().getGrupoCodigo()) == GrupoDt.MINISTERIO_PUBLICO  || Funcoes.StringToInt(usuarioNe.getUsuarioDt().getGrupoCodigo()) == GrupoDt.MP_TCE ){
                stTemp = obPersistencia.consultarIntimacoesCitacoesLidasJSON(usuarioNe, usuarioNe.getUsuarioDt().getId_UsuarioServentia(), usuarioNe.getUsuarioDt().getId_ServentiaCargo(), pendenciaTipoDt, numero_processo, dataInicialInicio, dataFinalInicio, dataInicioFim, dataFinalFim, posicao);
            } else {
                stTemp = obPersistencia.consultarIntimacoesCitacoesLidasJSON(usuarioNe, usuarioNe.getUsuarioDt().getId_UsuarioServentia(), "", pendenciaTipoDt, numero_processo, dataInicialInicio, dataFinalInicio, dataInicioFim, dataFinalFim, posicao);
            }
	        
           //Testes realizados para identificar se a pend�ncia est� na tabela pend ou na pend_final
           // o sql no m�todo consultarIntimacoesCitacoesLidasJSON foi alterardo para se a pendencia foi encontrada na pend_final retorna "1" se foi encontrada na pend retorna "0"
        	boolean isFinalizada = true;
        	String stringComparison = "";
            String jsonObjects[] = stTemp.split("(?<=})");
            for( int i = 2; i < jsonObjects.length - 1; i++){
            	stringComparison = "desc8\":\"\"1";
            	if(jsonObjects[i].contains(stringComparison)){
            		isFinalizada = true;
            	}
            	stringComparison = "desc8\":\"0\"";
            	if(jsonObjects[i].contains(stringComparison)){
            		isFinalizada = false;
            	}
            	jsonObjects[i] = jsonObjects[i].replaceFirst("}", ",\"desc9\":\""+isFinalizada+"\"}");
            	isFinalizada = true;
            }
           
            StringBuilder builder = new StringBuilder();
            for(String s : jsonObjects) {
                builder.append(s);
            }
            stTemp = builder.toString();
	            
 		}finally{
 			obFabricaConexao.fecharConexao();
 		}
 		return stTemp;   
 	}
    
    /**
     * Listagem de pendencias finalizadas
     * 
     * @author Ronneesley Moura Teles
     * @since 23/01/2009 13:48 [muito antes]
     * @param dataInicio
     *            data de inicio
     * @param dataFinal
     *            data de fim
     * @param usuarioDt
     *            vo de usuario
     * @param posicao
     *            posicao para paginacao
     * @return List
     * @throws Exception
     */
    public String consultarFinalizadasJSON(UsuarioNe usuarioNe, String id_UsuarioServentia, String id_ServentiaUsuario, PendenciaTipoDt pendenciaTipoDt, PendenciaStatusDt pendenciaStatusDt, boolean prioridade, Integer filtroTipo, String numero_processo, String dataInicialInicio, String dataFinalInicio, String dataInicioFim, String dataFinalFim, String posicao) throws Exception {
    	String stTemp = "";
        FabricaConexao obFabricaConexao = null;
     
        try {
            // Deixa a data inicial no inicio do dia
            if (dataInicialInicio != null && !dataInicialInicio.trim().equals("")) dataInicialInicio += " 00:00:00";
            // Deixa a data final ate o fim do dia
            if (dataFinalInicio != null && !dataFinalInicio.trim().equals("")) dataFinalInicio += " 23:59:59";

            // Deixa a data inicial no inicio do dia
            if (dataInicioFim != null && !dataInicioFim.trim().equals("")) dataInicioFim += " 00:00:00";
            // Deixa a data final ate o fim do dia
            if (dataFinalFim != null && !dataFinalFim.trim().equals("")) dataFinalFim += " 23:59:59";

            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            
            stTemp = pendenciaPs.consultarFinalizadasJSON(usuarioNe, id_UsuarioServentia, id_ServentiaUsuario, pendenciaTipoDt, pendenciaStatusDt, prioridade, filtroTipo, numero_processo, dataInicialInicio, dataFinalInicio, dataInicioFim, dataFinalFim, posicao);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return stTemp;
    }
    
	/**
	 * A pend�ncia Verificar Endere�o Parte � finalizada e a carta pode ser emitida
	 * 
	 * @author asrocha
	 * @param id_ProcessoParte identificador da parte do processo
	 *   
	 * @throws Exception
	 */
	public void retornarStatusAguardandoEnvioCorreios(String id_ProcessoParte, FabricaConexao fabrica) throws Exception {
        PendenciaPs obPersistencia = new  PendenciaPs(fabrica.getConexao());
        LogDt obLogDt = new LogDt("Pendencia", id_ProcessoParte, UsuarioDt.SistemaProjudi, "Servidor", String.valueOf(LogTipoDt.Alterar), "Pendencia Status Anterior: " + PendenciaStatusDt.ID_AGUARDANDO_CONFIRMACAO_CORREIOS + " Pendencia Status Novo: " + PendenciaStatusDt.ID_AGUARDANDO_ENVIO_CORREIOS);
        obPersistencia.retornarStatusAguardandoEnvioCorreios(id_ProcessoParte);
        obLog.salvar(obLogDt, fabrica);
	}
    
    /**
     * Listagem de pendencias intima��es distribu�das
     * 
     * @since 30/07/2012
     * @author mmgomes
     * @param usuarioDt
     *            vo de usuario
     * @param pendenciaTipoDt
     *            vo de pendencia
     * @param numero_processo
     *            numero do processo
     * @param dataInicialInicio
     *            data inicial de inicio
     * @param dataFinalInicio
     *            data final de inicio
     * @param dataInicioFim
     *            data inicial de fim
     * @param dataFinalFim
     *            data final de fim
     * @param posicao
     *            posicao para paginacao
     * @return lista de pendencias onde estao lidas e o cargo do usuario e
     *         um dos responsaveis
     * @throws Exception
     */
    public String consultarIntimacoesDistribuidasPromotoriaJSON(UsuarioNe usuarioNe, String numero_processo, String dataInicialInicio, String dataFinalInicio, String posicao) throws Exception {
 		String stTemp = "";
 		FabricaConexao obFabricaConexao = null; 

 		try{
            // Deixa a data inicial no inicio do dia
            if (dataInicialInicio != null && !dataInicialInicio.trim().equals("")) dataInicialInicio += " 00:00:00";
            // Deixa a data final ate o fim do dia
            if (dataFinalInicio != null && !dataFinalInicio.trim().equals("")) dataFinalInicio += " 23:59:59";
 			
 			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
             PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

             stTemp = obPersistencia.consultarIntimacoesDistribuidasPromotoriaJSON(usuarioNe, "", usuarioNe.getUsuarioDt().getId_ServentiaCargo(), numero_processo, dataInicialInicio, dataFinalInicio, posicao);
 		
 		}finally{
 			obFabricaConexao.fecharConexao();
 		}
 		return stTemp;   
 	}
    
    /**
     * Listagem de pendencias intima��es distribu�das
     * 
     * @since 21/03/2011
     * @param usuarioDt
     *            vo de usuario
     * @param pendenciaTipoDt
     *            vo de pendencia
     * @param numero_processo
     *            numero do processo
     * @param dataInicialInicio
     *            data inicial de inicio
     * @param dataFinalInicio
     *            data final de inicio
     * @param dataInicioFim
     *            data inicial de fim
     * @param dataFinalFim
     *            data final de fim
     * @param posicao
     *            posicao para paginacao
     * @return lista de pendencias onde estao finalizadas e o cargo do usuario e
     *         um dos responsaveis
     * @throws Exception
     */
    public String consultarIntimacoesDistribuidasProcuradoriaJSON(UsuarioNe usuarioNe, String numero_processo, String dataInicialInicio, String dataFinalInicio, String posicao) throws Exception {
 		String stTemp = "";
 		FabricaConexao obFabricaConexao = null; 

 		try{
            // Deixa a data inicial no inicio do dia
            if (dataInicialInicio != null && !dataInicialInicio.trim().equals("")) dataInicialInicio += " 00:00:00";
            // Deixa a data final ate o fim do dia
            if (dataFinalInicio != null && !dataFinalInicio.trim().equals("")) dataFinalInicio += " 23:59:59";
 			
 			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

             stTemp = obPersistencia.consultarIntimacoesDistribuidasProcuradoriaJSON(usuarioNe, "", usuarioNe.getUsuarioDt().getId_ServentiaCargo(), numero_processo, dataInicialInicio, dataFinalInicio, posicao);
 		
 		}finally{
 			obFabricaConexao.fecharConexao();
 		}
 		return stTemp;   
 	}
    

    /**
     * Listagem de pendencias respondidas da serventia
     * 
     * @author Leandro Bernardes
     * @param dataInicio
     *            data de inicio
     * @param dataFinal
     *            data de fim
     * @param usuarioDt
     *            vo de usuario
     * @param posicao
     *            posicao para paginacao
     * @return List
     * @throws Exception
     */
    public String consultarRespondidasServentiaJSON(UsuarioNe usuarioNe, PendenciaTipoDt pendenciaTipoDt, PendenciaStatusDt pendenciaStatusDt, boolean prioridade, Integer filtroTipo, String numero_processo, String dataInicialInicio, String dataFinalInicio, String dataInicioFim, String dataFinalFim, String posicao) throws Exception {
    	String stTemp = "";
        FabricaConexao obFabricaConexao = null;
     
        try {
            // Deixa a data inicial no inicio do dia
            if (dataInicialInicio != null && !dataInicialInicio.trim().equals("")) dataInicialInicio += " 00:00:00";
            // Deixa a data final ate o fim do dia
            if (dataFinalInicio != null && !dataFinalInicio.trim().equals("")) dataFinalInicio += " 23:59:59";

            // Deixa a data inicial no inicio do dia
            if (dataInicioFim != null && !dataInicioFim.trim().equals("")) dataInicioFim += " 00:00:00";
            // Deixa a data final ate o fim do dia
            if (dataFinalFim != null && !dataFinalFim.trim().equals("")) dataFinalFim += " 23:59:59";
        	

            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            
            stTemp = pendenciaPs.consultarRespondidasServentiaJSON(usuarioNe, pendenciaTipoDt, pendenciaStatusDt, prioridade, filtroTipo, numero_processo, dataInicialInicio, dataFinalInicio, dataInicioFim, dataFinalFim, posicao);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return stTemp;
    }
    
    /**
     * Listagem de pendencias respondidas de um usu�rio
     * 
     * @author Leandro Bernardes
     * @param dataInicio
     *            data de inicio
     * @param dataFinal
     *            data de fim
     * @param usuarioDt
     *            vo de usuario
     * @param posicao
     *            posicao para paginacao
     * @return List
     * @throws Exception
     */
    public String consultarRespondidasUsuarioJSON(UsuarioNe usuarioNe, PendenciaTipoDt pendenciaTipoDt, PendenciaStatusDt pendenciaStatusDt, boolean prioridade, Integer filtroTipo, String numero_processo, String dataInicialInicio, String dataFinalInicio, String dataInicioFim, String dataFinalFim, String posicao) throws Exception {
    	String stTemp = "";
        FabricaConexao obFabricaConexao = null;
     
        try {
            // Deixa a data inicial no inicio do dia
            if (dataInicialInicio != null && !dataInicialInicio.trim().equals("")) dataInicialInicio += " 00:00:00";
            // Deixa a data final ate o fim do dia
            if (dataFinalInicio != null && !dataFinalInicio.trim().equals("")) dataFinalInicio += " 23:59:59";

            // Deixa a data inicial no inicio do dia
            if (dataInicioFim != null && !dataInicioFim.trim().equals("")) dataInicioFim += " 00:00:00";
            // Deixa a data final ate o fim do dia
            if (dataFinalFim != null && !dataFinalFim.trim().equals("")) dataFinalFim += " 23:59:59";
        	

            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            
            stTemp = pendenciaPs.consultarRespondidasUsuarioJSON(usuarioNe, pendenciaTipoDt, pendenciaStatusDt, prioridade, filtroTipo, numero_processo, dataInicialInicio, dataFinalInicio, dataInicioFim, dataFinalFim, posicao);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return stTemp;
    }
    

    /**
     * Consulta as pendencias expedidas para serventias (on-line)
     * 
     * @author Leandro Bernardes
     * @since 13/08/2009
     * @param usuarioDt
     *            vo do usuario
     * @param posicao
     *            posicao da paginacao
     * @return List
     * @throws Exception
     */
    public String consultarExpedidasServentiaJSON(UsuarioNe usuarioNe, PendenciaTipoDt pendenciaTipoDt, PendenciaStatusDt pendenciaStatusDt, boolean prioridade, Integer filtroTipo, String numero_processo, String dataInicialInicio, String dataFinalInicio, String posicao) throws Exception {
    	String stTemp = "";
        FabricaConexao obFabricaConexao = null;
     
        try {
            // Deixa a data inicial no inicio do dia
            if (dataInicialInicio != null && !dataInicialInicio.trim().equals("")) dataInicialInicio += " 00:00:00";
            // Deixa a data final ate o fim do dia
            if (dataFinalInicio != null && !dataFinalInicio.trim().equals("")) dataFinalInicio += " 23:59:59";

            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            
            stTemp = pendenciaPs.consultarExpedidasServentiaJSON(usuarioNe, pendenciaTipoDt, pendenciaStatusDt, prioridade, filtroTipo, numero_processo, dataInicialInicio, dataFinalInicio, posicao);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return stTemp;
    }
    
    /**
     * Consulta as pendencias de um determinado usuario
     * 
     * @author Ronneesley Moura Teles
     * @since 12/11/2008 15:17
     * @param usuarioDt
     *            vo do usuario
     * @param posicao
     *            posicao da paginacao
     * @return List
     * @throws Exception
     */
    public String consultarMinhasJSON(UsuarioNe usuarioNe, PendenciaTipoDt pendenciaTipoDt, PendenciaStatusDt pendenciaStatusDt, boolean prioridade, Integer filtroTipo, String numero_processo, String dataInicialInicio, String dataFinalInicio, String posicao) throws Exception {
    	String stTemp = "";
        FabricaConexao obFabricaConexao = null;
     
        try {
            // Deixa a data inicial no inicio do dia
            if (dataInicialInicio != null && !dataInicialInicio.trim().equals("")) dataInicialInicio += " 00:00:00";
            // Deixa a data final ate o fim do dia
            if (dataFinalInicio != null && !dataFinalInicio.trim().equals("")) dataFinalInicio += " 23:59:59";

            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            
            stTemp = pendenciaPs.consultarMinhasJSON(usuarioNe, pendenciaTipoDt, pendenciaStatusDt, prioridade, filtroTipo, numero_processo, dataInicialInicio, dataFinalInicio, posicao);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return stTemp;
    }
    
    /**
     * Listagem de pendencias fechadas independente de estarem vistadas ou n�o.
     * 
     * @param dataInicio
     *            data de inicio
     * @param dataFinal
     *            data de fim
     * @param posicao
     *            posicao para paginacao
     * @return List
     * @throws Exception
     */
    public String consultarPendenciasFechadasJSON(UsuarioNe usuarioNe, String id_UsuarioServentia, String id_ServentiaUsuario, PendenciaTipoDt pendenciaTipoDt, PendenciaStatusDt pendenciaStatusDt, boolean prioridade, Integer filtroTipo, String numero_processo, String dataInicialInicio, String dataFinalInicio, String dataInicioFim, String dataFinalFim, String posicao) throws Exception {
    	String stTemp = "";
        FabricaConexao obFabricaConexao = null;
     
        try {
            // Deixa a data inicial no inicio do dia
            if (dataInicialInicio != null && !dataInicialInicio.trim().equals("")) dataInicialInicio += " 00:00:00";
            // Deixa a data final ate o fim do dia
            if (dataFinalInicio != null && !dataFinalInicio.trim().equals("")) dataFinalInicio += " 23:59:59";

            // Deixa a data inicial no inicio do dia
            if (dataInicioFim != null && !dataInicioFim.trim().equals("")) dataInicioFim += " 00:00:00";
            // Deixa a data final ate o fim do dia
            if (dataFinalFim != null && !dataFinalFim.trim().equals("")) dataFinalFim += " 23:59:59";

            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            
            stTemp = pendenciaPs.consultarPendenciasFechadasJSON(usuarioNe, id_UsuarioServentia, id_ServentiaUsuario, pendenciaTipoDt, pendenciaStatusDt, prioridade, filtroTipo, numero_processo, dataInicialInicio, dataFinalInicio, dataInicioFim, dataFinalFim, posicao);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return stTemp;
    }
    
    /**
     * Listagem de intima��es lidas paara distribui��o de uma serventia
     * 
     * @since 30/08/2011
     * @author mmgomes
     * @param usuarioDt
     *            vo de usuario
     * @param pendenciaTipoDt
     *            vo de pendencia
     * @param numero_processo
     *            numero do processo
     * @param dataInicialInicio
     *            data inicial de inicio
     * @param dataFinalInicio
     *            data final de inicio
     * @param dataInicioFim
     *            data inicial de fim
     * @param dataFinalFim
     *            data final de fim
     * @param posicao
     *            posicao para paginacao
     * @return lista de pendencias onde estao lidas e o cargo do usuario e
     *         um dos responsaveis
     * @throws Exception
     */

    public String consultarIntimacoesLidasDistribuicaoJSON(UsuarioNe usuarioNe, String numero_processo, String dataInicialInicio, String dataFinalInicio, String posicao) throws Exception {
    	String pendencias = "";

        if (usuarioNe.getUsuarioDt().isCoordenadorPromotoria()) {
            pendencias = this.consultarIntimacoesLidasDistribuicaoPromotoriaJSON(usuarioNe, numero_processo, dataInicialInicio, dataFinalInicio, posicao);
        }else if (usuarioNe.getUsuarioDt().isCoordenadorJuridico()){
            pendencias = this.consultarIntimacoesLidasDistribuicaoProcuradoriaJSON(usuarioNe, numero_processo, dataInicialInicio, dataFinalInicio, posicao);
        }    
        return pendencias;
    }
    
    /**
     * Listagem de pendencias intima��es distribu�das
     * 
     * @since 30/07/2012
     * @author mmgomes
     * @param usuarioDt
     *            vo de usuario
     * @param pendenciaTipoDt
     *            vo de pendencia
     * @param numero_processo
     *            numero do processo
     * @param dataInicialInicio
     *            data inicial de inicio
     * @param dataFinalInicio
     *            data final de inicio
     * @param dataInicioFim
     *            data inicial de fim
     * @param dataFinalFim
     *            data final de fim
     * @param posicao
     *            posicao para paginacao
     * @return lista de pendencias onde estao lidas e o cargo do usuario e
     *         um dos responsaveis
     * @throws Exception
     */
    public String consultarIntimacoesLidasDistribuicaoPromotoriaJSON(UsuarioNe usuarioNe, String numero_processo, String dataInicialInicio, String dataFinalInicio, String posicao) throws Exception {
 		String stTemp = "";
 		FabricaConexao obFabricaConexao = null; 

 		try{
            // Deixa a data inicial no inicio do dia
            if (dataInicialInicio != null && !dataInicialInicio.trim().equals("")) dataInicialInicio += " 00:00:00";
            // Deixa a data final ate o fim do dia
            if (dataFinalInicio != null && !dataFinalInicio.trim().equals("")) dataFinalInicio += " 23:59:59";
 			
 			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

            stTemp = obPersistencia.consultarIntimacoesLidasDistribuicaoPromotoriaJSON(usuarioNe, "", usuarioNe.getUsuarioDt().getId_ServentiaCargo(), numero_processo, dataInicialInicio, dataFinalInicio, posicao);
 		
 		}finally{
 			obFabricaConexao.fecharConexao();
 		}
 		return stTemp;   
 	}
    
    /**
     * Listagem de pendencias intima��es distribu�das
     * 
     * @since 30/08/2012
     * @author mmgomes
     * @param usuarioDt
     *            vo de usuario
     * @param pendenciaTipoDt
     *            vo de pendencia
     * @param numero_processo
     *            numero do processo
     * @param dataInicialInicio
     *            data inicial de inicio
     * @param dataFinalInicio
     *            data final de inicio
     * @param dataInicioFim
     *            data inicial de fim
     * @param dataFinalFim
     *            data final de fim
     * @param posicao
     *            posicao para paginacao
     * @return lista de pendencias onde estao lidas e o cargo do usuario e
     *         um dos responsaveis
     * @throws Exception
     */
    public String consultarIntimacoesLidasDistribuicaoProcuradoriaJSON(UsuarioNe usuarioNe, String numero_processo, String dataInicialInicio, String dataFinalInicio, String posicao) throws Exception {
 		String stTemp = "";
 		FabricaConexao obFabricaConexao = null; 

 		try{
            // Deixa a data inicial no inicio do dia
            if (dataInicialInicio != null && !dataInicialInicio.trim().equals("")) dataInicialInicio += " 00:00:00";
            // Deixa a data final ate o fim do dia
            if (dataFinalInicio != null && !dataFinalInicio.trim().equals("")) dataFinalInicio += " 23:59:59";
 			
 			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());

            stTemp = obPersistencia.consultarIntimacoesLidasDistribuicaoProcuradoriaJSON(usuarioNe, "", usuarioNe.getUsuarioDt().getId_ServentiaCargo(), numero_processo, dataInicialInicio, dataFinalInicio, posicao);
 		
 		}finally{
 			obFabricaConexao.fecharConexao();
 		}
 		return stTemp;   
 	}
    
	/**
	 * Uma inconsist�ncia � retornada pelos correios
	 * @author asrocha
	 * @param id_Pendencia identificador da pendencia
	 */
	public void alterarStatusInconsistenciaCorreios(String id_Pendencia, FabricaConexao fabrica) throws Exception {
        PendenciaPs obPersistencia = new  PendenciaPs(fabrica.getConexao());
        LogDt obLogDt = new LogDt("Pendencia", id_Pendencia, UsuarioDt.SistemaProjudi, "Servidor", String.valueOf(LogTipoDt.Alterar), "Pendencia Status Anterior: " + PendenciaStatusDt.ID_AGUARDANDO_CONFIRMACAO_CORREIOS + " Pendencia Status Novo: " + PendenciaStatusDt.ID_INCONSISTENCIA_CORREIOS);
        obPersistencia.alterarStatusInconsistenciaCorreios(id_Pendencia);
        obLog.salvar(obLogDt, fabrica);
	}

	/**
     * Consulta pendencias abertas da serventia
     * 
     * @author Ronneesley Moura Teles
     * @since 07/05/2008 - 16:34 Alteracoes 1 - Melhorias
     * @author Ronneesley Moura Teles
     * @since 08/05/2008 - 11:11
     * 
     * 2 - Incrementar paginacao
     * @author Ronneesley Moura Teles
     * @since 28/08/2008 16:54
     * 
     * 3 - Incrementar filtro de pendencia
     * @author Ronneesley Moura Teles
     * @since 09/12/2008 14:45
     * @param usuarioDt
     *            usuario da sessao
     * @param pendenciaTipoDt
     *            vo de tipo de pendencia
     * @param pendenciaStatusDt
     *            vo de status de pendencia
     * @param prioridade
     *            prioridade da listagem
     * @param filtroTipo
     *            filtro por tipo de pendencia, 1 somente processo, 2 somente
     *            normais os demais todas
     * @param numero_processo
     *            filtro para o numero do processo
     * @param dataInicialInicio
     *            data inicial para o inicio
     * @param dataFinalInicio
     *            data final para o inicio
     * @param posicao
     *            posicao da paginacao
     * @return List
     * @throws Exception
     */
    public String consultarAbertasJSON(UsuarioNe usuarioNe, PendenciaTipoDt pendenciaTipoDt, PendenciaStatusDt pendenciaStatusDt, boolean prioridade, Integer filtroTipo, Integer filtroCivelCriminal, String numero_processo, String dataInicialInicio, String dataFinalInicio, String posicao) throws Exception {
    	String stTemp = "";
        FabricaConexao obFabricaConexao = null;
     
        try {
            // Deixa a data inicial no inicio do dia
            if (dataInicialInicio != null && !dataInicialInicio.trim().equals("")) dataInicialInicio += " 00:00:00";
            // Deixa a data final ate o fim do dia
            if (dataFinalInicio != null && !dataFinalInicio.trim().equals("")) dataFinalInicio += " 23:59:59";

            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs pendenciaPs = new  PendenciaPs(obFabricaConexao.getConexao());
            
            stTemp = pendenciaPs.consultarAbertasJSON(usuarioNe, pendenciaTipoDt, pendenciaStatusDt, prioridade, filtroTipo, filtroCivelCriminal, numero_processo, dataInicialInicio, dataFinalInicio, posicao);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return stTemp;
    }
    
    /**
	 * Consutlar pendencias de prazos decorridos
	 * 
	 * @param id_Serventia
	 *           identificador da serventia do usuario que executa a acao
	 * @param PendenciaTipoDt
	 *            tipo da pendencia
	 * @param numero_processo
	 *            numero do processo
	 * @param posicao
	 *            paginacao
	 * @return lista de pendencias com prazos
	 * @throws Exception
	 */
    public String consultarPrazosDecorridosDevolucaoAutosJSON(String id_Serventia, String nomeBusca, String posicao) throws Exception {
 		String stTemp = "";
 		FabricaConexao obFabricaConexao = null; 
 		try{
 			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
            stTemp = obPersistencia.consultarPrazosDecorridosDevolucaoAutosJSON(id_Serventia, nomeBusca, posicao);
 		}finally{
 			obFabricaConexao.fecharConexao();
 		}
 		return stTemp;   
 	}

	public String verificarAlvaraSoltura(PendenciaDt pendenciaDt) {
		String stRetorno = "";
		if (pendenciaDt != null && pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.ALVARA_SOLTURA) {
			if (!pendenciaDt.getStatusPendenciaRetorno().equalsIgnoreCase("Cancelada")){
				if (pendenciaDt.getId_EventoTipo().equalsIgnoreCase("")) stRetorno += "O Campo Evento Tipo � obrigat�rio. \n";
				if (pendenciaDt.getDataEvento().equalsIgnoreCase("")) stRetorno += "O Campo Data do Evento Tipo � obrigat�rio. \n";
			}
		}
		
		return stRetorno;
	}
	public String consultarQtdLocomocao(String idModelo) throws Exception {
		return new ModeloNe().consultarQtdLocomocao(idModelo);
	}
	
	public String reservarNumeroProximoMandado() throws Exception {
		return new MandadoJudicialNe().reservarNumeroProximoMandado();
	}
	
	public boolean temCentralProjudiImplantada(String idServ) throws Exception {
		return new ServentiaNe().temCentralProjudiImplantada(idServ);
	}
	
    public String retornaPrazoLimiteMandado(String codigoPrazoMandado, String prazoEspecial, String idServentiaExpedir) throws Exception {
    	return new MandadoJudicialNe().retornaPrazoLimiteMandado(codigoPrazoMandado, prazoEspecial, idServentiaExpedir, null);
    }
    
    public void moverPendenciaParaPendFinal(PendenciaDt pendenciaDt, FabricaConexao obFabricaConexao) throws Exception {
    	 PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
         if (pendenciaDt != null && pendenciaDt.temDataFim() && pendenciaDt.temDataVisto()){
         	obPersistencia.moverPendencia(pendenciaDt.getId());
         }
    }
}
