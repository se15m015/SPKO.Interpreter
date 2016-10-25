grammar Exam;

expr: expr '+' prod
    | prod;

prod: prod '*' term
    | term;

term: NUM
    | '(' expr ')';

NUM: ('0' | '1');
