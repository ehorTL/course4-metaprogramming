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
    public boolean before_function_declaration_parentheses; // ?? -------
    public boolean before_function_call_parentheses; // ?? ---------

    public boolean before_if_parentheses; // +
    public boolean before_for_parentheses; // +
    public boolean before_while_parentheses; // +
    public boolean before_switch_parentheses; // +
    public boolean before_catch_parentheses; // +

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

    /**
     * Before Keywords
     * */
    public boolean before_else;
    public boolean before_while; //+-
    public boolean before_catch;

    /**
     * Within
     * not full
     * */
    public boolean within_empty_code_braces; //
    public boolean within_array_brackets; // + ?
    public boolean within_lambda_capture_list_brackets;
    public boolean within_empty_lambda_capture_list_brackets;
    public boolean within_grouping_parenth;
    public boolean within_if_parenth; // +
    public boolean within_for_parenth; // ++
    public boolean within_while_parenth; // ++
    public boolean within_switch_parenth; //++
    public boolean within_catch_parenth; // ++
    public boolean within_type_cast_parenth; //
    public boolean within_function_declaration_parenth;
    public boolean within_empty_function_declaration_parenth;
    public boolean within_function_call_parenth;
    public boolean within_empty_function_call_parenth;

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
    public boolean other_before_comma;
    public boolean other_after_comma;
    public boolean other_before_for_semicolon;
    public boolean other_after_for_semicolon;
    public boolean after_type_cast; // ------
    public boolean other_prevent_angle_brackets_concatenation_in_template; // -------------
    public boolean other_after_right_brace_in_structures;
    public boolean other_before_asterisk_in_declarations;
    public boolean other_after_asterisk_in_declarations;
    public boolean other_before_amp_in_declarations;
    public boolean other_after_amp_in_declarations;
    public boolean other_after_dereference_and_address_of; // *
    public boolean other_keep_space_between_same_type_brackets; // --------------
    public boolean other_before_colon_in_bit_field;
    public boolean other_after_colon_in_bit_field;
    public boolean other_between_operator_keyword_and_punctuator;

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
     * ------
     * */

    /**
     * BLANK LINES
     * */

    public int keep_max_blank_lines_in_declarations;
    public int keep_max_blank_lines_in_code;
    public int keep_max_blank_lines_in_before_right_brace;
    public int minimum_blank_lines_before_includes; // +
    public int minimum_blank_lines_after_includes; //+
    public int minimum_blank_lines_around_class_structure; // +-
    public int minimum_blank_lines_after_class_structure_header; // - what is the "class header"?
    public int minimum_blank_lines_around_field;
    public int minimum_blank_lines_around_global_variable;
    public int minimum_blank_lines_around_function_declaration;
    public int minimum_blank_lines_around_function_definition;
    public int minimum_blank_lines_before_function_body; // -+

    /**
     * WRAPPING AND BRACES
     * */

    /**
     * Enum Constants
     * */
    public boolean enum_constants_comma_on_next_line;

    /**
     * Try Statement
     * */
    public boolean try_statement_catch_on_new_line;

    /**
     * Switch Statement
     * */
    public boolean switch_statement_indent_case_branches;
    public boolean switch_statement_keep_simple_cases_in_one_line; // -

    /**
     * Do-while Statement
     * */
    public boolean do_while_statement_while_on_new_line;

    /**
     * Keep when reformatting
     * */
    public boolean keep_nested_namespaces_in_one_line;




    public TemplateProperties() { }
}
