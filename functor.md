# Foncteurs

Les foncteurs sont parmi les concepts les plus simples de la programmation fonctionnelle. Dans un univers fortement typé, un foncteur est avant tout un _constructeur de type_, c'est à dire une _fonction sur les types_, possédant certaines propriétés particulières. Plus prosaïquement, un foncteur est un _type générique_ qui offre une certaine interface : la fonction traditionnellement appelée *map*. Si l'on ignore la question du typage, un foncteur est un _conteneur_ de valeurs offrant une opération permettant de manipuler la valeur contenue en son sein.

La fonction *map* est une fonction _d'ordre supérieur_, c'est à dire une fonction qui opère sur des *fonctions*. Etant donné un foncteur $F$ et des types $A$ et $B$ quelconques, sa signature est simple:

$$
map : (A \rightarrow B) \rightarrow (F A \rightarrow F B)
$$

Autrement dit, map _transforme_ une fonction quelconque en une fonction opérant sur des valeurs de même type mais _contenues dans le foncteur_ sur lequel elle est définie. Une autre manière de voir les choses, la vision catégorique pourrait on dire, est qu'un foncteur est une "fonction" qui transforma à la fois des valeurs simples et des fonctions entre valeurs simples.

Concrètement dans un langage de programmation, les deux transformations sont découplées:
* la transformation de valeurs est _implicite_ dans le fait que le conteneur est un foncteur: *Liste* est un foncteur parce que je peux construire, pour tout type d'objet A, une *Liste de A*
* la transformation de fonctions est l'opération *map* qui est typiquement, dans les langages objets, une méthode des instances de foncteurs. 

Un foncteur doit théoriquement respecter une règle pour pouvoir prétendre à ce titre : conserver la composition de fonctions. Mathématiquement, cela signifie que, étant donnés un foncteur $F$ et deux fonctions $f : A → B$ et $g : B → C$:

$$
F (g ∙ f) = F g ∙ F f 
$$

Cette règle somme toute assez naturelle, elle exprime le simple fait que le foncteur ne change pas les valeurs qu'il contient, qu'il est un simple conteneur de valeurs. 
