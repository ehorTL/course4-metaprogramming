package templatereader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TemplateProperties {

//    after keyword, before brackets
    public boolean if_parentheses;
    public boolean for_parentheses;
    public boolean while_parentheses;
    public boolean switch_parentheses;
    public boolean catch_parentheses;

//    before keywords
    public boolean before_else;
    public boolean before_while;
    public boolean before_catch;

    public boolean around_assignment_ops;
    public boolean around_logical_ops;
    public boolean around_equality_ops;
    public boolean around_relational_ops;
    public boolean around_bitwise_ops;
    public boolean around_additive_ops;
    public boolean around_multiplicative_ops;
    public boolean around_shift_ops;
    public boolean around_unary_ops;
    public boolean around_pointer_in_ret_type_ops;
    public boolean around_pointer_to_member_ops_ops;

    public boolean before_left_brace_do;

    public boolean in_tern_op_bf_qm;
    public boolean in_tern_op_af_qm;
    public boolean in_tern_op_bf_colon;
    public boolean in_tern_op_af_colon;
    public boolean in_tern_op_space_betw_all;

//    before left brace
    public boolean namespace;
    public boolean namespace2; // ?


    public TemplateProperties() { }
}
