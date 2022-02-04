package br.gov.go.tj.utils;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import br.gov.go.tj.projudi.Projudi;

public class FabricaConexao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6488771271859625968L;
	private Connection conexao = null;
	private boolean boTransacao = false;
	public static final int PERSISTENCIA 	= 1;
	public static final int CONSULTA 		= 2;
	public static final int ADABAS 			= 3;
	public static final int PJE 			= 4;
	public static final int DRS3 			= 5;
	public static final int ADABASPROCESSOS = 6;
	public static final int DRS2 			= 7;
	
	private static Logger logger = Logger.getLogger(Projudi.class);
	
	public void setConexao(Connection connection) {

		conexao = connection;
	}

	public Connection getConexao() {

		return conexao;
	}
	
	public FabricaConexao(int tipo) throws  Exception{
		//usando um único servidor
		//conexao = ConexaoBD.getConexao();
		//para usar um servidor separado para as consultas
		switch(tipo){
			case FabricaConexao.PERSISTENCIA:
				conexao = ConexaoBD.getConexaoPersistencia();
				break;
			case FabricaConexao.CONSULTA:			
				conexao = ConexaoBD.getConexaoConsulta();
				break;
			case FabricaConexao.ADABAS:
				conexao = ConexaoBD.getConexaoAdabas();
				break;
			case FabricaConexao.PJE:
				conexao = ConexaoBD.getConexaoPJE();
				break;
			case FabricaConexao.DRS3:
				conexao = ConexaoBD.getConexaoDRS3();
				break;
			case FabricaConexao.ADABASPROCESSOS:
				conexao = ConexaoBD.getConexaoAdabasProcessos();
				break;
			case FabricaConexao.DRS2:
				conexao = ConexaoBD.getConexaoDRS2();
				break;
		}
		 
	}

	public void fecharConexao() throws Exception{		
		if (!conexao.isClosed()) {
			if (boTransacao) {
				cancelarTransacao();
			}
			conexao.close();
		}
		conexao = null;
	}

	public void finalize() throws Exception {
	    try{
	        if (conexao!=null)
	            if (!conexao.isClosed()){
	                conexao.close();
	                conexao = null;
	            }
        } catch(Exception e) {
            //em último caso
        }
	}
	
	public void iniciarTransacao() throws Exception{
		conexao.setAutoCommit(false);
		boTransacao  = true;
	}

	public void finalizarTransacao() throws Exception{
		conexao.commit();
		boTransacao = false;					
	}

	public void cancelarTransacao() throws Exception{
		conexao.rollback();
		boTransacao = false;
	}
	
	public boolean isTransacaoIniciada() {
		return boTransacao;
	}
	
	public static FabricaConexao criarConexaoPersistencia() throws PersistenciaException {
		return criarConexao(FabricaConexao.PERSISTENCIA);
	}
	
	public static FabricaConexao criarConexaoConsulta() throws PersistenciaException {
		return criarConexao(FabricaConexao.CONSULTA);
	}
	
	public static FabricaConexao criarConexaoAdabas() throws PersistenciaException {
		return criarConexao(FabricaConexao.ADABAS);
	}
	
	public static FabricaConexao criarConexaoPJE() throws PersistenciaException {
		return criarConexao(FabricaConexao.PJE);
	}
	
	public static FabricaConexao criarConexaoDRS3() throws PersistenciaException {
		return criarConexao(FabricaConexao.DRS3);
	}
	
	public static FabricaConexao criarConexaoDRS2() throws PersistenciaException {
		return criarConexao(FabricaConexao.DRS2);
	}
	
	public static FabricaConexao criarConexaoAdabasDataSourceProcessos() throws PersistenciaException {
		return criarConexao(FabricaConexao.ADABASPROCESSOS);
	}
	
	public static FabricaConexao criarConexao(int tipo) throws PersistenciaException {
		try {
			return new FabricaConexao(tipo);
		} catch (Exception e) {
			throw new PersistenciaException("Impossível criar conexão!", e);
		}
	}
	
	public static void fecharConexao(FabricaConexao conexao) {
		try {
			if (conexao != null) conexao.fecharConexao();
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	public static void iniciarTransacao(FabricaConexao conexao) throws PersistenciaException {
		try {
			validarEstadoConexao(conexao, "iniciar");	
			try {
				if (!conexao.getConexao().getAutoCommit()) throw new PersistenciaException("Impossível iniciar transação, já existe uma transação iniciada para essa conexão!");
			} catch (SQLException e) {
				throw new PersistenciaException("Impossível iniciar transação, não foi possível identificar o estado da transação!", e);	
			}
			try {
				conexao.iniciarTransacao();
			} catch (Exception e) {
				throw new PersistenciaException("Erro ao iniciar transação!", e);	
			}
		} catch (PersistenciaException e) {
			logger.error(e);
			throw e;
		}		
	}
	
	public static void finalizarTransacao(FabricaConexao conexao) throws PersistenciaException {
		try {
			validarEstadoConexao(conexao, "finalizar");	
			if (!conexao.isTransacaoIniciada()) throw new PersistenciaException("Impossível finalizar transação, não existe uma transação iniciada para essa conexão!");
			try {
				conexao.finalizarTransacao();
			} catch (Exception e) {
				throw new PersistenciaException("Erro ao finalizar transação!", e);	
			}
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
	}
	
	public static void cancelarTransacao(FabricaConexao conexao) {
		try {
			validarEstadoConexao(conexao, "cancelar");		
			if (!conexao.isTransacaoIniciada()) throw new PersistenciaException("Impossível cancelar transação, não existe uma transação iniciada para essa conexão!");
			try {
				conexao.cancelarTransacao();
			} catch (Exception e) {
				throw new PersistenciaException("Erro ao cancelar transação!", e);	
			}
		} catch (PersistenciaException e) {
			logger.error(e);
		}	
	}
	
	private static void validarEstadoConexao(FabricaConexao conexao, String acao) throws PersistenciaException {
		if (conexao == null) throw new PersistenciaException(String.format("Impossível %s transação, a fabrica de conexão está nula!", acao));
		if (conexao.getConexao() == null) throw new PersistenciaException(String.format("Impossível %s transação, a conexão está nula!", acao));
		try {
			if (conexao.getConexao().isClosed()) throw new PersistenciaException(String.format("Impossível %s transação, a conexão está fechada!", acao));
		} catch (SQLException e) {
			throw new PersistenciaException(String.format("Impossível %s transação, não foi possível identificar o estado da conexão!", acao), e);		
		}	
	}
}
