package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.List;

/**
 * Pacote de dados para a pagina inicial
 * @author Ronneesley Moura Teles
 * @since 02/12/2008 15:30
 */
public class PaginaInicialDt extends Dados {

	private static final long serialVersionUID = -4349300290067713847L;
	/**
	 * Lista de dados da serventia que serão mostrados: qtde processos, qtde audiencias
	 */
	private List dadosServentia;
	
	/**
	 * Lista de processo com possivel prescrição
	 */
	//private List dadosServentiaPrescricao;
	
	/**
	 * Lista de pendencias da serventia
	 */
	private List pendenciasServentia;

	/**
	 * Lista de pendencias de um serventia cargo
	 */
	private List pendenciasServentiaCargo;

	/**
	 * Lista de pendencias de um usuario serventia
	 */
	private List pendenciasUsuarioServentia;
	
	/**
	 * Lista de pendencias de um usuario serventia Lidas automaticamente
	 */
	private List pendenciasUsuarioServentiaLidasAutomaticamente;

	/**
	 * Lista de pendencias para serventia tipo
	 */
	private List pendenciasServentiaTipo;
	
	/**
	 * Lista de voto de sessão virtual
	 */
	/**
	 * Lista de pendencias para serventia - Mandado de prisão
	 */
	private List pendenciasServentiaMandadoPrisao;

	/**
	 * Lista de pendencias intimaçoes Aguardando Parecer
	 */
	private List pendenciasIntimacoesAguardandoParecer;

	/**
	 * Lista de pendencias intimaçoes lidas automaticamente Aguardando Parecer
	 */
	private List pendenciasIntimacoesLeituraAutomaticaAguardandoParecer;
	
	/**
	 * Lista de pendencias intimaçoes publicadas no diario eletronico Aguardando Parecer
	 */
	private List pendenciasIntimacoesPublicadasDiarioEletronicoAguardandoParecer;

	/**
	 * Lista de pendencias intimaçoes publicadas no diario eletronico Aguardando Parecer
	 */
	private List pendenciasVerificarImpedimento;
	
	/**
	 * Lista de conclusões
	 */
	private List conclusoes;

	/**
	 * Lista de conclusões
	 */
	private List pendenciasAnalise;

	/**
	 * Lista de pendencias de Apreciados
	 */
	// jvosantos - 04/06/2019 09:44 - Adicionar lista de pendencias de apreciados
	private List pendenciasApreciados;

	/**
	 * Quantidade de pendências na serventia com prazo decorrido
	 */
	private long qtdePrazoDecorrido;
	
	/**
	 * Quantidade de pendências na serventia com prazo a decorrer
	 */
	private long qtdePrazoADecorrer;
	
	/**
	 * Quantidade de pendências na serventia com prazo decorrido Devolucao Autos
	 */
	private long qtdePrazoDecorridoDevolucaoAutos;
	
	private long qtdeSessaoVirtualEmVotacao;

	private int qtdConclusoesGabinete=0;
	/**
	 * Quantidade de pendências do tipo liberar acesso para um serventia cargo
	 */
	private long qtdePendenciaLiberaAcesso;

	/**
	 * Quantidade de pendências do tipo liberar acesso para um serventia cargo
	 */
	private long qtdePendenciaInformativa;

	/**
	 * Quantidade de pendências expedidas para serventias on-line e que estao aguardando visto
	 */
	private long qtdExpedidasAguardandoVisto;

	/**
	 * Quantidade de pendências em andamento
	 */
	private long qtdPendenciasEmAndamento;

	/**
	 * Quantidade de pré-análises múltiplas para outras pendências
	 */
	private long qtdePreAnalisesMultiplasPendencias;

	/**
	 * Quantidade de pré-análises múltiplas para conclusões
	 */
	private long qtdePreAnalisesMultiplasConclusoes;
	
	/**
	 * Quantidade de Sessões aguardando acórdão
	 */
	private long qtdeSessoesAguardandoAcordaoPreAnalisadas;
	private long qtdeSessoesAguardandoAcordaoPreAnalisadasVirtual;
	
	/**
	 * Quantidade de Sessões aguardando acórdão
	 */
	private long qtdeSessoesAguardandoAcordaoNaoAnalisadas;
	private long qtdeSessoesAguardandoAcordaoNaoAnalisadasVirtual;
	
	/**
	 * Quantidade de Sessões aguardando acórdão aguardando assinatura
	 */
	private long qtdeSessoesAguardandoAcordaoAguardandoAssinatura;
	private long qtdeSessoesAguardandoAcordaoAguardandoAssinaturaVirtual;
	
	private long qtdeVotosPreAnalisadosEmLote;
	private long qtdeVotoNaoAnalisadas;
	private long qtdeVotoPreAnalisadas;
	private long qtdeVotoAguardandoAssinatura;
	
	private long qtdeVotoRenovarNaoAnalisadas;
	private long qtdeVotoRenovarPreAnalisadas;
	private long qtdeVotoRenovarAguardandoAssinatura;

	private long qtdeFinalizarVotoNaoAnalisadas;
	private long qtdeFinalizarVotoPreAnalisadas;
	private long qtdeFinalizarVotoAguardandoAssinatura;
	
