package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.AlcunhaDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.AlcunhaPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public abstract class AlcunhaNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -4656845526047016789L;
	protected  AlcunhaDt obDados;


	public AlcunhaNeGen() {
	
		obLog = new LogNe(); 

		obDados = new AlcunhaDt(); 

	}


//---------------------------------------------------------
	public void salvar(AlcunhaDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null; 
		
		//////System.out.println("..neAlcunhasalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			AlcunhaPs obPersistencia = new AlcunhaPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("Alcunha", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("Alcunha", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(AlcunhaDt dados ); 


//---------------------------------------------------------

	public void excluir(AlcunhaDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			AlcunhaPs obPersistencia = new AlcunhaPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("Alcunha", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public AlcunhaDt consultarId(String id_alcunha ) throws Exception {

		AlcunhaDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..ne-ConsultaId_Alcunha" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AlcunhaPs obPersistencia = new AlcunhaPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_alcunha ); 
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
		//////System.out.println("..ne-ConsultaAlcunha" ); 

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				AlcunhaPs obPersistencia = new AlcunhaPs(obFabricaConexao.getConexao());
				tempList=obPersistencia.consultarDescricao( descricao, posicao);
				if (tempList != null) {
					QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
					tempList.remove(tempList.size()-1);
				}
				else QuantidadePaginas = 0;
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return tempList;   
	}

	}
