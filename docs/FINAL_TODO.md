# TODO 

## Organizacja kodu
- Niech login i register beda w osobnym controllerze `/api/auth/login`, `/api/auth/register`, `/api/auth/logout`

## Endpointy w UserController  GET (`/api/user`)
## Wszystkie endpointy `/api/user` musza wymagać authoryzacji (ciasteczka z sessionToken)
### Pobieranie CurrentUsera po ciasteczku z sessionToken, niech zwraca obiekt usera (najwazniejsze zeby bylo username, email, bary, polubione)
### Dodawanie do polubionych POST/PUT (`/api/user/favourites`), niech zwraca liste ID polubionych drinkow (lub cale drinki jak tam wygodniej)
### Tworzenie baru POST/PUT (`/api/user/bar`)
### Pobranie danych o barze uzytkownika GET (`/api/user/bar/:barID`)
### Usuniecie baru DELETE (`/api/user/bar/:barID`)
### Dodanie drinka do baru POST/PUT (`/api/user/bar/:barID`)
### Modyfikacja calego obiektu baru PATCH (`/api/user/bar/:barID`) (opcjonalnie)
### Dodanie skladnika do "schowka skladnikow uzytkownika" POST/PUT (`/api/user/ingredient`)
### Pobranie wszystkich skladnikow ze schowka uzytkownika GET (`/api/user/ingredient`)