package br.gov.go.tj.projudi.dt;

import java.util.List;

public class LogDt extends LogDtGen {

	/**
     * 
     */
    private static final long serialVersionUID = -5180505241513851974L;
	
    public static final int CodigoPermissao = 162;

	//Variáveis para auxiliar na consulta de logs
	private String dataInicial;
	private String dataFinal;
	private String Hash="";
	private long QtdErrosDia=0;
	private List listaLogData = null;
	private String nomeUsuario = "";
	private List<AtributoLogDt> listaAtributos;
	private String id_Pendencia;
	private String pendenciaTipo;

	/**
	 * Construtor
	 * 
	 * @param tabela
	 * @param id_usuario
	 * @param ipcomputador
	 * @param logtipo
	 * @param valoratual
	 * @param valornovo
	 */
	
	public LogDt(String tabela, String id_tabela, String id_usuario, String ipcomputador, String logTipoCodigo, String valoratual, String valornovo) {
		this.limpar();
		this.setTabela(tabela);
		this.setId_Usuario(id_usuario);
		this.setIpComputador(ipcomputador);
		this.setId_LogTipo(null);
		this.setLogTipoCodigo(logTipoCodigo);
		this.setValorAtual(valoratual);
		this.setValorNovo(valornovo);
		this.setId_Tabela(id_tabela);
	}
	
    /**
	 * Construtor
	 * 
	 * @param id_Usuario
	 * @param ipComputador
	 */
	public LogDt(String id_Usuario, String ipComputador) {
		this.limpar();
		this.setId_Usuario(id_Usuario);
		this.setIpComputador(ipComputador);

	}
	
	/**
	 * Construtor - Uso do campo código ao invés do ID
	 * 
	 * @param tabela
	 * @param id_tabela
	 * @param id_usuario
	 * @param ipcomputador
	 * @param logTipoCodigo
	 * @param valoratual
	 * @param valornovo
	 */
	public LogDt(String tabela, String id_tabela, String id_usuario, String ipcomputador, String logTipoCodigo, String valornovo) {
		this.limpar();
		this.setTabela(tabela);
		this.setId_Usuario(id_usuario);
		this.setIpComputador(ipcomputador);
		this.setId_LogTipo(null);
		this.setLogTipoCodigo(logTipoCodigo);
		this.setValorNovo(valornovo);
		this.setId_Tabela(id_tabela);
	}

	public LogDt() {
		this.limpar();
	}

	public void limpar(){
		super.limpar();
		dataFinal = "";
		dataInicial = "";
		listaLogData = null;
		nomeUsuario = "";
	}

	public String getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(String dataInicial) {
		if (dataInicial != null) this.dataInicial = dataInicial;
	}

	public String getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(String dataFinal) {
		if (dataFinal != null) this.dataFinal = dataFinal;
	}

    public String getId_Usuario() {
        
        return super.getId_UsuarioLog();
    }

    public String getIpComputador() {
        
        return super.getIpComputadorLog();
    }

    public void setId_Usuario(String usuario) {
        super.setId_UsuarioLog(usuario);        
    }

    public void setIpComputador(String ip) {
       super.setIpComputadorLog(ip);
        
    }

	public void addErro() {
		QtdErrosDia++;		
	}
	
	public long getQtdErrosDia(){
		return QtdErrosDia;
	}
	
	public void setHash(String hash){
		Hash = hash;
	}
	
	public String getHash(){
		return Hash;
	}

	public void setQtdErrosDia(long qtdErrosDia) {
		QtdErrosDia = qtdErrosDia;		
	}

	public List getListaLogData() {
		return listaLogData;
	}

	public void setListaLogData(List listaLogData) {
		this.listaLogData = listaLogData;
	}
	
	public String getNomeUsuario() {
		return nomeUsuario;
	}
	
	public void setNomeUsuario(String nomeUsuario) {
		if (nomeUsuario != "" && !nomeUsuario.equalsIgnoreCase("null")){
			this.nomeUsuario = nomeUsuario;
		}
	}

	public String gerarJSON() {
		StringBuilder stTemp = new StringBuilder();
					
		stTemp.append("[");
		stTemp.append("{\"id\":\"-50000\",\"desc1\":\"").append('1').append("\"},");
		stTemp.append("{\"id\":\"-60000\",\"desc1\":\"").append('1').append("\"},");					
		stTemp.append("{\"id\":\"").append(this.getId());
		stTemp.append("\",\"desc1\":\"").append(this.getData());
		stTemp.append("\",\"desc2\":\"").append(this.getData());
		stTemp.append("\",\"desc3\":\"").append(this.getHora());
		stTemp.append("\",\"desc4\":\"").append(this.getTabela());
		stTemp.append("\",\"desc5\":\"").append(this.getId_Tabela());

		stTemp.append("\"}");
		
		stTemp.append("]");
		
		return stTemp.toString();
	}
		
