package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoParteDebitoCadinDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDebitoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDebitoFisicoDt;
import br.gov.go.tj.projudi.ne.boletos.BoletoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import br.gov.go.tj.utils.TJDataHora;

public class ProcessoParteDebitoCadinPs extends Persistencia {

   /**
	 * 
	 */
	private static final long serialVersionUID = 2686516702979863172L;

	public ProcessoParteDebitoCadinPs(Connection conexao) {
		Conexao = conexao;
	}

	public void inserir(ProcessoParteDebitoCadinDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		String stNomeTabela = (dados.isFisico() ? "PROC_PARTE_DEBITO_FISICO_CADIN" : "PROC_PARTE_DEBITO_CADIN");
		String stNomeCampoId = (dados.isFisico() ? "ID_PROC_PARTE_DEBITO_FISICO_CD" : "ID_PROC_PARTE_DEBITO_CD");
		String stNomeCampoIdProcParteDebito = (dados.isFisico() ? "ID_PROC_PARTE_DEBITO_FISICO" : "ID_PROC_PARTE_DEBITO");
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		stSqlCampos= "INSERT INTO PROJUDI." + stNomeTabela + " ("; 
		
		stSqlValores +=  " Values (";
	
		if ((dados.getId_ProcessoParteDebito() != null && dados.getId_ProcessoParteDebito().trim().length()>0)){
			stSqlCampos+=   stVirgula + stNomeCampoIdProcParteDebito + " " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarLong(dados.getId_ProcessoParteDebito());
			
			stVirgula=",";
		}
		if ((dados.getNumeroLote() != null && dados.getNumeroLote().trim().length()>0)){			
			stSqlCampos+=   stVirgula + "NUMERO_LOTE " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarLong(dados.getNumeroLote());
			stVirgula=",";
		}
		if ((dados.getDataGeracao() != null && dados.getDataGeracao().trim().length()>0)){			
			stSqlCampos+=   stVirgula + "DATA_GERACAO " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarDateTime(dados.getDataGeracao());
			stVirgula=",";
		}
		if ((dados.getTipoDocumento() != null && dados.getTipoDocumento().trim().length()>0)){			
			stSqlCampos+=   stVirgula + "TIPO_DOCUMENTO " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarLong(dados.getTipoDocumento());			
			stVirgula=",";
		}
		if ((dados.getNumeroDocumento() != null && dados.getNumeroDocumento().trim().length()>0)){			
			stSqlCampos+=   stVirgula + "NUMERO_DOCUMENTO " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarLong(dados.getNumeroDocumento());			
			stVirgula=",";
		}
		if ((dados.getNomePessoa() != null && dados.getNomePessoa().trim().length()>0)){			
			stSqlCampos+=   stVirgula + "NOME_PESSOA " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarString(dados.getNomePessoa());			
			stVirgula=",";
		}
		if ((dados.getNumeroProcesso() != null && dados.getNumeroProcesso().trim().length()>0)){			
			stSqlCampos+=   stVirgula + "NUMERO_PROCESSO " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarString(dados.getNumeroProcesso());			
			stVirgula=",";
		}
		if ((dados.getNaturezaPendencia() != null && dados.getNaturezaPendencia().trim().length()>0)){			
			stSqlCampos+=   stVirgula + "NATUREZA_PENDENCIA " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarLong(dados.getNaturezaPendencia());			
			stVirgula=",";
		}
		if ((dados.getCategoriaPendencia() != null && dados.getCategoriaPendencia().trim().length()>0)){			
			stSqlCampos+=   stVirgula + "CATEGORIA_PENDENCIA" ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarLong(dados.getCategoriaPendencia());			
			stVirgula=",";
		}
		if ((dados.getDataVencimentoDebito() != null && dados.getDataVencimentoDebito().trim().length()>0)){			
			stSqlCampos+=   stVirgula + "DATA_VENCIMENTO_DEBITO " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarDateTime(dados.getDataVencimentoDebito());			
			stVirgula=",";
		}
		if ((dados.getValorDevido() != null && dados.getValorDevido().trim().length()>0)){			
			stSqlCampos+=   stVirgula + "VALOR_DEVIDO " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarDecimal(Funcoes.FormatarDecimal(dados.getValorDevido()));
			
			stVirgula=",";
		}
		if ((dados.getMeioEnvioComunicado() != null && dados.getMeioEnvioComunicado().trim().length()>0)){			
			stSqlCampos+=   stVirgula + "MEIO_ENVIO_COMUNICADO " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarLong(dados.getMeioEnvioComunicado());		
			stVirgula=",";
		}
		if ((dados.getDataEnvioComunicado() != null && dados.getDataEnvioComunicado().trim().length()>0)){			
			stSqlCampos+=   stVirgula + "DATA_ENVIO_COMUNICADO " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarDateTime(dados.getDataEnvioComunicado());
			
			stVirgula=",";
		}
		if ((dados.getDataCienciaComunicado() != null && dados.getDataCienciaComunicado().trim().length()>0)){			
			stSqlCampos+=   stVirgula + "DATA_CIENCIA_COMUNICADO " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarDateTime(dados.getDataEnvioComunicado());
			
			stVirgula=",";
		}
		if ((dados.getTipoLogradouro() != null && dados.getTipoLogradouro().trim().length()>0)){			
			stSqlCampos+=   stVirgula + "TIPO_LOGRADOURO " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarString(dados.getTipoLogradouro());
			
			stVirgula=",";
		}
		if ((dados.getNomeLogradouro() != null && dados.getNomeLogradouro().trim().length()>0)){			
			stSqlCampos+=   stVirgula + "NOME_LOGRADOURO " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarString(dados.getNomeLogradouro());
			
			stVirgula=",";
		}
		if ((dados.getNumeroEndereco() != null && dados.getNumeroEndereco().trim().length()>0)){			
			stSqlCampos+=   stVirgula + "NUMERO_ENDERECO " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarString(dados.getNumeroEndereco());
			
			stVirgula=",";
		}
		if ((dados.getNumeroLoteEndereco() != null && dados.getNumeroLoteEndereco().trim().length()>0)){			
			stSqlCampos+=   stVirgula + "NUMERO_LOTE_ENDERECO " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarString(dados.getNumeroLoteEndereco());
			
			stVirgula=",";
		}
		if ((dados.getNumeroQuadraEndereco() != null && dados.getNumeroQuadraEndereco().trim().length()>0)){			
			stSqlCampos+=   stVirgula + "NUMERO_QUADRA_ENDERECO " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarString(dados.getNumeroQuadraEndereco());
			
			stVirgula=",";
		}
		if ((dados.getDescricaoComplementoEndereco() != null && dados.getDescricaoComplementoEndereco().trim().length()>0)){			
			stSqlCampos+=   stVirgula + "DESC_COMPLEMENTO_ENDERECO " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarString(dados.getDescricaoComplementoEndereco());
			
			stVirgula=",";
		}
		if ((dados.getNomeBairro() != null && dados.getNomeBairro().trim().length()>0)){			
			stSqlCampos+=   stVirgula + "NOME_BAIRRO " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarString(dados.getNomeBairro());
			
			stVirgula=",";
		}
		if ((dados.getCodigoCep() != null && dados.getCodigoCep().trim().length()>0)){			
			stSqlCampos+=   stVirgula + "CODIGO_CEP " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarLong(dados.getCodigoCep());
			
			stVirgula=",";
		}
		if ((dados.getMunicipioEndereco() != null && dados.getMunicipioEndereco().trim().length()>0)){			
			stSqlCampos+=   stVirgula + "NOME_MUNICIPIO " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarString(dados.getMunicipioEndereco());
			
			stVirgula=",";
		}
		if ((dados.getUFEndereco() != null && dados.getUFEndereco().trim().length()>0)){			
			stSqlCampos+=   stVirgula + "NOME_UF " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarString(dados.getUFEndereco());
			
			stVirgula=",";
		}
		if ((dados.getObservacaoPendencia() != null && dados.getObservacaoPendencia().trim().length()>0)){			
			stSqlCampos+=   stVirgula + "OBSERVACAO_PENDENCIA " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarString(dados.getObservacaoPendencia());
			
			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		
		dados.setId(executarInsert(stSql, stNomeCampoId, ps));		 
	} 

	public void alterar(ProcessoParteDebitoCadinDt dados) throws Exception{

		String stNomeTabela = (dados.isFisico() ? "PROC_PARTE_DEBITO_FISICO_CADIN" : "PROC_PARTE_DEBITO_CADIN");
		String stNomeCampoId = (dados.isFisico() ? "ID_PROC_PARTE_DEBITO_FISICO_CD" : "ID_PROC_PARTE_DEBITO_CD");
		String stNomeCampoIdProcParteDebito = (dados.isFisico() ? "ID_PROC_PARTE_DEBITO_FISICO" : "ID_PROC_PARTE_DEBITO");
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		stSql= "UPDATE PROJUDI." + stNomeTabela + " SET  ";
		stSql+= stNomeCampoIdProcParteDebito + " = ? "; ps.adicionarLong(dados.getId_ProcessoParteDebito()); 
		stSql+= ",NUMERO_LOTE  = ? "; ps.adicionarLong(dados.getNumeroLote());
		stSql+= ",DATA_GERACAO  = ? "; ps.adicionarDateTime(dados.getDataGeracao());
		stSql+= ",TIPO_DOCUMENTO  = ? "; ps.adicionarLong(dados.getTipoDocumento());
		stSql+= ",NUMERO_DOCUMENTO  = ? "; ps.adicionarLong( dados.getNumeroDocumento());
		stSql+= ",NOME_PESSOA  = ? "; ps.adicionarString( dados.getNomePessoa());
		stSql+= ",NUMERO_PROCESSO  = ? "; ps.adicionarString( dados.getNumeroProcesso());
		stSql+= ",NATUREZA_PENDENCIA  = ? "; ps.adicionarLong( dados.getNaturezaPendencia());		
		stSql+= ",CATEGORIA_PENDENCIA  = ? "; ps.adicionarLong( dados.getCategoriaPendencia());
		stSql+= ",DATA_VENCIMENTO_DEBITO  = ? "; ps.adicionarDateTime(dados.getDataVencimentoDebito());		
		stSql+= ",VALOR_DEVIDO  = ? "; ps.adicionarDecimal(Funcoes.FormatarDecimal(dados.getValorDevido()));
		stSql+= ",MEIO_ENVIO_COMUNICADO  = ? "; ps.adicionarLong( dados.getMeioEnvioComunicado());
		stSql+= ",DATA_ENVIO_COMUNICADO  = ? "; ps.adicionarDate( dados.getDataEnvioComunicado());
		stSql+= ",DATA_CIENCIA_COMUNICADO  = ? "; ps.adicionarDate(dados.getDataCienciaComunicado());
		stSql+= ",TIPO_LOGRADOURO  = ? "; ps.adicionarString(dados.getTipoLogradouro());
		stSql+= ",NOME_LOGRADOURO  = ? "; ps.adicionarString( dados.getNomeLogradouro());
		stSql+= ",NUMERO_ENDERECO  = ? "; ps.adicionarString(dados.getNumeroEndereco());
		stSql+= ",NUMERO_QUADRA_ENDERECO  = ? "; ps.adicionarString(dados.getNumeroQuadraEndereco());
		stSql+= ",DESC_COMPLEMENTO_ENDERECO  = ? "; ps.adicionarString(dados.getDescricaoComplementoEndereco());
		stSql+= ",NOME_BAIRRO  = ? "; ps.adicionarString(dados.getNomeBairro());
		stSql+= ",CODIGO_CEP  = ? "; ps.adicionarLong(dados.getCodigoCep());
		stSql+= ",NOME_MUNICIPIO  = ? "; ps.adicionarString(dados.getMunicipioEndereco());
		stSql+= ",NOME_UF  = ? "; ps.adicionarString(dados.getUFEndereco());
		stSql+= ",OBSERVACAO_PENDENCIA  = ? "; ps.adicionarString(dados.getObservacaoPendencia());
		stSql+= " WHERE " + stNomeCampoId + "  = ? "; ps.adicionarLong( dados.getId()); 

		executarUpdateDelete(stSql,ps);	
	}

	public void excluir(ProcessoParteDebitoCadinDt dados) throws Exception {

		String stNomeTabela = (dados.isFisico() ? "PROC_PARTE_DEBITO_FISICO_CADIN" : "PROC_PARTE_DEBITO_CADIN");
		String stNomeCampoId = (dados.isFisico() ? "ID_PROC_PARTE_DEBITO_FISICO_CD" : "ID_PROC_PARTE_DEBITO_CD");
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		stSql= "DELETE FROM PROJUDI." + stNomeTabela;
		stSql+= "  WHERE " + stNomeCampoId + " = ? "; ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);	
	}

