package formatter.streamconverter;

public class StackUnit {
    public Nonterminal nonterminal;
    public int startsFromPosition;

    public StackUnit(Nonterminal nonterminal, int position) {
        this.nonterminal = nonterminal;
        this.startsFromPosition = position;
    }
}
