package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.EnderecoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioCejuscArquivoDt;
import br.gov.go.tj.projudi.dt.UsuarioCejuscDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ps.UsuarioCejuscPs;
import br.gov.go.tj.utils.FabricaConexao;

public class AprovarCandidatoNe extends Negocio {
	
	public static final String NOME_CONTROLE_WEB_XML = "AprovarCandidato";
	
	private EnderecoNe enderecoNe = null;
	private UsuarioNe usuarioNe = null;
	private UsuarioCejuscNe usuarioCejuscNe;
	private UsuarioCejuscArquivoNe usuarioCejuscArquivoNe;

	/**
	 * Construtor
	 * @author fasoares
	 */
	public AprovarCandidatoNe() {
		usuarioCejuscNe = new UsuarioCejuscNe();
		usuarioNe = new UsuarioNe();
		enderecoNe = new EnderecoNe();
		usuarioCejuscArquivoNe = new UsuarioCejuscArquivoNe();
	}
	
	/**
	 * Método para consultar UsuarioCejuscDt pelo id do UsuadioDt.
	 * @param String idUsuarioDt
	 * @return UsuarioCejuscDt
	 * @throws Exception
	 */
	public UsuarioCejuscDt consultaUsuarioCejuscDtId(String idUsuarioDt) throws Exception {
		UsuarioCejuscDt usuarioCejuscDt = null;
		
		if( idUsuarioDt != null ) {
			
			UsuarioDt usuarioDt = usuarioNe.consultarId(idUsuarioDt);
			
			if( usuarioDt != null ) {
				EnderecoDt enderecoDt = enderecoNe.consultarId(usuarioDt.getId_Endereco());
				if( enderecoDt != null ) {
					usuarioDt.setEnderecoUsuario(enderecoDt);
				}
				
				usuarioCejuscDt = usuarioCejuscNe.consultarUsuarioCejuscDtIdUsuario(idUsuarioDt);
				
				if( usuarioCejuscDt != null ) {
					
					usuarioCejuscDt.setListaUsuarioCejuscArquivoDt(usuarioCejuscArquivoNe.consultaListaUsuarioCejuscArquivoDt(usuarioCejuscDt.getId()));
					
					usuarioCejuscDt.setUsuarioDt(usuarioDt);
				}
			}
			
		}
		
		return usuarioCejuscDt;
	}
	
