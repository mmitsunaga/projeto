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
  			<div class="area"><h2>&raquo; OBSERVAÇÃO SOBRE DATA BASE E DICC</h2></div>
  			
  			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<div id="divEditar" class="divEditar">
<br/>
						<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Data Base</legend>
<label style="text-align: justify">
<p>I- LIVRAMENTO CONDICIONAL:</p>

<p>O cálculo para verificar o requisito do BENEFÍCIO chamado "Livramento Condicional",  é sempre pelo total da pena, portanto, 
a data base é a primeira prisão provisória. O sistema não computa o tempo interrompido como tempo cumprido. 
Caso o juiz determine outra data, o usuário deve ir em "Opções Cálculo" na parte inferior da tela de calcular, e inserir a data determinada.</p>

<p>II-PROGRESSÃO DE REGIME:</p>

<p>Quanto à progressão, a data base pode ser:<br/>
(observar o art.112 da LEP, que é a lei expressa):</p>

<p>-Se o sentenciado não progrediu nenhuma vez, ainda, de regime, a data base será a primeira prisão provisória. 
O cálculo será feito pelo total da pena;</p>

<p>-se ele progredir do fechado para o semiaberto, a data base para a projeção ao aberto, será a data desta 
progressão (começa, assim, a ser observado o restante de pena);</p>

<p>-se ele foge após a data da progressão, é preso, porém, é mantido no mesmo regime, a data base continuará a ser a data da progressão;</p>

<p>-se ele foge, depois é preso e, tem o regime regredido, a data base será a data da prisão (pois esta prisão ensejou a regressão);</p>

<p>-se o sentenciado está cumprindo pena no regime semiaberto ou aberto, sofre uma nova condenação, e há a regressão de regime, 
a data base será a data da regressão;</p>

<p>-a Falta Grave cometida dentro do presídio por falta disciplinar e, declarada pelo juiz como data base, será incluída nos 
cálculos e considerada como data base, apenas para a progressão, tendo em vista que não há doutrina e nem jurisprudência, 
ainda, considerando para o Livramento Condicional;</p>

<p>-a data do último trânsito em julgado quando o juiz determina, expressamente;</p>

<p>*Estas são datas que determina a lei expressa! No entanto, se o juiz REGISTRAR outra data, é evidente que o analista 
deverá obedecer a ordem escrita! Caso o juiz determine outra data, o usuário deve ir em "Opções Cálculo" na parte inferior 
da tela de calcular, e inserir a data determinada.</p>
</label><br></fieldset><br/><br/>
						<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Data de Início de Cumprimento da Condenação (DICC)</legend>
<label style="text-align: justify">
<p>Observar cada condenação da unificação de penas: </p>

<p>O DICC é importante porque permite computar o tempo cumprido, e o restante de pena de cada condenação, especialmente, quando está 
envolvido crime comum e crime hediondo. Importante, ainda, para verificar o tempo para Comutação e Prescrição, na pena individual.</p>

<p>Para registrar o DICC, acesse "Manter Eventos da Execução" e observe a data do fato de cada condenação. Desse modo, o 
usuário poderá ver na totalidade as prisões ou possíveis datas para considerar como data início da condenação. Não confundir DICC 
com Primeiro Regime. O DICC é data de início de cada condenação. O Primeiro Regime é data de início de cumprimento de pena, ou seja, 
prisão definitiva, quando já há um trânsito em julgado para o sentenciado. Na unificação de penas, após o primeiro trânsito, o réu 
já é um sentenciado, portanto, já tem que cumprir um regime definitivo.</p> 

<p>O DICC pode ser (observe por exclusão):</p>

<p>I-A data da prisão em flagrante, se houver flagrante na condenação que o usuário está cadastrando; Se não houver 
flagrante, passar para a informação a seguir;</p>

<p>II-A próxima prisão após a data do fato lançada nos eventos do cálculo;</p>

<p>III-A data do fato, quando o crime for cometido dentro da cadeia.</p>

<p>IV-O Trânsito em Julgado, quando o sentenciado está no Regime Semiaberto ou Aberto, comete fato novo, mas não é preso e, 
continua,  após a nova condenação no mesmo regime.</p>

<p>V-Se o sentenciado não foi preso ao responder o processo, ou se não é nenhum dos casos acima, o DICC vai coincidir com o 
Primeiro Regime, que significa a prisão definitiva, por mandado de prisão ou data  da audiência admonitória, quando é o 
caso de semiaberto e aberto.</p>
</label><br></fieldset><br/><br/>
						<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Primeiro Regime</legend>
<label style="text-align: justify">
<p>Ou Prisão Definitiva ou Início de Cumprimento de PENA => significa que já existe um trânsito em julgado para o sentenciado.</p>

<p>Obs: Só existe um Primeiro Regime e, é o próximo evento após o primeiro Trânsito em Julgado.</p>

<p>O Primeiro Regime pode ser:</p>

<p>I-Se o sentenciado fica preso sem interrupção até a data do 1º Trânsito em Julgado na lista de eventos, o PRIMEIRO REGIME 
será um dia após o Trânsito em Julgado.<br/>
(Em qualquer das prisões provisórias! Ressalta-se que, prisão provisória em execução, na unificação de penas, é toda prisão 
antes do 1º trânsito em julgado. Após a data do 1º trânsito é: prisão ou, prisão em flagrante.).</p>

<p>II-Se houver Interrupção da Prisão Provisória (por fuga ou relaxamento), o Primeiro Regime será o dia em que o sentenciado 
iniciou o cumprimento da pena após a data do 1º trânsito em julgado da lista de eventos, seja por mandado de prisão ou por 
apresentação na Audiência Admonitória.</p>
</label><br></fieldset><br/><br/>	
					
					<br />
				</div>
			</form>
		</div>
	</body>
</html>