package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.utils.Funcoes;

public class RecursoDt extends RecursoDtGen {

	private static final long serialVersionUID = 8249743161956220087L;
	public static final int	  CodigoPermissao  = 336;

	private ProcessoDt		  processoDt;							  // Processo vinculado ao recurso
	private List			  listaAssuntos;						  // Assuntos do recurso
	private List			  listaRecorrentes;						  // Partes recorrentes
	private List			  listaRecorridos;						  // Partes recorridos
	private String			  Id_Assunto;
	private String			  Assunto;
	private String			  SemParte;
	private String			  descricaoPoloAtivo;
	private String			  descricaoPoloPassivo;
	private String			  Id_ProcessoTipoRecursoParteAtual;
	private String			  ProcessoTipoRecursoParteAtual;

	public String getSemParte() {
		return SemParte;
	}

	public void setSemParte(String semParte) {
		if (semParte != null)
			if (semParte.equalsIgnoreCase("null"))
				SemParte = "0";
			else if (!semParte.equalsIgnoreCase(""))
				SemParte = semParte;
	}

	public void limpar() {
		super.limpar();
		Id_Assunto = "";
		Assunto = "";
		SemParte = "0";
		descricaoPoloAtivo = "";
		descricaoPoloPassivo = "";
		Id_ProcessoTipoRecursoParteAtual = "";
		ProcessoTipoRecursoParteAtual = "";
	}

	public List getListaAssuntos() {
		return listaAssuntos;
	}

	public void setListaAssuntos(List listaAssuntos) {
		this.listaAssuntos = listaAssuntos;
	}

	public void addListaAssuntos(RecursoAssuntoDt dt) {
		if (listaAssuntos == null)
			listaAssuntos = new ArrayList();
		this.listaAssuntos.add(dt);
	}

	public String getId_Assunto() {
		return Id_Assunto;
	}

	public void setId_Assunto(String id_Assunto) {
		if (id_Assunto != null)
			if (id_Assunto.equalsIgnoreCase("null")) {
				Id_Assunto = "";
				Assunto = "";
			} else if (!id_Assunto.equalsIgnoreCase(""))
				Id_Assunto = id_Assunto;
	}

	public String getAssunto() {
		return Assunto;
	}

	public void setAssunto(String assunto) {
		if (assunto != null)
			if (assunto.equalsIgnoreCase("null"))
				Assunto = "";
			else if (!assunto.equalsIgnoreCase(""))
				Assunto = assunto;
	}

	public int getProcessoFaseCodigoToInt() {
		int inTemp = 0;
		if (processoDt != null)
			inTemp = Funcoes.StringToInt(processoDt.getProcessoFaseCodigo());
		return inTemp;
	}

	public ProcessoDt getProcessoDt() {
		return processoDt;
	}

	public void setProcessoDt(ProcessoDt processoDt) {
		this.processoDt = processoDt;
	}

	public List getListaRecorrentes() {
		return listaRecorrentes;
	}

	public void setListaRecorrentes(List listaRecorrentes) {
		this.listaRecorrentes = listaRecorrentes;
	}

	public List getListaRecorridos() {
		return listaRecorridos;
	}

	public void setListaRecorridos(List listaRecorridos) {
		this.listaRecorridos = listaRecorridos;
	}

	public void addListaRecorrentes(RecursoParteDt dt) {
		if (listaRecorrentes == null)
			listaRecorrentes = new ArrayList();
		this.listaRecorrentes.add(dt);
	}

	public void addListaRecorridos(RecursoParteDt dt) {
		if (listaRecorridos == null)
			listaRecorridos = new ArrayList();
		this.listaRecorridos.add(dt);
	}

	/**
	 * Retorna partes recorrentes e recorridas de um recurso
	 */
	public List getPartesRecorrentesRecorridas() {
		List lista = new ArrayList();
		if (listaRecorrentes != null)
			lista.addAll(listaRecorrentes);
		if (listaRecorridos != null)
			lista.addAll(listaRecorridos);
		return lista;

	}

