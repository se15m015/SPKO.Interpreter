grammar GrammarSimple;

@header {
    import java.util.*;
    import java.util.ArrayList;
}

@members {
    private Map<String, Integer> table = new HashMap<String, Integer>();

    private boolean variableDefined(String name){
        return table.containsKey(name);
    }

    private Integer getValue(String name){
        return table.get(name);
    }

    private void setVariable(String name, Integer value){
       table.put(name,value);
    }
}

statements : (statement ';')*;

statement: 'print' expr { System.out.println($expr.v); }
            | 'var' ID {setVariable($ID.text, 0);} ('=' expr {setVariable($ID.text, $expr.v);})?
            | ID ('=' expr)? {
                    if(!variableDefined($ID.text)) {
                        throw new IllegalArgumentException("The variable '"+$ID.text+"' is undefined!");
                    }
                    else {setVariable($ID.text, $expr.v);}
                    };

expr returns [int v] : sum { $v = $sum.v; };

sum returns [int v] : product { $v = $product.v; }
            ( '+' product { $v+=$product.v;} )*
            ( '-' product { $v-=$product.v;} )*;

product returns [int v] : unary { $v = $unary.vUnary; }
            ( '*' unary { $v*=$unary.vUnary;} )*
            ( '/' unary { $v/=$unary.vUnary;} )*
            ( '%' unary { $v/=$unary.vUnary;} )*;

unary returns [int vUnary] : '-' unary { $vUnary = $unary.vUnary *(-1); }
            | term { $vUnary = $term.vTerm;} ;

term returns [int vTerm]: INT { $vTerm = Integer.parseInt($INT.text); }
            | '(' expr ')' { $vTerm = $expr.v; }
            | ID { $vTerm = getValue($ID.text); };

ID          : [a-zA-Z][a-zA-Z0-9_]*;

INT         : [0-9]+;

SINGLE_LINE_COMMENT:     '//'  InputCharacter*    -> channel(HIDDEN);   //Quelle: https://github.com/antlr/grammars-v4/blob/master/csharp/CSharpLexer.g4
fragment InputCharacter:       ~[\r\n\u0085\u2028\u2029];

WS          : [ \t\n\r]+ -> skip ;