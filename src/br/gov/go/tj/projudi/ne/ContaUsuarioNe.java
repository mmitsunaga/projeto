package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.ContaUsuarioDt;
import br.gov.go.tj.projudi.ps.ContaUsuarioPs;
import br.gov.go.tj.utils.FabricaConexao;

public class ContaUsuarioNe extends ContaUsuarioNeGen{

    private static final long serialVersionUID = 4967057735507132273L;

	public  String Verificar(ContaUsuarioDt dados ) {

		String stRetorno="";
		
		 if (dados.getId_Agencia().equalsIgnoreCase(""))
			stRetorno += "Agência é campo obrigatório. ";
		 if (dados.getId_Usuario().equalsIgnoreCase(""))
			stRetorno += "Usuário é campo obrigatório. ";

		return stRetorno;

	}
	
	/**
	 * Método para fazer consulta de todos os usuários do sistema
	 * @param tempNomeBusca - nome de busca
	 * @param PosicaoPaginaAtual - posição da página
	 * @return lista de usuários do sistema
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarTodosUsuarios(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
		UsuarioNe Usuarione = new UsuarioNe(); 
		tempList = Usuarione.consultarTodosUsuarios(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Usuarione.getQuantidadePaginas();
		Usuarione = null;
		
		return tempList;
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception{
		String stTemp = "";
		FabricaConexao obFabricaConexao = null; 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ContaUsuarioPs obPersistencia = new ContaUsuarioPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}
	
	public String consultarDescricaoAgenciaJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception{
		String stTemp = "";
		
		AgenciaNe Agenciane = new AgenciaNe(); 
		stTemp = Agenciane.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);		
		
		return stTemp;
	}
	
	public String consultarTodosUsuariosJSON(String nome, String usuario, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		UsuarioNe Usuarione = new UsuarioNe(); 
		stTemp = Usuarione.consultarTodosUsuariosJSON(nome, usuario, PosicaoPaginaAtual);
		
		return stTemp;
	}

}