	/**
	 * Método para consultar UsuarioCejuscDt pelo id do UsuadioDt.
	 * @param String idUsuarioDt
	 * @return UsuarioCejuscDt
	 * @throws Exception
	 */
//	public UsuarioCejuscDt consultaUsuarioCejuscDtId(String idUsuarioDt) throws Exception {
//		if( usuarioCejuscNe == null ) {
//			usuarioCejuscNe = new UsuarioCejuscNe();
//		}
//		if( usuarioCejuscComarcaNe == null ) {
//			usuarioCejuscComarcaNe = new UsuarioCejuscComarcaNe();
//		}
//		if( usuarioCejuscComarcaTurnoNe == null ) {
//			usuarioCejuscComarcaTurnoNe = new UsuarioCejuscComarcaTurnoNe();
//		}
//		
//		UsuarioCandidatoDt usuarioCandidatoDt = null;
//		
//		if( idUsuarioCandidato != null && idUsuarioCandidato.length() > 0 ) {
//			usuarioCandidatoDt = usuarioCandidatoNe.consultarUsuarioCandidatoDtId(idUsuarioCandidato);
//			
//			if( usuarioCandidatoDt != null ) {
//				
//				//Preenche informações para apresentar na tela
//				ConfirmacaoEmailInscricaoNe confirmacaoEmailInscricaoNe = new ConfirmacaoEmailInscricaoNe();
//				if( usuarioCandidatoDt.getId_Naturalidade() != null && usuarioCandidatoDt.getId_Naturalidade().length() > 0 ) {
//					CidadeDt cidadeDt = confirmacaoEmailInscricaoNe.consultaCidadeDtId(usuarioCandidatoDt.getId_Naturalidade());
//					usuarioCandidatoDt.setNaturalidade( cidadeDt.getCidade() );
//				}
//				if( usuarioCandidatoDt.getId_RgOrgaoExpedidor() != null && usuarioCandidatoDt.getId_RgOrgaoExpedidor().length() > 0 ) {
//					RgOrgaoExpedidorDt rgOrgaoExpedidorDt = confirmacaoEmailInscricaoNe.consultaRgOrgaoExpedidorDtId(usuarioCandidatoDt.getId_RgOrgaoExpedidor());
//					usuarioCandidatoDt.setRgOrgaoExpedidor( rgOrgaoExpedidorDt.getRgOrgaoExpedidor() );
//				}
//				if( usuarioCandidatoDt.getEnderecoDt().getId_Bairro() != null && usuarioCandidatoDt.getEnderecoDt().getId_Bairro().length() > 0 ) {
//					BairroDt bairroDt = confirmacaoEmailInscricaoNe.consultaBairroDtId(usuarioCandidatoDt.getEnderecoDt().getId_Bairro());
//					usuarioCandidatoDt.setNaturalidade( bairroDt.getBairro() );
//				}
//				
//				//Consulta EnderecoDt
//				if( usuarioCandidatoDt.getEnderecoDt() != null & usuarioCandidatoDt.getEnderecoDt().getId() != null ) {
//					EnderecoDt enderecoDt = confirmacaoEmailInscricaoNe.consultaEnderecoDtPorId(usuarioCandidatoDt.getEnderecoDt().getId());
//					usuarioCandidatoDt.setEnderecoDt(enderecoDt);
//				}
//				
//				//Consulta lista de UsuarioCandidatoComarcaDt
//				usuarioCandidatoDt.setListaUsuarioCandidatoComarcaDt(usuarioCandidatoComarcaNe.consultarPorId_UsuarioCandidato(usuarioCandidatoDt.getId()));
//				if( usuarioCandidatoDt.getListaUsuarioCandidatoComarcaDt() != null & usuarioCandidatoDt.getListaUsuarioCandidatoComarcaDt().size() > 0 ) {
//					
//					for( UsuarioCandidatoComarcaDt usuarioCandidatoComarcaDt: usuarioCandidatoDt.getListaUsuarioCandidatoComarcaDt() ) {
//						
//						//consulta a comarcaDt
//						if( usuarioCandidatoComarcaDt.getComarcaDt() != null & usuarioCandidatoComarcaDt.getComarcaDt().getId() != null ) {
//							usuarioCandidatoComarcaDt.setComarcaDt(confirmacaoEmailInscricaoNe.consultaComarcaDtId(usuarioCandidatoComarcaDt.getComarcaDt().getId()));
//						}
//						
//						//consulta a serventiaDt
//						if( usuarioCandidatoComarcaDt.getServentiaDt() != null & usuarioCandidatoComarcaDt.getServentiaDt().getId() != null ) {
//							usuarioCandidatoComarcaDt.setServentiaDt(confirmacaoEmailInscricaoNe.consultaServentiaDtId(usuarioCandidatoComarcaDt.getServentiaDt().getId()));
//						}
//						
//						//consulta a lista de turnos para esta comarca e serventia
//						usuarioCandidatoComarcaDt.setListaUsuarioCandidatoComarcaTurnoDt(usuarioCandidatoComarcaTurnoNe.consultarPorId_UsuarioCandidatoComarca(usuarioCandidatoComarcaDt.getId()));
//					}
//				}
//				
//				//Consulta os arquivos
//				UsuarioCandidatoArquivoNe usuarioCandidatoArquivoNe = new UsuarioCandidatoArquivoNe();
//				usuarioCandidatoDt.setListaUsuarioCandidatoArquivoDt(usuarioCandidatoArquivoNe.consultaListaUsuarioCandidatoArquivoDt(usuarioCandidatoDt.getId()));
//				
//			}
//		}
//		
//		return usuarioCejuscDt;
//	}
	
