package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.utils.Funcoes;

public class GrupoDt extends GrupoDtGen {

    private static final long serialVersionUID = 3149257552034590801L;

    public static final int CodigoPermissao = 153;

	public static final int AUTORIDADES_POLICIAIS = 7;
		
	public static final int CONSULTORES = 10;
	
	public static final int PARTES = 14;
	
	public static final int JUIZ_EXECUCAO_PENAL = 41;
	public static final int JUIZES_TURMA_RECURSAL = 12;
	public static final int JUIZES_VARA = 13;
	public static final int JUIZ_CORREGEDOR = 24;
	public static final int JUIZ_AUXILIAR_PRESIDENCIA = 26;
	public static final int JUIZ_LEIGO = 108;
	public static final int DESEMBARGADOR = 31;
	public static final int MINISTERIO_PUBLICO = 15;
	public static final int MP_TCE = 106;
	public static final int PRESIDENTE_SEGUNDO_GRAU = 51;
	public static final int PRESIDENTE_TJGO = 100;
	
	public static final int DIVISAO_RECURSOS_CONSTITUCIONAIS = 65;	
	
	public static final int TECNICO_EXECUCAO_PENAL = 37;
	public static final int TECNICOS_JUDICIARIOS_TURMA_RECURSAL = 16;
	public static final int TECNICO_JUDICIARIO_PRIMEIRO_GRAU_CIVEL = 17;
	
	public static final int PRESIDENCIA = 20;

	public static final int ESTATISTICA = 23;
	
	public static final int ANALISTA_TI = 25;
	public static final int GERENTE_TI = 47;
	
	public static final int ADMINISTRADORES = 1;
	public static final int GERENCIAMENTO_TABELAS = 21;
	public static final int ADMINISTRADOR_LOG = 27;
	public static final int CADASTRADORES_TABELA = 28;
	public static final int CADASTRADORES = 8;
	public static final int GERENCIAMENTO_SEGUNDO_GRAU = 67;
	public static final int CADASTRADOR_MASTER = 79;

	public static final int MALOTE_DIGITAL = 30;

	public static final int CENTRAL_MANDADOS = 29;
	
	public static final int DISTRIBUIDOR_PRIMEIRO_GRAU = 22;
	public static final int DISTRIBUIDOR_GABINETE = 32;
	public static final int DISTRIBUIDOR_CRIMINAL = 66;
	public static final int DISTRIBUIDOR_CAMARA = 35;
	public static final int DISTRIBUIDOR_SEGUNDO_GRAU = 59;
	
	public static final int ASSISTENTE_GABINETE = 33;
	public static final int ASSISTENTE_GABINETE_FLUXO=72;
	
	public static final int ASSESSOR_GABINETE_PRESIDENTE = 33;
	
	public static final int OUVIDOR_PRESIDENCIA = 34;
	
	public static final int CONTADORES_VARA = 11;
	
	public static final int CONCILIADORES_VARA = 9;
	public static final int OFICIAL_JUSTICA = 68;
	
	public static final int TECNICO_JUDICIARIO_SEGUNDO_GRAU_CIVEL = 45;
	public static final int TECNICO_JUDICIARIO_SEGUNDO_GRAU_CRIMINAL = 97;
	public static final int TECNICO_JUDICIARIO_PRIMEIRO_GRAU_CRIMINAL = 62;
	
	public static final int CONTADOR_PROCURADORIA_MUNICIPAL = 46;
	
	public static final int ASSESSOR_ADVOGADOS = 19;
	public static final int ASSESSOR_MP = 98;
	public static final int ASSESSOR_JUIZES_SEGUNDO_GRAU = 36;
	public static final int ASSESSOR_PRESIDENTE_SEGUNDO_GRAU = 52;
	public static final int ASSESSOR_DESEMBARGADOR = 55;
	public static final int ASSESSOR = 5;
	public static final int ASSESSOR_JUIZES_VARA = 6;
	
	public static final int ESTAGIARIO_PRIMEIRO_GRAU = 82;	
	public static final int ESTAGIARIO_SEGUNDO_GRAU = 87;
	public static final int ESTAGIARIO_GABINETE = 101;
	
