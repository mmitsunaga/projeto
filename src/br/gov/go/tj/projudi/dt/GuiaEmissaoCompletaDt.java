package br.gov.go.tj.projudi.dt;

import java.util.List;

import org.json.JSONException;

public class GuiaEmissaoCompletaDt extends Dados {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6872479556591784739L;
	
	private GuiaEmissaoDt guiaEmissaoDt;
	
	public GuiaEmissaoCompletaDt()
	{
		
	}
	
	public GuiaEmissaoCompletaDt(GuiaEmissaoDt guiaEmissaoDt)
	{
		this.guiaEmissaoDt = guiaEmissaoDt;
		if (guiaEmissaoDt != null) {
			setIpComputadorLog(guiaEmissaoDt.getIpComputadorLog());
			setId_UsuarioLog(guiaEmissaoDt.getId_UsuarioLog());
			setCodigoTemp(guiaEmissaoDt.getCodigoTemp());			
			setId(guiaEmissaoDt.getId());
			setNovoValorAcaoAtualizado(guiaEmissaoDt.getNovoValorAcaoAtualizado());
			setId_GuiaTipo(guiaEmissaoDt.getId_GuiaTipo());
			setGuiaTipo(guiaEmissaoDt.getGuiaTipo());
			setRateioCodigo(guiaEmissaoDt.getRateioCodigo());
			setId_ProcessoParteResponsavelGuia(guiaEmissaoDt.getId_ProcessoParteResponsavelGuia());
			setNomeProcessoParteResponsavelGuia(guiaEmissaoDt.getNomeProcessoParteResponsavelGuia());
			setNumeroGuiaCompleto(guiaEmissaoDt.getNumeroGuiaCompleto());
			setId_Usuario(guiaEmissaoDt.getId_Usuario());
			setUsuario(guiaEmissaoDt.getUsuario());
			setDataEmissao(guiaEmissaoDt.getDataEmissao());
			setId_GuiaStatus(guiaEmissaoDt.getId_GuiaStatus());
			setGuiaStatus(guiaEmissaoDt.getGuiaStatus());
			setGuiaModeloDt(guiaEmissaoDt.getGuiaModeloDt());
			setDataVencimento(guiaEmissaoDt.getDataVencimento());
			setDataRecebimento(guiaEmissaoDt.getDataRecebimento());
			setDataCancelamento(guiaEmissaoDt.getDataCancelamento());
			setId_Apelante(guiaEmissaoDt.getId_Apelante());
			setId_Apelado(guiaEmissaoDt.getId_Apelado());
			setApelante(guiaEmissaoDt.getApelante());
			setApelado(guiaEmissaoDt.getApelado());
			
			if(guiaEmissaoDt.getListaRequerentes() != null && guiaEmissaoDt.getListaRequerentes().size() > 0 ) {
				for( int i = 0; i < guiaEmissaoDt.getListaRequerentes().size(); i++ ) {
					ProcessoParteDt parteDt = (ProcessoParteDt) guiaEmissaoDt.getListaRequerentes().get(i);
					this.listaRequerentes.add(new ProcessoParteGuiaEmissaoConsultaDt(parteDt));
				}
			}
			
			if(guiaEmissaoDt.getListaRequeridos() != null && guiaEmissaoDt.getListaRequeridos().size() > 0 ) {
				for( int i = 0; i < guiaEmissaoDt.getListaRequeridos().size(); i++ ) {
					ProcessoParteDt parteDt = (ProcessoParteDt) guiaEmissaoDt.getListaRequeridos().get(i);
					this.listaRequeridos.add(new ProcessoParteGuiaEmissaoConsultaDt(parteDt));
				}
			}
			
			if(guiaEmissaoDt.getListaOutrasPartes() != null && guiaEmissaoDt.getListaOutrasPartes().size() > 0 ) {
				for( int i = 0; i < guiaEmissaoDt.getListaOutrasPartes().size(); i++ ) {
					ProcessoParteDt parteDt = (ProcessoParteDt) guiaEmissaoDt.getListaOutrasPartes().get(i);
					this.listaOutrasPartes.add(new ProcessoParteGuiaEmissaoConsultaDt(parteDt));
				}
			}
			
			if(guiaEmissaoDt.getListaPartesLitisconsorte() != null && guiaEmissaoDt.getListaPartesLitisconsorte().size() > 0 ) {
				for( int i = 0; i < guiaEmissaoDt.getListaPartesLitisconsorte().size(); i++ ) {
					ProcessoParteDt parteDt = (ProcessoParteDt) guiaEmissaoDt.getListaPartesLitisconsorte().get(i);
					this.listaPartesLitisconsorte.add(new ProcessoParteGuiaEmissaoConsultaDt(parteDt));
				}
			}
			
			setNumeroDUAM(guiaEmissaoDt.getNumeroDUAM());
			setQuantidadeParcelasDUAM(guiaEmissaoDt.getQuantidadeParcelasDUAM());
			setNumeroGuia(guiaEmissaoDt.getNumeroGuia());
			setListaGuiaItemDt(guiaEmissaoDt.getListaGuiaItemDt());
			setValorAcao(guiaEmissaoDt.getValorAcao());
			setId_Serventia(guiaEmissaoDt.getId_Serventia());
			setServentia(guiaEmissaoDt.getServentia());	
			setNumeroProcessoDependente(guiaEmissaoDt.getNumeroProcessoDependente());
		}
	}
	
