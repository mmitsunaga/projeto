package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.PonteiroLogCompensarDt;
import br.gov.go.tj.projudi.ps.PonteiroLogCompensarPs;
import br.gov.go.tj.utils.FabricaConexao;

public abstract class PonteiroLogCompensarNeGen extends Negocio{

	private static final long serialVersionUID = -8305166864023847613L;
	protected  PonteiroLogCompensarDt obDados;


	public PonteiroLogCompensarNeGen() {
		obLog = new LogNe(); 
		obDados = new PonteiroLogCompensarDt(); 
	}

	public void salvar(PonteiroLogCompensarDt dados ) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			PonteiroLogCompensarPs obPersistencia = new PonteiroLogCompensarPs(obFabricaConexao.getConexao()); 

			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("PonteiroLogCompensar",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("PonteiroLogCompensar",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
 			obFabricaConexao.finalizarTransacao();

		}catch(Exception e){ 
			obFabricaConexao.cancelarTransacao();
			System.out.println("..ne-salvar"+ e.getMessage()); 
			throw new Exception(" <{Erro:.....}> PonteiroLogCompensarNeGen.salvar() " + e.getMessage() );
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

	public void excluir(PonteiroLogCompensarDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			PonteiroLogCompensarPs obPersistencia = new PonteiroLogCompensarPs(obFabricaConexao.getConexao()); 

			obLogDt = new LogDt("PonteiroLogCompensar",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		}catch(Exception e){ 
			obFabricaConexao.cancelarTransacao();
			throw e;
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


 /**
  * Método para lista as area processuais
  * @author jrcorrea
  */
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";
			try {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			PonteiroLogCompensarPs obPersistencia = new PonteiroLogCompensarPs(obFabricaConexao.getConexao()); 

				stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return stTemp;
	}

	public PonteiroLogCompensarDt consultarId(String id_ponteirologcompensar ) throws Exception {
		PonteiroLogCompensarDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		try{ 
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			PonteiroLogCompensarPs obPersistencia = new PonteiroLogCompensarPs(obFabricaConexao.getConexao()); 

			dtRetorno= obPersistencia.consultarId(id_ponteirologcompensar ); 
			obDados.copiar(dtRetorno);
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}


	public long getQuantidadePaginas(){
		return QuantidadePaginas;
	}

	public List consultarDescricao(String descricao, String posicao ) throws Exception { 
		List tempList=null;
		FabricaConexao obFabricaConexao = null;

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

			PonteiroLogCompensarPs obPersistencia = new PonteiroLogCompensarPs(obFabricaConexao.getConexao()); 

				tempList=obPersistencia.consultarDescricao( descricao, posicao);
				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
				tempList.remove(tempList.size()-1);
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return tempList;   
	}

	public List consultarDescricaoServentia(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		List tempList=null;
			ServentiaNe Serventiane = new ServentiaNe(); 
			tempList = Serventiane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Serventiane.getQuantidadePaginas();
			Serventiane = null;
		return tempList;
	}

	public List consultarDescricaoServentiaCargo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		List tempList=null;
			ServentiaCargoNe ServentiaCargone = new ServentiaCargoNe(); 
			tempList = ServentiaCargone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = ServentiaCargone.getQuantidadePaginas();
			ServentiaCargone = null;
		return tempList;
	}

	public List consultarDescricaoAreaDistribuicao(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		List tempList=null;
			AreaDistribuicaoNe AreaDistribuicaone = new AreaDistribuicaoNe(); 
			tempList = AreaDistribuicaone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = AreaDistribuicaone.getQuantidadePaginas();
			AreaDistribuicaone = null;
		return tempList;
	}

	public String consultarDescricaoAreaDistribuicaoJSON(String descricao, String PosicaoPaginaAtual ) throws Exception { 
		String stTemp = (new AreaDistribuicaoNe()).consultarDescricaoJSON(descricao, PosicaoPaginaAtual);
		return stTemp;
	}

	public String consultarServentiasAtivasAreaDistribuicaoJSON(String descricao, String idAreaDistribuicao, String PosicaoPaginaAtual ) throws Exception { 
		String stTemp = (new ServentiaNe()).consultarServentiasAtivasAreaDistribuicaoJSON(descricao, idAreaDistribuicao, PosicaoPaginaAtual);
		return stTemp;
	}

	public String consultarDescricaoUsuarioServentiaJSON(String descricao, String PosicaoPaginaAtual ) throws Exception { 
		String stTemp = (new UsuarioServentiaNe()).consultarDescricaoJSON(descricao, PosicaoPaginaAtual);
		return stTemp;
	}

	public List consultarDescricaoUsuarioServentia(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		List tempList=null;
			UsuarioServentiaNe UsuarioServentiane = new UsuarioServentiaNe(); 
			tempList = UsuarioServentiane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = UsuarioServentiane.getQuantidadePaginas();
			UsuarioServentiane = null;
		return tempList;
	}

}
