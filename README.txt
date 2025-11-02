Bonjour, ce projet a été réalisé par alecu luca.

comment utiliser le code:
1 récupérer l'entièreté du contenu du dossier VersionF et le mettre dans un dossier a part

2 ouvrir le terminal de votre choix linux ou windows (je vous conseille cependant linux)

3 ce déplacer jusqu'au dossier du projet dans le terminal a l'aide de cd

4 vous pouvez choisir de recompiler le code ou pas si vous avez conservé les fichiés .class
  dans le cas vous souhaiteriez recompiler le code faite rm *.class puis javac *.java 
  (j'utilise personnelement la version java 21 mais d'autres versions ne devraient pas poser de problème)

5 ensuite vous pouvez vous servir du code de 2 façon différentes:
  java Lancement.java (cette méthode executera le code sur un tableau de 10000 entiers aléatoires compris entre 0 et 5000)
  java Lancement.java Test.txt (cette méthode executera le code sur le tableau contenu dans Test.txt)
    attention pour utiliser cette méthode le fichier Test.txt dois ce trouver dans le même dossier que les autres fichiers
    de plus le contenu du fichier Test.txt est de la forme 1 2 3 4 5 6 7 ou par exemple il designe le tableau [1,2,3,4,5,6,7]
    le fichier Test.txt ne doit pas contenir de chiffre négatifs.
