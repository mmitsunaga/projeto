package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoParteDebitoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class ProcessoParteDebitoPsGen extends Persistencia {


/**
     * 
     */
    private static final long serialVersionUID = -8679176360145815414L;

    //---------------------------------------------------------
	public ProcessoParteDebitoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(ProcessoParteDebitoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		stSqlCampos= "INSERT INTO PROJUDI.PROC_PARTE_DEBITO ("; 
		
		stSqlValores +=  " Values (";
	
		if ((dados.getId_ProcessoDebito().length()>0)){
			stSqlCampos+=   stVirgula + "ID_PROC_DEBITO " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarLong(dados.getId_ProcessoDebito());
			
			stVirgula=",";
		}
		if ((dados.getId_ProcessoParte().length()>0)){			
			stSqlCampos+=   stVirgula + "ID_PROC_PARTE " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarLong(dados.getId_ProcessoParte());
			
			stVirgula=",";
		}
		if ((dados.getId_GuiaEmissao().length()>0)){			
			stSqlCampos+=   stVirgula + "ID_GUIA_EMIS " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarLong(dados.getId_GuiaEmissao());
			
			stVirgula=",";
		}
		if ((dados.getProcessoNumeroPROAD().length()>0)){			
			stSqlCampos+=   stVirgula + "NUMERO_PROAD " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarLong(dados.getProcessoNumeroPROAD());
			
			stVirgula=",";
		}
		if ((dados.getId_ProcessoDebitoStatus().length()>0)){			
			stSqlCampos+=   stVirgula + "ID_PROC_DEBITO_STATUS " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarLong(dados.getId_ProcessoDebitoStatus());
			
			stVirgula=",";
		}
		if ((dados.getDataBaixa().length()>0)){			
			stSqlCampos+=   stVirgula + "DATA_BAIXA " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarDate(dados.getDataBaixa());
			
			stVirgula=",";
		}
		if ((dados.getEnderecoPartePROAD().length()>0)){			
			stSqlCampos+=   stVirgula + "ENDERECO_PARTE_PROAD " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarString(dados.getEnderecoPartePROAD());
			
			stVirgula=",";
		}
		if ((dados.getDataDebitoPROAD().length()>0)){			
			stSqlCampos+=   stVirgula + "DATA_VENCIMENTO_PROAD " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarDate(dados.getDataDebitoPROAD());
			
			stVirgula=",";
		}
		if ((dados.getValorCreditoPROAD().length()>0)){			
			stSqlCampos+=   stVirgula + "VALOR_CREDITO_PROAD " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarDecimal(dados.getValorCreditoPROAD());
			
			stVirgula=",";
		}
		if ((dados.getDividaSolidaria() != null &&
			dados.getDividaSolidaria().length()>0)){			
			stSqlCampos+=   stVirgula + "DIVIDA_SOLIDARIA " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarBoolean(dados.getDividaSolidaria());
			
			stVirgula=",";
		}
		stSqlCampos+=   stVirgula + "OBSERVACAO_PROC_DEBITO_STATUS " ;
		stSqlValores+=   stVirgula + "? " ;
		ps.adicionarString(dados.getObservacaoProcessoDebitoStatus());
		
		stVirgula=",";
				
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		
		dados.setId(executarInsert(stSql, "ID_PROC_PARTE_DEBITO", ps));		 
	} 

//---------------------------------------------------------
	public void alterar(ProcessoParteDebitoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		stSql= "UPDATE PROJUDI.PROC_PARTE_DEBITO SET  ";
		stSql+= "ID_PROC_DEBITO  = ? "; ps.adicionarLong(dados.getId_ProcessoDebito()); 
		stSql+= ",ID_PROC_PARTE  = ? "; ps.adicionarLong( dados.getId_ProcessoParte());
		stSql+= ",ID_GUIA_EMIS  = ? "; ps.adicionarLong( dados.getId_GuiaEmissao());
		stSql+= ",NUMERO_PROAD  = ? "; ps.adicionarLong( dados.getProcessoNumeroPROAD());
		stSql+= ",ID_PROC_DEBITO_STATUS  = ? "; ps.adicionarLong( dados.getId_ProcessoDebitoStatus());
		stSql+= ",DATA_BAIXA  = ? "; ps.adicionarDateTime( dados.getDataBaixa());
		stSql+= ",ENDERECO_PARTE_PROAD  = ? "; ps.adicionarString(dados.getEnderecoPartePROAD());
		stSql+= ",DATA_VENCIMENTO_PROAD  = ? "; ps.adicionarDateTime( dados.getDataDebitoPROAD());
		stSql+= ",VALOR_CREDITO_PROAD  = ? "; ps.adicionarDecimal(dados.getValorCreditoPROAD());
		stSql+= ",DIVIDA_SOLIDARIA  = ? "; ps.adicionarBoolean(dados.getDividaSolidaria());
		stSql+= ",OBSERVACAO_PROC_DEBITO_STATUS  = ? "; ps.adicionarString(dados.getObservacaoProcessoDebitoStatus());
		stSql+= " WHERE ID_PROC_PARTE_DEBITO  = ? "; ps.adicionarLong( dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		stSql= "DELETE FROM PROJUDI.PROC_PARTE_DEBITO";
		stSql+= "  WHERE ID_PROC_PARTE_DEBITO = ? "; ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ProcessoParteDebitoDt consultarId(String id_processopartedebito )  throws Exception {

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";
		ProcessoParteDebitoDt Dados=null;
		ResultSetTJGO rs1 = null;

		stSql= "SELECT * FROM PROJUDI.VIEW_PROC_PARTE_DEBITO WHERE ID_PROC_PARTE_DEBITO = ? "; ps.adicionarLong(id_processopartedebito);

		try{
			rs1 = consultar(stSql, ps);
			if (rs1.next()) {
				Dados= new ProcessoParteDebitoDt();
				associarDt(Dados, rs1);
			}
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return Dados; 
	}

	protected void associarDt( ProcessoParteDebitoDt Dados, ResultSetTJGO rs1 )  throws Exception {
		Dados.setId(rs1.getString("ID_PROC_PARTE_DEBITO"));
		Dados.setProcessoDebito(rs1.getString("PROC_DEBITO"));
		Dados.setId_ProcessoDebito( rs1.getString("ID_PROC_DEBITO"));
		Dados.setId_ProcessoParte( rs1.getString("ID_PROC_PARTE"));
		Dados.setNome( rs1.getString("NOME"));
		Dados.setCodigoTemp( rs1.getString("CODIGO_TEMP"));
		Dados.setId_GuiaEmissao(rs1.getString("ID_GUIA_EMIS"));	
		Dados.setProcessoNumeroPROAD(rs1.getString("NUMERO_PROAD"));
		Dados.setId_ProcessoDebitoStatus(rs1.getString("ID_PROC_DEBITO_STATUS"));
		Dados.setProcessoDebitoStatus(rs1.getString("PROC_DEBITO_STATUS"));
		Dados.setDataBaixa(Funcoes.FormatarData(rs1.getString("DATA_BAIXA")));
		Dados.setDividaSolidaria(Funcoes.FormatarLogico(rs1.getString("DIVIDA_SOLIDARIA")));
		Dados.setTipoParte(rs1.getString("PROC_PARTE_TIPO_CODIGO"));
		Dados.setObservacaoProcessoDebitoStatus(rs1.getString("OBSERVACAO_PROC_DEBITO_STATUS"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;

		stSql= "SELECT ID_PROC_PARTE_DEBITO, PROC_DEBITO FROM PROJUDI.VIEW_PROC_PARTE_DEBITO WHERE PROC_DEBITO LIKE ? ";
		ps.adicionarString( descricao +"%");
		stSql+= " ORDER BY PROC_DEBITO ";		
		try{

			rs1 = consultar(stSql, ps);
			
			while (rs1.next()) {
				ProcessoParteDebitoDt obTemp = new ProcessoParteDebitoDt();
				obTemp.setId(rs1.getString("ID_PROC_PARTE_DEBITO"));
				obTemp.setProcessoDebito(rs1.getString("PROC_DEBITO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PROC_PARTE_DEBITO WHERE PROC_DEBITO LIKE ? ";
			rs2 = consultar(stSql, ps);
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}			
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp; 
	}
} 
