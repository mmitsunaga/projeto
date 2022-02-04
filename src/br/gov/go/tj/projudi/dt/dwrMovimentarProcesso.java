package br.gov.go.tj.projudi.dt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import br.gov.go.tj.utils.Funcoes;

import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONObject;
import com.google.gson.Gson;

public class dwrMovimentarProcesso implements Serializable{

	private static final long serialVersionUID = 6217917833479223947L;
	private int id;
	private String CodPendenciaTipo = "";
	private String PendenciaTipo = "";
	private String CodDestinatario = "";
	private String Destinatario = "";
	private String DestinatarioTipo = "";
	private String OnLine = "";
	private String Prazo = "";
	private String dataLimite = "";
	private String Urgencia = "";
	private String PessoalAdvogado = "";
	private String Pessoal = "";
	private String IntimacaoAudiencia = "";
	private String Id_Sessao = "";
	private String DataSessao = "";
	private String Outros = "";
	private String IdProcessoTipo = "";
	private String ProcessoTipo = "";
	private String Id_Classe = "";
	private String Classe = "";
	private String idServentiaDestino = "";
	private String idServentiaCargo = "";
	private String idAreaDistribuicaoDestino = "";
	private String CodTipoAudiencia = "";
	private String CodTipoProcessoFase = "";
	private boolean ExpedicaoAutomatica = false;
	private boolean MaoPropria = false;
	private boolean OrdemServico = false;
	private String CodExpedicaoAutomatica = "";
	private String Id_ProcessoCustaTipo = "";
	private String Id_ProcArquivamentoTipo = "";
	private String ProcArquivamentoTipo = "";
	private String Id_relator = "";
	private String Id_votante1 = "";
	private String Id_votante2 = "";
	private String Id_votante3 = "";
	private String Id_votante4 = "";
	private String Id_votante5 = "";
	private String Id_votante6 = "";
	private String Id_votante7 = "";
	private String Id_votante8 = "";
	private String Id_votante9 = "";
	private String Id_votante10 = "";
	private String Id_votante11 = "";
	private String Id_votante12 = "";
	private String Id_votante13 = "";
	private String Id_votante14 = "";
	private String Id_votante15 = "";
	private String Id_votante16 = "";
	private String Id_votante17 = "";
	private String Id_votante18 = "";
	private String Id_votante19 = "";
	private String Id_votante20 = "";
	private String Id_votante21 = "";
	private String Id_votante22 = "";
	private String Id_votante23 = "";
	private String Relator = "";
	private String Votante1 = "";
	private String Votante2 = "";
	private String Votante3 = "";
	private String Votante4 = "";
	private String Votante5 = "";
	private String Votante6 = "";
	private String Votante7 = "";
	private String Votante8 = "";
	private String Votante9 = "";
	private String Votante10 = "";
	private String Votante11 = "";
	private String Votante12 = "";
	private String Votante13 = "";
	private String Votante14 = "";
	private String Votante15 = "";
	private String Votante16 = "";
	private String Votante17 = "";
	private String Votante18 = "";
	private String Votante19 = "";
	private String Votante20 = "";
	private String Votante21 = "";
	private String Votante22 = "";
	private String Votante23 = "";
	private boolean Virtual = false;
//	private boolean VerificaImpedimento = true;
	private String RecursoSecundarioPartes;
	private boolean PermiteSustentacaoOral = true;

	public String getIdServentiaDestino() {
		return idServentiaDestino;
	}

	public void setIdServentiaDestino(String idServentiaDestino) {
		this.idServentiaDestino = idServentiaDestino;
	}

	public String getIdAreaDistribuicaoDestino() {
		return idAreaDistribuicaoDestino;
	}

	public void setIdAreaDistribuicaoDestino(String idAreaDistribuicaoDestino) {
		this.idAreaDistribuicaoDestino = idAreaDistribuicaoDestino;
	}

	public String getDestinatario() {
		return Destinatario;
	}

	public String getDestinatarioTipo() {
		return DestinatarioTipo;
	}

	public int getId() {
		return id;
	}

	public String getOnLine() {
		return OnLine;
	}

	public String getPendenciaTipo() {
		return PendenciaTipo;
	}

	public String getPrazo() {
		return Prazo;
	}

	public String getUrgencia() {
		return Urgencia;
	}

