function compararData(campo1, campo2, msg) {
	if (comparaDatas(campo1, campo2)) {
		mostrarMensagemOk("Cadastro Processo Execucao", msg);
		return true;// data do campo 1 maior que data do campo 2
	} else
		return false;
}

function comparaDatas(campo1, campo2) {
	var data_1 = document.getElementById(campo1).value;
	var data_2 = document.getElementById(campo2).value;
	if (data_1 == "" || data_2 == "")
		return false;
	var Compara01 = parseInt(data_1.split("/")[2].toString()
			+ data_1.split("/")[1].toString() + data_1.split("/")[0].toString());
	var Compara02 = parseInt(data_2.split("/")[2].toString()
			+ data_2.split("/")[1].toString() + data_2.split("/")[0].toString());

	if (Compara01 > Compara02)
		return true; // data do campo 1 maior
	else
		return false; // data do campo 2 maior;
}

// fun��o que esconde e mostra linhas da tabela
function mostrarLinhaTabela(conteudo, imagem) {
	if (document.getElementById(conteudo).style.display == "none") {
		document.getElementById(conteudo).style.display = "block";
		imagem.src = "./imagens/execpen_icons/up.png";
	} else {
		document.getElementById(conteudo).style.display = "none";
		imagem.src = "./imagens/execpen_icons/down.png";
	}
}

function formataCampo(e, objCampo, intTamanho) {

	if (e.keyCode == 8 || e.keyCode == 46) // 8-Backspace e 46-Delete
	{
		objCampo.value = "";
		return false;
	}
	if (e.keyCode == 9) // 9- tecla TAB
	{
		// formId = objCampo.form;
		formId = document.forms[0];
		for (i = 0; i < formId.elements.length; i++) {
			if (objCampo.name == formId.elements[i].name
					&& i < formId.elements.length) {
				formId.elements[i].focus();
				return;
			}
		}
		return false;
	}

	// objCampo = campo a ser formatado
	// intTamanho = Quantidade de caracteres que o campo poder� aceitar

	// verifica tecla pressionada
	var str = objCampo.value;

	// Tamanho m�ximo permitido
	if (str.length > intTamanho - 1) {
		return false;
	}

	// para IE
	if (window.event) {
		keynum = window.event.keyCode;
	}
	// para Netscape/Firefox/Opera
	else if (e.which) {
		keynum = e.which;
	}

	// Data
	if ((keynum < 48 || keynum > 57)) {
		return false;
	}

	if ((str.length == 2) || (str.length == 5)) {
		objCampo.value = objCampo.value + '/';
	}
	return true;
}

// TJGO
function getTextoEditor() {
	var editor_data = CKEDITOR.instances.editor1.getData();
	if (editor_data == null)
		editor_data = '';
	return editor_data;
}

/**
 * Modifica o texto do editor
 * 
 * @author Ronneesley Moura Teles
 * @since 30/09/2008
 */
function setTextoEditor(texto) {
	CKEDITOR.instances.editor1.setData(texto);
}

function mostrarEditor(id) {
	var oEditor = FCKeditorAPI.GetInstance(id);
	texto = oEditor.GetXHTML();
	if (texto != "" && texto != null) {
		MostrarOcultar('divEditor');
	}
}

function resizeIframe(obj) {
    obj.style.height = obj.contentWindow.document.body.scrollHeight + 'px';
}

////onload="Posicionar();" onResize="Posicionar();"
//function AlterarTamanho( objeto){
//
//	//var a = document.getElementById("corpo").height;
//	var a = objeto.contentDocument.body.clientHeight + 73;						
//	var winW = 630, winH = 460;
//	
//	//winW = window.innerWidth;
//	winH = window.innerHeight;			
//
//	//alert(a);
//	if (a>winW){
//		objeto.height = a ;
//	}else{
//		objeto.height = winH - 73;
//	}
//	tempoAtual = tempoMaximo;			
//	
//}

function calcularTamanhoIframe() {
	var objeto = window.parent.document.getElementById('Principal');
	var cabecalhoTela = 75;
	if (objeto != null) {
		
		// A obtenção da altura usava clientHeight mas este parâmetro apresentou problema
		// na última versão do Firefox, aumentando a altura da tela de maneira desproporcional.
		// Abaixo, trocamos pelo uso do offsetHeight, que resolveu o problema. Adicionalmente,
		// precisamos a partir de então tratar especificamente páginas com o ckEditor.
		//var altura = objeto.contentDocument.body.clientHeight;
		
		// Identifica se existe um editor na página e redimensiona somando um tamanho fixo
		// para que o editor não fique cortado na tela.
		if(document.getElementById('Editor')) {
			var altura = objeto.contentDocument.body.offsetHeight + 400;
		}
		else {
			var altura = objeto.contentDocument.body.offsetHeight;
		}

		//essa é altura minima da tela, sempre que o contoeudo do cliente for menos que este, esse será usando
		//sempre que o conteudo for maior, ele será utilizado acrescido de 75px 
		var winH = window.innerHeight;
		//se a tela cabe o conteudo do cliente o iframe será do tamanho da tela (sem scroll)
		if ((winH-cabecalhoTela)>altura){			
			objeto.height = winH-cabecalhoTela;
		}else{
			//se a tela não cabe o conteudo do cliente o iframe será do tamanho do conteudo do cliente acrescido de 75 (cabeçalho da tela)
			objeto.height = altura+cabecalhoTela;
		}
				
	}
}

//function calcularTamanhoIframe() {
//	var objIframe = window.parent.document.getElementById('Principal');
//	if (objIframe != null) {
//		var objTela = window.document.body;
//		objIframe.height = objTela.clientHeight + 75;
//	}
//}
/*
 * *Fun��o para mostrar ou ocultar um div *o id1 e a div que ser� mostrada ou
 * ocultada *o id2 � a imagem que est� na div titulo mostrando o mais ou o menos
 */

function MostrarOcultarDiv(id1, id2, redimensionar) {
	var obj = document.getElementById(id1);
	var obj1 = document.getElementById(id2);
	if (obj != null && obj1 != null) {
		if (obj.style.display != "none") {
			obj.style.display = "none";
			obj1.src = "imagens/16x16/Mais.png";
		} else {
			obj.style.display = "block";
			obj1.src = "imagens/16x16/Menos.png";
		}
	}
	if (redimensionar == 1)
		calcularTamanhoIframe();
}

/**
 * M�todo usado para ocultar uma div e mostrar outra
 * 
 * @param idDivOcultar -
 *            ID da div que ser� ocultado
 * @param idDivMostrar -
 *            ID da div que ser� mostrada
 * @param redimensionar -
 *            se for redimensionar, colocar valor 1
 * @author hmgodinho
 */
function ocultarMostrarDivs(idDivOcultar, imgDivOcultar, idDivMostrar,
		imgDivMostrar, redimensionar) {
	var objOcultar = document.getElementById(idDivOcultar);
	var imgOcultar = document.getElementById(imgDivOcultar);
	var objMostrar = document.getElementById(idDivMostrar);
	var imgMostrar = document.getElementById(imgDivMostrar);
	if (objOcultar != null && objMostrar != null) {
		objOcultar.style.display = "none";
		imgOcultar.src = "imagens/16x16/Mais.png";
		objMostrar.style.display = "block";
		imgMostrar.src = "imagens/16x16/Traco.png";
	}
	if (redimensionar == 1)
		calcularTamanhoIframe();
}

/**
 * Melhorias nas funcoes, otimizacoes
 * 
 * @author Ronneesley Moura Teles
 * @since 22/08/2008 15:43
 */

/**
 * Apelido para a funcao
 * 
 * @author Ronneesley Moura Teles
 * @since 22/08/2008 15:43
 */
function av(id, valor) {
	AlterarValue(id, valor);
}

/**
 * Altera valores dos temps
 * 
 * @author Ronneesley Moura Teles
 * @since 22/08/2008 15:44
 */
function avTemp(pojo) {
	av('tempBuscaId_' + pojo, 'Id_' + pojo);
	av('tempBusca' + pojo, pojo);
}

/**
 * Altera o temp e a pagina atual
 * 
 * @author Ronneesley Moura Teles
 * @since 22/08/2008 15:45
 */
function avTempPA(pojo, pa) {
	// Altera os temps
	avTemp(pojo);

	// Altera a pagina atual
	av("PaginaAtual", pa);
}

// JavaScript Document
function AlterarValue(id, valor) {
	obj = document.getElementById(id);

	if (obj != null) obj.value = valor;
// if (obj == null)
// alert("Objeto de ID (" + id + ") n�o encontrado.");
// else
// obj.value = valor;
}

function setarValueNull(id) {
	obj = document.getElementById(id);

	if (obj != null) obj.value = 'null';
}

function AlterarValue2(id1, id2) {
	obj1 = document.getElementById(id1);
	obj2 = document.getElementById(id2);
	obj1.value = '<html><body>' + obj2.value + '</body></html>';
}

function MostrarOcultar(id) {
	var obj = document.getElementById(id);
	if (obj.style.display != "block") {
		obj.style.display = "block";
	} else {
		obj.style.display = "none";
	}
	calcularTamanhoIframe();
}

function Mostrar(id) {
	var obj = document.getElementById(id);
	if (obj!= null){
		obj.style.display = "block";
		calcularTamanhoIframe();
	}
}

function MostrarInline(id) {
	var obj = document.getElementById(id);
	if (obj!= null){
		obj.style.display = "inline";
		calcularTamanhoIframe();
	}
}

function Ocultar(id) {
	var obj = document.getElementById(id);
	if (obj!= null){
		obj.style.display = "none";
		calcularTamanhoIframe();
	}
}

function focarComponente(id) {
	var obj = document.getElementById(id);
	if (obj!= null){
		obj.focus();
	}
}

/**
 * Ao acionar, permite adicionar na área idArea reconhecer o enter e acionar o componente id
 */
function habilitaTelaENTERComponente(idArea, id) {
	var objArea = document.getElementById(idArea);
	objArea.addEventListener("keyup", function (event) {
		if( event.keyCode === 13 ) {
			event.preventDefault();
			document.getElementById(id).click();
		}
	});
}

