package br.gov.go.tj.projudi.ps;

import java.sql.SQLException;

import br.gov.go.tj.projudi.dt.AudienciaAnexoDt;
import br.gov.go.tj.projudi.dt.AudienciaDRSDt;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class AudienciaAnexoPs extends Persistencia {
	
	private static final long serialVersionUID = 2972408313625767472L;

	public AudienciaAnexoPs(FabricaConexao obFabricaConexao)
	{
		super.Conexao = obFabricaConexao.getConexao();
	}
	
	public void consulteListaAnexosDeAudiencias(AudienciaDRSDt audiencia) throws Exception
	{
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ResultSetTJGO rs = null;
		
		try
		{	
			ps.adicionarLong(audiencia.getIdAudiencia());
			rs = super.consultar(obtenhaConsultaSQLAnexosDaAudienciaDoProcesso(), ps);
			
			while (rs.next()) 
				audiencia.AdicioneAnexo(obtenhaAnexoDaAudienciaDoCursor(rs, audiencia));
		}
		finally
		{	
			libereResultset(rs);
		}
	}
		
	private AudienciaAnexoDt obtenhaAnexoDaAudienciaDoCursor(ResultSetTJGO rs, AudienciaDRSDt audiencia) throws Exception
	{
		AudienciaAnexoDt anexo = new AudienciaAnexoDt(audiencia.getProcessoNumero(), audiencia.getDataHoraDaAudiencia());
		anexo.setId(rs.getString("ID"));
		anexo.setDisplayNomeArquivo(rs.getString("DISPLAYNAME"));
		anexo.setNomeArquivo(rs.getString("FILENAME"));		
		return anexo;
	}
	
	private String obtenhaConsultaSQLAnexosDaAudienciaDoProcesso()
	{
		StringBuffer consultaSQL = new StringBuffer();	
		
		consultaSQL.append(" SELECT DISTINCT ID, DISPLAYNAME, FILENAME ");
		consultaSQL.append(" FROM HEARINGATTACHMENT ");
		consultaSQL.append(" WHERE HEARINGID = ? ");
		//consultaSQL.append(" AND STATUS = 'R' ");
		consultaSQL.append(" ORDER BY ID ");
		
		return consultaSQL.toString();
	}
}
