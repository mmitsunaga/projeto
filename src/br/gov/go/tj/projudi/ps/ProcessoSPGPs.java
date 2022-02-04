package br.gov.go.tj.projudi.ps;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import br.gov.go.tj.projudi.dt.NumeroProcessoDt;
import br.gov.go.tj.projudi.dt.PoloSPGDt;
import br.gov.go.tj.projudi.dt.ProcessoParteEnderecoSPGDt;
import br.gov.go.tj.projudi.dt.ProcessoSPGDt;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.TJDataHora;

public class ProcessoSPGPs  extends Persistencia {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8072952915113303422L;
	
	public static final int PROMOVIDO_CODIGO_SPG_SSG        = 2;
	public static final int PROMOVENTE_CODIGO_SPG_SSG       = 1;
	
	public ProcessoSPGPs(FabricaConexao obFabricaConexao){
		this.Conexao = obFabricaConexao.getConexao();
	}
	
	public ProcessoSPGDt consulteProcesso(NumeroProcessoDt numeroProcessoCompletoDt) throws Exception{
		ProcessoSPGDt processo = null;
		
		processo = consulteProcessoBaseLocal(numeroProcessoCompletoDt);
		
		if (processo == null)		
			processo = consulteProcessoBaseRemota(numeroProcessoCompletoDt);
		
		return processo;
	}
	
	private ProcessoSPGDt consulteProcessoBaseLocal(NumeroProcessoDt numeroProcessoCompletoDt) throws Exception{
		return consulteProcesso(obtenhaConsultaSQLProcessoLocal(numeroProcessoCompletoDt, null), numeroProcessoCompletoDt, true);
	}
	
	private ProcessoSPGDt consulteProcessoBaseRemota(NumeroProcessoDt numeroProcessoCompletoDt) throws Exception{
		return consulteProcesso(obtenhaConsultaSQLProcessoRemoto(numeroProcessoCompletoDt, null), numeroProcessoCompletoDt, false);
	}
	
	public ProcessoSPGDt consulteProcesso(String numeroSPG) throws Exception{
		ProcessoSPGDt processo = null;
		
		processo = consulteProcessoBaseLocal(numeroSPG);
		
		if (processo == null)		
			processo = consulteProcessoBaseRemota(numeroSPG);
		
		return processo;
	}
	
	private ProcessoSPGDt consulteProcessoBaseLocal(String numeroSPG) throws Exception{
		return consulteProcesso(obtenhaConsultaSQLProcessoLocal(null, numeroSPG), null, true);
	}
	
	private ProcessoSPGDt consulteProcessoBaseRemota(String numeroSPG) throws Exception{
		return consulteProcesso(obtenhaConsultaSQLProcessoRemoto(null, numeroSPG), null, false);
	}
	
