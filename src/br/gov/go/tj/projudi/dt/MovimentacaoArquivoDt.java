package br.gov.go.tj.projudi.dt;

import java.util.StringTokenizer;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.ValidacaoUtil;


public class MovimentacaoArquivoDt extends MovimentacaoArquivoDtGen {

    private static final long serialVersionUID = 8814464960042297879L;

    public static final int CodigoPermissao = 195;
    
	// Variáveis para controlar possíveis status de MovimentacaoArquivo
	public static final int NORMAL = 0;
	public static final int BLOQUEADO_POR_VIRUS = 1;
	public static final int RESTRICAO_DOWNLOAD = 2;
	public static final int OBJECT_STORAGE = 3;
	public static final int FILE_SYSTEM = 4;
	public static final int PJE_MIGRADO = 5;
	
	public static final int ACESSO_PUBLICO = 1;
	public static final int ACESSO_NORMAL = 2;
	public static final int ACESSO_SOMENTE_ADVS_DELEGACIA_MP_CARTORIO_MAGISTRADO = 3;
	public static final int ACESSO_SOMENTE_DELEGACIA_MP_CARTORIO_MAGISTRADO = 4;
	public static final int ACESSO_SOMENTE_MP_CARTORIO_MAGISTRADO = 5;
	public static final int ACESSO_SOMENTE_CARTORIO_MAGISTRADO = 6;
	public static final int ACESSO_SOMENTE_MAGISTRADO = 7;
	public static final int ACESSO_BLOQUEADO = 8;
	public static final int ACESSO_BLOQUEADO_VIRUS = 9;
	public static final int ACESSO_BLOQUEADO_ERRO_MIGRACAO = 10;

	

	private ArquivoDt arquivoDt;
	private MovimentacaoDt movimentacaoDt;
	private boolean valido; // Define se arquivo de uma movimentação é válido
	private String ArquivoTipoCodigo;
	private String hash;
	private String Recibo;
	private String ArquivoTipo;
	private String UsuarioAssinador;
	private String Id_MovimentacaoArquivoAcesso;
	private int AcessoArquivo;
	private int AcessoUsuario;	

	public void limpar() {
		valido = true;
		super.limpar();
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getArquivoTipoCodigo() {
		return ArquivoTipoCodigo;
	}
	
	public void setArquivoTipoCodigo(String arquivoTipoCodigo) {
		ArquivoTipoCodigo = arquivoTipoCodigo;
	}

	public ArquivoDt getArquivoDt() {
		return arquivoDt;
	}

	public void setArquivoDt(ArquivoDt arquivoDt) {
		this.arquivoDt = arquivoDt;
	}

	public MovimentacaoDt getMovimentacaoDt() {
		return movimentacaoDt;
	}

	public void setMovimentacaoDt(MovimentacaoDt movimentacaoDt) {
		this.movimentacaoDt = movimentacaoDt;
	}

	public boolean getValido() {
		return valido;
	}

	public void setValido(boolean valido) {
		this.valido = valido;
	}

	public void setId_ArquivoTipo(String valor) {
		ArquivoTipo = valor;		
	}
	
	private String getRecibo() {
		return Recibo;
	}

	private String getUsuarioAssinador() {
		return UsuarioAssinador;
	}

	private String getArquivoTipo() {
		return ArquivoTipo;
	}

	public void setArquivoTipo(String valor) {
		ArquivoTipo=valor;		
	}

	public String getUsuarioAssinadorFormatado() {
		
		// @*virgula@* -  escape para \, no CN do certificado. Como a ',' indica a divisão de campos, estava ocorrendo erro 
		StringTokenizer st = new StringTokenizer(getUsuarioAssinador().replace("\\,", "@*virgula@*"), ",");
		StringBuffer assinador = new StringBuffer("");
		boolean coAssinado = false;
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (token.startsWith("CN=")) {
				if (coAssinado) assinador.append("; ");
				assinador.append(token.substring(3));
				coAssinado = true;
			}
		}
		return assinador.toString().replace("@*virgula@*", ",");
	}
	