	public void setDestinatario(String destinatario) {
		Destinatario = destinatario;
	}

	public void setDestinatarioTipo(String destinatarioTipo) {
		DestinatarioTipo = destinatarioTipo;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setOnLine(String onLine) {
		OnLine = onLine;
	}

	public void setPendenciaTipo(String pendenciaTipo) {
		PendenciaTipo = pendenciaTipo;
	}

	public void setPrazo(String prazo) {
		Prazo = prazo;
	}

	public void setUrgencia(String urgencia) {
		Urgencia = urgencia;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final dwrMovimentarProcesso other = (dwrMovimentarProcesso) obj;
		if (id != other.id) return false;
		return true;
	}

	public String getCodPendenciaTipo() {
		return CodPendenciaTipo;
	}

	public void setCodPendenciaTipo(String codPendenciaTipo) {
		CodPendenciaTipo = codPendenciaTipo;
	}

	public String getCodDestinatario() {
		return CodDestinatario;
	}

	/**
	 * Retorna a primeira parte da string retornada pelo DWR em CodDestinatario
	 * Será o tipo do destinatário a ser tratado.
	 * Exemplo: "3-100" (3 significa que pendência é para um parte, e 100 é o id da parte)
	 */
	public int getTipoDestinatario() {
		int inRetorno = -1;
		if (CodDestinatario != null && !CodDestinatario.equals("")) {
			inRetorno = Funcoes.StringToInt(CodDestinatario.split("-")[0]);
		}
		return inRetorno;
	}

	/**
	 * Retorna a segunda parte da string retornada pelo DWR em CodDestinatario
	 * Será a identificação da parte ou serventia
	 */
	public String getIdDestinatario() {
		String inRetorno = "";
		if (CodDestinatario != null && !CodDestinatario.equals("")) {
			if (CodDestinatario.split("-").length > 1) inRetorno = CodDestinatario.split("-")[1];
		}
		return inRetorno;
	}

	public void setCodDestinatario(String codDestinatario) {
		CodDestinatario = codDestinatario;
	}

	public String getPessoalAdvogado() {
		return PessoalAdvogado;
	}

	public void setPessoalAdvogado(String pessoalAdvogado) {
		if (pessoalAdvogado != null) {
			if (pessoalAdvogado.equalsIgnoreCase("true") || pessoalAdvogado.equalsIgnoreCase("sim") ) PessoalAdvogado = "Sim";
			else PessoalAdvogado = "Não";
		}
	}

	public String getPessoal() {
		return Pessoal;
	}

	public void setPessoal(String pessoal) {
		if (pessoal != null) {
			if (pessoal.equalsIgnoreCase("true") || pessoal.equalsIgnoreCase("sim")) Pessoal = "Sim";
			else Pessoal = "Não";
		}
	}

	public String getId_Sessao() {
		return Id_Sessao;
	}

	public void setId_Sessao(String id_Sessao) {
		Id_Sessao = id_Sessao;
	}

	public String getDataSessao() {
		return DataSessao;
	}

	public void setDataSessao(String dataSessao) {
		DataSessao = dataSessao;
	}

	public String getOutros() {
		return Outros;
	}

	public void setOutros(String outros) {
		Outros = outros;
	}

	public String getIdProcessoTipo() {
		return IdProcessoTipo;
	}

	public void setIdProcessoTipo(String idProcessoTipo) {
		IdProcessoTipo = idProcessoTipo;
	}

	public String getProcessoTipo() {
		return ProcessoTipo;
	}

	public void setProcessoTipo(String processoTipo) {
		ProcessoTipo = processoTipo;
	}

	public String getIntimacaoAudiencia() {
		return IntimacaoAudiencia;
	}

	public void setIntimacaoAudiencia(String intimacaoAudiencia) {
		if (intimacaoAudiencia != null) {
			if (intimacaoAudiencia.equalsIgnoreCase("true") || intimacaoAudiencia.equalsIgnoreCase("sim")) IntimacaoAudiencia = "Sim";
			else IntimacaoAudiencia = "Não";
		}
	}

	/**
	 * Método que retorna o tipo de intimação selecionado pelo usuário,
	 * podendo ser: "Pessoal" ou "Pessoal e Advogados" ou "Intimação em Audiência/Cartório"
	 * @return
	 */
	public String getTipoIntimacao(){
		String tipoIntimacao = "";
		if (Pessoal.equalsIgnoreCase("Sim")) tipoIntimacao = "Pessoal";
		if (PessoalAdvogado.equalsIgnoreCase("Sim")) tipoIntimacao = "Pessoal e Advogados";
		if (IntimacaoAudiencia.equalsIgnoreCase("Sim")) tipoIntimacao = "Intimação em Audiência/Cartório";
		return tipoIntimacao;
	}

	public String getId_Classe() {
		return Id_Classe;
	}

	public void setId_Classe(String id_Classe) {
		Id_Classe = id_Classe;
	}

	public String getClasse() {
		return Classe;
	}

	public void setClasse(String classe) {
		Classe = classe;
	}

	public boolean isPendenciaTipo(int[] arrayPendenciaTipo) {
		int pendeciaTipo = Funcoes.StringToInt( getCodPendenciaTipo());
		for (int i=0; i<arrayPendenciaTipo.length; i++){
			if (pendeciaTipo==arrayPendenciaTipo[i]){
				return true;
			}
		}
		return false;
	}

	public boolean isArquivamento() {
		if (Funcoes.StringToInt(getCodPendenciaTipo())==PendenciaTipoDt.ARQUIVAMENTO){
			return true;
		}
		return false;
	}

	public String getDataLimite() {
		return dataLimite;
	}

	public void setDataLimite(String dataLimite) {
		this.dataLimite = dataLimite;
	}

	public String getId_relator() {
		return Id_relator;
	}

	public void setId_relator(String id_relator) {
		Id_relator = id_relator;
	}

	public String getId_votante2() {
		return Id_votante2;
	}

	public void setId_votante2(String id_votante2) {
		Id_votante2 = id_votante2;
	}

	public String getId_votante1() {
		return Id_votante1;
	}

	public void setId_votante1(String id_votante1) {
		Id_votante1 = id_votante1;
	}

	public String getId_votante3() {
		return Id_votante3;
	}

	public void setId_votante3(String id_votante3) {
		Id_votante3 = id_votante3;
	}

	public String getId_votante4() {
		return Id_votante4;
	}

	public void setId_votante4(String id_votante4) {
		Id_votante4 = id_votante4;
	}

	public boolean isVirtual() {
		return Virtual;
	}

	public void setVirtual(boolean virtual) {
		Virtual = virtual;
	}
	
	public void setVirtual(String virtual) {
		this.Virtual = Funcoes.StringToBoolean(virtual);
	}
	
	public List<ServentiaCargoDt> getListaVotantes() {
		List votantes = new ArrayList<>();
		ServentiaCargoDt votante;
		if (!"".equals(this.Id_votante1)) {
			votante = new ServentiaCargoDt();
			votante.setId(this.Id_votante1);
			votante.setNomeUsuario(this.Votante1);
			votantes.add(votante);
		}
		if (!"".equals(this.Id_votante2)) {
			votante = new ServentiaCargoDt();
			votante.setId(this.Id_votante2);
			votante.setNomeUsuario(this.Votante2);
			votantes.add(votante);
		}
		if (!"".equals(this.Id_votante3)) {
			votante = new ServentiaCargoDt();
			votante.setId(this.Id_votante3);
			votante.setNomeUsuario(this.Votante3);
			votantes.add(votante);
		}
		if (!"".equals(this.Id_votante4)) {
			votante = new ServentiaCargoDt();
			votante.setId(this.Id_votante4);
			votante.setNomeUsuario(this.Votante4);
			votantes.add(votante);
		}
		if(StringUtils.isNotEmpty(this.Id_votante5)) {
			votante = new ServentiaCargoDt();
			votante.setId(this.Id_votante5);
			votante.setNomeUsuario(this.Votante5);
			votantes.add(votante);
		}
		if(StringUtils.isNotEmpty(this.Id_votante6)) {
			votante = new ServentiaCargoDt();
			votante.setId(this.Id_votante6);
			votante.setNomeUsuario(this.Votante6);
			votantes.add(votante);
		}
		if(StringUtils.isNotEmpty(this.Id_votante7)) {
			votante = new ServentiaCargoDt();
			votante.setId(this.Id_votante7);
			votante.setNomeUsuario(this.Votante7);
			votantes.add(votante);
		}
		if(StringUtils.isNotEmpty(this.Id_votante8)) {
			votante = new ServentiaCargoDt();
			votante.setId(this.Id_votante8);
			votante.setNomeUsuario(this.Votante8);
			votantes.add(votante);
		}
		if(StringUtils.isNotEmpty(this.Id_votante9)) {
			votante = new ServentiaCargoDt();
			votante.setId(this.Id_votante9);
			votante.setNomeUsuario(this.Votante9);
			votantes.add(votante);
		}
		if(StringUtils.isNotEmpty(this.Id_votante10)) {
			votante = new ServentiaCargoDt();
			votante.setId(this.Id_votante10);
			votante.setNomeUsuario(this.Votante10);
			votantes.add(votante);
		}
		if(StringUtils.isNotEmpty(this.Id_votante11)) {
			votante = new ServentiaCargoDt();
			votante.setId(this.Id_votante11);
			votante.setNomeUsuario(this.Votante11);
			votantes.add(votante);
		}
		if(StringUtils.isNotEmpty(this.Id_votante12)) {
			votante = new ServentiaCargoDt();
			votante.setId(this.Id_votante12);
			votante.setNomeUsuario(this.Votante12);
			votantes.add(votante);
		}
		if(StringUtils.isNotEmpty(this.Id_votante13)) {
			votante = new ServentiaCargoDt();
			votante.setId(this.Id_votante13);
			votante.setNomeUsuario(this.Votante13);
			votantes.add(votante);
		}
		if(StringUtils.isNotEmpty(this.Id_votante14)) {
			votante = new ServentiaCargoDt();
			votante.setId(this.Id_votante14);
			votante.setNomeUsuario(this.Votante14);
			votantes.add(votante);
		}
		if(StringUtils.isNotEmpty(this.Id_votante15)) {
			votante = new ServentiaCargoDt();
			votante.setId(this.Id_votante15);
			votante.setNomeUsuario(this.Votante15);
			votantes.add(votante);
		}
		if(StringUtils.isNotEmpty(this.Id_votante16)) {
			votante = new ServentiaCargoDt();
			votante.setId(this.Id_votante16);
			votante.setNomeUsuario(this.Votante16);
			votantes.add(votante);
		}
		if(StringUtils.isNotEmpty(this.Id_votante17)) {
			votante = new ServentiaCargoDt();
			votante.setId(this.Id_votante17);
			votante.setNomeUsuario(this.Votante17);
			votantes.add(votante);
		}
		if(StringUtils.isNotEmpty(this.Id_votante18)) {
			votante = new ServentiaCargoDt();
			votante.setId(this.Id_votante18);
			votante.setNomeUsuario(this.Votante18);
			votantes.add(votante);
		}
		if(StringUtils.isNotEmpty(this.Id_votante19)) {
			votante = new ServentiaCargoDt();
			votante.setId(this.Id_votante19);
			votante.setNomeUsuario(this.Votante19);
			votantes.add(votante);
		}
		if(StringUtils.isNotEmpty(this.Id_votante20)) {
			votante = new ServentiaCargoDt();
			votante.setId(this.Id_votante20);
			votante.setNomeUsuario(this.Votante20);
			votantes.add(votante);
		}
		if(StringUtils.isNotEmpty(this.Id_votante21)) {
			votante = new ServentiaCargoDt();
			votante.setId(this.Id_votante21);
			votante.setNomeUsuario(this.Votante21);
			votantes.add(votante);
		}
		if(StringUtils.isNotEmpty(this.Id_votante22)) {
			votante = new ServentiaCargoDt();
			votante.setId(this.Id_votante22);
			votante.setNomeUsuario(this.Votante22);
			votantes.add(votante);
		}
		if(StringUtils.isNotEmpty(this.Id_votante23)) {
			votante = new ServentiaCargoDt();
			votante.setId(this.Id_votante23);
			votante.setNomeUsuario(this.Votante23);
			votantes.add(votante);
		}
		return votantes;
	}

	public String getRelator() {
		return Relator;
	}

	public void setRelator(String relator) {
		Relator = relator;
	}

	public String getVotante1() {
		return Votante1;
	}

	public void setVotante1(String votante1) {
		Votante1 = votante1;
	}

	public String getVotante2() {
		return Votante2;
	}

	public void setVotante2(String votante2) {
		Votante2 = votante2;
	}

	public String getVotante3() {
		return Votante3;
	}

	public void setVotante3(String votante3) {
		Votante3 = votante3;
	}

	public String getVotante4() {
		return Votante4;
	}

	public void setVotante4(String votante4) {
		Votante4 = votante4;
	}

//	public boolean verificaImpedimento() {
//		return VerificaImpedimento;
//	}
//
//	public void setVerificaImpedimento(boolean verificaImpedimento) {
//		VerificaImpedimento = verificaImpedimento;
//	}
//	
//	public void setVerificaImpedimento(String verificaImpedimento) {
//		this.VerificaImpedimento = Funcoes.StringToBoolean(verificaImpedimento);
//	}

	public String getRecursoSecundarioPartes() {
		return RecursoSecundarioPartes;
	}

	public void setRecursoSecundarioPartes(String recursoSecundarioPartes) {
		RecursoSecundarioPartes = recursoSecundarioPartes;
	}
	
	public List<RecursoSecundarioParteDt> getRecursoSecundarioPartesDt() {
		try {
			JSONArray array = new JSONArray(RecursoSecundarioPartes);
			List<RecursoSecundarioParteDt> listaRecursoSecundarioPartes = new ArrayList<>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject jsonObj = array.getJSONObject(i);			
				listaRecursoSecundarioPartes.add(new Gson().fromJson(jsonObj.toString(), RecursoSecundarioParteDt.class));			
			}
			return listaRecursoSecundarioPartes;
		} catch (Exception e) {
			return null;
		}
		
	}

