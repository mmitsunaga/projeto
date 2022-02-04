package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.EnderecoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.EnderecoPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class EnderecoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -7423664644265349888L;
		
	
	protected  EnderecoDt obDados;
		 

	public EnderecoNeGen() {
		

		obLog = new LogNe(); 

		obDados = new EnderecoDt(); 

	}


//---------------------------------------------------------
	 public abstract String Verificar(EnderecoDt dados ); 


//---------------------------------------------------------

	public void excluir(EnderecoDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			EnderecoPs obPersistencia = new EnderecoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("Endereco", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public EnderecoDt consultarId(String id_endereco ) throws Exception {

		EnderecoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..ne-ConsultaId_Endereco" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			EnderecoPs obPersistencia = new EnderecoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_endereco ); 
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
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..ne-ConsultaEndereco" ); 

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				EnderecoPs obPersistencia = new EnderecoPs(obFabricaConexao.getConexao());
				tempList=obPersistencia.consultarDescricao( descricao, posicao);
				//stUltimaConsulta=descricao;
				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
				tempList.remove(tempList.size()-1);
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return tempList;   
	}

	public List consultarDescricaoBairro(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
		BairroNe Bairrone = new BairroNe(); 
		tempList = Bairrone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Bairrone.getQuantidadePaginas();
		Bairrone = null;
		
		return tempList;
	}

	public List consultarDescricaoCidade(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			CidadeNe Cidadene = new CidadeNe(); 
			tempList = Cidadene.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Cidadene.getQuantidadePaginas();
			Cidadene = null;
		
		return tempList;
	}

	}
