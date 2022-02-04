/*
 * Created on 11 f�vr. 2005
 *
 * Este arquivo � parte do Projudi. 
 * N�o pode ser distribu�do.
 */
package br.gov.go.tj.projudi.util;


/**
 * @author Andr� Luis C. Moreira e Leandro Lima Lira
 * Esta classe tem a fun��o de auxiliar na montagem do certificado digital gerado 
 * pelo sistema para seus usu�rios. Uma das fun��es da classe � retirar acentua��o dos 
 * valores do usu�rio(nome, endere�o etc), haja vista que dentro do certificado estes
 * valores ficar�o sem acentua��o, conforme padr�o de certifica��o icp-brasil.
 * 
 */
public class Identidade {

	String senha,
	       csenha,
	       pais,
	       estado,
	       cidade,
	       nome,
	       email;
	
	boolean pessoaFisica = false;
	String dataNascimento;
	String CPF;
	String NIS;
	String RG;
	String RGExpedidor;
	String RGExpedidorUF;
	String INSS;
	
	/**
	 * M�todo que valida os campos digitados pelo usu�rio para a gera��o 
	 * de um certificado digital
	 * @return Vector contendo mensagens para serem exibidas ao usu�rio, em caso de incoer�ncia
	 * das informa��es. 
	 */
	public String validaCampos()  {
		String mensagem = "";
		if(senha != null && senha.length() < 8) mensagem += "A senha deve ter no m�nimo 8 caracteres ";
		if(!(senha != null && csenha != null && senha.equals(csenha))) mensagem += "Senha n�o confere. ";		
		return mensagem;							
	}

	/**
	 * M�todo que retorna a cidade, sem acentua��o
	 * @return A cidade do usu�rio, sem acentua��o
	 */
	public String getCidade() {
		return retiraAcentos(cidade);
	}

	/**
	 * M�todo que retorna a confirma��o de senha digitada pelo usu�rio
	 * @return A confirma��o de senha digitada pelo usu�rio
	 */
	public String getCsenha() {
		return csenha;
	}

	/**
	 * M�todo que retorna o e-mail do usu�rio
	 * @return O e-mail do usu�rio
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * M�todo que retorna o Estado(UF) do usu�rio, sem acentua��o 
	 * @return O Estado (UF) do usu�rio, sem acentua��o 
	 */
	public String getEstado() {
		return retiraAcentos(estado);
	}

	/**
	 * M�todo que retorna o nome do usu�rio, sem acentua��o
	 * @return O nome do usu�rio, sem acentua��o
	 */
	public String getNome() {
		return retiraAcentos(nome);
	}

	/**
	 * M�todo que retorna o pa�s do usu�rio, sem acentua��o
	 * @return O pa�s do usu�rio, sem acentua��o
	 */
	public String getPais() {
		return retiraAcentos(pais);
	}

	/**
	 * M�todo que retorna a senha digitada pelo usu�rio.
	 * @return A senha digitada pelo usu�rio.
	 */
	public String getSenha() {
		return senha;
	}

	/**
	 * M�todo que seta a cidade
	 * @param string Cidade.
	 */
	public void setCidade(String string) {
		cidade = string;
	}

	/**
	 * M�todo que seta a confirma��o de senha digitada pelo usu�rio
	 * @param string COnfirma��o de senha
	 */
	public void setCsenha(String string) {
		csenha = string;
	}

	/**
	 * M�todo que seta o e-mail que ir� para o certificado
	 * @param string O e-mail que ficar� no certificado
	 */
	public void setEmail(String string) {
		email = string;
	}

	/**
	 * M�todo que seta o Estado (UF) que ficar� no certificado
	 * @param string O Estado (UF) que ficar� no certificado
	 */
	public void setEstado(String string) {
		estado = string;
	}

	/**
	 * M�todo que seta o nome que ficar� no certificado
	 * @param string O nome que ficar� no certificado
	 */
	public void setNome(String string) {
		nome = string;
	}

	/**
	 * M�todo que seta o pa�s do usu�rio, que ficar� no certificado 
	 * @param string O pa�s do usu�rio, que ficar� no certificado
	 */
	public void setPais(String string) {
		pais = string;
	}

	/**
	 * M�todo que seta a senha do usu�rio, que ficar� no certificado de forma criptografada.
	 * @param string A senha digitada pelo usu�rio para a chave privada do certificado
	 */
	public void setSenha(String string) {
		senha = string;
	}

	/**
	 * M�todo que retira acetua��o de uma string
	 * @param stringOrigem String cuja acentua��o se deseja retirar
	 * @return A string sem acentua��o
	 */
	private String retiraAcentos(String stringOrigem) {
		String destino = stringOrigem.replaceAll("[����]", "a");
		destino = destino.replaceAll("[����]","A");
		destino = destino.replaceAll("[���]", "e");
		destino = destino.replaceAll("[���]","E");
		destino = destino.replaceAll("[�����]", "o");
		destino = destino.replaceAll("[����]","O");
		destino = destino.replaceAll("[���]", "u");
		destino = destino.replaceAll("[���]","U");
		destino = destino.replaceAll("�","c");
		destino = destino.replaceAll("�","C");
		return destino.toUpperCase();		
	}