	public boolean ehAutuacao() {
		return getDataRecebimento() == null || getDataRecebimento().equals("");
	}

	public List getListaRecorrentesAtivos() {
		List lista = new ArrayList();
		if (this.listaRecorrentes != null && this.listaRecorrentes.size() > 0) {
			for (int i = 0; i < this.listaRecorrentes.size(); i++) {
				RecursoParteDt recursoParteDt = (RecursoParteDt) this.listaRecorrentes.get(i);
				if (recursoParteDt.getDataBaixa().length() == 0)
					lista.add(recursoParteDt);
			}
		}
		return lista;
	}

	public List getListaRecorrentesAtivos(ProcessoTipoDt processoTipo) {
		String id_processoTipo = "";
		if (processoTipo != null)
			id_processoTipo = processoTipo.getId();
		return getListaRecorrentesAtivos(id_processoTipo);
	}

	public List getListaRecorrentesAtivos(String id_processoTipo) {
		List lista = new ArrayList();
		if (this.listaRecorrentes != null && this.listaRecorrentes.size() > 0 && id_processoTipo != null && id_processoTipo.trim().length() > 0) {
			for (int i = 0; i < this.listaRecorrentes.size(); i++) {
				RecursoParteDt recursoParteDt = (RecursoParteDt) this.listaRecorrentes.get(i);
				if (recursoParteDt.getDataBaixa().length() == 0 && Funcoes.StringToInt(recursoParteDt.getId_ProcessoTipo()) == Funcoes.StringToInt(id_processoTipo))
					lista.add(recursoParteDt);
			}
		}
		return lista;
	}

	public RecursoParteDt getPrimeiroRecorrente(String id_processoTipo) {
		RecursoParteDt recursoParteDt = null;
		List recorrentesAtivos = getListaRecorrentesAtivos(id_processoTipo);
		if (recorrentesAtivos != null && recorrentesAtivos.size() > 0) {
			RecursoParteDt recorrente = (RecursoParteDt) recorrentesAtivos.get(0);
			if (recorrente != null)
				recursoParteDt = recorrente;
		}
		return recursoParteDt;
	}

	public List getListaRecorridosAtivos() {
		List lista = new ArrayList();
		if (this.listaRecorridos != null && this.listaRecorridos.size() > 0) {
			for (int i = 0; i < this.listaRecorridos.size(); i++) {
				RecursoParteDt recursoParteDt = (RecursoParteDt) this.listaRecorridos.get(i);
				if (recursoParteDt.getDataBaixa().length() == 0)
					lista.add(recursoParteDt);
			}
		}
		return lista;
	}

	public List getListaRecorridosAtivos(ProcessoTipoDt processoTipo) {
		String id_processoTipo = "";
		if (processoTipo != null)
			id_processoTipo = processoTipo.getId();
		return getListaRecorridosAtivos(id_processoTipo);
	}

	public List getListaRecorridosAtivos(String id_processoTipo) {
		List lista = new ArrayList();
		if (this.listaRecorridos != null && this.listaRecorridos.size() > 0 && id_processoTipo != null && id_processoTipo.trim().length() > 0) {
			for (int i = 0; i < this.listaRecorridos.size(); i++) {
				RecursoParteDt recursoParteDt = (RecursoParteDt) this.listaRecorridos.get(i);
				if (recursoParteDt.getDataBaixa().length() == 0 && Funcoes.StringToInt(recursoParteDt.getId_ProcessoTipo()) == Funcoes.StringToInt(id_processoTipo))
					lista.add(recursoParteDt);
			}
		}
		return lista;
	}

	public RecursoParteDt getPrimeiroRecorrido(String id_processoTipo) {
		RecursoParteDt recursoParteDt = null;
		List recorridosAtivos = getListaRecorridosAtivos(id_processoTipo);
		if (recorridosAtivos != null && recorridosAtivos.size() > 0) {
			RecursoParteDt recorrente = (RecursoParteDt) recorridosAtivos.get(0);
			if (recorrente != null)
				recursoParteDt = recorrente;
		}
		return recursoParteDt;
	}

