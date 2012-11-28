 - [Discussion sur implémenter filter avec foldr](http://stackoverflow.com/questions/3632999/implementing-filter-using-hof-in-haskell)

# Bananes, lentilles, enveloppes et barbelés

_Functional Programming with bananas, lenses, envelopes and barbed wires_ est un article célèbre qui explore différentes formes (ou _patterns_) de récursion. 
C'est à dire que l'on cherche à exprimer la récursion non pas comme une caractéristique du langage mais comme une _fonction d'ordre supérieur_, ou un combinateur comme un autre, comme tel donc susceptible d'être généralisé, réutilisé, composé.

## Types de données récursifs

Quand on définit une structure (un type de données), il est fréquent de définir des types qui soient _récursifs_, c'est à dire qui utilisent des données de leur propre type. 
L'exemple le plus typique en est l'ensemble des entiers naturels, définissable en Java comme suit:

~~~~~~~~~ {.java .numberLines}
public class Natural {

  public static final Natural Zero = new Natural();
  
  public static final Natural succ(Natural natural) {
   return new Natural(natural);
  }
  
  private Natural() {}
  
  private Natural(Natural pred) {
    this.pred = pred;
  }
}
~~~~~~~~~

Un entier est ici construit à l'aide de la méthode `succ` et de la constante `Zero`:
 - soit 0 ;
 - soit le successeur d'un autre entier.
 
La question que l'on peut se poser, si l'on s'intéresse à ce genre de choses, c'est : comment peut-on caractériser algébriquement l'ensemble des entiers ainsi défini, si ce n'est de manière tautologique ? Il peut nous apparaître très naturel d'utiliser un type dans sa propre définition mais c'est parce que nous sommes habitués à raisonner récursivement. 

### Point Fixe

Pour répondre à la question posée, on peut reformuler de manière "compacte" le problème en cherchant à définir le type `Natural` comme la solution d'une équation algébrique (+ joue ici le rôle de `OU`):
```
Natural = Zero  + Succ (Natural)
``` 
équation dont la solution est problématique puisque la variable apparaît des deux côtés de l'équation ! Si l'on substitue naïvement la définition de Natural en partie droite, alors on obtient quelque chose comme `Natural = Zero + Succ (Zero + Succ ( Zero + Succ (Zero +...`  ce qui peut se réécrire en `Natural = Zero + Succ (Zero) + Succ (Succ (Zero)) + ...`. Natural apparait bien comme un ensemble infini d'éléments qui sont soit `Zero` soit de la forme `Succ^n Zero` pour tout $n$ entier. Du point de vue mathématique, la solution d'une équtation de la forme $x = f(x)$ est appelée un *point fixe*, ce qui est bien la forme de l'équation de Natural. On peut donc dire que `Natural` est le point fixe de l'équation $X = Zero + Succ (X)$. Nous disons *le* point fixe, mais ce n'est pas tout à fait exact : comme on ne considére que des nombres finis (même si l'ensemble lui-même est de taille infinie), il s'agit là du *plus petit point fixe*. Il existe en effet des ensembles qui sont des points fixes de cette équation mais dont la cardinalité est plus grande que N car ils contiennent des nombres infinis (en quantité infinie...). 

Ce type de définition étant très courant, il a paru utile de généraliser cette notion de *plus petit point fixe*, d'où l'introduction l'opérateur *μ*. Pour toute fonction f, μf est le plus petit point fixe de f, plus formellement, 
$$
  μf = x \in dom(f), x = f(x) et \forall x' \in dom(f), x \leq x'
$$

Or ici la définition de Natural ne semble pas être une fonction. En fait, pour qu'une définition de type soit une fonction, il faut qu'elle soit une fonction sur des types, prenant en argument des types et retournant des types, en d'autres termes un [foncteur](functors.md). Mais c'est exactement ce que dit la forme $Zero + Succ (X)$ où X désigne un type quelconque, et donc on peut légitimement définir `Natural  = μ(Zero + Succ(x))` comme un ensemble d'éléments point fixe d'un foncteur.

### Définition explicite

Toute cette mécanique est rendu implicite dans tous les langages, même les plus plus sophistiqués comme Haskell, Scala ou Caml. Pour définir un type de données récursif, nul besoin d'utiliser l'opérateur μ, on se contente d'utiliser les possibilités syntaxiques du langage qui autorise l'utilisation du nom d'un type dans sa définition. Mais pour pouvoir généraliser les mécanismes de récursions sous forme de FOS, il est nécessaire de déconstruire cette vision et d'introduire explicitement la récursion.

