package stablo;

import java.util.HashMap;

public class PorodicnoStablo extends Veze{
    public Familija rootFamily;
    public HashMap<Integer, Osoba> sviClanovi = new HashMap<>();    // Da se lako pristupi svakoj osobi

	public PorodicnoStablo(Osoba majka, Osoba otac) {
		
        this.rootFamily = new Familija(majka, otac);
        otac.dole = rootFamily;
        majka.dole = rootFamily;
        otac.par = rootFamily;
        majka.par = rootFamily;

        otac.par.partner.add(majka);
        majka.par.partner.add(otac);
        this.sviClanovi.put(otac.id, otac);
        this.sviClanovi.put(majka.id, majka);
    }


    public void getVeze(int id, String veza){
        getVeze(this.sviClanovi, id, veza);
    }

    public void displayPorodicnoStablo(){
    	sviClanovi.forEach((id, osoba) -> {
            System.out.println("ID: " + id + " " + osoba.gore + " " + osoba.dole);
        });
    }

    
    public void addDete(int idMajke, Osoba dete, boolean silent){
        // silent je true ako se izvrsi dok se inicijalizuje stabllo
        if(this.sviClanovi.containsKey(idMajke)){
            Osoba majka = this.sviClanovi.get(idMajke);
            if (majka.pol=="Zensko" && majka.dole!=null){    // Ako ima partnera
            	majka.dole.deca.add(dete);
                dete.gore = majka.dole;
                this.sviClanovi.put(dete.id, dete);
                if (!silent)    System.out.println("Dodavanje deteta uspesno.");
                return;
            }
        }
        if (!silent)    System.out.println("Dodavanje deteta neuspesno.");
    }
    
    public void addPartner(int postojeciPartnerId, Osoba noviPartner){
        // validacija
        if(this.sviClanovi.containsKey(postojeciPartnerId) && !this.sviClanovi.containsKey(noviPartner.id)){
            Osoba postojeciPartner = this.sviClanovi.get(postojeciPartnerId);
            Familija familija = postojeciPartner.dole;
            if (familija == null){        // provera da li vec ima partnera
                
                if(postojeciPartner.pol=="Musko" && noviPartner.pol=="Zensko") {
                	familija = new Familija(noviPartner, postojeciPartner);
                }
                else if (postojeciPartner.pol=="Zensko" && noviPartner.pol=="Musko") {
                	familija = new Familija(postojeciPartner, noviPartner);
                }

                postojeciPartner.dole = familija;
                noviPartner.dole = familija;
                
                postojeciPartner.par = familija;
                noviPartner.par = familija;
                
                postojeciPartner.par.partner.add(noviPartner);
                noviPartner.par.partner.add(postojeciPartner);
                
                this.sviClanovi.put(noviPartner.id, noviPartner);
                return;
            }
        }
    }
    
    public void obrisiOsobu(int idOsobe){
    	if(this.sviClanovi.containsKey(idOsobe)) {
    		Osoba osoba = this.sviClanovi.get(idOsobe);
    		
    		if (osoba.par != null){
	    		Familija familija = osoba.par;
	            if (familija != null){
	                familija.partner.remove(osoba);
	                familija = null;
                }
	            osoba.par = null;
    		}
    		
    		if (osoba.dole != null){
    			Familija familija = osoba.dole;
	            if (familija != null) {
	            	for (Osoba dete : osoba.dole.deca)
		            	dete.gore = null;
	                osoba.dole.deca.remove(osoba);
	                familija.deca.remove(osoba);
	                familija = null;
                }
	            osoba.dole = null;
            }
    		
    		if (osoba.gore != null){
    			Familija familija = osoba.gore;
	            if (familija != null){   
	            	for (Osoba roditelj : osoba.gore.deca)
		            	roditelj.dole = null;
	                osoba.gore.deca.remove(osoba);
	                familija.deca.remove(osoba);
	                familija = null;
                }
	            osoba.gore = null;
            }
    		
    		this.sviClanovi.remove(osoba.id, osoba);
    		System.out.println("Uspesno je obrisana osoba sa ID: " + idOsobe + " " + osoba.ime);
    		osoba = null;
    		
    	}
    	else {
    		System.out.println("Greska pri brisanju osobe ili nepostojeci ID.");
    	}
    }
    
}
