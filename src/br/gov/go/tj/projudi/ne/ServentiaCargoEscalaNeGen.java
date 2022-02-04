package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoEscalaDt;
import br.gov.go.tj.projudi.ps.ServentiaCargoEscalaPs;
import br.gov.go.tj.utils.FabricaConexao;

public abstract class ServentiaCargoEscalaNeGen extends Negocio{


	private static final long serialVersionUID = 6021071218940651182L;
	protected  ServentiaCargoEscalaDt obDados;

	protected List lisGeral = null; 
	public ServentiaCargoEscalaNeGen() {
		
		obLog = new LogNe(); 

		obDados = new ServentiaCargoEscalaDt(); 

	}


//---------------------------------------------------------
	public void salvarMultiplo(ServentiaCargoEscalaDt dados, String[] listaEditada) throws Exception {

		FabricaConexao obFabricaConexao = null;
		LogDt obLogDt;

		//verifico os ids as serem excluidos
		List lisExcluir = new ArrayList();
		//pego a lista geral e procuro os que tem id somente os que tem id podem ser excluidos
		for (int i = 0; i < lisGeral.size(); i++) {
			ServentiaCargoEscalaDt obDt = (ServentiaCargoEscalaDt)lisGeral.get(i);
			boolean boEncontrado =false;
			//se tiver id vejo que o mesmo não esta mais na lista editada
			if (!obDt.getId().equalsIgnoreCase("")){
				//verifico qual id saiu da lista editada
				for(int j=0; j< listaEditada.length; j++)
					if (obDt.getId_Escala().equalsIgnoreCase((String)listaEditada[j])){
						boEncontrado = true;
						break;
					}
			//se o id do objeto não foi encontrado na lista editada coloco o objeto para ser excluido
			if (!boEncontrado) lisExcluir.add(obDt);
			}
		}

		//verifico os ids as serem incluidos
		List lisIncluir = new ArrayList();
		for (int i = 0; i < listaEditada.length; i++)
			for(int j=0; j< lisGeral.size(); j++){
				ServentiaCargoEscalaDt obDt = (ServentiaCargoEscalaDt)lisGeral.get(j);
				if (obDt.getId_Escala().equalsIgnoreCase((String)listaEditada[i]) && obDt.getId().equalsIgnoreCase("")){
					lisIncluir.add(obDt);
					break;
				}
			}

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ServentiaCargoEscalaPs obPersistencia = new ServentiaCargoEscalaPs(obFabricaConexao.getConexao());
			for(int i = 0; i < lisIncluir.size(); i++) {
				ServentiaCargoEscalaDt obDt = (ServentiaCargoEscalaDt)lisIncluir.get(i);				
				obPersistencia.inserir(obDt);
				obLogDt = new LogDt("UsuarioServentiaEscala", obDt.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",obDt.getPropriedades());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			for(int i = 0; i < lisExcluir.size(); i++) {
				ServentiaCargoEscalaDt obDtTemp = (ServentiaCargoEscalaDt)lisExcluir.get(i); 

				obLogDt = new LogDt("UsuarioServentiaEscala", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),obDtTemp.getPropriedades(),"");
				obPersistencia.excluir(obDtTemp.getId());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

	public String consultarEscalaUsuarioServentiaUlLiCheckBox(String id_serventiacargo ) throws Exception {

		FabricaConexao obFabricaConexao = null;
		lisGeral=null;
		String tempUlLi="<ul>";

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				ServentiaCargoEscalaPs obPersistencia = new ServentiaCargoEscalaPs(obFabricaConexao.getConexao());
				lisGeral=obPersistencia.consultarEscalaServentiaCargoGeral(id_serventiacargo);
				for(int i = 0; i < lisGeral.size(); i++) {
					ServentiaCargoEscalaDt obDtTemp = (ServentiaCargoEscalaDt)lisGeral.get(i); 

					tempUlLi+= obDtTemp.getListaLiCheckBox();
				}
				tempUlLi+="</ul>";
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return tempUlLi;   
	}

	public void salvar(ServentiaCargoEscalaDt dados ) throws Exception {

		FabricaConexao obFabricaConexao = null;
		LogDt obLogDt;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ServentiaCargoEscalaPs obPersistencia = new ServentiaCargoEscalaPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("UsuarioServentiaEscala", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("UsuarioServentiaEscala", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


	public abstract String Verificar(ServentiaCargoEscalaDt dados ); 

	public void excluir(ServentiaCargoEscalaDt dados) throws Exception {

		FabricaConexao obFabricaConexao = null;
		LogDt obLogDt;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ServentiaCargoEscalaPs obPersistencia = new ServentiaCargoEscalaPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("UsuarioServentiaEscala", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

	public ServentiaCargoEscalaDt consultarId(String id_serventiacargoescala ) throws Exception {

		FabricaConexao obFabricaConexao = null;
		ServentiaCargoEscalaDt dtRetorno=null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaCargoEscalaPs obPersistencia = new ServentiaCargoEscalaPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_serventiacargoescala ); 
			obDados.copiar(dtRetorno);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	public List consultarDescricao(String descricao, String posicao ) throws Exception {
		FabricaConexao obFabricaConexao = null;
		List tempList=null;

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				ServentiaCargoEscalaPs obPersistencia = new ServentiaCargoEscalaPs(obFabricaConexao.getConexao());
				tempList=obPersistencia.consultarDescricao( descricao, posicao);
				//stUltimaConsulta=descricao;
				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
				tempList.remove(tempList.size()-1);
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return tempList;   
	}

	public List consultarDescricaoUsuarioServentia(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
		UsuarioServentiaNe UsuarioServentiane = new UsuarioServentiaNe(); 
		tempList = UsuarioServentiane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = UsuarioServentiane.getQuantidadePaginas();
		UsuarioServentiane = null;
		
		return tempList;
	}

	public List consultarDescricaoEscala(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			EscalaNe Escalane = new EscalaNe(); 
			tempList = Escalane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Escalane.getQuantidadePaginas();
			Escalane = null;
		
		return tempList;
	}

	}