	public List<AtributoLogDt> getListaAtributos() {
		return listaAtributos;
	}

	public void setListaAtributos(List<AtributoLogDt> listaAtributos) {
		this.listaAtributos = listaAtributos;
	}

	public String getId_Pendencia() {
		return id_Pendencia;
	}

	public void setId_Pendencia(String id_Pendencia) {
		this.id_Pendencia = id_Pendencia;
	}

	public String getPendenciaTipo() {
		return pendenciaTipo;
	}

	public void setPendenciaTipo(String pendenciaTipo) {
		this.pendenciaTipo = pendenciaTipo;
	}
	
	public String getSomenteHorario(){
		String horario = getHora();
		if(horario != null && !horario.isEmpty()){
			try {
				horario = horario.substring(11,19);
			} catch (IndexOutOfBoundsException e){
				horario = "Inválido";
			}
		}
		return horario;
	}
	
	/**
	 * Método para destacar no texto do log o valor diferente do anterior.
	 * O algoritmo abaixo extrai o log em "chave:valor" e compara os valores das chaves. Caso o texto B tenha algum chave:valor diferente, então ele é adicionado ao seu 
	 * redor a tag label de amarelo para destacar.
	 * 
	 * @return String (getValorNovo() com o texto destacado)
	 * 
	 * @author fasoares
	 */
	public String mostrarDiferencaTextoLog() {
		String textoA = this.getValorAtual();
		String textoB = this.getValorNovo();
		String retorno = textoB;
		
		if( textoA != null && textoB != null ) {
			textoA = textoA.replace("[", "").replace("]", "");
			textoB = textoB.replace("[", "").replace("]", "");
			
			String[] vetorA = textoA.split(";");
			String[] vetorB = textoB.split(";");
			
			int cont = 0;
			
			for( String valorVetorA: vetorA ) {
				
				if( valorVetorA.contains(":") ) {
					valorVetorA += " ";
					String chaveA = valorVetorA.split(":")[0];
					String valorA = valorVetorA.split(":")[1].trim();
					
					for( String valorVetorB: vetorB ) {
						if( valorVetorB.contains(":") ) {
							valorVetorB += " ";
							String chaveB = valorVetorB.split(":")[0];
							String valorB = valorVetorB.split(":")[1].trim();
							
							if( chaveA.equalsIgnoreCase(chaveB) && !valorA.equalsIgnoreCase(valorB) ) {
								retorno = retorno.replace(chaveB + ":" + valorB, "<br/><label style='background-color:#FFFF00'>" + chaveB + ":" + valorB + "</label>");
							}
							
							//Dupliquei esse if para ficar claro que ele é para destacar a mensagem de vinculação realizada via ocorrência ou PROAD
							if(chaveB.equalsIgnoreCase("Motivo") && !valorB.equalsIgnoreCase("null")) {
								retorno = retorno.replace(chaveB + ":" + valorB, "<br/><label style='background-color:#FFFF00'>" + chaveB + ":" + valorB + "</label>");
							}
						}
					}
					
				}
			}
		}
		
		return retorno.replace(";", "; ");
	}
	
	/**
	 * Método criado para destacar chaves de valores de acordo que for precisando.
	 * 
	 * @return String
	 * 
	 * @author fasoares
	 */
	public String getValorNovoEspecial() {
		String valorNovo = this.getValorNovo();
		String retorno = valorNovo;
		
		if( valorNovo != null ) {
			valorNovo = valorNovo.replace("[", "").replace("]", "");
			String[] vetor = valorNovo.split(";");
			
			for( String valorVetor: vetor ) {
				if( valorVetor.contains(":") ) {
					valorVetor += " ";
					String chave = valorVetor.split(":")[0];
					String valor = valorVetor.split(":")[1].trim();
					if(chave.equalsIgnoreCase("Motivo") && !valor.equalsIgnoreCase("null")) {
						retorno = retorno.replace(chave + ":" + valor, "<br/><label style='background-color:#FFFF00'>" + chave + ":" + valor + "</label>");
					}
				}
			}
		}
		
		return retorno.replace(";", "; ");
	}
}
