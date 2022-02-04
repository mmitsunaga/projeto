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
  			<div class="area"><h2>&raquo; OBSERVA��O SOBRE AS OP��ES DO C�LCULO</h2></div>
  			
  			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<div id="divEditar" class="divEditar">
<br/>
						<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Remi��o</legend>
<label style="text-align: justify">
<p><i>Considera��o do tempo de remi��o no c�lculo: </i></p> 
<p>1: 	Deduzir o tempo de remi��o ap�s a data base, direto no Requisito Temporal:
<br /> Esta � a op��o padr�o, tendo em vista atender a interpreta��o majorit�ria por ser mais ben�fica, assim os dias trabalhados (remi��o) 
antes da data base s�o deduzidos, diretamente, do tempo cumprido gerando um restante de pena para aplica��o da fra��o, o saldo devedor ser� 
somado � data base apresentando o requisito temporal. Por�m, os dias trabalhados ap�s a data base ser�o abatidos, diretamente, no requisito 
temporal.  
</p> 
<p>2: Considerar todo o tempo de Remi��o (antes e depois da data base) como Tempo Cumprido at� a Data Base:
<br />Esta op��o n�o est� marcada, considerando ser menos utilizada, uma vez que serve para os casos em que o entendimento � de que os dias 
trabalhados (remi��o) antes e depois da data base dever�o ser deduzidos, diretamente, do tempo cumprido at� a data base para a aplica��o da 
fra��o no restante de pena e, o saldo devedor ser� somado � data base apresentando o requisito temporal.
</p>
</label><br></fieldset><br/><br/>
					
						<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Geral</legend>
<label style="text-align: justify">
<p><i>Visualizar, no relat�rio, o t�rmino da pena unificada para as penas maiores que 30 anos:</i></p> 
<p>O Art. 75 do CP disp�e que o tempo de cumprimento das penas privativas de liberdade n�o pode ser superior a 30 (trinta) anos. 
(Reda��o dada pela Lei n� 7.209, de 11.7.1984)
<br />� 1� - Quando o agente for condenado a penas privativas de liberdade cuja soma seja superior a 30 (trinta) anos, devem elas ser unificadas 
para atender ao limite m�ximo deste artigo. (Reda��o dada pela Lei n� 7.209, de 11.7.1984) 
<br />� 2� - Sobrevindo condena��o por fato posterior ao in�cio do cumprimento da pena, far-se-� nova unifica��o, desprezando-se, para esse fim, 
o per�odo de pena j� cumprido.(Reda��o dada pela Lei n� 7.209, de 11.7.1984) Concurso de infra��es.</p>
<p>*Desse modo, se as penas unificadas ultrapassarem 30 anos, esse c�lculo demonstrar� quando vai ocorrer o prov�vel t�rmino da pena unificada, 
somente para fins de exaurimento da pena, em atendimento ao art.75 do CP. No entanto, os c�lculos para verificar os benef�cios, como progress�o 
e livramento condicional, ser�o feitos considerando o total das condena��es e, n�o, em cima de 30 anos. Portanto, quando o juiz mandar fazer um 
c�lculo para verificar o prov�vel t�rmino da pena unificada em mais de 30 anos, somente para fins de exaurimento da pena, utilize essa op��o!
</p>
<br />
<p><i>Visualizar, no relat�rio, o restante da pena at� �ltimo evento (para o c�lculo de Pena Restritiva de Direito):</i></p> 
<p>No relat�rio oficial das penas restritivas de direito s� aparece o campo informando tempo cumprido. Se o usu�rio quiser ver o quanto resta 
da pena, utilizar essa op��o!</p>
</label><br></fieldset><br/><br/>

					<br />
				</div>
			</form>
		</div>
	</body>
</html>