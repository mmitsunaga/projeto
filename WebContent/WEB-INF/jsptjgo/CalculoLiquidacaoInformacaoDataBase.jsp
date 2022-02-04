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
  			<div class="area"><h2>&raquo; OBSERVA��O SOBRE DATA BASE E DICC</h2></div>
  			
  			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<div id="divEditar" class="divEditar">
<br/>
						<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Data Base</legend>
<label style="text-align: justify">
<p>I- LIVRAMENTO CONDICIONAL:</p>

<p>O c�lculo para verificar o requisito do BENEF�CIO chamado "Livramento Condicional",  � sempre pelo total da pena, portanto, 
a data base � a primeira pris�o provis�ria. O sistema n�o computa o tempo interrompido como tempo cumprido. 
Caso o juiz determine outra data, o usu�rio deve ir em "Op��es C�lculo" na parte inferior da tela de calcular, e inserir a data determinada.</p>

<p>II-PROGRESS�O DE REGIME:</p>

<p>Quanto � progress�o, a data base pode ser:<br/>
(observar o art.112 da LEP, que � a lei expressa):</p>

<p>-Se o sentenciado n�o progrediu nenhuma vez, ainda, de regime, a data base ser� a primeira pris�o provis�ria. 
O c�lculo ser� feito pelo total da pena;</p>

<p>-se ele progredir do fechado para o semiaberto, a data base para a proje��o ao aberto, ser� a data desta 
progress�o (come�a, assim, a ser observado o restante de pena);</p>

<p>-se ele foge ap�s a data da progress�o, � preso, por�m, � mantido no mesmo regime, a data base continuar� a ser a data da progress�o;</p>

<p>-se ele foge, depois � preso e, tem o regime regredido, a data base ser� a data da pris�o (pois esta pris�o ensejou a regress�o);</p>

<p>-se o sentenciado est� cumprindo pena no regime semiaberto ou aberto, sofre uma nova condena��o, e h� a regress�o de regime, 
a data base ser� a data da regress�o;</p>

<p>-a Falta Grave cometida dentro do pres�dio por falta disciplinar e, declarada pelo juiz como data base, ser� inclu�da nos 
c�lculos e considerada como data base, apenas para a progress�o, tendo em vista que n�o h� doutrina e nem jurisprud�ncia, 
ainda, considerando para o Livramento Condicional;</p>

<p>-a data do �ltimo tr�nsito em julgado quando o juiz determina, expressamente;</p>

<p>*Estas s�o datas que determina a lei expressa! No entanto, se o juiz REGISTRAR outra data, � evidente que o analista 
dever� obedecer a ordem escrita! Caso o juiz determine outra data, o usu�rio deve ir em "Op��es C�lculo" na parte inferior 
da tela de calcular, e inserir a data determinada.</p>
</label><br></fieldset><br/><br/>
						<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Data de In�cio de Cumprimento da Condena��o (DICC)</legend>
<label style="text-align: justify">
<p>Observar cada condena��o da unifica��o de penas: </p>

<p>O DICC � importante porque permite computar o tempo cumprido, e o restante de pena de cada condena��o, especialmente, quando est� 
envolvido crime comum e crime hediondo. Importante, ainda, para verificar o tempo para Comuta��o e Prescri��o, na pena individual.</p>

<p>Para registrar o DICC, acesse "Manter Eventos da Execu��o" e observe a data do fato de cada condena��o. Desse modo, o 
usu�rio poder� ver na totalidade as pris�es ou poss�veis datas para considerar como data in�cio da condena��o. N�o confundir DICC 
com Primeiro Regime. O DICC � data de in�cio de cada condena��o. O Primeiro Regime � data de in�cio de cumprimento de pena, ou seja, 
pris�o definitiva, quando j� h� um tr�nsito em julgado para o sentenciado. Na unifica��o de penas, ap�s o primeiro tr�nsito, o r�u 
j� � um sentenciado, portanto, j� tem que cumprir um regime definitivo.</p> 

<p>O DICC pode ser (observe por exclus�o):</p>

<p>I-A data da pris�o em flagrante, se houver flagrante na condena��o que o usu�rio est� cadastrando; Se n�o houver 
flagrante, passar para a informa��o a seguir;</p>

<p>II-A pr�xima pris�o ap�s a data do fato lan�ada nos eventos do c�lculo;</p>

<p>III-A data do fato, quando o crime for cometido dentro da cadeia.</p>

<p>IV-O Tr�nsito em Julgado, quando o sentenciado est� no Regime Semiaberto ou Aberto, comete fato novo, mas n�o � preso e, 
continua, �ap�s a nova condena��o no mesmo regime.</p>

<p>V-Se o sentenciado n�o foi preso ao responder o processo, ou se n�o � nenhum dos casos acima, o DICC vai coincidir com o 
Primeiro Regime, que significa a pris�o definitiva, por mandado de pris�o ou data �da audi�ncia admonit�ria, quando � o 
caso de semiaberto e aberto.</p>
</label><br></fieldset><br/><br/>
						<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Primeiro Regime</legend>
<label style="text-align: justify">
<p>Ou Pris�o Definitiva ou In�cio de Cumprimento de PENA => significa que j� existe um tr�nsito em julgado para o sentenciado.</p>

<p>Obs: S� existe um Primeiro Regime e, � o pr�ximo evento ap�s o primeiro Tr�nsito em Julgado.</p>

<p>O Primeiro Regime pode ser:</p>

<p>I-Se o sentenciado fica preso sem interrup��o at� a data do 1� Tr�nsito em Julgado na lista de eventos, o PRIMEIRO REGIME 
ser� um dia ap�s o Tr�nsito em Julgado.<br/>
(Em qualquer das pris�es provis�rias! Ressalta-se que, pris�o provis�ria em execu��o, na unifica��o de penas, � toda pris�o 
antes do 1� tr�nsito em julgado. Ap�s a data do 1� tr�nsito �: pris�o ou, pris�o em flagrante.).</p>

<p>II-Se houver Interrup��o da Pris�o Provis�ria (por fuga ou relaxamento), o Primeiro Regime ser� o dia em que o sentenciado 
iniciou o cumprimento da pena ap�s a data do 1� tr�nsito em julgado da lista de eventos, seja por mandado de pris�o ou por 
apresenta��o na Audi�ncia Admonit�ria.</p>
</label><br></fieldset><br/><br/>	
					
					<br />
				</div>
			</form>
		</div>
	</body>
</html>