function Imprimir() {
	if (!window.print) {
		alert("Use o Netscape  ou Internet Explorer \n nas vers�es 4.0 ou superior!");
		return;
	}
	window.print();
}

/**
 * Regras de validacoes, chamadas simples para os tratamentos
 * 
 * @author Ronneesley Moura Teles
 * @since 26/08/2008 14:59
 * @return boolean
 */
function SeNulo(objeto, mensagem) {
	if (objeto.value == "") {
		alert(mensagem); // Exibe a mensagem
		objeto.focus(); // Troca o foco
		return true;
	}

	return false;
}

///**
// * Funcao de limpar chave estrangueira
// * 
// * @author Ronneesley Moura Teles
// * @since 26/08/2008 17:00
// * @return void
// */
function LCE(id, campo) {
	av(id, "null");
	av(campo, "");
}

/**
 * Funcao de limpar chave estrangueira
 * 
 * @author Jesus Rodrigo
 * @since 6/6/2019
 * @return void
 */
function LimparChaveEstrangeira(field, id, desc) {
	if (desc==null){
		LCE(field,id);
	}else{
		$("fieldset#" +field+ " > input#"+id).val( "null");
		$("fieldset#" +field+ " > input#"+desc).val( "");
	}
}


function LimparCampo(id, campo) {
	av(id, "");
	av(campo, "");
}

/**
 * Funcao de limpar chave estrangueira e um Campo relacionado ao mesmo
 * 
 * @author Leandro de Souza Bernardes
 * @since 03/08/2009
 * @return void
 */
function LimparChaveEstrangeiraCampoRelacionado(id1, campo1, id2, campo2) {
	av(id1, "null");
	av(campo1, "");
	av(id2, "null");
	av(campo2, "");
}

/**
 * Limpar chave estrangueira, em casos comuns
 * 
 * @author Ronneesley Moura Teles
 * @since 26/08/2008 17:11
 * @return void
 */
function LimparCE(campo) {
	LimparChaveEstrangueira("Id_" + campo, campo);
}

/**
 * Formata um numero de processo
 * 
 * @author Ronneesley Moura Teles
 * @since 29/08/2008 09:19
 */
function formataNumeroProcesso(n) {
	if (n != null && n.length > 0) {
		if (n.length == 12)
			n = "00" + n;
		if (n.length == 13)
			n = "0" + n;
		n = n.substring(0, 3) + "." + n.substring(3, 7) + "."
				+ n.substring(7, 10) + "." + n.substring(10, 13) + "-"
				+ n.substring(13, 14);
	}

	return n;
}

/**
 * Limpar espacos no javascript
 * 
 * @author Ronneesley Moura Teles
 * @since 19/09/2008 10:51
 */
function trim(str) {
	return str.replace(/^\s+|\s+$/g, "");
}

/**
 * Altera o action de um form passado
 */
function AlterarAction(id, valor) {
	obj = document.getElementById(id);
	obj.action = valor;
}

/**
 * Troca o display de um elemento, ou seja, oculta quando esta visivel, e mostra
 * quando esta oculto
 * 
 * @author Ronneesley Moura Teles
 * @since 24/09/2008 14:11
 */
function trocarDisplay(elemento) {
	if (elemento.style.display == "none")
		elemento.style.display = "";
	else
		elemento.style.display = "none";

	calcularTamanhoIframe();
}
/**
 * Faz o submit de um formulario � necess�rio passar o id do mesmo
 * 
 * @author Jesus R Corr�a
 * @since 27/11/2008 14:18
 */
function FormSubmit(id) {
	var form = document.getElementById(id);
	form.submit();
}
/**
 * Verifica se o navegador � o IE
 * 
 * @author Jesus R Corr�a
 * @since 28/11/2008 09:15
 */
function isIe() {
	var ie = true;
	if (window.ActiveXObject) {
		ie = true;
	} else {
		ie = false;
	}

	return ie;
}
/**
 * Seleciona os dados escolhidos na tabela de localiza submete os dados
 * 
 * @author Jesus R Corr�a
 * @since 28/11/2008 13:32
 */
function selecionaSubmete(id, descricao) {
	AlterarValue('PaginaAtual', _PaginaEditar);
	AlterarValue(_tempBuscaId, id);
	AlterarValue(_tempBuscaDescricao, descricao);
	FormSubmit('Formulario');
}

function selecionaSubmeteJSON(id, descricao) {
	if(_Acao!=undefined && _Acao=="Editar"){
		AlterarValue('PaginaAtual', _PaginaEditar);
	}else{
		AlterarValue('PaginaAtual', _PaginaExcluir);
	}
	AlterarValue('tempBuscaId', id);
	AlterarValue('tempBuscaDescricao', descricao);
	FormSubmit('Formulario');
}

/**
 * Seleciona os dados escolhidos na tabela de localiza para a edi��o
 * 
 * @author Jesus R Corr�a
 * @since 01/12/2008 09:00
 */
function AlterarEditar(_id, _descricao) {
	AlterarValue('PaginaAtual', _PaginaEditar);
	AlterarValue(_tempBuscaId, _id);
	AlterarValue(_tempBuscaDescricao, _descricao);
}
/**
 * Seleciona os dados escolhidos na tabela de localiza para a exclus�o
 * 
 * @author Jesus R Corr�a
 * @since 01/12/2008 09:00
 */
function AlterarExcluir(_id) {
	AlterarValue('PaginaAtual', _PaginaExcluir);
	AlterarValue(_tempBuscaId, _id);
}

/**
 * Limpa o conte�do de uma div passada
 * 
 * @author Marielli de Paula
 * @since 03/04/2009 10:30
 */
function LimparDiv(id) {
	obj = document.getElementById(id);
	if (obj != null)
		obj.innerHTML = '';
}

/**
 * Captura a descri��o de um item do select e atribue ao campo passado
 */
function capturaTextoSelect(idSelect, idInput) {
	var obj = document.getElementById(idSelect);

	var obj1 = document.getElementById(idInput);
	if (obj.selectedIndex != -1) {
		obj1.value = obj[obj.selectedIndex].text;
	}
}

/**
 * Verifica se algum dos componentes radio est� selecionado
 */
function validarRadioButtons(componente, mensagem) {
	estaSelecionado = false;
	for (i = 0; i < componente.length; i++) {
		if (componente[i].checked) {
			estaSelecionado = true;
		}
	}
	if (!estaSelecionado) {
		alert(mensagem);
		return false;
	}
	return true;
}

function confirmaExclusao(mensagem, PaginaAtual, PassoEditar, posicao) {
	if (confirm(mensagem)) {
		if (PaginaAtual != '')
			AlterarValue('PaginaAtual', PaginaAtual);
		if (PassoEditar != '')
			AlterarValue('PassoEditar', PassoEditar);
		AlterarValue('posicaoLista', posicao);
	} else
		AlterarValue('PassoEditar', -1);
}

/**
 * Fun��o para habilitar ou desabilitar componente HTML na p�gina, caso deseje
 * tb limpar o valor � s� passar o valor true no segundo par�metro.
 * 
 * @param componente
 * @param boolean
 *            limparComponente
 */
function habilitaDesabilitaCampo(componente, limparComponente) {
	// Habilita ou Desabilita
	if (componente.disabled == true){
		componente.disabled = false;
		componente.value = "1";
	}
	else {
		componente.disabled = true;
	}

	// Limpa componente?
	if (limparComponente && componente.disabled == true) {
		componente.value = "";
	}
}

/**
 * Fun��o para criar a m�scara desejada no campo
 * 
 * @param src:
 *            componente
 * @param mascara:
 *            formato da m�scara, ex p/ cep: ##.###-###
 * @return
 */
function mascara(src, mascara) {
	var i = src.value.length;
	var saida = mascara.substring(0, 1);
	var texto = mascara.substring(i);
	if (texto.substring(0, 1) != saida) {
		src.value += texto.substring(0, 1);
	}
}

/* M�scaras ER */
function mascara1(o,f){
    v_obj=o;
    v_fun=f;
    setTimeout("execmascara()",1);
}
function execmascara(){
    v_obj.value=v_fun(v_obj.value);
}

function numeroProcessoCNJ(v){
    v=v.replace(/\D/g,"");// Remove tudo o que n�o � d�gito
    v=v.replace(/(\d)(\d{13})$/,"$1.$2");// coloca o ponto antes do 13�
											// digigo
	v=v.replace(/(\d)(\d{11})$/,"$1.$2");// coloca o ponto antes do 11�
											// digigo
	v=v.replace(/(\d)(\d{7})$/,"$1.$2");// coloca o ponto antes do 7� digigo
    v=v.replace(/(\d)(\d{6})$/,"$1.$2");// coloca o ponto antes do 6� digigo
    v=v.replace(/(\d)(\d{4})$/,"$1.$2");// coloca o ponto antes do 4� digigo
    return v;
}

function preencheZeros(param,tamanho) {  
   var contador = param.value.length;  
   if (param.value.length != tamanho) {  
      do {  
         param.value = "0" + param.value;  
         contador += 1;  
      }while (contador <tamanho);  
   }  
}


/**
 * Cron�metro regressivo para contar o tempo de sess�o do usu�rio
 * 
 * @param segundos
 *            dura��o da contagem
 * @param saida
 *            nome do campo que ser� atualizado com o contador
 * @return campo atualizado
 * @author asrocha
 */
function contadorSessao(saida) {
	tempoAtual--;
	if (tempoAtual > 0) {
		var ss = parseInt(tempoAtual); // Determina a quantidade total de
										// segundos que faltam
		var mm = parseInt(ss / 60); // Determina a quantidade total de minutos
									// que faltam
		var hh = parseInt(mm / 60); // Determina a quantidade total de horas que
									// faltam
		ss = ss - (mm * 60); // Determina a quantidade de segundos
		mm = mm - (hh * 60); // Determina a quantidade de minutos

		// O bloco abaixo monta o que vai ser escrito na tela
		var faltam = '<i class="fa fa-hourglass-1"></i> ';
		if (hh > 0)	faltam += hh + ':';
		faltam += mm < 10 ? '0' + mm + ':' : mm + ':';
		faltam += ss < 10 ? '0' + ss : ss;

		document.getElementById(saida).innerHTML = faltam; // Insere o conteudo
															// da vari?vel
															// faltam na p?gina
		setTimeout(function() {contadorSessao(saida);}, 1000); // Reinicia a
																// fun??o a cada
																// um segundo
	} else {
		document.getElementById(saida).innerHTML = 'Sua sess&atilde;o expirou!';
	}
}

