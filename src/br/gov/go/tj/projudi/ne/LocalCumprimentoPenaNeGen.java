package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LocalCumprimentoPenaDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.LocalCumprimentoPenaPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class LocalCumprimentoPenaNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = 4011258861359206413L;
	protected  LocalCumprimentoPenaDt obDados;

	public LocalCumprimentoPenaNeGen() {
		
		obLog = new LogNe(); 

		obDados = new LocalCumprimentoPenaDt(); 

	}


//---------------------------------------------------------
	public void salvar(LocalCumprimentoPenaDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null; 

		//////System.out.println("..neLocalCumprimentoPenasalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			LocalCumprimentoPenaPs obPersistencia = new LocalCumprimentoPenaPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("LocalCumprimentoPena", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("LocalCumprimentoPena", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(LocalCumprimentoPenaDt dados ); 


//---------------------------------------------------------

	public void excluir(LocalCumprimentoPenaDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null; 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			LocalCumprimentoPenaPs obPersistencia = new LocalCumprimentoPenaPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("LocalCumprimentoPena", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public LocalCumprimentoPenaDt consultarId(String id_localcumprimentopena ) throws Exception {

		LocalCumprimentoPenaDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null; 
		//////System.out.println("..ne-ConsultaId_LocalCumprimentoPena" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			LocalCumprimentoPenaPs obPersistencia = new LocalCumprimentoPenaPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_localcumprimentopena ); 
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
		//////System.out.println("..ne-ConsultaLocalCumprimentoPena" ); 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			LocalCumprimentoPenaPs obPersistencia = new LocalCumprimentoPenaPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			if (tempList != null){
				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
				tempList.remove(tempList.size()-1);
			} else QuantidadePaginas = 0;
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
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

}
