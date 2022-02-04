package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.projudi.ne.GuiaEmissaoNe;
import br.gov.go.tj.projudi.ne.GuiaLocomocaoNe;
import br.gov.go.tj.utils.Funcoes;

public class LocomocaoDt extends Dados {

	private static final long serialVersionUID = 4307039062707247591L;
	
	public static final int CodigoPermissao = 214222;
	
	private String Id_Locomocao;
	private String Id_Guia_Emissao;
	private GuiaItemDt GuiaItemDt;
	private GuiaItemDt GuiaItemContaVinculadaDt;
	private GuiaItemDt GuiaItemSegundoDt;
	private GuiaItemDt GuiaItemTerceiroDt;
	private BairroDt BairroDt;
	private ZonaDt ZonaDt;
	private RegiaoDt RegiaoDt;
	private MandadoJudicialDt MandadoJudicialDt;
	private String Id_GuiaEmissaoComplementar;
	private String CodigoTemp;
	private String CodigoOficialSPG;
	private int quantidadeAcrescimo;
	private boolean citacaoHoraCerta;
	private boolean foraHorarioNormal;
	
	//leandro**********************************
	private int  qtdLocomocao;
	private int finalidadeCodigo;
	private boolean penhora;
	private boolean intimacao;
	private boolean oficialCompanheiro;
	//*******************************************

	private LocomocaoSPGDt locomocaoSPGDt; 	//Utilizada apenas pela central de mandado do SPG

	@Override
	public String getId() {
		return Id_Locomocao;
	}

	@Override
	public void setId(String id) {
		this.Id_Locomocao = id;
	}
	
	public String getId_Guia_Emissao() {
		return Id_Guia_Emissao;
	}

	public void setId_Guia_Emissao(String id_Guia_Emissao) {
		Id_Guia_Emissao = id_Guia_Emissao;
	}

	public GuiaItemDt getGuiaItemDt() {
		return GuiaItemDt;
	}

	public void setGuiaItemDt(GuiaItemDt guiaItemDt) {
		this.GuiaItemDt = guiaItemDt;
	}

	public BairroDt getBairroDt() {
		return BairroDt;
	}

	public void setBairroDt(BairroDt bairroDt) {
		this.BairroDt = bairroDt;
	}

	public RegiaoDt getRegiaoDt() {
		return RegiaoDt;
	}

	public void setRegiaoDt(RegiaoDt regiaoDt) {
		this.RegiaoDt = regiaoDt;
	}

	public ZonaDt getZonaDt() {
		return ZonaDt;
	}

	public void setZonaDt(ZonaDt zonaDt) {
		this.ZonaDt = zonaDt;
	}
	
	public GuiaItemDt getGuiaItemContaVinculadaDt() {
		return GuiaItemContaVinculadaDt;
	}

	public void setGuiaItemContaVinculadaDt(GuiaItemDt guiaItemContaVinculadaDt) {
		GuiaItemContaVinculadaDt = guiaItemContaVinculadaDt;
	}

	public GuiaItemDt getGuiaItemSegundoDt() {
		return GuiaItemSegundoDt;
	}

	public void setGuiaItemSegundoDt(GuiaItemDt guiaItemSegundoDt) {
		GuiaItemSegundoDt = guiaItemSegundoDt;
	}

	public GuiaItemDt getGuiaItemTerceiroDt() {
		return GuiaItemTerceiroDt;
	}

	public void setGuiaItemTerceiroDt(GuiaItemDt guiaItemTerceiroDt) {
		GuiaItemTerceiroDt = guiaItemTerceiroDt;
	}

	public MandadoJudicialDt getMandadoJudicialDt() {
		return MandadoJudicialDt;
	}

	public void setMandadoJudicialDt(MandadoJudicialDt mandadoJudicialDt) {
		this.MandadoJudicialDt = mandadoJudicialDt;
	}
	
	public String getId_GuiaEmissaoComplementar() {
		return Id_GuiaEmissaoComplementar;
	}

