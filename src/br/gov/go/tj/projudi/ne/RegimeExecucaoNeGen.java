package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.RegimeExecucaoDt;
import br.gov.go.tj.projudi.ps.RegimeExecucaoPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class RegimeExecucaoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = 2605072346951573071L;	
	protected  RegimeExecucaoDt obDados;


	public RegimeExecucaoNeGen() {		

		obLog = new LogNe(); 

		obDados = new RegimeExecucaoDt(); 

	}


//---------------------------------------------------------
	public void salvar(RegimeExecucaoDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao =null;

		//////System.out.println("..neRegimeExecucaosalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			RegimeExecucaoPs obPersistencia = new RegimeExecucaoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("RegimeExecucao", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("RegimeExecucao", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(RegimeExecucaoDt dados ); 


//---------------------------------------------------------

	public void excluir(RegimeExecucaoDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao =null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			RegimeExecucaoPs obPersistencia = new RegimeExecucaoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("RegimeExecucao", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public RegimeExecucaoDt consultarId(String id_regimeexecucao ) throws Exception {

		RegimeExecucaoDt dtRetorno=null;
		FabricaConexao obFabricaConexao =null;
		//////System.out.println("..ne-ConsultaId_RegimeExecucao" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			RegimeExecucaoPs obPersistencia = new RegimeExecucaoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_regimeexecucao ); 
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
		FabricaConexao obFabricaConexao =null;
		//////System.out.println("..ne-ConsultaRegimeExecucao" ); 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			RegimeExecucaoPs obPersistencia = new RegimeExecucaoPs(obFabricaConexao.getConexao());
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

	public List consultarDescricaoPenaExecucaoTipo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
		PenaExecucaoTipoNe PenaExecucaoTipone = new PenaExecucaoTipoNe(); 
		tempList = PenaExecucaoTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = PenaExecucaoTipone.getQuantidadePaginas();
		PenaExecucaoTipone = null;
		
		return tempList;
	}

}
