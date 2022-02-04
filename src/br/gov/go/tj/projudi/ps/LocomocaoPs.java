package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.CustaDt;
import br.gov.go.tj.projudi.dt.GuiaItemDt;
import br.gov.go.tj.projudi.dt.GuiaStatusDt;
import br.gov.go.tj.projudi.dt.LocomocaoDt;
import br.gov.go.tj.projudi.dt.LocomocaoSPGDt;
import br.gov.go.tj.projudi.dt.MandadoJudicialDt;
import br.gov.go.tj.projudi.dt.RegiaoDt;
import br.gov.go.tj.projudi.dt.ZonaDt;
import br.gov.go.tj.projudi.ne.BairroNe;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class LocomocaoPs extends Persistencia {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7266533816720988832L;
	
	
	public LocomocaoPs(Connection conexao){		
		Conexao = conexao;
	}
	
	/**
	 * Inclusão de locomoção.
	 * @param dados
	 * @throws Exception
	 */
	public void inserir(LocomocaoDt dados) throws Exception { 

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO projudi.LOCOMOCAO ("; 

		stSqlValores += " Values (";
		
		if ((dados.getGuiaItemDt() != null && dados.getGuiaItemDt().getId() != null && dados.getGuiaItemDt().getId().length()>0)) {
			 stSqlCampos += stVirgula + "ID_GUIA_ITEM " ;
			 stSqlValores += stVirgula + "? " ;
			 ps.adicionarLong(dados.getGuiaItemDt().getId());  

			stVirgula=",";
		}
		
		if ((dados.getBairroDt() != null && dados.getBairroDt().getId() != null && dados.getBairroDt().getId().length()>0)) {
			 stSqlCampos += stVirgula + "ID_BAIRRO " ;
			 stSqlValores += stVirgula + "? " ;
			 ps.adicionarLong(dados.getBairroDt().getId());  

			stVirgula=",";
		}
		
		if ((dados.getZonaDt() != null && dados.getZonaDt().getId() != null && dados.getZonaDt().getId().length()>0)) {
			 stSqlCampos += stVirgula + "ID_ZONA " ;
			 stSqlValores += stVirgula + "? " ;
			 ps.adicionarLong(dados.getZonaDt().getId());  

			stVirgula=",";
		}
		
		if ((dados.getRegiaoDt() != null && dados.getRegiaoDt().getId() != null && dados.getRegiaoDt().getId().length()>0)) {
			 stSqlCampos += stVirgula + "ID_REGIAO " ;
			 stSqlValores += stVirgula + "? " ;
			 ps.adicionarLong(dados.getRegiaoDt().getId());  

			stVirgula=",";
		}
		
		if ((dados.getMandadoJudicialDt() != null && dados.getMandadoJudicialDt().getId() != null && dados.getMandadoJudicialDt().getId().length()>0)) {
			 stSqlCampos += stVirgula + "ID_MAND_JUD " ;
			 stSqlValores += stVirgula + "? " ;
			 ps.adicionarLong(dados.getMandadoJudicialDt().getId());  

			stVirgula=",";
		}
		
		if ((dados.getId_GuiaEmissaoComplementar() != null && dados.getId_GuiaEmissaoComplementar().trim().length()>0)) {
			 stSqlCampos += stVirgula + "ID_GUIA_EMIS_COMP " ;
			 stSqlValores += stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_GuiaEmissaoComplementar());  

			stVirgula=",";
		}
		
		if ((dados.getGuiaItemContaVinculadaDt() != null && dados.getGuiaItemContaVinculadaDt().getId() != null && dados.getGuiaItemContaVinculadaDt().getId().length()>0)) {
			 stSqlCampos += stVirgula + "ID_GUIA_ITEM_C_VINC " ;
			 stSqlValores += stVirgula + "? " ;
			 ps.adicionarLong(dados.getGuiaItemContaVinculadaDt().getId());  

			stVirgula=",";
		}
		
		if ((dados.getGuiaItemSegundoDt() != null && dados.getGuiaItemSegundoDt().getId() != null && dados.getGuiaItemSegundoDt().getId().length()>0)) {
			 stSqlCampos += stVirgula + "ID_GUIA_ITEM2 " ;
			 stSqlValores += stVirgula + "? " ;
			 ps.adicionarLong(dados.getGuiaItemSegundoDt().getId());  

			stVirgula=",";
		}
		
		if ((dados.getGuiaItemTerceiroDt() != null && dados.getGuiaItemTerceiroDt().getId() != null && dados.getGuiaItemTerceiroDt().getId().length()>0)) {
			 stSqlCampos += stVirgula + "ID_GUIA_ITEM3 " ;
			 stSqlValores += stVirgula + "? " ;
			 ps.adicionarLong(dados.getGuiaItemTerceiroDt().getId());  

			stVirgula=",";
		}
		
		if ((dados.getCodigoTemp() != null && dados.getCodigoTemp().length()>0)) {
			 stSqlCampos += stVirgula + "CODIGO_TEMP " ;
			 stSqlValores += stVirgula + "? " ;
			 ps.adicionarLong(dados.getCodigoTemp());  

			stVirgula=",";
		}
		
		if ((dados.getCodigoOficialSPG() != null && dados.getCodigoOficialSPG().length()>0)) {
			 stSqlCampos += stVirgula + "CODIGO_OFICIAL_SPG " ;
			 stSqlValores += stVirgula + "? " ;
			 ps.adicionarLong(dados.getCodigoOficialSPG());  

			stVirgula=",";
		}
		
		stSqlCampos += stVirgula + "QUANTIDADE_ACRESCIMO " ;
		stSqlValores += stVirgula + "? " ;
		ps.adicionarLong(dados.getQuantidadeAcrescimo());
		stVirgula=",";
		
		stSqlCampos += stVirgula + "CITACAO_HORA_CERTA " ;
		stSqlValores += stVirgula + "? " ;
		ps.adicionarBoolean(dados.isCitacaoHoraCerta());
		
		stSqlCampos += stVirgula + "FORA_HORARIO_NORMAL " ;
		stSqlValores += stVirgula + "? " ;
		ps.adicionarBoolean(dados.isForaHorarioNormal());
		
		if (dados.getFinalidadeCodigo() > 0) {
			stSqlCampos += stVirgula + "FINALIDADE_CODIGO " ;
			stSqlValores += stVirgula + "? " ;
			ps.adicionarLong(dados.getFinalidadeCodigo());			
		}
		
		stSqlCampos += stVirgula + "PENHORA " ;
		stSqlValores += stVirgula + "? " ;
		ps.adicionarBoolean(dados.isPenhora());
		
		stSqlCampos += stVirgula + "INTIMACAO " ;
		stSqlValores += stVirgula + "? " ;
		ps.adicionarBoolean(dados.isIntimacao());
		
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 

		dados.setId(executarInsert(stSql,"ID_LOCOMOCAO",ps)); 
	}
	
	/**
	 * Alteração da locomoção.
	 * @param dados
	 * @throws Exception
	 */
	public void alterar(LocomocaoDt dados) throws Exception{ 

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";
		String stVirgula="";

		stSql= "UPDATE PROJUDI.LOCOMOCAO SET  ";
		
		if ((dados.getGuiaItemDt() != null && dados.getGuiaItemDt().getId() != null && dados.getGuiaItemDt().getId().length()>0)) {
			stSql+= stVirgula + " ID_GUIA_ITEM = ?";		 
			
			ps.adicionarLong(dados.getGuiaItemDt().getId());
			
			stVirgula=",";
		}
		
		if ((dados.getBairroDt() != null && dados.getBairroDt().getId() != null && dados.getBairroDt().getId().length()>0)) {
			stSql+= stVirgula + " ID_BAIRRO = ?";
			 
			ps.adicionarLong(dados.getBairroDt().getId());  

			stVirgula=",";
		
		}
		
		if ((dados.getZonaDt() != null && dados.getZonaDt().getId() != null && dados.getZonaDt().getId().length()>0)) {
			stSql+= stVirgula + " ID_ZONA = ?";
			 
			ps.adicionarLong(dados.getZonaDt().getId());  

			stVirgula=",";
		}
		
		if ((dados.getRegiaoDt() != null && dados.getRegiaoDt().getId() != null && dados.getRegiaoDt().getId().length()>0)) {
			stSql+= stVirgula + " ID_REGIAO = ?";
			 
			ps.adicionarLong(dados.getRegiaoDt().getId());  

			stVirgula=",";
		}
		
		if ((dados.getMandadoJudicialDt() != null && dados.getMandadoJudicialDt().getId() != null && dados.getMandadoJudicialDt().getId().length()>0)) {
			stSql+= stVirgula + " ID_MAND_JUD = ?";
			 
			ps.adicionarLong(dados.getMandadoJudicialDt().getId());  

			stVirgula=",";
		}
		
		stSql+= stVirgula + " ID_GUIA_EMIS_COMP = ?";
		if ((dados.getId_GuiaEmissaoComplementar() != null && dados.getId_GuiaEmissaoComplementar().trim().length()>0)) 
			ps.adicionarLong(dados.getId_GuiaEmissaoComplementar());
		else
			ps.adicionarLongNull();		
		stVirgula=",";
		
		if ((dados.getGuiaItemContaVinculadaDt() != null && dados.getGuiaItemContaVinculadaDt().getId() != null && dados.getGuiaItemContaVinculadaDt().getId().length()>0)) {
			stSql+= stVirgula + " ID_GUIA_ITEM_C_VINC = ?";
			
			ps.adicionarLong(dados.getGuiaItemContaVinculadaDt().getId());  

			stVirgula=",";
		}
		
		if ((dados.getGuiaItemSegundoDt() != null && dados.getGuiaItemSegundoDt().getId() != null && dados.getGuiaItemSegundoDt().getId().length()>0)) {
			stSql+= stVirgula + " ID_GUIA_ITEM2 = ?";			
			
			ps.adicionarLong(dados.getGuiaItemSegundoDt().getId());  

			stVirgula=",";
		}
		
		if ((dados.getGuiaItemTerceiroDt() != null && dados.getGuiaItemTerceiroDt().getId() != null && dados.getGuiaItemTerceiroDt().getId().length()>0)) {
			stSql+= stVirgula + " ID_GUIA_ITEM3 = ?";
			
			ps.adicionarLong(dados.getGuiaItemTerceiroDt().getId());  

			stVirgula=",";
		}
		
		if ((dados.getCodigoTemp() != null && dados.getCodigoTemp().length()>0)) {
			stSql+= stVirgula + " CODIGO_TEMP = ?";
			
			ps.adicionarLong(dados.getCodigoTemp());  

			stVirgula=",";
		}
		
		if ((dados.getCodigoOficialSPG() != null && dados.getCodigoOficialSPG().length()>0)) {
			stSql+= stVirgula + " CODIGO_OFICIAL_SPG = ?";
			
			ps.adicionarLong(dados.getCodigoTemp());  

			stVirgula=",";
		}
		
		stSql+= stVirgula + " QUANTIDADE_ACRESCIMO = ?";
		ps.adicionarLong(dados.getQuantidadeAcrescimo());
		stVirgula=",";
		
		stSql+= stVirgula + " CITACAO_HORA_CERTA = ?";
		ps.adicionarBoolean(dados.isCitacaoHoraCerta());
		
		stSql+= stVirgula + " FORA_HORARIO_NORMAL = ?";
		ps.adicionarBoolean(dados.isForaHorarioNormal());
		
		if (dados.getFinalidadeCodigo() > 0) {
			stSql+= stVirgula + " FINALIDADE_CODIGO = ?";
			ps.adicionarLong(dados.getFinalidadeCodigo());			
		}
		
		stSql+= stVirgula + " PENHORA = ?";
		ps.adicionarBoolean(dados.isPenhora());
		
		stSql+= stVirgula + " INTIMACAO = ?";
		ps.adicionarBoolean(dados.isIntimacao());
		
		stSql += " WHERE ID_LOCOMOCAO = ? "; 		
		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);	
	} 
	
	/**
	 * Excluir locomoção.
	 * @param chave
	 * @throws Exception
	 */
	public void excluir( String chave) throws Exception { 

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "DELETE FROM projudi.LOCOMOCAO";
		stSql += " WHERE ID_LOCOMOCAO = ?";
		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);
	}
	
	/**
	 * Consultar locomoção pelo id.
	 * @param id_locomocao
	 * @return
	 * @throws Exception
	 */
	public LocomocaoDt consultarId(String id_locomocao )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		LocomocaoDt Dados=null;

		stSql= "SELECT * FROM projudi.VIEW_LOCOMOCAO WHERE ID_LOCOMOCAO = ?";
		ps.adicionarLong(id_locomocao);
		
		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new LocomocaoDt();
				associarDt(Dados, rs1);
			}		
		}
		finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
		return Dados; 
	}
	
	protected void associarDt( LocomocaoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_LOCOMOCAO"));
		
		if (rs.getString("ID_GUIA_ITEM") != null && rs.getString("ID_GUIA_ITEM").trim().length() > 0) {
			GuiaItemDt guiaItemDt = new GuiaItemDt();
			guiaItemDt.setId(rs.getString("ID_GUIA_ITEM"));			
			Dados.setGuiaItemDt(guiaItemDt);	
		}
		
		if (rs.getString("ID_ZONA") != null && rs.getString("ID_ZONA").trim().length() > 0) {
			ZonaDt zonaDt = new ZonaDt();
			zonaDt.setId(rs.getString("ID_ZONA"));
			zonaDt.setZona(rs.getString("ZONA"));
			Dados.setZonaDt(zonaDt);	
		}
		
		if (rs.getString("ID_BAIRRO") != null && rs.getString("ID_BAIRRO").trim().length() > 0) {
			BairroDt bairroDt = new BairroDt();
			bairroDt.setId(rs.getString("ID_BAIRRO"));
			bairroDt.setBairro(rs.getString("BAIRRO"));
			Dados.setBairroDt(bairroDt);
		}
		
		if (rs.getString("ID_REGIAO") != null && rs.getString("ID_REGIAO").trim().length() > 0) {
			RegiaoDt regiaoDt = new RegiaoDt();
			regiaoDt.setId(rs.getString("ID_REGIAO"));
			regiaoDt.setRegiao(rs.getString("REGIAO"));
			Dados.setRegiaoDt(regiaoDt);
		}
		
		if (rs.getString("ID_MAND_JUD") != null && rs.getString("ID_MAND_JUD").trim().length() > 0) {
			MandadoJudicialDt mandadoJudicialDt = new MandadoJudicialDt();
			mandadoJudicialDt.setId(rs.getString("ID_MAND_JUD"));			
			Dados.setMandadoJudicialDt(mandadoJudicialDt);	
		}
		
		Dados.setId_GuiaEmissaoComplementar(rs.getString("ID_GUIA_EMIS_COMP"));
		
		if (rs.getString("ID_GUIA_ITEM_C_VINC") != null && rs.getString("ID_GUIA_ITEM_C_VINC").trim().length() > 0) {
			GuiaItemDt guiaItemDt = new GuiaItemDt();
			guiaItemDt.setId(rs.getString("ID_GUIA_ITEM_C_VINC"));			
			Dados.setGuiaItemContaVinculadaDt(guiaItemDt);	
		}
		
		if (rs.getString("ID_GUIA_ITEM2") != null && rs.getString("ID_GUIA_ITEM2").trim().length() > 0) {
			GuiaItemDt guiaItemDt = new GuiaItemDt();
			guiaItemDt.setId(rs.getString("ID_GUIA_ITEM2"));			
			Dados.setGuiaItemSegundoDt(guiaItemDt);	
		}
		
		if (rs.getString("ID_GUIA_ITEM3") != null && rs.getString("ID_GUIA_ITEM3").trim().length() > 0) {
			GuiaItemDt guiaItemDt = new GuiaItemDt();
			guiaItemDt.setId(rs.getString("ID_GUIA_ITEM3"));			
			Dados.setGuiaItemTerceiroDt(guiaItemDt);	
		}
		
		if (rs.getString("QUANTIDADE_ACRESCIMO") != null) {
			Dados.setQuantidadeAcrescimo(rs.getInt("QUANTIDADE_ACRESCIMO"));	
		}
		
		if (rs.getString("CITACAO_HORA_CERTA") != null) {
			Dados.setCitacaoHoraCerta(rs.getBoolean("CITACAO_HORA_CERTA"));	
		}
		
		if (rs.getString("FORA_HORARIO_NORMAL") != null) {
			Dados.setForaHorarioNormal(rs.getBoolean("FORA_HORARIO_NORMAL"));	
		}
		
		if (rs.getString("FINALIDADE_CODIGO") != null) {
			Dados.setFinalidadeCodigo(rs.getInt("FINALIDADE_CODIGO"));
		}
		
		if (rs.getString("PENHORA") != null) {
			Dados.setPenhora(rs.getBoolean("PENHORA"));	
		}
		
		if (rs.getString("INTIMACAO") != null) {
			Dados.setIntimacao(rs.getBoolean("INTIMACAO"));	
		}
		
		Dados.setCodigoTemp(rs.getString("CODIGO_TEMP"));
		Dados.setCodigoOficialSPG(rs.getString("CODIGO_OFICIAL_SPG"));
	}
	
	public List consultarDescricao(String descricao, String posicao) throws Exception {

		String stSql="";
		String stSqlFrom ="";
		String stSqlOrder ="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "SELECT *";
		stSqlFrom = " FROM projudi.VIEW_LOCOMOCAO";
		stSqlFrom += " WHERE LOCOMOCAO_CODIGO LIKE ?"; //esta coluna LOCOMOCAO_CODIGO não existe mais no banco
		stSqlOrder = " ORDER BY LOCOMOCAO_CODIGO "; //esta coluna LOCOMOCAO_CODIGO não existe mais no banco
		ps.adicionarString("%"+descricao+"%"); 

		try{

			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);

			while (rs1.next()) {
				LocomocaoDt obTemp = new LocomocaoDt();
				this.associarDt(obTemp, rs1);
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(stSql + stSqlFrom,ps);
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
		}
		finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
			try {if (rs2 != null) rs2.close();} catch (Exception e) {}
		}
		return liTemp; 
	}
	
	/**
     * Método que consulta a LocomocaoDt pelo id do GuiaItem
     * @param String idGuiaItem
     * @return LocomocaoDt
     * @throws Exception
     */
    public LocomocaoDt consultarIdGuiaItem(String idGuiaItem) throws Exception {
    	LocomocaoDt locomocaoDt = null;
    	ResultSetTJGO rs1 = null;
    	PreparedStatementTJGO ps =  new PreparedStatementTJGO();
    	String sql = "SELECT * FROM projudi.VIEW_LOCOMOCAO WHERE ID_GUIA_ITEM = ?";
    	ps.adicionarLong(idGuiaItem);
    	
    	try{
    		rs1 = this.consultar(sql, ps);
    		if(rs1.next()) {
    			locomocaoDt = new LocomocaoDt();
    			
    			this.associarDt(locomocaoDt, rs1);
    		}
    	
    	}
    	finally {
    		rs1.close();
    	}    	
    	return locomocaoDt;
    }
    
    /**
     * Método que deverá ser utilizado quando a central de mandados do projudi estiver em produção.
     * @param idProcesso
     * @param descricao
     * @param cidade
     * @param uf
     * @param posicao
     * @return
     * @throws Exception
     */
    public String consultarLocomocaoNaoUtilizadaJSON(String idProcesso, String descricao, String cidade, String uf, String zona, String posicao) throws Exception {

    	String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 5;

		Sql = "SELECT L.ID_LOCOMOCAO AS ID, B.BAIRRO AS DESCRICAO1, B.CIDADE AS DESCRICAO2, B.UF AS DESCRICAO3, Z.ZONA AS DESCRICAO4, R.REGIAO AS DESCRICAO5";
		SqlFrom = obtenhaConsultaSQLLocomocaoCompleta();
		SqlFrom += " WHERE L.ID_MAND_JUD IS NULL";
		SqlFrom += " AND L.ID_GUIA_EMIS_COMP IS NULL";
		SqlFrom += " AND L.CODIGO_OFICIAL_SPG IS NULL";
		SqlFrom += " AND GE.ID_PROC = ?";
		ps.adicionarLong(idProcesso);
		SqlFrom += " AND GE.ID_GUIA_STATUS IN (?, ?, ?, ?, ?, ?, ?) ";
		ps.adicionarLong(GuiaStatusDt.PAGO_ON_LINE);
		ps.adicionarLong(GuiaStatusDt.PAGO);		
		ps.adicionarLong(GuiaStatusDt.PAGO_COM_VALOR_INFERIOR );
		ps.adicionarLong(GuiaStatusDt.PAGO_COM_VALOR_SUPERIOR);
		ps.adicionarLong(GuiaStatusDt.CANCELADA_PAGA);
		ps.adicionarLong(GuiaStatusDt.PAGA_CANCELADA);
		ps.adicionarLong(GuiaStatusDt.PAGO_APOS_VENCIMENTO);
		SqlFrom += " AND B.BAIRRO LIKE ?";
		ps.adicionarString( descricao +"%");
		SqlFrom += " AND B.CIDADE LIKE ?";		
		ps.adicionarString( cidade +"%");
		SqlFrom += " AND B.UF LIKE ?";
		ps.adicionarString( uf +"%");
		if (zona != null && zona.trim().length() > 0) {
			SqlFrom += " AND Z.ZONA LIKE ?";
			ps.adicionarString( zona +"%");	
		}
		SqlOrder = " ORDER BY B.BAIRRO, B.UF, B.CIDADE";		
		
		try {
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			Sql = "SELECT COUNT(*) AS QUANTIDADE ";
			rs2 = consultar(Sql + SqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		} finally {
             try {if (rs1 != null) rs1.close();} catch (Exception e1) {}
             try {if (rs2 != null) rs2.close();} catch (Exception e1) {}
        }  
		return stTemp;
	}
    
    /**
     * Método que retorna os itens de locomoção não utilizados.
     * @param idProcesso
     * @param descricao
     * @param cidade
     * @param uf
     * @param boolean
     * @param boolean validaOficialVinculadoLocomocao
     * 
     * @return
     * @throws Exception
     */
    public List<LocomocaoDt> consultarLocomocaoNaoUtilizada(String idProcesso, String descricao, String cidade, String uf, String zona, boolean somenteComValores, boolean validaOficialVinculadoLocomocao) throws Exception {

    	String Sql;
		String SqlComum;
		List<LocomocaoDt> locomocoes = new ArrayList<LocomocaoDt>();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		Sql = "SELECT *";
		SqlComum = obtenhaConsultaSQLLocomocaoCompleta();
		SqlComum += " WHERE GE.ID_PROC = ?";
		ps.adicionarLong(idProcesso);
		if( validaOficialVinculadoLocomocao ) {
			SqlComum += " AND L.ID_MAND_JUD IS NULL";
			SqlComum += " AND L.ID_GUIA_EMIS_COMP IS NULL";
			SqlComum += " AND L.CODIGO_OFICIAL_SPG IS NULL";
		}
		SqlComum += " AND GE.ID_GUIA_STATUS IN (?, ?, ?, ?, ?, ?, ?) ";
		ps.adicionarLong(GuiaStatusDt.PAGO_ON_LINE);
		ps.adicionarLong(GuiaStatusDt.PAGO);		
		ps.adicionarLong(GuiaStatusDt.PAGO_COM_VALOR_INFERIOR );
		ps.adicionarLong(GuiaStatusDt.PAGO_COM_VALOR_SUPERIOR);
		ps.adicionarLong(GuiaStatusDt.CANCELADA_PAGA);
		ps.adicionarLong(GuiaStatusDt.PAGA_CANCELADA);
		ps.adicionarLong(GuiaStatusDt.PAGO_APOS_VENCIMENTO);
		if (somenteComValores) SqlComum += " AND GI.VALOR_CALCULADO > 0 ";
		SqlComum += " AND B.BAIRRO LIKE ?";
		ps.adicionarString( descricao +"%");
		SqlComum += " AND B.CIDADE LIKE ?";		
		ps.adicionarString( cidade +"%");
		SqlComum += " AND B.UF LIKE ?";
		ps.adicionarString( uf +"%");
		if (zona != null && zona.trim().length() > 0) {
			SqlComum += " AND Z.ZONA LIKE ?";
			ps.adicionarString( zona +"%");
		}		
		
		/** ********************
		 * Alteração adicionada depois de conversa com o Fernando Meirelles (11/12/2020)
		 * Após dúvidas das Iolanda, percebemos que uma locomoções pode entrar numa cadeia grande de 
		 * complementações. 
		 * Por exemplo, a guia A pode ser complementada pela guia B. A guia B, pode ser complementada pela 
		 * guia C, e assim por diante.
		 * Para facilitar as consultas do Fernando, resolvemos restringir que uma guia possa ser 
		 * Complementada somente 1 vez. Isto evitara consultas por recursividade.
		 */
		SqlComum += " AND GE.ID_GUIA_EMIS NOT IN ( ";
		SqlComum += " 	      SELECT LOC.ID_GUIA_EMIS_COMP ";
		SqlComum += " 	      FROM PROJUDI.LOCOMOCAO LOC ";
		SqlComum += " 	      INNER JOIN PROJUDI.GUIA_ITEM GI2 ON LOC.ID_GUIA_ITEM = GI2.ID_GUIA_ITEM "; 
		SqlComum += " 	      INNER JOIN PROJUDI.GUIA_EMIS G2 ON GI2.ID_GUIA_EMIS = G2.ID_GUIA_EMIS ";
		SqlComum += " 	      WHERE G2.ID_PROC = ? ";
		SqlComum += " 	      AND LOC.ID_GUIA_EMIS_COMP IS NOT NULL ";
		SqlComum += " 		  ) ";
		ps.adicionarLong(idProcesso);
		/** ************************** */
		
		SqlComum += " ORDER BY B.BAIRRO, B.UF, B.CIDADE";		
		
		try {
			rs1 = consultar(Sql + SqlComum, ps);
			while (rs1.next()) {
				LocomocaoDt obTemp = new LocomocaoDt();
				this.associarCompletoDt(obTemp, rs1);
				locomocoes.add(obTemp);
			}
		} finally {
             try {if (rs1 != null) rs1.close();} catch (Exception e1) {}            
        }  
		return locomocoes;
	}
    
    /**
	 * Método para reservar locomoção para mandado judicial.
	 * 
	 * @param String idProcesso
	 * @param String idMandadoJudicial
	 * @param String idBairro
	 * @param String quantidade
	 * @return boolean
	 * @throws Exception
	 */
    public boolean reservarLocomocaoMandadoJudicial(String idProcesso, String idBairro, String idMandadoJudicial, int quantidade) throws Exception {
    	
    	PreparedStatementTJGO ps =  new PreparedStatementTJGO();
    	
    	String update = " UPDATE PROJUDI.LOCOMOCAO ";
    	
    	update += " SET ID_MAND_JUD = ? ";
    	ps.adicionarLong(idMandadoJudicial);
    	
    	update += " WHERE ID_LOCOMOCAO IN (";
    	update += " SELECT L2.ID_LOCOMOCAO FROM PROJUDI.LOCOMOCAO L2"; 
    	update += " INNER JOIN PROJUDI.GUIA_ITEM GI ON (L2.ID_GUIA_ITEM = GI.ID_GUIA_ITEM)";
    	update += " INNER JOIN PROJUDI.GUIA_EMIS GE ON (GI.ID_GUIA_EMIS = GE.ID_GUIA_EMIS)";
    	update += " WHERE L2.ID_MAND_JUD IS NULL ";
    	update += " AND L2.ID_GUIA_EMIS_COMP IS NULL ";
    	update += " AND L2.CODIGO_OFICIAL_SPG IS NULL ";
        //////  alteração para guia saldo	
	    update += " AND GE.GUIA_SALDO_STATUS IS NULL";   ///  para verificar se a guia de locomocao
	    update += " AND GE.GUIA_SALDO_NUMERO IS NULL";   ///  compos saldo importado spg. se verdadeiro nao pode usar.
	    ////// 
    	update += " AND GE.ID_PROC = ? ";
    	ps.adicionarLong(idProcesso);
    	
    	update += " AND GE.ID_GUIA_STATUS IN (?, ?, ?, ?, ?, ?, ?) ";
		ps.adicionarLong(GuiaStatusDt.PAGO_ON_LINE);
		ps.adicionarLong(GuiaStatusDt.PAGO);		
		ps.adicionarLong(GuiaStatusDt.PAGO_COM_VALOR_INFERIOR );
		ps.adicionarLong(GuiaStatusDt.PAGO_COM_VALOR_SUPERIOR);
		ps.adicionarLong(GuiaStatusDt.CANCELADA_PAGA);
		ps.adicionarLong(GuiaStatusDt.PAGA_CANCELADA);
		ps.adicionarLong(GuiaStatusDt.PAGO_APOS_VENCIMENTO);
    	
    	update += " AND GI.VALOR_CALCULADO > 0 ";
    	
    	update += " AND L2.ID_ZONA in ( SELECT ZBR.ID_ZONA FROM PROJUDI.ZONA_BAIRRO_REGIAO ZBR WHERE ZBR.ID_BAIRRO = ? ) ";
    	ps.adicionarLong(idBairro);
    	
    	update += " AND ROWNUM <= ? )";
    	ps.adicionarLong(quantidade);
    	
    	return executarUpdateDelete(update, ps) == quantidade;
    	
    }
    
    /**
	 * Método que vincula as locomoções complementadas com o idMandadoJudicial. 
	 * Este método corrige o valor total quando uma locomoção foi complementada.
	 * 
	 * @param String idMandadoJudicial
	 * @param FabricaConexao obFabricaConexao
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
    public boolean reservarLocomocaoMandadoJudicialGuiaComplementada(String idMandadoJudicial) throws Exception {
    	
    	PreparedStatementTJGO ps =  new PreparedStatementTJGO();
    	
    	String update = " UPDATE PROJUDI.LOCOMOCAO ";
    	
    	update += " SET ID_MAND_JUD = ? ";
    	ps.adicionarLong(idMandadoJudicial);
    	
    	update += " WHERE ID_LOCOMOCAO = ( ";
    	update += " SELECT L2.ID_LOCOMOCAO FROM PROJUDI.LOCOMOCAO L2";
    	update += " INNER JOIN PROJUDI.GUIA_ITEM GI ON (L2.ID_GUIA_ITEM = GI.ID_GUIA_ITEM)";
    	update += " INNER JOIN PROJUDI.GUIA_EMIS GE ON (GI.ID_GUIA_EMIS = GE.ID_GUIA_EMIS)";
    	update += " WHERE L2.ID_MAND_JUD IS NULL ";
		update += " AND GE.GUIA_SALDO_STATUS IS NULL";
	    update += " AND GE.GUIA_SALDO_NUMERO IS NULL";		
    	
    	update += " AND GE.ID_GUIA_STATUS IN (?, ?, ?, ?, ?, ?, ?) ";
		ps.adicionarLong(GuiaStatusDt.PAGO_ON_LINE);
		ps.adicionarLong(GuiaStatusDt.PAGO);
		ps.adicionarLong(GuiaStatusDt.PAGO_COM_VALOR_INFERIOR );
		ps.adicionarLong(GuiaStatusDt.PAGO_COM_VALOR_SUPERIOR);
		ps.adicionarLong(GuiaStatusDt.CANCELADA_PAGA);
		ps.adicionarLong(GuiaStatusDt.PAGA_CANCELADA);
		ps.adicionarLong(GuiaStatusDt.PAGO_APOS_VENCIMENTO);
		
		update += " AND L2.ID_GUIA_EMIS_COMP IN ("; 
									update += " SELECT L3.ID_GUIA_EMIS FROM PROJUDI.VIEW_LOCOMOCAO L3 ";
									update += " WHERE L3.ID_MAND_JUD = ?";
 									ps.adicionarLong(idMandadoJudicial);
					update += " )";
		update += " )";		
 
		return executarUpdateDelete(update, ps) > 0;   
 
    }
    
    /**
	 * Método para liberar locomocao que está reservada para mandado judicial.
	 * @param String idMandadoJudicial
	 * @return boolean
	 * @throws Exception 
	 */
    public int liberarLocomocao(String idLocomocao) throws Exception {
    	
    	PreparedStatementTJGO ps =  new PreparedStatementTJGO();    	
     	
    	String update = "UPDATE PROJUDI.LOCOMOCAO SET ID_MAND_JUD = NULL WHERE ID_LOCOMOCAO = ?";
    	ps.adicionarLong(idLocomocao);
        return  executarUpdateDelete(update, ps);
  
    }    
    /**
	 * Método para liberar locomocao complementada que está reservada para mandado judicial.
	 * @param String idMandadoJudicial
	 * @return void
	 * @throws Exception 
	 */
    
    public void liberarLocomocaoComplementada(String idGuiaEmis) throws Exception {
   
      try {
        	PreparedStatementTJGO ps =  new PreparedStatementTJGO();      
      	   	String update = "UPDATE PROJUDI.LOCOMOCAO SET ID_MAND_JUD = NULL WHERE ID_LOCOMOCAO IN ("
      			+ " SELECT L.ID_LOCOMOCAO FROM PROJUDI.VIEW_LOCOMOCAO L"  
      			+ " WHERE L.ID_GUIA_EMIS_COMP IN (?))";
      		ps.adicionarLong(idGuiaEmis);
         	executarUpdateDelete(update, ps);
      } catch (Exception e) {
    	  throw new MensagemException("Erro ao liberar locomoção complementada");
	  }
    }    
    
    /**
     * Método que retorna os itens de locomoção utilizados na guia de locomoção complementar.
     * @param idProcesso
     * @param descricao
     * @param cidade
     * @param uf
     * @return
     * @throws Exception
     */
    public List<LocomocaoDt> consultarLocomocaoUtilizadaGuiaComplementar(String idProcesso, String idGuiaComplementar) throws Exception {

    	String Sql;
		String SqlComum;
		List<LocomocaoDt> locomocoes = new ArrayList<LocomocaoDt>();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		Sql = "SELECT *";
		SqlComum = obtenhaConsultaSQLLocomocaoCompleta();
		SqlComum += " WHERE L.ID_MAND_JUD IS NULL";
		SqlComum += " AND L.CODIGO_OFICIAL_SPG IS NULL";
		SqlComum += " AND GE.ID_PROC = ?";
		ps.adicionarLong(idProcesso);
		SqlComum += " AND GE.ID_GUIA_STATUS IN (?, ?, ?, ?, ?, ?, ?) ";
		ps.adicionarLong(GuiaStatusDt.PAGO_ON_LINE);
		ps.adicionarLong(GuiaStatusDt.PAGO);		
		ps.adicionarLong(GuiaStatusDt.PAGO_COM_VALOR_INFERIOR );
		ps.adicionarLong(GuiaStatusDt.PAGO_COM_VALOR_SUPERIOR);
		ps.adicionarLong(GuiaStatusDt.CANCELADA_PAGA);
		ps.adicionarLong(GuiaStatusDt.PAGA_CANCELADA);
		ps.adicionarLong(GuiaStatusDt.PAGO_APOS_VENCIMENTO);
		SqlComum += " AND L.ID_GUIA_EMIS_COMP = ?";
		ps.adicionarLong(idGuiaComplementar);
		SqlComum += " ORDER BY B.BAIRRO, B.UF, B.CIDADE";		
		
		try {
			rs1 = consultar(Sql + SqlComum, ps);
			while (rs1.next()) {
				LocomocaoDt obTemp = new LocomocaoDt();
				this.associarCompletoDt(obTemp, rs1);
				locomocoes.add(obTemp);
			}
		} finally {
             try {if (rs1 != null) rs1.close();} catch (Exception e1) {}            
        }  
		return locomocoes;
	}
    
    /**
	 * Consultar locomoção pelo id.
	 * @param id_locomocao
	 * @return
	 * @throws Exception
	 */
	public LocomocaoDt consultarIdCompleto(String id_locomocao)  throws Exception {

		String Sql = "";
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		LocomocaoDt Dados=null;
		
		Sql = "SELECT * ";
		Sql += obtenhaConsultaSQLLocomocaoCompleta();
		Sql += " WHERE ID_LOCOMOCAO = ?";		ps.adicionarLong(id_locomocao);
		
		try{
			rs = consultar(Sql,ps);
			if (rs.next()) {
				Dados= new LocomocaoDt();
				associarCompletoDt(Dados, rs);
			}		
		}
		finally{
			try {if (rs != null) rs.close();} catch (Exception e) {}
		}
		return Dados; 
	}
	
	private String obtenhaConsultaSQLLocomocaoCompleta() {
		String Sql;
		
		Sql = " FROM PROJUDI.VIEW_LOCOMOCAO L INNER JOIN PROJUDI.VIEW_GUIA_ITEM GI ON GI.ID_GUIA_ITEM = L.ID_GUIA_ITEM";
		Sql += " INNER JOIN PROJUDI.VIEW_CUSTA C ON C.ID_CUSTA = GI.ID_CUSTA";
		Sql += " INNER JOIN PROJUDI.VIEW_GUIA_EMIS GE ON GE.ID_GUIA_EMIS = GI.ID_GUIA_EMIS";
		Sql += " INNER JOIN PROJUDI.VIEW_GUIA_MODELO GM ON GM.ID_GUIA_MODELO = GE.ID_GUIA_MODELO";
		Sql += " INNER JOIN PROJUDI.VIEW_ZONA Z ON Z.ID_ZONA = L.ID_ZONA";
		Sql += " INNER JOIN PROJUDI.VIEW_BAIRRO B ON B.ID_BAIRRO = L.ID_BAIRRO";
		Sql += " INNER JOIN PROJUDI.VIEW_REGIAO R ON R.ID_REGIAO = L.ID_REGIAO";		
				
		return Sql;
	}
	
	protected void associarCompletoDt( LocomocaoDt Dados, ResultSetTJGO rs )  throws Exception {
		GuiaItemPs guiaItemPs = new  GuiaItemPs(super.Conexao);
		associarDt(Dados, rs);
		if (Dados.getGuiaItemDt() != null) {
			guiaItemPs.associarDt(Dados.getGuiaItemDt(), rs);
			Dados.getGuiaItemDt().setCustaDt(new CustaDt());
			new GuiaModeloPs(null).associarDt(Dados.getGuiaItemDt().getGuiaEmissaoDt().getGuiaModeloDt(), rs); //Criado somente para reaproveitar o AssociarDt
			new CustaPs(null).associarDt(Dados.getGuiaItemDt().getCustaDt(), rs); //Criado somente para reaproveitar o AssociarDt
		}
		if (Dados.getZonaDt() != null) new ZonaPs(null).associarDt(Dados.getZonaDt(), rs); //Criado somente para reaproveitar o AssociarDt
		if (Dados.getBairroDt() != null) new BairroPs(null).associarDt(Dados.getBairroDt(), rs); //Criado somente para reaproveitar o AssociarDt
		if (Dados.getRegiaoDt() != null) new RegiaoPs(null).associarDt(Dados.getRegiaoDt(), rs); //Criado somente para reaproveitar o AssociarDt
		
		if (Dados.getGuiaItemContaVinculadaDt() != null) Dados.setGuiaItemContaVinculadaDt(guiaItemPs.consultarId(Dados.getGuiaItemContaVinculadaDt().getId()));
		if (Dados.getGuiaItemSegundoDt() != null) Dados.setGuiaItemSegundoDt(guiaItemPs.consultarId(Dados.getGuiaItemSegundoDt().getId()));
		if (Dados.getGuiaItemTerceiroDt() != null) Dados.setGuiaItemTerceiroDt(guiaItemPs.consultarId(Dados.getGuiaItemTerceiroDt().getId()));
	}
	
	public List<LocomocaoDt> consultarLocomocoesUtilizadasGuiaComplementar(String idProcesso, String idGuiaComplementar) throws Exception {

    	String Sql;
		String SqlComum;
		List<LocomocaoDt> locomocoes = new ArrayList<LocomocaoDt>();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		Sql = "SELECT *";
		SqlComum = obtenhaConsultaSQLLocomocaoCompleta();
		SqlComum += " WHERE L.ID_MAND_JUD IS NULL";
		SqlComum += " AND L.CODIGO_OFICIAL_SPG IS NULL";
		SqlComum += " AND GE.ID_PROC = ?";
		ps.adicionarLong(idProcesso);
		SqlComum += " AND L.ID_GUIA_EMIS_COMP = ?";
		ps.adicionarLong(idGuiaComplementar);
		SqlComum += " ORDER BY B.BAIRRO, B.UF, B.CIDADE";		
		
		try {
			rs1 = consultar(Sql + SqlComum, ps);
			while (rs1.next()) {
				LocomocaoDt obTemp = new LocomocaoDt();
				this.associarCompletoDt(obTemp, rs1);
				locomocoes.add(obTemp);
			}
		} finally {
             try {if (rs1 != null) rs1.close();} catch (Exception e1) {}            
        }  
		return locomocoes;
	}
	
	public List<LocomocaoDt> consultarTodasLocomocoes(String idGuia) throws Exception {

    	String Sql;
		String SqlComum;
		List<LocomocaoDt> locomocoes = new ArrayList<LocomocaoDt>();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		Sql = "SELECT *";
		SqlComum = obtenhaConsultaSQLLocomocaoCompleta();
		SqlComum += " WHERE GE.ID_GUIA_EMIS = ?";
		ps.adicionarLong(idGuia);
		SqlComum += " ORDER BY B.BAIRRO, B.UF, B.CIDADE";		
		
		try {
			rs1 = consultar(Sql + SqlComum, ps);
			while (rs1.next()) {
				LocomocaoDt obTemp = new LocomocaoDt();
				this.associarCompletoDt(obTemp, rs1);
				locomocoes.add(obTemp);
			}
		} finally {
             try {if (rs1 != null) rs1.close();} catch (Exception e1) {}            
        }  
		return locomocoes;
	}
	
	/**
     * Método que consulta a LocomocaoDt pelo id do GuiaItem de itens de penhora e/ou avaliacao. 
     * @param String idGuiaItem
     * @return LocomocaoDt
     * @throws Exception
     */
    public LocomocaoDt consultarLocomocaoDeItemPenhora_Avaliacao(String idGuiaItem) throws Exception {
    	LocomocaoDt locomocaoDt = null;
    	ResultSetTJGO rs1 = null;
    	PreparedStatementTJGO ps =  new PreparedStatementTJGO();
    	String sql = "SELECT * FROM PROJUDI.VIEW_LOCOMOCAO WHERE ID_GUIA_ITEM2 = ? OR ID_GUIA_ITEM3 = ?";
    	ps.adicionarLong(idGuiaItem);
    	ps.adicionarLong(idGuiaItem);
    	
    	try{
    		rs1 = this.consultar(sql, ps);
    		if(rs1.next()) {
    			locomocaoDt = new LocomocaoDt();
    			
    			this.associarDt(locomocaoDt, rs1);
    		}
    	
    	}
    	finally {
    		rs1.close();
    	}    	
    	return locomocaoDt;
    }
    
    /**
     * Método para consultar as locomoções-mandados da view no SPG V_SPGAGUIAS_LOCOMOCOES_MANDADOS(_REM)
     * 
     * @param String isnGuia
     * @param String remoto
     * @return List<LocomocaoSPGDt>
     * @throws Exception
     */
    public List<LocomocaoSPGDt> consultarLocomocaoMandadoSPG(String isnGuia, String remoto) throws Exception {
    	List<LocomocaoSPGDt> listaLocomocaoSPGDt = null;
    	ResultSetTJGO rs1 = null;
    	PreparedStatementTJGO ps =  new PreparedStatementTJGO();
    	
    	String sql = "SELECT * FROM V_SPGAGUIAS_LOCOMOCOES_MANDADOS"+ remoto +" WHERE ISN_SPGA_GUIAS"+ remoto +" = ?";
    	
    	ps.adicionarLong(isnGuia);
    	
    	try {
    		rs1 = this.consultar(sql, ps);
    		if(rs1 != null) {
    			listaLocomocaoSPGDt = new ArrayList<LocomocaoSPGDt>();
    			
    			while( rs1.next() ) {
    				LocomocaoSPGDt locomocaoSPGDt = new LocomocaoSPGDt();
    				
    				locomocaoSPGDt.setIsnGuia(rs1.getLong("ISN_SPGA_GUIAS"+remoto));
    				locomocaoSPGDt.setNumeroMandado(rs1.getLong("NUMR_MANDADO"));
    				locomocaoSPGDt.setNumeroGuiaComplementar(rs1.getLong("NUMR_GUIA_COMPLEMENTAR"));
    				
    				listaLocomocaoSPGDt.add(locomocaoSPGDt);
    			}
    		}
    	}
    	finally {
    		try {if (rs1 != null) rs1.close();} catch (Exception e1) {}
    	}
    	
    	return listaLocomocaoSPGDt;
    }
    
    /**
     * Método para consultar as locomoções no SPG
     * 
     * @param String isnGuia
     * @param String remoto
     * @return List<LocomocaoSPGDt>
     * @throws Exception
     */
    public List<LocomocaoSPGDt> consultarLocomocaoSPG(String isnGuia, String remoto) throws Exception {
    	List<LocomocaoSPGDt> listaLocomocaoSPGDt = null;
    	ResultSetTJGO rs1 = null;
    	PreparedStatementTJGO ps =  new PreparedStatementTJGO();
    	
    	String sql = "SELECT * FROM V_SPGAGUIAS_LOCOMOCOES"+ remoto +" WHERE ISN_SPGA_GUIAS"+ remoto +" = ?";
    	
    	ps.adicionarLong(isnGuia);
    	
    	try {
    		rs1 = this.consultar(sql, ps);
    		if(rs1 != null) {
    			listaLocomocaoSPGDt = new ArrayList<LocomocaoSPGDt>();
    			
    			while( rs1.next() ) {
    				LocomocaoSPGDt locomocaoSPGDt = new LocomocaoSPGDt();
    				
    				locomocaoSPGDt.setIsnGuia(rs1.getLong("ISN_SPGA_GUIAS"+remoto));
    				locomocaoSPGDt.setCodigoBairro(rs1.getString("CODG_BAIRRO"));
    				locomocaoSPGDt.setCodigoMunicipio(rs1.getString("CODG_MUNICIPIO"));
    				locomocaoSPGDt.setQuantidade(rs1.getString("QTDE_LOCOMOCOES"));
    				
    				listaLocomocaoSPGDt.add(locomocaoSPGDt);
    			}
    		}
    	}
    	finally {
    		try {if (rs1 != null) rs1.close();} catch (Exception e1) {}
    	}
    	
    	return listaLocomocaoSPGDt;
    }
    
    /**
     * Método para consultar locomocao vinculado a um mandado.
     * 
     * @param String idMandadoJudicial
     * @return List<LocomocaoDt>
     * @throws Exception
     */
    public List<LocomocaoDt> consultarLocomocaoVinculadaMandado(String idMandadoJudicial) throws Exception {

    	String Sql;
		List<LocomocaoDt> locomocoes = new ArrayList<LocomocaoDt>();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		Sql = "SELECT * FROM PROJUDI.VIEW_LOCOMOCAO L WHERE ID_MAND_JUD = ?"
				+ " AND L.GUIA_SALDO_STATUS IS NULL AND L.GUIA_SALDO_NUMERO IS NULL"
				+ " AND L.CODIGO_OFICIAL_SPG IS NULL";
		
		ps.adicionarLong(idMandadoJudicial);
		
		try {
			rs1 = consultar(Sql, ps);
			while (rs1.next()) {
				LocomocaoDt obTemp = new LocomocaoDt();
				this.associarDt(obTemp, rs1);
				locomocoes.add(obTemp);
			}
		} finally {
             try {if (rs1 != null) rs1.close();} catch (Exception e1) {}            
        }  
		return locomocoes;
	}

        
    /**
     * Método para consultar a quantidade de locomoções reservadas e vinculadas a um mandado.
     * 
     * @param String idMandadoJudicial
     * @return int
     * @throws Exception
     * @author hrrosa
     */
    public int consultarQtdLocomocaoVinculadaMandado(String idMandadoJudicial) throws Exception {

    	String Sql;
    	int qtd = 0;
		List<LocomocaoDt> locomocoes = new ArrayList<LocomocaoDt>();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		Sql = "SELECT count(1) qtd FROM PROJUDI.LOCOMOCAO WHERE ID_MAND_JUD = ?";
		ps.adicionarLong(idMandadoJudicial);
		
		try {
			rs1 = consultar(Sql, ps);
			while (rs1.next()) {
				qtd = rs1.getInt("qtd");
			}
		} finally {
             try {if (rs1 != null) rs1.close();} catch (Exception e1) {}            
        }  
		return qtd;
	}
    
    /**
     * Método para consultar saldo de locomocoes no processo disponiveis para serem utilizadas.
     * 
     * @param String idProcesso
     * @return List<LocomocaoDt>
     * @throws Exception
     * 
     * @author fasoares
     */
    public List<LocomocaoDt> consultaLocomocoesProcessoDisponiveis(String idProcesso) throws Exception {
    	String Sql;
		List<LocomocaoDt> listaLocomocaoDt = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		//********************************************
		//SQL Original
		//Quando a central for liberada para o estado todo, basta deixar este sql
//		Sql = "SELECT COUNT(LOC.ID_BAIRRO) AS QUANTIDADE, LOC.ID_BAIRRO AS ID_BAIRRO FROM PROJUDI.VIEW_LOCOMOCAO LOC "+
//		"INNER JOIN PROJUDI.GUIA_ITEM ITEM ON ITEM.ID_GUIA_ITEM = LOC.ID_GUIA_ITEM "+
//		"INNER JOIN PROJUDI.GUIA_EMIS GUIA ON GUIA.ID_GUIA_EMIS = ITEM.ID_GUIA_EMIS "+
//		"WHERE GUIA.ID_PROC = ? "+
//		"AND LOC.ID_MAND_JUD IS NULL "+
//		"AND LOC.CODIGO_OFICIAL_SPG IS NULL "+
//		"AND LOC.ID_GUIA_EMIS_COMP IS NULL "+
//		"AND GUIA.ID_GUIA_STATUS IN (?,?,?,?,?,?,?) "+
//		"GROUP BY LOC.ID_BAIRRO "+
//		"ORDER BY LOC.ID_BAIRRO ";
		//********************************************
		
		Sql = "SELECT COUNT(LOC.ID_BAIRRO) AS QUANTIDADE, LOC.ID_BAIRRO AS ID_BAIRRO FROM PROJUDI.VIEW_LOCOMOCAO LOC "+
		"INNER JOIN PROJUDI.GUIA_ITEM ITEM ON ITEM.ID_GUIA_ITEM = LOC.ID_GUIA_ITEM "+
		"INNER JOIN PROJUDI.GUIA_EMIS GUIA ON GUIA.ID_GUIA_EMIS = ITEM.ID_GUIA_EMIS "+
		"INNER JOIN PROJUDI.PROC P ON GUIA.ID_PROC = P.ID_PROC "+
	    "INNER JOIN PROJUDI.SERV S ON P.ID_SERV = S.ID_SERV "+
	    "INNER JOIN PROJUDI.COMARCA C ON C.ID_COMARCA = S.ID_COMARCA "+ 
		"WHERE GUIA.ID_PROC = ? "+
		"AND C.COMARCA_CODIGO = ? "+
		"AND LOC.ID_MAND_JUD IS NULL "+
		"AND LOC.CODIGO_OFICIAL_SPG IS NULL "+
		"AND LOC.ID_GUIA_EMIS_COMP IS NULL "+
		"AND GUIA.ID_GUIA_STATUS IN (?,?,?,?,?,?,?) "+
		"AND GUIA.GUIA_SALDO_STATUS IS NULL "+ //Guias utilizadas na guia saldo não podem entrar
		"AND GUIA.GUIA_SALDO_NUMERO IS NULL "+ //Guias utilizadas na guia saldo não podem entrar
		//
	 	"AND GUIA.DATA_EMIS > TO_DATE('03/11/2020  11:00:00' , 'dd/mm/yyyy hh24:mi:ss') " +  // data implantacao senador canedo
		 //  mudar esse teste e fazer  pelo controle do spg. 
		"GROUP BY LOC.ID_BAIRRO "+
		"ORDER BY LOC.ID_BAIRRO ";
		
		ps.adicionarLong(idProcesso);
		
		ps.adicionarLong(ComarcaDt.SENADOR_CANEDO);
		
		ps.adicionarLong(GuiaStatusDt.PAGO_ON_LINE);
		ps.adicionarLong(GuiaStatusDt.PAGO);
		ps.adicionarLong(GuiaStatusDt.PAGO_COM_VALOR_INFERIOR );
		ps.adicionarLong(GuiaStatusDt.PAGO_COM_VALOR_SUPERIOR);
		ps.adicionarLong(GuiaStatusDt.CANCELADA_PAGA);
		ps.adicionarLong(GuiaStatusDt.PAGA_CANCELADA);
		ps.adicionarLong(GuiaStatusDt.PAGO_APOS_VENCIMENTO);
		
		try {
			rs1 = consultar(Sql, ps);
			while (rs1.next()) {
				if( listaLocomocaoDt == null ) {
					listaLocomocaoDt = new ArrayList<LocomocaoDt>();
				}
				LocomocaoDt locomocaoDt = new LocomocaoDt();
				
				locomocaoDt.setQtdLocomocao(rs1.getInt("QUANTIDADE"));
				
				locomocaoDt.setBairroDt(new BairroNe().consultarId(rs1.getString("ID_BAIRRO")));
				
				listaLocomocaoDt.add(locomocaoDt);
			}
		} finally {
             try {if (rs1 != null) rs1.close();} catch (Exception e1) {}            
        }
		
		return listaLocomocaoDt;
    }
    /**
     * Método para consultar locomocao vinculado na liberacao .
     * 
     * @param String idMandadoJudicial
     * @return List<LocomocaoDt>
     * @throws Exception
     */
    public List<LocomocaoDt> consultaLiberacaoLocomocao(String idMandadoJudicial) throws Exception {

    	String Sql;
		List<LocomocaoDt> locomocoes = new ArrayList<LocomocaoDt>();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		LocomocaoDt obTemp = null;
		
		
		Sql = "SELECT l.id_locomocao AS idLocomocao, l.id_guia_emis AS idGuiaEmis FROM PROJUDI.VIEW_LOCOMOCAO L WHERE ID_MAND_JUD = ?"
				+ " AND L.GUIA_SALDO_STATUS IS NULL AND L.GUIA_SALDO_NUMERO IS NULL"
				+ " AND L.ID_GUIA_EMIS_COMP IS NULL AND L.CODIGO_OFICIAL_SPG IS NULL";
		
		ps.adicionarLong(idMandadoJudicial);
		
		try {
			rs1 = consultar(Sql, ps);
			while (rs1.next()) {
				obTemp = new LocomocaoDt();
				obTemp.setId(rs1.getString("idLocomocao"));
				obTemp.setId_Guia_Emissao(rs1.getString("idGuiaEmis"));
				locomocoes.add(obTemp);
			}
		} finally {
             try {if (rs1 != null) rs1.close();} catch (Exception e1) {}            
        }  
		return locomocoes;
	}
    
    /**
     * Método para consultar se guia tem algum item de locomocao vinculada a mandado.
     * 
     * @param String idGuiaEmissao
     * @return boolean
     * @throws Exception
     * @author fasoares
     */
    public boolean isGuiaVinculadaLocomocaoMandado(String idGuiaEmissao) throws Exception {

		boolean retorno = false;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT ID_GUIA_EMIS FROM PROJUDI.VIEW_LOCOMOCAO ";
		sql += "WHERE ID_GUIA_EMIS = ? ";
		sql += "AND ID_MAND_JUD IS NOT NULL ";
		
		ps.adicionarLong(idGuiaEmissao);
		
		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);
			
			if( rs1 != null ) {
				while(rs1.next()) {
					if( Funcoes.StringToInt(rs1.getString("ID_GUIA_EMIS")) == Funcoes.StringToInt(idGuiaEmissao) ) {
						retorno = true;
					}
				}				
			}
		}
		finally {
			 try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return retorno;
	
	}
        
}
