/*
 * ApplicationListener.java
 *
 * Created on 11/10/2007, 16:20:04
 *
 * To change this template, choose Tools | Templates
 * AND open the template in the editor.
 */

package br.gov.go.tj.projudi;

import java.io.File;
import java.io.FilenameFilter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.Security;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import br.gov.go.tj.projudi.dt.MidiaPublicadaDt;
import br.gov.go.tj.projudi.ne.LogNe;
import br.gov.go.tj.projudi.ne.PendenciaTipoRelacionadaNe;
import br.gov.go.tj.projudi.util.DistribuicaoProcesso;
import br.gov.go.tj.projudi.util.DistribuicaoProcessoServentiaCargo;
import br.gov.go.tj.projudi.util.GerenciadorEmail;
import br.gov.go.tj.projudi.util.ProcessoNumero;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.ConexaoBD;
import br.gov.go.tj.utils.Funcoes;

//import br.gov.go.tj.projudi.util.AddLicense;

/**
 * Classe Servlet usada como "ouvinte" para o in�cio e fim da aplica��o. Esta
 * classe � utilizada para configurar e inicializar os frameworks necess�rios. �
 * utilizada tamb�m para finalizar os pools de conex�es com o banco de dados e o
 * Hibernate no encerramento da aplica��o.
 *
 * Para esta classe funcionar corretamente, ela deve ser declarada no arquivo
 * web.xml dentro da tag "<listener>"
 *
 * @author Jesus Rodrigo
 * @author Renato Augusto
 */
public class Projudi implements ServletContextListener {

	private static Logger logger = Logger.getLogger(Projudi.class);	
	private Timer timer = new Timer();
	
	private void iniciaNumeroProcesso() {
		try{
			ProcessoNumero.getInstance();
			logger.info("N�mero de Processo inicializado com sucesso");
			
		} catch(Exception e) {
		    e.printStackTrace();
		    logger.error(e.getMessage() + "\n Erro ao consultar pr�ximo numero de processo.");
			throw new RuntimeException("Local Exception "+ this.getClass().getName() +".iniciaNumeroProcesso(): " + e.getMessage(), e);
		}
	}
	
	private void iniciarDistribuicaoProcesso() {
		try{
			DistribuicaoProcesso.getInstance();
			logger.info("Distribuicao de Processo inicializada com sucesso");
		} catch(Exception e) {
		    e.printStackTrace();
		    logger.error(e.getMessage() +  "\n Erro ao gerar a lista de distribuicao de processo para serventia.");
			throw new RuntimeException("Local Exception "+ this.getClass().getName() +".iniciarDistribuicaoProcesso(): " + e.getMessage(), e);
		}
	}

	private void iniciarDistribuicaoProcessoJuiz() {
		try{
			DistribuicaoProcessoServentiaCargo.getInstance();
			logger.info("Distribuicao de Processo Juiz inicializada com sucesso");
		} catch(Exception e) {
		    e.printStackTrace();
		    logger.error(e.getMessage() +  "\n Erro ao gerar a lista de distribui��o de processo para os Juizes.");
			throw new RuntimeException("Local Exception "+ this.getClass().getName() +".iniciarDistribuicaoProcessoJuiz(): " + e.getMessage(), e);
		}
	}

	/*private void iniciarDistribuicaoProcessoConciliador() {
		try{
			DistribuicaoProcessoConciliador.getInstance();
			logger.info("Distribuicao de Processo Conciliador inicializada com sucesso");
		} catch(Exception e) {
		    e.printStackTrace();
		    logger.error(e.getMessage() +  "\n Erro ao gerar a lista de distribui��o de processo para os Conciliadores.");
			throw new RuntimeException("Local Exception "+ this.getClass().getName() +".iniciarDistribuicaoProcessoConciliador(): " + e.getMessage(), e);
		}
	}*/
	
