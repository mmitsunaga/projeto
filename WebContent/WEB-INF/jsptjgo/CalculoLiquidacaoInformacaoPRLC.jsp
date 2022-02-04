<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
	<head>
		<title>Eventos da Execução Penal</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/menusimples.css');
		</style>
	</head>

	<body>
  		<div id="divCorpo" class="divCorpo">
  			<div class="area"><h2>&raquo; OBSERVAÇÃO SOBRE PROGRESSÃO E LIVRAMENTO</h2></div>
  			
  			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<div id="divEditar" class="divEditar">
<br/>
						<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Reincidência Progressão</legend>
<label style="text-align: justify">
<p>se necessário, informe se deseja considerar a REINCIDÊNCIA para todos os crimes hediondos existentes! Ressalta-se que a lei 11.464/2007 só 
fala em reincidência, não fala em específica. Desse modo, após o 1º trânsito em julgado do cálculo em questão, se o sentenciado comete um ou 
mais crimes considerado(os) hediondo(os), operará a reincidência e, a fração será 3/5 para o (os) hediondo (os). Se não for reincidente, a 
fração será 2/5.</p>
<p>*Lembrar que hediondos são todos os crimes previstos na Lei 8.072/90, mas para a progressão o fato deverá ter ocorrido a partir de 29.03.2007. 
Para todos os crimes cometidos antes dessa data, a fração será 1/6.</p>
</label><br></fieldset><br/><br/>
						<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Livramento Condicional</legend>
<label style="text-align: justify">
<p>se necessário, informe na opção de cálculo, se deseja considerar a REINCIDÊNCIA ESPECÍFICA para todos os crimes hediondos 
existentes! Lembrando que, se o sentenciado é reincidente em crime hediondo, ou seja, se ele cometeu um crime hediondo com condenação 
transitada em julgada e, após essa data de trânsito comete outro crime hediondo, ele é considerado reincidente específico e, 
não tem direito ao Livramento Condicional sobre essas condenações (art.83, V do CP). Assim, só vai ser fracionado nos cálculos, as penas dos 
demais crimes.</p> 
</label><br></fieldset><br/><br/>
					<br />
				</div>
			</form>
		</div>
	</body>
</html>