	public boolean isPermiteSustentacaoOral() {
		return PermiteSustentacaoOral;
	}

	public void setPermiteSustentacaoOral(boolean permiteSustentacaoOral) {
		PermiteSustentacaoOral = permiteSustentacaoOral;
	}
	
	public void setPermiteSustentacaoOral(String permiteSustentacaoOral) {
		PermiteSustentacaoOral = Funcoes.StringToBoolean(permiteSustentacaoOral);
	}

	public String getId_votante5() {
		return Id_votante5;
	}

	public void setId_votante5(String id_votante5) {
		Id_votante5 = id_votante5;
	}

	public String getId_votante6() {
		return Id_votante6;
	}

	public void setId_votante6(String id_votante6) {
		Id_votante6 = id_votante6;
	}

	public String getId_votante7() {
		return Id_votante7;
	}

	public void setId_votante7(String id_votante7) {
		Id_votante7 = id_votante7;
	}

	public String getId_votante8() {
		return Id_votante8;
	}

	public void setId_votante8(String id_votante8) {
		Id_votante8 = id_votante8;
	}

	public String getId_votante9() {
		return Id_votante9;
	}

	public void setId_votante9(String id_votante9) {
		Id_votante9 = id_votante9;
	}

	public String getId_votante10() {
		return Id_votante10;
	}