/**
 * Altera os dados id e descricao da busca para o submit
 * 
 * @param id -
 *            Conteudo do Id da busca
 * @param descricao -
 *            da busca
 * @author jrcorrea
 */

function AlterarEditarJSON(id, descricao){
	AlterarValue('PaginaAtual', _PaginaEditar);
	AlterarValue('tempBuscaId', id);
	AlterarValue('tempBuscaDescricao', descricao);
}

/**
 * Altera os dados id e descricao da busca para o submit
 * 
 * @param id -
 *            Conteudo do Id da busca
 * @param descricao -
 *            da busca
 * @author jrcorrea
 */

function AlterarExcluirJSON(id, descricao){
	AlterarValue('PaginaAtual', _PaginaExcluir);
	AlterarValue('tempBuscaId', id);
	AlterarValue('tempBuscaDescricao', descricao);
}

function MarcarExcluirJSON(){
	_Acao = "Excluir";
}

/**
 * Cria a p�gina��o das buscas
 * 
 * @param url -
 *            qual ser� o ct
 * @param posicao -
 *            posi��o inicial
 * @param tamanho -
 *            tamanho do retorno
 * @param Funcao -
 *            A funcao que deve ser chamada
 * @param Mostrar
 *            Excluir - Se deve Mostrar o Excluir * *
 * @author jrcorrea
 */
	

function buscaDadosJSON(url, boMostrarExcluir, paginaAtual, qtdeNomeBusca, posicaoPaginaAtual, tamanho, idDivRetorno){
	
	var timer;
	var nomeBusca = "";
	var tabela;
	
	if(typeof idDivRetorno != 'undefined'){
		tabela = $('#'+idDivRetorno);
	} else {
		tabela = $('#CorpoTabela');
	}
	
	for(i=1;i<=qtdeNomeBusca;i++) {
		 nomeBusca += "&nomeBusca" + i + "=" + $("#nomeBusca"+ i).val();
	}
	if (posicaoPaginaAtual=="") posicaoPaginaAtual=$("#CaixaTextoPosicionar").val()-1;	
	var tempFluxo1 = $("#tempFluxo1").val();	
	var tempFluxo2 = $("#tempFluxo2").val(); 
	
	tabela.html('');
	var chamada = 'buscaDadosJSON(\''  + url + '\',' + boMostrarExcluir +  ',\'' + paginaAtual + '\',\'' +  qtdeNomeBusca + '\''  ; 
	
	var boErro = false;
	if(url.indexOf("?")>0){
		url = url+'&AJAX=ajax&Passo=1&PaginaAtual='+ paginaAtual + nomeBusca + '&PosicaoPaginaAtual=' + posicaoPaginaAtual + '&tempFluxo1=' + tempFluxo1 + '&tempFluxo2=' + tempFluxo2;
	} else{
		url = url+'?AJAX=ajax&Passo=1&PaginaAtual='+ paginaAtual + nomeBusca + '&PosicaoPaginaAtual=' + posicaoPaginaAtual + '&tempFluxo1=' + tempFluxo1 + '&tempFluxo2=' + tempFluxo2;
	}
	var boFecharDialog = false;
	$.ajax({
		url: encodeURI(url),
		context: document.body,
		timeout: 300000, async: true,
		success: function(retorno){
			var inLinha=1;			
			var totalPaginas =0;
			var corpoTabela = "";
			$.each(retorno, function(i,item){
				if(item.id=="-50000"){						
					// Quantidade p�ginas
					totalPaginas = item.desc1;
					
				}else if (item.id=="-60000"){
					// posi��o atual
					posicaoPaginaAtual = item.desc1;
				}else {
					
					var dataSelecoes="";
					var iColuna=2;
         			while((descricao=eval('item.desc' + iColuna))!=null) {         				
         				dataSelecoes += "desc" + iColuna + ";" + descricao + ";";
                     	iColuna++;
                	}			

         			corpoTabela += '<tr data_descs="'+ dataSelecoes +'" data_id1="' +  item.id  + '" data_desc1="' +  item.desc1 + '" class="MarcarLinha TabelaLinha' + inLinha + '">';
					corpoTabela +='<td   class="Centralizado" >' + (i-1) + '</td>';
					corpoTabela +='<td   class="Centralizado" >' + item.id + '</td>';
					corpoTabela +='<td    >' + item.desc1 + '</td>';
					iColuna=2;
         			while((descricao=eval('item.desc' + iColuna))!=null) {
         				corpoTabela +='<td >' + descricao + '</td>';
                     	iColuna++;
                	}        			
         			corpoTabela +='<td class="Centralizado" ><input name="formLocalizarimgEditar" type="image"  src="./imagens/imgEditar.png" data_descs="'+ dataSelecoes +'" data_id1="' +  item.id  + '" data_desc1="' +  item.desc1 + '" class="MarcarLinha" />   </td>';
				
         			if(boMostrarExcluir)		
         				corpoTabela +='<td class="Centralizado"><input name="formLocalizarimgexcluir" type="image" src="./imagens/imgExcluir.png" onClick="MarcarExcluirJSON()" /></td>';
					
         			corpoTabela +='</tr>';
	
					if (inLinha==1) inLinha=2; else inLinha=1;
				}
				
			});
			tabela.append(corpoTabela);
			// fun��o para dar o submite com o click em cada linha da tabela
			// busca os dados inseridos no tr
			$('.MarcarLinha').click(function(event){
					// event.target
				    var jtag = $(this);
					var id1 = jtag.attr("data_id1");
					var desc1 =  jtag.attr("data_desc1");
                   	var array = jtag.attr("data_descs").split(";");
                   	// fa�o as altera��es para cada desc maior que 1
                   	for (i=0; i<(array.length-1);i=i+2)                   		
                   		AlterarValue(array[i],array[i+1]);
                   	// submeto com um click na linha
					selecionaSubmeteJSON(id1,desc1);
				});
			// crio a pagina��o
			CriarPaginacaoJSON(chamada, posicaoPaginaAtual,totalPaginas, tamanho); 
		},
		beforeSend: function(data ){
			// Mostra a mensagem de "consultando" apenas se o tempo de consulta
			// exceder o do timer
			timer = setTimeout(function() {
				mostrarMensagemConsultando('Projudi - Consultando', "Aguarde, buscando os dados...")
				boFecharDialog=true;
			}, 1500);
			$("#formLocalizarBotao").hide();
		},
		error: function(request, status, error){
			boFecharDialog=false;
			// alert(request);
			// alert(status);
			// alert(error);
			if (error=='timeout'){
				mostrarMensagemErro('Tempo Excedido', "A a��o demorou tempo demais, tente novamente mais tarde.");
			}else{
				mostrarMensagemErro('Erro na Consulta', request.responseText);
			}
		},
		complete: function(data ){
			// Quando completar a consultar, previnir a mensagem de
			// "consultando" de ser mostrada
			clearTimeout(timer);
			if (boFecharDialog){
				$("#dialog").dialog('close');
			}
			
			$("#formLocalizarBotao").show();
		  }
	}); // fim do .ajax*/
			
}

function CriarPaginacaoJSON(chamada, posicaoPaginaAtual, totalPaginas, tamanho){
        var tempString;		
        
        var loTotalPaginas = parseInt(totalPaginas);
		var loPaginaAtual = parseInt(posicaoPaginaAtual); 
		var loTamanho = parseInt(tamanho);
		
		var loTotal = Math.ceil((loTotalPaginas / loTamanho));  								
		
		// Guarda a �ltima p�gina selecionada
		var loPaginaSelecionada= (loPaginaAtual+1);
		
		// determino onde vai come�ar a contagem de p�ginas
		var loPaginaInicial= loPaginaAtual - Math.floor((loTamanho / 2 ));
		if (loPaginaInicial<1) loPaginaInicial = 1;
		
		var loPaginaFinal = loPaginaInicial + loTamanho -1;
		
		if (loPaginaFinal > loTotal)
			loPaginaFinal = loTotal;
		
		if (loPaginaInicial > (loPaginaFinal - (loTamanho -1)))
			loPaginaInicial = loPaginaFinal - (loTamanho -1);
			
		if (loPaginaInicial<1) loPaginaInicial = 1;
		
		// se houver s� uma p�gina n�o gerar pagina��o
		if(loTotal==1){
			$("#Paginacao").html( "");
			calcularTamanhoIframe();
			return
		}
		tempString = "<b>P&aacute;gina <\/b>\n";
		tempString +="<a href=\"javascript:" + chamada + ",'0','" + loTamanho + "')\"> Primeira </a>";

		loPaginaAtual = loPaginaInicial;
		while(loPaginaAtual<=loPaginaFinal){	
			if (loPaginaAtual==loPaginaSelecionada){
				tempString+= "<b>| " + (loPaginaAtual) + " |<\/b>";
			} else {				
				tempString +="<a href=\"javascript:" + chamada + ",'" +(loPaginaAtual-1) + "','"  + loTamanho + "')\"> " + (loPaginaAtual)  + " </a>";
			}		
			loPaginaAtual++;			
		}		
		
		tempString +="<a href=\"javascript:" + chamada + ",'" +(loTotal-1) + "','"  + loTamanho + "')\"> &Uacute;ltima </a>";
		
		tempString+="<input id=\"CaixaTextoPosicionar\" value=\"" + (loTotal) + "\" class=\"CaixaTextoPosicionar\" type=\"text\" size=\"5\" maxlength=\"10\" /><input class=\"BotaoIr\" type=\"button\" value=\"Ir\" onclick=\"" + chamada + ",'','" + loTamanho + "' ); return false;\" /> <b> Total de: "+loTotalPaginas+"</b>";
		$("#Paginacao").html( tempString);
		calcularTamanhoIframe();
}