	public String getDescricaoPoloAtivo() {
		if (descricaoPoloAtivo == null || descricaoPoloAtivo.trim().length() == 0)
			return "Recorrente";
		return descricaoPoloAtivo;
	}

	public void setDescricaoPoloAtivo(String descricaoPoloAtivo) {
		this.descricaoPoloAtivo = descricaoPoloAtivo;
	}

	public String getDescricaoPoloPassivo() {
		if (descricaoPoloPassivo == null || descricaoPoloPassivo.trim().length() == 0)
			return "Recorrido";
		return descricaoPoloPassivo;
	}

	public void setDescricaoPoloPassivo(String descricaoPoloPassivo) {
		this.descricaoPoloPassivo = descricaoPoloPassivo;
	}

	public List<ProcessoTipoDt> getListaDeProcessosTipoClasse() {
		List<String> listaDeCodigosDeClasses = new ArrayList<String>();
		List<ProcessoTipoDt> listaDeClasses = new ArrayList<ProcessoTipoDt>();

		listaDeCodigosDeClasses.add(this.getId_ProcessoTipo()); // Recurso Principal...
		listaDeClasses.add(getProcessoTipoDt(this.getId_ProcessoTipo(), this.getProcessoTipo()));

		List recorrentesAtivos = getListaRecorrentesAtivos();
		if (recorrentesAtivos != null) {
			for (int i = 0; i < recorrentesAtivos.size(); i++) {
				RecursoParteDt recursoParteDt = (RecursoParteDt) recorrentesAtivos.get(i);
				if (recursoParteDt.getId_ProcessoTipo() != null && recursoParteDt.getId_ProcessoTipo().trim().length() > 0 && !listaDeCodigosDeClasses.contains(recursoParteDt.getId_ProcessoTipo())) {
					listaDeCodigosDeClasses.add(recursoParteDt.getId_ProcessoTipo());
					listaDeClasses.add(getProcessoTipoDt(recursoParteDt.getId_ProcessoTipo(), recursoParteDt.getProcessoTipo()));
				}
			}
		}

		List recorridosAtivos = getListaRecorridosAtivos();
		if (recorridosAtivos != null) {
			for (int i = 0; i < recorridosAtivos.size(); i++) {
				RecursoParteDt recursoParteDt = (RecursoParteDt) recorridosAtivos.get(i);
				if (recursoParteDt.getId_ProcessoTipo() != null && recursoParteDt.getId_ProcessoTipo().trim().length() > 0 && !listaDeCodigosDeClasses.contains(recursoParteDt.getId_ProcessoTipo())) {
					listaDeCodigosDeClasses.add(recursoParteDt.getId_ProcessoTipo());
					listaDeClasses.add(getProcessoTipoDt(recursoParteDt.getId_ProcessoTipo(), recursoParteDt.getProcessoTipo()));
				}
			}
		}

		return listaDeClasses;
	}

	private ProcessoTipoDt getProcessoTipoDt(String id, String descricao) {
		ProcessoTipoDt processoTipo = new ProcessoTipoDt();
		processoTipo.setId(id);
		processoTipo.setProcessoTipo(descricao);
		return processoTipo;
	}

	public String getId_ProcessoTipoRecursoParteAtual() {
		return Id_ProcessoTipoRecursoParteAtual;
	}

	public void setId_ProcessoTipoRecursoParteAtual(String valor) {
		if (valor != null)
			if (valor.equalsIgnoreCase("null")) {
				Id_ProcessoTipoRecursoParteAtual = "";
				ProcessoTipoRecursoParteAtual = "";
			} else if (!valor.equalsIgnoreCase(""))
				Id_ProcessoTipoRecursoParteAtual = valor;
	}

	public String getProcessoTipoRecursoParteAtual() {
		return ProcessoTipoRecursoParteAtual;
	}

	public void setProcessoTipoRecursoParteAtual(String valor) {
		if (valor != null)
			ProcessoTipoRecursoParteAtual = valor;
	}
}
