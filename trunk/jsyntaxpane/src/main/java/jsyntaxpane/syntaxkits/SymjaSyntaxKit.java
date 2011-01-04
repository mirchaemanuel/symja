package jsyntaxpane.syntaxkits;

import jsyntaxpane.DefaultSyntaxKit;
import jsyntaxpane.Lexer;
import jsyntaxpane.lexers.SymjaLexer;

public class SymjaSyntaxKit  extends DefaultSyntaxKit {

  public SymjaSyntaxKit() {
      super(new SymjaLexer());
  }

  SymjaSyntaxKit(Lexer lexer) {
      super(lexer);
  }
}