	private void iniciarMapPendenciasRelacionadas() {
		try{
			PendenciaTipoRelacionadaNe pendenciaTipoRelacionadaNe = new PendenciaTipoRelacionadaNe();
			pendenciaTipoRelacionadaNe.consultaPendenciaTipoRelacionadas();
			logger.info("Map de Pend�ncias Relacionadas inicializado com sucesso");
		} catch(Exception e) {
		    e.printStackTrace();
		    logger.error(e.getMessage() +  "\n Erro ao iniciar Map de Pend�ncias Relacionadas.");
			throw new RuntimeException("Local Exception "+ this.getClass().getName() +".iniciarMapPendenciasRelacionadas(): " + e.getMessage(), e);
		}
	}
	
	private void tentarApagarArquivosTemporariosMidiaPublicada() {
		try{
			ProjudiPropriedades propriedades = ProjudiPropriedades.getInstance();
			
			String pastaTemporaria = propriedades.getObjectStorageUploadPastaTemporaria();
			
			if (!Funcoes.isStringVazia(pastaTemporaria)) return; //se n�o o caminho da aplica��o n�o deve-se fazer nada...
			
			pastaTemporaria = propriedades.getCaminhoAplicacao();
			
			File pasta = new File(pastaTemporaria);
			
			File ifile[] = pasta.listFiles(new FilenameFilter() {
			    public boolean accept(File dir, String name) {
			        return name.toLowerCase().startsWith(MidiaPublicadaDt.PrefixoArquivoTemporario);
			    }
			});
			
			if (ifile != null) {
				for(File currentFile : ifile) {
					currentFile.delete();
				}
			}
			
			logger.info("Limpeza de arquivos tempor�rios de upload de m�dias realizada com sucesso");
		} catch(Exception e) {
		    e.printStackTrace();
		    logger.error(e.getMessage() +  "\n Erro ao tentar apagar arquivos tempor�rios de upload de m�dias.");
			//throw new RuntimeException("Local Exception "+ this.getClass().getName() +".tentarApagarArquivosTemporariosAudienciaUpload(): " + e.getMessage(), e);
		}
	}

	/***
	 *@author jrcorrea
	 *@param null
	 *@return null
	 *	11/05/2013
	 * o adabas n�o parar� mais o projudi 
	 */
	public static void iniciarPoolADABAS() throws Exception {
		
		Context envCtx;
		try{
			envCtx = (Context) new InitialContext().lookup("java:comp/env");	
			DataSource dataSourceAdabas = (DataSource) envCtx.lookup("jdbc/AdabasProjudiDS");
					
			logger.info("jrcPool: DataSouce Adabas setado");
	
			ConexaoBD.setDatasourceAdabas(dataSourceAdabas);
		} catch(NamingException e) {
			
			logger.error("projudi.iniciarPoolADABAS(): N�o foi possivel pegar o Data Source do ADABAS");
		}	
		
	}
	
	/***
	 *@author jrcorrea
	 *@param null
	 *@return null
	 *	11/05/2013
	 * o adabas n�o parar� mais o projudi 
	 */
	public static void iniciarPoolADABASProcessos() throws Exception {
		
		Context envCtx;
		try{
			envCtx = (Context) new InitialContext().lookup("java:comp/env");
			
			DataSource dataSourceAdabas = tenteObterDataSource(envCtx, "jdbc/dataSourceSPGDS", false);
			
			if (dataSourceAdabas == null) {
				dataSourceAdabas = tenteObterDataSource(envCtx, "jdbc/dataSourceSPG", true);	
			}
					
			logger.info("jrcPool: DataSouce Adabas Processos setado");
	
			ConexaoBD.setDatasourceAdabasProcessos(dataSourceAdabas);
		} catch(NamingException e) {
			
			logger.error("projudi.iniciarPoolADABASProcessos(): N�o foi possivel pegar o Data Source do ADABAS PROCESSOS");
		}	
		
	}
	
	private static DataSource tenteObterDataSource(Context envCtx, String nome, boolean lancaExcecao) throws Exception {
		try 
		{
			return (DataSource) envCtx.lookup(nome);
			
		} catch (Exception ex) {
			if (lancaExcecao) throw new Exception("Erro ao obter o datasource " + nome, ex);
			return null;
		}
	}
	