	// jvosantos - 08/01/2020 16:54 - Adicionar pendência de "Erro Material"
	private long qtdeErroMaterialNaoAnalisadas;
	private long qtdeErroMaterialPreAnalisadas;
	private long qtdeErroMaterialAguardandoAssinatura;
	
	// jvosantos - 11/07/2019 18:10 - Adicionar pendência de "Verificar Resultado da Votação"
	private long qtdeVerificarResultadoVotacaoNaoAnalisadas;
	private long qtdeVerificarResultadoVotacaoPreAnalisadas;
	private long qtdeVerificarResultadoVotacaoAguardandoAssinatura;
	
	private long qtdSustentacaoOral;
	
	private long qtdeConhecimentoNaoAnalisadas;
	private long qtdeConhecimentoPreAnalisadas;
	
	private long qtdeVerificarImpedimentoNaoAnalisadas;
	private long qtdeVerificarImpedimentoPreAnalisadas;
	
	private long qtdeSessaoVirtualVotoEmentaAguardandoAssinatura; // jvosantos - 03/09/2019 14:08 - Adicionar contador de pendências de voto/ementa aguardando assinatura para casos de pendências com arquivos não assinados
	
	private long qtdeArquivadosSemMovito;
	private long qtdeInconsistenciaPoloPassivo;
	private long qtdeProcessosSemAssunto;
	private long qtdeProcessosComAssuntoPai;
	private long qtdeProcessosComClassePai;
	
	/**
	 * Central de Mandados
	 */
	private long qtdAfastamentoOficiaisAbertos;
	private long qtdMandadosAbertosReservadosOficial;
	
	/**
	 * Lista de conclusões sessão
	 */
	private List conclusoesSessao;
	private List conclusoesSessaoVirtualNaoIniciada;
	private List tomarConhecimento;
	
	private List conclusoesSessaoVirtual;
	
	private List conclusoesVotoVencido;
	private long QuantidadeEncaminhados=0;
	private long loQuantidadePrescritos=0;
	private String DescricaoPrescritos="";
	private String LinkPrescritos="";
	
	private long loQuantidadePrisaoForaPrazo=0;
	private String DescricaoPrisaoForaPrazo="";
	private String LinkPrisaoForaPrazo="";
	
	private long QuantidadeRecursos;
	private ArrayList conclusoesSessaoVirtualPreAnalise;
	private long isSessaoVirtual;
	private Integer qtdeTotalBoxPresencialSessaoVirtual;
	
	private long qtdeSessaoVirtualAcompanharVotacao; // jvosantos - 04/07/2019 14:54 - Adicionar contador de quantidade de sessões em andamento que o desembargador já votou
	
	private List<ArquivoBancoDt> listaUltimos5ArquivosRecebidoCaixa10Minutos;
	
	private long quantidadeBoletosEmitidosHoje;
	private LogDt logUltimoBoletoEmitidoHoje;
	
	public PaginaInicialDt() {
		limpar();
	}
	
	public void limpar() {
		dadosServentia = new ArrayList();
		//dadosServentiaPrescricao = new ArrayList();
		pendenciasServentia = new ArrayList();
		pendenciasServentiaCargo = new ArrayList();
		pendenciasUsuarioServentia = new ArrayList();
		pendenciasUsuarioServentiaLidasAutomaticamente = new ArrayList();
		pendenciasServentiaTipo = new ArrayList();
		pendenciasServentiaMandadoPrisao = new ArrayList();
		pendenciasIntimacoesAguardandoParecer = new ArrayList();
		pendenciasIntimacoesLeituraAutomaticaAguardandoParecer = new ArrayList();
		pendenciasIntimacoesPublicadasDiarioEletronicoAguardandoParecer = new ArrayList();
		conclusoes = new ArrayList();
		pendenciasAnalise = new ArrayList();
		conclusoesSessao = new ArrayList();
		conclusoesSessaoVirtual = new ArrayList();
		conclusoesSessaoVirtualNaoIniciada = new ArrayList();
		conclusoesSessaoVirtualPreAnalise = new ArrayList();
		conclusoesVotoVencido = new ArrayList();
		setTomarConhecimento(new ArrayList<>());
	}


	/**
	 * Adiciona um novo elemento na lista
	 * @author Ronneesley Moura Teles
	 * @since 03/12/2008 11:12
	 * @param ListaPendenciaDt lista, vo de lista de pendencia
	 * @return boolean
	 */
	public boolean adicionarPendenciasServentia(ListaPendenciaDt lista) {
		return this.pendenciasServentia.add(lista);
	}

	public boolean adicionarPendenciasServentiaCargo(ListaPendenciaDt lista) {
		return this.pendenciasServentiaCargo.add(lista);
	}

	public boolean adicionarPendenciasUsuarioServentia(ListaPendenciaDt lista) {
		return this.pendenciasUsuarioServentia.add(lista);
	}
	
	public boolean adicionarPendenciasUsuarioServentiaLidasAutomaticamente(ListaPendenciaDt lista) {
		return this.pendenciasUsuarioServentiaLidasAutomaticamente.add(lista);
	}

	public boolean adicionarPendenciasServentiaTipo(ListaPendenciaDt lista) {
		return this.pendenciasServentiaTipo.add(lista);
	}

	public boolean adicionarPendenciasServentiaMandadoPrisao(ListaPendenciaDt lista) {
		return this.pendenciasServentiaMandadoPrisao.add(lista);
	}
	