	/**
	 * Método para consulta lista de usuarioCejuscDt no formato JSON
	 * @param String nome
	 * @param String cpf
	 * @param String statusAtualUsuario
	 * @param String idServentia
	 * @param String apenasVoluntarios
	 * @param String posicaopaginaatual
	 * @return String
	 * @throws Exception
	 */
	public String consultarListaUsuarioCejuscDtJSON(String nome, String cpf, String statusAtualUsuario, String idServentia, String apenasVoluntarios, String posicaopaginaatual) throws Exception {
		return usuarioCejuscNe.consultarListaUsuarioCejuscDtJSON(nome, cpf, statusAtualUsuario, idServentia, apenasVoluntarios, posicaopaginaatual);
	}
	
	/**
	 * Método para consultar UsuarioCejuscArquivoDt pelo ID
	 * @param String id
	 * @return UsuarioCejuscArquivoDt
	 * @throws Exception
	 */
	public UsuarioCejuscArquivoDt consultarUsuarioCejuscArquivoDtId(String id) throws Exception {
		return usuarioCejuscArquivoNe.consultarUsuarioCejuscArquivoDtId(id);
	}
	
	/**
	 * Método para alterar o status do Cejusc e fazer as tarefas necessárias para ele já trabalhar.
	 * @param UsuarioCejuscDt usuarioCejuscDt
	 * @return boolean
	 * @throws Exception
	 */
//	public boolean alterarStatusCejusc(UsuarioCejuscDt usuarioCejuscDt) throws Exception {
//		boolean retorno = false;
//		
//		FabricaConexao obFabricaConexao = null;
//		try {
//			if( usuarioCejuscDt != null 
//				&& usuarioCejuscDt.getId() != null && usuarioCejuscDt.getId().length() > 0 
//				&& usuarioCejuscDt.getCodigoStatusAtual() != null && usuarioCejuscDt.getCodigoStatusAtual().length() > 0 ) {
//				
//				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
//				obFabricaConexao.iniciarTransacao();
//				
//				//Se o usuário não estiver aprovado ainda, permite mudar o status.
//				if( !usuarioCejuscDt.getCodigoStatusAnterior().equals(UsuarioCejuscDt.CODIGO_STATUS_APROVADO) ) {
//					
//					//Aprovar somente a opção(conciliador e/ou mediador) que o candidato escolheu
//					if( usuarioCejuscDt.getOpcaoConciliador() != null && !usuarioCejuscDt.getOpcaoConciliador().equals(ConfirmacaoEmailInscricaoNe.CODIGO_PERFIL_NAO_ESCOLHIDO) ) {
//						usuarioCejuscDt.setOpcaoConciliador( ConfirmacaoEmailInscricaoNe.CODIGO_PERFIL_APROVADO );
//					}
//					if( usuarioCejuscDt.getOpcaoMediador() != null && !usuarioCejuscDt.getOpcaoMediador().equals(ConfirmacaoEmailInscricaoNe.CODIGO_PERFIL_NAO_ESCOLHIDO) ) {
//						usuarioCejuscDt.setOpcaoMediador( ConfirmacaoEmailInscricaoNe.CODIGO_PERFIL_APROVADO );
//					}
//					
//					usuarioCejuscNe.atualizarStatus(usuarioCejuscDt, obFabricaConexao);
//					
//					//Vincula usuário com o grupo público(usuario-serventia-grupo)
//					ServentiaDt serventiaDt = new ServentiaNe().consultarServentiaCodigo(String.valueOf(ServentiaDt.PUBLICA_CODIGO));
//					GrupoDt grupoDt = new GrupoNe().consultarGrupoCodigo(String.valueOf(GrupoDt.PUBLICO));
//					
//					if( serventiaDt != null && 
//						serventiaDt.getId() != null &&
//						grupoDt != null && 
//						grupoDt.getId() != null ) {
//						
//						usuarioCejuscDt.getUsuarioDt().setId_Serventia(serventiaDt.getId());
//						usuarioCejuscDt.getUsuarioDt().setId_Grupo(grupoDt.getId());
//						
//						String idUsuarioServentiaGrupo = usuarioNe.consultarIdUsuarioServentiaGrupo(grupoDt.getId(), serventiaDt.getId(), usuarioCejuscDt.getUsuarioDt().getId());
//						
//						//Somente vincula caso o usuário não esteja vinculado
//						if( idUsuarioServentiaGrupo == null || idUsuarioServentiaGrupo.trim().length() == 0 ) {
//							usuarioCejuscDt.getUsuarioDt().setId_UsuarioLog(this.//salvar o id de quem está operando o sistema);
//							usuarioNe.salvarUsuarioServentiaGrupo(usuarioCejuscDt.getUsuarioDt(), obFabricaConexao);
//						}
//					}
//					
//					// Se estiver modificando o status para Aprovado desbloqueia ele para que ele possa então fazer login.
//					// Se o usuário não estiver bloqueado não fará diferença.
//					//if( usuarioCejuscDt.getCodigoStatusAtual().equals(UsuarioCejuscDt.CODIGO_STATUS_APROVADO) ) {
//					//	usuarioNe.ativarUsuario( usuarioCejuscDt.getUsuarioDt() );
//					//}
//					
//					
//					//Enviar email para o Cejusc informando da decisão deste processo
//					new InscricaoConciliadorMediadorNe().enviarEmailNovoStatus(usuarioCejuscDt.getUsuarioDt().getEMail(), usuarioCejuscDt.getUsuarioDt().getNome(), usuarioCejuscDt.getLabelCodigoStatus(usuarioCejuscDt.getCodigoStatusAtual()));
//					
//					retorno = true;
//				}
//	
//				obFabricaConexao.finalizarTransacao();
//			}
//		}
//		catch(Exception e) {
//			if( obFabricaConexao != null ) {
//				obFabricaConexao.cancelarTransacao();
//			}
//		}
//		finally {
//			if( obFabricaConexao != null ) {
//				obFabricaConexao.fecharConexao();
//			}
//		}
//		
//		return retorno;
//	}
	