	public List<ProcessoParteDebitoCadinDt> obtenhaRegistros(String numeroLote) throws Exception {
		List<ProcessoParteDebitoCadinDt> listaRegistros = new ArrayList<ProcessoParteDebitoCadinDt>();
		obtenhaRegistros(numeroLote, false, listaRegistros);
		obtenhaRegistros(numeroLote, true, listaRegistros);
		return listaRegistros;
	}
	
	private void obtenhaRegistros(String numeroLote, boolean isFisico, List<ProcessoParteDebitoCadinDt> listaRegistros) throws Exception {
		String stNomeTabela = (isFisico ? "PROC_PARTE_DEBITO_FISICO_CADIN" : "PROC_PARTE_DEBITO_CADIN");
		String stNomeCampoId = (isFisico ? "ID_PROC_PARTE_DEBITO_FISICO_CD" : "ID_PROC_PARTE_DEBITO_CD");
		String stNomeCampoIdProcParteDebito = (isFisico ? "ID_PROC_PARTE_DEBITO_FISICO" : "ID_PROC_PARTE_DEBITO");
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ResultSetTJGO rs1 = null;
		String stSql="";
		
		stSql= "SELECT " + stNomeCampoId + ", " + stNomeCampoIdProcParteDebito;
		stSql+= ",NUMERO_LOTE, DATA_GERACAO, TIPO_DOCUMENTO, NUMERO_DOCUMENTO ";
		stSql+= ",NOME_PESSOA, NUMERO_PROCESSO, NATUREZA_PENDENCIA, CATEGORIA_PENDENCIA";
		stSql+= ",DATA_VENCIMENTO_DEBITO, VALOR_DEVIDO, MEIO_ENVIO_COMUNICADO, DATA_ENVIO_COMUNICADO";
		stSql+= ",DATA_CIENCIA_COMUNICADO, TIPO_LOGRADOURO, NOME_LOGRADOURO, NUMERO_ENDERECO, NUMERO_QUADRA_ENDERECO";
		stSql+= ",DESC_COMPLEMENTO_ENDERECO, NOME_BAIRRO, CODIGO_CEP, NOME_MUNICIPIO, NOME_UF, OBSERVACAO_PENDENCIA";
		stSql+= " FROM PROJUDI." + stNomeTabela;
		stSql+= " WHERE NUMERO_LOTE = ? "; ps.adicionarLong(numeroLote);
		stSql+= " ORDER BY " + stNomeCampoId;
		
		try{
			rs1 = consultar(stSql, ps);

			while (rs1.next()) {
				ProcessoParteDebitoCadinDt ProcessoParteDebitoCadin = new ProcessoParteDebitoCadinDt();
				associarDtCompleto(ProcessoParteDebitoCadin, rs1, isFisico, stNomeCampoId, stNomeCampoIdProcParteDebito);
				listaRegistros.add(ProcessoParteDebitoCadin);
			}
			
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }
	}
	