C'est ce que l'on va faire, en Haskell tout d'abord. 

On introduit d'abord l'opérateur `Mu` comme un nouveau type de données prenant en paramètre un foncteur `f`. Mu a un seul constructeur, `In` qui empaquette le foncteur `f` dans une boucle récursive, ce qui nous donne 2 fonctions permettant de naviguer dans la "pile" de récursion:
 - `In : f (Mu f) -> Mu f` (le constructeur, vu comme une fonction), 
 - `out : Mu f -> f (Mu f)` (l'accesseur de l'unique champ de la structure encapsulée par In).

~~~~~~~~~ {.haskell .numberLines}
-- newtypes in Haskell are cheaps, they do not add any runtime overhead and serve
-- only for the compiler to distinguish types
newtype Mu f = In { out :: (f (Mu f)) }
~~~~~~~~~

Essayons maintenant de définir les entiers comme ci-dessus au moyen de Mu en évitant la récursion explicite et en définissant Natural comme un foncteur:

~~~~~~~~~ {.haskell .numberLines}
-- le foncteur engendrant les entiers naturels
data Natf x = Zero  | Succ x 

-- le type (un simple alias) Natural comme point fixe d'un foncteur
type Natural = Mu Natf
~~~~~~~~~

Voici quelques objets de type Natural que l'on peut construire en utilisant directement les constructeurs de Natf sans se préoccuper de Mu pour l'instant:

```
*Main> let zero = Zero
*Main> let un = Succ Zero
*Main> :t un
un :: Natf (Natf x)
*Main> let deux = Succ un
*Main> :t deux
deux :: Natf (Natf (Natf x))
```

On peut constater que chaque "nombre" a un type différent, ce qui n'est pas très pratique. En utilisan Mu, on uniformise le type d'où la naissance de Natural, un ensemble contenant des objets de type homogène:

```
*Main> let zero = In Zero
*Main> :t zero
zero :: Mu Natf
*Main> let un = In (Succ zero)
*Main> :t un
un :: Mu Natf
*Main> let deux = In (Succ un)
*Main> :t deux
deux :: Mu Natf
```

Tous les nombres ont bien ici le type `Mu Natf` et l'on peut sans problème les combiner, par exemple pour définir l'addition:

~~~~~~~~~ {.haskell}
add :: Natural -> Natural -> Natural
add (In Zero) x = x
add x (In Zero) = x
add (In (Succ x)) (In (Succ x')) = In (Succ (In (Succ (add x x'))))
~~~~~~~~~

### Foncteur et F-Algèbre

