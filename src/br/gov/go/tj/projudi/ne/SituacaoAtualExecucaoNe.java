package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.SituacaoAtualExecucaoDt;
import br.gov.go.tj.projudi.dt.SituacaoAtualModalidadeDt;
import br.gov.go.tj.projudi.dt.SituacaoAtualTipoPenaDt;
import br.gov.go.tj.projudi.ps.SituacaoAtualExecucaoPs;
import br.gov.go.tj.utils.FabricaConexao;

public class SituacaoAtualExecucaoNe extends Negocio{

	private static final long serialVersionUID = 6265703084102951999L;
	protected  SituacaoAtualExecucaoDt obDados;

	public SituacaoAtualExecucaoNe() {
		obLog = new LogNe(); 
		obDados = new SituacaoAtualExecucaoDt(); 
	}

	public void salvar(SituacaoAtualExecucaoDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		SituacaoAtualModalidadeNe modalidadeNe = new SituacaoAtualModalidadeNe();
		SituacaoAtualTipoPenaNe tipoPenaNe = new SituacaoAtualTipoPenaNe();
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			SituacaoAtualExecucaoPs obPersistencia = new  SituacaoAtualExecucaoPs(obFabricaConexao.getConexao());
			
			if (dados.getId().equalsIgnoreCase("") ) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("SituacaoAtualExecucao", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());				
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("SituacaoAtualExecucao", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			
			if (dados.getListaSituacaoAtualModalidadeDt() != null && dados.getListaSituacaoAtualModalidadeDt().size() > 0){
				for (SituacaoAtualModalidadeDt modalidade : (List<SituacaoAtualModalidadeDt>) dados.getListaSituacaoAtualModalidadeDt()) {
					modalidade.setIdSituacaoAtualExecucao(dados.getId());
					modalidade.setId_UsuarioLog(dados.getId_UsuarioLog());
					modalidade.setIpComputadorLog(dados.getIpComputadorLog());
					modalidadeNe.salvar(modalidade, obFabricaConexao);
				}
			}
			
			if (dados.getListaSituacaoAtualTipoPenaDt() != null && dados.getListaSituacaoAtualTipoPenaDt().size() > 0){
				for (SituacaoAtualTipoPenaDt tipoPena : (List<SituacaoAtualTipoPenaDt>) dados.getListaSituacaoAtualTipoPenaDt()) {
					tipoPena.setIdSituacaoAtualExecucao(dados.getId());
					tipoPena.setId_UsuarioLog(dados.getId_UsuarioLog());
					tipoPena.setIpComputadorLog(dados.getIpComputadorLog());
					tipoPenaNe.salvar(tipoPena, obFabricaConexao);
				}
			}
			
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

	public void salvar(SituacaoAtualExecucaoDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		SituacaoAtualModalidadeNe modalidadeNe = new SituacaoAtualModalidadeNe();
		SituacaoAtualTipoPenaNe tipoPenaNe = new SituacaoAtualTipoPenaNe();
		
		SituacaoAtualExecucaoPs obPersistencia = new  SituacaoAtualExecucaoPs(obFabricaConexao.getConexao());
		
		if (dados.getId().equalsIgnoreCase("") ) {
			obPersistencia.inserir(dados);
			obLogDt = new LogDt("SituacaoAtualExecucao", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());				
		}else {				
			obPersistencia.alterar(dados);
			obLogDt = new LogDt("SituacaoAtualExecucao", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
		}

		obDados.copiar(dados);
		obLog.salvar(obLogDt, obFabricaConexao);
		
		if (dados.getListaSituacaoAtualModalidadeDt() != null && dados.getListaSituacaoAtualModalidadeDt().size() > 0){
			for (SituacaoAtualModalidadeDt modalidade : (List<SituacaoAtualModalidadeDt>) dados.getListaSituacaoAtualModalidadeDt()) {
				modalidade.setIdSituacaoAtualExecucao(dados.getId());
				modalidade.setId_UsuarioLog(dados.getId_UsuarioLog());
				modalidade.setIpComputadorLog(dados.getIpComputadorLog());
				modalidadeNe.salvar(modalidade, obFabricaConexao);
			}
		}
		
		if (dados.getListaSituacaoAtualTipoPenaDt() != null && dados.getListaSituacaoAtualTipoPenaDt().size() > 0){
			for (SituacaoAtualTipoPenaDt tipoPena : (List<SituacaoAtualTipoPenaDt>) dados.getListaSituacaoAtualTipoPenaDt()) {
				tipoPena.setIdSituacaoAtualExecucao(dados.getId());
				tipoPena.setId_UsuarioLog(dados.getId_UsuarioLog());
				tipoPena.setIpComputadorLog(dados.getIpComputadorLog());
				tipoPenaNe.salvar(tipoPena, obFabricaConexao);
			}
		}
			

		
	}
	
	public void excluir(SituacaoAtualExecucaoDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		SituacaoAtualModalidadeNe modalidadeNe = new SituacaoAtualModalidadeNe();
		SituacaoAtualTipoPenaNe tipoPenaNe = new SituacaoAtualTipoPenaNe();
		
		if (dados.getListaSituacaoAtualModalidadeDt() != null && dados.getListaSituacaoAtualModalidadeDt().size() > 0){
			for (SituacaoAtualModalidadeDt modalidade : (List<SituacaoAtualModalidadeDt>) dados.getListaSituacaoAtualModalidadeDt()) {
				modalidadeNe.excluir(modalidade, obFabricaConexao);
			}
		}
		
		if (dados.getListaSituacaoAtualTipoPenaDt() != null && dados.getListaSituacaoAtualTipoPenaDt().size() > 0){
			for (SituacaoAtualTipoPenaDt tipoPena : (List<SituacaoAtualTipoPenaDt>) dados.getListaSituacaoAtualTipoPenaDt()) {
				tipoPenaNe.excluir(tipoPena, obFabricaConexao);
			}
		}
		
		SituacaoAtualExecucaoPs obPersistencia = new  SituacaoAtualExecucaoPs(obFabricaConexao.getConexao());
		obLogDt = new LogDt("SituacaoAtualExecucao", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
		obPersistencia.excluir(dados.getId()); 
		dados.limpar();

		obLog.salvar(obLogDt, obFabricaConexao);

		
	}

	public void excluir(SituacaoAtualExecucaoDt dados) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		SituacaoAtualModalidadeNe modalidadeNe = new SituacaoAtualModalidadeNe();
		SituacaoAtualTipoPenaNe tipoPenaNe = new SituacaoAtualTipoPenaNe();

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			
			if (dados.getListaSituacaoAtualModalidadeDt() != null && dados.getListaSituacaoAtualModalidadeDt().size() > 0){
				for (SituacaoAtualModalidadeDt modalidade : (List<SituacaoAtualModalidadeDt>) dados.getListaSituacaoAtualModalidadeDt()) {
					modalidade.setId_UsuarioLog(dados.getId_UsuarioLog());
					modalidade.setIpComputadorLog(dados.getIpComputadorLog());
					modalidadeNe.excluir(modalidade, obFabricaConexao);
				}
			}
			
			if (dados.getListaSituacaoAtualTipoPenaDt() != null && dados.getListaSituacaoAtualTipoPenaDt().size() > 0){
				for (SituacaoAtualTipoPenaDt tipoPena : (List<SituacaoAtualTipoPenaDt>) dados.getListaSituacaoAtualTipoPenaDt()) {
					tipoPena.setId_UsuarioLog(dados.getId_UsuarioLog());
					tipoPena.setIpComputadorLog(dados.getIpComputadorLog());
					tipoPenaNe.excluir(tipoPena, obFabricaConexao);
				}
			}
			
			SituacaoAtualExecucaoPs obPersistencia = new SituacaoAtualExecucaoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("SituacaoAtualExecucao", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

	public SituacaoAtualExecucaoDt consultarId(String idSituacaoAtualExecucao) throws Exception {

		SituacaoAtualExecucaoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			SituacaoAtualExecucaoPs obPersistencia = new  SituacaoAtualExecucaoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(idSituacaoAtualExecucao); 
			obDados.copiar(dtRetorno);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	public SituacaoAtualExecucaoDt consultarIdProcesso(String idProcesso, String idUsuario, String ipComputador) throws Exception {

		SituacaoAtualExecucaoDt dtRetorno = new SituacaoAtualExecucaoDt();
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			SituacaoAtualExecucaoPs obPersistencia = new  SituacaoAtualExecucaoPs(obFabricaConexao.getConexao());
			List lista = obPersistencia.consultarIdProcesso(idProcesso);
			
			//faço isso para garantir que terá apenas uma situação atual para cada processo. (mantém o mais atual)
			if (lista != null && lista.size() > 1){
				for (int i=1; i<lista.size(); i++){
					SituacaoAtualExecucaoDt situacao = (SituacaoAtualExecucaoDt)lista.get(i);
					situacao.setId_UsuarioLog(idUsuario);
					situacao.setIpComputadorLog(ipComputador);
					this.excluir(situacao);
				}
			}
			if (lista != null && lista.size() > 0) dtRetorno = (SituacaoAtualExecucaoDt)lista.get(0);
			
			obDados.copiar(dtRetorno);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	public long getQuantidadePaginas(){
		return QuantidadePaginas;
	}

}
