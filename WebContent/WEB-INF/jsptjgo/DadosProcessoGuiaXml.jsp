<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%
ProcessoDt processoDt = (ProcessoDt) request.getAttribute("ProcessoCompletoDt");
ServentiaDt serventiaDt = (ServentiaDt) request.getAttribute("ServentiaDt");
%>

<?xml version="1.0" encoding="iso-8859-1" ?>
<projudi>
	<dadosProcesso>
		
		<processo pr="<%=processoDt.getProcessoNumeroCompleto()%>" isn="" bd="PROJUDI" />
		
		<!-- Data do Cancelamento -->
		<dataCancelamento dt="0" />
		
		<!-- Data da autuação -->
		<dataAutuacao dt="0" />
		
		<!-- Natureza / Tipo do Processo -->
		<processoTipo cd="<%=processoDt.getProcessoTipoCodigo() %>" no="<%=processoDt.getProcessoTipo() %>" />
		
		<!-- Area, ex: Civil/Criminal -->
		<area cd="<%=processoDt.getAreaCodigo() %>" no="<%=processoDt.getArea() %>" />
		
		<!-- Serventia -->
		<serventia cd="<%=processoDt.getServentiaCodigo() %>" no="<%=processoDt.getServentia() %>" />
		
		<!-- Comarca -->
		<comarca cd="<%=serventiaDt.getComarcaCodigo()%>" no="<%=serventiaDt.getComarca()%>" />
		
		<%
					List listaPromoventes = processoDt.getListaPolosAtivos();
						if (listaPromoventes != null && listaPromoventes.size() > 0) {
				%>
		<!-- Lista de autores -->
		<autores qt="<%=listaPromoventes.size()%>">
		<%
			for (int i=0;i < listaPromoventes.size();i++) {
			ProcessoParteDt parteDt = (ProcessoParteDt)listaPromoventes.get(i);
		%>
			<autor isn="<%=parteDt.getId()%>" no="<%=parteDt.getNome()%>" />
		<%
			}
				}
		%>
		</autores>
		
		<!-- 1º Autor -->
		<%
			ProcessoParteDt parteAutorDt = (ProcessoParteDt)processoDt.getListaPolosAtivos().get(0);
		%>
		<autor1 isn="<%=parteAutorDt.getId() %>" no="<%=parteAutorDt.getNome()%>" />
		
		<%
					List listaPromovidos = processoDt.getListaPolosPassivos();
						if (listaPromovidos != null && listaPromovidos.size() > 0) {
				%>
		<!-- Lista de reus -->
		<reus qt="<%=listaPromovidos.size()%>">
		<%
			for (int i=0;i < listaPromovidos.size();i++) {
			ProcessoParteDt parteDt = (ProcessoParteDt)listaPromovidos.get(i);
		%>
			<reu isn="<%=parteDt.getId()%>" no="<%=parteDt.getNome()%>" />
		<%
			}
				}
		%>
		</reus>
		
		<!-- 1º Reu -->
		<%
			ProcessoParteDt parteReuDt = (ProcessoParteDt)processoDt.getListaPolosPassivos().get(0);
		%>
		<reu1 isn="<%=parteReuDt.getId() %>" no="<%=parteReuDt.getNome()%>" />
		
		<!-- Valor da Causa -->
		<valorCausa va="<%=processoDt.getValor()%>" />
		
		<!-- Data do Protocolo -->
		<dataProtocolo dt="<%=processoDt.getDataRecebimento() %>" />
		
	</dadosProcesso>
</projudi>