Evidemment, c'est théoriquement très intéressant mais ce qu'on veut c'est manipuler des "vrais" nombres, pas de longues chaînes de constructeurs, sauf dans les cas où l'on s'intéresse à la récursion explicite, évidemment. On voudrait donc pouvoir *transformer* des objets de notre type Natural en un type plus commun, par exemple Int. Pour ce faire, notre type de base Natf manque d'un ingrédient: la _fonctorialité_ (ou propriété d'être un foncteur). On a vu que ce qui définissait un foncteur, c'était le fait de posséder une fonction `fmap` possédant quelques bonnes propriétés de compositionnalité. Dans le cas de Natf, cette définition est simple:

~~~~~~~~~ {.haskell .numberLines}
instance Functor Natf where
  fmap f (Zero) = Zero
  fmap f (Succ x) = Succ (f x)
~~~~~~~~~

Dès que l'on a un foncteur `f`, alors pour tout type `a` on peut définir (entre autres myriades de choses) des fonctions de types `h :: f a -> a` qui "déconstruisent" des éléments de `a` "transformés" par `f` en éléments de `a`: c'est comme si on enlevait une couche d'une pelure d'oignon. Ce type de fonction est suffisamment courant pour avoir été nommé, on les appelle des *f-algèbres*. Par exemple, on peut écrire une f-algèbre qui permet de transformer des objets de type `Natf Int` en objets de type `Int` (nos gentils entiers habituels):

~~~~~~~~~ {.haskell .numberLines}
intalgebra :: Natf Int -> Int
intalgebra Zero     = 0
intalgebra (Succ x) = 1 + x
~~~~~~~~~

Cette fonction est très simple et non récursive, elle décrit simplement une correspondance univoque entre des opérations du type de départ (les constructeurs de NAtf) et des opérations du type d'arrivée (les fonctions plus et la constante 0). Ce serait encore plus explicite si l'on pouvait écrire ceci:

~~~~~~~~~ {.haskell .numberLines}
-- does not compile
intalgebra :: Natf Int -> Int
intalgebra Zero = 0
intalgebra Succ = (1+)
~~~~~~~~~

Mais une fois que l'on a cette fonction, on n'est guère avancé car de toute évidence, elle ne peut s'appliquer aux nombres de type `Natural`. C'est ici qu'entre un jeu notre premier "récurseur" d'ordre supérieur: le catamorphisme (roulement de tambour) !

## Catamorphismes

Un *catamorphisme* est donc une _fonction d'ordre supérieure_ permettant de produire une valeur d'un type arbitraire en "repliant" une structure, un type algébrique, récursivement, par application d'un opérateur quelconque sur une valeur initiale. 

Le catamorphisme "canonique" est l'opérateur `foldr` sur les listes:

~~~~~~~~~ {.haskell .numberLines}
foldr :: (a -> b ->  b) -> b -> [a] -> b
foldr op x []     = x
foldr op x (y:ys) = y `op` (foldr op x ys)
~~~~~~~~~

Pour tout opérateur binaire ⊙ et toute valeur x, h = foldr ⊙ x, est un catamorphisme pour les listes de type `[a] -> b`. Le parcours de la liste est imbriqué avec l'application de l'opérateur dans l'appel récursif à `foldr`. Par ailleurs, on a vu ci-dessus que la récursion pouvait être rendue explicite au travers de la structure du type de données, par l'opérateur `Mu`, qui produit un _point fixe_ d'un foncteur quelconque. On aimerait donc pouvoir distinguer, séparer, dans foldr et d'autres opérations du même type qui transforment un type de données récursif en une valeur quleconque, deux entités distinctes:
* le traitement de chaque instance possible d'un foncteur, autrement dit une f-algèbre quelconque ;
* et la récursion. 

Ces deux contraintes peuvent s'exprimer dans le système de type, ce qui nous donne la signature suivante pour `cata`:

~~~~~~~~~ {.haskell .numberLines}
cata :: Functor f => (f a -> a) -> (Mu f -> a)
~~~~~~~~~

`cata` est donc une fonction qui, à partir d'une f-algèbre, produit une fonction transformation un point fixe du foncteur f en une valeur. Sa définition est la suivante et l'on voit bien que la récursion y est explicite:

~~~~~~~~~ {.haskell .numberLines}
cata h = h . fmap (cata h) . out
~~~~~~~~~

On est désormais équipé pour appliquer notre fonction `intalgebra` définie ci-dessus pour transformer les nombres algébriques en entiers "sympathiques":

~~~~~~~~~ {.haskell .numberLines}
toInt :: Natural -> Int 
toInt = cata intalgebra
~~~~~~~~~

et l'on peut utiliser toint pour obtenir de "vrais" entiers:

```
*Main> toint (In Zero)
0
*Main> toint (In (Succ (In (Succ (In Zero)))))
2
*Main> 
```

## Anamorphismes

Dans l'univers des types, chaque concept a toujours son _duel_, un concept similaire mais inverse. Le duel d'une f-algèbre `h :: f a -> a` est une *f-coalgèbre*, `g :: a -> f a`, c'est à dire une fonction qui construit un élément d'un foncteur à partir d'une valeur. A titre d'exemple, on peut définir une `intcoalgebra` qui tranforme un nombre entier en `Natural`:

~~~~~~~~~ {.haskell .numberLines}
intcoalgebra :: Int -> Natf Int
intcoalgebra 0 = Zero
intcoalgebra n = Succ (n - 1)
~~~~~~~~~

From FPOO:
Stream functions. It is very interesting to note that the value, step, and eos interface
of ContFunc (see section 8.10 on page 130) is justified by a categorical
interpretation of streams as coinductive types [Jacobs & Rutten97]. Here,
streams over a type A are modeled as a state of type B (state of a ContFunc
object) and two functions: Function h : B!A produces a new stream element
(value) and function t : B!B produces a new state (step).
Then, an anamorphism on h and t (a stream generating function) is defined
to be the unique function f : B!Str A such that diagram 8.6 on the next page
commutes. A stream generator f , hence, needs to build a list cell (eventually
StreamValue) from calling an element generator h (value) and itself composed
with a state transformer t (step): f = Cons ∙ 〈 head, f ∙ tail 〉 [Pardo98b].
This view concerns infinite streams only. Introducing finite streams

~~~~~~~~~ {.haskell .numberLines}
head :: Stream a → a
tail :: Stream a → Stream a

-- generator function
f    :: b → Stream a
~~~~~~~~~