function CriarPaginacaoSimplesJSON(chamada, posicaoPaginaAtual, totalPaginas, tamanho){
    var tempString;		
    
    var loTotalPaginas = parseInt(totalPaginas);
	var loPaginaAtual = parseInt(posicaoPaginaAtual); 
	var loTamanho = parseInt(tamanho);
	
	var loTotal = Math.ceil((loTotalPaginas / loTamanho));  								
	
	// Guarda a �ltima p�gina selecionada
	var loPaginaSelecionada= (loPaginaAtual+1);
	
	// determino onde vai come�ar a contagem de p�ginas
	var loPaginaInicial= loPaginaAtual - Math.floor((loTamanho / 2 ));
	if (loPaginaInicial<1) loPaginaInicial = 1;
	
	var loPaginaFinal = loPaginaInicial + loTamanho -1;
	
	if (loPaginaFinal > loTotal)
		loPaginaFinal = loTotal;
	
	if (loPaginaInicial > (loPaginaFinal - (loTamanho -1)))
		loPaginaInicial = loPaginaFinal - (loTamanho -1);
		
	if (loPaginaInicial<1) loPaginaInicial = 1;
	
	if(loTotal==1){
		$("#Paginacao").html( "");
		calcularTamanhoIframe();
		return
	}
	tempString = "<b>P&aacute;gina <\/b>\n";
	tempString +="<a href=\"javascript:" + chamada + "'0','" + loTamanho + "')\"> Primeira </a>";

	loPaginaAtual = loPaginaInicial;
	while(loPaginaAtual<=loPaginaFinal){	
		if (loPaginaAtual==loPaginaSelecionada){
			tempString+= "<b>| " + (loPaginaAtual) + " |<\/b>";
		} else {				
			tempString +="<a href=\"javascript:" + chamada + "'" +(loPaginaAtual-1) + "','"  + loTamanho + "')\"> " + (loPaginaAtual)  + " </a>";
		}		
		loPaginaAtual++;			
	}		
	
	tempString +="<a href=\"javascript:" + chamada + "'" +(loTotal-1) + "','"  + loTamanho + "')\"> &Uacute;ltima </a>";
	
	tempString+="<input id=\"CaixaTextoPosicionar\" value=\"" + (loTotal) + "\" class=\"CaixaTextoPosicionar\" type=\"text\" size=\"5\" maxlength=\"10\" /><input class=\"BotaoIr\" type=\"button\" value=\"Ir\" onclick=\"" + chamada + "null,'" + loTamanho + "' ); return false;\" />";
	$("#Paginacao").html( tempString);
	calcularTamanhoIframe();
}


/**
 * Solicita uma confirma��o de uma opera��o
 * 
 * @param mensagem -
 *            PaginaAtual - tempFluxo1
 * @author Jelves
 */
function ConfirmaOperacao(mensagem, PaginaAtual, tempFluxo1) {
	if (confirm(mensagem)) {
		if (PaginaAtual != '')
			AlterarValue('PaginaAtual', PaginaAtual);
		if (tempFluxo1 != '')
			AlterarValue('tempFluxo1', tempFluxo1);
	} else
		AlterarValue('tempFluxo1', -1);
}

/**
 * Transforma um submit em ajax
 * 
 * @param id
 *            do formulario
 * @author Jesus Rodrigo
 */
function formularioAjax(id_formulario, apresentarDados, validarCampos){
	
		
		$("#" + id_formulario ).submit(function() {
			
			var timer;
			var dados =  this.action + "?";
			// pego todos os inputs de um formulario
			$.each($("#" + id_formulario +" input[type=hidden], input[type=text]"), function(i,item){	   					   				
				dados +=  item.name + "=" + item.value + "&" ; 
			});		
			
			dados = dados.substr(0,dados.length-1);
						
			// se houver problemas na valida��o do campos retorno
			if (!validarCampos()) return;
			var boFecharDialog = false;
			$.ajax({
				url: encodeURI(dados) ,
				context: document.body,
				timeout: 300000, async: true,
		        
				success: function(retorno){
					$("#" + id_formulario ).submit( null);					
					$("#" + id_formulario ).attr('id','Formulario');
					$("#" + id_formulario ).attr('name','Formulario');
					apresentarDados(retorno);
				},
				beforeSend: function(data ){
					// Mostra a mensagem de "consultando" apenas se o tempo de
					// consulta exceder o do timer
					timer = setTimeout(function() {
						mostrarMensagemConsultando('Projudi - Consultando', 'Aguarde, buscando os dados...');	
						boFecharDialog=true;
					}, 1500);
					$('#divConfirmarSalvar').hide();
				},
				error: function(request, status, error){
					boFecharDialog=false;
					if (error=='timeout'){
						mostrarMensagemErro('Tempo Excedido', "A a��o demorou tempo demais, tente novamente mais tarde.");
					}else{
						mostrarMensagemErro("Projudi - Erro", request.responseText);
					}				
				  }, 
				complete: function(data ){
					// Quando completar a consultar, previnir a mensagem de
					// "consultando" de ser mostrada
					clearTimeout(timer);
					if (boFecharDialog){
						$("#dialog").dialog('close');
					}
				  }
			}); // fim do .ajax*/
			return false;
		});
	
		
}

/**
 * Cria a p&Aacute;gina&ccedil;&atilde;o das buscas
 * 
 * @param url -
 *            qual ser� o ct
 * @param posicao -
 *            posi��o inicial
 * @param tamanho -
 *            tamanho do retorno
 * @param Funcao -
 *            A funcao que deve ser chamada
 * @param Mostrar
 *            Excluir - Se deve Mostrar o Excluir * *
 * @author jrcorrea
 */
	

function buscaDadosLogJSON(paginaAtual, posicaoPaginaAtual, tamanho){
	
	var timer;
	var tabela =  $('#CorpoTabela');
	var nomeBusca = "";

	if (posicaoPaginaAtual=="") posicaoPaginaAtual=$("#CaixaTextoPosicionar").val()-1;
	
	var id_log = $("#tempBuscaId").val();	
	var dataInicial = $("#dataInicial").val();
	var dataFinal = $("#dataFinal").val();
	var nomeTabela = $("#nomeTabela").val();
	var idTabela = $("#idTabela").val();
	var id_LogTipo = $("#id_LogTipo").val();
	var log_erro = $("#log_erro").prop( "checked" );
				
	tabela.html('');
	
	var chamada = 'buscaDadosLogJSON(\'' + paginaAtual  + '\'' ;
	var boFecharDialog = false;
	$.ajax({		
		url:  encodeURI('Log?AJAX=ajax&PaginaAtual=2&Passo=1&Id_Log=' + id_log + '&nomeTabela='+ nomeTabela + '&dataInicial='+ dataInicial + '&dataFinal='+ dataFinal + '&idTabela=' + idTabela + '&log_erro='+ log_erro + '&id_LogTipo='+ id_LogTipo + '&PosicaoPaginaAtual=' + posicaoPaginaAtual) ,
		context: document.body,
		timeout: 300000, async: true,
		success: function(retorno){
			var inLinha=1;			
			var totalPaginas =0;
			var corpoTabela = "";
			$.each(retorno, function(i,item){
				if(item.id=="-50000"){						
					// Quantidade p&Aacute;ginas
					totalPaginas = item.desc1;
					
				}else if (item.id=="-60000"){
					// posi&ccedil;&atilde;o atual
					posicaoPaginaAtual = item.desc1;
				}else {
							
         			corpoTabela +='<tr  data_id1="' +  item.id  + '" data_desc1="' +  item.desc1 + '" class="MarcarLinha TabelaLinha' + inLinha + '">';
					corpoTabela +='<td   class="Centralizado" >' + (i-1) + '</td>';
					corpoTabela +='<td   class="Centralizado" >' + item.id + '</td>';
					corpoTabela +='<td    >' + item.desc1 + '</td>';					         			
					corpoTabela +='<td    >' + item.desc2 + '</td>';
					corpoTabela +='<td    >' + item.desc3 + '</td>';
					corpoTabela +='<td    >' + item.desc4 + '</td>';
					corpoTabela +='<td    >' + item.desc5 + '</td>';
                  		
         			corpoTabela +='<td class="Centralizado" ><input name="formLocalizarimgEditar" type="image"  src="./imagens/imgEditar.png"  data_id1="' +  item.id  + '" data_desc1="' +  item.desc1 + '" class="MarcarLinha" />   </td>';				         			
					
         			corpoTabela +='</tr>';
	
					if (inLinha==1) inLinha=2; else inLinha=1;
				}
				
			});
			tabela.append(corpoTabela);
			// fun&ccedil;&atilde;o para dar o submit com o click em cada linha da tabela
			// busca os dados inseridos no tr
			$('.MarcarLinha').click(function(event){
					// event.target
				    var jtag = $(this);
					var id1 = jtag.attr("data_id1");
					var desc1 =  jtag.attr("data_desc1");
                   	
                   	// submeto com um click na linha
					selecionaSubmeteJSON(id1,desc1);
				});
			// crio a pagina&ccedil;&atilde;o
			CriarPaginacaoJSON(chamada, posicaoPaginaAtual,totalPaginas, tamanho); 
		},
		beforeSend: function(data ){
			// Mostra a mensagem de "consultando" apenas se o tempo de consulta
			// exceder o do timer
			timer = setTimeout(function() {
				mostrarMensagemConsultando('Projudi - Consultando', 'Aguarde, buscando os dados...');		
				boFecharDialog=true;
			}, 1500);
		},
		error: function(request, status, error){
			boFecharDialog=false;
			if (error=='timeout'){
				mostrarMensagemErro('Tempo Excedido', "A a��o demorou tempo demais, tente novamente mais tarde.");
			}else{
				mostrarMensagemErro("Projudi - Erro", request.responseText);
			}	
		  }, 
		complete: function(data ){
			// Quando completar a consultar, previnir a mensagem de
			// "consultando" de ser mostrada
			clearTimeout(timer);
			if (boFecharDialog){
				$("#dialog").dialog('close');
			}
		  }
	}); // fim do .ajax*/
			
}

/**
 * Limitar String
 * 
 * @param String
 * @Data 10/04/2014
 * @author jrcorrea
 */
function limitaString(texto, tamanho){
	var n = texto.length;	
	if (n>tamanho){
		texto = texto.substring(0,(tamanho-3)) + "...";		
	}
	return texto;
}


