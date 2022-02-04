package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.utils.Funcoes;

//---------------------------------------------------------
public class ServentiaRelacionadaDt extends ServentiaRelacionadaDtGen{

    
    /**
     * 
     */
    private static final long serialVersionUID = 489767483323803568L;
    public static final int CodigoPermissao = 143;
    
    private String Presidencia;
	private String RecebeProcesso;
	private String Id_ServentiaSubstituicao;
	private String ServentiaSubstituicao;
	private String DataInicialSubstituicao;
	private String DataFinalSubstituicao;
	private String OrdemTurmaJulgadora;
	private String Probabilidade;
	private String SubstitutoSegundoGrau;
	private String EstadoRepresentacao;
	private String ServentiaRelacaoCodigo;
	private boolean AtualizaPresidencia =false;
	
	
	public String getPresidencia() {return this.Presidencia;}
	public void setPresidencia(String valor) {if(valor != null) Presidencia = valor;}
	public String getRecebeProcesso() {return this.RecebeProcesso;}
	public void setRecebeProcesso(String valor) {if(valor != null) RecebeProcesso = valor;}	
	public String getId_ServentiaSubstituicao()  {return Id_ServentiaSubstituicao;}
	public void setId_ServentiaSubstituicao(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ServentiaSubstituicao = ""; ServentiaSubstituicao = "";}else if (!valor.equalsIgnoreCase("")) Id_ServentiaSubstituicao = valor;}
	public String getServentiaSubstituicao()  {return ServentiaSubstituicao;}
	public void setServentiaSubstituicao(String valor ) {if (valor!=null) ServentiaSubstituicao = valor;}	
	public String getDataInicialSubstituicao() {return this.DataInicialSubstituicao;}
	public void setDataInicialSubstituicao(String valor) {if(valor != null) DataInicialSubstituicao = valor;}
	public String getDataFinalSubstituicao() {return this.DataFinalSubstituicao;}
	public void setDataFinalSubstituicao(String valor) {if(valor != null) DataFinalSubstituicao = valor;}
	public String getProbabilidade() {return this.Probabilidade;}
	public void setProbabilidade(String valor) {if(valor != null) Probabilidade = valor;}
	
	public void atualizacaoPresidencia() {
		AtualizaPresidencia = true;
	}
	
	public boolean isAtualizacaoPresidencia() {
		return AtualizaPresidencia;
	}
	
	public String getProbabilidadeFormatada() {
//		float valor = Funcoes.FormatarDecimal(valor)StringToFloat(Probabilidade);
//		NumberFormat df = NumberFormat.getPercentInstance();
//		df.setMaximumFractionDigits(2);
		return Funcoes.FormatarDecimal(Probabilidade);
	}
	
	public String getServentiaRelacaoCodigo() {
		return ServentiaRelacaoCodigo;
	}

	public void setServentiaRelacaoCodigo(String serventiaRelacaoCodigo) {
		ServentiaRelacaoCodigo = serventiaRelacaoCodigo;
	}
	
	public boolean isPresidencia() {
		if (getPresidencia() != null && ( this.getPresidencia().equalsIgnoreCase("true") || this.getPresidencia().equalsIgnoreCase("1") || this.getPresidencia().equalsIgnoreCase("verdadeiro"))){
			return true;
		}
		return false;
	}
	
	public void copiar(ServentiaRelacionadaDt objeto){
		if (objeto==null) return;
		super.copiar(objeto);
		Presidencia = objeto.getPresidencia();
		RecebeProcesso = objeto.getRecebeProcesso();
		Id_ServentiaSubstituicao = objeto.getId_ServentiaSubstituicao();
		ServentiaSubstituicao = objeto.getServentiaSubstituicao();
		DataInicialSubstituicao = objeto.getDataInicialSubstituicao();
		DataFinalSubstituicao = objeto.getDataFinalSubstituicao();
		EstadoRepresentacao = objeto.getEstadoRepresentacao();
		ServentiaRelacaoCodigo = objeto.getServentiaRelacaoCodigo();
		Probabilidade = objeto.getProbabilidade();
		OrdemTurmaJulgadora = objeto.getOrdemTurmaJulgadora();
	}
	
	public void limpar(){
		super.limpar();
		Presidencia="";
		RecebeProcesso="";
		Id_ServentiaSubstituicao="";
		ServentiaSubstituicao="";
		DataInicialSubstituicao="";
		DataFinalSubstituicao="";
		OrdemTurmaJulgadora="";
	}   
	
    public String getPropriedades(){
		return "[Id_ServentiaRelacionada:" + getId() + ";ServentiaRelacionada:" + getServentiaRelacionada() + ";Id_ServentiaPrincipal:" + getId_ServentiaPrincipal() + ";ServentiaPrincipal:" + getServentiaPrincipal() + ";Id_ServentiaRelacao:" + getId_ServentiaRelacao() + ";ServentiaRelacao:" + getServentiaRelacao() + ";Id_ServentiaTipoRelacionada:" + getId_ServentiaTipoRelacionada() + ";ServentiaTipoCodigoRelacionada:" + getServentiaTipoCodigoRelacionada() + ";ServentiaTipoRelacionada:" + getServentiaTipoRelacionada() + ";CodigoTemp:" + getCodigoTemp() + ";Presidencia:" + Presidencia + ";RecebeProcesso:" + RecebeProcesso + ";Id_ServentiaSubstituicao:" + Id_ServentiaSubstituicao + ";ServentiaSubstituicao:" + ServentiaSubstituicao + ";DataInicialSubstituicao:" + DataInicialSubstituicao + ";DataFinalSubstituicao:" + DataFinalSubstituicao + "]";
	}
	public String getOrdemTurmaJulgadora() {
		return OrdemTurmaJulgadora;
	}
	public void setOrdemTurmaJulgadora(String ordemTurmaJulgadora) {
		OrdemTurmaJulgadora = ordemTurmaJulgadora;
	}
	
	public boolean isSubstitutoSegundoGrau() {
		if (this.Id_ServentiaSubstituicao != null && this.Id_ServentiaSubstituicao.trim().length() > 0) {
			return true;
		}
		return false;		
	}
	public boolean isGabineteDesembargadorServentiaRelacionada() {
		int codigo = Funcoes.StringToInt(this.getServentiaTipoCodigoRelacionada());
		if (codigo==ServentiaTipoDt.GABINETE){
    		return true;
    	}
    	
    	return false;
	}
	
	public void setSubstitutoSegundoGrau(String substitutoSegundoGrau) {
		SubstitutoSegundoGrau = substitutoSegundoGrau;		
	}
	
	public boolean isRecebeProcesso() {
		if (RecebeProcesso == null) return false;
		return (RecebeProcesso.equalsIgnoreCase("true") || RecebeProcesso.equalsIgnoreCase("1") || RecebeProcesso.equalsIgnoreCase("verdadeiro"));		
	}
	public void setEstadoRepresentacao(String valor) {
		EstadoRepresentacao = valor;
	}
	public String getEstadoRepresentacao() {
		return EstadoRepresentacao;
	}
	public boolean isMesmoId(String id_ServentiaRelacao) {
		if(getId()!=null && getId().equals(id_ServentiaRelacao)) {
			return true;
		}
		return false;
	}
	
	public boolean hasServentiaSubstituicao() {
		return (Id_ServentiaSubstituicao!=null && !Id_ServentiaSubstituicao.isEmpty());
	}
	public boolean isTemId() {
		// TODO Auto-generated method stub
		return (getId()!=null && !getId().isEmpty());
	}
}