	public boolean adicionarPendenciasIntimacoesAguardandoParecer(ListaPendenciaDt lista) {
		return this.pendenciasIntimacoesAguardandoParecer.add(lista);
	}

	public boolean adicionarPendenciasIntimacoesLeituraAutomaticaAguardandoParecer(ListaPendenciaDt lista) {
		return this.pendenciasIntimacoesLeituraAutomaticaAguardandoParecer.add(lista);
	}
	
	public boolean adicionarPendenciasIntimacoesPublicadasDiarioEletronicoAguardandoParecer(ListaPendenciaDt lista) {
		return this.pendenciasIntimacoesPublicadasDiarioEletronicoAguardandoParecer.add(lista);
	}

	public List getPendenciasServentia() {
		return pendenciasServentia;
	}

	public void setPendenciasServentia(List pendenciasServentia) {
		this.pendenciasServentia = pendenciasServentia;
	}

	public List getPendenciasServentiaCargo() {
		return pendenciasServentiaCargo;
	}

	public void setPendenciasServentiaCargo(List pendenciasServentiaCargo) {
		this.pendenciasServentiaCargo = pendenciasServentiaCargo;
	}

	public List getPendenciasUsuarioServentia() {
		return pendenciasUsuarioServentia;
	}

	public void setPendenciasUsuarioServentia(List pendenciasUsuarioServentia) {
		this.pendenciasUsuarioServentia = pendenciasUsuarioServentia;
	}
	
	public List getPendenciasUsuarioServentiaLidasAutomaticamente() {
		return pendenciasUsuarioServentiaLidasAutomaticamente;
	}

	public void setPendenciasUsuarioServentiaLidasAutomaticamente(List pendenciasUsuarioServentiaLidasAutomaticamente) {
		this.pendenciasUsuarioServentiaLidasAutomaticamente = pendenciasUsuarioServentiaLidasAutomaticamente;
	}

	public List getPendenciasServentiaTipo() {
		return pendenciasServentiaTipo;
	}

	public void setPendenciasServentiaTipo(List pendenciasServentiaTipo) {
		this.pendenciasServentiaTipo = pendenciasServentiaTipo;
	}
	
	public List getPendenciasServentiaMandadoPrisao() {
		return pendenciasServentiaMandadoPrisao;
	}

	public void setPendenciasServentiaMandadoPrisao(List pendenciasServentiaMandadoPrisao) {
		this.pendenciasServentiaMandadoPrisao = pendenciasServentiaMandadoPrisao;
	}

	public List getPendenciasIntimacoesAguardandoParecer() {
		return pendenciasIntimacoesAguardandoParecer;
	}

	public void setPendenciasIntimacoesAguardandoParecer(List pendenciasIntimacoesAguardandoParecer) {
		this.pendenciasIntimacoesAguardandoParecer = pendenciasIntimacoesAguardandoParecer;
	}

	public List getPendenciasIntimacoesLeituraAutomaticaAguardandoParecer() {
		return pendenciasIntimacoesLeituraAutomaticaAguardandoParecer;
	}

	public void setPendenciasIntimacoesLeituraAutomaticaAguardandoParecer(List pendenciasIntimacoesLeituraAutomaticaAguardandoParecer) {
		this.pendenciasIntimacoesLeituraAutomaticaAguardandoParecer = pendenciasIntimacoesLeituraAutomaticaAguardandoParecer;
	}
	
	public List getPendenciasIntimacoesPublicadasDiarioEletronicoAguardandoParecer() {
		return pendenciasIntimacoesPublicadasDiarioEletronicoAguardandoParecer;
	}

	public void setPendenciasIntimacoesPublicadasDiarioEletronicoAguardandoParecer(List pendenciasIntimacoesPublicadasDiarioEletronicoAguardandoParecer) {
		this.pendenciasIntimacoesPublicadasDiarioEletronicoAguardandoParecer = pendenciasIntimacoesPublicadasDiarioEletronicoAguardandoParecer;
	}

	public List getConclusoes() {
		return conclusoes;
	}

	public void setConclusoes(List conclusoes) {
		this.conclusoes = conclusoes;
	}

	public boolean adicionarConclusao(ListaConclusaoDt lista) {
		return this.conclusoes.add(lista);
	}

	public List getPendenciasAnalise() {
		return pendenciasAnalise;
	}

	public void setPendenciasAnalise(List pendencias) {
		this.pendenciasAnalise = pendencias;
	}

	public boolean adicionarPendenciaAnalise(ListaPendenciaDt lista) {
		return this.pendenciasAnalise.add(lista);
	}

	public long getQtdePreAnalisesMultiplasConclusoes() {
		return qtdePreAnalisesMultiplasConclusoes;
	}

	public void setQtdePreAnalisesMultiplasConclusoes(int qtdePreAnalisesMultiplas) {
		this.qtdePreAnalisesMultiplasConclusoes = qtdePreAnalisesMultiplas;
	}

	public long getQtdePrazoDecorrido() {
		return qtdePrazoDecorrido;
	}

	public void setQtdePrazoDecorrido(long qtdePrazoDecorrido) {
		this.qtdePrazoDecorrido = qtdePrazoDecorrido;
	}
	
