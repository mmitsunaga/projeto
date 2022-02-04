package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.go.tj.projudi.dt.SituacaoAtualExecucaoDt;
import br.gov.go.tj.projudi.dt.SituacaoAtualModalidadeDt;
import br.gov.go.tj.projudi.dt.SituacaoAtualTipoPenaDt;
import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class SituacaoAtualExecucaoPs extends Persistencia {

	private static final long serialVersionUID = 1164930033773378454L;

	public SituacaoAtualExecucaoPs() {
	}
	
	public SituacaoAtualExecucaoPs(Connection conexao){
    	Conexao = conexao;
    }
	public void inserir(SituacaoAtualExecucaoDt dados) throws Exception {
		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.SITUACAO_ATUAL_EXE("; 
		stSqlValores +=  " Values (";
		
		if ((dados.getIdLocalCumprimentoPena().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_LOCAL_CUMP_PENA" ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getIdLocalCumprimentoPena());  
			stVirgula=",";
		}
		if ((dados.getIdRegime().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_REGIME_EXE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getIdRegime());  
			stVirgula=",";
		}
		if ((dados.getIdEventoExecucaoStatus().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_EVENTO_EXE_STATUS " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getIdEventoExecucaoStatus());  
			stVirgula=",";
		}
		if ((dados.getIdFormaCumprimento().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_FORMA_CUMPRIMENTO_EXE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getIdFormaCumprimento());  
			stVirgula=",";
		}

		if ((dados.getDataFuga().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_FUGA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDate(dados.getDataFuga());  
			stVirgula=",";
		}
		
		stSqlCampos+=   stVirgula + "ID_PROC " ;
		 stSqlValores+=   stVirgula + "? " ;
		 ps.adicionarLong(dados.getIdProcesso());  
		 
		stSqlCampos+= ", DATA_ALTERACAO " ;
		stSqlValores+=", ?";
		ps.adicionarDate(new Date());
		dados.setDataAlteracao(Funcoes.dateToStringSoData(new Date()));
		
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		
		dados.setId(executarInsert(stSql,"ID_SITUACAO_ATUAL_EXE",ps)); 
	} 

	public void alterar(SituacaoAtualExecucaoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		stSql= "UPDATE PROJUDI.SITUACAO_ATUAL_EXE SET  ";
		stSql+= "ID_LOCAL_CUMP_PENA = ?";		 
		ps.adicionarLong(dados.getIdLocalCumprimentoPena());  
		stSql+= ",ID_REGIME_EXE = ?";
		ps.adicionarLong(dados.getIdRegime());
		stSql+= ",ID_EVENTO_EXE_STATUS = ?";
		ps.adicionarLong(dados.getIdEventoExecucaoStatus());
		stSql+= ",ID_FORMA_CUMPRIMENTO_EXE = ?";
		ps.adicionarLong(dados.getIdFormaCumprimento());
		stSql+= ",DATA_ALTERACAO = ? ";
		ps.adicionarDate(new Date());
		dados.setDataAlteracao(Funcoes.dateToStringSoData(new Date()));
		stSql+= ",DATA_FUGA = ? ";
		ps.adicionarDate(dados.getDataFuga());
		stSql += " WHERE ID_SITUACAO_ATUAL_EXE  = ? "; 
		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	} 

	public void excluir(String chave) throws Exception {
		String stSql="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "DELETE FROM PROJUDI.SITUACAO_ATUAL_EXE";
		stSql += " WHERE ID_SITUACAO_ATUAL_EXE = ?";
		ps.adicionarLong(chave); 
			executarUpdateDelete(stSql,ps);

		
	} 

	public SituacaoAtualExecucaoDt consultarId(String idSituacaoAtualExecucao )  throws Exception {
		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		SituacaoAtualExecucaoDt dados=null;

		stSql= "SELECT * FROM PROJUDI.VIEW_SITUACAO_ATUAL_EXE " +
				" WHERE ID_SITUACAO_ATUAL_EXE = ?";	
		ps.adicionarLong(idSituacaoAtualExecucao); 

		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				dados= new SituacaoAtualExecucaoDt();
				associarDt(dados, rs1);
			}
		}finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return dados; 
	}

	public List consultarIdProcesso(String idProcesso)  throws Exception {
		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		SituacaoAtualExecucaoDt dados=null;
		List lista = new ArrayList();
		
		stSql= "SELECT * FROM PROJUDI.VIEW_SITUACAO_ATUAL_EXE " +
				" WHERE ID_PROC = ?" +
				" ORDER BY ID_SITUACAO_ATUAL_EXE";	
		ps.adicionarLong(idProcesso); 

		try{
			rs1 = consultar(stSql,ps);
			String idSituacaoAtual = "";
			while (rs1.next()) {
				if (!rs1.getString("ID_SITUACAO_ATUAL_EXE").equalsIgnoreCase(idSituacaoAtual)){
					dados = new SituacaoAtualExecucaoDt();
					associarDt(dados, rs1);
					lista.add(dados);
					idSituacaoAtual = rs1.getString("ID_SITUACAO_ATUAL_EXE");
				} else {
					if (rs1.getString("ID_SITUACAO_ATUAL_MODALIDADE") != null){
						boolean inserir = true;
						if (((SituacaoAtualExecucaoDt)lista.get(lista.size()-1)).getListaSituacaoAtualModalidadeDt() != null){
							for (SituacaoAtualModalidadeDt sitMod : (List<SituacaoAtualModalidadeDt>)((SituacaoAtualExecucaoDt)lista.get(lista.size()-1)).getListaSituacaoAtualModalidadeDt()) {
								if (sitMod.getId().equals(rs1.getString("ID_SITUACAO_ATUAL_MODALIDADE"))){
									inserir = false;
									break;
								}
							}
						}
						if (inserir){
							SituacaoAtualModalidadeDt modalidade = new SituacaoAtualModalidadeDt();
							associarDtModalidade(modalidade, rs1);
							((SituacaoAtualExecucaoDt)lista.get(lista.size()-1)).addListaSituacaoAtualModalidadeDt(modalidade);	
						}
					}
					
					if (rs1.getString("ID_SITUACAO_ATUAL_TIPO_PENA") != null){
						boolean inserir = true;
						if (((SituacaoAtualExecucaoDt)lista.get(lista.size()-1)).getListaSituacaoAtualTipoPenaDt() != null){
							for (SituacaoAtualTipoPenaDt sitTipoPena : (List<SituacaoAtualTipoPenaDt>)((SituacaoAtualExecucaoDt)lista.get(lista.size()-1)).getListaSituacaoAtualTipoPenaDt()) {
								if (sitTipoPena.getId().equals(rs1.getString("ID_SITUACAO_ATUAL_TIPO_PENA"))){
									inserir = false;
									break;
								}
							}
						}
						if (inserir){
							SituacaoAtualTipoPenaDt tipoPena = new SituacaoAtualTipoPenaDt();
							associarDtTipoPena(tipoPena, rs1);
							((SituacaoAtualExecucaoDt)lista.get(lista.size()-1)).addListaSituacaoAtualTipoPenaDt(tipoPena);	
						}
					}
				}
			}
		
		}finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return lista; 
	}
	
	protected void associarDt(SituacaoAtualExecucaoDt dados, ResultSetTJGO rs )  throws Exception {
		
		dados.setId(rs.getString("ID_SITUACAO_ATUAL_EXE"));
		dados.setIdLocalCumprimentoPena(rs.getString("ID_LOCAL_CUMP_PENA"));
		dados.setIdRegime(rs.getString("ID_REGIME_EXE"));
		dados.setIdEventoExecucaoStatus(rs.getString("ID_EVENTO_EXE_STATUS"));
		dados.setIdFormaCumprimento(rs.getString("ID_FORMA_CUMPRIMENTO_EXE"));
		dados.setFormaCumprimento(rs.getString("FORMA_CUMPRIMENTO_EXE"));
		dados.setLocalCumprimentoPena(rs.getString("LOCAL_CUMP_PENA"));
		dados.setRegime(rs.getString("REGIME_EXE"));
		dados.setEventoExecucaoStatus(rs.getString("EVENTO_EXE_STATUS"));
		dados.setDataAlteracao( Funcoes.FormatarData(rs.getDateTime("DATA_ALTERACAO")));
		dados.setIdProcesso(rs.getString("ID_PROC"));
		dados.setDataFuga(Funcoes.FormatarData(rs.getDateTime("DATA_FUGA")));
		
		if (rs.getString("ID_SITUACAO_ATUAL_MODALIDADE") != null){
			SituacaoAtualModalidadeDt modalidade = new SituacaoAtualModalidadeDt();
			associarDtModalidade(modalidade, rs);
			dados.addListaSituacaoAtualModalidadeDt(modalidade);	
		}
		
		if (rs.getString("ID_SITUACAO_ATUAL_TIPO_PENA") != null){
			SituacaoAtualTipoPenaDt pena = new SituacaoAtualTipoPenaDt();
			associarDtTipoPena(pena, rs);
			dados.addListaSituacaoAtualTipoPenaDt(pena);	
		}
		
		
	}
	
	protected void associarDtModalidade(SituacaoAtualModalidadeDt dados, ResultSetTJGO rs )  throws Exception {
		
		if (rs.getString("ID_SITUACAO_ATUAL_MODALIDADE") != null){
			dados.setId(rs.getString("ID_SITUACAO_ATUAL_MODALIDADE"));
			dados.setIdModalidade(rs.getString("ID_MODALIDADE"));
			dados.setModalidade(rs.getString("MODALIDADE"));
			dados.setIdSituacaoAtualExecucao(rs.getString("ID_SITUACAO_ATUAL_EXE"));
		}
		
		
	}
	
	protected void associarDtTipoPena(SituacaoAtualTipoPenaDt dados, ResultSetTJGO rs )  throws Exception {
		
		if (rs.getString("ID_SITUACAO_ATUAL_TIPO_PENA") != null){
			dados.setId(rs.getString("ID_SITUACAO_ATUAL_TIPO_PENA"));
			dados.setIdPenaExecucaoTipo(rs.getString("ID_PENA_EXE_TIPO"));
			dados.setIdSituacaoAtualExecucao(rs.getString("ID_SITUACAO_ATUAL_EXE"));
			dados.setPenaExecucaoTipo(rs.getString("PENA_EXE_TIPO"));
		}
				
	}
} 
