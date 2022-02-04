package br.gov.go.tj.projudi.dt;

public class GuiaFinalidadeModeloDt extends Dados {

	private static final long serialVersionUID = 9011310912997502592L;

	private String id;
	private String guiaFinalidadeModelo;
	private GuiaTipoDt guiaTipoDt;
	private FinalidadeDt finalidadeDt;
	private String Id_GuiaTipo;
	private String GuiaTipo;
	private String guiaFinalidadeModeloCodigo;
	private String codigoTemp;
	private String acrescimoLocomocao;
	private String penhoraLocomocao;
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}
	
	public void copiar(GuiaFinalidadeModeloDt objeto){
		 if (objeto==null) return;
		 id = objeto.getId();
		 guiaFinalidadeModelo = objeto.getGuiaFinalidadeModelo();
		 guiaFinalidadeModeloCodigo = objeto.getGuiaFinalidadeModeloCodigo();
		 guiaTipoDt = objeto.getGuiaTipoDt();
		 finalidadeDt = objeto.getFinalidadeDt();
		 Id_GuiaTipo = objeto.getId_GuiaTipo();
		 GuiaTipo = objeto.getGuiaTipo();
		 acrescimoLocomocao = objeto.getAcrescimoLocomocao();
		 penhoraLocomocao = objeto.getPenhoraLocomocao();
		 codigoTemp = objeto.getCodigoTemp();
	}
	
	public String getId_GuiaTipo()  {return Id_GuiaTipo;}
	public void setId_GuiaTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_GuiaTipo = ""; GuiaTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_GuiaTipo = valor;}
	public String getGuiaTipo()  {return GuiaTipo;}
	public void setGuiaTipo(String valor ) {if (valor!=null) GuiaTipo = valor;}

	public String getGuiaFinalidadeModelo() {
		return guiaFinalidadeModelo;
	}

	public void setGuiaFinalidadeModelo(String guiaFinalidadeModelo) {
		this.guiaFinalidadeModelo = guiaFinalidadeModelo;
	}

	public GuiaTipoDt getGuiaTipoDt() {
		return guiaTipoDt;
	}

	public void setGuiaTipoDt(GuiaTipoDt guiaTipoDt) {
		this.guiaTipoDt = guiaTipoDt;
	}

	public FinalidadeDt getFinalidadeDt() {
		return finalidadeDt;
	}

	public void setFinalidadeDt(FinalidadeDt finalidadeDt) {
		this.finalidadeDt = finalidadeDt;
	}

	public String getGuiaFinalidadeModeloCodigo() {
		return guiaFinalidadeModeloCodigo;
	}

	public void setGuiaFinalidadeModeloCodigo(String guiaFinalidadeModeloCodigo) {
		this.guiaFinalidadeModeloCodigo = guiaFinalidadeModeloCodigo;
	}

	public String getCodigoTemp() {
		return codigoTemp;
	}

	public void setCodigoTemp(String codigoTemp) {
		this.codigoTemp = codigoTemp;
	}

	public String getAcrescimoLocomocao() {
		if (acrescimoLocomocao != null){
			return acrescimoLocomocao;
		} else {
			return "0";
		}
	}

	public void setAcrescimoLocomocao(String acrescimoLocomocao) {
		this.acrescimoLocomocao = acrescimoLocomocao;
	}

	public String getPenhoraLocomocao() {
		if (penhoraLocomocao != null){
			return penhoraLocomocao;
		} else {
			return "0";
		}
	}

	public void setPenhoraLocomocao(String penhoraLocomocao) {
		this.penhoraLocomocao = penhoraLocomocao;
	}
	
	

}