	public long getQtdePrazoADecorrer() {
		return qtdePrazoADecorrer;
	}

	public void setQtdePrazoADecorrer(long qtdePrazoADecorrer) {
		this.qtdePrazoADecorrer = qtdePrazoADecorrer;
	}
	
	public long getQtdePrazoDecorridoDevolucaoAutos() {
		return qtdePrazoDecorridoDevolucaoAutos;
	}

	public void setQtdePrazoDecorridoDevolucaoAutos(long qtdePrazoDecorridoDevolucaoAutos) {
		this.qtdePrazoDecorridoDevolucaoAutos = qtdePrazoDecorridoDevolucaoAutos;
	}

	public long getQtdePendenciaLiberaAcesso() {
		return qtdePendenciaLiberaAcesso;
	}

	public void setQtdePendenciaLiberaAcesso(long qtdePendenciaLiberaAcesso) {
		this.qtdePendenciaLiberaAcesso = qtdePendenciaLiberaAcesso;
	}

	public long getQtdePendenciaInformativa() {
		return qtdePendenciaInformativa;
	}

	public void setQtdePendenciaInformativa(long qtdePendenciaInformativa) {
		this.qtdePendenciaInformativa = qtdePendenciaInformativa;
	}

	public long getQtdExpedidasAguardandoVisto() {
		return qtdExpedidasAguardandoVisto;
	}

	public void setQtdExpedidasAguardandoVisto(long qtdExpedidasAguardandoVisto) {
		this.qtdExpedidasAguardandoVisto = qtdExpedidasAguardandoVisto;
	}

	public List getDadosServentia() {
		return dadosServentia;
	}

	public void setDadosServentia(List dadosServentia) {
		this.dadosServentia = dadosServentia;
	}

	public boolean adicionarDadosServentia(ListaDadosServentiaDt lista) {
		return this.dadosServentia.add(lista);
	}
	
//	public List getDadosServentiaPrescricao() {
//		return dadosServentiaPrescricao;
//	}
//
//	public void setDadosServentiaPrescricao(List dadosServentiaPrescricao) {
//		this.dadosServentiaPrescricao = dadosServentiaPrescricao;
//	}
//
//	public boolean adicionarDadosServentiaPrescricao(ListaDadosServentiaDt lista) {
//		return this.dadosServentiaPrescricao.add(lista);
//	}

	public long getQtdPendenciasEmAndamento() {
		return qtdPendenciasEmAndamento;
	}

	public void setQtdPendenciasEmAndamento(long qtdPendenciasEmAndamento) {
		this.qtdPendenciasEmAndamento = qtdPendenciasEmAndamento;
	}

	public long getQtdePreAnalisesMultiplasPendencias() {
		return qtdePreAnalisesMultiplasPendencias;
	}

	public void setQtdePreAnalisesMultiplasPendencias(long qtdePreAnalisesMultiplasPendencias) {
		this.qtdePreAnalisesMultiplasPendencias = qtdePreAnalisesMultiplasPendencias;
	}
	
	public long getQtdeSessoesAguardandoAcordaoPreAnalisadas() {
		return qtdeSessoesAguardandoAcordaoPreAnalisadas;		
	}

	public void setQtdeSessoesAguardandoAcordaoPreAnalisadas(long qtdeSessoesAguardandoAcordaoPreAnalisadas) {
		this.qtdeSessoesAguardandoAcordaoPreAnalisadas = qtdeSessoesAguardandoAcordaoPreAnalisadas;
	}
	
	public long getQtdeSessoesAguardandoAcordaoNaoAnalisadas() {
		return qtdeSessoesAguardandoAcordaoNaoAnalisadas;		
	}

	public void setQtdeSessoesAguardandoAcordaoNaoAnalisadas(long qtdeSessoesAguardandoAcordaoNaoAnalisadas) {
		this.qtdeSessoesAguardandoAcordaoNaoAnalisadas = qtdeSessoesAguardandoAcordaoNaoAnalisadas;
	}
	
	public long getQtdeSessoesAguardandoAcordaoAguardandoAssinatura() {
		return qtdeSessoesAguardandoAcordaoAguardandoAssinatura;		
	}

	public void setQtdeSessoesAguardandoAcordaoAguardandoAssinatura(long qtdeSessoesAguardandoAcordaoAguardandoAssinatura) {
		this.qtdeSessoesAguardandoAcordaoAguardandoAssinatura = qtdeSessoesAguardandoAcordaoAguardandoAssinatura;
	}
	
	// Virtual
	
	public long getQtdeSessoesAguardandoAcordaoPreAnalisadasVirtual() {
		return qtdeSessoesAguardandoAcordaoPreAnalisadasVirtual;		
	}

	public void setQtdeSessoesAguardandoAcordaoPreAnalisadasVirtual(long qtdeSessoesAguardandoAcordaoPreAnalisadas) {
		this.qtdeSessoesAguardandoAcordaoPreAnalisadasVirtual = qtdeSessoesAguardandoAcordaoPreAnalisadas;
	}
	
	public long getQtdeSessoesAguardandoAcordaoNaoAnalisadasVirtual() {
		return qtdeSessoesAguardandoAcordaoNaoAnalisadasVirtual;		
	}