	public void setId_GuiaEmissaoComplementar(String id_GuiaEmissaoComplementar) {
		Id_GuiaEmissaoComplementar = id_GuiaEmissaoComplementar;
	}
	
	public String getCodigoTemp() {
		return CodigoTemp;
	}

	public void setCodigoTemp(String codigoTemp) {
		this.CodigoTemp = codigoTemp;
	}
	
	public String getCodigoOficialSPG() {
		return CodigoOficialSPG;
	}

	public void setCodigoOficialSPG(String codigoOficialSPG) {
		CodigoOficialSPG = codigoOficialSPG;
	}	
	
	public int getQuantidadeAcrescimo() {
		return quantidadeAcrescimo;
	}

	public void setQuantidadeAcrescimo(int quantidadeAcrescimo) {
		this.quantidadeAcrescimo = quantidadeAcrescimo;
	}

	public boolean isCitacaoHoraCerta() {
		return citacaoHoraCerta;
	}

	public void setCitacaoHoraCerta(boolean citacaoHoraCerta) {
		this.citacaoHoraCerta = citacaoHoraCerta;
	}

	public boolean isForaHorarioNormal() {
		return foraHorarioNormal;
	}

	public void setForaHorarioNormal(boolean foraHorarioNormal) {
		this.foraHorarioNormal = foraHorarioNormal;
	}

	public int getFinalidadeCodigo() {
		return finalidadeCodigo;
	}

	public void setFinalidadeCodigo(int finalidadeCodigo) {
		this.finalidadeCodigo = finalidadeCodigo;
	}

	public boolean isPenhora() {
		return penhora;
	}

	public void setPenhora(boolean penhora) {
		this.penhora = penhora;
	}

	public boolean isIntimacao() {
		return intimacao;
	}

	public void setIntimacao(boolean intimacao) {
		this.intimacao = intimacao;
	}
	
	public boolean isOficialCompanheiro() {
		return oficialCompanheiro;
	}

	public void setOficialCompanheiro(boolean oficialCompanheiro) {
		this.oficialCompanheiro = oficialCompanheiro;
	}
	
	public int getQtdLocomocao() {
		return qtdLocomocao;
	}

	public void setQtdLocomocao(int qtdLocomocao) {
		this.qtdLocomocao = qtdLocomocao;
	}
	
	public void copiar(LocomocaoDt objeto){
		if (objeto==null) return;
		Id_Locomocao = objeto.getId();
		GuiaItemDt = objeto.getGuiaItemDt();
		BairroDt = objeto.getBairroDt();
		ZonaDt = objeto.getZonaDt();
		RegiaoDt = objeto.getRegiaoDt();
		MandadoJudicialDt = objeto.getMandadoJudicialDt();
		Id_GuiaEmissaoComplementar = objeto.getId_GuiaEmissaoComplementar();
		CodigoTemp = objeto.getCodigoTemp();
		CodigoOficialSPG = objeto.getCodigoOficialSPG();
		GuiaItemContaVinculadaDt = objeto.getGuiaItemContaVinculadaDt();
		GuiaItemSegundoDt = objeto.getGuiaItemSegundoDt();
		GuiaItemTerceiroDt = objeto.getGuiaItemTerceiroDt();
		quantidadeAcrescimo = objeto.getQuantidadeAcrescimo();
		citacaoHoraCerta = objeto.isCitacaoHoraCerta();
		foraHorarioNormal = objeto.isForaHorarioNormal();
		finalidadeCodigo = objeto.finalidadeCodigo;
		penhora = objeto.isPenhora();
		intimacao = objeto.isIntimacao();
		oficialCompanheiro = objeto.isOficialCompanheiro();
		locomocaoSPGDt = objeto.getLocomocaoSPGDt();
		qtdLocomocao = objeto.getQtdLocomocao();
	}

