# Obligatorisk oppgave 3 i Algoritmer og Datastrukturer

Denne oppgaven er en innlevering i Algoritmer og Datastrukturer. 
Oppgaven er levert av følgende student:
* Petter Sundh, S356095, s356095@oslomet.no


# Oppgavebeskrivelse

I oppgave 1 så gikk jeg frem ved å hente kildekoden fra kompendiet 5.2 3 a) for så å forsikre meg om at alle foreldre og barn forhold er i orden.

I oppgave 2 så brukte jeg en rekursiv hjelpemetode til å traversere treet og inkrementere en teller hver gang verdien i noden tillsvarte ønsket verdi.
I hoved metoden kalte jeg hjelpemetoden på treets rot. Treet traverseres da og antall instanser av en verdi returneres.

I oppgave 3 lagde jeg først metoden førstepostorden(), her brukte jeg en while løkke til å traversere treet til dens yttertste venstre side på nederste nivå.
While løkken prioriterer venstre flyttinger og gjør kun en bevegelse til høyre når først venstre barn ikke eksisterer. om ingen av dem eksisterer er vi nå på
noden som ligger først i postorden.

Deretter lagde jeg metoden nestepostorden(), her sjekker vi først at foreldre til pekeren ikke er null, om dette er tilfellet er nåværende node siste i postorden.
Metoden sjekker så om vi er et høyrebarn, et venstrebarn uten "søsken" høyrebarn eller et venstre barn med "søsken" og finner neste i postorden avhengig av dette.

I oppgave 4 kodet jeg først hjelpemetoden postorden(), her bruker vi førstepostorden på treets rot for å finne første node i treets postorden, vi gjør så en 
while løkke som kjører så lenge peker ikke er null. I while løkken utfører vi først oppgaven fra testene våre på verdi i nåværende node før vi kaller nestepostorden()

Deretter kodet jeg postordenRecursive(), her brukte jeg en hjelpemetode postordenRecursiveHelp(). Denne metoden traverserer treet i postorden og utfører testens oppgave på hver node.
jeg kalte så postordenRecursiveHelp i postordenRecursive på treets rot.

I oppgave 5 kodet jeg metodene serialize() og deserialize(), i serialize metoden bruker vi en kø for for å holde nodene våre før vi henter dem ut og skriver verdien deres til et array.
Siden treet skal traverseres i nivå orden legger vi først inn rot i køen for så å kjøre en while løkke som går så lenge køen ikke er tom. while-løkken tar så ut en node fra køen og skriver dens verdi.
vi sjekker så for barn i noden og de blir lagt inn i køen. Siden køen operer på en first inn first out basis blir ikke nivå-orden forstyrret.

I deserialize() metoden opretter vi et nytt binær-tre, lagde så en for-each løkke som går gjennom vært element i det gitte arrayet og kjører leggInn() metoden på det.
Til slutt returneres treet.

I Oppgave 6 kodet jeg metodene fjern(T verdi), fjernAlle(T verdi) og nullstill().

Metoden fjern(T verdi) ble bygget ved hjelp av kildekode fra kompendiet 5.2 8 d), små endringer ble gjort for å sette riktig forhold mellom noder etter fjerning.

Metoden fjernAlle(T verdi) bruker metoden fjern i en while løkke. While løkken kjører så lenge støttevariablen "loop" er sann, loop settes av metoden fjern(T verdi)
som returnerer enten true eller false avhengig av om den har fjernet en verdi eller ikke. Om loop fortsatt er true etter at fjern(T verdi) er kjørt inkrementeres
antall fjernet. Når loop settes false er det ikke lenger flere instanser av verdien igjen i treet og vi returnerer antall fjernet.

Metoden nullstill() er ganske tungvint kodet da en kunna ha nullet antall og rot for så å la trashcollector sanke inn resten. 
Metoden benytter seg istedet av metodene førstePostorden() og nestePostorden() for å traversere treet. vi har en peker "p" og en forrige-peker "prev". p settes til første node i treets postorden
Metoden bruker en while løkke som kjører til p er null (i.e treet er tomt) prev settes til p og p flyttes til neste i postorden, alle elementer i prev nulles.
Når while-løkken er ferdig nuller vi rot og setter antall til 0.