	public void setQtdeSessoesAguardandoAcordaoNaoAnalisadasVirtual(long qtdeSessoesAguardandoAcordaoNaoAnalisadas) {
		this.qtdeSessoesAguardandoAcordaoNaoAnalisadasVirtual = qtdeSessoesAguardandoAcordaoNaoAnalisadas;
	}
	
	public long getQtdeSessoesAguardandoAcordaoAguardandoAssinaturaVirtual() {
		return qtdeSessoesAguardandoAcordaoAguardandoAssinaturaVirtual;		
	}

	public void setQtdeSessoesAguardandoAcordaoAguardandoAssinaturaVirtual(long qtdeSessoesAguardandoAcordaoAguardandoAssinatura) {
		this.qtdeSessoesAguardandoAcordaoAguardandoAssinaturaVirtual = qtdeSessoesAguardandoAcordaoAguardandoAssinatura;
	}
	
	// FIM Virtual
	
	public long getQtdAfastamentoOficiaisAbertos() {
		return qtdAfastamentoOficiaisAbertos;
	}

	public void setQtdAfastamentoOficiaisAbertos(long qtdAfastamentoOficiaisAbertos) {
		this.qtdAfastamentoOficiaisAbertos = qtdAfastamentoOficiaisAbertos;
	}

	public long getQtdMandadosAbertosReservadosOficial() {
		return qtdMandadosAbertosReservadosOficial;
	}

	public void setQtdMandadosAbertosReservadosOficial(long qtdMandadosAbertosReservadosOficial) {
		this.qtdMandadosAbertosReservadosOficial = qtdMandadosAbertosReservadosOficial;
	}

	public List getConclusoesSessao() {
		return conclusoesSessao;
	}

	public void setConclusoesSessao(List conclusoesSessao) {
		this.conclusoesSessao = conclusoesSessao;
	}

	public boolean adicionarConclusaoSessao(ListaConclusaoDt lista) {
		return this.conclusoesSessao.add(lista);
	}	
	
	public List getConclusoesSessaoVirtualNaoIniciada() {
		return conclusoesSessaoVirtualNaoIniciada;
	}

	public void setConclusoesSessaoVirtualNaoIniciada(List conclusoesSessaoVirtualNaoIniciada) {
		this.conclusoesSessaoVirtualNaoIniciada = conclusoesSessaoVirtualNaoIniciada;
	}

	public boolean adicionarConclusaoSessaoVirtualNaoIniciada(ListaConclusaoDt lista) {
		return this.conclusoesSessaoVirtualNaoIniciada.add(lista);
	}	
	
	public boolean adicionarConclusaoSessaoVirtual(ListaConclusaoDt lista) {
		return this.conclusoesSessaoVirtual.add(lista);
	}
	
	public List getConclusoesSessaoVirtual() {
		return conclusoesSessaoVirtual;
	}
	
	
	public List getConclusoesVotoVencido() {
		return conclusoesVotoVencido;
	}

	public void setConclusoesVotoVencido(List conclusoesVotoVencido) {
		this.conclusoesVotoVencido = conclusoesVotoVencido;
	}

	public boolean adicionarConclusaoVotoVencido(ListaConclusaoDt lista) {
		return this.conclusoesVotoVencido.add(lista);
	}
	
	public String getId() {
		return null;
	}
	
	public void setId(String id) {
	}

	public void setQtdEncaminhados(long loQuantidadeEncaminhados) {
		QuantidadeEncaminhados = loQuantidadeEncaminhados;		
	}
	public long getQtdEncaminhados(){
		return QuantidadeEncaminhados;
	}

	public void setQuantidadePrescritos(long loQuantidade) {
		loQuantidadePrescritos = loQuantidade;		
	}
	public long getQuantidadePrescritos( ) {
		return loQuantidadePrescritos;		
	}

	public void setDescricaoPrescritos(String valor) {
		DescricaoPrescritos = valor;		
	}
	public String getDescricaoPrescritos(){
		return DescricaoPrescritos;
	}

	public void setLinkPrescritos(String valor) {
		LinkPrescritos = valor;		
	}
	
	public String getLinkPrescritos() {
		return LinkPrescritos;		
	}
	
	public void setQuantidadePrisaoForaPrazo(long loQuantidade) {
		loQuantidadePrisaoForaPrazo = loQuantidade;		
	}
	public long getQuantidadePrisaoForaPrazo( ) {
		return loQuantidadePrisaoForaPrazo;		
	}

	public void setDescricaoPrisaoForaPrazo(String valor) {
		DescricaoPrisaoForaPrazo = valor;		
	}
	public String getDescricaoPrisaoForaPrazo(){
		return DescricaoPrisaoForaPrazo;
	}

	public void setLinkPrisaoForaPrazo(String valor) {
		LinkPrisaoForaPrazo = valor;		
	}
	
	public String getLinkPrisaoForaPrazo() {
		return LinkPrisaoForaPrazo;		
	}

	public void setQtdRecursos(long loQuantidadeRecursos) {
		QuantidadeRecursos = loQuantidadeRecursos;
	}
	public long getQtdRecursos() {
		return QuantidadeRecursos;
	}
	
	public void adicionarPendenciasSessaoVirtualPreAnalise(ListaConclusaoDt pendenciasVotoSessao) {
		this.conclusoesSessaoVirtualPreAnalise.add(pendenciasVotoSessao);
	}
	
