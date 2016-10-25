grammar Grammar;

statements : (statement (';')?)*;

statement : 'print' e=expr                                                  #printStmt
            //| 'gvar' ID ('=' e=expr)?                                        #globalDefineStmt
            | 'var' ID ('=' e=expr)?                                        #defineStmt
            //| 'this.'ID '=' e=expr                                          #globalAssignStmt
            | ID '=' e=expr                                                 #assignStmt
            |  'while' c=expr 'do' s=statement                              #whileStmt
            | 'if' c=expr 'then' sIf=statement ('else' sElse=statement)?    #ifStmt
            | 'if!Null' o=expr ',' c=expr 'then' sIf=statement ('else' sElse=statement)? #ifNotNullStmt
            | 'whileif' cif=expr ',' cwhile=expr 'do' '{' sIf=statements '}' '{' sElse=statements '}' #whileIfStmt
            | 'funcdef' ID '(' idlist ')' statement                         #funcdefStmt
            | '{' s=statements '}'                                          #blockStmt // OPTION 1
            | expr                                                          #simpleExpr // OPTION 2
            ;

expr : left=expr op=('<'|'=<'|'=='|'><'|'>='|'>') right=expr #opExpr
        | left=expr op=('*'|'/'|'%') right=expr #opExpr
        | left=expr op=('+'|'-') right=expr #opExpr
        | '-' e=expr #negStmt
        | '(' e=expr ')' #wrapperExpr
        | methodeName=ID '('arglist')'                                              #funccallStmt
        | ID    #idExpr
        | INT   #numExpr
        | '{' s=statements '}' #inlineStmt// OPTION 2

        ;

idlist :  ID (',' ID)*;

arglist :  (expr) (',' expr)*;

ID          : [a-zA-Z][a-zA-Z0-9_]*;

INT         : [0-9]+;

SINGLE_LINE_COMMENT:     '//'  InputCharacter*    -> channel(HIDDEN);   //Quelle: https://github.com/antlr/grammars-v4/blob/master/csharp/CSharpLexer.g4
fragment InputCharacter:       ~[\r\n\u0085\u2028\u2029];

WS          : [ \t\n\r]+ -> skip ;