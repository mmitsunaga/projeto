
<div id="captchaDialogo">
	<style>
		.fixed-dialog { position: fixed; top: 50px; left: 50px; }
	</style>
		
	<form id="form" name="form" method="post" action="${tempRetorno}?PaginaAtual=-1&PassoBusca=3">
		<input type="hidden" id="Hash" name="Hash" value="${Hash}" />
		<input type="hidden" id="Viewstate" name="Viewstate" value="${Viewstate}" />		
		<p>Digite o c&oacute;digo de confer&ecirc;ncia abaixo para completar a consulta:</p>
		
		<img src="CaptchaServlet?v=<%=System.currentTimeMillis()%>" alt="codigo" />
		<audio id="audio" src="./CaptchaServlet?Tipo=SOM&v=<%=System.currentTimeMillis()%>"></audio>
		<div>
		  <img title="Reproduzir o áudio" alt="Reproduzir o áudio" src='./imagens/audio_icon.gif' onclick="document.getElementById('audio').play();"/> 						  
		  <img title="Aumentar o volume" alt="Aumentar o volume"  src='./imagens/16x16/Mais.png' onclick="document.getElementById('audio').volume+=0.1;">
		  <img title="Diminuir o volume"  alt="Diminuir o volume" src='./imagens/16x16/Menos.png' onclick="document.getElementById('audio').volume-=0.1;">
		</div>	
		
		<input class="formEdicaoInput" type="text" id="textoDigitado" name="textoDigitado" value=""/>
		<p style="font-weight: bold; color: red; margin:5px;">${MensagemErro}</p>
		
	</form>
</div>