	public List getPendenciasSessaoVritualPreAnalise() {
		return conclusoesSessaoVirtualPreAnalise;
	}

	public long getQtdeSessaoVirtualEmVotacao() {
		return qtdeSessaoVirtualEmVotacao;
	}

	public void setQtdeSessaoVirtualEmVotacao(long qtdeSessaoVirtualEmVotacao) {
		this.qtdeSessaoVirtualEmVotacao = qtdeSessaoVirtualEmVotacao;
	}

	public long getQtdeVotoNaoAnalisadas() {
		return qtdeVotoNaoAnalisadas;
	}

	public void setQtdeVotoNaoAnalisadas(long qtdeSessoesAguardandoVotoNaoAnalisadas) {
		this.qtdeVotoNaoAnalisadas = qtdeSessoesAguardandoVotoNaoAnalisadas;
	}

	public long getQtdeVotoPreAnalisadas() {
		return qtdeVotoPreAnalisadas;
	}

	public void setQtdeVotoPreAnalisadas(long qtdeSessoesAguardandoVotoPreAnalisadas) {
		this.qtdeVotoPreAnalisadas = qtdeSessoesAguardandoVotoPreAnalisadas;
	}

	public long getQtdeVotoAguardandoAssinatura() {
		return qtdeVotoAguardandoAssinatura;
	}

	public void setQtdeVotoAguardandoAssinatura(long qtdeSessoesAguardandoVotoAguardandoAssinatura) {
		this.qtdeVotoAguardandoAssinatura = qtdeSessoesAguardandoVotoAguardandoAssinatura;
	}

	public long getQtdeFinalizarVotoNaoAnalisadas() {
		return qtdeFinalizarVotoNaoAnalisadas;
	}

	public void setQtdeFinalizarVotoNaoAnalisadas(long qtdeFinalizarVotoNaoAnalisadas) {
		this.qtdeFinalizarVotoNaoAnalisadas = qtdeFinalizarVotoNaoAnalisadas;
	}

	public long getQtdeFinalizarVotoPreAnalisadas() {
		return qtdeFinalizarVotoPreAnalisadas;
	}

	public void setQtdeFinalizarVotoPreAnalisadas(long qtdeFinalizarVotoPreAnalisadas) {
		this.qtdeFinalizarVotoPreAnalisadas = qtdeFinalizarVotoPreAnalisadas;
	}

	public long getQtdeFinalizarVotoAguardandoAssinatura() {
		return qtdeFinalizarVotoAguardandoAssinatura;
	}

	public void setQtdeFinalizarVotoAguardandoAssinatura(long qtdeFinalizarVotoAguardandoAssinatura) {
		this.qtdeFinalizarVotoAguardandoAssinatura = qtdeFinalizarVotoAguardandoAssinatura;
	}

	public long getQtdeConhecimentoNaoAnalisadas() {
		return qtdeConhecimentoNaoAnalisadas;
	}

	public void setQtdeConhecimentoNaoAnalisadas(long qtdeConhecimentoNaoAnalisadas) {
		this.qtdeConhecimentoNaoAnalisadas = qtdeConhecimentoNaoAnalisadas;
	}

	public long getQtdeConhecimentoPreAnalisadas() {
		return qtdeConhecimentoPreAnalisadas;
	}

	public void setQtdeConhecimentoPreAnalisadas(long qtdeConhecimentoPreAnalisadas) {
		this.qtdeConhecimentoPreAnalisadas = qtdeConhecimentoPreAnalisadas;
	}

	public long getIsSessaoVirtual() {
		return isSessaoVirtual;
	}

	public void setIsSessaoVirtual(long isSessaoVirtual) {
		this.isSessaoVirtual = isSessaoVirtual;
	}

	public long getQtdSustentacaoOral() {
		return qtdSustentacaoOral;
	}

	public void setQtdSustentacaoOral(long qtdSustentacaoOral) {
		this.qtdSustentacaoOral = qtdSustentacaoOral;
	}

	public List getTomarConhecimento() {
		return tomarConhecimento;
	}

	public void setTomarConhecimento(List tomarConhecimento) {
		this.tomarConhecimento = tomarConhecimento;
	}

	public List getPendenciasVerificarImpedimento() {
		return pendenciasVerificarImpedimento;
	}

	public void setPendenciasVerificarImpedimento(List pendenciasVerificarImpedimento) {
		this.pendenciasVerificarImpedimento = pendenciasVerificarImpedimento;
	}

	public long getQtdeVerificarImpedimentoNaoAnalisadas() {
		return qtdeVerificarImpedimentoNaoAnalisadas;
	}

	public void setQtdeVerificarImpedimentoNaoAnalisadas(long qtdeVerificarImpedimentoNaoAnalisadas) {
		this.qtdeVerificarImpedimentoNaoAnalisadas = qtdeVerificarImpedimentoNaoAnalisadas;
	}

	public long getQtdeVerificarImpedimentoPreAnalisadas() {
		return qtdeVerificarImpedimentoPreAnalisadas;
	}

	public void setQtdeVerificarImpedimentoPreAnalisadas(long qtdeVerificarImpedimentoPreAnalisadas) {
		this.qtdeVerificarImpedimentoPreAnalisadas = qtdeVerificarImpedimentoPreAnalisadas;
	}

