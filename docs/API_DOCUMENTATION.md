# Dokumentacja API 
### TODO:
-  instrukcje niech bedą w tablicy stringów 
-  Poprawne pobieranie składników 

## Uwierzytelnianie
 wszystkie endpointy pod adresem /v1/public muszą przechodzić bez wymagania autoryzacji, potem musimy zrobić tak, żeby v1/secure wymagały tokenu JWT w headerze. 

 ## Endpointy (/v1/public) 
 ### Pobieranie drinka po id

- **Metoda HTTP**: `GET`
- **URL**: `/drink/{id}`
- **Opis**: Zwraca szczegóły drinka o podanym ID.
- **Parametry**: `id` - identyfikator drinka.
- **Przykładowy żądanie**: curl -X GET http://localhost:8080/v1/public/drink/1
- **Przykładowa odpowiedź**:
    ```json
  {
    "idDrink": 1,
    "apiId": 11000,
    "name": "Mojito",
    "instructions": [
      "Muddle mint leaves with sugar and lime juice.",
      "Add a splash of soda water and fill the glass with cracked ice.",
      "Pour the rum and top with soda water.",
      "Garnish and serve with straw."
    ],
    "glassType": "Highball glass",
    "image": "https://www.thecocktaildb.com/images/media/drink/metwgh1606770327.jpg",
    "category": "Cocktail",
    "alcoholic": true,
    "ingredients": [
      {
        "idIngredient": 1,
        "name": "Mint",
        "amount": "5 leaves"
      },
      {
        "idIngredient": 2,
        "name": "White rum",
        "amount": "2 oz"
      }
    ]
  }
    ```
### Pobieranie wszystkich drinków
- **Metoda HTTP**: `GET`
- **URL**: `/drink/all`
- **Opis**: Zwraca listę wszystkich drinków z bazy danych.
- **Przykładowy żądanie**: `curl -X GET http://localhost:8080/v1/public/drink/all`
- **Przykładowa odpowiedź**: (struktura odpowiedzi jak powyżej z listą wszystkich drinków)
### Pobieranie popularnych drinków

- **Metoda HTTP**: `GET`
- **URL**: `/drink/popular`
- **Opis**: Zwraca listę 10 najpopularniejszych drinków. Na początku to pewnie będzie 10 randomowych drinkow ale później fajnie by było to zrobić na zasadzie popularnośći
- **Przykładowy żądanie**: curl -X GET http://localhost:8080/v1/public/drink/popular
- **Przykładowa odpowiedź**: (struktura odpowiedzi jak powyżej z listą 10 drinków)

### Wyszukiwanie drinków

- **Metoda HTTP**: `GET`
- **URL**: `/drink/search`
- **Opis**: Pozwala na wyszukiwanie drinków po nazwie.
- **Parametry**: `query` - fraza wyszukiwania.
- **Przykładowy żądanie**: curl -X GET "http://localhost:8080/v1/public/drink/search?query=Mojito"
- **Przykładowa odpowiedź**: (struktura odpowiedzi jak powyżej z listą drinków spełniających kryteria wyszukiwania)

### Drink dnia

- **Metoda HTTP**: `GET`
- **URL**: `/drink/daily`
- **Opis**: Zwraca jeden losowy drink, później będzie zwracać losowy drink bazując na preferencjach użytkownika.
- **Przykładowy żądanie**: curl -X GET http://localhost:8080/v1/public/drink/daily
- **Przykładowa odpowiedź**: (struktura odpowiedzi jak powyżej z jednym losowo wybranym drinkiem)
### Pobieranie informacji o składniku po ID

- **Metoda HTTP**: `GET`
- **URL**: `/ingredient/{ID}`
- **Opis**: Zwraca szczegółowe informacje o składniku o podanym ID. Ogólnie to przez cocktaildb da sie jakoś pobrać zdjecie dla każdego składnika ale nie dostajemy go w odpowiedzi serwera xD
- **Parametry**: `ID` - identyfikator składnika.
- **Przykładowy żądanie**: curl -X GET http://localhost:8080/v1/public/ingredient/552
- **Przykładowa odpowiedź**:
    ```json
  {
    "idIngredient": "552",
    "strIngredient": "Elderflower cordial",
    "strDescription": "Elderflower cordial is a soft drink made largely from a refined sugar and water solution and uses the flowers of the European elderberry. Historically the cordial has been popular in North Western Europe where it has a strong Victorian heritage.",
    "strType": "Cordial",
    "strAlcohol": "No",
    "strABV": null
  }
    ```
### Filtrowanie drinków

- **Metoda HTTP**: `GET`
- **URL**: `/drink/filter`
- **Opis**: Zwraca drinki pasujące do zadanego zapytania. Możliwe jest filtrowanie po kategorii drinków, zawartości alkoholu oraz typie szkła.
- **Parametry**: 
  - `category` - kategoria drinków,
  - `alcoholic` - informacja o zawartości alkoholu,
  - `glassType` - typ szkła.
- **Przykładowy żądanie**: curl -X GET 'http://localhost:8080/v1/public/drink/filter?category=Cocktail&alcoholic=false&glassType=Highball+glass'
- **Przykładowa odpowiedź**:
    ```json
  [
    {
      "idDrink": 1,
      "apiId": 11000,
      "name": "Non-alcoholic Mojito",
      "instructions": [
        "Muddle mint leaves with sugar and lime juice.",
        "Add a splash of soda water and fill the glass with cracked ice.",
        "Pour the non-alcoholic rum and top with soda water.",
        "Garnish and serve with straw."
      ],
      "glassType": "Highball glass",
      "image": "https://www.thecocktaildb.com/images/media/drink/nonalcoholversion.jpg",
      "category": "Koktajl",
      "alcoholic": false,
      "ingredients": [
        {
          "idIngredient": 1,
          "name": "Mint",
          "amount": "5 leaves"
        },
        {
          "idIngredient": 2,
          "name": "Non-alcoholic rum",
          "amount": "2 oz"
        }
        // więcej składników
      ]
    }
    // więcej drinków pasujących do zapytania
  ]
    ```

## Roadmap

- ### Zrobienie osobnych enpointów pod adresem /v1/secure/ zabezpieczonych przez JWT
- ### DrinkBuilder - generowanie drinków po składnikach itp
- ### Autoryzacja użytkownika 💀
- ### A potem to już ogarniecie preferencji, dodawania do ulubionych, zapisywania w barze itp :)