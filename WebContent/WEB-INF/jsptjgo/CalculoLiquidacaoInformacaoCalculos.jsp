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
  			<div class="area"><h2>&raquo; OBSERVAÇÃO SOBRE OS CÁLCULOS</h2></div>
  			
  			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<div id="divEditar" class="divEditar">
<br/>
						<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Comutação Prévia - Pena Individual</legend>
<label style="text-align: justify">
<p>É um cálculo prévio utilizado para verificar, em cada pena (pena por pena, decreto por decreto), se o requisito temporal foi alcançado para obter a comutação. Nesse caso, o sistema apresenta o cálculo para demonstrar se o sentenciado cumpriu a fração exigida pelo decreto pertinente.</p> 
<p>Feito o cálculo, segue para decisão. Se a decisão for favorável, depois que o processo voltar ao setor de cálculos, o analista insere o evento COMUTAÇÃO.</p> 
<p>*Embora seja um direito do sentenciado, recomenda-se aguardar a decisão sobre a utilização deste recurso, uma vez que na forma individual, o cálculo é mais benéfico, conforme pode ser verificado na sua elaboração.</p>
A Comutação é um benefício para ser verificado e apreciado em todos os decretos, não só no último!
<p><i>Para realizar este cálculo é necessário:</i></p>
- selecionar o decreto presidencial;
</label><br></fieldset><br/><br/>
						<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Comutação Prévia - Pena Unificada</legend>
<label style="text-align: justify">
<p>Cálculo utilizado para verificar, sobre o TOTAL das penas (uma ou mais penas, decreto por decreto), se o requisito temporal foi alcançado para obter a comutação. Nesse caso, o sistema apresenta o cálculo para demonstrar se o sentenciado cumpriu a fração exigida pelo decreto pertinente.</p>
<p>Feito o cálculo, segue p/ decisão. Se a decisão for favorável, depois que o processo voltar ao setor de cálculos, o analista insere o evento COMUTAÇÃO. </p>
<p>*Considerando que este é o mais utilizado, não há necessidade de aguardar decisão.</p>
A Comutação é um benefício para ser verificado e apreciado em todos os decretos, não só no último!
<p><i>Para realizar este cálculo é necessário:</i></p>
- selecionar o decreto presidencial;<br/>
- se necessário, informe se deseja "Somar a fração do crime hediondo no saldo devedor".<br />
Utilizado quando o entendimento do juízo da comarca é de
que, para alcançar o requisito temporal, as frações devem ser somadas, ou seja, 2/3 do hediondo + 1/3 ou 1/4 dos comuns. Os decretos informam que
 o sentenciado tem que ter cumprido 2/3 do hediondo para ter direito no crime não impeditivo, no entanto, não informa se as frações devem ser 
somadas. Essa opção foi criada para atender a interpretação de somar 2/3 + 1/3 ou 1/4, do contrário, óbvio, não precisa marcar!
</label><br></fieldset><br/><br/>
						<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Indulto</legend>
<label style="text-align: justify">
<p>Para utilizar esta opção, observe primeiro o decreto e as suas frações, considerando que o Execpenweb, ainda não está desenvolvido para reconhecer os incisos do artigo 1º, dos decretos. O programa só faz o cálculo da fração que o analista considerar, portanto, estude o artigo 1º do decreto pertinente antes de elaborar este cálculo.</p>
<p>*O Indulto é um benefício para ser verificado e apreciado em todos os decretos, não só no último!</p>
<p><i>Para realizar este cálculo é necessário:</i></p>
- selecionar a fração do tempo total da condenação para o cálculo da data provável do indulto; 
</label><br></fieldset><br/><br/>
						<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Prescrição Retroativa/Intercorrente - Individual</legend>
<label style="text-align: justify">
<p>Cálculo utilizado quando houver determinação, considerando que esta opção deveria ter sido observada na ação penal. Ainda não foi desenvolvido o cálculo observando o art. 366 do CPP, a data de pronúncia e o trânsito MP.</p>
</label><br></fieldset><br/><br/>
						<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Prescrição Executória - Individual (art. 119 CP)</legend>
