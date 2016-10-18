grammar Grammar;

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

statements returns [int vStatements] : (statement {$vStatements = $statement.vStatement;} (';')?)*;

statement returns [int vStatement] : 'print' expr {System.out.println($expr.vExpr);  }
            | 'var' ID {setVariable($ID.text, 0); } ('=' expr {setVariable($ID.text, $expr.vExpr); })?
            | ID '=' expr {
                            if(!variableDefined($ID.text)) {
                                throw new IllegalArgumentException("The variable '"+$ID.text+"' is undefined!");
                            }
                            else {setVariable($ID.text, $expr.vExpr);}
                            }
            | 'while' expr 'do' statement
                            {
                                while($expr.vExpr != 0){$vStatement = $statement.vStatement;}
                            }
//            | 'if' expr 'then' statement ('else' statement)?
//         //   | 'funcdef' ID '(' idlist ')' statement // OPTION 3
            | '{' statements '}' {$vStatement = $statements.vStatements;};  // OPTION 1
//            | expr;                // OPTION 2

expr  returns [int vExpr] : cmp {$vExpr = $cmp.vCmp; };

cmp  returns [int vCmp] : sum {$vCmp = $sum.vSum;}
                        ('<' sum {$vCmp = ($vCmp < $sum.vSum) ? 1 : 0; })?
                        ('=<' sum {$vCmp = ($vCmp <= $sum.vSum) ? 1 : 0; })?
                        ('==' sum {$vCmp = ($vCmp == $sum.vSum) ? 1 : 0; })?
                        ('><' sum {$vCmp = ($vCmp != $sum.vSum) ? 1 : 0; })?
                        ('>=' sum {$vCmp = ($vCmp >= $sum.vSum) ? 1 : 0; })?
                        ('>' sum {$vCmp =  ($vCmp > $sum.vSum) ? 1 : 0; })?;

sum returns [int vSum] : product {$vSum = $product.vProduct; }
                         ('+' product {$vSum = $vSum + $product.vProduct; })*
                         ('-' product {$vSum = $vSum - $product.vProduct; })*;

product returns [int vProduct] : unary {$vProduct = $unary.vUnary; }
                ('*' unary {$vProduct = $vProduct * $unary.vUnary; })*
                ('/' unary {$vProduct = $vProduct / $unary.vUnary; })*
                ('%' unary {$vProduct = $vProduct / $unary.vUnary; })*;

unary returns [int vUnary] : '-' unary {$vUnary = $unary.vUnary *(-1); }
            | term {$vUnary = $term.vTerm; };

term returns [int vTerm]: '(' expr ')' { $vTerm = $expr.vExpr; }
            | ID { $vTerm = getValue($ID.text); }
           // | ID '(' arglist ')'
            | INT { $vTerm = Integer.parseInt($INT.text); };
            //| '{' statements '}' // OPTION 2

ID          : [a-zA-Z][a-zA-Z0-9_]*;

INT         : [0-9]+;

SINGLE_LINE_COMMENT:     '//'  InputCharacter*    -> channel(HIDDEN);   //Quelle: https://github.com/antlr/grammars-v4/blob/master/csharp/CSharpLexer.g4
fragment InputCharacter:       ~[\r\n\u0085\u2028\u2029];

WS          : [ \t\n\r]+ -> skip ;