	/***
	 *@author mmgomes
	 *@param null
	 *@return null
	 *	16/03/2015
	 * o PJE n�o parar� o projudi 
	 */
	public static void iniciarPoolPJE() throws Exception {
		
		Context envCtx;
		try{
			envCtx = (Context) new InitialContext().lookup("java:comp/env");	
			DataSource dataSourcePJE = (DataSource) envCtx.lookup("jdbc/PJEProjudiDS");
					
			logger.info("jrcPool: DataSouce PJE setado");
	
			ConexaoBD.setDatasourcePJE(dataSourcePJE);
			
		} catch(NamingException e) {
			
			logger.error("projudi.iniciarPoolPJE(): N�o foi possivel pegar o Data Source do PJE");
		}	
		
	}
	
	public static void iniciarPoolDRS3() throws Exception {
		try {

			Context envCtx = (Context) new InitialContext().lookup("java:comp/env");
			DataSource dataSource = (DataSource) envCtx.lookup("jdbc/audienciaTJGO");

			logger.info("iniciarPoolDRS3: DataSouce setado");

			ConexaoBD.setDatasourceDRS3(dataSource);

			logger.info("iniciarPoolDRS3: Ok");
		} catch (NamingException e) {
			logger.error("projudi.iniciarPoolDRS3(): N�o foi possivel obter o Data Source do DRS 3");
		}
	}

	public static void iniciarPoolDRS2() throws Exception {
		try {

			Context envCtx = (Context) new InitialContext().lookup("java:comp/env");
			DataSource dataSource = (DataSource) envCtx.lookup("jdbc/audienciaTJGO_FB");

			logger.info("iniciarPoolDRS2: DataSouce setado");

			ConexaoBD.setDatasourceDRS2(dataSource);

			logger.info("iniciarPoolDRS2: Ok");
		} catch (NamingException e) {
			logger.error("projudi.iniciarPoolDRS2(): N�o foi possivel obter o Data Source do DRS 2");
		}
	}

	private void iniciarPool() throws RuntimeException {

		try {

			Context envCtx = (Context) new InitialContext().lookup("java:comp/env");

			DataSource dataSourcePersistencia = (DataSource) envCtx.lookup("jdbc/projudiTJGO");
			ConexaoBD.setDatasourcePersistencia(dataSourcePersistencia);
			logger.info("jrcPool: DataSouce setado");

			DataSource dataSourceConsulta = (DataSource) envCtx.lookup("jdbc/projudiTJGO_STBY");
			ConexaoBD.setDatasourceConsulta(dataSourceConsulta);
			logger.info("jrcPool: DataSouce Consulta setado");

			iniciarPoolADABAS();

			iniciarPoolADABASProcessos();

			iniciarPoolDRS2();

			iniciarPoolDRS3();

			logger.info("jrcPool: Ok");
			Security.addProvider(new BouncyCastleProvider());
		} catch (NamingException e) {
			e.printStackTrace();
			logger.error(e.getMessage() + "\n jrcPool Erro -  Erro no nome do pool");
			throw new RuntimeException(
					"Local Exception " + this.getClass().getName() + ".iniciarPool(): " + e.getMessage(), e);
		} catch (Exception e) {
			e.printStackTrace();

			logger.error(e.getMessage() + "\n jrcPool Erro - Erro ao buscar uma conex�o");
			throw new RuntimeException(
					"Local Exception " + this.getClass().getName() + ".iniciarPool(): " + e.getMessage(), e);
		}
	}

	public void contextInitialized(ServletContextEvent servletEvent) {			        	       
	        
    		logger.info("*** INICIALIZANDO SISTEMA PROJUDI ***");
			
			Locale.setDefault(new Locale("pt", "BR"));
    
    		String caminhoAplicacao = "";
    		if (servletEvent != null && servletEvent.getServletContext() != null)
    			caminhoAplicacao = servletEvent.getServletContext().getRealPath("");    		
            iniciarPool();
    		initProperties(caminhoAplicacao);		
    		iniciaNumeroProcesso();
    		//caCert = initCertificador();
    		//initFileUpload(caCert);
    		//initUpTimeTest();
    		//inciarExecucaoAutomatica();
    		iniciarDistribuicaoProcesso();
    		iniciarDistribuicaoProcessoJuiz();
    		//iniciarDistribuicaoProcessoConciliador();
    		iniciarMapPendenciasRelacionadas();
    		tentarApagarArquivosTemporariosMidiaPublicada();
    		
    		//TODO Fred: Chamada do m�todo na inicializa��o do Projudi 
    		//iniciaNumeroGuia();
    
    		logger.info("*** SISTEMA PROJUDI INICIALIZADO ***");
    		
    		timer.schedule(new AcompanharDataHoraServidor(System.currentTimeMillis()), 60000);
    		
	}