	public List<ProcessoParteDebitoCadinDt> obtenhaRegistrosPelaDataDeGeracao(String dataDeGeracao) throws Exception {
		List<ProcessoParteDebitoCadinDt> listaRegistros = new ArrayList<ProcessoParteDebitoCadinDt>();
		obtenhaRegistrosPelaDataDeGeracao(dataDeGeracao, false, listaRegistros);
		obtenhaRegistrosPelaDataDeGeracao(dataDeGeracao, true, listaRegistros);
		return listaRegistros;
	}
	
	private void obtenhaRegistrosPelaDataDeGeracao(String dataDeGeracao, boolean isFisico, List<ProcessoParteDebitoCadinDt> listaRegistros) throws Exception {
		String stNomeTabela = (isFisico ? "PROC_PARTE_DEBITO_FISICO_CADIN" : "PROC_PARTE_DEBITO_CADIN");
		String stNomeCampoId = (isFisico ? "ID_PROC_PARTE_DEBITO_FISICO_CD" : "ID_PROC_PARTE_DEBITO_CD");
		String stNomeCampoIdProcParteDebito = (isFisico ? "ID_PROC_PARTE_DEBITO_FISICO" : "ID_PROC_PARTE_DEBITO");
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ResultSetTJGO rs1 = null;
		String stSql="";
		TJDataHora dataGeracao = new TJDataHora();
		dataGeracao.setDataddMMaaaa(dataDeGeracao);
		
		stSql= "SELECT " + stNomeCampoId + ", " + stNomeCampoIdProcParteDebito;
		stSql+= ",NUMERO_LOTE, DATA_GERACAO, TIPO_DOCUMENTO, NUMERO_DOCUMENTO ";
		stSql+= ",NOME_PESSOA, NUMERO_PROCESSO, NATUREZA_PENDENCIA, CATEGORIA_PENDENCIA";
		stSql+= ",DATA_VENCIMENTO_DEBITO, VALOR_DEVIDO, MEIO_ENVIO_COMUNICADO, DATA_ENVIO_COMUNICADO";
		stSql+= ",DATA_CIENCIA_COMUNICADO, TIPO_LOGRADOURO, NOME_LOGRADOURO, NUMERO_ENDERECO, NUMERO_QUADRA_ENDERECO";
		stSql+= ",DESC_COMPLEMENTO_ENDERECO, NOME_BAIRRO, CODIGO_CEP, NOME_MUNICIPIO, NOME_UF, OBSERVACAO_PENDENCIA";
		stSql+= " FROM PROJUDI." + stNomeTabela;
		stSql+= " WHERE TO_NUMBER(TO_CHAR(DATA_GERACAO, 'YYYYMMDD')) = ? "; ps.adicionarLong(dataGeracao.getDataHoraFormatadayyyyMMdd());
		stSql+= " ORDER BY " + stNomeCampoId;
		
		try{
			rs1 = consultar(stSql, ps);

			while (rs1.next()) {
				ProcessoParteDebitoCadinDt ProcessoParteDebitoCadin = new ProcessoParteDebitoCadinDt();
				associarDtCompleto(ProcessoParteDebitoCadin, rs1, isFisico, stNomeCampoId, stNomeCampoIdProcParteDebito);
				listaRegistros.add(ProcessoParteDebitoCadin);
			}
			
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }		
	}
	