	public static final int ANALISTAS_JUDICIARIOS_TURMA_RECURSAL = 3;
	public static final int ANALISTA_JUDICIARIO_PRIMEIRO_GRAU_CIVEL = 4;
	public static final int ANALISTAS_EXECUCAO_PENAL = 18;
	public static final int ANALISTA_CALCULO_EXECUCAO_PENAL = 38;
	public static final int ANALISTA_FORENSE = 39;
	public static final int ANALISTA_FORENSE_2_GRAU = 89;
	public static final int ANALISTA_JUDICIARIO_PRESIDENCIA = 42;
	public static final int ANALISTA_PROCURADORIA = 43;
	public static final int ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CIVEL = 44;
	public static final int ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CRIMINAL = 96;
	public static final int ANALISTA_CALCULO_PROCESSO_FISICO = 53;
	public static final int ANALISTA_COORDENARODIRA_JUDICIARIA = 54;
	public static final int ANALISTA_FINANCEIRO = 71;
	public static final int ANALISTA_PRE_PROCESSUAL = 78;
	public static final int ANALISTA_JUDICIARIO_PRIMEIRO_GRAU_CRIMINAL = 61;
	public static final int ANALISTAS_CEJUSC = 85;
	
	
	public static final int ADVOGADO_PARTICULAR = 2;
	public static final int ADVOGADO_DEFENSOR_PUBLICO = 70;
	public static final int ADVOGADO_PARTICULAR_ESCRITORIO_JURIDICO = 80;
	public static final int ADVOGADO_PUBLICO = 88;	
	public static final int ADVOGADO_PUBLICO_MUNICIPAL = 75;
	public static final int ADVOGADO_PUBLICO_ESTADUAL = 76;
	public static final int ADVOGADO_PUBLICO_UNIAO = 77;
	
	public static final int COORDENADOR_CENTRAL_MANDADO = 69;
	
	public static final int COORDENADOR_OAB = 63;

	public static final int COORDENADOR_HABILITACAO_MP = 64;
	
	/*
	 * Grupos resposaveis por consultar os processos e redistribuir as 
	 * intimações dentro das respectivas serventias, usam o id do usuário_chefe ou o id da serventia
	 */
	public static final int COORDENADOR_PROMOTORIA = 40;
	public static final int COORDENADOR_PROCURADORIA_ESTADUAL = 48;
	public static final int COORDENADOR_PROCURADORIA_MUNICIPAL = 49;
	public static final int COORDENADOR_PROCURADORIA_FEDERAL = 50;
	public static final int COORDENADOR_ESCRITORIO_JURIDICO = 81;
	public static final int COORDENADOR_DEFENSORIA_PUBLICA = 84;
	public static final int COORDENADOR_ADVOCACIA_PUBLICA = 90;
	
	public static final int PUBLICO = 83;
	
	public static final String ID_GRUPO_CADASTRO_ADVOGADO = "1";
	public static final String ID_GRUPO_PUBLICO = "83";
	
	public static final int JUIZ_INFANCIA_JUVENTUDE_CIVEL = 91;
	public static final int JUIZ_INFANCIA_JUVENTUDE_INFRACIONAL = 92;
	public static final int ANALISTA_JUDICIARIO_INFANCIA_JUVENTUDE_CIVEL = 93;
	public static final int ANALISTA_JUDICIARIO_INFANCIA_JUVENTUDE_INFRACIONAL = 94;
	public static final int CONSULTOR_SISTEMAS_EXTERNOS = 95;
	
	
	public static final int INTELIGENCIA_N0 = 112;			//consultor basico, não vê as abas: Advogados, conta bancaria, bens, imoveis e empresas
	public static final int INTELIGENCIA_N1 = 109;			//faz o cadastro basico exceto de Advogados, conta bancaria, bens, imoveis e empresas
	public static final int INTELIGENCIA_N2 = 110;			//faz o cadastro basico exceto de Advogados, conta bancaria, bens, imoveis e empresas e confirma o cadastro do n1, libera a consulta para o n0
	public static final int INTELIGENCIA_N3 = 113;			//completa o cadastro do n2 (Advogados, conta bancaria, bens, imoveis e empresas)
	public static final int INTELIGENCIA_N4 = 114;			//confirma o cadastro do n3
	public static final int CADASTRADOR_INTELIGENCIA = 111;
	
	public static final int MAGISTRADO_UPJ_PRIMEIRO_GRAU = 116;
	public static final int MAGISTRADO_UPJ_SEGUNDO_GRAU = 120;
	public static final int COORDENADOR_HABILITACAO_SSP = 117;
	
