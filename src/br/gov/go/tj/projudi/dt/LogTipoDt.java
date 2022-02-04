package br.gov.go.tj.projudi.dt;

public class LogTipoDt extends LogTipoDtGen {

    private static final long serialVersionUID = 8535841771294482658L;

    public static final int CodigoPermissao = 161;

	public static final int Incluir = 1;
	public static final int Alterar = 2;
	public static final int Excluir = 3;
	public static final int Acesso = 4;

	public static final int InternetUp = 5;
	public static final int InternetDown = 6;

	public static final int Erro = 7;
	public static final int Informacao = 8;
	public static final int Download = 9;
	public static final int LimparSenha = 10;
	public static final int AlterarSenha = 11;

	public static final int DesabilitarParte = 12;
	public static final int RestaurarParte = 13;

	public static final int ExecucaoAutomatica = 14;
	public static final int Inicializacao = 15;

	public static final int Redistribuicao = 16;
	public static final int EnvioRecurso = 17;
	public static final int ConverterProcessoRecurso = 41;
	public static final int ConverterRecursoProcesso = 42;

	public static final int Arquivamento = 18;
	public static final int Desarquivamento = 19;

	public static final int AutuacaoRecurso = 20;
	public static final int RetornoRecurso = 21;

	public static final int SuspensaoProcesso = 22;
	
	public static final int RevogacaoCertificado = 23;
	public static final int LiberacaoCertificado = 36;

	//Utilizado para troca de um usuário que está ocupando um cargo
	public static final int TrocaUsuarioCargo = 24;

	// Utilizado para ativação de Usuario e UsuarioServentiaGrupo
	public static final int HabilitacaoUsuario = 25;
	// Utilizado para desativação de Usuario e UsuarioServentiaGrupo
	public static final int DesabilitacaoUsuario = 26;

	// Utilizado para validação de movimentação e/ou arquivos de movimentação e pendência
	public static final int Validacao = 27;
	// Utilizado para invalidação de movimentação e/ou arquivos de movimentação e pendência
	public static final int Invalidacao = 28;

	//Utilizado para troca de um responsável pelo processo
	public static final int TrocaResponsavelProcesso = 29;

	//Utilizado para troca de um responsável pela pendencia
	public static final int TrocaResponsavelPendencia = 30;

	public static final int MovimentacaoAudiencia = 31;
	public static final int TerminoSuspensaoProcesso = 32;
	public static final int LeituraAutomaticaPendencia = 33;
	public static final int EncaminharPendencia = 34;
	public static final int FinalizarPendencia = 35;
	public static final int DescartarPendencia = 37;
	public static final int FechamentoAutomaticoPendencia = 38;
	public static final int ReaberturaAutomaticaPendencia = 62;
	public static final int EstatisticaGerada = 39;

	public static final int GuiaCancelada = 40;
	
	public static final int ExecucaoSQL = 43;
	
	public static final int IntegracaoPrefeituraGoiania = 44;
	
	public static final int ErroIntegracaoPrefeituraGoiania = 45;
	
	public static final int ExecucaoAutomaticaPrefeituraGoiania = 46;
	
	public static final int RepassePrefeituraGoiania = 49;
	
	public static final int IntegracaoSPGGuiasPagas = 50;
	
	public static final int ExecucaoAutomaticaSPGGuiasPagas = 51;
	
	// Utilizado para alterar a visibilidade de um arquivo
	public static final int AlteracaoVisibilidadeAquivo = 52;
	// Utilizado para alterar a visibilidade de uma movimentacao
	public static final int AlteracaoVisibilidadeMovimentacao = 53;
	
	public static final int DesabilitarResponsavelProcesso = 54;
	
	public static final int HabilitarResponsavelProcesso = 55;
	
	// Serviço de envio de processo sobrestados utilizando o webservice do CNJ
	public static final int EnviarProcessoSobrestadosCNJ = 56;
	
	// Serviço de envio de IRDR utilizando o webservice do CNJ
	public static final int EnviarPrecedentesIRDR = 57;
	
	// Serviço de consultar e gravar no projudi novos precedentes RG e RR do CNJ
	public static final int CadastrarNovosPrecedentesRG = 58;	
	public static final int CadastrarNovosPrecedentesRR = 59;
	
	public static final int AtualizarPrecedentesRG = 60;
	public static final int AtualizarPrecedentesRR = 61;
	
	// Gerenciamento de afastamento de oficiais por mandados expirados
	public static final int OficialAfastado = 87;
	public static final int RetornarOficialAfastado = 88;
	
	public static final int AtivacaoProcessoExecucao = 63;
	
	public static final int CODIGO_VALIDACAO = 64;
	
	public static final int ECarta = 75;	
	
	public static final int BoletoEmitido = 76;
	public static final int BoletoReemitido = 77;
	
	public static final int TextoBancoMandadoGratuito = 65;
	public static final int TextoBancoMandadoComCustas = 66;
	public static final int EnviarDadosDiretoriaFinanceira = 68;	
	public static final int AtualizaGuiaSaldo = 69;	
	public static final int LiberarLocomocao = 89;	
}