	private class esperar15Minutos extends TimerTask {

		@Override
		public void run() {
			timer.schedule(new AcompanharDataHoraServidor(System.currentTimeMillis()), 60000);			
		}
		
	}
	private class AcompanharDataHoraServidor extends TimerTask {
			
		private long loData;

		public AcompanharDataHoraServidor(long hora) {
			loData = hora;
		}

		public void run() {
			boolean boEsperar15Minutos= false;
	        InetAddress ip;
	        String hostname="";
	        String stIp = "";
	        try {
	            ip = InetAddress.getLocalHost();
	            hostname = ip.getHostName();		          
	            stIp = ip.getHostAddress();
	        } catch (UnknownHostException e) {
	 		            
	        }
	        
	        //se n�o for nas maquinas do produ��o nem deve continuar a executar 
			if (!"sv-javaas-c".contains(hostname) ) {
				return;				
			}
			
			boEsperar15Minutos = false;
						
			// se a diferen�a for maior que 20 segundos envio email
			if ((System.currentTimeMillis()-loData)>80000) {
				String stMensagem1 = " O Servidor " +hostname + ", ip " + stIp.toString() +", teve a sua hora alterada ";
				new GerenciadorEmail(	new String[] {"Jesus Rodrigo","Marcio Mendon�a Gomes", "Leandro de Souza Bernardes","Massahide de Oliveira Namba","Keila Sousa Silva","Giuliano Silva Oliveira","infra so"}, 
										new String[] {"jrcorrea@tjgo.jus.br","mmgomes@tjgo.jus.br","lsbernardes@tjgo.jus.br","monamba_old@tjgo.jus.br","kssilva@tjgo.jus.br","gsilvaoliveira@tjgo.jus.br", "infra.so@tjgo.jus.br"} ,
										"Altera��o inesperada na data/hora do servidor", stMensagem1).start();
				boEsperar15Minutos = true;					
			}
			Date Data;
			try {
				Data = (new LogNe().getDataHoraBDProducao());
				Date dataAqui = new Date();
				long diferencaTempo=(dataAqui.getTime() -Data.getTime());
				//System.out.println (TimeZone.getDefault());
				if(diferencaTempo>20000 || diferencaTempo<-20000) {
					String stMensagem1 = " O Servidor " +hostname + ", ip " + stIp.toString() +", Esta com data : " + Funcoes.DataHora(dataAqui) + " e banco de produ��o est� com a data:" +  Funcoes.DataHora(Data);
					new GerenciadorEmail(	new String[] {"Jesus Rodrigo","Marcio Mendon�a Gomes", "Leandro de Souza Bernardes","Massahide de Oliveira Namba","Keila Sousa Silva","Giuliano Silva Oliveira","infra Bd"}, 
							new String[] {"jrcorrea@tjgo.jus.br","mmgomes@tjgo.jus.br","lsbernardes@tjgo.jus.br","monamba_old@tjgo.jus.br","kssilva@tjgo.jus.br","gsilvaoliveira@tjgo.jus.br", "infra.bd@tjgo.jus.br"} ,
							"A data/hora do servidor de aplica��o est� diferente do banco de produ��o", stMensagem1 ).start();
					boEsperar15Minutos = true;					
				}
			} catch (Exception e) {
				//se der erro foi no acesso ao banco assim n�o tem nada o que fazer
			}
			try {
				Data = (new LogNe().getDataHoraBDDataGuard());
				Date dataAqui = new Date();
				long diferencaTempo=(dataAqui.getTime() -Data.getTime());
				//System.out.println (TimeZone.getDefault());
				if(diferencaTempo>20000 || diferencaTempo<-20000) {
					String stMensagem1 = " O Servidor " +hostname + ", ip " + stIp.toString() +", Esta com data : " + Funcoes.DataHora(dataAqui) + " e banco de Dataguard est� com a data:" +  Funcoes.DataHora(Data);
					new GerenciadorEmail(	new String[] {"Jesus Rodrigo","Marcio Mendon�a Gomes", "Leandro de Souza Bernardes","Massahide de Oliveira Namba","Keila Sousa Silva","Giuliano Silva Oliveira","infra Bd"}, 
							new String[] {"jrcorrea@tjgo.jus.br","mmgomes@tjgo.jus.br","lsbernardes@tjgo.jus.br","monamba_old@tjgo.jus.br","kssilva@tjgo.jus.br","gsilvaoliveira@tjgo.jus.br", "infra.bd@tjgo.jus.br"} ,
							"A data/hora do servidor de aplica��o est� diferente do banco de Dataguard", stMensagem1 ).start();
					boEsperar15Minutos = true;					
				}
			} catch (Exception e) {
				//se der erro foi no acesso ao banco assim n�o tem nada o que fazer
			}
			//se ja achou uma horario errado espera 15 minutos antes de verificar novamente
			if (boEsperar15Minutos) {										
				timer.schedule(new esperar15Minutos(), 900000);					
			}else {
				timer.schedule(new AcompanharDataHoraServidor(System.currentTimeMillis()), 60000);
			}						

		}
		
	}
	