	public void limpar(){
		Id_Locomocao="";
		GuiaItemDt=null;
		BairroDt = null;
		ZonaDt = null;
		RegiaoDt = null;
		MandadoJudicialDt = null;
		Id_GuiaEmissaoComplementar = null;
		CodigoTemp="";
		CodigoOficialSPG = "";
		GuiaItemContaVinculadaDt = null;
		GuiaItemSegundoDt = null;
		GuiaItemTerceiroDt = null;
		quantidadeAcrescimo = 0;
		qtdLocomocao = 0;
		citacaoHoraCerta = false;
		foraHorarioNormal = false;
		finalidadeCodigo = 0;
		penhora = false;
		intimacao = false;
		oficialCompanheiro = false;
		locomocaoSPGDt = null;
		Id_Guia_Emissao = "";
	}


	public String getPropriedades(){
		return "[Id_Locomocao:" + this.Id_Locomocao + "; Id_Guia_Emissao:" + this.Id_Guia_Emissao + "; Id_Guia_Item:" + (this.GuiaItemDt != null ? this.GuiaItemDt.getId() : "") + ";Id_Bairro:" + (this.BairroDt != null ? this.BairroDt.getId() : "") + ";Id_Zona:" + (this.ZonaDt != null ? this.ZonaDt.getId() : "") + ";Id_Regiao:" + (this.RegiaoDt != null ? this.RegiaoDt.getId() : "") + ";Id_Guia_Item_C_Vinc:" + (this.GuiaItemContaVinculadaDt != null ? this.GuiaItemContaVinculadaDt.getId() : "") + ";Id_Guia_Item2:" + (this.GuiaItemSegundoDt != null ? this.GuiaItemSegundoDt.getId() : "") + ";Id_Guia_Item3:" + (this.GuiaItemTerceiroDt != null ? this.GuiaItemTerceiroDt.getId() : "")  + ";Id_Mandado_Judicial:" + (this.MandadoJudicialDt != null ? this.MandadoJudicialDt.getId() : "") + ";Id_GuiaEmissaoComplementar:" + (this.Id_GuiaEmissaoComplementar != null ? this.Id_GuiaEmissaoComplementar : "") + ";CodigoTemp:" + CodigoTemp + ";CodigoOficialSPG:" + CodigoOficialSPG + ";QuantidadeAcresicomo:" + quantidadeAcrescimo + ";CitacaoHoraCerta:" + (citacaoHoraCerta?"1":"0") + ";ForaHorarioNormal:" + (foraHorarioNormal?"1":"0") + ";FinalidadeCodigo:" + finalidadeCodigo + ";Penhora:" + (penhora?"1":"0") + ";Intimacao:" + (intimacao?"1":"0") + "]";
	}
	
	public LocomocaoDt clonar() {
		LocomocaoDt objeto = new LocomocaoDt();
		objeto.copiar(this);
		objeto.setId("");		
		return objeto;
	}
	
	@Override
	public boolean equals(Object locomocaoObj) {
		if (locomocaoObj == null || ! (locomocaoObj instanceof LocomocaoDt)) return false;
		
		LocomocaoDt locomocaoDt = (LocomocaoDt) locomocaoObj;
		
		return locomocaoDt.getId().equalsIgnoreCase(this.getId());		
	}	
	
	public double getQuantidadeDeLocomocoes() {
		double quantidade = 0;
		
		if (this.getGuiaItemDt() != null) quantidade = Funcoes.StringToDouble(this.getGuiaItemDt().getQuantidade());
		
		if (this.getGuiaItemSegundoDt() != null) quantidade += Funcoes.StringToDouble(this.getGuiaItemSegundoDt().getQuantidade());
		
		if (this.getGuiaItemTerceiroDt() != null) quantidade += Funcoes.StringToDouble(this.getGuiaItemTerceiroDt().getQuantidade());
		
		return quantidade;
	}
	
