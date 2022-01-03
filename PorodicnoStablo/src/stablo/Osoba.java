package stablo;

public class Osoba {
	
	private static int count = 0; 
	public int id;
	public String ime;
	public String prezime;
	public String pol;
	public Datum datRodj;
	public Familija gore;
	public Familija par;
	public Familija dole;
	
	public Osoba() {
		System.out.println("Kreirana je osoba sa nultim vrednostima.");
		this.id = 0;
		this.ime = "Inconnu";
		this.prezime = "Inconnu";
		this.pol = "Nepoznato";
		this.datRodj = null ;
	}
	
	public Osoba(String ime, String prezime, String pol, Datum datRodj) {
		this.id = ++count;
		System.out.println("Kreirana je osoba: " + ime + " " + prezime + ", rodjena: " + datRodj + ", pol: " + pol + ", ID: " + id);
		this.ime = ime;
		this.prezime = prezime;
		this.pol = pol;
		this.datRodj = datRodj;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		for (int i = 0; i < ime.length(); i++) {
			if (!Character.isLetter(ime.charAt(i))) {
				ime = "";
				System.err.println("NEVAZECE ime, sadrzi karaktere koji nisu slova.");
				break;
			}
		}
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		for (int i = 0; i < prezime.length(); i++) {
			if (!Character.isLetter(prezime.charAt(i))) {
				prezime = "";
				System.err.println("NEVAZECE prezime, sadrzi karaktere koji nisu slova.");
				break;
			}
		}
		this.prezime = prezime;
	}

	public String getPol() {
		return pol;
	}

	public void setPol(String pol) {
		if (pol == "Musko" || pol == "Zensko") {
			this.pol = pol;

		} else {
			this.pol = "Nepoznato";
			System.err.println("NEVAZECI pol : vrednost :" + pol);
		}
	}

	public Datum getDatRodj() {
		return datRodj;
	}

	public void setDatRodj(Datum datRodj) {
		this.datRodj = datRodj;
	}
	
	
}