<label style="text-align: justify">
<p>Cálculo utilizado para observar a prescrição de cada pena, individualmente, após o respectivo trânsito em julgado com início de cumprimento de pena. Nesse caso, o sistema observa todos os lapsos temporais de fuga.</p>
<p>Caso não houve início do cumprimento da pena, marcar a opção "Não iniciou o cumprimento da pena". Desta maneira, o sistema observa a prescrição de cada pena, individualmente, após o respectivo trânsito em julgado sem início de cumprimento de pena.</p>
<p>* Quando o sentenciado tem mais de uma pena, esta é a opção correta, nos termos do art.119 do CP.</p>
</label><br></fieldset><br/><br/>
						<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Prescrição Executória - Unificada</legend>
<label style="text-align: justify">
<p>Cálculo utilizado para verificar a prescrição observando o restante da pena, conforme o art.113 do CP. Nesse caso, o cálculo levará em conta o quanto restou da pena até a última fuga do sentenciado, aplicando as regras do art.109 do CP.</p>
<p>*Mais indicado para utilizar quando há só uma pena!</p>
</label><br></fieldset><br/><br/>
						<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Progressão de Regime</legend>
<label style="text-align: justify">
<p>Cálculo utilizado para verificar o requisito temporal da progressão. Requer informação de data base, nos termos do art.112 da LEP ou interpretação judicial diversa.</p>
<p><i>Para realizar este cálculo é necessário:</i></p>
- selecionar a data base;<br/>
- se necessário, informe se deseja "considerar a REINCIDÊNCIA para todos os crimes hediondos existentes!<br />
Ressalta-se que a lei 11.464/2007 só fala em reincidência, não fala em específica. Desse modo, após o 1º trânsito em julgado do cálculo em questão, 
se o sentenciado comete um ou mais crimes considerado(os) hediondo(os), operará a reincidência e, a fração será 3/5 para o(os) hediondo(os). 
Se não for reincidente, a fração será 2/5." <br />
*Lembre-se que hediondos são todos os crimes previstos na Lei 8.072/90, mas para a progressão o fato deverá ter ocorrido a partir de 29.03.2007. 
Para todos os crimes cometidos antes dessa data, a fração será 1/6.
</label><br></fieldset><br/><br/>
						<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Livramento Condicional</legend>
<label style="text-align: justify">
<p>Cálculo utilizado para verificar o requisito temporal para o livramento condicional. O sistema reconhece a data base.</p>
<p><i>Para realizar este cálculo é necessário:</i></p>
- se necessário, informe se deseja "considerar a REINCIDÊNCIA ESPECÍFICA para todos os crimes hediondos existentes!"<br />
Lembrando que, se o sentenciado é reincidente em crime hediondo, ou seja, se ele cometeu um crime hediondo com condenação transitada em julgada 
e, após essa data de trânsito comete outro crime hediondo, ele é considerado reincidente específico e, não tem direito ao Livramento Condicional 
sobre essas condenações (art.83, V do CP). Assim, só vai ser fracionado nos cálculos, as penas dos demais crimes.
</label><br></fieldset><br/><br/>
						<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Pena Restritiva de Direito</legend>
<label style="text-align: justify">
<p>Cálculo utilizado para verificar o restante das horas de PSC (Prestação de Serviço à Comunidade) e/ou o provável término da pena da LFS (Limitação de Fim de Semana).</p>
<p>* Limitação de Fim de Semana são os casos em que o sentenciado vai cumprir pernoite nos finais de semana.</p>
</label><br></fieldset><br/><br/>
						<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Outros</legend>
<label style="text-align: justify">
<p>Utilizado para o cálculo das demais  modalidades instituídas pelas diversas interpretações acerca das penas restritivas de direitos, diversas da PSC e LFS, como Interdição Temporária de Direitos, Prestação Pecuniária e Sursis.
 Utilizado também para o cálculo manual, feito no campo de "Informações Adicionais".</p>
</label><br></fieldset><br/><br/>

					<br />
				</div>
			</form>
		</div>
	</body>
</html>