<%if (request.getAttribute("MensagemOk").toString().trim().equals("") == false){ %>
	<div class="divMensagemOk" id="MensagemOk"><%=request.getAttribute("MensagemOk").toString().replace("\n", "<br />").trim()%></div>
<%}%>