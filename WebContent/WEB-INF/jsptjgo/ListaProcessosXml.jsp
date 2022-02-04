<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>

<?xml version="1.0" encoding="iso-8859-1"?>
<resposta versao='1' operacao='<%=(String)request.getAttribute("Operacao")%>'> 
	<situacao>OK</situacao>
	<mensagem> 
		<paginacao><%=((String)request.getAttribute("Paginacao"))!=null?(String)request.getAttribute("Paginacao"):""%></paginacao>
		<lista> 
			<%if(request.getAttribute("ListaProcessos") != null) {
			List lisProcessos = (List)request.getAttribute("ListaProcessos");
			for (int i=0; i < lisProcessos.size();i++){
			ProcessoDt processoDt = (ProcessoDt)lisProcessos.get(i);%> 
			<Processo>
				<Id><%=processoDt.getId()%></Id> 
				<%if(processoDt.getHash() != null) {%> 
					<HashProcesso><%=processoDt.getHash()%></HashProcesso>
				<%}%>
				<NumeroProcesso><%=processoDt.getProcessoNumeroCompleto()%></NumeroProcesso>
				<IdServentia><%=processoDt.getId_Serventia()%></IdServentia>
				<Serventia><%=processoDt.getServentia()%></Serventia> 
				<IdProcessoTipo><%=processoDt.getId_ProcessoTipo()%></IdProcessoTipo>
				<ProcessoTipo><%=processoDt.getProcessoTipo()%></ProcessoTipo>
				<Valor><%=(processoDt.getValor() != null && !processoDt.getValor().equals("Null"))?processoDt.getValor():""%></Valor>
				<%
					List lisPromoventes = processoDt.getListaPolosAtivos();
						if(lisPromoventes != null && lisPromoventes.size()>0){
						for (int j=0; j < lisPromoventes.size();j++) {
						ProcessoParteDt promovente = (ProcessoParteDt) lisPromoventes.get(j);
				%>
				<NomePromovente><%=promovente.getNome()%></NomePromovente> 
				<%
 					}}
 						List lisPromovidos = processoDt.getListaPolosPassivos();
 						if(lisPromovidos != null && lisPromovidos.size()>0){
 						for(int j=0; j < lisPromovidos.size();j++) {
 						ProcessoParteDt promovido = (ProcessoParteDt) lisPromovidos.get(j);
 				%>
				<NomePromovido><%=promovido.getNome()%></NomePromovido> 
				<%}}%> 
			</Processo> 
			<%}}%>
		</lista> 
	</mensagem> 
</resposta>