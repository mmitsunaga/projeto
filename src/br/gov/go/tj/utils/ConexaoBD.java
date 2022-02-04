package br.gov.go.tj.utils;

/*
 * ConexaoBD.java
 *
 * Created on 20 de Abril de 2005, 10:42
 */
 
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import br.gov.go.tj.projudi.Projudi;

//package persistencia;

//Seria interessante buscar os parametros de conexão em uma arquivo.

public class ConexaoBD implements Serializable{
    
	private static final long serialVersionUID = 3493591364398902141L;
	private static DataSource dsPersistencia;
	private static DataSource dsConsulta;
	private static DataSource dsAdabas;
	private static DataSource dsAdabasProcessos;
	private static DataSource dsPJE;
	private static DataSource dsDRS3;
	private static DataSource dsDRS2;
    private static Logger logger = Logger.getLogger(Projudi.class);
    private static String urlAdabas;
    private static String usuarioAdabas;
    private static String senhaAdabas;
    private static String urlAdabasProcessos;
    private static String usuarioAdabasProcessos;
    private static String senhaAdabasProcessos;
    
    public static void setDatasourcePersistencia(DataSource ds) {
      ConexaoBD.dsPersistencia = ds;
      logger.info("Set Conection OK");
    }

    public static Connection getConexaoPersistencia() throws Exception{
        Connection con = null;            	
    	
    	try{
			con = dsPersistencia.getConnection();
			
			return   con;
		} catch(Exception e) {
						
			logger.error("get Conection Erro" + Funcoes.obtenhaConteudoExcecao(e), e);
			throw e;
		}

    }
    
    public static Connection getConexaoConsulta() throws Exception{
        Connection con = null;            	
    	
    	try{
			con = dsConsulta.getConnection();
			
			////System.out.println("get Conection OK");
			
			return   con;
		} catch(Exception e) {
						
			logger.error("get Conection Erro" + Funcoes.obtenhaConteudoExcecao(e), e);
			throw e;
		}

    }
    
    public static Connection getConexaoAdabas() throws MensagemException, Exception {
        Connection con = null;            	
    	
    	try{
    		if (dsAdabas==null && urlAdabas==null){
    			
    			Projudi.iniciarPoolADABAS();
    		
    		} else if (urlAdabas != null) {
    			
    			con = DriverManager.getConnection(urlAdabas, usuarioAdabas, senhaAdabas);
    			
    			return con;
    		}
    		
    		if (dsAdabas==null){
    			throw new MensagemException("Não é possivel conectar ao ADABAS (SPG)");
    		}
    		
			con = dsAdabas.getConnection();
			
			////System.out.println("get Conection OK");
			
			return con;
    	} catch(Exception e) {
						
			logger.error("get Conection Adabas Erro" + Funcoes.obtenhaConteudoExcecao(e), e);
			throw e;
		}

    }
    
    public static Connection getConexaoAdabasProcessos() throws MensagemException, Exception {
        Connection con = null;            	
    	
    	try{
    		if (dsAdabasProcessos==null && urlAdabasProcessos==null){
    			
    			Projudi.iniciarPoolADABASProcessos();
    		
    		} else if (urlAdabasProcessos != null) {
    			
    			con = DriverManager.getConnection(urlAdabasProcessos, usuarioAdabasProcessos, senhaAdabasProcessos);
    			
    			return con;
    		}
    		
    		if (dsAdabasProcessos==null){
    			throw new MensagemException("Não é possivel conectar ao ADABAS PROCESSOS (SPG)");
    		}
    		
			con = dsAdabasProcessos.getConnection();
			
			////System.out.println("get Conection OK");
			
			return con;
    	} catch(Exception e) {
						
			logger.error("get Conection Adabas Processos Erro" + Funcoes.obtenhaConteudoExcecao(e), e);
			throw e;
		}

    }
    
    public static Connection getConexaoPJE() throws MensagemException, Exception {
        Connection con = null;            	
    	
    	try{
    		if (dsPJE==null){
    			Projudi.iniciarPoolPJE();
    		}
    		if (dsPJE==null){
    			throw new MensagemException("Não é possivel conectar ao PJE");
    		}
			con = dsPJE.getConnection();
			org.postgresql.Driver dr;
			
			////System.out.println("get Conection OK");
			
			return con;
    	} catch(Exception e) {
						
			logger.error("get Conection PJE Erro" + Funcoes.obtenhaConteudoExcecao(e), e);
			throw e;
		}

    }
    
    public static Connection getConexaoDRS3() throws Exception{
        Connection con = null;            	
    	
    	try{
    		if (dsDRS3==null){
    			Projudi.iniciarPoolDRS3();
    		}
    		if (dsDRS3==null){
    			throw new MensagemException("Não é possivel conectar ao DRS3");
    		}
			con = dsDRS3.getConnection();
			
			return   con;
		} catch(Exception e) {
						
			logger.error("get Conection Erro" + Funcoes.obtenhaConteudoExcecao(e), e);
			throw e;
		}

    }
    
    public static Connection getConexaoDRS2() throws Exception{
        Connection con = null;            	
    	
    	try{
    		if (dsDRS2==null){
    			Projudi.iniciarPoolDRS2();
    		}
    		if (dsDRS2==null){
    			throw new MensagemException("Não é possivel conectar ao DRS2");
    		}
			con = dsDRS2.getConnection();
			
			return   con;
		} catch(Exception e) {
						
			logger.error("get Conection Erro" + Funcoes.obtenhaConteudoExcecao(e), e);
			throw e;
		}

    }
    
	public static void setDatasourceConsulta(DataSource dataSource) {
		dsConsulta = dataSource;
		logger.info("Set Conection CONSULTA OK");
	}
	
	public static void setDatasourceAdabas(DataSource dataSource) {
		dsAdabas = dataSource;
		logger.info("Set Conection ADABAS OK");
	}
	
	public static void setDatasourceAdabasProcessos(DataSource dataSource) {
		dsAdabasProcessos = dataSource;
		logger.info("Set Conection ADABAS PROCESSOS OK");
	}
	
	public static void setDatasourcePJE(DataSource dataSource) {
		dsPJE = dataSource;
		logger.info("Set Conection PJE OK");
	}
	
	public static void setDatasourceDRS3(DataSource dataSource) {
		dsDRS3 = dataSource;
		logger.info("Set Conection DRS3 OK");
	}
	
	public static void setDatasourceDRS2(DataSource dataSource) {
		dsDRS2 = dataSource;
		logger.info("Set Conection DRS2 OK");
	}
	
	public static void setUrlAdabas(String urlAdabasStr, String usuarioAdabasStr, String senhaAdabasStr) {
		urlAdabas = urlAdabasStr;
		usuarioAdabas = usuarioAdabasStr;
		senhaAdabas = senhaAdabasStr;
		logger.info("Set URL ADABAS OK");
	}
	
	public static void setUrlAdabasProcessos(String urlAdabasStr, String usuarioAdabasStr, String senhaAdabasStr) {
		urlAdabasProcessos = urlAdabasStr;
		usuarioAdabasProcessos = usuarioAdabasStr;
		senhaAdabasProcessos = senhaAdabasStr;
		logger.info("Set URL ADABAS PROCESSOS OK");
	}
}