	public void setId_votante10(String id_votante10) {
		Id_votante10 = id_votante10;
	}

	public String getId_votante11() {
		return Id_votante11;
	}

	public void setId_votante11(String id_votante11) {
		Id_votante11 = id_votante11;
	}

	public String getId_votante12() {
		return Id_votante12;
	}

	public void setId_votante12(String id_votante12) {
		Id_votante12 = id_votante12;
	}

	public String getId_votante13() {
		return Id_votante13;
	}

	public void setId_votante13(String id_votante13) {
		Id_votante13 = id_votante13;
	}

	public String getId_votante14() {
		return Id_votante14;
	}

	public void setId_votante14(String id_votante14) {
		Id_votante14 = id_votante14;
	}

	public String getId_votante15() {
		return Id_votante15;
	}

	public void setId_votante15(String id_votante15) {
		Id_votante15 = id_votante15;
	}

	public String getId_votante16() {
		return Id_votante16;
	}

	public void setId_votante16(String id_votante16) {
		Id_votante16 = id_votante16;
	}

	public String getId_votante17() {
		return Id_votante17;
	}

	public void setId_votante17(String id_votante17) {
		Id_votante17 = id_votante17;
	}

	public String getId_votante18() {
		return Id_votante18;
	}

	public void setId_votante18(String id_votante18) {
		Id_votante18 = id_votante18;
	}

