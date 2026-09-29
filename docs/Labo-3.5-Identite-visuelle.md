# Labo 3.5 — L’identité visuelle de l’Église de l’Empire

Ce labo documente les modifications réalisées dans `C:\Liberté\DevWebAv`, entre le labo 3 (catalogue et formulaires) et le labo 4 (panier en session). Il permet de comprendre, reproduire et personnaliser la présentation de la boutique.

## Objectif et résultat

Créer une boutique sombre inspirée de la maquette fournie : en-tête impérial, photographie de cathédrale, titres à empattements, catégories latérales, photographies de produits et boutons rouges. L’accueil, le catalogue, la recherche, les fiches produits, l’inscription et les confirmations partagent cette identité.

Les six produits existants restent les données du catalogue. Les illustrations ont été générées pour ces produits. La maquette est une référence graphique : les favoris, stocks et promotions qu’elle montre ne deviennent pas des fonctionnalités fictives. Le bouton d’une carte ouvre une fiche ; la fiche permet toujours de prévisualiser l’ajout. Le véritable panier viendra au labo 4.

## Appui sur le cours

| Document local | Notions appliquées |
| --- | --- |
| `C:\Liberté\devweb_cours\Module 4.pdf`, pages 3 et 5 | Séparer contenu et présentation ; ranger CSS et images dans `static`, les vues dans `templates`. |
| Module 4, pages 7–11 | Contenu dynamique Thymeleaf et fragments réutilisables. |
| Module 4, page 13 | Charger la feuille de style avec `th:href`. |
| Module 4, pages 14–20 | Adaptation aux tailles d’écran, points de rupture, inspection dans le navigateur. |
| `C:\Liberté\devweb_cours\Module 6.pdf`, pages 3–12 | Layout commun, pages clientes, `layout:decorate`, `layout:fragment` et noms de vues. |

Le module 4 présente Bootstrap pour le responsive. Ici, le même objectif est réalisé en CSS natif avec Grid, Flexbox et des media queries : c’est un complément technique à ce module, pas une utilisation de ses classes Bootstrap. Aucune bibliothèque front-end ni police distante n’a été ajoutée.

## Étape 1 — Comprendre qui fait quoi

1. Le navigateur demande `/catalogue`.
2. Le contrôleur prépare les catégories et produits dans le `Model`, puis retourne `"catalogue"`.
3. Thymeleaf assemble `catalogue.html`, le layout et les fragments, en remplaçant les expressions par leurs valeurs.
4. Le navigateur reçoit du HTML. Il demande ensuite `/css/static.css` et les images.
5. Le navigateur applique les règles CSS à ce HTML.

Une classe CSS comme `product-card` est une étiquette HTML de présentation. Ce n’est ni une classe Java, ni une entrée du dictionnaire `Model`. La feuille CSS ne recherche aucun produit et ne valide aucun formulaire.

## Étape 2 — Repérer les fichiers

```text
src/main/resources/
├── static/
│   ├── css/static.css
│   └── images/
│       ├── empire-mark.svg
│       ├── imperial-cathedral.png
│       └── products/
│           ├── cal-001.png
│           ├── pat-001.png
│           ├── enc-001.png
│           ├── nav-001.png
│           ├── cor-001.png
│           └── eto-001.png
└── templates/
    ├── layout/layout.html
    ├── fragments/fragments.html
    ├── welcome.html
    ├── catalogue.html
    ├── productLookup.html
    ├── productDetail.html
    ├── registration.html
    ├── cartPreview.html
    ├── registrationPreview.html
    ├── company.html
    └── error/404.html
```

`static` est un dossier de ressources, pas une partie de l’URL publique : `static/css/static.css` est servi à `/css/static.css`.

Dans le layout, la feuille est chargée avec :

```html
<link rel="stylesheet" th:href="@{/css/static.css}">
```

`@{...}` construit une URL adaptée à l’application. Le navigateur ne voit plus `th:href`, mais un attribut HTML `href`.

## Étape 3 — Réutiliser le décor