	private ProcessoSPGDt consulteProcesso(String consultaSQL, NumeroProcessoDt numeroProcessoCompletoDt, boolean ehLocal) throws Exception{
		ProcessoSPGDt processo = null;
		
		ResultSet rs = null;
		
		try {
			if( consultaSQL == null || (consultaSQL != null && consultaSQL.isEmpty()) ) {
				return null;
			}
			
			rs = this.Conexao.prepareStatement(consultaSQL).executeQuery();
			
			if (rs.next()) {
				processo = new ProcessoSPGDt();
				processo.setEhProcessoFisico(true);
				if (numeroProcessoCompletoDt == null) {
					numeroProcessoCompletoDt = new NumeroProcessoDt(rs.getString("NUMR_PROCESSO_CNJ"), rs.getString("INFO_ANO_PROCESSO_CNJ"), rs.getString("CODG_ORIGEM_PROCESSO_CNJ"));					
				}
				processo.setNumeroProcessoCompletoDt(numeroProcessoCompletoDt);
				processo.setNumeroProcessoSPG(rs.getString("NUMR_PROCESSO"));
				processo.setNumeroTCOInquerito(rs.getString("NUMR_TCO"));
				
				//A comarca são os 3 primeiros dígitos do CODG_SERVENTIA
				if (rs.getLong("CODG_SERVENTIA") > 0) {
					
					//Código da Comarca são os 3 primeiros digitos de um número de 6, então completa com zeros.
					String codComarca = (Funcoes.completarZeros(rs.getString("CODG_SERVENTIA"), 6)).substring(0,3);
					processo.setCodComarca(codComarca);
					if(ehLocal)
						processo.setNomeComarca(obtenhaDescricaoDaComarca("V_SPGUCOMARCAS ", Long.parseLong(codComarca)));
					else
						processo.setNomeComarca(obtenhaDescricaoDaComarca("V_SPGUCOMARCAS_REM ", Long.parseLong(codComarca)));
				}
				
				if (rs.getDate("DATA_DISTRIBUICAO") != null)
					processo.setDataDistribuicao(new TJDataHora(rs.getDate("DATA_DISTRIBUICAO")));
				
				if (rs.getString("INFO_SIGILOSO") != null)
					processo.setEhSegredoDeJustica(rs.getString("INFO_SIGILOSO").equalsIgnoreCase("S"));
				
				if (rs.getDate("DATA_BAIXA") == null)
					processo.setStatus("Ativo");
				else
					processo.setStatus("Arquivado");
				
				if (rs.getLong("CODG_JUIZ_ESPECIAL") > 0)
					processo.setNomeMagistradoResponsavel(obtenhaNomeDoMagistrado(rs.getLong("CODG_JUIZ_ESPECIAL"), ehLocal));				
				else
					processo.setNomeMagistradoResponsavel(obtenhaNomeDoMagistrado(rs.getLong("CODG_SERVENTIA"), rs.getLong("INFO_POSICAO_JUIZ"), ehLocal));
				
				if (rs.getLong("CODG_SERVENTIA") > 0) {
					processo.setIdServentia(rs.getLong("CODG_SERVENTIA"));
					processo.setServentia(obtenhaDescricaoDaServentia(rs.getLong("CODG_SERVENTIA")));
				}
				
				if (rs.getLong("CODG_CLASSE") > 0)
					processo.setClasse(obtenhaDescricaoDaClasse(rs.getLong("CODG_CLASSE")));
					
				if (rs.getLong("CODG_FASE") > 0)				
					processo.setFase(obtenhaDescricaoDaFase(rs.getLong("CODG_FASE")));
				
				libereResultset(rs);
				
				rs = this.Conexao.prepareStatement(obtenhaConsultaSQLParte(processo.getNumeroProcessoSPG(), ehLocal)).executeQuery();
				
				while (rs.next()) {					
					//Adiciona parte a lista correspondente					
					PoloSPGDt poloDt = new PoloSPGDt();
					
					poloDt.setId(rs.getString("NUMR_ISN"));
					
					poloDt.setNome(rs.getString("NOME_PESSOA"));
					poloDt.setFiliacao(rs.getString("NOME_MAE"));
					
					if (rs.getString("NUMR_CPF_CGC") != null)
					{
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
	
	private String obtenhaConsultaSQLProcessoLocal(NumeroProcessoDt numeroProcessoCompletoDt, String numeroSPG){
		return obtenhaConsultaSQLProcesso("V_SPGAPROCESSOS", numeroProcessoCompletoDt, numeroSPG);
	}
	
	private String obtenhaConsultaSQLProcessoRemoto(NumeroProcessoDt numeroProcessoCompletoDt, String numeroSPG){
		return obtenhaConsultaSQLProcesso("V_SPGAPROCESSOS_REM", numeroProcessoCompletoDt, numeroSPG);
	}
	
	private String obtenhaConsultaSQLProcesso(String nomeTabela, NumeroProcessoDt numeroProcessoCompletoDt, String numeroSPG){
		StringBuffer consultaSQL = new StringBuffer();	
		
		consultaSQL.append(" SELECT NUMR_TCO ");
		consultaSQL.append(" ,CODG_SERVENTIA ");
		consultaSQL.append(" ,CODG_CLASSE ");
		consultaSQL.append(" ,CODG_FASE ");
		consultaSQL.append(" ,DATA_DISTRIBUICAO ");
		consultaSQL.append(" ,INFO_SIGILOSO ");
		consultaSQL.append(" ,DATA_BAIXA ");
		consultaSQL.append(" ,INFO_POSICAO_JUIZ ");
		consultaSQL.append(" ,CODG_JUIZ_ESPECIAL ");
		consultaSQL.append(" ,NUMR_PROCESSO ");
		consultaSQL.append(" ,NUMR_PROCESSO_CNJ ");
		consultaSQL.append(" ,INFO_ANO_PROCESSO_CNJ ");
		consultaSQL.append(" ,CODG_ORIGEM_PROCESSO_CNJ ");
		consultaSQL.append(" FROM ");
		consultaSQL.append(nomeTabela);
		
		
		if ( numeroProcessoCompletoDt != null && numeroProcessoCompletoDt.getNumero() != 0L && numeroProcessoCompletoDt.getAno() != 0L ) {
			consultaSQL.append(" WHERE NUMR_PROCESSO_CNJ =");
			consultaSQL.append(numeroProcessoCompletoDt.getNumero());
			consultaSQL.append(Funcoes.preencheZeros(numeroProcessoCompletoDt.getDigito(), 2));
			consultaSQL.append(" AND INFO_ANO_PROCESSO_CNJ =");
			consultaSQL.append(numeroProcessoCompletoDt.getAno());
			consultaSQL.append(" AND CODG_ORIGEM_PROCESSO_CNJ =");
			consultaSQL.append(numeroProcessoCompletoDt.getForum());	
		} else {
			if( numeroSPG != null && !numeroSPG.isEmpty() && !numeroSPG.equals("0")) {
				consultaSQL.append(" WHERE NUMR_PROCESSO = ");
				if (numeroSPG.endsWith("0000")) {
					consultaSQL.append(Funcoes.completarZeros(numeroSPG, 16));	
				} else  {
					consultaSQL.append(Funcoes.completarZeros(numeroSPG, 12) + "0000");	
				}
			}
			else {
				return null;
			}
		}
			
		return consultaSQL.toString();
	}
	
	private String obtenhaNomeDoMagistrado(long codigoDoMagistrado, boolean ehLocal) throws SQLException{
		ResultSet rs = null;
		
		try {			
			rs = this.Conexao.prepareStatement(obtenhaConsultaSQLMagistrado(codigoDoMagistrado, ehLocal)).executeQuery();
			
			if (rs.next()) 
				return rs.getString("NOME_MAGISTRADO");	
			
		}  finally {
			libereResultset(rs);
		}
		
		return "";
	}
	
	private String obtenhaConsultaSQLMagistrado(long codigoDoMagistrado, boolean ehLocal){
		StringBuffer consultaSQL = new StringBuffer();	
		
		consultaSQL.append(" SELECT NOME_MAGISTRADO ");
		consultaSQL.append(" FROM ");
		if (ehLocal) consultaSQL.append("V_SPGUMAGISTRADOS");
		else consultaSQL.append("V_SPGUMAGISTRADOS_REM");
		consultaSQL.append(" WHERE CODG_MAGISTRADOS =");
		consultaSQL.append(codigoDoMagistrado);
				
		return consultaSQL.toString();
	}
	
	private String obtenhaNomeDoMagistrado(long codigoServentia, long infoPosicaoJuiz, boolean ehLocal) throws SQLException{
		ResultSet rs = null;
		String codigoMagistradoPrincipalESubstituto;
		String nomeMagistrado = "";
		
		try {			
			rs = this.Conexao.prepareStatement(obtenhaConsultaSQLInfoPosicaoJuiz(codigoServentia, infoPosicaoJuiz)).executeQuery();
			
			if (rs.next()){
				codigoMagistradoPrincipalESubstituto = String.format("%08d",rs.getLong("CODG_MAGISTRADO"));
				
				long magistradoPrincipal = Funcoes.StringToLong(codigoMagistradoPrincipalESubstituto.substring(0, 3));
				long magistradoSubstituto = Funcoes.StringToLong(codigoMagistradoPrincipalESubstituto.substring(4, 7));
				
				if (magistradoPrincipal > 0){
					nomeMagistrado = obtenhaNomeDoMagistrado(magistradoPrincipal, ehLocal);
					
					if (magistradoSubstituto > 0){
						nomeMagistrado = obtenhaNomeDoMagistrado(magistradoSubstituto, ehLocal) + " substituindo " + nomeMagistrado;
					}
				}
			}
			
		}  finally {
			libereResultset(rs);
		}
		
		return nomeMagistrado;
	}
	
	private String obtenhaDescricaoDaServentia(long codigoDaServentia) throws SQLException{
		ResultSet rs = null;
		
		try {			
			rs = this.Conexao.prepareStatement(obtenhaConsultaSQLServentia(codigoDaServentia)).executeQuery();
			
			if (rs.next()) 
				return rs.getString("NOME_ESCRIVANIA");	
			
		}  finally {
			libereResultset(rs);
		}
		
		return "";
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
	
	private String obtenhaConsultaSQLServentia(long codigoDaServentia){
		StringBuffer consultaSQL = new StringBuffer();	
		
		consultaSQL.append(" SELECT NOME_ESCRIVANIA ");
		consultaSQL.append(" FROM V_SPGUSERVENTIAS ");		
		consultaSQL.append(" WHERE CODG_ESCRIVANIA =");
		consultaSQL.append(codigoDaServentia);		
				
		return consultaSQL.toString();
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
	
	private String obtenhaDescricaoDaClasse(long codigoDaClasse) throws SQLException{
		ResultSet rs = null;
		
		try {			
			rs = this.Conexao.prepareStatement(obtenhaConsultaSQLClasse(codigoDaClasse)).executeQuery();
			
			if (rs.next()) 
				return rs.getString("DESC_CLASSE");	
			
		}  finally {
			libereResultset(rs);
		}
		
		return "";
	}
	
	private String obtenhaConsultaSQLClasse(long codigoDaClasse){
		StringBuffer consultaSQL = new StringBuffer();	
		
		consultaSQL.append(" SELECT DESC_CLASSE ");
		consultaSQL.append(" FROM V_SPGUCLASSES ");		
		consultaSQL.append(" WHERE CODG_CLASSE =");
		consultaSQL.append(codigoDaClasse);		
				
		return consultaSQL.toString();
	}
	
	private String obtenhaDescricaoDaFase(long codigoDaFase) throws SQLException{
		ResultSet rs = null;
		
		try {			
			rs = this.Conexao.prepareStatement(obtenhaConsultaSQLFase(codigoDaFase)).executeQuery();
			
			if (rs.next()) 
				return rs.getString("DESC_FASE");	
			
		}  finally {
			libereResultset(rs);
		}
		
		return "";
	}
	
	private String obtenhaConsultaSQLFase(long codigoDaFase){
		StringBuffer consultaSQL = new StringBuffer();	
		
		consultaSQL.append(" SELECT DESC_FASE ");
		consultaSQL.append(" FROM V_SPGUFASES ");		
		consultaSQL.append(" WHERE CODG_FASE =");
		consultaSQL.append(codigoDaFase);		
				
		return consultaSQL.toString();
	}
	
	private String obtenhaConsultaSQLInfoPosicaoJuiz(long codigoDaServentia, long infoPosicaoJuiz){
		StringBuffer consultaSQL = new StringBuffer();	
		
		consultaSQL.append(" SELECT M.CODG_MAGISTRADO "); //4 PRIMEIROS DIGITOS JUIZ PRINCIPAL/4 RESTANTES JUIZ SUBSTITUTO
		consultaSQL.append(" FROM V_SPGUSERVENTIAS S, V_INFOMAGISTRADOS M ");		
		consultaSQL.append(" WHERE S.ISN_SPGU_SERVENTIAS = M.ISN_SPGU_SERVENTIAS ");
		consultaSQL.append(" AND S.CODG_ESCRIVANIA =");
		consultaSQL.append(codigoDaServentia);
		consultaSQL.append(" AND M.INFO_POSICAO ="); //M.INFO_POSICAO_JUIZ
		consultaSQL.append(infoPosicaoJuiz);
				
		return consultaSQL.toString();
	}
		
	private String obtenhaConsultaSQLParte(String numeroProcessoSPG, boolean ehLocal){
		StringBuffer consultaSQL = new StringBuffer();	
		
		consultaSQL.append(" SELECT PT.NUMR_ISN, PT.TIPO_PARTE, PS.NOME_PESSOA, PS.NOME_MAE, PS.DATA_NASC, PS.NUMR_CPF_CGC ");
		consultaSQL.append(" FROM ");
		if (ehLocal) consultaSQL.append(" V_SPGAPARTES ");
		else consultaSQL.append(" V_SPGAPARTES_REM ");		
		consultaSQL.append(" PT, V_SPGAPESSOAS PS ");
		consultaSQL.append(" WHERE PT.NUMR_ISN = PS.NUMR_ISN ");	
		consultaSQL.append(" AND PT.NUMR_PROCESSO =");
		consultaSQL.append(numeroProcessoSPG);
		
		return consultaSQL.toString();
	}
	
	public ProcessoParteEnderecoSPGDt obtenhaEnderecoParte(String isnParte, boolean isLocal) throws SQLException {
		ProcessoParteEnderecoSPGDt dados = null;
		
		ResultSet rs = null;
		
		try {			
			rs = this.Conexao.prepareStatement(obtenhaConsultaSQLEnderecoParte(isnParte, isLocal)).executeQuery();
			
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
	
	private String obtenhaConsultaSQLEnderecoParte(String isnParte, boolean isLocal){
		StringBuffer consultaSQL = new StringBuffer();	
		
		consultaSQL.append(" SELECT IE.INFO_ENDER_COUNT, IE.TIPO_ENDER, IE.INFO_RUA, IE.INFO_NUMERO, IE.INFO_LOTE, IE.INFO_QUADRA, IE.INFO_COMPLEMENTO, IE.NUMR_CEP, BR.NOME_BAIRRO, MU.NOME_MUNIC ");
		consultaSQL.append(" FROM V_SPGAPESSOAS PS, V_SPGAPESSOAS_INFO_ENDER IE, ");
		if (isLocal) consultaSQL.append(" V_SPGUBAIRROS BR, V_SPGUMUNICIPIOS MU ");
		else consultaSQL.append(" V_SPGUBAIRROS_REM BR, V_SPGUMUNICIPIOS_REM MU ");
		consultaSQL.append(" WHERE PS.NUMR_ISN = " + isnParte);
		consultaSQL.append(" AND PS.NUMR_ISN = IE.ISN_SPGA_PESSOAS ");
		if (isLocal) consultaSQL.append(" AND IE.CODG_BAIRRO = BR.ISN_SPGU_BAIRROS AND IE.CODG_MUNICIPIO = MU.ISN_SPGU_MUNICIPIOS ");
		else consultaSQL.append(" AND IE.CODG_BAIRRO = BR.ISN_SPGU_BAIRROS_REM AND IE.CODG_MUNICIPIO = MU.ISN_SPGU_MUNICIPIOS_REM ");		
		consultaSQL.append(" ORDER BY IE.TIPO_ENDER");
				
		return consultaSQL.toString();
	}
	
	public void retireDataHibrido(NumeroProcessoDt numeroProcessoCompletoDt) throws Exception{
		if (retireDataHibridoBaseLocal(numeroProcessoCompletoDt) == 0) {
			retireDataHibridoBaseRemota(numeroProcessoCompletoDt);
		}
	}
	
	private int retireDataHibridoBaseLocal(NumeroProcessoDt numeroProcessoCompletoDt) throws Exception{
		return retireDataHibrido("V_SPGAPROCESSOS", numeroProcessoCompletoDt);
	}
	
	private int retireDataHibridoBaseRemota(NumeroProcessoDt numeroProcessoCompletoDt) throws Exception{
		return retireDataHibrido("V_SPGAPROCESSOS_REM", numeroProcessoCompletoDt);
	}
	
	private int retireDataHibrido(String nomeTabela, NumeroProcessoDt numeroProcessoCompletoDt) throws Exception {
		StringBuffer consultaSQL = new StringBuffer();	
		
		consultaSQL.append(" UPDATE ");
		consultaSQL.append(nomeTabela);
		consultaSQL.append(" SET DATA_HIBRIDO = 0 ");		
		consultaSQL.append(" WHERE NUMR_PROCESSO_CNJ =");
		consultaSQL.append(numeroProcessoCompletoDt.getNumero());
		consultaSQL.append(Funcoes.preencheZeros(numeroProcessoCompletoDt.getDigito(), 2));
		consultaSQL.append(" AND INFO_ANO_PROCESSO_CNJ =");
		consultaSQL.append(numeroProcessoCompletoDt.getAno());
		consultaSQL.append(" AND CODG_ORIGEM_PROCESSO_CNJ =");
		consultaSQL.append(numeroProcessoCompletoDt.getForum());
		
		return executarComando(consultaSQL.toString());
	}
	
	public void insereDataHibrido(NumeroProcessoDt numeroProcessoCompletoDt) throws Exception{
		if (insereDataHibridoBaseLocal(numeroProcessoCompletoDt) == 0) {
			insereDataHibridoBaseRemota(numeroProcessoCompletoDt);
		}
	}
	
	private int insereDataHibridoBaseLocal(NumeroProcessoDt numeroProcessoCompletoDt) throws Exception{
		return insereDataHibrido("V_SPGAPROCESSOS", numeroProcessoCompletoDt);
	}
	
	private int insereDataHibridoBaseRemota(NumeroProcessoDt numeroProcessoCompletoDt) throws Exception{
		return insereDataHibrido("V_SPGAPROCESSOS_REM", numeroProcessoCompletoDt);
	}
	
	private int insereDataHibrido(String nomeTabela, NumeroProcessoDt numeroProcessoCompletoDt) throws Exception {
		StringBuffer consultaSQL = new StringBuffer();	
		
		consultaSQL.append(" UPDATE ");
		consultaSQL.append(nomeTabela);
		consultaSQL.append(" SET DATA_HIBRIDO = ");
		consultaSQL.append(Funcoes.BancoData(new Date()));
		consultaSQL.append(" WHERE NUMR_PROCESSO_CNJ =");
		consultaSQL.append(numeroProcessoCompletoDt.getNumero());
		consultaSQL.append(Funcoes.preencheZeros(numeroProcessoCompletoDt.getDigito(), 2));
		consultaSQL.append(" AND INFO_ANO_PROCESSO_CNJ =");
		consultaSQL.append(numeroProcessoCompletoDt.getAno());
		consultaSQL.append(" AND CODG_ORIGEM_PROCESSO_CNJ =");
		consultaSQL.append(numeroProcessoCompletoDt.getForum());
		
		return executarComando(consultaSQL.toString());
	}
}