grammar GrammarParserRules;

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

statement returns [int vStatement]: 'print' expr {System.out.println($expr.vExpr);  }
            | 'var' ID {setVariable($ID.text, 0); } ('=' expr {setVariable($ID.text, $expr.vExpr); })?
            | ID '=' expr {
                            if(!variableDefined($ID.text)) {
                                throw new IllegalArgumentException("The variable '"+$ID.text+"' is undefined!");
                            }
                            else {setVariable($ID.text, $expr.vExpr);}
                            }
            |  'while' e=expr 'do' s=statement
                {
                    while(true)
                    {
                        if($e.vExpr == 0) {break;}

                        $vStatement = $s.vStatement;
                    }
                }
            | 'if' e=expr 'then' sIf=statement {
                                          if($e.vExpr == 1)
                                           {
                                               $vStatement = $sIf.vStatement;
                                           }
                   }
               ('else' sElse=statement
                {
                   if($e.vExpr == 1)
                    {
                        $vStatement = $sIf.vStatement;
                    }
                   else
                    {
                        $vStatement = $sElse.vStatement;
                    }
                }
            )?
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
                         ('+' p=product {$vSum = $vSum + $p.vProduct; })*
                         ('-' p=product {$vSum = $vSum - $p.vProduct; })*;

product returns [int vProduct] : unary {$vProduct = $unary.vUnary; }
                ('*' u=unary {$vProduct = $vProduct * $u.vUnary; })*
                ('/' u=unary {$vProduct = $vProduct / $u.vUnary; })*
                ('%' u=unary {$vProduct = $vProduct % $u.vUnary; })*;

unary returns [int vUnary] : '-' u=unary {$vUnary = -$u.vUnary; }
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