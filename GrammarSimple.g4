grammar GrammarSimple;

@header { import java.util.*; }

statements[Map<String, Integer> table] : (statement[table] ';')*;

statement[Map<String, Integer> table] : 'print' expr[table] { System.out.println($expr.v); }
            | 'var' ID ('=' expr[table])? { $table.put($ID.text, $expr.v); }
            | ID ('=' expr[table])? { $table.put($ID.text, $expr.v); };

expr[Map<String, Integer> table] returns [int v] : sum[table] { $v = $sum.v; };

sum[Map<String, Integer> table] returns [int v] : product[table] { $v = $product.v; }
            ( '+' product[table] { $v+=$product.v;} )*
            ( '-' product[table] { $v-=$product.v;} )*;

product[Map<String, Integer> table] returns [int v] : unary[table] { $v = $unary.v; }
            ( '*' unary[table] { $v*=$unary.v;} )*
            ( '/' unary[table] { $v/=$unary.v;} )*
            ( '%' unary[table] { $v/=$unary.v;} )*;

unary[Map<String, Integer> table] returns [int v] : '-' unary[table] { $v = $unary.v*-1; }
            | term[table] { $v = $term.v;} ;

term[Map<String, Integer> table] returns [int v]: INT { $v = Integer.parseInt($INT.text); }
            | '(' expr[table] ')' { $v = $expr.v; }
            | ID { $v = $table.get($ID.text); };
            //| ID {
            //                 return values.get($ID.text);
            //              };

ID          : [a-zA-Z][a-zA-Z0-9_]*;

INT         : [0-9]+;

WS          : [ \t\n\r]+ -> skip ;