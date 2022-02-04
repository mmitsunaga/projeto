package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.projudi.ne.GuiaEmissaoNe;
import br.gov.go.tj.utils.Funcoes;

public class GuiaItemDt extends GuiaItemDtGen {

	private static final long serialVersionUID = 8040545866883235327L;

	public static final int CodigoPermissao = 214222;
	
	private GuiaEmissaoDt guiaEmissaoDt;
	private CustaDt custaDt;
	private LocomocaoDt locomocaoDt;
	private String Id_ArrecadacaoCustaGenerica;
	private GuiaItemDt GuiaItemVinculadoDt;
	private String ValorCalculadoOriginal;
		
	public String getQuantidade() {
		if( super.getQuantidade() != null && super.getQuantidade().length() > 0 )
			return super.getQuantidade();
		else
			return "1";
	}
	
	public String getQuantidadeBanco() {
		if( super.getQuantidade() != null && super.getQuantidade().length() > 0 ) {
			if( super.getQuantidade().contains(GuiaEmissaoNe.LABEL_QUANTIDADE_DEPOSITARIO_ANOS) ) {
				String quant = super.getQuantidade();
				
				quant = quant.replace(GuiaEmissaoNe.LABEL_QUANTIDADE_DEPOSITARIO_ANOS, ",");
				quant = quant.replace(GuiaEmissaoNe.LABEL_QUANTIDADE_DEPOSITARIO_MESES, "");
				
				return quant;
			}
			return super.getQuantidade();
		}
		else
			return "1";
	}
	
	public String getValorReferencia() {
		if( super.getValorReferencia() != null && super.getValorReferencia().length() > 0 )
			return super.getValorReferencia();
		else
			return "0";
	}
	
	public GuiaEmissaoDt getGuiaEmissaoDt() {
		return guiaEmissaoDt;
	}

	public void setGuiaEmissaoDt(GuiaEmissaoDt guiaEmissaoDt) {
		this.guiaEmissaoDt = guiaEmissaoDt;
	}

	public CustaDt getCustaDt() {
		return custaDt;
	}

	public void setCustaDt(CustaDt custaDt) {
		this.custaDt = custaDt;
	}
	
	public String getId_ArrecadacaoCustaGenerica() {
	    return Id_ArrecadacaoCustaGenerica;
    }
	
	public void setId_ArrecadacaoCustaGenerica(String valor ) { 
		if (valor!=null) 
			if (valor.equalsIgnoreCase("null")) { 
				Id_ArrecadacaoCustaGenerica = "";
			}
			else if (!valor.equalsIgnoreCase("")) 
				Id_ArrecadacaoCustaGenerica = valor;
	}
	
	public void recalculeValor() { 
		Double valorRecalculado;	
		
		valorRecalculado = Funcoes.StringToDouble(this.getQuantidade()) * Funcoes.StringToDouble(this.getValorReferencia());
		
		this.setValorCalculado(Funcoes.retirarCasasDecimais(valorRecalculado).toString());
	}
	
	public LocomocaoDt getLocomocaoDt() {
		return locomocaoDt;
	}

	public void setLocomocaoDt(LocomocaoDt locomocaoDt) {
		this.locomocaoDt = locomocaoDt;
	}
	
	public GuiaItemDt getGuiaItemVinculadoDt() {
		return GuiaItemVinculadoDt;
	}

	public void setGuiaItemVinculadoDt(GuiaItemDt guiaItemVinculadoDt) {
		GuiaItemVinculadoDt = guiaItemVinculadoDt;
	}
	
	public void copiar(GuiaItemDt objeto){
		if (objeto==null) return;
		super.copiar(objeto);
		guiaEmissaoDt = objeto.getGuiaEmissaoDt();
		custaDt = objeto.getCustaDt();
		locomocaoDt = objeto.getLocomocaoDt();
		Id_ArrecadacaoCustaGenerica = objeto.getId_ArrecadacaoCustaGenerica();
		GuiaItemVinculadoDt = objeto.getGuiaItemVinculadoDt();	
		ValorCalculadoOriginal = objeto.getValorCalculadoOriginal();
	}
	
	public void limpar(){
		super.limpar();
		guiaEmissaoDt = null;
		custaDt = null;
		setLocomocaoDt(null);
		Id_ArrecadacaoCustaGenerica = "";
		GuiaItemVinculadoDt = null;
		ValorCalculadoOriginal = "";
	}
	