	public double getValorCalculadoLocomocoes() {
		double valor = 0;
	
		if (this.getGuiaItemDt() != null) {
//			if (Funcoes.StringToDouble(this.getGuiaItemDt().getValorCalculadoOriginal()) > 0) valor = Funcoes.StringToDouble(this.getGuiaItemDt().getValorCalculadoOriginal()); 
//			else valor = Funcoes.StringToDouble(this.getGuiaItemDt().getValorCalculado());
			
			valor = Funcoes.StringToDouble(this.getGuiaItemDt().getValorCalculado());
		}
		
		if (this.getGuiaItemSegundoDt() != null) {
//			if (Funcoes.StringToDouble(this.getGuiaItemSegundoDt().getValorCalculadoOriginal()) > 0) valor = Funcoes.StringToDouble(this.getGuiaItemSegundoDt().getValorCalculadoOriginal());
//			else valor += Funcoes.StringToDouble(this.getGuiaItemSegundoDt().getValorCalculado());
			
			valor += Funcoes.StringToDouble(this.getGuiaItemSegundoDt().getValorCalculado());
		}
		
		if (this.getGuiaItemTerceiroDt() != null) {
//			if (Funcoes.StringToDouble(this.getGuiaItemTerceiroDt().getValorCalculadoOriginal()) > 0) valor = Funcoes.StringToDouble(this.getGuiaItemTerceiroDt().getValorCalculadoOriginal());
//			else valor += Funcoes.StringToDouble(this.getGuiaItemTerceiroDt().getValorCalculado());
			
			valor += Funcoes.StringToDouble(this.getGuiaItemTerceiroDt().getValorCalculado());
		}
		
		return valor;
	}
	
	public double getValorCalculadoTJGO() {
		double valor = 0;
		
		if (this.getGuiaItemContaVinculadaDt() != null) {
			if (Funcoes.StringToDouble(this.getGuiaItemContaVinculadaDt().getValorCalculadoOriginal()) > 0) valor = Funcoes.StringToDouble(this.getGuiaItemContaVinculadaDt().getValorCalculadoOriginal());
			else valor = Funcoes.StringToDouble(this.getGuiaItemContaVinculadaDt().getValorCalculado());
		}
		
		return valor;
	}

	public LocomocaoSPGDt getLocomocaoSPGDt() {
		return locomocaoSPGDt;
	}

	public void setLocomocaoSPGDt(LocomocaoSPGDt locomocaoSPGDt) {
		this.locomocaoSPGDt = locomocaoSPGDt;
	}
	
	private GuiaEmissaoDt guiaEmissaoComplementarDt;
	public GuiaEmissaoDt getGuiaEmissaoComplementarDt() throws Exception {
		if (this.Id_GuiaEmissaoComplementar == null) return null;
		
		if (this.guiaEmissaoComplementarDt == null || !this.guiaEmissaoComplementarDt.getId().trim().equalsIgnoreCase(this.Id_GuiaEmissaoComplementar)) {
			this.guiaEmissaoComplementarDt = new GuiaEmissaoNe().consultarId(this.Id_GuiaEmissaoComplementar);
		}
		
		return guiaEmissaoComplementarDt;
	}
	
	public boolean isLocomocaoParaAvaliacao() {
		return this.getFinalidadeCodigo()  ==	GuiaLocomocaoNe.LOCOMOCAO_AVALIACAO || this.getFinalidadeCodigo() ==	GuiaLocomocaoNe.LOCOMOCAO_AVALIACAO_PRACA;
	}
	
	public boolean isFinalidadeCitacaoPenhoraPracaLeilao() {
		return  this.getFinalidadeCodigo() == GuiaLocomocaoNe.CITACAO_PENHORA_E_PRACA_LEILAO;
	}
	
	public boolean isFinalidadeCitacaoPenhoraAvaliacaoPracaLeilao() {
		return this.getFinalidadeCodigo() == GuiaLocomocaoNe.CITACAO_PENHORA_AVALIACAO_PRACA_LEILAO;
	}
	
	public boolean isFinalidadeCitacaoPenhoraAvaliacaoAlienacao() {
		return this.getFinalidadeCodigo() == GuiaLocomocaoNe.CITACAO_PENHORA_AVALIACAO_E_ALIENACAO;
	}
	
	public boolean isFinalidadeLocomocao() {
		return this.getFinalidadeCodigo() == GuiaLocomocaoNe.LOCOMOCAO;
	}
	
}