	public static final int ANALISTA_POSTAGEM = 118;
	
	//Perfil responsável pelo controle de obejtos dos processos criminais
	public static final int ANALISTA_ARQUIVAMENTO = 119;
	
	//Grupo População definido apenas para ser utilizado na consulta pública
	public static final int POPULACAO = -100;

	

	/**
	 * Retorna a atividade de um usuário de acordo com o grupo passado
	 * para exibir na página inicial
	 * @param grupoCodigo
	 * @return
	 */
	public static String getAtividadeUsuario(String grupoCodigo) {
		
		if( grupoCodigo == null ) {
			grupoCodigo = "-999";
		}
		
		switch (Funcoes.StringToInt(grupoCodigo)) {
		
			case ADMINISTRADORES:
				return "Administrador do Sistema";
			case JUIZES_VARA:
			case MAGISTRADO_UPJ_PRIMEIRO_GRAU:
			case JUIZ_INFANCIA_JUVENTUDE_CIVEL:
			case JUIZ_INFANCIA_JUVENTUDE_INFRACIONAL:
				return "Juiz de Direito";
			case JUIZES_TURMA_RECURSAL:
				return "Juiz Recursal";
			case DESEMBARGADOR:
				return "Desembargador";
			case MINISTERIO_PUBLICO :
				return "Ministério Público";
			case  MP_TCE:
				return "Ministério Público do TCE";	
			case CONCILIADORES_VARA:
				return "Conciliador";
			case CONTADORES_VARA:
				return "Contador";
			case ASSESSOR:
				return "Assessor";
			case JUIZ_LEIGO:	
				return "Juiz Leigo";
			case ASSESSOR_JUIZES_VARA:
			case ASSESSOR_JUIZES_SEGUNDO_GRAU:
				return "Assessor de Magistrado";
			case ASSISTENTE_GABINETE:
				return "Assistente de Gabinete";
			case ASSESSOR_DESEMBARGADOR:
				return "Assessor de Desembargador";
			case ASSESSOR_ADVOGADOS:
				return "Assessor Advogado";
			case ASSESSOR_MP:
				return "Assistente MP";				
			case ANALISTA_JUDICIARIO_PRIMEIRO_GRAU_CIVEL:
			case ANALISTA_JUDICIARIO_INFANCIA_JUVENTUDE_CIVEL:
			case ANALISTA_JUDICIARIO_INFANCIA_JUVENTUDE_INFRACIONAL:
				return "Analista Judiciário";
			case TECNICO_JUDICIARIO_PRIMEIRO_GRAU_CIVEL:
				return "Técnico Judiciário";
			case AUTORIDADES_POLICIAIS:
				return "Autoridade Policial";
			case CONSULTORES:
			case CONSULTOR_SISTEMAS_EXTERNOS:
				return "Consultor";
			case CADASTRADORES:
				return "Cadastrador";
			case ADVOGADO_PARTICULAR:
			case ADVOGADO_PARTICULAR_ESCRITORIO_JURIDICO:
				return "Advogado";
			case PARTES:
				return "Parte";
			case ANALISTAS_JUDICIARIOS_TURMA_RECURSAL:
				return "Analista Judiciário de 2º Grau";
			case TECNICOS_JUDICIARIOS_TURMA_RECURSAL:
				return "Técnico Judiciário de 2º Grau";
			case PRESIDENTE_TJGO:
				return "Presidente";
			case ESTATISTICA:
				return "Corregedoria";
			case DISTRIBUIDOR_GABINETE:
				return "Distribuidor de Gabinete";
			case CADASTRADORES_TABELA:
				return "Cadastradores de Tabela";
			case MALOTE_DIGITAL:
				return "Analista Banco";
			case COORDENADOR_HABILITACAO_MP:
				return "Habilitador do MP";
			case COORDENADOR_HABILITACAO_SSP:
				return "Habilitador da SSP";
			case COORDENADOR_OAB:
			case COORDENADOR_PROCURADORIA_FEDERAL:
			case COORDENADOR_PROCURADORIA_ESTADUAL:
			case COORDENADOR_PROCURADORIA_MUNICIPAL:
			case COORDENADOR_ESCRITORIO_JURIDICO:
			case COORDENADOR_DEFENSORIA_PUBLICA:
			case COORDENADOR_PROMOTORIA:
			case COORDENADOR_ADVOCACIA_PUBLICA:
				return "Coordenador da Serventia";
			case ESTAGIARIO_GABINETE:
				return "Estagiário de Gabinete";
			case ESTAGIARIO_PRIMEIRO_GRAU:
			case ESTAGIARIO_SEGUNDO_GRAU:
				return "Estagiário";
			default:
				return "Servidor";
				//throw new RuntimeException("Grupo desconhecido " + grupo);
		}
	}