	public ProcessoParteDebitoCadinDt obtenhaRegistro(String id_ProcessoParteDebito) throws Exception {
		ProcessoParteDebitoCadinDt processoParteDebitoCadin = obtenhaRegistro(id_ProcessoParteDebito, false);
		if (processoParteDebitoCadin == null) processoParteDebitoCadin = obtenhaRegistro(id_ProcessoParteDebito, true);
		return processoParteDebitoCadin;
	}
	
	public ProcessoParteDebitoCadinDt obtenhaRegistro(String id_ProcessoParteDebito, boolean isFisico) throws Exception {
		String stNomeTabela = (isFisico ? "PROC_PARTE_DEBITO_FISICO_CADIN" : "PROC_PARTE_DEBITO_CADIN");
		String stNomeCampoId = (isFisico ? "ID_PROC_PARTE_DEBITO_FISICO_CD" : "ID_PROC_PARTE_DEBITO_CD");
		String stNomeCampoIdProcParteDebito = (isFisico ? "ID_PROC_PARTE_DEBITO_FISICO" : "ID_PROC_PARTE_DEBITO");
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ProcessoParteDebitoCadinDt processoParteDebitoCadin = null;
		ResultSetTJGO rs1 = null;
		String stSql="";
		
		stSql= "SELECT " + stNomeCampoId + ", " + stNomeCampoIdProcParteDebito;
		stSql+= ",NUMERO_LOTE, DATA_GERACAO, TIPO_DOCUMENTO, NUMERO_DOCUMENTO ";
		stSql+= ",NOME_PESSOA, NUMERO_PROCESSO, NATUREZA_PENDENCIA, CATEGORIA_PENDENCIA";
		stSql+= ",DATA_VENCIMENTO_DEBITO, VALOR_DEVIDO, MEIO_ENVIO_COMUNICADO, DATA_ENVIO_COMUNICADO";
		stSql+= ",DATA_CIENCIA_COMUNICADO, TIPO_LOGRADOURO, NOME_LOGRADOURO, NUMERO_ENDERECO, NUMERO_QUADRA_ENDERECO";
		stSql+= ",DESC_COMPLEMENTO_ENDERECO, NOME_BAIRRO, CODIGO_CEP, NOME_MUNICIPIO, NOME_UF, OBSERVACAO_PENDENCIA";
		stSql+= " FROM PROJUDI." + stNomeTabela;
		stSql+= " WHERE " + stNomeCampoIdProcParteDebito + " = ? "; ps.adicionarLong(id_ProcessoParteDebito);
				
		try{
			rs1 = consultar(stSql, ps);

			if (rs1.next()) {
				processoParteDebitoCadin = new ProcessoParteDebitoCadinDt();
				associarDtCompleto(processoParteDebitoCadin, rs1, isFisico, stNomeCampoId, stNomeCampoIdProcParteDebito);				
			}
			
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }
		
		return processoParteDebitoCadin;
	}
	