	public boolean adicionarPendenciasApreciados(ListaPendenciaDt listaPendenciaDt) {
		return this.pendenciasApreciados.add(listaPendenciaDt);
	}

	public long getQtdeVotoRenovarNaoAnalisadas() {
		return qtdeVotoRenovarNaoAnalisadas;
	}

	public void setQtdeVotoRenovarNaoAnalisadas(long qtdeVotoRenovarNaoAnalisadas) {
		this.qtdeVotoRenovarNaoAnalisadas = qtdeVotoRenovarNaoAnalisadas;
	}

	public long getQtdeVotoRenovarPreAnalisadas() {
		return qtdeVotoRenovarPreAnalisadas;
	}

	public void setQtdeVotoRenovarPreAnalisadas(long qtdeVotoRenovarPreAnalisadas) {
		this.qtdeVotoRenovarPreAnalisadas = qtdeVotoRenovarPreAnalisadas;
	}

	public long getQtdeVotoRenovarAguardandoAssinatura() {
		return qtdeVotoRenovarAguardandoAssinatura;
	}

	public void setQtdeVotoRenovarAguardandoAssinatura(long qtdeVotoRenovarAguardandoAssinatura) {
		this.qtdeVotoRenovarAguardandoAssinatura = qtdeVotoRenovarAguardandoAssinatura;
	}

	// jvosantos - 04/07/2019 14:54 - Adicionar contador de quantidade de sessões em andamento que o desembargador já votou
	public long getQtdeSessaoVirtualAcompanharVotacao() {
		return qtdeSessaoVirtualAcompanharVotacao;
	}

	// jvosantos - 04/07/2019 14:54 - Adicionar contador de quantidade de sessões em andamento que o desembargador já votou
	public void setQtdeSessaoVirtualAcompanharVotacao(long qtdeSessaoVirtualAcompanharVotacao) {
		this.qtdeSessaoVirtualAcompanharVotacao = qtdeSessaoVirtualAcompanharVotacao;
	}

	// jvosantos - 11/07/2019 18:10 - Adicionar pendência de "Verificar Resultado da Votação"
	public long getQtdeVerificarResultadoVotacaoNaoAnalisadas() {
		return qtdeVerificarResultadoVotacaoNaoAnalisadas;
	}

	// jvosantos - 11/07/2019 18:10 - Adicionar pendência de "Verificar Resultado da Votação"
	public void setQtdeVerificarResultadoVotacaoNaoAnalisadas(long qtdeVerificarResultadoVotacaoNaoAnalisadas) {
		this.qtdeVerificarResultadoVotacaoNaoAnalisadas = qtdeVerificarResultadoVotacaoNaoAnalisadas;
	}

	// jvosantos - 11/07/2019 18:10 - Adicionar pendência de "Verificar Resultado da Votação"
	public long getQtdeVerificarResultadoVotacaoPreAnalisadas() {
		return qtdeVerificarResultadoVotacaoPreAnalisadas;
	}

	// jvosantos - 11/07/2019 18:10 - Adicionar pendência de "Verificar Resultado da Votação"
	public void setQtdeVerificarResultadoVotacaoPreAnalisadas(long qtdeVerificarResultadoVotacaoPreAnalisadas) {
		this.qtdeVerificarResultadoVotacaoPreAnalisadas = qtdeVerificarResultadoVotacaoPreAnalisadas;
	}

	// jvosantos - 11/07/2019 18:10 - Adicionar pendência de "Verificar Resultado da Votação"
	public long getQtdeVerificarResultadoVotacaoAguardandoAssinatura() {
		return qtdeVerificarResultadoVotacaoAguardandoAssinatura;
	}

	// jvosantos - 11/07/2019 18:10 - Adicionar pendência de "Verificar Resultado da Votação"
	public void setQtdeVerificarResultadoVotacaoAguardandoAssinatura(
			long qtdeVerificarResultadoVotacaoAguardandoAssinatura) {
		this.qtdeVerificarResultadoVotacaoAguardandoAssinatura = qtdeVerificarResultadoVotacaoAguardandoAssinatura;
	}

	// jvosantos - 03/09/2019 14:08 - Adicionar contador de pendências de voto/ementa aguardando assinatura para casos de pendências com arquivos não assinados
	public long getQtdeSessaoVirtualVotoEmentaAguardandoAssinatura() {
		return qtdeSessaoVirtualVotoEmentaAguardandoAssinatura;
	}
	
	// jvosantos - 03/09/2019 14:08 - Adicionar contador de pendências de voto/ementa aguardando assinatura para casos de pendências com arquivos não assinados
	public void setQtdeSessaoVirtualVotoEmentaAguardandoAssinatura(
			long qtdeSessaoVirtualVotoEmentaAguardandoAssinatura) {
		this.qtdeSessaoVirtualVotoEmentaAguardandoAssinatura = qtdeSessaoVirtualVotoEmentaAguardandoAssinatura;
	}

	public Integer getQtdeTotalBoxPresencialSessaoVirtual() {
		return qtdeTotalBoxPresencialSessaoVirtual;
	}