	private String Id_GuiaEmissao;

	@Override
	public void setId(String id) {
		if (id != null) this.Id_GuiaEmissao = id;
	}

	@Override
	public String getId() {
		if(Id_GuiaEmissao == null) Id_GuiaEmissao = "";
		return this.Id_GuiaEmissao;
	}	
	
	private String NovoValorAcaoAtualizado;
	
	public String getNovoValorAcaoAtualizado() {
		if( NovoValorAcaoAtualizado == null) NovoValorAcaoAtualizado = "";
		return NovoValorAcaoAtualizado;
	}

	public void setNovoValorAcaoAtualizado(String novoValorAcaoAtualizado) {
		NovoValorAcaoAtualizado = novoValorAcaoAtualizado;
	}
	
	private String Id_GuiaTipo;
	
	
	public String getId_GuiaTipo() {
		return Id_GuiaTipo;
	}
	
	public void setId_GuiaTipo(String valor ) {
		if (valor!=null) if (valor.equalsIgnoreCase("null")) {
			Id_GuiaTipo = ""; 
			GuiaTipo = "";
		} else if (!valor.equalsIgnoreCase("")) Id_GuiaTipo = valor;
	}	
	
	private String GuiaTipo;
	
	public String getGuiaTipo()  {
		return GuiaTipo;
	}
	
	public void setGuiaTipo(String valor) {
		if (valor!=null) GuiaTipo = valor;
	}
	
	private String RateioCodigo;
	
	public String getRateioCodigo() {
		if( RateioCodigo == null ) {
			RateioCodigo = "";
		}
		return RateioCodigo;
	}

	public void setRateioCodigo(String rateioCodigo) {
		RateioCodigo = rateioCodigo;
	}
	
	private String Id_ProcessoParteResponsavelGuia;

	public String getId_ProcessoParteResponsavelGuia() {
		return Id_ProcessoParteResponsavelGuia;
	}

	public void setId_ProcessoParteResponsavelGuia(String id_ProcessoParteResponsavelGuia) {
		Id_ProcessoParteResponsavelGuia = id_ProcessoParteResponsavelGuia;
	}
	
	private String NomeProcessoParteResponsavelGuia;
	
	public String getNomeProcessoParteResponsavelGuia() {
		return NomeProcessoParteResponsavelGuia;
	}

	public void setNomeProcessoParteResponsavelGuia(String nomeProcessoParteResponsavelGuia) {
		NomeProcessoParteResponsavelGuia = nomeProcessoParteResponsavelGuia;
	}
	
	private String numeroGuiaCompleto;
	
	public String getNumeroGuiaCompleto() {
		return numeroGuiaCompleto;
	}

	public void setNumeroGuiaCompleto(String numeroGuiaCompleto) {
		this.numeroGuiaCompleto = numeroGuiaCompleto;
	}
	
