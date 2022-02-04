package br.gov.go.tj.projudi.ne;

import java.io.Serializable;
import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ps.UsuarioPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class UsuarioNeGen extends Negocio implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -122613509114404389L;

	
	protected UsuarioDt obDados;
	
	 
	public UsuarioNeGen() {		

		obLog = new LogNe(); 

		obDados = new UsuarioDt(); 

	}


//---------------------------------------------------------
	public void salvar(UsuarioDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		//////System.out.println("..neUsuariosalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("Usuario", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("Usuario", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(UsuarioDt dados ); 


//---------------------------------------------------------

	public void excluir(UsuarioDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("Usuario", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public UsuarioDt consultarId(String id_usuario ) throws Exception {

		UsuarioDt dtRetorno=null;
		//////System.out.println("..ne-ConsultaId_Usuario" );
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_usuario ); 
			obDados.copiar(dtRetorno);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

//---------------------------------------------------------

	public long getQuantidadePaginas(){
		return QuantidadePaginas;
	}
//---------------------------------------------------------

	public List consultarDescricao(String descricao, String posicao ) throws Exception {
		List tempList=null;
		//////System.out.println("..ne-ConsultaUsuario" ); 
		FabricaConexao obFabricaConexao = null;
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
				tempList=obPersistencia.consultarDescricao( descricao, posicao);
				//stUltimaConsulta=descricao;
				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
				tempList.remove(tempList.size()-1);
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return tempList;   
	}

	public List consultarDescricaoCtpsUf(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
		EstadoNe CtpsUfne = new EstadoNe(); 
		tempList = CtpsUfne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = CtpsUfne.getQuantidadePaginas();
		CtpsUfne = null;
		
		return tempList;
	}

	public List consultarDescricaoNaturalidade(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			CidadeNe Naturalidadene = new CidadeNe(); 
			tempList = Naturalidadene.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Naturalidadene.getQuantidadePaginas();
			Naturalidadene = null;
		
		return tempList;
	}

	public List consultarDescricaoEndereco(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			EnderecoNe Enderecone = new EnderecoNe(); 
			tempList = Enderecone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Enderecone.getQuantidadePaginas();
			Enderecone = null;
		
		return tempList;
	}

	public List consultarDescricaoRgOrgaoExpedidor(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			RgOrgaoExpedidorNe RgOrgaoExpedidorne = new RgOrgaoExpedidorNe(); 
			tempList = RgOrgaoExpedidorne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = RgOrgaoExpedidorne.getQuantidadePaginas();
			RgOrgaoExpedidorne = null;
		
		return tempList;
	}
		
}