	public void alterarStatusCejusc(String idUsuCejusc, String codigoNovoStatus, String observacaoStatus, String id_usuarioLog) throws Exception {
		FabricaConexao obFabricaConexao = null;
		
		try {
			if( idUsuCejusc != null 
				&& codigoNovoStatus != null && !codigoNovoStatus.equals("") 
				&& observacaoStatus != null && !observacaoStatus.equals("") 
				&& id_usuarioLog 	!= null && !id_usuarioLog.equals("") ) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				obFabricaConexao.iniciarTransacao();

				UsuarioCejuscDt usuCejuscDt = this.consultarUsuarioCejuscDtIdUsuarioCejusc(idUsuCejusc);
				
				if(usuCejuscDt != null) {
					usuCejuscDt.setCodigoStatusAtual(codigoNovoStatus);
					usuCejuscDt.setObservacaoStatus(observacaoStatus);
					usuCejuscDt.setId_UsuarioLog(id_usuarioLog);
					
					if(codigoNovoStatus.equals(UsuarioCejuscDt.CODIGO_STATUS_APROVADO)) {
						//Aprovar somente a opção(conciliador e/ou mediador) que o candidato escolheu
						if( usuCejuscDt.getOpcaoConciliador() != null && !usuCejuscDt.getOpcaoConciliador().equals(ConfirmacaoEmailInscricaoNe.CODIGO_PERFIL_NAO_ESCOLHIDO) ) {
							usuCejuscDt.setOpcaoConciliador( ConfirmacaoEmailInscricaoNe.CODIGO_PERFIL_APROVADO );
						}
						if( usuCejuscDt.getOpcaoMediador() != null && !usuCejuscDt.getOpcaoMediador().equals(ConfirmacaoEmailInscricaoNe.CODIGO_PERFIL_NAO_ESCOLHIDO) ) {
							usuCejuscDt.setOpcaoMediador( ConfirmacaoEmailInscricaoNe.CODIGO_PERFIL_APROVADO );
						}
					}
					
					usuarioCejuscNe.atualizarStatus(usuCejuscDt, obFabricaConexao);
					
					//Vincula usuário com o grupo público(usuario-serventia-grupo)
					ServentiaDt serventiaDt = new ServentiaNe().consultarServentiaCodigo(String.valueOf(ServentiaDt.PUBLICA_CODIGO));
					GrupoDt grupoDt = new GrupoNe().consultarGrupoCodigo(String.valueOf(GrupoDt.PUBLICO));
					UsuarioDt usuDt = new UsuarioDt();
					usuDt.setId(usuCejuscDt.getUsuarioDt().getId());
					//Somente vincula caso o usuário não esteja vinculado
					String idUsuarioServentiaGrupo = usuarioNe.consultarIdUsuarioServentiaGrupo(grupoDt.getId(), serventiaDt.getId(), usuDt.getId());
					if( idUsuarioServentiaGrupo == null || idUsuarioServentiaGrupo.trim().length() == 0 ) {
						usuDt.setId_Serventia(serventiaDt.getId());
						usuDt.setId_Grupo(grupoDt.getId());
						usuDt.setId_UsuarioLog(id_usuarioLog);
						usuarioNe.salvarUsuarioServentiaGrupo(usuDt, obFabricaConexao);
					}
					
					// Se estiver modificando o status para Aprovado desbloqueia ele para que ele possa então fazer login.
					// Se o usuário não estiver bloqueado não fará diferença.
					if( usuCejuscDt.getCodigoStatusAtual().equals(UsuarioCejuscDt.CODIGO_STATUS_APROVADO) ) {
						//TODO: dando null em log_id_usu
						usuCejuscDt.getUsuarioDt().setId_UsuarioLog(id_usuarioLog);
						usuarioNe.ativarUsuario( usuCejuscDt.getUsuarioDt(), obFabricaConexao );
					}
					
					
					//Envia email para o Cejusc informando da decisão deste processo
					String email = this.usuarioNe.consultarEmailUsuario(usuCejuscDt.getUsuarioDt().getId());
					new InscricaoConciliadorMediadorNe().enviarEmailNovoStatus(email, usuCejuscDt.getUsuarioDt().getNome(), usuCejuscDt.getLabelCodigoStatus(usuCejuscDt.getCodigoStatusAtual()));
				}
				
				obFabricaConexao.finalizarTransacao();
			}
		}
		catch(Exception e) {
			if( obFabricaConexao != null ) {
				obFabricaConexao.cancelarTransacao();
			}
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
	}
	
