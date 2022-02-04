package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ComarcaCidadeDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.ComarcaCidadePs;
import br.gov.go.tj.utils.FabricaConexao;

public class ComarcaCidadeNe extends ComarcaCidadeNeGen{

    private static final long serialVersionUID = 8071767638465482804L;

	public  String Verificar(ComarcaCidadeDt dados ) {

		String stRetorno="";

		if (dados.getId_Comarca().length()==0)
			stRetorno += "O Campo Id_Comarca é obrigatório.";
		return stRetorno;

	}
	
	public String consultarDescricaoComarcaJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		ComarcaNe Comarcane = new ComarcaNe(); 
		stTemp = Comarcane.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		return stTemp;
	}

	public void salvarMultiplo(ComarcaCidadeDt dados, String[] listaEditada) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null; 

		//verifico os ids as serem excluidos
		List lisExcluir = new ArrayList();
		//pego a lista geral e procuro os que tem id
		//somente os que tem id podem ser excluidos
		for (int i = 0; i < lisGeral.size(); i++) {
			ComarcaCidadeDt obDt = (ComarcaCidadeDt)lisGeral.get(i);
			boolean boEncontrado =false;
			//se tiver id vejo que o mesmo não esta mais na lista editada
			if (!obDt.getId().equalsIgnoreCase("")){
				//verifica se a listaEditada é nula, se for é pq ficou vazia na tela e não precisa prosseguir
				if(listaEditada != null && listaEditada.length > 0) {
					//verifico qual id saiu da lista editada
					for(int j=0; j< listaEditada.length; j++) {
						if (obDt.getId_Cidade().equalsIgnoreCase((String)listaEditada[j])){
							boEncontrado = true;
							break;
						}
					}
				}
				//se o id do objeto não foi encontrado na lista editada coloco o objeto para ser excluido
				if (!boEncontrado) {
					lisExcluir.add(obDt);
				}
			}
		}

		//verifico os ids as serem incluidos
		List lisIncluir = new ArrayList();
		//verifica se a listaEditada é nula, se for é pq ficou vazia na tela e não precisa prosseguir
		if(listaEditada != null && listaEditada.length > 0) {
			for (int i = 0; i < listaEditada.length; i++) {
				for(int j=0; j< lisGeral.size(); j++){
					ComarcaCidadeDt obDt = (ComarcaCidadeDt)lisGeral.get(j);
					if (obDt.getId_Cidade().equalsIgnoreCase((String)listaEditada[i]) && obDt.getId().equalsIgnoreCase("")){
						lisIncluir.add(obDt);
						break;
					}
				}
			}
		}

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ComarcaCidadePs obPersistencia = new ComarcaCidadePs(obFabricaConexao.getConexao());
			for(int i = 0; i < lisIncluir.size(); i++) {
				ComarcaCidadeDt obDt = (ComarcaCidadeDt)lisIncluir.get(i);
				obPersistencia.inserir(obDt);
				obLogDt = new LogDt("ComarcaCidade", obDt.getId() , dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",obDt.getPropriedades());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			for(int i = 0; i < lisExcluir.size(); i++) {
				ComarcaCidadeDt obDtTemp = (ComarcaCidadeDt)lisExcluir.get(i); 

				obLogDt = new LogDt("ComarcaCidade", obDtTemp.getId() , dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),obDtTemp.getPropriedades(),"");
				obPersistencia.excluir(obDtTemp.getId());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public void incluirMultiplo(ComarcaCidadeDt dados, String[] listaEditada) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		// verifico os ids as serem incluidos
		List lisIncluir = new ArrayList();
		// verifica se a listaEditada é nula, se for é pq ficou vazia na tela e não
		// precisa prosseguir
		if (listaEditada != null && listaEditada.length > 0) {
			for (int i = 0; i < listaEditada.length; i++) {
				for (int j = 0; j < lisGeral.size(); j++) {
					ComarcaCidadeDt obDt = (ComarcaCidadeDt) lisGeral.get(j);
					if (obDt.getId_Cidade().equalsIgnoreCase((String) listaEditada[i])
							&& obDt.getId().equalsIgnoreCase("")) {
						lisIncluir.add(obDt);
						break;
					}
				}
			}

		}
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ComarcaCidadePs obPersistencia = new ComarcaCidadePs(obFabricaConexao.getConexao());
			for (int i = 0; i < lisIncluir.size(); i++) {
				ComarcaCidadeDt obDt = (ComarcaCidadeDt) lisIncluir.get(i);
				obPersistencia.inserir(obDt);
				obLogDt = new LogDt("ComarcaCidade", dados.getId(), dados.getId_UsuarioLog(),
						dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", obDt.getPropriedades());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			obFabricaConexao.finalizarTransacao();

		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	public void excluir(ComarcaCidadeDt dados, String id) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		// verifico os ids as serem excluidos
		List lisExcluir = new ArrayList();
		// pego a lista geral e procuro os que tem id
		// somente os que tem id podem ser excluidos
		for (int i = 0; i < lisGeral.size(); i++) {
			ComarcaCidadeDt obDt = (ComarcaCidadeDt) lisGeral.get(i);
			// se tiver id vejo que o mesmo não esta mais na lista editada
			if (!obDt.getId().equalsIgnoreCase("")) {
				if (obDt.getId_Cidade().equalsIgnoreCase(id)) {
					lisExcluir.add(obDt);
					break;
				}
			}
		}

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ComarcaCidadePs obPersistencia = new ComarcaCidadePs(obFabricaConexao.getConexao());

			for (int i = 0; i < lisExcluir.size(); i++) {
				ComarcaCidadeDt obDtTemp = (ComarcaCidadeDt) lisExcluir.get(i);

				obLogDt = new LogDt("ComarcaCidad", dados.getId(), dados.getId_UsuarioLog(),
						dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir), obDtTemp.getPropriedades(), "");
				obPersistencia.excluir(obDtTemp.getId());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			obFabricaConexao.finalizarTransacao();

		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	public List consultarComarca(String idComarca) throws Exception {

		lisGeral = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ComarcaCidadePs obPersistencia = new ComarcaCidadePs(obFabricaConexao.getConexao());
			lisGeral = obPersistencia.consultarCidadeComarcaGeral(idComarca);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return lisGeral;
	}
	
	public List consultarComarca(String idComarca, FabricaConexao obFabricaConexao) throws Exception {
		ComarcaCidadePs obPersistencia = new ComarcaCidadePs(obFabricaConexao.getConexao());
		return obPersistencia.consultarCidadeComarcaGeral(idComarca);	
	}
	
	public ComarcaCidadeDt consultarIdCidade(String id_cidade) throws Exception {

		ComarcaCidadeDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null; 

		try{ 
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			ComarcaCidadePs obPersistencia = new ComarcaCidadePs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarIdCidade(id_cidade ); 
			obDados.copiar(dtRetorno);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	public ComarcaCidadeDt consultarIdCidade(String id_cidade, FabricaConexao obFabricaConexao ) throws Exception {
		ComarcaCidadePs obPersistencia = new ComarcaCidadePs(obFabricaConexao.getConexao());
		return obPersistencia.consultarIdCidade(id_cidade );
	}
	
}
