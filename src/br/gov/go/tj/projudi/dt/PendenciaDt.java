package br.gov.go.tj.projudi.dt;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class PendenciaDt extends PendenciaDtGen {

	private static final long serialVersionUID = -570114541776265372L;

	public static final int CodigoPermissao = 112;

	public static int VOTO_VENCIDO_RELATOR = -4;
	
	public static int PRAZO_PARA_REALIZACAO_CARGA = 5;
	public static int PRAZO_PARA_DEVOLUCAO_AUTOS = 5;

	private ProcessoDt processoDt;
	private boolean envia_Magistrado;
	private String id_Bairro;
	private String id_ServentiaGrupo;
	private String id_MandadoJudicial;
	private String id_MandadoTipo;
	private String id_EnderecoParte;
	private String id_Area;
	private String id_Zona;
	private String id_Regiao;
	private String id_Escala;
	private String id_MandadoJudicialStatus;
	private String id_UsuarioServentia1;
	private String id_UsuarioServentia2;
	private String id_OficialAdhoc;
	private String oficialAdhoc;
	private String serventiaUsuarioCadastrador;
	private String serventiaUsuarioFinalizador;
	private String nomeUsuarioFinalizador;
	private String pendenciaStatusCodigo;
	private String processoPrioridadeCodigo;
	private String serventiaGrupo;
	private String bairro;
	private String valor;
	private String assistencia;
	private String mandadoTipo;
	private String prazoMandado;
	private String codigoPrazoMandado;
	private String processoTipo;
	private String id_Procurador;
	private String nome_Procurador;
	private String id_Promotor;
	private String nome_Promotor;
	private String nome_UsuarioServentia2;
	private int qtdLocomocoesMandado;
	private String numeroReservadoMandadoExpedir;
	private Integer codigoPendTemp;
	private String codModeloCorreio;
	private String maoPropriaCorreio;
	private List listaHistoricoPendencia;
	private String id_ProcessoCustaTipo;
	private String ordemServico;
	private String idModelo;
	private String idPendenciaCorreios;
	
	//Variáveis necessárias para o alvará de soltura
	private String id_EventoTipo;
	private String eventoTipo;
	private String dataEvento;
	
	//variável auxiliar para geração de relatório de intimações geradas no dia anterior
	private String complemento = "";

	// Lista de responsaveis para pendencia
	private List responsaveis;

	// Lista de magistrado da serventia
	private List magistradoResponsaveisAtuais;

	// Lista de arquivos vinculados a pendencia
	private List ListaArquivos = null;

	// Lista de Tipos de Mandados
	private List listaTiposMandados;

	// Auxiliar utilizado para indicar que o usuario pode liberar
	private boolean podeLiberar = false;

	// Auxiliar utilizada para indicar se pendência é de um advogado principal
	private boolean advogadoPrincipal = false;

	private String hash;

	private boolean lido;

	// atributo com o status da pendencia aguardando retorno
	private String statusPendenciaRetorno;

	// lista de status para pendencias aguardando retorno
	private String[] listaStatusAguardandoRetorno;

	// Serventia processo vinculado a pendência
	private String id_ServentiaProcesso;

	public static final int DISTRIBUIDA = -3;

	// Variável utilizada apenas para a funcionalidade de pedido de vista
	private String id_ServentiaCargo = "";

	private String processoTipoSessao = "";
	private String id_ProcessoTipoSessao = "";

	private boolean ExpedicaoAutomatica=false;

	public int getQtdLocomocoesMandado() {
		return qtdLocomocoesMandado;
	}

	public void setQtdLocomocoesMandado(int qtdLocomocoesMandado) {
		this.qtdLocomocoesMandado = qtdLocomocoesMandado;
	}

	public String getStatusPendenciaRetorno() {
		return statusPendenciaRetorno;
	}

	public void setStatusPendenciaRetorno(String statusPendenciaRetorno) {
		if (statusPendenciaRetorno != null)
			this.statusPendenciaRetorno = statusPendenciaRetorno;
	}

	public String[] getListaStatusAguardandoRetorno() {
		return listaStatusAguardandoRetorno;
	}

	public void setListaStatusAguardandoRetorno(
			String[] listaStatusAguardandoRetorno) {
		this.listaStatusAguardandoRetorno = listaStatusAguardandoRetorno;
	}

	public boolean isLido() {
		return lido;
	}

	public void setLido(boolean lido) {
		this.lido = lido;
	}

	public static int getCodigoPermissao() {
		return CodigoPermissao;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public boolean isPodeLiberar() {
		return podeLiberar;
	}

	public void setPodeLiberar(boolean podeLiberar) {
		this.podeLiberar = podeLiberar;
	}

	public String getNomeUsuarioFinalizador() {
		return nomeUsuarioFinalizador;
	}

	public void setNomeUsuarioFinalizador(String nomeUsuarioFinalizador) {
		this.nomeUsuarioFinalizador = nomeUsuarioFinalizador;
	}
	
	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		if(complemento != null)
			this.complemento = complemento;
	}

	public String getServentiaUsuarioFinalizador() {
		return serventiaUsuarioFinalizador;
	}

	public void setServentiaUsuarioFinalizador(
			String serventiaUsuarioFinalizador) {
		this.serventiaUsuarioFinalizador = serventiaUsuarioFinalizador;
	}

	/**
	 * Retorna se a pendencia e somente para marcacao de leitura
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 21/01/2009 10:21
	 * @return true se e para somente marcar a leitura e false se nao e para
	 *         somente marcar a leitura
	 */
	public boolean getSomenteMarcarLeitura() throws Exception {
		// Todas pendencias de aguardando retorno sao somente para marcar a
		// leitura
		if (this.getPendenciaStatusCodigo() != null
				&& !this.getPendenciaStatusCodigo().equals("")
				&& this.getPendenciaStatusCodigo().equals(
						String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_RETORNO))) {
			return true;
		}

		return false;
	}

	/**
	 * Retorna uma lista de arquivos, dos arquivos vinculados com o vo
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 12/12/2008 17:15
	 * @return List
	 */
	public List getArquivosResposta() {
		List arquivos = new ArrayList();

		if (this.ListaArquivos != null) {
			Iterator it = this.ListaArquivos.iterator();

			while (it.hasNext()) {
				Object obj = it.next();

				if (obj instanceof PendenciaArquivoDt) {
					PendenciaArquivoDt pendenciaArquivoDt = (PendenciaArquivoDt) obj;

					if (pendenciaArquivoDt.getResposta().equals("true"))
						arquivos.add(pendenciaArquivoDt.getArquivoDt());
				}
			}
		}

		return arquivos;
	}

	/**
	 * Retorna se a pendencia e uma pendencia de processo
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 10/12/2008 09:50
	 * @return boolean
	 */
	public boolean isPendenciaDeProcesso() {
		return this.getId_Processo() != null
				&& !this.getId_Processo().trim().equals("");
	}

	public boolean isVisto() {
		return (this.getDataFim() != null && !this.getDataFim().equals("") && (this
				.getDataVisto() == null || this.getDataVisto().equals("")));

	}

	public boolean isDescartar() {
		boolean retorno = false;
		if ((this.getId_Processo() == null || this.getId_Processo().trim()
				.equals(""))
				&& (this.getDataFim() == null || this.getDataFim().equals(""))) {
			retorno = true;
		}
		return retorno;
	}

	/**
	 * Retorna o vo do responsavel pela pendencia
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 27/11/2008 14:16
	 * @return PendenciaResponsavelDt
	 */
	public PendenciaResponsavelDt getResponsavel() {
		// Verifica se a lista esta preenchida
		if (this.getResponsaveis() == null
				|| this.getResponsaveis().size() <= 0)
			return null;

		return (PendenciaResponsavelDt) this.getResponsaveis().get(0);
	}

	/**
	 * Retorna o nome do responsavel caso possua somente um
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 17/10/2008 09:27
	 * @return String
	 */
	public String getNomeResponsavel() {
		String nome = "Nâo especificado";

		if (this.getResponsaveis() != null) {
			if (this.getResponsaveis().size() == 1) {
				PendenciaResponsavelDt responsavel = (PendenciaResponsavelDt) this
						.getResponsaveis().get(0);

				if (responsavel != null) {
					nome = responsavel.getServentia();

					if (!this.getUsuarioFinalizador().trim().equals(""))
						nome += " / " + this.getNomeUsuarioFinalizador();
				}
			} else {
				nome = "Vários responsáveis";
			}
		}

		return nome;
	}

	/**
	 * Retorna uma pendencia filha
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 01/10/2008 10:37
	 * @return PendenciaDt
	 */
	public PendenciaDt criarFilha() {
		PendenciaDt novaPendenciaDt = new PendenciaDt();
		novaPendenciaDt.copiar(this);

		// Modifica os dados descritos nos itens 2.1.X
		novaPendenciaDt.setDataInicio(Funcoes.DataHora(new java.util.Date()));
		novaPendenciaDt.setDataFim(""); // Devido a logica dos sets
		novaPendenciaDt.setId_PendenciaPai(this.getId());
		novaPendenciaDt.setId(""); // Limpa o id da pendencia
		novaPendenciaDt.setId_UsuarioFinalizador("null");

		novaPendenciaDt.setPendenciaStatusCodigo(this.getPendenciaStatusCodigo());
		novaPendenciaDt.setPendenciaTipoCodigo(this.getPendenciaTipoCodigo());

		return novaPendenciaDt;
	}

	/**
	 * Retorna uma pendencia filha que herda da pendencia pai a data de início
	 * da mesma.
	 *
	 * @param PendenciaDt
	 *            - pendencia pai
	 * @return PendenciaDt - pendência filha
	 * @author hmgodinho
	 * @since 25/11/2014
	 */
	public PendenciaDt criarFilhaPreAnalise(PendenciaDt pendenciaPai) {
		PendenciaDt novaPendenciaDt = new PendenciaDt();
		novaPendenciaDt.copiar(this);

		// Modifica os dados descritos nos itens 2.1.X
		novaPendenciaDt.setDataInicio(pendenciaPai.getDataInicio());
		novaPendenciaDt.setDataFim(""); // Devido a logica dos sets
		novaPendenciaDt.setId_PendenciaPai(this.getId());
		novaPendenciaDt.setId(""); // Limpa o id da pendencia
		novaPendenciaDt.setId_UsuarioFinalizador("null");

		novaPendenciaDt.setPendenciaStatusCodigo(this
				.getPendenciaStatusCodigo());
		novaPendenciaDt.setPendenciaTipoCodigo(this.getPendenciaTipoCodigo());

		return novaPendenciaDt;
	}

	/**
	 * Retorna uma pendencia filha com usuario cadastrador igual ao usuario
	 * finalizadar da pendencia pai.
	 * 
	 * @author Leandro Bernardes
	 * @since 18/06/2009
	 * @return PendenciaDt
	 */
	public PendenciaDt criarPendenciaFilhaExpedida() {
		PendenciaDt novaPendenciaDt = new PendenciaDt();
		novaPendenciaDt.copiar(this);

		// Modifica os dados descritos nos itens 2.1.X
		novaPendenciaDt.setDataInicio(Funcoes.DataHora(new java.util.Date()));
		novaPendenciaDt.setDataFim(""); // Devido a logica dos sets
		novaPendenciaDt.setId_PendenciaPai(this.getId());
		novaPendenciaDt.setId(""); // Limpa o id da pendencia
		novaPendenciaDt.setId_UsuarioFinalizador("null");
		novaPendenciaDt.setDataTemp("");
		novaPendenciaDt.setDataVisto("");

		// O usuário que finaliza a pendência pai é o cadastrador da pendência
		// filha
		novaPendenciaDt.setId_UsuarioCadastrador(this
				.getId_UsuarioFinalizador());
		novaPendenciaDt.setUsuarioCadastrador(this.getUsuarioFinalizador());

		novaPendenciaDt.setPendenciaStatusCodigo(this
				.getPendenciaStatusCodigo());
		novaPendenciaDt.setPendenciaTipoCodigo(this.getPendenciaTipoCodigo());

		return novaPendenciaDt;
	}

	/**
	 * Limpa os dados da pendencia
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 28/11/2008 11:12
	 */
	public void limparDados() {
		this.setDataFim("");
		this.setDataVisto("");
		this.setId_UsuarioFinalizador("null");
		this.setId_PendenciaStatus("null");
		this.ExpedicaoAutomatica=false;
	}

	public void limpar() {
		super.limpar();
		magistradoResponsaveisAtuais = new ArrayList();
		statusPendenciaRetorno = "";
		listaStatusAguardandoRetorno = null;
		pendenciaStatusCodigo = "";
		processoPrioridadeCodigo = "";
		serventiaUsuarioFinalizador = "";
		nomeUsuarioFinalizador = "";
		hash = "";
		envia_Magistrado = false;
		id_ServentiaGrupo = "";
		serventiaGrupo = "";
		id_Bairro = "";
		bairro = "";
		id_MandadoJudicial = "";
		id_MandadoTipo = "";
		id_EnderecoParte = "";
		id_Area = "";
		id_Zona = "";
		id_Regiao = "";
		id_Escala = "";
		id_MandadoJudicialStatus = "";
		serventiaUsuarioCadastrador = "";
		valor = "";
		assistencia = "";
		mandadoTipo = "";
		prazoMandado = "";
		codigoPrazoMandado = "";
		processoTipo = "";
		codModeloCorreio ="";
	    maoPropriaCorreio = "";
	    id_ProcessoCustaTipo = "";
		setProcessoTipoSessao("");
		setId_ProcessoTipoSessao("");
		id_EventoTipo = "";
		eventoTipo = "";
		dataEvento = "";
		listaHistoricoPendencia = null;
		idModelo = "";
		idPendenciaCorreios = "";
	}

	/**
	 * Adiciona um novo responsavel para pendencia
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 06/06/2008 11:46
	 * @param PendenciaResponsavelDt
	 *            responsavel, responsavel a ser adicionado
	 * @return boolean
	 */
	public boolean addResponsavel(PendenciaResponsavelDt responsavel) {
		if (this.responsaveis == null)
			this.responsaveis = new ArrayList();

		return this.responsaveis.add(responsavel);
	}

	/**
	 * Remove um responsavel da pendencia
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 06/06/2008 11:48
	 * @param PendenciaResponsavelDt
	 *            responsavel, responsavel a ser adicionado
	 * @return boolean
	 */
	public boolean removerResponsavel(PendenciaResponsavelDt responsavel) {
		if (this.responsaveis != null)
			return this.responsaveis.remove(responsavel);

		return false;
	}

	/**
	 * Adiciona um novo responsavel
	 * 
	 * @author lsbernardes
	 * @param ServentiaCargoDt
	 *            responsavel, responsavel a ser adicionado
	 * @return boolean
	 */
	public boolean addMagistradoResponsavelAtual(ServentiaCargoDt responsavel) {
		if (this.magistradoResponsaveisAtuais == null)
			this.magistradoResponsaveisAtuais = new ArrayList();
		return this.magistradoResponsaveisAtuais.add(responsavel);
	}

	/**
	 * Remove um responsavel da pendencia
	 * 
	 * @author lsbernardes
	 * @param ServentiaCargoDt
	 *            responsavel, responsavel a ser adicionado
	 * @return boolean
	 */
	public boolean removerMagistradoResponsavelAtual(
			ServentiaCargoDt responsavel) {
		if (this.magistradoResponsaveisAtuais != null)
			return this.magistradoResponsaveisAtuais.remove(responsavel);
		return false;
	}

	/**
	 * Calcula ate qual momento estara reservado a pendencia
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 24/04/2008 - 09:53
	 * @return Date
	 */
	public Date reservadoAte() {
		Date reserva = null;

		if (this.getDataTemp().trim().equals("") == false) {
			// Verifica se e uma data valida
			if (Funcoes.validaDataHora(this.getDataTemp())) {
				try {
					// Converte a data para formato date
					reserva = Funcoes.DataHora(this.getDataTemp());

					// Adiciona o tempo fixado
					Calendar cal = Calendar.getInstance();
					cal.setTime(reserva);

					cal.add(Calendar.HOUR, Configuracao.IntervaloReservadoTemp);

					reserva = cal.getTime();

				} catch (Exception ex) {
				}
			}
		}
		return reserva;
	}

	/**
	 * Formata reserva de data
	 * 
	 * @author Ronneesley Moura teles
	 * @since 24/04/2008 - 09:59
	 * @return String
	 */
	public String reservadoAteFormatado() {
		Date data = this.reservadoAte();
		String reserva = "";

		if (data != null) {

			SimpleDateFormat sp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			reserva = sp.format(data);

		}

		return reserva;

	}

	/**
	 * Retorna se a pendencia esta respondida
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 16/12/2008 14:13
	 * @return true se a pendencia foi respondida e false se a pendencia nao foi
	 *         respondida
	 */
	public boolean estaRespondida() {
		if (this.getDataFim() != null
				&& !this.getDataFim().trim().equals("")
				&& Funcoes.StringToInt(this.getPendenciaStatusCodigo()) != PendenciaStatusDt.ID_AGUARDANDO_RETORNO)
			return true;

		return false;
	}

	public List getResponsaveis() {
		return responsaveis;
	}

	public void setResponsaveis(List responsaveis) {
		this.responsaveis = responsaveis;
	}

	public List getMagistradoResponsaveisAtuais() {
		return magistradoResponsaveisAtuais;
	}

	public void setMagistradoResponsaveisAtuais(List responsaveis) {
		this.magistradoResponsaveisAtuais = responsaveis;
	}

	public void setListaArquivos(List listaArquivos) {
		ListaArquivos = listaArquivos;
	}

	public List getListaArquivos() {
		return ListaArquivos;
	}

	public void addListaArquivos(Object dt) {
		if (ListaArquivos == null)
			ListaArquivos = new ArrayList();
		this.ListaArquivos.add(dt);
	}

	public void setPendenciaStatusCodigo(String valor) {
		if (valor != null)
			pendenciaStatusCodigo = valor;
	}

	public String getPendenciaStatusCodigo() {
		return pendenciaStatusCodigo;
	}

	public String getProcessoPrioridadeCodigo() {
		return processoPrioridadeCodigo;
	}

	public void setProcessoPrioridadeCodigo(String valor) {
		if (valor != null){
			processoPrioridadeCodigo = valor;
		}
	}

	public ProcessoDt getProcessoDt() {
		return processoDt;
	}

	public void setProcessoDt(ProcessoDt processoDt) {
		this.processoDt = processoDt;
	}

	public void copiar(PendenciaDt objeto) {
		super.copiar(objeto);
		serventiaUsuarioFinalizador = objeto.getServentiaUsuarioFinalizador();
		nomeUsuarioFinalizador = objeto.getNomeUsuarioFinalizador();
		pendenciaStatusCodigo = objeto.getPendenciaStatusCodigo();
		processoPrioridadeCodigo = objeto.getProcessoPrioridadeCodigo();
		hash = objeto.getHash();
		envia_Magistrado = objeto.isEnviaMagistrado();
		id_ServentiaGrupo = objeto.getId_ServentiaGrupo();
		serventiaGrupo = objeto.getServentiaGrupo();
		id_Bairro = objeto.getId_Bairro();
		bairro = objeto.getBairro();
		id_MandadoJudicial = objeto.getId_MandadoJudicial();
		id_MandadoTipo = objeto.getId_MandadoTipo();
		id_EnderecoParte = objeto.getId_EnderecoParte();
		id_Area = objeto.getId_Area();
		id_Zona = objeto.getId_Zona();
		id_Regiao = objeto.getId_Regiao();
		id_Escala = objeto.getId_Escala();
		id_MandadoJudicialStatus = objeto.getId_MandadoJudicialStatus();
		serventiaUsuarioCadastrador = objeto.getServentiaUsuarioCadastrador();
		valor = objeto.getValor();
		assistencia = objeto.getAssistencia();
		mandadoTipo = objeto.getMandadoTipo();
		super.setId_UsuarioLog(objeto.getId_UsuarioLog());
		super.setIpComputadorLog(objeto.getIpComputadorLog());
		processoTipoSessao = objeto.getProcessoTipoSessao();
		id_ProcessoTipoSessao = objeto.getId_ProcessoTipoSessao();
		maoPropriaCorreio = objeto.getMaoPropriaCorreio();
		codModeloCorreio = objeto.getCodModeloCorreio();
		id_ProcessoCustaTipo = objeto.getId_ProcessoCustaTipo();
		ordemServico = objeto.getOrdemServico();
		id_EventoTipo = objeto.getId_EventoTipo();
		eventoTipo = objeto.getEventoTipo();
		dataEvento = objeto.getDataEvento();
	}

	public String getStatusPreAnalise() {
		return PendenciaStatusDt.getStatusPreAnalise(Funcoes
				.StringToInt(pendenciaStatusCodigo));
	}

	public boolean isAdvogadoPrincipal() {
		return advogadoPrincipal;
	}

	public void setAdvogadoPrincipal(boolean advogadoPrincipal) {
		this.advogadoPrincipal = advogadoPrincipal;
	}

	public String getServentiaUsuarioCadastrador() {
		return serventiaUsuarioCadastrador;
	}

	public void setServentiaUsuarioCadastrador(
			String serventiaUsuarioCadastrador) {
		this.serventiaUsuarioCadastrador = serventiaUsuarioCadastrador;
	}

	public String getId_ServentiaProcesso() {
		return id_ServentiaProcesso;
	}

	public void setId_ServentiaProcesso(String idServentiaProcesso) {
		if (idServentiaProcesso != null)
			id_ServentiaProcesso = idServentiaProcesso;
	}

	public void setId_ServentiaCargo(String id_ServentiaCargo) {
		if (id_ServentiaCargo != null)
			this.id_ServentiaCargo = id_ServentiaCargo;
	}

	public String getId_ServentiaCargo() {
		return id_ServentiaCargo;
	}

	public boolean isEnviaMagistrado() {
		return envia_Magistrado;
	}

	public void setEnviaMagistrado(boolean envia_Magistrado) {
		this.envia_Magistrado = envia_Magistrado;
	}

	public String getId_ServentiaGrupo() {
		return id_ServentiaGrupo;
	}

	public void setId_ServentiaGrupo(String idServentiaGrupo) {
		if (idServentiaGrupo != null)
			this.id_ServentiaGrupo = idServentiaGrupo;
	}

	public String getServentiaGrupo() {
		return serventiaGrupo;
	}

	public void setServentiaGrupo(String serventiaGrupo) {
		if (serventiaGrupo != null)
			this.serventiaGrupo = serventiaGrupo;
	}

	public boolean estaReservada() {
		Date reserva = reservadoAte();
		if (reserva == null)
			return false;
		return (new Date()).before(reserva);
	}

	public int getPendenciaTipoCodigoToInt() {
		return Funcoes.StringToInt(getPendenciaTipoCodigo(), -1);
	}

	// jvosantos - 28/01/2020 17:43 - Adicionar método para retornar inteiro
	public int getPendenciaStatusCodigoToInt() {
		return Funcoes.StringToInt(getPendenciaStatusCodigo(), -1);
	}

	public boolean temDataFim() {
		if (getDataFim() != null && getDataFim().length() > 0)
			return true;
		return false;
	}

	public boolean temDataVisto() {
		if (getDataVisto() != null && getDataVisto().length() > 0)
			return true;
		return false;
	}

	public boolean temPrazo() {
		if (getPrazo() != null && getPrazo().length() > 0)
			return true;
		return false;
	}

	public void setId_Bairro(String valor) {
		if (valor != null)
			this.id_Bairro = valor;
	}

	public String getId_Bairro() {
		return id_Bairro;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String valor) {
		if (valor != null)
			bairro = valor;
	}

	public String getId_MandadoJudicial() {
		return id_MandadoJudicial;
	}

	public void setId_MandadoJudicial(String valor) {
		if (valor != null)
			id_MandadoJudicial = valor;
	}

	public String getId_MandadoTipo() {
		return id_MandadoTipo;
	}

	public void setId_MandadoTipo(String valor) {
		if (valor != null)
			id_MandadoTipo = valor;
	}

	public String getId_EnderecoParte() {
		return id_EnderecoParte;
	}

	public void setId_EnderecoParte(String valor) {
		if (valor != null)
			id_EnderecoParte = valor;
	}

	public String getId_Area() {
		return id_Area;
	}

	public void setId_Area(String valor) {
		if (valor != null)
			id_Area = valor;
	}

	public String getId_Zona() {
		return id_Zona;
	}

	public void setId_Zona(String valor) {
		if (valor != null)
			id_Zona = valor;
	}

	public String getId_Regiao() {
		return id_Regiao;
	}

	public void setId_Regiao(String valor) {
		if (valor != null)
			id_Regiao = valor;
	}

	public String getId_Escala() {
		return id_Escala;
	}

	public void setId_Escala(String valor) {
		if (valor != null)
			id_Escala = valor;
	}

	public String getId_MandadoJudicialStatus() {
		return id_MandadoJudicialStatus;
	}

	public void setId_MandadoJudicialStatus(String valor) {
		if (valor != null)
			id_MandadoJudicialStatus = valor;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		if (valor != null)
			this.valor = valor;
	}

	public String getAssistencia() {
		return assistencia;
	}

	public void setAssistencia(String valor) {
		if (valor != null)
			assistencia = valor;
	}

	public String getMandadoTipo() {
		return mandadoTipo;
	}

	public void setMandadoTipo(String valor) {
		if (valor != null)
			mandadoTipo = valor;
	}

	public List getListaTiposMandados() {
		return listaTiposMandados;
	}

	public void setListaTiposMandados(List valor) {
		if (valor != null)
			listaTiposMandados = valor;
	}

	public String getId_UsuarioServentia1() {
		return id_UsuarioServentia1;
	}

	public void setId_UsuarioServentia1(String valor) {
		if (valor != null)
			id_UsuarioServentia1 = valor;
	}

	public String getId_UsuarioServentia2() {
		return id_UsuarioServentia2;
	}

	public void setId_UsuarioServentia2(String valor) {
		if (valor != null)
			id_UsuarioServentia2 = valor;
	}

	public String getNomeTipoMandado(String id) {
		String retorno = "";
		MandadoTipoDt mandadoTipoDt = null;

		for (int i = 0; i < listaTiposMandados.size(); i++) {
			mandadoTipoDt = (MandadoTipoDt) listaTiposMandados.get(i);
			if (mandadoTipoDt.getId().equalsIgnoreCase(id)) {
				retorno = mandadoTipoDt.getMandadoTipo();
				break;
			}
		}

		return retorno;
	}

	public boolean eMandado() {
		if (getPendenciaTipoCodigo() != null
				&& getPendenciaTipoCodigoToInt() == PendenciaTipoDt.MANDADO) {
			return true;
		}
		return false;
	}

	public boolean eCartaPrecatoria() {
		if (getPendenciaTipoCodigo() != null
				&& getPendenciaTipoCodigoToInt() == PendenciaTipoDt.CARTA_PRECATORIA) {
			return true;
		}
		return false;
	}

	public boolean isIntimacaCitacao() {
		if (getPendenciaTipoCodigo() != null
				&& (getPendenciaTipoCodigoToInt() == PendenciaTipoDt.INTIMACAO || getPendenciaTipoCodigoToInt() == PendenciaTipoDt.INTIMACAO_AUDIENCIA
						|| getPendenciaTipoCodigoToInt() == PendenciaTipoDt.CARTA_CITACAO || getPendenciaTipoCodigoToInt() == PendenciaTipoDt.CARTA_CITACAO_AUDIENCIA)
			) {
			return true;
		}
		return false;
	}

	public boolean isArquivamento() {
		if (getPendenciaTipoCodigo() != null && getPendenciaTipoCodigoToInt() == PendenciaTipoDt.ARQUIVAMENTO) {
			return true;
		}
		return false;
	}
	
	public String getProcessoTipoSessao() {
		return processoTipoSessao;
	}

	public void setProcessoTipoSessao(String processoTipoSessao) {
		if (processoTipoSessao != null)
			this.processoTipoSessao = processoTipoSessao;
	}

	public String getId_ProcessoTipoSessao() {
		return id_ProcessoTipoSessao;
	}

	public void setId_ProcessoTipoSessao(String id_ProcessoTipoSessao) {
		if (id_ProcessoTipoSessao != null)
			this.id_ProcessoTipoSessao = id_ProcessoTipoSessao;
	}

	public boolean isProcessoPrioridadeCodigo() {
		if (this.getProcessoPrioridadeCodigo() != null
				&& this.getProcessoPrioridadeCodigo().length() > 0) {
			return true;
		}

		return false;
	}
	
	public boolean isConclusao() {
		return PendenciaTipoDt.Conclusoes.contains(Funcoes.StringToInt(getPendenciaTipoCodigo()));		
	}

	public boolean isResponsavelUsuarioServentia(String id_UsuarioServentia) {
		if (this.responsaveis != null) {
			for (int i = 0; i< this.responsaveis.size(); i++) {
				PendenciaResponsavelDt responsavelDt = (PendenciaResponsavelDt) this.responsaveis.get(i);
				if (Funcoes.StringToLong(responsavelDt.getId_UsuarioResponsavel()) == Funcoes.StringToLong(id_UsuarioServentia)) return true;
			}
		}		
		return false;
	}	
	
	public boolean isResponsavelServentiaCargo(String id_ServentiaCargo) {
		if (this.responsaveis != null) {
			for (int i = 0; i< this.responsaveis.size(); i++) {
				PendenciaResponsavelDt responsavelDt = (PendenciaResponsavelDt) this.responsaveis.get(i);
				if (Funcoes.StringToLong(responsavelDt.getId_ServentiaCargo()) == Funcoes.StringToLong(id_ServentiaCargo)) return true;
			}
		}		
		return false;
	}

	public String getProcessoTipo() {
		return processoTipo;
	}

	public void setProcessoTipo(String processoTipo) {
		if (processoTipo != null)
			this.processoTipo = processoTipo;
	}
	
	public String getId_Procurador() {
		return id_Procurador;
	}
	
	public void setId_Procurador(String id_Procurador) {
		this.id_Procurador = id_Procurador;
	}
	
	public String getProcurador() {
		return nome_Procurador;
	}
	
	public void setProcurador(String nome_Procurador) {
		if(nome_Procurador != null)
			this.nome_Procurador = nome_Procurador;
	}
	
	public String getId_Promotor() {
		return id_Promotor;
	}
	
	public void setId_Promotor(String id_Promotor) {
		this.id_Promotor = id_Promotor;
	}
	
	public String getPromotor() {
		return nome_Promotor;
	}
	
	public void setPromotor(String nome_Promotor) {
		if(nome_Promotor != null)
			this.nome_Promotor = nome_Promotor;
	} 
	
	public String getNome_UsuarioServentia2() {
		return nome_UsuarioServentia2;
	}

	public void setNome_UsuarioServentia2(String nome_UsuarioServentia2) {
		this.nome_UsuarioServentia2 = nome_UsuarioServentia2;
	}

	public String getProcessoPrioridadeCodigoTexto(){
		int prioridade = Funcoes.StringToInt(getProcessoPrioridadeCodigo());
		
		if (prioridade == ProcessoPrioridadeDt.MAIOR_80_ANOS){				
			return  "Maior de 80 anos";
		}else if (prioridade == ProcessoPrioridadeDt.MAIOR_60_ANOS){				
			return  "Maior de 60 anos";
		}else if (prioridade == ProcessoPrioridadeDt.DESTITUICAO_PODER_FAMILIAR){
			return  "Destituição do Proder Familiar";
		}else if (prioridade == ProcessoPrioridadeDt.PESSOA_COM_DEFICIENCIA){
			return  "Pessoa com Deficiência";
		}else if (prioridade == ProcessoPrioridadeDt.ADOCAO){		
			return "Adoção";			
		}else if (prioridade == ProcessoPrioridadeDt.DOENCA_GRAVE){				
			return  "Doença Grave";
		}else if (prioridade == ProcessoPrioridadeDt.LIMINAR){				
			return  "Pedido de Liminar";
		}else if (prioridade == ProcessoPrioridadeDt.ANTECIPACAO_TUTELA){				
			return  "Antecipação de Tutela";
		}else if (prioridade == ProcessoPrioridadeDt.REU_PRESO){				
			return  "Réu Preso";
		}else if (prioridade == ProcessoPrioridadeDt.NORMAL){				
			return  "Normal";
		}
		return "";		
	}
	
	public String getPendenciaPrioridadeCodigoTexto()
	{
		int prioridade = Funcoes.StringToInt(getPendenciaPrioridadeCodigo());
		
		if (prioridade == ProcessoPrioridadeDt.MAIOR_80_ANOS){				
			return  "Maior de 80 anos";		
		}else if (prioridade == ProcessoPrioridadeDt.MAIOR_60_ANOS){				
			return  "Maior de 60 anos";
		}else if (prioridade == ProcessoPrioridadeDt.DESTITUICAO_PODER_FAMILIAR){
			return  "Destituição do Proder Familiar";
		}else if (prioridade == ProcessoPrioridadeDt.PESSOA_COM_DEFICIENCIA){
			return  "Pessoa com Deficiência";
		}else if (prioridade == ProcessoPrioridadeDt.ADOCAO){		
			return "Adoção";			
		}else if (prioridade == ProcessoPrioridadeDt.DOENCA_GRAVE){				
			return  "Doença Grave";
		}else if (prioridade == ProcessoPrioridadeDt.LIMINAR){				
			return  "Pedido de Liminar";
		}else if (prioridade == ProcessoPrioridadeDt.ANTECIPACAO_TUTELA){				
			return  "Antecipação de Tutela";
		}else if (prioridade == ProcessoPrioridadeDt.REU_PRESO){				
			return  "Réu Preso";
		}else if (prioridade == ProcessoPrioridadeDt.NORMAL){				
			return  "Normal";
		}
		return "";		
	}
	
	public boolean isProcessoPrioridade(){
		int prioridade = Funcoes.StringToInt(getProcessoPrioridadeCodigo());
		if (prioridade != ProcessoPrioridadeDt.NORMAL){
			return true;
		}
		return false;
		
	}
	
	public String getNumeroImagemPrioridade(){	
		return  getNumeroImagemPrioridade (getProcessoPrioridadeCodigo());
	}
	
	public String getNumeroImagemPrioridade(String codigo){
		String stTemp="0";
		switch (codigo) {
			case "1":
			case "2":
			case "3":
			case "4":
			case "8":
			case "9":
				stTemp="3";
				break;
			case "5":
			case "6":
				stTemp="4";
			break;

		default:
			stTemp="0";
		}
		return stTemp;
	}
	
	public boolean isEmAndamento() {
		int codigoStatus = Funcoes.StringToInt(getPendenciaStatusCodigo());
		if (codigoStatus == PendenciaStatusDt.ID_EM_ANDAMENTO){
			return true;
		}				
		return false;
	}
	
	public boolean isAguardandoRetorno() {
		int codigoStatus = Funcoes.StringToInt(getPendenciaStatusCodigo());
		if (codigoStatus == PendenciaStatusDt.ID_AGUARDANDO_RETORNO){
			return true;
		}				
		return false;
	}

	public boolean isVerificar() {
		int pendenciaTipoCodigo = getPendenciaTipoCodigoToInt();
		if (pendenciaTipoCodigo== PendenciaTipoDt.VERIFICAR_NOVO_PROCESSO || pendenciaTipoCodigo== PendenciaTipoDt.VERIFICAR_NOVA_PRECATORIA ||//pendenciaTipoCodigo == PendenciaTipoDt.VERIFICAR_HOMOLOGACAO_AUDIENCIA_CEJUSC  ||
			pendenciaTipoCodigo== PendenciaTipoDt.VERIFICAR_PROCESSO || pendenciaTipoCodigo == PendenciaTipoDt.VERIFICAR_PROCESSO_HIBRIDO	){
			return true;
		}
		return false;
	}

	public boolean isElaboracaoVoto() {
		int codigoTipo = Funcoes.StringToInt(getPendenciaTipoCodigo());
		if (codigoTipo == PendenciaTipoDt.ELABORACAO_VOTO){
			return true;
		}				
		return false;
	}
	
	public boolean isPendenciaTipoMandado(){
		if( Funcoes.StringToInt(this.getPendenciaTipoCodigo()) == PendenciaTipoDt.MANDADO ){
			return true;
		}
		return false;
	}

	public Integer getCodigoPendTemp() {
		return codigoPendTemp;
	}

	public void setCodigoPendTemp(Integer codigoPendTemp) {
		this.codigoPendTemp = codigoPendTemp;
	}
	
	/**
	 * Metodo que verifica se uma pendência pode ter o seu tipo trocado
	 * 
	 * @author lsbernardes
	 */
	public boolean isTrocarTipo() {
		if (getPendenciaTipoCodigo() != null
				&& (getPendenciaTipoCodigoToInt() == PendenciaTipoDt.INTIMACAO || getPendenciaTipoCodigoToInt() == PendenciaTipoDt.CARTA_CITACAO
						|| getPendenciaTipoCodigoToInt() == PendenciaTipoDt.INTIMACAO_AUDIENCIA || getPendenciaTipoCodigoToInt() == PendenciaTipoDt.CARTA_CITACAO_AUDIENCIA
						|| getPendenciaTipoCodigoToInt() == PendenciaTipoDt.CARTA_PRECATORIA || getPendenciaTipoCodigoToInt() == PendenciaTipoDt.MANDADO
						|| PendenciaTipoDt.isCalculoContadoria(getPendenciaTipoCodigoToInt()))
				
			) {
			return true;
		}
		return false;
	}


	public boolean isStatus(int status) {
		// TODO Auto-generated method stub
		return Funcoes.StringToInt(getId_PendenciaStatus(),-99) ==status;
	}

	public boolean isExpedicaoAutomatica() {
		return ExpedicaoAutomatica;
	}

	public boolean isMandado() {
		if (getPendenciaTipoCodigo() != null && getPendenciaTipoCodigoToInt() == PendenciaTipoDt.MANDADO) {
			return true;
		}
		return false;
	}
	
	public void setExpedicaoAutomatica(boolean expedicaoAutomatica) {
		ExpedicaoAutomatica = expedicaoAutomatica;		
	}

	public String getCodModeloCorreio() {
		return codModeloCorreio;
	}

	public void setCodModeloCorreio(String codModeloCorreio) {
		this.codModeloCorreio = codModeloCorreio;
	}
	
	public String getId_ProcessoCustaTipo() {
		return id_ProcessoCustaTipo;
	}

	public void setId_ProcessoCustaTipo(String id_ProcessoCustaTipo) {
		this.id_ProcessoCustaTipo = id_ProcessoCustaTipo;
	}

	public String getMaoPropriaCorreio() {
		return maoPropriaCorreio;
	}

	public void setMaoPropriaCorreio(String maoPropriaCorreio) {
		this.maoPropriaCorreio = maoPropriaCorreio;
	}

	public String getId_OficialAdhoc() {
		return id_OficialAdhoc;
	}

	public void setId_OficialAdhoc(String id_OficialAdhoc) {
		this.id_OficialAdhoc = id_OficialAdhoc;
	}

	public String getOficialAdhoc() {
		return oficialAdhoc;
	}

	public void setOficialAdhoc(String oficialAdhoc) {
		this.oficialAdhoc = oficialAdhoc;
	}

	public String getPrazoMandado() {
		return prazoMandado;
	}

	public void setPrazoMandado(String prazoMandado) {
		this.prazoMandado = prazoMandado;
	}

	public String getCodigoPrazoMandado() {
		return codigoPrazoMandado;
	}

	public void setCodigoPrazoMandado(String codigoPrazoMandado) {
		this.codigoPrazoMandado = codigoPrazoMandado;
	}
	
	public String getOrdemServico() {
		return ordemServico;
	}

	public void setOrdemServico(String ordemServico) {
		this.ordemServico = ordemServico;
	}
	
	public String getDataEvento()  {
		return dataEvento;
	}
	
	public void setDataEvento(String valor ) {
		if (valor!=null) {
			dataEvento = valor;
		}
	}
	
	public String getId_EventoTipo()  {
		return id_EventoTipo;
	}
	
	public void setId_EventoTipo(String valor ) {
		if (valor!=null) {
			if (valor.equalsIgnoreCase("null")) { 
				id_EventoTipo = ""; eventoTipo = "";
			} else if (!valor.equalsIgnoreCase("")) {
				id_EventoTipo = valor;
			}
		}
	}
	
	public String getEventoTipo()  {
		return eventoTipo;
	}
	
	public void setEventoTipo(String valor ) { 
		if (valor!=null) {
			eventoTipo = valor;
		}
	}
	
	public List getListaHistoricoPendencia() {
		return listaHistoricoPendencia;
	}

	public void setListaHistoricoPendencia(List listaHistoricoPendencia) {
		if (listaHistoricoPendencia != null) this.listaHistoricoPendencia = listaHistoricoPendencia;
	}

	public String getNumeroReservadoMandadoExpedir() {
		return numeroReservadoMandadoExpedir;
	}

	public void setNumeroReservadoMandadoExpedir(String numeroReservadoMandadoExpedir) {
		this.numeroReservadoMandadoExpedir = numeroReservadoMandadoExpedir;
	}

	public String getIdModelo() {
		return idModelo;
	}

	public void setIdModelo(String idModelo) {
		this.idModelo = idModelo;
	}
	
	public boolean isTemId() {
		return getId()!=null && !getId().isEmpty();
	}

	public void limparListaResponsaveis() {
		this.responsaveis = new ArrayList();		
	}

	public boolean isIndicadorVotoVencido() {
		return this.getCodigoTemp() != null &&
			   this.getCodigoTemp().equalsIgnoreCase(String.valueOf(PendenciaDt.VOTO_VENCIDO_RELATOR));
	}
	
	public void limparCodigoTemp() {
		this.setCodigoTemp("");
	}
	
	public String getIdPendenciaCorreios() {
		return idPendenciaCorreios;
	}

	public void setIdPendenciaCorreios(String valor) {
		this.idPendenciaCorreios = valor;
	}

}