	/**
	 * Retorna o grupo de um usuário de forma resumida, para mostrar nos detalhes da movimentação
	 * @param grupo
	 * @return
	 */
	public static String getGrupo(String grupo) {
		
		if( grupo == null ) {
			grupo = "-999";
		}
		
		switch (Funcoes.StringToInt(grupo)) {
			case ADMINISTRADORES:
				return "";

			case JUIZES_VARA:
			case MAGISTRADO_UPJ_PRIMEIRO_GRAU:
			case JUIZES_TURMA_RECURSAL:
			case JUIZ_INFANCIA_JUVENTUDE_CIVEL:
			case JUIZ_INFANCIA_JUVENTUDE_INFRACIONAL:
				return "Juiz";

			case MINISTERIO_PUBLICO:
				return "Ministério Público";
			case  MP_TCE:
				return "Ministério Público TCE";
			case ANALISTA_JUDICIARIO_PRIMEIRO_GRAU_CIVEL:
			case TECNICO_JUDICIARIO_PRIMEIRO_GRAU_CIVEL:
			case CONCILIADORES_VARA:
			case ANALISTAS_JUDICIARIOS_TURMA_RECURSAL:
			case TECNICOS_JUDICIARIOS_TURMA_RECURSAL:
			case ANALISTA_JUDICIARIO_INFANCIA_JUVENTUDE_CIVEL:
			case ANALISTA_JUDICIARIO_INFANCIA_JUVENTUDE_INFRACIONAL:
				return "Servidor";

			case CONTADORES_VARA:
				return "Contador";

			case AUTORIDADES_POLICIAIS:
				return "Servidor SSP";

			case ADVOGADO_PARTICULAR:
				return "Advogado";

			default:
				return "Servidor";
		}
	}	
	
	public static boolean isJuiz(String grupoCodigo) {
		switch (Funcoes.StringToInt(grupoCodigo)) {
			case JUIZES_VARA:
			case MAGISTRADO_UPJ_PRIMEIRO_GRAU:
			case JUIZES_TURMA_RECURSAL:
			case JUIZ_INFANCIA_JUVENTUDE_CIVEL:
			case JUIZ_INFANCIA_JUVENTUDE_INFRACIONAL:
				return true;
		}
		return false;
	}
	
	public static boolean isUsuarioPrimeiroSegundoGrauConflitoCompetencia(String grupoCodigo) {
		switch (Funcoes.StringToInt(grupoCodigo)) {
			case JUIZES_VARA:
			case MAGISTRADO_UPJ_PRIMEIRO_GRAU:
			case JUIZ_INFANCIA_JUVENTUDE_CIVEL:
			case JUIZ_INFANCIA_JUVENTUDE_INFRACIONAL:
			case ANALISTA_JUDICIARIO_PRIMEIRO_GRAU_CRIMINAL:
			case ANALISTA_JUDICIARIO_PRIMEIRO_GRAU_CIVEL:
			case TECNICO_JUDICIARIO_PRIMEIRO_GRAU_CRIMINAL:
			case TECNICO_JUDICIARIO_PRIMEIRO_GRAU_CIVEL:
			case DESEMBARGADOR:
			case ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CIVEL:
			case ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CRIMINAL:
			case TECNICO_JUDICIARIO_SEGUNDO_GRAU_CIVEL:
			case TECNICO_JUDICIARIO_SEGUNDO_GRAU_CRIMINAL:
				return true;
		}
		return false;
	}

	public static boolean isDistribuidor(String grupoCodigoUsuario) {
		switch (Funcoes.StringToInt(grupoCodigoUsuario)) {
			case DISTRIBUIDOR_PRIMEIRO_GRAU:
			case DISTRIBUIDOR_SEGUNDO_GRAU:
				return true;
		}
		return false;
	}
	
}
