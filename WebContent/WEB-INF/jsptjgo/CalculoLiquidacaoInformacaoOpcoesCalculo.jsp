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
  			<div class="area"><h2>&raquo; OBSERVAÇÃO SOBRE AS OPÇÕES DO CÁLCULO</h2></div>
  			
  			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<div id="divEditar" class="divEditar">
<br/>
						<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Remição</legend>
<label style="text-align: justify">
<p><i>Consideração do tempo de remição no cálculo: </i></p> 
<p>1: 	Deduzir o tempo de remição após a data base, direto no Requisito Temporal:
<br /> Esta é a opção padrão, tendo em vista atender a interpretação majoritária por ser mais benéfica, assim os dias trabalhados (remição) 
antes da data base são deduzidos, diretamente, do tempo cumprido gerando um restante de pena para aplicação da fração, o saldo devedor será 
somado à data base apresentando o requisito temporal. Porém, os dias trabalhados após a data base serão abatidos, diretamente, no requisito 
temporal.  
</p> 
<p>2: Considerar todo o tempo de Remição (antes e depois da data base) como Tempo Cumprido até a Data Base:
<br />Esta opção não está marcada, considerando ser menos utilizada, uma vez que serve para os casos em que o entendimento é de que os dias 
trabalhados (remição) antes e depois da data base deverão ser deduzidos, diretamente, do tempo cumprido até a data base para a aplicação da 
fração no restante de pena e, o saldo devedor será somado à data base apresentando o requisito temporal.
</p>
</label><br></fieldset><br/><br/>
					
						<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Geral</legend>
<label style="text-align: justify">
<p><i>Visualizar, no relatório, o término da pena unificada para as penas maiores que 30 anos:</i></p> 
<p>O Art. 75 do CP dispõe que o tempo de cumprimento das penas privativas de liberdade não pode ser superior a 30 (trinta) anos. 
(Redação dada pela Lei nº 7.209, de 11.7.1984)
<br />§ 1º - Quando o agente for condenado a penas privativas de liberdade cuja soma seja superior a 30 (trinta) anos, devem elas ser unificadas 
para atender ao limite máximo deste artigo. (Redação dada pela Lei nº 7.209, de 11.7.1984) 
<br />§ 2º - Sobrevindo condenação por fato posterior ao início do cumprimento da pena, far-se-á nova unificação, desprezando-se, para esse fim, 
o período de pena já cumprido.(Redação dada pela Lei nº 7.209, de 11.7.1984) Concurso de infrações.</p>
<p>*Desse modo, se as penas unificadas ultrapassarem 30 anos, esse cálculo demonstrará quando vai ocorrer o provável término da pena unificada, 
somente para fins de exaurimento da pena, em atendimento ao art.75 do CP. No entanto, os cálculos para verificar os benefícios, como progressão 
e livramento condicional, serão feitos considerando o total das condenações e, não, em cima de 30 anos. Portanto, quando o juiz mandar fazer um 
cálculo para verificar o provável término da pena unificada em mais de 30 anos, somente para fins de exaurimento da pena, utilize essa opção!
</p>
<br />
<p><i>Visualizar, no relatório, o restante da pena até último evento (para o cálculo de Pena Restritiva de Direito):</i></p> 
<p>No relatório oficial das penas restritivas de direito só aparece o campo informando tempo cumprido. Se o usuário quiser ver o quanto resta 
da pena, utilizar essa opção!</p>
</label><br></fieldset><br/><br/>

					<br />
				</div>
			</form>
		</div>
	</body>
</html>