package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.PonteiroCejuscDt;
import br.gov.go.tj.projudi.ps.PonteiroCejuscPs;
import br.gov.go.tj.utils.FabricaConexao;

public abstract class PonteiroCejuscNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -620007052259222949L;
	protected  PonteiroCejuscDt obDados;


	public PonteiroCejuscNeGen() {

		obLog = new LogNe(); 

		obDados = new PonteiroCejuscDt(); 

	}

	public void salvar(PonteiroCejuscDt dados ) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			PonteiroCejuscPs obPersistencia = new  PonteiroCejuscPs(obFabricaConexao.getConexao()); 

			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("PonteiroCejusc",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("PonteiroCejusc",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		}catch(Exception e){ 
			obFabricaConexao.cancelarTransacao();
			System.out.println("..ne-salvar"+ e.getMessage()); 
			throw new Exception(" <{Erro:.....}> PonteiroCejuscNeGen.salvar() " + e.getMessage() );
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

	public abstract String Verificar(PonteiroCejuscDt dados ); 

	public void excluir(PonteiroCejuscDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			PonteiroCejuscPs obPersistencia = new  PonteiroCejuscPs(obFabricaConexao.getConexao()); 

			obLogDt = new LogDt("PonteiroCejusc",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
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
			PonteiroCejuscPs obPersistencia = new  PonteiroCejuscPs(obFabricaConexao.getConexao()); 

				stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return stTemp;
	}

	public PonteiroCejuscDt consultarId(String id_ponteirocejusc ) throws Exception {

		PonteiroCejuscDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;


		try{ 
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			PonteiroCejuscPs obPersistencia = new  PonteiroCejuscPs(obFabricaConexao.getConexao()); 

			dtRetorno= obPersistencia.consultarId(id_ponteirocejusc ); 
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

			PonteiroCejuscPs obPersistencia = new  PonteiroCejuscPs(obFabricaConexao.getConexao()); 

				tempList=obPersistencia.consultarDescricao( descricao, posicao);
				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
				tempList.remove(tempList.size()-1);
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return tempList;   
	}

	public List consultarDescricaoUsuarioCejusc(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		List tempList=null;
			UsuarioCejuscNe UsuarioCejuscne = new UsuarioCejuscNe(); 
			tempList = UsuarioCejuscne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = UsuarioCejuscne.getQuantidadePaginas();
			UsuarioCejuscne = null;
		return tempList;
	}

	public String consultarDescricaoUsuarioCejuscJSON(String descricao, String PosicaoPaginaAtual ) throws Exception { 
		String stTemp = (new UsuarioCejuscNe()).consultarDescricaoJSON(descricao, PosicaoPaginaAtual);
		return stTemp;
	}

	public List consultarDescricaoServentia(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		List tempList=null;
			ServentiaNe Serventiane = new ServentiaNe(); 
			tempList = Serventiane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Serventiane.getQuantidadePaginas();
			Serventiane = null;
		return tempList;
	}

	public String consultarDescricaoServentiaJSON(String descricao, String PosicaoPaginaAtual ) throws Exception { 
		String stTemp = (new ServentiaNe()).consultarDescricaoJSON(descricao, PosicaoPaginaAtual);
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

	public List consultarDescricaoServentiaCargo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		List tempList=null;
			ServentiaCargoNe ServentiaCargone = new ServentiaCargoNe(); 
			tempList = ServentiaCargone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = ServentiaCargone.getQuantidadePaginas();
			ServentiaCargone = null;
		return tempList;
	}

	public String consultarDescricaoServentiaCargoJSON(String descricao, String idServentia, String PosicaoPaginaAtual ) throws Exception { 
		return (new ServentiaCargoNe()).consultarServentiaCargosJSON(descricao, idServentia, PosicaoPaginaAtual);
	}

	public List consultarDescricaoPonteiroCejusc(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		List tempList=null;
			PonteiroCejuscNe PonteiroCejuscne = new PonteiroCejuscNe(); 
			tempList = PonteiroCejuscne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = PonteiroCejuscne.getQuantidadePaginas();
			PonteiroCejuscne = null;
		return tempList;
	}

	public String consultarDescricaoPonteiroCejuscJSON(String descricao, String PosicaoPaginaAtual ) throws Exception { 
		String stTemp = (new PonteiroCejuscNe()).consultarDescricaoJSON(descricao, PosicaoPaginaAtual);
		return stTemp;
	}

	}
