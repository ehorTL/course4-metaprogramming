package parser.parser.grammar;

public class GrammarUnit {
    private GrammarUnitType type;
    private Terminal terminal;
    private Nonterminal nonterminal;

    public GrammarUnit(GrammarUnitType type) {
        this.type = type;
    }

    public void setTerminal(GrammarUnitType type, Terminal terminal) {
        this.type = type;
        this.terminal = terminal;
    }

    public void setNontrminal(GrammarUnitType type, Nonterminal nonterminal) {
        this.type = type;
        this.nonterminal = nonterminal;
    }

    public Nonterminal getNonterminal() {
        return nonterminal;
    }

    public Terminal getTerminal() {
        return terminal;
    }
}
