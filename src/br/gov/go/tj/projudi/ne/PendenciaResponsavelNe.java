package br.gov.go.tj.projudi.ne;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import br.gov.go.tj.projudi.dt.CargoTipoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaResponsavelDt;
import br.gov.go.tj.projudi.dt.PendenciaResponsavelHistoricoDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaGrupoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ps.PendenciaResponsavelPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class PendenciaResponsavelNe extends PendenciaResponsavelNeGen {

	/**
     * 
     */
    private static final long serialVersionUID = -7198975837410575430L;

    public String Verificar(PendenciaResponsavelDt dados) {

		String stRetorno = "";

		return stRetorno;

	}

	/**
	 * Verifica dados obrigatórios na troca de responsável da pendencia
	 * @author lsbernardes
	 * @throws Exception 
	 */
	public String verificarTrocaResponsavel(PendenciaResponsavelDt dados, UsuarioDt usuarioDt, boolean isSemAssistente) throws Exception {
		String stRetorno = "";
		if (!isSemAssistente && (dados.getId_ServentiaCargo() == null || dados.getId_ServentiaCargo().length() == 0)) stRetorno = "Selecione o Novo Responsável. \n";
		if (dados.getId_Pendencia() == null || dados.getId_Pendencia().length() == 0) stRetorno += "Nenhuma pendência foi selecionada. \n";
//		if (usuarioDt.getGrupoCodigo() != null && Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.DISTRIBUIDOR_GABINETE){			
//			List listaResponsavelPendencia = this.consultarResponsaveisDetalhado(dados.getId_Pendencia(), usuarioDt.getGrupoCodigo(), null);			
//			if (listaResponsavelPendencia != null && listaResponsavelPendencia.size() == 1){							
//				PendenciaResponsavelDt dadosAtuais = (PendenciaResponsavelDt) listaResponsavelPendencia.get(0);				
//				if (dadosAtuais != null && dadosAtuais.getId_ServentiaCargo() != null && dadosAtuais.getId_ServentiaCargo().trim().length() > 0){
//					ServentiaCargoDt serventiaCargoAtual = (new ServentiaCargoNe()).consultarId(dadosAtuais.getId_ServentiaCargo());
//					if (serventiaCargoAtual != null && serventiaCargoAtual.getCargoTipoCodigo() != null && serventiaCargoAtual.getCargoTipoCodigo().trim().length()> 0 &&
//						Funcoes.StringToInt(serventiaCargoAtual.getCargoTipoCodigo()) == CargoTipoDt.ASSISTENTE_GABINETE){
//						stRetorno += "O Assistente já foi escolhido, para alterá-lo favor acessar a opção de Trocar Assistente Responsável nas opções do Processo. \n";
//					}					
//				}
//				/*if (dadosAtuais != null && dadosAtuais.getId_ServentiaCargo() != null && dadosAtuais.getId_ServentiaCargo().trim().length() > 0 && 
//					Funcoes.StringToInt(dadosAtuais.getId_ServentiaCargo()) != Funcoes.StringToInt(usuarioDt.getId_ServentiaCargo())){
//					stRetorno += "O Assistente já foi escolhido, para alterá-lo favor acessar a opção de Trocar Assistente Responsável nas opções do Processo. \n";
//				}*/			
//			}		
//		}
		return stRetorno;
	}
	
	/**
	 * Verifica dados obrigatórios na troca de responsável da pendencia
	 * @author lsbernardes
	 * @throws Exception 
	 */
	public String verificarTrocaResponsavelConclusao(PendenciaResponsavelDt dados) throws Exception {
		String stRetorno = "";
		if ( (dados.getId_Pendencia() == null || dados.getId_Pendencia().length() == 0) && 
				(dados.getlistaPendenciaDt() == null || dados.getlistaPendenciaDt().size() == 0) 
			){
			stRetorno += "Nenhuma pendência foi selecionada. \n";
		}
		
		if (!dados.isConclusoMagistrado()) {
		
			if ( (dados.getId_ServentiaGrupo() == null || dados.getId_ServentiaGrupo().length() == 0) ) { 
				stRetorno += "É obrigatório selecionar um Serventia Grupo. \n";
			}
			
			if(dados.isSemRegra()){
				if (dados.getId_ServentiaCargo() == null || dados.getId_ServentiaCargo().length() == 0) 
					stRetorno += "É obrigatório selecionar um Responsável. \n";
			}
		}
		
		return stRetorno;
	}
	
	/**
	 * Verifica dados obrigatórios na troca de responsável da intimação de uma procuradoria
	 * @author lsbernardes
	 * @throws Exception 
	 */
	public String verificarTrocaResponsavelProcessoParteAdvogado(PendenciaResponsavelDt dados, String Id_UsuarioServentiaAtual, UsuarioDt usuarioDt){
		String stRetorno = "";
		if (dados.getId_UsuarioResponsavel() == null || dados.getId_UsuarioResponsavel().length() == 0) 
			stRetorno += "Selecione o Novo Responsável pelo Processo. \n";
		if (Id_UsuarioServentiaAtual == null || Id_UsuarioServentiaAtual.length() == 0)
			stRetorno += "Selecione o usuário que será Substituído. \n";
		if (dados.getId_UsuarioResponsavel() != null && dados.getId_UsuarioResponsavel().length() > 0
				&& Id_UsuarioServentiaAtual != null && Id_UsuarioServentiaAtual.length() > 0
				&& dados.getId_UsuarioResponsavel().equals(Id_UsuarioServentiaAtual))
			stRetorno += "Selecione um usuário diferente do atual no Processo. \n";
		if (!usuarioDt.isCoordenadorJuridico())
			stRetorno += "Sem permissão para efetuar operação. \n";

		return stRetorno;
	}
	
	/**
	 * Verifica dados obrigatórios na troca de responsável de um conclusão
	 * @author lsbernardes
	 * @throws Exception 
	 */
	public String verificarTrocaResponsavelConclusao(PendenciaResponsavelDt dados, PendenciaDt pendenciaDt, String assistenteGabinete, String Id_ServentiaCargoAtual, UsuarioDt usuarioDt) throws Exception{
		String stRetorno = "";
		
//		ServentiaCargoDt serventiaCargoAtual = new ServentiaCargoDt();
		
		if (PendenciaTipoDt.isConclusoPresidenteVicePresidente(pendenciaDt)){
			stRetorno += "Não é possivel trocar o responsável da conclusão Presidente ou Vice Presidente do TJGO. \n";
		
		} else if (!dados.isNovoResponsavel()){
			if (dados.getId_ServentiaCargo() == null || dados.getId_ServentiaCargo().length() == 0) 
				stRetorno += "Selecione o Novo Responsável pela Conclusão. \n";
			
//			if (Id_ServentiaCargoAtual == null || Id_ServentiaCargoAtual.length() == 0)
//				stRetorno += "Selecione o Responsável que será Substituído. \n";
			
			if (dados.getId_ServentiaCargo() != null && dados.getId_ServentiaCargo().length() > 0
					&& Id_ServentiaCargoAtual != null && Id_ServentiaCargoAtual.length() > 0 && dados.getId_ServentiaCargo().equals(Id_ServentiaCargoAtual))
				stRetorno += "Selecione um Responsável diferente, pois o selecionada já está responsável pela Conclusão. \n";
			
			if (Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) != GrupoDt.GERENCIAMENTO_SEGUNDO_GRAU 
					&& Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) != GrupoDt.ASSISTENTE_GABINETE
					&& Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) != GrupoDt.ASSESSOR_GABINETE_PRESIDENTE
					&& Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) != GrupoDt.ASSISTENTE_GABINETE_FLUXO
					&& Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) != GrupoDt.DISTRIBUIDOR_GABINETE
					&& Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) != GrupoDt.DESEMBARGADOR)
				stRetorno += "Sem permissão para efetuar operação. \n";
			
			if (Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.DISTRIBUIDOR_GABINETE){
				PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
				if (!pendenciaResponsavelNe.isPendenciaResponsavelMesmaServentia(pendenciaDt.getId(), usuarioDt.getId_Serventia())){
					stRetorno += "Não é possivel trocar responsável da conclusão, pois o Desembargador responsável não pertence ao seu gabinete.";
				}
			}
		}
			
//			if (assistenteGabinete == null || (assistenteGabinete != null && !assistenteGabinete.equalsIgnoreCase("true"))){
//				if (stRetorno.length() == 0 && Id_ServentiaCargoAtual != null && Id_ServentiaCargoAtual.length()>0
//						&& dados.getId_ServentiaCargo() != null && dados.getId_ServentiaCargo().length()>0){
		
//						ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
//						serventiaCargoAtual = serventiaCargoNe.consultarServentiaCargoId(Id_ServentiaCargoAtual);
						
//						if(serventiaCargoAtual.getCargoTipoCodigo().equals(String.valueOf(CargoTipoDt.DESEMBARGADOR))){
//							ServentiaCargoDt serventiaCargoNovo = serventiaCargoNe.consultarServentiaCargoId(dados.getId_ServentiaCargo());
//							if (!serventiaCargoNovo.getCargoTipoCodigo().equals(String.valueOf(CargoTipoDt.DESEMBARGADOR)))
//								stRetorno += "Um Desembargador não pode ser substituído por um "+serventiaCargoNovo.getCargoTipo()+". \n";
//						
//						} else {
//							ServentiaCargoDt serventiaCargoNovo = serventiaCargoNe.consultarServentiaCargoId(dados.getId_ServentiaCargo());
//							if (serventiaCargoNovo.getCargoTipoCodigo().equals(String.valueOf(CargoTipoDt.DESEMBARGADOR)))
//								stRetorno +=  "Um "+serventiaCargoAtual.getCargoTipo()+" não pode ser substituído por um Desembargador. \n";
//						}
						