	public void setUsuarioAssinador(String valor) {
		UsuarioAssinador=valor;		
	}

	public void setRecibo(String valor) {
		Recibo = valor;		
	}
	
	public String GerarListaMovimentacaoCapaProcessoJSON(){
		StringBuilder stTemp= new StringBuilder();
		
		stTemp.append("{");
		stTemp.append("\"id\":\"").append(this.getId()).append("\",");
		stTemp.append("\"id_arquivo\":\"").append(this.getId_Arquivo()).append("\",");
		if (this.getNomeArquivo().length()>0) stTemp.append("\"nome_arquivo\":\"").append(this.getNomeArquivo()).append("\","); else stTemp.append("\"nome_arquivo\":\"ARQUIVO SEM NOME\","); 
		stTemp.append("\"id_arquivo_tipo\":\"").append(this.getArquivoTipoCodigo()).append("\",");
		stTemp.append("\"arquivo_tipo\":\"").append(this.getArquivoTipo()).append("\",");
		if (getUsuarioAssinador() != null && getUsuarioAssinador().length()>0)	{
			stTemp.append("\"usuario_assinador\":\"").append(this.getUsuarioAssinadorFormatado()).append("\","); 
		} else if (this.getCodigoTemp() != null && this.getCodigoTemp().equals(String.valueOf(MovimentacaoArquivoDt.OBJECT_STORAGE))) {
			if (this.isMidiaDigitalUpload()) {
				stTemp.append("\"usuario_assinador\":\"MÍDIA NÃO ASSINADA\",");
			} else {
				stTemp.append("\"usuario_assinador\":\"ARQUIVO ASSINADO FISICAMENTE\",");
			}				
		}else if (this.getCodigoTemp() != null && this.getCodigoTemp().equals(String.valueOf(MovimentacaoArquivoDt.PJE_MIGRADO))) {
			stTemp.append("\"usuario_assinador\":\"ASSINADO NO SISTEMA PJE\",");					
		} else {
			stTemp.append("\"usuario_assinador\":\"ARQUIVO NÃO ASSINADO\",");
		}
		stTemp.append("\"AcessoUsuario\":\"").append(this.getAcessoUsuario()).append("\",");
		stTemp.append("\"AcessoArquivo\":\"").append(this.getAcessoArquivo()).append("\",");
		stTemp.append("\"valido\":\"").append(this.getValido()).append("\",");
		stTemp.append("\"hash\":\"").append(this.getHash()).append("\",");
		stTemp.append("\"recibo\":\"").append(this.getRecibo()).append("\",");
		stTemp.append("\"codigo_temp\":\"").append(this.getCodigoTemp()).append("\"");
		
		stTemp.append("}");
		
		return stTemp.toString();		
	}

	public String getId_MovimentacaoArquivoAcesso() {
		// TODO Auto-generated method stub
		if (Id_MovimentacaoArquivoAcesso!=null){
			return Id_MovimentacaoArquivoAcesso;
		}
		return String.valueOf(MovimentacaoArquivoDt.ACESSO_NORMAL);
	}
	
	public void setId_MovimentacaoArquivoAcesso(String id_AcessoArquivoMovimentacao) {
		Id_MovimentacaoArquivoAcesso = id_AcessoArquivoMovimentacao;
	}

	public int getId_MovimentacaoArquivoAcessoToInt() {
		// TODO Auto-generated method stub
		return Funcoes.StringToInt(Id_MovimentacaoArquivoAcesso,-1);
	}

	public void setAcessoArquivo(int inAcessoArquivo) {
		AcessoArquivo = inAcessoArquivo;		
	}
	
	public void setAcessoUsuario(int inAcessoUsuario) {
		AcessoUsuario = inAcessoUsuario;		
	}
	public boolean isBloqueado(){
		return AcessoArquivo!=MovimentacaoArquivoDt.ACESSO_NORMAL;				 
	}	
	private int getAcessoArquivo() {
		return AcessoArquivo;
	}

