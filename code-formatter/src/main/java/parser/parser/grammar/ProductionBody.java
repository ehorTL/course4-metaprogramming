package parser.parser.grammar;

import java.util.ArrayList;

/**
 * Reflects the roght-hand side of production
 * Each element of array is an alternative.
 * S -> Sa | Dff | Labc
 * */
public class ProductionBody {
    public ArrayList<SententialForm> sententialForms;

    public ProductionBody(ArrayList<SententialForm> sententialForms) {
        this.sententialForms = sententialForms;
    }
}
