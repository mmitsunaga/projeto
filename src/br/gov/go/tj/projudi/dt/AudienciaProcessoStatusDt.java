package br.gov.go.tj.projudi.dt;

public class AudienciaProcessoStatusDt extends AudienciaProcessoStatusDtGen {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8305839006174938229L;
	// CÓDIGO DE SEGURANÇA DA CLASSE
	public static final int CodigoPermissao = 223;
	
	// Constantes relacionadas ao status da audiência do processo.
	public static final int NENHUM = 0;
	public static final int A_SER_REALIZADA = 1;
	public static final int ANTECIPADA = 2;
	
	public static final int NAO_CONHECIDO = 20;
	
	public static final int CONHECIDO_PROVIDO_MONOCRATICO = 26;				// CNJ 972
	
	public static final int CONHECIDO_PROVIDO = 12;							// CNJ 237
	public static final int CONHECIDO_NAO_PROVIDO = 9;
	public static final int CONHECIDO_PROVIDO_EM_PARTE = 13;
	
	public static final int CONHECIDO_EM_PARTE_PROVIDO = 3;
	public static final int CONHECIDO_EM_PARTE_NAO_PROVIDO = 4;
	public static final int CONHECIDO_EM_PARTE_PROVIDO_EM_PARTE = 5;
	
	public static final int SEGURANCA_CONCEDIDA = 21;						// CNJ 442
	public static final int SEGURANCA_DENEGADA = 22;						// CNJ 446
	public static final int SEGURANCA_CONCEDIDA_EM_PARTE = 23;				// CNJ 450
	
	public static final int EMBARGOS_ACOLHIDOS = 24;						// CNJ 198
	public static final int EMBARGOS_ACOLHIDOS_EM_PARTE = 32;				// CNJ 871
	public static final int EMBARGOS_NAO_ACOLHIDOS = 25;					// CNJ 200
	public static final int RECURSO_PREJUDICADO = 27;						// CNJ 230
	public static final int PETICAO_INDEFERIDA = 28;						// CNJ 454
	
	public static final int EXTINTO_AUSENCIA_PRESSUPOSTOS = 29;				// CNJ 459
	public static final int EXTINTO_AUSENCIA_CONDICOES = 30;				// CNJ 461
	public static final int EXTINTO_DESISTENCIA = 31;						// CNJ 463
	
	public static final int HOMOLOGADO_DESISTENCIA = 33;					// CNJ 944
				
	public static final int CONVERTIDA_DILIGENCIA = 6;
	public static final int RETIRAR_PAUTA = 7;
	public static final int DESMARCAR_PAUTA = 52;
	public static final int LIVRE = 8;
	public static final int NAO_REALIZADA = 10;
	public static final int NEGATIVADA = 11;
		
	public static final int REMARCADA = 14;
	public static final int REALIZADA_SEM_SENTENCA = 15;
	public static final int REALIZADA_SENTENCA_COM_MERITO = 16;
	public static final int REALIZADA_SENTENCA_HOMOLOGACAO = 17;
	public static final int REALIZADA_SENTENCA_SEM_MERITO = 18;
		
	/**
	 * MARIELLI (09/04/2010) - O TIPO REALIZADA FOI CRIADO APENAS DEVIDO A IMPORTAÇÃO, POIS AS SESSOES DE SEGUNDO GRAU
	 * FICARÃO COM ESSE STATUS. PORÉM ELE NÃO PODERÁ MAIS SER UTILIZADO.
	 */
	public static final int REALIZADA = 19;
	public static final int REALIZADA_COM_SENTENCA = 46;
	
	public static final int JULGAMENTO_ADIADO = 34;
	public static final int JULGAMENTO_INICIADO = 35;	

	public static final int ARGUICAO_INCONSTITUCIONALIDADE_ACOLHIDA = 36;
	public static final int ARGUICAO_INCONSTITUCIONALIDADE_NAO_ACOLHIDA = 37;
	
	public static final int COMPETENCIA_DECLINADA = 39;
	