	/**
	 * M�todo que retorna se se trata de pessoa f�sica ou jur�ica  
	 * @return True caso se trate de pessoa f�sica, false caso contr�rio
	 */
	public boolean ehPessoaFisica() {
		return pessoaFisica;
	}
	
	/**
	 * M�todo que seta que � pessoa f�sica
	 *
	 */
	public void setEhPessoaFisica() {
		pessoaFisica = true;
	}

	/**
	 * M�todo que seta a data de nascimento do usu�rio da identifica��o
	 * @param date Data de nascimento do usu�rio  
	 */
	public void setDataNascimento(String date) {
		String stRetorno = "";
		if (date != null && date != "") 
			stRetorno = date.substring(8, 10) + date.substring(5, 7)+ date.substring(0, 4);
		this.dataNascimento = stRetorno;
	}

	/**
	 * M�todo que retorna a data de nascimento, atualizando os dados para o formato
	 * GragorianCalendar, atrav�s do qual o primeiro M�s tem c�digo 0 ao inv�s de 1, e 
	 * o �ltimo m�s t�m c�digo 11 ou inv�s de 12. 
	 * @return String dia+mes+ano da data de nascimento do usu�rio 
	 */
	public String getDataNascimento() {
		// GregorianCalendar calenGreg = new GregorianCalendar();
		// calenGreg.setTime(dataNascimento);
		// String ano = ""+calenGreg.get(GregorianCalendar.YEAR);
		// String mes = "" + (calenGreg.get(GregorianCalendar.MONTH) + 1);
		// String dia = "" + calenGreg.get(GregorianCalendar.DAY_OF_MONTH);
		// if(mes.length()==1) mes= "0"+mes;
		// if(dia.length() == 1) dia = "0"+dia;
		//		
		// String retorno = dia+mes+ano;
		 if (dataNascimento.length() != 8)
			throw new RuntimeException("<{Ocorreu um erro na gera��o da data de nascimento: dataNascimento}> Local Exception: " + this.getClass().getName() + ".getDataNascimento()");
		return dataNascimento;
	}

	/**
	 * M�todo que retorna o CPF do su�rio
	 * @return 
	 */
	public String getCPF() {
		if(CPF.length()!=11) throw new RuntimeException("<{Tamanho da string CPF incorreto.}> Local Exception: " + this.getClass().getName() + ".getCPF()");		
		return CPF;
	}

	public void setCPF(String string) {
		if(string == null) throw new IllegalArgumentException("<{CPF n�o pode estar em branco(null)}> Local Exception: " + this.getClass().getName() + ".setCPF()");		
		CPF = string;
	}

	public String getNIS() {
		if(NIS==null) return "00000000000";
		String retorno = completaZerosEsquerda(NIS,11);
		if(retorno.length()!=11) throw new RuntimeException("<{Tamanho da string NIS incorreto.}> Local Exception: " + this.getClass().getName() + ".getNIS()");
		return retorno;
	}

	public void setNIS(String string) {
		NIS = string;
	}
	
	public void setRG(String string) {
		if(string == null) throw new IllegalArgumentException("<{RG n�o pode estar em branco(null)}> Local Exception: " + this.getClass().getName() + ".setRG()");
		if (string.length()>15)
			RG = string.substring(0, 15);
		else
			RG = string;
	}

	public String getRG() {
		String retorno = completaZerosEsquerda(RG,15);
		//if(retorno.length()!=11) throw new RuntimeException("Tamanho da string RG incorreto.");
		return retorno;
	}

	public void setRGExpedidor(String string) {
		if(string == null) throw new IllegalArgumentException("<{RGExpedidor n�o pode estar em branco(null)}> Local Exception: " + this.getClass().getName() + ".setRGExpedidor()");
		RGExpedidor = string;
	}

	public void setRGExpedidorUF(String string) {
		if(string == null) throw new IllegalArgumentException("<{RGExpedidorUF n�o pode estar em branco(null)}> Local Exception: " + this.getClass().getName() + ".setRGExpedidorUF()");
		RGExpedidorUF = string;
	}

	public String getRGOrgaoExpedidorUF() {
		String retorno = "";
		if (RGExpedidor.length()>4)
			retorno = RGExpedidor.substring(0, 4) + RGExpedidorUF;
		else
			retorno = RGExpedidor + RGExpedidorUF;
		
		if(retorno.length()>6) 
			throw new RuntimeException("<{Tamanho da string RG Expedidor incorreto.}> Local Exception: " + this.getClass().getName() + ".getRGOrgaoExpedidorUF()");
		
		return retorno;
	}

	public String getINSS() {
		if(INSS==null) return "000000000000";
		String retorno = completaZerosEsquerda(INSS,12);
		if(retorno.length()!=12) throw new RuntimeException("<{Tamanho da string INSS incorreto.}> Local Exception: " + this.getClass().getName() + ".getINSS()");
		return retorno;
	}

	public void setINSS(String string) {
		INSS = string;
	}


	private String completaZerosEsquerda(String origem, int tam) {
		if(origem.length() >= tam) return origem;
		return completaZerosEsquerda("0"+origem,tam);
	}


}
