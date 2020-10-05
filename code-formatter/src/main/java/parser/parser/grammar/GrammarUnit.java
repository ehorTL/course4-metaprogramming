package parser.parser.grammar;

public class GrammarUnit {
    private GrammarUnitType type;
    private Terminal terminal;
    private Nonterminal nonterminal;

    public GrammarUnit() {

    }

    public void setTerminal(GrammarUnitType type, Terminal terminal) {
        this.type = type;
        this.terminal = terminal;
    }

    public void setNontrminal(GrammarUnitType type, Nonterminal nonterminal) {
        this.type = type;
        this.nonterminal = nonterminal;
    }
}