	public String getId_votante19() {
		return Id_votante19;
	}

	public void setId_votante19(String id_votante19) {
		Id_votante19 = id_votante19;
	}

	public String getId_votante20() {
		return Id_votante20;
	}

	public void setId_votante20(String id_votante20) {
		Id_votante20 = id_votante20;
	}
	
	public String getId_votante21() {
		return Id_votante21;
	}

	public void setId_votante21(String id_votante21) {
		Id_votante21 = id_votante21;
	}
	
	public String getId_votante22() {
		return Id_votante22;
	}

	public void setId_votante22(String id_votante22) {
		Id_votante22 = id_votante22;
	}
	
	public String getId_votante23() {
		return Id_votante23;
	}

	public void setId_votante23(String id_votante23) {
		Id_votante23 = id_votante23;
	}

	public String getVotante5() {
		return Votante5;
	}

	public void setVotante5(String votante5) {
		Votante5 = votante5;
	}

	public String getVotante6() {
		return Votante6;
	}

	public void setVotante6(String votante6) {
		Votante6 = votante6;
	}

	public String getVotante7() {
		return Votante7;
	}

	public void setVotante7(String votante7) {
		Votante7 = votante7;
	}

	public String getVotante8() {
		return Votante8;
	}

	public void setVotante8(String votante8) {
		Votante8 = votante8;
	}

