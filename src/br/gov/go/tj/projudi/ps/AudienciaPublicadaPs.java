package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.AudienciaDRSDt;
import br.gov.go.tj.projudi.dt.AudienciaPublicadaDt;
import br.gov.go.tj.projudi.dt.AudienciaTipoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoTipoDt;

import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import br.gov.go.tj.utils.TJDataHora;

public class AudienciaPublicadaPs extends Persistencia {

	/**
     * 
     */
    private static final long serialVersionUID = 5788017815870918759L;

    
    public AudienciaPublicadaPs(Connection conexao){
		Conexao = conexao;
	}

	public List consultarTodasAudienciasPublicadasNaoProcessadas() throws Exception {
		String statusTemp = String.valueOf(Math.round(Math.random() * 100000));
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql;
				
		try{
			sql = "UPDATE PROJUDI.AUDI_PUBLICADA";
			sql += " SET CODIGO_TEMP = ?";
			ps.adicionarString(statusTemp);
			sql += " WHERE STATUS IS NULL";
			sql += " AND CODIGO_TEMP IS NULL";
						
			executarUpdateDelete(sql, ps);
			
			sql = "SELECT PROC_NUMERO,";
			sql += " DATA_HORA_AUDI";			
			sql += " FROM PROJUDI.AUDI_PUBLICADA";
			sql += " WHERE STATUS IS NULL";
			sql += " AND CODIGO_TEMP = ?";
					
			rs1 = consultar(sql, ps);
			
			while (rs1.next()) {
				AudienciaPublicadaDt obTemp = new AudienciaPublicadaDt();
				associarDt(obTemp, rs1);				
				liTemp.add(obTemp);
			}			
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}             
        }
		return liTemp; 
	}
	
	protected void associarDt(AudienciaPublicadaDt dados, ResultSetTJGO rs1 )  throws Exception {
			
		dados.setProcessoNumero(rs1.getString("PROC_NUMERO"));
		dados.setDataAudiencia(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_HORA_AUDI")));
		
	}
	
	/**
	 * Consulta o id do processo
	 * @param NumeroCompletoDoProcesso: número completo do processo
	 * @return String idProcesso.
	 * @throws Exception
	 * @author mmgomes
	 */
	public String consultarIdProcesso(String numeroCompletoDoProcesso) throws Exception {
		String id_Processo = "";		
		String sql;
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		try{
			
			numeroCompletoDoProcesso = Funcoes.desformataNumeroProcesso(numeroCompletoDoProcesso.trim());
			numeroCompletoDoProcesso = Funcoes.completarZeros(numeroCompletoDoProcesso, 20);
			
			sql = "SELECT ID_PROC";
			sql += " FROM PROJUDI.PROC";
			sql += " WHERE PROC_NUMERO = ?";
			ps.adicionarLong(numeroCompletoDoProcesso.substring(0, 7));
			sql += " AND DIGITO_VERIFICADOR = ?";
			ps.adicionarLong(numeroCompletoDoProcesso.substring(7,9));
			sql += " AND ANO = ?";
			ps.adicionarLong(numeroCompletoDoProcesso.substring(9,13));
			sql += " AND FORUM_CODIGO = ?";
			ps.adicionarLong(numeroCompletoDoProcesso.substring(16,20));	

			rs = consultar(sql, ps);
			if (rs.next())id_Processo = rs.getString("ID_PROC");			
		
		} finally{
			try{
				if (rs != null) rs.close();
			} catch(Exception e) {
			}
		}
		return id_Processo;
	}
	
	public void atualizeAudienciaPublicadaProcessoNaoEncontrado(AudienciaPublicadaDt dados) throws Exception {

		String sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql = "UPDATE PROJUDI.AUDI_PUBLICADA";
		sql += " SET STATUS = ?";
		ps.adicionarString(AudienciaPublicadaDt.STATUS_PROCESSO_NAO_ENCONTRADO);
		sql += " WHERE PROC_NUMERO = ?";
		ps.adicionarString(dados.getProcessoNumero());
		sql += " AND DATA_HORA_AUDI = ?";
		ps.adicionarDateTime(dados.getDataAudiencia());					
					
		executarUpdateDelete(sql, ps);
	}
	
	public void excluirAudienciaPublicada(AudienciaPublicadaDt dados) throws Exception {

		String sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql= "DELETE FROM PROJUDI.AUDI_PUBLICADA";
		sql += " WHERE PROC_NUMERO = ?";
		ps.adicionarString(dados.getProcessoNumero());
		sql += " AND DATA_HORA_AUDI = ?";
		ps.adicionarDateTime(dados.getDataAudiencia());
			
		executarUpdateDelete(sql, ps);

	}
	
	public List<AudienciaDRSDt> consultarTodasAudienciasPublicadasProcessadas(String idProcesso) throws Exception {
		List<AudienciaDRSDt> liTemp = new ArrayList<AudienciaDRSDt>();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql;
				
		try{
			sql = "SELECT M.COMPLEMENTO";
			sql += " FROM PROJUDI.MOVI M ";			
			sql += " WHERE ID_PROC = ? "; ps.adicionarLong(idProcesso);
			sql += " AND ID_MOVI_TIPO = (SELECT ID_MOVI_TIPO FROM MOVI_TIPO WHERE MOVI_TIPO_CODIGO = ?)"; ps.adicionarLong(MovimentacaoTipoDt.AUDIENCIA_PUBLICADA);
					
			rs1 = consultar(sql, ps);
			
			while (rs1.next()) {
				String complementoMovimentacao = rs1.getString("COMPLEMENTO");
				
				// Quebrando o link em duas partes. ex: http://www.teste.com?processoNumero=xxxx&DataAudiencia=yyyy&Hash=<<HASH>>
				String[] link = complementoMovimentacao.split("\\?");		
				if (link != null && link.length == 2) {				
					// Quebrando o link em três partes. ex: processoNumero=xxxx&DataAudiencia=yyyy&Hash=<<HASH>>
					String[] complemento = link[1].split("&");
					if (complemento != null && complemento.length == 3) {
						// Quebrando o complemento em duas partes para obter o número do processo. ex: processoNumero=xxxx
						String[] valorComplementoNumeroProcesso = complemento[0].split("=");
						
						if (valorComplementoNumeroProcesso != null && valorComplementoNumeroProcesso.length == 2) {
							String numeroProcesso = valorComplementoNumeroProcesso[1];
							
							// Quebrando o complemento em duas partes para obter a data e hora da audiência. ex: DataAudiencia=xxxx
							String[] valorComplementoDataHoraAudienciaPublicada = complemento[1].split("=");
							if (valorComplementoDataHoraAudienciaPublicada != null && valorComplementoDataHoraAudienciaPublicada.length == 2) {
								String dataHoraAudienciaPublicada = valorComplementoDataHoraAudienciaPublicada[1];
								
								// Obtendo a data e hora da audiência
								TJDataHora dataHoraAudiencia = new TJDataHora();
								dataHoraAudiencia.setDataHoraFormatadayyyyMMddHHmmss(dataHoraAudienciaPublicada);
								
								AudienciaDRSDt obTemp = new AudienciaDRSDt();								
								obTemp.setProcessoNumero(numeroProcesso);
								obTemp.setDataHoraDaAudiencia(dataHoraAudiencia);
								liTemp.add(obTemp);
							}							
						}
					}					
				}
			}			
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}             
        }
		return liTemp; 
	}
} 