	/**
	 * Método para consultar UsuarioCejuscDt pelo ID_USU_CEJUSC
	 * @param String idUsuarioCejusc
	 * @return UsuarioCejuscDt
	 * @throws Exception
	 */
	public UsuarioCejuscDt consultarUsuarioCejuscDtIdUsuarioCejusc(String idUsuarioCejusc) throws Exception {
		UsuarioCejuscDt usuarioCejuscDt = null;
		UsuarioCejuscNe usuarioCejuscNe = new UsuarioCejuscNe();
		
		if( idUsuarioCejusc != null && idUsuarioCejusc.length() > 0 ) {
			usuarioCejuscDt = usuarioCejuscNe.consultarUsuarioCejuscDtIdUsuarioCejusc(idUsuarioCejusc);
		}
		
		return usuarioCejuscDt;
	}
	
	public String consultarDescricaoServentiaJSON(String tempNomeBusca, String PosicaoPaginaAtual) throws Exception {
		String stTemp = "";
		
		ServentiaNe serventiaNe = new ServentiaNe();
		stTemp = serventiaNe.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}
	
	public ServentiaDt consultarServentiaId(String id_serventia) throws Exception {
		ServentiaNe serventiaNe = new ServentiaNe();
		
		return serventiaNe.consultarId(id_serventia);
	}	
}
