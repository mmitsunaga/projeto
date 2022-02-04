<%@page language="java" contentType="text/xml" pageEncoding="iso-8859-1"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.RecursoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoCriminalDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.RecursoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoAssuntoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.RecursoAssuntoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoPrioridadeDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoTipoDt"%>

<?xml version="1.0" encoding="iso-8859-1"?>
<resposta versao='1' operacao='<%=(String)request.getAttribute("Operacao")%>'>
	<processos>
	<%List processos = (List) request.getAttribute("ListaProcessos");
	if(processos != null && processos.size() > 0){
	for(int i=0;i<processos.size();i++){
	ProcessoDt processoDt = (ProcessoDt) processos.get(i);%>
		<dadosProcesso>
			<numero><%=processoDt.getProcessoNumeroCompleto()%></numero>
			<area><%=processoDt.getArea()%></area>
			<numeroTco><%=processoDt.getTcoNumero() != null ? processoDt.getTcoNumero():""%></numeroTco>
			<idProcesso><%=processoDt.getId() != null ? processoDt.getId() : ""%></idProcesso>
			<hashProcesso><%=processoDt.getHash() != null ? processoDt.getHash() : ""%></hashProcesso>
			<%if(processoDt.getId_Recurso() != null && processoDt.getId_Recurso().length() > 0){
			RecursoDt recursoDt = processoDt.getRecursoDt();
			if(recursoDt != null){%>
			<recursos>
				<%List<ProcessoTipoDt> listaDeClasses = recursoDt.getListaDeProcessosTipoClasse();
				for(ProcessoTipoDt processoTipo:listaDeClasses){%>
				<recurso>
					<classe><%=processoTipo.getProcessoTipo()%></classe>
					<partes>
						<%List listaRecorrentes = null;
						if(recursoDt.getListaRecorrentes() != null ) {
							listaRecorrentes = recursoDt.getListaRecorrentesAtivos(processoTipo);
						}
					   	if(listaRecorrentes!=null && listaRecorrentes.size()>0) {
						RecursoParteDt recursoParteAux = (RecursoParteDt)listaRecorrentes.get(0);
						String parteTipo = "Polo Ativo | ";
						if(recursoParteAux.getProcessoParteDt().getProcessoParteTipoCodigo() != null && !recursoParteAux.getProcessoParteDt().getProcessoParteTipoCodigo().equals("")) {
							parteTipo += recursoParteAux.getDescricaoPoloAtivo();			
						} else {
							parteTipo += processoDt != null ? processoDt.getDescricaoPoloAtivo() : "Recorrente";			
						}
						for (int j=0; j<listaRecorrentes.size();j++) {
						RecursoParteDt recursoParteDt = (RecursoParteDt)listaRecorrentes.get(j);
					   	ProcessoParteDt parteDt = recursoParteDt.getProcessoParteDt();%>
						<parte>
							<parteTipo><%=parteTipo%></parteTipo>
							<nome><%=parteDt.getNome()%></nome>
						</parte>
						<%}}
						List listaRecorridos = null;
						if(recursoDt.getListaRecorridos() != null ) {
							listaRecorridos = recursoDt.getListaRecorridosAtivos(processoTipo);
						}
					   	if(listaRecorridos!=null && listaRecorridos.size()>0) {
						RecursoParteDt recursoParteAux = (RecursoParteDt)listaRecorridos.get(0);
						String parteTipo = "Polo Passivo | ";
						if(recursoParteAux.getProcessoParteDt().getProcessoParteTipoCodigo() != null && !recursoParteAux.getProcessoParteDt().getProcessoParteTipoCodigo().equals("")) {
							parteTipo += recursoParteAux.getDescricaoPoloPassivo();			
						} else {
							parteTipo += processoDt != null ? processoDt.getDescricaoPoloPassivo() : "Recorrido";			
						}
						for (int j=0; j<listaRecorridos.size();j++) {
						RecursoParteDt recursoParteDt = (RecursoParteDt)listaRecorridos.get(j);
					   	ProcessoParteDt parteDt = recursoParteDt.getProcessoParteDt();%>
						<parte>
							<parteTipo><%=parteTipo%></parteTipo>
							<nome><%=parteDt.getNome()%></nome>
						</parte>
						<%}}%>
					</partes>
				</recurso>
				<%}%>
				<outrasInformacoes>
					<serventia><%=processoDt.getServentia()%></serventia>
					<dataAutuacao><%=recursoDt!=null?recursoDt.getDataRecebimento():""%></dataAutuacao>
					<classe><%=recursoDt!=null?recursoDt.getProcessoTipo():""%></classe>
					<assuntos>
					<%List listaAssuntos = recursoDt.getListaAssuntos();
					if(listaAssuntos != null && listaAssuntos.size() > 0){
					for(int k=0;k<listaAssuntos.size();k++){
					RecursoAssuntoDt assuntoDt = (RecursoAssuntoDt)listaAssuntos.get(k);%>
						<assunto><%=assuntoDt.getAssunto()%></assunto>
					<%}}else{%>
						<assunto></assunto>
					<%}%>
					</assuntos>
				</outrasInformacoes>
			</recursos>
			<%}}%>
			<partes>
			<%
				List partes = new ArrayList();
					if(processoDt.getListaPolosAtivos() != null) partes.addAll(processoDt.getListaPolosAtivos());
					if(processoDt.getListaPolosPassivos() != null) partes.addAll(processoDt.getListaPolosPassivos());
					if(processoDt.getListaOutrasPartes() != null) partes.addAll(processoDt.getListaOutrasPartes());
					if (partes != null && partes.size() > 0){
					for (int j=0;j<partes.size();j++){
					ProcessoParteDt parte = (ProcessoParteDt)partes.get(j);
			%>
				<parte>
					<parteTipo><%=parte.getProcessoParteTipo()%></parteTipo>
					<nome><%=parte.getNome()%></nome>
				</parte>
			<%}}%>
			</partes>
			<outrasInformacoes>
				<serventia><%=(processoDt.getId_Recurso() != null && processoDt.getId_Recurso().length() > 0 && processoDt.getRecursoDt() != null) ? processoDt.getRecursoDt().getServentiaOrigem():processoDt.getServentia()%></serventia>
		   		<classe><%=processoDt.getProcessoTipo()%></classe>
				<assuntos>
				<%List listaAssuntos = processoDt.getListaAssuntos();	
				if (listaAssuntos != null && listaAssuntos.size() > 0){
				for (int k=0;k<listaAssuntos.size();k++){
				ProcessoAssuntoDt assuntoDt = (ProcessoAssuntoDt)listaAssuntos.get(k);%>
					<assunto><%=assuntoDt.getAssunto()%></assunto>
				<%}}else{%>
					<assunto></assunto>
				<%}%>
				</assuntos>	  	
	    	  	<valorCausa><%=(processoDt.getValor() != null && !processoDt.getValor().equals("Null"))?processoDt.getValor():"" %></valorCausa>
				<valorCondenacao><%=processoDt.getValorCondenacao()%></valorCondenacao>
				<processoOriginario><%=processoDt.getProcessoNumeroPrincipal()%></processoOriginario>				
				<faseProcessual><%=processoDt.getProcessoFase()%></faseProcessual>	
			    <dataDistribuicao><%=processoDt.getDataRecebimento()%></dataDistribuicao>
				<segredoJustica><%=processoDt.mostrarSegredoJustica()%></segredoJustica>
				<dataTransitoJulgado><%=processoDt.getDataTransitoJulgado()%></dataTransitoJulgado>
				<status><%=processoDt.getProcessoStatus()%></status>
				<prioridade><%=processoDt.getProcessoPrioridade()%></prioridade>
				<efeitoSuspensivo><%=(processoDt.getEfeitoSuspensivo() != null && processoDt.getEfeitoSuspensivo().equalsIgnoreCase("true") ? "Sim" : processoDt.getEfeitoSuspensivo().equalsIgnoreCase("false") ? "Não" : "")%></efeitoSuspensivo>
				<julgado2Grau><%=(processoDt.getServentiaTipoCodigo() != null && Funcoes.StringToInt(processoDt.getServentiaTipoCodigo()) == ServentiaTipoDt.SEGUNDO_GRAU && processoDt.getJulgado2Grau() != null && processoDt.getJulgado2Grau().equalsIgnoreCase("true") ? "Sim" : processoDt.getJulgado2Grau().equalsIgnoreCase("false") ? "Não" : "")%></julgado2Grau>
				<custa><%=processoDt.getCustaTipo()%></custa>
				<penhoraRosto><%=(processoDt.getPenhora() != null && processoDt.getPenhora().equalsIgnoreCase("true") ? "Sim" : processoDt.getPenhora().equalsIgnoreCase("false") ? "Não" : "")%></penhoraRosto>
				<dataPrescricao><%=processoDt.getDataPrescricao()%></dataPrescricao>
			</outrasInformacoes>
			<%if(processoDt.isCriminal()){%>
	    		<informacoesAdicionais>
	    		<%if(processoDt.getProcessoCriminalDt() == null){ 
	    			processoDt.setProcessoCriminalDt(new ProcessoCriminalDt());
	    		}%>
		   		<reuPreso><%=processoDt.getProcessoCriminalDt().getReuPreso() != null && processoDt.getProcessoCriminalDt().getReuPreso().equalsIgnoreCase("true") ? "Sim" : processoDt.getProcessoCriminalDt().getReuPreso().equalsIgnoreCase("false") ? "Não" : ""%></reuPreso>
		   		<numeroInquerito><%=processoDt.getTcoNumero()%></numeroInquerito>
				<dataPrisao><%=processoDt.getProcessoCriminalDt().getDataPrisao()%></dataPrisao>
	    	  	<dataOferecimentoDenunciaQueixa><%=processoDt.getProcessoCriminalDt().getDataOferecimentoDenuncia()%></dataOferecimentoDenunciaQueixa>
				<dataRecebimentoDenunciaQueixa><%=processoDt.getProcessoCriminalDt().getDataRecebimentoDenuncia()%></dataRecebimentoDenunciaQueixa>
			    <dataTransacaoPenal><%=processoDt.getProcessoCriminalDt().getDataTransacaoPenal()%></dataTransacaoPenal>
				<dataSuspensaoPenal><%=processoDt.getProcessoCriminalDt().getDataSuspensaoPenal()%></dataSuspensaoPenal>
				<dataFato><%=processoDt.getProcessoCriminalDt().getDataFato()%></dataFato>
				</informacoesAdicionais>							
			<%}%>
		</dadosProcesso>
		<%}}%>
	</processos>
</resposta>