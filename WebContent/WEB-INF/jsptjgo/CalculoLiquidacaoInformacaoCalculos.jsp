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
  			<div class="area"><h2>&raquo; OBSERVA��O SOBRE OS C�LCULOS</h2></div>
  			
  			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<div id="divEditar" class="divEditar">
<br/>
						<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Comuta��o Pr�via - Pena Individual</legend>
<label style="text-align: justify">
<p>� um c�lculo pr�vio utilizado para verificar, em cada pena (pena por pena, decreto por decreto), se o requisito temporal foi alcan�ado para obter a comuta��o. Nesse caso, o sistema apresenta o c�lculo para demonstrar se o sentenciado cumpriu a fra��o exigida pelo decreto pertinente.</p> 
<p>Feito o c�lculo, segue para decis�o. Se a decis�o for favor�vel, depois que o processo voltar ao setor de c�lculos, o analista insere o evento COMUTA��O.</p> 
<p>*Embora seja um direito do sentenciado, recomenda-se aguardar a decis�o sobre a utiliza��o deste recurso, uma vez que na forma individual, o c�lculo � mais ben�fico, conforme pode ser verificado na sua elabora��o.</p>
A Comuta��o � um benef�cio para ser verificado e apreciado em todos os decretos, n�o s� no �ltimo!
<p><i>Para realizar este c�lculo � necess�rio:</i></p>
- selecionar o decreto presidencial;
</label><br></fieldset><br/><br/>
						<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Comuta��o Pr�via - Pena Unificada</legend>
<label style="text-align: justify">
<p>C�lculo utilizado para verificar, sobre o TOTAL das penas (uma ou mais penas, decreto por decreto), se o requisito temporal foi alcan�ado para obter a comuta��o. Nesse caso, o sistema apresenta o c�lculo para demonstrar se o sentenciado cumpriu a fra��o exigida pelo decreto pertinente.</p>
<p>Feito o c�lculo, segue p/ decis�o. Se a decis�o for favor�vel, depois que o processo voltar ao setor de c�lculos, o analista insere o evento COMUTA��O. </p>
<p>*Considerando que este � o mais utilizado, n�o h� necessidade de aguardar decis�o.</p>
A Comuta��o � um benef�cio para ser verificado e apreciado em todos os decretos, n�o s� no �ltimo!
<p><i>Para realizar este c�lculo � necess�rio:</i></p>
- selecionar o decreto presidencial;<br/>
- se necess�rio, informe se deseja "Somar a fra��o do crime hediondo no saldo devedor".<br />
Utilizado quando o entendimento do ju�zo da comarca � de
que, para alcan�ar o requisito temporal, as fra��es devem ser somadas, ou seja, 2/3 do hediondo + 1/3 ou 1/4 dos comuns. Os decretos informam que
 o sentenciado tem que ter cumprido 2/3 do hediondo para ter direito no crime n�o impeditivo, no entanto, n�o informa se as fra��es devem ser 
somadas. Essa op��o foi criada para atender a interpreta��o de somar 2/3 + 1/3 ou 1/4, do contr�rio, �bvio, n�o precisa marcar!
</label><br></fieldset><br/><br/>
						<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Indulto</legend>
<label style="text-align: justify">
<p>Para utilizar esta op��o, observe primeiro o decreto e as suas fra��es, considerando que o Execpenweb, ainda n�o est� desenvolvido para reconhecer os incisos do artigo 1�, dos decretos. O programa s� faz o c�lculo da fra��o que o analista considerar, portanto, estude o artigo 1� do decreto pertinente antes de elaborar este c�lculo.</p>
<p>*O Indulto � um benef�cio para ser verificado e apreciado em todos os decretos, n�o s� no �ltimo!</p>
<p><i>Para realizar este c�lculo � necess�rio:</i></p>
- selecionar a fra��o do tempo total da condena��o para o c�lculo da data prov�vel do indulto; 
</label><br></fieldset><br/><br/>
						<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Prescri��o Retroativa/Intercorrente - Individual</legend>
<label style="text-align: justify">
<p>C�lculo utilizado quando houver determina��o, considerando que esta op��o deveria ter sido observada na a��o penal. Ainda n�o foi desenvolvido o c�lculo observando o art. 366 do CPP, a data de pron�ncia e o tr�nsito MP.</p>
</label><br></fieldset><br/><br/>
						<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Prescri��o Execut�ria - Individual (art. 119 CP)</legend>
