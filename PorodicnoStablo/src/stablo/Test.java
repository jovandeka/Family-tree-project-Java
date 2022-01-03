package stablo;

public class Test {

	public static void main(String[] args) {
		
		System.out.println(Datum.prestupna(2020) ? "2020 je prestupna godina.": "2020 nije prestupna godina.");
		
		Datum datum1 = new Datum(10,8,1973);
		Datum datum2 = new Datum(19,4,1973);
        Osoba otac = new Osoba("Dejan", "Jovanovic", "Musko", datum1);
        Osoba majka = new Osoba("Dragana", "Jovanovic", "Zensko", datum2);
        
        PorodicnoStablo stablo = new PorodicnoStablo(majka, otac);
        
        Datum datum3 = new Datum(7, 8, 1998);
        Osoba jovan = new Osoba("Jovan", "Jovanovic", "Musko", datum3);
        
        Datum datum4 = new Datum(4, 6, 2000);
        Osoba vanja = new Osoba("Vanja", "Markovic", "Zensko", datum4);
        
        Datum datum5 = new Datum(22, 8, 1996);
        Osoba jovana = new Osoba("Jovana", "Vuksanovic", "Zensko", datum5);
        
        Datum datum6 = new Datum(11, 7, 1991);
        Osoba petar = new Osoba("Petar", "Vuksanovic", "Musko", datum6);
        
        Datum datum7 = new Datum(18, 9, 2019);
        Osoba bogdan = new Osoba("Bogdan", "Vuksanovic", "Musko", datum7);
        
        stablo.addDete(2, jovan, true);
        stablo.addDete(2, jovana, true);
        
        stablo.addPartner(3, vanja);
        
        stablo.addPartner(5, petar);
        
        stablo.addDete(5, bogdan, true);
        
        stablo.getVeze(1, "Cerka");
        stablo.getVeze(1, "Sin");
        stablo.getVeze(1, "Partner");
        
        stablo.getVeze(2, "Cerka");
        stablo.getVeze(2, "Sin");
        stablo.getVeze(2, "Partner");
        
        stablo.getVeze(3, "Otac");
        stablo.getVeze(3, "Majka");
        stablo.getVeze(3, "Sestra");
        stablo.getVeze(3, "Partner");
        
        stablo.getVeze(4, "Partner");
        
        stablo.getVeze(5, "Otac");
        stablo.getVeze(5, "Majka");
        stablo.getVeze(5, "Brat");
        stablo.getVeze(5, "Partner");
        stablo.getVeze(5, "Sin");
        
        stablo.getVeze(6, "Partner");
        stablo.getVeze(6, "Sin");
        
        stablo.obrisiOsobu(4);
        
        stablo.getVeze(3, "Partner");
        stablo.getVeze(4, "Partner");
        
        stablo.obrisiOsobu(6);
        
        stablo.getVeze(5, "Partner");
        stablo.getVeze(5, "Sin");
        
        stablo.obrisiOsobu(3);
        
        stablo.getVeze(4, "Partner");
        stablo.getVeze(3, "Partner");
        stablo.getVeze(5, "Brat");
        stablo.getVeze(2, "Sin");
        stablo.getVeze(1, "Sin");
	}

}
