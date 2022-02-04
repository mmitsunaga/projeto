<?xml version="1.0" encoding="iso-8859-1"?>
<resposta versao='1' operacao='<%=(String)request.getAttribute("Operacao")%>'>
	<situacao><%=(String)request.getAttribute("RespostaTipo")%></situacao>
	<%if(request.getAttribute("Paginacao")!=null){%>
	<paginacao><%=((String)request.getAttribute("Paginacao"))!=null?(String)request.getAttribute("Paginacao"):""%></paginacao><%}%>		
	<mensagem><%=(String)request.getAttribute("Mensagem")%></mensagem>
</resposta>
<%-- Operação - número correspondente a resposta da operação
     RespostaTipo - se a mensagem  é de OK ou de Erro	
     Mensagem - descrição do resultado da resposta --%>