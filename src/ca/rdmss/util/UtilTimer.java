package ca.rdmss.util;

public class UtilTimer {
	
	private static final double _1_000_000_000 = 1000000000.0;
	private static final double _1_000_000 = 1000000.0;
	private static final double _1_000 = 1000.0;

	public static double timeScaleMls(double mls){
		return timeScaleNs(mls*_1_000_000)*_1_000_000;
	}
	
	public static double timeScaleNs(double ns){
		switch( getOrderOfMagnitude(ns)){
		case  0: return 1.0;
		case  1: return 1.0;
		case  2: return 1.0;
		case  3: return 1.0/_1_000;
		case  4: return 1.0/_1_000;
		case  5: return 1.0/_1_000;
		case  6: return 1.0/_1_000_000;
		case  7: return 1.0/_1_000_000;
		case  8: return 1.0/_1_000_000;
		case  9: return 1.0/_1_000_000_000;
		case 10: return 1.0/_1_000_000_000;
		case 11: return 1.0/_1_000_000_000;
		default: return 1.0/_1_000_000_000;
		}
	}
	
	public static String timeUnitMls(double mls){
		return timeUnitNs(mls*_1_000_000);
	}
	
	public static String timeUnitNs(double ns){
		switch( getOrderOfMagnitude(ns)){
		case  0: return "ns";
		case  1: return "ns";
		case  2: return "ns";
		case  3: return "mks";
		case  4: return "mks";
		case  5: return "mks";
		case  6: return "mls";
		case  7: return "mls";
		case  8: return "mls";
		case  9: return "sec";
		case 10: return "sec";
		case 11: return "sec";
		default: return "sec";
		}
	}

	public static double timeValMls(double mls){
		return timeValNs(mls*_1_000_000);
	}
	
	public static double timeValNs(double ns){
		switch( getOrderOfMagnitude(ns)){
		case  0: return ns;
		case  1: return ns;
		case  2: return ns;
		case  3: return ns/_1_000;
		case  4: return ns/_1_000;
		case  5: return ns/_1_000;
		case  6: return ns/_1_000_000;
		case  7: return ns/_1_000_000;
		case  8: return ns/_1_000_000;
		case  9: return ns/_1_000_000_000;
		case 10: return ns/_1_000_000_000;
		case 11: return ns/_1_000_000_000;
		default: return ns/_1_000_000_000;
		}
	}


	public static int getOrderOfMagnitude(double d) {
		return new Long( new Double( Math.log10(d)).longValue()).intValue();
	}
}
