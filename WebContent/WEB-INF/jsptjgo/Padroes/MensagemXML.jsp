<?xml version="1.0" encoding="iso-8859-1"?>
<resposta versao='1' operacao='<%=(String)request.getAttribute("Operacao")%>'>
	<situacao><%=(String)request.getAttribute("RespostaTipo")%></situacao>
	<%if(request.getAttribute("Paginacao")!=null){%>
	<paginacao><%=((String)request.getAttribute("Paginacao"))!=null?(String)request.getAttribute("Paginacao"):""%></paginacao><%}%>		
	<mensagem><%=(String)request.getAttribute("Mensagem")%></mensagem>
</resposta>
<%-- Opera��o - n�mero correspondente a resposta da opera��o
     RespostaTipo - se a mensagem  � de OK ou de Erro	
     Mensagem - descri��o do resultado da resposta --%>