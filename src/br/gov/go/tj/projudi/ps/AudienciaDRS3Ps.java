package br.gov.go.tj.projudi.ps;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.AudienciaDRSDt;
import br.gov.go.tj.projudi.dt.NumeroProcessoDt;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import br.gov.go.tj.utils.TJDataHora;

public class AudienciaDRS3Ps extends Persistencia {
	
	private static final long serialVersionUID = 7119215636411054400L;
	
	public AudienciaDRS3Ps(FabricaConexao obFabricaConexao)
	{
		super.Conexao = obFabricaConexao.getConexao();
	}
	
	public List<AudienciaDRSDt> consulteListaDeAudiencias(NumeroProcessoDt numeroProcessoDt) throws Exception
	{
		List<AudienciaDRSDt> listaDeAudiencias = new ArrayList<AudienciaDRSDt>();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ResultSetTJGO rs = null;
		
		try
		{	
			ps.adicionarString(numeroProcessoDt.getNumeroCompletoProcesso());
			rs = super.consultar(obtenhaConsultaSQLAudienciasDoProcesso(), ps);
			
			while (rs.next()) 
				listaDeAudiencias.add(obtenhaAudienciaDoCursor(rs, numeroProcessoDt.getNumeroCompletoProcesso()));
			
			for(AudienciaDRSDt audiencia : listaDeAudiencias) {
				if (audiencia.getNomeArquivo() != null && audiencia.getNomeArquivo().trim().length() > 0)
					audiencia.setIndicesDepoimento(consulteIndicesDepoimentoDoVideo(numeroProcessoDt, audiencia.getDataHoraDaAudiencia()));
			}
		}
		finally
		{	
			libereResultset(rs);
		}

		return listaDeAudiencias;
	}
		
	private AudienciaDRSDt obtenhaAudienciaDoCursor(ResultSetTJGO rs, String numeroProcessoDt) throws Exception
	{
		AudienciaDRSDt AudienciaDRSDt = new AudienciaDRSDt();
		AudienciaDRSDt.setDataHoraDaAudiencia(new TJDataHora(rs.getTimestamp("EXPECTEDDATE")));
		AudienciaDRSDt.setProcessoNumero(numeroProcessoDt);		
		AudienciaDRSDt.setIdAudiencia(rs.getLong("ID"));
		
		if (rs.getString("FILES") != null)
		{
			String tokenNomeArquivo[] = rs.getString("FILES").split(",");
			if (tokenNomeArquivo.length > 0 && tokenNomeArquivo[0] != null)
			{
				tokenNomeArquivo = tokenNomeArquivo[0].split(":");
				if (tokenNomeArquivo.length > 1 && tokenNomeArquivo[1] != null)
				{	
					String nomeArquivo = tokenNomeArquivo[1];
					
					if (nomeArquivo.indexOf("\"") >= 0)
						nomeArquivo = nomeArquivo.replaceAll("\"", "");
					
					AudienciaDRSDt.setNomeArquivo(nomeArquivo);
				}
			}
		}
		
		return AudienciaDRSDt;
	}
	
	private String obtenhaConsultaSQLAudienciasDoProcesso()
	{
		StringBuffer consultaSQL = new StringBuffer();	
		
		consultaSQL.append(" SELECT DISTINCT ID, EXPECTEDDATE, FILES ");
		consultaSQL.append(" FROM HEARING ");
		consultaSQL.append(" WHERE PROCESSNUMBER = ? ");
		consultaSQL.append(" AND ACTIVE = 1 ");
		consultaSQL.append(" AND STATUS = 'P' ");
		consultaSQL.append(" ORDER BY EXPECTEDDATE ");
		
		return consultaSQL.toString();
	}
	
	private String consulteIndicesDepoimentoDoVideo(NumeroProcessoDt numeroProcessoCompletoDt, TJDataHora dataHoraAudiencia) throws Exception
	{
		String Indexes = "";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ResultSetTJGO rs = null;
		
		try
		{	
			ps.adicionarString(numeroProcessoCompletoDt.getNumeroCompletoProcesso());
			ps.adicionarDateTime(dataHoraAudiencia);
			rs = super.consultar(obtenhaConsultaSQLIndicesDepoimentoDoVideo(), ps);
			
			while (rs.next()) {
				double offSetMiliSegundos = rs.getDouble("Offset") / 10000;				
                String att = rs.getString("att");
                String role = rs.getString("role");
                String subject = rs.getString("Subject");                
                String secs = new DecimalFormat("0.000").format(offSetMiliSegundos / 1000).replace(",", ".");
                long horas = 0;
                long minutos = 0;
                long segundos = 0;
                if (offSetMiliSegundos > 0) {
                	long offSetSegundos = (long) (offSetMiliSegundos / 1000);
                	horas = (offSetSegundos  / (60 * 60));                	
                	offSetSegundos -= horas * 60 * 60;
                	
                	minutos = (offSetSegundos  / 60);
                	offSetSegundos -= minutos * 60;
                	
                	segundos = offSetSegundos;
                } 
                String time = new DecimalFormat("00").format(horas) + ":" + new DecimalFormat("00").format(minutos) + ":" + new DecimalFormat("00").format(segundos);
                if (role != null && role.trim().length() > 0)
                {
                    att = att + " (" + role + ")";
                }
                Indexes += "<a href=\"javascript:play2(" + secs + ")\" ><b>" + time + " - " + att + " </b>" + subject + "</a>";
			}
		}
		finally
		{	
			libereResultset(rs);
		}

		return Indexes;
	}
	
	private String obtenhaConsultaSQLIndicesDepoimentoDoVideo()
	{
		StringBuffer consultaSQL = new StringBuffer();	
		
		consultaSQL.append(" SELECT i.Offset, COALESCE(COALESCE(a.Name, x.Name), 'Sem Orador') AS att  ,r.Name AS role  ,i.Subject ");
		consultaSQL.append(" FROM  Hearing h  INNER JOIN HearingIndex i ON h.Id = i.HearingId ");
		consultaSQL.append(" LEFT JOIN HearingAttendant x ON i.AttendantId = x.Id ");
		consultaSQL.append(" LEFT JOIN Attendant a ON x.AttendantId = a.Id ");
		consultaSQL.append(" LEFT JOIN AttendantRole r ON x.RoleId = r.Id ");
		consultaSQL.append(" WHERE  h.ProcessNumber=? ");
		consultaSQL.append(" AND h.ExpectedDate=? ");
		consultaSQL.append(" ORDER BY  i.Offset ");		
		
		return consultaSQL.toString();
	}
}
