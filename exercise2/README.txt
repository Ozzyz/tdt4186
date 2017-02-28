I denne oppgaven har jeg brukt wait og notify for å implementere producer-consumer i Java.
Det er customerQueue-klassen som har synchronized metoder "add" og "next", som trådene i Barber og Doorman kaller.
Disse metodene kaller "wait" helt til køen ikke er full (for add) eller ikke tom (for next). Deretter notifyer den tråden som har blitt kalt "wait", og den får kjøre.
Hele next og add er kritiske regioner, som markeres med nøkkelorder "synchronized". 
Barber og Doorman sover etter arbeid, innen et tilfeldig intervall.