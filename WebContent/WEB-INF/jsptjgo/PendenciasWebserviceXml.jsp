<%@page language="java" contentType="text/xml" pageEncoding="iso-8859-1"%>
<%@page import="java.util.List"%>

<?xml version="1.0" encoding="iso-8859-1"?>
<resposta versao='1' operacao='<%=(String)request.getAttribute("Operacao")%>'>
	<pendencias>
		<%List[] listaPendencias = null;
		if(request.getAttribute("ListaPendencias") != null)
		listaPendencias = (List[]) request.getAttribute("ListaPendencias");
	    if(listaPendencias != null){
		List listaAguardandoExpedicao = listaPendencias[0];
		List listaAguardandoVisto = listaPendencias[1];
		List listaAguardandoRecebimento = listaPendencias[2];
		List listaAguardandoCumprimento = listaPendencias[3];
		List listaAguardandoVerificacao = listaPendencias[4];
		List listaDecursoPrazo = listaPendencias[5];
		List listaAguardandoLeitura = listaPendencias[6];
		List listaPendenciasFuturas = listaPendencias[7];
		List listaPendenciasLiberarAcesso = listaPendencias[8];
		List listaAguardandoVerificacaoServentiaCargo = listaPendencias[9];
		List listaAguardandoCorrecao = listaPendencias[10];
		if (listaAguardandoExpedicao != null && listaAguardandoExpedicao.size() > 0){
   		for(int i=0; i < listaAguardandoExpedicao.size();i++){
		String[] pendenciaStr = (String[])listaAguardandoExpedicao.get(i);%>
		<pendencia>
			<pendenciaTipo><%=pendenciaStr[0]%></pendenciaTipo>
			<responsavel><%=pendenciaStr[2]%></responsavel>
			<dataInicio><%=pendenciaStr[1]%></dataInicio>
			<dataFim></dataFim>
			<dataLimite></dataLimite>
			<liberadoAte></liberadoAte>
			<status>Aguardando Expedição</status>
		</pendencia>
		<%}}if(listaAguardandoVerificacao != null && listaAguardandoVerificacao.size() > 0){
		for(int i=0; i < listaAguardandoVerificacao.size();i++){
		String[] pendenciaStr = (String[])listaAguardandoVerificacao.get(i);%>		
		<pendencia>
			<pendenciaTipo><%=pendenciaStr[0]%></pendenciaTipo>
			<responsavel><%=pendenciaStr[2]%></responsavel>
			<dataInicio><%=pendenciaStr[1]%></dataInicio>
			<dataFim></dataFim>
			<dataLimite></dataLimite>
			<liberadoAte></liberadoAte>
			<status>Aguardando Verificação</status>
		</pendencia>
		<%}}if (listaAguardandoVerificacaoServentiaCargo != null && listaAguardandoVerificacaoServentiaCargo.size() > 0){
		for(int i=0; i < listaAguardandoVerificacaoServentiaCargo.size();i++){
		String[] pendenciaStr = (String[])listaAguardandoVerificacaoServentiaCargo.get(i);%>
		<pendencia>
			<pendenciaTipo><%=pendenciaStr[1]%></pendenciaTipo>
			<responsavel><%=pendenciaStr[2]%></responsavel>
			<dataInicio><%=pendenciaStr[3]%></dataInicio>
			<dataFim></dataFim>
			<dataLimite></dataLimite>
			<liberadoAte></liberadoAte>
			<status>Aguardando Vista / Relatório / Revisão</status>
		</pendencia>
 		<%}}if(listaPendenciasFuturas != null && listaPendenciasFuturas.size() > 0){
 		for(int i=0; i < listaPendenciasFuturas.size();i++){
      	String[] pendenciaStr = (String[])listaPendenciasFuturas.get(i);%>
		<pendencia>
			<pendenciaTipo><%=pendenciaStr[0]%></pendenciaTipo>
			<responsavel><%=pendenciaStr[2]%></responsavel>
			<dataInicio><%=pendenciaStr[1]%></dataInicio>
			<dataFim></dataFim>
			<dataLimite></dataLimite>
			<liberadoAte></liberadoAte>
			<status>Pendências Futuras</status>
		</pendencia>
		<%}}if(listaPendenciasLiberarAcesso != null && listaPendenciasLiberarAcesso.size() > 0){
		for(int i=0; i < listaPendenciasLiberarAcesso.size();i++){
      	String[] pendenciaStr = (String[])listaPendenciasLiberarAcesso.get(i);%>
		<pendencia>
			<pendenciaTipo><%=pendenciaStr[0]%></pendenciaTipo>
			<responsavel><%=pendenciaStr[2]%></responsavel>
			<dataInicio></dataInicio>
			<dataFim></dataFim>
			<dataLimite></dataLimite>
			<liberadoAte><%=pendenciaStr[1]%></liberadoAte>
			<status>Pendências de Liberação de Acesso</status>
		</pendencia>
		<%}}if (listaAguardandoRecebimento != null && listaAguardandoRecebimento.size() > 0){
		for(int i=0; i < listaAguardandoRecebimento.size();i++){
      	String[] pendenciaStr = (String[])listaAguardandoRecebimento.get(i);%>		
		<pendencia>
			<pendenciaTipo><%=pendenciaStr[0]%></pendenciaTipo>
			<responsavel><%=pendenciaStr[2]%></responsavel>
			<dataInicio><%=pendenciaStr[1]%></dataInicio>
			<dataFim></dataFim>
			<dataLimite></dataLimite>
			<liberadoAte></liberadoAte>
			<status>Aguardando Recebimento</status>
		</pendencia>
		<%}}if(listaAguardandoCumprimento != null && listaAguardandoCumprimento.size() > 0){
		for(int i=0; i < listaAguardandoCumprimento.size();i++){
      	String[] pendenciaStr = (String[])listaAguardandoCumprimento.get(i);%>
		<pendencia>
			<pendenciaTipo><%=pendenciaStr[0]%></pendenciaTipo>
			<responsavel><%=pendenciaStr[2]%></responsavel>
			<dataInicio><%=pendenciaStr[1]%></dataInicio>
			<dataFim></dataFim>
			<dataLimite></dataLimite>
			<liberadoAte></liberadoAte>
			<status>Aguardando Cumprimento</status>
		</pendencia>
		<%}}if(listaAguardandoCorrecao != null && listaAguardandoCorrecao.size() > 0){
		for(int i=0; i < listaAguardandoCorrecao.size();i++){
      	String[] pendenciaStr = (String[])listaAguardandoCorrecao.get(i);%>		
		<pendencia>
			<pendenciaTipo><%=pendenciaStr[0]%></pendenciaTipo>
			<responsavel></responsavel>
			<dataInicio><%=pendenciaStr[1]%></dataInicio>
			<dataFim></dataFim>
			<dataLimite></dataLimite>
			<liberadoAte></liberadoAte>
			<status>Aguardando Correção</status>
		</pendencia>
		<%}}if(listaAguardandoVisto != null && listaAguardandoVisto.size() > 0){
		for(int i=0; i < listaAguardandoVisto.size();i++){
      	String[] pendenciaStr = (String[])listaAguardandoVisto.get(i);%>			
		<pendencia>
			<pendenciaTipo><%=pendenciaStr[0]%></pendenciaTipo>
			<responsavel></responsavel>
			<dataInicio><%=pendenciaStr[1]%></dataInicio>
			<dataFim><%=pendenciaStr[2]%></dataFim>
			<dataLimite></dataLimite>
			<liberadoAte></liberadoAte>
			<status>Aguardando Visto</status>
		</pendencia>
		<%}}if (listaAguardandoLeitura != null && listaAguardandoLeitura.size() > 0){
		for(int i=0; i < listaAguardandoLeitura.size();i++){
      	String[] pendenciaStr = (String[])listaAguardandoLeitura.get(i);%>		
		<pendencia>
			<pendenciaTipo><%=pendenciaStr[0]%></pendenciaTipo>
			<responsavel><%=pendenciaStr[3]%></responsavel>
			<dataInicio><%=pendenciaStr[1]%></dataInicio>
			<dataFim></dataFim>
			<dataLimite><%=pendenciaStr[2]%></dataLimite>
			<liberadoAte></liberadoAte>
			<status>Aguardando Leitura</status>
		</pendencia>
		<%}}if(listaDecursoPrazo != null && listaDecursoPrazo.size() > 0){
		for(int i=0; i < listaDecursoPrazo.size();i++){
      	String[] pendenciaStr = (String[])listaDecursoPrazo.get(i);%>			
		<pendencia>
			<pendenciaTipo><%=pendenciaStr[0]%></pendenciaTipo>
			<responsavel></responsavel>
			<dataInicio><%=pendenciaStr[1]%></dataInicio>
			<dataFim><%=pendenciaStr[2]%></dataFim>
			<dataLimite></dataLimite>
			<liberadoAte></liberadoAte>
			<status>Aguardando Decurso de Prazo</status>
		</pendencia>
		<%}}}%>
	</pendencias>
	<conclusoes>
		<%List listaConclusoesPendentes = null;
       	if(request.getAttribute("ListaConclusoes") != null) listaConclusoesPendentes = (List)request.getAttribute("ListaConclusoes");
   		if(listaConclusoesPendentes != null && listaConclusoesPendentes.size() > 0) {
   		String[] conclusaoPendente = null;
		for(int i=0;i < listaConclusoesPendentes.size(); i++){
		conclusaoPendente = (String[]) listaConclusoesPendentes.get(i);%>
		<conclusao>
			<conclusaoTipo><%=conclusaoPendente[1]%></conclusaoTipo>
            <responsavel><%=conclusaoPendente[5]%></responsavel>
           	<dataInicio><%=conclusaoPendente[2]%></dataInicio>
           	<status>Conclusão Pendente</status>
		</conclusao>
       	<%}}%>
	</conclusoes>
	<audiencias>
		<%List listaAudienciasPendentes = null;
       	if(request.getAttribute("ListaAudiencias") != null) listaAudienciasPendentes = (List)request.getAttribute("ListaAudiencias");
   		if(listaAudienciasPendentes != null && listaAudienciasPendentes.size() > 0) {
   		String[] audienciaPendente = null;
   		for(int i=0;i < listaAudienciasPendentes.size(); i++){
   		audienciaPendente = (String[]) listaAudienciasPendentes.get(i);%>
		<audiencia>
   			<audienciaTipo><%=audienciaPendente[1]%></audienciaTipo>
   			<responsavel><%=audienciaPendente[3]%></responsavel>
   			<dataAgendada><%=audienciaPendente[2]%></dataAgendada>
   			<serventia><%=audienciaPendente[8]%></serventia>
   			<status>Audiência Pendente</status>							          		
		</audiencia>
		<%}}%>
	</audiencias>
</resposta>