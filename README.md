# Poker

## Input Validity Assumptions
- Pas de doublons de cartes dans la liste d'éntrée.
- Les entrées sont des objets `Card` valides (valeur et couleur non nulles).
- La liste contient au moins 5 cartes

## Valid Input
- `List<Card>` contenant entre 5 et 7 cartes.
- Les cartes peuvent etre dans n'?'importe quel ordre

## Deterministic Output (chosen5)
- `PokerHandCombinaison.getBestCombination` retourne toujours exactement 5 cartes dans `chosenCards`.
- Pour une meme liste d'entree, les 5 cartes choisies et la catégorie sont déterministes.
- Quand plusieurs mains de 5 cartes donnent la meme catégorie et le meme rang, l'implementation actuelle en choisit une de maniere coherente selon son ordre interne (tri par valeur et premiers matchs).
