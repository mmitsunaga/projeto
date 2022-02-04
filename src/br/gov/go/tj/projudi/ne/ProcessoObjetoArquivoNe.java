package br.gov.go.tj.projudi.ne;


import br.gov.go.tj.projudi.dt.EnderecoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoObjetoArquivoDt;
import br.gov.go.tj.projudi.dt.ProcessoObjetoArquivoMovimentacaoDt;
import br.gov.go.tj.projudi.ps.ProcessoObjetoArquivoPs;
import br.gov.go.tj.utils.FabricaConexao;

public class ProcessoObjetoArquivoNe extends ProcessoObjetoArquivoNeGen{

	private static final long serialVersionUID = -5528254585785251352L;

	public String verificar(ProcessoObjetoArquivoDt dados ) {

		String stRetorno="";
		
		if (dados.getProcObjetoArq().length()==0)
			stRetorno += "O campo Descrição do Objeto é obrigatório.";
		
		if (dados.getId_ObjetoSubtipo().length()==0)
			stRetorno += "O campo Objeto Subtipo é obrigatório.";
		
		if (dados.getId_Processo().length()==0)
			stRetorno += "O campo Processo é obrigatório.";

		//if (dados.getCodigoLote().length()==0)
			//stRetorno += "O campo Codigo Lote é obrigatório.";
		
		if(dados.isTemNomeRecebedor()) {
			if (!dados.isTemCpf()) {
				stRetorno += "O campo CPF é obrigatório.";	
			}
			if (!dados.isTemRg()) {
				stRetorno += "O campo RG é obrigatório.";	
			}
			if (!dados.isTemOrgaoExpedidor()) {
				stRetorno += "O campo Orgão Expedidor é obrigatório.";	
			}
		}

		return stRetorno;

	}
	
	public String verificarMovimentacaoObjeto(ProcessoObjetoArquivoDt dados) {

		String stRetorno="";
		
		if (dados.getProcessoObjetoArquivoMovimentacao().length()==0)
			stRetorno += "O campo Descrição da Movimentação do Objeto é obrigatório.";
		
		if (dados.getDataMovimentacao().length()==0)
			stRetorno += "O campo Data Retirada é obrigatório.";

		return stRetorno;

	}
	
	public String consultarDescricaoBairroJSON(String descricao, String cidade, String uf, String posicao) throws Exception {
		String stTemp = "";
		
		BairroNe Bairrone = new BairroNe();
		stTemp = Bairrone.consultarDescricaoJSON(descricao, cidade, uf, "", posicao);
		
		return stTemp;
	}
	
	public String consultarDescricaoJSON(String numeroProcesso, String codigoLote, String inquerito, String id_Serventia_Usuario, String posicao ) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try {
				ProcessoObjetoArquivoPs obPersistencia = new ProcessoObjetoArquivoPs(obFabricaConexao.getConexao()); 
				
				String processoNumero= "";
				if (numeroProcesso!=null && !numeroProcesso.isEmpty()) {
					processoNumero = numeroProcesso;
					processoNumero = processoNumero.replaceAll("-", ".").replaceAll(",", ".");
					String[] strTemp = processoNumero.split("\\.");
					if (strTemp.length >= 1) { 
						processoNumero = strTemp[0];
					}else {
						processoNumero = "";
					}
					if (strTemp.length >= 2) {
						processoNumero += "-" + strTemp[1];
					}
					if (strTemp.length >= 3) {
						processoNumero += "." + strTemp[2];
					}
				}

				stTemp = obPersistencia.consultarDescricaoJSON(processoNumero, codigoLote, inquerito, id_Serventia_Usuario, posicao);
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return stTemp;
	}

	public String consultarListaObjetosJSON(String stId_Proc) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try {
				ProcessoObjetoArquivoPs obPersistencia = new ProcessoObjetoArquivoPs(obFabricaConexao.getConexao()); 
				
				stTemp = obPersistencia.consultarListaObjetosJSON(stId_Proc);
				
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return stTemp;
	}

	public String consultarListaMovimentosObjetoJSON(String stId_Obejto) throws Exception {
				
		return (new ProcessoObjetoArquivoMovimentacaoNe()).consultarListaMovimentosObjetoJSON(stId_Obejto);
	}

	public void salvarMovimentacao(ProcessoObjetoArquivoMovimentacaoDt dt, ProcessoObjetoArquivoDt processoObjetoArquivodt) throws Exception {
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {
			obFabricaConexao.iniciarTransacao();
			
			if(dt.isEncaminarLeilao()) {
				processoObjetoArquivodt.setStatusLeilao("1");
				if(dt.isTemFavorecidoLeilao()) {					
					processoObjetoArquivodt.setLeilao(dt.getFavorecidoLeilao());					
				}
				salvar(processoObjetoArquivodt, obFabricaConexao);
			}
			
			(new ProcessoObjetoArquivoMovimentacaoNe()).salvar(dt, obFabricaConexao);
			 
			obFabricaConexao.finalizarTransacao();
	
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

	public void excluirMovimentacaoArquivo(String id_excluir, String id_Usuario, String ip_computador) throws Exception {
		(new ProcessoObjetoArquivoMovimentacaoNe()).exlcuir(id_excluir, id_Usuario,ip_computador );				
	}

	private void salvar(ProcessoObjetoArquivoDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		EnderecoDt enderecoDt = new EnderecoDt();
		EnderecoNe enderecoNe = new EnderecoNe();
				
		ProcessoObjetoArquivoPs obPersistencia = new ProcessoObjetoArquivoPs(obFabricaConexao.getConexao()); 
		
		//Se tiver ID significa que é alteração de dados e não necessariamente terá endereço a ser preenchido
		if(dados.getId().length() > 0 &&	dados.getLogradouro().length() > 0) {
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
			
	}
}