	public GuiaItemDt clonar() {
		GuiaItemDt objeto = new GuiaItemDt();
		objeto.copiar(this);
		objeto.setId("");
		if (objeto.getGuiaItemVinculadoDt() != null) objeto.setGuiaItemVinculadoDt(objeto.getGuiaItemVinculadoDt().clonar());
		if (objeto.getLocomocaoDt() != null) objeto.setLocomocaoDt(objeto.getLocomocaoDt().clonar());;
		return objeto;
	}
	
	public boolean isLocomocaoParaOficialDeJustica() {
		return (this.getCustaDt() != null && 
				 (
				   this.getCustaDt().getId().equals(String.valueOf(CustaDt.CUSTAS_LOCOMOCAO)) || 
				   this.getCustaDt().getId().equals(String.valueOf(CustaDt.LOCOMOCAO_PARA_OFICIAL)) || 
				   this.getCustaDt().getId().equals(String.valueOf(CustaDt.LOCOMOCAO_PARA_PENHORA)) || 
				   this.getCustaDt().getId().equals(String.valueOf(CustaDt.LOCOMOCAO_PARA_AVALIACAO))
				 )
			    );
	}
	
	public boolean isLocomocaoParaTribunal() {
		return (this.getCustaDt() != null && this.getCustaDt().getId().equals(String.valueOf(CustaDt.LOCOMOCAO_PARA_TRIBUNAL)));
	}
	
	public boolean isLocomocaoRegimento1074() {
		return (this.getCustaDt() != null && Funcoes.StringToInt(this.getCustaDt().getCodigoArrecadacao()) == CustaDt.REGIMENTO_LOCOMOCAO_TRIBUNAL);
	}
	
	public boolean isLocomocaoPenhoraRegimento1082() {
		return (this.getCustaDt() != null && Funcoes.StringToInt(this.getCustaDt().getCodigoArrecadacao()) == CustaDt.REGIMENTO_LOCOMOCAO_PENHORA);
	}
	
	public boolean isLocomocaoAvaliacaoRegimento1084() {
		return (this.getCustaDt() != null && Funcoes.StringToInt(this.getCustaDt().getCodigoArrecadacao()) == CustaDt.REGIMENTO_LOCOMOCAO_AVALIACAO);
	}
	
	public boolean isLocomocaoContaVinculada1058() {
		return (this.getCustaDt() != null && Funcoes.StringToInt(this.getCustaDt().getCodigoArrecadacao()) == CustaDt.REGIMENTO_LOCOMOCAO_CONTA_VINCULADA);
	}

	public String getValorCalculadoOriginal() {
		return ValorCalculadoOriginal;
	}

	public void setValorCalculadoOriginal(String valorCalculadoOriginal) {
		ValorCalculadoOriginal = valorCalculadoOriginal;
	}
	
	public String getValorAbatimento() {
		Double valorCalculadoOriginalItem = Funcoes.StringToDouble(this.getValorCalculadoOriginal());
		Double valorCalculadoItem = Funcoes.StringToDouble(this.getValorCalculado());
		Double valorAbatimento = valorCalculadoOriginalItem - valorCalculadoItem;
		
		return Funcoes.retirarCasasDecimais(valorAbatimento).toString();
	}
	
	public boolean isGuiaItemCustaGenerica() {
		if( this.getId_Custa() != null && this.getId_Custa().equals(String.valueOf(CustaDt.CUSTA_GENERICA)) ) {
			return true;
		}
		return false;
	}
	
	public boolean isGuiaItemDespesaPostal() {
		
		if( this.getCustaDt() != null && this.getCustaDt().getId() != null && !this.getCustaDt().getId().isEmpty() ) {
			String id = this.getCustaDt().getId();
			
			if( id.equals(String.valueOf(CustaDt.DESPESAS_POSTAIS_POR_POSTAGEM))
				|| id.equals(String.valueOf(CustaDt.TAXAS_DE_SERVICO_DESPESAS_POSTAIS_POR_POSTAGEM))
				//TODO Fred: Resolver no CustaDt: id custa antigo de despesa postal
				|| id.equals(String.valueOf(CustaDt.DESPESA_POSTAL_ANTIGO)) ) { 
				
				return true;
				
			}
		}
		
		return false;
	}
}