	/**
	 * M�todo chamado quando a aplica��o � finalizada. Este m�todo � chamado
	 * antes dos m�todos de inicializa��o de qualquer Servlet ou Filter.
	 */
	public void contextDestroyed(ServletContextEvent servletEvent) {
		logger.info("*** FINALIZANDO SISTEMA PROJUDI ***");

		//shutdownTorque();

		logger.warn("*** SISTEMA PROJUDI FINALIZADO ***");
	}

	private void initProperties(String caminhoAplicacao) {
		
		//AddLicense.addLicense();

		try{
//			String projudiPropertiesPath = servletContext.getInitParameter(PROJUDI_PROPERTIES);
//			if (projudiPropertiesPath == null) {
//				logger.error("Erro ao carregar o atributo do nome do arquivo principal de configura��es definido no web.xml");
//			}

//			InputStream configStream = servletContext.getResourceAsStream(projudiPropertiesPath);
//			Properties projudiPropertiesConfiguration = new Properties();
//			projudiPropertiesConfiguration.load(configStream);

			ProjudiPropriedades projudiConfiguration = ProjudiPropriedades.getInstance();			
						
			projudiConfiguration.setCaminhoAplicacao(caminhoAplicacao + File.separator);

			//projudiConfiguration.setCertificadoEmissor(initCertificador());

			//@TODO pendencia: verifica��o dos tipos de arquivos v�lidos
			//if (projudiConfiguration.isVerificarTiposArquivos()) {
			//    TipoArquivoPeer tipoArquivoPeer = new TipoArquivoPeer();
			//    projudiConfiguration.setTiposArquivo(tipoArquivoPeer.listaTodosOsTiposOrdemAlfabetica());
			//}

			logger.info("FIM DE INICIALIZA��O DE PROPRIEDADES DO SISTEMA.\n");
			//} catch(TorqueException e) {
			//    logger.error("Erro ao acessar o banco de dados para buscar as propriedades", e);
		} catch(Exception e) {
		    e.printStackTrace();
		    logger.error(e.getMessage() +  "\n Erro ao carregar o arquivo principal de configura��es do Projudi", e);
			throw new RuntimeException("Local Exception "+ this.getClass().getName() +".initProperties(): " + e.toString());
		}
		
	}

