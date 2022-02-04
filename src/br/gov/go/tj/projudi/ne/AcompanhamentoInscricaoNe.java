package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.UsuarioCejuscDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.utils.Funcoes;

public class AcompanhamentoInscricaoNe extends Negocio {
	
	public static final String NOME_CONTROLE = "AcompanhamentoInscricao";
	
	/**
	 * Consulta UsuarioCejuscDt pelo id do UsuarioDt.
	 * @param String idUsuarioDt
	 * @return UsuarioCejuscDt
	 * @throws Exception
	 */
	public UsuarioCejuscDt consultarUsuarioCejuscDtIdUsuario(String idUsuarioDt) throws Exception {
		UsuarioCejuscDt usuarioCejuscDt = null;
		
		if( idUsuarioDt != null && idUsuarioDt.length() > 0 ) {
			UsuarioCejuscNe usuarioCejuscNe = new UsuarioCejuscNe();
			usuarioCejuscDt = usuarioCejuscNe.consultarUsuarioCejuscDtIdUsuario(idUsuarioDt);
		}
		
		return usuarioCejuscDt;
	}
	
	/**
	 * Testa o login do usuário com a particularidade de não levar em consideração se o mesmo está
	 * ativo ou não no projudi.
	 * @param String cpf
	 * @param String senha
	 * @return boolean
	 * @throws Exception
	 */
	public boolean testeUsuarioSenhaCejusc(String cpf, String senha) throws Exception {
		UsuarioNe usuarioNe = new UsuarioNe();
		UsuarioDt usuarioDt = null;
		
		if( (cpf != null && cpf.trim().length() > 0) && (senha != null && senha.trim().length() > 0) ) {
			
			usuarioDt = usuarioNe.consultarUsuarioCpf(cpf);
			if( usuarioDt != null && usuarioDt.getId() != null ) {
				usuarioDt = usuarioNe.consultarUsuarioCompleto( usuarioDt.getId() );
				
				if( Funcoes.SenhaMd5(senha).equals( usuarioDt.getSenha() ) ) {
					return true;
				} 
			}
		} 
			
		return false;
	}
	
	/**
	 * Método para validar o logon do usuário.
	 * 
	 * @param String cpfLogin
	 * @param String senhaLogin
	 * @return boolean
	 * @throws Exception
	 */
//	public boolean validarLogon(String cpfLogin, String senhaLogin) throws Exception {
//		boolean retorno = false;
//		
//		if( cpfLogin != null & senhaLogin != null ) {
//			
//			retorno = new UsuarioCejuscNe().validarLogon(cpfLogin, senhaLogin);
//			
//		}
//		
//		return retorno;
//	}
	
	/**
	 * Método para consultar UsuarioCejuscDt pelo ID
	 * @param String id
	 * @return UsuarioCejuscDt
	 * @throws Exception
	 */
//	public UsuarioCejuscDt consultarUsuarioCejuscDtId(String id) throws Exception {
//		return new UsuarioCejuscNe().consultarUsuarioCejuscDtId(id);
//	}
	
	/**
	 * Método para consultar UsuarioCejuscDt pelo ID
	 * @param String cpf
	 * @return UsuarioCejuscDt
	 * @throws Exception
	 */
//	public UsuarioCejuscDt consultarUsuarioCejuscDtCPF(String cpf) throws Exception {
//		UsuarioCejuscDt usuarioCejuscDt = null;
//		
//		ConfirmacaoEmailInscricaoNe confirmacaoEmailInscricaoNe = new ConfirmacaoEmailInscricaoNe();
//		UsuarioCejuscComarcaNe usuarioCejuscComarcaNe = new UsuarioCejuscComarcaNe();
//		UsuarioCejuscComarcaTurnoNe usuarioCejuscComarcaTurnoNe = new UsuarioCejuscComarcaTurnoNe();
//		
//		//Consulta UsuarioCejuscDt
//		usuarioCejuscDt = new UsuarioCejuscNe().consultarUsuarioCejuscDtCPF(cpf);
//		
//		if( usuarioCejuscDt != null & usuarioCejuscDt.getId() != null & usuarioCejuscDt.getId().length() > 0 ) {
//			
//			//Consulta EnderecoDt
//			if( usuarioCejuscDt.getEnderecoDt() != null & usuarioCejuscDt.getEnderecoDt().getId() != null ) {
//				EnderecoDt enderecoDt = confirmacaoEmailInscricaoNe.consultaEnderecoDtPorId(usuarioCejuscDt.getEnderecoDt().getId());
//				usuarioCejuscDt.setEnderecoDt(enderecoDt);
//			}
//			
//			//Consulta lista de UsuarioCejuscComarcaDt
//			usuarioCejuscDt.setListaUsuarioCejuscComarcaDt(usuarioCejuscComarcaNe.consultarPorId_UsuarioCejusc(usuarioCejuscDt.getId()));
//			if( usuarioCejuscDt.getListaUsuarioCejuscComarcaDt() != null & usuarioCejuscDt.getListaUsuarioCejuscComarcaDt().size() > 0 ) {
//				
//				for( UsuarioCejuscComarcaDt usuarioCejuscComarcaDt: usuarioCejuscDt.getListaUsuarioCejuscComarcaDt() ) {
//					
//					//consulta a comarcaDt
//					if( usuarioCejuscComarcaDt.getComarcaDt() != null & usuarioCejuscComarcaDt.getComarcaDt().getId() != null ) {
//						usuarioCejuscComarcaDt.setComarcaDt(confirmacaoEmailInscricaoNe.consultaComarcaDtId(usuarioCejuscComarcaDt.getComarcaDt().getId()));
//					}
//					
//					//consulta a serventiaDt
//					if( usuarioCejuscComarcaDt.getServentiaDt() != null & usuarioCejuscComarcaDt.getServentiaDt().getId() != null ) {
//						usuarioCejuscComarcaDt.setServentiaDt(confirmacaoEmailInscricaoNe.consultaServentiaDtId(usuarioCejuscComarcaDt.getServentiaDt().getId()));
//					}
//					
//					//consulta a lista de turnos para esta comarca e serventia
//					usuarioCejuscComarcaDt.setListaUsuarioCejuscComarcaTurnoDt(usuarioCejuscComarcaTurnoNe.consultarPorId_UsuarioCejuscComarca(usuarioCejuscComarcaDt.getId()));
//				}
//			}
//		}
//		
//		return usuarioCejuscDt;
//	}

}
