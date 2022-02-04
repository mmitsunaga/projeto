package br.gov.go.tj.projudi.ne;


import java.util.Date;
import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.RelatorioZonaHistoricoDt;
import br.gov.go.tj.projudi.dt.ZonaDt;
import br.gov.go.tj.projudi.dt.ZonaHistoricoDt;
import br.gov.go.tj.projudi.ps.ZonaPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;

public class ZonaNe extends ZonaNeGen{


    private static final long serialVersionUID = -6493712358787900948L;

	public  String Verificar(ZonaDt dados ) {
		String stRetorno="";
		
		if (dados.getZona().equalsIgnoreCase("")){
			stRetorno += "Campo Zona é obrigatório.";
		}
		if (dados.getZonaCodigo().equalsIgnoreCase("")){
			stRetorno += "Campo Código é obrigatório.";
		}
		if (dados.getId_Comarca().equalsIgnoreCase("")) {
			stRetorno += "Comarca é é obrigatório.";
		}	
		if (dados.getValorCivel().equalsIgnoreCase("")){
			stRetorno += "Campo Valor Cível é obrigatório.";
		}
		if (dados.getValorCriminal().equalsIgnoreCase("")){
			stRetorno += "Campo Valor Criminal é obrigatório.";
		}
		if (dados.getValorContaVinculada().equalsIgnoreCase("")){
			stRetorno += "Campo Valor Conta Vinculada é obrigatório.";
		}
		if (dados.getValorSegundoGrau().equalsIgnoreCase("")){
			stRetorno += "Campo Valor Segundo Grau é obrigatório.";
		}		
		if (dados.getValorSegundoGrauContadoria().equalsIgnoreCase("")){
			stRetorno += "Campo Valor Segundo Grau Contadoria é obrigatório.";
		}
		return stRetorno;

	}
	
	public void salvar(ZonaDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ZonaPs obPersistencia = new ZonaPs(obFabricaConexao.getConexao());
			if (dados.getId().length()==0) {
				dados.setDataInicio(Funcoes.DataHora(new Date()));
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("Zona",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				//Se o houve gravação do histório, teremos que gerar uma nova data de início para vigência dos valores.
				if (new ZonaHistoricoNe().salvar(dados, obFabricaConexao)) {
					dados.setDataInicio(Funcoes.DataHora(new Date()));
				}
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("Zona",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		}catch(Exception e){ 
			obFabricaConexao.cancelarTransacao();
			throw e;
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public void excluir(ZonaDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ZonaPs obPersistencia = new ZonaPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("Zona",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
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
	
	public ZonaDt consultarId(String id_zona ) throws Exception {

		ZonaDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		
		try{ 
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			dtRetorno = this.consultarId(id_zona, obFabricaConexao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	public ZonaDt consultarId(String id_zona, FabricaConexao obFabricaConexao) throws Exception {

		ZonaDt dtRetorno=null;
		ZonaPs obPersistencia = new ZonaPs(obFabricaConexao.getConexao());
		dtRetorno= obPersistencia.consultarId(id_zona ); 
		obDados.copiar(dtRetorno);
		return dtRetorno;
	}
	
	public String consultarDescricaoComarcaJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		String stTemp = "";
		ComarcaNe Comarcane = new ComarcaNe(); 
		stTemp = Comarcane.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		return stTemp;
	}
	
	public String consultarDescricaoJSON(String descricao, String comarca, String posicao) throws Exception { 
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ZonaPs obPersistencia = new  ZonaPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, comarca, posicao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}
	
	public RelatorioZonaHistoricoDt consultarHistoricos(String idZona) throws Exception { 
		RelatorioZonaHistoricoDt relatorio = new RelatorioZonaHistoricoDt();
		ZonaHistoricoNe zonaHistoricoNe = new ZonaHistoricoNe();
		
		List<ZonaHistoricoDt> listaDeHistoricos = zonaHistoricoNe.consultar(idZona);
		
		relatorio.setListaZonaHistorico(listaDeHistoricos);
		return relatorio;
	}

//	public List consultarDescricao(String descricaoZona, String descricaoCidade, String posicao ) throws Exception { 
//		List tempList=null;
//		FabricaConexao obFabricaConexao = null;
//		
//			try{
//				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
//				ZonaPs obPersistencia = new ZonaPs(obFabricaConexao.getConexao());
//				if( descricaoCidade != null && descricaoCidade.length() > 0 )
//					tempList=obPersistencia.consultarDescricao( descricaoZona, descricaoCidade, posicao);
//				else
//					tempList=obPersistencia.consultarDescricao( descricaoZona, posicao);
//				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
//				tempList.remove(tempList.size()-1);
//			} finally {
//				obFabricaConexao.fecharConexao();
//			}
//		return tempList;   
//	}
//	
//	public String consultarDescricaoCidadeJSON(String tempNomeBusca, String uf, String PosicaoPaginaAtual) throws Exception {
//		String stTemp = "";
//	CidadeNe Naturalidadene = new CidadeNe();
//	stTemp = Naturalidadene.consultarDescricaoJSON(tempNomeBusca, uf, PosicaoPaginaAtual);
//		return stTemp;
//	}


//	
//	public long getQuantidadePaginas(){
//		return QuantidadePaginas;
//	}
//	
//	public List consultarDescricao(String descricao, String posicao ) throws Exception { 
//		List tempList=null;
//		FabricaConexao obFabricaConexao = null;
//		
//			try{
//				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
//				ZonaPs obPersistencia = new ZonaPs(obFabricaConexao.getConexao());
//				tempList=obPersistencia.consultarDescricao( descricao, posicao);
//				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
//				tempList.remove(tempList.size()-1);
//			} finally {
//				obFabricaConexao.fecharConexao();
//			}
//		return tempList;   
//	}
//
//	public List consultarDescricaoCidade(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
//		List tempList=null;
//	CidadeNe Cidadene = new CidadeNe(); 
//	tempList = Cidadene.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
//	QuantidadePaginas = Cidadene.getQuantidadePaginas();
//	Cidadene = null;
//		return tempList;
//	}
}
