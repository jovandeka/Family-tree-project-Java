package stablo;

import java.util.ArrayList;

public class Familija {
	public Osoba majka;
	public Osoba otac;
	public ArrayList<Osoba> partner;
	public ArrayList<Osoba> deca;

    public Familija(Osoba majka, Osoba otac) {
        this.majka = majka;
        this.otac = otac;
        this.partner = new ArrayList<Osoba>();
        this.deca = new ArrayList<Osoba>();
    }
}