function DoPrintingApplet(numProcesso,nome,bairro,cidade,estado,logradouro,numero,cep,complemento){
	bairroCidadeUF = bairro+'  ,'+cidade+'-'+estado;
	ruaNumero = logradouro+'  N:'+numero;
	alert("Imprimindo Etiqueta!");	
	document.ImprimirEtiqueta.imprimirEtiqueta(numProcesso,nome,bairroCidadeUF,cep,ruaNumero,complemento);
}

function mostrarMensagemConsultando(titulo, mensagem) {
	
	var opt = { title: titulo, 
			autoOpen: false,
			modal: true, 
			position: {  at: "center top+250", of: window }, 						
			width: "350",
			height: "auto",
			buttons: [],
			show: { effect: "puff", duration: 500 },
			hide: { effect: "puff", duration: 250 }					
		  } ;
		
	$("#dialog").html(mensagem);
	$('#dialog').css({'background-image':'url("imagens/spinner.gif")','background-repeat':'no-repeat'});			
	$("#dialog").dialog(opt);
	$("#dialog").dialog('open');
}

function mostrarMensagemOk(titulo, mensagem) {
	
	var opt = { title: titulo, 
			autoOpen: false,
			modal: true, 
			position: { at: "center top+250", of: window }, 
			closeOnEscape:true,			
			width: "350",
			height: "auto",
			buttons: [{ text: "OK", click: function() { $( this ).dialog("close");}}],
			show: { effect: "puff", duration: 500 },
			hide: { effect: "puff", duration: 250 }					
		  } ;
		
	$("#dialog").html(mensagem);	
	$('#dialog').css({'background-image':'url("imagens/32x32/ico_informacao.png")','background-repeat':'no-repeat'});
	$("#dialog").dialog(opt);
	$("#dialog").dialog('open');
}

function mostrarMensagemErro(titulo, mensagem) {
	
	var opt = { title: titulo, 
			autoOpen: false,
			modal:true, 
			position: { at: "center top+250", of: window },
			closeOnEscape:true,			
			width: "350",
			height: "auto",
			buttons: [{ text: "OK", click: function() { $( this ).dialog("close");}}],
			show: { effect: "puff", duration: 500	 },
			hide: { effect: "puff", duration: 250 }					
		  } ;
	
	$("#dialog").html(mensagem);				
	$('#dialog').css({'background-image':'url("imagens/32x32/ico_erro.png")','background-repeat':'no-repeat'});
	$("#dialog").dialog(opt);
	$("#dialog").dialog('open');
}

function mostrarMensagemOkCallback(titulo, mensagem, callback) {
	
	var opt = { title: titulo, 
			autoOpen: false,
			modal: true, 
			position: { at: "center top+250", of: window }, 
			closeOnEscape:true,			
			width: "350",
			height: "auto",
			buttons: [{ text: "OK", click: function() { $( this ).dialog("close"); callback();}}],
			show: { effect: "puff", duration: 500 },
			hide: { effect: "puff", duration: 250 }					
		  } ;
		
	$("#dialog").html(mensagem);	
	$('#dialog').css({'background-image':'url("imagens/32x32/ico_informacao.png")','background-repeat':'no-repeat'});
	$("#dialog").dialog(opt);
	$("#dialog").dialog('open');
}

function mostrarMensagemErroCallback(titulo, mensagem, callback) {
	
	var opt = { title: titulo, 
			autoOpen: false,
			modal:true, 
			position: { at: "center top+250", of: window },
			closeOnEscape:true,			
			width: "350",
			height: "auto",
			buttons: [{ text: "OK", click: function() { $( this ).dialog("close"); callback();}}],
			show: { effect: "puff", duration: 500	 },
			hide: { effect: "puff", duration: 250 }					
		  } ;
	
	$("#dialog").html(mensagem);				
	$('#dialog').css({'background-image':'url("imagens/32x32/ico_erro.png")','background-repeat':'no-repeat'});
	$("#dialog").dialog(opt);
	$("#dialog").dialog('open');
}

/**
 * Cria a p&Aacute;gina&ccedil;&atilde;o das buscas
 * 
 * @param url -
 *            qual ser� o ct
 * @param posicao -
 *            posi��o inicial
 * @param tamanho -
 *            tamanho do retorno
 * @param Funcao -
 *            A funcao que deve ser chamada
 * @param Mostrar
 *            Excluir - Se deve Mostrar o Excluir * *
 * @author jrcorrea
 */
	

function mudarEstado(acao, paginaAtual, caso, variavel){
	$.ajax({
		  url: acao + '?AJAX=ajax&PaginaAtual=' + paginaAtual +'&caso=' + caso + '&variavel='+ variavel  ,
		  context: document.body,
		  error: function(request, status, error){
			  if (error=='timeout'){
					mostrarMensagemErro('Tempo Excedido', "A a��o demorou tempo demais, tente novamente mais tarde.");
				}else{
					mostrarMensagemErro("Projudi - Erro", request.responseText);
				}
				boErro=true;
			  }
		}).done(function() {
		  
		});					     		
}

/*******************************************************************************
 * M�todos do assinador
 */

function getTextoAssinar(){
 	var editor_data = CKEDITOR.instances.editor1.getData();  			
  	return editor_data;
}

function limparEditor(){
	CKEDITOR.instances.editor1.setData("");
	Ocultar("Editor");
}

function podeAssinar(){
  	var pode = $('#id_ArquivoTipo').attr('value');
  	if (pode != null && pode != 'null' && pode != '' ){
		 return 'true';	  			 
	}else {
		alert("Escolha o Tipo de Arquivo antes de assinar");
		return 'false';
  	}  	
}
  
function configuraAssinador(){
	return "Adicionar=true;Limpar=true";
}
	
function iniciarAssinatura(){	   	   	  
   arraynome=[];
   arrayarquivo=[];
}

/*
 * Fim dos m�todos do assinador
 */

function uncheckedRadio(name) {

	var obj = document.getElementsByName(name);

	for(var i=0;i<obj.length;i++){
		obj[i].checked = false;
	}	
}

function uncheckedVariosRadio() {
    for (var i = 0; i < arguments.length; i++) {
    	var idCampo = document.getElementsByName(arguments[i]);
    	for(var j=0;j<idCampo.length;j++){
    		idCampo[j].checked = false;
    	}	
    }
}

function checkedRadio(name) {

	var obj = document.getElementsByName(name);

	for(var i=0;i<obj.length;i++){
		obj[i].checked = true;
	}	
}

function checkedVariosRadio() {
    for (var i = 0; i < arguments.length; i++) {
    	var idCampo = document.getElementsByName(arguments[i]);
    	for(var j=0;j<idCampo.length;j++){
    		idCampo[j].checked = true;
    	}	
    }
}

function ocultarVarios() {
    var i;
    for (i = 0; i < arguments.length; i++) {
    	var idCampo = document.getElementById(arguments[i]);
    	if (idCampo != null){
    		idCampo.style.display = 'none';
    	}
    }
}

function mostrarVarios() {
    var i;
    for (i = 0; i < arguments.length; i++) {
    	var idCampo = document.getElementById(arguments[i]);
    	if (idCampo != null){
    		idCampo.style.display = 'block';
    	}
    }
}

function mostrarVariosInline() {
    var i;
    for (i = 0; i < arguments.length; i++) {
    	var idCampo = document.getElementById(arguments[i]);
    	if (idCampo != null){
    		idCampo.style.display = 'inline';
    	}
    }
}

function limparForm(idForm){
	$("#" + idForm +" input[type=text]").val('');
}



/*-------------------------------------
--Inicio da Anota��es de Processo
---------------------------------------*/
var tempoSalvarNota= "";
var mapa={};

function buscaNotasProcesso(){
		
	var url = 'Processo?AJAX=ajax&Passo=1&PaginaAtual=1';
		
	$.ajax({
		url: encodeURI(url),
		context: document.body,
		timeout: 300000, async: true,
		success: function(retorno){			
			$.each(retorno, function(i,item){
				criarNota(item.id_proc_nota, item.proc_nota, item.id_serv);				
			});									
		},
		beforeSend: function(data ){},
		error: function(request, status, error){ 
			if (error=='timeout'){
				mostrarMensagemErro('Tempo Excedido', "A a&ccedil;&atilde;o demorou tempo demais, tente novamente mais tarde.");
			}else{
				mostrarMensagemErro("Projudi - Erro", request.responseText);
			}
		},
		complete: function(data ){	  }
	}); // fim do .ajax*/
		
}

function excluirNotaProcesso(id, isPrivada){
	
	var id_banco = id.replace('#PIApostit_', '#idPostIt_');
	id_nota = $(id_banco).attr('id_banco');
	var url = 'Processo?AJAX=ajax&Passo=3&PaginaAtual=1&id_nota='+id_nota+'&isPrivada='+isPrivada;
	
	$.ajax({
		url: encodeURI(url),
		context: document.body,
		timeout: 300000, async: true,
		success: function(retorno){},			
		beforeSend: function(data ){},
		error: function(request, status, error){ mostrarMensagemErro('Erro na exclus�o da anota&ccedil;&atilde;o', request.responseText);},
		complete: function(data ){	  }
	}); // fim do .ajax*/
}

function mudarNota(id){
    // pego o texto para ver se houve altera��o no texto
    var id_conteudo = id.replace('#PIApostit_', '#pia_editable_');
    var id_banco = id.replace('#PIApostit_', '#idPostIt_');
	var textoAnterior = $(id_banco).attr('valor');
    var textoAtual = $(id_conteudo).html()      
    
	if (textoAnterior!=undefined && textoAtual !="Digite a sua nota aqui<br>Salvo automaticamente a cada 2seg." &&  textoAtual !=textoAnterior){
	    clearTimeout(mapa[id]);
	    mapa[id] =  setTimeout(function() {salvar(id)}, 2000);	    
	}
	$(id_banco).attr('valor',textoAtual);
}

function prepararNota(id, id_nota, id_serv_nota, isPrivada){
	var id_conteudo = id.replace('#PIApostit_', '#pia_editable_');
    var id_banco = id.replace('#PIApostit_', '#idPostIt_');
	
    var textoAtual = $(id_conteudo).html();			
	$(id_banco).attr('valor',textoAtual);
    $(id_banco).attr('id_banco', id_nota );
    $(id_banco).attr('id_serv_nota', id_serv_nota );
    $(id_banco).attr('isPrivada', isPrivada );
}

