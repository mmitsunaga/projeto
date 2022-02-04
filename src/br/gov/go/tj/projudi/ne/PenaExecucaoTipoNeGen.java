package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.PenaExecucaoTipoDt;
import br.gov.go.tj.projudi.ps.PenaExecucaoTipoPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public abstract class PenaExecucaoTipoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = 7110066261503805319L;	
	protected  PenaExecucaoTipoDt obDados;


	public PenaExecucaoTipoNeGen() {
		
		obLog = new LogNe(); 

		obDados = new PenaExecucaoTipoDt(); 

	}


//---------------------------------------------------------
	public void salvar(PenaExecucaoTipoDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		//////System.out.println("..nePenaExecucaoTiposalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			PenaExecucaoTipoPs obPersistencia = new PenaExecucaoTipoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("PenaExecucaoTipo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());				
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("PenaExecucaoTipo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(PenaExecucaoTipoDt dados ); 


//---------------------------------------------------------

	public void excluir(PenaExecucaoTipoDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			PenaExecucaoTipoPs obPersistencia = new PenaExecucaoTipoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("PenaExecucaoTipo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public PenaExecucaoTipoDt consultarId(String id_penaexecucaotipo ) throws Exception {

		PenaExecucaoTipoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..ne-ConsultaId_PenaExecucaoTipo" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PenaExecucaoTipoPs obPersistencia = new PenaExecucaoTipoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_penaexecucaotipo ); 
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
		//////System.out.println("..ne-ConsultaPenaExecucaoTipo" ); 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PenaExecucaoTipoPs obPersistencia = new PenaExecucaoTipoPs(obFabricaConexao.getConexao());
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

	}