	public String getVotante9() {
		return Votante9;
	}

	public void setVotante9(String votante9) {
		Votante9 = votante9;
	}

	public String getVotante10() {
		return Votante10;
	}

	public void setVotante10(String votante10) {
		Votante10 = votante10;
	}

	public String getVotante11() {
		return Votante11;
	}

	public void setVotante11(String votante11) {
		Votante11 = votante11;
	}

	public String getVotante12() {
		return Votante12;
	}

	public void setVotante12(String votante12) {
		Votante12 = votante12;
	}

	public String getVotante13() {
		return Votante13;
	}

	public void setVotante13(String votante13) {
		Votante13 = votante13;
	}

	public String getVotante14() {
		return Votante14;
	}

	public void setVotante14(String votante14) {
		Votante14 = votante14;
	}

	public String getVotante15() {
		return Votante15;
	}

	public void setVotante15(String votante15) {
		Votante15 = votante15;
	}

	public String getVotante16() {
		return Votante16;
	}

	public void setVotante16(String votante16) {
		Votante16 = votante16;
	}

	public String getVotante17() {
		return Votante17;
	}

	public void setVotante17(String votante17) {
		Votante17 = votante17;
	}

	public String getVotante18() {
		return Votante18;
	}

	public void setVotante18(String votante18) {
		Votante18 = votante18;
	}

	public String getVotante19() {
		return Votante19;
	}

	public void setVotante19(String votante19) {
		Votante19 = votante19;
	}

	public String getVotante20() {
		return Votante20;
	}

	public void setVotante20(String votante20) {
		Votante20 = votante20;
	}
	
	public String getVotante21() {
		return Votante21;
	}

	public void setVotante21(String votante21) {
		Votante21 = votante21;
	}
	
	public String getVotante22() {
		return Votante22;
	}

	public void setVotante22(String votante22) {
		Votante22 = votante22;
	}
	
	public String getVotante23() {
		return Votante23;
	}

	public void setVotante23(String votante23) {
		Votante23 = votante23;
	}

	public String getCodTipoAudiencia() {
		return CodTipoAudiencia;
	}

	public void setCodTipoAudiencia(String codTipoAudiencia) {
		this.CodTipoAudiencia = codTipoAudiencia;
	}
	
	public String getCodTipoProcessoFase() {
		return CodTipoProcessoFase;
	}

	public void setCodTipoProcessoFase(String codTipoProcessoFase) {
		CodTipoProcessoFase = codTipoProcessoFase;
	}

	public boolean getExpedicaoAutomatica() {
		return ExpedicaoAutomatica;
	}

	public void setExpedicaoAutomatica(boolean expedicaoAutomatica) {
		this.ExpedicaoAutomatica = expedicaoAutomatica;
	}
	
	public boolean getMaoPropria() {
		return MaoPropria;
	}

