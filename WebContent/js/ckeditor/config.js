/*
Copyright (c) 2003-2009, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

CKEDITOR.editorConfig = function( config )
{
	
	// Define changes to default configuration here. For example:
	config.language = 'pt-br';
	// config.uiColor = '#AADC6E';
	config.skin='moono_blue';
	config.toolbar = 'Projudi2';
	config.height = '570px';
	config.width = '98%'
	config.extraPlugins = 'tjgo_textindent,tjgo_lineheight,tjgo_ementa';	
	config.font_names = 'Arial;Times New Roman;Verdana;DejaVu Serif;DejaVu Sans Mono';
/*	config.scayt_autoStartup = true;	
	config.scayt_sLang = 'pt-br';*/
	
config.toolbar_Full =
[
    ['Source','-','Save','NewPage','Preview','-','Templates'],
    ['Cut','Copy','Paste','PasteText','PasteFromWord','-','Print', 'SpellChecker', 'Scayt'],
    ['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'],
    ['Form', 'Checkbox', 'Radio', 'TextField', 'Textarea', 'Select', 'Button', 'ImageButton', 'HiddenField'],
    '/',
    ['Bold','Italic','Underline','Strike','-','Subscript','Superscript'],
    ['NumberedList','BulletedList','-','Outdent','Indent','Blockquote'],
    ['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
    ['Link','Unlink','Anchor'],
    ['Image','Flash','Table','HorizontalRule','Smiley','SpecialChar','PageBreak'],
    '/',
    ['Styles','Format','Font','FontSize'],
    ['TextColor','BGColor'],
    ['Maximize', 'ShowBlocks','-','About']
];

config.toolbar_Basic =
[
    ['Bold', 'Italic', '-', 'NumberedList', 'BulletedList', '-', 'Link', 'Unlink','-','About']
];


config.toolbar_Projudi2 =
[
    ['NewPage','Preview'],['Undo','Redo','-','Cut','Copy'],['Find','Replace','-','SelectAll','RemoveFormat'],['Table','HorizontalRule','SpecialChar','PageBreak','Image'], ['Source','ShowBlocks','About'],'/',
	['Font','FontSize'], ['TextColor','BGColor'], ['Bold','Italic','Underline','Strike','-','Subscript','Superscript'],['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock', 'TextIndent','LineHeight', 'Ementa'], ['NumberedList','BulletedList','-','Outdent','Indent'] ,   
];

config.toolbar_Basic =
[
    ['Bold', 'Italic', '-', 'NumberedList', 'BulletedList', '-', 'Link', 'Unlink','-','About']
];

};
