package templatereader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * braces - {}
 * parentheses - ()
 * */
public class TemplateProperties {

    /**
     * Before Parentheses (after keywords), ("(")
     * */
    public boolean before_if_parentheses; // +
    public boolean before_for_parentheses; // +
    public boolean before_while_parentheses; // +
    public boolean before_switch_parentheses; // +
    public boolean before_catch_parentheses; // +
    public boolean function_declaration_parentheses; // ?? -------
    public boolean function_call_parentheses; // ?? ---------

    /**
     * Before Keywords
     * */
    public boolean before_else;
    public boolean before_while; //+-
    public boolean before_catch;

    /**
     * Around Operators
     * */
    public boolean around_assignment_ops;
    public boolean around_logical_ops; // +
    public boolean around_equality_ops; // +
    public boolean around_relational_ops; // +
    public boolean around_bitwise_ops;
    public boolean around_additive_ops;
    public boolean around_multiplicative_ops;
    public boolean around_shift_ops; // +
    public boolean around_unary_ops;
    public boolean around_pointer_in_ret_type_ops;
    public boolean around_pointer_to_member_ops_ops;


    /**
     * Before Left Brace ("{")
     * */
    public boolean before_left_brace_namespace;
    public boolean before_left_brace_init_list;
    public boolean before_left_brace_class_structure;
    public boolean before_left_brace_function;

    public boolean before_left_brace_if; //+
    public boolean before_left_brace_else; //+
    public boolean before_left_brace_for; //+
    public boolean before_left_brace_while; //+
    public boolean before_left_brace_do; // +
    public boolean before_left_brace_switch; // +
    public boolean before_left_brace_try; // +
    public boolean before_left_brace_catch; // +
    // ...

    /**
     * Within
     * */
    public boolean within_empty_code_braces; //
    public boolean within_if_parenth; // +
    public boolean within_for_parenth; // ++
    public boolean within_while_parenth; // ++
    public boolean within_switch_parenth; //++
    public boolean within_catch_parenth; // ++
    public boolean within_grouping_parenth;
    public boolean within_array_brackets; // + ?

    public boolean within_type_cast_parenth; //
    public boolean within_function_declaration_parenth;
    public boolean within_empty_function_declaration_parenth;
    public boolean within_function_call_parenth;
    public boolean within_empty_function_call_parenth;


//    ....


    /**
     * In ternary operator
     * */
    public boolean in_tern_op_bf_qm;
    public boolean in_tern_op_af_qm;
    public boolean in_tern_op_bf_colon;
    public boolean in_tern_op_af_colon;
    /**
     * usage ?? elvis operator?
     * */
    public boolean in_tern_op_space_betw_all;

    /**
     * Other
     * */
    public boolean other_before_for_semicolon;
    public boolean other_after_for_semicolon;
    public boolean other_before_comma;
    public boolean other_after_comma;
    public boolean other_between_operator_keyword_and_punctuator;
    public boolean other_after_right_brace_in_structures;

    public boolean other_before_colon_in_bit_field;
    public boolean other_after_colon_in_bit_field;

    /**
     * In Template Declaration
     * */
    public boolean template_before_left_angle_bracket;
    public boolean template_within_brackets;
    public boolean template_within_empty_brackets;

    /**
     * In Class/Structure
     * */
    public boolean class_structure_bf_base_class_colon;
    public boolean class_structure_af_base_class_colon;
    public boolean class_structure_bf_constr_init_list_colon; //??
    public boolean class_structure_af_constr_init_list_colon; //??

    /**
     * In Template Instantiation
     * */
    public boolean template_instantiation_before_left_angle_bracket;
    public boolean template_instantiation_within_brackets;
    public boolean template_instantiation_within_empty_brackets;


    /**
     * Wrapping and Braces block
     * */


    public TemplateProperties() { }
}
