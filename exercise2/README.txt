I denne oppgaven har jeg brukt wait og notify for � implementere producer-consumer i Java.
Det er customerQueue-klassen som har synchronized metoder "add" og "next", som tr�dene i Barber og Doorman kaller.
Disse metodene kaller "wait" helt til k�en ikke er full (for add) eller ikke tom (for next). Deretter notifyer den tr�den som har blitt kalt "wait", og den f�r kj�re.
Hele next og add er kritiske regioner, som markeres med n�kkelorder "synchronized". 
Barber og Doorman sover etter arbeid, innen et tilfeldig intervall.