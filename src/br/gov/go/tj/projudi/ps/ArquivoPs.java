package br.gov.go.tj.projudi.ps;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.MovimentacaoArquivoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoTipoDt;
import br.gov.go.tj.projudi.dt.NumeroProcessoDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class ArquivoPs extends ArquivoPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = -8493054550166757525L;
    private static AmazonS3 s3Client= null;
    
    public ArquivoPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Sobrecarga do metodo inserir devido aos problemas de caracteres
	 * Se arquivo tem um caminho, trata de arquivo que será gravado em disco. Senão, será blob	
	 *  - 16/09/2008 17:00
	 *  Modificacao para aceitar o tipo convencional de insercao de arquivos seguindo o padrao do projudi 
	 * @author msapaula
	 * @author Ronneesley Moura Teles
	 * @since 16/09/2008 17:00 
	*/
	public void inserir(ArquivoDt dados) throws Exception {
	    PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String inc = "?";

		if (dados.getArquivoTipoCodigo() != null && dados.getArquivoTipoCodigo().length() > 0) inc = "(SELECT ID_ARQ_TIPO FROM ARQ_TIPO WHERE ARQ_TIPO_CODIGO = ?)";

		String sql = "INSERT INTO ARQ (NOME_ARQ, ID_ARQ_TIPO, CONTENT_TYPE, CAMINHO, ARQ, DATA_INSERCAO, USU_ASSINADOR, RECIBO, CODIGO_TEMP)" + " VALUES(?, " + inc + ", ?, ?, ?, SYSDATE, ?, ?, ?)";

		if (dados.getNomeArquivo().length() == 0) {
			ps.adicionarStringNull();
		} else { 
			ps.adicionarString(Funcoes.limparNomeArquivo(dados.getNomeArquivo()));
		}
		
		if (dados.getArquivoTipoCodigo() != null && dados.getArquivoTipoCodigo().length() > 0) {
			ps.adicionarString(dados.getArquivoTipoCodigo());
		} else {
			if (dados.getId_ArquivoTipo().length() == 0) ps.adicionarStringNull();
			else ps.adicionarString(dados.getId_ArquivoTipo());
		}

		if (dados.getContentType().length() == 0) ps.adicionarStringNull();
		else ps.adicionarString(dados.getContentType());
				
		//é para salvar no cepf
		if (dados.isSalvarCeph()) {
			//salvo o arquivo para o CEPH			
			ps.adicionarString(dados.getCaminhoCeph());
			//null no blob do banco
			ps.adicionarByteNull();
			//para a geração dos pdf
			dados.setCodigoTemp(String.valueOf(dados.conteudoBytes().length));
		}else {
			//caminho null
			ps.adicionarStringNull();
			ps.adicionarByte(dados.conteudoBytes());
		}

		if (dados.getUsuarioAssinador().length() == 0) ps.adicionarStringNull();
		else ps.adicionarString(dados.getUsuarioAssinador());

		if (dados.getRecibo() != null && dados.getRecibo().length() != 0 && dados.getRecibo().equalsIgnoreCase("true")) ps.adicionarBoolean(true);
		else ps.adicionarBoolean(false);
		
		ps.adicionarLong(dados.getCodigoTemp());
		
		dados.setId(executarInsert(sql, "ID_ARQ", ps));
		
		//é para salvar no cepf
		if (dados.isSalvarCeph()) {
			//salvo o arquivo para o CEPH
			salvarArquivoCeph(dados);
		}
				
	}

	/**
	 * Sobrecarga do método alterar. Seta DATA_INSERCAO como atual
	 * - 19/09/2008 13:29
	 * Modificacao do metodo para adequacao aos bytes em preparedstatement
	 * @author Ronneesley Moura Teles
	 * @author msapaula
	 */
	public void alterar(ArquivoDt dados) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "UPDATE ARQ set ";

		String sql1 = "";
	
		if (dados.getNomeArquivo().length() > 0) {
			sql1 += " NOME_ARQ = ? ";
			ps.adicionarString(dados.getNomeArquivo());				
		}
		if (dados.getId_ArquivoTipo().length() > 0) {
			sql1 += (sql1.length() > 0) ? " , ID_ARQ_TIPO = ? " : " ID_ARQ_TIPO = ? ";
			ps.adicionarLong(dados.getId_ArquivoTipo());
		}
		if (dados.getContentType().length() > 0) {
			sql1 += (sql1.length() > 0) ? " , CONTENT_TYPE = ? " : " CONTENT_TYPE = ?";
			ps.adicionarString(dados.getContentType());
		}
		if (dados.getCaminho().length() > 0) {
			sql1 += (sql1.length() > 0) ? " , CAMINHO = ? " : " CAMINHO = ? ";
			ps.adicionarString(dados.getCaminho());
		}
		if (dados.getUsuarioAssinador().length() > 0) {
			sql1 += (sql1.length() > 0) ? " , USU_ASSINADOR = ? " : " USU_ASSINADOR = ? ";
			ps.adicionarString(dados.getUsuarioAssinador());
		}

		if (dados.getDataInsercao().length() > 0) {
			sql1 += (sql1.length() > 0) ? " , DATA_INSERCAO =  ? " : " DATA_INSERCAO = ? ";
			ps.adicionarDateTime(dados.getDataInsercao()); 
		}

		if (dados.getRecibo().length() > 0) {
			sql1 += (sql1.length() > 0) ? " , RECIBO = ? " : " RECIBO = ? ";
			ps.adicionarBoolean(dados.getRecibo());				
		}

		if (dados.getCodigoTemp().length() > 0) {
			sql1 += (sql1.length() > 0) ? " , CODIGO_TEMP =  ? "  : " CODIGO_TEMP =  ? ";
			ps.adicionarLong(dados.getCodigoTemp());
		}
		//é para salvar no cepf
		if (dados.isSalvarCeph()) {
			//salvo o arquivo para o CEPH
			salvarArquivoCeph(dados);
			//para a geração dos pdf
			dados.setCodigoTemp(String.valueOf(dados.conteudoBytes().length));
		}else {
			sql1 += (sql1.length() > 0) ? " , ARQ = ? " : " ARQ = ? ";
			if (dados.getArquivo().length() > 0) {				
				ps.adicionarByte(dados.conteudoBytes());
			} else {
				ps.adicionarByteNull();
			}			
		}
		
		sql += sql1 + "  WHERE ID_ARQ = ? ";
		ps.adicionarLong(dados.getId());
		
		executarUpdateDelete(sql, ps);
			
	}

	/**
	 * Sobrecarga da associacao devido aos problemas com bytes para string
	 * @author Ronneesley Moura Teles
	 * @since 19/09/2008 12:02
	 * @return void
	 * @throws Exception 
	 */
	protected void associarDt(ArquivoDt Dados, ResultSetTJGO rs1) throws Exception{
		
		Dados.setId(rs1.getString("ID_ARQ"));
		Dados.setNomeArquivo(rs1.getString("NOME_ARQ"));
		Dados.setId_ArquivoTipo(rs1.getString("ID_ARQ_TIPO"));
		Dados.setArquivoTipo(rs1.getString("ARQ_TIPO"));
		Dados.setContentType(rs1.getString("CONTENT_TYPE"));
		Dados.setRecibo(Funcoes.FormatarLogico(rs1.getString("RECIBO")));
		Dados.setBloqueado(rs1.getString("BLOQUEADO"));

		Dados.setArquivo(rs1.getBytes("ARQ"));
		Dados.setCaminho(rs1.getString("CAMINHO"));
		Dados.setDataInsercao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));
		Dados.setUsuarioAssinador(rs1.getString("USU_ASSINADOR"));
		Dados.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
		Dados.setArquivoTipoCodigo(rs1.getString("ARQ_TIPO_CODIGO"));
		
		//tratamento para arquivos do ceph
		if (Dados.isArquivoFisico() && 
			!Dados.isMidiaDigitalUpload() &&
			Dados.isArquivoObjectStorageProjudi()) {					
			Dados.setArquivo(getConteudoArquivoCeph(Dados.getCaminho()));
		}
		
	}