	private int getAcessoUsuario() {
		return AcessoUsuario;
	}
	public boolean temAcessoUsuario(){
		return AcessoUsuario>=AcessoArquivo;				 
	}
	
	public boolean isFisico(){
		return (Funcoes.StringToLong(CodigoTemp, -100) == OBJECT_STORAGE);
	}
	
	public boolean isMidiaDigitalUpload() {
		return ArquivoTipoDt.isMidiaDigitalUpload(getArquivoTipoCodigo());
	}
	
	/**
	 * Método que verifica se o arquivo é do histórico físico, isto é, processo físico digitalizado.
	 * São exclusivamente do tipo PDF e estão no Storage
	 */
	public boolean isHistoricoFisico() {
		boolean isArquivoPdf = ValidacaoUtil.isNaoVazio(arquivoDt) ? arquivoDt.isArquivoPDF() : false;
		return isFisico() && isArquivoPdf;
	}

	public int getTipoAcesso(String tipoBloqueio) {
		int constanteAcesso = 0;
		switch (tipoBloqueio) {
		    case "Publico":  	constanteAcesso = ACESSO_PUBLICO;
		    	break;
		    case "Adv":		constanteAcesso = ACESSO_SOMENTE_ADVS_DELEGACIA_MP_CARTORIO_MAGISTRADO;
    			break;	
		    case "Delegacia":	constanteAcesso = ACESSO_SOMENTE_DELEGACIA_MP_CARTORIO_MAGISTRADO;
	    		break;
		    case "Mp": 			constanteAcesso = ACESSO_SOMENTE_MP_CARTORIO_MAGISTRADO;
		    	break;
			case "Cartorio": 	constanteAcesso = ACESSO_SOMENTE_CARTORIO_MAGISTRADO;
				break;
			case "Juiz":  		constanteAcesso = ACESSO_SOMENTE_MAGISTRADO;
				break;
			case "Global": 		constanteAcesso = ACESSO_BLOQUEADO;
				break;
		}
		return constanteAcesso;
	}
	
	public String getTipoAcesso(int tipoAcesso) {
		String constanteAcesso = "";
		switch (tipoAcesso) {
			case ACESSO_PUBLICO:
				constanteAcesso = "Arquivo Público";
			    	break;
			case ACESSO_NORMAL:
				constanteAcesso = "Arquivo Normal";
			    	break;
			case ACESSO_SOMENTE_ADVS_DELEGACIA_MP_CARTORIO_MAGISTRADO:
				constanteAcesso = "Visível aos Advs, Delegacia, MP, Cartório e Magistrado";
				break;
			case ACESSO_SOMENTE_DELEGACIA_MP_CARTORIO_MAGISTRADO:
				constanteAcesso = "Visível à Delegacia, MP, Cartório e Magistrado";
			    	break;
			case ACESSO_SOMENTE_MP_CARTORIO_MAGISTRADO:
				constanteAcesso = "Visível ao MP, Cartório e Magistrado";
			    	break;
			case ACESSO_SOMENTE_CARTORIO_MAGISTRADO:
				constanteAcesso = "Visível ao Cartório e Magistrado";
			    	break;
			case ACESSO_SOMENTE_MAGISTRADO:
				constanteAcesso = "Visível ao Magistrado";
			    	break;
			case ACESSO_BLOQUEADO:
				constanteAcesso = "Bloqueado";
			    	break;
			case ACESSO_BLOQUEADO_VIRUS:
				constanteAcesso = "Bloqueado Vírus";
			    	break;
			case ACESSO_BLOQUEADO_ERRO_MIGRACAO:
				constanteAcesso = "Bloqueado Erro Migração";
			    	break;
			default:
				 constanteAcesso = "Nível não definido";
		   
		}
		return constanteAcesso;
	}
	
}