function criarNota(id_nota, nota, id_serv_nota, isPrivada){
	if(id_nota==null || id_nota== undefined){
		id_nota="";
	}
	if(nota==null || nota== undefined){
		nota= "Digite a sua nota aqui<br>Salvo automaticamente a cada 2seg.";
	}
	// http://postitall.txusko.com/plugin.html#doc_newnote
	if (id_serv_nota!=0 && id_serv_nota!=null){
		// nota do cart&oacute;rio
			$.PostItAll.new({id:id_nota, content: nota,
				style : { backgroundcolor : '#7dcea0'},
				onCreated: function(id, options, obj) { prepararNota(id, id_nota, id_serv_nota, isPrivada)	},									        		    
			    onChange: function(id) {mudarNota(id);	    },
			    onDelete: function(id) {excluirNotaProcesso(id, isPrivada);},
		});		
	}else{
		// nota particular
		$.PostItAll.new({id:id_nota, content: nota,
				onCreated: function(id, options, obj) { prepararNota(id, id_nota, id_serv_nota, isPrivada)	},									        		    
			    onChange: function(id) {mudarNota(id);	    },					    
			    onDelete: function(id) {excluirNotaProcesso(id, isPrivada);},
		});
	}
}

function salvar(id){				
	var id_conteudo = id.replace('#PIApostit_', '#pia_editable_');
	var id_banco = id.replace('#PIApostit_', '#idPostIt_');

	var nota = $(id_conteudo).html();
	var id_nota = $(id_banco).attr('id_banco');
	var id_serv_nota = $(id_banco).attr('id_serv_nota');
	
	
	var isPrivada = $(id_banco).attr('isPrivada');
	
	clearTimeout(mapa[id]);
	
	var url = 'Processo?AJAX=ajax&Passo=2&PaginaAtual=1';		
	
	var dados =  { "id_nota": id_nota, "nota": nota, "id_serv_nota": id_serv_nota, "isPrivada": isPrivada } ;
	$.ajax({
		type: "POST",
		data: dados,
		url: encodeURI(url),
		context: document.body,
		timeout: 300000, async: true,
		success: function(retorno){			
			$(id_banco).attr('id_banco', retorno.id );
		},			
		beforeSend: function(data ){},
		error: function(request, status, error){ 
			if (error=='timeout'){
				mostrarMensagemErro('Tempo Excedido', "A a&ccedil;&atilde;o demorou tempo demais, tente novamente mais tarde.");
			}else{
				mostrarMensagemErro("Projudi - Erro", request.responseText);
			}
		},
		complete: function(data ){
						
		}
	}); // fim do .ajax*/
}
/*-------------------------------------
--Fim da Anota��es de Processo
---------------------------------------*/
function converterParaAnoMesDia(tempPenaEmDias) {
	  var diasNegativo = false;
	  if (tempPenaEmDias < 0) diasNegativo = true;
	  
	  var tempo = Math.abs(tempPenaEmDias); // coloca a qtde de dias como
											// positivo
	  var ano = 0;
	  
	  while( 365.25 <= tempo) { // Dia Para Ano
	  ano++; 
	  tempo -= 365.25;
	  }
			
	  var mes = 0;
		
		while( 30.4375 <= tempo) { // Dia Para Mes
			mes++;
			tempo -=30.4375;
		}
			
		var dia = tempo;
		tempPenaEmDias = Math.round(dia);
			
		if (tempPenaEmDias >= 30) {
			mes += 1;
			tempPenaEmDias = 0;
		}
		
		if (mes == 12) {
			ano += 1;
			mes -= 12;
		}
	  
	  var retorno;
	  
	  if (ano < 10)
	    retorno = "0" + String(ano) + " - ";
	  else
	    retorno = String(ano) + " - ";
	    
	  retorno += "00".substr(1, 2 - String(mes).length) + mes + " - "; 
	  retorno += "00".substr(1, 2 - String(dia).length) + tempPenaEmDias;
	  
	  stRetorno = String(retorno);
	  
	  if (diasNegativo)
	   stRetorno = "(" + stRetorno + ")";
		
	  return stRetorno;
	}

	function replaceAll(str, de, para){
		return str.replace(new RegExp(de, 'g'), para);
	}
	
	function buscaDadosModal(url, paginaAtual, qtdeNomeBusca, posicaoPaginaAtual, tamanho, id, desc, outrasDescs, fieldName, filtro){
		
		var timer;
		var nomeBusca = "";
		var tabela;
		var id_atual = id;
		var desc_atual = desc;
		
		tabela = $('#CorpoTabela');
		
		for(i=1;i<=qtdeNomeBusca;i++) {
			 nomeBusca += "&nomeBusca" + i + "=" + $("#nomeBusca"+ i).val();
		}
		if (posicaoPaginaAtual=="") posicaoPaginaAtual=$("#CaixaTextoPosicionar").val()-1;	
		var tempFluxo1 = $("#tempFluxo1").val();	
		var tempFluxo2 = $("#tempFluxo2").val(); 
		
		tabela.html('');
		var chamada = 'buscaDadosModal(\''  + url + '\',\'' + paginaAtual + '\',\'' +  qtdeNomeBusca + '\''  ; 
		
		var boErro = false;
		if(url.indexOf("?")>0){
			url = url+'&AJAX=ajax&Passo=1&PaginaAtual='+ paginaAtual + nomeBusca + '&PosicaoPaginaAtual=' + posicaoPaginaAtual + '&tempFluxo1=' + tempFluxo1 + '&tempFluxo2=' + tempFluxo2+ '&filtroTabela=' + filtro;
		} else{
			url = url+'?AJAX=ajax&Passo=1&PaginaAtual='+ paginaAtual + nomeBusca + '&PosicaoPaginaAtual=' + posicaoPaginaAtual + '&tempFluxo1=' + tempFluxo1 + '&tempFluxo2=' + tempFluxo2+ '&filtroTabela=' + filtro;
		}
		var boFecharDialog = false;
		$.ajax({
			url: encodeURI(url),
			context: document.body,
			timeout: 300000, async: true,
			success: function(retorno){
				var inLinha=1;			
				var totalPaginas =0;
				var corpoTabela = "";
				$.each(retorno, function(i,item){
					if(item.id=="-50000"){						
						// Quantidade p�ginas
						totalPaginas = item.desc1;
						
					}else if (item.id=="-60000"){
						// posi��o atual
						posicaoPaginaAtual = item.desc1;
					}else {
						
						var dataSelecoes="";
						var iColuna=2;
	         			while((descricao=eval('item.desc' + iColuna))!=null) {         				
	         				dataSelecoes += descricao + ";";
	                     	iColuna++;
	                	}			

	         			corpoTabela += '<tr data_descs="'+ dataSelecoes +'" data_id1="' +  item.id  + '" data_desc1="' +  item.desc1 + '" class="MarcarLinha TabelaLinha' + inLinha + '">';
						corpoTabela +='<td class="Centralizado" >' + (i-1) + '</td>';
						corpoTabela +='<td class="Centralizado" >' + item.id + '</td>';
						corpoTabela +='<td align="left">' + item.desc1 + '</td>';
						iColuna=2;
	         			while((descricao=eval('item.desc' + iColuna))!=null) {
	         				corpoTabela +='<td align="left">' + descricao + '</td>';
	                     	iColuna++;
	                	}        			
	         			//corpoTabela +='<td class="Centralizado" ><input name="formLocalizarimgEditar" type="image"  src="./imagens/imgEditar.png" data_descs="'+ dataSelecoes +'" data_id1="' +  item.id  + '" data_desc1="' +  item.desc1 + '" class="MarcarLinha" />   </td>';
	         			corpoTabela +='</tr>';
		
						if (inLinha==1) inLinha=2; else inLinha=1;
					}
					
				});
				tabela.append(corpoTabela);
				// fun��o para dar o submite com o click em cada linha da tabela
				// busca os dados inseridos no tr
				$('.MarcarLinha').click(function(event){
						// event.target
					    var jtag = $(this);
						var id1 = jtag.attr("data_id1");
						var desc1 =  jtag.attr("data_desc1");
	                   	var array = jtag.attr("data_descs").split(";");
	                   	// fa�o as altera��es para cada desc maior que 1
	                   	for (i=0;outrasDescs!=undefined && i<(outrasDescs.length);i++)
	                   		if(outrasDescs[i] != '') {
	                   			$('#' + fieldName + ' input#'+ outrasDescs[i]).val(array[i]);
	                   		}
	                   	// submeto com um click na linha
	                   	$('#' + fieldName + ' input#'+id_atual).val(id1);
	                   	$('#' + fieldName + ' input#'+desc_atual).val(desc1);
	                   	modalClose("busca_padrao");
	                   	$('#' + fieldName + ' input#'+desc_atual).click();
					});
				// crio a pagina��o
				CriarPaginacaoModal(chamada, posicaoPaginaAtual,totalPaginas, tamanho, id, desc, outrasDescs, fieldName, filtro); 
			},
			beforeSend: function(data ){
				// Mostra a mensagem de "consultando" apenas se o tempo de consulta
				// exceder o do timer
				timer = setTimeout(function() {
					mostrarMensagemConsultando('Projudi - Consultando', "Aguarde, buscando os dados...")
					boFecharDialog=true;
				}, 1500);
				$("#formLocalizarBotao").hide();
			},
			error: function(request, status, error){
				boFecharDialog=false;
				// alert(request);
				// alert(status);
				// alert(error);
				if (error=='timeout'){
					mostrarMensagemErro('Tempo Excedido', "A a��o demorou tempo demais, tente novamente mais tarde.");
				}else{
					mostrarMensagemErro('Erro na Consulta', request.responseText);
				}
			},
			complete: function(data ){
				// Quando completar a consultar, previnir a mensagem de
				// "consultando" de ser mostrada
				clearTimeout(timer);
				if (boFecharDialog){
					$("#dialog").dialog('close');
				}
				
				$("#formLocalizarBotao").show();
			  }
		}); // fim do .ajax*/
				
	}
	
	function CriarPaginacaoModal(chamada, posicaoPaginaAtual, totalPaginas, tamanho, id, desc, outrasDescs, fieldName, filtro){
        var tempString;		
        
        var loTotalPaginas = parseInt(totalPaginas);
		var loPaginaAtual = parseInt(posicaoPaginaAtual); 
		var loTamanho = parseInt(tamanho);
		
		var loTotal = Math.ceil((loTotalPaginas / loTamanho));
		
		// Guarda a �ltima p�gina selecionada
		var loPaginaSelecionada= (loPaginaAtual+1);
		
		// determino onde vai come�ar a contagem de p�ginas
		var loPaginaInicial= loPaginaAtual - Math.floor((loTamanho / 2 ));
		if (loPaginaInicial<1) loPaginaInicial = 1;
		
		var loPaginaFinal = loPaginaInicial + loTamanho -1;
		
		if (loPaginaFinal > loTotal)
			loPaginaFinal = loTotal;
		
		if (loPaginaInicial > (loPaginaFinal - (loTamanho -1)))
			loPaginaInicial = loPaginaFinal - (loTamanho -1);
			
		if (loPaginaInicial<1) loPaginaInicial = 1;
		var _outrasDescs;
		
		if (outrasDescs!=undefined && outrasDescs.length>0){
			_outrasDescs= "\'"+outrasDescs[0] +"\'";
			for (i=1;i<(outrasDescs.length);i++){
				_outrasDescs+=	",\'"+outrasDescs[i] +"\'";
			}				
			_outrasDescs='['+_outrasDescs+']';
		} else{
			_outrasDescs='[]';
		}
		// se houver s� uma p�gina n�o gerar pagina��o
		if(loTotal==1){
			$("#PaginacaoBuscaPadrao").html( "");
			calcularTamanhoIframe();
			return
		}
		tempString = "<b>P&aacute;gina <\/b>\n";				
		tempString +="<a href=\"javascript:" + chamada + ",'0','"  + loTamanho + "','"  + id + "','"  + desc + "',"+ _outrasDescs + ",'"+ fieldName + "','"+ filtro + "')\"> Primeira </a>";
		
		loPaginaAtual = loPaginaInicial;
		while(loPaginaAtual<=loPaginaFinal){	
			if (loPaginaAtual==loPaginaSelecionada){
				tempString+= "<b>| " + (loPaginaAtual) + " |<\/b>";
			} else {				
				tempString +="<a href=\"javascript:" + chamada + ",'" +(loPaginaAtual-1) + "','"  + loTamanho + "','"  + id + "','"  + desc + "',"+ _outrasDescs + ",'"+ fieldName + "','"+ filtro+ "')\"> " + (loPaginaAtual)  + " </a>";
			}		
			loPaginaAtual++;			
		}		
				
		tempString +="<a href=\"javascript:" + chamada + ",'" +(loTotal-1) + "','"  + loTamanho + "','"  + id + "','"  + desc + "',"+ _outrasDescs + ",'"+ fieldName + "','" + filtro + "')\">  &Uacute;ltima  </a>";
		
		tempString+="<input id=\"CaixaTextoPosicionar\" value=\"" + (loTotal) + "\" class=\"CaixaTextoPosicionar\" type=\"text\" size=\"5\" maxlength=\"10\" /><input class=\"BotaoIr\" type=\"button\" value=\"Ir\" onclick=\"" + chamada + ",'','" + loTamanho  + "','"  + id + "','"  + desc + "','"+ _outrasDescs + "','"+ fieldName + "','"+ filtro + "'); return false;\" />  <b> Total de: "+loTotalPaginas+"</b>";
		$("#PaginacaoBuscaPadrao").html( tempString);
		//calcularTamanhoIframe();
}

