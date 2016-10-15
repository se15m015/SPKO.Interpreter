grammar Grammar;
statements: (statement ';')*;
statement: 'print' expr
| 'var' ID ('=' expr)?
| ID '=' expr;
expr: sum;
sum: product (('+'|'-') product)*;
product: unary (('*'|'/'|'%') unary)*;
unary: '-' unary
| term;
term: '(' expr ')'
| ID
| INT;
ID: [a-zA-Z][a-zA-Z0-9_]*;
INT: [0-9]+;
WS: [ \t\n\r]+ -> skip ;

/*prog:	(expr NEWLINE)* ;
expr:	expr ('*'|'/') expr
    |	expr ('+'|'-') expr
    |	INT
    |	'(' expr ')'
    ;
NEWLINE : [\r\n]+ ;
INT     : [0-9]+ ;*/