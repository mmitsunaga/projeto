package br.gov.go.tj.projudi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpSession;

import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Funcoes;

public class GerenciaUsuarios implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -75654402223027592L;
	private static GerenciaUsuarios principal = null;
	
	private Map<Integer,HttpSession> maUsuariosSessao = null;	
	private ProjudiPropriedades projudiConfiguration = null;

	private GerenciaUsuarios() {
		maUsuariosSessao = new HashMap<Integer,HttpSession>();	
		projudiConfiguration = ProjudiPropriedades.getInstance();
	}

	public static GerenciaUsuarios getInstancia() {
		if (principal == null) {
			principal = new GerenciaUsuarios();
		}

		return principal;
	}

	public void addUsuario(String id_usuario, HttpSession sessao) throws Exception {
		if(!projudiConfiguration.getPropriedade(ProjudiPropriedades.servidorSPG).equals("desenv.gyn.tjgo") ) {
			if (maUsuariosSessao.containsKey(Funcoes.StringToInt(id_usuario))) {
				 HttpSession tempSessao = maUsuariosSessao.remove(Funcoes.StringToInt(id_usuario));
				 if (tempSessao!=null) {
					 try {
						 tempSessao.setAttribute("Invalidar_Sessão", true);
					 }catch(Exception e){}
				 }			 
			}	
		}		
		maUsuariosSessao.put(Funcoes.StringToInt(id_usuario), sessao);		
	}

	public List getUsuarios() {
		List<UsuarioDt> tempList = new ArrayList<UsuarioDt>();		

		for (Integer key : maUsuariosSessao.keySet()) {
			 HttpSession tempSessao = maUsuariosSessao.get(key);

			if (tempSessao != null) {
				try{
					UsuarioDt usuario = (UsuarioDt) ((UsuarioNe) tempSessao.getAttribute("UsuarioSessao")).getUsuarioDt();
					//usuario.setDataUlitmoAcesso(Funcoes.DataHora(tempSessao.getLastAccessedTime()));
					tempList.add(usuario);

				} catch(Exception e) {					
				}
			}else{
				maUsuariosSessao.remove(key);
			}			
		}
					
		return tempList;

	}

	public UsuarioDt getUsuario(String id_usuario) {
		HttpSession tempSession = (HttpSession) maUsuariosSessao.get(Funcoes.StringToInt(id_usuario));
		return (UsuarioDt) ((UsuarioNe) tempSession.getAttribute("UsuarioSessao")).getUsuarioDt();

	}


	public void InvalidaSessao(String id_usuario) {
		HttpSession tempSessao = (HttpSession) maUsuariosSessao.remove(Funcoes.StringToInt(id_usuario));
		if (tempSessao != null){
			try {
				tempSessao.invalidate();
			}catch (Exception e) {
				// TODO: handle exception
			}
		}

	}

	public void atualizarPermissoesUsuario(String id_usuario) {

		HttpSession tempSessao = (HttpSession) maUsuariosSessao.get(Funcoes.StringToInt(id_usuario));

		if (tempSessao != null) {
			try{
				UsuarioNe usuario = (UsuarioNe) tempSessao.getAttribute("UsuarioSessao");
				usuario.AtualizarPermissoes();
			} catch(Exception e) {
				maUsuariosSessao.remove(Funcoes.StringToInt(id_usuario));
			}
		} else{
			maUsuariosSessao.remove(Funcoes.StringToInt(id_usuario));
		}

	}

	public void atualizarPermissoesGrupo(String id_grupo) {
		
		for (Integer key : maUsuariosSessao.keySet()) {
			 HttpSession tempSessao = maUsuariosSessao.get(key);

			if (tempSessao != null) {
				try{
					UsuarioNe usuario = (UsuarioNe) tempSessao.getAttribute("UsuarioSessao");

					if (usuario.getUsuarioDt().getId_Grupo().equalsIgnoreCase(id_grupo)){
						usuario.AtualizarPermissoes();
					}

				} catch(Exception e) {					
				}
			}else{
				maUsuariosSessao.remove(key);
			}			
		}		

	}
}
