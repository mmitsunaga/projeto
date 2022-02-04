package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ZonaDt;
import br.gov.go.tj.projudi.ps.ZonaPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class ZonaNeGen extends Negocio{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5160920981910458473L;
	
	protected  ZonaDt obDados;


	public ZonaNeGen() {		

		obLog = new LogNe(); 

		obDados = new ZonaDt(); 

	}


//---------------------------------------------------------
	public void salvar(ZonaDt dados ) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ZonaPs obPersistencia = new ZonaPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("Zona",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("Zona",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(ZonaDt dados ); 


//---------------------------------------------------------

	public void excluir(ZonaDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ZonaPs obPersistencia = new ZonaPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("Zona",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public ZonaDt consultarId(String id_zona ) throws Exception {

		ZonaDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ZonaPs obPersistencia = new ZonaPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_zona ); 
			obDados.copiar(dtRetorno);
		} finally {
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
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ZonaPs obPersistencia = new ZonaPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		} finally {
			obFabricaConexao.fecharConexao();
		}
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
