# La Machine de Turing #

## Objectif ##
Notre objectif avec ce projet est de simuler une machine de Turing en Java pour réaliser quelques opérations de base (addition, soustraction, etc.) grâce aux connaissances acquises dans le programme du premier cycle, notamment la programmation orientée objet et les interfaces graphiques.

## Introduction ##
Une machine de Turing est un modèle proposé par Alan Turing en 1936, afin de déterminer la limite de calculabilité des machines : si une opération ne peut pas être effectuée avec une machine de Turing, alors elle ne pourra pas être implémentée sur un ordinateur, peu importe ses capacités. C’est donc un modèle très important pour l’informatique théorique.

La machine est composée de 4 parties : un ruban de longueur infinie divisé en cases pouvant contenir un symbole, une table d’actions, un registre d’état et une tête de lecture et écriture.

Chaque fois que la tête lit une case, elle va agir selon la table d’actions, en fonction du symbole de la case et de l’état de la tête (stocké dans le registre). Elle peut écrire un nouveau symbole dans la case, se déplacer à gauche ou à droite, et changer d’état. Quand l’état final est atteint (“Halt”), alors l’opération est finie.

Comme Java est un langage Turing-complet, c’est-à-dire qui permet d’effectuer toutes les fonctions calculables par une machine de Turing, il est possible de créer une machine de Turing avec.

## Présentation des fonctionnalités ##
Avec l’interface graphique, l’utilisateur choisit l’opération à effectuer en sélectionnant le fichier texte contenant les données nécessaires. Ces données doivent être enregistrées dans un certain format (une chaîne de valeurs), sinon elles seront rejetées.
Il est aussi possible de créer sa propre opération. Une nouvelle fenêtre permet à l’utilisateur de définir les différents états, l’alphabet (l’ensemble des symboles utilisables), et la table d’actions. L’utilisateur peut, une fois l’opération entièrement définie, l’enregistrer sous forme d’un fichier texte compatible.

Une fois l’opération choisie, l’utilisateur peut entrer les données nécessaires, puis la machine de Turing commence à traiter les données avec la table d’action chargée. On pourra éventuellement créer une animation pour visualiser le processus, afin d’aider à comprendre le fonctionnement de la machine. Quand l’état final est atteint, le résultat est alors affiché.

L’utilisateur doit être capable d’arrêter l’opération en cours avec l’interface graphique, à cause du problème de l’arrêt. Alan Turing a en effet démontré qu’il n’est pas toujours possible de déterminer si une opération peut atteindre son état d’arrêt. On dit que le problème de l’arrêt est indécidable.

## Ébauche de l’interface graphique ##
À gauche, la fenêtre principale pour exécuter et visualiser une opération. À droite, la fenêtre permettant de créer une opération en définissant les états, l’alphabet, et la table d’actions.
![Ébauche de l’interface graphique](/ihm.jpg)
