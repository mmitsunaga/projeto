<%if (request.getAttribute("MensagemErro").toString().trim().equals("") == false){ %>
	<div class="divMensagemErro" id="MensagemErro"> <%=request.getAttribute("MensagemErro").toString().replace("\n", "<br />").trim()%></div>
<%}%>