`layout/layout.html` assemble l’en-tête, la bannière illustrée, le contenu principal et le pied de page. Les fragments `banner`, `hero` et `footer` vivent dans `fragments/fragments.html`.

Une page cliente déclare :

```html
<html layout:decorate="~{layout/layout}">
  <body>
    <main id="main-content" layout:fragment="main-content" class="page-content">
      <!-- Le contenu propre à cette page -->
    </main>
  </body>
</html>
```

Le fichier réel déclare également les espaces de noms `th` et `layout`. Le contenu de ce `main` remplace la zone du même nom dans le layout. Changer l’en-tête dans son fragment met ainsi à jour toutes les pages qui l’utilisent. La page 404 utilise les mêmes ressources et fragments dans sa structure autonome.

**Manipulation :** change le texte du pied de page dans le fragment, puis visite l’accueil et la recherche. Le texte change aux deux endroits sans modifier leurs contrôleurs.

## Étape 4 — Définir la palette et la typographie

Le début de `static.css` contient des variables :

```css
:root {
  --bg: #0b0e11;
  --panel: #141a20;
  --ink: #f0eee8;
  --muted: #b3bbc2;
  --red: #970e1e;
  --serif: Georgia, "Times New Roman", serif;
  --sans: "Segoe UI", Arial, sans-serif;
}
```

`var(--red)` réutilise la même couleur pour plusieurs éléments. `:root` désigne la racine du document HTML ; ces variables CSS ne sont pas des variables Spring. Georgia donne le caractère solennel aux titres ; Segoe UI ou Arial garde les descriptions et champs lisibles.

`box-sizing: border-box` inclut bordures et padding dans les dimensions déclarées. `margin` espace les éléments entre eux ; `padding` espace leur contenu de leur bord. Une bordure fine sépare les surfaces sombres sans devoir employer un fond blanc.

**Manipulation :** change temporairement `--red`, recharge la page, observe tous les composants concernés, puis restaure la couleur.

## Étape 5 — Construire le catalogue

La classe `.catalog-layout` crée les deux colonnes : catégories à gauche, produits à droite. `.product-grid` organise les cartes. `minmax(0, 1fr)` autorise une colonne à se réduire sans que son contenu impose une largeur excessive.

Chaque carte utilise le fragment paramétré `productCard(product)` :

```html
<th:block th:each="product : ${products}">
  <article th:replace="~{fragments/fragments :: productCard(${product})}"></article>
</th:block>
```

`products` est la liste fournie par le contrôleur. `product` est l’élément courant de la boucle. Il devient le paramètre du fragment. Le bloc extérieur évite de faire dépendre un remplacement de fragment d’une boucle portée par la même balise. `th:block` ne crée pas de conteneur dans le HTML final.

La carte affiche le nom, la description, la référence et le prix du véritable objet Java, avec un lien vers `/produits/{id}`. Son image utilise une convention explicite : référence en minuscules, suffixe `.png`.

```html
<img th:src="@{/images/products/{reference}.png(reference=${#strings.toLowerCase(product.reference)})}"
     th:alt="${product.name}">
```

Ainsi `CAL-001` désigne `images/products/cal-001.png`. Ajouter un produit exige actuellement d’ajouter son fichier selon cette convention. Un futur catalogue persistant pourra stocker un chemin d’image propre au produit. Le texte `alt` décrit l’article aux personnes qui ne voient pas l’image.

Les photos de cartes sont carrées pour montrer les objets entiers. La bannière, décorative, utilise une image de fond et un dégradé sombre ; son titre et son slogan restent du vrai texte HTML.

**Petite adaptation Java réalisée :** dans `CatalogController`, la visite de `/catalogue` sans `categoryId` ajoute désormais tous les produits au modèle. La sélection d’une catégorie conserve son filtrage existant. Cela remplit la page principale avec les six produits ; ce n’est pas le CSS qui décide quels articles appartiennent à une catégorie.

## Étape 6 — Habiller les formulaires sans modifier leur rôle

