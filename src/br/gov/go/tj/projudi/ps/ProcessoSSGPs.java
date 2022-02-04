package br.gov.go.tj.projudi.ps;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.gov.go.tj.projudi.dt.NumeroProcessoDt;
import br.gov.go.tj.projudi.dt.PoloSPGDt;
import br.gov.go.tj.projudi.dt.ProcessoParteEnderecoSPGDt;
import br.gov.go.tj.projudi.dt.ProcessoSSGDt;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.TJDataHora;

public class ProcessoSSGPs implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8331243537744585850L;
	
	public static final int PROMOVIDO_CODIGO_SPG_SSG        = 2;
	public static final int PROMOVENTE_CODIGO_SPG_SSG       = 1;
	
	protected transient Connection Conexao;	
	
	public ProcessoSSGPs(FabricaConexao obFabricaConexao){
		this.Conexao = obFabricaConexao.getConexao();
	}
	
	public ProcessoSSGDt consulteProcesso(NumeroProcessoDt numeroProcessoCompletoDt) throws Exception{
		return consulteProcesso(obtenhaConsultaSQLProcesso(numeroProcessoCompletoDt, null), numeroProcessoCompletoDt);
	}
	
	public ProcessoSSGDt consulteProcesso(String numeroProcessoSSG) throws Exception{
		return consulteProcesso(obtenhaConsultaSQLProcesso(null, numeroProcessoSSG), null);
	}
	
	private ProcessoSSGDt consulteProcesso(String consultaSQL, NumeroProcessoDt numeroProcessoCompletoDt) throws Exception {
		ProcessoSSGDt processo = null;
		
		ResultSet rs = null;
		
		try {			
			rs = this.Conexao.prepareStatement(consultaSQL).executeQuery();
			
			if (rs.next()) {
				processo = new ProcessoSSGDt();
				processo.setEhProcessoFisico(true);
				if (numeroProcessoCompletoDt == null) {
					numeroProcessoCompletoDt = new NumeroProcessoDt(rs.getString("NUMR_PROCESSO_CNJ"), rs.getString("INFO_ANO_PROCESSO_CNJ"), rs.getString("CODG_ORIGEM_PROCESSO_CNJ"));					
				}
				processo.setNumeroProcessoCompletoDt(numeroProcessoCompletoDt);
				processo.setNumeroProcessoSSG(rs.getString("NUMR_PROCESSO"));
				
				if (rs.getLong("CODG_COMARCA") > 0) {
					processo.setCodComarca(rs.getString("CODG_COMARCA"));
					processo.setNomeComarca(obtenhaDescricaoDaComarca("V_SPGUCOMARCAS ", rs.getLong("CODG_COMARCA")));
				}
				
				libereResultset(rs);
				
				rs = this.Conexao.prepareStatement(obtenhaConsultaSQLParte(processo.getNumeroProcessoSSG())).executeQuery();
				
				while (rs.next()) {					
					//Adiciona parte a lista correspondente					
					PoloSPGDt poloDt = new PoloSPGDt();
					
					poloDt.setId(rs.getString("NUMR_ISN"));
					
					poloDt.setNome(rs.getString("NOME_PESSOA"));
					poloDt.setFiliacao(rs.getString("NOME_MAE"));
					
					if (rs.getString("NUMR_CPF_CGC") != null){
						if (rs.getString("NUMR_CPF_CGC").trim().length() <= 11)							
							poloDt.setCpf(Funcoes.completaCPFZeros(rs.getString("NUMR_CPF_CGC")));
						else							
							poloDt.setCpf(Funcoes.completaCNPJZeros(rs.getString("NUMR_CPF_CGC")));							
					}
						
					if (rs.getDate("DATA_NASC") != null)
						poloDt.setDataDeNascimento(new TJDataHora(rs.getDate("DATA_NASC")));
					
					switch (Funcoes.StringToInt(rs.getString("TIPO_PARTE"))) {
						case (PROMOVENTE_CODIGO_SPG_SSG):
							processo.adicionePoloAtivo(poloDt);
							break;
						case (PROMOVIDO_CODIGO_SPG_SSG):
							processo.adicionePoloPassivos(poloDt);
							break;
						default:
							processo.adicioneOutrosPolos(poloDt);
							break;
					}					
				}
			}
		}  finally {
			libereResultset(rs);
		}
		
		return processo;
	}
	
	private void libereResultset(ResultSet rs){
		try {if (rs != null) rs.close();} catch (Exception e) {}
	}
	
	private String obtenhaConsultaSQLProcesso(NumeroProcessoDt numeroProcessoCompletoDt, String numeroProcessoSSG){
		return obtenhaConsultaSQLProcesso("V_SSGUPROCESSO", numeroProcessoCompletoDt, numeroProcessoSSG);
	}
	
	private String obtenhaConsultaSQLProcesso(String nomeTabela, NumeroProcessoDt numeroProcessoCompletoDt, String numeroProcessoSSG){
		StringBuffer consultaSQL = new StringBuffer();
		
		consultaSQL.append(" SELECT NUMR_PROCESSO ");
		consultaSQL.append(" ,NUMR_PROCESSO_CNJ ");
		consultaSQL.append(" ,INFO_ANO_PROCESSO_CNJ ");
		consultaSQL.append(" ,CODG_ORIGEM_PROCESSO_CNJ ");
		consultaSQL.append(" ,CODG_COMARCA ");
		consultaSQL.append(" FROM ");
		consultaSQL.append(nomeTabela);

		if ( numeroProcessoCompletoDt != null && numeroProcessoCompletoDt.getNumero() > 0 && numeroProcessoCompletoDt.getDigito() >= 0 && numeroProcessoCompletoDt.getAno() > 0 ) {
			consultaSQL.append(" WHERE NUMR_PROCESSO_CNJ =");
			consultaSQL.append(numeroProcessoCompletoDt.getNumero());
			consultaSQL.append(Funcoes.preencheZeros(numeroProcessoCompletoDt.getDigito(), 2));
			consultaSQL.append(" AND INFO_ANO_PROCESSO_CNJ =");
			consultaSQL.append(numeroProcessoCompletoDt.getAno());
			
			if(numeroProcessoCompletoDt.getForum() != 0){
				consultaSQL.append(" AND CODG_ORIGEM_PROCESSO_CNJ =");
				consultaSQL.append(numeroProcessoCompletoDt.getForum());	
			} else {
				consultaSQL.append(" AND CODG_ORIGEM_PROCESSO_CNJ IS NULL");
			}
		} else {
			if( numeroProcessoSSG != null && !numeroProcessoSSG.isEmpty() ) {
				consultaSQL.append(" WHERE NUMR_PROCESSO = ");
				if (numeroProcessoSSG.endsWith("0000")) {
					consultaSQL.append(Funcoes.completarZeros(numeroProcessoSSG, 16));	
				} else  {
					consultaSQL.append(Funcoes.completarZeros(numeroProcessoSSG, 12) + "0000");	
				}
			}
			else {
				return null;
			}
		}
		
		return consultaSQL.toString();
	}
	
	private String obtenhaConsultaSQLParte(String numeroProcessoSSG){
		StringBuffer consultaSQL = new StringBuffer();	
		
		consultaSQL.append(" SELECT PT.NUMR_ISN, PT.TIPO_PARTE, PS.NOME_PESSOA, PS.NOME_MAE, PS.DATA_NASC, PS.NUMR_CPF_CGC ");
		consultaSQL.append(" FROM ");
		consultaSQL.append(" V_SPGAPARTES ");
		consultaSQL.append(" PT, V_SPGAPESSOAS PS ");
		consultaSQL.append(" WHERE PT.NUMR_ISN = PS.NUMR_ISN ");	
		consultaSQL.append(" AND PT.NUMR_PROCESSO =");
		consultaSQL.append(numeroProcessoSSG);
		
		return consultaSQL.toString();
	}
	
	public ProcessoParteEnderecoSPGDt obtenhaEnderecoParte(String isnParte) throws SQLException {
		ProcessoParteEnderecoSPGDt dados = null;
		
		ResultSet rs = null;
		
		try {			
			rs = this.Conexao.prepareStatement(obtenhaConsultaSQLEnderecoParte(isnParte)).executeQuery();
			
			if (rs.next()) {
				dados = new ProcessoParteEnderecoSPGDt();
				dados.setId(rs.getString("INFO_ENDER_COUNT"));
				dados.setTipoEndereco(rs.getString("TIPO_ENDER"));
				dados.setNomeRua(rs.getString("INFO_RUA"));
				dados.setNumero(rs.getString("INFO_NUMERO"));
				dados.setLote(rs.getString("INFO_LOTE"));
				dados.setQuadra(rs.getString("INFO_QUADRA"));
				dados.setBairro(rs.getString("NOME_BAIRRO"));
				dados.setMunicipio(rs.getString("NOME_MUNIC"));
				dados.setComplemento(rs.getString("INFO_COMPLEMENTO"));
				dados.setCep(rs.getString("NUMR_CEP"));
			}
		} finally {
			if (rs != null) rs.close();
		}
		
		return dados;
	}
	
	private String obtenhaConsultaSQLEnderecoParte(String isnParte){
		StringBuffer consultaSQL = new StringBuffer();	
		
		consultaSQL.append(" SELECT IE.INFO_ENDER_COUNT, IE.TIPO_ENDER, IE.INFO_RUA, IE.INFO_NUMERO, IE.INFO_LOTE, IE.INFO_QUADRA, IE.INFO_COMPLEMENTO, IE.NUMR_CEP, BR.NOME_BAIRRO, MU.NOME_MUNIC ");
		consultaSQL.append(" FROM V_SPGAPESSOAS PS, V_SPGAPESSOAS_INFO_ENDER IE, ");
		consultaSQL.append(" V_SPGUBAIRROS BR, V_SPGUMUNICIPIOS MU ");
		consultaSQL.append(" WHERE PS.NUMR_ISN = " + isnParte);
		consultaSQL.append(" AND PS.NUMR_ISN = IE.ISN_SPGA_PESSOAS ");
		consultaSQL.append(" AND IE.CODG_BAIRRO = BR.ISN_SPGU_BAIRROS AND IE.CODG_MUNICIPIO = MU.ISN_SPGU_MUNICIPIOS ");
		consultaSQL.append(" ORDER BY IE.TIPO_ENDER");
				
		return consultaSQL.toString();
	}
	
	private String obtenhaDescricaoDaComarca(String nomeTabela, long codigoDaComarca) throws SQLException{
		ResultSet rs = null;
		
		try {			
			rs = this.Conexao.prepareStatement(obtenhaConsultaSQLComarca(nomeTabela, codigoDaComarca)).executeQuery();
			
			if (rs.next()) 
				return rs.getString("NOME_COMARCA");	
			
		}  finally {
			libereResultset(rs);
		}
		
		return "";
	}
	
	private String obtenhaConsultaSQLComarca(String nomeTabela, long codigoDaComarca){
		StringBuffer consultaSQL = new StringBuffer();	
		
		consultaSQL.append(" SELECT NOME_COMARCA ");
		consultaSQL.append(" FROM ");
		consultaSQL.append(nomeTabela);		
		consultaSQL.append(" WHERE CODG_COMARCA =");
		consultaSQL.append(codigoDaComarca);		
				
		return consultaSQL.toString();
	}
}