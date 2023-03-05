% Факты
parent(иван, катя).
parent(иван, женя).
parent(иван, егор).
parent(лиза, катя).
parent(лиза, женя).
parent(лиза, егор).
parent(егор, миша).
parent(василина, миша).
parent(анатолий, василина).
parent(анатолий, влада).
parent(ксюша, василина).
parent(ксюша, влада).
parent(женя, галина).
parent(генадий, галина).
parent(галина, стас).
parent(алекс, стас).
parent(влада, костя).
parent(влада, том).
parent(влада, дава).
parent(денис, костя).
parent(денис, том).
parent(денис, дава).
parent(вика, диана).
parent(костя, диана).

married(иван, лиза).
married(анатолий, ксюша).
married(генадий, женя).
married(егор, василина).
married(влада, денис).
married(галина, алекс).
married(вика, костя).

woman(лиза).
woman(ксюша).
woman(катя).
woman(женя).
woman(галина).
woman(василина).
woman(влада).
woman(вика).
woman(диана).

man(иван).
man(анатолий).
man(егор).
man(генадий).
man(алекс).
man(миша).
man(стас).
man(денис).
man(костя).
man(том).
man(дава).

% Правила
check_married(X, Y):-
  married(X, Y); married(Y, X).

dad(X, Y):-
    parent(X, Y),  man(X).

mother(X, Y):-
    parent(X, Y), woman(X).

son(X, Y):-
    parent(Y, X), man(X).

daughter(X, Y):-
    parent(Y, X), woman(X).

sister(X,Y):-
    parent(Z, X), parent(Z, Y), parent(Z1, X), parent(Z1, Y), married(Z, Z1), 
    woman(X), X\=Y.

brother(X, Y):-
    parent(Z, X), parent(Z, Y), parent(Z1, X), parent(Z1, Y), married(Z, Z1), 
    man(X), X\=Y.

grandfather(X, Y):-
  	parent(Parent, Y), dad(X, Parent).

grandmother(X, Y):-
  	parent(Z, Y), mother(X, Z).

uncle(X, Y):-
  	parent(Z, Y), brother(X, Z).


