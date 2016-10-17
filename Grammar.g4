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

term        : '(' expr ')'
            | ID
         //   | ID '(' arglist ')'
            | INT
            | '{' statements '}'; // OPTION 2

ID          : [a-zA-Z][a-zA-Z0-9_]*;

INT         : [0-9]+;

WS          : [ \t\n\r]+ -> skip ;

COMMENT     :   ( '//' ~[\r\n]* '\r'? '\n'
            | '/*' .*? '*/') -> channel(HIDDEN);