	public void setOrdemServico(boolean ordemServico) {
		this.OrdemServico = ordemServico;
	}
	
	public boolean getOrdemServico() {
		return OrdemServico;
	}

	public void setMaoPropria(boolean maoPropria) {
		this.MaoPropria = maoPropria;
	}

	public boolean isExpedicaoAutomatica() {
		return ExpedicaoAutomatica;
	}

	public String getCodExpedicaoAutomatica() {
		return CodExpedicaoAutomatica;
	}

	public void setCodExpedicaoAutomatica(String codExpedicaoAutomatica) {
		CodExpedicaoAutomatica = codExpedicaoAutomatica;
	}
	
	public String getId_ProcessoCustaTipo() {
		return Id_ProcessoCustaTipo;
	}

	public void setId_ProcessoCustaTipo(String id_ProcessoCustaTipo) {
		Id_ProcessoCustaTipo = id_ProcessoCustaTipo;
	}

	public String getId_ProcArquivamentoTipo() {
		return Id_ProcArquivamentoTipo;
	}

	public void setId_ProcArquivamentoTipo(String id_ProcArquivamentoTipo) {
		Id_ProcArquivamentoTipo = id_ProcArquivamentoTipo;
	}

	public String getProcArquivamentoTipo() {
		return ProcArquivamentoTipo;
	}

	public void setProcArquivamentoTipo(String procArquivamentoTipo) {
		ProcArquivamentoTipo = procArquivamentoTipo;
	}
	
	public String getIdServentiaCargo() {
		return idServentiaCargo;
	}

	public void setIdServentiaCargo(String idServentiaCargo) {
		this.idServentiaCargo = idServentiaCargo;
	}

	public String getJSON() {
		StringBuilder json = new StringBuilder();
		json.append("{");
		json.append("\"id\":\"" 				+ this.id + "\"");
		json.append(",\"codPendenciaTipo\":\"" 	+ this.CodPendenciaTipo + "\"");
		json.append(",\"pendenciaTipo\":\"" 	+ this.PendenciaTipo + "\"");
		json.append(",\"codDestinatario\":\"" 	+ this.CodDestinatario + "\"");
		json.append(",\"destinatario\":\"" 		+ this.Destinatario + "\"");
		json.append(",\"destinatarioTipo\":\"" 	+ this.DestinatarioTipo + "\"");
		json.append(",\"onLine\":\"" 			+ this.OnLine + "\"");
		json.append(",\"prazo\":\"" 			+ this.Prazo + "\"");
		json.append(",\"urgencia\":\"" 			+ this.Urgencia + "\"");
		json.append(",\"pessoalAdvogado\":\"" 	+ this.PessoalAdvogado + "\"");
		json.append(",\"pessoal\":\"" 			+ this.Pessoal + "\"");
		json.append(",\"id_Sessao\":\"" 		+ this.Id_Sessao + "\"");
		json.append(",\"dataSessao\":\"" 		+ this.DataSessao + "\"");
		json.append(",\"outros\":\"" 			+ this.Outros + "\"");
		json.append(",\"idProcessoTipo\":\"" 	+ this.IdProcessoTipo + "\"");
		json.append(",\"processoTipo\":\"" 		+ this.ProcessoTipo + "\"");
		json.append(",\"id_Classe\":\"" 		+ this.Id_Classe + "\"");
		json.append(",\"classe\":\"" 			+ this.Classe + "\"");		
		json.append(",\"dataLimite\":\"" 				+ this.dataLimite + "\"");
		json.append(",\"codTipoAudiencia\":\"" 			+ this.CodTipoAudiencia + "\"");
		json.append(",\"codTipoProcessoFase\":\"" 			+ this.CodTipoProcessoFase + "\"");
		json.append(",\"procArquivamentoTipo\":\"" 		+ this.ProcArquivamentoTipo + "\"");
		json.append(",\"codExpedicaoAutomatica\":\"" 	+ this.CodExpedicaoAutomatica + "\"");
		json.append(",\"maoPropria\":\"" 				+ this.MaoPropria + "\"");
		json.append(",\"ordemServico\":\"" 		+ this.OrdemServico + "\"");
		json.append(",\"tipoIntimacao\":\"" 	+ this.getTipoIntimacao() + "\"");				
		json.append("}");		
		return json.toString();
	}

}