//*--------------------SSP
	function salvarPasso(controle, id_form, titulo_func_info, titulo_func_editar, podeExcluir){
		
		var url = controle+'?AJAX=ajax&Passo='+id_form+'&PaginaAtual=-2';		
		
		var dados = {};
		$("#" + id_form + " input").each(function() {	    
	    	if ($(this).attr("name")!=undefined ){
	    		dados [$(this).attr("name")] = $(this).val();
	    	}
	        //dados.push(item);
	    });

		$("#" + id_form + " select ").each(function() {			
	    	if ($(this).attr("name")!=undefined ){				
	    		dados [$(this).attr("name")] = $(this).val();
	    	}
	        //dados.push(item);
	    });

	    //var html_dbody = $("#CorpoTabela_"+id_form + " tbody").html();
	    
	    //console.log(dados);
		//var dados =  { "id_nota": id_nota, "nota": nota, "id_serv_nota": id_serv_nota, "isPrivada": isPrivada } ;
		
		$.ajax({
			type: "POST",
			data: dados,
			url: encodeURI(url),
			context: document.body,
			timeout: 300000, async: true,
			success: function(retorno){							
				var stLinha ="";
				//campos='{"Id_PessoaApelido":"<%=dt.getId()%>","Apelido":"<%=dt.getApelido()%>"}' >
			    $("#" + id_form + " input").each(function() {	    
			    	if ($(this).attr("name")!=undefined ){			    	
			    		//limpos os campos da tela que n&atilde;o est&atilde;o ocultos
			    		if($(this).attr("type")!="hidden"){
			    			$(this).val("");
			    		}
			    	}
			        //dados.push(item);
			    });
				$("#" + id_form + " select ").each(function() {			
			    	if ($(this).attr("name")!=undefined ){				
			    		if($(this).attr("type")!="hidden"){
			    			$(this).val("");
			    		}
			    	}
			        //dados.push(item);
			    });
				stLinha +="<tr id='tr_" + retorno.Id + "' campos='" + JSON.stringify(retorno) + "'>";
				stLinha +="<td class=\"colunaMinima\">"+retorno.Id +"</td>";
				//var rowCount = $("table#tb_"+id_form + " tbody tr").length;
				var nomes = Object.keys(retorno);
				var valores = Object.values(retorno);
				for(var i=0; i<valores.length && i<=6;i++){
					//n&atilde;o vou mostrar os ids nas colunas da tabela
					if (nomes[i]!="Id" && nomes[i].toLowerCase().indexOf('id_')<0){
						stLinha +="<td>"+ valores[i] + "</td>";
					}
				}
				if(titulo_func_info!=undefined){
					stLinha +="<td class=\"colunaMinima\" title=\"Seleciona o registro para detalhes\"> <img src=\"./imagens/important_blue.png\" alt=\"Info\" onclick=\"infoPasso('"+id_form+"','"+retorno.Id+"','"+titulo_func_info+"')\"</td>";
				}
				if(titulo_func_editar!=undefined){
					stLinha +="<td class=\"colunaMinima\" title=\"Seleciona o registro para edi&ccedil;&atilde;o\"><img src=\"./imagens/imgEditar.png\" alt=\"Editar\" onclick=\"editarPasso('"+id_form+"','"+retorno.Id+"')\"</td>";				
				}				
				if(podeExcluir!=undefined){
					stLinha +="<td class=\"colunaMinima\" title=\"Exluir o registro\" ><img src=\"./imagens/imgExcluir.png\" alt=\"remover\" onclick=\"excluirPasso('"+controle+"','"+id_form+"','"+retorno.Id+"')\"/></td>";
				}
				stLinha +="</tr>";				
				$("table#tb_"+id_form + " tbody").append(stLinha);
				//console.log( stLinha);
			},			
			beforeSend: function(data ){},
			error: function(request, status, error){ 
				if (error=='timeout'){
					mostrarMensagemErro('Tempo Excedido', "A a&ccedil;&atilde;o demorou tempo demais, tente novamente mais tarde.");
				}else{
					mostrarMensagemErro("Projudi - Erro", request.responseText);
				}
			},
			complete: function(data ){
							
			}
		}); // fim do .ajax*/
	}
	
	function atualizarTabelaPasso(controle, id_form, titulo_func_info, titulo_func_editar, podeExcluir){
		
		var url = controle+'?AJAX=ajax&Passo='+id_form+'&PaginaAtual=2';		
		
		var dados = {};
		$("#" + id_form + " input").each(function() {	    
	    	if ($(this).attr("name")!=undefined ){
	    		dados [$(this).attr("name")] = $(this).val();
	    	}
	        //dados.push(item);
	    });
	    		
		$.ajax({
			type: "POST",
			data: dados,
			url: encodeURI(url),
			context: document.body,
			timeout: 300000, async: true,
			success: function(retorno){							
				var stLinha ="";
				$.each(retorno, function(i,item){
					stLinha +="<tr id='tr_" + item.Id + "' campos='" + JSON.stringify(item) + "'>";
					stLinha +="<td class=\"colunaMinima\">"+item.Id +"</td>";
					//var rowCount = $("table#tb_"+id_form + " tbody tr").length;
					var nomes = Object.keys(item);
					var valores = Object.values(item);
					for(var i=0; i<valores.length && i<=6;i++){
						//n&atilde;o vou mostrar os ids nas colunas da tabela
						if (nomes[i]!="Id" && nomes[i].toLowerCase().indexOf('id_')<0){
							stLinha +="<td>"+ valores[i] + "</td>";
						}
					}
					if(titulo_func_info!=undefined){
						stLinha +="<td class=\"colunaMinima\" title=\"Seleciona o registro para detalhes\"> <img src=\"./imagens/important_blue.png\" alt=\"Info\" onclick=\"infoPasso('"+id_form+"','"+item.Id+"','"+titulo_func_info+"')\"</td>";
					}
					if(titulo_func_editar!=undefined){
						stLinha +="<td class=\"colunaMinima\" title=\"Seleciona o registro para edi&ccedil;&atilde;o\"><img src=\"./imagens/imgEditar.png\" alt=\"Editar\" onclick=\"editarPasso('"+id_form+"','"+item.Id+"')\"</td>";				
					}
					if(podeExcluir!=undefined){
						stLinha +="<td class=\"colunaMinima\" title=\"Exluir o registro\" ><img src=\"./imagens/imgExcluir.png\" alt=\"remover\" onclick=\"excluirPasso('"+controle+"','"+id_form+"','"+item.Id+"')\"/></td>";
					}
					stLinha +="</tr>";	
				});
				$("table#tb_"+id_form + " tbody").html(stLinha);
				//console.log( stLinha);
			},			
			beforeSend: function(data ){},
			error: function(request, status, error){ 
				if (error=='timeout'){
					mostrarMensagemErro('Tempo Excedido', "A a&ccedil;&atilde;o demorou tempo demais, tente novamente mais tarde.");
				}else{
					mostrarMensagemErro("Projudi - Erro", request.responseText);
				}
			},
			complete: function(data ){
							
			}
		}); // fim do .ajax*/
	}
	
	function salvarFotoPasso(controle, id_form, id_pessoa ){
		//https://makitweb.com/how-to-upload-image-file-using-ajax-and-jquery/
		
	    var fd = new FormData();
	    var files = $("#" +id_form+ " > input[type='file']")[0].files[0];
	    fd.append('foto',files);
	    fd.append('Id_Pessoa',id_pessoa);
	    var url = controle+'?AJAX=ajax&Passo='+id_form+'&PaginaAtual=-2&';
	    
	    $.ajax({
	        url: url,
	        type: 'post',
	        encType : "multipart/form-data",
	        data: fd,
	        contentType: false,
	        processData: false,
	        timeout: 300000, 
	        async: true,
	        cache : false,
	        success: function(retorno){
				var stFoto ='<td id="td_foto'+retorno.Id+'" >'; 
					stFoto +='<img id="foto'+retorno.Id+'" src="'+ URL.createObjectURL(files)+'" alt='+retorno.DataCadastro+' onclick="infoFoto('+retorno.id+')" class="foto">';
					stFoto +="<img src=\"./imagens/imgExcluir.png\" alt=\"remover\" onclick=\"excluirPasso('"+controle+"','"+id_form+"','"+retorno.Id+"')\"/>";
					stFoto +='</td>';
					$('tr#tr_fotos').prepend(stFoto);
	        },
			error: function(request, status, error){ 
				if (error=='timeout'){
					mostrarMensagemErro('Tempo Excedido', "A a&ccedil;&atilde;o demorou tempo demais, tente novamente mais tarde.");
				}else{
					mostrarMensagemErro("Projudi - Erro", request.responseText);
				}
			},
	    });
	}
	
	function excluirPasso(controle, id_form, id){
		
		var url = controle+'?AJAX=ajax&Passo='+id_form+'&PaginaAtual=-3&Id='+id;
		
		var r = confirm("Confirma a Exclusão do Registro?");
		if (r != true) {
			return;
		}
		
		var campos = $('table#tb_'+id_form+ ' tr#tr_'+ id ).attr("campos");
		var dados = JSON.parse(campos);
						
		$.ajax({
			type: "POST",
			data: dados,		
			url: encodeURI(url),
			context: document.body,
			timeout: 300000, async: true,
			success: function(retorno){
				
				$('table#tb_'+id_form+ ' tr#tr_'+ id).remove();
				$('td#td_foto'+id).remove();

			},			
			beforeSend: function(data ){},
			error: function(request, status, error){ 
				if (error=='timeout'){
					mostrarMensagemErro('Tempo Excedido', "A a&ccedil;&atilde;o demorou tempo demais, tente novamente mais tarde.");
				}else{
					mostrarMensagemErro("Projudi - Erro", request.responseText);
				}
			},
			complete: function(data ){
							
			}
		}); // fim do .ajax*/
	
	}
	
	function editarPasso( id_form, id){
		var campos = $('table#tb_'+id_form+ ' tr#tr_'+ id ).attr("campos");
		var obJson = JSON.parse(campos);
		//console.log(campos);
		var ids = Object.keys(obJson);
		var valores = Object.values(obJson);
		for(var i=0; i<ids.length;i++){
			//testo os campos do formulario pelo id
			var obj =$("#" +id_form+ " #"+ids[i]);
			if(obj.attr('type')=='checkbox'){
				if (valores[i]=='1' || valores[i].toLowerCase()=='sim' || valores[i].toLowerCase()=='true'){
					obj.attr("checked", "checked");
				}else{
					obj.removeAttr('checked');
				}
			}else{
				obj.val( valores[i]);
			}
			
			//console.log(ids[i] + ' ' + valores[i] );
		}

		$('table#tb_'+id_form+ ' tr#tr_'+ id).remove();
		 if ($.isFunction(window.editarPassoSelecionar)) {
			//execute it
        	editarPassoSelecionar();
   		 }
	}