	public static final int PROCESSO_SUSPENSO_DEPENDENCIA_JULGAMENTO_OUTRA_CAUSA = 40;
	
	public static final int CAUTELAR_DEFERIDA = 41;	
	public static final int CAUTELAR_INDEFERIDA = 42;	
	public static final int EXTINTO_SEM_RESOLUCAO_DO_MERITO = 43;
	
	public static final int REALIZADA_SEM_ACORDO = 44;
	public static final int REALIZADA_COM_ACORDO = 45;
	
	public static final int NAO_ADMITIDO_IRDR = 48;                          // CNJ 12095
	public static final int ADMITIDO_IRDR = 49;                              // CNJ 12094
	
	public static final int NAO_ADMITIDO_IAC = 50;                           // CNJ 12095
	public static final int ADMITIDO_IAC = 51;                               // CNJ 12094
	
	public static final int CONCESSAO = 53;                                  // CNJ 210
	public static final int NAO_CONCESSAO = 54;                              // CNJ 210
	
	public static final int EXCECAO_IMPEDIMENTO_SUSPEICAO_REJEITADO = 55;    // Exceção de Impedimento ou Suspeição Rejeitado - CNJ 373
	public static final int EXCECAO_IMPEDIMENTO_SUSPEICAO_ACOLHIDO = 56;     // Exceção de Impedimento ou Suspeição Acolhido - CNJ 940
	public static final int JULGAMENTO_ADIADO_SUSTENTACAO_ORAL = 57;
	
	public static final int JUIZO_DE_RETRATACAO_EFETIVADO = 58;              //CNJ 12258
	public static final int JUIZO_DE_RETRATACAO_NAO_EFETIVADO = 59;          //CNJ 12258
	public static final int JUIZO_DE_RETRATACAO_EFETIVADO_PARCIALMENTE = 60; //CNJ 12258
			
	/**
	 * Construtor que limpa as propriedades da classe.
	 * 
	 * @author Keila Sousa Silva
	 * @since 07/08/2009
	 */
	public AudienciaProcessoStatusDt() {
		limpar();
	}

