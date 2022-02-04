package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.CargoTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.CargoTipoPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class CargoTipoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -8424270721147058242L;
	protected  CargoTipoDt obDados;


	public CargoTipoNeGen() {

		
		obLog = new LogNe(); 

		obDados = new CargoTipoDt(); 

	}


//---------------------------------------------------------
	public void salvar(CargoTipoDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null; 

		//////System.out.println("..neCargoTiposalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			CargoTipoPs obPersistencia = new CargoTipoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("CargoTipo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("CargoTipo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(CargoTipoDt dados ); 


//---------------------------------------------------------

	public void excluir(CargoTipoDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null; 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			CargoTipoPs obPersistencia = new CargoTipoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("CargoTipo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public CargoTipoDt consultarId(String id_cargotipo ) throws Exception {

		CargoTipoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null; 
		//////System.out.println("..ne-ConsultaId_CargoTipo" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CargoTipoPs obPersistencia = new CargoTipoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_cargotipo ); 
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
		//////System.out.println("..ne-ConsultaCargoTipo" ); 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CargoTipoPs obPersistencia = new CargoTipoPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

	public List consultarDescricaoGrupo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
		GrupoNe Grupone = new GrupoNe(); 
		tempList = Grupone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Grupone.getQuantidadePaginas();
		Grupone = null;
		
		return tempList;
	}

	}