	private String Id_Usuario;
	
	public String getId_Usuario()  {
		return Id_Usuario;
	}
	
	public void setId_Usuario(String valor ) {
		if (valor!=null) 
			if (valor.equalsIgnoreCase("null")) {
				Id_Usuario = ""; 
				DataEmissao = "";
			}else if (!valor.equalsIgnoreCase("")) 
				Id_Usuario = valor;
	}
	
	private String Usuario;
	
	public String getUsuario() {
		return Usuario;
	}
	
	public void setUsuario(String Usuario) {
		this.Usuario = Usuario;
	}
	
	private String DataEmissao;
	
	public String getDataEmissao() {
		return DataEmissao;
	}

	public void setDataEmissao(String dataEmissao) {
		DataEmissao = dataEmissao;
	}
	
	private String Id_GuiaStatus;
	
	public String getId_GuiaStatus()  {
		return Id_GuiaStatus;
	}
	
	public void setId_GuiaStatus(String valor ) {
		if (valor!=null) 
			if (valor.equalsIgnoreCase("null")) {
				Id_GuiaStatus = ""; 
				GuiaStatus = "";
			} else if (!valor.equalsIgnoreCase("")) 
				Id_GuiaStatus = valor;
	}
	
	private String GuiaStatus;
	
	public String getGuiaStatus()  {
		return GuiaStatus;
	}
	
	public void setGuiaStatus(String valor) {
		if (valor!=null) 
			GuiaStatus = valor;
	}
	
	private GuiaModeloDt guiaModeloDt;
	
	public GuiaModeloDt getGuiaModeloDt() {
		return guiaModeloDt;
	}

	public void setGuiaModeloDt(GuiaModeloDt guiaModeloDt) {
		this.guiaModeloDt = guiaModeloDt;
	}
	
	private String DataVencimento;
	
	public String getDataVencimento() {
		return DataVencimento;
	}
	
	public void setDataVencimento(String dataVencimento) {
		this.DataVencimento = dataVencimento;
	}
	
	private String DataRecebimento;
	
	public String getDataRecebimento() {
		return DataRecebimento;
	}

	public void setDataRecebimento(String dataRecebimento) {
		DataRecebimento = dataRecebimento;
	}
	
	private String DataCancelamento;
	
	public String getDataCancelamento() {
		return DataCancelamento;
	}

	public void setDataCancelamento(String dataCancelamento) {
		DataCancelamento = dataCancelamento;
	}
	
	private List<ProcessoParteGuiaEmissaoConsultaDt> listaRequerentes;
	
	public List<ProcessoParteGuiaEmissaoConsultaDt> getListaRequerentes() {
		return listaRequerentes;
	}

	public void setListaRequerentes(List<ProcessoParteGuiaEmissaoConsultaDt> listaRequerentes) {
		if (listaRequerentes != null) this.listaRequerentes = listaRequerentes;
	}
	
	public void addRequerente(ProcessoParteGuiaEmissaoConsultaDt requerente) {
		this.listaRequerentes.add(requerente);
	}
	
	private List<ProcessoParteGuiaEmissaoConsultaDt> listaRequeridos;
	
	public List<ProcessoParteGuiaEmissaoConsultaDt> getListaRequeridos() {
		return listaRequeridos;
	}

	public void setListaRequeridos(List<ProcessoParteGuiaEmissaoConsultaDt> listaRequeridos) {
		if (listaRequeridos != null) this.listaRequeridos = listaRequeridos;
	}
	
