package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.util.enumeradoresSeguros.EnumSistemaProcessoTJGO;

public class ProcessoGuiaEmissaoConsultaDt extends Dados {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5411471607773217686L;
	
	public ProcessoGuiaEmissaoConsultaDt()
	{
		this.numeroProcessoDt = new NumeroProcessoDt();
		this.listaPromoventes = new ArrayList<ProcessoParteGuiaEmissaoConsultaDt>();
		this.listaPromovidos = new ArrayList<ProcessoParteGuiaEmissaoConsultaDt>();
		this.listaOutrasPartes = new ArrayList<ProcessoParteGuiaEmissaoConsultaDt>();
		this.listaLitisconsorteAtivoPassivo = new ArrayList<ProcessoParteGuiaEmissaoConsultaDt>();
	}

	private String IdProcesso;
	
	@Override
	public void setId(String id) {
		this.IdProcesso = id;
	}

	@Override
	public String getId() {
		return this.IdProcesso;
	}
	
	private NumeroProcessoDt numeroProcessoDt;

	public NumeroProcessoDt getNumeroProcessoCompletoDt() {
		return numeroProcessoDt;
	}

	public void setNumeroProcessoCompletoDt(NumeroProcessoDt numeroProcessoDt) {
		this.numeroProcessoDt = numeroProcessoDt;
	}
	
	private EnumSistemaProcessoTJGO sistemaProcessoTJGO;

	public EnumSistemaProcessoTJGO getSistemaProcessoTJGO() {
		return sistemaProcessoTJGO;
	}

	public void setSistemaProcessoTJGO(EnumSistemaProcessoTJGO sistemaProcessoTJGO) {
		this.sistemaProcessoTJGO = sistemaProcessoTJGO;
	}
	
	private List<ProcessoParteGuiaEmissaoConsultaDt> listaPromoventes;
	
	public List<ProcessoParteGuiaEmissaoConsultaDt> getListaPromoventes() {
		return listaPromoventes;
	}

	public void setListaPromoventes(List<ProcessoParteGuiaEmissaoConsultaDt> listaPromoventes) {
		if (listaPromoventes != null) this.listaPromoventes = listaPromoventes;
	}
	
	public void addPromovente(ProcessoParteGuiaEmissaoConsultaDt promovente) {
		this.listaPromoventes.add(promovente);
	}
	
	private List<ProcessoParteGuiaEmissaoConsultaDt> listaPromovidos;
	
	public List<ProcessoParteGuiaEmissaoConsultaDt> getListaPromovidos() {
		return listaPromovidos;
	}

	public void setListaRequerentes(List<ProcessoParteGuiaEmissaoConsultaDt> listaPromovidos) {
		if (listaPromovidos != null) this.listaPromovidos = listaPromovidos;
	}
	
	public void addPromovido(ProcessoParteGuiaEmissaoConsultaDt promovido) {
		this.listaPromovidos.add(promovido);
	}
	
	private List<ProcessoParteGuiaEmissaoConsultaDt> listaOutrasPartes;
	
	public List<ProcessoParteGuiaEmissaoConsultaDt> getListaOutrasPartes() {
		return listaOutrasPartes;
	}

	public void setListaOutrasPartes(List<ProcessoParteGuiaEmissaoConsultaDt> listaOutrasPartes) {
		if (listaOutrasPartes != null) this.listaOutrasPartes = listaOutrasPartes;
	}
	
	public void addOutrasPartes(ProcessoParteGuiaEmissaoConsultaDt outrasPartes) {
		this.listaOutrasPartes.add(outrasPartes);
	}
	
    private List<ProcessoParteGuiaEmissaoConsultaDt> listaLitisconsorteAtivoPassivo;
	
	public List<ProcessoParteGuiaEmissaoConsultaDt> getListaLitisconsorteAtivoPassivo() {
		return listaLitisconsorteAtivoPassivo;
	}

	public void setListaLitisconsorteAtivoPassivo(List<ProcessoParteGuiaEmissaoConsultaDt> listaLitisconsorteAtivoPassivo) {
		if (listaLitisconsorteAtivoPassivo != null) this.listaLitisconsorteAtivoPassivo = listaLitisconsorteAtivoPassivo;
	}
	
	public void addLitisconsorteAtivoPassivo(ProcessoParteGuiaEmissaoConsultaDt litisconsorteAtivoPassivo) {
		this.listaLitisconsorteAtivoPassivo.add(litisconsorteAtivoPassivo);
	}
	
	private String id_ProcessoTipo;
	
	public String getId_ProcessoTipo() {
		return id_ProcessoTipo;
	}

	public void setId_ProcessoTipo(String id_ProcessoTipo) {
		if (id_ProcessoTipo != null) this.id_ProcessoTipo = id_ProcessoTipo;
	}
	
	private String processoTipo;
	
	public String getProcessoTipo() {
		return processoTipo;
	}

	public void setProcessoTipo(String processoTipo) {
		if (processoTipo != null) this.processoTipo = processoTipo;
	}
	
    private String valor;
	
	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		if (valor != null) this.valor = valor;
	}
	
	private String Id_Serventia;
	
	public String getId_Serventia()  {
		return Id_Serventia;
	}
	
	public void setId_Serventia(String valor) {
		if (valor!=null) 
			if (valor.equalsIgnoreCase("null")) {
				Id_Serventia = ""; 
				Serventia = "";
			}
			else if (!valor.equalsIgnoreCase("")) 
				Id_Serventia = valor;
	}
	
	private String Serventia;
	
	public String getServentia() {
		return Serventia;
	}
	
	public void setServentia(String valor )
	{
		if (valor!=null) 
			Serventia = valor;
	}
	
	private String ServentiaCodigo;
	
	public String getServentiaCodigo()  {
		return ServentiaCodigo;
	}
	
	public void setServentiaCodigo(String valor) {
		if (valor!=null) 
			if (valor.equalsIgnoreCase("null")) {
				ServentiaCodigo = "";
			}
			else if (!valor.equalsIgnoreCase("")) 
				ServentiaCodigo = valor;
	}
	
	private String Id_Comarca;
	
	public String getId_Comarca()  {
		return Id_Comarca;
	}
	
	public void setId_Comarca(String valor) {
		if (valor!=null) 
			if (valor.equalsIgnoreCase("null")) {
				Id_Comarca = ""; 
				Comarca = "";
			}
			else if (!valor.equalsIgnoreCase("")) 
				Id_Comarca = valor;
	}
	
	private String Comarca;
	
	public String getComarca() {
		return Comarca;
	}
	
	public void setComarca(String valor )
	{
		if (valor!=null) 
			Comarca = valor;
	}
	
	private String ComarcaCodigo;
	
	public String getComarcaCodigo()  {
		return ComarcaCodigo;
	}
	
	public void setComarcaCodigo(String valor) {
		if (valor!=null) 
			if (valor.equalsIgnoreCase("null")) {
				ComarcaCodigo = "";
			}
			else if (!valor.equalsIgnoreCase("")) 
				ComarcaCodigo = valor;
	}
}
