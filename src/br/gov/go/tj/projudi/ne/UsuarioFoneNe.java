package br.gov.go.tj.projudi.ne;


import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioFoneDt;
import br.gov.go.tj.projudi.ps.UsuarioFonePs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class UsuarioFoneNe extends UsuarioFoneNeGen{

//

/**
	 * 
	 */
	private static final long serialVersionUID = -1651466555753293135L;

	//---------------------------------------------------------
	public  String Verificar(UsuarioFoneDt dados ) {

		String stRetorno="";

		if (dados.getUsuario().length()==0)
			stRetorno += "O Campo Usu é obrigatório.";
		if (dados.getImei().length()==0)
			stRetorno += "O Campo Imei é obrigatório.";
		if (dados.getFone().length()==0)
			stRetorno += "O Campo Fone é obrigatório.";

		return stRetorno;

	}

	//---------------------------------------------------------
	public void salvar(UsuarioFoneDt dados ) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			UsuarioFonePs obPersistencia = new UsuarioFonePs(obFabricaConexao.getConexao()); 

			/* use o id do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("UsuarioFone",dados.getId(),UsuarioDt.SistemaProjudi, dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("UsuarioFone",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		}catch(Exception e){ 
			obFabricaConexao.cancelarTransacao();			
			throw e;
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
		
	public String pegarCodigoCadastrar(UsuarioFoneDt dtNovo) {
		/**
		 * codigo;mensagem 
		 * codigo -> 0, 1 ou 2, 0 para erro, 1 para mensagem, 2 para código.
		 */
		String stMensagem="";
		try {
    		if (dtNovo.getImei()==null || dtNovo.getUsuario()==null) {
    			stMensagem ="0;Ocorreu um erro no dados enviados (0)";
    		}else if(dtNovo.getImei().isEmpty() || dtNovo.getUsuario().isEmpty()) {
    			stMensagem ="0;Ocorreu um erro no dados enviados, vazios (1)";
    		}else {
    			FabricaConexao obFabricaConexao = null;
    		    
    	    	try{
    	    	    obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
    	    	    UsuarioFonePs ps = new UsuarioFonePs(obFabricaConexao.getConexao());
    	    
    	    	    //1º verificar se ja não existe um pedido pendente para esse usuario e fone
    	    	    UsuarioFoneDt dtAtual =ps.consultarUsuarioCpfImei(dtNovo.getUsuario(),dtNovo.getFone(),dtNovo.getImei());
    	    	    if(dtAtual==null) {
    	    	    	//consulto o usuário
    	    	    	UsuarioDt usuariodt =  (new UsuarioNe()).consultarUsuarioCpf(dtNovo.getUsuario());
    	    	    	if (usuariodt!=null) {
        	    	    	//cria um cadastro para a validação em duas etapas
    	    	    		dtNovo.setId_Usuario(usuariodt.getId());
        	    	    	salvar(dtNovo);
        	    	    	stMensagem ="1;Faça a liberação do aparelho no sistema.";
    	    	    	}else {
    	    	    		stMensagem ="1;Usuário não encontrado.";
    	    	    	}
    	    	    }else {
    	    	    	//verificar que o cadastro ja esta liberado 
    	    	    	if(dtAtual.isLiberado()) {
    	    	    		String stCaracteres = "AaBbCc@@DdEeFfGgHh$$IiJjKkLl##MmNn&&OoPqRrSsTtUuVvXxWwYyZz";
    	    	    		int loNumero = (int) Math.round((Math.random()*9999));
    	    	    		int loCaracter = (int) Math.round((Math.random()*stCaracteres.length()));
    	    	    		String codigo= stCaracteres.substring(loCaracter,loCaracter+1) + loNumero;
    	    	    		dtAtual.setCodigo(codigo);
    	    	    		
    	    	    		salvarCodigo(dtAtual);
    	    	    		stMensagem ="2;" + codigo;
    	    	    	}else {
    	    	    		stMensagem ="1;Libere esse aparelho para obter o código";
    	    	    	}
    	    	    }
    	    	    
    	    	
    	    	} finally{
    	    	    obFabricaConexao.fecharConexao();
    	    	}
    		}
		
			
		}catch (Exception e) {
			stMensagem ="0;Não foi possível obter o código";
		}
		return stMensagem;
	}

	private void salvarCodigo(UsuarioFoneDt dtAtual) throws Exception {
		LogDt obLogDt;

		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			UsuarioFonePs obPersistencia = new UsuarioFonePs(obFabricaConexao.getConexao()); 
			
			obPersistencia.salvarCodigo( dtAtual.getId(),  dtAtual.getCodigo(), (System.currentTimeMillis()+60000));
			obLogDt = new LogDt("UsuarioFone",  dtAtual.getId_Usuario(),UsuarioDt.SistemaProjudi,  dtAtual.getIpComputadorLog(), String.valueOf(LogTipoDt.CODIGO_VALIDACAO),"", dtAtual.getCodigo());
			
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		}catch(Exception e){ 
			obFabricaConexao.cancelarTransacao();			
			throw e;
		}finally{
			obFabricaConexao.fecharConexao();
		}
		
	}

	public boolean validaCodigo(String id_Usuario, String codigo) throws Exception {
		if(codigo==null) {
			return false;
		}
		if(codigo.isEmpty()) {
			return false;
		}
		String codigoAtual="adsfa3s2d1fa65sd1fa6s5d1fa6sdf";
		
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {
			UsuarioFonePs obPersistencia = new UsuarioFonePs(obFabricaConexao.getConexao());
			codigoAtual=obPersistencia.consultarCodigoValidacao(id_Usuario);
		} finally{
    	    obFabricaConexao.fecharConexao();
    	}

		//a validação é case sensetive 
		if(codigo.equals(codigoAtual)) {
			return true;
		}
			
		return false;
	}

	public boolean temDuplaAutentificacao(String id_Usuario) throws Exception {
		// TODO Auto-generated method stub
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		boolean boRetorno = false;
						
		try{
    	    
    		UsuarioFonePs obPersistencia = new UsuarioFonePs(obFabricaConexao.getConexao());    
    		boRetorno =  obPersistencia.temDuplaAutentificacao(id_Usuario);
    	
    	} finally{
    	    obFabricaConexao.fecharConexao();
    	}
    	return boRetorno;
    	
	}

	public List consultarFones(String id_Usuario) throws Exception {
    	List lisTemp = null;
    	FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
    	
    	try{
    	    
    		UsuarioFonePs obPersistencia = new UsuarioFonePs(obFabricaConexao.getConexao());    
    	    lisTemp = obPersistencia.consultarFones(id_Usuario);
    	
    	} finally{
    	    obFabricaConexao.fecharConexao();
    	}
    	return lisTemp;
    			
	}

	public void bloquear(String id_usuarioFone) throws Exception {
    	FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
    	
    	try{
    	    
    		UsuarioFonePs obPersistencia = new UsuarioFonePs(obFabricaConexao.getConexao());    
    	    obPersistencia.bloquear(id_usuarioFone);
    	
    	} finally{
    	    obFabricaConexao.fecharConexao();
    	}
		
	}

	public void liberar(String id_usuarioFone, String id_usu) throws Exception {
    	FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
    	
    	try{
    	    obFabricaConexao.iniciarTransacao();
    		UsuarioFonePs obPersistencia = new UsuarioFonePs(obFabricaConexao.getConexao());    		
    	    obPersistencia.liberar(id_usuarioFone, id_usu);
    	    obFabricaConexao.finalizarTransacao();
    	}catch (Exception e){
    		obFabricaConexao.cancelarTransacao();
    		throw e;
    	} finally{
    	    obFabricaConexao.fecharConexao();
    	}
		
	}

}