<label style="text-align: justify">
<p>C�lculo utilizado para observar a prescri��o de cada pena, individualmente, ap�s o respectivo tr�nsito em julgado com in�cio de cumprimento de pena. Nesse caso, o sistema observa todos os lapsos temporais de fuga.</p>
<p>Caso n�o houve in�cio do cumprimento da pena, marcar a op��o "N�o iniciou o cumprimento da pena". Desta maneira, o sistema observa a prescri��o de cada pena, individualmente, ap�s o respectivo tr�nsito em julgado sem in�cio de cumprimento de pena.</p>
<p>* Quando o sentenciado tem mais de uma pena, esta � a op��o correta, nos termos do art.119 do CP.</p>
</label><br></fieldset><br/><br/>
						<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Prescri��o Execut�ria - Unificada</legend>
<label style="text-align: justify">
<p>C�lculo utilizado para verificar a prescri��o observando o restante da pena, conforme o art.113 do CP. Nesse caso, o c�lculo levar� em conta o quanto restou da pena at� a �ltima fuga do sentenciado, aplicando as regras do art.109 do CP.</p>
<p>*Mais indicado para utilizar quando h� s� uma pena!</p>
</label><br></fieldset><br/><br/>
						<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Progress�o de Regime</legend>
<label style="text-align: justify">
<p>C�lculo utilizado para verificar o requisito temporal da progress�o. Requer informa��o de data base, nos termos do art.112 da LEP ou interpreta��o judicial diversa.</p>
<p><i>Para realizar este c�lculo � necess�rio:</i></p>
- selecionar a data base;<br/>
- se necess�rio, informe se deseja "considerar a REINCID�NCIA para todos os crimes hediondos existentes!<br />
Ressalta-se que a lei 11.464/2007 s� fala em reincid�ncia, n�o fala em espec�fica. Desse modo, ap�s o 1� tr�nsito em julgado do c�lculo em quest�o, 
se o sentenciado comete um ou mais crimes considerado(os) hediondo(os), operar� a reincid�ncia e, a fra��o ser� 3/5 para o(os) hediondo(os). 
Se n�o for reincidente, a fra��o ser� 2/5." <br />
*Lembre-se que hediondos s�o todos os crimes previstos na Lei 8.072/90, mas para a progress�o o fato dever� ter ocorrido a partir de 29.03.2007. 
Para todos os crimes cometidos antes dessa data, a fra��o ser� 1/6.
</label><br></fieldset><br/><br/>
						<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Livramento Condicional</legend>
<label style="text-align: justify">
<p>C�lculo utilizado para verificar o requisito temporal para o livramento condicional. O sistema reconhece a data base.</p>
<p><i>Para realizar este c�lculo � necess�rio:</i></p>
- se necess�rio, informe se deseja "considerar a REINCID�NCIA ESPEC�FICA para todos os crimes hediondos existentes!"<br />
Lembrando que, se o sentenciado � reincidente em crime hediondo, ou seja, se ele cometeu um crime hediondo com condena��o transitada em julgada 
e, ap�s essa data de tr�nsito comete outro crime hediondo, ele � considerado reincidente espec�fico e, n�o tem direito ao Livramento Condicional 
sobre essas condena��es (art.83, V do CP). Assim, s� vai ser fracionado nos c�lculos, as penas dos demais crimes.
</label><br></fieldset><br/><br/>
						<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Pena Restritiva de Direito</legend>
<label style="text-align: justify">
<p>C�lculo utilizado para verificar o restante das horas de PSC (Presta��o de Servi�o � Comunidade) e/ou o prov�vel t�rmino da pena da LFS (Limita��o de Fim de Semana).</p>
<p>* Limita��o de Fim de Semana s�o os casos em que o sentenciado vai cumprir pernoite nos finais de semana.</p>
</label><br></fieldset><br/><br/>
						<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Outros</legend>
<label style="text-align: justify">
<p>Utilizado para o c�lculo das demais  modalidades institu�das pelas diversas interpreta��es acerca das penas restritivas de direitos, diversas da PSC e LFS, como Interdi��o Tempor�ria de Direitos, Presta��o Pecuni�ria e Sursis.
 Utilizado tamb�m para o c�lculo manual, feito no campo de "Informa��es Adicionais".</p>
</label><br></fieldset><br/><br/>

					<br />
				</div>
			</form>
		</div>
	</body>
</html>