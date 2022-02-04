package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.pe.oab.AdvogadoData;
import br.gov.go.tj.pe.oab.CadastroOABProxy;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.PaginaInicialDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioUltimoLoginDt;
import br.gov.go.tj.projudi.ne.PaginaInicialNe;
import br.gov.go.tj.projudi.ne.ServentiaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.AjudanteArquivosAreaTransferencia;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class UsuarioCt extends UsuarioCtGen {

	private static final long serialVersionUID = 8794090248280491569L;

	public int Permissao(){
		return UsuarioDt.CodigoPermissao;
	}
	
	//-------------------------------------------------------------------------------------------	
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws MensagemException, Exception, ServletException, IOException {

		UsuarioDt Usuariodt;
		UsuarioNe Usuarione;
		RequestDispatcher dis = null;					

		Usuarione = (UsuarioNe) request.getSession().getAttribute("Usuarione");
		if (Usuarione == null) Usuarione = new UsuarioNe();

		Usuariodt = (UsuarioDt) request.getSession().getAttribute("Usuariodt");
		if (Usuariodt == null) Usuariodt = new UsuarioDt();

		Usuariodt.setUsuario(request.getParameter("Usuario"));
		Usuariodt.setId_CtpsUf(request.getParameter("Id_CtpsUf"));
		Usuariodt.setCtpsEstado(request.getParameter("CtpsEstado"));
		Usuariodt.setId_Naturalidade(request.getParameter("Id_Naturalidade"));
		Usuariodt.setNaturalidade(request.getParameter("Naturalidade"));
		Usuariodt.setId_Endereco(request.getParameter("Id_Endereco"));
		Usuariodt.setEndereco(request.getParameter("Endereco"));
		Usuariodt.setId_RgOrgaoExpedidor(request.getParameter("Id_RgOrgaoExpedidor"));
		Usuariodt.setRgOrgaoExpedidor(request.getParameter("RgOrgaoExpedidor"));
		Usuariodt.setId_OabUf(request.getParameter("Id_OabUf"));
		Usuariodt.setOabEstado(request.getParameter("OabEstado"));
		Usuariodt.setSenha(request.getParameter("Senha"));
		Usuariodt.setNome(request.getParameter("Nome"));
		Usuariodt.setSexo(request.getParameter("Sexo"));
		Usuariodt.setDataNascimento(request.getParameter("DataNascimento"));
		Usuariodt.setRg(request.getParameter("Rg"));
		Usuariodt.setRgDataExpedicao(request.getParameter("RgDataExpedicao"));
		Usuariodt.setCpf(request.getParameter("Cpf"));
		Usuariodt.setTituloEleitor(request.getParameter("TituloEleitor"));
		Usuariodt.setTituloEleitorZona(request.getParameter("TituloEleitorZona"));
		Usuariodt.setTituloEleitorSecao(request.getParameter("TituloEleitorSecao"));
		Usuariodt.setCtps(request.getParameter("Ctps"));
		Usuariodt.setCtpsSerie(request.getParameter("CtpsSerie"));
		Usuariodt.setPis(request.getParameter("Pis"));
		Usuariodt.setOabNumero(request.getParameter("OabNumero"));
		Usuariodt.setOabComplemento(request.getParameter("OabComplemento"));
		Usuariodt.setMatriculaTjGo(request.getParameter("MatriculaTjGo"));
		Usuariodt.setNumeroConciliador(request.getParameter("NumeroConciliador"));
		Usuariodt.setDataCadastro(request.getParameter("DataCadastro"));

		if (request.getParameter("Ativo") != null) Usuariodt.setAtivo(request.getParameter("Ativo"));
		else Usuariodt.setAtivo("false");

		Usuariodt.setDataAtivo(request.getParameter("DataAtivo"));
		Usuariodt.setDataExpiracao(request.getParameter("DataExpiracao"));
		Usuariodt.setEMail(request.getParameter("EMail"));
		Usuariodt.setTelefone(request.getParameter("Telefone"));
		Usuariodt.setCelular(request.getParameter("Celular"));
		Usuariodt.setCodigoTemp(request.getParameter("CodigoTemp"));

		Usuariodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Usuariodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
		boolean isExecpenweb = false;
		if (request.getSession().getAttribute("Acesso") != null && request.getSession().getAttribute("Acesso").equals("Execpenweb")) {
			isExecpenweb = true;
		}		
		
		if (paginaatual== Configuracao.Curinga6){
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			Usuarione.limparSenha(Usuariodt);
			request.setAttribute("MensagemOk", "Senha Alterada com sucesso. (senha 12345)");
			//Opção Logon1: Quando usuário seleciona serventia/grupo
		} else if (paginaatual== Configuracao.Curinga7){							
			obterServentiaCargo(request, response, UsuarioSessao, isExecpenweb);
			return;				
		} else if (paginaatual== Configuracao.Curinga9){
			obterServentiaGrupo(request,response, isExecpenweb, UsuarioSessao);
			return;							
		//Redireciona para a página inicial correspondente ao usuario
		} else if (paginaatual == Configuracao.Cancelar){
			// captura da variavel antes da limpeza de sessao
			String mensagem = (String) request.getParameter("Mensagem");
			String mensagemOk = (String) request.getParameter("MensagemOk");
			String mensagemErro = (String) request.getParameter("MensagemErro");
			
			// Obtem a lista de arquivos da area de transferência para armazenar novamente após a limpeza da sessão...
			Map listaSessaoArquivosAreaTransferencia = (Map) AjudanteArquivosAreaTransferencia.getListaArquivosAreaTransferencia(request);
			// Sempre que clicar na página principal vamos limpar os objetos da sessão
			LimparSessao(request.getSession());
			// Atualiza novamente a lista de arquivos na sessão...
			AjudanteArquivosAreaTransferencia.setListaArquivosAreaTransferencia(request, listaSessaoArquivosAreaTransferencia);
			
			if(UsuarioSessao.getUsuarioDt().getCodigoTemp().equalsIgnoreCase("trocarSenha")) {
				redireciona(response, "DadosUsuario?PaginaAtual=" + Configuracao.Curinga6);
				return;
			}
			// quando retorna da tela de alterar senha padrao(12345)
			if(mensagem != null && mensagem.length() > 0)
				request.setAttribute("MensagemOk", "Senha Alterada com Sucesso.");
			
			if(mensagemOk != null && mensagemOk.length() > 0)
				request.setAttribute("MensagemOk", mensagemOk);
			
			if(mensagemErro != null && mensagemErro.length() > 0)
				request.setAttribute("MensagemErro", mensagemErro);
			
			//switch (Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getGrupoCodigo())) {
			switch (UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt()) {
				//case GrupoDt.ADVOGADOS:
				case GrupoTipoDt.ADVOGADO:						
					dis = request.getRequestDispatcher("/WEB-INF/PaginaInicial/PaginaInicialAdvogado.jsp");
					break;

				//case GrupoDt.PROMOTORES:
				case GrupoTipoDt.MP:
					dis = request.getRequestDispatcher("/WEB-INF/PaginaInicial/PaginaInicialPromotor.jsp");
					break;
				case GrupoTipoDt.COORDENADOR_PROMOTORIA:
				case GrupoTipoDt.COORDENADOR_PROCURADORIA:
				case GrupoTipoDt.COORDENADOR_ESCRITORIO_JURIDICO:
				case GrupoTipoDt.COORDENADOR_ADVOCACIA_PUBLICA:
				case GrupoTipoDt.COORDENADOR_DEFENSORIA_PUBLICA:
					dis = request.getRequestDispatcher("/WEB-INF/PaginaInicial/PaginaInicialCoordenador.jsp");
					break;
//					dis = request.getRequestDispatcher("/WEB-INF/PaginaInicial/PaginaInicialCoordenadorDefensoria.jsp");
//					break;
				//case GrupoDt.JUIZES_VARA:
				//case GrupoDt.JUIZES_TURMA_RECURSAL:
				//case GrupoDt.DESEMBARGADOR:
				case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
					
					dis = request.getRequestDispatcher("/WEB-INF/PaginaInicial/PaginaInicialDesembargador.jsp");
					break;
					
				case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
				case GrupoTipoDt.JUIZ_TURMA:
				case GrupoTipoDt.PRESIDENTE_SEGUNDO_GRAU:
				    
					dis = request.getRequestDispatcher("/WEB-INF/PaginaInicial/PaginaInicialJuiz.jsp");
					break;

				//case GrupoDt.ASSISTENTES:
				case GrupoTipoDt.ASSESSOR:
					dis = request.getRequestDispatcher("/WEB-INF/PaginaInicial/PaginaInicialAssistente.jsp");
					break;
					
				case GrupoTipoDt.JUIZ_LEIGO:
					dis = request.getRequestDispatcher("/WEB-INF/PaginaInicial/PaginaInicialAssistenteJuiz.jsp");
					break;
					
				//case GrupoDt.ASSISTENTES_ADVOGADOS_PROMOTORES:
				case GrupoTipoDt.ASSESSOR_ADVOGADO:
				case GrupoTipoDt.ASSESSOR_MP:
					dis = request.getRequestDispatcher("/WEB-INF/PaginaInicial/PaginaInicialAssistenteAdvogadoPromotor.jsp");
					break;

				//case GrupoDt.ASSISTENTES_JUIZES_VARA:
				//case GrupoDt.ASSISTENTES_JUIZES_SEGUNDO_GRAU:
				//case GrupoDt.ASSISTENTES_GABINETE:
				case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:					
				    
					dis = request.getRequestDispatcher("/WEB-INF/PaginaInicial/PaginaInicialAssistenteJuiz.jsp");
					break;
				case GrupoTipoDt.ASSISTENTE_GABINETE:
				case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
					if (UsuarioSessao.isGabinetePresidenciaTjgo() || UsuarioSessao.isGabinetePresidenciaTjgo() || ( UsuarioSessao.isGabineteUpj() && UsuarioSessao.isSegundoGrau())) {						
						dis = request.getRequestDispatcher("/WEB-INF/PaginaInicial/PaginaInicialAssistenteGabinete.jsp");
					}else {						
						dis = request.getRequestDispatcher("/WEB-INF/PaginaInicial/PaginaInicialAssistenteJuiz.jsp");
					}
					break;
				case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:						
					
					dis = request.getRequestDispatcher("/WEB-INF/PaginaInicial/PaginaInicialAssistenteDesembargador.jsp");
					break;
				case GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:
					
					dis = request.getRequestDispatcher("/WEB-INF/PaginaInicial/PaginaInicialAssistentePresidenteSegundoGrau.jsp");
					break;						
				//case GrupoDt.DISTRIBUIDOR_GABINETE:
				case GrupoTipoDt.DISTRIBUIDOR_GABINETE:
					dis = request.getRequestDispatcher("/WEB-INF/PaginaInicial/PaginaInicialDistribuidorGabinete.jsp");
					break;
				case GrupoTipoDt.MALOTE_DIGITAL:
					dis = request.getRequestDispatcher("/WEB-INF/PaginaInicial/PaginaInicialMaloteDigital.jsp");
					break;
				case GrupoTipoDt.DIVISAO_RECURSOS_CONSTITUCIONAIS:
					dis = request.getRequestDispatcher("/WEB-INF/PaginaInicial/PaginaInicialDRC.jsp");
					break;
				case GrupoTipoDt.ANALISTA_EXECUCAO:
					
					dis = request.getRequestDispatcher("/WEB-INF/PaginaInicial/PaginaInicial.jsp");
					break;
				case GrupoTipoDt.DISTRIBUIDOR_CAMARA:
					
					dis = request.getRequestDispatcher("/WEB-INF/PaginaInicial/PaginaInicialDistribuidorCamara.jsp");
					break;
				case GrupoTipoDt.DISTRIBUIDOR:
					
					dis = request.getRequestDispatcher("/WEB-INF/PaginaInicial/PaginaInicial.jsp");
					break;
				case GrupoTipoDt.COORDENADOR_CENTRAL_MANDADO:
					dis = request.getRequestDispatcher("/WEB-INF/PaginaInicial/PaginaInicialCoordenadorCentralMandado.jsp");
					break;
				case GrupoTipoDt.INTELIGENCIA:
					dis = request.getRequestDispatcher("/WEB-INF/PaginaInicial/PaginaInicialInteligencia.jsp");
					break;
				case GrupoTipoDt.DIRETORIA_FINANCEIRA:
					dis = request.getRequestDispatcher("/WEB-INF/PaginaInicial/PaginaInicialFinanceiro.jsp");
					break;
				case GrupoTipoDt.OFICIAL_JUSTICA:
					//Tratamento para não impactar oficiais trabalhando fora da central de mandado (perfis que já existem)
					//Se não for um oficial acessando de uma Central de Mandados, segue o switch case.
					if(Funcoes.StringToInt(UsuarioSessao.getServentiaSubTipoCodigo()) == ServentiaSubtipoDt.CENTRAL_MANDADOS ) {
						dis = request.getRequestDispatcher("/WEB-INF/PaginaInicial/PaginaInicialOficialJustica.jsp");
						break;
					}
				default:
					if (isExecpenweb) {
						dis = request.getRequestDispatcher("/WEB-INF/PaginaInicial/PaginaInicialExecpenweb.jsp");
					}
					else {
						dis = request.getRequestDispatcher("/WEB-INF/PaginaInicial/PaginaInicial.jsp");
					}
				break;		
			}
		}else{				 
			//usuário ctgen não vai ser usado
			dis = request.getRequestDispatcher("/index.jsp");
			request.getSession(false).invalidate();										
		}
					
		//Configura os dados para a pagina inicial
		PaginaInicialNe paginaInicialNe = new PaginaInicialNe();
		PaginaInicialDt paginaInicialDt = paginaInicialNe.consultarDados(UsuarioSessao);
		request.setAttribute("paginaInicialDt", paginaInicialDt);

		if (isExecpenweb) {
			request.getSession().setAttribute("Acesso", "Execpenweb");
		}
																
		dis.include(request, response);
	}
	
	private void obterServentiaGrupo(HttpServletRequest request, HttpServletResponse response, boolean isExecpenweb, UsuarioNe UsuarioSessao) throws Exception {
		UsuarioDt  Usuariodt = null;	
		RequestDispatcher dis = null;
		
		request.setAttribute("Menu", "");
		List listaServentiasGrupos = UsuarioSessao.consultarServentiasGrupos();
		if (listaServentiasGrupos.size() > 0) {
			request.setAttribute("listaServentiasGrupos", listaServentiasGrupos);
		} else {
			request.setAttribute("MensagemErro", "Nenhuma Serventia Disponível.");
			
			if (isExecpenweb) {
				dis = request.getRequestDispatcher("/index2.jsp");
			}
			else {
				dis = request.getRequestDispatcher("/index.jsp");
			}
			dis.include(request, response);
			return;
		}
		
		// Redireciona tela para usuário selecionar serventia/grupo
		UsuarioSessao.getUsuarioDt().setDataEntrada(Funcoes.DataHora(request.getSession().getCreationTime()));
		// UsuarioSessao.getUsuarioDt().setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		UsuarioSessao.getUsuarioDt().setIpComputadorLog(getIpCliente(request));
			
		//se existir uma unica serventia vai direto para ela
		if (listaServentiasGrupos != null && listaServentiasGrupos.size() == 1) {
			//Quando cadastro estiver irregular na OAB e houver apenas uma serventia habilidata volta para tela inicial.
			if (request.getAttribute("MensagemErro") != null) {
				if (isExecpenweb) {
					dis = request.getRequestDispatcher("/index2.jsp");
				}
				else {
					dis = request.getRequestDispatcher("/index.jsp");
				}
				dis.include(request, response);
				return;
			}
			
			Usuariodt = (UsuarioDt) listaServentiasGrupos.get(0);
			if (isExecpenweb) {
				redireciona(response, "Usuario?PaginaAtual=7&a1=" + Usuariodt.getId_UsuarioServentia() + "&a2=" + Usuariodt.getGrupoCodigo() + "&a3="
						+ Usuariodt.getId_ServentiaCargo() + "&a4=" + Usuariodt.getId_ServentiaCargoUsuarioChefe() + "&a5="
						+ Usuariodt.getId_UsuarioServentiaChefe() + "&Acesso=Execpenweb");
			} else {
									
				redireciona(response, "Usuario?PaginaAtual=7&a1=" + Usuariodt.getId_UsuarioServentia() + "&a2=" + Usuariodt.getGrupoCodigo() + "&a3="
						+ Usuariodt.getId_ServentiaCargo() + "&a4=" + Usuariodt.getId_ServentiaCargoUsuarioChefe() + "&a5="
						+ Usuariodt.getId_UsuarioServentiaChefe()  + "&LoginPronto=S");
			}
			Usuariodt = null;
			return;
		}
		
		if( isExecpenweb ) {
			dis = request.getRequestDispatcher("/WEB-INF/jsptjgo/LogonServentiaGrupoExecpenweb.jsp");
		}
		else {
			dis = request.getRequestDispatcher("/WEB-INF/jsptjgo/LogonServentiaGrupo.jsp");
		}
		dis.include(request, response);
			
	}
	

	private void obterServentiaCargo(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, boolean isExecpenweb) throws Exception{
		List listaServentiasGrupos = null;
		List tempList = null;
		RequestDispatcher dis = null;
		
		UsuarioDt Usuariodt=null;		
		String Menu ="";
		
		// Pega Serventia, grupo e cargo escolhidos
		String id_usuarioServentia = (String) request.getParameter("a1");
		String grupoCodigo = (String) request.getParameter("a2");
		String id_serventiaCargo = (String) request.getParameter("a3");
		String id_serventiaCargoUsuarioChefe = (String) request.getParameter("a4");
		String id_UsuarioServentiaChefe = (String) request.getParameter("a5");
		
        if (UsuarioSessao == null) {
            throw new MensagemException("Usuário inválido.");
        } else if (UsuarioSessao.getId_Usuario().equalsIgnoreCase("")) {
            throw new MensagemException("Usuário inválido, sem acesso ou seção expirou.");
        }
        
		tempList = (List) UsuarioSessao.getListaServentiasGruposUsuario();

		if ((id_usuarioServentia != null) && (grupoCodigo != null)) {
				/**
				 * Percorrendo lista de usuários serventias/grupos para encontrar objeto correspondente a serventia/grupo escolhido
				 */
				for (int i = 0; tempList!=null && i < tempList.size(); i++) {
					Usuariodt = ((UsuarioDt) tempList.get(i));
					if ((Usuariodt.getGrupoCodigo().equalsIgnoreCase(grupoCodigo)) 
						&& (Usuariodt.getId_UsuarioServentia().equalsIgnoreCase(id_usuarioServentia)) 
						&& (Usuariodt.getId_ServentiaCargo().equalsIgnoreCase(id_serventiaCargo)) 
						&& (Usuariodt.getId_ServentiaCargoUsuarioChefe().equalsIgnoreCase(id_serventiaCargoUsuarioChefe))
						&& (Usuariodt.getId_UsuarioServentiaChefe().equalsIgnoreCase(id_UsuarioServentiaChefe))) {
						break;
					}
					Usuariodt = null;
				}
				if (Usuariodt==null){
                    request.setAttribute("MensagemErro", "Grupo não escolhido.");
                    
					if (isExecpenweb) {
						dis = request.getRequestDispatcher("index2.jsp");
					}
					else {
						dis = request.getRequestDispatcher("index.jsp");
					}
                    dis.include(request, response);	                    					   
				    return;
				}
				
				//Caso o usuário que está acessando o sistema for Assistente, o acesso precisa ser validado 
				//para evitar que um usuário desabilitado ou que possua chefe desabilitado acesse o sistema
				switch (Funcoes.StringToInt(Usuariodt.getGrupoTipoCodigo())) {
					case GrupoTipoDt.ASSESSOR:
					case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
					case GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:
					case GrupoTipoDt.ASSESSOR_ADVOGADO:
					case GrupoTipoDt.ASSESSOR_MP:
					case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
						UsuarioNe usuarioNe = new UsuarioNe();
						String mensagemErro = usuarioNe.validarAcessoAssistentes(Usuariodt);
						if(mensagemErro != null && !mensagemErro.equalsIgnoreCase("")){
							request.setAttribute("MensagemErro", mensagemErro);
							
							if (isExecpenweb) {
								dis = request.getRequestDispatcher("/index2.jsp");
							}
							else {
								dis = request.getRequestDispatcher("/index.jsp");
							}
							dis.forward(request, response);
							return;
						}
				}
				
				//define o tempo de sessao de cada grupo
				switch (Funcoes.StringToInt(Usuariodt.getGrupoTipoCodigo())) {
					case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
					case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
					case GrupoTipoDt.JUIZ_TURMA:
					case GrupoTipoDt.PRESIDENTE_SEGUNDO_GRAU:
					    request.getSession().setMaxInactiveInterval(ProjudiPropriedades.getInstance().getTempoExpiraSessao()*3);
					    break;
					case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:									    
					case GrupoTipoDt.ASSISTENTE_GABINETE:
					case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
					case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:											
					case GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:
					case GrupoTipoDt.ANALISTA_EXECUCAO:
						request.getSession().setMaxInactiveInterval(ProjudiPropriedades.getInstance().getTempoExpiraSessao()*2);											
						break;
					case GrupoTipoDt.MP:
					case GrupoTipoDt.ASSESSOR_ADVOGADO:
					case GrupoTipoDt.ASSESSOR_MP:
					    request.getSession().setMaxInactiveInterval(ProjudiPropriedades.getInstance().getTempoExpiraSessao()*2);
					    break;
					case GrupoTipoDt.DISTRIBUIDOR:
						request.getSession().setMaxInactiveInterval(ProjudiPropriedades.getInstance().getTempoExpiraSessao()*6);						
						break;
					case GrupoTipoDt.INTELIGENCIA:
						UsuarioSessao.temDuplaAutentificacao();
						break;
				}
				
				
				Usuariodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
				Usuariodt.setDataEntrada(UsuarioSessao.getUsuarioDt().getDataEntrada());
				Usuariodt.setCodigoTemp(UsuarioSessao.getUsuarioDt().getCodigoTemp());
				UsuarioSessao.setUsuarioDt(Usuariodt);
				Usuariodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

				// Consulta permissões e menu
				Menu = UsuarioSessao.getMenu();
				request.setAttribute("Menu", Menu);
				
				//Se usuário for Advogado, verificar se está em situação regular no sistema da OAB.
				if(!(request.getLocalAddr().equals("127.0.0.1") || request.getHeader("host").matches(".*teste.*"))){
					switch (Funcoes.StringToInt(Usuariodt.getGrupoCodigo())) {
					case GrupoDt.ADVOGADO_PARTICULAR:
					case GrupoDt.ADVOGADO_PARTICULAR_ESCRITORIO_JURIDICO:
						boolean isAdvogadoRegularOab = false; 
						try {
							List<AdvogadoData> oabs = CadastroOABProxy.get().consultaAdvogado(Usuariodt.getCpf());						
							for (AdvogadoData adv : oabs) {
								ServentiaDt serventiaDt = new ServentiaNe().consultarServentiaCodigo(Usuariodt.getServentiaCodigo());
								if (serventiaDt.getEstadoRepresentacao().toUpperCase().equals(adv.getUf().toUpperCase()) && adv.getCodigoSituacao().equals("1")){
									isAdvogadoRegularOab = true;
								}								
							}
							if (!isAdvogadoRegularOab) {
								request.setAttribute("MensagemErro", "O Advogado não está com Cadastro Regular na "+ UsuarioSessao.getServentia() +", por favor, entre em contato com a mesma para resolver a situação.");
								obterServentiaGrupo(request,response, isExecpenweb, UsuarioSessao);
								return;
							}							
						} catch (Exception e) {
							
						}					
						break;
					}
				}

				String stMensagem = "Usuário " + Usuariodt.getUsuario() + " fez logon no sistema a partir de " + request.getRemoteAddr() + " com o papel de " + Usuariodt.getGrupo() + " na Serventia: " + Usuariodt.getServentia();
				//app.info(stMensagem);
				UsuarioSessao.salvarLogAcesso(stMensagem);
				
				if (UsuarioSessao.getUsuarioDt().getId_UsuarioServentiaChefe() != null && UsuarioSessao.getUsuarioDt().getId_UsuarioServentiaChefe().trim().length() > 0) {
					UsuarioServentiaDt usuarioServentiaAssistente = UsuarioSessao.buscaUsuarioServentiaId(Usuariodt.getId(), Usuariodt.getId_Serventia(), UsuarioSessao.getUsuarioDt().getId_UsuarioServentiaChefe().trim());
					if (usuarioServentiaAssistente != null) UsuarioSessao.getUsuarioDt().setPodeGuardarAssinarUsuarioServentiaChefe(usuarioServentiaAssistente.getPodeGuardarAssinarUsuarioServentiaChefe());
				}

				request.getSession().setAttribute("UsuarioSessao", UsuarioSessao);
				request.getSession().setAttribute("UsuarioSessaoDt", UsuarioSessao.getUsuarioDt());
				
				if (isExecpenweb) {
					dis = request.getRequestDispatcher("/WEB-INF/jsptjgo/index2.jsp");
					request.getSession().setAttribute("Acesso", "Execpenweb");
				}else {
					//Salva o último login do usuário
					//tenteGravarUltimoLogin(request, UsuarioSessao, id_serventia, grupoCodigo, id_serventiaCargo, id_serventiaCargoUsuarioChefe, id_UsuarioServentiaChefe);
											
					dis = request.getRequestDispatcher("/WEB-INF/jsptjgo/index.jsp");
				}
				dis.include(request, response);	
		}
	}
		
//	Método reativado dia 30/01/2018 para utilização no PJD.
	protected void tenteGravarUltimoLogin(HttpServletRequest request, UsuarioNe UsuarioSessao,	String id_serventia, String grupoCodigo, String id_serventiaCargo,	String id_serventiaCargoUsuarioChefe, String id_UsuarioServentiaChefe) {
		
		try {
			
			if (request.getParameter("LoginPronto") != null && request.getParameter("LoginPronto").equalsIgnoreCase("S")) return;
			
			UsuarioUltimoLoginDt dados = new UsuarioUltimoLoginDt();
			
			dados.setId_Usuario(UsuarioSessao.getUsuarioDt().getId());
			dados.setId_Serventia(id_serventia);
			dados.setGrupoCodigo(grupoCodigo);
			dados.setId_ServentiaCargo(id_serventiaCargo);
			dados.setId_ServentiaCargoUsuarioChefe(id_serventiaCargoUsuarioChefe);
			dados.setId_UsuarioServentiaChefe(id_UsuarioServentiaChefe);
			
			UsuarioSessao.inserirUsuarioUltimoLogin(dados);	
		} catch (Exception e) {	
			e.printStackTrace();
		}
	}
}