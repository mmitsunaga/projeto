<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
	<head>
		<title>Eventos da Execu��o Penal</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/menusimples.css');
		</style>
	</head>

	<body>
  		<div id="divCorpo" class="divCorpo">
  			<div class="area"><h2>&raquo; OBSERVA��O SOBRE PROGRESS�O E LIVRAMENTO</h2></div>
  			
  			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<div id="divEditar" class="divEditar">
<br/>
						<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Reincid�ncia Progress�o</legend>
<label style="text-align: justify">
<p>se necess�rio, informe se deseja considerar a REINCID�NCIA para todos os crimes hediondos existentes! Ressalta-se que a lei 11.464/2007 s� 
fala em reincid�ncia, n�o fala em espec�fica. Desse modo, ap�s o 1� tr�nsito em julgado do c�lculo em quest�o, se o sentenciado comete um ou 
mais crimes considerado(os) hediondo(os), operar� a reincid�ncia e, a fra��o ser� 3/5 para o (os) hediondo (os). Se n�o for reincidente, a 
fra��o ser� 2/5.</p>
<p>*Lembrar que hediondos s�o todos os crimes previstos na Lei 8.072/90, mas para a progress�o o fato dever� ter ocorrido a partir de 29.03.2007. 
Para todos os crimes cometidos antes dessa data, a fra��o ser� 1/6.</p>
</label><br></fieldset><br/><br/>
						<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Livramento Condicional</legend>
<label style="text-align: justify">
<p>se necess�rio, informe na op��o de c�lculo, se deseja considerar a REINCID�NCIA ESPEC�FICA para todos os crimes hediondos 
existentes! Lembrando que, se o sentenciado � reincidente em crime hediondo, ou seja, se ele cometeu um crime hediondo com condena��o 
transitada em julgada e, ap�s essa data de tr�nsito comete outro crime hediondo, ele � considerado reincidente espec�fico e, 
n�o tem direito ao Livramento Condicional sobre essas condena��es (art.83, V do CP). Assim, s� vai ser fracionado nos c�lculos, as penas dos 
demais crimes.</p> 
</label><br></fieldset><br/><br/>
					<br />
				</div>
			</form>
		</div>
	</body>
</html>