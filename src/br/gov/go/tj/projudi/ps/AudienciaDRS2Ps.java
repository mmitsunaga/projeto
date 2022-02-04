package br.gov.go.tj.projudi.ps;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.AudienciaDRSDt;
import br.gov.go.tj.projudi.dt.NumeroProcessoDt;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import br.gov.go.tj.utils.TJDataHora;

public class AudienciaDRS2Ps extends Persistencia {
	
	private static final long serialVersionUID = 7119215636411054400L;
	
	public AudienciaDRS2Ps(FabricaConexao obFabricaConexao)
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
				if (audiencia.getNomeArquivo() != null && audiencia.getNomeArquivo().trim().length() > 0 && audiencia.getCodigoGravacao() > 0)
					audiencia.setIndicesDepoimento(consulteIndicesDepoimentoDoVideo(audiencia.getCodigoGravacao()));
			}
		}
		finally
		{	
			libereResultset(rs);
		}

		return listaDeAudiencias;
	}
		
	@SuppressWarnings("deprecation")
	private AudienciaDRSDt obtenhaAudienciaDoCursor(ResultSetTJGO rs, String numeroProcessoDt) throws Exception
	{
		AudienciaDRSDt AudienciaDRSDt = new AudienciaDRSDt();
		AudienciaDRSDt.setProcessoNumero(numeroProcessoDt);
		
		TJDataHora dataHoraAudiencia = new TJDataHora(rs.getDate("DATA"));
		Time horaDaAudiencia = rs.getTime("HORA");
		dataHoraAudiencia.setHora(horaDaAudiencia.getHours());
		dataHoraAudiencia.setMinuto(horaDaAudiencia.getMinutes());
		dataHoraAudiencia.setSegundo(horaDaAudiencia.getSeconds());
				
		AudienciaDRSDt.setDataHoraDaAudiencia(dataHoraAudiencia);	
		
		AudienciaDRSDt.setNomeArquivo(rs.getString("NOMEARQUIVO"));
		AudienciaDRSDt.setCodigoGravacao(rs.getInt("CODGRAVACAO"));
		
		return AudienciaDRSDt;
	}
	
	private String obtenhaConsultaSQLAudienciasDoProcesso()
	{
		StringBuffer consultaSQL = new StringBuffer();	
		
		consultaSQL.append(" SELECT g.NROPROCESSO ");
		consultaSQL.append(" ,g.DATA ");
		consultaSQL.append(" ,g.HORA ");
		consultaSQL.append(" ,a.NOMEARQUIVO ");
		consultaSQL.append(" ,g.CODGRAVACAO ");
		consultaSQL.append(" FROM DRS2X.GRAVACOES g INNER JOIN DRS2X.ARQUIVOS a ON g.CODGRAVACAO = a.CODGRAVACAO ");		
		consultaSQL.append(" WHERE g.NROPROCESSO = ?");
		consultaSQL.append(" AND NOT g.DATA IS NULL ");		
		consultaSQL.append(" AND NOT g.HORA IS NULL ");
		consultaSQL.append(" AND g.STATUSGRAVACAO = 'Publicado' ");
		consultaSQL.append(" ORDER BY g.DATA, g.HORA ");
		
		return consultaSQL.toString();
	}
	
	private String consulteIndicesDepoimentoDoVideo(int codigoGravacao) throws Exception
	{
		String Indexes = "";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ResultSetTJGO rs = null;
		
		try
		{	
			ps.adicionarLong(codigoGravacao);
			rs = super.consultar(obtenhaConsultaSQLIndicesDepoimentoDoVideo(), ps);
			
			while (rs.next()) {
				String tempo = rs.getString("TEMPO");
				int segundos = 0;
				if (tempo != null && tempo.trim().length() > 0 && tempo.contains(":")) {
					String vetorTempo[] = tempo.trim().split(":");
					if (vetorTempo.length == 3) {
						int horas = Funcoes.StringToInt(vetorTempo[0]);
						int minutos = Funcoes.StringToInt(vetorTempo[1]);
						int segundo = Funcoes.StringToInt(vetorTempo[2]);
						
						segundos = segundo + (minutos * 60) + (horas * 60 * 60);
					}
				}
				
				String nome = "";
				if (rs.getString("NOME") != null && rs.getString("NOME").length() > 0) {
					nome = rs.getString("NOME");
				}
				
				String assunto = "";
				if (rs.getString("DESC_ASSUNTO") != null && rs.getString("DESC_ASSUNTO").length() > 1) {
					assunto = rs.getString("DESC_ASSUNTO");					
				}
				
				String marcador = "";
				if (rs.getString("DESC_MARCADOR") != null && rs.getString("DESC_MARCADOR").length() > 0) {
					marcador = " (" + rs.getString("DESC_MARCADOR") + ") ";
				}
				
				Indexes += "<a href=\"javascript:play2(" + segundos + ")\" ><b>" + tempo + " - " + nome + marcador + "</b>" + assunto + "</a>";
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
		
		consultaSQL.append(" SELECT I.CODINDEX ");
		consultaSQL.append("       ,O.NOME");
		consultaSQL.append("       ,I.CODMARCADOR");
		consultaSQL.append("       ,M.DESCRICAO AS DESC_MARCADOR");
		consultaSQL.append("       ,I.TEMPO");
		consultaSQL.append("       ,I.CODASSUNTO");
		consultaSQL.append("       ,A.DESCRICAO AS DESC_ASSUNTO");
		consultaSQL.append("       ,I.HORAREAL");
		consultaSQL.append("       ,I.DURACAO");
		consultaSQL.append(" FROM DRS2X.INDEXS I ");
		consultaSQL.append(" LEFT JOIN DRS2X.ORADORESINDEXS OI ON OI.CODGRAVACAO = I.CODGRAVACAO AND OI.CODINDEX = I.CODINDEX");
		consultaSQL.append(" LEFT JOIN DRS2X.ORADORES O ON O.CODORADOR = OI.CODORADOR");
		consultaSQL.append(" LEFT JOIN DRS2X.ASSUNTOS A ON A.CODASSUNTO = I.CODASSUNTO ");
		consultaSQL.append(" LEFT JOIN DRS2X.MARCADORES M ON M.CODMARCADOR = I.CODMARCADOR ");
		consultaSQL.append(" WHERE I.CODGRAVACAO = ?");
		consultaSQL.append(" ORDER BY I.CODINDEX ");		
		
		return consultaSQL.toString();
	}
}
