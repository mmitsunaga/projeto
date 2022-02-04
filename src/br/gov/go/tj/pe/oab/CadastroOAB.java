/**
 * ServiceSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.gov.go.tj.pe.oab;

import java.rmi.Remote;
import java.rmi.RemoteException;

interface CadastroOAB extends Remote {
	
	/**
	 * Retorna os dados do Advogado em formato XML - Parâmetro [cpf: CPF do Advogado]
	 */
	public String consultaAdvogadoPorCpf(String cpf) throws RemoteException;
	
	/**
	 * Retorna os dados do Advogado em formato XML - Parâmetros [inscricao: Número da Inscrição do Advogado, uf: Unidade Federativa do Advogado, nome: Nome do
	 * Advogado]
	 */
	public String consultaAdvogado(String inscricao, String uf, String nome) throws RemoteException;
	
	/**
	 * Retorna um array de bytes com a foto do Advogado - Parâmetro [numeroSeguranca: Número de Segurança do Advogado]
	 */
	public byte[] buscaImagemAdvogado(int numeroSeguranca) throws RemoteException;
}