	public void setQtdeTotalBoxPresencialSessaoVirtual(Integer qtdeTotalBoxPresencialSessaoVirtual) {
		this.qtdeTotalBoxPresencialSessaoVirtual = qtdeTotalBoxPresencialSessaoVirtual;
	}

	public int getQtdConclusoesGabinete() {
		return qtdConclusoesGabinete;
	}

	public void setQtdConclusoesGabinete(int qtdConclusoesGabinete) {
		this.qtdConclusoesGabinete = qtdConclusoesGabinete;
	}

	public void setListaUltimos5ArquivosRecebidoCaixa10Minutos(List<ArquivoBancoDt> listaUltimos5ArquivosRecebidoCaixa10Minutos) {
		this.listaUltimos5ArquivosRecebidoCaixa10Minutos = listaUltimos5ArquivosRecebidoCaixa10Minutos;
	}

	public List<ArquivoBancoDt> getListaUltimos5ArquivosRecebidoCaixa10Minutos() {
		return listaUltimos5ArquivosRecebidoCaixa10Minutos;
	}
	
	public long getQuantidadeBoletosEmitidosHoje() {
		return quantidadeBoletosEmitidosHoje;
	}

	public void setQuantidadeBoletosEmitidosHoje(long quantidadeBoletosEmitidosHoje) {
		this.quantidadeBoletosEmitidosHoje = quantidadeBoletosEmitidosHoje;
	}
	
	public void setLogUltimoBoletoEmitidoHoje(LogDt logUltimoBoletoEmitidoHoje) {
		this.logUltimoBoletoEmitidoHoje = logUltimoBoletoEmitidoHoje;
	}
	
	public LogDt getLogUltimoBoletoEmitidoHoje() {
		return this.logUltimoBoletoEmitidoHoje;
	}

	public long getQtdeVotosPreAnalisadosEmLote() {
		return qtdeVotosPreAnalisadosEmLote;
	}
	
	public void setQtdeVotosPreAnalisadosEmLote(long qtdeVotosPreAnalisadosEmLote) {
		this.qtdeVotosPreAnalisadosEmLote = qtdeVotosPreAnalisadosEmLote;
	}

	// jvosantos - 08/01/2020 16:54 - Adicionar pendência de "Erro Material"
	public long getQtdeErroMaterialAguardandoAssinatura() {
		return qtdeErroMaterialAguardandoAssinatura;
	}

	// jvosantos - 08/01/2020 16:54 - Adicionar pendência de "Erro Material"
	public void setQtdeErroMaterialAguardandoAssinatura(long qtdeErroMaterialAguardandoAssinatura) {
		this.qtdeErroMaterialAguardandoAssinatura = qtdeErroMaterialAguardandoAssinatura;
	}

	// jvosantos - 08/01/2020 16:54 - Adicionar pendência de "Erro Material"
	public long getQtdeErroMaterialNaoAnalisadas() {
		return qtdeErroMaterialNaoAnalisadas;
	}

	// jvosantos - 08/01/2020 16:54 - Adicionar pendência de "Erro Material"
	public void setQtdeErroMaterialNaoAnalisadas(long qtdeErroMaterialNaoAnalisadas) {
		this.qtdeErroMaterialNaoAnalisadas = qtdeErroMaterialNaoAnalisadas;
	}

	// jvosantos - 08/01/2020 16:54 - Adicionar pendência de "Erro Material"
	public long getQtdeErroMaterialPreAnalisadas() {
		return qtdeErroMaterialPreAnalisadas;
	}

	// jvosantos - 08/01/2020 16:54 - Adicionar pendência de "Erro Material"
	public void setQtdeErroMaterialPreAnalisadas(long qtdeErroMaterialPreAnalisadas) {
		this.qtdeErroMaterialPreAnalisadas = qtdeErroMaterialPreAnalisadas;
	}

	public void setQtdeArquivadosSemMovito(long qtdeArquivadosSemMovito) {
		this.qtdeArquivadosSemMovito = qtdeArquivadosSemMovito;
	}
	
	public long getQtdeArquivadosSemMovito() {
		return qtdeArquivadosSemMovito;
	}
	
	public void setQtdeInconsistenciaPoloPassivo(long qtdeInconsistenciaPoloPassivo) {
		this.qtdeInconsistenciaPoloPassivo = qtdeInconsistenciaPoloPassivo;
	}
	
	public long getQtdeInconsistenciaPoloPassivo() {
		return qtdeInconsistenciaPoloPassivo;
	}
	
	public void setQtdeProcessosSemAssunto(long qtdeProcessosSemAssunto) {
		this.qtdeProcessosSemAssunto = qtdeProcessosSemAssunto;
	}
	
	public long getQtdeProcessosSemAssunto() {
		return qtdeProcessosSemAssunto;
	}
	
	public void setQtdeProcessosComAssuntoPai(long qtdeProcessosComAssuntoPai) {
		this.qtdeProcessosComAssuntoPai = qtdeProcessosComAssuntoPai;
	}
	
	public long getQtdeProcessosComAssuntoPai() {
		return qtdeProcessosComAssuntoPai;
	}
	
	public void setQtdeProcessosComClassePai(long qtdeProcessosComClassePai) {
		this.qtdeProcessosComClassePai = qtdeProcessosComClassePai;
	}
	
	public long getQtdeProcessosComClassePai() {
		return qtdeProcessosComClassePai;
	}
}
