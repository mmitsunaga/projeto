/**
 *  Javascript para manipulacao de tabelas de arquivos do projudi
 * @author Ronneesley Moura Teles
 * @since 23/09/2008 10:15
 * Dependencias: jquery.js, conflitos.js e tabela.js
 */
var TabelaArquivosFinalizados = function(id, pai, link, campo, pa, editavel, resposta, possivelMarcar, possivelBloquear, possivelCopiar){
	if (editavel == null) editavel = false;

	this.editavel = editavel;
	this.id = id;
	this.pai = pai;
	this.link = link;
	this.campo = campo;
	this.paginaAtual = pa;
	this.resposta = resposta;
	this.possivelMarcar = possivelMarcar;
	this.possivelBloquear = possivelBloquear;
	this.possivelCopiar = possivelCopiar;
};
 
TabelaArquivosFinalizados.prototype = {
 	/**
 	 * Id da tabela de arquivos
 	 */
 	id: null,
 	
 	/**
 	 * Objeto pai 
 	 */
 	pai: null,
 
 	/**
 	 * Tabela dos arquivos
 	 */
	tabela: null,
	
	/**
	 * Link para o arquivo a fazer upload
	 */
	link: null,
	
	/**
	 * Pagina atual
	 */
	paginaAtual: null,
	
	/**
	 * Campo
	 */
	campo: null,
	
	/**
	 * 	Determina se nos casos de arquivos nao assinados
	 * Aparecera a opcao de edicao do arquivo
	 */
	editavel: false,
	
	/**
	 * Se o arquivo e uma resposta
	 */
	resposta: false,
	
	/**
	 * Se e possivel marcar em um check box o arquivo
	 */
	possivelMarcar: false,

	/**
	 * Se é possível bloquear um arquivo
	 */
	possivelBloquear: false,
	 
	 /**
	 * Se é possível copiar um arquivo
	 */
	possivelBloquear: false,

	/**
	 * Adicionar uma lista de arquivos na tabela
	 * @author Ronneesley Moura Teles
	 * @since 24/09/2008 10:29
	 * @param Array() lista, lista de objetos para serem dicionados
	 */
	adicionar: function(lista){
		var display = $('.imgValidar').css("display");
	 	//Percorre todos elementos da lista
	 	for (var i = 0; i < lista.length; i++){
	 	 	var obj = lista[i]; //Objeto do laco
	 	 	
	 	 	var hash = "";
			
			if (obj.hash != null) hash = obj.hash;
	 	 	
	 	 	var linha = [];
	 	 	
	 	 	if (this.possivelMarcar) {
	 	 		linha.push("<input type=\"checkbox\" name=\"arquivos[]\" value=\"" + obj.arquivoDt.id + "\" />");
	 	 	}
	 	 	
 	 		linha.push(obj.valido == true?obj.arquivoDt.arquivoTipo:"ARQUIVO BLOQUEADO");
			linha.push(obj.arquivoDt.usuarioAssinadorFormatado == ""?"ARQUIVO NÃO ASSINADO":obj.arquivoDt.usuarioAssinadorFormatado);
			  				
			//Verifica se ira aparecera a resposta
			if (this.resposta == true){
				linha.push(obj.resposta == "true"?"Sim":"Não");
			}

			//Quando um arquivo estiver válido mostra links 
			if (obj.valido == true){
				linha.push("<a href=\"" + this.link + "?PaginaAtual=" + this.paginaAtual + "&" + this.campo + "=" + obj.id + "&hash=" + obj.hash + "&finalizado=true\">" + 
								(obj.arquivoDt.nomeArquivoFormatado == ""?"Arquivo sem nome":obj.arquivoDt.nomeArquivoFormatado) + "</a>");
				linha.push("<div align=\"center\"><a href=\"" + this.link + "?PaginaAtual=" + this.paginaAtual + "&" + this.campo + "=" + obj.id + "&hash=" + obj.hash + "&CodigoVerificacao=true&finalizado=true\"><img src=\"imagens/22x22/btn_pdf.png\"/></a></div>");
//				linha.push("<div align=\"center\"><a href=\"" + this.link + "?PaginaAtual=" + this.paginaAtual + "&" + this.campo + "=" + obj.id + "&hash=" + obj.hash + "\"><img src=\"imagens/22x22/btn_pdf.png\"/></a></div>");
				
				if (obj.arquivoDt.recibo == true) {
					//linha[linha.length - 1] = linha[linha.length - 1] + "&nbsp;&nbsp;<a href=\"" + this.link + "?PaginaAtual=" + this.paginaAtual + "&" + this.campo + "=" + obj.id + "&hash=" + obj.hash + "&recibo=true\">Recibo</a>";
					linha.push("<div align=\"center\"><a href=\"" + this.link + "?PaginaAtual=" + this.paginaAtual + "&" + this.campo + "=" + obj.id + "&hash=" + obj.hash + "&recibo=true&finalizado=true\">Recibo</a></div>");
				}			
			} else {
				//linha.push("<span class='bloqueado' title='Arquivo Bloqueado'>" + (obj.arquivoDt.nomeArquivoFormatado == ""?"Arquivo sem nome":obj.arquivoDt.nomeArquivoFormatado) + "</span>");
				linha.push("<span class='bloqueado' title='Arquivo Bloqueado'>arquivoBloqueado</span>");
			}
								
			//Se for editavel, aparece a opcao de editar
			if (this.editavel == true){
				if (obj.arquivoDt.usuarioAssinadorFormatado == ""){					
				
					linha.push("<a href='#' onclick=\"editarPendencia(" + obj.id + ", \'" + hash + "\' , "+ obj.arquivoDt.id_ArquivoTipo + ", \'" + obj.arquivoDt.arquivoTipo + "\', \'" + obj.arquivoDt.nomeArquivo + "\')\"><img src='imagens/22x22/ico_editar.png' alt='Editar' title='Editar o arquivo não assinado' />Editar</a>");
				} else {
					linha.push("");
				}
			}
			
			//Se é possível copiar
			if (this.possivelCopiar == true){
				if (obj.valido){
					linha.push("<img id='imgCopiar' class='imgCopiar' src='imagens/22x22/ico_solucionar.png' alt='Copiar Arquivo para Area de Transferência' title='Copiar Arquivo para Area de Transferência' " +
					" onclick=\"javascript: incluirMovimentacaoArquivoAreaTransferencia('" + obj.id + "','" + obj.arquivoDt.nomeArquivoFormatado + "','" + hash + "');\" />");
					linha.push("<img id='imgLimpar' class='imgLimpar' src='imagens/imgLimpar.png' alt='Limpar Arquivos da Area de Transferência' title='Limpar Arquivos da Area de Transferência' " +
							" onclick=\"javascript: limparArquivosAreaTransferencia();\" />");
				} else {
					linha.push("");
					linha.push("");
				}
			}

			//Só deve mostrar botões para validar ou invalidar arquivo se usuário tem permissão para bloquear
			if (this.possivelBloquear == true){
				if (obj.valido){
					linha.push("<img id='imgValidar' class='imgValidar' src='imagens/22x22/ico_fechar.png' alt='Bloquear Arquivo' title='Bloquear Arquivo' " +
					" onclick=\"javascript: invalidarArquivo('" + obj.id + "','" + obj.arquivoDt.nomeArquivoFormatado + "')\" />");
				} else {
					linha.push("<img id='imgValidar' class='imgValidar' src='imagens/22x22/ico_liberar.png' alt='Desbloquear Arquivo' title='Desbloquear Arquivo' " +
					" onclick=\"javascript: validarArquivo('" + obj.id + "','" + obj.arquivoDt.nomeArquivoFormatado + "')\" />");
				}
			}		
			
			
	 	 	this.tabela.addLinha( linha );
	 	 	$('.imgValidar').css('display',display);
		}	
	},
	
	/**
	 * Criar a tabela de arquivos
	 * @author Ronneesley Moura Teles
	 * @since 24/09/2008 09:11
	 * @param Array() lista, lista de objetos caso queira adicionar no final
	 */
	criar: function(lista){
	 	var fieldset = document.createElement("fieldset");
	 	//var legend = document.createElement("legend");	 	
	 	//$j(legend).html("Arquivos");	 	
	 	//$j(fieldset).append(legend);
		$(fieldset).attr("id", this.id);
	 	$("#" + this.pai).append(fieldset);

		var cabecalho = [];
		
		if (this.possivelMarcar){
			cabecalho.push("Op.");
		}
		cabecalho.push("Tipo Arquivo");
		cabecalho.push("Assinador");
		
		//Se e para aparecer a coluna resposta
		if (this.resposta == true){
			cabecalho.push("Resposta");
		}
		
		cabecalho.push("Visualização");
		cabecalho.push("Impressão");
		cabecalho.push("Recibo");
		
		//Se e editavel, aparece a coluna de opcoes
		if (this.editavel == true){
			cabecalho.push("Opções");
		}
		
		//Se é possível copiar
		if (this.possivelCopiar == true){
			cabecalho.push("Copiar");
			cabecalho.push("Limpar");
		}

		this.tabela = new Tabela(this.id + "_tabela", cabecalho, this.id, "Arquivos");	 
		this.tabela.criar();
		
		//Verifica se passou a lista, caso tenha, adiciona os itens
		if (lista != null)
			this.adicionar(lista);
	}	

}