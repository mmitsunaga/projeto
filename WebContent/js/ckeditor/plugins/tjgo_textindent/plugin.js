
(function() {
  function getTextIndent( element, useComputedState ) {
    useComputedState = useComputedState === undefined || useComputedState;

    var textIndent;
    if ( useComputedState )
      textIndent = element.getComputedStyle( 'text-indent' );
    else {
      while ( element.getStyle( 'text-indent' ) ) {
        var parent = element.getParent();
        if ( !parent )
          break;
        element = parent;
      }
      textIndent = element.getStyle( 'text-indent' ) || '';

    }
    
    // Sometimes computed values doesn't tell.
    textIndent && ( textIndent = textIndent.replace( /(?:-(?:moz|webkit)-)?(?:start|auto)/i, '' ) );

    //!align && useComputedState && ( align = element.getComputedStyle( 'direction' ) == 'rtl' ? 'right' : 'left' );

    return textIndent;
  }

  function textIndentCommand( editor, name, value ) {
    this.editor = editor;
    this.name = name;
    this.value = value;
    this.context = 'p';

    // //var classes = editor.config.justifyClasses,
    var blockTag = editor.config.enterMode == CKEDITOR.ENTER_P ? 'p' : 'div';


    this.allowedContent = {
      'caption div h1 h2 h3 h4 h5 h6 p pre td th li': {
        // Do not add elements, but only text-align style if element is validated by other rule.
        propertiesOnly: true,
        styles: 'text-indent',
        classes: null
      }
    };

    // // In enter mode BR we need to allow here for div, because when non other
    // // feature allows div justify is the only plugin that uses it.
    if ( editor.config.enterMode == CKEDITOR.ENTER_BR )
      this.allowedContent.div = true;
  }

  textIndentCommand.prototype = {
    exec: function( editor ) {
      
      var selection = editor.getSelection(),
        enterMode = editor.config.enterMode;

      if ( !selection )
        return;

      var bookmarks = selection.createBookmarks(),
        ranges = selection.getRanges( true );

      // var cssClassName = this.cssClassName,
      var iterator, block;

      var useComputedState = editor.config.useComputedState;
      useComputedState = useComputedState === undefined || useComputedState;

      for ( var i = ranges.length - 1; i >= 0; i-- ) {
        iterator = ranges[ i ].createIterator();
        iterator.enlargeBr = enterMode != CKEDITOR.ENTER_BR;

        while ( ( block = iterator.getNextParagraph( enterMode == CKEDITOR.ENTER_P ? 'p' : 'div' ) ) ) {

          block.removeStyle( 'text-indent' );

          // Remove any of the alignment classes from the className.
          // var className = cssClassName && ( block.$.className = CKEDITOR.tools.ltrim( block.$.className.replace( this.cssClassRegex, '' ) ) );

          var apply = ( this.state == CKEDITOR.TRISTATE_OFF ) && ( ! useComputedState || ( getTextIndent( block, true ) != this.value ) );

          if( apply )
            block.setStyle( 'text-indent', this.value );

        }

      }
      editor.focus();
      editor.forceNextSelectionCheck();
      selection.selectBookmarks( bookmarks );
    },

    refresh: function( editor, path ) {
      var firstBlock = path.block || path.blockLimit;
      this.setState( firstBlock.getName() != 'body' && getTextIndent( firstBlock, this.editor.config.useComputedState ) == this.value ? CKEDITOR.TRISTATE_ON : CKEDITOR.TRISTATE_OFF );
    }
  };

  CKEDITOR.plugins.add( 'tjgo_textindent', {
    lang: 'pt-br', // %REMOVE_LINE_CORE%
    icons: 'textindent', // %REMOVE_LINE_CORE%
    hidpi: false, // %REMOVE_LINE_CORE%
    init: function( editor ) {
      if ( editor.blockless )
        return;

      var textIndentCmd = new textIndentCommand( editor, 'tjgo_textindent', '70px' );

      editor.addCommand( 'textIndentCmd', textIndentCmd );


      if ( editor.ui.addButton ) {
        editor.ui.addButton( 'TextIndent', {
          label: editor.lang.tjgo_textindent.label,
          command: 'textIndentCmd',
          toolbar: 'blocks'
        });
      }

    }
  });
})();

 