	public boolean lotePossuiRegistros(String numeroLote) throws Exception {
		boolean possuiLote = lotePossuiRegistros(numeroLote, false);
		if (!possuiLote) possuiLote = lotePossuiRegistros(numeroLote, true);
		return possuiLote;
	}
	
	public boolean lotePossuiRegistros(String numeroLote, boolean isFisico) throws Exception {
		String stNomeTabela = (isFisico ? "PROC_PARTE_DEBITO_FISICO_CADIN" : "PROC_PARTE_DEBITO_CADIN");
		String stNomeCampoId = (isFisico ? "ID_PROC_PARTE_DEBITO_FISICO_CD" : "ID_PROC_PARTE_DEBITO_CD");
		String stNomeCampoIdProcParteDebito = (isFisico ? "ID_PROC_PARTE_DEBITO_FISICO" : "ID_PROC_PARTE_DEBITO");
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ResultSetTJGO rs1 = null;
		String stSql="";
		
		stSql= "SELECT " + stNomeCampoId + ", " + stNomeCampoIdProcParteDebito;
		stSql+= ",NUMERO_LOTE, DATA_GERACAO, TIPO_DOCUMENTO, NUMERO_DOCUMENTO ";
		stSql+= ",NOME_PESSOA, NUMERO_PROCESSO, NATUREZA_PENDENCIA, CATEGORIA_PENDENCIA";
		stSql+= ",DATA_VENCIMENTO_DEBITO, VALOR_DEVIDO, MEIO_ENVIO_COMUNICADO, DATA_ENVIO_COMUNICADO";
		stSql+= ",DATA_CIENCIA_COMUNICADO, TIPO_LOGRADOURO, NOME_LOGRADOURO, NUMERO_ENDERECO, NUMERO_QUADRA_ENDERECO";
		stSql+= ",DESC_COMPLEMENTO_ENDERECO, NOME_BAIRRO, CODIGO_CEP, NOME_MUNICIPIO, NOME_UF, OBSERVACAO_PENDENCIA";
		stSql+= " FROM PROJUDI." + stNomeTabela;
		stSql+= " WHERE NUMERO_LOTE = ? "; ps.adicionarLong(numeroLote);
				
		try{
			rs1 = consultar(stSql, ps);
			return rs1.next();	
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }	
	}
	