	/*private X509Certificate initCertificador() {
		X509Certificate caCert = null;
		CertificadoNe certificadoNe = null;
		ProjudiPropriedades projudiConfiguration = ProjudiPropriedades.getInstance();
		try{
			certificadoNe = new CertificadoNe();
			CertificadoDt certificadoEmissor = certificadoNe.consultaCertificadoEmissorSistema();
			
			if (certificadoEmissor != null){
				ByteArrayInputStream bisPKCS12 = new ByteArrayInputStream(certificadoEmissor.getConteudo());
				KeyStore ksRoot = KeyStore.getInstance("PKCS12","BC");
				ksRoot.load(bisPKCS12, projudiConfiguration.getSenhaCertificadoEmissor().toCharArray());
				bisPKCS12.close();
				
				String alias = ksRoot.aliases().nextElement();
				caCert = (X509Certificate) ksRoot.getCertificate(alias);
			} else{
				logger.error("Local Exception: " + this.getClass().getName() + ".initCertificador(): Erro ao Carregar Certificado Emissor");
				throw new Exception("Local Exception: " + this.getClass().getName() + ".initCertificador(): Erro ao Carregar Certificado Emissor");
			}
		} catch(NoSuchAlgorithmException e) {
		    e.printStackTrace();
			logger.fatal(e);
			throw new RuntimeException("Local Exception "+ this.getClass().getName() +".initCertificador(): " + e.getMessage(), e);
		} catch(CertificateException e) {
		    e.printStackTrace();
			logger.fatal(e);
			throw new RuntimeException("Local Exception "+ this.getClass().getName() +".initCertificador(): " + e.getMessage(), e);
		} catch(KeyStoreException e) {
		    e.printStackTrace();
			logger.fatal(e);
			throw new RuntimeException("Local Exception "+ this.getClass().getName() +".initCertificador(): " + e.getMessage(), e);
		} catch(NullPointerException e) {
		    e.printStackTrace();
			logger.fatal(e.getMessage() + "\n Erro ao inicializar o certificador", e);
			throw new RuntimeException("Local Exception "+ this.getClass().getName() +".initCertificador(): " + e.getMessage(), e);
		} catch(Exception e) {
		    e.printStackTrace();
			//logger.fatal(e.getMessage() + "\n Erro ao inicializar o certificador", e);
			throw new RuntimeException("Local Exception "+ this.getClass().getName() +".initCertificador(): " + e.getMessage(), e);
		} 

		return caCert;
	}

	private void initFileUpload(X509Certificate caCert) {
		ProjudiPropriedades projudiConfiguration = ProjudiPropriedades.getInstance();
		try{
			FileUpload.init(new File(projudiConfiguration.getCaminhoCAConfiavel()), caCert, projudiConfiguration.getTamMaxArquivo(), projudiConfiguration.getTempDir());
		} catch(IOException e) {
			logger.error("Erro ao inicializar o FileUpload", e);
			throw new RuntimeException("Erro ao inicializar o FileUpload", e);
		} catch(GeneralSecurityException e) {
			logger.error("Erro ao inicializar o FileUpload", e);
			throw new RuntimeException("Erro ao inicializar o FileUpload", e);
		}
	}

	private void initUpTimeTest() {
        ProjudiPropriedades projudiConfiguration = ProjudiPropriedades.getInstance();
        try{
            upTime = Running.getInstance();
        } catch(UnknownHostException e) {
            logger.error("Erro ao inicializar o testador de uptime", e);
            throw new RuntimeException(e.toString());
        }
    }
	
	 private void initTorque(ServletContext servletContext) {
		String torquePropertiesPath = servletContext.getInitParameter(TORQUE_PROPERTIES);

		if (torquePropertiesPath == null) {
			logger.error("Erro ao carregar o atributo do nome do arquivo principal de configura��es definido no web.xml");
		}

		try{
			InputStream configStream = servletContext
					.getResourceAsStream(torquePropertiesPath);
			PropertiesConfiguration torquePropertiesConfiguration = new PropertiesConfiguration();
			torquePropertiesConfiguration.load(configStream);
			logger.info("Inicializando Torque...");
			Torque.init(torquePropertiesConfiguration);
		} catch(TorqueException e) {
			logger.error("Erro ao inicializar Torque", e);
			throw new RuntimeException(e.toString());
		} catch(ConfigurationException e) {
			logger.error("Erro ao inicializar arquivo de configura��es do Torque",e);
			throw new RuntimeException(e.toString());
		}
	}

	private void shutdownTorque() {
		try{
			logger.info("Finalizando Torque...");
			Torque.shutdown();
		} catch(TorqueException e) {
			logger.error("Erro ao finalizar torque", e);
		}
	}
	*/
	 
}