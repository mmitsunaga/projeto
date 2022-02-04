package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.ArquivoPalavraDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.ArquivoPalavraPs;
import br.gov.go.tj.utils.FabricaConexao;
 
//---------------------------------------------------------
public abstract class ArquivoPalavraNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = 7089963621129276621L;	
	protected  ArquivoPalavraDt obDados;


	public ArquivoPalavraNeGen() {
		

		obLog = new LogNe(); 

		obDados = new ArquivoPalavraDt(); 

	}


//---------------------------------------------------------
	public void salvar(ArquivoPalavraDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		//////System.out.println("..neArquivoPalavrasalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ArquivoPalavraPs obPersistencia = new ArquivoPalavraPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ArquivoPalavra", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("ArquivoPalavra", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(ArquivoPalavraDt dados ); 


//---------------------------------------------------------

	public void excluir(ArquivoPalavraDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ArquivoPalavraPs obPersistencia = new ArquivoPalavraPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("ArquivoPalavra", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public ArquivoPalavraDt consultarId(String id_arquivopalavra ) throws Exception {

		ArquivoPalavraDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..ne-ConsultaId_ArquivoPalavra" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ArquivoPalavraPs obPersistencia = new ArquivoPalavraPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_arquivopalavra ); 
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
		//////System.out.println("..ne-ConsultaArquivoPalavra" ); 

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				ArquivoPalavraPs obPersistencia = new ArquivoPalavraPs(obFabricaConexao.getConexao());
				tempList=obPersistencia.consultarDescricao( descricao, posicao);
				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
				tempList.remove(tempList.size()-1);
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return tempList;   
	}

	public List consultarDescricaoArquivos(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
		ArquivoNe Arquivone = new ArquivoNe(); 
		tempList = Arquivone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Arquivone.getQuantidadePaginas();
		Arquivone = null;
		
		return tempList;
	}

	public List consultarDescricaoPalavra1(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			PalavraNe Palavra1ne = new PalavraNe(); 
			tempList = Palavra1ne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Palavra1ne.getQuantidadePaginas();
			Palavra1ne = null;
		
		return tempList;
	}

	public List consultarDescricaoPalavra2(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			PalavraNe Palavra2ne = new PalavraNe(); 
			tempList = Palavra2ne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Palavra2ne.getQuantidadePaginas();
			Palavra2ne = null;
		
		return tempList;
	}

}
