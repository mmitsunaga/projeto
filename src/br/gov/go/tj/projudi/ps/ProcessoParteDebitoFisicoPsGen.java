package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoParteDebitoFisicoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class ProcessoParteDebitoFisicoPsGen extends Persistencia {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4009722182987786652L;

	//---------------------------------------------------------
	public ProcessoParteDebitoFisicoPsGen() {

	}
	//---------------------------------------------------------
	public void inserir(ProcessoParteDebitoFisicoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		stSqlCampos= "INSERT INTO PROJUDI.PROC_PARTE_DEBITO_FISICO ("; 
		
		stSqlValores +=  " Values (";
	
		if ((dados.getId_ProcessoDebito().length()>0)){
			stSqlCampos+=   stVirgula + "ID_PROC_DEBITO " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarLong(dados.getId_ProcessoDebito());
			
			stVirgula=",";
		}
		if ((dados.getProcessoNumeroCompleto().length()>0)){			
			stSqlCampos+=   stVirgula + "PROC_NUMERO " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarString(dados.getProcessoNumeroCompleto());
			stVirgula=",";
		}
		if ((dados.getCodigoExternoServentia().length()>0)){			
			stSqlCampos+=   stVirgula + "CODG_ESCRIVANIA " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarLong(dados.getCodigoExternoServentia());
			stVirgula=",";
		}
		if ((dados.getDescricaoServentia().length()>0)){			
			stSqlCampos+=   stVirgula + "DESC_ESCRIVANIA " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarString(dados.getDescricaoServentia());			
			stVirgula=",";
		}
		if ((dados.getId_ProcessoParte().length()>0)){			
			stSqlCampos+=   stVirgula + "ISN_PARTE " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarLong(dados.getId_ProcessoParte());			
			stVirgula=",";
		}
		if ((dados.getNome().length()>0)){			
			stSqlCampos+=   stVirgula + "NOME_PARTE " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarString(dados.getNome());			
			stVirgula=",";
		}
		if ((dados.getTipoParte().length()>0)){			
			stSqlCampos+=   stVirgula + "TIPO_PARTE " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarString(dados.getTipoParte());			
			stVirgula=",";
		}
		if ((dados.getNomeSimplificado().length()>0)){			
			stSqlCampos+=   stVirgula + "NOME_SIMPLIFICADO " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarString(dados.getNomeSimplificado());			
			stVirgula=",";
		}
		if ((dados.getCpfParte().length()>0)){			
			stSqlCampos+=   stVirgula + "CPF_CNPJ_PARTE " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarLong(dados.getCpfParte());			
			stVirgula=",";
		}
		if ((dados.getNumeroGuia() != null && dados.getNumeroGuia().length()>0)){			
			stSqlCampos+=   stVirgula + "NUMERO_GUIA_COMPLETO " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarLong(dados.getNumeroGuia());			
			stVirgula=",";
		}
		if ((dados.getValorTotalGuia() != null && dados.getValorTotalGuia().length()>0)){			
			stSqlCampos+=   stVirgula + "VALOR_GUIA " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarDecimal(Funcoes.FormatarDecimal(dados.getValorTotalGuia()));		
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
		if ((dados.getCodigoTemp().length()>0)){			
			stSqlCampos+=   stVirgula + "CODIGO_TEMP " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarLong(dados.getCodigoTemp());
			
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
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		
		dados.setId(executarInsert(stSql, "ID_PROC_PARTE_DEBITO_FISICO", ps));		 
	} 

//---------------------------------------------------------
	public void alterar(ProcessoParteDebitoFisicoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		stSql= "UPDATE PROJUDI.PROC_PARTE_DEBITO_FISICO SET  ";
		stSql+= "ID_PROC_DEBITO  = ? "; ps.adicionarLong(dados.getId_ProcessoDebito()); 
		stSql+= ",PROC_NUMERO  = ? "; ps.adicionarString(dados.getProcessoNumeroCompleto());
		stSql+= ",CODG_ESCRIVANIA  = ? "; ps.adicionarLong( dados.getCodigoExternoServentia());
		stSql+= ",DESC_ESCRIVANIA  = ? "; ps.adicionarString(dados.getDescricaoServentia());
		stSql+= ",ISN_PARTE  = ? "; ps.adicionarLong( dados.getId_ProcessoParte());
		stSql+= ",NOME_PARTE  = ? "; ps.adicionarString( dados.getNome());
		stSql+= ",TIPO_PARTE  = ? "; ps.adicionarLong( dados.getTipoParte());
		stSql+= ",NOME_SIMPLIFICADO  = ? "; ps.adicionarString( dados.getNomeSimplificado());		
		stSql+= ",CPF_CNPJ_PARTE  = ? "; ps.adicionarLong( dados.getCpfParte());
		stSql+= ",NUMERO_GUIA_COMPLETO  = ? "; ps.adicionarLong( dados.getNumeroGuia());
		stSql+= ",VALOR_GUIA  = ? "; ps.adicionarDecimal(Funcoes.FormatarDecimal(dados.getValorTotalGuia()));		
		stSql+= ",NUMERO_PROAD  = ? "; ps.adicionarLong( dados.getProcessoNumeroPROAD());
		stSql+= ",ID_PROC_DEBITO_STATUS  = ? "; ps.adicionarLong( dados.getId_ProcessoDebitoStatus());
		stSql+= ",DATA_BAIXA  = ? "; ps.adicionarDate( dados.getDataBaixa());
		stSql+= ",CODIGO_TEMP  = ? "; ps.adicionarLong(dados.getCodigoTemp());
		stSql+= ",ENDERECO_PARTE_PROAD  = ? "; ps.adicionarString(dados.getEnderecoPartePROAD());
		stSql+= ",DATA_VENCIMENTO_PROAD  = ? "; ps.adicionarDate( dados.getDataDebitoPROAD());
		stSql+= ",VALOR_CREDITO_PROAD  = ? "; ps.adicionarDecimal(dados.getValorCreditoPROAD());
		stSql+= ",DIVIDA_SOLIDARIA  = ? "; ps.adicionarBoolean(dados.getDividaSolidaria());
		stSql+= " WHERE ID_PROC_PARTE_DEBITO_FISICO  = ? "; ps.adicionarLong( dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		stSql= "DELETE FROM PROJUDI.PROC_PARTE_DEBITO_FISICO";
		stSql+= "  WHERE ID_PROC_PARTE_DEBITO_FISICO = ? "; ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ProcessoParteDebitoFisicoDt consultarId(String id_processopartedebitofisico )  throws Exception {

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";
		ProcessoParteDebitoFisicoDt Dados=null;
		ResultSetTJGO rs1 = null;

		stSql= "SELECT * FROM PROJUDI.VIEW_PROC_PARTE_DEBITO_FISICO WHERE ID_PROC_PARTE_DEBITO_FISICO = ? "; ps.adicionarLong(id_processopartedebitofisico);

		try{
			rs1 = consultar(stSql, ps);
			if (rs1.next()) {
				Dados= new ProcessoParteDebitoFisicoDt();
				associarDt(Dados, rs1);
			}
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return Dados; 
	}

	protected void associarDt( ProcessoParteDebitoFisicoDt Dados, ResultSetTJGO rs1 )  throws Exception {
		Dados.setId(rs1.getString("ID_PROC_PARTE_DEBITO_FISICO"));
		Dados.setProcessoDebito(rs1.getString("PROC_DEBITO"));
		Dados.setId_ProcessoDebito( rs1.getString("ID_PROC_DEBITO"));
		Dados.setProcessoNumeroCompleto(rs1.getString("PROC_NUMERO"));
		Dados.setCodigoExternoServentia(rs1.getString("CODG_ESCRIVANIA"));
		Dados.setDescricaoServentia(rs1.getString("DESC_ESCRIVANIA"));
		Dados.setId_ProcessoParte( rs1.getString("ISN_PARTE"));		
		Dados.setNome( rs1.getString("NOME_PARTE"));
		Dados.setNomeSimplificado(rs1.getString("NOME_SIMPLIFICADO"));
		Dados.setTipoParte(rs1.getString("TIPO_PARTE"));
		Dados.setCpfParte(rs1.getString("CPF_CNPJ_PARTE"));
		Dados.setNumeroGuia(rs1.getString("NUMERO_GUIA_COMPLETO"));
		Dados.setValorTotalGuia(rs1.getString("VALOR_GUIA"));
		Dados.setProcessoNumeroPROAD(rs1.getString("NUMERO_PROAD"));
		Dados.setId_ProcessoDebitoStatus(rs1.getString("ID_PROC_DEBITO_STATUS"));
		Dados.setDataBaixa(Funcoes.FormatarData(rs1.getString("DATA_BAIXA")));
		/*Dados.setEnderecoPartePROAD(rs1.getString("ENDERECO_PARTE_PROAD"));
		Dados.setDataDebitoPROAD(rs1.getString("DATA_VENCIMENTO_PROAD"));
		Dados.setValorCreditoPROAD(rs1.getString("VALOR_CREDITO_PROAD"));*/
		Dados.setDividaSolidaria(Funcoes.FormatarLogico(rs1.getString("DIVIDA_SOLIDARIA")));
		Dados.setCodigoTemp(rs1.getString("CODIGO_TEMP"));		
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;

		stSql= "SELECT ID_PROC_PARTE_DEBITO_FISICO, PROC_DEBITO FROM PROJUDI.VIEW_PROC_PARTE_DEBITO_FISICO WHERE PROC_DEBITO LIKE ? ";
		ps.adicionarString( descricao +"%");
		stSql+= " ORDER BY PROC_DEBITO ";		
		try{

			rs1 = consultar(stSql, ps);
			
			while (rs1.next()) {
				ProcessoParteDebitoFisicoDt obTemp = new ProcessoParteDebitoFisicoDt();
				obTemp.setId(rs1.getString("ID_PROC_PARTE_DEBITO_FISICO"));
				obTemp.setProcessoDebito(rs1.getString("PROC_DEBITO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PROC_PARTE_DEBITO_FISICO WHERE PROC_DEBITO LIKE ? ";
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
