package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.CejuscDisponibilidadeDt;
import br.gov.go.tj.projudi.dt.UsuarioCejuscDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ps.CejuscDisponibilidadePs;
import br.gov.go.tj.utils.FabricaConexao;

public class CejuscDisponibilidadeNe extends CejuscDisponibilidadeNeGen{

	private static final long serialVersionUID = -6547967103743844924L;

	public  String Verificar(CejuscDisponibilidadeDt dados ) {

		String stRetorno = "";

		if (dados.getNome().length()==0)
			stRetorno += "O Campo Usuário CEJUSC é um é obrigatório.";
		if (dados.getAudiTipo().length()==0)
			stRetorno += "O Campo Tipo de Audiência é um é obrigatório.";
		if (dados.getServ().length()==0)
			stRetorno += "O Campo Serventia é um é obrigatório.";	
		

		return stRetorno;
	}
	
	public String consultarDescricaoUsuarioCejuscJSON(String descricao, String PosicaoPaginaAtual ) throws Exception { 
		String stTemp = (new UsuarioCejuscNe()).consultarDescricaoAprovadosJSON(descricao, PosicaoPaginaAtual);
		return stTemp;
	}
	
	public String consultarDescricaoAudienciaTipoJSON(String descricao, String idUsuarioCejusc, String PosicaoPaginaAtual) throws Exception { 
		String stTemp = (new AudienciaTipoNe()).consultarDescricaoJSON(descricao, idUsuarioCejusc, PosicaoPaginaAtual);
		return stTemp;
	}
	
	public String consultarDescricaoServentiaJSON(String descricao, String PosicaoPaginaAtual) throws Exception { 
		String stTemp = (new ServentiaNe()).consultarDescricaoServentiaPreProcessualJSON(descricao, PosicaoPaginaAtual);
		return stTemp;
	}
	
	public UsuarioCejuscDt consultarUsuarioCejuscDtIdUsuario(String idUsuario) throws Exception {
		return (new UsuarioCejuscNe()).consultarUsuarioCejuscDtIdUsuario(idUsuario);
	}
	
	public String consultarDescricaoJSON(String nome, String serventia, String idUsuario, String posicao ) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CejuscDisponibilidadePs obPersistencia = new  CejuscDisponibilidadePs(obFabricaConexao.getConexao()); 

			stTemp = obPersistencia.consultarDescricaoJSON(nome, serventia, idUsuario, posicao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public CejuscDisponibilidadeDt consultarUsuarioCejuscDt(String idUsuarioCejusc, String idAudienciaTipo, String idServentia) throws Exception {
		FabricaConexao obFabricaConexao = null;
		CejuscDisponibilidadeDt cejuscDisponibilidadeDt = null;
		if(idUsuarioCejusc != null && idUsuarioCejusc.length() > 0 && idAudienciaTipo != null && idAudienciaTipo.length() > 0 && idServentia != null && idServentia.length() > 0) {
			try {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				CejuscDisponibilidadePs obPersistencia = new  CejuscDisponibilidadePs(obFabricaConexao.getConexao()); 

				cejuscDisponibilidadeDt = obPersistencia.consultarUsuarioCejuscDt(idUsuarioCejusc, idAudienciaTipo, idServentia);
			} finally {
				obFabricaConexao.fecharConexao();
			}
		}
		return cejuscDisponibilidadeDt;
	}
	
	public UsuarioDt consultarUsuarioDt(String idUsuarioCejusc) throws Exception {
		String idUsu = null;
		UsuarioCejuscNe usuCejuscNe = new UsuarioCejuscNe();
		UsuarioNe usuNe = new UsuarioNe();
		UsuarioDt usuarioDt = null;
		
		if(idUsuarioCejusc != null) {
			idUsu = usuCejuscNe.consultarIdUsu(idUsuarioCejusc);
			usuarioDt = usuNe.consultarUsuarioCompleto(idUsu);
		}
		
		return usuarioDt;
	}
}