//						if (stRetorno.length() == 0){
//							if(serventiaCargoAtual.getCargoTipoCodigo().equals(String.valueOf(CargoTipoDt.ASSISTENTE_GABINETE))){
//								ServentiaCargoDt serventiaCargoNovo = serventiaCargoNe.consultarServentiaCargoId(dados.getId_ServentiaCargo());
//								if (!serventiaCargoNovo.getCargoTipoCodigo().equals(String.valueOf(CargoTipoDt.ASSISTENTE_GABINETE))
//										&& serventiaCargoAtual.getId_Serventia().equalsIgnoreCase(serventiaCargoNovo.getId_Serventia()))
//									stRetorno += "Um Assistente Gabinete não pode ser substituído por um "+serventiaCargoNovo.getCargoTipo()+" na Mesma Serventia. \n";
//							
//							} else {
//								ServentiaCargoDt serventiaCargoNovo = serventiaCargoNe.consultarServentiaCargoId(dados.getId_ServentiaCargo());
//								if (serventiaCargoNovo.getCargoTipoCodigo().equals(String.valueOf(CargoTipoDt.ASSISTENTE_GABINETE)))
//									stRetorno +=  "Um "+serventiaCargoAtual.getCargoTipo()+" não pode ser substituído por um Assistente Gabinete. \n";
//							}
//						}
//				}
//			}
//		} else {
//			ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
//			ServentiaCargoDt serventiaCargoNovo = serventiaCargoNe.consultarServentiaCargoId(dados.getId_ServentiaCargo());
//			if (serventiaCargoNovo.getCargoTipoCodigo().equals(String.valueOf(CargoTipoDt.DESEMBARGADOR)))
//				stRetorno +=  "Não é possível adicionar um Desembargador como Novo Responsável. \n";
//		}
		
		return stRetorno;
	}
	
	/**
	 * Verifica dados obrigatórios na troca de responsável de conclusão em lote
	 * @author lsbernardes
	 * @throws Exception 
	 */
	public String verificarTrocaResponsavelConclusaoLote(String id_UsuarioServentiaCargoAtual, String id_UsuarioServentiaCargoNovoResponsavel, String qtdConclusoes){
		String stRetorno = "";
		
		if (qtdConclusoes == null || qtdConclusoes.length() == 0 || qtdConclusoes.equalsIgnoreCase("null")){
			stRetorno +=  "Informe a quantidade de conclusões que terão o responsável alterado. \n";
		} else if (Funcoes.StringToInt(qtdConclusoes) == 0 || Funcoes.StringToInt(qtdConclusoes) > 100){
			stRetorno +=  "A quantidade de conclusões informadas deve ser maior que zero e menor ou igual a cem. \n";
		}
		
		if (id_UsuarioServentiaCargoAtual == null || id_UsuarioServentiaCargoAtual.length() == 0 || id_UsuarioServentiaCargoAtual.equalsIgnoreCase("null")){
			stRetorno +=  "Selecione o magistrado que cederá as conclusões. \n";
		}
		
		if (id_UsuarioServentiaCargoNovoResponsavel == null || id_UsuarioServentiaCargoNovoResponsavel.length() == 0 || id_UsuarioServentiaCargoNovoResponsavel.equalsIgnoreCase("null")){
			stRetorno +=  "Selecione o magistrado que receberá as conclusões. \n";
		}
		
		if (id_UsuarioServentiaCargoAtual != null && id_UsuarioServentiaCargoAtual.length()>0 && !id_UsuarioServentiaCargoAtual.equalsIgnoreCase("null")&&
				id_UsuarioServentiaCargoNovoResponsavel != null && id_UsuarioServentiaCargoNovoResponsavel.length()>0 && !id_UsuarioServentiaCargoNovoResponsavel.equalsIgnoreCase("null") &&
				id_UsuarioServentiaCargoAtual.equalsIgnoreCase(id_UsuarioServentiaCargoNovoResponsavel)){
			stRetorno += "O magistrado que cederá as conclusões deve ser difierente do que receberá as mesmas.";
		}
		
		return stRetorno;
	}
	
	/**
	 * Verifica dados obrigatórios na troca de responsável de conclusão.
	 * @author hmgodinho
	 * @throws Exception 
	 */
	public String verificarTrocaResponsavelConclusao(String id_UsuarioServentiaCargoAtual, String id_UsuarioServentiaCargoNovoResponsavel, String idConclusao){
		String stRetorno = "";
		
		if (idConclusao == null || idConclusao.length() == 0 || idConclusao.equalsIgnoreCase("null")){
			stRetorno +=  "Informe a conclusão que terá o responsável alterado. \n";
		}
		
		if (id_UsuarioServentiaCargoAtual == null || id_UsuarioServentiaCargoAtual.length() == 0 || id_UsuarioServentiaCargoAtual.equalsIgnoreCase("null")){
			stRetorno +=  "Selecione o magistrado que cederá a conclusão. \n";
		}
		
		if (id_UsuarioServentiaCargoNovoResponsavel == null || id_UsuarioServentiaCargoNovoResponsavel.length() == 0 || id_UsuarioServentiaCargoNovoResponsavel.equalsIgnoreCase("null")){
			stRetorno +=  "Selecione o magistrado que receberá a conclusão. \n";
		}
		
		if (id_UsuarioServentiaCargoAtual != null && id_UsuarioServentiaCargoAtual.length()>0 && !id_UsuarioServentiaCargoAtual.equalsIgnoreCase("null")&&
				id_UsuarioServentiaCargoNovoResponsavel != null && id_UsuarioServentiaCargoNovoResponsavel.length()>0 && !id_UsuarioServentiaCargoNovoResponsavel.equalsIgnoreCase("null") &&
				id_UsuarioServentiaCargoAtual.equalsIgnoreCase(id_UsuarioServentiaCargoNovoResponsavel)){
			stRetorno += "O magistrado que cederá a conclusão deve ser difierente do que receberá a mesma.";
		}
		
		return stRetorno;
	}

	/**
	 * Verifica se um usuario ja e responsavel da pendencia
	 * @author Ronneesley Moura Teles
	 * @since 20/01/2009 14:23
	 * @param pendenciaDt vo da pendencia
	 * @param usuarioDt vo do usuario que se deseja verificar 
	 * @return String
	 */
	public boolean eUsuarioResponsavel(PendenciaDt pendenciaDt, UsuarioDt usuarioDt) throws Exception {
	    FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaResponsavelPs obPersistencia = new PendenciaResponsavelPs(obFabricaConexao.getConexao());

			return obPersistencia.eUsuarioResponsavel(pendenciaDt, usuarioDt);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Valida os dados do responsavel
	 * @author Ronneesley Moura Teles
	 * @since 21/10/2008 16:47
	 * @param PendenciaResponsavelDt responsavel, vo a validar
	 * @return String
	 */
	public String validar(PendenciaResponsavelDt responsavel)  throws Exception {
		String mensagem = "";
		if ((responsavel.getId_Serventia() == null || responsavel.getId_Serventia().equals("")) && 
			(responsavel.getId_ServentiaCargo() == null || responsavel.getId_ServentiaCargo().equals("")) && 
			(responsavel.getId_ServentiaTipo() == null || responsavel.getId_ServentiaTipo().equals("")) && 
			(responsavel.getId_UsuarioResponsavel() == null || responsavel.getId_UsuarioResponsavel().equals(""))) 
		{
			throw new MensagemException("Responsável informado incorretamente ou o processo possui recurso em aberto."); 
		}

		return mensagem;
	}

	/**
	 * Inserir uma lista de responsaveis em uma pendencia
	 * @author Ronneesley Moura Teles
	 * @since 28/11/2008 09:35
	 * @param PendenciaDt pendenciaDt, vo de pendencia
	 * @param List responsaveis, lista de pendencia responsaveis
	 * @param FabricaConexao conexao, conexao
	 * @throws Exception
	 */
	public void inserirResponsaveis(PendenciaDt pendenciaDt, List responsaveis, FabricaConexao conexao) throws Exception {
		//Monta iterator para resonsaveis
		Iterator it = responsaveis.iterator();

		while (it.hasNext()) {
			PendenciaResponsavelDt pendenciaResponsavelDt = (PendenciaResponsavelDt)it.next();

			//Atribui o vinculo do responsavel com a pendencia
			pendenciaResponsavelDt.setId_Pendencia(pendenciaDt.getId());
			
			//log responsaveis
			pendenciaResponsavelDt.setId_UsuarioLog(pendenciaDt.getId_UsuarioLog());
			pendenciaResponsavelDt.setIpComputadorLog(pendenciaDt.getIpComputadorLog());

			this.inserir(pendenciaResponsavelDt, conexao);
		}
	}

	/**
	 * Inserir um novo responsavel
	 * @author Ronneesley Moura Teles
	 * @since 21/10/2008 16:51
	 * @param PendenciaResponsavelDt responsavel, vo de responsavel
	 * @param FabricaConexao conexao, conexao
	 * @throws Exception
	 */
	public void inserir(PendenciaResponsavelDt pendenciaResponsavelDt, FabricaConexao conexao) throws Exception {
	    FabricaConexao obFabricaConexao = null;
		if (conexao == null) obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = conexao;
		try{
			PendenciaResponsavelPs obPersistencia = new PendenciaResponsavelPs(obFabricaConexao.getConexao());

			String mensagem = this.validar(pendenciaResponsavelDt);
			if (!mensagem.trim().equals("")) throw new MensagemException(mensagem);

			obPersistencia.inserir(pendenciaResponsavelDt);
			
			LogNe logNe = new LogNe();
			logNe.salvar(new LogDt("PendenciaResponsavel", pendenciaResponsavelDt.getId(), pendenciaResponsavelDt.getId_UsuarioLog(), pendenciaResponsavelDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", pendenciaResponsavelDt.getPropriedades()), conexao);
		
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Consulta todos os responsaveis de uma determinada pendencia com dados de usuario
	 * @author Ronneesley Moura Teles
	 * @since 19/01/2009 17:12
	 * @param pendenciaDt vo de pendencia
	 * @return lista dos responsaveis
	 * @throws Exception
	 */
	public List consultarResponsaveisDetalhado(String id_PendenciaDt, String grupoCodigo, FabricaConexao fabConexao ) throws Exception {
		List responsaveis = null;
		FabricaConexao obFabricaConexao = null;
		PendenciaResponsavelPs obPersistencia = null;
		try{
			if (fabConexao == null){
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				 obPersistencia = new PendenciaResponsavelPs(obFabricaConexao.getConexao());
			} else
				 obPersistencia = new PendenciaResponsavelPs(fabConexao.getConexao());
			
			if ( grupoCodigo != null && Funcoes.StringToInt(grupoCodigo) == GrupoDt.DISTRIBUIDOR_GABINETE)
				responsaveis = obPersistencia.consultarResponsaveisCargoTipo(id_PendenciaDt, CargoTipoDt.DESEMBARGADOR);
			else
				responsaveis = obPersistencia.consultarResponsaveisDetalhado(id_PendenciaDt);
		
		
		} finally{
			if (fabConexao == null)
				obFabricaConexao.fecharConexao();
		}

		return responsaveis;
	}
	
	
	/**
	 * Consulta todos os responsaveis de uma determinada pendencia com dados de usuario
	 * @author Jesus Rodrigo Corrêa
	 * @since 21/08/2014
	 * @param pendenciaDt vo de pendencia
	 * @return lista dos responsaveis
	 * @throws Exception
	 */
	public List consultarResponsaveisDetalhadoPendenciaFinalizada(String id_PendenciaDt, String grupoCodigo, FabricaConexao fabConexao ) throws Exception {
		List responsaveis = null;
		FabricaConexao obFabricaConexao = null;
		PendenciaResponsavelPs obPersistencia = null;
		try{
			if (fabConexao == null){
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				 obPersistencia = new PendenciaResponsavelPs(obFabricaConexao.getConexao());
			} else
				 obPersistencia = new PendenciaResponsavelPs( fabConexao.getConexao());
			
			if ( grupoCodigo != null && Funcoes.StringToInt(grupoCodigo) == GrupoDt.DISTRIBUIDOR_GABINETE)
				responsaveis = obPersistencia.consultarResponsaveisCargoTipo(id_PendenciaDt, CargoTipoDt.DESEMBARGADOR);
			else
				responsaveis = obPersistencia.consultarResponsaveisDetalhadoPendenciaFinalizada(id_PendenciaDt);
		
		
		} finally{
			if (fabConexao == null)
				obFabricaConexao.fecharConexao();
		}

		return responsaveis;
	}
	
	/**
	 * Consulta todos os responsaveis de uma determinada pendencia com dados de usuario
	 * @param pendenciaDt vo de pendencia
	 * @return lista dos responsaveis
	 * @throws Exception
	 */
	public List consultarResponsaveisDetalhadoHistorico(String id_PendenciaDt) throws Exception {
		List responsaveis = null;
		FabricaConexao obFabricaConexao = null;
		PendenciaResponsavelPs obPersistencia = null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obPersistencia = new PendenciaResponsavelPs(obFabricaConexao.getConexao());
			responsaveis = obPersistencia.consultarResponsaveisDetalhado(id_PendenciaDt);
		
		} finally{
				obFabricaConexao.fecharConexao();
		}

		return responsaveis;
	}
	
	public PendenciaResponsavelDt consultarResponsavelId(String id_pendenciaresponsavel ) throws Exception {

		PendenciaResponsavelDt dtRetorno=null;
		////System.out.println("..ne-ConsultaId_PendenciaResponsavel" );
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaResponsavelPs obPersistencia = new PendenciaResponsavelPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarResponsavelId(id_pendenciaresponsavel ); 
			obDados.copiar(dtRetorno);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	/**
	 * Consulta todos os responsaveis de uma determinada pendencia com dados de usuario
	 * @param pendenciaDt vo de pendencia
	 * @return lista dos responsaveis
	 * @throws Exception
	 */
	public List consultarResponsaveisDetalhado(String id_PendenciaDt) throws Exception{
		return this.consultarResponsaveisDetalhado(id_PendenciaDt, null , null);
	}
	
	/**
	 * Consulta todos os responsaveis de uma determinada pendencia com dados de usuario
	 * @param pendenciaDt vo de pendencia
	 * @return lista dos responsaveis
	 * @throws Exception
	 */
	public List consultarResponsaveisDetalhado(String id_PendenciaDt, String grupoCodigo ) throws Exception{
		return this.consultarResponsaveisDetalhado(id_PendenciaDt, grupoCodigo , null);
	}
	
	public List consultarResponsaveisDetalhadoTrocaResponsavelConclusao(String id_PendenciaDt, String grupoCodigo ) throws Exception{
		return this.consultarResponsaveisDetalhadoTrocaResponsavelConclusao(id_PendenciaDt, grupoCodigo , null);
	}
	
	/**
	 * Consulta todos os responsaveis de uma determinada pendencia com dados de usuario
	 * @author lsbernardes
	 * @since 19/01/2009 17:12
	 * @param pendenciaDt vo de pendencia
	 * @return lista dos responsaveis
	 * @throws Exception
	 */
	public List consultarResponsaveisDetalhadoTrocaResponsavelConclusao(String id_PendenciaDt, String grupoCodigo, FabricaConexao fabConexao ) throws Exception {
		List responsaveis = null;
		FabricaConexao obFabricaConexao = null;
		PendenciaResponsavelPs obPersistencia = null;
		ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
		try{
			if (fabConexao == null){
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				 obPersistencia = new PendenciaResponsavelPs(obFabricaConexao.getConexao());
			} else
				 obPersistencia = new PendenciaResponsavelPs(fabConexao.getConexao());
			
			responsaveis = obPersistencia.consultarResponsaveisDetalhado(id_PendenciaDt);
		
			if (responsaveis != null && responsaveis.size() > 0){
				for (Iterator iterator = responsaveis.iterator(); iterator.hasNext();) {
					PendenciaResponsavelDt responsavel = (PendenciaResponsavelDt) iterator.next();
					if (responsavel != null && 
						responsavel.getId_ServentiaCargo() != null && 
						!responsavel.getId_ServentiaCargo().equals("") && 
						(responsavel.getId_Serventia() == null || responsavel.getId_Serventia().trim().length() == 0)){
						
						ServentiaCargoDt serventiaCargoDt = serventiaCargoNe.consultarId(responsavel.getId_ServentiaCargo(), (fabConexao != null ? fabConexao : obFabricaConexao));
						if (serventiaCargoDt != null) {
							responsavel.setId_Serventia(serventiaCargoDt.getId_Serventia());
						}
					}
				}
			}
		
		} finally{
			if (fabConexao == null)
				obFabricaConexao.fecharConexao();
		}

		return responsaveis;
	}
	
	/**
	 * Consulta procuradores responsável por uma determinada pendência em uma serventia
	 * @param String id_Pendencia, indentificador da pendencia
	 * @param String id_Serventia, indentificador da serventia
	 * @return lista dos responsaveis
	 * @throws Exception
	 */
	public List consultarResponsaveisIntimacoesCitacoes(String id_PendenciaDt, String id_Serventia ) throws Exception {
		List responsaveis = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaResponsavelPs obPersistencia = new PendenciaResponsavelPs(obFabricaConexao.getConexao());
			responsaveis = obPersistencia.consultarResponsaveisIntimacoesCitacoes(id_PendenciaDt, id_Serventia);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		
		return responsaveis;
	}
	
	
	/**
	 * Método para consultar pendencia por id e por status da pendencia. Consulta somente as pendencias abertas e o seu responsavel com a 
	 * respectiva Serventia(Id_Serventia).
	 * @param idPendencia
	 * @param pendenciaStatusCodigo
	 * @return PendenciaDt
	 * @throws Exception
	 */
	public PendenciaDt consultarPendenciaPorIdAbertaComResponsavel(int idPendencia, int pendenciaStatusCodigo) throws Exception {
		PendenciaDt pendenciaDt = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaResponsavelPs obPersistencia = new PendenciaResponsavelPs(obFabricaConexao.getConexao());
			pendenciaDt = obPersistencia.consultarPendenciaPorIdAbertaComResponsavel(idPendencia, pendenciaStatusCodigo);
		
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
		
		return pendenciaDt;
	}

	/**
	 * Consulta todos os responsaveis de uma determinada pendencia
	 * @author Ronneesley Moura Teles
	 * @since 14/10/2008 11:39
	 * @param PendenciaDt pendenciaDt, vo de pendencia
	 * @return List
	 * @throws Exception
	 */
	public List consultarResponsaveis(String id_PendenciaDt, FabricaConexao obFabricaConexao) throws Exception {
		List responsaveis = null;
		PendenciaResponsavelPs obPersistencia = new PendenciaResponsavelPs(obFabricaConexao.getConexao());
		responsaveis = obPersistencia.consultarResponsaveis(id_PendenciaDt);
		
		return responsaveis;
	}
	
	// jvosantos - 24/09/2019 17:36 - Extrair método para usar uma mesma conexão
	public List consultarResponsaveis(String id_PendenciaDt) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			return consultarResponsaveis(id_PendenciaDt, obFabricaConexao);
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Consulta todos os responsaveis de uma determinada pendencia
	 * @param PendenciaDt pendenciaDt, vo de pendencia
	 * @return List
	 * @throws Exception
	 */
	public List consultarResponsaveisFinais(String id_PendenciaDt, FabricaConexao obFabricaConexao) throws Exception {
		List responsaveis = null;
		PendenciaResponsavelPs obPersistencia = new PendenciaResponsavelPs(obFabricaConexao.getConexao());
		responsaveis = obPersistencia.consultarResponsaveisFinais(id_PendenciaDt);
		return responsaveis;
	}
	
	// jvosantos - 24/09/2019 18:06 - Extrair método para usar a mesma conexão
	public List consultarResponsaveisFinais(String id_PendenciaDt) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			return consultarResponsaveisFinais(id_PendenciaDt, obFabricaConexao);
		} finally{
			obFabricaConexao.fecharConexao();
		}

	}

	/**
	 * Método salvar que recebe conexão como parâmetro
	 */
	public void salvar(PendenciaResponsavelDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		
		PendenciaResponsavelPs obPersistencia = new PendenciaResponsavelPs(obFabricaConexao.getConexao());
		if (dados.getId().equalsIgnoreCase("")) {				
			obPersistencia.inserir(dados);
			obLogDt = new LogDt("PendenciaResponsavel", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
		} else {				
			obPersistencia.alterar(dados);
			obLogDt = new LogDt("PendenciaResponsavel", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());
		}
		obDados.copiar(dados);
		obLog.salvar(obLogDt, obFabricaConexao);
		
	}

	public PendenciaDt consultaSimplesId(UsuarioNe usuarioNe, String id_pendencia) throws Exception {

		PendenciaDt dtRetorno = null;
		PendenciaNe pendenciaNe = new PendenciaNe();
		
		dtRetorno = pendenciaNe.consultarId(id_pendencia);
		dtRetorno.setHash(usuarioNe.getCodigoHash(dtRetorno.getId()));

		return dtRetorno;
	}

	/**
	 * Chama método que realizará a consulta
	 */
	public List consultarServentiaCargos(String tempNomeBusca, String posicaoPaginaAtual, String id_Serventia, String serventiaTipoCodigo,  String serventiaSubTipoCodigo) throws Exception {
		List tempList = null;
		
		ServentiaCargoNe ServentiaCargone = new ServentiaCargoNe();
		tempList = ServentiaCargone.consultarServentiaCargos(tempNomeBusca, posicaoPaginaAtual, id_Serventia, serventiaTipoCodigo, serventiaSubTipoCodigo);
		QuantidadePaginas = ServentiaCargone.getQuantidadePaginas();
		ServentiaCargone = null;
		
		return tempList;
	}
	

	public String consultarDescricaoServentiaJSON(String descricao, UsuarioDt usuarioDt, String posicao) throws Exception {
		String stTemp = "";
		
		ServentiaNe serventiaNe = new ServentiaNe();
		stTemp = serventiaNe.consultarDescricaoServentiaAlterarResponsavelConclusaoJSON(descricao, usuarioDt, posicao);
		
		return stTemp;
	}	
	
	public String consultarServentiaCargosJSON(String tempNomeBusca, String posicaoPaginaAtual, String id_Serventia, String serventiaTipoCodigo, String serventiaSubtipoCodigo) throws Exception {
		String stTemp = "";
		ServentiaCargoNe ServentiaCargone = new ServentiaCargoNe();
		stTemp = ServentiaCargone.consultarServentiaCargosJSON(tempNomeBusca, posicaoPaginaAtual, id_Serventia, serventiaTipoCodigo, serventiaSubtipoCodigo);
		ServentiaCargone = null;
		return stTemp;
	}

	/**
	 * Chama método que realizará a consulta
	 */
	public List consultarUsuariosServentia(String tempNomeBusca, String posicaoPaginaAtual, String id_Serventia) throws Exception {
		List tempList = null;
		
		UsuarioServentiaNe usuarioServentiaNe = new UsuarioServentiaNe();
		tempList = usuarioServentiaNe.consultarUsuariosServentia(tempNomeBusca, posicaoPaginaAtual, id_Serventia);
		QuantidadePaginas = usuarioServentiaNe.getQuantidadePaginas();
		usuarioServentiaNe = null;
		
		return tempList;
	}
	
	/**
	 * Chama método que realizará a consulta
	 */
	public String consultarUsuariosServentiaJSON(String nomeBusca, String posicaoPaginaAtual, String id_Serventia) throws Exception {
		String stTemp = "";
		
		UsuarioServentiaNe usuarioServentiaNe = new UsuarioServentiaNe();
		stTemp = usuarioServentiaNe.consultarUsuariosServentiaAdvogadosJSON(nomeBusca, posicaoPaginaAtual, id_Serventia);			
		usuarioServentiaNe = null;
		
		return stTemp;
	}
	
	/**
	 * Salva Troca de Responsável de uma pendencia.
	 * 
	 * @param PendenciaResponsavelDt: objeto com dados da troca de responsável a ser persistida
	 * @author lsbernardes
	 */
	public void AtualizarResponsavelPendencia(PendenciaResponsavelDt pendenciaResponsavelNovoDt, String grupoCodigo, String serventiaSubTipoCodigo, String idProcesso, boolean processoSemAssistente) throws Exception {
	    FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			
			ProcessoResponsavelNe processoResponsavelNe = new ProcessoResponsavelNe();
			PendenciaNe pendenciaNe = new PendenciaNe();
			PendenciaResponsavelDt pendenciaResponsavelDtAtual = new PendenciaResponsavelDt();

			LogDt logDt = new LogDt(pendenciaResponsavelNovoDt.getId_UsuarioLog(), pendenciaResponsavelNovoDt.getIpComputadorLog());

			List listaResponsavelPendencia = this.consultarResponsaveisDetalhado(pendenciaResponsavelNovoDt.getId_Pendencia(), grupoCodigo, obFabricaConexao);
			if (listaResponsavelPendencia != null) 
				pendenciaResponsavelDtAtual = (PendenciaResponsavelDt) listaResponsavelPendencia.get(0);
			else 
				throw new MensagemException("Não foi possivel trocar responsável da pendência.");
			
			if (processoSemAssistente) {
				retirarResponsavelPendencia(pendenciaResponsavelNovoDt.getId_Pendencia(), pendenciaResponsavelDtAtual.getId_ServentiaCargo(), logDt, obFabricaConexao);
			} else {
				this.alterarResponsavelPendencia(pendenciaResponsavelNovoDt.getId_Pendencia(), pendenciaResponsavelDtAtual.getId_ServentiaCargo(), pendenciaResponsavelNovoDt.getId_ServentiaCargo(), logDt, obFabricaConexao);

				ProcessoDt processoDt = new ProcessoNe().consultarId(idProcesso);
				ServentiaCargoDt servCargoNovoResponsavelDt = new ServentiaCargoNe().consultarId(pendenciaResponsavelNovoDt.getId_ServentiaCargo());
				if(processoDt.isSigiloso() && servCargoNovoResponsavelDt.isMP()) {
					processoResponsavelNe.alterarResponsavelProcesso(idProcesso, pendenciaResponsavelDtAtual.getId_ServentiaCargo(), pendenciaResponsavelNovoDt.getId_ServentiaCargo(), logDt, obFabricaConexao, null);
				}

				if (pendenciaResponsavelDtAtual.getId_ServentiaCargo() != null && pendenciaResponsavelDtAtual.getId_ServentiaCargo().length()>0)
					this.alterarResponsavelPendencia(pendenciaResponsavelNovoDt.getId_Pendencia(), pendenciaResponsavelDtAtual.getId_ServentiaCargo(), pendenciaResponsavelNovoDt.getId_ServentiaCargo(), logDt, obFabricaConexao);
				else if (pendenciaResponsavelDtAtual.getId_UsuarioResponsavel() != null && pendenciaResponsavelDtAtual.getId_UsuarioResponsavel().length()>0)
					this.alterarResponsavelUsuarioServentiaPendencia(pendenciaResponsavelNovoDt.getId_Pendencia(), pendenciaResponsavelDtAtual.getId_UsuarioResponsavel(), pendenciaResponsavelNovoDt.getId_UsuarioResponsavel(), logDt, obFabricaConexao);
			
				//se a serventia for do subtipo presidencia ou vicepresidencia, Não salva responsável no Processo
				if (!ServentiaDt.isPresidenciaVicePresidencia(serventiaSubTipoCodigo)){
					ProcessoResponsavelNe processoNe = new ProcessoResponsavelNe();
					if ( grupoCodigo != null && Funcoes.StringToInt(grupoCodigo) == GrupoDt.DISTRIBUIDOR_GABINETE && !processoNe.isResponsavelProcesso(pendenciaResponsavelNovoDt.getId_ServentiaCargo(), idProcesso, obFabricaConexao)){
						processoResponsavelNe.salvarProcessoResponsavel(idProcesso, pendenciaResponsavelNovoDt.getId_ServentiaCargo(), false, CargoTipoDt.ASSISTENTE_GABINETE, logDt, obFabricaConexao);
					}
				}
				
			}			
			
			if (grupoCodigo != null && !grupoCodigo.equals("") && Funcoes.StringToInt(grupoCodigo) == GrupoDt.COORDENADOR_PROMOTORIA){
				pendenciaNe.marcarIntimacaoCitacaoDistribuida(pendenciaResponsavelNovoDt.getId_Pendencia(), logDt, obFabricaConexao);
			}
			
			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Distribui uma pendencia (conclusão) para uma Unidade de Trabalho (Serventia Grupo).
	 * 
	 * @param PendenciaResponsavelDt: objeto com dados da troca de responsável a ser persistida
	 * @author lsbernardes
	 */
	public void distribuirConclusaoUnidadeTrabalho(PendenciaResponsavelDt pendenciaResponsavelNovoDt, UsuarioDt usuarioDt, String idProcesso) throws Exception {
	    FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			
			ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
			PendenciaResponsavelHistoricoNe pendenciaResponsavelHistoricoNe = new PendenciaResponsavelHistoricoNe();
			PendenciaResponsavelDt pendenciaResponsavelDtAtual = new PendenciaResponsavelDt();
			LogDt logDt = new LogDt(pendenciaResponsavelNovoDt.getId_UsuarioLog(), pendenciaResponsavelNovoDt.getIpComputadorLog());

			List listaResponsavelPendencia = this.consultarResponsaveisDetalhado(pendenciaResponsavelNovoDt.getId_Pendencia(), usuarioDt.getGrupoCodigo(), obFabricaConexao);
			if (listaResponsavelPendencia != null) 
				pendenciaResponsavelDtAtual = (PendenciaResponsavelDt) listaResponsavelPendencia.get(0);
			else 
				throw new MensagemException("Não foi possível Distribuir Conclusão.");
			
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
            String data_Atual = df.format(new Date());

			if (usuarioDt.getGrupoCodigo() != null && !usuarioDt.getGrupoCodigo().equals("") && 
					(Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) != GrupoDt.DISTRIBUIDOR_GABINETE  &&
					 Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) != GrupoDt.DESEMBARGADOR )){
				//Fechar Historico
				pendenciaResponsavelHistoricoNe.fecharHistorico(pendenciaResponsavelNovoDt.getId_Pendencia(), data_Atual, pendenciaResponsavelNovoDt.getId_UsuarioLog(), pendenciaResponsavelNovoDt.getIpComputadorLog(), obFabricaConexao);
			}
			
			String id_ServentiaCargoNovo = "";
			if (pendenciaResponsavelNovoDt.isConclusoMagistrado()){
				//if (usuarioDt.getServentiaSubtipoCodigo().equalsIgnoreCase(String.valueOf(ServentiaSubtipoDt.GABINETE_VICE_PRESIDENCIA_TJGO)))
					id_ServentiaCargoNovo = serventiaCargoNe.getDesembargadorServentia(usuarioDt.getId_Serventia(), obFabricaConexao);
				//else
				//	id_ServentiaCargoNovo = serventiaCargoNe.getDesembargadorResponsavel(usuarioDt.getId_Serventia(), idProcesso, obFabricaConexao);
			
			} else {
				String id_ServentiaCargoPrevencao = serventiaCargoNe.consultarPrevensaoServentiaGrupo(pendenciaResponsavelNovoDt.getId_ServentiaGrupo(), pendenciaResponsavelNovoDt.getId_Pendencia());
				if (id_ServentiaCargoPrevencao != null && id_ServentiaCargoPrevencao.length()>0)
					id_ServentiaCargoNovo = id_ServentiaCargoPrevencao;
				else
					id_ServentiaCargoNovo  = serventiaCargoNe.consultarServentiaGrupoDistribuicao(pendenciaResponsavelNovoDt.getId_ServentiaGrupo());
			}
			
			if (id_ServentiaCargoNovo == null || id_ServentiaCargoNovo.length() == 0)
				throw new MensagemException("Erro ao buscar responsável. Responsável não localizado.");
			
			this.alterarResponsavelPendencia(pendenciaResponsavelNovoDt.getId_Pendencia(), pendenciaResponsavelDtAtual.getId_ServentiaCargo(), id_ServentiaCargoNovo, logDt, obFabricaConexao);
		
			if (!pendenciaResponsavelNovoDt.isConclusoMagistrado()){
				//Criar histórico
				PendenciaResponsavelHistoricoDt pendenciaResponsavelHistoricoDt = new PendenciaResponsavelHistoricoDt();
				pendenciaResponsavelHistoricoDt.setId_Pendencia(pendenciaResponsavelNovoDt.getId_Pendencia());
				pendenciaResponsavelHistoricoDt.setDataInicio(data_Atual);
				pendenciaResponsavelHistoricoDt.setId_ServentiaCargo(id_ServentiaCargoNovo);
				pendenciaResponsavelHistoricoDt.setId_ServentiaGrupo(pendenciaResponsavelNovoDt.getId_ServentiaGrupo());
				pendenciaResponsavelHistoricoDt.setId_UsuarioLog(pendenciaResponsavelNovoDt.getId_UsuarioLog());
				pendenciaResponsavelHistoricoDt.setIpComputadorLog(pendenciaResponsavelNovoDt.getIpComputadorLog());
				pendenciaResponsavelHistoricoNe.salvar(pendenciaResponsavelHistoricoDt, obFabricaConexao);
			}
		
			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Verifica se é uma Redistribui uma pendencia (conclusão) para uma Unidade de Trabalho (Serventia Grupo).
	 * 
	 * @param PendenciaResponsavelDt: objeto com dados da troca de responsável a ser persistida
	 * @author lsbernardes
	 */
	public boolean isReDistribuirConclusaoUnidadeTrabalho(PendenciaDt pendenciaDt, UsuarioDt usuarioDt){
		if (pendenciaDt.getResponsavel().getCargoTipoCodigo() != null  &&
				(Funcoes.StringToInt(pendenciaDt.getResponsavel().getCargoTipoCodigo()) == CargoTipoDt.ASSISTENTE_GABINETE ||
					Funcoes.StringToInt(pendenciaDt.getResponsavel().getCargoTipoCodigo()) == CargoTipoDt.ASSISTENTE_GABINETE_FLUXO ||
					Funcoes.StringToInt(pendenciaDt.getResponsavel().getCargoTipoCodigo()) == CargoTipoDt.DESEMBARGADOR || 
					Funcoes.StringToInt(pendenciaDt.getResponsavel().getCargoTipoCodigo()) == CargoTipoDt.JUIZ_UPJ ||
					Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.DISTRIBUIDOR_GABINETE)){
			return true;
		}
		return false;
	}
	
	/**
	 * Verifica se é uma Distribuicao ou Redistribui uma pendencia (conclusão) para uma Unidade de Trabalho (Serventia Grupo).
	 * 
	 * @param PendenciaResponsavelDt: objeto com dados da troca de responsável a ser persistida
	 * @author lsbernardes
	 */
	public boolean isDistribuirConclusaoUnidadeTrabalho(UsuarioDt usuarioDt){
		if (ServentiaDt.isPresidenciaVicePresidencia(usuarioDt.getServentiaSubtipoCodigo()) || ServentiaDt.isGabineteUPJ(usuarioDt.getServentiaSubtipoCodigo())){
			return true;
		}
		return false;
	}
	
	
	 public void distribuirConclusaoUnidadeTrabalhoLote(PendenciaResponsavelDt pendenciaResponsavelNovoDt, UsuarioDt usuarioDt) throws Exception {
	    FabricaConexao obFabricaConexao = null;
	    try {
	        obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
	        obFabricaConexao.iniciarTransacao();
	        
	        for (Iterator<PendenciaDt> iterator = pendenciaResponsavelNovoDt.getlistaPendenciaDt().iterator(); iterator.hasNext();) {
				PendenciaDt pendenciaDt = (PendenciaDt) iterator.next();
				 this.distribuirConclusaoUnidadeTrabalho(pendenciaResponsavelNovoDt, usuarioDt, pendenciaDt, obFabricaConexao);
			}
	
	        obFabricaConexao.finalizarTransacao();
	    } catch (Exception e) {
	        obFabricaConexao.cancelarTransacao();
	        throw e;
	    } finally {
	        obFabricaConexao.fecharConexao();
	    }
	 }
	
	
	/** 
	 * Redistribui uma pendencia (conclusão) para uma Unidade de Trabalho (Serventia Grupo).
	 * 
	 * @param PendenciaResponsavelDt: objeto com dados da troca de responsável a ser persistida
	 * @param  UsuarioDt: usuário que está realizando a operação
	 * @param PendenciaDt: Objeto com dados para realizar a troca de responsável a ser persistida
	 * @param FabricaConexao fabConexao, conexao
	 * 
	 * @author lsbernardes
	 */
	public void distribuirConclusaoUnidadeTrabalho(PendenciaResponsavelDt pendenciaResponsavelNovoDt, UsuarioDt usuarioDt, PendenciaDt pendenciaDt, FabricaConexao fabConexao) throws Exception{
	    FabricaConexao obFabricaConexao = null;
		try{
			 // Verifica se a conexao sera criada internamente
            if (fabConexao == null) {
                obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
                obFabricaConexao.iniciarTransacao();
            } else {
                // Caso da conexao criada em um nivel superior
                obFabricaConexao = fabConexao;
            }
			
			ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
			PendenciaResponsavelHistoricoNe pendenciaResponsavelHistoricoNe = new PendenciaResponsavelHistoricoNe();
			PendenciaResponsavelDt pendenciaResponsavelDtAtual = new PendenciaResponsavelDt();
			LogDt logDt = new LogDt(pendenciaResponsavelNovoDt.getId_UsuarioLog(), pendenciaResponsavelNovoDt.getIpComputadorLog());

			List listaResponsavelPendencia = this.consultarResponsaveisDetalhado(pendenciaDt.getId(), usuarioDt.getGrupoCodigo(), obFabricaConexao);
			if (listaResponsavelPendencia != null) 
				pendenciaResponsavelDtAtual = (PendenciaResponsavelDt) listaResponsavelPendencia.get(0);
			else 
				throw new MensagemException("Não foi possível Distribuir Conclusão.");
			
			boolean primeiraDistribuicao=false;
			if (this.consultarHistoricosPendencia(pendenciaDt.getId()).size() == 0){
				primeiraDistribuicao = true;
			}
			
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
            String data_Atual = df.format(new Date());

			if (usuarioDt.getGrupoCodigo() != null && (Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) != GrupoDt.DESEMBARGADOR && !primeiraDistribuicao)){
				//Fechar Historico
				pendenciaResponsavelHistoricoNe.fecharHistorico(pendenciaDt.getId(), data_Atual, pendenciaResponsavelNovoDt.getId_UsuarioLog(), pendenciaResponsavelNovoDt.getIpComputadorLog(), obFabricaConexao);
			}
			
			String id_ServentiaCargoNovo = "";
			if (pendenciaResponsavelNovoDt.isConclusoMagistrado()){
				
				if (pendenciaDt != null && pendenciaDt.getPendenciaTipoCodigo() != null && pendenciaDt.getPendenciaTipoCodigo().trim().length() > 0) {
					if (Funcoes.StringToInt(pendenciaDt.getPendenciaTipoCodigo()) == PendenciaTipoDt.CONCLUSO_PRESIDENTE_TJGO) {
						ServentiaCargoDt serventiaCargoPresidenteTJGO = serventiaCargoNe.getPresidenteTribunalDeJustica(obFabricaConexao);
	        			if (serventiaCargoPresidenteTJGO != null && serventiaCargoPresidenteTJGO.getId().length() > 0){
	        				id_ServentiaCargoNovo = serventiaCargoPresidenteTJGO.getId();
	        			}
	    			 } else if (Funcoes.StringToInt(pendenciaDt.getPendenciaTipoCodigo()) == PendenciaTipoDt.CONCLUSO_VICE_PRESIDENTE_TJGO) {
	    				 ServentiaCargoDt serventiaCargoVicePresidente = serventiaCargoNe.getVicePresidenteTribunalDeJustica(obFabricaConexao);
	    				 if (serventiaCargoVicePresidente != null && serventiaCargoVicePresidente.getId().length() > 0){
		        			id_ServentiaCargoNovo = serventiaCargoVicePresidente.getId();
	        			 }
	    			 } else if (Funcoes.StringToInt(pendenciaDt.getPendenciaTipoCodigo()) == PendenciaTipoDt.CONCLUSO_PRESIDENTE) {
	    				 ServentiaCargoDt serventiaCargoPresidente = serventiaCargoNe.getPresidenteSegundoGrau(usuarioDt.getId_Serventia(), obFabricaConexao);
	    				 if (serventiaCargoPresidente != null && serventiaCargoPresidente.getId().length() > 0){
	    					 id_ServentiaCargoNovo = serventiaCargoPresidente.getId();
	    				 }
	    			 }
				}
				
				if (id_ServentiaCargoNovo == null || id_ServentiaCargoNovo.trim().length() == 0) {
					id_ServentiaCargoNovo = serventiaCargoNe.getDesembargadorResponsavel(usuarioDt.getId_Serventia(), pendenciaDt.getId_Processo(), obFabricaConexao);
				}
				
				if (id_ServentiaCargoNovo == null || id_ServentiaCargoNovo.trim().length() == 0) {
					id_ServentiaCargoNovo = serventiaCargoNe.getJuizUPJResponsavel(usuarioDt.getId_Serventia(), pendenciaDt.getId_Processo(), obFabricaConexao);
				}
				
				if (id_ServentiaCargoNovo == null || id_ServentiaCargoNovo.trim().length() == 0) {
					String codServentiaSubTipoCodigo = new ServentiaNe().consultarServentiaSubTipoCodigo(usuarioDt.getId_Serventia(), obFabricaConexao);
					ServentiaCargoDt serventiaCargoRelator2Grau = new ProcessoResponsavelNe().consultarRelator2GrauConsideraSubstituicao(pendenciaDt.getId_Processo(), usuarioDt.getId_Serventia(), codServentiaSubTipoCodigo, obFabricaConexao);
					if (serventiaCargoRelator2Grau != null && serventiaCargoRelator2Grau.getId().length() > 0){
						id_ServentiaCargoNovo = serventiaCargoRelator2Grau.getId();
	   				}
				}
			
			} else if (pendenciaResponsavelNovoDt.isSemRegra()){
				id_ServentiaCargoNovo = pendenciaResponsavelNovoDt.getId_ServentiaCargo();
			
			} else {
				if (pendenciaDt.getId_ServentiaGrupo().equalsIgnoreCase(pendenciaResponsavelNovoDt.getId_ServentiaGrupo())) {
					id_ServentiaCargoNovo  = serventiaCargoNe.consultarServentiaGrupoReDistribuicao(pendenciaResponsavelNovoDt.getId_ServentiaGrupo(),pendenciaResponsavelDtAtual.getId_ServentiaCargo());
				} else{
					String id_ServentiaCargoPrevencao = serventiaCargoNe.consultarPrevensaoServentiaGrupo(pendenciaResponsavelNovoDt.getId_ServentiaGrupo(), pendenciaDt.getId());
					if (id_ServentiaCargoPrevencao != null && id_ServentiaCargoPrevencao.length()>0)
						id_ServentiaCargoNovo = id_ServentiaCargoPrevencao;
					else
						id_ServentiaCargoNovo  = serventiaCargoNe.consultarServentiaGrupoDistribuicao(pendenciaResponsavelNovoDt.getId_ServentiaGrupo());
				}
			}
			
			if (id_ServentiaCargoNovo == null || id_ServentiaCargoNovo.length() == 0)
				throw new MensagemException("Não foi encontrado Cargo");
			
			this.alterarResponsavelPendencia(pendenciaDt.getId(), pendenciaResponsavelDtAtual.getId_ServentiaCargo(), id_ServentiaCargoNovo, logDt, obFabricaConexao);
			
			if (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.DISTRIBUIDOR_GABINETE && primeiraDistribuicao){
				
				ServentiaGrupoDt serventiaGrupoDt = this.consultarSeventiaGrupoDistribuidorId(usuarioDt.getId_ServentiaCargo());
				if (serventiaGrupoDt != null && serventiaGrupoDt.getId() != null && serventiaGrupoDt.getId().length()>0) {
					//Criar histórico de distribução pelo Distribuidor que realizou a operacao
					PendenciaResponsavelHistoricoDt pendenciaResponsavelHistoricoDistribuidorDt   = new PendenciaResponsavelHistoricoDt();
					pendenciaResponsavelHistoricoDistribuidorDt.setId_Pendencia(pendenciaDt.getId());
					pendenciaResponsavelHistoricoDistribuidorDt.setDataInicio(pendenciaDt.getDataInicio());
					pendenciaResponsavelHistoricoDistribuidorDt.setDataFim(data_Atual);
					pendenciaResponsavelHistoricoDistribuidorDt.setId_ServentiaCargo(usuarioDt.getId_ServentiaCargo());
					pendenciaResponsavelHistoricoDistribuidorDt.setId_ServentiaGrupo(serventiaGrupoDt.getId());
					pendenciaResponsavelHistoricoDistribuidorDt.setId_UsuarioLog(pendenciaResponsavelNovoDt.getId_UsuarioLog());
					pendenciaResponsavelHistoricoDistribuidorDt.setIpComputadorLog(pendenciaResponsavelNovoDt.getIpComputadorLog());
					pendenciaResponsavelHistoricoNe.salvar(pendenciaResponsavelHistoricoDistribuidorDt, obFabricaConexao);
				}
			}
		
			if (!pendenciaResponsavelNovoDt.isConclusoMagistrado()){
				//Criar histórico
				PendenciaResponsavelHistoricoDt pendenciaResponsavelHistoricoDt = new PendenciaResponsavelHistoricoDt();
				pendenciaResponsavelHistoricoDt.setId_Pendencia(pendenciaDt.getId());
				pendenciaResponsavelHistoricoDt.setDataInicio(data_Atual);
				pendenciaResponsavelHistoricoDt.setId_ServentiaCargo(id_ServentiaCargoNovo);
				pendenciaResponsavelHistoricoDt.setId_ServentiaGrupo(pendenciaResponsavelNovoDt.getId_ServentiaGrupo());
				pendenciaResponsavelHistoricoDt.setId_UsuarioLog(pendenciaResponsavelNovoDt.getId_UsuarioLog());
				pendenciaResponsavelHistoricoDt.setIpComputadorLog(pendenciaResponsavelNovoDt.getIpComputadorLog());
				pendenciaResponsavelHistoricoNe.salvar(pendenciaResponsavelHistoricoDt, obFabricaConexao);
			} else {
				
				ServentiaGrupoDt serventiaGrupoDt = this.consultarSeventiaGrupoMagistradoId(id_ServentiaCargoNovo);
				if (serventiaGrupoDt != null && serventiaGrupoDt.getId() != null && serventiaGrupoDt.getId().length()>0) {
					//Criar histórico para o magistrado
					PendenciaResponsavelHistoricoDt pendenciaResponsavelHistoricoDt = new PendenciaResponsavelHistoricoDt();
					pendenciaResponsavelHistoricoDt.setId_Pendencia(pendenciaDt.getId());
					pendenciaResponsavelHistoricoDt.setDataInicio(data_Atual);
					pendenciaResponsavelHistoricoDt.setId_ServentiaCargo(id_ServentiaCargoNovo);
					pendenciaResponsavelHistoricoDt.setId_ServentiaGrupo(serventiaGrupoDt.getId());
					pendenciaResponsavelHistoricoDt.setId_UsuarioLog(pendenciaResponsavelNovoDt.getId_UsuarioLog());
					pendenciaResponsavelHistoricoDt.setIpComputadorLog(pendenciaResponsavelNovoDt.getIpComputadorLog());
					pendenciaResponsavelHistoricoNe.salvar(pendenciaResponsavelHistoricoDt, obFabricaConexao);
				}
			}
		
			 if (fabConexao == null) {
				 obFabricaConexao.finalizarTransacao();
			 }
	            
	        } catch (Exception e) {
	            // Se a conexao foi criada dentro do metodo, entao cancela a conexao
	            if (fabConexao == null) {
	            	obFabricaConexao.cancelarTransacao();
	            }
	            throw e;
	        } finally {
	            // Se a conexao foi criada dentro do metodo, entao finaliza a conexao
	            if (fabConexao == null) {
	            	obFabricaConexao.fecharConexao();
	            }
	        }
	}
	
	/**
	 * Redistribui uma pendencia (conclusão) para uma Unidade de Trabalho (Serventia Grupo).
	 * 
	 * @param PendenciaResponsavelDt: objeto com dados da troca de responsável a ser persistida
	 * @author lsbernardes
	 */
	public void reDistribuirConclusaoUnidadeTrabalho(PendenciaResponsavelDt pendenciaResponsavelNovoDt, UsuarioDt usuarioDt, PendenciaDt pendenciaDt, String id_ServentiaCargoNovoMagistrado) throws Exception{
	    FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			
			ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
			PendenciaResponsavelHistoricoNe pendenciaResponsavelHistoricoNe = new PendenciaResponsavelHistoricoNe();
			PendenciaResponsavelDt pendenciaResponsavelDtAtual = new PendenciaResponsavelDt();
			LogDt logDt = new LogDt(pendenciaResponsavelNovoDt.getId_UsuarioLog(), pendenciaResponsavelNovoDt.getIpComputadorLog());

			List listaResponsavelPendencia = this.consultarResponsaveisDetalhado(pendenciaResponsavelNovoDt.getId_Pendencia(), usuarioDt.getGrupoCodigo(), obFabricaConexao);
			if (listaResponsavelPendencia != null) 
				pendenciaResponsavelDtAtual = (PendenciaResponsavelDt) listaResponsavelPendencia.get(0);
			else 
				throw new MensagemException("Não foi possível Distribuir Conclusão.");
			
			boolean primeiraDistribuicao=false;
			if (this.consultarHistoricosPendencia(pendenciaDt.getId()).size() == 0){
				primeiraDistribuicao = true;
			}
			
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
            String data_Atual = df.format(new Date());

			if (usuarioDt.getGrupoCodigo() != null && (Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) != GrupoDt.DESEMBARGADOR && !primeiraDistribuicao)){
				//Fechar Historico
				pendenciaResponsavelHistoricoNe.fecharHistorico(pendenciaResponsavelNovoDt.getId_Pendencia(), data_Atual, pendenciaResponsavelNovoDt.getId_UsuarioLog(), pendenciaResponsavelNovoDt.getIpComputadorLog(), obFabricaConexao);
			}
			
			String id_ServentiaCargoNovo = "";
			if (pendenciaResponsavelNovoDt.isConclusoMagistrado()){
				id_ServentiaCargoNovo = id_ServentiaCargoNovoMagistrado;
				if (id_ServentiaCargoNovo == null || id_ServentiaCargoNovo.length() == 0) {
					if (pendenciaDt != null && pendenciaDt.getPendenciaTipoCodigo() != null && pendenciaDt.getPendenciaTipoCodigo().trim().length() > 0) {
						if (Funcoes.StringToInt(pendenciaDt.getPendenciaTipoCodigo()) == PendenciaTipoDt.CONCLUSO_PRESIDENTE_TJGO) {
							ServentiaCargoDt serventiaCargoPresidenteTJGO = serventiaCargoNe.getPresidenteTribunalDeJustica(obFabricaConexao);
		        			if (serventiaCargoPresidenteTJGO != null && serventiaCargoPresidenteTJGO.getId().length() > 0){
		        				id_ServentiaCargoNovo = serventiaCargoPresidenteTJGO.getId();
		        			}
		    			 } else if (Funcoes.StringToInt(pendenciaDt.getPendenciaTipoCodigo()) == PendenciaTipoDt.CONCLUSO_VICE_PRESIDENTE_TJGO) {
		    				 ServentiaCargoDt serventiaCargoVicePresidente = serventiaCargoNe.getVicePresidenteTribunalDeJustica(obFabricaConexao);
		    				 if (serventiaCargoVicePresidente != null && serventiaCargoVicePresidente.getId().length() > 0){
			        			id_ServentiaCargoNovo = serventiaCargoVicePresidente.getId();
		        			 }
		    			 } else if (Funcoes.StringToInt(pendenciaDt.getPendenciaTipoCodigo()) == PendenciaTipoDt.CONCLUSO_PRESIDENTE) {
		    				 ServentiaCargoDt serventiaCargoPresidente = serventiaCargoNe.getPresidenteSegundoGrau(usuarioDt.getId_Serventia(), obFabricaConexao);
		    				 if (serventiaCargoPresidente != null && serventiaCargoPresidente.getId().length() > 0){
		    					 id_ServentiaCargoNovo = serventiaCargoPresidente.getId();
		    				 }
		    			 }
					}
					
					if (id_ServentiaCargoNovo == null || id_ServentiaCargoNovo.trim().length() == 0) {
						id_ServentiaCargoNovo = serventiaCargoNe.getDesembargadorResponsavel(usuarioDt.getId_Serventia(), pendenciaDt.getId_Processo(), obFabricaConexao);
					}
					
					if (id_ServentiaCargoNovo == null || id_ServentiaCargoNovo.trim().length() == 0) {
						id_ServentiaCargoNovo = serventiaCargoNe.getJuizUPJResponsavel(usuarioDt.getId_Serventia(), pendenciaDt.getId_Processo(), obFabricaConexao);
					}
					
					if (id_ServentiaCargoNovo == null || id_ServentiaCargoNovo.trim().length() == 0) {
						String codServentiaSubTipoCodigo = new ServentiaNe().consultarServentiaSubTipoCodigo(usuarioDt.getId_Serventia(), obFabricaConexao);
						ServentiaCargoDt serventiaCargoRelator2Grau = new ProcessoResponsavelNe().consultarRelator2GrauConsideraSubstituicao(pendenciaDt.getId_Processo(), usuarioDt.getId_Serventia(), codServentiaSubTipoCodigo, obFabricaConexao);
						if (serventiaCargoRelator2Grau != null && serventiaCargoRelator2Grau.getId().length() > 0){
							id_ServentiaCargoNovo = serventiaCargoRelator2Grau.getId();
		   				}
					}
				}
			
			} else if (pendenciaResponsavelNovoDt.isSemRegra()){
				id_ServentiaCargoNovo = pendenciaResponsavelNovoDt.getId_ServentiaCargo();
			
			} else {
				if (pendenciaDt.getId_ServentiaGrupo().equalsIgnoreCase(pendenciaResponsavelNovoDt.getId_ServentiaGrupo())) {
					id_ServentiaCargoNovo  = serventiaCargoNe.consultarServentiaGrupoReDistribuicao(pendenciaResponsavelNovoDt.getId_ServentiaGrupo(),pendenciaResponsavelDtAtual.getId_ServentiaCargo());
				} else{
					String id_ServentiaCargoPrevencao = serventiaCargoNe.consultarPrevensaoServentiaGrupo(pendenciaResponsavelNovoDt.getId_ServentiaGrupo(), pendenciaResponsavelNovoDt.getId_Pendencia());
					if (id_ServentiaCargoPrevencao != null && id_ServentiaCargoPrevencao.length()>0)
						id_ServentiaCargoNovo = id_ServentiaCargoPrevencao;
					else
						id_ServentiaCargoNovo  = serventiaCargoNe.consultarServentiaGrupoDistribuicao(pendenciaResponsavelNovoDt.getId_ServentiaGrupo());
				}
			}
			
			if (id_ServentiaCargoNovo == null || id_ServentiaCargoNovo.length() == 0)
				throw new MensagemException("Não foi encontrado Cargo");			
			
			//altera o responsável da pendência
			this.alterarResponsavelPendencia(pendenciaResponsavelNovoDt.getId_Pendencia(), pendenciaResponsavelDtAtual.getId_ServentiaCargo(), id_ServentiaCargoNovo, logDt, obFabricaConexao);
			
			//alterar o responsável da ementa se a mesma existir
			if (pendenciaResponsavelNovoDt.getId_pendencia_Ementa() != null && pendenciaResponsavelNovoDt.getId_pendencia_Ementa().length()>0){
				this.alterarResponsavelPendencia(pendenciaResponsavelNovoDt.getId_pendencia_Ementa(), pendenciaResponsavelDtAtual.getId_ServentiaCargo(), id_ServentiaCargoNovo, logDt, obFabricaConexao);
			}
			
			
			if (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.DISTRIBUIDOR_GABINETE && primeiraDistribuicao){
				
				ServentiaGrupoDt serventiaGrupoDt = this.consultarSeventiaGrupoDistribuidorId(usuarioDt.getId_ServentiaCargo());
				if (serventiaGrupoDt != null && serventiaGrupoDt.getId() != null && serventiaGrupoDt.getId().length()>0) {
					//Criar histórico de distribução pelo Distribuidor que realizou a operacao
					PendenciaResponsavelHistoricoDt pendenciaResponsavelHistoricoDistribuidorDt   = new PendenciaResponsavelHistoricoDt();
					pendenciaResponsavelHistoricoDistribuidorDt.setId_Pendencia(pendenciaResponsavelNovoDt.getId_Pendencia());
					pendenciaResponsavelHistoricoDistribuidorDt.setDataInicio(pendenciaDt.getDataInicio());
					pendenciaResponsavelHistoricoDistribuidorDt.setDataFim(data_Atual);
					pendenciaResponsavelHistoricoDistribuidorDt.setId_ServentiaCargo(usuarioDt.getId_ServentiaCargo());
					pendenciaResponsavelHistoricoDistribuidorDt.setId_ServentiaGrupo(serventiaGrupoDt.getId());
					pendenciaResponsavelHistoricoDistribuidorDt.setId_UsuarioLog(pendenciaResponsavelNovoDt.getId_UsuarioLog());
					pendenciaResponsavelHistoricoDistribuidorDt.setIpComputadorLog(pendenciaResponsavelNovoDt.getIpComputadorLog());
					pendenciaResponsavelHistoricoNe.salvar(pendenciaResponsavelHistoricoDistribuidorDt, obFabricaConexao);
				}
			}
		
			if (!pendenciaResponsavelNovoDt.isConclusoMagistrado()){
				//Criar histórico
				PendenciaResponsavelHistoricoDt pendenciaResponsavelHistoricoDt = new PendenciaResponsavelHistoricoDt();
				pendenciaResponsavelHistoricoDt.setId_Pendencia(pendenciaResponsavelNovoDt.getId_Pendencia());
				pendenciaResponsavelHistoricoDt.setDataInicio(data_Atual);
				pendenciaResponsavelHistoricoDt.setId_ServentiaCargo(id_ServentiaCargoNovo);
				pendenciaResponsavelHistoricoDt.setId_ServentiaGrupo(pendenciaResponsavelNovoDt.getId_ServentiaGrupo());
				pendenciaResponsavelHistoricoDt.setId_UsuarioLog(pendenciaResponsavelNovoDt.getId_UsuarioLog());
				pendenciaResponsavelHistoricoDt.setIpComputadorLog(pendenciaResponsavelNovoDt.getIpComputadorLog());
				pendenciaResponsavelHistoricoNe.salvar(pendenciaResponsavelHistoricoDt, obFabricaConexao);
			} else {
				
				ServentiaGrupoDt serventiaGrupoDt = this.consultarSeventiaGrupoMagistradoId(id_ServentiaCargoNovo);
				if (serventiaGrupoDt != null && serventiaGrupoDt.getId() != null && serventiaGrupoDt.getId().length()>0) {
					//Criar histórico para o magistrado
					PendenciaResponsavelHistoricoDt pendenciaResponsavelHistoricoDt = new PendenciaResponsavelHistoricoDt();
					pendenciaResponsavelHistoricoDt.setId_Pendencia(pendenciaResponsavelNovoDt.getId_Pendencia());
					pendenciaResponsavelHistoricoDt.setDataInicio(data_Atual);
					pendenciaResponsavelHistoricoDt.setId_ServentiaCargo(id_ServentiaCargoNovo);
					pendenciaResponsavelHistoricoDt.setId_ServentiaGrupo(serventiaGrupoDt.getId());
					pendenciaResponsavelHistoricoDt.setId_UsuarioLog(pendenciaResponsavelNovoDt.getId_UsuarioLog());
					pendenciaResponsavelHistoricoDt.setIpComputadorLog(pendenciaResponsavelNovoDt.getIpComputadorLog());
					pendenciaResponsavelHistoricoNe.salvar(pendenciaResponsavelHistoricoDt, obFabricaConexao);
				}
			}
		
			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Salva Troca de Responsável de uma intimação ou citação.
	 * 
	 * @param PendenciaResponsavelDt: objeto com dados da troca de responsável a ser persistida
	 * @author lsbernardes
	 */
	public void salvarTrocaResponsavelPendencia(PendenciaResponsavelDt pendenciaResponsavelNovoDt, String Id_UsuarioServentiaAtural, String grupoCodigo, String idProcesso) throws Exception {
	    FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			
			PendenciaNe pendenciaNe = new PendenciaNe();

			LogDt logDt = new LogDt(pendenciaResponsavelNovoDt.getId_UsuarioLog(), pendenciaResponsavelNovoDt.getIpComputadorLog());
			
			this.alterarResponsavelPendenciaIntimacaoCitacao(pendenciaResponsavelNovoDt.getId_Pendencia(), Id_UsuarioServentiaAtural, pendenciaResponsavelNovoDt.getId_UsuarioResponsavel(), logDt, obFabricaConexao);

			
			int inGrupoCodigo = Funcoes.StringToInt(grupoCodigo);
			if ( inGrupoCodigo== GrupoDt.COORDENADOR_PROCURADORIA_ESTADUAL 
					 || inGrupoCodigo == GrupoDt.COORDENADOR_PROCURADORIA_MUNICIPAL
					 || inGrupoCodigo == GrupoDt.COORDENADOR_PROCURADORIA_FEDERAL
					 || inGrupoCodigo == GrupoDt.COORDENADOR_DEFENSORIA_PUBLICA
					 || inGrupoCodigo == GrupoDt.COORDENADOR_ESCRITORIO_JURIDICO
					 || inGrupoCodigo == GrupoDt.COORDENADOR_ADVOCACIA_PUBLICA){
				pendenciaNe.marcarIntimacaoCitacaoDistribuida(pendenciaResponsavelNovoDt.getId_Pendencia(), logDt, obFabricaConexao);
			}
			
			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Salva Troca de Responsável de uma conclusão
	 * 
	 * @param PendenciaResponsavelDt: objeto com dados da troca de responsável a ser persistida
	 * @author lsbernardes
	 */
	public void salvarTrocaResponsavelPendenciaConclusao(PendenciaResponsavelDt pendenciaResponsavelNovoDt, String id_UsuarioServentiaCargoAtual, String id_Processo) throws Exception {
	    FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			PendenciaResponsavelPs obPersistencia = new PendenciaResponsavelPs(obFabricaConexao.getConexao());

			if (pendenciaResponsavelNovoDt.isNovoResponsavel()){
				String valorAtual = "[Id_Pendencia:" + pendenciaResponsavelNovoDt.getId_Pendencia() + ";Id_UsuarioServentiaCargo:" + id_UsuarioServentiaCargoAtual + "]";
				String valorNovo = "[Id_Pendencia:" + pendenciaResponsavelNovoDt.getId_Pendencia() + ";Id_UsuarioServentiaCargo:" + pendenciaResponsavelNovoDt.getId_ServentiaCargo() + "]";
				
				LogDt obLogDt = new LogDt("PendenciaResponsavel", pendenciaResponsavelNovoDt.getId_Pendencia(), pendenciaResponsavelNovoDt.getId_UsuarioLog(), pendenciaResponsavelNovoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.TrocaResponsavelPendencia), valorAtual, valorNovo);
				obPersistencia.inserir(pendenciaResponsavelNovoDt);				
				obLog.salvar(obLogDt, obFabricaConexao);
				
				ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
				ServentiaCargoDt serventiaCargoNovo = serventiaCargoNe.consultarServentiaCargoId( pendenciaResponsavelNovoDt.getId_ServentiaCargo());
				
				if (serventiaCargoNovo != null && serventiaCargoNovo.getCargoTipoCodigo() != null 
						&& Funcoes.StringToInt(serventiaCargoNovo.getCargoTipoCodigo()) == CargoTipoDt.ASSISTENTE_GABINETE){
					ProcessoResponsavelNe processoNe = new ProcessoResponsavelNe();
					
					if (!processoNe.isResponsavelProcesso(serventiaCargoNovo.getId(), id_Processo, obFabricaConexao)){
						LogDt logDt = new LogDt(pendenciaResponsavelNovoDt.getId_UsuarioLog(), pendenciaResponsavelNovoDt.getIpComputadorLog());
						processoNe.salvarProcessoResponsavel(id_Processo, pendenciaResponsavelNovoDt.getId_ServentiaCargo(), false, CargoTipoDt.ASSISTENTE_GABINETE, logDt, obFabricaConexao);
					}
				}
				
			} else {
				String valorAtual = "[Id_Pendencia:" + pendenciaResponsavelNovoDt.getId_Pendencia() + ";Id_UsuarioServentiaCargo:" + id_UsuarioServentiaCargoAtual + "]";
				String valorNovo = "[Id_Pendencia:" + pendenciaResponsavelNovoDt.getId_Pendencia() + ";Id_UsuarioServentiaCargo:" + pendenciaResponsavelNovoDt.getId_ServentiaCargo() + "]";
	
				LogDt obLogDt = new LogDt("PendenciaResponsavel", pendenciaResponsavelNovoDt.getId_Pendencia(), pendenciaResponsavelNovoDt.getId_UsuarioLog(), pendenciaResponsavelNovoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.TrocaResponsavelPendencia), valorAtual, valorNovo);
				obPersistencia.alterarServentiaCargoResponsavel(pendenciaResponsavelNovoDt.getId_Pendencia(), id_UsuarioServentiaCargoAtual, pendenciaResponsavelNovoDt.getId_ServentiaCargo());

				PendenciaNe pendenciaNe = new PendenciaNe();
				PendenciaDt pendenciaDt = pendenciaNe.consultarId(pendenciaResponsavelNovoDt.getId_Pendencia());
				
				//Quando a pendência for CONCLUSO RELATOR, será feita a limpeza de todos os responsáveis e deixar
				//somente o desembargador e distribuidor do gabinete de destino. 
				if(pendenciaDt.getPendenciaTipoCodigo().equals(String.valueOf(PendenciaTipoDt.CONCLUSO_RELATOR))) {
					ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
					ServentiaCargoDt serventiaCargoAtual = serventiaCargoNe.consultarServentiaCargoId(id_UsuarioServentiaCargoAtual);
					ServentiaCargoDt serventiaCargoNovo = serventiaCargoNe.consultarServentiaCargoId(pendenciaResponsavelNovoDt.getId_ServentiaCargo());
	
					//Se estiver trocando para um responsável de outro gabinete, é preciso corrigir os responsáveis pela pendência
					PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
					if(!serventiaCargoAtual.getId_Serventia().equals(serventiaCargoNovo.getId_Serventia())){
						List listaResponsaveisPendencia = this.consultarResponsaveisDetalhado(pendenciaResponsavelNovoDt.getId_Pendencia(), String.valueOf(GrupoDt.DISTRIBUIDOR_GABINETE));
						for (int i = 0; i < listaResponsaveisPendencia.size(); i++) {
							PendenciaResponsavelDt responsavelDt = (PendenciaResponsavelDt)listaResponsaveisPendencia.get(i);
							if(i == 0){
								//se for a primeira execução, troca o responsável pelo distribuidor do novo gabinete
								ServentiaCargoDt distribuidorDt = serventiaCargoNe.getDistribuidorGabinete(serventiaCargoNovo.getId_Serventia(), obFabricaConexao);
								obPersistencia.alterarServentiaCargoResponsavel(pendenciaResponsavelNovoDt.getId_Pendencia(), responsavelDt.getId_ServentiaCargo(), distribuidorDt.getId());
							} else {
								//se for segunda (ou após) execução, desabilita o responsável pela pendência
								pendenciaResponsavelNe.excluir(responsavelDt);
							}
						}
					}
				}
				obLog.salvar(obLogDt, obFabricaConexao);
			}

			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Salva Troca de Responsável de uma conclusão
	 * 
	 * @param PendenciaResponsavelDt: objeto com dados da troca de responsável a ser persistida
	 * @author lsbernardes
	 */
	public void salvarTrocaResponsavelPendenciaConclusaoLote(String id_UsuarioServentiaCargoAtual, String id_UsuarioServentiaCargoNovo, String quantidade, String id_UsuarioLog, String ip_Computador) throws Exception {
	    FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			
			List listaConclusoesMagistrado = new PendenciaNe().consultarConclusoesMaisAntigasMagistado(id_UsuarioServentiaCargoAtual, quantidade, obFabricaConexao);
			
			PendenciaResponsavelPs obPersistencia = new PendenciaResponsavelPs(obFabricaConexao.getConexao());
			
			if (listaConclusoesMagistrado != null && listaConclusoesMagistrado.size()>0){
				for (Iterator iterator = listaConclusoesMagistrado.iterator(); iterator.hasNext();) {
					PendenciaDt pendenciaDt = (PendenciaDt) iterator.next();
					
					String valorAtual = "[Id_Pendencia:" + pendenciaDt.getId() + ";Id_UsuarioServentiaCargo:" + id_UsuarioServentiaCargoAtual + "]";
					String valorNovo = "[Id_Pendencia:" +  pendenciaDt.getId() + ";Id_UsuarioServentiaCargo:" + id_UsuarioServentiaCargoNovo + "]";
		
					LogDt obLogDt = new LogDt("PendenciaResponsavel",  pendenciaDt.getId(), id_UsuarioLog, ip_Computador, String.valueOf(LogTipoDt.TrocaResponsavelPendencia), valorAtual, valorNovo);
					obPersistencia.alterarServentiaCargoResponsavel( pendenciaDt.getId(), id_UsuarioServentiaCargoAtual, id_UsuarioServentiaCargoNovo);
						
					obLog.salvar(obLogDt, obFabricaConexao);
				} 
			} else{
				throw new MensagemException("Erro ao buscar conclusões. Conclusões do magistrado não localizadas.");
			}

			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Método responsável por realizar a troca do responsável por uma conclusão de primeiro grau.
	 * @param idConclusao - id da pendência
	 * @param id_ServentiaCargoNovoResponsavel - id do serventia cargo do novo responsável
	 * @param idProcesso - id do processo
	 * @param usuarioSessao - usuário da sessão
	 * @throws Exception
	 * @author hmgodinho
	 */
	public void trocarResponsavelConclusaoProcessoPrimeiroGrau(String idConclusao, String id_ServentiaCargoNovoResponsavel, String idProcesso, UsuarioNe usuarioSessao) throws Exception {
	
		List listaResponsaveis = new PendenciaResponsavelNe().consultarResponsaveis(idConclusao);
		ServentiaCargoDt servCargoAtualResponsavelDt = new ServentiaCargoDt();
		String id_ServentiaCargoAtual = null;
		for (int i = 0; i < listaResponsaveis.size(); i++) {
			PendenciaResponsavelDt responsavelDt = (PendenciaResponsavelDt)listaResponsaveis.get(i);
			if(responsavelDt.getId_ServentiaCargo() != null && !responsavelDt.getId_ServentiaCargo().equalsIgnoreCase("")) {
				servCargoAtualResponsavelDt = new ServentiaCargoNe().consultarId(responsavelDt.getId_ServentiaCargo());
				id_ServentiaCargoAtual = servCargoAtualResponsavelDt.getId();
			}
		}
		
		PendenciaResponsavelDt novoResponsavelDt = new PendenciaResponsavelDt();
		novoResponsavelDt.setId_Pendencia(idConclusao);
		novoResponsavelDt.setId_ServentiaCargo(id_ServentiaCargoNovoResponsavel);
		novoResponsavelDt.setId_UsuarioLog(usuarioSessao.getId_Usuario());
		novoResponsavelDt.setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());
		ServentiaCargoDt servCargoNovoResponsavelDt = new ServentiaCargoNe().consultarId(novoResponsavelDt.getId_ServentiaCargo());
		novoResponsavelDt.setNomeUsuarioServentiaCargo(servCargoNovoResponsavelDt.getNomeUsuario());
		
		String mensagem = this.verificarTrocaResponsavelConclusao(id_ServentiaCargoAtual, id_ServentiaCargoNovoResponsavel, idConclusao);
		if(mensagem.length() > 0) {
			throw new MensagemException(mensagem);
		}
	
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		if(servCargoNovoResponsavelDt.isJuizUPJ()){
			boolean gerarHistoricoPendencia = false;
			try{
				ServentiaCargoDt servCargoResponsavelGabinete = null;
				servCargoResponsavelGabinete = new ServentiaCargoNe().consultarServentiaCargpoDistribuicaoGabineteFluxo(servCargoNovoResponsavelDt.getId_Serventia(), CargoTipoDt.DISTRIBUIDOR_GABINETE, obFabricaConexao);
				if (servCargoResponsavelGabinete==null) {
					servCargoResponsavelGabinete = new ServentiaCargoNe().consultarServentiaCargpoDistribuicaoGabineteFluxo(servCargoNovoResponsavelDt.getId_Serventia(), CargoTipoDt.ASSISTENTE_GABINETE_FLUXO, obFabricaConexao);
					if (servCargoResponsavelGabinete==null) {
						servCargoResponsavelGabinete = new ServentiaCargoNe().consultarServentiaCargpoDistribuicaoGabineteFluxo(servCargoNovoResponsavelDt.getId_Serventia(), CargoTipoDt.JUIZ_UPJ, obFabricaConexao);	
						if(servCargoResponsavelGabinete == null) {
							throw new MensagemException("Gabinete não possui responsável cadastrado para receber conclusões.");
						}
					} 
					gerarHistoricoPendencia = true;
				}
				
				novoResponsavelDt.setId_ServentiaCargo(servCargoResponsavelGabinete.getId());
				novoResponsavelDt.setNomeUsuarioServentiaCargo(servCargoResponsavelGabinete.getNomeUsuario());
				novoResponsavelDt.setId_ServentiaGrupo(servCargoResponsavelGabinete.getId_ServentiaGrupo());
				
				this.salvarTrocaResponsavelPendenciaConclusao(novoResponsavelDt, id_ServentiaCargoAtual, idProcesso);
					
				SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
				String data_Atual = df.format(new Date());
				PendenciaResponsavelHistoricoDt historicoAtivoPendienciaDt =  new PendenciaResponsavelHistoricoNe().consultarHistoricoAbertoPendencia(novoResponsavelDt.getId_Pendencia());
				if(historicoAtivoPendienciaDt != null) {
					//Se a conclusão tiver histórico a ser fechado. 
					new PendenciaResponsavelHistoricoNe().fecharHistorico(novoResponsavelDt.getId_Pendencia(), data_Atual, usuarioSessao.getUsuarioDt().getId(), usuarioSessao.getUsuarioDt().getIpComputadorLog(), obFabricaConexao);
				}
				if(gerarHistoricoPendencia){
				    new PendenciaResponsavelHistoricoNe().salvarHistorioco(novoResponsavelDt.getId_Pendencia(), data_Atual, novoResponsavelDt.getId_ServentiaCargo(), novoResponsavelDt.getId_ServentiaGrupo(), usuarioSessao.getUsuarioDt().getId(), usuarioSessao.getUsuarioDt().getIpComputadorLog(),obFabricaConexao);
				}
			} finally{
				obFabricaConexao.fecharConexao();
			}
		} else {
			this.salvarTrocaResponsavelPendenciaConclusao(novoResponsavelDt, id_ServentiaCargoAtual, idProcesso);
		}
		
}
	
	/**
	 * Marcar intimação como Distribuída.
	 * 
	 * @param String id_Pendencia, identificador da pendência
	 * @param String grupoCodigo, código do grupo do usuário logado
	 * @param String id_UsuarioLog, identificador do usuário logado
	 * @param String ip_ComputadorLog, IP do computador do usuário logado
	 * @author lsbernardes
	 */
	public void marcarIntimacaoDistribuida(String id_Pendencia, String grupoCodigo, String id_UsuarioLog, String ip_ComputadorLog) throws Exception {
	    FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			
			PendenciaNe pendenciaNe = new PendenciaNe();

			LogDt logDt = new LogDt(id_UsuarioLog, ip_ComputadorLog);
			
			int inGrupoCodigo = Funcoes.StringToInt(grupoCodigo);
			if ( inGrupoCodigo== GrupoDt.COORDENADOR_PROCURADORIA_ESTADUAL 
					 || inGrupoCodigo == GrupoDt.COORDENADOR_PROCURADORIA_MUNICIPAL
					 || inGrupoCodigo == GrupoDt.COORDENADOR_PROCURADORIA_FEDERAL
					 || inGrupoCodigo == GrupoDt.COORDENADOR_DEFENSORIA_PUBLICA
					 || inGrupoCodigo == GrupoDt.COORDENADOR_ESCRITORIO_JURIDICO
					 || inGrupoCodigo == GrupoDt.COORDENADOR_ADVOCACIA_PUBLICA){
				pendenciaNe.marcarIntimacaoCitacaoDistribuida(id_Pendencia, logDt, obFabricaConexao);
			}
			
			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	
	
	/**
	 * Salva Troca de Responsável de uma pendencia.
	 * 
	 * @param PendenciaResponsavelDt: objeto com dados da troca de responsável a ser persistida
	 * @author lsbernardes
	 */
	public void salvarTrocaResponsavelPendencia(PendenciaResponsavelDt pendenciaResponsavelNovoDt, String grupoCodigo) throws Exception {
	    FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			
			PendenciaResponsavelDt pendenciaResponsavelDtAtual = new PendenciaResponsavelDt();

			LogDt logDt = new LogDt(pendenciaResponsavelNovoDt.getId_UsuarioLog(), pendenciaResponsavelNovoDt.getIpComputadorLog());

			List listaResponsavelPendencia = this.consultarResponsaveisDetalhado(pendenciaResponsavelNovoDt.getId_Pendencia(), grupoCodigo, obFabricaConexao);
			if (listaResponsavelPendencia != null && listaResponsavelPendencia.size() == 1) 
				pendenciaResponsavelDtAtual = (PendenciaResponsavelDt) listaResponsavelPendencia.get(0);
			else 
				throw new MensagemException("Não foi possivel trocar responsável da pendência.");

			this.alterarResponsavelPendencia(pendenciaResponsavelNovoDt.getId_Pendencia(), pendenciaResponsavelDtAtual.getId_ServentiaCargo(), pendenciaResponsavelNovoDt.getId_ServentiaCargo(), logDt, obFabricaConexao);

			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Salva Troca de Responsável de todas as pendencias abertas de um determinado processo e serventia cargo.
	 * 
	 * @param String id_Processo : id do processo que tera os responsáveis das pendencias abertas atualizados
	 * @param String id_ServentiaCargoAtual: id do serventiaCargo que sera atualizado por um novo
	 * @param String id_ServentiaCargoNovo: id do novo serventia cargo
	 * @param LogDt logDt, log do usuario
	 * @param FabricaConexao fabrica, conexao
	 * 
	 * @author lsbernardes
	 */
	public void atualizarResponsaveisPendenciasProcesso(String id_Processo, String id_ServentiaCargoAtual, String id_ServentiaCargoNovo, LogDt logDt, FabricaConexao fabrica) throws Exception {
	    FabricaConexao obFabricaConexao = null;
		try{

			if (fabrica == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				obFabricaConexao.iniciarTransacao();
			} else {
				obFabricaConexao = fabrica;
			}			

			PendenciaNe pendenciaNe = new PendenciaNe();

			List listaPendenciasAbertasProcesso = pendenciaNe.consultarPendenciasAbertasProcesso(id_Processo, id_ServentiaCargoAtual);

			for (int i = 0; i < listaPendenciasAbertasProcesso.size(); i++) {
				PendenciaDt pendenciaDt = (PendenciaDt) listaPendenciasAbertasProcesso.get(i);
				this.alterarResponsavelPendencia(pendenciaDt.getId(), id_ServentiaCargoAtual, id_ServentiaCargoNovo, logDt, obFabricaConexao);
			}

			if (fabrica == null) obFabricaConexao.finalizarTransacao();

		} catch(Exception e) {
			if (fabrica == null) obFabricaConexao.cancelarTransacao();

			throw e;
		} finally{
			if (fabrica == null) obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Salva Troca de Responsável de todas as pendencias abertas de um determinado processo e serventia cargo
	 * com exceção das pendências do tipo concluso ao presidente do tjgo e concluso ao vice presidente do tjgo.
	 * Este método foi copiado e alterado para atender às necessidades da funcionalidade do distribuidor adicionando
	 * a exceção do president e vice presidente sem alterar a regra para as outras funcionalidades do código.
	 * 
	 * @param String id_Processo : id do processo que tera os responsáveis das pendencias abertas atualizados
	 * @param String id_ServentiaCargoAtual: id do serventiaCargo que sera atualizado por um novo
	 * @param String id_ServentiaCargoNovo: id do novo serventia cargo
	 * @param LogDt logDt, log do usuario
	 * @param FabricaConexao fabrica, conexao
	 * 
	 * @author lsbernardes, hrrosa
	 */
	public void atualizarResponsaveisPendenciasProcessoDistribuidor(String id_Processo, String id_ServentiaCargoAtual, String id_ServentiaCargoNovo, LogDt logDt, FabricaConexao fabrica) throws Exception {
	    FabricaConexao obFabricaConexao = null;
		try{

			if (fabrica == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				obFabricaConexao.iniciarTransacao();
			} else {
				obFabricaConexao = fabrica;
			}			

			PendenciaNe pendenciaNe = new PendenciaNe();

			List listaPendenciasAbertasProcesso = pendenciaNe.consultarPendenciasAbertasProcesso(id_Processo, id_ServentiaCargoAtual);

			for (int i = 0; i < listaPendenciasAbertasProcesso.size(); i++) {
				PendenciaDt pendenciaDt = (PendenciaDt) listaPendenciasAbertasProcesso.get(i);
				if( Funcoes.StringToInt(pendenciaDt.getPendenciaTipoCodigo()) != PendenciaTipoDt.CONCLUSO_PRESIDENTE_TJGO &&
				    Funcoes.StringToInt(pendenciaDt.getPendenciaTipoCodigo()) != PendenciaTipoDt.CONCLUSO_VICE_PRESIDENTE_TJGO ) {
						this.alterarResponsavelPendencia(pendenciaDt.getId(), id_ServentiaCargoAtual, id_ServentiaCargoNovo, logDt, obFabricaConexao);
				}
			}

			if (fabrica == null) obFabricaConexao.finalizarTransacao();

		} catch(Exception e) {
			if (fabrica == null) obFabricaConexao.cancelarTransacao();

			throw e;
		} finally{
			if (fabrica == null) obFabricaConexao.fecharConexao();
		}
	}	
	
	/**
	 * Salva Troca de Responsável de todas as pendencias abertas de um determinado processo e serventia cargo de uma promotoria.
	 * 
	 * @param String id_Processo : id do processo que tera os responsáveis das pendencias abertas atualizados
	 * @param String id_ServentiaCargoAtual: id do serventiaCargo que sera atualizado por um novo
	 * @param String id_ServentiaCargoNovo: id do novo serventia cargo
	 * @param String grupoCodigo: código do grupo do usuário logado
	 * @param LogDt logDt, log do usuario
	 * @param FabricaConexao fabrica, conexao
	 * 
	 * @author lsbernardes
	 */
	public void atualizarResponsaveisPendenciasProcessoPromotoria(String id_Processo, String id_ServentiaCargoAtual, String id_ServentiaCargoNovo, String grupoCodigo,  LogDt logDt, FabricaConexao fabrica) throws Exception {
	    FabricaConexao obFabricaConexao = null;
		try{

			if (fabrica == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				obFabricaConexao.iniciarTransacao();
			} else {
				obFabricaConexao = fabrica;
			}			

			PendenciaNe pendenciaNe = new PendenciaNe();

			List listaPendenciasAbertasProcesso = pendenciaNe.consultarPendenciasAbertasProcesso(id_Processo, id_ServentiaCargoAtual);

			for (int i = 0; i < listaPendenciasAbertasProcesso.size(); i++) {
				PendenciaDt pendenciaDt = (PendenciaDt) listaPendenciasAbertasProcesso.get(i);
				this.alterarResponsavelPendencia(pendenciaDt.getId(), id_ServentiaCargoAtual, id_ServentiaCargoNovo, logDt, obFabricaConexao);
				
				if (grupoCodigo != null && !grupoCodigo.equals("") && Funcoes.StringToInt(grupoCodigo) == GrupoDt.COORDENADOR_PROMOTORIA){
					pendenciaNe.marcarIntimacaoCitacaoDistribuida(pendenciaDt.getId(), logDt, obFabricaConexao);
				}
			}

			if (fabrica == null) obFabricaConexao.finalizarTransacao();

		} catch(Exception e) {
			if (fabrica == null) obFabricaConexao.cancelarTransacao();

			throw e;
		} finally{
			if (fabrica == null) obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Salva Troca de Responsável de todas as pendencias abertas de um determinado processo e usuario serventia de um promotor (substituto processual).
	 * 
	 * @param String id_Processo : id do processo que tera os responsáveis das pendencias abertas atualizados
	 * @param String id_UsuarioServentiaAtual: id do usuario serventia que sera atualizado por um novo
	 * @param String id_UsuarioServentiaNovo: id do novo usuario serventia
	 * @param String grupoCodigo: código do grupo do usuário logado
	 * @param LogDt logDt, log do usuario
	 * @param FabricaConexao fabrica, conexao
	 * 
	 * @author lsbernardes
	 */
	public void atualizarResponsaveisPendenciasProcessoPromotorSubstituto(String id_Processo, String id_UsuarioServentiaAtual, String id_UsuarioServentiaNovo, String grupoCodigo,  LogDt logDt, FabricaConexao fabrica) throws Exception {
	    FabricaConexao obFabricaConexao = null;
		try{

			if (fabrica == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				obFabricaConexao.iniciarTransacao();
			} else {
				obFabricaConexao = fabrica;
			}			

			PendenciaNe pendenciaNe = new PendenciaNe();

			List listaPendenciasAbertasProcesso = pendenciaNe.consultarIntimacoesPromotorSubstitutoProcessual(id_Processo, id_UsuarioServentiaAtual);

			for (int i = 0; i < listaPendenciasAbertasProcesso.size(); i++) {
				PendenciaDt pendenciaDt = (PendenciaDt) listaPendenciasAbertasProcesso.get(i);
				this.alterarResponsavelUsuarioServentiaPendencia(pendenciaDt.getId(), id_UsuarioServentiaAtual, id_UsuarioServentiaNovo, logDt, obFabricaConexao);
				
				if (grupoCodigo != null && !grupoCodigo.equals("") && Funcoes.StringToInt(grupoCodigo) == GrupoDt.COORDENADOR_PROMOTORIA){
					pendenciaNe.marcarIntimacaoCitacaoDistribuida(pendenciaDt.getId(), logDt, obFabricaConexao);
				}
			}

			if (fabrica == null) obFabricaConexao.finalizarTransacao();

		} catch(Exception e) {
			if (fabrica == null) obFabricaConexao.cancelarTransacao();

			throw e;
		} finally{
			if (fabrica == null) obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Atualiza os dados de uma  PendenciaResponsavel em virtude de uma troca de responsável de uma intimação ou citação
	 * 
	 * @param String id_Processo : id do processo que tera os responsáveis das pendencias abertas atualizados
	 * @param String id_UsuarioServentiaAtual: id do usuario serventia que sera atualizado por um novo
	 * @param String id_UsuarioServentiaNovo: id do novo usuario serventia
	 * @param String grupoCodigo: código do grupo do usuário logado
	 * @param LogDt logDt, log do usuario
	 * @param FabricaConexao fabrica, conexao
	 * 
	 * @author lsbernardes
	 */
	public void atualizarResponsavelPendenciaIntimacaoCitacao(String id_Processo, String id_UsuarioServentiaAtual, String id_UsuarioServentiaNovo, String grupoCodigo,  LogDt logDt, FabricaConexao fabrica) throws Exception {
	    FabricaConexao obFabricaConexao = null;
		try{

			if (fabrica == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				obFabricaConexao.iniciarTransacao();
			} else {
				obFabricaConexao = fabrica;
			}			

			PendenciaNe pendenciaNe = new PendenciaNe();
			List listaPendenciasAbertasProcesso = pendenciaNe.consultarIntimacoesCitacoesUsuarioServentia(id_Processo, id_UsuarioServentiaAtual, obFabricaConexao);

			if (listaPendenciasAbertasProcesso != null && listaPendenciasAbertasProcesso.size() > 0){
				for (int i = 0; i < listaPendenciasAbertasProcesso.size(); i++) {
					PendenciaDt pendenciaDt = (PendenciaDt) listaPendenciasAbertasProcesso.get(i);
					
					this.alterarResponsavelPendenciaIntimacaoCitacao(pendenciaDt.getId(), id_UsuarioServentiaAtual, id_UsuarioServentiaNovo, logDt, obFabricaConexao);
					
					int inGrupoCodigo = Funcoes.StringToInt(grupoCodigo);
					if ( inGrupoCodigo== GrupoDt.COORDENADOR_PROCURADORIA_ESTADUAL 
							 || inGrupoCodigo == GrupoDt.COORDENADOR_PROCURADORIA_MUNICIPAL
							 || inGrupoCodigo == GrupoDt.COORDENADOR_PROCURADORIA_FEDERAL
							 || inGrupoCodigo == GrupoDt.COORDENADOR_DEFENSORIA_PUBLICA
							 || inGrupoCodigo == GrupoDt.COORDENADOR_ESCRITORIO_JURIDICO
							 || inGrupoCodigo == GrupoDt.COORDENADOR_ADVOCACIA_PUBLICA){
						pendenciaNe.marcarIntimacaoCitacaoDistribuida(pendenciaDt.getId(), logDt, obFabricaConexao);
					}
				}
			}

			if (fabrica == null) obFabricaConexao.finalizarTransacao();

		} catch(Exception e) {
			if (fabrica == null) obFabricaConexao.cancelarTransacao();

			throw e;
		} finally{
			if (fabrica == null) obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Atualiza o responsável por uma conclusão aberta de um determinado processo e serventia cargo.
	 * 
	 * @param String id_Processo: id do processo que tera o responsável pela conclusão modificado
	 * @param String id_ServentiaCargoAtual, id do serventiaCargo que sera atualizado por um novo
	 * @param String id_ServentiaCargoNovo, id do novo serventia cargo
	 * @param LogDt logDt, log do usuario
	 * @param FabricaConexao fabrica, conexao
	 * 
	 * @author msapaula
	 */
	public void atualizarResponsavelConclusao(String id_Processo, String id_ServentiaCargoAtual, String id_ServentiaCargoNovo, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
					
		PendenciaNe pendenciaNe = new PendenciaNe();
		PendenciaDt conclusao = pendenciaNe.consultarConclusaoAbertaProcesso(id_Processo, id_ServentiaCargoAtual, obFabricaConexao);

		if (conclusao != null) {
			this.alterarResponsavelPendencia(conclusao.getId(), id_ServentiaCargoAtual, id_ServentiaCargoNovo, logDt, obFabricaConexao);
		}

	}

	/**
	 * Atualiza os dados de uma PendenciaResponsavel em virtude de uma troca de responsável da pendencia
	 * 
	 * @param id_Pendencia, identificação da pendencia
	 * @param id_ResponsavelAnterior, responsável anterior
	 * @param id_NovoResponsavel, novo responsável
	 * @param logDt
	 * @param conexao
	 * @author lsbernardes
	 */
	public void alterarResponsavelPendencia(String id_Pendencia, String id_ResponsavelAnterior, String id_NovoResponsavel, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		
		PendenciaResponsavelPs obPersistencia = new PendenciaResponsavelPs(obFabricaConexao.getConexao());
		String valorAtual = "[Id_Pendencia:" + id_Pendencia + ";Id_ServentiaCargo:" + id_ResponsavelAnterior + "]";
		String valorNovo = "[Id_Pendencia:" + id_Pendencia + ";Id_ServentiaCargo:" + id_NovoResponsavel + "]";

		obLogDt = new LogDt("PendenciaResponsavel",id_Pendencia, logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.TrocaResponsavelPendencia), valorAtual, valorNovo);
		obPersistencia.alterarResponsavelPendencia(id_Pendencia, id_ResponsavelAnterior, id_NovoResponsavel);

		obLog.salvar(obLogDt, obFabricaConexao);		
	}
	
	/**
	 * Atualiza os dados de uma PendenciaResponsavel em virtude de uma troca de responsável da pendencia
	 * 
	 * @param id_Pendencia, identificação da pendencia
	 * @param id_ServentiaAnterior, responsável anterior
	 * @param id_NovaServentia, novo responsável
	 * @param logDt
	 * @param conexao
	 * @author lsbernardes
	 */
	public void alterarResponsavelPendenciaRedistribuicaoLote(String id_Pendencia, String id_Pend_Resp, String id_ServentiaAnterior, String id_NovaServentia, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		
		PendenciaResponsavelPs obPersistencia = new PendenciaResponsavelPs(obFabricaConexao.getConexao());
		String valorAtual = "[Id_Pendencia:" + id_Pendencia + ";ID_SERV:" + id_ServentiaAnterior + "]";
		String valorNovo = "[Id_Pendencia:" + id_Pendencia + ";ID_SERV:" + id_NovaServentia + "]";

		obLogDt = new LogDt("PendenciaResponsavel",id_Pendencia, logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.TrocaResponsavelPendencia), valorAtual, valorNovo);
		obPersistencia.alterarResponsavelPendenciaRedistribuicaoLote(id_Pendencia, id_Pend_Resp, id_ServentiaAnterior, id_NovaServentia);

		obLog.salvar(obLogDt, obFabricaConexao);
		
	}
	
	/**
	 * Atualiza os dados de uma PendenciaResponsavel em virtude de uma troca de responsável da pendencia
	 * 
	 * @param id_Pendencia, identificação da pendencia
	 * @param id_ResponsavelAnterior, responsável anterior
	 * @param id_NovoResponsavel, novo responsável
	 * @param logDt
	 * @param conexao
	 * @author lsbernardes
	 */
	public void alterarResponsavelUsuarioServentiaPendencia(String id_Pendencia, String id_ResponsavelAnterior, String id_NovoResponsavel, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		
		PendenciaResponsavelPs obPersistencia = new PendenciaResponsavelPs(obFabricaConexao.getConexao());
		String valorAtual = "[Id_Pendencia:" + id_Pendencia + ";Id_UsuarioServentia:" + id_ResponsavelAnterior + "]";
		String valorNovo = "[Id_Pendencia:" + id_Pendencia + ";Id_UsuarioServentia:" + id_NovoResponsavel + "]";

		obLogDt = new LogDt("PendenciaResponsavel",id_Pendencia, logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.TrocaResponsavelPendencia), valorAtual, valorNovo);
		obPersistencia.alterarResponsavelUsuarioServentiaPendencia(id_Pendencia, id_ResponsavelAnterior, id_NovoResponsavel);

		obLog.salvar(obLogDt, obFabricaConexao);
		
	}
	
	/**
	 * Atualiza os dados de uma  PendenciaResponsavel em virtude de uma troca de responsável de uma intimação ou citação
	 * 
	 * @param id_Pendencia, identificação da pendencia
	 * @param id_ResponsavelAnterior, responsável anterior
	 * @param id_NovoResponsavel, novo responsável
	 * @param logDt
	 * @param conexao
	 * @author lsbernardes
	 */
	public void alterarResponsavelPendenciaIntimacaoCitacao(String id_Pendencia, String id_ResponsavelAnterior, String id_NovoResponsavel, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		
		PendenciaResponsavelPs obPersistencia = new PendenciaResponsavelPs(obFabricaConexao.getConexao());
		String valorAtual = "[Id_Pendencia:" + id_Pendencia + ";Id_UsuarioServentia:" + id_ResponsavelAnterior + "]";
		String valorNovo = "[Id_Pendencia:" + id_Pendencia + ";Id_UsuarioServentia:" + id_NovoResponsavel + "]";

		obLogDt = new LogDt("PendenciaResponsavel",id_Pendencia, logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.TrocaResponsavelPendencia), valorAtual, valorNovo);
		obPersistencia.alterarResponsavelPendenciaIntimacaoCitacao(id_Pendencia, id_ResponsavelAnterior, id_NovoResponsavel);

		obLog.salvar(obLogDt, obFabricaConexao);
		
	}
	
	/**
	 * Atualiza os dados de uma PendenciaResponsavel em virtude de uma troca de responsável da pendencia
	 * 
	 * @param id_Pendencia, identificação da pendencia
	 * @param id_Responsavel, identificação do responsável 
	 * @param logDt
	 * @param conexao
	 * @author mmgomes
	 */
	public void retirarResponsavelPendencia(String id_Pendencia, String id_Responsavel, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		
		PendenciaResponsavelPs obPersistencia = new PendenciaResponsavelPs(obFabricaConexao.getConexao());
		String valorAtual = "[Id_Pendencia:" + id_Pendencia + ";Id_ServentiaCargo:" + id_Responsavel + "]";
		String valorNovo = "[Id_Pendencia:" + id_Pendencia + ";Id_ServentiaCargo:sem assistente]";

		obLogDt = new LogDt("PendenciaResponsavel",id_Pendencia, logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.TrocaResponsavelPendencia), valorAtual, valorNovo);
		obPersistencia.retirarResponsavelPendencia(id_Pendencia, id_Responsavel);

		obLog.salvar(obLogDt, obFabricaConexao);
		
	}
	
	/**
	 * Remover Responsável de todas as pendencias abertas de um determinado processo e serventia cargo.
	 * 
	 * @param String id_Processo : id do processo que tera os responsáveis das pendencias abertas atualizados
	 * @param String id_ServentiaCargoAtual: id do serventiaCargo que sera removido
	 * @param LogDt logDt, log do usuario
	 * @param FabricaConexao fabrica, conexao
	 * 
	 * @author mmgomes
	 */
	public void removerResponsaveisPendenciasProcesso(String id_Processo, String id_ServentiaCargo, LogDt logDt, FabricaConexao fabrica) throws Exception {
	    FabricaConexao obFabricaConexao = null;
		try{

			if (fabrica == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				obFabricaConexao.iniciarTransacao();
			} else {
				obFabricaConexao = fabrica;
			}			

			PendenciaNe pendenciaNe = new PendenciaNe();

			List listaPendenciasAbertasProcesso = pendenciaNe.consultarPendenciasAbertasProcesso(id_Processo, id_ServentiaCargo);

			for (int i = 0; i < listaPendenciasAbertasProcesso.size(); i++) {
				PendenciaDt pendenciaDt = (PendenciaDt) listaPendenciasAbertasProcesso.get(i);
				this.retirarResponsavelPendencia(pendenciaDt.getId(), id_ServentiaCargo, logDt, obFabricaConexao);
			}

			if (fabrica == null) obFabricaConexao.finalizarTransacao();

		} catch(Exception e) {
			if (fabrica == null) obFabricaConexao.cancelarTransacao();

			throw e;
		} finally{
			if (fabrica == null) obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Remove o responsável por uma conclusão aberta de um determinado processo e serventia cargo.
	 * 
	 * @param String id_Processo: id do processo que tera o responsável pela conclusão modificado
	 * @param String id_ServentiaCargo, id do serventiaCargo que sera removido	 
	 * @param LogDt logDt, log do usuario
	 * @param FabricaConexao fabrica, conexao
	 * 
	 * @author mmgomes
	 */
	public void removerResponsavelConclusao(String id_Processo, String id_ServentiaCargo, LogDt logDt, FabricaConexao fabrica) throws Exception {
				
		PendenciaNe pendenciaNe = new PendenciaNe();
		PendenciaDt conclusao = pendenciaNe.consultarConclusaoAbertaProcesso(id_Processo, id_ServentiaCargo, fabrica);

		if (conclusao != null) {
			this.retirarResponsavelPendencia(conclusao.getId(), id_ServentiaCargo, logDt, fabrica);
		}

	}
	
    /**
     * Consulta de serventias
     * 
     * @author lsbernardes
     * @param descricao
     *            descricao da serventia
     * @param posicao
     *            posicionamento da paginacao
     * @return List
     * @throws Exception
     */
    public List consultarDescricaoServentia(String descricao, UsuarioDt usuarioDt, String posicao) throws Exception {
        ServentiaNe serventiaNe = new ServentiaNe();

        List lista = serventiaNe.consultarDescricaoServentiaAlterarResponsavelConclusao(descricao, usuarioDt, posicao);
        this.QuantidadePaginas = serventiaNe.getQuantidadePaginas();
        serventiaNe = null;
        return lista;
    }
    
    /**
     * Consultar os cargos pela descricao, somente os cargos da serventai do
     * usuario informado
     * 
     * @author lsbernardes
     * @param idServentia
     *            id da serventia
     * @param descricao
     *            descricao da serventia
     * @param posicao
     *            posicao da paginacao
     * @return List
     * @throws Exception
     */
    public List consultarDescricaoServentiaCargoPorServentia(String idServentia, String grupoCodigo, String descricao, String posicao) throws Exception {
        ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
        List lista = null;
        
        if (grupoCodigo != null && grupoCodigo.length()>0 && Funcoes.StringToInt(grupoCodigo) == GrupoDt.ASSISTENTE_GABINETE){
        	//lista = serventiaCargoNe.consultarServentiaCargos(descricao, posicao, idServentia, String.valueOf(GrupoTipoDt.DISTRIBUIDOR_GABINETE));
        	lista = serventiaCargoNe.consultarServentiaCargosGrupo(descricao, posicao, idServentia, String.valueOf(GrupoDt.ASSISTENTE_GABINETE_FLUXO));
        	this.QuantidadePaginas = serventiaCargoNe.getQuantidadePaginas();
        } else {
        	lista = serventiaCargoNe.consultarServentiaCargos(descricao, posicao, idServentia);
        	this.QuantidadePaginas = serventiaCargoNe.getQuantidadePaginas();
        }
        serventiaCargoNe = null;
        return lista;
    }
    
    /**
     * Consultar os cargos juizes da serventia
     * @author lsbernardes
     * @param idServentia
     *            id da serventia
     * @return List
     * @throws Exception
     */
    public List consultarJuizesServentia(String id_Serventia) throws Exception {
        ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
        List lista = serventiaCargoNe.consultarServentiaCargosJuizes(id_Serventia);
        serventiaCargoNe = null;
        return lista;
    }
    
	public String consultarDescricaoServentiaGrupoJSON(String tempNomeBusca, String PosicaoPaginaAtual) throws Exception {
		ServentiaGrupoNe serventiaGrupoNe = new ServentiaGrupoNe();
		String stTemp = serventiaGrupoNe.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		return stTemp;
	}
	
	public String consultarDescricaoServentiaGrupoServentiaJSON(String tempNomeBusca, String PosicaoPaginaAtual, String id_serventia) throws Exception {
		ServentiaGrupoNe serventiaGrupoNe = new ServentiaGrupoNe();
		String stTemp = serventiaGrupoNe.consultarDescricaoServentiaGrupoServentiaJSON(tempNomeBusca, PosicaoPaginaAtual, id_serventia);
		return stTemp;
	}
	//lrcampos * 18/07/2019 * Montar o select2 filtrando pela descrição serv_cargo
	public String consultarDescricaoServentiaServCargoJSON(String tempNomeBusca, String PosicaoPaginaAtual, String id_serventia) throws Exception {
		ServentiaGrupoNe serventiaGrupoNe = new ServentiaGrupoNe();
		String stTemp = serventiaGrupoNe.consultarDescricaoServentiaServCargoJSON(tempNomeBusca, PosicaoPaginaAtual, id_serventia);
		return stTemp;
	}
	
	
	
	public String consultarServentiaGrupoIdCargo(String id_Cargo, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception {
		ServentiaGrupoNe serventiaGrupoNe = new ServentiaGrupoNe();
		String stTemp = serventiaGrupoNe.consultarServentiaGrupoIdCargo(id_Cargo, tempNomeBusca, PosicaoPaginaAtual);
		return stTemp;
	}
	
	public String consultarDescricaoServentiaGrupoJSON(String tempNomeBusca, String PosicaoPaginaAtual, String id_serventiagrupo) throws Exception {
		ServentiaGrupoNe serventiaGrupoNe = new ServentiaGrupoNe();
		String stTemp = serventiaGrupoNe.consultarDescricaoServentiaGrupoJSON(tempNomeBusca, PosicaoPaginaAtual, id_serventiagrupo);
		return stTemp;
	}
	
	public String consultarDescricaoServentiaGrupoJSON(String tempNomeBusca, String PosicaoPaginaAtual,String id_serventia, String id_serventiagrupo) throws Exception {
		ServentiaGrupoNe serventiaGrupoNe = new ServentiaGrupoNe();
		String stTemp = serventiaGrupoNe.consultarDescricaoServentiaGrupoJSON(tempNomeBusca, PosicaoPaginaAtual, id_serventia, id_serventiagrupo);
		return stTemp;
	}
	
	public List consultarHistoricosPendencia(String id_Pendencia) throws Exception {
		PendenciaResponsavelHistoricoNe pendenciaResponsavelHistoricoNe = new PendenciaResponsavelHistoricoNe();
		List tempList= pendenciaResponsavelHistoricoNe.consultarHistoricosPendencia(id_Pendencia);
		return tempList;   
	}
	
	public List consultarHistoricosPendenciaFinal(String id_Pendencia) throws Exception {
		PendenciaResponsavelHistoricoNe pendenciaResponsavelHistoricoNe = new PendenciaResponsavelHistoricoNe();
		List tempList= pendenciaResponsavelHistoricoNe.consultarHistoricosPendenciaFinal(id_Pendencia);
		return tempList;   
	}
	
	public ServentiaGrupoDt consultarSeventiaGrupoId(String id_ServentiaGrupo) throws Exception {
		ServentiaGrupoNe serventiaGrupoNe = new ServentiaGrupoNe();
		ServentiaGrupoDt serventiaGrupoDt = (ServentiaGrupoDt) serventiaGrupoNe.consultarId(id_ServentiaGrupo);
		return serventiaGrupoDt;   
	}
	
	
	public String consultarServentiasAtivasJSON(String descricao, String posicao, String serventiaTipoCodigo, String serventiaSubTipoCodigo) throws Exception {
		String stTemp = "";
		
		ServentiaNe ServentiaNe = new ServentiaNe(); 
		stTemp = ServentiaNe.consultarServentiasAtivasJSON(descricao,  posicao, serventiaTipoCodigo, null, serventiaSubTipoCodigo);
		
		return stTemp;
	}
	
	
	/**
     * Consultar os cargos pela descricao, somente os cargos da serventia informada
     * 
     * @param idServentia
     *            id da serventia
     * @param descricao
     *            descricao da serventia
     * @param posicao
     *            posicao da paginacao
     * @return String
     * @throws Exception
     */
    public String consultarDescricaoServentiaCargoPorServentiaJSON(String descricao, String idServentia,  String posicao) throws Exception {
        ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
        String stTemp = "";
        stTemp = serventiaCargoNe.consultarServentiaCargosJSON(descricao, idServentia, posicao);
        serventiaCargoNe = null;
        return stTemp;
    }
    
    
    /**
	 * Verifica se a pendência está no mesmo gabinete (serventia) do usuário logado
	 * 
	 * @param id_Pendencia, identificação da Pendência
	 * 
	 * @author lsbernardes
	 */
	public boolean isPendenciaResponsavelMesmaServentia(String id_Pendencia, String id_Serv) throws Exception {
		return this.isPendenciaResponsavelMesmaServentia(id_Pendencia, id_Serv,null);
	}
    
	/**
	 * Verifica se a pendência está no mesmo gabinete (serventia) do usuário logado
	 * 
	 * @param id_Pendencia, identificação da Pendência
	 * @param conexao,
	 *            conexão com o banco de dados
	 * 
	 * @author lsbernardes
	 */
	public boolean isPendenciaResponsavelMesmaServentia(String id_Pendencia, String id_Serv, FabricaConexao fabConexao) throws Exception {
		FabricaConexao obFabricaConexao = null;
		PendenciaResponsavelPs obPersistencia = null;
		boolean retorno=false;
		
		try{
			if (fabConexao == null){
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				 obPersistencia = new PendenciaResponsavelPs(obFabricaConexao.getConexao());
			} else
				 obPersistencia = new PendenciaResponsavelPs(fabConexao.getConexao());
			
			retorno = obPersistencia.isPendenciaResponsavelMesmaServentia(id_Pendencia, id_Serv);
		
		} finally{
			if (fabConexao == null)
				obFabricaConexao.fecharConexao();
		}

		return retorno;
	}
	
	/**
	 * Atualiza a serventia responsável por uma pendência do tipo Verificar Guia Pendente
	 * 
	 * @param String id_Processo: id do processo que tera o responsável pela conclusão modificado
	 * @param String id_ServentiaAtual, id do serventia que sera atualizado por um novo
	 * @param String id_ServentiaNovo, id do novo serventia 
	 * @param LogDt logDt, log do usuario
	 * @param FabricaConexao fabrica, conexao
	 * 
	 * @author lsebernardes
	 */
	public void atualizarServentiaResponsavelPendenciaVerificarGuiaPendente(String id_Processo, String id_ServentiaAtual, String id_ServentiaNova, FabricaConexao obFabricaConexao) throws Exception {
		PendenciaResponsavelPs obPersistencia = new PendenciaResponsavelPs(obFabricaConexao.getConexao());
		obPersistencia.atualizarServentiaResponsavelPendenciaVerificarGuiaPendente(id_Processo, id_ServentiaAtual, id_ServentiaNova);
	}
	
    public PendenciaResponsavelDt consultarIdPendenciaResponsavel(String id_pendencia, FabricaConexao obFabricaConexaoParam) throws Exception {
		PendenciaResponsavelDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		
		try{
			
			if(obFabricaConexaoParam == null){
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			} else {
				obFabricaConexao = obFabricaConexaoParam;
			}
			
			
			PendenciaResponsavelPs obPersistencia = new PendenciaResponsavelPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarIdPendenciaResponsavel(id_pendencia);
			if(dtRetorno != null) {
				obDados.copiar(dtRetorno);
			}
		
		}finally{
			if(obFabricaConexaoParam == null) {
				obFabricaConexao.fecharConexao();
			}
		}
		return dtRetorno;
	}
    
	public void alterarResponsavelMandadoResolvido(String idPend, String idServCargo, FabricaConexao fabricaConexao) throws Exception {

			if (idPend == null || idServCargo == null || fabricaConexao == null) {
				throw new MensagemException("Parâmetros inválidos.");
			}
			
			PendenciaResponsavelPs pendenciaResponsavelPs = new PendenciaResponsavelPs(fabricaConexao.getConexao());
			pendenciaResponsavelPs.alterarResponsavelMandadoResolvido(idPend, idServCargo);
			
	}
	
	public ServentiaCargoDt consultarServentiaCargo(String id_serventiacargo) throws Exception {
		ServentiaCargoNe ServentiaCargone = new ServentiaCargoNe();
		return ServentiaCargone.consultarId(id_serventiacargo);		
	}
	
	//mrbatista - 16/10/2019 15:19 refatoração da alteração dos votantes para que possa ser alterado o relator.
	public void trocarResponsavelPendencia(FabricaConexao obFabricaConexao, String idPendencia,
			String idServCargoNovo, PendenciaResponsavelDt responsavelSubstituir)
			throws Exception {
		
		
		if(responsavelSubstituir != null) {
			excluir(responsavelSubstituir, obFabricaConexao);
		}
		
		PendenciaResponsavelDt pendenciaResponsavelDt = new PendenciaResponsavelDt();
		pendenciaResponsavelDt.setId_ServentiaCargo(idServCargoNovo);
		pendenciaResponsavelDt.setId_Pendencia(idPendencia);
		pendenciaResponsavelDt.setId_UsuarioLog(responsavelSubstituir.getId_UsuarioLog());
		inserir(pendenciaResponsavelDt, obFabricaConexao);
		
	}
	
	
	//mrbatista - 23/06/2020 15:19 refatoração da alteração dos votantes para que possa ser alterado o relator.
	public void trocarResponsavelPendenciaDesembargador(FabricaConexao obFabricaConexao, String idPendencia,
			ServentiaCargoDt novoResponsavel, String idUsuarioLog) throws Exception {
	
		PendenciaResponsavelDt pendenciaResponsavelDt = new PendenciaResponsavelDt();
		pendenciaResponsavelDt.setId_ServentiaCargo(novoResponsavel.getId());
		pendenciaResponsavelDt.setId_Pendencia(idPendencia);
		pendenciaResponsavelDt.setId_UsuarioLog(idUsuarioLog);
		inserir(pendenciaResponsavelDt, obFabricaConexao);
		
     	//Captura o Distribuidor do Gabinete do Desembargador e adiciona como responsável pela pendência
    	ServentiaCargoDt serventiaCargoDistribuidor = (new ServentiaCargoNe()).getDistribuidorGabinete(novoResponsavel.getId_Serventia(), obFabricaConexao);
    	if(serventiaCargoDistribuidor != null) {		
    		PendenciaResponsavelDt responsavel = new PendenciaResponsavelDt();
    		responsavel.setId_Pendencia(idPendencia);
	    	responsavel.setId_ServentiaCargo(serventiaCargoDistribuidor.getId());
	    	responsavel.setId_UsuarioLog(idUsuarioLog);
	    	inserir(responsavel, obFabricaConexao);
    	}
		
	}

	//mrbatista - 16/10/2019 15:19 Sobrecarga do método para passar fabrica de conexão.
	public void excluir(PendenciaResponsavelDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		PendenciaResponsavelPs obPersistencia = new PendenciaResponsavelPs(obFabricaConexao.getConexao());
		obLogDt = new LogDt("PendenciaResponsavel", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
		obPersistencia.excluir(dados.getId()); dados.limpar();

		obLog.salvar(obLogDt, obFabricaConexao);
	}
	
	public ServentiaGrupoDt consultarSeventiaGrupoDistribuidorId(String id_ServentiaCargo) throws Exception {
		ServentiaGrupoNe serventiaGrupoNe = new ServentiaGrupoNe();
		ServentiaGrupoDt serventiaGrupoDt = (ServentiaGrupoDt) serventiaGrupoNe.consultarSeventiaGrupoDistribuidorId(id_ServentiaCargo);
		return serventiaGrupoDt;   
	}
	
	public ServentiaGrupoDt consultarSeventiaGrupoMagistradoId(String id_ServentiaCargo) throws Exception {
		ServentiaGrupoNe serventiaGrupoNe = new ServentiaGrupoNe();
		ServentiaGrupoDt serventiaGrupoDt = (ServentiaGrupoDt) serventiaGrupoNe.consultarSeventiaGrupoMagistradoId(id_ServentiaCargo);
		return serventiaGrupoDt;   
	}

	/**
	 * Consulta todas as pendênicas vinculada a um id_arquivo de uma pre-analise múltipla, assim como o seu histórico e o seu responsável.
	 * Para distribuição de pre-analise múltipa em gabinete com fluxo.
	 * 
	 * @param String id_Arquivo: id do arquivo vinculado as pendências
	 * 
	 * @author lsebernardes
	 */
	public List consultarPendencias(String id_Arquivo) throws Exception{
		List tempList = null;
		PendenciaArquivoNe neObjeto = new PendenciaArquivoNe();
		PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
		tempList = neObjeto.consultarPendencias(id_Arquivo);
		for (Iterator iterator = tempList.iterator(); iterator.hasNext();) {
			PendenciaDt pendenciaDt = (PendenciaDt) iterator.next();
			pendenciaDt.setListaHistoricoPendencia(this.consultarHistoricosPendencia(pendenciaDt.getId()));
			
			List listaResponsavelPendencia = pendenciaResponsavelNe.consultarResponsaveisDetalhado(pendenciaDt.getId());
			
			if (listaResponsavelPendencia != null && listaResponsavelPendencia.size()>0){
			    PendenciaResponsavelDt pendenciaResponsavelAtualDt = (PendenciaResponsavelDt)listaResponsavelPendencia.get(0);
				pendenciaDt.addResponsavel(pendenciaResponsavelAtualDt);
			}
		}
		neObjeto = null;
		return tempList;
	}
	
	//mrbatista - 23/06/2020 15:19 refatoração da alteração dos votantes para que possa ser alterado o relator.
	public void trocarResponsavelPendenciaDesembargadorMesmaServentia(FabricaConexao obFabricaConexao, String idPendencia,
			ServentiaCargoDt novoResponsavel, String idUsuarioLog) throws Exception {
	
		PendenciaResponsavelDt pendenciaResponsavelDt = new PendenciaResponsavelDt();
		pendenciaResponsavelDt.setId_ServentiaCargo(novoResponsavel.getId());
		pendenciaResponsavelDt.setId_Pendencia(idPendencia);
		pendenciaResponsavelDt.setId_UsuarioLog(idUsuarioLog);
		inserir(pendenciaResponsavelDt, obFabricaConexao);
		

		
	}

	public void devolverPendenciaUltimoAssessor(UsuarioNe usuarioSessao, PendenciaDt pendenciaDt, String hash) throws Exception {

			PendenciaResponsavelHistoricoNe pendenciaResponsavelHistoricoNe = new PendenciaResponsavelHistoricoNe();
			
			PendenciaResponsavelHistoricoDt pendenciaResponsavelHistoricoDt  = pendenciaResponsavelHistoricoNe.consultarResponsavelAnteriorPendencia(pendenciaDt.getId());

			if(pendenciaResponsavelHistoricoDt==null) {
				throw new MensagemException("Não foi possível determinar o último Assistente");
			}
								
			PendenciaResponsavelDt pendenciaResponsavelNovoDt = new PendenciaResponsavelDt();
			pendenciaResponsavelNovoDt.setId_Pendencia(pendenciaDt.getId());
			pendenciaResponsavelNovoDt.setId_ServentiaCargo(pendenciaResponsavelHistoricoDt.getId_ServentiaCargo());
			pendenciaResponsavelNovoDt.setId_ServentiaGrupo(pendenciaResponsavelHistoricoDt.getId_ServentiaGrupo());
			pendenciaResponsavelNovoDt.setId_UsuarioLog(usuarioSessao.getId_Usuario());
			pendenciaResponsavelNovoDt.setIpComputadorLog(usuarioSessao.getIpComputadorLog());
			pendenciaResponsavelNovoDt.setConclusoMagistrado(false);
					
			reDistribuirConclusaoUnidadeTrabalho(pendenciaResponsavelNovoDt,  usuarioSessao.getUsuarioDt(), pendenciaDt, "");									
		
		
	}

	
	
}