	/**
	 * Construtor onde serão "alimentadas" as seguintes propriedades da classe: id do status da audiência de processo,
	 * código do status da audiência de processo, descrição do status de audiência de processo e log (id do usuário e IP
	 * do computador).
	 * 
	 * @author Keila Sousa Silva
	 * @since 07/08/2009
	 * @param id_AudienciaProcessoStatus
	 * @param audienciaProcessoStatusCodigo
	 * @param audienciaProcessoStatus
	 * @param logDt
	 */
	public AudienciaProcessoStatusDt(String id_AudienciaProcessoStatus, String audienciaProcessoStatusCodigo, String audienciaProcessoStatus, LogDt logDt) {
		// Id do Status da Audiência de Processo
		if (id_AudienciaProcessoStatus != null) {
			setId(id_AudienciaProcessoStatus);
		}
		// Código do Status da Audiência de Processo
		if (audienciaProcessoStatusCodigo != null) {
			setAudienciaProcessoStatusCodigo(audienciaProcessoStatusCodigo);
		}
		// Descrição do Status da Audiência de Processo
		if (audienciaProcessoStatus != null) {
			setAudienciaProcessoStatus(audienciaProcessoStatus);
		}
		// Log (Id do Usuário e IP do Computador)
		if (logDt != null) {
			// Id do Usuário
			setId_UsuarioLog(logDt.getId_UsuarioLog());
			// IP do Computador
			setIpComputadorLog(logDt.getIpComputadorLog());
		}
	}

//Substituição deste enumerador pelas constantes localizadas no início da classe...
//	/**
//	 * O objeto do tipo "enum" através do qual poderá ser obtigo o código ou a descriçao do status da audiência do
//	 * processo.
//	 * 
//	 * @author Keila Sousa Silva
//	 * 
//	 */
//	public enum Codigo {
//		NENHUM(0), A_SER_REALIZADA(1), ANTECIPADA(2), CONHECIMENTO_PARTE_PROVIMENTO(3), CONHECIMENTO_PARTE_NAO_PROVIMENTO(4), 
//		CONHECIMENTO_PARTE_PROVIMENTO_PARTE(5), CONVERTIDA_DILIGENCIA(6), DESMARCADA(7), LIVRE(8), NAO_PROVIMENTO(9), NAO_REALIZADA(10), 
//		NEGATIVADA(11), PROVIMENTO(12), PROVIMENTO_PARTE(13), REMARCADA(14), REALIZADA_SEM_SENTENCA(15), REALIZADA_SENTENCA_COM_MERITO(16), 
//		REALIZADA_SENTENCA_HOMOLOGACAO(17), REALIZADA_SENTENCA_SEM_MERITO(18), REALIZADA(19);
//		
//		/**
//		 * MARIELLI (09/04/2010) - O TIPO REALIZADA FOI CRIADO APENAS DEVIDO A IMPORTAÇÃO, POIS AS SESSOES DE SEGUNDO GRAU
//		 * FICARÃO COM ESSE STATUS. PORÉM ELE NÃO PODERÁ MAIS SER UTILIZADO.
//		 */
//
//		// Código do status da audiência de processo
//		private int audienciaProcessoStatusCodigo;
//
//		/**
//		 * Construtor do objeto do tipo "enum" denominado "Codigo".
//		 * 
//		 * @author Keila Sousa Silva
//		 * @param audienciaProcessoStatusCodigo
//		 */
//		private Codigo(int audienciaProcessoStatusCodigo) {
//			this.audienciaProcessoStatusCodigo = audienciaProcessoStatusCodigo;
//		}
//
//		/**
//		 * Método responsável por retornar o código do status da audiência de processo.
//		 * 
//		 * @author Keila Sousa Silva
//		 * @return this.codigoAudienciaProcessoStatus: código do status da audiência de processo.
//		 */
//		public int getAudienciaProcessoStatusCodigo() {
//			return this.audienciaProcessoStatusCodigo;
//		}
//
//		/**
//		 * Método responsável por obter um objeto do tipo "enum", denominado "Código", através do qual poderá ser obtida
//		 * a descricao do status da audiência de processo.
//		 * 
//		 * @author Keila Sousa Silva
//		 * @param audienciaProcessoStatusCodigo
//		 * @return Codigo: descricao
//		 */
//		public Codigo getAudienciaProcessoStatusCodigo(int audienciaProcessoStatusCodigo) {
//			switch (audienciaProcessoStatusCodigo) {
//				case 0:
//					return Codigo.NENHUM;
//				case 1:
//					return Codigo.A_SER_REALIZADA;
//				case 2:
//					return Codigo.ANTECIPADA;
//				case 3:
//					return Codigo.CONHECIMENTO_PARTE_PROVIMENTO;
//				case 4:
//					return Codigo.CONHECIMENTO_PARTE_NAO_PROVIMENTO;
//				case 5:
//					return Codigo.CONHECIMENTO_PARTE_PROVIMENTO_PARTE;
//				case 6:
//					return Codigo.CONVERTIDA_DILIGENCIA;
//				case 7:
//					return Codigo.DESMARCADA;
//				case 8:
//					return Codigo.LIVRE;
//				case 9:
//					return Codigo.NAO_PROVIMENTO;
//				case 10:
//					return Codigo.NAO_REALIZADA;
//				case 11:
//					return Codigo.NEGATIVADA;
//				case 12:
//					return Codigo.PROVIMENTO;
//				case 13:
//					return Codigo.PROVIMENTO_PARTE;
//				case 14:
//					return Codigo.REMARCADA;
//				case 15:
//					return Codigo.REALIZADA_SEM_SENTENCA;
//				case 16:
//					return Codigo.REALIZADA_SENTENCA_COM_MERITO;
//				case 17:
//					return Codigo.REALIZADA_SENTENCA_HOMOLOGACAO;
//				case 18:
//					return Codigo.REALIZADA_SENTENCA_SEM_MERITO;
//				case 19:
//					return Codigo.REALIZADA;
//			}
//			return null;
//		}
//	}
}