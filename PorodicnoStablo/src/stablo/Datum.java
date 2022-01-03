package stablo;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Datum {
	private int dan;
	private int mesec;
	private int godina;
	
	public int getDan() {
		return dan;
	}
	public void setDan(int dan) {
		this.dan = dan;
	}
	public int getMesec() {
		return mesec;
	}
	public void setMesec(int mesec) {
		this.mesec = mesec;
	}
	public int getGodina() {
		return godina;
	}
	public void setGodina(int godina) {
		this.godina = godina;
	}
	
	public String toString() {
        StringBuilder s = new StringBuilder();
        if (this.dan < 10) s.append("0");
        s.append(String.valueOf(this.dan));
        s.append("-");
        if (this.mesec < 10) s.append("0");
        s.append(String.valueOf(this.mesec));
        s.append("-");
        s.append(String.valueOf(this.godina));
        return s.toString();
    }
	
	public static boolean validacija(int dan, int mesec, int godina) {
		if ((dan < 1) || (dan > 31)) return false;
        if ((mesec < 1) || (mesec > 12)) return false;
        if (godina < 0) return false;
        switch (mesec) {
            case 1: return true;
            case 2: return (prestupna(godina) ? dan <= 29 : dan <= 28);
            case 3: return true;
            case 4: return dan < 31;
            case 5: return true;
            case 6: return dan < 31;
            case 7: return true;
            case 8: return true;
            case 9: return dan < 31;
            case 10: return true;
            case 11: return dan < 31;
            default: return true;
        }
    }
	
	public static boolean prestupna(int godina) {
        if (godina % 4 != 0) {
          return false;
        } else if (godina % 400 == 0) {
          return true;
        } else if (godina % 100 == 0) {
          return false;
        } else {
          return true;
        }        
	}
	
	public Datum() {
        Calendar currentDate = Calendar.getInstance();
        java.util.Date x = currentDate.getTime();
        SimpleDateFormat formatdd = new SimpleDateFormat("dd");
        this.dan = Integer.parseInt(formatdd.format(x)); 
        SimpleDateFormat formatmonth = new SimpleDateFormat("MM");
        this.mesec = Integer.parseInt(formatmonth.format(x));     
        SimpleDateFormat formatyear = new SimpleDateFormat("yyyy");
        this.godina = Integer.parseInt(formatyear.format(x));
    }
	
	public Datum(int dan, int mesec, int godina) throws IllegalArgumentException{
        if (!validacija(dan, mesec, godina)) throw new IllegalArgumentException();
        this.dan = dan; 
        this.mesec = mesec;
        this.godina = godina;
    }
}