| Page | Présentation ajoutée | Comportement conservé |
| --- | --- | --- |
| Recherche | Panneau de saisie et liste de références côte à côte | POST de la référence, erreurs, redirection vers la fiche. |
| Fiche produit | Grande photo, description, prix, groupe quantité/bouton | Validation de la quantité et prévisualisation. |
| Inscription | Champs dans une grille, labels et erreurs lisibles | Liaison aux propriétés Java, pays dynamiques, validations. |
| Confirmations et société | Panneau de lecture commun | Contenu existant. |

`th:object`, `th:field`, `th:action`, `method="post"` et les messages `th:errors` gardent leur fonction. Ajouter `class="form-field"` ne change pas le nom envoyé au serveur. Les protections CSRF restent actives.

Les boutons sont réservés aux actions et les liens à la navigation. Les erreurs restent proches du champ concerné. Un contour de focus rend visible la navigation au clavier ; un lien « aller au contenu » permet d’éviter l’en-tête répété.

## Étape 7 — Adapter l’affichage

Les media queries appliquent d’autres règles selon la largeur du navigateur :

| Largeur | Principales adaptations |
| --- | --- |
| Au moins 1600 px | Quatre colonnes de produits. |
| De 1101 à 1599 px | Trois colonnes. |
| Jusqu’à 1100 px | Deux colonnes et en-tête plus compact. |
| Jusqu’à 760 px | Catégories au-dessus du catalogue ; fiche et recherche sur une colonne ; illustration latérale masquée. |
| Jusqu’à 440 px | Inscription sur une colonne, espacements et textes ajustés. |

Par exemple, `@media (max-width: 760px)` signifie « appliquer ces règles si la largeur disponible est au plus 760 pixels CSS ». Cela ne crée pas une seconde page ni une autre route Spring.

Les préférences de réduction des animations sont respectées par `prefers-reduced-motion`. Aucun JavaScript n’est nécessaire pour cette présentation.

**Manipulation :** ouvre les outils de développement du navigateur, sélectionne une carte puis observe les règles `.product-grid`. Réduis la largeur : repère quelle media query remplace la règle précédente.

## Étape 8 — Vérifier

Lancer le projet puis ouvrir `http://localhost:8080/catalogue`. Si une ancienne feuille reste en cache, utiliser `Ctrl+F5`. Si le processus utilise un ancien build, relancer l’application.

- Parcourir les trois catégories : seuls leurs produits doivent apparaître.
- Rechercher `CAL-001`, puis une référence inexistante : vérifier la fiche et le message d’erreur.
- Prévisualiser deux exemplaires depuis la fiche : vérifier la quantité affichée.
- Tester une inscription invalide : les messages du labo 3 doivent rester visibles.
- Naviguer avec Tab et vérifier l’indicateur de focus.
- Tester une largeur de téléphone et vérifier l’absence de défilement horizontal.

La commande `gradlew.bat build` a réussi avec les 46 tests existants. Le test de catégorie contrôle désormais les catégories des objets du modèle : une illustration éditoriale peut mentionner un encensoir sans qu’il fasse partie des produits filtrés.

Des vérifications dans Chrome ont couvert les pages accueil, catalogue, fiche, recherche et inscription aux largeurs 360, 390, 768, 1024 et 1440 pixels. Les captures permettent aussi de vérifier le rendu des cartes et formulaires.

## Bilan des changements

La feuille CSS commune, les fragments et les vues ont été retravaillés ; sept images originales et un emblème SVG ont été ajoutés. Le contrôleur du catalogue prépare aussi la liste complète pour sa page d’entrée. Aucun modèle métier supplémentaire, panier en session ou stockage de compte n’est introduit par ce labo.

Les prompts exacts des sept illustrations, produites avec ImageGen, figurent dans `Labo-3.5-prompts-images.json`, à côté de ce document dans le dossier `docs` du projet. Leurs chemins de destination y sont indiqués. L’emblème SVG est un dessin vectoriel local distinct.

**Pour vérifier ta compréhension :** explique pourquoi changer `--red` ne nécessite aucune modification d’un contrôleur ; pourquoi une carte produit n’est pas un formulaire ; et pourquoi changer une classe CSS ne change pas la propriété Java liée par `th:field`.