	public boolean dataGeracaoPossuiRegistros(String data) throws Exception {
		boolean possuiDataGeracao = dataGeracaoPossuiRegistros(data, false);
		if (!possuiDataGeracao) possuiDataGeracao = dataGeracaoPossuiRegistros(data, true);
		return possuiDataGeracao;
	}
	
	public boolean dataGeracaoPossuiRegistros(String data, boolean isFisico) throws Exception {
		String stNomeTabela = (isFisico ? "PROC_PARTE_DEBITO_FISICO_CADIN" : "PROC_PARTE_DEBITO_CADIN");
		String stNomeCampoId = (isFisico ? "ID_PROC_PARTE_DEBITO_FISICO_CD" : "ID_PROC_PARTE_DEBITO_CD");
		String stNomeCampoIdProcParteDebito = (isFisico ? "ID_PROC_PARTE_DEBITO_FISICO" : "ID_PROC_PARTE_DEBITO");
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ResultSetTJGO rs1 = null;
		String stSql="";
		TJDataHora dataGeracao = new TJDataHora();
		dataGeracao.setDataddMMaaaa(data);
		
		stSql= "SELECT " + stNomeCampoId + ", " + stNomeCampoIdProcParteDebito;
		stSql+= ",NUMERO_LOTE, DATA_GERACAO, TIPO_DOCUMENTO, NUMERO_DOCUMENTO ";
		stSql+= ",NOME_PESSOA, NUMERO_PROCESSO, NATUREZA_PENDENCIA, CATEGORIA_PENDENCIA";
		stSql+= ",DATA_VENCIMENTO_DEBITO, VALOR_DEVIDO, MEIO_ENVIO_COMUNICADO, DATA_ENVIO_COMUNICADO";
		stSql+= ",DATA_CIENCIA_COMUNICADO, TIPO_LOGRADOURO, NOME_LOGRADOURO, NUMERO_ENDERECO, NUMERO_QUADRA_ENDERECO";
		stSql+= ",DESC_COMPLEMENTO_ENDERECO, NOME_BAIRRO, CODIGO_CEP, NOME_MUNICIPIO, NOME_UF, OBSERVACAO_PENDENCIA";
		stSql+= " FROM PROJUDI." + stNomeTabela;
		stSql+= " WHERE TO_NUMBER(TO_CHAR(DATA_GERACAO, 'YYYYMMDD')) = ? "; ps.adicionarLong(dataGeracao.getDataHoraFormatadayyyyMMdd());
				
		try{
			rs1 = consultar(stSql, ps);
			return rs1.next();	
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }	
	}
	