//	/**
//	 * Atualiza conteúdo de um ARQ, já compactando o conteúdo desse
//	 * @since 23/01/2009 16:05
//	 * 
//	 * @author msapaula
//	 */
//	public void atualizaConteudoArquivo(ArquivoDt dados) throws Exception {
//		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
//		
//		String sql = "UPDATE ARQ set ARQ = ?, RECIBO = ?, CAMINHO = ?, CODIGO_TEMP = ? WHERE ID_ARQ = ?";
//		
//		//é para salvar no cepf
//		if (dados.isSalvarCeph()) {
//			//salvo o arquivo para o CEPH
//			salvarArquivoCeph(dados);
//			ps.adicionarByteNull();			
//			//para a geração dos pdf
//			dados.setCodigoTemp(String.valueOf(dados.conteudoBytes().length));
//			dados.setCaminho(dados.getCaminhoCeph());
//		}else {			
//			if (dados.getArquivo().length() > 0) {				
//				ps.adicionarByte(dados.conteudoBytes());
//			} else {
//				ps.adicionarByteNull();
//			}			
//		}
//		
//		if (dados.getRecibo() != null && dados.getRecibo().length() != 0 && dados.getRecibo().equalsIgnoreCase("true")) ps.adicionarBoolean(true);
//		else ps.adicionarBoolean(false);
//		
//		if (dados.getCaminho() == null || dados.getCaminho().length() == 0) ps.adicionarStringNull();
//		else ps.adicionarString(dados.getCaminho());
//		
//		ps.adicionarLong(dados.getCodigoTemp());
//		
//		ps.adicionarLong(dados.getId());
//		
//		executarUpdateDelete(sql, ps);
//	}

	public ArquivoDt consultarId(String id_arquivo) throws Exception {
		String Sql;
		ResultSetTJGO rs1 = null;
		ArquivoDt Dados = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		Sql = "SELECT * FROM PROJUDI.VIEW_ARQ WHERE ID_ARQ = ?";
		ps.adicionarLong(id_arquivo);
		try{
			 rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				Dados = new ArquivoDt();
				associarDt(Dados, rs1);
			}								
			
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return Dados;
	}
	/*
	 * jrcorrea
	 * 08/09/2014
	 * 
	 * 
	 */
	
	public ArquivoDt consultarIdAtivo(String ID_ARQ, boolean descompactar) throws Exception {
		String Sql;
		ResultSetTJGO rs1 = null;
		ArquivoDt Dados = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();                                         
		
		//foi usada a MOVI_ARQ para os arquivos que não estão ligados a movimentação. 
		//Exemplo: Publicações de certidões
		Sql = "SELECT A.* FROM PROJUDI.VIEW_ARQ A "
				+ "WHERE A.ID_ARQ = ?"; 	ps.adicionarLong(ID_ARQ);
		Sql += " 	AND A.bloqueado <> ?"; 	ps.adicionarLong(ArquivoDt.ARQUIVO_BLOQUEADO);
		Sql += "	AND NOT EXISTS ( SELECT 1 FROM PROJUDI.MOVI_ARQ MA WHERE MA.ID_ARQ = A.ID_ARQ AND MA.ID_MOVI_ARQ_ACESSO IN (?,?,?))";
		
		ps.adicionarLong(MovimentacaoArquivoDt.ACESSO_BLOQUEADO);
		ps.adicionarLong(MovimentacaoArquivoDt.ACESSO_BLOQUEADO_ERRO_MIGRACAO);
		ps.adicionarLong(MovimentacaoArquivoDt.ACESSO_BLOQUEADO_VIRUS);
		
		try{
			 rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				Dados = new ArquivoDt();
				associarDt(Dados, rs1);
			}
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return Dados;
	}
	
	/**
	 * Consulta os arquivos das pendências finais do tipo Publicacao, filtrado por período
	 * @param dataInicio
	 * @param dataFim
	 * @return
	 * @throws Exception
	 */
	public List<ArquivoDt> consultarArquivosPendenciaData(String dataInicio, String dataFinal, int opcaoPublicacao) throws Exception {
		List<ArquivoDt> arquivos = new ArrayList<ArquivoDt>();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT a.ID_ARQ, a.RECIBO, a.NOME_ARQ, a.USU_ASSINADOR, a.ARQ, a.DATA_INSERCAO, length(a.ARQ) as TAMANHO");
		sql.append(" FROM projudi.ARQ a");
		sql.append(" JOIN projudi.PEND_FINAL_ARQ pa ON pa.ID_ARQ = a.ID_ARQ");
		sql.append(" JOIN projudi.PEND_FINAL p ON p.id_pend = pa.id_pend");
		sql.append(" JOIN projudi.PEND_TIPO pt ON pt.id_pend_tipo = p.id_pend_tipo AND pt.pend_tipo_codigo = ?");			
		ps.adicionarLong(PendenciaTipoDt.PUBLICACAO_PUBLICA);
		
		if (opcaoPublicacao > 0){
			
			sql.append(" JOIN projudi.PEND_FINAL_RESP pfr ON pfr.id_pend = p.id_pend");
			sql.append(" JOIN projudi.SERV s ON pfr.id_serv = s.id_serv");
			sql.append(" JOIN projudi.SERV_SUBTIPO ss ON s.id_serv_subtipo = ss.id_serv_subtipo");
			
			// Publicacao de 1o ou 2o Grau?			
			if (opcaoPublicacao == 1){
				sql.append(" AND ss.serv_subtipo_codigo IN (?,?,?,?,?,?)");
			} else {
				sql.append(" AND ss.serv_subtipo_codigo NOT IN (?,?,?,?,?,?)");
			}
			ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CIVEL);
			ps.adicionarLong(ServentiaSubtipoDt.CONSELHO_SUPERIOR_MAGISTRATURA);
			ps.adicionarLong(ServentiaSubtipoDt.SECAO_CRIMINAL);
			ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CRIMINAL);
			ps.adicionarLong(ServentiaSubtipoDt.CORTE_ESPECIAL); 
			ps.adicionarLong(ServentiaSubtipoDt.SECAO_CIVEL);
						
			// Publicação de 1o grau: capital ou interior?
			if (opcaoPublicacao == 2){
				sql.append(" JOIN projudi.COMARCA c ON c.id_comarca = s.id_comarca");		
				sql.append(" AND c.comarca_codigo IN (?)"); ps.adicionarLong(ComarcaDt.GOIANIA);
				
			} else if (opcaoPublicacao == 3){
				sql.append(" JOIN projudi.COMARCA c ON c.id_comarca = s.id_comarca");		
				sql.append(" AND c.comarca_codigo NOT IN (?)"); ps.adicionarLong(ComarcaDt.GOIANIA);
			}
		}
				
		sql.append(" WHERE p.DATA_INICIO BETWEEN ? AND ?");
		ps.adicionarDateTime(dataInicio); ps.adicionarDateTime(dataFinal);
		
		sql.append(" ORDER BY p.DATA_INICIO ASC");
		
		try {
			rs1 = consultar(sql.toString(), ps);
			while (rs1.next()) {
				ArquivoDt arquivoDt = new ArquivoDt();
				arquivoDt.setId(rs1.getString("ID_ARQ"));
				arquivoDt.setNomeArquivo(rs1.getString("NOME_ARQ"));
				arquivoDt.setRecibo(Funcoes.FormatarLogico(rs1.getString("RECIBO")));
				arquivoDt.setUsuarioAssinador(rs1.getString("USU_ASSINADOR"));
				arquivoDt.setDataInsercao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));
				arquivoDt.setArquivo(rs1.getBytes("ARQ"));

				if (rs1.getString("USU_ASSINADOR")!=null && !rs1.getString("USU_ASSINADOR").isEmpty()) {
					arquivoDt.setAssinado(true);
				} else {
					arquivoDt.setAssinado(false);
				}
				
				arquivos.add(arquivoDt);
			}		
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		return arquivos;
	}
		
	
	/*
	 * jrcorrea
	 * 08/09/2014
	 * 
	 * 
	 */
	
	public ArquivoDt consultarIdMandadoPrisaoAtivo(String ID_ARQ, boolean descompactar) throws Exception {
		String Sql;
		ResultSetTJGO rs1 = null;
		ArquivoDt Dados = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();                                         // 0 Ativo ;  2 download bloqueado
		Sql = "SELECT A.* FROM PROJUDI.VIEW_ARQ A " ; 
		Sql += " INNER JOIN MANDADO_PRISAO_ARQUIVO MPA ON MPA.ID_ARQUIVO = A.ID_ARQ";
		Sql += " INNER JOIN MANDADO_PRISAO MP ON MP.ID_MANDADO_PRISAO = MPA.ID_MANDADO_PRISAO";
		Sql += " INNER JOIN MANDADO_PRISAO_STATUS MPS ON MPS.ID_MANDADO_PRISAO_STATUS = MP.ID_MANDADO_PRISAO_STATUS";
																										// 6 revogado		
		Sql += " WHERE A.ID_ARQ = ? AND MPS.MANDADO_PRISAO_STATUS_CODIGO <> ?"; ps.adicionarLong(ID_ARQ); ps.adicionarLong(6);
		try{
			 rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				Dados = new ArquivoDt();
				associarDt(Dados, rs1);
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return Dados;
	}

	public byte[] consultarConteudoArquivo(ArquivoDt arquivo) throws Exception {
		String Sql;
		ResultSetTJGO rs1 = null;
		byte[] Dados = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		Sql = "SELECT * FROM PROJUDI.VIEW_ARQ WHERE ID_ARQ = ?";
		ps.adicionarLong(arquivo.getId());
		try{
			 rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				//tratamento para arquivos do ceph
				if (arquivo.isArquivoFisico() && 
					!arquivo.isMidiaDigitalUpload() &&
					arquivo.isArquivoObjectStorageProjudi()) {
					Dados = getConteudoArquivoCeph(arquivo.getCaminho());
				}else {
					Dados = rs1.getBytes("ARQ");
				}

			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return Dados;
	}

//	public ArquivoDt consultarArquivo PendenciaId(String ID_ARQ) throws Exception {
//
//		String Sql;
//		ArquivoDt Dados = null;
//		////System.out.println("....ps-consultarArquivoPendenciaId)");
//		ResultSetTJGO rs1 = null;
//		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
//		Sql = "SELECT ID_ARQ, NOME_ARQ, RECIBO, a.USU_ASSINADOR, a.ID_ARQ_TIPO, ARQ_TIPO " + " FROM  PROJUDI.ARQ a JOIN  PROJUDI.ARQ_TIPO at " + " on a.ID_ARQ_TIPO = at.ID_ARQ_TIPO " + " WHERE a.ID_ARQ = ?";
//		ps.adicionarLong(ID_ARQ);
//
//		////System.out.println("....Sql  " + Sql);
//
//		try{
//			////System.out.println("..ps-ConsultaId_Arquivo  " + Sql);
//			 rs1 = consultar(Sql, ps);
//			if (rs1.next()) {
//				Dados = new ArquivoDt();
//				Dados.setId(rs1.getString("ID_ARQ"));
//				Dados.setNomeArquivo(rs1.getString("NOME_ARQ"));
//				Dados.setRecibo(Funcoes.FormatarLogico(rs1.getString("RECIBO")));
//				Dados.setId_ArquivoTipo(rs1.getString("ID_ARQ_TIPO"));
//				Dados.setArquivoTipo(rs1.getString("ARQ_TIPO"));
//				
//				
//				if (rs1.getString("USU_ASSINADOR")!=null && !rs1.getString("USU_ASSINADOR").isEmpty()) {
//					Dados.setAssinado(true);
//				} else {
//					Dados.setAssinado(false);
//				}
//				//tratamento para arquivos do ceph
//				if (Dados.isArquivoFisico() && 
//					!Dados.isMidiaDigitalUpload() &&
//					Dados.isArquivoObjectStorageProjudi()) {
//					Dados.setArquivo(getConteudoArquivoCeph(Dados.getCaminho()));
//				}
//				
//			}
//			//rs1.close();
//			////System.out.println("..ps-consultarArquivoPendenciaId");
//		} finally{
//             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
//        }  
//		return Dados;
//	}

	/**
	 * Consultar arquivos de uma determinada movimentação
	 * 
	 * @param id_Movimentacao, identificação da movimentação
	 * 
	 * @author msapaula
	 */
	public List consultarArquivosMovimentacao(String id_Movimentacao) throws Exception {
		List arquivos = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String Sql = " SELECT ID_ARQ, NOME_ARQ FROM PROJUDI.VIEW_MOVI_ARQ m";
		Sql += " WHERE m.ID_MOVI = ?";
		ps.adicionarLong(id_Movimentacao);
		try{
			rs1 = consultar(Sql, ps);

			while (rs1.next()) {
				if (arquivos == null) arquivos = new ArrayList();

				ArquivoDt arquivoDt = new ArquivoDt();
				arquivoDt.setId(rs1.getString("ID_ARQ"));
				arquivoDt.setNomeArquivo(rs1.getString("NOME_ARQ"));
				arquivos.add(arquivoDt);
			}
			rs1.close();

		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return arquivos;
	}

	/**
	 * Retorna o id do Último ARQ do tipo relatório inserido no processo
	 * @author Leandro Bernardes
	 * @param String idProcesso
	 * @param String idUsuarioServentia
	 * @return String id do ARQ
	 * @throws Exception
	 */
	public String consultarRelatorioProcesso(String idProcesso, String id_UsuarioServentia) throws Exception {
		ResultSetTJGO rs1 = null;
		String id_MovimentacaoAquivoRelatorio = null; 
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
			
		String Sql =  " SELECT MAX(a.ID_ARQ) as ID_ARQ, MAX(ma.ID_MOVI_ARQ) as ID_MOVI_ARQ ";
		Sql += " FROM PROJUDI.MOVI_ARQ ma " +
				" JOIN PROJUDI.MOVI m on ma.ID_MOVI = m.ID_MOVI " +
				" JOIN PROJUDI.PROC p on p.ID_PROC = m.ID_PROC " +
				" JOIN PROJUDI.ARQ a on a.ID_ARQ = ma.ID_ARQ " +
				"JOIN PROJUDI.ARQ_TIPO at on a.ID_ARQ_TIPO = at.ID_ARQ_TIPO ";
		Sql += " WHERE p.ID_PROC = ? AND at.ARQ_TIPO_CODIGO = ? AND m.ID_USU_REALIZADOR = ?" 
					+ " AND m.CODIGO_TEMP = ? AND ma.CODIGO_TEMP = ? ";
		
		ps.adicionarLong(idProcesso);
		ps.adicionarLong(ArquivoTipoDt.RELATORIO);
		ps.adicionarLong(id_UsuarioServentia);
		ps.adicionarLong(0);
		ps.adicionarLong(0);
		
		try{
			rs1 = consultar(Sql, ps);

			while (rs1.next()) {
				id_MovimentacaoAquivoRelatorio = (rs1.getString("ID_MOVI_ARQ"));
			}

		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return id_MovimentacaoAquivoRelatorio;
	}
	
	public ArquivoDt consultarArquivo(String idMandadoPrisao, String arquivoTipoCodigo) throws Exception {
		String Sql;
		ResultSetTJGO rs1 = null;
		ArquivoDt Dados = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
			Sql = "select a.*, at.* from arq a " +
					" inner join mandado_prisao_arquivo mpa on a.id_arq = mpa.id_arquivo" +
					" inner join arq_tipo at on a.id_arq_tipo = at.id_arq_tipo" +
					" where mpa.id_mandado_prisao = ?" +
					" and arq_tipo_codigo = ?";
		ps.adicionarLong(idMandadoPrisao);
		ps.adicionarLong(arquivoTipoCodigo);
		
		try{
			 rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				Dados = new ArquivoDt();
				associarDt(Dados, rs1);
			}
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return Dados;
	}
	
	/**
	 * Consulta os arquivos da movimentação, cujo acesso é NORMAL e PUBLICO e o tipo do arquivo deve ser DJe = 1
	 * @param id_movimentacao
	 * @return Lista de arquivos
	 * @throws Exception
	 */
	public List<ArquivoDt> consultaArquivosComAcessoNormalPublicoParaDJE(String id_movimentacao) throws Exception {
		List<ArquivoDt> lista = new ArrayList<>();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT a.id_arq, a.nome_arq, a.content_type, a.arq, a.usu_assinador, a.recibo, ta.dje");
		sql.append(" FROM projudi.movi_arq ma");
		sql.append(" JOIN projudi.arq a ON a.id_arq = ma.id_arq");
		sql.append(" JOIN projudi.arq_tipo ta on a.id_arq_tipo = ta.id_arq_tipo AND ta.dje = 1");
		sql.append(" WHERE ma.id_movi_arq_acesso IN (" + MovimentacaoArquivoDt.ACESSO_NORMAL + "," + MovimentacaoArquivoDt.ACESSO_PUBLICO + ")");
		sql.append(" AND ma.id_movi = ?");		
		ps.adicionarLong(id_movimentacao);		
		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				ArquivoDt arquivoDt = new ArquivoDt();
				arquivoDt.setId(rs.getString("ID_ARQ"));
				arquivoDt.setNomeArquivo(rs.getString("NOME_ARQ"));
				arquivoDt.setUsuarioAssinador(rs.getString("USU_ASSINADOR"));
				arquivoDt.setRecibo(Funcoes.FormatarLogico(rs.getString("RECIBO")));
				arquivoDt.setContentType(rs.getString("CONTENT_TYPE"));
				arquivoDt.setArquivo(rs.getBytes("ARQ"));
				lista.add(arquivoDt);
			}
		
       } finally{
            try{if (rs != null) rs.close();} catch(Exception e1) {}
       }  
		return lista;
	}
	
	/**
	 * Verifica se um determinado arquivo é do tipo Publico e se a movimentação
	 * @param id_arquivo
	 * @return
	 * @throws Exception
	 * 60196325
	 */
	public boolean isArquivoPublicoEAcessoNormalPublico(String id_movi_arq) throws Exception {
		boolean isPublico = false;
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT a.id_arq");
		sql.append(" FROM projudi.movi_arq ma");
		sql.append(" JOIN projudi.arq a ON a.id_arq = ma.id_arq AND ma.id_movi_arq_acesso IN (" + MovimentacaoArquivoDt.ACESSO_NORMAL + "," + MovimentacaoArquivoDt.ACESSO_PUBLICO + ")");
		sql.append(" JOIN projudi.arq_tipo ta on a.id_arq_tipo = ta.id_arq_tipo AND ta.publico = 1");
		sql.append(" WHERE ma.id_movi_arq = ?");
		ps.adicionarLong(id_movi_arq);
		try {
			rs = consultar(sql.toString(), ps);
			isPublico = rs.next();				
		} finally {
	        try{if (rs != null) rs.close();} catch(Exception e1) {}
		}
		return isPublico;
	}
	
	/**
	 * Grava a data que o arquivo foi indexado no Elasticsearch, para a consulta pública.
	 * @param id identificador do arquivo, em ARQ
	 * @return
	 * @throws Exception
	 */
	public int alterarDataIndexacao (String id) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder();
		sql.append(" UPDATE projudi.arq set data_indexacao = sysdate where id_arq = ?");
		ps.adicionarLong(id);
		return executarUpdateDelete(sql.toString(), ps);
	}
	
	public String ObtenhaNumeroCompletoDoProcessoMovimentacaoJuntadaDeDocumento(String numeroCompletoDoProcesso) throws Exception {
		ResultSetTJGO rs1 = null;
		String Dados = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder();
		
		NumeroProcessoDt numeroCompletoDt = new NumeroProcessoDt(numeroCompletoDoProcesso);
		
		sql.append(" SELECT A.CAMINHO");
		sql.append(" FROM PROJUDI.MOVI M INNER JOIN MOVI_TIPO MT ON MT.ID_MOVI_TIPO = M.ID_MOVI_TIPO");
		sql.append(" INNER JOIN PROJUDI.MOVI_ARQ MA ON MA.ID_MOVI = M.ID_MOVI");
		sql.append(" INNER JOIN PROJUDI.ARQ A ON A.ID_ARQ = MA.ID_ARQ");
		sql.append(" INNER JOIN PROJUDI.PROC P ON P.ID_PROC = M.ID_PROC");
		sql.append(" WHERE P.PROC_NUMERO = ?"); ps.adicionarLong(numeroCompletoDt.getNumero());
		sql.append(" AND P.DIGITO_VERIFICADOR = ?"); ps.adicionarLong(numeroCompletoDt.getDigito());
		sql.append(" AND P.ANO = ?"); ps.adicionarLong(numeroCompletoDt.getAno());
		sql.append(" AND P.FORUM_CODIGO = ?"); ps.adicionarLong(numeroCompletoDt.getForum());
		sql.append(" AND MT.MOVI_TIPO_CODIGO = ?"); ps.adicionarLong(MovimentacaoTipoDt.JUNTADA_DE_DOCUMENTO);
		sql.append(" AND MA.CODIGO_TEMP = ?"); ps.adicionarLong(MovimentacaoArquivoDt.OBJECT_STORAGE);
		sql.append(" AND ROWNUM = 1");		
		
		try{
			 rs1 = consultar(sql.toString(), ps);
			if (rs1.next()) {
				String caminhoCompleto = rs1.getString("CAMINHO");
				if (caminhoCompleto != null && caminhoCompleto.trim().length() > 0 && caminhoCompleto.contains("/")) {
					String[] vetorCaminhoCompleto = caminhoCompleto.split("/");
					if (vetorCaminhoCompleto != null &&
						vetorCaminhoCompleto.length > 0 &&
						Funcoes.validaProcessoNumero(Funcoes.formataNumeroCompletoProcesso(vetorCaminhoCompleto[0]))) {
						Dados = vetorCaminhoCompleto[0];
					}
				}
			}		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return Dados;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception  
	 */
	public ArquivoDt consultarArquivoEmentaPorIdRelatorioVoto(String id) throws Exception {
		ArquivoDt arquivoDt = null;
		ResultSetTJGO rs = null;		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder();
		sql.append(" select a.id_arq, a.nome_arq, a.content_type, a.usu_assinador, a.recibo, a.arq");
		sql.append(" from (");
		sql.append("  select distinct(mae.id_movi) as id_movi"); 
		sql.append("  from projudi.movi_arq mae"); 
		sql.append("  inner join projudi.arq ae on ae.id_arq = mae.id_arq and ae.arq is not null and ae.recibo = 1 and ae.content_type in ('text/plain','text/html','application/pdf','document/pdf','adobe/pdf')");
		sql.append("  inner join projudi.arq_tipo ate on ate.id_arq_tipo = ae.id_arq_tipo and ate.arq_tipo_codigo = " + ArquivoTipoDt.RELATORIO_VOTO + " and ate.publico = 1");
		sql.append("  where mae.id_movi_arq_acesso in (" + MovimentacaoArquivoDt.ACESSO_NORMAL + "," + MovimentacaoArquivoDt.ACESSO_PUBLICO + ")");
		sql.append("  and mae.id_arq = ?");
		sql.append(" ) vw inner join projudi.movi_arq ma on ma.id_movi = vw.id_movi");
		sql.append(" inner join projudi.arq a on a.id_arq = ma.id_arq and a.arq is not null and a.recibo = 1 and a.content_type in ('text/plain','text/html','application/pdf','document/pdf','adobe/pdf')");
		sql.append(" inner join projudi.arq_tipo at on at.id_arq_tipo = a.id_arq_tipo and at.arq_tipo_codigo = " + ArquivoTipoDt.EMENTA + " and at.publico = 1");		
		ps.adicionarLong(id);
		try {
			rs = consultar(sql.toString(), ps);
			if (rs.next()) {
				arquivoDt = new ArquivoDt();
				arquivoDt.setId(rs.getString("ID_ARQ"));
				arquivoDt.setNomeArquivo(rs.getString("NOME_ARQ"));
				arquivoDt.setUsuarioAssinador(rs.getString("USU_ASSINADOR"));
				arquivoDt.setRecibo(Funcoes.FormatarLogico(rs.getString("RECIBO")));
				arquivoDt.setContentType(rs.getString("CONTENT_TYPE"));
				arquivoDt.setArquivo(rs.getBytes("ARQ"));
			}
       } finally {
            try{if (rs != null) rs.close();} catch(Exception e1) {}
       }
	   return arquivoDt;
	}
	
	public void inserirArquivoStorage(ArquivoDt dados) throws Exception {
	    PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String inc = "?";

		if (dados.getArquivoTipoCodigo() != null && dados.getArquivoTipoCodigo().length() > 0) inc = "(SELECT ID_ARQ_TIPO FROM ARQ_TIPO WHERE ARQ_TIPO_CODIGO = ?)";

		String sql = "INSERT INTO ARQ (NOME_ARQ, ID_ARQ_TIPO, CONTENT_TYPE, CAMINHO, ARQ, DATA_INSERCAO, USU_ASSINADOR, RECIBO, CODIGO_TEMP)" + " VALUES(?, " + inc + ", ?, ?, ?, SYSDATE, ?, ?, ?)";

		if (dados.getNomeArquivo().length() == 0) {
			ps.adicionarStringNull();
		} else { 
			ps.adicionarString(dados.getNomeArquivo());
		}
		
		if (dados.getArquivoTipoCodigo() != null && dados.getArquivoTipoCodigo().length() > 0) {
			ps.adicionarString(dados.getArquivoTipoCodigo());
		} else {
			if (dados.getId_ArquivoTipo().length() == 0) ps.adicionarStringNull();
			else ps.adicionarString(dados.getId_ArquivoTipo());
		}

		if (dados.getContentType().length() == 0) ps.adicionarStringNull();
		else ps.adicionarString(dados.getContentType());
		if (dados.getCaminho().length() == 0) ps.adicionarStringNull();
		else ps.adicionarString(dados.getCaminho());

		//Se o CAMINHO do ARQ for maior que zero
		if (dados.getCaminho().length() > 0 || dados.conteudoBytes() == null || dados.conteudoBytes().length == 0) ps.adicionarByteNull();
		else ps.adicionarByte(dados.conteudoBytes());

		if (dados.getUsuarioAssinador().length() == 0) ps.adicionarStringNull();
		else ps.adicionarString(dados.getUsuarioAssinador());

		if (dados.getRecibo() != null && dados.getRecibo().length() != 0 && dados.getRecibo().equalsIgnoreCase("true")) ps.adicionarBoolean(true);
		else ps.adicionarBoolean(false);
		
		ps.adicionarLong(dados.getCodigoTemp());
		
		dados.setId(executarInsert(sql, "ID_ARQ", ps));
				
	}
	
	/**
	 * Grava se o arquivo foi bloqueado na publicacao
	 * Só deixa bloquear o arquivo se a serventia do usuário for a mesma que do arquivo.
	 * @param id identificador do arquivo, em ARQ
	 * @return
	 * @throws Exception
	 */
	public void bloquearArquivoPublicacao (String id, String idServ) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder();
		sql.append(" UPDATE projudi.arq set bloqueado = 1 where id_arq = ?");
		sql.append(" AND EXISTS (SELECT 1 FROM pend_final pf ");
		sql.append(" INNER JOIN pend_final_arq pa ON pf.id_pend = pa.id_pend ");
		sql.append(" INNER JOIN usu_serv us ON us.id_usu_serv = pf.id_usu_cadastrador ");
		sql.append(" WHERE pa.id_arq = ? AND us.id_serv = ?) ");
		ps.adicionarLong(id);
		ps.adicionarLong(id);
		ps.adicionarLong(idServ);
		executarUpdateDelete(sql.toString(), ps);
	}
	
	public boolean arquivoExiste(String nomeArquivo, int id_ArquivoTipo) throws Exception {
		String stSql = "";
		boolean existe = false;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql = "SELECT ID_ARQ FROM PROJUDI.ARQ WHERE NOME_ARQ = ? AND ID_ARQ_TIPO = ?";
		ps.adicionarString(nomeArquivo);
		ps.adicionarLong(id_ArquivoTipo);

		try {
			rs1 = consultar(stSql, ps);
			if(rs1.next()) {
				existe = true;
			}
		} finally {
			try {
				if (rs1 != null) rs1.close();
			} catch (Exception e) {}
		}
		return existe;
	}
	
	public List consultarArquivosECarta(String nomeArquivo, int id_ArquivoTipo, String dataInicial, String dataFinal) throws Exception {
		String stSql = "";
		List arquivosZip = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ArquivoDt arquivoDt = null;

		stSql = "SELECT * FROM PROJUDI.VIEW_ARQ WHERE ID_ARQ_TIPO = ? AND CONTENT_TYPE = ? ";
		ps.adicionarLong(id_ArquivoTipo);
		ps.adicionarString("application/zip");
		
		if (nomeArquivo.length() > 0) {
			stSql += " AND NOME_ARQ = ? ";
			ps.adicionarString(nomeArquivo);
		}
		
		if (dataInicial.length() > 0 ){
			stSql += " AND DATA_INSERCAO >= ?";
			ps.adicionarDateTime(dataInicial + " 00:00:00");
		}
		
		if (dataFinal.length() > 0){
			stSql += " AND DATA_INSERCAO <= ?";
			ps.adicionarDateTime(dataFinal + " 23:59:59");
		}
		
		try {
			rs1 = consultar(stSql, ps);
			while (rs1.next()) {
				arquivoDt = new ArquivoDt();
				associarDt(arquivoDt, rs1);
				arquivosZip.add(arquivoDt);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs1 != null) rs1.close();
			} catch (Exception e) {}
		}
		return arquivosZip;
	}
	
	public void inserirECarta(ArquivoDt dados) throws Exception {
	    PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "INSERT INTO ARQ (NOME_ARQ, ID_ARQ_TIPO, CONTENT_TYPE, CAMINHO, ARQ, DATA_INSERCAO, USU_ASSINADOR, RECIBO)" + " VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

		if (dados.getNomeArquivo().length() == 0) ps.adicionarStringNull();
		else ps.adicionarString(Funcoes.limparNomeArquivo(dados.getNomeArquivo()));
				
		if (dados.getId_ArquivoTipo().length() == 0) ps.adicionarStringNull();
		else ps.adicionarString(dados.getId_ArquivoTipo());

		if (dados.getContentType().length() == 0) ps.adicionarStringNull();
		else ps.adicionarString(dados.getContentType());
		
		if (dados.getCaminho().length() == 0) ps.adicionarStringNull();
		else ps.adicionarString(dados.getCaminho());

		if (dados.getCaminho().length() > 0 || dados.conteudoBytes() == null || dados.conteudoBytes().length == 0) ps.adicionarByteNull();
		else ps.adicionarByte(dados.conteudoBytes());

		if (dados.getDataInsercao().length() == 0) ps.adicionarDateTime(new Date());
		else ps.adicionarDateTime(dados.getDataInsercao());
		
		if (dados.getUsuarioAssinador().length() == 0) ps.adicionarStringNull();
		else ps.adicionarString(dados.getUsuarioAssinador());

		if (dados.getRecibo() != null && dados.getRecibo().length() != 0 && dados.getRecibo().equalsIgnoreCase("true")) ps.adicionarBoolean(true);
		else ps.adicionarBoolean(false);
		
		dados.setId(executarInsert(sql, "ID_ARQ", ps));
				
	} 
	
	public List consultarECarta() throws Exception {
		String Sql;
		ResultSetTJGO rs1 = null;
		List<String[]> lista = new ArrayList<String[]>();
		String[] arquivo = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		Sql = "SELECT ID_ARQ, NOME_ARQ FROM VIEW_ARQ WHERE ID_ARQ_TIPO IN (?,?,?,?,?)";
		ps.adicionarLong(ArquivoTipoDt.ID_CORREIO_RECIBO);
		ps.adicionarLong(ArquivoTipoDt.ID_CORREIO_INCONSISTENCIA);
		ps.adicionarLong(ArquivoTipoDt.ID_CORREIO_POSTAGEM);
		ps.adicionarLong(ArquivoTipoDt.ID_CORREIO_ENTREGA);
		ps.adicionarLong(ArquivoTipoDt.ID_CORREIO_DEVOLUCAO_AR);

		try{
			rs1 = consultar(Sql, ps);
			while (rs1.next()) {
				arquivo = new String[2];
				arquivo[0] = rs1.getString("ID_ARQ");
				arquivo[1] = rs1.getString("NOME_ARQ");
				lista.add(arquivo);
			}
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return lista;
	}
	
	public void alterarDataInsercao(String[] dados) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "UPDATE ARQ SET DATA_INSERCAO = ? WHERE ID_ARQ = ? ";
		ps.adicionarDateTime(dados[1]); 
		ps.adicionarLong(dados[0]);

		executarUpdateDelete(sql, ps);
	}
	
	public String[] lerReciboServicoBD(int id_ArquivoTipo, String dataInicial, String dataFinal, String idPendencia, String rastreamento) throws Exception {
		String stSql = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ByteArrayInputStream byteArrayInputStream;
		ZipInputStream zipInputStream;
		ZipEntry zipEntry;
		byte[] buffer;
		StringBuilder linhasAvisoRecebimento = new StringBuilder();
		StringBuilder reciboServico = null;
		String[] reciboCarta = null;
		String[] linhas = null;
		int contador = 0;

		stSql = "SELECT * FROM PROJUDI.VIEW_ARQ WHERE ID_ARQ_TIPO = ? AND CONTENT_TYPE = ? ";
		ps.adicionarLong(id_ArquivoTipo);
		ps.adicionarString("application/zip");
		
		if (dataInicial.length() > 0 ){
			stSql += " AND DATA_INSERCAO >= ?";
			ps.adicionarDateTime(dataInicial + " 00:00:00");
		}
		
		if (dataFinal.length() > 0){
			stSql += " AND DATA_INSERCAO <= ?";
			ps.adicionarDateTime(dataFinal + " 23:59:59");
		}
		
		try {
			rs1 = consultar(stSql, ps);
			while (rs1.next()) {
				contador++;
				reciboServico = new StringBuilder();
				int length = 0;
				byteArrayInputStream = new ByteArrayInputStream(rs1.getBytes("ARQ"));
				zipInputStream = new ZipInputStream(byteArrayInputStream, StandardCharsets.UTF_8);
				buffer = new byte[1024];
    			while ((zipEntry = zipInputStream.getNextEntry()) != null) {
    				while ((length = zipInputStream.read(buffer, 0, 1024)) >= 0) {
    					reciboServico.append(new String(buffer, 0, length, StandardCharsets.UTF_8));
    				}
    			}
				linhas = reciboServico.toString().split("\n");
				for (int i = 0; i < linhas.length; i++) {
					reciboCarta = linhas[i].split("\\|");
					if(reciboCarta.length >= 4) {
						if(reciboCarta[1].equalsIgnoreCase(idPendencia) || reciboCarta[3].equalsIgnoreCase(rastreamento)) {
							linhasAvisoRecebimento.append(rs1.getString("NOME_ARQ") + "|");
							linhasAvisoRecebimento.append(linhas[i] + "\n");
						}
					}
				}
				byteArrayInputStream.close();
				zipInputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs1 != null) rs1.close();
			} catch (Exception e) {}
		}
		if(linhasAvisoRecebimento.toString().equalsIgnoreCase(""))
			return new String[0];
		else
			return linhasAvisoRecebimento.toString().split("\n");
	}
	
	public String[] lerNotificacaoInconsistenciaBD(int id_ArquivoTipo, String dataInicial, String dataFinal, String idPendencia) throws Exception {
		String stSql = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ByteArrayInputStream byteArrayInputStream;
		ZipInputStream zipInputStream;
		ZipEntry zipEntry;
		byte[] buffer;
		StringBuilder linhasInconsistencia = new StringBuilder();
		StringBuilder inconsistencia = null;
		String[] inconsistenciaCarta = null;
		String[] linhas = null;
		int contador = 0;

		stSql = "SELECT * FROM PROJUDI.VIEW_ARQ WHERE ID_ARQ_TIPO = ? AND CONTENT_TYPE = ? ";
		ps.adicionarLong(id_ArquivoTipo);
		ps.adicionarString("application/zip");
		
		if (dataInicial.length() > 0 ){
			stSql += " AND DATA_INSERCAO >= ?";
			ps.adicionarDateTime(dataInicial + " 00:00:00");
		}
		
		if (dataFinal.length() > 0){
			stSql += " AND DATA_INSERCAO <= ?";
			ps.adicionarDateTime(dataFinal + " 23:59:59");
		}
		
		try {
			rs1 = consultar(stSql, ps);
			while (rs1.next()) {
				contador++;
				inconsistencia = new StringBuilder();
				int length = 0;
				byteArrayInputStream = new ByteArrayInputStream(rs1.getBytes("ARQ"));
				zipInputStream = new ZipInputStream(byteArrayInputStream, StandardCharsets.UTF_8);
				buffer = new byte[1024];
    			while ((zipEntry = zipInputStream.getNextEntry()) != null) {
    				while ((length = zipInputStream.read(buffer, 0, 1024)) >= 0) {
    					inconsistencia.append(new String(buffer, 0, length, StandardCharsets.UTF_8));
    				}
    			}
				linhas = inconsistencia.toString().split("\n");
				for (int i = 0; i < linhas.length; i++) {
					inconsistenciaCarta = linhas[i].split("\\|");
					if(inconsistenciaCarta.length >= 4 && inconsistenciaCarta[0].equalsIgnoreCase("1")) {
						if(inconsistenciaCarta[1].equalsIgnoreCase(idPendencia)) {
							linhasInconsistencia.append(rs1.getString("NOME_ARQ") + "|");
							linhasInconsistencia.append(linhas[i] + "\n");
						}
					}
				}
				byteArrayInputStream.close();
				zipInputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs1 != null) rs1.close();
			} catch (Exception e) {}
		}
		if(linhasInconsistencia.toString().equalsIgnoreCase(""))
			return new String[0];
		else
			return linhasInconsistencia.toString().split("\n");
	}
	
	public String[] lerRastreamentoDataEstimadaEntregaBD(int id_ArquivoTipo, String dataInicial, String dataFinal, Map codRastreamento) throws Exception {
		String stSql = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ByteArrayInputStream byteArrayInputStream;
		ZipInputStream zipInputStream;
		ZipEntry zipEntry;
		byte[] buffer;
		StringBuilder linhasPostagem = new StringBuilder();
		StringBuilder postagem = null;
		String[] postagemCarta = null;
		String[] linhas = null;
		int contador = 0;

		stSql = "SELECT * FROM PROJUDI.VIEW_ARQ WHERE ID_ARQ_TIPO = ? AND CONTENT_TYPE = ? ";
		ps.adicionarLong(id_ArquivoTipo);
		ps.adicionarString("application/zip");
		
		if (dataInicial.length() > 0 ){
			stSql += " AND DATA_INSERCAO >= ?";
			ps.adicionarDateTime(dataInicial + " 00:00:00");
		}
		
		if (dataFinal.length() > 0){
			stSql += " AND DATA_INSERCAO <= ?";
			ps.adicionarDateTime(dataFinal + " 23:59:59");
		}
		
		try {
			rs1 = consultar(stSql, ps);
			while (rs1.next()) {
				contador++;
				postagem = new StringBuilder();
				int length = 0;
				byteArrayInputStream = new ByteArrayInputStream(rs1.getBytes("ARQ"));
				zipInputStream = new ZipInputStream(byteArrayInputStream, StandardCharsets.UTF_8);
				buffer = new byte[1024];
    			while ((zipEntry = zipInputStream.getNextEntry()) != null) {
    				while ((length = zipInputStream.read(buffer, 0, 1024)) >= 0) {
    					postagem.append(new String(buffer, 0, length, StandardCharsets.UTF_8));
    				}
    			}
				linhas = postagem.toString().split("\n");
				for (int i = 0; i < linhas.length; i++) {
					postagemCarta = linhas[i].split("\\|");
					if(postagemCarta.length >= 15 && codRastreamento.containsKey(postagemCarta[1])) {
						linhasPostagem.append(rs1.getString("NOME_ARQ") + "|");
						linhasPostagem.append(linhas[i] + "\n");
					}
				}
				byteArrayInputStream.close();
				zipInputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs1 != null) rs1.close();
			} catch (Exception e) {}
		}
		if(linhasPostagem.toString().equalsIgnoreCase(""))
			return new String[0];
		else
			return linhasPostagem.toString().split("\n");
	}
	
	public String[] lerRastreamentoDataFinalEntregaBD(int id_ArquivoTipo, String dataInicial, String dataFinal, Map codRastreamento) throws Exception {
		String stSql = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ByteArrayInputStream byteArrayInputStream;
		ZipInputStream zipInputStream;
		ZipEntry zipEntry;
		byte[] buffer;
		StringBuilder linhasEntrega = new StringBuilder();
		StringBuilder entrega = null;
		String[] entregaCarta = null;
		String[] linhas = null;
		int contador = 0;

		stSql = "SELECT * FROM PROJUDI.VIEW_ARQ WHERE ID_ARQ_TIPO = ? AND CONTENT_TYPE = ? ";
		ps.adicionarLong(id_ArquivoTipo);
		ps.adicionarString("application/zip");
		
		if (dataInicial.length() > 0 ){
			stSql += " AND DATA_INSERCAO >= ?";
			ps.adicionarDateTime(dataInicial + " 00:00:00");
		}
		
		if (dataFinal.length() > 0){
			stSql += " AND DATA_INSERCAO <= ?";
			ps.adicionarDateTime(dataFinal + " 23:59:59");
		}
		
		try {
			rs1 = consultar(stSql, ps);
			while (rs1.next()) {
				contador++;
				entrega = new StringBuilder();
				int length = 0;
				byteArrayInputStream = new ByteArrayInputStream(rs1.getBytes("ARQ"));
				zipInputStream = new ZipInputStream(byteArrayInputStream, StandardCharsets.UTF_8);
				buffer = new byte[1024];
    			while ((zipEntry = zipInputStream.getNextEntry()) != null) {
    				while ((length = zipInputStream.read(buffer, 0, 1024)) >= 0) {
    					entrega.append(new String(buffer, 0, length, StandardCharsets.UTF_8));
    				}
    			}
				linhas = entrega.toString().split("\n");
				for (int i = 0; i < linhas.length; i++) {
					entregaCarta = linhas[i].split("\\|");
					if(entregaCarta.length >= 15 && codRastreamento.containsKey(entregaCarta[1])) {
						linhasEntrega.append(rs1.getString("NOME_ARQ") + "|");
						linhasEntrega.append(linhas[i] + "\n");
					}
				}
				byteArrayInputStream.close();
				zipInputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs1 != null) rs1.close();
			} catch (Exception e) {}
		}
		if(linhasEntrega.toString().equalsIgnoreCase(""))
			return new String[0];
		else
			return linhasEntrega.toString().split("\n");
	}
	
	public String[] lerDevolucaoARsBD(int id_ArquivoTipo, String dataInicial, String dataFinal, String idPendencia, Map codRastreamento) throws Exception {
		String stSql = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ByteArrayInputStream byteArrayInputStream;
		ZipInputStream zipInputStream;
		ZipEntry zipEntry;
		byte[] buffer;
		StringBuilder linhasAvisoRecebimento = new StringBuilder();
		StringBuilder avisoRecebimento = null;
		String[] arCarta = null;
		String[] linhas = null;
		int contador = 0;

		stSql = "SELECT * FROM PROJUDI.VIEW_ARQ WHERE ID_ARQ_TIPO = ? AND CONTENT_TYPE = ? ";
		ps.adicionarLong(id_ArquivoTipo);
		ps.adicionarString("application/zip");
		
		if (dataInicial.length() > 0 ){
			stSql += " AND DATA_INSERCAO >= ?";
			ps.adicionarDateTime(dataInicial + " 00:00:00");
		}
		
		if (dataFinal.length() > 0){
			stSql += " AND DATA_INSERCAO <= ?";
			ps.adicionarDateTime(dataFinal + " 23:59:59");
		}
		
		try {
			rs1 = consultar(stSql, ps);
			while (rs1.next()) {
				contador++;
				avisoRecebimento = new StringBuilder();
				int length = 0;
				byteArrayInputStream = new ByteArrayInputStream(rs1.getBytes("ARQ"));
				zipInputStream = new ZipInputStream(byteArrayInputStream, StandardCharsets.UTF_8);
				buffer = new byte[1024];
				while ((zipEntry = zipInputStream.getNextEntry()) != null) {
					if (zipEntry.getName().contains("DevolucaoAR")) {
						while ((length = zipInputStream.read(buffer, 0, 1024)) >= 0) {
							avisoRecebimento.append(new String(buffer, 0, length, StandardCharsets.UTF_8));
						}
						linhas = avisoRecebimento.toString().split("\n");
						for (int i = 0; i < linhas.length; i++) {
							arCarta = linhas[i].split("\\|");
							if(arCarta.length >= 7) {
								if(codRastreamento.containsKey(arCarta[3]) || arCarta[1].equalsIgnoreCase(idPendencia)) {
									linhasAvisoRecebimento.append(rs1.getString("NOME_ARQ") + "|");
									linhasAvisoRecebimento.append(linhas[i] + "\n");
								}
							}
						}
					}
				}
				byteArrayInputStream.close();
				zipInputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs1 != null) rs1.close();
			} catch (Exception e) {}
		}
		if(linhasAvisoRecebimento.toString().equalsIgnoreCase(""))
			return new String[0];
		else
			return linhasAvisoRecebimento.toString().split("\n");
	}
	
	public void salvarArquivoTransacaoCeph(ArquivoDt dados) throws Exception {
	    PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
	    String sql = "INSERT INTO ARQ_CEPH (ID_ARQ, ID_USU, CAMINHO_CEPH, DATA_INCLUSAO) VALUES(?, ?, ?, SYSDATE)";
	    
	    ps.adicionarLong(dados.getId());
	    if (dados.getId_UsuarioLog() != null && dados.getId_UsuarioLog().trim().length() > 0) {
	    	ps.adicionarLong(dados.getId_UsuarioLog());
	    } else {
	    	ps.adicionarLong(UsuarioDt.SistemaProjudi);
	    }
	    ps.adicionarString(dados.getCaminhoCeph());
	    
	    super.executarUpdateDelete(sql, ps);
	}
	
	public void excluirArquivosControleCeph(String idUsuarioLog) throws Exception {
	    PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
	    String sql = "DELETE FROM ARQ_CEPH WHERE ID_USU = ?";
	    
	    ps.adicionarLong(idUsuarioLog);
	    
	    super.executarUpdateDelete(sql, ps);
	}
	
	private void salvarArquivoCeph(ArquivoDt arquivoDt) throws Exception {
		String bucketName = ProjudiPropriedades.getInstance().getObjectStorageBucketProjudi();
		
		InputStream inputStream = new ByteArrayInputStream(arquivoDt.conteudoBytes());
					   
		AmazonS3 s3Client = obtenhaConexaoObjectStorageProjudi();
		
		ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(arquivoDt.getContentType());
        metadata.addUserMetadata("Projudi", "PecaProcessual");
        metadata.setContentLength(arquivoDt.conteudoBytes().length);
        
        PutObjectRequest request = new PutObjectRequest(bucketName, arquivoDt.getNomeObjetosStorage(), inputStream, metadata);
        PutObjectResult result = s3Client.putObject(request);        
	}
	
	private AmazonS3 obtenhaConexaoObjectStorageProjudi() {
		boolean setSignerOverride = true;
		if (s3Client==null || s3Client.listBuckets()==null ) {
			ProjudiPropriedades projudiPropriedades=ProjudiPropriedades.getInstance(); 
			String accessKey = projudiPropriedades.getObjectStorageAccessKeyProjudi();
			String secretKey = projudiPropriedades.getObjectStorageSecretKeyProjudi();
			String host = projudiPropriedades.getObjectStorageHost();
			
			BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
	    	ClientConfiguration clientConfig = new ClientConfiguration();
	    	if (projudiPropriedades.isObjectStorageUploadProtocoloHTTP()) {
	    		clientConfig.setProtocol(Protocol.HTTP);    			
	    	} else {
	    		clientConfig.setProtocol(Protocol.HTTPS);    		
	    	}
	    	
	    	if (setSignerOverride) {
				clientConfig.setSignerOverride("S3SignerType");	
			}	
			//clientConfig.setUseGzip(true);			
			
			 s3Client = new AmazonS3Client(awsCreds, clientConfig);
			
			S3ClientOptions s3ClientOptions = new S3ClientOptions();
			s3Client.setS3ClientOptions(s3ClientOptions);
			s3Client.setEndpoint(host);
		}
		return s3Client;
	}
	
	private byte[] getConteudoArquivoCeph(String stCaminho) throws IOException, MensagemException {
				
		InputStream inputStreamObjectStorage = this.obtenhaStreamObjectStorageProjudi(stCaminho);
		byte[] conteudo = null;
		try {
			conteudo = IOUtils.toByteArray(inputStreamObjectStorage);
			
  		} catch(Exception e) {
  			e.printStackTrace();
  		} finally {
  			if (inputStreamObjectStorage != null) inputStreamObjectStorage.close();
  		}			
		return conteudo;
	}

	private InputStream obtenhaStreamObjectStorageProjudi(String stCaminho) throws MensagemException {
				
		int indiceInicioBucket = stCaminho.indexOf("[");
		int indiceFinalBucket = stCaminho.indexOf("]");
		
		if (indiceInicioBucket < 0 || indiceFinalBucket < 0 || indiceFinalBucket <= indiceInicioBucket) 
			throw new MensagemException("Caminho da mídia digital inválido");
		
		String bucket = stCaminho.substring(indiceInicioBucket + 1, indiceFinalBucket);
		String pathArquivo = stCaminho.substring(indiceFinalBucket + 1);
		
		AmazonS3 conn = obtenhaConexaoObjectStorageProjudi();
		
		try {
			return conn.getObject(new GetObjectRequest(bucket, pathArquivo)).getObjectContent();	
		} catch (Exception ex) {
			throw new MensagemException("Não foi possível localizar no Storage o arquivo " + stCaminho + ". Favor entrar em contato com o responsável no departamento de infraestrutura.", ex);
		}
	}
}
