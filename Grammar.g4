grammar Grammar;
statements  : (statement (';')?)*;

statement   : 'print' expr
            | 'var' ID ('=' expr)?
            | ID '=' expr
            | 'while' expr 'do' statement
            | 'if' expr 'then' statement ('else' statement)?
         //   | 'funcdef' ID '(' idlist ')' statement // OPTION 3
            | '{' statements '}'  // OPTION 1
            | expr;                // OPTION 2

expr        : cmp;

cmp         : sum (('<' | '=<' | '==' | '><' | '>=' | '>') sum)?;

sum         : product (('+'|'-') product)*;

product     : unary (('*'|'/'|'%') unary)*;

unary       : '-' unary
            | term;

term returns [int vTerm]: '(' expr ')'
            | ID
            | ID '(' arglist ')'
            | INT
            //| '{' statements '}' // OPTION 2

ID          : [a-zA-Z][a-zA-Z0-9_]*;

INT         : [0-9]+;

SINGLE_LINE_COMMENT:     '//'  InputCharacter*    -> channel(HIDDEN);   //Quelle: https://github.com/antlr/grammars-v4/blob/master/csharp/CSharpLexer.g4
fragment InputCharacter:       ~[\r\n\u0085\u2028\u2029];

WS          : [ \t\n\r]+ -> skip ;