	private void associarDtCompleto(ProcessoParteDebitoCadinDt dados, 
			                        ResultSetTJGO rs1, 
			                        boolean isFisico,
			                        String stNomeCampoId,
			                        String stNomeCampoIdProcParteDebito )  throws Exception {
		
		dados.setId(rs1.getString(stNomeCampoId));
		dados.setId_ProcessoParteDebito(rs1.getString(stNomeCampoIdProcParteDebito));
		dados.setFisico(isFisico);
		dados.setNumeroLote(rs1.getString("NUMERO_LOTE"));
		dados.setDataGeracao(rs1.getString("DATA_GERACAO"));
		dados.setTipoDocumento(rs1.getString("TIPO_DOCUMENTO"));
		dados.setNumeroDocumento(rs1.getString("NUMERO_DOCUMENTO"));
		dados.setNomePessoa(rs1.getString("NOME_PESSOA"));
		dados.setNumeroProcesso(rs1.getString("NUMERO_PROCESSO"));
		dados.setNaturezaPendencia(rs1.getString("NATUREZA_PENDENCIA")); //01-TRIBUTÁRIA 02-NÃO TRIBUTÁRIA
		dados.setCategoriaPendencia(rs1.getString("CATEGORIA_PENDENCIA")); //08 Não tributário, confirmar outros valores com Administrado do Sistema
		dados.setDataVencimentoDebito(rs1.getString("DATA_VENCIMENTO_DEBITO"));
		dados.setValorDevido(Funcoes.FormatarDecimal(rs1.getDouble("VALOR_DEVIDO")));
		dados.setMeioEnvioComunicado(rs1.getString("MEIO_ENVIO_COMUNICADO")); //sendo 01-DTE 02-EDITAL 03-CARTA
		dados.setDataEnvioComunicado(rs1.getString("DATA_ENVIO_COMUNICADO"));
        dados.setDataCienciaComunicado(rs1.getString("DATA_CIENCIA_COMUNICADO"));
		dados.setTipoLogradouro(rs1.getString("TIPO_LOGRADOURO"));
        dados.setNomeLogradouro(rs1.getString("NOME_LOGRADOURO"));	
        dados.setNumeroEndereco(rs1.getString("NUMERO_ENDERECO"));        
        dados.setNumeroQuadraEndereco(rs1.getString("NUMERO_QUADRA_ENDERECO"));
        dados.setDescricaoComplementoEndereco(rs1.getString("DESC_COMPLEMENTO_ENDERECO"));
        dados.setNomeBairro(rs1.getString("NOME_BAIRRO"));
        dados.setCodigoCep(rs1.getString("CODIGO_CEP"));
        dados.setMunicipioEndereco(rs1.getString("NOME_MUNICIPIO"));
        dados.setUFEndereco(rs1.getString("NOME_UF"));
        dados.setObservacaoPendencia(rs1.getString("OBSERVACAO_PENDENCIA"));		
	}
	
	/**
 	 * Método para verificar se id guia está presente na tabela de envio ao CADIN.
 	 * 
 	 * @param String idGuiaEmissao
 	 * @return boolean
 	 * @throws Exception
 	 */
 	public boolean isGuiaEnviadaCadin(String idGuiaEmissao) throws Exception {
 		boolean retorno = false;
 		
 		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
 		ResultSetTJGO rs = null;
 		
 		String sql = "SELECT DEBITOCADIN.ID_PROC_PARTE_DEBITO_CD AS ID_PROC_PARTE_DEBITO_CD FROM PROJUDI.PROC_PARTE_DEBITO_CADIN DEBITOCADIN "+
 					" INNER JOIN PROJUDI.PROC_PARTE_DEBITO DEBITO ON (DEBITOCADIN.ID_PROC_PARTE_DEBITO = DEBITO.ID_PROC_PARTE_DEBITO) "+
 					" WHERE DEBITO.ID_GUIA_EMIS = ?";
 		ps.adicionarLong(idGuiaEmissao);
 		
		try {
			if (idGuiaEmissao != null) {
				rs = this.consultar(sql, ps);
				if (rs != null) {
					while (rs.next()) {
						if( rs.getString("ID_PROC_PARTE_DEBITO_CD") != null ) {
							retorno = true;
							break;
						}
					}
				}
			}
			
		}
		finally{
			try{if (rs != null) rs.close();} catch(Exception e) {}
		}
		
		return retorno;
 	}
 	
