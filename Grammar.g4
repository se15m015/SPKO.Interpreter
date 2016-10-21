grammar Grammar;

statements : (statement (';')?)*;

statement : 'print' e=expr                                                  #printStmt
            | 'var' ID ('=' e=expr)?                                        #defineStmt
            | ID '=' e=expr                                                 #assignStmt
            |  'while' c=expr 'do' s=statement                              #whileStmt
            | 'if' c=expr 'then' sIf=statement ('else' sElse=statement)?    #ifStmt
//         //   | 'funcdef' ID '(' idlist ')' statement // OPTION 3
            | '{' s=statements '}'                                          # blockStmt // OPTION 1
//            | expr;                // OPTION 2
            ;
expr : left=expr op=('<'|'=<'|'=='|'><'|'>='|'>') right=expr #opExpr
        | left=expr op=('*'|'/'|'%') right=expr #opExpr
        | left=expr op=('+'|'-') right=expr #opExpr
        | '-' e=expr #negStmt
        | '(' e=expr ')' #wrapperExpr
        | ID    #idExpr
        | INT   #numExpr
        //| '{' statements '}' // OPTION 2
    ;

ID          : [a-zA-Z][a-zA-Z0-9_]*;

INT         : [0-9]+;

SINGLE_LINE_COMMENT:     '//'  InputCharacter*    -> channel(HIDDEN);   //Quelle: https://github.com/antlr/grammars-v4/blob/master/csharp/CSharpLexer.g4
fragment InputCharacter:       ~[\r\n\u0085\u2028\u2029];

WS          : [ \t\n\r]+ -> skip ;