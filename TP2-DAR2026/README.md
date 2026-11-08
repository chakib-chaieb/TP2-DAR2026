# TP 2 – Les sockets en mode connecté TCP : E/R de CC & objets

**Matière :** Développement d'applications réparties
**Faculté des Sciences de Sfax – Département d'Informatique – LSI 3 – AU 2026-2027**
**Enseignante des TPs :** Zeineb DAOUD
**Étudiant :** Chaieb Chakib

---

## 1. Description

Ce dépôt contient les deux activités du TP 2 (calculatrice client/serveur via sockets TCP en Java) :

| Activité | Thème | Flux utilisés |
|---|---|---|
| **2-1** | Échange de chaînes de caractères (CC) | `BufferedReader` / `PrintWriter` |
| **2-2** | Échange d'objets | `ObjectOutputStream` / `ObjectInputStream` + `Serializable` |

Dans les deux cas, le client envoie une opération complète (opérande 1, opérateur, opérande 2), le serveur calcule et renvoie le résultat, et le client l'affiche.
Opérations supportées : addition `+`, soustraction `-`, multiplication `*`, division `/` (avec gestion de la division par zéro).

## 2. Structure du projet

```
ChaiebChakib_LSI3_DevAppReparties/
├── src/
│   ├── activite21/
│   │   ├── Serveur.java
│   │   └── Client.java
│   └── activite22/
│       ├── Operation.java      (implements Serializable)
│       ├── Serveur.java
│       └── Client.java
├── images/                     (captures d'écran)
├── .gitignore
└── README.md
```

## 3. Environnement

- JDK ≥ 1.8
- IntelliJ IDEA
- Git

## 4. Exécution (IntelliJ IDEA)

1. `File > Open…` puis choisir le dossier du projet (si besoin : clic droit sur `src` > *Mark Directory as* > *Sources Root*).
2. Lancer **d'abord** le `Serveur` (clic droit sur `Serveur.java` > *Run*).
3. Lancer ensuite le `Client` (clic droit sur `Client.java` > *Run*).
4. Saisir une opération, par exemple `55 * 25`. Taper `quit` pour quitter.

> Les deux activités utilisent le port **1234** : arrêter le serveur de l'activité 2-1 avant de lancer celui de l'activité 2-2.

En ligne de commande :

```bash
javac -d out src/activite21/*.java src/activite22/*.java
java -cp out activite21.Serveur      # terminal 1
java -cp out activite21.Client       # terminal 2
```

## 5. Activité 2-1 : échange de CC

### Principe
- Le client lit la saisie et **valide la syntaxe avant l'envoi** grâce à une expression régulière (rejette `25 ** 55`, `abc + 3`, …).
- Il envoie trois lignes : opérande 1, opérateur, opérande 2 (`println`).
- Le serveur les lit (`readLine`), calcule, puis renvoie le résultat sous forme de chaîne.
- Le client lit et affiche le résultat.

### Captures d'écran
![Structure du projet](images/01_structure_projet.png)
![2-1 Serveur](images/02_21_serveur.png)
![2-1 Client](images/03_21_client.png)

## 6. Activité 2-2 : échange d'objets

### Principe
- `Operation` est une classe **`Serializable`** contenant `operande1`, `operateur`, `operande2`.
- Le client crée un objet `Operation` et l'envoie avec `ObjectOutputStream.writeObject()`.
- Le serveur le reçoit avec `ObjectInputStream.readObject()`, récupère ses données (getters), calcule et renvoie le résultat (un `Double`, ou un `String` en cas d'erreur).
- Le client lit et affiche le résultat.

> **Remarque :** côté client **et** serveur, l'`ObjectOutputStream` est créé et `flush()`é **avant** l'`ObjectInputStream`, sinon les deux côtés restent bloqués à attendre l'en-tête du flux (deadlock).

### Captures d'écran
![2-2 Serveur](images/04_22_serveur.png)
![2-2 Client](images/05_22_client.png)

## 7. Exemple d'exécution

```
Entrez une opération (ex: 55 * 25) ou 'quit' : 55 * 25
Résultat : 1375
Entrez une opération (ex: 55 * 25) ou 'quit' : 10 / 4
Résultat : 2.5
Entrez une opération (ex: 55 * 25) ou 'quit' : 5 / 0
Erreur : division par zéro
Entrez une opération (ex: 55 * 25) ou 'quit' : 25 ** 55
Syntaxe invalide. Format attendu : <nombre> <+|-|*|/> <nombre>
```

## 8. Dépôt GitHub

![GitHub](images/06_github.png)

Dépôt : https://github.com/chakib-chaieb/ChaiebChakib_LSI3_DevAppReparties
