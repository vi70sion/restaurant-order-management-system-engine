Užduotis: Restorano užsakymų valdymo sistema su RabbitMQ, Redis ir MongoDB
Kontekstas:
Įsivaizduokite, kad kuriate restorano užsakymų valdymo sistemą. Sistema turi valdyti užsakymus, kurie siunčiami iš klientų, o virtuvės darbuotojai turi gauti ir apdoroti šiuos užsakymus. 
Užsakymai talpinami į eilių sistemą (RabbitMQ), duomenys laikinai saugomi talpykloje (Redis), o galutiniai užsakymai išsaugomi duomenų bazėje (MongoDB).

Užduoties aprašymas:

Sukurti Java aplikaciją, kuri:

Priima užsakymus per REST API ir siunčia juos į RabbitMQ eilę.
Virtuvės darbuotojai (RabbitMQ vartotojas) gauna užsakymus, juos apdoroja, ir jeigu duomenų nėra talpykloje (Redis), jie įrašomi į MongoDB.
Naudojama Redis talpykla siekiant laikinai saugoti apdorotus užsakymus, kad virtuvės darbuotojai galėtų greičiau gauti informaciją apie neseniai apdorotus užsakymus.

Sistemos reikalavimai:

RabbitMQ:

Sukurkite RabbitMQ siuntėją, kuris siunčia užsakymo informaciją į eilę (pvz., staliuko numeris, užsakytas maistas).
RabbitMQ vartotojas turi gauti šią užsakymo informaciją ir apdoroti (pvz., žymėti užsakymą kaip apdorotą).

Java Spring API:

Sukurkite REST API su Spring Boot, kur klientas gali:
Pateikti naują užsakymą per POST request.
Gauti užsakymų sąrašą (pavyzdžiui, užsakymų istorija, kas buvo užsakyta, ir kokia jų būsena).

Redis:

Naudokite Redis talpyklą tam, kad būtų galima greitai pasiekti užsakymus, kurie neseniai buvo apdoroti.
Talpyklos galiojimo laikas turi būti ribotas (pvz., 5 minutės).

MongoDB:

Saugo visą užsakymų istoriją. Kai užsakymas yra apdorojamas, jei jo nėra Redis, jis įrašomas į MongoDB.
Kiekvienas užsakymas turi turėti unikalų ID, klientą, staliuko numerį, užsakytą maistą ir būseną („nebaigtas“ arba „baigtas“).

Užduoties veiksmai:

RabbitMQ:

Sukurkite siuntėją, kuris siunčia užsakymo duomenis (užsakymas apima staliuko numerį, patiekalų sąrašą, laiką ir būseną).
Sukurkite vartotoją, kuris gauna šiuos užsakymus ir žymi juos kaip „paruošta“ arba „neparuošta“.

Java Spring API:

Sukurkite endpoint'ą POST requestams, kuris priima naujus užsakymus (pvz., staliuko numeris ir patiekalai).
Sukurkite GET endpoint'ą, kuris pateikia virtuvės darbuotojams sąrašą neapdorotų užsakymų.

Redis:

Sukurkite Redis talpyklą, kurioje laikomi neseniai apdoroti užsakymai, kad būtų greitesnis jų pasiekimas (pvz., užsakymo būsena arba detalės).

MongoDB:

Sukurkite MongoDB kolekciją, kurioje bus saugomi visi apdoroti užsakymai. Jei užsakymo nėra Redis, vartotojas turi tikrinti MongoDB.

Papildomos funkcijos:

Jei užsakymas užtrunka daugiau nei 30 minučių, jo būsena turi automatiškai keistis į „laiku neparuoštas“.
Sukurkite mechanizmą, kuris automatiškai ištrina senus užsakymus iš Redis po tam tikro laiko (pvz., 5 minutės).
Ši užduotis padės įvaldyti šiuolaikines technologijas ir kurti distribucines sistemas naudojant RabbitMQ, Redis, ir MongoDB bei sąveiką su jais per Spring Boot API.

 private String uzsakymoId;  // Unikalus užsakymo ID
    private String klientas;    // Kliento vardas arba identifikacija
    private int staliukoNr;     // Staliuko numeris
    private List<String> patiekalai;  // Užsakytų patiekalų sąrašas
    private String busena;      // Užsakymo būsena ("nebaigtas", "paruoštas", "laiku neparuoštas")
    private LocalDateTime uzsakymoLaikas;  // Laikas, kada užsakymas buvo pateiktas
    private LocalDateTime apdorojimoLaikas; 

Laukų aprašymas:

uzsakymoId: Unikalus ID, skirtas užsakymo identifikavimui (galima naudoti UUID arba kitą unikalų identifikatorių - pvz MongoDB ObjectId (JAVA - string)).

klientas: Kliento vardas arba identifikacija (galite naudoti, pavyzdžiui, telefono numerį ar kitą atpažinimo būdą).

staliukoNr: Staliuko numeris, kur sėdi klientas.

patiekalai: Užsakytų patiekalų sąrašas (List<String>), kiekviename sąrašo elemente pateikiamas patiekalo pavadinimas.

busena: Užsakymo būsena (pvz., „nebaigtas“, „paruoštas“, „laiku neparuoštas“).

uzsakymoLaikas: Laikas, kada užsakymas buvo pateiktas (naudojant LocalDateTime).

apdorojimoLaikas: Laikas, kada užsakymas buvo apdorotas (gali būti naudojamas, norint nustatyti, ar užsakymas buvo laiku apdorotas).


API - Sukurti uzsakymams perduoda engine per RabbitMQ - gauti užsakymams (Redis, MongoDB)

Engine - apdoroja užsakymus - įkelia į MongoDB ir Redis

API - taip pat naudojamas keisti užsakymo būseną (irgi per RabbitMQ) -> Engine pagauna žinutę ir pakeičia užsakymo būseną
