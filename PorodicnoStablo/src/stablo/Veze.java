package stablo;

import java.util.HashMap;

public class Veze {
	public void getVeze(HashMap<Integer, Osoba> sviClanovi, int id, String veza){
        if(sviClanovi.containsKey(id)){
            Osoba osoba = sviClanovi.get(id);
            switch(veza){
                case "Cerka":
                    cerka(sviClanovi, osoba);
                    break;
                case "Sin":
                    sin(sviClanovi, osoba);
                    break;
                case "Majka":
                    majka(sviClanovi, osoba);
                    break;
                case "Otac":
                    otac(sviClanovi, osoba);
                    break;
                case "Brat":
                    brat(sviClanovi, osoba);
                    break;
                case "Sestra":
                    sestra(sviClanovi, osoba);
                    break;
                case "Partner":
                    partner(sviClanovi, osoba);
                    break;
                
                default:
                    System.out.println("Pogresan unos.");
            }
        } else 
            System.out.println("Osoba sa ID: "+ id +" nije pronadjena.");
    }
    
    private static void cerka(HashMap<Integer, Osoba> allMembers, Osoba osoba){
        String output = "";
        if (osoba.dole!=null){  // ako ima dece
            for( Osoba dete : osoba.dole.deca)
                if (dete.pol=="Zensko") output+= osoba.ime + " ima cerku: " + dete.ime + " " + dete.prezime + ", datum rodjenja: " + dete.datRodj + ", pol: " + dete.pol + ", ID: " + dete.id + " ";
        }
        if(output.length()>0)
            System.out.println(output.substring(0,output.length()-1));
        else
            System.out.println(osoba.ime + " Nema cerku.");
    }


    private static void sin(HashMap<Integer, Osoba> allMembers, Osoba osoba){
        String output = "";
        if (osoba.dole!=null){  // ako ima dece
            for( Osoba dete : osoba.dole.deca)
                if (dete.pol=="Musko") output+= osoba.ime + " ima sina: " + dete.ime + " " + dete.prezime + ", datum rodjenja: " + dete.datRodj + ", pol: " + dete.pol + ", ID: " + dete.id + " ";
        }
        if(output.length()>0)
            System.out.println(output.substring(0,output.length()-1));
        else
            System.out.println(osoba.ime + " Nema sina.");
    }


    private static void majka(HashMap<Integer, Osoba> allMembers, Osoba osoba){
        if(osoba.gore!=null)   // ako ima roditelja
            System.out.println(osoba.ime + " ima majku: " + osoba.gore.majka.ime + " " + osoba.gore.majka.prezime + ", datum rodjenja: " + osoba.gore.majka.datRodj + ", pol: " + osoba.gore.majka.pol + ", ID: " + osoba.gore.majka.id);
        else
            System.out.println(osoba.ime + " Nema majku.");
    }


    private static void otac(HashMap<Integer, Osoba> allMembers, Osoba osoba){
        if(osoba.gore!=null)    // ako ima roditelja
            System.out.println(osoba.ime + " ima oca: " + osoba.gore.otac.ime + " " + osoba.gore.otac.prezime + ", datum rodjenja: " + osoba.gore.otac.datRodj + ", pol: " + osoba.gore.otac.pol + ", ID: " + osoba.gore.otac.id);
        else
            System.out.println(osoba.ime + " Nema oca.");
    }


    private static void brat(HashMap<Integer, Osoba> allMembers, Osoba osoba){
        String output = "";
        if (osoba.gore != null){    // ako ima roditelja
            for( Osoba brat : osoba.gore.deca)
                if(brat!=osoba) output += osoba.ime + " ima brata: " + brat.ime + " " + brat.prezime + ", datum rodjenja: " + brat.datRodj + ", pol: " + brat.pol + ", ID: " + brat.id + " ";
        }
        if(output.length()>0)
            System.out.println(output.substring(0, output.length()-1));
        else 
            System.out.println(osoba.ime + " Nema brata.");
    }


    private static void sestra(HashMap<Integer, Osoba> allMembers, Osoba osoba){
        String output = "";
        if (osoba.gore != null){    // ako ima roditelja
            for( Osoba sestra : osoba.gore.deca)
                if(sestra!=osoba) output += osoba.ime + " ima sestru: " + sestra.ime + " " + sestra.prezime + ", datum rodjenja: " + sestra.datRodj + ", pol: " + sestra.pol + ", ID: " + sestra.id + " ";
        }
        if(output.length()>0)
            System.out.println(output.substring(0, output.length()-1));
        else 
            System.out.println(osoba.ime + " Nema sestre.");
    }
    
    private static void partner(HashMap<Integer, Osoba> allMembers, Osoba osoba){
    	String output = "";
        if (osoba.dole != null){    // ako ima partnera
            for( Osoba partner : osoba.par.partner)
                if(partner!=osoba) output += osoba.ime + " ima partnera: " + partner.ime + " " + partner.prezime + ", datum rodjenja: " + partner.datRodj + ", pol: " + partner.pol + ", ID: " + partner.id + " ";
        }
        if(output.length()>0)
            System.out.println(output.substring(0, output.length()-1));
        else 
            System.out.println(osoba.ime + " Nema partnera.");
    }
}
