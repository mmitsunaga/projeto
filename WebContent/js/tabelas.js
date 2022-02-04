/**
 *  Javascript para manipulacao de tabelas
 *  A necessidade e criar a tabela e manipular os elementos internos
 * nela contida
 * @author Ronneesley Moura Teles
 * @since 23/09/2008 10:15
 * Dependencias: jquery.js e conflitos.js
 */
var Tabela = function(id, cab, pai, classe){
 	if (pai == null) pai = document;
 
 	//Atribui as inicializacoes
 	this.id = id;	
 	this.pai = $("#" + pai);
 	this.classe = classe;
 	this.cab = new Array();
 	
 	if (cab != null){
 		this.addCabecalho(cab);
 	}
};
 
Tabela.prototype = {
	/**
 	 * Id da tabela a ser criada
 	 */
 	id: null,
 	
 	/**
 	 * Classe da tabela
 	 */
 	classe: null,
 
 	/**
 	 * Elemento html pai da tabela
 	 */
	pai: null,
	
	/**
	 * Borda da tabela
	 */
	borda: 0,
	
	/**
	 * Linhas do cabecalho
	 */
	cab: new Array(),
	
	/**
	 * Adiciona o cabecalho a partir de um vetor
	 * @author Ronneesley Moura Teles
	 * @since 23/09/2008 16:52
	 * @páram Array() vetor, vetor
	 */
	addCabecalho: function(vetor){
	 	//Adiciona a linha do cabecalho
	 	this.addCab( new Linha() );
	 	
	 	for (var i = 0; i < vetor.length; i++){
			this.cab[0].add(new Coluna(vetor[i], 1));
		}
	},
	
	/**
	 * Adiciona uma nova linha para o cabecalho
	 * @author Ronneesley Moura Teles
	 * @since 23/09/2008 10:27
	 * @param Coluna coluna
	 */
	addCab: function(linha){
	 	//Adiciona a coluna no array
	 	this.cab.push(linha);	
	},
	
	/**
	 * Adicionar linhas na tabela
	 * @author Ronneesley Moura Teles
	 * @since 23/09/2008 13:34
	 * @param Array(Array()) linhas
	 */
	addLinhas: function(linhas){
	 	//Percorre as linhas
		for (var i = 0; i < linhas.length; i++){
			//Adiciona a linha
		 	this.addLinha(linhas[i]);
		}
	},
	
	/**
	 * Adicionar uma linha na tabela
	 * @author Ronneesley Moura Teles
	 * @since 23/09/2008 13:35
	 * @param Array() linha, linha a ser adicionada
	 */
	addLinha: function(linha){
	 	//Verifica se a tabela existe
	 	var tabela = $("#" + this.id).get(0);
	 	
	 	//Caso nao exista cria
	 	if (tabela == null) this.criar();
	 
	 	var tbody = $("#" + this.id + " tbody").get(0);	 	
	 		 	
	 	//Se nao tem tbody cria um
	 	if (tbody == null) {
	 	 	var tbody = document.createElement("tbody");
	 	 	var jtbody = $(tbody);
	 	 	
	 	 	$("#" + this.id).append(jtbody);
		} else {
			var jtbody = $(tbody);
		}
	 
		var oLinha = new Linha(); //Instancia uma linha
 	 
 	 	oLinha.addVetor( linha ); //Adiciona no conjunto de elementos
 	 	
 	 	jtbody.append( oLinha.criar() );
	},
	
	/**
	 * Criar colunas
	 * @author Ronneesley Moura Teles
	 * @since 23/09/2008 10:32
	 */
	criarCab: function(tabela){
		var thead = document.createElement("thead");
		var jthead = $(thead);
		
		//Percorre linhas
		for (var i = 0; i < this.cab.length; i++){
			jthead.append(this.cab[i].criar());
		}
		
		tabela.append(jthead);
	},
	
	/**
	 * Criar a tabela em seu elemento pai
	 * @author Ronneesley Moura Teles
	 * @since 23/09/2008 10:19
	 */
	criar: function(){
	 	var tabela = document.createElement("table");
	 	var jtabela = $(tabela);
	 	
	 	jtabela.attr("id", this.id);
		jtabela.attr("border", this.borda);
		jtabela.attr("class", this.classe);
		
		//Se tem colunas
		if (this.cab.length > 0){
		 	this.criarCab(jtabela);
		}

		this.pai.append(jtabela);
	},
}

/**
 * Classe para representar as linhas
 * @author Ronneesley Moura Teles
 * @since 23/09/2008 10:42 
 */
var Linha = function(){
	this.elementos = new Array();
}

Linha.prototype = {
	/**
	 * Elementos
	 */
	elementos: new Array(),
	
	/**
	 * Adiciona elementos em uma linha
	 * @author Ronneesley Moura Teles
	 * @since 23/09/2008 10:44
	 */
	add: function(elemento){
	 	this.elementos.push(elemento);
	},
	
	/**
	 * Adiciona elementos apartir de um vetor
	 * @author Ronneesley Moura Teles
	 * @since 23/09/2008 13:39
	 * @param Array() vetor, vetor de elementos, nao podem ser objetos e somente valores
	 * @return Array()
	 */
	addVetor: function(vetor){
	 	//Percorre elementos do vetor, criando os objetos para as colunas
	 	for (var i = 0; i < vetor.length; i++){
		 	this.elementos.push( new Coluna( vetor[i] ) );
		}
		
		return this.elementos;
	},	
	
	/**
	 * Criar a linha
	 * @author Ronneesley Moura Teles
	 * @since 23/09/2008 10:45
	 * @return HTMLObject
	 */
	criar: function(){
	 	var tr = document.createElement("tr");
	 	var jtr = $(tr);

		//Perccore os elementos para adicionalos
		for (var i = 0; i < this.elementos.length; i++){
		 	//Adiciona o elemento
		 	jtr.append(this.elementos[i].criar());
		}
	 	
	 	return tr;
	}
}


/**
 * Classe de colunas
 * @author Ronneesley Moura Teles
 * @since 23/09/2008 10:28
 */
var Coluna = function(conteudo, tipo){
 	this.conteudo = conteudo;
 	this.tipo = tipo;
}

Coluna.prototype = {
 	/**
 	 * Conteudo da coluna
 	 */
 	conteudo: null,

 	/**
 	 * Tipo normal
 	 */
 	TIPO_NORMAL: 0,
 	
 	/**
 	 * Tipo de cabecalho
 	 */
 	TIPO_CABECALHO: 1,
 	
 	/**
 	 * Tipo da coluna (0 - Coluna, 1 - Coluna de Cabecalho)
 	 */
 	tipo: Coluna.TIPO_NORMAL,
 	
 	/**
 	 * Criar a coluna em um objeto HTML
 	 * @author Ronneesley Moura Teles
 	 * @since 23/09/2008 10:35
 	 * @return HTMLObject
 	 */
 	criar: function(){
 	 	var t = document.createElement( this.tag() );
 	 	var jt = $(t);
 	 	
 	 	//Modifica o conteudo da coluna
 	 	jt.html(this.conteudo);
 	 	
 	 	return t;
 	},
 	
 	/**
 	 * Retorna a tag correspondente do tipo de coluna
 	 * @author Ronneesley Moura Teles
 	 * @since 23/09/2008 13:47
 	 */
 	tag: function(){
 		if (this.tipo == this.TIPO_CABECALHO) return "th"; //Se for cabecalho
 		
 		return "td";
 	}
 	
} 