	public void addPromovido(ProcessoParteGuiaEmissaoConsultaDt promovido) {
		this.listaRequeridos.add(promovido);
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
	
    private List<ProcessoParteGuiaEmissaoConsultaDt> listaPartesLitisconsorte;
	
	public List<ProcessoParteGuiaEmissaoConsultaDt> getListaPartesLitisconsorte() {
		return listaPartesLitisconsorte;
	}

	public void setListaPartesLitisconsorte(List<ProcessoParteGuiaEmissaoConsultaDt> listaPartesLitisconsorte) {
		if (listaPartesLitisconsorte != null) this.listaPartesLitisconsorte = listaPartesLitisconsorte;
	}
	
	public void addParteLitisconsorte(ProcessoParteGuiaEmissaoConsultaDt partesLitisconsorte) {
		this.listaPartesLitisconsorte.add(partesLitisconsorte);
	}
	
	private String Id_Apelante;
	
	public String getId_Apelante() {
		return Id_Apelante;
	}

	public void setId_Apelante(String Id_apelante) {
		Id_Apelante = Id_apelante;
		
		if(listaRequerentes != null && listaRequerentes.size() > 0 ) {
			for(ProcessoParteGuiaEmissaoConsultaDt parteDt: this.listaRequerentes) {
				if( parteDt.getId().equals(Id_apelante) ) {
					this.setApelante(parteDt.getNome());
					break;
				}
			}
		}
		if(listaRequeridos != null && listaRequeridos.size() > 0 ) {
			for(ProcessoParteGuiaEmissaoConsultaDt parteDt: this.listaRequeridos) {
				if( parteDt.getId().equals(Id_apelante) ) {
					this.setApelante(parteDt.getNome());
					break;
				}
			}
		}
		if(listaPartesLitisconsorte != null && listaPartesLitisconsorte.size() > 0 ) {
			for(ProcessoParteGuiaEmissaoConsultaDt parteDt: this.listaPartesLitisconsorte) {				
				if( parteDt.getId().equals(Id_apelante) ) {
					this.setApelante(parteDt.getNome());
					break;
				}
			}
		}
		
	}
	
	private String Id_Apelado;
	
	public String getId_Apelado() {
		return Id_Apelado;
	}

	public void setId_Apelado(String Id_apelado) {
		Id_Apelado = Id_apelado;
		
		if(listaRequerentes != null && listaRequerentes.size() > 0 ) {
			for(ProcessoParteGuiaEmissaoConsultaDt parteDt: this.listaRequerentes) {				
				if( parteDt.getId().equals(Id_apelado) ) {
					this.setApelado(parteDt.getNome());
					break;
				}
			}
		}
		if(listaRequeridos != null && listaRequeridos.size() > 0 ) {
			for(ProcessoParteGuiaEmissaoConsultaDt parteDt: this.listaRequeridos) {				
				if( parteDt.getId().equals(Id_apelado) ) {
					this.setApelado(parteDt.getNome());
					break;
				}
			}
		}
		if(listaPartesLitisconsorte != null && listaPartesLitisconsorte.size() > 0 ) {
			for(ProcessoParteGuiaEmissaoConsultaDt parteDt: this.listaPartesLitisconsorte) {				
				if( parteDt.getId().equals(Id_apelado) ) {
					this.setApelado(parteDt.getNome());
					break;
				}
			}
		}
		
	}
	
	private String Apelante; //ou Recorrente
	
	public String getApelante() {
		if( Apelante == null) Apelante = "";
		return Apelante;
	}

	public void setApelante(String apelante) {
		Apelante = apelante;
	}
	
	private String Apelado; //ou Recorrido
	
	public String getApelado() {
		if( Apelado == null) Apelado = "";
		return Apelado;
	}

	public void setApelado(String apelado) {
		Apelado = apelado;
	}
	
	private String NumeroDUAM;
	
	public String getNumeroDUAM() {
		if( NumeroDUAM == null )
			NumeroDUAM = "";
		return NumeroDUAM;
	}

	public void setNumeroDUAM(String numeroDUAM) {
		NumeroDUAM = numeroDUAM;
	}
	
	private String QuantidadeParcelasDUAM;
	
	public String getQuantidadeParcelasDUAM() {
		if( QuantidadeParcelasDUAM == null )
			QuantidadeParcelasDUAM = "";
		return QuantidadeParcelasDUAM;
	}

	public void setQuantidadeParcelasDUAM(String quantidadeParcelasDUAM) {
		QuantidadeParcelasDUAM = quantidadeParcelasDUAM;
	}
	
	private String numeroGuia;
	
	public String getNumeroGuia() {
		return numeroGuia;
	}

	public void setNumeroGuia(String numeroGuia) {
		this.numeroGuia = numeroGuia;
	}
	
	private List listaGuiaItemDt;
	
	public List getListaGuiaItemDt() {
		return listaGuiaItemDt;
	}

	public void setListaGuiaItemDt(List listaGuiaItemDt) {
		this.listaGuiaItemDt = listaGuiaItemDt;
	}
	
	private String ValorAcao;
	
	public String getValorAcao() {
		return ValorAcao;
	}

	public void setValorAcao(String valorAcao) {
		ValorAcao = valorAcao;
	}
	
	private String Id_Serventia;
	
	public String getId_Serventia()  {
		return Id_Serventia;
	}
	
	public void setId_Serventia(String valor) {
		if (valor!=null) 
			if (valor.equalsIgnoreCase("null")) {
				Id_Serventia = ""; 
				Serventia = "";}
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
	
	private String Comarca;
	
	public String getComarca() {
		return Comarca;
	}

	public void setComarca(String comarca) {
		Comarca = comarca;
	}
	
	private String ComarcaCodigo;
	
	public String getComarcaCodigo() {
		return ComarcaCodigo;
	}

	public void setComarcaCodigo(String comarcaCodigo) {
		ComarcaCodigo = comarcaCodigo;
	}

	private String Id_ProcessoTipo;
	
	public String getId_ProcessoTipo() {
		return Id_ProcessoTipo;
	}

	public void setId_ProcessoTipo(String id_ProcessoTipo) {
		Id_ProcessoTipo = id_ProcessoTipo;
	}
	
	private String ProcessoTipo;
	
	public String getProcessoTipo() {
		return ProcessoTipo;
	}

	public void setProcessoTipo(String processoTipo) {
		ProcessoTipo = processoTipo;
	}
	
	private String NumeroProcesso;
	
	public String getNumeroProcesso() {
		return NumeroProcesso;
	}

	public void setNumeroProcesso(String numeroProcesso) {
		NumeroProcesso = numeroProcesso;
	}
	
	public String getNomePrimeiroRequerente() throws JSONException {
		String retorno = null;
		
		if( listaRequerentes != null && listaRequerentes.size() > 0 ) {
			ProcessoParteGuiaEmissaoConsultaDt processoParteDt = (ProcessoParteGuiaEmissaoConsultaDt) listaRequerentes.get(0);
			retorno = processoParteDt.getNome();
		}
		else {			
			return "";
		}
		
		return retorno;
	}
	
	public String getNomePrimeiroRequerido() throws JSONException {
		String retorno = null;
		
		if( listaRequeridos != null && listaRequeridos.size() > 0 ) {
			ProcessoParteGuiaEmissaoConsultaDt processoParteDt = (ProcessoParteGuiaEmissaoConsultaDt) listaRequeridos.get(0);
			retorno = processoParteDt.getNome();
		}
		else {
			return "";
		}
		
		return retorno;
	}
	
	public String getNomeParte(String idProcessoParte) {
		String retorno = null;
		
		if( listaRequerentes != null && listaRequerentes.size() > 0 ) {
			for (int i = 0; i < listaRequerentes.size(); i++) {
				ProcessoParteGuiaEmissaoConsultaDt processoParteDt = (ProcessoParteGuiaEmissaoConsultaDt) listaRequerentes.get(i);
			    
				if( idProcessoParte.equals(processoParteDt.getId()) ) {
					retorno = processoParteDt.getNome();
					break;
				}
			}
		}
		if( listaRequeridos != null && listaRequeridos.size() > 0 ) {
			for (int i = 0; i < listaRequeridos.size(); i++) {
				ProcessoParteGuiaEmissaoConsultaDt processoParteDt = (ProcessoParteGuiaEmissaoConsultaDt) listaRequeridos.get(i);
			    
				if( idProcessoParte.equals(processoParteDt.getId()) ) {
					retorno = processoParteDt.getNome();
					break;
				}
			}
		}
		
		return retorno;
	}
	
	private String numeroProcessoDependente;
	
	public String getNumeroProcessoDependente() {
		return this.numeroProcessoDependente;
	}
	
	public void setNumeroProcessoDependente(String numeroProcessoDependente) {
		this.numeroProcessoDependente = numeroProcessoDependente;
	}
}