//*/--------------------------------------------
	
	function submete(id){		
									//editar
		AlterarValue('PaginaAtual' , '-1'); 
		AlterarValue('Id_Processo', id); 
		FormSubmit('Formulario');		
	}

	function imprimirEtiqueta(myDiv){
		var prtContent = document.getElementById(myDiv);
		var WinPrint = window.open('', '', 'left=0,top=0,width=330,height=230, menubar=0, resizable=0,scrollbars=0,status=0, titlebar=0,toolbar=0');
		WinPrint.document.write("<html><style type='text/css' media='print'>@page {size: auto; margin: 0; }</style>"+prtContent.innerHTML+"</html>");
		WinPrint.document.close();
		WinPrint.focus();
		WinPrint.print();
		WinPrint.close();
	}
	
	function buscaDados2JSON(posicao, tamanho, url, corpoTabela, jsonCallBack, nomeTabela, selecao) {
		var boFecharDialog = false;
	    $.ajax({
	        url: encodeURI(url),
	        context: document.body,
	        timeout: 300000, async: true,
	        success: function(retorno){
	        	
	        	var tabela =  $('#tabListaDados');
	        	if(nomeTabela != null){
	        		tabela =  $(''+nomeTabela+'');
	        	}
	            tabela.html('');
	        	
	            var corpoTabelaJSON = "";
	            
	            $.each(retorno, function(i,item){
	            	if (i > -1){
	            		if(item.id=="-50000"){						
	    					totalPaginas = item.desc1;
	    				}else if (item.id=="-60000"){
	    					posicao = item.desc1;
	    				}else {
	    					corpoTabelaJSON +='<tr class="TabelaLinha'+(i%2 + 1)+'">';
	    					if (selecao!=null){
	    						corpoTabelaJSON +='<td   class="Centralizado" > <input type="checkbox" name="pendenciaSelecionada[]" value="' + item.id +" "+item.hash+ '" /> </td>';
	    					}else{
	    						corpoTabelaJSON +='<td   class="Centralizado" >' + ((i-1)) + '</td>';
	    					}

	                		corpoTabelaJSON += corpoTabela;                		
	                		for (parametro in item) {
	                		    parametroSubstituir = 'JsoN'+parametro;
	                			//alert("aqui: "+item.id+"\npar: "+parametro+"\nval: "+item[parametro]);
	                		    
	                		    corpoTabelaJSON = corpoTabelaJSON.replace(RegExp(parametroSubstituir, 'g'), item[parametro]);
	                		}
	                		corpoTabelaJSON +='</tr>';
	                	}
	            	}
	            });
//	            alert(corpoTabelaJSON);
	            tabela.append(corpoTabelaJSON);
	            
	            chamada = 'buscaDados(';
	            CriarPaginacaoSimplesJSON(chamada, posicao,totalPaginas, tamanho);
	            if(jsonCallBack != null){
	            	jsonCallBack();
	            }
	        },
	        
	        beforeSend: function(data ){
	        	//Mostra a mensagem de "consultando" apenas se o tempo de consulta exceder o do timer
	        	timer = setTimeout(function() {
	        		mostrarMensagemConsultando('Projudi - Consultando', 'Aguarde, buscando os dados...');		
	        		boFecharDialog=true;
	        	}, 1500);
	        	$("#formLocalizarBotao").hide();			
	        },
	        error: function(request, status, error){			
	        	boFecharDialog=false;
	        	if (error=='timeout'){
					mostrarMensagemErro('Tempo Excedido', "A ação demorou tempo demais, tente novamente mais tarde.");
				}else{
					mostrarMensagemErro("Projudi - Erro", request.responseText);
				}				
	        },
	        complete: function(data ){
	        	//Quando completar a consultar, previnir a mensagem de "consultando" de ser mostrada
	        	clearTimeout(timer);
	        	if (boFecharDialog) {
	        		$("#dialog").dialog('close');	
	        	}
				$("#formLocalizarBotao").show();
	        }
	    });	
	}
	
	function excluirItemSelecaoMultipla(controle, id, paginaAtual){
		
		var url = controle+'?AJAX=ajax&PaginaAtual=' + paginaAtual ;
				
		//var campos = $('table#tb_'+id_form+ ' tr#tr_'+ id ).attr("campos");
		//var dados = JSON.parse(campos);
						
		$.ajax({
			type: "POST",
			data: { Passo: "excluir", Id_Excluir:  id  },		
			url: encodeURI(url),
			context: document.body,
			timeout: 300000, async: true,
			success: function(retorno){							

			},			
			beforeSend: function(data ){},
			error: function(request, status, error){ 
				if (error=='timeout'){
					mostrarMensagemErro('Tempo Excedido', "A a&ccedil;&atilde;o demorou tempo demais, tente novamente mais tarde.");
				}else{
					mostrarMensagemErro("Projudi - Erro", request.responseText);
				}
			},
			complete: function(data ){
							
			}
		}); // fim do .ajax*/
	}
	
	function salvarItemSelecaoMultipla(controle,  id_princiapal, id_secundario, paginaAtual){
		
		var url = controle+'?AJAX=ajax&PaginaAtual='+ paginaAtual ;		

		$.ajax({
			type: "POST",
			data: { Passo: "incluir", Id_Principal:  id_princiapal, Id_Secundario: id_secundario },
			url: encodeURI(url),
			context: document.body,
			timeout: 300000, async: true,
			success: function(retorno){							

			},			
			beforeSend: function(data ){},
			error: function(request, status, error){ 
				if (error=='timeout'){
					mostrarMensagemErro('Tempo Excedido', "A a&ccedil;&atilde;o demorou tempo demais, tente novamente mais tarde.");
				}else{
					mostrarMensagemErro("Projudi - Erro", request.responseText);
				}
			},
			complete: function(data ){
							
			}
		}); // fim do .ajax*/
	}