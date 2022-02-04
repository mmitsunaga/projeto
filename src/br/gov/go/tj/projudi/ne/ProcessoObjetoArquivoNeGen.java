package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.EnderecoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoObjetoArquivoDt;
import br.gov.go.tj.projudi.ps.ProcessoObjetoArquivoPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class ProcessoObjetoArquivoNeGen extends Negocio{


	protected  ProcessoObjetoArquivoDt obDados;


	public ProcessoObjetoArquivoNeGen() {

		obLog = new LogNe(); 

		obDados = new ProcessoObjetoArquivoDt(); 

	}


//---------------------------------------------------------
	public void salvar(ProcessoObjetoArquivoDt dados ) throws Exception {

		LogDt obLogDt;
		EnderecoDt enderecoDt = new EnderecoDt();
		EnderecoNe enderecoNe = new EnderecoNe();
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {
			obFabricaConexao.iniciarTransacao();
			ProcessoObjetoArquivoPs obPersistencia = new ProcessoObjetoArquivoPs(obFabricaConexao.getConexao()); 
			
			//Se tiver ID significa que é alteração de dados e não necessariamente terá endereço a ser preenchido
			if(dados.getId().length() > 0 && 
					dados.getLogradouro().length() > 0) {
				// Salva endereço da serventia*****************************************************************************************************************************************************************************************
				enderecoDt.setId(dados.getId_EnderecoRecebedor());
				enderecoDt.setLogradouro(dados.getLogradouro()); 
				enderecoDt.setNumero(dados.getNumero()); 
				enderecoDt.setComplemento(dados.getComplemento()); 
				enderecoDt.setCep(dados.getCep()); 
				enderecoDt.setId_Bairro(dados.getId_Bairro()); 
				enderecoDt.setBairro(dados.getBairro()); 
				enderecoDt.setCidade(dados.getCidade()); 
				enderecoDt.setUf(dados.getUf());
				enderecoDt.setIpComputadorLog(dados.getIpComputadorLog());
				enderecoDt.setId_UsuarioLog(dados.getId_UsuarioLog());
				enderecoNe.salvar(enderecoDt, obFabricaConexao);
				dados.setId_EnderecoRecebedor(enderecoDt.getId());
				//Salva endereço da serventia******************************************************************************************************************************************************************************************
			}
			
			/* use o id do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ProcessoObjetoArquivo",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("ProcessoObjetoArquivo",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String verificar(ProcessoObjetoArquivoDt dados ); 


//---------------------------------------------------------

	public void excluir(ProcessoObjetoArquivoDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {
			obFabricaConexao.iniciarTransacao();
			ProcessoObjetoArquivoPs obPersistencia = new ProcessoObjetoArquivoPs(obFabricaConexao.getConexao()); 

			obLogDt = new LogDt("ProcessoObjetoArquivo",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

	public ProcessoObjetoArquivoDt consultarId(String id_procobjetoarq ) throws Exception {

		ProcessoObjetoArquivoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;


		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{ 
			ProcessoObjetoArquivoPs obPersistencia = new ProcessoObjetoArquivoPs(obFabricaConexao.getConexao()); 

			dtRetorno= obPersistencia.consultarId(id_procobjetoarq ); 
			obDados.copiar(dtRetorno);
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	public long getQuantidadePaginas(){
		return QuantidadePaginas;
	}
//---------------------------------------------------------

	public List consultarDescricao(String descricao, String posicao ) throws Exception { 
		List tempList=null;
		FabricaConexao obFabricaConexao = null;


		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
		ProcessoObjetoArquivoPs obPersistencia = new ProcessoObjetoArquivoPs(obFabricaConexao.getConexao()); 

			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

	public List consultarDescricaoObjetoSubtipo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		List tempList=null;
			ObjetoSubtipoNe ObjetoSubtipone = new ObjetoSubtipoNe(); 
			tempList = ObjetoSubtipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = ObjetoSubtipone.getQuantidadePaginas();
			ObjetoSubtipone = null;
		return tempList;
	}

	public String consultarDescricaoObjetoSubtipoJSON(String descricao, String PosicaoPaginaAtual ) throws Exception { 
		String stTemp = (new ObjetoSubtipoNe()).consultarDescricaoJSON(descricao, PosicaoPaginaAtual);
		return stTemp;
	}

	public List consultarDescricaoProcesso(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		List tempList=null;
			ProcessoNe Processone = new ProcessoNe(); 
			tempList = Processone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Processone.getQuantidadePaginas();
			Processone = null;
		return tempList;
	}

	public String consultarDescricaoProcessoJSON(String descricao, String PosicaoPaginaAtual ) throws Exception {
		
		String stDigito = "";
		String stAno = "";
		String processoNumero= "";
		
		
		if (descricao!=null && !descricao.isEmpty()) {
			processoNumero = descricao;
			processoNumero = processoNumero.replaceAll("-", ".").replaceAll(",", ".");
			String[] stTemp = processoNumero.split("\\.");
			if (stTemp.length >= 1) { 
				processoNumero = stTemp[0];
			}else {
				processoNumero = "";
			}
			if (stTemp.length >= 2) stDigito = stTemp[1];
			if (stTemp.length >= 3) stAno = stTemp[2];
		}
		
		String stTemp = (new ProcessoNe()).consultarDescricaoJSON(processoNumero, PosicaoPaginaAtual);
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

	public String consultarDelegaciasAtivasJSON(String descricao, String PosicaoPaginaAtual ) throws Exception { 
		String stTemp = (new ServentiaNe()).consultarDelegaciasAtivasJSON(descricao, PosicaoPaginaAtual);
		return stTemp;
	}

	public List consultarDescricaoRgOrgaoExpedidor(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		List tempList=null;
			RgOrgaoExpedidorNe RgOrgaoExpedidorne = new RgOrgaoExpedidorNe(); 
			tempList = RgOrgaoExpedidorne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = RgOrgaoExpedidorne.getQuantidadePaginas();
			RgOrgaoExpedidorne = null;
		return tempList;
	}

	public String consultarDescricaoRgOrgaoExpedidorJSON(String sigla, String descricao, String PosicaoPaginaAtual ) throws Exception { 
		String stTemp = (new RgOrgaoExpedidorNe()).consultarDescricaoSiglaJSON(sigla, descricao, PosicaoPaginaAtual);
		return stTemp;
	}

	public List consultarDescricaoEndereco(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		List tempList=null;
			EnderecoNe Enderecone = new EnderecoNe(); 
			tempList = Enderecone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Enderecone.getQuantidadePaginas();
			Enderecone = null;
		return tempList;
	}



	}
