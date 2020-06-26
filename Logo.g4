grammar Logo ;

@header {
  package logoparsing;
}

FLOAT : [0-9][0-9]*('.'[0-9]+)? ;
WS : [ \t\r\n]+ -> skip ;
IDENT : [a-zA-Z]+;
COMMENT1 : '//' .*? [\r\n]+ -> skip;
COMMENT2 : '/*' .*? '*/' -> skip;

bool :
  expr ('=' | '!=' | '<' | '>' | '<=' | '>=') expr
;

programme :
  liste_procedure?
  liste_instructions
;

liste_instructions :
 (instruction)+
;

liste_procedure :
  (procedure | fonction)+
;

arg :
  ':'IDENT
;

procedure :
  'pour' IDENT arg* liste_instructions 'fin'
;

fonction :
  'pour' IDENT arg* liste_instructions? 'rend' expr 'fin'
;

instruction :
   'av' expr # av
 | 'td' expr # td
 | 'tg' expr # tg
 | 'lc' # lc
 | 'bc' # bc
 | 're' expr # re
 | 'fpos' expr expr # fpos
 | 'fcc' expr expr expr # fcc
 | 'fcap' expr # fcap
 | 'repete' expr '[' liste_instructions ']' # repete
 | 'store' # store
 | 'move' # move
 | 'donne' '"'IDENT expr # donne
 | 'si' bool '[' liste_instructions ']' ('[' liste_instructions ']')? #si
 | 'tantque' bool '[' liste_instructions ']' # tantque
 | IDENT '('expr*')' # appelprocedure
;

expr :
  FLOAT # float
 | '(' expr ')' # parenthese
 | 'cos' expr # cosinus
 | 'sin' expr # sinus
 | expr ('*' | '/') expr # produit
 | expr ('+' | '-') expr # somme
 | 'hasard' expr # hasard
 | 'loop' # loop
 | ':'IDENT #identificateur
 | IDENT '('expr*')' # appelfonction
;
