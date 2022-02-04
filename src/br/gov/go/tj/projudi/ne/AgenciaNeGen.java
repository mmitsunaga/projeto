package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.AgenciaDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.AgenciaPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class AgenciaNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1537201521040897623L;
	
	protected  AgenciaDt obDados;


	public AgenciaNeGen() {
		
		obLog = new LogNe(); 

		obDados = new AgenciaDt(); 

	}


//---------------------------------------------------------
	public void salvar(AgenciaDt dados ) throws Exception{

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null; 
		
		//////System.out.println("..neAgenciasalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			AgenciaPs obPersistencia = new AgenciaPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("Agencia", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados);
                obLogDt = new LogDt("Agencia", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(AgenciaDt dados ); 


//---------------------------------------------------------

	public void excluir(AgenciaDt dados) throws Exception{

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			AgenciaPs obPersistencia = new AgenciaPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("Agencia", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public AgenciaDt consultarId(String id_agencia ) throws Exception{

		AgenciaDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..ne-ConsultaId_Agencia" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AgenciaPs obPersistencia = new AgenciaPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_agencia ); 
			obDados.copiar(dtRetorno);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

//---------------------------------------------------------

	public long getQuantidadePaginas() {
		return QuantidadePaginas;
	}
//---------------------------------------------------------

	public List consultarDescricao(String descricao, String posicao ) throws Exception{
		List tempList=null;
		FabricaConexao obFabricaConexao = null; 

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				AgenciaPs obPersistencia = new AgenciaPs(obFabricaConexao.getConexao());
				tempList=obPersistencia.consultarDescricao( descricao, posicao);
				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
				tempList.remove(tempList.size()-1);
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return tempList;   
	}

	public List consultarDescricaoBanco(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception{
		List tempList=null;
		
			BancoNe Bancone = new BancoNe(); 
			tempList = Bancone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Bancone.getQuantidadePaginas();
			Bancone = null;
		
		return tempList;
	}

}
