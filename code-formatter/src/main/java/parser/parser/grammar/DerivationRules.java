package parser.parser.grammar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DerivationRules {
    public static Map<Nonterminal, ProductionBody> rules;

    static {
        rules = new HashMap<>();

        ArrayList<SententialForm> sententialForms1 = new ArrayList<>();


        rules.put(Nonterminal.APP, new ProductionBody(sententialForms1));

    }
}
