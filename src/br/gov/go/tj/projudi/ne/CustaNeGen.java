package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.CustaDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.CustaPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class CustaNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -4921905203568102756L;
	
	protected  CustaDt obDados;


	public CustaNeGen() {		

		obLog = new LogNe(); 

		obDados = new CustaDt(); 

	}


//---------------------------------------------------------
	public void salvar(CustaDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			CustaPs obPersistencia = new CustaPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("Custa",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("Custa",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(CustaDt dados ); 


//---------------------------------------------------------

	public void excluir(CustaDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			CustaPs obPersistencia = new CustaPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("Custa",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public CustaDt consultarId(String id_custa ) throws Exception {

		CustaDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CustaPs obPersistencia = new CustaPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_custa ); 
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

			CustaPs obPersistencia = new CustaPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		} finally { 
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

	public List consultarDescricaoArrecadacaoCusta(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		ArrecadacaoCustaNe ArrecadacaoCustane = new ArrecadacaoCustaNe(); 
		tempList = ArrecadacaoCustane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = ArrecadacaoCustane.getQuantidadePaginas();
		ArrecadacaoCustane = null;
		return tempList;
	}
}