 	/**
 	 * Método para verificar se id processo está presente na tabela de envio ao CADIN.
 	 * 
 	 * @param String idGuiaEmissao
 	 * @return boolean
 	 * @throws Exception
 	 */
 	public boolean isProcessoPossuiGuiaEnviadaCadin(String idProcesso) throws Exception {
 		boolean retorno = false;
 		
 		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
 		ResultSetTJGO rs = null;
 		
 		String sql = "SELECT DEBITOCADIN.ID_PROC_PARTE_DEBITO_CD AS ID_PROC_PARTE_DEBITO_CD FROM PROJUDI.PROC_PARTE_DEBITO_CADIN DEBITOCADIN " +
 					 " INNER JOIN PROJUDI.PROC_PARTE_DEBITO DEBITO ON (DEBITOCADIN.ID_PROC_PARTE_DEBITO = DEBITO.ID_PROC_PARTE_DEBITO) " +
 					 " INNER JOIN PROJUDI.PROC_PARTE PARTE ON (PARTE.ID_PROC_PARTE = DEBITO.ID_PROC_PARTE) " +
 					 " WHERE PARTE.ID_PROC = ?";
 		ps.adicionarLong(idProcesso);
 		
		try {
			if (idProcesso != null) {
				rs = this.consultar(sql, ps);
				if (rs != null) {
					while (rs.next()) {
						if( rs.getString("ID_PROC_PARTE_DEBITO_CD") != null ) {
							retorno = true;
							break;
						}
					}
				}
			}
			
		}
		finally{
			try{if (rs != null) rs.close();} catch(Exception e) {}
		}
		
		return retorno;
 	}
 	
 	/**
 	 * Método para verificar se id guia está presente na tabela de envio ao CADIN.
 	 * 
 	 * @param String idGuiaEmissao
 	 * @return boolean
 	 * @throws Exception
 	 */
 	public boolean isGuiaSPGSSGEnviadaCadin(String numeroGuia) throws Exception {
 		boolean retorno = false;
 		
 		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
 		ResultSetTJGO rs = null;
 		
 		String sql = "SELECT DEBITOCADIN.ID_PROC_PARTE_DEBITO_FISICO_CD AS ID_PROC_PARTE_DEBITO_FISICO_CD FROM PROJUDI.PROC_PARTE_DEBITO_FISICO_CADIN DEBITOCADIN "+
 					" INNER JOIN PROJUDI.PROC_PARTE_DEBITO_FISICO DEBITO ON (DEBITOCADIN.ID_PROC_PARTE_DEBITO_FISICO = DEBITO.ID_PROC_PARTE_DEBITO_FISICO) "+
 					" WHERE DEBITO.NUMERO_GUIA_COMPLETO = ?";
 		ps.adicionarLong(numeroGuia);
 		
		try {
			if (numeroGuia != null) {
				rs = this.consultar(sql, ps);
				if (rs != null) {
					while (rs.next()) {
						if( rs.getString("ID_PROC_PARTE_DEBITO_FISICO_CD") != null ) {
							retorno = true;
							break;
						}
					}
				}
			}
			
		}
		finally{
			try{if (rs != null) rs.close();} catch(Exception e) {}
		}
		
		return retorno;
 	}
 	
 	public long obtenhaNumeroDoProximoLote() throws Exception {
 		long numeroDoUltimoLotePJD = obtenhaNumeroDoUltimoLote(false);
 		long numeroDoUltimoLoteFisico = obtenhaNumeroDoUltimoLote(true);
 		if (numeroDoUltimoLoteFisico > numeroDoUltimoLotePJD) {
 			numeroDoUltimoLotePJD = numeroDoUltimoLoteFisico;
 		}
		return numeroDoUltimoLotePJD + 1;
	}
	
	private long obtenhaNumeroDoUltimoLote(boolean isFisico) throws Exception {
		String stNomeTabela = (isFisico ? "PROC_PARTE_DEBITO_FISICO_CADIN" : "PROC_PARTE_DEBITO_CADIN");
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ResultSetTJGO rs1 = null;
		String stSql="";
		
		stSql= "SELECT MAX(NUMERO_LOTE) AS ULTIMO_NUMERO_LOTE ";
		stSql+= " FROM PROJUDI." + stNomeTabela;
				
		try{
			rs1 = consultar(stSql, ps);

			while (rs1.next()) {
				return rs1.getLong("ULTIMO_NUMERO_LOTE");
			}
			
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }
		
		return 0;
	}
} 
